<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.*" %><%@ page import="java.util.*" %><%@ page import="java.text.*" %><%@ page import="com.hotenavi2.common.*" %><%@ page import="jp.happyhotel.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/qareport_ini.jsp" %>
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String hotelid = (String)session.getAttribute("SelectHotel");
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
    try
    {
        query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
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

    String     msg_subtitle    = "";
    String     msg             = "";
    String     msg_sub         = "";

    java.sql.Date start;
    java.sql.Date end;

    int start_master_date = 0;
    int start_master_year = 0;
    int start_master_month = 0;
    int start_master_day = 0;
    int end_master_date = 0;
    int end_master_year = 0;
    int end_master_month = 0;
    int end_master_day = 0;
    int member_flag     = 0;
    // アンケート期間・メッセージ読み込み
    try
    {
        query = "SELECT * FROM question_master WHERE hotel_id=?";
        query = query + " AND id=?";
        prestate        = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        prestate.setInt(2,id);
        result          = prestate.executeQuery();
        if( result.next() != false )
        {
            member_flag  = result.getInt("member_flag");
            msg_subtitle = result.getString("msg");
            start = result.getDate("start");
            start_master_year  = start.getYear() + 1900;
            start_master_month = start.getMonth() + 1;
            start_master_day   = start.getDate();
            start_master_date  = start_master_year*10000 + start_master_month*100 + start_master_day;
            end   = result.getDate("end");
            end_master_year  = end.getYear() + 1900;
            end_master_month = end.getMonth() + 1;
            end_master_day   = end.getDate();
            end_master_date  = end_master_year*10000 + end_master_month*100 + end_master_day;
            msg              = result.getString("q" + q_id + "_msg");
            type             = result.getInt("q" + q_id );
            type             = type % 10;
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
    if  (msg.length() < 2)
    {
        try
        {
            query = "SELECT * FROM question_data WHERE hotel_id=?";
            query = query + " AND id=?";
            query = query + " AND q_id=?";
            prestate        = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, id);
            prestate.setString(3,"q"+q_id);
            result          = prestate.executeQuery();
            if( result.next() != false )
            {
                msg = result.getString("msg");
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
    int     count_record = 0;
    int     count_total  = 0;
    try
    {
        query = "SELECT count(*) FROM question_answer WHERE hotel_id=?";
        query = query + " AND id=?";
        query = query + " AND q_id=?";
        query = query + " AND ans_date>=?";
        query = query + " AND ans_date<=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setInt(2, id);
        prestate.setString(3,"q"+q_id);
        prestate.setInt(4, date_from);
        prestate.setInt(5, date_to);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
           count_total = result.getInt(1);
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
        query = "SELECT count(*) FROM question_answer WHERE hotel_id=?";
        query = query + " AND id=?";
        query = query + " AND q_id=?";
        query = query + " AND ans_date>=?";
        query = query + " AND ans_date<=?";
        query = query + " AND answer != ','";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setInt(2, id);
        prestate.setString(3,"q"+q_id);
        prestate.setInt(4, date_from);
        prestate.setInt(5, date_to);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
           count_record = result.getInt(1);
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
    query = "SELECT * FROM question_answer WHERE hotel_id=?";
    query = query + " AND id=?";
    query = query + " AND q_id=?";
    query = query + " AND ans_date>=?";
    query = query + " AND ans_date<=?";
    query = query + " AND answer != ','";

    NumberFormat  nf2;
    nf2    = new DecimalFormat("00");

//１．ダウンロードする時の決まりごととして、以下のソースを書く。
    response.setContentType("application/octetstream");

//２．ダウンロードするファイル名を指定(拡張子をcsvにする)
    String filename = "answer_" + hotelid + "_" + id + "_" + q_id + "_" + nowdate;
    filename = filename + ".csv";
    response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
//３．ダウンロードするファイルをストリーム化する
    PrintWriter outstream = response.getWriter();

//  タイトル書き込み
    outstream.print("アンケートレポート");
    outstream.print(",");
    outstream.print(",");
    outstream.print(de.getDate(1));
    outstream.print(",");
    outstream.println(de.getTime(0));

    outstream.print(new String( hname.getBytes("Shift_JIS"), "Windows-31J"));
    outstream.print(",");
    outstream.print(",");
    outstream.print(",");
    if (member_flag == 1)
    {
        outstream.print("(メンバー用）");
    }
    outstream.println("");
    outstream.println(new String( msg.getBytes("Shift_JIS"), "Windows-31J"));
    outstream.print(StartYear  + "年");
    outstream.print(StartMonth + "月");
    outstream.print(StartDay   + "日から,");
    outstream.print(EndYear    + "年");
    outstream.print(EndMonth   + "月");
    outstream.print(EndDay     + "日まで,");
    outstream.print("回答者総数："+ count_total + ",");
    outstream.print("回答数："+ count_record );
    outstream.println("");

// タイトル
    outstream.print("回答内容,");
    outstream.print("日付,");
    outstream.print("時刻,");
    outstream.println("メンバー");

//明細
    try
    {
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setInt(2, id);
        prestate.setString(3,"q"+q_id);
        prestate.setInt(4, date_from);
        prestate.setInt(5, date_to);
       result      = prestate.executeQuery();
        while( result.next() != false )
        {
            String answer = result.getString("answer");
            answer = answer + ",,,,";
            answer = answer.replace(",,,,,","");
            answer = answer.replace("&quot;","'");
            outstream.print('"' + answer + '"');
            outstream.print(",");
            outstream.print(result.getDate("ans_date").toString());
            outstream.print(",");
            outstream.print(result.getTime("ans_time").toString());
            outstream.print(",");
            outstream.println(result.getString("custom_id"));
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

// クローズ
    outstream.close();
%>
?
