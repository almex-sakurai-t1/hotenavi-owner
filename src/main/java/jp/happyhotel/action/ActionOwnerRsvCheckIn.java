package jp.happyhotel.action;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.EncodeData;
import jp.happyhotel.common.GMOCcsCredit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataRsvCredit;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataRsvReserveBasic;
import jp.happyhotel.data.DataRsvRoom;
import jp.happyhotel.data.DataRsvSpid;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.owner.FormOwnerRsvManageCalendar;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvCheckIn;
import jp.happyhotel.owner.LogicOwnerRsvManageCalendar;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.reserve.FormReserveOptionSub;
import jp.happyhotel.reserve.FormReserveOptionSubImp;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveInitPC;
import jp.happyhotel.reserve.LogicReservePersonalInfoPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserPointPay;

/**
 * 
 * �\��m��^���X�m�F�^�L�����Z����� Action Class
 */

public class ActionOwnerRsvCheckIn extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {

        // ���O�C���ҏ��
        String loginTel = "";
        int paramHotelId = 0;
        String paramRsvNo = "";
        int paramRsvDate = 0;
        int paramCheckAgree = 0;
        int paramBtn = 0; // �����ꂽ�{�^��
        int paramStatus = 0;
        int paramWorkId = 0;
        int paramPlanId = 0;
        int paramSeq = 0;
        int paramOrgRsvDate = 0;
        int paramNoshow = 0;
        int offerKind = 0;
        int paramCancelCheck = 0;
        int paramAdult = 0;
        int paramChild = 0;
        int paramMan = 0;
        int paramWoman = 0;
        String paramHapihoteuserId = "";
        String paramMode = "";
        String paramHapihoteId = "";
        String paramHapihoteMail = "";
        String paramHapihoteTel = "";
        String paramCardNo = "";
        String paramDispCardNo = "";
        String paramCreditUpdateFlag = "";
        int paramExpireMonth = 0;
        int paramExpireYear = 0;
        String url = "";
        String paramUserKbn = "";
        String errMsg = "";
        String[] paramHapihoteMailList = null;
        ArrayList<String> hapihoteMailList = new ArrayList<String>();
        int ownerUserId = 0;
        boolean isLoginUser = false;
        FormReserveSheetPC frmSheetPC;
        FormReservePersonalInfoPC frmInfo;
        DataRsvPlan dataPlan = new DataRsvPlan();
        DataLoginInfo_M2 dataLoginInfo_M2 = null;
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        String hotenaviId = "";
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();
        DataRsvReserveBasic drrb = new DataRsvReserveBasic();
        DataRsvReserve dhrr = new DataRsvReserve();

        String checkHotelId = "";

        try
        {
            // ���C�ɓ���̓o�^����ČĂяo���ꂽ�ꍇ�́A�󂯓n�����ڂ�null�ƂȂ�
            // �z�e��ID���Anull�̏ꍇ�̓G���[�y�[�W�֑J�ڂ�����
            checkHotelId = request.getParameter( "selHotelId" );

            if ( (checkHotelId == null) )
            {
                errMsg = Message.getMessage( "erro.30008" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            frmSheetPC = new FormReserveSheetPC();

            // ��ʂ̃p�����[�^�擾
            paramHotelId = Integer.parseInt( request.getParameter( "selHotelId" ) );
            paramRsvNo = request.getParameter( "rsvNo" );
            paramRsvDate = Integer.parseInt( request.getParameter( "rsvDate" ) );
            paramStatus = Integer.parseInt( request.getParameter( "status" ) );
            if ( (request.getParameter( "check" ) != null) && (request.getParameter( "check" ).trim().length() != 0) )
            {
                paramCheckAgree = 1;
            }
            paramWorkId = Integer.parseInt( request.getParameter( "workId" ) );
            paramPlanId = Integer.parseInt( request.getParameter( "selPlanId" ) );
            paramSeq = Integer.parseInt( request.getParameter( "seq" ) );
            paramMode = request.getParameter( "mode" );
            paramOrgRsvDate = Integer.parseInt( request.getParameter( "orgRsvDate" ) );
            if ( (request.getParameter( "noShow" ) != null) && (request.getParameter( "noShow" ).trim().length() != 0) )
            {
                paramNoshow = Integer.parseInt( request.getParameter( "noShow" ) );
            }
            paramUserKbn = request.getParameter( "userKbn" );
            if ( (request.getParameter( "cancelCheck" ) != null) && (request.getParameter( "cancelCheck" ).trim().length() != 0) )
            {
                paramCancelCheck = Integer.parseInt( request.getParameter( "cancelCheck" ) );
            }
            paramHapihoteId = request.getParameter( "hapihoteUserId" );
            paramHapihoteMail = request.getParameter( "hapihoteAddr" );
            paramHapihoteMailList = request.getParameterValues( "hapihoteAddr" );
            paramHapihoteTel = request.getParameter( "hapihoteTel" );
            paramHapihoteuserId = request.getParameter( "hapihoteUserId" );
            paramAdult = Integer.parseInt( request.getParameter( "adult" ) );
            paramChild = Integer.parseInt( request.getParameter( "child" ) );
            if ( (request.getParameter( "man" ) != null) && (request.getParameter( "man" ).trim().length() != 0) )
            {
                paramMan = Integer.parseInt( request.getParameter( "man" ) );
            }
            else
            {
                paramMan = 0;
            }
            if ( (request.getParameter( "woman" ) != null) && (request.getParameter( "woman" ).trim().length() != 0) )
            {
                paramWoman = Integer.parseInt( request.getParameter( "woman" ) );
            }
            else
            {
                paramWoman = 0;
            }
            if ( request.getParameter( "cardno" ) != null )
            {
                paramCardNo = request.getParameter( "cardno" );
            }
            if ( request.getParameter( "dispcardno" ) != null )
            {
                paramDispCardNo = request.getParameter( "dispcardno" );
            }
            if ( request.getParameter( "expiremonth" ) != null )
            {
                paramExpireMonth = Integer.parseInt( request.getParameter( "expiremonth" ) );
            }
            if ( request.getParameter( "expireyear" ) != null )
            {
                paramExpireYear = Integer.parseInt( request.getParameter( "expireyear" ) );
            }
            if ( request.getParameter( "creditUpdateFlag" ) != null )
            {
                paramCreditUpdateFlag = request.getParameter( "creditUpdateFlag" );
            }

            // ���[���A�h���X���X�g
            if ( paramHapihoteMailList != null )
            {
                for( int l = 0 ; l < paramHapihoteMailList.length ; l++ )
                {
                    hapihoteMailList.add( paramHapihoteMailList[l] );
                }
            }
            paramHapihoteTel = request.getParameter( "hapihoteTel" );

            // ���O�C�����[�U�[���擾
            if ( paramUserKbn.equals( ReserveCommon.USER_KBN_USER ) )
            {
                // ���[�U�[�̏ꍇ
                dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

                if ( dataLoginInfo_M2 == null )
                {
                    // ���O�A�E�g��Ԃ̏ꍇ�̓G���[
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_nomember.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }

                if ( dataLoginInfo_M2.isMemberFlag() == true )
                {
                    // ���݂���
                    isLoginUser = true;
                }
            }
            else if ( paramUserKbn.equals( ReserveCommon.USER_KBN_OWNER ) )
            {
                // �I�[�i�[�̏ꍇ
                ownerUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
                if ( ownerUserId != 0 )
                {
                    // ���݂���
                    isLoginUser = true;
                }
                hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
                logicMenu.setFrm( new FormOwnerBkoMenu() );
                disptype = logicMenu.getUserAuth( hotenaviId, ownerUserId );
                adminflag = logicMenu.getAdminFlg( hotenaviId, ownerUserId );
                imediaflag = OwnerRsvCommon.getImediaFlag( hotenaviId, ownerUserId );
            }

            if ( (isLoginUser == false) || ((!paramUserKbn.equals( ReserveCommon.USER_KBN_USER )) && (!paramUserKbn.equals( ReserveCommon.USER_KBN_OWNER ))) )
            {
                // ���O�C�����[�U�[�����݂��Ȃ��ꍇ�̓G���[
                errMsg = Message.getMessage( "erro.30004" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // ���I�[�v�����ǂ������擾����
            drrb.getData( paramHotelId );

            frmSheetPC.setSelHotelId( paramHotelId );
            frmSheetPC.setRsvNo( paramRsvNo );
            frmSheetPC.setRsvDate( paramRsvDate );
            frmSheetPC.setAgree( paramCheckAgree );
            frmSheetPC.setUserId( paramHapihoteuserId );
            frmSheetPC.setMail( paramHapihoteMail );
            frmSheetPC.setLoginUserTel( loginTel );
            frmSheetPC.setWorkId( paramWorkId );
            frmSheetPC.setSelPlanId( paramPlanId );
            frmSheetPC.setSeq( paramSeq );
            frmSheetPC.setOrgRsvSeq( paramSeq );
            frmSheetPC.setMode( paramMode );
            frmSheetPC.setNoShow( paramNoshow );
            frmSheetPC.setUserKbn( paramUserKbn );
            frmSheetPC.setStatus( paramStatus );
            frmSheetPC.setCancelCheck( paramCancelCheck );
            frmSheetPC.setLoginUserId( paramHapihoteId );
            frmSheetPC.setLoginUserTel( paramHapihoteTel );
            frmSheetPC.setLoginUserMail( paramHapihoteMail );
            frmSheetPC.setTermKind( ReserveCommon.TERM_KIND_PC );
            frmSheetPC.setMailList( hapihoteMailList );
            frmSheetPC.setCardno( paramCardNo );
            frmSheetPC.setDispcardno( paramDispCardNo );
            frmSheetPC.setExpireMonth( paramExpireMonth );
            frmSheetPC.setExpireYear( paramExpireYear );
            if ( paramCreditUpdateFlag.equals( "true" ) )
            {
                frmSheetPC.setCreditUpdateFlag( true );
            }
            else
            {
                frmSheetPC.setCreditUpdateFlag( false );
            }

            // ���[�U�̏ꍇ�ɁA�p�����[�^�ƃ}�X�^�`�F�b�N�����{����
            // �L�����Z���̓p�����[�^�`�F�b�N�����Ȃ�
            if ( paramUserKbn.equals( ReserveCommon.USER_KBN_USER ) && (paramMode.equals( ReserveCommon.MODE_CANCEL ) == false) )
            {
                // �p�����[�^�̐������`�F�b�N�����{
                ReserveCommon rsvCmm = new ReserveCommon();

                errMsg = rsvCmm.checkParam( paramHotelId, paramPlanId, paramSeq, paramRsvDate, paramUserKbn, paramMode );
                if ( errMsg.trim().length() != 0 )
                {
                    // ���I�[�v���������牽�����Ȃ�
                    if ( drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 0 )
                    {
                        // �`�F�b�NNG�̏ꍇ�A�G���[�y�[�W�֑J�ڂ���B
                        request.setAttribute( "errMsg", errMsg );
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                        requestDispatcher.forward( request, response );
                        return;
                    }
                }

                // �\��l���͈̔̓`�F�b�N
                errMsg = rsvCmm.checkAdultChildNum( paramHotelId, paramPlanId, paramAdult, paramChild );
                if ( errMsg.trim().length() != 0 )
                {
                    // �`�F�b�NNG�̏ꍇ�A�G���[�y�[�W�֑J�ڂ���B
                    request.setAttribute( "errMsg", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }

                // �\��m�莞�ɁA�}�X�^�`�F�b�N�����{
                if ( request.getParameter( "btnRsvFix" ) != null )
                {
                    FormReserveSheetPC frmSheetPC_Check = new FormReserveSheetPC();
                    frmSheetPC_Check = rsvCmm.chkDspMaster( frmSheetPC );
                    if ( frmSheetPC_Check.getErrMsg().trim().length() != 0 )
                    {
                        // ���I�[�v������Ȃ��ƃG���[
                        if ( drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 0 )
                        {
                            // �`�F�b�NNG�̏ꍇ�A�G���[�y�[�W�֑J�ڂ���B
                            request.setAttribute( "errMsg", frmSheetPC_Check.getErrMsg() );
                            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                            requestDispatcher.forward( request, response );
                            return;
                        }
                    }
                    frmInfo = new FormReservePersonalInfoPC();
                    frmInfo.setSelHotelID( paramHotelId );
                    frmInfo.setSelPlanID( paramPlanId );
                    frmInfo.setSelSeq( paramSeq );
                    frmInfo.setSelRsvDate( paramRsvDate );
                    frmInfo.setOrgReserveDate( paramOrgRsvDate );
                    frmInfo.setLoginUserId( paramHapihoteId );
                    frmInfo.setLoginMailAddr( paramHapihoteMail );
                    frmInfo.setLoginMailAddrList( hapihoteMailList );
                    frmInfo.setLoginTel( paramHapihoteTel );
                    if ( dataLoginInfo_M2 != null )
                    {
                        frmInfo.setPaymemberFlg( dataLoginInfo_M2.isPaymemberFlag() );
                    }
                    else
                    {
                        frmInfo.setPaymemberFlg( false );
                    }
                    frmInfo = rsvCmm.getPlanData( frmInfo );

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
                        if ( ((paramRsvDate > checkDatePremiumST) && (paramRsvDate < checkDatePremiumED) &&
                                (paramRsvDate >= frmInfo.getSalesStartDay()) && (paramRsvDate <= frmInfo.getSalesEndDay())) ||
                                (paramRsvDate == checkDatePremiumST && curTime <= frmInfo.getRsvEndTime()) ||
                                (paramRsvDate == checkDatePremiumED && curTime >= frmInfo.getRsvStartTime()) )
                        {
                            // �v���~�A��������L���̏ꍇ�͕\����ύX
                            String err1 = "�{�v�����̓n�s�z�e�v���~�A������l�̂ݗ\��\�ł��B";
                            errMsg = err1;
                        }
                        else
                        {
                            // ���I�[�v���������牽�����Ȃ�
                            if ( drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 0 )
                            {

                                errMsg = "�\���t�̊��ԊO�ł��B���萔�ł����A������x�ŏ������蒼���Ă��������B";
                            }
                        } // �G���[��񂪓����Ă���ꍇ�̂�
                        if ( errMsg.equals( "" ) == false )
                        {
                            // �`�F�b�NNG�̏ꍇ�A�G���[�y�[�W�֑J�ڂ���B
                            request.setAttribute( "errMsg", errMsg );
                            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                            requestDispatcher.forward( request, response );
                            return;
                        }
                    }

                }
            }

            // �z�e���̒񋟋敪�擾
            dataPlan.getData( paramHotelId, paramPlanId );
            offerKind = dataPlan.getOfferKind();
            frmSheetPC.setOfferKind( offerKind );

            if ( paramUserKbn.equals( ReserveCommon.USER_KBN_OWNER ) == true && (request.getParameter( "btnCancel" ) != null || request.getParameter( "btnRaiten" ) != null || request.getParameter( "btnRsvFix" ) != null ||
                    request.getParameter( "btnBack" ) != null || request.getParameter( "btnUndoCancel" ) != null) &&
                    disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
            {
                // �����̂Ȃ����[�U�̓G���[�y�[�W��\������
                errMsg = Message.getMessage( "erro.30001", "�X�e�[�^�X���X�V���錠��" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // �����ꂽ�{�^���ɂ���ď�����U�蕪����B
            if ( request.getParameter( "btnCancel" ) != null )
            {
                if ( paramUserKbn.equals( ReserveCommon.USER_KBN_USER ) )
                {
                    frmSheetPC.setCancelKind( ReserveCommon.CANCEL_USER );
                }

                // ���L�����Z��
                frmSheetPC = execCancel( frmSheetPC, paramStatus );
                frmSheetPC.setMail( paramHapihoteMail );
                frmSheetPC.setMailList( hapihoteMailList );
                url = "/reserve/reserve_sheet_PC.jsp";
                request.setAttribute( "FORM_ReserveSheetPC", frmSheetPC );
            }
            else if ( request.getParameter( "btnRaiten" ) != null )
            {
                // �����X�m�F
                paramSeq = 0;
                if ( (request.getParameter( "roomNo" ) != null) && (request.getParameter( "roomNo" ).trim().length() != 0) )
                {
                    paramSeq = Integer.parseInt( request.getParameter( "roomNo" ) );
                }
                frmSheetPC.setSeq( paramSeq );
                // TODO
                frmSheetPC = execRaiten( frmSheetPC, paramStatus );
                // �G���[���莞
                if ( frmSheetPC.getErrMsg().trim().length() != 0 )
                {
                    frmSheetPC.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );
                    frmSheetPC.setMode( ReserveCommon.MODE_RAITEN );
                    frmSheetPC.setMail( paramHapihoteMail );
                    frmSheetPC.setMailList( hapihoteMailList );
                }
                else
                {
                    // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
                    frmSheetPC.setMode( "" );

                    // ��������n�s�z�e�^�b�`�̃f�[�^�쐬

                    // �`�F�b�N�C���f�[�^���쐬����
                    int nowPoint = 0;
                    int ciCode = 0;
                    boolean ret = false;
                    boolean updateCi = false;
                    UserPointPay upp = new UserPointPay();
                    HotelCi hc = new HotelCi();
                    nowPoint = upp.getNowPoint( frmSheetPC.getUserId(), false );

                    // ���m��̃f�[�^��24���Ԉȓ��ɂ�������o�^���Ȃ��B
                    ret = hc.getCheckInBeforeData( frmSheetPC.getSelHotelId(), frmSheetPC.getUserId() );
                    if ( ret == false )
                    {
                        // �`�F�b�N�C���f�[�^�o�^
                        hc = hc.registCiData( frmSheetPC.getUserId(), frmSheetPC.getSelHotelId() );
                        ciCode = hc.getHotelCi().getSeq();
                        if ( ciCode > 0 )
                        {
                            ret = true;
                        }
                        else
                        {
                            ret = false;
                        }
                    }
                    else
                    {
                        ciCode = hc.getHotelCi().getSeq();
                        ret = false;
                    }

                    // ���������o�^�ς݂ł���Ύ}�Ԓǉ��ő}���A�o�^����Ă��Ȃ���΍X�V
                    if ( hc.getHotelCi().getRoomNo().compareTo( "" ) != 0 )
                    {
                        updateCi = false;
                    }
                    else
                    {
                        updateCi = true;
                    }
                    hc.getHotelCi().setRoomNo( Integer.toString( paramSeq ) );
                    // �\��ԍ����Z�b�g
                    hc.getHotelCi().setRsvNo( paramRsvNo );

                    UserBasicInfo ubi = new UserBasicInfo();
                    int extUserFlag = 0;
                    if ( ubi.isLvjUser( frmSheetPC.getUserId() ) )
                    {
                        extUserFlag = 1;
                    }
                    hc.getHotelCi().setExtUserFlag( extUserFlag );

                    // �\����͎��g�p�}�C�����Z�b�g
                    if ( dhrr.getData( frmSheetPC.getSelHotelId(), paramRsvNo ) )
                    {
                        hc.getHotelCi().setUsePoint( dhrr.getUsedMile() );
                        hc.getHotelCi().setUseDate( dhrr.getAcceptDate() );
                        hc.getHotelCi().setUseTime( dhrr.getAcceptTime() );
                    }

                    // �\��̗��p�������Z�b�g���Ă���
                    // hc.getHotelCi().setAmount( frm.getChargeTotal() + frm.getOptionChargeTotal() );

                    // �`�F�b�N�C���f�[�^���X�V�܂��̓C���T�[�g
                    if ( updateCi != false )
                    {
                        // �X�V
                        ret = hc.getHotelCi().updateData( frmSheetPC.getSelHotelId(), hc.getHotelCi().getSeq(), hc.getHotelCi().getSubSeq() );
                    }
                    else
                    {
                        // false�Ȃ�Ύ}�ԍ���ǉ�����K�v������
                        if ( ret == false )
                        {
                            hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                        }
                        ret = hc.getHotelCi().insertData();
                    }

                }
                url = "/reserve/reserve_sheet_PC.jsp";
                request.setAttribute( "FORM_ReserveSheetPC", frmSheetPC );
            }
            else if ( request.getParameter( "btnRsvFix" ) != null )
            {
                // ���\��o�^�E�X�V
                frmSheetPC.setUserAgent( request.getHeader( "user-agent" ) );
                frmSheetPC = execRsvFix( frmSheetPC );
                if ( frmSheetPC.isCreditAuthorityNgFlag() == true )
                {
                    // �N���W�b�g�F�؂��Ă���z�e���łm�f�������ꍇ�̂݃N���W�b�g�ԍ����͉�ʂ֖߂�
                    url = "/reserve/reserve_cancel_credit_info.jsp";
                    frmInfo = new FormReservePersonalInfoPC();
                    frmInfo.setSelHotelID( paramHotelId );
                    frmInfo.setSelSeq( paramSeq );
                    frmInfo.setSelPlanID( paramPlanId );
                    frmInfo.setSelRsvDate( paramRsvDate );
                    frmInfo.setMode( paramMode );
                    frmInfo.setWorkId( paramWorkId );
                    frmInfo.setOrgReserveDate( paramOrgRsvDate );
                    frmInfo.setSelNumAdult( paramAdult );
                    frmInfo.setSelNumChild( paramChild );
                    frmInfo.setSelNumMan( paramMan );
                    frmInfo.setSelNumWoman( paramWoman );

                    frmInfo = execBack( frmInfo );
                    frmInfo.setLoginUserId( paramHapihoteId );
                    frmInfo.setLoginTel( paramHapihoteTel );
                    frmInfo.setLoginMailAddr( paramHapihoteMail );
                    frmInfo.setLoginMailAddrList( hapihoteMailList );
                    frmInfo.setLoginUserKbn( paramUserKbn );
                    // �������̃G���[���̂���
                    frmInfo.setErrMsg( frmSheetPC.getErrMsg() );
                    request.setAttribute( "dsp", frmInfo );
                }
                else
                {
                    frmSheetPC.setOrgRsvDate( paramOrgRsvDate );
                    frmSheetPC.setMail( paramHapihoteMail );
                    frmSheetPC.setMailList( hapihoteMailList );
                    frmSheetPC.setLoginUserId( paramHapihoteId );
                    frmSheetPC.setLoginUserMail( paramHapihoteMail );
                    frmSheetPC.setLoginUserTel( paramHapihoteTel );
                    frmSheetPC.setUserKbn( paramUserKbn );
                    frmSheetPC.setSeq( paramSeq );
                    frmSheetPC.setOfferKind( offerKind );
                    url = "/reserve/reserve_jump_sheet.jsp";
                    request.setAttribute( "FORM_ReserveSheetPC", frmSheetPC );
                }
            }
            else if ( request.getParameter( "btnBack" ) != null )
            {
                // ���߂�
                frmInfo = new FormReservePersonalInfoPC();
                frmInfo.setSelHotelID( paramHotelId );
                frmInfo.setSelSeq( paramSeq );
                frmInfo.setSelPlanID( paramPlanId );
                frmInfo.setSelRsvDate( paramRsvDate );
                frmInfo.setMode( paramMode );
                frmInfo.setWorkId( paramWorkId );
                frmInfo.setOrgReserveDate( paramOrgRsvDate );

                frmInfo = execBack( frmInfo );

                // ���O�C�����[�U�[(�\��o�^���[�U�[)���擾
                frmInfo.setLoginUserId( paramHapihoteId );
                frmInfo.setLoginTel( paramHapihoteTel );
                frmInfo.setLoginMailAddr( paramHapihoteMail );
                frmInfo.setLoginMailAddrList( hapihoteMailList );
                frmInfo.setLoginUserKbn( paramUserKbn );
                if ( ReserveCommon.checkNoShowCreditHotel( paramHotelId ) && ((paramMode.equals( ReserveCommon.MODE_INS ) || paramCreditUpdateFlag.equals( "true" ) == true)) )
                {
                    frmInfo.setCardno( paramCardNo );
                    frmInfo.setExpiremonth( paramExpireMonth );
                    frmInfo.setExpireyear( paramExpireYear );
                    url = "/reserve/reserve_cancel_credit_info.jsp";
                }
                else
                {
                    url = "/reserve/reserve_personal_info.jsp";
                }
                request.setAttribute( "dsp", frmInfo );
            }
            // �L�����Z���̎����������̓���
            else if ( request.getParameter( "btnUndoCancel" ) != null )
            {
                frmSheetPC = execUndoCancel( frmSheetPC, frmSheetPC.getStatus() );

                url = "/reserve/reserve_sheet_PC.jsp";
                request.setAttribute( "FORM_ReserveSheetPC", frmSheetPC );
            }

            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvCheckIn.execute() ][hotelId = "
                    + paramHotelId + ",reserveNo = " + paramRsvNo + ",paramBtn = " + paramBtn + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( IncompatibleClassChangeError eee )
            {
                Logging.info( eee.toString() );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvCheckIn.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

    /**
     * �L�����Z���{�^���N���b�N����
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @throws
     */
    private FormReserveSheetPC execCancel(FormReserveSheetPC frm, int status) throws Exception
    {
        String errMsg = "";
        String mode = "";
        boolean ret = false;
        LogicOwnerRsvCheckIn logic;
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();

        try
        {
            // �V�K�o�^���ȊO�����f�[�^�̃X�e�[�^�X�̃`�F�b�N
            if ( rsvCmm.checkStatus( frm.getSelHotelId(), frm.getRsvNo(), status ) == false )
            {
                errMsg = Message.getMessage( "warn.00019" );
                frm.setErrMsg( errMsg );
                return(frm);
            }

            // �\��L�����Z���`�F�b�N������Ă��邩
            if ( frm.getCancelCheck() == 0 )
            {
                // �o�^���s
                errMsg = Message.getMessage( "warn.00032" );

                // ��ʓ��e�Ď擾
                // �\��f�[�^���o
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( frm.getMode() );

                return(frm);
            }

            // ���[�N�e�[�u���ɓo�^����Ă���ʏ�I�v�V�������擾
            logic = new LogicOwnerRsvCheckIn();
            frm = logic.getOptionQuantity( frm );

            // �f�[�^�X�V�i�L�����Z�������j
            logic.setFrm( frm );
            if ( frm.getAdultNum() == -1 ) // �V�\��
            {
                ret = logic.execRsvCancel( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_NEWRSV );
            }
            else
            {
                ret = logic.execRsvCancel( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_OLDRSV );
            }
            if ( ret == false )
            {
                // �o�^���s
                errMsg = frm.getErrMsg().trim();
                mode = frm.getMode();

                // ��ʓ��e�Ď擾
                // �\��f�[�^���o
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( mode );
                return(frm);
            }

            // �o�^�f�[�^�̎擾
            logicSheet = new LogicReserveSheetPC();
            logicSheet.setFrm( frm );
            logicSheet.getData( 1 );

            // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
            frm.setMode( "" );
            frm.setStatus( ReserveCommon.RSV_STATUS_CANCEL );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvCheckIn.execCancel() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvCheckIn.execCancel() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * �L�����Z������{�^���N���b�N����
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @throws
     */
    public FormReserveSheetPC execUndoCancel(FormReserveSheetPC frm, int status) throws Exception
    {
        String errMsg = "";
        String mode = "";
        boolean ret = false;
        LogicOwnerRsvCheckIn logic;
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();

        try
        {

            // ���[�N�e�[�u���ɓo�^����Ă���ʏ�I�v�V�������擾
            logic = new LogicOwnerRsvCheckIn();
            frm = logic.getOptionQuantity( frm );

            // �f�[�^�X�V�i�L�����Z�������������j
            logic.setFrm( frm );
            if ( frm.getAdultNum() == -1 ) // �V�\��
            {
                ret = logic.execRsvUndoCancel( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_NEWRSV );
            }
            else
            {
                ret = logic.execRsvUndoCancel( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_OLDRSV );
            }
            if ( ret == false )
            {
                // �o�^���s
                errMsg = frm.getErrMsg().trim();
                mode = frm.getMode();

                // ��ʓ��e�Ď擾
                // �\��f�[�^���o
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( mode );
                return(frm);
            }

            // �o�^�f�[�^�̎擾
            logicSheet = new LogicReserveSheetPC();
            logicSheet.setFrm( frm );
            logicSheet.getData( 1 );

            // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
            frm.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvCheckIn.execCancel() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvCheckIn.execCancel() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * ���X�m�F�{�^���N���b�N����
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @throws
     */
    private FormReserveSheetPC execRaiten(FormReserveSheetPC frm, int status) throws Exception
    {
        String errMsg = "";
        String mode = "";
        boolean ret = false;
        int addPoint = 0;
        int reflectDate = 0;
        int rsvDate = 0;
        int arrivalTime = 0;
        int addBonusMile = 0;
        LogicOwnerRsvCheckIn logicCheckIn = new LogicOwnerRsvCheckIn();
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();

        try
        {
            // �V�K�o�^���ȊO�����f�[�^�̃X�e�[�^�X�̃`�F�b�N
            if ( rsvCmm.checkStatus( frm.getSelHotelId(), frm.getRsvNo(), status ) == false )
            {
                errMsg = Message.getMessage( "warn.00020" );
                frm.setErrMsg( errMsg );
                frm = getRaitenRegistData( frm );
                return(frm);
            }

            // �����̖��I���`�F�b�N
            if ( frm.getSeq() == 0 )
            {
                errMsg = Message.getMessage( "warn.00002", "�`�F�b�N�C�����镔���ԍ�" );
                frm.setErrMsg( errMsg );
                frm = getRaitenRegistData( frm );
                return(frm);
            }

            // �|�C���g�f�[�^�̐ݒ�
            addPoint = logicCheckIn.getRsvPointData( frm.getRsvNo(), 1 );
            rsvDate = logicCheckIn.getRsvPointData( frm.getRsvNo(), 2 );
            arrivalTime = logicCheckIn.getRsvPointData( frm.getRsvNo(), 3 );
            addBonusMile = logicCheckIn.getRsvPointData( frm.getRsvNo(), 4 );
            frm.setAddPoint( addPoint );
            frm.setAddBonusMile( addBonusMile );

            // ���f���̐ݒ�
            reflectDate = getReflectDate( rsvDate, arrivalTime );
            frm.setReflectDate( reflectDate );

            // �f�[�^�X�V�i���X�m�F�j
            logicCheckIn.setFrm( frm );
            if ( frm.getAdultNum() == -1 ) // �V�\��
            {
                ret = logicCheckIn.execRaiten( frm.getSelHotelId(), frm.getRsvNo(), frm.getRsvDate(), frm.getRoomNo(), ReserveCommon.SCHEMA_NEWRSV );
            }
            else
            {
                ret = logicCheckIn.execRaiten( frm.getSelHotelId(), frm.getRsvNo(), frm.getRsvDate(), frm.getRoomNo(), ReserveCommon.SCHEMA_OLDRSV );
            }

            if ( ret == false )
            {
                // �o�^���s
                errMsg = frm.getErrMsg().trim();
                mode = frm.getMode();

                // ��ʓ��e�Ď擾
                // �\��f�[�^���o
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( mode );

                return(frm);
            }

            // �����������b�Z�[�W
            switch( status )
            {
                case 1:
                    // ��t
                    frm.setProcMsg( Message.getMessage( "warn.00024" ) );
                    break;
                case 2:
                    // ���p�ς�
                    if ( frm.getMode().equals( ReserveCommon.MODE_RAITEN ) )
                    {
                        // ���X�m�F����
                        frm.setProcMsg( Message.getMessage( "warn.00025" ) );
                    }
                    else
                    {
                        // ���p�������߂��Ă���ꍇ
                        frm.setProcMsg( Message.getMessage( "warn.00026" ) );
                    }
                    break;
            }

            // �o�^�f�[�^�̎擾
            frm = getRaitenRegistData( frm );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvCheckIn.execRaiten() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvCheckIn.execRaiten() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * �L�����[�U�|�C���g�ꎞ�f�[�^�̔��f���擾
     * 
     * @param int rsvDate �\���
     * @param int arrivalTime �����\�莞��
     * @return int ���f��
     * @throws Exception
     */
    private int getReflectDate(int rsvDate, int arrivalTime) throws Exception
    {
        int retDate = 0;
        int limitFlg = 0;
        int range = 0;
        String year = "";
        String month = "";
        String day = "";
        String rsvYear = "";
        String rsvMonth = "";
        String rsvDay = "";
        String rsvHour = "";
        String rsvMinutes = "";
        String rsvSecond = "";
        String arrivalTimeStr = "";
        Calendar calendar = Calendar.getInstance();

        // �|�C���g�Ǘ��}�X�^����f�[�^�擾
        limitFlg = OwnerRsvCommon.getInitHapyPoint( 3 );
        range = OwnerRsvCommon.getInitHapyPoint( 4 );

        // ���t�ݒ�
        rsvYear = Integer.toString( rsvDate ).substring( 0, 4 );
        rsvMonth = Integer.toString( rsvDate ).substring( 4, 6 );
        rsvDay = Integer.toString( rsvDate ).substring( 6, 8 );

        arrivalTimeStr = ConvertTime.convTimeStr( arrivalTime, 0 );
        rsvHour = arrivalTimeStr.substring( 0, 2 );
        rsvMinutes = arrivalTimeStr.substring( 2, 4 );
        rsvSecond = arrivalTimeStr.substring( 4 );
        calendar.set( Integer.parseInt( rsvYear ), Integer.parseInt( rsvMonth ) - 1, Integer.parseInt( rsvDay ),
                Integer.parseInt( rsvHour ), Integer.parseInt( rsvMinutes ), Integer.parseInt( rsvSecond ) );

        switch( limitFlg )
        {
            case OwnerRsvCommon.LIMIT_FLG_TIME:
                // ���ԉ��Z
                calendar.add( Calendar.HOUR, range );
                break;

            case OwnerRsvCommon.LIMIT_FLG_DAY:
                // ���t���Z
                calendar.add( Calendar.DATE, range );
                break;

            case OwnerRsvCommon.LIMIT_FLG_MONTH:
                // �����Z
                calendar.add( Calendar.MONTH, range );
                break;
        }

        year = Integer.toString( calendar.get( Calendar.YEAR ) );
        month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );
        day = String.format( "%1$02d", calendar.get( Calendar.DATE ) );

        retDate = Integer.parseInt( year + month + day );

        return(retDate);
    }

    /**
     * ���X�m�F���̓o�^�f�[�^�擾����
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @throws Exception
     */
    private FormReserveSheetPC getRaitenRegistData(FormReserveSheetPC frm) throws Exception
    {
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();

        // �o�^�f�[�^�̎擾
        logicSheet.setFrm( frm );
        logicSheet.getData( 2 );

        frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );
        frm.setStatus( ReserveCommon.RSV_STATUS_ZUMI );
        frm.setMode( ReserveCommon.MODE_RAITEN );

        return(frm);
    }

    /**
     * �\��m��{�^���N���b�N����
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @throws
     */
    private FormReserveSheetPC execRsvFix(FormReserveSheetPC frm) throws Exception
    {
        String errMsg = "";
        String mode = "";
        String paramRsvNo = "";
        int reminderFlg = 0;
        LogicReservePersonalInfoPC logicPC = new LogicReservePersonalInfoPC();
        LogicOwnerRsvCheckIn logicCheckIn = new LogicOwnerRsvCheckIn();
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();
        FormReservePersonalInfoPC frmPC = new FormReservePersonalInfoPC();
        ReserveCommon rsvcomm = new ReserveCommon();
        boolean noshowCreditFlag = false;
        GMOCcsCredit gmoccs = null;
        DataRsvSpid dataspid = null;

        try
        {
            paramRsvNo = frm.getRsvNo();

            // ���G���[�Ȃ����A�m�菈�����s
            // ���Ӄ`�F�b�N������Ă��邩
            if ( frm.getAgree() == 0 )
            {
                errMsg = errMsg + Message.getMessage( "warn.00010" ) + "<br />";
                logicPC.setFrmSheet( frm );
                logicPC.getReserveWorkData( frm.getWorkId() );
                frm = logicPC.getFrmSheet();
                frm.setRsvNo( paramRsvNo );
                frm.setErrMsg( errMsg );

                // �I�v�V�������[�N�e�[�u������I�v�V�����f�[�^���擾
                frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_IMP );
                frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_USUAL );

                return(frm);
            }

            noshowCreditFlag = rsvcomm.checkNoShowCreditHotel( frm.getSelHotelId() );

            // �������s
            // �m�[�V���[�N���W�b�g�Ώۃz�e���Ȃ�N���W�b�g���`�F�b�N����
            if ( noshowCreditFlag && (frm.getMode().equals( ReserveCommon.MODE_INS ) || frm.isCreditUpdateFlag() == true) )
            {
                gmoccs = new GMOCcsCredit();
                dataspid = new DataRsvSpid();
                dataspid.getDataByHotelid( frm.getSelHotelId() );
                // �J�[�h�ԍ�
                gmoccs.setCardNo( frm.getCardno() );
                // �L������
                gmoccs.setCardExpire( String.valueOf( (frm.getExpireYear() * 100 + frm.getExpireMonth()) ) );
                // SPID
                gmoccs.setSpid( dataspid.getSpid() );
                // 1�~�Z�b�g
                gmoccs.setAmount( 1 );
                // 1�~�I�[�\��
                if ( gmoccs.execAuthority() == false )
                {
                    errMsg = "���̃N���W�b�g�J�[�h�́A�����p�ł��܂���B<br />�\���󂠂�܂��񂪍ēx���͂��肢���܂��B<br />";
                    logicPC.setFrmSheet( frm );
                    logicPC.getReserveWorkData( frm.getWorkId() );
                    frm = logicPC.getFrmSheet();
                    frm.setRsvNo( paramRsvNo );
                    frm.setErrMsg( errMsg );
                    frm.setCreditAuthorityNgFlag( true );

                    // �I�v�V�������[�N�e�[�u������I�v�V�����f�[�^���擾
                    frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_IMP );
                    frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_USUAL );
                    return(frm);
                }
                else
                {
                    // �I�[�\������
                    frm.setCreditAuthorityNgFlag( false );
                }
            }

            // ���}�C���_�[�t���O�擾
            reminderFlg = logicCheckIn.getWorkReminderFlg( frm.getWorkId() );
            frm.setReminder( reminderFlg );

            // ���[�N�e�[�u���ɓo�^����Ă���ʏ�I�v�V�������擾
            frm = logicCheckIn.getWorkOptionQuantity( frm );

            logicCheckIn.setFrm( frm );
            if ( frm.getAdultNum() == -1 ) // �V�\��
            {
                if ( frm.getMode().equals( ReserveCommon.MODE_INS ) )
                {
                    // �V�K�o�^
                    logicCheckIn.insReserve( frm.getUserAgent(), ReserveCommon.SCHEMA_NEWRSV );
                }
                else if ( frm.getMode().equals( ReserveCommon.MODE_UPD ) )
                {
                    // �X�V
                    logicCheckIn.updReserve( frm.getUserAgent(), ReserveCommon.SCHEMA_NEWRSV );
                }
            }
            else
            {
                if ( frm.getMode().equals( ReserveCommon.MODE_INS ) )
                {
                    // �V�K�o�^
                    logicCheckIn.insReserve( frm.getUserAgent(), ReserveCommon.SCHEMA_OLDRSV );
                }
                else if ( frm.getMode().equals( ReserveCommon.MODE_UPD ) )
                {
                    // �X�V
                    logicCheckIn.updReserve( frm.getUserAgent(), ReserveCommon.SCHEMA_OLDRSV );
                }
            }

            if ( frm.getErrMsg().trim().length() != 0 )
            {
                // �o�^���s
                errMsg = frm.getErrMsg().trim();
                mode = frm.getMode();

                // ��ʓ��e�Ď擾
                frmPC.setSelHotelID( frm.getSelHotelId() );
                frmPC.setWorkId( frm.getWorkId() );
                frm = getRsvWorkData( frmPC );
                frm.setErrMsg( errMsg );
                frm.setMode( mode );

                // �I�v�V�������[�N�e�[�u������K�{�I�v�V�����f�[�^���擾
                frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_IMP );
                frm = logicPC.getRsvWorkOptData( frm, frm.getWorkId(), ReserveCommon.OPTION_USUAL );

                return(frm);
            }
            // �o�^����

            if ( (noshowCreditFlag == true && frm.isCreditAuthorityNgFlag() == false && (frm.getMode().equals( ReserveCommon.MODE_INS ) || frm.isCreditUpdateFlag() == true))
                    || (noshowCreditFlag && (frm.getMode().equals( ReserveCommon.MODE_UPD ) && frm.isCreditUpdateFlag() == false)) )
            {
                byte[] key = "axpol ptmhyeeahl".getBytes();

                // �Í��x�N�^�[�iInitialization Vector�F�������x�N�g���j
                byte[] ivBytes = "s h t t i s n h ".getBytes();

                String cardno = frm.getCardno();
                // �Í���
                String encode = EncodeData.encodeString( key, ivBytes, cardno );

                // �N���W�b�g�f�[�^�o�^
                DataRsvCredit datarsv = new DataRsvCredit();
                // �N���W�b�g�f�[�^���݊m�F
                if ( datarsv.getData( frm.getRsvNo() ) )
                {
                    // ���݂����UPDATE
                    datarsv.setCard_no( "" );
                    datarsv.setLimit_date( (frm.getExpireYear() * 100 + frm.getExpireMonth()) );
                    datarsv.setLast_update( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    datarsv.setLast_uptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    if ( datarsv.updateData() == false )
                    {
                        // TODO�F�ǂ����邩�c
                    }
                }
                else
                {
                    datarsv.setReserve_no( frm.getRsvNo() );
                    datarsv.setCard_no( "" );
                    datarsv.setLimit_date( (frm.getExpireYear() * 100 + frm.getExpireMonth()) );
                    datarsv.setId( frm.getSelHotelId() );
                    datarsv.setLast_update( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    datarsv.setLast_uptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    datarsv.setDel_date( 0 );
                    datarsv.setDel_time( 0 );
                    datarsv.setDel_flag( 0 );
                    if ( datarsv.insertData() == false )
                    {
                        // TODO�F�ǂ����邩�c

                    }
                }

            }

            frm = logicCheckIn.getFrm();

            // �o�^�f�[�^�̎擾
            logicSheet.setFrm( frm );
            logicSheet.getData( 1 );

            // ���[�N�e�[�u������Ώۃf�[�^�폜
            logicPC.deleteRsvWork( frm.getWorkId() );
            logicPC.deleteRsvOptionWork( frm.getWorkId() );

            // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
            frm.setMode( "" );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvCheckIn.execRsvFix() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvCheckIn.execRsvFix() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * �߂�{�^���N���b�N����
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @throws
     */
    private FormReservePersonalInfoPC execBack(FormReservePersonalInfoPC frm) throws Exception
    {
        String week = "";
        String reserveDate = "";
        String reserveDateFormat = "";
        int inpMaxQuantity = 0;
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        ReserveCommon rsvCmm = new ReserveCommon();
        FormReserveOptionSub frmOptSub = new FormReserveOptionSub();
        ArrayList<FormReserveOptionSubImp> frmOptSumImpList = new ArrayList<FormReserveOptionSubImp>();
        ArrayList<Integer> selOptImpSubIdList = new ArrayList<Integer>();
        ArrayList<String> optSubRemarksList = new ArrayList<String>();

        try
        {
            // ���t�̕\���`��
            week = DateEdit.getWeekName( frm.getSelRsvDate() );
            reserveDate = Integer.toString( frm.getSelRsvDate() );
            reserveDateFormat = reserveDate.substring( 0, 4 ) + "�N" + reserveDate.substring( 4, 6 ) + "��" + reserveDate.substring( 6, 8 ) + "���i" + week + "�j";
            frm.setReserveDateFormat( reserveDateFormat );

            // �z�e����{���擾
            frm = rsvCmm.getHotelData( frm );

            // �\���{��蒓�ԏꗘ�p�敪�A���ԏꗘ�p�䐔���擾
            frm = rsvCmm.getParking( frm );
            frm.setParkingUsedKbnInit( 0 );

            // �v�����}�X�^���e������擾
            frm = rsvCmm.getPlanData( frm );
            frm.setSelNumAdult( 0 );
            frm.setSelNumChild( 0 );
            frm.setSelNumMan( 0 );
            frm.setSelNumWoman( 0 );

            // �ʗ����}�X�^���`�F�b�N�C���J�n�A�I���������擾
            frm = rsvCmm.getCiCoTime( frm );

            // �ݔ����
            frm = getEquip( frm );

            // �V�X�e�����t�E�����̎擾
            frm.setCurrentDate( Integer.valueOf( DateEdit.getDate( 2 ) ) );

            // ���ԏ���擾
            DataRsvReserve rsvData = new DataRsvReserve();
            if ( !frm.getReserveNo().equals( "" ) )
            {
                rsvData.getData( frm.getSelHotelID(), frm.getReserveNo() );
                frm.setSelParkingCount( rsvData.getParkingCount() );
                frm.setSelHiRoofCount( rsvData.getParkingHiRoofCount() );
            }

            // �J�����_�[���擾
            setCalenderInfo( frm, frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelSeq(), frm.getSelRsvDate() );

            // �K�{�I�v�V�������擾
            frmOptSumImpList = rsvCmm.getOptionSubImp( frm.getSelHotelID(), frm.getSelPlanID() );
            frm.setFrmOptSubImpList( frmOptSumImpList );

            // �ʏ�I�v�V�������擾
            frmOptSub = rsvCmm.getOptionSub( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelRsvDate() );
            for( int i = 0 ; i < frmOptSub.getUnitPriceList().size() ; i++ )
            {
                optSubRemarksList.add( "" );
            }
            frm.setSelOptSubRemarksList( optSubRemarksList );

            // ���[�N�e�[�u������Ώۃf�[�^�擾
            logic.setFrm( frm );
            logic.getReserveWorkData_Back( frm.getWorkId() );
            frm = logic.getFrm();

            // �I������Ă���K�{�I�v�V�����������[�N�e�[�u������擾
            selOptImpSubIdList = logic.getSelOptImpSubIdList( frm.getWorkId() );
            frm.setSelOptionImpSubIdList( selOptImpSubIdList );

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

            // �s���{���E�s�撬�����擾
            frm = getPref( frm );

            frm.setLastMonth( DateEdit.addMonth( frm.getSelRsvDate(), -1 ) );
            frm.setNextMonth( DateEdit.addMonth( frm.getSelRsvDate(), 1 ) );
            frm.setSelCalYm( frm.getSelRsvDate() );

            // ���[�N�e�[�u������Ώۃf�[�^�폜
            logic.deleteRsvWork( frm.getWorkId() );
            logic.deleteRsvOptionWork( frm.getWorkId() );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvCheckIn.execBack() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvCheckIn.execBack() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * 
     * �s���{�����擾
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC getPref(FormReservePersonalInfoPC frm) throws Exception
    {

        ArrayList<Integer> prefIdList = new ArrayList<Integer>();
        ArrayList<String> prefNMList = new ArrayList<String>();
        ArrayList<Integer> jisCdList = new ArrayList<Integer>();
        ArrayList<String> jisNMList = new ArrayList<String>();
        ReserveCommon rsvCmm = new ReserveCommon();

        // �s���{�����X�g�擾
        prefIdList = rsvCmm.getPrefIdList();
        prefNMList = rsvCmm.getPrefNmList();

        // �s�撬�����X�g�擾
        jisCdList = rsvCmm.getJisCdList( frm.getSelPrefId() );
        jisNMList = rsvCmm.getJisNmList( frm.getSelPrefId() );

        // �t�H�[���ɃZ�b�g
        frm.setPrefIdList( prefIdList );
        frm.setPrefNmList( prefNMList );
        frm.setJisCdList( jisCdList );
        frm.setJisNmList( jisNMList );

        return(frm);
    }

    /**
     * 
     * �ݔ����擾
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReservePersonalInfoPC�I�u�W�F�N�g
     */
    private FormReservePersonalInfoPC getEquip(FormReservePersonalInfoPC frm) throws Exception
    {

        LogicReserveInitPC logic = new LogicReserveInitPC();
        DataRsvRoom drr = new DataRsvRoom();
        String roomRemarks = "";
        String roomPr = "";

        logic.setFrm( frm );

        if ( frm.getOfferKind() == 2 )
        {
            // ������
            drr.getData( frm.getSelHotelID(), frm.getSelSeq() );
            roomRemarks = drr.getRemarks();
            roomPr = drr.getRoomPr();
            logic.getEquip( frm.getSelHotelID(), frm.getSelSeq() );
            frm = logic.getFrm();
            frm.setRoomRemarks( roomRemarks );
            frm.setRoomPr( roomPr );

            return(frm);
        }

        // �v������
        roomRemarks = "";
        roomPr = "";
        logic.getEquipPlan( frm.getSelHotelID(), ReserveCommon.getRoomSeqList( frm.getSelHotelID(), frm.getSelPlanID() ) );

        frm.setRoomRemarks( roomRemarks );
        frm.setRoomPr( roomPr );

        return(frm);

    }

    /**
     * 
     * �J�����_�[���
     * 
     * @param frm
     * @param selHotelId
     * @param planId
     * @param seq
     * @throws Exception
     */
    private void setCalenderInfo(FormReservePersonalInfoPC frm, int selHotelId, int planId, int seq, int rsvDate) throws Exception
    {
        LogicOwnerRsvManageCalendar logicCalendar;
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        String year = "";
        String month = "";
        int checkDateST = 0;
        int checkDateED = 0;
        int dispCheckDateST = 0;
        int dispCheckDateED = 0;
        int curDate = 0;
        int curTime = 0;
        int minusDay = 0;
        int plusDay = 0;
        int checkDatePremiumST = 0;
        int checkDatePremiumED = 0;
        int checkDateFreeST = 0;
        int checkDateFreeED = 0;
        int checkDateFreeAddED = 0;
        int numchild = 0;
        ReserveCommon rsvCmm = new ReserveCommon();
        logicCalendar = new LogicOwnerRsvManageCalendar();

        try
        {
            // �q���l������(4���c�ƃz�e���͋����I��0)
            if ( rsvCmm.checkLoveHotelFlag( frm.getSelHotelID() ) == false )
            {
                numchild = frm.getSelNumChild();
            }
            // �J�����_�[���擾
            // �����擾
            Calendar calendar = Calendar.getInstance();
            year = Integer.toString( calendar.get( Calendar.YEAR ) );
            month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );
            year = String.valueOf( rsvDate ).substring( 0, 4 );
            month = String.valueOf( rsvDate ).substring( 4, 6 );
            if ( seq != 0 )
            {
                monthlyList = logicCalendar.getCalendarData( selHotelId, planId, seq, Integer.parseInt( year + month ) );
            }
            else
            {
                monthlyList = logicCalendar.getCalendarData( selHotelId, planId, Integer.parseInt( year + month ) );
            }

            curDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            curTime = Integer.parseInt( DateEdit.getTime( 1 ) );
            if ( curTime > frm.getRsvEndTime() )
            {
                plusDay++;
            }
            if ( curTime < frm.getRsvStartTime() )
            {
                minusDay++;
            }
            checkDateST = DateEdit.addDay( curDate, frm.getRsvEndDate() + plusDay );
            checkDateED = DateEdit.addDay( curDate, frm.getRsvStartDate() - minusDay );

            // �̔��J�n���Ԃ������Ă���ꍇ�͊J�n����ς���
            if ( checkDateST < frm.getSalesStartDay() )
            {
                dispCheckDateST = frm.getSalesStartDay();
            }
            else
            {
                dispCheckDateST = checkDateST;
            }
            // �̔��I�����Ԃ𒴂��Ă���ꍇ�͏I�������Z�b�g
            if ( checkDateED > frm.getSalesEndDay() )
            {
                dispCheckDateED = frm.getSalesEndDay();
            }
            else
            {
                dispCheckDateED = checkDateED;
            }

            frm.setRsvStartDayStr( String.format( "%1$04d�N%2$02d��%3$02d��", dispCheckDateST / 10000, dispCheckDateST % 10000 / 100, dispCheckDateST % 10000 % 100 ) );
            frm.setRsvEndDayStr( String.format( "%1$04d�N%2$02d��%3$02d��", dispCheckDateED / 10000, dispCheckDateED % 10000 / 100, dispCheckDateED % 10000 % 100 ) );

            checkDateFreeST = DateEdit.addDay( curDate, frm.getRsvEndDateFree() + plusDay );
            checkDateFreeED = DateEdit.addDay( curDate, frm.getRsvStartDateFree() - minusDay );
            checkDateFreeAddED = DateEdit.addDay( curDate, frm.getRsvStartDateFree() - minusDay + 1 );

            checkDatePremiumST = DateEdit.addDay( curDate, frm.getRsvEndDatePremium() + plusDay );
            checkDatePremiumED = DateEdit.addDay( curDate, frm.getRsvStartDatePremium() - minusDay );

            frm.setRsvPremiumEndDayStr( String.format( "%1$04d�N%2$02d��%3$02d��", checkDatePremiumED / 10000, checkDatePremiumED % 10000 / 100, checkDatePremiumED % 10000 % 100 ) );

            for( int i = 0 ; i <= monthlyList.size() - 1 ; i++ )
            {
                ArrayList<FormOwnerRsvManageCalendar> oneList = monthlyList.get( i );
                for( int j = 0 ; j <= oneList.size() - 1 ; j++ )
                {
                    FormOwnerRsvManageCalendar frmMC = oneList.get( j );
                    String reserveCharge = rsvCmm.getReserveCharge( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelNumAdult(), numchild, frmMC.getCalDate() );
                    frmMC.setReserveChargeFormat( reserveCharge );
                    if ( frmMC.getVacancyRoomNum() != 0 )
                    {
                        if ( (frmMC.getCalDate() >= checkDateST) && (frmMC.getCalDate() <= checkDateED) &&
                                (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay()) )
                        {
                            // frmMC.setRsvJotaiMark( ReserveCommon.RSV_ON_MARK );
                            // ����������{���ł���������m�F���Č���Ȃ��ꍇ�̓v���~�A���L�����t���O�����Ă�
                            if ( frmMC.getCalDate() < checkDateFreeST || frmMC.getCalDate() > checkDateFreeED )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                        }
                        else
                        {
                            // �L��������{���ł���������m�F���ĉ{���ł���ꍇ�̓v���~�A���L�����t���O�����Ă�
                            frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            if ( ((frmMC.getCalDate() >= checkDatePremiumST) && (frmMC.getCalDate() <= checkDatePremiumED) &&
                                    (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay())) )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                        }
                    }
                    else
                    {
                        if ( frm.getMode().equals( ReserveCommon.MODE_UPD ) && (frmMC.getCalDate() == frm.getOrgReserveDate()) )
                        {
                            // �ύX�̏ꍇ�A���̗\����́u���v�ɂ���B
                            frmMC.setRsvJotaiMark( ReserveCommon.RSV_ON_MARK );
                        }
                        else
                        {
                            frmMC.setRsvJotaiMark( ReserveCommon.RSV_OFF_MARK );
                            if ( frmMC.getSalesFlag() == 0 )
                            {
                                frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            }
                        }
                        if ( (frmMC.getCalDate() >= checkDateST) && (frmMC.getCalDate() <= checkDateED) &&
                                (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay()) )
                        {
                            // ����������{���ł���������m�F���Č���Ȃ��ꍇ�̓v���~�A���L�����t���O�����Ă�
                            if ( frmMC.getCalDate() < checkDateFreeST || frmMC.getCalDate() > checkDateFreeED )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                            if ( frmMC.getRsvJotaiFlg() == 1 && frmMC.getSalesFlag() == 0 )
                            {
                                frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            }
                        }
                        else
                        {
                            // �L��������{���ł���������m�F���ĉ{���ł���ꍇ�̓v���~�A���L�����t���O�����Ă�
                            if ( ((frmMC.getCalDate() >= checkDatePremiumST) && (frmMC.getCalDate() <= checkDatePremiumED) &&
                                    (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay())) )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                            else
                            {
                                // ���ԊO�\��
                                frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            }
                        }
                    }

                }
            }

            // �t�H�[���ɃZ�b�g
            frm.setMonthlyList( monthlyList );
            // �L���������̊J�n���A�I������ǉ�
            frm.setRsvPremiumStartDay( checkDatePremiumST );
            frm.setRsvPremiumEndDay( checkDatePremiumED );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReserveInitPC.setRsvManage() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setRsvManage] " + exception );
        }
    }

    /**
     * 
     * �\�񉼃f�[�^�擾
     * 
     * @param frm FormReservePersonalInfoPC�I�u�W�F�N�g
     * @return FormReserveSheetPC �I�u�W�F�N�g
     */
    private FormReserveSheetPC getRsvWorkData(FormReservePersonalInfoPC frm) throws Exception
    {
        FormReserveSheetPC frmSheet = new FormReserveSheetPC();
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        ReserveCommon rsvCmm = new ReserveCommon();

        // ���t�H�[���ɕK�v�Ȓl���Z�b�g
        // �z�e����{���̐ݒ�
        frm = rsvCmm.getHotelData( frm );

        // ���[�N�e�[�u������f�[�^���擾
        frm.getWorkId();
        logic.setFrmSheet( frmSheet );
        logic.getReserveWorkData( frm.getWorkId() );
        frmSheet = logic.getFrmSheet();
        frmSheet.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );
        frmSheet.setWorkId( frm.getWorkId() );

        return(frmSheet);
    }

}