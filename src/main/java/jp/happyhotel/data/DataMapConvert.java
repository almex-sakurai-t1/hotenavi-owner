package jp.happyhotel.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 地図コード変換用クラス（データクラス）<br>
 * <br>
 * 地図コード変換用テーブル（hh_map_convert）を使用し、昭文社の地図コードをゼンリンの地図コードに変換します<br>
 * 
 * @author Koshiba
 */
public class DataMapConvert
{
    /** 変換されたゼンリンの地図コード */
    private String convertId;

    /**
     * コンストラクタ
     */
    public DataMapConvert()
    {
        this.convertId = "";
    }

    /**
     * convertIDのゲッター
     * 
     * @return convertID（変換されたゼンリンの地図コード）
     */
    public String getConvertId()
    {
        return convertId;
    }

    /**
     * 地図コードの変換<br>
     * <br>
     * 昭文社の地図コードをゼンリンの地図コードに変換します<br>
     * 変換を行うにあたりDB（hh_map_convert）を参照します<br>
     * 変換後のコードが存在する場合は"true"を、存在しない、もしくはエラーが発生した場合は"false"を返します<br>
     * 変換後のコードは"this.convertId"に格納されます<br>
     * 
     * @param map_id 昭文社の地図コード
     * @return コードの有無（true：変換後のコードあり、false：変換後のコードなし（もしくはエラー発生））
     */
    public boolean getData(String map_id)
    {
        // 変数
        String query = ""; // クエリ
        Connection connection = null; // DBコネクション
        PreparedStatement prestate = null; // プリコンパイルされたSQL文
        ResultSet result = null; // クエリの実行結果
        boolean ret = false; // 関数の戻り値（変換後のコードがあった場合のみtrueが入る）

        // クエリの作成
        query = "SELECT * FROM hh_map_convert WHERE map_id = ?";

        // DBからデータ取得
        try
        {
            int i = 1; // パラメータを順次挿入していくためのカウンタ
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( i++, map_id );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.convertId = result.getString( "convert_id" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMapConvert.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }
}
