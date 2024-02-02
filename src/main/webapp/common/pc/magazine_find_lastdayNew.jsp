<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int i;

    Calendar cal = Calendar.getInstance();
    int year = cal.get(cal.YEAR);
    int month = cal.get(cal.MONTH) + 1;
    int day = cal.get(cal.DATE);
%>

  <input name="LastDayCheck" type="checkbox" id="LastDayCheck" value="1" onClick="setConditionCheckOnly(this);">
  最終利用日：
    <select name="LastDayStartYear">
<%
    for( i = 4 ; i > 0 ; i-- )
    {
%>
    <option value="<%= year - i %>"><%= year - i %></option>
<%
    }
%>
    <option value="<%= year - i %>" selected><%= year - i %></option>
    </select>
    年
    <select name="LastDayStartMonth">
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
    月
    <select name="LastDayStartDay">
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
    日～
    <select name="LastDayEndYear">
<%
    for( i = 4 ; i > 0 ; i-- )
    {
%>
    <option value="<%= year - i %>"><%= year - i %></option>
<%
    }
%>
    <option value="<%= year - i %>" selected><%= year - i %></option>
    </select>
    年
    <select name="LastDayEndMonth">
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
    月
    <select name="LastDayEndDay">
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
日