<%@ page contentType="text/html; charset=Windows-31J"%><%@ page import="com.hotenavi2.mailmagazine.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../csrf/refererCheck.jsp" %><%@ include file="../csrf/checkCsrfForPost.jsp" %>
<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    // �z�e��ID�擾
    String selecthotel = (String)session.getAttribute("SelectHotel");
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
%>
<%=selecthotel %><br>
<%=ownerinfo.HotelId %><br>
<%=loginHotelId %><br>

<%
    if  (selecthotel.equals("all") )
    {
        selecthotel = ownerinfo.HotelId;
    }
    LogicProjectStart logicProjectStart = new LogicProjectStart();
    logicProjectStart.execute(selecthotel);
%>
responseCode:<%=logicProjectStart.getResponseCode()%><br>
errorMessage:<%=logicProjectStart.getErrorMessage()%>
<%
    if (logicProjectStart.getResponseCode() <= 205)
    {
        response.sendRedirect(response.encodeURL("../../"+loginHotelId+"/pc/mag_hotel_edit.jsp"));
    }
%>