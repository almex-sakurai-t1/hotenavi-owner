<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="jp.happyhotel.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<html STYLE="width: 238px; height: 187px"><head><title>画像表示</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<%
//MySql用
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    String query = "";

//　ホストとブラウザを取得
    String host = request.getRemoteHost();
    if (host==null)
    {
        host = "不明";
    }
    String userAgent = request.getHeader("User-Agent");

//ホテルID
    String loginHotelId  = (String)session.getAttribute("LoginHotelId");
    if( loginHotelId == null )
    {
        loginHotelId = "demo";
    }
    String hotelid       = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
        hotelid = "demo";
    }
    NumberFormat  nf2;
    nf2     = new DecimalFormat("00");

// imedia_user のチェック
    int imedia_user = 0;
    query = "SELECT * FROM owner_user WHERE hotelid=?";
    query = query + " AND userid=?";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1, loginHotelId);
    prestate.setInt(2, ownerinfo.DbUserId);
    result      = prestate.executeQuery();
    if( result.next() )
    {
        imedia_user = result.getInt("imedia_user");
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);
%>

<script language="javascript">

function _CloseOnEsc() {
  if (event.keyCode == 27) { window.close(); return; }
}

function Init() {                                                       // run on page load
  document.body.onkeypress = _CloseOnEsc;
//  document.linkform.IMG.value = opener.document.form1.imglink.value;
}

function Set() {
  if (document.linkform.IMG.value != "")
  {
    opener.document.form1.imglink.value = document.linkform.IMG.value;
    var resultform1 ="<img src='" + document.linkform.IMG.value + "' alt='";
    var resultform2 ="'";
    if  (document.linkform.IMGWIDTH.value != "")
    {
      resultform2 = resultform2 + " width='" + document.linkform.IMGWIDTH.value + "'";
    }
    if  (document.linkform.IMGFLOAT[1].checked)
    {
      resultform2 = resultform2 + " style='clear:both;float:left;'";
    }
    if  (document.linkform.IMGFLOAT[3].checked)
    {
      resultform2 = resultform2 + " style='clear:both;float:right;'";
    }

    resultform2 = resultform2 + " border='0'>";

    if  (document.linkform.IMGFLOAT[2].checked)
    {
      resultform1 = "<center>" + resultform1;
      resultform2 = resultform2 + "</center>";
    }

    if (document.linkform.ALT.value == "" || document.linkform.ALT.value == "無題")
    {
      window.opener.enclose(resultform1, resultform2);
    }
    else
    {
      window.opener.enclose(resultform1 + document.linkform.ALT.value + resultform2,'')
    }
    window.close();
  }
}

</script>
</head>
<body bgcolor="buttonface" topmargin=0 leftmargin=0 onload="Init();">
<form name="linkform" method=get onSubmit="Set(); return false;">
<table border=0 cellspacing=0 cellpadding=4>
<tr>
	<td bgcolor="buttonface" valign=center class=size12 style="padding-left:10px"><br/>表示画像（.jpg .gif .png のみ）<br/>
		<input type="text" name="IMG" value="" size=80 style="font-size: 12px">
		<input type="hidden" name="ALT" value="">
	</td>
	<td valign=center class=size12 nowrap style="padding-left:10px"><br/>サイズ（横幅）<br/>
		<input type="text" name="IMGWIDTH" value="" size=3 style="font-size: 12px">
	</td>
</tr>
<tr>
	<td bgcolor="buttonface" valign=center class=size10 colspan="2" style="padding-left:10px">
		<small>表示されている画像をクリックすると画像のURLがセットされます。</small>
	</td>
</tr>
<tr>
	<td bgcolor="buttonface" valign=top class=size12 colspan="2" style="padding-left:10px">
		配置箇所：
		<input name="IMGFLOAT" type="radio" checked>指定なし
		<input name="IMGFLOAT" type="radio">左側
		<input name="IMGFLOAT" type="radio">中央
		<input name="IMGFLOAT" type="radio">右側
		<input type="button" onclick="Set();return false;" value="表示">
	</td>
</tr>
</table>
</form>
<hr size=1>
<table border=0 cellspacing=0 cellpadding=4 style="padding-left:10px">
	<tr>
<%
    //投稿済み画像件数を取得
        int count = 0;
        int PAGE_MAX = 5;
        int start,end;
        String strStart = request.getParameter("start");
        if (strStart==null)
        {
            strStart = "1";
        }
        start = Integer.parseInt(strStart);
        end   = start + PAGE_MAX -1;

        String  query_sub = "";

        if (imedia_user != 1)
        {
            query_sub = query_sub + " upload_files,owner_user_hotel";
            query_sub = query_sub + " WHERE owner_user_hotel.hotelid ='" + loginHotelId + "'";
            query_sub = query_sub + " AND owner_user_hotel.userid=" + ownerinfo.DbUserId;
            query_sub = query_sub + " AND upload_files.hotel_id =?";
            query_sub = query_sub + " AND upload_files.target_hotelid = owner_user_hotel.accept_hotelid";
            query_sub = query_sub + " AND upload_files.imedia_user=0";
            if (loginHotelId.equals("demo"))
            {
                query_sub = query_sub + " AND upload_files.user_agent = '" + userAgent + "'";
                query_sub = query_sub + " AND upload_files.host = '" + host + "'";
            }
        }
        else
        {
            query_sub = query_sub + " upload_files";
            query_sub = query_sub + " WHERE (upload_files.hotel_id='" + loginHotelId + "'";
            query_sub = query_sub + " OR  upload_files.target_hotelid = ?)";
        }
        query_sub = query_sub + " AND upload_files.data_type=0";
        query_sub = query_sub + " AND upload_files.del_flag=0";

        query = "SELECT count(*) FROM";
        query = query + query_sub;
        prestate = connection.prepareStatement(query);
        if (imedia_user != 1)
        {
                prestate.setString(1,loginHotelId);
        }
        else
        {
                prestate.setString(1,hotelid);
        }
        result   = prestate.executeQuery();
        if( result.next() )
        {
            count = result.getInt(1);
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

        query = "SELECT * FROM";
        query = query + query_sub;
        query = query + " ORDER BY upload_files.add_date DESC,upload_files.add_time DESC";
        query = query + " LIMIT " + (start-1) + "," + PAGE_MAX;
        prestate = connection.prepareStatement(query);
        if (imedia_user != 1)
        {
                prestate.setString(1,loginHotelId);
        }
        else
        {
                prestate.setString(1,hotelid);
        }
        result   = prestate.executeQuery();
        int        id_image = 0;
        while( result.next() )
        {
            id_image = result.getInt("upload_files.id");
        // 題名
            String subject    = result.getString("upload_files.subject");
        // 投稿日付
            String date_image = (result.getInt("upload_files.add_date") / 10000) + "/" + nf2.format(result.getInt("upload_files.add_date") / 100 % 100) + "/" + nf2.format(result.getInt("upload_files.add_date") % 100) + " " + nf2.format(result.getInt("upload_files.add_time") / 10000) + ":" + nf2.format(result.getInt("upload_files.add_time") / 100 % 100) + ":" + nf2.format(result.getInt("upload_files.add_time") % 100);
%>
		<td class="honbun" style="font-size:8px">
			<img onclick="document.linkform.IMG.value='https://owner.hotenavi.com/upload_files/image/<%=result.getString("filename")%>';document.linkform.IMGWIDTH.value='<%=result.getString("imgwidth")%>';document.linkform.ALT.value='<%=result.getString("subject")%>'" src="/upload_files/image/<%=result.getString("filename")%>" border="0" <%if (Integer.parseInt(result.getString("imgwidth"))<100){%><%=result.getString("imgwidth")%><%}else{%>width="100"<%}%>><br>
			<small><%=result.getString("subject")%></small><br><%=result.getString("imgwidth")%>px
		</td>
<%
        }
        DBConnection.releaseResources(result,prestate,connection);
        if (count ==0)
        {
%>
		<td class="honbun" style="font-size:8px">
			<small>画像が投稿されていません。</small>
		</td>
<%
        }
%>
	</tr>
</table>
<hr size=1>
<table border=0 cellspacing=0 cellpadding=4 >
	<tr>
		<td>
		<table width="100%" border="0" cellpadding="5" cellspacing="0">
			<tr>
				<td  class="honbun">
<%
        int i = PAGE_MAX;
        boolean page_move = false;
        if (start - PAGE_MAX >= 1)
        {
            out.println("<input type=button value=\"前ページ\" onClick=\"location.href='event_edit_img.jsp?start=" + (start - PAGE_MAX) + "'\"> ");
        }
        if (start + PAGE_MAX <= count)
        {
            out.println("<input type=button value=\"次ページ\" onClick=\"location.href='event_edit_img.jsp?start=" + (start + PAGE_MAX) + "'\"> ");
        }
        if (count > i)
        {
            out.print("<br><br>");
            page_move = true;
        }
        while (count > i - PAGE_MAX)
        {
            if (i - PAGE_MAX + 1==start)
            {
                if (page_move)
                {
                    out.print("[<b>" + (i/PAGE_MAX) + "</b>] ");
                }
            }
            else
            {
                out.print("[<a href=\"event_edit_img.jsp?start=" + (i - PAGE_MAX +1) +"\" class=\"link1\">" + (i/PAGE_MAX) + "</a>] ");
            }
            i+=PAGE_MAX;
        }
%>
				</td>
			</tr>
		</table>
	  </td>
	</tr>
</table>
</body></html>