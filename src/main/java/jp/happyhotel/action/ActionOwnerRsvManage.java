package jp.happyhotel.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.data.DataRsvRoom;
import jp.happyhotel.owner.FormOwnerRsvManage;
import jp.happyhotel.owner.FormOwnerRsvManageCalendar;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvPlanCharge;
import jp.happyhotel.owner.FormOwnerRsvPlanManage;
import jp.happyhotel.owner.FormOwnerRsvRoomManage;
import jp.happyhotel.owner.LogicOwnerRsvManage;
import jp.happyhotel.owner.LogicOwnerRsvManageCalendar;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvPlanCharge;
import jp.happyhotel.owner.LogicOwnerRsvPlanManage;
import jp.happyhotel.owner.LogicOwnerRsvRoomManage;

/**
 * 
 * �\��Ǘ����
 */
public class ActionOwnerRsvManage extends BaseAction
{

    private RequestDispatcher   requestDispatcher = null;
    private static final String MODE_HOTEL        = "hotelSales";
    private static final String MODE_CALENDAR     = "calendarSales";
    private static final String MODE_ROOM         = "roomSales";
    private static final String MODE_PLAN         = "planSales";
    private static final String MODE_PREMONTH     = "preMonth";
    private static final String MODE_NEXTMONTH    = "nextMonth";
    // �����c���X�e�[�^�X
    private static final int    STATUS_EMP        = 1;              // ��
    private static final int    STATUS_STOP       = 3;              // ����~��

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvManage frmManage;
        FormOwnerBkoMenu frmMenu;
        LogicOwnerRsvManage logic;
        int paramHotelID = 0;
        int paramSeq = 0;
        int paramSalesFlg = 0;
        int paramPlanId = 0;
        String paramBtnMode = "";
        int paramUserId = 0;
        String paramTargetYM = "";
        int hotelId = 0;
        int paramYYYY = 0;
        int paramMM = 0;
        String paramTopMonth = "";
        String targetYYYYMM = "";
        Calendar orgCal;
        String errMsg = "";
        int selPlanId = -1;
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        String hotenaviId = "";
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            frmManage = new FormOwnerRsvManage();
            frmMenu = new FormOwnerBkoMenu();
            logic = new LogicOwnerRsvManage();
            // ��ʂ̒l���擾
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            logicMenu.setFrm( new FormOwnerBkoMenu() );
            disptype = logicMenu.getUserAuth( hotenaviId, paramUserId );
            adminflag = logicMenu.getAdminFlg( hotenaviId, paramUserId );
            imediaflag = OwnerRsvCommon.getImediaFlag( hotenaviId, paramUserId );

            if ( (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false && disptype != OwnerRsvCommon.USER_AUTH_OWNER && disptype != OwnerRsvCommon.USER_AUTH_DEMO && disptype != OwnerRsvCommon.USER_AUTH_CALLCENTER )
            {
                // �����̂Ȃ����[�U�̓G���[�y�[�W��\������
                errMsg = Message.getMessage( "erro.30001", "�y�[�W���{�����錠��" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            // �I�����ꂽ�v����ID���Z�b�g
            if ( (request.getParameter( "selPlanId" ) != null) && (request.getParameter( "selPlanId" ).toString().length() != 0) )
            {
                selPlanId = Integer.parseInt( request.getParameter( "selPlanId" ).toString() );
            }

            paramBtnMode = request.getParameter( "mode" );
            paramSalesFlg = Integer.parseInt( request.getParameter( "saleFlg" ) );

            if ( (paramBtnMode.equals( MODE_HOTEL ) || paramBtnMode.equals( MODE_CALENDAR ) || paramBtnMode.equals( MODE_ROOM ) || paramBtnMode.equals( MODE_PLAN )) &&
                    disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
            {
                // �����̂Ȃ����[�U�̓G���[�y�[�W��\������
                errMsg = Message.getMessage( "erro.30001", "�X�e�[�^�X���X�V���錠��" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( paramBtnMode.equals( MODE_HOTEL ) )
            {
                // ���z�e���̔̔��t���O�X�V
                paramTargetYM = request.getParameter( "currentYM" );
                updHotelSalesFlg( logic, paramHotelID, paramSalesFlg, paramUserId );
                // �z�e���C������
                OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                        paramUserId, OwnerRsvCommon.ADJUST_EDIT_ID_RSV, paramSalesFlg == 0 ? 1 : 0,
                        paramSalesFlg == 0 ? OwnerRsvCommon.ADJUST_MEMO_RSV_START : OwnerRsvCommon.ADJUST_MEMO_RSV_STOP );
            }
            else if ( paramBtnMode.equals( MODE_CALENDAR ) )
            {
                // ���J�����_�[���̔̔��t���O�X�V
                paramTopMonth = request.getParameter( "topMonth" );
                paramTargetYM = request.getParameter( "currentYM" );

                // �v�������I������Ă��Ȃ�
                if ( selPlanId == -1 )
                {
                    updCalendarSalesFlg( logic, paramHotelID, Integer.parseInt( paramTargetYM ), paramSalesFlg, paramUserId );
                }
                else
                {
                    updChargeMode( logic, paramHotelID, Integer.parseInt( paramTargetYM ), selPlanId, paramSalesFlg, paramUserId );
                }
                // �z�e���C������
                OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                        paramUserId, OwnerRsvCommon.ADJUST_EDIT_ID_RSV_DAY,
                        Integer.parseInt( paramTargetYM ) * 10 + (paramSalesFlg == 0 ? 1 : 0),
                        paramSalesFlg == 0 ? OwnerRsvCommon.ADJUST_MEMO_RSV_START_DAY : OwnerRsvCommon.ADJUST_MEMO_RSV_STOP_DAY );
                paramTargetYM = paramTopMonth.substring( 0, 6 );
            }
            else if ( paramBtnMode.equals( MODE_ROOM ) )
            {
                // �������󋵂̔̔��t���O�X�V
                paramSeq = Integer.parseInt( request.getParameter( "seq" ) );
                paramTargetYM = request.getParameter( "currentYM" );
                updRoomSalesFlg( logic, paramHotelID, paramSeq, paramSalesFlg, paramUserId );
            }
            else if ( paramBtnMode.equals( MODE_PLAN ) )
            {
                // ���v�����󋵂̔̔��t���O�X�V
                paramPlanId = Integer.parseInt( request.getParameter( "planId" ) );
                paramTargetYM = request.getParameter( "currentYM" );
                updPlanSalesFlg( logic, paramHotelID, paramPlanId, paramSalesFlg, paramUserId );
                // �z�e���C������
                OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                        paramUserId, OwnerRsvCommon.ADJUST_EDIT_ID_RSV_PLAN,
                        paramPlanId * 10 + (paramSalesFlg == 0 ? 1 : 0),
                        paramSalesFlg == 0 ? OwnerRsvCommon.ADJUST_MEMO_RSV_START_PLAN : OwnerRsvCommon.ADJUST_MEMO_RSV_STOP_PLAN );
            }
            else if ( paramBtnMode.equals( MODE_PREMONTH ) )
            {
                // ���O���{�^��
                paramTargetYM = request.getParameter( "currentYM" );
                paramYYYY = Integer.parseInt( paramTargetYM.substring( 0, 4 ) );
                paramMM = Integer.parseInt( paramTargetYM.substring( 4 ) );
                orgCal = Calendar.getInstance();
                orgCal.set( paramYYYY, paramMM - 1, 1 );

                // 2�����O�擾
                orgCal.add( Calendar.MONTH, -2 );
                targetYYYYMM = new SimpleDateFormat( "yyyyMM" ).format( orgCal.getTime() );
                paramTargetYM = targetYYYYMM;
            }
            else if ( paramBtnMode.equals( MODE_NEXTMONTH ) )
            {
                // �������{�^��
                paramTargetYM = request.getParameter( "currentYM" );
                paramYYYY = Integer.parseInt( paramTargetYM.substring( 0, 4 ) );
                paramMM = Integer.parseInt( paramTargetYM.substring( 4 ) );
                orgCal = Calendar.getInstance();
                orgCal.set( paramYYYY, paramMM - 1, 1 );

                // 2�����O�擾
                orgCal.add( Calendar.MONTH, 2 );
                targetYYYYMM = new SimpleDateFormat( "yyyyMM" ).format( orgCal.getTime() );
                paramTargetYM = targetYYYYMM;
            }

            // ���j���[�A�{�ݏ��̐ݒ�
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, 0, request.getCookies() );
            setPage( paramHotelID, selPlanId, paramUserId, frmManage, paramTargetYM );

            // ��ʑJ��
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_Manage", frmManage );
            request.setAttribute( "selPlanId", selPlanId );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvManage.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvManage.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }

    /**
     * �J�����_�[�̔̔��t���O�X�V
     * 
     * @param logic LogicOwnerRsvManage�I�u�W�F�N�g
     * @param hotelId �z�e��ID
     * @param calDate �Ώۓ��t
     * @param salesFlg �̔��t���O
     * @param userId ���[�U�[ID
     * @return �Ȃ�
     */
    private void updCalendarSalesFlg(LogicOwnerRsvManage logic, int hotelId, int calDate, int salesFlg, int userId) throws Exception
    {
        int newSalesFlg = 0;
        int status = 0;
        int condStatus = 0;

        try
        {
            // �̔��t���O�̍X�V
            if ( salesFlg == 0 )
            {
                newSalesFlg = 1;
            }

            // ���݂���ꍇ�͍X�V
            logic.updCalendarSalesFlg( hotelId, calDate, newSalesFlg, userId );

            // �����c���f�[�^�̍X�V
            if ( salesFlg == 1 )
            {
                // ��~�N���b�N��
                status = STATUS_STOP;
                condStatus = STATUS_EMP;
            }
            else
            {
                // �J�n�N���b�N
                status = STATUS_EMP;
                condStatus = STATUS_STOP;
            }
            logic.updRoomRemainder( hotelId, calDate, status, condStatus );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.updCalendarSalesFlg() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.updCalendarSalesFlg()] " + exception );
        }
    }

    /**
     * �����󋵂̔̔��t���O�X�V
     * 
     * @param logic LogicOwnerRsvManage�I�u�W�F�N�g
     * @para, hotelId �z�e��ID
     * @param seq �Ǘ��ԍ�
     * @param salesFlg �̔��t���O
     * @@aram userId ���[�U�[ID
     * @return �Ȃ�
     */
    private void updRoomSalesFlg(LogicOwnerRsvManage logic, int hotelId, int seq, int salesFlg, int userId) throws Exception
    {
        boolean isExists = false;
        int newSalesFlg = 0;
        DataRsvRoom rsvData = new DataRsvRoom();

        try
        {
            // �̔��t���O�̍X�V
            if ( salesFlg == 0 )
            {
                newSalesFlg = 1;
            }

            // �\�񕔉��}�X�^�����݂��邩
            isExists = rsvData.getData( hotelId, seq );
            if ( isExists == true )
            {
                // ���݂���ꍇ�͍X�V
                logic.updRoomSalesFlg( hotelId, seq, newSalesFlg, userId );
            }
            else
            {
                // ���݂��Ȃ��ꍇ�͒ǉ�
                logic.execInsRsvRoom( hotelId, seq, newSalesFlg, userId );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.updRoomSalesFlg() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.updRoomSalesFlg()] " + exception );
        }
    }

    /**
     * �v�����󋵂̔̔��t���O�X�V
     * 
     * @param logic LogicOwnerRsvPlanManage�I�u�W�F�N�g
     * @para, hotelId �z�e��ID
     * @param planId �v����ID
     * @param salesFlg �̔��t���O
     * @@aram userId ���[�U�[ID
     * @return �Ȃ�
     */
    private void updPlanSalesFlg(LogicOwnerRsvManage logic, int hotelId, int planId, int salesFlg, int userId) throws Exception
    {
        int newSalesFlg = 0;

        try
        {
            // �̔��t���O�̍X�V
            if ( salesFlg == 0 )
            {
                newSalesFlg = 1;
            }
            logic.updPlanSalesFlg( hotelId, planId, newSalesFlg, userId );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.updRoomSalesFlg() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.updRoomSalesFlg()] " + exception );
        }
    }

    /**
     * �z�e���̔̔��t���O�X�V
     * 
     * @param logic LogicOwnerRsvPlanManage�I�u�W�F�N�g
     * @para, hotelId �z�e��ID
     * @param salesFlg �̔��t���O
     * @@aram userId ���[�U�[ID
     * @return �Ȃ�
     */
    private void updHotelSalesFlg(LogicOwnerRsvManage logic, int hotelId, int salesFlg, int userId) throws Exception
    {
        int newSalesFlg = 0;

        try
        {
            // �̔��t���O�̍X�V
            if ( salesFlg == 0 )
            {
                newSalesFlg = 1;
            }
            logic.updHotelSalesFlg( hotelId, newSalesFlg, userId );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.updHotelSalesFlg() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.updHotelSalesFlg()] " + exception );
        }
    }

    /**
     * 
     * ��ʕ\��
     */
    private void setPage(int hotelId, int planId, int userId, FormOwnerRsvManage frm, String targetYM) throws Exception
    {
        int year = 0;
        int month = 0;
        String targetYYYYMM = "";
        FormOwnerRsvRoomManage frmRoom;
        FormOwnerRsvPlanManage frmPlan;
        LogicOwnerRsvManage logicManage;
        LogicOwnerRsvRoomManage logicRoom;
        LogicOwnerRsvPlanManage logicPlan;
        LogicOwnerRsvManageCalendar logicCalendar;
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList2 = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();

        frmRoom = new FormOwnerRsvRoomManage();
        frmPlan = new FormOwnerRsvPlanManage();
        logicManage = new LogicOwnerRsvManage();
        logicRoom = new LogicOwnerRsvRoomManage();
        logicPlan = new LogicOwnerRsvPlanManage();
        logicCalendar = new LogicOwnerRsvManageCalendar();

        try
        {
            // �z�e�����I����
            if ( hotelId == 0 )
            {
                frm.setSelHotelErrMsg( Message.getMessage( "warn.30010" ) );
                return;
            }

            // ��1�ڂ̃J�����_�[���擾
            if ( planId == -1 )
            {
                monthlyList = logicCalendar.getCalendarData( hotelId, planId, Integer.parseInt( targetYM ) );
            }
            // �v����ID�͑��Ŏg���Ă��郍�W�b�N�Ƃ͕ʂ̂��̂��擾����i�\��Ǘ������Ŏg�p���邽�߁j
            else
            {
                monthlyList = logicCalendar.getPlanCalendarData( hotelId, planId, Integer.parseInt( targetYM ) );
            }

            // ��2�ڂ̃J�����_�[���擾
            // �����擾
            Calendar calendar = Calendar.getInstance();
            calendar = Calendar.getInstance();
            year = Integer.parseInt( targetYM.toString().substring( 0, 4 ) );
            month = Integer.parseInt( targetYM.toString().substring( 4 ) );
            calendar.set( year, month - 1, 1 );
            calendar.add( Calendar.MONTH, 1 );
            targetYYYYMM = new SimpleDateFormat( "yyyyMM" ).format( calendar.getTime() );

            if ( planId == -1 )
            {
                monthlyList2 = logicCalendar.getCalendarData( hotelId, planId, Integer.parseInt( targetYYYYMM ) );
            }
            // �v����ID�������Ă����瑼�Ŏg���Ă��郍�W�b�N�Ƃ͕ʂ̂��̂��擾����i�\��Ǘ������Ŏg�p���邽�߁j
            else
            {
                monthlyList2 = logicCalendar.getPlanCalendarData( hotelId, planId, Integer.parseInt( targetYYYYMM ) );
            }

            // ���z�e�����擾
            frm.setSelHotelID( hotelId );
            logicManage.setFrm( frm );
            logicManage.getRsvManage();

            // ���v�������擾
            frmPlan.setSelHotelId( hotelId );
            frmPlan.setUserId( userId );
            logicPlan.setFrm( frmPlan );
            // ���f�ڂ͕\�����Ȃ��悤�ɕύX
            logicPlan.getPlan( 1 );

            // ���������擾
            frmRoom.setSelHotelID( hotelId );
            frmRoom.setUserId( userId );
            logicRoom.setFrm( frmRoom );
            logicRoom.getRoomData();

            // �t�H�[���ɃZ�b�g
            frm.setNowTopMonth( Integer.parseInt( targetYM ) );
            frm.setMonthlyList( monthlyList );
            frm.setMonthlyList2( monthlyList2 );
            frm.setFrmPlan( frmPlan );
            frm.setFrmRoom( logicRoom.getFrm() );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvManage.setPage() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvManage.setPage()]] " + exception );
        }
    }

    /**
     * �J�����_�[�̔̔��t���O�X�V
     * 
     * @param logic LogicOwnerRsvManage�I�u�W�F�N�g
     * @param hotelId �z�e��ID
     * @param calDate �Ώۓ��t
     * @param planId �\��ԍ�
     * @param chargeMode �����ԍ�
     * @param userId ���[�U�[ID
     * @return �Ȃ�
     */
    private void updChargeMode(LogicOwnerRsvManage logic, int hotelId, int calDate, int planId, int chargeMode, int userId) throws Exception
    {
        int newChargeMode = 0;
        int status = 0;
        int condStatus = 0;
        FormOwnerRsvPlanCharge frm;
        LogicOwnerRsvPlanCharge planCharge;

        try
        {
            if ( chargeMode == 1 )
            {
                frm = new FormOwnerRsvPlanCharge();
                frm.setSelHotelId( hotelId );
                frm.setSelPlanId( planId );
                planCharge = new LogicOwnerRsvPlanCharge();
                planCharge.setFrm( frm );
                // �Ή����闿�����[�h���擾����
                newChargeMode = planCharge.getChargeMode( hotelId, planId, calDate );
            }

            // ���݂���ꍇ�͍X�V
            logic.updChargeMode( hotelId, calDate, planId, newChargeMode, userId );

            // // �����c���f�[�^�̍X�V
            // if ( chargeMode == 0 )
            // {
            // // ��~�N���b�N��
            // status = STATUS_STOP;
            // condStatus = STATUS_EMP;
            // }
            // else
            // {
            // // �J�n�N���b�N
            // status = STATUS_EMP;
            // condStatus = STATUS_STOP;
            // }
            // logic.updRoomRemainder( hotelId, calDate, status, condStatus );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.updCalendarSalesFlg() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.updCalendarSalesFlg()] " + exception );
        }
    }
}
