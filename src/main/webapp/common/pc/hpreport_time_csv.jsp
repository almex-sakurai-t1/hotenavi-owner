<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/hpreport_ini.jsp" %>
<%
    // セッションの確認
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

    //ホテル名の取得
    String            hname = "";
    if (hotelid.compareTo("all") == 0) 
    {
        hname = "管理店舗集計";
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
    int     total_total   = 0;
    int     total_max     = Integer.parseInt(Max);
    int[]   t_total  = new int[24];
    String[]t_time   = new String[24];
    t_time[ 0] = " 9:00";
    t_time[ 1] = "10:00";
    t_time[ 2] = "11:00";
    t_time[ 3] = "12:00";
    t_time[ 4] = "13:00";
    t_time[ 5] = "14:00";
    t_time[ 6] = "15:00";
    t_time[ 7] = "16:00";
    t_time[ 8] = "17:00";
    t_time[ 9] = "18:00";
    t_time[10] = "19:00";
    t_time[11] = "20:00";
    t_time[12] = "21:00";
    t_time[13] = "22:00";
    t_time[14] = "23:00";
    t_time[15] = " 0:00";
    t_time[16] = " 1:00";
    t_time[17] = " 2:00";
    t_time[18] = " 3:00";
    t_time[19] = " 4:00";
    t_time[20] = " 5:00";
    t_time[21] = " 6:00";
    t_time[22] = " 7:00";
    t_time[23] = " 8:00";
    try
    {
        String FromDate = from_date/10000 + "-" + from_date/100%100 + "-" +  from_date%100;
        String ToDate   = to_date/10000 + "-" + to_date/100%100 + "-" +  to_date%100;
        if (hotelid.compareTo("all") == 0) 
        {
            query = "SELECT Sum(time01),Sum(time02),Sum(time03),Sum(time04),Sum(time05),Sum(time06),Sum(time07),Sum(time08),Sum(time09),Sum(time10),Sum(time11),Sum(time12),Sum(time13),Sum(time14),Sum(time15),Sum(time16),Sum(time17),Sum(time18),Sum(time19),Sum(time20),Sum(time21),Sum(time22),Sum(time23),Sum(time24),Sum(total) FROM access_mobile_detail,owner_user_hotel WHERE owner_user_hotel.hotelid = ?";
            query = query + " AND access_mobile_detail.hotel_id = owner_user_hotel.accept_hotelid";
            query = query + " AND owner_user_hotel.userid = ?";
        }
        else
        {
            query = "SELECT Sum(time01),Sum(time02),Sum(time03),Sum(time04),Sum(time05),Sum(time06),Sum(time07),Sum(time08),Sum(time09),Sum(time10),Sum(time11),Sum(time12),Sum(time13),Sum(time14),Sum(time15),Sum(time16),Sum(time17),Sum(time18),Sum(time19),Sum(time20),Sum(time21),Sum(time22),Sum(time23),Sum(time24),Sum(total) FROM access_mobile_detail WHERE hotel_id=?";
        }
        query = query + " AND acc_date>= ?";
        query = query + " AND acc_date<= ?";
        prestate    = connection.prepareStatement(query);
        if (hotelid.compareTo("all") == 0) 
        {
           prestate.setString(1,loginHotelId);
           prestate.setInt(2,ownerinfo.DbUserId);
           prestate.setString(3,FromDate);
           prestate.setString(4,ToDate);
        }
        else
        {
           prestate.setString(1,hotelid);
           prestate.setString(2,FromDate);
           prestate.setString(3,ToDate);
        }
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            for (i = 0;i < 24; i++)
            {
                t_total[i]   = result.getInt(i+1);
                if (total_max < t_total[i]) total_max = t_total[i];
            }
            total_total  = result.getInt(25);
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }

    NumberFormat  nf2;
    nf2    = new DecimalFormat("00");

//１．ダウンロードする時の決まりごととして、以下のソースを書く。
    response.setContentType("application/octetstream");

//２．ダウンロードするファイル名を指定(拡張子をcsvにする)
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
//３．ダウンロードするファイルをストリーム化する
    PrintWriter outstream = response.getWriter();


//  タイトル書き込み
    outstream.print(Title[type]);
    outstream.print(",");
    outstream.print(new String( hname.getBytes("Windows-31J"), "Windows-31J"));
    outstream.print(",");
    outstream.print(year + "年");
    if (month != 0)
    {
        outstream.print(month + "月");
    }
    if (day != 0)
    {
        outstream.print(day + "日");
    }
    outstream.print(",");
    outstream.print(de.getDate(1));
    outstream.print(",");
    outstream.println(de.getTime(0));

// タイトル
    outstream.print("時間帯,");
    outstream.println("アクセス数");

//明細
    for (i = 0;i < 24; i++)
    {
        outstream.print(" "+ t_time[i]);
        outstream.print(",");
        outstream.println(t_total[i]);
    }

//合計
    outstream.print("合計,");
    outstream.println(total_total);

// クローズ
    outstream.close();
%>
?
