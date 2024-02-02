package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.Url;
import jp.happyhotel.data.DataHotelCi;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlTouchState;
import jp.happyhotel.user.UserLoginInfo;

/**
 * メニュークラス（API）
 * 
 * @author S.Tashiro
 * @version 1.0 2012/07/20
 * 
 */

public class ActionApiTouchState extends BaseAction
{
    public String TOUCH_STATE = "TouchState";

    /**
     * メニュー情報（API）
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final String URL = Url.getUrl();

        String url = "";
        int count = 0;
        String paramMethod = "";
        UserLoginInfo uli;
        HotelCi hc = new HotelCi();
        DataHotelCi dhc = null;
        int errorCode = 0;
        String errorMsg = "";
        // XML出力
        boolean ret = false;

        GenerateXmlHeader header = new GenerateXmlHeader();
        GenerateXmlTouchState gmTouch = new GenerateXmlTouchState();
        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }
            if ( uli.isMemberFlag() != false )
            {

                dhc = hc.touchState( uli.getUserInfo().getUserId() );
                if ( dhc != null )
                {
                    // 取得したデータがチェックイン状態であれば、URLを作成
                    if ( dhc.getUserId().equals( uli.getUserInfo().getUserId() ) != false && dhc.getCiStatus() == 0 )
                    {
                        // ハピホテ環境であれば
                        url = URL;

                        if ( dhc.getRsvNo().equals( "" ) == false )
                        {
                            url += "/phone/htap/hapiTouch.act?method=HtRsvFix&id=" + dhc.getId() + "&seq=" + dhc.getSeq() + "&reTouch=true";
                        }
                        else
                        {
                            url += "/phone/htap/hapiTouch.act?method=HtHome&id=" + dhc.getId() + "&seq=" + dhc.getSeq() + "&reTouch=true";
                        }

                        // アプリ用に特殊文字を変換
                        url = ReplaceString.replaceApiSpecial( url );
                    }
                }
                if ( url.equals( "" ) == false )
                {
                    count = 1;
                }
            }
            else
            {
                count = 0;
                // ユーザ基本情報取得がnullの場合
                errorCode = Constants.ERROR_CODE_API16;
                errorMsg = Constants.ERROR_MSG_API16;
            }

            gmTouch.setError( errorMsg );
            gmTouch.setErrorCode( errorCode );
            gmTouch.setUrl( url );

            header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            header.setMethod( paramMethod );
            header.setName( "タッチ受付状況" );
            header.setCount( count );
            header.setTouchState( gmTouch );

            // 出力をヘッダーから
            String xmlOut = header.createXml();
            Logging.info( "TouchState:" + xmlOut );
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiTouchState ]Exception:" + exception.toString() );

            // エラーを出力
            gmTouch.setError( Constants.ERROR_MSG_API10 );
            gmTouch.setErrorCode( Constants.ERROR_CODE_API10 );

            // 検索結果ヘッダ作成
            header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            header.setMethod( paramMethod );
            header.setName( "タッチ受付状況" );
            header.setCount( 0 );
            header.setTouchState( gmTouch );

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
                Logging.error( "[ActionApiTouchState response]Exception:" + e.toString() );
            }
        }
        finally
        {
        }
    }
}
