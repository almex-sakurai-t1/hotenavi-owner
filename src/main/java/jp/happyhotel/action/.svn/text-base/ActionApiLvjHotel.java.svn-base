package jp.happyhotel.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.ApiConstants;
import jp.happyhotel.common.BaseApiAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApiHogeLvjHotel;
import jp.happyhotel.data.DataApiLvjHotel;
import jp.happyhotel.data.DataLvjHotelBasic;
import jp.happyhotel.lvj.LogicLvjHotelBasic;

/**
 * ラブインジャパンホテル情報一覧API
 * 
 */
public class ActionApiLvjHotel extends BaseApiAction
{

    /** ホテルID */
    private Integer hotelId;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {

        try
        {
            getRequestParameters( request );
        }
        catch ( Exception e )
        {
            // ホテルIDが数値でない時
            outputErrorJon( response, ApiConstants.ERROR_CODE_API201, ApiConstants.ERROR_MSG_API201 );
            return;
        }

        if ( this.hotelId == null )
        {
            // ホテルIDパラメータが無い時
            outputErrorJon( response, ApiConstants.ERROR_CODE_API202, ApiConstants.ERROR_MSG_API202 );
            return;
        }

        // テスト用
        // if(this.hotelId == 999)
        // {
        // outputErrorJon( response, ApiConstants.ERROR_CODE_API203, ApiConstants.ERROR_MSG_API203 );
        // return;
        // }

        try
        {

            // ホテル詳細情報を取得
            DataApiHogeLvjHotel dhb = new DataApiHogeLvjHotel();
            if ( dhb.getData( hotelId ) == false )
            {
                // 203
                throw new Exception( "ホテル情報の取得に失敗しました。 [hotelId=" + this.hotelId + "]" );
            }
            if ( dhb.getCount() == 0 )
            {
                // ホテルIDがはいっていて0件の場合は、1件もないかもしれないので、更新を試みる。
                if ( hotelId != 0 )
                {
                    LogicLvjHotelBasic logic = new LogicLvjHotelBasic();
                    logic.updateData( hotelId );
                    if ( dhb.getData( hotelId ) == false )
                    {
                        // 203
                        throw new Exception( "ホテル情報の取得に失敗しました。 [hotelId=" + this.hotelId + "]" );
                    }
                }
            }

            DataApiHogeLvjHotel[] m_Basic = dhb.getApiData();
            List<DataApiHogeLvjHotel> l_Basic = new ArrayList<DataApiHogeLvjHotel>();
            for( int i = 0 ; i < m_Basic.length ; i++ )
            {
                l_Basic.add( m_Basic[i] );
            }
            DataApiLvjHotel dataApiHotel = new DataApiLvjHotel();
            dataApiHotel.setNumber_of_records( dhb.getCount() );
            dataApiHotel.setHotels( l_Basic );

            DataLvjHotelBasic dhb_tmp = new DataLvjHotelBasic();
            for( int i = 0 ; i < m_Basic.length ; i++ )
            {
                int hotel_id = m_Basic[i].getHotel_id();
                int seq = m_Basic[i].getSeq();
                if ( dhb_tmp.getData( hotel_id, seq ) == false )
                {
                    Logging.error( "[ActionApiLvjHotel]reflect_flg更新データの取得に失敗しました。 [hotelId=" + hotel_id + "]" );
                }
                else
                {
                    dhb_tmp.setReflectFlag( 1 );
                    dhb_tmp.setReflectUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dhb_tmp.setReflectUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    if ( dhb_tmp.updateData( hotel_id, seq ) == false )
                    {
                        Logging.error( "[ActionApiLvjHotel]reflect_flg更新に失敗しました。 [hotelId=" + hotel_id + "]" );
                    }
                }
            }

            outputJson( response, dataApiHotel );

        }
        catch ( Exception e )
        {
            outputErrorJon( response, ApiConstants.ERROR_CODE_API203, ApiConstants.ERROR_MSG_API203 );
        }

    }

    /**
     * リクエストパラメーターの取得
     * 
     * @param request
     */
    private void getRequestParameters(HttpServletRequest request) throws Exception
    {

        String hotelId = null;
        // ホテルID
        if ( request.getParameter( "hotel_id" ) != null )
        {
            hotelId = request.getParameter( "hotel_id" );
            if ( hotelId.matches( "\\d+" ) == false )
            {
                throw new Exception( "hotel_id:" + hotelId );
            }
            this.hotelId = Integer.parseInt( hotelId );
        }
    }
}
