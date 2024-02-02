<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="java.sql.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    int          count = 0;
    HotelMailmagazine hotelMailmagazine = new HotelMailmagazine();
    String loginHotelId  = (String)session.getAttribute("LoginHotelId");
    String selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    String param_store = ReplaceString.getParameter(request,"Store");
    if( param_store != null )
    {
        if( !CheckString.hotenaviIdCheck(param_store) && !param_store.equals(""))
        {
            param_store = "";
%>
            <script type="text/javascript">
             <!--
            var dd = new Date();
            setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
            //-->
            </SCRIPT>
<%
        }
        selecthotel = param_store;
        // セッション属性に選択ホテルをセットする
        session.setAttribute("SelectHotel", selecthotel);
    }

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    connection  = DBConnection.getConnection();
    int          imedia_user        = 0;
    int          level              = 0;

     // imedia_user のチェック
    try
    {
        String query = "SELECT * FROM owner_user WHERE hotelid=?";
        query +=" AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
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
        DBConnection.releaseResources(result,prestate,connection);
    }


    // 管理店舗数の取得
    if (hotelMailmagazine.getManageHotel(loginHotelId,ownerinfo.DbUserId))
    {
        count = hotelMailmagazine.getHotelCount();
        if (count == 1)
        {
            selecthotel =  hotelMailmagazine.getHotelMailmagazine()[0].getHotelId();
        }
    }
%>

<%
    // 2店舗以上の場合表示する
    if( count > 1 )
    {
%>
<td width="70" align="center" nowrap bgcolor="#000000">
  <div class="white12" align="center">&nbsp;店舗選択&nbsp;</div>
</td>
<form action="magazine_select.jsp" method="post" name="selectstore">
  <td  width="25%"  align="left" valign="middle" nowrap bgcolor="#FFFFFF">
    <select name="Store" onChange="document.selectstore.elements['submitstore'].click();">
<%
        if( selecthotel.compareTo("all") == 0 || selecthotel.compareTo("") == 0)
        {
%>
      <option value="all" selected>全店共通</option>
<%
        }
        else
        {
%>
      <option value="all">全店共通</option>
<%
        }
        for (int i = 0;  i  < count; i++)
        {
%>
      <option value="<%= hotelMailmagazine.getHotelMailmagazine()[i].getHotelId() %>" <% if (selecthotel.equals(hotelMailmagazine.getHotelMailmagazine()[i].getHotelId())){ %>selected<% } %>><%=hotelMailmagazine.getHotelMailmagazine()[i].getName() %></option>
<%
        }
%>
    </select>
    <input type='hidden' name='csrf' value='<%=token%>'>
    <input type="submit" value="店舗切替" name="submitstore" onClick="return datacheck()">&nbsp;
  </td> 
</form>

<%
    }
    else if (imedia_user == 1 && level == 3)
    {
%>
<td width="80" rowspan="2" align="left" valign="middle" nowrap bgcolor="#000000">
  <div class="white12" align="center">店舗選択</div>
</td>
<form action="magazine_select.jsp" method="post" name="selectstore">
  <td width="25%" rowspan="2" align="left" valign="middle" nowrap bgcolor="#FFFFFF">
   <input name="Store" size="14" maxlength="10" onChange="document.selectstore.elements['submitstore'].click();top.Main.mainFrame.location.href='page.html';" value="<%= selecthotel %>">
   <input type='hidden' name='csrf' value='<%=token%>'>
   <input type="submit" value="店舗選択" name="submitstore" onclick="top.Main.mainFrame.location.href='page.html';">
  </td> 
</form>
<%
    }
    else
    {
       if(ownerinfo.HotelId.equals(selecthotel))
       {
          // セッション属性に選択ホテルをセットする
          session.setAttribute("SelectHotel", selecthotel);
       }
       else
       {
          session.setAttribute("SelectHotel", selecthotel);
//          session.setAttribute("SelectHotel", ownerinfo.HotelId);
       }
%>
<form action="magazine_select.jsp" method="post" name="selectstore">
  <input type="hidden" name="Store" value="<%= ownerinfo.HotelId %>">
  <input type='hidden' name='csrf' value='<%=token%>'>
</form>
<%
    }
%>