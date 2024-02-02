package jp.happyhotel.action;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvPlanCharge;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeCalendar;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeCalendarSub;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeManage;
import jp.happyhotel.owner.FormOwnerRsvPlanChargeSub;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvPlanCharge;
import jp.happyhotel.owner.LogicOwnerRsvPlanChargeCalendar;
import jp.happyhotel.owner.LogicOwnerRsvPlanChargeManage;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;

/**
 * 
 * �v�����ʗ����ݒ���Action�N���X
 */
public class ActionOwnerRsvPlanCharge extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  menuFlg_MNG       = 5;
    private static final int  menuFlg_DETAIL    = 51;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerRsvPlanCharge frm;
        FormOwnerRsvPlanChargeSub frmSub;
        FormOwnerRsvPlanChargeManage frmManage;
        LogicOwnerRsvPlanCharge logic;
        LogicOwnerRsvPlanChargeManage logicManage;
        int paramHotelID = 0;
        int paramUserID = 0;
        String paramBtnNm = "";
        boolean inptCheckFlg = false;
        boolean checkDeposit = false;
        int chashDeposit = 0;
        int menuFlg = 0;
        String errMsg = "";
        String targetYYYYMM = "";
        String paraOwnerHotelID = "";
        NumberFormat objNum = NumberFormat.getCurrencyInstance();
        ReserveCommon rsvcomm = new ReserveCommon();
        FormReservePersonalInfoPC frmInfo = new FormReservePersonalInfoPC();
        int planView = 1; // 1:��f�ڕ��͕\�����Ȃ��idefault�j

        // �z�e���C�������ŗ��p����
        ArrayList<FormOwnerRsvPlanChargeSub> frmSubList;
        int adjust_edit_id = 0;
        String adjust_edit_memo = "";
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        String hotenaviId = "";
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();
        int adminImediaFlag = 0;

        try
        {
            frmMenu = new FormOwnerBkoMenu();
            frm = new FormOwnerRsvPlanCharge();
            frmSub = new FormOwnerRsvPlanChargeSub();
            frmManage = new FormOwnerRsvPlanChargeManage();
            logic = new LogicOwnerRsvPlanCharge();
            logicManage = new LogicOwnerRsvPlanChargeManage();

            // ��ʂ̒l���擾���A�t�H�[���ɃZ�b�g
            paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ) );
            paramUserID = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            paramBtnNm = request.getParameter( "btnNm" );
            if ( paramBtnNm == null )
            {
                paramBtnNm = "";
            }
            setFrmParam( request, frm, 1 );

            // ��f�ڕ����f�ڂ��邩�ۂ�
            if ( request.getParameter( "planView" ) != null )
            {
                planView = Integer.parseInt( request.getParameter( "planView" ) );
            }

            // �\�������֌W�擾
            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            logicMenu.setFrm( new FormOwnerBkoMenu() );
            disptype = logicMenu.getUserAuth( hotenaviId, paramUserID );
            adminflag = logicMenu.getAdminFlg( hotenaviId, paramUserID );
            imediaflag = OwnerRsvCommon.getImediaFlag( hotenaviId, paramUserID );

            if ( (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) != false )
            {
                adminImediaFlag = 1;
            }

            if ( (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false && disptype != OwnerRsvCommon.USER_AUTH_OWNER && disptype != OwnerRsvCommon.USER_AUTH_DEMO && disptype != OwnerRsvCommon.USER_AUTH_CALLCENTER )
            {
                // �����̂Ȃ����[�U�̓G���[�y�[�W��\������
                errMsg = Message.getMessage( "erro.30001", "�y�[�W���{�����錠��" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( paramBtnNm.equals( "btnBack" ) == false && paramBtnNm.equals( "btnPreview" ) == false && paramBtnNm.equals( "btnPreview2" ) == false &&
                    disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
            {
                // �R�[���Z���^�[�̍X�V�͋����Ȃ�
                errMsg = Message.getMessage( "erro.30001", "�X�e�[�^�X���X�V���錠��" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            Logging.info( paramBtnNm );
            if ( paramBtnNm.equals( "btnReg" ) )
            {
                // ���o�^
                // ���̓`�F�b�N
                if ( isInputCheckMsg( frm, adminImediaFlag ) == true )
                {
                    // �f�[�^�o�^
                    logic.setFrm( frm );

                    // �Œ�\����z�擾
                    chashDeposit = logic.getChashDeposit( paramHotelID );
                    frm.setCashDeposit( chashDeposit );

                    // �V�K�ǉ��E�X�V����
                    logic.addPlanCharge();
                    adjust_edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_CHARGE_ADDUPD;
                    adjust_edit_memo = OwnerRsvCommon.ADJUST_MEMO_PLAN_CHARGE_ADDUPD;
                    inptCheckFlg = true;

                    // �X�V��v�����̔���~�j���Ď擾
                    DataRsvPlan data = new DataRsvPlan();
                    data.getData( paramHotelID, frm.getSelPlanId() );
                    frm.setSalesStopWeekStatus( data.getSalesStopWeekStatus() );
                    frm.setSalesStopWeekView( OwnerRsvCommon.createSalesStopWeek( data.getSalesStopWeekStatus() ) );

                    // �f�[�^�o�^���Ƀv�����ʃJ�����_�[�̍쐬���s��
                    logic.addPlanCalendar( data.getSalesStopWeekStatus() );

                    // �z�e���C�������̓o�^
                    frmSubList = frm.getFrmSubList();
                    for( int i = 0 ; i < frmSubList.size() ; i++ )
                    {
                        FormOwnerRsvPlanChargeSub frmSubWk = frm.getFrmSubList().get( i );
                        OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                frm.getUserId(), adjust_edit_id,
                                frm.getSelPlanId() * 1000 + frmSubWk.getChargeModeId(),
                                adjust_edit_memo );
                    }

                    // �Œ���z�`�F�b�N
                    checkDeposit = checkCashDeposit( frm, logic, chashDeposit );
                    if ( checkDeposit == true )
                    {
                        // �Œ�\����z��������
                        frm.setInfoMsg( Message.getMessage( "warn.30009", objNum.format( chashDeposit ) ) );
                        // ��U��ɂ���
                        frm.setFrmSubList( new ArrayList<FormOwnerRsvPlanChargeSub>() );
                        getDetailData( request, logic, frm, frmSub );
                        menuFlg = menuFlg_DETAIL;
                        request.setAttribute( "FORM_Charge", frm );

                    }
                    else
                    {
                        // �v�����ݒ�Ǘ���ʃf�[�^�擾
                        menuFlg = menuFlg_MNG;
                        frmManage.setSelHotelId( paramHotelID );
                        logicManage.setFrm( frmManage );
                        logicManage.getPlanChargeData( planView );
                        request.setAttribute( "FORM_ChargeManage", logicManage.getFrm() );
                    }
                }

                // ���̓`�F�b�N���G���[�̏ꍇ
                if ( inptCheckFlg == false )
                {
                    getDetailData( request, logic, frm, frmSub );

                    // �ҏW���A�\��f�[�^���݃`�F�b�N
                    if ( OwnerRsvCommon.isReservePlan( paramHotelID, frm.getSelPlanId() ) == true )
                    {
                        if ( (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
                        {
                            frm.setViewMode( 1 );
                        }
                        frm.setRsvMsg( Message.getMessage( "warn.30012", Integer.toString( paramHotelID ) ) );
                    }
                    menuFlg = menuFlg_DETAIL;
                    request.setAttribute( "FORM_Charge", frm );
                }
                // ���j���[�ݒ�
                frmMenu.setUserId( paramUserID );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

                // ��ʑJ��
                request.setAttribute( "FORM_Menu", frmMenu );
                requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
                requestDispatcher.forward( request, response );
                return;

            }
            else if ( paramBtnNm.equals( "btnBack" ) )
            {
                // ���߂�
                // �v���������ݒ�Ǘ���ʃf�[�^�擾
                menuFlg = menuFlg_MNG;
                frmManage.setSelHotelId( paramHotelID );
                logicManage.setFrm( frmManage );
                logicManage.getPlanChargeData( planView );
                request.setAttribute( "FORM_ChargeManage", logicManage.getFrm() );
                // ���j���[�ݒ�
                frmMenu.setUserId( paramUserID );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

                // ��ʑJ��
                request.setAttribute( "FORM_Menu", frmMenu );
                requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
                requestDispatcher.forward( request, response );

                return;
            }
            else if ( paramBtnNm.equals( "btnPreview" ) )
            {
                // �v���r���[��ʂ̕\��
                paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
                FormOwnerRsvPlanChargeCalendar frmCalendar;
                frmCalendar = new FormOwnerRsvPlanChargeCalendar();
                Calendar orgCal;

                frmCalendar.setSelHotelId( frm.getSelHotelId() );
                frmCalendar.setSelPlanId( frm.getSelPlanId() );
                frmCalendar.setOwnerHotelID( paraOwnerHotelID );
                frmCalendar.setUserId( paramUserID );
                frmCalendar.setSelPlanNm( frm.getPlanNm() );
                orgCal = Calendar.getInstance();

                targetYYYYMM = new SimpleDateFormat( "yyyyMM" ).format( orgCal.getTime() );
                getMonth( frmCalendar, paramHotelID, targetYYYYMM, frm.getSelPlanId(), imediaflag );
                if ( frmCalendar.getMonthlyList() != null && frmCalendar.getMonthlyList().get( 0 ) != null &&
                        frmCalendar.getMonthlyList().get( 0 ).get( 0 ) != null && frmCalendar.getMonthlyList().get( 0 ).get( 0 ).getErrMsg().equals( "" ) != true )
                {
                    // �S�̂̃J�����_�[�Ŏ擾
                    getMonth( frmCalendar, paramHotelID, targetYYYYMM, 0, imediaflag );
                }
                frmInfo.setSelHotelID( frm.getSelHotelId() );
                frmInfo.setSelPlanID( frm.getSelPlanId() );
                frmInfo = rsvcomm.getPlanData( frmInfo );
                request.setAttribute( "FORM_Charge", frm );
                request.setAttribute( "FORM_Calendar", frmCalendar );
                request.setAttribute( "FORM_Personal", frmInfo );
                requestDispatcher = request.getRequestDispatcher( "owner_rsv_plan_charge_preview.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            else if ( paramBtnNm.equals( "btnPreview2" ) )
            {
                if ( request.getParameter( "targetyyyymm" ) != null )
                {
                    paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
                    FormOwnerRsvPlanChargeCalendar frmCalendar;
                    frmCalendar = new FormOwnerRsvPlanChargeCalendar();
                    frmCalendar.setSelHotelId( frm.getSelHotelId() );
                    frmCalendar.setSelPlanId( frm.getSelPlanId() );
                    frmCalendar.setOwnerHotelID( paraOwnerHotelID );
                    frmCalendar.setSelPlanNm( frm.getPlanNm() );
                    frmCalendar.setUserId( paramUserID );
                    targetYYYYMM = request.getParameter( "targetyyyymm" );
                    getMonth( frmCalendar, paramHotelID, targetYYYYMM, frm.getSelPlanId(), imediaflag );
                    if ( frmCalendar.getMonthlyList() != null && frmCalendar.getMonthlyList().get( 0 ) != null &&
                            frmCalendar.getMonthlyList().get( 0 ).get( 0 ) != null && frmCalendar.getMonthlyList().get( 0 ).get( 0 ).getErrMsg().equals( "" ) != true )
                    {
                        // �S�̂̃J�����_�[�Ŏ擾
                        getMonth( frmCalendar, paramHotelID, targetYYYYMM, 0, imediaflag );
                    }
                    frmInfo.setSelHotelID( frm.getSelHotelId() );
                    frmInfo.setSelPlanID( frm.getSelPlanId() );
                    frmInfo = rsvcomm.getPlanData( frmInfo );
                    request.setAttribute( "FORM_Charge", frm );
                    request.setAttribute( "FORM_Calendar", frmCalendar );
                    request.setAttribute( "FORM_Personal", frmInfo );
                    requestDispatcher = request.getRequestDispatcher( "owner_rsv_plan_charge_preview.jsp" );
                    requestDispatcher.forward( request, response );
                }
                return;
            }

            // ���폜(�\��f�[�^���ݎ��͍폜�ł��Ȃ��悤�ɂ���)
            // �\��f�[�^���݃`�F�b�N
            if ( (OwnerRsvCommon.isReservePlan( frm.getSelHotelId(), frm.getSelPlanId() ) == true) &&
                    (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
            {
                // ���݂���
                frm.setViewMode( 1 );
                frm.setRsvMsg( Message.getMessage( "warn.30012", Integer.toString( frm.getSelHotelId() ) ) );
                getDetailData( request, logic, frm, frmSub );
                menuFlg = menuFlg_DETAIL;
                request.setAttribute( "FORM_Charge", frm );
            }
            else
            {
                frmSubList = new ArrayList<FormOwnerRsvPlanChargeSub>();
                // �폜�O�`�F�b�N
                if ( frm.getFrmDelList().size() == 0 )
                {
                    frm.setErrMsg( Message.getMessage( "warn.00002", "�폜�Ώۂ̗������[�h" ) );
                    getDetailData( request, logic, frm, frmSub );

                    menuFlg = menuFlg_DETAIL;
                    request.setAttribute( "FORM_Charge", frm );
                }
                else
                {
                    // �폜�������s
                    // �\��f�[�^���݃`�F�b�N
                    frmSubList = frm.getFrmDelList();
                    for( int i = 0 ; i < frmSubList.size() ; i++ )
                    {
                        FormOwnerRsvPlanChargeSub frmSubDel = frmSubList.get( i );
                        // �\�񂪑��݂����玖���������̂݋�����
                        if ( (OwnerRsvCommon.existsRsvChargeMode( paramHotelID, frm.getSelPlanId(), frmSubDel.getChargeModeId() ) == true)
                                && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
                        {
                            getDetailData( request, logic, frm, frmSub );

                            frm.setViewMode( 1 );
                            frm.setRsvMsg( Message.getMessage( "warn.30012", Integer.toString( paramHotelID ) ) );

                            menuFlg = menuFlg_DETAIL;
                            request.setAttribute( "FORM_Charge", frm );
                        }
                        else
                        {
                            // ���݂��Ȃ��ꍇ�͍폜
                            logic.execDelChargeMode( paramHotelID, frm.getSelPlanId(), frmSubDel.getChargeModeId() );

                            // �z�e���C�������̓o�^
                            adjust_edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_CHARGE_DEL;
                            adjust_edit_memo = OwnerRsvCommon.ADJUST_MEMO_PLAN_CHARGE_DEL;

                            FormOwnerRsvPlanChargeSub frmSubWk = frm.getFrmDelList().get( i );
                            OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                    frm.getUserId(), adjust_edit_id,
                                    frm.getSelPlanId() * 1000 + frmSubWk.getChargeModeId(),
                                    adjust_edit_memo );

                            // �v�����ݒ�Ǘ���ʃf�[�^�擾
                            menuFlg = menuFlg_MNG;
                            frmManage.setSelHotelId( paramHotelID );
                            logicManage.setFrm( frmManage );
                            logicManage.getPlanChargeData( planView );
                            request.setAttribute( "FORM_ChargeManage", logicManage.getFrm() );
                        }
                    }
                }
            }

            // ���j���[�ݒ�
            frmMenu.setUserId( paramUserID );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

            // ��ʑJ��
            request.setAttribute( "FORM_Menu", frmMenu );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvPlanCharge.execute() ][hotelId = " + paramHotelID + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvPlanCharge.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            logic = null;
        }
    }

    /**
     * �J�����_�[���擾
     * 
     * @param frm
     * @para, hotelId �z�e��ID
     * @param targetYM �w��N��
     * @param paramPlanId �v����ID
     * @param imediaFlag �A�C���f�B�A�t���O
     * @return �Ȃ�
     */
    private void getMonth(FormOwnerRsvPlanChargeCalendar frm, int hotelId, String targetYM, int paramPlanId, int imediaFlag) throws Exception
    {
        int planId = 0;
        LogicOwnerRsvPlanChargeCalendar logic = new LogicOwnerRsvPlanChargeCalendar();
        ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>>();

        try
        {
            frm.setSelHotelId( hotelId );

            // �v�������擾
            logic.setFrm( frm );
            logic.getPlanList();
            frm = logic.getFrm();

            if ( paramPlanId == -1 )
            {
                // �擪�s�̃J�����_�[����\��
                planId = frm.getPlanIdList().get( 0 );
            }
            else
            {
                planId = paramPlanId;
            }

            // �J�����_�[���擾
            if ( paramPlanId == 0 )
            {
                // �z�e���S�̂̏ꍇ
                logic.getHotelCalendar( frm.getSelHotelId(), Integer.parseInt( targetYM ), imediaFlag );
            }
            else
            {
                logic.getCalendar( frm.getSelHotelId(), planId, Integer.parseInt( targetYM ), imediaFlag );
            }

            monthlyList = logic.getFrm().getMonthlyList();
            frm.setMonthlyList( monthlyList );

            // �t�H�[���ɃZ�b�g
            frm.setSelPlanId( planId );
            frm.setMonthlyList( logic.getFrm().getMonthlyList() );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvPlanCharge.getMonth() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvPlanCharge.getMonth()] " + exception );
        }
    }

    /**
     * �������[�h�f�[�^�擾
     * 
     * @param HttpServletRequest
     * @param logic LogicOwnerRsvPlanCharge�I�u�W�F�N�g
     * @param frm FormOwnerRsvPlanCharge�I�u�W�F�N�g
     * @param frmSub FormOwnerRsvPlanChargeSub�I�u�W�F�N�g
     * @return �Ȃ�
     */
    private void getDetailData(HttpServletRequest request, LogicOwnerRsvPlanCharge logic, FormOwnerRsvPlanCharge frm, FormOwnerRsvPlanChargeSub frmSub) throws Exception
    {
        int chashDeposit = 0;
        boolean isChecked = false;

        if ( frm.getFrmSubList().size() > 0 )
        {
            isChecked = true;
        }

        // �f�[�^�Č���
        logic.setFrm( frm );
        logic.setFrmSub( frmSub );
        logic.getPlanCharge();

        // �Œ�\����z�擾
        chashDeposit = logic.getChashDeposit( frm.getSelHotelId() );
        frm.setCashDeposit( chashDeposit );

        // ���͒l���Z�b�g
        if ( isChecked == true )
        {
            setFrmParam( request, frm, 2 );
        }
    }

    /**
     * ��ʂ̒l���擾���AForm�ɃZ�b�g����B
     * 
     * @param HttpServletRequest
     * @param frm FormOwnerRsvPlanCharge�I�u�W�F�N�g
     * @param setKbn �t�H�[���Z�b�g�敪(1:��ʋN�����A2:�ĕ\����)
     * @return �Ȃ�
     */
    private void setFrmParam(HttpServletRequest request, FormOwnerRsvPlanCharge frm, int setKbn)
    {

        int chargeModeId = 0;
        ArrayList<Integer> modeList = new ArrayList<Integer>();
        ArrayList<Integer> delList = new ArrayList<Integer>();
        ArrayList<FormOwnerRsvPlanChargeSub> frmSubList = new ArrayList<FormOwnerRsvPlanChargeSub>();
        ArrayList<FormOwnerRsvPlanChargeSub> frmDelList = new ArrayList<FormOwnerRsvPlanChargeSub>();
        FormOwnerRsvPlanChargeSub frmSub;
        int weekStatus = 0;
        int salesStopWeekStatus = 0;
        int mondayCount = 0;
        int tuesdayCount = 0;
        int wednesdayCount = 0;
        int thursdayCount = 0;
        int fridayCount = 0;
        int saturdayCount = 0;
        int sundayCount = 0;
        int holidayCount = 0;
        int beforeHolidayCount = 0;
        boolean delflag = false;

        frm.setSelHotelId( Integer.parseInt( request.getParameter( "selHotelIDValue" ) ) );
        frm.setOwnerHotelID( OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ) );
        frm.setUserId( OwnerRsvCommon.getCookieLoginUserId( request.getCookies() ) );
        frm.setSelPlanId( Integer.parseInt( request.getParameter( "planId" ) ) );

        // �v�������擾
        DataRsvPlan data = new DataRsvPlan();
        data.getData( frm.getSelHotelId(), frm.getSelPlanId() );
        frm.setPlanNm( data.getPlanName() );
        frm.setSalesStopWeekStatus( data.getSalesStopWeekStatus() );
        frm.setSalesStopWeekView( OwnerRsvCommon.createSalesStopWeek( data.getSalesStopWeekStatus() ) );

        // �I������Ă��郂�[�h
        if ( request.getParameterValues( "sel" ) == null )
        {
            // ���`�F�b�N
            frm.setFrmSubList( frmSubList );
            return;
        }

        String[] selModes = request.getParameterValues( "sel" );
        if ( selModes != null )
        {
            for( int i = 0 ; i < selModes.length ; i++ )
            {
                modeList.add( Integer.parseInt( selModes[i] ) );
            }
        }

        String[] delModes = request.getParameterValues( "del" );
        if ( delModes != null )
        {
            for( int i = 0 ; i < delModes.length ; i++ )
            {
                delList.add( Integer.parseInt( delModes[i] ) );
            }
        }

        if ( setKbn == 1 )
        {
            // �������N����
            for( int i = 0 ; i < modeList.size() ; i++ )
            {
                frmSub = new FormOwnerRsvPlanChargeSub();
                chargeModeId = modeList.get( i );

                frmSub.setChargeModeId( chargeModeId );
                frmSub.setChargeModeNm( request.getParameter( "chargeModeNm" + chargeModeId ) );
                frmSub.setCiTimeFromHH( request.getParameter( "ciFromHH" + chargeModeId ) );
                frmSub.setCiTimeFromMM( request.getParameter( "ciFromMM" + chargeModeId ) );
                frmSub.setCiTimeToHH( request.getParameter( "ciToHH" + chargeModeId ) );
                frmSub.setCiTimeToMM( request.getParameter( "ciToMM" + chargeModeId ) );
                frmSub.setAdultTwo( request.getParameter( "adultTwo" + chargeModeId ) );
                frmSub.setAdultOne( request.getParameter( "adultOne" + chargeModeId ) );
                frmSub.setAdultAdd( request.getParameter( "adultAdd" + chargeModeId ) );
                frmSub.setChildAdd( request.getParameter( "childAdd" + chargeModeId ) );
                frmSub.setCoRemarks( request.getParameter( "coRemarks" + chargeModeId ) );
                frmSub.setRemarks( request.getParameter( "remarks" + chargeModeId ) );
                frmSub.setRegistStatus( Integer.parseInt( request.getParameter( "registStatus" + chargeModeId ) ) );
                weekStatus = 0;
                if ( request.getParameter( "chkmon" + chargeModeId ) != null && request.getParameter( "chkmon" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_MONDAY;
                    mondayCount++;
                }
                if ( request.getParameter( "chktue" + chargeModeId ) != null && request.getParameter( "chktue" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_TUESDAY;
                    tuesdayCount++;
                }
                if ( request.getParameter( "chkwed" + chargeModeId ) != null && request.getParameter( "chkwed" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_WEDNESDAY;
                    wednesdayCount++;
                }
                if ( request.getParameter( "chkthi" + chargeModeId ) != null && request.getParameter( "chkthi" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_THIRTHDAY;
                    thursdayCount++;
                }
                if ( request.getParameter( "chkfri" + chargeModeId ) != null && request.getParameter( "chkfri" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_FRIDAY;
                    fridayCount++;
                }
                if ( request.getParameter( "chksat" + chargeModeId ) != null && request.getParameter( "chksat" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_SATURDAY;
                    saturdayCount++;
                }
                if ( request.getParameter( "chksun" + chargeModeId ) != null && request.getParameter( "chksun" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_SUNDAY;
                    sundayCount++;
                }
                if ( request.getParameter( "chkholi" + chargeModeId ) != null && request.getParameter( "chkholi" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_HOLIDAY;
                    holidayCount++;
                }
                if ( request.getParameter( "chkbeforeholi" + chargeModeId ) != null && request.getParameter( "chkbeforeholi" + chargeModeId ).equals( "on" ) )
                {
                    weekStatus += OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY;
                    beforeHolidayCount++;
                }
                frmSub.setWeekStatus( weekStatus );
                if ( request.getParameter( "coKind" + chargeModeId ) != null )
                {
                    frmSub.setCoKind( Integer.parseInt( request.getParameter( "coKind" + chargeModeId ) ) );
                }
                frmSub.setCoTimeHH( request.getParameter( "coTimeHH" + chargeModeId ) );
                frmSub.setCoTimeMM( request.getParameter( "coTimeMM" + chargeModeId ) );

                delflag = false;
                for( int j = 0 ; j < delList.size() ; j++ )
                {
                    if ( delList.get( j ) == chargeModeId )
                    {
                        delflag = true;
                        break;
                    }
                }
                if ( delflag == true )
                {
                    // �폜���X�g�ɒǉ�
                    frmDelList.add( frmSub );
                }
                else
                {
                    // �o�^�E�X�V���X�g�֒ǉ�
                    frmSubList.add( frmSub );
                }
            }
            if ( mondayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_MONDAY;
            }
            if ( tuesdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_TUESDAY;
            }
            if ( wednesdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_WEDNESDAY;
            }
            if ( thursdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_THIRTHDAY;
            }
            if ( fridayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_FRIDAY;
            }
            if ( saturdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_SATURDAY;
            }
            if ( sundayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_SUNDAY;
            }
            if ( holidayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_HOLIDAY;
            }
            if ( beforeHolidayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY;
            }
        }
        else
        {
            // ���ēǍ���
            frmSubList = frm.getFrmSubList();
            for( int i = 0 ; i < frmSubList.size() ; i++ )
            {
                frmSub = frmSubList.get( i );

                // ���͒l�Z�b�g
                for( int j = 0 ; j < modeList.size() ; j++ )
                {
                    chargeModeId = modeList.get( j );

                    if ( frmSub.getChargeModeId() == chargeModeId )
                    {
                        frmSub.setCheck( 1 );
                        frmSub.setChargeModeId( chargeModeId );
                        frmSub.setCiTimeFromHH( request.getParameter( "ciFromHH" + chargeModeId ) );
                        frmSub.setCiTimeFromMM( request.getParameter( "ciFromMM" + chargeModeId ) );
                        frmSub.setCiTimeToHH( request.getParameter( "ciToHH" + chargeModeId ) );
                        frmSub.setCiTimeToMM( request.getParameter( "ciToMM" + chargeModeId ) );
                        frmSub.setCoTimeHH( request.getParameter( "coTimeHH" + chargeModeId ) );
                        frmSub.setCoTimeMM( request.getParameter( "coTimeMM" + chargeModeId ) );
                        frmSub.setAdultTwo( request.getParameter( "adultTwo" + chargeModeId ) );
                        frmSub.setAdultOne( request.getParameter( "adultOne" + chargeModeId ) );
                        frmSub.setAdultAdd( request.getParameter( "adultAdd" + chargeModeId ) );
                        frmSub.setChildAdd( request.getParameter( "childAdd" + chargeModeId ) );
                        frmSub.setCoRemarks( request.getParameter( "coRemarks" + chargeModeId ) );
                        frmSub.setRemarks( request.getParameter( "remarks" + chargeModeId ) );
                        frmSub.setRegistStatus( Integer.parseInt( request.getParameter( "registStatus" + chargeModeId ) ) );
                        weekStatus = 0;
                        if ( request.getParameter( "chkmon" + chargeModeId ) != null && request.getParameter( "chkmon" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_MONDAY;
                            mondayCount++;
                        }
                        if ( request.getParameter( "chktue" + chargeModeId ) != null && request.getParameter( "chktue" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_TUESDAY;
                            tuesdayCount++;
                        }
                        if ( request.getParameter( "chkwed" + chargeModeId ) != null && request.getParameter( "chkwed" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_WEDNESDAY;
                            wednesdayCount++;
                        }
                        if ( request.getParameter( "chkthi" + chargeModeId ) != null && request.getParameter( "chkthi" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_THIRTHDAY;
                            thursdayCount++;
                        }
                        if ( request.getParameter( "chkfri" + chargeModeId ) != null && request.getParameter( "chkfri" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_FRIDAY;
                            fridayCount++;
                        }
                        if ( request.getParameter( "chksat" + chargeModeId ) != null && request.getParameter( "chksat" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_SATURDAY;
                            saturdayCount++;
                        }
                        if ( request.getParameter( "chksun" + chargeModeId ) != null && request.getParameter( "chksun" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_SUNDAY;
                            sundayCount++;
                        }
                        if ( request.getParameter( "chkholi" + chargeModeId ) != null && request.getParameter( "chkholi" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_HOLIDAY;
                            holidayCount++;
                        }
                        if ( request.getParameter( "chkbeforeholi" + chargeModeId ) != null && request.getParameter( "chkbeforeholi" + chargeModeId ).equals( "on" ) )
                        {
                            weekStatus += OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY;
                            beforeHolidayCount++;
                        }
                        frmSub.setWeekStatus( weekStatus );
                        frmSub.setCoKind( Integer.parseInt( request.getParameter( "coKind" + chargeModeId ) ) );
                    }
                }

            }
            if ( mondayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_MONDAY;
            }
            if ( tuesdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_TUESDAY;
            }
            if ( wednesdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_WEDNESDAY;
            }
            if ( thursdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_THIRTHDAY;
            }
            if ( fridayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_FRIDAY;
            }
            if ( saturdayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_SATURDAY;
            }
            if ( sundayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_SUNDAY;
            }
            if ( holidayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_HOLIDAY;
            }
            if ( beforeHolidayCount == 0 )
            {
                salesStopWeekStatus += OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY;
            }
        }
        frm.setSalesStopWeekStatus( salesStopWeekStatus );
        frm.setFrmSubList( frmSubList );
        frm.setFrmDelList( frmDelList );
    }

    /**
     * ���͒l�`�F�b�N
     * 
     * @param frm FormOwnerRsvPlanCharge�I�u�W�F�N�g
     * @param adminImediaFlag �����ǌ���
     * @return true:����Afalse:�G���[����
     */
    private boolean isInputCheckMsg(FormOwnerRsvPlanCharge frm, int adminImediaFlag) throws Exception
    {
        String msg;
        boolean isCheck = false;
        boolean isCiFromHHCheck = true;
        boolean isCiFromMMCheck = true;
        boolean isCiToHHCheck = true;
        boolean isCiToMMCheck = true;
        boolean isCheckOutHHCheck = true;
        boolean isCheckOutMMCheck = true;
        int mondayCount = 0;
        int tuesdayCount = 0;
        int wednesdayCount = 0;
        int thirthdayCount = 0;
        int fridayCount = 0;
        int saturdayCount = 0;
        int sundayCount = 0;
        int holidayCount = 0;
        int beforeholidayCount = 0;
        String chargeModeNm = "";
        int ciFrom = 0;
        int ciTo = 0;
        int ret = 0;
        int coTime = 0;
        msg = "";
        ReserveCommon rsvcomm = new ReserveCommon();

        if ( frm.getFrmSubList().size() == 0 )
        {
            msg = msg + Message.getMessage( "warn.00002", "�o�^�Ώۂ̗������[�h" );
            frm.setErrMsg( msg );
            return(isCheck);
        }

        // �����ǌ����łȂ���Η\��f�[�^���݃`�F�b�N
        if ( adminImediaFlag == 0 )
        {
            // �\��f�[�^���݃`�F�b�N
            if ( OwnerRsvCommon.isReservePlan( frm.getSelHotelId(), frm.getSelPlanId() ) == true )
            {
                // ���݂���
                frm.setViewMode( 1 );
                frm.setRsvMsg( Message.getMessage( "warn.30012", Integer.toString( frm.getSelHotelId() ) ) );
                return(isCheck);
            }
        }

        for( int i = 0 ; i < frm.getFrmSubList().size() ; i++ )
        {
            FormOwnerRsvPlanChargeSub frmSub = frm.getFrmSubList().get( i );
            chargeModeNm = ReplaceString.HTMLEscape( frmSub.getChargeModeNm() );

            // ���`�F�b�N�C��From�@����
            if ( CheckString.onlySpaceCheck( frmSub.getCiTimeFromHH() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�y" + chargeModeNm + "�z�̃`�F�b�N�C��From�̎���" ) + "<br />";
                isCiFromHHCheck = false;
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getCiTimeFromHH() ) == false) )
                {
                    // ���p�����ȊO�����͂���Ă���ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̃`�F�b�N�C��From�̎���", "0�ȏ�̐���" ) + "<br />";
                    isCiFromHHCheck = false;
                }
                else
                {
                    if ( (Integer.parseInt( frmSub.getCiTimeFromHH() ) > 29 || Integer.parseInt( frmSub.getCiTimeFromHH() ) < 0) )
                    {
                        // ���Ԃ�0���`23���ȊO�̏ꍇ
                        msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̃`�F�b�N�C��From�̎���", "0�`29" ) + "<br />";
                        isCiFromHHCheck = false;
                    }
                }
            }

            // ���`�F�b�N�C��From�@��
            if ( CheckString.onlySpaceCheck( frmSub.getCiTimeFromMM() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�y" + chargeModeNm + "�z�̃`�F�b�N�C��From�̕�" ) + "<br />";
                isCiFromMMCheck = false;
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getCiTimeFromMM() ) == false) )
                {
                    // ���p�����ȊO�����͂���Ă���ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̃`�F�b�N�C��From�̕�", "0�ȏ�̐���" ) + "<br />";
                    isCiFromMMCheck = false;
                }
                else
                {
                    if ( (Integer.parseInt( frmSub.getCiTimeFromMM() ) > 59 || Integer.parseInt( frmSub.getCiTimeFromMM() ) < 0) )
                    {
                        // ���Ԃ�0���`23���ȊO�̏ꍇ
                        msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̃`�F�b�N�C��From�̕�", "0�`59" ) + "<br />";
                        isCiFromMMCheck = false;
                    }
                }
            }

            // ���`�F�b�N�C��To�@����
            if ( CheckString.onlySpaceCheck( frmSub.getCiTimeToHH() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�y" + chargeModeNm + "�z�̃`�F�b�N�C��To�̎���" ) + "<br />";
                isCiToHHCheck = false;
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getCiTimeToHH() ) == false) )
                {
                    // ���p�����ȊO�����͂���Ă���ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̃`�F�b�N�C��To�̎���", "0�ȏ�̐���" ) + "<br />";
                    isCiToHHCheck = false;
                }
                else
                {
                    if ( (Integer.parseInt( frmSub.getCiTimeToHH() ) > 29 || Integer.parseInt( frmSub.getCiTimeToHH() ) < 0) )
                    {
                        // ���Ԃ�0���`29���ȊO�̏ꍇ
                        msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̃`�F�b�N�C��To�̎���", "0�`29" ) + "<br />";
                        isCiToHHCheck = false;
                    }
                }
            }

            // ���`�F�b�N�C��To�@��
            if ( CheckString.onlySpaceCheck( frmSub.getCiTimeToMM() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�y" + chargeModeNm + "�z�̃`�F�b�N�C��To�̕�" ) + "<br />";
                isCiToMMCheck = false;
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getCiTimeToMM() ) == false) )
                {
                    // ���p�����ȊO�����͂���Ă���ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̃`�F�b�N�C��To�̕�", "0�ȏ�̐���" ) + "<br />";
                    isCiToMMCheck = false;
                }
                else
                {
                    if ( (Integer.parseInt( frmSub.getCiTimeToMM() ) > 59 || Integer.parseInt( frmSub.getCiTimeToMM() ) < 0) )
                    {
                        // ���Ԃ�0���`23���ȊO�̏ꍇ
                        msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̃`�F�b�N�C��To�̕�", "0�`59" ) + "<br />";
                        isCiToMMCheck = false;
                    }
                }
            }

            // ���`�F�b�N�C��From�|To�͈̔̓`�F�b�N
            if ( isCiFromHHCheck == true && isCiFromMMCheck == true && isCiToHHCheck == true && isCiToMMCheck == true )
            {
                ciFrom = Integer.parseInt( String.format( "%02d", Integer.parseInt( frmSub.getCiTimeFromHH() ) ) + String.format( "%02d", Integer.parseInt( frmSub.getCiTimeFromMM() ) ) );
                ciTo = Integer.parseInt( String.format( "%02d", Integer.parseInt( frmSub.getCiTimeToHH() ) ) + String.format( "%02d", Integer.parseInt( frmSub.getCiTimeToMM() ) ) );
                if ( ciFrom > ciTo )
                {
                    msg = msg + Message.getMessage( "warn.00009", "�y" + chargeModeNm + "�z�̃`�F�b�N�C���͈͎̔w��" ) + "<br />";
                }
            }

            // ���`�F�b�N�A�E�g ����
            if ( CheckString.onlySpaceCheck( frmSub.getCoTimeHH() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�y" + chargeModeNm + "�z�̃`�F�b�N�A�E�g�̎���" ) + "<br />";
                isCheckOutHHCheck = false;
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getCoTimeHH() ) == false) )
                {
                    // ���p�����ȊO�����͂���Ă���ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̃`�F�b�N�A�E�g�̎���", "0�ȏ�̐���" ) + "<br />";
                    isCheckOutHHCheck = false;
                }
                else
                {
                    if ( frmSub.getCoKind() == 0 )
                    {
                        if ( Integer.parseInt( frmSub.getCoTimeHH() ) > 23 || Integer.parseInt( frmSub.getCoTimeHH() ) < 0 )
                        {
                            // ���Ԃ�0���`23���ȊO�̏ꍇ
                            msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̃`�F�b�N�A�E�g�̎���", "0�`23" ) + "<br />";
                            isCheckOutHHCheck = false;
                        }
                    }
                    // �`�F�b�N�A�E�g�敪���A�`�F�b�N�C�����灛���Ԃ̏ꍇ�͐��l��24���Ԃ܂�OK�Ƃ���
                    else
                    {
                        if ( Integer.parseInt( frmSub.getCoTimeHH() ) > 24 || Integer.parseInt( frmSub.getCoTimeHH() ) < 0 )
                        {
                            // ���Ԃ�0���`24���ȊO�̏ꍇ
                            msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̃`�F�b�N�A�E�g�̎���", "0�`24" ) + "<br />";
                            isCheckOutHHCheck = false;
                        }

                    }
                }
            }

            // ���`�F�b�N�A�E�g ��
            if ( CheckString.onlySpaceCheck( frmSub.getCoTimeMM() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�y" + chargeModeNm + "�z�̃`�F�b�N�A�E�g�̕�" ) + "<br />";
                isCheckOutMMCheck = false;
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getCoTimeMM() ) == false) )
                {
                    // ���p�����ȊO�����͂���Ă���ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̃`�F�b�N�A�E�g�̕�", "0�ȏ�̐���" ) + "<br />";
                    isCheckOutMMCheck = false;
                }
                else
                {
                    if ( (Integer.parseInt( frmSub.getCoTimeMM() ) > 59 || Integer.parseInt( frmSub.getCoTimeMM() ) < 0) )
                    {
                        // ���Ԃ�0���`23���ȊO�̏ꍇ
                        msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̃`�F�b�N�A�E�g�̕�", "0�`59" ) + "<br />";
                        isCheckOutMMCheck = false;
                    }
                }
            }

            // ���`�F�b�N�C��To�ƃ`�F�b�N�A�E�g�̃`�F�b�N(�`�F�b�N�A�E�g�敪��0�̏ꍇ�̂݃`�F�b�N����)
            if ( frmSub.getCoKind() == 0 && isCiFromHHCheck == true && isCiFromMMCheck == true && isCiToHHCheck == true && isCiToMMCheck == true && isCheckOutHHCheck == true && isCheckOutMMCheck == true )
            {
                ciTo = Integer.parseInt( String.format( "%02d", Integer.parseInt( frmSub.getCiTimeToHH() ) ) + String.format( "%02d", Integer.parseInt( frmSub.getCiTimeToMM() ) ) );
                coTime = Integer.parseInt( String.format( "%02d", Integer.parseInt( frmSub.getCoTimeHH() ) ) + String.format( "%02d", Integer.parseInt( frmSub.getCoTimeMM() ) ) );
                if ( OwnerRsvCommon.checkCiCoTime( ciTo, coTime ) == false )
                {
                    msg = msg + Message.getMessage( "warn.00009", "�y" + chargeModeNm + "�z�̃`�F�b�N�C������To�ƃ`�F�b�N�A�E�g���Ԃ͈͎̔w��" ) + "<br />";
                }
            }

            // ����l��l
            if ( CheckString.onlySpaceCheck( frmSub.getAdultTwo() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�y" + chargeModeNm + "�z�̑�l2�l����" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getAdultTwo() ) == false) )
                {
                    // ���p�����ȊO�����͂���Ă���ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̑�l2�l����", "0�ȏ�̐���" ) + "<br />";
                }
            }

            // ����l1�l
            if ( CheckString.onlySpaceCheck( frmSub.getAdultOne() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�y" + chargeModeNm + "�z�̑�l1�l����" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getAdultOne() ) == false) )
                {
                    // ���p�����ȊO�����͂���Ă���ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̑�l1�l����", "0�ȏ�̐���" ) + "<br />";
                }
            }

            // ����l�ǉ�
            if ( CheckString.onlySpaceCheck( frmSub.getAdultAdd() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�y" + chargeModeNm + "�z�̑�l�ǉ�����" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frmSub.getAdultAdd() ) == false) )
                {
                    // ���p�����ȊO�����͂���Ă���ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̑�l�ǉ�����", "0�ȏ�̐���" ) + "<br />";
                }
            }

            if ( rsvcomm.checkLoveHotelFlag( frm.getSelHotelId() ) == false )
            {
                // ���q���ǉ�
                if ( CheckString.onlySpaceCheck( frmSub.getChildAdd() ) == true )
                {
                    // �����́E�󔒕����̏ꍇ
                    msg = msg + Message.getMessage( "warn.00001", "�y" + chargeModeNm + "�z�̎q���ǉ�����" ) + "<br />";
                }
                else
                {
                    if ( (OwnerRsvCommon.numCheck( frmSub.getChildAdd() ) == false) )
                    {
                        // ���p�����ȊO�����͂���Ă���ꍇ
                        msg = msg + Message.getMessage( "warn.30007", "�y" + chargeModeNm + "�z�̎q���ǉ�����", "0�ȏ�̐���" ) + "<br />";
                    }
                }
            }

            // ���Ԕ��l(�S�p50�����ȓ�)
            ret = OwnerRsvCommon.LengthCheck( frmSub.getCoRemarks().trim(), 100 );
            if ( ret == 1 )
            {
                // ����Over�̏ꍇ
                msg = msg + Message.getMessage( "warn.00038", "�y" + chargeModeNm + "�z�̎��Ԕ��l", "50" ) + "<br />";
            }

            // �������l(�S�p50�����ȓ�)
            ret = OwnerRsvCommon.LengthCheck( frmSub.getRemarks().trim(), 100 );
            if ( ret == 1 )
            {
                // ����Over�̏ꍇ
                msg = msg + Message.getMessage( "warn.00038", "�y" + chargeModeNm + "�z�̗������l", "50" ) + "<br />";
            }

            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_MONDAY) == OwnerRsvCommon.CALENDAR_MONDAY )
            {
                mondayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_TUESDAY) == OwnerRsvCommon.CALENDAR_TUESDAY )
            {
                tuesdayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_WEDNESDAY) == OwnerRsvCommon.CALENDAR_WEDNESDAY )
            {
                wednesdayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_THIRTHDAY) == OwnerRsvCommon.CALENDAR_THIRTHDAY )
            {
                thirthdayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_FRIDAY) == OwnerRsvCommon.CALENDAR_FRIDAY )
            {
                fridayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_SATURDAY) == OwnerRsvCommon.CALENDAR_SATURDAY )
            {
                saturdayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_SUNDAY) == OwnerRsvCommon.CALENDAR_SUNDAY )
            {
                sundayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_HOLIDAY) == OwnerRsvCommon.CALENDAR_HOLIDAY )
            {
                holidayCount++;
            }
            if ( (frmSub.getWeekStatus() & OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY) == OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY )
            {
                beforeholidayCount++;
            }
        }
        if ( mondayCount != 1 )
        {
            if ( mondayCount > 0 )
            {
                msg = msg + "�J�����_�[���j�����𕡐��I�����邱�Ƃ͂ł��܂���B<br />";
            }
        }
        if ( tuesdayCount != 1 )
        {
            if ( tuesdayCount > 0 )
            {
                msg = msg + "�J�����_�[�Ηj�����𕡐��I�����邱�Ƃ͂ł��܂���B<br />";
            }
        }
        if ( wednesdayCount != 1 )
        {
            if ( wednesdayCount > 0 )
            {
                msg = msg + "�J�����_�[���j�����𕡐��I�����邱�Ƃ͂ł��܂���B<br />";
            }
        }
        if ( thirthdayCount != 1 )
        {
            if ( thirthdayCount > 0 )
            {
                msg = msg + "�J�����_�[�ؗj�����𕡐��I�����邱�Ƃ͂ł��܂���B<br />";
            }
        }
        if ( fridayCount != 1 )
        {
            if ( fridayCount > 0 )
            {
                msg = msg + "�J�����_�[���j�����𕡐��I�����邱�Ƃ͂ł��܂���B<br />";
            }
        }
        if ( saturdayCount != 1 )
        {
            if ( saturdayCount > 0 )
            {
                msg = msg + "�J�����_�[�y�j�����𕡐��I�����邱�Ƃ͂ł��܂���B<br />";
            }
        }
        if ( sundayCount != 1 )
        {
            if ( sundayCount > 0 )
            {
                msg = msg + "�J�����_�[���j�����𕡐��I�����邱�Ƃ͂ł��܂���B<br />";
            }
        }
        if ( holidayCount != 1 )
        {
            if ( holidayCount > 0 )
            {
                msg = msg + "�J�����_�[�j�������𕡐��I�����邱�Ƃ͂ł��܂���B<br />";
            }
        }
        if ( beforeholidayCount != 1 )
        {
            if ( beforeholidayCount > 0 )
            {
                msg = msg + "�J�����_�[�j�O�������𕡐��I�����邱�Ƃ͂ł��܂���B<br />";
            }
        }

        frm.setErrMsg( msg );

        if ( msg.trim().length() == 0 )
        {
            isCheck = true;
        }

        return isCheck;
    }

    /**
     * �Œ�\����z�`�F�b�N(0�~�̎��̓`�F�b�N���Ȃ�)
     * 
     * @param frm FormOwnerRsvPlanCharge�I�u�W�F�N�g
     * @param logic LogicOwnerRsvPlanCharge�I�u�W�F�N�g
     * @param chashDeposit �Œ�\����z
     * @return true:�Œ�\����z�����Afalse:�Œ�\����z�ȏ�
     */
    private boolean checkCashDeposit(FormOwnerRsvPlanCharge frm, LogicOwnerRsvPlanCharge logic, int chashDeposit) throws Exception
    {
        boolean isCheckDeposit = false;
        ArrayList<FormOwnerRsvPlanChargeSub> frmSubList;
        FormOwnerRsvPlanChargeSub frmSub;
        ReserveCommon rsvcomm = new ReserveCommon();

        frmSubList = frm.getFrmSubList();
        for( int i = 0 ; i < frmSubList.size() ; i++ )
        {
            frmSub = frmSubList.get( i );

            // �Œ�\����z������
            if ( Integer.parseInt( frmSub.getAdultTwo() ) != 0 && Integer.parseInt( frmSub.getAdultTwo() ) < chashDeposit )
            {
                // ��l2�l����
                isCheckDeposit = true;
                break;
            }
            if ( Integer.parseInt( frmSub.getAdultOne() ) != 0 && Integer.parseInt( frmSub.getAdultOne() ) < chashDeposit )
            {
                // ��l��l����
                isCheckDeposit = true;
                break;
            }
            if ( Integer.parseInt( frmSub.getAdultAdd() ) != 0 && Integer.parseInt( frmSub.getAdultAdd() ) < chashDeposit )
            {
                // ��l�ǉ�����
                isCheckDeposit = true;
                break;
            }
            if ( rsvcomm.checkLoveHotelFlag( frm.getSelHotelId() ) == false )
            {
                if ( Integer.parseInt( frmSub.getChildAdd() ) != 0 && Integer.parseInt( frmSub.getChildAdd() ) < chashDeposit )
                {
                    // �q���ǉ�����
                    isCheckDeposit = true;
                    break;
                }
            }
        }

        return(isCheckDeposit);
    }
}