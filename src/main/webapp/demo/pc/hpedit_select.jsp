<%@ page contentType="text/html; charset=Windows-31J" %><%@ page errorPage="error.jsp" %><jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" /><%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%><script type="text/javascript">
<!--
    var dd = new Date();
    setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
//-->
</SCRIPT>
<%
    }
    else
    {
%><jsp:include page="../../common/pc/hpedit_select.jsp" flush="true"/><%
    }
%>
