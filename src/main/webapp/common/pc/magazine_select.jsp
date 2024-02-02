<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.File" %><%@ page import="java.sql.*" %><%@ page import="java.util.*" %><%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %><%@ page import="jp.happyhotel.common.HotelMailmagazine" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../csrf/refererCheck.jsp" %><%@ include file="../csrf/checkCsrfForPost.jsp" %><%@ include file="../../common/pc/owner_ini.jsp" %>
<%
// セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
    %>
        <script type="text/javascript">
         <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
   <%
    }
    HotelMailmagazine hotelMailmagazine = new HotelMailmagazine();
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");
    String     selecthotel   = "";
    selecthotel = request.getParameter("Store");
    if  (selecthotel == null)
    {
        selecthotel = (String)session.getAttribute("SelectHotel");
    }
    if (selecthotel != null && !selecthotel.equals("") && !CheckString.numAlphaCheck(selecthotel))
    {
%><script type="text/javascript">
    <!--
   var dd = new Date();
   setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
   //-->
   </SCRIPT>
<%
       selecthotel   = "";
    }
    int storecount = Integer.parseInt((String)session.getAttribute("StoreCount"));

    String     query              = "";

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    int        imedia_user = 0;
    // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
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
   

    boolean isMagaddressEdit = false; 
    File checkFile = null;
    checkFile = new File("/hotenavi/" +loginHotelId + "/pc/magaddress_edit.jsp");
    if(checkFile.exists()) 
    {
       isMagaddressEdit = true;
    }

    // 管理店舗数の取得
    if (hotelMailmagazine.getManageHotel(loginHotelId,ownerinfo.DbUserId))
    {
      storecount = hotelMailmagazine.getHotelCount();
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<title>設定店舗選択</title>
<script type="text/javascript" src="../../common/pc/scripts/report_datacheck.js"></script>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" <% if (storecount > 1 && selecthotel != null && !selecthotel.equals("")){%>onload="if(document.selectstore.Store.value !='<%=selecthotel%>'){document.selectstore.elements['submitstore'].click();top.Main.mainFrame.location.href='page.html';}"<%}%>>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <!-- 店舗選択表示 -->
        <jsp:include page="selectstore.jsp" flush="true">
            <jsp:param name="URL"        value="magazine_select"   />
            <jsp:param name="StoreCount" value="<%=storecount%>" />
         </jsp:include>
          <!-- 店舗選択表示ここまで -->
    <td width="90" align="left" valign="middle" rowspan="2" nowrap bgcolor="#000000">
      <div class="white12" align="center">メールマガジン</div>
    </td>
    <td  class="size12" rowspan="2"  valign="middle"  nowrap bgcolor="#ffffff">
<% if(storecount != 0 || imedia_user == 1){%>
    <a href="magazine_find.jsp"  <% if (storecount > 1){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;メルマガ作成</a>&nbsp;
    <a href="magazine_history.jsp" <% if (storecount > 1){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;履歴・編集</a>&nbsp;
    <a href="magazine_unknown.jsp" <% if (storecount > 1){%>onClick="return datacheck()"<%}%> target="mainFrame">&gt;&gt;配信不能アドレス</a>&nbsp;
<%}%>
    <a href="mag_hotel_hpedit.jsp" onClick="return datacheck()" target="mainFrame">&gt;&gt;HP編集</a>&nbsp;
    <a href="mag_hotel_edit.jsp" onClick="return datacheck()" target="mainFrame">&gt;&gt;送信設定</a>&nbsp;
<%
   if (request.getRequestURL().toString().indexOf("_debug_") != -1 || isMagaddressEdit)
   {
%>    <a href="magaddress_edit.jsp" onClick="return datacheck()" target="mainFrame">&gt;&gt;メールアドレス登録</a>&nbsp;
<%
   }
%>    </td>
  </tr>
</table>
</body>
</html>
