<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/hpreport_ini.jsp" %>
<%@ include file="../csrf/refererCheck.jsp" %>
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
    java.sql.Date acc_date;
    int     yy    = 0;
    int     mm    = 0;
    int     dd    = 0;
    int     target_date   = 0;
    int     acc_mobile    = 0;
    int     acc_pc        = 0;
    int     acc_smart     = 0;
    int     acc_total     = 0;
    int     total_mobile  = 0;
    int     total_pc      = 0;
    int     total_smart   = 0;
    int     total_docomo  = 0;
    int     total_jphone  = 0;
    int     total_au      = 0;
    int     total_total   = 0;
    int     total_max     = Integer.parseInt(Max);
    String  weekname      = "";
    int[]   t_mobile = new int[31];
    int[]   t_pc     = new int[31];
    int[]   t_smart  = new int[31];
    int[]   t_total  = new int[31];
    String[]t_week   = new String[31];
    int[]   t_date   = new int[31];
    try
    {
        String FromDate = from_date/10000 + "-" + from_date/100%100 + "-" +  from_date%100;
        String ToDate   = to_date/10000 + "-" + to_date/100%100 + "-" +  to_date%100;
        if (hotelid.compareTo("all") == 0) 
        {
            query = "SELECT * FROM access_mobile_detail,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
            query = query + " AND access_mobile_detail.hotel_id = owner_user_hotel.accept_hotelid";
            query = query + " AND owner_user_hotel.userid = ?";
        }
        else
        {
            query = "SELECT * FROM access_mobile_detail WHERE hotel_id= ?";
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
        while( result.next() != false )
        {
            acc_date = result.getDate("acc_date");
            yy = acc_date.getYear() + 1900;
            mm = acc_date.getMonth()+ 1;
            dd = acc_date.getDate();
            i            = dd-1;
            target_date  = (yy * 10000) +(mm*100) + dd;
            acc_mobile   = result.getInt("docomo") + result.getInt("jphone") + result.getInt("au");
            acc_pc       = result.getInt("etc");
            acc_smart    = result.getInt("smart");
            if (target_date <= 20131031)
            {
               acc_pc    = acc_smart + acc_pc;
               acc_smart = 0;
            }
            acc_total    = result.getInt("total");
            weekname     = de.getWeekName(target_date);
            total_mobile = total_mobile + acc_mobile;
            total_docomo = total_docomo + result.getInt("docomo");
            total_jphone = total_jphone + result.getInt("jphone");
            total_au     = total_au     + result.getInt("au");
            total_pc     = total_pc     + acc_pc;
            total_smart  = total_smart  + acc_smart;
            total_total  = total_total  + acc_total;
            t_mobile[i]  = t_mobile[i]  + acc_mobile;
            t_pc[i]      = t_pc[i]      + acc_pc;
            t_smart[i]   = t_smart[i]   + acc_smart;
            t_total[i]   = t_total[i]   + acc_total;
            if (total_max < t_total[i]) total_max = t_total[i];
            t_week[i]    = weekname;
            t_date[i]    = dd;
            if (i > count )
            {
                count = i;
            }
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
    outstream.print(hname);
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
    outstream.print(",");
    outstream.print(de.getDate(1));
    outstream.print(",");
    outstream.println(de.getTime(0));

// タイトル
    outstream.print("日付,");
    outstream.print("曜日,");
    outstream.print("携帯,");
    outstream.print("スマホ,");
    outstream.print("PC他,");
    outstream.println("合計");

//明細
    for (i = 0;i <= count; i++)
    {
        if (t_week[i]!=null)
        {
        outstream.print(t_date[i]);
        outstream.print(",");
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
    }

//合計
    outstream.print("合計,,");
    outstream.print(total_mobile);
    outstream.print(",");
    outstream.print(total_smart);
    outstream.print(",");
    outstream.print(total_pc);
    outstream.print(",");
    outstream.println(total_total);

// クローズ
    outstream.close();
%>
?
