<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int    i;
    int    day = 1;
    int    add_year;

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

    Calendar cal = Calendar.getInstance();

    // 年を加算する
    cal.add(cal.YEAR, add_year);

    // 日付を１日にする
    cal.set(cal.get(cal.YEAR), cal.get(cal.MONTH), 1);

    int year = cal.get(cal.YEAR);
%>

<table border="0" cellpadding="2" cellspacing="0">
    <tr>
      <td height="30" colspan="4"><table width="100%" height="30" border="0" cellpadding="0" cellspacing="0">
        <tr valign="middle">
          <td align="center" bgcolor="#21323F">
            <div class="size14">
              <font color="#FFFFFF">
                <a href="hpreport_disp_f.jsp?Year=<%= year %>" class="back" onMouseOver="MM_swapImage('download','','/common/pc/image/download_o.gif',1)" onMouseOut="MM_swapImgRestore()" target="_blank"><img src="/common/pc/image/download.gif" alt="年次ダウンロード" name="download" width="13" height="14" border="0" align="absmiddle" id="download">&nbsp;<%= year %>年</a>
              </font>
            </div>
          </td>
        </tr>
      </table>
      </td>
    </tr>
    <tr>
      <td colspan="4"><img src="/common/pc/image/spacer.gif" width="100" height="4"></td>
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
              <a href="hpreport_disp_f.jsp?Year=<%= year %>&Month=<%= i %>" target="_blank">
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
      <td colspan="4"><img src="/common/pc/image/spacer.gif" width="200" height="4"></td>
    </tr>
    <tr bgcolor="#666666">
      <td height="30" colspan="4">
        <table width="100%" height="30" border="0" cellpadding="0" cellspacing="0">
          <tr valign="middle" bgcolor="#666666">
            <td align="left">
              <div class="size14px">
              <img src="/common/pc/image/spacer.gif" width="4" height="12">
              <font color="#FFFFFF">
              <a href="hpreport_main.jsp?AddYear=<%= add_year-1 %>" class="back">
              <img src="/common/pc/image/yaji_w_l.gif" alt="前年" name="back2" width="17" height="14" border="0" align="absmiddle" id="back">前年
              </a>
              </font>
              </div>
            </td>
            <td align="right">
              <div class="size14px">
              <font color="#FFFFFF">
              <a href="hpreport_main.jsp?AddYear=<%= add_year+1 %>" class="back">翌年<img src="/common/pc/image/yaji_w_r.gif" alt="翌年" name="next2" width="17" height="14" border="0" align="absmiddle" id="next">
              </a>
              </font>
              <img src="/common/pc/image/spacer.gif" width="4" height="12">
              </div>
            </td>
          </tr>
        </table>
      </td>
    </tr>
</table>


