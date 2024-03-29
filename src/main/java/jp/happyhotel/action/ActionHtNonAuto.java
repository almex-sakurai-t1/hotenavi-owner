package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataApTouchCoupon;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.dto.DtoApCommon;
import jp.happyhotel.dto.DtoApCouponList;
import jp.happyhotel.dto.DtoApCouponUnit;
import jp.happyhotel.dto.DtoApHotelCustomer;
import jp.happyhotel.dto.DtoApHotelCustomerData;
import jp.happyhotel.dto.DtoApInquiryForm;
import jp.happyhotel.dto.DtoApNonAuto;
import jp.happyhotel.dto.DtoApUserInfo;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.touch.TouchReception;

/**
 * ハピホテアプリ部屋番号選択待ち
 * 
 * @author T.Sakurai
 * @version 1.0 2016/11/07
 * 
 */

public class ActionHtNonAuto extends BaseAction
{
    final String                       URL_MATCHING = "happyhotel.jp";
    final String                       SP_URL       = "/phone/htap/";
    final String                       FILE_URL     = "NonAuto.jsp";

    private RequestDispatcher          requestDispatcher;
    private DtoApCommon                apCommon;
    private DtoApNonAuto               apNonAuto;
    private DtoApHotelCustomer         apHotelCustomer;
    private DtoApHotelCustomerData     apHotelCustomerData;
    private DtoApUserInfo              apUserInfo;
    private ArrayList<DtoApCouponUnit> apCouponUnitList;
    private DtoApCouponList            apCouponList;
    private DtoApInquiryForm           apInquiry;

    /**
     * ハピホテタッチ
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int carrierFlag;
        boolean ret = true;
        boolean boolConnected = true; // ホスト連動
        boolean boolHotelCustom = false;// 顧客対応状況
        boolean boolMemberCheckIn = false; // メンバーチェックイン結果
        boolean boolMemberAccept = false;// メンバー情報受付結果
        boolean boolMemberInfo = false;// メンバー情報取得結果
        boolean boolUseableMile = false;// マイル使用はタッチ端末ではさせない
        boolean boolCouponAvailable = false;// クーポン利用可能フラグ
        int nonrefundableFlag = 0; // 0:返金OK、1:返金不可、2:クレジット前受のみ返金不可
        boolean boolMemberCardIssued = false; // メンバーカード発行有無

        String customId = "";
        String unavailableMessage = "";
        int errorCode = 0;
        int point = 0;
        int point2 = 0;
        String customRank = "";
        String forwardUrl = "";
        String paramId;
        String paramUserId;
        int goodsCode = 0; // 商品コード
        int goodsPrice = 0; // 金額

        DataHotelBasic dhb = new DataHotelBasic();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();
        DataApHotelSetting dahs = new DataApHotelSetting();
        HotelCi hc = new HotelCi();
        DataApTouchCoupon datc = new DataApTouchCoupon();

        try
        {
            paramId = request.getParameter( "id" );
            paramUserId = request.getParameter( "user_id" );
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( paramUserId == null || paramUserId.equals( "" ) != false )
            {
                paramUserId = "";
            }

            carrierFlag = UserAgent.getUserAgentTypeFromTouch( request );
            // URLを判断
            if ( carrierFlag == DataMasterUseragent.CARRIER_SMARTPHONE )
            {
                forwardUrl = SP_URL + FILE_URL;
            }
            else if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
            {
                forwardUrl = "/i/htap/" + FILE_URL;
            }
            else if ( carrierFlag == DataMasterUseragent.CARRIER_AU )
            {
                forwardUrl = "/au/htap/" + FILE_URL;
            }
            else if ( carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK )
            {
                forwardUrl = "/y/htap/" + FILE_URL;
            }
            else
            {
                if ( UserAgent.getUserAgentTypeString( request ).equals( "ipa" ) ||
                        UserAgent.getUserAgentTypeString( request ).equals( "ada" ) )
                {
                    forwardUrl = SP_URL + FILE_URL;
                }
            }
            if ( ret != false )
            {
                // ホテル情報を取得
                ret = dhb.getData( Integer.parseInt( paramId ) );
            }
            if ( ret != false )
            {
                // ホテル設定を取得 // ホテル設定を取得
                ret = dahs.getData( Integer.parseInt( paramId ) );
            }

            if ( ret != false )
            {

                // 顧客を行っているかどうか
                if ( dahs.getCustomFlag() > 0 )
                {
                    boolHotelCustom = true;
                }
                //
                nonrefundableFlag = dahs.getNonrefundableFlag();

            }
            // 同ユーザーの同ホテルでの24時間以内のチェックインを調べる
            ret = hc.getCheckInBeforeData( Integer.parseInt( paramId ), paramUserId );

            if ( ret != false && !hc.getHotelCi().getRoomNo().equals( "" ) )
            {

                // 24時間以内のチェックインデータがあれば、htHome もしくは HtRsvFix に遷移します。
                dhrm.getData( Integer.parseInt( paramId ), hc.getHotelCi().getRoomNo() );

                // クーポンデータがあったらクーポン使用できないため、戻り値を逆にして取得
                boolCouponAvailable = !(datc.getData( Integer.parseInt( paramId ), hc.getHotelCi().getSeq() ));

                TouchReception touchReception = new TouchReception();

                // タッチ接続
                touchReception.getTouchReception( Integer.parseInt( paramId ), hc.getHotelCi().getSeq(), paramUserId, hc, dhrm.getRoomNameHost(), boolHotelCustom, nonrefundableFlag );

                boolUseableMile = touchReception.isBoolUseableMile();
                unavailableMessage = touchReception.getUnavailableMessage();

                boolMemberInfo = touchReception.isBoolMemberInfo();
                customId = touchReception.getCustomId();
                customRank = touchReception.getCustomRank();
                point = touchReception.getPoint();
                point2 = touchReception.getPoint2();
                boolMemberCheckIn = touchReception.isBoolMemberCheckIn();
                apHotelCustomerData = touchReception.getApHotelCustomerData();
                boolMemberAccept = touchReception.isBoolMemberAccept();
                apCouponUnitList = touchReception.getApCouponUnitList();
                boolMemberCardIssued = touchReception.isBoolMemberCardIssued();
                if ( errorCode == 0 && touchReception.getErrorCode() > 0 )
                {
                    errorCode = touchReception.getErrorCode();
                }
                goodsCode = touchReception.getGoodsCode();
                goodsPrice = touchReception.getGoodsPrice();
                Logging.info( "[ActionHtNonAuto.execute()]hc.getHotelCi().getSeq():" + hc.getHotelCi().getSeq()
                        + ",paramUserId :" + paramUserId
                        + ",hc.getHotelCi().getUserId() :" + hc.getHotelCi().getUserId()
                        + ",paramUserId :" + paramUserId
                        + ",dhrm.getRoomNameHost() :" + dhrm.getRoomNameHost()
                        + ",boolHotelCustom :" + boolHotelCustom
                        + ",nonrefundableFlag:" + nonrefundableFlag
                        + ", boolMemberInfo :" + boolMemberInfo
                        + ", customId :" + customId
                        );

            }
            apCommon = new DtoApCommon();
            apNonAuto = new DtoApNonAuto();
            apCouponList = new DtoApCouponList();
            apHotelCustomer = new DtoApHotelCustomer();
            apHotelCustomerData = new DtoApHotelCustomerData();
            apInquiry = new DtoApInquiryForm();
            apUserInfo = new DtoApUserInfo();

            // 共通設定
            apCommon.setHtCheckIn( ret );
            apCommon.setConnected( boolConnected );// ホスト接続：true
            apCommon.setId( Integer.parseInt( paramId ) );
            apCommon.setHotelName( dhb.getName() );
            apCommon.setRoomNo( dhrm.getRoomNameHost() );
            apCommon.setSeq( hc.getHotelCi().getSeq() );
            apCommon.setUseableMile( boolUseableMile );// マイル使用はfalse
            apCommon.setUnavailableMessage( unavailableMessage );// メッセージはない
            apCommon.setErrorCode( errorCode );
            apCommon.setCustomer( boolHotelCustom );

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

            apHotelCustomer.setApCommon( apCommon );

            // クーポン設定
            apCouponList.setApCommon( apCommon );
            apCouponList.setApCouponUnit( apCouponUnitList );

            apNonAuto.setApCommon( apCommon );
            apInquiry.setApCommon( apCommon );
            apHotelCustomer.setApCommon( apCommon );
            apHotelCustomerData.setApCommon( apCommon );
            apUserInfo.setApCommon( apCommon );

            request.setAttribute( "DtoApCommon", apCommon );
            request.setAttribute( "DtoApNonAuto", apNonAuto );
            request.setAttribute( "DtoApCouponList", apCouponList );
            request.setAttribute( "DtoApInquiryForm", apInquiry );
            request.setAttribute( "DtoApHotelCustomer", apHotelCustomer );
            request.setAttribute( "DtoApHotelCustomerData", apHotelCustomerData );
            request.setAttribute( "DtoApUserInfo", apUserInfo );
            request.setAttribute( "AVAILABLE_COUPON", Boolean.toString( boolCouponAvailable ) );

            Logging.info( "HtNonAuto forwardUrl" + forwardUrl );

            requestDispatcher = request.getRequestDispatcher( forwardUrl );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "HtNonAuto:" + exception );
        }
        finally
        {
        }
    }
}
