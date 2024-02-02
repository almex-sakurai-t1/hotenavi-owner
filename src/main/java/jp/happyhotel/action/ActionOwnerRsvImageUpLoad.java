package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.owner.FormOwnerRsvImageUpLoad;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvImageUpLoad;
import jp.happyhotel.owner.LogicOwnerBkoMenu;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 
 * �摜�A�b�v���[�h Action Class
 */

public class ActionOwnerRsvImageUpLoad extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {

        int userId = 0;
        String strCarrierUrl = "";
        int selHotelId = 0; //
        String loginHotelId = "";
        String ownerHotelId = "";
        String errMsg = "";
        String hotenaviId = "";

        LogicOwnerRsvImageUpLoad logic = new LogicOwnerRsvImageUpLoad();
        FormOwnerRsvImageUpLoad form = new FormOwnerRsvImageUpLoad();
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {
            loginHotelId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            userId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            ownerHotelId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );

            // ���O�C�����[�U�ƒS���z�e���̃`�F�b�N
            if ( (request.getParameter( "selHotelIDValue" ) != null) && (request.getParameter( "selHotelIDValue" ).trim().length() != 0) )
            {
                selHotelId = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
            if ( (selHotelId != 0) && (OwnerRsvCommon.checkHotelID( hotenaviId, userId, selHotelId ) == false) )
            {
                // �Ǘ��O�̃z�e���̓��O�C����ʂ֑J��
                response.sendRedirect( "../../owner/index.jsp" );
                return;
            }

            logicMenu.setFrm( new FormOwnerBkoMenu() );
            disptype = logicMenu.getUserAuth( hotenaviId, userId );
            adminflag = logicMenu.getAdminFlg( hotenaviId, userId );
            imediaflag = OwnerRsvCommon.getImediaFlag( hotenaviId, userId );

            if ( (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false && disptype != OwnerRsvCommon.USER_AUTH_OWNER && disptype != OwnerRsvCommon.USER_AUTH_DEMO && disptype != OwnerRsvCommon.USER_AUTH_CALLCENTER )
            {
                // �����̂Ȃ����[�U�̓G���[�y�[�W��\������
                errMsg = Message.getMessage( "erro.30001", "�y�[�W���{�����錠��" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( ServletFileUpload.isMultipartContent( request ) == false )
            {
                // �摜�A�b�v���[�h�ł͂Ȃ�
                // �z�e��ID�擾
                form.setSelHotelId( Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() ) );
                selHotelId = form.getSelHotelId();

            }
            else
            // �摜�A�b�v���[�h
            {
                if ( logic.init( request ) )
                {
                    String strId = logic.getParam( "selHotelIDValue" );
                    if ( strId != "" )
                    {
                        form.setSelHotelId( Integer.parseInt( strId ) );
                        selHotelId = form.getSelHotelId();
                    }

                    // �摜�A�b�v���[�h�`�F�b�N
                    if ( logic.check() )
                    {
                        // �摜�t�@�C����ۑ�
                        if ( disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
                        {
                            // �����̂Ȃ����[�U�̓G���[�y�[�W��\������
                            errMsg = Message.getMessage( "erro.30001", "�X�e�[�^�X���X�V���錠��" );
                            request.setAttribute( "errMsg", errMsg );
                            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                            requestDispatcher.forward( request, response );
                            return;
                        }
                        else
                        {
                            logic.upload( ownerHotelId, loginHotelId, selHotelId, userId );
                        }
                    }
                    // �\�����b�Z�[�W��ݒ�
                    form.setMessage( logic.getErrMsg() );
                }

            }

            request.setAttribute( "Form_OwnerRsvImageUpLoad", form );

            strCarrierUrl = "owner_rsv_upload.jsp";
            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + strCarrierUrl );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvImageUpLoad.execute() ][hotelId = "
                    + loginHotelId + "] Exception: ", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvImageUpLoad.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

}
