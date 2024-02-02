package jp.happyhotel.action;

import java.text.NumberFormat;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.DecodeData;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterCity;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataRsvCredit;
import jp.happyhotel.owner.LogicOwnerRsvCheckIn;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReservePersonalInfoPC;

/**
 * 
 * �l�����͂Q��� Action Class
 */

public class ActionReservePersonalInfo2Mobile extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;
    // �G���[�`�F�b�N���
    private static final int  CHECKERR_INPUT    = 1;   // ���̓`�F�b�N�ŃG���[
    private static final int  CHECKERR_CRITICAL = 2;   // �v���I�G���[

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String carrierUrl = "";
        String loginMail = "";
        ArrayList<String> loginMailList = new ArrayList<String>();
        String userId = "";
        int type = 0;
        String url = "";
        String rsvUserId = "";
        FormReservePersonalInfoPC frmInfo = null;
        FormReserveSheetPC frmSheet;
        ReserveCommon rsvCmm = new ReserveCommon();
        DataLoginInfo_M2 dataLoginInfo_M2;

        String paramUidLink = "";
        int paramHotelId = 0;
        int paramSeq = 0;
        int paramRsvDate = 0;
        int paramPlanId = 0;
        String paramMode = "";
        String errMsg = "";
        String maskCardNo = "";

        String checkHotelId = "";

        try
        {
            frmInfo = new FormReservePersonalInfoPC();
            frmSheet = new FormReserveSheetPC();

            // �L�����A�̔���
            type = UserAgent.getUserAgentType( request );
            if ( type == UserAgent.USERAGENT_AU )
            {
                carrierUrl = "../../au/reserve/";
            }
            else if ( type == UserAgent.USERAGENT_VODAFONE )
            {
                carrierUrl = "../../y/reserve/";
            }
            else if ( type == UserAgent.USERAGENT_DOCOMO )
            {
                carrierUrl = "../../i/reserve/";
            }

            // ���C�ɓ���̓o�^����ČĂяo���ꂽ�ꍇ�́A�󂯓n�����ڂ�null�ƂȂ�
            // �z�e��ID���Anull�̏ꍇ�̓G���[�y�[�W�֑J�ڂ�����
            checkHotelId = request.getParameter( "selHotelId" );

            if ( (checkHotelId == null) )
            {
                url = carrierUrl + "reserve_error.jsp";
                errMsg = Message.getMessage( "erro.30008" );
                request.setAttribute( "err", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // ���O�C�����擾
            dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            if ( (dataLoginInfo_M2 != null) && (dataLoginInfo_M2.isMemberFlag() == true) )
            {
                // ���݂���
                userId = dataLoginInfo_M2.getUserId();

                // ���[���A�h���X�̃`�F�b�N
                if ( dataLoginInfo_M2.getMailAddrMobile().equals( "" ) == false )
                {
                    loginMail = dataLoginInfo_M2.getMailAddrMobile();
                    loginMailList.add( dataLoginInfo_M2.getMailAddrMobile() );
                }
                if ( dataLoginInfo_M2.getMailAddr().equals( "" ) == false )
                {
                    if ( loginMail.equals( "" ) != false )
                    {
                        loginMail = dataLoginInfo_M2.getMailAddr();
                    }
                    loginMailList.add( dataLoginInfo_M2.getMailAddr() );
                }
            }
            else
            {
                url = carrierUrl + "reserve_nomember.jsp";
                response.sendRedirect( url );
                return;
            }

            // ��ʓ��e�擾
            frmInfo.setLoginUserId( userId );
            frmInfo.setLoginMailAddr( loginMail );
            frmInfo.setLoginMailAddrList( loginMailList );
            frmInfo = getRequestData( request, frmInfo );
            paramHotelId = frmInfo.getSelHotelID();
            paramSeq = frmInfo.getSeq();
            paramRsvDate = frmInfo.getSelRsvDate();
            paramPlanId = frmInfo.getSelPlanID();
            if ( request.getParameter( "mode" ) != null )
            {
                paramMode = request.getParameter( "mode" );
            }

            // ����������L������̋�ʂ����邽�߂Ƀv�����f�[�^�擾
            ReserveCommon rsvcomm = new ReserveCommon();
            frmInfo.setPaymemberFlg( dataLoginInfo_M2.isPaymemberFlag() );
            frmInfo = rsvcomm.getPlanData( frmInfo );

            int curDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            int curTime = Integer.parseInt( DateEdit.getTime( 1 ) );
            int checkDateST = DateEdit.addDay( curDate, (frmInfo.getRsvEndDate()) );
            int checkDateED = DateEdit.addDay( curDate, (frmInfo.getRsvStartDate()) );
            int checkDatePremiumED = DateEdit.addDay( curDate, (frmInfo.getRsvStartDatePremium()) );
            int checkDatePremiumST = DateEdit.addDay( curDate, (frmInfo.getRsvEndDatePremium()) );

            // �\��\���`�F�b�N
            if ( (paramRsvDate > checkDateST && paramRsvDate < checkDateED &&
                    paramRsvDate >= frmInfo.getSalesStartDay() && paramRsvDate <= frmInfo.getSalesEndDay()) ||
                    (paramRsvDate == checkDateST && curTime <= frmInfo.getRsvEndTime() &&
                            paramRsvDate >= frmInfo.getSalesStartDay() && paramRsvDate <= frmInfo.getSalesEndDay()) ||
                    (paramRsvDate == checkDateED && curTime >= frmInfo.getRsvStartTime() &&
                            paramRsvDate >= frmInfo.getSalesStartDay() && paramRsvDate <= frmInfo.getSalesEndDay()) )
            {
            }
            else
            {
                String errMSG = "";
                String premiuminfo = "0";
                if ( ((paramRsvDate > checkDatePremiumST) && (paramRsvDate < checkDatePremiumED) &&
                        (paramRsvDate >= frmInfo.getSalesStartDay()) && (paramRsvDate <= frmInfo.getSalesEndDay())) ||
                        (paramRsvDate == checkDatePremiumST && curTime <= frmInfo.getRsvEndTime()) ||
                        (paramRsvDate == checkDatePremiumED && curTime >= frmInfo.getRsvStartTime()) )
                {
                    // �v���~�A��������L���̏ꍇ�͕\����ύX
                    String err1 = "�{�v�����̓n�s�z�e�v���~�A������l�̂ݗ\��\�ł��B";
                    errMSG = err1;
                    premiuminfo = "1";
                }
                else
                {
                    errMSG = "�\���t�̊��ԊO�ł��B���萔�ł����A������x�ŏ������蒼���Ă��������B";
                }
                // �`�F�b�NNG�̏ꍇ�A�G���[�y�[�W�֑J�ڂ���B
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", errMSG );
                request.setAttribute( "premiuminfo", premiuminfo );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url );
                requestDispatcher.forward( request, response );
                return;
            }

            // �p�����[�^�̐������`�F�b�N
            errMsg = rsvCmm.checkParam( paramHotelId, paramPlanId, paramSeq, paramRsvDate, ReserveCommon.USER_KBN_USER, paramMode );
            if ( errMsg.trim().length() != 0 )
            {
                // �`�F�b�NNG�̏ꍇ�A�G���[�y�[�W�֑J�ڂ���B
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // �\��l���͈̔̓`�F�b�N
            errMsg = rsvCmm.checkAdultChildNum( paramHotelId, paramPlanId, frmInfo.getSelNumAdult(), frmInfo.getSelNumChild() );
            if ( errMsg.trim().length() != 0 )
            {
                // �`�F�b�NNG�̏ꍇ�A�G���[�y�[�W�֑J�ڂ���B
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( paramMode.equals( ReserveCommon.MODE_UPD ) )
            {
                // ���[�U�[ID�Ɨ\��̍쐬�҂��������m�F
                rsvUserId = rsvCmm.getRsvUserId( frmInfo.getReserveNo() );

                if ( userId.compareTo( rsvUserId ) != 0 )
                {
                    // �Ⴄ�ꍇ�̓G���[�y�[�W
                    url = carrierUrl + "reserve_error.jsp";
                    request.setAttribute( "err", Message.getMessage( "erro.30004" ) );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                    requestDispatcher.forward( request, response );
                    return;
                }
            }

            // ��{�������㏑������Ă��܂����߁A���ۑ�
            int baseCharge = frmInfo.getBaseChargeTotal();

            // �v�����ʗ����}�X�^���`�F�b�N�C���J�n�A�I���������擾
            frmInfo = rsvCmm.getCiCoTime( frmInfo );

            if ( frmInfo.getBaseChargeTotal() < baseCharge )
            {
                frmInfo.setBaseChargeTotal( baseCharge );
            }

            if ( request.getParameter( "btnConfirm" ) != null )
            {
                // ���̓`�F�b�N
                frmInfo = inputCheck( frmInfo );

                // ���̓`�F�b�N
                // 2012/02/02 ���[���A�h���X�̃��X�g���Ȃ�������G���[�Ƃ���悤�ɕύX
                if ( frmInfo.getErrMsg().trim().length() != 0 || loginMailList.size() == 0 )
                {
                    if ( loginMailList.size() == 0 )
                    {
                        frmInfo.setErrMsg( frmInfo.getErrMsg() + "�A���惁�[���A�h���X���o�^����Ă��܂���B</br>" );
                    }
                    // ���̓G���[����
                    request.setAttribute( "dsp", frmInfo );
                    url = carrierUrl + "reserve_personal_info2.jsp";
                }
                else
                {
                    // ���m�F��ʂ�
                    frmSheet = setConfirm( frmInfo, frmSheet );
                    frmSheet.setMailList( loginMailList );
                    if ( frmSheet.getMobileCheckErrKbn() == CHECKERR_CRITICAL )
                    {
                        // �v���I�G���[
                        url = carrierUrl + "reserve_error.jsp";
                        request.setAttribute( "err", frmSheet.getErrMsg() );
                    }
                    else
                    {
                        url = carrierUrl + "reserve_confirmation.jsp";
                        request.setAttribute( "dsp", frmSheet );
                        request.setAttribute( "dsp2", frmInfo );
                    }
                }
            }
            else if ( request.getParameter( "btnRsvCreditConfirm" ) != null )
            {
                // �J�[�h�ԍ������̓G���[
                if ( request.getParameter( "card_no" ) == null || request.getParameter( "card_no" ).equals( "" ) )
                {
                    errMsg += Message.getMessage( "warn.00001", "�J�[�h�ԍ�" );
                }
                else
                {
                    String paramCardNo = request.getParameter( "card_no" );
                    // �J�[�h�ԍ��̗L��������
                    if ( !rsvCmm.CheckCardNo( paramCardNo ) )
                    {
                        errMsg += Message.getMessage( "warn.00009", "�J�[�h�ԍ�" );
                    }
                }
                // �J�[�h�L�����������̓G���[
                if ( request.getParameter( "expire_year" ) == null || request.getParameter( "expire_year" ).equals( "" ) ||
                        request.getParameter( "expire_month" ) == null || request.getParameter( "expire_month" ).equals( "" ) )
                {
                    errMsg += Message.getMessage( "warn.00001", "�L������" );
                }
                else
                {
                    // �L�������̗L��������
                    if ( !rsvCmm.CheckCardLimit( request.getParameter( "expire_year" ), request.getParameter( "expire_month" ) ) )
                    {
                        errMsg += Message.getMessage( "warn.00009", "�L������" );
                    }
                }

                if ( errMsg.equals( "" ) == false )
                {
                    frmInfo = setNext( frmInfo, frmSheet );
                    if ( frmInfo.getMobileCheckErrKbn() == CHECKERR_CRITICAL )
                    {
                        // �v���I�G���[
                        url = carrierUrl + "reserve_error.jsp";
                        request.setAttribute( "err", frmSheet.getErrMsg() );
                    }
                    else
                    {
                        // �N���W�b�g���͉�ʂ֖߂�
                        frmInfo.setErrMsg( errMsg );
                        url = carrierUrl + "reserve_cancel_credit_info.jsp";
                        request.setAttribute( "dsp", frmInfo );
                    }
                }
                else
                {
                    // �J�[�h�ԍ��ƗL�������Z�b�g
                    if ( request.getParameter( "card_no" ) != null )
                    {
                        frmSheet.setCardno( request.getParameter( "card_no" ) );
                        String dispCardNo = request.getParameter( "card_no" );
                        for( int i = 0 ; i < dispCardNo.length() ; i++ )
                        {
                            if ( i < dispCardNo.length() - 4 )
                            {
                                maskCardNo += "*";
                            }
                            else
                            {
                                maskCardNo += dispCardNo.charAt( i );
                            }
                        }
                        frmSheet.setDispcardno( maskCardNo );
                    }
                    if ( request.getParameter( "expire_month" ) != null && !request.getParameter( "expire_month" ).equals( "" ) )
                    {
                        frmSheet.setExpireMonth( Integer.parseInt( request.getParameter( "expire_month" ) ) );
                    }
                    if ( request.getParameter( "expire_year" ) != null && !request.getParameter( "expire_year" ).equals( "" ) )
                    {
                        frmSheet.setExpireYear( Integer.parseInt( request.getParameter( "expire_year" ) ) );
                    }

                    // ���̓`�F�b�N
                    frmInfo = inputCheck( frmInfo );

                    // ���̓`�F�b�N
                    // 2012/02/02 ���[���A�h���X�̃��X�g���Ȃ�������G���[�Ƃ���悤�ɕύX
                    if ( frmInfo.getErrMsg().trim().length() != 0 || loginMailList.size() == 0 )
                    {
                        if ( loginMailList.size() == 0 )
                        {
                            frmInfo.setErrMsg( frmInfo.getErrMsg() + "�A���惁�[���A�h���X���o�^����Ă��܂���B</br>" );
                        }
                        // ���̓G���[����
                        request.setAttribute( "dsp", frmInfo );
                        url = carrierUrl + "reserve_personal_info2.jsp";
                    }
                    else
                    {
                        // ���m�F��ʂ�
                        frmSheet = setConfirm( frmInfo, frmSheet );
                        frmSheet.setMailList( loginMailList );
                        if ( frmSheet.getMobileCheckErrKbn() == CHECKERR_CRITICAL )
                        {
                            // �v���I�G���[
                            url = carrierUrl + "reserve_error.jsp";
                            request.setAttribute( "err", frmSheet.getErrMsg() );
                        }
                        else
                        {
                            if ( paramMode.equals( ReserveCommon.MODE_UPD ) && request.getParameter( "btnRsvCreditConfirm" ) != null )
                            {
                                // �N���W�b�g�J�[�h�X�V��
                                frmSheet.setCreditUpdateFlag( true );
                            }
                            url = carrierUrl + "reserve_confirmation.jsp";
                            request.setAttribute( "dsp", frmSheet );
                            request.setAttribute( "dsp2", frmInfo );
                        }
                    }
                }
            }
            else if ( request.getParameter( "btnRsvCreditReg" ) != null )
            {
                // ���̓`�F�b�N
                frmInfo = inputCheck( frmInfo );

                // ���̓`�F�b�N
                // 2012/02/02 ���[���A�h���X�̃��X�g���Ȃ�������G���[�Ƃ���悤�ɕύX
                if ( frmInfo.getErrMsg().trim().length() != 0 || loginMailList.size() == 0 )
                {
                    if ( loginMailList.size() == 0 )
                    {
                        frmInfo.setErrMsg( frmInfo.getErrMsg() + "�A���惁�[���A�h���X���o�^����Ă��܂���B</br>" );
                    }
                    // ���̓G���[����
                    request.setAttribute( "dsp", frmInfo );
                    url = carrierUrl + "reserve_personal_info2.jsp";
                }
                else
                {
                    frmInfo = setNext( frmInfo, frmSheet );
                    if ( frmInfo.getMobileCheckErrKbn() == CHECKERR_CRITICAL )
                    {
                        // �v���I�G���[
                        url = carrierUrl + "reserve_error.jsp";
                        request.setAttribute( "err", frmSheet.getErrMsg() );
                    }
                    else
                    {
                        // �N���W�b�g���͉�ʂ�
                        url = carrierUrl + "reserve_cancel_credit_info.jsp";
                        request.setAttribute( "dsp", frmInfo );
                    }
                }
            }
            else if ( request.getParameter( "btnBack" ) != null )
            {
                // ���߂�
                frmInfo = setBack( frmInfo );
                url = carrierUrl + "reserve_personal_info1.jsp";
                request.setAttribute( "dsp", frmInfo );
            }
            else if ( request.getParameter( "btnInfo2Back" ) != null )
            {
                // ���߂�
                frmInfo = setBack( frmInfo );
                url = carrierUrl + "reserve_personal_info2.jsp";
                request.setAttribute( "dsp", frmInfo );
            }

            // ��������g�я��擾
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            // �f�o�b�O�����ǂ���
            if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
            {
                url = "/_debug_" + url;
            }
            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReservePersonalInfo2Mobile.execute() ][hotelId = "
                    + paramHotelId + ",planId = " + paramPlanId + ",reserveDate = " + paramRsvDate + "] Exception", exception );
            try
            {
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", Message.getMessage( "erro.30005" ) );
                request.setAttribute( "dsp", frmInfo );

                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReservePersonalInfo2Mobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

    /**
     * 
     * ���̓`�F�b�N
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC inputCheck(FormReservePersonalInfoPC frm) throws Exception
    {

        boolean ret = false;
        ReserveCommon rsvCmm = new ReserveCommon();

        // ���̓`�F�b�N
        ret = rsvCmm.isInputCheck( frm, ReserveCommon.INP_CHECK_MOBILE2 );
        if ( ret == false )
        {
            // �G���[���莞
            frm = getViewData( frm );
            frm.setMobileCheckErrKbn( CHECKERR_INPUT );
        }
        return(frm);
    }

    /**
     * 
     * �m�F��ʂփ{�^������
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReserveSheetPC�I�u�W�F�N�g
     */
    private FormReserveSheetPC setConfirm(FormReservePersonalInfoPC frm, FormReserveSheetPC frmSheet) throws Exception
    {
        boolean inputCheck = false;
        int workId = 0;
        ReserveCommon rsvCmm = new ReserveCommon();
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        LogicOwnerRsvCheckIn logicCheckIn = new LogicOwnerRsvCheckIn();
        String maskCardNo = "";

        // �f�[�^�̃`�F�b�N
        frmSheet = rsvCmm.margeFormSheetPC( frmSheet, frm );
        frmSheet.setWorkId( frm.getWorkId() );
        frmSheet = logicCheckIn.getWorkOptionQuantity( frmSheet );
        frmSheet = rsvCmm.chkDspMaster( frmSheet );
        if ( frmSheet.getErrMsg().trim().length() != 0 )
        {
            // �G���[����
            inputCheck = false;
            frmSheet.setMobileCheckErrKbn( CHECKERR_CRITICAL );
            return(frmSheet);
        }

        // �V�K�o�^���ȊO�́A�X�e�[�^�X�`�F�b�N
        if ( !frm.getMode().equals( ReserveCommon.MODE_INS ) )
        {
            inputCheck = rsvCmm.checkStatus( frm.getSelHotelID(), frm.getReserveNo(), frm.getStatus() );
            if ( inputCheck == false )
            {
                // �G���[����
                frmSheet.setMobileCheckErrKbn( CHECKERR_CRITICAL );
                frmSheet.setErrMsg( Message.getMessage( "warn.00021" ) );
                return(frmSheet);
            }
        }

        // ����ʑJ�ڏ���
        workId = registRsvWork( frm );
        frm.setWorkId( workId );

        // ���̉�ʓ��e���擾
        logic.setFrmSheet( frmSheet );
        logic.getReserveWorkData( frm.getWorkId() );
        frmSheet = logic.getFrmSheet();
        frmSheet.setMail( frm.getLoginMailAddr() );
        frmSheet.setWorkId( frm.getWorkId() );
        frmSheet.setOrgRsvDate( frm.getSelRsvDate() );
        frmSheet.setRsvNo( frm.getReserveNo() );

        // �v�]�\��
        frmSheet.setDispRequestFlag( frm.getDispRequestFlg() );

        // �z�e�����擾
        frm = rsvCmm.getHotelData( frm );
        frmSheet.setHotelNm( frm.getHotelName() );

        // �����c���擾
        frm = rsvCmm.getRoomZanSuu( frm );
        frmSheet.setRoomZansuu( frm.getRoomZanSuu() );

        // �񋟋敪���擾
        frm = rsvCmm.getPlanData( frm );
        frmSheet.setOfferKind( frm.getOfferKind() );

        // �I�v�V�������[�N�e�[�u������K�{�I�v�V�����f�[�^���擾
        frmSheet = logic.getRsvWorkOptData( frmSheet, frm.getWorkId(), ReserveCommon.OPTION_IMP );

        // �I�v�V�������[�N�e�[�u������ʏ�I�v�V�����f�[�^���擾
        frmSheet = logic.getRsvWorkOptData( frmSheet, frm.getWorkId(), ReserveCommon.OPTION_USUAL );

        // �J�[�h�ԍ����擾���ă}�X�N�l���Z�b�g
        if ( frmSheet.getCardno().equals( "" ) && ReserveCommon.checkNoShowCreditHotel( frm.getSelHotelID() ) == true )
        {
            DataRsvCredit rsvcredit = new DataRsvCredit();
            rsvcredit.getData( frm.getReserveNo() );
            String dispCardNo = DecodeData.decodeString( "axpol ptmhyeeahl".getBytes( "8859_1" ), "s h t t i s n h ".getBytes( "8859_1" ), new String( rsvcredit.getCard_no() ) );
            frmSheet.setCardno( dispCardNo );
            for( int i = 0 ; i < dispCardNo.length() ; i++ )
            {
                if ( i < dispCardNo.length() - 4 )
                {
                    maskCardNo += "*";
                }
                else
                {
                    maskCardNo += dispCardNo.charAt( i );
                }
            }
            frmSheet.setDispcardno( maskCardNo );
            frmSheet.setExpireYear( 20 * 100 + rsvcredit.getLimit_date() / 100 );
            frmSheet.setExpireMonth( rsvcredit.getLimit_date() % 100 );
        }

        return(frmSheet);
    }

    /**
     * 
     * ���փ{�^������
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC setNext(FormReservePersonalInfoPC frm, FormReserveSheetPC frmSheet) throws Exception
    {
        boolean inputCheck = false;
        int workId = 0;
        ReserveCommon rsvCmm = new ReserveCommon();
        LogicOwnerRsvCheckIn logic = new LogicOwnerRsvCheckIn();

        // ���̓`�F�b�N
        inputCheck = rsvCmm.isInputCheck( frm, ReserveCommon.INP_CHECK_MOBILE1 );
        if ( inputCheck == false )
        {
            // �G���[���莞
            frm = getViewData( frm );
            frm.setMobileCheckErrKbn( CHECKERR_INPUT );
            return(frm);
        }

        // �f�[�^�̃`�F�b�N
        frmSheet = rsvCmm.margeFormSheetPC( frmSheet, frm );
        frmSheet.setWorkId( frm.getWorkId() );
        frmSheet = logic.getWorkOptionQuantity( frmSheet );
        frmSheet = rsvCmm.chkDspMaster( frmSheet );
        if ( frmSheet.getErrMsg().trim().length() != 0 )
        {
            // �G���[����
            inputCheck = false;
            frm.setErrMsg( frmSheet.getErrMsg() );
            frm.setMobileCheckErrKbn( CHECKERR_CRITICAL );
            return(frm);
        }

        // �V�K�o�^���ȊO�́A�X�e�[�^�X�`�F�b�N
        if ( !frm.getMode().equals( ReserveCommon.MODE_INS ) )
        {
            inputCheck = rsvCmm.checkStatus( frm.getSelHotelID(), frm.getReserveNo(), frm.getStatus() );
            if ( inputCheck == false )
            {
                // �G���[����
                frm.setErrMsg( Message.getMessage( "warn.00021" ) );
                frm.setMobileCheckErrKbn( CHECKERR_CRITICAL );
                return(frm);
            }
        }

        // ����ʑJ�ڏ���
        workId = registRsvWork( frm );
        frm.setWorkId( workId );

        // �z�e�����擾
        frm = rsvCmm.getHotelData( frm );
        // ���̉�ʓ��e���擾
        // frm = getPersonlInfo2Data( frm );

        return(frm);
    }

    /**
     * 
     * ��ʓ��e�擾
     * 
     * @param req HttpServletRequest
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC getRequestData(HttpServletRequest req, FormReservePersonalInfoPC frm) throws Exception
    {
        int paramSelHotelId = 0;
        int paramSelPlanId = 0;
        int paramSelJisCd = 0;
        int paramSeq = 0;
        int paramRsvDate = 0;
        String paramAddress3 = "";
        String paramTel = "";
        int paramReminder = 0;
        String paramReminderMail = "";
        String paramDemands = "";
        int paramWorkId = 0;
        String paramRsvNo = "";
        String paramRemarks = "";

        paramSelHotelId = Integer.parseInt( req.getParameter( "selHotelId" ) );
        paramSelPlanId = Integer.parseInt( req.getParameter( "selPlanId" ) );
        paramSeq = Integer.parseInt( req.getParameter( "selSeq" ) );
        paramRsvDate = Integer.parseInt( req.getParameter( "rsvDate" ) );
        paramWorkId = Integer.parseInt( req.getParameter( "workId" ) );
        paramAddress3 = req.getParameter( "address3" );
        paramTel = req.getParameter( "tel" );
        paramReminderMail = req.getParameter( "other_mail_addr" );

        if ( (req.getParameter( "address2" ) != null) && (req.getParameter( "address2" ).trim().length() != 0) )
        {
            paramSelJisCd = Integer.parseInt( req.getParameter( "address2" ) );
        }
        if ( (req.getParameter( "remainder" ) != null) && (req.getParameter( "remainder" ).trim().length() != 0) )
        {
            paramReminder = Integer.parseInt( req.getParameter( "remainder" ) );
        }
        if ( (req.getParameter( "reserveNo" ) != null) && (req.getParameter( "reserveNo" ).trim().length() != 0) )
        {
            paramRsvNo = req.getParameter( "reserveNo" );
        }
        if ( (req.getParameter( "remarks" ) != null) && (req.getParameter( "remarks" ).trim().length() != 0) )
        {
            paramRemarks = req.getParameter( "remarks" );
        }
        if ( (req.getParameter( "demands" ) != null) && (req.getParameter( "demands" ).trim().length() != 0) )
        {
            paramDemands = req.getParameter( "demands" );
        }

        // ���f�[�^�擾
        frm.setWorkId( paramWorkId );
        frm = getRsvWorkData( frm );

        // �t�H�[���փZ�b�g
        frm.setMode( req.getParameter( "mode" ) );
        frm.setSelHotelID( paramSelHotelId );
        frm.setSelPlanID( paramSelPlanId );
        frm.setOrgReserveDate( paramRsvDate );
        frm.setSelRsvDate( paramRsvDate );
        frm.setSeq( paramSeq );
        frm.setSelSeq( paramSeq );
        frm.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );
        frm.setSelJisCd( paramSelJisCd );
        frm.setAddress3( paramAddress3 );
        frm.setTel( paramTel );
        frm.setRemainder( paramReminder );
        frm.setRemainderMailAddr( paramReminderMail );
        frm.setDemands( paramDemands );
        frm.setReserveNo( paramRsvNo );
        frm.setRemarks( paramRemarks );

        return(frm);
    }

    /**
     * 
     * �v�������f�[�^�擾
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC getRsvWorkData(FormReservePersonalInfoPC frm) throws Exception
    {
        String week = "";
        String rsvDateFormat = "";
        NumberFormat objNum = NumberFormat.getCurrencyInstance();
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();
        ReserveCommon rsvcomm = new ReserveCommon();
        int numchild = 0;

        logic.setFrmSheet( frmSheet );
        logic.getReserveWorkData( frm.getWorkId() );
        frmSheet = logic.getFrmSheet();

        // ��ʕ\���pForm�ɃZ�b�g
        frm.setSelHotelID( frmSheet.getSelHotelId() );
        frm.setSelPlanID( frmSheet.getSelPlanId() );
        frm.setSelEstTimeArrival( frmSheet.getEstTimeArrival() );
        frm.setSelNumAdult( frmSheet.getAdultNum() );
        if ( rsvcomm.checkLoveHotelFlag( frmSheet.getSelHotelId() ) == false )
        {
            numchild = frmSheet.getChildNum();
        }
        frm.setSelNumChild( numchild );
        frm.setSelNumMan( frmSheet.getManNum() );
        frm.setSelNumWoman( frmSheet.getWomanNum() );
        frm.setSelParkingUsedKbn( frmSheet.getParking() );
        frm.setSelParkingCount( frmSheet.getParkingCnt() );
        frm.setSelHiRoofCount( frmSheet.getHiRoofCnt() );
        frm.setSelRsvDate( frmSheet.getRsvDate() );
        week = DateEdit.getWeekName( frmSheet.getRsvDate() );
        rsvDateFormat = Integer.toString( frmSheet.getRsvDate() ).substring( 0, 4 ) + "�N"
                + Integer.toString( frmSheet.getRsvDate() ).substring( 4, 6 ) + "��"
                + Integer.toString( frmSheet.getRsvDate() ).substring( 6, 8 ) + "��(" + week + ")";
        frm.setReserveDateFormat( rsvDateFormat );
        frm.setSelParkingUsedKbn( frmSheet.getParking() );
        frm.setSelParkingCount( frmSheet.getParkingCnt() );
        frm.setParkingUsedKbnInit( frmSheet.getParking() );
        frm.setBaseChargeTotal( frmSheet.getBasicTotal() );
        frm.setOptionCharge( frmSheet.getOptionChargeTotal() );
        frm.setChargeTotal( frmSheet.getChargeTotal() );
        frm.setBaseChargeView( objNum.format( frmSheet.getBasicTotal() ) );
        frm.setChargeTotalView( objNum.format( frmSheet.getChargeTotal() ) );
        frm.setLastName( frmSheet.getLastName() );
        frm.setFirstName( frmSheet.getFirstName() );
        frm.setLastNameKana( frmSheet.getLastNmKana() );
        frm.setFirstNameKana( frmSheet.getFirstNmKana() );
        frm.setZipCd3( frmSheet.getZip().substring( 0, 3 ) );
        frm.setZipCd4( frmSheet.getZip().substring( 4 ) );
        frm.setSelPrefId( frmSheet.getPrefId() );

        return(frm);
    }

    /**
     * 
     * �߂�{�^������
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC setBack(FormReservePersonalInfoPC frm) throws Exception
    {
        int workId = 0;

        // ���[�N�e�[�u���֊i�[
        workId = registRsvWork( frm );
        frm.setWorkId( workId );

        // �O�̉�ʓ��e���擾
        frm = getPersonlInfo1Data( frm );

        return(frm);
    }

    /**
     * 
     * ���[�N�e�[�u���Ƀf�[�^��o�^
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @return int ���[�NID
     */
    private int registRsvWork(FormReservePersonalInfoPC frm) throws Exception
    {
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        DataMasterPref dataPref = new DataMasterPref();
        DataMasterCity dataCity = new DataMasterCity();

        // �s���{�����ݒ�
        dataPref.getData( frm.getSelPrefId() );
        frm.setAddress1( dataPref.getName() );

        // �s�撬�����ݒ�
        dataCity.getData( frm.getSelJisCd() );
        frm.setAddress2( dataCity.getName() );

        // �X�V�������s
        logic.setFrm( frm );
        logic.updReserveWork();
        frm = logic.getFrm();

        logic = null;
        dataPref = null;
        dataCity = null;

        return(frm.getWorkId());
    }

    /**
     * 
     * �l������1��ʂ֑J�ڂ��邽�߂̃f�[�^�擾
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC getPersonlInfo1Data(FormReservePersonalInfoPC frm) throws Exception
    {
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        ArrayList<Integer> prefIdList = new ArrayList<Integer>();
        ArrayList<String> prefNMList = new ArrayList<String>();
        ArrayList<Integer> jisIdList = new ArrayList<Integer>();
        ArrayList<String> jisNMList = new ArrayList<String>();
        ReserveCommon rsvCmm = new ReserveCommon();
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();
        int numchild = 0;

        // ���e�[�u���̃f�[�^�擾
        logic.setFrmSheet( frmSheet );
        logic.getReserveWorkData( frm.getWorkId() );
        frmSheet = logic.getFrmSheet();

        // �I�v�V�������[�N�e�[�u������ʏ�I�v�V�����f�[�^���擾
        frmSheet = logic.getRsvWorkOptData( frmSheet, frm.getWorkId(), ReserveCommon.OPTION_USUAL );

        // ��ʕ\���pForm�ɃZ�b�g
        frm.setSelHotelID( frmSheet.getSelHotelId() );
        frm.setSelPlanID( frmSheet.getSelPlanId() );
        frm.setSelEstTimeArrival( frmSheet.getEstTimeArrival() );
        frm.setSelNumAdult( frmSheet.getAdultNum() );
        if ( rsvCmm.checkLoveHotelFlag( frmSheet.getSelHotelId() ) == false )
        {
            numchild = frmSheet.getChildNum();
        }
        frm.setSelNumChild( numchild );
        frm.setSelNumMan( frmSheet.getManNum() );
        frm.setSelNumWoman( frmSheet.getWomanNum() );
        frm.setSelParkingUsedKbn( frmSheet.getParking() );
        frm.setSelParkingCount( frmSheet.getParkingCnt() );
        frm.setSelHiRoofCount( frmSheet.getHiRoofCnt() );
        frm.setSelRsvDate( frmSheet.getRsvDate() );
        frm.setReserveDateFormat( frmSheet.getRsvDateView() );
        frm.setPlanName( frmSheet.getPlanNm() );
        frm.setSelOptSubNmList( frmSheet.getOptNmList() );
        frm.setSelOptSubChargeTotalList( frmSheet.getOptChargeTotalList() );
        frm.setSelOptSubUnitPriceViewList( frmSheet.getOptUnitPriceViewList() );
        frm.setSelOptNumList( frmSheet.getOptInpMaxQuantityList() );
        frm.setSelOptSubRemarksList( frmSheet.getOptRemarksList() );
        frm.setSelOptSubNumList( frmSheet.getOptInpMaxQuantityList() );

        // �z�e�����擾
        frm = rsvCmm.getHotelData( frm );

        // �����c���擾
        frm = rsvCmm.getRoomZanSuu( frm );

        // �s���{�����X�g�擾
        prefIdList = rsvCmm.getPrefIdList();
        prefNMList = rsvCmm.getPrefNmList();

        // �t�H�[���ɃZ�b�g
        frm.setPrefIdList( prefIdList );
        frm.setPrefNmList( prefNMList );

        // �s�撬�����X�g�擾
        jisIdList = rsvCmm.getJisCdList( frm.getSelPrefId() );
        jisNMList = rsvCmm.getJisNmList( frm.getSelPrefId() );

        // �t�H�[���ɃZ�b�g
        frm.setJisCdList( jisIdList );
        frm.setJisNmList( jisNMList );

        return(frm);
    }

    /**
     * 
     * ����ʕ\������
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC getViewData(FormReservePersonalInfoPC frm) throws Exception
    {
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        ArrayList<Integer> jisIdList = new ArrayList<Integer>();
        ArrayList<String> jisNMList = new ArrayList<String>();
        ReserveCommon rsvCmm = new ReserveCommon();
        DataMasterPref dataPref = new DataMasterPref();
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();

        // ���e�[�u���̃f�[�^�擾
        logic.setFrmSheet( frmSheet );
        logic.getReserveWorkData( frm.getWorkId() );
        frmSheet = logic.getFrmSheet();

        // �I�v�V�������[�N�e�[�u������ʏ�I�v�V�����f�[�^���擾
        frmSheet = logic.getRsvWorkOptData( frmSheet, frm.getWorkId(), ReserveCommon.OPTION_USUAL );

        // ��ʕ\���pForm�ɃZ�b�g
        frm.setOrgReserveDate( frmSheet.getRsvDate() );
        frm.setSelSeq( frmSheet.getSeq() );
        frm.setSeq( frmSheet.getSeq() );
        frm.setZipCd3( frmSheet.getZip().substring( 0, 3 ) );
        frm.setZipCd4( frmSheet.getZip().substring( 4 ) );
        frm.setSelPrefId( frmSheet.getPrefId() );
        frm.setPlanName( frmSheet.getPlanNm() );
        frm.setQuestionFlg( frmSheet.getQuestionFlg() );
        frm.setQuestion( frmSheet.getQuestion() );
        frm.setSelOptSubNmList( frmSheet.getOptNmList() );
        frm.setSelOptSubChargeTotalList( frmSheet.getOptChargeTotalList() );
        frm.setSelOptSubUnitPriceViewList( frmSheet.getOptUnitPriceViewList() );
        frm.setSelOptNumList( frmSheet.getOptInpMaxQuantityList() );
        frm.setSelOptSubRemarksList( frmSheet.getOptRemarksList() );

        // �z�e�����擾
        frm = rsvCmm.getHotelData( frm );

        // �s���{�����擾
        dataPref.getData( frmSheet.getPrefId() );
        frm.setAddress1( dataPref.getName() );

        // �����c���擾
        frm = rsvCmm.getRoomZanSuu( frm );

        // �s�撬�����X�g�擾
        jisIdList = rsvCmm.getJisCdList( frm.getSelPrefId() );
        jisNMList = rsvCmm.getJisNmList( frm.getSelPrefId() );

        // �t�H�[���ɃZ�b�g
        frm.setJisCdList( jisIdList );
        frm.setJisNmList( jisNMList );

        return(frm);
    }

}
