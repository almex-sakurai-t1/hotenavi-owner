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
 * ハピー手続き番号制御クラス
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
     * 任意の場所付近でホテルを検索
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param response サーバからクライアントへのレスポンス
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

            // ユーザー情報の取得
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
                // トランザクション更新のため、10回ほどリトライ
                for( i = 0 ; i < 10 ; i++ )
                {
                    // ユーザの管理番号を取得（ホテル別）
                    ret = udi.getDataUserIndex( dataLoginInfo_M2.getUserId(), Integer.parseInt( paramId ) );
                    if ( ret != false )
                    {
                        // ホテルの情報を取得
                        dhb.getData( Integer.parseInt( paramId ) );

                        // ホテルの採番データを取得
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
        // メールの送信
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
            userKind = "プレミアム";
        }
        else
        {
            userKind = "無料";
        }
        ret = dhb.getData( id );
        if ( ret != false )
        {
            try
            {
                encdata = URLEncoder.encode( "手続きNo." + procedureNo, "Shift_JIS" );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionHappieProcedure sendMail] Exception:" + e.toString() );
            }

            // title = "[ハピホテ]ハピホテマイル申請お手続き受付完了";
            // text = "ハピホテマイル申請フォーム\r\n";
            // text += "ハピホテマイル申請のお手続きを受け付けました。\r\n";
            // text += "【ハピホテマイル申請お手続きNo." + procedureNo + "】\r\n";
            // text += "ホテル名:" + dhb.getName() + "\r\n";
            // text += "ホテルID:" + dhb.getId() + "\r\n\r\n";
            // text += "お手続きの回答はメールおよびマイページ内にて回答させていただきます。\r\n\r\n";
            // text += "------------------------\r\n";
            // text += "ハッピーホテル事務局\r\n";
            // text += MAIL_ADDR_ADMIN + "\r\n";
            // text += "[お手続きNo." + procedureNo + "]\r\n";
            // text += "お問い合わせの際はメールにお手続きNoを記入してください。\r\n";
            //
            // // メール送信を行う
            // SendMail.send( MAIL_ADDR_ADMIN, mailAddr, title, text );

            title = "【ハピホテマイル申請お手続きNo." + procedureNo + "(" + dhb.getName() + ")】";
            text = "会員状態:" + userKind + "\r\n";
            text += "お名前:" + dataLoginInfo.getUserName() + "様\r\n";
            text += "ハピホテID:" + dataLoginInfo.getUserId() + "\r\n";
            text += "ホテル名:" + dhb.getName() + "\r\n";
            text += "ホテルID:" + dhb.getId() + "\r\n";
            text += "ハピホテマイル残高:" + nfComma.format( upp.getNowPoint( dataLoginInfo.getUserId(), false ) ) + "マイル\r\n";
            text += "[お手続きNo." + procedureNo + "]\r\n";
            // 社内でテストする場合はコメントアウトする（premium_info@happyhotel.jp宛てにメール送信）
            SendMail.send( MAIL_ADDR_ADMIN, MAIL_ADDR_ADMIN, title, text );

        }
    }
}
