package jp.happyhotel.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.others.GenerateXml1;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelFreeword_M2;

/**
 * 
 * 携帯版フリーワード検索クラス（flash使用）
 * 
 * @author N.Ide
 * @version 1.0 2009/06/22
 */

public class ActionFreewordSearchMobile_M2_Flash extends BaseAction
{

    static int                pageRecords       = Constants.pageLimitRecordMobile; // 1ﾍﾟｰｼﾞの表示件数
    private RequestDispatcher requestDispatcher = null;

    /**
     * フリーワード検索の結果一覧をFlashで表示する
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param response サーバからクライアントへのレスポンス
     * @see "../search_result_flash_error.jsp Flfastがエラーを出力した際に表示"
     * @see "../../index.jsp 予期しないエラーの場合に表示"
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int pageNum = 0;
        int type;
        int count, xmlCount, loopCount = 0;
        int[] arrHotelIdList = null;
        String paramFreeword = null;
        String paramPrefId = null;
        String paramUidLink = null;
        String paramPage = null;
        String paramAndword = null;
        String termNo = null;
        String paramFull = "";
        String userAgent = "";
        String errCode = "";
        final String actPath = "search/freeword/searchFreewordMobile.act";
        final String actPathFlash = "search/freeword/searchFreewordMobileFlash.act";
        InputStream in = null, xmlIn = null;
        OutputStream out = null, xmlOut = null;
        byte[] readBuff = new byte[1024];
        byte[] readBuffXml = new byte[1024];
        String XMLFilePass = "";
        SearchHotelCommon searchHotelCommon = null;
        SearchHotelFreeword_M2 searchHotelFreeword = null;
        DataLoginInfo_M2 dli;

        final String confFilePass = "/etc/happyhotel/test.conf"; // TODO
        FileInputStream propfile = null;
        Properties config;
        String baseDir = "/happyhotel/flash/flfast"; // TODO
        String flfastCmd = "/bin/flfast"; // TODO
        String flfastConfig = "/bin/flfast.conf"; // TODO

        String cmd;
        Runtime rt;
        Process pr;

        String paramAcRead;
        boolean ret;
        AuAuthCheck auCheck;
        int carrierFlag;

        dli = new DataLoginInfo_M2();

        try
        {
            /*
             * //TODO
             * propfile = new FileInputStream( confFilePass );
             * config = new Properties();
             * config.load(propfile);
             * if( config.getProperty("dir.base") != null )
             * {
             * baseDir = config.getProperty("dir.base");
             * }
             * if( config.getProperty("") != null )
             * {
             * flfastCmd = config.getProperty("dir.flfastconf");
             * }
             * flfastCmd = baseDir + flfastCmd;
             * if( config.getProperty("") != null )
             * {
             * flfastConfig = config.getProperty("dir.flfastconf");
             * }
             */

            dli = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            // auだったらアクセスチケットをチェックする
            carrierFlag = UserAgent.getUserAgentType( request );
            paramAcRead = request.getParameter( "acread" );
            if ( (paramAcRead == null) && (carrierFlag == DataMasterUseragent.CARRIER_AU) )
            {
                try
                {
                    auCheck = new AuAuthCheck();
                    ret = auCheck.authCheckForClass( request, "free/mymenu/premium_flash.jsp?" + paramUidLink );
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
                            dli = auCheck.getDataLoginInfo();
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[ActionFreewordSearchMobile_M2_Flash AuAuthCheck] Exception:" + e.toString() );
                }
            }
            // 有料会員ﾁｪｯｸ
            if ( dli != null )
            {
                // 有料会員登録途中のユーザーは有料会員登録ページへ
                if ( dli.getRegistStatusPay() == 1 )
                {
                    response.sendRedirect( "../../free/mymenu/paymemberRegist.act?" + paramUidLink );
                    return;
                }
                // flash機能説明ページへ
                else if ( dli.getRegistStatusPay() != 9 )
                {
                    response.sendRedirect( "../../free/mymenu/premium_flash.jsp?" + paramUidLink );
                    return;
                }
            }
            // 会員情報がない場合はflash機能説明ページへ
            else
            {
                response.sendRedirect( "../../free/mymenu/premium_flash.jsp?" + paramUidLink );
                return;
            }

            // request.setCharacterEncoding( "Windows-31J" );
            paramFreeword = request.getParameter( "freeword" );
            paramAndword = request.getParameter( "andword" );
            paramPrefId = request.getParameter( "pref_id" );
            paramPage = request.getParameter( "page" );
            if ( paramPage == null || CheckString.numCheck( paramPage ) == false )
            {
                paramPage = "0";
            }
            pageNum = Integer.parseInt( paramPage );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            // termNoの取得(xmlのファイル名に使用)
            type = UserAgent.getUserAgentType( request );
            if ( type == UserAgent.USERAGENT_AU )
            {
                termNo = request.getHeader( "x-up-subno" );
            }
            else if ( type == UserAgent.USERAGENT_VODAFONE )
            {
                termNo = request.getHeader( "x-jphone-uid" );
            }
            else if ( type == UserAgent.USERAGENT_DOCOMO )
            {
                termNo = request.getParameter( "uid" );
            }
            // ユーザーエージェントの取得
            userAgent = URLEncoder.encode( UserAgent.getUserAgent( request ), "SHIFT_JIS" );

            if ( paramFreeword != null && (paramFreeword.trim().length() > 0) )
            {
                try
                {
                    paramFreeword = new String( paramFreeword.getBytes( "8859_1" ), "Windows-31J" );
                    paramFull = paramFull + "freeword=" + URLEncoder.encode( paramFreeword, "Shift_JIS" );
                    searchHotelFreeword = new SearchHotelFreeword_M2();
                    // フリーワードで検索する
                    if ( paramPrefId != null && CheckString.numCheck( paramPrefId ) != false )
                    {
                        arrHotelIdList = searchHotelFreeword.getHotelBasicIdListByPref( paramFreeword, Integer.parseInt( paramPrefId ) );
                        paramFull = paramFull + "&pref_id=" + paramPrefId;
                    }
                    else
                    {
                        arrHotelIdList = searchHotelFreeword.getSearchIdList( paramFreeword );
                    }
                    searchHotelCommon = new SearchHotelCommon();
                    // arrHotelIdListをEquipHotelListにセットする
                    searchHotelCommon.setEquipHotelList( arrHotelIdList );
                    if ( paramAndword != null && (paramAndword.trim().length() > 0) )
                    {
                        try
                        {
                            paramAndword = new String( paramAndword.getBytes( "8859_1" ), "Windows-31J" );
                            arrHotelIdList = searchHotelFreeword.getSearchIdList( paramAndword );
                            searchHotelCommon.setResultHotelList( arrHotelIdList );
                            arrHotelIdList = searchHotelCommon.getMargeHotel( 0, 0 );
                            paramFull = paramFull + "&andword=" + URLEncoder.encode( paramAndword, "Shift_JIS" );
                        }
                        catch ( UnsupportedEncodingException e )
                        {
                            Logging.error( "[ActionFreeWordSearchMobile.execute() ] Exception=" + e.toString() );
                        }
                    }
                }
                catch ( UnsupportedEncodingException e )
                {
                    Logging.error( "[ActionFreewordSearchMobile_M2_Flash.execute() ] UnsupportedEncodingException =" + e.toString() );
                    throw e;
                }

                // xmlファイルを作成
                XMLFilePass = GenerateXml1.GenerateXml( arrHotelIdList, paramFull, pageNum, paramFreeword, termNo, type, actPath, actPathFlash, 0, paramUidLink );

                // Flfast起動時のコマンドを生成
                flfastConfig = baseDir + flfastConfig;
                flfastCmd = baseDir + flfastCmd;
                cmd = flfastCmd + " -u" + userAgent + " -f" + flfastConfig;

                // コマンドを実行
                rt = Runtime.getRuntime();
                pr = rt.exec( cmd );
                xmlOut = pr.getOutputStream();
                in = pr.getInputStream();
                try
                {
                    // xmlファイルの読み込み
                    xmlIn = new BufferedInputStream( new FileInputStream( XMLFilePass ) );
                    while( true )
                    {
                        xmlCount = xmlIn.read( readBuffXml );
                        if ( xmlCount == -1 )
                        {
                            break;
                        }
                        // xmlデータをプロセスに流し込む
                        for( int j = 0 ; j < xmlCount ; j++ )
                        {
                            xmlOut.write( readBuffXml[j] );
                        }
                    }
                    // streamをclose
                    xmlIn.close();
                    xmlOut.close();
                }
                catch ( Exception e )
                {
                    Logging.error( "[ActionFreewordSearchMobile_M2_Flash] Exception 1 = " + e.toString() );
                }
                // Flfastで生成されたswfを表示
                try
                {
                    while( true )
                    {
                        count = in.read( readBuff );
                        // ERRCODEで始まっていたらエラーとして処理
                        if ( loopCount == 0 )
                        {
                            if ( new String( readBuff ).substring( 0, 7 ).compareTo( "ERRCODE" ) == 0 )
                            {
                                errCode = new String( readBuff ).substring( 10, 14 );
                                Logging.error( "[ActionFreewordSearchMobile_M2_Flash] ERRCODE = " + errCode );
                                /*
                                 * request.setAttribute( "ERROR-CODE", errCode );
                                 * requestDispatcher = request.getRequestDispatcher( "../search_result_flash_error.jsp" );
                                 * requestDispatcher.forward( request, response );
                                 */
                                response.sendRedirect( "../search_result_flash_error.jsp?" + paramUidLink + "&code=" + errCode );
                                break;
                            }
                            else
                            {
                                out = response.getOutputStream();
                            }
                            loopCount++;
                        }

                        if ( count == -1 )
                        {
                            break;
                        }
                        response.setContentType( "application/x-shockwave-flash" );
                        for( int i = 0 ; i < count ; i++ )
                        {
                            out.write( readBuff[i] );
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.error( "[ActionFreewordSearchMobile_M2_Flash] Exception 2 = " + e.toString() );
                }
                finally
                {
                    out.close();
                    in.close();
                    // xmlファイルを削除
                    File file = new File( XMLFilePass );
                    if ( file.exists() != false )
                    {
                        // file.delete();
                    }
                }

            }
            else
            {
                response.sendRedirect( "searchFreewordMobile.act?" + paramUidLink );
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionFreewordSearchMobile_M2_Flash.execute() ] Exception", exception );
            try
            {
                response.sendRedirect( "../../index.jsp" );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionFreewordSearchMobile_M2_Flash.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            searchHotelCommon = null;
            searchHotelFreeword = null;
        }
    }
}
