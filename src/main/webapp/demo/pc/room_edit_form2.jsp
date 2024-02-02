<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="java.sql.*" %><%@ page import="java.text.*" %><%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %><%@ page import="jp.happyhotel.common.HotelElement" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>

<%
    }
    // �z�e��ID�擾
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
    int       rank                = 0; //�����ݸ
    String    image_pc            = ""; //�g�їp�摜�����N
    String    image_mobile        = ""; //PC�p�摜�����N
    String    movie_mobile        = ""; //�g�їp���惊���N
    String    image_thumb         = ""; //PC�p�T���l�C��
    String    memo                = ""; //PC�p���l
    boolean   image_pc_exist      = false;
    boolean   image_mobile_exist  = false;
    boolean   movie_mobile_exist  = false;
    boolean   image_thumb_exist   = false;

    room_no = 0;
    disp_flg = 1;
    String col_room_no = "";

    if( id.compareTo("0") == 0 )
    {
        header_msg = header_msg + " �V�K�쐬";
        NewFlag = true;
    }
    else
    {
        query = "SELECT * FROM room WHERE hotelid='" + hotelid + "'";
        query = query + " AND id=" + id;

        // SQL�N�G���[�̎��s
        ResultSet result = db.execQuery(query);
        if( result.next() != false )
        {
            header_msg = header_msg + " �X�V";

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
            header_msg = header_msg + " �V�K�쐬";
            NewFlag = true;
        }

        db.close();
    }

    startDate = (start.getYear()+1900)*10000 + (start.getMonth()+1)*100+ start.getDate();

    if   (last_update != nowdate) EditFlag = true;

    //�����̉摜�̌��Z�o
    int subImg_count = image_pc.split("image/r").length - 2;
    if (image_pc.compareTo("") == 0 || image_pc.compareTo(" ") == 0) //�V�K�̏ꍇ
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
        image_mobile_temp = "rooml.jsp?HotelId=%HOTELID%�������摜\r\n";
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
        movie_mobile_temp = movie_mobile_temp + "<a href='#room.movie'>����������</a>\r\n";
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

//���͂���Ă���N�����A���̌��̍ŏI���t���Z�o
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
		
//���͂���Ă��錎�ɂ���āA�ŏI���t��ύX�B���肦�Ȃ����t�̏ꍇ�ɂ́A�ŏI���t�ɋ����I�ɕϊ��B
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

//���͂���Ă���N�����A���̌��̍ŏI���t���Z�o
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
		
//���͂���Ă��錎�ɂ���āA�ŏI���t��ύX�B���肦�Ȃ����t�̏ꍇ�ɂ́A�ŏI���t�ɋ����I�ɕϊ��B
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
        alert("�����ԍ��𐳂������͂��Ă�������");
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
			 alert("���Ԏw��i�J�n���t���I�����t�j�𐳂������͂��Ă�������");
			 document.form1.col_start_yy.focus();
			 return false;
			 }
	else
	  if  (input_yeare == input_years)
		{ 
		if (input_monthe < input_months)
		     {
			 alert("���Ԏw��i�J�n���t���I�����t�j�𐳂������͂��Ă�������");
			 document.form1.col_start_yy.focus();
			 return false;
			 }
		else if  (input_monthe == input_months && input_daye < input_days)
		     {
			 alert("���Ԏw��i�J�n���t���I�����t�j�𐳂������͂��Ă�������");
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
   var  image_mobile_temp = "rooml.jsp?HotelId=%HOTELID%�������摜\r\n";

	document.form1.col_image_mobile.value = image_mobile_temp;
}
function MovieSet(){
   var  movie_mobile_temp = "<object declare id='room.movie' data='image/r%ROOMNO%.3gp' type='video/3gpp'>\r\n";
        movie_mobile_temp = movie_mobile_temp + "<param name='count' value='0' valuetype='data'>\r\n";
        movie_mobile_temp = movie_mobile_temp + "</object>\r\n";
        movie_mobile_temp = movie_mobile_temp + "<a href='#room.movie'>����������</a>\r\n";

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
	//�폜�Ώە����ɗ\�񂪓����Ă��邩�`�F�b�N
	var collectTime = new Date().getTime();//IE�L���b�V���΍�
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
		alert( "�����ԍ�:<%= room_no %>�ɂ͗\�񂪓����Ă��邽�߁A�폜�ł��܂���");
	}
	else
	{
		//�\�񂪓����Ă��Ȃ���΁A�폜
		document.formDel.submit();
	}
}

-->
</script>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>PC�ŃC�x���g���ҏW</title>
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
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF"> �ҁ@�W</font></td>
              <td width="15" height="20"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="/common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ��������\ -->
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
                    <td><font color="#CC0000"><strong>�����̃y�[�W��ҏW���I������A�u�ۑ��v�{�^����K�������Ă�������</strong></font></td>
                    <td align="right">&nbsp;</td>
                    <td align="right">&nbsp;</td>
                  </tr>
                  <tr>
<%
    if( mobile != false )
    {
%>
                    <td><font color="#CC0000"><strong>���g�т̋@��ɂ��F��ύX����Ɛ���ɕ\������Ȃ��ꍇ������܂�</strong></font></td>
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
                  <!-- ���s�[�g�̈悱������ -->
                    <tr>
                      <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
		                  
                          <td width="30%" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="�ۑ�" onClick="if (validation_range()){MM_openInput('input', document.form1.hotelid.value, <%= room_no %>, <%= id %>)}">
						  <input name="BackUp" type="checkbox" value="1" <%if (reNewFlag){ %>checked <% } %> >Backup���c��
						  </td>
						  <td width="70%" align="center" valign="middle" bgcolor="#969EAD"><% if (!ownerinfo.DbLoginUser.equals("i-enter")){%>
�R�s�[��ID:
						  <input name="hotelid" type=text value="<%= hotelid %>" >
						<input name="regsubmit" type=button value="�R�s�[" onClick="MM_openInput('input', document.form1.hotelid.value, document.form1.col_room_no.value, 0)">
						<%}%>	</td>
                        </tr>
                      </table>
                      </td>
                    </tr>
                  <tr align="left">
                    <td>
                      <div align="left">
                        �����ԍ��F
                        <input name="old_room_no" type="hidden" value="<%= room_no %>" >
                        <input name="col_room_no" type="text" size="3" value="<%= room_no %>" onChange="document.form1.col_room_name.value=document.form1.col_room_name.value.replace(document.form1.old_room_no.value,document.form1.col_room_no.value);document.form1.old_room_no.value=document.form1.col_room_no.value;">

<%
    if( disp_flg == 1 )
    {
%>
                        <input name="col_disp_flg" type="checkbox" size="8" value="1" checked>�\������<br>
<%
    }
    else
    {
%>
                        <input name="col_disp_flg" type="checkbox" size="8" value="1">�\������<br>
<%
    }
%>

<br>
                      </div>
                    </td>
                  </tr>
                  <tr align="left">
                    <td height="24" bgcolor="#666666"><strong><font color="#FFFFFF">&nbsp;�\������</font></strong>
                      <input name="col_start_yy" type="text" size="4" maxlength="4" value="<%= startYY %>" onChange="setDayRange(this);">
                      �N
                      <input name="col_start_mm" type="text" size="2" maxlength="2" value="<%= startMM %>"  onchange="setDayRange(this);">
                      ��
                      <input name="col_start_dd" type="text" size="2" maxlength="2" value="<%= startDD %>"  onchange="setDayRange(this);">
                      ���`
                      <input name="col_end_yy" type="text" size="4" maxlength="4" value="<%if(id.compareTo("0") == 0){%>2999<%}else{%><%= end.getYear()+1900 %><%}%>"  onchange="setDayRange(this);">
                      �N
                      <input name="col_end_mm" type="text" size="2" maxlength="2" value="<%= end.getMonth()+1 %>"  onchange="setDayRange(this);">
                      ��
                      <input name="col_end_dd" type="text" size="2" maxlength="2" value="<%= end.getDate() %>"  onchange="setDayRange(this);">
                      ��
                    </td>
                  </tr>
                  <tr align="left">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td height="24" bgcolor="#666666"><strong>&nbsp;<font color="#FFFFFF">
                        �������́F<input name="col_room_name" type="text" size="30" value="<%= room_name %>" onChange="<%if(EditFlag){%>if (document.form1.old_room_name.value != '') if ( document.form1.old_room_name.value != document.form1.col_room_name.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>">
                                  <input name="old_room_name" type="hidden" value="<%= room_name %>" >
                        ���������N�F<input name="col_rank" type="text" size="2" maxlength="2" style="ime-mode:disable;text-align:right;" value="<%=rank %>"   onchange="<%if(EditFlag){%>if (document.form1.old_rank.value != '') if ( document.form1.old_rank.value != document.form1.col_rank.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>">
                                    <input name="old_rank" type="hidden" value="<%=rank %>">
                        </font></strong>
                    </td>
                  </tr>
                  <tr align="left">
                    <td>
                      <table>
                      <tr>
                        <td rowspan="2">�ݔ�<br>
                           <textarea class=size name=col_equipment rows=20 cols=30  onchange="<%if(EditFlag){%>if (document.form1.old_equipment.value != '' || document.form1.old_equipment.value != ' ') if ( document.form1.old_equipment.value != document.form1.col_equipment.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= equipment %></textarea>
                           <input name=old_equipment type="hidden" value="<%= equipment %>"><br>
						   �������l<font size="2">�i���͖��˕��������N���l��\���j</font><br>
                           <textarea class=size name=col_memo rows=5 cols=30  onchange="<%if(EditFlag){%>if (document.form1.old_memo.value != '' || document.form1.old_memo.value != ' ') if ( document.form1.old_memo.value != document.form1.col_memo.value){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>"><%= memo %></textarea>
                           <input name=old_memo type="hidden" value="<%= memo %>">
                        </td>


                        <td>�����摜�iPC)<%if(!image_pc_exist){%><font color=red>�@*���ݖ��o�^�ł�</font><%}%><span style="display:none"><%=image_pc%><%=image_pc.indexOf("<img src=\"/contents/%HOTELID%/img/%")%></span>
								<% if (image_pc.indexOf("roomImages")!=-1){
                                   int imgCount=image_pc.split("<img src=\"/contents/%HOTELID%/img/%").length-1;
                                 %>�@�摜�� <select id="subImgCount" onChange="colImgSet(this);"><% for(int i = 0;i<7;i++){%><option value=<%=i%><%if(imgCount==i){%> selected<%}%>><%=i%></option><%}%></select><%}%><br>
						<textarea class=size name=col_image_pc id=col_image_pc rows=14 cols=60 style="float:left;clear:both;" onChange="colImgChange(this);"><%= image_pc %></textarea>
                           <small>
                             %ROOMTITLE%<br>�������̂�\��<%if(room_name.compareTo("")!=0){%>�i<%=room_name%>�j<%}else{%>�iXX�����j<%}%><br><br>
                             %ROOMNO%<br>�����ԍ���3���ȏ�ŕ\��<%if(room_name.compareTo("")!=0){%>�i<%=col_room_no%>�j<%}else{%>�i0XX�j<%}%><br><br>
                             %ROOMNAME%<br>�������̂���u�����v���Ȃ��ĕ\��<%if(room_name.compareTo("")!=0){%>�i<%=room_name.replace("����","")%>�j<%}else{%>�iXX�j<%}%><br>
                           </small><br><br></td>
					  <tr>
						<td>						
							�����摜�i�T���l�C��)<%if(!image_thumb_exist){%><font color=red>�@*���ݖ��o�^�ł�</font><%}%><br>
                           <textarea class=size name=col_image_thumb id=col_image_thumb rows=5 cols=60 ><%= image_thumb %></textarea><br>
							�����摜�i�g��)<%if(!image_mobile_exist){%><font color=red>�@*���ݖ��o�^�ł�</font><input type="button" value="Default" onClick="ImageSet();"><%}%><br>
                           <textarea class=size name=col_image_mobile rows=4 cols=60 ><%= image_mobile %></textarea><br>
						   ��������i�g��)<%if(!movie_mobile_exist){%><font color=red>�@*���ݖ��o�^�ł�</font><input type="button" value="Default" onClick="MovieSet();"><%}%><br>
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
                          <td width="50%" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="�ۑ�" onClick="if (validation_range()){MM_openInput('input', '<%= hotelid %>', <%= room_no %>, <%= id %>)}"></td>
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
					<input name="regsubmit" type="button" value="�폜" onclick="roomDeleteChk();">
                  </form>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td align="center">
                  <form action="room_list2.jsp?HotelId=<%= hotelid %>" method="POST">
                  <input name="submit_ret" type=submit value="�߂�" >
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
      <!-- �����܂� -->
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
