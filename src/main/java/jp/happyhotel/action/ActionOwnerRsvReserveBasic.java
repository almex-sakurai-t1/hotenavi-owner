package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvReserveBasic;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvReserveBasic;

/**
 *
 * �\������
 */

public class ActionOwnerRsvReserveBasic extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;
    private static final int  menuFlg           = 2;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvReserveBasic frmReserveBasic;
        FormOwnerBkoMenu frmMenu;
        LogicOwnerRsvReserveBasic logic;
        int paramHotelID = 0;
        String paramChashDeposit = "";
        int paramParking = 0;
        int paramHiroof = 0;
        String paraOwnerHotelID = "";
        int paramUserId = 0;
        int paramEquipFlag = 0;
        String deadLineTimeHH = "";
        String deadLineTimeMM = "";
        String noShowCreditFlag = "";
        int hotelId = 0;
        boolean frmSetFlg = false;
        String errMsg = "";
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {
            logic = new LogicOwnerRsvReserveBasic();

            // ��ʂ̒l���擾
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            if ( (request.getParameter( "cashDeposit" ) == null) || (request.getParameter( "cashDeposit" ).trim().length() == 0) )
            {
                paramChashDeposit = "";
            }
            else
            {
                paramChashDeposit = request.getParameter( "cashDeposit" );
            }
            paramParking = Integer.parseInt( request.getParameter( "parking" ) );
            paramHiroof = Integer.parseInt( request.getParameter( "hiroof" ) );
            paramEquipFlag = Integer.parseInt( request.getParameter( "equip" ) );
            paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            deadLineTimeHH = request.getParameter( "deadLineTimeHH" );
            deadLineTimeMM = request.getParameter( "deadLineTimeMM" );

            logicMenu.setFrm( new FormOwnerBkoMenu() );
            disptype = logicMenu.getUserAuth( paraOwnerHotelID, paramUserId );
            adminflag = logicMenu.getAdminFlg( paraOwnerHotelID, paramUserId );
            imediaflag = OwnerRsvCommon.getImediaFlag( paraOwnerHotelID, paramUserId );

            if ( request.getParameter( "noshowFlag" ) != null )
            {
                noShowCreditFlag = request.getParameter( "noshowFlag" );
            }
            else
            {
                noShowCreditFlag = "0";
            }

            if ( (imediaflag == 1 && adminflag == 1 && paraOwnerHotelID.equals( "happyhotel" )) == false && disptype != OwnerRsvCommon.USER_AUTH_OWNER && disptype != OwnerRsvCommon.USER_AUTH_DEMO && disptype != OwnerRsvCommon.USER_AUTH_CALLCENTER )
            {
                // �����̂Ȃ����[�U�̓G���[�y�[�W��\������
                errMsg = Message.getMessage( "erro.30001", "�y�[�W���{�����錠��" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            frmReserveBasic = new FormOwnerRsvReserveBasic();
            frmMenu = new FormOwnerBkoMenu();

            // �l���t�H�[���ɃZ�b�g
            frmReserveBasic.setHotelId( paramHotelID );
            frmReserveBasic.setCashDeposit( paramChashDeposit );
            frmReserveBasic.setParking( paramParking );
            frmReserveBasic.setHiroof( paramHiroof );
            frmReserveBasic.setEquipDispFlag(paramEquipFlag);
            frmReserveBasic.setOwnerHotelID( paraOwnerHotelID );
            frmReserveBasic.setUserID( paramUserId );
            frmReserveBasic.setDeadlineTimeHH( deadLineTimeHH );
            frmReserveBasic.setDeadlineTimeMM( deadLineTimeMM );
            frmReserveBasic.setNoshowCreditFlag( Integer.parseInt( noShowCreditFlag ) );

            // ���̓`�F�b�N
            if ( isInputCheckMsg( frmReserveBasic ) == true )
            {
                if ( disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && paraOwnerHotelID.equals( "happyhotel" )) == false )
                {
                    // �����̂Ȃ����[�U�̓G���[�y�[�W��\������
                    errMsg = Message.getMessage( "erro.30001", "�X�e�[�^�X���X�V���錠��" );
                    request.setAttribute( "errMsg", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }
                // �f�[�^�̍X�V
                logic.setFrm( frmReserveBasic );
                logic.registHotelBase();
                frmSetFlg = true;
                // ����
                OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                paramUserId, OwnerRsvCommon.ADJUST_EDIT_ID_RSV_BASIC, 0, OwnerRsvCommon.ADJUST_MEMO_RSV_BASIC );
            }

            // ���j���[�A�\����̐ݒ�
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );
            setPage( frmReserveBasic, paramHotelID );

            // ���̓G���[�̏ꍇ�A���͒l�͕ێ�����
            if ( frmSetFlg == false )
            {
                frmReserveBasic.setDeadlineTimeHH( deadLineTimeHH );
                frmReserveBasic.setDeadlineTimeMM( deadLineTimeMM );
                frmReserveBasic.setCashDeposit( paramChashDeposit );
                frmReserveBasic.setParking( paramParking );
            }

            // ��ʑJ��
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_ReserveBasic", frmReserveBasic );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvReserveBasic.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvReserveBasic.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            logic = null;
        }
    }

    /**
     * ���͒l�`�F�b�N
     *
     * @param input FormOwnerRsvReserveBasic�I�u�W�F�N�g
     * @return true:����Afalse:�G���[����
     */
    private boolean isInputCheckMsg(FormOwnerRsvReserveBasic frm) throws Exception
    {
        String msg;
        boolean isCheck = false;

        msg = "";

        // ������ HH
        if ( CheckString.onlySpaceCheck( frm.getDeadlineTimeHH() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�������̎���" ) + "<br />";
        }
        else
        {
            if ( CheckString.numCheck( frm.getDeadlineTimeHH() ) == false )
            {
                msg = msg + Message.getMessage( "warn.00004", "�������̎���" ) + "<br />";
            }
            else
            {
                if ( (Integer.parseInt( frm.getDeadlineTimeHH() ) > 23 || Integer.parseInt( frm.getDeadlineTimeHH() ) < 0) )
                {
                    // ���Ԃ�0���`23���ȊO�̏ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�������̎���", "0�`23" ) + "<br />";
                }
            }
        }

        // ������ MM
        if ( CheckString.onlySpaceCheck( frm.getDeadlineTimeMM() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�������̕�" ) + "<br />";
        }
        else
        {
            if ( CheckString.numCheck( frm.getDeadlineTimeMM() ) == false )
            {
                msg = msg + Message.getMessage( "warn.00004", "�������̕�" ) + "<br />";
            }
            else
            {
                if ( (Integer.parseInt( frm.getDeadlineTimeMM() ) > 59 || Integer.parseInt( frm.getDeadlineTimeMM() ) < 0) )
                {
                    // ���Ԃ�0�`59���ȊO�̏ꍇ
                    msg = msg + Message.getMessage( "warn.30007", "�������̕�", "0�`59" ) + "<br />";
                }
            }
        }

        // �Œ�\����z
        if ( CheckString.onlySpaceCheck( frm.getCashDeposit() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�Œ�\����z" ) + "<br />";
        }
        else
        {
            if ( CheckString.numCheck( frm.getCashDeposit() ) == false )
            {
                msg = msg + Message.getMessage( "warn.00004", "�Œ�\����z" ) + "<br />";
            }
            else
            {
                if ( Integer.parseInt( frm.getCashDeposit() ) == 0 )
                {
                    msg = msg + Message.getMessage( "warn.30001" ) + "<br />";
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
     * �\���{����ݒ肷��
     *
     * @param frm FormOwnerU10302�I�u�W�F�N�g
     * @param selHotelID �I������Ă���z�e��ID
     * @return �Ȃ�
     */
    private void setPage(FormOwnerRsvReserveBasic frm, int selHotelID) throws Exception
    {
        LogicOwnerRsvReserveBasic logic = new LogicOwnerRsvReserveBasic();

        try
        {
            frm.setSelHotelID( selHotelID );
            logic.setFrm( frm );
            logic.getHotelRsv();

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvReserveBasic.setPage() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvReserveBasic.setPage() ] " + e.getMessage() );
        }
    }
}