<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String       selecthotel;
    String       hotelid = "";
    String       hotelname = "";
    int          i  = 0;

    // セッション属性より選択されたホテルを取得
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }
    ownerinfo.sendPacket0142(1,selecthotel);
    int    CalModeCount   = ownerinfo.CalModeCount;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int          imedia_user      = 0;
    int          level            = 0;
    connection  = DBConnection.getConnection();

    String ModeColor       = null;
    String ModeMax         = ReplaceString.getParameter(request,"ModeMax");

    // 変更された色データの書き込み
    if (ModeMax != null)
    {
        ModeColor = "";
        for( i = 1 ; i <= CalModeCount ; i++ )
        {
            if (i != 1) ModeColor = ModeColor + ",";
            ModeColor = ModeColor + ReplaceString.getParameter(request,"ModeColor"+i);
        }
        try
        {
        	final String query = "UPDATE hotel SET "
					            + "mode_color = '" + ModeColor + "'"
					            + " WHERE hotel_id='" + selecthotel + "'";
            prestate    = connection.prepareStatement(query);
            int retresult   = prestate.executeUpdate();
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
    // 色データの読み込み
    try
    {
    	final String query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            ModeColor   = result.getString("mode_color");
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
    if (ModeColor == null)
    {
        ModeColor = "";
    }

    ModeColor = ModeColor + ",#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF"; //何も入ってないときを考慮して分割用の(,)を付加。
    String[] strAry = ModeColor.split(",");
%>
<tr>
  <td align="center" valign="middle" nowrap class="size14 tableLN">No.</td>
  <td align="center" valign="middle" nowrap class="size14 tableLN">料金モード名</td>
  <td align="center" valign="middle" nowrap class="size14 tableLN">背景色</td>
</tr>
<%
    for( i = 1 ; i <= CalModeCount ; i++ )
    {
%>
<tr height="24px" id="MODE<%=i%>" onClick="MM_openBrWindow('../../common/pc/event_edit_select_color.html','色指定','width=240,height=180',<%=i%>)">
    <td id="No<%=i%>"   align="right"  valign="middle"  class="tableLW size14" nowrap onMouseOver="ColorChange(document.getElementById('No<%=i%>'),document.getElementById('Name<%=i%>'),document.getElementById('Col<%=i%>'));" onMouseOut="ColorRestore(document.getElementById('No<%=i%>'),document.getElementById('Name<%=i%>'),document.getElementById('Col<%=i%>'));"><%= ownerinfo.CalModeCode[i-1] %></td>
    <td id="Name<%=i%>" align="left"   valign="middle"  class="tableLW size14" nowrap onMouseOver="ColorChange(document.getElementById('No<%=i%>'),document.getElementById('Name<%=i%>'),document.getElementById('Col<%=i%>'));" onMouseOut="ColorRestore(document.getElementById('No<%=i%>'),document.getElementById('Name<%=i%>'),document.getElementById('Col<%=i%>'));"><%= ownerinfo.CalModeName[i-1] %></td>
    <td id="Col<%=i%>"  align="center" valign="middle"  class="tableRW" onMouseOver="ColorChange(document.getElementById('No<%=i%>'),document.getElementById('Name<%=i%>'),document.getElementById('Col<%=i%>'));" onMouseOut="ColorRestore(document.getElementById('No<%=i%>'),document.getElementById('Name<%=i%>'),document.getElementById('Col<%=i%>'));"><div style="margin:3px;background-color:<%=strAry[i-1]%>"><input type="text" style="width:1px" id="ModeColor<%=i%>" name="ModeColor<%=i%>" value="<%=strAry[i-1]%>" onfocus="document.getElementById('Col<%=i%>').style.backgroundColor=this.value;">&nbsp;</div></td>
</tr>
<%
    }
%>
   <input name="ModeMax"       type="hidden" value="<%=CalModeCount%>">

<script language="javascript">
<!--
function MM_openBrWindow(theURL,winName,features,colno) {
  targetNo    = colno;
  targetItem  = "ModeColor";
  window.open(theURL,winName,"width=240,height=180,location=no,menubar=no,scrollbars=no,toolbar=no,status=no,resizable=no").focus();
}
function ColorChange(obj1,obj2,obj3) {
}
function ColorRestore(obj1,obj2,obj3) {
}
-->
</script>
