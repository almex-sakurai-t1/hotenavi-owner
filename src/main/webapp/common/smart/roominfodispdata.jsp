<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- 現在部屋情報表示処理 --%>

<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    // ホテルIDのセット
    String hotelid = request.getParameter("HotelId");
    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }

    // imedia_user のチェック
    int imedia_user = 0;

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        final String query = "SELECT * FROM owner_user WHERE hotelid = ? AND userid = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result = prestate.executeQuery();
        if( result.next() )
        {
            imedia_user = result.getInt("imedia_user");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(connection);
    }

    boolean timeChartFlag = false;
    try
    {
        final String query = "SELECT * FROM hotel WHERE hotel_id = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, hotelid);

        result = prestate.executeQuery();
        if( result.next() )
        {
            if (result.getInt("timechart_flag") == 1)
            {
                timeChartFlag = true;
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result, prestate, connection);
    }

    int i;
    int count;
    int now_ymd;
    int add_ymd;
    int now_time;
    int starttime = 0;
    DateEdit df;
    StringFormat sf;
    NumberFormat nf;

	// 計上日付取得
    add_ymd = ownerinfo.Addupdate;
	
    // IN/OUT組数取得（開始時刻取得のため）
    ownerinfo.InOutGetStartDate = add_ymd;
    ownerinfo.InOutGetEndDate = 0;
    ownerinfo.sendPacket0106();

	// 開始時刻
    starttime = ownerinfo.InOutTime[0] / 100;
 
    df = new DateEdit();
    sf = new StringFormat();

    // 現在日付・時刻取得(YYYYMMDD / HHMMSS)
    now_ymd = Integer.parseInt(df.getDate(2));
    now_time = Integer.parseInt(df.getTime(1));

    // 開始時刻が０時以外の場合は、現在計上日取得。０時の場合は今日の日付
    if(starttime == 0)
    {
        add_ymd = now_ymd;
    }
    if (DemoMode)
    {
        add_ymd = now_ymd;
    }
%>
<h2><%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>現在</h2>
<hr class="border">
<%-- 現在部屋情報表示 --%>

<table class="roomdetail">
<tr>
<th width="50%">ステータス</th><th>室数</th>
</tr>
<%
    for( i = 0 ; i < ownerinfo.OWNERINFO_ROOMSTATUSMAX ; i++ )
    {
        if( ownerinfo.StatusCount[i] != 0 )
        {
%>
<tr>
<td><%= sf.leftFitFormat(ownerinfo.StatusName[i], 10) %></td><td class="roomdetail"><%= sf.rightFitFormat(Integer.toString(ownerinfo.StatusCount[i]), 3) %></td>
</tr>
<%
        }
    }
%>
</table>
<center>
<h1>
(
<%
    if( ownerinfo.StatusEmptyFullMode == 1 )
    {
%>
        手動
<%
    }
    else
    {
%>
        自動
<%
    }
%>
/
<%
    if( ownerinfo.StatusEmptyFullState == 1 )
    {
%>
        空室
<%
    }
    else
    {
%>
        満室
<%
     }
%>
)
<%
  if( ownerinfo.StatusWaiting != 0 )
    {
%>
<br>
     ウェイティング:<%= ownerinfo.StatusWaiting %>&nbsp;組
<%
    }
%>
</h1>
</center>
<%-- 現在部屋情報表示ここまで --%>

<hr class="border" />
<ul class="link">
<div align="right">
<li><a href="<%= response.encodeURL("roomdetail.jsp?HotelId=" + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J")) %>">部屋詳細を見る</a></li>
<li><a href="<%= response.encodeURL("closeinout.jsp?HotelId=" + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J")) %>">締単位IN/OUT組数</a></li>
<li><a href="<%= response.encodeURL("salesinout.jsp?HotelId=" + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J") + "&Ymd=" + add_ymd) %>">IN/OUT組数</a></li>
<li><a href="<%= response.encodeURL("roomhistory.jsp?HotelId=" + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J")) %>">部屋ステータス遷移</a></li>
<%
    if(timeChartFlag)
   {
%>
<li><a href="<%= response.encodeURL("timechart.jsp?HotelId=" + URLEncoder.encode(hotelid != null ? hotelid : "", "Windows-31J")) %>">タイムチャート</a></li>
<%
    }
%>
</div>
</ul>
<%
    if (request.getParameter("jump") == null)
    {
%>
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="roominfo.jsp" />
  <jsp:param name="dispname" value="部屋情報" />
</jsp:include>
<%
    }
%>
