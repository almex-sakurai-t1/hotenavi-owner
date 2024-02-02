package jp.happyhotel.action;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataRsvReserveBasic;
import jp.happyhotel.data.DataRsvRoom;
import jp.happyhotel.owner.FormOwnerRsvManageCalendar;
import jp.happyhotel.owner.LogicOwnerRsvManageCalendar;
import jp.happyhotel.reserve.FormReserveOptionSub;
import jp.happyhotel.reserve.FormReserveOptionSubImp;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveInitPC;
import jp.happyhotel.user.UserBasicInfo;

/**
 * 
 * �\����͉��(PC��) Action Class
 */

public class ActionReserveInitPC extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int hotelId = 0;
        int planId = 0;
        int seq = 0;
        int reserveDate = 0;
        int ymd = 0;
        int child = 0;
        int adult = 0;
        String reserveDateFormat = "";
        String rsvDate;
        String week;
        String strCarrierUrl = "";
        int currentDate = 0;
        String userId = "";
        String mailAddr = "";
        ArrayList<String> mailAddrList = new ArrayList<String>();
        String errMsg = "";
        boolean isLoginUser = false;
        DataRsvReserveBasic drrb = new DataRsvReserveBasic();

        ReserveCommon rsvCmm = new ReserveCommon();
        FormReservePersonalInfoPC frm = new FormReservePersonalInfoPC();
        ArrayList<FormReserveOptionSubImp> frmOptSubImpList = new ArrayList<FormReserveOptionSubImp>();
        FormReserveOptionSub frmOptSub = new FormReserveOptionSub();
        ArrayList<String> optSubRemarksList = new ArrayList<String>();

        String checkHotelId = "";
        String checkPlanId = "";
        String checkSeq = "";
        String checkRsvDate = "";

        try
        {
            // URL�p�����[�^��s���ɕύX���ꂽ�ꍇ�́A�G���[�y�[�W�֑J�ڂ�����
            checkHotelId = request.getParameter( "id" );
            checkPlanId = request.getParameter( "plan_id" );
            checkSeq = request.getParameter( "seq" );
            checkRsvDate = request.getParameter( "rsv_date" );

            // �p�����[�^�������ꂽ�ꍇ�̃`�F�b�N
            if ( (checkHotelId == null) || (checkPlanId == null) || (checkSeq == null) || (checkRsvDate == null) )
            {
                errMsg = Message.getMessage( "erro.30009", "�p�����[�^�Ȃ�" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // ���l�̃p�����[�^�ɁA���l�ȊO���w�肳�ꂽ�ꍇ�̃`�F�b�N
            try
            {
                // �p�����[�^�擾
                hotelId = Integer.parseInt( request.getParameter( "id" ) ); // �z�e��ID
                planId = Integer.parseInt( request.getParameter( "plan_id" ) ); // �v����ID
                seq = Integer.parseInt( request.getParameter( "seq" ) ); // �����ԍ�
                reserveDate = Integer.parseInt( request.getParameter( "rsv_date" ) ); // �\���
                adult = Integer.parseInt( request.getParameter( "adult" ) );
                if ( rsvCmm.checkLoveHotelFlag( hotelId ) == false )
                {
                    child = Integer.parseInt( request.getParameter( "child" ) );
                }
            }
            catch ( Exception exception )
            {
                errMsg = Message.getMessage( "erro.30009", "�p�����[�^�l�s��" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // ���O�C�����[�U�[���擾
            DataLoginInfo_M2 dataLoginInfo_M2;
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
                userId = dataLoginInfo_M2.getUserId();
            }

            if ( isLoginUser == false )
            {
                // ���O�C�����[�U�[�����݂��Ȃ��ꍇ�̓G���[
                errMsg = Message.getMessage( "erro.30004" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            UserBasicInfo userinfoUbi = new UserBasicInfo();
            userinfoUbi.getUserBasic( userId );
            // ���[���A�h���X���Z�b�g
            if ( userinfoUbi.getUserInfo().getMailAddr().equals( "" ) == false )
            {
                mailAddr = userinfoUbi.getUserInfo().getMailAddr();
                mailAddrList.add( userinfoUbi.getUserInfo().getMailAddr() );
            }
            if ( userinfoUbi.getUserInfo().getMailAddrMobile().equals( "" ) == false )
            {
                if ( mailAddr.equals( "" ) != false )
                {
                    mailAddr = userinfoUbi.getUserInfo().getMailAddrMobile();
                }
                mailAddrList.add( userinfoUbi.getUserInfo().getMailAddrMobile() );
            }

            // ���I�[�v�����ǂ������擾����
            drrb.getData( hotelId );

            // �p�����[�^�̐������`�F�b�N
            errMsg = rsvCmm.checkParam( hotelId, planId, seq, reserveDate, ReserveCommon.USER_KBN_USER, ReserveCommon.MODE_INS );
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
            errMsg = rsvCmm.checkAdultChildNum( hotelId, planId, adult, child );
            if ( errMsg.trim().length() != 0 )
            {
                // �`�F�b�NNG�̏ꍇ�A�G���[�y�[�W�֑J�ڂ���B
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // �f�[�^�`�F�b�N
            FormReserveSheetPC frmSheetPC = new FormReserveSheetPC();
            frmSheetPC.setSelHotelId( hotelId );
            frmSheetPC.setSelPlanId( planId );
            frmSheetPC.setRsvNo( "" );
            frmSheetPC.setSeq( seq );
            frmSheetPC.setMode( ReserveCommon.MODE_INS );
            frmSheetPC.setRsvDate( reserveDate );
            frmSheetPC.setUserId( userId );
            frmSheetPC = rsvCmm.chkDspMaster( frmSheetPC );
            if ( frmSheetPC.getErrMsg().trim().length() != 0 )
            {
                // �`�F�b�NNG�̏ꍇ�A�G���[�y�[�W�֑J�ڂ���B
                // request.setAttribute( "errMsg", frmSheetPC.getErrMsg() );
                // requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                // requestDispatcher.forward( request, response );
                // return;
            }

            // �t�H�[���ɃZ�b�g
            frm.setSelHotelID( hotelId );
            frm.setSelPlanID( planId );
            frm.setSelSeq( seq );
            frm.setSelRsvDate( reserveDate );
            frm.setOrgReserveDate( reserveDate );
            frm.setLoginUserId( userId );
            frm.setLoginMailAddr( mailAddr );
            frm.setLoginMailAddrList( mailAddrList );
            frm.setLoginTel( userinfoUbi.getUserInfo().getTel1() );
            frm.setPaymemberFlg( dataLoginInfo_M2.isPaymemberFlag() );

            // �\��\���`�F�b�N
            if ( reserveDate > 0 )
            {
                // ����������L������̋�ʂ����邽�߂Ƀv�����f�[�^�擾
                ReserveCommon rsvcomm = new ReserveCommon();
                frm = rsvcomm.getPlanData( frm );

                int curDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                int curTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                int checkDateST = DateEdit.addDay( curDate, (frm.getRsvEndDate()) );
                int checkDateED = DateEdit.addDay( curDate, (frm.getRsvStartDate()) );
                int checkDatePremiumED = DateEdit.addDay( curDate, (frm.getRsvStartDatePremium()) );
                int checkDatePremiumST = DateEdit.addDay( curDate, (frm.getRsvEndDatePremium()) );

                if ( (reserveDate > checkDateST && reserveDate < checkDateED &&
                        reserveDate >= frm.getSalesStartDay() && reserveDate <= frm.getSalesEndDay()) ||
                        (reserveDate == checkDateST && curTime <= frm.getRsvEndTime() &&
                                reserveDate >= frm.getSalesStartDay() && reserveDate <= frm.getSalesEndDay()) ||
                        (reserveDate == checkDateED && curTime >= frm.getRsvStartTime() &&
                                reserveDate >= frm.getSalesStartDay() && reserveDate <= frm.getSalesEndDay()) )
                {
                }
                else
                {
                    if ( ((reserveDate > checkDatePremiumST) && (reserveDate < checkDatePremiumED) &&
                            (reserveDate >= frm.getSalesStartDay()) && (reserveDate <= frm.getSalesEndDay())) ||
                            (reserveDate == checkDatePremiumST && curTime <= frm.getRsvEndTime()) ||
                            (reserveDate == checkDatePremiumED && curTime >= frm.getRsvStartTime()) )
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
                    }
                    // �G���[��񂪓����Ă���ꍇ�̂�
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

            // �ߋ��ɗ\��f�[�^�����݂���ΏZ���Ȃǂ̓Z�b�g����
            if ( frm.getZipCd3().equals( "" ) && frm.getZipCd4().equals( "" ) &&
                    frm.getSelPrefId() == 0 && frm.getSelJisCd() == 0 &&
                    frm.getAddress3().equals( "" ) && frm.getLastName().equals( "" ) &&
                    frm.getFirstName().equals( "" ) && frm.getLastNameKana().equals( "" ) &&
                    frm.getFirstNameKana().equals( "" ) && frm.getTel().equals( "" ) )
            {
                DataRsvReserve rsv = new DataRsvReserve();
                rsv.getDataByUserId( userId );
                if ( !rsv.getReserveNo().equals( "" ) )
                {
                    frm.setZipCd3( rsv.getZipCd().substring( 0, 3 ) );
                    frm.setZipCd4( rsv.getZipCd().substring( 3, rsv.getZipCd().length() ) );
                    frm.setSelPrefId( rsv.getPrefCode() );
                    frm.setSelJisCd( rsv.getJisCode() );
                    frm.setAddress3( rsv.getAddress3() );
                    frm.setLastName( rsv.getNameLast() );
                    frm.setFirstName( rsv.getNameFirst() );
                    frm.setLastNameKana( rsv.getNameLastKana() );
                    frm.setFirstNameKana( rsv.getNameFirstKana() );
                    frm.setTel( rsv.getTel1() );
                }
            }

            // ���t�̕\���`��
            ymd = Integer.parseInt( request.getParameter( "rsv_date" ) );
            if ( ymd != 0 )
            {
                week = DateEdit.getWeekName( ymd );
                rsvDate = String.valueOf( ymd );
                reserveDateFormat = rsvDate.substring( 0, 4 ) + "�N" + rsvDate.substring( 4, 6 ) + "��" + rsvDate.substring( 6, 8 ) + "���i" + week + "�j";
                frm.setReserveDateFormat( reserveDateFormat );
                frm.setReserveDateDay( ymd );
            }
            else
            {
                frm.setReserveDateFormat( "���t���I���i���L�̃J�����_�[�����t��I�����ĉ������j" );
            }

            // �z�e����{���擾
            frm = rsvCmm.getHotelData( frm );

            // �z�e���̒��ԏꗘ�p�敪�A���ԏꗘ�p�䐔���擾
            frm = rsvCmm.getParking( frm );
            frm.setParkingUsedKbnInit( 0 );

            // �v�����}�X�^���e������擾
            frm = rsvCmm.getPlanData( frm );
            if ( frm.getNumAdultList().size() > 0 )
            {
                frm.setSelNumAdult( 2 );
            }
            else
            {
                frm.setSelNumAdult( 1 );
            }
            frm.setSelNumChild( 0 );

            // �ʗ����}�X�^���`�F�b�N�C���J�n�A�I���������擾
            frm = rsvCmm.getCiCoTime( frm, adult, child, reserveDate );
            frm.setSelEstTimeArrival( -1 );

            // ��ʏ��ݒ�;
            frm.setMode( ReserveCommon.MODE_INS );

            // �s���{�����擾
            frm = getPref( frm );

            // �ݔ����
            frm = getEquip( frm );

            // �V�X�e�����t�E�����̎擾
            currentDate = Integer.valueOf( DateEdit.getDate( 2 ) );
            frm.setCurrentDate( currentDate );

            // ���ԏ���擾
            DataRsvReserve rsvData = new DataRsvReserve();
            if ( !frm.getReserveNo().equals( "" ) )
            {
                rsvData.getData( frm.getSelHotelID(), frm.getReserveNo() );
                frm.setSelParkingCount( rsvData.getParkingCount() );
                frm.setSelHiRoofCount( rsvData.getParkingHiRoofCount() );
            }

            // �K�{�I�v�V�������擾
            frmOptSubImpList = rsvCmm.getOptionSubImp( frm.getSelHotelID(), frm.getSelPlanID() );
            frm.setFrmOptSubImpList( frmOptSubImpList );
            ArrayList<Integer> selOptSubImpIdList = new ArrayList<Integer>();
            // 1�ڂ̃I�v�V������I���ς݂Ƃ���B
            for( int i = 0 ; i < frmOptSubImpList.size() ; i++ )
            {
                for( int j = 0 ; j < frmOptSubImpList.get( i ).getOptSubIdList().size() ; j++ )
                {
                    if ( j == 0 )
                    {
                        selOptSubImpIdList.add( frmOptSubImpList.get( i ).getOptSubIdList().get( j ) );
                        break;
                    }
                }
                frm.setSelOptionImpSubIdList( selOptSubImpIdList );
            }

            // �ʏ�I�v�V�������擾
            frmOptSub = rsvCmm.getOptionSub( frm.getSelHotelID(), frm.getSelPlanID(), ymd );
            frm.setFrmOptSub( frmOptSub );
            for( int i = 0 ; i < frmOptSub.getUnitPriceList().size() ; i++ )
            {
                optSubRemarksList.add( "" );
            }
            frm.setSelOptSubRemarksList( optSubRemarksList );

            // �J�����_�[���擾
            setCalenderInfo( frm, hotelId, planId, seq, ymd, adult, child );
            frm.setLastMonth( DateEdit.addMonth( ymd, -1 ) );
            frm.setNextMonth( DateEdit.addMonth( ymd, 1 ) );
            frm.setSelCalYm( ymd );

            frm.setLoginUserKbn( ReserveCommon.USER_KBN_USER );

            // �\����̃`�F�b�N
            errMsg = rsvCmm.checkReserveDuplicate( userId, Integer.parseInt( checkRsvDate ) );
            frm.setErrMsg( errMsg );

            request.setAttribute( "dsp", frm );
            request.setAttribute( "err", "" );

            strCarrierUrl = "/reserve/reserve_personal_info.jsp";
            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + strCarrierUrl );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReserveInitPC.execute() ][hotelId = "
                    + hotelId + " ,planId = " + planId + " ,reserveDate = " + reserveDate + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReserveInitPC.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
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
        ReserveCommon rsvCmm = new ReserveCommon();

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

        if ( frm.getOfferKind() == ReserveCommon.OFFER_KIND_ROOM )
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
     * @param selHotelId �z�e��ID
     * @param planId �v�����ԍ�
     * @param seq �����ԍ�
     * @param adult ��l�l��
     * @param child �q���l��
     * @throws Exception
     */
    private void setCalenderInfo(FormReservePersonalInfoPC frm, int selHotelId, int planId, int seq, int rsvDate, int adult, int child) throws Exception
    {
        LogicOwnerRsvManageCalendar logicCalendar;
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        String year = "";
        String month = "";
        int checkDateST = 0;
        int checkDateED = 0;
        int dispCheckDateST = 0;
        int dispCheckDateED = 0;
        int checkDatePremiumST = 0;
        int checkDatePremiumED = 0;
        int checkDateFreeST = 0;
        int checkDateFreeED = 0;
        int checkDateFreeAddED = 0;
        int curDate = 0;
        int curTime = 0;
        int minusDay = 0;
        int plusDay = 0;
        ReserveCommon rsvCmm = new ReserveCommon();

        logicCalendar = new LogicOwnerRsvManageCalendar();

        try
        {

            // �J�����_�[���擾
            // �����擾
            Calendar calendar = Calendar.getInstance();
            year = Integer.toString( calendar.get( Calendar.YEAR ) );
            month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );

            if ( rsvDate != 0 )
            {
                year = String.valueOf( rsvDate ).substring( 0, 4 );
                month = String.valueOf( rsvDate ).substring( 4, 6 );
            }
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

            frm.setRsvStartDayInt( dispCheckDateST );
            frm.setRsvEndDayInt( dispCheckDateED );

            checkDateFreeST = DateEdit.addDay( curDate, frm.getRsvEndDateFree() + plusDay );
            checkDateFreeED = DateEdit.addDay( curDate, frm.getRsvStartDateFree() - minusDay );
            checkDateFreeAddED = DateEdit.addDay( curDate, frm.getRsvStartDateFree() - minusDay + 1 );

            checkDatePremiumST = DateEdit.addDay( curDate, frm.getRsvEndDatePremium() + plusDay );
            checkDatePremiumED = DateEdit.addDay( curDate, frm.getRsvStartDatePremium() - minusDay );

            frm.setRsvPremiumEndDayStr( String.format( "%1$04d�N%2$02d��%3$02d��", checkDatePremiumED / 10000, checkDatePremiumED % 10000 / 100, checkDatePremiumED % 10000 % 100 ) );

            if ( checkDateFreeED == checkDatePremiumED )
            {
                // ��������ƗL������̏I�����������ꍇ�́A�J�n�\�����Ȃ��ɂ���
                frm.setRsvPremiumStartDayStr( "" );
            }
            else
            {
                frm.setRsvPremiumStartDayStr( String.format( "%1$04d�N%2$02d��%3$02d��", checkDateFreeAddED / 10000, checkDateFreeAddED % 10000 / 100, checkDateFreeAddED % 10000 % 100 ) );
            }

            for( int i = 0 ; i <= monthlyList.size() - 1 ; i++ )
            {
                ArrayList<FormOwnerRsvManageCalendar> oneList = monthlyList.get( i );
                for( int j = 0 ; j <= oneList.size() - 1 ; j++ )
                {
                    FormOwnerRsvManageCalendar frmMC = oneList.get( j );
                    String reserveCharge = rsvCmm.getReserveCharge( selHotelId, planId, adult, child, frmMC.getCalDate() );
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
                            frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
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

}
