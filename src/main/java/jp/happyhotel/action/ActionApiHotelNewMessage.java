package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.others.GenerateXmlContents;
import jp.happyhotel.others.GenerateXmlContentsHotel;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.search.SearchHotelMessage;

/**
 *
 * 新着メッセージ表示クラス
 *
 * @author S.Tashiro
 * @version 1.0 2011/05/02
 */

public class ActionApiHotelNewMessage extends BaseAction
{

    final private static int DISP_MAX_NUMBER = 20;

    /**
     *
     *
     * @param request クライアントからサーバへのリクエスト
     * @param response サーバからクライアントへのレスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret;
        String paramLocalId;
        String paramPrefId;
        String paramPage;
        String paramMethod;
        String prefName = "全国";
        String name;
        String hotelName;
        String strDate;
        DataMasterPref dmp;
        SearchHotelMessage shm;

        dmp = new DataMasterPref();
        shm = new SearchHotelMessage();

        paramLocalId = request.getParameter( "local_id" );
        paramPrefId = request.getParameter( "pref_id" );
        paramPage = request.getParameter( "page" );
        paramMethod = request.getParameter( "method" );

        if ( paramPage == null || paramPage.equals( "" ) != false || CheckString.numCheck( paramPage ) == false )
        {
            paramPage = "0";
        }
        if ( paramLocalId == null || paramLocalId.equals( "" ) != false || CheckString.numCheck( paramLocalId ) == false )
        {
            paramLocalId = "0";
        }
        if ( paramPrefId == null || paramPrefId.equals( "" ) != false || CheckString.numCheck( paramPrefId ) == false )
        {
            paramPrefId = "0";
        }
        if ( paramMethod == null )
        {
            paramMethod = "";
        }

        try
        {
            // 地方ID、都道府県ID
            if ( paramPrefId.equals( "0" ) == false )
            {
                dmp.getData( Integer.parseInt( paramPrefId ) );
                prefName = dmp.getName();
                ret = shm.getHotelListByPref( null, DISP_MAX_NUMBER, Integer.parseInt( paramPage ), Integer.parseInt( paramPrefId ) ); // 都道府県ごとの最新情報
            }
            else
            {
                ret = shm.getHotelList( null, DISP_MAX_NUMBER, Integer.parseInt( paramPage ) ); // 全国の最新情報
            }

            if ( ret != false )
            {
                GenerateXmlContents contents = new GenerateXmlContents();
                contents.setError( "" );
                contents.setErrorCode( 0 );
                contents.setResultCount( shm.getCount() );

                // ホテルのPVデータをセット
                if ( shm.getCount() > 0 )
                {
                    for( int i = 0 ; i < shm.getCount() ; i++ )
                    {
                        name = "";

                        GenerateXmlContentsHotel hotel = new GenerateXmlContentsHotel();

                        // 日付
                        strDate = Integer.toString( shm.getHotelMessageDate()[i] / 10000 ) + "."
                                + String.format( "%1$02d", shm.getHotelMessageDate()[i] / 100 % 100 ) + "."
                                + String.format( "%1$02d", shm.getHotelMessageDate()[i] % 100 );
                        hotel.setDate( strDate );

                        // 名前をセットする
                        hotelName = shm.getHotelInfo()[i].getName();
                        hotel.setName( hotelName );

                        hotel.setAddress( shm.getHotelInfo()[i].getPrefName() + shm.getHotelInfo()[i].getAddress1() );
                        hotel.setId( shm.getHotelInfo()[i].getId() );

                        name = shm.getHotelMessage()[i];
                        hotel.setMessage( name );

                        contents.addHotel( hotel );
                    }
                }
                else
                {
                    contents.setError( Constants.ERROR_MSG_API6 );
                    contents.setErrorCode( Constants.ERROR_CODE_API6 );
                    contents.setResultCount( shm.getCount() );
                }

                GenerateXmlHeader header = new GenerateXmlHeader();
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( prefName );
                header.setCount( shm.getAllCount() );
                // クチコミ詳細を追加
                header.setContents( contents );

                // 出力をヘッダーから
                String xmlOut = header.createXml();
                ServletOutputStream out = null;

                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );

            }
            else
            {
                //
                // GenerateXmlContents contents = new GenerateXmlContents();
                // contents.setError( Constants.ERROR_MSG_API6 );
                // contents.setErrorCode( Constants.ERROR_CODE_API6 );
                // contents.setResultCount( 0 );
                //
                // GenerateXmlHeader header = new GenerateXmlHeader();
                // header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                // header.setMethod( paramMethod );
                // header.setName( prefName );
                // header.setCount( 0 );
                // // クチコミ詳細を追加
                // header.setContents( contents );
                //
                // // 出力をヘッダーから
                // String xmlOut = header.createXml();
                // ServletOutputStream out = null;
                //
                // out = response.getOutputStream();
                // response.setContentType( "text/xml; charset=UTF-8" );
                // out.write( xmlOut.getBytes( "UTF-8" ) );

            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiHotelNewMessage.execute ]Exception:" + exception.toString() );

            // エラーを出力
            GenerateXmlHeader header = new GenerateXmlHeader();
            GenerateXmlContents contents = new GenerateXmlContents();
            contents.setError( Constants.ERROR_MSG_API10 );
            contents.setErrorCode( Constants.ERROR_CODE_API10 );

            // 検索結果ヘッダ作成
            header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            header.setMethod( paramMethod );
            header.setName( "ホテル最新情報" );
            header.setCount( 0 );
            // ホテル詳細を追加
            header.setContents( contents );

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
                Logging.error( "[ActionApiHotelNewMessage response]Exception:" + e.toString() );
            }
        }
    }
}
