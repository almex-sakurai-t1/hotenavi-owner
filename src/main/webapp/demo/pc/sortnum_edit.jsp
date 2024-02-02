<%@ page contentType="text/html;charset=Windows-31J"%><%@ page errorPage="error.jsp" %><%@ page import="java.sql.*" %><%@ page import="java.net.*" %>
<%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");
    if  (loginHotelId == null)
    {
         loginHotelId = "demo";
    }

    String     selecthotel   = "";
    selecthotel = request.getParameter("Store");
    if  (selecthotel == null)
    {
        selecthotel = (String)session.getAttribute("SelectHotel");
    }

    int[]    t_sort_num             = new int[100];
    String[] t_tenpoid              = new String[100];
    String[] t_hotel_id             = new String[100];
    String[] t_name                 = new String[100];
    int[]    t_plan                 = new int[100];
    int      count = -1;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    String query = "";
 
    String paramCount = request.getParameter("count");
    if (paramCount != null)
    {
        for (int i = 0; i <= Integer.parseInt(paramCount) ; i++)
        {
            query =  "SELECT hotel.hotel_id,hotel.sort_num,hotel.tenpoid FROM hotel WHERE hotel_id = ?";
            prestate    = connection.prepareStatement(query);
            prestate.setString( 1, request.getParameter("hotel_id_"+i));
            int sort_num = 0;
            String tenpoid ="";
            result      = prestate.executeQuery();
            if (result != null)
            {
                if( result.next() != false )
                {
                    sort_num =  result.getInt("sort_num");
                    tenpoid  =  result.getString("tenpoid");
                }
            }
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
            if (Integer.parseInt(request.getParameter("sort_num_"+i)) != sort_num || !request.getParameter("tenpoid_"+i).equals(tenpoid))
            {
                query =  "UPDATE hotel SET sort_num = ? ,tenpoid = ? WHERE hotel_id = ?";
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, Integer.parseInt(request.getParameter("sort_num_"+i)) );
                prestate.setString( 2, request.getParameter("tenpoid_"+i) );
                prestate.setString( 3, request.getParameter("hotel_id_"+i) );
                prestate.executeUpdate();
                DBConnection.releaseResources(prestate);
            }
        }
    }
 
    query =  "SELECT hotel.hotel_id,hotel.name,hotel.sort_num,hotel.tenpoid,hotel.plan FROM owner_user_hotel ouh";
    query += " INNER JOIN hotel ON  ouh.accept_hotelid = hotel.hotel_id";
    query += " WHERE ouh.hotelid = ? ";
    query += " AND hotel.plan IN (1,2,3,4)";
    query += " GROUP BY hotel.sort_num,hotel.hotel_id";
    prestate    = connection.prepareStatement(query);
    prestate.setString( 1, selecthotel);
    result      = prestate.executeQuery();

    if (result != null)
    {
        while( result.next() != false )
        {
            count++;
            t_hotel_id[count] = result.getString("hotel_id");
            t_tenpoid[count] = result.getString("tenpoid");
            t_name[count] = result.getString("name");
            t_sort_num[count] = result.getInt("sort_num");
            t_plan[count] = result.getInt("plan");
        }
    }
    DBConnection.releaseResources(result,prestate,connection);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>多店舗表示順設定定</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
function MM_openPreview(input,hotelid,rec_flag){
  if( input == 'preview' )
  {
    document.form1.target = '_blank';
    document.form1.action = 'etc_edit_preview.jsp?HotelId='+hotelid;
  }
  document.form1.submit();
}

function MM_openInput(){ 
  document.form1.target = '_self';
  document.form1.action = 'sortnum_edit.jsp';
  document.form1.submit();
}
function dateCheck(obj){
  if (isNaN(obj.value))
  {
     obj.value=99999999;
  }
}
</script>
<style>
td .size12 {
 padding:2px;
}
</style>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF">多店舗表示順設定</font></td>
              <td width="15" height="20"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="12">
              </td>
            </tr>
            <tr>
              <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><div class="size12">
                <form name=form1 method=post>
                <input type="hidden" name=count value="<%=count%>">
                <table width="60%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td colspan="5" class="size12"><font color="#CC0000"><strong>※このページを編集し終えたら、「更新」ボタンを必ず押してください</strong></font></td>
                  </tr>
				  <tr align="left">
                      <td align="left" colspan="5" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="更新" onClick="MM_openInput()"></td>
                   </tr>
                  <tr align="left">
                    <td width="180" colspan=5><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>
				  <tr align="left">
                    <th class="size14 tableLN">ホテナビID</th>
                    <th class="size14 tableLN">ホテル名</th>
                    <th class="size14 tableLN">プラン</th>
                    <th class="size14 tableLN" width="70px">店舗No.</th>
                    <th class="size14 tableRN" width="70px">表示順</th>
                  </tr>
<%
    for (int i = 0; i <= count ; i++)
    {
%>
				  <tr align="left">
                    <td class="size14 tableLW"><%=t_hotel_id[i]%><input type=hidden name="hotel_id_<%=i%>" value="<%=t_hotel_id[i]%>"></td>
                    <td class="size14 tableLW"><%=t_name[i]%></td>
                    <td class="size14 tableLW"><%if(t_plan[i]==1){%>カスタム<%}else if(t_plan[i]==2){%>オーナー<%}else if(t_plan[i]==3){%>エクストラ<%}else if(t_plan[i]==4){%>オフライン<%}%></td>
                    <td class="ac size14 tableLW"><input type="text" size=3 name="tenpoid_<%=i%>" value="<%=t_tenpoid[i]%>" class="ar"></td>
                    <td class="ac size14 tableRW"><input type="text" size=3 name="sort_num_<%=i%>" value="<%=t_sort_num[i]%>" class="ar"></td>
                  </tr>
<%
    }
%>
                  <tr align="left">
                    <td width="180" colspan=5><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>
                </table>
                </form>  
              </td>
              </tr>
              <tr>
                <td valign="top">&nbsp;</td>
                <td valign="top"></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- ここまで -->
    </table></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/owner/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18"><img src="../../common/pc/image/spacer.gif" width="12" height="18" align="absmiddle">Copyrigtht&copy; imedia
    inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
