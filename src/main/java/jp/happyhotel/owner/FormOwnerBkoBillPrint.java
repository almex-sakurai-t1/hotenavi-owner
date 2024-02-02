package jp.happyhotel.owner;

import java.util.Calendar;

/*
 * ���������sForm�N���X
 */
public class FormOwnerBkoBillPrint
{
    private int     selHotelID    = 0;
    private String  errMsg        = "";

    // ��������
    private int     billYear      = 0;
    private int     billMonth     = 0;
    private int     issueYear     = 0;
    private int     issueMonth    = 0;
    private int     issueDay      = 0;
    private String  chkReissue    = "";
    private int     imediaFlg     = 0;    // ������
    private boolean blankCloseFlg = false;

    public FormOwnerBkoBillPrint()
    {
        Calendar cal = Calendar.getInstance();
        billYear = cal.get( Calendar.YEAR );
        billMonth = cal.get( Calendar.MONTH ) + 1;

        /**
         * // �O��{���ߔN�������擾
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
     * billYear���擾���܂��B
     * 
     * @return billYear
     */
    public int getBillYear()
    {
        return billYear;
    }

    /**
     * billYear��ݒ肵�܂��B
     * 
     * @param billYear billYear
     */
    public void setBillYear(int billYear)
    {
        this.billYear = billYear;
    }

    /**
     * billMonth���擾���܂��B
     * 
     * @return billMonth
     */
    public int getBillMonth()
    {
        return billMonth;
    }

    /**
     * billMonth��ݒ肵�܂��B
     * 
     * @param billMonth billMonth
     */
    public void setBillMonth(int billMonth)
    {
        this.billMonth = billMonth;
    }

    /**
     * issueYear���擾���܂��B
     * 
     * @return issueYear
     */
    public int getIssueYear()
    {
        return issueYear;
    }

    /**
     * issueYear��ݒ肵�܂��B
     * 
     * @param issueYear issueYear
     */
    public void setIssueYear(int issueYear)
    {
        this.issueYear = issueYear;
    }

    /**
     * issueMonth���擾���܂��B
     * 
     * @return issueMonth
     */
    public int getIssueMonth()
    {
        return issueMonth;
    }

    /**
     * issueMonth��ݒ肵�܂��B
     * 
     * @param issueMonth issueMonth
     */
    public void setIssueMonth(int issueMonth)
    {
        this.issueMonth = issueMonth;
    }

    /**
     * issueDay���擾���܂��B
     * 
     * @return issueDay
     */
    public int getIssueDay()
    {
        return issueDay;
    }

    /**
     * issueDay��ݒ肵�܂��B
     * 
     * @param issueDay issueDay
     */
    public void setIssueDay(int issueDay)
    {
        this.issueDay = issueDay;
    }

    /**
     * chkReissue���擾���܂��B
     * 
     * @return chkReissue
     */
    public String getChkReissue()
    {
        return chkReissue;
    }

    /**
     * chkReissue��ݒ肵�܂��B
     * 
     * @param chkReissue chkReissue
     */
    public void setChkReissue(String chkReissue)
    {
        this.chkReissue = chkReissue;
    }

    /**
     * imediaFlg���擾���܂��B
     * 
     * @return imediaFlg
     */
    public int getImediaFlg()
    {
        return imediaFlg;
    }

    /**
     * imediaFlg��ݒ肵�܂��B
     * 
     * @param imediaFlg imediaFlg
     */
    public void setImediaFlg(int imediaFlg)
    {
        this.imediaFlg = imediaFlg;
    }

    /**
     * BlankFlg��ݒ肵�܂��B
     * 
     * @return BlankFlg
     */
    public void setBlankCloseFlg(boolean blankCloseFlg)
    {
        this.blankCloseFlg = blankCloseFlg;
    }

    /**
     * BlankFlg���擾���܂��B
     * 
     * @return BlankFlg
     */
    public boolean isBlankCloseFlg()
    {
        return blankCloseFlg;
    }

}
