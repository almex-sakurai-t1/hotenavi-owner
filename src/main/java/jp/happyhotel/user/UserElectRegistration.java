package jp.happyhotel.user;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckNgWord;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.SendMail;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataMasterChain;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.data.DataMasterPresent;
import jp.happyhotel.data.DataMasterZip;
import jp.happyhotel.data.DataUserElect;

/**
 * 
 * This class is called by the controller in case of FreeWordSearch for mobile It delegates to the related business logic for FreeWordSearch and dispatches the request to the required JSP.
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/18
 */

public class UserElectRegistration extends BaseAction
{

    /**
     *
     */
    private static final long serialVersionUID  = -9182084211676295161L;

    private RequestDispatcher requestDispatcher = null;

    /**
     * ポイントde交換　応募処理
     * 
     * @param seq 管理番号
     * @return (true:false)
     */
    public synchronized void execute(HttpServletRequest request, HttpServletResponse response)
    {
        Logging.info( "[ue.registUserElect] 排他ロック開始" );

        int nTotal;
        int nMinusPoint;
        int i;
        int type = 1;
        int carrierFlag;
        int loop;
        final int MAX_REGIST = 3;
        final int ONCE = 1;
        boolean ret;
        boolean memberFlag;
        boolean boolUserId;
        boolean boolSeq;
        String strTitle;
        String paramSeq;
        String paramOpinion;
        String strUserId;
        String strAddress1;
        String strAddress2;
        String strZipCode1;
        String strZipCode2;
        String strZipCode;
        String strName;
        String strTel;
        String strPref;
        String strMemo;
        String strErr;
        String strCheck;
        String strHandleName;
        String mailAddr;
        String strHotelName;
        String strCarrierUrl;
        String paramUidLink;
        String uidParam;
        String uidLink;
        UserBasicInfo ubi;
        DataMasterPoint dmp;
        DataMasterPresent dmPresent;
        DataHotelBasic dhb;
        DataMasterChain dmChain;
        DataUserElect due;
        UserPoint up;
        UserElect ue;
        String strString;
        Cookie[] cookies;
        Cookie hhCookie;

        ret = false;
        dmp = new DataMasterPoint();
        due = new DataUserElect();
        up = new UserPoint();
        ue = new UserElect();
        dhb = new DataHotelBasic();
        ubi = new UserBasicInfo();
        dmChain = new DataMasterChain();
        dmPresent = new DataMasterPresent();
        strTitle = "";
        strErr = "";
        nTotal = 0;
        nMinusPoint = 0;
        carrierFlag = 1;
        strZipCode = "";
        strPref = "";
        strUserId = "";
        strHandleName = "";
        mailAddr = "";
        strHotelName = "";
        strString = "～";
        boolUserId = false;
        boolSeq = false;
        memberFlag = false;
        strCarrierUrl = "";
        uidLink = "";
        hhCookie = null;

        paramUidLink = (String)request.getAttribute( "UID-LINK" );

        // cookieの確認（PC用）
        cookies = request.getCookies();
        if ( cookies != null )
        {
            for( loop = 0 ; loop < cookies.length ; loop++ )
            {
                if ( cookies[loop].getName().compareTo( "hhuid" ) == 0 )
                {
                    hhCookie = cookies[loop];
                    break;
                }
            }
        }

        if ( hhCookie != null )
        {
            ret = ubi.getUserBasicByCookie( hhCookie.getValue() );
            if ( ret != false )
            {
                memberFlag = true;
            }
        }

        // 携帯用
        if ( type == UserAgent.USERAGENT_DOCOMO || type == UserAgent.USERAGENT_VODAFONE || type == UserAgent.USERAGENT_AU )
        {

            if ( type == UserAgent.USERAGENT_AU )
            {
                uidParam = request.getHeader( "x-up-subno" );
                uidLink = "";
                strCarrierUrl = "/au/others/present_offer_hotel_write.jsp";
            }
            else if ( type == UserAgent.USERAGENT_VODAFONE )
            {
                uidParam = request.getHeader( "x-jphone-uid" );
                // UID通知していない場合、uidParamがnullになる
                if ( uidParam != null )
                {
                    uidParam = uidParam.substring( 1 );
                }
                uidLink = "uid=1&sid=BN14&pid=P423";
                strCarrierUrl = "/y/others/present_offer_hotel_write.jsp";
            }
            else if ( type == UserAgent.USERAGENT_DOCOMO )
            {
                uidParam = request.getParameter( "uid" );
                uidLink = "uid=NULLGWDOCOMO";
                strCarrierUrl = "/i/others/present_offer_hotel_write.jsp";
            }
            else
            {
                uidLink = "";
                uidParam = "";
                strCarrierUrl = "/others/present_offer_hotel_write.jsp";
            }

            if ( uidParam != null )
            {
                if ( request.getServerPort() != 80 && type == UserAgent.USERAGENT_DOCOMO )
                {
                    ret = ubi.getUserBasicByMd5( uidParam );
                }
                else
                {
                    ret = ubi.getUserBasicByTermno( uidParam );
                }
                if ( ret != false )
                {
                    memberFlag = true;
                }
                else
                {
                    memberFlag = false;
                }

                if ( request.getServerPort() != 80 && type == UserAgent.USERAGENT_DOCOMO )
                {
                    uidLink = "uid=" + "uid=" + ubi.getUserInfo().getMailAddrMobileMd5();
                }
            }
        }

        strName = request.getParameter( "name" );
        strPref = request.getParameter( "pref" );
        paramSeq = request.getParameter( "seq" );
        paramOpinion = request.getParameter( "opinion" );
        strZipCode1 = request.getParameter( "zip_code1" );
        strZipCode2 = request.getParameter( "zip_code2" );
        strAddress1 = request.getParameter( "address1" );
        strAddress2 = request.getParameter( "address2" );
        strTel = request.getParameter( "tel" );
        strMemo = request.getParameter( "memo" );
        strCheck = request.getParameter( "check" );
        if ( strZipCode1 == null )
            strZipCode1 = "";
        if ( strZipCode2 == null )
            strZipCode2 = "";
        strZipCode = strZipCode1 + strZipCode2;

        // 選択したﾃﾞｰﾀを取得
        if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || (CheckString.numCheck( paramSeq ) == false) )
        {
            paramSeq = "0";
            strErr = strErr + "賞品を選択してください。<br>";
        }
        if ( paramOpinion == null )
            paramOpinion = "";
        if ( CheckNgWord.ngWordCheck( paramOpinion ) != false )
            strErr = strErr + "NGﾜｰﾄﾞが入っているため、登録できません<br>";

        if ( (strName == null) || (strName.compareTo( "" ) == 0) )
        {
            strName = "";
            strErr = strErr + "送付先ご氏名が入力されていません。<br>";
        }
        if ( (strPref == null) || (strPref.compareTo( "" ) == 0) )
        {
            strPref = "";
            strErr = strErr + "都道府県が選択されていません。<br>";
        }
        if ( (strZipCode.length() >= 1 && strZipCode.length() <= 6) || strZipCode.compareTo( "" ) == 0 )
        {
            strZipCode = "0";
            strErr = strErr + "郵便番号が正しく入力されていません。<br>";
        }
        else
        {
            if ( CheckString.numCheck( strZipCode ) == false )
            {
                strErr = strErr + "郵便番号が正しく入力されていません。<br>";
                strZipCode = "0";
            }
        }
        if ( (strAddress1 == null) || (strAddress1.compareTo( "" ) == 0) )
        {
            strAddress1 = "";
            strErr = strErr + "市区町村が入力されていません。<br>";
        }
        if ( (strAddress2 == null) || (strAddress2.compareTo( "" ) == 0) )
        {
            strAddress2 = "";
        }
        if ( (strTel == null) || (strTel.compareTo( "" ) == 0) )
        {
            strTel = "";
            strErr = strErr + "電話番号が入力されていません。<br>";
        }
        if ( (strMemo == null) || (strMemo.compareTo( "" ) == 0) )
        {
            strMemo = "";
        }
        else
        {
            if ( CheckString.numCheck( strTel.replaceAll( "-", "" ) ) == false )
            {
                strErr = strErr + "電話番号に不備があります。<br>";
            }
        }

        // 選択した都道府県名と、郵便番号が一致するか？
        if ( strPref.compareTo( "0" ) != 0 && strZipCode.compareTo( "0" ) != 0 )
        {
            DataMasterZip dmZip;
            dmZip = new DataMasterZip();
            dmZip.getData( strZipCode );

            if ( dmZip.getPrefName().compareTo( strPref ) != 0 )
            {
                strErr = strErr + "郵便番号もしくは選択した都道府県が正しくありません。<br>";
            }
        }

        if ( (strCheck == null) || (strCheck.compareTo( "" ) == 0) )
        {
            strErr = strErr + "個人情報利用目的に同意されていません。";
        }

        // 受信した管理番号からﾌﾟﾚｾﾞﾝﾄﾏｽﾀを取得、減算ﾎﾟｲﾝﾄ数も求める
        ret = dmPresent.getData( Integer.parseInt( paramSeq ) );
        if ( ret != false )
        {
            if ( dmPresent.getLimitFrom() <= Integer.parseInt( DateEdit.getDate( 2 ) ) && dmPresent.getLimitTo() >= Integer.parseInt( DateEdit.getDate( 2 ) ) )
            {
                // ホテル名取得
                if ( dmPresent.getOfferHotel() < 100000 )
                {
                    ret = dmChain.getData( dmPresent.getOfferHotel() );
                    if ( ret != false )
                        strHotelName = dmChain.getName();
                }
                else
                {
                    ret = dhb.getData( dmPresent.getOfferHotel() );
                    if ( ret != false )
                        strHotelName = dhb.getName();
                }
                ret = dmp.getData( dmPresent.getPointCode() );
                if ( ret != false )
                {
                    nMinusPoint = dmp.getAddPoint();
                }
                else
                    strErr = strErr + "ﾎﾟｲﾝﾄが取得できませんでした。<br>";

                if ( dmPresent.getTitle().compareTo( "" ) != 0 )
                    strTitle = dmPresent.getTitle();
                else
                    strErr = strErr + "選択した賞品名が取得できませんでした。<br>";
            }
            else
                strErr = strErr + "有効期限が切れています。<br>";

            // 残数の確認( 残数が0以下または、提供数よりも応募数が上回った場合エラーとする )
            if ( dmPresent.getRemainsNumber() <= 0 || dmPresent.getElectNumber() <= ue.getUserElectBySeq( Integer.parseInt( paramSeq ) ) )
            {
                strErr = strErr + "選択された商品は残数がなくなりましたので、これ以上応募できません。<br>";
            }
        }
        else
            strErr = strErr + "選択した賞品は存在しません。<br>";

        if ( (memberFlag != false) && (ubi.getUserInfo().getRegistStatus() == 9) )
        {

            strUserId = ubi.getUserInfo().getUserId();
            strHandleName = ubi.getUserInfo().getHandleName();
            if ( ubi.getUserInfo().getMailAddrMobile().compareTo( "" ) != 0 )
            {
                mailAddr = ubi.getUserInfo().getMailAddrMobile();
            }
            else if ( ubi.getUserInfo().getMailAddr().compareTo( "" ) != 0 )
            {
                mailAddr = ubi.getUserInfo().getMailAddr();
            }

            // ﾎﾟｲﾝﾄ計算
            nTotal = up.getNowPoint( ubi.getUserInfo().getUserId(), false );
            if ( nTotal < nMinusPoint )
            {
                strErr = strErr + "ﾎﾟｲﾝﾄが足りません。<br>";
                ret = false;
            }
            else if ( nTotal == 0 )
            {
                strErr = strErr + "ﾎﾟｲﾝﾄが足りません。<br>";
                ret = false;
            }

            // すでに応募済みのユーザーは応募させない
            if ( ue.getUserElectByFirstCome( ubi.getUserInfo().getUserId(), dmPresent.getLimitFrom(), dmPresent.getLimitTo() ) != false )
            {
                if ( ue.getCount() >= MAX_REGIST )
                {
                    strErr = strErr + "すでに期間中" + MAX_REGIST + "回ご応募いただいているため、これ以上のご応募はできません。<br>";
                }
                // ホテルごとの応募データを取得
                if ( ue.getUserElectByOfferHotel( ubi.getUserInfo().getUserId(), dmPresent.getOfferHotel(), dmPresent.getPrefId(), dmPresent.getLimitFrom(), dmPresent.getLimitTo() ) != false )
                {
                    // 1回でも応募している
                    if ( ue.getCount() > 0 )
                        ;
                    {
                        // 全てのデータをチェックする。
                        for( i = 0 ; i < ue.getCount() ; i++ )
                        {
                            boolUserId = false;
                            boolSeq = false;

                            // UserIdと応募データの比較をログに出力。
                            Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp 取得データ比較 DB userId=" + ue.getUserElect()[i].getUserId() + "," +
                                    " パラメータ userId=" + ubi.getUserInfo().getUserId() +
                                    ", DB seq=" + ue.getUserElect()[i].getSeq() + "," + " パラメータ seq=" + paramSeq +
                                    ", DB application_count=" + ue.getUserElect()[i].getApplicationCount() );

                            // UserIdを応募データと比較
                            if ( ue.getUserElect()[i].getUserId().compareTo( ubi.getUserInfo().getUserId() ) == 0 )
                            {
                                boolUserId = true;
                            }
                            // 賞品の管理番号をパラメータと比較する。
                            if ( ue.getUserElect()[i].getSeq() == Integer.parseInt( paramSeq ) )
                            {
                                boolSeq = true;
                            }

                            // UserIdと応募データの比較をログに出力。
                            if ( boolUserId != false && boolSeq != false )
                            {
                                Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp boolUserId=" + boolUserId + ", boolSeq=" + boolSeq );
                                strErr = strErr + "このﾎﾃﾙへはすでにご応募済みです。これ以上のご応募はできません。<br>";
                                break;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            ret = false;
            strErr = strErr + "ﾌﾟﾚｾﾞﾝﾄ応募ｷｬﾝﾍﾟｰﾝは会員限定です。<br>";
        }

        if ( strErr.compareTo( "" ) == 0 )
        {
            due.setUserId( strUserId );
            due.setUserName( strName );
            due.setApplicationCount( ONCE );
            due.setSeq( Integer.parseInt( paramSeq ) );
            due.setZipCode( strZipCode );
            due.setPrefName( strPref );
            due.setAddress1( strAddress1 );
            due.setAddress2( strAddress2 );
            due.setTel1( strTel );
            due.setMemo( strMemo );
            due.setInputDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            due.setInputTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            due.setStatusFlag( 3 );
            ret = due.insertDataOnce();
            Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp インサート結果：" + ret );
            if ( ret != false )
            {
                ret = up.setPointOfferHotelPresent( strUserId, dmPresent.getPointCode(), dmPresent.getSeq() );
                Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp ポイント削除結果：" + ret );
            }

            if ( ret != false )
            {
                // 残数の確認
                if ( dmPresent.getRemainsNumber() > 0 && dmPresent.getElectNumber() >= ue.getUserElectBySeq( Integer.parseInt( paramSeq ) ) )
                {
                    Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp 残数=" + dmPresent.getRemainsNumber() );
                    // 残数を減らす処理
                    dmPresent.setRemainsNumber( dmPresent.getRemainsNumber() - 1 );
                    ret = dmPresent.updateData( dmPresent.getSeq() );
                    // UserIdと応募データの比較をログに出力。
                    Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp 残数を減らす処理：" + ret );

                }

                // メールの送信
                String title_mail = "【ハピホテ】プレゼント応募受付";
                String encdata = "";
                String text = "";

                try
                {
                    encdata = URLEncoder.encode( "お問い合わせ", "Shift_JIS" );
                }
                catch ( UnsupportedEncodingException e )
                {

                }

                if ( strHandleName.compareTo( "" ) != 0 )
                {
                    text = text + strHandleName + " さま" + "\r\n";
                }
                else
                {
                    text = text + strUserId + " さま" + "\r\n";
                }
                text = text + "\r\n";
                text = text + "ハッピー・ホテルをご利用いただきましてありがとうございます。" + "\r\n";
                text = text + "本メールはハッピー・ホテルのポイント交換企画にご応募いただいた\r\nお客様に送信しています。" + "\r\n";
                text = text + "\r\n";
                text = text + "ご登録いただいた住所に賞品を発送いたしますので、しばらくお待ちくださいませ。" + "\r\n";
                text = text + "\r\n";
                text = text + "【ホテル名】\r\n";
                text = text + strHotelName + "\r\n";
                text = text + "\r\n";
                if ( dmPresent.getMemo().compareTo( "" ) != 0 )
                {
                    text = text + "【備考】\r\n";
                    text = text + dmPresent.getMemo() + "\r\n";
                    text = text + "\r\n";
                }

                text = text + "【応募賞品】\r\n";
                text = text + strTitle + "（" + dmp.getAddPoint() + "pt）" + "\r\n";
                text = text + "\r\n";
                text = text + "【賞品発送先】" + "\r\n";
                text = text + strName + "様" + "\r\n";
                text = text + "〒 " + strZipCode1 + "-" + strZipCode2 + "\r\n";
                text = text + strPref + strAddress1 + strAddress2 + "\r\n";
                text = text + "TEL " + strTel + "\r\n";
                text = text + "\r\n";
                /* 追加点▼ */
                text = text + "※割引チケットの送付につきましては応募から発送まで2" + strString + "4週間程度のお時間を\r\n";
                text = text + "頂いております。\r\n";
                text = text + "※割引チケットは弊社「株式会社アルメックス」の社名が印刷された無地タイプ\r\n";
                text = text + "の封筒にて発送させていただきます。\r\n";
                text = text + "\r\n";
                /* 追加点▲ */
                text = text + "ご注意\r\n";
                text = text + "■本メールにお心当たりのない場合は、本メールの破棄をお願いいたします。" + "\r\n";
                text = text + "\r\n";
                text = text + "お問い合わせ" + "\r\n";
                text = text + "mailto:info@happyhotel.jp?subject=" + encdata + "\r\n";
                text = text + "\r\n";
                text = text + "ハッピー・ホテルはUSEN-NEXTグループの株式会社アルメックスが運営する" + "\r\n";
                text = text + "レジャーホテル検索サイトです。" + "\r\n";
                text = text + "\r\n";
                text = text + "ハッピー・ホテルURL" + "\r\n";
                text = text + Url.getUrl() + "\r\n";

                // メール送信を行う
                SendMail.send( "info@happyhotel.jp", mailAddr, title_mail, text );
            }
        }
        Logging.info( "エラーメッセージ：" + strErr );
        request.setAttribute( "err", strErr );

        // デバッグ環境かどうか
        if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
        {
            strCarrierUrl = "/_debug_" + strCarrierUrl;
        }
        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + strCarrierUrl + "?" + paramUidLink );
        Logging.info( "取得するURL：" + request.getContextPath() + strCarrierUrl + "?" + paramUidLink );
        Logging.info( "[ue.registUserElect] 排他ロック解除" );
        try
        {
            requestDispatcher.forward( request, response );
        }
        catch ( Exception e )
        {
            Logging.error( "[UserElectRegistration() ] Exception=" + e.toString() );
        }

    }
} // End Of Class

