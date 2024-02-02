<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.Constants" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page import="jp.happyhotel.common.DBSync" %>
<%@ page import="jp.happyhotel.common.DateEdit" %>
<%@ page import="jp.happyhotel.common.RandomString" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<%@ page import="jp.happyhotel.util.Base64Manager" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/SyncGcp_ini.jsp" %>
<%
    // 本日の日付取得
    Calendar calnd;
    calnd = Calendar.getInstance();
    int now_year   = calnd.get(calnd.YEAR);
    int now_month  = calnd.get(calnd.MONTH) + 1;
    int now_day    = calnd.get(calnd.DATE);
    int now_hour   = calnd.get(calnd.HOUR_OF_DAY);
    int now_minute = calnd.get(calnd.MINUTE);
    int now_second = calnd.get(calnd.SECOND);
    int now_date   = now_year * 10000 + now_month  * 100 + now_day;
    int now_time   = now_hour * 10000 + now_minute * 100 + now_second;
    NumberFormat  nf2;
    nf2    = new DecimalFormat("00");

    String  loginHotelId = (String)session.getAttribute("LoginHotelId");
    String  query;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    int               imedia_user = 0;
    int               level       = 0;
    int               unknown_flag_pc = 0;
    int               passwd_pc_update= 0;
    String            passwd_pc_now = "";
    String            derom_mail="";
    String            mailaddr_pc="";
    String            from_mail="";
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid= ? ";
        query = query + " AND userid=" + ownerinfo.DbUserId;
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,loginHotelId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            mailaddr_pc      = result.getString("mailaddr_pc");
            imedia_user      = result.getInt("imedia_user");
            level            = result.getInt("level");
            unknown_flag_pc  = result.getInt("unknown_flag_pc");
            passwd_pc_update = result.getInt("passwd_pc_update");
            passwd_pc_now    = result.getString("passwd_pc");
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
    String  group_name = "";  //ホテルグループ名
    try
    {
        query = "SELECT * FROM hotel WHERE hotel_id= ? ";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,loginHotelId);
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
        DBConnection.releaseResources(result,prestate,connection);
    }

    String  param_past   = ReplaceString.getParameter(request,"past");
    if (param_past == null)
    {
        param_past = "";
    }
    param_past           = ReplaceString.HTMLEscape(param_past); 
    String  param_confirm = ReplaceString.getParameter(request,"confirm");
    if (param_confirm == null)
    {
        param_confirm = "";
    }
    param_confirm         = ReplaceString.HTMLEscape(param_confirm); 
    String  passwd_pc = ReplaceString.getParameter(request,"passwd_pc");
    if (passwd_pc == null)
    {
        passwd_pc = "";
    }
    String  param_mailaddr_pc = ReplaceString.getParameter(request,"mailaddr_pc");
    if (param_mailaddr_pc != null)
    {
        param_mailaddr_pc   = ReplaceString.HTMLEscape(param_mailaddr_pc); 
        if (!param_mailaddr_pc.equals(mailaddr_pc))
        {
            unknown_flag_pc = 0;
            mailaddr_pc     = param_mailaddr_pc;
        }
    }
    String  passwd_pc_re   = ReplaceString.getParameter(request,"passwd_pc_re");
    if (passwd_pc_re == null)
    {
        passwd_pc_re = "";
    }
    passwd_pc_re           = ReplaceString.HTMLEscape(passwd_pc_re);

    String  param_token   = ReplaceString.getParameter(request,"token");
    if (param_token == null)
    {
        param_token = "";
    }
    param_token           = ReplaceString.HTMLEscape(param_token); 

    boolean ErrorMode   = false;
    String  ErrorMsg    = "";
    if (passwd_pc.equals(""))
    {
        ErrorMode = true;
        ErrorMsg  = "個人パスワードを入力してください。";
    }
    else if (passwd_pc.length() < Constants.MINIMUM_PASSWORD_LENGTH)
    {
        ErrorMode = true;
        ErrorMsg  = "個人パスワードは"+Constants.MINIMUM_PASSWORD_LENGTH+"文字以上入力してください。";
    }
    else if (!passwd_pc.matches(CheckString.getPasswordRegex()))
    {
        ErrorMode = true;
        ErrorMsg  = "「個人パスワード」は、英語の大文字・小文字・数字・記号の中から最低2種類を組み合わせて、" 
                        + Constants.MINIMUM_PASSWORD_LENGTH + "文字以上" + Constants.MAXIMUM_PASSWORD_LENGTH + "文字以下で入力してください。";
    }
    else if (passwd_pc.equals(passwd_pc_now))
    {
        ErrorMode = true;
        ErrorMsg  = "仮パスワードと同じです。個人パスワードを変更してください。";
    }
    else if (passwd_pc_re.equals(""))
    {
        ErrorMode = true;
        ErrorMsg  = "確認用パスワードを入力してください。";
    }
    else if (!passwd_pc_re.equals(passwd_pc))
    {
        ErrorMode = true;
        ErrorMsg  = "「個人パスワード」と「確認用パスワード」が一致しません。";
    }

    if (passwd_pc_update == 0)
    {
        int ret = 0;
        if(!ErrorMode)
        {
            try
            {
                query = "UPDATE owner_user SET ";
                query = query + " passwd_pc        = '" + ReplaceString.SQLEscape(passwd_pc)   + "',";
                query = query + " passwd_pc_update =  " + now_date;
                query = query + " WHERE hotelid= ? ";
                query = query + " AND userid=" + ownerinfo.DbUserId;
                connection = DBConnection.getConnection();
                prestate   = connection.prepareStatement(query);
                prestate.setString(1,loginHotelId);
                ret        = prestate.executeUpdate();
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
                query = "INSERT INTO hh_owner_user_log SET ";
                query = query + "hotel_id= ? , ";
                query = query + "user_id="             + ownerinfo.DbUserId    + ", ";
                query = query + "login_date="          + now_date              + ", ";
                query = query + "login_time="          + now_time              + ", ";
                query = query + "login_name= ? , ";
                query = query + "passwd='"             + ownerinfo.DbUserId    + "', ";
                query = query + "log_level=201,";
                query = query + "log_detail='【個人パスワード変更】'";
                connection = DBConnection.getConnection();
                prestate   = connection.prepareStatement(query);
                prestate.setString(1,loginHotelId);
                prestate.setString(2,loginHotelId);
                prestate.executeUpdate();
            }
            catch( Exception e )
            {
                Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
            }
            finally
            {
                DBConnection.releaseResources(result,prestate,connection);
            }
        }
        if (ret == 1)
        {
            // メールの送信（ハッピーホテル事務局宛）
            String title_mail = "";
            String text = "";
            title_mail  = "【ホテナビ】[個人パスワード更新]" + group_name + "（" + loginHotelId + "）";
            text = text +  "※情報収集ツールにより履歴を確認してください" + "\r\n";
            text = text +  "https://owner.hotenavi.com/happyhotel/owner/?HotelId=" + loginHotelId + "\r\n";
            text = text +  "\r\n";
            text = text +  "【ホテナビオーナーサイト】" + "\r\n";
            text = text +  "https://owner.hotenavi.com/" + loginHotelId + "/" + "\r\n";
            text = text +  "【ユーザーID】" + "\r\n";
            text = text +  ownerinfo.DbUserId + "\r\n";
            text = text +  "【ユーザー名】" + "\r\n";
            text = text +  ownerinfo.DbLoginUser + "\r\n";
            text = text +  "【名前】" + "\r\n";
            text = text +  ownerinfo.DbUserName + "\r\n";
            text = text +  "\r\n";
            SendMail sendmail = new SendMail();
            if (mailaddr_pc.equals("")) 
            {
                sendmail.send("imedia-info@hotenavi.com", "sakurai-t1@almex.jp", title_mail, text);
            }
            else
            {
                sendmail.send(mailaddr_pc, "sakurai-t1@almex.jp", title_mail, text);
                text =         "※ホテナビオーナーサイトの個人パスワードが変更されました。" + "\r\n";
                text = text +  "\r\n";
                text = text +  "【ホテナビオーナーサイト】" + "\r\n";
                text = text +  "https://owner.hotenavi.com/" + loginHotelId + "/" + "\r\n";
                text = text +  "【ユーザー名】" + "\r\n";
                text = text +  ownerinfo.DbLoginUser + "\r\n";
                text = text +  "【名前】" + "\r\n";
                text = text +  ownerinfo.DbUserName + "\r\n";
                text = text +  "【個人パスワード】" + "\r\n";
                text = text +  passwd_pc + "\r\n";
                text = text +  "\r\n";
                sendmail.send(from_mail, mailaddr_pc, title_mail, text);
            }

            query = "UPDATE hotenavi.owner_user SET passwd_pc_update=" + Integer.parseInt(DateEdit.getDate(2));
            query += " WHERE hotelid= '"+ loginHotelId  +"' AND userid = " + ownerinfo.DbUserId;
            if (apiUrl.indexOf("stg") != -1)
            {
                DBSync.publish(query,true);
            }
            else
            {
                DBSync.publish(query);
            }

%>
	  <tr valign="top">
		<td bgcolor="#E2D8CF" valign="top" class="size12" style="padding:10px 0px 0px 10px">
			<div id="passwordMessage"><font color=red><strong>個人パスワードが変更されました。</strong></font>しばらくお待ちください。</div>
		</td>
		<td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="26"></td>
	  </tr>
      <link href="../../common/pc/style/loading.css" rel="stylesheet" type="text/css" media="screen, print" />
      <script type="text/javascript" src="../../common/pc/scripts/jquery.js"></script>
      <script type="text/javascript" src="../../common/pc/scripts/loading.js"></script>
      <script type="text/javascript">
      $(function(){
          dispLoading('データ更新中...');
          setTimeout("updateUserData()",2000);
      });
      function updateUserData(){
          var JSONdata = {
              "token": "<%=param_token%>",
              "operate_hotelid": "<%=loginHotelId%>",
              "operate_userid": <%=ownerinfo.DbUserId%>,
              "update_hotelid": "<%=loginHotelId%>",
              "update_userid": <%=ownerinfo.DbUserId%>,
              "mailaddr_pc": "<%=Base64Manager.encode(mailaddr_pc)%>",
              "passwd_pc": "<%=Base64Manager.encode(passwd_pc)%>"
          };
//          console.log('<%=apiUrl%>');
//          console.log(JSON.stringify(JSONdata));
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
//                 console.log(JSON.stringify(responseJson));
              if (results.success) {
                 setTimeout("parent.topFrame.location.href='../../<%=loginHotelId%>/pc/ownermenu.jsp'",3000);
              } else {
                 $('#passwordMessage').html("<font color=red><strong>ハッピーホテルオーナーサイトのパスワード変更に失敗しました。</strong></font><br>"+results.error.message);
                 setTimeout("parent.topFrame.location.href='../../<%=loginHotelId%>/pc/ownermenu.jsp'",5000);
              }
          });
      }
	  </script>
<%
        }
        else
        {
            int[] TimesAfterMin = DateEdit.addSec(Integer.parseInt(DateEdit.getDate(2)),Integer.parseInt(DateEdit.getTime(1)), 300);
            String token = DateEdit.getDate(2) + DateEdit.getTime(1) + RandomString.getRandomString(50);
            try
            {
                connection  = DBConnection.getConnection();
                query = "INSERT INTO hotenavi.owner_user_token SET";
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
                prestate.setString(2, loginHotelId);
                prestate.setString(3, ownerinfo.DbLoginUser);
                prestate.setInt(4, ownerinfo.DbUserId);
                prestate.setString(5, loginHotelId);
                prestate.setInt(6, ownerinfo.DbUserId);
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
%>
	  <tr valign="top">
		<td bgcolor="#E2D8CF" valign="top" class="size12" style="padding:10px 0px 0px 10px">
			★仮パスワードなので、新しい個人パスワードを設定してください。
		</td>
		<td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="26"></td>
	  </tr>
<%
            if(ErrorMode)
            {
%>
	  <tr valign="top">
		<td bgcolor="#E2D8CF" valign="top" class="size12" style="padding:0px 0px 0px 10px">
			<font color=red><strong><%=ErrorMsg%></strong></font>
		</td>
		<td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="16"></td>
	  </tr>
<%
            }
%>
	  <tr valign="top">
		<td bgcolor="#E2D8CF" valign="top">
		<table width="98%" border="0" cellspacing="1" cellpadding="0" align="center">
			<form action="page.jsp" method="post" name="userform" id="userform">
			<input type="hidden" name=token   value="<%=token%>">
			<input type="hidden" name=past    value="<%=param_past%>">
			<input type="hidden" name=confirm value="<%=param_confirm%>">
			<tr> 
				<td class="tableLN" width="160">個人パスワード</td> 
				<td class="tableWhite"><input name="passwd_pc" type="password" class="tableWhite" id="passwd_pc" size="<%= Constants.MAXIMUM_PASSWORD_LENGTH %>" maxlength="<%= Constants.MAXIMUM_PASSWORD_LENGTH %>" style="ime-mode: disabled;" autocomplete="off"><font color=red>[必須]</font><div>※英語の大文字・小文字・数字・記号の中から最低2種類を組み合わせ<br>※半角英数<%=Constants.MINIMUM_PASSWORD_LENGTH%>文字以上<%= Constants.MAXIMUM_PASSWORD_LENGTH %>文字以内</div></td> 
			</tr> 
			<tr> 
				<td class="tableLN" width="160">個人パスワード(再度)</td> 
				<td class="tableWhite"><input name="passwd_pc_re" type="password" class="tableWhite" id="passwd_pc_re" size="<%= Constants.MAXIMUM_PASSWORD_LENGTH %>" maxlength="<%= Constants.MAXIMUM_PASSWORD_LENGTH %>" style="ime-mode: disabled;" autocomplete="off"><div>※コピーせずに再度入力してください。</div></td> 
			</tr> 
			<tr>
				<td class="tableLN" width="160">PCメールアドレス</td>
				<td class="tableWhite"><input name="mailaddr_pc" type="text" class="tableWhite" id="mailaddr_pc" value="<%= mailaddr_pc %>" size="40" maxlength="80" style="ime-mode: disabled;" autocomplete="off" readonly><% if(unknown_flag_pc==1){%><font color="#FF0000">&nbsp;Unknown</font><%}%>
					<div>※登録済みのメールアドレスに設定した個人パスワードが送信されます。</div>
				</td>
			</tr>
			<tr>
				<td class="size12" style="background-color:#FFFFFF;padding:5px" colspan=2 align=center>
					<input type="button" onclick="document.userform.submit()" value="パスワード変更">
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
<%
        }
    }
%>
