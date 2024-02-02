package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerBkoCommon;

/**
 * ハピホテマイル履歴ビジネスロジック
 */
public class LogicOwnerBkoComingToday implements Serializable
{
    private static final long       serialVersionUID = 7891464730793037841L;

    private FormOwnerBkoComingToday frm;

    /* フォームオブジェクト */
    public FormOwnerBkoComingToday getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoComingToday frm)
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
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        String ciDate = "";
        int sumAccrecvAmount = 0;
        ArrayList<String> ciDateList = new ArrayList<String>();
        ArrayList<String> ciTimeList = new ArrayList<String>();
        ArrayList<String> personNmList = new ArrayList<String>();
        ArrayList<Integer> customerIdList = new ArrayList<Integer>();
        ArrayList<Integer> slipNoList = new ArrayList<Integer>();
        ArrayList<String> seqList = new ArrayList<String>();
        ArrayList<Integer> htSlipNoList = new ArrayList<Integer>();
        ArrayList<String> accrecvAmountList = new ArrayList<String>();
        ArrayList<Integer> accrecvAmountIntList = new ArrayList<Integer>();
        ArrayList<String> closingList = new ArrayList<String>();
        NumberFormat formatCur = NumberFormat.getNumberInstance();

        query = query + "SELECT DISTINCT rcv.accrecv_slip_no, rcv.id, rcv.usage_date, rcv.usage_time, ";
        query = query + "rcv.person_name, rcv.user_management_no, rcv.ht_slip_no, rcv.ht_room_no, usage_charge, rcv.regist_flag ";
        query = query + "FROM hh_bko_account_recv rcv ";
        query = query + "  LEFT JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no ";
        query = query + "  INNER JOIN hh_hotel_newhappie happie ON rcv.id = happie.id AND rcv.usage_date >= happie.bko_date_start";
        query = query + " WHERE rcv.usage_date BETWEEN ? AND ? ";
        query = query + "  AND rcv.id = ? ";
        query = query + "  AND (detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_100 + " OR ";
        query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_110 + " OR ";
        query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_120 + " OR ";
        query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_130 + " OR ";
        // query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_140 + " OR ";
        query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_150 + ") ";
        query = query + "  AND rcv.invalid_flag = ? ";
        if ( frm.getSelCustomerId() != -99 )
        {
            query = query + " AND rcv.user_management_no = ? ";
        }
        query = query + "ORDER BY rcv.usage_date desc, rcv.usage_time desc , rcv.user_management_no ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getCiTimeFrom() );
            prestate.setInt( 2, frm.getCiTimeTo() );
            prestate.setInt( 3, frm.getSelHotelID() );
            prestate.setInt( 4, 0 );
            if ( frm.getSelCustomerId() != -99 )
            {
                prestate.setInt( 5, frm.getSelCustomerId() );
            }
            result = prestate.executeQuery();

            while( result.next() )
            {
                frm.setSelHotelID( result.getInt( "id" ) );
                slipNoList.add( result.getInt( "accrecv_slip_no" ) );
                ciDate = result.getString( "usage_date" ).substring( 0, 4 ) + "/" + result.getString( "usage_date" ).substring( 4, 6 ) + "/" + result.getString( "usage_date" ).substring( 6 );
                ciDateList.add( ciDate );
                ciTimeList.add( ConvertTime.convTimeHHMM( result.getInt( "usage_time" ), 0 ) );
                personNmList.add( ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( result.getString( "person_name" ) ) ) );
                customerIdList.add( result.getInt( "user_management_no" ) );
                seqList.add( result.getString( "ht_room_no" ) );
                htSlipNoList.add( result.getInt( "ht_slip_no" ) );
                if ( result.getInt( "usage_charge" ) == 0 )
                {
                    accrecvAmountList.add( "" );
                }
                else
                {
                    accrecvAmountList.add( formatCur.format( result.getInt( "usage_charge" ) ) );
                }
                accrecvAmountIntList.add( result.getInt( "usage_charge" ) );
                sumAccrecvAmount = sumAccrecvAmount + result.getInt( "usage_charge" );
                if ( result.getInt( "rcv.regist_flag" ) == OwnerBkoCommon.REGIST_FLG_KARI )
                {
                    closingList.add( OwnerBkoCommon.REGIST_NM_KARI );
                }
                else
                {
                    closingList.add( "" );
                }
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "ハピホテマイル履歴" ) );
                return;
            }

            frm.setUsageDate( ciDateList );
            frm.setUsageTime( ciTimeList );
            frm.setPersonNm( personNmList );
            frm.setCustomerId( customerIdList );
            frm.setSlipNoList( slipNoList );
            frm.setSeq( seqList );
            frm.setAccrecvAmount( accrecvAmountList );
            frm.setHtSlipNo( htSlipNoList );
            frm.setSumUsageCharrge( formatCur.format( sumAccrecvAmount ) );
            frm.setRecMaxCnt( count );
            frm.setClosingList( closingList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingToday.getAccountRecv] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 売掛明細取得
     * 
     * @param なし
     * @return なし
     */
    public void getAccountRecvDetail() throws Exception
    {
        int addPoint = 0;
        int subtractPoint = 0;
        int sumAddPoint = 0;
        int sumSubtractPoint = 0;
        String amount = "";
        String subtract = "";
        ArrayList<String> addAmountList = new ArrayList<String>();
        ArrayList<String> subtractList = new ArrayList<String>();

        // 科目コード、科目名の取得
        getAccountTitle();

        for( int i = 0 ; i < frm.getSlipNoList().size() ; i++ )
        {
            subtract = "";
            amount = "";

            // 金額合計取得
            addPoint = getSumPoint( 2, frm.getSlipNoList().get( i ) ); // 割引以外
            subtractPoint = getSumPoint( 1, frm.getSlipNoList().get( i ) ); // 割引
            if ( addPoint != 0 )
            {
                amount = String.format( "%1$,3d", addPoint );
            }
            addAmountList.add( amount );
            if ( subtractPoint != 0 )
            {
                subtract = String.format( "%1$,3d", subtractPoint * -1 );
            }
            subtractList.add( subtract );
            sumAddPoint = sumAddPoint + addPoint;
            sumSubtractPoint = sumSubtractPoint + subtractPoint;
        }
        frm.setAddAmountList( addAmountList );
        frm.setSubtractAmountList( subtractList );
        frm.setSumAddAmount( String.format( "%1$,3d", sumAddPoint ) );
        frm.setSumSubtractAmount( String.format( "%1$,3d", (sumSubtractPoint * -1) ) );
    }

    /**
     * 売掛明細の科目名取得
     * 
     * @param なし
     * @return なし
     */
    public void getAccountTitle() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        int orgSlipNo = 0;
        int newSlipNo = 0;
        ArrayList<Integer> slipNoList = new ArrayList<Integer>();
        ArrayList<Integer> newSlipNoList = new ArrayList<Integer>();
        ArrayList<Integer> accounTitleCdList = new ArrayList<Integer>();
        ArrayList<String> raitenList = new ArrayList<String>();
        ArrayList<String> huyoList = new ArrayList<String>();
        ArrayList<String> siyouList = new ArrayList<String>();
        ArrayList<String> yoyakuList = new ArrayList<String>();
        ArrayList<String> bonusList = new ArrayList<String>();
        int idx = 0;

        query = query + "SELECT detail.accrecv_slip_no, detail.account_title_cd ";
        query = query + "FROM hh_bko_account_recv rcv ";
        query = query + "   LEFT JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no ";
        query = query + "WHERE rcv.usage_date BETWEEN ? AND ? ";
        query = query + "  AND rcv.id = ? ";
        query = query + "  AND (detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_100 + " OR ";
        query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_110 + " OR ";
        query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_120 + " OR ";
        query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_130 + " OR ";
        // query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_140 + " OR ";
        query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_150 + ") ";
        query = query + "  AND rcv.invalid_flag = ? ";
        if ( frm.getSelCustomerId() != -99 )
        {
            query = query + " AND rcv.user_management_no = ? ";
        }
        query = query + "ORDER BY rcv.usage_date desc, rcv.usage_time desc , rcv.user_management_no, detail.accrecv_slip_no, detail.account_title_cd ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getCiTimeFrom() );
            prestate.setInt( 2, frm.getCiTimeTo() );
            prestate.setInt( 3, frm.getSelHotelID() );
            prestate.setInt( 4, 0 );
            if ( frm.getSelCustomerId() != -99 )
            {
                prestate.setInt( 5, frm.getSelCustomerId() );
            }
            result = prestate.executeQuery();

            while( result.next() )
            {
                slipNoList.add( result.getInt( "accrecv_slip_no" ) );
                accounTitleCdList.add( result.getInt( "account_title_cd" ) );
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "ハピホテマイル履歴" ) );
                return;
            }

            // 科目名の整形
            for( int i = 0 ; i < slipNoList.size() ; i++ )
            {
                orgSlipNo = slipNoList.get( i );
                if ( i == 0 )
                {
                    newSlipNo = slipNoList.get( i );
                    switch( accounTitleCdList.get( i ) )
                    {
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_100:
                            raitenList.add( "○" );
                            huyoList.add( "" );
                            siyouList.add( "" );
                            yoyakuList.add( "" );
                            bonusList.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_110:
                            raitenList.add( "" );
                            huyoList.add( "○" );
                            siyouList.add( "" );
                            yoyakuList.add( "" );
                            bonusList.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_120:
                            raitenList.add( "" );
                            huyoList.add( "" );
                            siyouList.add( "○" );
                            yoyakuList.add( "" );
                            bonusList.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_130:
                            raitenList.add( "" );
                            huyoList.add( "" );
                            siyouList.add( "" );
                            yoyakuList.add( "○" );
                            bonusList.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_140:
                            raitenList.add( "" );
                            huyoList.add( "" );
                            siyouList.add( "" );
                            yoyakuList.add( "○" );
                            bonusList.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_150:
                            raitenList.add( "" );
                            huyoList.add( "" );
                            siyouList.add( "" );
                            yoyakuList.add( "" );
                            bonusList.add( "○" );
                            break;
                    }
                }

                if ( orgSlipNo == newSlipNo )
                {
                    if ( i != 0 )
                    {
                        switch( accounTitleCdList.get( i ) )
                        {
                            case OwnerBkoCommon.ACCOUNT_TITLE_CD_100:
                                raitenList.set( idx, "○" );
                                break;
                            case OwnerBkoCommon.ACCOUNT_TITLE_CD_110:
                                huyoList.set( idx, "○" );
                                break;
                            case OwnerBkoCommon.ACCOUNT_TITLE_CD_120:
                                siyouList.set( idx, "○" );
                                break;
                            case OwnerBkoCommon.ACCOUNT_TITLE_CD_130:
                                yoyakuList.set( idx, "○" );
                                break;
                            case OwnerBkoCommon.ACCOUNT_TITLE_CD_140:
                                yoyakuList.set( idx, "○" );
                                break;
                            case OwnerBkoCommon.ACCOUNT_TITLE_CD_150:
                                bonusList.set( idx, "○" );
                                break;
                        }
                    }
                }
                else
                {
                    // 値を格納
                    switch( accounTitleCdList.get( i ) )
                    {
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_100:
                            raitenList.add( "○" );
                            huyoList.add( "" );
                            siyouList.add( "" );
                            yoyakuList.add( "" );
                            bonusList.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_110:
                            raitenList.add( "" );
                            huyoList.add( "○" );
                            siyouList.add( "" );
                            yoyakuList.add( "" );
                            bonusList.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_120:
                            raitenList.add( "" );
                            huyoList.add( "" );
                            siyouList.add( "○" );
                            yoyakuList.add( "" );
                            bonusList.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_130:
                            raitenList.add( "" );
                            huyoList.add( "" );
                            siyouList.add( "" );
                            yoyakuList.add( "○" );
                            bonusList.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_140:
                            raitenList.add( "" );
                            huyoList.add( "" );
                            siyouList.add( "" );
                            yoyakuList.add( "○" );
                            bonusList.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_150:
                            raitenList.add( "" );
                            huyoList.add( "" );
                            siyouList.add( "" );
                            yoyakuList.add( "" );
                            bonusList.add( "○" );
                            break;
                    }
                    newSlipNoList.add( newSlipNo );
                    idx++;
                }
                newSlipNo = orgSlipNo;

            }

            frm.setRaitenList( raitenList );
            frm.setHuyoList( huyoList );
            frm.setSiyouList( siyouList );
            frm.setReserveList( yoyakuList );
            frm.setBonusList( bonusList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingToday.getAccountTitle] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 科目ごとの合計
     * 
     * @param int selKbn(1:割引が対象、2:割引以外が対象)
     * @param int slipNo 売掛伝票No.
     * @return 対象伝票の
     */
    public int getSumPoint(int selKbn, int slipNo) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int amount = 0;

        query = query + "SELECT SUM(detail.point) AS sumPoint ";
        query = query + "FROM hh_bko_account_recv rcv ";
        query = query + "   LEFT JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no ";
        query = query + "WHERE rcv.usage_date BETWEEN ? AND ? ";
        query = query + "  AND rcv.id = ? ";
        if ( slipNo != -1 )
        {
            query = query + "  AND detail.accrecv_slip_no = ? ";
        }
        if ( selKbn == 1 )
        {
            query = query + "  AND detail.account_title_cd = " + OwnerBkoCommon.ACCOUNT_TITLE_CD_120;
        }
        else
        {
            query = query + "  AND (detail.account_title_cd = " + OwnerBkoCommon.ACCOUNT_TITLE_CD_100 + " OR " +
                    "detail.account_title_cd = " + OwnerBkoCommon.ACCOUNT_TITLE_CD_110 + " OR " +
                    "detail.account_title_cd = " + OwnerBkoCommon.ACCOUNT_TITLE_CD_150 + " OR " +
                    "detail.account_title_cd = " + OwnerBkoCommon.ACCOUNT_TITLE_CD_130 + ") ";
        }
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getCiTimeFrom() );
            prestate.setInt( 2, frm.getCiTimeTo() );
            prestate.setInt( 3, frm.getSelHotelID() );
            if ( slipNo != -1 )
            {
                prestate.setInt( 4, slipNo );
            }
            result = prestate.executeQuery();

            while( result.next() )
            {
                amount = result.getInt( "sumPoint" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingToday.getSumPoint] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(amount);
    }
}
