<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/hpreport_ini.jsp" %>
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
    int[]   total_total   = new int[hotel_count];
    int     total_max     = Integer.parseInt(Max);
    int[][]   t_total  = new int[hotel_count][24];
    String[]t_time   = new String[24];
    t_time[ 0] = " 9:00";
    t_time[ 1] = "10:00";
    t_time[ 2] = "11:00";
    t_time[ 3] = "12:00";
    t_time[ 4] = "13:00";
    t_time[ 5] = "14:00";
    t_time[ 6] = "15:00";
    t_time[ 7] = "16:00";
    t_time[ 8] = "17:00";
    t_time[ 9] = "18:00";
    t_time[10] = "19:00";
    t_time[11] = "20:00";
    t_time[12] = "21:00";
    t_time[13] = "22:00";
    t_time[14] = "23:00";
    t_time[15] = " 0:00";
    t_time[16] = " 1:00";
    t_time[17] = " 2:00";
    t_time[18] = " 3:00";
    t_time[19] = " 4:00";
    t_time[20] = " 5:00";
    t_time[21] = " 6:00";
    t_time[22] = " 7:00";
    t_time[23] = " 8:00";
    for (i = 0; i < hotel_count; i++)
    {
        try
        {
            String FromDate = from_date/10000 + "-" + from_date/100%100 + "-" +  from_date%100;
            String ToDate   = to_date/10000 + "-" + to_date/100%100 + "-" +  to_date%100;
            query = "SELECT Sum(time01),Sum(time02),Sum(time03),Sum(time04),Sum(time05),Sum(time06),Sum(time07),Sum(time08),Sum(time09),Sum(time10),Sum(time11),Sum(time12),Sum(time13),Sum(time14),Sum(time15),Sum(time16),Sum(time17),Sum(time18),Sum(time19),Sum(time20),Sum(time21),Sum(time22),Sum(time23),Sum(time24),Sum(total) FROM access_mobile_detail";
            query+= " WHERE hotel_id =?";
            query+= " AND acc_date>=?";
            query+= " AND acc_date<=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,t_hotelid[i]);
            prestate.setString(2,FromDate);
            prestate.setString(3,ToDate);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                for (j = 0;j < 24; j++)
                {
                    t_total[i][j]   = result.getInt(j+1);
                    if (total_max < t_total[i][j]) total_max = t_total[i][j];
                }
                total_total[i]  = result.getInt(25);
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
// -->
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
					<td valign="middle">
						<table width="720" border="1" cellspacing="0" cellpadding="0"  bgcolor="#9200d0">
							<tr bgcolor="#FFFFCC">
								<td class="size12 honbun" align="center" width="45" style="text-align:center;color:#333333">時間帯</td>
								<td class="size12 honbun" align="center" width="65" style="text-align:center;color:#333333">アクセス数</td>
								<td class="size12 honbun" align="center" style="text-align:center;color:#333333">
									<img src="../../common/pc/image/spacer.gif" width="1" height="3">
								</td>
							</tr>
<%
        for (j = 0;j < 24; j++)
        {
%>
							<tr bgcolor="<%if (j % 2 == 0){%>#FFEEFF<%}else{%>#FFFFEE<%}%>" height="15">
								<td  class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333"  height="15">
									<%=t_time[j]%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333"  height="15">
									<%=Kanma.get(t_total[i][j])%>
								</td>
								<td class="size12 honbun" style="text-align:left;padding-left:3px;color:#333333"  height="15">
<%
            if (t_total[i][j] != 0 && total_max != 0)
            {
%>
									<img src="../../common/pc/image/bar_aqua.gif" width="<%= t_total[i][j]*500 / total_max%>" height="12" border="0">
<%
            }
%>
&nbsp;
								</td>
							</tr>
<%
        }
%>
							<tr bgcolor="#FFFFCC">
								<td class="size12 honbun" align="center" width="40" style="text-align:center;color:#333333">合計</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
									<%=Kanma.get(total_total[i])%>
								</td>
								<td class="size12 honbun" style="text-align:left;padding-left:3px;color:#333333">
									<img src="../../common/pc/image/spacer.gif" width="1" height="3">
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


