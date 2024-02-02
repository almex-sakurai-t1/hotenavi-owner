package jp.happyhotel.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.user.UserBasicInfo;

/**
 * 
 * �L�����������N���X
 * 
 * @author N.Ide
 * @version 1.0 2009/07/**
 */

public class ActionPaymemberSecession extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    public static final int   RS_PAY_TEMPMEMBER = 1;   // �������L������ɂȂ�ۂɷ�ر����OK���A���ė�������RegistStatusPay�̒l
    public static final int   RS_OLD_NOTMEMBER  = 8;   // �������L������ɂȂ�ۂ�RegistStatusOld�̒l
    public static final int   RS_MEMBER         = 9;   // �������RegistStatus�̒l
    public static final int   RS_PAY_NOMEMBER   = 0;   // �L������łȂ����[�U��RegistStatus�̒l

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String paramStep = "1";
        String paramKind = "";
        String paramRes = "";
        String paramReason = "";
        String termNo = "";
        String forwardUrl = "";
        String strError = "";
        String paramUidLink;
        String paramUn = "";
        String paramAcRead;
        boolean ret;
        int stepNum = 1;
        int kindNum = 0;
        int carrierFlag = 0;
        int nowDate;
        int nowTime;

        DataLoginInfo_M2 dli;
        DataUserBasic dub;
        UserBasicInfo ubi;
        AuAuthCheck auCheck;

        dli = new DataLoginInfo_M2();
        dub = new DataUserBasic();
        ubi = new UserBasicInfo();
        ret = false;

        paramStep = request.getParameter( "step" );
        paramUidLink = (String)request.getAttribute( "UID-LINK" );
        if ( paramStep == null || CheckString.numCheck( paramStep ) == false )
        {
            paramStep = "1";
        }
        stepNum = Integer.parseInt( paramStep );
        paramKind = request.getParameter( "kind" );
        if ( paramKind == null || CheckString.numCheck( paramKind ) == false )
        {
            paramKind = "0";
        }
        kindNum = Integer.parseInt( paramKind );

        paramUn = request.getParameter( "un" );

        // termNo�̎擾
        carrierFlag = UserAgent.getUserAgentType( request );
        if ( carrierFlag == UserAgent.USERAGENT_AU )
        {
            termNo = request.getHeader( "x-up-subno" );
        }
        else if ( carrierFlag == UserAgent.USERAGENT_VODAFONE )
        {
            termNo = request.getHeader( "x-jphone-uid" );
            if ( termNo != null )
            {
                termNo = termNo.substring( 1 );
            }
        }
        else if ( carrierFlag == UserAgent.USERAGENT_DOCOMO )
        {
            termNo = request.getParameter( "uid" );
        }

        // �[���ԍ����擾�ł��Ȃ�������G���[�y�[�W�֔�΂�
        if ( termNo == null )
        {
            try
            {
                response.sendRedirect( "paymember_uid_error.jsp?" + paramUidLink + "&kind=1" );
            }
            catch ( Exception e )
            {
                Logging.info( "[ActionPaymemberSecession.sendRedirect] Exception:" + e.toString() );
            }
        }

        // �����擾
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

        try
        {
            dli = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

            // au��������A�N�Z�X�`�P�b�g���`�F�b�N����
            paramAcRead = request.getParameter( "acread" );
            // carrierFlag = UserAgent.getUserAgentType( request );
            if ( (paramAcRead == null) && (carrierFlag == DataMasterUseragent.CARRIER_AU) )
            {
                try
                {
                    auCheck = new AuAuthCheck();
                    // DataLoginInfo_M2�Ƀf�[�^�����Ȃ����߂ɁAJSP���̃��\�b�h���Ă�
                    ret = auCheck.authCheckForClass( request, false );
                    // �A�N�Z�X�`�P�b�g�m�F�̌��� false�������烊�_�C���N�g
                    if ( ret == false )
                    {
                        response.sendRedirect( auCheck.getResultData() );
                        return;
                    }
                    // �A�N�Z�X�`�P�b�g�m�F�̌��� true������������擾
                    else
                    {
                        // DataLoginInfo_M2���擾����
                        if ( auCheck.getDataLoginInfo() != null )
                        {
                            // dli = auCheck.getDataLoginInfo();
                        }
                        Logging.info( "mobileTermNo:" + auCheck.getUbi().getUserInfo().getMobileTermNo() );
                        Logging.info( "RS_PAY:" + auCheck.getUbi().getUserInfo().getRegistStatusPay() );
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[ActionEmptySearch AuAuthCheck] Exception:" + e.toString() );
                }
            }

            // RegistStatusPay==1�̏ꍇ���މ�����ł���悤�ɂ���
            if ( dli == null )
            {
                if ( ubi.getUserBasicByTermnoNoCheck( termNo ) != false )
                {
                    dli = new DataLoginInfo_M2();
                    dli.setUserId( ubi.getUserInfo().getUserId() );
                    dli.setRegistStatusPay( ubi.getUserInfo().getRegistStatusPay() );
                    dli.setRegistStatusOld( ubi.getUserInfo().getRegistStatusOld() );
                }
            }

            if ( stepNum == 1 )
            {
                forwardUrl = "paymember_secession.jsp";
            }
            else if ( stepNum == 2 )
            {
                forwardUrl = "paymember_secession2.jsp";
            }
            else if ( stepNum == 3 )
            {
                forwardUrl = "paymember_secession_reason.jsp";
            }

            else if ( stepNum == 4 )
            {
                if ( dli != null )
                {
                    if ( dub.getData( dli.getUserId() ) != false || dub != null )
                    {
                        paramReason = request.getParameter( "reason" );
                        if ( paramReason == null )
                        {
                            paramReason = "";
                        }
                        else
                        {
                            try
                            {
                                paramReason = new String( paramReason.getBytes( "8859_1" ), "Shift_JIS" );

                            }
                            catch ( UnsupportedEncodingException e )
                            {
                                Logging.info( "[ActionPaymemberSearch] Exception:" + e.toString() );
                            }

                        }
                        paramRes = request.getParameter( "res" );
                        if ( paramRes == null || paramRes.compareTo( "" ) == 0 || CheckString.numCheck( paramRes ) == false )
                        {
                            paramRes = "1";
                        }
                        switch( Integer.parseInt( paramRes ) )
                        {
                            case 1:
                                paramReason = "���I��," + paramReason;
                                break;
                            case 2:
                                paramReason = "���p���Ȃ��Ȃ���," + paramReason;
                                break;
                            case 3:
                                paramReason = "�g���ɂ�������," + paramReason;
                                break;
                            case 4:
                                paramReason = "��񂪖ʔ����Ȃ�," + paramReason;
                                break;
                            case 5:
                                paramReason = "�����}�K����M�������Ȃ�," + paramReason;
                                break;
                            case 6:
                                paramReason = "���̑�," + paramReason;
                                break;
                            default:
                                paramReason = "���I��," + paramReason;
                        }
                        dub.setDelReasonPay( paramReason );
                        dub.updateData( dli.getUserId() );
                    }
                }
                forwardUrl = "paymember_secession_carrier_check.jsp";
                // response.sendRedirect( "paymemberSecession.act?step=5" );
            }

            else if ( stepNum == 5 ) // �e��ر����OK���A���Ă����ꍇ(docomo�̏ꍇ��servlet�ɔ�Ԃ��ߗ��Ȃ�)
            {
                if ( dli != null )
                {
                    if ( dub.getData( dli.getUserId() ) != false || dub != null )
                    {
                        // ����r���ŁA����������������f�[�^���폜����i�[���ԍ������[�UID�ƂȂ��Ă��邽�߁j
                        if ( (dub.getRegistStatusPay() == 1) && (dub.getRegistStatusOld() == 8) )
                        {
                            dub.deleteData( dli.getUserId() );
                        }
                        else
                        {
                            dub.setDelDatePay( nowDate );
                            dub.setDelTimePay( nowTime );
                            // dub.setPointPay( 0 );
                            // dub.setPointPayUpdate( nowDate );
                            // ��������L������ɂȂ����ꍇ�Adel_flag��1�ɂ���B
                            if ( dub.getRegistStatusOld() == 8 )
                            {
                                dub.setDelFlag( 1 );
                            }
                            // docomoϲ�ƭ��o�^�݂̂̃��[�U���L������ɂȂ����ꍇ�Aregist_status=2�Ƃ���
                            else if ( dub.getRegistStatusOld() == 1 )
                            {
                                // ���[�U�[ID�ƒ[���ԍ��[���ԍ��������Ńp�X���[�h�����͂���Ă��Ȃ������炻�̂܂�
                                if ( dub.getUserId().compareTo( dub.getMobileTermNo() ) == 0 &&
                                        dub.getPasswd().compareTo( "" ) == 0 )
                                {
                                    dub.setRegistStatus( 1 );
                                }
                                // ����ȊO��regist_status=2�ɕύX����
                                else
                                {
                                    dub.setRegistStatus( 2 );
                                }

                                dub.setRegistStatus( 2 );
                            }
                            // �����������L������ɂȂ����ꍇ�A�L������ɂȂ������_��regist_status�ɖ߂�
                            else
                            {
                                // regist_status_old���Ƃ肦��l�ȊO�̏ꍇ�͏������s��Ȃ�
                                if ( dub.getRegistStatusOld() == 2 || dub.getRegistStatusOld() == 3 || dub.getRegistStatusOld() == 9 )
                                {
                                    dub.setRegistStatus( dub.getRegistStatusOld() );
                                }
                            }
                            dub.setRegistStatusPay( 0 );
                            dub.updateData( dli.getUserId() );
                        }
                    }
                }
                forwardUrl = "paymember_secession_complete.jsp";
            }
            // forwardUrl�������Ă��Ȃ��ꍇ��TOP�Ƀ��_�C���N�g
            if ( forwardUrl.compareTo( "" ) == 0 )
            {
                forwardUrl = "../../index.jsp?" + paramUidLink;
                response.sendRedirect( forwardUrl );
                return;
            }
            request.setAttribute( "LOGIN_INFO", dli );
            requestDispatcher = request.getRequestDispatcher( forwardUrl );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception e )
        {
            Logging.error( "ActionPaymemberSecsssion.execute() Exception=" + e.toString() );
        }
        finally
        {
            strError = "";
            forwardUrl = "";
            dli = null;
            dub = null;
            ubi = null;
        }
    }
}
