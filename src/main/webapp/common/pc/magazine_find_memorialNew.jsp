<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int i;

    Calendar cal = Calendar.getInstance();
    int month = cal.get(cal.MONTH) + 1;
    int day = cal.get(cal.DATE);
%>

  <input name="MemorialCheck" type="checkbox" id="MemorialCheck" value="1" onClick="setConditionCheckOnly(this);">
  ‹L”O“úF
    <select name="MemorialStartMonth">
<%
    for( i = 0 ; i < 12 ; i++ )
    {
        if( (i + 1) == month )
        {
%>
    <option value="<%= i + 1 %>" selected><%= i + 1 %></option>
<%
        }
        else
        {
%>
    <option value="<%= i + 1 %>"><%= i + 1 %></option>
<%
        }
    }
%>
    </select>
    ŒŽ
    <select name="MemorialStartDay">
<%
    for( i = 0 ; i < 31 ; i++ )
    {
        if( i == day )
        {
%>
    <option value="<%= i + 1 %>" selected><%= i + 1 %></option>
<%
        }
        else
        {
%>
    <option value="<%= i + 1 %>"><%= i + 1 %></option>
<%
        }
    }
%>
    </select>
    “ú`
    <select name="MemorialEndMonth">
<%
    for( i = 0 ; i < 12 ; i++ )
    {
        if( (i + 1) == month )
        {
%>
    <option value="<%= i + 1 %>" selected><%= i + 1 %></option>
<%
        }
        else
        {
%>
    <option value="<%= i + 1 %>"><%= i + 1 %></option>
<%
        }
    }
%>
    </select>
    ŒŽ
    <select name="MemorialEndDay">
<%
    for( i = 0 ; i < 31 ; i++ )
    {
        if( i == day )
        {
%>
    <option value="<%= i + 1 %>" selected><%= i + 1 %></option>
<%
        }
        else
        {
%>
    <option value="<%= i + 1 %>"><%= i + 1 %></option>
<%
        }
    }
%>
    </select>
“ú