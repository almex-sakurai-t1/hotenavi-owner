<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="java.sql.*" %><%@ page import="java.net.*" %><%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %><%@ page import="jp.happyhotel.common.HotelElement" %><jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<%
    }
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    // ホテルID取得
	String hotelid = request.getParameter("HotelId");

if  (hotelid.compareTo("all") == 0)
{
	hotelid = request.getParameter("HotelId");
}
    String id = request.getParameter("Id");
    String header_msg = "";
    String query;
    boolean mobile = false;

    boolean system_exist  = false;
    boolean EditFlag      = false;
    int     max_disp_idx  = 0;
    int       trialDate = HotelElement.getTrialDate(hotelid);
    boolean   reNewFlag = false;
    int       startDate = 0;
    boolean   NewFlag   = false;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    try
    {
        query = "SELECT * FROM equipment WHERE hotelid='" + hotelid + "'";
        query = query + " ORDER BY disp_idx DESC";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            system_exist = true; //設備情報システムの入力あり
            max_disp_idx = result.getInt("disp_idx"); 
        }
    }
    catch( Exception e )
    {
        Logging.info("foward Exception e=" + e.toString() );
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    int       disp_idx;
    int       disp_flg;
    java.util.Date start = new java.util.Date();
    java.util.Date end = new java.util.Date();
    int       last_update = nowdate; 
    String    equipment_name    = "";
    String    detail            = "";
    String    class_name        = ""; //設備情報：設備名箇所クラス
    String    class_detail      = ""; //設備名箇所クラス
    String    memo_all          = ""; //全室設備の下に表示する備考
    String    memo_limit        = ""; //限定設備の下に表示する備考
    String    memo_all_mobile   = ""; //全室設備の下に表示する備考（携帯）
    String    memo_limit_mobile = ""; //限定設備の下に表示する備考（携帯）
    String    limit_name        = ""; //限定設備名称

    disp_idx = 0;
    disp_flg = 1;

    if( id.compareTo("0") == 0 )
    {
        header_msg = header_msg + " 新規作成";
        NewFlag = true;
        if (system_exist)
        {
            disp_idx   = max_disp_idx + 1;
        }
        else
        {
            equipment_name      = "全室完備";
            limit_name          = "限定設備";
        }
    }
    else
    {
        query = "SELECT * FROM equipment WHERE hotelid='" + hotelid + "'";
        query = query + " AND id=" + id;

        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            header_msg = header_msg + " 更新";

            disp_idx = result.getInt("disp_idx");
            disp_flg = result.getInt("disp_flg");
            start = result.getDate("start_date");
            end = result.getDate("end_date");
            equipment_name    = result.getString("equipment_name");
            detail            = result.getString("detail");
            class_name        = result.getString("class");
            class_detail      = result.getString("class_detail");
            memo_all          = result.getString("memo_all");
            memo_limit        = result.getString("memo_limit");
            memo_all_mobile   = result.getString("memo_all_mobile");
            memo_limit_mobile = result.getString("memo_limit_mobile");
            last_update       = result.getInt("last_update");
            limit_name        = result.getString("limit_name");
        }
        else
        {
            header_msg = header_msg + " 新規作成";
            NewFlag = true;
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    DBConnection.releaseResources(connection);

    startDate = (start.getYear()+1900)*10000 + (start.getMonth()+1)*100+ start.getDate();
    if ((startDate < trialDate || NewFlag) && trialDate != 99999999)
    {
        if (detail.indexOf("</a>") == -1)
        {
           detail = detail.trim().replace("　",",").replace(" ",",");
        }
        else
        {
           detail = (detail.trim()+",").replace("</a>","</a>,").replace("</a>, ","</a>,").replace("</a>,　","</a>,").replace("</a>,,","</a>");
        }
    }

    if   (last_update != nowdate) EditFlag = true;

    if (trialDate != 99999999)
    {
        if (trialDate >= startDate)
        {
            if (!NewFlag)
            reNewFlag = true;
            startDate = trialDate;
        }
    }
    int startYY = startDate/10000;
    int startMM = startDate/100%100;
    int startDD = startDate%100;
%>

<script type="text/javascript">
<!--
function setDayRange(obj){
	obj = obj.form;
	var years = parseInt(obj.col_start_yy.value,10);
	var months = parseInt(obj.col_start_mm.value,10);
	var days = parseInt(obj.col_start_dd.value,10);
	var yeare = parseInt(obj.col_end_yy.value,10);
	var monthe = parseInt(obj.col_end_mm.value,10);
	var daye = parseInt(obj.col_end_dd.value,10);

    if (isNaN(years))
	{
	obj.col_start_yy.value = <%= start.getYear()+1900 %>;
	years = <%= start.getYear()+1900 %>;
	}
	else
	{
	obj.col_start_yy.value = years;
	}
    if (years < 2000)
	{
	    obj.col_start_yy.value = 2000;
	}
    if (years > 2999)
	{
	    obj.col_start_yy.value = 2999;
	}

    if (isNaN(months))
	{
	obj.col_start_mm.value = <%= start.getMonth()+1 %>;
	months = <%= start.getMonth()+1 %>;
	}
	else
	{
	obj.col_start_mm.value = months;
	}
    if (months < 1)
	{
	    obj.col_start_mm.value = 1;
	}
    if (months > 12)
	{
	    obj.col_start_mm.value = 12;
	}

//入力されている年月より、その月の最終日付を算出
	var lastday_s = monthday_s(years,months);

    if (isNaN(days))
	{
	obj.col_start_dd.value = <%= start.getDate() %>;
	days = <%= start.getDate() %>;
	}
	else
	{
	obj.col_start_dd.value = days;
	}
    if (days < 1)
	{
	    obj.col_start_dd.value = 1;
	}
		
//入力されている月によって、最終日付を変更。ありえない日付の場合には、最終日付に強制的に変換。
	if (lastday_s < days) {
		obj.col_start_dd.value = lastday_s;
	}



    if (isNaN(yeare))
	{
	obj.col_end_yy.value = <%= end.getYear()+1900 %>;
	yeare = <%= end.getYear()+1900 %>;
	}
	else
	{
	obj.col_end_yy.value = yeare;
	}
    if (yeare < 2000)
	{
	    obj.col_end_yy.value = 2000;
	}
    if (yeare > 2999)
	{
	    obj.col_end_yy.value = 2999;
	}

    if (isNaN(monthe))
	{
	obj.col_end_mm.value = <%= end.getMonth()+1 %>;
	monthe = <%= end.getMonth()+1 %>;
	}
	else
	{
	obj.col_end_mm.value = monthe;
	}
    if (monthe < 1)
	{
	    obj.col_end_mm.value = 1;
	}
    if (monthe > 12)
	{
	    obj.col_end_mm.value = 12;
	}

//入力されている年月より、その月の最終日付を算出
	var lastday_e = monthday_e(yeare,monthe);

    if (isNaN(daye))
	{
	obj.col_end_dd.value = <%= end.getDate() %>;
	daye = <%= end.getDate() %>;
	}
	else
	{
	obj.col_end_dd.value = daye;
	}
    if (daye < 1)
	{
	    obj.col_end_dd.value = 1;
	}
		
//入力されている月によって、最終日付を変更。ありえない日付の場合には、最終日付に強制的に変換。
	if (lastday_e < daye) {
		obj.col_end_dd.value = lastday_e;
	}

}


function monthday_s(yearx,monthx){
	var lastday_s = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	if (((yearx % 4 == 0) && (yearx % 100 != 0)) || (yearx % 400 == 0)){
		lastday_s[1] = 29;
	}
	return lastday_s[monthx - 1];
}

function monthday_e(yearx,monthx){
	var lastday_e = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	if (((yearx % 4 == 0) && (yearx % 100 != 0)) || (yearx % 400 == 0)){
		lastday_e[1] = 29;
	}
	return lastday_e[monthx - 1];
}
function validation_range(){
	var input_years = parseInt(form1.col_start_yy.value,10);
	var input_months = parseInt(form1.col_start_mm.value,10);
	var input_days = parseInt(form1.col_start_dd.value,10);
		
	var input_yeare = parseInt(form1.col_end_yy.value,10);
	var input_monthe = parseInt(form1.col_end_mm.value,10);
	var input_daye = parseInt(form1.col_end_dd.value,10);


	if  (input_yeare < input_years)
		     {
			 alert("期間指定（開始日付≦終了日付）を正しく入力してください");
			 document.form1.col_start_yy.focus();
			 return false;
			 }
	else
	  if  (input_yeare == input_years)
		{ 
		if (input_monthe < input_months)
		     {
			 alert("期間指定（開始日付≦終了日付）を正しく入力してください");
			 document.form1.col_start_yy.focus();
			 return false;
			 }
		else if  (input_monthe == input_months && input_daye < input_days)
		     {
			 alert("期間指定（開始日付≦終了日付）を正しく入力してください");
			 document.form1.col_start_yy.focus();
			 return false;
			 }
		else {
		     return true;
			}
		}
	  else
	 	{
		     return true;
		}
		
}
-->
</script>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>PC版イベント情報編集</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="/common/pc/style/access.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="service2.js"></script>
<script type="text/javascript" src="tohankaku.js"></script>

</head>

<body bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
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
                    <td><font color="#CC0000"><strong>※このページを編集し終えたら、「保存」ボタンを必ず押してください</strong></font></td>
                    <td align="right">&nbsp;</td>
                    <td align="right">&nbsp;</td>
                  </tr>
                  <tr>
<%
    if( mobile != false )
    {
%>
                    <td><font color="#CC0000"><strong>※携帯の機種により色を変更すると正常に表示されない場合があります</strong></font></td>
<%
    }
    else
    {
%>
                    <td align="right">&nbsp;</td>
<%
    }
%>
                    <td align="right">&nbsp;</td>
                    <td align="right">&nbsp;</td>
                  </tr>
                  <tr>
                    <td><strong><%= header_msg %></strong></td>
                    <td align="right">&nbsp;</td>
                    <td width="20" align="right"><img src="/common/pc/image/spacer.gif" width="20" height="12"></td>
                  </tr>
                </table>
              </div>
              </td>
            </tr>
          </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
                <td>
                  <table width="100%" border="0" cellspacing="0" cellpadding="2">

                  <form name=form1 method=post>
                  <!-- リピート領域ここから -->
                    <tr>
                      <td colspan=2>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
		                  
                          <td width="30%" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="保存" onClick="if (validation_range()){MM_openInput('input', document.form1.hotelid.value, <%= disp_idx %>, <%= id %>)}">
						  <input name="BackUp" type="checkbox" value="1" <%if (reNewFlag){ %>checked <% } %>>Backupを残す
						  </td>
						  <td width="70%" align="center" valign="middle" bgcolor="#969EAD"><% if (!ownerinfo.DbLoginUser.equals("i-enter")){%>
コピー先ID:
						  <input name="hotelid" type=text value="<%= hotelid %>" >
						<input name="regsubmit" type=button value="コピー" onClick="MM_openInput('input', document.form1.hotelid.value, document.form1,col_disp_idx.value, 0)">
						<%}%></td>
                        </tr>
                      </table>
                      </td>
                    </tr>
                  <tr align="left">
                    <td colspan=2>
                      <div align="left">
<%
    if (disp_idx == 0)
    {
%>
						■全室共通設備
                        <input name="old_disp_idx" type="hidden" value="0">
                        <input name="col_disp_idx" type="hidden" value="0">
<%
    }
    else
    {
%>
	                    ■限定設備：表示順番
                        <input name="old_disp_idx" type="hidden" value="<%= disp_idx %>" >
                        <input name="col_disp_idx" type="text" size="3" value="<%= disp_idx %>" style="ime-mode:disable;text-align:right">
<%
    }
%>

<%
    if( disp_flg == 1 )
    {
%>
                        <input name="col_disp_flg" type="checkbox" size="8" value="1" checked>表示する<br>
<%
    }
    else
    {
%>
                        <input name="col_disp_flg" type="checkbox" size="8" value="1">表示する<br>
<%
    }
%>
<br>
                      </div>
                    </td>
                  </tr>
                  <tr align="left">
                    <td height="24" bgcolor="#666666" colspan=2><strong><font color="#FFFFFF">&nbsp;表示期間</font></strong>
                      <input name="col_start_yy" type="text" size="4" maxlength="4" value="<%= startYY %>" onchange="setDayRange(this);">
                      年
                      <input name="col_start_mm" type="text" size="2" maxlength="2" value="<%= startMM %>"  onchange="setDayRange(this);">
                      月
                      <input name="col_start_dd" type="text" size="2" maxlength="2" value="<%= startDD %>"  onchange="setDayRange(this);">
                      日～
                      <input name="col_end_yy" type="text" size="4" maxlength="4" value="<%if(id.compareTo("0") == 0){%>2999<%}else{%><%= end.getYear()+1900 %><%}%>"  onchange="setDayRange(this);">
                      年
                      <input name="col_end_mm" type="text" size="2" maxlength="2" value="<%= end.getMonth()+1 %>"  onchange="setDayRange(this);">
                      月
                      <input name="col_end_dd" type="text" size="2" maxlength="2" value="<%= end.getDate() %>"  onchange="setDayRange(this);">
                      日
                    </td>
                  </tr>
                  <tr align="left">
                    <td colspan=2><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td height="24" bgcolor="#666666" colspan=2>
                      <strong>&nbsp;<font color="#FFFFFF">全室完備タイトル：
                                  <input name="col_equipment_name" type="text" size="30" value="<%= equipment_name %>" onchange="if(document.form1.col_equipment_name.value =='')document.form1.col_equipment_name.value=document.form1.old_equipment_name.value;<%if(EditFlag){%>if (document.form1.old_equipment_name.value != '') if ( document.form1.old_equipment_name.value != document.form1.col_equipment_name.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>">
                                  <input name="old_equipment_name" type="hidden" value="<%= equipment_name %>" >
                    </font></strong><img src="/common/pc/image/spacer.gif" width="200" height="8">
                      &nbsp;<font color="#FFFFFF">クラス指定：
                                  <input name="col_class" type="text" size="30" value="<%= class_name %>">
                    </font>
                    </td>
                  </tr>
                  <tr align="left">
                    <td colspan=2><%if (disp_idx == 0){%>設 備<%}else{%>部屋番号<%}%></td>
                  </tr>
                  <tr align="left">
<%
    if (disp_idx == 0)
    {
%>
                    <td valign=top>
                        <div style="float:left;clear:both">
                        <textarea class=size name=col_detail rows=20 cols=40  onchange="<%if(EditFlag){%>if (document.form1.old_detail.value != '' || document.form1.old_detail.value != ' ') if ( document.form1.old_detail.value != document.form1.col_detail.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= detail %></textarea>
                        <input name=old_detail type="hidden" value="<%= detail %>"><br>
                        限定設備タイトル<br>
                        <input name="col_limit_name" type="text" size="30" value="<%= limit_name %>" onchange="if(document.form1.col_limit_name.value =='')document.form1.col_limit_name.value=document.form1.old_limit_name.value;<%if(EditFlag){%>if (document.form1.old_limit_name.value != '') if ( document.form1.old_limit_name.value != document.form1.col_limit_name.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>">
                        <input name="old_limit_name" type="hidden" value="<%= limit_name %>" >
                        </div>
					</td>
					<td>
                        <div style="float:right;clear:left">
                        全室設備の下に表示する備考(PC)<br>
                        <textarea class=size name=col_memo_all rows=4 cols=60  onchange="<%if(EditFlag){%>if (document.form1.old_memo_all.value != '' || document.form1.old_memo_all.value != ' ') if ( document.form1.old_memo_all.value != document.form1.col_memo_all.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= memo_all.replace("\"","&quot;") %></textarea>
                        <input name=old_memo_all type="hidden" value="<%= memo_all.replace("\"","&quot;") %>"><br>
						（携帯）<br>
                        <textarea class=size name=col_memo_all_mobile rows=4 cols=40  onchange="<%if(EditFlag){%>if (document.form1.old_memo_all_mobile.value != '' || document.form1.old_memo_all_mobile.value != ' ') if ( document.form1.old_memo_all_mobile.value != document.form1.col_memo_all_mobile.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= memo_all_mobile.replace("\"","&quot;") %></textarea>
                        <input name=old_memo_all_mobile type="hidden" value="<%= memo_all_mobile.replace("\"","&quot;") %>"><br>
                        限定設備の下に表示する備考（PC)<br>
                        <textarea class=size name=col_memo_limit rows=9 cols=60  onchange="<%if(EditFlag){%>if (document.form1.old_memo_limit.value != '' || document.form1.old_memo_limit.value != ' ') if ( document.form1.old_memo_limit.value != document.form1.col_memo_limit.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= memo_limit.replace("\"","&quot;") %></textarea>
                        <input name=old_memo_limit type="hidden" value="<%= memo_limit.replace("\"","&quot;") %>"><br>
						（携帯）<br>
                        <textarea class=size name=col_memo_limit_mobile rows=4 cols=40  onchange="<%if(EditFlag){%>if (document.form1.old_memo_limit_mobile.value != '' || document.form1.old_memo_limit_mobile.value != ' ') if ( document.form1.old_memo_limit_mobile.value != document.form1.col_memo_limit_mobile.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= memo_limit_mobile.replace("\"","&quot;") %></textarea>
                        <input name=old_memo_limit_mobile type="hidden" value="<%= memo_limit_mobile.replace("\"","&quot;") %>"><br>
						</div>
                    </td>
<%
    }
    else
    {
%>
                    <td colspan=2>
                        <div style="float:left;clear:both">
                        <textarea class=size name=col_detail rows=20 cols=40  onchange="<%if(EditFlag){%>if (document.form1.old_detail.value != '' || document.form1.old_detail.value != ' ') if ( document.form1.old_detail.value != document.form1.col_detail.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= detail %></textarea>
                        <input name=old_detail type="hidden" value="<%= detail %>">
                        </div>
                         <input name="col_limit_name"          type="hidden"  value="<%= limit_name.replace("\"","&quot;") %>">
                         <input name="col_memo_all"            type="hidden"  value="<%= memo_all.replace("\"","&quot;") %>">
                         <input name="col_memo_limit"          type="hidden"  value="<%= memo_limit.replace("\"","&quot;") %>">
                         <input name="col_memo_all_mobile"     type="hidden"  value="<%= memo_all_mobile.replace("\"","&quot;") %>">
                         <input name="col_memo_limit_mobile"   type="hidden"  value="<%= memo_limit_mobile.replace("\"","&quot;") %>">
                    </td>
<%
    }
%>
                  </tr>
<%
    if (disp_idx == 0)
    {
%>
                         <input name="col_class_detail" type="hidden"  value="<%= class_detail %>">
<%
    }
    else
    {
%>
                  <tr align="left">
                    <td  colspan=2>明細箇所のクラス指定</td>
                  </tr>
                  <tr align="left">
                    <td  colspan=2>
                         <input name="col_class_detail" type="text" size="30" value="<%= class_detail %>">
                    </td>
                  </tr>
<%
    }
%>
                  <tr align="left">
                    <td  colspan=2>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="50%" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="保存" onClick="if (validation_range()){MM_openInput('input', '<%= hotelid %>', <%= disp_idx %>, <%= id %>)}"></td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </form>
              </td>
              </tr>
              <tr>
                <td valign="top">&nbsp;</td>
                <td height="30" align="center" valign="middle" bgcolor="#969EAD">
                  <form action="service_edit_delete2.jsp?HotelId=<%= hotelid %>&Id=<%= id %>" method="POST">
                  <input name="submit_del" type=submit value="削除" >
                  </form>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td align="center">
                  <form action="service_list2.jsp?HotelId=<%= hotelid %>" method="POST">
                  <input name="submit_ret" type=submit value="戻る" >
                  </form>
                </td>
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
