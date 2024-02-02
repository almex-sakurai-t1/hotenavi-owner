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
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataApTouchCoupon;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.dto.DtoApCommon;
import jp.happyhotel.dto.DtoApCouponList;
import jp.happyhotel.dto.DtoApCouponUnit;
import jp.happyhotel.dto.DtoApHotelCustomer;
import jp.happyhotel.dto.DtoApHotelCustomerData;
import jp.happyhotel.dto.DtoApInquiryForm;
import jp.happyhotel.dto.DtoApUserInfo;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.others.HapiTouchErrorMessage;
import jp.happyhotel.touch.CheckIn;
import jp.happyhotel.touch.FrontTouchAcceptCheck;
import jp.happyhotel.touch.HotelTerminal;
import jp.happyhotel.touch.TouchCi;
import jp.happyhotel.touch.TouchReception;
import jp.happyhotel.user.UserPointPay;

/**
 * �n�s�z�e�A�v���`�F�b�N�C���N���X
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtHome extends BaseAction
{
    final String                       URL_MATCHING          = "happyhotel.jp";
    final String                       HAPITOUCH             = "hapiTouch.act?method=HtCheckIn";
    final String                       SP_URL                = "/phone/htap/";
    final String                       FILE_URL              = "common.jsp";
    final int                          COUPON_KIND_AUTO      = 0;
    final int                          COUPON_KIND_MANUAL    = 1;
    final int                          COUPON_NO_USE         = 0;
    final int                          COUPON_USED1          = 1;
    final int                          COUPON_USED10         = 10;
    final int                          COUPON_USED100        = 100;
    final int                          TIMEOUT               = 5000;
    final int                          HOTENAVI_PORT_NO      = 7023;
    final int                          HAPIHOTE_PORT_NO      = 7046;
    final int                          RESULT_OK             = 1;
    final int                          RESULT_NG             = 2;
    final int                          DEPOSIT_NO            = 1;
    final int                          DEPOSIT_CASH          = 2;
    final int                          DEPOSIT_CREDIT        = 3;
    final int                          NONREFUNDABLE         = 1;                               // �ԋ��s��
    final int                          CREDIT_NONREFUNDABLE  = 2;                               // �N���W�b�g���Z�̕ԋ��s��
    final int                          CI_STATUS_NOT_DISPLAY = 3;                               // �^�b�`PC�ւ̔�\��

    private RequestDispatcher          requestDispatcher;
    private DtoApCommon                apCommon;
    private DtoApCouponList            apCouponList;
    private DtoApInquiryForm           apInquiry;
    private DtoApHotelCustomer         apHotelCustomer;
    private DtoApHotelCustomerData     apHotelCustomerData;
    private DtoApUserInfo              apUserInfo;
    private ArrayList<DtoApCouponUnit> apCouponUnitList;

    /**
     * �n�s�z�e�^�b�`
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int carrierFlag;
        boolean ret = true;
        boolean boolConnected = false; // �z�X�g�A��
        boolean boolHotelCustom = false;// �ڋq�Ή���
        boolean boolMemberCheckIn = false; // �����o�[�`�F�b�N�C������
        boolean boolMemberAccept = false;// �����o�[����t����
        boolean boolMemberInfo = false;// �����o�[���擾����
        boolean boolUseableMile = true;// �}�C���g�p�\�t���O
        boolean boolCouponAvailable = false;// �N�[�|�����p�\�t���O
        int nonrefundableFlag = 0; // 0:�ԋ�OK�A1:�ԋ��s�A2:�N���W�b�g�O��̂ݕԋ��s��
        boolean boolMemberCardIssued = false; // �����o�[�J�[�h���s�L��
        boolean reTouch = false;

        String hapihoteIp = "";
        String customId = "";
        String unavailableMessage = "";
        int errorCode = 0;
        int point = 0;
        int point2 = 0;
        String customRank = "";
        String forwardUrl = "";
        String paramId;
        String paramSeq;
        String userId = "";
        int goodsCode = 0; // ���i�R�[�h
        int goodsPrice = 0; // ���z
        int nowPoint = 0;

        DataUserBasic dub = new DataUserBasic();
        DataHotelBasic dhb = new DataHotelBasic();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();
        DataApHotelSetting dahs = new DataApHotelSetting();
        HotelTerminal ht = new HotelTerminal();
        TouchCi tc = new TouchCi();
        HotelCi hc = new HotelCi();
        UserPointPay upp = new UserPointPay();
        DataApTouchCoupon datc = new DataApTouchCoupon();

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

            carrierFlag = UserAgent.getUserAgentTypeFromTouch( request );
            // URL�𔻒f
            if ( carrierFlag == DataMasterUseragent.CARRIER_SMARTPHONE )
            {
                forwardUrl = SP_URL + FILE_URL;
            }
            else if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
            {
                forwardUrl = "/i/htap/" + FILE_URL;
            }
            else if ( carrierFlag == DataMasterUseragent.CARRIER_AU )
            {
                forwardUrl = "/au/htap/" + FILE_URL;
            }
            else if ( carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK )
            {
                forwardUrl = "/y/htap/" + FILE_URL;
            }
            else
            {
                if ( UserAgent.getUserAgentTypeString( request ).equals( "ipa" ) ||
                        UserAgent.getUserAgentTypeString( request ).equals( "ada" ) )
                {
                    forwardUrl = SP_URL + FILE_URL;
                }
            }
            if ( ret != false )
            {
                // �z�e�������擾
                ret = dhb.getData( Integer.parseInt( paramId ) );
            }
            if ( ret != false )
            {
                // �z�e���ݒ���擾 // �z�e���ݒ���擾
                dahs.getData( Integer.parseInt( paramId ) );
                // �z�e���̃t�����gIP���擾
                hapihoteIp = HotelIp.getFrontIp( Integer.parseInt( paramId ) );
            }

            if ( Integer.parseInt( paramId ) > 0 && Integer.parseInt( paramSeq ) > 0 && ret != false )
            {
                ret = hc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                if ( ret != false )
                {
                    // �`�F�b�N�C���f�[�^���������Ȃ��ꍇ
                    if ( hc.getHotelCi().getCiStatus() == 1 )
                    {
                        errorCode = HapiTouchErrorMessage.ERR_30108;
                        ret = false;
                    }
                    else if ( hc.getHotelCi().getCiStatus() == 2 )
                    {
                        errorCode = HapiTouchErrorMessage.ERR_30112;
                        ret = false;
                    }
                    else if ( hc.getHotelCi().getCiStatus() == 3 )
                    {
                        errorCode = HapiTouchErrorMessage.ERR_30113;
                        ret = false;
                    }
                }
                // �������擾
                dhrm.getData( Integer.parseInt( paramId ), hc.getHotelCi().getRoomNo() );
                Logging.info( "[ActionHtHome] id = " + paramId + ",seq=" + paramSeq + ",RoomNo=" + hc.getHotelCi().getRoomNo() + ",getRoomNameHost=" + dhrm.getRoomNameHost() );

                // �z�e���[���f�[�^�擾
                ht.getHotelTerminal( Integer.parseInt( paramId ), dhrm.getSeq() );

                // �N�[�|���f�[�^����������N�[�|���g�p�ł��Ȃ����߁A�߂�l���t�ɂ��Ď擾
                boolCouponAvailable = !(datc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) ));

                userId = hc.getHotelCi().getUserId();
                dub.getData( userId );

                // �ڋq���s���Ă��邩�ǂ���
                if ( dahs.getCustomFlag() > 0 )
                {
                    boolHotelCustom = true;
                }
                //
                nonrefundableFlag = dahs.getNonrefundableFlag();

                if ( ret != false )
                {
                    // �z�X�g�֑΂��ă`�F�b�N�C���f�[�^�𑗐M
                    if ( hapihoteIp.equals( "" ) == false )
                    {

                        if ( dhrm.getRoomNameHost().equals( "" ) == false )
                        {

                            /** �i1000�j�n�s�z�e_�n�s�z�e�^�b�`�`�F�b�N�C���d�� **/
                            CheckIn ci = new CheckIn();
                            ci.setSeq( hc.getHotelCi().getSeq() );
                            ci.setRoomName( dhrm.getRoomNameHost() );
                            // �ۗL�}�C��
                            nowPoint = upp.getNowPoint( userId, false );
                            ci.setNowPoint( nowPoint );

                            if ( ci.sendToHost( Integer.parseInt( paramId ) ) != false )
                            {
                            }
                            else
                            {
                                errorCode = ci.getErrorCode();
                                ret = false;
                            }

                            if ( tc.getData( Integer.parseInt( paramId ), hc.getHotelCi().getSeq() ) != false )
                            {

                                if ( ci.getResult() == RESULT_OK )
                                {
                                    tc.getTouchCi().setHostNotification( RESULT_OK );
                                    // �z�X�g�A���t���O
                                    boolConnected = true;
                                }
                                else
                                {
                                    tc.getTouchCi().setHostNotification( RESULT_NG );
                                    errorCode = ci.getErrorCode();
                                    ret = false;
                                    // if ( ci.getErrorCode() == 8023 && reTouch )// ��v���ōă^�b�`�̏ꍇ�ɂ͖����ɂ��Ȃ�
                                    if ( reTouch )// �ă^�b�`�̏ꍇ�ɂ͖����ɂ��Ȃ�
                                    {
                                    }
                                    else
                                    {
                                        tc.getTouchCi().setCiStatus( CI_STATUS_NOT_DISPLAY );
                                    }
                                }
                                tc.getTouchCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                tc.getTouchCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                tc.getTouchCi().updateData( tc.getTouchCi().getId(), tc.getTouchCi().getSeq() );
                                tc.registHotelCi( tc.getTouchCi() );
                            }
                            else
                            {
                                if ( !reTouch )// �ă^�b�`�̏ꍇ�ɂ͖����ɂ��Ȃ�
                                {
                                    hc.getHotelCi().setCiStatus( CI_STATUS_NOT_DISPLAY );
                                }
                                hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                hc.getHotelCi().updateData( Integer.parseInt( paramId ), hc.getHotelCi().getSeq(), hc.getHotelCi().getSubSeq() );
                            }

                        }
                        else
                        {

                            // �z�X�g�A���t���O
                            boolConnected = true;
                            // 2015.03.22 �^�b�`����t���̂ŁA�t�����g�̃^�b�`��t�󋵂���艺���� tashiro
                            FrontTouchAcceptCheck ftac = new FrontTouchAcceptCheck();
                            ftac.updateFrontTouchDisable( Integer.parseInt( paramId ) );
                        }
                    }
                    else
                    {
                        // �z�e���̐ڑ���IP���Ȃ��̂Ńz�e���ڋq�͔�Ή������ɂ���
                        boolHotelCustom = false;
                        boolConnected = false;
                        hapihoteIp = "";
                    }
                }
            }
            // �`�F�b�N�C������
            if ( ret != false )
            {
                // �����o�[ID�����Ȃ��ꍇ��NG�Ƃ��Ă���
                if ( hc.getHotelCi().getUserId().equals( "" ) != false )
                {
                    ret = false;
                    errorCode = HapiTouchErrorMessage.ERR_30102;
                }
            }
            else
            {
                if ( errorCode == 0 )
                {
                    errorCode = HapiTouchErrorMessage.ERR_30117;

                }
            }
            /** �\�񗈓X�Ƌ��ʉ� **/
            TouchReception touchReception = new TouchReception();
            if ( ret != false )
            {
                // �^�b�`�ڑ�
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

                // �G���[�R�[�h�����ɃZ�b�g����Ă���ꍇ�A�߂�l�ɃG���[�R�[�h���Z�b�g����Ă���ꍇ�̂݃Z�b�g����
                if ( errorCode == 0 && touchReception.getErrorCode() > 0 )
                {
                    errorCode = touchReception.getErrorCode();
                }
                apHotelCustomerData = touchReception.getApHotelCustomerData();
                boolMemberAccept = touchReception.isBoolMemberAccept();
                apCouponUnitList = touchReception.getApCouponUnitList();
                boolMemberCardIssued = touchReception.isBoolMemberCardIssued();
                goodsCode = touchReception.getGoodsCode();
                goodsPrice = touchReception.getGoodsPrice();

                /** �\�񗈓X�Ƌ��ʉ� **/
            }
            if ( errorCode != 0 && !UserAgent.isBot( request ) )
            {
                // �G���[���e��o�^
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( dhb.getName() );
                daeh.setRoomName( dhrm.getRoomNameHost() );
                daeh.setId( Integer.parseInt( paramId ) );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( hc.getHotelCi().getSeq() );
                daeh.setUserId( userId );
                daeh.insertData();
            }

            apCommon = new DtoApCommon();
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

            // �N�[�|���ݒ�
            apCouponList.setApCommon( apCommon );
            apCouponList.setApCouponUnit( apCouponUnitList );

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

            apHotelCustomer.setApCommon( apCommon );

            apUserInfo.setApCommon( apCommon );

            request.setAttribute( "DtoApCommon", apCommon );
            request.setAttribute( "DtoApCouponList", apCouponList );
            request.setAttribute( "DtoApInquiryForm", apInquiry );
            request.setAttribute( "DtoApHotelCustomer", apHotelCustomer );
            request.setAttribute( "DtoApHotelCustomerData", apHotelCustomerData );
            request.setAttribute( "DtoApUserInfo", apUserInfo );
            request.setAttribute( "AVAILABLE_COUPON", Boolean.toString( boolCouponAvailable ) );

            requestDispatcher = request.getRequestDispatcher( forwardUrl );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "HtHome:" + exception );
        }
        finally
        {
        }
    }
}