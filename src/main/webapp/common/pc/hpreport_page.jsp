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

    String  Mem   = ReplaceString.getParameter(request,"Member");
    if     (Mem  == null) Mem = "";
    if     (Mem.compareTo("") != 0 && Mem.compareTo("mem_") != 0) Mem = "";

    int     j            = 0;
    int     line_count   = 0;
    int     per          = 0;
    int[]   total_total   = new int[hotel_count];
    int     total_max     = Integer.parseInt(Max);
    int[][] t_total      = new int[hotel_count][50];
    int[]     page99_total = new int[hotel_count];
    String[][]t_page       = new String[hotel_count][50];
    String  page99       = "その他";
    String    tempid   = "";

    //ページ名の取得
    for (i = 0; i < hotel_count; i++)
    {
        try
        {
            query = "SELECT * FROM access_mobile_name";
            query+= " WHERE hotel_id = '" + t_hotelid[i] + "'";
            query+= " ORDER BY hotel_id";
            prestate    = connection.prepareStatement(query);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                for (j = 0;j < 9; j++)
                {
                    t_page[i][j]   = result.getString(Mem + "page0"+(j+1));
                }
                for (j = 9;j < 50; j++)
                {
                    t_page[i][j]   = result.getString(Mem + "page"+(j+1));
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
        try
        {
            String FromDate = from_date/10000 + "-" + from_date/100%100 + "-" +  from_date%100;
            String ToDate   = to_date/10000 + "-" + to_date/100%100 + "-" +  to_date%100;
            query = "SELECT ";
            for (j = 0;j < 9; j++)
            {
                query = query + "Sum(" + Mem + "page0" + (j+1) + "),";
            }
            for (j = 9;j < 50; j++)
            {
                query = query + "Sum(" + Mem + "page" + (j+1) + "),";
            }
            query+= "Sum(" + Mem + "page99),Sum(total)";
            query+= " FROM access_mobile_detail";
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
                for (j = 0;j < 50; j++)
                {
                    if (t_page[i][j].compareTo("") == 0)
                    {
                        page99_total[i]  += result.getInt(j+1);
                    }
                    else
                    {
                        t_total[i][j]   = result.getInt(j+1);
                    }
                    total_total[i]  += t_total[i][j];
                    if (total_max < t_total[i][j]) total_max = t_total[i][j];
                }
                page99_total[i]  += result.getInt(51);
                if (total_max < page99_total[i]) total_max = page99_total[i];
                total_total[i]   += page99_total[i];
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
						<table width="720" border="1" cellspacing="0" cellpadding="0"  bgcolor="#9200d0">
							<tr bgcolor="#FFFFCC">
								<td class="size12 honbun" align="center" width="150" style="text-align:center;color:#333333">ページ名</td>
								<td class="size12 honbun" align="center" width="65" style="text-align:center;color:#333333">アクセス数</td>
								<td class="size12 honbun" align="center" style="text-align:center;color:#333333">
									<img src="../../common/pc/image/spacer.gif" width="1" height="3">
								</td>
							</tr>
<%
        line_count = 0;
        for (j = 0;j < 50; j++)
        {
            if (t_total[i][j] != 0)
            {
                line_count++;
%>
							<tr bgcolor="<%if (line_count % 2 == 0){%>#FFEEFF<%}else{%>#FFFFEE<%}%>">
								<td  class="size12 honbun" style="text-align:left;;padding-left:5px;color:#333333">
									<%=t_page[i][j]%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
									<%=Kanma.get(t_total[i][j])%>
								</td>
								<td class="size12 honbun" style="text-align:left;padding-left:3px;color:#333333">
<%
                if (t_total[i][j] != 0 && total_max != 0)
                {
%>
									<img src="../../common/pc/image/bar_aqua.gif" width="<%= t_total[i][j]*400 / total_max%>" height="12" border="0">
<%
                }
%>
								</td>
							</tr>
<%
            }
        }
        if (page99_total[i] != 0)
        {
            line_count++;
%>
							<tr bgcolor="<%if (line_count % 2 == 0){%>#FFEEFF<%}else{%>#FFFFEE<%}%>">
								<td  class="size12 honbun" style="text-align:left;;padding-left:5px;color:#333333">
									<%=page99%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
									<%=Kanma.get(page99_total[i])%>
								</td>
								<td class="size12 honbun" style="text-align:left;padding-left:3px;color:#333333">
<%
            if (page99_total[i] != 0 && total_max != 0)
            {
%>
									<img src="../../common/pc/image/bar_aqua.gif" width="<%= page99_total[i]*400 / total_max%>" height="12" border="0">
<%
            }
%>
								</td>
							</tr>
<%
        }
%>							<tr bgcolor="#FFFFCC">
								<td class="size12 honbun" align="center" width="150" style="text-align:center;color:#333333">合　　計</td>
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
%>			</table>
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


