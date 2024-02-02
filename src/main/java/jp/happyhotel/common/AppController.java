package jp.happyhotel.common;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserCreditInfo;
import jp.happyhotel.user.UserLogin;
import jp.happyhotel.user.UserPoint;
import jp.happyhotel.user.UserPointPay;
import jp.happyhotel.user.UserTermInfo;

/**
 * アクション制御クラス
 * 
 * @author HCL Technologies Ltd.
 */
public class AppController extends HttpServlet
{

    private static final long serialVersionUID = 2204061181577868372L;

    ConfigClass               config           = null;

    public AppController()
    {
        super();
    }

    public void destroy()
    {
        super.destroy();
        // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * GET要求
     * 
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String uri = request.getRequestURI();
        try
        {
            try
            {
                int pathStart = uri.lastIndexOf( "/" );
                int pathEnd = uri.lastIndexOf( "." );
                String actionpath = uri.substring( pathStart + 1, pathEnd );
                ActionClass actionClass = config.getAction( actionpath );
                String className = actionClass.getActionClass();
                Class cls = Class.forName( className );
                Constructor ct = cls.getConstructor();
                Object retobj = ct.newInstance();
                BaseAction baseAction = (BaseAction)retobj;
                readCookiesInfo( request );
                response.setContentType( "text/html" );
                Logging.info( "[AppController.doGet() ] ,URI=" + uri + ",param=" + request.getQueryString() );
                baseAction.execute( request, response );
            }
            catch ( Exception e )
            {
                Logging.error( "[AppController.doGet() ] Exception=" + e.toString() + ",URI=" + uri + ",user-agent=" + request.getHeader( "user-agent" ) );
            }
        }
        catch ( Throwable t )
        {

            Logging.error( "[AppController.doGet() ] Exception=" + t.toString() );
        }

    }

    /**
     * POST要求
     * 
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doGet( request, response );
    }

    /**
     * POST要求
     * 
     * 
     * @param request リクエスト
     */
    private void readCookiesInfo(HttpServletRequest req)
    {
        // 変数名は別ページとかぶらないようにすること。
        final String CHECK_URL = "ssl.happyhotel.jp";
        // final String CHECK_URL = "10.120.8.70";
        // final String CHECK_URL = "121.101.88.177";
        int loop;
        int carrierFlag;
        int gpsFlag = 0;
        boolean resultFlag;
        Cookie[] cookies;
        Cookie hhCookie;
        UserTermInfo userinfoUti;
        UserPoint loginUserPoint;
        UserPointPay loginUserPointPay;
        UserLogin loginUser;
        UserBasicInfo userBasicInfo;
        UserCreditInfo loginUserCredit;
        DataLoginInfo_M2 dataLoginInfo;
        String loginUserId;
        String uidParam = "";
        String uidLink = "";

        hhCookie = null;
        int type = UserAgent.getUserAgentType( req );

        // cookieの確認
        cookies = req.getCookies();
        if ( cookies != null )
        {
            for( loop = 0 ; loop < cookies.length ; loop++ )
            {
                if ( cookies[loop].getName().compareTo( "u" ) == 0 )
                {
                    hhCookie = cookies[loop];
                    break;
                }
            }
        }

        userBasicInfo = null;
        loginUser = null;
        loginUserCredit = null;

        if ( type == UserAgent.USERAGENT_DOCOMO || type == UserAgent.USERAGENT_VODAFONE || type == UserAgent.USERAGENT_AU )
        {
            userBasicInfo = new UserBasicInfo();
            loginUser = new UserLogin();
            loginUserPoint = new UserPoint();
            loginUserPointPay = new UserPointPay();
            loginUserCredit = new UserCreditInfo();

            if ( type == UserAgent.USERAGENT_AU )
            {
                uidParam = req.getHeader( "x-up-subno" );
                uidLink = "";
            }
            else if ( type == UserAgent.USERAGENT_VODAFONE )
            {
                if ( req.getServerPort() == 80 || req.getServerPort() == 8080 || req.getServerPort() == 10080 )
                {
                    if ( req.getRequestURL().indexOf( CHECK_URL ) != -1 )
                    {
                        // メールアドレスハッシュ値の取得
                        uidParam = req.getParameter( "yuid" );
                    }
                    else
                    {
                        uidParam = req.getHeader( "x-jphone-uid" );
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
                    uidParam = req.getParameter( "yuid" );
                }
                uidLink = "uid=1&sid=EIJC&pid=P0P3";
            }
            else
            {
                uidParam = req.getParameter( "uid" );
                uidLink = "uid=NULLGWDOCOMO";
            }

            req.setAttribute( "UID-LINK", uidLink );
            userinfoUti = new UserTermInfo();

            if ( userinfoUti.getTermInfo( req ) )
            {

                carrierFlag = userinfoUti.getTerm().getCarrierFlag();
                gpsFlag = userinfoUti.getTerm().getGpsFlag();
            }
            else
            {
                carrierFlag = 3;
            }

            if ( uidParam != null )
            {
                if ( (req.getServerPort() != 80 && req.getServerPort() != 8080 && req.getServerPort() != 10080) && (type == UserAgent.USERAGENT_DOCOMO || type == UserAgent.USERAGENT_VODAFONE) )
                {
                    resultFlag = loginUser.getUserBasicByMd5( uidParam );
                }
                else
                {
                    // ドコモかソフトバンクで、URLがssl.happyhotel.jpだったらSSL環境扱いに
                    if ( (req.getRequestURL().indexOf( CHECK_URL ) != -1) && (type == UserAgent.USERAGENT_DOCOMO || type == UserAgent.USERAGENT_VODAFONE) )
                    {
                        resultFlag = loginUser.getUserBasicByMd5( uidParam );
                    }
                    else
                    {

                        resultFlag = loginUser.getUserBasicByTermNo( uidParam );
                    }
                }

                if ( resultFlag != false )
                {
                    dataLoginInfo = new DataLoginInfo_M2();

                    loginUserId = loginUser.getUserInfo().getUserId();
                    dataLoginInfo.setUserId( loginUser.getUserInfo().getUserId() );
                    dataLoginInfo.setUserName( loginUser.getUserInfo().getHandleName() );
                    dataLoginInfo.setUserPoint( loginUserPoint.getNowPoint( loginUserId, false ) );
                    dataLoginInfo.setRegistStatus( loginUser.getUserInfo().getRegistStatus() );
                    dataLoginInfo.setDelFlag( loginUser.getUserInfo().getDelFlag() );
                    dataLoginInfo.setCarrierFlag( carrierFlag );
                    dataLoginInfo.setGpsFlag( gpsFlag );
                    dataLoginInfo.setMemberFlag( true );
                    dataLoginInfo.setMailAddr( loginUser.getUserInfo().getMailAddr() );
                    dataLoginInfo.setMailAddrMobile( loginUser.getUserInfo().getMailAddrMobile() );
                    dataLoginInfo.setMobileTermNo( loginUser.getUserInfo().getMobileTermNo() );

                    // 有料会員情報
                    dataLoginInfo.setRegistStatusPay( loginUser.getUserInfo().getRegistStatusPay() );
                    dataLoginInfo.setRegistStatusOld( loginUser.getUserInfo().getRegistStatusOld() );
                    dataLoginInfo.setAccessTicket( loginUser.getUserInfo().getAccessTicket() );
                    dataLoginInfo.setUserPointPay( loginUserPointPay.getNowPoint( loginUser.getUserInfo().getUserId(), false ) );
                    if ( loginUser.getUserInfo().getRegistStatusPay() == 9 )
                    {
                        dataLoginInfo.setPaymemberFlag( true );
                        dataLoginInfo.setPaymemberTempFlag( false );
                    }
                    else
                    {
                        dataLoginInfo.setPaymemberFlag( false );
                        // 有料仮登録状態かどうか
                        if ( loginUser.getUserInfo().getRegistStatusPay() == 1 )
                        {
                            dataLoginInfo.setPaymemberTempFlag( true );
                        }
                        else
                        {
                            dataLoginInfo.setPaymemberTempFlag( false );
                        }
                    }

                    // カード情報を取得
                    dataLoginInfo.setCardmemberFlag( loginUserCredit.getPayMemberFlag( loginUser.getUserInfo().getUserId() ) );
                    dataLoginInfo.setCardmemberNgFlag( loginUserCredit.getNgMemberFlag( loginUser.getUserInfo().getUserId() ) );

                    if ( req.getServerPort() != 80 )
                    {
                        if ( type == UserAgent.USERAGENT_DOCOMO )
                        {
                            uidLink = "uid=" + loginUser.getUserInfo().getMailAddrMobileMd5();
                            req.setAttribute( "UID-LINK", uidLink );
                        }
                        else if ( type == UserAgent.USERAGENT_SOFTBANK )
                        {
                            uidLink = "yuid=" + loginUser.getUserInfo().getMailAddrMobileMd5();
                            req.setAttribute( "UID-LINK", uidLink );
                        }
                    }
                    else if ( req.getRequestURL().indexOf( CHECK_URL ) != -1 )
                    {
                        if ( type == UserAgent.USERAGENT_DOCOMO )
                        {
                            uidLink = "uid=" + loginUser.getUserInfo().getMailAddrMobileMd5();
                            req.setAttribute( "UID-LINK", uidLink );
                        }
                        else if ( type == UserAgent.USERAGENT_SOFTBANK )
                        {
                            uidLink = "yuid=" + loginUser.getUserInfo().getMailAddrMobileMd5();
                            req.setAttribute( "UID-LINK", uidLink );
                        }
                    }
                    req.setAttribute( "LOGIN_INFO", dataLoginInfo );
                }
                // データがない場合、SSL環境だったらメールアドレスのハッシュ値をセット
                else
                {
                    if ( req.getServerPort() != 80 )
                    {
                        if ( type == UserAgent.USERAGENT_DOCOMO )
                        {
                            uidLink = "uid=" + uidParam;
                            req.setAttribute( "UID-LINK", uidLink );
                        }
                        else if ( type == UserAgent.USERAGENT_SOFTBANK )
                        {
                            uidLink = "yuid=" + uidParam;
                            req.setAttribute( "UID-LINK", uidLink );
                        }
                    }
                    else if ( req.getRequestURL().indexOf( CHECK_URL ) != -1 )
                    {
                        if ( type == UserAgent.USERAGENT_DOCOMO )
                        {
                            uidLink = "uid=" + uidParam;
                            req.setAttribute( "UID-LINK", uidLink );
                        }
                        else if ( type == UserAgent.USERAGENT_SOFTBANK )
                        {
                            uidLink = "yuid=" + uidParam;
                            req.setAttribute( "UID-LINK", uidLink );
                        }
                    }
                }
            }
        }

        if ( hhCookie != null )
        {
            loginUser = new UserLogin();
            loginUserPoint = new UserPoint();
            loginUserPointPay = new UserPointPay();
            loginUserCredit = new UserCreditInfo();

            resultFlag = loginUser.userLogin( hhCookie.getValue(), req );
            if ( resultFlag != false )
            {
                dataLoginInfo = new DataLoginInfo_M2();

                loginUserId = loginUser.getUserInfo().getUserId();

                dataLoginInfo.setUserId( loginUser.getUserInfo().getUserId() );
                dataLoginInfo.setUserName( loginUser.getUserInfo().getHandleName() );
                dataLoginInfo.setUserPoint( loginUserPoint.getNowPoint( loginUserId, false ) );
                dataLoginInfo.setRegistStatus( loginUser.getUserInfo().getRegistStatus() );
                dataLoginInfo.setMemberFlag( true );
                dataLoginInfo.setMailAddr( loginUser.getUserInfo().getMailAddr() );
                dataLoginInfo.setMailAddrMobile( loginUser.getUserInfo().getMailAddrMobile() );
                dataLoginInfo.setMobileTermNo( loginUser.getUserInfo().getMobileTermNo() );

                // スマートフォンだったら
                if ( type == UserAgent.USERAGENT_SMARTPHONE )
                {
                    dataLoginInfo.setCarrierFlag( UserAgent.USERAGENT_SMARTPHONE );
                    dataLoginInfo.setGpsFlag( gpsFlag );
                }

                // 有料会員情報
                dataLoginInfo.setRegistStatusPay( loginUser.getUserInfo().getRegistStatusPay() );
                dataLoginInfo.setRegistStatusOld( loginUser.getUserInfo().getRegistStatusOld() );
                dataLoginInfo.setUserPointPay( loginUserPointPay.getNowPoint( loginUser.getUserInfo().getUserId(), false ) );
                dataLoginInfo.setAccessTicket( loginUser.getUserInfo().getAccessTicket() );
                if ( loginUser.getUserInfo().getRegistStatusPay() == 9 )
                {
                    dataLoginInfo.setPaymemberFlag( true );
                    dataLoginInfo.setPaymemberTempFlag( false );
                }
                else
                {
                    dataLoginInfo.setPaymemberFlag( false );
                    // 有料仮登録状態かどうか
                    if ( loginUser.getUserInfo().getRegistStatusPay() == 1 )
                    {
                        dataLoginInfo.setPaymemberTempFlag( true );
                    }
                    else
                    {
                        dataLoginInfo.setPaymemberTempFlag( false );
                    }
                }
                // カード情報を取得
                dataLoginInfo.setCardmemberFlag( loginUserCredit.getPayMemberFlag( loginUser.getUserInfo().getUserId() ) );
                dataLoginInfo.setCardmemberNgFlag( loginUserCredit.getNgMemberFlag( loginUser.getUserInfo().getUserId() ) );

                req.setAttribute( "LOGIN_INFO", dataLoginInfo );
            }
        }
    }

}
