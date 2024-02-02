<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.kitchen.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- ���ݕ������\������ --%>

<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    String       hotelid;
    // �z�e��ID�̃Z�b�g
    hotelid = ReplaceString.getParameter(request,"HotelId");
    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }
%>

<%
    NumberFormat    nf;
    nf        = new DecimalFormat("00");
    String          sorttype;
    sorttype  = ReplaceString.getParameter(request,"sorttype");
    if (sorttype == null) sorttype = "ROOM";

    String[]  arrWday = {"��", "��", "��", "��", "��", "��", "�y"};

    KitchenInfo kitcheninfo = new KitchenInfo();

    kitcheninfo.sendPacket0000(1,hotelid);

    if (sorttype.equals("ROOM"))
    {
        kitcheninfo.SortOrderInfo(kitcheninfo.ORDERINFOTYPE_INITIAL,kitcheninfo.SORTTYPE_ROOM);
    }
    else if (sorttype.equals("ACCEPT"))
    {
        kitcheninfo.SortOrderInfo(kitcheninfo.ORDERINFOTYPE_INITIAL,kitcheninfo.SORTTYPE_ACCEPT);
    }
    else if (sorttype.equals("SETTABLE"))
    {
        kitcheninfo.SortOrderInfo(kitcheninfo.ORDERINFOTYPE_INITIAL,kitcheninfo.SORTTYPE_SETTABLE);
    }
    else if (sorttype.equals("GOODSCODE"))
    {
        kitcheninfo.SortOrderInfo(kitcheninfo.ORDERINFOTYPE_INITIAL,kitcheninfo.SORTTYPE_GOODSCODE);
    }
%>
<%=kitcheninfo.InitialNowDate/100%100%>��<%=kitcheninfo.InitialNowDate%100%>��(<%=arrWday[kitcheninfo.InitialNowDayofweek]%>) <%=nf.format(kitcheninfo.InitialNowTime/100)%>:<%=nf.format(kitcheninfo.InitialNowTime%100)%>����
<form action="<%= response.encodeURL("kitcheninfo.jsp")%>" method="post">
<select name="sorttype">
<option value="ROOM" <%if(sorttype.equals("ROOM")){%>selected<%}%>>�����ԍ���</option>
<option value="ACCEPT" <%if(sorttype.equals("ACCEPT")){%>selected<%}%>>��t������</option>
<option value="SETTABLE" <%if(sorttype.equals("SETTABLE")){%>selected<%}%>>�z�V������</option>
<option value="GOODSCODE" <%if(sorttype.equals("GOODSCODE")){%>selected<%}%>>���i�R�[�h��</option>
</select>
<input type="hidden" name="HotelId" value="<%=hotelid%>">
<input type="submit" value="�X�V">
</form>
<hr>
<%-- ���ݏ��\�� --%>
[����] ��t�@ �z�V<br>
<%  if ( kitcheninfo.InitialOrderList.size() == 0)
    {%>
<hr>
<font size=1>���݁A���z�V�̒����͂���܂���</font>
<%  }
    else
    for( int i = 0 ; i < kitcheninfo.InitialOrderList.size() ; i++ )
    {%>
<hr>
[<%=kitcheninfo.InitialOrderList.get( i ).RoomName%>]�@<%=kitcheninfo.InitialOrderList.get( i ).AcceptDatetime.substring( 8, 10 )%>:<%=kitcheninfo.InitialOrderList.get( i ).AcceptDatetime.substring( 10, 12 )%>�@<%=kitcheninfo.InitialOrderList.get( i ).SettableDatetime.substring( 8, 10 )%>:<%=kitcheninfo.InitialOrderList.get( i ).SettableDatetime.substring( 10, 12 )%><br>
<font size=1><%=kitcheninfo.InitialOrderList.get( i ).GoodsName.replace(" ","").replace("�@","")%>(<%=kitcheninfo.InitialOrderList.get( i ).GoodsCount%>)</font><br>
<%  }%>

<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="kitcheninfo.jsp" />
  <jsp:param name="dispname" value="���ݒ[�����" />
</jsp:include>

