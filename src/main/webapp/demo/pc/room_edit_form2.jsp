<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="java.sql.*" %><%@ page import="java.text.*" %><%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %><%@ page import="jp.happyhotel.common.HotelElement" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>

<%
    }
    // ホテルID取得
	String hotelid = request.getParameter("HotelId");

if  (hotelid.compareTo("all") == 0)
{
	hotelid = request.getParameter("HotelId");
}
    NumberFormat  nf3;
    nf3    = new DecimalFormat("000");

    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);
    boolean EditFlag      = false;
    int       last_update = nowdate; 
    boolean   NewFlag   = false;

    String id = request.getParameter("Id");
    String header_msg = "";
    String query;
    boolean mobile = false;
    DbAccess  db = new DbAccess();

    int       trialDate = HotelElement.getTrialDate(hotelid);
    boolean   reNewFlag = false;
    int       startDate = 0;

    int       room_no;
    int       disp_flg;
    java.util.Date start = new java.util.Date();
    java.util.Date end = new java.util.Date();

    String    room_name           = "";
    String    equipment           = "";
    int       rank                = 0; //部屋ﾗﾝｸ
    String    image_pc            = ""; //携帯用画像リンク
    String    image_mobile        = ""; //PC用画像リンク
    String    movie_mobile        = ""; //携帯用動画リンク
    String    image_thumb         = ""; //PC用サムネイル
    String    memo                = ""; //PC用備考
    boolean   image_pc_exist      = false;
    boolean   image_mobile_exist  = false;
    boolean   movie_mobile_exist  = false;
    boolean   image_thumb_exist   = false;

    room_no = 0;
    disp_flg = 1;
    String col_room_no = "";

    if( id.compareTo("0") == 0 )
    {
        header_msg = header_msg + " 新規作成";
        NewFlag = true;
    }
    else
    {
        query = "SELECT * FROM room WHERE hotelid='" + hotelid + "'";
        query = query + " AND id=" + id;

        // SQLクエリーの実行
        ResultSet result = db.execQuery(query);
        if( result.next() != false )
        {
            header_msg = header_msg + " 更新";

            room_no = result.getInt("room_no");
            disp_flg = result.getInt("disp_flg");
            start = result.getDate("start_date");
            end = result.getDate("end_date");
            room_name    = result.getString("room_name");
            equipment    = result.getString("equipment");
            rank         = result.getInt("rank");
            image_pc     = result.getString("image_pc");
            image_mobile = result.getString("image_mobile");
            movie_mobile = result.getString("movie_mobile");
            image_thumb  = result.getString("image_thumb");
            memo         = result.getString("memo");
            last_update  = result.getInt("last_update");
            if (room_no < 100)
            {
                col_room_no  = nf3.format(room_no);
            }
            else
            {
                col_room_no  = Integer.toString(room_no);
            }
        }
        else
        {
            header_msg = header_msg + " 新規作成";
            NewFlag = true;
        }

        db.close();
    }

    startDate = (start.getYear()+1900)*10000 + (start.getMonth()+1)*100+ start.getDate();

    if   (last_update != nowdate) EditFlag = true;

    //既存の画像の個数算出
    int subImg_count = image_pc.split("image/r").length - 2;
    if (image_pc.compareTo("") == 0 || image_pc.compareTo(" ") == 0) //新規の場合
    {
        subImg_count = 0;
    }

    if ((startDate < trialDate || NewFlag) && trialDate != 99999999)
    {
        if (subImg_count == -1)
        {
            image_pc = "<div id=\"roomImages\"><img src=\"/contents/%HOTELID%/img/nowprinting.png\" alt=\"%ROOMTITLE%\" /></div>";
        }
        else
        {
            image_pc = "<div id=\"roomImages\">";
            image_pc = image_pc+"<img src=\"/contents/%HOTELID%/img/%ROOMNO%.png\" alt=\"%ROOMTITLE%\" /><br>";
            for (int i = 2; i <= subImg_count+1; i++)
            {
                image_pc = image_pc+"<a href=\"/contents/%HOTELID%/img/%ROOMNO%-"+i+".png\" class=\"img-popup\" style=\"max-width: calc(100%/"+(subImg_count==1?2:subImg_count)+") !important\"><img src=\"/contents/%HOTELID%/img/%ROOMNO%-"+i+".png\" style=\"width:100%\" /></a>";
            }
           image_pc = image_pc+"</div>";
        }
        EditFlag = true;
    }
    else if (image_pc.compareTo("") == 0 || image_pc.compareTo(" ") == 0)
    {
        image_pc = "<table width='400' border='0' cellspacing='0' cellpadding='0'>\r\n";
        image_pc = image_pc+"<tr>\r\n";
        image_pc = image_pc+"<td height='12'><img src='image/spacer.gif' width='100' height='12'></td>\r\n";
        image_pc = image_pc+"</tr>\r\n";
        image_pc = image_pc+"<tr>\r\n";
        image_pc = image_pc+"<td><img src='image/r%ROOMNO%.jpg' width='400' border='0' alt='%ROOMTITLE%'></td>\r\n";
        image_pc = image_pc+"</tr>\r\n";
        image_pc = image_pc+"<tr>\r\n";
        image_pc = image_pc+"<td><img src='image/spacer.gif' width='100' height='12'></td>\r\n";
        image_pc = image_pc+"</tr>\r\n";
        image_pc = image_pc+"</table>\r\n";
    }
    else
    {
        image_pc_exist = true;
    }

    String image_mobile_temp = "";
    if (image_mobile.compareTo("") == 0 || image_mobile.compareTo(" ") == 0)
    {
        image_mobile_temp = "rooml.jsp?HotelId=%HOTELID%☆部屋画像\r\n";
    }
    else
    {
        image_mobile_exist = true;
    }

    String movie_mobile_temp = "";
    if (movie_mobile.compareTo("") == 0 || movie_mobile.compareTo(" ") == 0)
    {
        movie_mobile_temp = "<object declare id='room.movie' data='image/r%ROOMNO%.3gp' type='video/3gpp'>\r\n";
        movie_mobile_temp = movie_mobile_temp + "<param name='count' value='0' valuetype='data'>\r\n";
        movie_mobile_temp = movie_mobile_temp + "</object>\r\n";
        movie_mobile_temp = movie_mobile_temp + "<a href='#room.movie'>☆部屋動画</a>\r\n";
    }
    else
    {
        movie_mobile_exist = true;
    }
    if ((startDate < trialDate || NewFlag) && trialDate != 99999999)
    {
        image_thumb = "<a href=\"/%HOTELID%/room/detail/%ROOMNO%/\" class=\"link1\">";
        if (subImg_count == -1)
        {
            image_thumb = image_thumb +  "<img src=\"/contents/%HOTELID%/img/nowprinting_s.png\" width=\"100\" class=\"thumbroom\" alt=\"%ROOMTITLE%\">";
        }
        else
        {
            image_thumb  = image_thumb + "<img src=\"/contents/%HOTELID%/img/thumb%ROOMNO%.png\" width=\"100\" class=\"thumbroom\" alt=\"%ROOMTITLE%\">";
        }
        image_thumb = image_thumb + "</a><br><a href=\"/%HOTELID%/room/detail/%ROOMNO%/\" class=\"link1\">%ROOMNAME%</a><br>";
        EditFlag = true;
    }
    else if (image_thumb.compareTo("") == 0 || image_thumb.compareTo(" ") == 0)
    {
        image_thumb = "<a href='room%ROOMNO%.jsp' class='link1'><img src='image/thumb%ROOMNO%.jpg' width='100' class='thumbroom' alt='%ROOMTITLE%'></a><br>\r\n";
        image_thumb = image_thumb + "<a href='room%ROOMNO%.jsp' class='link1'>%ROOMNAME%</a><br>\r\n";
    }
    else
    {
        image_thumb_exist = true;
    }


    if (trialDate != 99999999)
    {
        if (trialDate > startDate)
        {
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

    if (isNaN(document.form1.col_room_no.value) || document.form1.col_room_no.value == 0)
    {
        alert("部屋番号を正しく入力してください");
        return false;
	}
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
function ImageSet(){
   var  image_mobile_temp = "rooml.jsp?HotelId=%HOTELID%☆部屋画像\r\n";

	document.form1.col_image_mobile.value = image_mobile_temp;
}
function MovieSet(){
   var  movie_mobile_temp = "<object declare id='room.movie' data='image/r%ROOMNO%.3gp' type='video/3gpp'>\r\n";
        movie_mobile_temp = movie_mobile_temp + "<param name='count' value='0' valuetype='data'>\r\n";
        movie_mobile_temp = movie_mobile_temp + "</object>\r\n";
        movie_mobile_temp = movie_mobile_temp + "<a href='#room.movie'>☆部屋動画</a>\r\n";

	document.form1.col_movie_mobile.value = movie_mobile_temp;
}
function colImgSet(obj){
	var count = parseInt(obj.value)-1;
    var image_pc;
    if (count == -1)
    {
        image_pc = "<div id=\"roomImages\"><img src=\"/contents/%HOTELID%/img/nowprinting.png\" alt=\"%ROOMTITLE%\" /></div>";
    }
    else
    {
        image_pc = "<div id=\"roomImages\">";
        image_pc += "<img src=\"/contents/%HOTELID%/img/%ROOMNO%.png\" alt=\"%ROOMTITLE%\" /><br>";
        for (var i = 2; i <= count+1; i++)
        {
            image_pc += "<a href=\"/contents/%HOTELID%/img/%ROOMNO%-"+i+".png\" class=\"img-popup\" style=\"max-width: calc(100%/"+count+") !important\"><img src=\"/contents/%HOTELID%/img/%ROOMNO%-"+i+".png\" style=\"width:100%\" /></a>";
        }
        image_pc +="</div>";
    }
    document.getElementById("col_image_pc").value = image_pc;


    var image_thumb = "<a href=\"/%HOTELID%/room/detail/%ROOMNO%/\" class=\"link1\">";
    if (count == -1)
    {
        image_thumb = image_thumb +  "<img src=\"/contents/%HOTELID%/img/nowprinting_s.png\" width=\"100\" class=\"thumbroom\" alt=\"%ROOMTITLE%\">";
    }
    else
    {
        image_thumb  = image_thumb + "<img src=\"/contents/%HOTELID%/img/thumb%ROOMNO%.png\" width=\"100\" class=\"thumbroom\" alt=\"%ROOMTITLE%\">";
    }
    image_thumb = image_thumb + "</a><br><a href=\"/%HOTELID%/room/detail/%ROOMNO%/\" class=\"link1\">%ROOMNAME%</a><br>";
    document.getElementById("col_image_thumb").value = image_thumb;
}
function colImgChange(obj){
	var image_pc = obj.value;
    document.getElementById("subImgCount").value = image_pc.split("<img src=\"/contents/%HOTELID%/img/%R").length-1;
}

function roomDeleteChk(){
	//削除対象部屋に予約が入っているかチェック
	var collectTime = new Date().getTime();//IEキャッシュ対策
	new Ajax.Request( 'roomrsvinfo_json.jsp',
		{
			method: 'get',
			parameters: 'hotenaviId=<%= hotelid %>&seq=<%= room_no %>&t='+ collectTime,
			onComplete: roomDelete
		}
     )
}
function roomDelete(req){
	var retData = eval( '(' + req.responseText + ')' );
	var exist = retData.RsvExist.Exist;
	if(exist)
	{
		alert( "部屋番号:<%= room_no %>には予約が入っているため、削除できません");
	}
	else
	{
		//予約が入っていなければ、削除
		document.formDel.submit();
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

<script type="text/javascript" src="room2.js"></script>
<script type="text/javascript" src="tohankaku.js"></script>
<script type="text/javascript" src="scripts/prototype.js"></script>

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
                      <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
		                  
                          <td width="30%" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="保存" onClick="if (validation_range()){MM_openInput('input', document.form1.hotelid.value, <%= room_no %>, <%= id %>)}">
						  <input name="BackUp" type="checkbox" value="1" <%if (reNewFlag){ %>checked <% } %> >Backupを残す
						  </td>
						  <td width="70%" align="center" valign="middle" bgcolor="#969EAD"><% if (!ownerinfo.DbLoginUser.equals("i-enter")){%>
コピー先ID:
						  <input name="hotelid" type=text value="<%= hotelid %>" >
						<input name="regsubmit" type=button value="コピー" onClick="MM_openInput('input', document.form1.hotelid.value, document.form1.col_room_no.value, 0)">
						<%}%>	</td>
                        </tr>
                      </table>
                      </td>
                    </tr>
                  <tr align="left">
                    <td>
                      <div align="left">
                        部屋番号：
                        <input name="old_room_no" type="hidden" value="<%= room_no %>" >
                        <input name="col_room_no" type="text" size="3" value="<%= room_no %>" onChange="document.form1.col_room_name.value=document.form1.col_room_name.value.replace(document.form1.old_room_no.value,document.form1.col_room_no.value);document.form1.old_room_no.value=document.form1.col_room_no.value;">

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
                    <td height="24" bgcolor="#666666"><strong><font color="#FFFFFF">&nbsp;表示期間</font></strong>
                      <input name="col_start_yy" type="text" size="4" maxlength="4" value="<%= startYY %>" onChange="setDayRange(this);">
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
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td height="24" bgcolor="#666666"><strong>&nbsp;<font color="#FFFFFF">
                        部屋名称：<input name="col_room_name" type="text" size="30" value="<%= room_name %>" onChange="<%if(EditFlag){%>if (document.form1.old_room_name.value != '') if ( document.form1.old_room_name.value != document.form1.col_room_name.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>">
                                  <input name="old_room_name" type="hidden" value="<%= room_name %>" >
                        部屋ランク：<input name="col_rank" type="text" size="2" maxlength="2" style="ime-mode:disable;text-align:right;" value="<%=rank %>"   onchange="<%if(EditFlag){%>if (document.form1.old_rank.value != '') if ( document.form1.old_rank.value != document.form1.col_rank.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>">
                                    <input name="old_rank" type="hidden" value="<%=rank %>">
                        </font></strong>
                    </td>
                  </tr>
                  <tr align="left">
                    <td>
                      <table>
                      <tr>
                        <td rowspan="2">設備<br>
                           <textarea class=size name=col_equipment rows=20 cols=30  onchange="<%if(EditFlag){%>if (document.form1.old_equipment.value != '' || document.form1.old_equipment.value != ' ') if ( document.form1.old_equipment.value != document.form1.col_equipment.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= equipment %></textarea>
                           <input name=old_equipment type="hidden" value="<%= equipment %>"><br>
						   部屋備考<font size="2">（入力無⇒部屋ランク備考を表示）</font><br>
                           <textarea class=size name=col_memo rows=5 cols=30  onchange="<%if(EditFlag){%>if (document.form1.old_memo.value != '' || document.form1.old_memo.value != ' ') if ( document.form1.old_memo.value != document.form1.col_memo.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= memo %></textarea>
                           <input name=old_memo type="hidden" value="<%= memo %>">
                        </td>


                        <td>部屋画像（PC)<%if(!image_pc_exist){%><font color=red>　*現在未登録です</font><%}%><span style="display:none"><%=image_pc%><%=image_pc.indexOf("<img src=\"/contents/%HOTELID%/img/%")%></span>
								<% if (image_pc.indexOf("roomImages")!=-1){
                                   int imgCount=image_pc.split("<img src=\"/contents/%HOTELID%/img/%").length-1;
                                 %>　画像数 <select id="subImgCount" onChange="colImgSet(this);"><% for(int i = 0;i<7;i++){%><option value=<%=i%><%if(imgCount==i){%> selected<%}%>><%=i%></option><%}%></select><%}%><br>
						<textarea class=size name=col_image_pc id=col_image_pc rows=14 cols=60 style="float:left;clear:both;" onChange="colImgChange(this);"><%= image_pc %></textarea>
                           <small>
                             %ROOMTITLE%<br>部屋名称を表示<%if(room_name.compareTo("")!=0){%>（<%=room_name%>）<%}else{%>（XX号室）<%}%><br><br>
                             %ROOMNO%<br>部屋番号を3桁以上で表示<%if(room_name.compareTo("")!=0){%>（<%=col_room_no%>）<%}else{%>（0XX）<%}%><br><br>
                             %ROOMNAME%<br>部屋名称から「号室」を省いて表示<%if(room_name.compareTo("")!=0){%>（<%=room_name.replace("号室","")%>）<%}else{%>（XX）<%}%><br>
                           </small><br><br></td>
					  <tr>
						<td>						
							部屋画像（サムネイル)<%if(!image_thumb_exist){%><font color=red>　*現在未登録です</font><%}%><br>
                           <textarea class=size name=col_image_thumb id=col_image_thumb rows=5 cols=60 ><%= image_thumb %></textarea><br>
							部屋画像（携帯)<%if(!image_mobile_exist){%><font color=red>　*現在未登録です</font><input type="button" value="Default" onClick="ImageSet();"><%}%><br>
                           <textarea class=size name=col_image_mobile rows=4 cols=60 ><%= image_mobile %></textarea><br>
						   部屋動画（携帯)<%if(!movie_mobile_exist){%><font color=red>　*現在未登録です</font><input type="button" value="Default" onClick="MovieSet();"><%}%><br>
                           <textarea class=size name=col_movie_mobile rows=4 cols=60 ><%= movie_mobile %></textarea><br>
                        </td>
                      </tr>
                      </table>
                    </td>
                  </tr>
                  <tr align="left">
                    <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="50%" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="保存" onClick="if (validation_range()){MM_openInput('input', '<%= hotelid %>', <%= room_no %>, <%= id %>)}"></td>
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
                  <form name="formDel" action="room_edit_delete2.jsp?HotelId=<%= hotelid %>&Id=<%= id %>" method="POST">
					<input name="regsubmit" type="button" value="削除" onclick="roomDeleteChk();">
                  </form>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td align="center">
                  <form action="room_list2.jsp?HotelId=<%= hotelid %>" method="POST">
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
