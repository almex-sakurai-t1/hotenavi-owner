<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String selecthotel;
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    // �Z�b�V�����������I�����ꂽ�z�e�����擾
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }
%>
<%!
    private static Calendar getCalendar(String currentDate) {
        Calendar calendar = Calendar.getInstance(Locale.JAPAN);
        if (currentDate == null || currentDate.length() != 8) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        } else {
            int year = Integer.parseInt(currentDate.substring(0, 4));
            int month = Integer.parseInt(currentDate.substring(4, 6)) - 1;
            int date = Integer.parseInt(currentDate.substring(6, 8));
            calendar.set(year, month, date);
        }
        return calendar;
    }
    private static String formatDate(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
        return simpleDateFormat.format(calendar.getTime());
    }
    private static String getCurrentDateString() {
        return formatDate(getCalendar(null));
    }
    private static ArrayList<String> getDateList(Calendar calendar,Calendar calendar2) {
        ArrayList<String> list = new ArrayList<String>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
        int dateInt2 =   Integer.parseInt(simpleDateFormat.format(calendar2.getTime()));
        while (dateInt2 >=   Integer.parseInt(simpleDateFormat.format(calendar.getTime()))) {
                list.add(simpleDateFormat.format(calendar.getTime()));
                calendar.add(Calendar.DATE, 1);
        }
        return list;
    }
%>
<!--�j��������-->
<%@ include file="../../common/pc/getNationalHolidayName.jsp" %>
<%
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }
    String param_compare          = ReplaceString.getParameter(request,"Compare");
    if (param_compare == null)
    {
        param_compare = "false";
    }
    String paramKind   = ReplaceString.getParameter(request,"Kind");
    if (paramKind == null)
    {
        paramKind = "MONTHLIST";
    }

    boolean TotalMode = false;
    if (paramKind.indexOf("LISTR") != -1)
    {
        TotalMode = true;
    }
%><%-- ���ʔ���ꗗ�\������ --%><%

    int             i;
    StringFormat    sf;
    NumberFormat    nf;

    sf = new StringFormat();
    nf = new DecimalFormat("00");

    int       target_date;
    int       target_day;
    int       wday;
    String[]  arrWday = {"��", "��", "��", "��", "��", "��", "�y"};

    // ���ݓ��t
    String nowDate = getCurrentDateString();
    int now_date   = Integer.parseInt(nowDate);
    int now_year   = now_date/10000;
    int now_month  = now_date/100%100;
    int now_day    = now_date%100;

    // �v����t
    int cal_date  = ownerinfo.Addupdate;
    int cal_year  = cal_date/10000;
    int cal_month = cal_date/100%100;
    int cal_day   = cal_date%100;
    if  (cal_date == 0)
    {
        cal_date = now_date;
    }
    int   start_date = cal_date;
	boolean dateError = false;
    String startYear  = ReplaceString.getParameter(request,"StartYear");
	if( startYear != null && !CheckString.numCheck(startYear))
	{
		dateError = true;
	}
    String startMonth = ReplaceString.getParameter(request,"StartMonth");
	if( startMonth != null && !CheckString.numCheck(startMonth))
	{
		dateError = true;
	}
    String startDay   = ReplaceString.getParameter(request,"StartDay");
	if( startDay != null && !CheckString.numCheck(startDay))
	{
		dateError = true;
	}
    String paramDay   = "0";
    if  (startDay != null)
    {
        paramDay = startDay;
        if(startDay.equals("0")) startDay = "1";
    }
    if( startYear != null && startMonth != null && startDay != null )
    {
        start_date = (Integer.valueOf(startYear).intValue() * 10000) + (Integer.valueOf(startMonth).intValue() * 100) + Integer.valueOf(startDay).intValue();
    }
    else if (start_date == 0)
    {
        start_date = now_date;
    }
    String paramDate = Integer.toString(start_date);

    int start_year  = start_date/10000;
    int start_month = start_date/100%100;
    int start_day   = start_date%100;

    int end_date    = 0;
    String paramEndDate = paramDate;

    String endYear  = ReplaceString.getParameter(request,"EndYear");
	if( endYear != null && !CheckString.numCheck(endYear))
	{
		dateError = true;
	}
    String endMonth = ReplaceString.getParameter(request,"EndMonth");
	if( endMonth != null && !CheckString.numCheck(endMonth))
	{
		dateError = true;
	}
    String endDay   = ReplaceString.getParameter(request,"EndDay");
	if( endDay != null && !CheckString.numCheck(endDay))
	{
		dateError = true;
	}
	if(dateError)
	{
        startYear="0";
        startMonth="0";
        startDay="0";
        endYear="0";
        endMonth="0";
        endDay="0";
        response.sendError(400);
        return;
	}
    if( endYear != null && endMonth != null && endDay != null )
    {
        end_date = (Integer.valueOf(endYear).intValue() * 10000) + (Integer.valueOf(endMonth).intValue() * 100) + Integer.valueOf(endDay).intValue();
    }
    if (end_date == 0)
    {
        if (cal_date/100 == start_date/100)
        {
            end_date  = start_date + cal_day - 1;//�����Ȃ̂Ōv����t
        }
        else
        {
            end_date  = start_date + getCalendar(paramDate).getActualMaximum(Calendar.DATE) - 1;//�������t
        }
    }
    int end_year    = end_date/10000;
    int end_month   = end_date/100%100;
    int end_day     = end_date%100;
    paramEndDate    = Integer.toString(end_date);
    DateEdit de = new DateEdit();

    ArrayList<String> list = getDateList(getCalendar(paramDate),getCalendar(paramEndDate));
//�P�D�_�E�����[�h���鎞�̌��܂育�ƂƂ��āA�ȉ��̃\�[�X�������B
    response.setContentType("application/octetstream");

//�Q�D�_�E�����[�h����t�@�C�������w��(�g���q��csv�ɂ���)
    String filename = "monthlist_" + hotelid + "_" + start_year + "_" + start_month;
    filename = filename + ".csv";
    response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
//�R�D�_�E�����[�h����t�@�C�����X�g���[��������
    PrintWriter outstream = response.getWriter();

//  �^�C�g����������
    outstream.print("�Ǘ��X�ܓ��ʔ���ꗗ");
    outstream.print(",,");
    outstream.print(start_year + "�N");
    outstream.print(start_month + "��");
    outstream.print(",,");
    outstream.print(de.getDate(1));
    outstream.print(",");
    outstream.println(de.getTime(0));

// �^�C�g��
    outstream.print("���t,");
    outstream.print("������z,");
    outstream.print("�g��,");
    outstream.print("��]��,");
    outstream.print("�q�P��,");
    outstream.print("�����P��");
    outstream.println("");

    //�Ď擾���p
    int[]    t_target_date     = new int[61];
    int[]    t_SalesTotal      = new int[61];
    int[]    t_SalesTotalCount = new int[61];
    int[]    t_SalesTotalRate  = new int[61];
    int[]    t_SalesTotalPrice = new int[61];
    int[]    t_SalesRoomPrice  = new int[61];
    int[]    t_RoomCount       = new int[61];

    //�������[�h�̎擾
    boolean      getresult = true;
    for( i = 0 ; i < list.size() ; i++ )
    {
         // �j���̎Z�o
        wday = getCalendar(list.get(i)).get(Calendar.DAY_OF_WEEK)-1;

        target_date   = Integer.parseInt(list.get(i));
        target_day    = target_date%100;
        // ����擾���t�̃Z�b�g
        ownerinfo.SalesGetStartDate = target_date;
        ownerinfo.SalesGetEndDate = 0;

        String nationalHolidayName = null;
        nationalHolidayName = getNationalHolidayName(Integer.toString(target_date));

        Connection        connection  = null;
        PreparedStatement prestate    = null;
        ResultSet         result      = null;
        int          imedia_user      = 0;
        int          level            = 0;
        connection  = DBConnection.getConnection();
         // imedia_user �̃`�F�b�N
        try
        {
        	final String query = "SELECT * FROM owner_user WHERE hotelid=?"
             					+ " AND userid=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,loginHotelId);
            prestate.setInt(2, ownerinfo.DbUserId);
            result      = prestate.executeQuery();
            if( result.next() )
            {
                imedia_user = result.getInt("imedia_user");
                level       = result.getInt("level");
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }

        try
        {
        	final String query;
            if (imedia_user == 1 && level == 3)
            {
                query = "SELECT * FROM hotel WHERE hotel.group_id =?"
	                + " AND hotel.plan <= 2"
	                + " AND hotel.plan >= 1"
	                + " ORDER BY hotel.sort_num,hotel.hotel_id";
            }
            else
            {
                query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?"
	                 + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid"
	                 + " AND hotel.plan <= 2"
	                 + " AND hotel.plan >= 1"
	                 + " AND owner_user_hotel.sales_disp_flag = 1"
	                 + " AND owner_user_hotel.userid =?"
	                 + " ORDER BY hotel.sort_num,hotel.hotel_id";
            }
            prestate    = connection.prepareStatement(query);
            if (imedia_user == 1 && level == 3)
            {
                prestate.setString(1, selecthotel);
            }
            else
            {
                prestate.setString(1, loginHotelId);
                prestate.setInt(2, ownerinfo.DbUserId);
            }
            result      = prestate.executeQuery();
            Object lock = request.getSession();
            int room_count = 0;
            synchronized(lock)
            {
                while( result.next())
                {
                    hotelid = result.getString("hotel.hotel_id");
                    getresult = ownerinfo.sendPacket0102(1, hotelid,1);
                    if (getresult)
                    {
                        t_SalesTotal[i]      += ownerinfo.SalesTotal;
                        t_SalesTotalCount[i] += ownerinfo.SalesTotalCount;

                        room_count = 0;
                        if (ownerinfo.SalesRoomPrice != 0)
                        {
                            room_count    = Math.round((float)ownerinfo.SalesTotal / (float)ownerinfo.SalesRoomPrice);
                        }
                        t_RoomCount[i]       += room_count;
                    }
                }
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
        //��]��
        if (t_RoomCount[i] != 0)
        {
           t_SalesTotalRate[i] = Math.round((float)t_SalesTotalCount[i]*100 / (float)t_RoomCount[i]);
        }
        //�q�P��
        if (t_SalesTotalCount[i] != 0)
        {
           t_SalesTotalPrice[i] = Math.round((float)t_SalesTotal[i] / (float)t_SalesTotalCount[i]);
        }
        //�����P��
        if (t_RoomCount[i] != 0)
        {
           t_SalesRoomPrice[i] = Math.round((float)t_SalesTotal[i] / (float)t_RoomCount[i]);
        }

        outstream.print(target_date/100%100); 
        outstream.print("��"); 
        outstream.print(target_date%100); 
        outstream.print("��");
        outstream.print("�i");
        outstream.print(arrWday[wday]);
        outstream.print("�j");
        outstream.print(",");
        outstream.print(t_SalesTotal[i]);
        outstream.print(",");
        outstream.print(t_SalesTotalCount[i]);
        outstream.print(",");
        outstream.print((float)t_SalesTotalRate[i] / (float)100);
        outstream.print(",");
        outstream.print(t_SalesTotalPrice[i]);
        outstream.print(",");
        outstream.println(t_SalesRoomPrice[i]);
    }
// �N���[�Y
    outstream.close();
%>
