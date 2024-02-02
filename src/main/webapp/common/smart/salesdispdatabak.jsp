<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLEncoder" %> 
<%@ page import="com.hotenavi2.common.*" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- ����\������ --%>
<%
    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }
    float per = ((float)ownerinfo.SalesGetStartDate-20050325)/(float)50000;

    StringFormat sf = new StringFormat();

    boolean MonthFlag = false;
    boolean RangeFlag = false;
    String param_day  = request.getParameter("Day");
    if (param_day != null)
    {
       if(param_day.equals("0")) 
       {
           MonthFlag = true; 
       }
    }
    String param_month = request.getParameter("Month");

    if( ownerinfo.SalesGetStartDate != 0 && ownerinfo.SalesGetEndDate != 0  && ownerinfo.SalesGetEndDate != ownerinfo.SalesGetStartDate)
    {
        RangeFlag = true; 
        // ���Ԏw�肳��Ă���ꍇ
%>
<%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %>/<%= (ownerinfo.SalesGetStartDate % 100) %>~<br>
<%= (ownerinfo.SalesGetEndDate / 10000) %>/<%= (ownerinfo.SalesGetEndDate / 100 % 100) %>/<%= (ownerinfo.SalesGetEndDate % 100) %>
<%
    }
    else
    {
        // ����ȊO
        if( (ownerinfo.SalesGetStartDate % 100) != 0 )
        {
%>
<%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %>/<%= (ownerinfo.SalesGetStartDate % 100) %>
<%
        }
        else
        {
          MonthFlag = true; 
%>
<%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %>
<%
        }
    }
%>
<h1>�v�㕪<font size=1>[<a href="#DateSelect"><%if(MonthFlag){%>�N��<%}else{%>���t<%}%>�I��</a>]</font><br></h1>
<%
    int year     = ownerinfo.SalesGetStartDate / 10000;
    int month    = ownerinfo.SalesGetStartDate / 100 % 100;
    int day      = ownerinfo.SalesGetStartDate % 100;
    int endyear  = ownerinfo.SalesGetEndDate / 10000;
    int endmonth = ownerinfo.SalesGetEndDate / 100 % 100;
    int endday   = ownerinfo.SalesGetEndDate % 100;

    // �z�e��ID�̃Z�b�g
    String hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }
    String param = "?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J");
    if (param_month != null)
    {
        param = param + "&Year=" + year + "&Month=" + month;
    }
    if (MonthFlag)
    {
        param = param + "&Day=0";
    }
    else
    {
        param = param + "&Day=" + day;
    }
    if (RangeFlag)
    {
        param = param + "&EndYear=" + endyear + "&EndMonth=" + endmonth + "&EndDay=" + endday + "&Range";
    }
%>
<hr class="border">
<!-- ����\�� -->

<center><h1>������</h1></center>

<hr class="border">
<table class="detail">
<tr>
<td>����</td><td>���z</td>
</th>
<tr>
<td>��(��)</td><%if(DemoMode){%><td><%= sf.rightFitFormat( Kanma.get((int)Math.round(416850*per)), 10 ) %></td><%}else{%><td><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTex), 10 ) %></td><%}%>
</tr>
<tr>
<td>��(��)</td><%if(DemoMode){%><td><%= sf.rightFitFormat( Kanma.get((int)Math.round(107550*per)), 10 ) %></td><%}else{%><td><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTexCredit), 10 ) %></td><%}%>
</tr>
<tr>
<td>��(��)</td><%if(DemoMode){%><td><%= sf.rightFitFormat( Kanma.get((int)Math.round(   600*per)), 10 ) %></td><%}else{%><td><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesFront), 10 ) %></td><%}%>
</tr>
<tr>
<td>��(��)</td><%if(DemoMode){%><td><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 10 ) %></td><%}else{%><td><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesFrontCredit), 10 ) %></td><%}%>
</tr>
<tr>
<td colspan="2">-------------------------</td>
</tr>
<tr>
<td>���v</td><%if(DemoMode){%><td><%= sf.rightFitFormat( Kanma.get((int)Math.round(525000*per)), 12 ) %></td><%}else{%><td><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTotal), 12 ) %></td><%}%>
</tr>
</table>

<div align="right">
<li><a href="<%= response.encodeURL("salesdetail.jsp" + param) %>">����ڍׂ�����</a></li>
</div>

<!-- ����\�������܂� -->

<!-- �g���\�� -->

<center><h1>���g��</h1></center>

<hr class="border">
<table class="detail">
<tr>
<td>����</td><td>�g��</td>
</tr>
<tr>
<td>�x�e</td><%if(DemoMode){%><td><%= sf.rightFitFormat( Kanma.get((int)Math.round(42*per)), 10 ) %>�g</td><%}else{%><td><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesRestCount), 10) %><%}%>�g</td>
<tr>
<td>�h��</td><%if(DemoMode){%><td><%= sf.rightFitFormat( Kanma.get((int)Math.round(18*per)), 10 ) %>�g</td><%}else{%><td><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesStayCount), 10) %><%}%>�g</td>
</tr>
<tr>
<td colspan="2">-------------------------</td>
</tr>
<tr>
<td>�ގ��όv</td><%if(DemoMode){%><td><%= sf.rightFitFormat( Kanma.get((int)Math.round(60*per)), 6 ) %>�g</td><%}else{%><td><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesTotalCount), 6) %><%}%>�g</td>
</tr>
<tr>
<td colspan="2">-------------------------</td>
</tr>
</table>
<%
    if( ownerinfo.SalesNowCheckin != 999 )
    {
%>
<table class="detail">
<tr>
<td>���ݓ���</td><%if(DemoMode){%><td><%= sf.rightFitFormat( Kanma.get((int)Math.round(5*per)), 6 ) %>�g</td><%}else{%><td><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesNowCheckin), 6) %><%}%>�g</td>
<%
    }
%>
</tr>
<tr>
<td>��]��</td><%if(DemoMode){%><td>3.00</td><%}else{%><td><%=(float)ownerinfo.SalesTotalRate / (float)100 %></td><%}%>
</tr>
<tr>
<td>�q�P��</td><%if(DemoMode){%><td><%= sf.rightFitFormat( Kanma.get((int)Math.round(8750*per)), 10 ) %></td><%}else{%><td><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesTotalPrice),10) %></td><%}%>
</tr>
<tr>
<%
    if( ownerinfo.SalesRoomTotalPrice != 0 && ownerinfo.SalesRoomTotalPrice != ownerinfo.SalesRoomPrice)
    {
%><td>���P����</td><%if(DemoMode){%><td><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per)), 8 ) %></td><%}else{%><td><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomPrice),8) %></td><%}%>
</tr>
<tr>
<td>���P�v</td><%if(DemoMode){%><td><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per*30)), 10 ) %></td><%}else{%><td><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomTotalPrice),10) %></td><%}%>
</tr>
<%
    }
    else
    {
%>
<tr>
<td>�����P��</td><%if(DemoMode){%><td><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per)), 8 ) %></td><%}else{%><td><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomPrice),8) %></td><%}%>
</tr>
<%
    }
%>
</table>

<div align="right">
<li><a href="<%= response.encodeURL("salesinout.jsp" + param) %>">IN/OUT�g��������</a></li>
</div>


<a name="DateSelect"></a>
<% 
    if(MonthFlag)
    {
%>
<%-- �N���I����ʕ\�� --%>
<jsp:include page="salesselectmonth.jsp" flush="true" />
<%-- �N���I����ʕ\�������܂� --%>
<%
    }
    else if (RangeFlag)
    {
%>
<%-- ���t�͈͑I����ʕ\�� --%>
<jsp:include page="salesselectrange.jsp" flush="true" />
<%-- ���t�͈͑I����ʕ\�������܂� --%>
<%
    }
    else
    {
%>
<%-- ���t�I����ʕ\�� --%>
<jsp:include page="salesselectday.jsp" flush="true" />
<%-- ���t�I����ʕ\�������܂� --%>
<%
    }
%>
<!-- �g���\�������܂� -->
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="jumpurl" value="salesdisp.jsp" />
  <jsp:param name="dispname" value="������" />
</jsp:include>
