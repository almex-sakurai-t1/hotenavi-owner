<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/hpreport_ini.jsp" %>
<%@ include file="../csrf/refererCheck.jsp" %>
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
%><%@ include file="../../common/pc/hpreport_common.jsp" %><%

    int     j     = 0;
    int     per   = 0;
    java.sql.Date acc_date;
    int     yy    = 0;
    int     mm    = 0;
    int     dd    = 0;
    int     target_date   = 0;
    int     acc_mobile    = 0;
    int     acc_pc        = 0;
    int     acc_smart     = 0;
    int     acc_total     = 0;
    int[]   total_mobile  = new int[hotel_count];
    int[]   total_pc      = new int[hotel_count];
    int[]   total_smart   = new int[hotel_count];
    int[]   total_docomo  = new int[hotel_count];
    int[]   total_jphone  = new int[hotel_count];
    int[]   total_au      = new int[hotel_count];
    int[]   total_total   = new int[hotel_count];
    int     total_max     = Integer.parseInt(Max);
    String  weekname      = "";
    int[][]   t_mobile = new int[hotel_count][31];
    int[][]   t_pc     = new int[hotel_count][31];
    int[][]   t_smart  = new int[hotel_count][31];
    int[][]   t_total  = new int[hotel_count][31];
    String[][]t_week   = new String[hotel_count][31];
    int[][]   t_date   = new int[hotel_count][31];
    for (i = 0; i < hotel_count; i++)
    {
        try
        {
            String FromDate = from_date/10000 + "-" + from_date/100%100 + "-" +  from_date%100;
            String ToDate   = to_date/10000 + "-" + to_date/100%100 + "-" +  to_date%100;

            query = "SELECT access_mobile_detail.* FROM access_mobile_detail";
            query+= " WHERE hotel_id =?";
            query+= " AND acc_date>=?";
            query+= " AND acc_date<=?";
            query+= " ORDER BY acc_date";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,  t_hotelid[i]);
            prestate.setString(2,  FromDate);
            prestate.setString(3,  ToDate);
            result      = prestate.executeQuery();
            j = -1;
            while( result.next() != false )
            {
                acc_date = result.getDate("acc_date");
                yy = acc_date.getYear() + 1900;
                mm = acc_date.getMonth()+ 1;
                dd = acc_date.getDate();
                j++;
                target_date  = (yy * 10000) +(mm*100) + dd;
                acc_mobile   = result.getInt("docomo") + result.getInt("jphone") + result.getInt("au");
                acc_pc       = result.getInt("etc");
                acc_smart    = result.getInt("smart");
                if (target_date <= 20131031)
                {
                   acc_pc    = acc_smart + acc_pc;
                   acc_smart = 0;
                }
                acc_total    = result.getInt("total");
                weekname     = de.getWeekName(target_date);

                total_mobile[i] += acc_mobile;
                total_docomo[i] += result.getInt("docomo");
                total_jphone[i] += result.getInt("jphone");
                total_au[i]     += result.getInt("au");
                total_pc[i]     += acc_pc;
                total_smart[i]  += acc_smart;
                total_total[i]  += acc_total;
                t_mobile[i][j]  += acc_mobile;
                t_pc[i][j]      += acc_pc;
                t_smart[i][j]   += acc_smart;
                t_total[i][j]   += acc_total;
                if (total_max < t_total[i][j]) total_max = t_total[i][j];
                t_week[i][j]    = weekname;
                t_date[i][j]    = dd;
                if ((j+1) > count[i])
                {
                    count[i] = j+1;
                }
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
    DBConnection.releaseResources(connection);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>アクセスレポート</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<% if (loginHotelId.compareTo("demo") == 0){%>
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
<%}%>

<script type="text/javascript">
<!--
function func() {
    top.Main.mainFrame.menu.document.getElementById('max').value=<%=total_max%>;
}
function funcPrint(target) {
<%
    for (i = 0;i < hotel_count; i++)
    {
%>document.getElementById('tr_<%=t_hotelid[i]%>').className='dispNone';
<%
    }
%>
document.getElementById('tr_'+target).className='dispBlock';
document.getElementById('disp_'+target).style.display='none';
document.getElementById('print_'+target).style.display='block';
print();
document.getElementById('print_'+target).style.display='none';
document.getElementById('disp_'+target).style.display='block';
<%
    for (i = 0;i < hotel_count; i++)
    {
%>document.getElementById('tr_<%=t_hotelid[i]%>').className='dispBlock';
<%
    }
%>}
// -->
</script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="setTimeout('func()',300)">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
	<tr valign="top">
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="left" valign="middle" style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;">
						<strong><%= Title[type]%></strong>
					</td>
				</tr>
				<tr>
					<td ><img src="../../common/pc/image/spacer.gif" width="500" height="10"></td>
				</tr>
<%
    for (i = 0;i < hotel_count; i++)
    {
%>
				<tr style="<% if(!t_hotelid[i].equals(hotelid)){%>display:none<%}%>" id="tr_<%=t_hotelid[i]%>">
				<td valign="top">
				<table>
<%@ include file="../../common/pc/hpreport_title.jsp" %>
				<tr>
					<td valign="top">
						<table width="720" border="1" cellspacing="0" cellpadding="0"  bgcolor="#333333">
							<tr bgcolor="#FFFFCC">
								<td class="size12 honbun" align="center" width="40" rowspan="2" style="text-align:center;color:#333333">日付</td>
								<td class="size12 honbun" align="center" width="35" rowspan="2" style="text-align:center;color:#333333">曜日</td>
								<td class="size12 honbun" align="center" colspan="4" style="text-align:center;color:#333333">アクセス数</td>
								<td class="size12 honbun" rowspan="2" style="padding-left:50px;color:#333333">
									携帯:<img src="../../common/pc/image/bar_aqua.gif" width="50" height="12" border="0">
									スマホ:<img src="../../common/pc/image/bar_green.gif" width="50" height="12" border="0">
									PC他:<img src="../../common/pc/image/bar_orange.gif" width="50" height="12" border="0">
								</td>
							</tr>
							<tr bgcolor="#FFFFCC">
								<td class="size12 honbun" align="center" width="63" style="text-align:center;color:#333333">携帯</td>
								<td class="size12 honbun" align="center" width="63" style="text-align:center;color:#333333">スマホ</td>
								<td class="size12 honbun" align="center" width="63" style="text-align:center;color:#333333">PC他</td>
								<td class="size12 honbun" align="center" width="63" style="text-align:center;color:#333333">合計</td>
							</tr>
<%
        for (j = 0;j < count[i]; j++)
        {
            if(t_week[i][j] != null)
            {
%>
							<tr bgcolor="<%if (t_week[i][j].compareTo("日") == 0){%>#FFEEEE<%}else if (t_week[i][j].compareTo("土") == 0){%>#EEEEFF<%}else{%>#FFFFEE<%}%>">
								<td  class="size12 honbun" style="text-align:right;color:<%if (t_week[i][j].compareTo("日") == 0){%>#FF0000<%}else{%>#333333<%}%>" >
									<%=t_date[i][j]%>日
								</td>
								<td  class="size12 honbun" style="text-align:center;color:<%if (t_week[i][j].compareTo("日") == 0){%>#FF0000<%}else{%>#333333<%}%>">
									<%=t_week[i][j]%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
									<%=Kanma.get(t_mobile[i][j])%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
									<%=Kanma.get(t_smart[i][j])%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
									<%=Kanma.get(t_pc[i][j])%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
									<%=Kanma.get(t_total[i][j])%>
								</td>
								<td class="size12 honbun" style="text-align:left;padding-left:3px;color:#333333">
<%
                if (t_total[i][j] != 0 && total_max != 0)
                {
%>
									<img src="../../common/pc/image/bar_aqua.gif" width="<%= t_mobile[i][j]*350 / total_max%>" height="12" border="0"><img src="../../common/pc/image/bar_green.gif" width="<%= t_smart[i][j]*350 / total_max%>" height="12" border="0"><img src="../../common/pc/image/bar_orange.gif" width="<%= t_pc[i][j]*350 / total_max%>" height="12" border="0">
<%
                }
%>
								</td>
							</tr>
<%
            }
        }
%>
							<tr bgcolor="#FFFFCC">
								<td class="size12 honbun" align="center" width="75" colspan="2" rowspan="2" style="text-align:center;color:#333333">合　計</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
									<%=Kanma.get(total_mobile[i])%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
									<%=Kanma.get(total_smart[i])%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
									<%=Kanma.get(total_pc[i])%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
									<%=Kanma.get(total_total[i])%>
								</td>
								<td class="size12 honbun" style="text-align:left;padding-left:3px;color:#333333" rowspan="2">
									<img src="../../common/pc/image/spacer.gif" width="1" height="3">
								</td>
							</tr>
							<tr bgcolor="#FFFFCC">
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
<%
        per        = 0;
        if (total_total[i] != 0)
        {
            per = Math.round((float)total_mobile[i]*1000 /(float)total_total[i]);
        }
%>
									&nbsp;<%if (total_total[i] != 0){%><%= per/10 %>.<%= per % 10 %>%<%}%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
<%
        per        = 0;
        if (total_total[i] != 0)
        {
            per = Math.round((float)total_smart[i]*1000 /(float)total_total[i]);
        }
%>
									&nbsp;<%if (total_total[i] != 0){%><%= per/10 %>.<%= per % 10 %>%<%}%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
<%
        per        = 0;
        if (total_total[i] != 0)
        {
            per = Math.round((float)total_pc[i]*1000 /(float)total_total[i]);
        }
%>
									&nbsp;<%if (total_total[i] != 0){%><%= per/10 %>.<%= per % 10 %>%<%}%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
									&nbsp;
<%
        if (total_total[i] != 0)
        {
%>
									100.0%
<%
        }
%>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
				</tr>
				</table></td></tr>
<%
    }
%>	
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


