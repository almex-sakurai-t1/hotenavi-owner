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
 * 有料会員入会処理クラス
 * 
 * @author N.Ide
 * @version 1.0 2009/07/**
 */

public class ActionPaymemberSecession extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    public static final int   RS_PAY_TEMPMEMBER = 1;   // 非会員が有料会員になる際にｷｬﾘｱからOKが帰って来た時のRegistStatusPayの値
    public static final int   RS_OLD_NOTMEMBER  = 8;   // 非会員が有料会員になる際のRegistStatusOldの値
    public static final int   RS_MEMBER         = 9;   // 正会員のRegistStatusの値
    public static final int   RS_PAY_NOMEMBER   = 0;   // 有料会員でないユーザのRegistStatusの値

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

        // termNoの取得
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

        // 端末番号が取得できなかったらエラーページへ飛ばす
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

        // 日時取得
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

        try
        {
            dli = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

            // auだったらアクセスチケットをチェックする
            paramAcRead = request.getParameter( "acread" );
            // carrierFlag = UserAgent.getUserAgentType( request );
            if ( (paramAcRead == null) && (carrierFlag == DataMasterUseragent.CARRIER_AU) )
            {
                try
                {
                    auCheck = new AuAuthCheck();
                    // DataLoginInfo_M2にデータを入れないために、JSP側のメソッドを呼ぶ
                    ret = auCheck.authCheckForClass( request, false );
                    // アクセスチケット確認の結果 falseだったらリダイレクト
                    if ( ret == false )
                    {
                        response.sendRedirect( auCheck.getResultData() );
                        return;
                    }
                    // アクセスチケット確認の結果 trueだったら情報を取得
                    else
                    {
                        // DataLoginInfo_M2を取得する
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

            // RegistStatusPay==1の場合も退会処理ができるようにする
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
                                paramReason = "未選択," + paramReason;
                                break;
                            case 2:
                                paramReason = "利用しなくなった," + paramReason;
                                break;
                            case 3:
                                paramReason = "使いにくいから," + paramReason;
                                break;
                            case 4:
                                paramReason = "情報が面白くない," + paramReason;
                                break;
                            case 5:
                                paramReason = "メルマガを受信したくない," + paramReason;
                                break;
                            case 6:
                                paramReason = "その他," + paramReason;
                                break;
                            default:
                                paramReason = "未選択," + paramReason;
                        }
                        dub.setDelReasonPay( paramReason );
                        dub.updateData( dli.getUserId() );
                    }
                }
                forwardUrl = "paymember_secession_carrier_check.jsp";
                // response.sendRedirect( "paymemberSecession.act?step=5" );
            }

            else if ( stepNum == 5 ) // 各ｷｬﾘｱからOKが帰ってきた場合(docomoの場合はservletに飛ぶため来ない)
            {
                if ( dli != null )
                {
                    if ( dub.getData( dli.getUserId() ) != false || dub != null )
                    {
                        // 入会途中で、元が非会員だったらデータを削除する（端末番号がユーザIDとなっているため）
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
                            // 非会員から有料会員になった場合、del_flagを1にする。
                            if ( dub.getRegistStatusOld() == 8 )
                            {
                                dub.setDelFlag( 1 );
                            }
                            // docomoﾏｲﾒﾆｭｰ登録のみのユーザが有料会員になった場合、regist_status=2とする
                            else if ( dub.getRegistStatusOld() == 1 )
                            {
                                // ユーザーIDと端末番号端末番号が同じでパスワードが入力されていなかったらそのまま
                                if ( dub.getUserId().compareTo( dub.getMobileTermNo() ) == 0 &&
                                        dub.getPasswd().compareTo( "" ) == 0 )
                                {
                                    dub.setRegistStatus( 1 );
                                }
                                // それ以外はregist_status=2に変更する
                                else
                                {
                                    dub.setRegistStatus( 2 );
                                }

                                dub.setRegistStatus( 2 );
                            }
                            // 無料会員から有料会員になった場合、有料会員になった時点のregist_statusに戻す
                            else
                            {
                                // regist_status_oldがとりえる値以外の場合は処理を行わない
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
            // forwardUrlが入っていない場合はTOPにリダイレクト
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
