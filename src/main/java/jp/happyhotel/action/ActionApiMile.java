package jp.happyhotel.action;

import java.text.NumberFormat;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApUuidUser;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlMenu;
import jp.happyhotel.others.GenerateXmlMile;
import jp.happyhotel.others.GenerateXmlMileSub;
import jp.happyhotel.user.UserLoginInfo;
import jp.happyhotel.user.UserPointPay;

/**
 * ���j���[�N���X�iAPI�j
 * 
 * @author S.Tashiro
 * @version 1.0 2012/07/20
 * 
 */

public class ActionApiMile extends BaseAction
{
    public String MILE    = "Mile";
    public String MESSAGE = "�����O�C��";

    /**
     * ���j���[���iAPI�j
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        Logging.info( "ActionApiMile.execute start" );

        int count = 0;
        String strMenu = "";
        String paramMethod = "";
        UserLoginInfo uli;
        UserPointPay upp;

        // XML�o��
        boolean ret = false;

        String paramUserId = null;
        String paramUuid = null;
        int appStatus = 0;
        GenerateXmlHeader header = new GenerateXmlHeader();
        GenerateXmlMenu gmMenu = new GenerateXmlMenu();
        GenerateXmlMile gmMile = new GenerateXmlMile();

        gmMenu.setMessage( MESSAGE );

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );
            paramUserId = request.getParameter( "user_id" );
            paramUuid = request.getParameter( "uuid" );

            Logging.info( "ActionApiMile.execute uuid=" + paramUuid );

            if ( paramUserId == null )
            {
                paramUserId = "";
            }
            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( uli.getUserInfo() != null )
            {
                paramUserId = uli.getUserInfo().getUserId();

                DataApUuidUser dauu = new DataApUuidUser();

                dauu.getData( paramUuid, paramUserId );

                appStatus = dauu.getAppStatus();
                gmMenu.setLoginStatus( appStatus );

                Logging.info( "ActionApiMile.execute appStatus=" + appStatus );

                upp = new UserPointPay();
                // �����}�C���f�[�^�̎擾
                ret = upp.getPointExpired( paramUserId );

                NumberFormat nfComma;
                nfComma = NumberFormat.getInstance();

                gmMile.setPoint( nfComma.format( upp.getNowPoint( paramUserId, false ) ) );

                // �����}�C���f�[�^���擾�ł���
                if ( ret != false )
                {
                    count = 2;
                    gmMile.setCount( count );

                    // ���߂̎����}�C����\��
                    GenerateXmlMileSub gmMileSub = new GenerateXmlMileSub();
                    // �����}�C���Ώی����Z�b�g
                    gmMileSub.setMonth( upp.getExpiredMonth() + "����" );
                    // �����}�C�����Z�b�g
                    gmMileSub.setLostPoint( upp.getExpiredPointFormat() );
                    gmMile.setMileSub( gmMileSub );
                    gmMenu.addMile( gmMile );

                    // �����̎����}�C����\��
                    gmMileSub = new GenerateXmlMileSub();
                    // �����}�C���Ώی��i�����j���Z�b�g
                    gmMileSub.setMonth( upp.getExpiredMonthNext() + "����" );
                    // �����}�C���i�����j���Z�b�g
                    gmMileSub.setLostPoint( upp.getExpiredPointNextFormat() );
                    gmMile.setMileSub( gmMileSub );
                    gmMenu.addMile( gmMile );
                }
                else
                {
                    gmMile.setCount( count );
                }

                // �������ʃw�b�_�쐬
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( strMenu );
                header.setMenu( gmMenu );
            }
            else
            {
                gmMenu.setLoginStatus( appStatus );
                gmMenu.setError( Constants.ERROR_MSG_API11 );
                gmMenu.setErrorCode( Constants.ERROR_CODE_API11 );

                // �������ʃw�b�_�쐬
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( strMenu );
                header.setCount( count );
                header.setMenu( gmMenu );
            }

            // �o�͂��w�b�_�[����
            String xmlOut = header.createXml();
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiMile ]Exception:" + exception.toString() );

            gmMenu.setLoginStatus( appStatus );

            // �G���[���o��
            gmMenu.setError( Constants.ERROR_MSG_API10 );
            gmMenu.setErrorCode( Constants.ERROR_CODE_API10 );

            // �������ʃw�b�_�쐬
            header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            header.setMethod( paramMethod );
            header.setName( "�}�C��" );
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
                Logging.error( "[ActionApiMile response]Exception:" + e.toString() );
            }
        }
        finally
        {
        }
    }
}
