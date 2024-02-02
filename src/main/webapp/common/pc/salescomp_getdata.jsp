<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.owner.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int          comp1_ymd;
    int          comp2_ymd;
    String       selecthotel;
    String       hotelid;
    String       query;
    String       hotelname = "";
    String       comp1_year  = ReplaceString.getParameter(request,"Comp1Year");
    String       comp1_month = ReplaceString.getParameter(request,"Comp1Month");
    String       comp2_year  = ReplaceString.getParameter(request,"Comp2Year");
    String       comp2_month = ReplaceString.getParameter(request,"Comp2Month");
    boolean      ret;
    DbAccess     db;
    ResultSet    result;
    OwnerInfo    comp1;
    OwnerInfo    comp2;

    comp1 = new OwnerInfo();
    comp2 = new OwnerInfo();

    // �Z�b�V�����������I�����ꂽ�z�e�����擾
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    if( comp1_year != null && comp1_month != null )
    {
        comp1_ymd = Integer.parseInt(comp1_year) * 10000 + Integer.parseInt(comp1_month) * 100;
    }
    else
    {
        comp1_ymd = 0;
    }
    if( comp2_year != null && comp2_month != null )
    {
        comp2_ymd = Integer.parseInt(comp2_year) * 10000 + Integer.parseInt(comp2_month) * 100;
    }
    else
    {
        comp2_ymd = 0;
    }

    // �p�����^�̓Z�b�g�ς݂Ȃ̂Ńf�[�^�擾�݂̂��s��
    if( selecthotel.compareTo("all") == 0 )
    {
    // �S�X�擾
        DbAccess db_manage =  new DbAccess();

        // �Ǘ��X�ܐ������[�v
        ResultSet DbManageHotel = ownerinfo.getManageHotel(db_manage, ownerinfo.HotelId, ownerinfo.DbUserId);
        ret = DbManageHotel.first();
        while( ret != false )
        {
            hotelid = DbManageHotel.getString("accept_hotelid");

            db =  new DbAccess();

            // �z�e�����̂̎擾
            query = "SELECT name FROM hotel WHERE hotel_id=?";
            result = db.execQuery(query,hotelid);
            if( result != null )
            {
                result.next();
                hotelname = result.getString("name");
            }

            // ������擾
            comp1.SalesGetStartDate = comp1_ymd;
            comp1.SalesGetEndDate   = 0;
            comp1.sendPacket0102(1, hotelid);
            // ����ڍ׏��擾
            comp1.SalesDetailGetStartDate = comp1_ymd;
            comp1.SalesDetailGetEndDate   = 0;
            comp1.ManualSalesDetailGetStartDate = comp1_ymd;
            comp1.ManualSalesDetailGetEndDate   = 0;
            comp1.sendPacket0104(1, hotelid);
            // ��r�P���Z�b�V���������ɃZ�b�g
            session.setAttribute("comp1", comp1);

            comp2.SalesGetStartDate = comp2_ymd;
            comp2.SalesGetEndDate   = 0;
            comp2.sendPacket0102(1, hotelid);
            // ����ڍ׏��擾
            comp2.SalesDetailGetStartDate = comp2_ymd;
            comp2.SalesDetailGetEndDate   = 0;
            comp2.ManualSalesDetailGetStartDate = comp2_ymd;
            comp2.ManualSalesDetailGetEndDate   = 0;
            comp2.sendPacket0104(1, hotelid);
            // ��r�Q���Z�b�V���������ɃZ�b�g
            session.setAttribute("comp2", comp2);
%>
<jsp:include page="salescomp_disp.jsp" flush="true" >
  <jsp:param name="NowHotel" value="<%= hotelid %>" />
  <jsp:param name="NowHotelName" value="<%= hotelname %>" />
</jsp:include>
<%
            db.close();

            ret = DbManageHotel.next();
        }

        db_manage.close();
    }
    else
    {
        db =  new DbAccess();

        // �z�e�����̂̎擾
        query = "SELECT name FROM hotel WHERE hotel_id=?";
        result = db.execQuery(query,selecthotel);
        if( result != null )
        {
            result.next();
            hotelname = result.getString("name");
        }

        // �w��X�܂̂ݎ擾
        // ������擾
        comp1.SalesGetStartDate = comp1_ymd;
        comp1.SalesGetEndDate   = 0;
        comp1.sendPacket0102(1, selecthotel);
        // ����ڍ׏��擾
        comp1.SalesDetailGetStartDate = comp1_ymd;
        comp1.SalesDetailGetEndDate   = 0;
        comp1.ManualSalesDetailGetStartDate = comp1_ymd;
        comp1.ManualSalesDetailGetEndDate   = 0;
        comp1.sendPacket0104(1, selecthotel);
        // ��r�P���Z�b�V���������ɃZ�b�g
        session.setAttribute("comp1", comp1);

        // �w��X�܂̂ݎ擾
        comp2.SalesGetStartDate = comp2_ymd;
        comp2.SalesGetEndDate   = 0;
        comp2.sendPacket0102(1, selecthotel);
        // ����ڍ׏��擾
        comp2.SalesDetailGetStartDate = comp2_ymd;
        comp2.SalesDetailGetEndDate   = 0;
        comp2.ManualSalesDetailGetStartDate = comp2_ymd;
        comp2.ManualSalesDetailGetEndDate   = 0;
        comp2.sendPacket0104(1, selecthotel);
        // ��r�P���Z�b�V���������ɃZ�b�g
        session.setAttribute("comp2", comp2);

%>
<jsp:include page="salescomp_disp.jsp" flush="true" >
  <jsp:param name="NowHotel" value="<%= selecthotel %>" />
  <jsp:param name="NowHotelName" value="<%= hotelname %>" />
</jsp:include>
<%
        db.close();
    }
%>

