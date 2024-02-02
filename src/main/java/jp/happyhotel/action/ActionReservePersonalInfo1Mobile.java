package jp.happyhotel.action;

import java.text.NumberFormat;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataMasterZip;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.owner.LogicOwnerRsvCheckIn;
import jp.happyhotel.reserve.FormReserveOptionSub;
import jp.happyhotel.reserve.FormReserveOptionSubImp;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReservePersonalInfoPC;

/**
 * 
 * �l�����͂P��� Action Class
 */

public class ActionReservePersonalInfo1Mobile extends BaseAction
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
        FormReservePersonalInfoPC frmInfo;
        FormReserveSheetPC frmSheet;
        DataLoginInfo_M2 dataLoginInfo_M2;
        ReserveCommon rsvCmm = new ReserveCommon();
        String paramUidLink = "";
        int paramHotelId = 0;
        int paramSeq = 0;
        int paramRsvDate = 0;
        int paramPlanId = 0;
        String paramMode = "";
        String errMsg = "";
        String checkHotelId = "";
        String rsvZip3 = "";
        String rsvZip4 = "";
        DataMasterZip zip = null;

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
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            frmInfo.setLoginUserId( userId );
            frmInfo.setLoginMailAddr( loginMail );
            frmInfo.setLoginMailAddrList( loginMailList );

            // ���N�G�X�g�f�[�^�̎擾
            frmInfo = getRequestData( request, frmInfo );
            paramHotelId = frmInfo.getSelHotelID();
            paramSeq = frmInfo.getSeq();
            paramRsvDate = frmInfo.getSelRsvDate();
            paramPlanId = frmInfo.getSelPlanID();
            if ( request.getParameter( "mode" ) != null )
            {
                paramMode = request.getParameter( "mode" );
            }
            // �X�֔ԍ��ς���Ă���΂���s�撬���y�єԒn�Ď擾
            rsvZip3 = request.getParameter( "rsvZip3" );
            rsvZip4 = request.getParameter( "rsvZip4" );
            if ( rsvZip3 != null && rsvZip4 != null &&
                    (rsvZip3.equals( frmInfo.getZipCd3() ) && rsvZip4.equals( frmInfo.getZipCd4() )) == false )
            {
                zip = new DataMasterZip();
                zip.getData( frmInfo.getZipCd3() + frmInfo.getZipCd4() );
                frmInfo.setSelJisCd( zip.getJisCode() );
                frmInfo.setAddress3( zip.getAddress2Name() );
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
                // �G���[���̃��b�Z�[�W�쐬
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
                Logging.info( "btnNext.frmInfo.basecharge:" + frmInfo.getBaseChargeTotal() );
                Logging.info( "btnNext.frmSheet.basecharge:" + frmSheet.getBasicTotal() );

                // ������
                frmInfo = setNext( frmInfo, frmSheet );
                if ( frmInfo.getMobileCheckErrKbn() == CHECKERR_INPUT )
                {
                    // ���̓`�F�b�N�ŃG���[
                    request.setAttribute( "dsp", frmInfo );
                    url = carrierUrl + "reserve_personal_info1.jsp";

                }
                else if ( frmInfo.getMobileCheckErrKbn() == CHECKERR_CRITICAL )
                {
                    // �v���I�G���[�G���[
                    url = carrierUrl + "reserve_error.jsp";
                    request.setAttribute( "err", frmInfo.getErrMsg() );

                }
                else
                {
                    // �G���[�Ȃ�
                    url = carrierUrl + "reserve_personal_info2.jsp";
                    request.setAttribute( "dsp", frmInfo );
                }
            }
            else if ( request.getParameter( "btnBack" ) != null )
            {
                // ���߂�
                frmInfo = setBack( frmInfo );
                url = carrierUrl + "reserve_application.jsp";
                request.setAttribute( "dsp", frmInfo );
            }

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
            Logging.error( "[ActionReservePersonalInfo1Mobile.execute() ][hotelId = "
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
                Logging.error( "[ActionReservePersonalInfo1Mobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

    /**
     * 
     * ��ʓ��e�擾
     * 
     * ��ʓ��e�ƁA�v�����\����ʂœ��͂������e���擾����B
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC getRequestData(HttpServletRequest request, FormReservePersonalInfoPC frm) throws Exception
    {

        int paramHotelId = 0;
        int paramPlanId = 0;
        int paramWorkId = 0;
        int paramSeq = 0;
        String paramLastNm = "";
        String paramFirstNm = "";
        String paramLastNmKana = "";
        String paramFirstNmKana = "";
        String paramZip3 = "";
        String paramZip4 = "";
        int paramPrefId = 0;
        int paramJisCd = 0;
        String paramAddress3 = "";
        String paramTel = "";
        String paramMode = "";
        String paramRsvNo = "";
        int paramStatus = 0;

        // ��ʓ��e�擾
        paramHotelId = Integer.parseInt( request.getParameter( "selHotelId" ) );
        paramPlanId = Integer.parseInt( request.getParameter( "selPlanId" ) );
        paramWorkId = Integer.parseInt( request.getParameter( "workId" ) );
        paramSeq = Integer.parseInt( request.getParameter( "selSeq" ) );
        paramMode = request.getParameter( "mode" );
        if ( (request.getParameter( "name_last" ) != null) && (request.getParameter( "name_last" ).toString().length() != 0) )
        {
            paramLastNm = request.getParameter( "name_last" );
        }
        if ( (request.getParameter( "name_first" ) != null) && (request.getParameter( "name_first" ).toString().length() != 0) )
        {
            paramFirstNm = request.getParameter( "name_first" );
        }
        if ( (request.getParameter( "name_last_kana" ) != null) && (request.getParameter( "name_last_kana" ).toString().length() != 0) )
        {
            paramLastNmKana = request.getParameter( "name_last_kana" );
        }
        if ( (request.getParameter( "name_first_kana" ) != null) && (request.getParameter( "name_first_kana" ).toString().length() != 0) )
        {
            paramFirstNmKana = request.getParameter( "name_first_kana" );
        }
        if ( (request.getParameter( "zip_cd3" ) != null) && (request.getParameter( "zip_cd3" ).toString().length() != 0) )
        {
            paramZip3 = request.getParameter( "zip_cd3" );
        }
        if ( (request.getParameter( "zip_cd4" ) != null) && (request.getParameter( "zip_cd4" ).toString().length() != 0) )
        {
            paramZip4 = request.getParameter( "zip_cd4" );
        }
        if ( (request.getParameter( "prefId" ) != null) && (request.getParameter( "prefId" ).toString().length() != 0) )
        {
            paramPrefId = Integer.parseInt( request.getParameter( "prefId" ) );
        }
        if ( (request.getParameter( "jisCd" ) != null) && (request.getParameter( "jisCd" ).toString().length() != 0) )
        {
            paramJisCd = Integer.parseInt( request.getParameter( "jisCd" ) );
        }
        if ( (request.getParameter( "address3" ) != null) && (request.getParameter( "address3" ).toString().length() != 0) )
        {
            paramAddress3 = request.getParameter( "address3" );
        }
        if ( (request.getParameter( "tel" ) != null) && (request.getParameter( "tel" ).toString().length() != 0) )
        {
            paramTel = request.getParameter( "tel" );
        }
        if ( (request.getParameter( "rsvNo" ) != null) && (request.getParameter( "rsvNo" ).toString().length() != 0) )
        {
            paramRsvNo = request.getParameter( "rsvNo" );
        }
        if ( (request.getParameter( "status" ) != null) && (request.getParameter( "status" ).toString().length() != 0) )
        {
            paramStatus = Integer.parseInt( request.getParameter( "status" ) );
        }

        frm.setWorkId( paramWorkId );
        Logging.info( "ActionReservePersonalInfo1Mobile.paramWorkId:" + paramWorkId );

        // ���f�[�^�擾
        frm = getRsvWorkData( frm );

        // �t�H�[���փZ�b�g
        frm.setMode( paramMode );
        frm.setSelHotelID( paramHotelId );
        frm.setSelPlanID( paramPlanId );
        frm.setSelSeq( paramSeq );
        frm.setSeq( paramSeq );
        frm.setLastName( paramLastNm );
        frm.setFirstName( paramFirstNm );
        frm.setLastNameKana( paramLastNmKana );
        frm.setFirstNameKana( paramFirstNmKana );
        frm.setSelPrefId( paramPrefId );
        frm.setSelJisCd( paramJisCd );
        frm.setAddress3( paramAddress3 );
        frm.setTel( paramTel );
        frm.setZipCd3( paramZip3 );
        frm.setZipCd4( paramZip4 );
        frm.setReserveNo( paramRsvNo );
        frm.setStatus( paramStatus );

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
        String planNm = "";
        boolean ret = false;
        NumberFormat objNum = NumberFormat.getCurrencyInstance();
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();
        DataRsvPlan data = new DataRsvPlan();
        ReserveCommon rsvComm = new ReserveCommon();
        int numchild = 0;

        logic.setFrmSheet( frmSheet );
        logic.getReserveWorkData( frm.getWorkId() );

        // ��ʕ\���pForm�ɃZ�b�g
        frm.setSelHotelID( frmSheet.getSelHotelId() );
        frm.setSelPlanID( frmSheet.getSelPlanId() );
        frm.setSelEstTimeArrival( frmSheet.getEstTimeArrival() );
        frm.setSelNumAdult( frmSheet.getAdultNum() );
        if ( rsvComm.checkLoveHotelFlag( frmSheet.getSelHotelId() ) == false )
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
                + Integer.toString( frmSheet.getRsvDate() ).substring( 6, 8 ) + "���i" + week + "�j";
        frm.setReserveDateFormat( rsvDateFormat );
        frm.setSelParkingUsedKbn( frmSheet.getParking() );
        frm.setSelParkingCount( frmSheet.getParkingCnt() );
        frm.setParkingUsedKbnInit( frmSheet.getParking() );
        frm.setBaseChargeTotal( frmSheet.getBasicTotal() );
        frm.setOptionCharge( frmSheet.getOptionChargeTotal() );
        frm.setChargeTotal( frmSheet.getChargeTotal() );
        frm.setBaseChargeView( objNum.format( frmSheet.getBasicTotal() ) );
        frm.setChargeTotalView( objNum.format( frmSheet.getChargeTotal() ) );
        frm.setSelJisCd( frmSheet.getJisCd() );
        frm.setAddress3( frmSheet.getAddress3() );
        frm.setTel( frmSheet.getTel() );
        frm.setRemainder( frmSheet.getReminder() );
        frm.setRemainderMailAddr( frmSheet.getRemainderMail() );
        frm.setDemands( frmSheet.getDemands() );
        frm.setRemarks( frmSheet.getRemarks() );

        Logging.info( "setBaseChargeTotal�F" + frmSheet.getBasicTotal() );

        // �z�e�����擾
        frm = rsvComm.getHotelData( frm );

        // �����c���擾
        frm = rsvComm.getRoomZanSuu( frm );

        // �v�������擾
        ret = data.getData( frm.getSelHotelID(), frm.getSelPlanID() );
        if ( ret )
        {
            planNm = ConvertCharacterSet.convDb2Form( data.getPlanName() );
        }

        // �t�H�[���ɃZ�b�g
        frm.setPlanName( planNm );

        return(frm);
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

        // ��{�������㏑������Ă��܂����߁A���ۑ�
        int baseCharge = frm.getBaseChargeTotal();

        // �v�����ʗ����}�X�^���`�F�b�N�C���J�n�A�I���������擾
        frm = rsvCmm.getCiCoTime( frm );

        if ( frm.getBaseChargeTotal() < baseCharge )
        {
            frm.setBaseChargeTotal( baseCharge );
        }

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

        Logging.info( "ActionReservePersonalInfo1Mobile().basecharge:" + frm.getBaseChargeTotal() );

        // ����ʑJ�ڏ���
        workId = registRsvWork( frm );
        frm.setWorkId( workId );

        // ���̉�ʓ��e���擾
        frm = getPersonlInfo2Data( frm );

        return(frm);
    }

    /**
     * 
     * ����ʕ\��
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @return FormReserveSheetPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC getViewData(FormReservePersonalInfoPC frm) throws Exception
    {
        String week = "";
        String rsvDateFormat = "";
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        ArrayList<Integer> prefIdList = new ArrayList<Integer>();
        ArrayList<String> prefNMList = new ArrayList<String>();
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
        frm.setSelSeq( frmSheet.getSeq() );
        frm.setSeq( frmSheet.getSeq() );
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
        frm.setOrgReserveDate( frmSheet.getRsvDate() );
        week = DateEdit.getWeekName( frmSheet.getRsvDate() );
        rsvDateFormat = Integer.toString( frmSheet.getRsvDate() ).substring( 0, 4 ) + "�N"
                + Integer.toString( frmSheet.getRsvDate() ).substring( 4, 6 ) + "��"
                + Integer.toString( frmSheet.getRsvDate() ).substring( 6, 8 ) + "��(" + week + ")";
        frm.setReserveDateFormat( rsvDateFormat );
        frm.setPlanName( frmSheet.getPlanNm() );
        frm.setSelOptSubNmList( frmSheet.getOptNmList() );
        frm.setSelOptSubChargeTotalList( frmSheet.getOptChargeTotalList() );
        frm.setSelOptSubUnitPriceViewList( frmSheet.getOptUnitPriceViewList() );
        frm.setSelOptNumList( frmSheet.getOptInpMaxQuantityList() );
        frm.setSelOptSubNumList( frmSheet.getOptInpMaxQuantityList() );
        frm.setSelOptSubRemarksList( frmSheet.getOptRemarksList() );
        frm.setChargeTotalView( frmSheet.getChargeTotalView() );

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
     * ���[�N�e�[�u���Ƀf�[�^��o�^
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @return int ���[�NID
     */
    private int registRsvWork(FormReservePersonalInfoPC frm) throws Exception
    {
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();

        // �X�V�������s
        logic.setFrm( frm );
        logic.updReserveWork();
        frm = logic.getFrm();

        return(frm.getWorkId());
    }

    /**
     * 
     * �l������2��ʂ֑J�ڂ��邽�߂̃f�[�^�擾
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC getPersonlInfo2Data(FormReservePersonalInfoPC frm) throws Exception
    {
        String zip3 = "";
        String zip4 = "";
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
        if ( frmSheet.getZip().trim().length() != 0 )
        {
            zip3 = frmSheet.getZip().substring( 0, 3 );
            zip4 = frmSheet.getZip().substring( 4 );
        }
        frm.setZipCd3( zip3 );
        frm.setZipCd4( zip4 );
        frm.setSelPrefId( frmSheet.getPrefId() );
        frm.setPlanName( frmSheet.getPlanNm() );
        frm.setReserveDateFormat( frmSheet.getRsvDateView() );
        frm.setRemarks( frmSheet.getRemarks() );
        frm.setQuestionFlg( frmSheet.getQuestionFlg() );
        frm.setQuestion( frmSheet.getQuestion() );
        frm.setSelOptSubNmList( frmSheet.getOptNmList() );
        frm.setSelOptSubChargeTotalList( frmSheet.getOptChargeTotalList() );
        frm.setSelOptSubUnitPriceViewList( frmSheet.getOptUnitPriceViewList() );
        frm.setSelOptNumList( frmSheet.getOptInpMaxQuantityList() );
        frm.setSelOptSubRemarksList( frmSheet.getOptRemarksList() );

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
        frm = getPlanRequest( frm );

        return(frm);
    }

    /**
     * 
     * �v�����\�����݉�ʂ֑J�ڂ��邽�߂̃f�[�^�擾
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC getPlanRequest(FormReservePersonalInfoPC frm) throws Exception
    {
        String week = "";
        String rsvDateFormat = "";
        int inpMaxQuantity = 0;
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();
        FormReserveOptionSub frmOptSub = new FormReserveOptionSub();
        ReserveCommon rsvCmm = new ReserveCommon();
        ArrayList<Integer> selOptImpSubIdList = new ArrayList<Integer>();
        ArrayList<FormReserveOptionSubImp> frmOptSumImpList = new ArrayList<FormReserveOptionSubImp>();
        ArrayList<String> optSubRemarksList = new ArrayList<String>();
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
        frm.setOrgReserveDate( frmSheet.getRsvDate() );
        week = DateEdit.getWeekName( frmSheet.getRsvDate() );
        rsvDateFormat = Integer.toString( frmSheet.getRsvDate() ).substring( 0, 4 ) + "�N"
                + Integer.toString( frmSheet.getRsvDate() ).substring( 4, 6 ) + "��"
                + Integer.toString( frmSheet.getRsvDate() ).substring( 6, 8 ) + "���i" + week + "�j";
        frm.setReserveDateFormat( rsvDateFormat );
        frm.setSelParkingUsedKbn( frmSheet.getParking() );
        frm.setSelParkingCount( frmSheet.getParkingCnt() );
        frm.setParkingUsedKbnInit( frmSheet.getParking() );
        frm.setBaseChargeTotal( frmSheet.getBasicTotal() );
        frm.setOptionCharge( frmSheet.getOptionChargeTotal() );
        frm.setChargeTotal( frmSheet.getChargeTotal() );
        frm.setSelSeq( frmSheet.getSeq() );
        frm.setSeq( frmSheet.getSeq() );
        frm.setSelOptSubNmList( frmSheet.getOptNmList() );
        frm.setSelOptSubChargeTotalList( frmSheet.getOptChargeTotalList() );

        // �z�e�����̎擾
        frm = rsvCmm.getHotelData( frm );

        // �\���{��蒓�ԏꗘ�p�敪�A���ԏꗘ�p�䐔���擾
        frm = rsvCmm.getParking( frm );

        // ���ʗ����}�X�^��藿�����[�hID���擾
        // frm = rsvCmm.getDayChargeMode( frm );

        // �v�������A�v����PR�A�l�����X�g�A���ԏꃊ�X�g�̍쐬
        frm = rsvCmm.getPlanData( frm );

        // �����\�莞���̐ݒ�
        frm = rsvCmm.getCiCoTime( frm );

        // �����c���擾
        frm = rsvCmm.getRoomZanSuu( frm );

        // �K�{�I�v�V�������擾
        frmOptSumImpList = rsvCmm.getOptionSubImp( frm.getSelHotelID(), frm.getSelPlanID() );
        frm.setFrmOptSubImpList( frmOptSumImpList );

        // �K�{�I�v�V�����������[�N�e�[�u������擾
        selOptImpSubIdList = logic.getSelOptImpSubIdList( frm.getWorkId() );
        frm.setSelOptionImpSubIdList( selOptImpSubIdList );

        // �ʏ�I�v�V�������擾
        frmOptSub = rsvCmm.getOptionSub( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelRsvDate() );
        for( int i = 0 ; i < frmOptSub.getUnitPriceList().size() ; i++ )
        {
            optSubRemarksList.add( "" );
        }
        frm.setSelOptSubRemarksList( optSubRemarksList );

        // �I������Ă���ʏ�I�v�V�����������[�N�e�[�u������擾
        int rsvQuantity = 0;
        int remaindQuantity = 0;
        ArrayList<Integer> newQuantityList = new ArrayList<Integer>();
        frm = logic.getSelOptSubList( frm.getWorkId(), frm );

        // �X�V�̏ꍇ�̂݃I�v�V�������ʂ̍Đݒ���s���B
        for( int i = 0 ; i < frm.getSelOptSubIdList().size() ; i++ )
        {
            // �c���擾
            remaindQuantity = rsvCmm.getRemaindOption( frm.getSelHotelID(), frm.getSelRsvDate(), frm.getSelOptSubIdList().get( i ) );

            if ( remaindQuantity == -1 )
            {
                // �\�񖳂����́A1���̍ő���͉\�����擾
                inpMaxQuantity = rsvCmm.getInpMaxQuantity( frm.getSelHotelID(), frm.getSelOptSubIdList().get( i ) );
                newQuantityList.add( inpMaxQuantity );
                continue;
            }
            if ( frm.getMode().equals( ReserveCommon.MODE_INS ) )
            {
                // �V�K��
                newQuantityList.add( remaindQuantity );
            }
            else if ( frm.getMode().equals( ReserveCommon.MODE_UPD ) )
            {
                // �X�V��
                // �s�v�ȏꍇ�́A�\��f�[�^�ɓo�^����Ă��鐔�ʂ��擾
                rsvQuantity = logic.getRsvOptionQuantity( frm.getSelHotelID(), frm.getSelOptSubIdList().get( i ), frm.getReserveNo() );

                // �l�̔�r
                if ( rsvQuantity >= remaindQuantity )
                {
                    newQuantityList.add( rsvQuantity );
                }
                else
                {
                    newQuantityList.add( remaindQuantity );
                }
            }
        }
        frmOptSub.setMaxQuantityList( newQuantityList );
        frm.setFrmOptSub( frmOptSub );
        // //�ʏ�I�v�V�������擾
        // frmOptSub = rsvCmm.getOptionSub( frm.getSelHotelID(), frm.getSelPlanID(), frmSheet.getRsvDate() );
        // frm.setFrmOptSub( frmOptSub );
        // for (int i = 0; i < frmOptSub.getUnitPriceList().size(); i ++) {
        // optSubRemarksList.add( "" );
        // }
        // frm.setSelOptSubRemarksList( optSubRemarksList );
        //
        // // �I������Ă���ʏ�I�v�V�����������[�N�e�[�u������擾
        // frm = logic.getSelOptSubList( frm.getWorkId(), frm);

        return(frm);
    }
}
