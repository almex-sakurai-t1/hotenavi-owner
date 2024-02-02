<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.mailmagazine.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }

    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid.compareTo("all") == 0 )
    {
        hotelid = (String)session.getAttribute("HotelId");
    }

    String query;
    boolean editok = false;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection        = DBConnection.getConnection();
    NumberFormat nf = new DecimalFormat("00");

    query = "SELECT * FROM mag_data WHERE hotel_id=?";
    query = query + " AND state <=2";
    query = query + " ORDER BY history_id DESC";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1, hotelid);
    result      = prestate.executeQuery();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>メルマガ履歴</title>
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
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="120" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF">履歴・編集<%=hotelid%></font></td>
              <td width="15" height="20"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td align="center" valign="top" bgcolor="#FFF5EE"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
            </tr>
          </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td></td>
              </tr>
              <tr>
                <td valign="top">
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr valign="top">
                        <td width="8" align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td align="left" class="size12"><table width="100%" border="0" cellspacing="0" cellpadding="3" class="tbl">
                          <tr class="tbl">
                            <td height="24" align="center" valign="middle" nowrap bgcolor="#8CE8FF" class="size12">&nbsp;番号&nbsp;</td>
                            <td height="24" nowrap bgcolor="#8CE8FF" class="size12">&nbsp;件　　名</td>
                            <td height="24" nowrap bgcolor="#8CE8FF" class="size12">&nbsp;発行（予定）日時</td>
                            <td height="24" nowrap bgcolor="#8CE8FF" class="size12">&nbsp;対　象</td>
                            <td height="24" align="center" valign="middle" nowrap bgcolor="#8CE8FF" class="size12">発行数</td>
                            <td height="24" align="center" valign="middle" nowrap bgcolor="#8CE8FF" class="size12">&nbsp;</td>
                          </tr>
<%
    while( result.next() != false )
    {
        editok = false;

    String param_subject = result.getString("subject");
	char c;
	int  c_index = 0;
	int  i       = 0;
	StringBuffer sb = new StringBuffer();

	for( i = 0 ; i < param_subject.length() ; i++ )
	{
		c = param_subject.charAt(i);
		switch( c )
		{
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			default :
				sb.append(c);
				break;
		}
	}
	param_subject = sb.toString();
  int state = result.getInt("state");
    int send_flag = result.getInt("send_flag");
  if (state == 0 && !result.getString("recipient_id").equals("") && !result.getString("mail_id").equals(""))
  {
    if (LogicMagazineSend.check(hotelid ,result.getInt("history_id")))
    {
      state = 1;
    }
  }
%>
                          <form action="magazine_history_data.jsp?history=<%= result.getInt("history_id") %>" method="post">
                          <tr bgcolor="#FFFFFF">
                            <td height="24" align="center" valign="middle" class="tbl"><%= result.getInt("history_id") %></td>
                            <td height="24" nowrap class="tbl"><%= param_subject %></td>
                            <td height="24" nowrap class="tbl">
<%
        if( state == 0 )
        {
          if (result.getString("recipient_id").equals("") || result.getString("mail_id").equals("") ) 
          {
            editok = true;
%>                            送信失敗（<%= result.getInt("send_date") / 10000 %>/<%= result.getInt("send_date") / 100 % 100 %>/<%= result.getInt("send_date") % 100 %> <%= result.getInt("send_time") / 100 %>：<%= nf.format(result.getInt("send_time") % 100) %>）
<%
          } else if( send_flag == 1 )
            {
                editok = true;
%>                            配信準備中（<%= result.getInt("send_date") / 10000 %>/<%= result.getInt("send_date") / 100 % 100 %>/<%= result.getInt("send_date") % 100 %> <%= result.getInt("send_time") / 100 %>：<%= nf.format(result.getInt("send_time") % 100) %>）
<%
            }
            else
            {
              editok = true;
%>                            配信準備中（<%= result.getInt("send_date") / 10000 %>/<%= result.getInt("send_date") / 100 % 100 %>/<%= result.getInt("send_date") % 100 %> <%= result.getInt("send_time") / 100 %>：<%= nf.format(result.getInt("send_time") % 100) %>）
<%
            }
        }
        else if( state == 1 )
        {
%>                            配信済み(<%= result.getInt("send_date") / 10000 %>/<%= result.getInt("send_date") / 100 % 100 %>/<%= result.getInt("send_date") % 100 %> <%= result.getInt("send_time") / 100 %>：<%= nf.format(result.getInt("send_time") % 100) %>)
<%
        }
        else if( state == 2 )
        {
%>                            削除済み
<%
        }
%></td>
<%
        if( result.getInt("condition_all") == 0 )
        {
%>
                            <td height="24" nowrap class="tbl">全員</td>
<%
        }
        else if( result.getInt("condition_all") == 1 )
        {
%>
                            <td height="24" nowrap class="tbl">ビジターのみ</td>
<%
        }
        else if( result.getInt("condition_all") == 2 )
        {
%>
                            <td height="24" nowrap class="tbl">メンバーのみ</td>
<%
        }
        else if( result.getInt("condition_all") == 3 )
        {
%>
                            <td height="24" nowrap class="tbl">メンバー（条件指定）</td>
<%
        }
%>

                            <td height="24" align="center" valign="middle" nowrap class="tbl"><%= result.getInt("count") %></td>
<%
        if( editok != false )
        {
%>
                            <td height="24" align="center" valign="middle" nowrap class="tbl"><input type="submit" name="Submit" value="編集・削除">
<%
        }
        else
        {
%>
                            <td height="24" align="center" valign="middle" nowrap class="tbl"><input type="submit" name="Submit" value="参照">
<%
        }
%>
                            </td>
                          </tr>
                          </form>
<%
    }
    DBConnection.releaseResources(result,prestate,connection);
%>
                        </table></td>
                        <td align="left" class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left">&nbsp;</td>
                      </tr>
                  </table>
                </td>
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
      <!-- ここまで -->
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
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
