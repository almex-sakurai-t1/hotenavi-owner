<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="jp.happyhotel.common.DateEdit" %>
<%!
    private static String getNationalHolidayName(String date) {
        int targetDate = Integer.parseInt(date);
        return DateEdit.getHolidayName(targetDate);
    }
%>
