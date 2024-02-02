<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/menu_ini.jsp" %>
<%@ include file="menu_config_convert_map.jsp" %>

<%
    // 入力ホテルID取得
    String hotelidinput  = request.getParameter("HotelIdInput");
    String datatypeinput = request.getParameter("DataTypeInput");
    String loginhotel = (String)session.getAttribute("LoginHotelId");
	
	//データ件数
	int seq  = Integer.parseInt( request.getParameter("seq"));
	
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    String query      = "";
	String error_query = "";
	
    int ret        = 0;
    String  id        = "";
    String  disp_idx  = "";
    String  title     = "";
    String  page_title  = "";
    String  contents  = "";
    int  page_only_flg  = 0;
    String  priority_index  = "";

	boolean error_flag = true;
	
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    for( int i = 1; i <= seq ; i++)
    {
        id        = request.getParameter("id_" + i);
        if (id == null) break;
        disp_idx  = request.getParameter("disp_idx_" + i);
        title     = request.getParameter("title_" + i);
        page_title = request.getParameter("page_title_" + i);
        contents  = request.getParameter("contents_" + i);
        page_only_flg = request.getParameter("page_only_flg_" + i) == null ? 0 : 1;
        priority_index = request.getParameter("priority_index_" + i);

        query = "SELECT * FROM menu_config WHERE  hotelid='" + hotelidinput + "' AND data_type=" + Integer.parseInt(datatypeinput) + " AND id=" + Integer.parseInt(id);
        prestate = connection.prepareStatement(query);
        result   = prestate.executeQuery();
        int hpedit_id = 0;
        int event_data_type = 0;
        if( result.next() != false )
        {
            hpedit_id       = result.getInt("hpedit_id");
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        if (hpedit_id == 0)
        {
            //スラッシュを省く
            String contents_tmp = contents.replace("/", "");
            if(contents_tmp.length() > 0)
            {
                if (map.containsKey( contents_tmp )){
                event_data_type = map.get( contents_tmp );
                }
            }
        }
        else
        {
            event_data_type = MenuNo[0][hpedit_id-1];
        }

        query = "UPDATE hotenavi.menu_config SET ";
        query = query + "disp_idx="     + Integer.parseInt(disp_idx)         + ", ";
        query = query + "title='"       + ReplaceString.SQLEscape(title)     + "', ";
        query = query + "page_title='"  + ReplaceString.SQLEscape(title)     + "', ";
        query = query + "contents='"    + ReplaceString.SQLEscape(contents)  + "', ";
        query = query + "event_data_type=" + event_data_type + ", ";
        query = query + "page_only_flg=" + page_only_flg + ", ";
        if (!priority_index.equals(""))
        {
        query = query + "priority_index=" + priority_index + ", ";
        }
        else
        {
        query = query + "priority_index=null, ";
        }
        query = query + "upd_hotelid='" + loginhotel + "', ";
        query = query + "upd_userid="   + ownerinfo.DbUserId + ", ";
        query = query + "last_update="  + nowdate + ", ";
        query = query + "last_uptime="  + nowtime + " ";
        query = query + " WHERE hotelid='" + hotelidinput + "' AND data_type=" + Integer.parseInt(datatypeinput) + " AND id=" + Integer.parseInt(id);
        prestate = connection.prepareStatement(query);
        ret = prestate.executeUpdate();
		if(ret < 0)
		{
			error_flag = false;
			error_query = query;
		}
		DBSync.publish(query);
		DBSync.publish(query,true);
        DBConnection.releaseResources(prestate);
    }
    DBConnection.releaseResources(connection);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>クーポン作成</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="/common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/coupon.js"></script>
</head>

<body bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="100" height="20" nowrap bgcolor="#22333F" class="tab">
                <font color="#FFFFFF">登録確認</font></td>
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
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
          </tr>
          <tr>
            <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="size12">
<%
    if(error_flag)
    {
%>
登録しました。<br>
<%
    }
    else
    {
%>
登録に失敗しました。<%=error_query%>
<%
    }

%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="size12"><form action="menu_config_list.jsp?HotelId=<%= hotelidinput %>&DataType=<%= datatypeinput %>" method="POST">
                      <INPUT name="submit_ret" type=submit value=戻る >
                    </form></td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td></td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
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
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyrigtht&copy; imedia
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
