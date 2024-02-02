<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.Constants" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page import="jp.happyhotel.common.DBSync" %>
<%@ page import="jp.happyhotel.common.DateEdit" %>
<%@ page import="jp.happyhotel.common.RandomString" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<%@ page import="jp.happyhotel.util.Base64Manager" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../csrf/refererCheck.jsp" %>
<%@ include file="../csrf/checkCsrfForPost.jsp" %>
<%@ include file="../../common/pc/SyncGcp_ini.jsp" %>
<%
   // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
        String   loginHotelId =  (String)session.getAttribute("LoginHotelId");
        String   param_userid = ReplaceString.getParameter(request,"UserId");
        if(param_userid == null) param_userid = "0";

        String   query;
        int      i;
        int      userid = 0;
        int      level[] = new int[30];
        int      admin_flag = 0;
        int      imedia_user = 0;
        int      report_flag = 0;
        int      count;

//���A�h�~�j�X�g���[�^�̏��
        int      admin_userid      = 0;
        int      admin_admin_flag  = 0;
        int      admin_report_flag = 0;
        int      admin_level[]     = new int[30];
        String   admin_loginid     = "";
        String   admin_name        = "";

//�Ǘ��X�܃e�[�u��
        String   target_hotel[]    = new String[200];

        String loginid = "";
        String machineid = "";
        String name = "";
        String passwd_pc = "";
        String passwd_mobile = "";
        int    passwd_pc_update     = 0;
        int    passwd_mobile_update = 0;
        String mailaddr_pc = "";
        String mailaddr_mobile = "";
        int    user_level = 1;
        int    unknown_flag_pc     = 0;
        int    unknown_flag_mobile = 0;

        Connection db   = null;
        PreparedStatement  st   = null;
        ResultSet  ret  = null;
        Connection        dbh  = null;
        PreparedStatement sth  = null;
        ResultSet         reth = null;
        Connection        dbm  = null;
        PreparedStatement stm  = null;
        ResultSet         retm = null;
        Connection        dbc  = null;
        PreparedStatement stc  = null;
        ResultSet         retc = null;

        NumberFormat nf = new DecimalFormat("00");

        db = DBConnection.getConnection();
         // ���͒S���҂̃Z�L�����e�B���擾
        int    input_admin_flag  = 0;
        int    input_imedia_user = 0;
        String input_login_id    = "";
        query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid=?";
        query = query + " AND (owner_user.level=1 OR owner_user.level=3)";
        query = query + " AND owner_user.userid=?";
        query = query + " AND owner_user_security.hotelid = owner_user.hotelid";
        query = query + " AND owner_user_security.userid = owner_user.userid";
        try
        {
            st =db.prepareStatement(query);
            st.setString(1, loginHotelId);
            st.setInt(2, ownerinfo.DbUserId);
            ret = st.executeQuery();
            if( ret.next() )
            {
                input_imedia_user = ret.getInt("owner_user.imedia_user");
                input_admin_flag  = ret.getInt("owner_user_security.admin_flag");
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(ret);
            DBConnection.releaseResources(st);
        }

//administrator�ł͂Ȃ��A�Ώۂ��������g�łȂ���΃G���[
        if (input_admin_flag == 0  && ownerinfo.DbUserId != Integer.parseInt(param_userid))
    {
%>
        <jsp:forward page="../../common/pc/timeout.html" />
<%
    }
        int    sales_userid = 0;
        String sales_mailaddr_pc  = "";
        String sales_mailaddr_mobile = "";

        // ���[�U�ꗗ�E�Z�L�����e�B���擾
        if ( Integer.parseInt(param_userid) != 0)
        {
            query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid=?";
            query = query + " AND owner_user.userid=?";
            query = query + " AND owner_user_security.hotelid = owner_user.hotelid";
            query = query + " AND owner_user_security.userid = owner_user.userid";
            try
            {
                st =db.prepareStatement(query);
                st.setString(1, loginHotelId);
                st.setInt(2, Integer.parseInt(param_userid));
                ret = st.executeQuery();
                if(ret.next());
                {
                    userid          = ret.getInt("owner_user.userid");
                    loginid         = ret.getString("owner_user.loginid");
                    machineid       = ret.getString("owner_user.machineid");
                    name            = ret.getString("owner_user.name");
                    passwd_pc       = ret.getString("owner_user.passwd_pc");
                    passwd_mobile   = ret.getString("owner_user.passwd_mobile");
                    mailaddr_pc     = ret.getString("owner_user.mailaddr_pc");
                    mailaddr_mobile = ret.getString("owner_user.mailaddr_mobile");
                    imedia_user     = ret.getInt("owner_user.imedia_user");
                    user_level      = ret.getInt("owner_user.level");
                    report_flag     = ret.getInt("owner_user.report_flag");
                    admin_flag      = ret.getInt("owner_user_security.admin_flag");
                    unknown_flag_pc = ret.getInt("owner_user.unknown_flag_pc");
                    unknown_flag_mobile = ret.getInt("owner_user.unknown_flag_mobile");
                    passwd_pc_update     = ret.getInt("owner_user.passwd_pc_update");
                    passwd_mobile_update = ret.getInt("owner_user.passwd_mobile_update");

                    for( i = 0 ; i < 30 ; i++ )
                    {
                        level[i]    = ret.getInt("owner_user_security.sec_level" + nf.format(i+1));
                    }
                }
            }
             catch( Exception e )
             {
                 Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
             }
             finally
             {
             	DBConnection.releaseResources(ret);
             	DBConnection.releaseResources(st);
             }
        }

        if (user_level != 2)
        {
            //���チ�[����pID�����邩���ׂ�
            query = "SELECT * FROM owner_user WHERE owner_user.hotelid=?";
            query = query + " AND owner_user.level=2";
            query = query + " AND owner_user.loginid=?";
            try
            {
                 st =db.prepareStatement(query);
            	 st.setString(1, loginHotelId);
                 st.setString(2, loginid);
                 ret = st.executeQuery();
                 if( ret.next() )
                  {
                   sales_userid          = ret.getInt("owner_user.userid");
                   sales_mailaddr_pc     = ret.getString("owner_user.mailaddr_pc");
                   sales_mailaddr_mobile = ret.getString("owner_user.mailaddr_mobile");
                  }
            }
            catch( Exception e )
            {
                Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
            }
            finally
            {
            	DBConnection.releaseResources(ret);
            	DBConnection.releaseResources(st);
            }
        }

        // admin�����҂̃Z�L�����e�B���y�ъǗ��z�e���ꗗ�擾
        query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid=?";
        query = query + " AND owner_user_security.hotelid = owner_user.hotelid";
        query = query + " AND owner_user_security.userid = owner_user.userid";
        query = query + " AND owner_user.imedia_user=0";
        query = query + " AND owner_user_security.admin_flag=1";
        st =db.prepareStatement(query);
        st.setString(1, loginHotelId);
        ret = st.executeQuery();
        if( ret.next() )
        {
            admin_userid          = ret.getInt("owner_user.userid");
            admin_loginid         = ret.getString("owner_user.loginid");
            admin_name            = ret.getString("owner_user.name");
            admin_report_flag     = ret.getInt("owner_user.report_flag");
            admin_admin_flag      = ret.getInt("owner_user_security.admin_flag");
            for( i = 0 ; i < 30 ; i++ )
            {
                admin_level[i]    = ret.getInt("owner_user_security.sec_level" + nf.format(i+1));
            }
            query = "SELECT owner_user_hotel.*,hotel.name FROM owner_user_hotel,hotel";
            query = query + " WHERE owner_user_hotel.hotelid= ? ";
            query = query + " AND owner_user_hotel.userid= ? ";
            query = query + " AND owner_user_hotel.accept_hotelid=hotel.hotel_id";
            sth   = db.prepareStatement(query);
            sth.setString(1, loginHotelId);
            sth.setInt(2, admin_userid);
            reth  = sth.executeQuery();
        }
        else
        {
                reth = null;
        }


    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    int[] TimesAfterMin = DateEdit.addSec(Integer.parseInt(DateEdit.getDate(2)),Integer.parseInt(DateEdit.getTime(1)), 300);
    String token = DateEdit.getDate(2) + DateEdit.getTime(1) + RandomString.getRandomString(50);
    try
    {
        connection  = DBConnection.getConnection();
        query = "INSERT INTO hotenavi.owner_user_token SET";
        query = query + " token=?";
        query = query + ",operate_hotelid=?";
        query = query + ",operate_loginid=?";
        query = query + ",operate_userid=?";
        query = query + ",update_hotelid=?";
        query = query + ",update_userid=?";
        query = query + ",limit_date=?";
        query = query + ",limit_time=?";
        prestate   = connection.prepareStatement(query);
        prestate.setString(1, token);
        prestate.setString(2, loginHotelId);
        prestate.setString(3, ownerinfo.DbLoginUser);
        prestate.setInt(4, ownerinfo.DbUserId);
        prestate.setString(5, loginHotelId);
        prestate.setInt(6, userid);
        prestate.setInt(7, TimesAfterMin[0]);
        prestate.setInt(8, TimesAfterMin[1]);
        prestate.executeUpdate();

        String sql = prestate.toString().split(":",2)[1];
        if (apiUrl.indexOf("stg") != -1)
        {
            DBSync.publish(sql,true);
        }
        else
        {
            DBSync.publish(sql);
        }
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>�}�X�^�[�ݒ�</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/jquery-latest.js"></script>
<script>
    $(document).ready(function () {
        window.history.pushState("", "", "#");
    });
    window.addEventListener("popstate", function (e) {
        location.href = "../../common/pc/owner_userNew.jsp"; 
    })
</script></head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<form action="<% if (loginHotelId.equals("demo") && admin_admin_flag==1 && admin_userid == userid){%>#<%}else{%>user_input.jsp<%}%>" method="post" name="userform" id="userform">
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#E2D8CF">
<tr>
   	<td valign="top" bgcolor="#E2D8CF" width="100%">
	   	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	    <tr>
    	    <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
      	</tr>
    	</table>
    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
      	<tr>
       		<td >
          		<table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
					<tr>
 	  				<td width="60%" align="center" valign="top" style="padding-left:10px" >
			   			<table width="100%" border="0" cellspacing="0" cellpadding="0">

		           			<tr>
<%
    if( userid == 0 )
    {
%>
                       			<td width="140" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF">�}�X�^�[�ݒ�i�V�K�j</font></td>
<%
    }
    else
    {
%>
                      			<td width="140" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF">�}�X�^�[�ݒ�i�ύX�j</font></td>
<%
    }
%>
                      			<td height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20">

							</td>
							</tr>

							<tr>
								<td class="tableLN" width="150">���[�U�[ID</td>
								<td class="tableWhite">
									<input name="UserId"   type="hidden" class="tableWhite" id="userid"   value="<%= userid %>"   size="24" maxlength="10"><% if (userid != 0){%><%= userid %><%}else{%>�V�K�o�^<%}%>
<% if (input_admin_flag == 1){%>
									�y��1�Ǘ���
<%
       if (admin_admin_flag==1)
       {
           if (admin_userid != userid)
		   {
%>
									�F<a href="user_form.jsp?UserId=<%=admin_userid%>"><%=admin_userid%>:<%=admin_name%>(<%=admin_loginid%>)</a>
<%
           }
       }
       else
       {
%>
									�F�o�^�Ȃ�
<%
       }
%>									�z
<%}%>
								</td>
							</tr>
							<tr>
								<td class="tableLN" width="130">���O</td>
								<td class="tableWhite"><input name="name" type="text" class="tableWhite" id="name" value="<%= name %>" size="24" maxlength="20" style="ime-mode: active;" autocomplete="off"><font color=red>[�K�{]</font></td>
							</tr>
							<tr>
								<td class="tableLN" width="130">�l���[�U�[��</td>	<td class="tableWhite">
	<% if (input_admin_flag == 1){%>
									<input name="loginid" type="text" class="tableWhite" id="loginid" value="<%= loginid %>" size="24" maxlength="10" style="ime-mode: disabled;" autocomplete="off">�i���p�p��10�����ȓ��j</font><font color=red>[�K�{]</font></div>
	<%}else{%>
									<input name="loginid" type="hidden" class="tableWhite" id="loginid" value="<%= loginid %>" size="24" maxlength="10" style="ime-mode: disabled;" autocomplete="off"><%= loginid %>
	<%}%>
									<input name="user_level" type="hidden"  id="user_level" value="<%= user_level %>">
								</td>
							</tr>
	<% if (user_level == 2){ %>
								<input name="passwd_pc" type="hidden" class="tableWhite" id="passwd_pc" value="">
								<input name="passwd_pc_re" type="hidden" class="tableWhite" id="passwd_pc_re" value="">
								<input name="passwd_mobile" type="hidden" class="tableWhite" id="passwd_mobile" value="">
								<input name="passwd_mobile_re" type="hidden" class="tableWhite" id="passwd_mobile_re" value="">
	<%}else if(ownerinfo.DbUserId == userid){%>
							<tr>
								<td class="tableLN" width="130">�l�p�X���[�h</td>
								<td class="tableWhite"><input name="passwd_pc" type="password" class="tableWhite" id="passwd_pc" value="<%= passwd_pc %>" size="<%= Constants.MAXIMUM_PASSWORD_LENGTH %>" maxlength="<%= Constants.MAXIMUM_PASSWORD_LENGTH %>" style="ime-mode: disabled;" autocomplete="off"><font color=red>[�K�{]</font><div>���p��̑啶���E�������E�����E�L���̒�����Œ�2��ނ�g�ݍ��킹<br>�����p�p��<%=Constants.MINIMUM_PASSWORD_LENGTH%>�����ȏ�<%= Constants.MAXIMUM_PASSWORD_LENGTH %>�����ȓ�</div>
								<%if (passwd_pc_update == 0 && !passwd_pc.equals("")){%><font size=1 color=red>���p�X���[�h�ł��̂ŁA�C�����Ă��������B</font><%}%>
								</td>
							</tr>
							<tr>
								<td class="tableLN" width="130">�l�p�X���[�h(�ēx)</td>
								<td class="tableWhite"><input name="passwd_pc_re" type="password" class="tableWhite" id="passwd_pc_re" value="<%= passwd_pc %>" size="<%= Constants.MAXIMUM_PASSWORD_LENGTH %>" maxlength="<%= Constants.MAXIMUM_PASSWORD_LENGTH %>" style="ime-mode: disabled;" autocomplete="off">���R�s�[�����ɍēx���͂��Ă��������B</td>
							</tr>
							<tr>
								<td class="tableLN" width="130">�g�уp�X���[�h</td>
								<td class="tableWhite"><input name="passwd_mobile" type="password" class="tableWhite" id="passwd_mobile" value="<%= passwd_mobile %>" size="20" maxlength="4" style="ime-mode: disabled;" autocomplete="off">�i���p�p��4�����ȓ��j
								<%if (passwd_mobile_update == 0 && !passwd_mobile.equals("")){%><br><font size=1 color=red>���p�X���[�h�ł��̂ŁA�C�����Ă��������B</font><%}%>
								</td>
							</tr>
							<tr>
								<td class="tableLN" width="130">�g�уp�X���[�h(�ēx)</td>
								<td class="tableWhite"><input name="passwd_mobile_re" type="password" class="tableWhite" id="passwd_mobile_re" value="<%= passwd_mobile %>" size="20" maxlength="4" style="ime-mode: disabled;" autocomplete="off">���R�s�[�����ɍēx���͂��Ă��������B</td>
							</tr>
	<%}else{%>
								<input name="passwd_pc" type="hidden" class="tableWhite" id="passwd_pc" value="<%= passwd_pc %>">
								<input name="passwd_pc_re" type="hidden" class="tableWhite" id="passwd_pc_re" value="<%= passwd_pc %>">
								<input name="passwd_mobile" type="hidden" class="tableWhite" id="passwd_mobile" value="<%= passwd_mobile %>">
								<input name="passwd_mobile_re" type="hidden" class="tableWhite" id="passwd_mobile_re" value="<%= passwd_mobile %>">
							<tr>
								<td class="tableLN" width="130">�l�p�X���[�h</td>
								<td class="tableWhite">
								<%if (passwd_pc.equals("")){if(userid==0){%>�p�X���[�h�͎������s����܂��y���Ӂz<%}else{%>�p�X���[�h�͐ݒ肳��Ă��܂���<%}} else if (passwd_pc_update == 0){%>�i<font color=red>���p�X���[�h��Ԃł�</font>�j<%}else{%>�@�ݒ��<%}%>
								<%if (passwd_pc.equals("")){if(userid==0){%><input type=hidden name=passwd_pc_new value="1"><%}else{%><input type=checkbox name=passwd_pc_new value="1">�p�X���[�h�𔭍s���܂��y���Ӂz<%}} else {%><input type=checkbox name=passwd_pc_new value="1">�p�X���[�h���Ĕ��s����y���Ӂz<%}%>
								</td>
							</tr>
							<tr>
								<td class="tableLN" width="130">�g�уp�X���[�h</td>
								<td class="tableWhite">
								<%if (passwd_mobile.equals("")){if(userid==0){%>�p�X���[�h�͎������s����܂��y���Ӂz<%}else{%>�p�X���[�h�͐ݒ肳��Ă��܂���<%}} else if (passwd_mobile_update == 0){%>�i<font color=red>���p�X���[�h��Ԃł�</font>�j<%}else{%>�@�ݒ��<%}%>
								<%if (passwd_mobile.equals("")){if(userid==0){%><input type=hidden name=passwd_mobile_new value="1"><%}else{%><input type=checkbox name=passwd_mobile_new value="1">�p�X���[�h�𔭍s���܂��y���Ӂz<%}} else {%><input type=checkbox name=passwd_mobile_new value="1">�p�X���[�h���Ĕ��s����y���Ӂz<%}%>
								</td>
							</tr>
	<%}%>
							<tr>
								<td class="tableLN" width="130">PC���[���A�h���X</td>
								<td class="tableWhite">
									<input name="mailaddr_pc" type="text" class="tableWhite" id="mailaddr_pc" value="<%= mailaddr_pc %>" size="40" maxlength="80" style="ime-mode: disabled;" autocomplete="off">�i���p�p���j<% if(unknown_flag_pc==1){%><font color="#FF0000">&nbsp;Unknown</font><%}%>
									<input name="sales_userid"      type="hidden" value="<%= sales_userid %>">
									<input name="sales_mailaddr_pc" type="hidden" value="<%= sales_mailaddr_pc %>">
	<% if (sales_mailaddr_pc.compareTo("") != 0){%>
									<br>�i���チ�[����p�F<%= sales_mailaddr_pc %>�j
		<% if (input_admin_flag == 1){%>
									<a href="user_form.jsp?UserId=<%= sales_userid %>">�C��</a>&nbsp;<a href="user_delete.jsp?UserId=<%= sales_userid %>">�폜</a>
		<%}%>
	<%}%>
	<% if (input_admin_flag == 0 && report_flag == 1){%>
									<font color=brown><br>���C����̃��[���A�h���X�ɔ��チ�[���͓͂��܂���B<br>�����チ�[���̏C���́A�Ǘ��ҁi<%=admin_name%>�l�j�ɂ��˗����������B</font>
	<%}%>
								</td>
							</tr>
							<tr>
								<td class="tableLN" width="130">�g�у��[���A�h���X</td>
								<td class="tableWhite">
									<input name="mailaddr_mobile" type="text" class="tableWhite" id="mailaddr_mobile" value="<%= mailaddr_mobile %>" size="40" maxlength="80" style="ime-mode: disabled;" autocomplete="off">�i���p�p���j<% if(unknown_flag_mobile==1){%><font color="#FF0000">&nbsp;Unknown</font><%}%>
									<input name="sales_mailaddr_mobile" type="hidden" value="<%= sales_mailaddr_mobile %>">
	<% if (sales_mailaddr_mobile.compareTo("") != 0){%>
									<br>�i���チ�[����p�F<%= sales_mailaddr_mobile %>�j
		<% if (input_admin_flag == 1){%>
									<a href="user_form.jsp?UserId=<%= sales_userid %>">�C��</a>&nbsp;<a href="user_delete.jsp?UserId=<%= sales_userid %>">�폜</a>
		<%}%>
	<%}%>
	<% if (input_admin_flag == 0 && report_flag == 1){%>
									<font color=brown><br>���C����̃��[���A�h���X�ɔ��チ�[���͓͂��܂���B<br>�����チ�[���̏C���́A�Ǘ��ҁi<%=admin_name%>�l�j�ɂ��˗����������B</font>
	<%}%>
								</td>
							</tr>

	<% if (user_level != 2){%>
							<tr>
								<td class="tableLN" width="130">�g�ѐ����ԍ�</td>
		<% if (input_admin_flag == 1){%>
								<td class="tableWhite"><input name="machineid" type="text" class="tableWhite" id="machineid" value="<%= machineid %>" size="40" maxlength="50" style="ime-mode: disabled;" autocomplete="off">�i���p�p���j</td>
		<%}else{%>
								<td class="tableWhite"><input name="machineid" type="hidden" class="tableWhite" id="machineid" value="<%= machineid %>" size="40" maxlength="50" style="ime-mode: disabled;" autocomplete="off"><%= machineid %></td>
		<%}%>
							</tr>
	<%}else{%>
								<input name="machineid" type="hidden" class="tableWhite" id="machineid" value="<%= machineid %>" size="40" maxlength="50" style="ime-mode: disabled;" autocomplete="off">
	<%}%>
							<input name="imedia_user" type="hidden" id="imedia_user" value="<%= imedia_user %>">
    <% if (admin_report_flag == 1){%>
							<tr>
								<td class="tableLN" width="130">���チ�[���ݒ�</td>
		<% if (input_admin_flag == 1 && admin_userid != userid){%>
								<td  class="tableWhite"><input name="report_flag" type="checkbox" id="report_flag" value="1"  <% if( report_flag == 1 ) {%> checked <%}%>>���[�U�[�Ǘ��̃��[���ݒ�ɂ�蔄�チ�[������M����܂��B</td>
		<%}else{%>
								<td class="tableWhite"><input name="report_flag" type="hidden" id="report_flag" value="<%=report_flag%>">���[�U�[�Ǘ��̃��[���ݒ�ɂ�蔄�チ�[������M����܂��B</td>
		<%}%>
							</tr>
    <%}else{%>
								<input name="report_flag" type="hidden" id="report_flag" value="<%=report_flag%>">
    <%}%>
						</table>
			   			<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr valign=top>
								<td class="size10"  valign=middle>
	<% if(ownerinfo.DbUserId != userid){%>
							<br>
							<strong>�y���Ӂz�p�X���[�h�𔭍s����ꍇ</strong><br>
							<font color=red><strong>�m�l�p�X���[�h�n</strong></font><br>
							�u�l���[�U�[���v�u�l�p�X���[�h�v�����͂���Ă���PC���[���A�h���X�ɑ��M����܂��B<br>
							<input type=checkbox name=passwd_pc_no value="1">���M���Ȃ��ꍇ�̓`�F�b�N���Ă��������B<br>
							<input type=checkbox name=passwd_pc_network value="1">�u�l�b�g���[�N���[�U�[���v�y�сu�l�b�g���[�N�p�X���[�h�v�����M����ꍇ�̓`�F�b�N���Ă��������B<br>
							<br>
							<font color=red><strong>�m�g�уp�X���[�h�n</strong></font><br>
							�u�g�уp�X���[�h�v�����͂���Ă���g�у��[���A�h���X�ɑ��M����܂��B<br>
							<input type=checkbox name=passwd_mobile_no value="1">���M���Ȃ��ꍇ�̓`�F�b�N���Ă��������B<br>
							<br>
							<strong>�m�e�Ǘ��T�C�gURL���t�n</strong><br>
							<input type=checkbox name=target_system_hotenavi value="hotenavi" checked>�z�e�i�r
							<input type=checkbox name=target_system_hapihote value="hapihote" checked>�n�s�z�e
							<input type=checkbox name=target_system_reserve value="reserve" checked>�\��
	<%}%>
							</td>
							</tr>
						</table>
					</td>

					<td width="40%" valign=top>
						<table cellpadding="0" cellspacing="1" border="0"  width="100%" class="search_hoteldetail" valign=top>
						<tr valign=top>
						<td>
						<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search_hoteldetail" valign=top>
				        <tr valign=top>   <td colspan=3  align="right" valign=top>
						<input type=button onclick="history.back(); " value="�߂�"></td>
	       				</tr>
<% if (user_level != 2){%>
							<tr>
								<td valign="middle"  class="tableNavy" colspan="3">���[�U�����ݒ�</th>
							</tr>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">
    <% if (input_admin_flag==1  && admin_userid != userid && ownerinfo.DbUserId == admin_userid) {%>
									<input name="admin_flag" type="checkbox" id="admin_flag" value="1" <% if( admin_flag == 1 ) {%> checked <%}%>></td>
	<%}else{%>
	    <% if (admin_flag == 1){%>
									<input name="admin_flag" type="hidden"   id="admin_flag" value="<%=admin_flag%>">��
    	<%}else{%>
									<input name="admin_flag" type="hidden"   id="admin_flag" value="<%=admin_flag%>">-
		<%}%>
    <%}%>
								</td>
								<td class="tableWhite" colspan=1>�Ǘ��Ҍ���</td>
								<td class="tableWhite" colspan=1 width="160" style="text-align:right">���_��󋵁�</td>
							</tr>
						</table>
						<table cellpadding="0" cellspacing="1" border="0"  width="100%" class="search_hoteldetail" >
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">
    <% if (admin_flag == 1){%>
									<input name="sec_level01" type="hidden"   id="sec_level01" value="<%=level[0] %>">-</td>
	<%}else{%>
		<% if (admin_level[0] == 1){%>
			<% if (input_admin_flag == 1){%>
									<input name="sec_level01" type="checkbox" id="sec_level01" value="1" <% if( level[0] == 1 ) {%> checked <%}%>></td>
			<%}else{%>
									<input name="sec_level01" type="hidden"   id="sec_level01" value="<%= level[0] %>"><% if( level[0] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>
		<%}else{%>
									<input name="sec_level01" type="hidden"   id="sec_level01" value="<%= level[0] %>">-</td>
		<%}%>
	<%}%>
								<td class="tableWhite"   >����Ǘ�</td>
								<td class="tableWhite"   width="20"><% if (admin_level[0] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">
    <% if (admin_flag == 1){%>
									<input name="sec_level02" type="hidden"   id="sec_level02" value="<%= level[1] %>">-</td>
	<%}else{%>
		<% if (admin_level[1] == 1){%>
			<% if (input_admin_flag == 1){%>
									<input name="sec_level02" type="checkbox" id="sec_level02" value="1" <% if( level[1] == 1 ) {%> checked <%}%>></td>
			<%}else{%>
									<input name="sec_level02" type="hidden"   id="sec_level02" value="<%= level[1] %>"><% if( level[1] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>
		<%}else{%>
									<input name="sec_level02" type="hidden"   id="sec_level02" value="<%= level[1] %>">-</td>
		<%}%>
	<%}%>
								<td class="tableWhite">�������</td>
								<td class="tableWhite"   width="20"><% if (admin_level[1] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">
    <% if (admin_flag == 1){%>
									<input name="sec_level03" type="hidden"   id="sec_level03" value="<%=  level[2] %>">-</td>
	<%}else{%>
		<% if (admin_level[2] == 1){%>
			<% if (input_admin_flag == 1){%>
									<input name="sec_level03" type="checkbox" id="sec_level03" value="1" <% if( level[2] == 1 ) {%> checked <%}%>></td>
			<%}else{%>
									<input name="sec_level03" type="hidden"   id="sec_level03" value="<%= level[2] %>"><% if( level[2] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>
		<%}else{%>
									<input name="sec_level03" type="hidden"   id="sec_level03" value="<%= level[2] %>">-</td>
		<%}%>
	<%}%>
								<td class="tableWhite">���[�Ǘ�</td>
								<td class="tableWhite"   width="20"><% if (admin_level[2] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">
    <% if (admin_flag == 1){%>
									<input name="sec_level07" type="hidden"   id="sec_level07" value="<%= level[6] %>">-</td>
	<%}else{%>
		<% if (admin_level[6] == 1){%>
			<% if (input_admin_flag == 1){%>
									<input name="sec_level07" type="checkbox" id="sec_level07" value="1" <% if( level[6] == 1 ) {%> checked <%}%>></td>
			<%}else{%>
									<input name="sec_level07" type="hidden"   id="sec_level07" value="<%= level[6] %>"><% if( level[6] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>
		<%}else{%>
									<input name="sec_level07" type="hidden"   id="sec_level07" value="<%= level[6] %>">-</td>
		<%}%>
	<%}%>
								<td class="tableWhite">�ݒ胁�j���[</td>
								<td class="tableWhite"   width="20"><% if (admin_level[6] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
						</table>
						<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search_hoteldetail" >
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">
    <% if (admin_flag == 1){%>
									<input name="sec_level09" type="hidden"   id="sec_level09" value="<%= level[8] %>">-</td>
	<%}else{%>
		<% if (admin_level[8] == 1){%>
			<% if (input_admin_flag == 1){%>
									<input name="sec_level09" type="checkbox" id="sec_level09" value="1" <% if( level[8] == 1 ) {%> checked <%}%>></td>
			<%}else{%>
									<input name="sec_level09" type="hidden"   id="sec_level09" value="<%= level[8] %>"><% if( level[8] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>
		<%}else{%>
									<input name="sec_level09" type="hidden"   id="sec_level09" value="<%= level[8] %>">-</td>
		<%}%>
	<%}%>
								<td class="tableWhite" >���[�_�E�����[�h</td>
								<td class="tableWhite"   width="20"><% if (admin_level[8] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
						</table>
						<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search_hoteldetail" >
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">
    <% if (admin_flag == 1){%>
									<input name="sec_level04" type="hidden"   id="sec_level04" value="<%= level[3] %>">-</td>
	<%}else{%>
		<% if (admin_level[3] == 1){%>
			<% if (input_admin_flag == 1){%>
									<input name="sec_level04" type="checkbox" id="sec_level04" value="1" <% if( level[3] == 1 ) {%> checked <%}%>></td>
			<%}else{%>
									<input name="sec_level04" type="hidden"   id="sec_level04" value="<%= level[3] %>"><% if( level[3] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>
		<%}else{%>
									<input name="sec_level04" type="hidden"   id="sec_level04" value="<%= level[3] %>">-</td>
		<%}%>
	<%}%>
								<td class="tableWhite" >HP���|�[�g</td>
								<td class="tableWhite"   width="20"><% if (admin_level[3] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">
    <% if (admin_flag == 1){%>
									<input name="sec_level05" type="hidden"   id="sec_level05" value="<%= level[4] %>">-</td>
	<%}else{%>
		<% if (admin_level[4] == 1){%>
			<% if (input_admin_flag == 1){%>
									<input name="sec_level05" type="checkbox" id="sec_level05" value="1" <% if( level[4] == 1 ) {%> checked <%}%>></td>
			<%}else{%>
									<input name="sec_level05" type="hidden"   id="sec_level05" value="<%= level[4] %>"><% if( level[4] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>
		<%}else{%>
									<input name="sec_level05" type="hidden"   id="sec_level05" value="<%= level[4] %>">-</td>
		<%}%>
	<%}%>
								<td class="tableWhite">�����}�K�쐬</td>
								<td class="tableWhite"   width="20"><% if (admin_level[4] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">
    <% if (admin_flag == 1){%>
									<input name="sec_level06" type="hidden"   id="sec_level06" value="<%= level[5] %>">-</td>
	<%}else{%>
		<% if (admin_level[5] == 1){%>
			<% if (input_admin_flag == 1){%>
									<input name="sec_level06" type="checkbox" id="sec_level06" value="1" <% if( level[5] == 1 ) {%> checked <%}%>></td>
			<%}else{%>
									<input name="sec_level06" type="hidden"   id="sec_level06" value="<%= level[5] %>"><% if( level[5] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>
		<%}else{%>
									<input name="sec_level06" type="hidden"   id="sec_level06" value="<%= level[5] %>">-</td>
		<%}%>
	<%}%>
								<td class="tableWhite">�z�e�i�rHP�ҏW</td>
								<td class="tableWhite"   width="20"><% if (admin_level[5] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
						</table>
<% if (admin_level[7] == 1 || admin_level[9] == 1){%>
						<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search_hoteldetail" >
	<% if (admin_level[7] == 1 ){%>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">
		<% if (admin_flag == 1){%>
									<input name="sec_level08" type="hidden"   id="sec_level08" value="<%= level[7] %>">-</td>
		<%}else{%>
			<% if (admin_level[7] == 1){%>
				<% if (input_admin_flag == 1){%>
									<input name="sec_level08" type="checkbox" id="sec_level08" value="1" <% if( level[7] == 1 ) {%> checked <%}%>></td>
				<%}else{%>
									<input name="sec_level08" type="hidden"   id="sec_level08" value="<%= level[7] %>"><% if( level[7] == 1 ) {%>��<%}else{%>-<%}%></td>
				<%}%>
			<%}else{%>
									<input name="sec_level08" type="hidden"   id="sec_level08" value="<%= level[7] %>">-</td>
			<%}%>
		<%}%>
								<td class="tableWhite">�}�l�[�W�A�C</td>
								<td class="tableWhite"   width="20"><% if (admin_level[7] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
	<%}%>
	<% if (admin_level[9] == 1 ){%>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">
		<% if (admin_flag == 1){%>
									<input name="sec_level10" type="hidden"   id="sec_level10" value="<%= level[9] %>">-</td>
		<%}else{%>
			<% if (admin_level[9] == 1){%>
				<% if (input_admin_flag == 1){%>
									<input name="sec_level10" type="checkbox" id="sec_level10" value="1" <% if( level[9] == 1 ) {%> checked <%}%>></td>
				<%}else{%>
									<input name="sec_level10" type="hidden"   id="sec_level10" value="<%= level[9] %>"><% if( level[9] == 1 ) {%>��<%}else{%>-<%}%></td>
				<%}%>
			<%}else{%>
									<input name="sec_level10" type="hidden"   id="sec_level10" value="<%= level[9] %>">-</td>
			<%}%>
		<%}%>
								<td class="tableWhite">���X�ܒ��[�I��</td>
								<td class="tableWhite"   width="20"><% if (admin_level[9] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
	<%}%>
						</table>
<%}%>
						<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search_hoteldetail" >
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">
    <% if (admin_flag == 1){%>
									<input name="sec_level15" type="hidden"   id="sec_level15" value="<%= level[14] %>">-</td>
	<%}else{%>
		<% if (admin_level[14] == 1){%>
			<% if (input_admin_flag == 1){%>
									<input name="sec_level15" type="checkbox" id="sec_level15" value="1" <% if( level[14] == 1 ) {%> checked <%}%>></td>
			<%}else{%>
									<input name="sec_level15" type="hidden"   id="sec_level15" value="<%= level[14] %>"><% if( level[14] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>
		<%}else{%>
									<input name="sec_level15" type="hidden"   id="sec_level15" value="<%=level[14] %>">-</td>
		<%}%>
	<%}%>
								<td class="tableWhite"   >�n�b�s�[�E�z�e���ҏW����</td>
								<td class="tableWhite"   width="20"><% if (admin_level[14] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">
    <% if (admin_flag == 1){%>
									<input name="sec_level16" type="hidden"   id="sec_level16" value="<%= level[15] %>">-</td>
	<%}else{%>
		<% if (admin_level[15] == 1){%>
			<% if (input_admin_flag == 1){%>
									<input name="sec_level16" type="checkbox" id="sec_level16" value="1" <% if( level[15] == 1 ) {%> checked <%}%>></td>
			<%}else{%>
									<input name="sec_level16" type="hidden"   id="sec_level16" value="<%= level[15] %>"><% if( level[15] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>
		<%}else{%>
									<input name="sec_level16" type="hidden"   id="sec_level16" value="<%= level[15] %>">-</td>
		<%}%>
	<%}%>
								<td class="tableWhite">�N�`�R�~�񓚌���</td>
								<td class="tableWhite"   width="20"><% if (admin_level[15] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">

			<% if (input_admin_flag == 1){%>
				<% if (admin_userid == userid && admin_userid != ownerinfo.DbUserId) { %>
									<input name="sec_level17" type="hidden"   id="sec_level17" value="<%= level[16] %>"><% if( level[16] == 1 ) {%>��<%}else{%>-<%}%></td>
				<%}else{%>
									<input name="sec_level17" type="checkbox" id="sec_level17" value="1" <% if( level[16] == 1 ) {%> checked <%}%>></td>
				<%}%>
			<%}else{%>
									<input name="sec_level17" type="hidden"   id="sec_level17" value="<%= level[16] %>"><% if( level[16] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>


								<td class="tableWhite">�N�`�R�~�f�ڂ��m�点���[���iPC�j</td>
								<td class="tableWhite"   width="20"><% if (admin_level[16] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">


			<% if (input_admin_flag == 1){%>
				<% if (admin_userid == userid && admin_userid != ownerinfo.DbUserId) { %>
									<input name="sec_level18" type="hidden"   id="sec_level18" value="<%= level[17] %>"><% if( level[17] == 1 ) {%>��<%}else{%>-<%}%></td>
				<%}else{%>
									<input name="sec_level18" type="checkbox" id="sec_level18" value="1" <% if( level[17] == 1 ) {%> checked <%}%>></td>
				<%}%>
			<%}else{%>
									<input name="sec_level18" type="hidden"   id="sec_level18" value="<%= level[17] %>"><% if( level[17] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>


								<td class="tableWhite">�N�`�R�~�f�ڂ��m�点���[���i�g�сj</td>
								<td class="tableWhite"   width="20"><% if (admin_level[17] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
						</table>
						<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search_hoteldetail" >
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">


			<% if (input_admin_flag == 1){%>
				<% if (admin_userid == userid && admin_userid != ownerinfo.DbUserId) { %>
									<input name="sec_level19" type="hidden"   id="sec_level191" value="<%= level[18] %>"><% if( level[18] == 1 ) {%>��<%}else{%>-<%}%>
				<%}else{%>
									<input name="sec_level19" type="radio"    id="sec_level191"   value="1" <% if( level[18] == 1 ) {%> checked <%}%> onclick="if(this.checked){if(document.getElementById('sec_level19').value==1){this.checked=false;document.getElementById('sec_level19').value=0;}else{document.getElementById('sec_level19').value=1;}}">
				<%}%>
			<%}else{%>
									<input name="sec_level19" type="hidden"   id="sec_level191" value="<%= level[18] %>"><% if( level[18] == 1 ) {%>��<%}else{%>-<%}%>
			<%}%>


								</td>
								<td class="tableWhite">�n�s�z�e���O�\��@�ҏW</td>
								<td class="tableWhite"   width="20"><% if (admin_level[18] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">


			<% if (input_admin_flag == 1){%>
				<% if (admin_userid == userid && admin_userid != ownerinfo.DbUserId) { %>
									<input name="sec_level19" type="hidden"   id="sec_level192" value="<%= level[18] %>"><% if( level[18] == 2 ) {%>��<%}else{%>-<%}%>
				<%}else{%>
									<input name="sec_level19" type="radio"    id="sec_level192"   value="2" <% if( level[18] == 2 ) {%> checked <%}%> onclick="if(this.checked){if(document.getElementById('sec_level19').value==2){this.checked=false;document.getElementById('sec_level19').value=0;}else{document.getElementById('sec_level19').value=2;}}">
				<%}%>
			<%}else{%>
									<input name="sec_level19" type="hidden"   id="sec_level192" value="<%= level[18] %>"><% if( level[18] == 2 ) {%>��<%}else{%>-<%}%>
			<%}%>


								</td>
								<td class="tableWhite">�n�s�z�e���O�\��@�t�����g��p</td>
								<td class="tableWhite"   width="20"><% if (admin_level[18] == 2){%>��<%}else{%>�~<%}%></td>
							</tr>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">


			<% if (input_admin_flag == 1){%>
				<% if (admin_userid == userid && admin_userid != ownerinfo.DbUserId) { %>
									<input name="sec_level21" type="hidden"   id="sec_level21" value="<%= level[20] %>"><% if( level[20] == 1 ) {%>��<%}else{%>-<%}%></td>
				<%}else{%>
									<input name="sec_level21" type="checkbox" id="sec_level21" value="1" <% if( level[20] == 1 ) {%> checked <%}%>></td>
				<%}%>
			<%}else{%>
									<input name="sec_level21" type="hidden"   id="sec_level21" value="<%= level[20] %>"><% if( level[20] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>


								<td class="tableWhite">�n�s�z�e�\�񂨒m�点���[���iPC�j</td>
								<td class="tableWhite"   width="20"><% if (admin_level[20] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">


			<% if (input_admin_flag == 1){%>
				<% if (admin_userid == userid && admin_userid != ownerinfo.DbUserId) { %>
									<input name="sec_level22" type="hidden"   id="sec_level22" value="<%= level[21] %>"><% if( level[21] == 1 ) {%>��<%}else{%>-<%}%></td>
				<%}else{%>
									<input name="sec_level22" type="checkbox" id="sec_level22" value="1" <% if( level[21] == 1 ) {%> checked <%}%>></td>
				<%}%>
			<%}else{%>
									<input name="sec_level22" type="hidden"   id="sec_level22" value="<%= level[21] %>"><% if( level[21] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>


								<td class="tableWhite">�n�s�z�e�\�񂨒m�点���[���i�g�сj</td>
								<td class="tableWhite"   width="20"><% if (admin_level[21] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
						</table>
						<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search_hoteldetail" >
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">


			<% if (input_admin_flag == 1){%>
				<% if (admin_userid == userid && admin_userid != ownerinfo.DbUserId) { %>
									<input name="sec_level20" type="hidden"   id="sec_level20" value="<%= level[19] %>"><% if( level[19] == 1 ) {%>��<%}else{%>-<%}%></td>
				<%}else{%>
									<input name="sec_level20" type="checkbox" id="sec_level20" value="1" <% if( level[19] == 1 ) {%> checked <%}%>></td>
				<%}%>
			<%}else{%>
									<input name="sec_level20" type="hidden"   id="sec_level20" value="<%= level[19] %>"><% if( level[19] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>


								<td class="tableWhite">�n�s�z�e�}�C�������X�����{������</td>
								<td class="tableWhite"   width="20"><% if (admin_level[19] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
							<tr>
								<td valign="middle"  class="tableWhite" align="center" width="20">


			<% if (input_admin_flag == 1){%>
				<% if (admin_userid == userid && admin_userid != ownerinfo.DbUserId) { %>
									<input name="sec_level23" type="hidden"   id="sec_level23" value="<%= level[22] %>"><% if( level[22] == 1 ) {%>��<%}else{%>-<%}%></td>
				<%}else{%>
									<input name="sec_level23" type="checkbox" id="sec_level23" value="1" <% if( level[22] == 1 ) {%> checked <%}%>></td>
				<%}%>
			<%}else{%>
									<input name="sec_level23" type="hidden"   id="sec_level23" value="<%= level[22] %>"><% if( level[22] == 1 ) {%>��<%}else{%>-<%}%></td>
			<%}%>


								<td class="tableWhite">�n�s�z�e�^�b�`���ڑ����[��</td>
								<td class="tableWhite"   width="20"><% if (admin_level[22] == 1){%>��<%}else{%>�~<%}%></td>
							</tr>
							<input name="sec_level11" type="hidden" id="sec_level11" value="<%=level[10]%>">
							<input name="sec_level12" type="hidden" id="sec_level12" value="<%=level[11]%>">
							<input name="sec_level13" type="hidden" id="sec_level13" value="<%=level[12]%>">
							<input name="sec_level14" type="hidden" id="sec_level14" value="<%=level[13]%>">
<%}%>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td colspan=2><img src="../../common/pc/image/spacer.gif" width="10" height="5"></td>
			</tr>
			<tr>
				<td colspan=2 align=left style="padding-left:10px" width="100%">
<% if (admin_report_flag == 1){%>
						<table cellpadding="0" cellspacing="1" border="0" width="100%">
						<table cellpadding="0" cellspacing="1" border="0" width="100%">
							<tr>
								<th valign="middle" class="tableNavy" colspan="4">�Ǘ��X�ܐݒ�</th>
							</tr>
							<tr>
								<th valign="middle" class="tableNavy" >�Ǘ��X��</th>
								<th valign="middle" class="tableNavy" width="100" style="text-align:right">���_��󋵁�</th>
								<th valign="middle" class="tableNavy" width="320">���チ�[���z�M�ݒ�</th>
								<th valign="middle" class="tableNavy" width="100" style="text-align:right">�V�X�e���Ή���</th>
							</tr>
						</table>
						<table cellpadding="0" cellspacing="1" border="0"  width="100%">
<%}else{%>
						<table cellpadding="0" cellspacing="1" border="0" width="100%">
						<table cellpadding="0" cellspacing="1" border="0" width="100%">
							<tr>
								<th valign="middle" class="tableNavy" colspan="2" >�Ǘ��X�ܐݒ�</th>
							</tr>
							<tr>
								<th valign="middle" class="tableNavy" colspan="1" >�Ǘ��X��</th>
								<th valign="middle" class="tableNavy" colspan="1" width="100" style="text-align:right">���_��󋵁�</th>
							</tr>
						</table>
						<table cellpadding="0" cellspacing="1" border="0" width="100%">
<%}%>

<%
                boolean checked_accept_hotelid   = true;
                String  list_accept_hotelid;
                String  list_name;
                boolean list_admin               = true;
                int     list_report_daily_mobile = 0;
                int     list_report_daily_pc     = 0;
                int     list_report_month_mobile = 0;
                int     list_report_month_pc     = 0;
                int     list_report_times        = 0;
                int     list_report_timee        = 0;

                count = 0;
                if( reth != null )
                {
                    while( reth.next() != false )
                    {
                        count++;
                        target_hotel[count] = reth.getString("owner_user_hotel.accept_hotelid"); //��P�Ǘ��҂̊Ǘ��z�e�����Z�b�g

                        list_accept_hotelid = reth.getString("owner_user_hotel.accept_hotelid");
                        list_name           = reth.getString("hotel.name");

                        // ���̃��[�U�̊Ǘ��z�e���ƈ�v���邩
                        query = "SELECT * FROM owner_user_hotel WHERE hotelid= ? ";
                        query = query + " AND userid = ?";
                        query = query + " AND accept_hotelid= ?";
                        stm   = db.prepareStatement(query);
                        stm.setString(1, loginHotelId);
                        stm.setInt(2, Integer.parseInt(param_userid));
                        stm.setString(3, list_accept_hotelid);
                        retm  = stm.executeQuery();
                        if( retm.next() != false )
                        {
                            // �Ǘ��X��
                            checked_accept_hotelid   = true;
                            list_report_daily_pc     = retm.getInt("owner_user_hotel.report_daily_pc");
                            list_report_daily_mobile = retm.getInt("owner_user_hotel.report_daily_mobile");
                            list_report_month_pc     = retm.getInt("owner_user_hotel.report_month_pc");
                            list_report_month_mobile = retm.getInt("owner_user_hotel.report_month_mobile");
                            list_report_times        = retm.getInt("owner_user_hotel.report_times");
                            list_report_timee        = retm.getInt("owner_user_hotel.report_timee");
%>
							<%@ include file="owner_user_hotel_sub.jsp" %>
<%
                        }
                        else
                        {
                            // �Ǘ��X�܂���Ȃ�
                            checked_accept_hotelid   = false;
                            list_report_daily_pc     = 0;
                            list_report_daily_mobile = 0;
                            list_report_month_pc     = 0;
                            list_report_month_mobile = 0;
                            list_report_times        = 0;
                            list_report_timee        = 0;
                            if (input_admin_flag == 1)
                            {
%>
							<%@ include file="owner_user_hotel_sub.jsp" %>
<%
                            }
                        }
                        DBConnection.releaseResources(retm);
                        DBConnection.releaseResources(stm);
                    }
                }
                DBConnection.releaseResources(reth);
                DBConnection.releaseResources(sth);

                // ���̃��[�U�̊Ǘ��z�e����ǂݍ��݁A��P�Ǘ��҂̊Ǘ��z�e���ɂȂ��ꍇ�́A�Ǘ��X�܂�ǉ��\������B
                query = "SELECT owner_user_hotel.*,hotel.name FROM owner_user_hotel,hotel";
                query = query + " WHERE owner_user_hotel.hotelid= ?";
                query = query + " AND owner_user_hotel.userid= ? ";
                query = query + " AND owner_user_hotel.accept_hotelid=hotel.hotel_id";
                stm   = db.prepareStatement(query);
                stm.setString(1, loginHotelId);
                stm.setInt(2, Integer.parseInt(param_userid));
                retm  = stm.executeQuery();
                while( retm.next() != false )
                {
                    checked_accept_hotelid   = true;
                    list_accept_hotelid      = retm.getString("owner_user_hotel.accept_hotelid");
                    list_name                = retm.getString("hotel.name");
                    list_report_daily_pc     = retm.getInt("owner_user_hotel.report_daily_pc");
                    list_report_daily_mobile = retm.getInt("owner_user_hotel.report_daily_mobile");
                    list_report_month_pc     = retm.getInt("owner_user_hotel.report_month_pc");
                    list_report_month_mobile = retm.getInt("owner_user_hotel.report_month_mobile");
                    list_report_times        = retm.getInt("owner_user_hotel.report_times");
                    list_report_timee        = retm.getInt("owner_user_hotel.report_timee");
                    for( i = 1; i <= count;i++)
                    {
                        if (target_hotel[i].compareTo(list_accept_hotelid) == 0)
                         break;
                    }
                    if (i > count)
                    {
                        if (input_admin_flag == 1)
                        {
                            list_admin          = false;
                            count++;
                            target_hotel[count] = list_accept_hotelid;
%>
							<%@ include file="owner_user_hotel_sub.jsp" %>
<%
                        }
                    }
                 }
                DBConnection.releaseResources(retm);
                DBConnection.releaseResources(stm);

                // �O���[�v�R�[�h����Ώۃz�e����ǂݍ��݁A��P�Ǘ��҂̊Ǘ��z�e���ƈ�v���Ȃ������ꍇ�́A��Ǘ��X�܂Ƃ��Ēǉ��\������B
                query = "SELECT hotel.name,hotel.hotel_id FROM hotel";
                query = query + " WHERE (hotel.group_id = ? OR hotel.hotel_id  = ? )";
                query = query + " AND hotel.plan <= 4";
                stm   = db.prepareStatement(query);
                stm.setString(1, loginHotelId);
                stm.setString(2, loginHotelId);
                retm  = stm.executeQuery();
                while( retm.next() )
                {
                    checked_accept_hotelid   = false;
                    list_accept_hotelid      = retm.getString("hotel.hotel_id");
                    list_name                = retm.getString("hotel.name");
                    list_report_daily_pc     = 0;
                    list_report_daily_mobile = 0;
                    list_report_month_pc     = 0;
                    list_report_month_mobile = 0;
                    list_report_times        = 0;
                    list_report_timee        = 0;
                    for( i = 1; i <= count;i++)
                    {
                        if (target_hotel[i].compareTo(list_accept_hotelid) == 0)
                         break;
                    }
                    if (i > count)
                    {
                        if (input_admin_flag == 1)
                        {
                            list_admin          = false;
                            count++;
                            target_hotel[count] = list_accept_hotelid;
%>
							<%@ include file="owner_user_hotel_sub.jsp" %>
<%
                        }
                    }
                 }
                DBConnection.releaseResources(retm,stm,db);
%>
							<input name="count" type="hidden" class="tableWhite" id="count" value="<%= count %>">
							</table>
						</table>
					</td>
				</tr>
				<tr>
					<td class="mainframe_text" align="center" colspan=2>
						<input name="Reset" type="reset" value="���Z�b�g" class="submit_button">
						<input name="token" type="hidden" value="<%=token%>">
<%
	        if( userid == 0 )
	        {
%>
						<input name="Submit" type="submit" value="�V�K�o�^" onClick="return user_datacheck();" class="submit_button">
<%
	        }
	        else
	        {
%>
						<input name="Submit" type="submit" value=" �ρ@�X " onClick="return user_datacheck();" class="submit_button">
<%
       	 	}
%>
					</td>
				</tr>
</form>
<script type="text/javascript">
<!--
function SalesMailCheck()
{
    var flag = 0;
    <% for( i = 1; i <= count;i++) {%>
         if( document.userform.report_daily_pc<%= i %>.checked == true      ||
             document.userform.report_daily_mobile<%= i %>.checked == true  ||
             document.userform.report_month_pc<%= i %>.checked == true      ||
             document.userform.report_month_mobile<%= i %>.checked == true  )
         {
             flag = 1;
         }
    <%}%>

    if (flag == 1)
    {
    <% if (admin_report_flag == 1 && input_admin_flag == 1){%>
             document.userform.report_flag.checked = true;
    <%}else{%>
             document.userform.report_flag.value   = "1";
    <%}%>
    }
    else
    {
    <% if (admin_report_flag == 1 && input_admin_flag == 1){%>
             document.userform.report_flag.checked = false;
    <%}else{%>
             document.userform.report_flag.value   = "0";
    <%}%>
    }
}
function user_datacheck() //�Ǘ��Ҍ���
{
	<% if (admin_report_flag == 1 && admin_userid != userid && input_admin_flag == 1){%>
	SalesMailCheck();
	<%}%>
	if( document.userform.name.value == "" )
    {
        alert("���O����͂��Ă�������");
        return false;
    }
    if( document.userform.loginid.value == "" )
    {
        alert("���[�U������͂��Ă�������");
        return false;
    }
    if( document.forms["userform"].passwd_pc.value != document.forms["userform"].passwd_pc_re.value )
    {
        alert('PC�p�X���[�h�Ɗm�F���͂̃p�X���[�h���Ⴂ�܂�');
        return false;
    }

    if( document.forms["userform"].passwd_mobile.value != document.forms["userform"].passwd_mobile_re.value )
    {
        alert('�g�уp�X���[�h�Ɗm�F���͂̃p�X���[�h���Ⴂ�܂�');
        return false;
    }

    if(document.forms["userform"].mailaddr_pc.value!="" && !document.forms["userform"].mailaddr_pc.value.match(/^[A-Za-z0-9]+[\w\.-]+@[\w\.-]+\.\w{2,}$|^[A-Za-z0-9]+@[\w\.-]+\.\w{2,}$/))
    {
        alert('PC���[���A�h���X�𐳂������͂��Ă�������');
        return false;
    }

    if(document.forms["userform"].mailaddr_mobile.value!="" && !document.forms["userform"].mailaddr_mobile.value.match(/^[A-Za-z0-9]+[\w\.-]+@[\w\.-]+\.\w{2,}$|^[A-Za-z0-9]+@[\w\.-]+\.\w{2,}$/))
    {
        alert('�g�у��[���A�h���X�𐳂������͂��Ă�������');
        return false;
    }
<%
    if( admin_flag == 0 )
    {
%>
    if (document.userform.admin_flag.checked)
    {
        return confirm("�Ǘ��Ҍ����҂͂��ׂĂ̋@�\���g���܂����A�{���ɂ�낵���ł����H")
    }
<%
    }
%>
	return true;
}
-->
</script>
			</table>

            <tr>
              <td height="4" valign="top">&nbsp;</td>
              <td align="left" valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
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
        <td height="3" width="3955"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10">
	<img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle">
	<img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>

</body>
</html>
