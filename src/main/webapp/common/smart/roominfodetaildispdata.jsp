<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
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
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }
%>
<%
    int i;
    int count;
    int now_ymd;
    int now_time;
    DateEdit df;
    StringFormat sf;
    NumberFormat nf;

    df = new DateEdit();
    sf = new StringFormat();
    nf = new DecimalFormat("00");

    // 現在日付・時刻取得(YYYYMMDD / HHMMSS)
    now_ymd  = Integer.parseInt(df.getDate(2));
    now_time = Integer.parseInt(df.getTime(1));

    // ホスト種別取得
    int type = 0;

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        final String query = "SELECT * FROM hotel WHERE hotel_id = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, hotelid);

        result = prestate.executeQuery();
        if( result.next() )
        {
            type = result.getInt("host_kind");
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
%>
<h2><%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>現在</h2>
<hr class="border">

<%-- 部屋詳細情報表示 --%>
<table class="roomdetail2">
<tr>
<th class="roomdetail2" colspan="2"><%= ownerinfo.StatusDetailRoomName[0] %></th>
</tr>
<tr>
<th width="30%">ｽﾃｰﾀｽ</th><td><%= ownerinfo.StatusDetailStatusName[0] %>
<%
            if (ownerinfo.StatusDetailUserChargeMode[0] == 1)
            {
%>
				<img src="/common/mobile/image/rest.gif">
<%
            }
            else if (ownerinfo.StatusDetailUserChargeMode[0] == 2)
            {
%>
				<img src="/common/mobile/image/stay.gif">
<%
            }
%>
</td>
</tr>
<tr>
<th>経過</th><td><%= ownerinfo.StatusDetailElapseTime[0] / 60 %>:<%= nf.format(ownerinfo.StatusDetailElapseTime[0] % 60) %></td>
</tr>
<tr>
<th>入室</th>
<td>
<%
    if( ownerinfo.StateInDate[0] != 0 )
    {
%>
        <%= ownerinfo.StateInTime[0] / 100 %>時<%= nf.format(ownerinfo.StateInTime[0] % 100) %>分
<%
    }
%>
</td>
</tr>
<tr>
<th>退室</th>
<td>
<%
    if( ownerinfo.StateOutDate[0] == 0 )
    {
        if( ownerinfo.StateInDate[0] != 0 )
        {
%>
            <%= now_time / 10000 %>時<%= nf.format(now_time / 100 % 100) %>分(現在)
<%
        }
    }
    else
    {
%>
        <%= ownerinfo.StateOutTime[0] / 100 %>時<%= nf.format(ownerinfo.StateOutTime[0] % 100) %>分
<%
    }
%>
</td>
</tr>
</table>
<hr class="border">
<%
    // ホストがAMFの場合のみ表示
    if( type == 1 )
    {
%>
<table class="roomdetail2">
<tr>
<th colspan="2">支払状況</th>
</tr>
<tr>
<td width="50%">請求金額</td><td class="roomdetail2">\<%= Kanma.get(ownerinfo.DetailPayClaim) %></td>
</tr>
<tr class="roomdetail2">
<td>利用合計</td><td class="roomdetail2">\<%= Kanma.get(ownerinfo.DetailPayTotal) %></td>
</tr>
<%
        for( i = 0 ; i < ownerinfo.DetailPayCount ; i++ )
        {
%>
<tr>
<td><%= ownerinfo.DetailPayName[i] %></td><td class="roomdetail2">\<%= Kanma.get(ownerinfo.DetailPayMoney[i]) %></td>
</tr>
<%
        }
%>
</table>

<hr class="border">
<table class="roomdetail2">
<tr>
<th colspan="2">利用明細</th>
</tr>
<%
    if( ownerinfo.DetailUseCount != 0 )
    {
        for( i = 0 ; i < ownerinfo.DetailUseCount ; i++ )
        {
%>
		<tr>
		<td width="50%"><%= ownerinfo.DetailUseGoodsName[i] %></td>
		<td class="roomdetail2">\<%= Kanma.get(ownerinfo.DetailUseGoodsPrice[i]) %></td>
		</tr>
<%
            if( ownerinfo.DetailUseGoodsDiscount[i] != 0 )
            {
%>
				<tr>
				<td>(\<%= Kanma.get(ownerinfo.DetailUseGoodsRegularPrice[i]) %></td><td class="roomdetail2"><%= Kanma.get(ownerinfo.DetailUseGoodsDiscount[i]) %>%)</td>
				</tr>
<%
            }
        }
    }
	else
	{
	%>
		<tr>
		<td colspan="2">利用明細はありません。</td>
		</tr>
	<%
	}
%>
</table>
<hr class="border">
<table class="roomdetail2">
<tr>
<th colspan="3">商品明細</th>
<%
        if( ownerinfo.DetailGoodsCount != 0 )
        {
%>
<%
            for( i = 0 ; i < ownerinfo.DetailGoodsCount ; i++ )
            {
%>
			<tr>
			<td><%= ownerinfo.DetailGoodsName[i] %></td>
			<td class="roomdetail2"><%= nf.format(ownerinfo.DetailGoodsAmount[i]) %></td>
			<td class="roomdetail2">\<%= Kanma.get(ownerinfo.DetailGoodsPrice[i]) %></td>
			</tr>
<%
            }
        }
		else
		{
		%>
			<tr>
			<td colspan="3">商品明細はありません。</td>
			</tr>
		<%
		}
%>
</table>

<hr class="border">
<%
    }
    // ホストがAMFの場合のみ表示ここまで
%>

<table class="roomdetail2">
<tr>
<th colspan="2">ﾒﾝﾊﾞｰ情報</th>
</tr>
<%
    if( ownerinfo.MemberCustomId[0].compareTo("") != 0 )
    {
%>
<tr>
<td>番　号</td><td class="roomdetail2"><%= ownerinfo.MemberCustomId[0] %></td>
</tr>
<tr>
<td>ﾆｯｸﾈｰﾑ</td><td class="roomdetail2"><%= ownerinfo.MemberNickName[0] %></td>
</tr>
<tr>
<td>誕生日</td><td class="roomdetail2"><%= ownerinfo.MemberBirthday1[0] / 100 % 100 %>/<%= ownerinfo.MemberBirthday1[0] % 100 %>
<%if( ownerinfo.MemberBirthday1[0] != 0 ){%><br /><%= ownerinfo.MemberBirthday2[0] / 100 % 100 %>/<%= ownerinfo.MemberBirthday2[0] % 100 %><%}%></td>
</tr>
<tr>
<td>記念日</td><td class="roomdetail2"><%= ownerinfo.MemberMemorial1[0] / 100 % 100 %>/<%= ownerinfo.MemberMemorial1[0] % 100 %>
<%if( ownerinfo.MemberMemorial1[0] != 0 ){%><br /><%= ownerinfo.MemberMemorial2[0] / 100 % 100 %>/<%= ownerinfo.MemberMemorial2[0] % 100 %><%}%></td>
</tr>
<tr>
<td>ﾎﾟｲﾝﾄ</td><td class="roomdetail2"><%= Kanma.get(ownerinfo.MemberPoint[0]) %></td>
</tr>
<tr>
<td>ﾗﾝｸ</td><td class="roomdetail2"><%= ownerinfo.MemberRankName[0] %></td>
</tr>
</table>
<hr class="border">
<table class="roomdetail2">
<tr>
<th colspan="2">ﾒﾓ</th>
</tr>
<tr>
<td>連絡</td><td class="roomdetail2"><%= ownerinfo.MemberContact1[0] %><%= ownerinfo.MemberContact2[0] %></td>
</tr>
<tr>
<td>警告</td><td class="roomdetail2"><%= ownerinfo.MemberWarning1[0] %><%= ownerinfo.MemberWarning2[0] %><br></td>
</tr>
</table>
<%
    }
	else
	{
	%>
	<tr>
	<td colspan="2">ﾒﾝﾊﾞｰ情報はありません。</td>
	</tr>
	</table>
	<%
	}
%>

<%-- 部屋詳細情報表示ここまで --%>
<%
//ルームコードをクリアする
        ownerinfo.RoomCode = 0;

%>
