package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlMenu;
import jp.happyhotel.others.GenerateXmlTopics;
import jp.happyhotel.others.GenerateXmlTopicsSub;
import jp.happyhotel.search.SearchSystemInfo;
import jp.happyhotel.user.UserLoginInfo;

/**
 * メニュークラス（API）
 * 
 * @author S.Tashiro
 * @version 1.0 2012/07/20
 * 
 */

public class ActionApiTopics extends BaseAction
{
    public String TOP_MENU     = "TopMenu";
    public String SPECIAL_MENU = "SpecialMenu";
    public int    IMG_ICON     = 1;
    public int    IMG_BANNER   = 2;

    /**
     * メニュー情報（API）
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int DISP_COUNT = 5;
        final int TOPICS = 1;
        final int DISP_FLAG = 3;
        final int UA_SMARTPHONE = 5;
        int kind = 0;
        String strMenu = "";
        String paramMethod = "";
        UserLoginInfo uli;

        // XML出力
        boolean ret = false;

        SearchSystemInfo ssi = new SearchSystemInfo();
        GenerateXmlHeader header = new GenerateXmlHeader();
        GenerateXmlMenu gmMenu = new GenerateXmlMenu();
        GenerateXmlTopics gmTopics = new GenerateXmlTopics();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            ret = ssi.getTopicsDataList( TOPICS, DISP_COUNT, 0, DISP_FLAG, 0, UA_SMARTPHONE );
            if ( ret != false )
            {

                ssi.getDataCount();
                gmTopics.setCount( ssi.getDataCount() );

                for( int i = 0 ; i < ssi.getDataCount() ; i++ )
                {
                    GenerateXmlTopicsSub gmTopicsSub = new GenerateXmlTopicsSub();

                    gmTopicsSub.setDate( ssi.getSystemInfo()[i].getStartDate() );
                    gmTopicsSub.setTitle( ssi.getSystemInfo()[i].getTitleSmart() );
                    gmTopicsSub.setUrl( ssi.getSystemInfo()[i].getUrlSmart() );

                    gmTopics.setTopicsSub( gmTopicsSub );
                }
                gmMenu.addTopics( gmTopics );

                // 検索結果ヘッダ作成
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( strMenu );
                header.setCount( ssi.getDataCount() );
                header.setMenu( gmMenu );

            }
            else
            {
                gmMenu.setError( Constants.ERROR_MSG_API11 );
                gmMenu.setErrorCode( Constants.ERROR_CODE_API11 );

                // 検索結果ヘッダ作成
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( strMenu );
                header.setCount( ssi.getDataCount() );
                header.setMenu( gmMenu );
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
            Logging.error( "[ActionApiTopics ]Exception:" + exception.toString() );

            // エラーを出力
            gmMenu.setError( Constants.ERROR_MSG_API10 );
            gmMenu.setErrorCode( Constants.ERROR_CODE_API10 );

            // 検索結果ヘッダ作成
            header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            header.setMethod( paramMethod );
            header.setName( "メニュー" );
            header.setCount( 0 );
            header.setMenu( gmMenu );

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
                Logging.error( "[ActionApiTopics response]Exception:" + e.toString() );
            }
        }
        finally
        {
        }
    }
}
