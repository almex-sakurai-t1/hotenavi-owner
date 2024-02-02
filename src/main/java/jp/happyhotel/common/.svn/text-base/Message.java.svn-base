/**
 *
 */
package jp.happyhotel.common;

import java.io.FileInputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * メッセージ取得クラス
 * 
 * @author J.Sato
 * @version 1.00 2010/12/09
 */
public class Message implements Serializable
{
    /**
     *
     */
    private static final long   serialVersionUID = -7247565049652207806L;

    private static final String MESSAGE_FILE     = "/etc/happyhotel/messages_jp.properties";
    private static final String MESSAGE_WIN_FILE = "C:\\etc\\happyhotel\\messages_jp.properties";

    /**
     * メッセージファイルから、指定したメッセージIDのメッセージを取得する
     * 
     * @param messageID メッセージID
     * @return 取得したメッセージ（メッセージIDが未登録の場合は、Nullを返す）
     * 
     */
    public static String getMessage(String messageID)
    {
        String messageStr = null;
        String fileURL = null;

        try
        {
            // Propertiesオブジェクトを生成
            Properties props = new Properties();

            // OSの種類でインストールファイルの設置パスを変更する
            if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
            {
                fileURL = MESSAGE_WIN_FILE;
            }
            else
            {
                fileURL = MESSAGE_FILE;
            }

            // ファイルを読み込む
            props.load( new FileInputStream( fileURL ) );

            // メッセージファイルから取得
            messageStr = props.getProperty( messageID );

        }
        catch ( Exception e )
        {
            Logging.error( "[Message.getMessage()] Exception=" + e.toString() + ", messageID:" + messageID );
        }

        return messageStr;
    }

    /**
     * メッセージファイルから、指定したメッセージIDのメッセージを取得する
     * 
     * @param messageID メッセージID
     * @param repString 置換文字
     * @return 取得したメッセージ（メッセージIDが未登録の場合は、Nullを返す）
     * 
     */
    public static String getMessage(String messageID, String repString)
    {
        String messageStr = null;
        String fileURL = null;

        try
        {
            // Propertiesオブジェクトを生成
            Properties props = new Properties();

            // OSの種類でインストールファイルの設置パスを変更する
            if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
            {
                fileURL = MESSAGE_WIN_FILE;
            }
            else
            {
                fileURL = MESSAGE_FILE;
            }

            // ファイルを読み込む
            props.load( new FileInputStream( fileURL ) );

            // メッセージファイルから取得
            messageStr = MessageFormat.format( props.getProperty( messageID ), repString );

        }
        catch ( Exception e )
        {
            Logging.error( "[Message.getMessage()] Exception=" + e.toString() + ", messageID:" + messageID + ", repString:" + repString );
        }

        return messageStr;
    }

    /**
     * メッセージファイルから、指定したメッセージIDのメッセージを取得する
     * 
     * @param messageID メッセージID
     * @param repString1 置換文字１
     * @param repString2 置換文字２
     * @return 取得したメッセージ（メッセージIDが未登録の場合は、Nullを返す）
     * 
     */
    public static String getMessage(String messageID, String repString1, String repString2)
    {
        String messageStr = null;
        String fileURL = null;

        try
        {
            // Propertiesオブジェクトを生成
            Properties props = new Properties();

            // OSの種類でインストールファイルの設置パスを変更する
            if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
            {
                fileURL = MESSAGE_WIN_FILE;
            }
            else
            {
                fileURL = MESSAGE_FILE;
            }

            // ファイルを読み込む
            props.load( new FileInputStream( fileURL ) );

            // メッセージファイルから取得
            messageStr = MessageFormat.format( props.getProperty( messageID ), repString1, repString2 );

        }
        catch ( Exception e )
        {
            Logging.error( "[Message.getMessage()] Exception=" + e.toString() + ", messageID:" + messageID + ", repString1:" + repString1 + ", repString2:" + repString2 );
        }

        return messageStr;
    }

    /**
     * メッセージファイルから、指定したメッセージIDのメッセージを取得する
     * 
     * @param messageID メッセージID
     * @param repString1 置換文字１
     * @param repString2 置換文字２
     * @param repString3 置換文字３
     * @return 取得したメッセージ（メッセージIDが未登録の場合は、Nullを返す）
     * 
     */
    public static String getMessage(String messageID, String repString1, String repString2, String repString3)
    {
        String messageStr = null;
        String fileURL = null;

        try
        {
            // Propertiesオブジェクトを生成
            Properties props = new Properties();

            // OSの種類でインストールファイルの設置パスを変更する
            if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
            {
                fileURL = MESSAGE_WIN_FILE;
            }
            else
            {
                fileURL = MESSAGE_FILE;
            }

            // ファイルを読み込む
            props.load( new FileInputStream( fileURL ) );

            // メッセージファイルから取得
            messageStr = MessageFormat.format( props.getProperty( messageID ), repString1, repString2, repString3 );

        }
        catch ( Exception e )
        {
            Logging.error( "[Message.getMessage()] Exception=" + e.toString() + ", messageID:" + messageID + ", repString1:" + repString1 + ", repString2:" + repString2 + ", repString3:" + repString3 );
        }

        return messageStr;
    }

}
