/*
 * @(#)HapiTouch.java
 * 1.00 2011/01/12 Copyright (C) ALMEX Inc. 2007
 * ハピタッチ制御クラス
 */
package jp.happyhotel.others;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataHotelAuth;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelMaster;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataUserFelica;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.owner.FormOwnerRsvList;
import jp.happyhotel.owner.LogicOwnerRsvList;
import jp.happyhotel.owner.LogicOwnerRsvRoomChange;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;
import jp.happyhotel.touch.RsvList;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserPointPay;
import jp.happyhotel.user.UserPointPayTemp;

import org.apache.commons.lang.StringUtils;

/**
 * ハピタッチ>
 * 
 * @author S.Tashiro
 * @version 1.00 2010/11/17
 */
public class HapiTouchRsv
{
    // ポイント区分
    private static final int    POINT_KIND_YOYAKU     = 24;                       // 予約
    private static final int    AM12                  = 240000;                   // 0時00分
    private static final int    TOMORROW              = 1;
    private static final String RESULT_OK             = "OK";
    private static final String RESULT_NG             = "NG";
    private static final String RESULT_DENY           = "DENY";
    private static final String CONTENT_TYPE          = "text/xml; charset=UTF-8";
    private static final String ENCODE                = "UTF-8";
    private static final int    RSV_KIND              = 5;                        // 来店待ちと来店を扱う
    private static final int    CI_STATUS_NOT_DISPLAY = 3;                        // タッチPCへの非表示

    private int                 errCode               = 0;

    /***
     * エラーメッセージ出力処理
     * 
     * @param root ルートノードネーム
     * @param message エラーメッセージ
     * @param response レスポンス
     */
    public void errorData(String root, String message, HttpServletResponse response)
    {
        GenerateXmlHapiTouchHotelInfo gxTouch;
        ServletOutputStream stream = null;

        try
        {
            stream = response.getOutputStream();

            gxTouch = new GenerateXmlHapiTouchHotelInfo();

            // xml出力クラスにノードをセット

            gxTouch.setResult( RESULT_DENY );
            gxTouch.setMessage( message );

            // XMLの出力
            String xmlOut = gxTouch.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch errorData]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch errorData]Exception:" + e.toString() );
                }
            }
        }
    }

    /****
     * 予約の一覧を取得する
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void rsvData(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvList dsp0; // form
        LogicOwnerRsvList lgLPC; // logic
        ServletOutputStream stream = null;
        Boolean ret = false;
        GenerateXmlHapiTouchRsvData gxRsvData;
        GenerateXmlHapiTouchRsvDataSub gxRsvDataSub;
        DataRsvReserve drr;
        DataUserFelica duf;

        String paramStartDate;
        String paramEndDate;
        String paramRsvNo;
        String paramKind;
        int nRsvDate = 0;
        int nEstArrivalTime = 0;
        int felicaFlag = 0;
        paramKind = request.getParameter( "kind" );

        drr = new DataRsvReserve();
        dsp0 = new FormOwnerRsvList();
        duf = new DataUserFelica();
        lgLPC = new LogicOwnerRsvList();
        gxRsvData = new GenerateXmlHapiTouchRsvData();

        paramStartDate = request.getParameter( "startDate" );
        paramEndDate = request.getParameter( "endDate" );
        paramRsvNo = request.getParameter( "rsvNo" );

        // 開始日のチェック
        if ( (paramStartDate == null) || (paramStartDate.equals( "" ) != false) || (CheckString.numCheck( paramStartDate ) == false) )
        {
            paramStartDate = "0";
        }
        // 終了日のチェック
        if ( (paramEndDate == null) || (paramEndDate.equals( "" ) != false) || (CheckString.numCheck( paramEndDate ) == false) )
        {
            paramEndDate = "0";
        }
        //
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        //
        if ( (paramKind == null) || (paramKind.equals( "" ) != false) || (CheckString.numCheck( paramKind ) == false) )
        {
            paramKind = "0";
        }

        if ( Integer.parseInt( paramKind ) <= 0 )
        {
            paramKind = "5";
        }

        // レスポンスをセット
        try
        {
            stream = response.getOutputStream();

            // ホテルID取得
            dsp0.setSelHotelID( hotelId );
            dsp0.setDateFrom( DateEdit.getDate( 1 ) ); // システム日付
            dsp0.setDateTo( DateEdit.getDate( 1 ) ); // システム日付

            lgLPC.setDateFrom( paramStartDate );
            lgLPC.setDateTo( paramEndDate );
            lgLPC.setRsvNo( paramRsvNo );
            lgLPC.setHotelId( hotelId );
            ret = lgLPC.getData( dsp0, Integer.parseInt( paramKind ), 0, "ALL" );
            if ( ret != false )
            {
                int i = 0;
                for( i = 0 ; i < lgLPC.getIdList().size() ; i++ )
                {
                    gxRsvDataSub = new GenerateXmlHapiTouchRsvDataSub();
                    gxRsvDataSub.setHotelId( hotelId );
                    gxRsvDataSub.setRsvDate( lgLPC.getRsvDateList().get( i ) );
                    gxRsvDataSub.setRsvNo( lgLPC.getReserveNoList().get( i ) );
                    gxRsvDataSub.setPlanName( ReplaceString.replaceApiFull( lgLPC.getPlanNmList().get( i ) ) );
                    gxRsvDataSub.setArrivalTime( lgLPC.getEstTimeArrivalList().get( i ) );
                    gxRsvDataSub.setRoomNo( lgLPC.getSeqList().get( i ) );
                    gxRsvDataSub.setRoomName( this.getRoomName( hotelId, lgLPC.getSeqList().get( i ) ) );
                    gxRsvDataSub.setOption( lgLPC.getOptionList().get( i ) );
                    gxRsvDataSub.setUserId( lgLPC.getUserIdList().get( i ) );
                    gxRsvDataSub.setUserName( lgLPC.getUserNmList().get( i ) );

                    // 予約キャンセルだったら
                    if ( lgLPC.getStatusValList().get( i ) == 3 && lgLPC.getNoShowList().get( i ) == 1 )
                    {
                        gxRsvDataSub.setStatus( "\\" + lgLPC.getStatusList().get( i ) );
                    }
                    else
                    {
                        gxRsvDataSub.setStatus( lgLPC.getStatusList().get( i ) );

                    }
                    gxRsvDataSub.setDispStatus( lgLPC.getDspList().get( i ) );
                    gxRsvDataSub.setStatusValue( lgLPC.getStatusValList().get( i ) );
                    gxRsvDataSub.setCharegeFlag( lgLPC.getNoShowList().get( i ) );

                    // ユーザの紐付け状況を確認
                    if ( duf != null )
                    {
                        duf = null;
                        duf = new DataUserFelica();
                    }
                    if ( duf.getData( lgLPC.getUserIdList().get( i ) ) != false )
                    {
                        if ( duf.getIdm().equals( "" ) == false && duf.getDelFlag() == 0 )
                        {
                            felicaFlag = 1;
                        }
                        else
                        {
                            felicaFlag = 0;
                        }
                    }
                    else
                    {
                        felicaFlag = 0;
                    }

                    gxRsvDataSub.setFelicaFlag( felicaFlag );

                    nRsvDate = lgLPC.getRsvDateValList().get( i );
                    nEstArrivalTime = lgLPC.getEstTimeArrivalValList().get( i );

                    // 時刻が24時を過ぎたら翌日の日付時刻に変換する
                    if ( nEstArrivalTime >= AM12 )
                    {
                        nEstArrivalTime -= AM12;
                        nRsvDate = DateEdit.addDay( nRsvDate, TOMORROW );
                    }

                    gxRsvDataSub.setRsvDateValue( lgLPC.getRsvDateValList().get( i ) );
                    gxRsvDataSub.setArrivalDateValue( nRsvDate );
                    gxRsvDataSub.setArrivalTimeValue( nEstArrivalTime );

                    // ユーザ名及び電話番号を取得する
                    ret = false;
                    // 前のデータが残っていたら初期化
                    if ( drr != null )
                    {
                        drr = null;
                        drr = new DataRsvReserve();
                    }

                    // 新予約 newRsvDB 追加項目（2015.08.21）
                    gxRsvDataSub.setPaidFlag( lgLPC.getPaymentList().get( i ) );
                    gxRsvDataSub.setTotalPrice( lgLPC.getChargeTotalList().get( i ) );

                    ret = drr.getData( hotelId, lgLPC.getReserveNoList().get( i ) );
                    if ( ret != false )
                    {
                        gxRsvDataSub.setTel( drr.getTel1() );
                    }
                    gxRsvData.addData( gxRsvDataSub );
                }
            }
            else
            {
                gxRsvData.setErrorCode( 0 );

            }

            // XMLの出力
            String xmlOut = gxRsvData.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            response.setContentLength( xmlOut.getBytes().length );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvData]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvData]Exception:" + e.toString() );
                }
            }

        }
    }

    /****
     * 予約の一覧を取得する
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void rsvList(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret = true;
        boolean retUpd = true;
        RsvList rl = new RsvList();
        final int sendKind = 1; // データ取得要求
        int rsvCount = 0;
        int errorCount = 0;
        String paramLicenceKey = "";
        String paramVersion = "";

        paramLicenceKey = request.getParameter( "key" );
        paramVersion = request.getParameter( "version" );

        if ( paramVersion == null || !CheckString.numCheck( paramVersion ) )
        {
            paramVersion = "0";
        }
        DataHotelAuth dha;
        dha = new DataHotelAuth();

        retUpd = dha.getData( paramLicenceKey );
        if ( retUpd != false )
        {
            if ( dha.getRsvAddDate() == 0 )
            {
                dha.setRsvAddDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dha.setRsvAddTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            }
            dha.setRsvLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dha.setRsvLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dha.setRsvVersion( Integer.parseInt( paramVersion ) );

            dha.updateData( paramLicenceKey );
        }

        try
        {
            if ( rl.getData( hotelId, request, response ) != false )
            {
                ret = rl.sendToHost( hotelId, sendKind, "" );
                rsvCount = rl.getRsvListData().length;
                errorCount = rsvCount - rl.getSendCount();
                // rsvCount = rl.getSendCount();
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        if ( ret )
        {
            GenerateXmlRsvList gxRsvList;
            ServletOutputStream stream = null;

            gxRsvList = new GenerateXmlRsvList();

            // レスポンスをセット
            try
            {
                // hh_hotel_master.pms_flagに1をセット
                DataHotelMaster hm = new DataHotelMaster();
                if ( hm.getData( hotelId ) )
                {
                    if ( hm.getPmsFlag() != 1 )
                    {
                        hm.setPmsFlag( 1 );
                        hm.updateData( hotelId );
                    }
                }

                stream = response.getOutputStream();
                // if ( ret != false )
                if ( errorCount == 0 )
                {
                    gxRsvList.setResult( RESULT_OK );
                    gxRsvList.setIdentifyNo( rl.getIdentifyNo() );
                    gxRsvList.setRsvCount( rsvCount );
                    gxRsvList.setErrorCount( errorCount );
                }
                else
                {
                    gxRsvList.setResult( RESULT_NG );
                    gxRsvList.setIdentifyNo( rl.getIdentifyNo() );
                    gxRsvList.setRsvCount( rsvCount );
                    gxRsvList.setErrorCount( errorCount );
                    gxRsvList.setErrorCode( 0 );
                }

                // XMLの出力
                String xmlOut = gxRsvList.createXml();
                ServletOutputStream out = null;

                Logging.info( xmlOut );
                out = response.getOutputStream();
                response.setContentType( CONTENT_TYPE );
                out.write( xmlOut.getBytes( ENCODE ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionHapiTouch getRsvList]Exception:" + e.toString() );
            }
            finally
            {
                if ( stream != null )
                {
                    try
                    {
                        stream.close();
                    }
                    catch ( IOException e )
                    {
                        Logging.error( "[ActionHapiTouch getRsvList]Exception:" + e.toString() );
                    }
                }
            }
        }
        else
        // 送信がなかった
        {
            GenerateXmlRsvList gxRsvList;
            ServletOutputStream stream = null;

            gxRsvList = new GenerateXmlRsvList();

            // レスポンスをセット
            try
            {
                stream = response.getOutputStream();
                gxRsvList.setResult( RESULT_NG );
                if ( request.getParameter( "IdentifyNo" ) == null )
                {
                    gxRsvList.setIdentifyNo( 0 );
                }
                else
                {
                    gxRsvList.setIdentifyNo( Integer.parseInt( request.getParameter( "IdentifyNo" ) ) );
                }
                gxRsvList.setRsvCount( 0 );
                gxRsvList.setErrorCount( rsvCount );

                // XMLの出力
                String xmlOut = gxRsvList.createXml();
                ServletOutputStream out = null;

                Logging.info( xmlOut );
                out = response.getOutputStream();
                response.setContentType( CONTENT_TYPE );
                out.write( xmlOut.getBytes( ENCODE ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionHapiTouch getRsvList]Exception:" + e.toString() );
            }
            finally
            {
                if ( stream != null )
                {
                    try
                    {
                        stream.close();
                    }
                    catch ( IOException e )
                    {
                        Logging.error( "[ActionHapiTouch getRsvList]Exception:" + e.toString() );
                    }
                }
            }
        }
    }

    /****
     * 予約詳細情報を取得する
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void rsvDataDetail(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        GenerateXmlHapiTouchRsvDataDetail gxRsvDetail;
        GenerateXmlHapiTouchRsvDataDetailOptionPrice gxRsvDetailOptionPrice;
        GenerateXmlHapiTouchRsvDataDetailImperativeOption gxRsvDetailInperativeOption;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        String paramRsvNo;
        int nRsvDate = 0;
        int nEstArrivalTime = 0;

        gxRsvDetail = new GenerateXmlHapiTouchRsvDataDetail();

        paramRsvNo = request.getParameter( "rsvNo" );
        // 終了日のチェック
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        // レスポンスをセット
        try
        {
            stream = response.getOutputStream();

            if ( paramRsvNo.compareTo( "" ) != 0 )
            {
                // フォームにセット
                frm.setSelHotelId( hotelId );
                frm.setRsvNo( paramRsvNo );

                // 予約データ抽出
                logic.setFrm( frm );
                // 実績データ取得
                logic.getData( 2 );

                frm.setMode( "" ); // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
                frm.setErrMsg( "" );
                frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );

                if ( frm.getSeq() > 0 )
                {

                    // 予約登録したユーザーのメールアドレス取得
                    UserBasicInfo ubi = new UserBasicInfo();
                    ubi.getUserBasic( frm.getUserId() );
                    frm.setLoginUserId( frm.getUserId() );
                    if ( ubi.getUserInfo().getMailAddr().equals( "" ) == false )
                    {
                        frm.setLoginUserMail( ubi.getUserInfo().getMailAddr() );
                        frm.setMail( ubi.getUserInfo().getMailAddr() );
                    }
                    else if ( ubi.getUserInfo().getMailAddrMobile().equals( "" ) == false )
                    {
                        frm.setLoginUserMail( ubi.getUserInfo().getMailAddrMobile() );
                        frm.setMail( ubi.getUserInfo().getMailAddrMobile() );
                    }
                    frm.setLoginUserTel( ubi.getUserInfo().getTel1() );

                    // 予約データをセット
                    gxRsvDetail.setStatusValue( frm.getStatus() );

                    if ( frm.getStatus() == 1 )
                    {
                        gxRsvDetail.setStatus( "来店待ち" );
                    }
                    else if ( frm.getStatus() == 2 )
                    {
                        gxRsvDetail.setStatus( "来店" );
                    }
                    else if ( frm.getStatus() == 3 )
                    {
                        if ( frm.getNoShow() == 1 )
                        {
                            gxRsvDetail.setStatus( "\\キャンセル" );
                        }
                        else
                        {
                            gxRsvDetail.setStatus( "キャンセル" );
                        }
                    }
                    gxRsvDetail.setCancelKind( frm.getCancelKind() );
                    gxRsvDetail.setRsvNo( frm.getRsvNo() );
                    gxRsvDetail.setHotelId( frm.getSelHotelId() );
                    gxRsvDetail.setHotelName( frm.getHotelNm() );
                    gxRsvDetail.setHotelAddr( frm.getHotelAddr() );
                    gxRsvDetail.setHotelTel( frm.getHotelTel() );
                    gxRsvDetail.setPlanName( ReplaceString.replaceApiFull( frm.getPlanNm() ) );
                    gxRsvDetail.setPlanPr( frm.getPlanPr() );
                    gxRsvDetail.setRsvDate( frm.getRsvDateView() );
                    gxRsvDetail.setRsvDateValue( frm.getRsvDate() );
                    gxRsvDetail.setRoomNo( frm.getSeq() );
                    gxRsvDetail.setRoomName( this.getRoomName( hotelId, frm.getSeq() ) );
                    Logging.info( frm.getRsvNo() + "-" + frm.getRsvSubNo(), "HapiTouchRsv.rsvDataDetail setRankName" );
                    gxRsvDetail.setRankName( this.getRankName( hotelId, frm.getRsvNo() ) );
                    gxRsvDetail.setArrivalTime( frm.getEstTimeArrivalView() );

                    nRsvDate = frm.getRsvDate();
                    nEstArrivalTime = frm.getEstTimeArrival();

                    // 時刻が24時を過ぎたら翌日の日付時刻に変換する
                    if ( nEstArrivalTime >= AM12 )
                    {
                        nEstArrivalTime -= AM12;
                        nRsvDate = DateEdit.addDay( nRsvDate, TOMORROW );
                    }
                    // 予約日付、到着時刻をセット

                    gxRsvDetail.setArrivalDateValue( nRsvDate );
                    gxRsvDetail.setArrivalTimeValue( nEstArrivalTime );

                    // 料金
                    gxRsvDetail.setRoomPrice( frm.getBasicTotalView() );
                    gxRsvDetail.setAdultNum( frm.getAdultNumView() );
                    gxRsvDetail.setChildNum( frm.getChildNumView() );

                    // 通常オプション料金
                    int i = 0;
                    for( i = 0 ; i < frm.getOptInpMaxQuantityList().size() ; i++ )
                    {
                        gxRsvDetailOptionPrice = new GenerateXmlHapiTouchRsvDataDetailOptionPrice();
                        gxRsvDetailOptionPrice.setOptionName( frm.getOptNmList().get( i ) );
                        gxRsvDetailOptionPrice.setOptionUnitPrice( frm.getOptUnitPriceViewList().get( i ) );
                        gxRsvDetailOptionPrice.setOptionMaxQuantity( frm.getOptInpMaxQuantityList().get( i ) );
                        gxRsvDetailOptionPrice.setOptionTotal( frm.getOptChargeTotalList().get( i ) );
                        gxRsvDetailOptionPrice.setOptionRemarks( frm.getOptRemarksList().get( i ) );

                        gxRsvDetail.setOptionPrice( gxRsvDetailOptionPrice );
                    }
                    // 繰り返し

                    gxRsvDetail.setTotalPrice( frm.getChargeTotalView() );

                    boolean isNumber = false;
                    if ( frm.getOptNmImpList().size() > 1 )
                    {
                        if ( frm.getOptNumberList().get( frm.getOptNmImpList().size() - 1 ) > 1 )
                        {
                            isNumber = true;
                        }
                    }
                    int beforeNumber = -1;
                    // 必須オプションを取得
                    if ( frm.getExtFlag() == ReserveCommon.EXT_HAPIHOTE )
                    {
                        for( i = 0 ; i < frm.getOptNmImpList().size() ; i++ )
                        {
                            gxRsvDetailInperativeOption = new GenerateXmlHapiTouchRsvDataDetailImperativeOption();

                            String optName = "";
                            if ( beforeNumber != frm.getOptNumberList().get( i ) && isNumber )
                            {
                                optName = "【" + frm.getOptNumberList().get( i ) + "人目】\n";
                            }
                            optName += frm.getOptNmImpList().get( i );
                            gxRsvDetailInperativeOption.setImperativeOptionName( optName );

                            String optSubName = frm.getOptSubNmImpList().get( i );

                            if ( frm.getOptQuantityImpList().get( i ) > 1 )
                            {
                                optSubName += "×" + frm.getOptQuantityImpList().get( i );
                            }
                            gxRsvDetailInperativeOption.setImperativeOptionSubName( optSubName );

                            gxRsvDetail.setImperativeOption( gxRsvDetailInperativeOption );

                            beforeNumber = frm.getOptNumberList().get( i );
                        }
                    }
                    else
                    {
                        // lvj予約で連泊のとき、日付ごとの料金を出力
                        if ( StringUtils.isNotBlank( frm.getRsvNoMain() ) )
                        {
                            String[][] eachAmount = OwnerRsvCommon.getRsvEachAmountArr( frm.getSelHotelId(), frm.getRsvNo() );
                            int len = eachAmount.length;
                            for( int index = 0 ; index < len ; index++ )
                            {
                                gxRsvDetailInperativeOption = new GenerateXmlHapiTouchRsvDataDetailImperativeOption();
                                gxRsvDetailInperativeOption.setImperativeOptionName( eachAmount[index][0] );
                                gxRsvDetailInperativeOption.setImperativeOptionSubName( eachAmount[index][1] );
                                gxRsvDetail.setImperativeOption( gxRsvDetailInperativeOption );
                            }
                        }
                    }

                    // チェックイン開始時間をセット
                    gxRsvDetail.setCiTimeFrom( frm.getCiTime() );
                    gxRsvDetail.setCiTimeFromValue( frm.getCiTimeFrom() );
                    // チェックイン終了時間をセット
                    gxRsvDetail.setCiTimeTo( frm.getCiTimeToView() );
                    gxRsvDetail.setCiTimeToValue( frm.getCiTimeTo() );
                    // チェックアウト時間をセット
                    gxRsvDetail.setCoTime( frm.getCoTimeView() );
                    gxRsvDetail.setCar( frm.getParkingUsed() );
                    gxRsvDetail.setUserId( frm.getUserId() );
                    gxRsvDetail.setMailAddr( frm.getMail() );
                    gxRsvDetail.setMailReminder( frm.getReminderView() );
                    gxRsvDetail.setUserName( frm.getName() );
                    gxRsvDetail.setUserNameKana( "" );
                    gxRsvDetail.setZipCode( frm.getZip() );
                    gxRsvDetail.setAddress( frm.getAddress() );
                    gxRsvDetail.setTel( frm.getTel() );
                    gxRsvDetail.setDemands( frm.getDemandsView() );
                    if ( !frm.getRemarksView().equals( "" ) )
                    {
                        gxRsvDetail.setRemarks( frm.getQuestion() + "\n回答「" + frm.getRemarksView() + "」" );
                    }
                    gxRsvDetail.setCancelPolicy( frm.getCahcelPolicy() );
                    gxRsvDetail.setPaidCredit( frm.getPayCredit() );
                    gxRsvDetail.setPaidMile( frm.getUsedMile() );

                    if ( frm.getExtFlag() != ReserveCommon.EXT_HAPIHOTE ) // 外部予約の場合は、ci_status==3も読み込み対象とする
                    {
                        gxRsvDetail.setCiCode( this.getCheckInCode( hotelId, frm.getRsvNo(), "0,1,3,4" ) );
                    }
                    else
                    {
                        gxRsvDetail.setCiCode( this.getCheckInCode( hotelId, frm.getRsvNo() ) );
                    }
                }
                else
                {
                    // 予約番号がない
                    gxRsvDetail.setErrorCode( HapiTouchErrorMessage.ERR_20202 );
                }
            }
            else
            {
                // 予約番号エラー
                gxRsvDetail.setErrorCode( HapiTouchErrorMessage.ERR_20201 );
            }
            // XMLの出力
            String xmlOut = gxRsvDetail.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvDataDetail xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvDataDetail]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvDataDetail]Exception:" + e.toString() );
                }
            }

        }
    }

    /****
     * 予約部屋番号をセットする
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void getRsvRoom(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        GenerateXmlHapiTouchGetRsvRoom gxRsvRoom;
        GenerateXmlHapiTouchGetRsvRoomNo gxRsvRoomNo;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        String paramRsvNo;

        gxRsvRoom = new GenerateXmlHapiTouchGetRsvRoom();

        paramRsvNo = request.getParameter( "rsvNo" );
        // 終了日のチェック
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        // レスポンスをセット
        try
        {
            stream = response.getOutputStream();

            if ( paramRsvNo.compareTo( "" ) != 0 )
            {
                // フォームにセット
                frm.setSelHotelId( hotelId );
                frm.setRsvNo( paramRsvNo );

                // 予約データ抽出
                logic.setFrm( frm );
                // 実績データ取得
                logic.getData( 2 );
                // 予約未割り当て部屋のみを取得
                getSeqList( frm );

                frm.setMode( ReserveCommon.MODE_RAITEN );
                frm.setMode( "" ); // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
                frm.setErrMsg( "" );
                frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );

                if ( frm.getSeq() > 0 )
                {
                    for( int i = 0 ; i < frm.getSeqList().size() ; i++ )
                    {
                        gxRsvRoomNo = new GenerateXmlHapiTouchGetRsvRoomNo();
                        gxRsvRoomNo.setRoom( frm.getSeqList().get( i ) );
                        gxRsvRoomNo.setRoomName( this.getRoomName( hotelId, frm.getSeqList().get( i ) ) );
                        gxRsvRoom.setData( gxRsvRoomNo );
                    }
                    gxRsvRoom.setReserveRoom( frm.getSeq() );
                }
                else
                {
                    // 予約番号がない
                    gxRsvRoom.setErrorCode( HapiTouchErrorMessage.ERR_20302 );
                }
            }
            else
            {
                // パラメータエラー
                gxRsvRoom.setErrorCode( HapiTouchErrorMessage.ERR_20301 );
            }

            // XMLの出力
            String xmlOut = gxRsvRoom.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvRoom xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch getRsvRoom]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch getRsvRoom]Exception:" + e.toString() );
                }
            }

        }
    }

    /****
     * 予約来店確認を行う
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void rsvFix(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        Boolean ret = false;
        Boolean fixResult = false;
        GenerateXmlHapiTouchRsvFix gxRsvFix;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        String paramRsvNo;
        String paramRoomNo;
        String paramRoomNoEx;

        String roomHost = "";
        int roomSeq = 0;

        String paramVerifyCi;
        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();
        Boolean updateCi = true;
        boolean retVerify = false;
        String userId = "";

        // チェックインデータ作成のための変数
        HotelCi hc = new HotelCi();
        UserPointPay upp = new UserPointPay();
        int ciCode = 0;
        DataRsvReserve dhrr = new DataRsvReserve();

        gxRsvFix = new GenerateXmlHapiTouchRsvFix();

        paramRsvNo = request.getParameter( "rsvNo" );
        paramRoomNo = request.getParameter( "roomNo" ); // ホスト通信部屋名称 hh_hotel_room_more.room_name_host 相当
        paramRoomNoEx = request.getParameter( "roomNoEx" ); // hh_hotel_room_more.seq 相当
        paramVerifyCi = request.getParameter( "verifyCi" );

        // 終了日のチェック
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        if ( (paramRoomNo == null) || (paramRoomNo.equals( "" ) != false) )
        {
            paramRoomNo = "";
        }
        else
        {
            roomHost = paramRoomNo;
        }
        if ( (paramRoomNoEx == null) || (paramRoomNoEx.equals( "" ) != false) || CheckString.numCheck( paramRoomNoEx ) == false )
        {
            paramRoomNoEx = "0";
        }
        else
        {
            roomSeq = Integer.parseInt( paramRoomNoEx );
        }

        if ( (paramVerifyCi == null) || (paramVerifyCi.equals( "" ) != false) || CheckString.numCheck( paramVerifyCi ) == false )
        {
            paramVerifyCi = "0";
        }

        // レスポンスをセット
        try
        {
            stream = response.getOutputStream();

            if ( paramRsvNo.compareTo( "" ) != 0 )
            {
                // フォームにセット
                frm.setSelHotelId( hotelId );
                frm.setRsvNo( paramRsvNo );

                // 予約データ抽出
                logic.setFrm( frm );
                // 実績データ取得
                logic.getData( 2 );

                frm.setMode( ReserveCommon.MODE_RAITEN );
                frm.setErrMsg( "" );
                frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );
                frm.setRoomNo( frm.getSeq() );
                frm.setPlanType( logic.getFrm().getPlanType() );

                ret = hc.getCheckInBeforeData( hotelId, frm.getUserId(), paramRsvNo, roomHost );

                Logging.info( "rsvFix Start :paramVerifyCi:" + paramVerifyCi );
                Logging.info( "rsvFix Start :ret:" + ret );

                // 同ユーザの24時間以内チェックインデータがある場合は処理しない
                if ( ret == false )
                {

                    if ( paramVerifyCi.equals( "0" ) == false )
                    {
                        if ( hc.getData( hotelId, Integer.parseInt( paramVerifyCi ) ) != false )
                        {
                            if ( frm.getUserId().equals( hc.getHotelCi().getUserId() ) != false )
                            {
                                retVerify = true;
                            }
                            else
                            {
                                retVerify = false;
                            }

                        }
                        else
                        {
                            retVerify = true;
                        }
                    }
                    else
                    {
                        retVerify = true;
                    }

                    if ( frm.getSeq() > 0 && retVerify != false )
                    {
                        // 予約登録したユーザーのメールアドレス取得
                        UserBasicInfo ubi = new UserBasicInfo();
                        ubi.getUserBasic( frm.getUserId() );
                        frm.setLoginUserId( frm.getUserId() );
                        if ( ubi.getUserInfo().getMailAddr().equals( "" ) == false )
                        {
                            frm.setLoginUserMail( ubi.getUserInfo().getMailAddr() );
                            frm.setMail( ubi.getUserInfo().getMailAddr() );
                        }
                        else
                        {
                            frm.setLoginUserMail( ubi.getUserInfo().getMailAddrMobile() );
                            frm.setMail( ubi.getUserInfo().getMailAddrMobile() );
                        }
                        frm.setLoginUserTel( ubi.getUserInfo().getTel1() );

                        // リクエストで取得した部屋番号をセット
                        if ( roomSeq == 0 )
                        {
                            frm.setSeq( getSeq( hotelId, roomHost ) );
                        }
                        else
                        {
                            frm.setSeq( roomSeq );
                        }
                        DataRsvPlan dataPlan = new DataRsvPlan();
                        int offerKind;

                        // ホテルの提供区分取得
                        dataPlan.getData( frm.getSelHotelId(), frm.getSelPlanId() );
                        offerKind = dataPlan.getOfferKind();
                        frm.setOfferKind( offerKind );

                        // ▼来店確認
                        int statusOld = frm.getStatus();
                        frm = htRsvSub.execRaiten( frm, frm.getStatus() );

                        // エラーあり時
                        if ( frm.getErrMsg().trim().length() != 0 && statusOld != ReserveCommon.RSV_STATUS_ZUMI ) // 来店済みはエラーとしない
                        {
                            errCode = HapiTouchErrorMessage.ERR_20403;
                            Logging.error( frm.getErrMsg() );
                        }
                        else
                        {
                            // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
                            frm.setMode( "" );
                            fixResult = true;
                        }

                        if ( fixResult != false )
                        {
                            // 未確定のデータが24時間以内にあったら登録しない。
                            ret = hc.getCheckInBeforeData( hotelId, frm.getUserId(), paramRsvNo );

                            if ( ret == false )
                            {
                                if ( hc.isRaiten( hotelId, paramRsvNo ) ) // 同じ予約番号で来店済だった場合はもともとのデータを無効にする
                                {
                                    hc.getHotelCi().setCiStatus( CI_STATUS_NOT_DISPLAY );
                                    hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                    hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                    hc.getHotelCi().updateData( hotelId, hc.getHotelCi().getSeq(), hc.getHotelCi().getSubSeq() );
                                }

                                // チェックインデータ登録
                                hc = hc.registCiData( frm.getUserId(), hotelId );
                                ciCode = hc.getHotelCi().getSeq();
                                if ( ciCode > 0 )
                                {
                                    ret = true;
                                }
                                else
                                {
                                    ret = false;
                                }
                            }
                            else
                            // 未確定のデータが24時間以内にあった
                            {
                                ciCode = hc.getHotelCi().getSeq();
                                ret = false;
                            }
                            if ( errCode == 0 )
                            {
                                // ハピホテ会員以外が予約来店した場合は、チェックインデータを無効扱いとする
                                if ( ubi.isLvjUser( frm.getUserId() ) )
                                {
                                    hc.getHotelCi().setCiStatus( 4 );
                                    hc.getHotelCi().setExtUserFlag( 1 );
                                }
                                else
                                {
                                    hc.getHotelCi().setExtUserFlag( 0 );
                                }
                                // 部屋名が登録済みであれば枝番追加で挿入、登録されていなければ更新
                                if ( hc.getHotelCi().getRoomNo().compareTo( "" ) != 0 )
                                {
                                    updateCi = false;
                                }
                                else
                                {
                                    updateCi = true;
                                }
                                if ( roomHost.equals( "" ) )
                                {
                                    hc.getHotelCi().setRoomNo( this.getRoomName( hotelId, frm.getSeq() ) );
                                }
                                else
                                {
                                    hc.getHotelCi().setRoomNo( roomHost );
                                }
                                // 予約番号をセット
                                hc.getHotelCi().setRsvNo( paramRsvNo );

                                // 予約入力時使用マイルをセット
                                if ( dhrr.getData( hotelId, paramRsvNo ) != false )
                                {
                                    hc.getHotelCi().setUsePoint( dhrr.getUsedMile() );
                                    if ( dhrr.getUsedMile() != 0 )
                                    {
                                        hc.getHotelCi().setUseDate( dhrr.getAcceptDate() );
                                        hc.getHotelCi().setUseTime( dhrr.getAcceptTime() );
                                    }
                                }

                                // 予約の利用料金をセットしておく
                                // hc.getHotelCi().setAmount( frm.getChargeTotal() + frm.getOptionChargeTotal() );
                                // チェックインデータを更新またはインサート
                                if ( updateCi != false )
                                {
                                    // 更新
                                    ret = hc.getHotelCi().updateData( hotelId, hc.getHotelCi().getSeq(), hc.getHotelCi().getSubSeq() );
                                }
                                else
                                {
                                    // falseならば枝番号を追加する必要がある
                                    if ( ret == false )
                                    {
                                        hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                                    }
                                    ret = hc.getHotelCi().insertData();
                                }
                            }
                            if ( errCode == 0 )
                            {
                                gxRsvFix.setResult( RESULT_OK );
                                gxRsvFix.setRsvRoomNo( roomHost );
                                gxRsvFix.setStatus( this.getRsvStatus( frm.getStatus() ) );
                                gxRsvFix.setStatusValue( frm.getStatus() );
                                if ( ret != false )
                                {
                                    gxRsvFix.setCiResult( RESULT_OK );
                                    // ここに予約マイル登録処理を追加
                                    // ハピホテ予約のときだけ
                                    if ( frm.getExtFlag() == ReserveCommon.EXT_HAPIHOTE )
                                    {
                                        if ( statusOld != ReserveCommon.RSV_STATUS_ZUMI )
                                        {
                                            errCode = htRsvSub.setRsvPoint( hc );
                                        }
                                    }
                                }
                                else
                                {
                                    gxRsvFix.setCiResult( RESULT_NG );
                                }
                                gxRsvFix.setPoint( upp.getNowPoint( frm.getUserId(), false ) );
                                gxRsvFix.setCiCode( ciCode );
                                gxRsvFix.setCiDate( hc.getHotelCi().getCiDate() );
                                gxRsvFix.setCiTime( hc.getHotelCi().getCiTime() );
                                gxRsvFix.setUserId( Integer.toString( hc.getHotelCi().getUserSeq() ) );
                                gxRsvFix.setAmountRate( hc.getHotelCi().getAmountRate() );
                                gxRsvFix.setRoomNo( hc.getHotelCi().getRoomNo() );
                                gxRsvFix.setRoomName( hc.getHotelCi().getRoomNo() );
                                gxRsvFix.setUsePoint( hc.getHotelCi().getUsePoint() );
                                gxRsvFix.setAmount( hc.getHotelCi().getAmount() );
                                gxRsvFix.setSlipNo( hc.getHotelCi().getSlipNo() );
                                gxRsvFix.setRsvNo( frm.getRsvNo() );
                                gxRsvFix.setUserType( hc.getHotelCi().getUserType() );
                                gxRsvFix.setPaidCredit( frm.getPayCredit() );
                                gxRsvFix.setPaidMile( frm.getUsedMile() );
                                gxRsvFix.setErrorCode( errCode );
                                if ( !hc.getHotelCi().getRsvNo().equals( "" ) )
                                {
                                    if ( hc.getHotelCi().getExtUserFlag() == 1 )
                                    {
                                        if ( DataRsvReserve.getExtFlag( hotelId, hc.getHotelCi().getRsvNo() ) == 1 )
                                        {
                                            gxRsvFix.setUserId( "LIJ" );
                                            // gxRsvFix.setUserType( 2 );
                                        }
                                    }
                                }
                            }
                            else
                            {
                                gxRsvFix.setResult( "NG" );
                                gxRsvFix.setStatus( "来店" );
                                gxRsvFix.setStatusValue( 2 );
                                gxRsvFix.setCiResult( RESULT_NG );
                                gxRsvFix.setCiCode( ciCode );
                                gxRsvFix.setCiDate( hc.getHotelCi().getCiDate() );
                                gxRsvFix.setCiTime( hc.getHotelCi().getCiTime() );
                                gxRsvFix.setUserId( Integer.toString( hc.getHotelCi().getUserSeq() ) );
                                gxRsvFix.setAmountRate( hc.getHotelCi().getAmountRate() );
                                gxRsvFix.setRoomNo( hc.getHotelCi().getRoomNo() );
                                gxRsvFix.setRoomName( hc.getHotelCi().getRoomNo() );
                                gxRsvFix.setUsePoint( hc.getHotelCi().getUsePoint() );
                                gxRsvFix.setAmount( hc.getHotelCi().getAmount() );
                                gxRsvFix.setSlipNo( hc.getHotelCi().getSlipNo() );
                                gxRsvFix.setRsvNo( frm.getRsvNo() );
                                gxRsvFix.setUserType( hc.getHotelCi().getUserType() );
                                gxRsvFix.setPaidCredit( frm.getPayCredit() );
                                gxRsvFix.setPaidMile( frm.getUsedMile() );
                                gxRsvFix.setErrorCode( errCode );
                            }
                        }
                        else
                        {
                            // 結果をセット
                            gxRsvFix.setResult( "NG" );
                            gxRsvFix.setPoint( upp.getNowPoint( frm.getUserId(), false ) );
                            gxRsvFix.setStatus( this.getRsvStatus( frm.getStatus() ) );
                            gxRsvFix.setStatusValue( frm.getStatus() );
                            gxRsvFix.setErrorCode( errCode );
                            gxRsvFix.setRsvNo( paramRsvNo );
                            gxRsvFix.setRoomNo( roomHost );
                        }

                        userId = frm.getUserId();
                    }
                    else
                    {
                        if ( retVerify == false )
                        {
                            errCode = HapiTouchErrorMessage.ERR_20400;
                        }
                        else
                        {
                            // 予約番号がない
                            errCode = HapiTouchErrorMessage.ERR_20402;
                        }

                        gxRsvFix.setResult( "NG" );
                        gxRsvFix.setStatus( "" );
                        gxRsvFix.setStatusValue( 0 );
                        gxRsvFix.setRsvNo( paramRsvNo );
                        gxRsvFix.setRoomNo( roomHost );
                        // 予約番号がない
                        gxRsvFix.setErrorCode( errCode );
                    }
                }
                else
                {
                    errCode = HapiTouchErrorMessage.ERR_20400;
                    gxRsvFix.setResult( "NG" );
                    gxRsvFix.setStatus( "" );
                    gxRsvFix.setStatusValue( 0 );
                    gxRsvFix.setRsvNo( paramRsvNo );
                    gxRsvFix.setRoomNo( roomHost );
                    gxRsvFix.setErrorCode( errCode );
                }
            }
            else
            {
                // 予約番号エラー
                gxRsvFix.setErrorCode( -1 );
            }

            StringBuffer requestUrl = request.getRequestURL();
            String requestUrlStr = requestUrl.toString();
            int errorCode = 0;
            if ( HotelIp.getFrontIp( hotelId ) != "" && requestUrlStr.indexOf( "happyhotel.jp" ) > 0 )
            {
                errorCode = HapiTouchErrorMessage.ERR_20407;
            }
            if ( errorCode == 0 )
            {
                // IPアドレスが登録されている場合は、ホスト連動物件。予約データの最新状態をホストに伝える
                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                {
                    RsvList rl = new RsvList();
                    if ( rl.getData( hotelId, 0, 0, 0, paramRsvNo, 0 ) != false )
                    {
                        rl.sendToHost( hotelId );
                    }
                }
            }

            if ( errorCode != 0 )
            // タッチPCからオフラインの電文が送られてきた場合にはエラー履歴に書き込む
            {
                Logging.info( "rsvFix Error:" + errorCode, "rsvFix Error" );
                DataHotelBasic dhb;
                dhb = new DataHotelBasic();
                String hotenaviId = "";
                String hotelName = "";
                // ホテナビIDを取得する
                if ( dhb.getData( hotelId ) != false )
                {
                    hotenaviId = dhb.getHotenaviId();
                    hotelName = dhb.getName();
                }
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( roomHost );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( ciCode );
                daeh.setUserId( userId );
                daeh.setReserveNo( paramRsvNo );
                daeh.insertData();
                errorCode = 0;
            }
            // if ( errorCode == 0 )
            // {
            // // IPアドレスが登録されている場合は、ホスト連動物件。予約があったことをホストに伝える
            // if ( !HotelIp.getFrontIp( hotelId ).equals( "" ) )
            // {
            // int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            // int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
            // // 5時以前の場合は前日とする
            // if ( 50000 > nowTime )
            // {
            // nowTime = nowTime + 240000;
            // nowDate = DateEdit.addDay( nowDate, -1 );
            // }
            // if ( nRsvDate == nowDate// 予約日付が当日の場合
            // || (nRsvDate == DateEdit.addDay( nowDate, 1 ) && nEstTimeArrival < nowTime) /* 予約日付が翌日で来店予定時刻が24時間以内の場合 */)
            // {
            // RsvList rl = new RsvList();
            // if ( rl.getData( hotelId, 0, 0, 0, paramRsvNo ) != false )
            // {
            // rl.sendToHost( hotelId );
            // }
            // }
            // }
            // }
            //
            // XMLの出力
            String xmlOut = gxRsvFix.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvFix xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvFix]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvFix]Exception:" + e.toString() );
                }
            }

        }
    }

    /****
     * 予約キャンセルを行う
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void rsvCancel(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        GenerateXmlHapiTouchRsvCancel gxRsvCancel;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        String paramRsvNo;
        String paramNoShow;
        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();

        // チェックインデータ作成のための変数

        gxRsvCancel = new GenerateXmlHapiTouchRsvCancel();

        paramRsvNo = request.getParameter( "rsvNo" );
        paramNoShow = request.getParameter( "noShow" );
        // 終了日のチェック
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        if ( (paramNoShow == null) || (paramNoShow.equals( "" ) != false) || CheckString.numCheck( paramNoShow ) == false )
        {
            paramNoShow = "";
        }

        // レスポンスをセット
        try
        {
            stream = response.getOutputStream();

            if ( paramRsvNo.compareTo( "" ) != 0 )
            {
                // フォームにセット
                frm.setSelHotelId( hotelId );
                frm.setRsvNo( paramRsvNo );

                // 予約データ抽出
                logic.setFrm( frm );
                // 実績データ取得
                logic.getData( 2 );

                if ( frm.getExtFlag() != ReserveCommon.EXT_OTA )
                {
                    frm.setMode( ReserveCommon.MODE_CANCEL );
                    frm.setMode( "" ); // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
                    frm.setErrMsg( "" );
                    frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );

                    if ( frm.getSeq() > 0 )
                    {
                        // 予約登録したユーザーのメールアドレス取得
                        UserBasicInfo ubi = new UserBasicInfo();
                        ubi.getUserBasic( frm.getUserId() );
                        frm.setLoginUserId( frm.getUserId() );
                        frm.setLoginUserMail( ubi.getUserInfo().getMailAddr() );
                        frm.setLoginUserTel( ubi.getUserInfo().getTel1() );
                        frm.setMail( ubi.getUserInfo().getMailAddr() );
                        // リクエストで取得したノーショウフラグをセット
                        frm.setNoShow( Integer.parseInt( paramNoShow ) );
                        // キャンセルフラグを立てる
                        frm.setCancelCheck( 1 );

                        DataRsvPlan dataPlan = new DataRsvPlan();
                        int offerKind;

                        // ホテルの提供区分取得
                        dataPlan.getData( frm.getSelHotelId(), frm.getSelPlanId() );
                        offerKind = dataPlan.getOfferKind();
                        frm.setOfferKind( offerKind );
                        frm.setPlanType( logic.getFrm().getPlanType() );

                        // ▼キャンセル
                        frm = htRsvSub.execCancel( frm, frm.getStatus() );

                        if ( frm.getErrMsg().trim().length() != 0 )
                        {
                            errCode = HapiTouchErrorMessage.ERR_20503;
                            gxRsvCancel.setStatus( this.getRsvStatus( frm.getStatus() ) );
                            gxRsvCancel.setStatusValue( frm.getStatus() );
                        }
                        else
                        {
                            // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
                            frm.setMode( "" );
                            gxRsvCancel.setResult( RESULT_OK );
                            gxRsvCancel.setStatus( this.getRsvStatus( frm.getStatus() ) );
                            gxRsvCancel.setStatusValue( frm.getStatus() );
                            gxRsvCancel.setRsvNo( frm.getRsvNo() );
                        }
                    }
                    else
                    {
                        // 予約部屋割当がない
                        errCode = HapiTouchErrorMessage.ERR_20502;
                        gxRsvCancel.setStatusValue( -99 );
                    }

                }
                else
                {
                    // OTAからの予約
                    errCode = HapiTouchErrorMessage.ERR_20507;
                    gxRsvCancel.setStatusValue( -99 );
                }
            }
            else
            {
                // 予約番号エラー
                errCode = HapiTouchErrorMessage.ERR_20501;
                gxRsvCancel.setStatusValue( -99 );
            }

            if ( errCode == 0 )
            {
                // IPアドレスが登録されている場合は、ホスト連動物件。予約があったことをホストに伝える
                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                {
                    int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                    int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                    // 5時以前の場合は前日とする
                    if ( 50000 > nowTime )
                    {
                        nowTime = nowTime + 240000;
                        nowDate = DateEdit.addDay( nowDate, -1 );
                    }
                    if ( frm.getRsvDate() == nowDate// 予約日付が当日の場合
                            || (frm.getRsvDate() == DateEdit.addDay( nowDate, 1 ) && frm.getEstTimeArrival() < nowTime) /* 予約日付が翌日で来店予定時刻が24時間以内の場合 */)
                    {
                        RsvList rl = new RsvList();
                        if ( rl.getData( hotelId, frm.getRsvDate(), frm.getRsvDate(), 0, paramRsvNo, 1 ) != false )
                        {
                            rl.sendToHost( hotelId );
                        }
                    }
                }
            }
            else
            {
                gxRsvCancel.setResult( RESULT_NG );
                gxRsvCancel.setRsvNo( paramRsvNo );
                gxRsvCancel.setErrorCode( errCode );
            }

            // XMLの出力
            String xmlOut = gxRsvCancel.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvCancel xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvCancel]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvCancel]Exception:" + e.toString() );
                }
            }

        }
    }

    /***
     * 予約ステータス文言取得
     * 
     * @param status
     * @return レスポンス
     */
    public String getRsvStatus(int status)
    {

        String statusWord = "";
        if ( status == ReserveCommon.RSV_STATUS_UKETUKE )
        {
            statusWord = "来店待ち";
        }
        else if ( status == ReserveCommon.RSV_STATUS_ZUMI )
        {
            statusWord = "来店";
        }
        else if ( status == ReserveCommon.RSV_STATUS_CANCEL )
        {
            statusWord = "キャンセル";
        }
        return statusWord;
    }

    /****
     * 到着時刻変更を行う
     * 
     * @param hotelId ホテルID
     * @param request リクエスト内容
     * @param response レスポンス
     */
    public void rsvModifyArrvialTime(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        Boolean ret = false;

        GenerateXmlHapiTouchRsvModifyArrivalTime gxRsvModifyArrivalTime;
        String paramRsvNo;
        String paramArrivalTime;
        DataRsvReserve drr = new DataRsvReserve();
        int nEstTimeArrival = 0;
        int nEstTimeArrivalBefore = 0;
        int nRsvDate = 0;

        gxRsvModifyArrivalTime = new GenerateXmlHapiTouchRsvModifyArrivalTime();

        paramRsvNo = request.getParameter( "rsvNo" );
        paramArrivalTime = request.getParameter( "arrivalTime" );
        // 終了日のチェック
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        if ( (paramArrivalTime == null) || (paramArrivalTime.equals( "" ) != false) || CheckString.numCheck( paramArrivalTime ) == false )
        {
            paramArrivalTime = "0";
        }

        // レスポンスをセット
        try
        {
            stream = response.getOutputStream();
            if ( paramRsvNo.compareTo( "" ) != 0 )
            {
                ret = drr.getData( hotelId, paramRsvNo );
                if ( ret != false )
                {
                    if ( drr.getStatus() == ReserveCommon.RSV_STATUS_UKETUKE )
                    {
                        nEstTimeArrivalBefore = drr.getEstTimeArrival();
                        // 予約番号をセットしアップデート
                        drr.setEstTimeArrival( Integer.parseInt( paramArrivalTime ) );
                        ret = drr.updateEstTimeArrivalData( hotelId, paramRsvNo, ReserveCommon.SCHEMA_NEWRSV );
                        if ( ret != false )
                        {
                            gxRsvModifyArrivalTime.setResult( "OK" );
                            drr = null;
                            drr = new DataRsvReserve();
                            drr.getData( hotelId, paramRsvNo );
                            nEstTimeArrival = drr.getEstTimeArrival();
                            nRsvDate = drr.getReserveDate();
                            // 時刻が24時を過ぎたら翌日の日付時刻に変換する
                            if ( nEstTimeArrival >= AM12 )
                            {
                                nEstTimeArrival -= AM12;
                                nRsvDate = DateEdit.addDay( drr.getReserveDate(), TOMORROW );
                            }

                            gxRsvModifyArrivalTime.setArraivalDateValue( nRsvDate );
                            gxRsvModifyArrivalTime.setArraivalTime( ReserveCommon.getArrivalTimeView( drr.getEstTimeArrival() ) );
                            gxRsvModifyArrivalTime.setArraivalTimeValue( nEstTimeArrival );
                            gxRsvModifyArrivalTime.setRsvDateValue( drr.getReserveDate() );
                            /* 1016電文用に元に戻しておく */
                            nEstTimeArrival = drr.getEstTimeArrival();
                            nRsvDate = drr.getReserveDate();
                        }
                        else
                        {
                            // 予約データ更新失敗
                            errCode = HapiTouchErrorMessage.ERR_20604;
                            gxRsvModifyArrivalTime.setResult( "NG" );
                            gxRsvModifyArrivalTime.setErrorCode( errCode );
                        }
                    }
                    else
                    {
                        // 予約データなし
                        errCode = HapiTouchErrorMessage.ERR_20602;
                        gxRsvModifyArrivalTime.setResult( "NG" );
                        gxRsvModifyArrivalTime.setErrorCode( errCode );
                    }
                }
                else
                {
                    // 予約データなし
                    errCode = HapiTouchErrorMessage.ERR_20602;
                    gxRsvModifyArrivalTime.setResult( "NG" );
                    gxRsvModifyArrivalTime.setErrorCode( errCode );
                }
            }
            else
            {
                // 予約番号エラー
                errCode = HapiTouchErrorMessage.ERR_20601;
                gxRsvModifyArrivalTime.setResult( "NG" );
                gxRsvModifyArrivalTime.setErrorCode( errCode );
            }
            if ( errCode == 0 )
            {
                // IPアドレスが登録されている場合は、ホスト連動物件。予約があったことをホストに伝える
                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                {
                    int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                    int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                    // 5時以前の場合は前日とする
                    if ( 50000 > nowTime )
                    {
                        nowTime = nowTime + 240000;
                        nowDate = DateEdit.addDay( nowDate, -1 );
                    }
                    if ( nRsvDate == nowDate// 予約日付が当日の場合
                            || (nRsvDate == DateEdit.addDay( nowDate, 1 ) && nEstTimeArrivalBefore < nowTime /* 予約日付が翌日で変更前の来店予定時刻が24時間以内の場合 */)
                            || (nRsvDate == DateEdit.addDay( nowDate, 1 ) && nEstTimeArrival < nowTime) /* 予約日付が翌日で来店予定時刻が24時間以内の場合 */)
                    {
                        RsvList rv = new RsvList();
                        if ( rv.getData( hotelId, 0, 0, 0, paramRsvNo, 0 ) != false )
                        {
                            rv.sendToHost( hotelId );
                        }
                    }
                }
            }
            // XMLの出力
            String xmlOut = gxRsvModifyArrivalTime.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvModifyArrvialTime xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvModifyArrvialTime]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvModifyArrvialTime]Exception:" + e.toString() );
                }
            }
        }
    }

    /****
     * 予約キャンセル取消
     * 
     * @param hotelId ホテルID
     * @param request リクエスト内容
     * @param response レスポンス
     */
    public void rsvUndoCancel(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        boolean undoResult = false;
        GenerateXmlHapiTouchRsvUndoCancel gxRsvUndoCancel;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        String paramRsvNo;
        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();

        gxRsvUndoCancel = new GenerateXmlHapiTouchRsvUndoCancel();

        paramRsvNo = request.getParameter( "rsvNo" );

        // 予約番号のチェック
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        try
        {
            stream = response.getOutputStream();

            if ( paramRsvNo.compareTo( "" ) != 0 )
            {
                // フォームにセット
                frm.setSelHotelId( hotelId );
                frm.setRsvNo( paramRsvNo );

                // 予約データ抽出
                logic.setFrm( frm );
                // 実績データ取得
                logic.getData( 2 );

                frm.setMode( "" ); // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
                frm.setErrMsg( "" );
                frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );

                // 部屋番号が登録されており、キャンセルのステータスのデータのみ対象とする
                if ( frm.getSeq() > 0 && frm.getStatus() == ReserveCommon.RSV_STATUS_CANCEL )
                {
                    // 予約登録したユーザーのメールアドレス取得
                    UserBasicInfo ubi = new UserBasicInfo();
                    ubi.getUserBasic( frm.getUserId() );
                    frm.setLoginUserId( frm.getUserId() );
                    frm.setLoginUserMail( ubi.getUserInfo().getMailAddr() );
                    frm.setLoginUserTel( ubi.getUserInfo().getTel1() );
                    frm.setMail( ubi.getUserInfo().getMailAddr() );
                    frm.setPlanType( logic.getFrm().getPlanType() );

                    DataRsvPlan dataPlan = new DataRsvPlan();
                    int offerKind;

                    // ホテルの提供区分取得
                    dataPlan.getData( frm.getSelHotelId(), frm.getSelPlanId() );
                    offerKind = dataPlan.getOfferKind();
                    frm.setOfferKind( offerKind );

                    // ▼キャンセル
                    frm = htRsvSub.execUndoCancel( frm, frm.getStatus() );

                    // エラーあり時
                    if ( frm.getErrMsg().trim().length() != 0 )
                    {
                        errCode = HapiTouchErrorMessage.ERR_20704;
                    }
                    else
                    {
                        undoResult = true;
                    }
                }
                else
                {
                    if ( frm.getSeq() <= 0 )
                    {
                        // データなし
                        errCode = HapiTouchErrorMessage.ERR_20702;
                    }
                    else
                    {
                        // ステータスがキャンセル以外
                        errCode = HapiTouchErrorMessage.ERR_20703;
                    }
                }
            }
            // パラメータ異常
            else
            {
                errCode = HapiTouchErrorMessage.ERR_20701;
                gxRsvUndoCancel.setResult( "NG" );
            }

            if ( undoResult != false )
            {
                gxRsvUndoCancel.setResult( "OK" );
            }
            else
            {
                gxRsvUndoCancel.setResult( "NG" );
            }

            gxRsvUndoCancel.setStatus( frm.getStatus() );
            gxRsvUndoCancel.setErrorCode( errCode );
            if ( errCode == 0 )
            {
                // IPアドレスが登録されている場合は、ホスト連動物件。予約があったことをホストに伝える
                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                {
                    int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                    int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                    // 5時以前の場合は前日とする
                    if ( 50000 > nowTime )
                    {
                        nowTime = nowTime + 240000;
                        nowDate = DateEdit.addDay( nowDate, -1 );
                    }
                    if ( frm.getRsvDate() == nowDate// 予約日付が当日の場合
                            || (frm.getRsvDate() == DateEdit.addDay( nowDate, 1 ) && frm.getEstTimeArrival() < nowTime) /* 予約日付が翌日で来店予定時刻が24時間以内の場合 */)
                    {
                        RsvList rl = new RsvList();
                        if ( rl.getData( hotelId, frm.getRsvDate(), frm.getRsvDate(), 0, paramRsvNo, 1 ) != false )
                        {
                            rl.sendToHost( hotelId );
                        }
                    }
                }
            }

            // XMLの出力
            String xmlOut = gxRsvUndoCancel.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvUndoCancel xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvUndoCancel]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvUndoCancel]Exception:" + e.toString() );
                }
            }
        }
    }

    /****
     * 予約来店確認取消を行う
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void rsvUndoFix(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        boolean ret = false;
        boolean undoResult = false;
        GenerateXmlHapiTouchRsvUndoFix gxRsvUndoFix;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        String paramRsvNo;
        String paramSeq;
        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();
        HotelCi hc = new HotelCi();
        HapiTouchBko bko = new HapiTouchBko();

        gxRsvUndoFix = new GenerateXmlHapiTouchRsvUndoFix();

        paramRsvNo = request.getParameter( "rsvNo" );
        paramSeq = request.getParameter( "seq" );

        // 予約番号のチェック
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        if ( (paramSeq == null) || (paramSeq.equals( "" ) != false) || (CheckString.numCheck( paramSeq ) == false) )
        {
            paramSeq = "0";
        }

        // 予約データを読む
        int extFlag = 0;
        DataRsvReserve drr = new DataRsvReserve();
        if ( drr.getData( hotelId, paramRsvNo ) )
        {
            extFlag = drr.getExtFlag();
        }

        /*
         * HtCheckIn によりチェックインした場合は、ハピホテタッチPCの予約詳細ではチェックインコードがわからない
         * ので予約Noよりチェックインコードを取得する。
         */
        paramSeq = Integer.toString( getCheckInCode( hotelId, paramRsvNo ) );

        try
        {
            stream = response.getOutputStream();
            if ( (paramRsvNo.compareTo( "" ) != 0) && (Integer.parseInt( paramSeq ) > 0) )
            {
                // フォームにセット
                frm.setSelHotelId( hotelId );
                frm.setRsvNo( paramRsvNo );

                // 予約データ抽出
                logic.setFrm( frm );
                // 実績データ取得
                logic.getData( 2 );

                frm.setMode( "" ); // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
                frm.setErrMsg( "" );
                frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );

                // ホテルチェックインデータを取得する
                ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );

                if ( ret != false )
                {
                    hc = htRsvSub.visitCancel( hotelId, hc, 1 );
                    if ( hc.getErrorMsg() > 0 )
                    {
                        errCode = hc.getErrorMsg();
                        ret = false;
                    }
                    else
                    {
                        /** ハピホテタッチ端末用に追加 **/
                        hc.registTouchCi( hc.getHotelCi(), false );
                        /** ハピホテタッチ端末用に追加 **/
                    }
                }
                else
                {
                    ret = false;
                    errCode = HapiTouchErrorMessage.ERR_20905;
                }

                if ( ret != false )
                {
                    // 部屋番号が登録されており、来店のステータスのデータで、チェックインデータが作成されている場合
                    if ( frm.getSeq() > 0 && frm.getStatus() == ReserveCommon.RSV_STATUS_ZUMI )
                    {
                        // 予約登録したユーザーのメールアドレス取得
                        UserBasicInfo ubi = new UserBasicInfo();
                        ubi.getUserBasic( frm.getUserId() );
                        frm.setLoginUserId( frm.getUserId() );
                        frm.setLoginUserMail( ubi.getUserInfo().getMailAddr() );
                        frm.setLoginUserTel( ubi.getUserInfo().getTel1() );
                        frm.setMail( ubi.getUserInfo().getMailAddr() );

                        DataRsvPlan dataPlan = new DataRsvPlan();
                        int offerKind;

                        // ホテルの提供区分取得
                        dataPlan.getData( frm.getSelHotelId(), frm.getSelPlanId() );
                        offerKind = dataPlan.getOfferKind();
                        frm.setOfferKind( offerKind );
                        frm.setPlanType( logic.getFrm().getPlanType() );

                        // ▼来店のキャンセル
                        frm = htRsvSub.execUndoFix( frm, frm.getStatus() );

                        // エラーあり時
                        if ( frm.getErrMsg().trim().length() != 0 )
                        {
                            errCode = HapiTouchErrorMessage.ERR_20904;
                        }
                        else
                        {
                            if ( hc.getHotelCi().getCiStatus() <= 2 && extFlag == 0 )
                            {
                                errCode = hc.getErrorMsg();
                                UserPointPayTemp uppt = new UserPointPayTemp();
                                UserPointPay upp = new UserPointPay();

                                undoResult = bko.cancelBkoData( hc.getHotelCi().getUserId(), hotelId, POINT_KIND_YOYAKU, hc.getHotelCi() );
                                if ( undoResult == false )
                                {
                                    // ステータスがキャンセル以外
                                    errCode = HapiTouchErrorMessage.ERR_20912;

                                }

                                // ハピホテ予約の場合は予約マイルを取り消す
                                undoResult = uppt.cancelRsvPoint( hc.getHotelCi(), POINT_KIND_YOYAKU, 0 );
                                if ( undoResult == false )
                                {
                                    errCode = HapiTouchErrorMessage.ERR_20913;
                                }
                                // upptのマイル取り消しがうまくいったら、uppも取り消す
                                if ( undoResult != false )
                                {
                                    undoResult = upp.cancelRsvPoint( hc.getHotelCi(), POINT_KIND_YOYAKU, 0 );
                                }
                            }
                            else
                            {
                                undoResult = true;
                            }
                        }
                    }
                    else
                    {
                        if ( frm.getSeq() <= 0 )
                        {
                            // データなし
                            errCode = HapiTouchErrorMessage.ERR_20902;
                        }
                        else
                        {
                            // ステータスがキャンセル以外
                            errCode = HapiTouchErrorMessage.ERR_20911;
                        }
                    }
                }
                else
                {
                    undoResult = false;
                }
            }
            else
            {
                errCode = HapiTouchErrorMessage.ERR_20901;
                gxRsvUndoFix.setResult( "NG" );
            }

            if ( undoResult != false )
            {
                gxRsvUndoFix.setResult( "OK" );
            }
            else
            {
                gxRsvUndoFix.setResult( "NG" );
            }

            gxRsvUndoFix.setStatus( frm.getStatus() );
            gxRsvUndoFix.setErrorCode( errCode );

            if ( errCode == 0 )
            {
                // IPアドレスが登録されている場合は、ホスト連動物件。予約データの最新状態をホストに伝える
                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                {
                    RsvList rl = new RsvList();
                    if ( rl.getData( hotelId, 0, 0, 0, paramRsvNo, 0 ) != false )
                    {
                        rl.sendToHost( hotelId );
                    }
                }
            }

            // XMLの出力
            String xmlOut = gxRsvUndoFix.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvUndoFix xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvUndoFix]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvUndoFix]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * 予約設定取得
     * 
     * @param hotelId ホテルID
     * @param response レスポンス
     **/
    public void getRsvConfig(int hotelId, HttpServletResponse response)
    {

        GenerateXmlRsvGetConfig gxTouch;
        ServletOutputStream stream = null;
        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
        int manageDate = nowTime <= ReserveCommon.RSV_DEADLINE_TIME ? DateEdit.addDay( nowDate, -1 ) : nowDate;

        gxTouch = new GenerateXmlRsvGetConfig();

        // レスポンスをセット
        try
        {
            stream = response.getOutputStream();
            gxTouch.setManageDate( Integer.toString( manageDate ) );
            gxTouch.setDeadlineTime( Integer.toString( ReserveCommon.RSV_DEADLINE_TIME ) );

            // XMLの出力
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;

            Logging.info( xmlOut );
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch getRsvConfig]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch getRsvConfig]Exception:" + e.toString() );
                }
            }
        }
    }

    /*****
     * 予約部屋変更 (modifyCi,corrctCi)
     * 
     * @param hotelId
     * @param paramRsvNo
     * @param paramRoomNo
     */

    public static void rsvRoomChange(int hotelId, String paramRsvNo, String paramRoomNo)
    {
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        LogicOwnerRsvRoomChange logicRoom = new LogicOwnerRsvRoomChange();
        String paramSeq = paramRoomNo;
        boolean errorFlag = false;
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        try
        {
            if ( (paramSeq != null) && (paramSeq.equals( "" ) == false) )
            {
                paramSeq = Integer.toString( getSeq( hotelId, paramSeq ) ); // ホスト通信用部屋名称をハピホテの部屋番号に変換する
                // "0"で返ってきた場合は、hh_hotel_room_more の登録異常ということなので処理をしない。

                if ( paramSeq.equals( "0" ) == false )
                {
                    if ( checkChangeRoom( hotelId, paramRsvNo, paramSeq ) == false )
                    {
                        errorFlag = true; // 変更先の部屋が割当済み
                    }
                    if ( (paramRsvNo.compareTo( "" ) != 0) && (Integer.parseInt( paramSeq ) > 0) )
                    {
                        // フォームにセット
                        frm.setSelHotelId( hotelId );
                        frm.setRsvNo( paramRsvNo );

                        // 予約データ抽出
                        logic.setFrm( frm );
                        // 実績データ取得
                        logic.getData( 2 );

                        // 連泊の際は、他の宿泊日もチェック
                        if ( StringUtils.isNotBlank( frm.getRsvNoMain() ) )
                        {
                            if ( checkChangeRoomConsecutive( hotelId, frm.getRsvNoMain(), paramSeq ) == false )
                            {
                                errorFlag = true;// 変更先の部屋が割当済み
                            }
                        }

                        frm.setMode( "" ); // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
                        frm.setErrMsg( "" );
                        frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );
                        frm.setMode( ReserveCommon.MODE_UPD );
                        frm.setOrgRsvSeq( frm.getSeq() );
                        if ( errorFlag == false )
                        {
                            frm.setSeq( Integer.parseInt( paramSeq ) );
                            frm.setRoomHold( 0 );
                        }
                        else
                        // 割当済み
                        {
                            frm.setRoomHold( Integer.parseInt( paramSeq ) ); // 変更先の部屋が割当済みの場合は確保部屋のみにセットする。
                        }
                        frm.setOrgRsvDate( frm.getRsvDate() );
                        frm.setPlanType( logic.getFrm().getPlanType() );

                        // 来店待ちor来店済みのステータスのみ有効
                        if ( frm.getStatus() == ReserveCommon.RSV_STATUS_UKETUKE || frm.getStatus() == ReserveCommon.RSV_STATUS_ZUMI )
                        {
                            logicRoom.setFrm( frm );
                            logicRoom.updReserve();
                        }
                    }
                    // 電文の送信部分
                    // IPアドレスが登録されている場合は、ホスト連動物件。予約があったことをホストに伝える
                    if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                    {
                        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                        // 5時以前の場合は前日とする
                        if ( 50000 > nowTime )
                        {
                            nowTime = nowTime + 240000;
                            nowDate = DateEdit.addDay( nowDate, -1 );
                        }
                        if ( frm.getRsvDate() == nowDate// 予約日付が当日の場合
                                || (frm.getRsvDate() == DateEdit.addDay( nowDate, 1 ) && frm.getEstTimeArrival() < nowTime) /* 予約日付が翌日で来店予定時刻が24時間以内の場合 */)
                        {
                            RsvList rl = new RsvList();
                            // 要確認
                            if ( rl.getData( hotelId, frm.getRsvDate(), frm.getRsvDate(), 0, paramRsvNo, 1 ) != false )
                            {
                                rl.sendToHost( hotelId );
                            }
                        }
                    }
                }
            }
        }
        catch ( Exception e )
        {
        }
    }

    /*****
     * 部屋変更
     * 
     * @param hotelId
     * @param request
     * @param response
     */
    public void rsvRoomChange(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        GenerateXmlHapiTouchRsvRoomChange gxRsvChangeRoom;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        LogicOwnerRsvRoomChange logicRoom = new LogicOwnerRsvRoomChange();
        String paramRsvNo = "";
        String paramSeq = "";
        String paramRoomNo = "";
        String paramCode = "";
        boolean isSyncReserve = ReserveCommon.isSyncReserve( hotelId ); // 予約連動物件の判断

        gxRsvChangeRoom = new GenerateXmlHapiTouchRsvRoomChange();

        paramRsvNo = request.getParameter( "rsvNo" );
        paramRoomNo = request.getParameter( "roomNo" );
        paramCode = request.getParameter( "code" );

        if ( paramCode != null && !paramCode.equals( "" ) )
        {
            isSyncReserve = false; // タッチPCからのアクセスなので、予約割当済み部屋変更はNG
        }

        // 予約番号のチェック
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        try
        {
            if ( (paramRoomNo != null) && (paramRoomNo.equals( "" ) == false) )
            {
                paramSeq = Integer.toString( getSeq( hotelId, paramRoomNo ) );// ホスト通信用部屋名称をハピホテの部屋番号に変換する
                // タッチPCからの部屋名称は、hh_hotel_room_more.seq相当でくることもある
                // "0"で返ってきた場合は、hh_hotel_room_more の登録異常ということなのでパラメータエラーということ。

                if ( paramSeq.equals( "0" ) == false )
                {
                    if ( checkChangeRoom( hotelId, paramRsvNo, paramSeq ) == false )
                    {
                        errCode = HapiTouchErrorMessage.ERR_20807; // 変更先の部屋が割当済み
                    }
                    if ( errCode == 0 || isSyncReserve ) // 予約連動物件は変更先の部屋が割当済みでもOKとする。
                    {
                        if ( (paramRsvNo.compareTo( "" ) != 0) && (Integer.parseInt( paramSeq ) > 0) )
                        {
                            // フォームにセット
                            frm.setSelHotelId( hotelId );
                            frm.setRsvNo( paramRsvNo );

                            // 予約データ抽出
                            logic.setFrm( frm );
                            // 実績データ取得
                            logic.getData( 2 );

                            // 連泊の際は、他の宿泊日もチェック
                            if ( StringUtils.isNotBlank( frm.getRsvNoMain() ) )
                            {
                                if ( checkChangeRoomConsecutive( hotelId, frm.getRsvNoMain(), paramSeq ) == false )
                                {
                                    errCode = HapiTouchErrorMessage.ERR_20807;// 変更先の部屋が割当済み
                                }
                            }

                            if ( errCode == 0 || isSyncReserve )// 予約連動物件は変更先の部屋が割当済みでもOKとする。
                            {
                                frm.setMode( "" ); // 処理成功時はモードを空にする。(最終ページのため、モードは関係なく共通)
                                frm.setErrMsg( "" );
                                frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );
                                frm.setMode( ReserveCommon.MODE_UPD );
                                frm.setOrgRsvSeq( frm.getSeq() );
                                if ( errCode == 0 )
                                {
                                    frm.setSeq( Integer.parseInt( paramSeq ) );
                                    frm.setRoomHold( 0 );
                                }
                                else
                                {
                                    frm.setRoomHold( Integer.parseInt( paramSeq ) ); // 変更先の部屋が割当済みの場合は確保部屋のみにセットする。
                                }
                                frm.setOrgRsvDate( frm.getRsvDate() );
                                frm.setPlanType( logic.getFrm().getPlanType() );

                                // 来店待ちのステータスのみ有効
                                if ( frm.getStatus() == ReserveCommon.RSV_STATUS_UKETUKE )
                                {
                                    //
                                    logicRoom.setFrm( frm );
                                    logicRoom.updReserve();

                                    // エラーが無かったらOKとしてレスポンスをセット
                                    if ( frm.getErrMsg().trim().length() == 0 )
                                    {
                                        gxRsvChangeRoom.setRoomNo( paramSeq );
                                        gxRsvChangeRoom.setRoomName( this.getRoomName( hotelId, Integer.parseInt( paramSeq ) ) );
                                        gxRsvChangeRoom.setResult( "OK" );
                                        gxRsvChangeRoom.setStatusValue( frm.getStatus() );
                                        if ( frm.getStatus() == 1 )
                                        {
                                            gxRsvChangeRoom.setStatus( "来店待ち" );
                                        }
                                        else if ( frm.getStatus() == 2 )
                                        {
                                            gxRsvChangeRoom.setStatus( "来店" );
                                        }
                                        else if ( frm.getStatus() == 3 )
                                        {
                                            if ( frm.getNoShow() == 1 )
                                            {
                                                gxRsvChangeRoom.setStatus( "\\キャンセル" );
                                            }
                                            else
                                            {
                                                gxRsvChangeRoom.setStatus( "キャンセル" );
                                            }
                                        }
                                    }
                                    else
                                    {
                                        errCode = HapiTouchErrorMessage.ERR_20806;
                                        Logging.info( "[HapiTouchRsv.rsvRoomChange()]:" + frm.getErrMsg() );
                                    }
                                }
                                else
                                {
                                    if ( frm.getSeq() <= 0 )
                                    {
                                        // データなし
                                        errCode = HapiTouchErrorMessage.ERR_20802;
                                    }
                                    else
                                    {
                                        // ステータスがキャンセル以外
                                        errCode = HapiTouchErrorMessage.ERR_20803;
                                    }
                                }
                            }
                        }
                        else
                        {
                            errCode = HapiTouchErrorMessage.ERR_20801;
                        }
                    }
                }
                else
                {
                    errCode = HapiTouchErrorMessage.ERR_20801;
                }
            }
            else
            {
                errCode = HapiTouchErrorMessage.ERR_20801;
            }
            if ( errCode > 0 && !(isSyncReserve && errCode == HapiTouchErrorMessage.ERR_20807) )
            {
                gxRsvChangeRoom.setErrorCode( errCode );
                gxRsvChangeRoom.setResult( "NG" );
            }
            // 電文の送信部分
            if ( errCode == 0 || (isSyncReserve && errCode == HapiTouchErrorMessage.ERR_20807) ) // 変更先の部屋が他の予約割当済みでも確保部屋が変更になったのでホストに知らせます。
            {
                // IPアドレスが登録されている場合は、ホスト連動物件。予約があったことをホストに伝える
                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                {
                    int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                    int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                    // 5時以前の場合は前日とする
                    if ( 50000 > nowTime )
                    {
                        nowTime = nowTime + 240000;
                        nowDate = DateEdit.addDay( nowDate, -1 );
                    }
                    if ( frm.getRsvDate() == nowDate// 予約日付が当日の場合
                            || (frm.getRsvDate() == DateEdit.addDay( nowDate, 1 ) && frm.getEstTimeArrival() < nowTime) /* 予約日付が翌日で来店予定時刻が24時間以内の場合 */)
                    {
                        RsvList rl = new RsvList();
                        // 要確認
                        if ( rl.getData( hotelId, frm.getRsvDate(), frm.getRsvDate(), 0, paramRsvNo, 1 ) != false )
                        {
                            rl.sendToHost( hotelId );
                        }
                    }
                }
            }
            if ( errCode != 0 )
            // エラーが発生していた場合にはエラー履歴に書き込む
            {
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( frm.getHotelNm() );
                daeh.setRoomName( paramRoomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( frm.getCiSeq() );
                daeh.setUserId( frm.getUserId() );
                daeh.insertData();
            }

            // XMLの出力
            String xmlOut = gxRsvChangeRoom.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.roomCange xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
        }
    }

    /**
     * 部屋名称取得
     * 
     * @param id
     * @param seq
     * @return
     */
    private String getRoomName(int id, int seq)
    {
        String roomName = "";
        boolean ret = false;
        DataHotelRoomMore dhrm;
        dhrm = new DataHotelRoomMore();

        ret = dhrm.getData( id, seq );
        if ( ret != false )
        {
            roomName = dhrm.getRoomNameHost();
        }

        return roomName;
    }

    /**
     * ランク名称取得
     * 
     * @param id
     * @param seq
     * @return
     */
    private String getRankName(int id, String rsvNo)
    {
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String rankName = "";
        String query;
        try
        {

            query = "SELECT IFNULL(CASE pln.room_select_kind WHEN 3 THEN '指定無し' WHEN 2 THEN '部屋指定' ELSE rank.rank_name END, '') AS rank_name";
            query += " FROM newRsvDB.hh_rsv_reserve rsv INNER JOIN newRsvDB.hh_rsv_plan pln";
            query += " ON rsv.id=pln.id AND rsv.plan_id=pln.plan_id AND rsv.plan_sub_id=pln.plan_sub_id";
            query += " LEFT JOIN hh_hotel_room_more rm ON rm.id = rsv.id AND rm.seq = rsv.seq";
            query += " LEFT JOIN hh_hotel_roomrank rank ON rank.id = rm.id AND rank.room_rank = rm.room_rank";
            query += " WHERE rsv.id=? AND rsv.reserve_no=?";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, rsvNo );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    rankName = result.getString( "rank_name" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsv.getRankName] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return rankName;

    }

    /**
     * ホテルチェックインデータ取得
     * 
     * @param 予約No
     * @return 処理結果(チェックインコード)
     */
    private int getCheckInCode(int id, String rsv_no)
    {
        String status = "0,1,4";
        return getCheckInCode( id, rsv_no, status );
    }

    private int getCheckInCode(int id, String rsv_no, String status)
    {
        int ciCode = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT seq FROM hh_hotel_ci AS m " +
                " WHERE id = ? " +
                " AND rsv_no = ?" +
                " AND ci_status IN (" + status + ") " +
                " AND " +
                " NOT EXISTS (  " + // 同じseqで最大値のもののみを対象とする
                "  SELECT 1 " +
                " FROM hh_hotel_ci AS s " +
                "  WHERE m.id = s.id " +
                "  AND m.seq = s.seq " +
                "   AND m.sub_seq < s.sub_seq " +
                "  ) " +
                "  ORDER BY seq DESC " +
                "  LIMIT 0,1 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, rsv_no );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ciCode = result.getInt( "seq" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsv.getCheckInCode] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ciCode);
    }

    /**
     * 部屋番号取得
     * 
     * @param id
     * @param seq
     * @return
     */
    private static int getSeq(int id, String roomName)
    {
        int seq = 0;
        boolean ret = false;
        DataHotelRoomMore dhrm;
        dhrm = new DataHotelRoomMore();

        ret = dhrm.getData( id, roomName );
        if ( ret != false )
        {
            seq = dhrm.getSeq();
        }

        if ( seq == 0 && CheckString.numCheck( roomName ) )
        {
            seq = Integer.parseInt( roomName );
        }
        return seq;
    }

    /****
     * 予約の一覧を取得する
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void rsvDailyCount(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvList dsp0; // form
        LogicOwnerRsvList lgLPC; // logic
        ServletOutputStream stream = null;
        Boolean ret = false;
        GenerateXmlHapiTouchRsvDailyCount gxRsvDaily;
        GenerateXmlHapiTouchRsvDailyCountSub gxRsvDailySub;

        String paramStartDate;
        String paramEndDate;

        dsp0 = new FormOwnerRsvList();
        lgLPC = new LogicOwnerRsvList();
        gxRsvDaily = new GenerateXmlHapiTouchRsvDailyCount();

        paramStartDate = request.getParameter( "startDate" );
        paramEndDate = request.getParameter( "endDate" );

        // 開始日のチェック
        if ( (paramStartDate == null) || (paramStartDate.equals( "" ) != false) || (CheckString.numCheck( paramStartDate ) == false) )
        {
            paramStartDate = "0";
        }
        // 終了日のチェック
        if ( (paramEndDate == null) || (paramEndDate.equals( "" ) != false) || (CheckString.numCheck( paramEndDate ) == false) )
        {
            paramEndDate = "0";
        }

        // レスポンスをセット
        try
        {
            stream = response.getOutputStream();

            // ホテルID取得
            dsp0.setSelHotelID( hotelId );
            dsp0.setDateFrom( DateEdit.getDate( 1 ) ); // システム日付
            dsp0.setDateTo( DateEdit.getDate( 1 ) ); // システム日付

            lgLPC.setDateFrom( paramStartDate );
            lgLPC.setDateTo( paramEndDate );
            lgLPC.setHotelId( hotelId );
            ret = lgLPC.getDailyCount( dsp0, RSV_KIND );
            if ( ret != false )
            {
                int i = 0;
                for( i = 0 ; i < lgLPC.getRsvDateValList().size() ; i++ )
                {
                    gxRsvDailySub = new GenerateXmlHapiTouchRsvDailyCountSub();
                    gxRsvDailySub.setRsvDateValue( lgLPC.getRsvDateValList().get( i ) );
                    gxRsvDailySub.setRsvCount( lgLPC.getRsvCountList().get( i ) );
                    gxRsvDaily.addData( gxRsvDailySub );
                }
            }
            else
            {
                gxRsvDaily.setErrorCode( 0 );
            }

            // XMLの出力
            String xmlOut = gxRsvDaily.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvDailyCount xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvData]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvData]Exception:" + e.toString() );
                }
            }

        }
    }

    /**
     * 予約未割り当て部屋を取得
     * 
     * @param なし
     * @return なし
     */
    private void getSeqList(FormReserveSheetPC frm)
    {
        String seqTbl = "";
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        PreparedStatement prestateR = null;
        ResultSet result = null;
        ResultSet resultR = null;

        ArrayList<Integer> seqList = new ArrayList<Integer>();
        ArrayList<Integer> seqIds = new ArrayList<Integer>();

        try
        {
            // すでに予約割り当て済みの部屋番号を取得する。
            query = "SELECT seq FROM newRsvDB.hh_rsv_room_remainder" +
                    " WHERE id = ? AND cal_date = ? AND " +
                    " ( stay_status <> 1 " +
                    "   OR rest_status <> 1 " +
                    "   OR (stay_reserve_no <> '' AND stay_reserve_no <> ?) " +
                    "   OR (rest_reserve_no <> '' AND rest_reserve_no <> ?) " +
                    "   OR (stay_reserve_temp_no <> 0 AND stay_reserve_limit_day * 1000000 + stay_reserve_limit_time > ?) " +
                    "   OR (rest_reserve_temp_no <> 0 AND rest_reserve_limit_day * 1000000 + rest_reserve_limit_time > ?) " +
                    " ) ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, frm.getRsvDate() );
            prestate.setString( 3, frm.getRsvNo() );
            prestate.setString( 4, frm.getRsvNo() );
            prestate.setString( 5, DateEdit.getDate( 2 ) + DateEdit.getTime( 1 ) );
            prestate.setString( 6, DateEdit.getDate( 2 ) + DateEdit.getTime( 1 ) );
            result = prestate.executeQuery();

            while( result.next() )
            {
                seqIds.add( result.getInt( "seq" ) );
            }
            result.close();
            prestate.clearParameters();

            // 連泊の場合は、連泊日全てについて実行する
            if ( (frm.getRsvNoMain() != null) && (frm.getRsvNoMain().equals( "" ) == false) )
            {
                String que = "SELECT * FROM  newRsvDB.hh_rsv_reserve" +
                        " WHERE id = ? AND reserve_no_main = ? " +
                        " AND SUBSTRING(reserve_no,INSTR(reserve_no,'-') + 1) != reserve_no_main ORDER BY reserve_date";

                prestateR = connection.prepareStatement( que );
                prestateR.setInt( 1, frm.getSelHotelId() );
                prestateR.setString( 2, frm.getRsvNoMain() );
                resultR = prestateR.executeQuery();
                while( resultR.next() != false )
                {
                    prestate.setInt( 1, resultR.getInt( "id" ) );
                    prestate.setInt( 2, resultR.getInt( "reserve_date" ) );
                    prestate.setString( 3, resultR.getString( "reserve_no" ) );
                    prestate.setString( 4, resultR.getString( "reserve_no" ) );
                    prestate.setString( 5, DateEdit.getDate( 2 ) + DateEdit.getTime( 1 ) );
                    prestate.setString( 6, DateEdit.getDate( 2 ) + DateEdit.getTime( 1 ) );
                    result = prestate.executeQuery();

                    while( result.next() )
                    {
                        seqIds.add( result.getInt( "seq" ) );
                    }
                    result.close();
                    prestate.clearParameters();
                }

            }
            prestate.close();

            // 重複削除
            ArrayList<Integer> seqIdsTmp = new ArrayList<Integer>();
            for( int id : seqIds )
            {
                if ( !seqIdsTmp.contains( id ) )
                {
                    seqIdsTmp.add( id );
                }
            }
            // WHERE句作成
            for( int i = 0 ; i < seqIdsTmp.size() ; i++ )
            {
                seqTbl += " AND seq NOT IN (" + String.valueOf( seqIdsTmp.get( i ) ) + ")";
            }

            // 利用済みの部屋番号以外の部屋を取得する。
            query = "SELECT seq FROM hh_hotel_room_more WHERE id = ? AND disp_flag = 1 AND seq <>0";
            if ( !seqTbl.equals( "" ) )
            {
                query += seqTbl;
            }
            query = query + " ORDER BY seq";

            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );

            result = prestate.executeQuery();
            while( result.next() )
            {
                seqList.add( result.getInt( "seq" ) );
            }
            frm.setSeqList( seqList );
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsv.getSeqList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( resultR );
            DBConnection.releaseResources( prestateR );
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 部屋変更の際、変更先部屋が予約割り当て済みかどうかをチェックする
     * 
     * @param hotelId ホテルID
     * @param rsvNo 予約No
     * @param rsvDate 予約日
     * @param paramSeq 変更先部屋
     * @return なし
     */
    private static boolean checkChangeRoom(int hotelId, String RsvNo, String paramSeq)
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        try
        {
            // すでに予約割り当て済みの部屋番号を取得する。
            query = "SELECT stay_status,stay_reserve_no,stay_reserve_temp_no,stay_reserve_limit_day,stay_reserve_limit_time" +
                    ",rest_status,rest_reserve_no,rest_reserve_temp_no,rest_reserve_limit_day,rest_reserve_limit_time,cal_date FROM newRsvDB.hh_rsv_room_remainder" +
                    " WHERE id = ? AND cal_date = (SELECT reserve_date FROM newRsvDB.hh_rsv_reserve WHERE id = ? AND reserve_no = ?) AND seq= ?";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, hotelId );
            prestate.setString( 3, RsvNo );
            prestate.setString( 4, paramSeq );
            result = prestate.executeQuery();

            if ( result.next() )
            {
                // 当人の予約NOがはいっていれば問題なし
                if ( result.getString( "stay_reserve_no" ).equals( RsvNo ) || result.getString( "rest_reserve_no" ).equals( RsvNo ) )
                {
                    ret = true;
                }
                // 宿泊、休憩、どちらかに予約番号が入っていればfale
                else if ( !result.getString( "stay_reserve_no" ).equals( "" ) || !result.getString( "rest_reserve_no" ).equals( "" ) )
                {
                    // Logging.info( "[HapiTouchRsv.checkChangeRoom] stay_reserve_no=" + result.getString( "stay_reserve_no" ) );
                    ret = false;
                }
                // もしくは、どちらかがステータス1以外だったらfalse
                else if ( result.getInt( "stay_status" ) != 1 || result.getInt( "rest_status" ) != 1 )
                {
                    // Logging.info( "[HapiTouchRsv.checkChangeRoom] stay_status=" + result.getInt( "stay_status" ) );
                    ret = false;
                }
                // もしくは、宿泊の仮予約の期限前だったらfalse
                else if ( result.getInt( "stay_reserve_temp_no" ) != 0
                        && (double)result.getInt( "stay_reserve_limit_day" ) * 1000000 + (double)result.getInt( "stay_reserve_limit_time" ) > Double.parseDouble( DateEdit.getDate( 2 ) ) * 1000000 + Double.parseDouble( DateEdit.getTime( 1 ) ) )
                {
                    // Logging.info( "[HapiTouchRsv.checkChangeRoom] stay_reserve_limit_day=" + (double)result.getInt( "stay_reserve_limit_day" ) * 1000000 + (double)result.getInt( "stay_reserve_limit_time" ) );

                    ret = false;
                }
                // もしくは休憩の仮予約の期限前だったらfalse
                else if ( result.getInt( "rest_reserve_temp_no" ) != 0
                        && (double)result.getInt( "rest_reserve_limit_day" ) * 1000000 + (double)result.getInt( "rest_reserve_limit_time" ) > Double.parseDouble( DateEdit.getDate( 2 ) ) * 1000000 + Double.parseDouble( DateEdit.getTime( 1 ) ) )
                {
                    ret = false;
                }
                else
                {
                    ret = true;
                }

                /*
                 * if ( ret )
                 * {
                 * TimeCommon timeCommon = new TimeCommon();
                 * ret = timeCommon.isRsvChangeRoom( hotelId, result.getInt( "cal_date" ), RsvNo, Integer.parseInt( paramSeq ) );
                 * }
                 */
            }
            else
            {
                // Logging.info( "[HapiTouchRsv.checkChangeRoom] query=" + query );
                ret = false;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsv.checkChangeRoom] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * 部屋変更の際、変更先部屋が予約割り当て済みかどうかをチェックする(連泊用)
     * 
     * @param hotelId ホテルID
     * @param rsvNo 予約No
     * @param rsvDate 予約日
     * @param paramSeq 変更先部屋
     * @return なし
     */
    private static boolean checkChangeRoomConsecutive(int hotelId, String RsvNoMain, String paramSeq)
    {

        boolean chkChange = true;
        Connection connection = null;
        PreparedStatement prestateR = null;
        String que = "SELECT * FROM newRsvDB.hh_rsv_reserve  WHERE id = ? AND reserve_no_main = ?"
                + " AND SUBSTRING(reserve_no,INSTR(reserve_no,'-') + 1) != reserve_no_main ORDER BY reserve_date";
        ResultSet resultR = null;
        try
        {
            connection = DBConnection.getConnection();
            prestateR = connection.prepareStatement( que );
            prestateR.setInt( 1, hotelId );
            prestateR.setString( 2, RsvNoMain );
            resultR = prestateR.executeQuery();

            if ( resultR != null )
            {
                while( resultR.next() != false )
                {
                    // チェック
                    if ( checkChangeRoom( resultR.getInt( "id" ), resultR.getString( "reserve_no" ), paramSeq ) == false )
                    {
                        chkChange = false;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.execRaiten] Exception=" + e.toString() );

        }
        finally
        {
            DBConnection.releaseResources( resultR, prestateR, connection );
        }
        return(chkChange);
    }
}
