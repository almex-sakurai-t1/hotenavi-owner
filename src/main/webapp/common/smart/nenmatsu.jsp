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
�N���N�n�x�Ƃ̂��m�点<br>
<hr class="border">
���L�̊��Ԥ�N���N�n�x�ɂ̂��ߋƖ������x�݂����Ă��������܂��<br>
�Ȃ���ً}����ݽ�̍ۂ́A�㗝�X�̊�����б�ү���̊e�x�X�S���ɂ��A�����������܂��悤���肢�\���グ�܂��<br>
<br>
��ϋ��k�ł������܂�������������������܂��悤���肢�\���グ�܂�� <br>
<br>
�y�x�Ɗ��ԁz<br>
����17�N12��30��(��)~<br>
����18�N1��4��(��)<br>
<br>
������б���ި�
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
