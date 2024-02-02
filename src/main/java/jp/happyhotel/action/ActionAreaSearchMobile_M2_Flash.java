package jp.happyhotel.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.FindPrefId;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.others.GenerateXml1;
import jp.happyhotel.search.SearchArea_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelFreeword_M2;

/**
 * 
 * �g�єŏZ�������N���X�iflash�g�p�j
 * 
 * @author N.Ide
 * @version 1.0 2009/06/15
 */

public class ActionAreaSearchMobile_M2_Flash extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    /**
     * �Z�������̌��ʈꗗ��Flash�ŕ\������
     * 
     * @param request �N���C�A���g����T�[�o�ւ̃��N�G�X�g
     * @param response �T�[�o����N���C�A���g�ւ̃��X�|���X
     * 
     * @see "../search_result_flash_error.jsp Flfast���G���[���o�͂����ۂɕ\��<br>"
     * @see "../../index.jsp �\�����Ȃ��G���[�̏ꍇ�ɕ\��"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int pageNum = 0;
        int jisCode;
        int type;
        int count, xmlCount, loopCount = 0;
        int prefId = 0;
        int[] arrHotelIdList = null;
        String cityName;
        String paramJisCode = null;
        String paramPage;
        String paramUidLink = null;
        String paramAndword = null;
        String termNo = null;
        String paramFull = "";
        String userAgent = "";
        String errCode = "";
        final String actPath = "search/area/searchAreaMobile.act";
        final String actPathFlash = "search/area/searchAreaMobileFlash.act";
        InputStream in = null, xmlIn = null;
        OutputStream out = null, xmlOut = null;
        byte[] readBuff = new byte[1024];
        byte[] readBuffXml = new byte[1024];
        String XMLFilePass = "";
        SearchArea_M2 searchArea = null;
        SearchHotelCommon searchHotelCommon = null;
        SearchHotelFreeword_M2 searchHotelFreeword = null;
        DataLoginInfo_M2 dli;
        FindPrefId fpi;

        final String confFilePass = "/etc/happyhotel/test.conf"; // TODO
        FileInputStream propfile = null;
        Properties config;
        String baseDir = "/happyhotel/flash/flfast"; // TODO
        String flfastCmd = "/bin/flfast"; // TODO
        String flfastConfig = "/bin/flfast.conf"; // TODO

        String cmd;
        Runtime rt;
        Process pr;

        String paramAcRead;
        boolean ret;
        AuAuthCheck auCheck;
        int carrierFlag;

        searchArea = new SearchArea_M2();
        dli = new DataLoginInfo_M2();
        fpi = new FindPrefId();

        try
        {
            /*
             * propfile = new FileInputStream( confFilePass );
             * config = new Properties();
             * config.load(propfile);
             * if( config.getProperty("dir.base") != null )
             * {
             * baseDir = config.getProperty("dir.base");
             * }
             * if( config.getProperty("") != null )
             * {
             * flfastCmd = config.getProperty("dir.flfastconf");
             * }
             * flfastCmd = baseDir + flfastCmd;
             * if( config.getProperty("") != null )
             * {
             * flfastConfig = config.getProperty("dir.flfastconf");
             * }
             */

            dli = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );

            // au��������A�N�Z�X�`�P�b�g���`�F�b�N����
            carrierFlag = UserAgent.getUserAgentType( request );
            paramAcRead = request.getParameter( "acread" );
            if ( (paramAcRead == null) && (carrierFlag == DataMasterUseragent.CARRIER_AU) )
            {
                try
                {
                    auCheck = new AuAuthCheck();
                    ret = auCheck.authCheckForClass( request, "free/mymenu/premium_flash.jsp?" + paramUidLink );
                    // �A�N�Z�X�`�P�b�g�m�F�̌��� false�������烊�_�C���N�g
                    if ( ret == false )
                    {
                        response.sendRedirect( auCheck.getResultData() );
                        return;
                    }
                    // �A�N�Z�X�`�P�b�g�m�F�̌��� true������������擾
                    else
                    {
                        // DataLoginInfo_M2���擾����
                        if ( auCheck.getDataLoginInfo() != null )
                        {
                            dli = auCheck.getDataLoginInfo();
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.error( "[ActionAreaSearchMobile_M2_Flash AuAuthCheck] Exception:" + e.toString() );
                }
            }
            // �L���������
            if ( dli != null )
            {
                // �L������o�^�r���̃��[�U�[�͗L������o�^�y�[�W��
                if ( dli.getRegistStatusPay() == 1 )
                {
                    response.sendRedirect( "../../free/mymenu/paymemberRegist.act?" + paramUidLink );
                    return;
                }
                // flash�@�\�����y�[�W��
                else if ( dli.getRegistStatusPay() != 9 )
                {
                    response.sendRedirect( "../../free/mymenu/premium_flash.jsp?" + paramUidLink );
                    return;
                }
            }
            // �����񂪂Ȃ��ꍇ��flash�@�\�����y�[�W��
            else
            {
                response.sendRedirect( "../../free/mymenu/premium_flash.jsp?" + paramUidLink );
                return;
            }

            paramJisCode = request.getParameter( "jis_code" );
            paramPage = request.getParameter( "page" );
            paramAndword = request.getParameter( "andword" );
            if ( paramPage == null || CheckString.numCheck( paramPage ) == false )
            {
                paramPage = "0";
            }

            // PrefId���擾
            prefId = fpi.getPrefId( request );

            // termNo�̎擾(xml�̃t�@�C�����Ɏg�p)
            type = UserAgent.getUserAgentType( request );
            if ( type == UserAgent.USERAGENT_AU )
            {
                termNo = request.getHeader( "x-up-subno" );
            }
            else if ( type == UserAgent.USERAGENT_VODAFONE )
            {
                termNo = request.getHeader( "x-jphone-uid" );
            }
            else if ( type == UserAgent.USERAGENT_DOCOMO )
            {
                termNo = request.getParameter( "uid" );
            }
            // Logging.info( "idetest termNo:" + termNo );

            if ( CheckString.numCheck( paramJisCode ) && CheckString.numCheck( paramPage ) )
            {
                pageNum = Integer.parseInt( paramPage );
                jisCode = Integer.parseInt( paramJisCode );
                // �s�撬�������擾
                searchArea.getCityDetail( jisCode );
                cityName = searchArea.getCityName();
                // ���[�U�[�G�[�W�F���g�̎擾
                userAgent = URLEncoder.encode( UserAgent.getUserAgent( request ), "SHIFT_JIS" );
                // jiscode����z�e���ꗗ���擾����
                arrHotelIdList = searchArea.getSearchIdList( jisCode );
                paramFull = "jis_code=" + paramJisCode;

                // �i�荞�݌���������Ă���ꍇ�AarrHotelIdList���i�荞��
                if ( paramAndword != null && (paramAndword.trim().length() > 0) )
                {
                    searchHotelCommon = new SearchHotelCommon();
                    // arrHotelIdList��EquipHotelList�ɃZ�b�g����
                    searchHotelCommon.setEquipHotelList( arrHotelIdList );
                    try
                    {
                        paramAndword = new String( paramAndword.getBytes( "8859_1" ), "Windows-31J" );
                        paramFull = paramFull + "&andword=" + URLEncoder.encode( paramAndword, "Shift_JIS" );
                        searchHotelFreeword = new SearchHotelFreeword_M2();
                        // �t���[���[�h�Ō������AarrHotelIdList��ResultHotelList�ɃZ�b�g����
                        arrHotelIdList = searchHotelFreeword.getSearchIdListNoCount( paramAndword );
                        searchHotelCommon.setResultHotelList( arrHotelIdList );
                        // EquipHotelList��ResultHotelList���}�[�W����
                        arrHotelIdList = searchHotelCommon.getMargeHotel( 0, 0 );

                    }
                    catch ( UnsupportedEncodingException e )
                    {
                        Logging.error( "[ActionAreaSearchMobile_M2_Flash.execute() ] UnsupportedEncodingException =" + e.toString() );
                        throw e;
                    }
                }
                // xml�t�@�C�����쐬
                XMLFilePass = GenerateXml1.GenerateXml( arrHotelIdList, paramFull, pageNum, cityName, termNo, type, actPath, actPathFlash, prefId, paramUidLink );
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
                                response.sendRedirect( "../search_result_flash_error.jsp?" + paramUidLink + "&code=" + errCode );
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
                    // xml�t�@�C�����폜
                    File file = new File( XMLFilePass );
                    if ( file.exists() != false )
                    {
                        file.delete();
                    }
                }
            }
            else
            {
                response.sendRedirect( "searchAreaMobile.act?" + paramUidLink );
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
            searchArea = null;
        }
    }
}
