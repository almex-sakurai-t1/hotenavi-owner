<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.DBSync" %>
<%@ page import="jp.happyhotel.common.DateEdit" %>
<%@ page import="jp.happyhotel.common.RandomString" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<%@ page import="jp.happyhotel.util.Base64Manager" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/SyncGcp_ini.jsp" %>
<%
    String requestUri = request.getRequestURI();
    boolean DebugMode = false;
    if (requestUri.indexOf("_debug_") != -1)
    {
       DebugMode = true;
    }
    String hotelid    = requestUri.replace("_debug_","");
    hotelid           = hotelid.replace("pc/passwd_reminder.jsp","");
    hotelid           = hotelid.replace("/","");

    String loginid              = ReplaceString.getParameter(request,"loginid");
    if    (loginid == null)     loginid = "";
    if( !loginid.equals("") && !CheckString.loginIdCheck(loginid) )
    {
        loginid = "";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
     }

    String mailaddr_pc          = ReplaceString.getParameter(request,"mailaddr_pc");
    if    (mailaddr_pc == null) mailaddr_pc = "";
    
    boolean RemindMode = false;
    if (!loginid.equals("") && !mailaddr_pc.equals(""))
    {
        RemindMode = true;
    }

    Calendar cal = Calendar.getInstance();
    int now_year  = cal.get(cal.YEAR);
    int now_month = cal.get(cal.MONTH) + 1;
    int now_day   = cal.get(cal.DATE);
    int nowdate   = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime   = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    int     userid    = 0;
%>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J"> 
<meta http-equiv="Pragma" content="no-cache"> 
<title>パスワードリマインダー</title> 
</head> 
<body background="../../common/pc/image/bg.gif"> 
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css"> 
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0"> 
<tr> 
  <td valign="top"> 
    <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
      <tr> 
        <td height="3" bgcolor="#999999"> 
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0"> 
            <tr> 
              <td bgcolor="#E2D8CF"><img src="../../common/pc/image/spacer.gif" width="3" height="3"></td> 
            </tr> 
          </table> 
        </td> 
        <td height="3" width="3"><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td> 
      </tr> 
	  <tr valign="top"> 
		<td bgcolor="#E2D8CF" valign="top" class="size12" style="padding:0px 0px 0px 10px"> 
			★個人パスワード再発行
		</td> 
		<td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="26"></td> 
	  </tr> 
<%
    if (RemindMode)
    {
        String  query = "";
        String  name       = "";
        String  group_name = "";
        String  from_mail = "";
        String  passwd_pc = "";
        Connection        connection  = null;
        PreparedStatement prestate    = null;
        ResultSet         result      = null;
        connection        = DBConnection.getConnection();
        try
        {
            query = "SELECT * FROM hotel WHERE hotel_id=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,  hotelid);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                from_mail      = result.getString("mail");
                if (from_mail.equals("")) from_mail = "imedia-info@hotenavi.com"; 
                group_name     = result.getString("name");
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
            query = "SELECT * FROM owner_user WHERE hotelid= ? ";
            query = query + " AND loginid='"     + ReplaceString.HTMLEscape(loginid) + "'";
            query = query + " AND mailaddr_pc='" + ReplaceString.HTMLEscape(mailaddr_pc) + "'";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,  hotelid);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                userid       = result.getInt("userid");
                name         = result.getString("name");
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

        if (userid != 0)
        {
            passwd_pc =  RandomString.getOwnerPassword();
            query = "UPDATE owner_user SET passwd_pc_update=0,passwd_pc ='"+ passwd_pc + "' WHERE hotelid= ? AND userid=" + userid;
            connection  = DBConnection.getConnection();
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,  hotelid);
            prestate.executeUpdate();
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);

            // メールの送信（パスワード）
            String title_mail = "";
            String text = "";
            title_mail  = "【ホテナビ】[オーナーサイトパスワード]" + group_name + "（" + hotelid + "）";
            text =         "※ホテナビオーナーサイトの仮パスワードが発行されました。" + "\r\n";
            text = text +  "\r\n";
            text = text +  "【ホテナビオーナーサイト】" + "\r\n";
            text = text +  "https://owner.hotenavi.com/" + hotelid + "/" + "\r\n";
            text = text +  "【名前】" + "\r\n";
            text = text +  name + "\r\n";
            text = text +  "【個人ユーザー名】" + "\r\n";
            text = text +  loginid + "\r\n";
            text = text +  "【個人パスワード（仮）】" + "\r\n";
            text = text +  passwd_pc + "\r\n";
            text = text +  "\r\n";
            text = text +  "------------------\r\n";
            text = text +  "オーナーサイトにログインし、個人パスワードを変更してください。" + "\r\n";
            text = text +  "------------------\r\n";
            SendMail sendmail = new SendMail();
            sendmail.send(from_mail, mailaddr_pc, title_mail, text);
            sendmail.send(from_mail, "sakurai-t1@almex.jp", title_mail, text);

            String UserAgent = request.getHeader("user-agent");
            if (UserAgent.length() > 255)
            {
                UserAgent = UserAgent.substring(0,255);
            }
            query = "INSERT INTO hh_owner_user_log SET ";
            query = query + "hotel_id='"           + ReplaceString.SQLEscape(hotelid)   + "', ";
            query = query + "user_id="             + userid                             + ", ";
            query = query + "login_date="          + nowdate                                 + ", ";
            query = query + "login_time="          + nowtime                                 + ", ";
            query = query + "login_name='"         + ReplaceString.SQLEscape(loginid)        + "', ";
            query = query + "passwd='"             + passwd_pc                               + "', ";
            query = query + "log_level=230,";
            query = query + "log_detail='パソコン（仮）パスワード発行',";
            query = query + "user_agent='"   + UserAgent       + "', ";
            query = query + "remote_ip='"    + ( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() )  + "'  ";
            prestate    = connection.prepareStatement(query);
            prestate.executeUpdate();
            DBConnection.releaseResources(result,prestate,connection);

            int[] TimesAfterMin = DateEdit.addSec(Integer.parseInt(DateEdit.getDate(2)),Integer.parseInt(DateEdit.getTime(1)), 60);
            String token = DateEdit.getDate(2) + DateEdit.getTime(1) + RandomString.getRandomString(50);
            try
            {
                connection  = DBConnection.getConnection();
                query = "REPLACE INTO hotenavi.owner_user_token SET";
                query = query + " token=?";
                query = query + ",operate_hotelid=?";
                query = query + ",operate_loginid=?";
                query = query + ",operate_userid=?";
                query = query + ",update_hotelid=?";
                query = query + ",update_userid=?";
                query = query + ",limit_date=?";
                query = query + ",limit_time=?";
                prestate   = connection.prepareStatement(query);
                prestate.setString(1, token);
                prestate.setString(2, hotelid);
                prestate.setString(3, loginid);
                prestate.setInt(4, userid);
                prestate.setString(5, hotelid);
                prestate.setInt(6, userid);
                prestate.setInt(7, TimesAfterMin[0]);
                prestate.setInt(8, TimesAfterMin[1]);
                prestate.executeUpdate();

                String sql = prestate.toString().split(":",2)[1];
                if (apiUrl.indexOf("stg") != -1)
                {
                    DBSync.publish(sql,true);
                }
                else
                {
                    DBSync.publish(sql);
                }
                //UPDATE
                query = "UPDATE hotenavi.owner_user SET passwd_pc_update=0 ";
                query += " WHERE hotelid= '"+ hotelid +"' AND userid = " + userid;
                if (apiUrl.indexOf("stg") != -1)
                {
                    DBSync.publish(query,true);
                }
                else
                {
                    DBSync.publish(query);
                }
            }
            catch( Exception e )
            {
                Logging.error("foward Exception e=" + e.toString() );
            }
            finally
            {
                DBConnection.releaseResources(prestate);
                DBConnection.releaseResources(connection);
            }
%><link href="../../common/pc/style/loading.css" rel="stylesheet" type="text/css" media="screen, print" />
      <script type="text/javascript" src="../../common/pc/scripts/jquery.js"></script>
      <script type="text/javascript" src="../../common/pc/scripts/loading.js"></script>
      <script type="text/javascript">
      $(function(){
          dispLoading('データ更新中...');
          setTimeout("updateUserData()",2000);
      });
      function updateUserData(){
          var JSONdata = {
              "token": "<%=token%>",
              "operate_hotelid": "<%=hotelid%>",
              "operate_userid": <%=userid%>,
              "update_hotelid": "<%=hotelid%>",
              "update_userid": <%=userid%>,
              "mailaddr_pc": "<%=Base64Manager.encode(mailaddr_pc)%>",
              "passwd_pc": "<%=Base64Manager.encode(passwd_pc)%>"
          };
          $.ajax({
              type : 'POST',
              url : '<%=apiUrl%>',
              headers: {
                  'Content-Type': 'application/json'
              },
              data : JSON.stringify(JSONdata),
              dataType : 'JSON',
              scriptCharset: 'UTF-8'
          }).done(function (responseJson) {
              removeLoading();
              var results = responseJson.results;
              if (results.success) {
              } else {
                 $('#passwordMessage').html("<font color=red><strong>仮パスワード発行に失敗しました。</strong></font><br>しばらく経ってから再度発行手続きをしてください。");
              }
          });
      }
      </script>
	  <tr valign="top"> 
		<td bgcolor="#E2D8CF" valign="top" class="size12" style="padding:0px 0px 0px 10px"> 
		    <div id="passwordMessage">
			<font color=red><strong>仮パスワードが発行されました。</strong></font><br>
			メールが送信されていますので、オーナーサイトにログインし、個人パスワードを変更してください。
			</div>
		</td> 
		<td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="50"></td> 
	  </tr> 
<%
        }
        else
        {
%>
	  <tr valign="top"> 
		<td bgcolor="#E2D8CF" valign="top" class="size12" style="padding:0px 0px 0px 10px"> 
			<font color=red><strong>入力した個人ユーザー名もしくはPCメールアドレスが違います。</strong></font> 
			<br>
			<br>
			<input type=button onclick="location.href='passwd_reminder.jsp'" value="再入力する">
		</td> 
		<td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="26"></td> 
	  </tr> 
<%
        }
    }
    else
    {
%>
	  <tr valign="top"> 
		<td bgcolor="#E2D8CF" valign="top" class="size12" style="padding:0px 0px 0px 10px"> 
			<font color=red><strong>「個人パスワード（仮）」を再発行しますので、個人ユーザー名とPCメールアドレスを入力してください。</strong></font> 
		</td> 
		<td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="26"></td> 
	  </tr> 
	  <tr valign="top"> 
		<td bgcolor="#E2D8CF" valign="top"> 
		<table width="98%" border="0" cellspacing="1" cellpadding="0" align="center"> 
			<form action="passwd_reminder.jsp" method="post" name="userform" id="userform"> 
			<input type="hidden" name=past    value=""> 
			<input type="hidden" name=confirm value=""> 
			<tr> 
				<td class="tableLN" width="130">個人ユーザー名</td> 
				<td class="tableWhite"><input name="loginid" type="text" class="tableWhite" id="loginid" size="20" maxlength="10" style="ime-mode: disabled;" autocomplete="off"></td> 
			</tr> 
			<tr> 
				<td class="tableLN" width="130">PCメールアドレス</td> 
				<td class="tableWhite"><input name="mailaddr_pc" type="text" class="tableWhite" id="mailaddr_pc" size="40" maxlength="60" style="ime-mode: disabled;" autocomplete="off"></td> 
			</tr> 
			<tr> 
				<td class="size12" style="background-color:#FFFFFF;padding:5px" colspan=2 align=center> 
					<input type="button" onclick="document.userform.submit()" value="パスワード発行"> 
				</td> 
			</tr> 
		</from> 
		</table> 
		</td> 
		<td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3"></td> 
	  </tr> 
	  <tr valign="top"> 
		<td bgcolor="#E2D8CF" valign="top"> 
			<img src="../../common/pc/image/spacer.gif" width="3" height="3"> 
		</td> 
		<td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3"></td> 
	  </tr>
 	  <tr valign="top"> 
		<td bgcolor="#E2D8CF" valign="top" class="size12" style="padding:0px 0px 0px 10px"> 
		※「個人ユーザー名」がわからない場合、「PCメールアドレス」の登録がない場合は、管理者までお問い合わせください。<br>
		</td> 
		<td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="26"></td> 
	  </tr> 
	  <tr valign="top"> 
		<td bgcolor="#E2D8CF" valign="top"> 
			<img src="../../common/pc/image/spacer.gif" width="3" height="3"> 
		</td> 
		<td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3"></td> 
	  </tr>
 	  <tr valign="top"> 
		<td bgcolor="#E2D8CF" valign="top" class="size12" style="padding:0px 0px 0px 10px"> 
		※「PCメールアドレス」に仮パスワードが送信されますので、ログイン後「個人パスワード」を変更してください。<br>
		</td> 
		<td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="26"></td> 
	  </tr> 
	  <tr valign="top"> 
		<td bgcolor="#E2D8CF" valign="top"> 
			<img src="../../common/pc/image/spacer.gif" width="3" height="3"> 
		</td> 
		<td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3"></td> 
	  </tr>
<%
    }
%> 
      <tr> 
        <td height="3" bgcolor="#999999"> 
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0"> 
            <tr> 
              <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td> 
              <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="3" height="3"></td> 
            </tr> 
          </table> 
        </td> 
        <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td> 
      </tr> 
    </table> 
    </td> 
  </tr> 
  <tr> 
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="2"></td> 
  </tr> 
  <tr> 
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td> 
  </tr> 
  <tr> 
    <td align="center" valign="middle" class="size10"><img src="../../common/pc/image/spacer.gif" width="300" height="2"></td> 
  </tr> 
</table> 
 
</body> 
</html> 