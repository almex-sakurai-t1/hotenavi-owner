<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="java.nio.channels.FileChannel" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
            copyTransfer("/hotenavi/demo/pc/roomdisp_timechart.jsp","/hotenavi/demo/pc/aaa.jsp");
%>
<%!
/**
 * コピー元のパス[srcPath]から、コピー先のパス[destPath]へ
 * ファイルのコピーを行います。
 * コピー処理にはFileChannel#transferToメソッドを利用します。
 * 尚、コピー処理終了後、入力・出力のチャネルをクローズします。
 * @param srcPath    コピー元のパス
 * @param destPath    コピー先のパス
 * @throws IOException    何らかの入出力処理例外が発生した場合
 */
public static void copyTransfer(String srcPath, String destPath) 
    throws IOException {
    
    FileChannel srcChannel = new
        FileInputStream(srcPath).getChannel();
    FileChannel destChannel = new
        FileOutputStream(destPath).getChannel();
    try {
        srcChannel.transferTo(0, srcChannel.size(), destChannel);
    } finally {
        srcChannel.close();
        destChannel.close();
    }

}
%>
