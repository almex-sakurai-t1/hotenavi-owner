package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataApTouchCoupon;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.dto.DtoApCommon;
import jp.happyhotel.dto.DtoApCouponList;
import jp.happyhotel.dto.DtoApCouponUnit;
import jp.happyhotel.dto.DtoApHotelCustomer;
import jp.happyhotel.dto.DtoApHotelCustomerData;
import jp.happyhotel.dto.DtoApInquiryForm;
import jp.happyhotel.dto.DtoApNonAuto;
import jp.happyhotel.dto.DtoApUserInfo;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.touch.TouchReception;

/**
 * �n�s�z�e�A�v�������ԍ��I��҂�
 * 
 * @author T.Sakurai
 * @version 1.0 2016/11/07
 * 
 */

public class ActionHtNonAuto extends BaseAction
{
    final String                       URL_MATCHING = "happyhotel.jp";
    final String                       SP_URL       = "/phone/htap/";
    final String                       FILE_URL     = "NonAuto.jsp";

    private RequestDispatcher          requestDispatcher;
    private DtoApCommon                apCommon;
    private DtoApNonAuto               apNonAuto;
    private DtoApHotelCustomer         apHotelCustomer;
    private DtoApHotelCustomerData     apHotelCustomerData;
    private DtoApUserInfo              apUserInfo;
    private ArrayList<DtoApCouponUnit> apCouponUnitList;
    private DtoApCouponList            apCouponList;
    private DtoApInquiryForm           apInquiry;

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
        boolean boolConnected = true; // �z�X�g�A��
        boolean boolHotelCustom = false;// �ڋq�Ή���
        boolean boolMemberCheckIn = false; // �����o�[�`�F�b�N�C������
        boolean boolMemberAccept = false;// �����o�[����t����
        boolean boolMemberInfo = false;// �����o�[���擾����
        boolean boolUseableMile = false;// �}�C���g�p�̓^�b�`�[���ł͂����Ȃ�
        boolean boolCouponAvailable = false;// �N�[�|�����p�\�t���O
        int nonrefundableFlag = 0; // 0:�ԋ�OK�A1:�ԋ��s�A2:�N���W�b�g�O��̂ݕԋ��s��
        boolean boolMemberCardIssued = false; // �����o�[�J�[�h���s�L��

        String customId = "";
        String unavailableMessage = "";
        int errorCode = 0;
        int point = 0;
        int point2 = 0;
        String customRank = "";
        String forwardUrl = "";
        String paramId;
        String paramUserId;
        int goodsCode = 0; // ���i�R�[�h
        int goodsPrice = 0; // ���z

        DataHotelBasic dhb = new DataHotelBasic();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();
        DataApHotelSetting dahs = new DataApHotelSetting();
        HotelCi hc = new HotelCi();
        DataApTouchCoupon datc = new DataApTouchCoupon();

        try
        {
            paramId = request.getParameter( "id" );
            paramUserId = request.getParameter( "user_id" );
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( paramUserId == null || paramUserId.equals( "" ) != false )
            {
                paramUserId = "";
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
                ret = dahs.getData( Integer.parseInt( paramId ) );
            }

            if ( ret != false )
            {

                // �ڋq���s���Ă��邩�ǂ���
                if ( dahs.getCustomFlag() > 0 )
                {
                    boolHotelCustom = true;
                }
                //
                nonrefundableFlag = dahs.getNonrefundableFlag();

            }
            // �����[�U�[�̓��z�e���ł�24���Ԉȓ��̃`�F�b�N�C���𒲂ׂ�
            ret = hc.getCheckInBeforeData( Integer.parseInt( paramId ), paramUserId );

            if ( ret != false && !hc.getHotelCi().getRoomNo().equals( "" ) )
            {

                // 24���Ԉȓ��̃`�F�b�N�C���f�[�^������΁AhtHome �������� HtRsvFix �ɑJ�ڂ��܂��B
                dhrm.getData( Integer.parseInt( paramId ), hc.getHotelCi().getRoomNo() );

                // �N�[�|���f�[�^����������N�[�|���g�p�ł��Ȃ����߁A�߂�l���t�ɂ��Ď擾
                boolCouponAvailable = !(datc.getData( Integer.parseInt( paramId ), hc.getHotelCi().getSeq() ));

                TouchReception touchReception = new TouchReception();

                // �^�b�`�ڑ�
                touchReception.getTouchReception( Integer.parseInt( paramId ), hc.getHotelCi().getSeq(), paramUserId, hc, dhrm.getRoomNameHost(), boolHotelCustom, nonrefundableFlag );

                boolUseableMile = touchReception.isBoolUseableMile();
                unavailableMessage = touchReception.getUnavailableMessage();

                boolMemberInfo = touchReception.isBoolMemberInfo();
                customId = touchReception.getCustomId();
                customRank = touchReception.getCustomRank();
                point = touchReception.getPoint();
                point2 = touchReception.getPoint2();
                boolMemberCheckIn = touchReception.isBoolMemberCheckIn();
                apHotelCustomerData = touchReception.getApHotelCustomerData();
                boolMemberAccept = touchReception.isBoolMemberAccept();
                apCouponUnitList = touchReception.getApCouponUnitList();
                boolMemberCardIssued = touchReception.isBoolMemberCardIssued();
                if ( errorCode == 0 && touchReception.getErrorCode() > 0 )
                {
                    errorCode = touchReception.getErrorCode();
                }
                goodsCode = touchReception.getGoodsCode();
                goodsPrice = touchReception.getGoodsPrice();
                Logging.info( "[ActionHtNonAuto.execute()]hc.getHotelCi().getSeq():" + hc.getHotelCi().getSeq()
                        + ",paramUserId :" + paramUserId
                        + ",hc.getHotelCi().getUserId() :" + hc.getHotelCi().getUserId()
                        + ",paramUserId :" + paramUserId
                        + ",dhrm.getRoomNameHost() :" + dhrm.getRoomNameHost()
                        + ",boolHotelCustom :" + boolHotelCustom
                        + ",nonrefundableFlag:" + nonrefundableFlag
                        + ", boolMemberInfo :" + boolMemberInfo
                        + ", customId :" + customId
                        );

            }
            apCommon = new DtoApCommon();
            apNonAuto = new DtoApNonAuto();
            apCouponList = new DtoApCouponList();
            apHotelCustomer = new DtoApHotelCustomer();
            apHotelCustomerData = new DtoApHotelCustomerData();
            apInquiry = new DtoApInquiryForm();
            apUserInfo = new DtoApUserInfo();

            // ���ʐݒ�
            apCommon.setHtCheckIn( ret );
            apCommon.setConnected( boolConnected );// �z�X�g�ڑ��Ftrue
            apCommon.setId( Integer.parseInt( paramId ) );
            apCommon.setHotelName( dhb.getName() );
            apCommon.setRoomNo( dhrm.getRoomNameHost() );
            apCommon.setSeq( hc.getHotelCi().getSeq() );
            apCommon.setUseableMile( boolUseableMile );// �}�C���g�p��false
            apCommon.setUnavailableMessage( unavailableMessage );// ���b�Z�[�W�͂Ȃ�
            apCommon.setErrorCode( errorCode );
            apCommon.setCustomer( boolHotelCustom );

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

            // �N�[�|���ݒ�
            apCouponList.setApCommon( apCommon );
            apCouponList.setApCouponUnit( apCouponUnitList );

            apNonAuto.setApCommon( apCommon );
            apInquiry.setApCommon( apCommon );
            apHotelCustomer.setApCommon( apCommon );
            apHotelCustomerData.setApCommon( apCommon );
            apUserInfo.setApCommon( apCommon );

            request.setAttribute( "DtoApCommon", apCommon );
            request.setAttribute( "DtoApNonAuto", apNonAuto );
            request.setAttribute( "DtoApCouponList", apCouponList );
            request.setAttribute( "DtoApInquiryForm", apInquiry );
            request.setAttribute( "DtoApHotelCustomer", apHotelCustomer );
            request.setAttribute( "DtoApHotelCustomerData", apHotelCustomerData );
            request.setAttribute( "DtoApUserInfo", apUserInfo );
            request.setAttribute( "AVAILABLE_COUPON", Boolean.toString( boolCouponAvailable ) );

            Logging.info( "HtNonAuto forwardUrl" + forwardUrl );

            requestDispatcher = request.getRequestDispatcher( forwardUrl );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "HtNonAuto:" + exception );
        }
        finally
        {
        }
    }
}