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
import jp.happyhotel.owner.FormOwnerRsvPlanImageDelete;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvPlanImageDelete;

public class ActionOwnerRsvPlanImageDelete extends BaseAction
{
    private RequestDispatcher   requestDispatcher = null;
    private static final String PLAN_IMAGE_CONF   = "/etc/happyhotel/planimage.conf";
    private static final String IMAGE_KEY         = "release.image.url";
    private static final String MODETYPE_DELETE   = "1";

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int selHotelId = 0;
        String imagePath = "";
        String deleteImagePath = "";
        String mode = "";
        String deleteImageId = "";
        FormOwnerRsvPlanImageDelete frm;
        LogicOwnerRsvPlanImageDelete logic;
        int userId = 0;
        String ownerHotelId = "";
        Properties config = new Properties();
        FileInputStream propfile = null;
        String errMsg = "";
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {
            frm = new FormOwnerRsvPlanImageDelete();
            logic = new LogicOwnerRsvPlanImageDelete();

            selHotelId = Integer.parseInt( request.getParameter( "selHotelIDValue" ) );
            mode = request.getParameter( "mode" );
            if ( mode == null )
            {
                mode = "";
            }
            // ユーザID取得
            userId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            ownerHotelId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );

            logicMenu.setFrm( new FormOwnerBkoMenu() );
            disptype = logicMenu.getUserAuth( ownerHotelId, userId );
            adminflag = logicMenu.getAdminFlg( ownerHotelId, userId );
            imediaflag = OwnerRsvCommon.getImediaFlag( ownerHotelId, userId );
            if ( (imediaflag == 1 && adminflag == 1 && ownerHotelId.equals( "happyhotel" )) == false && disptype != OwnerRsvCommon.USER_AUTH_OWNER && disptype != OwnerRsvCommon.USER_AUTH_DEMO && disptype != OwnerRsvCommon.USER_AUTH_CALLCENTER )
            {
                // 権限のないユーザはエラーページを表示する
                errMsg = Message.getMessage( "erro.30001", "ページを閲覧する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && ownerHotelId.equals( "happyhotel" )) == false && mode.equals( MODETYPE_DELETE ) )
            {
                // 権限のないユーザはエラーページを表示する
                errMsg = Message.getMessage( "erro.30001", "ステータスを更新する権限" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // ファイル格納先取得
            propfile = new FileInputStream( PLAN_IMAGE_CONF );
            config = new Properties();
            config.load( propfile );

            imagePath = config.getProperty( IMAGE_KEY );
            String inworkPath = config.getProperty( "inwork.image.path" );
            deleteImagePath = config.getProperty( "release.image.path" );
            propfile.close();
            frm.setFilePath( imagePath );

            // ファイル名取得
            frm.setSelHotelID( selHotelId );
            logic.setFrm( frm );

            if ( mode.equals( MODETYPE_DELETE ) )
            {
                // 指定画像削除処理
                deleteImageId = request.getParameter( "selectDelImg" );

                // 指定画像を削除後、画像一覧取得
                if ( logic.deletePlanImage( inworkPath, deleteImagePath, selHotelId, deleteImageId, userId, ownerHotelId ) )
                {
                    logic.getFile();
                }
            }
            else
            {
                // 画像一覧取得
                logic.getFile();
            }

            // 画面遷移
            request.setAttribute( "FORM_planImageDelete", frm );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_planImageDelete.jsp" );
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
