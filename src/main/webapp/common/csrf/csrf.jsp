<%@ page pageEncoding="Windows-31J" %>
<%@ page import="jp.happyhotel.common.RandomString" %>
<%
    // csrf用ワンタイムトークン発行
    session.removeAttribute("csrf");
    String token = RandomString.generateRandomString(64);
    session.setAttribute("csrf", token);
    Logging.info("token:" + token);
%>
<script>
function postUrl(url)
{
    document.write("<form action='" + url + "' method='post' name='formPost' style='display:none'>");  
    document.write("<input type='hidden' name='csrf' value='<%=token%>'>");  
    document.write("</form>");  
    document.formPost.submit();  
}
</script>