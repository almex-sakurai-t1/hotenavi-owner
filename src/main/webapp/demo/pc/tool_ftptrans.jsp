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
    // セッションの確認
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
<title>スマホコンテンツ 送信</title>
<link href="lovehosearch.css" rel="stylesheet" type="text/css">
<link href="contents.css" rel="stylesheet" type="text/css">
</head>
<body class="honbun honbun_margin">
<form name="form" action="tool_ftptrans.jsp" method="post">
ホテナビID:<input type=text name=hotel_id value=<%=ftp_user%>>
<input name=check type="submit" value="check">
<input name=send type="submit"  value="send">
<input name=open type="submit"  value="open">
</form>
<%
    if (OpenFlag)
    {
        String  fileTime = fileCheck(jupiter_server,ftp_user,ftp_password,server_path+"index.jsp");
        if (fileTime.equals("ファイルがない") )
        {
%>
アップされていませんので、まずはアップからお願いします。<br>
<%
        }
        else
        {
            ftp_filename = "index.jsp";
            int ret = sendTrans(jupiter_server,ftp_user,ftp_password,client_path+"open/pc/",ftp_filename);
            if (ret == 0)
            {
%>
<%=jupiter_server%>にキャリア判別index.jspをアップしました。<br>
<%
            }
            else
            {
%>
<font color=red><%=jupiter_server%>にキャリア判別index.jspをアップできませんでした。</font><br>
<%
            }
            ret = sendTrans(neptune_server,ftp_user,ftp_password,client_path+"open/mobile/",ftp_filename);
            if (ret == 0)
            {
%>
<%=neptune_server%>にキャリア判別index.jspをアップしました。<br>
<%
            }
            else
            {
%>
<font color=red><%=neptune_server%>にキャリア判別index.jspをアップできませんでした。</font><br>
<%
            }
        }
    }
%>

<%
    if (CheckFlag)
    {
        String  fileTime = fileCheck(jupiter_server,ftp_user,ftp_password,server_path+"index.jsp");
        if (fileTime.equals("ファイルがない") )
        {
            if (fileMake(jupiter_server,ftp_user,ftp_password,"smart"))
            {
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/css");
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/error");
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/image");
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/js");
%>
アップされていないので、フォルダを作成しました。<br>
<%
            }
        }
        else if(fileTime.length() == 20)
        {
%>
<%=fileTime.substring(4,8)%>/<%=fileTime.substring(8,10)%>/<%=fileTime.substring(10,12)%>
<%=fileTime.substring(12,14)%>:<%=fileTime.substring(14,16)%>:<%=fileTime.substring(16,18)%>
にアップされています。
<%
        }
    }

    if (SendFlag)
    {
        String  fileTime = fileCheck(jupiter_server,ftp_user,ftp_password,server_path+"index.jsp");
        if (fileTime.equals("ファイルがない"))
        {
            if (fileMake(jupiter_server,ftp_user,ftp_password,"smart"))
            {
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/css");
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/error");
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/image");
                fileMake(jupiter_server,ftp_user,ftp_password,"smart/js");
%>
アップされていないので、フォルダを作成しました。<br>
<%
            }
            File checkFile = null;
            checkFile = new File(client_path + "image/logo.gif" );
            if (checkFile.delete())
            {
%>
前回受信した画像ファイルを削除しました。<br><br>
<%
            }
            else
            {
%>
<font color=red>前回受信した画像ファイルを削除できませんでした。</font><br>
<%
            }
            int ret = recieveTrans(neptune_server,ftp_user,ftp_password,client_path+"image/",neptune_path,"logo.gif");
            if (ret == 0)
            {
%>
携帯の画像ファイルを受信しました。<br>
<%
            }
            else
            {
%>
携帯の画像ファイルを受信できませんでした。<br>
<%
            }
%>
<img src=/_debug_/image/logo.gif><br>
ロゴが正しいかチェックしてください。<br>
<%
            FTPClient fp = new FTPClient();
            FileInputStream is = null;
            try
            {
                fp.connect(jupiter_server);
                if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) 
                { // コネクトできたか？
%>
<%=jupiter_server%>につながりませんでした<br>
<%
                }
                else
                {
%>
<%=jupiter_server%>につながりました<br>
<%
                    if (fp.login(ftp_user, ftp_password) == false)
                    { // ログインできたか？
%>
<%=ftp_user%>
ログインできません<br>
<%
                    }
                    else
                    {
%>
<%=ftp_user%>
ログインしました<br>
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
転送しました<br>
<%
                                }
                                else
                                {
%>
転送できませんでした<br>
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
転送しました<br>
<%
                        }
                        else
                        {
%>
<%=filename%>
転送できませんでした<br>
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
にファイルがアップされています。手動でアップしてください。
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
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // コネクトできたか？
            return 1;
        }
        if (fp.login(user, password) == false) { // ログインできたか？
            return 2;
        }
// ファイル送信
        is = new FileInputStream(path+filename);// クライアント側
        if(fp.storeFile(filename, is))// サーバー側
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
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // コネクトできたか？
            return 1;
        }
        if (fp.login(user, password) == false) { // ログインできたか？
            return 2;
        }
// ファイル受信
        os = new FileOutputStream(path+filename);// クライアント側
        if(fp.retrieveFile(server_path+filename, os))// サーバー側
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
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // コネクトできたか？
            return "つながらないっす";
        }
        else
            if (fp.login(user, password) == false) { // ログインできたか？
                return "ログインできません";
            }
            else
            {
                String getModificationTime = fp.getModificationTime(filename);
                if (getModificationTime == null)
                {
                    return "ファイルがない";
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
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // コネクトできたか？
            return false;
        }
        else
            if (fp.login(user, password) == false) { // ログインできたか？
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
