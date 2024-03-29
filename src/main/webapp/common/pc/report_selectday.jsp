<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int    i;
    int    day = 1;
    int    add_month;
    String add_year;
    int    limit_month =(-1)*7*12; //7年

    String param = ReplaceString.getParameter(request,"Add");
    if( param == null )
    {
        add_month = 0;
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
        add_month = Integer.valueOf(param).intValue();
    }

    add_year = ReplaceString.getParameter(request,"AddYear");
    if (add_year == null)
    {
        add_year = "0";
    }
    if( !CheckString.numCheck(add_year) )
    {
        add_year = "0";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
    }
    add_year = "&AddYear="+add_year;

    Calendar cal = Calendar.getInstance();

    // 月を加算する
    cal.add(cal.MONTH, add_month);

    // 日付を１日にする
    cal.set(cal.get(cal.YEAR), cal.get(cal.MONTH), 1);

    int year = cal.get(cal.YEAR);
    int month = cal.get(cal.MONTH) + 1;
    int max_month = cal.getActualMaximum(cal.DAY_OF_MONTH);
    int start = cal.get(cal.DAY_OF_WEEK);
%>

  <table border="0" cellspacing="0" cellpadding="2">
    <tr>
      <td height="30" colspan="7"><table width="100%" height="30" border="0" cellpadding="0" cellspacing="0">
          <tr valign="middle">
            <td align="center" bgcolor="#21323F"><div class="size14"><font color="#FFFFFF">
              <select onchange="location.href='report_main.jsp?Add='+this.value+'<%=add_year%>';">
<%
    for (i=limit_month ; i <= 0 ;  i++)
    {
        Calendar optCal = Calendar.getInstance();
        optCal.add(cal.MONTH, i);
        int opt_year = optCal.get(cal.YEAR);
        int opt_month = optCal.get(cal.MONTH) + 1;
%>                <option value="<%=i%>" <%if(i == add_month){%>selected<%}%>> <%= opt_year %>年<%= opt_month %>月
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
      <td colspan="7"><img src="../../common/pc/image/spacer.gif" width="100" height="4"></td>
    </tr>
    <tr>
      <td>
        <table width="50" height="16" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="middle" bgcolor="#AF0011"><div class="size10"><font color="#FFFFFF">日</font></div>
            </td>
          </tr>
        </table>
      </td>
      <td>
        <table width="50" height="16" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="middle" bgcolor="#000066"><div class="size10"><font color="#FFFFFF">月</font></div>
            </td>
          </tr>
        </table>
      </td>
      <td>
        <table width="50" height="16" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="middle" bgcolor="#000066"><div class="size10"><font color="#FFFFFF">火</font></div>
            </td>
          </tr>
        </table>
      </td>
      <td>
        <table width="50" height="16" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="middle" bgcolor="#000066"><div class="size10"><font color="#FFFFFF">水</font></div>
            </td>
          </tr>
        </table>
      </td>
      <td>
        <table width="50" height="16" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="middle" bgcolor="#000066"><div class="size10"><font color="#FFFFFF">木</font></div>
            </td>
          </tr>
        </table>
      </td>
      <td>
        <table width="50" height="16" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="middle" bgcolor="#000066"><div class="size10"><font color="#FFFFFF">金</font></div>
            </td>
          </tr>
        </table>
      </td>
      <td>
        <table width="50" height="16" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="middle" bgcolor="#000066"><div class="size10"><font color="#FFFFFF">土</font></div>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>

<!-- 第１週目の表示 -->
<%
    day = 1;

    // 空白の日付
    for( i = 1 ; i < start ; i++ )
    {
%>
      <td>
        <table width="50" height="50" border="0" cellpadding="0" cellspacing="0" class="access">
          <tr>
            <td align="center" valign="middle">
              <a href="#"><div class="size18"> </div></a>
            </td>
          </tr>
        </table>
      </td>
<%
    }

    for( i = start ; i < 8 ; i++ )
    {
        if( i == 1 )
        {
%>

      <td>
        <table width="50" height="50" border="0" cellpadding="0" cellspacing="0" class="access">
          <tr>
            <td align="center" valign="middle" bgcolor="#FF0000">
              <a href="report_disp_f.jsp?Year=<%=year%>&Month=<%=month%>&Day=<%=day%>" target="_blank">
              <div class="size18"> <%= day %></div></a>
            </td>
          </tr>
        </table>
      </td>

<%
        }
        else if( i == 7 )
        {
%>
      <td>
        <table width="50" height="50" border="0" cellpadding="0" cellspacing="0" class="access">
          <tr>
            <td align="center" valign="middle" bgcolor="#00FFFF">
              <a href="report_disp_f.jsp?Year=<%=year%>&Month=<%=month%>&Day=<%=day%>" target="_blank">
              <div class="size18"> <%= day %></div></a>
            </td>
          </tr>
        </table>
      </td>
<%
        }
        else
        {
%>
      <td>
        <table width="50" height="50" border="0" cellpadding="0" cellspacing="0" class="access">
          <tr>
            <td align="center" valign="middle">
              <a href="report_disp_f.jsp?Year=<%=year%>&Month=<%=month%>&Day=<%=day%>" target="_blank">
              <div class="size18"> <%= day %></div></a>
            </td>
          </tr>
        </table>
      </td>
<%
        }

        day++;
    }
%>
<!-- 第１週目の表示ここまで -->

    </tr>

<!-- 第２週目以降 -->
<%
    while( day <= max_month )
    {
%>
    <tr>
<%
        for( i = 1 ; i <= 7 && day <= max_month ; i++ )
        {
            if( i == 1 )
            {
%>
      <td>
        <table width="50" height="50" border="0" cellpadding="0" cellspacing="0" class="access">
          <tr>
            <td align="center" valign="middle" bgcolor="#FF0000">
              <a href="report_disp_f.jsp?Year=<%=year%>&Month=<%=month%>&Day=<%=day%>" target="_blank">
              <div class="size18"> <%= day %></div></a>
            </td>
          </tr>
        </table>
      </td>
<%
            }
            else if( i == 7 )
            {
%>
      <td>
        <table width="50" height="50" border="0" cellpadding="0" cellspacing="0" class="access">
          <tr>
            <td align="center" valign="middle" bgcolor="#00FFFF">
              <a href="report_disp_f.jsp?Year=<%=year%>&Month=<%=month%>&Day=<%=day%>" target="_blank">
              <div class="size18"> <%= day %></div></a>
            </td>
          </tr>
        </table>
      </td>
<%
            }
            else
            {
%>
      <td>
        <table width="50" height="50" border="0" cellpadding="0" cellspacing="0" class="access">
          <tr>
            <td align="center" valign="middle">
              <a href="report_disp_f.jsp?Year=<%=year%>&Month=<%=month%>&Day=<%=day%>" target="_blank">
              <div class="size18"> <%= day %></div></a>
            </td>
          </tr>
        </table>
      </td>
<%
            }

            day++;
        }
%>
    </tr>

<%
    }
%>
<!-- 第２週目以降ここまで -->

    <tr>
      <td colspan="7"><img src="../../common/pc/image/spacer.gif" width="200" height="4"></td>
    </tr>
    <tr>
      <td height="30" colspan="7">
        <table width="100%" height="30" border="0" cellpadding="0" cellspacing="0">
          <tr valign="middle" bgcolor="#666666">
            <td align="left" bgcolor="#666666">
              <div class="size14px">
              <img src="../../common/pc/image/spacer.gif" width="4" height="12">
              <font color="#FFFFFF">
              <a class="back" href="report_main.jsp?Add=<%= add_month-1 %><%=add_year%>" <%if (add_month <= limit_month){%>style="display:none"<%}%>>
              <img src="../../common/pc/image/yaji_w_l.gif" alt="前月" name="back2" width="17" height="14" border="0" align="absmiddle" id="back">前月
              </a>
              </font>
              </div>
          </td>
            <td align="right">
              <div class="size14px">
              <font color="#FFFFFF">
              <a class="back" href="report_main.jsp?Add=<%= add_month+1 %><%=add_year%>" <%if (add_month >= 0){%>style="display:none"<%}%>>翌月<img src="../../common/pc/image/yaji_w_r.gif" alt="翌月" name="next2" width="17" height="14" border="0" align="absmiddle" id="next">
              </a>
              </font>
              <img src="../../common/pc/image/spacer.gif" width="4" height="12">
              </div>
            </td>
          </tr>
        </table>
      </td>
    </tr>
      </td>
    </tr>
  </table>
