<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page isErrorPage="true" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>error</title>
<script type="text/javascript">
function hideAdBar(){
setTimeout("scrollTo(0,1)", 100);
if (window.orientation ==0){document.body.className="portrait";}else{document.body.className="landscape";}
}
</script>
<link rel="stylesheet" type="text/css" href="../../common/smart/iphone_index.css">
</head>
<body class="portrait" text="#555555" onLoad="hideAdBar()" onOrientationChange="hideAdBar();">

<jsp:include page="header.jsp" flush="true" />
���V�X�e�������e�i���X�̂��m�点��<br>
<hr class="border">
���f���A�T�[�r�X�������p���������A���ɂ��肪�Ƃ��������܂��B<br>
�l�b�g���[�N�ƃT�[�o�̐������s�����߁A�����e�i���X�����{�������܂��B<br>
��Ǝ��ԑт́A�n�b�s�[�E�z�e���y�уz�e�i�r�̑S�ẴT�[�r�X�������p�ł��Ȃ��Ȃ�܂��B<br>
�����f�����������܂����A�����������̂قǂ�낵�����肢�\���グ�܂��B<br>
<br>
�y�����e�i���X���{���ԁz<br>
����23�N6��8��(��)<br>
�ߑO1:00�`�ߑO9:00<br>
<br>
������� �A�����b�N�X <br>
<hr class="border">
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
