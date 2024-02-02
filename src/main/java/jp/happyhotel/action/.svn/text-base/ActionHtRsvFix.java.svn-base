package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.dto.DtoApCommon;
import jp.happyhotel.dto.DtoApCouponList;
import jp.happyhotel.dto.DtoApCouponUnit;
import jp.happyhotel.dto.DtoApHotelCustomer;
import jp.happyhotel.dto.DtoApHotelCustomerData;
import jp.happyhotel.dto.DtoApInquiryForm;
import jp.happyhotel.dto.DtoApReserve;
import jp.happyhotel.dto.DtoApUserInfo;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.others.HapiTouchErrorMessage;
import jp.happyhotel.others.HapiTouchRsvSub;
import jp.happyhotel.owner.LogicOwnerRsvRoomChange;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.touch.HotelTerminal;
import jp.happyhotel.touch.RsvFix;
import jp.happyhotel.touch.RsvList;
import jp.happyhotel.touch.TerminalTouch;
import jp.happyhotel.touch.TouchCi;
import jp.happyhotel.touch.TouchReception;
import jp.happyhotel.user.UserBasicInfo;

import org.apache.commons.lang.StringUtils;

/**
 * ハピホテアプリチェックインクラス
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtRsvFix extends BaseAction
{

    final int                      TIMEOUT               = 10000;
    final String                   HTTP                  = "http://";
    final String                   CLASS_NAME            = "hapiTouch.act?method=";
    final String                   METHOD_RSV_FIX        = "HtRsvFix";
    final int                      RESULT_OK             = 1;
    final int                      RESULT_NG             = 2;
    final int                      HOTENAVI_PORT_NO      = 7023;
    final int                      HAPIHOTE_PORT_NO      = 7046;
    final int                      CI_STATUS_NOT_DISPLAY = 3;                      // ハピホテタッチ非表示
    final int                      CI_STATUS_EXT         = 4;                      // 外部予約のタッチ非表示
    private RequestDispatcher      requestDispatcher;
    private DtoApCommon            apCommon;
    private DtoApReserve           apReserve;
    private DtoApCouponList        apCouponList;
    private DtoApInquiryForm       apInquiry;
    private DtoApHotelCustomer     apHotelCustomer;
    private DtoApHotelCustomerData apHotelCustomerData;
    private DtoApUserInfo          apUserInfo;

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
        boolean boolConnected = false;
        boolean boolExistRsv = false;
        boolean boolGuide = false;
        boolean reTouch = false;

        int roomNo = 0;
        String userId = "";
        String hapihoteIp = "";
        String[] anotherRoomList = null;
        int anotherRoomCount = 0;
        String paramId;
        String paramSeq;
        String paramRsvNo = "";
        String touchRoom = "";

        String hotelName = "";
        String hotenaviId = "";

        DataUserBasic dub = new DataUserBasic();
        HotelTerminal ht = new HotelTerminal();
        TerminalTouch tt = new TerminalTouch();
        DataHotelBasic dhb = new DataHotelBasic();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();
        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();
        TouchCi tc = new TouchCi();
        HotelCi hc = new HotelCi();
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicOwnerRsvRoomChange logicRoom = new LogicOwnerRsvRoomChange();
        RsvFix rf = new RsvFix();
        DataApHotelSetting dahs = new DataApHotelSetting();

        /* タッチ受付と共通 */
        String customId = "";
        String unavailableMessage = "";
        String customRank = "";
        int point = 0;
        int point2 = 0;
        int errorCode = 0;
        boolean boolHotelCustom = false;// 顧客対応状況
        boolean boolMemberCheckIn = false; // メンバーチェックイン結果
        boolean boolMemberAccept = false;// メンバー情報受付結果
        boolean boolMemberInfo = false;// メンバー情報取得結果
        boolean boolUseableMile = true;// マイル使用可能フラグ
        int nonrefundableFlag = 0; // 0:返金OK、1:返金不可、2:クレジット前受のみ返金不可
        ArrayList<DtoApCouponUnit> apCouponUnitList = null;
        /* タッチ受付と共通 */

        boolean boolMemberCardIssued = false; // メンバーカード発行有無
        int goodsCode = 0; // 商品コード
        int goodsPrice = 0; // 金額

        try
        {

            paramId = request.getParameter( "id" );
            paramSeq = request.getParameter( "seq" );
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( paramSeq == null || paramSeq.equals( "" ) != false || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( request.getParameter( "reTouch" ) != null )
            {
                reTouch = request.getParameter( "reTouch" ).equals( "true" );
            }
            if ( request.getParameter( "rsvNo" ) != null )
            {
                paramRsvNo = request.getParameter( "rsvNo" );
            }

            frm = new FormReserveSheetPC();

            if ( Integer.parseInt( paramId ) > 0 && Integer.parseInt( paramSeq ) > 0 )
            {

                dhb.getData( Integer.parseInt( paramId ) );
                hotelName = dhb.getName();
                hotenaviId = dhb.getHotenaviId();

                // ホテルのフロントIPを取得
                hapihoteIp = HotelIp.getFrontIp( Integer.parseInt( paramId ) );
                ret = hc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                dhrm.getData( Integer.parseInt( paramId ), hc.getHotelCi().getRoomNo() );
                touchRoom = dhrm.getRoomNameHost();
                roomNo = dhrm.getRoomNo();

                // ホテル端末データ取得
                ht.getHotelTerminal( Integer.parseInt( paramId ), dhrm.getSeq() );

                // ホテル設定を取得
                dahs.getData( Integer.parseInt( paramId ) );

                userId = hc.getHotelCi().getUserId();

                dub.getData( userId );

                // 顧客を行っているかどうか
                if ( dahs.getCustomFlag() > 0 )
                {
                    boolHotelCustom = true;
                }
                nonrefundableFlag = dahs.getNonrefundableFlag();

                // 予約データを取得
                if ( StringUtils.isBlank( paramRsvNo ) )
                {
                    // パラメータ予約番号が入っていないとき
                    frm = htRsvSub.getReserveData( userId, Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                }
                else
                {
                    // パラメータ予約番号が入っているとき
                    frm = htRsvSub.getReserveData( userId, Integer.parseInt( paramId ), paramRsvNo );
                }
                frm.setLoginUserId( frm.getUserId() );
                // 予約番号が取得できていればOKとみなす。
                if ( frm.getRsvNo().equals( "" ) == false )
                {

                    boolExistRsv = true;
                    boolGuide = true;

                    // フロントでタッチした場合は、予約で抑えられる部屋の中で、予約の入っていない部屋を取得する
                    // 部屋端末でタッチした場合は、その部屋を受け渡し、プランの対象でなければ対象とならなくなる

                    anotherRoomList = htRsvSub.getAnotherRoom( Integer.parseInt( paramId ), frm.getSelPlanId(), frm.getSelPlanSubId(), frm.getRsvDate(), touchRoom, frm.getRsvNo() );
                    anotherRoomCount = anotherRoomList.length;

                    // フロントの場合は、予約部屋で取得しなおす。
                    if ( touchRoom.equals( "" ) )
                    {
                        dhrm.getData( Integer.parseInt( paramId ), frm.getRoomHold() == 0 ? frm.getSeq() : frm.getRoomHold() );
                    }

                    // IPアドレスがあればホストへ対してチェックインデータを送信
                    if ( hapihoteIp.equals( "" ) == false )
                    {
                        // 接続設定をtrueへ
                        boolConnected = true;

                        /** （1004）ハピホテ_ハピホテタッチ予約来店要求 **/
                        rf.setSeq( hc.getHotelCi().getSeq() );
                        rf.setRsvNo( frm.getRsvNo() );
                        rf.setRoomName( dhrm.getRoomNameHost() );
                        rf.setDispRsvNo( frm.getRsvNo().substring( frm.getRsvNo().length() - 6 ) );
                        rf.setRoomNameListLength( anotherRoomCount );
                        rf.setRoomNameList( anotherRoomList );
                        rf.setTouchRoomName( touchRoom );

                        if ( frm.getStatus() != ReserveCommon.RSV_STATUS_ZUMI ) // 来店済みの場合はホストへの通知をしない
                        {
                            /*                  */
                            /* チェックイン通知 */
                            /*                  */
                            rf.sendToHost( hapihoteIp, TIMEOUT, HAPIHOTE_PORT_NO, paramId );

                            if ( rf.getResult() == RESULT_OK )
                            {
                                // 予約誘導をOKにする
                                boolGuide = true;
                                // 予約来店処理で部屋番号データが取得できた場合
                                if ( rf.getGuidRoomName().trim().equals( "" ) == false )
                                {
                                    // タッチ時の部屋と誘導部屋が違う場合はデータを取り直す
                                    if ( touchRoom.trim().equals( rf.getGuidRoomName().trim() ) == false )
                                    {
                                        dhrm.getData( Integer.parseInt( paramId ), rf.getGuidRoomName().trim() );
                                        roomNo = dhrm.getRoomNo();
                                    }
                                }
                                hc.getHotelCi().setRoomNo( dhrm.getRoomNameHost() );

                                // 誘導した部屋のタッチデータに追加
                                tt.registData( Integer.parseInt( paramId ), hc, roomNo );
                            }
                            else
                            {
                                // 2015.03.21 エラーコードがセットされていなかったのでエラーコードをセット tahsiro
                                errorCode = rf.getErrorCode();
                                if ( errorCode == 0 )
                                {
                                    // 2015.03.21 ホストでエラーコードがセットされてなかったら、ホストへの接続が失敗したエラーをセット tashiro
                                    errorCode = HapiTouchErrorMessage.ERR_30302;
                                }

                                // 2015.03.19 ホストのエラーの場合はタッチを非表示にして更新する。
                                // 2015.03.19 エラー時はタッチPCにも残らないようにする。 tashiro
                                if ( reTouch )// 会計中で再タッチの場合には無効にしない
                                {
                                }
                                else
                                {
                                    // 予約誘導をNGにする
                                    boolGuide = false;
                                    if ( tc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) ) != false )
                                    {
                                        tc.getTouchCi().setCiStatus( CI_STATUS_NOT_DISPLAY );
                                        tc.getTouchCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                        tc.getTouchCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                        tc.getTouchCi().updateData( Integer.parseInt( paramId ), dhrm.getSeq() );
                                        tc.registHotelCi( tc.getTouchCi() );
                                    }
                                    else
                                    {
                                        hc.getHotelCi().setCiStatus( CI_STATUS_NOT_DISPLAY );
                                        hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                        hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                        hc.getHotelCi().updateData( Integer.parseInt( paramId ), dhrm.getSeq(), hc.getHotelCi().getSubSeq() );
                                    }
                                }
                            }
                        }
                        else
                        {
                            boolGuide = true;
                        }
                    }
                    // ホスト非連動物件またはホスト連動物件で誘導できた物件のみ来店処理を行う。
                    if ( boolConnected == false || boolGuide != false )
                    {
                        // 予約部屋とタッチ部屋が違ったら
                        if ( frm.getSeq() != dhrm.getSeq() && dhrm.getRoomNo() > 0 && frm.getStatus() == ReserveCommon.RSV_STATUS_UKETUKE )
                        {
                            frm.setOrgRsvSeq( frm.getSeq() );
                            frm.setSeq( roomNo );

                            logicRoom.setFrm( frm );
                            logicRoom.updReserve();
                        }

                        UserBasicInfo ubi = new UserBasicInfo();
                        int extUserFlag = 0;
                        if ( ubi.isLvjUser( userId ) )
                        {
                            extUserFlag = 1;
                        }

                        if ( tc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) ) != false )
                        {
                            // 予約来店のキャンセルをした場合は、ap_touch_ci は更新しなくなる。
                            tc.getTouchCi().setRsvNo( frm.getRsvNo() );
                            tc.getTouchCi().setExtUserFlag( extUserFlag );
                            if ( extUserFlag == 1 )
                            {
                                tc.getTouchCi().setCiStatus( CI_STATUS_EXT );
                            }
                            else
                            {
                                tc.getTouchCi().setCiStatus( 0 );
                            }
                            tc.getTouchCi().setRoomNo( dhrm.getRoomNameHost() );

                            if ( frm.getUsedMile() != 0 )
                            {
                                tc.getTouchCi().setUsePoint( frm.getUsedMile() );
                                tc.getTouchCi().setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                tc.getTouchCi().setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            }
                            tc.getTouchCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            tc.getTouchCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            tc.getTouchCi().updateData( Integer.parseInt( paramId ), tc.getTouchCi().getSeq() );
                            tc.registHotelCi( tc.getTouchCi() );
                            hc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) ); // hh_hotel_ci 更新後再読み込み
                        }
                        else
                        {
                            hc.getHotelCi().setRsvNo( frm.getRsvNo() );
                            hc.getHotelCi().setExtUserFlag( extUserFlag );
                            if ( extUserFlag == 1 )
                            {
                                hc.getHotelCi().setCiStatus( CI_STATUS_EXT );
                            }
                            if ( frm.getUsedMile() != 0 )
                            {
                                hc.getHotelCi().setUsePoint( frm.getUsedMile() );
                                hc.getHotelCi().setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                hc.getHotelCi().setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            }
                            hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

                            if ( hc.getHotelCi().getCiStatus() == 2 )
                            {
                                if ( extUserFlag == 1 )
                                {
                                    hc.getHotelCi().setCiStatus( CI_STATUS_EXT );
                                }
                                else
                                {
                                    hc.getHotelCi().setCiStatus( 0 );
                                }
                                hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                                hc.getHotelCi().insertData();
                            }
                            else
                            {
                                hc.getHotelCi().updateData( Integer.parseInt( paramId ), hc.getHotelCi().getSeq(), hc.getHotelCi().getSubSeq() );
                            }
                        }

                        if ( frm.getStatus() != ReserveCommon.RSV_STATUS_ZUMI )
                        {
                            // ▼来店確認
                            frm = htRsvSub.execRaiten( frm, frm.getStatus() );

                            // エラーあり時
                            if ( frm.getErrMsg().trim().length() != 0 )
                            {
                                boolGuide = false;
                                Logging.error( "ActionHtRsvFix:" + frm.getErrMsg(), "ActionHtRsvFix" );

                                // 2015.03.21 来店処理失敗のエラーコードをセット tashiro
                                errorCode = HapiTouchErrorMessage.ERR_30303;

                            }
                            else
                            {
                                // 予約マイルを付与(ハピホテ予約のときだけ)
                                if ( frm.getExtFlag() == 0 )
                                {
                                    errorCode = htRsvSub.setRsvPoint( hc );
                                }
                                boolGuide = true;
                            }
                        }
                    }

                }
                else
                {
                    // データを取得しなおす
                    dhrm.getData( Integer.parseInt( paramId ), roomNo );
                    errorCode = HapiTouchErrorMessage.ERR_30301;
                    boolExistRsv = false;
                }
            }

            if ( errorCode == 0 )
            {
                // IPアドレスが登録されている場合は、ホスト連動物件。予約データの最新状態をホストに伝える
                if ( !HotelIp.getFrontIp( Integer.parseInt( paramId ), 1 ).equals( "" ) )
                {
                    RsvList rl = new RsvList();
                    if ( rl.getData( Integer.parseInt( paramId ), 0, 0, 0, frm.getRsvNo(), 0 ) != false )
                    {
                        rl.sendToHost( Integer.parseInt( paramId ) );
                    }
                }
            }

            /** 予約来店と共通化 **/
            TouchReception touchReception = new TouchReception();
            // 来店処理できた場合のみ
            if ( boolGuide != false )
            {
                touchReception.getTouchReception( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ), userId, hc,
                        dhrm.getRoomNameHost(), boolHotelCustom, nonrefundableFlag );

                boolUseableMile = touchReception.isBoolUseableMile();
                unavailableMessage = touchReception.getUnavailableMessage();

                boolMemberInfo = touchReception.isBoolMemberInfo();
                customId = touchReception.getCustomId();

                customRank = touchReception.getCustomRank();
                point = touchReception.getPoint();
                point2 = touchReception.getPoint2();
                boolMemberCheckIn = touchReception.isBoolMemberCheckIn();
                errorCode = touchReception.getErrorCode();
                apHotelCustomerData = touchReception.getApHotelCustomerData();
                boolMemberAccept = touchReception.isBoolMemberAccept();
                apCouponUnitList = touchReception.getApCouponUnitList();

                boolMemberCardIssued = touchReception.isBoolMemberCardIssued();
                goodsCode = touchReception.getGoodsCode();
                goodsPrice = touchReception.getGoodsPrice();
                /** 予約来店と共通化 **/
            }
            if ( errorCode != 0 )
            {
                // エラー内容を登録
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( touchRoom );
                daeh.setRoomNo( roomNo );
                daeh.setId( Integer.parseInt( paramId ) );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                daeh.setUserId( userId );
                daeh.insertData();
            }

            apCommon = new DtoApCommon();
            apReserve = new DtoApReserve();
            apCouponList = new DtoApCouponList();
            apInquiry = new DtoApInquiryForm();
            apHotelCustomer = new DtoApHotelCustomer();
            apHotelCustomerData = new DtoApHotelCustomerData();
            apUserInfo = new DtoApUserInfo();

            // 共通設定
            apCommon.setHtCheckIn( ret );
            apCommon.setConnected( boolConnected );
            apCommon.setId( Integer.parseInt( paramId ) );
            apCommon.setHotelName( dhb.getName() );
            apCommon.setRoomNo( dhrm.getRoomNameHost() );
            apCommon.setSeq( hc.getHotelCi().getSeq() );
            apCommon.setUseableMile( boolUseableMile );
            apCommon.setUnavailableMessage( unavailableMessage );
            apCommon.setErrorCode( errorCode );
            apCommon.setCustomer( boolHotelCustom );

            // 予約
            apReserve.setApCommon( apCommon );
            apReserve.setExistsRsv( boolExistRsv );
            if ( boolConnected == false )
            {
                apReserve.setHostKind( 2 );
            }
            else
            {
                apReserve.setHostKind( 0 );
            }
            apReserve.setReserveNo( frm.getRsvNo() );
            apReserve.setRsvResult( boolGuide );

            // ホテル顧客
            apHotelCustomer.setResultMemberInfo( boolMemberInfo );
            apHotelCustomer.setResultMemberCard( boolMemberAccept );
            apHotelCustomer.setResultMemberCheckIn( boolMemberCheckIn );
            apHotelCustomer.setCustomId( customId );
            apHotelCustomer.setRank( customRank );
            apHotelCustomer.setPoint( point );
            apHotelCustomer.setPoint2( point2 );
            apHotelCustomer.setResultMemberCardIssued( boolMemberCardIssued );
            apHotelCustomer.setGoodsCode( goodsCode );
            apHotelCustomer.setGoodsPrice( goodsPrice );
            apHotelCustomer.setErrorCode( errorCode );

            // クーポン設定
            apCouponList.setApCommon( apCommon );
            apCouponList.setApCouponUnit( apCouponUnitList );

            apInquiry.setApCommon( apCommon );
            apHotelCustomer.setApCommon( apCommon );
            apHotelCustomerData.setApCommon( apCommon );
            apUserInfo.setApCommon( apCommon );

            request.setAttribute( "DtoApCommon", apCommon );
            request.setAttribute( "DtoApReserve", apReserve );

            request.setAttribute( "DtoApCouponList", apCouponList );
            request.setAttribute( "DtoApInquiryForm", apInquiry );
            request.setAttribute( "DtoApHotelCustomer", apHotelCustomer );
            request.setAttribute( "DtoApHotelCustomerData", apHotelCustomerData );
            request.setAttribute( "DtoApUserInfo", apUserInfo );

            requestDispatcher = request.getRequestDispatcher( "HappyHotelReserveRecept.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "HtRsvFix:" + exception );
        }
        finally
        {
        }
    }
}
