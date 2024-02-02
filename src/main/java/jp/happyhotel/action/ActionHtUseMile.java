package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataApTouchUserPoint;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.dto.DtoApCommon;
import jp.happyhotel.dto.DtoApUsePoint;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.others.HapiTouchErrorMessage;
import jp.happyhotel.touch.TouchCi;
import jp.happyhotel.touch.UseMile;
import jp.happyhotel.user.UserPointPay;
import jp.happyhotel.user.UserPointPayTemp;

/**
 * �n�s�z�e�A�v���`�F�b�N�C���N���X
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtUseMile extends BaseAction
{
    final int                 TIMEOUT          = 10000;
    final int                 HOTENAVI_PORT_NO = 7023;
    final int                 HAPIHOTE_PORT_NO = 7046;
    final int                 TEMP_USE         = 1;                      // ���g�p

    final String              HTTP             = "http://";
    final String              CLASS_NAME       = "hapiTouch.act?method=";
    final int                 RESULT_OK        = 1;
    final int                 RESULT_NG        = 2;
    final int                 USE_CODE         = 1000006;
    final int                 USE_KIND         = 24;
    private RequestDispatcher requestDispatcher;
    private DtoApCommon       apCommon;
    private DtoApUsePoint     apUsePoint;

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
        boolean boolAvailable = false;
        boolean boolMemberOperation = false;
        boolean boolConnected = false;
        String paramSeq = "";
        String roomNo = "";
        String paramPoint = "";
        String paramId = "";
        String userId = "";
        String frontIp = "";
        String hotenaviId = "";
        String hotelName = "";
        String paramAllUse = "";

        int usePoint = 0;
        int nowPoint = 0;
        int paySeq = 0;
        int payTempSeq = 0;
        int errorCode = 0;
        int subSeq = 0;
        TouchCi tc = new TouchCi();
        HotelCi hc = new HotelCi();
        DataHotelBasic dhb = new DataHotelBasic();
        DataApTouchUserPoint dtup = new DataApTouchUserPoint();
        UserPointPay upp = new UserPointPay();
        UserPointPayTemp uppt = new UserPointPayTemp();
        UseMile um = new UseMile();
        // �o�b�N�I�t�B�X�o�^������g�ݍ���

        try
        {

            paramId = request.getParameter( "id" );
            paramSeq = request.getParameter( "seq" );
            paramPoint = request.getParameter( "point" );
            paramAllUse = request.getParameter( "allUse" );

            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( paramSeq == null || paramSeq.equals( "" ) != false || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( paramPoint == null || paramPoint.equals( "" ) != false || CheckString.numCheck( paramPoint ) == false )
            {
                paramPoint = "0";
            }
            if ( paramAllUse == null || paramAllUse.equals( "" ) != false || CheckString.numCheck( paramAllUse ) == false )
            {
                paramAllUse = "0";
            }

            dhb.getData( Integer.parseInt( paramId ) );
            hotenaviId = dhb.getHotenaviId();
            hotelName = dhb.getName();

            ret = hc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
            if ( ret != false )
            {
                userId = hc.getHotelCi().getUserId();
                roomNo = hc.getHotelCi().getRoomNo();
                subSeq = hc.getHotelCi().getSubSeq();
            }

            // �g�p�}�C��
            usePoint = Integer.parseInt( paramPoint );
            // �ۗL�}�C��
            nowPoint = upp.getNowPoint( userId, false );
            // �g�p�}�C�����ۗL�}�C���ȉ��ł��A�^�b�`���L���̏ꍇ�̂݁A�g�p�\
            if ( usePoint <= nowPoint && hc.getHotelCi().getCiStatus() == 0 )
            {
                boolAvailable = true;
            }

            // �z�e���̃t�����gIP���擾
            frontIp = HotelIp.getFrontIpForUseMile( Integer.parseInt( paramId ) );
            Logging.debug( "[ActionHtUseMile] usePoint= " + usePoint + ",nowPoint=" + nowPoint + ",boolAvailable=" + boolAvailable + ",frontIp=" + frontIp );

            // �}�C���g�p���f��OK��������}�C���g�p
            if ( boolAvailable != false )
            {

                // �z�X�g�փ}�C���g�p�ʒm
                if ( frontIp.equals( "" ) == false )
                {
                    boolConnected = true;
                    /** �i1002�j�n�s�z�e_�n�s�z�e�^�b�`�}�C���g�p�d�� **/
                    um.setSeq( hc.getHotelCi().getSeq() );
                    um.setMile( Integer.parseInt( paramPoint ) );
                    um.setNowPoint( nowPoint - usePoint );
                    um.sendToHost( frontIp, TIMEOUT, HAPIHOTE_PORT_NO, paramId );

                    // ���ʂ�OK�ł����ap_touch_user_point�֏�������
                    if ( um.getResult() == RESULT_OK )
                    {
                        ret = true;
                    }
                    else
                    {
                        ret = false;
                        errorCode = um.getErrorCode();
                        if ( errorCode == 0 )
                            errorCode = HapiTouchErrorMessage.ERR_30202;
                    }
                }
                // �z�X�g��A�������̓}�C�������g�p
                else
                {
                    ret = true;
                    boolMemberOperation = true;
                }
            }
            else
            {
                ret = false;
                errorCode = HapiTouchErrorMessage.ERR_30201;
                // �^�b�`���L���ł͂Ȃ��ꍇ�A�`�F�b�N�C���f�[�^�X�e�[�^�X�ُ�̃��b�Z�[�W��ݒ�B
                if ( hc.getHotelCi().getCiStatus() != 0 )
                {
                    errorCode = HapiTouchErrorMessage.ERR_30203;
                }
            }

            // �������ݔ��f��OK��������
            if ( ret != false )
            {
                if ( tc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) ) != false )
                {

                    // ���O�Ƀ^�b�`�f�[�^���X�V
                    tc.getTouchCi().setUsePoint( usePoint );
                    // �}�C���g�p���t�E�������Z�b�g
                    tc.getTouchCi().setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    tc.getTouchCi().setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    // �ŏI�X�V���E�������Z�b�g
                    tc.getTouchCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    tc.getTouchCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    tc.getTouchCi().setUseHotenaviId( hotenaviId );
                    // ���}�C���g�p�t���O
                    if ( boolMemberOperation != false )
                    {
                        tc.getTouchCi().setUseTempFlag( TEMP_USE );
                    }
                    // �S�}�C���g�p���N�G�X�g�������
                    if ( Integer.parseInt( paramAllUse ) == 1 )
                    {
                        tc.getTouchCi().setAlluseFlag( 1 );
                        tc.getTouchCi().setAllusePoint( usePoint );
                    }

                    ret = tc.getTouchCi().updateData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                    // �}�C���g�p�X�V����
                    if ( ret != false )
                    {
                        // hh_hotel_ci�ɔ��f
                        tc.registHotelCi( tc.getTouchCi() );
                        // hh_hotel_ci �ɔ��f�����̂ōēx�ǂݍ���
                        hc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                    }
                }
                else
                {
                    // ���O�Ƀ^�b�`�f�[�^���X�V
                    hc.getHotelCi().setUsePoint( usePoint );
                    // �}�C���g�p���t�E�������Z�b�g
                    hc.getHotelCi().setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    hc.getHotelCi().setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    // �ŏI�X�V���E�������Z�b�g
                    hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    hc.getHotelCi().setUseHotenaviId( hotenaviId );
                    // ���}�C���g�p�t���O
                    if ( boolMemberOperation != false )
                    {
                        hc.getHotelCi().setUseTempFlag( TEMP_USE );
                    }
                    // �S�}�C���g�p���N�G�X�g�������
                    if ( Integer.parseInt( paramAllUse ) == 1 )
                    {
                        hc.getHotelCi().setAllUseFlag( 1 );
                        hc.getHotelCi().setAllUsePoint( usePoint );
                    }
                    hc.getHotelCi().updateData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ), subSeq );
                }
                dtup.setUserId( userId );
                dtup.setId( Integer.parseInt( paramId ) );
                dtup.setCiSeq( Integer.parseInt( paramSeq ) );
                dtup.setCode( USE_CODE );
                dtup.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dtup.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

                // �^�b�`�f�[�^�}�C���}������
                ret = dtup.insertData();

                // �}������
                if ( ret != false )
                {
                    // upp��uppt�Ƀ}�C�����C���T�[�g
                    // �f�[�^���Ȃ����߁Ahh_user_point_pay_temp�Ɏg�p�}�C����ǉ�
                    payTempSeq = uppt.insertMile( hc.getHotelCi(), USE_CODE, nowPoint );
                    // hh_user_point_pay�Ɏg�p�}�C���̒ǉ�
                    paySeq = upp.insertMile( hc.getHotelCi(), USE_CODE, nowPoint );

                    if ( payTempSeq > 0 && paySeq > 0 )
                    {
                        dtup.setPaySeq( paySeq );
                        dtup.setTempSeq( payTempSeq );
                        dtup.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dtup.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        ret = dtup.updateData( userId, Integer.parseInt( paramId ), Integer.parseInt( paramSeq ), USE_CODE );
                    }
                    else
                    {
                        ret = false;
                    }
                }
                else
                {
                    // �f�[�^�d���̂��߃C���T�[�g���s���Ă���\��������̂Ńf�[�^�擾
                    dtup.getData( userId, Integer.parseInt( paramId ), Integer.parseInt( paramSeq ), USE_CODE );

                    // upp��uppt�Ɏg�p�}�C�����A�b�v�f�[�g
                    payTempSeq = uppt.updateMile( hc.getHotelCi(), USE_CODE, nowPoint, dtup.getTempSeq() );
                    paySeq = upp.updateMile( hc.getHotelCi(), USE_CODE, nowPoint, dtup.getPaySeq() );

                    dtup.setPaySeq( paySeq );
                    dtup.setTempSeq( payTempSeq );
                    dtup.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dtup.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    ret = dtup.updateData( userId, Integer.parseInt( paramId ), Integer.parseInt( paramSeq ), USE_CODE );

                    if ( payTempSeq > 0 && paySeq > 0 )
                    {
                        ret = true;
                    }
                }
            }
            if ( errorCode != 0 )
            {
                // �G���[���e��o�^
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( roomNo );
                daeh.setId( Integer.parseInt( paramId ) );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                daeh.setUserId( userId );
                daeh.insertData();
            }

            apCommon = new DtoApCommon();
            apUsePoint = new DtoApUsePoint();

            // ���ʐݒ�
            apCommon.setHotelName( hotelName );
            apCommon.setRoomNo( roomNo );
            apCommon.setSeq( Integer.parseInt( paramSeq ) );
            apCommon.setId( Integer.parseInt( paramId ) );
            apCommon.setConnected( boolConnected );
            apCommon.setErrorCode( errorCode );

            apUsePoint.setApCommon( apCommon );
            apUsePoint.setResult( ret );
            apUsePoint.setUserPoint( usePoint );
            apUsePoint.setErrorCode( errorCode );

            request.setAttribute( "DtoApUsePoint", apUsePoint );

            requestDispatcher = request.getRequestDispatcher( "MileUseResult.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionHtUseMile] Exception:" + exception.toString() );
        }
        finally
        {
        }
    }
}