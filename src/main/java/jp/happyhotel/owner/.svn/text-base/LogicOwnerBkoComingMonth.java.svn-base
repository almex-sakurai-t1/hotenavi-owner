package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerBkoCommon;

/**
 * 月別来店状況確認ビジネスロジック
 */
public class LogicOwnerBkoComingMonth implements Serializable
{

    private static final long       serialVersionUID = 6234968061831008635L;

    private FormOwnerBkoComingMonth frm;

    /* フォームオブジェクト */
    public FormOwnerBkoComingMonth getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoComingMonth frm)
    {
        this.frm = frm;
    }

    /**
     * 売掛データ取得
     * 
     * @param なし
     * @return なし
     */
    public void getAccountRecv() throws Exception
    {
        int targetDateFrom = 0;
        int targetDateTo = 0;
        int raitenCnt = 0;
        int raitenPoint = 0;
        int seisanCnt = 0;
        int seisanPoint = 0;
        int seisanAmount = 0;
        int genzanAmount = 0;
        int genzanPoint = 0;
        int rsvCnt = 0;
        int rsvPoint = 0;
        int rsvBonus = 0;
        int rsvSiyou = 0;
        int billAmount = 0;
        int sumRaitenCnt = 0;
        int sumRaitenPoint = 0;
        int sumSeisanCnt = 0;
        int sumSeisanPoint = 0;
        int sumGenzanPoint = 0;
        int sumRsvCnt = 0;
        int sumRsvPoint = 0;
        int sumRsvBonus = 0;
        int sumRsvSiyou = 0;
        int sumBillAmount = 0;
        NumberFormat formatCur = NumberFormat.getNumberInstance();
        ArrayList<Integer> raitenCntList = new ArrayList<Integer>();
        ArrayList<String> raitenPointList = new ArrayList<String>();
        ArrayList<Integer> seisanCntList = new ArrayList<Integer>();
        ArrayList<String> seisanPointList = new ArrayList<String>();
        ArrayList<String> genzanAmountList = new ArrayList<String>();
        ArrayList<Integer> rsvCntList = new ArrayList<Integer>();
        ArrayList<String> rsvPointList = new ArrayList<String>();
        ArrayList<String> billAmountList = new ArrayList<String>();
        ArrayList<String> rsvBonusList = new ArrayList<String>();
        ArrayList<String> rsvSiyouList = new ArrayList<String>();

        // 売掛ヘッダー情報取得(利用日、予約金額の合計)
        getAccountRecvHeader();

        // 科目コード「100」のデータ取得
        for( int i = 0 ; i < frm.getDateList().size() ; i++ )
        {
            targetDateFrom = Integer.parseInt( Integer.toString( frm.getDateList().get( i ) ) + "00" );
            targetDateTo = Integer.parseInt( Integer.toString( frm.getDateList().get( i ) ) + "99" );

            // 「清算」 組数、金額、加算ポイント取得
            seisanCnt = getAccountRecvDetailRowCnt( OwnerBkoCommon.ACCOUNT_TITLE_CD_110, targetDateFrom, targetDateTo );
            seisanPoint = getAccountRecvDetail( OwnerBkoCommon.ACCOUNT_TITLE_CD_110, targetDateFrom, targetDateTo, 1 );
            seisanCntList.add( seisanCnt );
            seisanPointList.add( String.format( "%1$,3d", seisanPoint ) );

            // 「使用マイル」 金額取得
            genzanAmount = getAccountRecvDetail( OwnerBkoCommon.ACCOUNT_TITLE_CD_120, targetDateFrom, targetDateTo, 2 );
            genzanPoint = getAccountRecvDetail( OwnerBkoCommon.ACCOUNT_TITLE_CD_120, targetDateFrom, targetDateTo, 1 );
            if ( genzanPoint == 0 )
            {
                genzanAmountList.add( "" );
            }
            else
            {
                genzanAmountList.add( String.format( "%1$,3d", (genzanPoint * -1) ) );
            }

            // 「予約」 組数、加算ポイント取得
            rsvCnt = getAccountRecvDetailRowCnt( OwnerBkoCommon.ACCOUNT_TITLE_CD_130, targetDateFrom, targetDateTo );
            rsvPoint = getAccountRecvDetail( OwnerBkoCommon.ACCOUNT_TITLE_CD_130, targetDateFrom, targetDateTo, 1 );
            rsvCntList.add( rsvCnt );
            rsvPointList.add( String.format( "%1$,3d", rsvPoint ) );

            // 「予約」 ボーナスマイル取得
            rsvBonus = getAccountRecvDetail( OwnerBkoCommon.ACCOUNT_TITLE_CD_150, targetDateFrom, targetDateTo, 1 );
            if ( rsvBonus == 0 )
            {
                rsvBonusList.add( "" );
            }
            else
            {
                rsvBonusList.add( String.format( "%1$,3d", rsvBonus ) );
            }

            // 「予約」 使用マイル取得
            rsvSiyou = getAccountRecvDetailRsv( OwnerBkoCommon.ACCOUNT_TITLE_CD_120, targetDateFrom, targetDateTo, 1 );
            if ( rsvSiyou == 0 )
            {
                rsvSiyouList.add( "" );
            }
            else
            {
                rsvSiyouList.add( String.format( "%1$,3d", (rsvSiyou * -1) ) );
            }

            // ご請求金額
            seisanAmount = getAccountRecvDetail( OwnerBkoCommon.ACCOUNT_TITLE_CD_110, targetDateFrom, targetDateTo, 2 );
            billAmount = seisanAmount + (genzanAmount);

            // 画面では、ホテル側から見たときの収支として確認するため、＋、－の符号を逆にして表示する
            billAmount = billAmount * -1;
            billAmountList.add( formatCur.format( billAmount ) );

            sumRaitenCnt = sumRaitenCnt + raitenCnt;
            sumRaitenPoint = sumRaitenPoint + raitenPoint;
            sumSeisanCnt = sumSeisanCnt + seisanCnt;
            sumSeisanPoint = sumSeisanPoint + seisanPoint;
            sumGenzanPoint = sumGenzanPoint + genzanPoint;
            sumRsvCnt = sumRsvCnt + rsvCnt;
            sumRsvPoint = sumRsvPoint + rsvPoint;
            sumRsvBonus = sumRsvBonus + rsvBonus;
            sumRsvSiyou = sumRsvSiyou + rsvSiyou;
            sumBillAmount = sumBillAmount + billAmount;
        }

        // フォームにセット
        frm.setRaitenCntList( raitenCntList );
        frm.setRaitenPointList( raitenPointList );
        frm.setSeisanCntList( seisanCntList );
        frm.setSeisanPointList( seisanPointList );
        frm.setGenzanAmountList( genzanAmountList );
        frm.setRsvCntList( rsvCntList );
        frm.setRsvPointList( rsvPointList );
        frm.setRsvBonusList( rsvBonusList );
        frm.setRsvSiyouList( rsvSiyouList );
        frm.setBillAmountList( billAmountList );
        frm.setSumRaitenCnt( String.format( "%1$,3d", sumRaitenCnt ) );
        frm.setSumRaitenPoint( String.format( "%1$,3d", sumRaitenPoint ) );
        frm.setSumSeisanCnt( String.format( "%1$,3d", sumSeisanCnt ) );
        frm.setSumSeisanPoint( String.format( "%1$,3d", sumSeisanPoint ) );
        frm.setSumGenzanAmount( String.format( "%1$,3d", (sumGenzanPoint * -1) ) );
        frm.setSumRsvCnt( String.format( "%1$,3d", sumRsvCnt ) );
        frm.setSumRsvPoint( String.format( "%1$,3d", sumRsvPoint ) );
        frm.setSumRsvBonus( String.format( "%1$,3d", sumRsvBonus ) );
        frm.setSumRsvSiyou( String.format( "%1$,3d", (sumRsvSiyou * -1) ) );
        frm.setSumBillAmount( formatCur.format( sumBillAmount ) );
    }

    /**
     * 売掛ヘッダー情報取得
     * 
     * @param なし
     * @return なし
     */
    public void getAccountRecvHeader() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        ArrayList<Integer> usageDateList = new ArrayList<Integer>();
        ArrayList<Integer> usageChargeIntList = new ArrayList<Integer>();
        ArrayList<Integer> rcvChargeIntList = new ArrayList<Integer>();
        ArrayList<String> dateStringList = new ArrayList<String>();
        ArrayList<String> amountList = new ArrayList<String>();
        ArrayList<String> rcvChargeList = new ArrayList<String>();
        ArrayList<Integer> rcvChargeNumList = new ArrayList<Integer>();
        ArrayList<Integer> dateList = new ArrayList<Integer>();
        NumberFormat formatCur = NumberFormat.getNumberInstance();
        int selFrom = 0;
        int selTo = 0;
        int sumUsageCharge = 0;
        int sumRcvCharge = 0;
        int orgDate = 0;
        int newDate = 0;
        int sumTotalUsageCharge = 0;
        int sumTotalRcvCharge = 0;

        query = query + "SELECT DISTINCT rcv.accrecv_slip_no, substring(rcv.usage_date, 1,6) usageDate, rcv.usage_charge, rcv.receive_charge ";
        query = query + "FROM hh_bko_account_recv rcv ";
        query = query + "  INNER JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no ";
        query = query + "  INNER JOIN hh_hotel_newhappie happie ON rcv.id = happie.id AND rcv.usage_date >= happie.bko_date_start ";
        query = query + "WHERE rcv.usage_date BETWEEN ? AND ? ";
        query = query + "  AND rcv.id = ? ";
        query = query + "  AND (detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_100 + " OR ";
        query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_110 + " OR ";
        query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_120 + " OR ";
        query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_130 + " OR ";
        query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_140 + ") ";
        query = query + "  AND rcv.invalid_flag = ? ";
        query = query + "ORDER BY rcv.usage_date ";

        selFrom = Integer.parseInt( Integer.toString( frm.getSelYear() ) + "0000" );
        selTo = Integer.parseInt( Integer.toString( frm.getSelYear() ) + "9999" );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, selFrom );
            prestate.setInt( 2, selTo );
            prestate.setInt( 3, frm.getSelHotelID() );
            prestate.setInt( 4, 0 );
            result = prestate.executeQuery();

            while( result.next() )
            {
                usageDateList.add( result.getInt( "usageDate" ) );
                usageChargeIntList.add( result.getInt( "usage_charge" ) );
                rcvChargeIntList.add( result.getInt( "receive_charge" ) );
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "月別来店状況" ) );
                return;
            }

            // データ生成
            for( int i = 0 ; i < usageDateList.size() ; i++ )
            {
                orgDate = usageDateList.get( i );

                if ( i == 0 )
                {
                    newDate = usageDateList.get( i );
                }

                if ( orgDate == newDate )
                {
                    sumUsageCharge = sumUsageCharge + usageChargeIntList.get( i );
                    sumRcvCharge = sumRcvCharge + rcvChargeIntList.get( i );
                }
                else
                {
                    dateList.add( newDate );
                    dateStringList.add( Integer.toString( newDate ).substring( 0, 4 ) + "/" + Integer.toString( newDate ).substring( 4, 6 ) );
                    amountList.add( formatCur.format( sumUsageCharge ) );
                    rcvChargeList.add( formatCur.format( sumRcvCharge ) );
                    sumTotalUsageCharge = sumTotalUsageCharge + sumUsageCharge;
                    sumTotalRcvCharge = sumTotalRcvCharge + sumRcvCharge;

                    sumUsageCharge = 0;
                    sumRcvCharge = 0;
                    sumUsageCharge = sumUsageCharge + usageChargeIntList.get( i );
                    sumRcvCharge = sumRcvCharge + rcvChargeIntList.get( i );
                }

                if ( i == (usageDateList.size() - 1) )
                {
                    // 最後のデータの場合
                    dateList.add( orgDate );
                    dateStringList.add( Integer.toString( orgDate ).substring( 0, 4 ) + "/" + Integer.toString( orgDate ).substring( 4, 6 ) );
                    amountList.add( formatCur.format( sumUsageCharge ) );
                    rcvChargeList.add( formatCur.format( sumRcvCharge ) );
                    sumTotalUsageCharge = sumTotalUsageCharge + sumUsageCharge;
                    sumTotalRcvCharge = sumTotalRcvCharge + sumRcvCharge;
                }

                newDate = usageDateList.get( i );
            }

            frm.setDateList( dateList );
            frm.setDateStringList( dateStringList );
            frm.setRsvAmountList( rcvChargeList );
            frm.setSeisanAmountList( amountList );
            frm.setRsvAmountNumList( rcvChargeNumList );
            frm.setSumRsvAmount( formatCur.format( sumTotalRcvCharge ) );
            frm.setSumSeisanAmount( formatCur.format( sumTotalUsageCharge ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingMonth.getAccountRecvHeader] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /*
     * 売掛データレコード数取得
     * @param int accTitleCd 科目コード
     * @param int usageMonthFrom 利用月
     * @param int usageMonthTo 利用月
     * @return int レコード件数
     */
    public int getAccountRecvDetailRowCnt(int accTitleCd, int usageMonthFrom, int usageMonthTo) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int retRowCnt = 0;

        query = query + "SELECT  COUNT(rcv.usage_date) as rowCnt ";
        query = query + "FROM hh_bko_account_recv rcv ";
        query = query + "   INNER JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no ";
        query = query + "   INNER JOIN hh_hotel_newhappie happie ON rcv.id = happie.id AND rcv.usage_date >= happie.bko_date_start ";
        query = query + "WHERE rcv.id = ? ";
        query = query + "  AND rcv.usage_date BETWEEN ? AND ?";
        query = query + "  AND detail.account_title_cd = ? ";
        query = query + "  AND rcv.invalid_flag = ? ";
        query = query + "GROUP BY substring(rcv.usage_date, 1, 6) ";
        query = query + "ORDER BY rcv.usage_date ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, usageMonthFrom );
            prestate.setInt( 3, usageMonthTo );
            prestate.setInt( 4, accTitleCd );
            prestate.setInt( 5, 0 );
            result = prestate.executeQuery();

            while( result.next() )
            {
                retRowCnt = result.getInt( "rowCnt" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingMonth.getAccountRecvDetailRowCnt] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(retRowCnt);
    }

    /*
     * 売掛明細情報取得
     * @param int accTitleCd 科目コード
     * @param int usageDate 利用日
     * @param int selKbn (1:ポイント取得、2：金額取得)
     * @return int ポイント、または金額
     */
    public int getAccountRecvDetail(int accTitleCd, int usageMonthFrom, int usageMonthTo, int selKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int sumPoint = 0;
        int sumAmount = 0;
        int ret = 0;

        query = query + "SELECT SUM(detail.point) as sumPoint, SUM(detail.amount) as sumAmount ";
        query = query + "FROM hh_bko_account_recv rcv ";
        query = query + "   INNER JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no ";
        query = query + "   INNER JOIN hh_hotel_newhappie happie ON rcv.id = happie.id AND rcv.usage_date >= happie.bko_date_start ";
        query = query + "WHERE rcv.id = ? ";
        query = query + "  AND rcv.usage_date BETWEEN ? AND ? ";
        query = query + "  AND detail.account_title_cd = ? ";
        query = query + "  AND rcv.invalid_flag = ? ";
        query = query + "ORDER BY rcv.usage_date ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, usageMonthFrom );
            prestate.setInt( 3, usageMonthTo );
            prestate.setInt( 4, accTitleCd );
            prestate.setInt( 5, 0 );
            result = prestate.executeQuery();

            while( result.next() )
            {
                sumPoint = result.getInt( "sumPoint" );
                sumAmount = result.getInt( "sumAmount" );
            }

            if ( selKbn == 1 )
            {
                ret = sumPoint;
            }
            else
            {
                ret = sumAmount;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingMonth.getAccountRecvDetail] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /*
     * 売掛明細情報取得(予約)
     * @param int accTitleCd 科目コード
     * @param int usageDate 利用日
     * @param int selKbn (1:ポイント取得、2：金額取得)
     * @return int ポイント、または金額
     */
    public int getAccountRecvDetailRsv(int accTitleCd, int usageMonthFrom, int usageMonthTo, int selKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int sumPoint = 0;
        int sumAmount = 0;
        int ret = 0;

        query = query + "SELECT SUM(detail.point) as sumPoint, SUM(detail.amount) as sumAmount ";
        query = query + "FROM hh_bko_account_recv rcv ";
        query = query + "   INNER JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no AND detail.reserve_no != '' ";
        query = query + "   INNER JOIN hh_hotel_newhappie happie ON rcv.id = happie.id AND rcv.usage_date >= happie.bko_date_start ";
        query = query + "WHERE rcv.id = ? ";
        query = query + "  AND rcv.usage_date BETWEEN ? AND ? ";
        query = query + "  AND detail.account_title_cd = ? ";
        query = query + "  AND rcv.invalid_flag = ? ";
        query = query + "ORDER BY rcv.usage_date ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, usageMonthFrom );
            prestate.setInt( 3, usageMonthTo );
            prestate.setInt( 4, accTitleCd );
            prestate.setInt( 5, 0 );
            result = prestate.executeQuery();

            while( result.next() )
            {
                sumPoint = result.getInt( "sumPoint" );
                sumAmount = result.getInt( "sumAmount" );
            }

            if ( selKbn == 1 )
            {
                ret = sumPoint;
            }
            else
            {
                ret = sumAmount;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingMonth.getAccountRecvDetail] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }
}
