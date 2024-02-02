<%@ page contentType="text/html;charset=Windows-31J"%>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // ﾊﾟﾗﾒｰﾀ取得
    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    int id =  0;
    String param_id =  request.getParameter("Id");
    if (param_id != null)
	{
	id =  Integer.parseInt(param_id);
	}


    String query;
    DbAccess  db = new DbAccess();
    String ng_word = "";
    String header_msg = "";

    if( id == 0 )
    {
        header_msg = " 新規作成";
    }
    else
    {
        query = "SELECT * FROM bbs_ngword WHERE hotel_id=?";
        query = query + " AND id=?";
        List<Object> list = new ArrayList<Object>();
        list.add(hotelid);
        list.add(id);
        // SQLクエリーの実行
        ResultSet result = db.execQuery(query,list);
        if( result.next() != false )
        {
            header_msg = " 更新";
            ng_word = result.getString("ng_word");
        }
    }
    db.close();
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>PC版イベント情報編集</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/room.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
<!--
function MM_openInput(input,hotelid,id){
  if( input == 'input' )
  {
    document.form1.target = '_self';
    document.form1.action = 'ngword_edit_input.jsp?HotelId='+hotelid+'&Id='+id;
  }
  document.form1.submit();
}
-->
</script>

</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF"> 編　集</font></td>
              <td width="15" height="20"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="12">
              </td>
            </tr>
            <tr>
              <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><font color="#CC0000"><strong>※編集し終えたら　「<%= header_msg %>　」　ボタンを押してください</strong></font></td>
                    <td align="right">&nbsp;</td>
                    <td align="right">&nbsp;</td>
                  </tr>
                  <tr>
                    <td><strong><%= header_msg %></strong></td>
                    <td align="right">&nbsp;</td>
                    <td width="20" align="right"><img src="../../common/pc/image/spacer.gif" width="20" height="12"></td>
                  </tr>
                </table>
              </div>
              </td>
            </tr>
            <tr>
              <td colspan="2"><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
            </tr>
          </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                <td>
                  <table width="100%" border="0" cellspacing="0" cellpadding="2">
                  <form name=form1 method=post>
                  <tr align="left">
                    <td height="24" bgcolor="#666666"><strong>&nbsp;<font color="#FFFFFF">NGワード</font></strong></td>
                  </tr>
                  <tr align="left">
                    <td height="8"><img src="../../common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td><input name="ng_word" type="text" size="100" value="<%= ng_word %>">
                    </td>
                  </tr>
                  <tr align="left">
                    <td><img src="../../common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="50%" align="center" valign="middle">
                          <input name="regsubmit" type=button value="<%= header_msg %>" onClick="MM_openInput('input', '<%= hotelid %>', <%= id %>)">
                          </form>
                          </td>
                          <td align="center" valign="middle" >
<%
    if( id != 0 )
{
%>

                          <form action="ngword_edit_delete.jsp?HotelId=<%= hotelid %>&Id=<%= id %>" method="POST">
                          <input name="submit_del" type=submit value="削除" >
                          </form>
<%
}
%>
                          </td>
                          <td align="center">
                          <form action="ngword_edit.jsp?HotelId=<%= hotelid %>" method="POST">
                          <input name="submit_ret" type=submit value="戻る" >
                           </form>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="new/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- ここまで -->
    </table></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/owner/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18"><img src="../../common/pc/image/spacer.gif" width="12" height="18" align="absmiddle">Copyrigtht&copy; almex
    inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
