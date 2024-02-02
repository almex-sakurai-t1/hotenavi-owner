<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="java.sql.*" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String       selecthotel;
    String       hotelid = "";
    String       query = "";
    String       hotelname = "";
    int          i  = 0;
    int          count  = 0;

    String loginHotelId = (String)session.getAttribute("LoginHotelId");

    // セッション属性より選択されたホテルを取得
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }
%>
<tr>
  <td align="center" valign="middle" nowrap class="size12 tableLN">店舗名</td>
  <td align="center" valign="middle" nowrap class="size12 tableLN">対象</td>
</tr>
<%
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int          imedia_user      = 0;
    int          level            = 0;
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
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
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

    try
    {
        if (imedia_user == 1 && level == 3 && !selecthotel.equals("all_manage"))
        {
            query = "SELECT * FROM hotel WHERE hotel.group_id ?";
            query = query + " AND hotel.plan IN (1,3,4)";
            query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
        }
        else
        {
            query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid =?";
            query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
            query = query + " AND hotel.plan IN (1,3,4)";
            query = query + " AND owner_user_hotel.userid = ?";
            query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
        }
        prestate    = connection.prepareStatement(query);
        if (imedia_user == 1 && level == 3 && !selecthotel.equals("all_manage"))
        {
            prestate.setString(1, selecthotel);
        }
        else
        {
            prestate.setString(1, ownerinfo.HotelId);
            prestate.setInt(2, ownerinfo.DbUserId);
        }
        result      = prestate.executeQuery();
        if  (result != null)
        {
            while( result.next() != false)
            {
                hotelid   = result.getString("hotel.hotel_id");
                hotelname = result.getString("hotel.name");
                i++;
%>
<tr height="24px" onclick="if(document.getElementById('DispFlag_<%=i%>').checked){top.Main.mainFrame.contents.location.href=targetUrl+'#a_<%=hotelid%>'}">
    <td align="left" valign="middle"  class="tableL<%if (i%2==0){%>W<%}else{%>G<%}%> size12"><%= hotelname %></td>
    <td align="center" valign="middle" class="tableR<%if (i%2==0){%>W<%}else{%>G<%}%>"><input onclick="hotelClick(this.checked,'<%=hotelid%>');groupSet();" type="checkbox" name="DispFlag_<%=i%>" id="DispFlag_<%=i%>" value="<%=hotelid%>" <%if(hotelid.equals(selecthotel)){%>checked<%}%>></td>
</tr>
<%
            }
            count = i;
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

<tr height="24px">
    <td align="left" valign="middle"  class="size12" colspan=2>
        <input type=button value="全選択" onclick="allClick(true)">
        <input type=button value="全解除" onclick="allClick(false)">
    </td>
</tr>
<script type="text/javascript">
<%
if (count < 2)
{
%> document.getElementById("groupLink").style.display="none";
<%
}
%>
var targetUrl = top.Main.mainFrame.contents.location.href;


setTimeout("init();",3000);

//top.Main.mainFrame.contents.document.body.onload = function()
//{
//init();
//};

function init()
{
    var groupList = document.getElementById("Group").value.split(",");
    <% for(i = 1; i <= count; i++)
    {%>document.getElementById("DispFlag_<%=i%>").checked = false;
       var hotelid = document.getElementById("DispFlag_<%=i%>").value;
       top.Main.mainFrame.contents.document.getElementById("tr_"+hotelid).style.display="none";
       for (var i = 0;i<groupList.length ; i++ ) {
           if (groupList[i] == hotelid)
           {
              document.getElementById("DispFlag_<%=i%>").checked = true;
              top.Main.mainFrame.contents.document.getElementById("tr_"+hotelid).style.display="";
           }
       }
    <%}%>
    groupSet();
    targetUrl = top.Main.mainFrame.contents.location.href;
}

function hotelClick(check,hotelid)
{
   if (check)
   {
      top.Main.mainFrame.contents.document.getElementById("tr_"+hotelid).style.display="";
   }
   else
   {
      top.Main.mainFrame.contents.document.getElementById("tr_"+hotelid).style.display="none";
   }
}

function allClick(check)
{
<% for(i = 1; i <= count; i++)
{%>document.getElementById("DispFlag_<%=i%>").checked = check;
   hotelClick(check,document.getElementById("DispFlag_<%=i%>").value);<%}%>
   groupSet();
}

function groupSet()
{
   var wk_group = "";
<% for(i = 1; i <= count; i++)
{%>
   if (document.getElementById("DispFlag_<%=i%>").checked)
   {
      if (wk_group != "")
      {
          wk_group += ",";
      }
      wk_group += document.getElementById("DispFlag_<%=i%>").value;
   }
<%}%>
   document.getElementById("Group").value=wk_group;
}
</script>

