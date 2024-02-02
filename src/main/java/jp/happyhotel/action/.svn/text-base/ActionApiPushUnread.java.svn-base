package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.others.GenerateXmlDeleteResult;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.touch.PushInfo;
import jp.happyhotel.user.UserLoginInfo;

public class ActionApiPushUnread extends BaseAction
{
    static int    pageRecords         = Constants.pageLimitRecords;
    static String recordsNotFoundMsg2 = Constants.errorRecordsNotFound2;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int DISP_COUNT = 1;
        final int DISP_FLAG = 2;

        String paramMethod = null;
        String paramDate = "";
        String paramTime = "";
        UserLoginInfo uli;
        String errorMsg = "";
        boolean ret = false;
        int regCount = 0;
        int errorCode = 0;
        PushInfo pushInfo = new PushInfo();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );
            paramDate = request.getParameter( "date" );
            paramTime = request.getParameter( "time" );
            if ( paramDate == null || paramDate.equals( "" ) == false || CheckString.numCheck( paramDate ) )
            {
                paramDate = "0";
            }
            if ( paramTime == null || paramTime.equals( "" ) == false || CheckString.numCheck( paramTime ) )
            {
                paramTime = "0";
            }

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( uli.getUserInfo() != null && uli.isMemberFlag() != false )
            {
                ret = pushInfo.getUnreadData( uli.getUserInfo().getUserId(), paramDate, paramTime );
                if ( ret != false )
                {
                    // お知らせ件数
                    regCount = pushInfo.getCampaignMaster().length;
                }
                else
                {
                    regCount = 0;
                }
            }
            else
            {
                regCount = 0;
                // ユーザ基本情報取得がnullの場合
                errorCode = Constants.ERROR_CODE_API16;
                errorMsg = Constants.ERROR_MSG_API16;
            }
            // TODO 暫定処置 errorCode,errorMessageをGenerateXmlDeleteResultで受ける
            // 検索結果作成
            GenerateXmlDeleteResult deleteResult = new GenerateXmlDeleteResult();
            deleteResult.setErrorCode( errorCode );
            deleteResult.setErrorMessage( errorMsg );

            // 検索結果ヘッダ作成
            GenerateXmlHeader deleteHeader = new GenerateXmlHeader();
            deleteHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            deleteHeader.setMethod( paramMethod );
            deleteHeader.setName( "お知らせ件数取得" );
            deleteHeader.setCount( regCount );
            // 検索結果ノードを検索結果ヘッダーノードに追加
            deleteHeader.setDeleteResult( deleteResult );

            String xmlOut = deleteHeader.createXml();
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiPushUnread.execute() ] Exception:", exception );

            // TODO 暫定処置 errorCode,errorMessageをGenerateXmlDeleteResultで受ける
            // エラーを出力
            GenerateXmlHeader deleteHeader = new GenerateXmlHeader();
            GenerateXmlDeleteResult deleteResult = new GenerateXmlDeleteResult();
            deleteResult.setErrorCode( Constants.ERROR_CODE_API10 );
            deleteResult.setErrorMessage( Constants.ERROR_MSG_API10 );

            // 検索結果ヘッダ作成
            deleteHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            deleteHeader.setMethod( paramMethod );
            deleteHeader.setName( "お知らせ件数取得" );
            deleteHeader.setCount( 0 );
            // 検索結果を追加
            deleteHeader.setDeleteResult( deleteResult );

            String xmlOut = deleteHeader.createXml();
            ServletOutputStream out = null;

            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiPushUnread response]Exception:" + e.toString() );
            }
        }
        finally
        {
        }
    }

}
