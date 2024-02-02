<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.Url" %>
<%@ page import="jp.happyhotel.common.Constants" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page import="jp.happyhotel.common.DBSync" %>
<%@ page import="jp.happyhotel.common.DateEdit" %>
<%@ page import="jp.happyhotel.common.RandomString" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<%@ page import="jp.happyhotel.util.Base64Manager" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
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
    int      loginUserId  =   ownerinfo.DbUserId;
    String   loginUserName = "";
    Calendar cal = Calendar.getInstance();
    int now_year  = cal.get(cal.YEAR);
    int now_month = cal.get(cal.MONTH) + 1;
    int now_day   = cal.get(cal.DATE);
    int nowdate   = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime   = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);
    String   query     = "";
    String   queryGcp  = "";
    int      i;
    int      ret       = 0;
    int      ret_error = 0;
    Connection db      = null;
    PreparedStatement  st      = null;
    ResultSet  result  = null;
    Connection        dbh  = null;
    PreparedStatement sth  = null;
    ResultSet         reth = null;
    DbAccess    db_update;
    NumberFormat nf = new DecimalFormat("00");

    int      edit_flag  = 0;
    int      log_level  = 0;
    String   log_detail = "";
    String   mail_message = "";
    int      input_admin_flag = 0;
    int      input_report_flag = 0;
    int      mail_change_flag = 0;

//���͒S���҂�PC���[���A�h���X�ƃZ�L�����e�B�����߂�
    String  input_mail    = "";
    query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid=?";
    query = query + " AND owner_user.userid=?";
    query = query + " AND owner_user_security.hotelid = owner_user.hotelid";
    query = query + " AND owner_user_security.userid = owner_user.userid";

    db     = DBConnection.getConnection();
    st     =db.prepareStatement(query);
    st.setString(1, loginHotelId);
    st.setInt(2, loginUserId);
    result = st.executeQuery();
    if( result.next() != false )
    {
        input_mail        = result.getString("owner_user.mailaddr_pc");
        loginUserName     = result.getString("owner_user.name");
        input_admin_flag  = result.getInt("owner_user_security.admin_flag");
        input_report_flag = result.getInt("owner_user.report_flag");
    }
    DBConnection.releaseResources(result,st,db);
    if (input_mail.compareTo("") == 0)
    {
        query = "SELECT * FROM mag_hotel WHERE hotel_id=?";
        db     = DBConnection.getConnection();
        st     =db.prepareStatement(query);
        st.setString(1, loginHotelId);        result = st.executeQuery();
        if( result.next() != false )
        {
            input_mail       = result.getString("address");
        }
        DBConnection.releaseResources(result,st,db);
    }
    if (input_mail.compareTo("") == 0)
    {
        input_mail = "imedia-info@hotenavi.com";
    }


//�����f�[�^�ǂݍ��݁i���[�����M�`�F�b�N�̂��߁j
    int   history_seq  = 1;  // �f�[�^���Ȃ����1
    query = "SELECT * FROM hh_owner_user_log WHERE login_name=?";
    query = query + " AND passwd=? ";
    query = query + " AND hotel_id=? ";
    query = query + " AND log_level >= 200";
    query = query + " AND log_level <= 299";
    query = query + " ORDER BY seq DESC";
    Connection dbseq      = null;
    PreparedStatement  stseq      = null;
    ResultSet  retseq     = null;
    dbseq = DBConnection.getConnection();
    stseq = dbseq.prepareStatement(query);
    stseq.setString(1, loginHotelId);
    stseq.setString(2, Integer.toString(loginUserId));
    stseq.setString(3, loginHotelId);
    retseq = stseq.executeQuery();
    if( retseq.next() != false )
    {
         history_seq = retseq.getInt("seq") + 1;
         if (retseq.getString("login_name").compareTo(loginHotelId) != 0) history_seq = 1;
         if (retseq.getInt("log_level") < 200 || retseq.getInt("log_level") > 299) history_seq = 1;
         if (retseq.getString("passwd").compareTo(Integer.toString(loginUserId)) != 0) history_seq = 1;
    }
    DBConnection.releaseResources(retseq,stseq,dbseq);

//��1.owner_user �������ݏ���
    String userid          = ReplaceString.getParameter(request,"UserId");
	if(userid != null && !CheckString.numCheck(userid))
	{
		userid = "";
		ret_error = 2;
	}
    String loginid         = ReplaceString.getParameter(request,"loginid");
	if(loginid != null)
	{
        if( !CheckString.loginIdCheck(loginid) )
        {
			loginid = "";
			ret_error = 3;
        }
	}

    String machineid       = ReplaceString.getParameter(request,"machineid");
	if(machineid != null && !machineid.equals("") && !CheckString.numAlphaCheck(machineid))
	{
		machineid = "";
		ret_error = 4;
	}
    if    (machineid      == null)   machineid = "";
    String name            = ReplaceString.getParameter(request,"name");
	if(name != null)
	{
        if( !CheckString.nameCheck(name) )
        {
			name = "";
			ret_error = 11;
        }
	}

    String passwd_pc       = ReplaceString.getParameter(request,"passwd_pc");

    String passwd_mobile   = ReplaceString.getParameter(request,"passwd_mobile");
	if(passwd_mobile != null && !passwd_mobile.equals("") && !CheckString.passwordCheck(passwd_mobile))
	{
		passwd_mobile = "";
		ret_error = 6;
	}
    String mailaddr_pc     = ReplaceString.getParameter(request,"mailaddr_pc");
    String mailaddr_mobile = ReplaceString.getParameter(request,"mailaddr_mobile");
    if (mailaddr_pc != null)
    {
        mailaddr_pc = mailaddr_pc.replace("&nbsp;","").replace(" ","").replace("\r\n","");
        if (!mailaddr_pc.equals(""))
        {
            if(!CheckString.mailaddrCheck(mailaddr_pc))
            {
                mailaddr_pc = "";
                ret_error = 7;
            }
        }
    }
    if (mailaddr_mobile != null)
    {
        mailaddr_mobile = mailaddr_mobile.replace("&nbsp;","").replace(" ","").replace("\r\n","");
        if (!mailaddr_mobile.equals(""))
        {
            if(!CheckString.mailaddrCheck(mailaddr_mobile))
            {
                mailaddr_mobile = "";
                ret_error = 8;
            }
        }
    }

    String imedia_user     = ReplaceString.getParameter(request,"imedia_user");
    if    (imedia_user    == null)   imedia_user = "0";
    String report_flag     = ReplaceString.getParameter(request,"report_flag");
    if    (report_flag    == null)   report_flag = "0";
    String user_level      = ReplaceString.getParameter(request,"user_level");
    if    (user_level     == null)   user_level  = "1";

    String passwd_pc_new   = ReplaceString.getParameter(request,"passwd_pc_new");
    if (passwd_pc_new != null)
    {
        if (passwd_pc_new.equals("1"))
        {
            passwd_pc = RandomString.getOwnerPassword();
        }
    }
    String passwd_mobile_new   = ReplaceString.getParameter(request,"passwd_mobile_new");
    if (passwd_mobile_new != null)
    {
        if (passwd_mobile_new.equals("1") && machineid.length() > 10)
        {
            passwd_mobile = RandomString.getRandomNumber(4);
        }
    }

    String passwd_pc_network = ReplaceString.getParameter(request,"passwd_pc_network");
    String passwd_pc_no      = ReplaceString.getParameter(request,"passwd_pc_no");
    String passwd_mobile_no  = ReplaceString.getParameter(request,"passwd_mobile_no");


    String old_loginid          = "";
    String old_machineid        = "";
    String old_name             = "";
    String old_passwd_pc        = "";
    String old_passwd_mobile    = "";
    String old_mailaddr_pc      = "";
    String old_mailaddr_mobile  = "";
    int    old_imedia_user      = 0;
    int    old_report_flag      = 0;
    int    old_unknown_flag_pc      = 0;
    int    old_unknown_flag_mobile  = 0;
    int    old_passwd_pc_update     = 0;
    int    old_passwd_mobile_update = 0;

    int    count_report_hotel   = 0;

    String sales_userid          = ReplaceString.getParameter(request,"sales_userid");
    if(!CheckString.numCheck(sales_userid))
	{
%>
		<jsp:forward page="timeout.html" />
<%
	}
    String sales_mailaddr_pc     = ReplaceString.getParameter(request,"sales_mailaddr_pc");
	if(sales_mailaddr_pc != null && !CheckString.mailaddrCheck(sales_mailaddr_pc))
	{
//		ret_error = 9;//DB�ɓo�^���Ă��Ȃ�
	}
    String sales_mailaddr_mobile = ReplaceString.getParameter(request,"sales_mailaddr_mobile");
	if(sales_mailaddr_mobile != null && !CheckString.mailaddrCheck(sales_mailaddr_mobile))
	{
//		ret_error = 10;//DB�ɓo�^���Ă��Ȃ�
	}
	String target_system_hotenavi = ReplaceString.getParameter(request,"target_system_hotenavi");
	if(target_system_hotenavi == null){target_system_hotenavi = "";}
	String target_system_hapihote = ReplaceString.getParameter(request,"target_system_hapihote");
	if(target_system_hapihote == null){target_system_hapihote = "";}
	String target_system_reserve = ReplaceString.getParameter(request,"target_system_reserve");
	if(target_system_reserve == null){target_system_reserve = "";}

//1.(1) ���[�U��o�^�`�F�b�N
    int   ret_user = 0;
    query  = "SELECT * FROM owner_user WHERE hotelid=?";
    query  = query + " AND  userid=?";
    db     = DBConnection.getConnection();
    st     =db.prepareStatement(query);
    st.setString(1, loginHotelId);
    st.setInt(2, Integer.parseInt(userid));
    result = st.executeQuery();
    if( result != null )
    {
        if(result.next() != false)
        {
            ret_user = 1;     //�o�^��
            old_loginid         = result.getString("loginid");
            old_machineid       = result.getString("machineid");
            old_name            = result.getString("name");
            old_passwd_pc       = result.getString("passwd_pc");
            old_passwd_mobile   = result.getString("passwd_mobile");
            old_mailaddr_pc     = result.getString("mailaddr_pc");
            old_mailaddr_mobile = result.getString("mailaddr_mobile");
            old_imedia_user     = result.getInt("imedia_user");
            old_report_flag     = result.getInt("report_flag");
            old_unknown_flag_pc = result.getInt("unknown_flag_pc");
            old_unknown_flag_mobile = result.getInt("unknown_flag_mobile");
            old_passwd_pc_update     = result.getInt("passwd_pc_update");
            old_passwd_mobile_update = result.getInt("passwd_mobile_update");
        }
    }
    DBConnection.releaseResources(result,st,db);


    if (passwd_pc == null)
    {
        passwd_pc = old_passwd_pc;
    }

	if(passwd_pc != null && !passwd_pc.equals("")  && !passwd_pc.equals(old_passwd_pc) && !passwd_pc.matches(CheckString.getPasswordRegex()))
	{
		passwd_pc = "";
		ret_error = 5;
	}



    if (passwd_mobile == null)
    {
        passwd_mobile = old_passwd_mobile;
    }



//���͒S���҂� admin_flag �� 0 �Ł@report_flag = 1 �̏ꍇ�A���[���A�h���X��ύX�����ꍇ�́A�������O�C��ID��level=2�ɏ������ށB

    if (input_admin_flag == 0 && input_report_flag == 1)
    {
        if(mailaddr_pc.compareTo(old_mailaddr_pc) != 0 || mailaddr_mobile.compareTo(old_mailaddr_mobile) != 0)
        {
            mail_change_flag = 1;
        }
    }

//1.(2) ���O�C��ID�o�^�`�F�b�N
    int   ret_loginid = 0;
    if  (passwd_pc.compareTo("") != 0 && loginid.compareTo(old_loginid) != 0)
    {
        query  = "SELECT * FROM owner_user WHERE hotelid=?";
        query  = query + " AND  loginid=?";
        query  = query + " AND  level=1";
        db     = DBConnection.getConnection();
        st     =db.prepareStatement(query);
        st.setString(1, loginHotelId);
        st.setString(2, loginid);
        result = st.executeQuery();
        if( result != null )
        {
            if(result.next() != false)
            {
               ret_loginid = 1;     //���O�C��ID�o�^��
            }
        }
        DBConnection.releaseResources(result,st,db);
    }

    if  (ret_loginid == 1 || (ret_user == 0 && userid.compareTo("0") != 0) )
    {
         ret_error = 1;
    }
    
    if(ret_error == 0)
	{
//1.(3) �������ݏ���
        if( userid.compareTo("0") == 0 )  // �V�K�o�^
        {
            int useridNew = 0;
%><%@ include file="../../common/pc/getUserIdNew.jsp" %><%
            useridNew = useridNew + 1;
            edit_flag = 1;
            log_level = 200;
            log_detail = "�y�V�K�o�^�z" + "\r\n";
            query = "INSERT INTO hotenavi.owner_user SET ";
            query = query + "hotelid='" + loginHotelId + "', ";
            query = query + "userid=" + useridNew + ", ";
        }
        else
        {
            if (loginid.compareTo(old_loginid) != 0 ||
                machineid.compareTo(old_machineid) != 0 ||
                name.compareTo(old_name) != 0 ||
                passwd_pc.compareTo(old_passwd_pc) != 0 ||
                passwd_mobile.compareTo(old_passwd_mobile) != 0 ||
                mailaddr_pc.compareTo(old_mailaddr_pc) != 0 ||
                mailaddr_mobile.compareTo(old_mailaddr_mobile) != 0 ||
                Integer.parseInt(imedia_user) != old_imedia_user ||
                Integer.parseInt(report_flag) != old_report_flag ||
                old_unknown_flag_pc == 1 ||
                old_unknown_flag_mobile == 1)
             {
                edit_flag = 1;
                log_level = 201;
                log_detail = "�y��{�C���z"+ "\r\n";
                log_detail = log_detail + "�ύX�O���";
                if (loginid.compareTo(old_loginid) != 0)                 log_detail = log_detail + "���O�C��ID:"           + ReplaceString.SQLEscape(old_loginid) + ",\r\n";
                if (name.compareTo(old_name) != 0)                       log_detail = log_detail + "���[�U�[��:"           + ReplaceString.SQLEscape(old_name) + ",\r\n";
                if (passwd_pc.compareTo(old_passwd_pc) != 0)             log_detail = log_detail + "�p�X���[�h(PC):"       + ",\r\n";
                if (passwd_mobile.compareTo(old_passwd_mobile) != 0)     log_detail = log_detail + "�p�X���[�h(�g��):"     + ",\r\n";
                if (mailaddr_pc.compareTo(old_mailaddr_pc) != 0)         log_detail = log_detail + "PC���[���A�h���X:"     + ReplaceString.SQLEscape(old_mailaddr_pc) + ",\r\n";
                if (mailaddr_mobile.compareTo(old_mailaddr_mobile) != 0) log_detail = log_detail + "�g�у��[���A�h���X:"   + ReplaceString.SQLEscape(old_mailaddr_mobile) + ",\r\n";
                if (machineid.compareTo(old_machineid) != 0)             log_detail = log_detail + "�g�ѐ����ԍ�:"         + ReplaceString.SQLEscape(old_machineid) + ",\r\n";
                if (Integer.parseInt(imedia_user) != old_imedia_user)    log_detail = log_detail + "�A�����b�N�X���[�U�[:" + old_imedia_user + ",\r\n";
                if (old_unknown_flag_pc == 1)                            log_detail = log_detail + "Unknown PC:"    + old_unknown_flag_pc + ",\r\n";
                if (old_unknown_flag_mobile == 1)                        log_detail = log_detail + "Unknown �g��:"  + old_unknown_flag_mobile + ",\r\n";

                if (input_admin_flag == 1)
                {
                    if (Integer.parseInt(report_flag) != old_report_flag) log_detail = log_detail + "���チ�[���ݒ�:" + old_report_flag + ",\r\n";
                }
             }
             query = "UPDATE hotenavi.owner_user SET ";
        }
        query = query + "loginid='"         + ReplaceString.SQLEscape(loginid) + "', ";
        query = query + "name='"            + ReplaceString.SQLEscape(name) + "', ";
        query = query + "machineid='"       + ReplaceString.SQLEscape(machineid) + "', ";
        query = query + "imedia_user="      + Integer.parseInt(imedia_user) + ", ";
        query = query + "unknown_flag_pc=0,";
        query = query + "unknown_flag_mobile=0,";
        if (passwd_pc_new != null)
        {
            if (passwd_pc_new.equals("1"))
            {
                query = query + "passwd_pc_update=0, ";
            }
        }
        else if (passwd_pc.compareTo(old_passwd_pc) != 0)
        {
            query = query + "passwd_pc_update=" + nowdate + ", ";
        }
        if (passwd_mobile_new != null)
        {
            if (passwd_mobile_new.equals("1") && machineid.length() > 10)
            {
                query = query + "passwd_mobile_update=0, ";
            }
        }
        else if (passwd_mobile.compareTo(old_passwd_mobile) != 0)
        {
            query = query + "passwd_mobile_update=" + nowdate + ", ";
        }
        if (mail_change_flag ==  1)
        {
            query = query + "report_flag=0,";
        }
        else
        {
            if (input_admin_flag == 1)
            {
            query = query + "report_flag="      + Integer.parseInt(report_flag) + ", ";
            }
        }
        query = query + "level=" + Integer.parseInt(user_level) + " ";
        queryGcp = query;
        //Gcp�ɂ͈Í������ځA�n�b�V�������ڂ͑��M���Ȃ�

        query = query + ",passwd_pc='"       + ReplaceString.SQLEscape(passwd_pc) + "', ";
        query = query + "passwd_mobile='"   + ReplaceString.SQLEscape(passwd_mobile) + "', ";
        query = query + "mailaddr_pc='"     + ReplaceString.SQLEscape(mailaddr_pc) + "', ";
        query = query + "mailaddr_mobile='" + ReplaceString.SQLEscape(mailaddr_mobile) + "' ";
        if( userid.compareTo("0") != 0 )
        {
            query = query + " WHERE hotelid='" + loginHotelId + "' AND userid=" + Integer.parseInt(userid);
            queryGcp = queryGcp + " WHERE hotelid='" + loginHotelId + "' AND userid=" + Integer.parseInt(userid);
        }
        db_update =  new DbAccess();
        ret = db_update.execUpdate(query);
        db_update.close();
        if (apiUrl.indexOf("stg") != -1)
        {
            DBSync.publish(queryGcp,true);
        }
        else
        {
            DBSync.publish(queryGcp);
        }

        String  param_token   = ReplaceString.getParameter(request,"token");
        if (param_token == null)
        {
            param_token = "";
        }
        param_token           = ReplaceString.HTMLEscape(param_token); 
        if( userid.compareTo("0") == 0 )  // �V�K�o�^�̏ꍇ�A�o�^���ꂽ�@userid ��ǂݍ���
        {
            query  = "SELECT * FROM owner_user WHERE hotelid=?";
            query  = query  + " ORDER BY userid DESC";
            db     = DBConnection.getConnection();
            st     =db.prepareStatement(query);
            st.setString(1, loginHotelId);
            result = st.executeQuery();
            if( result != null )
            {
                if(result.next() != false)
                {
                   userid = Integer.toString(result.getInt("userid"));
                   DBSync.publish("UPDATE hotenavi.owner_user_token SET update_userid =" + userid + " WHERE token = '" + param_token + "'");
                }
            }
            DBConnection.releaseResources(result,st,db);
         }
//1.(5) �����ւ̏������ݏ���
        if (log_detail.compareTo("") != 0)
        {
            query = "INSERT INTO hh_owner_user_log SET ";
            query = query + "hotel_id='"           + ReplaceString.SQLEscape(loginHotelId)   + "', ";
            query = query + "user_id="             + Integer.parseInt(userid)                + ", ";
            query = query + "login_date="          + nowdate                                 + ", ";
            query = query + "login_time="          + nowtime                                 + ", ";
            query = query + "login_name='"         + loginHotelId                            + "', ";
            query = query + "passwd='"             + Integer.toString(loginUserId)           + "', ";
            query = query + "log_level="           + log_level                               + ", ";
            query = query + "log_detail='"         + ReplaceString.SQLEscape(log_detail)     + "' ";
            db_update =  new DbAccess();
            ret = db_update.execUpdate(query);
            db_update.close();
            mail_message  = mail_message + log_detail;
            log_detail    = "";
        }
%>
      <link href="../../common/pc/style/loading.css" rel="stylesheet" type="text/css" media="screen, print" />
      <script type="text/javascript" src="../../common/pc/scripts/jquery.js"></script>
      <script type="text/javascript" src="../../common/pc/scripts/loading.js"></script>
      <script type="text/javascript">
      $(function(){
          dispLoading('�f�[�^�X�V��...');
          setTimeout("updateUserData()",2000);
      });
      function updateUserData(){
          var JSONdata = {
              "token": "<%=param_token%>",
              "operate_hotelid": "<%=loginHotelId%>",
              "operate_userid": <%=ownerinfo.DbUserId%>,
              "update_hotelid": "<%=loginHotelId%>",
              "update_userid": <%=Integer.parseInt(userid)%>,
              "mailaddr_pc": "<%=Base64Manager.encode(mailaddr_pc)%>",
              "passwd_pc": "<%=Base64Manager.encode(passwd_pc)%>",
              "mailaddr_mobile": "<%=Base64Manager.encode(mailaddr_mobile)%>",
              "passwd_mobile": "<%=Base64Manager.encode(passwd_mobile)%>"
          };
//         console.log('<%=apiUrl%>');
//         console.log(JSON.stringify(JSONdata));
          $.ajax({
              type : 'POST',
              url : '<%=apiUrl%>',
              headers: {
                  'Content-Type': 'application/json'
              },
              data : JSON.stringify(JSONdata),
              dataType : 'JSON',
              scriptCharset: 'UTF-8'
          }).done(function (responseJson) {
              removeLoading();
              var results = responseJson.results;
//                 console.log(JSON.stringify(responseJson));
              if (results.success) {
              } else {
                 $('#passwordMessage').html("<font color=red><strong>�n�b�s�[�z�e���I�[�i�[�T�C�g�̃p�X���[�h�ύX�Ɏ��s���܂����B</strong></font><br>"+results.error.message);
              }
          });
      }
  </script>
<%



        //���A�h��ύX�����ꍇ�̏���
        if (mail_change_flag ==  1)
        {
            ret_user = 0;
            if (sales_userid.compareTo("0") != 0)
            {
                query  = "SELECT * FROM owner_user WHERE hotelid=?";
                query  = query + " AND  userid=?";
                query  = query + " AND  level=2";
                db     = DBConnection.getConnection();
                st     =db.prepareStatement(query);
                st.setString(1, loginHotelId);
                st.setInt(2, Integer.parseInt(sales_userid));
                result = st.executeQuery();
                if( result != null )
                {
                    if(result.next() != false)
                    {
                        ret_user = 1;     //�o�^��
                    }
                }
                DBConnection.releaseResources(result,st,db);
            }
            if( ret_user == 0)  // �V�K�o�^
            {
                int useridNew = 0;
        %><%@ include file="../../common/pc/getUserIdNew.jsp" %><%
                useridNew = useridNew + 1;
                edit_flag = 1;
                log_level = 200;
                log_detail = "�y�V�K�o�^�z" + "\r\n";
                query = "INSERT INTO hotenavi.owner_user SET ";
                query = query + "hotelid='" + loginHotelId + "', ";
                query = query + "userid=" + useridNew + ", ";
                query = query + "level=" + "2" + ", ";
            }
            else
            {
                if ((sales_mailaddr_pc.compareTo(old_mailaddr_pc) != 0 && old_mailaddr_pc.compareTo("")!= 0)||
                    (sales_mailaddr_mobile.compareTo(old_mailaddr_mobile) != 0 && old_mailaddr_mobile.compareTo("")!= 0))
                {
                    edit_flag = 1;
                    log_level = 201;
                    log_detail = "�y��{�C���z"+ "\r\n";
                    log_detail = log_detail + "�ύX�O���";
                    if (sales_mailaddr_pc.compareTo(old_mailaddr_pc) != 0 && old_mailaddr_pc.compareTo("")!= 0)             log_detail = log_detail + "PC���[���A�h���X:"     + ReplaceString.SQLEscape(sales_mailaddr_pc) + ",\r\n";
                    if (sales_mailaddr_mobile.compareTo(old_mailaddr_mobile) != 0 && old_mailaddr_mobile.compareTo("")!= 0) log_detail = log_detail + "�g�у��[���A�h���X:"   + ReplaceString.SQLEscape(sales_mailaddr_mobile) + ",\r\n";
                 }
                query = "UPDATE hotenavi.owner_user SET ";
            }
            query = query + "loginid='"         + ReplaceString.SQLEscape(loginid) + "', ";
            query = query + "name='"            + ReplaceString.SQLEscape(name) + "(���チ�[����p�j', ";
            query = query + "machineid='',";
            query = query + "imedia_user="      + Integer.parseInt(imedia_user) + ", ";
            query = query + "report_flag="      + Integer.parseInt(report_flag) + " ";
            query = query + ",passwd_pc='', ";
            query = query + ",passwd_mobile='' ";
            queryGcp = query;
            if (old_mailaddr_pc.compareTo("")!= 0)
            query = query + ",mailaddr_pc='"     + ReplaceString.SQLEscape(old_mailaddr_pc) + "' ";
            if (old_mailaddr_mobile.compareTo("")!= 0)
            query = query + ",mailaddr_mobile='" + ReplaceString.SQLEscape(old_mailaddr_mobile) + "', ";
            if( ret_user == 1)
            {
                query = query + " WHERE hotelid='" + loginHotelId + "' AND userid=" + Integer.parseInt(sales_userid) + " AND level=2";
            }
            db_update =  new DbAccess();
            ret = db_update.execUpdate(query);
            db_update.close();
            if (apiUrl.indexOf("stg") != -1)
            {
                DBSync.publish(queryGcp,true);
            }
            else
            {
                DBSync.publish(queryGcp);
            }

            if( ret_user == 0)  // �V�K�o�^�̏ꍇ�A�o�^���ꂽ�@userid ��ǂݍ���
            {
                query  = "SELECT * FROM owner_user WHERE hotelid=?";
                query  = query + " AND  loginid=?";
                query  = query + " AND  level=2";
                db     = DBConnection.getConnection();
                st     =db.prepareStatement(query);
                st.setString(1, loginHotelId);
                st.setString(2, loginid);
                result = st.executeQuery();
                if( result != null )
                {
                    if(result.next() != false)
                    {
                       sales_userid = Integer.toString(result.getInt("userid"));
                    }
                }
                DBConnection.releaseResources(result,st,db);
             }

            if (log_detail.compareTo("") != 0)
            {
                query = "INSERT INTO hh_owner_user_log SET ";
                query = query + "hotel_id='"           + ReplaceString.SQLEscape(loginHotelId)   + "', ";
                query = query + "user_id="             + Integer.parseInt(sales_userid)          + ", ";
                query = query + "login_date="          + nowdate                                 + ", ";
                query = query + "login_time="          + nowtime                                 + ", ";
                query = query + "login_name='"         + loginHotelId                            + "', ";
                query = query + "passwd='"             + Integer.toString(loginUserId)           + "', ";
                query = query + "log_level="           + log_level                               + ", ";
                query = query + "log_detail='"         + ReplaceString.SQLEscape(log_detail)     + "' ";
                db_update =  new DbAccess();
                ret = db_update.execUpdate(query);
                db_update.close();
                mail_message  = mail_message + log_detail;
                log_detail    = "";
            }
        }
    }
//��2.owner_user_security �������ݏ���

    String admin_flag   = ReplaceString.getParameter(request,"admin_flag");
    if    (admin_flag  == null)   admin_flag  = "0";
    String level[]      = new String[30];

    int    old_admin_flag = 0;
    int    old_level[]  = new int[30];

    for(i = 1; i <= 30;i++)
    {
        level[i-1]      = ReplaceString.getParameter(request,"sec_level" + nf.format(i));
        if( level[i-1] == null ) level[i-1] = "0";
    }

if  (ret_error == 0)
{
//2.(1) �Z�L�����e�B�t�@�C���o�^�`�F�b�N
    int   ret_security = 0;
    query  = "SELECT * FROM owner_user_security WHERE hotelid=?";
    query  = query + " AND  userid=?";
    db     = DBConnection.getConnection();
    st     = db.prepareStatement(query);
    st.setString(1, loginHotelId);
    st.setInt(2, Integer.parseInt(userid));
    result = st.executeQuery();
    if (result != null )
    {
        if (result.next() != false)
        {
            ret_security = 1;     //�o�^��
            old_admin_flag      = result.getInt("admin_flag");
            if (old_admin_flag != Integer.parseInt(admin_flag))
            {
                edit_flag = 1;
                log_detail = "�y�����ύX�z"+ "\r\n";
                log_level = 210;
            }
            for(i = 1; i <= 20;i++)
            {
                old_level[i-1]      = result.getInt("sec_level" + nf.format(i));
                if (old_level[i-1] != Integer.parseInt(level[i-1]))
                {
                    edit_flag = 1;
                    log_detail = "�y�����ύX�z"+ "\r\n";
                    log_level = 210;
                }
            }
        }
    }
    DBConnection.releaseResources(result,st,db);

//2.(2) �������ݏ���
    if( ret_security == 0 )  // �V�K�o�^
    {
        query = "INSERT INTO hotenavi.owner_user_security SET ";
        query = query + "hotelid='" + loginHotelId + "', ";
        query = query + "userid="   + Integer.parseInt(userid) + ", ";
    }
    else
    {
        query = "UPDATE hotenavi.owner_user_security SET ";
    }
        query = query + "admin_flag  =" + Integer.parseInt(admin_flag) + ", ";
        query = query + "sec_level01 =" + Integer.parseInt(level[0])   + ",";
        query = query + "sec_level02 =" + Integer.parseInt(level[1])   + ",";
        query = query + "sec_level03 =" + Integer.parseInt(level[2])   + ",";
        query = query + "sec_level04 =" + Integer.parseInt(level[3])   + ",";
        query = query + "sec_level05 =" + Integer.parseInt(level[4])   + ",";
        query = query + "sec_level06 =" + Integer.parseInt(level[5])   + ",";
        query = query + "sec_level07 =" + Integer.parseInt(level[6])   + ",";
        query = query + "sec_level08 =" + Integer.parseInt(level[7])   + ",";
        query = query + "sec_level09 =" + Integer.parseInt(level[8])   + ",";
        query = query + "sec_level10 =" + Integer.parseInt(level[9])   + ",";
        query = query + "sec_level11 =" + Integer.parseInt(level[10])  + ","; //555�X�y�V�����Ȃ̂ŏ��������Ȃ�
        query = query + "sec_level12 =" + Integer.parseInt(level[11])  + ",";
        query = query + "sec_level13 =" + Integer.parseInt(level[12])  + ",";
        query = query + "sec_level14 =" + Integer.parseInt(level[13])  + ",";
        query = query + "sec_level15 =" + Integer.parseInt(level[14])  + ",";
        query = query + "sec_level16 =" + Integer.parseInt(level[15])  + ",";
        query = query + "sec_level17 =" + Integer.parseInt(level[16])  + ",";
        query = query + "sec_level18 =" + Integer.parseInt(level[17])  + ",";
        query = query + "sec_level19 =" + Integer.parseInt(level[18])  + ",";
        query = query + "sec_level20 =" + Integer.parseInt(level[19])  + ",";
        query = query + "sec_level21 =" + Integer.parseInt(level[20])  + ",";
        query = query + "sec_level22 =" + Integer.parseInt(level[21])  + ",";
        query = query + "sec_level23 =" + Integer.parseInt(level[22])  + " ";
    if( ret_security == 1 )
    {
        query = query + " WHERE hotelid='" + loginHotelId + "' AND userid=" + Integer.parseInt(userid);
    }
    db_update =  new DbAccess();
    ret = db_update.execUpdate(query);
    db_update.close();
    if (apiUrl.indexOf("stg") != -1)
    {
        DBSync.publish(query,true);
    }
    else
    {
        DBSync.publish(query);
    }

//2.(3) �����ւ̏������ݏ���
    if (log_detail.compareTo("") != 0)
    {
        log_detail = log_detail + " �ύX�O���";
        log_detail = log_detail + old_admin_flag + ",";
        log_detail = log_detail + old_level[0]  + old_level[1]  + old_level[2]  + old_level[6]  + ",";
        log_detail = log_detail + old_level[8]  + ",";
        log_detail = log_detail + old_level[3]  + old_level[4]  + old_level[5]  + ",";
        log_detail = log_detail + old_level[8]  + old_level[10] + ",";
        log_detail = log_detail + old_level[14] + old_level[15] + old_level[16] + old_level[17] + ",";
        log_detail = log_detail + old_level[18] + old_level[20] + old_level[21] + ",";
        log_detail = log_detail + old_level[19] + old_level[22] + " ";
        log_detail = log_detail + "\r\n";

        query = "INSERT INTO hh_owner_user_log SET ";
        query = query + "hotel_id='"           + ReplaceString.SQLEscape(loginHotelId)   + "', ";
        query = query + "user_id="             + Integer.parseInt(userid)                + ", ";
        query = query + "login_date="          + nowdate                                 + ", ";
        query = query + "login_time="          + nowtime                                 + ", ";
        query = query + "login_name='"         + loginHotelId                            + "', ";
        query = query + "passwd='"             + loginUserId                             + "', ";
        query = query + "log_level="           + log_level                               + ", ";
        query = query + "log_detail='"         + ReplaceString.SQLEscape(log_detail)     + "' ";
        db_update =  new DbAccess();
        ret = db_update.execUpdate(query);
        db_update.close();
        mail_message  = mail_message + log_detail;
        log_detail    = "";
    }

//2.(4) ���チ�A�h�ύX���̏���
if (mail_change_flag ==  1)
{
    ret_security = 0;
    query  = "SELECT * FROM owner_user_security WHERE hotelid='" + loginHotelId + "'";
    query  = query + " AND  userid=" + Integer.parseInt(sales_userid);
    db     = DBConnection.getConnection();
    st     =db.prepareStatement(query);
    result = st.executeQuery();
    if (result != null )
    {
        if (result.next() != false)
        {
            ret_security = 1;     //�o�^��
        }
    }
    DBConnection.releaseResources(result,st,db);

//2.(2) �������ݏ���
    if( ret_security == 0 )  // �V�K�o�^
    {
        query = "INSERT INTO hotenavi.owner_user_security SET ";
        query = query + "hotelid='" + loginHotelId + "', ";
        query = query + "userid="   + Integer.parseInt(sales_userid) + ", ";
    }
    else
    {
        query = "UPDATE hotenavi.owner_user_security SET ";
    }
        query = query + "admin_flag  =0,";
        query = query + "sec_level01 =0,";
        query = query + "sec_level02 =0,";
        query = query + "sec_level03 =0,";
        query = query + "sec_level04 =0,";
        query = query + "sec_level05 =0,";
        query = query + "sec_level06 =0,";
        query = query + "sec_level07 =0,";
        query = query + "sec_level08 =0,";
        query = query + "sec_level09 =0,";
        query = query + "sec_level10 =0,";
        query = query + "sec_level11 =0,"; //555�X�y�V�����Ȃ̂ŏ������܂Ȃ�
        query = query + "sec_level12 =0,";
        query = query + "sec_level13 =0,";
        query = query + "sec_level14 =0,";
        query = query + "sec_level15 =0,";
        query = query + "sec_level16 =0,";
        query = query + "sec_level17 =0,";
        query = query + "sec_level18 =0,";
        query = query + "sec_level19 =0,";
        query = query + "sec_level20 =0,";
        query = query + "sec_level21 =0,";
        query = query + "sec_level22 =0,";
        query = query + "sec_level23 =0";
    if( ret_security == 1 )
    {
        query = query + " WHERE hotelid='" + loginHotelId + "' AND userid=" + Integer.parseInt(sales_userid);
    }
    db_update =  new DbAccess();
    ret = db_update.execUpdate(query);
    db_update.close();
    if (apiUrl.indexOf("stg") != -1)
    {
        DBSync.publish(query,true);
    }
    else
    {
        DBSync.publish(query);
    }
}

//��3.owner_user_hotel ��������,�폜����
    String count               = ReplaceString.getParameter(request,"count");
    String accept_hotelid      = "";
    String accept_hotel        = "";
    String report_daily_pc     = "";
    String report_daily_mobile = "";
    String report_month_pc     = "";
    String report_month_mobile = "";
    String report_times        = "";
    String report_timee        = "";
    int    old_report_daily_pc     = 0;
    int    old_report_daily_mobile = 0;
    int    old_report_month_pc     = 0;
    int    old_report_month_mobile = 0;
    int    old_report_times        = 0;
    int    old_report_timee        = 0;
    int    ret_hotel               = 0;
    dbh   = DBConnection.getConnection();
    for(i = 1;i <= Integer.parseInt(count);i++)
    {
//3.(1)�Ǘ��X�܂̓o�^�L���`�F�b�N
        ret_hotel = 0;
        accept_hotelid =  ReplaceString.getParameter(request,"hotelid" + i);
        accept_hotel   =  ReplaceString.getParameter(request,"hotel" + i);
        if (accept_hotel != null)
        {
            report_times           = ReplaceString.getParameter(request,"report_times" + i);
            if (report_times       == null)                report_times           = "0";
            if (report_times.compareTo("") == 0)           report_times           = "0";
            report_timee           = ReplaceString.getParameter(request,"report_timee" + i);
            if (report_timee       == null)                report_timee           = "0";
            if (report_timee.compareTo("") == 0)           report_timee           = "0";
            report_daily_pc        = ReplaceString.getParameter(request,"report_daily_pc" + i);
            if (report_daily_pc    == null     )           report_daily_pc        = "0";
            report_daily_mobile    = ReplaceString.getParameter(request,"report_daily_mobile" + i);
            if (report_daily_mobile == null     )          report_daily_mobile    = "0";
            report_month_pc        = ReplaceString.getParameter(request,"report_month_pc" + i);
            if (report_month_pc    == null     )           report_month_pc        = "0";
            report_month_mobile    = ReplaceString.getParameter(request,"report_month_mobile" + i);
            if (report_month_mobile == null     )          report_month_mobile    = "0";
            count_report_hotel++;
        }
        //���A�h���ύX���ꂽ��A���M���Ȃ��B
        if (mail_change_flag ==  1 || (input_admin_flag == 0 && old_report_flag == 0))
        {
            report_times           = "0";
            report_timee           = "0";
            report_daily_pc        = "0";
            report_daily_mobile    = "0";
            report_month_pc        = "0";
            report_month_mobile    = "0";
        }

        query  = "SELECT * FROM owner_user_hotel WHERE hotelid = ? ";
        query  = query + " AND  userid = ? ";
        query  = query + " AND  accept_hotelid = ? ";
        sth    = dbh.prepareStatement(query);
        sth.setString(1, loginHotelId);
        sth.setInt(2, Integer.parseInt(userid));
        sth.setString(3, accept_hotelid);
        reth   = sth.executeQuery();
        if (reth != null)
        {
            if (reth.next() != false)
            {
                ret_hotel = 1;
                old_report_daily_pc     = reth.getInt("report_daily_pc");
                old_report_daily_mobile = reth.getInt("report_daily_mobile");
                old_report_month_pc     = reth.getInt("report_month_pc");
                old_report_month_mobile = reth.getInt("report_month_mobile");
                old_report_times        = reth.getInt("report_times");
                old_report_timee        = reth.getInt("report_timee");
            }
        }
        DBConnection.releaseResources(reth);
        DBConnection.releaseResources(sth);


//3.(2)�`�F�b�N�������Ă��Ȃ������o�^�@�ˁ@�폜����
        if (accept_hotel == null && ret_hotel == 1)
        {
           log_detail = "�y�Ǘ��X�܍폜�z";
           log_detail = log_detail + accept_hotelid + "\r\n";
           log_detail = log_detail + "�폜�O���" + old_report_daily_pc + "," +old_report_daily_pc + "," + old_report_month_pc + "," +old_report_month_pc + ",(" + old_report_times + "-" + old_report_timee + ")";           log_level = 222;
           query = "DELETE FROM hotenavi.owner_user_hotel WHERE hotelid='" + loginHotelId + "'";
           query = query + " AND userid=" + Integer.parseInt(userid);
           query = query + " AND accept_hotelid='" + accept_hotelid + "'";
           db_update =  new DbAccess();
           ret = db_update.execUpdate(query);
           db_update.close();
           if (apiUrl.indexOf("stg") != -1)
           {
               DBSync.publish(query,true);
           }
           else
           {
               DBSync.publish(query);
           }
        }
//3.(3)�`�F�b�N�������Ă��違���o�^�@�ˁ@�ǉ�����
        else if (accept_hotel != null && ret_hotel == 0)
        {
           log_detail = "�y�Ǘ��X�ܒǉ��z";
           log_detail = log_detail + accept_hotelid + "\r\n";
           log_level = 220;
           query = "INSERT INTO hotenavi.owner_user_hotel SET ";
           query = query + "hotelid='"            + ReplaceString.SQLEscape(loginHotelId)        + "', ";
           query = query + "userid="              + Integer.parseInt(userid)                + ", ";
           query = query + "accept_hotelid='"     + ReplaceString.SQLEscape(accept_hotelid) + "', ";
           query = query + "report_times="        + Integer.parseInt(report_times)          + ", ";
           query = query + "report_timee="        + Integer.parseInt(report_timee)          + ", ";
           query = query + "report_daily_pc="     + Integer.parseInt(report_daily_pc)       + ", ";
           query = query + "report_daily_mobile=" + Integer.parseInt(report_daily_mobile)   + ", ";
           query = query + "report_month_pc="     + Integer.parseInt(report_month_pc)       + ", ";
           query = query + "report_month_mobile=" + Integer.parseInt(report_month_mobile)   + " ";
           db_update =  new DbAccess();
           ret = db_update.execUpdate(query);
           db_update.close();
           if (apiUrl.indexOf("stg") != -1)
           {
               DBSync.publish(query,true);
           }
           else
           {
               DBSync.publish(query);
           }
        }
        else if ((accept_hotel != null && ret_hotel == 1) &&
                (old_report_times       != Integer.parseInt(report_times)       ||
                old_report_timee        != Integer.parseInt(report_timee)        ||
                old_report_daily_pc     != Integer.parseInt(report_daily_pc)     ||
                old_report_daily_mobile != Integer.parseInt(report_daily_mobile) ||
                old_report_month_pc     != Integer.parseInt(report_month_pc)     ||
                old_report_month_mobile != Integer.parseInt(report_month_mobile) ))
        {
           log_detail = "�y�Ǘ��X�܏C���z";
           log_detail = log_detail + accept_hotelid + "\r\n";
           log_detail = log_detail + "�ύX�O���" + old_report_daily_pc + "," +old_report_daily_pc + "," + old_report_month_pc + "," +old_report_month_pc + ",(" + old_report_times + "-" + old_report_timee + ")";
           log_level = 221;
           query = "UPDATE hotenavi.owner_user_hotel SET ";
           query = query + "report_times="        + Integer.parseInt(report_times)          + ", ";
           query = query + "report_timee="        + Integer.parseInt(report_timee)          + ", ";
           query = query + "report_daily_pc="     + Integer.parseInt(report_daily_pc)       + ", ";
           query = query + "report_daily_mobile=" + Integer.parseInt(report_daily_mobile)   + ", ";
           query = query + "report_month_pc="     + Integer.parseInt(report_month_pc)       + ", ";
           query = query + "report_month_mobile=" + Integer.parseInt(report_month_mobile)   + " ";
           query = query + " WHERE hotelid='" + loginHotelId + "'";
           query = query + " AND userid=" + Integer.parseInt(userid);
           query = query + " AND accept_hotelid='" + accept_hotelid + "'";
           db_update =  new DbAccess();
           ret = db_update.execUpdate(query);
           db_update.close();
           if (apiUrl.indexOf("stg") != -1)
           {
               DBSync.publish(query,true);
           }
           else
           {
               DBSync.publish(query);
           }
        }

//3.(4)�����̏�������
        if (log_detail.compareTo("") != 0)
        {
            query = "INSERT INTO hh_owner_user_log SET ";
            query = query + "hotel_id='"           + ReplaceString.SQLEscape(loginHotelId)   + "', ";
            query = query + "user_id="             + Integer.parseInt(userid)                + ", ";
            query = query + "login_date="          + nowdate                                 + ", ";
            query = query + "login_time="          + nowtime                                 + ", ";
            query = query + "login_name='"         + loginHotelId                            + "', ";
            query = query + "passwd='"             + loginUserId                             + "', ";
            query = query + "log_level="           + log_level                               + ", ";
            query = query + "log_detail='"         + ReplaceString.SQLEscape(log_detail)     + "' ";
            db_update =  new DbAccess();
            ret = db_update.execUpdate(query);
            db_update.close();
            mail_message  = mail_message + log_detail;
            log_detail    = "";
        }
    }
    DBConnection.releaseResources(dbh);

//3.(5) ���チ�A�h�ύX���̏���
if (mail_change_flag ==  1)
{
    dbh   = DBConnection.getConnection();
    for(i = 1;i <= Integer.parseInt(count);i++)
    {
//�Ǘ��X�܂̓o�^�L���`�F�b�N
        ret_hotel = 0;
        accept_hotelid =  ReplaceString.getParameter(request,"hotelid" + i);
        accept_hotel   =  ReplaceString.getParameter(request,"hotel" + i);
        if (accept_hotel != null)
        {
            report_times           = ReplaceString.getParameter(request,"report_times" + i);
            if (report_times       == null)                report_times           = "0";
            if (report_times.compareTo("") == 0)           report_times           = "0";
            report_timee           = ReplaceString.getParameter(request,"report_timee" + i);
            if (report_timee       == null)                report_timee           = "0";
            if (report_timee.compareTo("") == 0)           report_timee           = "0";
            report_daily_pc        = ReplaceString.getParameter(request,"report_daily_pc" + i);
            if (report_daily_pc    == null     )           report_daily_pc        = "0";
            report_daily_mobile    = ReplaceString.getParameter(request,"report_daily_mobile" + i);
            if (report_daily_mobile == null     )          report_daily_mobile    = "0";
            report_month_pc        = ReplaceString.getParameter(request,"report_month_pc" + i);
            if (report_month_pc    == null     )           report_month_pc        = "0";
            report_month_mobile    = ReplaceString.getParameter(request,"report_month_mobile" + i);
            if (report_month_mobile == null     )          report_month_mobile    = "0";
        }

        query  = "SELECT * FROM owner_user_hotel WHERE hotelid = ? ";
        query  = query + " AND  userid = ? ";
        query  = query + " AND  accept_hotelid = ? ";
        sth    = dbh.prepareStatement(query);
        sth.setString(1, loginHotelId);
        sth.setInt(2, Integer.parseInt(sales_userid));
        sth.setString(3, accept_hotelid);
        reth   = sth.executeQuery();
        if (reth != null)
        {
            if (reth.next() != false)
            {
                ret_hotel = 1;
                old_report_daily_pc     = reth.getInt("report_daily_pc");
                old_report_daily_mobile = reth.getInt("report_daily_mobile");
                old_report_month_pc     = reth.getInt("report_month_pc");
                old_report_month_mobile = reth.getInt("report_month_mobile");
                old_report_times        = reth.getInt("report_times");
                old_report_timee        = reth.getInt("report_timee");
            }
        }
        DBConnection.releaseResources(reth);
        DBConnection.releaseResources(sth);

        if (accept_hotel != null)
        {
            if(ret_hotel == 0)
            {
                log_detail = "�y�Ǘ��X�ܒǉ��z";
                log_detail = log_detail + accept_hotelid + "\r\n";
                log_level = 220;
                query = "INSERT INTO hotenavi.owner_user_hotel SET ";
                query = query + "hotelid='"            + ReplaceString.SQLEscape(loginHotelId)   + "', ";
                query = query + "userid="              + Integer.parseInt(sales_userid)          + ", ";
                query = query + "accept_hotelid='"     + ReplaceString.SQLEscape(accept_hotelid) + "', ";
                query = query + "report_times="        + Integer.parseInt(report_times)          + ", ";
                query = query + "report_timee="        + Integer.parseInt(report_timee)          + ", ";
                query = query + "report_daily_pc="     + Integer.parseInt(report_daily_pc)       + ", ";
                query = query + "report_daily_mobile=" + Integer.parseInt(report_daily_mobile)   + ", ";
                query = query + "report_month_pc="     + Integer.parseInt(report_month_pc)       + ", ";
                query = query + "report_month_mobile=" + Integer.parseInt(report_month_mobile)   + " ";
                db_update =  new DbAccess();
                ret = db_update.execUpdate(query);
                db_update.close();
                if (apiUrl.indexOf("stg") != -1)
                {
                   DBSync.publish(query,true);
                }
                else
                {
                   DBSync.publish(query);
                }
            }
            else if
                (old_report_times       != Integer.parseInt(report_times)       ||
                old_report_timee        != Integer.parseInt(report_timee)        ||
                old_report_daily_pc     != Integer.parseInt(report_daily_pc)     ||
                old_report_daily_mobile != Integer.parseInt(report_daily_mobile) ||
                old_report_month_pc     != Integer.parseInt(report_month_pc)     ||
                old_report_month_mobile != Integer.parseInt(report_month_mobile) )
           {
                log_detail = "�y�Ǘ��X�܏C���z";
                log_detail = log_detail + accept_hotelid + "\r\n";
                log_detail = log_detail + "�ύX�O���" + old_report_daily_pc + "," +old_report_daily_pc + "," + old_report_month_pc + "," +old_report_month_pc + ",(" + old_report_times + "-" + old_report_timee + ")";
                log_level = 221;
                query = "UPDATE hotenavi.owner_user_hotel SET ";
                query = query + "report_times="        + Integer.parseInt(report_times)          + ", ";
                query = query + "report_timee="        + Integer.parseInt(report_timee)          + ", ";
                query = query + "report_daily_pc="     + Integer.parseInt(report_daily_pc)       + ", ";
                query = query + "report_daily_mobile=" + Integer.parseInt(report_daily_mobile)   + ", ";
                query = query + "report_month_pc="     + Integer.parseInt(report_month_pc)       + ", ";
                query = query + "report_month_mobile=" + Integer.parseInt(report_month_mobile)   + " ";
                query = query + " WHERE hotelid='" + loginHotelId + "'";
                query = query + " AND userid=" + Integer.parseInt(sales_userid);
                query = query + " AND accept_hotelid='" + accept_hotelid + "'";
                db_update =  new DbAccess();
                ret = db_update.execUpdate(query);
                db_update.close();
                if (apiUrl.indexOf("stg") != -1)
                {
                   DBSync.publish(query,true);
                }
                else
                {
                   DBSync.publish(query);
                }
            }
        }

//3.(4)�����̏�������
        if (log_detail.compareTo("") != 0)
        {
            query = "INSERT INTO hh_owner_user_log SET ";
            query = query + "hotel_id='"           + ReplaceString.SQLEscape(loginHotelId)   + "', ";
            query = query + "user_id="             + Integer.parseInt(sales_userid)          + ", ";
            query = query + "login_date="          + nowdate                                 + ", ";
            query = query + "login_time="          + nowtime                                 + ", ";
            query = query + "login_name='"         + loginHotelId                            + "', ";
            query = query + "passwd='"             + loginUserId                             + "', ";
            query = query + "log_level="           + log_level                               + ", ";
            query = query + "log_detail='"         + ReplaceString.SQLEscape(log_detail)     + "' ";
            db_update =  new DbAccess();
            ret = db_update.execUpdate(query);
            db_update.close();
            mail_message  = mail_message + log_detail;
            log_detail    = "";
        }
    }
    DBConnection.releaseResources(dbh);
}


//��4.���[���̑��M

    String  group_name = "";
    String  from_mail  = "";
    String  owner_user = "";
    String  owner_password = "";
    query = "SELECT * FROM hotel WHERE hotel_id='" + loginHotelId + "'";
    db     = DBConnection.getConnection();
    st     =db.prepareStatement(query);
    result = st.executeQuery();
    if( result.next() != false )
    {
        from_mail      = result.getString("mail");
        if (from_mail.equals("")) from_mail = "imedia-info@hotenavi.com";
        group_name     = result.getString("name");
        owner_user     = result.getString("owner_user");
        owner_password = result.getString("owner_password");
    }
    DBConnection.releaseResources(result,st,db);

    if (history_seq == 1 && edit_flag == 1)
    {
          // ���[���̑��M�i�n�b�s�[�z�e�������ǈ��j
           String title_mail = "";
           String text = "";
           title_mail  = "�y�z�e�i�r�z[�I�[�i�[�Ǘ��X�V]" + group_name + "�i" + loginHotelId + "�j";
           text = text +  "������X�V���̂݃��[�����Ă��܂��B���̍X�V�ɂ��ẮA�����W�c�[���ɂ�藚�����m�F���Ă�������" + "\r\n";
           text = text +  "https://owner.hotenavi.com/happyhotel/owner/?HotelId=" + loginHotelId + "\r\n";
           text = text +  "\r\n";
		   if(target_system_hotenavi.equals("hotenavi"))
			{
				text = text +  "�y�z�e�i�r�I�[�i�[�T�C�g�z" + "\r\n";
				text = text +  "http://owner.hotenavi.com/" + loginHotelId + "/" + "\r\n";
			}
			if(target_system_hapihote.equals("hapihote"))
			{
				text = text +  "�y�n�s�z�e�I�[�i�[�T�C�g�z" + "\r\n";
				text = text + "https://owner.happyhotel.jp" + "\r\n";
			}
		   if(target_system_reserve.equals("reserve"))
			{
				text = text +  "�y�\��I�[�i�[�T�C�g�z" + "\r\n";
				text = text + "https://owner.happyhotel.jp" + "\r\n";
			}
           text = text +  "�y���[�U�[ID�z" + "\r\n";
           text = text +  userid + "\r\n";
           text = text +  "�y���[�U�[���z" + "\r\n";
           text = text +  loginid + "\r\n";
           text = text +  "�y���O�z" + "\r\n";
           text = text +  name + "\r\n";
           text = text +  mail_message + "\r\n";
           text = text +  "\r\n";
           text = text +  "�y�X�V�S���ҁz" + "\r\n";
           text = text +  "(" + loginUserId + ")" + loginUserName + " �l\r\n";
           SendMail sendmail = new SendMail();
//         sendmail.send(input_mail, "sakurai-t1@almex.jp", title_mail, text);
           sendmail.send(input_mail, "all_imedia@almex.jp", title_mail, text);
    }
//��5.�p�X���[�h�̑��M
    if (passwd_pc_new != null)
    {
        if (passwd_pc_new.equals("1") && passwd_pc_no == null)
        {
           String title_mail = "";
           String text = "";
           log_detail  = "�yPC���p�X���[�h���s���[�����M�z";
           title_mail  = "�y�z�e�i�r�z[�l�p�X���[�h�X�V]" + group_name + "�i" + loginHotelId + "�j";
           text = name + "�@�l\r\n";
           text = text +  "\r\n";
           text = text +  "���I�[�i�[�T�C�g�̌l�p�X���[�h�i���j�����s����܂����B" + "\r\n";
           text = text +  "\r\n";
           if (target_system_hotenavi.equals("hotenavi"))
           {
               text = text +  "�y�z�e�i�r�I�[�i�[�T�C�g�z" + "\r\n";
               text = text +  "https://owner.hotenavi.com/" + loginHotelId + "/" + "\r\n";
           }
           if (target_system_hapihote.equals("hapihote"))
           {
				text = text +  "�y�n�s�z�e�I�[�i�[�T�C�g�z" + "\r\n";
				text = text + "https://owner.happyhotel.jp" + "\r\n";
           }
		   if(target_system_reserve.equals("reserve"))
		   {
				text = text +  "�y�\��I�[�i�[�T�C�g�z" + "\r\n";
				text = text + "https://owner.happyhotel.jp" + "\r\n";
		   }
		   if (target_system_reserve.equals("reserve")|| target_system_hapihote.equals("hapihote") && Integer.parseInt(user_level) != 0 )
           {
				text = text +  "�y���_��ID�z" + "\r\n";
				text = text +  loginHotelId + "\r\n";
           }
           if (passwd_pc_network != null)
           {
               if (passwd_pc_network.equals("1"))
               {
                   log_detail = log_detail + "�l�b�g���[�N�p�X���[�h�t";
                   text = text +  "�y�l�b�g���[�N���[�U�[���z" + "\r\n";
                   text = text +  owner_user + "\r\n";
                   text = text +  "�y�l�b�g���[�N�p�X���[�h�z" + "\r\n";
                   text = text +  owner_password + "\r\n";
               }
           }
           text = text +  "�y�l���[�U�[���z" + "\r\n";
           text = text +  loginid + "\r\n";
           text = text +  "�y�l�p�X���[�h�z" + "\r\n";
           text = text +  passwd_pc + "\r\n";
           text = text +  "\r\n";
           text = text +  "------------------\r\n";
           text = text +  "�I�[�i�[�T�C�g�Ƀ��O�C�����A�l�p�X���[�h��ύX���Ă��������B" + "\r\n";
           text = text +  "------------------\r\n";
           SendMail sendmail = new SendMail();
           sendmail.send(from_mail, mailaddr_pc, title_mail, text);
           sendmail.send("imedia-info@hotenavi.com", "all_imedia@almex.jp", title_mail, text);

           query = "INSERT INTO hh_owner_user_log SET ";
           query = query + "hotel_id='"           + ReplaceString.SQLEscape(loginHotelId)   + "', ";
           query = query + "user_id="             + Integer.parseInt(userid)                + ", ";
           query = query + "login_date="          + nowdate                                 + ", ";
           query = query + "login_time="          + nowtime                                 + ", ";
           query = query + "login_name='"         + loginHotelId                            + "', ";
           query = query + "passwd='"             + loginUserId                             + "', ";
           query = query + "log_level=230,";
           query = query + "log_detail='"         + ReplaceString.SQLEscape(log_detail)     + "' ";
           db_update =  new DbAccess();
           ret = db_update.execUpdate(query);
           db_update.close();
        }
    }
    if (passwd_mobile_new != null)
    {
        if (passwd_mobile_new.equals("1") && passwd_mobile_no == null && machineid.length() > 10)
        {
           String title_mail = "";
           String text = "";
           log_detail  = "�y�g�щ��p�X���[�h���s���[�����M�z";
           title_mail  = "�y�z�e�i�r�z[�p�X���[�h�X�V]" + group_name + "�i" + loginHotelId + "�j";
           text =         "���z�e�i�r�I�[�i�[�T�C�g�̌g�уp�X���[�h�i���j�����s����܂����B" + "\r\n";
           text = text +  "\r\n";
           text = text +  "�y�z�e�i�r�I�[�i�[�T�C�g�z" + "\r\n";
           text = text +  "https://owner.hotenavi.com/" + loginHotelId + "/" + "\r\n";
           text = text +  "�y���O�z" + "\r\n";
           text = text +  name + "�l\r\n";
           text = text +  "�y�g�уp�X���[�h�z" + "\r\n";
           text = text +  passwd_mobile + "\r\n";
           text = text +  "\r\n";
           text = text +  "------------------\r\n";
           text = text +  "�I�[�i�[�T�C�g�Ƀ��O�C�����A�g�уp�X���[�h��ύX���Ă��������B" + "\r\n";
           text = text +  "------------------\r\n";
           SendMail sendmail = new SendMail();
           sendmail.send(from_mail, mailaddr_mobile, title_mail, text);
           sendmail.send("imedia-info@hotenavi.com", "all_imedia@almex.jp", title_mail, text);
           query = "INSERT INTO hh_owner_user_log SET ";
           query = query + "hotel_id='"           + ReplaceString.SQLEscape(loginHotelId)   + "', ";
           query = query + "user_id="             + Integer.parseInt(userid)                + ", ";
           query = query + "login_date="          + nowdate                                 + ", ";
           query = query + "login_time="          + nowtime                                 + ", ";
           query = query + "login_name='"         + loginHotelId                            + "', ";
           query = query + "passwd='"             + loginUserId                             + "', ";
           query = query + "log_level=230,";
           query = query + "log_detail='"         + ReplaceString.SQLEscape(log_detail)     + "' ";
           db_update =  new DbAccess();
           ret = db_update.execUpdate(query);
           db_update.close();
        }
    }
}
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>���[�U�Ǘ�</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#E2D8CF">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="100" height="20" nowrap bgcolor="#22333F" class="tab">
                <font color="#FFFFFF">�o�^�m�F</font></td>
              <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ��������\ -->
      <tr>
        <td align="center" valign="top" bgcolor="#E2D8CF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
          </tr>
          <tr>
            <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="size12"><div id="passwordMessage">
<%
	if (ret_error == 1)
	{
%>
						���[�U�[�������ɓo�^����Ă��܂��B<br>
						�������܂����A�ēx�}�X�^�[�ݒ���s���Ă��������B<br>
<%
	}
	else if(ret_error >= 2)
	{
		switch (ret_error) {
		case 2:
%>
			���[�U�[�o�^/�ύX�����s���܂����B(���[�U�[ID�𐳂������͂��Ă�������)<br>
<%	
			break;
		case 3:
%>
			���[�U�[�o�^/�ύX�����s���܂����B(�l���[�U�[���𐳂������͂��Ă�������)<br>
<%	
			break;
		case 4:
%>
			���[�U�[�o�^/�ύX�����s���܂����B(�g�ѐ����ԍ��𐳂������͂��Ă�������)<br>
<%	
			break;
		case 5:
%>
			���[�U�[�o�^/�ύX�����s���܂����B(�l�p�X���[�h�𐳂������͂��Ă�������)<br>
<%	
			break;
		case 6:
%>
			���[�U�[�o�^/�ύX�����s���܂����B(�g�уp�X���[�h�𐳂������͂��Ă�������)<br>
<%	
			break;
		case 7:
%>
			���[�U�[�o�^/�ύX�����s���܂����B(PC���[���A�h���X�𐳂������͂��Ă�������)<br>
<%	
			break;
		case 8:
%>
			���[�U�[�o�^/�ύX�����s���܂����B(�g�у��[���A�h���X�𐳂������͂��Ă�������)<br>
<%	
			break;
		case 9:
%>
			���[�U�[�o�^/�ύX�����s���܂����B(PC���[���A�h���X(���チ�[����p)�𐳂������͂��Ă�������)<br>
<%	
			break;
		case 10:
%>
			���[�U�[�o�^/�ύX�����s���܂����B(�g�у��[���A�h���X(���チ�[����p))�𐳂������͂��Ă�������)<br>
<%	
			break;
		case 11:
%>
			���[�U�[�o�^/�ύX�����s���܂����B�i���O�𐳂������͂��Ă�������)<br>
<%	
			break;
		default:
%>
			���[�U�[�o�^/�ύX�����s���܂����B(<%=ret_error%>)<br>
<%	
		}
	}
	else
	{
		if( ret != 0 )
		{
%>
						�o�^���܂����B<br>
<%
            if (passwd_pc_new != null)
            {
                if (passwd_pc_new.equals("1"))
                {
%>
						<br>
						<font color=red>PC�I�[�i�[�T�C�g�̉��p�X���[�h�����s����Ă��܂��B</font><br>
						<strong>�y�l���[�U�[���z<%=loginid%></strong><br>
						<strong>�y�l�p�X���[�h�z<%=passwd_pc%></strong><br>
						PC���[���A�h���X���ݒ肳��Ă���ꍇ�́A�S���҈��ɑ��M����Ă��܂��B<br>
						PC���[���A�h���X���ݒ肳��Ă��Ȃ��ꍇ�́A���萔�ł����A�S���҂Ɍl�p�X���[�h�����A�����������B<br>
<%
                }
            }
            if (passwd_mobile_new != null)
            {
                if (passwd_mobile_new.equals("1") && machineid.length() > 10)
                {
%>
						<br>
						<font color=red>�g�уI�[�i�[�T�C�g�̉��p�X���[�h�����s����Ă��܂��B</font><br>
						<strong>�y�g�уp�X���[�h�z<%=passwd_mobile%></strong><br>
						�g�у��[���A�h���X���ݒ肳��Ă���ꍇ�́A�S���҈��ɑ��M����Ă��܂��B<br>
						�g�у��[���A�h���X���ݒ肳��Ă��Ȃ��ꍇ�́A���萔�ł����A�S���҂Ɍg�уp�X���[�h�����A�����������B<br>
<%
                }
            }
            if(count_report_hotel == 0)
            {
%>
<script type="text/javascript">alert("�Ǘ��X�܂��o�^����Ă��܂��񂪁A��낵���ł����H");</script>

						<font color=red>�Ǘ��X�܂��o�^����܂���ł����B��낵���ł����H</font>
<%
            }
		}
		else
		{
%>
						�o�^�Ɏ��s���܂����B<br>
						�������܂����A�ēx�}�X�^�[�ݒ���s���Ă��������B<br>
<%
		}
	}
%>
                    </div></td>
                  </tr>
<% if (input_admin_flag == 1){%>
                  <tr>
                    <td class="size12">
                      <div align="right">
                        <input name="Submit" type="submit" onClick="MM_goToURL('parent.frames[\'mainFrame\']','user_form.jsp?UserId=0');return document.MM_returnValue" value="�V�K�쐬����">
                      </div>
                      &nbsp;</td>
                  </tr>
                  <tr>
                    <td class="size12">
                        <jsp:include page="owner_user_dispNew.jsp" flush="true" >
                             <jsp:param name="UserId" value="" />
                        </jsp:include>
                    </td>
                  </tr>
<%}%>
                </table>
              </div>
            </td>
          </tr>
        </table>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td></td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
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
      <!-- �����܂� -->
    </table></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
