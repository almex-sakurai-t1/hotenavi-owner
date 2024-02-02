<%@ page contentType="text/html; charset=Windows-31J"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Locale"%>
<%!
    private static Calendar getCalendar(String currentDate) {
        Calendar calendar = Calendar.getInstance(Locale.JAPAN);
        if (currentDate == null || currentDate.length() != 8) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        } else {
            int year = Integer.parseInt(currentDate.substring(0, 4));
            int month = Integer.parseInt(currentDate.substring(4, 6)) - 1;
            int date = Integer.parseInt(currentDate.substring(6, 8));
            calendar.set(year, month, date);
        }
        return calendar;
    }
    private static String formatDate(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
        return simpleDateFormat.format(calendar.getTime());
    }
    private static String getCurrentDateString() {
        return formatDate(getCalendar(null));
    }
    private static String getBeforeMonthDateString(String date) {
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.MONTH, -1);
        return formatDate(calendar);
    }
    private static String getNextMonthDateString(String date) {
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.MONTH, 1);
        return formatDate(calendar);
    }
    private static String getBeforeYearDateString(String date) {
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.YEAR, -1);
        return formatDate(calendar);
    }
    private static String getNextYearDateString(String date) {
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.YEAR, 1);
        return formatDate(calendar);
    }
    private static ArrayList<String> getDateList(Calendar calendar) {
        ArrayList<String> list = new ArrayList<String>();
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisMonthWithYear = calendar.get(Calendar.YEAR) * 100 + thisMonth;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
        String dateStr = simpleDateFormat.format(calendar.getTime());

        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        while (calendar.get(Calendar.DATE) != 1 && thisMonth == calendar.get(Calendar.MONTH)) {
            calendar.add(Calendar.DATE, -7);
        }

        while (thisMonthWithYear >= calendar.get(Calendar.YEAR) * 100 + calendar.get(Calendar.MONTH)) {
            for (int i = 0; i < 7; i++) {
                list.add(simpleDateFormat.format(calendar.getTime()));
                calendar.add(Calendar.DATE, 1);
            }
        }
        return list;
    }
    private static String getNationalHolidayName(String date) {
        String[][] nationalHolidaysA = {
            {"0101", "���U"},
            {"0102", "����"},
            {"0103", "����"},
            {"0211", "�����L�O��"},
            {"0429", "���a�̓�"},
            {"0503", "���@�L�O��"},
            {"0504", "�݂ǂ�̓�"},
            {"0505", "���ǂ��̓�"},
            {"0505", "���ǂ��̓�"},
            {"0923", "�H���̓�"},
            {"1103", "�����̓�"},
            {"1123", "�ΘJ���ӂ̓�"},
            {"1223", "�V�c�a����"}
        };
        String[][] nationalHolidaysB = {
            {"20090112", "���l�̓�"},
            {"20090320", "�t���̓�"},
            {"20090506", "�U�֋x��"},
            {"20090720", "�C�̓�"},
            {"20090921", "�h�V�̓�"},
            {"20090922", "�����̋x��"},
            {"20091012", "�̈�̓�"},
            {"20100111", "���l�̓�"},
            {"20100321", "�t���̓�"},
            {"20100322", "�U�֋x��"},
            {"20100719", "�C�̓�"},
            {"20100920", "�h�V�̓�"},
            {"20101011", "�̈�̓�"}
        };
        String d = date.substring(4, 8);
        for (int i = 0; i < nationalHolidaysA.length; i++) {
            if (nationalHolidaysA[i][0].equals(d)) {
                return nationalHolidaysA[i][1];
            }
        }
        for (int i = 0; i < nationalHolidaysB.length; i++) {
            if (nationalHolidaysB[i][0].equals(date)) {
                return nationalHolidaysB[i][1];
            }
        }
        return "";
    }
%>
<%
    String paramDate = ReplaceString.getParameter(request,"paramDate");
    String nextMonthDate = "";
    String beforMonthDate = "";
    String nextYearDate = "";
    String beforYearDate = "";
    if (paramDate == null || paramDate.trim().length() == 0) {
        paramDate = getCurrentDateString();
    }
    nextMonthDate = getNextMonthDateString(paramDate);
    beforMonthDate = getBeforeMonthDateString(paramDate);
    nextYearDate = getNextYearDateString(paramDate);
    beforYearDate = getBeforeYearDateString(paramDate);

    String thisMonth = paramDate.substring(4, 6);
    ArrayList<String> list = getDateList(getCalendar(paramDate));
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta http-equiv="Content-Style-Type" content="text/css">
        <meta http-equiv="Content-Script-Type" content="text/javascript">
        <meta http-equiv="Content-Language" content="ja">
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="Expires" content="-1">
        <title>�J�����_�[</title>
        <style>
            *.pink {background-color: pink;}
            *.skyblue {background-color: skyblue;}
            td.gray {background-color: gray;}
            *.al {text-align: left;}
            *.ac {text-align: center;}
            *.ar {text-align: right;}
            td.boldRedFont {font-weight: bold; color: red;}
        </style>
    </head>
    <body>
        <table>
            <tr>
                <td class="ac">
                    <a href="<%=request.getContextPath()%>/calendar.jsp?paramDate=<%=beforYearDate%>">�O�N�x</a>
                    <a href="<%=request.getContextPath()%>/calendar.jsp?paramDate=<%=beforMonthDate%>">�O��</a>
                    <%=paramDate.substring(0, 4)%>�N <%=Integer.parseInt(thisMonth)%>��
                    <a href="<%=request.getContextPath()%>/calendar.jsp?paramDate=<%=nextMonthDate%>">����</a>
                    <a href="<%=request.getContextPath()%>/calendar.jsp?paramDate=<%=nextYearDate%>">���N�x</a>
                </td>
            </tr>
            <tr>
                <td class="ac">
                    <table border="1">
                        <tr>
                            <%
                                String dayOfWeek = "�����ΐ��؋��y";
                                for (int i = 0; i < dayOfWeek.length(); i++) {
                            %>
                                    <th class="pink" width="30"><%=dayOfWeek.charAt(i)%></th>
                            <%
                                }
                            %>
                        </tr>
                        <tr>
                    <%
                        String dateStr = null;
                        String tdColor = null;
                        String tdTitle = null;
                        String nationalHolidayName = null;
                        for (int i = 0; i < list.size(); i++) {
                            dateStr = list.get(i);
                    %>
                        <%
                            if (i != 0 && i % 7 == 0) {
                        %>
                            </tr><tr>
                        <%
                            }
                            tdTitle = "";
                            if (thisMonth.equals(dateStr.substring(4, 6))) {
                                nationalHolidayName = getNationalHolidayName(dateStr);
                                if (nationalHolidayName.length() != 0) {
                                    tdColor = " pink boldRedFont";
                                    String replaceStr = " title=\"��\" onClick=\"alert('��')\"";
                                    tdTitle = replaceStr.replace("��", nationalHolidayName);
                                } else if (i % 7 == 0) {
                                    tdColor = " pink";
                                } else if ((i + 1) % 7 == 0) {
                                    tdColor = " skyblue";
                                } else {
                                    tdColor = "";
                                }
                            } else {
                                tdColor = " gray";
                            }
                        %>
                            <td class="ac<%=tdColor%>" <%=tdTitle%>><%=Integer.parseInt(dateStr.substring(6, 8))%></td>
                    <%
                        }
                    %>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </body>
</HTML>
