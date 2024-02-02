<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />


<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<%
    }
%>

<%
    // ホテルID取得
	String hotelid = request.getParameter("HotelId");

if  (hotelid.compareTo("all") == 0)
{
	hotelid = request.getParameter("HotelId");
}
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);
    boolean EditFlag      = false;
    int       last_update = nowdate; 

    String rank  = request.getParameter("Rank");
    if (rank == null) rank = "0";
    int    rank_i = Integer.parseInt(rank);
    String id = request.getParameter("Id");
    String header_msg = "";
    String query;
    boolean mobile = false;

    boolean system_exist  = false;
    int     max_rank      = 0;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    try
    {
        query = "SELECT * FROM roomrank WHERE hotelid='" + hotelid + "'";
        query = query + " ORDER BY rank DESC";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            max_rank = result.getInt("rank");
            if (max_rank != 0)
            {
                max_rank++; //料金ランクの最大値（新規登録時・コピー時）
            }
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
    try
    {
        query = "SELECT * FROM roomrank WHERE hotelid='" + hotelid + "'";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            system_exist = true; //料金システムの入力あり
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
    String html_head = "";
    try
    {
        query = "SELECT html_head FROM hotel_element WHERE hotel_id='" + hotelid + "'";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            html_head = result.getString("html_head");
            if (html_head.indexOf("<script>location.href=\"https://happyhotel.jp\"</script>") != -1)
            {
                html_head = "";
            }
            html_head = html_head.replace("https://happyhotel.jp","#");
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
    if (html_head.equals(""))
    {
    html_head +="<link rel=\"stylesheet\" href=\"/contents/demoa/css/demoa-a.css\" type=\"text/css\" id=\"cssStyle\" />";
    html_head +="<link rel=\"stylesheet\" media=\"screen and (max-width: 480px) and (min-width: 0px)\" href=\"/contents/demoa/css/demoa-a-sp.css\" id=\"cssStyleSp\" />";
    html_head +="<link rel=\"stylesheet\" href=\"/contents/common/css/jquery.bxslider.css\" type=\"text/css\" />";
    html_head +="<script type=\"text/javascript\" src=\"/contents/common/js/jquery.bxslider.min.js\"></script>";
    html_head +="<script type=\"text/javascript\" src=\"/contents/common/js/style-a.js\"></script>";
    html_head +="<script type=\"text/javascript\" src=\"/contents/demoa/js/demoa-a.js\"></script>";
    }
    html_head = html_head.replace("pw=\"\"","pw=\""+hotelid+"\"");
    html_head = html_head.replace("pw=prompt(\" パスワードを入力してください \",\"\");","");
    html_head = html_head.replace("/contents/","https://www.hotenavi.com/contents/");

    int       disp_flg;
    java.util.Date start = new java.util.Date();
    java.util.Date end = new java.util.Date();
    String    rank_name     = "";
    String    class_name    = ""; //PC版クラス
    String    system        = "";
    String    memo          = ""; //備考
    String    memo_for_happyhotel = ""; //ハピホテ用備考
    String    system_mobile = ""; //携帯用料金情報
    String    title_class   = ""; //PC版料金表タイトルのクラス
    String    price_notation= ""; //税込記述
	int		  disp_index;
	
    disp_flg = 1;
	disp_index = 0;
	
    if( id.compareTo("0") == 0 )
    {
        header_msg = header_msg + " 新規作成";
        if(!system_exist) rank_name = "料金情報";
    }
    else
    {
        query = "SELECT * FROM roomrank WHERE hotelid='" + hotelid + "'";
        query = query + " AND rank=" + Integer.parseInt(rank);
        query = query + " AND id=" + id;
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            header_msg = header_msg + " 更新";

            disp_flg = result.getInt("disp_flg");
            start = result.getDate("start_date");
            end = result.getDate("end_date");
            rank_name     = result.getString("rank_name");
            class_name    = result.getString("class");
            system        = result.getString("system");
            memo          = result.getString("memo");
            memo_for_happyhotel = result.getString("memo_for_happyhotel");
            system_mobile = result.getString("system_mobile");
            last_update   = result.getInt("last_update");
            title_class   = result.getString("title_class");
            price_notation= result.getString("price_notation");
            if(price_notation.equals(""))
            {
                price_notation = "税込";
            }
			disp_index = result.getInt("disp_index");
        }
        else
        {
            header_msg = header_msg + " 新規作成";
            if(!system_exist) rank_name = "料金情報";
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    DBConnection.releaseResources(connection);
    if   (last_update != nowdate) EditFlag = true;
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

  if (form1.col_system.value.indexOf("\\") == -1)
	{
			alert("料金の先頭に\\マークを入力してないので、強制的につけます。再度確認してください。");
			document.form1.col_system.value = document.form1.col_system.value.replace(/'>\n/g,"'>\n\\");
			document.form1.col_system.focus();
			 return false;
	}

  if (form1.col_system.value.match(/>/g).length != form1.col_system.value.match(/</g).length)
  {
      alert("< と > に囲まれていない箇所がありますので確認してください。");
			document.form1.col_system.focus();
	  	return false;
  }

  if (form1.col_system.value.lastIndexOf("<") > form1.col_system.value.lastIndexOf(">"))
  {
      alert("< と > に囲まれていない箇所がありますので確認してください。");
			document.form1.col_system.focus();
	  	return false;
  }

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
function checkChange(elm)
{
<%
if(EditFlag)
{
%>
if (document.form1.elements["old_"+elm].value != '' || document.form1.elements["old_"+elm].value != ' ') 
   {
     if ( document.form1.elements["old_"+elm].value != document.form1.elements["col_"+elm].value)
     {
        document.form1.BackUp.checked=true;
     }
     else
     {
        document.form1.BackUp.checked=false;
     }
   }    
<%
}
%>
}
-->
</script>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>PC版イベント情報編集</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="/common/pc/style/access.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="roomrank2.js"></script>
<link rel="stylesheet" type="text/css" href="https://www.hotenavi.com/contents/common/css/hotel-common.css" />
<script type="text/javascript" src="https://www.hotenavi.com/contents/common/js/jquery.min.js"></script>
<script type="text/javascript" src="https://www.hotenavi.com/contents/common/js/hotenavi.js"></script>
<%=html_head%>
<style>
*:not(font){
   color:inherit;
}
</style>
</head>

<body id="room" bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
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
                      <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
		                  
                          <td width="30%" align="center" valign="middle" bgcolor="#969EAD">
							  <input name="regsubmit" type=button value="保存" onClick="if (validation_range()){MM_openInput('input', '<%=hotelid%>',document.form1.col_rank.value,<%= id %>)}">
							  <input name="BackUp" type="checkbox" value="1">Backupを残す
						  </td>
						  <td width="70%" align="center" valign="middle" bgcolor="#969EAD">
<%
    if( id.compareTo("0") != 0 )
    {
%><% if (!ownerinfo.DbLoginUser.equals("i-enter")){%>

						  コピー先ID:
						  <input name="hotelid" type=text value="<%= hotelid %>" >
						  部屋ランク:
						  <input name="copy_rank" type=text value="<%= max_rank %>" size="2" maxlength="2" >
						<input name="regsubmit" type=button value="コピー" onClick="MM_openInput('input', document.form1.hotelid.value,document.form1.copy_rank.value, 0)">
<%
}}
%>
							</td>
                        </tr>
                      </table>
                      </td>
                    </tr>
                  <tr align="left">
                    <td>
                      <div align="left">
<% if (id.compareTo("0") == 0){%>
                        部屋ランク
                        <input name="col_rank" type="text" size="2" maxlength="2" value="<%= max_rank %>" style="ime-mode: disabled;text-align:right;">
<% }else{%>
                        部屋ランク
                        <input name="col_rank" type="hidden" size="2" maxlength="2" value="<%= rank_i %>" style="ime-mode: disabled;text-align:right;"><%=rank_i%>
<%}%>
<%
    if( disp_flg == 1 )
    {
%>
                        <input name="col_disp_flg" type="checkbox" size="8" value="1" checked>表示する
<%
    }
    else
    {
%>
                        <input name="col_disp_flg" type="checkbox" size="8" value="1">表示する
<%
    }
%>
                      <input name="disp_index" type="hidden" value="<%= disp_index %>">
					  </div>
                    </td>
                  </tr>
                  <tr align="left">
                    <td height="24" bgcolor="#666666"><strong><font color="#FFFFFF">&nbsp;表示期間</font></strong>
                      <input name="col_start_yy" type="text" size="4" maxlength="4" value="<%= start.getYear()+1900 %>" onchange="setDayRange(this);">
                      年
                      <input name="col_start_mm" type="text" size="2" maxlength="2" value="<%= start.getMonth()+1 %>"  onchange="setDayRange(this);">
                      月
                      <input name="col_start_dd" type="text" size="2" maxlength="2" value="<%= start.getDate() %>"  onchange="setDayRange(this);">
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
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td height="24" bgcolor="#666666"><strong>&nbsp;<font color="#FFFFFF">名称：</font></strong><input name="col_rank_name" type="text" size="30" value="<%= rank_name %>"  onchange="<%if(EditFlag){%>if (document.form1.old_rank_name.value != '') if ( document.form1.old_rank_name.value != document.form1.col_rank_name.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>">
                                  <input name="old_rank_name" type="hidden" value="<%= rank_name %>" >
							<img src="/common/pc/image/spacer.gif" width="20" height="8">
                           <font color="#FFFFFF">PC版料金表タイトルクラス指定</font><input name="col_title_class" type="text" size="20" value="<%= title_class %>" style="ime-mode:disable">
					</td>
                  </tr>
                  <tr align="left">
                    <td>
					</td>
                  </tr>
                  <tr align="left">
                    <td>
                      <table style="width:100%">
                      <tr>
                        <td rowspan="2" style="vertical-align: top;width:30%">
						ランク別料金内容<br>
                           <textarea class=size id="col_system" name=col_system rows=20 style="width:100%" onchange="<%if(EditFlag){%>if (document.form1.old_system.value != '' || document.form1.old_system.value != ' ') if ( document.form1.old_system.value != document.form1.col_system.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= system %></textarea>
                           <input name=old_system type="hidden" value="<%= system %>"><br>
						税込表記
                           <input name="col_price_notation" type="text" size="16" maxlength="16" value="<%= price_notation %>" id="priceNotation" onchange="priceNotationChange();">
                           <input name="old_price_notation" type="hidden" value="<%= price_notation %>"><br>
							<br>
							<div  style="float:left;">
								【計算方法】<br>
								<input type="radio" name=calcMethod value=0 >税込8%→税込10%<br>
								<input type="radio" name=calcMethod value=1 >税抜→税込10%<br>
								<input type="radio" name=calcMethod value=2 >税込8%→税抜<br>
								<br>
								<br>
								<br>
								<input type="button" value="計算" id=calcButton style="display:none">
								<input type="button" value="戻す" id=resetButton style="display:none"><br>
							</div>
							<div style="float:right;">
								【端数処理】<br>
								<input type="radio" name=roundValue value=1>1円未満<br>
								<input type="radio" name=roundValue value=10>10円未満<br>
								<input type="radio" name=roundValue value=100>100円未満<br>
								<br>
								<input type="radio" name=roundMethod value=0>切捨て<br>
								<input type="radio" name=roundMethod value=1>四捨五入<br>
								<input type="radio" name=roundMethod value=2>切上げ<br>
							</div>
                        </td>
						<td style="vertical-align: top;width:40%" id="html">
						</td>
                        <td   style="vertical-align:top;width:30%">備考<br>
                           <textarea class=size id=col_memo name=col_memo rows=8 style="width:100%" onchange="checkChange('memo');"><%= memo %></textarea>
                           <textarea name=old_memo style="display:none"><%= memo %></textarea><br>
                           備考（ハピホテ用・htmlタグ除外）<br>
                           <textarea class=size id=col_memo_for_happyhotel name=col_memo_for_happyhotel rows=8 style="width:100%" onchange="checkChange('memo_for_happyhotel');"><%= memo_for_happyhotel %></textarea>
                           <textarea name=old_memo_for_happyhotel style="display:none"><%= memo_for_happyhotel %></textarea><br>
                           PC版各部屋詳細見出しクラス指定<br>
                           <input name="col_class" type="text" value="<%= class_name %>" style="ime-mode:disable;width:100%"><br>
                           携帯各部屋用料金<br>
                           <textarea class=size name=col_system_mobile rows=4 style="width:100%" onchange="<%if(EditFlag){%>if (document.form1.old_system_mobile.value != '' || document.form1.old_system_mobile.value != ' ') if ( document.form1.old_system_mobile.value != document.form1.col_system_mobile.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= system_mobile %></textarea>
                           <input name=old_system_mobile type="hidden" value="<%= system_mobile %>">
                           <input type=hidden name=col_class value="">&nbsp;
                        </td>
                      </tr>
                      </table>
                    </td>
                  </tr>
                  <tr align="left">
                    <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="50%" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="保存" onClick="if (validation_range()){MM_openInput('input', '<%= hotelid %>',document.form1.col_rank.value, <%= id %>)}"></td>
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
                  <form action="roomrank_edit_delete2.jsp?HotelId=<%= hotelid %>&Rank=<%= rank %>&Id=<%= id %>" method="POST">
                  <input name="submit_del" type=submit value="削除" >
                  </form>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td align="center">
                  <form action="roomrank_list2.jsp?HotelId=<%= hotelid %>" method="POST">
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
              <td><img src="/common/pc/image/tab_kado.gif" width="3" height="3"></td>
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
<script type="text/javascript">
 var col_system = "";
 var col_system_sv = "";
 var priceNotation = "（<%=price_notation%>）";

$(function() {
  system_disp();
});

$("#col_system").keyup(function() {
  system_disp();
});

$("input[name=calcMethod]").click(function() {
  calcInputCheck()
});

$("input[name=roundValue]").click(function() {
  calcInputCheck()
});

$("input[name=roundMethod]").click(function() {
  calcInputCheck()
});

function calcInputCheck()
{
    var flag = 0;
    $('input[name="calcMethod"]').each( function( index ) {
            if( this.checked ) flag++;
    })
    $('input[name="roundValue"]').each( function( index ) {
            if( this.checked ) flag++;
    })
    $('input[name="roundMethod"]').each( function( index ) {
            if( this.checked ) flag++;
    })
   if (flag == 3) $("#calcButton").css("display","");
}

$("#resetButton").click(function() {
  if (col_system_sv!=""){
     $('#col_system').val(col_system_sv);
      system_disp();
     $("#resetButton").css("display","none");
  }
});

$("#calcButton").click(function() {
  col_system_sv = $('#col_system').val();
  var str = $('#col_system').val();
  var idx=[];
  var yen = -1;
  var pre_i = 0;
  var resultStr = "";
  for (var i = 0; i < str.length;i++)
  {
    if (str.charAt(i) == "\\")
    {
      resultStr += str.substring(pre_i,i);
      tempStr = str.substr(i);
      var tempStrIndex = 1;
      for (var j = 1; j < tempStr.length;j++)
      {
          if (tempStr.charAt(j)!="," && isNaN(tempStr.charAt(j)))  break;
          tempStrIndex++;
      }
      var price = tempStr.substring(0,tempStrIndex);
      price = price.replace("\\","").replace(new RegExp(",","g"),"");
      resultStr += "\\"+cal(price);
      pre_i = i+tempStrIndex;
    }
  }
  resultStr+=str.substr(pre_i);

  $('#col_system').val(resultStr);
  system_disp();
  $("#resetButton").css("display","");
});

function cal(param){
   var price = parseInt(param);
   var calcMethod = $('input[name="calcMethod"]:checked').val();
   var roundValue = $('input[name="roundValue"]:checked').val();
   var roundMethod = $('input[name="roundMethod"]:checked').val();

   if (calcMethod == 0){ //税込8%→税込10%
      if (roundMethod == 0){
          price = Math.floor(price /(108/100) * (11/10) / roundValue )  * roundValue;
      }
      else if (roundMethod == 1){
          price = Math.round(price /(108/100) * (11/10) / roundValue )  * roundValue;
      }
      else if (roundMethod == 2){
          price = Math.ceil(price /(108/100) * (11/10) / roundValue )  * roundValue;
      }
   }
   if (calcMethod == 1){ //税抜→税込10%
      if (roundMethod == 0){
          price = Math.floor(price * 11 / (roundValue*10) )  * roundValue;
      }
      else if (roundMethod == 1){
          price = Math.round(price * 11 / (roundValue*10) )  * roundValue;
      }
      else if (roundMethod == 2){
          price = Math.ceil(price * 11 / (roundValue*10) )  * roundValue;
      }
   }
   if (calcMethod == 2){ //税込8%→税抜き
      if (roundMethod == 0){
          price = Math.floor(price /(108/100) / roundValue ) * roundValue;
      }
      else if (roundMethod == 1){
          price = Math.round(price /(108/100) / roundValue ) * roundValue;
      }
      else if (roundMethod == 2){
          price = Math.ceil(price /(108/100) / roundValue ) * roundValue;
      }
   }
   return String(price).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
}

function priceNotationChange(){
    col_system = $('#col_system').val();
    col_system = col_system.replace(new RegExp(priceNotation,"g"), "（"+$('#priceNotation').val()+"）");
    $('#col_system').val(col_system);
    priceNotation = "（"+ $('#priceNotation').val() + "）";
    system_disp();
}

function system_disp(){
    var html = "";
    col_system = $('#col_system').val();
    var systems=[];
    systems = col_system.split("【");
    for (var k = 1; k < systems.length;k++)
    {
        var subSystems = [];
        subSystems = systems[k].split("[");
        for (var i = 1; i < subSystems.length;i++)
        {
            var titles = [];
            titles = subSystems[i].split("]");
            if (titles.length >0)
            {
                var title = titles[0];
                if (title == "休憩")
                {
                    title = "ご　休　憩";
                }
                else if (title == "宿泊")
                {
                    title = "ご　宿　泊";
                }
                else if (title == "延長")
                {
                    title = "ご　延　長";
                }
                html += "<div class='roomRankDetail'>";
                html += "<div class='Title'>";
                html += title +"<a href=\"#\" class=\"displayChange linkOn\"></a>";
                html += "</div>";
                html += "<div class=\"item-table\">";
                var weeks = [];
                weeks = titles[1].split("◇");
                if (weeks.length != 0)
                {
                    for (var j = 0; j < weeks.length;j++)
                    {
                        html += "<dl class=\"roomDl\">";
                        var classes = [];
                        weeks[j] = weeks[j].replace(new RegExp("<BR>","g"), "\r\n");
                        weeks[j] = weeks[j].replace(new RegExp("<br>","g"), "\r\n");
                        classes = weeks[j].split("<");
                        if (classes.length > 1)
                        {

                            var className = [];
                            className = classes[1].split(">");
                            if (className.length > 1)
                            {
                                html += "<dt rowspan=\"1\" "+ className[0] + ">" + classes[0];
                            }
                            else
                            {
                                html += "<dt rowspan=\"1\">" + classes[0];
                            }
                            html += "</dt>";

                            var priceDetail = [];
                            priceDetail = classes[1].split(">");
                            if (className.length > 1)
                            {
                                html += "<dd rowspan=\"1\" "+ className[0] + ">";
                            }
                            else
                            {
                                html += "<dt rowspan=\"1\">";
                            }
                            if (priceDetail.length > 1)
                            {
                               html += priceDetail[1].replace(new RegExp("\\\\","g"),"").replace(new RegExp("（","g"),"円（"); 
                            }
                            else
                            {
                               html += priceDetail[0].replace(new RegExp("\\\\","g"),"").replace(new RegExp("（","g"),"円（"); 
                            }
                            html += "</dd>";
                        }
                        html += "</dl>";
                    }
                }
                html += "</div>";
                html += "</div>";
            }
        }
    }
    html = html.replace(new RegExp("\\r\\n","g"), "<br>"); //改行をhtmlの改行に戻す
    $('#html').html(html);
}

$("#col_memo").keyup(function() {
  memo_for_happyhotel_disp("memo");
});
$("#col_memo").blur(function() {
  const add_message = "※GW・お盆・年末年始等の連休期間は料金システムが異なる場合がございます。";
  var col_memo = $('#col_memo').val();
  if (col_memo.indexOf(add_message) == -1)
  {
    if (confirm("「"+add_message+"」をセットしますか？"))
    {
      col_memo += "\n" + add_message +"\n";
      $('#col_memo').val(col_memo);
      memo_for_happyhotel_disp("memo");
    }
  }
});
$("#col_memo_for_happyhotel").blur(function() {
  memo_for_happyhotel_disp("memo_for_happyhotel");
});

function memo_for_happyhotel_disp(elm){
  var col_memo = $('#col_'+elm).val();
  var col_memo_for_happyhotel = "";
  col_memo=col_memo.replace(/<br>/g,"\n");
  memoSplit = col_memo.split("\n");
  for (var i = 0;i < memoSplit.length;i++)
  {
      if (memoSplit[i].indexOf("<") == -1 && memoSplit[i].indexOf(">") == -1)
      {
        col_memo_for_happyhotel += memoSplit[i] +"\n";
      }
  }
  $('#col_memo_for_happyhotel').val(col_memo_for_happyhotel);
}

</script>

</body>
</html>
