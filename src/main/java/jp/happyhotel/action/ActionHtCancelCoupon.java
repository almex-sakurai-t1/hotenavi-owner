package jp.happyhotel.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelCoupon;
import jp.happyhotel.data.DataUserCoupon;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.others.GenerateXmlHapiTouchHtCancelCoupon;
import jp.happyhotel.touch.TouchCi;
import jp.happyhotel.touch.TouchUserCoupon;

/**
 * ハピホテアプリ（ハピホテタッチクーポン使用取消）
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */
public class ActionHtCancelCoupon extends BaseAction
{
    final int                   USED               = 1;
    final int                   COUPON_KIND_AUTO   = 0;
    final int                   COUPON_KIND_MANUAL = 1;
    final int                   COUPON_NO_USE      = 0;
    final int                   COUPON_USED1       = 1;
    final int                   COUPON_USED10      = 10;
    final int                   COUPON_USED100     = 100;
    final int                   TIMEOUT            = 5000;
    final int                   PORT_NO            = 7023;
    final String                HTTP               = "http://";
    final String                CLASS_NAME         = "hapiTouch.act?method=";
    final String                METHOD_DISCOUNT    = "HtDiscountCoupon";
    final int                   RESULT_OK          = 1;
    final int                   RESULT_NG          = 2;
    final int                   DISCOUNT_COUPON    = 1;                        // 割引の区分のみ
    private static final String CONTENT_TYPE       = "text/xml; charset=UTF-8";
    private static final String ENCODE             = "UTF-8";

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
        String paramId = "";
        String paramSeq = "";
        String paramCouponSeq = "";
        String paramUsed = "";
        TouchCi tc = new TouchCi();
        TouchUserCoupon tuc = new TouchUserCoupon();
        int errorCode = 0;
        GenerateXmlHapiTouchHtCancelCoupon gxTouch = null;
        ServletOutputStream stream = null;
        HotelCi hc = new HotelCi();

        try
        {
            paramId = (String)request.getAttribute( "HOTEL_ID" );
            paramSeq = request.getParameter( "seq" );
            paramCouponSeq = request.getParameter( "couponSeq" );
            paramUsed = request.getParameter( "used" );

            if ( paramSeq == null || paramSeq.equals( "" ) != false || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( paramCouponSeq == null || paramCouponSeq.equals( "" ) != false || CheckString.numCheck( paramCouponSeq ) == false )
            {
                paramCouponSeq = "0";
            }
            stream = response.getOutputStream();

            if ( Integer.parseInt( paramSeq ) > 0 && Integer.parseInt( paramId ) > 0 && Integer.parseInt( paramCouponSeq ) > 0 )
            {

                ret = hc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                if ( ret != false )
                {

                    DataUserCoupon duc = new DataUserCoupon();
                    duc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramCouponSeq ) );
                    duc.setUsedFlag( Integer.parseInt( paramUsed ) );
                    boolCoupon = duc.updateData( Integer.parseInt( paramId ), Integer.parseInt( paramCouponSeq ) );
                }
            }
            gxTouch = new GenerateXmlHapiTouchHtCancelCoupon();
            if ( boolCoupon != false )
            {
                gxTouch.setResult( "OK" );
            }
            else
            {
                gxTouch.setResult( "NG" );
            }
            gxTouch.setErrorCode( errorCode );

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
            Logging.error( "[ActionHtUseCoupon]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHtUseCoupon]Exception:" + e.toString() );
                }
            }
        }
    }

    private DataHotelCoupon getHotelCoupon(int id, int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataHotelCoupon dhc = new DataHotelCoupon();

        query = "SELECT * FROM hh_hotel_coupon A";
        query += " INNER JOIN hh_master_coupon B";
        query += " ON A.id=B.id AND A.seq = B.coupon_no AND B.service_flag=1";
        query += " WHERE A.id = ? AND B.seq=?";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    dhc.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHtUseCoupon.getHotelCoupon] Exception=" + e.toString() );
            return(dhc);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(dhc);

    }
}
