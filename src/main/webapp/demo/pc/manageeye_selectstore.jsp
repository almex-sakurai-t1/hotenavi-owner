<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int          count;
    boolean      ret;
    String       query;
    String       hotelname = "";
    String       storecount;
    String       selecthotel;
    String       param_store;
    ResultSet    result;

    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    param_store = request.getParameter("Store");
    if( param_store != null )
    {
        selecthotel = param_store;
    }

    // セッション属性に選択ホテルをセットする
    session.setAttribute("SelectHotel", selecthotel);

    // 管理店舗数の取得
    storecount = (String)session.getAttribute("StoreCount");
    if( storecount != null )
    {
        count = Integer.valueOf(storecount).intValue();
    }
    else
    {
        count = 0;
    }

    // 2店舗以上の場合表示する
    if( count > 1 )
    {
%>

<td width="80" align="center" nowrap bgcolor="#000000">
  <div class="white12" align="center">店舗選択</div>
</td>
<form action="manageeye_main.jsp" method="get" name="selectstore" target="mainFrame">
  <td width="100%" align="left" valign="middle" nowrap bgcolor="#FFFFFF">
    <select name="Store" onChange="document.selectstore.elements['submitstore'].click()">
      <option value="" selected>選択してください</option>
      <option value="fine001">ﾌｧｲﾝｶﾞｰﾃﾞﾝ香芝</option>
      <option value="fine002">ﾌｧｲﾝｶﾞｰﾃﾞﾝ泉北</option>
      <option value="fine005">ﾌｧｲﾝｶﾞｰﾃﾞﾝ姫路</option>
      <option value="fine011">ﾌｧｲﾝ堺</option>
      <option value="fine012">ﾌｧｲﾝｶﾞｰﾃﾞﾝ桑名</option>
      <option value="fine013">ﾌｧｲﾝｶﾞｰﾃﾞﾝ豊中</option>
      <option value="fine014-1">ﾌｧｲﾝ姫路I</option>
      <option value="fine014-2">ﾌｧｲﾝ姫路II</option>
      <option value="olive01">ｵﾘｰﾌﾞ琵琶湖</option>
      <option value="olive02">ｵﾘｰﾌﾞ堺</option>
      <option value="fine016">ｸﾞﾗﾝﾄﾞﾌｧｲﾝ京都南</option>
    </select>
    <input type="submit" value="店舗選択" name="submitstore" onClick="return datacheck()">
  </td> 
</form>

<%
    }
    else
    {
%>
<script type="text/javascript">
<!--
    setTimeout("window.open('manageeye_main.jsp','mainFrame')",0000);
//-->
</script>
<%
    }
%>

