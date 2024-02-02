package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApAutoCoupon;
import jp.happyhotel.data.DataApTouchCoupon;
import jp.happyhotel.data.DataApUserAutoCoupon;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.dto.DtoApCommon;
import jp.happyhotel.dto.DtoApCoupon;
import jp.happyhotel.dto.DtoApCouponInformation;
import jp.happyhotel.touch.DiscountCoupon;
import jp.happyhotel.touch.TouchCi;
import jp.happyhotel.touch.TouchUserCoupon;

public class ActionHtDiscountCoupon extends BaseAction
{
    final int                      USED               = 1;
    final int                      COUPON_KIND_AUTO   = 0;
    final int                      COUPON_KIND_MANUAL = 1;
    final int                      COUPON_NO_USE      = 0;
    final int                      COUPON_USED1       = 1;
    final int                      COUPON_USED10      = 10;
    final int                      COUPON_USED100     = 100;
    final int                      TIMEOUT            = 5000;
    final int                      HOTENAVI_PORT_NO   = 7023;
    final int                      HAPIHOTE_PORT_NO   = 7046;
    final String                   HTTP               = "http://";
    final String                   CLASS_NAME         = "hapiTouch.act?method=";
    final String                   METHOD_DISCOUNT    = "HtDiscountCoupon";
    final int                      RESULT_OK          = 1;
    final int                      RESULT_NG          = 2;
    final int                      DISCOUNT_COUPON    = 1;                                // �����̋敪�̂�
    private RequestDispatcher      requestDispatcher;
    private DtoApCommon            apCommon;
    private DtoApCoupon            apCoupon;
    private DtoApCouponInformation apCouponInfo;
    private final String           DISP_MESSAGE       = "�����ɂăt�����g�܂ł��A�����������N�[�|���ԍ������`���������B";

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
        boolean boolCoupon = false;
        boolean boolResultCoupon = false;
        String roomNo = "";
        String paramId = "";
        String paramSeq = "";
        String paramCouponNo = "";
        String paramCouponSeq = "";
        String userId = "";
        String connectUrl = "";
        String hapihoteIp = "";
        String printCouponNo = "";
        TouchCi tc = new TouchCi();
        TouchUserCoupon tuc = new TouchUserCoupon();
        DataHotelBasic dhb = new DataHotelBasic();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();
        int errorCode = 0;
        String errorMessage = "";
        String unavailableKind = "";
        String unavailableMessage = "";
        boolean boolUseable = false;
        DiscountCoupon dc = new DiscountCoupon();

        try
        {
            paramId = request.getParameter( "id" );
            paramSeq = request.getParameter( "seq" );
            paramCouponNo = request.getParameter( "couponNo" );
            paramCouponSeq = request.getParameter( "couponSeq" );

            if ( paramSeq == null || paramSeq.equals( "" ) != false || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( paramCouponNo == null || paramCouponNo.equals( "" ) != false || CheckString.numCheck( paramCouponNo ) == false )
            {
                paramCouponNo = "0";
            }
            if ( paramCouponSeq == null || paramCouponSeq.equals( "" ) != false || CheckString.numCheck( paramCouponSeq ) == false )
            {
                paramCouponSeq = "0";
            }

            dhb.getData( Integer.parseInt( paramId ) );

            // �z�e���̃t�����gIP���擾
            hapihoteIp = HotelIp.getFrontIp( Integer.parseInt( paramId ) );

            if ( Integer.parseInt( paramSeq ) > 0 && Integer.parseInt( paramId ) > 0 && paramCouponNo.equals( "" ) == false )
            {
                ret = tc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                if ( tc.getTouchCi().getId() > 0 && tc.getTouchCi().getSeq() > 0 )
                {
                    userId = tc.getTouchCi().getUserId();
                    roomNo = tc.getTouchCi().getRoomNo();
                }
                if ( ret != false )
                {
                    dhrm.getData( Integer.parseInt( paramId ), roomNo );
                    if ( tc.getTouchCi().getCiStatus() == 0 )
                    {
                        boolCoupon = tuc.getCouponData( userId, Integer.parseInt( paramId ), Integer.parseInt( paramCouponNo ), Integer.parseInt( paramCouponSeq ) );
                        if ( boolCoupon != false )
                        {
                            // �N�[�|���ԍ��̔���
                            printCouponNo = getPrintCouponNp( tuc.getUserAutoCoupon(), tuc.getCoupon() );
                            ret = tuc.getCouponDetailData( Integer.parseInt( paramId ), Integer.parseInt( paramCouponNo ) );
                            if ( ret != false )
                            {

                                // ���N�G�X�g
                                /** �i1008�j�n�s�z�e_�����K�p�N�[�|�������v�� **/
                                dc.setSeq( tc.getTouchCi().getSeq() );
                                dc.setRoomName( dhrm.getRoomNameHost() );
                                dc.setCouponNo( printCouponNo );

                                dc.setDiscountKind( tuc.getCoupon()[0].getMethod() + 1 );// DB�l���������@���0:������,1:���z�����A�z�X�g�֒ʒm����l��1:������,2:���z�����̂��߁A+1����
                                dc.setCombinedUse( tuc.getCoupon()[0].getCustomFlag() );
                                dc.setDiscountPart( tuc.getCoupon()[0].getDiscountPoint() );
                                dc.setDiscountDataLength( tuc.getCouponDetail().length );
                                dc.setPlanCode( tuc.getPlanNoList() );
                                dc.setModeCode( tuc.getChargeModeList() );
                                dc.setValue( tuc.getDiscountList() );
                                dc.sendToHost( hapihoteIp, TIMEOUT, HAPIHOTE_PORT_NO, paramId );

                                if ( dc.getResult() == RESULT_OK )
                                {
                                    boolResultCoupon = true;
                                    boolUseable = true;
                                    // �N�[�|���g�p�ɕύX
                                    tuc.getUserAutoCoupon()[0].setUsedFlag( USED );
                                    tuc.getUserAutoCoupon()[0].setUsedDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                    tuc.getUserAutoCoupon()[0].setUsedTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                    ret = tuc.getUserAutoCoupon()[0].updateData( tuc.getUserAutoCoupon()[0].getId(),
                                            tuc.getUserAutoCoupon()[0].getCouponSeq(), tuc.getUserAutoCoupon()[0].getSeq() );

                                    if ( ret != false )
                                    {
                                        // �^�b�`�N�[�|���Ƀf�[�^��o�^
                                        DataApTouchCoupon datc = new DataApTouchCoupon();
                                        ret = datc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                                        datc.setId( Integer.parseInt( paramId ) );
                                        datc.setSeq( Integer.parseInt( paramSeq ) );
                                        datc.setKind( 1 );
                                        datc.setCouponNo( Integer.parseInt( paramCouponSeq ) );
                                        datc.setUserSeq( tuc.getUserAutoCoupon()[0].getCouponSeq() );
                                        datc.insertData();
                                    }

                                }
                                else
                                {
                                    // �����K�pNG
                                    boolResultCoupon = true;
                                    boolUseable = false;
                                    errorCode = dc.getErrorCode();
                                    unavailableMessage = "�N�[�|���������p�ɂȂ�܂���ł����B";
                                }
                            }
                            else
                            {
                                // �N�[�|���ڍ׃f�[�^�Ȃ�
                                boolResultCoupon = true;
                                boolUseable = false;
                                unavailableMessage = "�N�[�|�����擾�ł��܂���ł����B";
                            }
                        }
                        else
                        {
                            // �N�[�|���f�[�^�Ȃ�
                            boolResultCoupon = true;
                            boolUseable = false;
                            unavailableMessage = "�N�[�|�����擾�ł��܂���ł����B";
                        }
                    }
                    else
                    {
                        // �����X�e�[�^�X�G���[
                        // ����NG�G���[��ʂ�
                        boolResultCoupon = true;
                        boolUseable = false;
                        unavailableMessage = "�`�F�b�N�A�E�g�ς݂̂��߂��g�p�ɂȂ�܂���B";
                    }
                }
                else
                {
                    // �����X�e�[�^�X�G���[
                    // ����NG�G���[��ʂ�
                    boolResultCoupon = true;
                    boolUseable = false;
                    unavailableMessage = "�`�F�b�N�A�E�g�ς݂̂��߂��g�p�ɂȂ�܂���B";
                }
            }

            // �z�e���̃N�[�|�������邩�ǂ������m�F
            apCommon = new DtoApCommon();
            // ���ʐݒ�
            apCommon.setId( Integer.parseInt( paramId ) );
            apCommon.setHotelName( dhb.getName() );
            apCommon.setSeq( Integer.parseInt( paramSeq ) );
            apCommon.setRoomNo( dhrm.getRoomNameHost() );

            apCoupon = new DtoApCoupon();
            apCoupon.setApCommon( apCommon );
            apCoupon.setErrorCode( errorCode );
            apCoupon.setErrorMessage( errorMessage );
            apCoupon.setExistsCoupon( boolCoupon );
            apCoupon.setResult( boolResultCoupon );
            apCoupon.setUnavailableKind( unavailableKind );
            apCoupon.setUnavailableMessage( unavailableMessage );
            apCoupon.setUseable( boolUseable );

            request.setAttribute( "DtoApCommon", apCommon );
            request.setAttribute( "DtoApCoupon", apCoupon );

            requestDispatcher = request.getRequestDispatcher( "/phone/htap/CouponUse.jsp" );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "HtDiscountCoupon:" + exception );
        }
        finally
        {
        }
    }

    /**
     * �N�[�|�����Z�b�g
     * 
     * @param dauac
     * @param daac
     * @see �����K�p�N�[�|����DtoApCouponInformation�ɃZ�b�g
     * @return
     */
    private String getPrintCouponNp(DataApUserAutoCoupon[] dauac, DataApAutoCoupon[] daac)
    {
        String couponNo = "";

        couponNo = String.format( "%02d", daac[0].getCouponKind() % 100 ) + "-" +
                String.format( "%03d", dauac[0].getCouponSeq() % 1000 ) + "-" +
                String.format( "%04d", dauac[0].getSeq() % 10000 );

        return couponNo;
    }
}
