<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>
<%
    DateEdit  de   = new DateEdit();
    int  nowdate   = Integer.parseInt(de.getDate(2));

    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");
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
    if(!CheckString.numAlphaCheck(selecthotel))
    {
        response.sendError(400);
        return;
    }
    int storecount = Integer.parseInt((String)session.getAttribute("StoreCount"));

    int        imedia_user        = 0;
    int        level              = 0;
    boolean    access_flag        = false;
    boolean    coupon_flag        = false;
    boolean    goods_flag         = false;
    boolean    question_flag      = false;
    int        question_id        = 0;

    String     query              = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid='" + loginHotelId + "'";
        query = query + " AND userid=" + ownerinfo.DbUserId;
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
        }
    }
    catch( Exception e )
    {
        Logging.info("foward Exception e=" + e.toString() );
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

// アクセスレポート有無チェック
    try
    {
        if (selecthotel.compareTo("all") == 0) 
        {
            query = "SELECT * FROM access_mobile_name,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + loginHotelId + "'";
            query = query + " AND access_mobile_name.hotel_id = owner_user_hotel.accept_hotelid";
            query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
        }
        else
        {
            query = "SELECT * FROM access_mobile_name WHERE access_mobile_name.hotel_id='" + selecthotel + "'";
        }
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            access_flag = true;
        }
    }
    catch( Exception e )
    {
        Logging.info("foward Exception e=" + e.toString() );
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

// クーポン有無チェック
    try
    {
        query = "SELECT * FROM menu WHERE hotelid='" + selecthotel + "'";
        query = query + " AND contents='coupon.jsp'";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            coupon_flag = true;
        }
    }
    catch( Exception e )
    {
        Logging.info("foward Exception e=" + e.toString() );
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

// クーポン有無チェック2
    if (selecthotel.compareTo("") != 0)
    {
        try
        {
//          if (selecthotel.compareTo("all") == 0) {
//              query = "SELECT * FROM edit_coupon WHERE hotelid='" + loginHotelId +  "'";
//          }else{
                query = "SELECT * FROM edit_coupon WHERE hotelid='" + selecthotel + "'";
//          }
            prestate    = connection.prepareStatement(query);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                coupon_flag = true;
            }
        }
        catch( Exception e )
        {
            Logging.info("foward Exception e=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
    }
// アンケート有無チェック
    try
    {
//        if (selecthotel.compareTo("all") == 0) 
//        {
//            query = "SELECT * FROM question_master,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + loginHotelId + "'";
//            query = query + " AND question_master.hotel_id = owner_user_hotel.accept_hotelid";
//            query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
//            query = query + " ORDER BY question_master.id DESC";
//        }
//        else
//        {
            query = "SELECT * FROM question_master WHERE hotel_id='" + selecthotel + "'";
            query = query + " ORDER BY question_master.id DESC";
//        }
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            question_id   = result.getInt("question_master.id");
            question_flag = true;
        }
    }
    catch( Exception e )
    {
        Logging.info("foward Exception e=" + e.toString() );
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
// 景品交換有無チェック
    try
    {
        query = "SELECT * FROM goods WHERE hotelid='" + selecthotel + "'";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            goods_flag = true;
        }
    }
    catch( Exception e )
    {
        Logging.info("foward Exception e=" + e.toString() );
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    //  店舗数のチェック
    try
    {
        query = "SELECT count(*) FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + loginHotelId + "'";
        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
        query = query + " AND hotel.plan <= 4";
        query = query + " AND hotel.plan >= 1";
        query = query + " AND hotel.plan != 2";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if  (result != null)
        {
            if( result.next() != false )
            {
                storecount   = result.getInt(1);
            }
        }
    }
    catch( Exception e )
    {
        Logging.info("foward Exception e=" + e.toString() );
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
<meta http-equiv="Pragma" content="no-cache">
<title>店舗選択</title>
<script type="text/javascript" src="../../common/pc/scripts/report_datacheck.js"></script>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" <% if(storecount > 1){%>onload="if(document.selectstore.Store.value !='<%=selecthotel%>'){document.selectstore.elements['submitstore'].click();top.Main.mainFrame.location.href='page.html';}"<%}%>>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
        <tr valign="middle" bgcolor="#FFFFFF">

          <!-- 店舗選択表示 -->
    <jsp:include page="../../common/pc/selectstore.jsp" flush="true">
       <jsp:param name="URL" value="hpreport_select" />
       <jsp:param name="StoreCount" value="<%=storecount%>" />
    </jsp:include>
          <!-- 店舗選択表示ここまで -->
          <td width="71" align="left" valign="middle" nowrap bgcolor="#000000" rowspan="2">
            <div class="white12" align="center">HPレポート</div>
          </td>
<%
        if( access_flag)
        {
%>
          <td  class="size12" rowspan="2"  valign="middle">
                    <a href="hpreport.jsp"  <%if (storecount > 1){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;アクセスレポート</a>
          </td>
<%
        }
%>
<%
        if( coupon_flag)
        {
%>

          <td  class="size12" rowspan="2"  valign="middle">
                    <a href="coupon_report_f.html"  <%if (storecount > 1){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;クーポンレポート</a>
          </td>
<%
        }
%>
<%
        if( question_flag)
        {
%>

          <td  class="size12" rowspan="2"  valign="middle">
                    <a href="qareport.jsp?Id=<%= question_id %>"  <%if (storecount > 1){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;アンケートレポート</a>
          </td>
<%
        }
%>
<%
        if( goods_flag)
        {
%>

          <td  class="size12" rowspan="2"  valign="middle">
                    <a href="goods_report_f.html"  <%if (storecount > 1){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;景品交換レポート</a>
          </td>
<%
        }
%>
          <td  class="size12" rowspan="2"  valign="middle">
            &nbsp;
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>
