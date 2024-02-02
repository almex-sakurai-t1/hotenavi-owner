<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    int    report_flag = 0;
    String param_flag  = request.getParameter("flg");
    if    (param_flag != null)
    {
           report_flag = Integer.parseInt(param_flag);
    }
%>
<%@ include file="../../common/pc/report_ini.jsp" %>
<%
    boolean  NewFlag   = false;

    java.sql.Date start;
    java.sql.Date end;
    int    start_date  = 20000101;
    int    start_year  = 2000;
    int    start_month = 1;
    int    start_day   = 1;
    int    end_date    = 29991231;
    int    end_year    = 2999;
    int    end_month   = 12;
    int    end_day     = 31;
    int    id          = 0;
    String msg1        = "";
    String param_id    = request.getParameter("Id");
    if (param_id == null)
    {
        NewFlag   = true;
    }
    else
    {
        NewFlag   = false;
        id = Integer.parseInt(param_id);
    }
    if (!NewFlag)
    {
        try
        {
            query = "SELECT * FROM edit_event_info WHERE hotelid=?";
            query = query + " AND data_type=?";
            query = query + " AND id=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, data_type);
            prestate.setInt(3, id);
            result      = prestate.executeQuery();
            if (result != null)
            {
               if( result.next() != false )
               {
                   msg1        = result.getString("msg1");
                   start       = result.getDate("start_date");
                   start_year  = start.getYear() + 1900;
                   start_month = start.getMonth() + 1;
                   start_day   = start.getDate();
                   start_date  = start_year*10000 + start_month*100 + start_day;
                   end         = result.getDate("end_date");
                   end_year    = end.getYear() + 1900;
                   end_month   = end.getMonth() + 1;
                   end_day     = end.getDate();
                   end_date    = end_year*10000 + end_month*100 + end_day;
               }
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
        	DBConnection.releaseResources(result, prestate, connection);
        }
    }
 
    if (msg1.compareTo("") == 0)
    {
        msg1 = ReportList[report_flag][type];
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>クーポンレポート</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/edit_form.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0" id="disp" style="display:block">
            <tr>
              <td width="100" height="20" bgcolor="#22333F" class="tab" align="center"><font color="#FFFFFF"><%=hname%></font></td>
              <td width="140" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF">帳票設定</font></td>
              <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td width="200" height="20"  class="tab" align="right">&nbsp;</td>
              <td height="20" align="right">&nbsp;</td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ここから表 -->
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
						<strong>帳票設定</strong>
						&nbsp;&nbsp;<strong><%if(NewFlag){%>新規作成<%}else{%>更新<%}%></strong>
					</td>
				</tr>
                  <tr>
                    <td align="right" class="size10">&nbsp;</td>
                    <td align="right" class="size10">&nbsp;</td>
                    <td align="right" class="size10">&nbsp;</td>
                  </tr>

                  <tr>
                    <td class="red12"><strong>※このページを編集し終えたら「保存」ボタンを必ず押してください</strong></td>
                    <td align="right" class="size12">&nbsp;</td>
                    <td align="right" class="size12">&nbsp;</td>
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
                  <form name=form1 action="report_edit_input.jsp" method=post>
                  <tr align="left" bgcolor="#FFFFFF">
                    <td  class="size12">
                      <div align="left" class="size12" style="CLEAR: both; FLOAT: left;color:black">
<br><br><br>
                        表示期間：
                      <input name="col_start_yy" type="text" size="4" maxlength="4" value="<%= start_year %>" onChange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
                      年
                      <input name="col_start_mm" type="text" size="2" maxlength="2" value="<%= start_month %>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
                      月
                      <input name="col_start_dd" type="text" size="2" maxlength="2" value="<%= start_day %>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
                      日～
                      <input name="col_end_yy" type="text" size="4" maxlength="4"  value="<%= end_year %>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
                      年
                      <input name="col_end_mm" type="text" size="2" maxlength="2" value="<%= end_month %>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
                      月
                      <input name="col_end_dd" type="text" size="2" maxlength="2" value="<%= end_day %>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
                      日
						<img src="/common/pc/image/spacer.gif" width="50" height="8">
                      </div>
                    </td>
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
								<strong>&nbsp;設定内容</strong>
							</td>
							<td class="size12">
							</td>
							<td>
							</td>
						</tr>
						</table>
						</div>
					</td>
				</tr>
				<tr align="left" bgcolor="#FFFFFF">
					<td class="honbuntitlebar_text">
						<div class="honbun_margin" style="float:left;clear:both;">
							<textarea id="col_msg1" name=col_msg1 rows=20 cols=40><%=msg1%></textarea>
						</div> 
						<div class="size12">
							シートごとに改行して入力してください。<br>
							シートがあって表示しない場合は、その行を何も入力せずに改行してください。
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
                <td valign="top"></td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td valign="top">&nbsp;</td>
                <td height="30" align="center" valign="middle" bgcolor="#969EAD">
					<input type="hidden" name="Id"    value="<%= id %>">
					<input type="hidden" name="flg"   value="<%= report_flag %>">
					<input type="hidden" name="Year"  value="<%= year%>">
					<input type="hidden" name="Month" value="<%= month%>">
					<input type="hidden" name="Day"   value="<%= day%>">
					<input name="regsubmit" type=submit value="保存" >
				</td></form>
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
				<form action="report_edit_delete.jsp" target="mainFrame" method="post">
                <td height="30" align="center" valign="middle" bgcolor="#969EAD">
					<input type="hidden" name="Id"    value="<%= id %>">
					<input type="hidden" name="flg"   value="<%= report_flag %>">
					<input type="hidden" name="Year"  value="<%= year%>">
					<input type="hidden" name="Month" value="<%= month%>">
					<input type="hidden" name="Day"   value="<%= day%>">
					<input type="submit" value="削除">
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
				<form action="report_edit_list.jsp" target="mainFrame" method="post">
                <td align="center">
					<input type="hidden" name="Id"    value="<%=id%>">
					<input type="hidden" name="flg"   value="<%= report_flag %>">
					<input type="hidden" name="Year"  value="<%= year%>">
					<input type="hidden" name="Month" value="<%= month%>">
					<input type="hidden" name="Day"   value="<%= day%>">
					<input type="submit" value="戻る">
				</td></form>
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
<%
    db_sec.close();
%>
