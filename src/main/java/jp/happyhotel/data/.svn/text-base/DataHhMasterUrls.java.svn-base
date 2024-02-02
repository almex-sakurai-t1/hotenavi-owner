package jp.happyhotel.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 外部サービス定義データのコレクションクラス<br>
 * <br>
 * 外部サービス（Facebook、GoogleMap、LINE@、etc...）の定義データのコレクションクラスです。<br>
 * 外部サービスごとにデータを取得すると、クエリの実行回数が増えてしまい、レスポンスが悪くなると思ったので作成しました。<br>
 * 
 * @author koshiba-y1
 */
public class DataHhMasterUrls extends ArrayList<DataHhMasterUrl>
{
    /**
     * コンストラクタ
     */
    public DataHhMasterUrls()
    {
        // 特になし
    }

    /**
     * hh_muster_url内の全データの一括取得および自身への格納
     * 
     * @return 処理結果
     * @throws Exception
     */
    public boolean selectAllData() throws Exception
    {
        // DBアクセス関連
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        try
        {
            // テーブルの全データを取得
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( "SELECT * FROM hh_master_url" );
            result = prestate.executeQuery();

            if ( result == null )
            {
                return false;
            }

            // 各行を自身に格納していく
            while( result.next() )
            {
                DataHhMasterUrl dhmu = new DataHhMasterUrl();
                dhmu.setData( result );
                this.add( dhmu );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhMasterUrls.selectAllData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return true;
    }
}
