package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CreateToken;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * 
 * 注文情報取得IF
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/06
 */

public class ActionDocomoOrderInfo extends BaseAction
{

    /**
     * 注文情報取得IF
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final String FREE_SPCD = "00073361101";
        final String PAY_SPCD = "00073361102";
        boolean ret = true;
        int i = 0;
        int nContentLength = 0;
        ServletOutputStream stream;
        // リクエストパラメータ用
        String sSpcd = "";
        String sCptok = "";
        CreateToken cToken = new CreateToken();

        // レスポンス用
        String result = "";
        String sSpnm = "";// 加盟店注文番号
        String sSpsppr = "aaaa";// 加盟店指定パラメータ（任意）
        String sDate = "";// 決済要求日時
        String sPric = "315";// 料金
        String sDspstr1 = "";
        String sDspstr2 = "";
        String sTrset = "0";
        String sSuid = "";

        sSpcd = request.getParameter( "sSpcd" );
        sCptok = request.getParameter( "sCptok" );

        Logging.info( "request:" + request.toString() );

        try
        {
            if ( sSpcd == null )
            {
                sSpcd = "";
            }
            if ( sCptok == null )
            {
                sCptok = "";
            }

            stream = response.getOutputStream();

            Logging.info( "scpTok:" + sCptok );
            // URLデコード(社内でテストするときは必要)
            // sCptok = URLDecoder.decode( sCptok, "Shift_JIS" );
            Logging.info( "scpTok(decode):" + sCptok );

            while( true )
            {
                // ドコモが通知したsSpcdが決済要求IFで設定したsSpcdであること
                if ( sSpcd.equals( FREE_SPCD ) == false && sSpcd.equals( PAY_SPCD ) == false )
                {
                    Logging.info( "SPCD ERROR" );
                    ret = false;
                    result = "NG";
                    break;
                }

                // sCptokが決済要求IFで設定したsCptokであること
                // ドコモが通知したsCptokが発行から5分以内であること
                if ( sCptok.equals( "" ) != false || cToken.decodeCheck( sCptok ) == false )
                {
                    Logging.info( "sCptok ERROR" );
                    ret = false;
                    result = "NG";
                    break;
                }

                // 接続元がドコモのIPアドレスであること。

                break;
            }

            if ( ret != false )
            {
                if ( cToken.getKind() == 1 || cToken.getKind() == 3 )
                {
                    sPric = "0";
                }
                else if ( cToken.getKind() == 2 || cToken.getKind() == 4 )
                {
                    sPric = "315";
                }
                sSuid = cToken.getSuid();
                Logging.error( "ORDERINFO_SUID:" + sSuid );

                result = "OK";
                sSpnm = "100000" + DateEdit.getDate( 2 ) + DateEdit.formatTime( 1, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                sDate = DateEdit.getDate( 0 ) + " " + DateEdit.formatTime( 0, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                Logging.info( "result1:" + result );
            }
            Logging.info( "createToken.getKind():" + cToken.getKind() );
            // 登録完了
            response.setContentType( "text/plain" );
            response.setCharacterEncoding( "Shift_JIS" );
            response.setStatus( 200 );

            // OK<LF>を送信

            if ( ret == false )
            {
                Logging.info( "resultNG:" + result );
                stream.print( "Result=" + result + "\r" );
            }
            else
            {
                Logging.info( "Result=" + result );
                Logging.info( "sSpnm=" + sSpnm );
                Logging.info( "sDate=" + sDate );
                Logging.info( "sPric=" + sPric );
                Logging.info( "sSpsppr=" + sSpsppr );
                Logging.info( "sDspstr1=" + sDspstr1 );
                Logging.info( "sTrset=" + sTrset );
                Logging.info( "sSuid=" + sSuid );

                stream.print( "Result=" + result );
                stream.print( "\r" );
                stream.print( "sSpnm=" + sSpnm );
                stream.print( "\r" );
                stream.print( "sDate=" + sDate );
                stream.print( "\r" );
                stream.print( "sPric=" + sPric );
                stream.print( "\r" );
                stream.print( "sSpsppr=" + sSpsppr );
                stream.print( "\r" );
                stream.print( "sDspstr1=" + sDspstr1 );
                stream.print( "\r" );
                // stream.print( "sDspstr2=" + sDspstr2 );
                // stream.print( "\r" );
                stream.print( "sTrset=" + sTrset );
                stream.print( "\r" );
                stream.print( "sSuid=" + sSuid );
                stream.print( "\r" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionOrderInfoDocomo.excute()] Exception:" + e.toString() );
        }
        finally
        {
        }
    }
}
