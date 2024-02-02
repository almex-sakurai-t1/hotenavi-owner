<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<%@ page import="com.hotenavi2.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/SyncGcp_ini.jsp" %>
<%
    String   loginHotelId =  (String)session.getAttribute("LoginHotelId");
    int      loginUserId  =   ownerinfo.DbUserId;
    String   loginUserName = "";
    String  userid = ReplaceString.getParameter(request,"UserId");
    if( userid == null )
    {
        userid = "0";
    }
    Calendar cal = Calendar.getInstance();
    int now_year  = cal.get(cal.YEAR);
    int now_month = cal.get(cal.MONTH) + 1;
    int now_day   = cal.get(cal.DATE);
    int nowdate   = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime   = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);
    int      i;
    String   query     = "";
    int      ret       = 0;
    int      ret_error = 0;
    Connection db      = null;
    PreparedStatement  st      = null;
    ResultSet  result  = null;
    Connection        dbh  = null;
    PreparedStatement sth  = null;
    ResultSet         reth = null;
    DbAccess    db_update;
    NumberFormat nf = new DecimalFormat("00");

    int      edit_flag  = 0;
    int      log_level  = 0;
    String   log_detail = "";
    String   mail_message = "";

//入力担当者のPCメールアドレスを求める
    String  input_mail    = "";
    query = "SELECT * FROM owner_user WHERE hotelid=?";
    query = query + " AND  userid=?";
    db     = DBConnection.getConnection();
    st     =db.prepareStatement(query);
    st.setString(1, loginHotelId);
    st.setInt(2, loginUserId);
    result = st.executeQuery();
    if( result.next() != false )
    {
        input_mail       = result.getString("mailaddr_pc");
        loginUserName    = result.getString("name");
    }
    DBConnection.releaseResources(result,st,db);
    if (input_mail.compareTo("") == 0)
    {
        query = "SELECT * FROM mag_hotel WHERE hotel_id=?";
        db     = DBConnection.getConnection();
        st     =db.prepareStatement(query);
        st.setString(1, loginHotelId);        result = st.executeQuery();
        if( result.next() != false )
        {
            input_mail       = result.getString("address");
        }
        DBConnection.releaseResources(result,st,db);
    }
    if (input_mail.compareTo("") == 0)
    {
        input_mail = "imedia-info@hotenavi.com";
    }

//履歴データ読み込み（メール送信チェックのため）
    int   history_seq  = 1;  // データがなければ1
    query = "SELECT * FROM hh_owner_user_log WHERE login_name=?";
    query = query + " AND passwd=?";
    query = query + " AND hotel_id=?";
    query = query + " AND log_level >= 200";
    query = query + " AND log_level <= 299";
    query = query + " ORDER BY seq DESC";
    Connection dbseq      = null;
    PreparedStatement  stseq      = null;
    ResultSet  retseq     = null;
    dbseq = DBConnection.getConnection();
    stseq = dbseq.prepareStatement(query);
    stseq.setString(1, loginHotelId);
    stseq.setString(2, Integer.toString(loginUserId));
    stseq.setString(3, loginHotelId);
    retseq = stseq.executeQuery();
    if( retseq.next() != false )
    {
         history_seq = retseq.getInt("seq") + 1;
         if (retseq.getString("login_name").compareTo(loginHotelId) != 0) history_seq = 1;
         if (retseq.getInt("log_level") < 200 || retseq.getInt("log_level") > 299) history_seq = 1;
         if (retseq.getString("passwd").compareTo(Integer.toString(loginUserId)) != 0) history_seq = 1;
    }
    DBConnection.releaseResources(retseq,stseq,dbseq);
%>
<%
//●1.owner_user_hotel 書き込み,削除処理
    int    old_report_daily_pc     = 0;
    int    old_report_daily_mobile = 0;
    int    old_report_month_pc     = 0;
    int    old_report_month_mobile = 0;
    int    old_report_times        = 0;
    int    old_report_timee        = 0;
    dbh   = DBConnection.getConnection();

//1.(1)管理店舗の登録店舗読み込み&履歴書き込み

    query  = "SELECT * FROM owner_user_hotel WHERE hotelid =? ";
    query  = query + " AND  userid = ? ";
    sth    = dbh.prepareStatement(query);
    sth.setString(1, loginHotelId);
    sth.setInt(2, Integer.parseInt(userid));
    reth   = sth.executeQuery();
    if (reth != null)
    {
        while (reth.next() != false)
        {
            old_report_daily_pc     = reth.getInt("report_daily_pc");
            old_report_daily_mobile = reth.getInt("report_daily_mobile");
            old_report_month_pc     = reth.getInt("report_month_pc");
            old_report_month_mobile = reth.getInt("report_month_mobile");
            old_report_times        = reth.getInt("report_times");
            old_report_timee        = reth.getInt("report_timee");
            log_detail = "【管理店舗削除】";
            log_detail = log_detail + reth.getString("accept_hotelid") + "\r\n";
            log_level = 222;
            query = "INSERT INTO hh_owner_user_log SET ";
            query = query + "hotel_id='"           + ReplaceString.SQLEscape(loginHotelId)        + "', ";
            query = query + "user_id="             + Integer.parseInt(userid)                + ", ";
            query = query + "login_date="          + nowdate                                 + ", ";
            query = query + "login_time="          + nowtime                                 + ", ";
            query = query + "login_name='"         + loginHotelId                            + "', ";
            query = query + "passwd='"             + loginUserId                           + "', ";
            query = query + "log_level="           + log_level                               + ", ";
            query = query + "log_detail='"         + ReplaceString.SQLEscape(log_detail)        + "' ";
            db_update =  new DbAccess();
            ret = db_update.execUpdate(query);
            db_update.close();
            mail_message  = mail_message + log_detail;
            log_detail    = "";
        }
    }
    DBConnection.releaseResources(reth);
    DBConnection.releaseResources(sth);
%>
<%


//●2.(1) セキュリティファイル読み込み
    int    old_admin_flag = 0;
    int    old_level[]  = new int[20];
    int   ret_security = 0;
    query  = "SELECT * FROM owner_user_security WHERE hotelid=?";
    query  = query + " AND  userid=?";
    db     = DBConnection.getConnection();
    st     = db.prepareStatement(query);
    st.setString(1, loginHotelId);
    st.setInt(2, Integer.parseInt(userid));
    result = st.executeQuery();
    if (result != null )
    {
        if (result.next() != false)
        {
            log_detail = "【権限削除】"+ "\r\n";
            log_level = 210;
            old_admin_flag      = result.getInt("admin_flag");
            for(i = 1; i <= 20;i++)
            {
                old_level[i-1]      = result.getInt("sec_level" + nf.format(i));
            }
        }
    }
    DBConnection.releaseResources(result,st,db);

//2.(2) 履歴への書き込み処理
    if (log_detail.compareTo("") != 0)
    {
        log_detail = log_detail + " 削除前･･･";
        log_detail = log_detail + old_admin_flag + ",";
        log_detail = log_detail + old_level[0]  + old_level[1]  + old_level[2]  + old_level[6]  + ",";
        log_detail = log_detail + old_level[8]  + ",";
        log_detail = log_detail + old_level[3]  + old_level[4]  + old_level[5]  + ",";
        log_detail = log_detail + old_level[8]  + old_level[10] + ",";
        log_detail = log_detail + old_level[14] + old_level[15] + old_level[16] + old_level[17] + " ";
        log_detail = log_detail + "\r\n";

        query = "INSERT INTO hh_owner_user_log SET ";
        query = query + "hotel_id='"           + ReplaceString.SQLEscape(loginHotelId)        + "', ";
        query = query + "user_id="             + Integer.parseInt(userid)                + ", ";
        query = query + "login_date="          + nowdate                                 + ", ";
        query = query + "login_time="          + nowtime                                 + ", ";
        query = query + "login_name='"         + loginHotelId                            + "', ";
        query = query + "passwd='"             + loginUserId                           + "', ";
        query = query + "log_level="           + log_level                               + ", ";
        query = query + "log_detail='"         + ReplaceString.SQLEscape(log_detail)        + "' ";
        db_update =  new DbAccess();
        ret = db_update.execUpdate(query);
        db_update.close();
        mail_message  = mail_message + log_detail;
        log_detail    = "";
    }
%>
<%

//●3.owner_user 読み込み
    String old_loginid         = "";
    String old_machineid       = "";
    String old_name            = "";
    String old_passwd_pc       = "";
    String old_passwd_mobile   = "";
    String old_mailaddr_pc     = "";
    String old_mailaddr_mobile = "";
    int    old_imedia_user     = 0;
    int    old_report_flag     = 0;

//3.(1) ユーザ一登録チェック
    query  = "SELECT * FROM owner_user WHERE hotelid=?";
    query  = query + " AND  userid=?";
    db     = DBConnection.getConnection();
    st     =db.prepareStatement(query);
    st.setString(1, loginHotelId);
    st.setInt(2, Integer.parseInt(userid));
    result = st.executeQuery();
    if( result != null )
    {
        if(result.next() != false)
        {
            old_loginid         = result.getString("loginid");
            old_machineid       = result.getString("machineid");
            old_name            = result.getString("name");
            old_passwd_pc       = result.getString("passwd_pc");
            old_passwd_mobile   = result.getString("passwd_mobile");
            old_mailaddr_pc     = result.getString("mailaddr_pc");
            old_mailaddr_mobile = result.getString("mailaddr_mobile");
            old_imedia_user     = result.getInt("imedia_user");
            old_report_flag     = result.getInt("report_flag");
            log_level = 201;
            log_detail = "【基本削除】"+ "\r\n";
            if (old_loginid.compareTo("") != 0)        log_detail = log_detail + "ログインID:"           + ReplaceString.SQLEscape(old_loginid) + ",\r\n";
            if (old_name.compareTo("") != 0)           log_detail = log_detail + "ユーザー名:"           + ReplaceString.SQLEscape(old_name) + ",\r\n";
            if (old_mailaddr_pc.compareTo("") != 0)    log_detail = log_detail + "PCメールアドレス:"     + ReplaceString.SQLEscape(old_mailaddr_pc) + ",\r\n";
            if (old_mailaddr_mobile.compareTo("") != 0)log_detail = log_detail + "携帯メールアドレス:"   + ReplaceString.SQLEscape(old_mailaddr_mobile) + ",\r\n";
            if (old_machineid.compareTo("") != 0)      log_detail = log_detail + "携帯製造番号:"         + ReplaceString.SQLEscape(old_machineid) + ",\r\n";
            if (old_imedia_user == 1) log_detail = log_detail + "アルメックスユーザー:" + ",\r\n";
            if (old_report_flag == 1) log_detail = log_detail + "売上メール設定:"       + ",\r\n";
        }
    }

//3.(2) 履歴への書き込み処理
    if (log_detail.compareTo("") != 0)
    {
        query = "INSERT INTO hh_owner_user_log SET ";
        query = query + "hotel_id='"           + ReplaceString.SQLEscape(loginHotelId)        + "', ";
        query = query + "user_id="             + Integer.parseInt(userid)                + ", ";
        query = query + "login_date="          + nowdate                                 + ", ";
        query = query + "login_time="          + nowtime                                 + ", ";
        query = query + "login_name='"         + loginHotelId                            + "', ";
        query = query + "passwd='"             + Integer.toString(loginUserId)         + "', ";
        query = query + "log_level="           + log_level                               + ", ";
        query = query + "log_detail='"         + ReplaceString.SQLEscape(log_detail)        + "' ";
        db_update =  new DbAccess();
        ret = db_update.execUpdate(query);
        db_update.close();
        mail_message  = mail_message + log_detail;
        log_detail    = "";
    }
%>
<%

//●削除処理
    db_update =  new DbAccess();
    query = "DELETE FROM hotenavi.owner_user_hotel WHERE hotelid='" + loginHotelId + "'";
    query = query + " AND userid=" + userid;
    // SQLクエリーの実行
    db_update.execUpdate(query);
    db_update.close();
    if (apiUrl.indexOf("stg") != -1)
    {
        DBSync.publish(query,true);
    }
    else
    {
        DBSync.publish(query);
    }

    db_update =  new DbAccess();
    query = "DELETE FROM hotenavi.owner_user_security WHERE hotelid='" + loginHotelId + "'";
    query = query + " AND userid=" + userid;
    // SQLクエリーの実行
    db_update.execUpdate(query);
    db_update.close();
    if (apiUrl.indexOf("stg") != -1)
    {
        DBSync.publish(query,true);
    }
    else
    {
        DBSync.publish(query);
    }

    db_update =  new DbAccess();
    query = "DELETE FROM  hotenavi.owner_user WHERE hotelid='" + loginHotelId + "'";
    query = query + " AND userid=" + userid;
    // SQLクエリーの実行
    ret = db_update.execUpdate(query);
    db_update.close();
    if (apiUrl.indexOf("stg") != -1)
    {
        DBSync.publish(query,true);
    }
    else
    {
        DBSync.publish(query);
    }

if  (history_seq == 1)
{
//●4.メールの送信
    String  group_name = "";
    query = "SELECT * FROM hotel WHERE hotel_id=?";
    db     = DBConnection.getConnection();
    st     =db.prepareStatement(query);
    st.setString(1, loginHotelId);
    result = st.executeQuery();
    if( result.next() != false )
    {
        group_name     = result.getString("name");
    }
    DBConnection.releaseResources(result,st,db);

    String title_mail = "";
    String text = "";
    title_mail  = "【ホテナビ】[オーナー管理削除]" + group_name + "（" + loginHotelId + "）";
    text = text +  "※初回更新時のみメールしています。他の更新については、情報収集ツールにより履歴を確認してください" + "\r\n";
    text = text +  "https://owner.hotenavi.com/happyhotel/owner/?loginHotelId=" + loginHotelId + "\r\n";
    text = text +  "\r\n";
    text = text +  "【ホテナビオーナーサイト】" + "\r\n";
    text = text +  "http://owner.hotenavi.com/" + loginHotelId + "/" + "\r\n";
    text = text +  "【ユーザーID】" + "\r\n";
    text = text +  userid + "\r\n";
    text = text +  "【ユーザー名】" + "\r\n";
    text = text +  old_loginid + "\r\n";
    text = text +  "【名前】" + "\r\n";
    text = text +  old_name + "\r\n";
    text = text +  mail_message + "\r\n";
    text = text +  "\r\n";
    text = text +  "【更新担当者】" + "\r\n";
    text = text +  "(" + loginUserId + ")" + loginUserName + " 様\r\n";
    SendMail sendmail = new SendMail();
//  sendmail.send(input_mail, "sakurai-t1@almex.jp", title_mail, text);
    sendmail.send(input_mail, "all_imedia@almex.jp", title_mail, text);
}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>ユーザ管理</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="100" height="20" nowrap bgcolor="#22333F" class="tab">
                <font color="#FFFFFF">削除確認</font></td>
              <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
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
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
          </tr>
          <tr>
            <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="size12">
<%
    if( ret == 1 )
    {
%>
削除しました。<br>
<%
    }
    else
    {
%>
削除に失敗しました。(<%= ret %>)
<%
    }

%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12">
                      <div align="right">
                        <input name="Submit" type="submit" onClick="MM_goToURL('parent.frames[\'mainFrame\']','user_form.jsp?UserId=0');return document.MM_returnValue" value="新規作成する">
                      </div>
                      &nbsp;</td>
                  </tr>
                  <tr>
                    <td class="size12">
                        <jsp:include page="owner_user_dispNew.jsp" flush="true" >
                             <jsp:param name="UserId" value="" />
                        </jsp:include>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td></td>
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
