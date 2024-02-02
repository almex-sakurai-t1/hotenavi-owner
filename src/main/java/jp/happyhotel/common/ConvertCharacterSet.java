package jp.happyhotel.common;

import java.io.Serializable;

/**
 * 文字コード変換クラス
 *
 * @author
 * @version 1.00 2010/12/28
 */
public class ConvertCharacterSet implements Serializable
{
    private static final long serialVersionUID = 505830332174012204L;

    /**
     * データベース登録用変換
     * 画面で入力した文字の文字コードを、データベースの文字コードに変換する。(java 1.6のバージョンアップに伴い変換をしないように修正)
     *
     * @param str 文字コード変換前の文字列
     * @return 文字コード変換後の文字列
     */
    public static String convForm2Db(String str)
    {

        String convStr = null;

        try
        {
            convStr = str;
        }
        catch ( Exception e )
        {
            Logging.error( "[ConvertCharacterSet.convForm2Db()] Exception=" + e.toString() + ", str:" + str );
        }

        return convStr;
    }

    /**
     * 画面表示用変換
     * データベースの文字コードを、画面表示用の文字コードに変換する。(java 1.6のバージョンアップに伴い変換をしないように修正)
     *
     * @param str 文字コード変換前の文字列
     * @return 文字コード変換後の文字列
     */
    public static String convDb2Form(String str)
    {

        String convStr = null;

        try
        {
            convStr = str;
        }
        catch ( Exception e )
        {
            Logging.error( "[ConvertCharacterSet.convDb2Form()] Exception=" + e.toString() + ", str:" + str );
        }

        return convStr;
    }

}
