<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.util.*, org.apache.commons.fileupload.*" %>
<html>
<head><title>Jakarta Commonsを使ったファイルアップロードサンプル</title></head>
<body bgcolor="white">

<%
  /*
   * Jakarta Commonsのファイルアップロードを使用するには、
   * FileUploadパッケージとBeanUtilsパッケージが必要です。
   */
  String encoding = "SJIS";
  ServletFileUpload upload = new ServletFileUpload();
  upload.setSizeMax(100000);
  upload.setRepositoryPath("img");

  FileItemIterator iter = upload.getItemIterator(request);
  while(iter.hasNext()) {
    FileItem item = (FileItem) iter.next();
    long size = item.getSize();
    /* ↑クライアントに存在しないファイル名が */
    /* 入力された場合、0になります。 */
    String clientFileName = item.getName();
    String content = item.getString(encoding);
%>
<table border="1">
<tr>
  <td bgcolor="#cccccc">サイズ</td><td><%= size %></td>
  <td bgcolor="#cccccc">クライアント上のファイル名</td><td><%= clientFileName %></td>
</tr>
<%
    /* 1行ずつ切り出して処理します。*/
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