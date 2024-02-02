package jp.happyhotel.action;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.FormOwnerRsvPlanImage;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvPlanImage;

public class ActionOwnerRsvPlanImage extends BaseAction
{
    private RequestDispatcher   requestDispatcher = null;
    private static final String PLAN_IMAGE_CONF   = "/etc/happyhotel/planimage.conf";
    private static final String IMAGE_KEY         = "release.image.url";

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int selHotelId = 0;
        String imagePath = "";
        FormOwnerRsvPlanImage frm;
        LogicOwnerRsvPlanImage logic;
        Properties config = new Properties();
        FileInputStream propfile = null;
        String errMsg = "";
        int disptype = 0;
        int paramUserId = 0;
        int imediaflag = 0;
        int adminflag = 0;
        String hotenaviId = "";
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {
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

            frm = new FormOwnerRsvPlanImage();
            logic = new LogicOwnerRsvPlanImage();

            selHotelId = Integer.parseInt( request.getParameter( "selHotelIDValue" ) );

            // �t�@�C���i�[��擾
            propfile = new FileInputStream( PLAN_IMAGE_CONF );
            config = new Properties();
            config.load( propfile );

            imagePath = config.getProperty( IMAGE_KEY );
            propfile.close();
            frm.setFilePath( imagePath );

            // �t�@�C�����擾
            frm.setSelHotelID( selHotelId );
            logic.setFrm( frm );
            logic.getFile();

            // ��ʑJ��
            request.setAttribute( "FORM_planImage", frm );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_planImage.jsp" );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvPlanImage.execute() ]Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvChargeMode.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            logic = null;
        }
    }
}