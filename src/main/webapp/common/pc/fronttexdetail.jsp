<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>

<%
    int    termcode;
    String hotelid = ReplaceString.getParameter(request,"HotelId");
	if( hotelid != null && !CheckString.hotenaviIdCheck(hotelid))
	{
            hotelid = "0";
%>
		<script type="text/javascript">		<!--
		var dd = new Date();
		setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
		//-->
		</script>
<%
	}
    if( hotelid == null )
    {
        hotelid = "";
    }
    String param_termcode = ReplaceString.getParameter(request,"TermCode");
    if( param_termcode == null )
    {
        termcode = 0;
    }
    else
    {
        termcode = Integer.parseInt(param_termcode);
    }

    ownerinfo.FrontTexTermCodeIn = termcode;

    ownerinfo.sendPacket0110(1, hotelid);
    ownerinfo.sendPacket0156(1, hotelid);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>両替機入出金状況</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="/common/pc/style/room.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('../image/yajirushiGrey_f2.gif')">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="/common/pc/image/spacer.gif" width="100" height="6"></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="250" height="20" bgcolor="#22333F" class="tab" nowrap><font color="#FFFFFF">フロント精算機入出金（号機別現在状況）</font></td>
              <td width="15" height="20" valign="bottom"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="/common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td align="center" valign="top" bgcolor="#D0CED5"><table width="98%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td height="6" align="center"><img src="/common/pc/image/spacer.gif" width="300" height="6"></td>
            </tr>
            <tr>
              <td align="center"><table width="99%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="bar">入出金状況</td>
                    <td align="right" class="bar2" nowrap>&nbsp;</td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td><img src="/common/pc/image/spacer.gif" width="160" height="14"></td>
            </tr>
            <tr>
              <td align="center" valign="top">
                  <table width="99%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td valign="top">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td class="tableLN">　</td>
                          <td align="center" valign="middle" class="tableLN">1万</td>
                          <td align="center" valign="middle" class="tableLN">5千</td>
                          <td align="center" valign="middle" class="tableLN">2千</td>
                          <td align="center" valign="middle" class="tableLN">1千</td>
                          <td align="center" valign="middle" class="tableLN">500</td>
                          <td align="center" valign="middle" class="tableLN">100</td>
                          <td align="center" valign="middle" class="tableLN">50</td>
                          <td align="center" valign="middle" class="tableLN">10</td>
                          <td align="center" valign="middle" class="tableLN">5</td>
                          <td align="center" valign="middle" class="tableLN">1</td>
                          <td align="center" valign="middle" class="tableRN">金額</td>
                        </tr>
                        <tr>
                          <td nowrap class="tableLW"><div class="space6">初期枚数</div>
                          </td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][7][0] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][7][1] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][7][10] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][7][2] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][7][3] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][7][4] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][7][5] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][7][6] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][7][11] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][7][12] %></td>
                          <td align="right" valign="middle" class="tableRW"><%= Kanma.get(ownerinfo.FrontTexSafeTotal[0][7]) %></td>
                        </tr>
                        <tr>
                          <td nowrap class="tableLG"><div class="space6">取引差引</div>
                          </td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][2][0] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][2][1] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][2][10] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][2][2] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][2][3] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][2][4] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][2][5] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][2][6] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][2][11] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][2][12] %></td>
                          <td align="right" valign="middle" class="tableRG"><%= Kanma.get(ownerinfo.FrontTexSafeTotal[0][2]) %></td>
                        </tr>
                        <tr>
                          <td nowrap class="tableLW"><div class="space6">補充差引</div>
                          </td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][5][0] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][5][1] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][5][10] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][5][2] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][5][3] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][5][4] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][5][5] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][5][6] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][5][11] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][5][12] %></td>
                          <td align="right" valign="middle" class="tableRW"><%= Kanma.get(ownerinfo.FrontTexSafeTotal[0][5]) %></td>
                        </tr>
                        <tr>
                          <td nowrap class="tableLG"><div class="space6">現在枚数</div>
                          </td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][6][0] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][6][1] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][6][10] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][6][2] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][6][3] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][6][4] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][6][5] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][6][6] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][6][11] %></td>
                          <td align="center" valign="middle" class="tableLG"><%= ownerinfo.FrontTexSafeCount[0][6][12] %></td>
                          <td align="right" valign="middle" class="tableRG"><%= Kanma.get(ownerinfo.FrontTexSafeTotal[0][6]) %></td>
                        </tr>
<%if (ownerinfo.FrontTexSafeCount[0][8][0]!=0 || ownerinfo.FrontTexSafeCount[0][8][1] !=0 || ownerinfo.FrontTexSafeCount[0][8][10] !=0 || ownerinfo.FrontTexSafeCount[0][8][2] != 0 || ownerinfo.FrontTexSafeCount[0][8][3] != 0 || ownerinfo.FrontTexSafeCount[0][8][4] != 0 || ownerinfo.FrontTexSafeCount[0][8][5] != 0 || ownerinfo.FrontTexSafeCount[0][8][6] != 0 || ownerinfo.FrontTexSafeCount[0][8][11] != 0 || ownerinfo.FrontTexSafeCount[0][8][12] != 0){%>
                        <tr>
                          <td nowrap class="tableLW"><div class="space6">（釣銭可能枚数）</div>
                          </td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][8][0] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][8][1] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][8][10] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][8][2] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][8][3] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][8][4] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][8][5] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][8][6] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][8][11] %></td>
                          <td align="center" valign="middle" class="tableLW"><%= ownerinfo.FrontTexSafeCount[0][8][12] %></td>
                          <td align="right" valign="middle" class="tableRW"></td>
                        </tr>
<%}%>
                      </table>
                    </td>
                    <td width="12"><img src="/common/pc/image/spacer.gif" width="12" height="50"></td>
                    <td valign="top">
                    </td>
                  </tr>
                  <tr>
                    <td height="12" colspan="3"><img src="/common/pc/image/spacer.gif" width="200" height="12"></td>
                  </tr>
                  <tr>
                    <td height="24" colspan="3"><img src="/common/pc/image/spacer.gif" width="200" height="24"></td>
                  </tr>
                  <tr>
                    <td colspan="3">
                        <table width="50%" border="0" cellspacing="0" cellpadding="0">
                        <tr align="center">
                          <td colspan="4" valign="middle" class="tableN"><%= ownerinfo.FrontTexTermName[0] %>の売上情報</td>
                        </tr>
                        <tr>
                          <td align="left" valign="middle" nowrap class="tableLW" rowspan=2><div class="space6">現金</div>
                          </td>
                          <td align="left" valign="middle" nowrap class="tableLW"><div class="space6">IN</div>
                          </td>
                          <td align="right" valign="middle" class="tableLW"><div class="space6R"><%= Kanma.get(ownerinfo.FrontTexSalesCount[0][0]) %> 回</div>
                          </td>
                          <td align="right" valign="middle" class="tableRW"><div class="space6R"><%= Kanma.get(ownerinfo.FrontTexSalesTotal[0][0]) %> 円</div>
                          </td>
                        </tr>
                        <tr>
                          <td align="left" valign="middle" nowrap class="tableLG"><div class="space6">OUT</div>
                          </td>
                          <td align="right" valign="middle" class="tableLG"><div class="space6R"><%= Kanma.get(ownerinfo.FrontTexSalesCount[0][1]) %> 回</div>
                          </td>
                          <td align="right" valign="middle" class="tableRG"><div class="space6R"><%= Kanma.get(ownerinfo.FrontTexSalesTotal[0][1]) %> 円</div>
                          </td>
                        </tr>
                        <tr>
                          <td align="left" valign="middle" nowrap class="tableLW" rowspan=2><div class="space6">クレジット</div>
                          </td>
                          <td align="left" valign="middle" nowrap class="tableLW"><div class="space6">IN</div>
                          </td>
                          <td align="right" valign="middle" class="tableLW"><div class="space6R"><%= Kanma.get(ownerinfo.FrontTexSalesCount[0][2]) %> 回</div>
                          </td>
                          <td align="right" valign="middle" class="tableRW"><div class="space6R"><%= Kanma.get(ownerinfo.FrontTexSalesTotal[0][2]) %> 円</div>
                          </td>
                        </tr>
                        <tr>
                          <td align="left" valign="middle" nowrap class="tableLG"><div class="space6">OUT</div>
                          </td>
                          <td align="right" valign="middle" class="tableLG"><div class="space6R"><%= Kanma.get(ownerinfo.FrontTexSalesCount[0][3]) %> 回</div>
                          </td>
                          <td align="right" valign="middle" class="tableRG"><div class="space6R"><%= Kanma.get(ownerinfo.FrontTexSalesTotal[0][3]) %> 円</div>
                          </td>
                        </tr>
                        <tr>
                          <td align="left" valign="middle" nowrap class="tableLW" rowspan=2><div class="space6">その他</div>
                          </td>
                          <td align="left" valign="middle" nowrap class="tableLW"><div class="space6">IN</div>
                          </td>
                          <td align="right" valign="middle" class="tableLW"><div class="space6R"><%= Kanma.get(ownerinfo.FrontTexSalesCount[0][4]+ownerinfo.FrontTexSalesCount[0][6]) %> 回</div>
                          </td>
                          <td align="right" valign="middle" class="tableRW"><div class="space6R"><%= Kanma.get(ownerinfo.FrontTexSalesTotal[0][4]+ownerinfo.FrontTexSalesTotal[0][6]) %> 円</div>
                          </td>
                        </tr>
                        <tr>
                          <td align="left" valign="middle" nowrap class="tableLG"><div class="space6">OUT</div>
                          </td>
                          <td align="right" valign="middle" class="tableLG"><div class="space6R"><%= Kanma.get(ownerinfo.FrontTexSalesCount[0][5]+ownerinfo.FrontTexSalesCount[0][7]) %> 回</div>
                          </td>
                          <td align="right" valign="middle" class="tableRG"><div class="space6R"><%= Kanma.get(ownerinfo.FrontTexSalesTotal[0][5]+ownerinfo.FrontTexSalesTotal[0][7]) %> 円</div>
                          </td>
                        </tr>
                        <tr>
                          <td align="left" valign="middle" nowrap  class="tableLB" colspan=2><div class="space6">売上合計</div>
                          </td>
                          <td align="right" valign="middle" class="tableLB"><div class="space6R"><%= Kanma.get(ownerinfo.FrontTexSalesCount[0][0] + ownerinfo.FrontTexSalesCount[0][1] + ownerinfo.FrontTexSalesCount[0][2] + ownerinfo.FrontTexSalesCount[0][3] + ownerinfo.FrontTexSalesCount[0][4] + ownerinfo.FrontTexSalesCount[0][5] + ownerinfo.FrontTexSalesCount[0][6] + ownerinfo.FrontTexSalesCount[0][7]) %> 回</div>
                          </td>
                          <td align="right" valign="middle" class="tableRB"><div class="space6R"><%= Kanma.get(ownerinfo.FrontTexSalesTotal[0][0] + ownerinfo.FrontTexSalesTotal[0][1] + ownerinfo.FrontTexSalesTotal[0][2] + ownerinfo.FrontTexSalesTotal[0][3] + ownerinfo.FrontTexSalesTotal[0][4] + ownerinfo.FrontTexSalesTotal[0][5] + ownerinfo.FrontTexSalesTotal[0][6] + ownerinfo.FrontTexSalesTotal[0][7]) %> 円</div>
                          </td>
                        </tr>
                        </table>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td height="12"><img src="/common/pc/image/spacer.gif" width="200" height="12"></td>
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
              <td bgcolor="#666666" height="100%"><img src="/common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="../image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="/common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="../image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- ここまで -->
    </table></td>
  </tr>
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="/common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="/common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyrigtht&copy; almex
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
