package jp.happyhotel.action;

import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.SendMail;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataUserInputDataTemp;
import jp.happyhotel.data.DataUserInquiryPay;
import jp.happyhotel.user.UserInquiryPay;

/**
 * 
 * 有料問い合わせクラス
 * 
 * @author S.Tashiro
 * @version 1.0 2009/11/09
 */

public class ActionInquiryPay extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private DataLoginInfo_M2  dataLoginInfo_M2  = null;

    /**
     * 問い合わせの処理を行う
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param response サーバからクライアントへのレスポンス
     * 
     * @see "step 0:問い合わせ入力ページへ<br>
     *      1:問い合わせ確認ページ<br>
     *      2:問い合わせ完了ページ<br>
     *      それ以外:問い合わせ入力ページへ"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret;
        boolean memberFlag;
        boolean paymemberFlag = false;
        boolean paymemberTempFlag = false;
        int i;
        int registStatus;
        int delFlag;
        int scale;
        int dispScale;
        int carrierFlag;
        int distance;
        int gpsFlag = 0;
        String paramUidLink;
        String paramAcRead;
        String paramStep;
        String paramUserName;
        String paramMailAddr;
        String paramInquiryNo;
        String paramInquiryNoSub;
        String paramInquiryKind;
        String paramInquiry;
        String paramHotelName;
        String paramHotelAddr;
        String uidParam;
        String strErr = "";
        String strRegistNo = "0"; // hh_user_inquiry_payに登録されたinquiry_noを管理する
        AuAuthCheck auCheck;
        Cookie[] cookies;
        Cookie hhuidCookie;
        DataUserInquiryPay duip;
        DataUserInputDataTemp duidt;

        carrierFlag = 0;
        hhuidCookie = null;
        duidt = new DataUserInputDataTemp();
        duip = new DataUserInquiryPay();

        try
        {
            dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            // 会員判別を行う
            if ( dataLoginInfo_M2 != null )
            {
                memberFlag = dataLoginInfo_M2.isMemberFlag();
                paymemberFlag = dataLoginInfo_M2.isPaymemberFlag();
                paymemberTempFlag = dataLoginInfo_M2.isPaymemberTempFlag();
                registStatus = dataLoginInfo_M2.getRegistStatus();
                delFlag = dataLoginInfo_M2.getDelFlag();
                carrierFlag = dataLoginInfo_M2.getCarrierFlag();
            }

            // パラメータの取得
            paramStep = request.getParameter( "step" );
            if ( paramStep == null || paramStep.compareTo( "" ) == 0 || CheckString.numCheck( paramStep ) == false )
            {
                paramStep = "0";
            }

            // それぞれの処理を行う
            try
            {
                // データチェックを行う（DB登録処理後行う）
                if ( Integer.parseInt( paramStep ) == 0 || Integer.parseInt( paramStep ) == 2 )
                {
                    // DBに保存したデータを取得する
                    if ( dataLoginInfo_M2 != null && (paymemberFlag != false || paymemberTempFlag != false) )
                    {
                        request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                        ret = duidt.getData( dataLoginInfo_M2.getUserId() );
                        if ( ret != false )
                        {
                            strErr = this.checkUserInputDataTemp( duidt );
                            request.setAttribute( "USER_INPUT_DATA_TEMP", duidt );
                        }
                    }
                    request.setAttribute( "ERROR", strErr );
                }

                // 問い合わせフォームへ
                if ( Integer.parseInt( paramStep ) == 0 )
                {
                    requestDispatcher = request.getRequestDispatcher( "inquiry_pay.jsp" );
                }
                // stepが2だったらDBに保存
                else if ( Integer.parseInt( paramStep ) == 1 )
                {
                    paramUserName = request.getParameter( "name" );
                    paramMailAddr = request.getParameter( "mail" );
                    paramInquiry = request.getParameter( "comment" );
                    paramInquiryNo = request.getParameter( "no" );
                    paramInquiryNoSub = request.getParameter( "no_sub" );
                    paramInquiryKind = request.getParameter( "subject" );
                    paramHotelName = request.getParameter( "hotel" );
                    paramHotelAddr = request.getParameter( "address" );
                    if ( paramUserName == null )
                    {
                        paramUserName = "";
                    }
                    if ( paramMailAddr == null )
                    {
                        paramMailAddr = "";
                    }
                    if ( paramInquiry == null )
                    {
                        paramInquiry = "";
                    }
                    if ( paramInquiryNo == null || paramInquiryNo.compareTo( "" ) == 0 || CheckString.numCheck( paramInquiryNo ) == false )
                    {
                        paramInquiryNo = "0";
                    }
                    if ( paramInquiryNoSub == null || paramInquiryNoSub.compareTo( "" ) == 0 || CheckString.numCheck( paramInquiryNoSub ) == false )
                    {
                        paramInquiryNoSub = "0";
                    }
                    if ( paramInquiryKind == null )
                    {
                        paramInquiryKind = "";
                    }
                    if ( paramHotelName == null )
                    {
                        paramHotelName = "";
                    }
                    if ( paramHotelAddr == null )
                    {
                        paramHotelAddr = "";
                    }

                    // データ登録するためにユーザー情報を取得
                    if ( dataLoginInfo_M2 != null && (paymemberFlag != false || paymemberTempFlag != false) )
                    {

                        // クッキー（セッションID）を取得
                        cookies = request.getCookies();
                        if ( cookies != null )
                        {
                            for( i = 0 ; i < cookies.length ; i++ )
                            {
                                if ( cookies[i].getName().compareTo( "JSESSIONID" ) == 0 )
                                {
                                    hhuidCookie = cookies[i];
                                    break;
                                }
                            }
                        }

                        // 取得したパラメータを一時保存
                        ret = duidt.getData( dataLoginInfo_M2.getUserId() );
                        // 取得したデータをクリアする
                        duidt = null;
                        duidt = new DataUserInputDataTemp();
                        // 仮領域に登録するデータをセットする
                        duidt.setUserId( dataLoginInfo_M2.getUserId() );
                        duidt.setUserName( ReplaceString.DBEscape( paramUserName ) );
                        // クッキーがあったらtermnoに登録
                        if ( hhuidCookie != null )
                        {
                            duidt.setTermNo( hhuidCookie.getValue() );
                        }
                        else
                        {
                            duidt.setTermNo( "" );
                        }
                        duidt.setMailAddr( ReplaceString.DBEscape( paramMailAddr ) );
                        duidt.setHotelName( ReplaceString.DBEscape( paramHotelName ) );
                        duidt.setHotelAddress( ReplaceString.DBEscape( paramHotelAddr ) );
                        duidt.setInquiryNo( Integer.parseInt( paramInquiryNo ) );
                        duidt.setInquiryNoSub( Integer.parseInt( paramInquiryNoSub ) );
                        duidt.setInquiryKind( ReplaceString.DBEscape( paramInquiryKind ) );
                        duidt.setInquiry( ReplaceString.DBEscape( paramInquiry ) );

                        // データがあればアップデート、なければインサート
                        if ( ret != false )
                        {
                            ret = duidt.updateData( dataLoginInfo_M2.getUserId() );
                        }
                        else
                        {
                            ret = duidt.insertData();
                        }

                        if ( ret != false )
                        {
                            // 登録しなおしたデータで再度エラーチェックを行う。
                            strErr = this.checkUserInputDataTemp( duidt );
                            request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                            request.setAttribute( "USER_INPUT_DATA_TEMP", duidt );
                            request.setAttribute( "ERROR", strErr );
                            requestDispatcher = request.getRequestDispatcher( "inquiry_pay_confirm.jsp" );
                        }
                    }
                    else
                    {
                        requestDispatcher = request.getRequestDispatcher( "inquiry_pay_confirm.jsp" );
                    }
                }
                // 問い合わせフォーム完了画面へ
                else if ( Integer.parseInt( paramStep ) == 2 )
                {
                    // 有料会員、有料途中会員のみ
                    if ( dataLoginInfo_M2 != null && (paymemberFlag != false || paymemberTempFlag != false) )
                    {
                        duip = new DataUserInquiryPay();
                        // エラーがない場合、データ書き込みを行う。
                        if ( strErr.compareTo( "" ) == 0 )
                        {
                            // 共通項目をセット
                            duip.setInquiryNoSub( 0 );
                            duip.setUserId( duidt.getUserId() );
                            duip.setUserName( duidt.getUserName() );
                            duip.setTermNo( duidt.getTermNo() );
                            duip.setInquiryKind( duidt.getInquiryKind() );
                            duip.setMailAddr( duidt.getMailAddr() );
                            duip.setHotelName( duidt.getHotelName() );
                            duip.setHotelAddress( duidt.getHotelAddress() );
                            duip.setInquiry( duidt.getInquiry() );
                            duip.setInquiryDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            duip.setInquiryTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            duip.setInquiryIp( request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr() );
                            duip.setInquiryUseragent( request.getHeader( "user-agent" ) );
                            // 問い合わせ番号がある場合は、その番号に枝番を追加
                            if ( duidt.getInquiryNo() > 0 )
                            {
                                // 新しく追加を行う
                                duip.setInquiryNo( duidt.getInquiryNo() );
                                duip.setInquiryNoSub( duip.getMaxInquiryNoSub( duidt.getInquiryNo() ) + 1 );
                                ret = duip.insertDataAsInquiryNo();
                            }
                            else
                            {
                                ret = duip.insertData();
                            }
                            if ( ret == false )
                            {
                                strErr += "データ追加に失敗しました。";
                                request.setAttribute( "ERROR", strErr );
                            }
                            else
                            {
                                // 問い合わせ番号が0だったら、インサートされたデータを取得
                                if ( duidt.getInquiryNo() == 0 )
                                {
                                    strRegistNo = Integer.toString( duip.getMaxInquiryNoByUserId( duidt.getUserId() ) );
                                }
                                else
                                {
                                    strRegistNo = Integer.toString( duidt.getInquiryNo() );
                                }
                                // ユーザーの問い合わせ番号の最大を取得
                                if ( Integer.parseInt( strRegistNo ) > 0 )
                                {
                                    // 問い合わせ番号に枝番があるかどうかを確認
                                    if ( duip.getMaxInquiryNoSub( Integer.parseInt( strRegistNo ) ) > 0 )
                                    {
                                        strRegistNo += "-" + duip.getMaxInquiryNoSub( Integer.parseInt( strRegistNo ) );
                                    }
                                    // メールを送信
                                    this.sendMail( duidt.getMailAddr(), duidt.getUserId(), duidt.getUserName(), duidt.getInquiryKind(), duidt.getInquiry(), strRegistNo,
                                            duidt.getHotelName(), duidt.getHotelAddress() );
                                    request.setAttribute( "INQUIRY_NO", strRegistNo );
                                }
                            }
                        }
                    }
                    requestDispatcher = request.getRequestDispatcher( "inquiry_pay_complete.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( "inquiry_pay.jsp" );
                }
                requestDispatcher.forward( request, response );
            }
            catch ( Exception e )
            {
                Logging.info( "[ActionInquiryPay requestDispatcher] Exception:" + e.toString() );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionInquiryPay dataLoginInfo] Exception:" + e.toString() );
        }
    }

    /**
     * DBに登録された仮登録データのチェックを行う
     * 
     * @param duidt DataUserInputDataTempをセット
     * @see "step 0:問い合わせ入力ページへ<br>
     *      1:入力データ保存処理<br>
     *      2:問い合わせ確認ページ<br>
     *      3:問い合わせ完了ページ<br>
     *      それ以外:問い合わせ入力ページへ"
     */
    private String checkUserInputDataTemp(DataUserInputDataTemp duidt)
    {
        UserInquiryPay uip;
        String strErrMsg;
        strErrMsg = "";

        try
        {
            uip = new UserInquiryPay();
            while( true )
            {
                if ( duidt == null )
                {
                    return("データがありません");
                }

                if ( duidt.getUserId().compareTo( "" ) == 0 )
                {
                    strErrMsg += "ユーザーIDが取得できません<br>";
                }
                if ( duidt.getUserName().compareTo( "" ) == 0 )
                {
                    strErrMsg += "お名前を入力してください<br>";
                }
                /*
                 * if ( duidt.getTermNo().compareTo( "" ) == 0 )
                 * {
                 * strErrMsg += "端末番号が取得できません<br>";
                 * }
                 */
                if ( duidt.getMailAddr().compareTo( "" ) == 0 )
                {
                    strErrMsg += "メールアドレスを入力してください<br>";
                }
                else
                {
                    // 携帯のメールアドレス以外を探す
                    if ( duidt.getMailAddr().indexOf( "@docomo.ne.jp" ) == -1 &&
                            duidt.getMailAddr().indexOf( "@disney.ne.jp" ) == -1 &&
                            duidt.getMailAddr().indexOf( "@ezweb.ne.jp" ) == -1 &&
                            duidt.getMailAddr().indexOf( "@au.com" ) == -1 &&
                            duidt.getMailAddr().indexOf( "@softbank.ne.jp" ) == -1 &&
                            duidt.getMailAddr().indexOf( "@t.vodafone.ne.jp" ) == -1 &&
                            duidt.getMailAddr().indexOf( "@d.vodafone.ne.jp" ) == -1 &&
                            duidt.getMailAddr().indexOf( "@h.vodafone.ne.jp" ) == -1 &&
                            duidt.getMailAddr().indexOf( "@c.vodafone.ne.jp" ) == -1 &&
                            duidt.getMailAddr().indexOf( "@k.vodafone.ne.jp" ) == -1 &&
                            duidt.getMailAddr().indexOf( "@r.vodafone.ne.jp" ) == -1 &&
                            duidt.getMailAddr().indexOf( "@n.vodafone.ne.jp" ) == -1 &&
                            duidt.getMailAddr().indexOf( "@s.vodafone.ne.jp" ) == -1 &&
                            duidt.getMailAddr().indexOf( "@q.vodafone.ne.jp" ) == -1 )
                    {
                        // メールの規約に沿っていないメールアドレスはエラーとする
                        if ( !duidt.getMailAddr().matches( "[\\w\\d._-]+\\@[\\w\\d_-]+\\.[\\w\\d._-]+" ) )
                        {
                            strErrMsg += "メールアドレスを正しく入力してください<br>";
                        }
                    }
                }
                if ( duidt.getInquiryKind().compareTo( "" ) == 0 )
                {
                    strErrMsg += "ご連絡内容を選択してください<br>";
                }
                if ( duidt.getInquiry().compareTo( "" ) == 0 )
                {
                    strErrMsg += "問い合わせ内容を入力してください<br>";
                }
                else
                {
                    // if ( CheckNgWord.outPutNgWord( duidt.getInquiry() ).compareTo( "" ) != 0 )
                    // {
                    // strErrMsg += "以下の項目がNGワードとなるため、登録できません<br>";
                    // strErrMsg += CheckNgWord.outPutNgWord( duidt.getInquiry() ) + "<br>";
                    // }
                }
                if ( uip.checkDuplication( duidt.getUserId(), duidt.getInquiry() ) != false )
                {
                    strErrMsg += "以前のお問い合わせと同じ内容のため受付できません<br>";
                }
                break;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionInquiryPay checkUserInputDataTemp] Exception:" + e.toString() );
        }

        return(strErrMsg);
    }

    /**
     * メールを送信する
     * 
     * @param mailAddr メールアドレス
     * @param UserName ユーザ名
     * @param inquirykind ご連絡内容
     * @param inquiry 問い合わせ内容
     * @param inquiryNo お問い合わせ番号
     * @param hotelName ホテル名
     * @param hotelAddr ホテル所在地
     * 
     */
    private void sendMail(String mailAddr, String userId, String userName, String inquirykind, String inquiry, String inquiryNo, String hotelName, String hotelAddr)
    {
        // メールの送信
        String title = "";
        String encdata = "";
        String text = "";

        try
        {
            encdata = URLEncoder.encode( "問い合わせNo." + inquiryNo, "Shift_JIS" );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionInquiryPay sendMail] Exception:" + e.toString() );
        }
        title = "【プレミアムコースお問い合わせ受付完了のお知らせ】";

        text = "ハピホテプレミアムコース\r\n";
        text += "お問い合わせを受け付けました。\r\n";
        text += "【問い合わせNo." + inquiryNo + "】\r\n\r\n";
        text += "お問い合わせの内容によって回答\r\nまでに数日かかることがございます\r\nので何卒ご了承下さい\r\n\r\n";
        text += "お問い合わせの回答はメールおよび\r\nマイページ内にて回答させていただきます。\r\n\r\n";
        text += "【ホテル名】\r\n" + hotelName + "\r\n【ホテル所在地】\r\n" + hotelAddr + "\r\n\r\n";
        text += "【ご連絡内容】\r\n" + inquirykind + "\r\n【問い合わせ内容】\r\n" + inquiry + "\r\n\r\n";
        text += "---------------------------------------------------------------------------\r\n";
        text += "ハッピーホテル事務局\r\n";
        text += "(mailto:premium_info@happyhotel.jp?subject=" + encdata + ")\r\n";

        // メール送信を行う
        SendMail.send( "premium_info@happyhotel.jp", mailAddr, title, text );

        title = "【プレミアムお問い合わせNo." + inquiryNo + "(PC)】";
        text = "お名前:" + userName + "様\r\n";
        text += "ハピホテID:" + userId + "\r\n\r\n";
        text += "【ホテル名】\r\n" + hotelName + "\r\n【ホテル所在地】\r\n" + hotelAddr + "\r\n\r\n";
        text += "【ご連絡内容】\r\n" + inquirykind + "\r\n【問い合わせ内容】\r\n" + inquiry + "\r\n\r\n";
        SendMail.send( mailAddr, "premium_info@happyhotel.jp", title, text );
    }
}
