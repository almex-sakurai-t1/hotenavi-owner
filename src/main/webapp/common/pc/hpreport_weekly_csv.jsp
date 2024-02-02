<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/hpreport_ini.jsp" %>
<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String hotelid = (String)session.getAttribute("SelectHotel");
    if (ReplaceString.getParameter(request,"hotelid") != null)
    {
        hotelid = ReplaceString.getParameter(request,"hotelid");
    }

    if( hotelid == null )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");

    ReplaceString rs = new ReplaceString();

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String            query       = "";
    connection  = DBConnection.getConnection();

    //�z�e�����̎擾
    String            hname = "";
    if (hotelid.compareTo("all") == 0) 
    {
        hname = "�Ǘ��X�܏W�v";
    }
    else
    {
        try
        {
            query = "SELECT * FROM hotel WHERE hotel_id=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,  hotelid);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                hname       = result.getString("name");
                hname       = rs.replaceKanaFull(hname);
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
    }

    int     i     = 0;
    int     count = 0;
    int     per   = 0;
    int     target_date   = 0;
    int     total_mobile  = 0;
    int     total_pc      = 0;
    int     total_smart   = 0;
    int     total_docomo  = 0;
    int     total_jphone  = 0;
    int     total_au      = 0;
    int     total_total   = 0;
    int     total_max     = Integer.parseInt(Max);
    int[]   t_count  = new int[7];
    int[]   t_mobile = new int[7];
    int[]   t_smart  = new int[7];
    int[]   t_pc     = new int[7];
    int[]   t_total  = new int[7];
    String[]t_week   = new String[7];
    t_week[0] = "��";
    t_week[1] = "��";
    t_week[2] = "��";
    t_week[3] = "��";
    t_week[4] = "��";
    t_week[5] = "��";
    t_week[6] = "�y";
    for (i = 0;i < 7; i++)
    {
        String FromDate = from_date/10000 + "-" + from_date/100%100 + "-" +  from_date%100;
        String ToDate   = to_date/10000 + "-" + to_date/100%100 + "-" +  to_date%100;
        if (hotelid.compareTo("all") == 0) 
        {
            query = "SELECT count(*),Sum(total),Sum(docomo),Sum(jphone),Sum(au),Sum(etc),Sum(smart)  FROM access_mobile_detail,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
            query = query + " AND access_mobile_detail.hotel_id = owner_user_hotel.accept_hotelid";
            query = query + " AND owner_user_hotel.userid = ?";
        }
        else
        {
            query = "SELECT count(*),Sum(total),Sum(docomo),Sum(jphone),Sum(au),Sum(etc),Sum(smart)  FROM access_mobile_detail WHERE hotel_id=?";
        }
        query = query + " AND acc_date>=?";
        query = query + " AND acc_date<=?";
        query = query + " AND week0" + (i+1) + ">0";
        prestate    = connection.prepareStatement(query);
        if (hotelid.compareTo("all") == 0) 
        {
            prestate.setString(1, loginHotelId);
            prestate.setInt(2, ownerinfo.DbUserId);
            prestate.setString(3, FromDate);
            prestate.setString(4, ToDate);
        }
        else
        {
            prestate.setString(1, hotelid);
            prestate.setString(2, FromDate );
            prestate.setString(3, ToDate);
        }
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            t_count[i]   = result.getInt(1);
            t_total[i]   = result.getInt(2);
            t_mobile[i]  = result.getInt(3) + result.getInt(4) + result.getInt(5);
            if (from_date <= 20131031)
            {
                t_pc[i]      = result.getInt(6) + result.getInt(7);
                t_smart[i]   = 0;
            }
            else
            {
                t_pc[i]      = result.getInt(6);
                t_smart[i]   = result.getInt(7);
            }
            total_mobile = total_mobile + t_mobile[i];
            total_docomo = total_docomo + result.getInt(3);
            total_jphone = total_jphone + result.getInt(4);
            total_au     = total_au     + result.getInt(5);
            total_smart  = total_smart  + t_smart[i];
            total_pc     = total_pc     + t_pc[i];
            total_total  = total_total  + t_total[i];
            if (total_max < t_total[i]) total_max = t_total[i];
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    DBConnection.releaseResources(connection);

    NumberFormat  nf2;
    nf2    = new DecimalFormat("00");

//�P�D�_�E�����[�h���鎞�̌��܂育�ƂƂ��āA�ȉ��̃\�[�X�������B
    response.setContentType("application/octetstream");

//�Q�D�_�E�����[�h����t�@�C�������w��(�g���q��csv�ɂ���)
    String filename = Contents[type].replace(".jsp","") + "_" + hotelid + "_" + year;
    if (month != 0)
    {
        filename = filename  + "_" + nf2.format(month);
    }
    if (day != 0)
    {
        filename = filename  + "_" + nf2.format(day);
    }
    filename = filename + ".csv";
    response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
//�R�D�_�E�����[�h����t�@�C�����X�g���[��������
    PrintWriter outstream = response.getWriter();


//  �^�C�g����������
    outstream.print(Title[type]);
    outstream.print(",");
    outstream.print(hname);
    outstream.print(",");
    outstream.print(year + "�N");
    if (month != 0)
    {
        outstream.print(month + "��");
    }
    if (day != 0)
    {
        outstream.print(day + "��");
    }
    outstream.print(",");
    outstream.print(de.getDate(1));
    outstream.print(",");
    outstream.println(de.getTime(0));

// �^�C�g��
    outstream.print("�j��,");
    outstream.print("�g��,");
    outstream.print("�X�}�z,");
    outstream.print("PC��,");
    outstream.println("���v");

//����
    for (i = 0;i < 7; i++)
    {
        outstream.print(t_week[i]);
        outstream.print(",");
        outstream.print(t_mobile[i]);
        outstream.print(",");
        outstream.print(t_smart[i]);
        outstream.print(",");
        outstream.print(t_pc[i]);
        outstream.print(",");
        outstream.println(t_total[i]);
    }

//���v
    outstream.print("���v,");
    outstream.print(total_mobile);
    outstream.print(",");
    outstream.print(total_smart);
    outstream.print(",");
    outstream.print(total_pc);
    outstream.print(",");
    outstream.println(total_total);

// �N���[�Y
    outstream.close();
%>
