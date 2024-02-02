package jp.happyhotel.search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelAdjustmentHistory;
import jp.happyhotel.data.DataHotelBasic;

/**
 * 新着ホテル取得クラス
 * 
 * @author koshiba-y1
 */
public class SearchHotelNewArrival
{
    /**
     * ホテル基本情報を格納した配列
     */
    private DataHotelBasic[]             hotelInfoArray;

    /**
     * ホテル変更履歴情報を格納した配列
     */
    private DataHotelAdjustmentHistory[] hotelAdjustmentHistoryArray;

    /**
     * 全新着ホテルのホテルIDを格納した配列
     */
    private int[]                        hotelIdArray;

    /**
     * コンストラクタ
     */
    public SearchHotelNewArrival()
    {
        initInstanceVariables();
    }

    /**
     * ホテル基本情報の件数の取得
     * 
     * @return ホテル基本情報を格納した配列の要素数
     */
    public int getCount()
    {
        return this.hotelInfoArray.length;
    }

    /**
     * 新着ホテルの全件数の取得
     * 
     * @return 新着ホテルの全件数
     */
    public int getAllCount()
    {
        return this.hotelIdArray.length;
    }

    /**
     * ホテル基本情報の取得
     * 
     * @return ホテル基本情報を格納した配列
     */
    public DataHotelBasic[] getHotelInfo()
    {
        return hotelInfoArray;
    }

    /**
     * ホテル変更履歴情報の取得
     * 
     * @return ホテル変更履歴情報を格納した配列
     */
    public DataHotelAdjustmentHistory[] getHotelAdjustment()
    {
        return hotelAdjustmentHistoryArray;
    }

    /**
     * 全新着ホテルのホテルIDを格納した配列の取得
     * 
     * @return 全新着ホテルのホテルIDを格納した配列
     */
    public int[] getHotelIdList()
    {
        return hotelIdArray;
    }

    /**
     * ホテル一覧情報取得<br>
     * <br>
     * 下記メソッドで得られる値を更新します。<br>
     * {@link #getCount()}<br>
     * {@link #getAllCount()}<br>
     * {@link #getHotelInfo()}<br>
     * {@link #getHotelAdjustment()}<br>
     * {@link #getHotelIdList()}<br>
     * <br>
     * {@code countNum}と{@code pageNum}は次のような関係にあります。<br>
     * ・{@code countNum}: {@code 3}, {@code pageNum}: {@code 1} の場合<br>
     * &emsp;・全レコード: {@code [1, 2, 3, 4, 5, 6, 7, 8, 9]} の場合<br>
     * &emsp;&emsp;取得対象: {@code [4, 5, 6]}<br>
     * &emsp;・全レコード: {@code [1, 2, 3, 4, 5]} の場合<br>
     * &emsp;&emsp;取得対象: {@code [4, 5]}<br>
     * &emsp;・全レコード: {@code [1, 2, 3]} の場合<br>
     * &emsp;&emsp;取得対象: {@code []}<br>
     * 
     * @param countNum 取得件数（{@code 0}：全件（{@code pageNum}は無視））
     * @param pageNum ページ番号（{@code 0}～）
     * @return 処理結果（{@code true}：正常、{@code false}：異常）
     */
    public boolean getHotelList(int countNum, int pageNum)
    {
        if ( countNum <= -1 || pageNum <= -1 )
        {
            initInstanceVariables();
            return false;
        }

        return getHotelList( null, null, countNum, pageNum );
    }

    /**
     * ホテル一覧情報取得<br>
     * <br>
     * 下記メソッドで得られる値を更新します。<br>
     * {@link #getCount()}<br>
     * {@link #getAllCount()}<br>
     * {@link #getHotelInfo()}<br>
     * {@link #getHotelAdjustment()}<br>
     * {@link #getHotelIdList()}<br>
     * <br>
     * {@code countNum}と{@code pageNum}は次のような関係にあります。<br>
     * ・{@code countNum}: {@code 3}, {@code pageNum}: {@code 1} の場合<br>
     * &emsp;・全レコード: {@code [1, 2, 3, 4, 5, 6, 7, 8, 9]} の場合<br>
     * &emsp;&emsp;取得対象: {@code [4, 5, 6]}<br>
     * &emsp;・全レコード: {@code [1, 2, 3, 4, 5]} の場合<br>
     * &emsp;&emsp;取得対象: {@code [4, 5]}<br>
     * &emsp;・全レコード: {@code [1, 2, 3]} の場合<br>
     * &emsp;&emsp;取得対象: {@code []}<br>
     * 
     * @param prefId 都道府県ID（{@code 0}：絞り込みなし）
     * @param countNum 取得件数（{@code 0}：全件（{@code pageNum}は無視））
     * @param pageNum ページ番号（{@code 0}～）
     * @return 処理結果（{@code true}：正常、{@code false}：異常）
     */
    public boolean getHotelListByPref(int prefId, int countNum, int pageNum)
    {
        if ( countNum <= -1 || pageNum <= -1 )
        {
            initInstanceVariables();
            return false;
        }

        return getHotelList( prefId, null, countNum, pageNum );
    }

    /**
     * ホテル一覧情報取得<br>
     * <br>
     * 下記メソッドで得られる値を更新します。<br>
     * {@link #getCount()}<br>
     * {@link #getAllCount()}<br>
     * {@link #getHotelInfo()}<br>
     * {@link #getHotelAdjustment()}<br>
     * {@link #getHotelIdList()}<br>
     * <br>
     * {@code countNum}と{@code pageNum}は次のような関係にあります。<br>
     * ・{@code countNum}: {@code 3}, {@code pageNum}: {@code 1} の場合<br>
     * &emsp;・全レコード: {@code [1, 2, 3, 4, 5, 6, 7, 8, 9]} の場合<br>
     * &emsp;&emsp;取得対象: {@code [4, 5, 6]}<br>
     * &emsp;・全レコード: {@code [1, 2, 3, 4, 5]} の場合<br>
     * &emsp;&emsp;取得対象: {@code [4, 5]}<br>
     * &emsp;・全レコード: {@code [1, 2, 3]} の場合<br>
     * &emsp;&emsp;取得対象: {@code []}<br>
     * 
     * @param hotelIdList ホテルIDを格納した配列（{@code null}：絞り込みなし、{@code []}：絞り込みあり）
     * @param countNum 取得件数（{@code 0}：全件（{@code pageNum}は無視））
     * @param pageNum ページ番号（{@code 0}～）
     * @return 処理結果（{@code true}：正常、{@code false}：異常）
     */
    public boolean getHotelListByHotelIdList(int[] hotelIdList, int countNum, int pageNum)
    {
        if ( countNum <= -1 || pageNum <= -1 )
        {
            initInstanceVariables();
            return false;
        }

        if ( hotelIdList != null && hotelIdList.length == 0 )
        {
            initInstanceVariables();
            return true;
        }

        return getHotelList( null, hotelIdList, countNum, pageNum );
    }

    /**
     * インスタンス変数の初期化
     */
    private void initInstanceVariables()
    {
        this.hotelInfoArray = new DataHotelBasic[0];
        this.hotelAdjustmentHistoryArray = new DataHotelAdjustmentHistory[0];
        this.hotelIdArray = new int[0];
    }

    /**
     * ホテル一覧情報取得（本体）<br>
     * <br>
     * 下記メソッドで得られる値を更新します。<br>
     * {@link #getCount()}<br>
     * {@link #getAllCount()}<br>
     * {@link #getHotelInfo()}<br>
     * {@link #getHotelAdjustment()}<br>
     * {@link #getHotelIdList()}<br>
     * <br>
     * {@code countNum}と{@code pageNum}は次のような関係にあります。<br>
     * ・{@code countNum}: {@code 3}, {@code pageNum}: {@code 1} の場合<br>
     * &emsp;・全レコード: {@code [1, 2, 3, 4, 5, 6, 7, 8, 9]} の場合<br>
     * &emsp;&emsp;取得対象: {@code [4, 5, 6]}<br>
     * &emsp;・全レコード: {@code [1, 2, 3, 4, 5]} の場合<br>
     * &emsp;&emsp;取得対象: {@code [4, 5]}<br>
     * &emsp;・全レコード: {@code [1, 2, 3]} の場合<br>
     * &emsp;&emsp;取得対象: {@code []}<br>
     * 
     * @param prefId 都道府県ID（{@code null} or {@code 0}：絞り込みなし）
     * @param hotelIdList ホテルIDを格納した配列（{@code null}：絞り込みなし、{@code []}：絞り込みあり）
     * @param countNum 取得件数（{@code 0}：全件（{@code pageNum}は無視））
     * @param pageNum ページ番号（{@code 0}～）
     * @return 処理結果（{@code true}：正常、{@code false}：異常）
     */
    private boolean getHotelList(final Integer prefId, final int[] hotelIdList, final int countNum, final int pageNum)
    {
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        try
        {
            connection = DBConnection.getConnection();
            String query = makeQuery( prefId, hotelIdList );
            prestate = connection.prepareStatement( query );
            prestate = insertQueryParam( prestate, prefId, hotelIdList );
            result = prestate.executeQuery();

            if ( result == null )
            {
                initInstanceVariables();
                return false;
            }

            // 全新着ホテルのホテルIDを格納した配列の作成
            this.hotelIdArray = makeHotelIdArray( result );

            // ホテル基本情報を格納した配列の作成
            this.hotelInfoArray = makeHotelInfoArray( result, countNum, pageNum );

            // ホテル変更履歴情報を格納した配列
            this.hotelAdjustmentHistoryArray = makeHotelAdjustmentHistoryArray( result, countNum, pageNum );
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelList] Exception=" + e.toString() );

            initInstanceVariables();
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return true;
    }

    /**
     * クエリ作成処理
     * 
     * @param prefId 都道府県ID（{@code null} or {@code 0}：絞り込みなし）
     * @param hotelIds ホテルIDを格納した配列（{@code null}：絞り込みなし、{@code []}：絞り込みあり）
     * @return クエリ
     */
    private static String makeQuery(final Integer prefId, final int[] hotelIds)
    {
        String query = "";

        query += "SELECT ";
        query += "    tmp.* ";
        query += "FROM ( ";
        query += "    SELECT ";
        query += "        hh_hotel_adjustment_history.* ";

        // カラムをカンマで区切って、カラムに「hh_hotel_basic_」で始まる別名を付ける
        for( String column : DataHotelBasic.COLUMNS.split( "," ) )
        {
        	if ("empty_kind".equals(column.toLowerCase())) {
                query += "        ,IFNULL(hh_hotel_status.`mode`, 0)";
        	} else if ("empty_status".equals(column.toLowerCase())) {
                query += "        ,IFNULL(hh_hotel_status." + column + ", 0)";
        	} else {
                query += "        ,hh_hotel_basic." + column;
        	}
            query += " hh_hotel_basic_" + column + "";
        }

        query += "    FROM ";
        query += "        hh_hotel_adjustment_history ";
        query += "    INNER JOIN ";
        query += "        hh_hotel_basic ";
        query += "        ON hh_hotel_adjustment_history.id = hh_hotel_basic.id ";
        query += "        AND hh_hotel_basic.kind <= 7 ";
        query += "        AND hh_hotel_basic.rank != 0 ";

        // 都道府県による絞り込みありの場合
        if ( prefId != null && prefId > 0 )
        {
            query += "        AND hh_hotel_basic.pref_id = ? ";
        }

        // ホテルIDによる絞り込みありの場合
        if ( hotelIds != null && hotelIds.length > 0 )
        {
            query += "        AND hh_hotel_basic.id IN(";
            for( int i = 0 ; i < hotelIds.length ; i++ )
            {
                query += "?";
                if ( i < hotelIds.length - 1 )
                {
                    query += ", ";
                }
            }
            query += ") ";
        }
        
        query += "    LEFT JOIN hh_hotel_status";
        query += "        ON hh_hotel_basic.id = hh_hotel_status.id";

        query += "    WHERE ";
        query += "        (edit_id = 101 AND edit_sub = 0) OR (edit_id = 102 AND edit_sub = 0) ";
        query += "    ORDER BY ";
        query += "        input_date DESC, ";
        query += "        input_time DESC ";
        query += ") AS tmp ";
        query += "GROUP BY ";
        query += "    tmp.id ";
        query += "ORDER BY ";
        query += "    tmp.input_date DESC, ";
        query += "    tmp.input_time DESC"; // this.hotelIdArrayに全件分のデータを入れる必要があるため、LIMIT句は使用しない

        return query;
    }

    /**
     * クエリパラメータ設定処理
     * 
     * @param prestate クエリが設定された{@code PreparedStatement}オブジェクト
     * @param prefId 都道府県ID（{@code null} or {@code 0}：絞り込みなし）
     * @param hotelIds ホテルIDを格納した配列（{@code null}：絞り込みなし、{@code []}：絞り込みあり）
     * @return パラメータが挿入された{@code PreparedStatement}オブジェクト
     * @throws SQLException DB関連のエラーが発生した場合
     */
    private static PreparedStatement insertQueryParam(
            final PreparedStatement prestate, final Integer prefId, final int[] hotelIds)
            throws SQLException
    {
        int i = 1;

        // 都道府県による絞り込みありの場合
        if ( prefId != null && prefId > 0 )
        {
            prestate.setInt( i++, prefId );
        }

        // ホテルIDによる絞り込みありの場合
        if ( hotelIds != null && hotelIds.length > 0 )
        {
            for( int hotelId : hotelIds )
            {
                prestate.setInt( i++, hotelId );
            }
        }

        return prestate;
    }

    /**
     * 全新着ホテルのホテルIDを格納した配列の作成
     * 
     * @param result 検索結果を格納した{@code ResultSet}オブジェクト
     * @return 全新着ホテルのホテルIDを格納した配列
     * @throws SQLException DB関連のエラーが発生した場合
     */
    private static int[] makeHotelIdArray(final ResultSet result) throws SQLException
    {
        final int numberOfRecords;
        if ( result.last() )
        {
            numberOfRecords = result.getRow();
        }
        else
        {
            numberOfRecords = 0;
        }

        result.beforeFirst();

        int[] hotelIdArray = new int[numberOfRecords];

        int rowCount = 0;
        while( result.next() )
        {
            hotelIdArray[rowCount] = result.getInt( "id" );
            rowCount++;
        }

        return hotelIdArray;
    }

    /**
     * ホテル基本情報を格納した配列の作成
     * 
     * @param result 検索結果を格納した{@code ResultSet}オブジェクト
     * @param countNum 取得件数（{@code 0}：全件（{@code pageNum}は無視））
     * @param pageNum ページ番号（{@code 0}～）
     * @return ホテル基本情報を格納した配列
     * @throws SQLException DB関連のエラーが発生した場合
     */
    private static DataHotelBasic[] makeHotelInfoArray(
            final ResultSet result, final int countNum, final int pageNum)
            throws SQLException
    {
        final int rowJump = countNum * pageNum;
        if ( rowJump == 0 )
        {
            result.beforeFirst();
        }
        else
        {
            result.absolute( rowJump );
        }

        List<DataHotelBasic> hotelInfoList = new ArrayList<DataHotelBasic>();

        int rowCount = 1;
        while( result.next() )
        {
            DataHotelBasic hotelInfo = new DataHotelBasic();
            hotelInfo.setData( result, "hh_hotel_basic_" );
            hotelInfoList.add( hotelInfo );

            // countNumが0の場合は絞り込みなし
            if ( countNum != 0 && rowCount >= countNum )
            {
                break;
            }

            rowCount++;
        }

        return hotelInfoList.toArray( new DataHotelBasic[hotelInfoList.size()] );
    }

    /**
     * ホテル変更履歴情報を格納した配列の作成
     * 
     * @param result 検索結果を格納した{@code ResultSet}オブジェクト
     * @param countNum 取得件数（{@code 0}：全件（{@code pageNum}は無視））
     * @param pageNum ページ番号（{@code 0}～）
     * @return ホテル変更履歴情報を格納した配列の作成
     * @throws SQLException DB関連のエラーが発生した場合
     */
    private static DataHotelAdjustmentHistory[] makeHotelAdjustmentHistoryArray(
            final ResultSet result, final int countNum, final int pageNum)
            throws SQLException
    {
        final int rowJump = countNum * pageNum;
        if ( rowJump == 0 )
        {
            result.beforeFirst();
        }
        else
        {
            result.absolute( rowJump );
        }

        List<DataHotelAdjustmentHistory> hotelAdjustmentHistoryList = new ArrayList<DataHotelAdjustmentHistory>();

        int rowCount = 1;
        while( result.next() )
        {
            DataHotelAdjustmentHistory hotelAdjustmentHistory = new DataHotelAdjustmentHistory();
            hotelAdjustmentHistory.setData( result );
            hotelAdjustmentHistoryList.add( hotelAdjustmentHistory );

            // countNumが0の場合は絞り込みなし
            if ( countNum != 0 && rowCount >= countNum )
            {
                break;
            }

            rowCount++;
        }

        return hotelAdjustmentHistoryList.toArray( new DataHotelAdjustmentHistory[hotelAdjustmentHistoryList.size()] );
    }
}
