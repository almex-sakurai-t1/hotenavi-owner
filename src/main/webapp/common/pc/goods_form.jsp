<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
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
    String loginhotel = (String)session.getAttribute("LoginHotelId");

    int     i  = 0;
    // ﾊﾟﾗﾒｰﾀ取得
    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    String paramCategoryId = ReplaceString.getParameter(request,"CategoryId");
    if  (paramCategoryId  == null)
    {
         paramCategoryId  = "0";
    }
    String paramSeq       = ReplaceString.getParameter(request,"Seq");
    if  (paramSeq         == null)
    {
         paramSeq         = "0";
    }
    int  seq              = Integer.parseInt(paramSeq);

    int       category_id      = 0;
    int       disp_idx         = 0;
    String    title            = "";
    String    title_color      = "";
    String    title_sub1       = "";
    String    title_sub1_color = "";
    String    msg              = "";
    int       point            = 0;
    int       count_flag       = 1;
    int       count_max        = 10;
    int       count_unit       = 1;
    String    picture_pc       = "";
    String    picture_gif      = "";
    int       disp_from        = 0;
    int       disp_to          = 0;
    int       suggest_flag     = 0;
    int       suggest_idx      = 0;


    String    query       = "";
    DateEdit  de          = new DateEdit();
    java.util.Date start  = new java.util.Date();
    java.util.Date end    = new java.util.Date();
    int       nowdate     = Integer.parseInt(de.getDate(2));
    boolean   EditFlag    = false;
    boolean   NewFlag     = false;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection                    = DBConnection.getConnection();

    if( seq == 0 )
    {
        NewFlag    = true;
    }
    else
    {
        try
        {
            query = "SELECT * FROM goods WHERE hotelid= ? ";
            query = query + " AND seq= ? ";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, seq);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                category_id      = result.getInt("category_id");
                disp_idx         = result.getInt("disp_idx");
                title            = result.getString("title");
                title_color      = result.getString("title_color");
                title_sub1       = result.getString("title_sub1");
                title_sub1_color = result.getString("title_sub1_color");
                msg              = result.getString("msg");
                point            = result.getInt("point");
                count_flag       = result.getInt("count_flag");
                count_max        = result.getInt("count_max");
                count_unit       = result.getInt("count_unit");
                picture_pc       = result.getString("picture_pc");
                picture_gif      = result.getString("picture_gif");
                disp_from        = result.getInt("disp_from");
                disp_to          = result.getInt("disp_to");
                suggest_flag     = result.getInt("suggest_flag");;
                suggest_idx      = result.getInt("suggest_idx");
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

    int               imedia_user = 0;
    int               level       = 0;
    // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,loginhotel);
        prestate.setInt(2,ownerinfo.DbUserId);
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

    int start_year  = disp_from / 10000;
    int start_month = disp_from / 100 % 100;
    int start_day   = disp_from % 100;
    int start_date  = start_year * 10000 + start_month * 100 + start_day;

    int end_year    = disp_to / 10000;
    int end_month   = disp_to / 100 % 100;
    int end_day     = disp_to % 100;
    int end_date    = end_year * 10000 + end_month * 100 + end_day;

    if (NewFlag)
    {
        start_date    = nowdate;
        start_date    = de.addDay(start_date,1);
        start_year    = start_date / 10000;
        start_month   =(start_date / 100) % 100;
        start_day     = start_date % 100;
        end_date      = nowdate;
        end_year      = 2999;
        end_month     =(end_date   / 100) % 100;
        end_day       = end_date   % 100;
    }
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>景品設定</title>
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
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
        <td width="3"  colspan=2>&nbsp;</td>
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
					<td align="left" colspan=3 valign="middle" style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="3">
						<strong>景品登録</strong>
						&nbsp;&nbsp;<strong><%if(NewFlag){%>新規作成<%}else{%>更新<%}%></strong>
					</td>
				</tr>
                  <tr>
                    <td class="red12"><strong>※このページを編集し終えたら、「この景品を保存」ボタンを必ず押してください</strong></td>
                    <td align="right" class="size12">&nbsp;</td>
                    <td align="right" class="size12">&nbsp;</td>
                  </tr>
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
                <td  colspan=2>
                  <table width="100%" border="0" cellspacing="0" cellpadding="2" >
                  <form name=form1 method=post>
                    <tr bgcolor="#FFFFFF">
                      <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="60%" align="center" valign="middle" class="size12" bgcolor="#969EAD">
							<input type="hidden" name="col_seq"         value="<%=seq%>">
							<input type="hidden" name="HotelId"         value="<%=hotelid%>">
						  </td>
                          <td width="40%" align="center" valign="middle" class="size12" bgcolor="#969EAD">
                          </td>
                        </tr>
                      </table>
                      </td>
                    </tr>
                  <tr align="left" bgcolor="#FFFFFF">
                    <td  class="size12">
                      <div align="left" class="size12" style="CLEAR: both; FLOAT: left;color:black">
                        表示期間：<input name="col_disp_flg" type="hidden" value="1">
                      <input name="col_start_yy" type="text" size="4" maxlength="4" value="<%= start_year %>" onChange="setDayRange(this,'<%= start_date %>','<%= end_date %>');" style="text-align:right;">
                      年
                      <input name="col_start_mm" type="text" size="2" maxlength="2" value="<%= start_month %>"  onchange="setDayRange(this,'<%= start_date %>','<%= end_date %>');" style="text-align:right;">
                      月
                      <input name="col_start_dd" type="text" size="2" maxlength="2" value="<%= start_day %>"  onchange="setDayRange(this,'<%= start_date %>','<%= end_date %>');" style="text-align:right;">
                      日～
                      <input name="col_end_yy" type="text" size="4" maxlength="4"  value="<%if(NewFlag){%>2999<%}else{%><%= end_year %><%}%>"  onchange="setDayRange(this,'<%= start_date %>','<%= end_date %>');" style="text-align:right;">
                      年
                      <input name="col_end_mm" type="text" size="2" maxlength="2" value="<%= end_month %>"  onchange="setDayRange(this,'<%= start_date %>','<%= end_date %>');" style="text-align:right;">
                      月
                      <input name="col_end_dd" type="text" size="2" maxlength="2" value="<%= end_day %>"  onchange="setDayRange(this,'<%= start_date %>','<%= end_date %>');" style="text-align:right;">
                      日
<%
            if(disp_from > nowdate || disp_to < nowdate)
            {
%>
						&nbsp;<font color="red">非表示</font>
<%
            }
%>

						<img src="/common/pc/image/spacer.gif" width="50" height="8">
						カテゴリー：<select name="col_category_id">
<%
    int     category_disp_idx   = 0;
    int     disp_sv             = 999;
    int     category_idx        = 0;
    query = "SELECT * FROM goods_category WHERE hotelid= ? ";
    query = query + " AND category_id!=0";
    query = query + " AND disp_to>= ? ";
    query = query + " ORDER BY disp_idx,seq DESC";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1, hotelid);
    prestate.setInt(2, nowdate);
    result      = prestate.executeQuery();

    while( result.next() != false )
    {
            category_disp_idx  =  result.getInt("disp_idx");
            category_idx       =  result.getInt("category_id");
        if (category_disp_idx  != disp_sv)
        {
            disp_sv      =  category_disp_idx;
%>
                        <option value="<%= category_idx %>" <% if(Integer.parseInt(paramCategoryId) == category_idx){%>selected<%}%>><%= result.getString("title") %></option>

<%
        }
    }
    DBConnection.releaseResources(result,prestate,connection);
%>
						</select>
                      </div>
                    </td>
                  </tr>
				<tr>
					<td align="center" valign="top" colspan="3">
						<table>
						<div class="honbun_margin honbun">
							<tr>
								<td align="left" valign="top">
									<table border="0" cellspacing="1" cellpadding="3" class="subtitlebar_linecolor">
										<tr class="honbun">
											<td  class="subtitlebar_basecolor subtitle_text" id="title_big_title" width="80">&nbsp;
												商品名</font>
											</td>
											<td  class="subtitlebar_basecolor subtitle_text" id="title_big_input" width="360">&nbsp;
												<input  name="col_title" type="text" size="20" maxlength="20" value="<%= title.replace("\"","&quot;") %>" onChange="title_input.innerHTML=document.form1.col_title.value;">
												<small><strong>文字色：</strong><input name="col_title_color" type="text" size="8" readonly value="<%= title_color %>" onchange=""></small>
												<img border="0" style="background-color:white" src="/common/pc/image/color_btn.gif" name="color_btn1" width="100" height="22" align="absmiddle" id="color_btn1" onClick="MM_openBrWindow('../../common/pc/event_edit_select_color.html','色見本','width=240,height=180',0);title_big_input.style.color=document.form1.col_title_color.value;title_big_title.style.color=document.form1.col_title_color.value" onMouseOver="MM_swapImage('color_btn1','','/common/pc/image/color_btn_o.gif',1)" onMouseOut="MM_swapImgRestore()">
											</td>
											<td  class="subtitlebar_basecolor subtitle_text" id="title_big" width="125">&nbsp;
												<font color="<%= title_color %>"><span id="title_input"><%= title %></span></font>
											</td>
											<td  class="subtitlebar_text" align="left" width="130" rowspan="4" valign="top">
												表示順：<input type="text" name="col_disp_idx" size=3 maxlength=3 value="<%= disp_idx %>" style="text-align:right;ime-mode:disabled">
												<hr>
												おすすめ：<input type="checkbox" name="col_suggest_flag" value="1" <%if(suggest_flag == 1){%>checked<%}%>>有<br/>
												おすすめ表示順：<input type="text" name="col_suggest_idx" size=3 maxlength=3 value="<%= suggest_idx %>" style="text-align:right;ime-mode:disabled">
												<hr>
												数量入力：<input type="checkbox" name="col_count_flag" value="1" <%if(count_flag == 1){%>checked<%}%>>有<br/>
												数量単位：<input type="text"     name="col_count_unit" size=3 maxlength=3 value="<%= count_unit %>" style="text-align:right;ime-mode:disabled">
												最大数量：<input type="text"     name="col_count_max"  size=3 maxlength=3 value="<%= count_max %>" style="text-align:right;ime-mode:disabled">
											</td>
										</tr>
										<tr class="honbun">
											<td  class="subtitlebar_text" id="title_1_title" width="80">&nbsp;
												消費ポイント
											</td>
											<td  class="subtitlebar_text" id="title_1_input" width="360">&nbsp;
												<input type="text" name="col_point"     size=5 maxlength=5 value="<%= point %>"      style="text-align:right;ime-mode:disabled">
											</td>
											<td  class="subtitlebar_text" id="msg_title_1" width="125" rowspan="2">&nbsp;
												<font color="<%= title_sub1_color %>"><span id="title_sub1_input"><%=title_sub1 %></span></font>
											</td>
										</tr>
										<tr class="honbun">
											<td  class="subtitlebar_text" id="title_1_title" width="80">&nbsp;
												サブタイトル
											</td>
											<td  class="subtitlebar_text" id="title_1_input" width="360">&nbsp;
												<input name="col_title_sub1" type="text" size="20" maxlength="20" value="<%= title_sub1.replace("\"","&quot;") %>" onChange="title_sub1_input.innerHTML=document.form1.col_title_sub1.value;">
												<small><strong>文字色：</strong><input name="col_msg1_title_color" type="text" size="8" readonly value="<%= title_sub1_color %>" onchange=""></small>
												<img border="0" style="background-color:white" src="/common/pc/image/color_btn.gif" name="color_btn1" width="100" height="22" align="absmiddle" id="color_btn1" onClick="MM_openBrWindow('../../common/pc/event_edit_select_color.html','色見本','width=240,height=180',1);title_1_input.style.color=document.form1.col_msg1_title_color.value;title_1_title.style.color=document.form1.col_msg1_title_color.value" onMouseOver="MM_swapImage('color_btn1','','/common/pc/image/color_btn_o.gif',1)" onMouseOut="MM_swapImgRestore()">
											</td>
										</tr>
										<tr class="honbun">
											<td  class="subtitlebar_text" width="80">&nbsp;
												画像<br><br><br>
												&nbsp;&nbsp;商品説明<br>
											</td>
											<td  class="subtitlebar_text" width="80" valign="top">
												&nbsp;ＰＣ<br>
												<input name="col_picture_pc" type="text" size="40" maxlength="50" value="<%= picture_pc %>" onChange="PictureSet();" style="ime-mode:disabled"><br>
												&nbsp;携帯<br>
												<input name="col_picture_gif" type="text" size="40" maxlength="50" value="<%= picture_gif %>"  style="ime-mode:disabled"><br>
												&nbsp;商品説明<br>
												<input name="col_msg" type="text" size="40" maxlength="40" value="<%= msg %>" onchange="msg_input.innerHTML=document.form1.col_msg.value;">
											</td>
<script type="text/javascript">
<!--
function PictureSet(){

var pict = document.form1.col_picture_pc.value;
if  (pict.indexOf("http://") < 0)
{
     pict = "http://www.hotenavi.com/<%=hotelid%>/" + pict;
}
var image_data = "<img src='" + pict + "' style='clear:both'>";
img_input.innerHTML = image_data;
}
-->
</script>
											<td class="subtitlebar_text" width="125" height="150" align=center>
													<span id="img_input">
<%
            if(picture_pc.compareTo("") != 0)
            {
                if(picture_pc.indexOf("http://") > 0)
                {
%>
												<img src="<%=picture_pc%>" style="clear:both">
<%
                }
                else
                {
%>
												<img src="http://www.hotenavi.com/<%=hotelid%>/<%=picture_pc%>" style="clear:both">
<%
                }
            }
%>
													</span>
												<div align=left><span id="msg_input"><%= msg %></span></div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</div>
						</table>
					</td>
				</tr>
                  <tr align="left" bgcolor="#FFFFFF">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>

				<tr align="left">
					<td >
						<div class="honbun" id="dispArea1">
						</div>
					</td>
				</tr>
                <tr align="left" bgcolor="#FFFFFF">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="3"></td>
                </tr>
                </table>

              </td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td valign="top">&nbsp;</td>
                <td valign="top"  colspan=2></td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td valign="top">&nbsp;
                </td>
                <td height="30" align="center" valign="middle" bgcolor="#969EAD">
				     <input name="regsubmit" type=button value="この景品を保存" onClick="if (validation_range()){MM_openInput('goods', '<%= hotelid %>', <%= category_id %>, <%= seq %>)}">
<%
    if (!NewFlag)
    {
%>
						  <input name="BackUp" type="checkbox" value="1">Backupを残す
				</td></form>
                <td height="30" align="center" valign="middle" bgcolor="#969EAD">
                  <form action="goods_delete.jsp?HotelId=<%= hotelid %>&CategoryId=<%= category_id %>&Seq=<%= seq %>" method="POST">
                  <input name="submit_del" type=submit value="削除">
<%
    }
%>
                </td></form>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td>&nbsp;</td>
                <td  colspan=2>&nbsp;</td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td>&nbsp;</td>
                <td align="center" colspan=2>
                  <form action="goods_list_detail.jsp?HotelId=<%= hotelid %>&CategoryId=<%= category_id %>" method="POST">
                  <input name="submit_ret" type=button value="戻る" onclick="history.back()">
                  </form>
                </td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td>&nbsp;</td>
                <td  colspan=2>&nbsp;</td>
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
