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
 * �R�s�[���̃p�X[srcPath]����A�R�s�[��̃p�X[destPath]��
 * �t�@�C���̃R�s�[���s���܂��B
 * �R�s�[�����ɂ�FileChannel#transferTo���\�b�h�𗘗p���܂��B
 * ���A�R�s�[�����I����A���́E�o�͂̃`���l�����N���[�Y���܂��B
 * @param srcPath    �R�s�[���̃p�X
 * @param destPath    �R�s�[��̃p�X
 * @throws IOException    ���炩�̓��o�͏�����O�����������ꍇ
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
