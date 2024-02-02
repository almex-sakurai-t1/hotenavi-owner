<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.Constants" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String requestUri = request.getRequestURI();
    boolean DebugMode = false;
    if (requestUri.indexOf("_debug_") != -1)
    {
       DebugMode = true;
    }
    String hotelid = requestUri.replace("_debug_","");
    hotelid = hotelid.replace("smart/index.jsp","");
    hotelid = hotelid.replace("smart/top.jsp","");
    hotelid = hotelid.replace("/","");
    if (hotelid.indexOf(";jsessionid") != -1)
    {
        hotelid = hotelid.substring(0,hotelid.indexOf(";jsessionid"));
    }

%>
<%@ include file="CheckCookie.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<title>��Ű���۸޲�</title>
<script type="text/javascript">
function hideAdBar(){
setTimeout("scrollTo(0,1)", 100);
if (window.orientation ==0){document.body.className="portrait";}else{document.body.className="landscape";}
}

	function showPwd(pwdElementID, o) {
		var input = jQuery("#" + pwdElementID);
		var showFlg = ("password" == input.attr("type").toLowerCase());
		var inputJsonData = {
			id: pwdElementID,
			name: input.attr("name"),
			style: input.attr("style"),
			cssClass: "nyuryoku"
		};
		var btnVal = "";
		if (showFlg) {
			inputJsonData.type = "text";
			inputJsonData.autocomplete = null;
			btnVal = "�B��";
		} else {
			inputJsonData.type = "password";
			inputJsonData.autocomplete = "new-password";
			btnVal = "�\��";
		}
		jQuery(o).val(btnVal);
		changeInputType(inputJsonData);
	}
	function changeInputType(inputJsonData) {
		var input = $("#" + inputJsonData.id);
		$(input).replaceWith($("<input />").val(input.val())
			.attr({
				name: inputJsonData.name,
				id: inputJsonData.id,
				type: inputJsonData.type,
				style: inputJsonData.style,
				size: input.attr("size"),
				maxlength: input.attr("maxlength"),
				autocomplete: inputJsonData.autocomplete // �p�X���[�h�t�B�[���h�̃I�[�g�t�B����}�~
			}).addClass(inputJsonData.cssClass)
		);
	}
</script>
<link rel="stylesheet" type="text/css" href="../../common/smart/iphone_index.css"></head>

<body class="portrait" text="#555555" onLoad="hideAdBar()" onOrientationChange="hideAdBar();">

<jsp:include page="header.jsp" flush="true" />
<!-- #include virtual="/mainte.html" --> 

<h1 class="border"><%= StringEscapeUtils.escapeHtml4(hotelid) %><br>�I�[�i�[�T�C�g���O�C��</h1>

<div class="form" align="center">
<form action="<%= response.encodeURL("login.jsp") %>" method="post">
<%
    if (hotelid.equals("demo")&&!DebugMode)
    {
%>
<input name="nid"     type="hidden" value="net">
<INPUT name="npasswd" type="hidden" value="1111">
�l�b�g���[�N���[�U�[��:<br>
<input type="text"     id="text"  size="20" maxlength="10" value="xxxxx"><br>
�l�b�g���[�N�p�X���[�h:<br>
<INPUT type="password" id="text2" style="width: 155px; margin-right: -4px;" size="20" maxlength="20" class="nyuryoku" value="1111">
<input type="button" value="�\��" style="vertical-align: -0.5em;" onclick="showPwd('text2', this)"/><br>
<input type="checkbox" name="nidsave" value="1" <%if(loginNidSave.equals("1")){%>checked<%}%>/>�ۑ�����<br><br>
<%
    }
    else if(!loginNidSave.equals("1") || !loginHid.equals(hotelid))
    {
%>
�l�b�g���[�N���[�U�[��:<br>
<input name="nid" id="text" type="text" size="20" maxlength="10" autocomplete=off value=""><br>
�l�b�g���[�N�p�X���[�h:<br>
<INPUT name="npasswd" id="text2" style="width: 155px; margin-right: -4px;" type="password" style="width: 155px;" size="20" maxlength="20" class="nyuryoku"  autocomplete="new-password" value="">
<input type="button" value="�\��" style="vertical-align: -0.5em;" onclick="showPwd('text2', this)"/><br>
<input type="checkbox" name="nidsave" value="1"/>�ۑ�����<br><br>
<%
    }
%>
�l���[�U�[��:<br>
<%
    if (hotelid.equals("demo")&&!DebugMode)
    {
%>
<input name="LoginId" id="text_user" type="text" size="20" maxlength="20" value="demo" autocomplete=off value=""><br>
<input type="checkbox" name="lidsave" value="1" <%if(loginLidSave.equals("1")){%>checked<%}%>/>�ۑ�����<br><br>
<%
    }
    else if(loginLidSave.equals("1") && loginHid.equals(hotelid))
    {
%>
<input name="LoginId" id="text_user" type="text" size="20" maxlength="20" value="<%= loginLid %>" autocomplete=off><br>
<%
    }
    else
    {
%>
<input name="LoginId" id="text_user" type="text" size="20" maxlength="20" <% if (hotelid.equals("demo")&&!DebugMode){%>value="demo"<%}else{%>value=""<%}%>  autocomplete=off><br>
<input type="checkbox" name="lidsave" value="1" />�ۑ�����<br><br>
<%
    }
%>
�l�p�X���[�h:<br>
<INPUT name="Password" id="text2_user" type="password" style="width: 155px;" size="20" maxlength="<%= Constants.MAXIMUM_PASSWORD_LENGTH %>" class="nyuryoku" <% if (hotelid.equals("demo")&&!DebugMode){%>value="1111"<%}%> autocomplete="new-password"><input type="button" value="�\��" style="vertical-align: -0.5em;" onclick="showPwd('text2_user', this)"/><br>
<input type="submit" value="���O�C��" id="button">
<%
    if (hotelid.equals("demo")&&!DebugMode)
    {
%>
<div align="left">
<font size="3">
�p�X���[�h��ύX�����ɂ��̂܂܃��O�C�����N���b�N���Ă��������B<br>
�ʏ�u�p�X���[�h��ۑ�����v�Ƀ`�F�b�N����Ǝ�������͂��ȗ�����܂��B<br>
</font>
</div>
<%
    }
%>
</form>
</div>

<hr class="border">
<ul class="link">
<li><a href="passwdreminder.jsp"><span class="title">�p�X���[�h��Y�ꂽ��</span></a></li>
<center>
<!--
<a href="<%= response.encodeURL("../smartpc/ownerindex.jsp") %>">PC�ł�</a>
-->
</center>
</ul>
<!--
<hr class="border">
-->
<%
    if (loginHid.equals(hotelid))
    {
%>
<ul class="link">
<li><a href="logout.jsp"><span class="title">���S�Ƀ��O�A�E�g����</span></a></li>
</ul>
<%
    }
%>
<div align="center" class="copyright">
Copyright(C) almex inc. All rights reserved.
</div>
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-209661508-1"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'UA-209661508-1');
</script>
</body>
</html>
