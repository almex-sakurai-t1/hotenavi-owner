package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApTokenUser;
import jp.happyhotel.data.DataApUserPushConfig;
import jp.happyhotel.others.GenerateXmlDeleteResult;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.touch.GooglePushMapper;
import jp.happyhotel.touch.Push2Android;
import jp.happyhotel.touch.Push2Iphone;
import jp.happyhotel.user.UserLoginInfo;

public class ActionApiRegDeviceId extends BaseAction
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
        String paramDeviceType = null;
        UserLoginInfo uli;
        DataApTokenUser datu = new DataApTokenUser();
        DataApUserPushConfig daupc = new DataApUserPushConfig();
        String errorMsg = "";
        boolean retToken = false;
        boolean ret = false;
        int regCount = 0;
        int errorCode = 0;
        String oldUserId = "";
        String oldToken = "";

        try
        {
            Logging.info( "ActionApiRegDeviceId:URL:" + request.getRequestURI() );
            Logging.info( "ActionApiRegDeviceId:param:" + request.getQueryString() );
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );
            paramDeviceId = request.getParameter( "deviceId" );
            paramDeviceType = request.getParameter( "deviceType" );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }
            if ( paramUserId == null )
            {
                paramUserId = "";
            }
            if ( paramDeviceId == null || paramDeviceId.equals( "" ) != false )
            {
                paramDeviceId = "";
            }
            if ( paramDeviceType == null || paramDeviceType.equals( "" ) != false || CheckString.numCheck( paramDeviceType ) == false )
            {
                paramDeviceType = "0";
            }

            if ( uli.getUserInfo() != null )
            {
                if ( paramDeviceId.equals( "" ) == false )
                {
                    // ���N�G�X�g���ꂽ�f�o�C�XID�Ŏ擾����
                    retToken = datu.getDataByToken( paramDeviceId );
                    datu.setToken( paramDeviceId );
                    datu.setUserId( uli.getUserInfo().getUserId() );
                    // DB�ɂ͎擾�����l�{1�ŕۑ�����B�iiPhone:1�AAndorid:2�j
                    datu.setOsType( Integer.parseInt( paramDeviceType ) + 1 );
                    datu.setUpdateDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    datu.setUpdateTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    datu.setErrorFlag( 0 );
                    if ( retToken != false )
                    {
                        ret = datu.updateData();
                    }
                    else
                    // Token�̓o�^���Ȃ��̂ŏ�������
                    {
                        datu.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        datu.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        ret = datu.insertData();
                        Logging.info( "[ActionApiRegDeviceId.insertData()]:" + ret );
                    }
                    if ( ret != false )
                    {
                        regCount = 1;
                        // �o�^�ł�����C���T�[�g���Ă���
                        ret = daupc.getData( uli.getUserInfo().getUserId() );
                        daupc.setUserId( uli.getUserInfo().getUserId() );
                        daupc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        daupc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        if ( ret != false )
                        {
                            daupc.updateData( uli.getUserInfo().getUserId() );
                        }
                        else
                        {
                            daupc.insertData();
                        }

                        if ( paramDeviceType.equals( "0" ) != false )
                        {
                            Push2Iphone p2i = new Push2Iphone();
                            // p2i.push( paramDeviceId, "PUSH�pUUID�o�^����" );
                        }
                        else
                        {
                            Push2Android p2a = new Push2Android();
                            GooglePushMapper gpm = new GooglePushMapper();

                            gpm.addRegId( paramDeviceId );
                            gpm.createData( "PUSH�pUUID�o�^����", "" );
                            // p2a.push( gpm );
                        }

                    }
                    else
                    {
                        // �o�^�Ɏ��s�������߃G���[
                        regCount = 0;
                        errorCode = Constants.ERROR_CODE_API17;
                        errorMsg = Constants.ERROR_MSG_API17;
                    }
                }
                else
                {
                    // �f�o�C�X�g�[�N�����擾�ł��Ȃ����߃G���[
                    regCount = 0;
                    errorCode = Constants.ERROR_CODE_API4;
                    errorMsg = Constants.ERROR_MSG_API4;
                }
            }
            else
            {
                regCount = 0;
                // ���[�U��{���擾��null�̏ꍇ
                errorCode = Constants.ERROR_CODE_API16;
                errorMsg = Constants.ERROR_MSG_API16;
            }

            // �폜���ʍ쐬
            GenerateXmlDeleteResult deleteResult = new GenerateXmlDeleteResult();
            deleteResult.setErrorCode( errorCode );
            deleteResult.setErrorMessage( errorMsg );

            // �폜���ʃw�b�_�쐬
            GenerateXmlHeader deleteHeader = new GenerateXmlHeader();
            deleteHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            deleteHeader.setMethod( paramMethod );
            deleteHeader.setName( "�f�o�C�XID�o�^" );
            deleteHeader.setCount( regCount );
            // �������ʃm�[�h���������ʃw�b�_�[�m�[�h�ɒǉ�
            deleteHeader.setDeleteResult( deleteResult );

            String xmlOut = deleteHeader.createXml();
            Logging.info( xmlOut );

            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiRegDeviceId.execute() ] Exception:", exception );

            // �G���[���o��
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // �������ʃw�b�_�쐬
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "�f�o�C�XID�o�^" );
            searchHeader.setCount( 0 );
            // �z�e���ڍׂ�ǉ�
            searchHeader.setSearchResult( searchResult );

            String xmlOut = searchHeader.createXml();
            ServletOutputStream out = null;

            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiRegDeviceId response]Exception:" + e.toString() );
            }
        }
        finally
        {
        }
    }
}
