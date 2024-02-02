<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.HotelElement" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../csrf/refererCheck.jsp" %>
<%@ include file="../../common/pc/menu_ini.jsp" %>
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String requestUri = request.getRequestURI();
    boolean DebugMode = false;
    if (requestUri.indexOf("_debug_/demo/") != -1)
    {
       DebugMode = true;
    }

    String loginHotelId = (String)session.getAttribute("LoginHotelId");

    int     i  = 0;
    // ﾊﾟﾗﾒｰﾀ取得
    // ﾎﾃﾙID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    if  (hotelid.compareTo("all") == 0)
    {
        hotelid = request.getParameter("HotelId");
        if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
        {
            response.sendError(400);
            return;
        }
    }
    String data_type = request.getParameter("DataType");
    String id = request.getParameter("Id");
    String[] dates = new String[]{ data_type, id };
    for( String date : dates )
    {
        if(CheckString.isValidParameter(date) && !CheckString.numAlphaCheck(date))
        {
            response.sendError(400);
            return;
        }
    }
    String query ="";
    boolean mobile = false;
    if (Integer.parseInt(data_type) % 2 == 0) mobile = true;//携帯
    DateEdit  de = new DateEdit();

    int       disp_idx   = 0;
    int       disp_flg   = 1;
    java.util.Date start = new java.util.Date();
    java.util.Date end = new java.util.Date();
    String    title      = "";
    String    title_color = "";
    int       nowdate     = Integer.parseInt(de.getDate(2));
    int       nowtime     = Integer.parseInt(de.getTime(1));
    int       start_time  = nowtime;
    int       end_time    = 235959;
    int       last_update = nowdate;
    boolean   EditFlag    = false;
    boolean   NewFlag     = false;

    String[]  msg_title       = new String[8];
    String[]  msg_title_color = new String[8];
    String[]  msg             = new String[8];

    int       member_only = 0;
    int       smart_flg = 0;

    int       trialDate = HotelElement.getTrialDate(hotelid);
    int       startDate = HotelElement.getStartDate(hotelid);
    boolean   reNewFlag = false;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

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
        if( result.next() )
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

    // HP編集メニュー名称の書き換え
    try
    {
        query = "SELECT * FROM menu WHERE hotelid=?";
        query = query + " AND data_type=" + (((Integer.parseInt(data_type)+1) % 2)*2+1);
        query = query + " AND start_date <=" + nowdate;
        query = query + " AND end_date >=" + nowdate;
        query = query + " AND disp_flg =1";
        query = query + " AND hpedit_id=" + (MenuDataType[(Integer.parseInt(data_type)+1)/2]+1);
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            Title[(Integer.parseInt(data_type)+1) % 2][MenuDataType[(Integer.parseInt(data_type)+1)/2]]=result.getString("title");
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

    if(!mobile && nowdate >= trialDate){ //リニューアル済みの場合
        try
        {
            query = "SELECT * FROM menu_config WHERE hotelid=?";
            query = query + " AND data_type IN (1,2)";
            int dataType = Integer.parseInt(data_type);
            if (dataType == 15)
            {
                query = query + " AND contents = 'service'";
            }
            else
            {
                query = query + " AND event_data_type = ?";
            }
            query = query + " ORDER BY data_type,disp_idx";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            if (dataType != 15)
            {
            	prestate.setInt(2, Integer.parseInt(data_type));
            }
            result      = prestate.executeQuery();
            if( result.next() )
            {
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


    //携帯の場合、最後に編集されたものをデフォルト色指定とする。
    String DefaultTitleColor    = "";
    String DefaultMsgTitleColor = "";

    if (mobile)
    {
        DefaultTitleColor    = "#000000";
        DefaultMsgTitleColor = "#663333";
        try
        {
            query = "SELECT * FROM edit_event_info WHERE hotelid=?";
            query = query + " AND data_type % 2 = 0";
            query = query + " ORDER BY last_update DESC,last_uptime DESC";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            result      = prestate.executeQuery();
            if( result.next() )
            {
                DefaultTitleColor    = result.getString("title_color");
                DefaultMsgTitleColor = result.getString("msg1_title_color");
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
        title_color = DefaultTitleColor;
    }

    for( i = 0 ; i < 8 ; i++ )
    {
        msg_title[i] = "";
        msg[i]       = "";
        msg_title_color[i] = DefaultMsgTitleColor;
    }


    // member_only = 2; ビジターのみの使用有無チェック
    boolean visitor_flag = false;
    try
    {
        query = "SELECT * FROM edit_event_info WHERE hotelid=?";
        query = query + " AND member_only=2";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            visitor_flag = true;
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

    if( id.compareTo("0") == 0 )
    {
        NewFlag    = true;
    }
    else
    {
        try
        {
            query = "SELECT * FROM edit_event_info WHERE hotelid=?";
            query = query + " AND data_type=?";
            query = query + " AND id=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,hotelid );
            prestate.setInt(2,Integer.parseInt(data_type));
            prestate.setInt(3,Integer.parseInt(id));
            result      = prestate.executeQuery();
            if( result.next() != false )
            {

                disp_idx    = result.getInt("disp_idx");
                disp_flg    = result.getInt("disp_flg");
                start       = result.getDate("start_date");
                end         = result.getDate("end_date");
                start_time  = result.getInt("start_time");
                end_time    = result.getInt("end_time");
                if (end_time == 0)
                {
                    end_time = 235959;
                }
                title       = result.getString("title");
                title_color = result.getString("title_color");
                last_update = result.getInt("last_update");
                smart_flg   = result.getInt("smart_flg");

                for( i = 0 ; i < 8 ; i++ )
                {
                    if( result.getString("msg" + (i + 1) + "_title").length() != 0 || result.getString("msg" + (i + 1)).length() > 1)
                    {
                        msg_title[i]       = result.getString("msg" + (i + 1) + "_title");
                        msg_title_color[i] = result.getString("msg" + (i + 1) + "_title_color");
                        msg[i]             = result.getString("msg" + (i + 1));
//                        msg[i] = msg[i].replace("<iframe","<span");
//                        msg[i] = msg[i].replace("</iframe","</span");
                        msg[i] = msg[i].replace("<","&lt;");
                        msg[i] = msg[i].replace(">","&gt;");
                    }
                }
                member_only = result.getInt("member_only");
            }
            else
            {
                NewFlag    = true;
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
    if (NewFlag)
    {
        try
        {
            query = "SELECT * FROM edit_event_info WHERE hotelid=?";
            query = query + " AND data_type<100";
            query = query + " AND disp_idx != -9999";
            query = query + " ORDER BY disp_idx";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            result      = prestate.executeQuery();
            if( result.next() )
            {
                disp_idx = result.getInt("disp_idx");
                if (disp_idx > -999) disp_idx = disp_idx - 1;
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
    DBConnection.releaseResources(connection);


    int start_year  = start.getYear()+1900;
    int start_month = start.getMonth()+1;
    int start_day   = start.getDate();
    int start_date  = start_year * 10000 + start_month * 100 + start_day;
    int end_year    = end.getYear()+1900;
    int end_month   = end.getMonth()+1;
    int end_day     = end.getDate();
    int end_date    = end_year * 10000 + end_month * 100 + end_day;


%><%@ include file="../../common/pc/edit_form_ini.jsp" %><%

    if (data_type.equals("43") || data_type.equals("45") || data_type.equals("47") || data_type.equals("53"))
    {
        if ((start_date < trialDate || NewFlag) && trialDate != 99999999)
        {
            if (data_type.equals("47"))
            {
                msg[0] = MsgDefault[47][0];
                msg[1] = MsgDefault[47][1];
                msg[2] = MsgDefault[47][2];
                msg_title[0] = MsgTitleDefault[47][0];
                msg_title[1] = MsgTitleDefault[47][1];
                msg_title[2] = MsgTitleDefault[47][2];
            }
            else if (data_type.equals("45"))
            {
                msg[0] = MsgDefault[45][0];
                msg[1] = MsgDefault[45][1];
                msg[2] = MsgDefault[45][2];
                msg_title[0] = MsgTitleDefault[45][0];
                msg_title[1] = MsgTitleDefault[45][1];
                msg_title[2] = MsgTitleDefault[45][2];
            }
            else if (data_type.equals("43"))
            {
                msg[0] = MsgDefault[43][0];
                msg_title[0] = MsgTitleDefault[43][0];
            }
            else if (data_type.equals("53"))
            {
                msg[0] = MsgDefault[53][0];
                msg[1] = MsgDefault[53][1];
                msg[2] = MsgDefault[53][2];
                msg[3] = MsgDefault[53][3];
                msg_title[0] = MsgTitleDefault[53][0];
                msg_title[1] = MsgTitleDefault[53][1];
                msg_title[2] = MsgTitleDefault[53][2];
                msg_title[3] = MsgTitleDefault[53][3];
            }
        }
        if (trialDate != 99999999)
        {
            if (trialDate > start_date)
            {
                reNewFlag = true;
                start_date = trialDate;
            }
        }
    }
    if (NewFlag)
    {
//        start_date    = de.addDay(start_date,1);
    }
    start_year    = start_date / 10000;
    start_month   =(start_date / 100) % 100;
    start_day     = start_date % 100;

    if (last_update != nowdate) EditFlag = true;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>イベント情報編集</title>
<% if (loginHotelId.compareTo("hotenavi") != 0){%>
<link href="../../<%= image_hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<%}%>
<link href="http://www.hotenavi.com/<%= image_hotelid %>/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/edit_form.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/tohankaku.js"></script>

</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF"> 編　集</font></td>
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
        <td align="center" valign="top" class="size10">
           <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="8" class="size10" bgcolor="#FFFFFF"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
              <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
                  <tr>
                    <td align="right" class="size10">&nbsp;</td>
                    <td align="right" class="size10">&nbsp;</td>
                    <td align="right" class="size10">&nbsp;</td>
                  </tr>

				<tr>
					<td align="left" colspan="3" valign="middle" style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;">
						<strong><%= Title[(Integer.parseInt(data_type)+1) % 2][MenuDataType[(Integer.parseInt(data_type)+1)/2]]%></strong>
						<%if(mobile){%>(携帯版）<%}else{%>（PC版）<%}%>
						&nbsp;&nbsp;<strong><%if(NewFlag){%>新規作成<%}else{%>更新<%}%></strong>
					</td>
				</tr>
                  <tr>
                    <td class="red12"></td>
                    <td align="right" class="size12">&nbsp;</td>
                    <td align="right" class="size12">&nbsp;</td>
                  </tr>
<%
    if( mobile )
    {
%>
                  <tr>
                    <td class="red12"><strong>※携帯の機種により色を変更すると正常に表示されない場合があります</strong></td>
                    <td align="right" class="size12">&nbsp;</td>
                    <td align="right" class="size12">&nbsp;</td>
                  </tr>
<%
    }
%>
                  <tr>
                    <td colspan="3"  class="size10"><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                </table>
              </div>
              </td>
            </tr>
          </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0" >
              <tr>
                <td  class="size12" bgcolor="#FFFFFF"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
                <td>
                  <table width="100%" border="0" cellspacing="0" cellpadding="2" >
                  <form name=form1 method=post>
                    <tr bgcolor="#FFFFFF">
                      <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="60%" align="center" valign="middle" class="size12" bgcolor="#969EAD">
                          <input name="regsubmit1" type=button value="下書き保存（非掲載）" onClick="if (validation_range('<%=mobile%>','<%=data_type%>')){MM_openInput2('event_edit', '<%= hotelid %>', <%= data_type %>, <%= id %>,2)}">
                          <input name="regsubmit2" type=button value="保存（掲載）" onClick="if (validation_range('<%=mobile%>','<%=data_type%>')){MM_openInput2('event_edit', '<%= hotelid %>', <%= data_type %>, <%= id %>,1)}">
<%
    if (!NewFlag)
    {
%>
						  <input name="BackUp" type="checkbox" value="1" <%if (reNewFlag){%>checked<%}%> >Backupを残す
<%
    }
%>
						  </td>
                          <td width="40%" align="center" valign="middle" class="size12" bgcolor="#969EAD">
                          <input name="presubmit" type=button value="プレビュー" onClick="MM_openPreview('preview', <%= data_type %>)"></td>
                        </tr>
                      </table>
                      </td>
                    </tr>
                  <tr align="left" bgcolor="#FFFFFF">
                    <td  class="size12">
                      <div align="left" class="size12" style="CLEAR: both; FLOAT: left;color:black">
						表示：
                        <input name="col_member_only" type="radio" value=0 <% if (member_only == 0) { %> checked <% } %>>両方
                        <input name="col_member_only" type="radio" value=1 <% if (member_only == 1) { %> checked <% } %>>メンバーのみ
                        <input name="col_member_only" type="radio" value=2 <% if (member_only == 2) { %> checked <% } %>>ビジターのみ
                        <input name="old_disp_flg" type="hidden" value="<%=disp_flg%>">
                        <%if( disp_flg == 0 ){%><font color=red><strong>（非表示）</strong></font><%}%><%if( disp_flg == 2 ){%><font color=red><strong>（下書き保存）</strong></font><%}%>
<%
    if( !mobile )
    {
%>
						<span style="margin-left: 50px;">
						ブラウザ：
                        <input name="col_smart_flg" type="radio" value=0 <% if (smart_flg == 0) { %> checked <% } %>>両方
                        <input name="col_smart_flg" type="radio" value=1 <% if (smart_flg == 1) { %> checked <% } %>>スマホのみ
                        <input name="col_smart_flg" type="radio" value=2 <% if (smart_flg == 2) { %> checked <% } %>>PCのみ
						</span>
<%
    }
%>

<%
if (visitor_flag == false)
{
%>
<br>「ビジターのみ」はまだ使用されていません。
<%
}
%>

<br><br>
						表示期間：<input name="col_start_yy" type="text" size="4" maxlength="4" value="<%= start_year %>" onChange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">年<input name="col_start_mm" type="text" size="2" maxlength="2" value="<%= start_month %>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">月<input name="col_start_dd" type="text" size="2" maxlength="2" value="<%= start_day %>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">日
						<input name="col_start_hour"   type="text" size="2" maxlength="2" value="<%= start_time/10000 %>" style="text-align:right;" onChange="setTimeRange(this,'<%=start_time%>','<%=end_time%>');">:<input name="col_start_minute" type="text" size="2" maxlength="2" value="<%= start_time/100%100 %>" style="text-align:right;" onChange="setTimeRange(this,'<%=start_time%>','<%=end_time%>');">
						～
						<input name="col_end_yy" type="text" size="4" maxlength="4"  value="<%if(NewFlag){%>2999<%}else{%><%= end_year %><%}%>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">年<input name="col_end_mm" type="text" size="2" maxlength="2" value="<%if(NewFlag){%>12<%}else{%><%= end_month %><%}%>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">月<input name="col_end_dd" type="text" size="2" maxlength="2" value="<%if(NewFlag){%>31<%}else{%><%= end_day %><%}%>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">日
						<input name="col_end_hour"   type="text" size="2" maxlength="2" value="<%= end_time/10000 %>" style="text-align:right;" onChange="setTimeRange(this,'<%=start_time%>','<%=end_time%>');">:<input name="col_end_minute" type="text" size="2" maxlength="2" value="<%= end_time/100%100 %>" style="text-align:right;" onChange="setTimeRange(this,'<%=start_time%>','<%=end_time%>');">
						<%
							long end_dtm = ((long) end_date) * 1000000 + end_time;
							long now_dtm = ((long) nowdate) * 1000000 + nowtime;
							if (end_dtm >= now_dtm) {
						%>
						<input type="button" onclick="setEndDtm()" value="掲載終了"/>
						<%
							}
						%>
							<img src="/common/pc/image/spacer.gif" width="25" height="8">
<%
        if(!DebugMode && disp_idx == -9999)
        {
%>
                        <input name="col_disp_idx" type="hidden"  value="<%= disp_idx %>" >
<%
        }else{
%>
                        表示順：<input name="col_disp_idx" type="text" size="3" <% if (!DebugMode){%>maxlength="4"<%}%> value="<%= disp_idx %>" style="text-align:right;" onchange="if (this.value<-999 <%if(DebugMode){%>&& this.value!=-9999<%}%>){this.value=-999}">
<%
        }
%>
                      </div>
                    </td>
                  </tr>
                  <tr align="left" bgcolor="#FFFFFF">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>

                  <tr align="left" >
<%
        if (mobile)//携帯
        {
%>
					<td bgcolor="#FFFFFF">
						<table border="1" cellspacing="0" cellpadding="0" class="size10" width="100%">
								<td colstan="4">
									<div id="title_big" class="size10" style="color:<%= title_color %>" >
										&nbsp;大見出し：<input name="col_title" type="text" size="35" value="<%= new ReplaceString().HTMLEscape(title)%><%if(title.equals("") && TitleDefault[Integer.parseInt(data_type)]!=null){%><%=TitleDefault[Integer.parseInt(data_type)]%><%}%>" onChange="<%if(EditFlag){%>CheckEdit();<%}%>">
										<input name="old_title" type="hidden" value="<%=  new ReplaceString().HTMLEscape(title) %>">
										<input type=button value="ｶﾅ半角に変換" onClick="document.form1.col_title.value = toHankaku(document.form1.col_title.value);">
										<small><strong>文字色：</strong><input name="col_title_color"  id="titlecol0" type="text" size="8" readonly value="<%= title_color %>" onchange="title_big.style.color=document.form1.col_title_color.value"></small>
										<img style="background-color:white" src="/common/pc/image/color_btn.gif" name="color_btn1" width="100" height="22" align="absmiddle" id="color_btn1" onClick="MM_openBrWindow('../../common/pc/event_edit_select_color.html','色見本','width=240,height=180',0)" onMouseOver="MM_swapImage('color_btn1','','/common/pc/image/color_btn_o.gif',1)" onMouseOut="MM_swapImgRestore();title_big.style.color=document.form1.col_title_color.value">
									</div>
								</td>
							</tr>
						</table>
					</td>
<%
        }
        else
        {
%>
					<td>
						<table cellspacing="0" cellpadding="0" class="subtitlebar_basecolor" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3" ><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor" ><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td>
									<div class="subtitle_text" id="title_big"  style="color:<%= title_color %>">
										大見出し：<input name="col_title" type="text" size="64" value="<%=  new ReplaceString().HTMLEscape(title) %><%if(title.equals("") && TitleDefault[Integer.parseInt(data_type)]!=null){%><%=TitleDefault[Integer.parseInt(data_type)]%><%}%>" onChange="<%if(EditFlag){%>CheckEdit();<%}%>">
										<input name="old_title" type="hidden" value="<%= new ReplaceString().HTMLEscape(title) %>">
										<small><strong>文字色：</strong><input name="col_title_color"  id="titlecol0" type="text" size="8" readonly value="<%= title_color %>" onchange="title_big.style.color=document.form1.col_title_color.value"></small>
										<img border="0" style="background-color:white" src="/common/pc/image/color_btn.gif" name="color_btn1" width="100" height="22" align="absmiddle" id="color_btn1" onClick="MM_openBrWindow('../../common/pc/event_edit_select_color.html','色見本','width=240,height=180',0)" onMouseOver="MM_swapImage('color_btn1','','/common/pc/image/color_btn_o.gif',1)" onMouseOut="MM_swapImgRestore()">
									</div>
								</td>
							</tr>
						</table>
					</td>

<%
        }
%>
                  </tr>
                  <tr align="left" bgcolor="#FFFFFF">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
<!--小見出しここから(1) -->
<%
    for( i = 0 ; i < 8 ; i++ )
    {
%>
				<tr>
<%
        if (mobile)//携帯
        {
%>
					<td bgcolor="#FFFFFF">
						<table width="100%" border="1" cellpadding="0" cellspacing="0" height="16">
							<tr>
								<td class="size10">
									<div id="msg_title_<%= i + 1 %>" style="color:<%= msg_title_color[i] %>">
										&nbsp;小見出し（<%= i + 1 %>）：<input name="col_msg<%= i + 1 %>_title" type="text" size="32" value="<%=new ReplaceString().HTMLEscape(msg_title[i])%><%if(msg_title[i].equals("") && MsgTitleDefault[Integer.parseInt(data_type)][i]!=null){%><%=MsgTitleDefault[Integer.parseInt(data_type)][i]%><%}%>"  onchange="<%if(EditFlag){%>CheckEdit();<%}%>" >
										<input type=button value="ｶﾅ半角に変換" onClick="document.form1.col_msg<%= i + 1 %>_title.value = toHankaku(document.form1.col_msg<%= i + 1 %>_title.value);<%if(EditFlag){%>CheckEdit();<%}%>">
										<input name="old_msg<%= i + 1 %>_title" type="hidden" value="<%=  new ReplaceString().HTMLEscape(msg_title[i]) %>">
										<small><strong>文字色：</strong><input name="col_msg<%= i + 1 %>_title_color"  id="titlecol<%= i + 1 %>" type="text" readonly size="8" value="<%= msg_title_color[i] %>" onchange="msg_title_<%= i + 1 %>.style.color=document.form1.col_msg<%= i + 1 %>_title_color.value"></small>
										<img border="0" style="background-color:white" src="/common/pc/image/color_btn.gif" name="color_btn<%= i + 2 %>" width="100" height="22" align="absmiddle" id="color_btn" onClick="MM_openBrWindow('../../common/pc/event_edit_select_color.html','色見本','width=240,height=180',<%= i + 1 %>)" onMouseOver="MM_swapImage('color_btn<%= i + 2 %>','','/common/pc/image/color_btn_o.gif',1)" onMouseOut="MM_swapImgRestore()">
									</div>
								</td>
							</tr>
						</table>
					</td>
<%
        }
        else
        {
%>
					<td class="honbuntitlebar">
						<table width="100%" border="0" cellpadding="0" cellspacing="0" height="16">
							<tr>
								<td class="honbuntatitlebar_text">
									<div id="msg_title_<%= i + 1 %>" style="color:<%= msg_title_color[i] %>">
										小見出し（<%= i + 1 %>）：<input name="col_msg<%= i + 1 %>_title" type="text" size="62" value="<%=  new ReplaceString().HTMLEscape(msg_title[i]) %><%if(msg_title[i].equals("") && MsgTitleDefault[Integer.parseInt(data_type)][i]!=null){%><%=MsgTitleDefault[Integer.parseInt(data_type)][i]%><%}%>" onchange="<%if(EditFlag){%>CheckEdit();<%}%>" >
										<input name="old_msg<%= i + 1 %>_title" type="hidden" value="<%=  new ReplaceString().HTMLEscape(msg_title[i]) %>">
										<img src="image/spacer.gif" width="6" height="16" border="0" align="absmiddle">
										<small><strong>文字色：</strong><input name="col_msg<%= i + 1 %>_title_color"  id="titlecol<%= i + 1 %>" type="text" readonly size="8" value="<%= msg_title_color[i] %>" onchange="msg_title_<%= i + 1 %>.style.color=document.form1.col_msg<%= i + 1 %>_title_color.value"></small>
										<img border="0" style="background-color:white" src="/common/pc/image/color_btn.gif" name="color_btn<%= i + 2 %>" width="100" height="22" align="absmiddle" id="color_btn" onClick="MM_openBrWindow('../../common/pc/event_edit_select_color.html','色見本','width=240,height=180',<%= i + 1 %>)" onMouseOver="MM_swapImage('color_btn<%= i + 2 %>','','/common/pc/image/color_btn_o.gif',1)" onMouseOut="MM_swapImgRestore()">
									</div>
								</td>
							</tr>
						</table>
					</td>
<%
        }
%>
				</tr>
				<tr align="left" bgcolor="#FFFFFF">
					<td class="size10"><img src="/common/pc/image/spacer.gif" width="200" height="3"></td>
                </tr>
				<tr bgcolor="#FFFFFF">
					<td align="left" valign="top">
						<div class="size12">
						<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td class="size12">
								<strong>&nbsp;トピック本文</strong>
								<select id="spansize<%= i + 1 %>" name="spansize<%= i + 1 %>" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onchange="if(spansize<%= i + 1 %>.selectedIndex !=0){enclose('<span style=&quot;font-size:' + spansize<%= i + 1 %>.options[spansize<%= i + 1 %>.selectedIndex].value + '&quot;>', '</span>');spansize<%= i + 1 %>.selectedIndex=0;return false;}">
									<option value="">文字サイズ</option><option value="60%">小さく</option><option value="105%">少し大きく</option><option value="120%">大きく</option><option value="150%">とても大きく</option></select>
								<input type="button" name="strong<%= i + 1 %>" value="太" style="font-weight:bold" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<strong>', '</strong>');return false;">
								<input type="button" name="em<%= i + 1 %>" value="斜" style="font-style:oblique" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<em>', '</em>');return false;">
								<input type="button" name="under<%= i + 1 %>" value="線" style="text-decoration:underline" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;text-decoration: underline;&quot;>', '</span>');return false;">
							</td>
							<td class="size12">
<input type="image" border="0" style="margin:1px;margin-left:5px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:#000000&quot;>', '</span>');return false;" src="/common/pc/image/000000.gif" alt="black"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:#FF0000&quot;>', '</span>');return false;" src="/common/pc/image/FF0000.gif" alt="red"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:#FF1493&quot;>', '</span>');return false;" src="/common/pc/image/FF1493.gif" alt="deeppink"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:#800080&quot;>', '</span>');return false;" src="/common/pc/image/800080.gif" alt="purple"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:#0000FF&quot;>', '</span>');return false;" src="/common/pc/image/0000FF.gif" alt="blue"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:#008000&quot;>', '</span>');return false;" src="/common/pc/image/008000.gif" alt="green"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:#808000&quot;>', '</span>');return false;" src="/common/pc/image/808000.gif" alt="olive"/><br/>
<input type="image" border="0" style="margin:1px;margin-left:5px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:#FFFFFF&quot;>', '</span>');return false;" src="/common/pc/image/FFFFFF.gif" alt="white"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:#FA8072&quot;>', '</span>');return false;" src="/common/pc/image/FA8072.gif" alt="salmon"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:#EE82EE&quot;>', '</span>');return false;" src="/common/pc/image/EE82EE.gif" alt="violet"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:#9370DB&quot;>', '</span>');return false;" src="/common/pc/image/9370DB.gif" alt="mediumpurple"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:#00BFFF&quot;>', '</span>');return false;" src="/common/pc/image/00BFFF.gif" alt="deepskyblue"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:#33CC33&quot;>', '</span>');return false;" src="/common/pc/image/33CC33.gif" alt="limegreen"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:#FFD700&quot;>', '</span>');return false;" src="/common/pc/image/FFD700.gif" alt="gold"/>
							</td>
							<td>
								<input type="hidden" id="spancol<%= i + 1 %>" name="spancol<%= i + 1 %>" value="#" size=7 maxlength="7" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" >
								<!--							<input type="button" name="spanbtn<%= i + 1 %>" value="色" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:' + document.getElementById('spancol<%= i + 1 %>').value+'&quot;>', '</span>');return false;">-->
								<img border="0" style="background-color:white" src="/common/pc/image/color_btn.gif" name="color_btn_d<%= i + 1 %>" width="100" height="22" align="absmiddle" id="color_btn_d<%= i + 1 %>" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onClick="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);MM_openBrWindowDetail('../../common/pc/event_edit_select_color.html','色見本','width=240,height=180,menubar=no,scrollbars=no,toolbar=no,status=no,resizable=no',<%= i + 1 %>)" onMouseOver="MM_swapImage('color_btn_d<%= i + 1 %>','','/common/pc/image/color_btn_o.gif',1)" onMouseOut="MM_swapImgRestore()">
<%
        if (!mobile)//PC
        {
%>
							<input type="button" name="hrefbtn<%= i + 1 %>" value="リンク" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);"  onClick="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>); window.open('../../common/pc/event_edit_link.html','リンク先','screenX=300,screenY=400,left=300,top=400,width=240,height=60');">
							<input type="button" name="imgbtn<%= i + 1 %>" value="画像表示" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);"  onClick="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);　window.open('event_edit_img.jsp','画像表示','screenX=200,screenY=200,left=200,top=200,width=560,height=400');">
<%
        }
%>

							</td>
						</tr>
						</table>
						</div>
					</td>
				</tr>
<%
        if (mobile)//携帯
        {
%>
				<tr align="left" bgcolor="#FFFFFF">
					<td class="size10">
						<div class="honbun_margin" style="float:left;clear:both;">
							<textarea <%if (!DebugMode && msg[i].indexOf("&lt;imedia&gt;")==0){%>readonly<%}%> id="col_msg<%= i + 1 %>" name=col_msg<%= i + 1 %> rows=10 cols=40 onchange="<%if(EditFlag){%>CheckEdit();<%}%>" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);get_pos(col_msg<%= i + 1 %>);" onkeyup="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);get_pos(col_msg<%= i + 1 %>);" onmouseup="get_pos(col_msg<%= i + 1 %>);" onmousedown="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onblur="dispArea<%= i + 1 %>.innerHTML='';"><%= msg[i].replace("\"","&quot;").replace("&lt;imedia&gt;","") %><%if(msg[i].length() < 2 && MsgDefault[Integer.parseInt(data_type)][i]!=null){%><%=MsgDefault[Integer.parseInt(data_type)][i]%><%}%></textarea>
							<input name="old_msg<%= i + 1 %>" type="hidden" value="<%= msg[i].replace("\"","&quot;") %>">
						</div>
						<div class="size12">
							<%if (msg[i].indexOf("&lt;imedia&gt;")==0){%><font color="red"><strong>ロックがかかっていますので編集できません。<br></strong></font><%}%>
							<%if(DebugMode){%>先頭に&lt;imedia&gt;を入力すると禁止タグを登録できます。<br><%}%>
							装飾する場合は、マウス等で装飾テキストの範囲を指定してから各ボタンをクリックしてください。<br/><br/>
							<input type=button value="ｶﾅ半角に変換" onClick="document.form1.col_msg<%= i + 1 %>.value = toHankaku(document.form1.col_msg<%= i + 1 %>.value);<%if(EditFlag){%>CheckEdit();<%}%>"><br>
							<font size="1"><%if(MsgReplace[Integer.parseInt(data_type)][i]!=null){%><%=MsgReplace[Integer.parseInt(data_type)][i]%><%}%></font>
						</div>
					</td>
				</tr>
<%
        }
        else
        {
%>
				<tr align="left" bgcolor="#FFFFFF">
					<td class="honbuntitlebar_text">
						<div class="honbun_margin" style="float:left;clear:both;">
							<textarea <%if (!DebugMode && msg[i].indexOf("&lt;imedia&gt;")==0){%>readonly<%}%> id="col_msg<%= i + 1 %>" name=col_msg<%= i + 1 %> rows=10 cols=64 onchange="<%if(EditFlag){%>CheckEdit();<%}%>" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);get_pos(col_msg<%= i + 1 %>);" onkeyup="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);get_pos(col_msg<%= i + 1 %>);" onmouseup="get_pos(col_msg<%= i + 1 %>);" onmousedown="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onblur="dispArea<%= i + 1 %>.innerHTML='';"><%= msg[i].replace("\"","&quot;").replace("&lt;imedia&gt;","") %><%if(msg[i].length() < 2 && MsgDefault[Integer.parseInt(data_type)][i]!=null){%><%=MsgDefault[Integer.parseInt(data_type)][i]%><%}%></textarea>
							<input name="old_msg<%= i + 1 %>" type="hidden" value="<%= msg[i].replace("\"","&quot;") %>" >
						</div>
						<div class="size12">
							<%if (msg[i].indexOf("&lt;imedia&gt;")==0){%><font color="red"><strong>ロックがかかっていますので編集できません。<br></strong></font><%}%>
							<%if(DebugMode){%>先頭に&lt;imedia&gt;を入力すると禁止タグを登録できます。<br><%}%>
								装飾する場合は、マウス等で装飾したいテキスト範囲を指定してから各ボタンをクリックしてください。<br>
							<%if(MsgReplace[Integer.parseInt(data_type)][i]!=null){%><%=MsgReplace[Integer.parseInt(data_type)][i]%><%}%>

<!--							<input type="text" name="href<%= i + 1 %>" id="href<%= i + 1 %>" size="30" value="http://" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" >
							<input type="button" name="hrefbtn<%= i + 1 %>" value="リンク" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);"  onclick="enclose('<a href=&quot;' + document.getElementById('href<%= i + 1 %>').value + '&quot;>', '</a>');return false;"><br/>
-->
<!--							<input type="text" id="img" name="img" value="" size=10 onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);">(.png .jpg .gif のみ）<input type="button" name="imgbtn" value="画像" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<img src=&quot;' + document.getElementById('img').value+'&quot;>', '');return false;"><br/>
-->
						</div>
					</td>
				</tr>
<%
        }
%>
<script language="javascript">
if( String("jadge") ){
 bl=3;
} else if( document.getElementById ){
 bl=4;
}
if( document.getElementById("col_msg<%= i + 1 %>").setSelectionRange ){
  bl=2;
} else if( document.selection.createRange ){
  bl=1;
}
</script>
<%
        if (mobile)//携帯
        {
%>
				<tr align="left" bgcolor="#FFFFFF">
					<td width="160"  nowrap>
					<div class="size12" id="dispArea<%= i + 1 %>">
					</div>
					</td>
				</tr>
<%
        }
        else
        {
%>
				<tr align="left">
					<td >
						<div class="honbun" id="dispArea<%= i + 1 %>">
						</div>
					</td>
				</tr>
<%
        }
%>
                <tr align="left" bgcolor="#FFFFFF">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="3"></td>
                </tr>
<%
    }
%>
							<input type="hidden" name="hreflink" id="hreflink" value="http://">
							<input type="hidden" name="hrefblank" id="hrefblank" value="_blank">
							<input type="hidden" name="imglink" id="imglink" value="">
<script language="javascript">
</script>

<!-- 小見出しここまで -->

                  <tr align="left" bgcolor="#FFFFFF">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left" bgcolor="#FFFFFF">
                    <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="50%" align="center" valign="middle" bgcolor="#969EAD">
                          <input name="regsubmit1" type=button value="下書き保存（非掲載）" onClick="if (validation_range('<%=mobile%>','<%=data_type%>')){MM_openInput2('event_edit', '<%= hotelid %>', <%= data_type %>, <%= id %>,2)}">
                          <input name="regsubmit2" type=button value="保存（掲載）" onClick="if (validation_range('<%=mobile%>','<%=data_type%>')){MM_openInput2('event_edit', '<%= hotelid %>', <%= data_type %>, <%= id %>,1)}">
						  </td>
                          <td width="50%" align="center" valign="middle" bgcolor="#969EAD"><input name="presubmit" type=button value="プレビュー" onClick="MM_openPreview('preview', <%= data_type %>)"></td>

                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>

              </td>
              </form>

              </tr>
<%
    if(!NewFlag)
    {
%>
              <tr bgcolor="#FFFFFF">
                <td valign="top">&nbsp;</td>
                <td valign="top"></td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td valign="top">&nbsp;</td>
                <td height="30" align="center" valign="middle" bgcolor="#969EAD">
                  <form action="event_edit_delete.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=<%= id %>" method="POST">
                  <input name="submit_del" type=button value="削除" onclick="if (confirm('削除します。よろしいですか？')){location.href='event_edit_delete.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=<%= id %>'}">
                </td></form>
              </tr>
<%
    }
%>
              <tr bgcolor="#FFFFFF">
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td>&nbsp;</td>
                <td align="center">
                  <form action="event_list.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>" method="POST">
                  <input name="submit_ret" type=submit value="戻る" >
                  </form>
                </td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%" bgcolor="#FFFFFF">
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
  <tr bgcolor="#FFFFFF">
    <td><img src="/common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr bgcolor="#FFFFFF">
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/owner/Library/footer.lbi" --><img src="/common/pc/image/imedia.gif" width="63" height="18"><img src="/common/pc/image/spacer.gif" width="12" height="18" align="absmiddle">Copyrigtht&copy; almex
    inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="/common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
