<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page errorPage="error.jsp" %><%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    NumberFormat nf = new DecimalFormat("00");
    boolean monthFlag = false;
    boolean rangeFlag = false;
    if (ReplaceString.getParameter(request,"Range") != null)
    {
        rangeFlag = true;
    }
    String paramDay = ReplaceString.getParameter(request,"Day");
    if (paramDay != null)
    {
        if(paramDay.equals("0")) 
        {
            monthFlag = true; 
        }
    }
    String paramMonth    = ReplaceString.getParameter(request,"Month");
    String paramYear     = ReplaceString.getParameter(request,"Year");
    String paramEndDay   = ReplaceString.getParameter(request,"EndDay");
    String paramEndMonth = ReplaceString.getParameter(request,"EndMonth");
    String paramEndYear  = ReplaceString.getParameter(request,"EndYear");

    boolean isDayCheck = true;

    if (paramDay == null)
    {
    }
    else if ( !CheckString.numCheck(paramDay))
    {
        isDayCheck = false;
    }
    else if (Integer.parseInt(paramDay) < -1 || Integer.parseInt(paramDay) > 31)
    {
        isDayCheck = false;
    }
    if (paramMonth == null)
    {
    }
    else if ( !CheckString.numCheck(paramMonth))
    {
        isDayCheck = false;
    }
    else if (Integer.parseInt(paramMonth) < 0 || Integer.parseInt(paramMonth) > 12)
    {
        isDayCheck = false;
    }
    if (paramYear == null)
    {
    }
    else if ( !CheckString.numCheck(paramYear))
    {
        isDayCheck = false;
    }

    if (paramEndDay == null)
    {
    }
    else if ( !CheckString.numCheck(paramEndDay))
    {
        isDayCheck = false;
    }
    else if (Integer.parseInt(paramEndDay) < 0 || Integer.parseInt(paramEndDay) > 31)
    {
        isDayCheck = false;
    }
    if (paramEndMonth == null)
    {
    }
    else if ( !CheckString.numCheck(paramEndMonth))
    {
        isDayCheck = false;
    }
    else if (Integer.parseInt(paramEndMonth) < 0 || Integer.parseInt(paramEndMonth) > 12)
    {
        isDayCheck = false;
    }
    if (paramEndYear == null)
    {
    }
    else if ( !CheckString.numCheck(paramEndYear))
    {
        isDayCheck = false;
    }

    if(!isDayCheck )
    {
%>
<jsp:include page="timeout.jsp" flush="true" />
<%
    }
    else
    {

    String selectTitle1 = ReplaceString.getParameter(request,"selectTitle1");
    if (selectTitle1 == null)
    {
        selectTitle1 = "0";
    }
    String selectTitle2 = ReplaceString.getParameter(request,"selectTitle2");
    if (selectTitle2 == null)
    {
        selectTitle2 = "0";
    }
    String selectStore = ReplaceString.getParameter(request,"selectStore");
    if (selectStore == null)
    {
        selectStore = "false";
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>管理店舗情報(売上)<%if(paramDay==null){%>本日分<%}else if(paramDay.equals("-1")){%>前回計上日分<%}else if(paramMonth == null){%>本月分<%}else{%><%= StringEscapeUtils.escapeHtml4(paramYear) %>/<%= StringEscapeUtils.escapeHtml4(paramMonth) %><%if(!paramDay.equals("0")){%>/<%= StringEscapeUtils.escapeHtml4(paramDay) %><%}%><%if(paramEndYear != null && paramEndMonth != null && paramEndDay != null){%>~<%= StringEscapeUtils.escapeHtml4(paramEndYear) %>/<%= StringEscapeUtils.escapeHtml4(paramEndMonth) %>/<%= StringEscapeUtils.escapeHtml4(paramEndDay) %><%}%>分<%}%></title>
<script type="text/javascript" src="../../common/smart/scripts/tableSort.js"></script>
<script type="text/javascript">
function hideAdBar(){
setTimeout("scrollTo(0,1)", 100);
if (window.orientation ==0){document.body.className="portrait";}else{document.body.className="landscape";}
}
function tableChange(){
  var selects = document.getElementsByClassName("Selects");
  for (i = 0;i < selects.length;i++)
  {
     if (selects[i].style.display=='none')
         {selects[i].style.display='';}
     else{selects[i].style.display='none';}
  }
  var dispTitle = document.getElementsByClassName("dispTitle");
  for (i = 2;i < dispTitle.length;i++)
 {
     if (dispTitle[i].style.display=='none')
         {dispTitle[i].style.display='';}
     else{dispTitle[i].style.display='none';}
  }
}

var category = 0;
var kind = 0;
var old_category = 0;
var old_kind = 0;

function titleChange(titleKind,oldIndex,obj){
  tableChange();
  var theadth = document.getElementsByClassName("theadTh"+titleKind);
  var newIndex = obj.selectedIndex;
  for (i = 0;i < theadth.length;i++)
  {
      if(theadth[i].innerHTML.indexOf(obj.value+'<span>') != -1)
      {
          theadth[i].style.display='';
      }
      else
      {
          theadth[i].style.display='none';
      }
  }
  if (titleKind == 1)
  {
     category = newIndex;
     old_category = oldIndex;
     old_kind = kind;
  }
  else
  {
     kind = newIndex;
     old_kind = oldIndex;
     old_category = category;
  }
//  alert("titleKind="+titleKind+",category="+category+",old_category="+old_category+",kind="+kind+",old_kind="+old_kind);
   
  var tbodyTd1New = document.getElementsByClassName("tbodyTd1"+category);
  var tbodyTd1Old = document.getElementsByClassName("tbodyTd1"+old_category);
  var tbodyTd2New = document.getElementsByClassName("tbodyTd2"+category+kind);
  var tbodyTd2Old = document.getElementsByClassName("tbodyTd2"+old_category+old_kind);
  for (i = 0;i < tbodyTd1New.length;i++)
  {
     tbodyTd1Old[i].style.display='none';
     tbodyTd1New[i].style.display='';
     tbodyTd2Old[i].style.display='none';
     tbodyTd2New[i].style.display='';
  }
  document.getElementById("selectTitle"+category).value = newIndex;
}

function dateSelect(startDate,endDate){
    document.form1.Year.value = Math.floor(startDate / 10000);
    document.form1.Month.value = Math.floor(startDate / 100) % 100;
    document.form1.Day.value = startDate % 100;
<%
        if (rangeFlag)
        {
%>  document.form1.EndYear.value = Math.floor(endDate / 10000);
    document.form1.EndMonth.value = Math.floor(endDate / 100) % 100;
    document.form1.EndDay.value = endDate % 100;
<%
   }
%>   document.form1.submit();
}

function selStore(){
    if (document.getElementById("selectStore").value == "true")
    {
        document.getElementById("selectStore").value = "false"; 
        document.form1.submit();
    }
    else
    {
        document.getElementById("reloadStore").style.display=''; 
        document.getElementById("selectStore").value = "true"; 
        document.getElementById("selStoreButton").value = "店舗選択終了"; 
        var storeCheck = document.getElementsByClassName("storeCheck");
        for (i = 0;i < storeCheck.length;i++)
        {
            storeCheck[i].style.display='';
        }
        var storeDetail = document.getElementsByClassName("storeDetail");
        for (i = 0;i < storeDetail.length;i++)
        {
            storeDetail[i].style.display='';
        }
    }
}

function storeEdit(hotelid,obj){
    document.getElementById("storeEdit").src = "../../common/smart/targetedit.jsp?TargetHotelId="+hotelid+"&SalesDispFlag="+obj.checked;
}
</script>
<link rel="stylesheet" type="text/css" href="../../common/smart/iphone_index.css">
<style>
.changeButton {
    width: 120px;
    height: 30px;
    color: #555555;
    font-size: 16px;
    vertical-align: -0.5em;
    margin: 8px;
}
.storeCheck {
    float: right;
    width: 20px;
    height: 20px;
}
.Selects {
    font-size:18px;
}
</style>
</head>
<body class="portrait" text="#555555">
<jsp:include page="header.jsp" flush="true" />
<h1 class="title">管理店舗売上情報<%if(paramDay==null){%><font size="-1">（本日分）</font><%}else if(paramDay.equals("-1")){%><font size="-1">（前回計上日分）</font><%}else if(paramMonth == null){%><font size="-1">（本月分）</font><%}%></h1>
<%-- 管理店舗売上情報取得 --%>
<jsp:include page="allstoresalesget.jsp" flush="true" />
<%-- 管理店舗売上情報取得ここまで --%>
<%
    if( ownerinfo.SalesGetStartDate == 0 )
    {
%>
取得できませんでした<br>
<%
    }
    else
    {
        int year     = ownerinfo.SalesGetStartDate / 10000;
        int month    = ownerinfo.SalesGetStartDate / 100 % 100;
        int day      = ownerinfo.SalesGetStartDate % 100;
        int endyear  = ownerinfo.SalesGetEndDate / 10000;
        int endmonth = ownerinfo.SalesGetEndDate / 100 % 100;
        int endday   = ownerinfo.SalesGetEndDate % 100;

%><h2><%= ownerinfo.SalesGetStartDate / 10000 %>/<%= nf.format(ownerinfo.SalesGetStartDate / 100 % 100) %><%if(!monthFlag){%>/<%= nf.format(ownerinfo.SalesGetStartDate % 100) %><%}%><%
        if (rangeFlag && ownerinfo.SalesGetEndDate != ownerinfo.SalesGetStartDate)
        {
%>～<%= ownerinfo.SalesGetEndDate / 10000 %>/<%= nf.format(ownerinfo.SalesGetEndDate / 100 % 100) %>/<%= nf.format(ownerinfo.SalesGetEndDate % 100) %>
<%
        }
%>
計上分<font size="-1"><a href="#DateSelect">[<%if(monthFlag){%>年月<%}else{%>日付<%}%>選択]</a></font></h2>
<hr class="border">
<%-- 管理店舗売上情報表示 --%>
<jsp:include page="allstoresalesdispdata.jsp" flush="true" />
<%-- 管理店舗売上情報表示ここまで --%>
<hr class="border">
<a name="DateSelect"></a>
<form name="form1" action="<%= response.encodeURL("allstoresales.jsp") %>" method="post">
<input type="button" id="selStoreButton" value="管理店舗選択" class="changeButton" onclick="selStore();">

<input type="hidden" name="selectTitle1" value="<%= StringEscapeUtils.escapeHtml4(selectTitle1) %>">
<input type="hidden" name="selectTitle2" value="<%= StringEscapeUtils.escapeHtml4(selectTitle2) %>">
<input type="hidden" id="selectStore" value="<%= StringEscapeUtils.escapeHtml4(selectStore) %>">
<input type="hidden" name="Year" value="<%= year %>">
<input type="hidden" name="Month" value="<%= month %>">
<input type="hidden" name="Day" value="<%= day %>">
<%
        if (rangeFlag)
        {
%>
<input type="hidden" name="EndYear" value="<%= endyear %>">
<input type="hidden" name="EndMonth" value="<%= endmonth %>">
<input type="hidden" name="EndDay" value="<%= endday %>">
<%
        }
%>
<% 
        if(monthFlag)
        {
%>
<%-- 年月選択画面表示 --%>
<jsp:include page="allstoreselectmonth.jsp" flush="true" />
<%-- 年月選択画面表示ここまで --%>
<%
        }
        else if (rangeFlag)
        {
%>
<%-- 日付範囲選択画面表示 --%>
<jsp:include page="allstoreselectrange.jsp" flush="true" />
<%-- 日付範囲選択画面表示ここまで --%>
<%
        }
        else
        {
%>
<%-- 日付選択画面表示 --%>
<jsp:include page="allstoreselectday.jsp" flush="true" />
<%-- 日付選択画面表示ここまで --%>
<%
        }
%>
<%
    }
%></form>
<hr class="border">
<jsp:include page="footer.jsp" flush="true" />
<script type="text/javascript">
<%
    if (!selectTitle1.equals("0"))
    {
%>
  document.getElementsByClassName("theadTh1")[0].style.display='none';
  document.getElementsByClassName("theadTh1")[<%= URLEncoder.encode(selectTitle2, "Windows-31J") %>].style.display='';
  for (i = 0;i < document.getElementsByClassName("tbodyTd10").length;i++)
  {
    document.getElementsByClassName("tbodyTd1<%= URLEncoder.encode(selectTitle2, "Windows-31J") %>")[i].style.display='';
    document.getElementsByClassName("tbodyTd10")[i].style.display='none';
  }
<%
    }
    if (!selectTitle2.equals("0"))
    {
%>
  document.getElementsByClassName("theadTh2")[0].style.display='none';
  document.getElementsByClassName("theadTh2")[<%= URLEncoder.encode(selectTitle2, "Windows-31J") %>].style.display='';
  for (i = 0;i < document.getElementsByClassName("tbodyTd20").length;i++)
  {
    document.getElementsByClassName("tbodyTd2<%= URLEncoder.encode(selectTitle2, "Windows-31J") %>")[i].style.display='';
    document.getElementsByClassName("tbodyTd20")[i].style.display='none';
  }
<%
    }
%>
</script>
<iframe id="storeEdit" style="display:none"></iframe>
</body>
</html>
<%
    }
%>