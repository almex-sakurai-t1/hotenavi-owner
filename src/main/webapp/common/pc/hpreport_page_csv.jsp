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

    String  Mem   = ReplaceString.getParameter(request,"Member");
    if     (Mem  == null) Mem = "";
    if     (Mem.compareTo("") != 0 && Mem.compareTo("mem_") != 0) Mem = "";

    int     i            = 0;
    int     count        = 0;
    int     per          = 0;
    int     total_total  = 0;
    int     total_max     = Integer.parseInt(Max);
    int[]   t_total      = new int[50];
    int     page99_total = 0;
    String[]t_page       = new String[50];
    String  page99       = "その他";

    //ページ名の取得
    try
    {
        if (hotelid.compareTo("all") == 0) 
        {
            query = "SELECT * FROM access_mobile_name,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
            query = query + " AND access_mobile_name.hotel_id = owner_user_hotel.accept_hotelid";
            query = query + " AND owner_user_hotel.userid = ?";
        }
        else
        {
            query = "SELECT * FROM access_mobile_name WHERE hotel_id=?";
        }
        prestate    = connection.prepareStatement(query);
        if (hotelid.compareTo("all") == 0) 
        {
           prestate.setString(1,loginHotelId);
           prestate.setInt(2,ownerinfo.DbUserId);
        }
        else
        {
           prestate.setString(1,hotelid);
        }

        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            for (i = 0;i < 9; i++)
            {
                t_page[i]   = result.getString(Mem + "page0"+(i+1));
            }
            for (i = 9;i < 50; i++)
            {
                t_page[i]   = result.getString(Mem + "page"+(i+1));
            }
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

    try
    {
        String FromDate = from_date/10000 + "-" + from_date/100%100 + "-" +  from_date%100;
        String ToDate   = to_date/10000 + "-" + to_date/100%100 + "-" +  to_date%100;
        query = "SELECT ";
        for (i = 0;i < 9; i++)
        {
            query = query + "Sum(" + Mem + "page0" + (i+1) + "),";
        }
        for (i = 9;i < 50; i++)
        {
            query = query + "Sum(" + Mem + "page" + (i+1) + "),";
        }
        query = query + "Sum(" + Mem + "page99),Sum(total)";
        if (hotelid.compareTo("all") == 0) 
        {
            query = query + " FROM access_mobile_detail,owner_user_hotel WHERE owner_user_hotel.hotelid = ?";
            query = query + " AND access_mobile_detail.hotel_id = owner_user_hotel.accept_hotelid";
            query = query + " AND owner_user_hotel.userid = ?";
        }
        else
        {
            query = query + " FROM access_mobile_detail WHERE hotel_id=?";
        }
        query = query + " AND acc_date>=?";
        query = query + " AND acc_date<=>?";
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
            for (i = 0;i < 50; i++)
            {
                if (t_page[i].compareTo("") == 0)
                {
                    page99_total  = page99_total + result.getInt(i+1);
                }
                else
                {
                    t_total[i]   = result.getInt(i+1);
                }
                total_total  = total_total + t_total[i];
                if (total_max < t_total[i]) total_max = t_total[i];
            }
            page99_total  = page99_total + result.getInt(51);
            if (total_max < page99_total) total_max = page99_total;
            total_total   = total_total + page99_total;
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
    outstream.print("ページ名,");
    outstream.println("アクセス数");

//明細
    for (i = 0;i < 50; i++)
    {
        if (t_total[i] != 0)
        {
            outstream.print(t_page[i]);
            outstream.print(",");
            outstream.println(t_total[i]);
        }
    }
    if (page99_total != 0)
    {
        outstream.print(page99);
        outstream.print(",");
        outstream.println(page99_total);
    }
//合計
    outstream.print("合計,");
    outstream.println(total_total);

// クローズ
    outstream.close();
%>
