<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.owner.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
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
    int    i;
    int    numdata;
    int    group;
    int    money;
    int    now_year;
    int    now_month;
    int    ymd;
    int    all;
    String data;
    String param_year = request.getParameter("Year");
    String param_month = request.getParameter("Month");
    String param_group = request.getParameter("SelectGroup");
    String param_money = request.getParameter("SelectMoney");
    String param_new   = request.getParameter("New");
    String param_all   = request.getParameter("AllInput");
    String[] params = new String[]{param_year, param_month, param_group, param_money, param_new, param_all};
    for(String paramStr : params)
    {
        if(paramStr != null && !CheckString.numCheck(paramStr))
        {
            response.sendError(400);
            return;
        }
    }
    DateEdit de = new DateEdit();
    OwnerInfo refertarget = (OwnerInfo)session.getAttribute("refertarget");
    OwnerInfo refermoney = (OwnerInfo)session.getAttribute("refermoney");

    // セッション属性より選択されたホテルを取得
    String selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    if( param_all != null )
    {
        all = Integer.parseInt(param_all);
    }
    else
    {
        all = 0;
    }

    if( refertarget == null || param_new != null )
    {
        refertarget = new OwnerInfo();
    }
    if( refermoney == null || param_new != null )
    {
        refermoney = new OwnerInfo();
    }

    if( param_year == null )
    {
        numdata = ownerinfo.TargetMonth;
        param_year = Integer.toString(numdata / 100);
    }
    else
    {
        ownerinfo.TargetMonth = Integer.parseInt(param_year) * 100;
    }
    if( param_month == null )
    {
        numdata = ownerinfo.TargetMonth;
        param_month = Integer.toString(numdata % 100);
    }
    else
    {
        ownerinfo.TargetMonth = Integer.parseInt(param_year) * 100 + Integer.parseInt(param_month);
    }

    // 指定した年月をカレンダーにセット
    ymd = Integer.parseInt(param_year) * 10000 + Integer.parseInt(param_month) * 100 + 1;

    if( param_group == null )
    {
        data = (String)session.getAttribute("SelectGroup");
        if( data != null )
        {
            group = Integer.parseInt(data);
        }
        else
        {
            group = 0;
        }
    }
    else
    {
        group = Integer.parseInt(param_group);
        session.setAttribute("SelectGroup", Integer.toString(group));
        refertarget.HotelId = (String)session.getAttribute("SelectHotel");

        switch( group )
        {
          case  0:
            // 前年同月実績取得
            refertarget.TargetMonth = de.addYear(ymd, -1) / 100;
            refertarget.sendPacket0138();
            break;

          case  1:
            // 前月実績取得
            refertarget.TargetMonth = de.addMonth(ymd, -1) / 100;
            refertarget.sendPacket0138();
            break;

          case  2:
            // 前年同月目標取得
            refertarget.TargetMonth = de.addYear(ymd, -1) / 100;
            refertarget.sendPacket0134();
            break;

          case  3:
            // 前月目標取得
            refertarget.TargetMonth = de.addMonth(ymd, -1) / 100;
            refertarget.sendPacket0134();
            break;
        }

        // セッション属性にセット
        session.setAttribute("refertarget", refertarget);
    }
    if( param_money == null )
    {
        data = (String)session.getAttribute("SelectMoney");
        if( data != null )
        {
            money = Integer.parseInt(data);
        }
        else
        {
            money = 0;
        }
    }
    else
    {
        money = Integer.parseInt(param_money);
        session.setAttribute("SelectMoney", Integer.toString(money));
        refermoney.HotelId = (String)session.getAttribute("SelectHotel");

        switch( money )
        {
          case  0:
            // 前年同月実績取得
            refermoney.TargetMonth = de.addYear(ymd, -1) / 100;
            refermoney.sendPacket0138();
            break;

          case  1:
            // 前月実績取得
            refermoney.TargetMonth = de.addMonth(ymd, -1) / 100;
            refermoney.sendPacket0138();
            break;

          case  2:
            // 前年同月目標取得
            refermoney.TargetMonth = de.addYear(ymd, -1) / 100;
            refermoney.sendPacket0134();
            break;

          case  3:
            // 前月目標取得
            refermoney.TargetMonth = de.addMonth(ymd, -1) / 100;
            refermoney.sendPacket0134();
            break;
        }

        // セッション属性にセット
        session.setAttribute("refermoney", refermoney);
    }

    if( all != 0 )
    {
        // 取得したデータをコピー
        if( all == 1 )
        {
            // 組数情報
            ownerinfo.TargetCount = refertarget.TargetCount;
            for( i = 0 ; i < refertarget.TargetModeCount ; i++ )
            {
                ownerinfo.TargetModeCode[i] = refertarget.TargetModeCode[i]; 
                ownerinfo.TargetModeName[i] = refertarget.TargetModeName[i]; 
                ownerinfo.TargetModeRestCount[i] = refertarget.TargetModeRestCount[i]; 
                ownerinfo.TargetModeStayCount[i] = refertarget.TargetModeStayCount[i]; 
            }
        }
        else if( all == 2 )
        {
            // 金額情報
            ownerinfo.TargetTotal = refermoney.TargetTotal;
            for( i = 0 ; i < refermoney.TargetModeCount ; i++ )
            {
                ownerinfo.TargetModeCode[i] = refermoney.TargetModeCode[i]; 
                ownerinfo.TargetModeName[i] = refermoney.TargetModeName[i]; 
                ownerinfo.TargetModeRestTotal[i] = refermoney.TargetModeRestTotal[i]; 
                ownerinfo.TargetModeStayTotal[i] = refermoney.TargetModeStayTotal[i]; 
            }
        }
    }
    else
    {
        // データを取得する
        // 目標値取得
        ownerinfo.sendPacket0134(1, selecthotel);
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>売上目標設定</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><img src="/common/pc/image/spacer.gif" width="100" height="6"></td>
        </tr>
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" valign="top">
            <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="140" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF">売上目標設定</font></td>
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
        <tr valign="top">
          <td align="left" valign="top" bgcolor="#E2D8CF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td height="4" valign="top"><img src="/common/pc/image/spacer.gif" width="8" height="4"></td>
              <td align="left" valign="top"><img src="/common/pc/image/spacer.gif" width="10" height="4"></td>
              <td width="8" valign="top"><img src="/common/pc/image/spacer.gif" width="8" height="4"></td>
            </tr>
            <tr>
              <td height="4" valign="top"><img src="/common/pc/image/spacer.gif" width="8" height="8"></td>
              <td align="left" valign="top"><img src="/common/pc/image/spacer.gif" width="40" height="8"></td>
              <td width="8" valign="top"><img src="/common/pc/image/spacer.gif" width="8" height="4"></td>
            </tr>
            <tr>
              <td width="8" height="4" valign="top"><img src="/common/pc/image/spacer.gif" width="8" height="100"></td>
              <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="90">&nbsp;</td>
                  <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="24" valign="top" nowrap background="/common/pc/image/kumisu_bg.gif"><img src="/common/pc/image/kumisu.gif" width="50" height="24"></td>
                      <td height="24" nowrap background="/common/pc/image/kumisu_bg.gif">&nbsp;</td>
                    </tr>
                    <form name="selectgroup" action="master_target.jsp" method="post">
                      <tr bgcolor="#CCD7E5">
                        <td height="38" valign="top" nowrap class="size12">▼当月目標</td>
                        <td height="38" valign="top" nowrap class="size12">▼参照データ<br>
                            <select name="SelectGroup" class="size12">
<%
    if( group == 0 )
    {
%>
                              <option value="0" selected>前年同月実績</option>
<%
    }
    else
    {
%>
                              <option value="0">前年同月実績</option>
<%
    }
%>
<%
    if( group == 1 )
    {
%>
                              <option value="1" selected>前月実績</option>
<%
    }
    else
    {
%>
                              <option value="1">前月実績</option>
<%
    }
%>
<%
    if( group == 2 )
    {
%>
                              <option value="2" selected>前年同月目標</option>
<%
    }
    else
    {
%>
                              <option value="2">前年同月目標</option>
<%
    }
%>
<%
    if( group == 3 )
    {
%>
                              <option value="3" selected>前月目標</option>
<%
    }
    else
    {
%>
                              <option value="3">前月目標</option>
<%
    }
%>
                            </select>
                            <input name="Submit4" type="submit" class="size12" value="照会">
                      </tr>
                    </form>
                  </table></td>
                  <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="24" nowrap background="/common/pc/image/kingaku_bg.gif"><img src="/common/pc/image/kingaku.gif" width="50" height="24"></td>
                      <td height="24" nowrap background="/common/pc/image/kingaku_bg.gif">&nbsp;</td>
                    </tr>
                    <form name="selectmoney" action="master_target.jsp" method="post">
                    <tr valign="top" bgcolor="#CCE1AF">
                      <td height="38" nowrap class="size12">▼当月目標</td>
                      <td height="38" nowrap class="size12">▼参照データ<br>
                        <select name="SelectMoney" class="size12">
<%
    if( money == 0 )
    {
%>
                              <option value="0" selected>前年同月実績</option>
<%
    }
    else
    {
%>
                              <option value="0">前年同月実績</option>
<%
    }
%>
<%
    if( money == 1 )
    {
%>
                              <option value="1" selected>前月実績</option>
<%
    }
    else
    {
%>
                              <option value="1">前月実績</option>
<%
    }
%>
<%
    if( money == 2 )
    {
%>
                              <option value="2" selected>前年同月目標</option>
<%
    }
    else
    {
%>
                              <option value="2">前年同月目標</option>
<%
    }
%>
<%
    if( money == 3 )
    {
%>
                              <option value="3" selected>前月目標</option>
<%
    }
    else
    {
%>
                              <option value="3">前月目標</option>
<%
    }
%>
                        </select>
                        <input type="submit" name="Submit43" value="照会" class="size12"></td>
                    </tr>
                    </form>
                  </table></td>
                </tr>
<form action="master_update.jsp" method="post">
                <tr>
                  <td valign="top" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="24">&nbsp;</td>
                      </tr>
                    <tr>
                      <td height="24">&nbsp;</td>
                      </tr>
                    <tr>
                      <td height="24">&nbsp;</td>
                      </tr>
                    <tr>
                      <td height="24" align="center" valign="middle" nowrap class="tableLWG">料金モード</td>
                    </tr>
<%
    for( i = 0 ; i < ownerinfo.TargetModeCount ; i++ )
    {
        if( (i % 2) == 0 )
        {
%>
                    <tr>
                      <td height="24" align="center" valign="middle" nowrap class="tableLW"><%= ownerinfo.TargetModeName[i] %></td>
                    </tr>
<%
        }
        else
        {
%>
                    <tr>
                      <td height="24" align="center" valign="middle" nowrap class="tableLG"><%= ownerinfo.TargetModeName[i] %></td>
                    </tr>
<%
        }
    }
%>
                  </table></td>
                  <td valign="top" bgcolor="#CCD7E5"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="24" colspan="2" align="center" valign="middle" nowrap class="tableLB3">累計組数目標（単位：組）</td>
                      <td height="24" colspan="2" align="center" valign="middle" nowrap class="tableLWG">累計組数（単位：組）</td>
                    </tr>
                    <tr>
                      <td height="24" colspan="2" align="center" valign="middle" nowrap class="tableLW"><input name="TargetCount" type="text" size="10" class="size12"  style="text-align:right" value="<%= ownerinfo.TargetCount %>">
                      </td>
                      <td height="24" colspan="2" align="center" valign="middle" nowrap class="tableLW"><%= refertarget.TargetCount %></td>
                    </tr>
                    <tr>
                      <td height="24" colspan="2" align="center" valign="middle" nowrap class="tableLN3">一日当り組数目標（単位：組）</td>
                      <td height="24" colspan="2" align="center" valign="middle" nowrap class="tableLWG2">組　数（単位：組）</td>
                    </tr>
                    <tr>
                      <td height="24" align="center" valign="middle" nowrap class="tableLN">休憩</td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLN">宿泊</td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLWG2">休憩</td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLWG2">宿泊</td>
                    </tr>
<%
    for( i = 0 ; i < ownerinfo.TargetModeCount ; i++ )
    {
        if( (i % 2) == 0 )
        {
%>
                    <tr>
                      <td height="24" align="center" valign="middle" nowrap class="tableLW"><input name="TargetModeRestCount<%= i+1 %>" type="text" size="10" class="size12" style="text-align:right"  value="<%= ownerinfo.TargetModeRestCount[i] %>">
                      </td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLW"><input name="TargetModeStayCount<%= i+1 %>" type="text" size="10" class="size12" style="text-align:right"  value="<%= ownerinfo.TargetModeStayCount[i] %>">
                      </td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLW"><%= refertarget.TargetModeRestCount[i] %></td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLW"><%= refertarget.TargetModeStayCount[i] %></td>
                    </tr>
<%
        }
        else
        {
%>
                    <tr>
                      <td height="24" align="center" valign="middle" nowrap class="tableLLB"><input name="TargetModeRestCount<%= i+1 %>" type="text" size="10" class="size12" style="text-align:right"  value="<%= ownerinfo.TargetModeRestCount[i] %>">
                      </td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLLB"><input name="TargetModeStayCount<%= i+1 %>" type="text" size="10" class="size12" style="text-align:right"  value="<%= ownerinfo.TargetModeStayCount[i] %>">
                      </td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLG"><%= refertarget.TargetModeRestCount[i] %></td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLG"><%= refertarget.TargetModeStayCount[i] %></td>
                    </tr>
<%
        }
    }
%>

                    <tr>
                      <td height="32" colspan="4" align="center" valign="middle" nowrap class="tableLB">
<%
if(group == 2 || group == 3){ 
%>
                        <input type="submit" name="Submit3" value="一括入力" onClick="MM_goToURL('parent.frames[\'targetFrame\']','master_target.jsp?AllInput=1');return document.MM_returnValue">
                        <img src="/common/pc/image/spacer.gif" width="3" height="30" align="absmiddle"><img src="/common/pc/image/yaji_bl.gif" width="27" height="26" align="absmiddle">
<%
}
%>
                      </td>
                    </tr>
                  </table></td>
                  <td valign="top" bgcolor="#BCCC9E"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="24" colspan="2" align="center" valign="middle" nowrap class="tableGR">累計金額目標（単位：円）</td>
                      <td height="24" colspan="2" align="center" valign="middle" nowrap class="tableLWG">累計金額（単位：円）</td>
                    </tr>
                    <tr>
                      <td height="24" colspan="2" align="center" valign="middle" nowrap class="tableLW"><input name="TargetTotal" type="text" size="20" class="size12" style="text-align:right"  value="<%= ownerinfo.TargetTotal %>">
                      </td>
                      <td height="24" colspan="2" align="center" valign="middle" nowrap class="tableRW"><%= refermoney.TargetTotal %></td>
                    </tr>
                    <tr>
                      <td height="24" colspan="2" align="center" valign="middle" class="tableLDG">一日当り金額目標（単位：円）</td>
                      <td height="24" colspan="2" align="center" valign="middle" nowrap class="tableRWG">金　額（単位：円）</td>
                    </tr>
                    <tr>
                      <td height="24" align="center" valign="middle" nowrap class="tableLDG">休憩</td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLDG">宿泊</td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLWG2">休憩</td>
                      <td height="24" align="center" valign="middle" nowrap class="tableRWG">宿泊</td>
                    </tr>
<%
    for( i = 0 ; i < ownerinfo.TargetModeCount ; i++ )
    {
        if( (i % 2) == 0 )
        {
%>
                    <tr>
                      <td height="24" align="center" valign="middle" nowrap class="tableLW"><input name="TargetModeRestTotal<%= i+1 %>" type="text" size="10" class="size12" style="text-align:right"  value="<%= ownerinfo.TargetModeRestTotal[i] %>">
                      </td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLW"><input name="TargetModeStayTotal<%= i+1 %>" type="text" size="10" class="size12" style="text-align:right"  value="<%= ownerinfo.TargetModeStayTotal[i] %>">
                      </td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLW"><%= refermoney.TargetModeRestTotal[i] %></td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLW"><%= refermoney.TargetModeStayTotal[i] %></td>
                    </tr>
<%
        }
        else
        {
%>
                    <tr>
                      <td height="24" align="center" valign="middle" nowrap class="tableLLB"><input name="TargetModeRestTotal<%= i+1 %>" type="text" size="10" class="size12" style="text-align:right"  value="<%= ownerinfo.TargetModeRestTotal[i] %>">
                      </td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLLB"><input name="TargetModeStayTotal<%= i+1 %>" type="text" size="10" class="size12" style="text-align:right"  value="<%= ownerinfo.TargetModeStayTotal[i] %>">
                      </td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLG"><%= refermoney.TargetModeRestTotal[i] %></td>
                      <td height="24" align="center" valign="middle" nowrap class="tableLG"><%= refermoney.TargetModeStayTotal[i] %></td>
                    </tr>
<%
        }
    }
%>


                    <tr>

                      <td height="32" colspan="4" align="center" valign="middle" nowrap class="tableGR2">
<%
if(money == 2 || money == 3){ 
%>
                        <input type="submit" name="Submit32" value="一括入力" onClick="MM_goToURL('parent.frames[\'targetFrame\']','master_target.jsp?AllInput=2');return document.MM_returnValue">
                        <img src="/common/pc/image/spacer.gif" width="3" height="30" align="absmiddle"><img src="/common/pc/image/yaji_gr.gif" width="26" height="27" align="absmiddle">
<%
}
%>
                      </td>

                    </tr>
                  </table></td>
                </tr>
                <tr align="center" valign="middle">
                  <td height="28">&nbsp;</td>
                  <td height="28" colspan="2" class="tableDG3">
                    <input type="submit" name="Submit" value="設定する">
                    &nbsp;
                    <input type="reset" name="Submit2" value="リセット" title="読み込んだ状態に戻します">
                  </td>
                </tr>
</form>
              </table></td>
              <td width="8" valign="top"><img src="/common/pc/image/spacer.gif" width="8" height="4"></td>
            </tr>
            <tr>
              <td height="4" valign="top">&nbsp;</td>
              <td align="left" valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
            </tr>
          </table></td>
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
        <!-- ここまで -->
      </table>
    </td>
  </tr>
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="/common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="/common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
</table>
</body>
</html>
