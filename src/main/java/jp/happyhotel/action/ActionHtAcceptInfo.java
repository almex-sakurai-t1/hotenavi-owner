package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApContentsConfig;
import jp.happyhotel.data.DataApHotelCustom;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.dto.DtoApCommon;
import jp.happyhotel.dto.DtoApHotelCustomerData;
import jp.happyhotel.dto.DtoApMemberCardReg;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.touch.MemberAcceptInfo;
import jp.happyhotel.touch.MemberRegistExInfo;

/**
 * �n�s�z�e�A�v���`�F�b�N�C���N���X
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtAcceptInfo extends BaseAction
{
    final int                    TIMEOUT             = 5000;
    final int                    HOTENAVI_PORT_NO    = 7023;
    final int                    HAPIHOTE_PORT_NO    = 7046;
    final String                 HTTP                = "http://";
    final String                 CLASS_NAME          = "hapiTouch.act?method=";
    final int                    RESULT_OK           = 1;
    final int                    RESULT_NG           = 2;
    private RequestDispatcher    requestDispatcher;
    private DtoApCommon          apCommon;
    DtoApHotelCustomerData       apHotelCustomerData = null;
    private DtoApMemberCardReg   apMemberCardReg;
    private DataApContentsConfig config              = new DataApContentsConfig();

    /**
     * �n�s�z�e�^�b�`
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret = false;
        boolean boolHotelCustom = false;// �ڋq�Ή���
        boolean boolMemberAccept = false;// �����o�[����t����

        String userId = "";// ���[�UID
        String hotenaviIp = "";
        String customId = "";
        String contents = "";

        DataHotelBasic dhb = new DataHotelBasic();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();
        DataApHotelSetting dahs = new DataApHotelSetting();
        DataApHotelCustom dahc = new DataApHotelCustom();
        HotelCi hc = new HotelCi();

        MemberAcceptInfo memberAcceptInfo = new MemberAcceptInfo();// �����o�[��t���N���X

        String paramSeq;
        String paramId;
        String forwardUrl = "";
        try
        {
            // ID��Seq����^�b�`�f�[�^���擾
            paramSeq = request.getParameter( "seq" );
            paramId = request.getParameter( "id" );
            if ( paramSeq == null || paramSeq.equals( "" ) != false || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( Integer.parseInt( paramSeq ) > 0 && Integer.parseInt( paramId ) > 0 )
            {
                // �ڋq�����o�[���������`�F�b�N
                boolHotelCustom = dahs.getData( Integer.parseInt( paramId ) );

                ret = hc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                // �t�F���J�f�[�^����擾
                if ( hc.getHotelCi().getUserId().equals( "" ) == false )
                {
                    userId = hc.getHotelCi().getUserId();
                    // ���Ƀ����o�[�o�^���݂��ǂ������`�F�b�N
                    ret = dahc.getData( Integer.parseInt( paramId ), userId );
                }

                // �z�e���̃t�����gIP���擾
                hotenaviIp = HotelIp.getHotenaviIp( Integer.parseInt( paramId ) );

                dhrm.getData( Integer.parseInt( paramId ), hc.getHotelCi().getRoomNo() );

                // �ڋq��������
                if ( boolHotelCustom != false )
                {
                    // �����o�[�Ȃ��������ɑ΂��Ď��C�J�[�h�}���`�F�b�N
                    /** �i1042�j�z�e�i�r_�����o�[��t���擾�d�� **/
                    memberAcceptInfo.setRoomName( dhrm.getRoomNameHost() );
                    memberAcceptInfo.sendToHost( hotenaviIp, TIMEOUT, HOTENAVI_PORT_NO, paramId );

                    // MemberInfo memberInfo = new MemberInfo();
                    MemberRegistExInfo memberRegistExInfo = new MemberRegistExInfo();

                    // �J�[�h��t�ς�
                    if ( memberAcceptInfo.getResult() == RESULT_OK )
                    {
                        boolMemberAccept = true;
                        // �����o�[ID���擾
                        // �J�[�h��t�ς݁��u�V�K����o�^��t�v�ƁA�u���̃J�[�h���g�p����v
                        customId = memberAcceptInfo.getMemberId();

                        // �ȑO�g�p���Ă��������o�[���擾�d���i1002�j
                        // customId = memberAcceptInfo.getMemberId();
                        // memberInfo.setMemberId( customId );
                        // memberInfo.setBirthMonth( Integer.parseInt( memberAcceptInfo.getBirthMonth() ) );
                        // memberInfo.setBirthDay( Integer.parseInt( memberAcceptInfo.getBirthDay() ) );

                        // �z�e�i�r�d���@�����o�[�o�^�O���擾�d���i1050�j
                        memberRegistExInfo.setMemberId( customId );
                        memberRegistExInfo.setBirthMonth1( Integer.parseInt( memberAcceptInfo.getBirthMonth() ) );
                        memberRegistExInfo.setBirthDate1( Integer.parseInt( memberAcceptInfo.getBirthDay() ) );

                        // memberInfo.sendToHost( hotenaviIp, TIMEOUT, HOTENAVI_PORT_NO, paramId );
                        // if ( memberInfo.getResult() == RESULT_OK )
                        // {
                        // apHotelCustomerData = memberInfo.getMemberInfo();
                        // }
                        memberRegistExInfo.sendToHost( hotenaviIp, TIMEOUT, HOTENAVI_PORT_NO, paramId );
                        if ( memberRegistExInfo.getResult() == RESULT_OK )
                        {
                            apHotelCustomerData = memberRegistExInfo.getMemberInfo();
                        }

                    }
                }
            }

            if ( customId.equals( "" ) == false )
            {
                // forwardUrl = "MemberCardRegForm.jsp";
                forwardUrl = "MemberCard.jsp";
            }
            else
            {
                forwardUrl = "MemberCardRegIntoApplication.jsp";
            }

            if ( config.getDataCommon( "MemberCard.jsp", 0, Integer.parseInt( paramId ), 0 ) )
            {
                contents = config.getContents();
            }

            apCommon = new DtoApCommon();
            apMemberCardReg = new DtoApMemberCardReg();
            // �����o�[���擾�Ɏ��s���Ă����珉���������s��
            if ( apHotelCustomerData == null )
            {
                apHotelCustomerData = new DtoApHotelCustomerData();
            }

            // ���ʐݒ�
            apCommon.setHtCheckIn( ret );
            apCommon.setId( Integer.parseInt( paramId ) );
            apCommon.setHotelName( dhb.getName() );
            apCommon.setRoomNo( dhrm.getRoomNameHost() );
            apCommon.setSeq( hc.getHotelCi().getSeq() );

            // �z�e���ڋq
            apMemberCardReg.setApCommon( apCommon );
            apMemberCardReg.setCustomId( customId );
            apMemberCardReg.setResult( boolMemberAccept );

            apHotelCustomerData.setApCommon( apCommon );
            apHotelCustomerData.setCustomId( customId );
            apHotelCustomerData.setContents( contents );

            request.setAttribute( "DtoApCommon", apCommon );
            request.setAttribute( "DtoApMemberCardReg", apMemberCardReg );
            request.setAttribute( "DtoApHotelCustomerData", apHotelCustomerData );
            request.setAttribute( "id", paramId );
            request.setAttribute( "seq", paramSeq );

            requestDispatcher = request.getRequestDispatcher( forwardUrl );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "HtAcceptInfo:" + exception );
        }
        finally
        {
        }
    }
}
