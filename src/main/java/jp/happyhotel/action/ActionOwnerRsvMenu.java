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
import jp.happyhotel.owner.FormOwnerRsvChargeMode;
import jp.happyhotel.owner.FormOwnerRsvEquipManage;
import jp.happyhotel.owner.FormOwnerRsvHotelBasic;
import jp.happyhotel.owner.FormOwnerRsvManage;
import jp.happyhotel.owner.FormOwnerRsvManageCalendar;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvOptionManage;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeCalendar;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeCalendarSub;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeManage;
import jp.happyhotel.owner.FormOwnerRsvPlanManage;
import jp.happyhotel.owner.FormOwnerRsvReserveBasic;
import jp.happyhotel.owner.FormOwnerRsvRoomManage;
import jp.happyhotel.owner.LogicOwnerRsvChargeMode;
import jp.happyhotel.owner.LogicOwnerRsvEquipManage;
import jp.happyhotel.owner.LogicOwnerRsvHotelBasic;
import jp.happyhotel.owner.LogicOwnerRsvManage;
import jp.happyhotel.owner.LogicOwnerRsvManageCalendar;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvOptionManage;
import jp.happyhotel.owner.LogicOwnerRsvPlanChargeCalendar;
import jp.happyhotel.owner.LogicOwnerRsvPlanChargeManage;
import jp.happyhotel.owner.LogicOwnerRsvPlanManage;
import jp.happyhotel.owner.LogicOwnerRsvReserveBasic;
import jp.happyhotel.owner.LogicOwnerRsvRoomManage;

/**
 * 
 * �I�[�i�[���j���[���
 */
public class ActionOwnerRsvMenu extends BaseAction
{

    private RequestDispatcher requestDispatcher   = null;
    private static final int  PARKING_SEL         = 1;   // ���ԏ�L���f�t�H���g
    private static final int  PUBLISHING_NON_DISP = 1;   // ���f�ڔ�\��
    private static final int  PUBLISHING_DISP     = 0;   // ���f�ڕ\��

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerRsvHotelBasic frmHotelBasic;
        FormOwnerRsvReserveBasic frmReserveBasic;
        FormOwnerRsvRoomManage frmRoomManage;
        FormOwnerRsvChargeMode frmChargeMode;
        FormOwnerRsvEquipManage frmEquipManage;
        FormOwnerRsvPlanChargeManage frmPlnChargeManage;
        FormOwnerRsvPlanManage frmPlanManage;
        FormOwnerRsvManage frmManage;
        FormOwnerRsvPlanChargeCalendar frmCalendar;
        FormOwnerRsvOptionManage frmOptionManage;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        int selHotelID = 0;
        int paramModeFlg = 0;
        int paramUserID = 0;
        String errMsg = "";
        String hotenaviId = "";
        int paramPlanId = -1;
        int imediaflag = 0;
        int adminflag = 0;
        boolean dispAllFlag = false;

        try
        {
            // �����̎擾
            if ( (request.getParameter( "modeFlg" ) != null) && (request.getParameter( "modeFlg" ).toString().length() != 0) )
            {
                paramModeFlg = Integer.parseInt( request.getParameter( "modeFlg" ).toString() );
            }

            // �I�����ꂽ�z�e��ID�̎擾
            if ( (request.getParameter( "selHotelIDValue" ) != null) && (request.getParameter( "selHotelIDValue" ).trim().length() != 0) )
            {
                selHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            if ( selHotelID == 0 )
            {
                paramModeFlg = 0;
            }
            // �I�����ꂽ�v����ID���Z�b�g
            if ( (request.getParameter( "selPlanId" ) != null) && (request.getParameter( "selPlanId" ).toString().length() != 0) )
            {
                paramPlanId = Integer.parseInt( request.getParameter( "selPlanId" ).toString() );
            }

            // ���O�C�����[�UID�̎擾
            paramUserID = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );

            // ���O�C�����[�U�ƒS���z�e���̃`�F�b�N
            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            if ( (selHotelID != 0) && (OwnerRsvCommon.checkHotelID( hotenaviId, paramUserID, selHotelID ) == false) )
            {
                // �Ǘ��O�̃z�e���̓��O�C����ʂ֑J��
                response.sendRedirect( "../../owner/index.jsp" );
                return;
            }

            imediaflag = OwnerRsvCommon.getImediaFlag( hotenaviId, paramUserID );
            logicMenu.setFrm( new FormOwnerBkoMenu() );
            adminflag = logicMenu.getAdminFlg( hotenaviId, paramUserID );
            if ( imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" ) )
            {
                dispAllFlag = true;
            }

            // TOP�y�[�W�ݒ�
            frmMenu = new FormOwnerBkoMenu();
            OwnerRsvCommon.setMenu( frmMenu, selHotelID, paramModeFlg, request.getCookies() );
            frmMenu.setUserId( paramUserID );
            request.setAttribute( "FORM_Menu", frmMenu );

            // �z�e����1�������Ȃ��ꍇ�́A�I���z�e��ID�Ƃ���
            if ( frmMenu.getHotelIDList().size() == 1 )
            {
                selHotelID = frmMenu.getHotelIDList().get( 0 );
                frmMenu.setSelHotelID( selHotelID );
            }

            // ��ʑJ��
            switch( paramModeFlg )
            {
                case 0:
                    // �\��Ǘ�TOP
                    frmManage = new FormOwnerRsvManage();
                    frmManage.setSelHotelID( selHotelID );
                    frmManage.setUserId( paramUserID );
                    setRsvManage( frmManage, selHotelID, paramUserID, paramPlanId, dispAllFlag );
                    request.setAttribute( "FORM_Manage", frmManage );
                    request.setAttribute( "selPlanId", paramPlanId );
                    break;
                case 1:
                    // �{�݊�{���ݒ�
                    frmHotelBasic = new FormOwnerRsvHotelBasic();
                    frmHotelBasic.setSelHotelID( selHotelID );
                    frmHotelBasic.setUserID( paramUserID );
                    setHotelBasic( frmHotelBasic );
                    request.setAttribute( "FORM_HotelBasic", frmHotelBasic );
                    break;

                case 2:
                    // �\���{���ݒ�
                    frmReserveBasic = new FormOwnerRsvReserveBasic();
                    frmReserveBasic.setSelHotelID( selHotelID );
                    frmReserveBasic.setUserID( paramUserID );
                    setReservBasic( frmReserveBasic );
                    request.setAttribute( "FORM_ReserveBasic", frmReserveBasic );
                    break;

                case 3:
                    // �v�����ݒ�
                    frmPlanManage = new FormOwnerRsvPlanManage();
                    frmPlanManage.setSelHotelId( selHotelID );
                    setPlanManage( frmPlanManage );
                    request.setAttribute( "FORM_PlanManage", frmPlanManage );
                    break;

                case 4:
                    // �������[�h�ݒ�
                    frmChargeMode = new FormOwnerRsvChargeMode();
                    frmChargeMode.setSelHotelID( selHotelID );
                    frmChargeMode.setUserId( paramUserID );
                    setChargeMode( frmChargeMode );
                    request.setAttribute( "FORM_ChargeMode", frmChargeMode );
                    break;

                case 5:
                    // �v�����ʗ��ݒ�
                    frmPlnChargeManage = new FormOwnerRsvPlanChargeManage();
                    frmPlnChargeManage.setSelHotelId( selHotelID );
                    frmPlnChargeManage.setUserId( paramUserID );
                    setPlanChargeManage( frmPlnChargeManage );
                    request.setAttribute( "FORM_ChargeManage", frmPlnChargeManage );
                    break;
                case 7:
                    // �������Ǘ�
                    frmRoomManage = new FormOwnerRsvRoomManage();
                    frmRoomManage.setSelHotelID( selHotelID );
                    frmRoomManage.setUserId( paramUserID );
                    setRoomManage( frmRoomManage );
                    request.setAttribute( "FORM_RoomManage", frmRoomManage );
                    break;

                case 8:
                    // �ݔ����Ǘ�
                    frmEquipManage = new FormOwnerRsvEquipManage();
                    frmEquipManage.setSelHotelID( selHotelID );
                    frmEquipManage.setUserId( paramUserID );
                    setEquip( frmEquipManage );
                    request.setAttribute( "FORM_EquipManage", frmEquipManage );
                    break;

                case 9:
                    // �I�v�V�����ݒ�
                    frmOptionManage = new FormOwnerRsvOptionManage();
                    frmOptionManage.setSelHotelId( selHotelID );
                    frmOptionManage.setUserId( paramUserID );
                    setOption( frmOptionManage );
                    request.setAttribute( "FORM_OptionManage", frmOptionManage );
                    break;

                case 10:
                    // �v�����ʃJ�����_�[�Ǘ�
                    frmCalendar = new FormOwnerRsvPlanChargeCalendar();
                    frmCalendar.setSelHotelId( selHotelID );
                    frmCalendar.setUserId( paramUserID );
                    setPlanChargeCalendar( frmCalendar, imediaflag );
                    request.setAttribute( "FORM_Calendar", frmCalendar );
                    break;

                case 100:
                    // �{�����X�Ǘ�
                    frmCalendar = new FormOwnerRsvPlanChargeCalendar();
                    frmCalendar.setSelHotelId( selHotelID );
                    frmCalendar.setUserId( paramUserID );
                    setPlanChargeCalendar( frmCalendar, imediaflag );
                    request.setAttribute( "FORM_Calendar", frmCalendar );
                    break;

                default:
                    // TOP�֖߂�
                    frmManage = new FormOwnerRsvManage();
                    frmManage.setSelHotelID( selHotelID );
                    frmManage.setUserId( paramUserID );
                    setRsvManage( frmManage, selHotelID, paramUserID, paramPlanId, dispAllFlag );
                    paramModeFlg = 0;
                    frmMenu.setModeFlg( paramModeFlg );
                    request.setAttribute( "FORM_Manage", frmManage );
                    request.setAttribute( "selPlanId", paramPlanId );
                    break;
            }

            // TOP�y�[�W�ݒ� - �I���z�e��ID�̐ݒ�
            if ( frmMenu.getSelHotelID() == 0 )
            {
                selHotelID = 0;
            }
            else if ( frmMenu.getSelHotelID() != 0 )
            {
                // �w�肳�ꂽ�z�e��
                selHotelID = frmMenu.getSelHotelID();
            }
            else
            {
                // �z�e�����X�g�̐擪��I���ς݂Ƃ���B
                selHotelID = frmMenu.getHotelIDList().get( 0 );
            }
            frmMenu.setSelHotelID( selHotelID );

            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.execute() ] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvMenu.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            frmMenu = null;
            frmHotelBasic = null;
            frmReserveBasic = null;
        }
    }

    /**
     * 
     * �\��Ǘ��Ăяo��
     */
    private void setRsvManage(FormOwnerRsvManage frm, int selHotelId, int userId, int planId, boolean dispAllFlag) throws Exception
    {
        LogicOwnerRsvManage logicManage;
        LogicOwnerRsvRoomManage logicRoom;
        LogicOwnerRsvPlanManage logicPlan;
        LogicOwnerRsvManageCalendar logicCalendar;
        FormOwnerRsvRoomManage frmRoom;
        FormOwnerRsvPlanManage frmPlan;
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList2 = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        ArrayList<Integer> planIdList = new ArrayList<Integer>();
        ArrayList<String> planIdNmList = new ArrayList<String>();
        String year = "";
        String month = "";
        String targetYYYYMM = "";
        String paramPlanId;

        logicManage = new LogicOwnerRsvManage();
        logicRoom = new LogicOwnerRsvRoomManage();
        logicPlan = new LogicOwnerRsvPlanManage();
        logicCalendar = new LogicOwnerRsvManageCalendar();
        frmRoom = new FormOwnerRsvRoomManage();
        frmPlan = new FormOwnerRsvPlanManage();
        try
        {
            // �Ǘ����擾
            frm.setSelHotelID( selHotelId );
            logicManage.setFrm( frm );
            logicManage.getRsvManage();

            // �z�e�����I����
            if ( selHotelId == 0 )
            {
                frm.setSelHotelErrMsg( Message.getMessage( "warn.30010" ) );
                return;
            }

            // �v�����󋵎擾
            frmPlan.setSelHotelId( selHotelId );
            frmPlan.setUserId( userId );
            logicPlan.setFrm( frmPlan );
            logicPlan.getPlan( PUBLISHING_NON_DISP );

            if ( dispAllFlag == false && planId == -1 )
            {
                if ( frmPlan.getExPlanId().size() > 0 )
                {
                    planId = frmPlan.getExPlanId().get( 0 );
                }
            }

            // 1�ڂ̃J�����_�[���擾
            // �����擾
            Calendar calendar = Calendar.getInstance();
            year = Integer.toString( calendar.get( Calendar.YEAR ) );
            month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );
            if ( planId == -1 )
            {
                monthlyList = logicCalendar.getCalendarData( selHotelId, planId, Integer.parseInt( year + month ) );
            }
            // �v����ID�������Ă����瑼�Ŏg���Ă��郍�W�b�N�Ƃ͕ʂ̂��̂��擾����i�\��Ǘ������Ŏg�p���邽�߁j
            else
            {
                monthlyList = logicCalendar.getPlanCalendarData( selHotelId, planId, Integer.parseInt( year + month ) );
            }

            // 2�ڂ̃J�����_�[���擾
            // �����擾
            calendar = Calendar.getInstance();
            calendar.add( Calendar.MONTH, 1 );
            targetYYYYMM = new SimpleDateFormat( "yyyyMM" ).format( calendar.getTime() );
            if ( planId == -1 )
            {
                monthlyList2 = logicCalendar.getCalendarData( selHotelId, planId, Integer.parseInt( targetYYYYMM ) );
            }
            // �v����ID�������Ă����瑼�Ŏg���Ă��郍�W�b�N�Ƃ͕ʂ̂��̂��擾����i�\��Ǘ������Ŏg�p���邽�߁j
            else
            {
                monthlyList2 = logicCalendar.getPlanCalendarData( selHotelId, planId, Integer.parseInt( targetYYYYMM ) );
            }

            // �������擾
            frmRoom.setSelHotelID( selHotelId );
            frmRoom.setUserId( userId );
            logicRoom.setFrm( frmRoom );
            logicRoom.getRoomData();

            // �t�H�[���ɃZ�b�g
            frm.setNowTopMonth( Integer.parseInt( year + month ) );
            frm.setMonthlyList( monthlyList );
            frm.setMonthlyList2( monthlyList2 );
            frm.setFrmPlan( logicPlan.getFrm() );
            frm.setFrmRoom( logicRoom.getFrm() );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setRsvManage() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setRsvManage] " + exception );
        }
    }

    /**
     * 
     * �{�݊�{���ݒ��ʌĂяo��
     */
    private void setHotelBasic(FormOwnerRsvHotelBasic frm) throws Exception
    {
        LogicOwnerRsvHotelBasic logic;

        logic = new LogicOwnerRsvHotelBasic();

        try
        {
            logic.setFrm( frm );
            logic.getHotelRsv();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setHotelBasic() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setHotelBasic] " + exception );
        }
    }

    /**
     * 
     * �{�݊�{���ݒ��ʌĂяo��
     */
    private void setReservBasic(FormOwnerRsvReserveBasic frm) throws Exception
    {
        LogicOwnerRsvReserveBasic logic;

        logic = new LogicOwnerRsvReserveBasic();

        try
        {
            frm.setParking( PARKING_SEL );
            logic.setFrm( frm );
            logic.getHotelRsv();
            if ( logic.getFrm().getParking() <= 0 )
            {
                // DB�̃f�t�H���g�l��0�Ȃ̂ŏ����l���ăZ�b�g����悤�ɏC��
                logic.getFrm().setParking( PARKING_SEL );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setReservBasic() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setReservBasic] " + exception );
        }
    }

    /**
     * 
     * �������Ăяo��
     */
    private void setRoomManage(FormOwnerRsvRoomManage frm) throws Exception
    {
        LogicOwnerRsvRoomManage logic;

        logic = new LogicOwnerRsvRoomManage();

        try
        {
            logic.setFrm( frm );
            logic.getRoomData();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setRoomManage() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setRoomManage] " + exception );
        }
    }

    /**
     * 
     * �ݔ����Ăяo��
     */
    private void setEquip(FormOwnerRsvEquipManage frm) throws Exception
    {
        LogicOwnerRsvEquipManage logic;

        logic = new LogicOwnerRsvEquipManage();

        try
        {
            logic.setFrm( frm );
            logic.getEquipData();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setEquip() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setEquip] " + exception );
        }
    }

    /**
     * 
     * �I�v�V�����Ǘ��Ăяo��
     */
    private void setOption(FormOwnerRsvOptionManage frm) throws Exception
    {
        LogicOwnerRsvOptionManage logic;

        logic = new LogicOwnerRsvOptionManage();

        try
        {
            logic.setFrm( frm );
            logic.getOption();

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setOption() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setOption] " + exception );
        }
    }

    /**
     * 
     * �������[�h�ݒ�Ăяo��
     */
    private void setChargeMode(FormOwnerRsvChargeMode frm) throws Exception
    {
        String errMsg = "";
        LogicOwnerRsvChargeMode logic;
        LogicOwnerRsvPlanChargeManage logicPlnCharge;

        logic = new LogicOwnerRsvChargeMode();
        logicPlnCharge = new LogicOwnerRsvPlanChargeManage();

        try
        {
            // �������[�h�����݂��邩
            if ( logicPlnCharge.existsChargeMode( frm.getSelHotelID() ) == false )
            {
                // �������[�h�����݂��Ȃ�
                errMsg = errMsg + Message.getMessage( "warn.30017" );
                frm.setErrMsg( errMsg );
            }

            logic.setFrm( frm );
            logic.getInit();
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setChargeMode() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setChargeMode] " + exception );
        }
    }

    /**
     * 
     * �v�����ʗ����ݒ�Ăяo��
     */
    private void setPlanChargeManage(FormOwnerRsvPlanChargeManage frm) throws Exception
    {
        LogicOwnerRsvPlanChargeManage logic;
        String errMsg = "";
        logic = new LogicOwnerRsvPlanChargeManage();

        try
        {
            logic.setFrm( frm );

            // �������[�h�����݂��邩
            if ( logic.existsChargeMode( frm.getSelHotelId() ) == true )
            {
                // �v�����ʗ������擾
                logic.getPlanChargeData( 1 );
                return;
            }

            // �������[�h�����݂��Ȃ�
            errMsg = errMsg + Message.getMessage( "warn.30004" );

            // �v������񂪑��݂��邩
            if ( frm.getPlanIdList().size() == 0 )
            {
                if ( errMsg.trim().length() != 0 )
                {
                    errMsg = errMsg + "<br /> ";
                }
                errMsg = errMsg + Message.getMessage( "erro.30001", "�o�^����Ă���v�������" );
            }
            frm.setErrMsg( errMsg );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setPlanChargeManage() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setPlanChargeManage] " + exception );
        }
    }

    /**
     * 
     * �v�����ݒ�Ăяo��
     */
    private void setPlanManage(FormOwnerRsvPlanManage frm) throws Exception
    {
        LogicOwnerRsvPlanManage logic;

        logic = new LogicOwnerRsvPlanManage();

        try
        {
            logic.setFrm( frm );
            logic.getPlan( OwnerRsvCommon.PLAN_VIEW_PART );
            frm.setViewMode( OwnerRsvCommon.PLAN_VIEW_PART );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setPlanManage() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setPlanManage()] " + exception );
        }
    }

    /**
     * 
     * �J�����_�[�Ǘ��Ăяo��
     */
    private void setPlanChargeCalendar(FormOwnerRsvPlanChargeCalendar frm, int imediaFlag) throws Exception
    {
        LogicOwnerRsvPlanChargeCalendar logic = new LogicOwnerRsvPlanChargeCalendar();
        int planId = 0;
        String year = "";
        String month = "";
        String planNm = "";
        ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>>();

        try
        {
            // �v�������擾
            logic.setFrm( frm );
            logic.getPlanList();
            if ( frm.getPlanIdList().size() == 0 )
            {
                // �v�������Ȃ�
                frm.setErrMsg( Message.getMessage( "erro.30001", "�\���\�ȃJ�����_�[���" ) );
                return;
            }

            // �擪�s�̃J�����_�[����\��
            planId = frm.getPlanIdList().get( 0 );

            // �v�������擾
            planNm = frm.getPlanNmList().get( 0 );
            frm.setSelPlanNm( planNm );
            frm.setSelPlanId( planId );

            // �\�񂪓����Ă��邩�`�F�b�N
            if ( OwnerRsvCommon.isExistsRsvPlan( frm.getSelHotelId(), planId ) != false )
            {
                frm.setWarnMsg( Message.getMessage( "warn.30041", Integer.toString( frm.getSelHotelId() ) ) );
            }

            // �����擾
            Calendar calendar = Calendar.getInstance();
            year = Integer.toString( calendar.get( Calendar.YEAR ) );
            month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );

            // �J�����_�[���擾
            logic.getCalendar( frm.getSelHotelId(), planId, Integer.parseInt( year + month ), imediaFlag );
            monthlyList = logic.getFrm().getMonthlyList();

            frm.setMonthlyList( monthlyList );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvMenu.setPlanChargeCalendar() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setPlanChargeCalendar()] " + exception );
        }
    }
}