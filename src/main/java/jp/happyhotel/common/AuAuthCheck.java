/*
 * @(#)AuAuthCheck.java 1.00 2009/08/31 Copyright (C) ALMEX Inc. 2009 auアクセスチケットチェッククラス
 */
package jp.happyhotel.common;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.others.FindConstellation;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserPoint;
import jp.happyhotel.user.UserPointPay;
import jp.happyhotel.user.UserTermInfo;

/**
 * auアクセスチケットチェッククラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/08/31
 */
public class AuAuthCheck implements Serializable
{
    private static final long  serialVersionUID   = 814936032919042369L;
    public static final int    RS_PAY_TEMPMEMBER  = 1;                                                                 // 非会員が有料会員になる際にｷｬﾘｱからOKが帰って来た時のRegistStatusPayの値
    public static final int    RS_OLD_NOTMEMBER   = 8;                                                                 // 非会員が有料会員になる際のRegistStatusOldの値
    public static final int    RS_MEMBER          = 9;                                                                 // 正会員のRegistStatusの値
    public static final int    REGIST_POINT       = 1000001;                                                           // 有料入会ポイントのポイントコード
    public static final String AC_READ            = "acread=1";
    // public static final String DEFAULT_URL = "http://121.101.88.177";
    // public static final String DEFAULT_URL_AU = "http://121.101.88.177/au/";
    // public static final String DEFAULT_NG_URL = "http://121.101.88.177/au/free/mymenu/paymemberRegist.act?step=1";
    public static final String DEFAULT_URL        = Url.getUrl( false );
    public static final String DEFAULT_URL_AU     = Url.getUrl( false ) + "/au/";
    public static final String DEFAULT_NG_URL     = Url.getUrl( false ) + "/au/free/mymenu/paymemberRegist.act?step=1";
    public static final String DEFAULT_URL_SSL    = Url.getSslUrl();
    public static final String DEFAULT_URL_AU_SSL = Url.getSslUrl() + "/au/";
    public static final String AUTHCHECK_MEMO     = "アクセスチケットOKのため有料会員へ変更";

    public String              resultData;
    public UserBasicInfo       ubi;
    public DataLoginInfo_M2    dataLoginInfo;

    public void AuthCheck()
    {
        resultData = "";
    }

    /**
     * DataLoginInfo_M2クラス取得
     * 
     * @return DataLoginInfo_M2
     * @see "クラスでアクセスチケットをチェックする場合に使用する"
     */
    public DataLoginInfo_M2 getDataLoginInfo()
    {
        return dataLoginInfo;
    }

    /**
     * DataLoginInfo_M2クラスセット
     * 
     * @param dataLoginInfo DataLoginInfo_M2クラス
     * @see "クラスでアクセスチケットをチェックする場合に使用する"
     */
    public void setDataLoginInfo(DataLoginInfo_M2 dataLoginInfo)
    {
        this.dataLoginInfo = dataLoginInfo;
    }

    /**
     * 実行結果取得
     * 
     * @return 実行結果
     */
    public String getResultData()
    {
        return resultData;
    }

    /**
     * UserBasicInfoクラス取得
     * 
     * @return UserBasicInfo
     */
    public UserBasicInfo getUbi()
    {
        return ubi;
    }

    /**
     * UserBasicInfoクラスセット
     * 
     * @param ubi UserBaicInfoクラス
     */
    public void setUbi(UserBasicInfo ubi)
    {
        this.ubi = ubi;
    }

    /**
     * 実行結果セット
     * 
     * @param resultData 実行結果
     */
    public void setResultData(String resultData)
    {
        this.resultData = resultData;
    }

    /**
     * アクセスチケットの確認(JSPで確認する場合)
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param ngUrlFlag 失敗した場合のリダイレクト先(true:有料会員登録ページへ、false:今表示しているページ)
     * @return 処理結果(true:課金済でアクセスチケットも期限内<br>
     *         false:非課金または、課金済みでアクセスチケットがない、課金済みでアクセスチケットが期限切れ)
     * @see "ngUrlFlagは、非課金の場合に使用されるURLである。<br>
     *      プロセス実行結果がuidを含む文字列の場合true、Locationを含む文字列の場合falseとする"
     * 
     */
    public boolean authCheck(HttpServletRequest request, boolean ngUrlFlag)
    {
        Logging.info( "authCheck1" );
        StringBuffer strbuff = new StringBuffer();
        String paramAc = "";
        String okURL = DEFAULT_URL;
        String ngURL = DEFAULT_NG_URL;
        String paramURI;
        String paramQuery;
        boolean ret = false;
        Process ps;
        ProcessBuilder psbuild;
        UserPointPay upp;

        // 現在参照しているコンテンツ取得
        paramURI = request.getRequestURI();
        paramQuery = request.getQueryString();
        upp = new UserPointPay();
        if ( paramURI == null )
        {
            paramURI = "";
        }

        if ( paramQuery == null )
        {
            paramQuery = "";
        }
        else
        {
            paramQuery = paramQuery.replaceAll( "&amp;", "&" );
        }

        // 現在の位置をポート番号で判別
        if ( request.getServerPort() == 80 || request.getServerPort() == 8080 || request.getServerPort() == 10080 )
        {
            okURL = DEFAULT_URL;
            ngURL = DEFAULT_URL;
        }
        else
        {
            okURL = DEFAULT_URL_SSL;
            ngURL = DEFAULT_URL_SSL;
        }

        // nuURLFlagがfalseの場合は現在のURLにする
        if ( ngUrlFlag == false )
        {
            if ( paramQuery.compareTo( "" ) == 0 )
            {
                ngURL += paramURI + "?" + AC_READ;
            }
            else
            {
                ngURL += paramURI + "?" + paramQuery + "&" + AC_READ;
            }
        }
        else
        {
            ngURL = DEFAULT_NG_URL + "?" + AC_READ;
        }

        paramAc = request.getParameter( "ac" );
        if ( paramAc == null )
        {
            if ( paramQuery.compareTo( "" ) != 0 )
            {
                paramURI += "?" + paramQuery;
            }
            Logging.info( "authCheck1_ACなし OKURL：" + okURL + paramURI );
            Logging.info( "authCheck1_ACなし NGURL：" + ngURL );

            psbuild = new ProcessBuilder( "/usr/local/bin/exec_authcheck.sh",
                    "tu=" + okURL + paramURI + "",
                    "nu=" + ngURL + "",
                    "lt=86400" );
        }
        else
        {
            if ( paramQuery.compareTo( "" ) != 0 )
            {
                paramQuery = paramQuery.replaceAll( "ac=" + paramAc, "" );
                paramURI += "?" + paramQuery;
            }
            Logging.info( "authCheck1_ACあり：" + okURL + paramURI );

            psbuild = new ProcessBuilder( "/usr/local/bin/exec_authcheck.sh",
                    "tu=" + okURL + paramURI + "",
                    "nu=" + ngURL + "",
                    "lt=86400" );
        }

        Map<String, String> env = psbuild.environment();
        env.clear();
        env.put( "REQUEST_METHOD", "GET" );

        if ( request.getQueryString() != null )
        {
            env.put( "QUERY_STRING", request.getQueryString() );
        }
        else
        {
            env.put( "QUERY_STRING", "" );
        }

        try
        {
            ps = psbuild.start();

            // プロセス実行結果
            InputStream instream = ps.getInputStream();
            if ( instream != null )
            {
                while( true )
                {
                    int readdata = instream.read();
                    if ( readdata == -1 )
                    {
                        break;
                    }
                    strbuff.append( String.format( "%c", readdata ) );
                }
                this.resultData = strbuff.toString();
                Logging.info( "authCheck1_result:" + this.resultData );

                // 戻り値がLocationで始まるとアクセスチケットなしまたは、アクセスチケットの期限切れ
                if ( this.resultData.indexOf( "Location" ) >= 0 )
                {
                    this.resultData = this.resultData.replaceAll( "Location: ", "" );
                }
                // 戻り値がuidで始まるとアクセスチケットがあり期限内である
                else if ( this.resultData.indexOf( "uid" ) >= 0 )
                {
                    // 端末番号を抜き出す
                    this.resultData = this.resultData.replaceAll( "uid=", "" );
                    if ( this.resultData.indexOf( "&" ) != -1 )
                    {
                        this.resultData = this.resultData.substring( 0, this.resultData.indexOf( "&" ) );
                    }

                    // 端末番号から未削除のユーザーを探す
                    this.ubi = new UserBasicInfo();
                    ret = this.ubi.getUserBasicByTermnoNoCheck( this.resultData );

                    // 会員情報がなかったら端末番号をuser_idにして登録
                    if ( ret == false )
                    {
                        // 端末番号で一時的に会員登録
                        this.ubi.getUserInfo().setUserId( this.resultData );
                        this.ubi.getUserInfo().setMobileTermNo( this.resultData );
                        this.ubi.getUserInfo().setRegistStatusPay( RS_PAY_TEMPMEMBER );
                        this.ubi.getUserInfo().setRegistStatusOld( RS_OLD_NOTMEMBER );
                        this.ubi.getUserInfo().setRegistDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        this.ubi.getUserInfo().setRegistTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        // mail_addr_mobile_Md5が登録されていない場合、端末番号のハッシュ値を登録する
                        if ( this.ubi.getUserInfo().getMailAddrMobileMd5() == null || this.ubi.getUserInfo().getMailAddrMobileMd5().compareTo( "" ) == 0 )
                        {
                            this.ubi.getUserInfo().setMailAddrMobileMd5( ConvertString.convert2md5( this.resultData ) );
                        }
                        ret = this.ubi.getUserInfo().insertData();
                    }
                    else
                    {
                        // regist_status_payが0未登録のときのみ更新する
                        if ( this.ubi.getUserInfo().getRegistStatusPay() == 0 )
                        {
                            this.ubi.getUserInfo().setRegistStatusOld( this.ubi.getUserInfo().getRegistStatus() );
                            this.ubi.getUserInfo().setRegistStatus( RS_MEMBER );
                            this.ubi.getUserInfo().setRegistStatusPay( RS_MEMBER );
                            this.ubi.getUserInfo().setRegistDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            this.ubi.getUserInfo().setRegistTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            // mail_addr_mobile_Md5が登録されていない場合は端末番号のハッシュ値を登録する
                            if ( this.ubi.getUserInfo().getMailAddrMobileMd5() == null || this.ubi.getUserInfo().getMailAddrMobileMd5().compareTo( "" ) == 0 )
                            {
                                // Logging.info( "[ActionPaymemberRegist.execute] ハッシュ値：" + ConvertString.convert2md5( termNo ) ); // test log
                                this.ubi.getUserInfo().setMailAddrMobileMd5( ConvertString.convert2md5( this.resultData ) );
                            }
                            if ( this.ubi.getUserInfo().getBirthdayMonth() > 0 && this.ubi.getUserInfo().getBirthdayDay() > 0 )
                            {
                                this.ubi.getUserInfo().setConstellation( FindConstellation.getConstellation( this.ubi.getUserInfo().getBirthdayMonth() * 100 + this.ubi.getUserInfo().getBirthdayDay() ) );
                            }
                            ret = this.ubi.getUserInfo().updateData( this.ubi.getUserInfo().getUserId(), AUTHCHECK_MEMO );
                            if ( ret != false )
                            {
                                // 成功したので、有料ポイントを付与
                                // upp.setRegistPoint( this.ubi.getUserInfo().getUserId(), REGIST_POINT, 0, "" );
                            }

                        }
                    }
                }
                else
                {
                    Logging.error( "authCheckFor1_result, no_return :" + this.resultData );
                }

            }
            else
            {
                Logging.error( "authCheck1 process :" + this.resultData );
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[AuAuthCheck.AuthCheck] Exceptioon:" + e.toString() );
        }
        return(ret);
    }

    /**
     * アクセスチケットの確認(クラスで確認する場合)
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param ngUrlFlag 失敗した場合のリダイレクト先(true:有料会員登録ページへ、false:今表示しているページ)
     * @return 処理結果(true:課金済でアクセスチケットも期限内<br>
     *         false:非課金または、課金済みでアクセスチケットがない、課金済みでアクセスチケットが期限切れ)
     * @see "ngUrlFlagは、非課金の場合に使用されるURLである。<br>
     *      プロセス実行結果がuidを含む文字列の場合true、Locationを含む文字列の場合falseとする<br>
     *      DataLoginInfo_M2のデータがセットされます。"
     */
    public boolean authCheckForClass(HttpServletRequest request, boolean ngUrlFlag)
    {

        Logging.info( "authCheckForClass1" );
        StringBuffer strbuff = new StringBuffer();
        String paramAc = "";
        String okURL = DEFAULT_URL;
        String ngURL = DEFAULT_NG_URL;
        String paramURI;
        String paramQuery;
        boolean ret = false;
        int gpsFlag = 0;
        int carrierFlag;
        Process ps;
        ProcessBuilder psbuild;
        UserTermInfo userinfoUti;
        UserPoint userPoint;
        UserPointPay upp;

        // 現在参照しているコンテンツ取得
        paramURI = request.getRequestURI();
        paramQuery = request.getQueryString();
        upp = new UserPointPay();
        if ( paramURI == null )
        {
            paramURI = DEFAULT_NG_URL;
        }
        Logging.info( "paramURI:" + paramURI );

        if ( paramQuery == null )
        {
            paramQuery = "";
        }
        else
        {
            paramQuery = paramQuery.replaceAll( "&amp;", "&" );
        }
        Logging.info( "paramQuery:" + paramQuery );

        // 現在の位置をポート番号で判別
        if ( request.getServerPort() == 80 || request.getServerPort() == 8080 || request.getServerPort() == 10080 )
        {
            okURL = DEFAULT_URL;
            ngURL = DEFAULT_URL;
        }
        else
        {
            okURL = DEFAULT_URL_SSL;
            ngURL = DEFAULT_URL_SSL;
        }

        // nuURLFlagがfalseの場合は現在のURLにする
        if ( ngUrlFlag == false )
        {
            if ( paramQuery.compareTo( "" ) == 0 )
            {
                ngURL += paramURI + "?" + AC_READ;
            }
            else
            {
                ngURL += paramURI + "?" + paramQuery + "&" + AC_READ;
            }
        }
        else
        {
            ngURL = DEFAULT_NG_URL + "?" + AC_READ;
        }

        paramAc = request.getParameter( "ac" );
        if ( paramAc == null )
        {
            if ( paramQuery.compareTo( "" ) != 0 )
            {
                paramURI += "?" + paramQuery;
            }
            Logging.info( "authCheckForClass1_ACなし：" + DEFAULT_URL + paramURI );
            Logging.info( "authCheckForClass1_getQueryString：" + paramQuery );
            Logging.info( "authCheckForClass1_ngUrl：" + ngURL );
            Logging.info( "authCheckForClass1_exec_cmd：" + "/usr/local/bin/exec_authcheck.sh,tu=" + DEFAULT_URL + paramURI + ",nu=" + ngURL + ",lt=86400" );

            psbuild = new ProcessBuilder( "/usr/local/bin/exec_authcheck.sh",
                    "tu=" + okURL + paramURI + "",
                    "nu=" + ngURL + "",
                    "lt=86400" );
        }
        else
        {
            if ( paramQuery.compareTo( "" ) != 0 )
            {
                paramQuery = paramQuery.replaceAll( "ac=" + paramAc, "" );
                paramURI += "?" + paramQuery;
            }
            Logging.info( "authCheckForClass1_ACあり：" + paramURI );

            psbuild = new ProcessBuilder( "/usr/local/bin/exec_authcheck.sh",
                    "tu=" + okURL + paramURI + "",
                    "nu=" + ngURL + "",
                    "lt=86400" );
        }

        Map<String, String> env = psbuild.environment();
        env.clear();
        env.put( "REQUEST_METHOD", "GET" );

        if ( request.getQueryString() != null )
        {
            env.put( "QUERY_STRING", request.getQueryString() );
        }
        else
        {
            env.put( "QUERY_STRING", "" );
        }

        try
        {
            ps = psbuild.start();

            InputStream instream = ps.getInputStream();
            if ( instream != null )
            {
                while( true )
                {
                    int readdata = instream.read();
                    if ( readdata == -1 )
                    {
                        break;
                    }
                    strbuff.append( String.format( "%c", readdata ) );
                }
                this.resultData = strbuff.toString();
                Logging.info( "authCheckForClass1_result:" + this.resultData );

                // 戻り値がLocationで始まるとアクセスチケットなしまたは、アクセスチケットの期限切れ
                if ( this.resultData.indexOf( "Location" ) >= 0 )
                {

                    this.resultData = this.resultData.replaceAll( "Location: ", "" );
                }
                // 戻り値がuidで始まるとアクセスチケットがあり期限内である
                else if ( this.resultData.indexOf( "uid" ) >= 0 )
                {
                    // 端末番号を抜き出す
                    this.resultData = this.resultData.replaceAll( "uid=", "" );
                    if ( this.resultData.indexOf( "&" ) != -1 )
                    {
                        this.resultData = this.resultData.substring( 0, this.resultData.indexOf( "&" ) );
                    }

                    // 端末番号から未削除のユーザーを探す
                    this.ubi = new UserBasicInfo();
                    ret = this.ubi.getUserBasicByTermnoNoCheck( this.resultData );

                    // 会員情報がなかったら端末番号をuser_idにして登録
                    if ( ret == false )
                    {
                        // 端末番号で一時的に会員登録
                        this.ubi.getUserInfo().setUserId( this.resultData );
                        this.ubi.getUserInfo().setMobileTermNo( this.resultData );
                        this.ubi.getUserInfo().setRegistStatusPay( RS_PAY_TEMPMEMBER );
                        this.ubi.getUserInfo().setRegistStatusOld( RS_OLD_NOTMEMBER );
                        this.ubi.getUserInfo().setRegistDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        this.ubi.getUserInfo().setRegistTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        // mail_addr_mobile_Md5が登録されていない場合、端末番号のハッシュ値を登録する
                        if ( this.ubi.getUserInfo().getMailAddrMobileMd5() == null || this.ubi.getUserInfo().getMailAddrMobileMd5().compareTo( "" ) == 0 )
                        {
                            this.ubi.getUserInfo().setMailAddrMobileMd5( ConvertString.convert2md5( this.resultData ) );
                        }
                        ret = this.ubi.getUserInfo().insertData();
                    }
                    else
                    {
                        // regist_status_payが0未登録のときのみ更新する
                        if ( this.ubi.getUserInfo().getRegistStatusPay() == 0 )
                        {
                            this.ubi.getUserInfo().setRegistStatusOld( this.ubi.getUserInfo().getRegistStatus() );
                            this.ubi.getUserInfo().setRegistStatus( RS_MEMBER );
                            this.ubi.getUserInfo().setRegistStatusPay( RS_MEMBER );
                            this.ubi.getUserInfo().setRegistDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            this.ubi.getUserInfo().setRegistTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            // mail_addr_mobile_Md5が登録されていない場合は端末番号のハッシュ値を登録する
                            if ( this.ubi.getUserInfo().getMailAddrMobileMd5() == null || this.ubi.getUserInfo().getMailAddrMobileMd5().compareTo( "" ) == 0 )
                            {
                                // Logging.info( "[ActionPaymemberRegist.execute] ハッシュ値：" + ConvertString.convert2md5( termNo ) ); // test log
                                this.ubi.getUserInfo().setMailAddrMobileMd5( ConvertString.convert2md5( this.resultData ) );
                            }
                            if ( this.ubi.getUserInfo().getBirthdayMonth() > 0 && this.ubi.getUserInfo().getBirthdayDay() > 0 )
                            {
                                this.ubi.getUserInfo().setConstellation( FindConstellation.getConstellation( this.ubi.getUserInfo().getBirthdayMonth() * 100 + this.ubi.getUserInfo().getBirthdayDay() ) );
                            }
                            ret = this.ubi.getUserInfo().updateData( this.ubi.getUserInfo().getUserId(), AUTHCHECK_MEMO );
                            if ( ret != false )
                            {
                                // 成功したので、有料ポイントを付与
                                // upp.setRegistPoint( this.ubi.getUserInfo().getUserId(), REGIST_POINT, 0, "" );
                            }

                        }
                    }

                    // 更新または挿入されたDataLoginInfo_M2をセットする
                    if ( ret != false )
                    {
                        userPoint = new UserPoint();
                        userinfoUti = new UserTermInfo();

                        // 端末情報をチェック
                        if ( userinfoUti.getTermInfo( request ) )
                        {
                            carrierFlag = userinfoUti.getTerm().getCarrierFlag();
                            gpsFlag = userinfoUti.getTerm().getGpsFlag();
                        }
                        else
                        {
                            carrierFlag = 3;
                        }

                        this.dataLoginInfo = new DataLoginInfo_M2();
                        // 更新されたdataLoginInfo_M2をセット
                        this.dataLoginInfo.setUserId( this.ubi.getUserInfo().getUserId() );
                        this.dataLoginInfo.setUserName( this.ubi.getUserInfo().getHandleName() );
                        this.dataLoginInfo.setUserPoint( userPoint.getNowPoint( this.ubi.getUserInfo().getUserId(), false ) );
                        this.dataLoginInfo.setRegistStatus( this.ubi.getUserInfo().getRegistStatus() );
                        this.dataLoginInfo.setDelFlag( this.ubi.getUserInfo().getDelFlag() );
                        this.dataLoginInfo.setCarrierFlag( carrierFlag );
                        this.dataLoginInfo.setGpsFlag( gpsFlag );
                        this.dataLoginInfo.setMemberFlag( true );
                        this.dataLoginInfo.setMailAddr( this.ubi.getUserInfo().getMailAddr() );
                        this.dataLoginInfo.setMailAddrMobile( this.ubi.getUserInfo().getMailAddrMobile() );

                        // 有料会員情報
                        this.dataLoginInfo.setRegistStatusPay( this.ubi.getUserInfo().getRegistStatusPay() );
                        this.dataLoginInfo.setRegistStatusOld( this.ubi.getUserInfo().getRegistStatusOld() );
                        this.dataLoginInfo.setUserPointPay( upp.getNowPoint( this.ubi.getUserInfo().getUserId(), false ) );
                        this.dataLoginInfo.setAccessTicket( this.ubi.getUserInfo().getAccessTicket() );
                        if ( this.ubi.getUserInfo().getRegistStatusPay() == 9 )
                        {
                            this.dataLoginInfo.setPaymemberFlag( true );
                            this.dataLoginInfo.setPaymemberTempFlag( false );
                        }
                        else
                        {
                            this.dataLoginInfo.setPaymemberFlag( false );
                            // 有料仮登録状態かどうか
                            if ( this.ubi.getUserInfo().getRegistStatusPay() == 1 )
                            {
                                this.dataLoginInfo.setPaymemberTempFlag( true );
                            }
                            else
                            {
                                this.dataLoginInfo.setPaymemberTempFlag( false );
                            }
                        }
                    }
                }
                else
                {
                    Logging.error( "authCheckForClass1_result, no_return :" + this.resultData );
                }
            }
            else
            {
                Logging.error( "authCheckForClass1_process :" + this.resultData );
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[AuAuthCheck.authCheckForClass] Exceptioon:" + e.toString() );
        }
        return(ret);
    }

    /**
     * アクセスチケットの確認(JSPで確認する場合)
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param ngUrl 失敗した場合のリダイレクト先を指定(http://happyhotel.jp/au/以降を指定してください)
     * @return 処理結果(true:課金済でアクセスチケットも期限内<br>
     *         false:非課金または、課金済みでアクセスチケットがない、課金済みでアクセスチケットが期限切れ)
     * @see "ngUrlFlagは、非課金の場合に使用されるURLである。<br>
     *      プロセス実行結果がuidを含む文字列の場合true、Locationを含む文字列の場合falseとする"
     * 
     */
    public boolean authCheck(HttpServletRequest request, String ngUrl)
    {
        Logging.info( "authCheck2" );
        StringBuffer strbuff = new StringBuffer();
        String paramAc = "";
        String ngURL = DEFAULT_NG_URL;
        String paramURI;
        String paramQuery;
        boolean ret = false;
        Process ps;
        ProcessBuilder psbuild;
        UserPointPay upp;

        // 現在参照しているコンテンツ取得
        paramURI = request.getRequestURI();
        paramQuery = request.getQueryString();
        upp = new UserPointPay();
        if ( paramURI == null )
        {
            paramURI = "";
        }

        if ( paramQuery == null )
        {
            paramQuery = "";
        }
        else
        {
            paramQuery = paramQuery.replaceAll( "&amp;", "&" );
        }

        // nuURLFlagがfalseの場合は現在のURLにする
        if ( ngUrl.compareTo( "" ) != 0 )
        {
            if ( ngUrl.indexOf( "?" ) != -1 )
            {
                ngURL = ngUrl + "&" + AC_READ;
            }
            else
            {
                ngURL = ngUrl + "?" + AC_READ;
            }
        }
        else
        {
            ngURL = DEFAULT_URL_AU + "?" + AC_READ;
        }

        paramAc = request.getParameter( "ac" );
        if ( paramAc == null )
        {
            if ( paramQuery.compareTo( "" ) != 0 )
            {
                paramURI += "?" + paramQuery;
            }
            Logging.info( "authCheck2_ACなし OKURL：" + DEFAULT_URL + paramURI );
            Logging.info( "authCheck2_ACなし NGURL：" + ngURL );

            psbuild = new ProcessBuilder( "/usr/local/bin/exec_authcheck.sh",
                    "tu=" + DEFAULT_URL + paramURI + "",
                    "nu=" + ngURL + "",
                    "lt=86400" );
        }
        else
        {
            if ( paramQuery.compareTo( "" ) != 0 )
            {
                paramQuery = paramQuery.replaceAll( "ac=" + paramAc, "" );
                paramURI += "?" + paramQuery;
            }
            Logging.info( "authCheck2_ACあり：" + DEFAULT_URL + paramURI );

            psbuild = new ProcessBuilder( "/usr/local/bin/exec_authcheck.sh",
                    "tu=" + DEFAULT_URL + paramURI + "",
                    "nu=" + ngURL + "",
                    "lt=86400" );
        }

        Map<String, String> env = psbuild.environment();
        env.clear();
        env.put( "REQUEST_METHOD", "GET" );

        if ( request.getQueryString() != null )
        {
            env.put( "QUERY_STRING", request.getQueryString() );
        }
        else
        {
            env.put( "QUERY_STRING", "" );
        }

        try
        {
            ps = psbuild.start();

            // プロセス実行結果
            InputStream instream = ps.getInputStream();
            if ( instream != null )
            {
                while( true )
                {
                    int readdata = instream.read();
                    if ( readdata == -1 )
                    {
                        break;
                    }
                    strbuff.append( String.format( "%c", readdata ) );
                }
                this.resultData = strbuff.toString();
                Logging.info( "authCheck2_result:" + this.resultData );

                // 戻り値がLocationで始まるとアクセスチケットなしまたは、アクセスチケットの期限切れ
                if ( this.resultData.indexOf( "Location" ) >= 0 )
                {
                    this.resultData = this.resultData.replaceAll( "Location: ", "" );
                }
                // 戻り値がuidで始まるとアクセスチケットがあり期限内である
                else if ( this.resultData.indexOf( "uid" ) >= 0 )
                {
                    // 端末番号を抜き出す
                    this.resultData = this.resultData.replaceAll( "uid=", "" );
                    if ( this.resultData.indexOf( "&" ) != -1 )
                    {
                        this.resultData = this.resultData.substring( 0, this.resultData.indexOf( "&" ) );
                    }

                    // 端末番号から未削除のユーザーを探す
                    this.ubi = new UserBasicInfo();
                    ret = this.ubi.getUserBasicByTermnoNoCheck( this.resultData );

                    // 会員情報がなかったら端末番号をuser_idにして登録
                    if ( ret == false )
                    {
                        // 端末番号で一時的に会員登録
                        this.ubi.getUserInfo().setUserId( this.resultData );
                        this.ubi.getUserInfo().setMobileTermNo( this.resultData );
                        this.ubi.getUserInfo().setRegistStatusPay( RS_PAY_TEMPMEMBER );
                        this.ubi.getUserInfo().setRegistStatusOld( RS_OLD_NOTMEMBER );
                        this.ubi.getUserInfo().setRegistDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        this.ubi.getUserInfo().setRegistTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        // mail_addr_mobile_Md5が登録されていない場合、端末番号のハッシュ値を登録する
                        if ( this.ubi.getUserInfo().getMailAddrMobileMd5() == null || this.ubi.getUserInfo().getMailAddrMobileMd5().compareTo( "" ) == 0 )
                        {
                            this.ubi.getUserInfo().setMailAddrMobileMd5( ConvertString.convert2md5( this.resultData ) );
                        }
                        ret = this.ubi.getUserInfo().insertData();
                    }
                    else
                    {
                        // regist_status_payが0未登録のときのみ更新する
                        if ( this.ubi.getUserInfo().getRegistStatusPay() == 0 )
                        {
                            this.ubi.getUserInfo().setRegistStatusOld( this.ubi.getUserInfo().getRegistStatus() );
                            this.ubi.getUserInfo().setRegistStatus( RS_MEMBER );
                            this.ubi.getUserInfo().setRegistStatusPay( RS_MEMBER );
                            this.ubi.getUserInfo().setRegistDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            this.ubi.getUserInfo().setRegistTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            // mail_addr_mobile_Md5が登録されていない場合は端末番号のハッシュ値を登録する
                            if ( this.ubi.getUserInfo().getMailAddrMobileMd5() == null || this.ubi.getUserInfo().getMailAddrMobileMd5().compareTo( "" ) == 0 )
                            {
                                // Logging.info( "[ActionPaymemberRegist.execute] ハッシュ値：" + ConvertString.convert2md5( termNo ) ); // test log
                                this.ubi.getUserInfo().setMailAddrMobileMd5( ConvertString.convert2md5( this.resultData ) );
                            }
                            if ( this.ubi.getUserInfo().getBirthdayMonth() > 0 && this.ubi.getUserInfo().getBirthdayDay() > 0 )
                            {
                                this.ubi.getUserInfo().setConstellation( FindConstellation.getConstellation( this.ubi.getUserInfo().getBirthdayMonth() * 100 + this.ubi.getUserInfo().getBirthdayDay() ) );
                            }
                            ret = this.ubi.getUserInfo().updateData( this.ubi.getUserInfo().getUserId(), AUTHCHECK_MEMO );
                            if ( ret != false )
                            {
                                // 成功したので、有料ポイントを付与
                                // upp.setRegistPoint( this.ubi.getUserInfo().getUserId(), REGIST_POINT, 0, "" );
                            }

                        }
                    }
                }
                else
                {
                    // 戻り値が何もかえってこない場合のログ
                    Logging.error( "authCheck2_result, no_return :" + this.resultData );
                }
            }
            else
            {
                Logging.error( "authCheck2 process :" + this.resultData );
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[AuAuthCheck.AuthCheck] Exceptioon:" + e.toString() );
        }
        return(ret);
    }

    /**
     * アクセスチケットの確認(クラスで確認する場合)
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param ngUrl 失敗した場合のリダイレクト先を指定(http://happyhotel.jp/au/以降を指定してください)
     * @return 処理結果(true:課金済でアクセスチケットも期限内<br>
     *         false:非課金または、課金済みでアクセスチケットがない、課金済みでアクセスチケットが期限切れ)
     * @see "ngUrlFlagは、非課金の場合に使用されるURLである。<br>
     *      プロセス実行結果がuidを含む文字列の場合true、Locationを含む文字列の場合falseとする<br>
     *      DataLoginInfo_M2のデータがセットされます。"
     */
    public boolean authCheckForClass(HttpServletRequest request, String ngUrl)
    {
        Logging.info( "authCheckForClass2" );
        StringBuffer strbuff = new StringBuffer();
        String paramAc = "";
        String ngURL = DEFAULT_NG_URL;
        String paramURI;
        String paramQuery;
        boolean ret = false;
        int gpsFlag = 0;
        int carrierFlag;
        Process ps;
        ProcessBuilder psbuild;
        UserTermInfo userinfoUti;
        UserPoint userPoint;
        UserPointPay upp;

        // 現在参照しているコンテンツ取得
        paramURI = request.getRequestURI();
        paramQuery = request.getQueryString();
        upp = new UserPointPay();
        if ( paramURI == null )
        {
            paramURI = DEFAULT_NG_URL;
        }

        if ( paramQuery == null )
        {
            paramQuery = "";
        }
        else
        {
            paramQuery = paramQuery.replaceAll( "&amp;", "&" );
        }

        // nuURLFlagがfalseの場合は現在のURLにする
        if ( ngUrl.compareTo( "" ) != 0 )
        {
            if ( ngUrl.indexOf( "?" ) != -1 )
            {
                ngURL = DEFAULT_URL_AU + ngUrl + "&" + AC_READ;
            }
            else
            {
                ngURL = DEFAULT_URL_AU + ngUrl + "?" + AC_READ;
            }
        }
        else
        {
            ngURL = DEFAULT_URL_AU + "?" + AC_READ;
            Logging.info( "authCheckForClass2_defaultNgUrl:" + ngURL );
        }

        paramAc = request.getParameter( "ac" );
        if ( paramAc == null )
        {
            if ( paramQuery.compareTo( "" ) != 0 )
            {
                paramURI += "?" + paramQuery;
            }
            Logging.info( "authCheckForClass2_ACなし：" + DEFAULT_URL + paramURI + paramQuery );
            Logging.info( "authCheckForClass2_getQueryString：" + paramQuery );
            Logging.info( "authCheckForClass2_ngUrl：" + ngURL );
            Logging.info( "authCheckForClass2_exec_cmd：" + "/usr/local/bin/exec_authcheck.sh,tu=" + DEFAULT_URL + paramURI + ",nu=" + ngURL + ",lt=86400" );

            psbuild = new ProcessBuilder( "/usr/local/bin/exec_authcheck.sh",
                    "tu=" + DEFAULT_URL + paramURI + "",
                    "nu=" + ngURL + "",
                    "lt=86400" );
        }
        else
        {
            if ( paramQuery.compareTo( "" ) != 0 )
            {
                paramQuery = paramQuery.replaceAll( "ac=" + paramAc, "" );
                paramURI += "?" + paramQuery;
            }
            Logging.info( "authCheckForClass2_ACあり：" + DEFAULT_URL + paramURI + paramQuery );

            psbuild = new ProcessBuilder( "/usr/local/bin/exec_authcheck.sh",
                    "tu=" + DEFAULT_URL + paramURI + "",
                    "nu=" + ngURL + "",
                    "lt=86400" );
        }

        Map<String, String> env = psbuild.environment();
        env.clear();
        env.put( "REQUEST_METHOD", "GET" );

        if ( request.getQueryString() != null )
        {
            env.put( "QUERY_STRING", request.getQueryString() );
        }
        else
        {
            env.put( "QUERY_STRING", "" );
        }

        try
        {
            ps = psbuild.start();

            InputStream instream = ps.getInputStream();
            if ( instream != null )
            {
                while( true )
                {
                    int readdata = instream.read();
                    if ( readdata == -1 )
                    {
                        break;
                    }
                    strbuff.append( String.format( "%c", readdata ) );
                }
                this.resultData = strbuff.toString();
                Logging.info( "authCheckForClass2_result:" + this.resultData );

                // 戻り値がLocationで始まるとアクセスチケットなしまたは、アクセスチケットの期限切れ
                if ( this.resultData.indexOf( "Location" ) >= 0 )
                {
                    this.resultData = this.resultData.replaceAll( "Location: ", "" );
                }
                // 戻り値がuidで始まるとアクセスチケットがあり期限内である
                else if ( this.resultData.indexOf( "uid" ) >= 0 )
                {
                    // 端末番号を抜き出す
                    this.resultData = this.resultData.replaceAll( "uid=", "" );
                    if ( this.resultData.indexOf( "&" ) != -1 )
                    {
                        this.resultData = this.resultData.substring( 0, this.resultData.indexOf( "&" ) );
                    }

                    // 端末番号から未削除のユーザーを探す
                    this.ubi = new UserBasicInfo();
                    ret = this.ubi.getUserBasicByTermnoNoCheck( this.resultData );

                    // 会員情報がなかったら端末番号をuser_idにして登録
                    if ( ret == false )
                    {
                        // 端末番号で一時的に会員登録
                        this.ubi.getUserInfo().setUserId( this.resultData );
                        this.ubi.getUserInfo().setMobileTermNo( this.resultData );
                        this.ubi.getUserInfo().setRegistStatusPay( RS_PAY_TEMPMEMBER );
                        this.ubi.getUserInfo().setRegistStatusOld( RS_OLD_NOTMEMBER );
                        this.ubi.getUserInfo().setRegistDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        this.ubi.getUserInfo().setRegistTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        // mail_addr_mobile_Md5が登録されていない場合、端末番号のハッシュ値を登録する
                        if ( this.ubi.getUserInfo().getMailAddrMobileMd5() == null || this.ubi.getUserInfo().getMailAddrMobileMd5().compareTo( "" ) == 0 )
                        {
                            this.ubi.getUserInfo().setMailAddrMobileMd5( ConvertString.convert2md5( this.resultData ) );
                        }
                        ret = this.ubi.getUserInfo().insertData();
                    }
                    else
                    {
                        // regist_status_payが0未登録のときのみ更新する
                        if ( this.ubi.getUserInfo().getRegistStatusPay() == 0 )
                        {
                            this.ubi.getUserInfo().setRegistStatusOld( this.ubi.getUserInfo().getRegistStatus() );
                            this.ubi.getUserInfo().setRegistStatus( RS_MEMBER );
                            this.ubi.getUserInfo().setRegistStatusPay( RS_MEMBER );
                            this.ubi.getUserInfo().setRegistDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            this.ubi.getUserInfo().setRegistTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            // mail_addr_mobile_Md5が登録されていない場合は端末番号のハッシュ値を登録する
                            if ( this.ubi.getUserInfo().getMailAddrMobileMd5() == null || this.ubi.getUserInfo().getMailAddrMobileMd5().compareTo( "" ) == 0 )
                            {
                                // Logging.info( "[ActionPaymemberRegist.execute] ハッシュ値：" + ConvertString.convert2md5( termNo ) ); // test log
                                this.ubi.getUserInfo().setMailAddrMobileMd5( ConvertString.convert2md5( this.resultData ) );
                            }
                            if ( this.ubi.getUserInfo().getBirthdayMonth() > 0 && this.ubi.getUserInfo().getBirthdayDay() > 0 )
                            {
                                this.ubi.getUserInfo().setConstellation( FindConstellation.getConstellation( this.ubi.getUserInfo().getBirthdayMonth() * 100 + this.ubi.getUserInfo().getBirthdayDay() ) );
                            }
                            ret = this.ubi.getUserInfo().updateData( this.ubi.getUserInfo().getUserId(), AUTHCHECK_MEMO );
                            if ( ret = !false )
                            {
                                // 成功したので、有料ポイントを付与
                                // upp.setRegistPoint( this.ubi.getUserInfo().getUserId(), REGIST_POINT, 0, "" );

                            }

                        }
                    }

                    // 更新または挿入されたDataLoginInfo_M2をセットする
                    if ( ret != false )
                    {
                        userPoint = new UserPoint();
                        userinfoUti = new UserTermInfo();

                        // 端末情報をチェック
                        if ( userinfoUti.getTermInfo( request ) )
                        {
                            carrierFlag = userinfoUti.getTerm().getCarrierFlag();
                            gpsFlag = userinfoUti.getTerm().getGpsFlag();
                        }
                        else
                        {
                            carrierFlag = 3;
                        }

                        this.dataLoginInfo = new DataLoginInfo_M2();
                        // 更新されたdataLoginInfo_M2をセット
                        this.dataLoginInfo.setUserId( this.ubi.getUserInfo().getUserId() );
                        this.dataLoginInfo.setUserName( this.ubi.getUserInfo().getHandleName() );
                        this.dataLoginInfo.setUserPoint( userPoint.getNowPoint( this.ubi.getUserInfo().getUserId(), false ) );
                        this.dataLoginInfo.setRegistStatus( this.ubi.getUserInfo().getRegistStatus() );
                        this.dataLoginInfo.setDelFlag( this.ubi.getUserInfo().getDelFlag() );
                        this.dataLoginInfo.setCarrierFlag( carrierFlag );
                        this.dataLoginInfo.setGpsFlag( gpsFlag );
                        this.dataLoginInfo.setMemberFlag( true );
                        this.dataLoginInfo.setMailAddr( this.ubi.getUserInfo().getMailAddr() );
                        this.dataLoginInfo.setMailAddrMobile( this.ubi.getUserInfo().getMailAddrMobile() );

                        // 有料会員情報
                        this.dataLoginInfo.setRegistStatusPay( this.ubi.getUserInfo().getRegistStatusPay() );
                        this.dataLoginInfo.setRegistStatusOld( this.ubi.getUserInfo().getRegistStatusOld() );
                        this.dataLoginInfo.setUserPointPay( upp.getNowPoint( this.ubi.getUserInfo().getUserId(), false ) );
                        this.dataLoginInfo.setAccessTicket( this.ubi.getUserInfo().getAccessTicket() );
                        if ( this.ubi.getUserInfo().getRegistStatusPay() == 9 )
                        {
                            this.dataLoginInfo.setPaymemberFlag( true );
                            this.dataLoginInfo.setPaymemberTempFlag( false );
                        }
                        else
                        {
                            this.dataLoginInfo.setPaymemberFlag( false );
                            // 有料仮登録状態かどうか
                            if ( this.ubi.getUserInfo().getRegistStatusPay() == 1 )
                            {
                                this.dataLoginInfo.setPaymemberTempFlag( true );
                            }
                            else
                            {
                                this.dataLoginInfo.setPaymemberTempFlag( false );
                            }
                        }
                    }
                }
                else
                {
                    // 戻り値が何もかえってこない場合のログ
                    Logging.error( "authCheckForClass2_result, no_return :" + this.resultData );
                }

            }
            else
            {
                Logging.error( "authCheckForClass2 process :" + this.resultData );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[AuAuthCheck.authCheckForClass] Exceptioon:" + e.toString() );
        }
        return(ret);
    }

    /* アクセスチケットがNGの場合、有料会員情報を削除する */
    public boolean delUserBasic(HttpServletRequest request)
    {
        String uidParam;
        boolean ret;

        ret = false;
        uidParam = request.getHeader( "x-up-subno" );
        if ( uidParam != null )
        {
            if ( this.ubi == null )
            {
                this.ubi = new UserBasicInfo();
            }

            ret = this.ubi.getUserBasicByTermnoNoCheck( uidParam );
            if ( ret != false )
            {
                if ( this.ubi.getUserInfo().getRegistStatusPay() == 1 )
                {
                    this.ubi.getUserInfo().deleteData( this.ubi.getUserInfo().getUserId() );
                }
                else if ( this.ubi.getUserInfo().getRegistStatusPay() == 9 )
                {
                    this.ubi.getUserInfo().setDelDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    this.ubi.getUserInfo().setDelTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    // this.ubi.getUserInfo().setPointPay( 0 );
                    // this.ubi.getUserInfo().setPointPayUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    // 非会員から有料会員になった場合、del_flagを1にする。
                    if ( this.ubi.getUserInfo().getRegistStatusOld() == 8 )
                    {
                        this.ubi.getUserInfo().setDelFlag( 1 );
                        this.ubi.getUserInfo().setMailAddr( "" );
                        this.ubi.getUserInfo().setMailAddrMobile( "" );
                    }
                    // 無料会員から有料会員になった場合、有料会員になった時点のregist_statusに戻す
                    else
                    {
                        // regist_status_oldがとりえる値以外の場合は処理を行わない
                        if ( this.ubi.getUserInfo().getRegistStatusOld() == 2 || this.ubi.getUserInfo().getRegistStatusOld() == 3 || this.ubi.getUserInfo().getRegistStatusOld() == 9 )
                        {
                            this.ubi.getUserInfo().setRegistStatus( this.ubi.getUserInfo().getRegistStatusOld() );
                        }
                    }
                    this.ubi.getUserInfo().setRegistStatusPay( 0 );
                    ret = this.ubi.getUserInfo().updateData( this.ubi.getUserInfo().getUserId(), "アクセスチケットNGのため" );
                }

            }
        }
        return(ret);
    }
}
