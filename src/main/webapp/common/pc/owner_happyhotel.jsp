<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="jp.happyhotel.common.DBSync" %>
<%@ page import="jp.happyhotel.common.DateEdit" %>
<%@ page import="jp.happyhotel.common.RandomString" %>
<%@ page import="jp.happyhotel.common.DBConnection" %>
<%@ page import="jp.happyhotel.common.Logging" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="SyncGcp_ini.jsp" %>
<%
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    if (loginhotel == null)
    {
        loginhotel = ownerinfo.HotelId;
    }
    String  query;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String  loginid = "";
    int     userid = ownerinfo.DbUserId;

    if( loginhotel == null)
    {
        response.sendRedirect(ownerUrl);
    }
        // リファラーが無い場合は駄目よ。
    String paramReferer = request.getHeader("Referer");
    if( paramReferer == null )
    {
        paramReferer ="";
    }
    if (paramReferer.indexOf("owner.hotenavi.com") < 0)
    {
        response.sendRedirect(ownerUrl);
    }
    else
    {

        try
        {
            connection  = DBConnection.getConnection();
            query = "SELECT loginid,passwd_pc FROM owner_user WHERE hotelid = ? AND userid= ?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,loginhotel);
            prestate.setInt(2,userid);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
               loginid = result.getString("loginid");
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString());
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }

        int[] TimesAfterMin = DateEdit.addSec(Integer.parseInt(DateEdit.getDate(2)),Integer.parseInt(DateEdit.getTime(1)), 10);
        String token = DateEdit.getDate(2) + DateEdit.getTime(1) + RandomString.getRandomString(50);
        try
        {
            query = "REPLACE INTO hotenavi.owner_user_token SET";
            query = query + " token=?";
            query = query + ",operate_hotelid=?";
            query = query + ",operate_loginid=?";
            query = query + ",operate_userid=?";
            query = query + ",limit_date=?";
            query = query + ",limit_time=?";
            prestate   = connection.prepareStatement(query);
            prestate.setString(1, token);
            prestate.setString(2, loginhotel);
            prestate.setString(3, loginid);
            prestate.setInt(4, userid);
            prestate.setInt(5, TimesAfterMin[0]);
            prestate.setInt(6, TimesAfterMin[1]);
            prestate.executeUpdate();

            String sql = prestate.toString().split(":",2)[1];
            DBSync.publish(sql);
        }
        catch( Exception e )
        {
            Logging.error("foward Exception e=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(connection);
        }
%>
<form id=gcpOwnerLogin action="<%=ownerLoginUrl%>/owner_login_check.jsp" method="post">
<input type="hidden" name="token" value="<%=token%>">
<input type="hidden" name="operate_hotelid" value="<%=loginhotel%>">
<input type="hidden" name="operate_userid" value="<%=userid%>">
<input type="hidden" name="operate_loginid" value="<%=loginid%>">
</form>
<link href="../../common/pc/style/loading.css" rel="stylesheet" type="text/css" media="screen, print" />
<script type="text/javascript" src="../../common/pc/scripts/jquery.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/loading.js"></script>
<script>
dispLoading('ハッピー・ホテルオーナーサイトログイン中...');
setTimeout("document.getElementById(\"gcpOwnerLogin\").submit()",1000);
</script>
<%
    }
%>
