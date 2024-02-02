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

            // base64�������"+"���Aweb���N�G�X�g�ŋ󔒂ɂȂ��Ă��܂��Ă���̂Ō��ɖ߂�
            paramReceipt = paramReceipt.replace( " ", "+" );

            Logging.info( "uuid=" + paramUuid + ", checkType=" + paramCheckType + ", receipt=" + paramReceipt );

            // ���V�[�g����
            ReceiptCheck();

            GenerateXmlCheckReceipt xmlReceipt = new GenerateXmlCheckReceipt();

            xmlReceipt.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            xmlReceipt.setMethod( paramMethod );
            xmlReceipt.setName( "���V�[�g���ؗv��" );
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

            // �G���[���o��
            GenerateXmlCheckReceipt xmlReceipt = new GenerateXmlCheckReceipt();
            xmlReceipt.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            xmlReceipt.setMethod( paramMethod );
            xmlReceipt.setName( "���V�[�g���ؗv��" );
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

        // ���X�g�A��
        if ( paramCheckType == "1" )
        {
            // ���X�g�A���̓��X�g�A�O�̃��[�UID�𕜋A������
            // ���V�[�g�̃g�����U�N�V����ID�����p�ł��邩�ǂ����A���݂͗l�q���B
        }

        AppleReceiptCheck arc = new AppleReceiptCheck();

        // ���V�[�g����
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

        // �{�Ԋ��܂��̓T���h�{�b�N�X
        if ( ((env * 100000) == arc.PRODUCTION_ID) || ((env * 100000) == arc.SANDBOX_ID) )
        {
            // ���V�[�g����OK
            if ( status == 0 )
            {
                Logging.info( "receipt check success" );

                // �L�����
                dau.setRegistStatusPay( KIND_PREMIUMMEMBER );
                dau.setReceiptCheck( 1 );

                DataUserBasic userbasic = uli.getUserInfo();

                userbasic.setRegistStatusPay( 9 );
                userbasic.setRegistDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                userbasic.setRegistTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );

                userbasic.updateData( userbasic.getUserId() );

                // iOS�̃A�v�����ۋ��ł́A����}�C����t�^���Ȃ�
                /*
                 * if ( userbasic.updateData( userbasic.getUserId() ) )
                 * {
                 * UserPointPay point = new UserPointPay();
                 * point.setRegistPoint( userbasic.getUserId(), 1000001, 0, "����}�C��" );
                 * }
                 */
            }

            // ���V�[�g�L�������؂�
            else if ( status == 21006 )
            {
                // �������
                Logging.info( "This receipt is expired." );
                dau.setRegistStatusPay( KIND_FREEMEMBER );
                errorCode = Constants.ERROR_CODE_API21;
                errorMsg = Constants.ERROR_MSG_API21;
            }

            // ���؎��s
            else
            {
                Logging.info( "receipt check error" );
                errorCode = Constants.ERROR_CODE_API20;
                errorMsg = Constants.ERROR_MSG_API20;
            }
        }

        // ���s��
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
