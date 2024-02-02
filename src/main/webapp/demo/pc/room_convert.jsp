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

    int[] t_room_no     = new int[128];
    int[] t_id          = new int[128];
    int[] t_img_count   = new int[128];
    int      trial_date = 99999999;
    int      count = -1;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    String query = "";

    query = "SELECT trial_date FROM hotel_element WHERE hotel_id= ?";
    prestate    = connection.prepareStatement(query);
    prestate.setString( 1, selecthotel);
    result      = prestate.executeQuery();
    if (result != null)
    {
        if( result.next() != false )
        {
            trial_date              	= result.getInt("trial_date");
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    query = " SELECT r_main.* FROM room r_main ";
    query += " WHERE";
    query += " r_main.hotelid = ?";
    query += " AND";
    query += " NOT EXISTS";
    query += " (SELECT 1 FROM room r_sub ";
    query += " WHERE r_main.hotelid=r_sub.hotelid ";
    query += " AND r_main.room_no = r_sub.room_no";
    query += " AND r_main.id < r_sub.id";
    query += " )";
    query += " AND r_main.start_date < ? ";
    query += " ORDER BY r_main.room_no ";

    prestate    = connection.prepareStatement(query);
    prestate.setString( 1, selecthotel);
    prestate.setInt( 2, trial_date);
    result      = prestate.executeQuery();
    if (result != null)
    {
        while( result.next() != false )
        {
            count++;
            t_id[count]   = result.getInt("id");
            t_room_no[count]   = result.getInt("room_no");
            t_img_count[count] = result.getString("image_pc").split("image/r").length - 1;
        }
    }
    DBConnection.releaseResources(result,prestate,connection);
%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>部屋情報一括変換</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
function MM_openInput(){ 
  document.form1.target = '_self';
  document.form1.action = 'room_convert_input.jsp';
  document.form1.submit();
}
function numCheck(obj,num){
  if (isNaN(obj.value))
  {
     obj.value=num;
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
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF">部屋情報一括変換</font></td>
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
                <input type="hidden" name=count value="<%=count+1%>">
                <table width="15%" border="0" cellspacing="0" cellpadding="0">
				  <tr align="left">
                      <td align="left" colspan="5" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="変換" onClick="MM_openInput();"></td>
                   </tr>
                  <tr align="left">
                    <td width="180" colspan=5><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>
				  <tr align="left">
                    <th class="size14 tableLN">部屋No</th>
                    <th class="size14 tableRN" width="60px">画像数</th>
                  </tr>
<%
    for (int i = 0; i <= count ; i++)
    {
%>
				  <tr align="left">
                    <td class="size14 tableLW"><%=t_room_no[i]%><input type=hidden name="room_no_<%=i%>" value="<%=t_room_no[i]%>"><input type=hidden name="id_<%=i%>" value="<%=t_id[i]%>"></td>
                    <td class="ac size14 tableRW"><input type="text" size=1 name="img_count_<%=i%>" value="<%=t_img_count[i]%>" class="ar" onchange"numCheck(this,<%=t_img_count[i]%>);"></td>
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
