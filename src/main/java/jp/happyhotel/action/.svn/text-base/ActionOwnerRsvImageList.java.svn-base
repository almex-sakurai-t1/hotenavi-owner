package jp.happyhotel.action;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.FileUploader;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerRsvImageList;
import jp.happyhotel.owner.LogicOwnerRsvImageList;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 
 * 画像一覧 Action Class
 */

public class ActionOwnerRsvImageList extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    private static final int  STATUS_TMP        = 1;
    private static final int  STATUS_REQ        = 2;
    // private static final int STATUS_OK = 3;
    private static final int  STATUS_DEL        = 9;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {

        String hotelID = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() ); // ホテルID
        int userId = 0;
        int selHotelId = 0; //
        String loginHotelId = "";
        String strCarrierUrl = "";
        int start = 1;
        Properties config = new Properties();
        FileInputStream propfile = null;
        String ownerHotelId = "";
        String errMsg = "";
        String hotenaviId = "";
        int statusKind = 0;
        boolean multicontentFlag = false;

        LogicOwnerRsvImageList logic = new LogicOwnerRsvImageList();

        FormOwnerRsvImageList form = new FormOwnerRsvImageList();

        FileUploader fupl = new FileUploader();

        try
        {
            if ( ServletFileUpload.isMultipartContent( request ) == true )
            {
                multicontentFlag = true;
                fupl.setData( request );
            }

            loginHotelId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            // ユーザID取得
            userId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            ownerHotelId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );

            form.setHotelID( hotelID );

            if ( multicontentFlag )
            {
                // ログインユーザと担当ホテルのチェック
                if ( (fupl.getParameter( "selHotelIDValue" ) != null) && (fupl.getParameter( "selHotelIDValue" ).trim().length() != 0) )
                {
                    selHotelId = Integer.parseInt( fupl.getParameter( "selHotelIDValue" ).toString() );
                }
            }
            else
            {
                // ログインユーザと担当ホテルのチェック
                if ( (request.getParameter( "selHotelIDValue" ) != null) && (request.getParameter( "selHotelIDValue" ).trim().length() != 0) )
                {
                    selHotelId = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
                }
            }

            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            if ( (selHotelId != 0) && (OwnerRsvCommon.checkHotelID( hotenaviId, userId, selHotelId ) == false) )
            {
                // 管理外のホテルはログイン画面へ遷移
                response.sendRedirect( "../../owner/index.jsp" );
                return;
            }

            // 画像の格納ディレクトリをセット
            propfile = new FileInputStream( "/etc/happyhotel/planimage.conf" );
            config = new Properties();
            config.load( propfile );

            String imagePath = config.getProperty( "inwork.image.url" );
            String releasePath = config.getProperty( "release.image.url" );
            propfile.close();

            form.setBaseDir( imagePath );
            form.setReleaseDir( releasePath );

            // ホテルID取得
            // form.setSelHotelID( Integer.parseInt( fupl.getParameter( "selHotelIDValue" ).toString() ) );
            // selHotelId = form.getSelHotelID();

            if ( multicontentFlag )
            {
                if ( fupl.getParameter( "statusKind" ) != null )
                {
                    statusKind = Integer.parseInt( fupl.getParameter( "statusKind" ) );
                }
                else
                {
                    statusKind = STATUS_TMP; // 仕掛（未承認）
                }
                if ( fupl.getParameter( "page" ) != null )
                {
                    start = Integer.parseInt( fupl.getParameter( "page" ) );
                    start = start * FormOwnerRsvImageList.PAGE_MAX + 1;
                }
                if ( fupl.getParameter( "search" ) != null ) // 抽出ボタン
                {
                    start = 1; // 抽出ボタンは、最初から表示
                }
            }
            else
            {
                if ( request.getParameter( "statusKind" ) != null )
                {
                    statusKind = Integer.parseInt( request.getParameter( "statusKind" ) );
                }
                else
                {
                    statusKind = STATUS_TMP; // 仕掛（未承認）
                }
                if ( request.getParameter( "page" ) != null )
                {
                    start = Integer.parseInt( request.getParameter( "page" ) );
                    start = start * FormOwnerRsvImageList.PAGE_MAX + 1;
                }
                if ( request.getParameter( "search" ) != null ) // 抽出ボタン
                {
                    start = 1; // 抽出ボタンは、最初から表示
                }
            }

            form.setStatusKind( statusKind );
            form.setStart( start );

            if ( multicontentFlag )
            {
                if ( fupl.getParameter( "Exec" ) != null )
                {
                    if ( fupl.getParameter( "status" ) == null )
                    {
                        form.setErrMsg( Message.getMessage( "warn.00002", "更新対象" ) );
                    }
                    else
                    {
                        // 承認処理
                        if ( logic.setStatus( STATUS_REQ, request, fupl, userId, ownerHotelId, multicontentFlag ) )
                        {
                            form.setErrMsg( Message.getMessage( "info.00004", "承認処理" ) );
                        }
                        else
                        {
                            form.setErrMsg( Message.getMessage( "erro.30002", "承認処理" ) );
                        }
                        // 最初から表示
                        start = 1;
                        form.setStart( start );
                    }
                }
                else if ( fupl.getParameter( "Delete" ) != null )
                {
                    if ( fupl.getParameter( "status" ) == null )
                    {
                        form.setErrMsg( Message.getMessage( "warn.00002", "更新対象" ) );
                    }
                    else
                    {
                        // 削除処理
                        if ( logic.setStatus( STATUS_DEL, request, fupl, userId, ownerHotelId, multicontentFlag ) )
                        {
                            form.setErrMsg( Message.getMessage( "info.00004", "削除処理" ) );
                        }
                        else
                        {
                            form.setErrMsg( Message.getMessage( "erro.30002", "削除処理" ) );
                        }
                        // 最初から表示
                        start = 1;
                        form.setStart( start );
                    }
                }
            }
            else
            {
                if ( request.getParameter( "Exec" ) != null )
                {
                    if ( request.getParameter( "status" ) == null )
                    {
                        form.setErrMsg( Message.getMessage( "warn.00002", "更新対象" ) );
                    }
                    else
                    {
                        // 承認処理
                        if ( logic.setStatus( STATUS_REQ, request, fupl, userId, ownerHotelId, multicontentFlag ) )
                        {
                            form.setErrMsg( Message.getMessage( "info.00004", "承認処理" ) );
                        }
                        else
                        {
                            form.setErrMsg( Message.getMessage( "erro.30002", "承認処理" ) );
                        }
                        // 最初から表示
                        start = 1;
                        form.setStart( start );
                    }
                }
                else if ( request.getParameter( "Delete" ) != null )
                {
                    if ( request.getParameter( "status" ) == null )
                    {
                        form.setErrMsg( Message.getMessage( "warn.00002", "更新対象" ) );
                    }
                    else
                    {
                        // 削除処理
                        if ( logic.setStatus( STATUS_DEL, request, fupl, userId, ownerHotelId, multicontentFlag ) )
                        {
                            form.setErrMsg( Message.getMessage( "info.00004", "削除処理" ) );
                        }
                        else
                        {
                            form.setErrMsg( Message.getMessage( "erro.30002", "削除処理" ) );
                        }
                        // 最初から表示
                        start = 1;
                        form.setStart( start );
                    }
                }
            }
            // 検索
            logic.getData( loginHotelId, userId, selHotelId, statusKind, form, start, FormOwnerRsvImageList.PAGE_MAX );

            request.setAttribute( "FORM_OwnerRsvImageList", form );
            strCarrierUrl = "owner_rsv_image_list.jsp";

            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + strCarrierUrl );
            requestDispatcher.forward( request, response );

        }

        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvImageList() ][hotelId = "
                    + hotelID + "] Exception: ", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvImageList.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

}
