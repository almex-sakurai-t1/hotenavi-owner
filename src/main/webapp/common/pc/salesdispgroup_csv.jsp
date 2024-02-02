<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String hotelid = (String)session.getAttribute("SelectHotel");
    // ƒZƒbƒVƒ‡ƒ“‚ÌŠm”F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String       selecthotel;
    String       hotelname = "";
    int          i  = 0;
    boolean      getresult = true;

    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    ReplaceString rs = new ReplaceString();

    // ƒZƒbƒVƒ‡ƒ“‘®«‚æ‚è‘I‘ð‚³‚ê‚½ƒzƒeƒ‹‚ðŽæ“¾
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }
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
    String paramGroup = ReplaceString.getParameter(request,"Group");
    if (paramGroup!=null)
    {
        if( !CheckString.hotenaviIdCheck(paramGroup) )
        {
            paramGroup = "";
            response.sendError(400);
            return;
        }
    }
    DateEdit de = new DateEdit();

    if( startYear != null &&startMonth != null && startDay != null && paramGroup != null)
    {
       ownerinfo.SalesGetStartDate = (Integer.valueOf(startYear).intValue() * 10000) + (Integer.valueOf(startMonth).intValue() * 100) + Integer.valueOf(startDay).intValue();
    }
    if( endYear != null &&endMonth != null && endDay != null  && paramGroup != null)
    {
       ownerinfo.SalesGetEndDate = (Integer.valueOf(endYear).intValue() * 10000) + (Integer.valueOf(endMonth).intValue() * 100) + Integer.valueOf(endDay).intValue();
    }

//‚PDƒ_ƒEƒ“ƒ[ƒh‚·‚éŽž‚ÌŒˆ‚Ü‚è‚²‚Æ‚Æ‚µ‚ÄAˆÈ‰º‚Ìƒ\[ƒX‚ð‘‚­B
    response.setContentType("application/octetstream");

//‚QDƒ_ƒEƒ“ƒ[ƒh‚·‚éƒtƒ@ƒCƒ‹–¼‚ðŽw’è(Šg’£Žq‚ðcsv‚É‚·‚é)
    String filename = loginHotelId + "_" + startYear + "_" + startMonth;
    if (ownerinfo.SalesGetStartDate%100 != 0)
    {
        filename = filename + "_" + startDay;
    }
    filename = filename + ".csv";
    response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
//‚RDƒ_ƒEƒ“ƒ[ƒh‚·‚éƒtƒ@ƒCƒ‹‚ðƒXƒgƒŠ[ƒ€‰»‚·‚é
    PrintWriter outstream = response.getWriter();

//  ƒ^ƒCƒgƒ‹‘‚«ž‚Ý
    outstream.print("ŠÇ—“X•Ü”„ã");
    outstream.print(",,");
    outstream.print(startYear + "”N");
    outstream.print(startMonth + "ŒŽ");
    if (ownerinfo.SalesGetStartDate%100 != 0)
    {
        outstream.print(startDay + "“ú");
    }
    if (ownerinfo.SalesGetStartDate%100 == 0)
    {
        outstream.print(",,,,,,,,,,,,,,,,,");
    }
    else
    {
        outstream.print(",,,,,,,,,,,,,,");
    }
    outstream.print(de.getDate(1));
    outstream.print(",");
    outstream.println(de.getTime(0));

// ƒ^ƒCƒgƒ‹
    outstream.print("“X•Ü–¼,");
    outstream.print("Žº”,");
    outstream.print("”„ã‹àŠz,");
    outstream.print("‘g”,");
    outstream.print("‰ñ“]”,");
    outstream.print("‹q’P‰¿,");
    outstream.print("•”‰®’P‰¿,");
    if (ownerinfo.SalesGetStartDate%100 == 0)
    {
        outstream.print("ˆê“ú“–•”‰®’P‰¿,");
    }
    outstream.print("‹xŒe”„ã‹àŠz,");
    outstream.print("‹xŒe‘g”,");
    outstream.print("‹xŒe‰ñ“]”,");
    outstream.print("‹xŒe‹q’P‰¿,");
    outstream.print("‹xŒe•”‰®’P‰¿,");
    if (ownerinfo.SalesGetStartDate%100 == 0)
    {
        outstream.print("ˆê“ú“–‹xŒe•”‰®’P‰¿,");
    }
    outstream.print("h”‘”„ã‹àŠz,");
    outstream.print("h”‘‘g”,");
    outstream.print("h”‘‰ñ“]”,");
    outstream.print("h”‘‹q’P‰¿,");

    if (ownerinfo.SalesGetStartDate%100 == 0)
    {
        outstream.print("h”‘•”‰®’P‰¿,");
        outstream.print("ˆê“ú“–h”‘•”‰®’P‰¿,");
    }
    else
    {
        outstream.print("h”‘•”‰®’P‰¿,");
    }
    outstream.print("‚»‚Ì‘¼ŽºŠO”„ã");
    outstream.println("");

    int[]    t_room_count           = new int[50];
    int[]    t_SalesTotal           = new int[50];
    int[]    t_SalesTotalCount      = new int[50];
    int[]    t_SalesTotalRate       = new int[50];
    int[]    t_SalesTotalPrice      = new int[50];
    int[]    t_SalesRoomPrice       = new int[50];
    int[]    t_SalesRoomTotalPrice  = new int[50];
    int[]    t_SalesRestTotal       = new int[50];
    int[]    t_SalesRestCount      = new int[50];
    int[]    t_SalesRestRate       = new int[50];
    int[]    t_SalesRestPrice = new int[50];
    int[]    t_SalesRestRoomPrice  = new int[50];
    int[]    t_SalesRestRoomTotalPrice  = new int[50];
    int[]    t_SalesStayTotal      = new int[50];
    int[]    t_SalesStayCount      = new int[50];
    int[]    t_SalesStayRate       = new int[50];
    int[]    t_SalesStayPrice  = new int[50];
    int[]    t_SalesStayRoomPrice  = new int[50];
    int[]    t_SalesStayRoomTotalPrice  = new int[50];
    int[]    t_SalesOtherTotal      = new int[50];
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int          imedia_user      = 0;
    int          level            = 0;
    int          sales_cache_flag = 0;
    connection  = DBConnection.getConnection();
     // imedia_user ‚Ìƒ`ƒFƒbƒN
    try
    {
        final String query = "SELECT owner_user.imedia_user,owner_user.level,owner_user_security.sales_cache_flag"
                           + " FROM owner_user"
                           + " INNER JOIN owner_user_security ON owner_user.hotelid = owner_user_security.hotelid AND owner_user.userid = owner_user_security.userid"
                           + " WHERE owner_user.hotelid = ? AND owner_user.userid = ?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
            sales_cache_flag = result.getInt("sales_cache_flag");
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


    int date_count  = 0;
    int date_count_total = 0;
    int store_count = 0;
    int line_count  = 0;
    int sales_total = 0;
    int sales_total_count = 0;
    int room_count = 0;
    int room_count_total = 0;
    int sales_rest_total = 0;
    int sales_rest_total_count = 0;
    int sales_stay_total = 0;
    int sales_stay_total_count = 0;
    int sales_other_total = 0;
    boolean SplitFlag = false;
    try
    {
        final String query;
        if (imedia_user == 1 && level == 3 && !selecthotel.equals("all_manage"))
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
             + " AND owner_user_hotel.userid = ?"
             + " ORDER BY hotel.sort_num,hotel.hotel_id";
        }
        prestate    = connection.prepareStatement(query);
        if (imedia_user == 1 && level == 3 && !selecthotel.equals("all_manage"))
        {
            prestate.setString(1,selecthotel);
        }
        else
        {
            prestate.setString(1,ownerinfo.HotelId);
            prestate.setInt(2,ownerinfo.DbUserId);
        }
        result      = prestate.executeQuery();
        Object lock = request.getSession();
        synchronized(lock){
        while( result.next())
        {
            hotelid   = result.getString("hotel.hotel_id");
            hotelname = result.getString("hotel.name");
            line_count++;
            i = line_count;

            room_count = 0;
            ownerinfo.sendPacket0100(1, hotelid);
            getresult = ownerinfo.sendPacket0114(1, hotelid);
            room_count        = ownerinfo.StateRoomCount;
            t_room_count[i]   = room_count;


            if (t_SalesTotal[i] == 0 && t_SalesTotalCount[i]==0)
            {
                getresult = true;

                if (ownerinfo.SalesGetStartDate == ownerinfo.SalesGetEndDate || ownerinfo.SalesGetEndDate == 0)
                {
                    getresult = ownerinfo.sendPacket0102(1, hotelid,sales_cache_flag);
                }
                else
                {
                    getresult = ownerinfo.sendPacket0102(1, hotelid);
                }
                if (getresult)
                {
                    t_SalesTotal[i]          = ownerinfo.SalesTotal;
                    t_SalesTotalCount[i]     = ownerinfo.SalesTotalCount;
                    t_SalesTotalRate[i]      = ownerinfo.SalesTotalRate;
                    t_SalesTotalPrice[i]     = ownerinfo.SalesTotalPrice;
                    t_SalesRoomPrice[i]      = ownerinfo.SalesRoomPrice;
                    t_SalesRestTotal[i]      = ownerinfo.SalesRestTotal;
                    t_SalesRestCount[i]     = ownerinfo.SalesRestCount;
                    t_SalesRestRate[i]      = ownerinfo.SalesRestRate;
                    t_SalesRestPrice[i]     = ownerinfo.SalesRestPrice;
                    t_SalesRestRoomPrice[i] = Math.round((float)t_SalesRestTotal[i] / (float)t_room_count[i]);
                    t_SalesStayTotal[i]      = ownerinfo.SalesStayTotal;
                    t_SalesStayCount[i]     = ownerinfo.SalesStayCount;
                    t_SalesStayRate[i]      = ownerinfo.SalesStayRate;
                    t_SalesStayPrice[i]     = ownerinfo.SalesStayPrice;
                    t_SalesStayRoomPrice[i] = Math.round((float)t_SalesStayTotal[i] / (float)t_room_count[i]);
                    if (ownerinfo.SalesGetStartDate%100 != 0 && ownerinfo.SalesGetEndDate==0)
                    {
                        t_SalesRoomTotalPrice[i] = ownerinfo.SalesRoomPrice;
                    }
                    else
                    {
                        t_SalesRoomTotalPrice[i] = ownerinfo.SalesRoomTotalPrice;
                    }
                    t_SalesOtherTotal[i]      = ownerinfo.SalesOtherTotal;
                }
            }
            date_count = 0;
            if (t_SalesRoomPrice[i] != 0)
            {
                date_count    = Math.round((float)t_SalesRoomTotalPrice[i] / (float)t_SalesRoomPrice[i]);
            }
            if (ownerinfo.SalesGetStartDate%100 != 0 && ownerinfo.SalesGetEndDate==0)
            {
                t_SalesRestRoomTotalPrice[i] = t_SalesRestRoomPrice[i];
                t_SalesStayRoomTotalPrice[i] = t_SalesStayRoomPrice[i];
            }
            else
            {
                t_SalesRoomTotalPrice[i] = ownerinfo.SalesRoomTotalPrice;
                t_SalesRestRoomTotalPrice[i] = Math.round((float)t_SalesRestTotal[i] / ((float)t_room_count[i] * date_count));
                t_SalesStayRoomTotalPrice[i] = Math.round((float)t_SalesStayTotal[i] / ((float)t_room_count[i] * date_count));
            }

            if(result.getInt("split_flag")==1)
            {
                SplitFlag = true;
            }

            if((t_SalesTotal[i]!=0 || t_SalesTotalCount[i]!=0) && result.getInt("split_flag") == 0)
            {
                store_count++;
            }
            if(result.getInt("split_flag") == 0)
            {
                sales_total       = sales_total       + t_SalesTotal[i];
                sales_total_count = sales_total_count + t_SalesTotalCount[i];
                room_count_total  = room_count_total + room_count;
                date_count_total  = date_count_total + date_count*room_count;
                sales_rest_total       = sales_rest_total       + t_SalesRestTotal[i];
                sales_rest_total_count = sales_rest_total_count + t_SalesRestCount[i];
                sales_stay_total       = sales_stay_total       + t_SalesStayTotal[i];
                sales_stay_total_count = sales_stay_total_count + t_SalesStayCount[i];
                sales_other_total      = sales_other_total      + t_SalesOtherTotal[i];
            }

            outstream.print(hotelname);
            outstream.print(",");
            if (!getresult)//Žæ“¾‚Å‚«‚È‚©‚Á‚½
            {
                outstream.print("Žæ“¾‚Å‚«‚Ü‚¹‚ñ‚Å‚µ‚½");
            }
            else
            {
                outstream.print(t_room_count[i]);
                outstream.print(",");
                outstream.print(t_SalesTotal[i]);
                outstream.print(",");
                outstream.print(t_SalesTotalCount[i]);
                outstream.print(",");
                outstream.print((float)t_SalesTotalRate[i] / (float)100);
                outstream.print(",");
                outstream.print(t_SalesTotalPrice[i]);
                outstream.print(",");
                outstream.print(t_SalesRoomTotalPrice[i]);
                outstream.print(",");
                if (ownerinfo.SalesGetStartDate%100 == 0)
                {
                    outstream.print(t_SalesRoomPrice[i]);
                    outstream.print(",");
                }
                outstream.print(t_SalesRestTotal[i]);
                outstream.print(",");
                outstream.print(t_SalesRestCount[i]);
                outstream.print(",");
                outstream.print((float)t_SalesRestRate[i] / (float)100 );
                outstream.print(",");
                outstream.print(t_SalesRestPrice[i]);
                outstream.print(",");
                outstream.print(t_SalesRestRoomPrice[i]);
                outstream.print(",");
                if (ownerinfo.SalesGetStartDate%100 == 0)
                {
                    outstream.print(t_SalesRestRoomTotalPrice[i]);
                    outstream.print(",");
                }
                outstream.print(t_SalesStayTotal[i]);
                outstream.print(",");
                outstream.print(t_SalesStayCount[i]);
                outstream.print(",");
                outstream.print((float)t_SalesStayRate[i] / (float)100 );
                outstream.print(",");
                outstream.print(t_SalesStayPrice[i]);
                outstream.print(",");
                outstream.print(t_SalesStayRoomPrice[i]);
                outstream.print(",");
                if (ownerinfo.SalesGetStartDate%100 == 0)
                {
                    outstream.print(t_SalesStayRoomTotalPrice[i]);
                    outstream.print(",");
                }
                outstream.print(t_SalesOtherTotal[i]);
                outstream.println("");
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
// ƒNƒ[ƒY
    outstream.close();
%>

