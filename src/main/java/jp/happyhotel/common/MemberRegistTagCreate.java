package jp.happyhotel.common;

public class MemberRegistTagCreate
{

    public static final int yearMaxCalcu = 18;
    public static final int yearMinCalcu = 80;

    /**
     * �Nselect�^�O����
     * 
     * @param selecName select_tag��idName
     * @param currentYear option selected ��ݒ肷��l
     * @param parentTagName select_tag���܂ސe�^�Oname
     * @param yearMaxCalcu ����v���_�E���̏���l(���ݔN - ���Z�l)���Z�o����ׂ̌��Z�l
     * @param yearMinCalcu ����v���_�E���̉����l(���ݔN - ���Z�l)���Z�o����ׂ̌��Z�l
     * @param disabledFlag �^�O�񊈐��t���O
     * @return �Nselect�^�O������
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
     * ��select�^�O����
     * 
     * @param selecName select_tag��idName
     * @param currentYear option selected ��ݒ肷��l
     * @param parentTagName select_tag���܂ސe�^�Oname
     * @param disabledFlag �^�O�񊈐��t���O
     * @return ��select�^�O������
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
     * ��select�^�O����
     * 
     * @param selecName select_tag��idName
     * @param currentYear option selected ��ݒ肷��l
     * @param parentTagName select_tag���܂ސe�^�Oname
     * @param disabledFlag �^�O�񊈐��t���O
     * @return ��select�^�O������
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
     * �񊈐� �Nselect�^�O����
     * 
     * @param name select_tag��id
     * @param value
     * @return �񊈐��v���_�E���^�O������
     */
    public static String createDisabledSelectYearTag(String name, String value)
    {
        return "<select class=\"narrow\" name=\"" + name + "\" style=\"width:150px;font-size:160%;\" disabled=\"disabled\">"
                + "<option value=" + value + ">" + value + "</option>"
                + "</select>";
    }

    /**
     * �񊈐� ��select�^�O����
     * 
     * @param name select_tag��id
     * @param value
     * @return �񊈐��v���_�E���^�O������
     */
    public static String createDisabledSelectMonthTag(String name, String value)
    {
        return "<select class=\"narrow\" name=\"" + name + "\" style=\"width:120px;font-size:160%;\" disabled=\"disabled\">"
                + "<option value=" + value + ">" + value + "</option>"
                + "</select>";
    }

    /**
     * �񊈐� ��select�^�O����
     * 
     * @param name select_tag��id
     * @param value
     * @return �񊈐��v���_�E���^�O������
     */
    public static String createDisabledSelectDayTag(String name, String value)
    {
        return "<select class=\"narrow\" name=\"" + name + "\" style=\"width:120px;font-size:160%;\" disabled=\"disabled\">"
                + "<option value=" + value + ">" + value + "</option>"
                + "</select>";
    }

    /**
     * ����selectTag�쐬
     * 
     * @param value �f�t�H���g�I��l
     * @return ����selectTag������
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
                + "<option value=1" + manSelected + ">�j��</option>"
                + "<option value=2" + womanSelected + ">����</option>"
                + "</select>";
    }

    /**
     * ���[���z�MselectTag�쐬
     * 
     * @param value �f�t�H���g�I��l
     * @return ���[���z�MselectTag������
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
                + "<option value=1" + distribute + ">�z�M����</option>"
                + "<option value=2" + undistribute + ">�z�M���Ȃ�</option>"
                + "</select>";
    }

    /**
     * �Nselect�^�O����
     * 
     * @param selecName select_tag��idName
     * @param currentYear option selected ��ݒ肷��l
     * @param parentTagName select_tag���܂ސe�^�Oname
     * @param yearMaxCalcu ����v���_�E���̏���l(���ݔN - ���Z�l)���Z�o����ׂ̌��Z�l
     * @param yearMinCalcu ����v���_�E���̉����l(���ݔN - ���Z�l)���Z�o����ׂ̌��Z�l
     * @param disabledFlag �^�O�񊈐��t���O
     * @return �Nselect�^�O������
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
     * ��select�^�O����
     * 
     * @param selecName select_tag��idName
     * @param currentYear option selected ��ݒ肷��l
     * @param parentTagName select_tag���܂ސe�^�Oname
     * @param disabledFlag �^�O�񊈐��t���O
     * @return ��select�^�O������
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
     * ��select�^�O����
     * 
     * @param selecName select_tag��idName
     * @param currentYear option selected ��ݒ肷��l
     * @param parentTagName select_tag���܂ސe�^�Oname
     * @param disabledFlag �^�O�񊈐��t���O
     * @return ��select�^�O������
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
