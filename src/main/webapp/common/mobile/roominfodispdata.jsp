<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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
    String       hotelid;
    // ホテルIDのセット
    hotelid = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int               imedia_user = 0;

    // imedia_user のチェック
    try
    {
        connection        = DBConnection.getConnection();
        final String query = "SELECT * FROM owner_user WHERE hotelid = ? AND userid = ? ";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result      = prestate.executeQuery();
        if( result.next() )
        {
            imedia_user      = result.getInt("imedia_user");
        }
    }
    catch( Exception e )
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    boolean TimeChartFlag = false;
    // imedia_user のチェック
    try
    {
        final String query = "SELECT * FROM hotel WHERE hotel_id = ? ";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result != null )
        {
            if( result.next() )
            {
                if (result.getInt("timechart_flag") == 1)
                {
                    TimeChartFlag = true;
                }
            }
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
<%
    int            i;
    int            count;
    int            now_ymd;
    int            add_ymd;
    int            now_time;
    int            starttime = 0;
    DateEdit        df;
    StringFormat    sf;
    NumberFormat    nf;

	// 計上日付取得
    add_ymd         = ownerinfo.Addupdate;


    // IN/OUT組数取得（開始時刻取得のため）
    ownerinfo.InOutGetStartDate = add_ymd;
    ownerinfo.InOutGetEndDate = 0;
    ownerinfo.sendPacket0106();

	// 開始時刻
    starttime  = ownerinfo.InOutTime[0] / 100;

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
<%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>現在<br>
<hr>
<%-- 現在部屋情報表示 --%>
<%
    for( i = 0 ; i < ownerinfo.OWNERINFO_ROOMSTATUSMAX ; i++ )
    {
        if( ownerinfo.StatusCount[i] != 0 )
        {
%>
<pre><%= sf.leftFitFormat(ownerinfo.StatusName[i], 10) %>&nbsp;<%= sf.rightFitFormat(Integer.toString(ownerinfo.StatusCount[i]), 3) %></pre>
<%
        }
    }
%>
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
     ｳｪｲﾃｨﾝｸﾞ:<%= ownerinfo.StatusWaiting %>&nbsp;組
<%
    }
%>
<%-- 現在部屋情報表示ここまで --%>

<div align="right">
<a href="<%= response.encodeURL("roomdetail.jsp?HotelId=" + hotelid) %>">部屋詳細を見る</a><br>
<a href="<%= response.encodeURL("closeinout.jsp?HotelId=" + hotelid) %>">締単位IN/OUT組数</a><br>
<a href="<%= response.encodeURL("salesinout.jsp?HotelId=" + hotelid + "&Ymd=" + add_ymd) %>">IN/OUT組数</a><br>
<a href="<%= response.encodeURL("roomhistory.jsp?HotelId=" + hotelid) %>">部屋ｽﾃｰﾀｽ遷移</a><br>
<%
    if(imedia_user == 1 || TimeChartFlag)
    {
%>
<a href="<%= response.encodeURL("timechart.jsp?HotelId=" + hotelid) %>">ﾀｲﾑﾁｬｰﾄ</a><br>
<%
    }
%>
</div>
<%
    if (ReplaceString.getParameter(request,"jump") == null)
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
