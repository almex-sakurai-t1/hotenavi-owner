<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/menu_ini.jsp" %>

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
    String data_type = request.getParameter("DataType");
    String id = request.getParameter("Id");
    String header_msg = "";
    String query;
    boolean mobile = false;
    DbAccess  db = new DbAccess();

    int       disp_idx;
    int       disp_flg;

    java.util.Date start = new java.util.Date();
    java.util.Date end = new java.util.Date();
    String    title;
    String    title_color = "";
    String    contents    = "";
    String    decoration  = "";
    int       hpedit_id=0;
    int       parent_id=0;
    String    page_title = "";
    int       page_only_flg=0;
    int       priority_index=0;
    String    icon = "";
    String    title_icon = "";
    int       disp_type = 0;

    disp_idx = 0;
    disp_flg = 1;
    page_only_flg =0;
    
    boolean isNull_parent_id = true;
    boolean isNull_priority_index = true;
    
    title = "";

    if( data_type.compareTo("1") == 0 )
    {
        title_color = "#000000";
        header_msg = "PC版ビジター";
    }
    else if( data_type.compareTo("2") == 0 )
    {
        title_color = "#000000";
        header_msg = "PC版メンバー";
    }
    else if( data_type.compareTo("3") == 0 )
    {
        title_color = "#000000";
        header_msg = "携帯版ビジター";
        mobile = true;
    }
    else if( data_type.compareTo("4") == 0 )
    {
        title_color = "#000000";
        header_msg = "携帯版メンバー";
        mobile = true;
    }


    if( id.compareTo("0") == 0 )
    {
        header_msg =  "【新ホテナビ対応】" + header_msg + " 新規作成";
    }
    else
    {
        query = "SELECT * FROM menu_config WHERE hotelid='" + hotelid + "'";
        query = query + " AND data_type=" + data_type;
        query = query + " AND id=" + id;

        // SQLクエリーの実行
        ResultSet result = db.execQuery(query);
        if( result.next() != false )
        {
            header_msg =  "【新ホテナビ対応】" + header_msg + " 更新";

            disp_idx = result.getInt("disp_idx");
            disp_flg = result.getInt("disp_flg");
            start = result.getDate("start_date");
            end = result.getDate("end_date");
            title       = result.getString("title");
            contents    = result.getString("contents");
            decoration  = result.getString("decoration");
            title_color = result.getString("title_color");
            hpedit_id = result.getInt("hpedit_id");
            parent_id = result.getInt("parent_id");
            if(!result.wasNull()){
                isNull_parent_id = false;
            }
            page_title    = result.getString("page_title");
            page_only_flg = result.getInt("page_only_flg");
            priority_index = result.getInt("priority_index");
            icon = result.getString("icon");
            title_icon = result.getString("title_icon");
            disp_type = result.getInt("disp_type");
            if (disp_flg == 1 && page_only_flg != 1 && disp_type != 0)
            {
               icon =  icon.equals("") ? contents.replace("member/","") : icon.replace("https://happyhotel.jp/sc/image/icon/","").replace(".png","");
            }

            if(!result.wasNull()){
                if(priority_index != 0)
                {
                    isNull_priority_index = false;
                }
            }
        }
        else
        {
            header_msg = "【新ホテナビ対応】" + header_msg + " 新規作成";
        }

        db.close();
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

function iconDisp(){
   if (document.getElementById("col_disp_flg").checked && !document.getElementById("col_page_only_flg").checked && document.getElementById("col_disp_type").value != 0)
   {
       document.getElementById("col_icon").value="<%=icon%>";
       document.getElementById('icon_image').src='https://happyhotel.jp/sc/image/icon/'+document.getElementById("col_icon").value+'.png';
       document.getElementById('icon_image').style.display = "inline";
   }
   else
   {
       document.getElementById("col_icon").value="";
       document.getElementById('icon_image').style.display = "none";
   }
}
-->
</script>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>新ホテナビ対応　PC版イベント情報編集</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="/common/pc/style/access.css" rel="stylesheet" type="text/css">
<link href="/common/pc/style/room.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="menu_config.js"></script>
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
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF"> 編　集</font><%=data_type%></td>
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
                    <td align="right">&nbsp;</td>
                    <td align="right">&nbsp;</td>
                    <td align="right">&nbsp;</td>
                  </tr>
                  <tr>
                    <td><strong><%= header_msg %></strong></td>
                    <td align="right">&nbsp;</td>
                    <td width="20" align="right"><img src="/common/pc/image/spacer.gif" width="20" height="12"></td>
                  </tr>
                  <tr>
                    <td height="14" colspan="2"><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                    <td align="right">&nbsp;</td>
                  </tr>
                </table>
              </div>
              </td>
            </tr>
            <tr>
              <td colspan="2"><img src="/common/pc/image/spacer.gif" width="400" height="12"></td>
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
                          
                          <td width="50%" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="保存" onClick="if (validation_range()){MM_openInput('input', document.form1.hotelid.value, <%= data_type %>, <%= id %>)}"></td>
                          <td width="50%" align="center" valign="middle" bgcolor="#969EAD">コピー先ID:
                          <input name="hotelid" type=text value="<%= hotelid %>" >
                         <select name="datatype">
                        <option value = "1" <% if (data_type.compareTo("1") == 0) { %> selected <% } %> >PC版ビジター
                        <option value = "2" <% if (data_type.compareTo("2") == 0) { %> selected <% } %> >PC版メンバー
                        <option value = "11" <% if (data_type.compareTo("11") == 0) { %> selected <% } %> >PC版リンク
                        <option value = "12" <% if (data_type.compareTo("12") == 0) { %> selected <% } %> >PC版Official
                        </select>
                        <input name="regsubmit" type=button value="コピー" onClick="MM_openInput('input', document.form1.hotelid.value, document.form1.datatype.value, 0)">
                        </tr>
                      </table>
                      </td>
                    </tr>
                  <tr align="left">
                    <td>
                      <div align="left">
                        表示順：
                        <input name="col_disp_idx" type="text" size="8" value="<%= disp_idx %>" >
                        <input name="col_disp_flg" type="checkbox" size="8" value="1" id=col_disp_flg <%if( disp_flg == 1 ){%>checked<%}%> onchange="iconDisp();">表示する<br>
                      </div>
                    </td>
                  </tr>

                  <tr align="left">
                    <td height="24" bgcolor="#666666"><strong><font color="#FFFFFF">&nbsp;表示期間</font></strong></td>
                  </tr>
                  <tr align="left">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td class="size12">
                      <input name="col_start_yy" type="text" size="4" value="<%= start.getYear()+1900 %>" onChange="setDayRange(this);">
                      年
                      <input name="col_start_mm" type="text" size="2" value="<%= start.getMonth()+1 %>"  onchange="setDayRange(this);">
                      月
                      <input name="col_start_dd" type="text" size="2" value="<%= start.getDate() %>"  onchange="setDayRange(this);">
                      日～
                      <input name="col_end_yy" type="text" size="4" value="<%if(id.compareTo("0") == 0){%>2999<%}else{%><%= end.getYear()+1900 %><%}%>"  onchange="setDayRange(this);">
                      年
                      <input name="col_end_mm" type="text" size="2" value="<%= end.getMonth()+1 %>"  onchange="setDayRange(this);">
                      月
                      <input name="col_end_dd" type="text" size="2" value="<%= end.getDate() %>"  onchange="setDayRange(this);">
                      日
                    </td>
                  </tr>

                  <tr align="left">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>

                  <tr align="left">
                    <td height="24" bgcolor="#666666"><strong>&nbsp;<font color="#FFFFFF">メニュー：</font></strong></td>
                  </tr>
                  <tr align="left">
                    <td height="8"><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
                  </tr>
                  <tr align="left">
                    <td>
                      <small>
                      <strong>文字色：</strong>
                      <input name="col_title_color" type="text" size="8" value="<%= title_color %>" >
                      &nbsp;
                      </small>
                      <img src="/common/pc/image/color_btn.gif" name="color_btn1" width="100" height="22" align="absmiddle" id="color_btn1" onClick="MM_openBrWindow('/common/pc/menu_edit_select_color.html','色見本','width=240,height=180',0)" onMouseOver="MM_swapImage('color_btn1','','/common/pc/image/color_btn_o.gif',1)" onMouseOut="MM_swapImgRestore()">
                      <small>TAGを入力する場合は、「"」は使わず「'」を使用すること
                      </small>
                    </td>
                  </tr>
                  <tr align="left">
                    <td height="8"><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td>タイトル　　<input name="col_title" type="text" size="100" value="<%= title %>" ></td>
                  </tr>
                  <tr align="left">
                    <td height="8"><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td>飾　　り　　<input name="col_decoration" type="text" size="100" value="<%= decoration %>"></td>
                  </tr>
                  <tr align="left">
                    <td height="8"><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td>コンテンツ&nbsp;<input name="col_contents" type="text" size="32" value="<%= contents %>"></td>
                  </tr>
                  <tr align="left">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td>HP編集メニュー&nbsp;
                        <select name="col_hpedit_id">
                            <option value="0">変更なし
<% 
        for(int i = 1 ; i <= 37; i++ )
        {
            if (MenuNo[0][i-1] != 0)
            {
%>
                            <option value="<%=i%>" <%if (i==hpedit_id){%>selected<%}%>><%=Title[0][i-1]%>
<%
            }
        }
%>
                        </select>(オーナーサイトのHP編集メニュー名称をユーザーサイトと同じにする場合に指定）
                    </td>
                  </tr>
                  <tr align="left">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td>親メニューID&nbsp;<input name="col_parent_id" type="text" size="11" maxlength="11"  <%if( !isNull_parent_id ){%>value="<%= parent_id%>"<%}%>></td>
                  </tr>
                  <tr align="left">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td>ページタイトル　　<input name="col_page_title" type="text" size="100" value="<%= page_title %>" ></td>
                  </tr>
                  <tr align="left">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td>ページのみフラグ&nbsp;<input name="col_page_only_flg" type="checkbox" size="8" value="1" id=col_page_only_flg <%if( page_only_flg == 1 ){%>checked<%}%> onchange="iconDisp();">メニューに表示しない</td>
                  </tr>
                  <tr align="left">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left">
                    <td>優先表示有無/順序&nbsp;<input name="col_priority_index" type="text" size="2" maxlength="2"   <%if( !isNull_priority_index ){%>value="<%= priority_index%>"<%}%>></td>
                  </tr>
                  <tr align="left">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr> 
                  <tr align="left">
                    <td>SCアプリicon&nbsp;<input name="col_icon" type="text" size="10" id="col_icon" <%if (disp_flg == 1 && page_only_flg != 1 && disp_type != 0){%>value="<%=icon%>" onchange="document.getElementById('icon_image').src='https://happyhotel.jp/sc/image/icon/'+this.value+'.png';"<%}else{%>readonly value=""<%}%>>
<%
                if (disp_flg == 1 && page_only_flg != 1 && disp_type != 0)
                {
%>
                     <img src="https://happyhotel.jp/sc/image/icon/<%=icon%>.png" width=20px id="icon_image">（アイコン画像が404エラーになる場合は調整が必要です）
<%
                }
%>                    </td>
                  </tr>
                  <tr align="left">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr> 
                  <tr align="left">
                    <td>
                       SCアプリ表示&nbsp;<select name="col_disp_type" id="col_disp_type" onchange="iconDisp();">
                        <option value = "0" <% if (disp_type == 0) { %> selected <% } %> >アプリに表示しない
                        <option value = "1" <% if (disp_type == 1) { %> selected <% } %> >アプリに表示する：ツールバーなし（ネイティブぽく見せる）
                        <option value = "2" <% if (disp_type == 2) { %> selected <% } %> >アプリに表示する：ツールバーあり、外部ブラウザで開かない
                        <option value = "3" <% if (disp_type == 3) { %> selected <% } %> >アプリに表示する：ツールバーあり、外部ブラウザで開く
                        </select>
                    </td>
                  </tr>
                  <tr align="left">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr> 
                  <tr align="left">
                    <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="50%" align="center" valign="middle" bgcolor="#969EAD"><input name="regsubmit" type=button value="保存" onClick="if (validation_range()){MM_openInput('input', '<%= hotelid %>', <%= data_type %>, <%= id %>)}"></td>
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
                <td valign="top"></td>
              </tr>
              <tr>
                <td valign="top">&nbsp;</td>
                <td height="30" align="center" valign="middle" bgcolor="#969EAD">
                  <form action="menu_config_edit_delete.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=<%= id %>" method="POST">
                  <input name="submit_del" type=submit value="削除" >
                  </form>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td align="center">
                  <form action="menu_config_list.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>" method="POST">
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
