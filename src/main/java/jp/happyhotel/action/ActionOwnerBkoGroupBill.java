package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerBkoGroupBill;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoGroupBill;

public class ActionOwnerBkoGroupBill extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  MENU_FLG          = 180; // �O���[�v�X��������

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerBkoGroupBill frmBill;
        LogicOwnerBkoGroupBill logic;
        int paramHotelID = 0;
        String paramContractID = "";
        int paramUserId = 0;
        int hotelId = 0;
        int paramYear = 0;
        int paramMonth = 0;
        String errMsg = "";

        try
        {
            frmBill = new FormOwnerBkoGroupBill();
            frmMenu = new FormOwnerBkoMenu();
            logic = new LogicOwnerBkoGroupBill();

            // ����ʂ̒l���擾
            // �z�e��ID
            paramContractID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            paramYear = Integer.parseInt( request.getParameter( "selYear" ) );
            paramMonth = Integer.parseInt( request.getParameter( "selMonth" ) );
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }

            // �l���t�H�[���ɃZ�b�g
            frmBill.setSelContractID( paramContractID );
            frmBill.setSelUserID( paramUserId );
            frmBill.setSelYear( paramYear );
            frmBill.setSelMonth( paramMonth );
            frmBill.setSelHotelID( paramHotelID );

            // �f�[�^�擾
            logic.setFrm( frmBill );
            logic.getGroupBill();

            // ���j���[�̐ݒ�
            frmMenu.setUserId( paramUserId );
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                hotelId = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }

            OwnerRsvCommon.setMenu( frmMenu, hotelId, MENU_FLG, request.getCookies() );

            // ��ʑJ��
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_groupBill", frmBill );
            requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoGroupBill.execute() ][hotelId = " + paramContractID + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerBkoGroupBill.execute() ] - Unable to dispatch....." + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }
}
