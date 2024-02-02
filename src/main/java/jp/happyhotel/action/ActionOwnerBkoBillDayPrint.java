package jp.happyhotel.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerBkoCommon;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerBkoBillDay;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoBillDay;

public class ActionOwnerBkoBillDayPrint extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  MENU_FLG          = 140; // ���ʐ������׉�ʈ��

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerBkoMenu frmMenu;
        FormOwnerBkoBillDay frmBill;
        String paraOwnerHotelID = "";
        // LogicOwnerBkoBillDay logic;
        int paramHotelID = 0;
        int paramUserId = 0;
        String paramYear = "";
        String paramMonth = "";
        int paramrsvKind = 0;// 0:�S�� 1:�n�s�z�e�^�b�` 2:���u�C���\��̂�
        int hotelId = 0;
        int menuFlg = 0;
        String dateFrom = "";
        String dateTo = "";
        String simeKikan = "";
        String errMsg = "";

        try
        {
            frmBill = new FormOwnerBkoBillDay();
            frmMenu = new FormOwnerBkoMenu();

            // ����ʂ̒l���擾
            // �z�e��ID
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            paramYear = request.getParameter( "selYear" );
            paramMonth = String.format( "%02d", Integer.parseInt( request.getParameter( "selMonth" ) ) );
            if ( request.getParameter( "rsvKind" ) != null )
            {
                paramrsvKind = Integer.parseInt( request.getParameter( "rsvKind" ).toString() );
            }
            // ��ʂ̓��t������Ԃ��擾
            Calendar cal = Calendar.getInstance();
            cal.set( Integer.parseInt( paramYear ), Integer.parseInt( paramMonth ) - 1, OwnerBkoCommon.SIME_DATE );
            dateTo = new SimpleDateFormat( "yyyy�NMM��" ).format( cal.getTime() );

            // �挎�̎擾
            cal.add( Calendar.MONTH, -1 );
            dateFrom = new SimpleDateFormat( "yyyy�NMM��" ).format( cal.getTime() );
            simeKikan = dateFrom + (OwnerBkoCommon.SIME_DATE + 1) + "���`" + dateTo + OwnerBkoCommon.SIME_DATE + "��";

            // �l���t�H�[���ɃZ�b�g
            frmBill.setSelHotelID( paramHotelID );
            frmBill.setSelYear( Integer.parseInt( paramYear ) );
            frmBill.setSelMonth( Integer.parseInt( paramMonth ) );
            frmBill.setSimeKikan( simeKikan );
            frmBill.setRsvKind( paramrsvKind );
            menuFlg = MENU_FLG;

            // ���̓`�F�b�N
            if ( errMsg.trim().length() != 0 )
            {
                // �G���[����
                frmBill.setErrMsg( errMsg );
                frmBill.setBlankCloseFlg( true );

                // ���j���[�̐ݒ�
                frmMenu.setUserId( paramUserId );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

                request.setAttribute( "FORM_Menu", frmMenu );
                request.setAttribute( "FORM_creditDetail", frmBill );
                requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
                requestDispatcher.forward( request, response );

                return;
            }

            LogicOwnerBkoBillDay logic = new LogicOwnerBkoBillDay();

            // �N���W�b�g���׏����s
            logic.setFrm( frmBill );
            logic.getAccountRecv();
            // frmBill = logic.getFrm();
            if ( !frmBill.getErrMsg().trim().equals( "" ) )
            {
                // ���j���[�̐ݒ�
                frmMenu.setUserId( paramUserId );
                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );
                frmBill.setBlankCloseFlg( true );

                // ��ʑJ��
                request.setAttribute( "FORM_Menu", frmMenu );
                request.setAttribute( "FORM_billDay", frmBill );
                requestDispatcher = request.getRequestDispatcher( "owner_hotel_base.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            // �N���W�b�g���׏o��
            logic.doPost( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerBkoBillDay.execute() ][hotelId = " + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerBkoBillDay.execute() ] - Unable to dispatch....." + subException.toString() );
            }
        }
        finally
        {
            // logic = null;
        }
    }
}
