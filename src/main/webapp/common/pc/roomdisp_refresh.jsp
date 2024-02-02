<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="java.sql.*" %><%@ page errorPage="error.jsp" %><%@ page import="com.hotenavi2.common.*" %><jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    boolean interval_exist = false;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String            query       = "";
    connection  = DBConnection.getConnection();
    String[] intervalList = null;
    String   roomdispInterval = "";
    String   interval_text = "";

     // 設定値の読み込み
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    try
    {
        query = "SELECT roomdisp_interval FROM owner_config WHERE hotelid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginhotel);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            roomdispInterval = result.getString("roomdisp_interval");
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
    if (roomdispInterval.equals(""))
    {
        roomdispInterval = "30,60,180,300,600";
    }
    intervalList = roomdispInterval.split(",");

    String    interval;
    String    reqpage;

    interval = ReplaceString.getParameter(request,"Interval");
    if( interval == null )
    {
        interval = (String)session.getAttribute("Interval");
        if( interval == null )
        {
            interval = "";
        }
    }
    else
    {
		if(!CheckString.numCheck(interval))
		{
                    interval ="0";  
%>
			<script type="text/javascript">
			<!--
			var dd = new Date();
			setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
			//-->
			</script>
<%
		}
        session.setAttribute("Interval", interval);
    }
    reqpage = ReplaceString.getParameter(request,"reqpage");
    if( reqpage == null )
    {
        reqpage = "";
    }

    String param_cnt = ReplaceString.getParameter(request,"cnt");
    if( param_cnt == null )
    {
        param_cnt = "0";
    }
    if(!CheckString.numCheck(param_cnt))
	{
	param_cnt ="0";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </script>
<%
	}
    int cnt = Integer.parseInt(param_cnt);
    
%>
<form action="<%= reqpage %>?cnt=<%= cnt %>" id=refreshForm method="post">
<tr>
  <td height="20">
    <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <td height="20" class="tab">
          <div align="center">再表示間隔:
            <select name="Interval" id="IntervalSelect">
<%
    if( interval.compareTo("") == 0 )
    {
        interval_exist = true;
%>
              <option value="" selected>なし</option>
<%
    }
    else
    {
%>
              <option value="">なし</option>
<%
    }
    for (int i = 0; i < intervalList.length ; i++ )
    {
        interval_text = "";
        if (Integer.parseInt(intervalList[i]) / 60 !=0 )
        {  
            interval_text += Integer.parseInt(intervalList[i]) / 60 + "分";
        }
        if (Integer.parseInt(intervalList[i]) % 60 !=0 )
        {  
            interval_text += Integer.parseInt(intervalList[i]) % 60 + "秒";
        }
%>              <option value="<%=intervalList[i]%>" <% if(interval.equals(intervalList[i])){interval_exist = true; %>selected<%}%>><%=interval_text%></option>
<%
    }
%>           </select>
           <input type="submit" value="決定">
         </div>
       </td>
   </table>
  </td>
</tr>
</form>
<script>
<%
    if (!interval_exist)
    {
%>
    document.getElementById('IntervalSelect').selectedIndex = 1;
    document.getElementById("refreshForm").submit();
<%
    }
%>
</script>
