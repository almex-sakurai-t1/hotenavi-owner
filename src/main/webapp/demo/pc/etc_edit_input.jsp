<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
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
	
	//hotel_element
	String trial_date        = request.getParameter("trial_date");          	//���j���[�A���J�n���t
    if    (trial_date        == null)   trial_date = "99999999";
	String start_date        = request.getParameter("start_date");          	//�V�z�e�i�r�ғ��J�n���t
    if    (start_date        == null)   trial_date = "99999999";
	String chk_age_flg       = request.getParameter("chk_age_flg");          	//�N��m�F�t���O �i1:�N�b�V�����y�[�W����j
    if    (chk_age_flg       == null)   chk_age_flg = "0";
	String html_head         = request.getParameter("html_head");          		//HTML�w�b�_���
    if    (html_head         == null)   html_head = "";
	String html_login_form   = request.getParameter("html_login_form");    		//���O�C���t�H�[��
    if    (html_login_form   == null)   html_login_form = "";
	String offlineflg        = request.getParameter("offlineflg");          	//���O�C���L���t���O�i0:�����o�[�y�[�W�Ȃ� 1:�����o�[�p�[�W����j
    if    (offlineflg        == null)   offlineflg = "";
	String mailmagazineflg   = request.getParameter("mailmagazineflg");         //�����}�K�L���t���O�i0�F���� 1:�L�j
    if    (mailmagazineflg   == null)   mailmagazineflg = "1";
	String mailtoflg         = request.getParameter("mailtoflg");          		//�z�e���ֈꌾ�L���t���O�i0�F���� 1:�L�j
    if    (mailtoflg          == null)   mailtoflg = "1";
	String mailnameflg        = request.getParameter("mailnameflg");         	//�z�e���ֈꌾ�z�e�������L���i0�F�����Ƀz�e��������(default) 1:�����Ƀz�e�����L�j
    if    (mailnameflg        == null)   mailnameflg = "0";
	String mailname        = request.getParameter("mailname");         			//�z�e���ֈꌾ�z�e����
    if    (mailname        == null)   mailname = "";
	String viewflg        = request.getParameter("viewflg");         			//0:�ʏ�@1:�Q�ƃo�[�W���� 2:�Q�ƃo�[�W�����i�����o�[���j 9:�x�~��
    if    (viewflg        == null)   viewflg = "0";
	String bbsgroupflg        = request.getParameter("bbsgroupflg");         	//���X�܌f���̎g�p(0:�ʏ�@1:���X�܃o�[�W����)
    if    (bbsgroupflg        == null)   bbsgroupflg = "0";
	String prize_hotelid        = request.getParameter("prize_hotelid");         //���i�����p�z�e��ID�i�����͂̏ꍇ��hotel_id���g�p�j
    if    (prize_hotelid        == null)   prize_hotelid = hotelid;
	if    (prize_hotelid.equals(""))   prize_hotelid = hotelid;
	String coupon_map_flg        = request.getParameter("coupon_map_flg");       //0:Yahoo!MAP���g�p,1:�摜�t�@�C�����g�p
    if    (coupon_map_flg        == null)   coupon_map_flg = "0";
	String coupon_map_img1        = request.getParameter("coupon_map_img1");     //�N�[�|���摜1
    if    (coupon_map_img1        == null)   coupon_map_img1 = "";
	String coupon_map_img2        = request.getParameter("coupon_map_img2");     //�N�[�|���摜2
    if    (coupon_map_img2        == null)   coupon_map_img2 = "";
	String bbs_temp_flg        = request.getParameter("bbs_temp_flg");           //0:�ʏ�f����,1:�����e�f���i�f���ǉ�����del_flag��1���Z�b�g�j
    if    (bbs_temp_flg        == null)   bbs_temp_flg = "0";
	String ranking_hidden_flg        = request.getParameter("ranking_hidden_flg");     //0:�ʏ�,1:�����L���O�����o�͂��Ȃ�
    if    (ranking_hidden_flg        == null)   ranking_hidden_flg = "0";
	String ownercount        	= request.getParameter("ownercount");     //�I�[�i�[�Y���[���B����
    if    (ownercount        	== null)   ownercount = "0";
    String count_whatsnew_for_hoteltop        	= request.getParameter("count_whatsnew_for_hoteltop");     //TOP�y�[�W��WhatsNew�\������
    if    (count_whatsnew_for_hoteltop        	== null)  count_whatsnew_for_hoteltop = "3";
    if (!CheckString.numCheck(count_whatsnew_for_hoteltop)) count_whatsnew_for_hoteltop = "3";

    String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    connection      = DBConnection.getConnection();
    if( mode.compareTo("NEW") == 0 )
    {
        query = "INSERT INTO hotenavi.hotel_element SET ";
        query = query + "hotel_id='"    + hotelid  + "', ";
    }
    else
    {
        query = "UPDATE hotenavi.hotel_element SET ";
    }

	query = query + "trial_date='"             + trial_date + "', ";
	query = query + "start_date='"             + start_date + "', ";
	query = query + "chk_age_flg='"            + chk_age_flg + "', ";
    query = query + "html_head='"              + ReplaceString.SQLEscape(html_head) + "', ";
    query = query + "html_login_form='"        + ReplaceString.SQLEscape(html_login_form) + "', ";
    query = query + "offlineflg='"             + offlineflg + "', ";
    query = query + "mailmagazineflg='"        + mailmagazineflg + "', ";
    query = query + "mailtoflg='"              + mailtoflg + "', ";
    query = query + "mailnameflg='"            + mailnameflg + "', ";
    query = query + "mailname='"      		   + ReplaceString.SQLEscape(mailname) + "', ";
    query = query + "viewflg='"                + viewflg + "', ";
    query = query + "bbsgroupflg='"            + bbsgroupflg + "', ";
    query = query + "prize_hotelid='"          + prize_hotelid + "', ";
    query = query + "coupon_map_flg='"         + coupon_map_flg + "', ";
    query = query + "coupon_map_img1='"        + coupon_map_img1 + "', ";
    query = query + "coupon_map_img2='"        + coupon_map_img2 + "', ";
    query = query + "bbs_temp_flg='"           + bbs_temp_flg + "', ";
    query = query + "ranking_hidden_flg='"     + ranking_hidden_flg + "', ";
    query = query + "ownercount='"             + ownercount + "', ";
    query = query + "count_whatsnew_for_hoteltop='"  + count_whatsnew_for_hoteltop + "' ";

    if( mode.compareTo("UPD") == 0 )
    {
        query = query + " WHERE hotel_id='" + hotelid + "'";
    }

    // SQL�N�G���[�̎��s
    prestate = connection.prepareStatement(query);
    int ret  = prestate.executeUpdate();
    DBSync.publish(query);
    DBSync.publish(query,true);
    DBConnection.releaseResources(result,prestate,connection);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>�����ݒ�</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
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
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
          </tr>
          <tr>
            <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
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
                    <td class="size12"><form action="etc_edit.jsp?HotelId=<%= hotelid %>" method="POST">
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
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyrigtht&copy; imedia
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
