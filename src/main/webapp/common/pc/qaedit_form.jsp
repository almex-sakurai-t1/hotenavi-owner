<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/qaedit_ini.jsp" %>
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");

    String query   = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection    = DBConnection.getConnection();
    // imedia_user のチェック
    int        imedia_user        = 0;
    int        level              = 0;
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
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
    boolean MemberFlag  = false;
    boolean RunningFlag = false;
    boolean EditFlag    = false;
    boolean AnswerFlag  = false;

    String paramEdit = ReplaceString.getParameter(request,"EDIT");
    if    (paramEdit == null) paramEdit="";
    if    (paramEdit.compareTo("Y")== 0) EditFlag = true;

    ReplaceString rs = new ReplaceString();
    String paramId   = ReplaceString.getParameter(request,"Id");
    if    (paramId  == null) paramId="0";

    int qid         = Integer.parseInt(paramId);
    int i           = 0;
    int id[]        = new int[31];
    int required[]  = new int[31];
    String data_q_id[]    = new String[31];
    String msg_subtitle = "";
    String msg[] = new String[31];
    java.util.Date start = new java.util.Date();
    java.util.Date end = new java.util.Date();

    int last_update     = nowdate;
    int member_flag     = 0;
    int count           = 0;
    int count_record    = 0;
    int duplicate_check = 0;
    boolean   NewFlag     = false;

    if( qid == 0 )
    {
        NewFlag    = true;
    }
    else if (!EditFlag)
    {
        query = "SELECT * FROM question_master WHERE hotel_id=?";
        query = query + " AND id=?";
        prestate        = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        prestate.setInt(2,qid);
        result          = prestate.executeQuery();
        if( result.next() != false )
        {
            member_flag = result.getInt("member_flag");
            if (member_flag % 2 == 1)
            {
                MemberFlag = true;//メンバー用
            }
            if (member_flag <= 1)
            {
                RunningFlag = true;//掲載中
            }
            start        = result.getDate("start");
            end          = result.getDate("end");
            qid = result.getInt("id");
            duplicate_check    = result.getInt("duplicate_check");
            msg_subtitle = result.getString("msg");
            for(i = 0 ; i < 30 ; i++ )
            {
                id[i]  = result.getInt("q" + (i+1));
                msg[i] = result.getString("q" + (i+1) + "_msg");
                required[i]  = id[i]/10;
            }
        }
        else
        {
              MemberFlag = false;
              id[0] = 0;
              qid = 0;
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    if (msg_subtitle.length() < 1)
    {
        msg_subtitle = "お客様について教えてください。";
    }

    if(!NewFlag)
    {
        count_record = 0;
        query = "SELECT COUNT(seq),MAX(seq),MIN(seq) FROM question_answer WHERE hotel_id=?";
        query = query + " AND id=?";
        prestate        = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        prestate.setInt(2,qid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            if (result.getInt(1) != 0)
            {
                count_record = result.getInt(2)-result.getInt(3)+1;
            }
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    DBConnection.releaseResources(connection);

    if (count_record != 0)
    {
        AnswerFlag   = true;
    }

    int start_year  = start.getYear()+1900;
    int start_month = start.getMonth()+1;
    int start_day   = start.getDate();
    int start_date  = start_year * 10000 + start_month * 100 + start_day; 

    int end_year    = end.getYear()+1900;
    int end_month   = end.getMonth()+1;
    int end_day     = end.getDate();
    int end_date    = end_year * 10000 + end_month * 100 + end_day; 

//    if (NewFlag)
//    {
//        start_date    = de.addDay(start_date,1);
//    }
    start_year    = start_date / 10000;
    start_month   =(start_date / 100) % 100;
    start_day     = start_date % 100;

    String BackUp  = "0";
    String  col_disp_flg        = ReplaceString.getParameter(request,"col_disp_flg");
    if      (col_disp_flg == null)        col_disp_flg = "0";
    String  col_member_only     = ReplaceString.getParameter(request,"col_member_only");
    if      (col_member_only == null)     col_member_only = "0";
    String  col_duplicate_check = ReplaceString.getParameter(request,"col_duplicate_check");
    if      (col_duplicate_check == null) col_duplicate_check = "0";
    String  col_qid_max         = ReplaceString.getParameter(request,"col_qid_max");
    if      (col_qid_max == null)         col_qid_max = "0";
    String  col_del_qid             = ReplaceString.getParameter(request,"del_qid");
    if      (col_del_qid == null)         col_del_qid  = "999";
    String  col_ins_qid             = ReplaceString.getParameter(request,"ins_qid");
    if      (col_ins_qid == null)         col_ins_qid  = "999";
    String  col_msg_subtitle     = ReplaceString.getParameter(request,"col_msg_subtitle");
    if     (col_msg_subtitle == null)        col_msg_subtitle = "お客様について教えてください。";
    int qid_max         = Integer.parseInt(col_qid_max);
    int del_qid         = Integer.parseInt(col_del_qid);
    int ins_qid         = Integer.parseInt(col_ins_qid);
    String col_msg[]      = new String[31];
    String col_id[]       = new String[31];
    String col_required[] = new String[31];
    String col_data[]     = new String[31];

    if (EditFlag)
    {
        start_year  = Integer.parseInt(ReplaceString.getParameter(request,"col_start_yy"));
        start_month = Integer.parseInt(ReplaceString.getParameter(request,"col_start_mm"));
        start_day   = Integer.parseInt(ReplaceString.getParameter(request,"col_start_dd"));
        end_year    = Integer.parseInt(ReplaceString.getParameter(request,"col_end_yy"));
        end_month   = Integer.parseInt(ReplaceString.getParameter(request,"col_end_mm"));
        end_day     = Integer.parseInt(ReplaceString.getParameter(request,"col_end_dd"));
        BackUp      = ReplaceString.getParameter(request,"BackUp");
        if (BackUp == null) BackUp = "0";
    }
    if (NewFlag)    EditFlag = true;
    if (EditFlag)
    {
        if (col_member_only.compareTo("1") == 0)
        {
           MemberFlag = true;
        }
        duplicate_check = Integer.parseInt(col_duplicate_check);
        msg_subtitle    = col_msg_subtitle;
        for( i = 0 ; i <= qid_max ; i++ )
        {
            int target = i;
            if ( i > del_qid)
            {
                target = i -1;
            }
            if ( i >= ins_qid)
            {
                target = i +1;
            }
            col_msg[target]      =  ReplaceString.getParameter(request,"col_msg"+i);
            if(col_msg[target]  == null) col_msg[target] = "";
            msg[target]          =  col_msg[target];
            col_id[target]       =  ReplaceString.getParameter(request,"col_id"+i);
            if (col_id[target] ==  null) col_id[target] = "0";
            id[target]           =  Integer.parseInt(col_id[target]);

            col_data[target]     =  ReplaceString.getParameter(request,"col_data"+i);
            if(col_data[target]  == null) col_data[target] = "";

            col_required[target] =  ReplaceString.getParameter(request,"col_required"+i);
            if (col_required[target] ==  null) col_required[target] = "0";
            required[target]     =  Integer.parseInt(col_required[target]);
        }
        if (del_qid != 999 && qid_max != 0)
        {
            qid_max = qid_max -1;
        }
        if (ins_qid != 999)
        {
            msg[ins_qid]         =  "";
            id[ins_qid]          = 0;
            col_data[ins_qid]    =  "";
            required[ins_qid]    = 0;
        }
    }
 %>
 <%

    int ins_disp        = 0;
    if  (ins_qid != 999)
    {
        for(i = 0 ; i <= ins_qid; i++ )
        {
            if(msg[i].compareTo("") != 0)
            {
                ins_disp++;
            }
        }
        ins_disp++;
    }
    else
    {
        ins_disp = 1;
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>アンケート</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<% if (loginHotelId.compareTo("demo") == 0){%>
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
<%}%>
<script type="text/javascript" src="../../common/pc/scripts/qaedit_form.js"></script>
<script type="text/javascript">
<!--
function func() {
    document.form1.col_msg<%=ins_qid%>.focus();
}
// -->
</script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" <% if (ins_qid != 999){%>onload="setTimeout('func()',500)"<%}%>>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td valign="top">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
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
			<td width="3">
				&nbsp;
			</td>
		</tr>
		<!-- ここから表 -->
		<tr>
			<td align="center" valign="top" class="size10">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="8" class="size10" bgcolor="#FFFFFF">
						<img src="/common/pc/image/spacer.gif" width="8" height="12">
					</td>
					<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
						<tr>
							<td align="right" class="size10">&nbsp;</td>
							<td align="right" class="size10">&nbsp;</td>
							<td align="right" class="size10">&nbsp;</td>
						</tr>
						<tr>
							<td align="left" colspan=3 valign="middle" style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="3">
								<strong>アンケート</strong>
								&nbsp;&nbsp;<strong><%if(NewFlag){%>新規作成<%}else{%>更新<%}%></strong>
							</td>
						</tr>
						<tr>
							<td class="red12">
<%
    if (!AnswerFlag)
    {
%> 
								<strong>※このページを編集し終えたら、「下書き保存(非掲載）」もしくは「保存（掲載）」ボタンを必ず押してください。</strong>
<%
    }
    else
    {
%>
								<strong>※すでにアンケートの投稿が発生していますので、修正箇所が限定されます。</strong><br>
								「Backupを残す」にチェックを入れて保存すると表示内容のものを利用して新規に作成できます。
<%
    }
%>
							</td>
							<td align="right" class="size12">&nbsp;</td>
							<td align="right" class="size12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="3"  class="size10"><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
						</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td  class="size12" bgcolor="#FFFFFF"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
					<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="2" >
						<form name=form1 method=post>
						<tr bgcolor="#FFFFFF">
							<td colspan="2">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="60%" align="center" valign="middle" class="size12" bgcolor="#969EAD">
										<input name="regsubmit1" type=button value="下書き保存（非掲載）" onClick="if (validation_range()){MM_openInput(<%= qid %>,1)}">
										<input name="regsubmit2" type=button value="保存（掲載）" onClick="if (validation_range()){MM_openInput(<%= qid %>,0)}">
<%
        if (!NewFlag)
        {
%>
										<input name="BackUp" type="checkbox" value="1" <%if (BackUp.compareTo("1") == 0){%>checked<%}%> >Backupを残す
<%
        }
%>
									</td>
									<td width="1%" align="center" valign="middle" class="size12" bgcolor="#969EAD">
									</td>
								</tr>
								</table>
							</td>
						</tr>
						<tr align="left" bgcolor="#FFFFFF">
							<td  class="size12"  rowspan="2" style="color:black">
								表示期間：
								<input name="col_start_yy" type="text" size="4" maxlength="4" value="<%= start_year %>" onChange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
								年
								<input name="col_start_mm" type="text" size="2" maxlength="2" value="<%= start_month %>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
								月
								<input name="col_start_dd" type="text" size="2" maxlength="2" value="<%= start_day %>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
								日～
								<input name="col_end_yy" type="text" size="4" maxlength="4"  value="<%if(NewFlag){%>2999<%}else{%><%= end_year %><%}%>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
								年
								<input name="col_end_mm" type="text" size="2" maxlength="2" value="<%if(NewFlag){%>12<%}else{%><%= end_month %><%}%>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
								月
								<input name="col_end_dd" type="text" size="2" maxlength="2" value="<%if(NewFlag){%>31<%}else{%><%= end_day %><%}%>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
								日<br/>
							</td>
							<td  class="size12"  style="color:black">
								<input name="col_member_only"     type="checkbox" value=1 <% if (MemberFlag)           {%>checked<%}%>>メンバーのみ
							</td>
						</tr>
						<tr align="left" bgcolor="#FFFFFF">
							<td class="size12"  style="color:black">
								<input name="col_duplicate_check" type="checkbox" value=1 <% if (duplicate_check == 1 ){%>checked<%}%>>二重投稿不可
							</td>
						</tr>
						<tr align="left" bgcolor="#FFFFFF">
							<td colspan="2"><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
						</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<img src="../../common/pc/image/spacer.gif" width="480" height="3">
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<!-- ############### サブタイトルバー1 ############### -->				
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td>
									<div class="subtitle_text">
									<input name="col_msg_subtitle" type="text" size="80" value="<%= msg_subtitle.replace("\"","&quot;") %>">
									</div>
								</td>
							</tr>
						</table>
						<!-- ############### サブタイトルバー1 ここまで ############### -->				
					</td>
				</tr>
				<tr>
					<td align="left" valign="top" class="honbun" colspan="2">
						<div class="honbun_margin">

						<!-- アンケートのテーブル -->
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="480" height="4"></td>
							</tr>
<%
            Connection        connection_sub  = null;
            PreparedStatement prestate_sub    = null;
            ResultSet         result_sub      = null;
            connection_sub    = DBConnection.getConnection();
            for( i = 0; i < 30; i++ )
            {
                if (EditFlag)
                {
                    if (i > qid_max)
                    {
                        if (i > 0)
                        {
                            i = i -1;
                        }
                        break;
                    }
                }
                else
                if( id[i] == 0 && !NewFlag)
                {
                    if (i > 0)
                    {
                       i = i -1;
                    }
                    break;
                }
%>
							<tr>
								<td class="hyouyou_bgcolor honbun">
								 <a name="qid_name<%=i%>"></a>
									<input <%if(AnswerFlag){%>disabled<%}else{%>name="col_msg<%=i%>" id="col_msg<%=i%>"<%}%> type="text" size="50" value="<%if(msg[i].compareTo("")!=0){%><%= msg[i].replace("\"","&quot;") %><%}else if(i==ins_qid || i == 0){%><%=ins_disp%>.質問内容を入力し、回答方法を選択してください→<%}%>" onfocus="ColFunc(dispArea<%=i%>,col_data<%=i%>,col_id<%=i%>,<%=i%>);" >
									<select <%if(AnswerFlag){%>disabled<%}else{%>name="col_id<%=i%>" id="col_id<%=i%>" <%}%> onfocus="ColFunc(dispArea<%=i%>,col_data<%=i%>,col_id<%=i%>,<%=i%>);" onchange="IdFunc(inputArea<%=i%>,explainArea<%=i%>,col_data<%=i%>,col_id<%=i%>);ColFunc(dispArea<%=i%>,col_data<%=i%>,col_id<%=i%>,<%=i%>);" >
										<option value="1" <%if (id[i]%10 == 1){%>selected<%}%>>チェックボックス</option>
										<option value="2" <%if (id[i]%10 == 2){%>selected<%}%>>ラジオボタン</option>
										<option value="3" <%if (id[i]%10 == 3){%>selected<%}%>>テキスト入力</option>
										<option value="4" <%if (id[i]%10 == 4){%>selected<%}%>>コンボボックス</option>
										<option value="9" <%if (id[i]%10 == 9 || id[i]%10 == 0){%>selected<%}%>>見出しのみ</option>
									</select>

									<input <%if(AnswerFlag){%>disabled<%}else{%>name="col_required<%=i%>"<%}%> type="checkbox" value=1 <% if (required[i] == 1 ){%>checked<%}%> onfocus="ColFunc(dispArea<%=i%>,col_data<%=i%>,col_id<%=i%>,<%=i%>);" >必須
<%
    if (AnswerFlag)
    {
%>
									<input name="col_msg<%=i%>"  type="hidden" value="<%if(msg[i].compareTo("")!=0){%><%= msg[i].replace("\"","&quot;") %><%}else if(i==ins_qid || i == 0){%><%=ins_disp%>.質問内容を入力し、回答方法を選択してください→<%}%>" >
									<input name="col_id<%=i%>"   type="hidden" value="<%= id[i]%10 %>" >
									<input name="col_required<%=i%>" type="hidden" value="<%=required[i]%>">
<%
    }
    else
    {
%> 
									<input name="delQid<%=i%>" type="button" value="削除" onclick="QidFunc('<%=hotelid%>','<%=qid%>','DEL',col_qid_max,<%=i%>);">
<%
        if (qid_max < 29)
        {
%> 
									<input name="delQid<%=i%>" type="button" value="挿入" onclick="QidFunc('<%=hotelid%>','<%=qid%>','INS',col_qid_max,<%=i%>);">
<%
        }
    }
%>
								</td>
							</tr>
							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="480" height="4"></td>
							</tr>
<%
                String data = ""; 
                if (EditFlag)
                {
                    data = col_data[i];
                }
                else 
                {
                    data_q_id[i]    = "q" +  (i + 1);
                    query = "SELECT * FROM question_data WHERE hotel_id=?";
                    query = query + " AND id=?";
                    query = query + " AND q_id=?";
                    prestate_sub    = connection_sub.prepareStatement(query);
                    prestate_sub.setString(1, hotelid);
                    prestate_sub.setInt(2, qid);
                    prestate_sub.setString(3,"q" + (i + 1));
                    result_sub      = prestate_sub.executeQuery();

                    if( result_sub != null )
                    {
                        while( result_sub.next() != false )
                        {
                            if (result_sub.getString("msg")!= null) 
                            data = data + result_sub.getString("msg") + "\r\n";
                        }
                    }
                    DBConnection.releaseResources(result_sub);
                    DBConnection.releaseResources(prestate_sub);
                }
%>
							<tr>
								<td class="honbun">
									<div id="inputArea<%= i %>" style="float:left;clear:both;display:<%if (id[i]%10 == 1 || id[i]%10 == 2 || id[i]%10 == 3 || id[i]%10 == 4){%>'block'<%}else{%>'none'<%}%>;" >
										<textarea <%if(AnswerFlag){%>disabled<%}else{%> name="col_data<%=i%>" <%}%> cols="40" rows="<%if (id[i]%10 == 1 || id[i]%10 == 2 || id[i]%10 == 4){%>10<%}else{%>1<%}%>"  onfocus="ColFunc(dispArea<%=i%>,col_data<%=i%>,col_id<%=i%>,<%=i%>);get_pos(col_data<%=i%>);" onkeyup="ColFunc(dispArea<%=i%>,col_data<%=i%>,col_id<%=i%>,<%=i%>);get_pos(col_data<%=i%>);" onmouseup="get_pos(col_data<%=i%>);" onmousedown="ColFunc(dispArea<%=i%>,col_data<%= i %>,col_id<%=i%>,<%=i%>);"><%if (data != null){%><%=data%><%}%></textarea>
										<%if (AnswerFlag) {%><input name="col_data<%=i%>" type="hidden" value="<%= data %>" ><%}%>
										<div class="honbun size12" id="dispArea<%= i %>">
										<%if (id[i]%10 == 1 || id[i]%10 == 2 || id[i]%10 == 4){%><br><%}else{%><br><br><br><br><br><%}%>
										</div>
									</div>
									<div class="honbun size10" id="explainArea<%= i %>">
<%
                    switch (id[i]%10)
                    {
                        case 1:
%>
										チェックボックスは選択肢について複数回答を求めるものです。質問の最後に（複数回答）と入力することをおすすめします。<br><br>
										改行すると選択肢を追加できます。<br>空白行は無視されます。<br>
<%
                            break;
                        case 2:
%>
										ラジオボタンは、選択肢のうち1つだけ回答を求めるものです。<br><br>
										改行すると選択肢を追加できます。<br>空白行は無視されます。<br>
<%
                            break;
                        case 3:
%>
										テキスト入力は、自由に回答を入力してもらうものです。<br><br>
										前問のチェックボックスやラジオボタンで最後に「その他」の選択肢を用意した場合は、質問を入力せずに、この左側に「その他を選択した方」等入力するとわかりやすくレイアウトされます。<br>
<%
                            break;
                        case 4:
%>
										コンボボックスは、プルダウン式で1つだけ回答を求めるものです。何も入力しない場合は先頭に表示されているものが選択されます。<br><br>
										改行すると選択肢を追加できます。<br>空白行は無視されます。<br>
<%
                            break;
                        default:
                            break;
                    }
%>
									</div>
								</td>
							</tr>
							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="480" height="8"></td>
							</tr>
<%
            }  // End of for( i = 0; i < 30; i++ )
            DBConnection.releaseResources(connection_sub);
    if (i == 30)
    {
        i = i -1;
    }
%>
							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="480" height="8"></td>
							</tr>
<%
    if (!AnswerFlag && qid_max < 29)
    {
%> 
							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="480" height="8"></td>
							</tr>
							<tr>
								<td align="left">
									<input name="addQid" type="button" value="設問追加" onclick="QidFunc('<%=hotelid%>','<%=qid%>','ADD',col_qid_max,col_qid_max);">
								</td>
							</tr>
							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="480" height="8"></td>
							</tr>
<%
    }
%>
							<tr valign="middle">
								<td align="center" valign="middle" bgcolor="#cccccc">
										<input name="HotelId"     type=<%if(imedia_user==0){%>hidden<%}else{%>text<%}%> id="HotelId" value="<%= hotelid %>" size=10 maxlength=10>
										<input name="col_qid_max" type=hidden id="QidMax" value="<%=i%>">
										<input name="regsubmit1"  type=button value="下書き保存（非掲載）" onClick="if (validation_range()){MM_openInput(<%= qid %>,1)}">
										<input name="regsubmit2"  type=button value="保存（掲載）" onClick="if (validation_range()){MM_openInput(<%= qid %>,0)}">
								</td>
							</tr>
							</form>
<%
    if (!AnswerFlag && qid != 0)
    {
%> 
							<tr>
								<td><img src="../../common/pc/image/spacer.gif" width="480" height="16"></td>
							</tr>
							<tr valign="middle">
								<td align="center" valign="middle" bgcolor="#cccccc">
									<input name="regsubmit3"  type=button value="削除" onClick="location.href='qaedit_delete.jsp?Id=<%=qid %>'">
								</td>
							</tr>
<%
    }
%>
						</table>
						<!-- アンケートのテーブル ここまで -->
						</div>
					</td>
				</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2"><img src="../../common/pc/image/spacer.gif" width="480" height="50"></td>
		</tr>
		</table>
	</td>
</tr>
<tr valign="bottom">
	<td>
		<!-- copyright テーブル -->
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="center" valign="middle">
				<div class="copyright">
					Copy right &copy; almex inc, All Rights Reserved
				</div>
			</td>
		</tr>
		<tr>
			<td><img src="../../common/pc/image/spacer.gif" width="480" height="20"></td>
		</tr>
		</table>
	</td>
</tr>
</table>
</body>
</html>
