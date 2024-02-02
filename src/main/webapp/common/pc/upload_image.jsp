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
    private static final int PAGE_MAX          = 10; // �P�y�[�W����̍ő�\���e�L����
    private static final int DATA_TYPE         = 0;  //  upload_files.data_type: 0����摜,1������̑�
    private static final boolean TOMCAT3       = false; // Tomcat 3�@�ŕ�����������ꍇtrue�ɐݒ�

// ���M�惁�[���A�h���X�i���M�����[���A�h���X���Ȃ��ꍇ�ɂ����p�j
    private static final String ADMIN_MAIL     = "g-happy@happyhotel.jp";
    private static final int SUB_MAX_LEN       = 15; // �薼�̒���(����ȏゾ�Əȗ�����܂�)

// �t�@�C���A�b�v���[�h�֘A�̐ݒ�
    private static final String IMG_DIR        = "upload_files/image"; // �A�b�v���[�h�����摜�����ۑ�����t�H���_
    private static final int MAX_POST_SIZE     = 1024 * 1000; // ���M����f�[�^�̍ő�T�C�Y(�o�C�g)
    private static final int IMG_WIDTH         = 600;  // �\������ۂ̉摜�̍ő啝

// ���[�h�萔
    private static final String MODE_NORMAL   = "normal"; // �ʏ�
    private static final String MODE_REGIST   = "regist"; // �o�^
    private static final String MODE_DEL      = "del"; // �폜

// �A�b�v���[�h��������摜�̊g���q
    private static final String[] EXTENSION_LIST = { "gif",
                                                     "jpg",
                                                     "jpeg",
                                                     "png" };
%>
<%
//MySql�p
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    Connection        connection_user  = null;
    PreparedStatement prestate_user    = null;
    ResultSet         result_user      = null;
    connection  = DBConnection.getConnection();
    String query = "";

//�z�e��ID
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

// imedia_user �̃`�F�b�N

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

//�������ݎ��ԍ��擾
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

//���ݓ���
    DateEdit  de = new DateEdit();
    NumberFormat     nf2;
    nf2     = new DecimalFormat("00");
    int nowdate  = Integer.parseInt(de.getDate(2));
    int nowtime  = Integer.parseInt(de.getTime(1));
    String date_format = (nowdate / 10000) + "/" + nf2.format(nowdate / 100 % 100) + "/" + nf2.format(nowdate % 100) + " " + nf2.format(nowtime / 10000) + ":" + nf2.format(nowtime / 100 % 100) + ":" + nf2.format(nowtime % 100);


// �p�X�֘A
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

// MIME�^�C�v���擾
    String contentType = request.getContentType();
    if (contentType!=null) {
        contentType = contentType.toLowerCase();
    }

    if (contentType!=null && contentType.startsWith("multipart/form-data")) // �}���`�p�[�g�̏ꍇ
    {
    // �}���`�p�[�g�p���N�G�X�g�擾
        FileUploadRequest uploadRequest;
        try
        {
            uploadRequest = new FileUploadRequest(request ,UPLOAD_DIR_PATH,MAX_POST_SIZE);
        }
        catch (ExceededLimitException e)
        {
            showErrorHTML(out,"�A�b�v���[�h�ł���ő�e�ʂ𒴂��Ă��܂�",JSP_FILE);
            return;
        }
        catch (FileSizeZeroException e)
        {
            showErrorHTML(out,"�A�b�v���[�h����t�@�C���̃T�C�Y��0�ł�",JSP_FILE);
            return;
        }
        catch (Exception e)
        {
            showErrorHTML(out,"�A�b�v���[�h���ɃG���[���������܂���",JSP_FILE);
            e.printStackTrace();
            return;
        }
    // �摜�֘A
        uploadFile = uploadRequest.getFile("uploadfile");
        if (uploadFile!=null)
        {
            String extension = uploadRequest.getFileExtension("uploadfile");
            if (extension==null)
            {
                uploadFile.delete();
                showErrorHTML(out,"�A�b�v���[�h����t�@�C���̊g���q������܂���",JSP_FILE);
                return;
            }
            extension = extension.toLowerCase();
            if (!fileExtensionCheck(extension))
            {
                uploadFile.delete();
                showErrorHTML(out,"�g���q���u" + extension + "�v�̃t�@�C���̓A�b�v���[�h�ł��܂���",JSP_FILE);
                return;
            }
            try
            {
                ImageInfomation imageInfo = new ImageInfomation(uploadFile);
                if (!imageInfo.checkImage())
                {
                    uploadFile.delete();
                    showErrorHTML(out,"�摜�̃t�H�[�}�b�g���s���ł�",JSP_FILE);
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
                showErrorHTML(out,"�摜�̃t�H�[�}�b�g���s���ł�",JSP_FILE);
                return;
            }

           //�t�@�C���T�C�Y���Z�o
            Double objectD = new Double(uploadFile.length());
            FileSize   = objectD.intValue();

           //�t�@�C�����ϊ�
            File fileRename = new File(uploadFile.getPath().replace(uploadRequest.getFilesystemName("uploadfile"),loginHotelId + "_" + nowdate + "_" + DATA_TYPE + newNo + "." + uploadRequest.getFileExtension("uploadfile")));
            uploadFile.renameTo(fileRename);
            uploadFile = fileRename;

            params.put("originalfilename" , uploadRequest.getOriginalFileName("uploadfile"));
            params.put("filename" , loginHotelId + "_" + nowdate + "_" + DATA_TYPE + newNo + "." + uploadRequest.getFileExtension("uploadfile"));
            params.put("extension" , uploadRequest.getFileExtension("uploadfile"));

        }

     // �y�[�W�؂�ւ��֘A
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
                showErrorHTML(out,"�p�����[�^���s���ł�",JSP_FILE);
                return;
            }
            end = start + PAGE_MAX -1;
        }
        catch (Exception e)
        {
            showErrorHTML(out,e.toString(),JSP_FILE);
            return;
        }
    // �p�����[�^�[���擾
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
    else  // �}���`�p�[�g�łȂ��ꍇ
    {
    // �y�[�W�؂�ւ��֘A
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
                showErrorHTML(out,"�p�����[�^���s���ł�",JSP_FILE);
                return;
            }
            end = start + PAGE_MAX -1;
        }
        catch (Exception e)
        {
            showErrorHTML(out,e.toString(),JSP_FILE);
            return;
        }
    // �p�����[�^�[���擾
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

// ���݂̓��t��ݒ�
    params.put("date" , date_format);
// �z�X�g����ݒ�
    String host = request.getRemoteHost();
    if (host==null)
    {
        host = "�s��";
    }
    params.put("host" , host);
// �u���E�U�����擾
    String userAgent = request.getHeader("User-Agent");
    params.put("user-agent" , userAgent);
// ���[�h���擾
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
    { /** �ʏ�\�� **/
       showdata = true;
    }
    else if (mode.equals(MODE_REGIST))
    { /** �o�^ **/
    // HTTP���\�b�h�̃`�F�b�N
        if (!request.getMethod().equalsIgnoreCase("POST"))
        {
            showErrorHTML(out,"�s���ȃA�N�Z�X�ł�",JSP_FILE);
            if (uploadFile!=null)
            {
                uploadFile.delete();
            }
            return;
        }
    // �p�����[�^�[�̃`�F�b�N
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
            msg = "�摜���I������Ă��܂���B";
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
            msg = "�����摜�����łɓ��e����Ă��܂��B";
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
    // ���[���̑��M
        String   mailaddr = ADMIN_MAIL;
        //���e�z�e�����̎擾
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
        //���e�S���Җ��̎擾
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

        if (imedia_user != 1) // �A�����b�N�X�Ј��̓��[���𑗐M���Ȃ�
        {
            if (mailaddr_user.compareTo("") == 0)
            {
               mailaddr_user =  ADMIN_MAIL;
            }
            String   title_mail = "";
            title_mail = "�摜�̓��e������܂����B";
            title_mail  = title_mail     + "(" + loginHotelId + ")";
            String text = date_format    + "\r\n";
            text = text + "�m�O���[�v�^�z�e�����n" + "\r\n";
            text = text +  hotelname     + "(" + hotelid + ")" + "\r\n";
            text = text + "�m�S���Җ��n" + "\r\n";
            text = text +  username      + "(" +  ownerinfo.DbUserId + ")" + "\r\n";
            text = text + "=====================" + "\r\n";
            text = text + "�m���摜���n" + (String)params.get("originalfilename") + "\r\n";
            text = text + "�m�� �� ���n" + "http://owner.hotenavi.com" + IMG_DIR_PATH + "/" + (String)params.get("filename") + "\r\n";
            text = text + "�m�摜�����n" + (String)params.get("subject") + "\r\n";
            text = text + "�m�� �� ���n" + (String)params.get("originalwidth") + " �~ "+ (String)params.get("originalheight") + "\r\n";
            if(FileSize > 1000)
            {
                text = text + "�m�T �C �Y�n" + (FileSize / 1024) + " KB\r\n";
            }
            else
            {
                text = text + "�m�T �C �Y�n" + FileSize + " �o�C�g\r\n";
            }
            text = text + "=====================" + "\r\n";
            text = text + "�m�A�������n" + "\r\n" + (String)params.get("message") + "\r\n";

        // ���[�����M���s��
            SendMail sendmail = new SendMail();
            sendmail.send(mailaddr_user, mailaddr, title_mail, text);
        }
    }
    else if (mode.equals(MODE_DEL))
    { /** �폜 **/
    // HTTP���\�b�h�̃`�F�b�N
        if (!request.getMethod().equalsIgnoreCase("POST"))
        {
            showErrorHTML(out,"�s���ȃA�N�Z�X�ł�",JSP_FILE);
            return;
        }
    // �p�����[�^�[�̃`�F�b�N
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
           // �摜���폜���܂�
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

/** �f�[�^�\�� **/
    if (showdata)
    {
%>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>�摜�A�b�v���[�h</title>
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
					<strong>�摜�A�b�v���[�h</strong><br>
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
					<td nowrap class="honbun"><strong>�摜</strong></td>
					<td nowrap class="honbun"><input name="uploadfile" type="file" id="uploadfile" size="40" class="honbun" style="background-color:#FFFFFF;color:#000000;"></td>
				</tr>
				<tr>
					<td nowrap class="honbun"><b>�摜����</b></td>
					<td nowrap class="honbun">
						<input type=text name=subject size="30" value="" maxlength="100" tabindex=3>
						<input type=submit value="�A�b�v���[�h" tabindex=8 name="submit" onclick="if(document.getElementById('uploadfile').value==''){alert('�摜��I�����Ă�������'); return false;}document.getElementById('loader').style.display='block';">
						<input type=hidden name=mode value="<%=MODE_REGIST%>">
					</td>
				</tr>
				<tr>
					<td nowrap class="honbun"><b>�A������</b></td>
					<td nowrap class="honbun">
						<textarea name=message cols="50" rows="3" wrap=soft tabindex=4></textarea>
					</td>
				</tr>
				<tr>
					<td class="honbun" colspan="2">
						���A�b�v���[�h�����摜�́AHP�ҏW�́u�摜�\���v�ł����p�ɂȂ�܂��B<br>
						���A�b�v���[�h�ł���摜�́Agif�Ajpg�Apng �݂̂ƂȂ�܂��B<br>
						���A�b�v���[�h�ł���摜�̍ő�T�C�Y�́A<%=(MAX_POST_SIZE/1024)/1000%>.<%=((MAX_POST_SIZE/1024)%1000)/100%> MB�ł��B<br>
						���摜�̉����T�C�Y��600px�ȓ��Ɏ��߂邱�Ƃ𐄏����܂��B<br>
						�������T�C�Y��600px�𒴂����摜�́AHP�ҏW�f�ڎ��ɉ���600px�ɒ�������܂��B�i�ύX�j<br>
						�����쌠���̐N�Q������Ǒ��ɔ�����Ɣ��f�������̂͌f�ڂ����f�肵�܂��̂ł��������������B<br>
						����L�ȊO�̊g���q�̃t�@�C���̃A�b�v���[�h�A��e�ʃt�@�C���̃A�b�v���[�h��<a href="upload_file.jsp" class="link1"><strong>������</strong></a>�������p���������B
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
				�������܃A�b�v���[�h���ł��B���̂܂܂��΂炭���҂����������B<br>
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
    //���e�ς݉摜�������擾
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
        // �薼�̒���
            String subject = omitString(result.getString("subject"),SUB_MAX_LEN);
        // ���e���t
            String date_image = (result.getInt("add_date") / 10000) + "/" + nf2.format(result.getInt("add_date") / 100 % 100) + "/" + nf2.format(result.getInt("add_date") % 100) + " " + nf2.format(result.getInt("add_time") / 10000) + ":" + nf2.format(result.getInt("add_time") / 100 % 100) + ":" + nf2.format(result.getInt("add_time") % 100);
        // ���e���[�U�[���擾
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
                    user_name = "�s��";
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
				<small>�摜���F<%=result.getString("filename")%></small>
<%
            }
%>
				<small>���摜���F<%=result.getString("originalfilename")%></small>
				<small>���e�ҁF<%=user_name%></small>
				<small>���e���F<%=date_image%></small> <br>
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
            out.println("<input type=button value=\"�O�y�[�W\" onClick=\"location.href='" + JSP_FILE + "?start=" + (start - PAGE_MAX) + "'\"> ");
        }
        if (start + PAGE_MAX <= count)
        {
            out.println("<input type=button value=\"���y�[�W\" onClick=\"location.href='" + JSP_FILE + "?start=" + (start + PAGE_MAX) + "'\"> ");
        }
        if (count > i)
        {
            out.print("<br><br>[�y�[�W] ");
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
					<small>�摜No</small>
					<input type=text name=no size=3 maxlength="6" style="ime-mode: disabled">
					<input type=submit value="�摜�폜" onclick="return confirm('�폜����ƁA�f�ڒ��̉摜���\������Ȃ��Ȃ�܂��B�{���ɂ�낵���ł����H')">
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
 * �摜�̊g���q�̃`�F�b�N
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
 * �p�����[�^�[�`�F�b�N
 */
private String paramCheck(String mode , Hashtable params , Hashtable mailData) {
  if (mode.equals(MODE_REGIST)) { // �o�^
    String subject = (String)params.get("subject");
    String message = (String)params.get("message");

    if (subject== null || subject.equals("")) {
      subject = "����";
    }

//    subject = htmlEncode(subject);

    params.put("subject" , subject);
    params.put("message" , message);
  } else if (mode.equals(MODE_DEL)) { // �摜�폜
    String no = (String)params.get("no");
    if ((no==null || no.equals(""))) {
      return "�摜No�����͂���Ă��܂���";
    }
  }
  return null;
}
/**
 * �p�����[�^�[�̒l��K�؂ɕϊ�
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
 * HTML�G���R�[�h
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
 * �������u��
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
 * �����񂪒����ꍇ�ȗ����܂�
 */
private String omitString(String input, int len) {
  if (input.length()<=len) {
    return input;
  }
  return input.substring(0,len) + "�D�D";
}

/**
 * �G���[�o��
 */
private void showErrorHTML(JspWriter out, String msg , String back) throws IOException {
  out.println("<html>");
  out.println("<head>");
  out.println("<meta http-equiv=\"Content-type\" CONTENT=\"text/html; charset=Windows-31J\">");
  out.println("<title>�G���[</title>");
  out.println("<style type=\"text/css\">");
  out.println("<!--");
  out.println("body { font-size:10pt }");
  out.println("-->");
  out.println("</style>");
  out.println("</head>");
  out.println("<body bgcolor=\"#FFFFFF\">");
  out.println("<hr width=\"350\" noshade size=\"1\">");
  out.println("<center>");
  out.println("<h4>�G���[</h4>");
  out.println("<p><font color='#CC0000'>" + msg + "</font>");
  out.println("<p>[<a href=\"" + back + "\">�߂�</a>]");
  out.println("</center>");
  out.println("<hr width=\"350\" noshade size=\"1\">");
  out.println("</body>");
  out.println("</html>");
}
%>
