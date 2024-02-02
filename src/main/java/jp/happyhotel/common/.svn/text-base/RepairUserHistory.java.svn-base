/*
 * @(#)RepairUserHistory.java 2.00 2008/06/30 Copyright (C) ALMEX Inc. 2007 アクセスログ集計クラス
 */
package jp.happyhotel.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * アクセスログ集計クラス。
 * 
 * @author S.Shiiya
 * @version 2.00 2008/06/30
 */
public class RepairUserHistory
{
    /**
     * メイン
     * 
     * @param argv
     */
    public static void main(String argv[])
    {
        String fileName = "";

        if ( argv.length == 1 )
        {
            fileName = argv[0];

            Debug( "Starting RepairUserHistory(" + fileName + ")" );

            // アクセスログの解析開始
            RepairData( fileName );

            Debug( "End ConvertAccessLog(" + DateEdit.getDate( 0 ) + " " + DateEdit.getTime( 0 ) + ")" );
        }
        else
        {
            Debug( "RepairUserHistory <filename>" );
        }

    }

    /**
     * アクセスログ集計処理
     * 
     * @param fileName 対象日付
     */
    private static void RepairData(String fileName)
    {
        int cutIndex;
        String driver;
        String connurl;
        String user;
        String password;
        String line;
        String cutData;
        Properties prop;
        Connection connect = null;
        Statement stateupdate;
        FileReader fr;
        BufferedReader br;

        try
        {
            // データベースの初期化
            prop = new Properties();
            // ﾌﾟﾛﾊﾟﾃｨﾌｧｲﾙからｷｰと値のﾘｽﾄを読み込みます
            prop.load( new FileInputStream( "/etc/happyhotel/dbconnect_repair.conf" ) );
            // "jdbc.driver"に設定されている値を取得します
            driver = prop.getProperty( "jdbc.driver" );
            // "jdbc.url"に設定されている値を取得します
            connurl = prop.getProperty( "jdbc.url" );
            // "jdbc.user"に設定されている値を取得します
            user = prop.getProperty( "jdbc.user" );
            // "jdbc.url"に設定されている値を取得します
            password = prop.getProperty( "jdbc.password" );

            // MySQLへの接続を確立する
            Class.forName( driver );
            // MySQLへ接続する
            connect = DriverManager.getConnection( connurl, user, password );
        }
        catch ( Exception e )
        {
            Debug( "dbiniterr " + e.toString() );
        }

        try
        {
            Debug( "LogFileName=" + fileName );

            // アクセスログの取得
            fr = new FileReader( fileName );
            br = new BufferedReader( fr );
            while( true )
            {
                line = br.readLine();
                if ( line == null )
                {
                    break;
                }

                cutIndex = line.indexOf( "INSERT" );
                if ( cutIndex > 0 )
                {
                    cutData = line.substring( cutIndex );
                    Debug( cutData );

                    // java.lang.Thread.sleep(20);

                    try
                    {
                        stateupdate = connect.createStatement();
                        stateupdate.executeUpdate( cutData );
                        stateupdate.close();
                    }
                    catch ( Exception e )
                    {
                        Debug( "RepairData(" + e.toString() + ")" );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Debug( e.toString() );
        }

        try
        {
            connect.close();
        }
        catch ( Exception e )
        {
        }
    }

    /**
     * ログ出力処理
     * 
     * @param output
     */
    private static void Debug(String output)
    {
        System.out.println( output );
    }
}
