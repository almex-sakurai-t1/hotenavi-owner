/*
 * @(#)HapiTouch.java
 * 1.00 2011/01/12 Copyright (C) ALMEX Inc. 2007
 * �n�s�^�b�`����N���X
 */
package jp.happyhotel.others;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.FileUrl;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataApHotelTerminal;
import jp.happyhotel.data.DataApTouchCi;
import jp.happyhotel.data.DataApUuidUser;
import jp.happyhotel.data.DataHotelAuth;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelCi;
import jp.happyhotel.data.DataHotelFelicaNg;
import jp.happyhotel.data.DataHotelHapiTouchConfig;
import jp.happyhotel.data.DataHotelMaster;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataRsvReserveBasic;
import jp.happyhotel.data.DataSystemFelicaMatching;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.data.DataUserFelica;
import jp.happyhotel.hotel.HotelAuth;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.hotel.HotelCoupon;
import jp.happyhotel.hotel.HotelHappie;
import jp.happyhotel.hotel.HotelRoom;
import jp.happyhotel.hotel.HotelRoomMore;
import jp.happyhotel.owner.FormOwnerRsvList;
import jp.happyhotel.owner.LogicOwnerRsvCheckIn;
import jp.happyhotel.owner.LogicOwnerRsvList;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;
import jp.happyhotel.touch.FrontTouchAcceptCheck;
import jp.happyhotel.touch.RsvFix;
import jp.happyhotel.touch.RsvList;
import jp.happyhotel.touch.TokenUser;
import jp.happyhotel.touch.TouchErrorHistory;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserLoginInfo;
import jp.happyhotel.user.UserPointPay;
import jp.happyhotel.user.UserPointPayTemp;

/**
 * �n�s�^�b�`
 * 
 * @author S.Tashiro
 * @version 1.00 2010/11/17
 */
public class HapiTouch
{
    // �|�C���g�敪
    private static final int    POINT_KIND_RAITEN   = 21;                                                    // ���X
    private static final int    POINT_KIND_RIYOU    = 22;                                                    // ���p
    private static final int    POINT_KIND_WARIBIKI = 23;                                                    // ����
    private static final int    POINT_KIND_YOYAKU   = 24;                                                    // �\��
    private static final int    COME_POINT          = 1000004;
    private static final int    AMOUNT_POINT        = 1000005;
    private static final int    USE_POINT           = 1000006;
    private static final int    RSV_POINT           = 1000007;
    private static final int    CI_STATUS_DEL       = 3;
    private static final int    SYNC_CI_DEL_FLAG    = 1;                                                     // SyncCi�ō폜�Ώۂ̃t���O
    private static final int    SYNC_CI_ADD_FLAG    = 0;                                                     // SyncCi�Œǉ��Ώۂ̃t���O
    private static final String METHOD_REGIST       = "Regist";
    private static final String RESULT_OK           = "OK";
    private static final String RESULT_NG           = "NG";
    private static final String RESULT_NO           = "NO";
    private static final String RESULT_DENY         = "DENY";
    private static final String CONTENT_TYPE        = "text/xml; charset=UTF-8";
    private static final String ENCODE              = "UTF-8";
    private DataLoginInfo_M2    dataLoginInfo_M2    = null;
    /* �Г��� */
    // private static String BASE_URL = "http://121.101.88.177/";
    // private static String INSTALL_URL32 = "http://121.101.88.177/install/"; // �C���X�g�[������URL
    // private static String INSTALL_URL64 = "http://121.101.88.177/install/"; // �C���X�g�[������URL
    // private static String FILE_URL32 = "/happyhotel/install/"; // ���[�J���Ńt�@�C���������s���ꍇ��URL
    // private static String FILE_URL64 = "/happyhotel/install/"; // ���[�J���Ńt�@�C���������s���ꍇ��URL
    // private static String FILE_URL32WIN = "C:\\ALMEX\\WWW\\happyhotel\\secure\\owner\\install\\"; // ���[�J���Ńt�@�C���������s���ꍇ��URL(Windows��OS�̏ꍇ)
    // private static String FILE_URL64WIN = "C:\\ALMEX\\WWW\\happyhotel\\secure\\owner\\install\\"; // ���[�J���Ńt�@�C���������s���ꍇ��URL(Windows��OS�̏ꍇ)
    /* �{�Ԋ� */
    private static String       BASE_URL            = Url.getUrl() + "/";
    private static String       INSTALL_URL32       = Url.getSslUrl() + "/owner/install/";                   // �C���X�g�[������URL
    private static String       INSTALL_URL64       = Url.getSslUrl() + "/owner/install/";                   // �C���X�g�[������URL
    private static String       FILE_URL32          = "/happyhotel/secure/owner/install/";                   // ���[�J���Ńt�@�C���������s���ꍇ��URL
    private static String       FILE_URL64          = "/happyhotel/secure/owner/install/";                   // ���[�J���Ńt�@�C���������s���ꍇ��URL
    private static String       FILE_URL32WIN       = "C:\\ALMEX\\WWW\\happyhotel\\secure\\owner\\install\\"; // ���[�J���Ńt�@�C���������s���ꍇ��URL(Windows��OS�̏ꍇ)
    private static String       FILE_URL64WIN       = "C:\\ALMEX\\WWW\\happyhotel\\secure\\owner\\install\\"; // ���[�J���Ńt�@�C���������s���ꍇ��URL(Windows��OS�̏ꍇ)
    /* �f�o�b�O */
    // private static String BASE_URL = "http://debug.happyhotel.jp/";
    // private static String INSTALL_URL32 = "https://debugssl.happyhotel.jp/owner/install/"; // �C���X�g�[������URL
    // private static String INSTALL_URL64 = "https://debugssl.happyhotel.jp/owner/install/"; // �C���X�g�[������URL
    // private static String FILE_URL32 = "/happyhotel/secure/owner/install/"; // ���[�J���Ńt�@�C���������s���ꍇ��URL
    // private static String FILE_URL64 = "/happyhotel/secure/owner/install/"; // ���[�J���Ńt�@�C���������s���ꍇ��URL
    // private static String FILE_URL32WIN = "C:\\ALMEX\\WWW\\happyhotel\\secure\\owner\\install\\"; // ���[�J���Ńt�@�C���������s���ꍇ��URL(Windows��OS�̏ꍇ)
    // private static String FILE_URL64WIN = "C:\\ALMEX\\WWW\\happyhotel\\secure\\owner\\install\\"; // ���[�J���Ńt�@�C���������s���ꍇ��URL(Windows��OS�̏ꍇ)
    private static String       FILE_NAME           = "HapiTouchInstall";                                    // �}�b�`���O���s���ꍇ�ɕK�v�ȃt�@�C����URL
    private static String       PAGE_URL            = "regist_idm.jsp";
    private static String       PAGE_URL_TEX1500    = "ht/";
    private int                 errorCode           = 0;

    /**
     * IDm�f�[�^�擾
     * 
     * @param idm �t�F���JID
     * @param response ���X�|���X
     * 
     */
    public void findData(String idm, int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        int nPremiumPoint = 0;
        boolean ret;
        boolean retCI;
        DataUserFelica duf;
        String seq = request.getParameter( "seq" );
        GenerateXmlHapiTouchFind gxTouch;
        HotelCi hc;
        ServletOutputStream stream = null;
        UserPointPay upp;
        hc = new HotelCi();
        duf = new DataUserFelica();
        gxTouch = new GenerateXmlHapiTouchFind();
        upp = new UserPointPay();
        retCI = false;
        int nUsePoint = 0;
        try
        {
            if ( idm == null )
            {
                idm = "";
            }
            if ( seq == null || seq.compareTo( "" ) == 0 || CheckString.numCheck( seq ) == false )
            {
                seq = "0";
            }
            stream = response.getOutputStream();
            if ( idm.equals( "" ) == false )
            {
                ret = duf.getUserData( idm );
                if ( ret != false )
                {
                    nPremiumPoint = upp.getNowPoint( duf.getUserId(), false );
                }
                retCI = hc.getCheckInBeforeData( hotelId, duf.getUserId() );
                if ( retCI != false )
                {
                    nUsePoint = hc.getHotelCi().getUsePoint();
                    gxTouch.setCiCode( hc.getHotelCi().getSeq() );
                    gxTouch.setIdm( hc.getHotelCi().getIdm() );
                }
                else
                {
                    gxTouch.setCiCode( 0 );
                    gxTouch.setIdm( "" );
                }
            }
            else if ( seq.equals( "" ) == false )
            {
                ret = hc.getData( hotelId, Integer.parseInt( seq ) );
                if ( ret )
                {
                    nPremiumPoint = upp.getNowPoint( hc.getHotelCi().getUserId(), false );
                    nUsePoint = hc.getHotelCi().getUsePoint();
                    /*
                     * if ( hc.getHotelCi().getRsvNo().equals( "" ) == false )
                     * {
                     * // �\��No�������Ă�����c����0�ŕԂ��ă}�C���g�p�������Ȃ��B
                     * nPremiumPoint = 0;
                     * }
                     * else
                     * {
                     * nPremiumPoint = upp.getNowPoint( hc.getHotelCi().getUserId(), false );
                     * }
                     */
                    // CiStatus�����X����A�����m�肵�Ă�����0��Ԃ�
                    if ( hc.getHotelCi().getCiStatus() >= 0 && hc.getHotelCi().getCiStatus() < 2 &&
                            hc.getHotelCi().getFixFlag() == 0 )
                    {
                        gxTouch.setCiCode( hc.getHotelCi().getSeq() );
                        gxTouch.setIdm( hc.getHotelCi().getIdm() );
                    }
                    else
                    {
                        gxTouch.setCiCode( 0 );
                        gxTouch.setIdm( "" );
                    }
                }
            }
            else
            {
                ret = false;
                gxTouch.setCiCode( 0 );
            }
            // xml�o�̓N���X�Ƀm�[�h���Z�b�g
            if ( ret != false )
            {
                // xml�o�̓N���X�ɒl���Z�b�g
                gxTouch.setResult( RESULT_OK );
                // ���b�Z�[�W�m�[�h�Ƀ|�C���g�^�O�A�|�C���g���Z�b�g
                gxTouch.setPoint( nPremiumPoint );
                gxTouch.setUsePoint( nUsePoint );
            }
            else
            {
                // xml�o�̓N���X�ɒl���Z�b�g
                gxTouch.setResult( RESULT_NO );
                gxTouch.setMessage( "�}�C�����擾�ł��܂���B" );
                // �}�C���擾�G���[
                errorCode = 0;
            }
            // XML�̏o��
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            Logging.info( xmlOut );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch findData]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch findData]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * �}�C���Ȃ����X�f�[�^�擾
     * 
     * @param idm �t�F���JID
     * @param hotelId
     * @param response ���X�|���X
     */
    public void visitData(String idm, int hotelId, int employeeCode, int registType, int kind, HttpServletRequest request, HttpServletResponse response)
    {
        int ciCode = 0;
        boolean ret = false;
        boolean retComeMile = false;
        DataUserBasic dub;
        DataUserFelica duf;
        DataMasterPoint dmp;
        GenerateXmlHapiTouchVisit gxTouch;
        ServletOutputStream stream = null;
        UserPointPay upp;
        UserPointPayTemp uppt;
        HotelCi hc;
        DataHotelFelicaNg dhfn = new DataHotelFelicaNg();
        String userId = "";
        DataHotelBasic dhb;
        String hotelName = ""; // ��hh_hotel_basic
        String hotenaviId = ""; // ��hh_hotel_basic
        String roomNo = "";
        int ciDate = 0;
        int ciTime = 0;
        duf = new DataUserFelica();
        dmp = new DataMasterPoint();
        gxTouch = new GenerateXmlHapiTouchVisit();
        upp = new UserPointPay();
        uppt = new UserPointPayTemp();
        hc = new HotelCi();
        dhb = new DataHotelBasic();
        // ���X�|���X���Z�b�g
        try
        {
            stream = response.getOutputStream();
            if ( idm == null || idm.equals( "" ) )
            {
                userId = request.getParameter( "userId" );
                if ( userId != null )
                {
                    ret = true;
                }
            }
            else
            {
                ret = duf.getUserData( idm );
                if ( ret )
                {
                    userId = duf.getUserId();
                }
            }
            // xml�o�̓N���X�Ƀm�[�h���Z�b�g
            if ( ret != false )
            {
                dub = new DataUserBasic();
                dub.getData( userId );
                if ( hotelId == 0 )
                {
                    hotelId = duf.getId();
                }
                // ���X�}�C�����ǉ��\���ǂ������`�F�b�N����i�ꎞ�̕��j
                retComeMile = uppt.getMasterPointExtNum( userId, POINT_KIND_RAITEN, COME_POINT, hotelId, dmp, Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ) );
                if ( retComeMile != false )
                {
                    // ���X�}�C�����ǉ��\���ǂ������`�F�b�N����
                    retComeMile = upp.getMasterPointExtNum( userId, POINT_KIND_RAITEN, COME_POINT, hotelId, dmp, Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ) );
                }
                // ���m��̃f�[�^��24���Ԉȓ��ɂ�������o�^���Ȃ��B
                ret = hc.getCheckInBeforeData( hotelId, userId );
                if ( ret == false )
                {
                    // �`�F�b�N�C���f�[�^�o�^
                    hc = hc.registCiData( userId, hotelId );
                    ciCode = hc.getHotelCi().getSeq();
                }
                else
                {
                    ciCode = hc.getHotelCi().getSeq();
                    if ( ciCode <= 0 )
                    {
                        // �`�F�b�N�C���f�[�^�擾���s
                        errorCode = 0;
                    }
                }
                /* ���\��f�[�^�Z�b�g�� */
                // �\��ԍ����Ȃ��ꍇ�͗\��̃f�[�^���擾���ɍs��
                if ( hc.getHotelCi().getRsvNo().equals( "" ) != false )
                {
                    HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();
                    hc = htRsvSub.getReserveData( userId, hotelId, hc );
                    gxTouch.setRsvNo( hc.getHotelCi().getRsvNo() );
                }
                else
                {
                    gxTouch.setRsvNo( hc.getHotelCi().getRsvNo() );
                }
                /* ���\��f�[�^�Z�b�g�� */
                // ���X�}�C�����t�^�\�ł����OK�t�^�s�\�ł����NG��Ԃ�
                if ( retComeMile != false )
                {
                    gxTouch.setResult( RESULT_OK );
                    if ( hc.getHotelCi().getRsvNo().equals( "" ) == false )
                    {
                        gxTouch.setMessage( "�����X���肪�Ƃ��������܂��B\n\n�}�C�������g�p�̍ۂ̓t�����g�ɐ\�������������B" );
                    }
                    else
                    {
                        gxTouch.setMessage( "�^�b�`���󂯕t���܂����B\n\n�t�����g�Ɂu �����ԍ� �v�����A�����������B" );
                    }
                }
                else
                {
                    gxTouch.setResult( RESULT_NG );
                    if ( hc.getHotelCi().getRsvNo().equals( "" ) == false )
                    {
                        gxTouch.setMessage( "�����X���肪�Ƃ��������܂��B\n\n�}�C�������g�p�̍ۂ̓t�����g�ɐ\�������������B" );
                    }
                    else
                    {
                        gxTouch.setMessage( "�{���̃^�b�`�͎󂯕t���ς݂ł��B\n\n�t�����g�Ɂu �����ԍ� �v�����A�����������B" );
                    }
                }
                gxTouch.setUserType( hc.getHotelCi().getUserType() );
                if ( ret == false )
                {
                    gxTouch.setCiResult( RESULT_OK );
                }
                else
                {
                    gxTouch.setCiResult( RESULT_NG );
                    if ( idm.equals( "" ) == false )
                    {
                        // NG�Ȃ̂�idm����ۑ����Ă���
                        dhfn.insertData( hotelId, kind, idm, "" );
                    }
                }
                gxTouch.setCiCode( ciCode );
                gxTouch.setUserId( hc.getHotelCi().getUserSeq() );
                gxTouch.setPoint( upp.getNowPoint( userId, false ) );
                gxTouch.setCiDate( hc.getHotelCi().getCiDate() );
                gxTouch.setCiTime( hc.getHotelCi().getCiTime() );
                gxTouch.setRoomNo( hc.getHotelCi().getRoomNo() );
                gxTouch.setAmountRate( hc.getHotelCi().getAmountRate() );
                gxTouch.setAmount( hc.getHotelCi().getAmount() );
                gxTouch.setUsePoint( hc.getHotelCi().getUsePoint() );
                gxTouch.setSlipNo( hc.getHotelCi().getSlipNo() );
                gxTouch.setUserType( hc.getHotelCi().getUserType() );
                ciDate = hc.getHotelCi().getCiDate();
                ciTime = hc.getHotelCi().getCiTime();
                roomNo = hc.getHotelCi().getRoomNo();
            }
            else
            {
                if ( idm.equals( "" ) == false )
                {
                    // NG�Ȃ̂�idm����ۑ����Ă���
                    dhfn.insertData( hotelId, kind, idm, "" );
                }
                Logging.info( "[HapiTouch.visitData]:�R�t������Ă��܂���Bidm:" + idm );
                if ( registType == 0 )
                {
                    gxTouch.setResult( RESULT_NO );
                    gxTouch.setMessage( "�R�t����Ă��܂���B" );
                    gxTouch.setRegistUrl( BASE_URL + PAGE_URL + "?method=" + METHOD_REGIST + "&idm=" + idm );
                    gxTouch.setCiResult( RESULT_NG );
                    gxTouch.setCiCode( 0 );
                }
                else if ( registType == 1 )
                {
                    FelicaMatching fm;
                    fm = new FelicaMatching();
                    ret = fm.getIdm( idm );
                    if ( ret != false )
                    {
                        gxTouch.setAccessKey1( fm.getFelicaDataInfo().getKey1() );
                        gxTouch.setAccessKey2( fm.getFelicaDataInfo().getKey2() );
                    }
                    else
                    {
                        gxTouch.setAccessKey1( "" );
                        gxTouch.setAccessKey2( "" );
                    }
                    gxTouch.setResult( RESULT_NO );
                    gxTouch.setMessage( "�R�t����Ă��܂���B" );
                    gxTouch.setRegistUrl( BASE_URL + PAGE_URL_TEX1500 );
                    // gxTouch.setRegistUrl( "http://happyhotel.jp/" + PAGE_URL_TEX1500 );
                    gxTouch.setCiResult( RESULT_NG );
                    gxTouch.setCiCode( 0 );
                }
            }
            StringBuffer requestUrl = request.getRequestURL();
            String requestUrlStr = requestUrl.toString();
            if ( !HotelIp.getFrontIp( hotelId ).equals( "" ) && requestUrlStr.indexOf( "happyhotel.jp" ) > 0 )
            {
                errorCode = HapiTouchErrorMessage.ERR_10303;
            }
            if ( errorCode != 0 )
            // �^�b�`PC����I�t���C���̓d���������Ă����ꍇ�ɂ̓G���[�����ɏ�������
            {
                // �z�e�i�rID���擾����
                if ( dhb.getData( hotelId ) != false )
                {
                    hotenaviId = dhb.getHotenaviId();
                    hotelName = dhb.getName();
                }
                Logging.info( "visitData Error:" + errorCode + " requestUrlStr:" + requestUrlStr, "visitData Error" );
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( roomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( ciCode );
                daeh.setUserId( userId );
                daeh.setEntryDate( ciDate );
                daeh.setEntryTime( ciTime );
                daeh.insertData();
                errorCode = 0;
            }
            // XML�̏o��
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            Logging.info( xmlOut );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch visitData]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch visitData]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * �t�F���JID�ƃ��[�UID�̕R�t������
     * 
     * @param reques ���N�G�X�g
     * @param response ���X�|���X
     */
    public void registUserFelica(HttpServletRequest request, HttpServletResponse response)
    {
        // ��`
        boolean ret;
        int carrierFlag;
        String paramUidLink;
        String paramIdm;
        String sendUrl;
        String oldUserId;
        String paramChange;
        String paramKey1;
        String paramKey2;
        DataUserFelica duf;
        duf = new DataUserFelica();
        ret = false;
        sendUrl = "";
        oldUserId = "";
        paramChange = request.getParameter( "change" );
        carrierFlag = UserAgent.getUserAgentType( request );
        dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
        paramUidLink = (String)request.getAttribute( "UID-LINK" );
        paramIdm = request.getParameter( "idm" );
        paramKey1 = request.getParameter( "key1" );
        paramKey2 = request.getParameter( "key2" );
        if ( (paramIdm == null) || (paramIdm.compareTo( "" ) == 0) )
        {
            paramIdm = "";
        }
        if ( (paramChange == null) || (paramChange.compareTo( "" ) == 0) || CheckString.numCheck( paramChange ) == false )
        {
            paramChange = "0";
        }
        if ( (paramKey1 == null) || (paramKey1.compareTo( "" ) == 0) || CheckString.numCheck( paramKey1 ) == false )
        {
            paramKey1 = "0";
        }
        if ( (paramKey2 == null) || (paramKey2.compareTo( "" ) == 0) || CheckString.numCheck( paramKey2 ) == false )
        {
            paramKey2 = "0";
        }
        try
        {
            // ���[�U�[���̎擾
            if ( dataLoginInfo_M2 == null )
            {
                if ( carrierFlag == DataMasterUseragent.CARRIER_SMARTPHONE )
                {
                    response.sendRedirect( "https://ssl.happyhotel.jp/phone/mypage/mypage_login_sp.jsp?idm=" + paramIdm );
                }
                else
                {
                    if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
                    {
                        response.sendRedirect( BASE_URL + "i/free/mymenu/hapitouch_join.jsp?" + paramUidLink );
                    }
                    else if ( carrierFlag == DataMasterUseragent.CARRIER_AU )
                    {
                        response.sendRedirect( BASE_URL + "au/free/mymenu/hapitouch_join.jsp?" + paramUidLink );
                    }
                    else if ( carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK )
                    {
                        response.sendRedirect( BASE_URL + "y/free/mymenu/hapitouch_join.jsp?" + paramUidLink );
                    }
                }
                return;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch ] Exception:" + e.toString() );
        }
        if ( (paramIdm.compareTo( "" ) != 0) && (paramIdm.length() == 16) )
        {
            if ( Integer.parseInt( paramChange ) == 1 )
            {
                // IDM�̈�v����f�[�^��S�č폜
                duf.deleteDataByIdm( paramIdm );
                // ���[�U���Ńf�[�^���擾����
                ret = duf.getData( dataLoginInfo_M2.getUserId() );
            }
            else
            {
                // �t�F���JID�Ńf�[�^���擾����
                ret = duf.getUserDataNoCheck( paramIdm );
            }
            if ( ret != false )
            {
                oldUserId = duf.getUserId();
            }
            duf.setUserId( dataLoginInfo_M2.getUserId() );
            duf.setIdm( paramIdm );
            duf.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            duf.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            duf.setDelFlag( 0 );
            if ( ret != false )
            {
                ret = duf.updateData( oldUserId );
                Logging.info( "[ActionHapiTouch duf.updateData]:" + ret );
            }
            else
            {
                duf.setUserId( dataLoginInfo_M2.getUserId() );
                ret = duf.insertData();
                Logging.info( "[ActionHapiTouch duf.insertData]:" + ret );
            }
        }
        // �A�N�Z�X�L�[1�A2�̃p�����[�^���Z�b�g����Ă�����
        else if ( paramKey1.equals( "0" ) == false && paramKey2.equals( "0" ) == false )
        {
            // �t�F���J�R�t���f�[�^����IDm���擾����
            DataSystemFelicaMatching dsfm = new DataSystemFelicaMatching();
            ret = dsfm.getData( paramKey1, paramKey2 );
            if ( ret != false )
            {
                paramIdm = dsfm.getIdm();
                // �t�F���JID�Ńf�[�^���擾����
                ret = duf.getUserDataNoCheck( paramIdm );
                if ( ret == false )
                {
                    // ���[�U���Ńf�[�^���擾����
                    ret = duf.getData( dataLoginInfo_M2.getUserId() );
                }
                if ( ret != false )
                {
                    oldUserId = duf.getUserId();
                }
                duf.setUserId( dataLoginInfo_M2.getUserId() );
                duf.setIdm( paramIdm );
                duf.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                duf.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                duf.setDelFlag( 0 );
                if ( ret != false )
                {
                    ret = duf.updateData( oldUserId );
                    Logging.info( "[ActionHapiTouch duf.updateData]:" + ret );
                }
                else
                {
                    duf.setUserId( dataLoginInfo_M2.getUserId() );
                    ret = duf.insertData();
                    Logging.info( "[ActionHapiTouch duf.insertData]:" + ret );
                }
                // �o�^�E�X�V�����܂���������AFelica�̕R�t���f�[�^��o�^�ς݂ɕύX����
                if ( ret != false )
                {
                    dsfm.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dsfm.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    dsfm.setRegistStatus( 1 );
                    ret = dsfm.updateData( paramKey1, paramKey2 );
                }
            }
        }
        if ( ret != false )
        {
            sendUrl = PAGE_URL;
            // �h�R�����̓\�t�g�o���N�̏ꍇ��uidLink��ǉ�����
            if ( (carrierFlag == DataMasterUseragent.CARRIER_DOCOMO) || (carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK) )
            {
                sendUrl += "?" + paramUidLink;
            }
            try
            {
                response.sendRedirect( sendUrl );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionHapiTouch registUserFelica] Exception:" + e.toString() );
            }
        }
        else
        {
            sendUrl = PAGE_URL;
            sendUrl += "?ret=" + ret;
            // �h�R�����̓\�t�g�o���N�̏ꍇ��uidLink��ǉ�����
            if ( (carrierFlag == DataMasterUseragent.CARRIER_DOCOMO) || (carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK) )
            {
                sendUrl += "&" + paramUidLink;
            }
            try
            {
                response.sendRedirect( sendUrl );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionHapiTouch registUserFelica] Exception:" + e.toString() );
            }
        }
    }

    /**
     * �z�e�����擾����
     * 
     * @param licenceKey ���C�Z���X�L�[
     * @param macAddr MAC�A�h���X
     * @param response ���X�|���X
     **/
    public void getHotelInfo(String licenceKey, String macAddr, HttpServletRequest request, HttpServletResponse response, boolean retAuth)
    {
        final int HOTEL_INFO_INTERVAL = 0;
        boolean ret;
        boolean terminalFlag = true;
        int newTouch = 0; // �V�^�b�`�[���Ή�:1
        String paramVersion;
        String paramGetRsvCount;
        HotelAuth ha;
        ServletOutputStream stream = null;
        String fileName = "";
        DataHotelAuth dha;
        DataHotelHapiTouchConfig dhhtc;
        DataRsvReserveBasic drrb;
        DataApHotelSetting dahs;
        DataApHotelTerminal daht;
        GenerateXmlHapiTouchHotelInfo gxTouch;
        FormOwnerRsvList dsp0; // form
        LogicOwnerRsvList lgLPC; // logic
        DataHotelMaster dhm;
        ha = new HotelAuth();
        dha = new DataHotelAuth();
        gxTouch = new GenerateXmlHapiTouchHotelInfo();
        String strFileURL32 = "";
        String strFileURL64 = "";
        String globalIp; // �n�s�z�e�^�b�`PC�̃O���[�o��IP���擾����B
        // ���X�|���X���Z�b�g
        try
        {
            paramVersion = request.getParameter( "version" );
            paramGetRsvCount = request.getParameter( "getRsvCount" );
            if ( paramVersion == null )
            {
                paramVersion = "";
            }
            if ( paramGetRsvCount == null || paramGetRsvCount.equals( "" ) != false || CheckString.numCheck( paramGetRsvCount ) == false )
            {
                paramGetRsvCount = "0";
            }
            if ( retAuth != false )
            {
                ret = dha.getData( licenceKey );
                if ( ret != false )
                {
                    dha.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dha.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    if ( paramVersion.equals( "" ) == false )
                    {
                        dha.setVersion( paramVersion );
                    }
                    dha.updateData( licenceKey );
                    ret = true;
                }
                else
                {
                    errorCode = HapiTouchErrorMessage.ERR_10101;
                }
            }
            else
            {
                ret = ha.registMacAddr( licenceKey, macAddr, paramVersion );
            }
            // OS�̎�ނŃC���X�g�[���t�@�C���̐ݒu�p�X��ύX����
            if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
            {
                strFileURL32 = FILE_URL32WIN;
                strFileURL64 = FILE_URL64WIN;
            }
            else
            {
                strFileURL32 = FILE_URL32;
                strFileURL64 = FILE_URL64;
            }
            if ( ret != false )
            {
                gxTouch.setResult( RESULT_OK );
                gxTouch.setMessage( "�F�؂��������܂����B" );
                fileName = FileUrl.getFileUrl( strFileURL32, FILE_NAME );
                if ( (fileName != null) && (fileName.compareTo( "" ) != 0) )
                {
                    if ( fileName.indexOf( fileName ) > 0 )
                    {
                        fileName = fileName.substring( fileName.indexOf( fileName ) );
                    }
                    gxTouch.setFile32( INSTALL_URL32 + fileName );
                }
                // 64�r�b�g��
                fileName = FileUrl.getFileUrl( strFileURL64, FILE_NAME );
                if ( (fileName != null) && (fileName.compareTo( "" ) != 0) )
                {
                    if ( fileName.indexOf( fileName ) > 0 )
                    {
                        fileName = fileName.substring( fileName.indexOf( fileName ) );
                    }
                    gxTouch.setFile64( INSTALL_URL64 + fileName );
                }
                if ( dha != null )
                {
                    dha = null;
                }
                dha = new DataHotelAuth();
                dha.getData( licenceKey );
                dahs = new DataApHotelSetting();
                if ( dahs.getData( dha.getId() ) != false )
                {
                    newTouch = dahs.getTerminalFlag();
                    if ( dahs.getTerminalFlag() == 0 )
                    {
                        terminalFlag = false;
                    }
                    else if ( dahs.getStartDate() > Integer.parseInt( DateEdit.getDate( 2 ) ) )
                    {
                        terminalFlag = false;
                    }
                    else if ( dahs.getEndDate() < Integer.parseInt( DateEdit.getDate( 2 ) ) )
                    {
                        terminalFlag = false;
                    }
                    else
                    {
                        terminalFlag = true;
                    }
                }
                else
                {
                    terminalFlag = false;
                }
                globalIp = request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr();
                dhm = new DataHotelMaster();
                if ( dhm.getData( dha.getId() ) != false )
                {
                    if ( dhm.getGlobalIp().equals( "0.0.0.0" ) )
                    {
                        dhm.setGlobalIp( globalIp );
                        dhm.updateData( dha.getId() );
                    }
                }
                gxTouch.setNewTouch( newTouch ); // �V�^�b�`�[���Ή��������ۂ�
                daht = new DataApHotelTerminal();
                if ( daht.getFrontTerminal( dha.getId() ) != false )
                {
                    if ( daht.getFrontTouchState() == 1 )
                    {
                        if ( daht.getLimitDate() < Integer.parseInt( DateEdit.getDate( 2 ) ) )
                        {
                            terminalFlag = false;
                        }
                        else if ( daht.getLimitDate() == Integer.parseInt( DateEdit.getDate( 2 ) ) && daht.getLimitTime() < Integer.parseInt( DateEdit.getTime( 1 ) ) )
                        {
                            terminalFlag = false;
                        }
                    }
                    if ( terminalFlag == false )
                    {
                        // �t�����g�^�b�`�𖳌��ɂ���
                        daht.setFrontTouchState( 0 );
                        daht.setLimitDate( 0 );
                        daht.setLimitTime( 0 );
                        daht.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        daht.setLimitTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        daht.updateFrontData( dha.getId() );
                        gxTouch.setFrontTouchState( 0 );
                        gxTouch.setFrontTouchLimitDate( 0 );
                        gxTouch.setFrontTouchLimitTime( 0 );
                    }
                    else
                    {
                        gxTouch.setFrontTouchState( daht.getFrontTouchState() );
                        gxTouch.setFrontTouchLimitDate( daht.getLimitDate() );
                        gxTouch.setFrontTouchLimitTime( daht.getLimitTime() );
                    }
                }
                dhhtc = new DataHotelHapiTouchConfig();
                if ( dhhtc.getData( dha.getId() ) != false )
                {
                    // YYYYMMDDHHMMSS����t�Ǝ����ɕ�����
                    gxTouch.setSyncCiUpdate( dhhtc.getCiUpdate() );
                    gxTouch.setSyncCiUptime( dhhtc.getCiUptime() );
                    gxTouch.setHotelInfoInterval( dhhtc.getHotelInfoInterval() );
                }
                else
                {
                    // YYYYMMDDHHMMSS����t�Ǝ����ɕ�����
                    gxTouch.setSyncCiUpdate( 0 );
                    gxTouch.setSyncCiUptime( 0 );
                    gxTouch.setHotelInfoInterval( HOTEL_INFO_INTERVAL );
                }
                // �T�[�o�[�̓��t���Z�b�g
                gxTouch.setDate( DateEdit.getDate( 2 ) );
                // �T�[�o�[�̎������Z�b�g
                gxTouch.setTime( DateEdit.getTime( 1 ) );
                // �N���W�b�g�����t���O���Z�b�g
                drrb = new DataRsvReserveBasic();
                drrb.getData( dha.getId() );
                gxTouch.setNoShowCredit( drrb.getNoshow_credit_flag() );
                // �N���W�b�g�����J�n�������Z�b�g
                gxTouch.setChargeStartTime( ReserveCommon.getChargeStartTime() );
                // �\��J�n���Ă����
                if ( (drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 1) && Integer.parseInt( paramGetRsvCount ) == 1 )
                {
                    dsp0 = new FormOwnerRsvList();
                    lgLPC = new LogicOwnerRsvList();
                    dsp0.setSelHotelID( dha.getId() );
                    dsp0.setDateFrom( DateEdit.getDate( 1 ) ); // �V�X�e�����t
                    dsp0.setDateTo( DateEdit.getDate( 1 ) ); // �V�X�e�����t
                    lgLPC.setDateFrom( DateEdit.getDate( 1 ) );
                    lgLPC.setDateTo( DateEdit.getDate( 1 ) );
                    lgLPC.setHotelId( dha.getId() );
                    ret = lgLPC.getDailyCount( dsp0, 5 );
                    if ( ret != false )
                    {
                        gxTouch.setRsvCount( lgLPC.getIdList().size() );
                    }
                    else
                    {
                        gxTouch.setRsvCount( 0 );
                    }
                }
            }
            else
            {
                gxTouch.setResult( RESULT_NG );
                gxTouch.setErrorCode( errorCode );
                gxTouch.setMessage( "�F�؂Ɏ��s���܂����B" );
            }
            // �����ŕ\������GenerateXml�N���X��
            String xmlOut = gxTouch.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch getHotelInfo]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch getHotelInfo]Exception:" + e.toString() );
                }
            }
        }
    }

    /***
     * �G���[���b�Z�[�W�o�͏���
     * 
     * @param root ���[�g�m�[�h�l�[��
     * @param message �G���[���b�Z�[�W
     * @param response ���X�|���X
     */
    public void getAvailableRooms(HttpServletResponse response)
    {
        GenerateXmlHapiTouchGetAvailableRooms gxTouch;
        ServletOutputStream stream = null;
        try
        {
            stream = response.getOutputStream();
            gxTouch = new GenerateXmlHapiTouchGetAvailableRooms();
            // xml�o�̓N���X�Ƀm�[�h���Z�b�g
            gxTouch.setResult( RESULT_OK );
            // XML�̏o��
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch errorData]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch errorData]Exception:" + e.toString() );
                }
            }
        }
    }

    /***
     * �G���[���b�Z�[�W�o�͏���
     * 
     * @param root ���[�g�m�[�h�l�[��
     * @param message �G���[���b�Z�[�W
     * @param response ���X�|���X
     */
    public void errorData(String root, String message, HttpServletResponse response)
    {
        GenerateXmlHapiTouchHotelInfo gxTouch;
        ServletOutputStream stream = null;
        try
        {
            stream = response.getOutputStream();
            gxTouch = new GenerateXmlHapiTouchHotelInfo();
            // xml�o�̓N���X�Ƀm�[�h���Z�b�g
            gxTouch.setResult( RESULT_DENY );
            gxTouch.setMessage( message );
            // XML�̏o��
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch errorData]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch errorData]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * MAC�A�h���X�F�؏���
     * 
     * @param licenceKey ���C�Z���X�L�[
     * @param macAddr MAC�A�h���X
     **/
    public int authMacAddr(String licenceKey, String macAddr)
    {
        int hotelId;
        boolean ret;
        DataHotelAuth dha;
        ret = false;
        hotelId = 0;
        dha = new DataHotelAuth();
        ret = dha.getValidData( licenceKey, macAddr );
        if ( ret != false )
        {
            hotelId = dha.getId();
        }
        return(hotelId);
    }

    /****
     * �`�F�b�N�C���f�[�^�ύX
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void modifyCi(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        int nowPoint;
        int nAmountPoint;
        int nVisitPoint = 0;
        boolean ret;
        boolean retIsUppt;
        boolean retIsUsed;
        boolean retUpdUppt;
        boolean retUpdUpp;
        boolean retUpdBko;
        boolean retInsCi;
        String paramIdm;
        String paramPrice;
        String paramPoint;
        String paramSlipNo;
        String paramRoomNo;
        String paramEmployeeCode;
        String paramSeq;
        String paramFix;
        String paramAllUse;
        // DataUserFelica duf;
        DataHotelBasic dhb;
        HotelCi hc;
        DataHotelCi dhc;
        GenerateXmlHapiTouchModifyCi gxTouch;
        ServletOutputStream stream = null;
        UserPointPayTemp uppt;
        UserPointPay upp;
        DataMasterPoint dmp;
        HapiTouchBko bko = new HapiTouchBko();
        // duf = new DataUserFelica();
        gxTouch = new GenerateXmlHapiTouchModifyCi();
        uppt = new UserPointPayTemp();
        upp = new UserPointPay();
        dhb = new DataHotelBasic();
        hc = new HotelCi();
        dhc = new DataHotelCi();
        nowPoint = 0;
        nAmountPoint = -1;
        ret = false;
        retIsUppt = false;
        retIsUsed = false;
        retUpdUppt = false;
        retUpdUpp = false;
        retUpdBko = false;
        retInsCi = false;
        paramIdm = request.getParameter( "idm" );
        paramPrice = request.getParameter( "price" );
        if ( paramPrice == null )
        {
            paramPrice = (String)request.getAttribute( "price" );
        }
        paramPoint = request.getParameter( "point" );
        paramSlipNo = request.getParameter( "slipNo" );
        paramRoomNo = request.getParameter( "roomNo" );
        paramEmployeeCode = request.getParameter( "employeeCode" );
        paramSeq = request.getParameter( "seq" );
        if ( paramSeq == null )
        {
            paramSeq = (String)request.getAttribute( "seq" );
        }
        paramAllUse = request.getParameter( "allUse" );
        paramFix = request.getParameter( "fix" );
        if ( paramFix == null )
        {
            paramFix = (String)request.getAttribute( "fix" );
        }
        Logging.info( "idm:" + paramIdm + ",Price:" + paramPrice + ",Point:" + paramPoint + ",SlipNo:" + paramSlipNo + ",roomNo:" + paramRoomNo + ",employeeCode:" + paramEmployeeCode + ",seq:" + paramSeq + ",allUse:" + paramAllUse + ",paramFix:"
                + paramFix, "" );
        String userId = ""; // ��hh_hotel_ci
        int ciStatus = 0; // ��hh_hotel_ci
        int ciFixFlag = 0; // ��hh_hotel_ci
        int ciAllUseFlag = 0; // ��hh_hotel_ci
        String ciRoomNo = ""; // ��hh_hotel_ci
        int ciSlipNo = 0; // ��hh_hotel_ci
        int ciUserSeq = 0; // ��hh_hotel_ci
        int ciVisitSeq = 0; // ��hh_hotel_ci
        int ciUsePoint = 0; // ��hh_hotel_ci
        int ciAllUsePoint = 0; // ��hh_hotel_ci
        int ciAmount = 0; // ��hh_hotel_ci
        double ciAmountRate = 0; // ��hh_hotel_ci
        int ciSubSeq = 0; // ��hh_hotel_ci
        String ciRsvNo = "";
        String hotelName = ""; // ��hh_hotel_basic
        String hotenaviId = ""; // ��hh_hotel_basic
        // ���X�|���X���Z�b�g
        try
        {
            // �p�����[�^�`�F�b�N
            if ( paramIdm == null )
            {
                paramIdm = "";
            }
            if ( (paramPrice == null) || (paramPrice.compareTo( "" ) == 0) || CheckString.numCheck( paramPrice ) == false )
            {
                paramPrice = "-1";
            }
            if ( (paramPoint == null) || (paramPoint.compareTo( "" ) == 0) || CheckString.numCheck( paramPoint ) == false )
            {
                paramPoint = "0";
            }
            if ( (paramSlipNo == null) || (paramSlipNo.compareTo( "" ) == 0) || CheckString.numCheck( paramSlipNo ) == false )
            {
                paramSlipNo = "0";
            }
            if ( paramRoomNo == null )
            {
                paramRoomNo = "";
            }
            else
            {
                paramRoomNo = new String( URLDecoder.decode( paramRoomNo, "8859_1" ).getBytes( "8859_1" ), "utf-8" );
            }
            if ( (paramEmployeeCode == null) || (paramEmployeeCode.compareTo( "" ) == 0) || CheckString.numCheck( paramEmployeeCode ) == false )
            {
                paramEmployeeCode = "0";
            }
            if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( (paramFix == null) || (paramFix.compareTo( "" ) == 0) || CheckString.numCheck( paramFix ) == false )
            {
                paramFix = "0";
            }
            if ( (paramAllUse == null) || (paramAllUse.compareTo( "" ) == 0) || CheckString.numCheck( paramAllUse ) == false )
            {
                paramAllUse = "0";
            }
            if ( Integer.parseInt( paramSeq ) > 0 )
            {
                // �z�e���`�F�b�N�C���f�[�^���擾����
                ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
                if ( ret != false )
                {
                    dhc = hc.getHotelCi();
                    userId = dhc.getUserId();
                    ciStatus = dhc.getCiStatus();
                    ciFixFlag = dhc.getFixFlag();
                    ciAllUseFlag = dhc.getAllUseFlag();
                    ciRoomNo = dhc.getRoomNo();
                    // �`�F�b�N�C���f�[�^���X�V����Ă��Ȃ��\�������邽�߁A�K�v�ȏ����Z�b�g����
                    if ( paramRoomNo.equals( "" ) == false && dhc.getRoomNo().equals( "" ) != false )
                    {
                        dhc.setRoomNo( paramRoomNo );
                    }
                    if ( Integer.parseInt( paramSlipNo ) > 0 && dhc.getSlipNo() == 0 )
                    {
                        dhc.setSlipNo( Integer.parseInt( paramSlipNo ) );
                    }
                    ciSlipNo = dhc.getSlipNo();
                    ciUserSeq = dhc.getUserSeq();
                    ciVisitSeq = dhc.getVisitSeq();
                    ciUsePoint = dhc.getUsePoint();
                    ciAllUsePoint = dhc.getAllUsePoint();
                    ciAmount = dhc.getAmount();
                    ciAmountRate = dhc.getAmountRate();
                    ciSubSeq = dhc.getSubSeq();
                    ciRsvNo = dhc.getRsvNo();
                }
            }
            // �z�e�i�rID���擾����
            if ( dhb.getData( hotelId ) != false )
            {
                hotenaviId = dhb.getHotenaviId();
                hotelName = dhb.getName();
            }
            Logging.info( "STEP00.1", "ret:" + ret + ",status:" + ciStatus + ",userId:" + userId + ",rsvNo:" + ciRsvNo + ",ciFixFlag:" + ciFixFlag + ",paramFix:" + paramFix );
            StringBuffer requestUrl = request.getRequestURL();
            String requestUrlStr = requestUrl.toString();
            if ( !HotelIp.getFrontIp( hotelId ).equals( "" ) && requestUrlStr.indexOf( "happyhotel.jp" ) > 0 )
            {
                errorCode = HapiTouchErrorMessage.ERR_10514;
            }
            if ( errorCode != 0 )
            // �^�b�`PC����I�t���C���̓d���������Ă����ꍇ�ɂ̓G���[�����ɏ�������
            {
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( paramRoomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                daeh.setUserId( userId );
                daeh.insertData();
                errorCode = 0;
            }
            // ���X����̏ꍇ�͉����ύX�����Ȃ�
            if ( ciStatus >= 2 )
            {
                gxTouch.setResult( RESULT_NG );
                gxTouch.setCiStatus( ciStatus );
                if ( ciStatus == 4 )
                {
                    ciStatus = 3;
                    dhc.setCiStatus( ciStatus );
                    dhc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dhc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    ret = dhc.updateData( hotelId, Integer.parseInt( paramSeq ), ciSubSeq );
                }
            }
            else
            {

                if ( ret != false )
                {
                    if ( !HotelIp.getFrontIp( hotelId ).equals( "" ) && requestUrlStr.indexOf( "happyhotel.jp" ) == -1 && !paramPoint.equals( "0" ) && request.getParameter( "code" ) != null && request.getParameter( "uuid" ) != null )
                    { // �A�������ŁA�^�b�`PC����̃}�C���g�p�͋����Ȃ�
                        gxTouch.setResult( RESULT_NG );
                        gxTouch.setUseResult( RESULT_NG );
                        errorCode = HapiTouchErrorMessage.ERR_10516;
                        gxTouch.setErrorCode( errorCode );
                        dhc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dhc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        dhc.updateData( hotelId, Integer.parseInt( paramSeq ), ciSubSeq );
                    }
                    else if ( !ciRsvNo.equals( "" ) && Integer.parseInt( paramPoint ) != 0 )
                    {
                        gxTouch.setResult( RESULT_NG );
                        gxTouch.setUseResult( RESULT_NG );
                        errorCode = HapiTouchErrorMessage.ERR_10512;
                        gxTouch.setErrorCode( errorCode );
                    }
                    else
                    {
                        if ( UserLoginInfo.checkMileUser( userId ) != false )// �}�C�����Z�Ώ� 2015.3.26sakurai�ǉ�
                        {
                            // ret = duf.getUserData( paramIdm );
                            // ���g���Ă��Ȃ��̂ŃR�����g�A�E�g 2015.04.17 sakurai
                            // duf.getData( dhc.getUserId() );
                            // ���݂̃|�C���g���擾
                            nowPoint = upp.getNowPoint( userId, false );
                            // �S�g�p�t���O�̃��N�G�X�g�ŁA�`�F�b�N�C���f�[�^�ihh_hotel_ci�j�̑S�g�p�t���O�������Ă��Ȃ��ꍇ�̂�
                            if ( Integer.parseInt( paramAllUse ) == 1 && ciAllUseFlag == 0 )
                            {
                                // �S�}�C���g�p����
                                if ( this.allUsePoint( userId, hc, Integer.parseInt( paramEmployeeCode ), Integer.parseInt( paramPoint ) ) != false )
                                {
                                    // �S�}�C���g�p���������܂���������A�X�V��̃`�F�b�N�C���f�[�^��ǂݍ���
                                    ciAllUseFlag = 1;
                                    if ( hc.getData( hotelId, Integer.parseInt( paramSeq ) ) != false )
                                    {
                                        dhc = hc.getHotelCi();
                                        ciAllUseFlag = dhc.getAllUseFlag();
                                        ciUsePoint = dhc.getUsePoint();
                                        ciAmount = dhc.getAmount();
                                    }
                                }
                            }
                            // �ǉ����X�V���𔻒f����
                            // �`�F�b�N�C���f�[�^�ihh_hotel_ci�j�ɕ����ԍ����o�^�ς݂ŁA���N�G�X�g�̕����ԍ�������Ă�����C���T�[�g
                            if ( ciRoomNo.equals( "" ) == false && paramRoomNo.equals( "" ) == false && ciRoomNo.equals( paramRoomNo ) == false )
                            {
                                retInsCi = true;
                            }
                            // �`�F�b�N�C���f�[�^�ihh_hotel_ci�j�ɓ`�[�ԍ����o�^�ς݂ŁA���N�G�X�g�̓`�[�ԍ�������Ă�����C���T�[�g
                            else if ( ciSlipNo > 0 && Integer.parseInt( paramSlipNo ) > 0 && Integer.parseInt( paramSlipNo ) != ciSlipNo )
                            {
                                retInsCi = true;
                            }
                            Logging.info( "STEP01", "" + retInsCi );
                            // �S�z���p�����Ă��Ȃ��ꍇ�ŁA
                            if ( ciAllUseFlag == 0 )
                            {
                                Logging.info( "STEP02", "ciUsePoint:" + ciUsePoint + ",paramPoint:" + paramPoint + ",paramPrice:" + paramPrice );
                                // �|�C���g�������Ă�����}�C���g�p
                                if ( (Integer.parseInt( paramPoint ) > 0 && Integer.parseInt( paramPoint ) != ciUsePoint)
                                        || (ciUsePoint > 0 && Integer.parseInt( paramPrice ) >= 0/* && ciUsePoint > Integer.parseInt( paramPrice ) */) )
                                {
                                    retIsUsed = true;
                                    // ���z���g�p�}�C�����傫���ꍇ�́A�g�p�}�C�������z�Ɠ����ɂ���B
                                    if ( ciUsePoint > 0 && Integer.parseInt( paramPrice ) >= 0 && ciUsePoint > Integer.parseInt( paramPrice ) )
                                    {
                                        dhc.setUsePoint( Integer.parseInt( paramPrice ) );
                                        ciUsePoint = Integer.parseInt( paramPrice );
                                        paramPoint = paramPrice;
                                    }
                                    Logging.info( "STEP03.0", "ciUsePoint:" + ciUsePoint + ",paramPoint:" + paramPoint );
                                    if ( Integer.parseInt( paramPoint ) != 0 )
                                    {
                                        if ( Integer.parseInt( paramPoint ) != ciUsePoint )
                                        {
                                            ciUsePoint = Integer.parseInt( paramPoint );
                                        }
                                    }
                                    if ( ciUsePoint != 0 )
                                    {
                                        // ���Ɏg�p�}�C���f�[�^�����邩�ǂ������m�F����
                                        retIsUppt = uppt.getUserPointHistory( userId, hotelId, POINT_KIND_WARIBIKI, ciUserSeq, ciVisitSeq );
                                        if ( retIsUppt != false )
                                        {
                                            // �f�[�^�����邽�߁A�g�p�}�C�����X�V
                                            retUpdUppt = uppt.setUsePointUpdate( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                            if ( retUpdUppt != false ) // �\��No�������Ă�����g�p�}�C���͍X�V���Ȃ��B
                                            {
                                                // hh_user_point_pay�̎g�p�}�C���̍X�V
                                                retUpdUpp = upp.setUsePointUpdate( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                            }
                                        }
                                        else
                                        {
                                            // �f�[�^���Ȃ����߁Ahh_user_point_pay_temp�Ɏg�p�}�C����ǉ�
                                            retUpdUppt = uppt.setUsePoint( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                            if ( retUpdUppt != false )
                                            {
                                                if ( !dhc.getRsvNo().equals( "" ) )
                                                {
                                                    // ���O�\��̏ꍇ�́A���łɎg�p�}�C���������Ă���̂ŁAuser_seq ��visit_seq�݂̂��X�V����
                                                    retUpdUpp = upp.setUsePointUpdate( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                                }
                                                else
                                                {
                                                    // hh_user_point_pay�Ɏg�p�}�C���̒ǉ�
                                                    retUpdUpp = upp.setUsePoint( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                                }
                                            }
                                        }
                                        Logging.info( "STEP03", "retUpdUpp:" + retUpdUpp + ",usePoint:" + dhc.getUsePoint() );
                                        // �g�p�}�C�����X�V�E�ǉ��ł������ǂ���
                                        if ( retUpdUpp != false )
                                        {
                                            // OK���������������̃|�C���g���擾
                                            nowPoint = upp.getNowPoint( userId, false );
                                            gxTouch.setUseResult( RESULT_OK );
                                        }
                                        else
                                        {
                                            gxTouch.setUseResult( RESULT_NG );
                                            // �}�C���g�p���s
                                            errorCode = HapiTouchErrorMessage.ERR_10505;
                                        }
                                    }
                                }
                                // �����������Ă�����}�C���t�^���s��
                                if ( Integer.parseInt( paramPrice ) >= 0 )
                                {
                                    retIsUppt = uppt.getUserPointHistory( userId, hotelId, POINT_KIND_RAITEN, ciUserSeq, ciVisitSeq );
                                    if ( retIsUppt == false )
                                    {
                                        // ���X�n�s�[���Z(�ꎞ�ۑ��e�[�u���ɕۑ�����)
                                        nVisitPoint = uppt.setVisitPoint( userId, COME_POINT, nowPoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                        if ( nVisitPoint > 0 )
                                        {
                                            gxTouch.setVisitResult( RESULT_OK );
                                            dhc.setVisitPoint( nVisitPoint );
                                        }
                                        else
                                        {
                                            gxTouch.setVisitResult( RESULT_NG );
                                            // ���X�}�C���t�^�G���[
                                            if ( nVisitPoint < 0 )
                                            {
                                                errorCode = HapiTouchErrorMessage.ERR_10504;
                                            }
                                        }
                                    }
                                    Logging.info( "STEP05", "nVisitPoint:" + nVisitPoint );
                                    // �`�F�b�N�C���f�[�^���X�V����Ă��Ȃ��\�������邽�߁A�K�v�ȏ����Z�b�g����
                                    if ( paramRoomNo.equals( "" ) == false )
                                    {
                                        dhc.setRoomNo( paramRoomNo );
                                    }
                                    if ( Integer.parseInt( paramSlipNo ) > 0 )
                                    {
                                        dhc.setSlipNo( Integer.parseInt( paramSlipNo ) );
                                    }
                                    // �|�C���g�����̊m�F
                                    retIsUppt = uppt.getUserPointHistory( userId, hotelId, POINT_KIND_RIYOU, ciUserSeq, ciVisitSeq );
                                    if ( retIsUppt != false )
                                    {
                                        // �ߋ��f�[�^������΃C���T�[�g�t���O��true�ɂ���
                                        // retInsCi = true;
                                        // �f�[�^������ꍇ�A�|�C���g���X�V
                                        nAmountPoint = uppt.setAmountPointUpdate( userId, AMOUNT_POINT, nowPoint, Integer.parseInt( paramPrice ), Integer.parseInt( paramEmployeeCode ), dhc );
                                    }
                                    else
                                    {
                                        // �f�[�^���Ȃ��ꍇ�A�|�C���g��ǉ�
                                        nAmountPoint = uppt.setAmountPoint( userId, AMOUNT_POINT, nowPoint, Integer.parseInt( paramPrice ), Integer.parseInt( paramEmployeeCode ), dhc );
                                        // nAmountPoint>=0�̂Ƃ�
                                        // �V�K�ɋ��z���������̂�PUSH����
                                        if ( nAmountPoint >= 0 && requestUrlStr.indexOf( "happyhotel" ) == -1 && DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -2 ) < dhc.getCiDate() )
                                        {
                                            TokenUser tk = new TokenUser();
                                            tk.sendCheckOutMessage( userId, hotelId );
                                        }
                                    }
                                    Logging.info( "STEP06", "nAmountPoint:" + nAmountPoint );
                                    if ( nAmountPoint >= 0 )
                                    {
                                        gxTouch.setAmountResult( RESULT_OK );
                                        // �|�C���g����������ꍇ
                                        if ( retIsUppt != false && retInsCi != false )
                                        {
                                            if ( retInsCi != false )
                                            {
                                                // �}�C�i�X�̗���ǉ�
                                                hc.setMinusHistory( dhc, Integer.parseInt( paramEmployeeCode ) );
                                                // �ēx�ŐV�̃f�[�^���擾����
                                                if ( hc.getData( hotelId, Integer.parseInt( paramSeq ) ) != false )
                                                {
                                                    dhc = hc.getHotelCi();
                                                    userId = dhc.getUserId();
                                                    ciStatus = dhc.getCiStatus();
                                                    ciFixFlag = dhc.getFixFlag();
                                                    ciAllUseFlag = dhc.getAllUseFlag();
                                                    ciRoomNo = dhc.getRoomNo();
                                                    ciSlipNo = dhc.getSlipNo();
                                                    ciUserSeq = dhc.getUserSeq();
                                                    ciVisitSeq = dhc.getVisitSeq();
                                                    ciUsePoint = dhc.getUsePoint();
                                                    ciAllUsePoint = dhc.getAllUsePoint();
                                                    ciAmount = dhc.getAmount();
                                                    ciAmountRate = dhc.getAmountRate();
                                                    ciSubSeq = dhc.getSubSeq();
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        gxTouch.setAmountResult( RESULT_NG );
                                        // �}�C���t�^�G���[
                                        errorCode = HapiTouchErrorMessage.ERR_10506;
                                    }
                                    Logging.info( "STEP07", "retUpdBko:" + retUpdBko + ",retIsUppt:" + retIsUppt + ",retInsCi:" + retInsCi );
                                }
                                // ���z�m��̃X�e�[�^�X���������ꍇ�͎g�p�}�C�����m�F����
                                if ( Integer.parseInt( paramFix ) == 1 )
                                {
                                    if ( ciUsePoint > 0 && ciAmount >= 0 )
                                    {
                                        if ( ciUsePoint > (Integer.parseInt( paramPrice ) >= 0 ? Integer.parseInt( paramPrice ) : ciAmount) )
                                        {
                                            dhc.setUsePoint( Integer.parseInt( paramPrice ) >= 0 ? Integer.parseInt( paramPrice ) : ciAmount );
                                            ciUsePoint = Integer.parseInt( paramPrice ) >= 0 ? Integer.parseInt( paramPrice ) : ciAmount;
                                        }
                                        // ���Ɏg�p�}�C���f�[�^�����邩�ǂ������m�F����
                                        retIsUppt = uppt.getUserPointHistory( userId, hotelId, POINT_KIND_WARIBIKI, ciUserSeq, ciVisitSeq );
                                        if ( retIsUppt != false )
                                        {
                                            // �ߋ��f�[�^������΃C���T�[�g�t���O��true�ɂ���
                                            // = true;
                                            // �f�[�^�����邽�߁A�g�p�}�C�����X�V
                                            retUpdUppt = uppt.setUsePointUpdate( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                            if ( retUpdUppt != false )
                                            {
                                                // hh_user_point_pay�Ɏg�p�}�C���̒ǉ�
                                                retUpdUpp = upp.setUsePointUpdate( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                            }
                                        }
                                        else
                                        {
                                            // �f�[�^���Ȃ����߁Ahh_user_point_pay_temp�Ɏg�p�}�C����ǉ�
                                            retUpdUppt = uppt.setUsePoint( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                            if ( retUpdUppt != false )
                                            {
                                                if ( !dhc.getRsvNo().equals( "" ) )
                                                {
                                                    // ���O�\��̏ꍇ�́A���łɎg�p�}�C���������Ă���̂ŁAuser_seq ��visit_seq�݂̂��X�V����
                                                    retUpdUpp = upp.setUsePointUpdate( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                                }
                                                else
                                                {
                                                    // hh_user_point_pay�Ɏg�p�}�C���̒ǉ�
                                                    retUpdUpp = upp.setUsePoint( userId, USE_POINT, nowPoint, ciUsePoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                                }
                                            }
                                        }
                                    }
                                    Logging.info( "STEP08", "paramPoint:" + paramPoint + ", ciUsePoint:" + ciUsePoint + ", ciAmount:" + ciAmount );
                                }
                            }
                            else
                            // �S�z�g�p
                            {
                                if ( Integer.parseInt( paramFix ) == 0 )
                                {
                                    dmp = new DataMasterPoint();
                                    // �|�C���g�������Ă�����}�C���g�p
                                    if ( Integer.parseInt( paramPoint ) > 0 && Integer.parseInt( paramPoint ) != ciUsePoint )
                                    {
                                        retIsUsed = true;
                                    }
                                    // �����������Ă�����}�C���t�^���s��
                                    if ( Integer.parseInt( paramPrice ) >= 0 )
                                    {
                                        nAmountPoint = (int)(ciAmountRate * Integer.parseInt( paramPrice ) / 100);
                                        // ���X�}�C�����ǉ��\���ǂ������`�F�b�N����i�ꎞ�̕��j
                                        if ( uppt.getMasterPointExtNum( userId, POINT_KIND_RAITEN, COME_POINT, hotelId, dmp, Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ) ) != false )
                                        {
                                            // ���X�}�C�����ǉ��\���ǂ������`�F�b�N����
                                            if ( upp.getMasterPointExtNum( userId, POINT_KIND_RAITEN, COME_POINT, hotelId, dmp, Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ) ) != false )
                                            {
                                                nVisitPoint = 1;
                                            }
                                        }
                                    }
                                    Logging.info( "STEP09", "paramPoint:" + paramPoint + ", ciUsePoint:" + ciUsePoint );
                                }
                                // fix=1�ɂȂ�����
                                else if ( Integer.parseInt( paramFix ) == 1 )
                                {
                                    // �}�C�����g�p����
                                    ret = this.returnUsePoint( userId, hc, Integer.parseInt( paramEmployeeCode ) );
                                    if ( ret != false )
                                    {
                                        if ( ciAllUsePoint >= (Integer.parseInt( paramPrice ) >= 0 ? Integer.parseInt( paramPrice ) : ciAmount) )
                                        {
                                            dhc.setUsePoint( (Integer.parseInt( paramPrice ) >= 0 ? Integer.parseInt( paramPrice ) : ciAmount) );
                                        }
                                        else
                                        {
                                            dhc.setUsePoint( ciAllUsePoint );
                                        }
                                    }
                                    // ���X�}�C����t�^����
                                    nVisitPoint = uppt.setVisitPoint( userId, COME_POINT, nowPoint, Integer.parseInt( paramEmployeeCode ), dhc );
                                    if ( nVisitPoint > 0 )
                                    {
                                        gxTouch.setVisitResult( RESULT_OK );
                                        dhc.setVisitPoint( nVisitPoint );
                                    }
                                    // �}�C���t�^�̗�����ǉ�
                                    nAmountPoint = uppt.setAmountPoint( userId, AMOUNT_POINT, nowPoint, ciAmount, Integer.parseInt( paramEmployeeCode ), dhc );
                                    // �}�C����t�^����
                                    if ( nAmountPoint > 0 )
                                    {
                                        paramPoint = Integer.toString( ciAmount );
                                    }
                                    Logging.info( "STEP10", "ciAmount:" + ciAmount + ", ciAllUsePoint:" + ciAllUsePoint );
                                }
                            }
                        }
                        else
                        {
                            ret = true;
                            dhc.setCiStatus( 3 ); // �����O�C������̏ꍇ�͂Ȃ��������Ƃɂ���
                            errorCode = HapiTouchErrorMessage.ERR_10513;
                        }

                        // ���݂̃|�C���g���Z�b�g nowPoint = upp.getNowPoint( userId, false );
                        gxTouch.setPoint( nowPoint );
                        if ( ret != false )
                        {
                            dhc = hc.getHotelCi();
                            // �S�}�C���g�p�̏ꍇ�́A���Z���ɗ��X�}�C�����m�肷��B�S�}�C���g�p�łȂ���Η��X�}�C�������������邪�A�S�}�C���g�p�̏ꍇ�́AFIX���݂̂ɏ���������
                            if ( nVisitPoint > 0 && (ciAllUseFlag == 0 || Integer.parseInt( paramFix ) == 1) )
                            {
                                dhc.setVisitPoint( nVisitPoint );
                            }
                            dhc.setUserId( userId );
                            dhc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            dhc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            if ( paramRoomNo.equals( "" ) == false )
                            {
                                dhc.setRoomNo( paramRoomNo );
                            }
                            if ( Integer.parseInt( paramSlipNo ) > 0 )
                            {
                                dhc.setSlipNo( Integer.parseInt( paramSlipNo ) );
                            }
                            if ( nAmountPoint > 0 )
                            {
                                dhc.setCiStatus( 1 );
                                if ( Integer.parseInt( paramPrice ) > 0 )
                                {
                                    dhc.setAmount( Integer.parseInt( paramPrice ) );
                                    ciAmount = Integer.parseInt( paramPrice );
                                }
                                dhc.setAddPoint( nAmountPoint );
                                dhc.setAddDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                dhc.setAddTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                dhc.setAddHotenaviId( hotenaviId );
                                dhc.setAddEmployeeCode( Integer.parseInt( paramEmployeeCode ) );
                            }
                            else if ( nAmountPoint == 0 )
                            {
                                dhc.setCiStatus( 1 );
                                if ( Integer.parseInt( paramPrice ) > 0 )
                                {
                                    dhc.setAmount( Integer.parseInt( paramPrice ) );
                                    ciAmount = Integer.parseInt( paramPrice );
                                }
                                dhc.setAddHotenaviId( hotenaviId );
                                dhc.setAddEmployeeCode( Integer.parseInt( paramEmployeeCode ) );
                            }
                            if ( retIsUsed != false )
                            {
                                // HotelCi��o�^����
                                if ( Integer.parseInt( paramPoint ) > 0 )
                                {
                                    dhc.setUsePoint( Integer.parseInt( paramPoint ) );
                                }
                                dhc.setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                dhc.setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                dhc.setUseHotenaviId( hotenaviId );
                                dhc.setUseEmployeeCode( Integer.parseInt( paramEmployeeCode ) );
                            }
                            if ( Integer.parseInt( paramFix ) == 1 )
                            {
                                dhc.setFixFlag( 1 );
                            }
                            Logging.info( "STEP11", "retIsUsed:" + retIsUsed + ",paramPoint:" + paramPoint + ",retInsCi:" + retInsCi + ",paramFix:" + paramFix );
                            // �C���T�[�g�Ώۂ�������A�}�ԍ���ǉ����ăC���T�[�g
                            if ( retInsCi != false )
                            {
                                // Logging.info( "" + dhc.getSubSeq(), "" + ciSubSeq );
                                dhc.setSubSeq( ciSubSeq + 1 );
                                ret = dhc.insertData();
                                if ( ret != false )
                                {
                                    hc.registTouchCi( dhc, retIsUsed );
                                }
                                /** �n�s�z�e�^�b�`�[���p�ɒǉ� **/
                                else
                                {
                                    // �`�F�b�N�C���f�[�^�ǉ��G���[
                                    errorCode = HapiTouchErrorMessage.ERR_10503;
                                }
                            }
                            else
                            {
                                // HotelCi���X�V����
                                Logging.info( "dhc", "slipNo:" + dhc.getSlipNo() + ",AddPoint:" + dhc.getAddPoint() + ",VisitPoint:" + dhc.getVisitPoint() );
                                ret = dhc.updateData( hotelId, Integer.parseInt( paramSeq ), ciSubSeq );
                                if ( ret != false )
                                {
                                    hc.registTouchCi( dhc, retIsUsed );
                                }
                                /** �n�s�z�e�^�b�`�[���p�X�V **/
                                else
                                {
                                    // �`�F�b�N�C���f�[�^�X�V�G���[
                                    errorCode = HapiTouchErrorMessage.ERR_10502;
                                }
                            }
                            if ( ret != false )
                            {
                                if ( !dhc.getRsvNo().equals( "" ) )
                                {
                                    /** �\��{�[�i�X�}�C���̍X�V */
                                    if ( uppt.setAddBonusMile( userId, nowPoint, Integer.parseInt( paramEmployeeCode ), dhc ) != false )
                                    {
                                    }
                                }
                            }
                            if ( ret != false )
                            {
                                Logging.info( "STEP12", "paramSlipNo:" + paramSlipNo + ", nAmountPoint:" + nAmountPoint + ", ciAmount:" + ciAmount );
                                // �������o�^����Ă���A���z�̕ύX���Ȃ�������X�V���s���B
                                if ( (paramRoomNo.equals( "" ) == false || Integer.parseInt( paramSlipNo ) > 0) && (nAmountPoint == 0 && ciAmount > 0) )
                                {
                                    // �z�e�����p�}�C���X�V
                                    nAmountPoint = uppt.setAmountPointUpdate( userId, AMOUNT_POINT, nowPoint, ciAmount, Integer.parseInt( paramEmployeeCode ), dhc );
                                    if ( nAmountPoint <= 0 )
                                    {
                                        // �t�^�}�C���X�V�G���[
                                        errorCode = HapiTouchErrorMessage.ERR_10506;
                                    }
                                }
                                if ( Integer.parseInt( paramFix ) == 1 )
                                {
                                    if ( !dhc.getRsvNo().equals( "" ) ) // �\�񕔉��݌ɂ��J������
                                    {
                                        LogicOwnerRsvCheckIn logic = new LogicOwnerRsvCheckIn();
                                        if ( logic.releaseRoomRemaindar( hotelId, dhc.getRoomNo(), dhc.getRsvNo(), dhc.getCiDate() ) )
                                        {
                                            if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) && requestUrlStr.indexOf( "happyhotel" ) == -1 && DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -2 ) < dhc.getCiDate() )
                                            {
                                                RsvList rl = new RsvList();
                                                // �v�m�F
                                                if ( rl.getData( hotelId, dhc.getCiDate(), dhc.getCiDate(), 0, dhc.getRsvNo(), 1 ) != false )
                                                {
                                                    rl.sendToHost( hotelId );
                                                }
                                            }
                                        }
                                        else
                                        {
                                            DataApErrorHistory daeh = new DataApErrorHistory();
                                            daeh.setErrorCode( HapiTouchErrorMessage.ERR_10515 );
                                            daeh.setErrorSub( 0 );
                                            daeh.setHotelName( hotelName );
                                            daeh.setHotenaviId( hotenaviId );
                                            daeh.setRoomName( paramRoomNo );
                                            daeh.setId( hotelId );
                                            daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                            daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                            daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                                            daeh.setUserId( userId );
                                            daeh.insertData();
                                        }
                                    }
                                    // TokenUser tk = new TokenUser();
                                    // tk.sendCheckOutMessage( userId, hotelId );
                                }

                                gxTouch.setResult( RESULT_OK );
                                gxTouch.setCiStatus( ciStatus );
                            }
                            else
                            {
                                gxTouch.setResult( RESULT_NG );
                                gxTouch.setCiStatus( 0 );
                            }
                        }
                        else
                        {
                            gxTouch.setResult( RESULT_NG );
                            gxTouch.setCiStatus( 0 );
                        }
                    }
                }
                else
                {
                    if ( ciFixFlag == 1 )
                    {
                        gxTouch.setResult( RESULT_NG );
                        gxTouch.setCiStatus( ciStatus );
                    }
                    else
                    {
                        gxTouch.setResult( RESULT_NO );
                        gxTouch.setErrorCode( HapiTouchErrorMessage.ERR_10501 );
                        gxTouch.setRegistUrl( BASE_URL + PAGE_URL + "?method=" + METHOD_REGIST + "&idm=" + paramIdm );
                    }
                }
            }
            if ( errorCode == 0 && ReserveCommon.isSyncReserve( hotelId ) )
            {
                if ( dhc.getRsvNo().equals( "" ) == false && !ciRoomNo.equals( paramRoomNo ) )
                {
                    // �����ւ����s��
                    HapiTouchRsv.rsvRoomChange( hotelId, dhc.getRsvNo(), paramRoomNo );
                }
            }
            if ( errorCode == 0 )
            {
                // �o�b�N�I�t�B�X�f�[�^�X�V
                retUpdBko = bko.updateBkoData( userId, hotelId, dhc );
                // �o�b�N�I�t�B�X�}�C���X�V�G���[
                if ( retUpdBko == false )
                {
                    errorCode = HapiTouchErrorMessage.ERR_10510;
                }
            }
            if ( errorCode != 0 )
            // �G���[���������Ă����ꍇ�ɂ̓G���[�����ɏ�������
            {
                Logging.info( "ModifyCi Error:" + errorCode, "ModifyCi Error" );
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( paramRoomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                daeh.setUserId( userId );
                daeh.insertData();
            }
            if ( requestUrlStr.indexOf( "owner.hotenavi.com" ) == -1 )
            {
                stream = response.getOutputStream();
                // XML�̏o��
                String xmlOut = gxTouch.createXml();
                Logging.info( xmlOut );
                ServletOutputStream out = null;
                out = response.getOutputStream();
                response.setContentType( CONTENT_TYPE );
                out.write( xmlOut.getBytes( ENCODE ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch modifyCi]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch modifyCi]Exception:" + e.toString() );
                }
            }
        }
    }

    /****
     * �`�F�b�N�C���f�[�^�ύX
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void correctCi(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        int nowPoint;
        int nAmountPoint;
        int nVisitPoint = 0;
        boolean ret;
        boolean retUse;
        boolean retData;
        String hotenaviId = "";
        String hotelName = "";
        String userId = "";
        String paramIdm;
        String paramPrice;
        String paramPoint;
        String paramSlipNo;
        String paramRoomNo;
        String ciRoomNo = "";
        String paramEmployeeCode;
        String paramSeq;
        String paramFix;
        String paramAllUse;
        DataUserFelica duf;
        DataHotelBasic dhb;
        HotelCi hc;
        GenerateXmlHapiTouchModifyCi gxTouch;
        ServletOutputStream stream = null;
        UserPointPayTemp uppt;
        UserPointPay upp;
        DataMasterPoint dmp;
        HapiTouchBko bko = new HapiTouchBko();
        duf = new DataUserFelica();
        gxTouch = new GenerateXmlHapiTouchModifyCi();
        uppt = new UserPointPayTemp();
        upp = new UserPointPay();
        dhb = new DataHotelBasic();
        hc = new HotelCi();
        nowPoint = 0;
        nAmountPoint = -1;
        retUse = false;
        retData = false;
        int ciStatus = 0;
        paramIdm = request.getParameter( "idm" );
        paramPrice = request.getParameter( "price" );
        paramPoint = request.getParameter( "point" );
        paramSlipNo = request.getParameter( "slipNo" );
        paramRoomNo = request.getParameter( "roomNo" );
        paramEmployeeCode = request.getParameter( "employeeCode" );
        paramSeq = request.getParameter( "seq" );
        paramAllUse = request.getParameter( "allUse" );
        paramFix = request.getParameter( "fix" );
        // ���X�|���X���Z�b�g
        try
        {
            // �p�����[�^�`�F�b�N
            if ( paramIdm == null )
            {
                paramIdm = "";
            }
            if ( (paramPrice == null) || (paramPrice.compareTo( "" ) == 0) || CheckString.numCheck( paramPrice ) == false )
            {
                paramPrice = "-1";
            }
            if ( (paramPoint == null) || (paramPoint.compareTo( "" ) == 0) || CheckString.numCheck( paramPoint ) == false )
            {
                paramPoint = "0";
            }
            if ( (paramSlipNo == null) || (paramSlipNo.compareTo( "" ) == 0) || CheckString.numCheck( paramSlipNo ) == false )
            {
                paramSlipNo = "0";
            }
            if ( paramRoomNo == null )
            {
                paramRoomNo = "";
            }
            else
            {
                paramRoomNo = new String( URLDecoder.decode( paramRoomNo, "8859_1" ).getBytes( "8859_1" ), "utf-8" );
            }
            if ( (paramEmployeeCode == null) || (paramEmployeeCode.compareTo( "" ) == 0) || CheckString.numCheck( paramEmployeeCode ) == false )
            {
                paramEmployeeCode = "0";
            }
            if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( (paramFix == null) || (paramFix.compareTo( "" ) == 0) || CheckString.numCheck( paramFix ) == false )
            {
                paramFix = "0";
            }
            if ( (paramAllUse == null) || (paramAllUse.compareTo( "" ) == 0) || CheckString.numCheck( paramAllUse ) == false )
            {
                paramAllUse = "0";
            }
            // �z�e���`�F�b�N�C���f�[�^���擾����
            ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
            userId = hc.getHotelCi().getUserId();
            ciStatus = hc.getHotelCi().getCiStatus();
            ciRoomNo = hc.getHotelCi().getRoomNo();
            Logging.info( "[ActionHapiTouch correctCi]ciStatus:" + ciStatus + ",ciRoomNo:" + ciRoomNo + ",ret:" + ret );
            // ���X����̏ꍇ,�����ւ����������Ɣ��f 2015.3.26
            if ( ciStatus == 2 )
            {
                hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                hc.getHotelCi().setRoomNo( paramRoomNo );
                hc.getHotelCi().setCiStatus( 0 );
                ret = hc.getHotelCi().insertData();
                /** �n�s�z�e�^�b�`�[���p�ɒǉ� **/
                hc.registTouchCi( hc.getHotelCi(), false );
                /** �n�s�z�e�^�b�`�[���p�ɒǉ� **/
                gxTouch.setResult( RESULT_OK );
                gxTouch.setCiStatus( 0 );
                Logging.info( "[ActionHapiTouch correctCi]HotelIp.getFrontIp( hotelId, 1 ):" + HotelIp.getFrontIp( hotelId, 1 ) + ",hc.getHotelCi().getRsvNo():" + hc.getHotelCi().getRsvNo() + ",paramRoomNo" + paramRoomNo );

                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) && !hc.getHotelCi().getRsvNo().equals( "" ) && !paramRoomNo.equals( "" ) )
                {
                    RsvFix rf = new RsvFix();
                    /** �i1004�j�n�s�z�e_�n�s�z�e�^�b�`�\�񗈓X�v�� **/
                    rf.setSeq( hc.getHotelCi().getSeq() );
                    rf.setRsvNo( hc.getHotelCi().getRsvNo() );
                    rf.setRoomName( paramRoomNo );
                    rf.setDispRsvNo( hc.getHotelCi().getRsvNo().substring( hc.getHotelCi().getRsvNo().length() - 6 ) );
                    rf.setTouchRoomName( paramRoomNo );
                    rf.sendToHost( HotelIp.getFrontIp( hotelId, 1 ), 10000, 7046, Integer.toString( hotelId ) );

                    FormReserveSheetPC frm = new FormReserveSheetPC();
                    LogicReserveSheetPC logic = new LogicReserveSheetPC();
                    HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();
                    frm.setSelHotelId( hotelId );
                    frm.setRsvNo( hc.getHotelCi().getRsvNo() );
                    // �\��f�[�^���o
                    logic.setFrm( frm );
                    // ���уf�[�^�擾
                    logic.getData( 2 );
                    frm.setMode( ReserveCommon.MODE_RAITEN );
                    htRsvSub.execRaiten( frm, frm.getStatus() );
                }
            }
            else if ( ciStatus == 4 )
            {
                gxTouch.setResult( RESULT_OK );
                hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                if ( paramRoomNo.equals( "" ) == false && paramRoomNo.equals( hc.getHotelCi().getRoomNo() ) == false )
                {
                    hc.getHotelCi().setRoomNo( paramRoomNo );
                }
                if ( Integer.parseInt( paramSlipNo ) > 0 )
                {
                    hc.getHotelCi().setSlipNo( Integer.parseInt( paramSlipNo ) );
                }
                if ( Integer.parseInt( paramPrice ) >= 0 )
                {
                    ciStatus = 3;
                    hc.getHotelCi().setAmount( Integer.parseInt( paramPrice ) );
                }
                gxTouch.setCiStatus( ciStatus );
                hc.getHotelCi().setCiStatus( ciStatus );
                ret = hc.getHotelCi().insertData();
            }
            else
            { // xml�o�̓N���X�Ƀm�[�h���Z�b�g
                if ( ret != false && hc.getHotelCi().getFixFlag() == 0 )
                {
                    StringBuffer requestUrl = request.getRequestURL();
                    String requestUrlStr = requestUrl.toString();
                    if ( !HotelIp.getFrontIp( hotelId ).equals( "" ) && requestUrlStr.indexOf( "happyhotel.jp" ) == -1 && !paramPoint.equals( "0" ) && request.getParameter( "code" ) != null && request.getParameter( "uuid" ) != null )
                    { // �A�������ŁA�^�b�`PC����̃}�C���g�p�͋����Ȃ�
                        gxTouch.setResult( RESULT_NG );
                        gxTouch.setUseResult( RESULT_NG );
                        errorCode = HapiTouchErrorMessage.ERR_10516;
                        hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        hc.getHotelCi().updateData( hotelId, Integer.parseInt( paramSeq ), hc.getHotelCi().getSubSeq() );
                    }
                    else if ( !hc.getHotelCi().getRsvNo().equals( "" ) && Integer.parseInt( paramPoint ) != 0 )
                    {
                        gxTouch.setResult( RESULT_NG );
                        gxTouch.setUseResult( RESULT_NG );
                        errorCode = HapiTouchErrorMessage.ERR_10512;
                    }
                    else
                    {
                        duf.getData( hc.getHotelCi().getUserId() );
                        // �z�e�i�rID���擾����
                        dhb.getData( hotelId );
                        if ( dhb.getId() > 0 )
                        {
                            hotenaviId = dhb.getHotenaviId();
                            hotelName = dhb.getName();
                        }
                        nowPoint = upp.getNowPoint( hc.getHotelCi().getUserId(), false );
                        // �S�g�p�t���O�̃��N�G�X�g�őS�g�p�t���O�������Ă��Ȃ��ꍇ�̂�
                        if ( Integer.parseInt( paramAllUse ) == 1 && hc.getHotelCi().getAllUseFlag() == 0 )
                        {
                            ret = this.allUsePoint( hc.getHotelCi().getUserId(), hc, Integer.parseInt( paramEmployeeCode ), Integer.parseInt( paramPoint ) );
                            if ( ret != false )
                            {
                                ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
                            }
                        }
                        if ( hc.getHotelCi().getAllUseFlag() == 0 )
                        {
                            // �����������Ă���΃}�C���t�^�̕ύX���s��
                            if ( Integer.parseInt( paramPrice ) >= 0 )
                            {
                                // ���X�n�s�[���Z(�ꎞ�ۑ��e�[�u���ɕۑ�����)
                                nVisitPoint = uppt.setVisitPoint( hc.getHotelCi().getUserId(), COME_POINT, nowPoint, Integer.parseInt( paramEmployeeCode ), hc.getHotelCi() );
                                if ( nVisitPoint > 0 )
                                {
                                    gxTouch.setVisitResult( RESULT_OK );
                                    hc.getHotelCi().setVisitPoint( nVisitPoint );
                                }
                                else
                                {
                                    gxTouch.setVisitResult( RESULT_NG );
                                    // ���X�}�C���G���[
                                    if ( nVisitPoint < 0 )
                                    {
                                        errorCode = HapiTouchErrorMessage.ERR_10504;
                                    }
                                }
                                // �z�e�����p�}�C�����Z(�ꎞ�ۑ��e�[�u���ɕۑ�����)
                                nAmountPoint = uppt.setAmountPointUpdate( hc.getHotelCi().getUserId(), AMOUNT_POINT, nowPoint, Integer.parseInt( paramPrice ), Integer.parseInt( paramEmployeeCode ), hc.getHotelCi() );
                                if ( nAmountPoint >= 0 )
                                {
                                    gxTouch.setAmountResult( RESULT_OK );
                                    // �}�C�i�X�̗���ǉ�
                                    hc.setMinusHistory( hc.getHotelCi(), Integer.parseInt( paramEmployeeCode ) );
                                    // �ēx�ŐV�̃f�[�^���擾����
                                    ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
                                }
                                else
                                {
                                    gxTouch.setAmountResult( RESULT_NG );
                                    // �t�^�}�C���G���[
                                    errorCode = HapiTouchErrorMessage.ERR_10511;
                                }
                            }
                        }
                        else
                        {
                            if ( Integer.parseInt( paramFix ) == 0 )
                            {
                                dmp = new DataMasterPoint();
                                // �|�C���g�������Ă�����}�C���g�p
                                if ( Integer.parseInt( paramPoint ) > 0 && Integer.parseInt( paramPoint ) != hc.getHotelCi().getUsePoint() )
                                {
                                    retUse = true;
                                }
                                // �����������Ă�����}�C���t�^���s��
                                if ( Integer.parseInt( paramPrice ) > 0 )
                                {
                                    nAmountPoint = (int)(hc.getHotelCi().getAmountRate() * Integer.parseInt( paramPrice ) / 100);
                                    // �N���X�Ŏ����Ă��闿���ƃ��N�G�X�g���ꂽ�������������}�C�i�X�̗�����ǉ�
                                    if ( nAmountPoint > 0 && nAmountPoint != hc.getHotelCi().getAmount() )
                                    {
                                        // �}�C�i�X�̗���ǉ�
                                        hc.setMinusHistory( hc.getHotelCi(), Integer.parseInt( paramEmployeeCode ) );
                                    }
                                    // ���X�}�C�����ǉ��\���ǂ������`�F�b�N����i�ꎞ�̕��j
                                    if ( uppt.getMasterPointExtNum( hc.getHotelCi().getUserId(), POINT_KIND_RAITEN, COME_POINT, hotelId, dmp, Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ) ) != false )
                                    {
                                        // ���X�}�C�����ǉ��\���ǂ������`�F�b�N����
                                        if ( upp.getMasterPointExtNum( hc.getHotelCi().getUserId(), POINT_KIND_RAITEN, COME_POINT, hotelId, dmp, Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ) ) != false )
                                        {
                                            nVisitPoint = 1;
                                        }
                                    }
                                }
                            }
                        }

                        // ���݂̃|�C���g���Z�b�g
                        nowPoint = upp.getNowPoint( hc.getHotelCi().getUserId(), false );
                        gxTouch.setPoint( nowPoint );
                        if ( ret != false )
                        {
                            hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                            hc.getHotelCi().setUserId( hc.getHotelCi().getUserId() );
                            hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            if ( paramRoomNo.equals( "" ) == false && paramRoomNo.equals( hc.getHotelCi().getRoomNo() ) == false )
                            {
                                hc.getHotelCi().setRoomNo( paramRoomNo );
                            }
                            if ( Integer.parseInt( paramSlipNo ) > 0 )
                            {
                                hc.getHotelCi().setSlipNo( Integer.parseInt( paramSlipNo ) );
                            }
                            if ( nAmountPoint >= 0 )
                            {
                                hc.getHotelCi().setCiStatus( 1 );
                                hc.getHotelCi().setAmount( Integer.parseInt( paramPrice ) );
                                hc.getHotelCi().setAddPoint( nAmountPoint );
                                hc.getHotelCi().setAddDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                hc.getHotelCi().setAddTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                hc.getHotelCi().setAddHotenaviId( hotenaviId );
                                hc.getHotelCi().setAddEmployeeCode( Integer.parseInt( paramEmployeeCode ) );
                            }
                            if ( retUse != false )
                            {
                                // HotelCi��o�^����
                                hc.getHotelCi().setUsePoint( Integer.parseInt( paramPoint ) );
                                hc.getHotelCi().setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                hc.getHotelCi().setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                hc.getHotelCi().setUseHotenaviId( hotenaviId );
                                hc.getHotelCi().setUseEmployeeCode( Integer.parseInt( paramEmployeeCode ) );
                            }
                            if ( Integer.parseInt( paramAllUse ) > 0 )
                            {
                                hc.getHotelCi().setCiStatus( Integer.parseInt( paramAllUse ) );
                            }
                            ret = hc.getHotelCi().insertData();
                            Logging.info( "[ActionHapiTouch correctCi]ret2:" + ret );
                            if ( ret != false )
                            {
                                // �������o�^����Ă���A���z�̕ύX���Ȃ�������X�V���s���B
                                if ( (paramRoomNo.equals( "" ) == false || Integer.parseInt( paramSlipNo ) > 0) && (nAmountPoint == 0 && hc.getHotelCi().getAmount() > 0) )
                                {
                                    // �z�e�����p�}�C���X�V
                                    nAmountPoint = uppt.setAmountPointUpdate( hc.getHotelCi().getUserId(), AMOUNT_POINT, nowPoint, hc.getHotelCi().getAmount(), Integer.parseInt( paramEmployeeCode ), hc.getHotelCi() );
                                    // �z�e�����p�}�C���X�V�G���[
                                    if ( nAmountPoint < 0 )
                                    {
                                        errorCode = HapiTouchErrorMessage.ERR_10508;
                                    }
                                }
                                // ���}�C���̎g�p��߂����߂Ƀ|�C���g�̃��N�G�X�g����������retUse��true�ɂ���
                                if ( Integer.parseInt( paramPoint ) > 0 )
                                {
                                    retUse = true;
                                }
                                /** �n�s�z�e�^�b�`�[���p�ɒǉ� **/
                                hc.registTouchCi( hc.getHotelCi(), retUse );
                                /** �n�s�z�e�^�b�`�[���p�ɒǉ� **/
                                if ( Integer.parseInt( paramFix ) == 1 )
                                {
                                    if ( !hc.getHotelCi().getRsvNo().equals( "" ) ) // �\�񕔉��݌ɂ��J������
                                    {
                                        LogicOwnerRsvCheckIn logic = new LogicOwnerRsvCheckIn();
                                        if ( logic.releaseRoomRemaindar( hotelId, paramRoomNo, hc.getHotelCi().getRsvNo(), hc.getHotelCi().getCiDate() ) )
                                        {
                                            if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                                            {
                                                RsvList rl = new RsvList();
                                                // �v�m�F
                                                if ( rl.getData( hotelId, hc.getHotelCi().getCiDate(), hc.getHotelCi().getCiDate(), 0, hc.getHotelCi().getRsvNo(), 1 ) != false )
                                                {
                                                    rl.sendToHost( hotelId );
                                                }
                                            }
                                        }
                                        else
                                        {
                                            DataApErrorHistory daeh = new DataApErrorHistory();
                                            daeh.setErrorCode( HapiTouchErrorMessage.ERR_10615 );
                                            daeh.setErrorSub( 0 );
                                            daeh.setHotelName( hotelName );
                                            daeh.setHotenaviId( hotenaviId );
                                            daeh.setRoomName( paramRoomNo );
                                            daeh.setId( hotelId );
                                            daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                            daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                            daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                                            daeh.setUserId( userId );
                                            daeh.insertData();
                                        }
                                    }
                                }
                                gxTouch.setResult( RESULT_OK );
                                gxTouch.setCiStatus( hc.getHotelCi().getCiStatus() );
                            }
                            else
                            {
                                gxTouch.setResult( RESULT_NG );
                                gxTouch.setCiStatus( 0 );
                                // �`�F�b�N�C���R�[�h�ǉ��G���[
                                errorCode = HapiTouchErrorMessage.ERR_10603;
                            }
                        }
                        else
                        {
                            gxTouch.setResult( RESULT_NG );
                            gxTouch.setCiStatus( 0 );
                        }
                    }
                }
                else
                {
                    if ( hc.getHotelCi().getFixFlag() == 1 )
                    {
                        gxTouch.setResult( RESULT_NG );
                        gxTouch.setCiStatus( hc.getHotelCi().getCiStatus() );
                    }
                    gxTouch.setResult( RESULT_NO );
                    gxTouch.setRegistUrl( BASE_URL + PAGE_URL + "?method=" + METHOD_REGIST + "&idm=" + paramIdm );
                    // �`�F�b�N�C���f�[�^�擾�G���[
                    errorCode = HapiTouchErrorMessage.ERR_10601;
                }
            }
            if ( errorCode == 0 )
            {
                Logging.info( "STEP13", "userId:" + userId );
                // �o�b�N�I�t�B�X�f�[�^�X�V
                if ( bko.updateBkoData( userId, hotelId, hc.getHotelCi() ) == false )
                {
                    // �o�b�N�I�t�B�X�}�C���X�V�G���[
                    errorCode = HapiTouchErrorMessage.ERR_10510;
                }
            }
            if ( errorCode == 0 && ReserveCommon.isSyncReserve( hotelId ) )
            {
                if ( hc.getHotelCi().getRsvNo().equals( "" ) == false && !ciRoomNo.equals( paramRoomNo ) )
                {
                    // �����ւ����s��
                    HapiTouchRsv.rsvRoomChange( hotelId, hc.getHotelCi().getRsvNo(), paramRoomNo );
                }
            }
            if ( errorCode != 0 )
            // �G���[���������Ă����ꍇ�ɂ̓G���[�����ɏ�������
            {
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 1 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( paramRoomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                daeh.setUserId( userId );
                daeh.insertData();
            }
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
            Logging.error( "[ActionHapiTouch modifyCi]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch modifyCi]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * �X�e�[�^�X�m�F
     * 
     * @param hotelId �z�e��ID
     * @param seq �Ǘ��ԍ�
     * @param response ���X�|���X
     **/
    public void statusCi(int hotelId, int seq, HttpServletResponse response)
    {
        int status;
        HotelCi hc;
        GenerateXmlHapiTouchModifyCi gxTouch;
        ServletOutputStream stream = null;
        gxTouch = new GenerateXmlHapiTouchModifyCi();
        hc = new HotelCi();
        // ���X�|���X���Z�b�g
        try
        {
            stream = response.getOutputStream();
            status = hc.getCiStatus( hotelId, seq, 0 );
            gxTouch.setCiStatus( status );
            // XML�̏o��
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch useData]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch useData]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * �X�e�[�^�X�m�F
     * 
     * @param hotelId �z�e��ID
     * @param response ���X�|���X
     **/
    public void getConfig(int hotelId, boolean roomDispFlag, HttpServletResponse response)
    {
        int seq = 0;
        boolean retRoom = false;
        boolean retRoomMore = false;
        boolean retHappieNow = false;
        boolean retHappieNext = false;
        HotelHappie hh;
        HotelRoom hr;
        HotelRoomMore hrm;
        DataRsvReserveBasic drrb;
        DataHotelBasic dhb;
        DataMasterPoint dmpCome;
        DataMasterPoint dmpAmount;
        DataMasterPoint dmpRsv;
        GenerateXmlHapiTouchGetConfig gxTouch;
        GenerateXmlHapiTouchGetConfigRoom gxRoom;
        GenerateXmlHapiTouchGetConfigPoint gxPoint;
        ServletOutputStream stream = null;
        hh = new HotelHappie();
        hr = new HotelRoom();
        hrm = new HotelRoomMore();
        drrb = new DataRsvReserveBasic();
        dhb = new DataHotelBasic();
        dmpCome = new DataMasterPoint();
        dmpAmount = new DataMasterPoint();
        dmpRsv = new DataMasterPoint();
        gxTouch = new GenerateXmlHapiTouchGetConfig();
        gxRoom = new GenerateXmlHapiTouchGetConfigRoom();
        gxPoint = new GenerateXmlHapiTouchGetConfigPoint();
        // ���X�|���X���Z�b�g
        try
        {
            stream = response.getOutputStream();
            if ( dhb.getData( hotelId ) != false )
            {
                gxTouch.setHotelName( dhb.getName() );
            }
            // ���������擾����ꍇ�̂ݎ擾
            if ( roomDispFlag != false )
            {
                // ���������擾
                retRoomMore = hrm.getRoomData( hotelId, 0 );
                // ������񂪂Ȃ��ꍇ��hh_hotel_room����擾
                if ( retRoomMore == false && hrm.getHotelRoomCount() == 0 )
                {
                    retRoom = hr.getRoomData( hotelId, 0 );
                }
                // ���������Z�b�g
                if ( retRoomMore != false && hrm.getHotelRoomCount() > 0 )
                {
                    // �z�e���̏����Z�b�g
                    for( int i = 0 ; i < hrm.getHotelRoomCount() ; i++ )
                    {
                        GenerateXmlHapiTouchGetConfigRoomNo gxRoomNo = new GenerateXmlHapiTouchGetConfigRoomNo();
                        gxRoomNo.setRoomNo( hrm.getHotelRoom()[i].getRoomNameHost() );
                        gxRoom.setRoomNo( gxRoomNo );
                    }
                    gxTouch.setRoom( gxRoom );
                }
                else if ( retRoom != false && hr.getHotelRoomCount() > 0 )
                {
                    // �z�e���̏����Z�b�g
                    for( int i = 0 ; i < hr.getHotelRoomCount() ; i++ )
                    {
                        GenerateXmlHapiTouchGetConfigRoomNo gxRoomNo = new GenerateXmlHapiTouchGetConfigRoomNo();
                        gxRoomNo.setRoomNo( hr.getHotelRoom()[i].getRoomName() );
                        gxRoom.setRoomNo( gxRoomNo );
                    }
                    gxTouch.setRoom( gxRoom );
                }
                else
                {
                    errorCode = 0;
                }
            }
            dmpCome.getData( COME_POINT );
            dmpAmount.getData( AMOUNT_POINT );
            dmpRsv.getData( RSV_POINT );
            // �f�t�H���g�̗L����������|�C���g���Z�b�g
            gxPoint.setDefault( "" );
            gxPoint.setVisitRate( dmpCome.getAddPoint() );
            gxPoint.setAmountRate( dmpAmount.getAddPoint() );
            gxPoint.setRsvRate( dmpRsv.getAddPoint() );
            // �f�t�H���g�̖�����������|�C���g���Z�b�g
            gxPoint.setVisitRateFree( dmpCome.getAddPoint() * dmpCome.getFreeMultiple() );
            gxPoint.setAmountRateFree( dmpAmount.getAddPoint() * dmpAmount.getFreeMultiple() );
            gxPoint.setRsvRateFree( dmpRsv.getAddPoint() * dmpRsv.getFreeMultiple() );
            // �}�C���̏����擾
            retHappieNow = hh.getData( hotelId );
            if ( retHappieNow != false )
            {
                if ( hh.getHotelCouponCount() > 0 )
                {
                    seq = hh.getHotelHappie().getSeq();
                    // �f�[�^���擾�ł�����f�[�^���Z�b�g
                    GenerateXmlHapiTouchGetConfigPointData dataList = new GenerateXmlHapiTouchGetConfigPointData();
                    dataList.setId( hh.getHotelHappie().getSeq() );
                    dataList.setStartDate( hh.getHotelHappie().getStartDate() );
                    dataList.setEndDate( hh.getHotelHappie().getEndDate() );
                    // �L������̃��[�g���Z�b�g
                    dataList.setVisitRate( dmpCome.getAddPoint() * hh.getHotelHappie().getComePointMultiple() );
                    dataList.setAmountRate( dmpAmount.getAddPoint() * hh.getHotelHappie().getUsePointMultiple() );
                    dataList.setRsvRate( dmpRsv.getAddPoint() );
                    // ��������̃��[�g���Z�b�g
                    dataList.setVisitRateFree( dmpCome.getAddPoint() * dmpCome.getFreeMultiple() * hh.getHotelHappie().getComePointMultiple() );
                    dataList.setAmountRateFree( dmpAmount.getAddPoint() * dmpAmount.getFreeMultiple() * hh.getHotelHappie().getUsePointMultiple() );
                    dataList.setRsvRateFree( dmpRsv.getAddPoint() * dmpRsv.getFreeMultiple() );
                    gxPoint.setData( dataList );
                    retHappieNext = hh.getDataMulti( hotelId, seq );
                    if ( retHappieNext != false )
                    {
                        for( int i = 0 ; i < hh.getHotelHappieMulti().length ; i++ )
                        {
                            GenerateXmlHapiTouchGetConfigPointData dataListNext = new GenerateXmlHapiTouchGetConfigPointData();
                            dataListNext.setId( hh.getHotelHappieMulti()[i].getSeq() );
                            dataListNext.setStartDate( hh.getHotelHappieMulti()[i].getStartDate() );
                            dataListNext.setEndDate( hh.getHotelHappieMulti()[i].getEndDate() );
                            // �L������̃��[�g���Z�b�g
                            dataListNext.setVisitRate( dmpCome.getAddPoint() * hh.getHotelHappieMulti()[i].getComePointMultiple() );
                            dataListNext.setAmountRate( dmpAmount.getAddPoint() * hh.getHotelHappieMulti()[i].getUsePointMultiple() );
                            dataListNext.setRsvRate( dmpRsv.getAddPoint() );
                            // ��������̃��[�g���Z�b�g
                            dataListNext.setVisitRateFree( dmpCome.getAddPoint() * dmpCome.getFreeMultiple() * hh.getHotelHappieMulti()[i].getComePointMultiple() );
                            dataListNext.setAmountRateFree( dmpAmount.getAddPoint() * dmpAmount.getFreeMultiple() * hh.getHotelHappieMulti()[i].getUsePointMultiple() );
                            dataListNext.setRsvRateFree( dmpRsv.getAddPoint() * dmpRsv.getFreeMultiple() );
                            // �߂������Z�b�g����
                            gxPoint.setData( dataListNext );
                        }
                    }
                }
                else
                {
                    // �}�C���{���擾�G���[
                    errorCode = 0;
                }
            }
            else
            {
                // �}�C���{���擾�G���[
                errorCode = 0;
            }
            // �\��\���ǂ����𔻒f����
            drrb.getData( hotelId );
            if ( drrb.getID() > 0 )
            {
                if ( drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 1 )
                {
                    gxTouch.setIsValidRsv( 1 );
                    gxTouch.setDeadlineTime( drrb.getDeadline_time() );
                }
            }
            if ( dhb.getRank() >= 2 )
            {
                // �z�e���̃N�[�|�����擾����
                HotelCoupon hc = new HotelCoupon();
                hc.getCouponData( hotelId );
                if ( hc.getHotelCouponCount() > 0 )
                {
                    // gxTouch.setIsCoupon( 1 );
                    gxTouch.setIsCoupon( 0 ); // �^�b�`PC�ɃN�[�|���̕\�������Ȃ�
                }
                else
                {
                    gxTouch.setIsCoupon( 0 );
                }
            }
            else
            {
                gxTouch.setIsCoupon( 0 );
            }
            // �}�C���̏����Z�b�g
            gxPoint.setNowId( seq );
            gxTouch.setPoint( gxPoint );
            // IP�A�h���X���Z�b�g�igetConfig�ɃZ�b�g����̂̓X�[�p�[�t�����e�B�A�ȊO�j
            gxTouch.setFrontIp( HotelIp.getFrontIpForUseMile( hotelId ) );
            // XML�̏o��
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;
            Logging.info( xmlOut );
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch getConfig]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch getConfig]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * �`�F�b�N�C���f�[�^��������
     * 
     * @param id �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     * @return
     */
    public void syncCi(int id, HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret = false;
        int i;
        String paramStartDate;
        String paramEndDate;
        String paramStartTime;
        String paramEndTime;
        ServletOutputStream stream = null;
        GenerateXmlHapiTouchSyncCi gxSyncCi;
        HotelCi hc;
        DataApTouchCi datc;
        DataApUuidUser dauu;
        UserPointPay upp = null;
        TouchErrorHistory teh;
        teh = new TouchErrorHistory();
        hc = new HotelCi();
        paramStartDate = request.getParameter( "startDate" );
        paramEndDate = request.getParameter( "endDate" );
        paramStartTime = request.getParameter( "startTime" );
        paramEndTime = request.getParameter( "endTime" );
        gxSyncCi = new GenerateXmlHapiTouchSyncCi();
        datc = new DataApTouchCi();
        dauu = new DataApUuidUser();
        // �J�n���̃`�F�b�N
        if ( (paramStartDate == null) || (paramStartDate.equals( "" ) != false) || (CheckString.numCheck( paramStartDate ) == false) )
        {
            paramStartDate = "0";
        }
        // �I�����̃`�F�b�N
        if ( (paramEndDate == null) || (paramEndDate.equals( "" ) != false) || (CheckString.numCheck( paramEndDate ) == false) )
        {
            paramEndDate = "0";
        }
        // �J�n�����̃`�F�b�N
        if ( (paramStartTime == null) || (paramStartDate.equals( "" ) != false) || (CheckString.numCheck( paramStartTime ) == false) )
        {
            paramStartTime = "0";
        }
        // �I�������̃`�F�b�N
        if ( (paramEndTime == null) || (paramEndTime.equals( "" ) != false) || (CheckString.numCheck( paramEndTime ) == false) )
        {
            paramEndTime = "0";
        }
        try
        {
            stream = response.getOutputStream();
            // �z�e��ID�A�J�n���A�I��������`�F�b�N�C���f�[�^���擾
            ret = hc.getData( id, Integer.parseInt( paramStartDate ), Integer.parseInt( paramStartTime ), Integer.parseInt( paramEndDate ), Integer.parseInt( paramEndTime ) );
            if ( ret != false )
            {
                if ( hc.gethotelCiCount() > 0 )
                {
                    for( i = 0 ; i < hc.gethotelCiCount() ; i++ )
                    {
                        if ( upp != null )
                        {
                            upp = null;
                        }
                        upp = new UserPointPay();
                        GenerateXmlHapiTouchSyncCiData gxSyncCiData = new GenerateXmlHapiTouchSyncCiData();
                        // �擾�����f�[�^��xml�ɃZ�b�g
                        gxSyncCiData.setCiCode( hc.getHotelCiMulti()[i].getSeq() );
                        gxSyncCiData.setCiDate( hc.getHotelCiMulti()[i].getCiDate() );
                        gxSyncCiData.setCiTime( hc.getHotelCiMulti()[i].getCiTime() );
                        gxSyncCiData.setUserId( Integer.toString( hc.getHotelCiMulti()[i].getUserSeq() ) );
                        gxSyncCiData.setPoint( upp.getNowPoint( hc.getHotelCiMulti()[i] ) );
                        gxSyncCiData.setAmountRate( hc.getHotelCiMulti()[i].getAmountRate() );
                        gxSyncCiData.setRoomNo( hc.getHotelCiMulti()[i].getRoomNo() );
                        gxSyncCiData.setUsePoint( hc.getHotelCiMulti()[i].getUsePoint() );
                        // ������0�~�̏ꍇ�����邽�߁A�X�e�[�^�X�Ŋm�F����
                        if ( hc.getHotelCiMulti()[i].getCiStatus() > 0 )
                        {
                            gxSyncCiData.setAmount( hc.getHotelCiMulti()[i].getAmount() );
                        }
                        // �`�[�ԍ���0���傫���ꍇ�ɃZ�b�g
                        if ( hc.getHotelCiMulti()[i].getSlipNo() > 0 )
                        {
                            gxSyncCiData.setSlipNo( hc.getHotelCiMulti()[i].getSlipNo() );
                        }
                        int ciStatus = hc.getHotelCiMulti()[i].getCiStatus();
                        // �n�s�z�e���[�U�[�łȂ��ꍇ�ł��A�������̏ꍇ�͖��������\��
                        gxSyncCiData.setCiStatus( ciStatus );
                        gxSyncCiData.setUserType( hc.getHotelCiMulti()[i].getUserType() );
                        // CiStatus = 3��������폜�ΏۂƂ���
                        if ( ciStatus == CI_STATUS_DEL )
                        {
                            if ( teh.getErrorData( id, hc.getHotelCiMulti()[i].getSeq() ) )
                            {
                                gxSyncCiData.setDelFlag( SYNC_CI_ADD_FLAG );
                            }
                            else
                            {
                                gxSyncCiData.setDelFlag( SYNC_CI_DEL_FLAG );
                            }
                        }
                        else
                        {
                            gxSyncCiData.setDelFlag( SYNC_CI_ADD_FLAG );
                        }
                        gxSyncCiData.setRsvNo( hc.getHotelCiMulti()[i].getRsvNo() );
                        if ( datc.getData( id, hc.getHotelCiMulti()[i].getSeq() ) != false )
                        {
                            gxSyncCiData.setUseTempFlag( datc.getUseTempFlag() );
                        }
                        else
                        {
                            // �f�[�^���擾�ł��Ȃ�������0���Z�b�g
                            gxSyncCiData.setUseTempFlag( 0 );
                        }
                        if ( dauu.getAppData( hc.getHotelCiMulti()[i].getUserId() ) )
                        {
                            gxSyncCiData.setAppStatus( dauu.getAppStatus() );
                        }
                        else
                        {
                            gxSyncCiData.setAppStatus( 1 );
                        }
                        // �\��ԍ��������Ă��āA�O���\��ł��O�����[�U�̏ꍇ��LIJ�ƕ\������
                        if ( !hc.getHotelCiMulti()[i].getRsvNo().equals( "" ) )
                        {
                            if ( hc.getHotelCiMulti()[i].getExtUserFlag() == 1 )
                            {
                                if ( DataRsvReserve.getExtFlag( id, hc.getHotelCiMulti()[i].getRsvNo() ) == ReserveCommon.EXT_LVJ )
                                {
                                    gxSyncCiData.setUserId( "LIJ" );
                                    // gxSyncCiData.setUserType( 2 );
                                }
                            }
                        }
                        gxSyncCi.setCiData( gxSyncCiData );
                    }
                }
            }
            else
            {
                GenerateXmlHapiTouchSyncCiData gxSyncCiData = new GenerateXmlHapiTouchSyncCiData();
                gxSyncCi.setCiData( gxSyncCiData );
            }
            // XML�̏o��
            String xmlOut = gxSyncCi.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            Logging.info( xmlOut );
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch syncCi]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch syncCi]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * �`�F�b�N�C���f�[�^��������
     * 
     * @param id �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     * @return
     */
    public void syncCiBySeq(int id, HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret = false;
        String paramSeq;
        ServletOutputStream stream = null;
        GenerateXmlHapiTouchSyncCiBySeq gxSyncCiBySeq;
        DataHotelCi dhc;
        DataApTouchCi datc;
        UserPointPay upp = null;
        TouchErrorHistory teh;
        dhc = new DataHotelCi();
        paramSeq = request.getParameter( "seq" );
        gxSyncCiBySeq = new GenerateXmlHapiTouchSyncCiBySeq();
        datc = new DataApTouchCi();
        teh = new TouchErrorHistory();
        DataApUuidUser dauu;
        dauu = new DataApUuidUser();
        // �J�n���̃`�F�b�N
        if ( (paramSeq == null) || (paramSeq.equals( "" ) != false) || (CheckString.numCheck( paramSeq ) == false) )
        {
            paramSeq = "0";
        }
        try
        {
            stream = response.getOutputStream();
            // �z�e��ID�A�J�n���A�I��������`�F�b�N�C���f�[�^���擾
            ret = dhc.getData( id, Integer.parseInt( paramSeq ) );
            if ( ret != false )
            {
                upp = new UserPointPay();
                // �擾�����f�[�^��xml�ɃZ�b�g
                gxSyncCiBySeq.setCiCode( dhc.getSeq() );
                gxSyncCiBySeq.setCiDate( dhc.getCiDate() );
                gxSyncCiBySeq.setCiTime( dhc.getCiTime() );
                gxSyncCiBySeq.setUserId( Integer.toString( dhc.getUserSeq() ) );
                gxSyncCiBySeq.setAmountRate( dhc.getAmountRate() );
                gxSyncCiBySeq.setRoomNo( dhc.getRoomNo() );
                gxSyncCiBySeq.setUsePoint( dhc.getUsePoint() );
                gxSyncCiBySeq.setPoint( upp.getNowPoint( dhc ) );
                // ������0�~�̏ꍇ�����邽�߁A�X�e�[�^�X�Ŋm�F����
                if ( dhc.getCiStatus() > 0 )
                {
                    gxSyncCiBySeq.setAmount( dhc.getAmount() );
                }
                // �`�[�ԍ���0���傫���ꍇ�ɃZ�b�g
                if ( dhc.getSlipNo() > 0 )
                {
                    gxSyncCiBySeq.setSlipNo( dhc.getSlipNo() );
                }
                // �n�s�z�e���[�U�[�łȂ��ꍇ�͖��������\��
                int ciStatus = dhc.getCiStatus();
                gxSyncCiBySeq.setCiStatus( ciStatus );
                gxSyncCiBySeq.setUserType( dhc.getUserType() );
                // CiStatus = 3��������폜�ΏۂƂ���
                if ( ciStatus == CI_STATUS_DEL )
                {
                    if ( teh.getErrorData( id, dhc.getSeq() ) )
                    {
                        gxSyncCiBySeq.setDelFlag( SYNC_CI_ADD_FLAG );
                    }
                    else
                    {
                        gxSyncCiBySeq.setDelFlag( SYNC_CI_DEL_FLAG );
                    }
                }
                else
                {
                    gxSyncCiBySeq.setDelFlag( SYNC_CI_ADD_FLAG );
                }
                gxSyncCiBySeq.setRsvNo( dhc.getRsvNo() );
                if ( datc.getData( id, dhc.getSeq() ) != false )
                {
                    gxSyncCiBySeq.setUseTempFlag( datc.getUseTempFlag() );
                }
                else
                {
                    // �f�[�^���擾�ł��Ȃ�������0���Z�b�g
                    gxSyncCiBySeq.setUseTempFlag( 0 );
                }
                if ( dauu.getAppData( dhc.getUserId() ) )
                {
                    gxSyncCiBySeq.setAppStatus( dauu.getAppStatus() );
                }
                else
                {
                    gxSyncCiBySeq.setAppStatus( 1 );
                }
                // �\��ԍ��������Ă��āA�O���\��ł��O�����[�U�̏ꍇ��LIJ�ƕ\������
                if ( !dhc.getRsvNo().equals( "" ) )
                {
                    if ( dhc.getExtUserFlag() == 1 )
                    {
                        if ( DataRsvReserve.getExtFlag( id, dhc.getRsvNo() ) == ReserveCommon.EXT_LVJ )
                        {
                            gxSyncCiBySeq.setUserId( "LIJ" );
                            // gxSyncCiBySeq.setUserType( 2 );
                        }
                    }
                }
            }
            // XML�̏o��
            String xmlOut = gxSyncCiBySeq.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            Logging.info( xmlOut );
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch syncCi]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch syncCi]Exception:" + e.toString() );
                }
            }
        }
    }

    /****
     * ���X�L�����Z��
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void visitCancel(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret;
        String paramEmployeeCode;
        String paramSeq;
        String paramFix;
        HotelCi hc;
        GenerateXmlHapiTouchVisitCancel gxTouch;
        ServletOutputStream stream = null;
        UserPointPayTemp uppt;
        UserPointPay upp;
        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        HapiTouchBko bko = new HapiTouchBko();
        DataHotelBasic dhb;
        gxTouch = new GenerateXmlHapiTouchVisitCancel();
        dhb = new DataHotelBasic();
        uppt = new UserPointPayTemp();
        upp = new UserPointPay();
        hc = new HotelCi();
        ret = false;
        paramEmployeeCode = request.getParameter( "employeeCode" );
        paramSeq = request.getParameter( "seq" );
        paramFix = request.getParameter( "fix" );
        String hotelName = "";
        String hotenaviId = "";
        String rsvNo = "";
        String roomNo = "";
        String userId = "";
        // ���X�|���X���Z�b�g
        try
        {
            if ( dhb.getData( hotelId ) != false )
            {
                hotelName = dhb.getName();
                hotenaviId = dhb.getHotenaviId();
            }
            // �p�����[�^�`�F�b�N
            if ( (paramEmployeeCode == null) || (paramEmployeeCode.compareTo( "" ) == 0) || CheckString.numCheck( paramEmployeeCode ) == false )
            {
                paramEmployeeCode = "0";
            }
            if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( (paramFix == null) || (paramFix.compareTo( "" ) == 0) || CheckString.numCheck( paramFix ) == false )
            {
                paramFix = "0";
            }
            stream = response.getOutputStream();
            if ( Integer.parseInt( paramSeq ) > 0 )
            {
                // �z�e���`�F�b�N�C���f�[�^���擾����
                ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
            }
            // xml�o�̓N���X�Ƀm�[�h���Z�b�g
            if ( ret != false && hc.getHotelCi().getCiStatus() == 0 )
            {
                rsvNo = hc.getHotelCi().getRsvNo();
                roomNo = hc.getHotelCi().getRoomNo();
                userId = hc.getHotelCi().getUserId();
                hc = htRsvSub.visitCancel( hotelId, hc, 1 );
                if ( hc.getErrorMsg() > 0 )
                {
                    ret = false;
                }
                if ( ret != false )
                {
                    gxTouch.setResult( RESULT_OK );
                    /** �n�s�z�e�^�b�`�[���p�ɒǉ� **/
                    hc.registTouchCi( hc.getHotelCi(), false );
                    /** �n�s�z�e�^�b�`�[���p�ɒǉ� **/
                }
                else
                {
                    gxTouch.setResult( RESULT_NG );
                    // �`�F�b�N�C���f�[�^�ǉ��G���[
                    errorCode = HapiTouchErrorMessage.ERR_10802;
                }
                gxTouch.setCiStatus( hc.getHotelCi().getCiStatus() );
                gxTouch.setCiCode( hc.getHotelCi().getSeq() );
                gxTouch.setCiDate( hc.getHotelCi().getCiDate() );
                gxTouch.setCiTime( hc.getHotelCi().getCiTime() );
                gxTouch.setUserId( hc.getHotelCi().getUserSeq() );
                gxTouch.setAmountRate( hc.getHotelCi().getAmountRate() );
                gxTouch.setRoomNo( hc.getHotelCi().getRoomNo() );
                gxTouch.setUsePoint( hc.getHotelCi().getUsePoint() );
                gxTouch.setAmount( hc.getHotelCi().getAmount() );
                gxTouch.setSlipNo( hc.getHotelCi().getSlipNo() );
                gxTouch.setRsvNo( hc.getHotelCi().getRsvNo() );
                gxTouch.setUserType( hc.getHotelCi().getUserType() );
                // �\��ԍ��������Ă���ꍇ
                if ( hc.getHotelCi().getRsvNo().equals( "" ) == false )
                {
                    // �t�H�[���ɃZ�b�g
                    frm.setSelHotelId( hotelId );
                    frm.setRsvNo( hc.getHotelCi().getRsvNo() );
                    // �\��f�[�^���o
                    logic.setFrm( frm );
                    // ���уf�[�^�擾
                    logic.getData( 2 );
                    frm.setMode( "" ); // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
                    frm.setErrMsg( "" );
                    frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );
                    // �����ԍ����o�^����Ă���A���X�̃X�e�[�^�X�̃f�[�^�ŁA�`�F�b�N�C���f�[�^���쐬����Ă���ꍇ
                    if ( frm.getSeq() > 0 && frm.getStatus() == ReserveCommon.RSV_STATUS_ZUMI && ret != false )
                    {
                        // �\��o�^�������[�U�[�̃��[���A�h���X�擾
                        UserBasicInfo ubi = new UserBasicInfo();
                        ubi.getUserBasic( frm.getUserId() );
                        frm.setLoginUserId( frm.getUserId() );
                        frm.setLoginUserMail( ubi.getUserInfo().getMailAddr() );
                        frm.setLoginUserTel( ubi.getUserInfo().getTel1() );
                        frm.setMail( ubi.getUserInfo().getMailAddr() );
                        DataRsvPlan dataPlan = new DataRsvPlan();
                        int offerKind;
                        // �z�e���̒񋟋敪�擾
                        dataPlan.getData( frm.getSelHotelId(), frm.getSelPlanId() );
                        offerKind = dataPlan.getOfferKind();
                        frm.setOfferKind( offerKind );
                        // �����X�̃L�����Z��
                        frm = htRsvSub.execUndoFix( frm, frm.getStatus() );
                        // �G���[���莞
                        if ( frm.getErrMsg().trim().length() != 0 )
                        {
                            errorCode = HapiTouchErrorMessage.ERR_20904;
                        }
                        else
                        {
                            if ( hc.getHotelCi().getCiStatus() <= 2 && frm.getExtFlag() == ReserveCommon.EXT_HAPIHOTE )
                            {
                                errorCode = hc.getErrorMsg();
                                ret = bko.cancelBkoData( hc.getHotelCi().getUserId(), hotelId, POINT_KIND_YOYAKU, hc.getHotelCi() );
                                if ( ret == false )
                                {
                                    // �o�b�N�I�t�B�X�f�[�^����������s
                                    errorCode = HapiTouchErrorMessage.ERR_20912;
                                }
                                ret = uppt.cancelRsvPoint( hc.getHotelCi(), POINT_KIND_YOYAKU, 0 );
                                if ( ret == false )
                                {
                                    errorCode = HapiTouchErrorMessage.ERR_20913;
                                }
                                // uppt�̃}�C�������������܂���������Aupp��������
                                if ( ret != false )
                                {
                                    ret = upp.cancelRsvPoint( hc.getHotelCi(), POINT_KIND_YOYAKU, 0 );
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                errorCode = HapiTouchErrorMessage.ERR_10801;
                gxTouch.setResult( RESULT_NO );
                gxTouch.setErrorCode( errorCode );
            }
            if ( errorCode == 0 )
            {
                // IP�A�h���X���o�^����Ă���ꍇ�́A�z�X�g�A�������B�\��f�[�^�̍ŐV��Ԃ��z�X�g�ɓ`����
                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) && hc.getHotelCi().getRsvNo().equals( "" ) == false )
                {
                    RsvList rl = new RsvList();
                    if ( rl.getData( hotelId, 0, 0, 0, hc.getHotelCi().getRsvNo(), 0 ) != false )
                    {
                        rl.sendToHost( hotelId );
                    }
                }
            }
            if ( errorCode != 0 )
            // �G���[���������Ă����ꍇ�ɂ̓G���[�����ɏ�������
            {
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 1 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( roomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                daeh.setReserveNo( rsvNo );
                daeh.setUserId( userId );
                daeh.insertData();
            }
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
            Logging.error( "[ActionHapiTouch visitCancel]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch visitCancel]Exception:" + e.toString() );
                }
            }
        }
    }

    /****
     * �|�C���g�L�����Z��
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void pointCancel(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        int nowPoint;
        boolean ret;
        boolean retUse;
        boolean retData;
        String hotelName = "";
        String hotenaviId = "";
        String rsvNo = "";
        String roomNo = "";
        String userId = "";
        String paramEmployeeCode;
        String paramSeq;
        DataUserFelica duf;
        DataHotelBasic dhb;
        HotelCi hc;
        GenerateXmlHapiTouchPointCancel gxTouch;
        ServletOutputStream stream = null;
        UserPointPayTemp uppt;
        UserPointPay upp;
        HapiTouchBko bko = new HapiTouchBko();
        duf = new DataUserFelica();
        gxTouch = new GenerateXmlHapiTouchPointCancel();
        uppt = new UserPointPayTemp();
        upp = new UserPointPay();
        dhb = new DataHotelBasic();
        hc = new HotelCi();
        nowPoint = 0;
        ret = false;
        retUse = false;
        retData = false;
        paramEmployeeCode = request.getParameter( "employeeCode" );
        paramSeq = request.getParameter( "seq" );
        // ���X�|���X���Z�b�g
        try
        {
            // �p�����[�^�`�F�b�N
            if ( (paramEmployeeCode == null) || (paramEmployeeCode.compareTo( "" ) == 0) || CheckString.numCheck( paramEmployeeCode ) == false )
            {
                paramEmployeeCode = "0";
            }
            if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            stream = response.getOutputStream();
            if ( Integer.parseInt( paramSeq ) > 0 )
            {
                // �z�e���`�F�b�N�C���f�[�^���擾����
                ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
            }
            // xml�o�̓N���X�Ƀm�[�h���Z�b�g

            StringBuffer requestUrl = request.getRequestURL();
            String requestUrlStr = requestUrl.toString();
            if ( !HotelIp.getFrontIp( hotelId ).equals( "" ) && requestUrlStr.indexOf( "happyhotel.jp" ) == -1 && request.getParameter( "code" ) != null && request.getParameter( "uuid" ) != null )
            {
                // �A�������Ń^�b�`PC����̃}�C���g�p�L�����Z����NG�Ƃ���
                gxTouch.setResult( RESULT_NG );
                errorCode = HapiTouchErrorMessage.ERR_10907;
                gxTouch.setErrorCode( HapiTouchErrorMessage.ERR_10907 );
                hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                hc.getHotelCi().updateData( hotelId, Integer.parseInt( paramSeq ), hc.getHotelCi().getSubSeq() );
            }
            else if ( ret != false )
            {
                rsvNo = hc.getHotelCi().getRsvNo();
                roomNo = hc.getHotelCi().getRoomNo();
                userId = hc.getHotelCi().getUserId();
                if ( hc.getHotelCi().getCiStatus() == 0 )
                {
                    duf.getData( hc.getHotelCi().getUserId() );
                    // �z�e�i�rID���擾����
                    dhb.getData( hotelId );
                    if ( dhb.getId() > 0 )
                    {
                        hotenaviId = dhb.getHotenaviId();
                        hotelName = dhb.getName();
                    }
                    nowPoint = upp.getNowPoint( hc.getHotelCi().getUserId(), false );
                    // �|�C���g�������Ă�����}�C����������
                    if ( hc.getHotelCi().getUsePoint() > 0 )
                    {
                        // ���Ɏg�p�}�C���f�[�^�����邩�ǂ������m�F����
                        retData = uppt.getUserPointHistory( hc.getHotelCi().getUserId(), hc.getHotelCi().getId(), POINT_KIND_WARIBIKI, hc.getHotelCi().getUserSeq(), hc.getHotelCi().getVisitSeq() );
                        if ( retData != false )
                        {
                            // �f�[�^�����邽�߁A�g�p�}�C�����X�V
                            retUse = uppt.setUsePointUpdate( hc.getHotelCi().getUserId(), USE_POINT, nowPoint, 0, Integer.parseInt( paramEmployeeCode ), hc.getHotelCi() );
                            if ( retUse != false )
                            {
                                // hh_user_point_pay�Ɏg�p�}�C���̍X�V
                                retUse = upp.setUsePointUpdate( hc.getHotelCi().getUserId(), USE_POINT, nowPoint, 0, Integer.parseInt( paramEmployeeCode ), hc.getHotelCi() );
                                if ( retUse == false )
                                {
                                    errorCode = HapiTouchErrorMessage.ERR_10903;
                                }
                            }
                            else
                            {
                                errorCode = HapiTouchErrorMessage.ERR_10903;
                            }
                        }
                        else
                        {
                            // errorCode ���Z�b�g
                            errorCode = HapiTouchErrorMessage.ERR_10904;
                        }
                        if ( errorCode > 0 )
                        {
                            retData = false;
                        }
                        else
                        {
                            retData = true;
                        }
                    }
                    else
                    {
                        retData = true;
                    }
                    if ( retData != false )
                    {
                        hc.getHotelCi().setUserId( hc.getHotelCi().getUserId() );
                        hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        if ( retUse != false )
                        {
                            // HotelCi��o�^����
                            // �|�C���g�̕����𔽓]�����ēo�^
                            if ( hc.getHotelCi().getUsePoint() >= 0 )
                            {
                                hc.getHotelCi().setUsePoint( Math.abs( hc.getHotelCi().getUsePoint() ) * -1 );
                            }
                            else
                            {
                                hc.getHotelCi().setUsePoint( Math.abs( hc.getHotelCi().getUsePoint() ) );
                            }
                            hc.getHotelCi().setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            hc.getHotelCi().setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            hc.getHotelCi().setUseHotenaviId( hotenaviId );
                            hc.getHotelCi().setUseEmployeeCode( Integer.parseInt( paramEmployeeCode ) );
                            if ( hc.getHotelCi().getAllUseFlag() == 1 )
                            {
                                hc.getHotelCi().setAllUseFlag( 0 );
                                hc.getHotelCi().setAllUsePoint( 0 );
                            }
                        }
                        hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                        ret = hc.getHotelCi().insertData();
                        if ( ret != false )
                        {
                            // �ēx�ŐV�̃f�[�^���擾����
                            ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
                            /** �n�s�z�e�^�b�`�[���p�ɒǉ� **/
                            hc.registTouchCi( hc.getHotelCi(), retUse );
                            /** �n�s�z�e�^�b�`�[���p�ɒǉ� **/
                            gxTouch.setResult( RESULT_OK );
                            if ( retUse != false )
                            {
                                // �o�b�N�I�t�B�X�̃f�[�^��ǉ�
                                retData = bko.cancelBkoData( hc.getHotelCi().getUserId(), hotelId, POINT_KIND_WARIBIKI, hc.getHotelCi() );
                                if ( retData == false )
                                {
                                    errorCode = HapiTouchErrorMessage.ERR_10905;
                                }
                            }
                        }
                        else
                        {
                            gxTouch.setResult( RESULT_NG );
                            // �`�F�b�N�C���f�[�^�ǉ��G���[
                            errorCode = HapiTouchErrorMessage.ERR_10902;
                        }
                    }
                    else
                    {
                        gxTouch.setResult( RESULT_NG );
                    }
                    // �G���[�R�[�h���Z�b�g����Ă���΃G���[�R�[�h���Z�b�g����
                    if ( errorCode > 0 )
                    {
                        gxTouch.setErrorCode( errorCode );
                    }
                }
                else
                {
                    gxTouch.setResult( RESULT_NO );
                    errorCode = HapiTouchErrorMessage.ERR_10906;
                    gxTouch.setErrorCode( HapiTouchErrorMessage.ERR_10906 );
                }
            }
            else
            {
                gxTouch.setResult( RESULT_NO );
                errorCode = HapiTouchErrorMessage.ERR_10901;
                gxTouch.setErrorCode( HapiTouchErrorMessage.ERR_10901 );
            }
            if ( errorCode != 0 )
            // �G���[���������Ă����ꍇ�ɂ̓G���[�����ɏ�������
            {
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 1 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( roomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                daeh.setReserveNo( rsvNo );
                daeh.setUserId( userId );
                daeh.insertData();
            }
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
            Logging.error( "[ActionHapiTouch visitCancel]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch visitCancel]Exception:" + e.toString() );
                }
            }
        }
    }

    // �|�C���g��S�����炵�āA�A�b�v�f�[�g����
    /****
     * �|�C���g�S�g�p����
     * 
     * @param userId ���[�UID
     * @param ci �z�e���`�F�b�N�C���f�[�^
     * @param employeeCode �]�ƈ��ԍ�
     * @return
     */
    public boolean allUsePoint(String userId, HotelCi ci, int employeeCode, int point)
    {
        int nowPoint = 0;
        boolean ret = false;
        boolean retData = false;
        boolean retUse = false;
        UserPointPay upp;
        UserPointPayTemp uppt;
        upp = new UserPointPay();
        uppt = new UserPointPayTemp();
        // ���݂̃|�C���g���Z�b�g
        nowPoint = point;
        // ���Ɏg�p�}�C���f�[�^�����邩�ǂ������m�F����
        retData = uppt.getUserPointHistory( ci.getHotelCi().getUserId(), ci.getHotelCi().getId(), POINT_KIND_WARIBIKI, ci.getHotelCi().getUserSeq(), ci.getHotelCi().getVisitSeq() );
        if ( retData != false )
        {
            // �f�[�^�����邽�߁A�g�p�}�C�����X�V
            retUse = uppt.setUsePointUpdate( ci.getHotelCi().getUserId(), USE_POINT, nowPoint, nowPoint, employeeCode, ci.getHotelCi() );
            if ( retUse != false )
            {
                // hh_user_point_pay�Ɏg�p�}�C���̒ǉ�
                retUse = upp.setUsePointUpdate( ci.getHotelCi().getUserId(), USE_POINT, nowPoint, nowPoint, employeeCode, ci.getHotelCi() );
            }
        }
        else
        {
            // �f�[�^���Ȃ����߁Ahh_user_point_pay_temp�Ɏg�p�}�C����ǉ�
            retUse = uppt.setUsePoint( ci.getHotelCi().getUserId(), USE_POINT, nowPoint, nowPoint, employeeCode, ci.getHotelCi() );
            if ( retUse != false && ci.getHotelCi().getRsvNo().equals( "" ) ) // �\��No�������Ă�����g�p�}�C���͍X�V���Ȃ��B
            {
                // hh_user_point_pay�Ɏg�p�}�C���̒ǉ�
                retUse = upp.setUsePoint( ci.getHotelCi().getUserId(), USE_POINT, nowPoint, nowPoint, employeeCode, ci.getHotelCi() );
            }
        }
        // �|�C���g�����̓o�^�����܂���������X�V
        if ( retUse != false )
        {
            ci.getHotelCi().setAllUseFlag( 1 );
            ci.getHotelCi().setAllUsePoint( nowPoint );
            ci.getHotelCi().setUsePoint( nowPoint );
            ci.getHotelCi().setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            ci.getHotelCi().setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            ret = ci.getHotelCi().updateData( ci.getHotelCi().getId(), ci.getHotelCi().getSeq(), ci.getHotelCi().getSubSeq() );
            /** �n�s�z�e�^�b�`�[���p�ɒǉ� **/
            if ( ret != false )
            {
                ci.registTouchCi( ci.getHotelCi(), false );
            }
            /** �n�s�z�e�^�b�`�[���p�ɒǉ� **/
        }
        return(ret);
    }

    /****
     * �|�C���g�ԋp����
     * 
     * @param userId ���[�UID
     * @param ci �z�e���`�F�b�N�C���f�[�^
     * @param employeeCode �]�ƈ��ԍ�
     * @return
     */
    public boolean returnUsePoint(String userId, HotelCi ci, int employeeCode)
    {
        int nowPoint = 0;
        int price = 0;
        int minusPoint = 0;
        boolean retUse = false;
        UserPointPay upp;
        UserPointPayTemp uppt;
        upp = new UserPointPay();
        uppt = new UserPointPayTemp();
        // ���Ɏg�p�}�C���f�[�^�����邩�ǂ������m�F����
        uppt.getUserPointHistory( ci.getHotelCi().getUserId(), ci.getHotelCi().getId(), POINT_KIND_WARIBIKI, ci.getHotelCi().getUserSeq(), ci.getHotelCi().getVisitSeq() );
        nowPoint = ci.getHotelCi().getAllUsePoint();
        price = ci.getHotelCi().getAmount();
        if ( nowPoint >= price )
        {
            minusPoint = price;
        }
        else
        {
            minusPoint = nowPoint;
        }
        // �f�[�^�����邽�߁A�g�p�}�C�����X�V
        retUse = uppt.setUsePointUpdate( ci.getHotelCi().getUserId(), USE_POINT, nowPoint, minusPoint, employeeCode, ci.getHotelCi() );
        if ( retUse != false )
        {
            // hh_user_point_pay�Ɏg�p�}�C���̒ǉ�
            retUse = upp.setUsePointUpdate( ci.getHotelCi().getUserId(), USE_POINT, nowPoint, minusPoint, employeeCode, ci.getHotelCi() );
        }
        return(retUse);
    }

    /**
     * �X�e�[�^�X�m�F
     * 
     * @param hotelId �z�e��ID
     * @param response ���X�|���X
     **/
    public void htFrontTouch(int hotelId, int frontTouchLimit, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        errorCode = 0;
        Logging.info( "hotelId:" + hotelId + ",frontTouchLimit:" + frontTouchLimit, "[ActionHapiTouch htFrontTouch]" );
        // ���X�|���X���Z�b�g
        try
        {
            stream = response.getOutputStream();
            GenerateXmlHapiTouchFrontTouch gxFrontTouch = new GenerateXmlHapiTouchFrontTouch();
            DataApHotelTerminal daht = new DataApHotelTerminal();
            // �t�����g�^�b�`�[��������Ԃɂ���
            FrontTouchAcceptCheck ftac = new FrontTouchAcceptCheck();
            if ( ftac.updateFrontTouchEnable( hotelId, frontTouchLimit ) != false )
            {
                if ( daht.getFrontTerminal( hotelId ) != false )
                {
                    gxFrontTouch.setResult( "OK" );
                    gxFrontTouch.setErrorCode( errorCode );
                    gxFrontTouch.setLimitDate( daht.getLimitDate() );
                    gxFrontTouch.setLimitTime( daht.getLimitTime() );
                }
                else
                {
                    errorCode = HapiTouchErrorMessage.ERR_30502;
                }
            }
            else
            {
                errorCode = HapiTouchErrorMessage.ERR_30501;
            }
            if ( errorCode > 0 )
            {
                gxFrontTouch.setResult( "NG" );
                gxFrontTouch.setErrorCode( errorCode );
                gxFrontTouch.setLimitDate( 0 );
                gxFrontTouch.setLimitTime( 0 );
                DataHotelBasic dhb = new DataHotelBasic();
                String hotenaviId = "";
                String hotelName = "";
                // �z�e�i�rID���擾����
                if ( dhb.getData( hotelId ) != false )
                    ;
                {
                    hotenaviId = dhb.getHotenaviId();
                    hotelName = dhb.getName();
                }
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 1 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.insertData();
            }
            // XML�̏o��
            String xmlOut = gxFrontTouch.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch htFrontTouch]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch htFrontTouch]Exception:" + e.toString() );
                }
            }
        }
    }
}
