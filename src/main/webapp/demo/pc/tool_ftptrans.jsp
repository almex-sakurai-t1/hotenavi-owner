<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.util.StringTokenizer" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="org.apache.commons.net.ftp.FTPClient" %>
<%@ page import="org.apache.commons.net.ftp.FTPReply" %>
<%@ page import="jp.happyhotel.common.DBConnection" %>
<%@ page import="jp.happyhotel.common.Logging" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String hotel_id = (String)session.getAttribute("SelectHotel");
    if  (hotel_id.compareTo("all") == 0)
    {
        hotel_id    = "";
    }
    String jupiter_server   = "jupiter.hotenavi.com";
    String neptune_server   = "neptune.hotenavi.com";
    String ftp_user         = "";
    String ftp_password     = "";
    String client_path      = "/hotenavi/_debug_/";
    String neptune_path     = "i/image/";
    String server_path      = "smart/";
    String ftp_filename     = "";
    boolean CheckFlag = false;
    boolean SendFlag  = false;
    boolean OpenFlag  = false;

    String paramHotelId = request.getParameter("hotel_id");
    if    (paramHotelId== null) paramHotelId = hotel_id;
    ftp_user =  paramHotelId;

    String paramCheck = request.getParameter("check");
    if   (paramCheck!= null)
    {
        CheckFlag =  paramCheck.equals("check");
    }
    String paramSend = request.getParameter("send");
    if   (paramSend!= null)
    {
        SendFlag =  paramSend.equals("send");
    }
    String paramOpen = request.getParameter("open");
    if   (paramOpen!= null)
    {
        OpenFlag =  paramOpen.equals("open");
    }
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection        = DBConnection.getConnection();

    String  query = "SELECT * FROM hotel";
    query = query + " WHERE hotel_id = '" + ftp_user + "'";
    prestate = connection.prepareStatement(query);
    result   = prestate.executeQuery();
    if( result.next() != false)
    {
        ftp_user     = result.getString("hotel_id");
        ftp_password = result.getString("ftp_passwd");
    }
    DBConnection.releaseResources(result,prestate,connection);

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>�X�}�z�R���e���c ���M</title>
<link href="lovehosearch.css" rel="stylesheet" type="text/css">
<link href="contents.css" rel="stylesheet" type="text/css">
</head>
<body class="honbun honbun_margin">
<form name="form" action="tool_ftptrans.jsp" method="post">
�z�e�i�rID:<input type=text name=hotel_id value=<%=ftp_user%>>
<input name=check type="submit" value="check">
<input name=send type="submit"  value="send">
<input name=open type="submit"  value="open">
</form>
<%
    if (OpenFlag)
    {
        String  fileTime = fileCheck(jupiter_server,ftp_user,ftp_password,server_path+"index.jsp");
        if (fileTime.equals("�t�@�C�����Ȃ�") )
        {
%>
�A�b�v����Ă��܂���̂ŁA�܂��̓A�b�v���炨�肢���܂��B<br>
<%
        }
        else
        {
            ftp_filename = "index.jsp";
            int ret = sendTrans(jupiter_server,ftp_user,ftp_password,client_path+"open/pc/",ftp_filename);
            if (ret == 0)
            {
%>
<%=jupiter_server%>�ɃL�����A����index.jsp���A�b�v���܂����B<br>
<%
            }
            else
            {
%>
<font color=red><%=jupiter_server%>�ɃL�����A����index.jsp���A�b�v�ł��܂���ł����B</font><br>
<%
            }
            ret = sendTrans(neptune_server,ftp_user,ftp_password,client_path+"open/mobile/",ftp_filename);
            if (ret == 0)
            {
%>
<%=neptune_server%>�ɃL�����A����index.jsp���A�b�v���܂����B<br>
<%
            }
            else
            {
%>
<font color=red><%=neptune_server%>�ɃL�����A����index.jsp���A�b�v�ł��܂���ł����B</font><br>
<%
            }
        }
    }
%>

<%
    if (CheckFlag)
    {
        String  fileTime = fileCheck(jupiter_server,ftp_user,ftp_password,server_path+"index.jsp");
        if (fileTime.equals("�t�@�C�����Ȃ�") )
        {
            if (fileMake(jupiter_server,ftp_user,ftp_password,"smart"))
            {
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/css");
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/error");
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/image");
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/js");
%>
�A�b�v����Ă��Ȃ��̂ŁA�t�H���_���쐬���܂����B<br>
<%
            }
        }
        else if(fileTime.length() == 20)
        {
%>
<%=fileTime.substring(4,8)%>/<%=fileTime.substring(8,10)%>/<%=fileTime.substring(10,12)%>
<%=fileTime.substring(12,14)%>:<%=fileTime.substring(14,16)%>:<%=fileTime.substring(16,18)%>
�ɃA�b�v����Ă��܂��B
<%
        }
    }

    if (SendFlag)
    {
        String  fileTime = fileCheck(jupiter_server,ftp_user,ftp_password,server_path+"index.jsp");
        if (fileTime.equals("�t�@�C�����Ȃ�"))
        {
            if (fileMake(jupiter_server,ftp_user,ftp_password,"smart"))
            {
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/css");
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/error");
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/image");
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/js");
%>
�A�b�v����Ă��Ȃ��̂ŁA�t�H���_���쐬���܂����B<br>
<%
            }
            File checkFile = null;
            checkFile = new File(client_path + "image/logo.gif" );
            if (checkFile.delete())
            {
%>
�O���M�����摜�t�@�C�����폜���܂����B<br><br>
<%
            }
            else
            {
%>
<font color=red>�O���M�����摜�t�@�C�����폜�ł��܂���ł����B</font><br>
<%
            }
            int ret = recieveTrans(neptune_server,ftp_user,ftp_password,client_path+"image/",neptune_path,"logo.gif");
            if (ret == 0)
            {
%>
�g�т̉摜�t�@�C������M���܂����B<br>
<%
            }
            else
            {
%>
�g�т̉摜�t�@�C������M�ł��܂���ł����B<br>
<%
            }
%>
<img src=/_debug_/image/logo.gif><br>
���S�����������`�F�b�N���Ă��������B<br>
<%
            FTPClient fp = new FTPClient();
            FileInputStream is = null;
            try
            {
                fp.connect(jupiter_server);
                if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) 
                { // �R�l�N�g�ł������H
%>
<%=jupiter_server%>�ɂȂ���܂���ł���<br>
<%
                }
                else
                {
%>
<%=jupiter_server%>�ɂȂ���܂���<br>
<%
                    if (fp.login(ftp_user, ftp_password) == false)
                    { // ���O�C���ł������H
%>
<%=ftp_user%>
���O�C���ł��܂���<br>
<%
                    }
                    else
                    {
%>
<%=ftp_user%>
���O�C�����܂���<br>
<%
                        FileReader filereader = new FileReader(client_path+server_path+"contents.txt");
                        BufferedReader bufferedreader = new BufferedReader(filereader);
                        String line;
                        while((line = bufferedreader.readLine()) != null) 
                        {
                            StringTokenizer stringTokenizerTest = new StringTokenizer(line, ",");
                            while(stringTokenizerTest.hasMoreTokens())
                            { 
                                String filename = stringTokenizerTest.nextToken().toString();
%>
<%=filename%>
<%
                                is = new FileInputStream(client_path+server_path+filename);
                                if(fp.storeFile(server_path+filename, is))
                                {
%>
�]�����܂���<br>
<%
                                }
                                else
                                {
%>
�]���ł��܂���ł���<br>
<%
                                }
                            }
                        }
                        filereader.close();
                        String filename = "image/logo.gif";
                        is = new FileInputStream(client_path+filename);
                        if(fp.storeFile(server_path+filename, is))
                        {
%>
<%=filename%>
�]�����܂���<br>
<%
                        }
                        else
                        {
%>
<%=filename%>
�]���ł��܂���ł���<br>
<%
                        }
                    }
                }
            }
            finally
            {
                fp.disconnect();
            }
        }
        else if(fileTime.length() == 20)
        {
%>
<%=fileTime.substring(4,8)%>/<%=fileTime.substring(8,10)%>/<%=fileTime.substring(10,12)%>
<%=fileTime.substring(12,14)%>:<%=fileTime.substring(14,16)%>:<%=fileTime.substring(16,18)%>
�Ƀt�@�C�����A�b�v����Ă��܂��B�蓮�ŃA�b�v���Ă��������B
<%
        }
    }
%>
</body>
</html>
<%!
public static int sendTrans(String host,String user,String password,String path,String filename)
throws IOException {
    FTPClient fp = new FTPClient();
    FileInputStream is = null;
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
        is = new FileInputStream(path+filename);// �N���C�A���g��
        if(fp.storeFile(filename, is))// �T�[�o�[��
        {
           return 0;
        }
        else
        {
           return 3;
        }
    }
    finally
    {
        fp.disconnect();
    }

}
%>
<%!
public static int recieveTrans(String host,String user,String password,String path,String server_path,String filename)
throws IOException {
    FTPClient fp = new FTPClient();
    FileOutputStream os = null;
    
    try
    {
        fp.connect(host);
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // �R�l�N�g�ł������H
            return 1;
        }
        if (fp.login(user, password) == false) { // ���O�C���ł������H
            return 2;
        }
// �t�@�C����M
        os = new FileOutputStream(path+filename);// �N���C�A���g��
        if(fp.retrieveFile(server_path+filename, os))// �T�[�o�[��
        {
           return 0;
        }
        else
        {
           return 3;
        }
    }
    finally
    {
        fp.disconnect();
    }
}
%>
<%!
public static String fileCheck(String host,String user,String password,String filename)
throws IOException {
    FTPClient fp = new FTPClient();
    try
    {
        fp.connect(host);
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // �R�l�N�g�ł������H
            return "�Ȃ���Ȃ�����";
        }
        else
            if (fp.login(user, password) == false) { // ���O�C���ł������H
                return "���O�C���ł��܂���";
            }
            else
            {
                String getModificationTime = fp.getModificationTime(filename);
                if (getModificationTime == null)
                {
                    return "�t�@�C�����Ȃ�";
                }
                else
                {
                    return getModificationTime;
                }
            }
    }
    finally
    {
        fp.disconnect();
    }
}
%>
<%!
public static boolean fileMake(String host,String user,String password,String filename)
throws IOException {
    FTPClient fp = new FTPClient();
    try
    {
        fp.connect(host);
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // �R�l�N�g�ł������H
            return false;
        }
        else
            if (fp.login(user, password) == false) { // ���O�C���ł������H
                return false;
            }
            else
            {
                return fp.makeDirectory(filename);
            }
    }
    finally
    {
        fp.disconnect();
    }
}
%>
