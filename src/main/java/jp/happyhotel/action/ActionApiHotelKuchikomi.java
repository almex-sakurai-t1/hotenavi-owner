package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.ConstantsHotel;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserHistory;
import jp.happyhotel.hotel.HotelKuchikomi;
import jp.happyhotel.others.GenerateXmlDetail;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlKuchikomi;
import jp.happyhotel.others.GenerateXmlKuchikomiKuchikomi;
import jp.happyhotel.others.GenerateXmlKuchikomiKuchikomiDetail;
import jp.happyhotel.user.UserLoginInfo;

/**
 * ホテルクチコミクラス（API）
 * 
 * @author S.Tashiro
 * @version 1.0 2011/05/01
 * 
 */

public class ActionApiHotelKuchikomi extends BaseAction
{

    /**
     * ホテルクチコミ情報（API）
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String paramId;
        String paramMethod = "";
        String paramPage = "";
        DataUserHistory duh;
        UserLoginInfo uli;
        HotelKuchikomi hotelKuchikomi;
        String global_ip;

        // XML出力
        boolean ret = false;
        GenerateXmlHeader header = new GenerateXmlHeader();
        hotelKuchikomi = new HotelKuchikomi();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );
            paramId = request.getParameter( "hotel_id" );
            paramPage = request.getParameter( "page" );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( paramPage == null || paramPage.equals( "" ) != false || CheckString.numCheck( paramPage ) == false )
            {
                paramPage = "0";
            }
            ret = hotelKuchikomi.getData( Integer.parseInt( paramId ), Integer.parseInt( paramPage ) );
            if ( ret != false )
            {
                // hh_user_historyに履歴を追加する
                if ( (uli.isMemberFlag() != false) && (uli.getUserInfo().getRegistStatus() == 9) )
                {

                    // ユーザー履歴に追加
                    duh = new DataUserHistory();
                    duh.setUserId( uli.getUserInfo().getUserId() );
                    duh.setId( Integer.parseInt( paramId ) );
                    duh.setDispDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    duh.setDispTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    duh.setDispIp( request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr() );
                    duh.setDispUserAgent( request.getHeader( "user-agent" ) );
                    duh.insertData();
                }
                else
                {
                    // 非ユーザー分履歴に追加（非ユーザーはキャリアごとに書き込み）
                    duh = new DataUserHistory();
                    duh.setUserId( ConstantsHotel.USER_HISTORY_USERID );
                    duh.setId( Integer.parseInt( paramId ) );
                    duh.setDispDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    duh.setDispTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    duh.setDispIp( request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr() );
                    duh.setDispUserAgent( request.getHeader( "user-agent" ) );
                    duh.insertData();
                }

                GenerateXmlKuchikomiKuchikomi addKuchikomiList = new GenerateXmlKuchikomiKuchikomi();
                addKuchikomiList.setCount( hotelKuchikomi.getAllCount() );
                addKuchikomiList.setAverage( Float.toString( hotelKuchikomi.getAverage() ) );

                // detailタグの作成
                if ( hotelKuchikomi.getPostDate() != null )
                {
                    for( int i = 0 ; i < hotelKuchikomi.getPostDate().length ; i++ )
                    {
                        GenerateXmlKuchikomiKuchikomiDetail detail = new GenerateXmlKuchikomiKuchikomiDetail();
                        detail.setPostDate( hotelKuchikomi.getPostDate()[i] );
                        detail.setBrowser( hotelKuchikomi.getBrowser()[i] );
                        detail.setName( hotelKuchikomi.getName()[i] );
                        detail.setSex( hotelKuchikomi.getSex()[i] );
                        detail.setCleanness( hotelKuchikomi.getCleanness()[i] );
                        detail.setWidth( hotelKuchikomi.getWidth()[i] );
                        detail.setEquip( hotelKuchikomi.getEquip()[i] );
                        detail.setService( hotelKuchikomi.getService()[i] );
                        detail.setCost( hotelKuchikomi.getCost()[i] );
                        detail.setPoint( hotelKuchikomi.getPoint()[i] );
                        detail.setMessage( hotelKuchikomi.getMessage()[i] );
                        detail.setVoteCount( hotelKuchikomi.getVoteCount()[i] );
                        detail.setVoteYes( hotelKuchikomi.getVoteCount()[i] );
                        if ( hotelKuchikomi.getReplyMessage()[i] != null &&
                                hotelKuchikomi.getReplyMessage()[i].equals( "" ) == false )
                        {
                            detail.setReply( "" );
                            detail.setReplyPostDate( hotelKuchikomi.getReplyPostDate()[i] );
                            detail.setReplyName( hotelKuchikomi.getReplyName()[i] );
                            detail.setReplyMessage( hotelKuchikomi.getReplyMessage()[i] );
                        }
                        addKuchikomiList.addKuchikomiDetail( detail );
                    }
                }

                GenerateXmlKuchikomi kuchikomi = new GenerateXmlKuchikomi();
                kuchikomi.setError( "" );
                kuchikomi.setErrorCode( 0 );
                kuchikomi.addKuchikomi( addKuchikomiList );

                // 検索結果ヘッダ作成
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( "クチコミ" );
                header.setCount( hotelKuchikomi.getCount() );
                // クチコミ詳細を追加
                header.setKuchikomi( kuchikomi );

            }
            else
            {
                GenerateXmlKuchikomi kuchikomi = new GenerateXmlKuchikomi();
                kuchikomi.setError( Constants.ERROR_MSG_API8 );
                kuchikomi.setErrorCode( Constants.ERROR_CODE_API8 );

                // 検索結果ヘッダ作成
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( "クチコミ" );
                header.setCount( hotelKuchikomi.getCount() );
                // クチコミ詳細を追加
                header.setKuchikomi( kuchikomi );
            }

            // 出力をヘッダーから
            String xmlOut = header.createXml();
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiHotelKuchikomi ]Exception:" + exception.toString() );

            // エラーを出力
            GenerateXmlDetail detail = new GenerateXmlDetail();
            detail.setError( Constants.ERROR_MSG_API10 );
            detail.setErrorCode( Constants.ERROR_CODE_API10 );

            // 検索結果ヘッダ作成
            header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            header.setMethod( paramMethod );
            header.setName( "クチコミ" );
            header.setCount( 0 );
            // ホテル詳細を追加
            header.setDetail( detail );

            String xmlOut = header.createXml();
            ServletOutputStream out = null;

            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiHotelKuchikomi response]Exception:" + e.toString() );
            }
        }
        finally
        {
        }
    }
}
