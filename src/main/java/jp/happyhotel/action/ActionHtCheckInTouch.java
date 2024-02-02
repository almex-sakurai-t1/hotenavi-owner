package jp.happyhotel.action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataApUuidUser;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.others.HapiTouchErrorMessage;
import jp.happyhotel.others.HapiTouchRsvSub;
import jp.happyhotel.others.TouchHistory;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.touch.FrontTouchAcceptCheck;
import jp.happyhotel.touch.HotelTerminal;
import jp.happyhotel.touch.TerminalTouch;
import jp.happyhotel.touch.TouchCi;
import jp.happyhotel.user.UserLogin;
import jp.happyhotel.user.UserLoginInfo;
import jp.happyhotel.util.AccessToken;

import org.apache.commons.lang.StringUtils;

// import jp.happyhotel.common.SSTouchDecoder;

/**
 * �n�s�z�e�A�v���`�F�b�N�C���N���X�i�����^�b�`�ネ�O�C���j
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtCheckInTouch extends BaseAction
{
    final String          URL_MATCHING          = "happyhotel.jp";
    final String          HAPITOUCH             = "hapiTouch.act?method=";
    final String          METHOD_HT_CHECKIN     = "HtCheckIn";
    final String          METHOD_HT_HOME        = "HtHome";
    final String          METHOD_HT_RSV_FIX     = "HtRsvFix";
    final String          SP_URL                = "/phone/htap/";
    final String          FILE_URL              = "common.jsp";
    final int             CI_STATUS_NOT_DISPLAY = 3;                      // �^�b�`PC�̃^�b�`�����Ɏc���Ȃ�
    private UserLoginInfo uli;

    // private SSTouchDecoder sstDecoder;

    /**
     * �n�s�z�e�^�b�`
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String reqesutURL = new String( request.getRequestURL() );

        // �n�s�z�e���ł����
        if ( reqesutURL.indexOf( URL_MATCHING ) != -1 )
        {
            decodeParam( request, response );
        }
        // �Г����ł����
        else
        {
            decodeParam( request, response );
        }

    }

    /***
     * BUG�̃��_�C���N�g�T�[�o�[�o�R�ŗ����ꍇ�̕�������
     * 
     * @param request
     * @param response
     */
    public void decodeParam(HttpServletRequest request, HttpServletResponse response)
    {

        String serialNo;
        String paramUserId;
        String paramPassword;
        String auto;

        paramUserId = request.getParameter( "user_id" );
        paramPassword = request.getParameter( "password" );
        serialNo = request.getParameter( "serialNo" );
        auto = request.getParameter( "auto" );

        try
        {
            String accessToken = StringUtils.defaultIfEmpty( request.getParameter( "t" ), "" );

            // �g�[�N�����󂯎�����ꍇ�A�g�[�N���ɐݒ肳�ꂽ���[�UID�Ƃ��̃��[�U�̃p�X���[�h��ϐ��ɐݒ�
            if ( accessToken.equals( "" ) == false )
            {
                // �g�[�N���̌��؂Ɏ��s
                if ( AccessToken.verify( accessToken ) == false )
                {
                    Logging.info( "method=HtCheckInTouch, access_token=" + accessToken );
                    Logging.warn( "token verification failed. (token: " + accessToken + ")" );
                    // �G���[����Http�X�e�[�^�X��401�ɂ���B
                    response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "�F�؃G���[" );
                    return;
                }

                // �g�[�N���̗L�������؂�
                if ( AccessToken.isWithinExpirationTime( accessToken ) == false )
                {
                    Logging.info( "method=HtCheckInTouch, access_token=" + accessToken );
                    Logging.warn( "token has expired. (token: " + accessToken + ")" );
                    // �G���[����Http�X�e�[�^�X��401�ɂ���B
                    response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "�F�؃G���[" );
                    return;
                }

                paramUserId = AccessToken.getUserId( accessToken );
                DataUserBasic dataUserBasic = new DataUserBasic();
                dataUserBasic.getData( paramUserId );
                paramPassword = UserLogin.decrypt( dataUserBasic.getPasswd() );
            }
            // �����O�C�����[�U�̏ꍇ�A�����O�C�����[�U���������[�UID�ƃp�X���[�h��ݒ�
            // else if ( paramUserId.equals( "" ) && paramPassword.equals( "" ) && accessToken.equals( "" ) )
            // {
            // paramUserId = UserLoginInfo.NON_MEMBER_USER_ID;
            // paramPassword = UserLoginInfo.NON_MEMBER_PASSWD;
            // }

            uli = new UserLoginInfo();
            uli.getUserLoginInfo( paramUserId, paramPassword );

            paramUserId = uli.getUserInfo().getUserId(); // ���[���A�h���X�̏ꍇ������̂ŁA���[�U�[ID�ɕϊ�
            registCookie( uli.getUserInfo(), request, response );

            if ( serialNo.equals( "" ) == false && paramUserId.equals( "" ) == false )
            {
                checkIn( request, response, serialNo, paramUserId, paramPassword, auto );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHtCheckInTouch.decodeParam()] Exception:" + e.toString() );
        }
    }

    /****
     * �[���ԍ��ƃ��[�UID����`�F�b�N�C���������s���B
     * 
     * @param request
     * @param response
     * @param termNo
     * @param userId
     */
    public void checkIn(HttpServletRequest request, HttpServletResponse response, String serialNo, String userId, String passWord, String auto)
    {
        int carrierFlag;
        boolean ret = false;
        boolean boolRegTc = false;// �^�b�`�f�[�^�o�^
        boolean boolExistRsv = false;
        FormReserveSheetPC frm = null;
        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();

        String idm = "";
        int hotelId = 0;// �z�e��ID
        int termId = 0;// �z�e�����Ƃ̒[��ID
        int termNo = 0;// �^�[�~�i��No
        int roomNo = 0;// �����ԍ�
        int ttRoomNo = 0;// ��������擾���������ԍ�
        String roomName = "";// ��������
        String ttRoomName = "";// ��������擾������������
        String hotelName = ""; // �z�e����
        String hotenaviId = ""; // �z�e�i�rID
        String rsvNo = "";
        String touchIp = "";
        String touchUseragent = "";
        int errorCode = 0;
        String forwardUrl = "";
        int ciSeq = 0; // �`�F�b�N�C���R�[�h
        String uidLink = "";
        String paramType = "";
        String hotelIp = "";
        int terminalFlag = 0;
        boolean reTouch = false;
        boolean isReplaceUserId = false;

        DataHotelBasic dhb = new DataHotelBasic();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();
        DataUserBasic dub = new DataUserBasic();
        DataApHotelSetting dahs = new DataApHotelSetting();
        DataApUuidUser dauu = new DataApUuidUser();
        HotelTerminal ht = new HotelTerminal();
        TerminalTouch tt = new TerminalTouch();
        TouchCi tc = new TouchCi();
        HotelCi hc = new HotelCi();

        try
        {
            touchIp = request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr();
            touchUseragent = request.getHeader( "User-Agent" );
            paramType = request.getParameter( "type" );

            carrierFlag = UserAgent.getUserAgentTypeFromTouch( request );
            // URL�𔻒f
            if ( carrierFlag == DataMasterUseragent.CARRIER_SMARTPHONE )
            {
                forwardUrl = SP_URL;
            }
            else if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
            {
                forwardUrl = "/i/htap/";
                uidLink = "&uid=NULLGWDOCOMO";
            }
            else if ( carrierFlag == DataMasterUseragent.CARRIER_AU )
            {
                forwardUrl = "/au/htap/";
            }
            else if ( carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK )
            {
                forwardUrl = "/y/htap/";
                uidLink = "&uid=1&sid=EIJC&pid=P0P3";
            }
            else
            {
                if ( UserAgent.getUserAgentTypeString( request ).equals( "ipa" ) ||
                        UserAgent.getUserAgentTypeString( request ).equals( "ada" ) )
                {
                    forwardUrl = SP_URL;
                }
            }

            if ( paramType == null || paramType.equals( "resserve" ) == false )
            {
                paramType = "";
            }

            ret = dub.getData( userId );
            if ( ret == false )
            {
                userId = "";
            }
            if ( userId.equals( "" ) != false )
            {
                ret = false;
                // �������^�b�`���Ă���ꍇ
                errorCode = HapiTouchErrorMessage.ERR_30102;
            }
            if ( ret != false )
            {

                if ( serialNo.equals( "" ) != false )
                {
                    ret = false;
                    // �[��ID���擾�ł��Ȃ��ꍇ
                    errorCode = HapiTouchErrorMessage.ERR_30110;
                }
            }

            int appStatus = 1; // 0:�����O�C�����,1:���O�C�����
            // ap_uuid_user �ɓo�^����Ă��Ȃ����[�U�[�͓��Ӎς݃��[�U�[�Ƃ݂Ȃ��B
            if ( dauu.getAppData( userId ) != false )
            {
                appStatus = dauu.getAppStatus();
            }

            if ( ret != false )
            {
                // �[���̃V���A���ԍ�����z�e��ID�A�����ԍ����擾
                if ( ht.getHotelTerminal( serialNo ) == false )
                {
                    errorCode = HapiTouchErrorMessage.ERR_30110;
                    ret = false;
                }
                else if ( ht.getTerminal().getStartDate() > Integer.parseInt( DateEdit.getDate( 2 ) ) || ht.getTerminal().getEndDate() < Integer.parseInt( DateEdit.getDate( 2 ) ) )
                {
                    /* �[���̓��t�͈͂��L���łȂ��ꍇ */
                    errorCode = HapiTouchErrorMessage.ERR_30122;
                    ret = false;
                }
                else
                {
                    hotelId = ht.getTerminal().getId();
                    termId = ht.getTerminal().getTerminalId();
                    termNo = ht.getTerminal().getTerminalNo();
                    roomNo = ht.getTerminal().getRoomNo();
                    if ( dhb.getData( hotelId ) != false )
                    {
                        hotelName = dhb.getName();
                        hotenaviId = dhb.getHotenaviId();
                    }
                    else
                    {
                        ret = false;
                        // �z�e���}�X�^���擾�ł��Ȃ��ꍇ
                        errorCode = HapiTouchErrorMessage.ERR_30114;
                    }

                    if ( roomNo != 0 )// �t�����g�ȊO
                    {
                        if ( dhrm.getData( hotelId, roomNo ) != false )
                        {
                            if ( dhrm.getRoomNameHost().equals( "" ) )
                            {
                                ret = false;
                                // �����}�X�^���擾�ł��Ȃ��ꍇ(�f�[�^���Ȃ��Ƃ�)
                                errorCode = HapiTouchErrorMessage.ERR_30115;
                            }
                            else
                            {
                                roomName = dhrm.getRoomNameHost();
                            }
                        }
                        else
                        {
                            ret = false;
                            // �����}�X�^���擾�ł��Ȃ��ꍇ(Exception�̂Ƃ�)
                            errorCode = HapiTouchErrorMessage.ERR_30115;
                        }
                    }
                    if ( dahs.getData( hotelId ) != false )
                    {
                        terminalFlag = dahs.getTerminalFlag(); // �^�[�~�i���t���O�F1����^�[�~�i���[������
                        if ( terminalFlag <= 0 )
                        {
                            ret = false;
                            // �[��ID���L���ł͂Ȃ��ꍇ
                            errorCode = HapiTouchErrorMessage.ERR_30111;
                        }

                    }
                    else
                    {
                        ret = false;
                        // �z�e���ݒ�t�@�C���擾�ł��Ȃ��ꍇ
                        errorCode = HapiTouchErrorMessage.ERR_30116;
                    }
                    if ( ret != false )
                    {

                        // �t�����g��IP�A�h���X���擾
                        hotelIp = HotelIp.getFrontIp( hotelId );

                        boolean boolTerminal = false;
                        if ( roomNo == 0 ) // �t�����g�^�b�`
                        {
                            boolTerminal = tt.getTerminalTouch( hotelId, termId );
                        }
                        else
                        {
                            boolTerminal = tt.getTerminalTouchFromRoomNo( hotelId, roomNo );
                        }
                        // �z�e��id�A�[��ID����^�b�`�[�������f�[�^���擾

                        if ( boolTerminal != false )
                        {
                            ciSeq = tt.getTerminal().getCiSeq();
                            ttRoomNo = tt.getTerminal().getRoomNo();
                            if ( dhrm.getData( hotelId, ttRoomNo ) != false )
                            {
                                ttRoomName = dhrm.getRoomNameHost();
                            }
                            else
                            {
                                ret = false;
                                // �����}�X�^���擾�ł��Ȃ��ꍇ
                                errorCode = HapiTouchErrorMessage.ERR_30115;
                            }

                            // �����A�����[�U�E�������ōŐV�`�F�b�N�C���R�[�h������̂Ȃ炻���D�悷��
                            if ( hc.getCheckInCode( hotelId, ciSeq, ttRoomName, userId ) )
                            {
                                if ( hc.getHotelCi().getSeq() > ciSeq )
                                {
                                    ciSeq = hc.getHotelCi().getSeq();
                                }
                                rsvNo = hc.getHotelCi().getRsvNo();
                                if ( rsvNo.equals( "" ) == false )
                                {
                                    boolExistRsv = true;
                                }
                                if ( hc.getHotelCi().getCiStatus() == 0 || (hc.getHotelCi().getCiStatus() == 3 && hc.getHotelCi().getExtUserFlag() == 1) )
                                {
                                    reTouch = true; // �ă^�b�`�Ƃ�������
                                }
                            }
                        }
                    }
                }

                if ( ret != false )
                {
                    // 2015.03.22 �\��f�[�^���擾:�t�����g�^�b�`�̋������邽�߂Ɏ擾����ύX tashiro
                    // ���[�U�[�̗\��f�[�^�̎擾
                    frm = htRsvSub.getReserveData( userId, hotelId, ciSeq );
                    // �擾�����\��f�[�^���A���\��̓r���̓��t�̏ꍇ�A�e�\��f�[�^���擾������
                    if ( StringUtils.isNotBlank( frm.getRsvNoMain() ) && !frm.getRsvNo().substring( frm.getRsvNo().indexOf( "-" ) + 1 ).equals( frm.getRsvNoMain() ) )
                    {
                        String reserveNo = htRsvSub.getMainReserveNo( hotelId, frm.getRsvNoMain() );
                        if ( StringUtils.isNotBlank( reserveNo ) )
                        {
                            frm = htRsvSub.getReserveData( userId, hotelId, reserveNo );
                        }
                    }
                    if ( frm.getSeq() != 0 && roomNo != 0 ) // �t�����g�^�b�`�̏ꍇ�͗\�񕔉����̂��g��Ȃ�
                    {
                        if ( roomNo != frm.getSeq() ) // �t�����g�^�b�`�ł͂Ȃ��ė\��ē������ȊO�ɓ��������ꍇ�ɂ̓G���[�ɂ���B
                        {
                            ret = false;
                            errorCode = HapiTouchErrorMessage.ERR_30121;
                        }
                        else if ( dhrm.getData( hotelId, frm.getSeq() ) != false )// �������̂��擾
                        {
                            roomName = dhrm.getRoomNameHost();
                        }
                    }
                    if ( ret != false )
                    {

                        // �����[�U�[�̓��z�e���ł�24���Ԉȓ��̃`�F�b�N�C���𒲂ׂ�
                        if ( hc.getCheckInBeforeData( hotelId, userId ) != false )
                        {
                            if ( (!frm.getRsvNo().equals( "" ) || !hc.getHotelCi().getRsvNo().equals( "" )) && !frm.getRsvNo().equals( hc.getHotelCi().getRsvNo() ) )
                            {
                                // �`�F�b�N�C�����̗\��No�ƈႤ�̂ŃG���[�Ƃ���B
                                ret = false;
                                ciSeq = hc.getHotelCi().getSeq();
                                errorCode = HapiTouchErrorMessage.ERR_30120;
                            }
                            else
                            {
                                rsvNo = frm.getRsvNo();
                                if ( rsvNo.equals( "" ) == false )
                                {
                                    boolExistRsv = true;
                                }
                            }
                            if ( ret != false )
                            {
                                // �t�����g�^�b�`�̏ꍇ�́A�`�F�b�N�C���f�[�^�̕������̂ɂ���B
                                if ( roomNo == 0 )
                                {
                                    roomName = hc.getHotelCi().getRoomNo();
                                }
                                if ( hotelIp.equals( "" ) == false )
                                {
                                    // 2015.03.20 �����ԍ�������Ă�����G���[ tashiro
                                    if ( hc.getHotelCi().getRoomNo().equals( roomName ) == false )
                                    {
                                        ret = false;
                                        ciSeq = hc.getHotelCi().getSeq();
                                        errorCode = HapiTouchErrorMessage.ERR_30106;
                                    }
                                    else
                                    {
                                        TouchCi.registTouchCi( hc.getHotelCi() );// ap_touch_ci �Ɠ���
                                    }
                                }
                            }
                        }
                        else
                        {
                            rsvNo = frm.getRsvNo();
                            if ( rsvNo.equals( "" ) == false )
                            {
                                boolExistRsv = true;
                            }
                        }
                    }
                }
                if ( ret != false )
                {
                    // �擾�����^�b�`�[�������f�[�^����A���̕����̃^�b�`�������Ăяo��(24���Ԍo�߂��Ă�����f�[�^�͎擾���Ȃ��B)
                    // 2015.03.21 �t�����g�Ń^�b�`�����ꍇ�͉��L�̃��W�b�N��ʂ��Ȃ��B tashiro
                    if ( roomNo > 0 )
                    {
                        if ( hc.getDataFromRoom( hotelId, roomName ) != false ) // �����ɈȑO�̃`�F�b�N�C���f�[�^������
                        {
                            // �`�F�b�N�C���R�[�h�̓ǂݍ���
                            ciSeq = hc.getHotelCi().getSeq();

                            // �f�[�^������AciStatus != 0�̏ꍇ��ap_touch_ci��������
                            // �܂���IDM������Ă���E�}�C�����g�p����Ă��Ȃ��ꍇ��ap_touch_ci�ɏ������݂��s���B
                            if ( hc.getHotelCi().getCiStatus() != 0 && hc.getHotelCi().getCiStatus() != 4 )
                            {
                                boolRegTc = true;
                            }
                            else if ( userId.equals( hc.getHotelCi().getUserId() ) == false )
                            {
                                {
                                    // �}�C���g�p�ς݂̏ꍇ�͐V�����^�b�`����t���Ȃ�
                                    if ( hc.getHotelCi().getUsePoint() > 0 )
                                    {
                                        ret = false;
                                        errorCode = HapiTouchErrorMessage.ERR_30104;
                                    }
                                    // �z�X�g�A�������ŁA�����o�[�J�[�h��t�ς݂̏ꍇ�͐V�����^�b�`����t���Ȃ�
                                    else if ( hotelIp.equals( "" ) == false && hc.getHotelCi().getCustomId().equals( "" ) == false )
                                    {
                                        ret = false;
                                        errorCode = HapiTouchErrorMessage.ERR_30105;
                                    }
                                    // ���Ɋ����ς݂̃f�[�^�������ȊO�̏ꍇ�A�^�b�`�����l�������ł���Ώ㏑���s��
                                    // hh_hotel_ci�������ŏ㏑�������͔̂����邽��
                                    else if ( hc.getHotelCi().getUserType() != 99 && userId.equals( "" ) != false )
                                    {
                                        ret = false;
                                        errorCode = HapiTouchErrorMessage.ERR_30109;
                                    }
                                    // ���Ɋ����ς݂̃f�[�^���\��`�F�b�N�C���̏ꍇ�͎󂯕t���Ȃ�
                                    else if ( hc.getHotelCi().getRsvNo().equals( "" ) == false )
                                    {
                                        ret = false;
                                        errorCode = HapiTouchErrorMessage.ERR_30120;
                                    }
                                    else
                                    {
                                        if ( appStatus == 0 )
                                        {
                                            ret = false;
                                            errorCode = HapiTouchErrorMessage.ERR_30109;
                                        }
                                        else
                                        {
                                            boolRegTc = true;
                                            if ( tc.getData( hotelId, ciSeq ) != false )
                                            {
                                                tc.getTouchCi().setCiStatus( CI_STATUS_NOT_DISPLAY );
                                                tc.getTouchCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                                tc.getTouchCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                                tc.getTouchCi().updateData( hotelId, ciSeq );
                                                tc.registHotelCi( tc.getTouchCi() );
                                            }
                                            else
                                            {
                                                hc.getHotelCi().setCiStatus( CI_STATUS_NOT_DISPLAY );
                                                hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                                hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                                hc.getHotelCi().updateData( hotelId, ciSeq, hc.getHotelCi().getSubSeq() );
                                            }
                                            /* �`�F�b�N�C���f�[�^��V�����쐬�͂��邪�A�`�F�b�N�C���R�[�h�͐V�������Ȃ� ���[�U�[ID��u�������� */
                                            isReplaceUserId = true;
                                        }
                                    }
                                }
                            }
                        }
                        // �^�b�`�������[�U�[�ł̃`�F�b�N�C���f�[�^���Ȃ����A���̃��[�U�[�ł̃`�F�b�N�C���f�[�^�͂Ȃ����낤���H
                        else
                        {
                            boolRegTc = true;
                            // 2015.06.11 �z�X�g�A�������ŁA���[�U�̈ȑO�̃`�F�b�N�C���f�[�^������
                            if ( hotelIp.equals( "" ) == false )
                            {
                                boolRegTc = true;
                                // 2015.06.11 �z�X�g�A�������ŁA���[�U�̈ȑO�̃`�F�b�N�C���f�[�^������
                                if ( hotelIp.equals( "" ) == false && hc.getDataFromRoom( hotelId, roomName ) != false )
                                {
                                    // �Ȃ񂾂��m��Ȃ����ǁA���������Ƀ`�F�b�N�C���f�[�^������B�t�����g�ł̗\��`�F�b�N�C���ȂǂȂ�
                                    if ( !hc.getHotelCi().getRsvNo().equals( "" ) )
                                    {
                                        errorCode = HapiTouchErrorMessage.ERR_30120;
                                        ret = false;
                                        boolRegTc = false;
                                    }
                                }
                            }
                            // �Ώۃf�[�^���Ȃ������̂�����\��No�̓N���A����
                            hc.getHotelCi().setRsvNo( "" );
                        }
                    }
                    if ( ret != false )
                    {
                        // 2015.03.22 �\�񂪂Ȃ��ꍇ�Ń^�b�`���ꂽ�������t�����g�������ꍇ tashiro
                        if ( boolExistRsv == false && roomName.equals( "" ) != false )
                        {
                            FrontTouchAcceptCheck ftac = new FrontTouchAcceptCheck();

                            // 2015.03.22 �t�����g�̃^�b�`���L�����ǂ������m�F���� tashiro
                            if ( ftac.check( ht.getTerminal() ) == false )
                            {
                                errorCode = HapiTouchErrorMessage.ERR_30107;
                                ret = false;
                                boolRegTc = false;
                            }
                            else
                            {
                                boolRegTc = true;
                            }
                        }
                        else if ( boolExistRsv )
                        {
                            if ( roomName.equals( "" ) != false ) // �\��Ńt�����g��������
                            {
                                boolRegTc = true;
                            }
                            else
                            {
                                if ( !hc.getHotelCi().getRsvNo().equals( "" ) )
                                {
                                    boolRegTc = false;
                                }
                                else
                                {
                                    boolRegTc = true;
                                }
                            }
                        }
                    }

                    // �`�F�b�N�C���f�[�^�쐬��������`�F�b�N�C���f�[�^�쐬
                    if ( boolRegTc != false && ret != false )
                    {
                        // ap_touch_ci�f�[�^�ɏ������� ������hh_hotel_ci�ɂ��������܂��
                        tc = tc.registCiData( userId, idm, hotelId, roomName, isReplaceUserId, ciSeq );

                        // �������݌�̃`�F�b�N�C���R�[�h�̓ǂݍ���
                        ciSeq = tc.getTouchCi().getSeq();

                        if ( roomNo == 0 ) // �t�����g�`�F�b�N�C���̏ꍇ�́A�\��̕����ԍ����Z�b�g
                        {
                            roomNo = frm.getSeq();
                        }

                        // �`�F�b�N�C���f�[�^���MOK�ł���΁Aap_terminal_touch�f�[�^�ɏ�������(ci_seq��������)
                        ret = tt.registTerminalTouch( tt.getTerminal(), hotelId, termId, userId, idm, roomNo, touchIp, touchUseragent, ciSeq );

                        // �`�F�b�N�C���f�[�^�쐬�Ɏ��s������G���[
                        if ( ret == false )
                        {
                            errorCode = HapiTouchErrorMessage.ERR_30101;
                        }
                    }
                }
            }

            // �z�e���^�b�`����������
            TouchHistory touchHistory = null;
            touchHistory = new TouchHistory();
            touchHistory.touchHistory( hotelId, "HtCheckInTouch", String.valueOf( ciSeq ), userId, request );

            // �`�F�b�N�C�����o�^�ł�����z�[����ʂցA���s������G���[��ʂ�
            if ( ret != false )
            {
                // �\��ԍ����擾�ł�����A�\�񗈓X�����̉�ʂ֑J�ڂ���
                if ( rsvNo.equals( "" ) == false )
                {
                    response.sendRedirect( forwardUrl + HAPITOUCH + METHOD_HT_RSV_FIX + "&id=" + hotelId + "&seq=" + ciSeq + "&rsvNo=" + rsvNo + "&reTouch=" + reTouch + uidLink );
                    return;
                }
                else
                {
                    response.sendRedirect( forwardUrl + HAPITOUCH + METHOD_HT_HOME + "&id=" + hotelId + "&seq=" + ciSeq + "&user_id=" + userId + "&password=" + passWord + "&reTouch=" + reTouch + "&auto=" + auto + uidLink );
                    return;
                }
            }
            else
            {
                // �G���[���e��o�^
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setTerminalId( termId );
                daeh.setTerminalNo( termNo );
                daeh.setRoomName( roomName );
                daeh.setRoomNo( roomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( ciSeq );
                daeh.setReserveNo( rsvNo );
                daeh.setUserId( userId );
                daeh.insertData();

                // ���_�C���N�gURL���Z�b�g
                forwardUrl += "error.jsp?id=" + hotelId;
                forwardUrl += "&seq=" + ciSeq;
                forwardUrl += "&reserveNo=" + rsvNo;
                forwardUrl += "&roomNo=" + roomNo;
                forwardUrl += "&errorCode=" + errorCode;
                forwardUrl += "&head=1";

                // requestDispatcher = request.getRequestDispatcher( forwardUrl + FILE_URL );
                // requestDispatcher.forward( request, response );

                response.sendRedirect( forwardUrl );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionHtCheckInTouch.checkIn()]Exception:" + exception, "ActionHtCheckInTouch.checkIn()" );
        }
        finally
        {
        }
    }

    /**
     * 
     * @param uli
     * @param request
     * @param response
     */
    public void registCookie(DataUserBasic uli, HttpServletRequest request, HttpServletResponse response)
    {
        String requestURL = new String( request.getRequestURL() );
        Cookie hhcookie = null;
        String cookieValue = "";

        if ( uli.getMailAddrMd5().compareTo( "" ) != 0 )
        {
            cookieValue = uli.getMailAddrMd5();
        }
        else if ( uli.getMailAddrMobileMd5().compareTo( "" ) != 0 )
        {
            cookieValue = uli.getMailAddrMobileMd5();
        }
        else
        {
            cookieValue = uli.getUserId();
        }
        hhcookie = new Cookie( "hhuid", cookieValue );
        hhcookie.setPath( "/" );

        // �n�s�z�e���ł����
        if ( requestURL.indexOf( URL_MATCHING ) != -1 )
        {
            hhcookie.setDomain( ".happyhotel.jp" );
        }
        else
        {
            hhcookie.setDomain( "121.101.88.177" );
        }
        hhcookie.setMaxAge( Integer.MAX_VALUE );

        // ���O�C������
        response.addCookie( hhcookie );

        // �N�b�L�[("u"=)�쐬
        UserLogin login = new UserLogin();
        if ( login.makeCookieValue( uli.getUserId() ) != false )
        {
            cookieValue = login.getCookieValue();
            hhcookie = new Cookie( "u", cookieValue );
            hhcookie.setPath( "/" );
            hhcookie.setDomain( ".happyhotel.jp" );
            hhcookie.setMaxAge( Integer.MAX_VALUE );
        }
        else
        {
            // �N�b�L�[�폜
            hhcookie = new Cookie( "u", "" );
            hhcookie.setPath( "/" );
            hhcookie.setMaxAge( 0 );
            hhcookie.setDomain( ".happyhotel.jp" );
            hhcookie.setValue( "" );
        }
        // ���O�C������
        response.addCookie( hhcookie );
    }

}
