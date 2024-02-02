<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="java.text.*" %><%@ page import="java.sql.*" %><%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %><%@ page import="jp.happyhotel.common.HotelElement" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%@ include file="../../common/pc/menu_ini.jsp" %>
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
    String requestUri = request.getRequestURI();
    boolean DebugMode = false;
    if (requestUri.indexOf("_debug_/demo/") != -1)
    {
       DebugMode = true;
    }

    NumberFormat nf2         = new DecimalFormat("00");
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");

    String Type      = ReplaceString.getParameter(request,"Type");
    if    (Type     == null)
    {
           Type      = "pc";
    }
    if( !CheckString.alphabetCheck(Type) )
    {
        Type="";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
    }

    String disp_type = ReplaceString.getParameter(request,"DispType");
    if  (disp_type == null)
    {
         disp_type = "0";
    }
    disp_type = ReplaceString.HTMLEscape(disp_type);    //20100910追加

    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
%>
        <jsp:forward page="error/error.html" />
<%
    }
    if  (hotelid.compareTo("all") == 0)
    {
         hotelid = ReplaceString.getParameter(request,"HotelId");
		if( hotelid != null && !CheckString.hotenaviIdCheck(hotelid))
		{
			hotelid = "0";
%>
			<script type="text/javascript">
			<!--
			var dd = new Date();
			setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
			//-->
			</script>
<%
		}
    }
    if (hotelid != null)
    {
        hotelid = ReplaceString.HTMLEscape(hotelid);
    }

    String smart_flg = ReplaceString.getParameter(request,"SmartFlg"); //20131111追加
    if  (smart_flg == null)
    {
         smart_flg = "0";
    }
    smart_flg = ReplaceString.HTMLEscape(smart_flg);

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String            query="";
    //現在日時の取得
    DateEdit  de = new DateEdit();
    int    now_date        = Integer.parseInt(de.getDate(2));
    int    now_time        = Integer.parseInt(de.getTime(1));
    int trialDate = HotelElement.getTrialDate(hotelid);
    int startDate = HotelElement.getStartDate(hotelid);

    String data_type = ReplaceString.getParameter(request,"DataType");
    if    (data_type == null)
    {
        if(Type.equals("pc") && now_date >= startDate){ //リニューアル済みの場合
            data_type="1";
	    }
        else
        {
            if (Type.compareTo("pc") == 0)
            {
                data_type = "5";
            }
            else
            {
                data_type = "6";
            }
         }
    }
    data_type = ReplaceString.HTMLEscape(data_type);    //20100910追加
    int eventDataType = Integer.parseInt(data_type);

    String titleName = "";

    connection  = DBConnection.getConnection();
    // HP編集メニュー名称の書き換え
    try
    {
        query = "SELECT * FROM menu WHERE hotelid=?";
        query = query + " AND data_type=" + (((Integer.parseInt(data_type)+1) % 2)*2+1);
        query = query + " AND start_date <=" + now_date;
        query = query + " AND end_date >=" + now_date;
        query = query + " AND disp_flg =1";
        query = query + " AND hpedit_id=" + (MenuDataType[(Integer.parseInt(data_type)+1)/2]+1);
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            Title[(Integer.parseInt(data_type)+1) % 2][MenuDataType[(Integer.parseInt(data_type)+1)/2]]=result.getString("title");
            titleName = result.getString("title");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    if(Type.equals("pc") && now_date >= trialDate){ //リニューアル済みの場合
        try
        {
            query = "SELECT * FROM menu_config WHERE hotelid=?";
            query = query + " AND data_type IN (1,2)";
            if (eventDataType == 15)
            {
                query = query + " AND contents = 'service'";
            }
            else
            {
                query = query + " AND event_data_type =?";
            }
            query = query + " ORDER BY data_type,disp_idx";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,hotelid);
            if (eventDataType != 15)
            {
                prestate.setInt(2,eventDataType);
            }
            result      = prestate.executeQuery();
            if( result.next() )
            {
                titleName = result.getString("title");
                Title[(Integer.parseInt(data_type)+1) % 2][MenuDataType[(Integer.parseInt(data_type)+1)/2]]= result.getString("title");
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
    }


    String image_hotelid = "demo";

    //  店舗のチェック
    try
    {
        query = "SELECT * FROM hotel WHERE hotel.hotel_id =?";
        query = query + " AND hotel.plan <= 4";
        query = query + " AND hotel.plan >= 1";
        query = query + " AND hotel.plan != 2";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            image_hotelid = result.getString("hotel_id");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    int imedia_user = 0;
    // imedia_user のチェック
    try
    {
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
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>イベント情報</title>
<% if (loginHotelId.compareTo("hotenavi") != 0){%>
<link href="../../<%= image_hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<%}%>
<link href="http://www.hotenavi.com/<%= image_hotelid %>/contents.css" rel="stylesheet" type="text/css">
</head>

<script type="text/javascript">
<!--
function disp_find(id) {
	if(document.getElementById(id).style.display == "none"){
		document.getElementById(id).style.display = "table-row";
	} else {
		document.getElementById(id).style.display = "none";
	}
}
-->
</script>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

	<tr valign="top">
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%
    if (DebugMode)
    {
        if (ReplaceString.getParameter(request,"attentionChange") != null && ReplaceString.getParameter(request,"attentionChange").equals("true"))
        {
            query = "UPDATE hotel_element SET attention=? WHERE hotel_id=?";
            prestate = connection.prepareStatement(query);
            prestate.setString(1, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"attention")) );
            prestate.setString(2,hotelid);
            int element_upd = prestate.executeUpdate();
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
            if (element_upd != 1)
            {
                query = "INSERT hotel_element SET html_head='<script>location.href=\"https://happyhotel.jp\"</script>',html_login_form='',attention=?,hotel_id=?";
                prestate = connection.prepareStatement(query);
                prestate.setString(1, ReplaceString.SQLEscape(ReplaceString.getParameter(request,"attention")) );
                prestate.setString(2,hotelid);
                prestate.executeUpdate();
                DBConnection.releaseResources(result);
                DBConnection.releaseResources(prestate);
            }
        }
        String attention = "";
        try
        {
            query = "SELECT attention FROM hotel_element WHERE hotel_id=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,hotelid);
            result      = prestate.executeQuery();
            if( result.next() )
            {
                if ( result.getString("attention") != null)
                attention = result.getString("attention");
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
%>				<tr>
					<td align="left" valign="middle" style="width:150px;font-size:20px;padding:3px;border:1px solid #ffffff;background-color:#FF0000;color:#ffffff;">
						<%= hotelid %>
					</td>
					<td colspan=2>
					<div id="attentionArea" style="background-color:#FF9999;color:#000000;line-height:20px;height:20px;overflow:hidden;">
					<form action="event_list.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&DispType=1&SmartFlg=<%=smart_flg%>" method=POST>
					<input type="hidden" name="attentionChange" value="true">
					<div id="dispArea" onclick="document.getElementById('attentionArea').style.height='';">
					<%=attention.replace("\r\n","<br>")%>
					<input type=button value="注意事項 編集" id="hensyu" style="margin-left:15px" onclick="this.style.display='none';
					document.getElementById('dispArea').style.display='none';
					document.getElementById('sbmit').style.display='inline';
					document.getElementById('close').style.display='inline';
					document.getElementById('attention').style.display='inline';">
					</div>
					<div style="float:left;">
					<textarea name="attention" id="attention" style="display:none;width:500px;height:54px;" onkeyup="
					var areaH = this.style.height;
					areaH = parseInt(areaH) - 54;
					if(areaH < 30){ areaH = 30; }
					this.style.height = areaH + 'px';
					this.style.height = parseInt(this.scrollHeight + 30) + 'px';
					"><%=attention%></textarea>
					</div>
					<div id="buttonArea">
					<input type=submit value="保存" id="sbmit" style="display:none;">
					<input type=button value="×" id="close" style="display:none;width:20px" onclick="
					this.style.display='none';
					document.getElementById('hensyu').style.display='inline';
					document.getElementById('sbmit').style.display='none';
					document.getElementById('attention').style.display='none';
					document.getElementById('dispArea').style.display='inline';
					document.getElementById('attentionArea').style.height='20px';
					">
					</div>
					</form>
					</div>
					</td>
				</tr>
<%
    }
%>
				<tr>
					<td align="left" valign="middle" style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="3">
						<strong><%= Title[(Integer.parseInt(data_type)+1) % 2][MenuDataType[(Integer.parseInt(data_type)+1)/2]] %></strong>
						<%if(Integer.parseInt(data_type)%2 == 0){%>(携帯版）<%}else{%>（PC版）
						<select name="smart_flg" onchange="location.href='event_list.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&DispType=<%=disp_type%>&SmartFlg=' + this.value;">
							<option value=0 <%if (smart_flg.equals("0")){%>selected<%}%>>
							<option value=1 <%if (smart_flg.equals("1")){%>selected<%}%>>スマホ
							<option value=2 <%if (smart_flg.equals("2")){%>selected<%}%>>PC
						</select>
						<%}%>
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="3"></td>
				</tr>
				<tr>
					<td>
						<form action="event_edit_form.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=0" method=POST>
						<input name="submit00" type=submit value="新しくトピックを追加" >
					</td></form>
<%
    if (disp_type.compareTo("0") == 0)
    {
%>
					<td>
					<form action="event_list.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&DispType=1&SmartFlg=<%=smart_flg%>" method=POST>
						<input name="submit01" type=submit value="非表示分も表示する" >
					</td></form>
<%
    }
%>
					<td>
						<form action="event_order_change.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&DispType=<%= disp_type %>&SmartFlg=<%=smart_flg%>" method=POST>
						<input name="submit02" type=submit value="表示順番変更" >
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="3"></td>
				</tr>

<%
    Connection        connection_user  = null;
    PreparedStatement prestate_user    = null;
    ResultSet         result_user      = null;
    String        query_user;
    java.sql.Date start;
    java.sql.Date end;
    int yy;
    int mm;
    int dd;
    int start_date;
    int end_date;
    int start_time;
    int end_time;

    int event_id;
    int last_update;
    int last_uptime;
    int i;
    int count = 0;
    String user_name = "";
    String msg_data;
    String msg_title;
    String msg_title_color;

    query = "SELECT * FROM edit_event_info WHERE hotelid=?";
    query = query + " AND data_type=?";
    if (disp_type.compareTo("1") != 0)
    {
        if (trialDate > now_date || disp_type.compareTo("5")!=0 )
        query = query + " AND (end_date>"   +now_date + " OR (end_date="   + now_date + " AND end_time   >=" + now_time + ") OR (end_date=" + now_date + " AND end_time=0))";
        query = query + " AND disp_flg >=1";
    }
    if (smart_flg.compareTo("0") != 0)
    {
        query = query + " AND smart_flg IN (0,"+smart_flg+")";
    }
    query = query + " ORDER BY disp_idx";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1,hotelid);
    prestate.setInt(2,Integer.parseInt(data_type));
    result      = prestate.executeQuery();
    if (result != null)
    {
        while( result.next() != false )
        {
            if( now_date>=trialDate && Type.equals("pc") && result.getInt("disp_idx") <= -9990 && imedia_user==0)
            {

            }
            else
            {
            event_id =  result.getInt("id");
            // 時間判定の準備
            start = result.getDate("start_date");
            yy = start.getYear();
            mm = start.getMonth();
            dd = start.getDate();
            start_date = (yy+1900)*10000 + (mm+1)*100 + dd;
            end = result.getDate("end_date");
            yy = end.getYear();
            mm = end.getMonth();
            dd = end.getDate();
            end_date = (yy+1900)*10000 + (mm+1)*100 + dd;
            start_time =  result.getInt("start_time");
            end_time   =  result.getInt("end_time");

            count++;
            // ユーザ一覧・セキュリティ情報取得
            connection_user  = DBConnection.getConnection();
            query_user = "SELECT * FROM owner_user WHERE hotelid=?";
            query_user = query_user + " AND userid=?";
            prestate_user = connection_user.prepareStatement(query_user);
            prestate_user.setString(1,result.getString("upd_hotelid") );
            prestate_user.setInt(2,result.getInt("upd_userid"));
            result_user   = prestate_user.executeQuery();
            if( result_user != null )
            {
                if (result_user.next() != false )
                {
                    user_name = result_user.getString("name");
                }
                else
                {
                    user_name = "不明";
                }
            }
            DBConnection.releaseResources(result_user,prestate_user,connection_user);
%>

				<tr>
					<td  colspan="3">
						<!-- ############### サブタイトルバー ############### -->
<%
            if (Integer.parseInt(data_type) % 2 == 0)
            { //携帯版
%>
						<table cellspacing="0" bgcolor="#ffffff"  border="1" cellpadding="0" bordercolor="#666666" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor" onclick="disp_find('honbun_<%= hotelid %>_<%= event_id %>')" ><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3" onclick="disp_find('honbun_<%= hotelid %>_<%= event_id %>')"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor" onclick="disp_find('honbun_<%= hotelid %>_<%= event_id %>')"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="400" onclick="disp_find('honbun_<%= hotelid %>_<%= event_id %>')">
									<div class="subtitle_text" <%if (result.getString("title_color").equals("")){%>style="background-color:#FFFFFF;color:#000000"<%}else{%>style="background-color:#FFFFFF;color:<%=result.getString("title_color")%>"<%}%>><%= result.getString("title") %></div>
								</td>
<%
            }
            else
            { //PC版
%>
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="400">
									<div class="subtitle_text"><font color="<%= result.getString("title_color") %>"><%= result.getString("title") %></font></div>
								</td>
<%
            }
%>
								<td align=left title="作成:<%=  result.getString("add_date") %>&nbsp;<%=  result.getString("add_time") %>&nbsp;<%=  result.getString("add_hotelid") %>&nbsp;<%=  result.getString("add_userid") %>
編集:<%= user_name %>&nbsp;<% if (user_name.compareTo("不明") != 0) { %><%=  result.getString("last_update") %>&nbsp;<%=  result.getString("last_uptime") %>&nbsp;<%=  result.getString("upd_hotelid") %>&nbsp;<%=  result.getString("upd_userid") %><% } %>"  class="subtitle_text" <%if (Integer.parseInt(data_type) % 2 != 0){%>style="color:<%=result.getString("title_color")%>"<%}else if(result.getString("title_color").equals("")){%>style="background-color:#FFFFFF;color:#000000"<%}else{%>style="background-color:#FFFFFF;color:<%=result.getString("title_color")%>"<%}%>>
<%
            if(!DebugMode && result.getInt("disp_idx") == -9999)
            {
%>
								<input name="DispIdx<%= count %>" type="hidden" value="<%= result.getInt("disp_idx") %>"><input name="Id<%= count %>" type=hidden value="<%= result.getInt("id") %>">
<%
            }
            else
            {
%>
								表示順番:<input name="DispIdx<%= count %>" size="3" <% if (!DebugMode){%>maxlength="4"<%}%> value="<%= result.getInt("disp_idx") %>" style="ime-mode:disableed;text-align:right;"  onchange="if (this.value<-999 <%if(DebugMode){%>&& this.value!=-9999<%}%>){this.value=-999}"><input name="Id<%= count %>" type=hidden value="<%= result.getInt("id") %>">
<%
            }
%>
								<a href="event_edit_form.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=<%= result.getInt("id") %>"><input type="button" value="編集" class="size12" onclick="location.href='event_edit_form.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=<%= result.getInt("id") %>'"></a>
								<a href="event_copy.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=<%= result.getInt("id") %>"><input type="button" value="COPY" class="size12" onclick="location.href='event_copy.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=<%= result.getInt("id") %>'"></a>
<%
            if( result.getInt("member_only") == 1 )
            {
%>
								<b>メンバーのみ</b>&nbsp;
<%
            }
%>
<%
            if( result.getInt("member_only") == 2 )
            {
%>
								<b>ビジターのみ</b>&nbsp;
<%
            }
%>
			<%if(result.getInt("smart_flg")==1){%><b>【スマホのみ】</b><%}%><%if(result.getInt("smart_flg")==2){%><b>【PCのみ】</b><%}%>

								<br>期間:<%= start_date/10000 %>年<%= start_date/100%100 %>月<%= start_date%100 %>日<%if(start_time!=0){%><%=nf2.format(start_time/10000)%>:<%=nf2.format(start_time/100%100)%><%}%>～<%= end_date/10000 %>年<%= end_date/100%100 %>月<%= end_date%100 %>日<%if(end_time!=0&& end_time!=235959){%><%=nf2.format(end_time/10000)%>:<%=nf2.format(end_time/100%100)%><%}%>&nbsp;
<%
            if( now_date < start_date || now_date == start_date && now_time < start_time || now_date > end_date || now_date == end_date && now_time > end_time && end_time != 0 || result.getInt("disp_flg") != 1)
            {
%>
								<br><b>表示していません。
<%
                if( now_date < start_date || now_date == start_date && now_time < start_time || now_date > end_date || now_date == end_date && now_time > end_time && end_time != 0)
                {
%>

								&nbsp;表示期間チェック！
<%
                }
                if(result.getInt("disp_flg") == 2)
                {
%>

								&nbsp;（下書保存）
<%
                }
%>
								</b>
<%
            }
%>
								</td>
							</tr>
						</table>
						<!-- ############### サブタイトルバー ここまで ############### -->
					</td>
				</tr>
<%
            if (Integer.parseInt(data_type) % 2 == 0)
            {
%>
				<tr id="honbun_<%= hotelid %>_<%= event_id %>" style="display:none;">
					<td colspan="3">
						<table width="166" bgcolor="#FFFFFF" border="0" cellpadding="0" cellspacing="0" bordercolor="#666666">
						<tr>
							<td width="160" nowrap>
<%
                for( i = 1 ; i <= 8 ; i++ )
                {
                    msg_data  = "msg" + Integer.toString(i);
                    msg_data  = msg_data.replace("<iframe","<span");
                    msg_data  = msg_data.replace("</iframe","</span");
                    msg_title = "msg" + Integer.toString(i) + "_title";
                    msg_title_color = "msg" + Integer.toString(i) + "_title_color";
                    if( result.getString(msg_title).length() != 0 || result.getString(msg_data).length() > 1 )
                    {
%>
								<font color="<%= result.getString(msg_title_color) %>"><%= result.getString(msg_title) %></font><br>
								<font color="#000000"><%=  result.getString(msg_data) %></font><br>
<%
                     }
                 }
%>
				</td></tr></table></td></tr>
<%
            }
            else
            {
                for( i = 1 ; i <= 8 ; i++ )
                {
                    msg_data  = "msg" + Integer.toString(i);
                    msg_data  = msg_data.replace("<iframe","<span");
                    msg_data  = msg_data.replace("</iframe","</span");
                    msg_title = "msg" + Integer.toString(i) + "_title";
                    msg_title_color = "msg" + Integer.toString(i) + "_title_color";
                    if( result.getString(msg_title).length() != 0 || result.getString(msg_data).length() > 1 )
                    {
%>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>
<%
                        if( result.getString(msg_title).length() != 0 )
                        {
%>
				<tr>
					<td align="left" valign="top"  colspan="3">
						<!-- ############### 本文タイトルバー ############### -->
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="honbun_margin">
							<tr>
								<td width="4" class="honbuntitlebar_left"><img src="../../common/pc/image/spacer.gif" width="4" height="16"></td>
								<td class="honbuntatitlebar_text" style="color:<%= result.getString(msg_title_color) %>"><img src="../../common/pc/image/spacer.gif" width="6" height="16" border="0" align="absmiddle"><%= result.getString(msg_title) %></td>
							</tr>
						</table>
						<!-- ############### 本文タイトルバー ここまで ############### -->
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>
<%
                        }
%>
				<tr>
					<td align="left" valign="top" class="honbun"  colspan="3">

<%
                        String firstString  = result.getString(msg_data);
                        String taisyoString = "src=\"image/";
                        String henkanString = "src=\"http://www.hotenavi.com/" + hotelid +  "/image/";
                        String afterString = "";
                        //対象文字列を判断しCRLFなどの改行コードを考慮してインプリメントするポインタ数を制御する。
                        int plusPoint = taisyoString.length();
                        int startPoint = 0;
                        int endPoint   = firstString.indexOf(taisyoString, startPoint);
                        //文字列に対象文字列がない場合、そのままの文字列を戻す
                        //文字列に対象文字列がある間、以下の処理を繰り返す
                        while (endPoint != -1)
                        {
                            //文字列から対象文字列を元に検索行い変換文字列に置換する。
                            afterString = afterString + firstString .substring(startPoint, endPoint) + henkanString;
                            startPoint  = endPoint + plusPoint;
                            endPoint    = firstString .indexOf(taisyoString, startPoint);
                        }
                        afterString = afterString + firstString .substring(startPoint);

%>

						<div class="honbun_margin">
						<%= afterString %>
						</div>
					</td>
				</tr>

				<tr>
					<td  colspan="3" ><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>

<%
                    }
                }
%>

				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
				</tr>


<%
            }
            }
        }
    }
    DBConnection.releaseResources(result,prestate,connection);
%>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="50"></td>
				</tr>
<%
    if (Integer.parseInt(data_type) % 2 == 0 && count > 0)
    {
%>
				<tr valign="middle" class="subtitlebar_basecolor">
					<td colspan="3">
						<div class="subtitle_text">クリックすると明細が表示されます。</div>
					</td>
				</tr>
<%
    }
%>
			</table>
		</td><input name="LineCount" type=hidden value="<%= count %>"></form>
	</tr>

	<tr valign="bottom">
		<td>
			<!-- copyright テーブル -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center" valign="middle">
						<div class="copyright">
							Copyright &copy; almex inc, All Rights Reserved
						</div>
					</td>
				</tr>
				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
				</tr>
			</table>

		</td>
	</tr>
</table>

</body>
</html>


