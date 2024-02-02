<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.PrintWriter" %><%@ page import="java.sql.*" %><%@ page import="java.util.*" %><%@ page import="java.text.*" %><%@ page import="com.hotenavi2.common.*" %><%@ page import="jp.happyhotel.common.ReplaceString" %>
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


    Connection        connection_sub  = null;
    PreparedStatement prestate_sub    = null;
    ResultSet         result_sub      = null;
    connection_sub  = DBConnection.getConnection();

    int     i     = 0;
    int     count = 0;
    int     per   = 0;
    String[]t_msg    = new String[30];
    int[]   t_count  = new int[30];
    int     max   = 0;
    int     total = 0;
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
           count = result.getInt(1);
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
        query = "SELECT * FROM question_data WHERE hotel_id=?";
        query = query + " AND id=?";
        query = query + " AND q_id=?";
        prestate        = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setInt(2, id);
        prestate.setString(3,"q"+q_id);
        result          = prestate.executeQuery();

        connection_sub  = DBConnection.getConnection();
        while( result.next() != false )
        {
            t_msg[(result.getInt("sub_id")-1)] = result.getString("msg");
            query = "SELECT count(*) FROM question_answer WHERE hotel_id=?";
            query = query + " AND id=?";
            query = query + " AND q_id=?";
            query = query + " AND ans_date>=?";
            query = query + " AND ans_date<=?";
            query = query + " AND (answer LIKE '" + result.getInt("value") + ",%' OR answer LIKE '%," + result.getInt("value") + ",%')";
            prestate_sub    = connection_sub.prepareStatement(query);
            prestate_sub.setString(1, hotelid);
            prestate_sub.setInt(2, id);
            prestate_sub.setString(3,"q"+q_id);
            prestate_sub.setInt(4, date_from);
            prestate_sub.setInt(5, date_to);
            result_sub      = prestate_sub.executeQuery();
            if( result_sub.next() != false )
            {
                t_count[(result.getInt("sub_id")-1)] = result_sub.getInt(1);
                total = total + t_count[(result.getInt("sub_id")-1)];
                if (t_count[(result.getInt("sub_id")-1)]>max)
                {
                    max = t_count[(result.getInt("sub_id")-1)];
                }
            }
            DBConnection.releaseResources(result_sub);
            DBConnection.releaseResources(prestate_sub);
        }
        DBConnection.releaseResources(connection_sub);
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
    if (msg.length() < 2)
    {
        msg = t_msg[0];
    }

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
    outstream.print(de.getDate(1));
    outstream.print(",");
    outstream.println(de.getTime(0));
    outstream.print(new String( hname.getBytes("Shift_JIS"), "Windows-31J"));
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
    outstream.println(EndDay     + "日まで");

// タイトル
    outstream.print("選択肢,");
    outstream.print("票数,");
    outstream.println("比率");

//明細
    for (i = 0;i < 30; i++)
    {
        if(t_msg[i]!= null)
        {
            outstream.print(t_msg[i]);
            outstream.print(",");
            outstream.print(t_count[i]);
            outstream.print(",");
            per        = 0;
            if (count != 0)
            {
                per = Math.round((float)t_count[i]*1000 /(float)count);
            }
            if (count != 0)
            { 
               outstream.print(per/10 + "." + per % 10 + "%");
            }
            outstream.println("");
        }
    }

//合計
    outstream.print("合　計（回答数："+ count + "）,");
    outstream.print(total);
    outstream.print(",");
    per        = 0;
    if (count != 0)
    {
        per = Math.round((float)total*1000 /(float)count);
    }
    if (count != 0)
    { 
         outstream.print(per/10 + "." + per % 10 + "%");
    }
    outstream.println("");

// クローズ
    outstream.close();
%>
?
