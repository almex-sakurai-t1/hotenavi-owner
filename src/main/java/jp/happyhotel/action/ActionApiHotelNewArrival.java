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
import jp.happyhotel.others.GenerateXmlContents;
import jp.happyhotel.others.GenerateXmlContentsHotel;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.search.SearchHotelNewArrival;

/**
 *
 * 新着ホテル表示クラス
 *
 * @author S.Tashiro
 * @version 1.0 2011/05/02
 */

public class ActionApiHotelNewArrival extends BaseAction
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
        int lastday;
        String paramLocalId;
        String paramPrefId;
        String paramPage;
        String paramMethod;
        String prefName = "全国";
        String name;
        int kind;
        DataHotelBasic dhb;
        DataMasterPref dmp;
        SearchHotelNewArrival shna;

        dhb = new DataHotelBasic();
        dmp = new DataMasterPref();
        shna = new SearchHotelNewArrival();

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
            ret = shna.getHotelListByPref( Integer.parseInt( paramPrefId ), DISP_MAX_NUMBER, Integer.parseInt( paramPage ) );
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
                contents.setResultCount( shna.getCount() );

                // ホテルのPVデータをセット
                if ( shna.getCount() > 0 )
                {
                    for( int i = 0 ; i < shna.getCount() ; i++ )
                    {
                        name = "";
                        kind = 0;

                        GenerateXmlContentsHotel hotel = new GenerateXmlContentsHotel();

                        // 新規、新着の判別
                        if ( shna.getHotelAdjustment()[i].getEditId() == 101 )
                        {
                            kind = 1;
                        }
                        else if ( shna.getHotelAdjustment()[i].getEditId() == 102 )
                        {
                            kind = 2;
                        }
                        hotel.setNew( kind );
                        // 日付
                        hotel.setDate( shna.getHotelAdjustment()[i].getInputDate() / 10000 + "." + String.format( "%1$02d", shna.getHotelAdjustment()[i].getInputDate() / 100 % 100 ) );

                        name = shna.getHotelInfo()[i].getName();
                        hotel.setName( name );
                        hotel.setAddress( shna.getHotelInfo()[i].getPrefName() + shna.getHotelInfo()[i].getAddress1() );
                        hotel.setId( shna.getHotelInfo()[i].getId() );

                        contents.addHotel( hotel );
                    }
                }
                else
                {
                    contents.setError( Constants.ERROR_MSG_API6 );
                    contents.setErrorCode( Constants.ERROR_CODE_API6 );
                    contents.setResultCount( shna.getCount() );
                }

                GenerateXmlHeader header = new GenerateXmlHeader();
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( prefName );
                header.setCount( shna.getAllCount() );
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
            Logging.error( "[ActionApiHotelNewArrival.execute ]Exception:" + exception.toString() );

            // エラーを出力
            GenerateXmlHeader header = new GenerateXmlHeader();
            GenerateXmlContents contents = new GenerateXmlContents();
            contents.setError( Constants.ERROR_MSG_API10 );
            contents.setErrorCode( Constants.ERROR_CODE_API10 );

            // 検索結果ヘッダ作成
            header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            header.setMethod( paramMethod );
            header.setName( "新着ホテル" );
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
                Logging.error( "[ActionApiHotelNewArrival response]Exception:" + e.toString() );
            }
        }
    }
}
