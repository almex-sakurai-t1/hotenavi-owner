<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    int             i;
    int             count;
    int             pageno;
    int             dispno;
    int             tmcount;
    boolean         ret;
    boolean         already;
    String          param_page;
    String          param_count;
    String          param_tmcount;
    NumberFormat    nf;
    StringFormat    sf;

    nf = new DecimalFormat("00");
    sf = new StringFormat( );

    param_page = ReplaceString.getParameter(request,"page");
    if( param_page == null )
    {
        param_page = "0";
    }
    param_count = ReplaceString.getParameter(request,"count");
    if( param_count == null )
    {
        param_count = "3";
    }
    param_tmcount = ReplaceString.getParameter(request,"tmcount");
    if( param_tmcount == null )
    {
        param_tmcount = "1";
    }
    String[] dates = new String[]{ param_page, param_count, param_tmcount };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }

    // 取得日付
    int cal_date  = ownerinfo.InOutGetStartDate;
    int cal_year  = cal_date/10000;
    int cal_month = cal_date/100%100;
    int cal_day   = cal_date%100;

    pageno = Integer.parseInt(param_page);
    dispno = Integer.parseInt(param_count);
    tmcount = Integer.parseInt(param_tmcount);
    String param_date = "&Year="+cal_year+"&Month="+cal_month+"&Day="+cal_day+"&count="+dispno+"&tmcount="+tmcount;

    String loginHotelId =  (String)session.getAttribute("HotelId");

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    try
    {
        connection        = DBConnection.getConnection();
        final String query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid = ?"
            + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid"
            + " AND hotel.plan <= 2"
            + " AND hotel.plan >= 1"
            + " AND owner_user_hotel.userid = ? "
            + " ORDER BY hotel.sort_num,hotel.hotel_id";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result      = prestate.executeQuery();
        if (result != null)
        {
            count = 0;
            already = false;
            ret = result.first();
            // 5件*page分移動
            ret = result.relative(pageno * dispno);
            // 管理店舗数分ループする
            while( ret != false )
            {
                ownerinfo.HotelId = result.getString("accept_hotelid");
                ownerinfo.sendPacket0146();
                ownerinfo.sendPacket0106();
                if( count != 0 )
                {
%>
<div align="right"><a href="#Top">ﾍﾟｰｼﾞTOPへ</a></div>
<hr>
<%
                }
%>
<%-- ホテル名称表示 --%>
<%= result.getString("name") %><br>
<%-- 部屋数表示 --%>
部屋数:<%= ownerinfo.RoomHistoryRoomCount %><br>
<%= ownerinfo.RoomHistoryDate / 10000 %>/<%= nf.format(ownerinfo.RoomHistoryDate / 100 % 100) %>/<%= nf.format(ownerinfo.RoomHistoryDate % 100) %>分<br>
時間&nbsp;空&nbsp;在&nbsp;準&nbsp;止&nbsp;IN&nbsp;OT<br>
<%
                for( i = 0 ; i < 24 ; i += tmcount )
                {
%>
            <%= nf.format(ownerinfo.RoomHistoryTime[i] / 100) %>時&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryEmpty[i]), 2) %>&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryExist[i]), 2) %>&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryClean[i]), 2) %>&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryStop[i]), 2) %>&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.InOutIn[i]), 2) %>&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.InOutOut[i]), 2) %><br>
<%
                // 真ん中に再度タイトル入れる(1時間おきのみ)
                    if( i == 11 && tmcount == 1 )
                    {
%>
時間&nbsp;空&nbsp;在&nbsp;準&nbsp;止&nbsp;IN&nbsp;OT<br>
<%
                    }
                }
                count++;
            // 5件超えた場合次ページへ
                if( count >= dispno && result.isLast() == false )
                {
%>
<hr>
<div align="right">
<a href="<%= response.encodeURL("allstoreroom.jsp") %>?page=<%= pageno + 1 %><%=param_date%>">次ﾍﾟｰｼﾞ</a>
</div>
<%
                    if( pageno >= 1 )
                    {
%>
<div align="left">
<a href="<%= response.encodeURL("allstoreroom.jsp") %>?page=<%= pageno - 1 %><%=param_date%>">前ﾍﾟｰｼﾞ</a>
</div>
<%
                    }
%>
<%
                    already = true;
                    break;
                }
                ret = result.next();
            }
            if( count == 0 )
            {
%>
管理するホテルがありません<br>
<%
            }
            else
            {
                if( pageno >= 1 && already == false )
                {
%>
<hr>
<div align="left">
<a href="<%= response.encodeURL("allstoreroom.jsp") %>?page=<%= pageno - 1 %><%=param_date%>">前ﾍﾟｰｼﾞ</a>
</div>
<%
                }
            }
        }
        else
        {
%>
管理するホテルがありません<br>
<%
        }
    }
    catch( Exception e )
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>
