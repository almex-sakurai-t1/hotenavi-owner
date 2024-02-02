<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int i;
    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");

    String unknown_list[];
    unknown_list = request.getParameterValues("UnknownAddress");
    if( unknown_list != null )
    {

%>


                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr valign="top">
                        <td width="8" align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12"><table border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="300" nowrap><table width="300" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <td valign="top" nowrap class="size12"><strong>選択した配信不能アドレス一覧</strong></td>
                                <td>&nbsp;</td>
                                <td class="size12">&nbsp;</td>
                              </tr>
                            </table></td>
                            </tr>
                          <tr>
                            <td width="300" class="size12">

<%
        for( i = 0 ; i < unknown_list.length ; i++ )
        {
%>
                              <input type="hidden" name="UnknownAddress" value='<%= unknown_list[i] %>'>
                              <%= ReplaceString.maskedMailAddress(MailAddressEncrypt.decrypt(unknown_list[i]))%><br>
<%
        }
%>



                            </td>
                            <td align="left" valign="bottom"><img src="../../common/pc/image/spacer.gif" width="30" height="12"></td>
                          </tr>
                          <tr valign="top">
                            <td colspan="2"><img src="../../common/pc/image/spacer.gif" width="400" height="8"></td>
                          </tr>
                          <tr>
                            <td colspan="2" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="400" height="8"></td>
                            </tr>
                          <tr>
                            <td><input type="submit" name="Submit4" value="<%=ReplaceString.getParameter(request,"Submit4")%>"></td>
                            <td align="left" valign="bottom"><img src="../../common/pc/image/spacer.gif" width="30" height="12"></td>
                            </tr>
                        </table></td>
                        <td width="8" align="left" class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"><br><br><input type="button" value="戻る" onclick="history.back()"></td>
                        <td width="8" align="left">&nbsp;</td>
                      </tr>
                  </table>
<%
    }
    else
    {
%>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr valign="top">
                        <td width="8" align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12"><table border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="300" nowrap><table width="300" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <td valign="top" nowrap class="size12"><strong>選択した配信不能アドレス一覧</strong></td>
                                <td>&nbsp;</td>
                                <td class="size12">&nbsp;</td>
                              </tr>
                            </table></td>
                            </tr>
                          <tr>
                            <td width="300">選択されていません<br></td>
                            <td align="left" valign="bottom"><img src="../../common/pc/image/spacer.gif" width="30" height="12"></td>
                          </tr>
                          <tr valign="top">
                            <td colspan="2"><img src="../../common/pc/image/spacer.gif" width="400" height="8"></td>
                          </tr>
                          <tr>
                            <td colspan="2" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="400" height="8"></td>
                            </tr>
                          <tr>
                            <td>&nbsp;</td>
                            <td align="left" valign="bottom"><img src="../../common/pc/image/spacer.gif" width="30" height="12"></td>
                            </tr>
                        </table></td>
                        <td width="8" align="left" class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"><input type="button" value="戻る" onclick="history.back()"></td>
                        <td width="8" align="left">&nbsp;</td>
                      </tr>
                  </table>

<%
    }
%>

