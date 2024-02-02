<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String limit_flag = ReplaceString.getParameter(request,"limit_flag");
    if(limit_flag == null) limit_flag ="false";
    if(limit_flag.compareTo("") == 0) limit_flag ="false";
    String coupon_no  = ReplaceString.getParameter(request,"coupon_no");
    if    (coupon_no == null) coupon_no = "";
    String coupon_id  = ReplaceString.getParameter(request,"coupon_id");
    if    (coupon_id == null) coupon_id = "0";
    int    count = 0;

    Calendar cal = Calendar.getInstance();
    int now_year  = cal.get(cal.YEAR);
    int now_month = cal.get(cal.MONTH) + 1;
    int now_day   = cal.get(cal.DATE);
    int nowdate   = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime   = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);
    String  nowdate_s = Integer.toString(nowdate+100000000);
    String  nowtime_s = Integer.toString(nowtime+1000000);
    nowdate_s = nowdate_s.substring(1,5) + "/" + nowdate_s.substring(5,7) + "/" + nowdate_s.substring(7,9);
    nowtime_s = nowtime_s.substring(1,3) + ":" + nowtime_s.substring(3,5) + ":" + nowtime_s.substring(5,7);

    String hotelid = (String)session.getAttribute("SelectHotel");
    String            name  = "";
    String 			 query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection = DBConnection.getConnection();
    try
    {
        query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        result      = prestate.executeQuery();
        if( result.next() )
        {
           name = result.getString("name");
        }
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
/**    try
    {
        query = "SELECT count(hh_user_coupon.coupon_no) FROM hh_hotel_basic";
        query += " INNER JOIN edit_coupon";
        query += " ON edit_coupon.hotelid =hh_hotel_basic.hotenavi_id ";
        if(coupon_id.compareTo("0") != 0)
        {
            query = query + " AND edit_coupon.id =" + coupon_id;
        }
        if(limit_flag.compareTo("true") == 0)
        {
            query = query + " AND edit_coupon.start_date <= " + nowdate;
            query = query + " AND edit_coupon.end_date >= " +  nowdate;
        }
        query += " INNER JOIN hh_master_coupon";
        query += " ON hh_hotel_basic.id = hh_master_coupon.id";
        query += " AND edit_coupon.id = hh_master_coupon.coupon_no";
        query += " AND hh_master_coupon.service_flag=2";
        query += " INNER JOIN  hh_user_coupon";
        query += " ON hh_user_coupon.id = hh_hotel_basic.id";
        query += " AND hh_user_coupon.seq = hh_master_coupon.seq";
        query += " AND hh_user_coupon.hotenavi_flag = 1";
        if(coupon_no.compareTo("") != 0)
        {
            query += " AND (hh_user_coupon.coupon_no%10000)  =" + coupon_no;
        }
        if(limit_flag.compareTo("true") == 0)
        {
            query += " AND hh_user_coupon.end_date >= "   + nowdate;
        }
        query += " WHERE hh_hotel_basic.hotenavi_id ='"+ hotelid + "'";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery(query);
        if( result != null )
        {
            if( result.next() != false )
            {
               count = result.getInt(1);
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
**/
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>クーポンレポート</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="/common/pc/image/spacer.gif" width="100" height="6"></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0" id="disp" style="display:block">
            <tr>
              <td width="100" height="20" bgcolor="#22333F" class="tab" align="center"><font color="#FFFFFF"><%=name%></font></td>
              <td width="140" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF">クーポンレポート</font></td>
              <td width="15" height="20" valign="bottom"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
              <td width="200" height="20"  class="tab" align="right">
			  		<input type="checkbox" id="limit_flag" name="limit_flag" value="true" <% if (limit_flag.compareTo("true") == 0){%>checked<%}%> onclick="location.href='coupon_report.jsp?coupon_id=' + document.getElementById('coupon_id').value + '&coupon_no=' + document.getElementById('coupon_no').value + '&limit_flag='+document.getElementById('limit_flag').checked">有効期限内のみ
			  </td>
              <td height="20" align="right" class="tab">
                <div>
		  			クーポンNo:（どちらかを入力）
					<select name="coupon_id" id="coupon_id">
					<option value="0"></option>
<%
    try
    {
        query = "SELECT edit_coupon.id,edit_coupon.all_seq FROM edit_coupon,hh_master_coupon,hh_hotel_basic WHERE edit_coupon.hotelid=?";
        query = query + " AND hh_hotel_basic.hotenavi_id = edit_coupon.hotelid";
        query = query + " AND hh_hotel_basic.id = hh_master_coupon.id";
        query = query + " AND edit_coupon.id = hh_master_coupon.coupon_no";
        if(limit_flag.compareTo("true") == 0)
        {
            query = query + " AND edit_coupon.start_date <= " + nowdate;
            query = query + " AND edit_coupon.end_date >= " +  nowdate;
        }
        query = query + " AND hh_master_coupon.service_flag=2";
        query = query + " ORDER BY edit_coupon.id DESC";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery(query);
        while( result.next() )
        {
%>
			<option value="<%=result.getInt("edit_coupon.id")%>" <% if(result.getInt("edit_coupon.id")==Integer.parseInt(coupon_id)){%>selected<%}%>><%=result.getInt("edit_coupon.all_seq")%1000%></option>
<%
        }
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>
					</select>-<input type="text" size="4" maxlength="4" style="text-align:right" id="coupon_no" name="coupon_no" value="<%=coupon_no%>">
					<input type=button value="絞込" onclick="location.href='coupon_report.jsp?coupon_id=' + document.getElementById('coupon_id').value + '&coupon_no=' + document.getElementById('coupon_no').value + '&limit_flag='+document.getElementById('limit_flag').checked">
					<img src="/common/pc/image/spacer.gif" width="30" height="6"><input type=button value="PRINT" onclick="document.getElementById('disp').style.display='none';document.getElementById('print').style.display='block';print();document.getElementById('print').style.display='none';document.getElementById('disp').style.display='block';">
                     <img src="/common/pc/image/spacer.gif" width="30" height="6"><input type=button value="再読込" onclick="location.href='coupon_report.jsp?coupon_id=' + document.getElementById('coupon_id').value + '&coupon_no=' + document.getElementById('coupon_no').value + '&limit_flag='+document.getElementById('limit_flag').checked"></div>
              </td>
            </tr>
          </table>
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0" id="print" style="display:none">
            <tr>
              <td width="100" height="20" bgcolor="#22333F" class="tab" align="center"><font color="#FFFFFF"><%=name%></font></td>
              <td width="140" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF">クーポンレポート</font></td>
              <td width="15" height="20" valign="bottom"></td>
              <td height="20" align="right" class="tab">
                <div>
                     <%= nowdate_s %><img src="/common/pc/image/spacer.gif" width="3" height="20">
                     <%= nowtime_s %><img src="/common/pc/image/spacer.gif" width="15" height="20">
                </div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td bgcolor="#E2D8CF">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<div align="center">
			<tr>
				<td align="center">
				<table width="98%" border="0" cellspacing="1" cellpadding="0" >
           		 <tr>
              		<td align="right" valign="top" class="size12">
					<!--	<%=count%>件-->
              		</td>
            	</tr>
				</table>
				</td>
			</tr>
                <%-- ユーザ一覧表示 --%>
                <jsp:include page="coupon_report_disp.jsp" flush="true" >
                   <jsp:param name="limit_flag" value="<%= limit_flag %>" />
                   <jsp:param name="coupon_no"  value="<%= coupon_no %>" />
                   <jsp:param name="coupon_id"  value="<%= coupon_id %>" />
                </jsp:include>
                <%-- ユーザ一覧表示ここまで--%>
            <tr>
              <td height="4" valign="top">&nbsp;</td>
              <td align="left" valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
            </tr>
          </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="/common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="/common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="/common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="/common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="/common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="/common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="/common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><img src="/common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
