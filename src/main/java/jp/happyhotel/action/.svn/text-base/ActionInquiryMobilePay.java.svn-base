package jp.happyhotel.action;

import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.SendMail;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataUserInputDataTemp;
import jp.happyhotel.data.DataUserInquiryPay;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserInquiryPay;
import jp.happyhotel.user.UserPoint;
import jp.happyhotel.user.UserPointPay;
import jp.happyhotel.user.UserTermInfo;

/**
 * 
 * 有料問い合わせクラス
 * 
 * @author S.Tashiro
 * @version 1.0 2009/11/09
 */

public class ActionInquiryMobilePay extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;
    private DataLoginInfo_M2  dataLoginInfo_M2  = null;
    private final String      CHECK_URL         = "ssl.happyhotel.jp";

    // private final String CHECK_URL = "10.120.8.70";
    // private final String CHECK_URL = "121.101.88.177";

    /**
     * 問い合わせの処理を行う
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param response サーバからクライアントへのレスポンス
     * @see "step 0:問い合わせ入力ページへ<br>
     *      1:入力データ保存処理<br>
     *      2:問い合わせ確認ページ<br>
     *      3:問い合わせ完了ページ<br>
     *      それ以外:問い合わせ入力ページへ"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret;
        boolean memberFlag;
        boolean paymemberFlag = false;
        boolean paymemberTempFlag = false;
        int registStatus;
        int delFlag;
        int carrierFlag;
        int gpsFlag = 0;
        int getMethodFlag = 0;
        String paramUidLink;
        String paramAcRead;
        String paramStep;
        String paramUserName;
        String paramMailAddr;
        String paramInquiryNo;
        String paramInquiryNoSub;
        String paramInquiryKind;
        String paramInquiry;
        String uidParam;
        String strErr = "";
        String strRegistNo = "0"; // hh_user_inquiry_payに登録されたinquiry_noを管理する
        String strMobileTermNo = "";

        AuAuthCheck auCheck;
        DataUserInquiryPay duip;
        DataUserInputDataTemp duidt;
        UserTermInfo uti;
        carrierFlag = 0;
        duidt = new DataUserInputDataTemp();
        duip = new DataUserInquiryPay();

        try
        {
            this.dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );

            // 端末番号の取得
            carrierFlag = UserAgent.getUserAgentType( request );
            if ( carrierFlag == UserAgent.USERAGENT_AU )
            {
                uidParam = request.getHeader( "x-up-subno" );
            }
            else if ( carrierFlag == UserAgent.USERAGENT_VODAFONE )
            {
                if ( request.getServerPort() == 80 && request.getServerPort() == 8080 && request.getServerPort() == 10080 )
                {
                    if ( request.getRequestURL().indexOf( CHECK_URL ) != -1 )
                    {
                        // メールアドレスハッシュ値の取得
                        uidParam = request.getParameter( "yuid" );
                    }
                    else
                    {
                        uidParam = request.getHeader( "x-jphone-uid" );
                        // UID通知していない場合、uidParamがnullになる
                        if ( uidParam != null )
                        {
                            uidParam = uidParam.substring( 1 );
                        }
                    }
                }
                else
                {
                    // メールアドレスハッシュ値の取得
                    uidParam = request.getParameter( "yuid" );
                }
            }
            else
            {
                uidParam = request.getParameter( "uid" );
            }

            // 会員判別を行う
            if ( this.dataLoginInfo_M2 != null )
            {
                memberFlag = this.dataLoginInfo_M2.isMemberFlag();
                paymemberFlag = this.dataLoginInfo_M2.isPaymemberFlag();
                paymemberTempFlag = this.dataLoginInfo_M2.isPaymemberTempFlag();
                registStatus = this.dataLoginInfo_M2.getRegistStatus();
                delFlag = this.dataLoginInfo_M2.getDelFlag();
                carrierFlag = this.dataLoginInfo_M2.getCarrierFlag();
                strMobileTermNo = this.dataLoginInfo_M2.getMobileTermNo();
            }
            else
            {
                if ( uidParam != null )
                {
                    // GPSフラグを取得する
                    uti = new UserTermInfo();
                    if ( uti.getTermInfo( request ) )
                    {
                        gpsFlag = uti.getTerm().getGpsFlag();
                    }

                    // DataLoginInfo_M2の取得メソッドを設定
                    if ( (request.getServerPort() != 80 && request.getServerPort() != 8080 && request.getServerPort() != 10080) && (carrierFlag == UserAgent.USERAGENT_DOCOMO || carrierFlag == UserAgent.USERAGENT_VODAFONE) )
                    {
                        getMethodFlag = 1;
                    }
                    else
                    {
                        if ( (request.getRequestURL().indexOf( CHECK_URL ) != -1) && (carrierFlag == UserAgent.USERAGENT_DOCOMO || carrierFlag == UserAgent.USERAGENT_VODAFONE) )
                        {
                            getMethodFlag = 1;
                        }
                        else
                        {
                            getMethodFlag = 0;
                        }
                    }
                    // DataLoginInfo_M2を取得
                    ret = this.getDataLoginInfo( uidParam, getMethodFlag, carrierFlag, gpsFlag );
                    if ( ret != false )
                    {
                        memberFlag = this.dataLoginInfo_M2.isMemberFlag();
                        paymemberFlag = this.dataLoginInfo_M2.isPaymemberFlag();
                        paymemberTempFlag = this.dataLoginInfo_M2.isPaymemberTempFlag();
                        registStatus = this.dataLoginInfo_M2.getRegistStatus();
                        delFlag = this.dataLoginInfo_M2.getDelFlag();
                        carrierFlag = this.dataLoginInfo_M2.getCarrierFlag();
                        strMobileTermNo = this.dataLoginInfo_M2.getMobileTermNo();
                    }
                }
            }

            // パラメータの取得
            paramStep = request.getParameter( "step" );
            if ( paramStep == null || paramStep.compareTo( "" ) == 0 || CheckString.numCheck( paramStep ) == false )
            {
                paramStep = "0";
            }

            // stepが2だったらDBに一時保存
            if ( Integer.parseInt( paramStep ) == 1 )
            {
                try
                {
                    paramUserName = request.getParameter( "user_name" );
                    paramMailAddr = request.getParameter( "mailaddr" );
                    paramInquiry = request.getParameter( "inquiry" );
                    paramInquiryNo = request.getParameter( "no" );
                    paramInquiryNoSub = request.getParameter( "no_sub" );
                    paramInquiryKind = request.getParameter( "kind" );
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

                    // データ登録するためにユーザー情報を取得
                    if ( this.dataLoginInfo_M2 != null && (paymemberFlag != false || paymemberTempFlag != false) )
                    {

                        // 取得したパラメータを一時保存
                        ret = duidt.getData( this.dataLoginInfo_M2.getUserId() );
                        duidt = null;
                        duidt = new DataUserInputDataTemp();
                        duidt.setUserId( this.dataLoginInfo_M2.getUserId() );
                        duidt.setUserName( ReplaceString.DBEscape( paramUserName ) );
                        duidt.setTermNo( this.dataLoginInfo_M2.getMobileTermNo() );
                        duidt.setMailAddr( ReplaceString.DBEscape( paramMailAddr ) );
                        duidt.setInquiryNo( Integer.parseInt( paramInquiryNo ) );
                        duidt.setInquiryNoSub( Integer.parseInt( paramInquiryNoSub ) );
                        duidt.setInquiryKind( ReplaceString.DBEscape( paramInquiryKind ) );
                        duidt.setInquiry( ReplaceString.DBEscape( paramInquiry ) );
                        duidt.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        duidt.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        // データがあればアップデート、なければインサート
                        if ( ret != false )
                        {
                            ret = duidt.updateData( this.dataLoginInfo_M2.getUserId() );
                        }
                        else
                        {
                            ret = duidt.insertData();
                        }
                        if ( ret != false )
                        {
                            response.sendRedirect( "inquiryMobilePay.act?step=2&no=" + paramInquiryNo + "&no_sub=" + paramInquiryNoSub + "&" + paramUidLink );
                            return;
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[ActionInquiryMobilePay tempData] Exception:" + e.toString() );
                }
            }
            // auだったらアクセスチケットをチェックする
            paramAcRead = request.getParameter( "acread" );
            carrierFlag = UserAgent.getUserAgentType( request );
            /*
             * if ( (paramAcRead == null) && (carrierFlag == DataMasterUseragent.CARRIER_AU) )
             * {
             * try
             * {
             * auCheck = new AuAuthCheck();
             * ret = auCheck.authCheckForClass( request, false );
             * // アクセスチケット確認の結果 falseだったらリダイレクト
             * if ( ret == false )
             * {
             * response.sendRedirect( auCheck.getResultData() );
             * return;
             * }
             * // アクセスチケット確認の結果 trueだったら情報を取得
             * else
             * {
             * // DataLoginInfo_M2を取得する
             * if ( auCheck.getDataLoginInfo() != null )
             * {
             * this.dataLoginInfo_M2 = auCheck.getDataLoginInfo();
             * // それぞれを更新する
             * memberFlag = this.dataLoginInfo_M2.isMemberFlag();
             * paymemberFlag = this.dataLoginInfo_M2.isPaymemberFlag();
             * paymemberTempFlag = this.dataLoginInfo_M2.isPaymemberTempFlag();
             * registStatus = this.dataLoginInfo_M2.getRegistStatus();
             * delFlag = this.dataLoginInfo_M2.getDelFlag();
             * carrierFlag = this.dataLoginInfo_M2.getCarrierFlag();
             * }
             * }
             * }
             * catch ( Exception e )
             * {
             * Logging.info( "[ActionInquiryMobilePay AuAuthCheck] Exception:" + e.toString() );
             * }
             * }
             */
            // それぞれの処理を行う
            try
            {
                // DBに保存したデータを取得する
                if ( this.dataLoginInfo_M2 != null && (paymemberFlag != false || paymemberTempFlag != false) )
                {
                    request.setAttribute( "LOGIN_INFO", this.dataLoginInfo_M2 );
                    ret = duidt.getData( this.dataLoginInfo_M2.getUserId() );
                    if ( ret != false )
                    {
                        strErr = this.checkUserInputDataTemp( duidt );
                        request.setAttribute( "USER_INPUT_DATA_TEMP", duidt );
                    }
                }
                request.setAttribute( "ERROR", strErr );

                // 問い合わせフォームへ
                if ( Integer.parseInt( paramStep ) == 0 )
                {
                    requestDispatcher = request.getRequestDispatcher( "inquiry_pay.jsp?" + paramUidLink );
                }
                // 問い合わせフォーム確認画面へ
                else if ( Integer.parseInt( paramStep ) == 2 )
                {
                    requestDispatcher = request.getRequestDispatcher( "inquiry_pay_confirm.jsp?" + paramUidLink );
                }
                // 問い合わせフォーム完了画面へ
                else if ( Integer.parseInt( paramStep ) == 3 )
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
                                    this.sendMail( duidt.getMailAddr(), duidt.getUserId(), duidt.getUserName(), duidt.getInquiryKind(), duidt.getInquiry(), strRegistNo, request.getHeader( "user-agent" ) );
                                    request.setAttribute( "INQUIRY_NO", strRegistNo );
                                }
                            }
                        }
                    }
                    requestDispatcher = request.getRequestDispatcher( "inquiry_pay_complete.jsp?" + paramUidLink );
                }
                else
                {
                    // requestDispatcher = request.getRequestDispatcher( "inquiry_pay.jsp?" + paramUidLink );
                    requestDispatcher = request.getRequestDispatcher( "inquiry_pay.jsp" );
                }
                requestDispatcher.forward( request, response );
            }
            catch ( Exception e )
            {
                Logging.info( "[ActionInquiryMobilePay requestDispatcher] Exception:" + e.toString() );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionInquiryMobilePay dataLoginInfo] Exception:" + e.toString() );
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
                    return("ﾃﾞｰﾀがありません");
                }

                if ( duidt.getUserId().compareTo( "" ) == 0 )
                {
                    strErrMsg += "ﾕｰｻﾞｰIDが取得できません<br>";
                }
                if ( duidt.getUserName().compareTo( "" ) == 0 )
                {
                    strErrMsg += "お名前を入力してください<br>";
                }
                if ( duidt.getTermNo().compareTo( "" ) == 0 )
                {
                    strErrMsg += "端末番号が取得できません<br>";
                }
                if ( duidt.getMailAddr().compareTo( "" ) == 0 )
                {
                    strErrMsg += "ﾒｰﾙｱﾄﾞﾚｽを入力してください<br>";
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
                            strErrMsg += "ﾒｰﾙｱﾄﾞﾚｽを正しく入力してください<br>";
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
                    // strErrMsg += "以下の項目がNGﾜｰﾄﾞとなるため、登録できません<br>";
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
            Logging.error( "[ActionInquiryMobilePay checkUserInputDataTemp] Exception:" + e.toString() );
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
     * @param userAgent ユーザエージェント
     */
    private void sendMail(String mailAddr, String userId, String userName, String inquirykind, String inquiry, String inquiryNo, String userAgent)
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
            Logging.error( "[ActionInquiryMobilePay sendMail] Exception:" + e.toString() );
        }
        title = "[ハピホテ]お問い合わせ受付完了";

        text = "ハピホテプレミアムコース\r\n";
        text += "お問い合わせを受け付けました。\r\n";
        text += "【問い合わせNo." + inquiryNo + "】\r\n\r\n";
        text += "お問い合わせの内容によって回答までに数日かかることがございますので何卒ご了承下さい\r\n\r\n";
        text += "お問い合わせの回答はメールおよびマイページ内にて回答させていただきます。\r\n\r\n";

        text += "【ご連絡内容】\r\n" + inquirykind + "\r\n【問い合わせ内容】\r\n" + inquiry + "\r\n\r\n";
        text += "------------------------\r\n";
        text += "ハッピーホテル事務局\r\n";
        // text += "(mailto:premium_info@happyhotel.jp?subject=" + encdata + ")\r\n";
        text += "premium_info@happyhotel.jp\r\n";
        text += "[問い合わせNo." + inquiryNo + "]\r\n";
        text += "お問い合わせの際はメールに問い合わせNoを記入してください。\r\n";

        // メール送信を行う
        SendMail.send( "premium_info@happyhotel.jp", mailAddr, title, text );

        title = "【プレミアムお問い合わせNo." + inquiryNo + "(携帯)】";
        text = "お名前:" + userName + "様\r\n";
        text += "ハピホテID:" + userId + "\r\n";
        text += "ユーザエージェント:" + userAgent + "\r\n\r\n";
        text += "【ご連絡内容】\r\n" + inquirykind + "\r\n【問い合わせ内容】\r\n" + inquiry + "\r\n\r\n";
        // 社内でテストする場合はコメントアウトする（premium_info@happyhotel.jp宛てにメール送信）
        SendMail.send( mailAddr, "premium_info@happyhotel.jp", title, text );
    }

    /**
     * DataLoginInfo_M2データを取得する
     * 
     * @param uidParam 端末番号
     * @param getFlag 取得フラグ(0:メールアドレスのハッシュ値から取得、1:端末番号から取得)
     * @param carrierFlag キャリアフラグ
     * @param gpsFlag GPSフラグ
     * @return 処理結果(true:成功、false:失敗)
     */
    private boolean getDataLoginInfo(String uidParam, int getFlag, int carrierFlag, int gpsFlag)
    {
        boolean ret;
        UserBasicInfo ubi;
        UserPoint up;
        UserPointPay upp;

        ret = false;
        try
        {
            // 登録ステータス無視でデータを取得する
            ubi = new UserBasicInfo();
            up = new UserPoint();
            upp = new UserPointPay();
            // 社内環境でのテストのため変更
            if ( getFlag == 1 )
            {
                ret = ubi.getUserBasicByMd5NoCheck( uidParam );
            }
            else if ( getFlag == 0 )
            {
                ret = ubi.getUserBasicByTermnoNoCheck( uidParam );
            }
            else
            {
                return(false);
            }

            if ( ret != false )
            {
                this.dataLoginInfo_M2 = new DataLoginInfo_M2();
                this.dataLoginInfo_M2.setUserId( ubi.getUserInfo().getUserId() );
                this.dataLoginInfo_M2.setUserName( ubi.getUserInfo().getHandleName() );
                this.dataLoginInfo_M2.setUserPoint( up.getNowPoint( ubi.getUserInfo().getUserId(), false ) );
                this.dataLoginInfo_M2.setRegistStatus( ubi.getUserInfo().getRegistStatus() );
                this.dataLoginInfo_M2.setDelFlag( ubi.getUserInfo().getDelFlag() );
                this.dataLoginInfo_M2.setCarrierFlag( carrierFlag );
                this.dataLoginInfo_M2.setGpsFlag( gpsFlag );
                this.dataLoginInfo_M2.setMemberFlag( true );
                this.dataLoginInfo_M2.setMailAddr( ubi.getUserInfo().getMailAddr() );
                this.dataLoginInfo_M2.setMailAddrMobile( ubi.getUserInfo().getMailAddrMobile() );
                this.dataLoginInfo_M2.setMobileTermNo( ubi.getUserInfo().getMobileTermNo() );
                // 有料会員情報
                this.dataLoginInfo_M2.setRegistStatusPay( ubi.getUserInfo().getRegistStatusPay() );
                this.dataLoginInfo_M2.setRegistStatusOld( ubi.getUserInfo().getRegistStatusOld() );
                this.dataLoginInfo_M2.setAccessTicket( ubi.getUserInfo().getAccessTicket() );
                this.dataLoginInfo_M2.setUserPointPay( upp.getNowPoint( ubi.getUserInfo().getUserId(), false ) );
                if ( ubi.getUserInfo().getRegistStatusPay() == 9 )
                {
                    this.dataLoginInfo_M2.setPaymemberFlag( true );
                    this.dataLoginInfo_M2.setPaymemberTempFlag( false );
                }
                else
                {
                    this.dataLoginInfo_M2.setPaymemberFlag( false );
                    // 有料仮登録状態かどうか
                    if ( ubi.getUserInfo().getRegistStatusPay() == 1 )
                    {
                        this.dataLoginInfo_M2.setPaymemberTempFlag( true );
                    }
                    else
                    {
                        this.dataLoginInfo_M2.setPaymemberTempFlag( false );
                    }
                }

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionInquiryMobilePay getDataLoginInfo] Exception:" + e.toString() );
        }

        return(ret);
    }
}
