package jp.happyhotel.reserve;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.LockReserve;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataRsvResult;
import jp.happyhotel.touch.RsvList;
import jp.happyhotel.user.UserPointPay;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 
 * 個人情報入力画面（PC版） business logic
 */

public class LogicReserveCancel implements Serializable
{
    private static final long   serialVersionUID           = 4066584694901283027L;
    /** マイル処理区分 - キャンセル */
    private static final int    MILE_KIND_CANCEL           = 1;
    /** マイル処理区分 - 利用 */
    private static final int    MILE_KIND_USE              = -1;
    /** プレミアム先行予約の日数 */
    private static String       premiumRsvAdvantageDaysStr = "";

    private static final int    REQUEST_MAIL_KIND_REMINDAR = 5;
    private static final int    PLAN_TYPE_STAY             = 1;
    private static final int    PLAN_TYPE_TODAY_STAY       = 3;
    private static final int    PLAN_TYPE_REST             = 2;
    private static final int    PLAN_TYPE_TODAY_REST       = 4;
    private static final int    ROOM_STATUS_VACANT         = 1;

    protected FormReserveCancel frm;

    public FormReserveCancel getFrm()
    {
        return frm;
    }

    public void setFrm(FormReserveCancel frm)
    {
        this.frm = frm;
    }

    String execMethod = "execRsvCancelSub";

    /**
     * 予約情報のキャンセル
     * 
     * @param customId
     * @param cancelKind 更新区分(1:キャンセル、2:ノーショー)
     * @throws Exception
     */
    public boolean execRsvCancel(String customId, String cancelKind) throws Exception
    {
        Connection connection = null;
        ArrayList<Integer> lockId = new ArrayList<Integer>();
        ArrayList<Integer> lockReserveDate = new ArrayList<Integer>();
        ArrayList<Integer> lockSeq = new ArrayList<Integer>();
        Logging.info( "execRsvCancel START", "LogicReserveCancel" );
        try
        {
            // 連泊予約の途中の宿泊日のキャンセル
            if ( !StringUtils.isBlank( frm.getReserveNoMain() ) && !frm.getReserveNo().substring( frm.getReserveNo().indexOf( "-" ) + 1 ).equals( frm.getReserveNoMain() ) )
            {
                Logging.warn( "[LogicReserveCancel.execRsvCancel]Other user locked.[id=" + frm.getId() + ", reserve_no=" + this.frm.getReserveNo() + "]" );
                frm.addErrorMessage( Message.getMessage( "erro.30002", "予約情報のキャンセル" ) );
                return false;
            }

            // プラン情報取得 プラン名、プランPR、人数リスト、駐車場リストの作成
            getPlanData();
            // ホテル情報取得
            getHotelBasic();

            // languageをメール送信依頼データ(hh_rsv_mail_request)から取得する
            if ( getLanguageFromMailReauest() == false )
            {
                return false;
            }

            // ロックの設定
            if ( LockReserve.Lock( frm.getId(), frm.getReserveDate(), frm.getSeq() ) == false )
            {
                // 他の人がロックしていた場合
                Logging.warn( "[LogicReserveCancel.execRsvCancel]Other user locked.[id=" + frm.getId() + ", reserve_no=" + this.frm.getReserveNo() + "]" );
                frm.addErrorMessage( Message.getMessage( "warn.00030" ) );
                return false;
            }
            lockId.add( frm.getId() );
            lockReserveDate.add( frm.getReserveDate() );
            lockSeq.add( frm.getSeq() );

            connection = DBConnection.getConnection( false );
            boolean result = true;
            String reserveNo = frm.getReserveNo();
            result = execRsvCancelSub( connection, customId, cancelKind );

            if ( result )
            {
                // 初日の値の保存
                String SvRsvNo = frm.getReserveNo();
                int SvRsvSubNo = frm.getReserveSubNo();
                int SvRsvDate = frm.getReserveDate();
                int SvSeq = frm.getSeq();

                // 新予約で、連泊予約時、連泊分の予約情報を更新
                if ( StringUtils.isNotBlank( frm.getReserveNoMain() ) )
                {
                    String que = "SELECT * FROM newRsvDB.hh_rsv_reserve  WHERE id = ? AND reserve_no_main = ?"
                            + " AND SUBSTRING(reserve_no,INSTR(reserve_no,'-') + 1) != reserve_no_main ORDER BY reserve_date";
                    ResultSet resultR = null;
                    PreparedStatement prestateR = null;
                    try
                    {
                        prestateR = connection.prepareStatement( que );
                        prestateR.setInt( 1, frm.getId() );
                        prestateR.setString( 2, frm.getReserveNoMain() );
                        resultR = prestateR.executeQuery();

                        if ( resultR != null )
                        {
                            while( resultR.next() != false )
                            {
                                // 入力値のセット
                                frm.setReserveNo( resultR.getString( "reserve_no" ) );
                                frm.setReserveSubNo( resultR.getInt( "reserve_sub_no" ) + 1 );
                                frm.setReserveDate( resultR.getInt( "reserve_date" ) );
                                frm.setSeq( resultR.getInt( "seq" ) );

                                // 変更予定の内容でロック
                                if ( LockReserve.Lock( frm.getId(), frm.getReserveDate(), frm.getSeq() ) == false )
                                {
                                    // 他の人がロックしていた場合
                                    Logging.warn( "[LogicReserveCancel.execRsvCancel]Other user locked.[id=" + frm.getId() + ", reserve_no=" + this.frm.getReserveNo() + "]" );
                                    frm.addErrorMessage( Message.getMessage( "warn.00030" ) );
                                    return false;
                                }
                                lockId.add( resultR.getInt( "id" ) );
                                lockReserveDate.add( resultR.getInt( "reserve_date" ) );
                                lockSeq.add( resultR.getInt( "seq" ) );

                                result = execRsvCancelSub( connection, customId, cancelKind );
                                if ( result == false )
                                {
                                    break;
                                }
                            }
                        }
                    }
                    catch ( Exception e )
                    {
                        Logging.error( "[LogicOwnerRsvCheckIn.execRsvCancel] Exception=" + e.toString() );
                        result = false;
                    }
                    finally
                    {
                        DBConnection.releaseResources( prestateR );
                        DBConnection.releaseResources( resultR );
                    }
                }

                // frmの内容を初日の宿泊日のデータに戻す
                frm.setReserveNo( SvRsvNo );
                frm.setReserveDate( SvRsvDate );
                frm.setReserveSubNo( SvRsvSubNo );
                frm.setSeq( SvSeq );
            }

            if ( result )
            {

                // メール送信データ更新
                if ( result )
                {
                    cancelSendMail( connection, cancelKind );
                }

                // no-showのときは、実績データを作成する。
                if ( cancelKind.equals( "2" ) )
                {
                    result = registRsvResult( connection, ReserveCommon.RESULT_KIND_NOSHOW, frm, frm.getSeq() );
                    execMethod = "createRsvResult";
                }
                // no-show でないとき使用マイルを入力していた場合は、マイルを戻す
                else
                {
                    if ( NumberUtils.toInt( frm.getUsedMile() ) > 0 )
                    {
                        // 保有マイルの取得
                        getHoldMile();

                        // ユーザ有料ポイントデータ
                        if ( result )
                        {
                            result = createUserPointPay( connection, reserveNo, frm.getSeq(), MILE_KIND_CANCEL );
                            execMethod = "createUserPointPay";
                        }

                        // 有料ユーザポイント一時データ
                        // if ( result )
                        // {
                        // result = createUserPointPayTemp( connection, reserveNo, frm.getSeq(), MILE_KIND_CANCEL );
                        // execMethod = "createUserPointPayTemp";
                        // }
                    }
                }
            }

            // 予約ユーザー基本データ更新
            if ( result )
            {
                result = updateRsvUserBasic( connection, cancelKind );
                execMethod = "updateRsvUserBasic";
            }

            if ( result )
            {
                // IPアドレスが登録されている場合は、ホスト連動物件。予約があったことをホストに伝える
                if ( !HotelIp.getFrontIp( frm.getId(), 1 ).equals( "" ) )
                {
                    int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                    int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                    // 5時以前の場合は前日とする
                    if ( 50000 > nowTime )
                    {
                        nowTime = nowTime + 240000;
                        nowDate = DateEdit.addDay( nowDate, -1 );
                    }
                    if ( frm.getReserveDate() == nowDate// 予約日付が当日の場合
                            || (frm.getReserveDate() == DateEdit.addDay( nowDate, 1 ) && frm.getEstTimeArrival() < nowTime) /* 予約日付が翌日で来店予定時刻が24時間以内の場合 */)
                    {
                        RsvList rl = new RsvList();
                        if ( rl.getData( connection, frm.getId(), frm.getReserveDate(), frm.getReserveDate(), 0, frm.getReserveNo(), 1 ) != false )
                        {
                            rl.sendToHost( frm.getId() );
                        }
                    }
                }
                connection.commit();
                return true;
            }

            Logging.info( "[LogicReserveCancel.execRsvCancel]" +
                    " failed " + execMethod + "." +
                    " : id = " + this.frm.getId() +
                    " : reserve_no = " + this.frm.getReserveNo() );

            connection.rollback();
            frm.addErrorMessage( Message.getMessage( "erro.30002", "予約情報のキャンセル" ) );
            return false;
        }
        catch ( Exception e )
        {
            connection.rollback();
            throw e;
        }
        finally
        {

            // ロックの解除
            for( int i = 0 ; i < lockId.size() ; i++ )
            {
                LockReserve.UnLock( lockId.get( i ), lockReserveDate.get( i ), lockSeq.get( i ) );
            }
            DBConnection.releaseResources( connection );

        }
    }

    /**
     * 
     * 予約データの更新（予約キャンセル）
     * 
     * @param hotelId // ホテルID
     * @param reserveNo // 予約番号
     * @param reserveDate // 予約日
     * @param seq // 部屋番号
     * @param kind // 機種
     * @param noShow // noShowﾌﾗｸﾞ
     * @return
     * @throws Exception
     */
    public boolean execRsvCancelSub(Connection connection, String customId, String cancelKind) throws Exception
    {

        boolean result = false;
        try
        {

            // 未送信のメールリマンダの停止
            stopSendMail( connection );

            // 予約情報ステータス更新
            result = updateReserveStatus( connection, ReserveCommon.RSV_STATUS_CANCEL, cancelKind, customId );
            execMethod = "updateReserveStatus";
            if ( !result )
                Logging.info( "[LogicOwnerReserveCancel.execRsvCancelSub]setReserveStatus=" + result );

            // 部屋残数データ更新
            if ( result )
            {
                result = updateRoomRemainder( connection, "", frm.getSeq() );
                if ( !result )
                    Logging.info( "[LogicOwnerReserveCancel.execRsvCancelSub]updateRoomRemainder=" + result + ",frm.getSeq()=" + frm.getSeq() );
                execMethod = "updateRoomRemainder";
            }
            // 予約履歴データ作成
            if ( result )
            {
                result = createReserveHistory( connection, frm.getReserveNo(), ReserveCommon.UPDKBN_CANCEL, customId );
                if ( !result )
                    Logging.info( "[LogicOwnerReserveCancel.execRsvCancelSub]createReserveHistory=" + result + ",frm.getReserveNo()=" + frm.getReserveNo() );
                execMethod = "createReserveHistory";
            }
            // 予約・オプション履歴データ作成
            if ( result )
            {
                result = createRsvOptionHistory( connection, frm.getReserveNo() );
                if ( !result )
                    Logging.info( "[LogicOwnerReserveCancel.execRsvCancelSub]createRsvOptionHistory=" + result + ",frm.getReserveNo()=" + frm.getReserveNo() );
                execMethod = "createRsvOptionHistory";
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveCancel.execRsvCancelSub] Exception=" + e.toString() );
            throw e;
        }
        return result;

    }

    /**
     * プラン情報取得
     * プラン名、プランPR、人数リスト、駐車場リストの作成
     */
    public void getPlanData() throws Exception
    {
        Connection connection = null;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT * FROM newRsvDB.hh_rsv_plan WHERE id = ? AND plan_id = ? AND plan_sub_id = ? ";
        connection = DBConnection.getConnection( false );
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getId() );
            prestate.setInt( 2, frm.getPlanId() );
            prestate.setInt( 3, frm.getPlanSubId() );
            result = prestate.executeQuery();
            if ( result.next() == false )
            {
                throw new Exception();
            }

            frm.setPlanName( result.getString( "plan_name" ) );
            frm.setPlanPr( result.getString( "plan_pr" ) );
            frm.setPrecaution( result.getString( "precaution" ) );
            frm.setMaxStayNum( result.getInt( "max_stay_num" ) );
            frm.setMinStayNum( result.getInt( "min_stay_num" ) );
            frm.setMaxStayNumMan( result.getInt( "max_stay_num_man" ) );
            frm.setMaxStayNumWoman( result.getInt( "max_stay_num_woman" ) );
            frm.setMinStayNumMan( result.getInt( "min_stay_num_man" ) );
            frm.setMinStayNumWoman( result.getInt( "min_stay_num_woman" ) );
            frm.setMaxStayNumChild( result.getInt( "max_stay_num_child" ) );
            frm.setRoomSelectKind( result.getInt( "room_select_kind" ) );
            frm.setPlanType( result.getInt( "plan_type" ) );

            frm.setAdultAddChargeKind( result.getInt( "adult_add_charge_kind" ) );
            frm.setAdultAddCharge( result.getInt( "adult_add_charge" ) );
            frm.setChildAddChargeKind( result.getInt( "child_add_charge_kind" ) );
            frm.setChildAddCharge( result.getInt( "child_add_charge" ) );

            frm.setReserveStartTime( result.getInt( "reserve_start_time" ) );
            frm.setReserveEndDay( result.getInt( "reserve_end_day" ) );
            frm.setReserveEndTime( result.getInt( "reserve_end_time" ) );
            // プレミアム先行予約の日数取得
            // String ctrlKey = Constants.SYSTEM_CONF_CTRL_KEY_PREMIUM_RSV_ADVANTAGE_DAYS;
            String ctrlKey = "プレミアム先行予約の日数設定";
            if ( getSystemConfData( ctrlKey ) == false )
            {
                throw new Exception( "システム設定値が読み込めません。[ctrl_key=" + ctrlKey + "]" );
            }
            int premiumRsvAdvantageDays = Integer.parseInt( premiumRsvAdvantageDaysStr );

            /*
             * 無料会員のチェックイン可能日数算出
             */
            {
                /*
                 * 　　　　　　　　　　　　 |←予約日
                 * <---------★　|　　　　　|
                 * 　　　　<-----☆---->　　|
                 * 　　　　　　　|　▲------|---->
                 * --------------|----------|------
                 * 　　　　　　　|<-------->|
                 * 　　　　　　　　　↑先行予約日数
                 */
                // 無料会員のチェックイン可能日（一時変数）
                int tmpReserveStartDay = result.getInt( "reserve_start_day" ) - premiumRsvAdvantageDays; // 上図の ">"

                // 予約開始日の開始日が期限より前の場合は実際より1マイナスする
                if ( Integer.parseInt( DateEdit.getTime( 1 ) ) < result.getInt( "reserve_start_time" ) )
                {
                    tmpReserveStartDay--;
                }

                // 無料会員の予約開始日数
                int reserveStartDayFree = 0;
                if ( tmpReserveStartDay >= premiumRsvAdvantageDays )
                {
                    // 無料会員はプレミアム先行予約日数後から予約可能
                    reserveStartDayFree = tmpReserveStartDay; // ★
                }
                else if ( tmpReserveStartDay > 0 )
                {
                    /*
                     * （特別な条件）
                     * 先行予約日数を引いた無料会員の予約可能日が
                     * プレミアム先行予約日数以下の場合は
                     * 無料会員はプレミアム先行予約日数前から予約可能
                     */
                    reserveStartDayFree = premiumRsvAdvantageDays; // ☆
                }
                else
                {
                    /*
                     * 先行予約日数を引いた無料会員の予約可能日が
                     * チェックイン日を超える場合は
                     * プレミアム会員も無料会員も差がなくなる
                     */
                    reserveStartDayFree = result.getInt( "reserve_start_day" ); // ▲
                }

                frm.setReserveStartDayFree( reserveStartDayFree );
                frm.setReserveStartDayPremium( result.getInt( "reserve_start_day" ) );
            }

            /*
             * ログインユーザーの予約開始日数
             */
            if ( frm.isPaymember() )
            {
                frm.setReserveStartDay( frm.getReserveStartDayPremium() );
            }
            else
            {
                frm.setReserveStartDay( frm.getReserveStartDayFree() );
            }

            /*
             * 予約開始 / 終了日
             */
            {
                // 現在日時
                int curDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                int curTime = Integer.parseInt( DateEdit.getTime( 1 ) );

                /*
                 * 予約開始日（無料 / プレミアム共通）
                 */
                {
                    int reserveEndDay = frm.getReserveEndDay();
                    if ( curTime > frm.getReserveEndTime() )
                    {
                        reserveEndDay++;
                    }
                    /*
                     * C/Iの2日前まで予約を受け付けている(reserveEndDay=2)ならば、
                     * 現在の2日後からの予約を受け付けることが可能
                     */
                    int reserveStartDate = DateEdit.addDay( curDate, reserveEndDay );
                    // 販売開始期間が超えている場合は開始日を変える
                    if ( reserveStartDate < result.getInt( "sales_start_date" ) )
                    {
                        reserveStartDate = result.getInt( "sales_start_date" );
                    }
                    frm.setReserveStartDate( reserveStartDate );
                }

                /*
                 * 予約終了日（無料会員）
                 */
                {
                    int reserveStartDayFree = frm.getReserveStartDayFree();
                    if ( curTime < frm.getReserveStartTime() )
                    {
                        reserveStartDayFree--;
                    }
                    /*
                     * C/Iの10日前から予約を受け付けている(reserveStartDay=10)ならば、
                     * 現在の10日後までの予約を受け付けることが可能
                     */
                    int reserveEndDateFree = DateEdit.addDay( curDate, reserveStartDayFree );
                    // 販売終了期間を超えている場合は終了日をセット
                    if ( reserveEndDateFree > result.getInt( "sales_end_date" ) )
                    {
                        reserveEndDateFree = result.getInt( "sales_end_date" );
                    }
                    frm.setReserveEndDateFree( reserveEndDateFree );
                }

                /*
                 * 予約終了日（プレミアム会員）
                 */
                {
                    int reserveStartDayPremium = frm.getReserveStartDayPremium();
                    if ( curTime < frm.getReserveStartTime() )
                    {
                        reserveStartDayPremium--;
                    }
                    /*
                     * C/Iの10日前から予約を受け付けている(reserveStartDay=10)ならば、
                     * 現在の10日後までの予約を受け付けることが可能
                     */
                    int reserveEndDatePremium = DateEdit.addDay( curDate, reserveStartDayPremium );
                    // 販売終了期間を超えている場合は終了日をセット
                    if ( reserveEndDatePremium > result.getInt( "sales_end_date" ) )
                    {
                        reserveEndDatePremium = result.getInt( "sales_end_date" );
                    }
                    frm.setReserveEndDatePremium( reserveEndDatePremium );
                }
            }

            // ログインユーザーに合わせた予約終了日
            if ( frm.isPaymember() )
            {
                frm.setReserveEndDate( frm.getReserveEndDatePremium() );
            }
            else
            {
                frm.setReserveEndDate( frm.getReserveEndDateFree() );
            }

            frm.setSalesStartDate( result.getInt( "sales_start_date" ) );
            frm.setSalesEndDate( result.getInt( "sales_end_date" ) );
            frm.setQuestion( result.getString( "question" ) );
            frm.setConsumerDemandsKind( result.getInt( "consumer_demands_kind" ) );

            frm.setPaymentKind( result.getInt( "payment_kind" ) );
            frm.setLocalPaymentKind( result.getInt( "local_payment_kind" ) );

            frm.setBonusMile( result.getInt( "bonus_mile" ) );
            frm.setComingFlag( result.getInt( "coming_flag" ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveCancel.getPlanData] Exception=" + e.toString() );
            throw new Exception();
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );

        }

    }

    /**
     * ホテル情報取得
     */
    public void getHotelBasic()
    {
        DataHotelBasic data = new DataHotelBasic();
        data.getData( frm.getId() );
        frm.setHotelName( data.getName() );
        frm.setHotelPrefId( data.getPrefId() );
        frm.setHotelPrefName( data.getPrefName() );
        frm.setHotelJisCode( data.getJisCode() );
        frm.setHotelAddress1( data.getAddress1() );
        frm.setHotelNameKana( data.getNameKana() );
        frm.setHotelParking( data.getParking() );
        frm.setHotenaviId( data.getHotenaviId() );
    }

    /****
     * システム設定値取得
     * 
     * @param ctrlKey キー
     * @return
     */
    public boolean getSystemConfData(String ctrlKey)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM newRsvDB.hh_rsv_system_conf WHERE ctrl_key = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, ctrlKey );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    premiumRsvAdvantageDaysStr = result.getString( "val" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveCancel.getSystemConfData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 同じ予約番号で未依頼のレコードを無効に変更する
     * 
     * @param connection
     * @return
     * @throws Exception
     */
    private void stopSendMail(Connection connection) throws Exception
    {
        String query = "";
        // hoternavi.hh_rsv_mail_request を更新する
        query += "UPDATE newRsvDB.hh_rsv_mail_request ";
        query += " SET request_flag = 2 ";
        query += " ,regist_date = ? ";
        query += " ,regist_time = ? ";
        query += "WHERE id = ? ";
        query += "  AND reserve_no = ? ";
        query += "  AND request_mail_kind = ? ";
        query += "  AND request_flag = 0 ";

        PreparedStatement prestate = null;
        try
        {
            prestate = connection.prepareStatement( query );

            int i = 1;
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( i++, frm.getId() );
            prestate.setString( i++, frm.getReserveNo() );
            prestate.setInt( i++, REQUEST_MAIL_KIND_REMINDAR );// newrsv Constants.REQUEST_MAIL_KIND_REMINDAR
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveCancel.stopSendMail] Exception=" + e.toString(), "LogicReserveCancel" );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 予約のステータス更新
     * 
     * @param connection
     * @param status
     * @return
     */
    private boolean updateReserveStatus(Connection connection, int status, String cancelKind, String customId)
    {
        String query = "";
        query += " UPDATE newRsvDB.hh_rsv_reserve ";
        query += " SET reserve_sub_no = ? ";
        query += " , status = ? ";
        if ( cancelKind.equals( "2" ) )
        {
            query = query + ",noshow_flag = ?";
        }
        // query += " , cancel_type = ? ";
        query += " , accept_date = ? ";
        query += " , accept_time = ? ";
        query += " , owner_hotel_id = ? ";
        query += " , owner_user_id = ? ";
        // query += " , last_update = ? ";
        // query += " , last_uptime = ? ";
        query += " , cancel_date = ? ";
        query += " , cancel_credit_status = ? ";
        query += " WHERE reserve_no = ? ";
        PreparedStatement prestate = null;
        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            int i = 1;
            prestate.setInt( i++, frm.getReserveSubNo() );
            prestate.setInt( i++, status );// 3
            if ( cancelKind.equals( "2" ) )
            {
                prestate.setInt( i++, 1 );
            }
            // prestate.setInt( i++, 0 );// newrsv Constants.CANCEL_TYPE_HOTEL
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setString( i++, "happyhotel" );
            prestate.setString( i++, customId );
            // prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, 2 );
            prestate.setString( i++, frm.getReserveNo() );
            return prestate.executeUpdate() > 0;
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveCancel.updateReserveStatus] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 保有マイルの取得
     */
    public void getHoldMile()
    {
        UserPointPay loginUpPay = new UserPointPay();
        frm.setHoldMile( loginUpPay.getNowPoint( frm.getUserId(), false ) );
    }

    /**
     * ユーザ有料ポイントの作成
     * 
     * @param connection
     * @param reserveNo
     * @param seq
     * @param mileKind
     * @return
     * @throws Exception
     */
    private boolean createUserPointPay(Connection connection, String reserveNo, int seq, int mileKind) throws Exception
    {

        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT hh_user_point_pay SET ";
        query += " user_id=?";
        query += ", seq=?";
        query += ", get_date=?";
        query += ", get_time=?";
        query += ", code=?";
        query += ", point=?";
        query += ", point_kind=?";
        query += ", ext_code=?";
        query += ", ext_string=?";
        query += ", person_code=?";
        query += ", append_reason=?";
        query += ", memo=?";
        query += ", idm=?";
        query += ", user_seq=?";
        query += ", visit_seq=?";
        query += ", slip_no=?";
        query += ", room_no=?";
        query += ", amount=?";
        query += ", then_point=?";
        query += ", hotenavi_id=?";
        query += ", employee_code=?";
        query += ", used_point=?";
        query += ", user_type=?";
        query += ", expired_point=?";
        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, frm.getUserId() );// user_id
            int seq_ = getMaxSeqyUserId( connection, frm.getUserId() );
            prestate.setInt( i++, seq_ + 1 );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( i++, 1000006 );

            int point = Math.abs( NumberUtils.toInt( frm.getUsedMile() ) );
            if ( mileKind == MILE_KIND_USE )
            {
                prestate.setInt( i++, point * -1 );
            }
            else if ( mileKind == MILE_KIND_CANCEL )
            {
                prestate.setInt( i++, point );
            }
            else
            {
                Logging.error( "[LogicReserveCancel.createUserPointPay] invalid mile kind.(" + mileKind + ") " );
                return false;
            }

            prestate.setInt( i++, 23 );// PointKind
            prestate.setInt( i++, frm.getId() );// ExtCode
            prestate.setString( i++, reserveNo );// ExtString
            prestate.setString( i++, "" );// ppersonCode
            prestate.setString( i++, "" );// appendReason
            prestate.setString( i++, "" );// memo
            prestate.setString( i++, "" );// idm
            prestate.setInt( i++, 0 );// userSeq ;
            prestate.setInt( i++, 0 );// visitSeq
            prestate.setInt( i++, 0 );// slipNo
            prestate.setString( i++, String.valueOf( seq ) );// roomNo
            prestate.setInt( i++, 0 );// amount
            prestate.setInt( i++, frm.getHoldMile() );
            prestate.setString( i++, frm.getHotenaviId() );
            prestate.setInt( i++, 0 );// this.employeeCode
            prestate.setInt( i++, 0 );// this.usedPoint
            prestate.setInt( i++, frm.isPaymember() ? 1 : 0 );// UserType
            prestate.setInt( i++, 0 );// ExpiredPoint
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveCancel.createUserPointPay insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);

    }

    /**
     * 最大の管理番号を取得する
     * 
     * @param userId
     * @return
     * @throws Exception
     */
    public int getMaxSeqyUserId(Connection connection, String userId) throws Exception
    {
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT IFNULL( max(seq), 0 ) max FROM hh_user_point_pay WHERE user_id = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();
            result.next();
            return result.getInt( "max" );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveCancel.getMaxSeqyUserId] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 部屋残数テーブルの予約番号を更新する
     * 
     * @param connection
     * @param reserveNo 空文字の場合はキャンセル扱い
     * @param seq
     * @return
     */
    private boolean updateRoomRemainder(Connection connection, String reserveNo, int seq)
    {
        // DataHhRsvRoomRemainder roomRemainder = new DataHhRsvRoomRemainder();
        RsvRoomRemainder roomRemainder = new RsvRoomRemainder();
        if ( StringUtils.isEmpty( reserveNo ) )
        {
            return roomRemainder.cancelReserveNo( connection, frm.getPlanType(), frm.getId(), frm.getReserveDate(), seq );

        }
        return roomRemainder.registReserveNo( connection, frm.getPlanType(), frm.getId(), frm.getReserveDate(), seq, frm.getReserveTempNo(), reserveNo );
    }

    /**
     * 予約履歴を作成する
     * 
     * @param connection
     * @param reserveNo
     * @param updateKind
     * @return
     */
    private boolean createReserveHistory(Connection connection, String reserveNo, int updateKind, String customId)
    {
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret;
        int resultInt = 0;
        ret = false;

        try
        {
            query = "SELECT  *  FROM newRsvDB.hh_rsv_reserve";
            query = query + " WHERE id = '" + frm.getId() + "'";
            query = query + "   AND reserve_no = '" + reserveNo + "'";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {

                    query = "INSERT newRsvDB.hh_rsv_reserve_history SET ";
                    query = query + " id              = " + result.getInt( "id" );
                    query = query + ",reserve_no      ='" + ReplaceString.SQLEscape( result.getString( "reserve_no" ) ) + "'";
                    query = query + ",reserve_sub_no  = " + result.getInt( "reserve_sub_no" );
                    query = query + ",update_kind     = 3";
                    query = query + ",plan_id         = " + result.getInt( "plan_id" );
                    query = query + ",plan_sub_id     = " + result.getInt( "plan_sub_id" );
                    query = query + ",user_id         ='" + ReplaceString.SQLEscape( result.getString( "user_id" ) ) + "'";
                    query = query + ",reserve_date    = " + result.getInt( "reserve_date" );
                    query = query + ",seq             = " + result.getInt( "seq" );
                    query = query + ",est_time_arrival= " + result.getInt( "est_time_arrival" );
                    query = query + ",num_child       = " + result.getInt( "num_child" );
                    query = query + ",name_last       ='" + ReplaceString.SQLEscape( result.getString( "name_last" ) ) + "'";
                    query = query + ",name_first      ='" + ReplaceString.SQLEscape( result.getString( "name_first" ) ) + "'";
                    query = query + ",name_last_kana  ='" + ReplaceString.SQLEscape( result.getString( "name_last_kana" ) ) + "'";
                    query = query + ",name_first_kana ='" + ReplaceString.SQLEscape( result.getString( "name_first_kana" ) ) + "'";
                    query = query + ",zip_code        ='" + ReplaceString.SQLEscape( result.getString( "zip_code" ) ) + "'";
                    query = query + ",pref_code       = " + result.getInt( "pref_code" );
                    query = query + ",jis_code        = " + result.getInt( "jis_code" );
                    query = query + ",address1        ='" + ReplaceString.SQLEscape( result.getString( "address1" ) ) + "'";
                    query = query + ",address2        ='" + ReplaceString.SQLEscape( result.getString( "address2" ) ) + "'";
                    query = query + ",address3        ='" + ReplaceString.SQLEscape( result.getString( "address3" ) ) + "'";
                    query = query + ",tel1            ='" + ReplaceString.SQLEscape( result.getString( "tel1" ) ) + "'";
                    query = query + ",tel2            ='" + ReplaceString.SQLEscape( result.getString( "tel2" ) ) + "'";
                    query = query + ",reminder_flag   = " + result.getInt( "reminder_flag" );
                    query = query + ",mail_addr       ='" + ReplaceString.SQLEscape( result.getString( "mail_addr" ) ) + "'";
                    query = query + ",demands         ='" + ReplaceString.SQLEscape( result.getString( "demands" ) ) + "'";
                    query = query + ",remarks         ='" + ReplaceString.SQLEscape( result.getString( "remarks" ) ) + "'";
                    query = query + ",accept_date     = " + result.getInt( "accept_date" );
                    query = query + ",accept_time     = " + result.getInt( "accept_time" );
                    query = query + ",status          = " + result.getInt( "status" );
                    query = query + ",basic_charge_total= " + result.getInt( "basic_charge_total" );
                    query = query + ",option_charge_total=" + result.getInt( "option_charge_total" );
                    query = query + ",charge_total    = " + result.getInt( "charge_total" );
                    query = query + ",add_point       = " + result.getInt( "add_point" );
                    query = query + ",coming_flag     = " + result.getInt( "coming_flag" );
                    query = query + ",hotel_name      ='" + ReplaceString.SQLEscape( result.getString( "hotel_name" ) ) + "'";
                    query = query + ",noshow_flag     = " + result.getInt( "noshow_flag" );
                    query = query + ",parking         = " + result.getInt( "parking" );
                    query = query + ",parking_count   = " + result.getInt( "parking_count" );
                    query = query + ",ci_time_from    = " + result.getInt( "ci_time_from" );
                    query = query + ",ci_time_to      = " + result.getInt( "ci_time_to" );
                    query = query + ",co_time         = " + result.getInt( "co_time" );
                    query = query + ",temp_coming_flag= " + result.getInt( "temp_coming_flag" );
                    query = query + ",num_man         = " + result.getInt( "num_man" );
                    query = query + ",num_woman       = " + result.getInt( "num_woman" );
                    query = query + ",co_kind         = " + result.getInt( "co_kind" );
                    query = query + ",cancel_type     = " + result.getInt( "cancel_type" );
                    query = query + ",mail_addr1      ='" + ReplaceString.SQLEscape( result.getString( "mail_addr1" ) ) + "'";
                    query = query + ",mail_addr2      ='" + ReplaceString.SQLEscape( result.getString( "mail_addr2" ) ) + "'";
                    query = query + ",owner_hotel_id  = 'happyhotel'";
                    query = query + ",owner_user_id   = '" + customId + "'";
                    query = query + ",payment         = " + result.getInt( "payment" );
                    query = query + ",payment_status  = " + result.getInt( "payment_status" );
                    query = query + ",consumer_demands='" + ReplaceString.SQLEscape( result.getString( "consumer_demands" ) ) + "'";
                    query = query + ",highroof_count  = " + result.getInt( "highroof_count" );
                    query = query + ",add_bonus_mile  = " + result.getInt( "add_bonus_mile" );
                    query = query + ",used_mile       = " + result.getInt( "used_mile" );
                    query = query + ",num_adult       = " + result.getInt( "num_adult" );
                    query = query + ",last_update     = " + Integer.parseInt( DateEdit.getDate( 2 ) );
                    query = query + ",last_uptime     = " + Integer.parseInt( DateEdit.getTime( 1 ) );
                    query = query + ",reserve_no_main  = '" + result.getString( "reserve_no_main" ) + "'";
                    query = query + ",reserve_date_to         = " + result.getInt( "reserve_date_to" );
                    query = query + ",ext_flag         = " + result.getInt( "ext_flag" );
                    query = query + ",cancel_charge         = " + result.getInt( "cancel_charge" );
                    query = query + ",basic_charge_total_all         = " + result.getInt( "basic_charge_total_all" );
                    query = query + ",option_charge_total_all         = " + result.getInt( "option_charge_total_all" );
                    query = query + ",charge_total_all         = " + result.getInt( "charge_total_all" );
                    query = query + ",cancel_date           = " + result.getInt( "cancel_date" );
                    query = query + ",cancel_credit_status  = " + result.getInt( "cancel_credit_status" );

                    try
                    {
                        prestate = connection.prepareStatement( query );
                        resultInt = prestate.executeUpdate();
                        if ( resultInt > 0 )
                        {
                            ret = true;
                        }
                    }
                    catch ( Exception e )
                    {
                        Logging.error( "reserve_cancel.jsp e=" + e.toString() + "," + query );
                        ret = false;
                    }
                    finally
                    {
                        DBConnection.releaseResources( prestate );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "foward Exception e=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);

    }

    /**
     * キャンセルメール送信依頼
     * 
     * @param connection
     * @param cancelKind(1:キャンセル 2:ノーショー)
     * @return
     * @throws Exception
     */
    private void cancelSendMail(Connection connection, String cancelKind) throws Exception
    {
        String query;
        PreparedStatement prestate = null;

        try
        {
            // メール送信依頼データ作成
            query = "INSERT newRsvDB.hh_rsv_mail_request SET ";
            query = query + " id              = ?";
            query = query + ",reserve_no      = ?";
            query = query + ",reserve_sub_no  = ?";
            if ( cancelKind.equals( "1" ) )
            {
                query = query + ",request_mail_kind = ?";
            }
            else if ( cancelKind.equals( "2" ) )
            {
                query = query + ",request_mail_kind = ?";
            }
            query = query + ",request_flag    = ?";
            query = query + ",regist_term_kind= ?";
            query = query + ",regist_date = ?";
            query = query + ",regist_time = ?";
            query = query + ",language = ?";

            prestate = connection.prepareStatement( query );

            int i = 1;
            prestate.setInt( i++, frm.getId() );
            prestate.setString( i++, ReplaceString.SQLEscape( frm.getReserveNo() ) );
            prestate.setInt( i++, frm.getReserveSubNo() );
            if ( cancelKind.equals( "1" ) )
            {
                prestate.setInt( i++, ReserveCommon.MAIL_REQ_CANCELRSV );
            }
            else if ( cancelKind.equals( "2" ) )
            {
                prestate.setInt( i++, ReserveCommon.MAIL_REQ_NOSHOW );
            }
            prestate.setInt( i++, 0 );
            prestate.setInt( i++, 1 );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setString( i++, frm.getLanguage() );
            prestate.executeUpdate();
            DBConnection.releaseResources( prestate );

            query = "INSERT newRsvDB.hh_rsv_mail_request SET ";
            query = query + " id              = ?";
            query = query + ",reserve_no      =?";
            query = query + ",reserve_sub_no  = ?";
            if ( cancelKind.equals( "1" ) )
            {
                query = query + ",request_mail_kind = ?";
            }
            else if ( cancelKind.equals( "2" ) )
            {
                query = query + ",request_mail_kind = ?";
            }
            query = query + ",request_flag    = ?";
            query = query + ",regist_term_kind= ?";
            query = query + ",regist_date = ?";
            query = query + ",regist_time = ?";
            query = query + ",language = ?";
            prestate = connection.prepareStatement( query );

            i = 1;
            prestate.setInt( i++, frm.getId() );
            prestate.setString( i++, ReplaceString.SQLEscape( frm.getReserveNo() ) );
            prestate.setInt( i++, frm.getReserveSubNo() );
            if ( cancelKind.equals( "1" ) )
            {
                prestate.setInt( i++, ReserveCommon.MAIL_REQ_HOTEL_CANCELRSV );
            }
            else if ( cancelKind.equals( "2" ) )
            {
                prestate.setInt( i++, ReserveCommon.MAIL_REQ_HOTEL_NOSHOW );
            }
            prestate.setInt( i++, 0 );
            prestate.setInt( i++, 1 );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setString( i++, frm.getLanguage() );
            prestate.executeUpdate();
            DBConnection.releaseResources( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveCancel.cancelSendMail] Exception=" + e.toString() );
            throw e;
        }

    }

    /**
     * 予約・オプション設定履歴データ作成
     * 
     * @param connection
     * @param reserveNo
     * @return
     * @throws Exception
     */
    private boolean createRsvOptionHistory(Connection connection, String reserveNo) throws Exception
    {

        String query = "";
        query += "INSERT INTO newRsvDB.hh_rsv_rel_reserve_option_history ( ";
        query += "  id ";
        query += " ,reserve_no ";
        query += " ,reserve_sub_no ";
        query += " ,option_id ";
        query += " ,option_sub_id ";
        query += " ,number ";
        query += " ,quantity ";
        query += " ,unit_price ";
        query += " ,charge_total ";
        query += " ,remarks ";
        query += ") ";
        query += "SELECT  ";
        query += "  id ";
        query += " ,reserve_no ";
        query += " ,? ";
        query += " ,option_id ";
        query += " ,option_sub_id ";
        query += " ,number ";
        query += " ,quantity ";
        query += " ,unit_price ";
        query += " ,charge_total ";
        query += " ,remarks ";
        query += "FROM newRsvDB.hh_rsv_rel_reserve_option ";
        query += "WHERE id = ? ";
        query += "  AND reserve_no = ? ";

        PreparedStatement prestate = null;
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getReserveSubNo() );
            prestate.setInt( 2, frm.getId() );
            prestate.setString( 3, reserveNo );
            prestate.executeUpdate();
            return true;
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveCancel.createRsvOptionHistory] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 予約ユーザー基本データ更新
     * isNoShow 変数の設定ロジック未実装！
     * 
     * @param connection
     * @param cancelKind 更新区分(1:キャンセル、2:ノーショー)
     * @see jp.happyhotel.reserve.LogicReserveCancel#updateRsvUserBasic
     * @return
     * @throws Exception
     */
    private boolean updateRsvUserBasic(Connection connection, String cancelKind) throws Exception
    {
        String query = "";
        int i = 1;
        PreparedStatement prestate = null;
        query = "UPDATE hh_rsv_user_basic SET";
        query = query + " last_update = ?";
        query = query + ",last_uptime = ?";

        query = query + ",cancel_count = cancel_count+1"; // キャンセル回数更新
        if ( cancelKind.equals( "2" ) )
        {
            query = query + ",noshow_count = noshow_count+1";// ノーショウ回数更新
            query = query + ",limitation_flag = 1";//
            query = query + ",limitation_start_date = ?";// キャンセル日
            query = query + ",limitation_end_date = ?";// 60日後
        }
        query = query + " WHERE user_id = ?";
        try
        {
            prestate = connection.prepareStatement( query );

            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            if ( cancelKind.equals( "2" ) )
            {
                int range = 60;
                prestate.setInt( i++, frm.getReserveDate() );
                prestate.setInt( i++, DateEdit.addDay( frm.getReserveDate(), range ) );// 60日後
            }
            prestate.setString( i++, frm.getUserId() );
            int retCnt = prestate.executeUpdate();

            return retCnt > 0;

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveCancel.updateRsvUserBasic] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 
     * 実績データの作成
     * 
     * @param connection Connection
     * @param kind ReserveCommon.RESULT_KIND_NOSHOW
     * @param FormReserveCancel
     * @param seq
     * @return true:処理成功、false:処理失敗
     * @throws Exception
     */
    public boolean registRsvResult(Connection connection, int kind, FormReserveCancel frm, int seq)
    {

        boolean ret = false;
        DataRsvResult drrs = new DataRsvResult();

        drrs.setId( frm.getId() );
        drrs.setRsvNo( frm.getReserveNo() );
        drrs.setSeq( seq );
        drrs.setCiDate( frm.getReserveDate() );
        drrs.setUseKind( kind );
        drrs.setTotalFlag( 0 );
        drrs.setTotalDate( 0 );
        drrs.setTotalTime( 0 );
        drrs.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        drrs.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

        ret = drrs.insertData( connection );

        return(ret);
    }

    /**
     * メール送信依頼データから言語情報を取得
     * 
     * @return
     * @throws Exception
     */
    public boolean getLanguageFromMailReauest()
    {

        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT * FROM newRsvDB.hh_rsv_mail_request WHERE id = ? AND reserve_no = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getId() );
            prestate.setString( 2, frm.getReserveNo() );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    frm.setLanguage( result.getString( "language" ) );
                }
            }
            return true;
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveCancel.getLanguageFromMailReauest] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

    }

    public static class RsvRoomRemainder implements Serializable
    {
        /**
         * 予約のキャンセル処理を行う
         * planTypeによって宿泊、休憩どちらのカラムに書き込むかの制御を行う
         * 
         * @param connection
         * @param planType
         * @param id
         * @param calDate
         * @param seq
         * @param reserveNo
         * @return
         */
        public boolean cancelReserveNo(Connection connection, int planType, int id, int calDate, int seq)
        {
            StringBuilder query = new StringBuilder();

            query.append( "UPDATE newRsvDB.hh_rsv_room_remainder SET " );
            query.append( " [type]_reserve_no = ? , [type]_status = ? " );
            query.append( " WHERE id = ? AND cal_date = ? AND seq = ? " );

            String replacement = "";
            switch( planType )
            {
                case PLAN_TYPE_STAY:// newrsv Constants.PLAN_TYPE_STAY
                case PLAN_TYPE_TODAY_STAY:// newrsv Constants.PLAN_TYPE_TODAY_STAY
                    replacement = "stay";
                    break;
                case PLAN_TYPE_REST:// newrsv Constants.PLAN_TYPE_REST
                case PLAN_TYPE_TODAY_REST:// newrsv Constants.PLAN_TYPE_TODAY_REST
                    replacement = "rest";
                    break;
            }
            String execQuery = StringUtils.replace( query.toString(), "[type]", replacement );

            PreparedStatement prestate = null;
            try
            {
                prestate = connection.prepareStatement( execQuery );
                // 更新対象の値をセットする
                int i = 1;
                prestate.setString( i++, "" );
                prestate.setInt( i++, ROOM_STATUS_VACANT );// newrsv Constants.ROOM_STATUS_VACANT
                prestate.setInt( i++, id );
                prestate.setInt( i++, calDate );
                prestate.setInt( i++, seq );

                int result = prestate.executeUpdate();
                if ( result == 0 )
                    Logging.error( "[RsvRoomRemainder.cancelReserveNo]  planType=" + planType + ",id=" + id + ",calDate=" + calDate + ",seq=" + seq );
                return result > 0;
            }
            catch ( Exception e )
            {
                Logging.error( "[RsvRoomRemainder.cancelReserveNo] Exception=" + e.toString() );
                return false;
            }
            finally
            {
                DBConnection.releaseResources( prestate );
            }
        }

        /**
         * 予約の登録処理を行う
         * planTypeによって宿泊、休憩どちらのカラムに書き込むかの制御を行う
         * ステータスが空きで予約が入っていないものが対象
         * 
         * @param connection
         * @param planType
         * @param id
         * @param calDate
         * @param seq
         * @param reserveTempNo
         * @param reserveNo
         * @return
         */
        public boolean registReserveNo(Connection connection, int planType, int id, int calDate, int seq, long reserveTempNo, String reserveNo)
        {
            StringBuilder query = new StringBuilder();

            query.append( "UPDATE newRsvDB.hh_rsv_room_remainder SET " );
            query.append( " [type]_reserve_no = ? " );
            query.append( " WHERE id = ? AND cal_date = ? AND seq = ? AND [type]_reserve_temp_no = ? " );
            query.append( " AND [type]_status = ? AND ([type]_reserve_no = '' OR [type]_reserve_no is null) " );

            String replacement = "";
            switch( planType )
            {
                case PLAN_TYPE_STAY:// newrsv Constants.PLAN_TYPE_STAY
                case PLAN_TYPE_TODAY_STAY:// newrsv Constants.PLAN_TYPE_TODAY_STAY
                    replacement = "stay";
                    break;
                case PLAN_TYPE_REST:// newrsv Constants.PLAN_TYPE_REST
                case PLAN_TYPE_TODAY_REST:// newrsv Constants.PLAN_TYPE_TODAY_REST
                    replacement = "rest";
                    break;
            }
            String execQuery = StringUtils.replace( query.toString(), "[type]", replacement );

            PreparedStatement prestate = null;
            try
            {
                prestate = connection.prepareStatement( execQuery );
                // 更新対象の値をセットする
                int i = 1;
                prestate.setString( i++, reserveNo );
                prestate.setInt( i++, id );
                prestate.setInt( i++, calDate );
                prestate.setInt( i++, seq );
                prestate.setLong( i++, reserveTempNo );
                prestate.setLong( i++, ROOM_STATUS_VACANT );// newrsv Constants.ROOM_STATUS_VACANT

                int result = prestate.executeUpdate();
                return result > 0;
            }
            catch ( Exception e )
            {
                Logging.error( "[RsvRoomRemainder.registReserveNo] Exception=" + e.toString() );
                return false;
            }
            finally
            {
                DBConnection.releaseResources( prestate );
            }
        }
    }

}
