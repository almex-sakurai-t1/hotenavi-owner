<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String    hotelid;
    String    hotelname;
    String    salesLayout = "1";

    hotelid = ReplaceString.getParameter(request,"NowHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }
    hotelname = ReplaceString.getParameter(request,"NowHotelName");
    if( hotelname == null )
    {
        hotelname = "";
    }
%>
<!-- �X�ܕ\�� -->
<a name="<%= hotelid %>"></a>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="20">
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="150" height="20" bgcolor="#22333F" class="tab" nowrap><font color="#FFFFFF"><%= hotelname %></font></td>
          <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
          <td height="20">
            <div><img src="../../common/pc/image/spacer.gif" width="12" height="16" align="absmiddle"><a href="#pagetop" class="navy10px">&gt;&gt;���̃y�[�W�̃g�b�v��</a></div>
          </td>
          <td height="20" align="right">
            <%if(ReplaceString.getParameter(request,"HotelIdfromGroup")!=null){%>
            <%-- �߂�@�\--%>
            <input type="button" value="�߂�" onclick="history.back();" >
            <%}%>
          </td>
        </tr>
      </table>
    </td>
    <td width="3">&nbsp;</td>
  </tr>
<!-- ��������\ -->
  <tr>
    <td bgcolor="#BBBBBB">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="2" valign="top"><img src="../../common/pc/image/spacer.gif" width="2" height="3"></td>
          <td height="3" align="left" valign="top" colspan=2><img src="../../common/pc/image/spacer.gif" width="2" height="3"></td>
          <td width="2" valign="top"><img src="../../common/pc/image/spacer.gif" width="2" height="3"></td>
          <td width="3"><img src="../../common/pc/image/bg.gif" width="3" height="3"></td>
        </tr>
        <tr>
          <td width="2" valign="top"><img src="../../common/pc/image/spacer.gif" width="2" height="20"></td>
          <td valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td width="2" valign="top"><img src="../../common/pc/image/spacer.gif" width="2" height="20"></td>
                <td height="20">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td valign="top">
                        <div class="size14">
                          <font color="#FFFFFF">
                  <%-- ���t�\���p�[�c --%>
                            <jsp:include page="../../common/pc/salesdisp_datedisp.jsp" flush="true" />
                          </font>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr align="left" valign="top">
                            <td width="6"><img src="../../common/pc/image/spacer.gif" width="6" height="12"></td>
                            <td>
                 <%-- ������擾�E�\���p�[�c --%>
<%
    if (salesLayout.equals("1"))
    {
%>
                  <jsp:include page="salesdisp_salesdisp1.jsp" flush="true" />
<%
    }
    else
    {
%>
                  <jsp:include page="salesdisp_salesdisp2.jsp" flush="true" />
<%
    }
%>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                    <tr>
                      <td valign="top"><img src="../../common/pc/image/spacer.gif" width="2" height="4"></td>
                    </tr>
                    <tr>
                      <td valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr align="left" valign="top">
                            <td width="6"><img src="../../common/pc/image/spacer.gif" width="6" height="12"></td>
                            <td>
              <%-- ����ڍ׏��擾�E�\���p�[�c --%>
                              <jsp:include page="salesdisp_detailNew.jsp" flush="true" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
          </td>
     <!--��r�Ώ۔���\��-->
          <td valign="top" bgcolor="#A8BEBC">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td width="2" valign="top"><img src="../../common/pc/image/spacer.gif" width="2" height="20"></td>
                <td height="20">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td valign="top">
                        <div class="size14">
                          <font color="#FFFFFF">
                  <%-- ���t�\���p�[�c --%>
                            <jsp:include page="../../common/pc/salescomparedisp_datedisp.jsp" flush="true" />
                          </font>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr align="left" valign="top">
                            <td width="6"><img src="../../common/pc/image/spacer.gif" width="6" height="12"></td>
                            <td>
                 <%-- ������擾�E�\���p�[�c --%>
<%
    if (salesLayout.equals("1"))
    {
%>
                  <jsp:include page="salesdisp_salesdisp1.jsp" flush="true" >
                     <jsp:param name="NowHotel" value="<%= hotelid %>" />
                     <jsp:param name="CompMode" value="2" />
                  </jsp:include>
<%
    }
    else
    {
%>
                  <jsp:include page="salesdisp_salesdisp2.jsp" flush="true" >
                     <jsp:param name="NowHotel" value="<%= hotelid %>" />
                     <jsp:param name="CompMode" value="2" />
                  </jsp:include>
<%
    }
%>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                    <tr>
                      <td valign="top"><img src="../../common/pc/image/spacer.gif" width="2" height="4"></td>
                    </tr>
                    <tr>
                      <td valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr align="left" valign="top">
                            <td width="6"><img src="../../common/pc/image/spacer.gif" width="6" height="12"></td>
                            <td>
              <%-- ����ڍ׏��擾�E�\���p�[�c --%>
                              <jsp:include page="salesdisp_detailNew.jsp" flush="true" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
          </td>
          <td width="2" valign="top" bgcolor="#A8BEBC">&nbsp;</td>
          <td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3"></td>
        </tr>
        <tr>
          <td width="2" valign="top"><img src="../../common/pc/image/spacer.gif" width="2" height="8"></td>
          <td height="3" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="2" height="8"></td>
          <td height="3" align="left" valign="top" bgcolor="#A8BEBC"><img src="../../common/pc/image/spacer.gif" width="2" height="8"></td>
          <td width="2" valign="top" bgcolor="#A8BEBC"><img src="../../common/pc/image/spacer.gif" width="2" height="8"></td>
          <td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3"></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td height="3" bgcolor="#999999">
      <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="3"><img src="../../common/pc/image/bg.gif" width="3" height="3"></td>
          <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
          <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<!-- 1�X�ܖڂ����܂� -->

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="200" height="6"></td>
  </tr>
</table>
