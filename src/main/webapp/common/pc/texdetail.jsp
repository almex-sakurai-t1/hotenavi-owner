<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
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
    int    roomcode;
    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if( hotelid != null && !CheckString.hotenaviIdCheck(hotelid))
    {
        hotelid = "";
    }
    if (hotelid == null)
    {
      hotelid = "";
    }
    String param_roomcode = ReplaceString.getParameter(request,"RoomCode");
    if( param_roomcode == null )
    {
        roomcode = 0;
    }
    else
    {
        roomcode = Integer.parseInt(param_roomcode);
    }

    ownerinfo.RoomCode = 0;
    ownerinfo.sendPacket0110(1, hotelid);
    ownerinfo.sendPacket0124(1, hotelid);
    int minCode   = ownerinfo.StatusDetailRoomCode[0];
    int RoomCount = ownerinfo.TexRoomCount;

    ownerinfo.RoomCode = roomcode;

    ownerinfo.sendPacket0110(1, hotelid);
    ownerinfo.sendPacket0124(1, hotelid);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>���֋@���o����</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/room.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('../image/yajirushiGrey_f2.gif')">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="220" height="20" bgcolor="#22333F" class="tab" nowrap><font color="#FFFFFF">���֋@���o���i�q���ʌ��ݏ󋵁j(<%=hotelid%>)</font></td>
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
        <td align="center" valign="top" bgcolor="#D0CED5"><table width="98%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td height="6" align="center"><img src="../../common/pc/image/spacer.gif" width="300" height="6"></td>
            </tr>
            <tr>
              <td align="center"><table width="99%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="bar">���o����</td>
                    <td align="right" class="bar2" nowrap>
<%
    if( ownerinfo.RoomCode > minCode )
    {
%> 
              <a href="texdetail.jsp?HotelId=<%= hotelid %>&RoomCode=<%= roomcode - 1 %>"><IMG src="../../common/pc/image/yajirushiGrey2.gif" alt="����" name="next" width="15" height="13" border="0" align="absmiddle" id="next"></a><img src="../../common/pc/image/spacer.gif" width="8" height="12" border="0" align="absmiddle"><a href="texdetail.jsp?HotelId=<%= hotelid %>&RoomCode=<%= roomcode - 1 %>" >�O�̕���</a>
<%
    }

    if( ownerinfo.RoomCode < RoomCount + minCode - 1 )
    {
%>
              <a href="texdetail.jsp?HotelId=<%= hotelid %>&RoomCode=<%= roomcode + 1 %>" >���̕���</a><img src="../../common/pc/image/spacer.gif" width="8" height="12" border="0" align="absmiddle"><a href="texdetail.jsp?HotelId=<%= hotelid %>&RoomCode=<%= roomcode + 1 %>"><IMG src="../../common/pc/image/yajirushiGrey.gif" alt="����" name="next" width="15" height="13" border="0" align="absmiddle" id="next"></a>
<%
    }
%>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="160" height="14"></td>
            </tr>
            <tr>
              <td align="center" valign="top"><table width="99%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr align="center">
                          <td colspan="2" valign="middle" class="tableN"><%= ownerinfo.StatusDetailRoomName[0] %>�̓��o�����</td>
                        </tr>
                        <tr>
                          <td align="left" valign="middle" nowrap class="tableLB"><div class="space6">���֋@���v</div>
                          </td>
                          <td align="right" valign="middle" class="tableRB"><div class="space6R"><%= Kanma.get(ownerinfo.TexSafeTotal[0][2]) %> �~</div>
                          </td>
                        </tr>
                        <tr>
                          <td align="left" valign="middle" nowrap class="tableLW"><div class="space6">���Ȃ���[</div>
                          </td>
                          <td align="right" valign="middle" class="tableRW"><div class="space6R"><%= Kanma.get(ownerinfo.TexSupplyTotal[0]) %> �~</div>
                          </td>
                        </tr>
                        <tr>
                          <td height="20" align="left" valign="middle" nowrap class="tableLG"><div class="space6">�]���</div>
                          </td>
                          <td align="right" valign="middle" class="tableRG"><div class="space6R"><%= Kanma.get(ownerinfo.TexSurplus[0]) %> �~</div>
                          </td>
                        </tr>
                        <tr>
                          <td align="left" valign="middle" nowrap  class="tableLW"><div class="space6">���㍇�v</div>
                          </td>
                          <td align="right" valign="middle" class="tableRW"><div class="space6R"><%= Kanma.get(ownerinfo.TexSalesTotal[0][0] + ownerinfo.TexSalesTotal[0][1]) %> �~</div>
                          </td>
                        </tr>
                      </table>
                    </td>
                    <td width="12"><img src="../../common/pc/image/spacer.gif" width="12" height="50"></td>
                    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td nowrap class="tableN"><div class="space6">�ŏI�W����</div>
                          </td>
                        </tr>
                        <tr>
                          <td nowrap class="tableW">
                            <div class="space6">
                              <%= ownerinfo.TexSupplyDate[0] / 10000 %> �N
                              <%= ownerinfo.TexSupplyDate[0] / 100 % 100 %> ��
                              <%= ownerinfo.TexSupplyDate[0] % 100 %> ��
                            �@<%= ownerinfo.TexSupplyTime[0] / 1000000 %> ��
                              <%= ownerinfo.TexSupplyTime[0] / 10000 % 100 %> ��
                            </div>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td height="12" colspan="3"><img src="../../common/pc/image/spacer.gif" width="200" height="12"></td>
                  </tr>
                  <tr>
                    <td colspan="3"><table width="80%" border="0" cellspacing="0" cellpadding="0">
                        <tr align="left" valign="middle">
                          <td class="tableN" nowrap><div class="space6">��������</div>
                          </td>
                          <td width="180" align="right" class="tableW2"><div class="space6R"><%= Kanma.get(ownerinfo.TexSalesTotal[0][0]) %> �~</div>
                          </td>
                          <td class="tableN" nowrap><div class="space6">�N���W�b�g</div>
                          </td>
                          <td width="180" align="right" class="tableW2"><div class="space6R"><%= Kanma.get(ownerinfo.TexSalesTotal[0][1]) %> �~</div>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td height="24" colspan="3"><img src="../../common/pc/image/spacer.gif" width="200" height="24"></td>
                  </tr>
                  <tr>
                    <td colspan="3"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td class="tableLN">�@</td>
                          <td align="center" valign="middle" class="tableLN">1��</td>
                          <td align="center" valign="middle" class="tableLN">5��</td>
                          <td align="center" valign="middle" class="tableLN">2��</td>
                          <td align="center" valign="middle" class="tableLN">1��</td>
                          <td align="center" valign="middle" class="tableLN">500</td>
                          <td align="center" valign="middle" class="tableLN">100</td>
                          <td align="center" valign="middle" class="tableLN">50</td>
                          <td align="center" valign="middle" class="tableLN">10</td>
                          <td align="center" valign="middle" class="tableLN">sub1</td>
                          <td align="center" valign="middle" class="tableLN">sub2</td>
                          <td align="center" valign="middle" class="tableRN">�J�[�h</td>
                        </tr>
                        <tr>
                          <td nowrap class="tableLW"><div class="space6">�g�[�^������</div>
                          </td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][0][0] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][0][1] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][0][10] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][0][2] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][0][3] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][0][4] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][0][5] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][0][6] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][0][11] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][0][12] %></td>
                          <td align="center" valign="middle" class="tableRW"><%= ownerinfo.TexSafeCount[0][0][9] %></td>
                        </tr>
                        <tr>
                          <td nowrap class="tableLG"><div class="space6">�g�[�^���o��</div>
                          </td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][1][0] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][1][1] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][1][10] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][1][2] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][1][3] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][1][4] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][1][5] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][1][6] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][1][11] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][1][12] %></td>
                          <td align="center" valign="middle" class="tableRG"><%= ownerinfo.TexSafeCount[0][1][9] %></td>
                        </tr>
                        <tr>
                          <td nowrap class="tableLW"><div class="space6">���@��</div>
                          </td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][2][0] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][2][1] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][2][10] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][2][2] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][2][3] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][2][4] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][2][5] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][2][6] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][2][11] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.TexSafeCount[0][2][12] %></td>
                          <td align="center" valign="middle" class="tableRW"><%= ownerinfo.TexSafeCount[0][2][9] %></td>
                        </tr>
                        <tr>
                          <td nowrap class="tableLG"><div class="space6">���ݖ���</div>
                          </td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][3][0] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][3][1] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][3][10] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][3][2] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][3][3] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][3][4] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][3][5] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][3][6] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][3][11] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.TexSafeCount[0][3][12] %></td>
                          <td align="center" valign="middle" class="tableRG"><%= ownerinfo.TexSafeCount[0][3][9] %></td>
                        </tr>
                      </table>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td height="12"><img src="../../common/pc/image/spacer.gif" width="200" height="12"></td>
                          </tr>
                        </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="../image/tab_kado.gif" width="3" height="3"></td>
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
              <td width="3"><img src="../image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="../image/grey.gif" width="3" height="3"></td>
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
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
