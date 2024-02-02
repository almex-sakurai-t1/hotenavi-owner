package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlMenu;
import jp.happyhotel.others.GenerateXmlTopMessage;
import jp.happyhotel.others.XmlMessage;
import jp.happyhotel.user.UserLoginInfo;

/**
 * メニュークラス（API）
 * 
 * @author S.Tashiro
 * @version 1.0 2012/07/20
 * 
 */

public class ActionApiTopMessage extends BaseAction
{
    public String TOP_MESSAGE = "TopMessage";

    /**
     * メッセージ情報（API）
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int DISP_COUNT = 1;
        int kind = 0;
        String strMessage = "";
        String paramMethod = "";
        UserLoginInfo uli;

        // XML出力
        boolean ret = false;
        XmlMessage xmlMessage = new XmlMessage();
        GenerateXmlHeader header = new GenerateXmlHeader();
        GenerateXmlMenu menu = new GenerateXmlMenu();
        GenerateXmlTopMessage message = new GenerateXmlTopMessage();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( paramMethod.equals( TOP_MESSAGE ) != false )
            {
                kind = 1;
                strMessage = "TOPメッセージ";
            }

            ret = xmlMessage.getMessage( kind, DISP_COUNT );
            if ( ret != false )
            {
                for( int i = 0 ; i < xmlMessage.getCount() ; i++ )
                {
                    message.setMessage( xmlMessage.getMessageDataInfo()[i].getText() );
                }

                // クチコミ詳細を追加
                if ( kind == 1 )
                {
                    menu.addTopMessage( message );
                }

                // 検索結果ヘッダ作成
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( strMessage );
                header.setCount( xmlMessage.getCount() );
                header.setMenu( menu );

            }
            else
            {
                menu.setError( Constants.ERROR_MSG_API11 );
                menu.setErrorCode( Constants.ERROR_CODE_API11 );

                // 検索結果ヘッダ作成
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( strMessage );
                header.setCount( xmlMessage.getCount() );
                // クチコミ詳細を追加
                if ( kind == 1 )
                {
                    menu.addTopMessage( message );
                }
                header.setMenu( menu );
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
            Logging.error( "[ActionApiTopMessage ]Exception:" + exception.toString() );

            // エラーを出力
            menu.setError( Constants.ERROR_MSG_API10 );
            menu.setErrorCode( Constants.ERROR_CODE_API10 );

            // 検索結果ヘッダ作成
            header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            header.setMethod( paramMethod );
            header.setName( "Topメッセージ" );
            header.setCount( 0 );
            header.setMenu( menu );

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
                Logging.error( "[ActionApiTopMessage response]Exception:" + e.toString() );
            }
        }
        finally
        {
        }
    }
}
