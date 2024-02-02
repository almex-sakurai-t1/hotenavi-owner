package jp.happyhotel.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.data.DataRsvPlanCharge;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvPlan;
import jp.happyhotel.owner.FormOwnerRsvPlanManage;
import jp.happyhotel.owner.FormOwnerRsvPlanOptionSub;
import jp.happyhotel.owner.FormOwnerRsvPlanSub;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvPlan;
import jp.happyhotel.owner.LogicOwnerRsvPlanManage;

/**
 * 
 * �v�����ݒ���
 */
public class ActionOwnerRsvPlan extends BaseAction
{
    private RequestDispatcher   requestDispatcher = null;

    // �������敪
    private static final int    DRAFT_OFF         = 0;         // �ʏ�
    private static final int    DRAFT_ON          = 1;         // ������

    // �o�^���[�h(ActionOwnerRsvPlanManage�Őݒ肵�Ă���u�����ꂽ�{�^���̔��ʒl�v�Ɠ��l)
    private static final int    MODE_NEW          = 2;         // �V�K
    private static final int    MODE_EDIT         = 4;         // �X�V

    private static final int    menuFlg_MANAGE    = 3;
    private static final int    menuFlg_REGIST    = 31;
    private static final String BTN_ON            = "�ꎞ�I�ɒ�~����";
    private static final String BTN_OFF           = "�̔����ɂ���";

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerRsvPlan frm;
        FormOwnerRsvPlanManage frmManage;
        LogicOwnerRsvPlan logic;
        LogicOwnerRsvPlanManage logicManage;
        int hotelID = 0;
        boolean inputCheck = false;
        int selBtn = 0;
        int paramPlanId = 0;
        boolean checkplancharge = false;
        int roomseq = 0;
        String errMsg = "";
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        String hotenaviId = "";
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();
        int paramUserId = 0;

        Logging.info( "[ActionOwnerRsvPlan.execute() ][request.getParameter( 'mode' ) = " + request.getParameter( "mode" ) + "]" );

        try
        {
            frmMenu = new FormOwnerBkoMenu();
            frm = new FormOwnerRsvPlan();
            logic = new LogicOwnerRsvPlan();

            // ��ʂ̒l���擾
            setFormInputParam( request, frm );
            hotelID = frm.getSelHotelID();
            logic.setFrm( frm );

            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
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

            if ( request.getParameter( "btnSales" ) != null )
            {
                // �ꎞ�I�ɒ�~����{�^���N���b�N
                selBtn = OwnerRsvCommon.BTN_SALES;
            }
            else if ( request.getParameter( "btnRegist" ) != null )
            {
                // �v�����ݒ�X�V�{�^���N���b�N
                selBtn = OwnerRsvCommon.BTN_REGIST;
            }
            else if ( request.getParameter( "btnCopyRegist" ) != null )
            {
                // �v�����ݒ�R�s�[
                selBtn = OwnerRsvCommon.BTN_COPYREGIST;
            }
            else if ( request.getParameter( "btnCopy" ) != null )
            {
                selBtn = OwnerRsvCommon.BTN_COPY;
            }
            else if ( request.getParameter( "btnDraftSave" ) != null )
            {
                // �������ۑ��{�^���N���b�N
                selBtn = OwnerRsvCommon.BTN_DRAFT;
            }
            else if ( request.getParameter( "changePlan" ) != null )
            {
                // �\���{�^���N���b�N
                selBtn = OwnerRsvCommon.BTN_VIEW;
            }
            else if ( request.getParameter( "btnDraftDel" ) != null )
            {
                // �������폜�{�^���N���b�N
                selBtn = OwnerRsvCommon.BTN_DEL;
            }
            else if ( request.getParameter( "btnDraftRegist" ) != null )
            {
                // ������ �v�����ݒ�X�V�{�^���N���b�N
                selBtn = OwnerRsvCommon.BTN_DRAFTUPD;
            }
            else if ( request.getParameter( "btnPreView" ) != null && request.getParameter( "btnPreView" ).equals( "" ) == false )
            {
                // �v���r���[�{�^���N���b�N
                selBtn = OwnerRsvCommon.BTN_PREVIEW;
            }
            else if ( request.getParameter( "btnBack" ) != null )
            {
                // �߂�{�^���N���b�N
                selBtn = OwnerRsvCommon.BTN_BACK;
            }
            else if ( request.getParameter( "previewDetail" ) != null )
            {
                // �v���r���[�ڍו\��
                selBtn = OwnerRsvCommon.BTN_PREVIEW_DETAIL;
                if ( request.getParameter( "roomSeq" ) != null )
                {
                    if ( request.getParameter( "roomSeq" ).equals( "" ) == false && CheckString.numCheck( request.getParameter( "roomSeq" ) ) == true )
                    {
                        roomseq = Integer.parseInt( request.getParameter( "roomSeq" ) );
                    }
                }
            }

            if ( disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false &&
                    (selBtn == OwnerRsvCommon.BTN_PREVIEW || selBtn == OwnerRsvCommon.BTN_PREVIEW_DETAIL) == false )
            {
                // �����̂Ȃ����[�U�̓G���[�y�[�W��\������
                errMsg = Message.getMessage( "erro.30001", "�X�e�[�^�X���X�V���錠��" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // �������s
            switch( selBtn )
            {
                case OwnerRsvCommon.BTN_SALES:
                    // ���ꎞ�I�ɒ�~����{�^���N���b�N
                    if ( frm.getSelPlanID() == 0 )
                    {
                        // �v����ID�����ݒ�̏ꍇ�́A�V�K�o�^�Ɠ��l�̈���
                        frm = execReviewPlan( request, logic, frm, DRAFT_OFF );

                    }
                    else
                    {
                        // �̔��t���O�X�V
                        frm = execSalesMode( request, logic, frm, DRAFT_OFF );
                        // �z�e���C������
                        OwnerRsvCommon.addAdjustmentHistory( frm.getSelHotelID(), OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                frm.getUserId(), OwnerRsvCommon.ADJUST_EDIT_ID_RSV_PLAN,
                                frm.getSelPlanID() * 10 + frm.getSalesFlag(),
                                frm.getSalesFlag() == 0 ? OwnerRsvCommon.ADJUST_MEMO_RSV_STOP_PLAN : OwnerRsvCommon.ADJUST_MEMO_RSV_START_PLAN );
                    }
                    request.setAttribute( "FORM_Plan", frm );
                    OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );
                    break;

                case OwnerRsvCommon.BTN_DRAFT:
                    // ���������ۑ�
                    // ���̓`�F�b�N
                    frm.setDraftMode( DRAFT_ON );
                    if ( isInputCheckMsg( frm ) == true )
                    {
                        // ���̓`�F�b�N������
                        paramPlanId = Integer.parseInt( request.getParameter( "selPlanId" ) );

                        // �o�^�E�X�V
                        if ( paramPlanId == 0 )
                        {
                            // �V�K�o�^
                            logic.registPlan( MODE_NEW, DRAFT_ON, OwnerRsvCommon.BTN_REGIST );
                        }
                        else
                        {
                            // �X�V
                            logic.registPlan( MODE_EDIT, DRAFT_ON, OwnerRsvCommon.BTN_REGIST );
                        }
                        inputCheck = true;
                    }

                    // ���j���[�ݒ�
                    frmMenu.setUserId( frm.getUserId() );

                    // ���̓G���[�̏ꍇ�A���͒l�͕ێ�����
                    if ( inputCheck == false )
                    {
                        // ��ʍĕ\��
                        frm = execReviewPlan( request, logic, frm, DRAFT_ON );
                        request.setAttribute( "FORM_Plan", frm );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );
                    }
                    else
                    {
                        // ����������ʕ\��
                        frm = execReviewPlan( request, logic, frm, DRAFT_ON );
                        request.setAttribute( "FORM_Plan", frm );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );
                        // frm.setDraftMode( DRAFT_OFF );
                        // // �o�^������
                        // logicManage = new LogicOwnerRsvPlanManage();
                        // frmManage = new FormOwnerRsvPlanManage();
                        // frmManage.setSelHotelId( hotelID );
                        // logicManage.setFrm( frmManage );
                        // logicManage.getPlan( OwnerRsvCommon.PLAN_VIEW_PART );
                        // frmManage.setViewMode( OwnerRsvCommon.PLAN_VIEW_PART );
                        // request.setAttribute( "FORM_PlanManage", frmManage );
                        // OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_MANAGE, request.getCookies() );
                    }
                    break;

                case OwnerRsvCommon.BTN_VIEW:
                    // ���\��
                    frm = execPlanView( request, logic, frm );
                    request.setAttribute( "FORM_Plan", frm );
                    OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );
                    break;

                case OwnerRsvCommon.BTN_DEL:
                    // ���������폜
                    if ( execDelDraft( hotelID, Integer.parseInt( request.getParameter( "selPlanId" ) ), logic, frm ) == false )
                    {
                        // �G���[�̏ꍇ�A��ʍĕ\��
                        frm = execReviewPlan( request, logic, frm, DRAFT_ON );
                        frm.setDraftMode( DRAFT_ON );
                        request.setAttribute( "FORM_Plan", frm );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );

                    }
                    else
                    {
                        // �폜����
                        logicManage = new LogicOwnerRsvPlanManage();
                        frmManage = new FormOwnerRsvPlanManage();
                        frmManage.setSelHotelId( hotelID );
                        logicManage.setFrm( frmManage );
                        logicManage.getPlan( 1 );
                        request.setAttribute( "FORM_PlanManage", frmManage );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_MANAGE, request.getCookies() );
                    }
                    break;

                case OwnerRsvCommon.BTN_DRAFTUPD:
                    // ���������@�v�����ݒ�X�V
                    if ( execRegDraftRelease( hotelID, Integer.parseInt( request.getParameter( "selPlanId" ) ), logic, frm ) == false )
                    {
                        // �G���[�̏ꍇ�A��ʍĕ\��
                        frm = execReviewPlan( request, logic, frm, DRAFT_ON );
                        frm.setDraftMode( DRAFT_ON );
                        request.setAttribute( "FORM_Plan", frm );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );
                    }
                    else
                    {
                        // ��������
                        // �z�e���C������
                        OwnerRsvCommon.addAdjustmentHistory( frm.getSelHotelID(), OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                frm.getUserId(), OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_ADD,
                                frm.getSelPlanID() * 10 + frm.getSalesFlag(), OwnerRsvCommon.ADJUST_MEMO_PLAN_ADD );

                        logicManage = new LogicOwnerRsvPlanManage();
                        frmManage = new FormOwnerRsvPlanManage();
                        frmManage.setSelHotelId( hotelID );
                        logicManage.setFrm( frmManage );
                        logicManage.getPlan( 1 );
                        request.setAttribute( "FORM_PlanManage", frmManage );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_MANAGE, request.getCookies() );
                    }
                    break;

                case OwnerRsvCommon.BTN_BACK:
                    // ���߂�
                    logicManage = new LogicOwnerRsvPlanManage();
                    frmManage = new FormOwnerRsvPlanManage();

                    frmManage.setSelHotelId( hotelID );
                    logicManage.setFrm( frmManage );
                    logicManage.getPlan( OwnerRsvCommon.PLAN_VIEW_PART );
                    frmManage.setViewMode( OwnerRsvCommon.PLAN_VIEW_PART );
                    request.setAttribute( "FORM_PlanManage", frmManage );
                    OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_MANAGE, request.getCookies() );
                    break;
                case OwnerRsvCommon.BTN_PREVIEW:
                    // �v���r���[��ʕ\��
                    if ( isInputCheckMsg( frm ) == true )
                    {
                        if ( frm.getSelPlanID() > 0 )
                        {
                            // �ő�E�ŏ��l���擾
                            logic.getPlanCharge( frm );
                            // �v��������Ӱ�ޖ��̎擾
                            logic.getPlanChargeModeName( frm );
                            // �v��������Ӱ�ނ�������������Ď����̎擾
                            logic.getPlanChargeDetail( frm );
                        }
                        request.setAttribute( "FORM_Plan", frm );
                        requestDispatcher = request.getRequestDispatcher( "owner_rsv_plan_preview1.jsp" );
                        requestDispatcher.forward( request, response );
                        return;
                    }
                    else
                    {
                        // ��ʍĕ\��
                        frm = execReviewPlan( request, logic, frm, DRAFT_OFF );
                        request.setAttribute( "FORM_Plan", frm );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );
                    }
                    break;
                case OwnerRsvCommon.BTN_PREVIEW_DETAIL:
                    // �v���r���[�ڍ׉�ʕ\��
                    if ( frm.getSelPlanID() > 0 )
                    {
                        // �ő�E�ŏ��l���擾
                        logic.getPlanCharge( frm );
                        // �v��������Ӱ�ޖ��̎擾
                        logic.getPlanChargeModeName( frm );
                        // �v��������Ӱ�ނ�������������Ď����̎擾
                        logic.getPlanChargeDetail( frm );
                    }
                    frm.setPlanNm( new String( frm.getPlanNm().getBytes( "8859_1" ), "Windows-31J" ) );
                    frm.setPlanInfo( ReplaceString.DBEscape( new String( frm.getPlanInfo().getBytes( "8859_1" ), "Windows-31J" ) ) );
                    frm.setQuestion( ReplaceString.DBEscape( new String( frm.getQuestion().getBytes( "8859_1" ), "Windows-31J" ) ) );
                    frm.setRemarks( ReplaceString.DBEscape( new String( frm.getRemarks().getBytes( "8859_1" ), "Windows-31J" ) ) );
                    request.setAttribute( "FORM_Plan", frm );
                    request.setAttribute( "roomseq", roomseq );
                    requestDispatcher = request.getRequestDispatcher( "owner_rsv_plan_preview2.jsp" );
                    requestDispatcher.forward( request, response );
                    return;

                default:
                    // �v�����ݒ�X�V
                    // ���̓`�F�b�N
                    if ( isInputCheckMsg( frm ) == true )
                    {
                        // �R�s�[�݂̂̏ꍇ�͌��f�[�^�̍X�V�͍s��Ȃ�
                        if ( selBtn != OwnerRsvCommon.BTN_COPY )
                        {
                            // �o�^�E�X�V
                            // �v�����ݒ�X�V�̏ꍇ
                            logic.registPlan( Integer.parseInt( request.getParameter( "mode" ) ), DRAFT_OFF, OwnerRsvCommon.BTN_REGIST );
                            // �z�e���C������
                            int edit_id = 0;
                            int edit_sub = frm.getSelPlanID();
                            String memo = "";
                            if ( Integer.parseInt( request.getParameter( "mode" ) ) == MODE_NEW ) // �V�K
                            {
                                edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_ADD;
                                memo = OwnerRsvCommon.ADJUST_MEMO_PLAN_ADD;
                            }
                            else
                            {
                                edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_UPD;
                                memo = OwnerRsvCommon.ADJUST_MEMO_PLAN_UPD;
                                // �f�ځE��f�ڂ̕ύX���`�F�b�N
                                if ( frm.getPublishingFlg() != replaceIntValue( request.getParameter( "planDispOld" ) ) )
                                {
                                    OwnerRsvCommon.addAdjustmentHistory( hotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                            frm.getUserId(), edit_id, edit_sub, memo );
                                    edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_UPDWN;
                                    edit_sub = edit_sub * 10 + frm.getPublishingFlg();
                                    memo = (frm.getPublishingFlg() == 0) ? OwnerRsvCommon.ADJUST_MEMO_PLAN_DWN : OwnerRsvCommon.ADJUST_MEMO_PLAN_UP;
                                }
                            }
                            OwnerRsvCommon.addAdjustmentHistory( hotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                    frm.getUserId(), edit_id, edit_sub, memo );
                        }
                        inputCheck = true;
                    }
                    // ���j���[�A�{�ݏ��̐ݒ�
                    frmMenu.setUserId( frm.getUserId() );
                    // ���̓G���[�̏ꍇ�A���͒l�͕ێ�����
                    if ( inputCheck == false )
                    {
                        // ��ʍĕ\��
                        frm = execReviewPlan( request, logic, frm, DRAFT_OFF );
                        request.setAttribute( "FORM_Plan", frm );
                        OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_REGIST, request.getCookies() );
                    }
                    else
                    {
                        // �o�^������
                        if ( selBtn == OwnerRsvCommon.BTN_COPYREGIST || selBtn == OwnerRsvCommon.BTN_COPY )
                        {
                            int oldPlanId = frm.getSelPlanID();
                            // ����t���O�̂�False�ɂ���
                            frm.setSalesFlag( 0 );
                            logic.setFrm( frm );
                            // �R�s�[����(�������e�ŐV�K�o�^)
                            logic.registPlan( 2, DRAFT_OFF, OwnerRsvCommon.BTN_REGIST );
                            OwnerRsvCommon.addAdjustmentHistory( hotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                    frm.getUserId(), OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_ADD, 0, OwnerRsvCommon.ADJUST_MEMO_PLAN_ADD );
                            // �v�����ɕR�Â��ݒ���R�s�[
                            copySetting( frm, oldPlanId );
                        }
                        logicManage = new LogicOwnerRsvPlanManage();
                        frmManage = new FormOwnerRsvPlanManage();
                        frmManage.setSelHotelId( hotelID );
                        logicManage.setFrm( frmManage );
                        logicManage.getPlan( OwnerRsvCommon.PLAN_VIEW_PART );
                        frmManage.setViewMode( OwnerRsvCommon.PLAN_VIEW_PART );
                        for( int i = 0 ; i < frmManage.getExPlanId().size() ; i++ )
                        {
                            if ( frmManage.getExPlanId().get( i ) != null && frmManage.getExPlanId().get( i ) == Integer.parseInt( request.getParameter( "selPlanId" ) ) )
                            {
                                checkplancharge = true;
                                break;
                            }
                        }
                        if ( checkplancharge == true || Integer.parseInt( request.getParameter( "selPlanId" ) ) == 0 )
                        {
                            request.setAttribute( "FORM_PlanManage", frmManage );
                            OwnerRsvCommon.setMenu( frmMenu, frm.getSelHotelID(), menuFlg_MANAGE, request.getCookies() );
                        }
                        else
                        {
                            // ���ڗ����ҏW�y�[�W�֑J��
                            response.sendRedirect( "ownerRsvPlanChargeManage.act?selHotelIDValue=" + hotelID + "&planID=" + request.getParameter( "selPlanId" ) + "&edit=1" );
                            return;
                        }
                    }
                    break;
            }
            // ��ʑJ��
            frmMenu.setUserId( frm.getUserId() );
            request.setAttribute( "FORM_Menu", frmMenu );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvPlan.execute() ][hotelId = " + hotelID + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvPlan.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            logic = null;
        }
    }

    /**
     * �v�����ɕR�t���ݒ�R�s�[����
     * 
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @param oldPlanID �R�s�[���v����ID
     * @return �Ȃ�
     */
    private void copySetting(FormOwnerRsvPlan frm, int oldPlanID)
    {
        try
        {
            // �v�����ʗ����}�X�^�R�s�[
            copyRsvPlanCharge( frm, oldPlanID );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvPlan.copySetting() ][hotelId = " + frm.getSelHotelID() + "] Exception", e );
        }
        finally
        {
        }

        return;
    }

    /**
     * �v�����ʗ����}�X�^�R�s�[����
     * 
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @param newPlanId �R�s�[���v����ID
     * @return �Ȃ�
     */
    private void copyRsvPlanCharge(FormOwnerRsvPlan frm, int oldPlanId)
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataRsvPlanCharge dataPlanCharge = null;

        try
        {
            query = "select charge_mode_id from hh_rsv_plan_charge where id = ? and plan_id = ? ";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, oldPlanId );
            result = prestate.executeQuery();

            while( result.next() != false )
            {
                dataPlanCharge = new DataRsvPlanCharge();
                dataPlanCharge.getData( frm.getSelHotelID(), oldPlanId, result.getInt( "charge_mode_id" ) );
                // �V�����v����ID��INSERT
                dataPlanCharge.setPlanId( frm.getSelPlanID() );
                dataPlanCharge.insertData();
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvPlan.copyRsvPlanCharge() ][hotelId = " + frm.getSelHotelID() + "] Exception", e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return;
    }

    /**
     * �ꎞ�I�ɒ�~����{�^���N���b�N����
     * 
     * @param request HttpServletRequest
     * @param logic LogicOwnerRsvPlan �I�u�W�F�N�g
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @param draftKbn 0:�ʏ�A1:������
     * @return �Ȃ�
     */
    private FormOwnerRsvPlan execSalesMode(HttpServletRequest request, LogicOwnerRsvPlan logic, FormOwnerRsvPlan frm, int draftKbn) throws Exception
    {
        int salesFlag = 0;
        int salesFlagDB = 0;

        salesFlag = Integer.parseInt( request.getParameter( "salesFlag" ) );
        salesFlag = setSalesFlg( logic, salesFlag );
        logic.getPlan_PlanId( frm.getSelPlanID(), draftKbn );

        // ���͒l��ێ�
        setFormInputParam( request, frm );
        logic.setSubFormList( new ArrayList<FormOwnerRsvPlanSub>() );

        if ( salesFlagDB != -1 )
        {
            // �X�V�����̔��t���O���Z�b�g
            frm.setSalesFlag( salesFlag );
        }
        else
        {
            frm.setSalesFlag( 0 );
        }

        // �{�^�������Z�b�g
        frm.setSalesBtnValue( BTN_OFF );
        if ( salesFlag == 1 )
        {
            // �̔����ɂ���
            frm.setSalesBtnValue( BTN_ON );
        }

        // �������擾
        logic.getPlanCharge( frm );
        // �I�v�V�����ݒ�
        createOption( frm, logic, draftKbn );

        return(logic.getFrm());
    }

    /**
     * �v�����ݒ�X�V�N���b�N����(���̓`�F�b�N�G���[��)
     * 
     * @param request HttpServletRequest
     * @param logic LogicOwnerRsvPlan �I�u�W�F�N�g
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @param draftKbn 0:�ʏ�A1:������
     * @return �Ȃ�
     */
    private FormOwnerRsvPlan execReviewPlan(HttpServletRequest request, LogicOwnerRsvPlan logic, FormOwnerRsvPlan frm, int draftKbn) throws Exception
    {
        boolean isExistsRsv = false;

        // �f�[�^�Ď擾
        logic.getPlan_PlanId( frm.getSelPlanID(), draftKbn );

        // ���͒l��ێ�
        setFormInputParam( request, frm );
        logic.setSubFormList( new ArrayList<FormOwnerRsvPlanSub>() );

        if ( draftKbn == 0 )
        {
            // �ʏ�̏ꍇ�A�������擾
            logic.getPlanCharge( frm );
        }
        else
        {
            // �������̏ꍇ�A�������v�������擾
            logic.getDraftPlanIdList();
            logic.getDraftPlanNmList();
        }

        // �I�v�V�����ݒ�
        createOption( frm, logic, draftKbn );

        // �������ݒ�
        createRoomList( frm, logic );

        // �\��f�[�^�����݂��邩
        isExistsRsv = OwnerRsvCommon.isExistsRsvPlan( frm.getSelHotelID(), frm.getSelPlanID() );
        frm.setExistsRsv( isExistsRsv );
        frm.setInfoMsg( "" );
        if ( isExistsRsv == true && draftKbn == 0 )
        {
            frm.setInfoMsg( Message.getMessage( "warn.30019" ) );
        }

        return(logic.getFrm());
    }

    /**
     * �v�����E�����ݒ�f�[�^�擾
     * 
     * @param request HttpServletRequest
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @return �Ȃ�
     * @throws Exception
     */
    private void setFormInputParam(HttpServletRequest request, FormOwnerRsvPlan frm) throws Exception
    {
        int paramHotelId = 0;
        int paramUserId = 0;
        int paramPlanId = 0;
        String paraOwnerHotelID = "";
        String paramDispIndex = "";
        String paramPlanNm = "";
        String paramStrDispFrom = "";
        String paramStrDispTo = "";
        String paramStrSalesFrom = "";
        String paramStrOrgDispFrom = "";
        String paramStrOrgDispTo = "";
        String paramStrOrgSalesFrom = "";
        String paramRsvEndDay = "";
        String paramRsvEndHH = "";
        String paramRsvEndMM = "";
        String paramRsvStartDay = "";
        String paramRsvStartHH = "";
        String paramRsvStartMM = "";
        String paramAdultNum = "";
        String paramChildNum = "";
        String paramMinAdultNum = "";
        String paramMinChildNum = "";
        String paramLongStayNum = "";
        String paramMinManNum = "0";
        String paramMaxManNum = "0";
        String paramMinWomanNum = "0";
        String paramMaxWomanNum = "0";
        int paramManCountFlg = 0;
        int paramHapyKbn = 0;
        String paramRoomPoint = "";
        String paramFixPoint = "";
        String paramQuestion = "";
        int paramQuestionFlag = 0;
        String paramPlanInfo = "";
        String paramRemarks = "";
        String paramQuantity = "";
        String paramFileNm = "";
        int paramImediaFlg = 0;
        int paramMode = 0;
        int paramOfferKind = 0;
        int paramSalesFlg = 0;
        int paramDefaultPoint = 0;
        int planDisp = 0;
        int userRequest = 0;
        int paramSalesStopWeekStatus = 0;
        int selSeq = 0;
        boolean isExistsRsv = false;
        FormOwnerRsvPlanSub frmSub;
        ArrayList<Integer> seqList = new ArrayList<Integer>();
        ArrayList<Integer> mustOptList = new ArrayList<Integer>();
        ArrayList<Integer> commOptList = new ArrayList<Integer>();
        ArrayList<FormOwnerRsvPlanSub> frmSubList = new ArrayList<FormOwnerRsvPlanSub>();

        // ��ʂ̒l���擾
        if ( request.getParameter( "selHotelIDValue" ) != null )
        {
            paramHotelId = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
        }
        paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
        paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
        if ( request.getParameter( "mode" ) != null )
        {
            paramMode = Integer.parseInt( request.getParameter( "mode" ) );
        }
        if ( request.getParameter( "selPlanId" ) != null )
        {
            paramPlanId = Integer.parseInt( request.getParameter( "selPlanId" ) );
        }
        // �\����
        if ( request.getParameter( "dispIndex" ) != null )
        {
            paramDispIndex = request.getParameter( "dispIndex" ).trim();
        }
        // �v������
        if ( request.getParameter( "planNm" ) != null )
        {
            paramPlanNm = request.getParameter( "planNm" );
        }
        // �\������
        if ( request.getParameter( "dispDateFrom" ) != null )
        {
            paramStrDispFrom = request.getParameter( "dispDateFrom" );
        }
        if ( request.getParameter( "dispDateTo" ) != null )
        {
            paramStrDispTo = request.getParameter( "dispDateTo" );
        }
        if ( request.getParameter( "orgDispStartDate" ) != null )
        {
            paramStrOrgDispFrom = request.getParameter( "orgDispStartDate" );
        }
        if ( request.getParameter( "orgDispEndDate" ) != null )
        {
            paramStrOrgDispTo = request.getParameter( "orgDispEndDate" );
        }
        // �̔�����
        if ( request.getParameter( "salesDateFrom" ) != null )
        {
            paramStrSalesFrom = request.getParameter( "salesDateFrom" );
        }
        if ( request.getParameter( "orgSalesStartDate" ) != null )
        {
            paramStrOrgSalesFrom = request.getParameter( "orgSalesStartDate" );
        }
        // �\���t�J�n
        if ( request.getParameter( "startDayFrom" ) != null )
        {
            paramRsvStartDay = request.getParameter( "startDayFrom" );
        }
        if ( request.getParameter( "startHHFrom" ) != null )
        {
            paramRsvStartHH = request.getParameter( "startHHFrom" );
        }
        if ( request.getParameter( "startMMFrom" ) != null )
        {
            paramRsvStartMM = request.getParameter( "startMMFrom" );
        }
        if ( request.getParameter( "endDayTo" ) != null )
        {
            paramRsvEndDay = request.getParameter( "endDayTo" );
        }
        if ( request.getParameter( "endHHTo" ) != null )
        {
            paramRsvEndHH = request.getParameter( "endHHTo" );
        }
        if ( request.getParameter( "endMMTo" ) != null )
        {
            paramRsvEndMM = request.getParameter( "endMMTo" );
        }
        // �ő�l��
        if ( request.getParameter( "adultNum" ) != null )
        {
            paramAdultNum = request.getParameter( "adultNum" ).trim();
        }
        if ( request.getParameter( "childNum" ) != null )
        {
            paramChildNum = request.getParameter( "childNum" ).trim();
        }
        // �ŏ��l��
        if ( request.getParameter( "minAdultNum" ) != null )
        {
            paramMinAdultNum = request.getParameter( "minAdultNum" ).trim();
        }
        if ( request.getParameter( "minChildNum" ) != null )
        {
            paramMinChildNum = request.getParameter( "minChildNum" ).trim();
        }
        // �j���͈͐l��
        if ( request.getParameter( "minManNum" ) != null )
        {
            if ( request.getParameter( "minManNum" ).trim().equals( "" ) == false )
            {
                paramMinManNum = request.getParameter( "minManNum" ).trim();
            }
        }
        if ( request.getParameter( "minWomanNum" ) != null )
        {
            if ( request.getParameter( "minWomanNum" ).trim().equals( "" ) == false )
            {
                paramMinWomanNum = request.getParameter( "minWomanNum" ).trim();
            }
        }
        // �̔���~�j��
        if ( request.getParameter( "salesStopWeekStatus" ) != null )
        {
            paramSalesStopWeekStatus = Integer.parseInt( request.getParameter( "salesStopWeekStatus" ) );
        }

        if ( request.getParameter( "maxManNum" ) != null )
        {
            if ( request.getParameter( "maxManNum" ).trim().equals( "" ) == false )
            {
                paramMaxManNum = request.getParameter( "maxManNum" ).trim();
            }
        }
        if ( request.getParameter( "maxWomanNum" ) != null )
        {
            if ( request.getParameter( "maxWomanNum" ).trim().equals( "" ) == false )
            {
                paramMaxWomanNum = request.getParameter( "maxWomanNum" ).trim();
            }
        }

        // �A���\��
        if ( request.getParameter( "lognStayDays" ) != null )
        {
            paramLongStayNum = request.getParameter( "lognStayDays" ).trim();
        }
        // imediaFlg
        if ( request.getParameter( "imediaFlg" ) != null )
        {
            paramImediaFlg = Integer.parseInt( request.getParameter( "imediaFlg" ) );
        }
        // �n�s�[�t�^
        if ( request.getParameter( "defaultPoint" ) != null )
        {
            paramDefaultPoint = Integer.parseInt( request.getParameter( "defaultPoint" ) );
        }
        if ( paramImediaFlg == OwnerRsvCommon.IMEDIAFLG_IMEDIA )
        {
            // ALMEX�Ј�
            paramHapyKbn = Integer.parseInt( request.getParameter( "point" ) );
            paramRoomPoint = request.getParameter( "roomPoint" ).trim();
            paramFixPoint = request.getParameter( "fixPointNm" ).trim();
        }
        else
        {
            // �z�e���I�[�i�[
            paramHapyKbn = OwnerRsvCommon.POINT_KIND_FIX;
            paramRoomPoint = "0";
            paramFixPoint = Integer.toString( paramDefaultPoint );
        }
        // �j���������t���O
        if ( request.getParameter( "manCountJudge" ) != null && request.getParameter( "manCountJudge" ).equals( "1" ) )
        {
            paramManCountFlg = 1;
        }
        // �\��҂֎���
        if ( request.getParameter( "question" ) != null )
        {
            paramQuestion = request.getParameter( "question" );
        }
        // �\��҂֎���t���O
        if ( request.getParameter( "questionFlag" ) != null && request.getParameter( "questionFlag" ).equals( "on" ) )
        {
            paramQuestionFlag = 1;
        }
        // �v�����Љ�
        if ( request.getParameter( "planInfo" ) != null )
        {
            paramPlanInfo = request.getParameter( "planInfo" );
        }
        // ���l
        if ( request.getParameter( "remarks" ) != null )
        {
            paramRemarks = request.getParameter( "remarks" );
        }
        // �v�����摜
        if ( request.getParameter( "setFileNm" ) != null )
        {
            paramFileNm = request.getParameter( "setFileNm" );
        }
        // �ő�\���t��
        if ( request.getParameter( "quantity" ) != null )
        {
            paramQuantity = request.getParameter( "quantity" ).trim();
        }
        // �����w��
        if ( request.getParameter( "offerKind" ) != null )
        {
            paramOfferKind = replaceIntValue( request.getParameter( "offerKind" ) );
        }
        // �v�����f��
        if ( request.getParameter( "planDisp" ) != null )
        {
            planDisp = replaceIntValue( request.getParameter( "planDisp" ) );
        }
        // �v�]���͍���
        if ( request.getParameter( "userRequest" ) != null )
        {
            userRequest = replaceIntValue( request.getParameter( "userRequest" ) );
        }
        // �̔��t���O
        if ( request.getParameter( "salesFlag" ) != null )
        {
            paramSalesFlg = Integer.parseInt( request.getParameter( "salesFlag" ) );
        }
        // �K�p����
        String[] seqs = request.getParameterValues( "roomNum" );
        if ( seqs != null )
        {
            for( int i = 0 ; i < seqs.length ; i++ )
            {
                seqList.add( Integer.parseInt( seqs[i] ) );
            }
        }
        // �K�p�����ɐݒ肳��Ă���J�n���E�I����
        for( int i = 0 ; i < seqList.size() ; i++ )
        {
            selSeq = seqList.get( i );
            frmSub = new FormOwnerRsvPlanSub();
            frmSub.setCheck( 1 );
            frmSub.setSeq( selSeq );
            // frmSub.setTekiyoDateFrom( request.getParameter( "dateFrom" + selSeq ) );
            // frmSub.setTekiyoDateTo( request.getParameter( "dateTo" + selSeq ) );

            // �K���������t���Z�b�g����悤�ɂ���
            frmSub.setTekiyoDateFrom( DateEdit.getDate( 1 ) );
            // �K��99999999���Z�b�g����悤�ɂ���
            frmSub.setTekiyoDateTo( "9999/99/99" );
            frmSubList.add( frmSub );
        }

        // �K�{�I�v�V����
        String[] mustOpts = request.getParameterValues( "mustOpt" );
        if ( mustOpts != null )
        {
            for( int i = 0 ; i < mustOpts.length ; i++ )
            {
                mustOptList.add( Integer.parseInt( mustOpts[i] ) );
            }
        }

        // �ʏ�I�v�V����
        String[] commOpts = request.getParameterValues( "commOpt" );
        if ( commOpts != null )
        {
            for( int i = 0 ; i < commOpts.length ; i++ )
            {
                commOptList.add( Integer.parseInt( commOpts[i] ) );
            }
        }
        // �t�H�[���ɃZ�b�g
        frm.setSelHotelID( paramHotelId );
        frm.setOwnerHotelID( paraOwnerHotelID );
        frm.setUserId( paramUserId );
        frm.setSelPlanID( paramPlanId );
        frm.setPlanNm( paramPlanNm );
        frm.setDispIndex( paramDispIndex );
        frm.setMode( paramMode );
        // �\������
        frm.setDispStartDate( paramStrDispFrom );
        frm.setDispEndDate( paramStrDispTo );
        frm.setOrgDispStartDate( paramStrOrgDispFrom );
        frm.setOrgDispEndDate( paramStrOrgDispTo );
        // �̔�����
        frm.setSalesStartDate( paramStrSalesFrom );
        frm.setOrgSalesStartDate( paramStrOrgSalesFrom );
        // �\���t
        frm.setRsvStartDay( paramRsvStartDay );
        frm.setRsvStartTimeHH( paramRsvStartHH );
        frm.setRsvStartTimeMM( paramRsvStartMM );
        frm.setRsvEndDay( paramRsvEndDay );
        frm.setRsvEndTimeHH( paramRsvEndHH );
        frm.setRsvEndTimeMM( paramRsvEndMM );
        // �ő�l��
        frm.setMaxNumAdult( paramAdultNum );
        frm.setMaxNumChild( paramChildNum );
        // �ŏ��l��
        frm.setMinNumAdult( paramMinAdultNum );
        frm.setMinNumChild( paramMinChildNum );
        // �j���l���͈�
        frm.setMinNumMan( paramMinManNum );
        frm.setMinNumWoman( paramMinWomanNum );
        frm.setMaxNumMan( paramMaxManNum );
        frm.setMaxNumWoman( paramMaxWomanNum );
        // �j����������
        frm.setManCountJudgeFlg( paramManCountFlg );
        // �̔���~�j��
        frm.setSalesStopWeekStatus( paramSalesStopWeekStatus );
        // �A���\��
        frm.setRenpakuNum( paramLongStayNum );
        // imediaflg
        frm.setImediaFlg( paramImediaFlg );
        // �n�s�[�|�C���g�����l
        frm.setDefaultPoint( paramDefaultPoint );
        // �n�s�[�t�^
        frm.setPointKbn( paramHapyKbn );
        frm.setPointRoom( paramRoomPoint );
        frm.setPointFix( paramFixPoint );
        // �\��҂֎���
        frm.setQuestion( paramQuestion );
        // �\��҂֎���K�{�t���O
        frm.setQuestionFlag( paramQuestionFlag );
        // �v�����Љ�
        frm.setPlanInfo( paramPlanInfo );
        // ���l
        frm.setRemarks( paramRemarks );
        // �v�����摜
        frm.setPlanImg( paramFileNm );
        // �v�����f��
        frm.setPublishingFlg( planDisp );
        // �v�]���͍��ڕ\��
        frm.setUserRequestFlg( userRequest );
        // �ő�\���t��
        frm.setMaxQuantity( paramQuantity );
        // �����w��
        frm.setOfferKbn( paramOfferKind );
        // �K�p����
        frm.setFrmSubList( frmSubList );
        // �̔��t���O
        frm.setSalesFlag( paramSalesFlg );
        if ( paramSalesFlg == 0 )
        {
            frm.setSalesBtnValue( BTN_OFF );
        }
        else
        {
            frm.setSalesBtnValue( BTN_ON );
        }
        // �K�{�I�v�V����
        frm.setMustOptIdList( mustOptList );
        // �ʏ�I�v�V����
        frm.setComOptIdList( commOptList );

        // ���\��f�[�^�����݂��邩
        isExistsRsv = OwnerRsvCommon.isExistsRsvPlan( frm.getSelHotelID(), frm.getSelPlanID() );
        frm.setExistsRsv( isExistsRsv );
        frm.setInfoMsg( Message.getMessage( "warn.30019" ) );

    }

    /**
     * Int�^�̒l�ɕϊ�����
     * 
     * @param value �ϊ��Ώۂ̒l
     * @return �ϊ���̒l
     */
    private int replaceIntValue(String value)
    {
        int ret = 0;

        if ( (value == null) || (value.trim().length() == 0) )
        {
            ret = 0;
        }
        else
        {
            ret = Integer.parseInt( value );
        }
        return(ret);
    }

    /**
     * ���͒l�`�F�b�N
     * 
     * @param input FormOwnerRsvPlan�I�u�W�F�N�g
     * @return true:����Afalse:�G���[����
     */
    private boolean isInputCheckMsg(FormOwnerRsvPlan frm) throws Exception
    {
        String msg;
        int ret;
        boolean isCheck = false;
        boolean isDispFromCheck = true;
        boolean isDispToCheck = true;
        boolean isSalesFromCheck = true;
        boolean isFromCheck = true;
        boolean isToCheck = true;
        int maxnumChild = 0;
        int maxnumAdult = 0;
        int minnumChild = 0;
        int minnumAdult = 0;
        int maxnumMan = 0;
        int minnumMan = 0;
        int maxnumWoman = 0;
        int minnumWoman = 0;
        int orgDate = 0;
        int frmDate = 0;
        FormOwnerRsvPlanSub frmSub;
        ArrayList<FormOwnerRsvPlanSub> frmSubList = new ArrayList<FormOwnerRsvPlanSub>();
        boolean maxnumcheckAdult = false;
        boolean maxnumcheckChild = false;
        msg = "";
        ret = 0;

        // �\����(���������[�h���̓`�F�b�N���Ȃ�)
        if ( frm.getDraftMode() == DRAFT_OFF )
        {
            if ( CheckString.onlySpaceCheck( frm.getDispIndex() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�\����" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frm.getDispIndex() ) == false) )
                {
                    // ���p�����ȊO�����͂���Ă���ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�\����", "0�ȏ�̐��l" ) + "<br />";
                }
            }
        }
        else
        {
            frm.setDispIndex( "0" );
        }

        // �v������
        if ( CheckString.onlySpaceCheck( frm.getPlanNm() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�v������" ) + "<br />";
        }
        else
        {
            ret = OwnerRsvCommon.LengthCheck( frm.getPlanNm().trim(), 60 );
            if ( ret == 1 )
            {
                // ����Over�̏ꍇ
                msg = msg + Message.getMessage( "warn.00038", "�v������", "30" ) + "<br />";
            }
        }

        // �\������
        if ( CheckString.onlySpaceCheck( frm.getDispStartDate() ) == true )
        {
            // �J�n���������́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�\������ �J�n��" ) + "<br />";
            isDispFromCheck = false;
        }

        if ( CheckString.onlySpaceCheck( frm.getDispEndDate() ) == true )
        {
            // �I�����������́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�\������ �I��" ) + "<br />";
            isDispToCheck = false;

        }
        else
        {
            // �ύX�O����ύX��̊��Ԃ̊Ԃɗ\��f�[�^�����邩
            if ( frm.getOrgDispEndDate().trim().length() != 0 )
            {
                orgDate = Integer.parseInt( frm.getOrgDispEndDate().replace( "/", "" ) );
                frmDate = Integer.parseInt( frm.getDispEndDate().replace( "/", "" ) );
                if ( orgDate > frmDate )
                {
                    if ( OwnerRsvCommon.isReservePlanTargetDate( frm.getSelHotelID(), frm.getSelPlanID(), frmDate, orgDate ) == true )
                    {
                        msg = msg + Message.getMessage( "warn.30013" ) + "<br />";
                        isDispToCheck = false;
                    }
                }
            }
        }

        if ( (isDispFromCheck == true) && (isDispToCheck == true) )
        {
            // �J�n���E�I������From-To�`�F�b�N
            if ( frm.getDispStartDate().compareTo( frm.getDispEndDate() ) > 0 )
            {
                msg = msg + Message.getMessage( "warn.00009", "�\�����Ԃ͈͎̔w��" ) + "<br />";
            }
        }

        // �̔�����
        if ( CheckString.onlySpaceCheck( frm.getSalesStartDate() ) == true )
        {
            // �J�n���������́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�̔��J�n��" ) + "<br />";
            isSalesFromCheck = false;
        }
        else
        {
            // �ύX�O����ύX��̊��Ԃ̊Ԃɗ\��f�[�^�����邩
            if ( frm.getOrgSalesStartDate().trim().length() != 0 )
            {
                orgDate = Integer.parseInt( frm.getOrgSalesStartDate().replace( "/", "" ) );
                frmDate = Integer.parseInt( frm.getSalesStartDate().replace( "/", "" ) );
                if ( orgDate < frmDate )
                {
                    if ( OwnerRsvCommon.isReservePlanTargetDate( frm.getSelHotelID(), frm.getSelPlanID(), orgDate, frmDate ) == true )
                    {
                        msg = msg + Message.getMessage( "warn.30013" ) + "<br />";
                        isSalesFromCheck = false;
                    }
                }
            }
        }
        if ( (isDispFromCheck == true) && (isSalesFromCheck == true) )
        {
            // �̔��J�n���A�\������From�`�F�b�N
            if ( frm.getDispStartDate().compareTo( frm.getSalesStartDate() ) > 0 )
            {
                msg = msg + Message.getMessage( "warn.30005", "�̔�����", "�\�����ԊJ�n��" ) + "<br />";
            }
        }

        // �\������(�I��) < �̔�����(�J�n)�̏ꍇ�̓G���[
        if ( (isDispToCheck == true) && (isSalesFromCheck == true) )
        {
            if ( frm.getSalesStartDate().compareTo( frm.getDispEndDate() ) > 0 )
            {
                msg = msg + Message.getMessage( "warn.30011", "�̔�����", "�\�����ԏI����" ) + "<br />";
            }
        }

        // �\���t
        if ( (CheckString.onlySpaceCheck( frm.getRsvEndDay() ) == true) && (CheckString.onlySpaceCheck( frm.getRsvEndTimeHH() ) == true)
                && (CheckString.onlySpaceCheck( frm.getRsvEndTimeMM() ) == true) && (CheckString.onlySpaceCheck( frm.getRsvStartDay() ) == true)
                && (CheckString.onlySpaceCheck( frm.getRsvStartTimeHH() ) == true) && (CheckString.onlySpaceCheck( frm.getRsvStartTimeMM() ) == true) )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�\���t�J�n" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getRsvEndDay() ) == false) || (OwnerRsvCommon.numCheck( frm.getRsvEndTimeHH() ) == false)
                    || (OwnerRsvCommon.numCheck( frm.getRsvEndTimeMM() ) == false) || (OwnerRsvCommon.numCheck( frm.getRsvStartDay() ) == false)
                    || (OwnerRsvCommon.numCheck( frm.getRsvStartTimeHH() ) == false) || (OwnerRsvCommon.numCheck( frm.getRsvStartTimeMM() ) == false) )
            {
                // ���p�����ȊO�����͂���Ă���ꍇ
                msg = msg + Message.getMessage( "warn.30007", "�\���t�J�n�̓��t�A����", "����" ) + "<br />";
            }
            else
            {
                if ( (replaceIntValue( frm.getRsvStartDay() ) == 0) || (replaceIntValue( frm.getRsvEndDay() ) == 0) || (replaceIntValue( frm.getRsvStartDay() ) < 0) || (replaceIntValue( frm.getRsvEndDay() ) < 0) )
                {
                    // �J�n���A�܂��͏I������0
                    msg = msg + Message.getMessage( "warn.30006", "�\���t�J�n", "0" ) + "<br />";
                }
                // 60���𒴂��Ă��Ȃ����ǂ����̔���
                if ( replaceIntValue( frm.getRsvStartDay() ) > 60 || replaceIntValue( frm.getRsvEndDay() ) > 60 )
                {
                    msg = msg + Message.getMessage( "warn.30007", "�\���t�J�n", "60�ȉ��̐���" ) + "<br />";
                }
                if ( (Integer.parseInt( frm.getRsvEndTimeHH() ) > 23 || Integer.parseInt( frm.getRsvEndTimeHH() ) < 0)
                        || (Integer.parseInt( frm.getRsvStartTimeHH() ) > 23 || Integer.parseInt( frm.getRsvStartTimeHH() ) < 0) )
                {
                    // ���Ԃ�0���`23���ȊO�̏ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�\���t�J�n�̎���", "0�`23" ) + "<br />";
                }
                if ( (Integer.parseInt( frm.getRsvEndTimeMM() ) > 59 || Integer.parseInt( frm.getRsvEndTimeMM() ) < 0)
                        || (Integer.parseInt( frm.getRsvStartTimeMM() ) > 59 || Integer.parseInt( frm.getRsvStartTimeMM() ) < 0) )
                {
                    // ����0���`59�ȊO�̏ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�\���t�J�n�̕�", "0�`59" ) + "<br />";
                }
                if ( ((Integer.parseInt( frm.getRsvStartDay() ) > 0) && (Integer.parseInt( frm.getRsvStartTimeHH() ) >= 0) && (Integer.parseInt( frm.getRsvStartTimeMM() ) >= 0))
                        && ((Integer.parseInt( frm.getRsvEndDay() ) > 0) && (Integer.parseInt( frm.getRsvEndTimeHH() ) >= 0) && (Integer.parseInt( frm.getRsvEndTimeMM() ) >= 0)) )
                {
                    // �O��֌W�`�F�b�N
                    if ( Integer.parseInt( frm.getRsvStartDay() ) < Integer.parseInt( frm.getRsvEndDay() ) )
                    {
                        // ���̑O��֌W���s��
                        msg = msg + Message.getMessage( "warn.00009", "�\���t�J�n�̓��t�͈͎̔w��" ) + "<br />";
                    }
                    else if ( Integer.parseInt( frm.getRsvStartDay() ) == Integer.parseInt( frm.getRsvEndDay() ) )
                    {
                        // ������t�̏ꍇ�A���Ԃ̑O��֌W�`�F�b�N
                        if ( Integer.parseInt( frm.getRsvStartTimeHH() ) > Integer.parseInt( frm.getRsvEndTimeHH() ) )
                        {
                            msg = msg + Message.getMessage( "warn.00009", "�\���t�J�n�̎��Ԃ͈͎̔w��" ) + "<br />";
                        }
                        else if ( Integer.parseInt( frm.getRsvStartTimeHH() ) == Integer.parseInt( frm.getRsvEndTimeHH() ) )
                        {
                            // ���ꎞ�Ԃ̏ꍇ�A���̑O��֌W�`�F�b�N
                            if ( Integer.parseInt( frm.getRsvStartTimeMM() ) > Integer.parseInt( frm.getRsvEndTimeMM() ) )
                            {
                                msg = msg + Message.getMessage( "warn.00009", "�\���t�J�n�̕��͈͎̔w��" ) + "<br />";
                            }
                        }
                    }
                }
            }
        }

        // �ő�l��
        if ( (CheckString.onlySpaceCheck( frm.getMaxNumAdult() ) == true) || (CheckString.onlySpaceCheck( frm.getMaxNumChild() ) == true) )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�ő�l��" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getMaxNumAdult() ) == false) || (OwnerRsvCommon.numCheck( frm.getMaxNumChild() ) == false) )
            {
                // ���p�����ȊO�����͂���Ă���ꍇ
                msg = msg + Message.getMessage( "warn.30007", "�ő�l��", "1�ȏ�̐���" ) + "<br />";
            }
            else
            {
                maxnumcheckAdult = true;
                if ( Integer.parseInt( frm.getMaxNumAdult() ) <= 0 )
                {
                    msg = msg + Message.getMessage( "warn.30007", "��l�̍ő�l��", "1�ȏ�̐���" ) + "<br />";
                }
                else
                {
                    maxnumAdult = Integer.parseInt( frm.getMaxNumAdult() );
                }
                maxnumcheckChild = true;
                if ( (frm.getMaxNumChild().trim().length() != 0) && (Integer.parseInt( frm.getMaxNumChild() ) < 0) )
                {
                    msg = msg + Message.getMessage( "warn.30007", "�q���̍ő�l��", "0�ȏ�̐���" ) + "<br />";
                }
                else
                {
                    maxnumChild = Integer.parseInt( frm.getMaxNumChild() );
                }
            }
        }

        // �ŏ��l��
        if ( (CheckString.onlySpaceCheck( frm.getMinNumAdult() ) == true) || (CheckString.onlySpaceCheck( frm.getMinNumChild() ) == true) )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�ŏ��l��" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getMinNumAdult() ) == false) || (OwnerRsvCommon.numCheck( frm.getMinNumChild() ) == false) )
            {
                // ���p�����ȊO�����͂���Ă���ꍇ
                msg = msg + Message.getMessage( "warn.30007", "�ŏ��l��", "1�ȏ�̐���" ) + "<br />";
            }
            else
            {
                if ( Integer.parseInt( frm.getMinNumAdult() ) <= 0 )
                {
                    msg = msg + Message.getMessage( "warn.30007", "��l�̍ŏ��l��", "1�ȏ�̐���" ) + "<br />";
                }
                else
                {
                    minnumAdult = Integer.parseInt( frm.getMinNumAdult() );
                }
                if ( (frm.getMinNumChild().trim().length() != 0) && (Integer.parseInt( frm.getMinNumChild() ) < 0) )
                {
                    msg = msg + Message.getMessage( "warn.30007", "�q���̍ŏ��l��", "0�ȏ�̐���" ) + "<br />";
                }
                else
                {
                    minnumChild = Integer.parseInt( frm.getMinNumChild() );
                }
            }
        }

        // �ő�ŏ��l���̐�
        if ( maxnumcheckAdult == true && minnumAdult > maxnumAdult )
        {
            msg = msg + Message.getMessage( "warn.30006", "��l�̍ő�l��", "��l�̍ŏ��l��" ) + "<br />";
        }

        if ( maxnumcheckChild == true && minnumChild > maxnumChild )
        {
            msg = msg + Message.getMessage( "warn.30006", "�q���̍ő�l��", "�q���̍ŏ��l��" ) + "<br />";
        }

        // �A���\��
        if ( CheckString.onlySpaceCheck( frm.getRenpakuNum() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�A���\��" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getRenpakuNum() ) == false) || (OwnerRsvCommon.numCheck( frm.getRenpakuNum() ) == false) )
            {
                // ���p�����ȊO�����͂���Ă���ꍇ
                msg = msg + Message.getMessage( "warn.30007", "�A���\��", "1�ȏ�̐���" ) + "<br />";
            }
            else
            {
                if ( (frm.getRenpakuNum().trim().length() == 0) || (Integer.parseInt( frm.getRenpakuNum() ) <= 0) )
                {
                    msg = msg + Message.getMessage( "warn.30006", "�A���\��", "0" ) + "<br />";
                }
            }
        }

        // ���ʐl���͈͂̐��l�`�F�b�N
        if ( (CheckString.onlySpaceCheck( frm.getMinNumMan() ) == true) || (CheckString.onlySpaceCheck( frm.getMinNumWoman() ) == true) ||
                (CheckString.onlySpaceCheck( frm.getMaxNumMan() ) == true) || (CheckString.onlySpaceCheck( frm.getMaxNumWoman() ) == true) )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "���ʐl���͈�" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getMinNumMan() ) == false) || (OwnerRsvCommon.numCheck( frm.getMinNumWoman() ) == false) ||
                    (OwnerRsvCommon.numCheck( frm.getMaxNumMan() ) == false) || (OwnerRsvCommon.numCheck( frm.getMaxNumWoman() ) == false) )
            {
                // ���p�����ȊO�����͂���Ă���ꍇ
                msg = msg + Message.getMessage( "warn.30007", "���ʐl���͈�", "0�ȏ�̐���" ) + "<br />";
            }
            else
            {
                if ( (frm.getMinNumMan().trim().length() != 0) && (Integer.parseInt( frm.getMinNumMan() ) < 0) ||
                        (frm.getMinNumWoman().trim().length() != 0) && (Integer.parseInt( frm.getMinNumWoman() ) < 0) ||
                        (frm.getMaxNumMan().trim().length() != 0) && (Integer.parseInt( frm.getMaxNumMan() ) < 0) ||
                        (frm.getMaxNumWoman().trim().length() != 0) && (Integer.parseInt( frm.getMaxNumWoman() ) < 0) )
                {
                    msg = msg + Message.getMessage( "warn.30007", "���ʐl���͈�", "0�ȏ�̐���" ) + "<br />";
                }
                else
                {
                    minnumMan = Integer.parseInt( frm.getMinNumMan() );
                    minnumWoman = Integer.parseInt( frm.getMinNumWoman() );
                    maxnumMan = Integer.parseInt( frm.getMaxNumMan() );
                    maxnumWoman = Integer.parseInt( frm.getMaxNumWoman() );
                }
            }
        }

        // ���ʐl���͈͂̍ő�ŏ��`�F�b�N
        if ( minnumMan > maxnumMan )
        {
            msg = msg + Message.getMessage( "warn.30006", "�j���̍ő�l��", "�j���̍ŏ��l��" ) + "<br />";
        }

        if ( minnumWoman > maxnumWoman )
        {
            msg = msg + Message.getMessage( "warn.30006", "�����̍ő�l��", "�����̍ŏ��l��" ) + "<br />";
        }

        // �j���E�������v�̍ő�l���ݒ�`�F�b�N
        if ( maxnumMan > 0 || maxnumWoman > 0 )
        {
            if ( maxnumAdult > maxnumMan + maxnumWoman )
            {
                msg = msg + Message.getMessage( "warn.30006", "�j���̍ő�l���Ə����̍ő�l���̍��v", "��l�̍ő�l��" ) + "<br />";
            }
            // �j���E�������v�̍Œ�l���ݒ�`�F�b�N
            if ( minnumAdult > maxnumMan + maxnumWoman )
            {
                msg = msg + Message.getMessage( "warn.30006", "�j���̍ő�l���Ə����̍ő�l���̍��v", "��l�̍ŏ��l��" ) + "<br />";
            }
        }

        // �j�������s���̏ꍇ�͏����̍ő�l�����j���ő�l�����������Ƃ�F�߂Ȃ�
        if ( frm.getManCountJudgeFlg() == 0 && maxnumMan > maxnumWoman )
        {
            msg = msg + Message.getMessage( "warn.30006", "�j�������������Ȃ��ꍇ�A�����̍ő�l��", "�j���̍ő�l��" ) + "<br />";
        }

        // �n�s�[�t�^
        if ( frm.getPointKbn() == 1 )
        {
            // �����I��
            if ( CheckString.onlySpaceCheck( frm.getPointRoom() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�n�s�[�t�^�|�C���g" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frm.getPointRoom() ) == false) )
                {
                    // ���p�����ȊO�����͂���Ă���ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�n�s�[�t�^�|�C���g", "0�ȏ�̐���" ) + "<br />";
                }
                else
                {
                    if ( (frm.getPointKbn() == 1) && ((frm.getPointFix().trim().length() != 0) && ((Integer.parseInt( frm.getPointFix() ) > 0) || (Integer.parseInt( frm.getPointFix() ) < 0))) )
                    {
                        msg = msg + Message.getMessage( "warn.30008", "�����p�ɑ΂��銄��", "�Œ�|�C���g" ) + "<br />";
                    }
                }
            }
        }
        else
        {
            // �����I��
            if ( CheckString.onlySpaceCheck( frm.getPointFix() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�n�s�[�t�^�|�C���g" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frm.getPointFix() ) == false) )
                {
                    // ���p�����ȊO�����͂���Ă���ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�n�s�[�t�^�|�C���g", "0�ȏ�̐���" ) + "<br />";
                }
                else
                {
                    if ( (frm.getPointKbn() == 2) && ((frm.getPointRoom().trim().length() != 0) && ((Integer.parseInt( frm.getPointRoom() ) > 0) || (Integer.parseInt( frm.getPointRoom() ) < 0))) )
                    {
                        msg = msg + Message.getMessage( "warn.30008", "�Œ�|�C���g", "�����p�ɑ΂��銄��" ) + "<br />";
                    }
                }
            }
        }

        // �v�����Љ�
        if ( CheckString.onlySpaceCheck( frm.getPlanInfo() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�v�����Љ�" ) + "<br />";
        }
        else
        {
            ret = OwnerRsvCommon.LengthCheck( frm.getPlanInfo().trim(), 300 );
            if ( ret == 1 )
            {
                // ����Over�̏ꍇ
                msg = msg + Message.getMessage( "warn.00038", "�v�����Љ�", "150" ) + "<br />";
            }
        }

        // �\��҂֎���(�K�{�̏ꍇ�͋󔒂͋����Ȃ�)
        if ( frm.getQuestionFlag() == 1 )
        {
            if ( CheckString.onlySpaceCheck( frm.getQuestion() ) == true )
            {
                msg = msg + Message.getMessage( "warn.00001", "�\��҂֎���" ) + "<br />";
            }
        }

        // ���l
        ret = OwnerRsvCommon.LengthCheck( frm.getRemarks().trim(), 200 );
        if ( ret == 1 )
        {
            // ����Over�̏ꍇ
            msg = msg + Message.getMessage( "warn.00038", "���l", "100" ) + "<br />";
        }

        // �ő�\���t��
        if ( CheckString.onlySpaceCheck( frm.getMaxQuantity() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�ő�\���t��" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getMaxQuantity() ) == false) || (OwnerRsvCommon.numCheck( frm.getMaxQuantity() ) == false) )
            {
                // ���p�����ȊO�����͂���Ă���ꍇ
                msg = msg + Message.getMessage( "warn.30007", "�ő�\���t��", "1�ȏ�̐���" ) + "<br />";
            }
        }

        // �K�p����
        boolean isChecked = false;
        frmSubList = frm.getFrmSubList();
        for( int i = 0 ; i < frmSubList.size() ; i++ )
        {
            frmSub = frmSubList.get( i );
            if ( frmSub.getCheck() == 1 )
            {
                isChecked = true;
                break;
            }
        }
        if ( isChecked == false )
        {
            // ���I��
            msg = msg + Message.getMessage( "warn.00002", "�K�p����" ) + "<br />";
        }
        else
        {
            for( int i = 0 ; i < frmSubList.size() ; i++ )
            {
                frmSub = frmSubList.get( i );
                isFromCheck = true;
                isToCheck = true;

                if ( CheckString.onlySpaceCheck( frmSub.getTekiyoDateFrom() ) == true )
                {
                    // �J�n���������͂̏ꍇ
                    isFromCheck = false;
                }
                if ( CheckString.onlySpaceCheck( frmSub.getTekiyoDateTo() ) == true )
                {
                    // �I�����������͂̏ꍇ
                    isFromCheck = false;
                }
                if ( (isFromCheck == true) && (isToCheck == true) )
                {
                    // �J�n���E�I������From-To�`�F�b�N
                    if ( Integer.parseInt( frmSub.getTekiyoDateFrom().replace( "/", "" ) ) > Integer.parseInt( frmSub.getTekiyoDateTo().replace( "/", "" ) ) )
                    {
                        msg = msg + Message.getMessage( "warn.00009", "�K�p�����őI������Ă���A�Ǘ��ԍ�[" + frmSub.getSeq() + "]�̔̔����Ԃ͈͎̔w��" ) + "<br />";
                    }
                }
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
     * �I�v�V�������쐬����
     * 
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @param logic LogicOwnerRsvPlan�I�u�W�F�N�g
     * @param draftKbn 0:�ʏ�A1:������
     * @return �Ȃ�
     */
    private void createOption(FormOwnerRsvPlan frm, LogicOwnerRsvPlan logic, int draftKbn) throws Exception
    {
        String mustOpt = "";
        String commOpt = "";
        FormOwnerRsvPlanOptionSub newFrmOptSub;
        ArrayList<FormOwnerRsvPlanOptionSub> allMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> regMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> newMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> allCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> regCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> newCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();

        // ���K�{�I�v�V�������擾
        logic.setSubMustOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 1 );
        allMustOptFrmSubList = logic.getSubMustOptionFormList();

        // �o�^�ςݕK�{�I�v�V�����̎擾
        logic.setSubMustOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getOption( 1, draftKbn );
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

        // ��ʂ̓��e�ƃ}�[�W
        for( int i = 0 ; i < allMustOptFrmSubList.size() ; i++ )
        {
            FormOwnerRsvPlanOptionSub optionSub = allMustOptFrmSubList.get( i );
            newFrmOptSub = new FormOwnerRsvPlanOptionSub();
            newFrmOptSub.setCheck( 0 );
            newFrmOptSub.setOptionID( optionSub.getOptionID() );
            newFrmOptSub.setOptionNm( optionSub.getOptionNm() );
            newFrmOptSub.setOptionSubNm( optionSub.getOptionSubNm() );

            for( int j = 0 ; j < frm.getMustOptIdList().size() ; j++ )
            {
                if ( optionSub.getOptionID() == frm.getMustOptIdList().get( j ) )
                {
                    newFrmOptSub.setCheck( 1 );
                    newFrmOptSub.setOptionID( optionSub.getOptionID() );
                    newFrmOptSub.setOptionNm( optionSub.getOptionNm() );
                    newFrmOptSub.setOptionSubNm( optionSub.getOptionSubNm() );
                    break;
                }
            }
            newMustOptFrmSubList.add( newFrmOptSub );
        }

        // ���ʏ�I�v�V�������擾
        logic.setSubCommOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getNewOption( 0 );
        allCommOptFrmSubList = logic.getSubCommOptionFormList();

        // �o�^�ςݕK�{�I�v�V�����̎擾
        logic.setSubCommOptionFormList( new ArrayList<FormOwnerRsvPlanOptionSub>() );
        logic.getOption( 0, draftKbn );
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

        // ��ʂ̃f�[�^�ƃ}�[�W
        for( int i = 0 ; i < allCommOptFrmSubList.size() ; i++ )
        {
            FormOwnerRsvPlanOptionSub optionSub = allCommOptFrmSubList.get( i );
            newFrmOptSub = new FormOwnerRsvPlanOptionSub();
            newFrmOptSub.setCheck( 0 );
            newFrmOptSub.setOptionID( optionSub.getOptionID() );
            newFrmOptSub.setOptionNm( optionSub.getOptionNm() );
            newFrmOptSub.setOptionSubNm( optionSub.getOptionSubNm() );

            for( int j = 0 ; j < frm.getComOptIdList().size() ; j++ )
            {
                if ( optionSub.getOptionID() == frm.getComOptIdList().get( j ) )
                {
                    newFrmOptSub.setCheck( 1 );
                    newFrmOptSub.setOptionID( optionSub.getOptionID() );
                    newFrmOptSub.setOptionNm( optionSub.getOptionNm() );
                    newFrmOptSub.setOptionSubNm( optionSub.getOptionSubNm() );
                    break;
                }
            }
            newCommOptFrmSubList.add( newFrmOptSub );
        }

        frm.setFrmMustOptionSubList( newMustOptFrmSubList );
        frm.setFrmCommOptionSubList( newCommOptFrmSubList );
    }

    /**
     * �K�p�����쐬����
     * 
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @param logic LogicOwnerRsvPlan�I�u�W�F�N�g
     * @return �Ȃ�
     */
    private void createRoomList(FormOwnerRsvPlan frm, LogicOwnerRsvPlan logic) throws Exception
    {
        FormOwnerRsvPlanSub logicFormSub;
        FormOwnerRsvPlanSub frmSub;
        FormOwnerRsvPlanSub newFrmSub;
        ArrayList<FormOwnerRsvPlanSub> frmSubList = new ArrayList<FormOwnerRsvPlanSub>();

        // �v�����E�����ݒ�f�[�^�擾
        logic.setFrm( frm );
        logic.getNewPlanRoom();

        // �v�����E�����ݒ�f�[�^�쐬
        for( int i = 0 ; i < logic.getSubFormList().size() ; i++ )
        {
            // DB����擾����Seq�̃��X�g
            logicFormSub = logic.getSubFormList().get( i );
            // �V�������X�g�ɏ����l�ݒ�
            newFrmSub = new FormOwnerRsvPlanSub();
            newFrmSub.setCheck( 0 );
            newFrmSub.setSeq( logicFormSub.getSeq() );
            newFrmSub.setRoomNm( logicFormSub.getRoomNm() );
            for( int j = 0 ; j < frm.getFrmSubList().size() ; j++ )
            {
                // ��ʂŃZ�b�g����Ă���Seq�̃��X�g
                frmSub = frm.getFrmSubList().get( j );
                if ( logicFormSub.getSeq() == frmSub.getSeq() )
                {
                    newFrmSub.setCheck( 1 );
                    newFrmSub.setTekiyoDateFrom( frmSub.getTekiyoDateFrom() );
                    newFrmSub.setTekiyoDateTo( frmSub.getTekiyoDateTo() );
                    break;
                }
            }

            frmSubList.add( newFrmSub );
        }

        // �t�H�[���ɃZ�b�g
        frm.setFrmSubList( frmSubList );
    }

    /**
     * �̔��t���O�ݒ�
     * 
     * @param LogicOwnerRsvPlan �r�W�l�X���W�b�N
     * @return �X�V��̔̔��t���O
     */
    private int setSalesFlg(LogicOwnerRsvPlan logic, int salesFlag) throws Exception
    {
        int newSalesFlg = 0;

        try
        {
            if ( salesFlag == -1 )
            {
                // �Ώۃf�[�^�Ȃ�
                return(newSalesFlg);
            }
            else if ( salesFlag == 0 )
            {
                // �̔����~�ɂ���
                newSalesFlg = 1;
            }
            logic.setSalesFlg( newSalesFlg );

            return(newSalesFlg);
        }
        catch ( Exception e )
        {
            throw new Exception( "ActionOwnerRscPlan.setSalesFlg " + e.toString() );
        }
    }

    /**
     * �\���{�^���N���b�N
     * 
     * @param request HttpServletRequest
     * @param logic LogicOwnerRsvPlan �I�u�W�F�N�g
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @return �Ȃ�
     */
    private FormOwnerRsvPlan execPlanView(HttpServletRequest request, LogicOwnerRsvPlan logic, FormOwnerRsvPlan frm) throws Exception
    {
        int selPlanId = 0;
        int salesFlg = 0;
        int baseSeq = 0;
        String mustOpt = "";
        String commOpt = "";
        String selPlanNm = "";
        ArrayList<Integer> selSeqList = new ArrayList<Integer>();
        ArrayList<FormOwnerRsvPlanSub> regFrmSubList = new ArrayList<FormOwnerRsvPlanSub>();
        ArrayList<FormOwnerRsvPlanSub> newFrmSubList = new ArrayList<FormOwnerRsvPlanSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> allMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> regMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> newMustOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> allCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> regCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        ArrayList<FormOwnerRsvPlanOptionSub> newCommOptFrmSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        FormOwnerRsvPlanSub frmPlanSub = new FormOwnerRsvPlanSub();

        // ���I������Ă���v����ID���擾
        String[] plans = request.getParameterValues( "selPlan" );
        for( int i = 0 ; i < plans.length ; i++ )
        {
            selPlanId = Integer.parseInt( plans[i] );
        }
        frm.setSelPlanID( selPlanId );

        // �v���������擾
        selPlanNm = logic.getDraftPlanNm( frm.getSelHotelID(), selPlanId );

        // ���������v�������X�g�擾
        logic.setFrm( frm );
        logic.getDraftPlanIdList();
        logic.getDraftPlanNmList();

        // ���o�^�ς݃v�������̎擾
        logic.setFrmSub( frmPlanSub );
        logic.getPlan_PlanId( selPlanId, DRAFT_ON );
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
        logic.getOption( 1, DRAFT_ON );
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
        logic.getOption( 0, DRAFT_ON );
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

        // ���t�H�[���ɃZ�b�g
        frm = logic.getFrm();
        frm.setSalesFlag( salesFlg );
        if ( salesFlg == 0 )
        {
            frm.setSalesBtnValue( BTN_OFF );
        }
        else
        {
            frm.setSalesBtnValue( BTN_ON );
        }
        frm.setSelDraftPlanId( selPlanId );
        frm.setSelDraftPlanNm( selPlanNm );
        frm.setDraftMode( DRAFT_ON );
        frm.setFrmSubList( logic.getSubFormList() );
        frm.setFrmMustOptionSubList( newMustOptFrmSubList );
        frm.setFrmCommOptionSubList( newCommOptFrmSubList );
        frm.setExistsRsv( false );
        return(frm);
    }

    /**
     * �������ۑ��{�^���N���b�N
     * 
     * @param id �z�e��ID
     * @param planID �v����ID
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @return �Ȃ�
     */
    private boolean execDelDraft(int id, int planId, LogicOwnerRsvPlan logic, FormOwnerRsvPlan frm) throws Exception
    {
        boolean ret = false;
        String msg = "";

        // �v�������I�����̓G���[
        if ( planId == 0 )
        {
            msg = msg + Message.getMessage( "warn.00002", "�폜�Ώۂ̃v����" ) + "<br />";
            frm.setErrMsg( msg );
            return(ret);
        }

        // �폜���s
        logic.execDelDraftPlan( id, planId );
        ret = true;

        return(ret);
    }

    /**
     * �������v�����ݒ�X�V�{�^���N���b�N
     * 
     * @param id �z�e��ID
     * @param planID �v����ID
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @return �Ȃ�
     */
    private boolean execRegDraftRelease(int id, int planId, LogicOwnerRsvPlan logic, FormOwnerRsvPlan frm) throws Exception
    {
        boolean ret = false;

        // ���̓`�F�b�N
        if ( isInputCheckMsg( frm ) == false )
        {
            return(ret);
        }

        // �v�����ݒ�X�V
        frm.setSalesFlag( 1 );
        logic.setFrm( frm );
        logic.registDraftPlan();
        ret = true;

        return(ret);
    }

}
