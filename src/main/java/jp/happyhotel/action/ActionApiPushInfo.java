package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApTokenUser;
import jp.happyhotel.others.GenerateXmlDeleteResult;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlPushInfo;
import jp.happyhotel.others.GenerateXmlPushInfoSub;
import jp.happyhotel.touch.PushInfo;
import jp.happyhotel.user.UserLoginInfo;

public class ActionApiPushInfo extends BaseAction
{
    static int    pageRecords         = Constants.pageLimitRecords;
    static String recordsNotFoundMsg2 = Constants.errorRecordsNotFound2;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int DISP_COUNT = 1;
        final int DISP_FLAG = 2;

        String paramUserId = null;
        String paramMethod = null;
        String paramDeviceId = null;
        UserLoginInfo uli;
        DataApTokenUser datu = new DataApTokenUser();
        String errorMsg = "";
        boolean ret = false;
        int regCount = 0;
        int errorCode = 0;
        PushInfo pushInfo = new PushInfo();
        GenerateXmlPushInfo xmlPush = new GenerateXmlPushInfo();
        GenerateXmlPushInfoSub xmlPushSub = new GenerateXmlPushInfoSub();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( uli.getUserInfo() != null && uli.isMemberFlag() != false )
            {
                ret = pushInfo.getData( uli.getUserInfo().getUserId() ,1);
                regCount = pushInfo.getCampaignMaster().length;
                for( int i = 0 ; i < regCount ; i++ )
                {
                    xmlPushSub = new GenerateXmlPushInfoSub();
                    xmlPushSub.setSeq( pushInfo.getCampaignMaster()[i].getCampaignId() );
                    // 送信した日
                    xmlPushSub.setDate( DateEdit.formatDate( 5, pushInfo.getUserCampaign()[i].getPushDate() ) );
                    xmlPushSub.setTime( DateEdit.formatTime( 2, pushInfo.getUserCampaign()[i].getPushTime() ) );
                    xmlPushSub.setTitle( pushInfo.getCampaignMaster()[i].getTitle() );
                    xmlPushSub.setDetail( pushInfo.getCampaignMaster()[i].getDetail() );
                    xmlPush.setPushInfoSub( xmlPushSub );
                }

            }
            else
            {
                regCount = 0;
                // ユーザ基本情報取得がnullの場合
                errorCode = Constants.ERROR_CODE_API16;
                errorMsg = Constants.ERROR_MSG_API16;
            }

            // 検索結果作成
            GenerateXmlDeleteResult deleteResult = new GenerateXmlDeleteResult();
            deleteResult.setErrorCode( errorCode );
            deleteResult.setErrorMessage( errorMsg );

            // 検索結果ヘッダ作成
            GenerateXmlHeader deleteHeader = new GenerateXmlHeader();
            deleteHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            deleteHeader.setMethod( paramMethod );
            deleteHeader.setName( "お知らせ一覧" );
            deleteHeader.setCount( regCount );
            // 検索結果ノードを検索結果ヘッダーノードに追加
            deleteHeader.setDeleteResult( deleteResult );

            // 追加したXMLを追加する
            deleteHeader.setPushInfo( xmlPush );

            String xmlOut = deleteHeader.createXml();
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiPushInfo.execute() ] Exception:", exception );

            // エラーを出力
            GenerateXmlHeader deleteHeader = new GenerateXmlHeader();
            GenerateXmlDeleteResult deleteResult = new GenerateXmlDeleteResult();
            deleteResult.setErrorCode( Constants.ERROR_CODE_API10 );
            deleteResult.setErrorMessage( Constants.ERROR_MSG_API10 );

            // 検索結果ヘッダ作成
            deleteHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            deleteHeader.setMethod( paramMethod );
            deleteHeader.setName( "お知らせ一覧" );
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
                Logging.error( "[ActionApiPushInfo response]Exception:" + e.toString() );
            }
        }
        finally
        {
        }
    }
}
