package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvPlan;
import jp.happyhotel.owner.FormOwnerRsvPlanManage;
import jp.happyhotel.owner.FormOwnerRsvPlanOptionSub;
import jp.happyhotel.owner.FormOwnerRsvPlanSub;
import jp.happyhotel.owner.LogicOwnerRsvManage;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvPlan;
import jp.happyhotel.owner.LogicOwnerRsvPlanManage;

/**
 * 
 * �v�����ݒ�Ǘ����
 */
public class ActionOwnerRsvPlanManage extends BaseAction
{
    private RequestDispatcher   requestDispatcher = null;
    private static final int    menuFlg_top       = 3;
    private static final int    menuFlg_new       = 31;

    private static final String BTN_ON            = "�ꎞ�I�ɒ�~����";
    private static final String BTN_OFF           = "�̔����ɂ���";
    private static final int    BTN_ON_VALUE      = 0;

    // �����ꂽ�{�^���̔��ʒl
    private static final int    BTN_VIEW          = 1;
    private static final int    BTN_ADD           = 2;
    private static final int    BTN_VIEWCHANGE    = 3;
    private static final int    BTN_EDIT          = 4;
    private static final int    BTN_DRAFT         = 5;
    private static final int    BTN_NO_DISP       = 6;
    private static final int    BTN_PLAN_SALES    = 7;
    private static final int    BTN_DEL           = 8;

    // ���������[�h
    private static final int    DRAFT_ON          = 1;         // ������
    private static final int    DRAFT_OFF         = 0;         // �ʏ�

    private static final int    RESERVE_CODE      = 1000007;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerRsvPlanManage frm;
        LogicOwnerRsvPlanManage logic;
        LogicOwnerRsvPlan logicPlan;
        FormOwnerRsvPlan frmPlan;
        FormOwnerRsvPlanSub frmPlanSub;
        int paramHotelID = 0;
        int paramUserId = 0;
        int paramViewMode = 0;
        int selBtn = 0;
        int menuFlg = 0;
        int imediaFlg = 0;
        int newViewMode = 0;
        String premiumGoAheadDays = "";
        String loginHotelId = "";
        String errMsg = "";
        String hotenaviId = "";
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();
        DataMasterPoint dmp;

        try
        {
            frmMenu = new FormOwnerBkoMenu();
            frm = new FormOwnerRsvPlanManage();
            logic = new LogicOwnerRsvPlanManage();
            frmPlan = new FormOwnerRsvPlan();
            logicPlan = new LogicOwnerRsvPlan();
            frmPlanSub = new FormOwnerRsvPlanSub();

            // ��ʂ̒l���擾
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );

            // ���O�C�����[�U�ƒS���z�e���̃`�F�b�N
            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            if ( (paramHotelID != 0) && (OwnerRsvCommon.checkHotelID( hotenaviId, paramUserId, paramHotelID ) == false) )
            {
                // �Ǘ��O�̃z�e���̓��O�C����ʂ֑J��
                response.sendRedirect( "../../owner/index.jsp" );
                return;
            }

            if ( request.getParameter( "planView" ) != null )
            {
                // ���f�ڃv�����{�^���N���b�N
                selBtn = BTN_VIEW;
            }
            else if ( request.getParameter( "planAdd" ) != null )
            {
                // �V�K�o�^�{�^���N���b�N
                selBtn = BTN_ADD;
            }
            else if ( request.getParameter( "viewChange" ) != null )
            {
                // �\�����ύX�{�^���N���b�N
                selBtn = BTN_VIEWCHANGE;
            }
            else if ( request.getParameter( "draftEdit" ) != null )
            {
                // �������ҏW�{�^���N���b�N
                selBtn = BTN_DRAFT;
            }
            else if ( (request.getParameter( "planId" ) != null) && (Integer.parseInt( request.getParameter( "edit" ) ) == 1) )
            {
                // �ҏW�����N�N���b�N
                selBtn = BTN_EDIT;
            }
            else if ( (request.getParameter( "planId" ) != null) && (request.getParameter( "planNoDisp" ) != null) )
            {
                // ��f�ڃ{�^���N���b�N
                selBtn = BTN_NO_DISP;
            }
            else if ( request.getParameter( "planSales" ) != null )
            {
                // �̔��E��~�{�^���N���b�N
                selBtn = BTN_PLAN_SALES;
            }
            else if ( (request.getParameter( "planId" ) != null) && (request.getParameter( "planDel" ) != null) )
            {
                // ��f�ڃ{�^���N���b�N
                selBtn = BTN_DEL;
            }

            // �l���t�H�[���ɃZ�b�g
            frm.setSelHotelId( paramHotelID );
            frm.setUserId( paramUserId );
            frmPlan.setMode( selBtn );
            frmPlan.setDraftMode( DRAFT_OFF );

            // ���j���[�A�\����̐ݒ�
            frmMenu.setUserId( paramUserId );

            // �����ǂ��擾����
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

            // �v���~�A����s�\������擾
            premiumGoAheadDays = OwnerRsvCommon.getPremiumGoAheadDays();
            frmPlan.setPremiumGoAheadDays( premiumGoAheadDays );

            // �v�����ҏW���ɔ��f�����邽�߂ɃZ�b�g�i2013/01/07�j
            dmp = new DataMasterPoint();
            dmp.getData( RESERVE_CODE );
            frmPlan.setDefaultPoint( dmp.getAddPoint() );
            frmPlan.setImediaFlg( imediaflag );

            switch( selBtn )
            {
                case BTN_ADD:
                    // �V�K�o�^�{�^���N���b�N��
                    menuFlg = menuFlg_new;
                    frmPlan.setSelHotelID( paramHotelID );
                    setNewPage( frmPlan, frmPlanSub, logicPlan );
                    request.setAttribute( "FORM_Plan", frmPlan );
                    break;

                case BTN_EDIT:
                    // �ҏW�����N�N���b�N��
                    menuFlg = menuFlg_new;
                    frmPlan.setSelHotelID( paramHotelID );
                    frmPlan.setSelPlanID( Integer.parseInt( request.getParameter( "planId" ) ) );
                    setEditPage( frmPlan, frmPlanSub, logicPlan, Integer.parseInt( request.getParameter( "planId" ) ) );
                    request.setAttribute( "FORM_Plan", frmPlan );
                    break;

                case BTN_VIEWCHANGE:
                    // �\�����ύX�{�^���N���b�N��
                    changeDispIndex( frm, logic, request );
                    menuFlg = menuFlg_top;
                    request.setAttribute( "FORM_PlanManage", frm );
                    break;

                case BTN_DRAFT:
                    // �������ҏW�{�^���N���b�N��
                    menuFlg = menuFlg_new;
                    frmPlan.setSelHotelID( paramHotelID );
                    setDraftEditPage( frmPlan, frmPlanSub, logicPlan );
                    request.setAttribute( "FORM_Plan", frmPlan );
                    break;

                case BTN_NO_DISP:

                    // �v����ID�̍폜�O�ɁA�\��f�[�^�̃`�F�b�N���s��
                    // �\�񂪓����Ă��Ȃ����H
                    if ( logicPlan.existReserve( paramHotelID, Integer.parseInt( request.getParameter( "planId" ) ) ) == false )
                    {
                        if ( logicPlan.noPublishPlan( paramHotelID, Integer.parseInt( request.getParameter( "planId" ) ) ) == false )
                        {
                            errMsg = Message.getMessage( "warn.30044", "�v������f�ڃG���[" );
                        }
                    }
                    else
                    {
                        // �\��f�[�^�����邽�߁A�폜�ł��Ȃ��G���[���Z�b�g
                        errMsg = Message.getMessage( "warn.30043", "�\�񂠂�G���[" );
                    }
                    // �ĕ\��
                    frm.setErrMsg( errMsg );
                    logic.setFrm( frm );
                    logic.getPlan( 1 );
                    frm = logic.getFrm();
                    frm.setViewMode( 1 );

                    // �v�����ݒ�
                    menuFlg = menuFlg_top;
                    request.setAttribute( "FORM_PlanManage", frm );

                    break;

                case BTN_PLAN_SALES:
                    paramViewMode = Integer.parseInt( request.getParameter( "view" ).toString() );
                    updPlanSalesFlg( paramHotelID, Integer.parseInt( request.getParameter( "planId" ) ), Integer.parseInt( request.getParameter( "salesFlag" ) ), paramUserId );
                    logic.setFrm( frm );
                    logic.getPlan( 1 );
                    frm = logic.getFrm();
                    frm.setViewMode( 1 );

                    // �v�����ݒ�
                    menuFlg = menuFlg_top;
                    request.setAttribute( "FORM_PlanManage", frm );

                    break;

                case BTN_DEL:

                    // �v����ID�̍폜�O�ɁA�\��f�[�^�̃`�F�b�N���s��
                    // �\�񂪓����Ă��Ȃ����H
                    if ( logicPlan.existReserve( paramHotelID, Integer.parseInt( request.getParameter( "planId" ) ) ) == false )
                    {
                        if ( logicPlan.delPublishPlan( paramHotelID, Integer.parseInt( request.getParameter( "planId" ) ) ) == false )
                        {
                            errMsg = Message.getMessage( "warn.30044", "�v�����폜�G���[" );
                        }
                    }
                    else
                    {
                        // �\��f�[�^�����邽�߁A�폜�ł��Ȃ��G���[���Z�b�g
                        errMsg = Message.getMessage( "warn.30043", "�\�񂠂�G���[" );
                    }
                    // �ĕ\��
                    frm.setErrMsg( errMsg );
                    logic.setFrm( frm );
                    logic.getPlan( 0 );// ���ԊO�̂ݍ폜�̂���
                    frm = logic.getFrm();
                    frm.setViewMode( 1 );

                    // �v�����ݒ�
                    menuFlg = menuFlg_top;
                    request.setAttribute( "FORM_PlanManage", frm );

                    break;

                default:
                    // ���f�ڃ{�^���N���b�N��
                    paramViewMode = Integer.parseInt( request.getParameter( "viewMode" ).toString() );
                    menuFlg = menuFlg_top;
                    frm.setSelHotelId( paramHotelID );
                    logic.setFrm( frm );
                    if ( paramViewMode == OwnerRsvCommon.PLAN_VIEW_PART )
                    {
                        // ���f�ڃv�������\������
                        logic.getPlan( OwnerRsvCommon.PLAN_VIEW_ALL );
                        newViewMode = OwnerRsvCommon.PLAN_VIEW_ALL;
                    }
                    else
                    {
                        // ���f�ڃv�����͕\�����Ȃ�
                        logic.getPlan( OwnerRsvCommon.PLAN_VIEW_PART );
                        newViewMode = OwnerRsvCommon.PLAN_VIEW_PART;
                    }
                    frm.setViewMode( newViewMode );
                    request.setAttribute( "FORM_PlanManage", frm );
                    break;
            }

            // ��ʑJ��
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );
            request.setAttribute( "FORM_Menu", frmMenu );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvPlanManage.execute() ][hotelId = " + paramHotelID + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvPlanManage.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            logicPlan = null;
        }
    }

    /**
     * �V�K�o�^�y�[�W�֑J�ڂ��邽�߂̉�ʐݒ菈��
     * 
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @param logic LogicOwnerRsvPlan�I�u�W�F�N�g
     * @return �Ȃ�
     */
    private void setNewPage(FormOwnerRsvPlan frm, FormOwnerRsvPlanSub frmPlanSub, LogicOwnerRsvPlan logic) throws Exception
    {
        int hapiPoint = 0;
        String salesEndDate = "";
        ArrayList<Integer> selSeqList = new ArrayList<Integer>();

        // �v�����E�����ݒ�f�[�^�擾
        logic.setFrm( frm );
        logic.setFrmSub( frmPlanSub );
        logic.getNewPlanRoom();

        // �n�s�[�t�^�̏����l
        hapiPoint = OwnerRsvCommon.getInitHapyPoint( 1 );
        frm.setDefaultPoint( hapiPoint );
        if ( frm.getImediaFlg() == OwnerRsvCommon.IMEDIAFLG_IMEDIA )
        {
            frm.setPointKbn( OwnerRsvCommon.POINT_KIND_FIX );
            frm.setPointFix( Integer.toString( hapiPoint ) );
        }

        // �K�{�I�v�V�����̐ݒ�
        logic.setSubMustOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 1 );
        frm.setMustOptions( "" );

        // �ʏ�I�v�V�����̐ݒ�
        logic.setSubCommOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 0 );
        frm.setCommOptions( "" );

        // �����w��̏����l
        frm.setOfferKbn( 2 );

        // �v�����̌f��
        frm.setPublishingFlg( 1 );

        // �v�]���͍��ڕ\��
        frm.setUserRequestFlg( 0 );

        // �I���ς݊Ǘ��ԍ����X�g�̐ݒ�
        for( int i = 0 ; i < frm.getPlanSeq().size() ; i++ )
        {
            selSeqList.add( 0 );
        }

        // �������擾
        logic.getPlanCharge( frm );

        // �t�H�[���ɃZ�b�g
        frm.setDispIndex( "" );
        frm.setSalesBtnValue( BTN_OFF );
        frm.setSalesFlag( BTN_ON_VALUE );
        frm.setSalesEndDate( salesEndDate );
        frm.setFrmSubList( logic.getSubFormList() );
        frm.setFrmMustOptionSubList( logic.getSubMustOptionFormList() );
        frm.setFrmCommOptionSubList( logic.getSubCommOptionFormList() );
    }

    /**
     * �v�����ύX�y�[�W�֑J�ڂ��邽�߂̉�ʐݒ菈��
     * 
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @param logic LogicOwnerRsvPlan�I�u�W�F�N�g
     * @param planId �v����ID
     * @return �Ȃ�
     */
    private void setEditPage(FormOwnerRsvPlan frm, FormOwnerRsvPlanSub frmPlanSub, LogicOwnerRsvPlan logic, int planId) throws Exception
    {
        int salesFlg = 0;
        int baseSeq = 0;
        String mustOpt = "";
        String commOpt = "";
        boolean isExistsRsv = false;
        ArrayList<Integer> selSeqList = new ArrayList<Integer>();
        ArrayList<FormOwnerRsvPlanSub> regFrmSubList = new ArrayList<FormOwnerRsvPlanSub>();
        ArrayList<FormOwnerRsvPlanSub> newFrmSubList = new ArrayList<FormOwnerRsvPlanSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> allMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> regMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> newMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> allCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> regCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> newCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();

        // ���o�^�ς݃v�������̎擾
        logic.setFrm( frm );
        logic.setFrmSub( frmPlanSub );
        logic.getPlan_PlanId( frm.getSelPlanID(), DRAFT_OFF );
        salesFlg = logic.getSalesFlg();
        regFrmSubList = logic.getSubFormList();

        // ���v�����E�����ݒ�f�[�^�擾
        logic.setSubFormList( new ArrayList<FormOwnerRsvPlanSub>() );
        logic.getNewPlanRoom();

        // ���K�p�����̃f�[�^�ݒ�
        for( int i = 0 ; i < logic.getSubFormList().size() ; i++ )
        {
            FormOwnerRsvPlanSub frmSub = logic.getSubFormList().get( i );
            baseSeq = frmSub.getSeq();

            for( int j = 0 ; j < regFrmSubList.size() ; j++ )
            {
                FormOwnerRsvPlanSub regFrmSub = regFrmSubList.get( j );
                if ( baseSeq == regFrmSub.getSeq() )
                {
                    frmSub.setCheck( 1 );
                    frmSub.setTekiyoDateFrom( regFrmSub.getTekiyoDateFrom() );
                    frmSub.setTekiyoDateTo( regFrmSub.getTekiyoDateTo() );
                }
            }
            newFrmSubList.add( frmSub );
        }

        // �I���ς݊Ǘ��ԍ����X�g�̐ݒ�
        for( int i = 0 ; i < frm.getPlanSeq().size() ; i++ )
        {
            selSeqList.add( 0 );
        }

        // ���K�{�I�v�V�������擾
        logic.setSubMustOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 1 );
        allMustOptFrmSubList = logic.getSubMustOptionFormList();

        // �o�^�ςݕK�{�I�v�V�����̎擾
        logic.setSubMustOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getOption( 1, DRAFT_OFF );
        regMustOptFrmSubList = logic.getSubMustOptionFormList();
        for( int i = 0 ; i < regMustOptFrmSubList.size() ; i++ )
        {
            if ( mustOpt.trim().length() != 0 )
            {
                mustOpt = mustOpt + "<br />";
            }
            mustOpt = mustOpt + regMustOptFrmSubList.get( i ).getOptionNm()
                    + "(" + regMustOptFrmSubList.get( i ).getOptionSubNm() + ")";
        }
        frm.setMustOptions( mustOpt );

        // �f�[�^�}�[�W
        for( int i = 0 ; i < allMustOptFrmSubList.size() ; i++ )
        {
            FormOwnerRsvPlanOptionSub optionSub = allMustOptFrmSubList.get( i );
            if ( optionSub.getErrMsg().trim().length() != 0 )
            {
                newMustOptFrmSubList.add( optionSub );
                break;
            }
            for( int j = 0 ; j < regMustOptFrmSubList.size() ; j++ )
            {
                FormOwnerRsvPlanOptionSub regOptFrmSub = regMustOptFrmSubList.get( j );
                if ( optionSub.getOptionID() == regOptFrmSub.getOptionID() )
                {
                    optionSub.setCheck( 1 );
                }
            }
            newMustOptFrmSubList.add( optionSub );
        }

        // ���ʏ�I�v�V�������擾
        logic.setSubCommOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 0 );
        allCommOptFrmSubList = logic.getSubCommOptionFormList();

        // �o�^�ςݕK�{�I�v�V�����̎擾
        logic.setSubCommOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getOption( 0, DRAFT_OFF );
        regCommOptFrmSubList = logic.getSubCommOptionFormList();
        for( int i = 0 ; i < regCommOptFrmSubList.size() ; i++ )
        {
            if ( commOpt.trim().length() != 0 )
            {
                commOpt = commOpt + "<br />";
            }
            commOpt = commOpt + regCommOptFrmSubList.get( i ).getOptionNm();
        }
        frm.setCommOptions( commOpt );

        // �f�[�^�}�[�W
        for( int i = 0 ; i < allCommOptFrmSubList.size() ; i++ )
        {
            FormOwnerRsvPlanOptionSub optionSub = allCommOptFrmSubList.get( i );
            if ( optionSub.getErrMsg().trim().length() != 0 )
            {
                newCommOptFrmSubList.add( optionSub );
                break;
            }
            for( int j = 0 ; j < regCommOptFrmSubList.size() ; j++ )
            {
                FormOwnerRsvPlanOptionSub regOptFrmSub = regCommOptFrmSubList.get( j );
                if ( optionSub.getOptionID() == regOptFrmSub.getOptionID() )
                {
                    optionSub.setCheck( 1 );
                }
            }
            newCommOptFrmSubList.add( optionSub );
        }

        // ���������擾
        logic.getPlanCharge( frm );

        // ���\��f�[�^�����݂��邩
        isExistsRsv = OwnerRsvCommon.isExistsRsvPlan( frm.getSelHotelID(), frm.getSelPlanID() );
        frm.setExistsRsv( isExistsRsv );
        if ( isExistsRsv == true )
        {
            frm.setInfoMsg( Message.getMessage( "warn.30019" ) );
        }

        // ���t�H�[���ɃZ�b�g
        frm.setSalesFlag( salesFlg );
        if ( salesFlg == 0 )
        {
            frm.setSalesBtnValue( BTN_OFF );
        }
        else
        {
            frm.setSalesBtnValue( BTN_ON );
        }
        frm.setFrmSubList( logic.getSubFormList() );
        frm.setFrmMustOptionSubList( newMustOptFrmSubList );
        frm.setFrmCommOptionSubList( newCommOptFrmSubList );
    }

    /**
     * �\�����ύX����
     * 
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @param logic LogicOwnerRsvPlanManage�I�u�W�F�N�g
     * @param req HttpServletRequest
     * @return �Ȃ�
     */
    private void changeDispIndex(FormOwnerRsvPlanManage frm, LogicOwnerRsvPlanManage logic, HttpServletRequest req) throws Exception
    {

        ArrayList<Integer> planIdList = new ArrayList<Integer>();
        ArrayList<String> planNmList = new ArrayList<String>();
        ArrayList<String> dispIdxList = new ArrayList<String>();
        String msg = "";

        // ��ʂ̃v����ID�̎擾
        String planIds[] = req.getParameterValues( "planId" );
        String planNms[] = req.getParameterValues( "planNm" );
        if ( planIds != null )
        {
            for( int i = 0 ; i < planIds.length ; i++ )
            {
                planIdList.add( Integer.parseInt( planIds[i] ) );
                planNmList.add( planNms[i] );
            }
        }

        // ��ʂ̕\�����̎擾
        String dispIdxs[] = req.getParameterValues( "dispIndex" );
        if ( dispIdxs != null )
        {
            for( int i = 0 ; i < dispIdxs.length ; i++ )
            {
                dispIdxList.add( dispIdxs[i] );
            }
        }

        // ���͒l�`�F�b�N
        msg = checkInputValue( dispIdxList, planNmList );
        if ( msg.trim().length() == 0 )
        {
            // �X�V����
            msg = "";
            for( int i = 0 ; i < dispIdxList.size() ; i++ )
            {
                if ( logic.changeDispIdx( frm.getSelHotelId(), planIdList.get( i ), Integer.parseInt( dispIdxList.get( i ) ), frm.getUserId() ) == false )
                {
                    // �X�V���s
                    msg = msg + Message.getMessage( "erro.30001", "�X�V�Ώ�" );
                }
            }
        }

        // �ĕ\��
        frm.setErrMsg( msg );
        logic.setFrm( frm );
        logic.getPlan( 1 );
        frm = logic.getFrm();
        frm.setViewMode( 1 );
    }

    /**
     * �������ҏW��ʑJ�ڂ��邽�߂̉�ʐݒ菈��
     * 
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @param logic LogicOwnerRsvPlan�I�u�W�F�N�g
     * @return �Ȃ�
     */
    private void setDraftEditPage(FormOwnerRsvPlan frm, FormOwnerRsvPlanSub frmPlanSub, LogicOwnerRsvPlan logic) throws Exception
    {
        int hapiPoint = 0;
        String salesEndDate = "";
        ArrayList<Integer> selSeqList = new ArrayList<Integer>();

        // ���������[�h�ݒ�
        frm.setDraftMode( DRAFT_ON );

        // �������v�������擾
        logic.setFrm( frm );
        logic.getDraftPlanIdList();
        logic.getDraftPlanNmList();

        // �v�����E�����ݒ�f�[�^�擾
        logic.setFrmSub( frmPlanSub );
        logic.getNewPlanRoom();

        // �n�s�[�t�^�̏����l
        hapiPoint = OwnerRsvCommon.getInitHapyPoint( 1 );
        frm.setDefaultPoint( hapiPoint );
        if ( frm.getImediaFlg() == OwnerRsvCommon.IMEDIAFLG_IMEDIA )
        {
            frm.setPointKbn( OwnerRsvCommon.POINT_KIND_FIX );
            frm.setPointFix( Integer.toString( hapiPoint ) );
        }

        // �K�{�I�v�V�����̐ݒ�
        logic.setSubMustOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 1 );
        frm.setMustOptions( "" );

        // �ʏ�I�v�V�����̐ݒ�
        logic.setSubCommOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 0 );
        frm.setCommOptions( "" );

        // �����w��̏����l
        frm.setOfferKbn( 2 );

        // �v�����̌f��
        frm.setPublishingFlg( 1 );

        // �v�]���͍��ڕ\��
        frm.setUserRequestFlg( 0 );

        // �I���ς݊Ǘ��ԍ����X�g�̐ݒ�
        for( int i = 0 ; i < frm.getPlanSeq().size() ; i++ )
        {
            selSeqList.add( 0 );
        }

        // �t�H�[���ɃZ�b�g
        frm.setDispIndex( "" );
        /*
         * frm.setSalesBtnValue( BTN_OFF );
         * frm.setSalesFlag( BTN_ON_VALUE );
         */
        frm.setSalesEndDate( salesEndDate );
        frm.setFrmSubList( logic.getSubFormList() );
        frm.setFrmMustOptionSubList( logic.getSubMustOptionFormList() );
        frm.setFrmCommOptionSubList( logic.getSubCommOptionFormList() );
    }

    /**
     * �\�����ύX���̓��͒l�`�F�b�N
     * 
     * @param dispIdxList ��ʂœ��͂��ꂽ�\����
     * @param plnNmList �v������
     * @return �G���[���b�Z�[�W
     */
    private String checkInputValue(ArrayList<String> dispIdxList, ArrayList<String> plnNmList) throws Exception
    {
        String msg = "";

        for( int i = 0 ; i < dispIdxList.size() ; i++ )
        {
            if ( CheckString.onlySpaceCheck( dispIdxList.get( i ) ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�y" + plnNmList.get( i ) + "�z��" + "�\����" ) + "<br />";
            }
            else
            {

                if ( (OwnerRsvCommon.numCheck( dispIdxList.get( i ) ) == false) )
                {
                    // ���p�����ȊO�����͂���Ă���ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�y" + plnNmList.get( i ) + "�z��" + "�\����", "1�ȏ�̐���" ) + "<br />";
                }
            }
        }

        return(msg);
    }

    /**
     * �v�����󋵂̔̔��t���O�X�V
     * 
     * @param logic LogicOwnerRsvPlanManage�I�u�W�F�N�g
     * @para, hotelId �z�e��ID
     * @param planId �v����ID
     * @param salesFlg �̔��t���O
     * @param userId ���[�U�[ID
     * @return �Ȃ�
     */
    private void updPlanSalesFlg(int hotelId, int planId, int salesFlg, int userId) throws Exception
    {
        LogicOwnerRsvManage logic = new LogicOwnerRsvManage();
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

}
