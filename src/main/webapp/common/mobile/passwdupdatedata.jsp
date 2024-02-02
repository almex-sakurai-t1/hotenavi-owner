<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // 本日の日付取得
    Calendar calnd;
    calnd = Calendar.getInstance();
    int now_year   = calnd.get(calnd.YEAR);
    int now_month  = calnd.get(calnd.MONTH) + 1;
    int now_day    = calnd.get(calnd.DATE);
    int now_hour   = calnd.get(calnd.HOUR_OF_DAY);
    int now_minute = calnd.get(calnd.MINUTE);
    int now_second = calnd.get(calnd.SECOND);
    int now_date   = now_year * 10000 + now_month  * 100 + now_day;

    int         ret =0;
    String      oldpwd;
    String      newpwd;
    String      renewpwd;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    oldpwd   = ReplaceString.getParameter(request,"oldpassword");
    newpwd   = ReplaceString.getParameter(request,"newpassword");
    renewpwd = ReplaceString.getParameter(request,"newpassword2");

    for( ; ; )
    {
        // 新しいパスワードと再入力パスワードのチェック
        if( newpwd.compareTo(renewpwd) != 0 )
        {
%>
新しいパスワードと再入力したパスワードが違います。
<%
            break;
        }

        // 現在のパスワードチェック
        if( oldpwd.compareTo(ownerinfo.DbPassword) != 0 )
        {
            // パスワード違い
%>
入力されたパスワードが違います。
<%
        }
        else
        {
            try
            {
                // パスワード書き換え
                final String query = "UPDATE owner_user SET passwd_mobile =? , "
                    + " passwd_mobile_update = ? "
                    + " WHERE hotelid = ?"
                    + " AND userid = ? ";

                connection  = DBConnection.getConnection();
                prestate    = connection.prepareStatement(query);

                prestate.setString(1, ReplaceString.SQLEscape(newpwd));
                prestate.setInt(2, now_date);
                prestate.setString(3, (String)session.getAttribute("HotelId"));
                prestate.setInt(4, ownerinfo.DbUserId);

                if (!ownerinfo.HotelId.equals("demo"))
                {
                    prestate.executeUpdate();
                }
                if( ret != -1 )
                {
    %>
    パスワードの変更が完了しました。
    <%
                }
                else
                {
    %>
    パスワードの変更に失敗しました。
    <%
                }

            }
            catch( Exception e )
            {
                Logging.error(e.toString(), e);
            }
            finally
            {
                DBConnection.releaseResources(result,prestate,connection);
            }
        }
        break;
    }
%>
