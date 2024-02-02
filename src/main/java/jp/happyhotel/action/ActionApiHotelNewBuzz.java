package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.hotel.HotelBbs;
import jp.happyhotel.others.GenerateXmlContents;
import jp.happyhotel.others.GenerateXmlContentsHotel;
import jp.happyhotel.others.GenerateXmlHeader;

/**
 *
 * 新着クチコミ表示クラス
 *
 * @author S.Tashiro
 * @version 1.0 2011/05/02
 */

public class ActionApiHotelNewBuzz extends BaseAction
{

    final private static int DISP_MAX_NUMBER = 20;

    /**
     *
     *
     * @param request クライアントからサーバへのリクエスト
     * @param response サーバからクライアントへのレスポンス
     * @see "/キャリアのフォルダ/search/hotelmap_M2.jsp うまくいった場合に遷移する"
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
        DataHotelBasic dhb;
        DataMasterPref dmp;
        HotelBbs bbs;

        dhb = new DataHotelBasic();
        dmp = new DataMasterPref();
        bbs = new HotelBbs();

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
            ret = bbs.getOwnerBbsListByPref( DISP_MAX_NUMBER, Integer.parseInt( paramPage ), Integer.parseInt( paramPrefId ) );
            // 地方ID、都道府県ID
            if ( paramPrefId.equals( "0" ) == false )
            {
                dmp.getData( Integer.parseInt( paramPrefId ) );
                prefName = dmp.getName();
            }

            if ( ret != false )
            {
                GenerateXmlContents contents = new GenerateXmlContents();
                contents.setError( "" );
                contents.setErrorCode( 0 );
                contents.setResultCount( bbs.getBbsCount() );

                // ホテルのPVデータをセット
                if ( bbs.getBbsCount() > 0 )
                {
                    for( int i = 0 ; i < bbs.getBbsCount() ; i++ )
                    {
                        name = "";
                        dhb.getData( bbs.getHotelBbs()[i].getId() );

                        GenerateXmlContentsHotel hotel = new GenerateXmlContentsHotel();

                        // 日付
                        strDate = Integer.toString( bbs.getHotelBbs()[i].getContributeDate() / 10000 ) + "."
                                + String.format( "%1$02d", bbs.getHotelBbs()[i].getContributeDate() / 100 % 100 ) + "."
                                + String.format( "%1$02d", bbs.getHotelBbs()[i].getContributeDate() % 100 );
                        hotel.setDate( strDate );

                        // 名前をセットする
                        hotelName = dhb.getName();
                        hotel.setName( hotelName );

                        hotel.setAddress( dhb.getPrefName() + dhb.getAddress1() );
                        hotel.setId( bbs.getHotelBbs()[i].getId() );

                        name = bbs.getHotelBbs()[i].getUserName() + "さんのクチコミ";
                        hotel.setMessage( name );

                        contents.addHotel( hotel );
                    }
                }
                else
                {
                    contents.setError( Constants.ERROR_MSG_API6 );
                    contents.setErrorCode( Constants.ERROR_CODE_API6 );
                    contents.setResultCount( bbs.getBbsCount() );
                }

                GenerateXmlHeader header = new GenerateXmlHeader();
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( prefName );
                header.setCount( bbs.getBbsAllCount() );
                // クチコミ詳細を追加
                header.setContents( contents );

                // 出力をヘッダーから
                String xmlOut = header.createXml();
                ServletOutputStream out = null;

                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );

            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiHotelNewBuzz.execute ]Exception:" + exception.toString() );

            // エラーを出力
            GenerateXmlHeader header = new GenerateXmlHeader();
            GenerateXmlContents contents = new GenerateXmlContents();
            contents.setError( Constants.ERROR_MSG_API10 );
            contents.setErrorCode( Constants.ERROR_CODE_API10 );

            // 検索結果ヘッダ作成
            header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            header.setMethod( paramMethod );
            header.setName( "新着クチコミ" );
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
                Logging.error( "[ActionApiHotelNewBuzz response]Exception:" + e.toString() );
            }
        }
    }
}
