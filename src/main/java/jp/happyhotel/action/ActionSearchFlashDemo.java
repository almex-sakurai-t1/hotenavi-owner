package jp.happyhotel.action;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;

/**
 * 
 * ��������flash�f���y�[�W�\���N���X
 * 
 * @author N.Ide
 * @version 1.0 2009/10/13
 */

public class ActionSearchFlashDemo extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    /**
     * �Z�������̌��ʈꗗ��Flash�ŕ\������
     * 
     * @param request �N���C�A���g����T�[�o�ւ̃��N�G�X�g
     * @param response �T�[�o����N���C�A���g�ւ̃��X�|���X
     * @see "../search_result_flash_error.jsp Flfast���G���[���o�͂����ۂɕ\��"
     * @see "../../index.jsp �\�����Ȃ��G���[�̏ꍇ�ɕ\��"
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int count, xmlCount, loopCount = 0;
        String paramUidLink = null;
        String userAgent = "";
        String errCode = "";
        InputStream in = null, xmlIn = null;
        OutputStream out = null, xmlOut = null;
        byte[] readBuff = new byte[1024];
        byte[] readBuffXml = new byte[1024];
        final String XMLFilePass = "/usr/local/tomcat/temp/searchdemo.xml";
        final String confFilePass = "/etc/happyhotel/test.conf";
        FileInputStream propfile = null;
        Properties config;
        String baseDir = "/happyhotel/flash/flfast";
        String flfastCmd = "/bin/flfast";
        String flfastConfig = "/bin/flfast.conf";
        String cmd;
        Runtime rt;
        Process pr;

        try
        {
            paramUidLink = (String)request.getAttribute( "UID-LINK" );

            // ���[�U�[�G�[�W�F���g�̎擾
            userAgent = URLEncoder.encode( UserAgent.getUserAgent( request ), "SHIFT_JIS" );

            // Flfast�N�����̃R�}���h�𐶐�
            flfastConfig = baseDir + flfastConfig;
            flfastCmd = baseDir + flfastCmd;
            cmd = flfastCmd + " -u" + userAgent + " -f" + flfastConfig;

            // �R�}���h�����s
            rt = Runtime.getRuntime();
            pr = rt.exec( cmd );
            xmlOut = pr.getOutputStream();
            in = pr.getInputStream();

            try
            {
                // xml�t�@�C���̓ǂݍ���
                xmlIn = new BufferedInputStream( new FileInputStream( XMLFilePass ) );
                // xmlIn = new BufferedInputStream(new FileInputStream("/usr/local/tomcat/temp/05001014503955_me.ezweb.ne.jp.txt"));
                while( true )
                {
                    xmlCount = xmlIn.read( readBuffXml );
                    if ( xmlCount == -1 )
                    {
                        break;
                    }
                    // xml�f�[�^���v���Z�X�ɗ�������
                    for( int j = 0 ; j < xmlCount ; j++ )
                    {
                        xmlOut.write( readBuffXml[j] );
                    }
                }
                // stream��close
                xmlIn.close();
                xmlOut.close();
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionAreaSearchMobile_M2_Flash] Exception 1 = " + e.toString() );
            }
            // Flfast�Ő������ꂽswf��\��
            try
            {
                while( true )
                {
                    count = in.read( readBuff );
                    // ERRCODE�Ŏn�܂��Ă�����G���[�Ƃ��ď���
                    if ( loopCount == 0 )
                    {
                        if ( new String( readBuff ).substring( 0, 7 ).compareTo( "ERRCODE" ) == 0 )
                        {
                            errCode = new String( readBuff ).substring( 10, 14 );
                            Logging.error( "[ActionAreaSearchMobile_M2_Flash] ERRCODE = " + errCode );
                            /*
                             * request.setAttribute( "ERROR-CODE", errCode );
                             * requestDispatcher = request.getRequestDispatcher( "../search_result_flash_error.jsp?" + paramUidLink );
                             * requestDispatcher.forward( request, response );
                             */
                            response.sendRedirect( "search_result_flash_error.jsp?" + paramUidLink + "&code=" + errCode );
                            break;
                        }
                        else
                        {
                            out = response.getOutputStream();
                        }
                        loopCount++;
                    }

                    if ( count == -1 )
                    {
                        break;
                    }
                    response.setContentType( "application/x-shockwave-flash" );
                    for( int i = 0 ; i < count ; i++ )
                    {
                        out.write( readBuff[i] );
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionAreaSearchMobile_M2_Flash] Exception 2 = " + e.toString() );
            }
            finally
            {
                out.close();
                in.close();
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionAreaSearchMobile_M2_Flash.execute() ] Exception", exception );
            try
            {
                response.sendRedirect( "../../index.jsp?" + paramUidLink );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionAreaSearchMobile_M2_Flash.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
        }
    }
}
