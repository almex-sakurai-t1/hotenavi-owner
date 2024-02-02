<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    NumberFormat nf = new DecimalFormat("00");
    String loginHotelId = (String)session.getAttribute("LoginHotelId");

%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>管理店舗情報 店舗選択</title>
<script type="text/javascript">
function hideAdBar(){
setTimeout("scrollTo(0,1)", 100);
if (window.orientation ==0){document.body.className="portrait";}else{document.body.className="landscape";}
}
</script>
<link rel="stylesheet" type="text/css" href="../../common/smart/iphone_index.css">
<style>
.changeButton {
    width: 120px;
    height: 30px;
    color: #555555;
    font-size: 16px;
    vertical-align: -0.5em;
    margin: 8px;
}
.storeCheck {
    float: right;
    width: 20px;
    height: 20px;
}
.Selects {
    font-size:18px;
}
</style>
</head>
<body class="portrait" text="#555555" onLoad="hideAdBar()" onOrientationChange="hideAdBar();">
<jsp:include page="header.jsp" flush="true" />
<h2>☆管理店舗選択</h2>
<hr class="border">
<form name="form1" method="post" action="salestargetedit.jsp">
<input type="button" value="戻る" class="changeButton" onclick="document.form1.action='<%= URLEncoder.encode(request.getParameter("Contents") != null ? request.getParameter("Contents") : "", "Windows-31J") %>';document.form1.submit();">
<table class="uriage">
<tr>
<th>店舗名</th><th>対象</th>
</tr>
<%
    int i = 0;

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        connection = DBConnection.getConnection();

        String targetHotelId = request.getParameter("TargetHotelId");
        String salesDispFlag = request.getParameter("SalesDispFlag");
        if (targetHotelId != null && salesDispFlag != null)
        {
            try
            {
                final String query = "UPDATE owner_user_hotel SET sales_disp_flag = ? "
                                   + "WHERE hotelid = ? AND userid = ? AND accept_hotelid = ?";

                prestate = connection.prepareStatement(query);
                prestate.setInt(1, salesDispFlag.equals("false") ? 0 : 1);
                prestate.setString(2, loginHotelId);
                prestate.setInt(3, ownerinfo.DbUserId);
                prestate.setString(4, targetHotelId);

                prestate.executeUpdate();
            }
            catch( Exception e )
            {
                Logging.error("foward Exception e=" + e.toString(), e);
            }
            finally
            {
                DBConnection.releaseResources(prestate);
            }
        }

        try
        {
            final String query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
                               + "AND hotel.hotel_id = owner_user_hotel.accept_hotelid "
                               + "AND hotel.plan <= 2 "
                               + "AND hotel.plan >= 1 "
                               + "AND owner_user_hotel.userid = ? "
                               + "ORDER BY hotel.sort_num,hotel.hotel_id";

            prestate = connection.prepareStatement(query);
            prestate.setString(1, loginHotelId);
            prestate.setInt(2, ownerinfo.DbUserId);

            result = prestate.executeQuery();
            while(result.next())
            {
                String hotelid      = result.getString("hotel.hotel_id");
                String hotelname    = result.getString("hotel.name");
                int sales_disp_flag = result.getInt("owner_user_hotel.sales_disp_flag");
                i++;
%><tr>
    <td><%= hotelname %></td>
    <td><input class="selects" onclick="document.form1.TargetHotelId.value='<%= hotelid %>';document.form1.SalesDispFlag.value=this.checked;document.form1.submit();" type="checkbox" name="SalesDispFlag_<%= i %>" id="SalesDispFlag_<%= i %>" value="<%= hotelid %>" <%if(sales_disp_flag==1){%>checked<%}%>></td>
</tr>
<%
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
    }
    finally
    {
        DBConnection.releaseResources(connection);
    }
%>
</table>
<input name="Max"           type="hidden" value="<%= i %>">
<input name="TargetHotelId" type="hidden" value="">
<input name="SalesDispFlag" type="hidden" value="">
<% if (request.getParameter("Contents") != null) {%><input name="Contents" type="hidden" value="<%= URLEncoder.encode(request.getParameter("Contents"), "Windows-31J") %>"><%}%>
<% if (request.getParameter("Year")     != null) {%><input name="Year"     type="hidden" value="<%= URLEncoder.encode(request.getParameter("Year"),     "Windows-31J") %>"><%}%>
<% if (request.getParameter("Month")    != null) {%><input name="Month"    type="hidden" value="<%= URLEncoder.encode(request.getParameter("Month"),    "Windows-31J") %>"><%}%>
</form>
<hr class="border">
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
