<%@ page contentType="text/html;charset=Windows-31J"%><%@ page errorPage="error.jsp" %><%@ page import="java.sql.*" %><%@ page import="java.net.*" %><%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %><%@ page import="jp.happyhotel.common.HotelElement" %><jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%><%
    String header_msg = "";
    String query;

//�󎺏��p
    int    rec_flag           = 0;               //0:�ʏ�,1:�����o�[��p
    String empty_title        = "�󎺏��";
    int    empty_flag         = 0;               //0:�\���Ȃ�,1:�\������,2:���������N��,3:�\������i"�ȏ�"�Ȃ��j,4:�\�����蕔�������N�ʁi"�ȏ�"�Ȃ��j
    int    empty_method       = 0;               //0:�L���\��,999:�����\��,1�`998:�w�萔���������\��
    String no_vacancies       = "";              //���������b�Z�[�W
    String vacancies_message  = "";              //�󎺎����b�Z�[�W
    int    clean_flag         = 0;               //0:�\���Ȃ�,1:�\������,2:���������N��,3:�\������i"�ȏ�"�Ȃ��j,4:�\�����蕔�������N�ʁi"�ȏ�"�Ȃ��j,5:�\������iempty_method�ȏ�͕\���Ȃ��j,6:�\�����蕔�������N�ʁiempty_method�ȏ�͕\���Ȃ��j
    int    clean_method       = 0;               //0:�L���\��,999:�����\��,1�`998:�w�萔���������\��
    int    empty_list_method  = 0;               //0:�\���Ȃ�,999:���ׂĕ\��,1�`998:�����\��
    int    clean_list_method  = 0;               //0:�\���Ȃ�,999:���ׂĕ\��,1�`998:�����\��
    int    allroom_flag       = 0;               //1:�S�����\��
    int    allroom_flag2      = 0;               //1:�S�����\���i2�߁j
    int    allroom_flag3      = 0;               //1:�S�����\���i3�߁j
    String room_link          = "<td width=\"150\" height=\"30\" align=\"center\" valign=\"middle\" nowrap class=\"roomlist\"><a href=\"roomdetail.jsp?RoomDetail=%ROOMNO%\" class=\"link1\">%ROOMNO%</a></td>";         //���������N�ӏ�
    int    line_count         = 4;               //��
    String room_exclude       = "";              //�S���\�������O����
    String allroom_title      = "�����ꗗ";      //�S���\�����^�C�g��
    String room_exclude2      = "";              //�S���\�������O����2
    String allroom_title2     = "�����ꗗ";      //�S���\�����^�C�g��2
    String room_exclude3      = "";              //�S���\�������O����3
    String allroom_title3     = "�����ꗗ";      //�S���\�����^�C�g��4
    String allroom_empty_link       = "<td width=\"150\" height=\"40\" align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\"><a href=\"room%ROOMNO%.jsp\" class=\"link1\">%ROOMNO%</a><br>��  ��</td>"; //�S���\���󎺎������N�ӏ�
    String allroom_novacancies_link = "<td width=\"150\" height=\"40\" align=\"center\" valign=\"middle\" class=\"roomlist\"><a href=\"room%ROOMNO%.jsp\" class=\"link1\">%ROOMNO%</a><br>��  ��</td>";  //�S���\���ݎ��������N�ӏ�
    String allroom_clean_link       = "<td width=\"150\" height=\"40\" align=\"center\" valign=\"middle\" class=\"cleanlist\"><a href=\"room%ROOMNO%.jsp\" class=\"link1\">%ROOMNO%</a><br>������</td>"; //�S���\����ƒ��������N�ӏ�
    int    konzatsu_flag      = 0;               //1:���G�󋵂���
    int    reserve_flag       = 0;               //1:���[���\�񃊃��N����
    int    crosslink_flag     = 0;               //1:�n��X�󎺏󋵃����N����G2�F�n��X�󎺏󋵃����N�s���{����
    String hotel_id_sub1      = "";              //�󖞗p�z�e��ID(1)�i�z�e��ID�ƈႤ�Ƃ��̂ݓ��́j
    String hotel_id_sub2      = "";              //�󖞗p�z�e��ID(2)�i�z�e��ID�ƈႤ�Ƃ��̂ݓ��́B2�z�e���ڂ�\������ۂɎg�p�j
    String empty_message_pc   = "";              //PC���b�Z�[�W

//�\��p
    String reserve_title      = "���[���\��";
    String reserve_message    = "";              //�\�񃁃b�Z�[�W
    String reserve_conductor  = "�z�e���ɓ�������܂����礃t�����g�܂ŗ\��ԍ������`�����������<br>";
           reserve_conductor  = reserve_conductor + "[����]�\�񎞍����߂��Ă������X�̖����ꍇ��\��̓L�����Z������܂��<br>";
           reserve_conductor  = reserve_conductor + "3��L�����Z������܂��Ƥ�ȍ~�̂����p�͏o���܂���B<br>";
           reserve_conductor  = reserve_conductor + "�����o�[��1�l�l�ɂ�1���܂ŗ\��\�ł��";
    String reserve_conductor_mail = "�z�e���ɓ�������܂�����A�t�����g�܂ŗ\��ԍ������`�����������";          //�\�񓱐����[�������b�Z�[�W
    String mail               = "";              //�z�e�����[���A�h���X
    String mail_report        = "";              //���񃁁[�����M���A�h���X
    String mail_bbs           = "";              //�f�����e���[���p�A�h���X
    String mail_reserve       = "";              //�\���p���[���A�h���X
    String mail_reserve_info  = "";              //�\�񂨒m�点���[���A�h���X
    String object_no          = "";              //�����ԍ�
    int    reserve_mail_flag  = 0;               //1:���[���A�h���X���͕K�{
    int    reserve_update_flag= 1;               //1:�\��ύX��


//�����V�~�����[�V����
    int    max_budget         = 50000;           //�\�Z�Œ��ׂ�Ƃ��̍ő���z�w��
    int    simulate24_flag    = 1;               //1:24���Ԃ𒴂��Ă̌v�Z�����Ȃ��B2:���ۂ�24���Ԃ𒴂��Ȃ�

    String mode               = "NEW";           //NEW:�V�K UPD:�X�V

    // �z�e��ID�擾
    String hotelid = (String)session.getAttribute("SelectHotel");
    if  (hotelid.compareTo("all") == 0)
    {
        hotelid    = request.getParameter("HotelId");
    }
    String recflag = request.getParameter("Recflag");
    if  (recflag == null)
    {
        recflag  = "0";
    }

    String recflag_name       = "";
    if  (recflag.compareTo("0") == 0)
    {
        recflag_name = "�ʏ�";
    }
    else
    {
        recflag_name = "����p";
    }

    DateEdit  de = new DateEdit();
    int       nowdate   = Integer.parseInt(de.getDate(2));
    int       trialDate = HotelElement.getTrialDate(hotelid);
    int       lastUpdate = 0;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    query = "SELECT * FROM hotel WHERE hotel_id='" + hotelid + "'";
    prestate    = connection.prepareStatement(query);
    result      = prestate.executeQuery();

    if (result != null)
    {
        if( result.next() != false )
        {
            mail              = result.getString("mail");
            mail_report       = result.getString("mail");
            if(mail_report.equals("")) mail_report = "imedia-info@hotenavi.com";
            mail_bbs          = result.getString("mail_bbs");
            mail_reserve      = result.getString("mail_reserve");
            mail_reserve_info = result.getString("mail_reserve_info");
            object_no         = result.getString("object_no");
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    query = "SELECT * FROM hotel_config WHERE hotel_id='" + hotelid + "'";
    query = query + " AND rec_flag='" + recflag +"'";
    prestate    = connection.prepareStatement(query);
    result      = prestate.executeQuery();

    if (result != null)
    {
        if( result.next() != false )
        {
            header_msg = "�X�V";
            mode = "UPD";
            rec_flag               = result.getInt("rec_flag");
            if (!result.getString("empty_title").equals(""))
                empty_title        = result.getString("empty_title");
            empty_flag             = result.getInt("empty_flag");
            empty_method           = result.getInt("empty_method");
            no_vacancies           = result.getString("no_vacancies");
            vacancies_message      = result.getString("vacancies_message");
            clean_flag             = result.getInt("clean_flag");
            clean_method           = result.getInt("clean_method");
            empty_list_method      = result.getInt("empty_list_method");
            clean_list_method      = result.getInt("clean_list_method");
            if (!result.getString("room_link").equals(""))
                room_link          = result.getString("room_link");
            if (result.getInt("line_count")!=0) 
                line_count         = result.getInt("line_count");
            allroom_flag             = result.getInt("allroom_flag");
            if (!result.getString("allroom_title").equals(""))
                allroom_title        = result.getString("allroom_title");
            room_exclude             = result.getString("room_exclude");
            allroom_flag2             = result.getInt("allroom_flag2");
            if (!result.getString("allroom_title2").equals(""))
                allroom_title2        = result.getString("allroom_title2");
            room_exclude2             = result.getString("room_exclude2");
            allroom_flag3             = result.getInt("allroom_flag3");
            if (!result.getString("allroom_title3").equals(""))
                allroom_title3        = result.getString("allroom_title3");
            room_exclude3             = result.getString("room_exclude3");
            if (!result.getString("allroom_empty_link").equals(""))
                allroom_empty_link   = result.getString("allroom_empty_link");
            if (!result.getString("allroom_novacancies_link").equals(""))
                allroom_novacancies_link = result.getString("allroom_novacancies_link");
            if (!result.getString("allroom_clean_link").equals(""))
                allroom_clean_link   = result.getString("allroom_clean_link");
            konzatsu_flag          = result.getInt("konzatsu_flag");
            reserve_flag           = result.getInt("reserve_flag");
            crosslink_flag         = result.getInt("crosslink_flag");
            reserve_message        = result.getString("reserve_message");
            if (!result.getString("reserve_title").equals(""))
                reserve_title          = result.getString("reserve_title");
            if (!result.getString("reserve_conductor").equals(""))
                reserve_conductor      = result.getString("reserve_conductor");
            if (!result.getString("reserve_conductor_mail").equals(""))
                reserve_conductor_mail = result.getString("reserve_conductor_mail");
            reserve_mail_flag      = result.getInt("reserve_mail_flag");
            hotel_id_sub1          = result.getString("hotel_id_sub1");
            hotel_id_sub2          = result.getString("hotel_id_sub2");
            if (object_no.indexOf("n") == 0 || result.getInt("max_budget") != 0)
                max_budget         = result.getInt("max_budget");
            simulate24_flag        = result.getInt("simulate24_flag");
            empty_message_pc       = result.getString("empty_message_pc");
            reserve_update_flag    = result.getInt("reserve_update_flag");
            lastUpdate             = result.getInt("last_update");
        }
        else
        {
            header_msg = "�V�K�쐬";
            mode = "NEW";
        }
    }

    DBConnection.releaseResources(result,prestate,connection);

    //�V�z�e�i�r�Ή�
    if (nowdate >= trialDate)
    {
        if (room_link.indexOf("roomitem") == -1)
        {
            room_link = "<div class=\"oldSite\">" + room_link;
            room_link +="</div>\r\n<div class=\"roomitem\"><span class=\"roomlist\"><a href=\"room/detail/%ROOMNO%\">%ROOMNO%</a></span></div>";
        }
        if (allroom_empty_link.indexOf("roomitem") == -1)
        {
            allroom_empty_link = "<div class=\"oldSite\">" + allroom_empty_link;
            allroom_empty_link +="</div>\r\n<div class=\"roomitem\"><span class=\"roomempty\" id=\"room%ROOMNO%\"><a href=\"room/detail/%ROOMNO%\">%ROOMNO%</a><br>��  ��</span></div>";
        }
        if (allroom_novacancies_link.indexOf("roomitem") == -1)
        {
            allroom_novacancies_link = "<div class=\"oldSite\">" + allroom_novacancies_link;
            allroom_novacancies_link +="</div>\r\n<div class=\"roomitem\"><span class=\"roomlist\" id=\"room%ROOMNO%\"><a href=\"room/detail/%ROOMNO%\">%ROOMNO%</a><br>��  ��</span></div>";
        }
        if (allroom_clean_link.indexOf("roomitem") == -1)
        {
            allroom_clean_link = "<div class=\"oldSite\">" + allroom_clean_link;
            allroom_clean_link +="</div>\r\n<div class=\"roomitem\"><span class=\"roomclean\" id=\"room%ROOMNO%\"><a href=\"room/detail/%ROOMNO%\">%ROOMNO%</a><br>������</span></div>";
        }
    }

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>�󖞁E�\��ݒ�</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
function MM_openPreview(input,hotelid,rec_flag){
  if( input == 'preview' )
  {
    document.form1.target = '_blank';
    document.form1.action = 'vacant_edit_preview.jsp?HotelId='+hotelid+'&Recflag='+rec_flag;
  }
  document.form1.submit();
}

function MM_openInput(input,hotelid,mode){
  if( input == 'input' )
  {
    document.form1.target = '_self';
    document.form1.action = 'vacant_edit_input.jsp?HotelId='+hotelid+'&Mode='+mode;
  }
  else if( input == 'edit' )
  {
    document.form1.target = '_self';
    document.form1.action = 'vacant_edit.jsp?HotelId='+hotelid+'&Recflag='+mode;
  }
  document.form1.submit();
}
</script>

</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF"><%= header_msg %></font></td>
              <td width="15" height="20"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
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
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="12">
              </td>
            </tr>
            <tr>
              <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td colspan="2" class="size12"><font color="#CC0000"><strong>�����̃y�[�W��ҏW���I������A�u<%= header_msg %>�v�{�^����K�������Ă�������</strong></font>
					<% if (lastUpdate<trialDate && trialDate!=99999999){%><br><font size="4" color="#FF0000"><strong>���V�z�e�i�r���j���[�A���J�n��́A�K���X�V���Ă�������</strong></font><%}%>
					</td>
                  </tr>
                  <form name=form1 method=post>
                  <input name="rec_flag" type="hidden"  value="<%= recflag %>"> 
				  <tr align="left">
<% 
if  (mode.compareTo("NEW") == 0)
{
%>
                      <td align="left" colspan="2" valign="middle" bgcolor="#969EAD">&nbsp;<strong><%= recflag_name %></strong>&nbsp;<input name="regsubmit" type=button value="<%= header_msg %>" onClick="MM_openInput('input', '<%= hotelid %>', '<%= mode %>')"></td>
<%
}
else
   if (recflag.compareTo("0") == 0)  //�ʏ�p
	{
%>
                      <td align="left" valign="middle" bgcolor="#969EAD" class="size18">&nbsp;<strong><%= recflag_name %></strong>&nbsp;<input name="regsubmit" type=button value="<%= header_msg %>" onClick="MM_openInput('input', '<%= hotelid %>', '<%= mode %>')"></td>
                      <td align="left" valign="middle" bgcolor="#969EAD" class="size18"><input name="regsubmit" type=button value="�����p�͂�����" onClick="MM_openInput('edit', '<%= hotelid %>', '1')"></td>
<%
	}
	else  //�����p
	{
%>
                      <td align="left" valign="middle" bgcolor="#969EAD" class="size18">&nbsp;<strong><%= recflag_name %></strong>&nbsp;<input name="regsubmit" type=button value="<%= header_msg %>" onClick="MM_openInput('input', '<%= hotelid %>', '<%= mode %>')"></td>
                      <td align="left" valign="middle" bgcolor="#969EAD" class="size18"><input name="regsubmit" type=button value="�ʏ�p�͂�����" onClick="MM_openInput('edit', '<%= hotelid %>', '0')"></td>
<%
	}
%>
                   </tr>
                  <tr align="left">
                    <td width="150"><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�󎺏��^�C�g����</td>
					<td nowrap class="size12">
						<input name="empty_title" type="text" size="30" value="<%= empty_title %>" style="ime-mode:active">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�󎺏��\��</td>
					<td nowrap class="size12">
						<select name="empty_flag"> 
							<option value=0 <%if (empty_flag == 0){%>selected<%}%>>0.�\������</option>
							<option value=1 <%if (empty_flag == 1){%>selected<%}%>>1.�\���L�i�����ꗗ�F�W���j</option>
							<option value=2 <%if (empty_flag == 2){%>selected<%}%>>2.�\���L�i�����ꗗ�F�����N�ʁj</option>
							<option value=3 <%if (empty_flag == 3){%>selected<%}%>>3.�\���L�i�����ꗗ�F�W��,�u�ȏ�v�����Ȃ��j</option>
							<option value=4 <%if (empty_flag == 4){%>selected<%}%>>4.�\���L�i�����ꗗ�F�����N��,�u�ȏ�v�����Ȃ��j</option>
						</select>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�󎺏��\�����@</td>
					<td nowrap class="size12">
						<input type="radio" name="empty_method" id="empty_method_radio1" value="0"    <%if (empty_method == 0)  {%>checked<%}%> onclick="document.getElementById('empty_method_text').value=this.value;">�L���\��
						<input type="radio" name="empty_method" id="empty_method_radio2" value="999"  <%if (empty_method == 999){%>checked<%}%> onclick="document.getElementById('empty_method_text').value=this.value;">�����\��
						�E�w�萔���������\��<input name="empty_method" id="empty_method_text" type="text" size="3" value="<%= empty_method %>" style="text-align:right;ime-mode:disabled" onchange="if(this.value==0){document.getElementById('empty_method_radio1').checked=true;}else{document.getElementById('empty_method_radio1').checked=false;};if(this.value==999){document.getElementById('empty_method_radio2').checked=true;}else{document.getElementById('empty_method_radio2').checked=false;}">���w�萔�����
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">&nbsp;</td>
                    <td nowrap class="size12">&nbsp;��j5�����������\���̂Ƃ��́u5�v�ɐݒ�</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�����̏ꍇ�̕\����</td>
					<td nowrap class="size12"><input name="no_vacancies" type="text" size="20" value="<%= no_vacancies %>"> ��j�������</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�󎺂̏ꍇ�̕\����</td>
					<td nowrap class="size12"><input name="vacancies_message" type="text" size="20" value="<%= vacancies_message %>"> ��j���������ł�`</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">���������\��</td>
					<td nowrap class="size12">
						<select name="clean_flag"> 
							<option value=0 <%if (clean_flag == 0){%>selected<%}%>>0.�\������</option>
							<option value=1 <%if (clean_flag == 1){%>selected<%}%>>1.�\���L�i�����ꗗ�F�W���j</option>
							<option value=2 <%if (clean_flag == 2){%>selected<%}%>>2.�\���L�i�����ꗗ�F���������N�ʁj</option>
							<option value=3 <%if (clean_flag == 3){%>selected<%}%>>3.�\���L�i�����ꗗ�F�W��,�u�ȏ�v�����Ȃ��j</option>
							<option value=4 <%if (clean_flag == 4){%>selected<%}%>>4.�\���L�i�����ꗗ�F�����N��,�u�ȏ�v�����Ȃ��j</option>
							<option value=5 <%if (clean_flag == 5){%>selected<%}%>>5.�\���L�i�����ꗗ�F�W���C�󎺎w�萔�ȏ�͕\�����Ȃ��j</option>
							<option value=6 <%if (clean_flag == 6){%>selected<%}%>>6.�\���L�i�����ꗗ�F���������N�ʁC�󎺎w�萔�ȏ�͕\�����Ȃ��j</option>
						</select>
					</td>
                  </tr>
                 <tr align="left">
                    <td nowrap class="size12">�������\�����@</td>
					<td nowrap class="size12">
						<input type="radio" name="clean_method" id="clean_method_radio1" value="0"   <%if (clean_method == 0)  {%>checked<%}%> onclick="document.getElementById('clean_method_text').value=this.value;">�L���\��
						<input type="radio" name="clean_method" id="clean_method_radio2" value="999" <%if (clean_method == 999)  {%>checked<%}%>  onclick="document.getElementById('clean_method_text').value=this.value;">�����\��
						�E�w�萔���������\��<input name="clean_method" id="clean_method_text" type="text" size="3" value="<%= clean_method %>" style="text-align:right;ime-mode:disabled" onchange="if(this.value==0){document.getElementById('clean_method_radio1').checked=true;}else{document.getElementById('clean_method_radio1').checked=false;};if(this.value==999){document.getElementById('clean_method_radio2').checked=true;}else{document.getElementById('clean_method_radio2').checked=false;}">���w�萔�����
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�󎺈ꗗ�\������</td>
					<td nowrap class="size12">
						<input type="radio" name="empty_list_method" id="empty_list_method_radio1" value="0"   <%if (empty_list_method == 0)  {%>checked<%}%> onclick="document.getElementById('empty_list_method_text').value=this.value;">�\������
						<input type="radio" name="empty_list_method" id="empty_list_method_radio2" value="999" <%if (empty_list_method == 999)  {%>checked<%}%> onclick="document.getElementById('empty_list_method_text').value=this.value;">�S�ĕ\��
						�E�w�萔�����̏ꍇ�\��<input name="empty_list_method" id="empty_list_method_text" type="text" size="3" value="<%= empty_list_method %>" style="text-align:right;ime-mode:disabled"  onchange="if(this.value==0){document.getElementById('empty_list_method_radio1').checked=true;}else{document.getElementById('empty_list_method_radio1').checked=false;};if(this.value==999){document.getElementById('empty_list_method_radio2').checked=true;}else{document.getElementById('empty_list_method_radio2').checked=false;}">���w�萔�����
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�������ꗗ�\������</td>
					<td nowrap class="size12">
						<input type="radio" name="clean_list_method" id="clean_list_method_radio1" value="0"   <%if (clean_list_method == 0)  {%>checked<%}%> onclick="document.getElementById('clean_list_method_text').value=this.value;">�\������
						<input type="radio" name="clean_list_method" id="clean_list_method_radio2" value="999" <%if (clean_list_method == 999)  {%>checked<%}%> onclick="document.getElementById('clean_list_method_text').value=this.value;">�S�ĕ\��
						�E�w�萔�����̏ꍇ�\��<input name="clean_list_method" id="clean_list_method_text" type="text" size="3" value="<%= clean_list_method %>" style="text-align:right;ime-mode:disabled"  onchange="if(this.value==0){document.getElementById('clean_list_method_radio1').checked=true;}else{document.getElementById('clean_list_method_radio1').checked=false;};if(this.value==999){document.getElementById('clean_list_method_radio2').checked=true;}else{document.getElementById('clean_list_method_radio2').checked=false;}">���w�萔�����
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">���������N�ӏ�</td>
					<td nowrap class="size12">
						<textarea name="room_link" rows="3" cols="80" style="ime-mode:active"><%=room_link%></textarea>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�����ꗗ��</td>
					<td nowrap class="size12">
						<input name="line_count" id="line_count" type="text" size="2" value="<%= line_count %>" style="text-align:right;ime-mode:disabled">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�S�����\������</td>
					<td nowrap class="size12">
						<input type="checkbox" name="allroom_flag" value="1"   <%if (allroom_flag == 1) {%>checked<%}%>>
						�S�����ꗗ�^�C�g��<input name="allroom_title" type="text" size="30" value="<%= allroom_title %>" style="ime-mode:active">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�S�����\���̏ꍇ�A���O����</td>
					<td nowrap class="size12">
						<input name="room_exclude" type="text" size="80" value="<%= room_exclude %>" style="ime-mode:disable"><font size=1>(�R���}��؂�)</font>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�S�����\������(2)</td>
					<td nowrap class="size12">
						<input type="checkbox" name="allroom_flag2" value="1"   <%if (allroom_flag2 == 1) {%>checked<%}%>>
						�S�����ꗗ�^�C�g��(2)<input name="allroom_title2" type="text" size="30" value="<%= allroom_title2 %>" style="ime-mode:active">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�S�����\���̏ꍇ�A���O����(2)</td>
					<td nowrap class="size12">
						<input name="room_exclude2" type="text" size="80" value="<%= room_exclude2 %>" style="ime-mode:disable"><font size=1>(�R���}��؂�)</font>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�S�����\������(3)</td>
					<td nowrap class="size12">
						<input type="checkbox" name="allroom_flag3" value="1"   <%if (allroom_flag3 == 1) {%>checked<%}%>>
						�S�����ꗗ�^�C�g��(3)<input name="allroom_title3" type="text" size="30" value="<%= allroom_title3 %>" style="ime-mode:active">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�S�����\���̏ꍇ�A���O����(3)</td>
					<td nowrap class="size12">
						<input name="room_exclude3" type="text" size="80" value="<%= room_exclude3 %>" style="ime-mode:disable"><font size=1>(�R���}��؂�)</font>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�S�����ꗗ�����N�ӏ�</td>
					<td nowrap class="size12">
						<textarea name="allroom_empty_link" rows="3" cols="80" style="ime-mode:active"><%=allroom_empty_link%></textarea>�i��@���j<br>
						<textarea name="allroom_novacancies_link" rows="3" cols="80" style="ime-mode:active"><%=allroom_novacancies_link%></textarea>�i�݁@���j<br>
						<textarea name="allroom_clean_link" rows="3" cols="80" style="ime-mode:active"><%=allroom_clean_link%></textarea>�i�������j<br>
					</td>
                  </tr>
                  <tr align="left">
                    <td><img src="../../common/pc/image/spacer.gif" width="120" height="5"></td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">���G�󋵃����N����</td>
					<td nowrap class="size12">
						<input type="checkbox" name="konzatsu_flag" value="1"   <%if (konzatsu_flag == 1) {%>checked<%}%>>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">���[���\�񃊃��N����</td>
					<td nowrap class="size12">
						<input type="checkbox" name="reserve_flag" value="1"   <%if (reserve_flag == 1) {%>checked<%}%>>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�n��X�󎺏󋵃����N</td>
					<td nowrap class="size12">
						<input type="radio" name="crosslink_flag" value="0"   <%if (crosslink_flag == 0) {%>checked<%}%>>�Ȃ�
						<input type="radio" name="crosslink_flag" value="1"   <%if (crosslink_flag == 1) {%>checked<%}%>>����
						<input type="radio" name="crosslink_flag" value="2"   <%if (crosslink_flag == 2) {%>checked<%}%>>����i�s���{���ʁj
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�󖞗p�z�e��ID</td>
					<td nowrap class="size12">
						(1)<input name="hotel_id_sub1" type="text" size="10" value="<%= hotel_id_sub1 %>" style="ime-mode:inactive">
						(2)<input name="hotel_id_sub2" type="text" size="10" value="<%= hotel_id_sub2 %>" style="ime-mode:inactive">
						�i�Ⴄ�z�e��ID���e��\������ۂ̂ݓ��́j
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�󎺏�񉺕����b�Z�[�W</td>
					<td nowrap class="size12">
						<textarea name="empty_message_pc" rows="3" cols="80" style="ime-mode:active"><%= empty_message_pc.replace("\"","&quot;") %></textarea>
					</td>
                  </tr>
                  <tr align="left">
                    <td width="150"><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�y���[���̐ݒ�z</td>
					<td nowrap class="size12">
					</td>
				  </tr>
                  <tr align="left">
                    <td nowrap class="size12">���񃁁[�����M����</td>
					<td nowrap class="size12">
						<input name="mail" id="mail" type="text" size="64" maxlength="64" value="<%= mail %>" style="ime-mode:disabled">
					</td>
				  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�f�����e��</td>
					<td nowrap class="size12">
						<textarea name="mail_bbs" id="mail_bbs" type="text" rows=3 cols=80 style="ime-mode:disabled"><%= mail_bbs %></textarea>
					</td>
				  </tr>
                  <tr align="left">
                    <td width="150"><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>

                  <tr align="left">
                    <td nowrap class="size12">�y�\��z</td>
					<td nowrap class="size12">
					</td>
				  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�\��^�C�g����</td>
					<td nowrap class="size12">
						<input name="reserve_title" type="text" size="30" value="<%= reserve_title %>" style="ime-mode:active">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�\�񃁃b�Z�[�W</td>
					<td nowrap class="size12">
						<textarea name="reserve_message" rows="4" cols="80" style="ime-mode:active"><%=reserve_message%></textarea>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�\�񓱐����b�Z�[�W</td>
					<td nowrap class="size12">
						<textarea name="reserve_conductor" rows="4" cols="80" style="ime-mode:active"><%=reserve_conductor%></textarea>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�\�񓱐����[��</td>
					<td nowrap class="size12">
						<textarea name="reserve_conductor_mail" rows="4" cols="80" style="ime-mode:active"><%=reserve_conductor_mail%></textarea>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�m�F���[�����M���A�h���X</td>
					<td nowrap class="size12">
						<input type=checkbox name="mail_check" <%if(mail_reserve.equals("")){%>checked<%}%> onclick="if(this.checked){document.form1.mail_reserve.value='';}else{document.form1.mail_reserve.value=document.form1.mail_report.value;}">���񃁁[�����M�����[���i<%=mail_report%>�j�Ɠ���<br>
						<input type=hidden name="mail_report" value="<%=mail_report%>"> 
						�ʓr�ݒ�F<input type=text   name="mail_reserve" size="64" value="<%=mail_reserve%>" style="ime-mode:inactive" onchange="if(this.value==document.form1.mail.value||this.value==''){document.form1.mail_check.checked=true;}else{document.form1.mail_check.checked=false;}">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�I�[�i�[�����m�F���[��</td>
					<td nowrap class="size12">
						�ݒ莞���M<input type=text   name="mail_reserve_info" size="64" value="<%=mail_reserve_info%>" style="ime-mode:inactive">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">���[���A�h���X�̓���</td>
					<td nowrap class="size12">
						<input type="checkbox" name="reserve_mail_flag" value="1"   <%if (reserve_mail_flag == 1) {%>checked<%}%>>�K�{
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�\���̕����ύX</td>
					<td nowrap class="size12">
						<input type="checkbox" name="reserve_update_flag" value="1"   <%if (reserve_update_flag == 1) {%>checked<%}%>>��
					</td>
                  </tr>
                  <tr align="left">
                    <td width="150"><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�y�����V�~�����[�V�����z(<%=object_no%>)</td>
					<td nowrap class="size12">
					</td>
				  </tr>
                  <tr align="left">
                    <td nowrap class="size12">�\�Z�Œ��ׂ�ő���z</td>
					<td nowrap class="size12">
						<input name="max_budget" id="max_budget" type="text" size="5" maxlength="6" value="<%= max_budget %>" style="text-align:right;ime-mode:disabled" onchange="if (this.value<=50000 <%if (object_no.indexOf("n") == 0){%> && this.value != 0<%}%>){this.value=50000}">
						<%if (object_no.indexOf("n") == 0){%>�i���z��0�ɂ���ƁA�\�Z�ɂ��V�~�����[�V���������܂���j<%}%>
					</td>
				  </tr>
                  <tr align="left">
                    <td nowrap class="size12">24���Ԃ𒴂��Ă̌v�Z</td>
					<td nowrap class="size12">
						<input type="radio" name="simulate24_flag" value="0" <%if (simulate24_flag == 0) {%>checked<%}%>>����
						<input type="radio" name="simulate24_flag" value="1" <%if (simulate24_flag == 1) {%>checked<%}%>>���Ȃ�
						<input type="radio" name="simulate24_flag" value="2" <%if (simulate24_flag == 2) {%>checked<%}%>>���ۂɒ����Ȃ�
					</td>
				  </tr>
                  <tr align="left">
                    <td width="150"><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>
                </form>  
                </table>
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
              <td><img src="new/image/tab_kado.gif" width="3" height="3"></td>
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
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/owner/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18"><img src="../../common/pc/image/spacer.gif" width="12" height="18" align="absmiddle">Copyrigtht&copy; imedia
    inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
