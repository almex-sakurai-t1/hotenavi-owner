<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.util.*, org.apache.commons.fileupload.*" %>
<html>
<head><title>Jakarta Commons���g�����t�@�C���A�b�v���[�h�T���v��</title></head>
<body bgcolor="white">

<%
  /*
   * Jakarta Commons�̃t�@�C���A�b�v���[�h���g�p����ɂ́A
   * FileUpload�p�b�P�[�W��BeanUtils�p�b�P�[�W���K�v�ł��B
   */
  String encoding = "SJIS";
  ServletFileUpload upload = new ServletFileUpload();
  upload.setSizeMax(100000);
  upload.setRepositoryPath("img");

  FileItemIterator iter = upload.getItemIterator(request);
  while(iter.hasNext()) {
    FileItem item = (FileItem) iter.next();
    long size = item.getSize();
    /* ���N���C�A���g�ɑ��݂��Ȃ��t�@�C������ */
    /* ���͂��ꂽ�ꍇ�A0�ɂȂ�܂��B */
    String clientFileName = item.getName();
    String content = item.getString(encoding);
%>
<table border="1">
<tr>
  <td bgcolor="#cccccc">�T�C�Y</td><td><%= size %></td>
  <td bgcolor="#cccccc">�N���C�A���g��̃t�@�C����</td><td><%= clientFileName %></td>
</tr>
<%
    /* 1�s���؂�o���ď������܂��B*/
    StringTokenizer stok = new StringTokenizer(content, "\n");
    while(stok.hasMoreTokens()){
      String line = stok.nextToken();
%>
      <tr><td colspan="4"><%= line %></td></tr>
<%
    }
%>
</table>
<%
  }
%>
</body>
</html>