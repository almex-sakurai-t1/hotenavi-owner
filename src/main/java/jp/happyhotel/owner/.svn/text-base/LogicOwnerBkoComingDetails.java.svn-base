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
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerBkoCommon;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.data.DataBkoAccountRecv;
import jp.happyhotel.data.DataBkoAccountRecvDetail;

/**
 * 来店明細ビジネスロジック
 */
public class LogicOwnerBkoComingDetails implements Serializable
{
    private static final long         serialVersionUID = -987298264984295770L;

    private FormOwnerBkoComingDetails frm;

    /* フォームオブジェクト */
    public FormOwnerBkoComingDetails getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoComingDetails frm)
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
        String usageDate = "";
        String usageTime = "";
        String slipUpdate = "";
        int sumHapy = 0;
        int sumBill = 0;
        int seikyWaribiki = 0;
        int hWaribiki = 0;
        int sWaribiki = 0;
        NumberFormat formatCur = NumberFormat.getNumberInstance();

        query = query + "SELECT ";
        query = query + " h.id, hotel_id, usage_date, usage_time, slip_update, person_name, user_management_no, ";
        query = query + " ht_slip_no, ht_room_no, usage_charge, receive_charge, h.closing_kind, d.account_title_cd, ";
        query = query + " d.account_title_name, d.point, d.amount, happy_balance, rel.bill_slip_no ";
        query = query + "FROM hh_bko_account_recv h ";
        query = query + "    INNER JOIN hh_bko_account_recv_detail d ON d.accrecv_slip_no = h.accrecv_slip_no ";
        query = query + "    LEFT JOIN hh_bko_rel_bill_account_recv rel ON h.accrecv_slip_no = rel.accrecv_slip_no   ";
        query = query + "    INNER JOIN hh_hotel_newhappie nh ON h.id = nh.id AND h.usage_date >= nh.bko_date_start ";
        query = query + "WHERE h.accrecv_slip_no = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getAccrecvSlipNo() );

            result = prestate.executeQuery();

            while( result.next() )
            {
                frm.setBillSlipNo( result.getInt( "bill_slip_no" ) );
                frm.setSelHotelID( result.getInt( "id" ) );
                usageDate = result.getString( "usage_date" );
                usageTime = ConvertTime.convTimeHHMM( result.getInt( "usage_time" ), 0 );
                frm.setUsageDate( usageDate.substring( 0, 4 ) + "/" + usageDate.substring( 4, 6 ) + "/" + usageDate.substring( 6 ) + " " + usageTime );
                if ( result.getInt( "slip_update" ) == 0 )
                {
                    frm.setSlipUpdate( "0" );
                }
                else
                {
                    slipUpdate = result.getString( "slip_update" );
                    frm.setSlipUpdate( slipUpdate.substring( 0, 4 ) + "/" + slipUpdate.substring( 4, 6 ) + "/" + slipUpdate.substring( 6 ) );
                }
                frm.setPersonName( ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( result.getString( "person_name" ) ) ) );
                frm.setUsrMngNo( result.getInt( "user_management_no" ) );
                frm.setHtSlipNo( result.getString( "ht_slip_no" ) );
                frm.setHtRoomNo( result.getString( "ht_room_no" ) );
                frm.setUsageCharge( result.getString( "usage_charge" ) );
                frm.setUsageChargeStr( formatCur.format( result.getInt( "usage_charge" ) ) );
                frm.setReceiveCharge( result.getString( "receive_charge" ) );
                frm.setReceiveChargeStr( formatCur.format( result.getInt( "receive_charge" ) ) );
                frm.sethZandaka( result.getInt( "happy_balance" ) );
                frm.setClosingKind( result.getInt( "closing_kind" ) );

                if ( OwnerBkoCommon.ACCOUNT_TITLE_CD_100 == result.getInt( "d.account_title_cd" ) )
                {
                    // 来店
                    frm.sethRaiten( result.getInt( "point" ) ); // マイル
                    frm.setsRaiten( result.getInt( "d.amount" ) ); // 収支
                }
                else if ( OwnerBkoCommon.ACCOUNT_TITLE_CD_110 == result.getInt( "d.account_title_cd" ) )
                {
                    // 付与
                    frm.sethSeisan( result.getInt( "point" ) ); // マイル
                    frm.setsSeisan( Integer.toString( (result.getInt( "d.amount" ) * -1) ) ); // 収支
                }
                else if ( OwnerBkoCommon.ACCOUNT_TITLE_CD_120 == result.getInt( "d.account_title_cd" ) )
                {
                    hWaribiki += result.getInt( "point" );
                    sWaribiki += result.getInt( "d.amount" );

                    // 使用
                    frm.sethWaribiki( hWaribiki * -1 ); // マイル
                    frm.sethWaribiki_View( Integer.toString( hWaribiki ) );
                    // frm.sethWaribiki_Inp( Integer.toString( result.getInt( "d.amount" ) * -1 ) );

                    frm.setsWaribiki( Integer.toString( sWaribiki * -1 ) ); // 収支
                    frm.setsWaribikiInt( sWaribiki * -1 );
                    seikyWaribiki = sWaribiki;
                }
                else if ( OwnerBkoCommon.ACCOUNT_TITLE_CD_130 == result.getInt( "d.account_title_cd" ) )
                {
                    // マイル 予約
                    frm.sethYoyaku( result.getInt( "point" ) );
                }

                else if ( OwnerBkoCommon.ACCOUNT_TITLE_CD_140 == result.getInt( "d.account_title_cd" ) || OwnerBkoCommon.ACCOUNT_TITLE_CD_141 == result.getInt( "d.account_title_cd" ) )
                {
                    // 収支 予約手数料
                    frm.setsYoyaku( Integer.toString( result.getInt( "d.amount" ) * -1 ) );
                }

                else if ( OwnerBkoCommon.ACCOUNT_TITLE_CD_150 == result.getInt( "d.account_title_cd" ) )
                {
                    // 予約ボーナスマイル
                    frm.sethBonusMile( result.getInt( "point" ) );
                    // 収支　予約ボーナスマイル
                    frm.setsBonusMile( Integer.toString( result.getInt( "d.amount" ) * -1 ) );
                }

                else if ( OwnerBkoCommon.ACCOUNT_TITLE_CD_200 <= result.getInt( "d.account_title_cd" ) )
                {
                    // その他 収支
                    frm.setSonota1( result.getString( "d.account_title_name" ) );
                    frm.setSonotaFlg( 1 ); // 商品コード200番台のため、他の商品コードと混在しないように更新不可とする
                    frm.setSonota1Charge( Integer.toString( result.getInt( "d.amount" ) * -1 ) );
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
                frm.setErrMsg( Message.getMessage( "erro.30001", "来店明細" ) );
                return;
            }

            // マイル　合計
            sumHapy = frm.gethRaiten() + frm.gethSeisan() + (frm.gethWaribiki() * -1) + frm.gethYoyaku() + frm.gethBonusMile();
            frm.sethSum( Integer.toString( sumHapy ) );

            // 収支　合計
            int yoyaku = 0;
            int seisan = 0;
            int bonusMile = 0;
            if ( frm.getsYoyaku().trim().length() == 0 )
            {
                yoyaku = 0;
            }
            else
            {
                yoyaku = Integer.parseInt( frm.getsYoyaku() );
            }
            if ( frm.getsBonusMile().trim().length() == 0 )
            {
                bonusMile = 0;
            }
            else
            {
                bonusMile = Integer.parseInt( frm.getsBonusMile() );
            }
            if ( frm.getsSeisan().trim().length() == 0 )
            {
                seisan = 0;
            }
            else
            {
                seisan = Integer.parseInt( frm.getsSeisan() );
            }
            sumBill = seisan + (seikyWaribiki * -1) + yoyaku + bonusMile;
            frm.setsSum( Integer.toString( sumBill ) );

            // ポイント倍率取得
            double bairitu = 0; // 付与倍率
            bairitu = getAmountRate( frm.getSelHotelID(), frm.getAccrecvSlipNo() );
            frm.setBairitu( bairitu );

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getAccountRecv] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * ポイント倍率取得
     * 
     * @param hotelId:ホテルID、accSlipNo:売掛伝票番号
     * @return ポイント倍率
     * @throws Exception
     */
    public double getAmountRate(int hotelid, int accSlipNo) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        double ret = 1;
        int idx = 0;

        query = query + "SELECT ci.amount_rate ";
        query = query + "FROM hh_bko_account_recv_detail dtl ";
        query = query + "  LEFT JOIN hh_user_point_pay_temp point ON dtl.user_id = point.user_id AND dtl.seq = point.seq ";
        query = query + "  LEFT JOIN hh_hotel_ci ci ON dtl.user_id = ci.user_id AND point.user_seq = ci.user_seq AND point.visit_seq = ci.visit_seq ";
        query = query + "WHERE dtl.accrecv_slip_no = ? ";
        query = query + "  AND dtl.account_title_cd = " + OwnerBkoCommon.ACCOUNT_TITLE_CD_110;
        query = query + " ORDER BY dtl.id, ci.seq, ci.sub_seq DESC ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( idx > 0 )
                {
                    break;
                }
                ret = result.getDouble( "amount_rate" );

                idx++;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getAmountRate] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 請求・売掛データ存在チェック
     * 結果をFormにセットする。
     * 
     * @param なし
     * @return なし
     */
    public void existsBillData() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;

        query = query + "SELECT COUNT(*) AS CNT FROM hh_bko_rel_bill_account_recv ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getAccrecvSlipNo() );

            result = prestate.executeQuery();

            while( result.next() )
            {
                count = result.getInt( "CNT" );
            }

            frm.setExistsSeikyu( false );
            if ( count > 0 )
            {
                frm.setExistsSeikyu( true );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.existsBillData] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 本締めデータの売掛データ作成
     * 
     * @return true:処理成功、false:処理失敗
     * @throws Exception
     */
    public boolean registAccountRecvHon() throws Exception
    {
        String query = "";
        PreparedStatement prestate = null;
        ResultSet result = null;
        Connection connection = null;
        boolean ret = false;
        int usageAmount = 0;
        int slipNo = 0;
        int akaSlipNo = 0;
        int newSlipNo = 0;

        try
        {
            // 現在の売掛伝票番号取得
            slipNo = frm.getAccrecvSlipNo();
            connection = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            // ▼赤伝レコードを作成
            ret = insertAccountRecv( prestate, connection, slipNo, 1, 0, 0, OwnerBkoCommon.CLOSING_KBN_KARI, OwnerBkoCommon.REGIST_FLG_HONT );

            // 赤伝用売掛伝票No.取得
            akaSlipNo = getAccrSlipNo( prestate, connection );
            if ( ret )
            {
                // 赤伝売掛明細
                ret = copyDetail( connection, slipNo, akaSlipNo );
            }

            // 請求金額差額（利用金額）
            usageAmount = Integer.parseInt( frm.getUsageCharge() );

            // ▼黒伝作成
            // 売掛データ作成
            if ( ret )
            {
                ret = insertAccountRecv( prestate, connection, slipNo, 2, usageAmount, Integer.parseInt( frm.getReceiveCharge() ), OwnerBkoCommon.CLOSING_KBN_KARI, OwnerBkoCommon.REGIST_FLG_HONT );
            }

            // 新しい売掛伝票No取得
            newSlipNo = getAccrSlipNo( prestate, connection );
            int slipDetailNo = 1;
            int accrecv_amount = 0;
            int amount = 0;
            int point = 0;
            DataBkoAccountRecvDetail dataAccDetail = null;

            // 元の売掛明細データ取得
            int hotelId = getAccDetailNo( prestate, connection, akaSlipNo, 1 );
            int seq = getAccDetailNo( prestate, connection, akaSlipNo, 2 );
            String rsvNo = getAccDetailRsvno( prestate, connection, akaSlipNo, 1 );
            String userId = getAccDetailRsvno( prestate, connection, akaSlipNo, 2 );

            // もとの売掛明細データを引き継ぐ
            if ( ret )
            {
                ret = copyDetail( connection, slipNo, newSlipNo );
            }

            // 現在登録されている明細番号の次の番号を取得
            slipDetailNo = getNextSlipDetailNo( prestate, connection, newSlipNo );

            // // 予約金額
            // if ( existsAccountRecv( prestate, connection, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_140 ) == true )
            // {
            // // 対象の売掛データが存在する場合、更新
            // ret = updAccountRecvDetail( connection, prestate, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, Integer.valueOf( frm.getReceiveCharge() ) );
            //
            // }
            // else
            // {
            // // 存在しない場合は追加
            // if ( Integer.valueOf( frm.getReceiveCharge() ) > 0 )
            // {
            //
            // dataAccDetail = OwnerBkoCommon.GetDetailYoyaku( Integer.valueOf( frm.getReceiveCharge() ) );
            // dataAccDetail.setAccrecvSlipNo( newSlipNo );
            // dataAccDetail.setSlipDetailNo( slipDetailNo );
            //
            // if ( ret )
            // {
            // dataAccDetail.setId( hotelId );
            // dataAccDetail.setReserveNo( rsvNo );
            // dataAccDetail.setUserId( userId );
            // dataAccDetail.setSeq( seq );
            // dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_KARI );
            //
            // ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
            // slipDetailNo = slipDetailNo + 1;
            // accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
            // }
            // }
            // }

            // ハピー発生・使用
            if ( existsAccountRecv( prestate, connection, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_120 ) == true )
            {
                // 対象の売掛データが存在する場合、更新
                ret = updAccountRecvDetail( connection, prestate, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_120, Integer.valueOf( frm.gethWaribiki() ) );

            }
            else
            {

                if ( Integer.valueOf( frm.gethWaribiki() ) > 0 )
                {

                    dataAccDetail = OwnerBkoCommon.GetDetailWaribiki( Integer.valueOf( frm.gethWaribiki() ) );
                    dataAccDetail.setAccrecvSlipNo( newSlipNo );
                    dataAccDetail.setSlipDetailNo( slipDetailNo );

                    if ( ret )
                    {
                        amount = dataAccDetail.getAmount() * -1;
                        point = dataAccDetail.getPoint() * -1;

                        dataAccDetail.setAmount( amount );
                        dataAccDetail.setPoint( point );
                        dataAccDetail.setId( hotelId );
                        dataAccDetail.setReserveNo( rsvNo );
                        dataAccDetail.setUserId( userId );
                        dataAccDetail.setSeq( seq );
                        dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_KARI );

                        ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                        slipDetailNo = slipDetailNo + 1;
                        accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
                    }
                }
            }

            // ハピー発生・付与
            if ( existsAccountRecv( prestate, connection, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_110 ) == true )
            {
                // 対象の売掛データが存在する場合、更新
                ret = updAccountRecvDetail( connection, prestate, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, Integer.valueOf( frm.gethSeisan() ) );

            }
            else
            {

                if ( Integer.valueOf( frm.gethWaribiki() ) > 0 )
                {

                    dataAccDetail = OwnerBkoCommon.GetDetailWaribiki( Integer.valueOf( frm.gethSeisan() ) );
                    dataAccDetail.setAccrecvSlipNo( newSlipNo );
                    dataAccDetail.setSlipDetailNo( slipDetailNo );

                    if ( ret )
                    {
                        amount = dataAccDetail.getAmount() * -1;
                        point = dataAccDetail.getPoint() * -1;

                        dataAccDetail.setAmount( amount );
                        dataAccDetail.setPoint( point );
                        dataAccDetail.setId( hotelId );
                        dataAccDetail.setReserveNo( rsvNo );
                        dataAccDetail.setUserId( userId );
                        dataAccDetail.setSeq( seq );
                        dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_KARI );

                        ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                        slipDetailNo = slipDetailNo + 1;
                        accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
                    }
                }
            }

            // 黒の売掛データの売掛金と売掛残を新しい伝票明細の金額で更新
            if ( ret )
            {
                ret = updAccountRecvUrikake( connection, prestate, newSlipNo );
            }

            // 元請求伝票の請求先コード取得
            int billCd = 0;
            billCd = getOrgBillCd( connection, slipNo );

            // 仮状態の該当請求先伝票番号取得
            int billSlipNo = 0;
            billSlipNo = getNewBillSlipNo( connection, billCd );

            // 請求・売掛データ作成
            if ( ret )
            {
                ret = insRelBillAccountRecv( connection, prestate, newSlipNo, billSlipNo );
            }

            // 請求明細データ更新（作成）
            if ( ret )
            {
                billSlipNo = getBillSlipNo( prestate, connection, newSlipNo );

                if ( billSlipNo > 0 )
                {
                    ret = OwnerBkoCommon.RegistBillDetail( prestate, connection, billSlipNo );
                }
            }

            // 請求データの金額更新
            if ( ret )
            {
                ret = updBillCharge( connection, prestate, billSlipNo );
            }

            // 履歴データ作成
            if ( ret )
            {
                // 請求明細削除(赤伝)
                OwnerRsvCommon.addAdjustmentHistory( frm.getSelHotelID(), frm.getHotenaviID(), frm.getUserId(),
                        OwnerRsvCommon.ADJUST_EDIT_ID_BILL_DEL, akaSlipNo, OwnerRsvCommon.ADJUST_MEMO_BILL_DEL );
                // 請求明細追加(黒伝)
                OwnerRsvCommon.addAdjustmentHistory( frm.getSelHotelID(), frm.getHotenaviID(), frm.getUserId(),
                        OwnerRsvCommon.ADJUST_EDIT_ID_BILL_ADD, newSlipNo, OwnerRsvCommon.ADJUST_MEMO_BILL_ADD );
            }

            if ( ret )
            {
                query = "COMMIT ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
            }
            else
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
            }

        }
        catch ( Exception e )
        {
            query = "ROLLBACK";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            Logging.error( "[LogicOwnerBkoComingDetails.registAccountRecvHon] Exception=" + e.toString() );
            ret = false;
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /**
     * 売掛明細データ存在チェック
     * 
     * @param prestate PreparedStatement
     * @param connection Connection
     * @param accSlipNo 売掛伝票番号
     * @param accTitleCd 科目コード
     * @return true;存在する、false:存在しない
     * @throws Exception
     */
    private boolean existsAccountRecv(PreparedStatement prestate, Connection connection, int accSlipNo, int accTitleCd) throws Exception
    {
        boolean ret = false;

        String query = "";
        ResultSet result = null;
        int cnt = 0;

        query = query + "SELECT COUNT(*) as CNT ";
        query = query + "FROM hh_bko_account_recv_detail ";
        query = query + "WHERE accrecv_slip_no = ? ";
        query = query + "  AND account_title_cd = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accSlipNo );
            prestate.setInt( 2, accTitleCd );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.existsAccountRecv] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * 売掛明細データ更新処理
     * 
     * @param conn Connection
     * @param prestate PreparedStatement
     * @param billSlipNo 売掛明細No.
     * @param accTitleCd 科目コード
     * @param amount 利用金額
     * @return true:更新成功、false:更新失敗
     */
    private boolean updAccountRecvDetail(Connection conn, PreparedStatement prestate, int billSlipNo, int accTitleCd, int amount)
    {

        String query = "";
        boolean ret = false;
        int detailAmount = 0;
        int point = 0;

        query = query + "UPDATE hh_bko_account_recv_detail SET ";
        query = query + "  amount = ?, ";
        query = query + "  point = ? ";
        query = query + "WHERE accrecv_slip_no = ? ";
        query = query + "  AND account_title_cd = ? ";

        if ( accTitleCd == OwnerBkoCommon.ACCOUNT_TITLE_CD_110 )
        {
            detailAmount = amount * 2;
            point = amount;

        }
        else if ( accTitleCd == OwnerBkoCommon.ACCOUNT_TITLE_CD_120 )
        {
            detailAmount = amount * -1;
            point = amount * -1;

        }
        else if ( accTitleCd == OwnerBkoCommon.ACCOUNT_TITLE_CD_140 )
        {
            detailAmount = (int)Math.floor( amount * OwnerBkoCommon.PERCENT_YOYAKU_AMOUNT / 100 );
            point = 0;
        }

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, detailAmount );
            prestate.setInt( 2, point );
            prestate.setInt( 3, billSlipNo );
            prestate.setInt( 4, accTitleCd );

            if ( prestate.executeUpdate() > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[LogicOwnerBkoComingDetails.updAccountRecvDetail] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * 売掛データ更新
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean registAccountRecv() throws Exception
    {
        String query = "";
        PreparedStatement prestate = null;
        ResultSet result = null;
        Connection connection = null;
        boolean ret = false;
        DataBkoAccountRecv data = new DataBkoAccountRecv();
        int usageAmount = 0;
        int slipNo = 0;
        int akaSlipNo = 0;
        int newSlipNo = 0;
        int registFlag = 0; // 0:オーナー 1:事務局

        try
        {
            slipNo = frm.getAccrecvSlipNo();

            if ( frm.getImediaFlg() == OwnerRsvCommon.IMEDIAFLG_IMEDIA )
            {
                registFlag = OwnerBkoCommon.REGIST_FLG_HONT;
            }

            connection = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            // 現在のレコードを無効に更新
            data.getData( slipNo );
            data.setInvalidFlag( 1 );
            data.setSlipUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            data.setOwnerUserId( frm.getUserId() );
            data.setOwnerHotelId( frm.getHotenaviID() );
            data.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            data.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            ret = data.updateData( connection );

            // 赤伝レコードを作成
            if ( ret )
            {
                ret = insertAccountRecv( prestate, connection, slipNo, 1, 0, 0, OwnerBkoCommon.CLOSING_KBN_KARI, registFlag );
            }
            // 赤伝用売掛伝票No.取得
            akaSlipNo = getAccrSlipNo( prestate, connection );

            if ( ret )
            {
                // 赤伝売掛明細
                ret = copyDetail( connection, slipNo, akaSlipNo );
            }

            // 請求金額差額（利用金額）
            usageAmount = Integer.parseInt( frm.getUsageCharge() );

            // 売掛データ作成
            if ( ret )
            {
                ret = insertAccountRecv( prestate, connection, slipNo, 2, usageAmount, Integer.parseInt( frm.getReceiveCharge() ), OwnerBkoCommon.CLOSING_KBN_KARI, registFlag );
            }

            // 新しい売掛伝票No取得
            newSlipNo = getAccrSlipNo( prestate, connection );
            int slipDetailNo = 0;
            int accrecv_amount = 0;
            int amount = 0;
            int point = 0;
            DataBkoAccountRecvDetail dataAccDetail = null;

            // 元の売掛明細データ取得
            int hotelId = getAccDetailNo( prestate, connection, akaSlipNo, 1 );
            int seq = getAccDetailNo( prestate, connection, akaSlipNo, 2 );
            String rsvNo = getAccDetailRsvno( prestate, connection, akaSlipNo, 1 );
            String userId = getAccDetailRsvno( prestate, connection, akaSlipNo, 2 );

            // もとの売掛明細データを引き継ぐ
            if ( ret )
            {
                ret = copyDetail( connection, slipNo, newSlipNo );
            }

            // 現在登録されている明細番号の次の番号を取得
            slipDetailNo = getNextSlipDetailNo( prestate, connection, newSlipNo );

            // // 予約金額
            // if ( existsAccountRecv( prestate, connection, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_140 ) == true )
            // {
            // // 対象の売掛データが存在する場合、更新
            // ret = updAccountRecvDetail( connection, prestate, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, Integer.valueOf( frm.getReceiveCharge() ) );
            //
            // }
            // else
            // {
            //
            // if ( Integer.valueOf( frm.getReceiveCharge() ) > 0 )
            // {
            //
            // dataAccDetail = OwnerBkoCommon.GetDetailYoyaku( Integer.valueOf( frm.getReceiveCharge() ) );
            // dataAccDetail.setAccrecvSlipNo( newSlipNo );
            // dataAccDetail.setSlipDetailNo( slipDetailNo );
            //
            // if ( ret )
            // {
            // dataAccDetail.setId( hotelId );
            // dataAccDetail.setReserveNo( rsvNo );
            // dataAccDetail.setUserId( userId );
            // dataAccDetail.setSeq( seq );
            // dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_KARI );
            //
            // ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
            // slipDetailNo = slipDetailNo + 1;
            // accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
            // }
            // }
            // }

            // ハピー発生・使用
            if ( existsAccountRecv( prestate, connection, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_120 ) == true )
            {
                // 対象の売掛データが存在する場合、更新
                ret = updAccountRecvDetail( connection, prestate, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_120, Integer.valueOf( frm.gethWaribiki() ) );

            }
            else
            {
                if ( Integer.valueOf( frm.gethWaribiki() ) > 0 )
                {

                    dataAccDetail = OwnerBkoCommon.GetDetailWaribiki( Integer.valueOf( frm.gethWaribiki() ) );
                    dataAccDetail.setAccrecvSlipNo( newSlipNo );
                    dataAccDetail.setSlipDetailNo( slipDetailNo );

                    if ( ret )
                    {
                        amount = dataAccDetail.getAmount() * -1;
                        point = dataAccDetail.getPoint() * -1;

                        dataAccDetail.setAmount( amount );
                        dataAccDetail.setPoint( point );
                        dataAccDetail.setId( hotelId );
                        dataAccDetail.setReserveNo( rsvNo );
                        dataAccDetail.setUserId( userId );
                        dataAccDetail.setSeq( seq );
                        dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_KARI );

                        ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                        slipDetailNo = slipDetailNo + 1;
                        accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
                    }
                }
            }

            // マイル・付与
            if ( existsAccountRecv( prestate, connection, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_110 ) == true )
            {
                // 対象の売掛データが存在する場合、更新
                ret = updAccountRecvDetail( connection, prestate, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, Integer.valueOf( frm.gethSeisan() ) );

            }
            else
            {
                if ( Integer.valueOf( frm.gethSeisan() ) > 0 )
                {
                    dataAccDetail = OwnerBkoCommon.GetDetailHuyo( Integer.valueOf( frm.gethSeisan() ) );
                    dataAccDetail.setAccrecvSlipNo( newSlipNo );
                    dataAccDetail.setSlipDetailNo( slipDetailNo );

                    if ( ret )
                    {
                        dataAccDetail.setAmount( dataAccDetail.getAmount() );
                        dataAccDetail.setPoint( dataAccDetail.getPoint() );
                        dataAccDetail.setId( hotelId );
                        dataAccDetail.setReserveNo( rsvNo );
                        dataAccDetail.setUserId( userId );
                        dataAccDetail.setSeq( seq );
                        dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_KARI );

                        ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                        slipDetailNo = slipDetailNo + 1;
                        accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
                    }
                }
            }

            // 黒の売掛データの売掛金と売掛残を新しい伝票明細の金額で更新
            if ( ret )
            {
                ret = updAccountRecvUrikake( connection, prestate, newSlipNo );
            }

            // 請求データ更新
            if ( ret )
            {
                ret = updateBill( connection, slipNo, newSlipNo );
            }

            // 請求明細データ更新（作成）
            int billSlipNo = 0;
            if ( ret )
            {
                billSlipNo = getBillSlipNo( prestate, connection, newSlipNo );

                if ( billSlipNo > 0 )
                {
                    ret = OwnerBkoCommon.RegistBillDetail( prestate, connection, billSlipNo );
                }
            }

            // 請求データの金額更新
            if ( ret )
            {
                ret = updBillCharge( connection, prestate, billSlipNo );
            }

            // 履歴データ作成
            if ( ret )
            {
                // 請求明細削除(赤伝)
                OwnerRsvCommon.addAdjustmentHistory( frm.getSelHotelID(), frm.getHotenaviID(), frm.getUserId(),
                        OwnerRsvCommon.ADJUST_EDIT_ID_BILL_DEL, akaSlipNo, OwnerRsvCommon.ADJUST_MEMO_BILL_DEL );
                // 請求明細追加(黒伝)
                OwnerRsvCommon.addAdjustmentHistory( frm.getSelHotelID(), frm.getHotenaviID(), frm.getUserId(),
                        OwnerRsvCommon.ADJUST_EDIT_ID_BILL_ADD, newSlipNo, OwnerRsvCommon.ADJUST_MEMO_BILL_ADD );
            }

            // 新しい売掛伝票番号をセット
            frm.setNewAccSLipNo( newSlipNo );
            if ( ret )
            {
                query = "COMMIT ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
            }
            else
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
            }

        }
        catch ( Exception e )
        {
            query = "ROLLBACK";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            Logging.error( "[LogicOwnerBkoComingDetails.RegistAccountRecv] Exception=" + e.toString() );
            ret = false;
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /**
     * 請求明細から一番小さい番号のデータ取得
     * 
     * @param prestate
     * @param connection
     * @param accSlipNo 売掛伝票番号
     * @param selKbn 1:ホテルID、2：管理番号
     * @return
     * @throws Exception
     */
    private int getAccDetailNo(PreparedStatement prestate, Connection connection, int accSlipNo, int selKbn) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int hotelId = 0;
        int seq = 0;
        int cnt = 0;
        int ret = 0;

        query = query + "SELECT id, seq ";
        query = query + "FROM hh_bko_account_recv_detail ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( cnt == 0 )
                {
                    hotelId = result.getInt( "id" );
                    seq = result.getInt( "seq" );
                }

                cnt = cnt + 1;
            }

            if ( selKbn == 1 )
            {
                ret = hotelId;
            }
            else
            {
                ret = seq;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getAccDetailNo] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * 請求明細から一番小さい番号の予約番号、またはユーザーID取得
     * 
     * @param prestate
     * @param connection
     * @param accSlipNo 売掛伝票番号
     * @param selKbn 1：予約番号、2：ユーザーID
     * @return
     * @throws Exception
     */
    private String getAccDetailRsvno(PreparedStatement prestate, Connection connection, int accSlipNo, int selKbn) throws Exception
    {
        String query = "";
        ResultSet result = null;
        String rsvNo = "";
        String ret = "";
        String userId = "";
        int cnt = 0;

        query = query + "SELECT reserve_no, user_id FROM hh_bko_account_recv_detail ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( cnt == 0 )
                {
                    rsvNo = result.getString( "reserve_no" );
                    userId = result.getString( "user_id" );
                }
                cnt = cnt + 1;
            }

            if ( selKbn == 1 )
            {
                ret = rsvNo;
            }
            else
            {
                ret = userId;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getAccDetailRsvno] Exception=" + e.toString() );
            throw new Exception( e );
        }
        return(ret);
    }

    /**
     * 売掛明細コピー
     * 
     * @param conn Connection
     * @param oldAccrecvSlipNo 変更前売掛伝票No
     * @param newAccrecvSlipNo 変更後売掛伝票No
     * @return なし
     */
    private boolean copyDetail(Connection connection, int oldAccrecvSlipNo, int newAccrecvSlipNo) throws Exception
    {
        String query = "";
        PreparedStatement prestate = null;

        query = query + "INSERT INTO hh_bko_account_recv_detail (";
        query = query + " accrecv_slip_no, ";
        query = query + " slip_detail_no, ";
        query = query + " slip_kind, ";
        query = query + " account_title_cd, ";
        query = query + " account_title_name, ";
        query = query + " amount, ";
        query = query + " point, ";
        query = query + " id, ";
        query = query + " reserve_no, ";
        query = query + " user_id, ";
        query = query + " seq, ";
        query = query + " closing_kind ) ";
        query = query + "SELECT ";
        query = query + " ?, ";
        query = query + " slip_detail_no, ";
        query = query + " slip_kind, ";
        query = query + " account_title_cd, ";
        query = query + " account_title_name, ";
        query = query + " amount, ";
        query = query + " point, ";
        query = query + " id, ";
        query = query + " reserve_no, ";
        query = query + " user_id, ";
        query = query + " seq, ";
        query = query + " ? ";
        query = query + "FROM hh_bko_account_recv_detail WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, newAccrecvSlipNo );
            prestate.setInt( 2, OwnerBkoCommon.CLOSING_KBN_KARI );
            prestate.setInt( 3, oldAccrecvSlipNo );

            if ( !(prestate.executeUpdate() > 0) )
            {
                return false;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.CopyDetail] Exception=" + e.toString() );
            return false;

        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return true;
    }

    /**
     * 売掛データ作成
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param slipNo int 元の売掛データ
     * @param insMode int 1;赤伝作成、2:通常の売掛データ
     * @param usageAmount int 利用金額
     * @param rsvAmount int 予約金額
     * @param closingKind int 締め処理フラグ
     * @param registFlag int 登録フラグ
     * @return true:正常、false:失敗
     */
    private boolean insertAccountRecv(PreparedStatement prestate, Connection conn, int slipNo, int insMode, int usageAmount, int rsvAmount, int closingKind, int registFlag) throws Exception
    {
        String query = "";
        int result;
        boolean ret = false;

        query = query + "INSERT INTO hh_bko_account_recv (";
        query = query + " hotel_id, ";
        query = query + " id, ";
        query = query + " add_up_date, ";
        query = query + " slip_update, ";
        query = query + " bill_cd, ";
        query = query + " bill_name, ";
        query = query + " div_name, ";
        query = query + " person_name, ";
        query = query + " user_management_no, ";
        query = query + " usage_date, ";
        query = query + " usage_time, ";
        query = query + " ht_slip_no, ";
        query = query + " ht_room_no, ";
        query = query + " happy_balance, ";
        query = query + " accrecv_amount, ";
        query = query + " reconcile_amount, ";
        query = query + " accrecv_balance, ";
        query = query + " remarks, ";
        query = query + " correction, ";
        query = query + " temp_slip_no, ";
        query = query + " first_accrecv_slip_no, ";
        query = query + " credit_note_flag, ";
        query = query + " invalid_flag, ";
        query = query + " regist_flag, ";
        query = query + " closing_kind, ";
        query = query + " owner_hotel_id, ";
        query = query + " owner_user_id, ";
        query = query + " last_update, ";
        query = query + " last_uptime, ";
        query = query + " usage_charge, ";
        query = query + " receive_charge ) ";
        query = query + "SELECT ";
        query = query + " hotel_id, ";
        query = query + " id, ";
        query = query + " add_up_date, ";
        query = query + " slip_update, ";
        query = query + " bill_cd, ";
        query = query + " bill_name, ";
        query = query + " div_name, ";
        query = query + " person_name, ";
        query = query + " user_management_no, ";
        query = query + " usage_date, ";
        query = query + " usage_time, ";
        query = query + " ht_slip_no, ";
        query = query + " ht_room_no, ";
        query = query + " happy_balance, ";
        query = query + " accrecv_amount, ";
        query = query + " reconcile_amount, ";
        query = query + " accrecv_balance, ";
        query = query + " remarks, ";
        query = query + " correction, ";
        query = query + " temp_slip_no, ";
        query = query + " first_accrecv_slip_no, ";
        query = query + " ?, "; // credit_note_flag
        query = query + " ?, "; // invalid_flag
        query = query + " ?, "; // regist_flag
        query = query + " ?, "; // closing_kind
        query = query + " owner_hotel_id, ";
        query = query + " owner_user_id, ";
        query = query + " last_update, ";
        query = query + " last_uptime, ";
        if ( insMode == 1 )
        {
            query = query + " usage_charge, ";
            query = query + " receive_charge ";
        }
        else
        {
            query = query + " ?, "; // usageAmount
            query = query + " ? "; // rsvAmount
        }
        query = query + "FROM hh_bko_account_recv ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            if ( insMode == 1 )
            {
                prestate.setInt( 1, 1 );
                prestate.setInt( 2, 1 );
                prestate.setInt( 3, registFlag );
                prestate.setInt( 4, closingKind );
                prestate.setInt( 5, slipNo );
            }
            else
            {
                prestate.setInt( 1, 0 );
                prestate.setInt( 2, 0 );
                prestate.setInt( 3, registFlag );
                prestate.setInt( 4, closingKind );
                prestate.setInt( 5, usageAmount );
                prestate.setInt( 6, rsvAmount );
                prestate.setInt( 7, slipNo );
            }

            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetail.insertAccountRecv] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * 本登録時の元伝票の請求先コード取得
     * 
     * @param Connectionオブジェクト
     * @param int oldAccrecvSlipNo 更新前売掛伝票No.
     * @return true:更新成功、false:更新失敗
     */
    private int getOrgBillCd(Connection connection, int oldAccrecvSlipNo) throws Exception
    {
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        int billCd = 0;

        query = query + "SELECT h.bill_cd ";
        query = query + "FROM hh_bko_rel_bill_account_recv d ";
        query = query + "  INNER JOIN hh_bko_bill h ON d.bill_slip_no = h.bill_slip_no ";
        query = query + "WHERE d.accrecv_slip_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, oldAccrecvSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                billCd = result.getInt( "bill_cd" );
            }

            return billCd;
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getOrgBillCd] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 仮登録されている請求伝票No.取得
     * 
     * @param connection Connectionオブジェクト
     * @param billCd 請求先コード
     * @return 請求伝票No.
     * @throws Exception
     */
    private int getNewBillSlipNo(Connection connection, int billCd) throws Exception
    {
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        int billSlipNo = 0;

        query = query + "SELECT bill_slip_no FROM hh_bko_bill ";
        query = query + "WHERE bill_cd = ? ";
        query = query + "  AND closing_kind = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, billCd );
            prestate.setInt( 2, OwnerBkoCommon.CLOSING_KBN_KARI );
            result = prestate.executeQuery();

            while( result.next() )
            {
                billSlipNo = result.getInt( "bill_slip_no" );
            }

            return billSlipNo;
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getNewBillSlipNo] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 請求データ更新
     * 
     * @param Connectionオブジェクト
     * @param int oldAccrecvSlipNo 更新前売掛伝票No.
     * @param int newAccrecvSlipNo 更新後売掛伝票No.
     * @return true:更新成功、false:更新失敗
     */
    private boolean updateBill(Connection connection, int oldAccrecvSlipNo, int newAccrecvSlipNo) throws Exception
    {
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> billSlipNoList = new ArrayList<Integer>();
        ArrayList<Integer> slipDtlNoList = new ArrayList<Integer>();
        ArrayList<Integer> chargeIncTaxList = new ArrayList<Integer>();
        boolean ret = false;

        query = query + "SELECT d.*, h.charge_inc_tax ";
        query = query + "FROM hh_bko_rel_bill_account_recv d ";
        query = query + "  INNER JOIN hh_bko_bill h ON d.bill_slip_no = h.bill_slip_no ";
        query = query + "WHERE d.accrecv_slip_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, oldAccrecvSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                billSlipNoList.add( result.getInt( "bill_slip_no" ) );
                slipDtlNoList.add( result.getInt( "slip_detail_no" ) );
                chargeIncTaxList.add( result.getInt( "charge_inc_tax" ) );
            }

            // 請求・売掛データ作成
            for( int i = 0 ; i < billSlipNoList.size() ; i++ )
            {
                ret = updRelBillAccountRecv( connection, prestate, newAccrecvSlipNo, oldAccrecvSlipNo, billSlipNoList.get( i ), slipDtlNoList.get( i ) );
                if ( ret == false )
                {
                    throw new Exception( "請求・売掛データ作成時エラー：updBillDetail" );
                }

            }

            return true;
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.updateBill] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 請求・売掛データ追加
     * 
     * @param conn Connection
     * @param prestate PreparedStatement
     * @param newSlipNo int 新しい売掛番号
     * @param billSlipNo int 請求番号
     * @return slipNo int 登録後の売掛伝票No.
     */
    private boolean insRelBillAccountRecv(Connection conn, PreparedStatement prestate, int newSlipNo, int billSlipNo)
    {

        String query = "";
        boolean ret = false;
        int nextDtlNo = 0;

        query = query + "Insert INTO hh_bko_rel_bill_account_recv VALUES(?, ?, ?, ?); ";

        try
        {
            // 次の明細番号取得
            nextDtlNo = getNextSlipDtlNo( billSlipNo );

            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, billSlipNo );
            prestate.setInt( 2, nextDtlNo );
            prestate.setInt( 3, newSlipNo );
            prestate.setInt( 4, OwnerBkoCommon.CLOSING_KBN_KARI );

            if ( prestate.executeUpdate() > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[LogicOwnerBkoComingDetails.insRelBillAccountRecv] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * 請求・売掛データの最大の明細番号 + 1 の値を取得
     * 
     * @param int billSlipNo 請求番号
     * @return 登録されている最大の明細番号 + 1
     */
    public int getNextSlipDtlNo(int billSlipNo) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int ret = 0;

        query = query + "SELECT max(slip_detail_no) as maxNo from hh_bko_rel_bill_account_recv ";
        query = query + "WHERE bill_slip_no = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, billSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                ret = result.getInt( "maxNo" ) + 1;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getNextSlipDtlNo] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 請求・売掛データ更新
     * 
     * @param conn Connection
     * @param prestate PreparedStatement
     * @param newSlipNo int 新しい売掛番号
     * @param oldSlipNo int 古い売掛番号
     * @param billSlipNo int 請求番号
     * @param slipDtlNo int 伝票明細番号
     * @return slipNo int 登録後の売掛伝票No.
     */
    private boolean updRelBillAccountRecv(Connection conn, PreparedStatement prestate, int newSlipNo, int oldSlipNo, int billSlipNo, int slipDtlNo)
    {

        String query = "";
        boolean ret = false;

        query = query + "UPDATE hh_bko_rel_bill_account_recv SET ";
        query = query + "  accrecv_slip_no = ? ";
        query = query + "WHERE bill_slip_no = ? ";
        query = query + "  AND slip_detail_no = ? ";
        query = query + "  AND accrecv_slip_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, newSlipNo );
            prestate.setInt( 2, billSlipNo );
            prestate.setInt( 3, slipDtlNo );
            prestate.setInt( 4, oldSlipNo );

            if ( prestate.executeUpdate() > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[LogicOwnerBkoComingDetails.updRelBillAccountRecv] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * 請求金額データ更新
     * 
     * @param conn Connection
     * @param prestate PreparedStatement
     * @param billSlipNo int 請求書番号
     * @param chargeIncTax int 請求金額の消費税
     * @param balance int 差額
     * @return slipNo int 登録後の売掛伝票No.
     */
    private boolean updBillCharge(Connection conn, PreparedStatement prestate, int billSlipNo) throws Exception
    {

        String query = "";
        boolean ret = false;
        int charge100 = 0;
        int charge200 = 0;
        int tax200 = 0;

        query = query + "UPDATE hh_bko_bill SET ";
        query = query + "  charge_inc_tax = ?,";
        query = query + "  charge_not_inc_tax = ?,";
        query = query + "  tax = ?, ";
        query = query + "  last_update = ?, ";
        query = query + "  last_uptime = ? ";
        query = query + "WHERE bill_slip_no = ? ";

        try
        {
            // 科目コード「100」番台の請求金額を取得(税込)
            charge100 = getSumCharge( prestate, conn, billSlipNo, 1 );

            // 科目コード「200」番台の請求金額を取得(税別)
            charge200 = getSumCharge( prestate, conn, billSlipNo, 2 );
            tax200 = (int)Math.floor( charge200 * OwnerBkoCommon.TAX );

            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, charge100 + charge200 + tax200 );// 請求金額（税込み）
            prestate.setInt( 2, charge100 + charge200 ); // 請求金額（税抜き）
            prestate.setInt( 3, tax200 );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 5, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 6, billSlipNo );

            if ( prestate.executeUpdate() > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.updBillCharge] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * 請求金額(税込)取得
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param billSlipNo 請求伝票No.
     * @param selKbn (1:科目コード「100」番台、2:科目コード「200」番台)
     * @return 税込請求金額
     * @throws Exception
     */
    private int getSumCharge(PreparedStatement prestate, Connection conn, int billSlipNo, int selKbn) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int chargeInTax = 0;

        try
        {
            query = query + "SELECT SUM(ad.amount) AS amount ";
            query = query + "FROM hh_bko_bill bh ";
            query = query + "  INNER JOIN hh_bko_rel_bill_account_recv br ON br.bill_slip_no = bh.bill_slip_no ";
            query = query + "    INNER JOIN hh_bko_account_recv_detail ad ON ad.accrecv_slip_no = br.accrecv_slip_no ";
            query = query + "WHERE bh.bill_slip_no = ? ";
            query = query + "AND NOT (ad.slip_kind = 0 AND ad.account_title_cd = 100) ";// ポイントの来店は請求しないので除く
            query = query + "AND NOT (ad.slip_kind = 0 AND ad.account_title_cd = 130) ";// ポイントの予約は請求しないので除く
            if ( selKbn == 1 )
            {
                query = query + "AND (ad.account_title_cd BETWEEN 100 AND 199) ";
            }
            else
            {
                query = query + "AND (ad.account_title_cd BETWEEN 200 AND 299) ";
            }

            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, billSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                chargeInTax = result.getInt( "amount" );
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[OwnerBkoCommon.getSumCharge] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(chargeInTax);
    }

    /**
     * 登録した売掛伝票No.取得
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @return slipNo int 登録後の売掛伝票No.
     */
    private int getAccrSlipNo(PreparedStatement prestate, Connection connection) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int slipNo = 0;

        query = query + "SELECT LAST_INSERT_ID() AS IDX ";

        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            while( result.next() )
            {
                slipNo = result.getInt( "IDX" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getAccrSlipNo] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(slipNo);
    }

    /**
     * 売掛データ追加（来店追加入力用）
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean addAccountRecv() throws Exception
    {
        String query = "";
        PreparedStatement prestate = null;
        ResultSet result = null;
        Connection connection = null;
        boolean ret = false;
        DataBkoAccountRecvDetail dataAccDetail = null;

        try
        {
            connection = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            int lastUpdate = Integer.parseInt( DateEdit.getDate( 2 ) );
            int lastUptime = Integer.parseInt( DateEdit.getTime( 1 ) );

            // 売掛データ
            DataBkoAccountRecv dataAcc = new DataBkoAccountRecv();
            dataAcc.setHotelId( frm.getHotenaviID() ); // ホテルID（ホテナビ）
            dataAcc.setId( frm.getSelHotelID() ); // ホテルID（ハピホテ）
            dataAcc.setAddUpDate( lastUpdate ); // 計上日
            dataAcc.setSlipUpdate( lastUpdate ); // 伝票更新日
            dataAcc.setBillCd( 0 ); // 請求先コード
            dataAcc.setBillName( "" ); // 請求先名
            dataAcc.setPersonName( "" );// 担当者名
            dataAcc.setUserManagementNo( 0 );// ユーザ管理番号
            dataAcc.setUsageDate( frm.getInpUsageDate() );// 利用日
            dataAcc.setHtSlipNo( 0 );// 伝票No（ハピタッチ）
            dataAcc.setHtRoomNo( frm.getInpHtRoomNo() );// 部屋番号（ハピタッチ）
            dataAcc.setUsageCharge( Integer.valueOf( frm.getInpUsageCharge() ) );// 利用金額
            dataAcc.setReceiveCharge( Integer.valueOf( frm.getInpRsvCharge() ) );// 予約金額
            // dataAcc.setAccrecvAmount( 0 ); // 売掛金額
            // dataAcc.setAccrecvBalance( 0 );// 売掛残
            dataAcc.setHappyBalance( frm.gethZandaka() );// ハピー残高
            dataAcc.setClosingKind( OwnerBkoCommon.CLOSING_KBN_HON );// 締め処理区分
            dataAcc.setOwnerHotelId( frm.getHotenaviID() );// オーナーホテルID
            dataAcc.setOwnerUserId( frm.getUserId() );// オーナーユーザID
            dataAcc.setLastUpdate( lastUpdate );
            dataAcc.setLastUptime( lastUptime );

            ret = OwnerBkoCommon.RegistRecv( prestate, connection, dataAcc );

            // 自動採番された売掛伝票No取得
            int accrecvSlipNo = OwnerBkoCommon.GetInsertedAccrecvSlipNo( prestate, connection );
            dataAcc.setAccrecvSlipNo( accrecvSlipNo );

            // 伝票明細No
            int slipDetailNo = 1;

            // 売掛金額
            int accrecv_amount = 0;

            // 予約金額
            if ( Integer.valueOf( frm.getInpRsvCharge() ) > 0 )
            {

                dataAccDetail = OwnerBkoCommon.GetDetailYoyaku( Integer.valueOf( frm.getInpRsvCharge() ) );
                dataAccDetail.setAccrecvSlipNo( accrecvSlipNo );
                dataAccDetail.setSlipDetailNo( slipDetailNo );

                if ( ret )
                {
                    ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                    slipDetailNo = slipDetailNo + 1;
                    accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
                }
            }

            // 割引ハピー
            if ( Integer.valueOf( frm.getInpWaribiki() ) > 0 )
            {

                dataAccDetail = OwnerBkoCommon.GetDetailWaribiki( Integer.valueOf( frm.getInpWaribiki() ) );
                dataAccDetail.setAccrecvSlipNo( accrecvSlipNo );
                dataAccDetail.setSlipDetailNo( slipDetailNo );

                if ( ret )
                {
                    ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                    slipDetailNo = slipDetailNo + 1;
                    accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
                }
            }

            // 売掛金額、売掛残更新
            if ( ret )
            {
                ret = OwnerBkoCommon.UpdateRecv( prestate, connection, accrecvSlipNo, accrecv_amount );
            }

            // 請求データの更新はキーがわからないので保留
            // 請求年月取得
            // int billDate = getBillDate(connection);

            // if (billDate == -1) {
            // frm.setErrMsg( Message.getMessage( "warn.30033" ) );
            // return false;
            // }

            // 請求データ更新
            // ret = getBill(connection ,frm.getSelHotelID() ,billDate,accrecvSlipNo ,accrecv_amount);

            if ( ret )
            {
                query = "COMMIT ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
            }
            else
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
            }

        }
        catch ( Exception e )
        {
            query = "ROLLBACK";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            Logging.error( "[LogicOwnerBkoComingDetails.addAccountRecv] Exception=" + e.toString() );
            ret = false;
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /**
     * 請求年月取得
     * 
     * @param なし
     * @return 仮締め中の年月
     */
    public int getClosingCntrlDate() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int ret = 0;

        query = query + "SELECT max(closing_date) as closing_date from hh_bko_closing_control ";
        query = query + "WHERE closing_kind = " + OwnerBkoCommon.CLOSING_KBN_KARI;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            while( result.next() )
            {
                ret = result.getInt( "closing_date" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getClosingCntrlDate] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 売掛伝票No.から請求伝票No.取得
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param accrecvSlipNo 売掛伝票No.
     * @return billSlipNo int 請求伝票No.
     */
    private int getBillSlipNo(PreparedStatement prestate, Connection connection, int accrecvSlipNo) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int slipNo = -1;

        query = query + "SELECT bill_slip_no FROM hh_bko_rel_bill_account_recv ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accrecvSlipNo );
            result = prestate.executeQuery();
            while( result.next() )
            {
                slipNo = result.getInt( "bill_slip_no" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getBillSlipNo] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(slipNo);
    }

    /**
     * 次の売掛明細伝票番号取得
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param accrecvSlipNo 売掛伝票No.
     * @return 明細番号 int 次の売掛明細番号
     */
    private int getNextSlipDetailNo(PreparedStatement prestate, Connection connection, int accrecvSlipNo) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int dtlNo = 0;

        query = query + "SELECT MAX(slip_detail_no) as dtlNo FROM hh_bko_account_recv_detail ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accrecvSlipNo );
            result = prestate.executeQuery();
            while( result.next() )
            {
                dtlNo = result.getInt( "dtlNo" );
            }

            dtlNo = dtlNo + 1;

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getNextSlipDetailNo] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(dtlNo);
    }

    /**
     * 締め処理テーブルに「仮」のデータが存在しているか
     * 
     * @param なし
     * @return true:仮データあり、false:仮データなし
     */
    public boolean existsClosingCtrlKari() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int cnt = 0;
        boolean ret = false;

        query = query + "SELECT COUNT(*) AS CNT FROM hh_bko_closing_control ";
        query = query + "WHERE CLOSING_KIND = " + OwnerBkoCommon.CLOSING_KBN_KARI;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.existsClosingCtrlKari] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 売掛データの売掛金額、売掛残更新
     * 
     * @param conn Connection
     * @param prestate PreparedStatement
     * @param accSlipNo 売掛伝票No
     * @return true:処理成功、false:処理失敗
     */
    private boolean updAccountRecvUrikake(Connection conn, PreparedStatement prestate, int accSlipNo)
    {
        String query = "";
        boolean ret = false;
        int detailAmount = 0;

        query = query + "UPDATE hh_bko_account_recv SET ";
        query = query + "  accrecv_amount = ?, ";
        query = query + "  accrecv_balance = ? ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {

            // 売掛金額取得
            detailAmount = getAccrecvAmount( prestate, conn, accSlipNo );

            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, detailAmount );
            prestate.setInt( 2, detailAmount );
            prestate.setInt( 3, accSlipNo );

            if ( prestate.executeUpdate() > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[LogicOwnerBkoComingDetails.updAccountRecvUrikake] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * 売掛明細の合計金額取得
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param accSlipNo 売掛伝票番号
     * @return 合計金額
     * @throws Exception
     */
    public int getAccrecvAmount(PreparedStatement prestate, Connection conn, int accSlipNo) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int amount = 0;

        query = query + "SELECT SUM(amount) AS sumAmount FROM hh_bko_account_recv_detail ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, accSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                amount = result.getInt( "sumAmount" );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getAccrecvAmount] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(amount);
    }
}
