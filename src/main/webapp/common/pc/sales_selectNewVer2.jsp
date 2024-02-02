<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");
    String     session_selecthotel = (String)session.getAttribute("SelectHotel");
    String     selecthotel   = "";
    selecthotel = request.getParameter("Store");
    if  (selecthotel == null)
    {
        selecthotel = (String)session.getAttribute("SelectHotel");
    }
    if  (selecthotel == null || selecthotel.equals(""))
    {
        selecthotel = "all";
    }
   if( !CheckString.hotenaviIdCheck(selecthotel) )
    {
           response.sendError(400);
            return;
    }
    int storecount = Integer.parseInt((String)session.getAttribute("StoreCount"));

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    //  店舗数のチェック
    try
    {
    	final String query = "SELECT count(*) FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?"
					         + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid"
					         + " AND owner_user_hotel.userid = ?"
					         + " AND hotel.plan <= 2"
					         + " AND hotel.plan >= 1";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            storecount   = result.getInt(1);
        }
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }

    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }

    String paramKind   = request.getParameter("Kind");
    if (paramKind == null)
    {
        paramKind = "DATE";
    }
    String dateRange = request.getParameter("Range");
    if (dateRange==null)
    {
        dateRange = "0";
    }

    boolean SubmitOK = true;
    String paramSubmitOK = request.getParameter("SubmitOK");
    if ( paramSubmitOK == null)
    {
         paramSubmitOK = "true";
    }
    if  ( paramSubmitOK.equals("false"))
    {
        SubmitOK = false;
    }
    if ((dateRange.equals("0") || dateRange.equals("1")) && paramKind.indexOf("RANGE") == 0)
    {
       SubmitOK = false;
    }

    boolean CompareMode = true;
    String paramCompare = request.getParameter("Compare");
    if (paramCompare == null)
    {
        paramCompare = "false";
    }
    if  (paramCompare.equals("false"))
    {
        CompareMode = false;
    }
    boolean CompareSubmit = true;
    String paramCompareSubmit = request.getParameter("CompareSubmit");
    if (paramCompareSubmit == null)
    {
        paramCompareSubmit = "true";
    }
    if  (paramCompareSubmit.equals("false"))
    {
        CompareSubmit = false;
    }

    if (!CompareSubmit)
    {
       SubmitOK = false;
    }
    if (session_selecthotel==null)
    {
       if (storecount != 1)
       {
           SubmitOK = false;
       }
    }
    else if (session_selecthotel.equals(""))
    {
       SubmitOK = false;
    }
    String[] params = new String[]{paramKind, dateRange, paramSubmitOK, paramCompare, paramCompareSubmit };
    for( String param : params )
    {
        if(CheckString.isValidParameter(param) && !CheckString.numAlphaCheck(param))
        {
            response.sendError(400);
            return;
        }
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<title>売上日付選択</title>
<script type="text/javascript" src="../../common/pc/scripts/sales_datacheck.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/date_check.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/date_range_check.js"></script>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="<%if (SubmitOK){%>document.selectday.submit();<%}else if (session_selecthotel==null){%>top.Main.mainFrame.location.href='sales_target_edit.jsp';<%}else{%>top.Main.mainFrame.location.href='sales_page.html';<%}%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <table  width="100%" border="0" cellpadding="0" cellspacing="0">
          <!-- 店舗選択表示 -->
          <jsp:include page="../../common/pc/selectstoreSales.jsp" flush="true">
             <jsp:param name="StoreCount" value="<%=storecount%>" />
          </jsp:include>
          <!-- 店舗選択表示ここまで -->
<%
    if (paramKind.equals("DATE"))
    {
%>          <!-- 日計選択 -->
          <jsp:include page="sales_selectdayNew.jsp" flush="true" />
          <!-- 日計選択ここまで -->
<%
    }
    else if (paramKind.equals("MONTH") || paramKind.equals("MONTHLIST"))
    {
%>          <!-- 月計選択 -->
          <jsp:include page="sales_selectmonthNew.jsp" flush="true" />
          <!-- 月計選択ここまで -->
<%
    }
    else if (paramKind.equals("RANGE")||paramKind.equals("RANGELIST"))
    {
%>          <!-- 期間指定選択 -->
          <jsp:include page="sales_selectrangeNew.jsp" flush="true" />
          <!-- 期間指定選択ここまで -->
<%
    }
    else if (paramKind.equals("YEARLIST"))
    {
%>          <!-- 年次月別売上 -->
          <jsp:include page="sales_selectyear.jsp" flush="true" />
          <!-- 年次月別売上ここまで -->
<%
    }
    else if (paramKind.equals("CALENDAR"))
    {
%>          <!-- カレンダー -->
          <jsp:include page="sales_calendar.jsp" flush="true" />
          <!-- カレンダーここまで -->
<%
    }
%>      </table>
    </td>
  </tr>
</table>
</body>
</html>
