package jp.happyhotel.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataSearchCoupon_M2;
import jp.happyhotel.others.GenerateXml1;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelCoupon_M2;
import jp.happyhotel.search.SearchHotelGps_M2;

/**
 * 
 * �g�єŃN�[�|�������N���X�iflash�g�p�j
 * 
 * @author N.Ide
 * @version 1.0 2009/06/17
 */

public class ActionCouponSearchMobile_M2_Flash extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    /**
     * �N�[�|�������̌��ʈꗗ��Flash�ŕ\������
     * 
     * @param request �N���C�A���g����T�[�o�ւ̃��N�G�X�g
     * @param response �T�[�o����N���C�A���g�ւ̃��X�|���X
     * @see "../search_result_flash_error.jsp Flfast���G���[���o�͂����ۂɕ\��"
     * @see "../../index.jsp �\�����Ȃ��G���[�̏ꍇ�ɕ\��"
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int pageNum = 0;
        int scaleNum;
        int type;
        int count, xmlCount, loopCount = 0;
        int[] arrHotelIdList = null;
        int registStatus = 0;
        int available = 0;
        int delFlag = 0;
        int findCount;
        String groupName;
        String paramPrefId = null;
        String paramLocalId = null;
        String paramPage;
        String paramLatPos = null;
        String paramLonPos = null;
        String paramScale = null;
        String paramUidLink = null;
        String termNo = null;
        String paramFull = "";
        String userAgent = "";
        String errCode = "";
        final String actPath = "search/coupon/searchCouponMobile.act";
        final String actPathFlash = "search/coupon/searchCouponMobileFlash.act";
        boolean prefSearchFlag;
        boolean gpsSearchFlag;
        InputStream in = null, xmlIn = null;
        ;
        OutputStream out = null, xmlOut = null;
        byte[] readBuff = new byte[1024];
        byte[] readBuffXml = new byte[1024];
        String XMLFilePass = "";
        SearchHotelCoupon_M2 searchHotelCoupon = null;
        DataSearchCoupon_M2 dataSearchCoupon = null;
        SearchHotelCommon searchHotelCommon = null;
        DataLoginInfo_M2 dataLoginInfo = null;
        SearchHotelGps_M2 searchHotelGps = null;
        DataLoginInfo_M2 dli;

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

        dataSearchCoupon = new DataSearchCoupon_M2();
        dli = new DataLoginInfo_M2();

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
                    Logging.info( "[ActionCouponSearchMobile_M2_Flash AuAuthCheck] Exception:" + e.toString() );
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

            paramPrefId = request.getParameter( "pref_id" );
            paramPage = request.getParameter( "page" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            paramLatPos = request.getParameter( "lat" );
            paramLonPos = request.getParameter( "lon" );
            paramScale = request.getParameter( "scale" );
            if ( paramPage == null || CheckString.numCheck( paramPage ) == false )
            {
                paramPage = "0";
            }
            pageNum = Integer.parseInt( paramPage );
            if ( paramScale == null || CheckString.numCheck( paramScale ) == false )
            {
                paramScale = "0";
            }
            scaleNum = Integer.parseInt( paramScale );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
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
            // ���[�U�[�G�[�W�F���g�̎擾
            userAgent = URLEncoder.encode( UserAgent.getUserAgent( request ), "SHIFT_JIS" );
            // ������ۂ��̔���
            dataLoginInfo = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            if ( dataLoginInfo != null )
            {
                delFlag = dataLoginInfo.getDelFlag();
                registStatus = dataLoginInfo.getRegistStatus();
            }
            if ( registStatus == 9 && delFlag == 0 )
            {
                available = 1;
            }
            else
            {
                available = 2;
            }
            prefSearchFlag = (paramPrefId != null && CheckString.numCheck( paramPrefId ));
            gpsSearchFlag = (paramLatPos != null && paramLatPos.compareTo( "" ) != 0 && paramLonPos != null && paramLonPos.compareTo( "" ) != 0);
            if ( prefSearchFlag || gpsSearchFlag )
            {
                findCount = 0;
                searchHotelCoupon = new SearchHotelCoupon_M2();
                // �s���{�����I������Ă���ꍇ
                if ( prefSearchFlag )
                {
                    paramFull = "pref_id=" + paramPrefId;
                    arrHotelIdList = searchHotelCoupon.getHotelIdListByPref( Integer.parseInt( paramPrefId ), available, true );
                    searchHotelCommon = new SearchHotelCommon();
                    searchHotelCommon.setResultHotelList( arrHotelIdList );
                    findCount++;
                }
                // GPS�����̏ꍇ
                if ( gpsSearchFlag )
                {
                    paramFull = "lat=" + paramLatPos + "&lon=" + paramLonPos + "&scale=" + paramScale;
                    searchHotelGps = new SearchHotelGps_M2();
                    arrHotelIdList = searchHotelGps.getHotelIdList( paramLatPos, paramLonPos, scaleNum );
                    if ( findCount != 0 )
                    {
                        searchHotelCommon.setEquipHotelList( arrHotelIdList );
                        // �s���{���������ʂ�GPS�������ʂ��}�[�W
                        arrHotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                        {
                            arrHotelIdList = searchHotelCoupon.getHotelIdListGps( arrHotelIdList, available, true );
                        }
                    }
                }
                // xml�t�@�C�����쐬
                XMLFilePass = GenerateXml1.GenerateXml( arrHotelIdList, paramFull, pageNum, "����݌���", termNo, type, actPath, actPathFlash, 0, paramUidLink );

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
                    Logging.error( "[ActionCouponSearchMobile_M2_Flash] Exception 1 = " + e.toString() );
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
                                Logging.error( "[ActionCouponSearchMobile_M2_Flash] ERRCODE = " + errCode );
                                /*
                                 * request.setAttribute( "ERROR-CODE", errCode );
                                 * requestDispatcher = request.getRequestDispatcher( "../search_result_flash_error.jsp" );
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
                    Logging.error( "[ActionCouponSearchMobile_M2_Flash] Exception 2 = " + e.toString() );
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
                response.sendRedirect( "searchCouponMobile.act?" + paramUidLink );
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionCouponSearchMobile_M2_Flash.execute() ] Exception", exception );
            try
            {
                response.sendRedirect( "../../index.jsp?" + paramUidLink );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionCouponSearchMobile_M2_Flash.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            searchHotelCoupon = null;
            dataSearchCoupon = null;
            searchHotelCommon = null;
            dataLoginInfo = null;
            searchHotelGps = null;
        }
    }
}
