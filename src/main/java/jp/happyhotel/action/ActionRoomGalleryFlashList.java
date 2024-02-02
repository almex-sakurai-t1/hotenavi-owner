package jp.happyhotel.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.others.GenerateXmlRoomGallery;

/**
 * 
 * Flashルームギャラリーリスト表示クラス
 * 
 * @author S.Tashiro
 * @version 1.0 2010/04/08
 */

public class ActionRoomGalleryFlashList extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    /**
     * ホテルのルームギャラリー画像の一覧をFlashで表示する
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param response サーバからクライアントへのレスポンス
     * @see "search_result_flash_error.jsp Flfastがエラーを出力した際に表示"
     * @see "../../index.jsp 予期しないエラーの場合に表示"
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int pageNum = 0;
        int currentNum = 0;
        int hotelId;
        int carrierFlag;
        int count, xmlCount, loopCount = 0;
        String paramHotelId;
        String paramPage;
        String paramUidLink = null;
        String termNo = null;
        String paramFull = "";
        String paramBefore = "";
        String paramCurrentNum = "";
        String userAgent = "";
        String errCode = "";
        String paramCategory;
        final String actPathFlash = "search/roomGalleryFlashList.act";
        InputStream in = null, xmlIn = null;
        OutputStream out = null, xmlOut = null;
        byte[] readBuff = new byte[1024];
        byte[] readBuffXml = new byte[1024];
        String XMLFilePass = "";
        DataLoginInfo_M2 dli;
        // NumberFormat nf;
        final String confFilePass = "/etc/happyhotel/test.conf";
        FileInputStream propfile = null;
        Properties config;
        String baseDir = "/happyhotel/flash/flfast";
        String flfastCmd = "/bin/flfast";
        String flfastConfig = "/bin/flfast.conf";
        String cmd;
        Runtime rt;
        Process pr;
        String paramAcRead;
        boolean ret;
        AuAuthCheck auCheck;

        dli = new DataLoginInfo_M2();
        // nf = new DecimalFormat( "0000" );

        try
        {
            dli = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

            carrierFlag = UserAgent.getUserAgentType( request );
            // auだったらアクセスチケットをチェックする
            carrierFlag = UserAgent.getUserAgentType( request );
            paramAcRead = request.getParameter( "acread" );
            if ( (paramAcRead == null) && (carrierFlag == DataMasterUseragent.CARRIER_AU) )
            {
                try
                {
                    auCheck = new AuAuthCheck();
                    ret = auCheck.authCheckForClass( request, "free/mymenu/premium_flash.jsp" );
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
                    Logging.info( "[ActionRoomFlashList AuAuthCheck] Exception:" + e.toString() );
                }
            }
            // 有料会員ﾁｪｯｸ
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            if ( dli != null )
            {
                // 有料会員登録途中のユーザーは有料会員登録ページへ
                if ( dli.getRegistStatusPay() == 1 )
                {
                    response.sendRedirect( "../free/mymenu/paymemberRegist.act?" + paramUidLink );
                    return;
                }
                // flash機能説明ページへ
                else if ( dli.getRegistStatusPay() != 9 )
                {
                    response.sendRedirect( "../free/mymenu/premium_flash.jsp?" + paramUidLink );
                    return;
                }
            }
            // 会員情報がない場合はflash機能説明ページへ
            else
            {
                response.sendRedirect( "../free/mymenu/premium_flash.jsp?" + paramUidLink );
                return;
            }
            paramHotelId = request.getParameter( "hotel_id" );
            if ( paramHotelId == null || CheckString.numCheck( paramHotelId ) == false )
            {
                response.sendRedirect( "../index.jsp?" + paramUidLink );
                return;
            }
            paramPage = request.getParameter( "page" );
            if ( paramPage == null || CheckString.numCheck( paramPage ) == false )
            {
                paramPage = "0";
            }
            paramBefore = request.getParameter( "bef" );
            if ( paramBefore == null )
            {
                paramBefore = "";
            }
            paramCurrentNum = request.getParameter( "num" );
            if ( paramCurrentNum == null || CheckString.numCheck( paramCurrentNum ) == false )
            {
                paramCurrentNum = "0";
            }
            // termNoの取得(xmlのファイル名に使用)
            if ( carrierFlag == UserAgent.USERAGENT_AU )
            {
                termNo = request.getHeader( "x-up-subno" );
                // carrier = "au/";
            }
            else if ( carrierFlag == UserAgent.USERAGENT_VODAFONE )
            {
                termNo = request.getHeader( "x-jphone-uid" );
                // carrier = "y/";
            }
            else
            {
                termNo = request.getParameter( "uid" );
                // carrier = "i/";
            }
            paramCategory = request.getParameter( "category" );
            if ( paramCategory == null || paramCategory.compareTo( "" ) == 0 || CheckString.numCheck( paramCategory ) == false )
            {
                paramCategory = "0";
            }

            paramFull = "hotel_id=" + paramHotelId;
            paramFull = paramFull + "&" + paramUidLink;
            {

                if ( CheckString.numCheck( paramHotelId ) && CheckString.numCheck( paramPage ) )
                {
                    hotelId = Integer.parseInt( paramHotelId );
                    pageNum = Integer.parseInt( paramPage );
                    currentNum = Integer.parseInt( paramCurrentNum );

                    // ユーザーエージェントの取得
                    userAgent = URLEncoder.encode( UserAgent.getUserAgent( request ), "SHIFT_JIS" );

                    // xmlファイルを作成
                    XMLFilePass = GenerateXmlRoomGallery.GenerateXml( hotelId, paramFull, pageNum, currentNum, termNo, carrierFlag, actPathFlash, paramBefore );

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
                        Logging.error( "[ActionRoomFlashList] Exception 1 = " + e.toString() );
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
                                    Logging.error( "[ActionRoomFlashList] ERRCODE = " + errCode );
                                    /*
                                     * request.setAttribute( "ERROR-CODE", errCode );
                                     * requestDispatcher = request.getRequestDispatcher( "search_result_flash_error.jsp?" + paramUidLink );
                                     * requestDispatcher.forward( request, response );
                                     */
                                    response.sendRedirect( "search_result_flash_error.jsp?" + paramUidLink + "&code=" + errCode );
                                    return;
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
                        Logging.error( "[ActionRoomFlashList] Exception 2 = " + e.toString() );
                    }
                    finally
                    {
                        out.close();
                        in.close();
                        // xmlファイルを削除
                        File file = new File( XMLFilePass );
                        if ( file.exists() != false )
                        {
                            file.delete();
                        }
                    }

                }
                else
                {
                    response.sendRedirect( "searchAreaMobile.act" );
                }
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionRoomFlashList.execute() ] Exception", exception );
            try
            {
                response.sendRedirect( "../index.jsp?" + paramUidLink );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionRoomFlashList.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
        }
    }
}
