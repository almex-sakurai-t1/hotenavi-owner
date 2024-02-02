<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%

    // ホテルID取得
    String disp_type = ReplaceString.getParameter(request,"DispType");
    if  (disp_type == null)
    {
         disp_type = "0";
    }
    String category_id = ReplaceString.getParameter(request,"CategoryId");
    if  (category_id == null)
    {
         category_id = "0";
    }
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
			hotelid="0";
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
    String loginhotel = (String)session.getAttribute("LoginHotelId");


    String    header_msg  = "";
    String    query       = "";
    boolean   mobile      = false;
    DateEdit  dateedit    = new DateEdit();

    int       disp_idx    = 0;
    String    title       = "";
    String    title_color = "";
    int start_yy   = 0;
    int start_mm   = 0;
    int start_dd   = 0;
    int start_date = 0;
    int end_yy     = 0;
    int end_mm     = 0;
    int end_dd     = 0;
    int end_date   = 0;

    int       nowdate     = Integer.parseInt(dateedit.getDate(2));
    int       goods_count = 0;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    try
    {
        query = "SELECT * FROM goods_category WHERE hotelid=? ";
        query = query + " AND category_id=? ";
        query = query + " AND disp_to>=? ";
        query = query + " ORDER BY disp_idx,seq DESC";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setString(2, category_id);
        prestate.setInt(3, nowdate);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            disp_idx    = result.getInt("disp_idx");
            title       = result.getString("title");
            title_color = result.getString("title_color");
            start_date  = result.getInt("disp_from");
            end_date    = result.getInt("disp_to");
            start_yy    = start_date / 10000;
            start_mm    = start_date / 100 % 100;
            start_dd    = start_date % 100;
            end_yy      = end_date / 10000;
            end_mm      = end_date / 100 % 100;
            end_dd      = end_date % 100;
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

    try
    {       //景品マスタ登録件数の抽出
        query = "SELECT count(*) FROM goods WHERE hotelid= ? ";
        if  (category_id.compareTo("0") == 0)
        {
            query = query + " AND suggest_flag = ?";
        }
        else
        {
            query = query + " AND category_id= ? ";
        }
        query = query + " AND disp_from<= ? ";
        query = query + " AND disp_to>= ? ";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        if  (category_id.compareTo("0") == 0)
        {
            prestate.setInt(2, 1);
        }
        else
        {
            prestate.setString(2, category_id);
        }
        prestate.setInt(3, nowdate);
        prestate.setInt(4, nowdate);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            goods_count = result.getInt(1);
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
%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>景品登録一覧</title>
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
</head>

<script type="text/javascript">
<!--
function disp_find(id,obj) {
	target = document.all(id);
	if(target.style.display == "none"){
		target.style.display = "block";
	} else {
		target.style.display = "none";
	}
}
-->
</script>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

	<tr valign="top">
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="left" valign="middle" style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="3">
						<strong>景品登録</strong>
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>

				<tr>
					<td  colspan="3">
						<!-- ############### サブタイトルバー ############### -->
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="120">
									<div class="subtitle_text"><font color="<%= title_color %>"><%= title %></div>
								</td>
								<td width="180" align=left title=" class="subtitle_text"><font color="<%= title_color %>">件数：<%=goods_count %>
								</td>
								<td style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;">
								期間:<%= start_yy %>年<%= start_mm %>月<%= start_dd %>日～<%= end_yy %>年<%= end_mm %>月<%= end_dd %>日&nbsp;
<%
            if( nowdate < start_date || nowdate > end_date )
            {
%>
								<br><b>表示していません。表示期間チェック！</b>
<%
            }
%>
								</td>
								<td align="center" bgcolor="#FFFFFF">
									<form action="goods_list.jsp?HotelId=<%= hotelid %>" method="POST">
									<INPUT name="submit_ret" type=submit value=戻る >
								</td></form>
							</tr>
						</table>
						<!-- ############### サブタイトルバー ここまで ############### -->
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="3"></td>
				</tr>

				<tr>
					<td>
						<form action="goods_form.jsp?HotelId=<%= hotelid %>&CategoryId=<%=category_id%>&Seq=0" method=POST>
						<input name="submit00" type=submit value="新しく景品を追加" >
					</td></form>
<%
    if (disp_type.compareTo("0") == 0)
    {
%>
					<td>
					<form action="goods_list_detail.jsp?HotelId=<%= hotelid %>&CategoryId=<%=category_id%>&DispType=1" method=POST>
						<input name="submit01" type=submit value="非表示分も表示する" >
					</td></form>
<%
    }
%>
					<td>
						<form action="goods_order_change.jsp?HotelId=<%= hotelid %>" method=POST>
						<input name="submit02" type=submit value="表示順番変更" >
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>
<%
        int    i     = 0;
        int    Seq   = 0;
        int    Category = 0;
        int    count = 0;

        query = "SELECT * FROM goods WHERE goods.hotelid='" + hotelid + "'";
//      query = query + " AND disp_from<=" + nowdate;
        if (disp_type.compareTo("0") == 0)
        {
            query = query + " AND disp_to>="   + nowdate;
        }
        if (category_id.compareTo("0") == 0)
        {
            query = query + " AND goods.suggest_flag=1";
            query = query + " ORDER BY goods.suggest_idx";
        }
        else
        {
            query = query + " AND goods.category_id =" + Integer.parseInt(category_id);
            query = query + " ORDER BY goods.disp_idx";
        }
        prestate = connection.prepareStatement(query);
        result   = prestate.executeQuery();
%>
				<tr>
					<td align="center" valign="top" colspan="3">
						<table>
						<div class="honbun_margin honbun">
<%
        while( result.next() != false )
        {
            Category = result.getInt("category_id");
            Seq      = result.getInt("seq");
            count++;
            if (count % 4 == 1)
            {
%>
							<tr>
<%
            }
%>
								<td align="left" valign="top">
									<table border="0" cellspacing="1" cellpadding="3" class="subtitlebar_linecolor">
										<tr class="honbun prize_box">
											<td  class="subtitlebar_text" align="center" rowspan="2"  nowrap>
												<input type="hidden" name="Category<%=count%>" value="<%= Category %>">
												<input type="hidden" name="Seq<%=count%>"      value="<%= Seq%>">
												表示順<br/>
<%
        if (category_id.compareTo("0") == 0)
        {
%>
												<input type="text" name="SuggestIdx<%=count%>" size=3 maxlength=3 value="<%=result.getInt("suggest_idx")%>" style="text-align:right;ime-mode:disabled">
<%
        }
        else
        {
%>
												<input type="text" name="DispIdx<%=count%>" size=3 maxlength=3 value="<%=result.getInt("disp_idx")%>" style="text-align:right;ime-mode:disabled">
<%
        }
%>
											</td>

											<td  class="subtitlebar_basecolor subtitle_text" width="125" colspan="2" nowrap style="padding-left:5px">
												<font color="<%= result.getString("title_color") %>"><%= new String(result.getString("title").getBytes("Shift_JIS"), "Windows-31J") %></font>
											</td>
										</tr>
										<tr class="honbun prize_box">
											<td  class="subtitlebar_text" width="125" colspan="2" nowrap style="padding-left:5px">
												<font color="<%= result.getString("title_sub1_color") %>"><%= new String(result.getString("title_sub1").getBytes("Shift_JIS"), "Windows-31J") %></font>
											</td>
										</tr>
										<tr class="honbun prize_box">
											<td  class="subtitlebar_text" align="center">
												<input type="button" value="編集" onclick="location.href='goods_form.jsp?HotelId=<%=hotelid%>&CategoryId=<%=Category%>&Seq=<%=Seq%>'"><br/>
<%
            if(result.getInt("disp_from") > nowdate || result.getInt("disp_to") < nowdate)
            {
%>
												<font color="red">非表示</font>
<%
            }
%>
											</td>
											<td class="subtitlebar_text" width="125" height="150" colspan="2" align=center>
<%
            if(result.getString("picture_pc").compareTo("") != 0)
            {
                if(result.getString("picture_pc").indexOf("http://") > 0)
                {
%>
												<img src="<%=result.getString("picture_pc")%>" style="clear:both">
<%
                }
                else
                {
%>
												<img src="http://www.hotenavi.com/<%=hotelid%>/<%=result.getString("picture_pc")%>" style="clear:both">
<%
                }
            }
%>
												<div align=left><%= new String(result.getString("msg").getBytes("Shift_JIS"), "Windows-31J") %></font></div>
											</td>
										</tr>
									</table>
								</td>
<%
            if (count % 4 == 0)
            {
%>
							</tr>
<%
            }
%>
<%
        }
        DBConnection.releaseResources(result,prestate,connection);
%>
<%
       if (count % 4 != 0)
       {
%>
							</tr>
<%
        }
%>
						</table>
						</div>
					</td>
				</tr>
			</table>
		</td><input name="LineCount" type=hidden value="<%= count %>"><input name="CategoryId" type=hidden value="<%= category_id %>"></form>
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


