<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.DBSync" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    DateEdit  de = new DateEdit();
    int    last_update         = Integer.parseInt(de.getDate(2));
    int    last_uptime         = Integer.parseInt(de.getTime(1));
    int    hapihote_id         = 0;
%>

<%
    // ﾊﾟﾗﾒｰﾀ取得
    String hotelid = ReplaceString.getParameter(request,"HotelId");
	if( hotelid != null && !CheckString.hotenaviIdCheck(hotelid))
	{
        response.sendError(400);
        return;
	}
    String coupon_type = ReplaceString.getParameter(request,"CouponType");
    String id = ReplaceString.getParameter(request,"Id");
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int retresult     = 0;

    String available           = ReplaceString.getParameter(request,"available");
    if    (available == null)    available = "0";
    String use_count           = ReplaceString.getParameter(request,"use_count");
    if    (use_count == null)    use_count = "0";
    String use_start_day       = ReplaceString.getParameter(request,"use_start_day");
    if    (use_start_day == null)  use_start_day = "0";
    String use_end_day         = ReplaceString.getParameter(request,"use_end_day");
    if    (use_end_day   == null)  use_end_day = "0";


    // 日付ﾃﾞｰﾀの編集
    int start_ymd = 0;
    int end_ymd = 0;

    int start_yy = Integer.parseInt(ReplaceString.getParameter(request,"col_start_yy"));
    int start_mm = Integer.parseInt(ReplaceString.getParameter(request,"col_start_mm"));
    int start_dd = Integer.parseInt(ReplaceString.getParameter(request,"col_start_dd"));
    start_ymd = start_yy*10000 + start_mm*100 + start_dd;

    int end_yy = Integer.parseInt(ReplaceString.getParameter(request,"col_end_yy"));
    int end_mm = Integer.parseInt(ReplaceString.getParameter(request,"col_end_mm"));
    int end_dd = Integer.parseInt(ReplaceString.getParameter(request,"col_end_dd"));
    end_ymd = end_yy*10000 + end_mm*100 + end_dd;

    int  all_seq    = 0;
    int  coupon_no  = 0;
    int  master_seq = 0;

    // メンバー専用
    int memberonly = 0;
    if( ReplaceString.getParameter(request,"col_member_only") != null )
    {
        memberonly = Integer.parseInt( ReplaceString.getParameter(request,"col_member_only") );
    }
    // 携帯表示
    int disp_mobile = 0;
    if( ReplaceString.getParameter(request,"disp_mobile") != null )
    {
        disp_mobile = Integer.parseInt( ReplaceString.getParameter(request,"disp_mobile") );
    }
	String query = "";
    connection  = DBConnection.getConnection();
    try
    {
        //ハピホテのIDを調べる
        query = "SELECT * FROM hh_hotel_basic WHERE hotenavi_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() )
        {
           hapihote_id = result.getInt("id");
        }
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(result);
    }

    String restrict1 = ReplaceString.getParameter(request,"col_restrict1");
    restrict1 = restrict1.replaceAll("\r\n|\r|\n", "<br>");
    restrict1 = ReplaceString.SQLEscape(restrict1);

    String restrict2 = ReplaceString.getParameter(request,"col_restrict2");
    restrict2 = restrict2.replaceAll("\r\n|\r|\n", "<br>");
    restrict2 = ReplaceString.SQLEscape(restrict2);

    if( id.compareTo("0") == 0 )
    {
        try
        {
            //ホテル連番管理マスターへの書き込み
            query = "INSERT INTO hotenavi.hh_hotel_system_seq SET ";
            query = query + "kind=2"                                         + ", ";
            query = query + "seq=0"                                          + ", ";
            query = query + "id=?,";
            query = query + "regist_date="  + last_update                    + ", ";
            query = query + "regist_time="  + last_uptime                    + "  ";
            prestate    = connection.prepareStatement(query);
            prestate.setInt(1, hapihote_id);
            retresult   = prestate.executeUpdate();
            DBSync.publish(prestate.toString().split(":",2)[1]);
            DBSync.publish(prestate.toString().split(":",2)[1],true);
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(prestate);
        }

        try
        {
            //書き込んだホテル連番管理マスターから、all_seq を読み込む
            query = "SELECT * FROM hh_hotel_system_seq WHERE kind=2 AND id=?";
            query = query + " ORDER BY seq DESC";
            prestate    = connection.prepareStatement(query);
            prestate.setInt(1, hapihote_id);
            result      = prestate.executeQuery();
            if( result.next() )
            {
               all_seq = result.getInt("seq");
            }
        }
        catch( Exception e )
        {
        	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(result);
        }

        try
        {
            //クーポンへの書き込み
            query = "INSERT INTO edit_coupon SET ";
            query = query + "hotelid=?, ";
            query = query + "coupon_type=?, ";
            query = query + "disp_idx=?, ";
            query = query + "start_date=?,";
            query = query + "end_date=?, ";
            query = query + "hotel_name=?, ";
            query = query + "coupon_name=?, ";
            query = query + "contents1=?, ";
            query = query + "contents2=?, ";
            query = query + "restrict1=?, ";
            query = query + "restrict2=?, ";
            query = query + "use_limit=?, ";
            query = query + "teleno=?, ";
            query = query + "member_only=?, ";
            query = query + "all_seq =?, ";
            query = query + "disp_mobile=?, ";
            query = query + "disp_mobile_message=? ";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, Integer.parseInt(coupon_type));
            prestate.setInt(3, Integer.parseInt(ReplaceString.getParameter(request,"col_disp_idx")));
            prestate.setInt(4, start_ymd);
            prestate.setInt(5, end_ymd);
            prestate.setString(6, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_hotel_name")));
            prestate.setString(7, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_coupon_name")));
            prestate.setString(8, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_contents1")));
            prestate.setString(9, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_contents2")));
            prestate.setString(10, restrict1);
            prestate.setString(11, restrict2);
            prestate.setString(12, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_use_limit")));
            prestate.setString(13, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_teleno")));
            prestate.setInt(14, memberonly);
            prestate.setInt(15, all_seq);
            prestate.setInt(16, disp_mobile);
            prestate.setString(17, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"disp_mobile_message")));
            retresult   = prestate.executeUpdate();
        }
        catch( Exception e )
        {
        	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(prestate);
        }

        try
        {
            //書き込んだクーポンデータから、coupon_no を読み込む
            query = "SELECT * FROM edit_coupon WHERE hotelid=?";
            query = query + " ORDER BY id DESC";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            result      = prestate.executeQuery();
            if( result.next() )
            {
                coupon_no = result.getInt("id");
            }
        }
        catch( Exception e )
        {
        	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(result);
        }

        try
        {
            //クーポンマスターのseq最大値を読み込む
           	query = "SELECT * FROM hh_master_coupon WHERE id=?";
            query = query + " ORDER BY seq DESC";
            prestate    = connection.prepareStatement(query);
            prestate.setInt(1, hapihote_id);
            result      = prestate.executeQuery();
            if( result.next() )
            {
                master_seq = result.getInt("seq") + 1;
            }
        }
        catch( Exception e )
        {
        	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(result);
        }

        try
        {
            //クーポンマスターへの書き込み
            query = "INSERT INTO hotenavi.hh_master_coupon SET ";
            query = query + "id=?, ";
            query = query + "service_flag=?, ";
            query = query + "coupon_no=?, ";
            query = query + "seq=?, ";
            query = query + "available=?, ";
            query = query + "use_count=?, ";
            query = query + "start_day=?, ";
            query = query + "end_day=? ";
            prestate    = connection.prepareStatement(query);
            prestate.setInt(1, hapihote_id);
            prestate.setInt(2, 2);
            prestate.setInt(3, coupon_no);
            prestate.setInt(4, master_seq);
            prestate.setInt(5, Integer.parseInt(available));
            prestate.setInt(6, Integer.parseInt(use_count));
            prestate.setInt(7, Integer.parseInt(use_start_day));
            prestate.setInt(8, Integer.parseInt(use_end_day));
            retresult   = prestate.executeUpdate();
            DBSync.publish(prestate.toString().split(":",2)[1]);
            DBSync.publish(prestate.toString().split(":",2)[1],true);
        }
        catch( Exception e )
        {
        	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(prestate);
        }
    }
    else
    {
        try
        {
            //クーポンの書き変え
            query = "UPDATE edit_coupon SET disp_idx=?, ";
            query = query + "start_date=?, ";
            query = query + "end_date=?, ";
            query = query + "hotel_name=?, ";
            query = query + "coupon_name=?, ";
            query = query + "contents1=?, ";
            query = query + "contents2=?, ";
            query = query + "restrict1=?, ";
            query = query + "restrict2=?, ";
            query = query + "use_limit=?, ";
            query = query + "teleno=?, ";
            query = query + "member_only=?, ";
            query = query + "disp_mobile=?, ";
            query = query + "disp_mobile_message=? ";
            query = query + " WHERE hotelid=? AND coupon_type=? AND id=?";
            prestate    = connection.prepareStatement(query);
            prestate.setInt(1, Integer.parseInt(ReplaceString.getParameter(request,"col_disp_idx")));
            prestate.setInt(2, start_ymd);
            prestate.setInt(3, end_ymd);
            prestate.setString(4, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_hotel_name")));
            prestate.setString(5, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_coupon_name")));
            prestate.setString(6, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_contents1")));
            prestate.setString(7, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_contents2")));
            prestate.setString(8, restrict1);
            prestate.setString(9, restrict2);
            prestate.setString(10, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_use_limit")));
            prestate.setString(11, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"col_teleno")));
            prestate.setInt(12, memberonly);
            prestate.setInt(13, disp_mobile);
            prestate.setString(14, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"disp_mobile_message")));
            prestate.setString(15, hotelid);
            prestate.setInt(16, Integer.parseInt(coupon_type));
            prestate.setInt(17, Integer.parseInt(id));
            retresult   = prestate.executeUpdate();
        }
        catch( Exception e )
        {
        	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(prestate);
        }
        try
        {
            //クーポンマスターの書き変え
            query = "UPDATE hotenavi.hh_master_coupon SET ";
            query = query + "available=?, ";
            query = query + "use_count=?, ";
            query = query + "start_day=?, ";
            query = query + "end_day=? ";
            query = query + " WHERE id=? AND service_flag=? AND coupon_no=?";
            prestate    = connection.prepareStatement(query);
            prestate.setInt(1, Integer.parseInt(available));
            prestate.setInt(2, Integer.parseInt(use_count));
            prestate.setInt(3, Integer.parseInt(use_start_day));
            prestate.setInt(4, Integer.parseInt(use_end_day));
            prestate.setInt(5, hapihote_id);
            prestate.setInt(6, 2);
            prestate.setInt(7, Integer.parseInt(id));
            retresult   = prestate.executeUpdate();
            DBSync.publish(prestate.toString().split(":",2)[1]);
            DBSync.publish(prestate.toString().split(":",2)[1],true);
        }
        catch( Exception e )
        {
        	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(prestate);
        }
    }

    DBConnection.releaseResources(connection);
%>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>クーポン登録結果</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/coupon.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/coupon.js"></script>
</head>
<%
    if( retresult == 1  )
    {
%>
<body background="../../common/pc/image/bg.gif" onload="window.opener.location.reload();window.close();">
<%
    }
    else
    {
%>
<body background="../../common/pc/image/bg.gif" >
        登録に失敗しました。<br>
<INPUT name="submit_ret" type=submit value=戻る onClick="history.back();">
<%
    }
%>
</body>
</html>

