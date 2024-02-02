<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/owner_ini.jsp" %>

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
    String loginhotel = (String)session.getAttribute("LoginHotelId");

    DbAccess db =  new DbAccess();
    // セキュリティ情報の取得
    ResultSet DbUserSecurity = ownerinfo.getUserSecurity(db, loginhotel, ownerinfo.DbUserId);

    // セキュリティチェック
    if( DbUserSecurity == null )
    {
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }
    if( DbUserSecurity.first() == false )
    {
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }

%>


<%
    String       selecthotel = "";
    selecthotel = ReplaceString.getParameter(request,"Store");
if  (selecthotel != null)
{
    selecthotel = ReplaceString.HTMLEscape(selecthotel);
}

if  (selecthotel == null)
	{
    selecthotel = (String)session.getAttribute("SelectHotel");
	}
if  (selecthotel == null)
	{
    selecthotel = "all";
	}
    if( !CheckString.hotenaviIdCheck(selecthotel) )
    {
        selecthotel = "";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
    }
	
    int storecount = Integer.parseInt((String)session.getAttribute("StoreCount"));
    int       count = 0;

    String    query ="";
    boolean    bbs_flag           = false;
    boolean    bbs_multi_flag     = false;
    boolean    coupon_flag        = false;
    boolean    question_flag      = false;
    String     hotel_id_multi     = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    try
    {
        query = "SELECT count(*) FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + ownerinfo.HotelId + "'";
        query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
        query = query + " AND hotel.plan <= 4";
        query = query + " AND hotel.plan >= 1";
        query = query + " AND hotel.plan != 2";
        query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result != null )
        {
            if( result.next() != false )
            {
               storecount = result.getInt(1);
            }
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

// 掲示板情報チェック
    try
    {
        query = "SELECT * FROM bbs WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            bbs_flag = true;
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

// 多店舗掲示板情報チェック
    try
    {
        if (selecthotel.compareTo("all") == 0) {
            query = "SELECT * FROM bbs_multi WHERE hotel_id=?";
        }else{
            query = "SELECT * FROM bbs_multi WHERE topic_hotel_id=?";
        }
        prestate    = connection.prepareStatement(query);
        if (selecthotel.compareTo("all") == 0) {
            prestate.setString(1, (String)session.getAttribute("LoginHotelId"));
        }else{
            prestate.setString(1, selecthotel);
        }
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            bbs_multi_flag = true;
            hotel_id_multi = result.getString("hotel_id");
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

%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>掲示板管理メニュー</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/bbs_datacheck.js"></script>
</head>
<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" <% if(storecount > 1){%>onload="if(document.selectstore.Store.value !='<%=selecthotel%>'){document.selectstore.elements['submitstore'].click();top.Main.mainFrame.location.href='page.html';}"<%}%>>
<table width="100%" height="40" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <jsp:include page="../../common/pc/selectstore.jsp" flush="true">
       <jsp:param name="URL" value="bbs_select" />
       <jsp:param name="StoreCount" value="<%=storecount%>" />
    </jsp:include>
    <td width="80" rowspan="1" align="left" valign="middle" nowrap bgcolor="#000000">
      <div class="white12" align="center">掲示板管理</div>
    </td>
		<!-- ユーザレベルによってメニューを切り替える -->
<%
	if( DbUserSecurity.getInt("sec_level12") == 1 )
	{
        count++;
        count++;
%>
			<td height="20" align="left" valign="middle" nowrap bgcolor="#FFFFFF" style="font-size=12px" ><a href="http://www.hotenavi.com/<%= selecthotel %>/ownerbbs.jsp?HotelId=<%= selecthotel %>" onClick="return datacheck()" target="mainFrame">&gt;&gt;仮投稿管理</a></td>
			<td align="left" valign="middle" nowrap bgcolor="#FFFFFF" style="font-size=12px" ><a href="http://www.hotenavi.com/<%= selecthotel %>/ownerbbsnglist.jsp?HotelId=<%= selecthotel %>" onClick="return datacheck()" target="mainFrame">&gt;&gt;削除一覧</a></td>
<%
    }
%>
<%
    if (bbs_flag == true || bbs_multi_flag == true)
    {
%>

    <td height="20" align="left" valign="middle" nowrap bgcolor="#FFFFFF" style="font-size=12px" ><a href="ngword_edit.jsp" onClick="return datacheck()" target="mainFrame">&gt;&gt;掲示板NGWORD</a></td>
<%
    }
%>
<%
    if(bbs_multi_flag)
    {
        count++;
%>
		    <td height="20" align="left" valign="middle" nowrap bgcolor="#FFFFFF" style="font-size=12px" ><a href="bbs_multi.jsp?HotelId=<%= selecthotel %>&HotelId_multi=<%= hotel_id_multi %>" onClick="return datacheck()" target="mainFrame" >&gt;&gt;掲示板</a></td>
<%
    }
    else if (bbs_flag)
    {
        count++;
%>
		    <td height="20" align="left" valign="middle" nowrap bgcolor="#FFFFFF" style="font-size=12px" ><a href="bbs.jsp?HotelId=<%= selecthotel %>" onClick="return datacheck()" target="mainFrame" >&gt;&gt;掲示板</a></td>
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
<%
	    db.close();
%>
</table>
</body>
</html>
