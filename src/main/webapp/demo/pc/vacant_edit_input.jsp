<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>


<%
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    // �z�e��ID�擾
    String hotelid = (String)session.getAttribute("SelectHotel");
    String mode = request.getParameter("Mode");

//�󎺏��p
    String rec_flag           = request.getParameter("rec_flag");          //0:�ʏ�,1:�����o�[��p
    if    (rec_flag          == null)   rec_flag = "0";
    String empty_title        = request.getParameter("empty_title");       //�󎺏��^�C�g��
    if    (empty_title       == null)   empty_title = "�󎺏��";
    String empty_flag         = request.getParameter("empty_flag");        //0:�\���Ȃ�,1:�\������,2:���������N��,3:�\������i"�ȏ�"�Ȃ��j,4:�\�����蕔�������N�ʁi"�ȏ�"�Ȃ��j
    if    (empty_flag        == null)   empty_flag = "0";
    String empty_method       = request.getParameter("empty_method");      //0:�L���\��,999:�����\��,1�`998:�w�萔���������\��
    if    (empty_method      == null)   empty_method = "0";
    String no_vacancies       = request.getParameter("no_vacancies");      //���������b�Z�[�W
    if    (no_vacancies      == null)   no_vacancies = "";
    String vacancies_message  = request.getParameter("vacancies_message"); //�󎺎����b�Z�[�W
    if    (vacancies_message == null)   vacancies_message = "";
    String clean_flag         = request.getParameter("clean_flag");        //0:�\���Ȃ�,1:�\������,2:���������N��,3:�\������i"�ȏ�"�Ȃ��j,4:�\�����蕔�������N�ʁi"�ȏ�"�Ȃ��j,5:�\������iempty_method�ȏ�͕\���Ȃ��j,6:�\�����蕔�������N�ʁiempty_method�ȏ�͕\���Ȃ��j
    if    (clean_flag        == null)   clean_flag = "0";
    String clean_method       = request.getParameter("clean_method");      //0:�L���\��,999:�����\��,1�`998:�w�萔���������\��
    if    (clean_method      == null)   clean_method = "0";
    String empty_list_method  = request.getParameter("empty_list_method"); //0:�\���Ȃ�,999:���ׂĕ\��,1�`998:�����\��
    if    (empty_list_method == null)   empty_list_method = "0";
    String clean_list_method  = request.getParameter("clean_list_method"); //0:�\���Ȃ�,999:���ׂĕ\��,1�`998:�����\��
    if    (clean_list_method == null)   clean_list_method = "0";
    String room_link          = request.getParameter("room_link");         //���������N�ӏ�
    if    (room_link         == null)   room_link = "";
    String line_count         = request.getParameter("line_count");        //�ꗗ��
    if    (line_count        == null)   line_count = "4";
    String allroom_flag       = request.getParameter("allroom_flag");      //1:�S�����\��
    if    (allroom_flag      == null)   allroom_flag = "0";
    String allroom_title      = request.getParameter("allroom_title");    //�S�����\���^�C�g��
    if    (allroom_title     == null)   allroom_title = "0";
    String room_exclude       = request.getParameter("room_exclude");      //�S���\�������O����
    if    (room_exclude      == null)   room_exclude = "";
    String allroom_flag2      = request.getParameter("allroom_flag2");      //1:�S�����\��2
    if    (allroom_flag2     == null)   allroom_flag2 = "0";
    String allroom_title2     = request.getParameter("allroom_title2");    //�S�����\���^�C�g��2
    if    (allroom_title2    == null)   allroom_title2 = "0";
    String room_exclude2      = request.getParameter("room_exclude2");      //�S���\�������O����2
    if    (room_exclude2     == null)   room_exclude2 = "";
    String allroom_flag3      = request.getParameter("allroom_flag3");      //1:�S�����\��3
    if    (allroom_flag3     == null)   allroom_flag3 = "0";
    String allroom_title3     = request.getParameter("allroom_title3");    //�S�����\���^�C�g��3
    if    (allroom_title3    == null)   allroom_title3 = "0";
    String room_exclude3      = request.getParameter("room_exclude3");      //�S���\�������O����3
    if    (room_exclude3     == null)   room_exclude3 = "";
    String allroom_empty_link        = request.getParameter("allroom_empty_link");        //�S���\���󎺎������N�ӏ�
    if    (allroom_empty_link       == null)   allroom_empty_link = "0";
    String allroom_novacancies_link  = request.getParameter("allroom_novacancies_link");  //�S���\���ݎ��������N�ӏ�
    if    (allroom_novacancies_link == null)   allroom_novacancies_link = "0";
    String allroom_clean_link        = request.getParameter("allroom_clean_link");        //�S���\���������������N�ӏ�
    if    (allroom_clean_link       == null)   allroom_clean_link = "0";
    String konzatsu_flag      = request.getParameter("konzatsu_flag");     //1:���G�󋵂���
    if    (konzatsu_flag     == null)   konzatsu_flag = "0";
    String reserve_flag       = request.getParameter("reserve_flag");      //1:���[���\�񃊃��N����
    if    (reserve_flag      == null)   reserve_flag = "0";
    String crosslink_flag     = request.getParameter("crosslink_flag");    //1:�n��X�󎺏󋵃����N����G2�F�n��X�󎺏󋵃����N�s���{����
    if    (crosslink_flag    == null)   crosslink_flag = "0";
    String hotel_id_sub1      = request.getParameter("hotel_id_sub1");      //�󖞗p�z�e��ID(1)�i�z�e��ID�ƈႤ�Ƃ��̂ݓ��́j
    if    (hotel_id_sub1     == null)   hotel_id_sub1 = "";
    if    (hotel_id_sub1.compareTo(hotelid) == 0) hotel_id_sub1 = "";
    String hotel_id_sub2      = request.getParameter("hotel_id_sub2");      //�󖞗p�z�e��ID(2)�i�z�e��ID�ƈႤ�Ƃ��̂ݓ��́B2�z�e���ڂ�\������ۂɎg�p�j
    if    (hotel_id_sub2     == null)   hotel_id_sub2 = "";
    if    (hotel_id_sub2.compareTo(hotelid) == 0) hotel_id_sub2 = "";
    String empty_message_pc   = request.getParameter("empty_message_pc");   //PC�󎺏�񉺕��\�����b�Z�[�W
    if    (empty_message_pc  == null)   empty_message_pc = "";

//�\��p
    String reserve_title          = request.getParameter("reserve_title");          //���[���\��^�C�g��
    if    (reserve_title         == null)   reserve_title = "���[���\��";
    String reserve_message        = request.getParameter("reserve_message");        //�\�񃁃b�Z�[�W
    if    (reserve_message       == null)   reserve_message = "";
    String reserve_conductor      = request.getParameter("reserve_conductor");      //�\�񓱐����b�Z�[�W
    if    (reserve_conductor     == null)   reserve_conductor = "";
    String reserve_conductor_mail = request.getParameter("reserve_conductor_mail"); //�\�񓱐����[�������b�Z�[�W
    if    (reserve_conductor_mail == null)  reserve_conductor_mail = "";
    String mail                   = request.getParameter("mail");                   //���񑗐M�����[���A�h���X
    if    (mail == null)                    mail = "";
    String mail_bbs               = request.getParameter("mail_bbs");                //�f�����e�����[���A�h���X
    if    (mail_bbs == null)                mail_bbs = "";
    String mail_reserve           = request.getParameter("mail_reserve");           //�\���p���[���A�h���X
    if    (mail_reserve == null)            mail_reserve = "";
    if    (mail_reserve.equals(mail))       mail_reserve = "";
    String mail_reserve_info      = request.getParameter("mail_reserve_info");       //�\�񂨒m�点���[���A�h���X
    String reserve_mail_flag      =  request.getParameter("reserve_mail_flag");      //1:���[���A�h���X���͕K�{
    if    (reserve_mail_flag      == null)  reserve_mail_flag = "0";
    String reserve_update_flag    =  request.getParameter("reserve_update_flag");    //1:�\���̕����ύX��
    if    (reserve_update_flag    == null)  reserve_update_flag = "0";


//�����V�~�����[�V�����p
    String max_budget            = request.getParameter("max_budget");           //�\�Z�Œ��ׂ�Ƃ��̍ő���z�w��
    if    (max_budget           == null)   max_budget = "50000";
    String simulate24_flag       = request.getParameter("simulate24_flag");      //1:24���Ԃ𒴂��Ă̌v�Z�����Ȃ��B2:���ۂɒ����Ȃ�
    if    (simulate24_flag      == null)   simulate24_flag = "1";

    String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    connection      = DBConnection.getConnection();
    if( mode.compareTo("NEW") == 0 )
    {
        query = "INSERT INTO hotel_config SET ";
        query = query + "hotel_id='"    + hotelid  + "', ";
        query = query + "rec_flag='"    + rec_flag + "', ";
        query = query + "add_hotelid='" + loginhotel + "', ";
        query = query + "add_userid='"  + ownerinfo.DbUserId + "', ";
        query = query + "add_date='"    + nowdate + "', ";
        query = query + "add_time='"    + nowtime + "', ";
    }
    else
    {
        query = "UPDATE hotel_config SET ";
    }
    query = query + "empty_title='"            + empty_title + "', ";
    query = query + "empty_flag='"             + empty_flag  + "', ";
    query = query + "empty_method='"           + empty_method + "', ";
    query = query + "no_vacancies='"           + ReplaceString.SQLEscape(no_vacancies) + "', ";
    query = query + "vacancies_message='"      + ReplaceString.SQLEscape(vacancies_message) + "', ";
    query = query + "clean_flag='"             + clean_flag + "', ";
    query = query + "clean_method='"           + clean_method + "', ";
    query = query + "empty_list_method='"      + empty_list_method + "', ";
    query = query + "clean_list_method='"      + clean_list_method + "', ";
    query = query + "room_link='"              + ReplaceString.SQLEscape(room_link) + "', ";
    query = query + "line_count='"             + line_count + "', ";
    query = query + "allroom_flag='"           + allroom_flag + "', ";
    query = query + "allroom_title='"          + ReplaceString.SQLEscape(allroom_title) + "', ";
    query = query + "room_exclude='"           + ReplaceString.SQLEscape(room_exclude) + "', ";
    query = query + "allroom_flag2='"           + allroom_flag2 + "', ";
    query = query + "allroom_title2='"          + ReplaceString.SQLEscape(allroom_title2) + "', ";
    query = query + "room_exclude2='"           + ReplaceString.SQLEscape(room_exclude2) + "', ";
    query = query + "allroom_flag3='"           + allroom_flag3 + "', ";
    query = query + "allroom_title3='"          + ReplaceString.SQLEscape(allroom_title3) + "', ";
    query = query + "room_exclude3='"           + ReplaceString.SQLEscape(room_exclude3) + "', ";
    query = query + "allroom_empty_link='"      + ReplaceString.SQLEscape(allroom_empty_link) + "', ";
    query = query + "allroom_novacancies_link='"+ ReplaceString.SQLEscape(allroom_novacancies_link) + "', ";
    query = query + "allroom_clean_link='"      + ReplaceString.SQLEscape(allroom_clean_link) + "', ";
    query = query + "konzatsu_flag='"          + konzatsu_flag + "', ";
    query = query + "reserve_flag='"           + reserve_flag + "', ";
    query = query + "crosslink_flag='"         + crosslink_flag + "', ";
    query = query + "reserve_title='"          + ReplaceString.SQLEscape(reserve_title) + "', ";
    query = query + "reserve_message='"        + ReplaceString.SQLEscape(reserve_message) + "', ";
    query = query + "reserve_conductor='"      + ReplaceString.SQLEscape(reserve_conductor) + "', ";
    query = query + "reserve_conductor_mail='" + ReplaceString.SQLEscape(reserve_conductor_mail) + "', ";
    query = query + "reserve_mail_flag='"      + reserve_mail_flag + "', ";
    query = query + "reserve_update_flag='"    + reserve_update_flag + "', ";
    query = query + "hotel_id_sub1='"          + ReplaceString.SQLEscape(hotel_id_sub1) + "', ";
    query = query + "hotel_id_sub2='"          + ReplaceString.SQLEscape(hotel_id_sub2) + "', ";
    query = query + "empty_message_pc='"       + ReplaceString.SQLEscape(empty_message_pc) + "', ";
    query = query + "max_budget='"             + max_budget + "', ";
    query = query + "simulate24_flag='"        + simulate24_flag + "', ";
    if (rec_flag.equals("1"))
    {
    query = query + "order_crosslink='999',order_bbs='999', ";
    }
    query = query + "upd_hotelid='"     + loginhotel + "', ";
    query = query + "upd_userid='"      + ownerinfo.DbUserId + "', ";
    query = query + "last_update='"     + nowdate + "', ";
    query = query + "last_uptime='"     + nowtime + "' ";

    if( mode.compareTo("UPD") == 0 )
    {
        query = query + " WHERE hotel_id='" + hotelid + "' AND rec_flag='" + request.getParameter("rec_flag") + "'";
    }

    // SQL�N�G���[�̎��s
    prestate = connection.prepareStatement(query);
    int ret  = prestate.executeUpdate();
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);


    query = "UPDATE hotel SET ";
    query = query + "mail='"               + mail              + "', ";
    query = query + "mail_bbs='"           + mail_bbs          + "', ";
    query = query + "mail_reserve='"       + mail_reserve      + "', ";
    query = query + "mail_reserve_info='"  + mail_reserve_info + "' ";
    query = query + " WHERE hotel_id='" + hotelid + "'";

    // SQL�N�G���[�̎��s
    prestate = connection.prepareStatement(query);
    int rethotel  = prestate.executeUpdate();
    DBConnection.releaseResources(result,prestate,connection);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>�󖞁E�\��ݒ�</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="/common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="100" height="20" nowrap bgcolor="#22333F" class="tab">
                <font color="#FFFFFF">�o�^�m�F</font></td>
              <td width="15" height="20" valign="bottom"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="/common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ��������\ -->
      <tr>
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><img src="/common/pc/image/spacer.gif" width="400" height="12"></td>
          </tr>
          <tr>
            <td width="8"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="size12">
<%
    if( ret != 0 )
    {
%>
�o�^���܂����B<br>
<%
    }
    else
    {
%>
�o�^�Ɏ��s���܂����B
<%
    }
%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12"><form action="vacant_edit.jsp?HotelId=<%= hotelid %>&Recflag=<%= rec_flag %>" method="POST">
                      <INPUT name="submit_ret" type=submit value=�߂� >
                    </form></td>
                  </tr>
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
      <!-- �����܂� -->
    </table></td>
  </tr>
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="/common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="/common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyrigtht&copy; imedia
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="/common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
