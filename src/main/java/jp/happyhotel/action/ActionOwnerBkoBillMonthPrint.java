package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerBkoBillMonth;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoBillMonth;

public class ActionOwnerBkoBillMonthPrint extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  MENU_FLG          = 150; // ���ʐ������׈��

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerBkoBillMonth frmBill;
        // LogicOwnerBkoBillMonth logic;
        String paraOwnerHotelID = "";
        int paramHotelID = 0;
        int paramUserId = 0;
        int hotelId = 0;
        int menuFlg = 0;
        int imediaFlg = 0;
        String errMsg = "";
        int paramrsvKind = 0;// 0:�S�� 1:�n�s�z�e�^�b�` 2:���u�C���\��̂�

        try
        {
            frmBill = new FormOwnerBkoBillMonth();
            frmMenu = new FormOwnerBkoMenu();

            // ����ʂ̒l���擾
            // �z�e��ID
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );

            // �l���t�H�[���ɃZ�b�g
            frmBill.setSelHotelID( paramHotelID );
            menuFlg = MENU_FLG;

            // �����ǃt���O
            imediaFlg = OwnerRsvCommon.getImediaFlag( paraOwnerHotelID, paramUserId );

            frmBill.setSelYearFrom( Integer.parseInt( request.getParameter( "selYearFrom" ).toString() ) );
            frmBill.setSelMonthFrom( Integer.parseInt( request.getParameter( "selMonthFrom" ).toString() ) );
            frmBill.setSelYearTo( Integer.parseInt( request.getParameter( "selYearTo" ).toString() ) );
            frmBill.setSelMonthTo( Integer.parseInt( request.getParameter( "selMonthTo" ).toString() ) );
            frmBill.setRsvKind( Integer.parseInt( request.getParameter( "rsvKind" ).toString() ) );

            // ���̓`�F�b�N
            if ( errMsg.trim().length() != 0 )
            {
                // �G���[����
                frmBill.setErrMsg( errMsg );

                // ���j���[�̐ݒ�
                frmMenu.setUserId( paramUserId );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );
                frmBill.setBlankCloseFlg( true );

                request.setAttribute( "FORM_Menu", frmMenu );
                request.setAttribute( "FORM_billMonth", frmBill );
                requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
                requestDispatcher.forward( request, response );

                return;
            }

            LogicOwnerBkoBillMonth logic = new LogicOwnerBkoBillMonth();

            // �f�[�^�擾
            logic.setFrm( frmBill );
            logic.getAccountRecv();
            if ( !frmBill.getErrMsg().trim().equals( "" ) )
            {

                // ���j���[�̐ݒ�
                frmMenu.setUserId( paramUserId );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );
                frmBill.setBlankCloseFlg( true );

                // ��ʑJ��
                request.setAttribute( "FORM_Menu", frmMenu );
                request.setAttribute( "FORM_billMonth", frmBill );
                requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // �N���W�b�g���׏o��
            logic.doPost( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoBillMonth.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerBkoBillMonth.execute() ] - Unable to dispatch....." + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }
}