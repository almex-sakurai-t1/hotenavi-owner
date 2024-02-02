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

public class ActionOwnerBkoBillMonth extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  MENU_FLG          = 150; // ���ʐ�������

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerBkoBillMonth frmBill;
        LogicOwnerBkoBillMonth logic;
        int paramHotelID = 0;
        int paramUserId = 0;
        int hotelId = 0;
        int paramYearFrom = 0;
        int paramMonthFrom = 0;
        int paramYearTo = 0;
        int paramMonthTo = 0;
        int paramrsvKind = 0;// 0:�S�� 1:�n�s�z�e�^�b�` 2:���u�C���\��̂�
        String errMsg = "";

        try
        {
            frmBill = new FormOwnerBkoBillMonth();
            frmMenu = new FormOwnerBkoMenu();
            logic = new LogicOwnerBkoBillMonth();

            // ����ʂ̒l���擾
            // �z�e��ID
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            paramYearFrom = Integer.parseInt( request.getParameter( "selYearFrom" ) );
            paramMonthFrom = Integer.parseInt( request.getParameter( "selMonthFrom" ) );
            paramYearTo = Integer.parseInt( request.getParameter( "selYearTo" ) );
            paramMonthTo = Integer.parseInt( request.getParameter( "selMonthTo" ) );
            if ( request.getParameter( "rsvKind" ) != null )
            {
                paramrsvKind = Integer.parseInt( request.getParameter( "rsvKind" ).toString() );
            }

            // �l���t�H�[���ɃZ�b�g
            frmBill.setSelHotelID( paramHotelID );
            frmBill.setSelYearFrom( paramYearFrom );
            frmBill.setSelMonthFrom( paramMonthFrom );
            frmBill.setSelYearTo( paramYearTo );
            frmBill.setSelMonthTo( paramMonthTo );
            frmBill.setRsvKind( paramrsvKind );

            // �f�[�^�擾
            logic.setFrm( frmBill );
            logic.getAccountRecv();

            // ���j���[�̐ݒ�
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, MENU_FLG, request.getCookies() );

            // ��ʑJ��
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_billMonth", frmBill );
            requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
            requestDispatcher.forward( request, response );
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
