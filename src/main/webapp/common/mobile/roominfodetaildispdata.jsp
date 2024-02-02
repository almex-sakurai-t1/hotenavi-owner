<%@ page contentType="text/html; charset=Windows-31J" %>
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
    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }
%>
<%
    int            i;
    int            count;
    int            now_ymd;
    int            now_time;
    DateEdit        df;
    StringFormat    sf;
    NumberFormat    nf;

    df = new DateEdit();
    sf = new StringFormat();
    nf = new DecimalFormat("00");

    // 現在日付・時刻取得(YYYYMMDD / HHMMSS)
    now_ymd = Integer.parseInt(df.getDate(2));
    now_time = Integer.parseInt(df.getTime(1));

    // ホスト種別取得
    int type = 0;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    try
    {
        final String query = "SELECT * FROM hotel WHERE hotel_id = ? ";
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);

        result      = prestate.executeQuery();
        if( result != null )
        {
            if( result.next() != false )
            {
                type = result.getInt("host_kind");
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
<%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>現在<br>
<hr>
<%-- 部屋詳細情報表示 --%>
<%= ownerinfo.StatusDetailRoomName[0] %>&nbsp;<%= ownerinfo.StatusDetailStatusName[0] %>
<%
            if (ownerinfo.StatusDetailUserChargeMode[0] == 1)
            {
%><img src="/common/mobile/image/rest.gif">
<%
            }
            else if (ownerinfo.StatusDetailUserChargeMode[0] == 2)
            {
%><img src="/common/mobile/image/stay.gif">
<%
            }
%><br>
経過:&nbsp;<%= ownerinfo.StatusDetailElapseTime[0] / 60 %>:<%= nf.format(ownerinfo.StatusDetailElapseTime[0] % 60) %><br>
入室:
<%
    if( ownerinfo.StateInDate[0] != 0 )
    {
%>
        <%= ownerinfo.StateInTime[0] / 100 %>時<%= nf.format(ownerinfo.StateInTime[0] % 100) %>分
<%
    }
%>
<br>

退室:
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
<hr>

<%
    // ホストがAMFの場合のみ表示
    if( type == 1 )
    {
%>
[支払状況]<br>
請求金額:\<%= Kanma.get(ownerinfo.DetailPayClaim) %><br>
利用合計:\<%= Kanma.get(ownerinfo.DetailPayTotal) %><br>
<br>
<%
        for( i = 0 ; i < ownerinfo.DetailPayCount ; i++ )
        {
%>
<%= ownerinfo.DetailPayName[i] %>:\<%= Kanma.get(ownerinfo.DetailPayMoney[i]) %><br>
<%
        }
%>

<hr>
[利用明細]
<%
        for( i = 0 ; i < ownerinfo.DetailUseCount ; i++ )
        {
%>
<br>
<%= ownerinfo.DetailUseGoodsName[i] %><br>
&nbsp;&nbsp;\<%= Kanma.get(ownerinfo.DetailUseGoodsPrice[i]) %>
<%
            if( ownerinfo.DetailUseGoodsDiscount[i] != 0 )
            {
%>
(\<%= Kanma.get(ownerinfo.DetailUseGoodsRegularPrice[i]) %>　<%= Kanma.get(ownerinfo.DetailUseGoodsDiscount[i]) %>%)
<%
            }
        }
%>

<br>
<hr>
[商品明細]

<%
        if( ownerinfo.DetailGoodsCount != 0 )
        {
%>
<%
            for( i = 0 ; i < ownerinfo.DetailGoodsCount ; i++ )
            {
%>
<br>
<%= ownerinfo.DetailGoodsName[i] %><br>
&nbsp;<%= nf.format(ownerinfo.DetailGoodsAmount[i]) %>&nbsp;\<%= Kanma.get(ownerinfo.DetailGoodsPrice[i]) %>
<%
            }
        }
%>

<hr>
<%
    }
    // ホストがAMFの場合のみ表示ここまで
%>


[ﾒﾝﾊﾞｰ情報]<br>
<%
    if( ownerinfo.MemberCustomId[0].compareTo("") != 0 )
    {
%>
番　号:<%= ownerinfo.MemberCustomId[0] %><br>
<%
        if(!ownerinfo.MemberName[0].equals(""))
        {
%>
氏名:<%= ownerinfo.MemberName[0] %><br>
<%
        }
%>
ﾆｯｸﾈｰﾑ:<%= ownerinfo.MemberNickName[0] %><br>
誕生日:<%= ownerinfo.MemberBirthday1[0] / 100 % 100 %>/<%= ownerinfo.MemberBirthday1[0] % 100 %>
<%if( ownerinfo.MemberBirthday2[0] != 0 ){%> <%= ownerinfo.MemberBirthday2[0] / 100 % 100 %>/<%= ownerinfo.MemberBirthday2[0] % 100 %><%}%><br>
記念日:<%= ownerinfo.MemberMemorial1[0] / 100 % 100 %>/<%= ownerinfo.MemberMemorial1[0] % 100 %>
<%if( ownerinfo.MemberMemorial2[0] != 0 ){%> <%= ownerinfo.MemberMemorial2[0] / 100 % 100 %>/<%= ownerinfo.MemberMemorial2[0] % 100 %><%}%><br>
ﾎﾟｲﾝﾄ&nbsp;:<%= Kanma.get(ownerinfo.MemberPoint[0]) %><br>
ﾗﾝｸ:<%= ownerinfo.MemberRankName[0] %><br>

[ﾒﾓ]<br>
連絡:<%= ownerinfo.MemberContact1[0] %><%= ownerinfo.MemberContact2[0] %><br>
警告:<%= ownerinfo.MemberWarning1[0] %><%= ownerinfo.MemberWarning2[0] %><br>

<%
    }
%>

<%-- 部屋詳細情報表示ここまで --%>
<%
//ルームコードをクリアする
        ownerinfo.RoomCode = 0;

%>
