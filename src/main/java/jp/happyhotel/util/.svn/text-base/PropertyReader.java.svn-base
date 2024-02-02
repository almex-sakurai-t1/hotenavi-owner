package jp.happyhotel.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * プロパティファイル読み込みクラス<br>
 * <br>
 * プロパティファイルを読み込むためのユーティリティクラスです。<br>
 * プロパティファイルから値を取得する機能を提供します。<br>
 */
public class PropertyReader
{
    /**
     * コンストラクタ
     */
    private PropertyReader()
    {
    }

    /**
     * 文字列取得<br>
     * <br>
     * プロパティファイルから文字列を取得します。<br>
     *
     * @param key キー
     * @param fileName ファイル名（拡張子なし）
     * @return キーに対応する値
     * @throws NullPointerException 指定したキーやファイル名がnullの場合。
     * @throws MissingResourceException 指定したキーやファイルが見つからない場合。
     * @throws ClassCastException 指定したキーで見つかったオブジェクトが文字列ではない場合。
     */
    public static String readStr(String key, String fileName)
    {
        ResourceBundle rb = ResourceBundle.getBundle( fileName );

        return rb.getString( key );
    }

    /**
     * 文字列取得<br>
     * <br>
     * プロパティファイルから文字列を取得します。<br>
     *
     * @param key キー
     * @param fileName ファイル名（拡張子なし）
     * @param directory プロパティファイルが存在するディレクトリ
     * @return キーに対応する値
     * @throws NullPointerException 指定したキーやファイル名がnullの場合。
     * @throws MissingResourceException 指定したキーやファイルが見つからない場合。
     * @throws ClassCastException 指定したキーで見つかったオブジェクトが文字列ではない場合。
     */
    public static String readStr(String key, String fileName, URL directory)
    {
        URLClassLoader urlLoader = new URLClassLoader( new URL[]{ directory } );
        ResourceBundle rb = ResourceBundle.getBundle( fileName, Locale.getDefault(), urlLoader );

        return rb.getString( key );
    }

    /**
     * 文字列取得<br>
     * <br>
     * プロパティファイルから文字列を取得します。<br>
     * 値が読み込めなかった場合、指定したデフォルト値を返します。<br>
     *
     * @param key キー
     * @param fileName ファイル名（拡張子なし）
     * @param defaultValue 値が読み込めなかった場合のデフォルト値
     * @return キーに対応する値（読み込めなかった場合は指定したデフォルト値）
     */
    public static String readStr(String key, String fileName, String defaultValue)
    {
        try
        {
            return readStr( key, fileName );
        }
        catch ( Exception e )
        {
            return defaultValue;
        }
    }

    /**
     * 文字列取得<br>
     * <br>
     * プロパティファイルから文字列を取得します。<br>
     * 値が読み込めなかった場合、指定したデフォルト値を返します。<br>
     *
     * @param key キー
     * @param fileName ファイル名（拡張子なし）
     * @param directory プロパティファイルが存在するディレクトリ
     * @param defaultValue 値が読み込めなかった場合のデフォルト値
     * @return キーに対応する値（読み込めなかった場合は指定したデフォルト値）
     */
    public static String readStr(String key, String fileName, URL directory, String defaultValue)
    {
        try
        {
            return readStr( key, fileName, directory );
        }
        catch ( Exception e )
        {
            return defaultValue;
        }
    }

    /**
     * 整数値取得<br>
     * <br>
     * プロパティファイルから整数値を取得します。<br>
     *
     * @param key キー
     * @param fileName ファイル名（拡張子なし）
     * @return キーに対応する値
     * @throws NullPointerException 指定したキーやファイル名がnullの場合。
     * @throws MissingResourceException 指定したキーやファイルが見つからない場合。
     * @throws ClassCastException 指定したキーで見つかったオブジェクトが文字列ではない場合。
     * @throws NumberFormatException 指定したキーで見つかったオブジェクトを整数値にパースできなかった場合。
     */
    public static int readInt(String key, String fileName)
    {
        return Integer.parseInt( readStr( key, fileName ) );
    }

    /**
     * 整数値取得<br>
     * <br>
     * プロパティファイルから整数値を取得します。<br>
     *
     * @param key キー
     * @param fileName ファイル名（拡張子なし）
     * @param directory プロパティファイルが存在するディレクトリ
     * @return キーに対応する値
     * @throws NullPointerException 指定したキーやファイル名がnullの場合。
     * @throws MissingResourceException 指定したキーやファイルが見つからない場合。
     * @throws ClassCastException 指定したキーで見つかったオブジェクトが文字列ではない場合。
     * @throws NumberFormatException 指定したキーで見つかったオブジェクトを整数値にパースできなかった場合。
     */
    public static int readInt(String key, String fileName, URL directory)
    {
        return Integer.parseInt( readStr( key, fileName, directory ) );
    }

    /**
     * 整数値取得<br>
     * <br>
     * プロパティファイルから整数値を取得します。<br>
     * 値が読み込めなかった場合、指定したデフォルト値を返します。<br>
     *
     * @param key キー
     * @param fileName ファイル名（拡張子なし）
     * @param defaultValue 値が読み込めなかった場合のデフォルト値
     * @return キーに対応する値（読み込めなかった場合は指定したデフォルト値）
     */
    public static int readInt(String key, String fileName, int defaultValue)
    {
        try
        {
            return readInt( key, fileName );
        }
        catch ( Exception e )
        {
            return defaultValue;
        }
    }

    /**
     * 整数値取得<br>
     * <br>
     * プロパティファイルから整数値を取得します。<br>
     * 値が読み込めなかった場合、指定したデフォルト値を返します。<br>
     *
     * @param key キー
     * @param fileName ファイル名（拡張子なし）
     * @param directory プロパティファイルが存在するディレクトリ
     * @param defaultValue 値が読み込めなかった場合のデフォルト値
     * @return キーに対応する値（読み込めなかった場合は指定したデフォルト値）
     */
    public static int readInt(String key, String fileName, URL directory, int defaultValue)
    {
        try
        {
            return readInt( key, fileName, directory );
        }
        catch ( Exception e )
        {
            return defaultValue;
        }
    }

    /**
     * 文字列のリスト取得<br>
     * <br>
     * プロパティファイルから文字列を格納したリストを取得します。<br>
     *
     * @param key キー
     * @param fileName ファイル名（拡張子なし）
     * @return キーに対応する値を格納したリスト
     * @throws NullPointerException 指定したキーやファイル名がnullの場合。
     * @throws MissingResourceException 指定したキーやファイルが見つからない場合。
     * @throws ClassCastException 指定したキーで見つかったオブジェクトが文字列ではない場合。
     */
    public static List<String> readStrs(String key, String fileName)
    {
        return Arrays.asList( readStr( key, fileName ).split( ", *" ) );
    }

    /**
     * 文字列のリスト取得<br>
     * <br>
     * プロパティファイルから文字列を格納したリストを取得します。<br>
     *
     * @param key キー
     * @param fileName ファイル名（拡張子なし）
     * @param directory プロパティファイルが存在するディレクトリ
     * @return キーに対応する値を格納したリスト
     * @throws NullPointerException 指定したキーやファイル名がnullの場合。
     * @throws MissingResourceException 指定したキーやファイルが見つからない場合。
     * @throws ClassCastException 指定したキーで見つかったオブジェクトが文字列ではない場合。
     */
    public static List<String> readStrs(String key, String fileName, URL directory)
    {
        return Arrays.asList( readStr( key, fileName, directory ).split( ", *" ) );
    }

    /**
     * 文字列のリスト取得<br>
     * <br>
     * プロパティファイルから文字列を格納したリストを取得します。<br>
     * 値が読み込めなかった場合、指定したデフォルト値を返します。<br>
     *
     * @param key キー
     * @param fileName ファイル名（拡張子なし）
     * @param defaultValues 値が読み込めなかった場合のデフォルト値を格納したリスト
     * @return キーに対応する値を格納したリスト（読み込めなかった場合は指定したデフォルト値を格納したリスト）
     */
    public static List<String> readStrs(String key, String fileName, List<String> defaultValues)
    {
        try
        {
            return readStrs( key, fileName );
        }
        catch ( Exception e )
        {
            return defaultValues;
        }
    }

    /**
     * 文字列のリスト取得<br>
     * <br>
     * プロパティファイルから文字列を格納したリストを取得します。<br>
     * 値が読み込めなかった場合、指定したデフォルト値を返します。<br>
     *
     * @param key キー
     * @param fileName ファイル名（拡張子なし）
     * @param directory プロパティファイルが存在するディレクトリ
     * @param defaultValues 値が読み込めなかった場合のデフォルト値を格納したリスト
     * @return キーに対応する値を格納したリスト（読み込めなかった場合は指定したデフォルト値を格納したリスト）
     */
    public static List<String> readStrs(String key, String fileName, URL directory, List<String> defaultValues)
    {
        try
        {
            return readStrs( key, fileName, directory );
        }
        catch ( Exception e )
        {
            return defaultValues;
        }
    }
}
