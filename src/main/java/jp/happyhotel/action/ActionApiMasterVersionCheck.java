package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.others.GenerateXmlMaster;

/**
 * �}�X�^�[�f�[�^�o�[�W�����`�F�b�N���t�N���X
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/05
 */

public class ActionApiMasterVersionCheck extends BaseAction
{

    /**
     * �}�X�^�[�o�[�W�����`�F�b�N
     * 
     * @param request �N���C�A���g����T�[�o�ւ̃��N�G�X�g
     * @param response �T�[�o����N���C�A���g�ւ̃��X�|���X
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {

        // XML�o�͊֘A
        boolean ret = false;
        String paramMethod;
        paramMethod = request.getParameter( "method" );

        try
        {
            GenerateXmlMaster master = new GenerateXmlMaster();
            master.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            master.setError( "" );
            master.setErrorCode( 0 );
            // IC�������C��
            master.setMaster( "" );
            master.setVersion( "2018100901" );

            // �o�͂��w�b�_�[����
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
