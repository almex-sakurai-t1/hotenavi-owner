package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApAutoCoupon;
import jp.happyhotel.data.DataApUserAutoCoupon;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelCoupon;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataUserCoupon;
import jp.happyhotel.dto.DtoApCommon;
import jp.happyhotel.dto.DtoApCoupon;
import jp.happyhotel.dto.DtoApCouponInformation;
import jp.happyhotel.touch.TouchCi;
import jp.happyhotel.touch.TouchUserCoupon;

/**
 * ハピホテアプリクーポン表示クラス
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtCoupon extends BaseAction
{
    private RequestDispatcher      requestDispatcher;
    private DtoApCommon            apCommon;
    private DtoApCoupon            apCoupon;
    private DtoApCouponInformation apCouponInfo;
    private final String           DISP_MESSAGE = "内線にてフロントまでご連絡いただきクーポン番号をお伝え下さい。";

    final int                      AUTO         = 0;                                // 自動適用
    final int                      MANUAL       = 1;                                // 申告制
    static final int               BENEFIT1     = 1;                                // 条件1
    static final int               BENEFIT2     = 10;                               // 条件2
    static final int               BENEFIT3     = 100;                              // 条件3

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
        String roomNo = "0";
        String paramId = "";
        String paramSeq = "";
        String paramCoupon = "";
        String userId = "";
        String paramKind = "";
        String paramUsed = "";
        TouchCi tc = new TouchCi();
        TouchUserCoupon tuc = new TouchUserCoupon();
        DataHotelBasic dhb = new DataHotelBasic();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();

        try
        {
            paramId = request.getParameter( "id" );
            paramSeq = request.getParameter( "seq" );
            paramCoupon = request.getParameter( "couponNo" );
            paramKind = request.getParameter( "kind" );
            paramUsed = request.getParameter( "used" );

            if ( paramSeq == null || paramSeq.equals( "" ) != false || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( paramKind == null || paramKind.equals( "" ) != false || CheckString.numCheck( paramKind ) == false )
            {
                paramKind = "0";
            }
            if ( paramCoupon == null || paramCoupon.equals( "" ) != false || CheckString.numCheck( paramCoupon ) == false )
            {
                paramCoupon = "0";
            }
            if ( paramUsed == null || paramUsed.equals( "" ) != false || CheckString.numCheck( paramUsed ) == false )
            {
                paramUsed = "0";
            }

            dhb.getData( Integer.parseInt( paramId ) );
            if ( Integer.parseInt( paramSeq ) > 0 && Integer.parseInt( paramId ) > 0 && paramCoupon.equals( "" ) == false )
            {

                ret = tc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                userId = tc.getTouchCi().getUserId();
                roomNo = tc.getTouchCi().getRoomNo();

                if ( ret != false )
                {
                    if ( tc.getTouchCi().getCiStatus() == 0 )
                    {
                        // 自動適用のクーポン区分だったら
                        if ( Integer.parseInt( paramKind ) == AUTO )
                        {
                            boolCoupon = tuc.getCouponData( userId, Integer.parseInt( paramId ), Integer.parseInt( paramCoupon ) );
                        }
                        // 申告制だったら
                        else
                        {
                            boolCoupon = tuc.getUserCoupon( Integer.parseInt( paramId ), Integer.parseInt( paramCoupon ), userId );
                        }
                    }
                    else
                    {
                        boolCoupon = false;
                    }
                }

            }
            dhrm.getData( Integer.parseInt( paramId ), tc.getTouchCi().getRoomNo() );

            // ホテルのクーポンがあるかどうかを確認
            apCommon = new DtoApCommon();
            apCouponInfo = new DtoApCouponInformation();
            // 共通設定
            apCommon.setId( Integer.parseInt( paramId ) );
            apCommon.setHotelName( dhb.getName() );
            apCommon.setSeq( Integer.parseInt( paramSeq ) );
            apCommon.setRoomNo( dhrm.getRoomNameHost() );

            // クーポン情報がある
            if ( boolCoupon != false )
            {
                // 自動適用クーポンだったら
                if ( Integer.parseInt( paramKind ) == AUTO )
                {
                    apCouponInfo = setUserAutoCoupon( tuc.getUserAutoCoupon(), tuc.getCoupon(), dhb );
                }
                else
                {
                    apCouponInfo = setUserCoupon( tuc.getUserCoupon(), tuc.getHotelCoupon(), dhb, Integer.parseInt( paramUsed ) );
                }
            }

            apCouponInfo.setApCommon( apCommon );
            // 問合せフォーム
            request.setAttribute( "DtoApCommon", apCommon );
            request.setAttribute( "DtoApCouponInformation", apCouponInfo );
            // クーポンが使える使えないの判別に使用する。
            request.setAttribute( "COUPON_FLAG", Boolean.toString( boolCoupon ) );

            requestDispatcher = request.getRequestDispatcher( "/phone/htap/CouponInformation.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "HtCoupon:" + exception );
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
     * @param dhb
     * @see 自動適用クーポンをDtoApCouponInformationにセット
     * @return
     */
    private DtoApCouponInformation setUserAutoCoupon(DataApUserAutoCoupon[] dauac, DataApAutoCoupon[] daac, DataHotelBasic dhb)
    {
        DtoApCouponInformation daci = new DtoApCouponInformation();
        String couponNo = "";

        couponNo = String.format( "%02d", daac[0].getCouponKind() % 100 ) + "-" +
                String.format( "%03d", dauac[0].getCouponSeq() % 1000 ) + "-" +
                String.format( "%04d", dauac[0].getSeq() % 10000 );

        daci.setDeclarationFlag( false );
        daci.setCouponNo( couponNo );
        daci.setPrintDate( dauac[0].getPrintDate() );
        daci.setStartDate( dauac[0].getStartDate() );
        daci.setEndDate( dauac[0].getEndDate() );
        daci.setBenefitText1( daac[0].getDispText() );
        daci.setAddress( dhb.getAddressAll() );
        daci.setTellNo( dhb.getTel1() );
        daci.setCouponKind( daac[0].getCouponKind() );// 1:割引、2:印字
        daci.setOver18Flag( dhb.getOver18Flag() );
        daci.setCompanyType( dhb.getCompanyType() );
        // 以下クーポン使用するボタンで必要な情報
        daci.setCouponSeq( dauac[0].getCouponSeq() );
        daci.setSeq( dauac[0].getSeq() );
        return daci;
    }

    /**
     * クーポン情報セット
     * 
     * @param duc
     * @param dhc
     * @param dhb
     * @see 申告制クーポンをDtoApCouponInformationにセット
     * @return
     */
    private DtoApCouponInformation setUserCoupon(DataUserCoupon duc, DataHotelCoupon dhc, DataHotelBasic dhb, int used)
    {
        DtoApCouponInformation daci = new DtoApCouponInformation();
        String couponNo = "";

        couponNo = String.format( "%03d", dhc.getAllSeq() % 1000 ) + "-" +
                String.format( "%04d", duc.getCouponNo() % 10000 );

        daci.setDeclarationFlag( true );
        daci.setDispMessage( DISP_MESSAGE );
        daci.setCouponNo( couponNo );
        daci.setPrintDate( duc.getPrintDate() );
        daci.setStartDate( duc.getStartDate() );
        daci.setEndDate( duc.getEndDate() );

        if ( used == BENEFIT1 )
        {
            daci.setBenefitText1( dhc.getBenefitText1() );
            daci.setBenefitCondition1( dhc.getBenefitCondition1() );
        }
        else if ( used == BENEFIT2 )
        {
            daci.setBenefitText2( dhc.getBenefitText2() );
            daci.setBenefitCondition2( dhc.getBenefitCondition2() );
        }
        else if ( used == BENEFIT3 )
        {
            daci.setBenefitText3( dhc.getBenefitText3() );
            daci.setBenefitCondition3( dhc.getBenefitCondition3() );
        }
        daci.setCommonCondition( dhc.getCommonCondition() );
        daci.setAddress( dhb.getAddressAll() );
        daci.setTellNo( dhb.getTel1() );
        daci.setCouponKind( 0 );
        daci.setOver18Flag( dhb.getOver18Flag() );
        daci.setCompanyType( dhb.getCompanyType() );

        return daci;

    }
}
