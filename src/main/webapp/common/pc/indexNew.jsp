<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.Constants" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/owner_ini.jsp" %><%

    String requestUri = request.getRequestURI();
    boolean DebugMode = false;
    if (requestUri.indexOf("_debug_") != -1 || requestUri.indexOf(projectName) != -1)
    {
       DebugMode = true;
    }
    String hotelid    = requestUri.replace("_debug_","").replace(projectName,"");
    hotelid           = hotelid.replace("pc/top.jsp","");
    hotelid           = hotelid.replace("pc/index.html","");
    hotelid           = hotelid.replace("/","");

    if (hotelid.indexOf(";jsessionid") != -1)
    {
        hotelid = hotelid.substring(0,hotelid.indexOf(";jsessionid"));
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<META http-equiv="Content-Style-Type" content="text/css">
<title>オーナーサイト</title>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/index_datacheck.js"></script>
<link rel="stylesheet" href="../../common/pc/style/index.css" type="text/css">
</head>

<BODY bgcolor="#000039" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('../../common/pc/image/yaji_big_o.gif')">
<FORM name=hotenaviLogin action="ownerlogin.jsp" method="POST" target="_top" class="size20">
<input type="hidden" name="HotelId" value="<%=hotelid%>">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td>&nbsp;</td>
    <td width="800" align="center" valign="top">
      <table width="800" border="0" cellpadding="0" cellspacing="0" bgcolor="#000000">
        <tr>
          <td><img src="../../common/pc/image/loginhead.jpg" alt="オーナーサイト" name="image" width="800" height="116" id="image"></td>
        </tr>
      </table>
      <table width="800" border="0" cellpadding="0" cellspacing="0" background="../../common/pc/image/bg.gif" bgcolor="#000000">
        <tr>
          <td><img src="../../common/pc/image/spacer.gif" width="40" height="30"></td>
          <td align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="40" height="30"></td>
          <td><img src="../../common/pc/image/spacer.gif" width="26" height="30"></td>
          <td align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="20" height="30"></td>
          <td align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="20" height="30"></td>
        </tr>
        <tr>
          <td width="40"><img src="../../common/pc/image/spacer.gif" width="40" height="40"></td>
          <td width="363" align="left" valign="top">
            <table width="363" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="10" height="10" valign="bottom"><img src="../../common/pc/image/LogIn_r1.gif" width="10" height="10"></td>
                <td width="343" height="10" valign="bottom" bgcolor="#000066"><img src="../../common/pc/image/spacer.gif" width="343" height="10"></td>
                <td width="10" height="10" valign="bottom"><img src="../../common/pc/image/LogIn_r2.gif" width="10" height="10"></td>
              </tr>
              <tr align="left" valign="top">
                <td width="10" height="246" bgcolor="#000066"><img src="../../common/pc/image/spacer.gif" width="10" height="246"></td>
                <td height="246" bgcolor="#000066">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="300"><img src="../../common/pc/image/kochira.gif" width="226" height="25"></td>
                      <td rowspan="2" align="center"><img src="../../common/pc/image/kazari.gif" width="23" height="53"></td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                    </tr>
                  </table>
                  <table width="343" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td colspan="3"><img src="../../common/pc/image/spacer.gif" width="100" height="18"></td>
                    </tr>
                    <tr>
                      <td width="130" align="right" valign="middle" nowrap class="size12"><font color="#FFFFFF">個人ユーザー名:</font></td>
                      <td width="6"><img src="../../common/pc/image/spacer.gif" width="6" height="12"></td>
                      <td><input name="LoginId" id="LoginId" type="text" size="20" maxlength="20" class="nyuryoku" <% if (hotelid.equals("demo")&&!DebugMode){%>value="demo"<%}%>></td>
                    </tr>
                    <tr>
                      <td colspan="3"><img src="../../common/pc/image/spacer.gif" width="100" height="15"></td>
                    </tr>
                    <tr>
                      <td width="130" align="right" valign="middle" nowrap class="size12"><font color="#FFFFFF">個人パスワード:</font></td>
                      <td><img src="../../common/pc/image/spacer.gif" width="6" height="12"></td>
                      <td><INPUT id="pwd" name="Password" type="password" size="20" maxlength="<%= Constants.MAXIMUM_PASSWORD_LENGTH %>" class="nyuryoku" <% if (hotelid.equals("demo")&&!DebugMode){%>value="1111"<%}%>>
    				  	<a id="showPwd" style="height: 18px; color: #FFFFFF; font-size: 12px; cursor: pointer;-ms-user-select: none; -webkit-user-select: none; -moz-user-select: none;">表示</a>
    				  </td>
                    </tr>
                    <tr>
                      <td colspan="3"><img src="../../common/pc/image/spacer.gif" width="100" height="20"></td>
                    </tr>
                    <tr>
                      <td width="130">&nbsp;</td>
                      <td><img src="../../common/pc/image/spacer.gif" width="6" height="12"></td>
                     <td><INPUT name="login" type="image" id="login" onClick="return datacheck()" src="../../common/pc/image/login_btn.gif" alt="ログイン" width="172" height="40" border="0" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('login','','../../common/pc/image/login_btn_o.gif',1)" class="btn"></td>
                    </tr>
<% if (hotelid.equals("demo")&&!DebugMode){%>
                    <tr align="left">
                      <td width="130" align="right" valign="middle" nowrap class="size12"></td>
                      <td><img src="../../common/pc/image/spacer.gif" width="6" height="12"></td>
                      <td><font color="#FFFFFF">そのまま「ログイン」を<br>クリックしてください。</font></td>
                    </tr>
<%}%>
                    <tr align="left">
                      <td class="size12" colspan=3 align="right" valign="middle"><font color="#FFFFFF">個人パスワードを忘れた方は</font><a href="javascript:;" class="size12" onClick="MM_openBrWindow('passwd_reminder.jsp','パスワードを忘れた方','scrollbars=yes,width=420,height=300')"><font color="#FFFFFF"><strong>こちら</strong></font></a></td>
                    </tr>
                    <tr>
                      <td colspan="3"><img src="../../common/pc/image/spacer.gif" width="100" height="8"></td>
                    </tr>
<%
    String paramURL = new String(request.getRequestURL());
    if (request.getHeader("user-agent").indexOf("Chrome") == -1 && paramURL.indexOf("https://") != -1)
    {
%>                  <tr>
                      <td class="size12" colspan=3 align="right" valign="middle">
						<a href="<%=paramURL.replace("https://","http://").replace("pc/top.jsp","pc/index.html")%>"><font color="#FFFFFF">ログインできない方はこちら(古いブラウザ使用の方）</a></font>
					  </td>
                    </tr>
                     <tr>
                      <td colspan="3"><img src="../../common/pc/image/spacer.gif" width="100" height="8"></td>
                     </tr>
<%
    }
%>
                  </table>
                </td>
                <td width="10" height="246" bgcolor="#000066"><img src="../../common/pc/image/spacer.gif" width="10" height="246"></td>
              </tr>
              <tr>
                <td width="10" height="10"><img src="../../common/pc/image/LogIn_r3.gif" width="10" height="10"></td>
                <td height="10" bgcolor="#000066"><img src="../../common/pc/image/spacer.gif" width="10" height="10"></td>
                <td width="10" height="10"><img src="../../common/pc/image/LogIn_r4.gif" width="10" height="10"></td>
              </tr>
              <tr valign="middle">
                  <td colspan="3" align="center"><img src="../../common/pc/image/spacer.gif" width="100" height="30"></td>
              </tr>
              <tr>
                <td width="10" height="10" valign="bottom"><img src="../../common/pc/image/LogIn_r1.gif" width="10" height="10"></td>
                <td width="343" height="10" valign="bottom" bgcolor="#000066"><img src="../../common/pc/image/spacer.gif" width="343" height="10"></td>
                <td width="10" height="10" valign="bottom"><img src="../../common/pc/image/LogIn_r2.gif" width="10" height="10"></td>
              </tr>
<% if (!hotelid.equals("demo") || DebugMode){%>
              <tr align="left" valign="top">
                <td width="10" height="50" bgcolor="#000066"><img src="../../common/pc/image/spacer.gif" width="10" height="50"></td>
                <td height="50" bgcolor="#000066">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td colspan="3"><img src="../../common/pc/image/spacer.gif" width="100" height="20"></td>
                    </tr>
                    <tr>
                      <td width="30">&nbsp;</td>
                      <td class="size12">
					  <a href="getfiles.jsp?fname=kiyaku_hotenavi_20211101.pdf" target="_blank" class="size12" ><font color="#FFFFFF">ホテナビ規約 2021年11月1日施行</font></a></td>
                      <td class="size12"><font color="#FFFFFF">(2021.10.15)</font></td>
                    </tr>
                    <tr>
                      <td width="30">&nbsp;</td>
                      <td class="size12">
					  <a href="getfiles.jsp?fname=manual_hotenavi_20101116.pdf" target="_blank" class="size12" ><font color="#FFFFFF">マニュアルダウンロード[Ver1.50]</font></a></td>
                      <td class="size12"><font color="#FFFFFF">(2010.11.16)</font></td>
                    </tr>
					<tr>
                      <td width="30">&nbsp;</td>
                      <td class="size12">
					  <a href="javascript:;" class="size12" onClick="MM_openBrWindow('../../common/pc/owner_faq.html','FAQ','scrollbars=yes,width=700,height=700')">
<strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">オーナーサイト関連Q＆A</strong>
</a><br>
					  </td>
                      <td class="size12"><font color="#FFFFFF">(2010.11.16)</font></td>
                    </tr>
                    <tr>
                      <td colspan="3"><img src="../../common/pc/image/spacer.gif" width="100" height="8"></td>
                    </tr>
                  </table>
                </td>
                <td width="10" height="50" bgcolor="#000066"><img src="../../common/pc/image/spacer.gif" width="10" height="30"></td>
              </tr>
<%}%>
              <tr>
                <td width="10" height="10"><img src="../../common/pc/image/LogIn_r3.gif" width="10" height="10"></td>
                <td height="10" bgcolor="#000066"><img src="../../common/pc/image/spacer.gif" width="10" height="10"></td>
                <td width="10" height="10"><img src="../../common/pc/image/LogIn_r4.gif" width="10" height="10"></td>
              </tr>
            </table>
          </td>

<jsp:include page="information_owner.jsp" />

          <td align="left" valign="top">&nbsp;</td>
        </tr>
        <tr valign="middle">
          <td colspan="5" align="center"><img src="../../common/pc/image/spacer.gif" width="100" height="18"></td>
        </tr>
        <tr valign="middle">
          <td>&nbsp;</td>
          <td align="center" class="size10" colspan=3><img src="../../common/pc/image/newimedia.gif" align="absmiddle" border="0"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyrigtht&copy;  almex inc . All Rights Reserved.</td>
          <td>&nbsp;</td>
        </tr>
        <tr valign="middle">
          <td colspan="5" align="center" class="size10"><img src="../../common/pc/image/spacer.gif" width="100" height="12"></td>
        </tr>
      </table>
    </td>
    <td>&nbsp;</td>
  </tr>
</table>
</FORM>
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-209661508-1"></script>
<script type="text/javascript">
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'UA-209661508-1');

	$(function () {
		$("#showPwd").click(function () {
			var inputID = "pwd";
			var input = $("#" + inputID);
			var showFlg = ("password" == input.attr("type").toLowerCase());
			var inputJsonData = {
				id: inputID,
				name: "Password",
				cssClass: "nyuryoku"
			};
			var showText = "";
			if (showFlg) {
				inputJsonData.type = "text";
				inputJsonData.autocomplete = null;
				showText = "隠す";
			} else {
				inputJsonData.type = "password";
				inputJsonData.autocomplete = "new-password";
				showText = "表示";
			}
			$(this).text(showText);
			changeInputType(inputJsonData);
		});
	});
	function changeInputType(inputJsonData) {
		var input = $("#" + inputJsonData.id);
		$(input).replaceWith($("<input />").val(input.val())
			.attr({
				id: inputJsonData.id,
				name: inputJsonData.name,
				type: inputJsonData.type,
				size: input.attr("size"),
				maxlength: input.attr("maxlength"),
				autocomplete: inputJsonData.autocomplete // パスワードフィールドのオートフィルを抑止
			}).addClass(inputJsonData.cssClass)
		);
	}
</script>
</BODY>
</html>
<%
    session.setAttribute("LoginHotelId", hotelid);
    ownerinfo.HotelId     = hotelid;
%>
