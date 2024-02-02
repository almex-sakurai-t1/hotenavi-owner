package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.dto.DtoApCommon;
import jp.happyhotel.dto.DtoApCouponList;
import jp.happyhotel.dto.DtoApCouponUnit;
import jp.happyhotel.dto.DtoApHotelCustomer;
import jp.happyhotel.dto.DtoApHotelCustomerData;
import jp.happyhotel.dto.DtoApInquiryForm;
import jp.happyhotel.dto.DtoApReserve;
import jp.happyhotel.dto.DtoApUserInfo;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.others.HapiTouchErrorMessage;
import jp.happyhotel.others.HapiTouchRsvSub;
import jp.happyhotel.owner.LogicOwnerRsvRoomChange;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.touch.HotelTerminal;
import jp.happyhotel.touch.RsvFix;
import jp.happyhotel.touch.RsvList;
import jp.happyhotel.touch.TerminalTouch;
import jp.happyhotel.touch.TouchCi;
import jp.happyhotel.touch.TouchReception;
import jp.happyhotel.user.UserBasicInfo;

import org.apache.commons.lang.StringUtils;

/**
 * �n�s�z�e�A�v���`�F�b�N�C���N���X
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtRsvFix extends BaseAction
{

    final int                      TIMEOUT               = 10000;
    final String                   HTTP                  = "http://";
    final String                   CLASS_NAME            = "hapiTouch.act?method=";
    final String                   METHOD_RSV_FIX        = "HtRsvFix";
    final int                      RESULT_OK             = 1;
    final int                      RESULT_NG             = 2;
    final int                      HOTENAVI_PORT_NO      = 7023;
    final int                      HAPIHOTE_PORT_NO      = 7046;
    final int                      CI_STATUS_NOT_DISPLAY = 3;                      // �n�s�z�e�^�b�`��\��
    final int                      CI_STATUS_EXT         = 4;                      // �O���\��̃^�b�`��\��
    private RequestDispatcher      requestDispatcher;
    private DtoApCommon            apCommon;
    private DtoApReserve           apReserve;
    private DtoApCouponList        apCouponList;
    private DtoApInquiryForm       apInquiry;
    private DtoApHotelCustomer     apHotelCustomer;
    private DtoApHotelCustomerData apHotelCustomerData;
    private DtoApUserInfo          apUserInfo;

    /**
     * �n�s�z�e�^�b�`
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        // XML�o��
        boolean ret = false;
        boolean boolConnected = false;
        boolean boolExistRsv = false;
        boolean boolGuide = false;
        boolean reTouch = false;

        int roomNo = 0;
        String userId = "";
        String hapihoteIp = "";
        String[] anotherRoomList = null;
        int anotherRoomCount = 0;
        String paramId;
        String paramSeq;
        String paramRsvNo = "";
        String touchRoom = "";

        String hotelName = "";
        String hotenaviId = "";

        DataUserBasic dub = new DataUserBasic();
        HotelTerminal ht = new HotelTerminal();
        TerminalTouch tt = new TerminalTouch();
        DataHotelBasic dhb = new DataHotelBasic();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();
        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();
        TouchCi tc = new TouchCi();
        HotelCi hc = new HotelCi();
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicOwnerRsvRoomChange logicRoom = new LogicOwnerRsvRoomChange();
        RsvFix rf = new RsvFix();
        DataApHotelSetting dahs = new DataApHotelSetting();

        /* �^�b�`��t�Ƌ��� */
        String customId = "";
        String unavailableMessage = "";
        String customRank = "";
        int point = 0;
        int point2 = 0;
        int errorCode = 0;
        boolean boolHotelCustom = false;// �ڋq�Ή���
        boolean boolMemberCheckIn = false; // �����o�[�`�F�b�N�C������
        boolean boolMemberAccept = false;// �����o�[����t����
        boolean boolMemberInfo = false;// �����o�[���擾����
        boolean boolUseableMile = true;// �}�C���g�p�\�t���O
        int nonrefundableFlag = 0; // 0:�ԋ�OK�A1:�ԋ��s�A2:�N���W�b�g�O��̂ݕԋ��s��
        ArrayList<DtoApCouponUnit> apCouponUnitList = null;
        /* �^�b�`��t�Ƌ��� */

        boolean boolMemberCardIssued = false; // �����o�[�J�[�h���s�L��
        int goodsCode = 0; // ���i�R�[�h
        int goodsPrice = 0; // ���z

        try
        {

            paramId = request.getParameter( "id" );
            paramSeq = request.getParameter( "seq" );
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( paramSeq == null || paramSeq.equals( "" ) != false || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( request.getParameter( "reTouch" ) != null )
            {
                reTouch = request.getParameter( "reTouch" ).equals( "true" );
            }
            if ( request.getParameter( "rsvNo" ) != null )
            {
                paramRsvNo = request.getParameter( "rsvNo" );
            }

            frm = new FormReserveSheetPC();

            if ( Integer.parseInt( paramId ) > 0 && Integer.parseInt( paramSeq ) > 0 )
            {

                dhb.getData( Integer.parseInt( paramId ) );
                hotelName = dhb.getName();
                hotenaviId = dhb.getHotenaviId();

                // �z�e���̃t�����gIP���擾
                hapihoteIp = HotelIp.getFrontIp( Integer.parseInt( paramId ) );
                ret = hc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                dhrm.getData( Integer.parseInt( paramId ), hc.getHotelCi().getRoomNo() );
                touchRoom = dhrm.getRoomNameHost();
                roomNo = dhrm.getRoomNo();

                // �z�e���[���f�[�^�擾
                ht.getHotelTerminal( Integer.parseInt( paramId ), dhrm.getSeq() );

                // �z�e���ݒ���擾
                dahs.getData( Integer.parseInt( paramId ) );

                userId = hc.getHotelCi().getUserId();

                dub.getData( userId );

                // �ڋq���s���Ă��邩�ǂ���
                if ( dahs.getCustomFlag() > 0 )
                {
                    boolHotelCustom = true;
                }
                nonrefundableFlag = dahs.getNonrefundableFlag();

                // �\��f�[�^���擾
                if ( StringUtils.isBlank( paramRsvNo ) )
                {
                    // �p�����[�^�\��ԍ��������Ă��Ȃ��Ƃ�
                    frm = htRsvSub.getReserveData( userId, Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                }
                else
                {
                    // �p�����[�^�\��ԍ��������Ă���Ƃ�
                    frm = htRsvSub.getReserveData( userId, Integer.parseInt( paramId ), paramRsvNo );
                }
                frm.setLoginUserId( frm.getUserId() );
                // �\��ԍ����擾�ł��Ă����OK�Ƃ݂Ȃ��B
                if ( frm.getRsvNo().equals( "" ) == false )
                {

                    boolExistRsv = true;
                    boolGuide = true;

                    // �t�����g�Ń^�b�`�����ꍇ�́A�\��ŗ}�����镔���̒��ŁA�\��̓����Ă��Ȃ��������擾����
                    // �����[���Ń^�b�`�����ꍇ�́A���̕������󂯓n���A�v�����̑ΏۂłȂ���ΑΏۂƂȂ�Ȃ��Ȃ�

                    anotherRoomList = htRsvSub.getAnotherRoom( Integer.parseInt( paramId ), frm.getSelPlanId(), frm.getSelPlanSubId(), frm.getRsvDate(), touchRoom, frm.getRsvNo() );
                    anotherRoomCount = anotherRoomList.length;

                    // �t�����g�̏ꍇ�́A�\�񕔉��Ŏ擾���Ȃ����B
                    if ( touchRoom.equals( "" ) )
                    {
                        dhrm.getData( Integer.parseInt( paramId ), frm.getRoomHold() == 0 ? frm.getSeq() : frm.getRoomHold() );
                    }

                    // IP�A�h���X������΃z�X�g�֑΂��ă`�F�b�N�C���f�[�^�𑗐M
                    if ( hapihoteIp.equals( "" ) == false )
                    {
                        // �ڑ��ݒ��true��
                        boolConnected = true;

                        /** �i1004�j�n�s�z�e_�n�s�z�e�^�b�`�\�񗈓X�v�� **/
                        rf.setSeq( hc.getHotelCi().getSeq() );
                        rf.setRsvNo( frm.getRsvNo() );
                        rf.setRoomName( dhrm.getRoomNameHost() );
                        rf.setDispRsvNo( frm.getRsvNo().substring( frm.getRsvNo().length() - 6 ) );
                        rf.setRoomNameListLength( anotherRoomCount );
                        rf.setRoomNameList( anotherRoomList );
                        rf.setTouchRoomName( touchRoom );

                        if ( frm.getStatus() != ReserveCommon.RSV_STATUS_ZUMI ) // ���X�ς݂̏ꍇ�̓z�X�g�ւ̒ʒm�����Ȃ�
                        {
                            /*                  */
                            /* �`�F�b�N�C���ʒm */
                            /*                  */
                            rf.sendToHost( hapihoteIp, TIMEOUT, HAPIHOTE_PORT_NO, paramId );

                            if ( rf.getResult() == RESULT_OK )
                            {
                                // �\��U����OK�ɂ���
                                boolGuide = true;
                                // �\�񗈓X�����ŕ����ԍ��f�[�^���擾�ł����ꍇ
                                if ( rf.getGuidRoomName().trim().equals( "" ) == false )
                                {
                                    // �^�b�`���̕����ƗU���������Ⴄ�ꍇ�̓f�[�^����蒼��
                                    if ( touchRoom.trim().equals( rf.getGuidRoomName().trim() ) == false )
                                    {
                                        dhrm.getData( Integer.parseInt( paramId ), rf.getGuidRoomName().trim() );
                                        roomNo = dhrm.getRoomNo();
                                    }
                                }
                                hc.getHotelCi().setRoomNo( dhrm.getRoomNameHost() );

                                // �U�����������̃^�b�`�f�[�^�ɒǉ�
                                tt.registData( Integer.parseInt( paramId ), hc, roomNo );
                            }
                            else
                            {
                                // 2015.03.21 �G���[�R�[�h���Z�b�g����Ă��Ȃ������̂ŃG���[�R�[�h���Z�b�g tahsiro
                                errorCode = rf.getErrorCode();
                                if ( errorCode == 0 )
                                {
                                    // 2015.03.21 �z�X�g�ŃG���[�R�[�h���Z�b�g����ĂȂ�������A�z�X�g�ւ̐ڑ������s�����G���[���Z�b�g tashiro
                                    errorCode = HapiTouchErrorMessage.ERR_30302;
                                }

                                // 2015.03.19 �z�X�g�̃G���[�̏ꍇ�̓^�b�`���\���ɂ��čX�V����B
                                // 2015.03.19 �G���[���̓^�b�`PC�ɂ��c��Ȃ��悤�ɂ���B tashiro
                                if ( reTouch )// ��v���ōă^�b�`�̏ꍇ�ɂ͖����ɂ��Ȃ�
                                {
                                }
                                else
                                {
                                    // �\��U����NG�ɂ���
                                    boolGuide = false;
                                    if ( tc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) ) != false )
                                    {
                                        tc.getTouchCi().setCiStatus( CI_STATUS_NOT_DISPLAY );
                                        tc.getTouchCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                        tc.getTouchCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                        tc.getTouchCi().updateData( Integer.parseInt( paramId ), dhrm.getSeq() );
                                        tc.registHotelCi( tc.getTouchCi() );
                                    }
                                    else
                                    {
                                        hc.getHotelCi().setCiStatus( CI_STATUS_NOT_DISPLAY );
                                        hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                        hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                        hc.getHotelCi().updateData( Integer.parseInt( paramId ), dhrm.getSeq(), hc.getHotelCi().getSubSeq() );
                                    }
                                }
                            }
                        }
                        else
                        {
                            boolGuide = true;
                        }
                    }
                    // �z�X�g��A�������܂��̓z�X�g�A�������ŗU���ł��������̂ݗ��X�������s���B
                    if ( boolConnected == false || boolGuide != false )
                    {
                        // �\�񕔉��ƃ^�b�`�������������
                        if ( frm.getSeq() != dhrm.getSeq() && dhrm.getRoomNo() > 0 && frm.getStatus() == ReserveCommon.RSV_STATUS_UKETUKE )
                        {
                            frm.setOrgRsvSeq( frm.getSeq() );
                            frm.setSeq( roomNo );

                            logicRoom.setFrm( frm );
                            logicRoom.updReserve();
                        }

                        UserBasicInfo ubi = new UserBasicInfo();
                        int extUserFlag = 0;
                        if ( ubi.isLvjUser( userId ) )
                        {
                            extUserFlag = 1;
                        }

                        if ( tc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) ) != false )
                        {
                            // �\�񗈓X�̃L�����Z���������ꍇ�́Aap_touch_ci �͍X�V���Ȃ��Ȃ�B
                            tc.getTouchCi().setRsvNo( frm.getRsvNo() );
                            tc.getTouchCi().setExtUserFlag( extUserFlag );
                            if ( extUserFlag == 1 )
                            {
                                tc.getTouchCi().setCiStatus( CI_STATUS_EXT );
                            }
                            else
                            {
                                tc.getTouchCi().setCiStatus( 0 );
                            }
                            tc.getTouchCi().setRoomNo( dhrm.getRoomNameHost() );

                            if ( frm.getUsedMile() != 0 )
                            {
                                tc.getTouchCi().setUsePoint( frm.getUsedMile() );
                                tc.getTouchCi().setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                tc.getTouchCi().setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            }
                            tc.getTouchCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            tc.getTouchCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            tc.getTouchCi().updateData( Integer.parseInt( paramId ), tc.getTouchCi().getSeq() );
                            tc.registHotelCi( tc.getTouchCi() );
                            hc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) ); // hh_hotel_ci �X�V��ēǂݍ���
                        }
                        else
                        {
                            hc.getHotelCi().setRsvNo( frm.getRsvNo() );
                            hc.getHotelCi().setExtUserFlag( extUserFlag );
                            if ( extUserFlag == 1 )
                            {
                                hc.getHotelCi().setCiStatus( CI_STATUS_EXT );
                            }
                            if ( frm.getUsedMile() != 0 )
                            {
                                hc.getHotelCi().setUsePoint( frm.getUsedMile() );
                                hc.getHotelCi().setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                hc.getHotelCi().setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            }
                            hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

                            if ( hc.getHotelCi().getCiStatus() == 2 )
                            {
                                if ( extUserFlag == 1 )
                                {
                                    hc.getHotelCi().setCiStatus( CI_STATUS_EXT );
                                }
                                else
                                {
                                    hc.getHotelCi().setCiStatus( 0 );
                                }
                                hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                                hc.getHotelCi().insertData();
                            }
                            else
                            {
                                hc.getHotelCi().updateData( Integer.parseInt( paramId ), hc.getHotelCi().getSeq(), hc.getHotelCi().getSubSeq() );
                            }
                        }

                        if ( frm.getStatus() != ReserveCommon.RSV_STATUS_ZUMI )
                        {
                            // �����X�m�F
                            frm = htRsvSub.execRaiten( frm, frm.getStatus() );

                            // �G���[���莞
                            if ( frm.getErrMsg().trim().length() != 0 )
                            {
                                boolGuide = false;
                                Logging.error( "ActionHtRsvFix:" + frm.getErrMsg(), "ActionHtRsvFix" );

                                // 2015.03.21 ���X�������s�̃G���[�R�[�h���Z�b�g tashiro
                                errorCode = HapiTouchErrorMessage.ERR_30303;

                            }
                            else
                            {
                                // �\��}�C����t�^(�n�s�z�e�\��̂Ƃ�����)
                                if ( frm.getExtFlag() == 0 )
                                {
                                    errorCode = htRsvSub.setRsvPoint( hc );
                                }
                                boolGuide = true;
                            }
                        }
                    }

                }
                else
                {
                    // �f�[�^���擾���Ȃ���
                    dhrm.getData( Integer.parseInt( paramId ), roomNo );
                    errorCode = HapiTouchErrorMessage.ERR_30301;
                    boolExistRsv = false;
                }
            }

            if ( errorCode == 0 )
            {
                // IP�A�h���X���o�^����Ă���ꍇ�́A�z�X�g�A�������B�\��f�[�^�̍ŐV��Ԃ��z�X�g�ɓ`����
                if ( !HotelIp.getFrontIp( Integer.parseInt( paramId ), 1 ).equals( "" ) )
                {
                    RsvList rl = new RsvList();
                    if ( rl.getData( Integer.parseInt( paramId ), 0, 0, 0, frm.getRsvNo(), 0 ) != false )
                    {
                        rl.sendToHost( Integer.parseInt( paramId ) );
                    }
                }
            }

            /** �\�񗈓X�Ƌ��ʉ� **/
            TouchReception touchReception = new TouchReception();
            // ���X�����ł����ꍇ�̂�
            if ( boolGuide != false )
            {
                touchReception.getTouchReception( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ), userId, hc,
                        dhrm.getRoomNameHost(), boolHotelCustom, nonrefundableFlag );

                boolUseableMile = touchReception.isBoolUseableMile();
                unavailableMessage = touchReception.getUnavailableMessage();

                boolMemberInfo = touchReception.isBoolMemberInfo();
                customId = touchReception.getCustomId();

                customRank = touchReception.getCustomRank();
                point = touchReception.getPoint();
                point2 = touchReception.getPoint2();
                boolMemberCheckIn = touchReception.isBoolMemberCheckIn();
                errorCode = touchReception.getErrorCode();
                apHotelCustomerData = touchReception.getApHotelCustomerData();
                boolMemberAccept = touchReception.isBoolMemberAccept();
                apCouponUnitList = touchReception.getApCouponUnitList();

                boolMemberCardIssued = touchReception.isBoolMemberCardIssued();
                goodsCode = touchReception.getGoodsCode();
                goodsPrice = touchReception.getGoodsPrice();
                /** �\�񗈓X�Ƌ��ʉ� **/
            }
            if ( errorCode != 0 )
            {
                // �G���[���e��o�^
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( touchRoom );
                daeh.setRoomNo( roomNo );
                daeh.setId( Integer.parseInt( paramId ) );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                daeh.setUserId( userId );
                daeh.insertData();
            }

            apCommon = new DtoApCommon();
            apReserve = new DtoApReserve();
            apCouponList = new DtoApCouponList();
            apInquiry = new DtoApInquiryForm();
            apHotelCustomer = new DtoApHotelCustomer();
            apHotelCustomerData = new DtoApHotelCustomerData();
            apUserInfo = new DtoApUserInfo();

            // ���ʐݒ�
            apCommon.setHtCheckIn( ret );
            apCommon.setConnected( boolConnected );
            apCommon.setId( Integer.parseInt( paramId ) );
            apCommon.setHotelName( dhb.getName() );
            apCommon.setRoomNo( dhrm.getRoomNameHost() );
            apCommon.setSeq( hc.getHotelCi().getSeq() );
            apCommon.setUseableMile( boolUseableMile );
            apCommon.setUnavailableMessage( unavailableMessage );
            apCommon.setErrorCode( errorCode );
            apCommon.setCustomer( boolHotelCustom );

            // �\��
            apReserve.setApCommon( apCommon );
            apReserve.setExistsRsv( boolExistRsv );
            if ( boolConnected == false )
            {
                apReserve.setHostKind( 2 );
            }
            else
            {
                apReserve.setHostKind( 0 );
            }
            apReserve.setReserveNo( frm.getRsvNo() );
            apReserve.setRsvResult( boolGuide );

            // �z�e���ڋq
            apHotelCustomer.setResultMemberInfo( boolMemberInfo );
            apHotelCustomer.setResultMemberCard( boolMemberAccept );
            apHotelCustomer.setResultMemberCheckIn( boolMemberCheckIn );
            apHotelCustomer.setCustomId( customId );
            apHotelCustomer.setRank( customRank );
            apHotelCustomer.setPoint( point );
            apHotelCustomer.setPoint2( point2 );
            apHotelCustomer.setResultMemberCardIssued( boolMemberCardIssued );
            apHotelCustomer.setGoodsCode( goodsCode );
            apHotelCustomer.setGoodsPrice( goodsPrice );
            apHotelCustomer.setErrorCode( errorCode );

            // �N�[�|���ݒ�
            apCouponList.setApCommon( apCommon );
            apCouponList.setApCouponUnit( apCouponUnitList );

            apInquiry.setApCommon( apCommon );
            apHotelCustomer.setApCommon( apCommon );
            apHotelCustomerData.setApCommon( apCommon );
            apUserInfo.setApCommon( apCommon );

            request.setAttribute( "DtoApCommon", apCommon );
            request.setAttribute( "DtoApReserve", apReserve );

            request.setAttribute( "DtoApCouponList", apCouponList );
            request.setAttribute( "DtoApInquiryForm", apInquiry );
            request.setAttribute( "DtoApHotelCustomer", apHotelCustomer );
            request.setAttribute( "DtoApHotelCustomerData", apHotelCustomerData );
            request.setAttribute( "DtoApUserInfo", apUserInfo );

            requestDispatcher = request.getRequestDispatcher( "HappyHotelReserveRecept.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "HtRsvFix:" + exception );
        }
        finally
        {
        }
    }
}
