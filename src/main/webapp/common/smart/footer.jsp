<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.net.URLEncoder" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" /><%@ page import="com.hotenavi2.common.*" %>
<%
    String hotelid;
    hotelid = ReplaceString.getParameter(request,"HotelId");
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }
    int count = 1;
    if (session.getAttribute("StoreCount") != null)
    {
        count = Integer.valueOf((String)session.getAttribute("StoreCount")).intValue();
    }
    DateEdit de = new DateEdit();
%><div align="center" class="footer">
<%
    if( hotelid != null )
    {
%><a href="<%= response.encodeURL("ownerindex.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J")) %>">MENU‚Ö</a><br>
<%
    }
    if( count > 1 )
    {
%><a href="<%= response.encodeURL("storeselect.jsp") %>">“X•Ü‘I‘ð‚Ö</a><br>
<%
    }
%><a href="<%= response.encodeURL("index.jsp") %>">TOP‚Ö</a>
<% if (request.getHeader("X-FORWARDED-FOR") != null && !request.getHeader("X-FORWARDED-FOR").equals("125.63.42.196")){%>
<hr class="border">
<a href="<%= response.encodeURL("../smartpc/ownerindex.jsp?"+de.getDate(2)+de.getTime(1)) %>">PC”Å‚Ö</a>
<%}%>
</div>
<hr class="border">
<div align="center" class="copyright">
Copyright(C) almex inc. All rights reserved.
</div>
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-209661508-1"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'UA-209661508-1');
</script>
