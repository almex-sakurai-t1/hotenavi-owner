<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.awt.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*" %>
<%@ page import="org.apache.commons.net.ftp.FTPClient" %>
<%@ page import="org.apache.commons.net.ftp.FTPReply" %>
<%@ page import="com.hellohiro.servlet.upload.*"%>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%!
    private static final int PAGE_MAX          = 10; // １ページ当りの最大表示親記事数
    private static final int DATA_TYPE         = 1;  //  upload_files.data_type: 0･･･画像,1･･･その他ファイル
    private static final boolean TOMCAT3       = false; // Tomcat 3　で文字化けする場合trueに設定

// 送信先メールアドレス（送信元メールアドレスがない場合にも利用）
    private static final String ADMIN_MAIL     = "g-happy@happyhotel.jp";
    private static final int SUB_MAX_LEN       = 15; // 題名の長さ(これ以上だと省略されます)

// ファイルアップロード関連の設定
    private static final String IMG_DIR        = "upload_files/others"; // アップロードしたファイルを保存するフォルダ
    private static final String IMG_DIR_DISP   = "upload_files/others"; // アップロードしたファイルを保存するフォルダ
    private static final int MAX_POST_SIZE     = 1024 * 100000; // 送信するデータの最大サイズ(バイト)

// モード定数
    private static final String MODE_NORMAL   = "normal"; // 通常
    private static final String MODE_REGIST   = "regist"; // 登録
    private static final String MODE_DEL      = "del"; // 削除

// アップロードを許可するファイルの拡張子
    private static final String[] EXTENSION_LIST = { "gif",
                                                     "jpg",
                                                     "jpeg",
                                                     "png",
                                                     "bmp",
                                                     "pdf",
                                                     "ai",
                                                     "psd",
                                                     "zip",
                                                     "lzh",
                                                     "doc",
                                                     "docx",
                                                     "xls",
                                                     "xlsx"
                                                     };
%>
<%
//MySql用
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    Connection        connection_user  = null;
    PreparedStatement prestate_user    = null;
    ResultSet         result_user      = null;
    connection  = DBConnection.getConnection();
    String query = "";

//ホテルID
    String loginHotelId  = (String)session.getAttribute("LoginHotelId");
    if( loginHotelId == null )
    {
        loginHotelId = "demo";
    }
    String hotelid       = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
        hotelid = "demo";
    }

// imedia_user のチェック

    int imedia_user = 0;
    query = "SELECT * FROM owner_user WHERE hotelid=?";
    query = query + " AND userid=?";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1, loginHotelId);
    prestate.setInt(2, ownerinfo.DbUserId);
    result      = prestate.executeQuery();
    if( result.next() != false )
    {
        imedia_user = result.getInt("imedia_user");
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

//書き込み時番号取得
    int newNo = 0;
    query = "SELECT * FROM upload_files WHERE hotel_id=?";
    query = query + " AND data_type=?";
    query = query + " ORDER BY id DESC";
    prestate = connection.prepareStatement(query);
    prestate.setString(1, loginHotelId);
    prestate.setInt(2, DATA_TYPE);
    result   = prestate.executeQuery();
    if( result.next() != false )
    {
        newNo = result.getInt("id");
    }
    DBConnection.releaseResources(result,prestate,connection);
    newNo = newNo + 1;

//現在日時
    DateEdit  de = new DateEdit();
    NumberFormat     nf2;
    nf2     = new DecimalFormat("00");
    int nowdate  = Integer.parseInt(de.getDate(2));
    int nowtime  = Integer.parseInt(de.getTime(1));
    String date_format = (nowdate / 10000) + "/" + nf2.format(nowdate / 100 % 100) + "/" + nf2.format(nowdate % 100) + " " + nf2.format(nowtime / 10000) + ":" + nf2.format(nowtime / 100 % 100) + ":" + nf2.format(nowtime % 100);


// パス関連
    String requestUri = request.getRequestURI();
    String contextPath = request.getContextPath();
    if (requestUri.indexOf(contextPath)==0)
    {
        requestUri = requestUri.substring(contextPath.length() + 1);
    }

    String JSP_PATH = application.getRealPath(requestUri);
    String JSP_FILE = JSP_PATH.substring(JSP_PATH.lastIndexOf(File.separator) + 1, JSP_PATH.length());
    String BASE_PATH     = JSP_PATH.substring(0, JSP_PATH.length()-requestUri.length());
    String UPLOAD_DIR_PATH =  BASE_PATH + IMG_DIR;
    String IMG_DIR_PATH =  "/" + IMG_DIR;
    String UPLOAD_DIR_PATH_DISP =  BASE_PATH + IMG_DIR_DISP;
    String IMG_DIR_PATH_DISP =  "/" + IMG_DIR_DISP;

    File uploadFile = null;
    int start,end;
    int FileSize = 0;
    Hashtable params = new Hashtable();

// MIMEタイプを取得
    String contentType = request.getContentType();
    if (contentType!=null) {
        contentType = contentType.toLowerCase();
    }

    if (contentType!=null && contentType.startsWith("multipart/form-data")) // マルチパートの場合
    {
    // マルチパート用リクエスト取得
        FileUploadRequest uploadRequest;
        try
        {
            uploadRequest = new FileUploadRequest(request ,UPLOAD_DIR_PATH,MAX_POST_SIZE);
        }
        catch (ExceededLimitException e)
        {
            showErrorHTML(out,"アップロードできる最大容量を超えています",JSP_FILE);
            return;
        }
        catch (FileSizeZeroException e)
        {
            showErrorHTML(out,"アップロードするファイルのサイズが0です",JSP_FILE);
            return;
        }
        catch (Exception e)
        {
            showErrorHTML(out,"アップロード中にエラーが発生しました",JSP_FILE);
            e.printStackTrace();
            return;
        }
    // ファイル関連
        uploadFile = uploadRequest.getFile("uploadfile");

        if (uploadFile!=null)
        {
            String extension = uploadRequest.getFileExtension("uploadfile");
            if (extension==null)
            {
                uploadFile.delete();
                showErrorHTML(out,"アップロードするファイルの拡張子がありません",JSP_FILE);
                return;
            }
            extension = extension.toLowerCase();
            if (!fileExtensionCheck(extension))
            {
                uploadFile.delete();
                showErrorHTML(out,"拡張子が「" + extension + "」のファイルはアップロードできません",JSP_FILE);
                return;
            }

           //ファイルサイズを算出
            Double objectD = new Double(uploadFile.length());
            FileSize   = objectD.intValue();

           //ファイル名変換
            File fileRename = new File(uploadFile.getPath().replace(uploadRequest.getFilesystemName("uploadfile"),loginHotelId + "_" + nowdate + "_" + DATA_TYPE + newNo + "." + uploadRequest.getFileExtension("uploadfile")));
            uploadFile.renameTo(fileRename);
            uploadFile = fileRename;

            params.put("originalfilename" , uploadRequest.getOriginalFileName("uploadfile"));
            params.put("filename" , loginHotelId + "_" + nowdate + "_" + DATA_TYPE + newNo + "." + uploadRequest.getFileExtension("uploadfile"));
            params.put("extension" , uploadRequest.getFileExtension("uploadfile"));

        }

     // ページ切り替え関連
        String strStart = uploadRequest.getParameter("start");
        if (strStart==null)
        {
            strStart = "1";
        }
        strStart = ReplaceString.HTMLEscape(strStart); //20100910追加
        try
        {
            start = Integer.parseInt(strStart);
            if (start<1)
            {
                showErrorHTML(out,"パラメータが不正です",JSP_FILE);
                return;
            }
            end = start + PAGE_MAX -1;
        }
        catch (Exception e)
        {
            showErrorHTML(out,e.toString(),JSP_FILE);
            return;
        }
    // パラメーターを取得
        Enumeration paramNames = uploadRequest.getParameterNames();
        while (paramNames.hasMoreElements())
        {
            String paramName = (String)paramNames.nextElement();
            if (paramName.equals("del"))
            {
                String[] del = uploadRequest.getParameterValues("del");
                params.put("del" , del);
            }
            else
            {
                params.put(paramName,uploadRequest.getParameter(paramName));
            }
        }
    }
    else  // マルチパートでない場合
    {
    // ページ切り替え関連
        String strStart = request.getParameter("start");
        if (strStart==null)
        {
            strStart = "1";
        }
        try
        {
            start = Integer.parseInt(strStart);
            if (start<1)
            {
                showErrorHTML(out,"パラメータが不正です",JSP_FILE);
                return;
            }
            end = start + PAGE_MAX -1;
        }
        catch (Exception e)
        {
            showErrorHTML(out,e.toString(),JSP_FILE);
            return;
        }
    // パラメーターを取得
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements())
        {
            String paramName = (String)paramNames.nextElement();
            if (paramName.equals("del"))
            {
                String[] del = request.getParameterValues("del");
                params.put("del" , del);
            }
            else
            {
                params.put(paramName,toSJIS(request.getParameter(paramName)));
            }
        }
    }

// 現在の日付を設定
    params.put("date" , date_format);
// ホスト名を設定
    String host = request.getRemoteHost();
    if (host==null)
    {
        host = "不明";
    }
    params.put("host" , host);
// ブラウザ名を取得
    String userAgent = request.getHeader("User-Agent");
    params.put("user-agent" , userAgent);
// モードを取得
    String mode = (String)params.get("mode");
    if (mode==null)
    {
        mode = MODE_NORMAL;
    }

    boolean showdata = false;
    boolean showlogin = false;
    String msg = null;
    Hashtable mailData = new Hashtable();
    if (mode.equals(MODE_NORMAL))
    { /** 通常表示 **/
       showdata = true;
    }
    else if (mode.equals(MODE_REGIST))
    { /** 登録 **/
    // HTTPメソッドのチェック
        if (!request.getMethod().equalsIgnoreCase("POST"))
        {
            showErrorHTML(out,"不正なアクセスです",JSP_FILE);
            if (uploadFile!=null)
            {
                uploadFile.delete();
            }
            return;
        }
    // パラメーターのチェック
        msg = paramCheck(mode , params , mailData);
        if (msg!=null)
        {
            showErrorHTML(out,msg,JSP_FILE);
            if (uploadFile!=null)
            {
                uploadFile.delete();
            }
            return;
        }
        String fileName = (String)params.get("originalfilename");
        if  (fileName == null)  fileName="";
        if  (fileName.equals(""))
        {
            msg = "ファイルが選択されていません。";
        }

        fileName = ReplaceString.SQLEscape(fileName);

        query = "SELECT * FROM upload_files WHERE hotel_id=?";
        query = query + " AND data_type=" + DATA_TYPE;
        query = query + " AND del_flag=0";
        query = query + " AND originalfilename ='" + fileName + "'";
        connection  = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        result   = prestate.executeQuery();
        if( result.next() != false )
        {
            msg = "同じファイルがすでに投稿されています。";
        }
        DBConnection.releaseResources(result,prestate,connection);

        String message  = (String)params.get("message");
        if  (message.equals(""))
        {
            msg = "連絡事項をご入力ください。";
        }
        if (msg!=null)
        {
            showErrorHTML(out,msg,JSP_FILE);
            if (uploadFile!=null)
            {
                uploadFile.delete();
            }
            return;
        }
        showdata = true;
        String Message = (String)params.get("message");
        Message = new String(Message.getBytes("Shift_JIS"),"Windows-31J");
        String UserAgent = (String)params.get("user-agent");
        if (UserAgent.length() > 255)
        {
           UserAgent = UserAgent.substring(0,255);
        }

        connection  = DBConnection.getConnection();
        query = "INSERT INTO upload_files SET ";
        query = query + "hotel_id=?, ";
        query = query + "data_type=?, ";
        query = query + "id=?,";
        query = query + "originalfilename=?, ";
        query = query + "filename=?,";
        query = query + "extension=?,";
        query = query + "message=?, ";
        query = query + "filesize=?, ";
        query = query + "del_flag=?, ";
        query = query + "add_date=?, ";
        query = query + "add_time=?, ";
        query = query + "add_hotelid=?, ";
        query = query + "add_userid=?, ";
        query = query + "imedia_user=?, ";
        query = query + "user_agent=?, ";
        query = query + "host=?, ";
        query = query + "target_hotelid=?  ";
        try
        {
            prestate    = connection.prepareStatement(query);
            int col =1;
            prestate.setString(col++, loginHotelId          );
            prestate.setInt(col++, DATA_TYPE           );
            prestate.setInt(col++, newNo                                  );
            prestate.setString(col++, ReplaceString.SQLEscape((String)params.get("originalfilename")));
            prestate.setString(col++, ReplaceString.SQLEscape((String)params.get("filename"))      );
            prestate.setString(col++, ReplaceString.SQLEscape((String)params.get("extension"))     );
            prestate.setString(col++, ReplaceString.SQLEscape(Message)       );
            prestate.setInt(col++, FileSize                               );
            prestate.setInt(col++,0);
            prestate.setInt(col++, nowdate            );
            prestate.setInt(col++, nowtime            );
            prestate.setString(col++, loginHotelId    );
            prestate.setInt(col++, ownerinfo.DbUserId                  );
            prestate.setInt(col++, imedia_user                            );
            prestate.setString(col++, ReplaceString.SQLEscape(UserAgent)    );
            prestate.setString(col++, ReplaceString.SQLEscape((String)params.get("host"))          );
            prestate.setString(col++, hotelid                               );
            int retresult   = prestate.executeUpdate();
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
        catch( Exception e )
        {
%>
	<%= e.toString() %>
<%
        }
    // メールの送信
        String   mailaddr = ADMIN_MAIL;
        //投稿ホテル名の取得
        String hotelname= "";
        query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result   = prestate.executeQuery();
        if( result.next() != false )
        {
            hotelname = result.getString("name");
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        //投稿担当者名の取得
        String username= "";
        String mailaddr_user = "";
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result   = prestate.executeQuery();
        if (result.next() != false )
        {
            username    = result.getString("name");
            mailaddr_user = result.getString("mailaddr_pc");
        }
        DBConnection.releaseResources(result,prestate,connection);

     //FTP UPLOAD
//        int upd_ret = sendContents("webmaster","hotenavi",(String)params.get("filename"));
%>
<%
        if (imedia_user != 1) // アルメックス社員はメールを送信しない
        {
            if (mailaddr_user.compareTo("") == 0)
            {
               mailaddr_user =  ADMIN_MAIL;
            }
            String   title_mail = "";
            title_mail = "ファイルの投稿がありました。";
            title_mail  = title_mail     + "(" + loginHotelId + ")";
            String text = date_format    + "\r\n";
            text = text + "［グループ／ホテル名］" + "\r\n";
            text = text +  hotelname     + "(" + hotelid + ")" + "\r\n";
            text = text + "［ご担当者名］" + "\r\n";
            text = text +  username      + "様 (" + ownerinfo.DbUserId + ")" + "\r\n";
            text = text + "=====================" + "\r\n";
            text = text + "［元ファイル名］" + (String)params.get("originalfilename") + "\r\n";
            text = text + "［アップロード名］" + "https://owner.hotenavi.com" + IMG_DIR_PATH_DISP + "/" + (String)params.get("filename") + "\r\n";
            if(FileSize > 1000)
            {
                text = text + "［ファイルサイズ］" + (FileSize / 1024) + " KB\r\n";
            }
            else
            {
                text = text + "［ファイルサイズ］" + FileSize + " バイト\r\n";
            }
            text = text + "=====================" + "\r\n";
            text = text + "［連絡事項］" + "\r\n" + (String)params.get("message") + "\r\n";

        // メール送信を行う
            SendMail sendmail = new SendMail();
            sendmail.send(mailaddr_user, mailaddr, title_mail, text);
            sendmail.send(mailaddr_user, "imedia-info@hotenavi.com", title_mail, text);
        }
    }
    else if (mode.equals(MODE_DEL))
    { /** 削除 **/
    // HTTPメソッドのチェック
        if (!request.getMethod().equalsIgnoreCase("POST"))
        {
            showErrorHTML(out,"不正なアクセスです",JSP_FILE);
            return;
        }
    // パラメーターのチェック
        msg = paramCheck(mode , params , mailData);
        if (msg!=null)
        {
            showErrorHTML(out,msg,JSP_FILE);
            return;
        }
        showdata = true;
        String  input_id           = (String)params.get("no");
        String  paramHotelId       = (String)params.get("HotelId");
        if (paramHotelId == null)
        {
            paramHotelId = loginHotelId;
        }

        connection  = DBConnection.getConnection();
        query = "SELECT * FROM upload_files WHERE hotel_id=?";
        query = query + " AND data_type=" + DATA_TYPE;
        query = query + " AND del_flag=0";
        query = query + " AND id =?";
        prestate = connection.prepareStatement(query);
        prestate.setString(1, paramHotelId);
        prestate.setInt(2, Integer.parseInt(input_id));
        result   = prestate.executeQuery();
        if( result.next() != false )
        {
           // ファイルを削除します
            String filePath = UPLOAD_DIR_PATH + "/" + result.getString("filename");
            if (filePath!=null)
            {
                File file = new File(filePath);
                if (file.exists())
                {
                    file.delete();
                }
            }
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        query = "UPDATE upload_files SET del_flag=1 WHERE ";
        query = query + "hotel_id=? AND data_type=" + DATA_TYPE + " AND id=?";
        try
        {
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, paramHotelId);
            prestate.setInt(2, Integer.parseInt(input_id));
            int retresult   = prestate.executeUpdate();
            DBConnection.releaseResources(result,prestate,connection);
        }
        catch( Exception e )
        {
%>
	<%= e.toString() %>
<%
        }
    }

/** データ表示 **/
    if (showdata)
    {
%>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>ファイルアップロード</title>
<% if (loginHotelId.compareTo("hotenavi") != 0){%>
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<%}%>
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
</style>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
	<tr valign="top">
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td align="left" valign="middle" style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="3">
					<strong>ファイルアップロード</strong><br>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<form action="<%=JSP_FILE%>" method="POST" enctype="multipart/form-data">
			<blockquote>
			<table cellpadding=2 cellspacing=0 width="600">
				<tr>
					<td nowrap class="honbun"><strong>アップロードファイル</strong></td>
					<td nowrap class="honbun"><input name="uploadfile" type="file" id="uploadfile" size="40" class="honbun" style="background-color:#FFFFFF;color:#000000;"></td>
				</tr>
				<tr>
					<td nowrap class="honbun"><b>連絡事項をご記入ください</b><br>（必須）</td>
					<td nowrap class="honbun">
						<textarea name=message id=message cols="80" rows="20" wrap=soft tabindex=4>
以下の対応をお願いします。
=====================
【ホテル名】

【依頼内容】（必須）

【掲載箇所】

【掲載期間】

【トピック】
大見出し：

小見出し（1）：

トピック本文



=====================
						</textarea><br>
						<input type=submit value="アップロード" tabindex=8 name="submit" onclick="var inputMessage=document.getElementById('message').value.split('【依頼内容】（必須）');if(inputMessage.length<=1){alert('【依頼内容】（必須）を消さないでください');return false;}if(inputMessage[1].split('【掲載箇所】')[0].length<=2){alert('【依頼内容】（必須）を記入してください');return false;}if(document.getElementById('uploadfile').value==''){alert('ファイルを選択してください'); return false;}if(document.getElementById('message').value==''){alert('連絡事項を入力してください'); return false;}document.getElementById('loader').style.display='block';">
						<input type=hidden name=mode value="<%=MODE_REGIST%>">
					</td>
				</tr>
				<tr>
					<td class="honbun" colspan="2">
						※こちらでアップロードしたファイルはアルメックスにて画像表示をおこないます。<br>
<!--						※こちらでアップロードしたファイルはすぐにHP編集でご利用になれませんのでご注意ください。<br>-->
						※アップロードできるファイル<br>　
						<%  for (int i=0; i < EXTENSION_LIST.length; i++) {%><%if (i!=0){%>、<%}%><%=EXTENSION_LIST[i]%><%}%>
						<br>
						※アップロードできるファイルの最大サイズは、<%=MAX_POST_SIZE/1024000%>MBです。<br>
						※Excel(.xls .xlsx)、Word（.doc）、PowerPoint（.ppt）のファイルで広告画像をご入稿の場合、<br>
						　デザインの崩れや文字化けすることがございます。<br>
						　PDFファイルや画像形式（.jpg .gif）のファイルに変換してからのご入稿をおすすめいたします。<br>
						※連絡事項に応じて加工等の処理後、サーバーから削除いたします。
					</td>
				</tr>
			</table>
			</blockquote>
		</form>
		</td>
	</tr>
	<tr id="loader" style="display:none;">
		<td>
			<div style="text-align:center;font-size:13px;" class="honbun">
				ただいまアップロード中です。そのまましばらくお待ちください。<br>
			</div>
			<div style="text-align:center; margin:10px auto 10px auto;"><img src="../../common/pc/image/loader.gif" border="0"></div>
		</td>
	</tr>
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="5">
			<tr>
				<td class="honbun">
<%
    //投稿済みファイル件数を取得
        int count = 0;
        connection  = DBConnection.getConnection();
        if (imedia_user != 1)
        {
            query = "SELECT count(*) FROM upload_files,owner_user_hotel";
            query = query + " WHERE owner_user_hotel.hotelid =?";
            query = query + " AND owner_user_hotel.userid=?";
            query = query + " AND upload_files.hotel_id =?";
            query = query + " AND upload_files.target_hotelid = owner_user_hotel.accept_hotelid";
            query = query + " AND upload_files.data_type=" + DATA_TYPE;
            query = query + " AND upload_files.del_flag=0";
            query = query + " AND upload_files.imedia_user=0";
        }
        else
        {
            query = "SELECT count(*) FROM upload_files";
            query = query + " WHERE data_type=" + DATA_TYPE;
            query = query + " AND del_flag=0";
        }
        prestate = connection.prepareStatement(query);
        if (imedia_user != 1)
        {
        	prestate.setString(1, loginHotelId);
            prestate.setInt(2, ownerinfo.DbUserId);
            prestate.setString(3, loginHotelId);
        }
        result   = prestate.executeQuery();
        if( result.next() != false )
        {
            count = result.getInt(1);
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

        if (imedia_user != 1)
        {
            query = "SELECT * FROM upload_files,owner_user_hotel";
            query = query + " WHERE owner_user_hotel.hotelid =?";
            query = query + " AND owner_user_hotel.userid=?";
            query = query + " AND upload_files.hotel_id =?";
            query = query + " AND upload_files.target_hotelid = owner_user_hotel.accept_hotelid";
            query = query + " AND upload_files.data_type=" + DATA_TYPE;
            query = query + " AND upload_files.del_flag=0";
            query = query + " AND upload_files.imedia_user=0";
            query = query + " ORDER BY upload_files.add_date DESC,upload_files.add_time DESC";
        }
        else
        {
            query = "SELECT * FROM upload_files";
            query = query + " WHERE data_type=" + DATA_TYPE;
            query = query + " AND del_flag=0";
            query = query + " ORDER BY add_date DESC,add_time DESC";
        }
        query = query + " LIMIT " + (start-1) + "," + PAGE_MAX;
        prestate = connection.prepareStatement(query);
        if (imedia_user != 1)
        {
        	prestate.setString(1, loginHotelId);
            prestate.setInt(2, ownerinfo.DbUserId);
            prestate.setString(3, loginHotelId);
        }
        result   = prestate.executeQuery();


        int        id_image = 0;
        connection_user  = DBConnection.getConnection();
        while( result.next() != false )
        {
            id_image = result.getInt("id");
        // 投稿日付
            String date_image = (result.getInt("add_date") / 10000) + "/" + nf2.format(result.getInt("add_date") / 100 % 100) + "/" + nf2.format(result.getInt("add_date") % 100) + " " + nf2.format(result.getInt("add_time") / 10000) + ":" + nf2.format(result.getInt("add_time") / 100 % 100) + ":" + nf2.format(result.getInt("add_time") % 100);
        // 投稿ユーザー名取得
            String user_name="";
            query = "SELECT * FROM owner_user WHERE hotelid='" + result.getString("add_hotelid") + "'";
            query = query + " AND userid='" + result.getString("add_userid") + "'";
            prestate_user = connection_user.prepareStatement(query);
            result_user   = prestate_user.executeQuery();
            if( result_user != null )
            {
                if (result_user.next() != false )
                {
                    user_name = result_user.getString("name");
                }
                else
                {
                    user_name = "不明";
                }
            }
            DBConnection.releaseResources(result_user);
            DBConnection.releaseResources(prestate_user);
%>
			<hr size="1" noshade>
				[<%if(imedia_user == 1){%><%=result.getString("hotel_id")%>,<%}%><%=id_image%>]
				<small>ファイル名：<%=result.getString("originalfilename")%></small>
<%
            if(result.getInt("filesize") > 1000)
            {
%>
				<small>サイズ：<%=(result.getInt("filesize")/1024)%>KB</small>
<%
            }
            else
            {
%>
				<small>サイズ：<%=result.getInt("filesize")%>バイト</small>
<%
            }
%>
				<small>投稿者：<%=user_name%></small>
				<small>投稿日：<%=date_image%></small> <br>
				<blockquote>
<%
            if (result.getString("message")!=null)
            {
                if (result.getString("message").length() > 1)
                {
%>
				<small><%=result.getString("message").replace("\r","<br>")%></small> <br>
<%
                }
            }
%>
<%
            if (imedia_user == 1)
            {
%>
				<%if (result.getString("filename")!=null){%>[<a href="<%=IMG_DIR_PATH_DISP + "/" + (String)result.getString("filename")%>" target='_blank' class="link1">ダウンロード</a>]<%}%>
<%
            }
%>
				</blockquote>
<%
        }
        if (id_image == 0)
        {
            out.println("</blockquote>");
        }
        DBConnection.releaseResources(connection_user);
        DBConnection.releaseResources(result,prestate,connection);
%>
				<hr size="1" noshade>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table width="100%" border="0" cellpadding="5" cellspacing="0">
			<tr>
				<td  class="honbun">
<%
        int i = PAGE_MAX;
        boolean page_move = false;
        if (start - PAGE_MAX >= 1)
        {
            out.println("<input type=button value=\"前ページ\" onClick=\"location.href='" + JSP_FILE + "?start=" + (start - PAGE_MAX) + "'\"> ");
        }
        if (start + PAGE_MAX <= count)
        {
            out.println("<input type=button value=\"次ページ\" onClick=\"location.href='" + JSP_FILE + "?start=" + (start + PAGE_MAX) + "'\"> ");
        }
        if (count > i)
        {
            out.print("<br><br>[ページ] ");
            page_move = true;
        }
        while (count > i - PAGE_MAX)
        {
            if (i - PAGE_MAX + 1==start)
            {
                if (page_move)
                {
                    out.print("[<b>" + (i/PAGE_MAX) + "</b>] ");
                }
            }
            else
            {
                out.print("[<a href=\"" + JSP_FILE + "?start=" + (i - PAGE_MAX +1) +"\" class=\"link1\">" + (i/PAGE_MAX) + "</a>] ");
            }
            i+=PAGE_MAX;
        }
%>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table width="304" align=right cellspacing="0" cellpadding="2" border="0">
			<tr>
				<td nowrap align=center  class="honbun">
					<form action="<%=JSP_FILE%>" method="POST"  enctype="multipart/form-data">
					<input type=hidden name=mode value="<%=MODE_DEL%>">
<%
        if (imedia_user == 1)
        {
%>
					<small>HotelId</small>
					<input type=text name=HotelId size=10 maxlength="10" style="ime-mode: disabled">
<%
        }
%>
					<small>ファイルNo</small>
					<input type=text name=no size=3 maxlength="6" style="ime-mode: disabled">
					<input type=submit value="ファイル削除"  onclick="return confirm('本当に削除してよろしいですか？')">
					</form>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>
<%
    } // end if
%>

<%!
/**
 * ファイルの拡張子のチェック
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
 * パラメーターチェック
 */
private String paramCheck(String mode , Hashtable params , Hashtable mailData) {
  if (mode.equals(MODE_REGIST)) { // 登録
    String message = (String)params.get("message");

    if (message== null) {
      message = "";
    }

//    message = htmlEncode(message);

    params.put("message" , message);
  } else if (mode.equals(MODE_DEL)) { // ファイル削除
    String no = (String)params.get("no");
    if ((no==null || no.equals(""))) {
      return "ファイルNoが入力されていません";
    }
  }
  return null;
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
<%!
public static int sendContents(String user,String password,String filename)
throws IOException {
    FTPClient fp = new FTPClient();
    FileInputStream is = null;
    String host;
    host = "mars.hotenavi.com";
    try
    {
        fp.connect(host);
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // コネクトできたか？
            return 1;
        }

        if (fp.login(user, password) == false) { // ログインできたか？
            return 2;
        }
// ファイル送信
        is = new FileInputStream("/hotenavi/upload_files/temp/"+filename);// クライアント側
        fp.storeFile("/upload_files/others/"+filename, is);// サーバー側
        is.close();
    }
    finally
    {
        fp.disconnect();
    }
    return 0;
}
%>