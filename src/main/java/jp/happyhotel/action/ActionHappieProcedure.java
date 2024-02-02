package jp.happyhotel.action;

import java.net.URLEncoder;
import java.text.NumberFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.SendMail;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelHappieProcedure;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.user.UserDataIndex;
import jp.happyhotel.user.UserPointPay;

/**
 * 
 * �n�s�[�葱���ԍ�����N���X
 * 
 * @author S.Tashiro
 * @version 1.0 2011/05/27
 */

public class ActionHappieProcedure extends BaseAction
{
    //
    boolean                   memberFlag;
    boolean                   paymemberFlag;
    boolean                   paymemberTempFlag;
    boolean                   ret;

    private RequestDispatcher requestDispatcher = null;
    private DataLoginInfo_M2  dataLoginInfo_M2  = null;

    /**
     * �C�ӂ̏ꏊ�t�߂Ńz�e��������
     * 
     * @param request �N���C�A���g����T�[�o�ւ̃��N�G�X�g
     * @param response �T�[�o����N���C�A���g�ւ̃��X�|���X
     * 
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String paramAcRead;
        boolean memberFlag;
        boolean paymemberFlag;
        boolean paymemberTempFlag;
        boolean ret;
        int i;
        int seq;
        int registStatus;
        int delFlag;
        int carrierFlag;
        String paramUidLink = null;
        String paramId;
        UserDataIndex udi;
        DataHotelBasic dhb;
        DataHotelHappieProcedure dhhp;
        udi = new UserDataIndex();
        dhb = new DataHotelBasic();
        dhhp = new DataHotelHappieProcedure();

        carrierFlag = UserAgent.getUserAgentType( request );
        dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
        paramUidLink = (String)request.getAttribute( "UID-LINK" );
        paramAcRead = request.getParameter( "acread" );
        paramId = request.getParameter( "hotel_id" );

        try
        {
            if ( (paramId == null) || (paramId.equals( "" ) != false) || (CheckString.numCheck( paramId ) == false) )
            {
                paramId = "0";
            }

            // ���[�U�[���̎擾
            if ( dataLoginInfo_M2 != null )
            {
                memberFlag = dataLoginInfo_M2.isMemberFlag();
                paymemberFlag = dataLoginInfo_M2.isPaymemberFlag();
                paymemberTempFlag = dataLoginInfo_M2.isPaymemberTempFlag();
                registStatus = dataLoginInfo_M2.getRegistStatus();
                delFlag = dataLoginInfo_M2.getDelFlag();
                carrierFlag = dataLoginInfo_M2.getCarrierFlag();
            }
            else
            {
                memberFlag = false;
                paymemberFlag = false;
                paymemberTempFlag = false;
                registStatus = 0;
                delFlag = 1;
            }

            if ( memberFlag != false && Integer.parseInt( paramId ) > 0 )
            {
                // �g�����U�N�V�����X�V�̂��߁A10��قǃ��g���C
                for( i = 0 ; i < 10 ; i++ )
                {
                    // ���[�U�̊Ǘ��ԍ����擾�i�z�e���ʁj
                    ret = udi.getDataUserIndex( dataLoginInfo_M2.getUserId(), Integer.parseInt( paramId ) );
                    if ( ret != false )
                    {
                        // �z�e���̏����擾
                        dhb.getData( Integer.parseInt( paramId ) );

                        // �z�e���̍̔ԃf�[�^���擾
                        dhhp.setId( Integer.parseInt( paramId ) );
                        dhhp.setRegistStatus( 0 );
                        dhhp.setUserId( dataLoginInfo_M2.getUserId() );
                        dhhp.setUserSeq( udi.getUserDataIndexInfo().getUserSeq() );
                        dhhp.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dhhp.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        ret = dhhp.insertData();
                        if ( ret != false )
                        {
                            seq = dhhp.getSeq();
                            if ( seq > 0 )
                            {
                                this.sendMail( dataLoginInfo_M2, Integer.parseInt( paramId ), seq );
                                break;
                            }
                        }
                    }
                }

                if ( dhhp.getId() > 0 )
                {
                    request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                    request.setAttribute( "HAPPIE_PROCEDURE", dhhp );
                    request.setAttribute( "DHB", dhb );
                }
                else
                {
                    if ( dataLoginInfo_M2 != null )
                    {
                        request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                    }
                }

            }
            requestDispatcher = request.getRequestDispatcher( "happie_procedure_complete.jsp" );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHappieProcedure ] Exception:" + e.toString() );

        }
    }

    /***
     * 
     * @param dataLoginInfo
     * @param id
     * @param procedureNo
     */
    public void sendMail(DataLoginInfo_M2 dataLoginInfo, int id, int procedureNo)
    {
        final String MAIL_ADDR_ADMIN = "premium_info@happyhotel.jp";
        // ���[���̑��M
        String title = "";
        String encdata = "";
        String text = "";
        String mailAddr = "";
        String userKind = "";
        DataHotelBasic dhb;
        UserPointPay upp;
        NumberFormat nfComma;
        dhb = new DataHotelBasic();
        upp = new UserPointPay();
        nfComma = NumberFormat.getInstance();

        if ( dataLoginInfo.getMailAddrMobile().equals( "" ) == false )
        {
            mailAddr = dataLoginInfo.getMailAddrMobile();
        }
        else if ( dataLoginInfo.getMailAddr().equals( "" ) != false )
        {
            mailAddr = dataLoginInfo.getMailAddr();
        }
        if ( dataLoginInfo.isPaymemberFlag() != false )
        {
            userKind = "�v���~�A��";
        }
        else
        {
            userKind = "����";
        }
        ret = dhb.getData( id );
        if ( ret != false )
        {
            try
            {
                encdata = URLEncoder.encode( "�葱��No." + procedureNo, "Shift_JIS" );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionHappieProcedure sendMail] Exception:" + e.toString() );
            }

            // title = "[�n�s�z�e]�n�s�z�e�}�C���\�����葱����t����";
            // text = "�n�s�z�e�}�C���\���t�H�[��\r\n";
            // text += "�n�s�z�e�}�C���\���̂��葱�����󂯕t���܂����B\r\n";
            // text += "�y�n�s�z�e�}�C���\�����葱��No." + procedureNo + "�z\r\n";
            // text += "�z�e����:" + dhb.getName() + "\r\n";
            // text += "�z�e��ID:" + dhb.getId() + "\r\n\r\n";
            // text += "���葱���̉񓚂̓��[������у}�C�y�[�W���ɂĉ񓚂����Ă��������܂��B\r\n\r\n";
            // text += "------------------------\r\n";
            // text += "�n�b�s�[�z�e��������\r\n";
            // text += MAIL_ADDR_ADMIN + "\r\n";
            // text += "[���葱��No." + procedureNo + "]\r\n";
            // text += "���₢���킹�̍ۂ̓��[���ɂ��葱��No���L�����Ă��������B\r\n";
            //
            // // ���[�����M���s��
            // SendMail.send( MAIL_ADDR_ADMIN, mailAddr, title, text );

            title = "�y�n�s�z�e�}�C���\�����葱��No." + procedureNo + "(" + dhb.getName() + ")�z";
            text = "������:" + userKind + "\r\n";
            text += "�����O:" + dataLoginInfo.getUserName() + "�l\r\n";
            text += "�n�s�z�eID:" + dataLoginInfo.getUserId() + "\r\n";
            text += "�z�e����:" + dhb.getName() + "\r\n";
            text += "�z�e��ID:" + dhb.getId() + "\r\n";
            text += "�n�s�z�e�}�C���c��:" + nfComma.format( upp.getNowPoint( dataLoginInfo.getUserId(), false ) ) + "�}�C��\r\n";
            text += "[���葱��No." + procedureNo + "]\r\n";
            // �Г��Ńe�X�g����ꍇ�̓R�����g�A�E�g����ipremium_info@happyhotel.jp���ĂɃ��[�����M�j
            SendMail.send( MAIL_ADDR_ADMIN, MAIL_ADDR_ADMIN, title, text );

        }
    }
}
