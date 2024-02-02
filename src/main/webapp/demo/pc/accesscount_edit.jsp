<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>

<%
    String query;

//アクセスカウント
    int    access_cnt         = 0;               //アクセスカウント
    int    access_today       = 0;               //本日のカウント
    int    access_prev        = 0;               //昨日のカウント
    int    last_update        = 0;               //最終更新日
    String contents_pc        = "<div style=\"font-family:Arial,Helvetica,'Courier New';font-size:10pt;line-height:150%;color:ffffff;\">\r\nVisitor Numbers:<strong>%TOTAL%</strong>\r\n</div><br>";
    String contents_mobile    = "Visitor Numbers:%TOTAL%";
    String mode               = "NEW";           //NEW:新規 UPD:更新
    String header_msg         = "";
    int    disp_flag          = 0;               //1:旧サイトに表示する

    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    query = "SELECT * FROM access_counter_sp WHERE hotel_id='" + hotelid + "'";
    prestate    = connection.prepareStatement(query);
    result      = prestate.executeQuery();

    if (result != null)
    {
        if( result.next() != false )
        {
            access_cnt             = result.getInt("access_cnt");
            access_today           = result.getInt("access_today");
            access_prev            = result.getInt("access_prev");
            last_update            = result.getInt("last_update");
            contents_pc            = result.getString("contents_pc");
            contents_mobile        = result.getString("contents_mobile");
            disp_flag              = result.getInt("disp_flag");
            header_msg = "更新";
            mode = "UPD";
        }
        else
        {
            header_msg = "新規作成";
            mode = "NEW";
        }
    }
    DBConnection.releaseResources(result,prestate,connection);

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>アクセスカウンタ設定</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
function MM_openInput(hotelid,mode){
  document.form1.target = '_self';
  document.form1.action = 'accesscount_input.jsp?HotelId='+hotelid+'&Mode='+mode;
  document.form1.submit();
}
</script>

</head>

<body bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF"><%= header_msg %></font></td>
              <td width="15" height="20"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="/common/pc/image/spacer.gif" width="200" height="20"></div>
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
              <td><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="/common/pc/image/spacer.gif" width="400" height="12">
              </td>
            </tr>
            <tr>
              <td width="8"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td colspan="2" class="size12"><font color="#CC0000"><strong>※このページを編集し終えたら、「<%= header_msg %>」ボタンを必ず押してください</strong></font></td>
                  </tr>
                  <form name=form1 method=post>
				  <tr align="left">
                      <td align="left" colspan="2" valign="middle" bgcolor="#969EAD">&nbsp;<input name="regsubmit" type=button value="<%= header_msg %>" onClick="MM_openInput('<%= hotelid %>', '<%= mode %>')"></td>
                   </tr>
                  <tr align="left">
                    <td width="150"><img src="/common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">トータルアクセス数</td>
					<td nowrap class="size12">
						<input name="access_cnt" type="text" size="10" value="<%= access_cnt %>" style="ime-mode:inactive;text-align:right">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">本日のアクセス数</td>
					<td nowrap class="size12">
						<input name="access_today" type="text" size="10" value="<%= access_today %>" style="ime-mode:inactive;text-align:right">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">昨日のアクセス数</td>
					<td nowrap class="size12">
						<input name="access_prev" type="text" size="10" value="<%= access_prev %>" style="ime-mode:inactive;text-align:right">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">最終更新日</td>
					<td nowrap class="size12">
						<%= last_update %>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">PC版表示箇所</td>
					<td nowrap class="size12">
						<textarea name="contents_pc" rows="5" cols="100" style="ime-mode:active"><%=contents_pc%></textarea>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">携帯版表示箇所</td>
					<td nowrap class="size12">
						<textarea name="contents_mobile" rows="3" cols="100" style="ime-mode:active"><%=contents_mobile%></textarea>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">表示</td>
					<td nowrap class="size12">
						<input type=checkbox name="disp_flag" value=1 <%if(disp_flag==1){%>checked<%}%>>旧ホテナビサイトに表示する<br>
						<small>※新ホテナビサイトはスタイルシートにて表示してください</small>
					</td>
                  </tr>
                  <tr align="left">
                    <td width="150"><img src="/common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12" ></td>
                    <td nowrap class="size12" >トータルアクセス数⇒%TOTAL%，本日のアクセス数⇒%TODAY%,昨日のアクセス数⇒%YESTERDAY%</td>
                  </tr>
                  <tr align="left">
                    <td width="150"><img src="/common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>
                </form>  
                </table>
              </td>
              </tr>
              <tr>
                <td valign="top">&nbsp;</td>
                <td valign="top"></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="new/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="/common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="/common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="/common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="/common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- ここまで -->
    </table></td>
  </tr>
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/owner/Library/footer.lbi" --><img src="/common/pc/image/imedia.gif" width="63" height="18"><img src="/common/pc/image/spacer.gif" width="12" height="18" align="absmiddle">Copyrigtht&copy; imedia
    inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="/common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
