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
 * ���j���[�N���X�iAPI�j
 * 
 * @author S.Tashiro
 * @version 1.0 2012/07/20
 * 
 */

public class ActionApiTopMessage extends BaseAction
{
    public String TOP_MESSAGE = "TopMessage";

    /**
     * ���b�Z�[�W���iAPI�j
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int DISP_COUNT = 1;
        int kind = 0;
        String strMessage = "";
        String paramMethod = "";
        UserLoginInfo uli;

        // XML�o��
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
                strMessage = "TOP���b�Z�[�W";
            }

            ret = xmlMessage.getMessage( kind, DISP_COUNT );
            if ( ret != false )
            {
                for( int i = 0 ; i < xmlMessage.getCount() ; i++ )
                {
                    message.setMessage( xmlMessage.getMessageDataInfo()[i].getText() );
                }

                // �N�`�R�~�ڍׂ�ǉ�
                if ( kind == 1 )
                {
                    menu.addTopMessage( message );
                }

                // �������ʃw�b�_�쐬
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

                // �������ʃw�b�_�쐬
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( strMessage );
                header.setCount( xmlMessage.getCount() );
                // �N�`�R�~�ڍׂ�ǉ�
                if ( kind == 1 )
                {
                    menu.addTopMessage( message );
                }
                header.setMenu( menu );
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
            Logging.error( "[ActionApiTopMessage ]Exception:" + exception.toString() );

            // �G���[���o��
            menu.setError( Constants.ERROR_MSG_API10 );
            menu.setErrorCode( Constants.ERROR_CODE_API10 );

            // �������ʃw�b�_�쐬
            header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            header.setMethod( paramMethod );
            header.setName( "Top���b�Z�[�W" );
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
