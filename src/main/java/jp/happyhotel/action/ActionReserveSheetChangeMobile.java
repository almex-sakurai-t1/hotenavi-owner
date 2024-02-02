package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;

/**
 * 
 * �\��[��� Action
 */

public class ActionReserveSheetChangeMobile extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String carrierUrl = "";
        String errMsg = "";
        String loginMail = "";
        ArrayList<String> loginMailList = new ArrayList<String>();
        String userId = "";
        int type = 0;
        String paramUidLink = "";
        int paramHotelId = 0;
        String paramRsvNo = "";
        String url = "";
        String rsvUserId = "";
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();
        DataLoginInfo_M2 dataLoginInfo_M2;

        String checkRsvNo = "";

        try
        {
            // �L�����A�̔���
            type = UserAgent.getUserAgentType( request );
            if ( type == UserAgent.USERAGENT_AU )
            {
                carrierUrl = "../au/reserve/";
            }
            else if ( type == UserAgent.USERAGENT_VODAFONE )
            {
                carrierUrl = "../y/reserve/";
            }
            else if ( type == UserAgent.USERAGENT_DOCOMO )
            {
                carrierUrl = "../i/reserve/";
            }
            else if ( type == UserAgent.USERAGENT_SMARTPHONE )
            {
                carrierUrl = "../phone/reserve/";
            }

            // �g�уT�C�g�APC�T�C�g�̗\��[��QR�R�[�h�̂ǂ��炩��Ă΂ꂽ�����f����
            if ( request.getRequestURL().indexOf( "happyhotel.jp/reserve/" ) == -1 )
            {
                // URL�ɁAi/�Aau/�Ay/���܂܂�Ă���Ɣ��f���āAURL�̊K�w���g�їp�ɕύX
                carrierUrl = "../" + carrierUrl;
            }

            // ���O�C�����擾
            dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            if ( (dataLoginInfo_M2 != null) && (dataLoginInfo_M2.isMemberFlag() == true) )
            {
                // ���݂���
                userId = dataLoginInfo_M2.getUserId();
                // ���[���A�h���X���Z�b�g
                if ( dataLoginInfo_M2.getMailAddr().equals( "" ) == false )
                {
                    loginMail = dataLoginInfo_M2.getMailAddr();
                    loginMailList.add( dataLoginInfo_M2.getMailAddr() );
                }
                if ( dataLoginInfo_M2.getMailAddrMobile().equals( "" ) == false )
                {
                    loginMail = dataLoginInfo_M2.getMailAddrMobile();
                    loginMailList.add( dataLoginInfo_M2.getMailAddrMobile() );
                }

            }
            else
            {
                url = carrierUrl + "reserve_nomember.jsp";
                response.sendRedirect( url );
                return;
            }

            // URL�p�����[�^��s���ɕύX���ꂽ�ꍇ�́A�G���[�y�[�W�֑J�ڂ�����
            // QR�R�[�h��ǂݎ�����ꍇ�́A�z�e��ID��null�ƂȂ邽�߁A�z�e��ID�̃`�F�b�N�͏���
            checkRsvNo = request.getParameter( "reserveNo" );

            // �p�����[�^�������ꂽ�ꍇ�̃`�F�b�N
            if ( checkRsvNo == null )
            {
                url = carrierUrl + "reserve_error.jsp";
                errMsg = Message.getMessage( "erro.30009", "�p�����[�^�Ȃ�" );
                request.setAttribute( "err", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // �����̎擾
            paramRsvNo = request.getParameter( "reserveNo" );
            if ( (request.getParameter( "hotelid" ) == null) && (paramRsvNo != null) )
            {
                // �z�e��ID�擾
                paramHotelId = logic.getHotelId( paramRsvNo );
            }
            else
            {
                // ���l�̃p�����[�^�ɁA���l�ȊO���w�肳�ꂽ�ꍇ�̃`�F�b�N
                try
                {
                    // �p�����[�^�擾
                    paramHotelId = Integer.parseInt( request.getParameter( "hotelid" ) );
                }
                catch ( Exception exception )
                {
                    url = carrierUrl + "reserve_error.jsp";
                    errMsg = Message.getMessage( "erro.30009", "�p�����[�^�l�s��" );
                    request.setAttribute( "err", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                    requestDispatcher.forward( request, response );
                    return;
                }
            }

            paramUidLink = (String)request.getAttribute( "UID-LINK" );

            // �N�b�L�[�̃��[�U�[ID�Ɨ\��̍쐬�҂��������m�F
            rsvUserId = rsvCmm.getRsvUserId( paramRsvNo );

            if ( userId.compareTo( rsvUserId ) != 0 )
            {
                // �Ⴄ�ꍇ�̓G���[�y�[�W
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", Message.getMessage( "erro.30004" ) );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // �t�H�[���ɃZ�b�g
            frm.setSelHotelId( paramHotelId );
            frm.setRsvNo( paramRsvNo );

            // �\��f�[�^���o
            logic.setFrm( frm );
            logic.getData( 2 );
            frm = logic.getFrm();

            frm.setMail( loginMail );
            frm.setMode( ReserveCommon.MODE_DETL );
            frm.setErrMsg( "" );
            frm.setUserId( userId );
            frm.setUserKbn( ReserveCommon.USER_KBN_USER );
            frm.setLoginUserMail( loginMail );
            frm.setMailList( loginMailList );

            url = carrierUrl + "reserve_sheet.jsp";
            request.setAttribute( "dsp", frm );

            // �f�o�b�O�����ǂ���
            if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
            {
                url = "/_debug_" + url;
            }
            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReserveSheetChangeMobile.execute() ][hotelId = "
                    + paramHotelId + ",reserveNo = " + paramRsvNo + "] Exception", exception );
            try
            {
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "dsp", null );
                request.setAttribute( "err", Message.getMessage( "erro.30005" ) );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReserveSheetChangeMobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }
}
