package jp.happyhotel.action;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserSp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * 差分チェック情報取得
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/06
 */

public class ActionYWalletDiffUser extends BaseAction
{
    DataUserSp[] duSp         = null;
    // String FTP_HOST = "ftp.yahoos.jp";
    String       FTP_HOST     = "ftp.castle.yahoofs.jp";
    String       FTP_PATH     = "/check";
    String       FTP_USER     = "yoswgw-wg1236";
    String       FTP_PASSWORD = "HoKoG5LG";

    /**
     * 差分チェック情報
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        // final String PID = "123600005";// テスト
        final String PID = "123600006";// 本番
        final String PROP_ID = "1236";
        final String PRODUCT_TYPE = "MS";
        boolean ret = false;
        String strMd5 = "";
        String date = "";
        String fileDate;
        String fileTime;
        String fileName = "";
        String filePath = "";
        int i = 0;
        int nContentLength = 0;
        int result = 99;

        ServletOutputStream stream;

        date = request.getParameter( "checkDate" );
        if ( (date == null) || (date.equals( "" ) != false) || (CheckString.numCheck( date ) == false) )
        {
            return;
        }

        try
        {
            filePath = "/home/tashiro-s1/";
            // ファイル名生成
            fileName = PROP_ID + "-" + DateEdit.getDate( 2 ) + DateEdit.getTime( 1 ) + "-" + PRODUCT_TYPE;

            Logging.info( "filename:" + fileName );
            File tsv = new File( filePath + fileName ); // TSVデータファイル

            duSp = this.getUserData( Integer.parseInt( date ) );
            if ( duSp != null && duSp.length > 0 )
            {

                // 追記モード
                BufferedWriter bw = new BufferedWriter( new FileWriter( tsv, true ) );

                String checkOpenId = "";
                // 新たなデータ行の追加
                for( i = 0 ; i < duSp.length ; i++ )
                {
                    // openIDが違った時だけ出力
                    if ( checkOpenId.equals( duSp[i].getOpenId() ) == false )
                    {
                        checkOpenId = duSp[i].getOpenId();
                        bw.write( duSp[i].getOrderNo() + "\t" + duSp[i].getOpenId() + "\t" +
                                this.formatDate( duSp[i].getRegistDatePay(), duSp[i].getRegistTimePay() ) + "\t" + PID );
                        bw.newLine();
                    }
                }
                bw.close();

                // ファイルが出力されたかを確認する
                if ( tsv.exists() != false )
                {
                    strMd5 = this.getMd5( filePath + fileName );
                    File newTsv = new File( filePath + fileName + "." + strMd5 );
                    ret = tsv.renameTo( newTsv );
                    fileName += "." + strMd5;
                }
                if ( ret != false )
                {
                    result = ftp( FTP_HOST, FTP_USER, FTP_PASSWORD, filePath, fileName );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionYWalletDiffUser.execute()] Exception:" + e.toString() );
        }
        finally
        {
        }
    }

    /**
     * 有効ユーザSPデータ取得（yahooWallet）
     * 
     * @param yyyymmdd
     * @return
     */
    private DataUserSp[] getUserData(int yyyymmdd)
    {
        int i = 0;
        int count = 0;
        int thisMonth = 0;
        int lastMonth = 0;
        DataUserSp[] dusp = null;
        boolean ret = false;

        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        try
        {
            if ( yyyymmdd > 0 && DateEdit.checkDate( Integer.toString( yyyymmdd ) ) != false )
            {
                thisMonth = DateEdit.addDay( yyyymmdd, -1 );
                lastMonth = thisMonth / 100 * 100 + 1;
                Logging.info( "thisMonth" + thisMonth );
                Logging.info( "lastMonth" + lastMonth );
            }
            if ( lastMonth > 0 && thisMonth > 0 )
            {
                // 現在YahooWalletで注文番号が12桁、有料会員のものと、退会済みで退会日が対象範囲内のものを取得
                query = "SELECT * FROM hh_user_sp";
                query += " WHERE carrier_kind = 4 AND charge_flag = 1 AND del_flag = 0 AND CHAR_LENGTH( order_no ) = 12";
                query += " And regist_date_pay <= ?";
                query += " UNION SELECT * FROM hh_user_sp";
                query += " WHERE carrier_kind = 4 AND charge_flag = 0 AND del_date_pay >= ? AND del_date_pay <= ? AND CHAR_LENGTH( order_no ) = 12";
                query += " ORDER BY open_id, regist_date_pay DESC, regist_time DESC, del_date_pay DESC, del_time_pay DESC";

                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );

                prestate.setInt( 1, thisMonth );
                prestate.setInt( 2, lastMonth );
                prestate.setInt( 3, thisMonth );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        count = result.getRow();
                    }

                    // クラスの配列を用意し、初期化する。
                    dusp = new DataUserSp[count];
                    for( i = 0 ; i < count ; i++ )
                    {
                        dusp[i] = new DataUserSp();
                    }
                    i = 0;
                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        dusp[i].setData( result );
                        i++;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionYWalletDiffUser.getUserData()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return dusp;
    }

    /**
     * MD5変換処理
     * 
     * @param passwd パスワード
     * @return 処理結果(null：MD5変換失敗)
     **/
    private String getMd5(String filename)
    {

        String strMd5 = "";
        MessageDigest md;
        try
        {
            md = MessageDigest.getInstance( "MD5" );
            DigestInputStream inStream = new DigestInputStream(
                    new BufferedInputStream( new FileInputStream( filename ) ), md );

            while( inStream.read() != -1 )
            {
            }
            byte[] digest = md.digest();
            inStream.close();

            for( int i = 0 ; i < digest.length ; i++ )
            {
                strMd5 += String.format( "%02x", digest[i] );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionYWalletDiffUser.getMD5()] Exception:" + e.toString() );
        }

        return strMd5;
    }

    /**
     * 日付時刻のフォーマット処理
     * 
     * @param date
     * @param time
     * @return
     */
    private String formatDate(int date, int time)
    {
        String returnDateTime = "";

        returnDateTime = DateEdit.getDate( 3, date ) + " " + DateEdit.getTime( 0, time );

        return returnDateTime;
    }

    /**
     * 
     * @param host
     * @param user
     * @param password
     * @param path
     * @param filename
     * @return
     */
    private int ftp(String host, String user, String password, String path, String filename)
    {
        int returnInt = 99;
        FTPClient fp = new FTPClient();
        FileInputStream is = null;
        try
        {

            Logging.info( "FTP LOADED" );

            fp.connect( host );
            if ( !FTPReply.isPositiveCompletion( fp.getReplyCode() ) )
            { // コネクトできたか？
                returnInt = 1;
                Logging.info( "1" );
            }
            if ( fp.login( user, password ) == false )
            { // ログインできたか？
                returnInt = 2;
                Logging.info( "2" );
            }

            // ファイル送信
            is = new FileInputStream( path + filename );// クライアント側
            Logging.info( path + filename );

            if ( fp.storeFile( FTP_PATH + "/" + filename, is ) )// サーバー側
            {
                Logging.info( "FTP UPLOAD:" + FTP_PATH + "/" + filename );
                returnInt = 0;
                Logging.info( "0" );
            }
            else
            {
                returnInt = 3;
                Logging.info( "3" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionYWalletDiffUser.ftp()] Exception:" + e.toString() );
        }
        finally
        {
            try
            {
                fp.disconnect();
            }
            catch ( IOException e )
            {
                // TODO 自動生成された catch ブロック
                Logging.error( "[ActionYWalletDiffUser.ftp()] disconnect IOException:" + e.toString() );
            }
        }
        return returnInt;
    }
}
