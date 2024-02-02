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
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvOption;
import jp.happyhotel.owner.FormOwnerRsvOptionManage;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvOption;
import jp.happyhotel.owner.LogicOwnerRsvOptionManage;

public class ActionOwnerRsvOption extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  menuFlg_ADD       = 91;
    private static final int  menuFlg_ADD_COMM  = 92;
    private static final int  menuFlg_UPD       = 9;
    private static final int  OPTIN_COMM        = 0;
    private static final int  OPTIN_MUST        = 1;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvOptionManage frmManage;
        FormOwnerRsvOption frmOpt;
        FormOwnerRsvOption newFrmOpt;
        FormOwnerBkoMenu frmMenu;
        LogicOwnerRsvOption logic;
        int hotelId = 0;
        int menuFlg = 0;
        boolean frmSetFlg = false;
        int edit_id = 0;
        String memo = "";
        String msg = "";
        String errMsg = "";
        int disptype = 0;
        int imediaflag = 0;
        int paramHotelID = 0;
        int paramUserId = 0;
        String hotenaviId = "";
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {
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

            if ( (request.getParameter( "updBtn" ) != null || request.getParameter( "commUpdBtn" ) != null ||
                    request.getParameter( "btnDel" ) != null || request.getParameter( "btnDel_usually" ) != null) &&
                    disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
            {
                // �����̂Ȃ����[�U�̓G���[�y�[�W��\������
                errMsg = Message.getMessage( "erro.30001", "�X�e�[�^�X���X�V���錠��" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            frmManage = new FormOwnerRsvOptionManage();
            frmMenu = new FormOwnerBkoMenu();
            frmOpt = new FormOwnerRsvOption();
            newFrmOpt = new FormOwnerRsvOption();
            logic = new LogicOwnerRsvOption();

            if ( request.getParameter( "addRow" ) != null )
            {
                // ��1�s�ǉ��{�^��
                menuFlg = menuFlg_ADD;

                // ��ʂ̒l���擾
                newFrmOpt = getViewData( frmOpt, request );

                // �Ώۂ̃I�v�V���������擾
                logic.setFrm( newFrmOpt );
                logic.getOpt( 1 );

                // ��ʂ̒l���擾
                newFrmOpt = getViewData( logic.getFrm(), request );
                newFrmOpt.setOptionNmMustView( logic.getFrm().getOptionNmMustView() );

                addRow( newFrmOpt );
                request.setAttribute( "FORM_Option", newFrmOpt );
            }
            else if ( request.getParameter( "updBtn" ) != null )
            {
                // ���K�{�I�v�V�����ݒ�X�V�{�^��
                menuFlg = menuFlg_UPD;

                // ��ʂ̒l���擾
                newFrmOpt = getViewData( frmOpt, request );

                // ���̓`�F�b�N
                if ( inputCheck( newFrmOpt, 1 ) == true )
                {
                    // �o�^����
                    registOption( newFrmOpt );
                    frmSetFlg = true;

                    // �z�e���C������
                    if ( request.getParameter( "optionNmView" ).length() == 0 )
                    {
                        edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_OPTION_ADD;
                        memo = OwnerRsvCommon.ADJUST_MEMO_OPTION_ADD;
                    }
                    else
                    {
                        edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_OPTION_UPD;
                        memo = OwnerRsvCommon.ADJUST_MEMO_OPTION_UPD;
                    }
                    OwnerRsvCommon.addAdjustmentHistory( frmOpt.getSelHotelId(), OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                            frmOpt.getUserId(), edit_id, frmOpt.getSelOptId(), memo );
                }

                if ( frmSetFlg == true )
                {
                    // �Ǘ���ʂ�\��
                    frmManage = setOptionManage( newFrmOpt.getSelHotelId() );
                    request.setAttribute( "FORM_OptionManage", frmManage );
                }
                else
                {
                    // ���̓G���[�̏ꍇ
                    menuFlg = menuFlg_ADD;

                    // �Ώۂ̃I�v�V���������擾
                    logic.setFrm( frmOpt );
                    logic.getOpt( 1 );

                    // ���͒l��ێ�
                    newFrmOpt = getViewData( frmOpt, request );
                    request.setAttribute( "FORM_Option", newFrmOpt );
                }

            }
            else if ( request.getParameter( "commUpdBtn" ) != null )
            {
                // ���ʏ�I�v�V�����ݒ�X�V�{�^��
                menuFlg = menuFlg_UPD;

                // ��ʂ̒l���擾
                newFrmOpt = getCommViewData( frmOpt, request );

                // ���̓`�F�b�N
                if ( inputCheck( newFrmOpt, OPTIN_COMM ) == true )
                {
                    // �o�^����
                    registCommOption( newFrmOpt );
                    frmSetFlg = true;

                    // �z�e���C������
                    if ( request.getParameter( "optNmCommView" ).length() == 0 )
                    {
                        edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_OPTION_ADD;
                        memo = OwnerRsvCommon.ADJUST_MEMO_OPTION_ADD;
                    }
                    else
                    {
                        edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_OPTION_UPD;
                        memo = OwnerRsvCommon.ADJUST_MEMO_OPTION_UPD;
                    }
                    OwnerRsvCommon.addAdjustmentHistory( frmOpt.getSelHotelId(), OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                            frmOpt.getUserId(), edit_id, frmOpt.getSelOptId(), memo );

                }

                if ( frmSetFlg == true )
                {
                    // �Ǘ���ʂ�\��
                    frmManage = setOptionManage( newFrmOpt.getSelHotelId() );
                    request.setAttribute( "FORM_OptionManage", frmManage );
                }
                else
                {
                    // ���̓G���[�̏ꍇ
                    menuFlg = menuFlg_ADD_COMM;

                    // �Ώۂ̃I�v�V���������擾
                    logic.setFrm( newFrmOpt );
                    logic.getOpt( 0 );

                    // ���͒l��ێ�
                    newFrmOpt = getCommViewData( frmOpt, request );
                    request.setAttribute( "FORM_Option", newFrmOpt );
                }
            }
            else if ( request.getParameter( "btnBack" ) != null )
            {
                // ���߂�
                menuFlg = menuFlg_UPD;
                newFrmOpt = getCommViewData( frmOpt, request );
                frmManage = setOptionManage( newFrmOpt.getSelHotelId() );
                request.setAttribute( "FORM_OptionManage", frmManage );
            }
            else if ( request.getParameter( "btnDel" ) != null )
            {
                // ���K�{�I�v�V�����폜�{�^��
                menuFlg = menuFlg_UPD;

                // ��ʂ̒l���擾
                newFrmOpt = getCommViewData( frmOpt, request );

                // �I�v�V�������v�����A�v�����������A�\��ɓo�^����Ă��邩
                msg = checkDelOption( frmOpt.getSelHotelId(), frmOpt.getSelOptId() );
                if ( msg.trim().length() > 0 )
                {
                    // �\��f�[�^�����݂���ꍇ�͍폜�s��
                    menuFlg = menuFlg_ADD;

                    // �Ώۂ̃I�v�V���������擾
                    logic.setFrm( newFrmOpt );
                    logic.getOpt( 1 );

                    // ���͒l���
                    newFrmOpt = getCommViewData( logic.getFrm(), request );
                    newFrmOpt.setErrMsg( msg );
                    request.setAttribute( "FORM_Option", newFrmOpt );
                }
                else
                {
                    // �폜����
                    logic.deleteOption( frmOpt.getSelHotelId(), frmOpt.getSelOptId() );

                    // �z�e���C������
                    edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_OPTION_DEL;
                    memo = OwnerRsvCommon.ADJUST_MEMO_OPTION_DEL;
                    OwnerRsvCommon.addAdjustmentHistory( frmOpt.getSelHotelId(), OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                            frmOpt.getUserId(), edit_id, frmOpt.getSelOptId(), memo );

                    // �Ǘ���ʂ�\��
                    frmManage = setOptionManage( newFrmOpt.getSelHotelId() );
                    request.setAttribute( "FORM_OptionManage", frmManage );
                }
            }
            else if ( request.getParameter( "btnDel_usually" ) != null )
            {
                // ���폜�{�^��
                menuFlg = menuFlg_UPD;

                // ��ʂ̒l���擾
                newFrmOpt = getCommViewData( frmOpt, request );

                // �I�v�V�������v�����A�v�����������A�\��ɓo�^����Ă��邩
                msg = checkDelOption( frmOpt.getSelHotelId(), frmOpt.getSelOptId() );
                if ( msg.trim().length() > 0 )
                {
                    // �\��f�[�^�����݂���ꍇ�͍폜�s��
                    menuFlg = menuFlg_ADD_COMM;

                    // �Ώۂ̃I�v�V���������擾
                    logic.setFrm( newFrmOpt );
                    logic.getOpt( 0 );

                    // ���͒l���
                    newFrmOpt = getCommViewData( logic.getFrm(), request );
                    newFrmOpt.setErrMsg( Message.getMessage( "warn.30022" ) );
                    request.setAttribute( "FORM_Option", newFrmOpt );
                }
                else
                {
                    // �폜����
                    logic.deleteOption( frmOpt.getSelHotelId(), frmOpt.getSelOptId() );

                    // �z�e���C������
                    edit_id = OwnerRsvCommon.ADJUST_EDIT_ID_OPTION_DEL;
                    memo = OwnerRsvCommon.ADJUST_MEMO_OPTION_DEL;
                    OwnerRsvCommon.addAdjustmentHistory( frmOpt.getSelHotelId(), OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                            frmOpt.getUserId(), edit_id, frmOpt.getSelOptId(), memo );

                    // �Ǘ���ʂ�\��
                    frmManage = setOptionManage( newFrmOpt.getSelHotelId() );
                    request.setAttribute( "FORM_OptionManage", frmManage );
                }
            }

            // ��ʑJ��
            OwnerRsvCommon.setMenu( frmMenu, newFrmOpt.getSelHotelId(), menuFlg, request.getCookies() );
            request.setAttribute( "FORM_Menu", frmMenu );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvOptionManage.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvOptionManage.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }

    /**
     * �K�{�I�v�V������ʂ̓��e���t�H�[���ɃZ�b�g
     * 
     * @param frm FormOwnerRsvOption�I�u�W�F�N�g
     * @param request HttpServletRequest�I�u�W�F�N�g
     * @return FormOwnerRsvOption�I�u�W�F�N�g
     */
    private FormOwnerRsvOption getViewData(FormOwnerRsvOption frm, HttpServletRequest request)
    {
        int paramHotelId = 0;
        int paramUserId = 0;
        int paramOptId = 0;
        int paramMaxRow = 0;
        int paramMaxSubOptId = 0;
        String paraOwnerHotelID = "";
        String paramDispIdx = "";
        String paramOptionNm = "";
        String paramOptionNmSearch = "";
        String[] dels;
        ArrayList<Integer> delOptSubIdList = new ArrayList<Integer>();
        ArrayList<Integer> subIdList = new ArrayList<Integer>();
        ArrayList<String> subNmList = new ArrayList<String>();

        if ( request.getParameter( "selHotelIDValue" ) != null )
        {
            paramHotelId = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
        }
        paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
        paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
        paramOptId = Integer.parseInt( request.getParameter( "optId" ) );
        paramMaxRow = Integer.parseInt( request.getParameter( "maxRow" ) );
        paramMaxSubOptId = Integer.parseInt( request.getParameter( "maxSubOptId" ) );
        paramOptionNm = request.getParameter( "optionNm" );
        paramOptionNmSearch = request.getParameter( "optionNmSearch" );
        paramDispIdx = request.getParameter( "dispIdx" );

        // �T�u�I�v�V�������擾
        dels = request.getParameterValues( "del" );
        if ( dels != null )
        {
            for( int i = 0 ; i < dels.length ; i++ )
            {
                delOptSubIdList.add( Integer.parseInt( dels[i] ) );
            }
        }
        for( int i = 0 ; i < paramMaxRow ; i++ )
        {
            subIdList.add( Integer.parseInt( request.getParameter( "subOptId" + i ) ) );
            subNmList.add( request.getParameter( "subOptNm" + i ) );
        }

        frm.setSelHotelId( paramHotelId );
        frm.setOwnerHotelID( paraOwnerHotelID );
        frm.setUserId( paramUserId );
        frm.setSelOptId( paramOptId );
        frm.setMaxRow( paramMaxRow );
        frm.setMaxSubOptId( paramMaxSubOptId );
        frm.setOptionNm_Must( paramOptionNm );
        frm.setOptionNm_MustSearch( paramOptionNmSearch );
        frm.setDispIdx( paramDispIdx );
        frm.setOptSubIdMustList( subIdList );
        frm.setOptSubNmMustList( subNmList );
        frm.setDelOptSubIdList( delOptSubIdList );

        return(frm);
    }

    /**
     * �ʏ�I�v�V������ʂ̓��e���t�H�[���ɃZ�b�g
     * 
     * @param frm FormOwnerRsvOption�I�u�W�F�N�g
     * @param request HttpServletRequest�I�u�W�F�N�g
     * @return FormOwnerRsvOption�I�u�W�F�N�g
     */
    private FormOwnerRsvOption getCommViewData(FormOwnerRsvOption frm, HttpServletRequest request)
    {
        int paramHotelId = 0;
        int paramUserId = 0;
        int paramOptId = 0;
        String paraOwnerHotelID = "";
        String paramDispIdx = "";
        String paramOptionNm = "";
        String paramOptionNmSearch = "";
        String paramOptCharge = "";
        String paramOptChargeView = "";
        String paramMaxQuantity = "";
        String paramMaxQuantityView = "";
        String paramInpMaxQuantity = "";
        String paramInpMaxQuantityView = "";
        String paramLimitDate = "";
        String paramLimitDateView = "";
        String paramLimitHH = "";
        String paramLimitHHView = "";
        String paramLimitMM = "";
        String paramLimitMMView = "";

        if ( request.getParameter( "selHotelIDValue" ) != null )
        {
            paramHotelId = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
        }
        paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
        paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
        paramOptId = Integer.parseInt( request.getParameter( "optId" ) );
        paramOptionNm = request.getParameter( "optNm" );
        paramOptionNmSearch = request.getParameter( "optNmSearch" );

        paramDispIdx = request.getParameter( "comDispIdx" );
        paramOptCharge = request.getParameter( "optCharge" );
        paramOptChargeView = request.getParameter( "optChargeView" );
        paramMaxQuantity = request.getParameter( "maxQuantity" );
        paramMaxQuantityView = request.getParameter( "maxQuantityView" );
        paramInpMaxQuantity = request.getParameter( "inpMaxQuantity" );
        paramInpMaxQuantityView = request.getParameter( "inpMaxQuantityView" );
        paramLimitDate = request.getParameter( "cancelLimitDate" );
        paramLimitDateView = request.getParameter( "cancelLimitDateView" );
        paramLimitHH = request.getParameter( "cancelLimitTimeHH" );
        paramLimitHHView = request.getParameter( "cancelLimitTimeHHView" );
        paramLimitMM = request.getParameter( "cancelLimitTimeMM" );
        paramLimitMMView = request.getParameter( "cancelLimitTimeMMView" );

        frm.setSelHotelId( paramHotelId );
        frm.setOwnerHotelID( paraOwnerHotelID );
        frm.setUserId( paramUserId );
        frm.setSelOptId( paramOptId );
        frm.setOptionNm_Comm( paramOptionNm );
        frm.setOptionNm_CommSearch( paramOptionNmSearch );
        frm.setDispIndexComm( paramDispIdx );
        frm.setOptCharge( paramOptCharge );
        frm.setOptChargeView( paramOptChargeView );
        frm.setMaxQuantity( paramMaxQuantity );
        frm.setMaxQuantityView( paramMaxQuantityView );
        frm.setInpMaxQuantity( paramInpMaxQuantity );
        frm.setInpMaxQuantityView( paramInpMaxQuantityView );
        frm.setCancelLimitDate( paramLimitDate );
        frm.setCancelLimitDateView( paramLimitDateView );
        frm.setCancelLimitTimeHH( paramLimitHH );
        frm.setCancelLimitTimeHHView( paramLimitHHView );
        frm.setCancelLimitTimeMM( paramLimitMM );
        frm.setCancelLimitTimeMMView( paramLimitMMView );

        return(frm);
    }

    /**
     * 1�s�ǉ�����
     * 
     * @param frm FormOwnerRsvPlan�I�u�W�F�N�g
     * @return �Ȃ�
     */
    private void addRow(FormOwnerRsvOption frm) throws Exception
    {
        int newMaxSubOptId = 0;
        ArrayList<Integer> subIdList;
        ArrayList<String> subNmList;

        // �t�H�[���̓��e���擾
        subIdList = frm.getOptSubIdMustList();
        subNmList = frm.getOptSubNmMustList();

        // �V�����T�u�I�v�V����ID���擾
        newMaxSubOptId = frm.getMaxSubOptId() + 1;

        subIdList.add( newMaxSubOptId );
        subNmList.add( "" );

        frm.setSelHotelId( frm.getSelHotelId() );
        frm.setMaxRow( frm.getMaxRow() + 1 );
        frm.setMaxSubOptId( newMaxSubOptId );
        frm.setOptSubIdMustList( subIdList );
        frm.setOptSubNmMustList( subNmList );
    }

    /**
     * �K�{�I�v�V�����f�[�^�o�^����
     * 
     * @param frm FormOwnerRsvOption�I�u�W�F�N�g
     * @return �Ȃ�
     */
    private void registOption(FormOwnerRsvOption frm) throws Exception
    {
        LogicOwnerRsvOption logic = new LogicOwnerRsvOption();

        // �o�^����
        logic.setFrm( frm );
        logic.registOption();
    }

    /**
     * �ʏ�I�v�V�����f�[�^�o�^����
     * 
     * @param frm FormOwnerRsvOption�I�u�W�F�N�g
     * @return �Ȃ�
     */
    private void registCommOption(FormOwnerRsvOption frm) throws Exception
    {
        LogicOwnerRsvOption logic = new LogicOwnerRsvOption();

        // �o�^����
        logic.setFrm( frm );
        logic.registCommOption();
    }

    /**
     * ��ʂ̓��e���t�H�[���ɃZ�b�g
     * 
     * @param frm FormOwnerRsvOption�I�u�W�F�N�g
     * @param checkKbn �`�F�b�N�Ώ�(0:�ʏ�I�v�V�����A1�F�K�{�I�v�V����)
     * @return true:�`�F�b�NOK�AFalse;�`�F�b�NNG
     */
    private boolean inputCheck(FormOwnerRsvOption frm, int checkKbn) throws Exception
    {
        boolean ret = false;
        String msg = "";
        int lenCheck = 0;
        boolean checkFlg = false;
        boolean maxNumCheck = true;

        if ( checkKbn == OPTIN_MUST )
        {
            // ���K�{�I�v�V����
            // �\����
            if ( CheckString.onlySpaceCheck( frm.getDispIdx() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�\����" ) + "<br />";
            }
            else
            {
                if ( (OwnerRsvCommon.numCheck( frm.getDispIdx() ) == false) )
                {
                    // ���p�����ȊO�����͂���Ă���ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�\����", "0�ȏ�̐��l" ) + "<br />";
                }
            }

            // �I�v�V������
            if ( CheckString.onlySpaceCheck( frm.getOptionNm_Must() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�I�v�V������" ) + "<br />";
            }
            else
            {
                lenCheck = OwnerRsvCommon.LengthCheck( frm.getOptionNm_Must().trim(), 50 );
                if ( lenCheck == 1 )
                {
                    // ����Over�̏ꍇ
                    msg = msg + Message.getMessage( "warn.00038", "�I�v�V������", "25" ) + "<br />";
                }
            }
            // �����\���I�v�V������
            if ( CheckString.onlySpaceCheck( frm.getOptionNm_MustSearch() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�����\���I�v�V������" ) + "<br />";
            }
            else
            {
                lenCheck = OwnerRsvCommon.LengthCheck( frm.getOptionNm_MustSearch().trim(), 50 );
                if ( lenCheck == 1 )
                {
                    // ����Over�̏ꍇ
                    msg = msg + Message.getMessage( "warn.00038", "�����\���I�v�V������", "25" ) + "<br />";
                }
            }

            // �T�u�I�v�V�������
            for( int i = 0 ; i < frm.getOptSubIdMustList().size() ; i++ )
            {
                checkFlg = false;
                for( int j = 0 ; j < frm.getDelOptSubIdList().size() ; j++ )
                {
                    if ( frm.getOptSubIdMustList().get( i ) != frm.getDelOptSubIdList().get( j ) )
                    {
                        // �폜�Ώۂł͂Ȃ��ꍇ
                        continue;
                    }

                    // �폜�Ώۂ̏ꍇ
                    checkFlg = true;
                }

                if ( checkFlg == false )
                {
                    if ( CheckString.onlySpaceCheck( frm.getOptSubNmMustList().get( i ) ) == true )
                    {
                        // �����́E�󔒕����̏ꍇ
                        msg = msg + Message.getMessage( "warn.00001", "�T�u�I�v�V������" ) + "<br />";
                    }
                    else
                    {
                        lenCheck = OwnerRsvCommon.LengthCheck( frm.getOptSubNmMustList().get( i ), 50 );
                        if ( lenCheck == 1 )
                        {
                            // ����Over�̏ꍇ
                            msg = msg + Message.getMessage( "warn.00038", "�T�u�I�v�V������", "25" ) + "<br />";
                        }
                    }
                }
            }

            frm.setErrMsg( msg );
            if ( msg.trim().length() == 0 )
            {
                ret = true;
            }
            return(ret);
        }

        // ���ʏ�I�v�V����
        // �\����
        if ( CheckString.onlySpaceCheck( frm.getDispIndexComm() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�\����" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getDispIndexComm() ) == false) )
            {
                // ���p�����ȊO�����͂���Ă���ꍇ
                msg = msg + Message.getMessage( "warn.30007", "�\����", "0�ȏ�̐��l" ) + "<br />";
            }
        }

        // �I�v�V������
        if ( CheckString.onlySpaceCheck( frm.getOptionNm_Comm() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�I�v�V������" ) + "<br />";
        }
        else
        {
            lenCheck = OwnerRsvCommon.LengthCheck( frm.getOptionNm_Comm().trim(), 50 );
            if ( lenCheck == 1 )
            {
                // ����Over�̏ꍇ
                msg = msg + Message.getMessage( "warn.00038", "�I�v�V������", "25" ) + "<br />";
            }
        }

        // �����\���I�v�V������
        if ( CheckString.onlySpaceCheck( frm.getOptionNm_CommSearch() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�����\���I�v�V������" ) + "<br />";
        }
        else
        {
            lenCheck = OwnerRsvCommon.LengthCheck( frm.getOptionNm_CommSearch().trim(), 50 );
            if ( lenCheck == 1 )
            {
                // ����Over�̏ꍇ
                msg = msg + Message.getMessage( "warn.00038", "�����\���I�v�V������", "25" ) + "<br />";
            }
        }

        // �I�v�V��������
        if ( CheckString.onlySpaceCheck( frm.getOptCharge() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�I�v�V��������" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getOptCharge() ) == false) )
            {
                // ���p�����ȊO�����͂���Ă���ꍇ
                msg = msg + Message.getMessage( "warn.30007", "�I�v�V��������", "0�ȏ�̐��l" ) + "<br />";
            }
        }

        // 1������������
        if ( CheckString.onlySpaceCheck( frm.getMaxQuantity() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "1������������" ) + "<br />";
            maxNumCheck = false;
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getMaxQuantity() ) == false) )
            {
                // ���p�����ȊO�����͂���Ă���ꍇ
                msg = msg + Message.getMessage( "warn.30007", "1������������", "1�ȏ�̐��l" ) + "<br />";
                maxNumCheck = false;
            }
            else if ( Integer.parseInt( frm.getMaxQuantity() ) < 1 )
            {
                // ���p�����ȊO�����͂���Ă���ꍇ
                msg = msg + Message.getMessage( "warn.30007", "1������������", "1�ȏ�̐��l" ) + "<br />";
                maxNumCheck = false;
            }
        }

        // 1�񓖂�������
        if ( CheckString.onlySpaceCheck( frm.getInpMaxQuantity() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "1�񓖂�������" ) + "<br />";
            maxNumCheck = false;
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getInpMaxQuantity() ) == false) )
            {
                // ���p�����ȊO�����͂���Ă���ꍇ
                msg = msg + Message.getMessage( "warn.30007", "1�񓖂�������", "1�ȏ�̐��l" ) + "<br />";
                maxNumCheck = false;
            }
            else if ( Integer.parseInt( frm.getInpMaxQuantity() ) < 1 )
            {
                // ���p�����ȊO�����͂���Ă���ꍇ
                msg = msg + Message.getMessage( "warn.30007", "1�񓖂�������", "1�ȏ�̐��l" ) + "<br />";
                maxNumCheck = false;
            }
        }

        // 1�񂠂���̏������1��������̏������葽���ꍇ�̓G���[
        if ( (maxNumCheck == true) && (Integer.parseInt( frm.getMaxQuantity() ) < Integer.parseInt( frm.getInpMaxQuantity() )) )
        {
            msg = msg + Message.getMessage( "warn.30018" ) + "<br />";
        }

        // ��d������(���ɂ�)
        if ( CheckString.onlySpaceCheck( frm.getCancelLimitDate() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "��d�������̓��ɂ�" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getCancelLimitDate() ) == false) )
            {
                // ���p�����ȊO�����͂���Ă���ꍇ
                msg = msg + Message.getMessage( "warn.30007", "��d�������̓��ɂ�", "0�ȏ�̐��l" ) + "<br />";
            }
        }

        // ��d������(����)
        if ( (CheckString.onlySpaceCheck( frm.getCancelLimitTimeHH() ) == true) && (CheckString.onlySpaceCheck( frm.getCancelLimitTimeMM() ) == true) )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "��d�������̎���" ) + "<br />";
        }
        else
        {
            if ( (OwnerRsvCommon.numCheck( frm.getCancelLimitTimeHH() ) == false) || (OwnerRsvCommon.numCheck( frm.getCancelLimitTimeMM() ) == false) )
            {
                // ���p�����ȊO�����͂���Ă���ꍇ
                msg = msg + Message.getMessage( "warn.30007", "��d�������̎���", "����" ) + "<br />";
            }
            else
            {
                if ( (Integer.parseInt( frm.getCancelLimitTimeHH() ) > 23 || Integer.parseInt( frm.getCancelLimitTimeHH() ) < 0)
                        || (Integer.parseInt( frm.getCancelLimitTimeHH() ) > 23 || Integer.parseInt( frm.getCancelLimitTimeHH() ) < 0) )
                {
                    // ���Ԃ�0���`23���ȊO�̏ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "��d�������̎���", "0�`23" ) + "<br />";
                }
                if ( (Integer.parseInt( frm.getCancelLimitTimeMM() ) > 59 || Integer.parseInt( frm.getCancelLimitTimeMM() ) < 0)
                        || (Integer.parseInt( frm.getCancelLimitTimeMM() ) > 59 || Integer.parseInt( frm.getCancelLimitTimeMM() ) < 0) )
                {
                    // ����0���`59�ȊO�̏ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "��d�������̕�", "0�`59" ) + "<br />";
                }
            }
        }

        frm.setErrMsg( msg );
        if ( msg.trim().length() == 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * �I�v�V�����Ǘ���ʕ\��
     * 
     * @param hoteiID �z�e��ID
     * @return FormOwnerRsvOptionManage�I�u�W�F�N�g
     */
    private FormOwnerRsvOptionManage setOptionManage(int hotelID) throws Exception
    {
        LogicOwnerRsvOptionManage logic = new LogicOwnerRsvOptionManage();
        FormOwnerRsvOptionManage frm = new FormOwnerRsvOptionManage();

        frm.setSelHotelId( hotelID );
        logic.setFrm( frm );
        logic.getOption();

        return(logic.getFrm());
    }

    /**
     * �I�v�V�����o�^�`�F�b�N
     * 
     * @param hoteiID �z�e��ID
     * @return String �G���[���b�Z�[�W
     */
    private String checkDelOption(int hotelId, int optId) throws Exception
    {

        String retMsg = "";
        LogicOwnerRsvOption logic = new LogicOwnerRsvOption();

        // �\��f�[�^���݃`�F�b�N
        if ( logic.existsReserve( hotelId, optId ) == true )
        {
            retMsg = Message.getMessage( "warn.30022" );
            return(retMsg);
        }

        // �v�������݃`�F�b�N
        if ( logic.existsPlanOption( hotelId, optId, 1 ) == true )
        {
            retMsg = Message.getMessage( "warn.30023" );
            return(retMsg);
        }

        // ���������݃`�F�b�N
        if ( logic.existsPlanOption( hotelId, optId, 2 ) == true )
        {
            retMsg = Message.getMessage( "warn.30024" );
            return(retMsg);
        }

        return(retMsg);
    }
}
