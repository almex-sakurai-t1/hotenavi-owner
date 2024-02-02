package jp.happyhotel.action;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataApUuid;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlMenu;
import jp.happyhotel.others.GenerateXmlMenuSpecial;
import jp.happyhotel.others.GenerateXmlMenuSub;
import jp.happyhotel.others.GenerateXmlMenuTop;
import jp.happyhotel.others.GenerateXmlReviewUrl;
import jp.happyhotel.others.XmlMenu;
import jp.happyhotel.user.UserLoginInfo;

/**
 * メニュークラス（API）
 * 
 * @author S.Tashiro
 * @version 1.0 2012/07/20
 * 
 */

public class ActionApiMenu extends BaseAction
{
    public String        TOP_MENU                  = "TopMenu";
    public String        SPECIAL_MENU              = "SpecialMenu";
    public int           IMG_ICON                  = 1;
    public int           IMG_BANNER                = 2;

    // /etc/happpyhotel/common.confのパラメータ取得処理
    private final String CONF_PATH                 = "/etc/happyhotel/common.conf";
    private final String REVIEW_APPSTORE_URL_KEY   = "review.appStore";
    private final String REVIEW_GOOGLEPLAY_URL_KEY = "review.googlePlay";
    private String       reviewAppStoreUrl         = "";
    private String       reviewGooglePlayUrl       = "";

    /**
     * メニュー情報（API）
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        Logging.info( "ActionApiMenu.execute start" );

        final int DISP_COUNT = 99;
        int kind = 0;
        String strMenu = "";
        String paramMethod = "";
        String paramUserId = "";
        String paramUuid = "";
        UserLoginInfo uli;

        // XML出力
        boolean ret = false;
        XmlMenu xmlMenu = new XmlMenu();
        GenerateXmlHeader header = new GenerateXmlHeader();
        GenerateXmlMenu menu = new GenerateXmlMenu();
        GenerateXmlMenuTop gmTop = new GenerateXmlMenuTop();
        GenerateXmlMenuSpecial gmSpecial = new GenerateXmlMenuSpecial();
        GenerateXmlReviewUrl reviewUrl = new GenerateXmlReviewUrl();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );
            paramUuid = request.getParameter( "uuid" );

            Logging.info( "User-Agent=" + UserAgent.getUserAgent( request ) );
            Logging.info( "ActionApiMenu.execute uuid=" + paramUuid );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( uli.getUserInfo() != null )
            {
                paramUserId = uli.getUserInfo().getUserId();
            }

            Logging.info( "ActionApiMenu.execute user_id=" + paramUserId );

            if ( paramMethod.equals( TOP_MENU ) != false )
            {
                kind = 1;
                strMenu = "TOPメニュー";
            }
            if ( paramMethod.equals( SPECIAL_MENU ) != false )
            {
                kind = 2;
                strMenu = "特集メニュー";
            }

            int noDispCount = 0;
            ret = xmlMenu.getMenu( kind, DISP_COUNT );
            if ( ret != false )
            {
                if ( request.getHeader( "user-agent" ).indexOf( "HappyHotel" ) != -1 )
                {
                    // バージョン3.1.0よりハピホテタッチに対応
                    int[] touchVersion = { 3, 1, 0 };
                    if ( UserAgent.IsLargerAppVersion( request, touchVersion ) )
                    {
                        gmTop.setTouchDisp( 1 );
                    }
                    else
                    {
                        gmTop.setTouchDisp( 0 );
                    }
                }
                for( int i = 0 ; i < xmlMenu.getCount() ; i++ )
                {
                    GenerateXmlMenuSub gmSub = new GenerateXmlMenuSub();
                    // disp_no が 100未満のものは必ず表示
                    if ( xmlMenu.getMenuDataInfo()[i].getDispNo() < 100 )
                    {
                        if ( xmlMenu.getMenuDataInfo()[i].getBannerUrl().equals( "" ) == false )
                        {
                            gmSub.setKind( IMG_BANNER );
                            gmSub.setImg( xmlMenu.getMenuDataInfo()[i].getBannerUrl() );
                        }
                        else
                        {
                            gmSub.setKind( IMG_ICON );
                            gmSub.setImg( xmlMenu.getMenuDataInfo()[i].getIconUrl() );
                        }

                        gmSub.setTitle( ReplaceString.replaceApiSpecial( xmlMenu.getMenuDataInfo()[i].getText() ) );
                        gmSub.setUrl( xmlMenu.getMenuDataInfo()[i].getTextUrl() );

                        gmTop.setMenuSub( gmSub );
                        gmSpecial.setMenuSub( gmSub );
                    }
                    else
                    {
                        String userAgentType = UserAgent.getUserAgentTypeString( request );

                        // iOS
                        if ( userAgentType.equals( "ipa" ) )
                        {
                            String appVersion = UserAgent.getAppVersion( request );
                            Logging.info( "appVersion=" + appVersion );
                            // バージョンあり
                            if ( !appVersion.equals( "" ) && request.getHeader( "user-agent" ).indexOf( "HappyHotel" ) != -1 )
                            {
                                // バージョン3.0.0よりアプリ内課金に対応
                                int[] purchaseVersion = { 3, 0, 0 };
                                if ( UserAgent.IsLargerAppVersion( request, purchaseVersion ) )
                                {
                                    if ( paramUserId != null && !(paramUserId.equals( "" )) )
                                    {
                                        DataUserBasic userbasic = uli.getUserInfo();
                                        int pay = userbasic.getRegistStatusPay();

                                        if ( pay != 9 )
                                        {
                                            if ( paramUuid != null )
                                            {
                                                DataApUuid uid = new DataApUuid();
                                                uid.getData( paramUuid );
                                                if ( uid.getRegistStatusPay() == 2 )
                                                {
                                                    pay = 9;
                                                }
                                            }
                                        }

                                        String menuUrl = "";

                                        if ( pay == 9 )
                                        {
                                            menuUrl = xmlMenu.getMenuDataInfo()[i].getTextUrl();
                                        }
                                        else
                                        {
                                            menuUrl = "premium";
                                        }

                                        if ( xmlMenu.getMenuDataInfo()[i].getBannerUrl().equals( "" ) == false )
                                        {
                                            gmSub.setKind( IMG_BANNER );
                                            gmSub.setImg( xmlMenu.getMenuDataInfo()[i].getBannerUrl() );
                                        }
                                        else
                                        {
                                            gmSub.setKind( IMG_ICON );
                                            gmSub.setImg( xmlMenu.getMenuDataInfo()[i].getIconUrl() );
                                        }
                                        gmSub.setTitle( xmlMenu.getMenuDataInfo()[i].getText() );
                                        gmSub.setUrl( menuUrl );

                                        gmTop.setMenuSub( gmSub );
                                        gmSpecial.setMenuSub( gmSub );
                                    }
                                    else
                                    {
                                        noDispCount++;
                                    }
                                }
                                else
                                {
                                    noDispCount++;
                                }
                            }
                            else
                            {
                                noDispCount++;
                            }
                        }
                        // Android
                        else
                        {
                            if ( xmlMenu.getMenuDataInfo()[i].getBannerUrl().equals( "" ) == false )
                            {
                                gmSub.setKind( IMG_BANNER );
                                gmSub.setImg( xmlMenu.getMenuDataInfo()[i].getBannerUrl() );
                            }
                            else
                            {
                                gmSub.setKind( IMG_ICON );
                                gmSub.setImg( xmlMenu.getMenuDataInfo()[i].getIconUrl() );
                            }
                            gmSub.setTitle( xmlMenu.getMenuDataInfo()[i].getText() );
                            gmSub.setUrl( xmlMenu.getMenuDataInfo()[i].getTextUrl() );

                            gmTop.setMenuSub( gmSub );
                            gmSpecial.setMenuSub( gmSub );
                        }
                    }
                }
                gmTop.setCount( xmlMenu.getCount() - noDispCount );
                gmSpecial.setCount( xmlMenu.getCount() - noDispCount );

                // 検索結果ヘッダ作成
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( strMenu );
                header.setCount( xmlMenu.getCount() - noDispCount );

                // クチコミ詳細を追加
                if ( kind == 1 )
                {
                    menu.addMenuTop( gmTop );

                    // AppleStore・GooglePlayレビューURLを追加（#15076対応）
                    readConfFile();
                    reviewUrl.setAppStore( reviewAppStoreUrl );
                    reviewUrl.setGooglePlay( reviewGooglePlayUrl );
                    header.setReviewUrl( reviewUrl );
                }
                else if ( kind == 2 )
                {
                    menu.addMenuSpecial( gmSpecial );
                }
                header.setMenu( menu );
            }
            else
            {
                menu.setError( Constants.ERROR_MSG_API11 );
                menu.setErrorCode( Constants.ERROR_CODE_API11 );

                // 検索結果ヘッダ作成
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( strMenu );
                header.setCount( xmlMenu.getCount() );
                // クチコミ詳細を追加
                if ( kind == 1 )
                {
                    menu.addMenuTop( gmTop );

                    // AppleStore・GooglePlayレビューURLを追加（#15076対応）
                    readConfFile();
                    reviewUrl.setAppStore( reviewAppStoreUrl );
                    reviewUrl.setGooglePlay( reviewGooglePlayUrl );
                    header.setReviewUrl( reviewUrl );
                }
                else if ( kind == 2 )
                {
                    menu.addMenuSpecial( gmSpecial );
                }
                header.setMenu( menu );
            }

            // 出力をヘッダーから
            String xmlOut = header.createXml();
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );
            Logging.info( xmlOut );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiMenu ]Exception:" + exception.toString() );

            // エラーを出力
            menu.setError( Constants.ERROR_MSG_API10 );
            menu.setErrorCode( Constants.ERROR_CODE_API10 );

            // 検索結果ヘッダ作成
            header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            header.setMethod( paramMethod );
            header.setName( "メニュー" );
            header.setCount( 0 );
            header.setMenu( menu );

            String xmlOut = header.createXml();
            ServletOutputStream out = null;

            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiMenu response]Exception:" + e.toString() );
            }
        }
        finally
        {
            Logging.info( "ActionApiMenu.execute end" );
        }
    }

    /**
     * /etc/happpyhotel/common.confのパラメータ取得処理
     * 
     * @throws Exception
     */
    private void readConfFile() throws Exception
    {
        FileInputStream conffile = null;
        Properties prop = null;

        try
        {
            // confファイルからキーと値のリストを読み込む
            conffile = new FileInputStream( CONF_PATH );
            prop = new Properties();
            prop.load( conffile );

            // 設定値の取得
            reviewAppStoreUrl = prop.getProperty( REVIEW_APPSTORE_URL_KEY );
            reviewGooglePlayUrl = prop.getProperty( REVIEW_GOOGLEPLAY_URL_KEY );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionApiMenu.readConfFile() ]Exception:" + e.toString() );
            throw e;
        }
        finally
        {
            // confファイルのクローズ
            prop = null;
            conffile.close();
        }
    }
}
