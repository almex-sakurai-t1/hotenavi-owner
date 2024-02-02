package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerBkoCreditDetail;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoCreditDetail;

public class ActionOwnerBkoCreditDetailPrint extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;
    private static final int  MENU_FLG          = 200; // �N���W�b�g�ڍ׈��

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerBkoCreditDetail frmCredit;
        String paraOwnerHotelID = "";
        int paramHotelID = 0;
        int paramUserId = 0;
        int hotelId = 0;
        int menuFlg = 0;
        int imediaFlg = 0;
        int paramrsvKind = 0;// 0:�S�� 1:�n�s�z�e�^�b�` 2:���u�C���\��̂�
        String errMsg = "";

        try
        {
            frmCredit = new FormOwnerBkoCreditDetail();
            frmMenu = new FormOwnerBkoMenu();

            // ����ʂ̒l���擾
            // �z�e��ID
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paraOwnerHotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            if ( request.getParameter( "rsvKind" ) != null )
            {
                paramrsvKind = Integer.parseInt( request.getParameter( "rsvKind" ).toString() );
            }

            // �l���t�H�[���ɃZ�b�g
            frmCredit.setSelHotelID( paramHotelID );
            menuFlg = MENU_FLG;

            // �����ǃt���O
            imediaFlg = OwnerRsvCommon.getImediaFlag( paraOwnerHotelID, paramUserId );

            frmCredit.setSelYearFrom( Integer.parseInt( request.getParameter( "selYearFrom" ).toString() ) );
            frmCredit.setSelMonthFrom( Integer.parseInt( request.getParameter( "selMonthFrom" ).toString() ) );
            frmCredit.setSelYearTo( Integer.parseInt( request.getParameter( "selYearTo" ).toString() ) );
            frmCredit.setSelMonthTo( Integer.parseInt( request.getParameter( "selMonthTo" ).toString() ) );
            frmCredit.setRsvKind( paramrsvKind );

            // ���̓`�F�b�N
            if ( errMsg.trim().length() != 0 )
            {
                // �G���[����
                frmCredit.setErrMsg( errMsg );
                frmCredit.setBlankCloseFlg( true );

                // ���j���[�̐ݒ�
                frmMenu.setUserId( paramUserId );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

                request.setAttribute( "FORM_Menu", frmMenu );
                request.setAttribute( "FORM_creditDetail", frmCredit );
                requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
                requestDispatcher.forward( request, response );

                return;
            }

            LogicOwnerBkoCreditDetail logic = new LogicOwnerBkoCreditDetail();

            // �N���W�b�g���׏����s
            logic.setFrm( frmCredit );
            logic.getCreditDetail();
            if ( !frmCredit.getErrMsg().trim().equals( "" ) )
            {

                // ���j���[�̐ݒ�
                frmMenu.setUserId( paramUserId );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );
                frmCredit.setBlankCloseFlg( true );

                // ��ʑJ��
                request.setAttribute( "FORM_Menu", frmMenu );
                request.setAttribute( "FORM_creditDetail", frmCredit );
                requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            // �N���W�b�g���׏o��
            logic.doPost( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoCreditPrint.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerBkoCreditPrint.execute() ] - Unable to dispatch....." + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }

}