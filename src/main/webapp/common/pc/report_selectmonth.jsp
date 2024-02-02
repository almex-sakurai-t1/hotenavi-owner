<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="java.util.*" %><%@ page import="java.sql.*" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int    i;
    int    day = 1;
    int    add_year;
    String add_month;
    int    limit_year =(-1)*7; //7年

    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String selecthotel = (String)session.getAttribute("SelectHotel");

    int    plan= 0;
    String ftp_serv    = "";
    String ftp_passwd  = "";
    String kind_head   = "";
    String kind_name   = "";
    int    imedia_user = 0;
    int    level       = 0;
	String query = "";
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
    if (imedia_user == 1 && level == 3)
    {
        try
        {
          	query = "SELECT * FROM hotel WHERE hotel_id = ?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, selecthotel);
            result      = prestate.executeQuery();
            if  (result != null)
            {
                if(result.next())
                {
                    ftp_serv   =  result.getString("ftp_server");
                    ftp_passwd =  result.getString("ftp_passwd");
                    plan       =  result.getInt("plan");
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
    DBConnection.releaseResources(connection);

    String param = ReplaceString.getParameter(request,"AddYear");
    if( param == null )
    {
        add_year = 0;
    }
    else
    {
        if( !CheckString.numCheck(param) )
        {
            param="0";
%>
            <script type="text/javascript">
            <!--
            var dd = new Date();
            setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
            //-->
            </SCRIPT>
<%
        }
        add_year = Integer.valueOf(param).intValue();
    }
    add_month = ReplaceString.getParameter(request,"Add");
    if (add_month == null)
    {
        add_month = "0";
    }
    if( !CheckString.numCheck(add_month) )
    {
        add_month = "0";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
    }
    add_month = "&Add="+add_month;


    Calendar cal = Calendar.getInstance();

    // 年を加算する
    cal.add(cal.YEAR, add_year);

    // 日付を１日にする
    cal.set(cal.get(cal.YEAR), cal.get(cal.MONTH), 1);

    int year = cal.get(cal.YEAR);
%>

<table border="0" cellspacing="0" cellpadding="2">
    <tr>
      <td height="30" colspan="4"><table width="100%" height="30" border="0" cellpadding="0" cellspacing="0">
        <tr valign="middle">
          <td align="center" bgcolor="#21323F"><div class="size14"><font color="#FFFFFF">
              <select onchange="location.href='report_main.jsp?AddYear='+this.value+'<%=add_month%>';">
<%
    for (i=limit_year ; i <= 0 ;  i++)
    {
        Calendar optCal = Calendar.getInstance();
        optCal.add(cal.YEAR, i);
        int opt_year = optCal.get(cal.YEAR);
%>                <option value="<%=i%>" <%if(i == add_year){%>selected<%}%>> <%= opt_year %>年
<%
    }
%>
              </select>
           </font></div>
          </td>
        </tr>
      </table>
      </td>
    </tr>
    <tr>
      <td colspan="4"><img src="../../common/pc/image/spacer.gif" width="100" height="4"></td>
    </tr>
    <tr>
<%
    for( i = 1 ; i <= 12 ; i++ )
    {
        if( ((i-1) % 4) == 0)
        {
%>
    </tr>
    <tr>
<%
        }
%>
      <td>
        <table width="88" height="63" border="0" cellpadding="0" cellspacing="0" class="access">
          <tr>
            <td align="center" valign="middle">
              <a href="report_disp_f.jsp?Year=<%= year %>&Month=<%= i %>" target="_blank">
                <div class="size12"><%= year %>年</div>
                <div class="size18"><%= i %>月</div>
              </a>
            </td>
          </tr>
        </table>
      </td>
<%
    }
%>
    <tr>
      <td colspan="4"><img src="../../common/pc/image/spacer.gif" width="200" height="4"></td>
    </tr>
    <tr bgcolor="#666666">
      <td height="30" colspan="4">
        <table width="100%" height="30" border="0" cellpadding="0" cellspacing="0">
          <tr valign="middle" bgcolor="#666666">
            <td align="left">
              <div class="size14px">
              <img src="../../common/pc/image/spacer.gif" width="4" height="12">
              <font color="#FFFFFF">
              <a href="report_main.jsp?AddYear=<%= add_year-1 %><%=add_month%>" class="back" <%if (add_year <= limit_year){%>style="display:none"<%}%>>
              <img src="../../common/pc/image/yaji_w_l.gif" alt="前年" name="back2" width="17" height="14" border="0" align="absmiddle" id="back">前年
              </a>
              </font>
              </div>
            </td>
            <td align="right">
              <div class="size14px">
              <font color="#FFFFFF">
              <a href="report_main.jsp?AddYear=<%= add_year+1 %><%=add_month%>" class="back" <%if (add_year >= 0){%>style="display:none"<%}%>>翌年<img src="../../common/pc/image/yaji_w_r.gif" alt="翌年" name="next2" width="17" height="14" border="0" align="absmiddle" id="next">
              </a>
              </font>
              <img src="../../common/pc/image/spacer.gif" width="4" height="12">
              </div>
            </td>
          </tr>
        </table>
      </td>
    </tr>
<%
    if (imedia_user == 1 && level == 3)
    {
%>
    <tr bgcolor="#666666">
      <td height="30" colspan="4">
        <table width="100%" height="30" border="0" cellpadding="0" cellspacing="0">
          <tr valign="middle" bgcolor="#666666">
            <td align="left">
              <div class="size14px">
              <a href="#" target="_blank" onClick="window.open('ftp://<%= selecthotel %>:<%= ftp_passwd %>@<%= ftp_serv %>/pc/sales','','scrollbars=yes,toolbar=yes,width=830,height=840,resizable=yes');return false">
              フォルダ表示（ブラウザ表示の場合、再読込必須）</a><br>
               </div>
            </td>
          </tr>
        </table>
      </td>
    </tr>
<%
    }
%>
</table>

