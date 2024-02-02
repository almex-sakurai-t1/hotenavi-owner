package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.others.GenerateXmlMaster;

/**
 * マスターデータバージョンチェック送付クラス
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/05
 */

public class ActionApiMasterVersionCheck extends BaseAction
{

    /**
     * マスターバージョンチェック
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param response サーバからクライアントへのレスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {

        // XML出力関連
        boolean ret = false;
        String paramMethod;
        paramMethod = request.getParameter( "method" );

        try
        {
            GenerateXmlMaster master = new GenerateXmlMaster();
            master.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            master.setError( "" );
            master.setErrorCode( 0 );
            // IC検索を修正
            master.setMaster( "" );
            master.setVersion( "2018100901" );

            // 出力をヘッダーから
            String xmlOut = master.createXml();
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionApiMasterVersionCheck.execute()] Ecxeption:" + e.toString() );
        }
    }
}
