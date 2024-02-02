<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>
<%
    String loginHotelId  = (String)session.getAttribute("LoginHotelId");
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>

<%
    String data_type = ReplaceString.getParameter(request,"DataType");
    if( data_type == null )
    {
        data_type = "1";
    }

    int i;
    String msg_data;
    String msg_title;
    String msg_title_color;

%>
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<META http-equiv="Content-Style-Type" content="text/css">
<title>プレビュー</title>
<% if (loginHotelId.compareTo("hotenavi") != 0){%>
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<%}%>
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr valign="top">
    <td>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="10" height="20"><img src="../../common/pc/image/spacer.gif" width="10" height="20"></td>
          <td height="20"><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
          <td width="20" height="20"><img src="../../common/pc/image/spacer.gif" width="20" height="20"></td>
        </tr>
        <tr>
          <td width="10"><img src="../../common/pc/image/spacer.gif" width="10" height="20"></td>
          <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="titlebar">
<%
if (data_type.compareTo("1") == 0 || data_type.compareTo("2") == 0)
{
%>
				<tr>
					<td align="left" valign="middle" ><img src="../../common/pc/image/title_new.gif"  alt="What's new">
					</td>
				</tr>
<%
}
else if (data_type.compareTo("3") == 0 || data_type.compareTo("4") == 0)
{
%>
				<tr>
					<td align="left" valign="middle" ><img src="../../common/pc/image/title_event.gif"  alt="イベント情報">
					</td>
				</tr>
<%
}
%>            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><img src="../../common/pc/image/spacer.gif" width="500" height="16"></td>
              </tr>
            </table>
          </td>
          <td><img src="../../common/pc/image/spacer.gif" width="20" height="20"></td>
        </tr>


<%
if (Integer.parseInt(data_type) % 2 == 1)
    {
        // PCコンテンツ
%>

        <tr>
          <td width="10"><img src="../../common/pc/image/spacer.gif" width="10" height="40"></td>
          <td>
<%
        if( ReplaceString.getParameter(request,"col_title_color").length() != 0 )
        {
%>
			<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
				<tr>
					<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
					<td width="3"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
					<td width="3" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
					<td width="450">
						<div class="subtitle_text"><font color="<%= ReplaceString.getParameter(request,"col_title_color") %>"><%= ReplaceString.getParameter(request,"col_title") %></font></div>
					</td>
				</tr>
            </table>
<%
        }
%>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="10"><img src="../../common/pc/image/spacer.gif" width="10" height="20"></td>
                <td>

<%
        for( i = 1 ; i <= 8 ; i++ )
        {
            msg_data = "col_msg" + Integer.toString(i);
            msg_data  = msg_data.replace("<iframe","<span");
            msg_data  = msg_data.replace("</iframe","</span");
            msg_title = "col_msg" + Integer.toString(i) + "_title";
            msg_title_color = "col_msg" + Integer.toString(i) + "_title_color";

            if( ReplaceString.getParameter(request,msg_title).length() != 0 )
            {
%>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td><img src="../../common/pc/image/spacer.gif" width="4" height="16"></td>
						</tr>
					</table>
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="honbun_margin">
						<tr>
							<td width="4" class="honbuntitlebar_left"><img src="../../common/pc/image/spacer.gif" width="4" height="16"></td>
							<td class="honbuntatitlebar_text"><img src="../../common/pc/image/spacer.gif" width="6" height="16" border="0" align="absmiddle"><font color="<%= ReplaceString.getParameter(request,msg_title_color) %>"><%= ReplaceString.getParameter(request,msg_title) %></font></td>
						</tr>
					</table>
<%
            }
            if( ReplaceString.getParameter(request,msg_data).length() != 0 )
            {
%>
                  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td><img src="../../common/pc/image/spacer.gif" width="40" height="8"></td>
                    </tr>
                  </table>
                  <table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td align="left" valign="top" class="honbun"  colspan="2">

<%
      String firstString  = ReplaceString.getParameter(request,msg_data);

      firstString = firstString.replace("<br>","<br/>");
      firstString = firstString.replace("<BR>","<br/>");
      firstString = firstString.replace(">\r\n","><br/>");
      firstString = firstString.replace("\r\n","<br/><br/>");
      firstString = firstString.replace("><br/>",">\r\n");

	  String taisyoString = "src=\"image/";
	  String henkanString = "src=\"http://www.hotenavi.com/" + hotelid +  "/image/";
      String afterString = "";
      //対象文字列を判断しCRLFなどの改行コードを考慮してインプリメントするポインタ数を制御する。
      int plusPoint = taisyoString.length();
      int startPoint = 0;
      int endPoint   = firstString.indexOf(taisyoString, startPoint);

      //文字列に対象文字列がない場合、そのままの文字列を戻す
      //文字列に対象文字列がある間、以下の処理を繰り返す
      while (endPoint != -1){
        //文字列から対象文字列を元に検索行い変換文字列に置換する。
        afterString = afterString + firstString .substring(startPoint, endPoint) + henkanString;
        startPoint  = endPoint + plusPoint;
        endPoint    = firstString .indexOf(taisyoString, startPoint);
      }
      afterString = afterString + firstString.substring(startPoint);

%>

						<div class="honbun_margin">
						<%= afterString %>
						</div>
						</td>
                    </tr>
                  </table>

<%
            }
        }
%>
                </td>
                <td width="10"><IMG src="../../common/pc/image/spacer.gif" width="10" height="20"></td>
              </tr>
            </table>
            <table width="100%" height="8" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td><img src="../../common/pc/image/spacer.gif" width="500" height="24"></td>
              </tr>
            </table>
          </td>
          <td>&nbsp;</td>
        </tr>
<%
    }
else if (Integer.parseInt(data_type) % 2 == 0)
    {
        // 携帯コンテンツ
%>

                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                      <tr valign="top">
                        <td width="8" align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left">


<table height="350" border="2" cellpadding="3" cellspacing="0" bordercolor="#666666">
  <tr>
    <td width="150" valign="top" bgcolor="#FFFFFF">
タイトルページ<br>
<br>
<font color="<%= ReplaceString.getParameter(request,"col_title_color") %>"><%= ReplaceString.getParameter(request,"col_title") %></font><br>
<hr>
リンク選択後<br>
<br>
<%
        for( i = 1 ; i <= 8 ; i++ )
        {
            msg_data = "col_msg" + Integer.toString(i);
            msg_data  = msg_data.replace("<iframe","<span");
            msg_data  = msg_data.replace("</iframe","</span");
            msg_title = "col_msg" + Integer.toString(i) + "_title";
            msg_title_color = "col_msg" + Integer.toString(i) + "_title_color";

            if( ReplaceString.getParameter(request,msg_title).length() != 0 )
            {
%>
<font color="<%= ReplaceString.getParameter(request,msg_title_color) %>"><%= ReplaceString.getParameter(request,msg_title) %></font><br>
<%= ReplaceString.getParameter(request,msg_data) %><br>
<hr>
<%
            }
        }
%>
    </td>
  </tr>
</table>

                        </td>
                      </tr>
                      <tr>
                        <td colspan="2" align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                  </table>

<%
    }
%>


        <tr>
          <td width="10">&nbsp;</td>
          <td><img src="../../common/pc/image/spacer.gif" width="500" height="50"></td>
          <td>&nbsp;</td>
        </tr>
      </table>
    </td>
    <td width="2"><img src="../../common/pc/image/spacer.gif" width="2" height="100"></td>
  </tr>
  <tr valign="bottom">
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="center" valign="middle"><div class="copyright">Copyright &copy; almex inc, All Rights Reserved</div></td>
        </tr>
        <tr>
          <td><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
        </tr>
      </table></td>
    <td width="2"><img src="../../common/pc/image/spacer.gif" width="2" height="30"></td>
  </tr>
</table>
</body>
</html>

