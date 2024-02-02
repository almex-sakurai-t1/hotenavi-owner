package jp.happyhotel.action;

import java.text.NumberFormat;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataRsvDayCharge;
import jp.happyhotel.data.DataRsvPlanCharge;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.owner.LogicOwnerRsvCheckIn;
import jp.happyhotel.reserve.FormReserveOptionSub;
import jp.happyhotel.reserve.FormReserveOptionSubImp;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReservePersonalInfoPC;

/**
 * 
 * �g�єŃv�����\����� Action Class
 */

public class ActionReserveApplicationMobile extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int type = 1;
        String paramUidLink = "";
        String userId = "";
        String loginMail = "";
        ArrayList<String> loginMailList = new ArrayList<String>();
        FormReserveSheetPC frmSheet;
        FormReservePersonalInfoPC frmInfo;
        ReserveCommon rsvCmm = new ReserveCommon();
        LogicOwnerRsvCheckIn logicCheckIn = new LogicOwnerRsvCheckIn();
        DataLoginInfo_M2 dataLoginInfo_M2;
        String url = "";
        String carrierUrl = "";
        String rsvUserId = "";

        boolean inputCheck = false;
        int paramHotelId = 0;
        int paramSeq = 0;
        int paramRsvDate = 0;
        int paramPlanId = 0;
        String paramMode = "";
        String errMsg = "";
        String checkHotelId = "";

        try
        {
            frmInfo = new FormReservePersonalInfoPC();
            frmSheet = new FormReserveSheetPC();

            // �L�����A���擾
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

            // ���C�ɓ���ɓo�^����ČĂяo���ꂽ�ꍇ�́A�󂯓n�����ڂ�null�ƂȂ�
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
            frmInfo.setPaymemberFlg( dataLoginInfo_M2.isPaymemberFlag() );
            if ( request.getParameter( "mode" ) != null )
            {
                paramMode = request.getParameter( "mode" );
            }

            // ����������L������̋�ʂ����邽�߂Ƀv�����f�[�^�擾
            ReserveCommon rsvcomm = new ReserveCommon();
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

            if ( request.getParameter( "btnNext" ) != null )
            {
                // �����փ{�^���N���b�N
                // ���̓`�F�b�N
                inputCheck = rsvCmm.isInputCheck( frmInfo, ReserveCommon.INP_CHECK_PLAN_M );
                if ( inputCheck == false )
                {
                    // �G���[���莞
                    frmInfo = getViewData( frmInfo );
                    request.setAttribute( "dsp", frmInfo );
                    url = carrierUrl + "reserve_application.jsp";
                }

                // �f�[�^�̃`�F�b�N
                if ( inputCheck == true )
                {
                    frmSheet = rsvCmm.margeFormSheetPC( frmSheet, frmInfo );
                    // ���[�N�e�[�u���ɓo�^����Ă���ʏ�I�v�V�������擾
                    frmSheet.setWorkId( frmInfo.getWorkId() );
                    frmSheet = logicCheckIn.getWorkOptionQuantity( frmSheet );
                    frmSheet = rsvCmm.chkDspMaster( frmSheet );
                    if ( frmSheet.getErrMsg().trim().length() == 0 )
                    {
                        // �G���[�Ȃ�
                        inputCheck = true;
                    }
                    else
                    {
                        // �G���[����
                        inputCheck = false;
                        url = carrierUrl + "reserve_error.jsp";
                        request.setAttribute( "err", frmSheet.getErrMsg() );
                    }
                }

                // �V�K�o�^���ȊO�́A�X�e�[�^�X�`�F�b�N
                if ( (inputCheck == true) && (!frmInfo.getMode().equals( ReserveCommon.MODE_INS )) )
                {
                    inputCheck = rsvCmm.checkStatus( frmInfo.getSelHotelID(), frmInfo.getReserveNo(), frmInfo.getStatus() );
                    if ( inputCheck == false )
                    {
                        // �G���[����
                        url = carrierUrl + "reserve_error.jsp";
                        request.setAttribute( "err", Message.getMessage( "warn.00021" ) );
                    }
                }

                if ( inputCheck == true )
                {
                    // ����ʑJ�ڏ���
                    frmInfo = registRsvWork( frmInfo );

                    // ����ʏ����擾
                    frmInfo = getPersonlInfo1Data( frmInfo );

                    // �ߋ��ɗ\�񂵂����Ƃ�����Η\������Z�b�g����
                    DataRsvReserve rsv = new DataRsvReserve();
                    rsv.getDataByUserId( userId );
                    if ( !rsv.getReserveNo().equals( "" ) )
                    {
                        frmInfo.setZipCd3( rsv.getZipCd().substring( 0, 3 ) );
                        frmInfo.setZipCd4( rsv.getZipCd().substring( 3, rsv.getZipCd().length() ) );
                        frmInfo.setSelPrefId( rsv.getPrefCode() );
                        frmInfo.setSelJisCd( rsv.getJisCode() );
                        frmInfo.setAddress3( rsv.getAddress3() );
                        frmInfo.setLastName( rsv.getNameLast() );
                        frmInfo.setFirstName( rsv.getNameFirst() );
                        frmInfo.setLastNameKana( rsv.getNameLastKana() );
                        frmInfo.setFirstNameKana( rsv.getNameFirstKana() );
                        frmInfo.setTel( rsv.getTel1() );
                    }
                    // �v�����ʗ����}�X�^���`�F�b�N�C���J�n�A�I���������擾
                    frmInfo = rsvCmm.getCiCoTime( frmInfo );

                    url = carrierUrl + "reserve_personal_info1.jsp";
                    request.setAttribute( "dsp", frmInfo );
                }
            }

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
            Logging.error( "[ActionReserveApplicationMobile.execute() ][hotelId = "
                    + paramHotelId + ",planId = " + paramPlanId + ",reserveDate = " + paramRsvDate + "] Exception", exception );
            try
            {
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", Message.getMessage( "erro.30005" ) );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReserveApplicationMobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

    /**
     * 
     * ��ʓ��e�擾
     * 
     * @param req HttpServletRequest
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     * @throws Exception
     */
    private FormReservePersonalInfoPC getRequestData(HttpServletRequest req, FormReservePersonalInfoPC frm) throws Exception
    {
        int paramSelHotelId = 0;
        int paramSelPlanId = 0;
        int paramSelNumAdult = 0;
        int paramSelNumChild = 0;
        int paramSelNumMan = 0;
        int paramSelNumWoman = 0;
        int paramSelParkingUsedKbn = 0;
        int paramSelParkingCount = 0;
        int paramSelHiRoofCount = 0;
        int paramSelEstArrivaltime = -99;
        int paramRsvDate = 0;
        int paramSeq = 0;
        int paramWorkId = 0;
        String paramRsvNo = "";
        int paramHotelParking = 0;

        paramSelHotelId = Integer.parseInt( req.getParameter( "selHotelId" ) );
        paramSelPlanId = Integer.parseInt( req.getParameter( "selPlanId" ) );
        paramSelNumAdult = Integer.parseInt( req.getParameter( "num_adult" ) );
        if ( (req.getParameter( "num_man" ) != null) && (req.getParameter( "num_man" ).trim().length() != 0) )
        {
            paramSelNumMan = Integer.parseInt( req.getParameter( "num_man" ) );
        }
        if ( (req.getParameter( "num_woman" ) != null) && (req.getParameter( "num_woman" ).trim().length() != 0) )
        {
            paramSelNumWoman = Integer.parseInt( req.getParameter( "num_woman" ) );
        }
        if ( (req.getParameter( "num_child" ) != null) && (req.getParameter( "num_child" ).trim().length() != 0) )
        {
            paramSelNumChild = Integer.parseInt( req.getParameter( "num_child" ) );
        }
        if ( (req.getParameter( "est_time_arrival" ) != null) && (req.getParameter( "est_time_arrival" ).trim().length() != 0) )
        {
            paramSelEstArrivaltime = Integer.parseInt( req.getParameter( "est_time_arrival" ) );
        }
        if ( (req.getParameter( "parking_used" ) != null) && (req.getParameter( "parking_used" ).trim().length() != 0) )
        {
            paramSelParkingUsedKbn = Integer.parseInt( req.getParameter( "parking_used" ) );
        }
        if ( (req.getParameter( "parking_count" ) != null) && (req.getParameter( "parking_count" ).trim().length() != 0) )
        {
            paramSelParkingCount = Integer.parseInt( req.getParameter( "parking_count" ) );
        }
        if ( (req.getParameter( "hiroof_count" ) != null) && (req.getParameter( "hiroof_count" ).trim().length() != 0) )
        {
            paramSelHiRoofCount = Integer.parseInt( req.getParameter( "hiroof_count" ) );
        }
        paramRsvDate = Integer.parseInt( req.getParameter( "rsvDate" ) );
        paramSeq = Integer.parseInt( req.getParameter( "selSeq" ) );
        paramWorkId = Integer.parseInt( req.getParameter( "workId" ) );
        if ( (req.getParameter( "rsvNo" ) != null) && (req.getParameter( "rsvNo" ).trim().length() != 0) )
        {
            paramRsvNo = req.getParameter( "rsvNo" );
        }
        if ( (req.getParameter( "hotelParking" ) != null) && (req.getParameter( "hotelParking" ).trim().length() != 0) )
        {
            paramHotelParking = Integer.parseInt( req.getParameter( "hotelParking" ) );
        }

        frm.setMode( req.getParameter( "mode" ) );
        frm.setSelHotelID( paramSelHotelId );
        frm.setSelPlanID( paramSelPlanId );
        frm.setSelNumAdult( paramSelNumAdult );
        frm.setSelNumChild( paramSelNumChild );
        frm.setSelNumMan( paramSelNumMan );
        frm.setSelNumWoman( paramSelNumWoman );
        frm.setSelEstTimeArrival( paramSelEstArrivaltime );
        frm.setSelParkingUsedKbn( paramSelParkingUsedKbn );
        frm.setParkingUsedKbnInit( paramSelParkingUsedKbn );
        frm.setSelParkingCount( paramSelParkingCount );
        frm.setSelHiRoofCount( paramSelHiRoofCount );
        frm.setOrgReserveDate( paramRsvDate );
        frm.setSelRsvDate( paramRsvDate );
        frm.setSeq( paramSeq );
        frm.setSelSeq( paramSeq );
        frm.setWorkId( paramWorkId );
        frm.setReserveNo( paramRsvNo );
        frm.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );
        frm.setHotelParking( paramHotelParking );

        // �K�{�I�v�V�����擾
        frm = getOptionImpFormData( req, frm );

        // �ʏ�I�v�V�����擾
        frm = getOptionSubFormData( req, frm );

        return(frm);
    }

    /**
     * 
     * ��ʂőI������Ă���K�{�I�v�V�����擾
     * 
     * @param request HttpServletRequest �I�u�W�F�N�g
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC getOptionImpFormData(HttpServletRequest request, FormReservePersonalInfoPC frm) throws Exception
    {
        ArrayList<Integer> optIdList = new ArrayList<Integer>();
        ArrayList<Integer> selOptIdList = new ArrayList<Integer>();
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        ReserveCommon rsvCmm = new ReserveCommon();
        ArrayList<FormReserveOptionSubImp> frmOptSubImpList = new ArrayList<FormReserveOptionSubImp>();

        try
        {
            // �v����ID�őΏۂ̃I�v�V����ID���擾
            optIdList = logic.getPlanOption( frm.getSelHotelID(), frm.getSelPlanID(), ReserveCommon.OPTION_IMP );

            // ��ʂőI������Ă���T�u�I�v�V����ID���擾
            for( int i = 0 ; i < optIdList.size() ; i++ )
            {
                selOptIdList.add( Integer.parseInt( request.getParameter( "optImp" + optIdList.get( i ) ) ) );
            }

            // �I�v�V�����̃��X�g�擾
            frmOptSubImpList = rsvCmm.getOptionSubImp( frm.getSelHotelID(), frm.getSelPlanID() );

            // �t�H�[���ɃZ�b�g
            frm.setFrmOptSubImpList( frmOptSubImpList );
            frm.setSelOptionImpSubIdList( selOptIdList );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionReserveApplicationMobile.getOptionImpFormData] Exception=" + e.toString() );
            throw e;
        }

        return(frm);
    }

    /**
     * 
     * ��ʂőI������Ă���ʏ�I�v�V�����擾
     * 
     * @param request HttpServletRequest �I�u�W�F�N�g
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC getOptionSubFormData(HttpServletRequest request, FormReservePersonalInfoPC frm) throws Exception
    {
        ArrayList<Integer> optIdList = new ArrayList<Integer>();
        ArrayList<Integer> optNumList = new ArrayList<Integer>();
        ArrayList<String> optRemarksList = new ArrayList<String>();
        ArrayList<Integer> optUnitPriceList = new ArrayList<Integer>();

        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();

        try
        {
            // �v����ID�őΏۂ̃I�v�V����ID���擾
            optIdList = logic.getPlanOption( frm.getSelHotelID(), frm.getSelPlanID(), ReserveCommon.OPTION_USUAL );

            // ��ʂ̒ʏ�I�v�V���������擾
            for( int i = 0 ; i < optIdList.size() ; i++ )
            {
                if ( request.getParameter( "optNum" + optIdList.get( i ) ) == null )
                {
                    optNumList.add( -1 );
                    optRemarksList.add( "" );
                    optUnitPriceList.add( 0 );
                }
                else
                {
                    optNumList.add( Integer.parseInt( request.getParameter( "optNum" + optIdList.get( i ) ) ) );
                    optRemarksList.add( request.getParameter( "optSubRemarks" + optIdList.get( i ) ) );
                    optUnitPriceList.add( Integer.parseInt( request.getParameter( "unitPrice" + optIdList.get( i ) ) ) );
                }
            }

            // �t�H�[���ɃZ�b�g
            frm.setSelOptSubIdList( optIdList );
            frm.setSelOptSubNumList( optNumList );
            frm.setSelOptSubRemarksList( optRemarksList );
            frm.setSelOptSubUnitPriceList( optUnitPriceList );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionReservePersonalInfoPC.getOptionSubFormData] Exception=" + e.toString() );
            throw e;
        }

        return(frm);
    }

    /**
     * 
     * ��ʕ\�����e�Ď擾
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @return FormReserveSheetPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC getViewData(FormReservePersonalInfoPC frm) throws Exception
    {
        String week = "";
        String rsvDateFormat = "";
        ReserveCommon rsvCmm = new ReserveCommon();
        FormReserveOptionSub frmOptSub = new FormReserveOptionSub();

        // �z�e�����̎擾
        frm = rsvCmm.getHotelData( frm );

        // �\���{��蒓�ԏꗘ�p�敪�A���ԏꗘ�p�䐔���擾
        frm = rsvCmm.getParking( frm );

        // ���ʗ����}�X�^��藿�����[�hID���擾
        frm = rsvCmm.getDayChargeMode( frm );

        // �v�������A�v����PR�A�l�����X�g�A���ԏꃊ�X�g�̍쐬
        frm = rsvCmm.getPlanData( frm );

        // �����\�莞���̐ݒ�
        frm = rsvCmm.getCiCoTime( frm );

        // �����c���擾
        frm = rsvCmm.getRoomZanSuu( frm );

        // �\���
        week = DateEdit.getWeekName( frm.getOrgReserveDate() );
        rsvDateFormat = Integer.toString( frm.getOrgReserveDate() ).substring( 0, 4 ) + "�N"
                + Integer.toString( frm.getOrgReserveDate() ).substring( 4, 6 ) + "��"
                + Integer.toString( frm.getOrgReserveDate() ).substring( 6, 8 ) + "��(" + week + ")";
        frm.setReserveDateFormat( rsvDateFormat );

        // �ʏ�I�v�V�������擾
        frmOptSub = rsvCmm.getOptionSub( frm.getSelHotelID(), frm.getSelPlanID(), frm.getOrgReserveDate() );
        frm.setFrmOptSub( frmOptSub );

        return(frm);

    }

    /**
     * 
     * ���[�N�e�[�u���Ƀf�[�^��o�^
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC �I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC registRsvWork(FormReservePersonalInfoPC frmInfo) throws Exception
    {
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();
        ;
        String zip3 = "";
        String zip4 = "";
        int hotelParking = 0;
        int optChargeTotal = 0;
        int basicChargeTotal = 0;

        // �������̐ݒ�
        frmInfo = setCharge( frmInfo );

        // �z�e���̒��ԏ�敪�̑ޔ�
        hotelParking = frmInfo.getHotelParking();
        // ���f�[�^���݃`�F�b�N
        if ( logic.isExistsRsvWork( frmInfo.getWorkId() ) == false )
        {
            // �����݂��Ȃ��ꍇ�͐V�K�ǉ�
            if ( frmInfo.getMode().equals( ReserveCommon.MODE_UPD ) )
            {
                // �X�V�̏ꍇ�A�o�^�ς݃f�[�^���Ď擾
                frmInfo = setReserveData( frmInfo );
            }
            if ( hotelParking == ReserveCommon.PARKING_NO_INPUT )
            {
                frmInfo.setSelParkingUsedKbn( hotelParking );
            }
            logic.setFrm( frmInfo );
            logic.insReserveWork();
            frmInfo = logic.getFrm();

            // �K�{�I�v�V�����������[�N�e�[�u���֓o�^
            frmInfo = setRsvWorkOptionImp( frmInfo, logic );

            // �ʏ�I�v�V�����������[�N�e�[�u���֓o�^
            frmInfo = setRsvWorkOption( frmInfo, logic );

            // �ʏ�I�v�V�������v���z���擾
            optChargeTotal = logic.getOptionChargeTotal( frmInfo.getWorkId() );

            // ��{�����擾
            basicChargeTotal = logic.getBasicChargeTotal( frmInfo.getWorkId() );

            // �\�񃏁[�N�e�[�u���փI�v�V�������v�A�����v�̔��f
            logic.updRsvWorkChargeTotal( frmInfo.getWorkId(), optChargeTotal, optChargeTotal + basicChargeTotal );

            return(frmInfo);
        }

        // �����݂���ꍇ�͍X�V
        // ���e�[�u���̃f�[�^�擾
        logic.setFrmSheet( frmSheet );
        logic.getReserveWorkData( frmInfo.getWorkId() );
        frmSheet = logic.getFrmSheet();

        // ��ʕ\���pForm�ɃZ�b�g
        frmInfo.setSelRsvDate( frmSheet.getRsvDate() );
        frmInfo.setLastName( frmSheet.getLastName() );
        frmInfo.setFirstName( frmSheet.getFirstName() );
        frmInfo.setLastNameKana( frmSheet.getLastNmKana() );
        frmInfo.setFirstNameKana( frmSheet.getFirstNmKana() );
        frmInfo.setSelPrefId( frmSheet.getPrefId() );
        frmInfo.setBaseChargeTotal( frmSheet.getBasicTotal() );
        frmInfo.setOptionCharge( frmSheet.getOptionChargeTotal() );
        frmInfo.setChargeTotal( frmSheet.getChargeTotal() );
        if ( frmSheet.getZip().trim().length() != 0 )
        {
            zip3 = frmSheet.getZip().substring( 0, 3 );
            zip4 = frmSheet.getZip().substring( 4 );
        }
        frmInfo.setZipCd3( zip3 );
        frmInfo.setZipCd4( zip4 );
        frmInfo.setLastName( frmSheet.getLastName() );
        frmInfo.setFirstName( frmSheet.getFirstName() );
        frmInfo.setLastNameKana( frmSheet.getLastNmKana() );
        frmInfo.setFirstNameKana( frmSheet.getFirstNmKana() );
        frmInfo.setSelPrefId( frmSheet.getPrefId() );
        frmInfo.setSelJisCd( frmSheet.getJisCd() );
        frmInfo.setAddress3( frmSheet.getAddress3() );
        frmInfo.setTel( frmSheet.getTel() );
        frmInfo.setRemainder( frmSheet.getReminder() );
        frmInfo.setRemainderMailAddr( frmSheet.getRemainderMail() );
        frmInfo.setDemands( frmSheet.getDemands() );
        frmInfo.setRemarks( frmSheet.getRemarks() );
        if ( hotelParking == ReserveCommon.PARKING_NO_INPUT )
        {
            frmInfo.setSelParkingUsedKbn( hotelParking );
        }

        // �X�V�������s
        logic.setFrm( frmInfo );
        logic.updReserveWork();

        // �K�{�I�v�V�����������[�N�e�[�u���֓o�^
        frmInfo = setRsvWorkOptionImp( frmInfo, logic );

        // �ʏ�I�v�V�����������[�N�e�[�u���֓o�^
        frmInfo = setRsvWorkOption( frmInfo, logic );

        // �ʏ�I�v�V�������v���z���擾
        optChargeTotal = logic.getOptionChargeTotal( frmInfo.getWorkId() );

        // ��{�����擾
        basicChargeTotal = logic.getBasicChargeTotal( frmInfo.getWorkId() );

        // �\�񃏁[�N�e�[�u���փI�v�V�������v�A�����v�̔��f
        logic.updRsvWorkChargeTotal( frmInfo.getWorkId(), optChargeTotal, optChargeTotal + basicChargeTotal );

        return(frmInfo);
    }

    /**
     * 
     * �K�{�I�v�V��������\�񉼃e�[�u���֓o�^
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return �Ȃ�
     */
    private FormReservePersonalInfoPC setRsvWorkOptionImp(FormReservePersonalInfoPC frm, LogicReservePersonalInfoPC logic) throws Exception
    {
        // ���[�N�e�[�u���폜
        logic.deleteRsvOptionWork( frm.getWorkId() );

        for( int i = 0 ; i < frm.getFrmOptSubImpList().size() ; i++ )
        {
            FormReserveOptionSubImp frmOptSubImp = frm.getFrmOptSubImpList().get( i );

            if ( (frmOptSubImp.getOptIdList() != null) && (frmOptSubImp.getOptIdList().size() != 0) )
            {
                // �I�v�V��������
                logic.insRsvOptionWork( frmOptSubImp.getOptIdList().get( 0 ), frm.getSelOptionImpSubIdList().get( i ), -1, 0, 0, "", 0 );
            }
        }

        return(frm);
    }

    /**
     * 
     * �ʏ�I�v�V��������\�񉼃e�[�u���֓o�^
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return �Ȃ�
     */
    private FormReservePersonalInfoPC setRsvWorkOption(FormReservePersonalInfoPC frm, LogicReservePersonalInfoPC logic) throws Exception
    {
        int num = 0;
        int unitPrice = 0;
        int chargeTotal = 0;
        int quantityFlg = 0;
        String remarks = "";

        for( int i = 0 ; i < frm.getSelOptSubIdList().size() ; i++ )
        {
            num = 0;
            unitPrice = 0;
            chargeTotal = 0;
            quantityFlg = 0;
            remarks = "";

            if ( frm.getSelOptSubNumList().get( i ) == 0 )
            {
                // �s�v�̏ꍇ�̐��ʃt���O�ݒ�
                quantityFlg = ReserveCommon.QUANTITY_NEED_NO;
            }

            if ( frm.getSelOptSubNumList().get( i ) > 0 )
            {
                // ���ʑI�����Ă���ꍇ
                num = frm.getSelOptSubNumList().get( i );
                unitPrice = frm.getSelOptSubUnitPriceList().get( i );
                chargeTotal = num * unitPrice;
                remarks = frm.getSelOptSubRemarksList().get( i );
            }

            logic.insRsvOptionWork( frm.getSelOptSubIdList().get( i ), 1, num, unitPrice, chargeTotal, remarks, quantityFlg );

        }
        return(frm);
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
        ReserveCommon rsvCmm = new ReserveCommon();
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();

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
        frm.setSelNumChild( frmSheet.getChildNum() );
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
        frm.setChargeTotalView( frmSheet.getChargeTotalView() );
        frm.setRemarks( frmSheet.getRemarks() );

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

        return(frm);
    }

    /**
     * 
     * �������Z�o
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC setCharge(FormReservePersonalInfoPC frm)
    {
        int chargeId = 0;
        int adultTwoPrice = 0;
        int adultOnePrice = 0;
        int adultAddPrice = 0;
        int childAddPrice = 0;
        int sumAdult = 0;
        int sumChild = 0;
        int baseChargeTotal = 0;
        int chargeTotal = 0;
        int optCharge = 0;
        boolean ret = false;
        NumberFormat objNum = NumberFormat.getCurrencyInstance();

        // ���ʗ����̎擾
        DataRsvDayCharge dataDayCharge = new DataRsvDayCharge();
        dataDayCharge.getData( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelRsvDate() );
        chargeId = dataDayCharge.getChargeModeId();

        // �v�����ʗ����̎擾
        DataRsvPlanCharge dataPlanCharge = new DataRsvPlanCharge();
        ret = dataPlanCharge.getData( frm.getSelHotelID(), frm.getSelPlanID(), chargeId );
        if ( ret )
        {
            adultTwoPrice = dataPlanCharge.getAdultTwoCharge();
            adultOnePrice = dataPlanCharge.getAdultOneCharge();
            adultAddPrice = dataPlanCharge.getAdultAddCharge();
            childAddPrice = dataPlanCharge.getChildAddCharge();
        }

        // ��{�����Z�o
        switch( frm.getSelNumAdult() )
        {
            case 1:
                sumAdult = adultOnePrice;
                break;
            case 2:
                sumAdult = adultTwoPrice;
                break;
            default:
                sumAdult = adultTwoPrice + (adultAddPrice * (frm.getSelNumAdult() - 2));
                break;
        }
        // �q������
        sumChild = (childAddPrice * frm.getSelNumChild());
        baseChargeTotal = sumAdult + sumChild;

        // �����v = �I�v�V�������� + ��{����
        chargeTotal = optCharge + baseChargeTotal;

        // �t�H�[���ɃZ�b�g
        frm.setBaseChargeTotal( baseChargeTotal );
        frm.setOptionCharge( optCharge );
        frm.setChargeTotal( chargeTotal );
        frm.setBaseChargeView( objNum.format( baseChargeTotal ) );
        frm.setChargeTotalView( objNum.format( chargeTotal ) );

        objNum = null;
        dataDayCharge = null;
        dataPlanCharge = null;

        return(frm);
    }

    /**
     * 
     * �o�^����Ă���\��f�[�^���擾���A��ʂ̒l�ƃ}�[�W
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC setReserveData(FormReservePersonalInfoPC frm) throws Exception
    {
        int selNumAdult = 0;
        int selNumChild = 0;
        int selParking = 0;
        int selParkingCnt = 0;
        int selArrivalTime = 0;
        int selHiRoofCount = 0;

        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();

        // ��ʂ̓��e��ޔ�
        selNumAdult = frm.getSelNumAdult();
        selNumChild = frm.getSelNumChild();
        selParking = frm.getSelParkingUsedKbn();
        selParkingCnt = frm.getSelParkingCount();
        selHiRoofCount = frm.getSelHiRoofCount();
        selArrivalTime = frm.getSelEstTimeArrival();

        // �\��f�[�^�̒��o
        logic.setFrm( frm );
        logic.getReserveData();
        frm = logic.getFrm();

        // ��ʓ��e�ƃ}�[�W
        frm.setSelNumAdult( selNumAdult );
        frm.setSelNumChild( selNumChild );
        frm.setSelParkingUsedKbn( selParking );
        frm.setSelParkingCount( selParkingCnt );
        frm.setSelHiRoofCount( selHiRoofCount );
        frm.setSelEstTimeArrival( selArrivalTime );

        return(frm);

    }
}
