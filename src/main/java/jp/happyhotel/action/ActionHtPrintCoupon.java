package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApAutoCoupon;
import jp.happyhotel.data.DataApTouchCoupon;
import jp.happyhotel.data.DataApUserAutoCoupon;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.dto.DtoApCommon;
import jp.happyhotel.dto.DtoApCoupon;
import jp.happyhotel.dto.DtoApCouponInformation;
import jp.happyhotel.touch.PrintCoupon;
import jp.happyhotel.touch.TouchCi;
import jp.happyhotel.touch.TouchUserCoupon;

public class ActionHtPrintCoupon extends BaseAction
{
    final int                      USED               = 1;
    final int                      COUPON_KIND_AUTO   = 0;
    final int                      COUPON_KIND_MANUAL = 1;
    final int                      COUPON_NO_USE      = 0;
    final int                      COUPON_USED1       = 1;
    final int                      COUPON_USED10      = 10;
    final int                      COUPON_USED100     = 100;
    final int                      TIMEOUT            = 5000;
    final int                      HOTENAVI_PORT_NO   = 7023;
    final int                      HAPIHOTE_PORT_NO   = 7046;
    final String                   HTTP               = "http://";
    final String                   CLASS_NAME         = "hapiTouch.act?method=";
    final String                   METHOD_PRINT       = "HtPrintCoupon";
    final int                      RESULT_OK          = 1;
    final int                      RESULT_NG          = 2;
    final int                      PRINT_COUPON       = 2;                                // 印字の区分のみ
    private RequestDispatcher      requestDispatcher;
    private DtoApCommon            apCommon;
    private DtoApCoupon            apCoupon;
    private DtoApCouponInformation apCouponInfo;
    private final String           DISP_MESSAGE       = "内線にてフロントまでご連絡いただきクーポン番号をお伝え下さい。";

    /**
     * ハピホテタッチ
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        // XML出力
        boolean ret = false;
        boolean boolCoupon = false;
        boolean boolResultCoupon = false;
        String roomNo = "";
        String paramId = "";
        String paramSeq = "";
        String paramCouponNo = "";
        String paramCouponSeq = "";
        String userId = "";
        String connectUrl = "";
        String hapihoteIp = "";
        String printCouponNo = "";
        TouchCi tc = new TouchCi();
        TouchUserCoupon tuc = new TouchUserCoupon();
        DataHotelBasic dhb = new DataHotelBasic();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();
        int errorCode = 0;
        String errorMessage = "";
        String unavailableKind = "";
        String unavailableMessage = "";
        boolean boolUseable = false;
        String printText = "";
        int printDate = 0;
        int printTime = 0;
        PrintCoupon pc = new PrintCoupon();

        try
        {
            paramId = request.getParameter( "id" );
            paramSeq = request.getParameter( "seq" );
            paramCouponNo = request.getParameter( "couponNo" );
            paramCouponSeq = request.getParameter( "couponSeq" );

            if ( paramSeq == null || paramSeq.equals( "" ) != false || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( paramCouponNo == null || paramCouponNo.equals( "" ) != false || CheckString.numCheck( paramCouponNo ) == false )
            {
                paramCouponNo = "0";
            }
            if ( paramCouponSeq == null || paramCouponSeq.equals( "" ) != false || CheckString.numCheck( paramCouponSeq ) == false )
            {
                paramCouponSeq = "0";
            }

            dhb.getData( Integer.parseInt( paramId ) );

            // ホテルのフロントIPを取得
            hapihoteIp = HotelIp.getFrontIp( Integer.parseInt( paramId ) );

            if ( Integer.parseInt( paramSeq ) > 0 && Integer.parseInt( paramId ) > 0 && paramCouponNo.equals( "" ) == false )
            {

                ret = tc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                if ( tc.getTouchCi().getId() > 0 && tc.getTouchCi().getSeq() > 0 )
                {
                    userId = tc.getTouchCi().getUserId();
                    roomNo = tc.getTouchCi().getRoomNo();
                }
                if ( ret != false )
                {
                    dhrm.getData( Integer.parseInt( paramId ), roomNo );
                    if ( tc.getTouchCi().getCiStatus() == 0 )
                    {

                        boolCoupon = tuc.getCouponData( userId, Integer.parseInt( paramId ), Integer.parseInt( paramCouponNo ), Integer.parseInt( paramCouponSeq ) );
                        if ( boolCoupon != false )
                        {
                            // クーポン番号の発券
                            printCouponNo = getPrintCouponNp( tuc.getUserAutoCoupon(), tuc.getCoupon() );
                            // プリントテキスト
                            printText = tuc.getCoupon()[0].getPrintText();
                            printDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                            printTime = Integer.parseInt( DateEdit.getTime( 1 ) );

                            /** （1006）ハピホテ_申告制クーポン印字要求 **/
                            pc.setSeq( tc.getTouchCi().getSeq() );
                            pc.setRoomName( dhrm.getRoomNameHost() );
                            pc.setCouponNo( printCouponNo );
                            pc.setPrintDate( printDate );
                            pc.setPrintTime( printTime );
                            pc.setMemo( printText );
                            pc.sendToHost( hapihoteIp, TIMEOUT, HAPIHOTE_PORT_NO, paramId );

                            if ( pc.getResult() == RESULT_OK )
                            {
                                // 結果OK
                                boolResultCoupon = true;
                                boolUseable = true;
                                // クーポン使用に変更
                                tuc.getUserAutoCoupon()[0].setUsedFlag( USED );
                                tuc.getUserAutoCoupon()[0].setUsedDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                tuc.getUserAutoCoupon()[0].setUsedTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                ret = tuc.getUserAutoCoupon()[0].updateData( tuc.getUserAutoCoupon()[0].getId(),
                                        tuc.getUserAutoCoupon()[0].getCouponSeq(), tuc.getUserAutoCoupon()[0].getSeq() );
                                if ( ret != false )
                                {
                                    // タッチクーポンにデータを登録
                                    DataApTouchCoupon datc = new DataApTouchCoupon();
                                    ret = datc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                                    datc.setId( Integer.parseInt( paramId ) );
                                    datc.setSeq( Integer.parseInt( paramSeq ) );
                                    datc.setKind( 1 );
                                    datc.setCouponNo( Integer.parseInt( paramCouponSeq ) );
                                    datc.setUserSeq( tuc.getUserAutoCoupon()[0].getCouponSeq() );
                                    datc.insertData();
                                }
                            }
                            else
                            {
                                // 申告適用NG
                                boolResultCoupon = true;
                                boolUseable = false;
                                errorCode = pc.getErrorCode();
                                unavailableMessage = "クーポンをご利用になれませんでした。";
                            }
                        }
                        else
                        {
                            // クーポンなし
                            boolResultCoupon = true;
                            boolUseable = false;
                            unavailableMessage = "クーポンを取得できませんでした。";
                        }
                    }
                    else
                    {
                        // 部屋ステータスエラー
                        // 結果NGエラー画面へ
                        boolResultCoupon = true;
                        boolUseable = false;
                        unavailableMessage = "チェックアウト済みのためご使用になれません。";
                    }
                }
                else
                {
                    // 部屋ステータスエラー
                    // 結果NGエラー画面へ
                    boolResultCoupon = true;
                    boolUseable = false;
                    unavailableMessage = "チェックアウト済みのためご使用になれません。";
                }
            }

            // ホテルのクーポンがあるかどうかを確認
            apCommon = new DtoApCommon();
            // 共通設定
            apCommon.setId( Integer.parseInt( paramId ) );
            apCommon.setHotelName( dhb.getName() );
            apCommon.setSeq( Integer.parseInt( paramSeq ) );
            apCommon.setRoomNo( dhrm.getRoomNameHost() );

            apCoupon = new DtoApCoupon();
            apCoupon.setApCommon( apCommon );
            apCoupon.setErrorCode( errorCode );
            apCoupon.setErrorMessage( errorMessage );
            apCoupon.setExistsCoupon( boolCoupon );
            apCoupon.setResult( boolResultCoupon );// 結果
            apCoupon.setUnavailableKind( unavailableKind );
            apCoupon.setUnavailableMessage( unavailableMessage );
            apCoupon.setUseable( boolUseable );

            // 問合せフォーム
            request.setAttribute( "DtoApCommon", apCommon );
            request.setAttribute( "DtoApCoupon", apCoupon );

            requestDispatcher = request.getRequestDispatcher( "/phone/htap/CouponUse.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "HtPrintCoupon:" + exception );
        }
        finally
        {
        }
    }

    /**
     * クーポン情報セット
     * 
     * @param dauac
     * @param daac
     * @see 自動適用クーポンをDtoApCouponInformationにセット
     * @return
     */
    private String getPrintCouponNp(DataApUserAutoCoupon[] dauac, DataApAutoCoupon[] daac)
    {
        String couponNo = "";

        couponNo = String.format( "%02d", daac[0].getCouponKind() % 100 ) + "-" +
                String.format( "%03d", dauac[0].getCouponSeq() % 1000 ) + "-" +
                String.format( "%04d", dauac[0].getSeq() % 10000 );

        return couponNo;
    }
}
