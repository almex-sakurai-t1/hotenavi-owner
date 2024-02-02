<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // �{���̓��t�擾
    Calendar calnd;
    calnd = Calendar.getInstance();
    int now_year   = calnd.get(calnd.YEAR);
    int now_month  = calnd.get(calnd.MONTH) + 1;
    int now_day    = calnd.get(calnd.DATE);
    int now_hour   = calnd.get(calnd.HOUR_OF_DAY);
    int now_minute = calnd.get(calnd.MINUTE);
    int now_second = calnd.get(calnd.SECOND);
    int now_date   = now_year * 10000 + now_month  * 100 + now_day;

    int    ret = -1;
    String oldpwd;
    String newpwd;
    String renewpwd;

    oldpwd   = request.getParameter("oldpassword");
    newpwd   = request.getParameter("newpassword");
    renewpwd = request.getParameter("newpassword2");

    for( ; ; )
    {
        // �V�����p�X���[�h�ƍē��̓p�X���[�h�̃`�F�b�N
        if( newpwd.compareTo(renewpwd) != 0 )
        {
%>
�V�����p�X���[�h�ƍē��͂����p�X���[�h���Ⴂ�܂��B
<%
            break;
        }

        // ���݂̃p�X���[�h�`�F�b�N
        if( oldpwd.compareTo(ownerinfo.DbPassword) != 0 )
        {
            // �p�X���[�h�Ⴂ
%>
���͂��ꂽ�l�p�X���[�h���Ⴂ�܂��B
<%
        }
        else
        {
            Connection connection = null;
            PreparedStatement prestate = null;
            ResultSet result = null;
            try
            {
                final String query = "UPDATE owner_user SET "
                                   + "passwd_pc = ?,"
                                   + "passwd_pc_update = ? "
                                   + "WHERE hotelid = ? AND userid = ?";

                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement(query);
                prestate.setString(1, newpwd);
                prestate.setInt(2, now_date);
                prestate.setString(3, session.getAttribute("HotelId") != null ? (String)session.getAttribute("HotelId") : "");
                prestate.setInt(4, ownerinfo.DbUserId);

                ret = prestate.executeUpdate();
            }
            catch( Exception e )
            {
                Logging.error("foward Exception e=" + e.toString(), e);
            }
            finally
            {
                DBConnection.releaseResources(prestate);
                DBConnection.releaseResources(connection);
            }

            if( ret != -1 )
            {
%>
�l�p�X���[�h�̕ύX���������܂����B
<%
            }
            else
            {
%>
�l�p�X���[�h�̕ύX�Ɏ��s���܂����B
<%
            }
        }

        break;
    }
%>
