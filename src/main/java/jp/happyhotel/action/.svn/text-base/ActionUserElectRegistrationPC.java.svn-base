package jp.happyhotel.action;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.SendMail;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterChain;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.data.DataMasterPresent;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataMasterZip;
import jp.happyhotel.data.DataUserElect;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserElect;
import jp.happyhotel.user.UserPoint;

/**
 * ポイント交換応募クラス
 *
 * @author S.Tashiro
 * @version 1.0 2010/09/10
 */

public class ActionUserElectRegistrationPC extends BaseAction implements Serializable
{
    private static final long        serialVersionUID  = 2691807714704675334L;
    private static RequestDispatcher requestDispatcher = null;

    /**
     * ポイントde交換　応募処理
     *
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        execUserElect( request, response );
    }

    /**
     * ポイントde交換　応募処理
     *
     * @param request リクエスト
     * @param response レスポンス
     */
    private static synchronized void execUserElect(HttpServletRequest request, HttpServletResponse response)
    {
        Logging.info( "[ActionUserElectRegistrationPC.execUserElect] 排他ロック開始(" + request.getSession().getId() + ")" );

        int nTotal;
        int nMinusPoint;
        int i;
        int carrierFlag;
        int maxRegistCount;
        final int MAX_REGIST = 1;
        final int MAX_REGIST_PAY = 3;
        final int ONCE = 1;
        boolean ret;
        boolean memberFlag;
        boolean paymemberFlag;
        boolean paymemberTempFlag;
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
        DataMasterPoint dmp;
        DataMasterPresent dmPresent;
        DataHotelBasic dhb;
        DataMasterChain dmChain;
        DataUserElect due;
        UserPoint up;
        UserElect ue;
        UserBasicInfo ubi;
        DataLoginInfo_M2 userinfoUbi;
        String strString;
        String sendRedirectURL;
        int loopCnt = 0;
        ret = false;

        dmp = new DataMasterPoint();
        due = new DataUserElect();
        up = new UserPoint();
        ue = new UserElect();
        dhb = new DataHotelBasic();
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
        paymemberFlag = false;
        paymemberTempFlag = false;
        strCarrierUrl = "";
        sendRedirectURL = "";

        strCarrierUrl = "/others/present_offer_hotel_write.jsp";
        userinfoUbi = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

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
        {
            strZipCode1 = "";
        }
        if ( strZipCode2 == null )
        {
            strZipCode2 = "";
        }
        strZipCode = strZipCode1 + strZipCode2;

        // ユーザー情報があるかどうか
        if ( userinfoUbi != null )
        {
            memberFlag = userinfoUbi.isMemberFlag();
            paymemberFlag = userinfoUbi.isPaymemberFlag();
            paymemberTempFlag = userinfoUbi.isPaymemberTempFlag();
        }
        else
        {
            memberFlag = false;
            paymemberFlag = false;
            paymemberTempFlag = false;
        }

        try
        {
            // ニックネーム・性別・メールアドレスのチェックを行う。
            // 有料または有料途中メンバーだったらメールアドレスが入っているかどうかを確認
            if ( paymemberFlag != false )
            {
                ubi = new UserBasicInfo();
                ret = ubi.getUserBasic( userinfoUbi.getUserId() );
                if ( ret != false )
                {
                    // 住所が登録されていなかったら、先に住所を登録させる。
                    if ( ubi.getUserInfo().getPrefCode() == 0 || ubi.getUserInfo().getJisCode() == 0 )
                    {
                        sendRedirectURL = "../mypage/mypage_edit.jsp";
                        response.sendRedirect( sendRedirectURL );
                        return;
                    }
                    // ニックネーム、性別、メールアドレスのいづれかがないユーザーはリダイレクトさせる
                    if ( ubi.getUserInfo().getHandleNameOnly().compareTo( "" ) == 0 || ubi.getUserInfo().getSex() == 2 ||
                            (ubi.getUserInfo().getMailAddr().compareTo( "" ) == 0 && ubi.getUserInfo().getMailAddrMobile().compareTo( "" ) == 0) )
                    {
                        sendRedirectURL = "../mypage/mypage_edit.jsp";
                        response.sendRedirect( sendRedirectURL );
                        return;
                    }
                }
            }
            else if ( paymemberTempFlag != false )
            {
                response.sendRedirect( sendRedirectURL );
                return;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionUserElectRegistrationPC.execUserElect() ] PayMemberCheck Exception=" + e.toString() );
        }

        // 選択したﾃﾞｰﾀを取得
        if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || (CheckString.numCheck( paramSeq ) == false) )
        {
            paramSeq = "0";
            strErr = strErr + "賞品が選択されていません。<br>";
        }
        if ( paramOpinion == null )
            paramOpinion = "";
        // if( CheckNgWord.ngWordCheck( paramOpinion ) != false ) strErr = strErr + "NGﾜｰﾄﾞが入っているため、登録できません<br>";

        if ( (strName == null) || (strName.compareTo( "" ) == 0) )
        {
            strName = "";
            strErr = strErr + "送付先ご氏名が入力されていません。<br>";
        }

        if ( (strPref == null) || (strPref.compareTo( "" ) == 0) )
        {
            strPref = "";
            strErr = strErr + "住所（都道府県）が入力されていません。<br>";
        }
        if ( (strZipCode.length() >= 1 && strZipCode.length() <= 6) || strZipCode.compareTo( "" ) == 0 )
        {
            strZipCode = "0";
            strErr = strErr + "郵便番号に不備があります。<br>";
        }
        else
        {
            if ( CheckString.numCheck( strZipCode ) == false )
            {
                strErr = strErr + "郵便番号に不備があります。<br>";
                strZipCode = "0";
            }
        }
        if ( (strAddress1 == null) || (strAddress1.compareTo( "" ) == 0) )
        {
            strAddress1 = "";
            strErr += "住所（市区町村）が入力されていません。<br>";
        }
        if ( (strAddress2 == null) || (strAddress2.compareTo( "" ) == 0) )
        {
            strAddress2 = "";
            strErr += "住所（上記以外）が入力されていません。<br>";
        }
        if ( (strTel == null) || (strTel.compareTo( "" ) == 0) )
        {
            strTel = "";
            strErr += "連絡先電話番号が入力されていません。<br>";
        }
        else
        {
            if ( CheckString.numCheck( strTel.replaceAll( "-", "" ) ) == false )
            {
                strErr = strErr + "電話番号に不備があります。<br>";
            }
        }
        if ( (strMemo == null) || (strMemo.compareTo( "" ) == 0) )
        {
            strMemo = "";
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
            strErr = strErr + "個人情報利用目的に同意されていません。<br>";
        }
        if ( paymemberFlag != false )
        {
            maxRegistCount = MAX_REGIST_PAY;
        }
        else
        {
            maxRegistCount = MAX_REGIST;
        }

        if ( (memberFlag != false) && (userinfoUbi.getRegistStatus() == 9) )
        {

            strUserId = userinfoUbi.getUserId();
            strHandleName = userinfoUbi.getUserName();
            strHandleName = ReplaceString.DBEscape( strHandleName );
            if ( userinfoUbi.getMailAddrMobile().compareTo( "" ) != 0 )
            {
                mailAddr = userinfoUbi.getMailAddrMobile();
            }
            else if ( userinfoUbi.getMailAddr().compareTo( "" ) != 0 )
            {
                mailAddr = userinfoUbi.getMailAddr();
            }

            // ﾎﾟｲﾝﾄ計算
            nTotal = userinfoUbi.getUserPoint();
            if ( nTotal < nMinusPoint )
            {
                strErr = strErr + "ポイントが足りません。<br>";
                ret = false;
            }
            else if ( nTotal == 0 )
            {
                strErr = strErr + "ポイントが足りません。<br>";
                ret = false;
            }

            // すでに応募済みのユーザーは応募させない
            ret = dmPresent.getData( Integer.parseInt( paramSeq ) );
            if ( ue.getUserElectByFirstCome( userinfoUbi.getUserId(), dmPresent.getLimitFrom(), dmPresent.getLimitTo() ) != false )
            {
                if ( ue.getCount() >= maxRegistCount )
                {
                    strErr = strErr + "すでに期間中のご応募可能枚数に達しておりますため、これ以上のご応募はできません。<br>";
                }
                // ホテルごとの応募データを取得
                if ( ue.getUserElectByOfferHotel( userinfoUbi.getUserId(), dmPresent.getOfferHotel(), dmPresent.getPrefId(), dmPresent.getLimitFrom(), dmPresent.getLimitTo() ) != false )
                {
                    // 1回でも応募している
                    if ( ue.getCount() > 0 )
                    {
                        // 全てのデータをチェックする。
                        for( i = 0 ; i < ue.getCount() ; i++ )
                        {
                            boolUserId = false;
                            boolSeq = false;

                            // UserIdと応募データの比較をログに出力。
                            Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp 取得データ比較 DB userId=" + ue.getUserElect()[i].getUserId() + "," +
                                    " パラメータ userId=" + userinfoUbi.getUserId() +
                                    ", DB seq=" + ue.getUserElect()[i].getSeq() + "," + " パラメータ seq=" + paramSeq +
                                    ", DB application_count=" + ue.getUserElect()[i].getApplicationCount() );

                            // UserIdを応募データと比較
                            if ( ue.getUserElect()[i].getUserId().compareTo( userinfoUbi.getUserId() ) == 0 )
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
                else
                {
                    Logging.info( "[ue.getUserElectByOfferHotel] ret = false" );
                }
            }
            else
            {
                Logging.info( "[ue.getUserElectByFirstCome] ret = false" );
            }
            // 受信した管理番号からﾌﾟﾚｾﾞﾝﾄﾏｽﾀを取得、減算ﾎﾟｲﾝﾄ数も求める
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
                    {
                        strErr = strErr + "ポイントが取得できませんでした。<br>";
                    }

                    if ( dmPresent.getTitle().compareTo( "" ) != 0 )
                    {
                        strTitle = dmPresent.getTitle();
                    }
                    else
                    {
                        strErr = strErr + "選択した賞品名が取得できませんでした。<br>";
                    }
                }
                else
                {
                    strErr = strErr + "有効期限が切れています。<br>";
                }

                // 残数の確認( 残数が0以下または、提供数よりも応募数が上回った場合エラーとする )
                if ( dmPresent.getRemainsNumber() <= 0 || dmPresent.getElectNumber() <= ue.getUserElectBySeq( Integer.parseInt( paramSeq ) ) )
                {
                    strErr = strErr + "選択された商品は残数がなくなりましたので、これ以上応募できません。<br>";
                }
            }
            else
            {
                strErr = strErr + "選択した賞品は存在しません。<br>";
            }

        }
        else
        {
            ret = false;
            strErr = strErr + "ポイント応募は会員限定です。<br>";
        }

        if ( strErr.compareTo( "" ) == 0 )
        {
            // 2つのTOMCATを制御するため、ファイルがある場合は処理を少し待つ
            // タイムアウト（500ms*10）になった時は処理を終了し、次回リクエストを受け付ける。
            File dupFile = new File( "/tmp/userelect.dat" );
            try
            {
                ret = dupFile.createNewFile();
            }
            catch ( IOException e1 )
            {
                e1.printStackTrace();
            }
            if ( ret == false )
            {
                while( dupFile.exists() != false )
                {
                    Logging.info( "[ue.registUserElect] userelect.datあり(" + request.getSession().getId() + ")" );
                    loopCnt++;
                    if ( loopCnt >= 10 )
                    {
                        strErr = strErr + "更新処理に失敗しました。再度応募をお願いします。";
                        Logging.info( "[ue.registUserElect] userelect.dat TimeOut(" + request.getSession().getId() + ")" );
                        break;
                    }
                    try
                    {
                        Thread.sleep( 500 );
                    }
                    catch ( InterruptedException e )
                    {
                        e.printStackTrace();
                    }
                }
                if ( loopCnt < 10 )
                {
                    // ファイルがなくなったんでファイル作成しループを抜ける
                    try
                    {
                        ret = dupFile.createNewFile();
                    }
                    catch ( IOException e1 )
                    {
                        e1.printStackTrace();
                    }
                }
            }

            if ( ret != false )
            {
                ret = dmPresent.getData( Integer.parseInt( paramSeq ) );
                // 残数の確認
                if ( dmPresent.getRemainsNumber() > 0 && dmPresent.getElectNumber() >= ue.getUserElectBySeq( Integer.parseInt( paramSeq ) ) )
                {
                    if ( ret != false )
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
                        due.setHandleName( strHandleName );
                        due.setMailAddr( userinfoUbi.getMailAddr() );
                        due.setInputDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        due.setInputTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        due.setStatusFlag( 3 );
                        ret = due.insertDataOnce();
                        Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp インサート結果：" + ret + " UserId=" + strUserId );
                        if ( ret != false )
                        {
                            ret = up.setPointOfferHotelPresent( strUserId, dmPresent.getPointCode(), dmPresent.getSeq() );
                            Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp ポイント削除結果：" + ret + " UserId=" + strUserId );
                        }
                        else
                        {
                        }
                    }

                    if ( ret != false )
                    {
                        Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp 残数=" + dmPresent.getRemainsNumber() );
                        // 残数を減らす処理
                        dmPresent.setRemainsNumber( dmPresent.getRemainsNumber() - 1 );
                        ret = dmPresent.updateData( dmPresent.getSeq() );
                        // UserIdと応募データの比較をログに出力。
                        Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp 残数を減らす処理：" + ret );
                    }
                }
                else
                {
                    strErr = strErr + "選択された商品は残数がなくなりましたので、これ以上応募できません。<br>";
                    ret = false;
                }

                if ( ret != false )
                {
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
                    text = text + "※割引チケットの送付につきましては応募から発送まで1週間" + strString + "10日程度のお時間を\r\n";
                    text = text + "頂いております。\r\n";
                    text = text + "※割引チケットは弊社「株式会社アルメックス」の社名が印刷された無地タイプ\r\n";
                    text = text + "の封筒にて発送させていただきます。\r\n";
                    text = text + "\r\n";
                    /* 追加点▲ */
                    text = text + "ご注意\r\n";
                    text = text + "■本メールにお心当たりのない場合は、本メールの破棄をお願いいたします。" + "\r\n";
                    text = text + "\r\n";
                    text = text + "お問い合わせ" + "\r\n";
                    text = text + "mailto:info@happyhotel.jp\r\n";
                    text = text + "\r\n";
                    text = text + "ハッピー・ホテルはUSEN-NEXTグループの株式会社アルメックスが運営する" + "\r\n";
                    text = text + "レジャーホテル検索サイトです。" + "\r\n";

                    // メール送信を行う
                    SendMail.send( "info@happyhotel.jp", mailAddr, title_mail, text );
                }
            }
            else
            {
                strErr = strErr + "更新処理に失敗しました。再度応募をお願いします。";
            }
            // 処理中ファイルを削除する
            dupFile.delete();
        }
        Logging.info( "エラーメッセージ：" + strErr );
        request.setAttribute( "err", strErr );

        switch( carrierFlag )
        {
            case DataMasterUseragent.CARRIER_ETC:
                break;
        }
        // デバッグ環境かどうか
        if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
        {
            strCarrierUrl = "/_debug_" + strCarrierUrl;
        }
        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + strCarrierUrl + "?" );
        Logging.info( "取得するURL：" + request.getContextPath() + strCarrierUrl + "?" );
        Logging.info( "[ue.registUserElect] 排他ロック解除(" + request.getSession().getId() + ")" );
        try
        {
            requestDispatcher.forward( request, response );
        }
        catch ( Exception e )
        {
            Logging.error( "[UserElectRegistration() ] Exception=" + e.toString() );
        }
    }
}
