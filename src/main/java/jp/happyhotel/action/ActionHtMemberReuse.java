package jp.happyhotel.action;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApHotelCustom;
import jp.happyhotel.others.GenerateXmlHapiTouchHtMemberReuse;

/**
 * �n�s�z�e�A�v���`�F�b�N�C���N���X
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtMemberReuse extends BaseAction
{
    private static final String CONTENT_TYPE = "text/xml; charset=UTF-8";
    private static final String ENCODE       = "UTF-8";
    private static final String RESULT_OK    = "OK";
    private static final String RESULT_NG    = "NG";

    /**
     * �n�s�z�e�^�b�`
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        Logging.info( "HtMemberReuse Called" );
        // XML�o��
        boolean ret = false;
        boolean retMemberOperation = false;
        String paramId = "";
        int id = 0;
        int errorCode = 0;
        int result = 0;
        String customId;
        DataApHotelCustom dahc = new DataApHotelCustom();
        GenerateXmlHapiTouchHtMemberReuse gxTouch = new GenerateXmlHapiTouchHtMemberReuse();
        ServletOutputStream stream = null;

        // �z�e��ID�̓��C�Z���X�L�[�F�؂Œʂ����z�e��ID���Z�b�g
        paramId = (String)request.getAttribute( "HOTEL_ID" );
        customId = request.getParameter( "memberId" );
        if ( paramId == null )
        {
            paramId = "0";
        }
        if ( customId == null || customId.equals( "" ) != false || CheckString.numCheck( customId ) == false )
        {
            customId = "0";
        }
        id = Integer.parseInt( paramId );

        try
        {
            stream = response.getOutputStream();
            // �z�e��ID�ƃ����o�[ID����f�[�^���擾
            ret = dahc.isReusableCustomId( id, customId );

            if ( ret != false )
            {
                gxTouch.setResult( RESULT_OK );
            }
            else
            {
                gxTouch.setResult( RESULT_NG );
            }
            gxTouch.setErrorCode( errorCode );

            // XML�̏o��
            String xmlOut = gxTouch.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHtMemberReuse execute]Exception:" + e.toString() );

            try
            {
                // �G���[�����������ꍇ���Ԃ��B
                gxTouch.setResult( "NG" );
                gxTouch.setErrorCode( 99999 );

                stream = response.getOutputStream();
                // XML�̏o��
                String xmlOut = gxTouch.createXml();
                Logging.info( xmlOut );
                ServletOutputStream out = null;
                out = response.getOutputStream();
                response.setContentType( CONTENT_TYPE );
                out.write( xmlOut.getBytes( ENCODE ) );
            }
            catch ( Exception exception )
            {
                Logging.error( "[ActionHtMemberReuse execute]Exception:" + exception.toString() );
            }

        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHtMemberReuse execute]Exception:" + e.toString() );
                }
            }
        }
    }
}