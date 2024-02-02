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
<%@ page import="jp.happyhotel.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%!
    private static final int PAGE_MAX          = 10; // １ページ当りの最大表示親記事数
    private static final int DATA_TYPE         = 0;  //  upload_files.data_type: 0･･･画像,1･･･その他
    private static final boolean TOMCAT3       = false; // Tomcat 3　で文字化けする場合trueに設定

// 送信先メールアドレス（送信元メールアドレスがない場合にも利用）
    private static final String ADMIN_MAIL     = "g-happy@happyhotel.jp";
    private static final int SUB_MAX_LEN       = 15; // 題名の長さ(これ以上だと省略されます)

// ファイルアップロード関連の設定
    private static final String IMG_DIR        = "upload_files/image"; // アップロードした画像を仮保存するフォルダ
    private static final int MAX_POST_SIZE     = 1024 * 1000; // 送信するデータの最大サイズ(バイト)
    private static final int IMG_WIDTH         = 600;  // 表示する際の画像の最大幅

// モード定数
    private static final String MODE_NORMAL   = "normal"; // 通常
    private static final String MODE_REGIST   = "regist"; // 登録
    private static final String MODE_DEL      = "del"; // 削除

// アップロードを許可する画像の拡張子
    private static final String[] EXTENSION_LIST = { "gif",
                                                     "jpg",
                                                     "jpeg",
                                                     "png" };
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
    // 画像関連
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
            try
            {
                ImageInfomation imageInfo = new ImageInfomation(uploadFile);
                if (!imageInfo.checkImage())
                {
                    uploadFile.delete();
                    showErrorHTML(out,"画像のフォーマットが不正です",JSP_FILE);
                    return;
                }
                int imgWidth       = imageInfo.getWidth();
                int originalWidth  = imageInfo.getWidth();
                int imgHeight      = imageInfo.getHeight();
                int originalHeight = imageInfo.getHeight();
                if (imgWidth > IMG_WIDTH)
                {
                    imgWidth = IMG_WIDTH;
                    imgHeight = (originalHeight * (imgWidth*10000 / originalWidth))/10000;
                }
                params.put("imgwidth" , String.valueOf(imgWidth));
                params.put("originalwidth" , String.valueOf(originalWidth));
                params.put("imgheight" , String.valueOf(imgHeight));
                params.put("originalheight" , String.valueOf(originalHeight));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                uploadFile.delete();
                showErrorHTML(out,"画像のフォーマットが不正です",JSP_FILE);
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
        String strStart = ReplaceString.getParameter(request,"start");
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
                params.put(paramName,toSJIS(ReplaceString.getParameter(request,paramName)));
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
            msg = "画像が選択されていません。";
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
            msg = "同じ画像がすでに投稿されています。";
        }
        DBConnection.releaseResources(result,prestate,connection);
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

        String Subject = (String)params.get("subject");
        Subject = new String(Subject.getBytes("Shift_JIS"),"Windows-31J");
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
        query = query + "subject=?,";
        query = query + "message=?, ";
        query = query + "extension=?, ";
        query = query + "filesize=?, ";
        query = query + "originalwidth=?, ";
        query = query + "imgwidth=?, ";
        query = query + "originalheight=?, ";
        query = query + "imgheight=?, ";
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
            prestate.setString(col++, ReplaceString.SQLEscape(Subject)       );
            prestate.setString(col++, ReplaceString.SQLEscape(Message)       );
            prestate.setString(col++, ReplaceString.SQLEscape((String)params.get("extension"))     );
            prestate.setInt(col++, FileSize                               );
            prestate.setString(col++, ReplaceString.SQLEscape((String)params.get("originalwidth")) );
            prestate.setString(col++, ReplaceString.SQLEscape((String)params.get("imgwidth"))      );
            prestate.setString(col++, ReplaceString.SQLEscape((String)params.get("originalheight")));
            prestate.setString(col++, ReplaceString.SQLEscape((String)params.get("imgheight"))     );
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

        if (imedia_user != 1) // アルメックス社員はメールを送信しない
        {
            if (mailaddr_user.compareTo("") == 0)
            {
               mailaddr_user =  ADMIN_MAIL;
            }
            String   title_mail = "";
            title_mail = "画像の投稿がありました。";
            title_mail  = title_mail     + "(" + loginHotelId + ")";
            String text = date_format    + "\r\n";
            text = text + "［グループ／ホテル名］" + "\r\n";
            text = text +  hotelname     + "(" + hotelid + ")" + "\r\n";
            text = text + "［担当者名］" + "\r\n";
            text = text +  username      + "(" +  ownerinfo.DbUserId + ")" + "\r\n";
            text = text + "=====================" + "\r\n";
            text = text + "［元画像名］" + (String)params.get("originalfilename") + "\r\n";
            text = text + "［画 像 名］" + "http://owner.hotenavi.com" + IMG_DIR_PATH + "/" + (String)params.get("filename") + "\r\n";
            text = text + "［画像説明］" + (String)params.get("subject") + "\r\n";
            text = text + "［大 き さ］" + (String)params.get("originalwidth") + " × "+ (String)params.get("originalheight") + "\r\n";
            if(FileSize > 1000)
            {
                text = text + "［サ イ ズ］" + (FileSize / 1024) + " KB\r\n";
            }
            else
            {
                text = text + "［サ イ ズ］" + FileSize + " バイト\r\n";
            }
            text = text + "=====================" + "\r\n";
            text = text + "［連絡事項］" + "\r\n" + (String)params.get("message") + "\r\n";

        // メール送信を行う
            SendMail sendmail = new SendMail();
            sendmail.send(mailaddr_user, mailaddr, title_mail, text);
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
           // 画像を削除します
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
<title>画像アップロード</title>
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
					<strong>画像アップロード</strong><br>
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
					<td nowrap class="honbun"><strong>画像</strong></td>
					<td nowrap class="honbun"><input name="uploadfile" type="file" id="uploadfile" size="40" class="honbun" style="background-color:#FFFFFF;color:#000000;"></td>
				</tr>
				<tr>
					<td nowrap class="honbun"><b>画像名称</b></td>
					<td nowrap class="honbun">
						<input type=text name=subject size="30" value="" maxlength="100" tabindex=3>
						<input type=submit value="アップロード" tabindex=8 name="submit" onclick="if(document.getElementById('uploadfile').value==''){alert('画像を選択してください'); return false;}document.getElementById('loader').style.display='block';">
						<input type=hidden name=mode value="<%=MODE_REGIST%>">
					</td>
				</tr>
				<tr>
					<td nowrap class="honbun"><b>連絡事項</b></td>
					<td nowrap class="honbun">
						<textarea name=message cols="50" rows="3" wrap=soft tabindex=4></textarea>
					</td>
				</tr>
				<tr>
					<td class="honbun" colspan="2">
						※アップロードした画像は、HP編集の「画像表示」でご利用になれます。<br>
						※アップロードできる画像は、gif、jpg、png のみとなります。<br>
						※アップロードできる画像の最大サイズは、<%=(MAX_POST_SIZE/1024)/1000%>.<%=((MAX_POST_SIZE/1024)%1000)/100%> MBです。<br>
						※画像の横幅サイズは600px以内に収めることを推奨します。<br>
						※横幅サイズが600pxを超えた画像は、HP編集掲載時に横幅600pxに調整されます。（変更可）<br>
						※著作権等の侵害や公序良俗に反すると判断されるものは掲載をお断りしますのでご了承ください。<br>
						※上記以外の拡張子のファイルのアップロード、大容量ファイルのアップロードは<a href="upload_file.jsp" class="link1"><strong>こちら</strong></a>をご利用ください。
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
    //投稿済み画像件数を取得
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
            query = query + " WHERE (hotel_id=?";
            query = query + " OR  target_hotelid =?)";
            query = query + " AND data_type=" + DATA_TYPE;
            query = query + " AND del_flag=0";
        }
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        if (imedia_user != 1)
        {
        	prestate.setString(1, loginHotelId);
            prestate.setInt(2, ownerinfo.DbUserId);
            prestate.setString(3, loginHotelId);
        }
        else
        {
           	prestate.setString(1, loginHotelId);
            prestate.setString(2, hotelid);
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
        }
        else
        {
            query = "SELECT * FROM upload_files";
            query = query + " WHERE (hotel_id=?";
            query = query + " OR  target_hotelid =?)";
            query = query + " AND data_type=" + DATA_TYPE;
            query = query + " AND del_flag=0";
        }
        query = query + " ORDER BY add_date DESC,add_time DESC";
        query = query + " LIMIT " + (start-1) + "," + PAGE_MAX;
        prestate = connection.prepareStatement(query);
        if (imedia_user != 1)
        {
        	prestate.setString(1, loginHotelId);
            prestate.setInt(2, ownerinfo.DbUserId);
            prestate.setString(3, loginHotelId);
        }
        else
        {
           	prestate.setString(1, loginHotelId);
            prestate.setString(2, hotelid);
        }
        result   = prestate.executeQuery();

        int        id_image = 0;
        connection_user  = DBConnection.getConnection();
        while( result.next() != false )
        {
            id_image = result.getInt("id");
        // 題名の長さ
            String subject = omitString(result.getString("subject"),SUB_MAX_LEN);
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
<%
            if (imedia_user == 1 || !loginHotelId.equals("demo") || params.get("user-agent").equals(result.getString("user_agent"))&& params.get("host").equals(result.getString("host")))
            {
%>
				<small>画像名：<%=result.getString("filename")%></small>
<%
            }
%>
				<small>元画像名：<%=result.getString("originalfilename")%></small>
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
            if (imedia_user == 1 || !loginHotelId.equals("demo") || params.get("user-agent").equals(result.getString("user_agent"))&& params.get("host").equals(result.getString("host")))
            {
%>
				<%if (result.getString("filename")!=null){%><a href="<%=IMG_DIR_PATH + "/" + (String)result.getString("filename")%>" target='_blank'><img src="<%=IMG_DIR_PATH + "/" + result.getString("filename")%>" width="<%=result.getString("imgwidth")%>" border="0"></a><%}%>
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
					<small>画像No</small>
					<input type=text name=no size=3 maxlength="6" style="ime-mode: disabled">
					<input type=submit value="画像削除" onclick="return confirm('削除すると、掲載中の画像が表示されなくなります。本当によろしいですか？')">
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
 * パラメーターチェック
 */
private String paramCheck(String mode , Hashtable params , Hashtable mailData) {
  if (mode.equals(MODE_REGIST)) { // 登録
    String subject = (String)params.get("subject");
    String message = (String)params.get("message");

    if (subject== null || subject.equals("")) {
      subject = "無題";
    }

//    subject = htmlEncode(subject);

    params.put("subject" , subject);
    params.put("message" , message);
  } else if (mode.equals(MODE_DEL)) { // 画像削除
    String no = (String)params.get("no");
    if ((no==null || no.equals(""))) {
      return "画像Noが入力されていません";
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
