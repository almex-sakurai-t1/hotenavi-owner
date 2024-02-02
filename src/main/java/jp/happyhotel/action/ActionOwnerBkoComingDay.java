package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerBkoCommon;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerBkoComingDay;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoComingDay;

public class ActionOwnerBkoComingDay extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  MENU_FLG          = 110; // ���ʗ��X�󋵊m�F���

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerBkoComingDay frmDay;
        LogicOwnerBkoComingDay logic;
        int paramHotelID = 0;
        int paramUserId = 0;
        String paramYear = "";
        String paramMonth = "";
        String paramHotenaviId = "";
        int hotelId = 0;
        int menuFlg = 0;
        String errMsg = "";
        int billFlg = 0;

        try
        {
            frmDay = new FormOwnerBkoComingDay();
            frmMenu = new FormOwnerBkoMenu();
            logic = new LogicOwnerBkoComingDay();

            // ����ʂ̒l���擾
            // �z�e��ID
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            paramHotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            paramYear = request.getParameter( "selYear" );
            paramMonth = request.getParameter( "selMonth" );

            // �l���t�H�[���ɃZ�b�g
            frmDay.setSelHotelID( paramHotelID );
            frmDay.setSelYear( Integer.parseInt( paramYear ) );
            frmDay.setSelMonth( Integer.parseInt( paramMonth ) );
            menuFlg = MENU_FLG;

            // �����{���\�t���O
            billFlg = OwnerBkoCommon.getBillOwnFlg( paramHotenaviId, paramUserId );
            frmDay.setBillFlg( billFlg );

            // �f�[�^�擾
            logic.setFrm( frmDay );
            logic.getAccountRecv();

            // ���j���[�̐ݒ�
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

            // ��ʑJ��
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_comDay", frmDay );
            requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoComingDay.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerBkoComingDay.execute() ] - Unable to dispatch....." + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }
}
