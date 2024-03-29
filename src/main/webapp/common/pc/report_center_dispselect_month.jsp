<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    int report_flag    = 2;  //多店舗
%>
<%@ include file="../../common/pc/report_ini.jsp" %>
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
    // centeridの取得
    String centerid = "";
    DbAccess db_manage = new DbAccess();

    // 管理店舗数分ループ
    ResultSet DbManageHotel = ownerinfo.getManageHotel(db_manage, ownerinfo.HotelId, ownerinfo.DbUserId);
    boolean ret = DbManageHotel.first();
    if( ret != false )
    {
        centerid = DbManageHotel.getString("center_id");
        if( centerid.compareTo("") == 0 )
        {
            centerid = DbManageHotel.getString("hotel_id");
        }
    }

    db_manage.close();


    String requestUri = request.getRequestURI();
    if (requestUri.indexOf("/demo/") == 0)
    {
        year  = "2005";
        month = "03";
        centerid = "demo";
    }
    if (requestUri.indexOf("/_debug_/demo/") != -1)
    {
        loginhotel = hotelid;
        centerid   = hotelid;
    }

    type = 0;
    //設定データ読み込み
    query = "SELECT * FROM edit_event_info WHERE hotelid=?";
    query = query + " AND data_type=" + data_type;
    query = query + " AND (start_date <= '" + (target_date/100)*100+99 + "'";
    query = query + " AND end_date >= '"    + (target_date/100)*100 + "')";
    query = query + " ORDER BY id DESC";
    prestate    = connection.prepareStatement(query);
	prestate.setString(1, loginhotel);
    result      = prestate.executeQuery();
	try
	{
		if( result.next() )
		{
			ReportList[report_flag][type] = result.getString("msg1") + "\r\n";
		}
	}
	catch( Exception e )
	{
		Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
	}
	finally
	{
		DBConnection.releaseResources(result, prestate, connection);
	}
    String[]     Title_t  = new String[50];
    String       temp     = "";
    StringBuffer buf      = new StringBuffer();
    String       title    = ReportList[report_flag][type];
    int          i        = 0;
    int          seq      = 0;
    for( i = 0 ; i < title.length() ; i++ )
    {
        char c = title.charAt(i);
        switch( c )
        {
           case '\r':
               temp = buf.toString();
               temp = temp.replace("\r\n","");
               Title_t[seq] = temp;
               seq ++;
               buf = new StringBuffer();
               buf.append(c);
               break;
           default :
               buf.append(c);
               break;
        }
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>売上管理帳票ダウンロード</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/room.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/click_check.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="20">
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="140" height="20" bgcolor="#22333F" class="tab" nowrap><font color="#FFFFFF">多店舗帳票(月報)</font></td>
          <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
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
    <td align="center" valign="top" bgcolor="#D5D8CB">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="8" height="6"><img src="../../common/pc/image/spacer.gif" width="400" height="6"></td>
        </tr>
      </table>
        <table border="0" align="left" cellspacing="0" cellpadding="0">
          <tr>
            <td valign="top">
				<table border="0" cellspacing="0" cellpadding="1">
                <tr valign="top">
                  <td width="4"><img src="../../common/pc/image/spacer.gif" width="4" height="10"></td>
                  <td height="24">
				  	<table width="100%" height="32" border="0" cellpadding="0" cellspacing="0">
                      <tr valign="middle">
                        <td align="center" bgcolor="#000066"><div class="size12"><font color="#FFFFFF"><%= year %>年<%= month %>月　計上分</font></div>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="4" height="20"></td>
                  <td>
                      <table border="0" cellpadding="2" cellspacing="0" class="access">
                      <tr>
                        <td align="right" valign="middle" nowrap><div class="size12"><font class="space4">帳票を選択してください（PDF形式で表示）→</font></div>
                        </td>
						<form name=form action="../../common/pc/loader.html" target="mainFrame"></form>
                        <td align="left" valign="middle" class="size12" nowrap>
                              <select name="menu1" onChange="document.form.submit();MM_jumpMenu('parent.mainFrame',this,1)">
                              <option selected>選択してください</option>
<%
    for( i = 0 ; i < seq; i++ )
    {
%>
                             <% if(Title_t[i].length() > 1){%><option value="report_getpdf.jsp?fname=CENTER<%= year %><%= month %>99-1&ftype=sales&num=<%=i+1%>"><%=Title_t[i]%></option><%}%>
<%
    }
%>
                              </select>
                        </td>
                        </tr>
                        <tr>
                        <td colspan="2" nowrap>
                            <div class="size12"><font color=red>
                            ※PDF形式に事前に変換されていない場合には、都度変換しますので表示までに30秒以上かかることがあります。
                            </font>
                            </div>
                        </td>
                        </tr>
                    </table>
                  </td>
                </tr>
              </table>
                <img src="../../common/pc/image/spacer.gif" width="8" height="1"></td>
<%
if( DbUserSecurity.getInt("sec_level09") == 1)
{
%>
                <td>
                <table border="0" cellspacing="0" cellpadding="1">
                <tr valign="top">
                  <td width="4"><img src="../../common/pc/image/spacer.gif" width="4" height="10"></td>
                  <td height="24">
				  	<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
                      <tr valign="middle">
                        <td align="center"nowrap bgcolor="#000000"><div class="size12"><font color="#FFFFFF">&nbsp;帳票ダウンロード&nbsp;</font></div>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="4" height="20"></td>
                  <td>
                  <table width="100%" border="0" cellpadding="2" cellspacing="0" class="access">
					<tr>

                        <td align="center" valign="middle" nowrap class="size12"><a href="report_center_getfile.jsp?HotelId=<%= centerid %>&fname=CENTER<%= year %><%= month %>99-1.xls" target="mainFrame"><img src="../../common/pc/image/sales_d.gif" alt="帳票をダウンロード" width="65" border="0" align="absmiddle"></a>&nbsp;</td>

                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
                </td>
            <td valign="bottom" style="padding:10px" nowrap><div class="size12">
            ※ダウンロードボタンを押すと、<br>
            　エクセルファイルがダウンロードできます。</div></td>
<%
}
%>            
          </tr>
          <tr>
            <td><img src="../../common/pc/image/spacer.gif" width="400" height="4"></td>
          </tr>
        </table>
    </td>
    <td width="3" valign="top" align="left" height="100%">
      <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
        </tr>
        <tr>
          <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="20"></td>
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
</table>
</body>
</html>
<%
    db_sec.close();
%>
