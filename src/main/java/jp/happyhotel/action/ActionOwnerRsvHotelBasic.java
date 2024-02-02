package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.owner.FormOwnerRsvHotelBasic;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvHotelBasic;
import jp.happyhotel.owner.LogicOwnerBkoMenu;

/**
 * 
 * �{�݊�{�����
 */

public class ActionOwnerRsvHotelBasic extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvHotelBasic frmHotelBasic;
        FormOwnerBkoMenu frmMenu;
        LogicOwnerRsvHotelBasic logic;
        int paramHotelID = 0;
        String paramRsvPR = "";
        String paraOwnerHotelID = "";
        String paramChildCharge = "";
        int paramUserId = 0;
        int hotelId = 0;
        boolean frmSetFlg = false;
        String errMsg = "";
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {

            logic = new LogicOwnerRsvHotelBasic();

            // ��ʂ̒l���擾
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paramRsvPR = ((String)request.getParameter( "reservePR" )).trim();
            paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            paramChildCharge = ((String)request.getParameter( "childCharge" )).trim();

            logicMenu.setFrm( new FormOwnerBkoMenu() );
            disptype = logicMenu.getUserAuth( paraOwnerHotelID, paramUserId );
            adminflag = logicMenu.getAdminFlg( paraOwnerHotelID, paramUserId );
            imediaflag = OwnerRsvCommon.getImediaFlag( paraOwnerHotelID, paramUserId );

            if ( (imediaflag == 1 && adminflag == 1 && paraOwnerHotelID.equals( "happyhotel" )) == false && disptype != OwnerRsvCommon.USER_AUTH_OWNER && disptype != OwnerRsvCommon.USER_AUTH_DEMO && disptype != OwnerRsvCommon.USER_AUTH_CALLCENTER )
            {
                // �����̂Ȃ����[�U�̓G���[�y�[�W��\������
                errMsg = Message.getMessage( "erro.30001", "�y�[�W���{�����錠��" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            frmHotelBasic = new FormOwnerRsvHotelBasic();
            frmMenu = new FormOwnerBkoMenu();

            // �l���t�H�[���ɃZ�b�g
            frmHotelBasic.setHotelId( paramHotelID );
            frmHotelBasic.setReservePr( paramRsvPR );
            frmHotelBasic.setOwnerHotelID( paraOwnerHotelID );
            frmHotelBasic.setUserID( paramUserId );
            frmHotelBasic.setChildCharge( paramChildCharge );

            // ���̓`�F�b�N
            if ( isInputCheckMsg( frmHotelBasic ) != false )
            {
                if ( disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && paraOwnerHotelID.equals( "happyhotel" )) == false )
                {
                    errMsg = Message.getMessage( "erro.30001", "�X�e�[�^�X���X�V���錠��" );
                    request.setAttribute( "errMsg", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }
                // �f�[�^�̍X�V
                logic.setFrm( frmHotelBasic );
                logic.registHotelBase();
                frmSetFlg = true;
                // �z�e���C������
                OwnerRsvCommon.addAdjustmentHistory( paramHotelID, OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ),
                                paramUserId, OwnerRsvCommon.ADJUST_EDIT_ID_HOTEL_BASIC, 0, OwnerRsvCommon.ADJUST_MEMO_HOTEL_BASIC );
            }

            // ���j���[�A�{�ݏ��̐ݒ�
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, 1, request.getCookies() );
            setPage( frmHotelBasic, paramHotelID );

            // ���̓G���[�̏ꍇ�A���͒l�͕ێ�����
            if ( frmSetFlg == false )
            {
                frmHotelBasic.setReservePr( paramRsvPR );
            }

            // ��ʑJ��
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_HotelBasic", frmHotelBasic );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvHotelBasic.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvHotelBasic.execute() ] - Unable to dispatch....."
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
     * @param input FormOwnerU010301�I�u�W�F�N�g
     * @return true:����Afalse:�G���[����
     */
    private boolean isInputCheckMsg(FormOwnerRsvHotelBasic frm) throws Exception
    {
        String msg;
        int ret;
        boolean isCheck = false;
        ReserveCommon rsvcomm = new ReserveCommon();

        msg = "";
        ret = 0;

        // �\��PR
        if ( CheckString.onlySpaceCheck( frm.getReservePr() ) == true )
        {
            // �����́E�󔒕����̏ꍇ
            msg = msg + Message.getMessage( "warn.00001", "�\��PR" ) + "<br />";
        }
        else
        {
            ret = OwnerRsvCommon.LengthCheck( frm.getReservePr().trim(), 50 );
            if ( ret == 1 )
            {
                // ����Over�̏ꍇ
                msg = msg + Message.getMessage( "warn.00038", "�\��PR", "25" ) + "<br />";
            }
        }

        // �q�������̒�`(���ًƖ@�̂�)
        if ( rsvcomm.checkLoveHotelFlag( frm.getHotelId() ) == false )
        {
            if ( CheckString.onlySpaceCheck( frm.getChildCharge() ) == true )
            {
                // �����́E�󔒕����̏ꍇ
                msg = msg + Message.getMessage( "warn.00001", "�q��������`" ) + "<br />";
            }
            else
            {
                ret = OwnerRsvCommon.LengthCheck( frm.getChildCharge(), 60 );
                if ( ret == 1 )
                {
                    // ����Over�̏ꍇ
                    msg = msg + Message.getMessage( "warn.00038", "�q��������`", "30" ) + "<br />";
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
     * �{�݊�{����ݒ肷��
     * 
     * @param frm FormOwnerU010301�I�u�W�F�N�g
     * @param selHotelID �I������Ă���z�e��ID
     * @return �Ȃ�
     */
    private void setPage(FormOwnerRsvHotelBasic frm, int selHotelID) throws Exception
    {
        LogicOwnerRsvHotelBasic logic = new LogicOwnerRsvHotelBasic();

        try
        {
            frm.setSelHotelID( selHotelID );
            logic.setFrm( frm );
            logic.getHotelRsv();

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOwnerRsvHotelBasic.setMenu() ] " + e.getMessage() );
            throw new Exception( "[ActionOwnerRsvHotelBasic.setMenu() ] " + e.getMessage() );
        }
    }
}