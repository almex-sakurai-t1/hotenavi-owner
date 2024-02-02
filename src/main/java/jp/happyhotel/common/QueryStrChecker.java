package jp.happyhotel.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.happyhotel.data.DataMapConvert;

/**
 * クエリストリング（リクエストパラメータ）確認・変換用クラス<br>
 * <br>
 * クエリストリングを検査し、必要であれば変換も行います<br>
 * 
 * @author Koshiba
 */
public class QueryStrChecker
{
    /** 変換済み */
    private static final int CONVERTED           = 1;
    /** 変換なし */
    private static final int UNCONVERTED         = 0;
    /** エラー */
    private static final int ERROR               = -1;

    /** 変換されたクエリストリング */
    private String           converted_query_str = null;

    /**
     * コンストラクタ
     */
    public QueryStrChecker()
    {
        converted_query_str = "";
    }

    /**
     * converted_query_strのゲッター
     * 
     * @return 変換されたクエリストリング
     */
    public String getConvertedQueryStr()
    {
        return converted_query_str;
    }

    /**
     * クエリストリングに昭文社のコードが含まれているかどうかを確認<br>
     * <br>
     * クエリストリングに昭文社の地図コードが含まれているかどうかを確認します<br>
     * 昭文社の地図コードが含まれていた場合、ゼンリンのコードを使用したものに変換します<br>
     * コードが含まれていた場合は 1 を、含まれていなかった場合は 0 を、不正な文字列が含まれていた場合（変換テーブルにデータが存在しなかった場合）は -1 を返します<br>
     * 変換テーブルとして"hh_map_convert"を参照します（DataMapConvert.javaを参照）<br>
     * 変換後のクエリストリングは"this.converted_query_str"に格納されます<br>
     * 
     * @param query_str クエリストリング
     * @return 処理結果（1：昭文社のコードあり（コード変換済み）、0：昭文社のコードなし（コード変換なし）、-1：エラー（不正な文字列の混入））
     */
    public int checkIncludingShobunshaCode(String query_str)
    {
        converted_query_str = "";

        if ( query_str == null )
        {
            return UNCONVERTED;
        }

        DataMapConvert converter = new DataMapConvert(); // 昭文社コード -> ゼンリンコード変換用クラス
        boolean f = false; // コード変換処理の実行の有無

        query_str = query_str.replaceAll( "%40", "@" ); // クエリストリングの"@"が"%40"となっている場合があるので事前に置換を行う
        Pattern pattern = Pattern.compile( "[a-zA-Z_]+=\\d+@\\d+((,|:)\\d+@\\d+)*" ); // "route_id=513@000040"などがヒットする
        Matcher matcher = pattern.matcher( query_str );

        while( matcher.find() )
        {
            String param[] = matcher.group().split( "=", 2 ); // "route_id"と"513@000040"に分割

            // 昭文社コードに含まれる区切り文字が","か":"かを判定（含まれていなくても下記for文を一度通過させるため適当な区切り文字を入れておく）
            Matcher code_separator = Pattern.compile( "(,|:)" ).matcher( param[1] );
            String sep = " "; // 区切り文字
            if ( code_separator.find() )
            {
                sep = code_separator.group();
            }

            // "123@456789,123@456789,……"のようなコードもあるのでforで回す
            StringBuffer converted_param = new StringBuffer(); // 変換されたコード格納用変数
            for( String code : param[1].split( sep ) )
            {
                // Java8でないとjoinが使えないようなので原始的なカンマ（もしくはコロン）区切りを行う
                if ( converted_param.length() > 0 )
                {
                    converted_param.append( sep );
                }

                // コード変換
                if ( converter.getData( code ) )
                {
                    converted_param.append( converter.getConvertId() );
                    f = true;
                }
                else
                {
                    return ERROR; // 変換テーブルにデータがなかった場合は不正なアクセスと判断
                }
            }
            query_str = query_str.replaceAll( param[0] + "=" + param[1], param[0] + "=" + converted_param.toString() );
        }

        if ( f == true )
        {
            // コードが含まれていた場合（コードの変換処理があった場合）
            converted_query_str = query_str;
            return CONVERTED;
        }
        else
        {
            // コードが含まれていなかった場合（コードの変換処理が無かった場合）
            return UNCONVERTED;
        }
    }
}
