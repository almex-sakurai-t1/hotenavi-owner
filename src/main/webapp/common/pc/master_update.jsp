<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.owner.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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
    int    value;
    String param_data;

    // セッション属性より選択されたホテルを取得
    String selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    // 計上年月
    ownerinfo.ResultMonth = ownerinfo.TargetMonth;

    // 累計組数
    param_data = request.getParameter("TargetCount");
    if( param_data == null )
    {
        value = 0;
    }
    else
    {
        if(!CheckString.numCheck(param_data))
        {
            response.sendError(400);
            return;
        }
        value = Integer.parseInt(param_data);
    }
    ownerinfo.ResultCount = value;

    // 累計金額
    param_data = request.getParameter("TargetTotal");
    if( param_data == null )
    {
        value = 0;
    }
    else
    {
        if(!CheckString.numCheck(param_data))
        {
            response.sendError(400);
            return;
        }
        value = Integer.parseInt(param_data);
    }
    ownerinfo.ResultTotal = value;

    // 料金モード数
    ownerinfo.ResultModeCount = ownerinfo.TargetModeCount;

    for( i = 0 ; i < ownerinfo.TargetModeCount ; i++ )
    {
        // 料金モード
        ownerinfo.ResultModeCode[i] = ownerinfo.TargetModeCode[i];

        // 休憩組数
        param_data = request.getParameter("TargetModeRestCount" + (i+1));
        if( param_data == null )
        {
            value = 0;
        }
        else
        {
            if(!CheckString.numCheck(param_data))
            {
                response.sendError(400);
                return;
            }
            value = Integer.parseInt(param_data);
        }
        ownerinfo.ResultModeRestCount[i] = value;

        // 宿泊組数
        param_data = request.getParameter("TargetModeStayCount" + (i+1));
        if( param_data == null )
        {
            value = 0;
        }
        else
        {
            if(!CheckString.numCheck(param_data))
            {
                response.sendError(400);
                return;
            }
            value = Integer.parseInt(param_data);
        }
        ownerinfo.ResultModeStayCount[i] = value;

        // 休憩金額
        param_data = request.getParameter("TargetModeRestTotal" + (i+1));
        if( param_data == null )
        {
            value = 0;
        }
        else
        {
            if(!CheckString.numCheck(param_data))
            {
                response.sendError(400);
                return;
            }
            value = Integer.parseInt(param_data);
        }
        ownerinfo.ResultModeRestTotal[i] = value;

        // 宿泊金額
        param_data = request.getParameter("TargetModeStayTotal" + (i+1));
        if( param_data == null )
        {
            value = 0;
        }
        else
        {
            if(!CheckString.numCheck(param_data))
            {
                response.sendError(400);
                return;
            }
            value = Integer.parseInt(param_data);
        }
        ownerinfo.ResultModeStayTotal[i] = value;
    }

    ownerinfo.sendPacket0136(1, selecthotel);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>売上目標設定</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
        </tr>
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" valign="top">
            <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="140" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF">売上目標設定</font></td>
                <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
                <td height="20">
                  <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
                </td>
              </tr>
            </table>
          </td>
          <td width="3">&nbsp;</td>
        </tr>
        <!-- ここから表 -->
        <tr valign="top">
          <td align="left" valign="top" bgcolor="#E2D8CF">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
              <td align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="10" height="4"></td>
              <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
            </tr>
            <tr>
              <td height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
              <td align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="40" height="8"></td>
              <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
            </tr>
            <tr>
              <td height="4" valign="top">設定完了しました</td>
              <td align="left" valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
            </tr>
            <tr>
              <td height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
              <td align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="40" height="8"></td>
              <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
            </tr>
            <tr>
              <td height="4" valign="top">
                <form action="master_target.jsp" method="post">
                  <input type="submit" name="Submit" value="戻る">
                </form>
              </td>
              <td align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="40" height="8"></td>
              <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
            </tr>
          </table></td>
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
        <!-- ここまで -->
      </table>
    </td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
</table>
</body>
</html>
