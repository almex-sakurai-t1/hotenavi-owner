package jp.happyhotel.owner;

import java.util.Calendar;

/*
 * 請求書発行Formクラス
 */
public class FormOwnerBkoBillPrint
{
    private int     selHotelID    = 0;
    private String  errMsg        = "";

    // 検索条件
    private int     billYear      = 0;
    private int     billMonth     = 0;
    private int     issueYear     = 0;
    private int     issueMonth    = 0;
    private int     issueDay      = 0;
    private String  chkReissue    = "";
    private int     imediaFlg     = 0;    // 事務局
    private boolean blankCloseFlg = false;

    public FormOwnerBkoBillPrint()
    {
        Calendar cal = Calendar.getInstance();
        billYear = cal.get( Calendar.YEAR );
        billMonth = cal.get( Calendar.MONTH ) + 1;

        /**
         * // 前回本締め年月日を取得
         * String query = "select max(last_update) as last_update from hh_bko_closing_control where closing_kind = 3";
         * 
         * try
         * {
         * Connection connection = DBConnection.getConnection( true );
         * PreparedStatement prestate = connection.prepareStatement( query );
         * ResultSet result = prestate.executeQuery();
         * int lastUpdate = 0;
         * if ( result != null )
         * {
         * if ( result.next() != false )
         * {
         * lastUpdate = result.getInt( "last_update" );
         * }
         * }
         * 
         * if ( lastUpdate != 0 )
         * {
         * issueYear = lastUpdate / 10000;
         * issueMonth = lastUpdate / 100 % 100;
         * issueDay = lastUpdate % 100;
         * }
         * }
         * catch ( Exception e )
         * {
         * }
         */
        issueYear = billYear;
        issueMonth = billMonth;
        issueDay = 17;
    }

    public int getSelHotelID()
    {
        return selHotelID;
    }

    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    /**
     * billYearを取得します。
     * 
     * @return billYear
     */
    public int getBillYear()
    {
        return billYear;
    }

    /**
     * billYearを設定します。
     * 
     * @param billYear billYear
     */
    public void setBillYear(int billYear)
    {
        this.billYear = billYear;
    }

    /**
     * billMonthを取得します。
     * 
     * @return billMonth
     */
    public int getBillMonth()
    {
        return billMonth;
    }

    /**
     * billMonthを設定します。
     * 
     * @param billMonth billMonth
     */
    public void setBillMonth(int billMonth)
    {
        this.billMonth = billMonth;
    }

    /**
     * issueYearを取得します。
     * 
     * @return issueYear
     */
    public int getIssueYear()
    {
        return issueYear;
    }

    /**
     * issueYearを設定します。
     * 
     * @param issueYear issueYear
     */
    public void setIssueYear(int issueYear)
    {
        this.issueYear = issueYear;
    }

    /**
     * issueMonthを取得します。
     * 
     * @return issueMonth
     */
    public int getIssueMonth()
    {
        return issueMonth;
    }

    /**
     * issueMonthを設定します。
     * 
     * @param issueMonth issueMonth
     */
    public void setIssueMonth(int issueMonth)
    {
        this.issueMonth = issueMonth;
    }

    /**
     * issueDayを取得します。
     * 
     * @return issueDay
     */
    public int getIssueDay()
    {
        return issueDay;
    }

    /**
     * issueDayを設定します。
     * 
     * @param issueDay issueDay
     */
    public void setIssueDay(int issueDay)
    {
        this.issueDay = issueDay;
    }

    /**
     * chkReissueを取得します。
     * 
     * @return chkReissue
     */
    public String getChkReissue()
    {
        return chkReissue;
    }

    /**
     * chkReissueを設定します。
     * 
     * @param chkReissue chkReissue
     */
    public void setChkReissue(String chkReissue)
    {
        this.chkReissue = chkReissue;
    }

    /**
     * imediaFlgを取得します。
     * 
     * @return imediaFlg
     */
    public int getImediaFlg()
    {
        return imediaFlg;
    }

    /**
     * imediaFlgを設定します。
     * 
     * @param imediaFlg imediaFlg
     */
    public void setImediaFlg(int imediaFlg)
    {
        this.imediaFlg = imediaFlg;
    }

    /**
     * BlankFlgを設定します。
     * 
     * @return BlankFlg
     */
    public void setBlankCloseFlg(boolean blankCloseFlg)
    {
        this.blankCloseFlg = blankCloseFlg;
    }

    /**
     * BlankFlgを取得します。
     * 
     * @return BlankFlg
     */
    public boolean isBlankCloseFlg()
    {
        return blankCloseFlg;
    }

}
