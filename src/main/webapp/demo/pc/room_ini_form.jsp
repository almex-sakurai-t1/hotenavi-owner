<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="java.sql.*" %><%@ page import="java.text.*" %><%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %><%@ page import="jp.happyhotel.common.HotelElement" %>
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
    NumberFormat  nf3;
    nf3    = new DecimalFormat("000");

    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);
    boolean EditFlag      = false;
    int       last_update = nowdate; 
    boolean   NewFlag   = false;
    int       trialDate = HotelElement.getTrialDate(hotelid);

    String header_msg = "";
    String query;
    boolean mobile = false;
    DbAccess  db = new DbAccess();

    int       room_no = 0;
    int       disp_flg = 0;
    java.util.Date start = new java.util.Date();
    java.util.Date end = new java.util.Date();
    String    room_name           = "";
    String    equipment           = "";
    int       rank                = 0; //部屋ﾗﾝｸ
    String    room_text           = ""; //部屋番号テキスト
    String    image_pc            = ""; //携帯用画像リンク
    String    image_mobile        = ""; //PC用画像リンク
    String    image_thumb         = ""; //PC用サムネイル
    String    memo                = ""; //PC用備考
    boolean   image_pc_exist      = false;
    boolean   image_mobile_exist  = false;
    boolean   image_thumb_exist   = false;

    room_no = 0;
    disp_flg = 1;
    String col_room_no = "";

    header_msg = header_msg + " 新規作成";

    if   (last_update != nowdate) EditFlag = true;

    room_text = "↓こんな感じで入力してね\r\n";
    room_text = room_text + "◇ランクA\r\n";
    room_text = room_text + "201 202 203\r\n";
    room_text = room_text + "◇ランクB\r\n";
    room_text = room_text + "204 205 206\r\n";

    if (image_pc.compareTo("") == 0 || image_pc.compareTo(" ") == 0)
    {
        if (trialDate != 99999999)
        {
            int subImg_count = 3;
            image_pc = "<div id=\"roomImages\">";
            image_pc = image_pc+"<img src=\"/contents/%HOTELID%/img/%ROOMNO%.png\" alt=\"%ROOMTITLE%\" /><br>";
            for (int i = 2; i <= subImg_count+1; i++)
            {
                image_pc = image_pc+"<a href=\"/contents/%HOTELID%/img/%ROOMNO%-"+i+".png\" class=\"img-popup\" style=\"max-width: calc(100%/"+subImg_count+") !important\"><img src=\"/contents/%HOTELID%/img/%ROOMNO%-"+i+".png\" style=\"width:100%\" /></a>";
            }
           image_pc = image_pc+"</div>";
        }
        else
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
    }
    else
    {
        image_pc_exist = true;
    }
    if (image_mobile.compareTo("") == 0 || image_mobile.compareTo(" ") == 0)
    {
        image_mobile = "rooms.jsp?HotelId=%HOTELID%☆部屋画像\r\n";
        image_mobile = image_mobile+"rooml.jsp?HotelId=%HOTELID%☆部屋画像（大）\r\n";
    }
    else
    {
        image_mobile_exist = true;
    }
    if (image_thumb.compareTo("") == 0 || image_thumb.compareTo(" ") == 0)
    {
    if (trialDate != 99999999)
    {
        image_thumb = "<a href=\"/%HOTELID%/room/detail/%ROOMNO%/\" class=\"link1\"><img src=\"/contents/%HOTELID%/img/thumb%ROOMNO%.png\" width=\"100\" class=\"thumbroom\" alt=\"%ROOMTITLE%\"></a><br>";
        image_thumb = image_thumb + "<a href=\"/%HOTELID%/room/detail/%ROOMNO%/\" class=\"link1\">%ROOMNAME%</a><br>";
    }
    else
    {

        image_thumb = "<a href='room%ROOMNO%.jsp' class='link1'><img src='image/thumb%ROOMNO%.jpg' width='100' class='thumbroom' alt='%ROOMTITLE%'></a><br>\r\n";
        image_thumb = image_thumb + "<a href='room%ROOMNO%.jsp' class='link1'>%ROOMNAME%</a><br>\r\n";
    }
    }
    else
    {
        image_thumb_exist = true;
    }
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
                    <td><strong><%= header_msg %></strong></td>
                    <td align="right">
						<a href="check_roominfo.jsp?id=<%=hotelid%>" target="_blank" style="color:#7200ff;text-decoration: none;" onClick="window.open('check_roominfo.jsp?id=<%=hotelid%>&ftp=DCGXU5AC','','scrollbars=no,toolbar=no,status=yes,width=800,resizable=yes');return(false)">コンテンツ参照</a>
					</td>
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

                  <form name=form1 action="room_ini_confirm.jsp" method=post>
                  <tr align="left">
                    <td height="24" bgcolor="#666666"><strong><font color="#FFFFFF">&nbsp;表示期間</font></strong>
                      <input name="HotelId" type=hidden value="<%=hotelid%>">
                      <input name="col_start_yy" type="text" size="4" maxlength="4" value="<%= start.getYear()+1900 %>" onchange="setDayRange(this);">
                      年
                      <input name="col_start_mm" type="text" size="2" maxlength="2" value="<%= start.getMonth()+1 %>"  onchange="setDayRange(this);">
                      月
                      <input name="col_start_dd" type="text" size="2" maxlength="2" value="<%= start.getDate() %>"  onchange="setDayRange(this);">
                      日～
                      <input name="col_end_yy" type="text" size="4" maxlength="4" value="2999"  onchange="setDayRange(this);">
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
                    <td>
                      <table>
                      <tr>
                        <td rowspan="2">部屋番号<br>
                           <textarea class=size name=col_room_text rows=25 cols=30><%= room_text %></textarea><br>
							<small>
								※部屋ランク名の先頭には◇を入力。<br>
								※部屋番号は数字のみ入力。
							</small
                        </td>
                        <td>部屋画像（PC)
						<% if (image_pc.indexOf("roomImages")!=-1){
                              int imgCount=image_pc.split("<img src=\"/contents/%HOTELID%/img/%").length-1;
                             %>　画像数 <select id="subImgCount" onChange="colImgSet(this);"><% for(int i = 0;i<7;i++){%><option value=<%=i%><%if(imgCount==i){%> selected<%}%>><%=i%></option><%}%></select><%}%><br>
                           <textarea class=size name=col_image_pc id=col_image_pc rows=14 cols=60 style="float:left;clear:both;"><%= image_pc %></textarea>
                           <small>
                             %ROOMTITLE%<br>部屋名称を表示（XX号室）<br><br>
                             %ROOMNO%<br>部屋番号を3桁以上で表示（0XX）<br><br>
                             %ROOMNAME%<br>部屋名称から「号室」を省いて表示（XX）<br>
                           </small><br><br>
						</td>
					  <tr>
						<td>						
							部屋画像（サムネイル)<br>
                           <textarea class=size name=col_image_thumb  id=col_image_thumb rows=5 cols=60 ><%= image_thumb %></textarea><br>
							部屋画像（携帯)<br>
                           <textarea class=size name=col_image_mobile rows=4 cols=60 ><%= image_mobile %></textarea><br>
                        </td>
                      </tr>
                      </table>
                    </td>
                  </tr>
                  <tr align="left">
                    <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="50%" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=submit value="確認" onClick="return validation_range();"></td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
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
