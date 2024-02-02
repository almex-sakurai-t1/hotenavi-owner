<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page import="java.util.*" %>
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

    StringFormat    sf;
    sf = new StringFormat();

    boolean MonthFlag = false;
    boolean RangeFlag = false;
    String param_day   = ReplaceString.getParameter(request,"Day");
    if (param_day != null)
    {
        if ( !CheckString.numCheck( param_day ) )
        {
            response.sendError(400);
            return;
        }
        if(param_day.equals("0"))
        {
            MonthFlag = true;
        }
    }
    String param_month = ReplaceString.getParameter(request,"Month");
    if ( param_month != null && !CheckString.numCheck( param_month ) )
    {
        response.sendError(400);
        return;
    }

    if( ownerinfo.SalesGetStartDate != 0 && ownerinfo.SalesGetEndDate != 0  && ownerinfo.SalesGetEndDate != ownerinfo.SalesGetStartDate)
    {
        RangeFlag = true;
        // ���Ԏw�肳��Ă���ꍇ
%>
<%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %>/<%= (ownerinfo.SalesGetStartDate % 100) %>�`<br>
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
�v�㕪<font size=1>[<a href="#DateSelect"><%if(MonthFlag){%>�N��<%}else{%>���t<%}%>�I��</a>]</font><br>
<%
    int year     = ownerinfo.SalesGetStartDate / 10000;
    int month    = ownerinfo.SalesGetStartDate / 100 % 100;
    int day      = ownerinfo.SalesGetStartDate % 100;
    int endyear  = ownerinfo.SalesGetEndDate / 10000;
    int endmonth = ownerinfo.SalesGetEndDate / 100 % 100;
    int endday   = ownerinfo.SalesGetEndDate % 100;

    // �z�e��ID�̃Z�b�g
    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }

    int    pointtotal = 0;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    try
    {
        connection        = DBConnection.getConnection();
        final String query = "SELECT pointtotal_flag FROM hotel WHERE hotel_id = ? ";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            pointtotal= result.getInt("hotel.pointtotal_flag");
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        if (pointtotal == 0 && ownerinfo.SalesPointTotal!=0)
        {
            pointtotal = 1;
            prestate = connection.prepareStatement("UPDATE hotel SET pointtotal_flag = 1 WHERE hotel_id = ? ");
            prestate.setString(1, hotelid);
            prestate.executeUpdate();
        }
    }
    catch( Exception e )
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }

    String param = "?HotelId=" + hotelid;
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
<hr><!-- ����\�� -->
<pre>
������
 ����       ���z
��(��)<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(416850*per)), 10 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTex), 10 ) %><%}%>
��(��)<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(107550*per)), 10 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTexCredit), 10 ) %><%}%>
��(��)<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(   600*per)), 10 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesFront), 10 ) %><%}%>
��(��)<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 10 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesFrontCredit), 10 ) %><%}%>
<%
    if (pointtotal == 1)
    {
%>��g�߲��<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(     0*per)), 7 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesPointTotal), 7 ) %><%}%>
<%
    }
%>----------------
���v<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(525000*per)), 12 ) %><%}else{%><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTotal), 12 ) %><%}%></pre>
<div align="right">
<a href="<%= response.encodeURL("salesdetail.jsp" + param) %>">����ڍׂ�����</a>
</div>
<!-- ����\�������܂� -->
<hr>
<!-- �g���\�� -->
<pre>
���g��
 ����       �g��
�x�e<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(42*per)), 10 ) %><%}else{%><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesRestCount), 10) %><%}%>�g
�h��<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(18*per)), 10 ) %><%}else{%><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesStayCount), 10) %><%}%>�g
----------------
�ގ��όv<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(60*per)), 6 ) %><%}else{%><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesTotalCount), 6) %><%}%>�g
----------------
<!--<%
    if( ownerinfo.SalesNowCheckin != 999 )
    {
%>
���ݓ���<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(5*per)), 6 ) %><%}else{%><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesNowCheckin), 6) %><%}%>�g
<%
    }
%>-->
��]���@�@�@<%if(DemoMode){%>3.00<%}else{%><%=(float)ownerinfo.SalesTotalRate / (float)100 %><%}%>
�q�P��<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(8750*per)), 10 ) %><%}else{%><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesTotalPrice),10) %><%}%>
<%
    if( ownerinfo.SalesRoomTotalPrice != 0 && ownerinfo.SalesRoomTotalPrice != ownerinfo.SalesRoomPrice)
    {
%>���P����<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per)), 8 ) %><%}else{%><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomPrice),8) %><%}%>
���P�v<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per*30)), 10 ) %><%}else{%><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomTotalPrice),10) %><%}%>
<%
    }
    else
    {
%>�����P��<%if(DemoMode){%><%= sf.rightFitFormat( Kanma.get((int)Math.round(26250*per)), 8 ) %><%}else{%><%=sf.rightFitFormat ( Kanma.get(ownerinfo.SalesRoomPrice),8) %><%}%>
<%
    }
%></pre>
<%@ include file="checkHotelId.jsp" %>
<%
    boolean    HotelIdCheck = true;
    HotelIdCheck = checkHotelId(loginHotelId,hotelid,ownerinfo.DbUserId,2);
    if (HotelIdCheck && ownerinfo.Addupdate == ownerinfo.SalesGetStartDate && ownerinfo.Addupdate == ownerinfo.SalesGetEndDate)
    {
%>
<hr>
�����ݕ������<br>
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
<a href="<%= response.encodeURL("salesinout.jsp" + param) %>">IN/OUT�g��������</a>
</div>
<%
    }
%>
<hr>
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


