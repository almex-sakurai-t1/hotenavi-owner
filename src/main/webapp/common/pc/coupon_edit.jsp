<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
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
    // �z�e��ID�擾
    String hotelid = (String)session.getAttribute("SelectHotel");
    String loginHotelId = (String)session.getAttribute("LoginHotelId");

    String showExpiredCoupon = request.getParameter("showExpiredCoupon");
	if (showExpiredCoupon == null) {
		showExpiredCoupon = "0";
	}

    if( hotelid.compareTo("all") == 0 )
    {
        hotelid = loginHotelId;
    }

    DateEdit  de      = new DateEdit();
    int       nowdate = Integer.parseInt(de.getDate(2));
    String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    //���^�C�v�̃N�[�|�����g�p���Ă��邩���Ȃ��𒲂ׂ�
    boolean   OldCouponExist = false;
    try
    {
        query = "SELECT * FROM edit_coupon WHERE hotelid=?";
        query = query + " AND coupon_type<>99";
        query = query + " AND start_date <=?";
        query = query + " AND end_date >=?";
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setInt(2, nowdate);
        prestate.setInt(3, nowdate);
        result      = prestate.executeQuery();
        if( result.next() )
        {
           OldCouponExist = true;
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

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>�N�[�|���쐬</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/coupon.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/coupon.js"></script>
<script type="text/javascript">
function openCouponEditWindow(hotelId, couponType, Id) {
	MM_openBrWindow(
			'coupon_edit_form.jsp?HotelId=' + hotelId + '&CouponType=' + couponType + '&Id=' + Id,
			'coupon',
			'menubar=no,scrollbars=yes,resizable=no');
}
</script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20">
            <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="100" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF">�N�[�|���쐬</font></td>
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
          <td align="center" valign="top" bgcolor="#FFFFFF">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
                <td><img src="../../common/pc/image/spacer.gif" width="400" height="4"></td>
              </tr>
              <tr>
                <td valign="top" class="size12" colspan="2">
                <div class="size12" style="margin-left:10px">
<%
  if (OldCouponExist)
  {
%>
					<br><font color=red><strong>2008�N8��13�����N�[�|�����V���A���ԍ��ɑΉ��������̂ɂȂ��Ă��܂��B</strong></font><br>
					���N�[�|�����\�������ƁuHP���|�[�g�v�ˁu�N�[�|�����|�[�g�v�ɔ��f����܂��B<br>
					���z�[���y�[�W���m�F���Ă��������A�V���A���ԍ����\������Ă��Ȃ��ꍇ�́A<br>
					<font color=blue><strong>�z�[���y�[�W���̒������K�v</strong></font>�ł��̂ŁA���萔�ł����A�����b�N�X�܂ŘA���肢�܂��B<br>
					&nbsp;<input name="submitnew" type="button" id="submitnew" style="width:300; height:30; border:5px outset; color:blue;font-size:15px;font-weight:bold;" onClick="openCouponEditWindow('<%= hotelid %>', 99, 0)" value="�N�[�|���iPC�Œn�}�t�j�V�K�쐬 [����]">
					<br/><br/><br/>
<%
   }
   else
   {
%>
					&nbsp;<input name="submitnew" type="button" id="submitnew" onClick="openCouponEditWindow('<%= hotelid %>', 99, 0)" value="�V�K�쐬">
<%
   }
%>
				<input id="showExpiredCoupon" type="checkbox" onclick="setCouponShow(this);"<% if ("1".equals(showExpiredCoupon)) { out.print(" checked"); } %>/><label for="showExpiredCoupon">���Ԑ؂�̃N�[�|����\��</label>
              </div>
              </td>
            </tr>
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="4"></td>
            </tr>
            <tr valign="top">
              <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td>
                <div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="4">

                  <tr bgcolor="#666666">
                    <td colspan="2"><strong><font color="#FFFFFF">���݂̃N�[�|��</font></strong><img src="../../common/pc/image/spacer.gif" width="20" height="12"></td>
                  </tr>
                    <%-- ���ݓo�^����Ă���N�[�|���\�� --%>
                    <jsp:include page="coupon_edit_now.jsp" flush="true" >
                    	<jsp:param name="showExpiredCoupon" value="<%=showExpiredCoupon%>" />
                    </jsp:include>
                    <%-- ���ݓo�^����Ă���N�[�|���\�������܂� --%>

                    <td valign="top" class="size12">&nbsp;</td>
                  </tr>
                  <tr>
                    <td valign="top" class="size12"><img src="../../common/pc/image/spacer.gif" width="200" height="4"></td>
                    <td valign="top" class="size12">&nbsp;</td>
                  </tr>
                </table>
                </div>
              </td>
            </tr>
          </table>

          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="4">
                  <tr bgcolor="#DDDDDD">
                    <td colspan="2"><strong>���ӎ���</strong><img src="../../common/pc/image/spacer.gif" width="20" height="12"></td>
                  </tr>
                  <tr>
                    <td colspan="2" align=center>
                    <%-- ���ݓo�^����Ă��钍�ӎ����\�� --%>
                    <jsp:include page="coupon_edit_nowattention.jsp" flush="true" />
                      <input name="submit00" type="button" id="submit00" onClick="MM_openBrWindow('coupon_attention.jsp?HotelId=<%= hotelid %>','���ӎ����ҏW','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���ӎ����ҏW">
                    <%-- ���ݓo�^����Ă��钍�ӎ����\�������܂� --%>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>

<%
  if (OldCouponExist)
  {
%>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="28"></td>
            </tr>
            <tr>
              <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="4">
                  <tr bgcolor="#DDDDDD">
                    <td colspan="2"><strong>�N�[�|�����{</strong><img src="../../common/pc/image/spacer.gif" width="20" height="12"></td>
                  </tr>
                  <tr>
                    <td height="14" colspan="2" valign="top"><img src="../../common/pc/image/spacer.gif" width="200" height="1"></td>
                  </tr>
                  <tr>
                    <td class="size12">�쐬�������N�[�|����I��ł��������B�i���{��65%�ɏk�����ꂽ�傫���ł��B�j<br>
                      <br>
                      <a href="#all">�E�r�W�^�[�^�����o�[��킸</a>�@�@<a href="#member">�E�����o�[����</a>�@�@<a href="#event">�E�C�x���g����</a>�@�@<a href="#season">�E�G�߂���</a>
					  &nbsp;<input name="submit01" type="button" id="submit01" style="width:300; height:30; border:5px outset; color:blue;font-size:15px;font-weight:bold;" onClick="openCouponEditWindow('<%= hotelid %>', 99, 0)" value="�N�[�|���iPC�Œn�}�t�j�V�K�쐬"></td>
                    <td width="20" align="right">&nbsp;</td>
                  </tr>
                </table>
                </div>
              </td>
            </tr>
            <tr>
              <td colspan="2"><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
            </tr>
          </table>

          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td></td>
            </tr>
            <tr>
              <td valign="top">
                <table width="100%" cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                    <td valign="top"><table border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td valign="top" class="size12"><a name="all"></a><strong>�r�W�^�[�^�����o�[��킸</strong></td>
                        <td valign="top">&nbsp;</td>
                        <td align="left" valign="top" nowrap>&nbsp;</td>
                        <td width="24" align="left" valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top" nowrap>&nbsp;</td>
                        <td valign="top" nowrap>&nbsp;</td>
                      </tr>
                      <tr>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_n_f_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" align="left" valign="top" nowrap>
                          <div class="size12"><strong>�x�[�V�b�N�E���ړI�^�C�v</strong><br></div>
                          <div class="size12">
                            �����o�[�l�A��ʂ̂��q�l��킸�g���A<br>
                            �T�[�r�X���e��ς�����̂ŁA<br>
                            �l�X�ȏ�ʂɑΉ��ł��܂��B <br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=1&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                          </div>
                        </td>
                        <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_n_off_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" valign="top" nowrap>
                          <div class="size12"><strong>�x�[�V�b�N�E�����^�C�v</strong><br></div>
                          <div class="size12">
                            �����o�[�l�A��ʂ̂��q�l��킸�A<br>
                            ��������p�Ɏg���܂��B<br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=2&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                          </div>
                        </td>
                        <td valign="top" nowrap><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                      </tr>

                      <tr>
                        <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                      </tr>
                      <tr>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_opn_f_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" align="left" valign="top">
                          <div class="size12"><strong>�I�[�v���L�O�p�E<br>���ړI�^�C�v</strong><br></div>
                          <div class="size12">
                            �����o�[�l�A��ʂ̂��q�l��킸�g���A<br>
                            �I�[�v�����ɁA�T�[�r�X���e��ς��Ďg�����Ƃ��ł��܂��B<br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=3&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                          </div>
                        </td>
                        <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_opn_off_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" valign="top">
                          <div class="size12"><strong>�I�[�v���L�O�p�E<br>�����^�C�v</strong><br></div>
                          <div class="size12">
                            �����o�[�l�A��ʂ̂��q�l��킸�A�I�[�v�����́A�����p�Ɏg�p���܂��B<br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=4&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                          </div>
                        </td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                      </tr>

                      <tr>
                        <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                      </tr>
                      <tr>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_rn_f_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" align="left" valign="top">
                          <div class="size12"><strong>���j���[�A���L�O�p�E<br>���ړI�^�C�v</strong><br></div>
                          <div class="size12">
                            �����o�[�l�A��ʂ̂��q�l��킸�g���A���j���[�A�����ɃT�[�r�X���e��ς��Ďg�p�ł��܂��B<br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=5&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                          </div>
                        </td>
                        <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_rn_off_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" valign="top">
                          <div class="size12"><strong>���j���[�A���L�O�p�E<br>�����^�C�v</strong><br></div>
                          <div class="size12">
                            �����o�[�l�A��ʂ̂��q�l��킸�A���j���[�A�����̊����p�Ɏg�p���܂��B <br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=6&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                          </div>
                        </td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                      </tr>
                      <tr>
                        <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                      </tr>
                      <tr>
                        <td valign="top" class="size12"><a name="member" class="size12"></a><strong>�����o�[����</strong></td>
                        <td valign="top">&nbsp;</td>
                        <td align="left" valign="top">&nbsp;</td>
                        <td width="24" align="left" valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_mb_f_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" align="left" valign="top">
                          <div class="size12"><strong>�����o�[�l�p�E���ړI�^�C�v</strong><br></div>
                          <div class="size12">
                            �����o�[�̕��ɑ΂��Ă̂ݎg�p�ł��܂��B<br>
                            �T�[�r�X���e��ς����A�l�X�ȏ�ʂɑΉ��ł��܂��B <br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=7&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                          </div>
                        </td>
                        <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_mb_off_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" valign="top">
                          <div class="size12"><strong>�����o�[�l�p�E�����^�C�v</strong><br></div>
                          <div class="size12">
                            �����o�[�̕��ɑ΂��Ă̂݁A�����p�Ɏg�p���܂��B<br>
                            <br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=8&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                          </div>
                        </td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                      </tr>
                      <tr>
                        <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                      </tr>
                      <tr>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_md_f_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" align="left" valign="top">
                          <div class="size12"><strong>�L�O���p�E���ړI�^�C�v</strong><br></div>
                          <div class="size12">
                            �����o�[�l�̋L�O���Ɏg���܂��B<br>
                            �T�[�r�X���e��ς����܂��B <br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=9&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                          </div>
                        </td>
                        <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_md_off_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" valign="top">
                          <div class="size12"><strong>�L�O���p�E�����^�C�v</strong><br></div>
                          <div class="size12">
                            �����o�[�l�̋L�O���ɁA�����p�Ɏg�p���܂��B<br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=10&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                          </div>
                        </td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                      </tr>
                      <tr>
                        <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                      </tr>
                      <tr>
                        <td valign="top"><img src="../../common/pc/image/cpn_bd_f_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" align="left" valign="top">
                          <div class="size12"><strong>�a�����p�E���ړI�^�C�v</strong><br></div>
                          <div class="size12">
                            �����o�[�l�̒a�����Ɏg���܂��B<br>
                            �T�[�r�X���e��ς����܂��B<br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=11&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                          </div>
                        </td>
                        <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                        <td valign="top"><img src="../../common/pc/image/cpn_bd_off_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" valign="top">
                          <div class="size12"><strong>�a�����p�E�����^�C�v</strong><br></div>
                          <div class="size12">
                            �����o�[�l�̒a�����ɁA�����p�Ɏg�p���܂��B <br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=12&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                          </div>
                        </td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                      </tr>
                      <tr>
                        <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                      </tr>
                      <tr>
                        <td valign="top" class="size12"><a name="event"></a><strong>�C�x���g����</strong></td>
                        <td valign="top">&nbsp;</td>
                        <td align="left" valign="top">&nbsp;</td>
                        <td width="24" align="left" valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_ny_f_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td align="left" valign="top">
                              <div class="size12"><strong>�������p�E���ړI�^�C�v</strong><br></div>
                              <div class="size12">
                                �������̎��ɁA�g�p���܂��B<br>
                                �T�[�r�X���e��ς���܂��B <br>
                                <br>
                                <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=13&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                                </div>
                            </td>
                            <td width="24" align="left" valign="top">&nbsp;</td>
                            <td valign="top"><img src="../../common/pc/image/cpn_ny_off_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top"><div class="size12"><strong>�������p�E�����^�C�v</strong><br>
                            </div>
                              <div class="size12">�������̎��ɁA�����p�Ɏg�p���܂��B <br>
                                  <br>
                                  <br>
                                  <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=14&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                              </div></td>
                            <td valign="top">&nbsp;</td>
                          </tr>
                          <tr>
                            <td colspan="7" valign="top"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                            <td valign="top">&nbsp;</td>
                          </tr>
                          <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_vd_f_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" align="left" valign="top"><div class="size12"><strong>�o�����^�C���p�E���ړI�^�C�v</strong><br>
                                                                </div>
                                                          <div class="size12">�o�����^�C���̎��ɁA�g�p���܂��B<br>
                                                                  �T�[�r�X���e��ς���܂��B <br>
                                                                  <br>
                                                                  <br>
                                                                    <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=15&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                                                          </div></td>
                            <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                            <td valign="top"><img src="../../common/pc/image/cpn_vd_off_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" valign="top"><div class="size12"><strong>�o�����^�C���p�E�����^�C�v</strong><br>
                                                                </div>
                                                          <div class="size12">�o�����^�C���̎��ɁA�����p�Ɏg�p���܂��B <br>
                                                                  <br>
                                                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=16&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                                                          </div></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                          </tr>
                          <tr>
                            <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                          </tr>
                          <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_wd_f_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" align="left" valign="top"><div class="size12"><strong>�z���C�g�f�C�p�E���ړI�^�C�v</strong><br>
                                                                </div>
                                                          <div class="size12">�z���C�g�f�C�̎��ɁA�g�p���܂��B<br>
                                                                  �T�[�r�X���e��ς����܂��B <br>
                                                                  <br>
                                                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=17&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                                                          </div></td>
                            <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                            <td valign="top"><img src="../../common/pc/image/cpn_wd_off_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" valign="top"><div class="size12"><strong>�z���C�g�f�C�p�E�����^�C�v</strong><br>
                                                                </div>
                                                          <div class="size12">�z���C�g�f�C�̎��ɁA�����p�Ɏg�p���܂��B <br>
                                                                  <br>
                                                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=18&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                                                          </div></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                          </tr>
                          <tr>
                            <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                          </tr>
                          <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_hw_f_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" valign="top"><div class="size12"><strong>�n���E�B���p�E���ړI�^�C�v</strong><br>
                                                                </div>
                                                          <div class="size12">�z���C�g�f�C�̎��ɁA�g�p���܂��B<br>
                                                                  �T�[�r�X���e��ς����܂��B <br>
<br>
<br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=19&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
</div></td>
                            <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                            <td valign="top"><img src="../../common/pc/image/cpn_hw_off_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" valign="top"><div class="size12"><strong>�n���E�B���p�E�����^�C�v</strong><br>
                                                                </div>
                                                          <div class="size12">�n���E�B���̎��ɁA�����p�Ɏg�p���܂��B <br>
                                                                  <br>
                                                              <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=20&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                                                          </div></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                          </tr>
                          <tr>
                            <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                          </tr>
                          <tr>
                            <td width="213" valign="top"><img src="../../common/pc/image/cpn_xms_f_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" align="left" valign="top"><div class="size12"><strong>�N���X�}�X�p�E���ړI�^�C�v</strong><br>
                                                                </div>
                                                          <div class="size12">�N���X�}�X�̎��ɁA�g�p���܂��B<br>
                                                                  �T�[�r�X���e��ς����܂��B <br>
                                                                  <br>
                                                              <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=21&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                                                          </div></td>
                            <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                            <td width="213" valign="top"><img src="../../common/pc/image/cpn_xms_off_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" valign="top"><div class="size12"><strong>�N���X�}�X�p�E�����^�C�v</strong><br>
                                                                </div>
                                                          <div class="size12">�N���X�}�X�̎��ɁA�����p�Ɏg�p���܂��B<br>
                                  <br>
                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=22&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                                                          </div></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                          </tr>
                          <tr>
                            <td colspan="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                            </tr>
                          <tr>
                            <td valign="top" class="size12"><a name="season"></a><strong>�G�߂���</strong></td>
                            <td valign="top">&nbsp;</td>
                            <td align="left" valign="top">&nbsp;</td>
                            <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top">&nbsp;</td>
                          </tr>
                          <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_sp_f_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td align="left" valign="top"><div class="size12"><strong>�t�p�E���ړI�^�C�v</strong><br>
                            </div>
                              <div class="size12">�t�Ɏg�p���܂��B<br>
  �T�[�r�X���e��ς����܂��B <br>
  <br>
  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=23&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                              </div></td>
                            <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                            <td valign="top"><img src="../../common/pc/image/cpn_sp_off_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top"><div class="size12"><strong>�t�p�E�����^�C�v</strong><br>
                            </div>
                              <div class="size12">�t�ɁA�����p�Ɏg�p���܂��B<br>
                                  <br>
                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=24&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                              </div></td>
                            <td valign="top">&nbsp;</td>
                          </tr>
                          <tr>
                            <td colspan="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                            </tr>
                          <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_sm_f_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td align="left" valign="top"><div class="size12"><strong>�ėp�E���ړI�^�C�v</strong><br>
                            </div>
                              <div class="size12">�ĂɎg�p���܂��B<br>
  �T�[�r�X���e��ς����܂��B <br>
  <br>
  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=25&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
  </div></td>
                            <td width="24" align="left" valign="top">&nbsp;</td>
                            <td valign="top"><img src="../../common/pc/image/cpn_sm_off_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top"><div class="size12"><strong>�ėp�E�����^�C�v</strong><br>
                            </div>
                              <div class="size12">�ĂɁA�����p�Ɏg�p���܂��B<br>
                                  <br>
                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=26&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                              </div></td>
                            <td valign="top">&nbsp;</td>
                          </tr>
                          <tr>
                            <td colspan="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                            </tr>
                          <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_au_f_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td align="left" valign="top"><div class="size12"><strong>�H�p�E���ړI�^�C�v</strong><br>
                            </div>
                              <div class="size12">�H�Ɏg�p���܂��B<br>
  �T�[�r�X���e��ς����܂��B <br>
  <br>
  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=27&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                              </div></td>
                            <td align="left" valign="top">&nbsp;</td>
                            <td valign="top"><img src="../../common/pc/image/cpn_au_off_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top"><div class="size12"><strong>�H�p�E�����^�C�v</strong><br>
                            </div>
                              <div class="size12">�H�ɁA�����p�Ɏg�p���܂��B<br>
                                  <br>
                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=28&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                              </div></td>
                            <td valign="top">&nbsp;</td>
                          </tr>
                          <tr>
                            <td colspan="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                            </tr>
                          <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_wn_f_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td align="left" valign="top"><div class="size12"><strong>�~�p�E���ړI�^�C�v</strong><br>
                            </div>
                              <div class="size12">�~�Ɏg�p���܂��B<br>
  �T�[�r�X���e��ς����܂��B <br>
  <br>
  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=29&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
</div></td>
                            <td width="24" align="left" valign="top">&nbsp;</td>
                            <td valign="top"><img src="../../common/pc/image/cpn_wn_off_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top"><div class="size12"><strong>�~�p�E�����^�C�v</strong><br>
                            </div>
                              <div class="size12">�~�ɁA�����p�Ɏg�p���܂��B<br>
                                  <br>
                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=30&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="���̃N�[�|�����쐬">
                              </div></td>
                            <td valign="top">&nbsp;</td>
                          </tr>
                        </table>
                    </td>
                   </tr>
                    </table></td>
              </tr>
           </table>
<%
    }
%>
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
