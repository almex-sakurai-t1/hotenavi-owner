<%
/**
 * Hello JSP BBS G ver 1.0
 * Copyright(C) Hirokazu Tanaka 2002-2003 All rights reserved.
 * E-MAIL : hiro@hellohiro.com
 * WWW : http://www.hellohiro.com/
 *
 * [必須ライブラリ]
 * 「Hello ファイルアップロードライブラリ」が必要になります。WEB-INF/libフォルダに
 * 「fileupload.jar」ファイルを置いてください。
 *
 */
%>
<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.awt.*,java.util.*,java.io.*,java.net.*,java.text.*,com.hellohiro.servlet.upload.*" %>
<%!
/***************
*** 基本設定始 **
****************/

private static final String BG_COLOR       = "#DAFAD3"; // 背景色
private static final String TEXT_COLOR     = "#0E4600"; // テキストの色
private static final String SUBJECT_COLOR  = "#0E4600"; // 記事の題名の色
private static final String TEXT_POINT     = "10pt"; // 本文の文字のサイズ
private static final String TEXT_HIGHT     = "12pt"; // 本文の文字の高さ

private static final String LINK_COLOR     = "#0000BB"; // リンク
private static final String VLINK_COLOR    = "#BB0000"; // 訪問済みリンク
private static final String HLINK_COLOR    = "#FF0000"; // マウスホーバーリンク
private static final String ALINK_COLOR    = "#FF0000"; // アクティブリンク

private static final boolean USE_IMG       = false; // タイトルに画像を使う：true　使わない：false
private static final String IMG_PATH       = "./title.gif"; // タイトルに画像を使う場合の画像のパス
private static final String TITLE          = "画像Uploadテスト"; // タイトル
private static final String TITLE_COLOR    = "#0E4600"; // タイトルの文字の色
private static final String TITLE_POINT    = "20pt"; // タイトルの文字のサイズ

private static final String HOME_URL       = "/index.html"; // 戻り先のURL
private static final String LOG_FILE       = "htbbsg.log"; // ログファイル名
private static final String COUNT_FILE     = "count.dat"; // 記事番号保存ファイル
private static final boolean PAST_LOG      = true; // 過去ログ機能：true　あり :false なし
private static final String PAST_DIR       = "past"; // 過去ログを保存するフォルダ名
private static final String PAST_COUNT     = "pastcount.dat"; // 過去ログカウントファイル名
private static final int MAX_PAST_LINE     = 200; // 過去ログの1ファイル当たりの最大行数
private static final int MAX               = 50; // 最大親記事保持数
private static final int PAGE_MAX          = 10; // １ページ当りの最大表示親記事数
private static final String ADMIN_PASSWD   = "0123"; // 管理者パスワード
private static final boolean URL_AUTO_LINK = true; // URLの自動リンクをする：true しない：false
private static final boolean NORMAL_RES    = true; // 通常の返信機能を使う：true 使わない:false
private static final boolean TOP_SORT      = true; // 通常の返信がつくと親記事を一番上へ移動する：true しない：false

private static final boolean TOMCAT3       = false; // Tomcat 3　で文字化けする場合trueに設定

private static final boolean SEND_MAIL     = false; // 管理者にメールを送信する場合trueに設定
// 管理者メールアドレス(メールを送信する場合)
private static final String ADMIN_MAIL     = "sakurai-t1@almex.jp";
// sendmailパス(メールを送信する場合)
private static final String SENDMAIL_PATH  = "/usr/sbin/sendmail";

private static final int SUB_MAX_LEN       = 15; // 題名の長さ(これ以上だと省略されます)

// ファイルアップロード関連の設定
private static final String IMG_DIR        = "image"; // アップロードした画像を保存するフォルダ
private static final int MAX_POST_SIZE     = 1024 * 500; // 送信するデータの最大サイズ(バイト)
private static final int IMG_WIDTH         = 200;  // 表示する際の画像の最大幅


// カラーリストとカラー名リストは対応するように設定してください
// カラーリスト
private static final String[] COLOR_LIST = { "800000" ,
                                             "000000" ,
                                             "DF0000" ,
                                             "008040" ,
                                             "0000FF" ,
                                             "C100C1" ,
                                             "FF80C0" ,
                                             "FF8040" ,
                                             "000080" };
// カラー名リスト
private static final String[] COLOR_NAME_LIST = { "茶" ,
                                                  "黒" ,
                                                  "赤" ,
                                                  "みどり" ,
                                                  "青" ,
                                                  "紫" ,
                                                  "ピンク" ,
                                                  "オレンジ" ,
                                                  "あい色" };

// JSPファイルがあるディレクトリのパスが取得できずに、
// ファイルの書き込みができない場合に指定してください。
private static final String JSP_BASE_DIR   = ""; // JSPファイルがあるディレクトのパス(例）"/home/tomcat/webapps/bbs/"
/***************
*** 基本設定終 **
****************/

/** ここから先は変更しないでください **/
// バージョン
private static final String VERSION = "1.0";
// モード定数
private static final String MODE_NORMAL   = "normal"; // 通常
private static final String MODE_REGIST   = "regist"; // 登録
private static final String MODE_USER_DEL = "userdel"; // 記事削除
private static final String MODE_HOW_TO   = "howto"; // 使い方
private static final String MODE_PAST     = "past"; // 過去ログ
private static final String MODE_LOG      = "log"; // 過去ログ表示
private static final String MODE_SEARCH   = "search"; // 過去ログ検索
private static final String MODE_ADMIN    = "admin"; // 管理用
private static final String MODE_RES_MSG  = "resmsg"; // 返信
// クッキーを発行するリスト
private static final String[] COOKIE_NAMES = { "name" ,
                                               "email" ,
                                               "url" ,
                                               "passwd" ,
                                               "color" };


// アップロードを許可する画像の拡張子
private static final String[] EXTENSION_LIST = { "gif",
                                                 "jpg",
                                                 "jpeg",
                                                 "png" };

// 日付のフォーマット形式
private static final String DATE_PATTERN = "yyyy/MM/dd(EEE) HH:mm:ss";
// ログロック
private Object lock  = new Object();
// 過去ログロック
private Object plock = new Object();
%>
<%
// パス関連
String requestUri = request.getRequestURI();
String contextPath = request.getContextPath();
if (requestUri.indexOf(contextPath)==0) {
  requestUri = requestUri.substring(contextPath.length() + 1);
}

String JSP_PATH = application.getRealPath(requestUri);
String JSP_FILE = JSP_PATH.substring(JSP_PATH.lastIndexOf(File.separator) + 1, JSP_PATH.length());
String BASE_PATH = JSP_PATH.substring(0 , JSP_PATH.lastIndexOf(File.separator) + 1);
if (!JSP_BASE_DIR.equals("")) {
  if (!JSP_BASE_DIR.substring(JSP_BASE_DIR.length()-1).equals(File.separator)) {
    BASE_PATH = JSP_BASE_DIR + File.separator;
  } else {
    BASE_PATH = JSP_BASE_DIR;
  }
}
String LOG_PATH = BASE_PATH + LOG_FILE;
String COUNT_PATH = BASE_PATH + COUNT_FILE;
String PAST_COUNT_PATH = BASE_PATH + PAST_COUNT;
String PAST_DIR_PATH = BASE_PATH + PAST_DIR;
String IMG_DIR_PATH = BASE_PATH + IMG_DIR;

File uploadFile = null;
int start,end;
Hashtable params = new Hashtable();

// MIMEタイプを取得
String contentType = request.getContentType();
if (contentType!=null) {
  contentType = contentType.toLowerCase();
}

if (contentType!=null && contentType.startsWith("multipart/form-data")) { // マルチパートの場合
  // マルチパート用リクエスト取得
  FileUploadRequest uploadRequest;
  try {
    uploadRequest = new FileUploadRequest(request ,IMG_DIR_PATH,MAX_POST_SIZE);
  } catch (ExceededLimitException e) {
      showErrorHTML(out,"アップロードできる最大容量を超えています",JSP_FILE);
      return;
  } catch (FileSizeZeroException e) {
      showErrorHTML(out,"アップロードするファイルのサイズが0です",JSP_FILE);
      return;
  } catch (Exception e) {
      showErrorHTML(out,"アップロード中にエラーが発生しました",JSP_FILE);
      e.printStackTrace();
      return;
  }
  // 画像関連
  uploadFile = uploadRequest.getFile("uploadfile");
  if (uploadFile!=null) {
    String extension = uploadRequest.getFileExtension("uploadfile");
    if (extension==null) {
      uploadFile.delete();
      showErrorHTML(out,"アップロードするファイルの拡張子がありません",JSP_FILE);
      return;
    }
    extension = extension.toLowerCase();
    if (!fileExtensionCheck(extension)) {
      uploadFile.delete();
      showErrorHTML(out,"拡張子が「" + extension + "」のファイルはアップロードできません",JSP_FILE);
      return;
    }
    try {
      ImageInfomation imageInfo = new ImageInfomation(uploadFile);
      if (!imageInfo.checkImage()) {
        uploadFile.delete();
        showErrorHTML(out,"画像のフォーマットが不正です",JSP_FILE);
        return;
      }
      int imgWidth = imageInfo.getWidth();
      if (imgWidth > IMG_WIDTH) {
        imgWidth = IMG_WIDTH;
      }
      params.put("imgwidth" , String.valueOf(imgWidth));
    } catch (Exception e) {
      e.printStackTrace();
      uploadFile.delete();
      showErrorHTML(out,"画像のフォーマットが不正です",JSP_FILE);
      return;
    }
    params.put("originalfilename" , uploadRequest.getOriginalFileName("uploadfile"));
    params.put("filesystemname" , uploadRequest.getFilesystemName("uploadfile"));
    params.put("filesystempath" , uploadFile.getPath());
    params.put("fileextension" , uploadRequest.getFileExtension("uploadfile"));
    params.put("filecontenttype" , uploadRequest.getFileContentType("uploadfile"));
  }

  // ページ切り替え関連
  String strStart = uploadRequest.getParameter("start");
  if (strStart==null) {
    strStart = "1";
  }
  try {
    start = Integer.parseInt(strStart);
    if (start<1 || start > MAX) {
      showErrorHTML(out,"パラメータが不正です",JSP_FILE);
      return;
    }
    end = start + PAGE_MAX -1;
  } catch (Exception e) {
      showErrorHTML(out,e.toString(),JSP_FILE);
      return;
  }
  // パラメーターを取得
  Enumeration paramNames = uploadRequest.getParameterNames();
  while (paramNames.hasMoreElements()) {
    String paramName = (String)paramNames.nextElement();
    if (paramName.equals("del")) {
      String[] del = uploadRequest.getParameterValues("del");
      params.put("del" , del);
    } else {
      params.put(paramName,uploadRequest.getParameter(paramName));
    }
  }

} else { // マルチパートでない場合
  // ページ切り替え関連
  String strStart = request.getParameter("start");
  if (strStart==null) {
    strStart = "1";
  }
  try {
    start = Integer.parseInt(strStart);
    if (start<1 || start > MAX) {
      showErrorHTML(out,"パラメータが不正です",JSP_FILE);
      return;
    }
    end = start + PAGE_MAX -1;
  } catch (Exception e) {
      showErrorHTML(out,e.toString(),JSP_FILE);
      return;
  }
  // パラメーターを取得
  Enumeration paramNames = request.getParameterNames();
  while (paramNames.hasMoreElements()) {
    String paramName = (String)paramNames.nextElement();
    if (paramName.equals("del")) {
      String[] del = request.getParameterValues("del");
      params.put("del" , del);
    } else {
      params.put(paramName,toSJIS(request.getParameter(paramName)));
    }
  }
}

// 現在の日付を設定
SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN,Locale.US);
params.put("date" , sdf.format(new Date()));
// ホスト名を設定
String host = request.getRemoteHost();
if (host==null) {
  host = "不明";
}
params.put("host" , host);
// ブラウザ名を取得
String userAgent = request.getHeader("User-Agent");
params.put("user-agent" , userAgent);

// モードを取得
String mode = (String)params.get("mode");
if (mode==null) {
  mode = MODE_NORMAL;
}
boolean showdata = false;
boolean showhowto = false;
boolean showlogin = false;
boolean showadmin = false;
boolean showpast = false;
boolean showlog = false;
boolean showsearch = false;
String msg = null;
Hashtable mailData = new Hashtable();
if (mode.equals(MODE_NORMAL)) { /** 通常表示 **/
  showdata = true;
} else if (mode.equals(MODE_HOW_TO)) { /** 使い方 **/
  showhowto = true;
} else if (mode.equals(MODE_ADMIN)) { /** 管理用 **/
  if (params.get("passwd")==null) {
    showlogin = true;
  } else {
    // HTTPメソッドのチェック
    if (!request.getMethod().equalsIgnoreCase("POST")) {
      showErrorHTML(out,"不正なアクセスです",JSP_FILE);
      return;
    }
    if (((String)params.get("passwd")).equals(ADMIN_PASSWD)) {
      if (params.get("del")!=null) {
        // データの削除
        msg = registData(mode , params , mailData , COUNT_PATH , LOG_PATH , PAST_COUNT_PATH ,PAST_DIR_PATH);
        if (msg!=null) {
          showErrorHTML(out,msg,JSP_FILE);
          return;
        }
      }
      showadmin = true;
    } else {
      msg = "パスワードが間違っています";
      showlogin = true;
    }
  }

} else if (mode.equals(MODE_REGIST)) { /** 登録 **/
  // HTTPメソッドのチェック
  if (!request.getMethod().equalsIgnoreCase("POST")) {
    showErrorHTML(out,"不正なアクセスです",JSP_FILE);
    if (uploadFile!=null) {
      uploadFile.delete();
    }
    return;
  }
  // パラメーターのチェック
  msg = paramCheck(mode , params , mailData);
  if (msg!=null) {
    showErrorHTML(out,msg,JSP_FILE);
    if (uploadFile!=null) {
      uploadFile.delete();
    }
    return;
  }
  // データの登録
  msg = registData(mode , params , mailData , COUNT_PATH , LOG_PATH , PAST_COUNT_PATH ,PAST_DIR_PATH);
  if (msg!=null) {
    showErrorHTML(out,msg,JSP_FILE);
    if (uploadFile!=null) {
      uploadFile.delete();
    }
    return;
  }
  // クッキーを発行
  if (params.get("cookie")!=null) {
    addCookie(COOKIE_NAMES , params , response , Integer.MAX_VALUE);
  } else {
    addCookie(COOKIE_NAMES , params , response , 0);
  }
  showdata = true;

} else if (mode.equals(MODE_USER_DEL)) { /** 記事削除 **/
  // HTTPメソッドのチェック
  if (!request.getMethod().equalsIgnoreCase("POST")) {
    showErrorHTML(out,"不正なアクセスです",JSP_FILE);
    return;
  }
  // パラメーターのチェック
  msg = paramCheck(mode , params , mailData);
  if (msg!=null) {
    showErrorHTML(out,msg,JSP_FILE);
    return;
  }
  // データの削除
  msg = registData(mode , params , mailData , COUNT_PATH , LOG_PATH , PAST_COUNT_PATH ,PAST_DIR_PATH);
  if (msg!=null) {
    showErrorHTML(out,msg,JSP_FILE);
    return;
  }
  showdata = true;
} else if (mode.equals(MODE_RES_MSG)) { // 返信
  if (params.get("no") == null) {
    showErrorHTML(out,"不正なアクセスです",JSP_FILE);
    return;
  }
  showdata = true;
} else if (mode.equals(MODE_PAST)) { // 過去ログ
  showpast = true;
} else if (mode.equals(MODE_SEARCH)) { // 過去ログ検索
  showsearch = true;
} else if (mode.equals(MODE_LOG)) { // 過去ログ表示
  showlog = true;
}

/** データ表示 **/
if (showdata) {
  // クッキーの取得
  Hashtable cookieTable = getCookie(COOKIE_NAMES , params , request);
  cookieTable.put("subject" , "");
  // カラー選択
  Hashtable colorHTML = new Hashtable();
  colorCheck(colorHTML , (String)cookieTable.get("color"));

  String btncap = "投稿する";
  // データの取得
  Vector dataList;
  if (mode.equals(MODE_RES_MSG)) { // 返信
    dataList = readData(LOG_PATH , (String)params.get("no"));
    if (dataList==null) {
      showErrorHTML(out,"その記事は既に削除されています",JSP_FILE);
      return;
    }
    cookieTable.put("subject" , "Re:" + (String)((Hashtable)dataList.get(0)).get("subject"));
    btncap = "返信する";
  } else { // 通常
    dataList = readData(LOG_PATH , null);
  }
  int size = dataList.size();

  // タイトル
  String title = "<font color=\"" + TITLE_COLOR + "\"><b style=\"font-size:" + TITLE_POINT + "\">" + TITLE + "</b></font>";
  if (USE_IMG) {
    title = "<img src=\"" + IMG_PATH + "\" border=\"0\">";
  }
%>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=Windows-31J">
<title><%=TITLE%></title>
<style type="text/css">
<!--
body,tr,td,th { font-size:<%=TEXT_POINT%>;line-height: <%=TEXT_HIGHT%>; }
small { font-size:9pt; color:<%=TEXT_COLOR%> }
a:link {
  text-decoration: underline; font-weight: normal; color: <%=LINK_COLOR%>
}
a:visited {
  text-decoration: underline; font-weight: normal; color: <%=VLINK_COLOR%>
}
a:hover {
  text-decoration: underline; font-weight: normal; color: <%=HLINK_COLOR%>
}
a:active {
  text-decoration: underline; font-weight: normal; color: <%=ALINK_COLOR%>
}
-->
</style>
</head>
<body bgcolor="<%=BG_COLOR%>" text="<%=TEXT_COLOR%>" link="<%=LINK_COLOR%>" vlink="<%=VLINK_COLOR%>" alink="<%=ALINK_COLOR%>">
<div align="center"><%=title%>
  <hr noshade size="1">
</div>
<div align="center">[ <a href="<%=HOME_URL%>" target="_top">トップに戻る</a> ] [ <a href="<%=JSP_FILE%>?mode=<%=MODE_HOW_TO%>">使い方</a>
  ] <%if (PAST_LOG) {%>[<a href="<%=JSP_FILE%>?mode=<%=MODE_PAST%>" target="_top">過去ログ</a>] <%}%>[ <a href="<%=JSP_FILE%>?mode=<%=MODE_ADMIN%>">管理用</a>
  ] [<a href="<%=JSP_FILE%>">リロード</a>]
  <hr noshade size="1">
</div>
<%
  if (mode.equals(MODE_RES_MSG)) {
%>
  <br>
[<a href="<%=JSP_FILE%>">掲示板に戻る</a> ]
<table width="100%" cellspacing="0" cellpadding="3" border="0" bgcolor="#666666">
    <tr>
      <td align="center"><font color="#FFFFFF"><b>返信</b></font></td>
    </tr>
  </table>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td>
<%
    for (int i=0;i<size;i++) {
      Hashtable data = (Hashtable)dataList.get(i);
      String no = (String)data.get("no");
      String pno = (String)data.get("pno");
      if (!no.equals(pno)) {
        out.println("<blockquote>");
      }
      // 題名の長さ
      String subject = omitString((String)data.get("subject"),SUB_MAX_LEN);
%>
      <hr size="1" noshade>
      [<%=(String)data.get("no")%>] <font color="<%=SUBJECT_COLOR%>"><b><%=subject%></b></font> 投稿者：<%=(String)data.get("name")%>
      <small>投稿日：<%=(String)data.get("date")%></small><br>
      <blockquote>
        <font color="#<%=(String)data.get("color")%>">
        <%=(String)data.get("comment")%>
        </font>
        <% if (data.get("filesystemname")!=null)  { %>
		<br><br><a href="<%=IMG_DIR + "/" + (String)data.get("filesystemname")%>" target='_blank'><img src="<%=IMG_DIR + "/" + (String)data.get("filesystemname")%>" width="<%=(String)data.get("imgwidth")%>" border="0"></a>
        <% } %>
        <%=(String)data.get("url")%>
      </blockquote>
<%
      if (!no.equals(pno)) {
        out.println("</blockquote>");
      }
    } // end for
%>
<hr size="1" noshade>
    </td>
  </tr>
</table>
<%
  } // end if
%>
<form action="<%=JSP_FILE%>" method="POST" enctype="multipart/form-data">
  <blockquote>
    <table cellpadding=2 cellspacing=0 width="401">
      <tr>
        <td nowrap><b>おなまえ</b></td>
        <td nowrap> <input type=text name=name size="25" maxlength="100" tabindex=1 value="<%=cookieTable.get("name")%>">
        </td>
      </tr>
      <tr>
        <td nowrap><b>Ｅメール</b></td>
        <td nowrap> <input type=text name=email size="25" maxlength="100" tabindex=2 style="ime-mode: disabled" value="<%=cookieTable.get("email")%>">
        </td>
      </tr>
      <tr>
        <td nowrap><b>題　　名</b></td>
        <td nowrap> <input type=text name=subject size="30" value="<%=cookieTable.get("subject")%>" maxlength="100" tabindex=3>
          <input type=submit value="<%=btncap%>" tabindex=8 name="submit"> <input type=reset value="リセット" tabindex=9 name="reset">
          <input type=hidden name=mode value="<%=MODE_REGIST%>">
          <%
  if (mode.equals(MODE_RES_MSG)) {
%>
          <input type=hidden name=pno value="<%=(String)params.get("no")%>">
          <%
  }
%>
        </td>
      </tr>
      <tr>
        <td colspan=2 nowrap> <b>コメント</b><br> <textarea name=comment cols="50" rows="8" wrap=soft tabindex=4></textarea>
        </td>
      </tr>
      <tr>
        <td nowrap><b>ＵＲＬ</b></td>
        <td nowrap> <input type=text name=url size="45" value="<%=cookieTable.get("url")%>" maxlength="100" tabindex=5 style="ime-mode: disabled">
        </td>
      </tr>
      <% if (!mode.equals(MODE_RES_MSG)) { %>
      <tr>
        <td nowrap><strong>画像</strong></td>
        <td nowrap><input name="uploadfile" type="file" id="uploadfile" size="25"></td>
      </tr>
      <% } %>
      <tr>
        <td nowrap><b>削除キー</b></td>
        <td nowrap> <input type=password name=passwd size=10 maxlength=8 tabindex=6 value="<%=cookieTable.get("passwd")%>">
          <small>(記事削除時に使用)</small> 　
          <input type="checkbox" name="cookie" value="on" checked> <small>クッキー情報を保存</small></td>
      </tr>
      <tr>
        <td nowrap height="11"><b>文字色</b></td>
        <td nowrap height="11"><%=colorHTML.get("colorcheck")%></td>
      </tr>
    </table>
  </blockquote>
</form>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td>
<%
  int count = 0;
  if (!mode.equals(MODE_RES_MSG)) {
    for (int i=0;i<size;i++) {
      Hashtable data = (Hashtable)dataList.get(i);
      String no = (String)data.get("no");
      String pno = (String)data.get("pno");
      if (no.equals(pno)) {
        count++;
      }
      if (count>=start && count<=end) {
        String res = "[ <a href=\"" + JSP_FILE + "?mode=" + MODE_RES_MSG + "&no=" + no + "\">返信</a> ]";
        if (!NORMAL_RES) {
          res = "";
        }
        if (!no.equals(pno)) {
          res = "";
          out.println("<blockquote>");
        }
        // 題名の長さ
        String subject = omitString((String)data.get("subject"),SUB_MAX_LEN);
%>
      <hr size="1" noshade>
      [<%=(String)data.get("no")%>] <font color="<%=SUBJECT_COLOR%>"><b><%=subject%></b></font> 投稿者：<%=(String)data.get("name")%>
      <small>投稿日：<%=(String)data.get("date")%></small> <%=res%><br>
      <blockquote>
        <font color="#<%=(String)data.get("color")%>">
        <%=(String)data.get("comment")%>
        </font>
        <% if (data.get("filesystemname")!=null)  { %>
		<br><br><a href="<%=IMG_DIR + "/" + (String)data.get("filesystemname")%>" target='_blank'><img src="<%=IMG_DIR + "/" + (String)data.get("filesystemname")%>" width="<%=(String)data.get("imgwidth")%>" border="0"></a>
        <% } %>
        <%=(String)data.get("url")%>
	  </blockquote>
<%
        if (!no.equals(pno)) {
          out.println("</blockquote>");
        }
      } // end if
    } // end for
  } // end if
%>
<hr size="1" noshade>
    </td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="5" cellspacing="0">
  <tr>
    <td>
<%
  if (!mode.equals(MODE_RES_MSG)) {
    int i = PAGE_MAX;
    boolean page_move = false;
    if (start - PAGE_MAX >= 1) {
      out.println("<input type=button value=\"前ページ\" onClick=\"location.href='" + JSP_FILE + "?start=" + (start - PAGE_MAX) + "'\"> ");
    }
    if (start + PAGE_MAX <= count) {
      out.println("<input type=button value=\"次ページ\" onClick=\"location.href='" + JSP_FILE + "?start=" + (start + PAGE_MAX) + "'\"> ");
    }
    if (count > i) {
      out.print("<br><br>[直接移動] ");
      page_move = true;
    }
    while (count > i - PAGE_MAX) {
      if (i - PAGE_MAX + 1==start) {
        if (page_move) {
          out.print("[<b>" + (i/PAGE_MAX) + "</b>] ");
        }
      } else {
        out.print("[<a href=\"" + JSP_FILE + "?start=" + (i - PAGE_MAX +1) +"\">" + (i/PAGE_MAX) + "</a>] ");
      }
      i+=PAGE_MAX;
    }
  }
%>
    </td>
  </tr>
</table>
<table width="304" align=right cellspacing="0" cellpadding="2" border="0">
  <tr>
    <td nowrap align=center>
      <form action="<%=JSP_FILE%>" method="POST"  enctype="multipart/form-data">
        <input type=hidden name=mode value="<%=MODE_USER_DEL%>">
        <small>記事No</small>
        <input type=text name=no size=3 maxlength="6" style="ime-mode: disabled">
        <small>削除キー</small>
        <input type=password name=delpasswd size=5 maxlength=8>
        <input type=submit value="記事削除">
      </form>
    </td>
  </tr>
</table>
<div align="center"><br clear=all><!-- 著作権表示（削除改変不可）-->
  <small><a href="http://www.hellohiro.com/" target="_top">Hello JSP BBS G</a></small></div>
</body>
</html>
<%
} // end if
/** 使い方表示 **/
if (showhowto) {
%>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=Windows-31J">
<style type="text/css">
<!--
body,tr,td,th { font-size:<%=TEXT_POINT%>;line-height: <%=TEXT_HIGHT%>; }
small { font-size:9pt; color:<%=TEXT_COLOR%> }
a:link {
  text-decoration: underline; font-weight: normal; color: <%=LINK_COLOR%>
}
a:visited {
  text-decoration: underline; font-weight: normal; color: <%=VLINK_COLOR%>
}
a:hover {
  text-decoration: underline; font-weight: normal; color: <%=HLINK_COLOR%>
}
a:active {
  text-decoration: underline; font-weight: normal; color: <%=ALINK_COLOR%>
}
-->
</style>
<title><%=TITLE%> 使い方</title>
</head>
<body bgcolor="<%=BG_COLOR%>" text="<%=TEXT_COLOR%>" link="<%=LINK_COLOR%>" vlink="<%=VLINK_COLOR%>" alink="<%=ALINK_COLOR%>">
[<a href="<%=JSP_FILE%>">掲示板に戻る</a>]
<table width="100%" cellspacing="0" cellpadding="3" border="0" bgcolor="#666666">
  <tr>
    <td align="center"><font color="#FFFFFF"><b>掲示板の使い方</b></font></td>
  </tr>
</table>
<ol>
  <p>
  <li>投稿記事に<b>HTMLタグは使用できません。</b>
    <p>
  <li><b>「おなまえ」</b>と<b>「コメント」</b>は必ず入力しなければなりません。「Ｅメール」、「URL」、「題名」、「削除キー」は任意です。
    <p>
  <li>投稿時に<b>「削除キー」</b>を入力しておくと、その「削除キー」を使って後で自分の記事を削除することができます。
    <p>
  <li>投稿時に<b>「削除キー」</b>を入力していない場合はその記事を削除できません。ご注意ください。
    <p>
  <li>アップロードできる画像の拡張子は「gif」、「png」、「jpg」、「jpeg」の４つです。
    <p>
  <li>画像の幅が<%=IMG_WIDTH%>以上の場合は縮小表示されます。
    <p>
  <li><b>「クッキー情報を保存」</b>にチェックを入れておくと、次回から、おなまえ、Ｅメール、ＵＲＬ、文字色、削除キーは自動入力されます。
    <p>
  <li>親記事の最大保持件数は<b>最大 <%=MAX%>件</b>です。それを超えると古い記事から
    <% if (PAST_LOG) { %>
    過去ログに書き込まれます。
    <% } else { %>
    削除されます。
    <% } %>
    <p>
  <li> 自分の記事を削除する場合はページ右下のフォームより行うことができます。
    <p>
  <li>管理者がこの掲示板に相応しくない内容と判断した場合は予告なく削除することがあります。
</ol>
<table width="100%" cellspacing="0" cellpadding="3" border="0" bgcolor="#666666">
  <tr>
    <td align="center">　</td>
  </tr>
</table>
<div align="center"><br><!-- 著作権表示（削除改変不可）-->
  <a href="http://www.hellohiro.com/" target="_top">Hello JSP BBS G</a></div>
</body>
</html>
<%
} // end if
/** 管理者ログインページ表示 **/
if (showlogin) {
%>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=Windows-31J">
<title><%=TITLE%> 認証</title>
<style type="text/css">
<!--
body,tr,td,th { font-size:<%=TEXT_POINT%>;line-height: <%=TEXT_HIGHT%>; }
small { font-size:9pt; color:<%=TEXT_COLOR%> }
a:link {
  text-decoration: underline; font-weight: normal; color: <%=LINK_COLOR%>
}
a:visited {
  text-decoration: underline; font-weight: normal; color: <%=VLINK_COLOR%>
}
a:hover {
  text-decoration: underline; font-weight: normal; color: <%=HLINK_COLOR%>
}
a:active {
  text-decoration: underline; font-weight: normal; color: <%=ALINK_COLOR%>
}
-->
</style>
</head>
<body bgcolor="<%=BG_COLOR%>" text="<%=TEXT_COLOR%>" link="<%=LINK_COLOR%>" vlink="<%=VLINK_COLOR%>" alink="<%=ALINK_COLOR%>">
[<a href="<%=JSP_FILE%>">掲示板に戻る</a>]
<table width="100%" cellspacing="0" cellpadding="3" border="0" bgcolor="#666666" align="center">
  <tr>
    <td align="center"><font color="#FFFFFF"><b>認証</b></font></td>
  </tr>
</table>
<form action="<%=JSP_FILE%>" method="POST">
  <div align="center">
    <input type=hidden name=mode value="<%=MODE_ADMIN%>">
  </div>
  <div align="center"><b>パスワードを入力してください</b><br>
    <p><input type="password" name="passwd" size="10" >
    <br>
<font color="#CC0000">
<%
  if (msg!=null) out.print(msg);
%>
</font>
  </div>
  <div align="center"> </div>
  <P align="center">
    <input type=submit value="ログイン">
</form>
<table width="100%" cellspacing="0" cellpadding="3" border="0" bgcolor="#666666" align="center">
  <tr>
    <td align="center">　</td>
  </tr>
</table>
<div align="center"><br><!-- 著作権表示（削除改変不可）-->
  <a href="http://www.hellohiro.com/" target="_top">Hello JSP BBS G</a> </div>
</body>
</html>
<%
} // end if
/** 管理者画面表示 **/
if (showadmin) {
  // データの取得
  Vector dataList = readData(LOG_PATH , null);
  int size = dataList.size();
%>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=Windows-31J">
<title><%=TITLE%> 管理ページ</title>
<style type="text/css">
<!--
body,tr,td,th { font-size:<%=TEXT_POINT%>;line-height: <%=TEXT_HIGHT%>; }
small { font-size:9pt; color:<%=TEXT_COLOR%> }
a:link {
  text-decoration: underline; font-weight: normal; color: <%=LINK_COLOR%>
}
a:visited {
  text-decoration: underline; font-weight: normal; color: <%=VLINK_COLOR%>
}
a:hover {
  text-decoration: underline; font-weight: normal; color: <%=HLINK_COLOR%>
}
a:active {
  text-decoration: underline; font-weight: normal; color: <%=ALINK_COLOR%>
}
-->
</style>
</head>
<body bgcolor="<%=BG_COLOR%>" text="<%=TEXT_COLOR%>" link="<%=LINK_COLOR%>" vlink="<%=VLINK_COLOR%>" alink="<%=ALINK_COLOR%>">
[<a href="<%=JSP_FILE%>">掲示板に戻る</a>]
<table width="100%" cellspacing="0" cellpadding="3" border="0" bgcolor="#666666" align="center">
  <tr>
    <td align="center"><font color="#FFFFFF"><b>管理ページ</b></font></td>
  </tr>
</table>
<form action="<%=JSP_FILE%>" method="POST">
  <div align="center">
    <input type=hidden name=mode value="<%=MODE_ADMIN%>">
    <input type=hidden name=passwd value="<%=(String)params.get("passwd")%>">
  </div>
  <div align="center">削除したい記事にチェックを入れ削除ボタンを押して下さい。 <br>
    <br>
  </div>
  <div align="center">
    <table border=1 cellspacing=0 cellpadding="2">
      <tr>
        <th nowrap><font color="#666666">削除</font></th>
        <th nowrap><font color="#666666">No</font></th>
        <th nowrap><font color="#666666">ホスト名</font></th>
        <th nowrap><font color="#666666">題名</font></th>
        <th nowrap><font color="#666666">投稿者</font></th>
        <th nowrap><font color="#666666">投稿日</font></th>
        <th nowrap><font color="#666666">コメント</font></th>
        <th nowrap><font color="#666666">画像</font></th>
      </tr>
      <%
  for (int i=0;i<size;i++) {
    Hashtable data = (Hashtable)dataList.get(i);
    String no = (String)data.get("no");
    String subject = (String)data.get("subject");
    subject = omitString(removeTag(subject) , 8);
    String name = (String)data.get("name");
    name = omitString(removeTag(name) , 8);
    String comment = (String)data.get("comment");
    comment = omitString(removeTag(comment), 12);
    String fileName = (String)data.get("filesystemname");
    String fileExist = "[無]";
	if (fileName!=null) {
	  fileExist = "[<a href=\"" + IMG_DIR + "/" + (String)data.get("filesystemname") + "\" target='_blank'>有</a>]";
	}
%>
      <tr valign="middle">
        <td nowrap align="center"> <input type=checkbox name=del value="<%=no%>">
        </td>
        <td nowrap align="center"><b><%=no%></b></td>
        <td nowrap><%=(String)data.get("host")%></td>
        <td><%=subject%></td>
        <td><%=name%></td>
        <td nowrap><small><%=(String)data.get("date")%></small></td>
        <td><small><%=comment%></small></td>
        <td><%=fileExist%></td>
      </tr>
      <%
  } // end for
%>
    </table>
  </div>
  <P align="center">
    <input type=submit value="削除する">
    　
<input type=reset value="リセット">
</form>
<table width="100%" cellspacing="0" cellpadding="3" border="0" bgcolor="#666666" align="center">
  <tr>
    <td align="center">　</td>
  </tr>
</table>
<div align="center"><br><!-- 著作権表示（削除改変不可）-->
  <a href="http://www.hellohiro.com/" target="_top">Hello JSP BBS G</a></div>
</body>
</html>
<%
} // end if
/** 過去ログ画面表示 **/
if (showpast) {
%>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=Windows-31J">
<title><%=TITLE%> 過去ログ</title>
<style type="text/css">
<!--
body,tr,td,th { font-size:<%=TEXT_POINT%>;line-height: <%=TEXT_HIGHT%>; }
small { font-size:9pt; color:<%=TEXT_COLOR%> }
a:link {
  text-decoration: underline; font-weight: normal; color: <%=LINK_COLOR%>
}
a:visited {
  text-decoration: underline; font-weight: normal; color: <%=VLINK_COLOR%>
}
a:hover {
  text-decoration: underline; font-weight: normal; color: <%=HLINK_COLOR%>
}
a:active {
  text-decoration: underline; font-weight: normal; color: <%=ALINK_COLOR%>
}
-->
</style>
<script language="JavaScript" type="text/JavaScript">
<!--
function goToPastLog(form) {
  if (form.select_file.selectedIndex >= 0) {
    form.mode.value = "<%=MODE_LOG%>";
    form.submit();
  }
}
function goToResult(form) {
  form.mode.value = "<%=MODE_SEARCH%>";
  form.submit();
}
//-->
</script>
</head>
<%
File dir = new File(PAST_DIR_PATH);
String[] fileList = dir.list();
String title = "過去ログ";
if (fileList == null || fileList.length == 0) {
  title = "過去ログはありません";
}
%>
<body bgcolor="<%=BG_COLOR%>" text="<%=TEXT_COLOR%>" link="<%=LINK_COLOR%>" vlink="<%=VLINK_COLOR%>" alink="<%=ALINK_COLOR%>">
[<a href="<%=JSP_FILE%>">掲示板に戻る</a>]
<table width="100%" cellspacing="0" cellpadding="3" border="0" bgcolor="#666666" align="center">
  <tr>
    <td align="center"><font color="#FFFFFF"><b><%=title%></b></font></td>
  </tr>
</table>
<form action="<%=JSP_FILE%>" method="POST">
  <div align="center">過去ログ：
    <select name="select_file" id="select_file">
<%
if (fileList!=null) {
  for (int i=fileList.length-1;i>=0;i--) {
%>
      <option value="<%=fileList[i]%>"><%=fileList[i]%></option>
<%
  }
}
%>
    </select>
    　
    <input type=button onClick="goToPastLog(this.form)" value="移動">
    　過去ログ検索：
    <input name="keyword" type="text" id="keyword" size="15" maxlength="20">
    　
    <input type=button onClick="goToResult(this.form)" value="検索">
    <input name="mode" type="hidden" id="mode" value="">
  </div>
</form>
<table width="100%" cellspacing="0" cellpadding="3" border="0" bgcolor="#666666" align="center">
  <tr>
    <td align="center">　</td>
  </tr>
</table>
<div align="center"><br>
  <!-- 著作権表示（削除改変不可）-->
  <a href="http://www.hellohiro.com/" target="_top">Hello JSP BBS G</a> </div>
</body>
</html>
<%
} // end if
/** 過去ログ検索結果表示 **/
if (showsearch) {
%>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=Windows-31J">
<title><%=TITLE%> 過去ログ検索結果</title>
<style type="text/css">
<!--
body,tr,td,th { font-size:<%=TEXT_POINT%>;line-height: <%=TEXT_HIGHT%>; }
small { font-size:9pt; color:<%=TEXT_COLOR%> }
a:link {
  text-decoration: underline; font-weight: normal; color: <%=LINK_COLOR%>
}
a:visited {
  text-decoration: underline; font-weight: normal; color: <%=VLINK_COLOR%>
}
a:hover {
  text-decoration: underline; font-weight: normal; color: <%=HLINK_COLOR%>
}
a:active {
  text-decoration: underline; font-weight: normal; color: <%=ALINK_COLOR%>
}
-->
</style>
</head>
<body bgcolor="<%=BG_COLOR%>" text="<%=TEXT_COLOR%>" link="<%=LINK_COLOR%>" vlink="<%=VLINK_COLOR%>" alink="<%=ALINK_COLOR%>">
[<a href="<%=JSP_FILE%>">掲示板に戻る</a>]
<table width="100%" cellspacing="0" cellpadding="3" border="0" bgcolor="#666666" align="center">
  <tr>
    <td align="center"><font color="#FFFFFF"><b>過去ログ検索結果</b></font></td>
  </tr>
</table><br>
<div align="center">[<a href="javascript:history.back()">検索画面に戻る</a>] </div>
<div align="center"><br>
<%
String keyword = (String)params.get("keyword");
if (keyword!=null && !keyword.trim().equals("")) {
  keyword = keyword.trim();
  Vector lines = new Vector();
  File dir = new File(PAST_DIR_PATH);
  String[] fileList = dir.list();
  if (fileList!=null) {
    for (int i=fileList.length-1;i>=0;i--) {
      String filePath = PAST_DIR_PATH + File.separator + fileList[i];
      BufferedReader br = new BufferedReader(
                          new InputStreamReader(
                          new FileInputStream(filePath),"Windows-31J"));
      String line = null;
      while ((line=br.readLine())!=null) {
        if (line.indexOf(keyword)>=0) {
          lines.add(line);
        }
      }
      br.close();
    }
  }
  if (lines.size()==0) {
    if (fileList==null || fileList.length==0) {
      out.println("過去ログが存在しません");
    } else {
      out.println("見つかりませんでした");
    }
  } else {
    out.println(lines.size() + "件ヒットしました");
    out.println("<div align=\"left\">");
    for (int i=0;i<lines.size();i++) {
      out.println(lines.get(i));
    }
    out.println("</div>");
  }
} else {
  out.println("キーワードを指定してください");
}
%>
  <br>
  <br>
  [<a href="javascript:history.back()">検索画面に戻る</a>]</div><br>
<table width="100%" cellspacing="0" cellpadding="3" border="0" bgcolor="#666666" align="center">
  <tr>
    <td align="center">
　</td>
  </tr>
</table>
<div align="center"><br>
  <!-- 著作権表示（削除改変不可）-->
  <a href="http://www.hellohiro.com/" target="_top">Hello JSP BBS G</a> </div>
</body>
</html>
<%
} // end if
/** 過去ログ表示 **/
if (showlog) {
%>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=Windows-31J">
<title><%=TITLE%> 過去ログ表示 [ <%=(String)params.get("select_file")%> ]</title>
<style type="text/css">
<!--
body,tr,td,th { font-size:<%=TEXT_POINT%>;line-height: <%=TEXT_HIGHT%>; }
small { font-size:9pt; color:<%=TEXT_COLOR%> }
a:link {
  text-decoration: underline; font-weight: normal; color: <%=LINK_COLOR%>
}
a:visited {
  text-decoration: underline; font-weight: normal; color: <%=VLINK_COLOR%>
}
a:hover {
  text-decoration: underline; font-weight: normal; color: <%=HLINK_COLOR%>
}
a:active {
  text-decoration: underline; font-weight: normal; color: <%=ALINK_COLOR%>
}
-->
</style>
</head>
<body bgcolor="<%=BG_COLOR%>" text="<%=TEXT_COLOR%>" link="<%=LINK_COLOR%>" vlink="<%=VLINK_COLOR%>" alink="<%=ALINK_COLOR%>">
[<a href="<%=JSP_FILE%>">掲示板に戻る</a>]
<table width="100%" cellspacing="0" cellpadding="3" border="0" bgcolor="#666666" align="center">
  <tr>
    <td align="center"><font color="#FFFFFF"><b>過去ログ表示 [ <%=(String)params.get("select_file")%> ]</b></font></td>
  </tr>
</table><br><div align="center">[<a href="javascript:history.back()">検索画面に戻る</a>] </div>
<div align="left"><br>
<%
String fileName = (String)params.get("select_file");
if (fileName==null || fileName.equals("") || fileName.indexOf(File.separator) > 0) {
  out.println("ログファイルが存在しません");
} else {
  String filePath = PAST_DIR_PATH + File.separator + fileName;
  File logFile = new File(filePath);
  if (logFile.exists()) {
      BufferedReader br = new BufferedReader(
                          new InputStreamReader(
                          new FileInputStream(filePath),"Windows-31J"));
      String line;
      while ((line=br.readLine())!=null) {
        out.println(line);
      }
      br.close();
  } else {
      out.println("ログファイルが存在しません");
  }
}
%>
  <br>
  <br>
  </div>
<div align="center">[<a href="javascript:history.back()">検索画面に戻る</a>] </div><br><br>
<table width="100%" cellspacing="0" cellpadding="3" border="0" bgcolor="#666666" align="center">

  <tr>
    <td align="center">　</td>
  </tr>
</table>
<div align="center"><br>
  <!-- 著作権表示（削除改変不可）-->
  <a href="http://www.hellohiro.com/" target="_top">Hello JSP BBS G</a> </div>
</body>
</html>
<%
} // end if
%>
<%!
/**
 * 画像の拡張子のチェック
 */
private boolean fileExtensionCheck(String extension) {
  for (int i=0;i<EXTENSION_LIST.length;i++) {
    if (EXTENSION_LIST[i].equals(extension)) {
      return true;
    }
  }
  return false;
}
/**
 * クッキーを発行
 */
private void addCookie(String[] cookieNames,
                       Hashtable params,
                       HttpServletResponse response,
                       int expiry) {
  String name = removeTag((String)params.get("name"));
  name = replace(name , "&nbsp;" , " ");
  params.put("name" , name);
  String url = (String)params.get("url");
  if (url!=null) {
    url = removeTag(url);
    if (url.equals("")) {
      url = "http://";
    }
    params.put("url" , url);
  }
  try {
    for (int i=0;i<cookieNames.length;i++) {
      String val = (String)params.get(cookieNames[i]);
      if (val!=null) {
        Cookie cookie = new Cookie(cookieNames[i] , URLEncoder.encode(val,"Windows-31J"));
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
      }
    }
  } catch (Exception e) {
    e.printStackTrace();
  }
}
/**
 * クッキーを取得
 */
private Hashtable getCookie(String[] cookieNames, Hashtable params , HttpServletRequest request) {
  Hashtable cookieTable = new Hashtable();
  boolean needcookie = true;
  for (int i=0;i<cookieNames.length;i++) {
    String param = (String)params.get(cookieNames[i]);
    if (param==null) {
      param = "";
    } else {
      if (cookieNames[i].equals("name")) {
        needcookie = false;
      }
    }
    cookieTable.put(cookieNames[i] , param);
  }
  String url = (String)cookieTable.get("url");
  if (url==null || url.equals("")) {
    url = "http://";
  }
  cookieTable.put("url" , url);
  if (needcookie) {
    try {
      Cookie[] cookies = request.getCookies();
      if (cookies!=null) {
        for (int i=0;i<cookies.length;i++) {
          cookieTable.put(cookies[i].getName() , URLDecoder.decode(cookies[i].getValue(),"Windows-31J"));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  return cookieTable;
}
/**
 * パラメーターチェック
 */
private String paramCheck(String mode , Hashtable params , Hashtable mailData) {
  if (mode.equals(MODE_REGIST)) { // 登録
    String name = (String)params.get("name");
    String email = (String)params.get("email");
    String subject = (String)params.get("subject");
    String comment = (String)params.get("comment");
    String url = (String)params.get("url");

    if (name==null || name.equals("")) {
      return "名前が入力されていません";
    }
    if (comment== null || comment.equals("")) {
      return "コメントが入力されていません";
    }
    if (email!=null && !email.equals("")) {
      if (!emailCheck(email)) {
        return "Ｅメールが正しくありません";
      }
    }
    if (subject== null || subject.equals("")) {
      subject = "無題";
    }
    if (SEND_MAIL) {
      mailData.put("o_name" , name);
      mailData.put("o_email" , email);
      mailData.put("o_subject" , subject);
      mailData.put("o_comment" , comment);
      mailData.put("o_url" , url);
    }

    name = htmlEncode(name);
    email = htmlEncode(email);
    subject = htmlEncode(subject);
    comment = htmlEncode(comment);
    comment = crlf2br(comment);
    comment = replace(comment,"\t","&nbsp;&nbsp;&nbsp;&nbsp;");
    if (URL_AUTO_LINK) {
      comment = urlAutoLink(comment);
    }
    
    url = htmlEncode(url);
    if (url!=null && !url.equals("http://") && url.startsWith("http://")) {
      url = "<p><a href=\"" + url + "\" target='_blank'>" + url + "</a>";
    } else {
      url = "";
      if (SEND_MAIL) {
        mailData.put("o_url" , url);
      }
    }
    name = "<b>" + name + "</b>";
    if (email!=null && !email.equals("")) {
      name = "<a href=\"mailto:" + email + "\">" + name + "</a>";
    }

    params.put("name" , name);
    params.put("email" , email);
    params.put("subject" , subject);
    params.put("comment" , comment);
    params.put("url" , url);
  } else if (mode.equals(MODE_USER_DEL)) { // 記事削除
    String no = (String)params.get("no");
    String delpasswd = (String)params.get("delpasswd");
    if ((no==null || no.equals("")) ||
        (delpasswd==null || delpasswd.equals(""))) {
      return "記事Noまたは削除キーが入力されていません";
    }
  }
  return null;
}
/**
 * メールアドレスをチェック
 */
private boolean emailCheck(String email) {
  if(email.indexOf('@')==-1 || email.indexOf('.')==-1) {
    return false;
  }
  return true;
}
/**
 * パラメーターの値を適切に変換
 */
private String toSJIS(String input) {
  try {
    String charsetName = "iso-8859-1";
    if (TOMCAT3) {
      charsetName = "";
    }
    input = (new String(input.getBytes(charsetName),"JISAutoDetect")).trim();
  } catch (UnsupportedEncodingException e) {
  }
  return input;
}
/**
 * HTMLエンコード
 */
private String htmlEncode(String input) {
  if (input==null) {
    return input;
  }
  StringBuffer buf = new StringBuffer();
  char[] c = input.toCharArray();
  for (int i=0; i < c.length; i++) {
    switch (c[i]) {
      case '<':
        buf.append("&lt;");
        break;
      case '>':
        buf.append("&gt;");
        break;
      case '"':
        buf.append("&quot;");
        break;
      case ' ':
        buf.append("&nbsp;");
        break;
      default:
        buf.append(c[i]);
    }
  }
  return buf.toString();
}
/**
 * 文字列を置換
 */
private String replace(String input, String pattern, String replace) {
  int index = input.indexOf(pattern);
  if (index==-1) {
    return input;
  }
  StringBuffer sb = new StringBuffer();
  sb.append(input.substring(0,index) + replace);
  int plen = pattern.length();
  if (index + plen < input.length()) {
    String s = input.substring(index + plen);
    sb.append(replace(s , pattern , replace));
  }
  return sb.toString();
}
/**
 * 文字列が長い場合省略します
 */
private String omitString(String input, int len) {
  if (input.length()<=len) {
    return input;
  }
  return input.substring(0,len) + "．．";
}
/**
 * カラー選択処理
 */
private void colorCheck(Hashtable colorHTML , String color) {
  if (color==null || color.equals("")) {
    color = COLOR_LIST[0];
  }
  StringBuffer sbChecked = new StringBuffer();
  StringBuffer sbSelected = new StringBuffer();
  for (int i=0;i<COLOR_LIST.length;i++) {
    if (COLOR_LIST[i].equals(color)) {
      sbChecked.append("<input type=radio value=" + COLOR_LIST[i] + " name=color checked><font color=\"#" + COLOR_LIST[i] + "\">■</font>" + "\n");
      sbSelected.append("<option value=\"" + COLOR_LIST[i] + "\" selected>" + COLOR_NAME_LIST[i] + "\n");
    } else {
      sbChecked.append("<input type=radio value=" + COLOR_LIST[i] + " name=color><font color=\"#" + COLOR_LIST[i] + "\">■</font>" + "\n");
      sbSelected.append("<option value=\"" + COLOR_LIST[i] + "\">" + COLOR_NAME_LIST[i] + "\n");
    }
  }
  colorHTML.put("colorcheck", sbChecked.toString());
  colorHTML.put("coloroptions" , sbSelected.toString());
}
/**
 * URL自動リンク処理
 */
private String urlAutoLink(String input) {
  String urlString = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_%/.?+&=";
  int start = input.indexOf("http://");
  int len = "http://".length();
  if (start==-1) {
    return input;
  }
  int index;
  for (index=start + len;index<input.length();index++) {
   if (urlString.indexOf(input.charAt(index))==-1) {
    break;
   }
  }
  String url = input.substring(start , index);
  String link = "<a href=\"" + url + "\" target=\"_blank\">" + url + "</a>";
  StringBuffer sb = new StringBuffer();
  sb.append(input.substring(0, start)).append(link);
  if (index < input.length()) {
    sb.append(urlAutoLink(input.substring(index)));
  }
  return sb.toString();
}
// 配列の中を検索します
private boolean searchArray(String[] array, String search) {
  for (int i=0;i<array.length;i++) {
    if (array[i].equals(search)) {
      return true;
    }
  }
  return false;
}
// 改行コード(CrLf,Cr,Lf)→"<br>"に変換
private String crlf2br(String value) {
  if (value.indexOf("\r")==-1 && value.indexOf("\n")==-1) {
    return value;
  }
  final String BR = "<br>";
  StringBuffer sb = new StringBuffer();
  char c;
  int flagCr=-2;
  int len = value.length();
  for (int i=0;i<len;i++) {
    c = value.charAt(i);
    if (c=='\r') {
      flagCr=i;
      sb.append(BR);
    } else if (c=='\n') {
      if (i-flagCr==1) {
        sb.delete(sb.length()-BR.length(), sb.length());
      }
      sb.append(BR);
    } else {
      sb.append(c);
    }
  }
  return sb.toString();
}
/**
 * タグを取り除きます
 */
private String removeTag(String input) {
  if (input==null) {
    return "";
  }
  int index = input.indexOf("<");
  if (index==-1) {
    return input;
  }
  while (index!=-1) {
    int endTagIndex = input.indexOf(">" , index);
    if (endTagIndex==-1) {
      break;
    }
    input = input.substring(0,index) + input.substring(endTagIndex + 1);
    index = input.indexOf("<");
  }
  return input;
}

/**
 * メール送信
 */
private boolean sendMail(Hashtable params , Hashtable mailData) {
  String cmd = SENDMAIL_PATH + " -t";
  String fileName = (String)params.get("originalfilename");
  String fileExist = "有";
  if (fileName==null) {
    fileExist = "無";
  }
  String param = "From: " + ADMIN_MAIL + "\n" +
                 "To: " + ADMIN_MAIL + "\n" +
                 "Subject: [" + TITLE + " : " + (String)params.get("no") + "] " +
                 (String)params.get("subject") + "\n" +
                 "Content-type: text/plain; charset=ISO-2022-JP\n" +
                 "X-Mailer: Hello JSP BBS G\n\n" +
                 "--------------------------------------------------------\n" +
                 "投稿日時：" + (String)params.get("date") + "\n" +
                 "ホスト名：" + (String)params.get("host") + "\n" +
                 "ブラウザ：" + (String)params.get("user-agent") + "\n" +
                 "\n" +
                 "投稿者名：" + (String)mailData.get("o_name") + "\n" +
                 "Ｅメール：" + (String)mailData.get("o_email") + "\n" +
                 "ＵＲＬ：" + (String)mailData.get("o_url") + "\n" +
                 "画像：" + fileExist + "\n" +
                 "タイトル：" + (String)mailData.get("o_subject") + "\n" +
                 "投稿記事：\n\n" +
                 (String)mailData.get("o_comment") + "\n" +
                 "--------------------------------------------------------\n";

  try {
    Process process = Runtime.getRuntime().exec(cmd);
    OutputStream out = process.getOutputStream();
    out.write(param.getBytes("ISO-2022-JP"));
    out.flush();
    out.close();
    return true;
  } catch (Exception e) {
    e.printStackTrace();
    return false;
  }
}

/**
 * 過去ログの書き込み
 */
private void writePastLog(Hashtable params ,
                          String countPath ,
                          String pastDirPath,
                          int delCount) {
  synchronized (plock) {
    // 過去ログのカウントを取得
    int pastCount = 0;
    try {
      BufferedReader cin = new BufferedReader(new FileReader(countPath));
      String strCnt = cin.readLine();
      cin.close();
      pastCount = Integer.parseInt(strCnt);
    } catch (Exception e) {
    }

    // 過去ログファイルのパスを取得
    String pastFilePath = pastDirPath + File.separator + pastCount + ".html";
    // 過去ログ読み込み
    Vector logs = new Vector();
    try {
      File f = new File(pastDirPath);
      if (!f.isDirectory()) {
        f.mkdir();
      }
      BufferedReader pin = new BufferedReader(
                           new InputStreamReader(
                           new FileInputStream(pastFilePath),"Windows-31J"));
      String line;
      while ((line=pin.readLine())!=null) {
        if (!line.equals("")) {
          logs.add(line);
        }
      }
      if (logs.size()>2) {
        logs.remove(0);
        logs.remove(logs.size()-1);
      }
      pin.close();
    } catch (Exception e) {
    }

    // 行数取得
    int lineCount = logs.size();
    // 最大行数のチェック
    if (lineCount >= MAX_PAST_LINE && delCount==0) {
      pastCount++;
      pastFilePath = pastDirPath + File.separator + pastCount + ".html";
      logs.clear();
    }

    // 過去ログの書き込み
    try {
      String no = (String)params.get("no");
      String pno = (String)params.get("pno");

      String line = "["+ no + "] <font color=" + SUBJECT_COLOR + "><b>" + (String)params.get("subject") +
                    "</b></font> 投稿者：" + (String)params.get("name") +
                    " <small>投稿日:" + (String)params.get("date") +
                    "</small><p><blockquote>" + (String)params.get("comment");
      if (params.get("filesystemname")!=null)  {
        line += "<br><br><a href=\"" +IMG_DIR + "/" + (String)params.get("filesystemname") + "\" target='_blank'><img src=\"" + IMG_DIR + "/" + (String)params.get("filesystemname") + "\" width=\"" + (String)params.get("imgwidth") + "\" border=\"0\"></a>";
      }
      line += "<p>" + (String)params.get("url") + "</blockquote><hr>";
      if (!no.equals(pno)) {
        line = "<blockquote>" + line + "</blockquote>";
      }
      BufferedWriter pout = new BufferedWriter(
                          new OutputStreamWriter(
                          new FileOutputStream(pastFilePath),"Windows-31J"));
      pout.write("<html><body>\r\n");
      pout.write(line + "\r\n");
      lineCount = logs.size();
      for (int i=0;i<lineCount;i++) {
        pout.write((String)logs.get(i) + "\r\n");
      }
      pout.write("</body></html>");
      pout.close();

      BufferedWriter cout = new BufferedWriter(new FileWriter(countPath));
      cout.write(String.valueOf(pastCount));
      cout.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
/**
 * データの取得
 */
private Vector readData(String logPath , String no) {
  Vector dataList = new Vector();
  try {
    ObjectInputStream in = new ObjectInputStream(new FileInputStream(logPath));
    dataList = (Vector)in.readObject();
    in.close();
  } catch (Exception e) {
  }
  if (no!=null) { // 返信
    int size = dataList.size();
    Vector resList = new Vector();
    for (int i=0;i<size;i++) {
      Hashtable data = (Hashtable)dataList.get(i);
      if (((String)data.get("pno")).equals(no)) {
        resList.add(data);
      }
    }
    if (resList.size()==0) {
      return null;
    }
    return resList;
  }
  return dataList;
}
/**
 * データの登録・削除
 */
private String registData(String
                          mode,
                          Hashtable params,
                          Hashtable mailData,
                          String countPath ,
                          String logPath,
                          String logCountPath,
                          String logDirPath) {
  synchronized (lock) {
    Vector dataList = new Vector();
    int newNo = 1;
    String name = "";
    String comment = "";
    // データ読み込み
    try {
      // 記事取得
      ObjectInputStream din = new ObjectInputStream(new FileInputStream(logPath));
      dataList = (Vector)din.readObject();
      din.close();
      // 記事番号取得
      BufferedReader cin = new BufferedReader(new FileReader(countPath));
      String strCnt = cin.readLine();
      cin.close();
      newNo = Integer.parseInt(strCnt) + 1;
    } catch (Exception e) {
    }

    if (mode.equals(MODE_REGIST)) { /** 登録 **/
      int i;
      // 二重投稿かどうかのチェック
      for (i=0;i<dataList.size();i++) {
        // 最新の記事の名前とコメントを取得
        name = (String)((Hashtable)dataList.get(i)).get("name");
        comment = (String)((Hashtable)dataList.get(i)).get("comment");
        String thisFileName = (String)((Hashtable)dataList.get(i)).get("originalfilename");
        if (thisFileName == null) {
          thisFileName = "";
        }
        String fileName = (String)params.get("originalfilename");
        if (fileName == null) {
          fileName = "";
        }

        if (((String)params.get("name")).equals(name) &&
            ((String)params.get("comment")).equals(comment) &&
            fileName.equals(thisFileName)) {
            return "二重投稿は禁止です";
        }
      }

      // 記事番号を設定
      String no = String.valueOf(newNo);
      params.put("no", no);
      int size = dataList.size();
      if (params.get("pno")==null) { // 通常登録の場合
        if (size >= MAX) {
          // 親記事数を取得
          Hashtable data = (Hashtable)dataList.get(0);
          String pno = (String)data.get("pno");
          int parentCount = 1;
          for (i=0;i<size;i++) {
            data = (Hashtable)dataList.get(i);
            String tmpPno = (String)data.get("pno");
            if (!tmpPno.equals(pno)) {
              parentCount++;
            }
            pno = tmpPno;
          }
          // 最大保持親記事数のチェック
          if (parentCount >= MAX) {
            int delParentNum = parentCount - MAX + 1;
            data = (Hashtable)dataList.get(size-1);
            pno = (String)data.get("pno");
            int delCount=0;
            for (i=size-1;i>0;i--) {
              data = (Hashtable)dataList.get(i);
              String tmpPno = (String)data.get("pno");
              if (tmpPno.equals(pno)) {
                if (PAST_LOG) {
                  writePastLog((Hashtable)dataList.get(i),logCountPath,logDirPath,delCount++);
                }
                dataList.remove(i);
              } else {
                if (--delParentNum<=0) {
                  break;
                } else {
                  if (PAST_LOG) {
                    writePastLog((Hashtable)dataList.get(i),logCountPath,logDirPath,delCount++);
                  }
                  dataList.remove(i);
                  pno = tmpPno;
                }
              }
            }
          }
        }

        params.put("pno",no);
        // リストに追加
        dataList.add(0,params);
      } else { // 返信からの場合
        String pno = (String)params.get("pno");
        boolean exist = false;
        Vector sortData = new Vector();
        for (i=0;i<size;i++) {
          Hashtable data = (Hashtable)dataList.get(i);
          String tmpPno = (String)data.get("pno");
          // 親記事が存在する場合
          if (tmpPno.equals(pno)) {
            exist = true;
            // トップソートあり
            if (TOP_SORT) {
              dataList.remove(i--);
              size--;
              sortData.add(data);
            }
          } else {
            if (exist) {
              break;
            }
          }
        }
        if (!exist) {
          return "記事が既に削除されています";
        }
        if (TOP_SORT) { // トップソートあり
          sortData.add(params);
          dataList.addAll(0, sortData);
        } else { // トップソートなし
          dataList.add(i,params);
        }
      }
    } else if (mode.equals(MODE_USER_DEL)) { /** ユーザー記事削除 **/
      String delNo = (String)params.get("no");
      String delPasswd = (String)params.get("delpasswd");
      int size = dataList.size();
      boolean delete = false;
      for (int i=size-1;i>=0;i--) {
        Hashtable data = (Hashtable)dataList.get(i);
        String tmpNo = (String)data.get("no");
        String tmpPasswd = (String)data.get("passwd");
        if (tmpNo.equals(delNo)) {
          if (tmpPasswd!=null && tmpPasswd.equals(delPasswd) && !tmpPasswd.equals("")) {
            delete = true;
            dataList.remove(i);
            // 画像を削除します
            String filePath = (String)data.get("filesystempath");
            if (filePath!=null) {
              File file = new File(filePath);
              if (file.exists()) {
                file.delete();
              }
            }
          } else {
            return "削除キーがちがいます";
          }
          break;
        }
      }
      // 返信を削除します
      if (delete) {
        size = dataList.size();
        for (int i=size-1;i>=0;i--) {
          Hashtable data = (Hashtable)dataList.get(i);
          String tmpPno = (String)data.get("pno");
          if (tmpPno.equals(delNo)) {
            dataList.remove(i);
            // 画像を削除します
            String filePath = (String)data.get("filesystempath");
            if (filePath!=null) {
              File file = new File(filePath);
              if (file.exists()) {
                file.delete();
              }
            }
          }
        }
      }
    } else if (mode.equals(MODE_ADMIN)) { /** 管理者記事削除 **/
      String passwd = (String)params.get("passwd");
      if (passwd!=null && passwd.equals(ADMIN_PASSWD)) {
        String[] del = (String[])params.get("del");
        int size = dataList.size();
        Vector plist = new Vector();
        for (int i=size-1;i>=0;i--) {
          Hashtable data = (Hashtable)dataList.get(i);
          String tmpNo = (String)(data.get("no"));
          if (searchArray(del, tmpNo)) {
            String tmpPno = (String)(data.get("pno"));
            if (tmpNo.equals(tmpPno)) {
              plist.add(tmpPno);
            }
            dataList.remove(i);
            // 画像を削除します
            String filePath = (String)(data.get("filesystempath"));
            if (filePath!=null) {
              File file = new File(filePath);
              if (file.exists()) {
                file.delete();
              }
            }
          }
        }
        // 返信記事を削除
        if (plist.size()>0) {
          del = new String[plist.size()];
          plist.toArray(del);
          size = dataList.size();
          for (int i=size-1;i>=0;i--) {
            Hashtable data = (Hashtable)dataList.get(i);
            String tmpPno = (String)(data.get("pno"));
            if (searchArray(del, tmpPno)) {
              dataList.remove(i);
              // 画像を削除します
              String filePath = (String)(data.get("filesystempath"));
              if (filePath!=null) {
                File file = new File(filePath);
                if (file.exists()) {
                  file.delete();
                }
              }
            }
          }
        }
      }
    }

    // データ書き込み
    try {
      File logFile = new File(logPath);
      if (logFile.exists() && !logFile.canWrite()) {
        return "ログファイルに書き込み権限がありません";
      }
      File countFile = new File(countPath);
      if (countFile.exists() && !countFile.canWrite()) {
        return "カウントファイルに書き込み権限がありません";
      }
      // 記事書き込み
      ObjectOutputStream dout = new ObjectOutputStream(new FileOutputStream(logFile));
      dout.writeObject(dataList);
      dout.close();
      // 記事番号を書き込み
      if (mode.equals(MODE_REGIST)) {
        BufferedWriter cout = new BufferedWriter(new FileWriter(countFile));
        cout.write(String.valueOf(newNo));
        cout.close();
        // メール送信
        if (SEND_MAIL) {
          if (!sendMail(params , mailData)) {
            return "メール送信時にエラーが発生しました";
          }
        }
      }
    } catch (Exception e) {
      return "書き込み時にエラーが発生しました";
    }
    return null;
  }
}

/**
 * エラー出力
 */
private void showErrorHTML(JspWriter out, String msg , String back) throws IOException {
  out.println("<html>");
  out.println("<head>");
  out.println("<meta http-equiv=\"Content-type\" CONTENT=\"text/html; charset=Windows-31J\">");
  out.println("<title>エラー</title>");
  out.println("<style type=\"text/css\">");
  out.println("<!--");
  out.println("body { font-size:10pt }");
  out.println("-->");
  out.println("</style>");
  out.println("</head>");
  out.println("<body bgcolor=\"#FFFFFF\">");
  out.println("<hr width=\"350\" noshade size=\"1\">");
  out.println("<center>");
  out.println("<h4>エラー</h4>");
  out.println("<p><font color='#CC0000'>" + msg + "</font>");
  out.println("<p>[<a href=\"" + back + "\">戻る</a>]");
  out.println("</center>");
  out.println("<hr width=\"350\" noshade size=\"1\">");
  out.println("</body>");
  out.println("</html>");
}
%>