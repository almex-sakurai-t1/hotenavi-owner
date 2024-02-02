<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="
java.text.*,
java.util.ArrayList,
java.util.Calendar,
java.util.Locale
" %>
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
    private static String getMinDateString(String date) {//5年前の日付
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.YEAR, -5);
        return formatDate(calendar);
    }
    private static String getBeforeMonthDateString(String date) {//前月（同曜日）
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.DATE, -28);
        if (Integer.parseInt(date)/100%100 == Integer.parseInt(formatDate(calendar))/100%100)
        {
            calendar.add(Calendar.DATE, -7);
        }
        return formatDate(calendar);
    }
    private static String getNextMonthDateString(String date) {//翌月（同曜日）
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.DATE, 28);
        if (Integer.parseInt(date)/100%100 == Integer.parseInt(formatDate(calendar))/100%100)
        {
            calendar.add(Calendar.DATE, 7);
        }
        return formatDate(calendar);
    }
    private static String getBeforeYearDateString(String date) {//前年（同曜日）
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.DATE, -364);
        return formatDate(calendar);
    }
    private static String getNextYearDateString(String date) {//翌年（同曜日）
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.DATE, 364);
        return formatDate(calendar);
    }
    private static String getBeforeDayDateString(String date) {//前日
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.DATE, -1);
        return formatDate(calendar);
    }
    private static String getNextDayDateString(String date) {//翌日
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.DATE, 1);
        return formatDate(calendar);
    }
    private static String getBeforeWeekDateString(String date) {//前週
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.DATE, -7);
        return formatDate(calendar);
    }
    private static String getNextWeekDateString(String date) {//翌週
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.DATE, 7);
        return formatDate(calendar);
    }
    private static String getNext1MDateString(String date) {//翌月
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.MONTH, 1);
        return formatDate(calendar);
    }
    private static String getNext2MDateString(String date) {//翌々月
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.MONTH, 2);
        return formatDate(calendar);
    }
    private static String getBefore1MDateString(String date) {//前月
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.MONTH, -1);
        return formatDate(calendar);
    }
    private static String getBefore1YDateString(String date) {//前年
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.YEAR, -1);
        return formatDate(calendar);
    }
    private static String getNext1YDateString(String date) {//翌年
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
    private static int getDateRange(String date,String date2) {//日数算出
        Calendar startdate   = getCalendar(date);
        Calendar enddate     = getCalendar(date2);
        long rday = (enddate.getTimeInMillis() - startdate.getTimeInMillis())/(1000*60*60*24);
        int  irday = (int)rday + 1;
        return irday;
    }
    private static String getAddDayDateString(String date,int addday) {//日付指定
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.DATE, addday);
        return formatDate(calendar);
    }
%>
