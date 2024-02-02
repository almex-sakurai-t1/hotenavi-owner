<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="java.util.regex.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String selecthotel = request.getParameter("HotelId");
    if(CheckString.isValidParameter(selecthotel) && !CheckString.numAlphaCheck(selecthotel))
    {
        response.sendError(400);
        return;
    }
    if    (selecthotel == null) selecthotel = "";

    // PC�E�g�с@���
    String Type = request.getParameter("Type");
    if (Type == null) { Type = "pc"; }
    if(CheckString.isValidParameter(Type) && !CheckString.numAlphaCheck(Type))
    {
        response.sendError(400);
        return;
    }

    String Target = request.getParameter("Target");
    if(CheckString.isValidParameter(Target) && !CheckString.numAlphaCheck(Target))
    {
        response.sendError(400);
        return;
    }
    if (Target == null) { Target = "event_list"; }
    
    /*
    	�u>>PC��HP�ҏW�v���N���b�N�������_�ŁA���������͐V�z�e�i�r�ғ��J�n���t�ȍ~�̏ꍇ�AHTML�w�b�_���Ȃǂ��擾
    	20180508�@enn-k1�@�ǉ�
    */
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

	StringBuilder sqlSb = null;
	String htmlHead = null;
	boolean useNewHotenaviFlg = false;
	
	Pattern pTag = null;
	Matcher mTag = null;
	Pattern pParam = null;
	Matcher mParam = null;

	String rowHTMLTagStr = null;
	String source = null;
	String replaceHeadStr = null;
	StringBuilder sb = new StringBuilder();
	
	try {
		sqlSb = new StringBuilder();
    	sqlSb.append("SELECT * FROM hotel_element");
		sqlSb.append(" WHERE hotel_id = ?");
		sqlSb.append(" AND START_DATE <= DATE_FORMAT(NOW(), '%Y%m%d')");
		connection  = DBConnection.getConnection();
		prestate    = connection.prepareStatement(sqlSb.toString());
	    prestate.setString( 1, selecthotel);
	    result      = prestate.executeQuery();
		if( useNewHotenaviFlg = result.next() ) {
			htmlHead = result.getString("html_head");
	    	if (htmlHead != null && !"".equals(htmlHead.trim())) {
	    		// �O��CSS(link)�ƊO��js(script)�̃^�O���i�荞��ł���
				for (String tagRegex : new String[] { "<link(.*?)>", "<script(.*?)script>" }) {
					pTag = Pattern.compile(tagRegex, Pattern.CASE_INSENSITIVE);
					mTag = pTag.matcher(htmlHead);
					while (mTag.find()) {
						rowHTMLTagStr = mTag.group();
						for (String paramRegex : new String[] { "<link(.*?)href=\"(.*?)\"(.*?)>", "<script(.*?)src=\"(.*?)\"(.*?)>" }) {
							pParam = Pattern.compile(paramRegex, Pattern.CASE_INSENSITIVE);
							mParam = pParam.matcher(rowHTMLTagStr);
							if (mParam.find()) {
								// script��src�܂���link��href�̒��g���擾
								source = mParam.group(2);
								replaceHeadStr = "";
								
								// �z�e�i�r�̃��[�U�T�C�g�̎d�l���g�����߁A�������́uhttps://www.hotenavi.com�v�ł͂Ȃ��ꍇ
								if (!source.startsWith("https://www.hotenavi.com")) {
									replaceHeadStr = "https://www.hotenavi.com";
									if (!source.startsWith("/") && !source.startsWith("\\")) {
										replaceHeadStr += "/";
									}
								}
								
								rowHTMLTagStr = rowHTMLTagStr.replace(source, replaceHeadStr + source);
								break;
							}
						}
						sb.append(rowHTMLTagStr);
					}
				}
	    	}
	    	session.setAttribute("htmlHead", sb.toString());
		}
	    session.setAttribute("useNewHotenaviFlg", useNewHotenaviFlg);
	} catch(Exception e) {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
	} finally {
	    DBConnection.releaseResources(result);
	    DBConnection.releaseResources(prestate);
		sqlSb = null;
		htmlHead = null;
		
		pTag = null;
		mTag = null;
		pParam = null;
		mParam = null;

		rowHTMLTagStr = null;
		source = null;
		replaceHeadStr = null;
		sb = null;
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>HP�ҏW</title>
</head>

<frameset cols="174,*" frameborder="no" border="0" framespacing="0">
  <frame src="hpedit_menu.jsp?Type=<%= Type %>"     name="menu" frameborder="no" scrolling="auto" noresize marginwidth="0" marginheight="0" id="menu">
<%
    if (selecthotel.compareTo("all") == 0)
    {
        if(Type.compareTo("pc") == 0)
        {
%>
  <frame src="event_list_all.jsp?DataType=5&DispType=0&SortType=0" name="contents" frameborder="no" scrolling="auto" noresize marginwidth="0" marginheight="0" id="contents">
<%
        }
        else
        {
%>
  <frame src="event_list_all.jsp?DataType=6&DispType=0&SortType=0" name="contents" frameborder="no" scrolling="auto" noresize marginwidth="0" marginheight="0" id="contents">
<%
        }
    }
    else
    {
%>
  <frame src="<%=Target%>.jsp?Type=<%= Type %>&DispType=0" name="contents" frameborder="no" scrolling="auto" noresize marginwidth="0" marginheight="0" id="contents">
<%
    }
%>
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>
