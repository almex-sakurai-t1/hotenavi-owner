package jp.happyhotel.common;

public class MemberRegistTagCreate
{

    public static final int yearMaxCalcu = 18;
    public static final int yearMinCalcu = 80;

    /**
     * 年selectタグ生成
     * 
     * @param selecName select_tagのidName
     * @param currentYear option selected を設定する値
     * @param parentTagName select_tagを含む親タグname
     * @param yearMaxCalcu 西暦プルダウンの上限値(現在年 - 減算値)を算出する為の減算値
     * @param yearMinCalcu 西暦プルダウンの下限値(現在年 - 減算値)を算出する為の減算値
     * @param disabledFlag タグ非活性フラグ
     * @return 年selectタグ文字列
     */
    public static String createSelectYearValues(String selecName, String currentYear, String parentTagName, int yearMaxCalcu, int yearMinCalcu, boolean disabledFlag)
    {
        int i = Integer.parseInt( DateEdit.getDate( 2 ) ) / 10000;
        int selectedValue = null == currentYear ? 0 : Integer.parseInt( currentYear );
        String disabledString = true == disabledFlag ? "disabled=\"disabled\"" : " ";
        StringBuilder selectYear = new StringBuilder();
        selectYear.append( "<select name=" )
                .append( selecName )
                .append( " class=\"narrow\" " + disabledString + " style=\"width:150px;font-size:160%;\" onchange=\"setDay(" + parentTagName + ");\">" )
                .append( "<option value=\"0\">-</option>" );
        for( int year = i - yearMinCalcu ; year <= i - (yearMaxCalcu - 1) ; year++ )
        {
            selectYear.append( "<option value=" )
                    .append( year )
                    .append( year == selectedValue ? " selected=\"selected\">" : ">" )
                    .append( year )
                    .append( "</option>" );
        }
        selectYear.append( "</select>" );
        return selectYear.toString();
    }

    /**
     * 月selectタグ生成
     * 
     * @param selecName select_tagのidName
     * @param currentYear option selected を設定する値
     * @param parentTagName select_tagを含む親タグname
     * @param disabledFlag タグ非活性フラグ
     * @return 月selectタグ文字列
     */
    public static String createSelectMonthValues(String selecName, String currentMonth, String parentTagName, boolean disabledFlag)
    {
        int selectedValue = null == currentMonth ? 0 : Integer.parseInt( currentMonth );
        String disabledString = true == disabledFlag ? "disabled=\"disabled\"" : " ";
        StringBuilder selectMonth = new StringBuilder();
        selectMonth.append( "<select name=" )
                .append( selecName )
                .append( " class=\"narrow\" " + disabledString + " style=\"width:120px;font-size:160%;\" onchange=\"setDay(" + parentTagName + ");\">" )
                .append( "<option value=\"0\">-</option>" );
        for( int month = 1 ; month <= 12 ; month++ )
        {
            selectMonth.append( "<option value=" )
                    .append( month )
                    .append( month == selectedValue ? " selected=\"selected\">" : ">" )
                    .append( month )
                    .append( "</option>" );
        }
        selectMonth.append( "</select>" );
        return selectMonth.toString();
    }

    /**
     * 日selectタグ生成
     * 
     * @param selecName select_tagのidName
     * @param currentYear option selected を設定する値
     * @param parentTagName select_tagを含む親タグname
     * @param disabledFlag タグ非活性フラグ
     * @return 日selectタグ文字列
     */
    public static String createSelectDayValues(String selecName, String currentDay, String parentTagName, boolean disabledFlag)
    {
        int selectedValue = null == currentDay ? 0 : Integer.parseInt( currentDay );
        String disabledString = true == disabledFlag ? "disabled=\"disabled\"" : " ";
        StringBuilder selectDay = new StringBuilder();
        selectDay.append( "<select name=" )
                .append( selecName )
                .append( " class=\"narrow\" " + disabledString + " style=\"width:120px;font-size:160%;\" onchange=\"setDay(" + parentTagName + ");\">" )
                .append( "<option value=\"0\">-</option>" );
        for( int day = 1 ; day <= 31 ; day++ )
        {
            selectDay.append( "<option value=" )
                    .append( day )
                    .append( day == selectedValue ? " selected=\"selected\">" : ">" )
                    .append( day )
                    .append( "</option>" );
        }
        selectDay.append( "</select>" );
        return selectDay.toString();
    }

    /**
     * 非活性 年selectタグ生成
     * 
     * @param name select_tagのid
     * @param value
     * @return 非活性プルダウンタグ文字列
     */
    public static String createDisabledSelectYearTag(String name, String value)
    {
        return "<select class=\"narrow\" name=\"" + name + "\" style=\"width:150px;font-size:160%;\" disabled=\"disabled\">"
                + "<option value=" + value + ">" + value + "</option>"
                + "</select>";
    }

    /**
     * 非活性 月selectタグ生成
     * 
     * @param name select_tagのid
     * @param value
     * @return 非活性プルダウンタグ文字列
     */
    public static String createDisabledSelectMonthTag(String name, String value)
    {
        return "<select class=\"narrow\" name=\"" + name + "\" style=\"width:120px;font-size:160%;\" disabled=\"disabled\">"
                + "<option value=" + value + ">" + value + "</option>"
                + "</select>";
    }

    /**
     * 非活性 日selectタグ生成
     * 
     * @param name select_tagのid
     * @param value
     * @return 非活性プルダウンタグ文字列
     */
    public static String createDisabledSelectDayTag(String name, String value)
    {
        return "<select class=\"narrow\" name=\"" + name + "\" style=\"width:120px;font-size:160%;\" disabled=\"disabled\">"
                + "<option value=" + value + ">" + value + "</option>"
                + "</select>";
    }

    /**
     * 性別selectTag作成
     * 
     * @param value デフォルト選択値
     * @return 性別selectTag文字列
     */
    public static String createSelectSexTag(String value)
    {
        String manSelected = "";
        String womanSelected = "";

        if ( "1".equals( value ) )
        {
            manSelected = " selected=\"selected\"";
        }
        else if ( "2".equals( value ) )
        {
            womanSelected = " selected=\"selected\"";
        }

        return "<select class=\"narrow\" name=\"sex\" style=\"width:120px;font-size:160%;\">"
                + "<option value=1" + manSelected + ">男性</option>"
                + "<option value=2" + womanSelected + ">女性</option>"
                + "</select>";
    }

    /**
     * メール配信selectTag作成
     * 
     * @param value デフォルト選択値
     * @return メール配信selectTag文字列
     */
    public static String createSelectMailMagazineTag(String value)
    {
        String distribute = "";
        String undistribute = "";

        if ( "1".equals( value ) )
        {
            distribute = " selected=\"selected\"";
        }
        else if ( "2".equals( value ) )
        {
            undistribute = " selected=\"selected\"";
        }

        return "<select class=\"narrow\" name=\"mailMagazine\" style=\"width:250px;font-size:160%;\">"
                + "<option value=1" + distribute + ">配信する</option>"
                + "<option value=2" + undistribute + ">配信しない</option>"
                + "</select>";
    }

    /**
     * 年selectタグ生成
     * 
     * @param selecName select_tagのidName
     * @param currentYear option selected を設定する値
     * @param parentTagName select_tagを含む親タグname
     * @param yearMaxCalcu 西暦プルダウンの上限値(現在年 - 減算値)を算出する為の減算値
     * @param yearMinCalcu 西暦プルダウンの下限値(現在年 - 減算値)を算出する為の減算値
     * @param disabledFlag タグ非活性フラグ
     * @return 年selectタグ文字列
     */
    public static String createSelectYearValuesMobile(String selecName, String currentYear, String parentTagName, int yearMaxCalcu, int yearMinCalcu, boolean disabledFlag)
    {
        int i = Integer.parseInt( DateEdit.getDate( 2 ) ) / 10000;
        int selectedValue = null == currentYear ? 0 : Integer.parseInt( currentYear );
        String disabledString = true == disabledFlag ? "disabled=\"disabled\"" : " ";
        StringBuilder selectYear = new StringBuilder();
        selectYear.append( "<select name=" )
                .append( selecName )
                .append( disabledString + " onchange=\"setDay(" + parentTagName + ");\">" )
                .append( "<option value=\"0\">-</option>" );
        for( int year = i - yearMinCalcu ; year <= i - yearMaxCalcu ; year++ )
        {
            selectYear.append( "<option value=" )
                    .append( year )
                    .append( year == selectedValue ? " selected=\"selected\">" : ">" )
                    .append( year )
                    .append( "</option>" );
        }
        selectYear.append( "</select>" );
        return selectYear.toString();
    }

    /**
     * 月selectタグ生成
     * 
     * @param selecName select_tagのidName
     * @param currentYear option selected を設定する値
     * @param parentTagName select_tagを含む親タグname
     * @param disabledFlag タグ非活性フラグ
     * @return 月selectタグ文字列
     */
    public static String createSelectMonthValuesMobile(String selecName, String currentMonth, String parentTagName, boolean disabledFlag)
    {
        int selectedValue = null == currentMonth ? 0 : Integer.parseInt( currentMonth );
        String disabledString = true == disabledFlag ? "disabled=\"disabled\"" : " ";
        StringBuilder selectMonth = new StringBuilder();
        selectMonth.append( "<select name=" )
                .append( selecName )
                .append( disabledString + " onchange=\"setDay(" + parentTagName + ");\">" )
                .append( "<option value=\"0\">-</option>" );
        for( int month = 1 ; month <= 12 ; month++ )
        {
            selectMonth.append( "<option value=" )
                    .append( month )
                    .append( month == selectedValue ? " selected=\"selected\">" : ">" )
                    .append( month )
                    .append( "</option>" );
        }
        selectMonth.append( "</select>" );
        return selectMonth.toString();
    }

    /**
     * 日selectタグ生成
     * 
     * @param selecName select_tagのidName
     * @param currentYear option selected を設定する値
     * @param parentTagName select_tagを含む親タグname
     * @param disabledFlag タグ非活性フラグ
     * @return 日selectタグ文字列
     */
    public static String createSelectDayValuesMobile(String selecName, String currentDay, String parentTagName, boolean disabledFlag)
    {
        int selectedValue = null == currentDay ? 0 : Integer.parseInt( currentDay );
        String disabledString = true == disabledFlag ? "disabled=\"disabled\"" : " ";
        StringBuilder selectDay = new StringBuilder();
        selectDay.append( "<select name=" )
                .append( selecName )
                .append( disabledString + " onchange=\"setDay(" + parentTagName + ");\">" )
                .append( "<option value=\"0\">-</option>" );
        for( int day = 1 ; day <= 31 ; day++ )
        {
            selectDay.append( "<option value=" )
                    .append( day )
                    .append( day == selectedValue ? " selected=\"selected\">" : ">" )
                    .append( day )
                    .append( "</option>" );
        }
        selectDay.append( "</select>" );
        return selectDay.toString();
    }

}
