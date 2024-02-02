<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
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
    String   loginHotelId =  (String)session.getAttribute("LoginHotelId");
    String   query;
    int    i;
    int    userid = 0;
    int    level[] = new int[20];
    String loginid = "";
    String machineid = "";
    String name = "";
    String passwd_pc = "";
    String passwd_mobile = "";
    String mailaddr_pc = "";
    String mailaddr_mobile = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    NumberFormat nf = new DecimalFormat("00");

    String param_userid = ReplaceString.getParameter(request,"UserId");
    if( param_userid != null )
    {
        connection        = DBConnection.getConnection();
        // ユーザ一覧・セキュリティ情報取得
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2,Integer.parseInt(param_userid));
        result      = prestate.executeQuery();
        if( result != null )
        {
            result.next();

            userid          = result.getInt("userid");
            loginid         = result.getString("loginid");
            machineid       = result.getString("machineid");
            name            = result.getString("name");
            passwd_pc       = result.getString("passwd_pc");
            passwd_mobile   = result.getString("passwd_mobile");
            mailaddr_pc     = result.getString("mailaddr_pc");
            mailaddr_mobile = result.getString("mailaddr_mobile");
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

        // セキュリティ情報取得
        query = "SELECT * FROM owner_user_security WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2,Integer.parseInt(param_userid));
        result      = prestate.executeQuery();
        if( result != null )
        {
            if( result.next() != false )
            {
                for( i = 0 ; i < 20 ; i++ )
                {
                    level[i] = result.getInt("sec_level" + nf.format(i+1));
                }
            }
        }
        DBConnection.releaseResources(result,prestate,connection);
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>マスター設定</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/user_datacheck.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<form action="user_delete_commit.jsp?UserId=<%= userid %>" method="post" name="userform" id="userform">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="100%" height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="150" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF">マスター設定（削除確認）</font></td>
              <td width="44" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td width="250" height="20">
                <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3955">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td bgcolor="#E2D8CF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
              <td align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="40" height="12"></td>
              <td width="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
            </tr>
            <tr>
              <td height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
              <td align="left" valign="top" class="size12"><font color="#000066">★現在の登録内容</font></td>
              <td valign="top">&nbsp;</td>
            </tr>
            <tr>
              <td height="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
              <td height="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
              <td height="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
            </tr>
            <tr>
              <td height="8" valign="top">&nbsp;</td>
              <td height="8" align="left" valign="top" class="size12">以下のユーザを削除します。よろしければ削除ボタンを押してください。</td>
              <td height="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
            </tr>
            <tr>
              <td height="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
              <td height="8" align="right" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"><input type=button onclick="history.back(); " value="戻る"></td>
              <td height="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
            </tr>
            <tr>
              <td width="8" height="4" valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="100"></td>
              <td align="left" valign="top">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="200" valign="middle" nowrap class="tableLN"><font class="space8">ユーザ名</font></td>
                    <td height="25" valign="middle" nowrap class="tableWhite">
                      <div class="size12">
                        <font class="space8">
                        <input name="loginid" type="hidden" id="loginid" value="<%= loginid %>">
                        <%= loginid %>
                        </font>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td valign="middle" nowrap class="tableNavy"><font class="space8">名前</font></td>
                    <td height="25" valign="middle" nowrap class="tableRW"><font class="space8">
                      <input name="name" type="hidden" id="name" value="<%= name %>">
                      <%= name %>
                    </font></td>
                  </tr>
                  <tr>
                    <td width="200" valign="middle" nowrap class="tableNavy"><font class="space8">PCパスワード</font></td>
                    <td height="25" valign="middle" nowrap class="tableRW"><div class="size12"><font class="space8">
                        <input name="passwd_pc" type="hidden" id="passwd_pc" value="<%= passwd_pc %>">
                        <%= passwd_pc %>
                      </font></div>
                    </td>
                  </tr>
                  <tr>
                    <td width="200" valign="middle" nowrap class="tableNavy"><font class="space8">携帯パスワード</font></td>
                    <td height="25" valign="middle" nowrap class="tableRW"><div class="size12"><font class="space8">
                        <input name="passwd_mobile" type="hidden" id="passwd_mobile" value="<%= passwd_mobile %>">
                        <%= passwd_mobile %>
                      </font></div>
                    </td>
                  </tr>
                  <tr>
                    <td width="200" valign="middle" nowrap class="tableNavy"><font class="space8">PCメールアドレス</font></td>
                    <td height="25" valign="middle" nowrap class="tableRW"><div class="size12"><font class="space8">
                        <input name="mailaddr_pc" type="hidden" id="mailaddr_pc" value="<%= mailaddr_pc %>">
                        <%= mailaddr_pc %>
                      </font></div>
                    </td>
                  </tr>
                  <tr>
                    <td width="200" valign="middle" nowrap class="tableNavy"><font class="space8">携帯メールアドレス</font></td>
                    <td height="25" valign="middle" nowrap class="tableRW"><div class="size12"><font class="space8">
                        <input name="mailaddr_mobile" type="hidden" id="mailaddr_mobile" value="<%= mailaddr_mobile %>">
                        <%= mailaddr_mobile %>
                      </font></div>
                    </td>
                  </tr>
                  <tr>
                    <td width="200" valign="middle" nowrap class="tableNavy"><font class="space8">携帯製造番号</font></td>
                    <td height="25" valign="middle" nowrap class="tableRW"><div class="size12"><font class="space8">
                        <input name="machineid" type="hidden" id="machineid" value="<%= machineid %>">
                        <%= machineid %>
                      </font></div>
                    </td>
                  </tr>
                  <tr align="center">
                    <td height="25" colspan="2" valign="middle" nowrap class="tableDG3">
                      <input name="Submit" type="submit" value="このデータを削除する" >
                    </td>
                  </tr>
</form>
	            </table>
               <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan=2><img src="../../common/pc/image/spacer.gif" width="3" height="10"></td>
				</tr>

				<tr>
					<td class="mainframe_text" colspan=2 >

                <%-- ユーザ一覧表示 --%>
                <jsp:include page="../../common/pc/owner_user_dispNew.jsp" flush="true" >
                   <jsp:param name="UserId" value="<%= userid %>" />
                </jsp:include>

					</td>
				</tr>
			</table>
            <tr>
              <td height="4" valign="top">&nbsp;</td>
              <td align="left" valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
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
        <td height="3" width="3955"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
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
