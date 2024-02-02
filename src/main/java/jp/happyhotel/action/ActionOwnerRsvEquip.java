package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataHotelEquip;
import jp.happyhotel.owner.FormOwnerRsvEquip;
import jp.happyhotel.owner.FormOwnerRsvEquipManage;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerRsvEquip;
import jp.happyhotel.owner.LogicOwnerRsvEquipManage;
import jp.happyhotel.owner.LogicOwnerBkoMenu;

public class ActionOwnerRsvEquip extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private static final int  menuFlg           = 81;
    private static final int  menuFlg_MENU      = 8;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvEquip frmEquip;
        FormOwnerBkoMenu frmMenu;
        LogicOwnerRsvEquip logic;
        int paramHotelID = 0;
        int paramEqId = 0;
        int paramUserId = 0;
        int paramRental = 0;
        int paramInputFlag2 = 0;
        String errMsg = "";
        ArrayList<Integer> selSeqList = new ArrayList<Integer>();
        ArrayList<Integer> checkList = new ArrayList<Integer>();
        int disptype = 0;
        int imediaflag = 0;
        int adminflag = 0;
        String hotenaviId = "";
        LogicOwnerBkoMenu logicMenu = new LogicOwnerBkoMenu();

        try
        {
            // ��ʂ̒l���擾
            if ( request.getParameter( "selHotelIDValue" ) != null )
            {
                paramHotelID = Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() );
            }
            paramEqId = Integer.parseInt( request.getParameter( "equipId" ) );
            paramUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
            String seqs[] = request.getParameterValues( "selSeq" );
            if ( seqs != null )
            {
                for( int i = 0 ; i < seqs.length ; i++ )
                {
                    selSeqList.add( Integer.parseInt( seqs[i] ) );
                }
            }
            if ( request.getParameter( "selRental" ) != null )
            {
                paramRental = 1;
            }
            if ( request.getParameter( "inputFlg2" ) != null )
            {
                paramInputFlag2 = Integer.parseInt( request.getParameter( "inputFlg2" ).toString() );
            }

            hotenaviId = OwnerRsvCommon.getCookieLoginHotenavi( request.getCookies() );
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

            frmEquip = new FormOwnerRsvEquip();
            frmMenu = new FormOwnerBkoMenu();
            logic = new LogicOwnerRsvEquip();

            // �l���t�H�[���ɃZ�b�g
            frmEquip.setSelHotelID( paramHotelID );
            frmEquip.setEquipID( paramEqId );
            frmEquip.setUserID( paramUserId );
            frmEquip.setSelSeqList( selSeqList );
            frmEquip.setInputFlag2( paramInputFlag2 );
            if ( paramInputFlag2 > 0 )
            {
                frmEquip.setEquipRental( paramRental );
            }

            if ( request.getParameter( "btnBack" ) != null )
            {
                // ���߂�
                LogicOwnerRsvEquipManage logicEq = new LogicOwnerRsvEquipManage();
                FormOwnerRsvEquipManage frmEquipManage = new FormOwnerRsvEquipManage();
                frmEquipManage.setSelHotelID( paramHotelID );
                frmEquipManage.setUserId( paramUserId );

                logicEq.setFrm( frmEquipManage );
                logicEq.getEquipData();

                OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg_MENU, request.getCookies() );

                request.setAttribute( "FORM_EquipManage", logicEq.getFrm() );
                request.setAttribute( "FORM_Menu", frmMenu );
                requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // ���X�V����
            // ���̓`�F�b�N
            if ( isInputCheckMsg( frmEquip ) == true )
            {
                if ( disptype == OwnerRsvCommon.USER_AUTH_CALLCENTER && (imediaflag == 1 && adminflag == 1 && hotenaviId.equals( "happyhotel" )) == false )
                {
                    errMsg = Message.getMessage( "erro.30001", "�X�e�[�^�X���X�V���錠��" );
                    request.setAttribute( "errMsg", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }
                else
                {
                    // �f�[�^�̍X�V
                    logic.setFrm( frmEquip );
                    logic.registRoom();
                    // �z�e���@��X�V
                    execUpdateHotelEquip( frmEquip );
                }
            }

            // ���j���[�A�\����̐ݒ�
            frmMenu.setUserId( paramUserId );
            OwnerRsvCommon.setMenu( frmMenu, paramHotelID, menuFlg, request.getCookies() );

            // �ݔ����̎擾
            logic.setFrm( frmEquip );
            logic.getEquipData();

            // �`�F�b�N����Ǘ��ԍ���ݒ�
            checkList = setCheckList( frmEquip.getAllEqIdList(), selSeqList );
            frmEquip.setCheckList( checkList );

            // ��ʑJ��
            request.setAttribute( "FORM_Menu", frmMenu );
            request.setAttribute( "FORM_Equip", frmEquip );
            requestDispatcher = request.getRequestDispatcher( "owner_rsv_base.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvEquip.execute() ][hotelId = " + paramHotelID + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvEquip.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            logic = null;
        }
    }

    /**
     * �z�e���ݔ��X�V����
     * 
     * @param frm �t�H�[���f�[�^
     */
    private void execUpdateHotelEquip(FormOwnerRsvEquip frm) throws Exception
    {
        ReserveCommon rsvcomm = new ReserveCommon();
        DataHotelEquip data = new DataHotelEquip();

        // �z�e���@��ݒ�擾
        if ( data.getData( frm.getSelHotelID(), frm.getEquipID() ) == true )
        {
            // �z�e�����̋@��ݒ�X�V
            if ( rsvcomm.getRoomCnt( frm.getSelHotelID() ) == frm.getSelSeqList().size() )
            {
                // �S��
                if ( frm.getInputFlag2() == 1 )
                {
                    data.setEquipRental( frm.getEquipRental() );
                    if ( frm.getEquipRental() > 0 )
                    {
                        data.setEquipType( 3 );
                    }
                    else
                    {
                        data.setEquipType( 1 );
                    }
                }
                else
                {
                    data.setEquipType( 1 );
                }
            }
            else if ( frm.getSelSeqList().size() == 0 )
            {
                // �Ȃ�
                if ( frm.getInputFlag2() == 1 )
                {
                    data.setEquipRental( frm.getEquipRental() );
                    if ( frm.getEquipRental() > 0 )
                    {
                        data.setEquipType( 3 );
                    }
                    else
                    {
                        data.setEquipType( 9 );
                    }
                }
                else
                {
                    data.setEquipType( 9 );
                }
            }
            else
            {
                if ( frm.getInputFlag2() == 1 )
                {
                    data.setEquipRental( frm.getEquipRental() );
                    if ( frm.getEquipRental() > 0 )
                    {
                        data.setEquipType( 3 );
                    }
                    else
                    {
                        data.setEquipType( 2 );
                    }
                }
                else
                {
                    // �ꕔ
                    data.setEquipType( 2 );
                }
            }

            data.updateData( frm.getSelHotelID(), frm.getEquipID() );
        }

        return;
    }

    /**
     * ���͒l�`�F�b�N
     * 
     * @param input FormOwnerRsvEquip�I�u�W�F�N�g
     * @return true:����Afalse:�G���[����
     */
    private boolean isInputCheckMsg(FormOwnerRsvEquip frm) throws Exception
    {
        String msg;
        boolean isCheck = false;

        msg = "";

        frm.setErrMsg( msg );

        if ( msg.trim().length() == 0 )
        {
            isCheck = true;
        }
        return isCheck;
    }

    /**
     * �Ǘ��ԍ��Ƀ`�F�b�N�����鐧��̂��߂�List���쐬����B
     * 
     * @param allRoomList �Ώۃz�e���̑S�����̃��X�g
     * @param eqRoomList �o�^�ς݂̕������X�g
     * @return �`�F�b�NON�EOFF����`����Ă���ArrayList
     */
    private ArrayList<Integer> setCheckList(ArrayList<Integer> allRoomList, ArrayList<Integer> eqRoomList)
    {
        ArrayList<Integer> checkList = new ArrayList<Integer>();
        int allEqId = 0;
        int checkValue = 0;

        for( int i = 0 ; i < allRoomList.size() ; i++ )
        {
            allEqId = allRoomList.get( i );
            checkValue = 0;

            for( int j = 0 ; j < eqRoomList.size() ; j++ )
            {
                if ( allEqId == eqRoomList.get( j ) )
                {
                    checkValue = 1;
                    break;
                }
            }
            checkList.add( checkValue );
        }
        return(checkList);
    }
}
