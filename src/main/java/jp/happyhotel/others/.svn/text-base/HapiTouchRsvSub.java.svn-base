/*
 * @(#)HapiTouch.java
 * 1.00 2011/01/12 Copyright (C) ALMEX Inc. 2007
 * ハピタッチ制御クラス
 */
package jp.happyhotel.others;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataHhRsvPlan;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoom;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataRsvReserveBasic;
import jp.happyhotel.data.DataUserFelica;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.owner.LogicOwnerRsvCheckIn;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserPointPay;
import jp.happyhotel.user.UserPointPayTemp;

/**
 * ハピタッチ>
 * 
 * @author S.Tashiro
 * @version 1.00 2010/11/17
 */
public class HapiTouchRsvSub
{

    private static final int POINT_KIND_WARIBIKI = 23;     // 割引
    private static final int POINT_KIND_YOYAKU   = 24;     // 予約
    private static final int RSV_POINT           = 1000007;

    /**
     * キャンセルボタンクリック処理
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @throws
     */
    public FormReserveSheetPC execCancel(FormReserveSheetPC frm, int status) throws Exception
    {
        String errMsg = "";
        String mode = "";
        boolean ret = false;
        LogicOwnerRsvCheckIn logic;
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();

        try
        {
            // 新規登録時以外既存データのステータスのチェック
            if ( rsvCmm.checkStatus( frm.getSelHotelId(), frm.getRsvNo(), status ) == false )
            {
                errMsg = Message.getMessage( "warn.00019" );
                frm.setErrMsg( errMsg );
                return(frm);
            }

            // 予約キャンセルチェックがされているか
            if ( frm.getCancelCheck() == 0 )
            {
                // 登録失敗
                errMsg = Message.getMessage( "warn.00032" );

                // 画面内容再取得
                // 予約データ抽出
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( frm.getMode() );

                return(frm);
            }

            // ワークテーブルに登録されている通常オプション情報取得
            logic = new LogicOwnerRsvCheckIn();
            frm = logic.getOptionQuantity( frm );

            // データ更新（キャンセル処理）
            logic.setFrm( frm );

            Logging.info( "[FormReserveSheetPC execCancel]reserve_no=" + frm.getRsvNo() + ",calDate=" + frm.getOrgRsvDate() + ",frm.getAdultNum() " + frm.getAdultNum() );

            ret = logic.execRsvCancel( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_NEWRSV );

            if ( ret == false )
            {
                // 登録失敗
                errMsg = frm.getErrMsg().trim();
                mode = frm.getMode();

                // 画面内容再取得
                // 予約データ抽出
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( mode );
                return(frm);
            }

            // 登録データの取得
            logicSheet = new LogicReserveSheetPC();
            logicSheet.setFrm( frm );
            logicSheet.getData( 1 );

            // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
            frm.setMode( "" );
            frm.setStatus( ReserveCommon.RSV_STATUS_CANCEL );

        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.execCancel() ] " + e.getMessage() );
            throw new Exception( "[HapiTouchRsvSub.execCancel() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * 来店確認ボタンクリック処理
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @throws
     */
    public FormReserveSheetPC execRaiten(FormReserveSheetPC frm, int status) throws Exception
    {
        String errMsg = "";
        String mode = "";
        boolean ret = false;
        int addPoint = 0;
        int reflectDate = 0;
        int rsvDate = 0;
        int arrivalTime = 0;
        int addBonusMile = 0;
        LogicOwnerRsvCheckIn logicCheckIn = new LogicOwnerRsvCheckIn();
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();

        try
        {
            Logging.info( "[HapiTouchRsvSub.exceRaiten Start]" );
            Logging.info( "[HapiTouchRsvSub.exceRaiten ] getSelHotelId()=" + frm.getSelHotelId() + ",getRsvDate()=" + frm.getRsvDate() + ",getSeq()=" + frm.getSeq() + ",getRsvNo()" + frm.getRsvNo() );

            // 新規登録時以外既存データのステータスのチェック
            if ( rsvCmm.checkStatus( frm.getSelHotelId(), frm.getRsvNo(), status ) == false )
            {
                errMsg = Message.getMessage( "warn.00020" );
                frm.setErrMsg( errMsg );
                frm = getRaitenRegistData( frm );
                Logging.info( "[HapiTouchRsvSub.warn.00020 ] " + errMsg );
                return(frm);
            }

            // 部屋の未選択チェック
            if ( frm.getSeq() == 0 )
            {
                errMsg = Message.getMessage( "warn.00002", "チェックインする部屋番号" );
                frm.setErrMsg( errMsg );
                frm = getRaitenRegistData( frm );
                Logging.info( "[HapiTouchRsvSub.warn.00002 ] " + errMsg );
                return(frm);
            }

            // ポイントデータの設定
            addPoint = logicCheckIn.getRsvPointData( frm.getRsvNo(), 1 );
            rsvDate = logicCheckIn.getRsvPointData( frm.getRsvNo(), 2 );
            arrivalTime = logicCheckIn.getRsvPointData( frm.getRsvNo(), 3 );
            addBonusMile = logicCheckIn.getRsvPointData( frm.getRsvNo(), 4 );
            Logging.info( "[HapiTouchRsvSub.exceRaiten ] addPoint=" + addPoint + ",rsvDate=" + rsvDate + ",arrivalTime=" + arrivalTime + ",addBonusMile=" + addBonusMile );

            if ( frm.getExtFlag() == ReserveCommon.EXT_HAPIHOTE )
            {
                // 予約ポイントはハピホテ予約のときのみセットする
                frm.setAddPoint( addPoint );
            }
            frm.setAddBonusMile( addBonusMile );

            // 反映日の設定
            reflectDate = getReflectDate( rsvDate, arrivalTime );
            frm.setReflectDate( reflectDate );

            Logging.info( "[HapiTouchRsvSub.exceRaiten ] " + frm.getRsvNo() );

            // データ更新（来店確認）
            logicCheckIn.setFrm( frm );

            // 予約は新予約のみとする 2015.12.16
            ret = logicCheckIn.execRaiten( frm.getSelHotelId(), frm.getRsvNo(), frm.getRsvDate(), frm.getRoomNo(), ReserveCommon.SCHEMA_NEWRSV );

            if ( ret == false )
            {
                // 登録失敗
                errMsg = frm.getErrMsg().trim();
                mode = frm.getMode();
                Logging.info( "[HapiTouchRsvSub.execRaiten() ] " + errMsg );

                // 画面内容再取得
                // 予約データ抽出
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( mode );

                return(frm);
            }

            // 処理完了メッセージ
            switch( status )
            {
                case 1:
                    // 受付
                    frm.setProcMsg( Message.getMessage( "warn.00024" ) );
                    break;
                case 2:
                    // 利用済み
                    if ( frm.getMode().equals( ReserveCommon.MODE_RAITEN ) )
                    {
                        // 来店確認完了
                        frm.setProcMsg( Message.getMessage( "warn.00025" ) );
                    }
                    else
                    {
                        // 利用期限が過ぎている場合
                        frm.setProcMsg( Message.getMessage( "warn.00026" ) );
                    }
                    break;
            }
            // 登録データの取得
            frm = getRaitenRegistData( frm );
            Logging.info( "[HapiTouchRsvSub.exceRaiten End]" );

        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.execRaiten() ] " + e.getMessage() );
            throw new Exception( "[HapiTouchRsvSub.execRaiten() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * 有料ユーザポイント一時データの反映日取得
     * 
     * @param int rsvDate 予約日
     * @param int arrivalTime 到着予定時刻
     * @return int 反映日
     * @throws Exception
     */
    public int getReflectDate(int rsvDate, int arrivalTime) throws Exception
    {
        int retDate = 0;
        int limitFlg = 0;
        int range = 0;
        String year = "";
        String month = "";
        String day = "";
        String rsvYear = "";
        String rsvMonth = "";
        String rsvDay = "";
        String rsvHour = "";
        String rsvMinutes = "";
        String rsvSecond = "";
        String arrivalTimeStr = "";
        Calendar calendar = Calendar.getInstance();

        // ポイント管理マスタからデータ取得
        limitFlg = OwnerRsvCommon.getInitHapyPoint( 3 );
        range = OwnerRsvCommon.getInitHapyPoint( 4 );

        // 日付設定
        rsvYear = Integer.toString( rsvDate ).substring( 0, 4 );
        rsvMonth = Integer.toString( rsvDate ).substring( 4, 6 );
        rsvDay = Integer.toString( rsvDate ).substring( 6, 8 );

        arrivalTimeStr = ConvertTime.convTimeStr( arrivalTime, 0 );
        rsvHour = arrivalTimeStr.substring( 0, 2 );
        rsvMinutes = arrivalTimeStr.substring( 2, 4 );
        rsvSecond = arrivalTimeStr.substring( 4 );
        calendar.set( Integer.parseInt( rsvYear ), Integer.parseInt( rsvMonth ) - 1, Integer.parseInt( rsvDay ),
                Integer.parseInt( rsvHour ), Integer.parseInt( rsvMinutes ), Integer.parseInt( rsvSecond ) );

        switch( limitFlg )
        {
            case OwnerRsvCommon.LIMIT_FLG_TIME:
                // 時間加算
                calendar.add( Calendar.HOUR, range );
                break;

            case OwnerRsvCommon.LIMIT_FLG_DAY:
                // 日付加算
                calendar.add( Calendar.DATE, range );
                break;

            case OwnerRsvCommon.LIMIT_FLG_MONTH:
                // 月加算
                calendar.add( Calendar.MONTH, range );
                break;
        }

        year = Integer.toString( calendar.get( Calendar.YEAR ) );
        month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );
        day = String.format( "%1$02d", calendar.get( Calendar.DATE ) );

        retDate = Integer.parseInt( year + month + day );

        return(retDate);
    }

    /**
     * 来店確認時の登録データ取得処理
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @throws Exception
     */
    private FormReserveSheetPC getRaitenRegistData(FormReserveSheetPC frm) throws Exception
    {
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();

        // 登録データの取得
        logicSheet.setFrm( frm );
        logicSheet.getData( 2 );

        frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );
        frm.setStatus( ReserveCommon.RSV_STATUS_ZUMI );
        frm.setMode( ReserveCommon.MODE_RAITEN );

        return(frm);
    }

    /**
     * キャンセルボタンクリック処理
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @throws
     */
    public FormReserveSheetPC execUndoFix(FormReserveSheetPC frm, int status) throws Exception
    {
        String errMsg = "";
        String mode = "";
        boolean ret = false;
        LogicOwnerRsvCheckIn logic;
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();

        try
        {

            // ワークテーブルに登録されている通常オプション情報取得
            logic = new LogicOwnerRsvCheckIn();
            frm = logic.getOptionQuantity( frm );

            // データ更新（来店確認取り消し処理）
            logic.setFrm( frm );
            ret = logic.execRsvUndoFix( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_NEWRSV );

            if ( ret == false )
            {
                // 登録失敗
                errMsg = Integer.toString( HapiTouchErrorMessage.ERR_20904 );
                mode = frm.getMode();

                // 画面内容再取得
                // 予約データ抽出
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( mode );
                return(frm);
            }

            // 登録データの取得
            logicSheet = new LogicReserveSheetPC();
            logicSheet.setFrm( frm );
            logicSheet.getData( 1 );

            // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
            frm.setMode( "" );
            frm.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );

        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.execCancel() ] " + e.getMessage() );
            throw new Exception( "[HapiTouchRsvSub.execCancel() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * キャンセルボタンクリック処理
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @throws
     */
    public FormReserveSheetPC execUndoCancel(FormReserveSheetPC frm, int status) throws Exception
    {
        String errMsg = "";
        String mode = "";
        boolean ret = false;
        LogicOwnerRsvCheckIn logic;
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();

        try
        {

            // ワークテーブルに登録されている通常オプション情報取得
            logic = new LogicOwnerRsvCheckIn();
            frm = logic.getOptionQuantity( frm );

            // データ更新（キャンセル取り消し処理）
            logic.setFrm( frm );
            if ( frm.getAdultNum() == -1 ) // 新予約
            {
                ret = logic.execRsvUndoCancel( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_NEWRSV );
            }
            else
            {
                ret = logic.execRsvUndoCancel( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_OLDRSV );
            }
            if ( ret == false )
            {
                // 登録失敗
                errMsg = Integer.toString( HapiTouchErrorMessage.ERR_20704 );
                mode = frm.getMode();

                // 画面内容再取得
                // 予約データ抽出
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( mode );
                return(frm);
            }

            // 登録データの取得
            logicSheet = new LogicReserveSheetPC();
            logicSheet.setFrm( frm );
            logicSheet.getData( 1 );

            // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
            frm.setMode( "" );
            frm.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );

        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.execCancel() ] " + e.getMessage() );
            throw new Exception( "[HapiTouchRsvSub.execCancel() ] " + e.getMessage() );
        }
        return(frm);
    }

    /****
     * 予約データ取得(チェックインデータに紐付ける）
     * 
     * @param userId
     * @param hotelId
     * @param hc
     * @return
     */
    public HotelCi getReserveData(String userId, int hotelId, HotelCi hc)
    {
        String roomNo = "";
        DataRsvReserveBasic drrb = new DataRsvReserveBasic();
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();

        // フォームにセット
        frm.setSelHotelId( hotelId );
        frm.setUserId( userId );

        drrb.getData( hotelId );
        // 5時を越えていない場合は先日とする
        if ( 50000 > Integer.parseInt( DateEdit.getTime( 1 ) ) )
        {
            frm.setRsvDate( DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 ) );
        }
        else
        {
            frm.setRsvDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }

        try
        {
            // 予約データ抽出
            logic.setFrm( frm );
            // 予約データの取得
            logic.getRsvData();
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.getReserveData()] Exception:" + e.toString() );
        }

        // 予約データが取得が取得でき、ステータスが来店待ちの場合のみ
        if ( frm.getSeq() > 0 && frm.getStatus() == 1 )
        {
            DataHotelRoomMore dhrm = new DataHotelRoomMore();

            // 部屋番号が既に入っていたら枝番追加、入っていない場合は更新
            if ( hc.getHotelCi().getRoomNo().equals( "" ) == false )
            {
                hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
            }

            // 部屋番号を取得する
            if ( dhrm.getData( hotelId, frm.getSeq() ) != false )
            {
                roomNo = dhrm.getRoomNameHost();
            }
            else
            {
                DataHotelRoom dhr = new DataHotelRoom();
                if ( dhr.getData( hotelId, frm.getSeq() ) != false )
                {
                    roomNo = dhr.getRoomName();
                }
            }

            hc.getHotelCi().setRsvNo( frm.getRsvNo() );
            int extUserFlag = 0;
            UserBasicInfo ubi = new UserBasicInfo();
            if ( ubi.isLvjUser( userId ) )
            {
                extUserFlag = 1;
            }
            hc.getHotelCi().setExtUserFlag( extUserFlag );
            hc.getHotelCi().setUsePoint( frm.getUsedMile() );// 予約時に入力した使用マイルをセット 20150904

            // 部屋番号が既に入っていたら枝番追加、入っていない場合は更新
            if ( hc.getHotelCi().getRoomNo().equals( "" ) == false )
            {
                hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );

                hc.getHotelCi().insertData();
            }
            else
            {
                hc.getHotelCi().updateData( hotelId, hc.getHotelCi().getSeq(), hc.getHotelCi().getSubSeq() );
            }

            // 直近のCIデータを取得
            hc.getCheckInBeforeData( hotelId, userId );
            hc.getHotelCi().setRoomNo( roomNo );
        }
        return hc;
    }

    /****
     * 予約データ取得(チェックインデータに紐付ける）
     * 
     * @param userId
     * @param hotelId
     * @param ciSeq
     * @return
     */
    public FormReserveSheetPC getReserveData(String userId, int hotelId, int ciSeq)
    {
        Logging.info( "HapiTouchRsvSub getReserveData(String userId, int hotelId, int ciSeq)" + userId + " " + hotelId + " " + ciSeq );
        DataRsvReserveBasic drrb = new DataRsvReserveBasic();
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();

        // フォームにセット
        frm.setSelHotelId( hotelId );
        frm.setUserId( userId );

        drrb.getData( hotelId );
        // 5時を越えていない場合は1日前の日付にする
        if ( 50000 > Integer.parseInt( DateEdit.getTime( 1 ) ) )
        {
            frm.setRsvDate( DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 ) );
        }
        else
        {
            frm.setRsvDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }

        frm.setCiSeq( ciSeq );

        try
        {
            // 予約データ抽出
            logic.setFrm( frm );
            // 予約データの取得
            logic.getRsvData();

            // 必須オプションデータ取得
            logic.getRsvOptData( ReserveCommon.OPTION_IMP, 2 );

            // 通常オプションデータ取得
            logic.getRsvOptData( ReserveCommon.OPTION_USUAL, 2 );
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.getReserveData()] Exception:" + e.toString() );
        }
        return frm;
    }

    /****
     * 予約データ取得(チェックインデータに紐付ける）
     * 
     * @param userId
     * @param hotelId
     * @param hc
     * @return
     */
    public FormReserveSheetPC getReserveData(String userId, int hotelId, String rsvNo)
    {
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();

        // フォームにセット
        frm.setSelHotelId( hotelId );
        frm.setUserId( userId );

        try
        {
            // 予約データ抽出
            logic.setFrm( frm );
            // 予約データの取得
            logic.getRsvData( rsvNo );

            // 必須オプションデータ取得
            logic.getRsvOptData( ReserveCommon.OPTION_IMP, 2 );

            // 通常オプションデータ取得
            logic.getRsvOptData( ReserveCommon.OPTION_USUAL, 2 );
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.getReserveData()] Exception:" + e.toString() );
        }
        return frm;
    }

    /****
     * 来店キャンセル処理
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param kind 種類（0:VisitCancel、1:RsvUndoFix）
     * @param response レスポンス
     */
    public HotelCi visitCancel(int hotelId, HotelCi hc, int kind)
    {
        int errorCode = 0;
        boolean ret;
        boolean retUse;
        boolean retData;
        String hotenaviId = "";
        DataUserFelica duf;
        DataHotelBasic dhb;
        UserPointPayTemp uppt;
        UserPointPay upp;

        duf = new DataUserFelica();
        uppt = new UserPointPayTemp();
        upp = new UserPointPay();
        dhb = new DataHotelBasic();
        ret = false;
        retUse = false;
        retData = false;

        // レスポンスをセット
        try
        {
            if ( hc.getHotelCi().getCiStatus() == 0 )
            {
                duf.getData( hc.getHotelCi().getUserId() );

                // ホテナビIDを取得する
                dhb.getData( hotelId );
                if ( dhb.getId() > 0 )
                {
                    hotenaviId = dhb.getHotenaviId();
                }

                // ポイントが入っていたらマイル使用
                // if ( hc.getHotelCi().getUsePoint() > 0 && hc.getHotelCi().getRsvNo().equals( "" ) )
                if ( hc.getHotelCi().getUsePoint() > 0 )
                {
                    // 既に使用マイルデータがあるかどうかを確認する
                    retData = uppt.getUserPointHistory( hc.getHotelCi().getUserId(), hc.getHotelCi().getId(), POINT_KIND_WARIBIKI, hc.getHotelCi().getUserSeq(), hc.getHotelCi().getVisitSeq() );
                    if ( retData != false )
                    {
                        // データがあるため、使用マイルを更新
                        retUse = uppt.cancelUsePoint( hc.getHotelCi(), POINT_KIND_WARIBIKI, hc.getHotelCi().getUseEmployeeCode() );
                        if ( retUse != false )
                        {
                            // hh_user_point_payに使用マイルの追加
                            retUse = upp.cancelUsePoint( hc.getHotelCi(), POINT_KIND_WARIBIKI, hc.getHotelCi().getUseEmployeeCode() );
                            if ( retUse == false )
                            {
                                // RsvUndoFixエラー
                                if ( kind == 1 )
                                {
                                    errorCode = HapiTouchErrorMessage.ERR_20908;
                                }
                                // VisitCancelエラー
                                else
                                {
                                    errorCode = HapiTouchErrorMessage.ERR_10803;
                                }
                            }
                        }
                        else
                        {
                            // RsvUndoFixエラー
                            if ( kind == 1 )
                            {
                                errorCode = HapiTouchErrorMessage.ERR_20908;
                            }
                            // VisitCancelエラー
                            else
                            {
                                errorCode = HapiTouchErrorMessage.ERR_10803;
                            }
                        }
                    }
                    else
                    {
                        // 予約番号が入っていない場合
                        if ( hc.getHotelCi().getRsvNo().equals( "" ) != false )
                        {
                            // RsvUndoFixエラー
                            if ( kind == 1 )
                            {
                                errorCode = HapiTouchErrorMessage.ERR_20907;
                            }
                            // VisitCancelエラー
                            else
                            {
                                errorCode = HapiTouchErrorMessage.ERR_10804;
                            }
                        }
                    }
                    if ( errorCode > 0 )
                    {
                        retData = false;
                    }
                    else
                    {
                        retData = true;
                    }
                }
                else
                {
                    retData = true;
                }
            }
            else if ( hc.getHotelCi().getCiStatus() == 4 )// ハピホテ以外の来店
            {
                retData = true;
            }

            if ( retData != false )
            {
                hc.getHotelCi().setUserId( hc.getHotelCi().getUserId() );
                hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

                if ( retUse != false )
                {
                    // HotelCiを登録する
                    // ポイントの符号を反転させて登録
                    if ( hc.getHotelCi().getUsePoint() >= 0 )
                    {
                        hc.getHotelCi().setUsePoint( Math.abs( hc.getHotelCi().getUsePoint() ) * -1 );
                    }
                    else
                    {
                        hc.getHotelCi().setUsePoint( Math.abs( hc.getHotelCi().getUsePoint() ) );
                    }
                    hc.getHotelCi().setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    hc.getHotelCi().setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    hc.getHotelCi().setUseHotenaviId( hotenaviId );
                    hc.getHotelCi().setUseEmployeeCode( hc.getHotelCi().getUseEmployeeCode() );
                }
                if ( hc.getHotelCi().getCiStatus() == 4 )// 外部の予約データは無効データにする。
                {
                    hc.getHotelCi().setCiStatus( 3 );

                }
                else
                {
                    hc.getHotelCi().setCiStatus( 2 );
                }
                hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                ret = hc.getHotelCi().insertData();
                if ( ret == false )
                {
                    // チェックインデータ追加エラー
                    if ( kind == 1 )
                    {
                        errorCode = HapiTouchErrorMessage.ERR_20907;
                    }
                    else
                    {
                        errorCode = HapiTouchErrorMessage.ERR_10802;
                    }
                }
                else
                {
                    // 全マイル使用時はバックオフィスのデータは追加させない（全マイル使用時は料金確定時に書き込んでいるため）
                    if ( retUse != false && hc.getHotelCi().getAllUseFlag() == 0 )
                    {
                        // バックオフィスのデータを追加
                        // retData = bko.addBkoData( hc.getHotelCi().getUserId(), hotelId, POINT_KIND_WARIBIKI, hc.getHotelCi() );
                        // if ( retData == false )
                        // {
                        // errorCode = HapiTouchErrorMessage.ERR_10805;
                        // }
                    }
                }
            }
            else
            {

                if ( kind == 1 )
                {
                    errorCode = HapiTouchErrorMessage.ERR_20911;
                }
                else
                {
                    errorCode = HapiTouchErrorMessage.ERR_10806;
                }
            }
            hc.setErrorMsg( errorCode );
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub visitCancel]Exception:" + e.toString() );
        }
        return hc;
    }

    /**
     * 他の候補部屋取得
     * 
     * @param id　ホテルID
     * @param planId　プランID
     * @param date　対象日
     * @return
     */
    public String[] getAnotherRoom(int id, int planId, int planSubId, int date, String touchRoom, String reserveNo)
    {
        int i = 0;
        int count = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        String[] roomNoList = null;

        String query = "SELECT C.room_name_host FROM newRsvDB.hh_rsv_rel_plan_room A";
        query += " INNER JOIN newRsvDB.hh_rsv_room_remainder B";
        query += "    ON A.id = B.id AND A.seq = B.seq AND B.cal_date = ? AND (B.stay_reserve_no='' OR B.stay_reserve_no = ?)";
        query += " INNER JOIN hh_hotel_room_more C ";
        query += "    ON A.id = C.id AND A.seq = C.seq ";
        query += " WHERE A.id = ?";
        query += " AND A.plan_id = ?";
        query += " AND A.plan_sub_id = ?";
        if ( touchRoom.equals( "" ) == false ) // フロントでなかった場合
        {
            query += " AND C.room_name_host ='" + touchRoom + "'";
        }
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, date );
            prestate.setString( 2, reserveNo );
            prestate.setInt( 3, id );
            prestate.setInt( 4, planId );
            prestate.setInt( 5, planSubId );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    count = result.getRow();
                    result.beforeFirst();
                }

                roomNoList = new String[count];
                while( result.next() != false )
                {
                    roomNoList[i] = result.getString( "room_name_host" );
                    i++;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.getAnotherRoom] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return roomNoList;
    }

    public String[] getAnotherReserveRoom(int id, int planId, int planSubId, int date, String reserveRoom, String reserveNo)
    {
        int room_select_kind = 3;
        DataHhRsvPlan plan = new DataHhRsvPlan();
        if ( plan.getData( id, planId, planSubId ) != false )
        {
            room_select_kind = plan.getRoomSelectKind();
        }
        int i = 0;
        int count = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        String[] roomNoList = null;

        String query = "SELECT C.room_name_host FROM newRsvDB.hh_rsv_rel_plan_room A";
        query += " INNER JOIN newRsvDB.hh_rsv_room_remainder B";
        query += "    ON A.id = B.id AND A.seq = B.seq AND B.cal_date = ? AND (B.stay_reserve_no='' OR B.stay_reserve_no = ?)";
        query += " INNER JOIN hh_hotel_room_more C ";
        query += "    ON A.id = C.id AND A.seq = C.seq ";
        query += " WHERE A.id = ?";
        query += " AND A.plan_id = ?";
        query += " AND A.plan_sub_id = ?";
        query += " AND C.room_name_host <>'" + reserveRoom + "'";
        if ( room_select_kind == 1 )
        {
            query += "  AND C.room_rank = (SELECT room_rank FROM hh_hotel_room_more M WHERE M.id= ? AND M.room_name_host='" + reserveRoom + "')";
        }
        else if ( room_select_kind == 2 )
        {
            query += "  AND C.room_name_host ='" + reserveRoom + "'";
        }
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, date );
            prestate.setString( 2, reserveNo );
            prestate.setInt( 3, id );
            prestate.setInt( 4, planId );
            prestate.setInt( 5, planSubId );
            if ( room_select_kind == 1 )
            {
                prestate.setInt( 6, id );
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    count = result.getRow();
                    result.beforeFirst();
                }

                roomNoList = new String[count];
                while( result.next() != false )
                {
                    roomNoList[i] = result.getString( "room_name_host" );
                    i++;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.getAnotherReserveRoom] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return roomNoList;
    }

    public int setRsvPoint(HotelCi hc)
    {
        int errCode = 0;
        boolean ret = false;
        UserPointPayTemp uppt = new UserPointPayTemp();
        UserPointPay upp = new UserPointPay();
        HapiTouchBko bko = new HapiTouchBko();

        // 予約のポイント履歴はid=0で登録されるため
        ret = uppt.getUserPointHistoryByRsvNo( hc.getHotelCi().getUserId(), hc.getHotelCi().getId(), POINT_KIND_YOYAKU, hc.getHotelCi().getRsvNo() );
        if ( ret != false )
        {
            // 来店回数、ユーザ管理番号などをセットする
            uppt.getUserPoint()[0].setIdm( hc.getHotelCi().getIdm() );
            uppt.getUserPoint()[0].setUserSeq( hc.getHotelCi().getUserSeq() );
            uppt.getUserPoint()[0].setVisitSeq( hc.getHotelCi().getVisitSeq() );
            uppt.getUserPoint()[0].setThenPoint( upp.getNowPoint( hc.getHotelCi().getUserId(), false ) );
            uppt.getUserPoint()[0].setHotenaviId( hc.getHotelCi().getVisitHotenaviId() );
            // 部屋番号など
            uppt.getUserPoint()[0].setSlipNo( hc.getHotelCi().getSlipNo() );
            uppt.getUserPoint()[0].setAmount( hc.getHotelCi().getAmount() );
            uppt.getUserPoint()[0].setRoomNo( hc.getHotelCi().getRoomNo() );
            uppt.getUserPoint()[0].setAddFlag( 1 );
            // update
            ret = uppt.getUserPoint()[0].updateData( hc.getHotelCi().getUserId(), uppt.getUserPoint()[0].getSeq() );
            if ( ret != false )
            {
                // 予約マイルのセット
                upp.setVisitPoint( hc.getHotelCi().getUserId(), RSV_POINT, upp.getNowPoint( hc.getHotelCi().getUserId(), false ), 0, hc.getHotelCi() );

                ret = bko.addBkoData( hc.getHotelCi().getUserId(), hc.getHotelCi().getId(), POINT_KIND_YOYAKU, hc.getHotelCi() );
                if ( ret == false )
                {
                    // バックオフィス追加エラー
                    errCode = HapiTouchErrorMessage.ERR_20406;
                }
            }
            else
            {
                // ポイント履歴が更新できなかったためエラー
                errCode = HapiTouchErrorMessage.ERR_20405;
            }
        }
        else
        {
            // 来店確定時に追加されたデータがないためエラー
            errCode = HapiTouchErrorMessage.ERR_20404;
        }
        return errCode;
    }

    /****
     * 親予約番号取得
     * 
     * @param hotelId
     * @param rsvNo
     * @return
     */
    public String getMainReserveNo(int hotelId, String rsvNoMain)
    {
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String reserveNo = "";

        String query = "SELECT * FROM newRsvDB.hh_rsv_reserve  WHERE id = ? AND reserve_no_main = ?"
                + " AND SUBSTRING(reserve_no,INSTR(reserve_no,'-') + 1) = ? ORDER BY reserve_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, rsvNoMain );
            prestate.setString( 3, rsvNoMain );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    reserveNo = result.getString( "reserve_no" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiToucnRsvSub.getMainReserveNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return reserveNo;
    }

    public int[] getChargeList(int hotelId, String rsvNoMain)
    {
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int[] chargeList = null;
        int i = -1;

        String query = "SELECT charge_total FROM newRsvDB.hh_rsv_reserve  WHERE id = ? AND reserve_no_main = ? ORDER BY reserve_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, rsvNoMain );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 件数分の金額を初期化する。
                    chargeList = new int[result.getRow()];
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    i++;
                    chargeList[i] = result.getInt( "charge_total" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiToucnRsvSub.getMainReserveNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return chargeList;
    }

}
