<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.HotelElement" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/owner_ini.jsp" %><%
    // セッションの確認
    if( ownerinfo.HotelId == null)
    {
        ownerinfo.HotelId = "";
    }
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    int storecount = 1;

    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");
    if  (loginHotelId == null)
    {
         loginHotelId = "demo";
    }

    String     selecthotel   = "";
    selecthotel = request.getParameter("Store");
     if  (selecthotel == null)
    {
        selecthotel = (String)session.getAttribute("SelectHotel");
    }
    if  (selecthotel == null || selecthotel.equals(""))
    {
        selecthotel = "all";
    }
    if(CheckString.isValidParameter(selecthotel) && !CheckString.numAlphaCheck(selecthotel))
    {
        response.sendError(400);
        return;
    }
    //現在日時の取得
    DateEdit  de = new DateEdit();
    int    now_date        = Integer.parseInt(de.getDate(2));
    int    startDate = HotelElement.getStartDate(selecthotel);

    int        imedia_user        = 0;
    int        level              = 0;
    boolean    bbs_flag           = false;
    boolean    bbs_multi_flag     = false;
    boolean    coupon_flag        = false;
    boolean    question_flag      = false;
    boolean    goods_flag         = false;
    boolean    goods_message_flag = false;
    boolean    sort_num_flag      = false;
    String     hotel_id_multi     = "";
    int        count              = 0;

    String     query              = "";

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    if (imedia_user == 1 && level==3)
    {
    //  多店舗表示順編集店舗数のチェック
        try
        {
            query = "SELECT count(DISTINCT ouh.accept_hotelid) FROM owner_user_hotel ouh";
            query = query + " INNER JOIN hotel ON  ouh.accept_hotelid = hotel.hotel_id";
            query = query + " WHERE ouh.hotelid = ?";
            query = query + " AND hotel.plan in (1,2,3,4)";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, selecthotel);
            result      = prestate.executeQuery();
            if  (result != null)
            {
                if( result.next() )
                {
                    if (result.getInt(1) > 1)
                    sort_num_flag   = true;
                }
            }
        }
        catch( Exception e )
        {
          Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
    }

    //  店舗数のチェック
    try
    {
        query = "SELECT count(*) FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        query = query + " AND owner_user_hotel.userid = ?";
        query = query + " AND hotel.plan <= 4";
        query = query + " AND hotel.plan >= 1";
        query = query + " AND hotel.plan != 2";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if  (result != null)
        {
            if( result.next() )
            {
                storecount   = result.getInt(1);
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    if  (storecount == 1)
    {
        query = "SELECT owner_user_hotel.accept_hotelid FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        query = query + " AND owner_user_hotel.userid = ?";
        query = query + " AND hotel.plan <= 4";
        query = query + " AND hotel.plan >= 1";
        query = query + " AND hotel.plan != 2";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if  (result != null)
        {
            if( result.next() )
            {
                selecthotel   = result.getString(1);
            }
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

// クーポン有無チェック
    try
    {
        query = "SELECT * FROM menu WHERE hotelid=?";
        query = query + " AND contents='coupon.jsp'";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            coupon_flag = true;
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    try
    {
        query = "SELECT * FROM menu_config WHERE hotelid=?";
        query = query + " AND event_data_type = 17";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            coupon_flag = true;
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

// クーポン有無チェック2
    try
    {
        if (selecthotel.compareTo("all") == 0)
        {
            query = "SELECT * FROM edit_coupon WHERE hotelid=? LIMIT 0,1";
        }else
        {
            query = "SELECT * FROM edit_coupon WHERE hotelid=? LIMIT 0,1";
        }
        prestate    = connection.prepareStatement(query);
        if (selecthotel.compareTo("all") == 0)
        {
            prestate.setString(1, loginHotelId);
        }
        else
        {
            prestate.setString(1, selecthotel);
        }
        result      = prestate.executeQuery();
        if( result.next() )
        {
            coupon_flag = true;
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

// アンケート有無チェック
    try
    {
        query = "SELECT * FROM menu WHERE hotelid=?";
        query = query + " AND contents='question.jsp'";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            question_flag = true;
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
// アンケート有無チェック
    try
    {
        query = "SELECT * FROM menu_config WHERE hotelid=?";
        query = query + " AND contents='question'";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            question_flag = true;
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
// アンケート有無チェック2
    if (question_flag)
    {
//        question_flag = false;
        try
        {
            query = "SELECT * FROM question_master WHERE hotel_id=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, selecthotel);
            result      = prestate.executeQuery();
            if( result.next() )
            {
                question_flag = true;
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
    }

// 掲示板情報取得
    if (selecthotel.compareTo("") != 0) {
        try
        {
            query = "SELECT * FROM bbs WHERE hotel_id=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, selecthotel);
            result      = prestate.executeQuery();
            if( result.next() )
            {
               bbs_flag = true;
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }


// 多店舗掲示板情報取得
        try
        {
            if (selecthotel.compareTo("all") == 0 && imedia_user == 1)
            {
                query = "SELECT * FROM bbs_multi WHERE hotel_id=?";
            }else{ 
                query = "SELECT * FROM bbs_multi WHERE topic_hotel_id=?";
            }
            prestate    = connection.prepareStatement(query);
            if (selecthotel.compareTo("all") == 0 && imedia_user == 1)
            {
                prestate.setString(1, loginHotelId);
            }
            else
            {
                prestate.setString(1, selecthotel);
            }
            result      = prestate.executeQuery();
            if( result.next() )
            {
                bbs_multi_flag = true;
                hotel_id_multi = result.getString("hotel_id");
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
    }

// 景品交換メッセージ有無チェック
    try
    {
        query = "SELECT * FROM edit_event_info WHERE hotelid=?";
        query = query + " AND data_type=81";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            goods_message_flag = true;
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
    }

// 景品交換有無チェック
    try
    {
        query = "SELECT * FROM goods WHERE hotelid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            goods_flag = true;
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
            DBConnection.releaseResources(result,prestate,connection);
    }

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>HP編集メニュー</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/hpedit_datacheck.js"></script>
</head>
<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" <% if(storecount > 1){%>onload="if(document.selectstore.Store.value !='<%=selecthotel%>'){document.selectstore.elements['submitstore'].click();top.Main.mainFrame.location.href='page.html';}"<%}%>>
<table width="100%" height="45" border="0" cellpadding="0" cellspacing="0">
<tr>
    <jsp:include page="selectstore.jsp" flush="true">
       <jsp:param name="URL"        value="hpedit_select"   />
       <jsp:param name="StoreCount" value="<%=storecount%>" />
    </jsp:include>
    <td width="80" rowspan="2" align="left" valign="middle" nowrap bgcolor="#000000">
      <div class="white12" align="center">HP編集</div>
    </td>
<%
   if (selecthotel.compareTo("all") != 0 || imedia_user == 1)
   {
%>
    <td width="14%" height="45" align="left" valign="middle" nowrap bgcolor="#FFFFFF" style="font-size:12px;line-height:150%" >
        <a href="hpedit.jsp?Type=pc&HotelId=<%=selecthotel%>" <% if (level != 3 && selecthotel.compareTo("all") !=0 &&  storecount > 1){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;PC版HP編集</a><br/>
<%
       if (imedia_user == 1 && loginHotelId.compareTo("demo") == 0)
       {
%>
        <a href="hpedit.jsp?Type=pc&HotelId=<%=selecthotel%>&Target=room_list2" target="mainFrame">&gt;&gt;部屋情報</a><br/>
<%
       }
%>
	</td>
    <td width="15%" height="45" align="left" valign="middle" nowrap bgcolor="#FFFFFF" style="font-size:12px;line-height:150%" >
     <% if (imedia_user == 1 && loginHotelId.compareTo("demo") == 0){%><a href="hpedit.jsp?Type=mobile&HotelId=<%=selecthotel%>" <% if (level != 3 && selecthotel.compareTo("all") !=0 &&  storecount > 1){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;携帯版HP編集</a><%}%><br/>
<%
       if (imedia_user == 1 && loginHotelId.compareTo("demo") == 0)
       {
%>
        <a href="vacant_edit.jsp" target="mainFrame">&gt;&gt;空満予約等</a><br/>
<%
       }
%>
	</td>
<%
    }
    count = 1;

%>
    <td width="12%" align="left" valign="middle" nowrap bgcolor="#FFFFFF" style="font-size:12px;line-height:150%" >
<%
	if (coupon_flag == true  || level == 3)
    {
        count++;
%>
		<a href="coupon_edit.jsp" <% if (level != 3 && selecthotel.compareTo("all") !=0 &&  storecount > 1){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;クーポン</a><br/>
<%
    }
%>
<%
       if (imedia_user == 1 && loginHotelId.compareTo("demo") == 0)
       {
%>
        <a href="etc_edit.jsp?<%=new DateEdit().getDate(2)%><%=new DateEdit().getTime(1)%>" target="mainFrame">&gt;&gt;その他編集</a><br/>
<%
       }
%>
	</td>

<%
    if (question_flag == true || imedia_user == 1 || sort_num_flag)
    {
    count++;
%>
     <td width="12%" align="left" valign="middle" nowrap bgcolor="#FFFFFF" style="font-size:12px;line-height:150%" >
        <% if (question_flag == true || imedia_user == 1){%>
		<a href="qaedit.jsp" <% if (level != 3 && selecthotel.compareTo("all") !=0 &&  storecount > 1){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;アンケート</a><br>
		<% } %>
        <% if (sort_num_flag){%>
		<a href="sortnum_edit.jsp" target="mainFrame">&gt;&gt;多店舗表示順</a>
		<% } %>
	</td>
<%
    }
%>
<%
    if (bbs_multi_flag == true)
    {
      count++;
%>
    <td width="15%" height="45" align="left" valign="middle" nowrap bgcolor="#FFFFFF" style="font-size:12px;line-height:150%" >
		<a href="bbs_multi.jsp?HotelId=<%= selecthotel %>&HotelId_multi=<%= hotel_id_multi %>" target="mainFrame" class="submenu">&gt;&gt;多店舗掲示板</a><br/>
		<a href="ngword_edit.jsp"  target="mainFrame">&gt;&gt;NGワード</a>
	</td>
<%
    }
    else if (bbs_flag == true)
    {
      count++;
%>
    <td width="12%" height="45" align="left" valign="middle" nowrap bgcolor="#FFFFFF" style="font-size:12px;line-height:150%" >
		<a href="bbs.jsp?HotelId=<%= selecthotel %>" target="mainFrame">&gt;&gt;掲示板</a><br/>
		<a href="ngword_edit.jsp"  target="mainFrame">&gt;&gt;NGワード</a>
	</td>
<%
    }
%>
<%
    if (goods_flag == true || goods_message_flag == true || level == 3 )
    {
    count++;
%>
	<td width="15%" align="left" valign="middle" nowrap bgcolor="#FFFFFF" style="font-size:12px;line-height:150%" >
<%
        if (goods_message_flag == true || level == 3 )
        {
%>
		<a href="goods_message.jsp" <% if (level != 3 && selecthotel.compareTo("all") !=0 &&  storecount > 1){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;景品交換設定</a><br/>
<%
        }
        if (goods_flag == true || level == 3 )
        {
%>

		<a href="goods_list.jsp" <% if (level != 3 && selecthotel.compareTo("all") !=0 &&  storecount > 1){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;景品登録</a><br/>
<%
        }
%>
	</td>
<%
    }
%>

<%
    for(int i = count; i < 5 ; i++)
    {
%>
    <td height="20" align="left" valign="middle" nowrap bgcolor="#FFFFFF" style="font-size=12px" >&nbsp;</td>
<%
    }
%>
  </tr>
</table>
</body>
</html>
