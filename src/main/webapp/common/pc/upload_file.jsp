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
    private static final int PAGE_MAX          = 10; // �P�y�[�W����̍ő�\���e�L����
    private static final int DATA_TYPE         = 1;  //  upload_files.data_type: 0����摜,1������̑��t�@�C��
    private static final boolean TOMCAT3       = false; // Tomcat 3�@�ŕ�����������ꍇtrue�ɐݒ�

// ���M�惁�[���A�h���X�i���M�����[���A�h���X���Ȃ��ꍇ�ɂ����p�j
    private static final String ADMIN_MAIL     = "g-happy@happyhotel.jp";
    private static final int SUB_MAX_LEN       = 15; // �薼�̒���(����ȏゾ�Əȗ�����܂�)

// �t�@�C���A�b�v���[�h�֘A�̐ݒ�
    private static final String IMG_DIR        = "upload_files/others"; // �A�b�v���[�h�����t�@�C����ۑ�����t�H���_
    private static final String IMG_DIR_DISP   = "upload_files/others"; // �A�b�v���[�h�����t�@�C����ۑ�����t�H���_
    private static final int MAX_POST_SIZE     = 1024 * 100000; // ���M����f�[�^�̍ő�T�C�Y(�o�C�g)

// ���[�h�萔
    private static final String MODE_NORMAL   = "normal"; // �ʏ�
    private static final String MODE_REGIST   = "regist"; // �o�^
    private static final String MODE_DEL      = "del"; // �폜

// �A�b�v���[�h��������t�@�C���̊g���q
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
    String UPLOAD_DIR_PATH_DISP =  BASE_PATH + IMG_DIR_DISP;
    String IMG_DIR_PATH_DISP =  "/" + IMG_DIR_DISP;

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
    // �t�@�C���֘A
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
        strStart = ReplaceString.HTMLEscape(strStart); //20100910�ǉ�
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
                params.put(paramName,toSJIS(request.getParameter(paramName)));
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
            msg = "�t�@�C�����I������Ă��܂���B";
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
            msg = "�����t�@�C�������łɓ��e����Ă��܂��B";
        }
        DBConnection.releaseResources(result,prestate,connection);

        String message  = (String)params.get("message");
        if  (message.equals(""))
        {
            msg = "�A�������������͂��������B";
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
%>
<%
        if (imedia_user != 1) // �A�����b�N�X�Ј��̓��[���𑗐M���Ȃ�
        {
            if (mailaddr_user.compareTo("") == 0)
            {
               mailaddr_user =  ADMIN_MAIL;
            }
            String   title_mail = "";
            title_mail = "�t�@�C���̓��e������܂����B";
            title_mail  = title_mail     + "(" + loginHotelId + ")";
            String text = date_format    + "\r\n";
            text = text + "�m�O���[�v�^�z�e�����n" + "\r\n";
            text = text +  hotelname     + "(" + hotelid + ")" + "\r\n";
            text = text + "�m���S���Җ��n" + "\r\n";
            text = text +  username      + "�l (" + ownerinfo.DbUserId + ")" + "\r\n";
            text = text + "=====================" + "\r\n";
            text = text + "�m���t�@�C�����n" + (String)params.get("originalfilename") + "\r\n";
            text = text + "�m�A�b�v���[�h���n" + "https://owner.hotenavi.com" + IMG_DIR_PATH_DISP + "/" + (String)params.get("filename") + "\r\n";
            if(FileSize > 1000)
            {
                text = text + "�m�t�@�C���T�C�Y�n" + (FileSize / 1024) + " KB\r\n";
            }
            else
            {
                text = text + "�m�t�@�C���T�C�Y�n" + FileSize + " �o�C�g\r\n";
            }
            text = text + "=====================" + "\r\n";
            text = text + "�m�A�������n" + "\r\n" + (String)params.get("message") + "\r\n";

        // ���[�����M���s��
            SendMail sendmail = new SendMail();
            sendmail.send(mailaddr_user, mailaddr, title_mail, text);
            sendmail.send(mailaddr_user, "imedia-info@hotenavi.com", title_mail, text);
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
           // �t�@�C�����폜���܂�
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

/** �f�[�^�\�� **/
    if (showdata)
    {
%>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>�t�@�C���A�b�v���[�h</title>
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
					<strong>�t�@�C���A�b�v���[�h</strong><br>
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
					<td nowrap class="honbun"><strong>�A�b�v���[�h�t�@�C��</strong></td>
					<td nowrap class="honbun"><input name="uploadfile" type="file" id="uploadfile" size="40" class="honbun" style="background-color:#FFFFFF;color:#000000;"></td>
				</tr>
				<tr>
					<td nowrap class="honbun"><b>�A�����������L����������</b><br>�i�K�{�j</td>
					<td nowrap class="honbun">
						<textarea name=message id=message cols="80" rows="20" wrap=soft tabindex=4>
�ȉ��̑Ή������肢���܂��B
=====================
�y�z�e�����z

�y�˗����e�z�i�K�{�j

�y�f�ډӏ��z

�y�f�ڊ��ԁz

�y�g�s�b�N�z
�匩�o���F

�����o���i1�j�F

�g�s�b�N�{��



=====================
						</textarea><br>
						<input type=submit value="�A�b�v���[�h" tabindex=8 name="submit" onclick="var inputMessage=document.getElementById('message').value.split('�y�˗����e�z�i�K�{�j');if(inputMessage.length<=1){alert('�y�˗����e�z�i�K�{�j�������Ȃ��ł�������');return false;}if(inputMessage[1].split('�y�f�ډӏ��z')[0].length<=2){alert('�y�˗����e�z�i�K�{�j���L�����Ă�������');return false;}if(document.getElementById('uploadfile').value==''){alert('�t�@�C����I�����Ă�������'); return false;}if(document.getElementById('message').value==''){alert('�A����������͂��Ă�������'); return false;}document.getElementById('loader').style.display='block';">
						<input type=hidden name=mode value="<%=MODE_REGIST%>">
					</td>
				</tr>
				<tr>
					<td class="honbun" colspan="2">
						��������ŃA�b�v���[�h�����t�@�C���̓A�����b�N�X�ɂĉ摜�\���������Ȃ��܂��B<br>
<!--						��������ŃA�b�v���[�h�����t�@�C���͂�����HP�ҏW�ł����p�ɂȂ�܂���̂ł����ӂ��������B<br>-->
						���A�b�v���[�h�ł���t�@�C��<br>�@
						<%  for (int i=0; i < EXTENSION_LIST.length; i++) {%><%if (i!=0){%>�A<%}%><%=EXTENSION_LIST[i]%><%}%>
						<br>
						���A�b�v���[�h�ł���t�@�C���̍ő�T�C�Y�́A<%=MAX_POST_SIZE/1024000%>MB�ł��B<br>
						��Excel(.xls .xlsx)�AWord�i.doc�j�APowerPoint�i.ppt�j�̃t�@�C���ōL���摜�������e�̏ꍇ�A<br>
						�@�f�U�C���̕���╶���������邱�Ƃ��������܂��B<br>
						�@PDF�t�@�C����摜�`���i.jpg .gif�j�̃t�@�C���ɕϊ����Ă���̂����e���������߂������܂��B<br>
						���A�������ɉ����ĉ��H���̏�����A�T�[�o�[����폜�������܂��B
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
    //���e�ς݃t�@�C���������擾
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
				<small>�t�@�C�����F<%=result.getString("originalfilename")%></small>
<%
            if(result.getInt("filesize") > 1000)
            {
%>
				<small>�T�C�Y�F<%=(result.getInt("filesize")/1024)%>KB</small>
<%
            }
            else
            {
%>
				<small>�T�C�Y�F<%=result.getInt("filesize")%>�o�C�g</small>
<%
            }
%>
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
            if (imedia_user == 1)
            {
%>
				<%if (result.getString("filename")!=null){%>[<a href="<%=IMG_DIR_PATH_DISP + "/" + (String)result.getString("filename")%>" target='_blank' class="link1">�_�E�����[�h</a>]<%}%>
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
					<small>�t�@�C��No</small>
					<input type=text name=no size=3 maxlength="6" style="ime-mode: disabled">
					<input type=submit value="�t�@�C���폜"  onclick="return confirm('�{���ɍ폜���Ă�낵���ł����H')">
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
 * �t�@�C���̊g���q�̃`�F�b�N
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
    String message = (String)params.get("message");

    if (message== null) {
      message = "";
    }

//    message = htmlEncode(message);

    params.put("message" , message);
  } else if (mode.equals(MODE_DEL)) { // �t�@�C���폜
    String no = (String)params.get("no");
    if ((no==null || no.equals(""))) {
      return "�t�@�C��No�����͂���Ă��܂���";
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
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // �R�l�N�g�ł������H
            return 1;
        }

        if (fp.login(user, password) == false) { // ���O�C���ł������H
            return 2;
        }
// �t�@�C�����M
        is = new FileInputStream("/hotenavi/upload_files/temp/"+filename);// �N���C�A���g��
        fp.storeFile("/upload_files/others/"+filename, is);// �T�[�o�[��
        is.close();
    }
    finally
    {
        fp.disconnect();
    }
    return 0;
}
%>