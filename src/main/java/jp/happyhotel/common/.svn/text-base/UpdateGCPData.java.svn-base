package jp.happyhotel.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.happyhotel.hotel.HotelCount;

/*
 * GCP上ホテルデータ情報を更新するクラス
 */
public class UpdateGCPData
{
    private static final String schemaHotenavi = "hotenavi";
    private static final String schemaNewRsv   = "newRsvDB";

    /**
     * GCPデータ同期
     * 
     * @param hotelid ホテルID
     * @param isStg true:GCPステージング環境特定 false:ハピホテ環境ごとに更新
     * @return
     */
    public String updateData(int hotelid) throws IOException
    {
        Logging.info( "updateData!" );
        StringBuilder sb = new StringBuilder();
        Connection connection = null;
        DatabaseMetaData dbMetaData = null;

        // データ同期テーブル(primaryKey = id)
        String[] tableListPrimaryKeyId = new String[20];
        int a = 0;
        tableListPrimaryKeyId[a++] = "hh_hotel_chain";
        tableListPrimaryKeyId[a++] = "hh_hotel_gallery";
        tableListPrimaryKeyId[a++] = "hh_hotel_newhappie";
        tableListPrimaryKeyId[a++] = "hh_hotel_room_more";
        tableListPrimaryKeyId[a++] = "hh_hotel_roomrank";
        tableListPrimaryKeyId[a++] = "hh_rsv_reserve_basic";

        // データ同期テーブル(primaryKey = hotenavi_id)
        String[] tableListlPrimaryKeyHotenaviId = new String[3];
        String[] hotenaviKeyName = new String[3];
        a = 0;
        hotenaviKeyName[a] = "hotel_id";
        tableListlPrimaryKeyHotenaviId[a++] = "hotel";
        hotenaviKeyName[a] = "hotelid";
        tableListlPrimaryKeyHotenaviId[a++] = "roomrank";
        hotenaviKeyName[a] = "hotelid";
        tableListlPrimaryKeyHotenaviId[a++] = "price";

        // データ同期テーブル(update専用)
        // String[] tableListUpdate = new String[1];
        // a = 0;
        // tableListUpdate[a++] = "hh_hotel_count";

        // データ入れ替えテーブル(delete,insert)
        List<String> deleteInsertList = new ArrayList<String>();
        deleteInsertList.add( "hh_hotel_roomrank" );
        deleteInsertList.add( "hh_hotel_room_more" );
        deleteInsertList.add( "roomrank" );
        deleteInsertList.add( "price" );

        try
        {
            connection = DBConnection.getConnection();
            dbMetaData = connection.getMetaData();
            String hotenaviId = getHotenaviId( hotelid, connection );

            // ReplaceSQL(primaryKey=id)
            sb.append( getReplaceSqlPrimaryKeyId( hotelid, tableListPrimaryKeyId, deleteInsertList, schemaHotenavi, connection, dbMetaData ) );

            // ReplaceSQL(primaryKey=hotenavi_id)
            sb.append( getReplaceSqlPrimaryKeyHotenaviId( hotenaviId, tableListlPrimaryKeyHotenaviId, hotenaviKeyName, deleteInsertList, schemaHotenavi, connection, dbMetaData ) );

            // UpdateSQL
            // sb.append( getUpdateSql( hotelid, tableListUpdate, schemaHotenavi, connection, dbMetaData ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[UpdateGCPData.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return sb.toString();
    }

    /**
     * ホテル情報データ更新
     * 
     * @param id ホテルID
     * @staff_id 社員ID（調査用フォームログイン）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateHotel(int id, String staff_id)
    {
        boolean ret;

        ret = HotelCount.setData( id );/* 送信前に、対象ホテルについてのフラグを再計算する */

        if ( ret )
        {
            String query;

            Connection connection = null;
            PreparedStatement prestate = null;
            ResultSet result = null;

            ret = false;
            query = "UPDATE hh_hotel_basic SET ";
            query = query + " last_update = ?,";
            query = query + " last_uptime = ?";
            query = query + " WHERE id = ?";

            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                // 更新対象の値をセットする
                prestate.setInt( 1, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                prestate.setInt( 2, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                prestate.setInt( 3, id );
                int prestateUpdate = prestate.executeUpdate();
                if ( prestateUpdate > 0 )
                {
                    ret = true;
                }
                DBConnection.releaseResources( prestate );

                if ( ret )
                {
                    int user_id = 0;
                    query = "SELECT userid FROM owner_user WHERE hotelid='happyhotel'";
                    query = query + " AND  loginid= ?";
                    prestate = connection.prepareStatement( query );
                    prestate.setString( 1, staff_id );
                    result = prestate.executeQuery();
                    if ( result.next() )
                    {
                        user_id = result.getInt( "userid" );
                    }
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );
                    if ( user_id == 0 )
                    {
                        if ( CheckString.numCheck( staff_id ) )
                        {
                            user_id = Integer.parseInt( staff_id );
                        }
                    }
                    query = "INSERT INTO hh_hotel_adjustment_history SET ";
                    query = query + "id=?, ";
                    query = query + "hotel_id='happyhotel', ";
                    query = query + "user_id=?, ";
                    query = query + "edit_id=1000, ";
                    query = query + "input_date=?, ";
                    query = query + "input_time=?, ";
                    query = query + "memo='→GCPに情報更新'";
                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, id );
                    prestate.setInt( 2, user_id );
                    prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    prestate.setInt( 4, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    prestate.executeUpdate();
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[DataHotelBasic.updateHotel] Exception=" + e.toString(), "DataHotelBasic" );
                ret = false;
            }
            finally
            {
                DBConnection.releaseResources( prestate );
                DBConnection.releaseResources( connection );
            }
        }
        return(ret);
    }

    /**
     * GCPデータ同期Replace文(PrimaryKey = id)
     * 
     * @param hotelid ホテルID
     * @param tableList テーブルリスト
     * @param deleteInsertList deleteInsertを行うテーブルリスト
     * @param schema schema(hotenavi、 newRsvDB)
     * @param connection
     * @param dbMetaData
     * @return GCP同期用SQL文
     */
    public String getReplaceSqlPrimaryKeyId(int hotelid, String[] tableList, List<String> deleteInsertList, String schema,
            Connection connection, DatabaseMetaData dbMetaData) throws IOException
    {
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            for( int i = 0 ; i < tableList.length ; i++ )
            {
                boolean isInsert = false;
                int numSelectResult = 0;
                if ( deleteInsertList.contains( tableList[i] ) )
                {
                    sb.append( "DELETE FROM " )
                            .append( schema ).append( "." )
                            .append( tableList[i] )
                            .append( " WHERE id=" )
                            .append( hotelid )
                            .append( ";" );
                    isInsert = true;
                }
                query = "SELECT * FROM " + schema + "." + tableList[i] + " WHERE id = ?";
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, hotelid );
                result = prestate.executeQuery();
                while( result.next() )
                {
                    sb.append( getSqlForSingleTable( isInsert, tableList[i], schema, result, connection, dbMetaData ) );
                    numSelectResult++;
                }
                if ( numSelectResult == 0 )
                {
                    sb.append( "DELETE FROM " )
                            .append( schema ).append( "." )
                            .append( tableList[i] )
                            .append( " WHERE id=" )
                            .append( hotelid )
                            .append( ";" );
                }
                DBConnection.releaseResources( result );
                DBConnection.releaseResources( prestate );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UpdateGCPData.getUpdateSQL] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return sb.toString();
    }

    /**
     * GCPデータ同期Replace文(PrimaryKey = hotenavi_id)
     * 
     * @param hotenaviId ホテナビID
     * @param tableList テーブルリスト
     * @param deleteInsertList deleteInsertを行うテーブルリスト
     * @param schema schema(hotenavi、 newRsvDB)
     * @param connection
     * @param dbMetaData
     * @return GCP同期用SQL文
     */
    public String getReplaceSqlPrimaryKeyHotenaviId(String hotenaviId, String[] tableList, String[] keyName, List<String> deleteInsertList, String schema,
            Connection connection, DatabaseMetaData dbMetaData) throws IOException
    {
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            for( int i = 0 ; i < tableList.length ; i++ )
            {
                boolean isInsert = false;
                int numSelectResult = 0;
                if ( deleteInsertList.contains( tableList[i] ) )
                {
                    sb.append( "DELETE FROM " )
                            .append( schema ).append( "." )
                            .append( tableList[i] )
                            .append( " WHERE " + keyName[i] + "=" )
                            .append( "\"" )
                            .append( hotenaviId )
                            .append( "\";" );
                    isInsert = true;
                }
                query = "SELECT * FROM " + schema + "." + tableList[i] + " WHERE " + keyName[i] + " = ?";
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, hotenaviId );
                result = prestate.executeQuery();
                while( result.next() )
                {
                    sb.append( getSqlForSingleTable( isInsert, tableList[i], schema, result, connection, dbMetaData ) );
                    numSelectResult++;
                }
                if ( numSelectResult == 0 )
                {
                    sb.append( "DELETE FROM " )
                            .append( schema ).append( "." )
                            .append( tableList[i] )
                            .append( " WHERE " + keyName[i] + "=" )
                            .append( "\"" )
                            .append( hotenaviId )
                            .append( "\";" );
                }
                DBConnection.releaseResources( result );
                DBConnection.releaseResources( prestate );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UpdateGCPData.getUpdateSQL] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return sb.toString();
    }

    /**
     * GCPデータ同期Update文(PrimaryKey = id)
     * 
     * @param hotelid ホテルID
     * @param tableList テーブルリスト
     * @param schema schema(hotenavi、 newRsvDB)
     * @param connection
     * @param dbMetaData
     * @return GCP同期用SQL文
     */
    public String getUpdateSql(int hotelid, String[] tableList, String schema,
            Connection connection, DatabaseMetaData dbMetaData) throws IOException
    {
        String query;
        ResultSet result = null;
        ResultSet column = null;
        PreparedStatement prestate = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            for( int i = 0 ; i < tableList.length ; i++ )
            {
                int numSelectResult = 0;
                query = "SELECT * FROM " + schema + "." + tableList[i] + " WHERE id = ?";
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, hotelid );
                result = prestate.executeQuery();
                while( result.next() )
                {
                    int numColumn = 0;
                    // 指定されたカタログで使用可能なテーブル列の記述を取得する。
                    column = dbMetaData.getColumns( connection.getCatalog(), schema, tableList[i], "%" );
                    // UPDATE
                    sb.append( "UPDATE " )
                            .append( schema ).append( "." )
                            .append( tableList[i] ).append( " SET " );
                    while( column.next() )
                    {
                        // 項目名
                        String columnName = column.getString( "COLUMN_NAME" );
                        if ( columnName.equals( "id" ) || columnName.equals( "sort_key" ) )
                        {
                            continue;
                        }
                        // 項目データ型
                        String columnType = column.getString( "TYPE_NAME" );
                        if ( numColumn > 0 )
                        {
                            sb.append( "," );
                        }
                        sb.append( columnName )
                                .append( "=" )
                                .append( getColumnData( columnName, columnType, result ) );
                        numColumn++;
                    }
                    sb.append( " WHERE id=" )
                            .append( hotelid )
                            .append( ";" );
                    DBConnection.releaseResources( column );
                    numSelectResult++;
                }
                if ( numSelectResult == 0 )
                {
                    sb.append( "DELETE FROM " )
                            .append( schema ).append( "." )
                            .append( tableList[i] )
                            .append( " WHERE id = " )
                            .append( hotelid )
                            .append( ";" );
                }
                DBConnection.releaseResources( result );
                DBConnection.releaseResources( prestate );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UpdateGCPData.getUpdateSql] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return sb.toString();
    }

    /**
     * ホテナビID取得
     * 
     * @param hotelid ホテルID
     * @param connection
     * @return hotenavi_id ホテナビID
     */
    public String getHotenaviId(int hotelid, Connection connection)
    {
        ResultSet result = null;
        PreparedStatement prestate = null;
        String hotenaviId = "";
        try
        {

            String query = "SELECT hotenavi_id FROM hotenavi.hh_hotel_basic WHERE id = ?";
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelid );
            result = prestate.executeQuery();
            if ( result.next() )
            {
                hotenaviId = result.getString( "hotenavi_id" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UpdateGCPData.getHotenaviId] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return hotenaviId;
    }

    /**
     * 項目のデータ値取得
     * 
     * @param columnName 項目名
     * @param columnType 項目データ型
     * @param result
     * @return 項目データ
     */
    private String getColumnData(String columnName, String columnType, ResultSet result) throws SQLException
    {
        StringBuilder sb = new StringBuilder();
        // int型
        if ( columnType.indexOf( "INT" ) != -1 )
        {
            sb.append( result.getInt( columnName ) );
        }
        // float型
        else if ( columnType.indexOf( "FLOAT" ) != -1 )
        {
            sb.append( result.getFloat( columnName ) );
        }
        // String型
        else if ( columnType.indexOf( "CHAR" ) != -1 || columnType.indexOf( "TEXT" ) != -1 )
        {
            String columns = result.getString( columnName );
            if ( columns == null || columns.equals( "" ) )
            {
                sb.append( "\"\"" );
            }
            else
            {
                if ( columnName.equals( "front_ip" ) )
                {
                    columns = HotelIp.convertIP( columns );
                }
                sb.append( "\"" )
                        .append( columns.replace( "\\", "\\\\" ).replace( "\"", "\\\"" ) )
                        .append( "\"" );
            }

        }
        // LONGBLOB型(.jpg/.gifなど)
        else if ( columnType.indexOf( "BLOB" ) != -1 )
        {
            sb.append( "NULL" );
        }
        // Date型
        else if ( columnType.indexOf( "DATE" ) != -1 )
        {
            sb.append( "\"" )
                    .append( result.getDate( columnName ).toString() )
                    .append( "\"" );
        }
        else
        {
            sb.append( "\"\"" );
        }
        return sb.toString();
    }

    /**
     * 一つのテーブルに対してのSQL文取得
     * 
     * @param isInsert (true:delete後insert false:replace)
     * @param table テーブル
     * @param schema schema(hotenavi、 newRsvDB)
     * @param result ResultSet
     * @param connection Connection
     * @param dbMetaData DatabaseMetaData
     * @return SQL文
     */
    private String getSqlForSingleTable(boolean isInsert, String table, String schema,
            ResultSet result, Connection connection, DatabaseMetaData dbMetaData)
    {
        StringBuilder sb = new StringBuilder();
        ResultSet column = null;
        try
        {
            // 指定されたカタログで使用可能なテーブル列の記述を取得する。
            column = dbMetaData.getColumns( connection.getCatalog(), schema, table, "%" );
            // delete後insertを行う
            if ( isInsert )
            {
                sb.append( "INSERT INTO " );
            }
            else
            {
                sb.append( "REPLACE INTO " );
            }
            sb.append( schema ).append( "." ).append( table ).append( " SET " );
            int numColumn = 0;
            while( column.next() )
            {
                // 項目名
                String columnName = column.getString( "COLUMN_NAME" );
                if ( columnName.equals( "start_time" ) || columnName.equals( "end_time" ) )
                {
                    continue;
                }
                // 項目データ型
                String columnType = column.getString( "TYPE_NAME" );
                if ( numColumn > 0 )
                {
                    sb.append( "," );
                }
                sb.append( columnName )
                        .append( "=" )
                        .append( getColumnData( columnName, columnType, result ) );
                numColumn++;
            }
            sb.append( ";" );
        }
        catch ( SQLException e )
        {
            Logging.error( "[UpdateGCPData.getSingleTableSql] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( column );
        }
        return sb.toString();
    }
}
