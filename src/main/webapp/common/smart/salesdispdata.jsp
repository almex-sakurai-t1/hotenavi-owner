<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLEncoder" %> 
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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
    String param_day  = ReplaceString.getParameter(request,"Day");
    if (param_day != null)
    {
       if(param_day.equals("0")) 
       {
           MonthFlag = true; 
       }
    }
    String param_month = ReplaceString.getParameter(request,"Month");
    String[] dates = new String[]{ param_month, param_day };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }

    if( ownerinfo.SalesGetStartDate != 0 && ownerinfo.SalesGetEndDate != 0  && ownerinfo.SalesGetEndDate != ownerinfo.SalesGetStartDate)
    {
        RangeFlag = true; 
        // ���Ԏw�肳��Ă���ꍇ
%>
<h2><%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %>/<%= (ownerinfo.SalesGetStartDate % 100) %>~<%= (ownerinfo.SalesGetEndDate / 10000) %>/<%= (ownerinfo.SalesGetEndDate / 100 % 100) %>/<%= (ownerinfo.SalesGetEndDate % 100) %></h2>
<%
    }
    else
    {
        // ����ȊO
        if( (ownerinfo.SalesGetStartDate % 100) != 0 )
        {
%>
<h2><%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %>/<%= (ownerinfo.SalesGetStartDate % 100) %></h2>
<%
        }
        else
        {
          MonthFlag = true; 
%>
<h2><%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %></h2>
<%
        }
    }
%>

<h1>�v�㕪 <font size="-1"><a href="#DateSelect">[<%if(MonthFlag){%>�N��<%}else{%>���t<%}%>�I��]</a></font></h1>
<%
    int year     = ownerinfo.SalesGetStartDate / 10000;
    int month    = ownerinfo.SalesGetStartDate / 100 % 100;
    int day      = ownerinfo.SalesGetStartDate % 100;
    int endyear  = ownerinfo.SalesGetEndDate / 10000;
    int endmonth = ownerinfo.SalesGetEndDate / 100 % 100;
    int endday   = ownerinfo.SalesGetEndDate % 100;

    // �z�e��ID�̃Z�b�g
    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }

    int pointtotal = 0;

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        final String query = "SELECT pointtotal_flag FROM hotel WHERE hotel_id = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, hotelid);

        result = prestate.executeQuery();
        if( result.next() )
        {
            pointtotal= result.getInt("hotel.pointtotal_flag");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result, prestate, connection);
    }

    if (pointtotal == 0 && ownerinfo.SalesPointTotal != 0)
    {
        try
        {
            final String query = "UPDATE hotel SET pointtotal_flag = 1 WHERE hotel_id = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setString(1, hotelid);

            prestate.executeUpdate();
            pointtotal = 1;
        }
        catch( Exception e )
        {
            Logging.error("foward Exception e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(connection);
        }
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
<!-- ����\�� -->

<h1>������</h1>

<table class="uriage">
<tr>
<th width="50%">����</th><th>���z</th>
</tr>
<tr>
<td><div align=left style="margin-left:10px">���Z�@<font size=-1>(����)</font></div></td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(416850*per)), 10 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTex), 10 ) %></td><%}%>
</tr>
<tr>
<td><div align=left style="margin-left:10px">���Z�@<font size=-1>(CREDIT)</font></div></td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(107550*per)), 10 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTexCredit), 10 ) %></td><%}%>
</tr>
<tr>
<td><div align=left style="margin-left:10px">�t�����g<font size=-1>(����)</font></div></td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(   600*per)), 10 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesFront), 10 ) %></td><%}%>
</tr>
<tr>
<td><div align=left style="margin-left:10px">�t�����g<font size=-1>(CREDIT)</font></div></td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 10 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesFrontCredit), 10 ) %></td><%}%>
</tr>
<%
    if (pointtotal == 1)
    {
%><tr>
<td><div align=left style="margin-left:10px">��g�|�C���g</div></td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 7 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesPointTotal), 7 ) %></td><%}%>
</tr>
<%
    }
%><tr class="uriage">
<td>���v</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(525000*per)), 12 ) %></td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTotal), 12 ) %></td><%}%>
</tr>
</table>

<div align="right">
<ul class="link">
<li><a href="<%= response.encodeURL("salesdetail.jsp" + param) %>">����ڍׂ�����</a></li>
</ul>
</div>
<!-- ����\�������܂� -->

<!-- �g���\�� -->
<h1>���g��</h1>
<table class="uriage">
<tr>
<th width="50%">����</td><th>�g��</td>
</tr>
<tr>
<td>�x�e</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(42*per)), 10 ) %>�g</td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesRestCount), 10) %><%}%>�g</td>
<tr>
<td>�h��</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(18*per)), 10 ) %>�g</td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesStayCount), 10) %><%}%>�g</td>
</tr>
<tr class="uriage">
<td>�ގ��όv</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(60*per)), 6 ) %>�g</td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesTotalCount), 6) %><%}%>�g</td>
</tr>
</table>
�@<br>
<table class="uriage">
<tr>
<th width="50%">����</td><th>�@</td>
</tr>
<!--<%
    if( ownerinfo.SalesNowCheckin != 999 )
    {
%>
<tr>
<td>���ݓ���</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(5*per)), 6 ) %>�g</td><%}else{%><td class="uriage"><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesNowCheckin), 6) %>�g</td><%}%>
</tr>
<%
    }
%>-->
<tr>
<td>��]��</td><%if(DemoMode){%><td class="uriage">3.00</td><%}else{%><td class="uriage"><%=(float)ownerinfo.SalesTotalRate / (float)100 %></td><%}%>
</tr>
<tr>
<td>�q�P��</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(8750*per)), 10 ) %></td><%}else{%><td class="uriage"><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesTotalPrice),10) %></td><%}%>
</tr>
<%
    if( ownerinfo.SalesRoomTotalPrice != 0 && ownerinfo.SalesRoomTotalPrice != ownerinfo.SalesRoomPrice)
    {
%>
<tr>
<td>���P����</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per)), 8 ) %></td><%}else{%><td class="uriage"><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomPrice),8) %></td><%}%>
</tr>
<tr>
<td>���P�v</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per*30)), 10 ) %></td><%}else{%><td class="uriage"><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomTotalPrice),10) %></td><%}%>
</tr>
<%
    }
    else
    {
%>
<tr>
<td>�����P��</td><%if(DemoMode){%><td class="uriage"><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per)), 8 ) %></td><%}else{%><td class="uriage"><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomPrice),8) %></td><%}%>
</tr>
<%
    }
%>
</table>
<%@ include file="checkHotelId.jsp" %>
<%
    boolean HotelIdCheck = true;
    HotelIdCheck = checkHotelId(loginHotelId, hotelid, ownerinfo.DbUserId, 2);
    if (HotelIdCheck && ownerinfo.Addupdate == ownerinfo.SalesGetStartDate && ownerinfo.Addupdate == ownerinfo.SalesGetEndDate)
    {
%>
�@<br>
<h1 class="title">���ݕ������</h1>
<%-- �����Ō��ݕ��������擾���܂� --%>
<jsp:include page="roominfoget.jsp" flush="true" />
<%-- �����Ō��ݕ��������擾���܂��i�����܂Łj --%>
<%-- ���ݕ������f�[�^�̕\�� --%>
<jsp:include page="roominfodispdata.jsp" flush="true">
 <jsp:param name="jump" value="no"/>
</jsp:include>
<%-- ���ݕ������f�[�^�̕\�������܂� --%>
<%
    }
    else
    {
%>
<div align="right">
<ul class="link">
<li><a href="<%= response.encodeURL("salesinout.jsp" + param) %>">IN/OUT�g��������</a></li>
</ul>
</div>
<%
    }
%>
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
