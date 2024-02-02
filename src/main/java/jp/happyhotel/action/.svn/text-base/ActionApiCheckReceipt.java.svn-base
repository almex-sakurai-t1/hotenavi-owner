package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AppleReceiptCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApUuid;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.others.GenerateXmlCheckReceipt;
import jp.happyhotel.user.UserLoginInfo;

public class ActionApiCheckReceipt extends BaseAction
{
    static int              pageRecords         = Constants.pageLimitRecords;
    static String           recordsNotFoundMsg2 = Constants.errorRecordsNotFound2;

    String                  paramMethod         = null;
    String                  paramUuid           = null;
    String                  paramReceipt        = null;
    String                  paramCheckType      = null;

    int                     errorCode           = 0;
    String                  errorMsg            = "";

    final int               KIND_NONMEMBER      = 0;
    final int               KIND_FREEMEMBER     = 1;
    final int               KIND_PREMIUMMEMBER  = 2;

    protected UserLoginInfo uli;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            Logging.info( "ActionApiCheckReceipt.execute start" );

            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );
            paramUuid = request.getParameter( "uuid" );
            paramCheckType = request.getParameter( "checkType" );
            paramReceipt = request.getParameter( "receipt" );

            // base64文字列の"+"が、webリクエストで空白になってしまっているので元に戻す
            paramReceipt = paramReceipt.replace( " ", "+" );

            Logging.info( "uuid=" + paramUuid + ", checkType=" + paramCheckType + ", receipt=" + paramReceipt );

            // レシート検証
            ReceiptCheck();

            GenerateXmlCheckReceipt xmlReceipt = new GenerateXmlCheckReceipt();

            xmlReceipt.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            xmlReceipt.setMethod( paramMethod );
            xmlReceipt.setName( "レシート検証要求" );
            xmlReceipt.setCount( 1 );
            xmlReceipt.setErrorCode( errorCode );
            xmlReceipt.setErrorMessage( errorMsg );

            String xmlOut = xmlReceipt.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;

            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiCheckReceipt response]Exception:" + e.toString() );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiCheckReceipt.execute() ] Exception:", exception );

            // エラーを出力
            GenerateXmlCheckReceipt xmlReceipt = new GenerateXmlCheckReceipt();
            xmlReceipt.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            xmlReceipt.setMethod( paramMethod );
            xmlReceipt.setName( "レシート検証要求" );
            xmlReceipt.setCount( 1 );
            xmlReceipt.setErrorCode( Constants.ERROR_CODE_API10 );
            xmlReceipt.setErrorMessage( Constants.ERROR_MSG_API10 );

            String xmlOut = xmlReceipt.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;

            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiCheckReceipt response]Exception:" + e.toString() );
            }
        }
        finally
        {
            Logging.info( "ActionApiCheckReceipt.execute end" );
        }
    }

    public void ReceiptCheck() throws Exception
    {
        Logging.info( "ActionApiCheckReceipt.ReceiptCheck start" );

        DataApUuid dau = new DataApUuid();

        boolean IsUuidExist = dau.getData( paramUuid );

        Logging.info( "IsUuidExist=" + IsUuidExist );

        dau.setUuid( paramUuid );
        dau.setReceipt( paramReceipt );
        dau.setUpdateDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        dau.setUpdateTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

        if ( IsUuidExist )
        {
            dau.setReceiptCheck( 0 );
            if ( dau.updateData( paramUuid ) )
            {
                Logging.info( "ap_uuid update" );
            }
            else
            {
                errorCode = Constants.ERROR_CODE_API20;
                errorMsg = Constants.ERROR_MSG_API20;
                Logging.info( "ap_uuid update error" );
            }
        }
        else
        {
            if ( dau.insertData() )
            {
                Logging.info( "ap_uuid insert" );
            }
            else
            {
                errorCode = Constants.ERROR_CODE_API20;
                errorMsg = Constants.ERROR_MSG_API20;
            }
        }

        // リストア時
        if ( paramCheckType == "1" )
        {
            // リストア時はリストア前のユーザIDを復帰させる
            // レシートのトランザクションIDが利用できるかどうか、現在は様子見。
        }

        AppleReceiptCheck arc = new AppleReceiptCheck();

        // レシート検証
        int ret;
        try
        {
            ret = arc.execute( paramUuid );
        }
        catch ( Exception e )
        {
            throw e;
        }

        int env = ret / 100000;
        int status = ret - (env * 100000);

        Logging.info( "AppleReceiptCheck=" + ret );

        // 本番環境またはサンドボックス
        if ( ((env * 100000) == arc.PRODUCTION_ID) || ((env * 100000) == arc.SANDBOX_ID) )
        {
            // レシート検証OK
            if ( status == 0 )
            {
                Logging.info( "receipt check success" );

                // 有料会員
                dau.setRegistStatusPay( KIND_PREMIUMMEMBER );
                dau.setReceiptCheck( 1 );

                DataUserBasic userbasic = uli.getUserInfo();

                userbasic.setRegistStatusPay( 9 );
                userbasic.setRegistDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                userbasic.setRegistTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );

                userbasic.updateData( userbasic.getUserId() );

                // iOSのアプリ内課金では、入会マイルを付与しない
                /*
                 * if ( userbasic.updateData( userbasic.getUserId() ) )
                 * {
                 * UserPointPay point = new UserPointPay();
                 * point.setRegistPoint( userbasic.getUserId(), 1000001, 0, "入会マイル" );
                 * }
                 */
            }

            // レシート有効期限切れ
            else if ( status == 21006 )
            {
                // 無料会員
                Logging.info( "This receipt is expired." );
                dau.setRegistStatusPay( KIND_FREEMEMBER );
                errorCode = Constants.ERROR_CODE_API21;
                errorMsg = Constants.ERROR_MSG_API21;
            }

            // 検証失敗
            else
            {
                Logging.info( "receipt check error" );
                errorCode = Constants.ERROR_CODE_API20;
                errorMsg = Constants.ERROR_MSG_API20;
            }
        }

        // 環境不明
        else
        {
            Logging.info( "check environment unknown" );
            errorCode = Constants.ERROR_CODE_API20;
            errorMsg = Constants.ERROR_MSG_API20;
        }

        if ( dau.updateData( paramUuid ) )
        {
            Logging.info( "ap_uuid update" );
        }

        Logging.info( "ActionApiCheckReceipt.ReceiptCheck end" );
    }
}
