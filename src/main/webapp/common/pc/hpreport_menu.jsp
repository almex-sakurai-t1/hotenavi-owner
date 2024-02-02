<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
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
    if( hotelid == null || !CheckString.hotenaviIdCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    String Group  = ReplaceString.getParameter(request,"Group");
    if (Group  == null || Group.equals(""))  Group  = hotelid;
    String[] ckGroup = Group.split(",");

    for (int ck_i=0;ck_i < ckGroup.length; ck_i++)
    {
        if( !CheckString.hotenaviIdCheck(ckGroup[ck_i]))
        {
          Group = "";
          response.sendError(400);
          return;
        }
    }
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");

    int        i                  = 0;
    int        imedia_user        = 0;
    String     query              = "";
    boolean    MemberFlag         = false;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
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

    // メンバーページ有無のチェック
    try
    {
        query = "SELECT * FROM menu WHERE hotelid=?";
        query = query + " AND contents='search.jsp'";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            MemberFlag = true;
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }

    int menu_count = 5;
    if (!MemberFlag) menu_count = 4;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>メニュー</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../<%= hotelid %>/pc/menu.css" rel="stylesheet" type="text/css">
<% if (loginHotelId.compareTo("demo") == 0){%>
<link href="http://www.hotenavi.com/<%= hotelid %>/menu.css" rel="stylesheet" type="text/css">
<%}%>
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_preloadImages() { //v3.0
	var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
		var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
		if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
	var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
	var p,i,x;	if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
		d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
	var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	 if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="220" border="0" cellspacing="0" cellpadding="0">

	<tr>
		<td><img src="../../common/pc/image/spacer.gif" width="7" height="20"></td>
	</tr>
	<tr>
		<td><div class="size14 grouptop">HPアクセスレポート</div></td>
	</tr>
	<tr>
		<td><img src="../../common/pc/image/spacer.gif" width="7" height="8"></td>
	</tr>
	<tr>
		<td><div class="size12 name">↓項目を選択してください</div></td>
	</tr>
	
	<tr valign="top">
		<td>
		
			<table width="220" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td valign="top">
						
						<!-- メニューボックス -->
						<table width="220" border="1" cellspacing="0" cellpadding="0"  bgcolor="#FFFFEE">
<%
    for(i = 0; i <  menu_count ; i++)
    {
%>
							<tr>
								<td class="size12" style="text-align:left;padding:3px;color:#333333">
									<a style="color:#333333" href="#" target="_self" onclick="formAct(<%=i%>,null,null,null);top.Main.mainFrame.contents.location.href='<%=Contents[i]%>?Type=<%=i%>&Year=<%=Year%>&Month=<%=Month%>';"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">
<%
        if(i == type)
        {
%>
										<strong><%= Title[i] %></strong>
<%
        }
        else
        {
%>
										<%= Title[i] %>
<%
        }
%>
									</a>
								</td>
							</tr>
<%
    }
%>
						</table>
					</td>
				</tr>
				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="7" height="20"></td>
				</tr>
			</table>
			
		</td>
	</tr>
	<tr>
		<td>
			<table width="220" height="30" border="1" cellpadding="0" cellspacing="0"  bgcolor="#FFFFEE">
				<tr valign="middle">
					<td class="size12" style="text-align:left;padding-left:3px;color:#333333">
						<a style="color:#333333" href="#" class="back"  target="_self"  onclick="formAct(null,<%= year-1 %>,null,null);top.Main.mainFrame.contents.location.href='<%=Contents[type]%>?Type=<%=type%>&Year=<%=year -1 %>&Month=<%=Month%>&Max='+document.getElementById('max').value;">←</a>
						<img src="../../common/pc/image/spacer.gif" width="15" height="1">
<%
           if (type == 0)
           {
%>
						<a style="color:#333333" href="#" class="back" onMouseOver="MM_swapImage('download','','../../common/pc/image/download_o.gif',1)" onMouseOut="MM_swapImgRestore()" target="_self"  onclick="formAct(null,<%= year %>,0,null);top.Main.mainFrame.contents.location.href='hpreport_monthly.jsp?Type=<%=type%>&Year=<%=Year%>&Month=0<%if(month!=0){%>'<%}else{%>&Max='+document.getElementById('max').value<%}%>;"><img src="../../common/pc/image/download.gif" alt="年次ダウンロード" name="download" width="13" height="14" border="0" align="absmiddle" id="download">&nbsp;<%= year %>年</a>
<%
           }
           else
           {
%>
						<a style="color:#333333" href="#" class="back" onMouseOver="MM_swapImage('download','','../../common/pc/image/download_o.gif',1)" onMouseOut="MM_swapImgRestore()" target="_self"  onclick="formAct(null,<%= year %>,0,null);top.Main.mainFrame.contents.location.href='<%=Contents[type]%>?Type=<%=type%>&Year=<%=Year%>&Month=0<%if(month!=0){%>'<%}else{%>&Max='+document.getElementById('max').value<%}%>;"><img src="../../common/pc/image/download.gif" alt="年次ダウンロード" name="download" width="13" height="14" border="0" align="absmiddle" id="download">&nbsp;<%= year %>年</a>
<%
           }
%>
						<img src="../../common/pc/image/spacer.gif" width="10" height="1">

<%
           if (year < now_year)
           {
%>
					　　<a style="color:#333333" href="#" class="back"  target="_self"  onclick="formAct(null,<%= year+1 %>,null,null);top.Main.mainFrame.contents.location.href='<%=Contents[type]%>?Type=<%=type%>&Year=<%=year + 1%>&Month=<%=Month%>&Max='+document.getElementById('max').value;">→</a>
<%
           }
%>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr valign="top">
		<td>
			<table width="220" border="1" cellspacing="0" cellpadding="0"    bgcolor="#FFFFEE">
				<tr>
<%
    Contents[type] = Contents[type].replace("monthly","daily");
    for( i = 1 ; i <= 12 ; i++ )
    {
        if( ((i-1) % 4) == 0 && i!=1)
        {
%>
				</tr>
				<tr>
<%
        }
%>
					<td width="25%" class="size12" style="text-align:center;padding:1px;color:#333333" nowrap>
<%
        if (month == i)
        {
%>
						<a style="color:#333333" href="#" target="_self"  onclick="formAct(null,<%= year %>,<%= i %>,null);top.Main.mainFrame.contents.location.href='<%=Contents[type]%>?Type=<%=type%>&Year=<%=Year%>&Month=<%=i%><%if(month==0){%>'<%}else{%>&Max='+document.getElementById('max').value<%}%>;"><strong><big><%= i %>月</big></strong></a>
<%
        }
        else
        {
           if (year > now_year || (year == now_year && i > now_month))
           {
%>
						<a style="color:#333333" href="#"><%= i %>月</a>
<%
            }
            else
            {
%>
						<a style="color:#333333" href="#" target="_self"  onclick="formAct(null,<%= year %>,<%= i %>,null);top.Main.mainFrame.contents.location.href='<%=Contents[type]%>?Type=<%=type%>&Year=<%=Year%>&Month=<%=i%><%if(month==0){%>'<%}else{%>&Max='+document.getElementById('max').value<%}%>;"><%= i %>月</a>
<%
            }
        }
%>
					</td>
<%
    }
%>

				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td><img src="../../common/pc/image/spacer.gif" width="7" height="20"></td>
	</tr>
	<tr valign="top" id="groupLink">
		<td>
			<table width="220" border="1" cellspacing="0" cellpadding="0" bgcolor="#FFFFEE" style="padding:10px">
<jsp:include page="../../common/pc/hpreport_group.jsp" flush="true" />
			</table>
		</td>
	</tr>
	<tr>
		<td >
		<form name="formMenu" action="hpreport_menu.jsp" method="post" target="_self">
			<input id="Type" name="Type" type="hidden" value="<%=type%>">
			<input id="Year" name="Year" type="hidden" value="<%=Year%>">
			<input id="Month" name="Month" type="hidden" value="<%=Month%>">
			<input id="Day" name="Day" type="hidden" value="<%=Day%>">
			<input id="max" type="hidden" value="<%=Max%>">
			<input id="Group" name="Group" type="hidden" value="<%=Group%>">
		</td></form>
	</tr>

</table>
<script type="text/javascript">
function formAct(Type,Year,Month,Day)
{
    if (Type != null)
    {
        document.getElementById("Type").value=Type;
    }
    if (Year != null)
    {
        document.getElementById("Year").value=Year;
    }
    if (Month != null)
    {
        document.getElementById("Month").value=Month;
    }
    if (Day != null)
    {
        document.getElementById("Day").value=Day;
    }
    document.formMenu.submit();
}
</script>
</body>
</html>
