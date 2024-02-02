<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String custom_id  = ReplaceString.getParameter(request,"custom_id");
    String limit_flag = ReplaceString.getParameter(request,"limit_flag");
    if(limit_flag == null) limit_flag ="true";
    if(limit_flag.compareTo("") == 0) limit_flag ="true";
    String detail_flag = ReplaceString.getParameter(request,"detail_flag");
    if(detail_flag == null) detail_flag ="false";
    if(detail_flag.compareTo("") == 0) detail_flag ="false";
    Calendar cal = Calendar.getInstance();
    int now_year  = cal.get(cal.YEAR);
    int now_month = cal.get(cal.MONTH) + 1;
    int now_day   = cal.get(cal.DATE);
    int nowdate   = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime   = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);
    String  nowdate_s = Integer.toString(nowdate+100000000);
    String  nowtime_s = Integer.toString(nowtime+1000000);
    nowdate_s = nowdate_s.substring(1,5) + "/" + nowdate_s.substring(5,7) + "/" + nowdate_s.substring(7,9);
    nowtime_s = nowtime_s.substring(1,3) + ":" + nowtime_s.substring(3,5) + ":" + nowtime_s.substring(5,7);

    String hotelid = (String)session.getAttribute("SelectHotel");
    String            query = "";
    String            name  = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection = DBConnection.getConnection();
    try
    {
        query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        result      = prestate.executeQuery();
        if( result != null )
        {
            if( result.next() != false )
            {
               name = result.getString("name");
            }
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
<title>景品交換レポート</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0" id="disp" style="display:block">
            <tr>
              <td width="100" height="20" bgcolor="#22333F" class="tab" align="center"><font color="#FFFFFF"><%=name%></font></td>
              <td width="140" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF">景品交換レポート</font></td>
              <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td width="200" height="20"  class="tab" align="right">
                <input type="checkbox" id="limit_flag" name="limit_flag" value="true" <% if (limit_flag.compareTo("true") == 0){%>checked<%}%> onclick="location.href='goods_report.jsp?<% if(custom_id != null){%>custom_id=<%=custom_id%>&<%}%>limit_flag=' + document.getElementById('limit_flag').checked + '&detail_flag=' + document.getElementById('detail_flag').checked">完了以外を表示
              </td>
              <td height="20" align="right" class="tab">
                <div><input type=button value="PRINT" onclick="document.getElementById('disp').style.display='none';document.getElementById('print').style.display='block';print();document.getElementById('print').style.display='none';document.getElementById('disp').style.display='block';">
                     <img src="../../common/pc/image/spacer.gif" width="100" height="6">
                     <input type=button value="再読込" onclick="location.href='goods_report.jsp?<% if(custom_id != null){%>custom_id=<%=custom_id%>&<%}%>limit_flag=' + document.getElementById('limit_flag').checked + '&detail_flag=' + document.getElementById('detail_flag').checked">
<%
    if(hotelid.equals("happyhotel"))
    {
%>
                     <input type="checkbox" id="detail_flag" name="detail_flag" value="true" <% if (detail_flag.compareTo("true") == 0){%>checked<%}%> onclick="location.href='goods_report.jsp?<% if(custom_id != null){%>custom_id=<%=custom_id%>&<%}%>detail_flag=' + document.getElementById('detail_flag').checked + '&limit_flag=' + document.getElementById('limit_flag').checked">明細を表示
                     <input type=button value="未確認分のCSV出力" onclick="location.href='goods_report_csv.jsp'">
<%
    }
%>
                     </div>
              </td>
<%
    if(custom_id != null)
    {
%>
              <td width="50" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" >
                 <INPUT name="submit_ret" type=button value=戻る onclick="location.href='goods_report.jsp'">
              </td>
<%
    }
%>
            </tr>
          </table>
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0" id="print" style="display:none">
            <tr>
              <td width="100" height="20" bgcolor="#22333F" class="tab" align="center"><font color="#FFFFFF"><%=name%></font></td>
              <td width="140" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF">景品交換レポート</font></td>
              <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20" align="right" class="tab">
                <div>
                     <%= nowdate_s %><img src="../../common/pc/image/spacer.gif" width="3" height="20">
                     <%= nowtime_s %><img src="../../common/pc/image/spacer.gif" width="15" height="20">
                </div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td bgcolor="#E2D8CF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="8" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" ></td>
              <td align="left" valign="top">

                <%-- ユーザ一覧表示 --%>
                <jsp:include page="goods_report_disp.jsp" flush="true" />
                <%-- ユーザ一覧表示ここまで--%>

              </td>
              <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
            </tr>
            <tr>
              <td height="4" valign="top">&nbsp;</td>
              <td align="left" valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
            </tr>
          </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
