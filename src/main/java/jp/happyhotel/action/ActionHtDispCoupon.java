package jp.happyhotel.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApTouchCoupon;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelCoupon;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataUserCoupon;
import jp.happyhotel.dto.DtoApCouponInformation;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.others.GenerateXmlHapiTouchHtDispCoupon;
import jp.happyhotel.others.GenerateXmlHapiTouchHtDispCouponData;
import jp.happyhotel.touch.TouchUserCoupon;

/**
 * ハピホテアプリクーポン表示クラス
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtDispCoupon extends BaseAction
{
    private RequestDispatcher      requestDispatcher;
    private DtoApCouponInformation apCouponInfo;

    private static final String    CONTENT_TYPE = "text/xml; charset=UTF-8";
    private static final String    ENCODE       = "UTF-8";

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
        String roomNo = "";
        String paramId = "";
        String paramSeq = "";
        String userId = "";
        String paramKind = "";
        String paramUsed = "";
        HotelCi hc = new HotelCi();
        TouchUserCoupon tuc = new TouchUserCoupon();
        DataHotelBasic dhb = new DataHotelBasic();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();
        ServletOutputStream stream = null;
        int errorCode = 0;
        int usedFlag = 0;
        DataApTouchCoupon datc = new DataApTouchCoupon();

        try
        {
            paramId = (String)request.getAttribute( "HOTEL_ID" );
            paramSeq = request.getParameter( "seq" );
            datc = new DataApTouchCoupon();

            if ( paramSeq == null || paramSeq.equals( "" ) != false || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }

            // ホテル情報を取得
            dhb.getData( Integer.parseInt( paramId ) );
            if ( Integer.parseInt( paramSeq ) > 0 && Integer.parseInt( paramId ) > 0 )
            {

                ret = hc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                if ( ret != false )
                {
                    userId = hc.getHotelCi().getUserId();

                    // タッチからクーポンを使用したデータがあれば
                    ret = datc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                    if ( ret != false )
                    {
                        // 申告制だったら
                        boolCoupon = tuc.getDeclareCouponData( userId, Integer.parseInt( paramId ), datc.getCouponNo() );
                    }
                    else
                    {
                        if ( hc.getHotelCi().getCiStatus() == 0 )
                        {
                            // 申告制だったら
                            boolCoupon = tuc.getDeclareCouponData( userId, Integer.parseInt( paramId ) );
                        }
                    }
                }
            }

            // クーポン情報がある
            if ( boolCoupon != false )
            {
                apCouponInfo = setUserCoupon( tuc.getUserCoupon(), tuc.getHotelCoupon(), dhb );
            }

            GenerateXmlHapiTouchHtDispCoupon dispCoupon = new GenerateXmlHapiTouchHtDispCoupon();
            if ( errorCode == 0 )
            {
                dispCoupon.setResult( "OK" );
            }
            else
            {
                dispCoupon.setResult( "NG" );
            }

            dispCoupon.setErrorCode( errorCode );
            if ( boolCoupon != false )
            {
                dispCoupon.setCouponNo( apCouponInfo.getCouponNo() );
                dispCoupon.setCouponSeq( apCouponInfo.getCouponSeq() );

                // 特典1をセット
                if ( apCouponInfo.getBenefitText1().equals( "" ) == false )
                {
                    GenerateXmlHapiTouchHtDispCouponData data = new GenerateXmlHapiTouchHtDispCouponData();
                    data.setSubSeq( tuc.getUserCoupon().getSeq() );
                    data.setTitle( apCouponInfo.getBenefitText1() );
                    data.setCondition( apCouponInfo.getBenefitCondition1() );

                    // 特典1の使用フラグは1桁目なので10で割った余りが0より大きい
                    if ( tuc.getUserCoupon().getUsedFlag() % 10 > 0 )
                    {
                        usedFlag = 1;
                    }
                    else
                    {
                        usedFlag = 0;
                    }

                    // 特典1の使用フラグをセット
                    data.setUsedFlag( usedFlag );
                    dispCoupon.addCouponData( data );
                }
                // 特典2をセット
                if ( apCouponInfo.getBenefitText2().equals( "" ) == false )
                {
                    GenerateXmlHapiTouchHtDispCouponData data = new GenerateXmlHapiTouchHtDispCouponData();
                    data.setSubSeq( tuc.getUserCoupon().getSeq() );
                    data.setTitle( apCouponInfo.getBenefitText2() );
                    data.setCondition( apCouponInfo.getBenefitCondition2() );

                    // 特典2は2桁目なので、100で割った余りが9より大きい
                    if ( tuc.getUserCoupon().getUsedFlag() % 100 > 9 )
                    {
                        usedFlag = 1;
                    }
                    else
                    {
                        usedFlag = 0;
                    }

                    data.setUsedFlag( usedFlag );
                    dispCoupon.addCouponData( data );

                }
                // 特典3をセット
                if ( apCouponInfo.getBenefitText3().equals( "" ) == false )
                {
                    GenerateXmlHapiTouchHtDispCouponData data = new GenerateXmlHapiTouchHtDispCouponData();
                    data.setSubSeq( tuc.getUserCoupon().getSeq() );
                    data.setTitle( apCouponInfo.getBenefitText3() );
                    data.setCondition( apCouponInfo.getBenefitCondition3() );

                    // 特典3は3桁目なので、99より大きいければ使用済み
                    if ( tuc.getUserCoupon().getUsedFlag() > 99 )
                    {
                        usedFlag = 1;
                    }
                    else
                    {
                        usedFlag = 0;
                    }

                    data.setUsedFlag( usedFlag );
                    dispCoupon.addCouponData( data );
                }
            }
            // XMLの出力
            String xmlOut = dispCoupon.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            Logging.info( xmlOut );

            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception exception )
        {
            Logging.error( "HtCoupon:" + exception );
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
                    Logging.error( "[ActionHapiTouch modifyCi]Exception:" + e.toString() );
                }
            }
        }
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
    private DtoApCouponInformation setUserCoupon(DataUserCoupon duc, DataHotelCoupon dhc, DataHotelBasic dhb)
    {
        DtoApCouponInformation daci = new DtoApCouponInformation();
        String couponNo = "";

        couponNo = String.format( "%03d", dhc.getAllSeq() % 1000 ) + "-" +
                String.format( "%04d", duc.getCouponNo() % 10000 );

        daci.setDeclarationFlag( true );
        // クーポン表示用のクーポン番号をセット
        daci.setCouponNo( couponNo );
        // hh_user_couponの主キー（coupon_no）をセット
        daci.setCouponSeq( duc.getCouponNo() );
        daci.setPrintDate( duc.getPrintDate() );
        daci.setStartDate( duc.getStartDate() );
        daci.setEndDate( duc.getEndDate() );
        daci.setBenefitText1( dhc.getBenefitText1() );
        daci.setBenefitCondition1( dhc.getBenefitCondition1() );
        daci.setBenefitText2( dhc.getBenefitText2() );
        daci.setBenefitCondition2( dhc.getBenefitCondition2() );
        daci.setBenefitText3( dhc.getBenefitText3() );
        daci.setBenefitCondition3( dhc.getBenefitCondition3() );
        daci.setCommonCondition( dhc.getCommonCondition() );
        daci.setAddress( dhb.getAddressAll() );
        daci.setTellNo( dhb.getTel1() );
        daci.setCouponKind( 0 );
        daci.setOver18Flag( dhb.getOver18Flag() );
        daci.setCompanyType( dhb.getCompanyType() );

        return daci;
    }

}
