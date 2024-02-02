<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
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
    String selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    String param_store = request.getParameter("Store");
    if( param_store != null )
    {
        selecthotel = param_store;
    }

    // セッション属性に選択ホテルをセットする
    session.setAttribute("SelectHotel", selecthotel);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>マネージアイ</title>
<link href="style/contents.css" rel="stylesheet" type="text/css">
<link href="style/access.css" rel="stylesheet" type="text/css">
<link href="style/room.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('image/yajirushiGrey_f2.gif')">
<%
    if(selecthotel.compareTo("fine001") == 0 )
    {
%>
<DIV ALIGN=CENTER>
	<APPLET
  	//CODEBASE = "http://210.150.102.106:8013/kcCamRot"
  	ARCHIVE  = "kcrot3.jar"
  	CODE     = "kcCamRotApplet.class"
  	NAME     = "CamRotApplet"
  	WIDTH    = 800  	HEIGHT   = 600  	HSPACE   = 0
  	VSPACE   = 0
  	ALIGN    = middle
	>
	<PARAM name = "SERVER" value = "210.150.102.106:8013">	
	<PARAM name = "ID" value = "1">
	<PARAM name = "DEBUG" value = "R">
	<PARAM name = "NX" value = "2">
	<PARAM name = "NY" value = "2">
	<PARAM name = "SLICE" value = "3">
	<PARAM name = "CAMS1" value = "1,-1">
<PARAM name = "CAMS2" value = "2,-1">
<PARAM name = "CAMS3" value = "3,-1">
<PARAM name = "CAMS4" value = "4,-1">
	</APPLET>
</DIV>
<%
	}
    else if(selecthotel.compareTo("fine002") == 0 )
    {
%>
<DIV ALIGN=CENTER>
	<APPLET
  	//CODEBASE = "http://202.229.225.1:8013/kcCamRot"
  	ARCHIVE  = "kcrot3.jar"
  	CODE     = "kcCamRotApplet.class"
  	NAME     = "CamRotApplet"
  	WIDTH    = 800  	HEIGHT   = 600  	HSPACE   = 0
  	VSPACE   = 0
  	ALIGN    = middle
	>
	<PARAM name = "SERVER" value = "202.229.225.1:8013">	
	<PARAM name = "ID" value = "1">
	<PARAM name = "DEBUG" value = "R">
	<PARAM name = "NX" value = "2">
	<PARAM name = "NY" value = "2">
	<PARAM name = "SLICE" value = "3">
	<PARAM name = "CAMS1" value = "1,-1">
<PARAM name = "CAMS2" value = "2,-1">
<PARAM name = "CAMS3" value = "3,-1">
<PARAM name = "CAMS4" value = "4,-1">
	</APPLET>
</DIV>
<%
	}
    else if(selecthotel.compareTo("fine005") == 0 )
    {
%>
<DIV ALIGN=CENTER>
	<APPLET
  	//CODEBASE = "http://61.194.125.50:8013/kcCamRot"
  	ARCHIVE  = "kcrot3.jar"
  	CODE     = "kcCamRotApplet.class"
  	NAME     = "CamRotApplet"
  	WIDTH    = 800  	HEIGHT   = 600  	HSPACE   = 0
  	VSPACE   = 0
  	ALIGN    = middle
	>
	<PARAM name = "SERVER" value = "61.194.125.50:8013">	
	<PARAM name = "ID" value = "1">
	<PARAM name = "DEBUG" value = "R">
	<PARAM name = "NX" value = "2">
	<PARAM name = "NY" value = "2">
	<PARAM name = "SLICE" value = "3">
	<PARAM name = "CAMS1" value = "1,-1">
<PARAM name = "CAMS2" value = "2,-1">
<PARAM name = "CAMS3" value = "3,-1">
<PARAM name = "CAMS4" value = "4,-1">
	</APPLET>
</DIV>
<%
	}
    else if(selecthotel.compareTo("fine011") == 0 )
    {
%>
<DIV ALIGN=CENTER>
	<APPLET
  	//CODEBASE = "http://202.229.225.2:8014/kcCamRot"
  	ARCHIVE  = "kcrot3.jar"
  	CODE     = "kcCamRotApplet.class"
  	NAME     = "CamRotApplet"
  	WIDTH    = 800  	HEIGHT   = 600  	HSPACE   = 0
  	VSPACE   = 0
  	ALIGN    = middle
	>
	<PARAM name = "SERVER" value = "202.229.225.2:8014">	
	<PARAM name = "ID" value = "1">
	<PARAM name = "DEBUG" value = "R">
	<PARAM name = "NX" value = "2">
	<PARAM name = "NY" value = "2">
	<PARAM name = "SLICE" value = "3">
	<PARAM name = "CAMS1" value = "1,-1">
<PARAM name = "CAMS2" value = "2,-1">
<PARAM name = "CAMS3" value = "3,-1">
<PARAM name = "CAMS4" value = "4,-1">
	</APPLET>
</DIV>
<%
	}
    else if(selecthotel.compareTo("fine012") == 0 )
    {
%>
<DIV ALIGN=CENTER>
	<APPLET
  	//CODEBASE = "http://202.229.253.170:8013/kcCamRot"
  	ARCHIVE  = "kcrot3.jar"
  	CODE     = "kcCamRotApplet.class"
  	NAME     = "CamRotApplet"
  	WIDTH    = 800  	HEIGHT   = 600  	HSPACE   = 0
  	VSPACE   = 0
  	ALIGN    = middle
	>
	<PARAM name = "SERVER" value = "202.229.253.170:8013">	
	<PARAM name = "ID" value = "1">
	<PARAM name = "DEBUG" value = "R">
	<PARAM name = "NX" value = "2">
	<PARAM name = "NY" value = "2">
	<PARAM name = "SLICE" value = "3">
	<PARAM name = "CAMS1" value = "1,-1">
<PARAM name = "CAMS2" value = "2,-1">
<PARAM name = "CAMS3" value = "3,-1">
<PARAM name = "CAMS4" value = "4,-1">
	</APPLET>
</DIV>
<%
	}
    else if(selecthotel.compareTo("fine013") == 0 )
    {
%>
<DIV ALIGN=CENTER>
	<APPLET
  	//CODEBASE = "http://202.229.225.10:8013/kcCamRot"
  	ARCHIVE  = "kcrot3.jar"
  	CODE     = "kcCamRotApplet.class"
  	NAME     = "CamRotApplet"
  	WIDTH    = 800  	HEIGHT   = 600  	HSPACE   = 0
  	VSPACE   = 0
  	ALIGN    = middle
	>
	<PARAM name = "SERVER" value = "202.229.225.10:8013">	
	<PARAM name = "ID" value = "1">
	<PARAM name = "DEBUG" value = "R">
	<PARAM name = "NX" value = "2">
	<PARAM name = "NY" value = "2">
	<PARAM name = "SLICE" value = "3">
	<PARAM name = "CAMS1" value = "1,-1">
<PARAM name = "CAMS2" value = "2,-1">
<PARAM name = "CAMS3" value = "3,-1">
<PARAM name = "CAMS4" value = "4,-1">
	</APPLET>
</DIV>
<%
	}
    else if(selecthotel.compareTo("olive01") == 0 )
    {
%>
<DIV ALIGN=CENTER>
	<APPLET
  	//CODEBASE = "http://61.197.151.171:8013/kcCamRot"
  	ARCHIVE  = "kcrot3.jar"
  	CODE     = "kcCamRotApplet.class"
  	NAME     = "CamRotApplet"
  	WIDTH    = 800  	HEIGHT   = 600  	HSPACE   = 0
  	VSPACE   = 0
  	ALIGN    = middle
	>
	<PARAM name = "SERVER" value = "61.197.151.171:8013">	
	<PARAM name = "ID" value = "1">
	<PARAM name = "DEBUG" value = "R">
	<PARAM name = "NX" value = "2">
	<PARAM name = "NY" value = "2">
	<PARAM name = "SLICE" value = "3">
	<PARAM name = "CAMS1" value = "1,-1">
<PARAM name = "CAMS2" value = "2,-1">
<PARAM name = "CAMS3" value = "3,-1">
<PARAM name = "CAMS4" value = "4,-1">
	</APPLET>
</DIV>
<%
	}
    else if(selecthotel.compareTo("olive02") == 0 )
    {
%>
<DIV ALIGN=CENTER>
	<APPLET
  	//CODEBASE = "http://61.194.124.1:8013/kcCamRot"
  	ARCHIVE  = "kcrot3.jar"
  	CODE     = "kcCamRotApplet.class"
  	NAME     = "CamRotApplet"
  	WIDTH    = 800  	HEIGHT   = 600  	HSPACE   = 0
  	VSPACE   = 0
  	ALIGN    = middle
	>
	<PARAM name = "SERVER" value = "61.194.124.1:8013">	
	<PARAM name = "ID" value = "1">
	<PARAM name = "DEBUG" value = "R">
	<PARAM name = "NX" value = "2">
	<PARAM name = "NY" value = "2">
	<PARAM name = "SLICE" value = "3">
	<PARAM name = "CAMS1" value = "1,-1">
<PARAM name = "CAMS2" value = "2,-1">
<PARAM name = "CAMS3" value = "3,-1">
<PARAM name = "CAMS4" value = "4,-1">
	</APPLET>
</DIV>
<%
	}
    else if(selecthotel.compareTo("fine014-1") == 0 )
    {
%>
<DIV ALIGN=CENTER>
<iframe name="recommend" src="http://122.210.130.193:8013/dvr/monitor/index.php" scrolling="no" frameborder="0" width="760" height="800"></iframe>
</DIV>
<%
	}
    else if(selecthotel.compareTo("fine014-2") == 0 )
    {
%>
<DIV ALIGN=CENTER>
<iframe name="recommend" src="http://122.210.130.203:8013/dvr/monitor/index.php" scrolling="no" frameborder="0" width="760" height="800"></iframe>
</DIV>
<%
	}
    else if(selecthotel.compareTo("fine016") == 0 )
    {
%>
<DIV ALIGN=CENTER>
<iframe name="recommend" src="http://122.215.127.225:8013/dvr/monitor/index.php" scrolling="no" frameborder="0" width="760" height="800"></iframe>
</DIV>
<%
	}
%>
</body>
</html>
