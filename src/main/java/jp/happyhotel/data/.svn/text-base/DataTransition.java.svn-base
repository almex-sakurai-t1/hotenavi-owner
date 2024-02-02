package jp.happyhotel.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class DataTransition
{

    private String     sourceUserID;
    private String     destinationUserID;

    /**
     * insert, update, delete用connection
     * エラーが発生した場合、前実行された内容が一気に復元するため
     */
    private Connection connection;

    public DataTransition(String sourceUserID, String destinationUserID)
    {
        this.sourceUserID = sourceUserID;
        this.destinationUserID = destinationUserID;
    }

    public void transitionData() throws Exception
    {

        try
        {

            this.connection = DBConnection.getConnection();

            transitionNumberingData();

            transitionData( "hh_user_point_pay_temp", "hh_bko_account_recv_detail" );
            transitionData( "hh_user_point_pay", "hh_user_point_pay_used" );
            transitionData( "hh_user_myhotel" );
            transitionData( "ap_hotel_custom" );

            transitionReserveData();

            updateApTouchUserPoint();

        }
        catch ( Exception e )
        {
            Logging.error( "DataTransition.transitionData Exceptionが発生", e );
            this.connection.rollback();
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( this.connection );
        }
    }

    private void transitionReserveData() throws Exception
    {
        transitionReserveData( "hh_rsv_reserve" );
        transitionReserveData( "hh_rsv_reserve_history" );
        // transitionReserveData("hh_rsv_credit");
    }

    /**
     * 予約データ移行
     * 
     * @param tableName
     * @throws Exception
     */
    private void transitionReserveData(String tableName) throws Exception
    {

        PreparedStatement prestate = null;

        StringBuilder querySb = null;

        try
        {

            querySb = new StringBuilder();
            querySb.append( "UPDATE newRsvDB." ).append( tableName ).append( " SET" );
            querySb.append( " user_id = ?" );
            querySb.append( " WHERE user_id = ?" );

            prestate = this.connection.prepareStatement( querySb.toString() );
            prestate.setString( 1, destinationUserID );
            prestate.setString( 2, sourceUserID );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "DataTransition.transitionReserveData Exceptionが発生", e );
            throw e;
        }
        finally
        {

            DBConnection.releaseResources( prestate );

            querySb = null;

        }

    }

    /**
     * 関連データ移行
     * 
     * @param baseTableName
     * @param updateTableName
     * @throws Exception
     */
    private void transitionData(String baseTableName, String updateTableName) throws Exception
    {

        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        ResultSetMetaData rsmd = null;
        StringBuilder querySb = null;

        int max_seq = 0;

        StringBuilder insertSql = null;
        StringBuilder selectSql = null;
        ArrayList<String> selectParamList = null;
        String colName = null;

        Map<Integer, Integer> seqMap = null;
        StringBuilder updateSql = null;

        try
        {

            // 移行先最大seqを取得
            querySb = new StringBuilder();
            querySb.append( "SELECT MAX(seq) max_seq FROM " + baseTableName + " WHERE user_id = ?" );

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( querySb.toString() );
            prestate.setString( 1, destinationUserID );
            result = prestate.executeQuery();

            if ( result.next() )
            {
                max_seq = result.getInt( "max_seq" );
            }

            // 移行元のデータを取得
            querySb = new StringBuilder();
            querySb.append( "SELECT * FROM " + baseTableName + " WHERE user_id = ? ORDER BY seq" );

            prestate = connection.prepareStatement( querySb.toString() );
            prestate.setString( 1, sourceUserID );
            result = prestate.executeQuery();

            // テーブルのフィールドを全取得
            rsmd = result.getMetaData();

            // 元のseqをキーとして、新しいseqをmapに格納
            seqMap = new HashMap<Integer, Integer>();
            while( result.next() )
            {
                seqMap.put( result.getInt( "seq" ), result.getRow() + max_seq );
            }

            int colCount = rsmd.getColumnCount();
            insertSql = new StringBuilder();
            selectSql = new StringBuilder();
            selectParamList = new ArrayList<String>();
            insertSql.append( "INSERT INTO " + baseTableName + " (" );
            selectSql.append( "SELECT " );

            for( int i = 1 ; i <= colCount ; i++ )
            {
                colName = rsmd.getColumnName( i );
                if ( i > 1 )
                {
                    insertSql.append( ", " );
                    selectSql.append( ", " );
                }
                insertSql.append( colName );
                if ( "user_id".equals( colName.toLowerCase() ) )
                {
                    selectSql.append( "?" );
                    selectParamList.add( destinationUserID );
                }
                else if ( "seq".equals( colName.toLowerCase() ) )
                {
                    selectSql.append( "((@i:=@i+1) + " + max_seq + ")" );
                }
                else
                {
                    selectSql.append( colName );
                }
            }
            insertSql.append( ") " );
            selectSql.append( " FROM (SELECT @i:=0) AS index_num, " + baseTableName + " WHERE user_id = ?" );
            selectParamList.add( sourceUserID );

            // insertとselect構文を合併
            insertSql.append( selectSql.toString() );

            // 移行元からデータを検出し、移行先に挿入
            prestate = this.connection.prepareStatement( insertSql.toString() );
            for( int paramIndex = 1 ; paramIndex <= selectParamList.size() ; paramIndex++ )
            {
                prestate.setString( paramIndex, selectParamList.get( paramIndex - 1 ) );
            }
            prestate.executeUpdate();

            // 移行元のデータを削除
            prestate = this.connection.prepareStatement( "DELETE FROM " + baseTableName + " WHERE user_id = ?" );
            prestate.setString( 1, sourceUserID );
            prestate.executeUpdate();

            // 元のseqと移行元ユーザーを新しいseqと移行先ユーザーに変更
            for( Iterator<Integer> it = seqMap.keySet().iterator() ; it.hasNext() ; )
            {
                int ori_seq = it.next();
                int new_seq = seqMap.get( ori_seq );
                updateSql = new StringBuilder();
                updateSql.append( "UPDATE " + updateTableName + " SET" );
                updateSql.append( " user_id = ?" );
                updateSql.append( ", seq = ?" );
                updateSql.append( " WHERE user_id = ?" );
                updateSql.append( " AND seq = ?" );

                prestate = this.connection.prepareStatement( updateSql.toString() );
                prestate.setString( 1, destinationUserID );
                prestate.setInt( 2, new_seq );
                prestate.setString( 3, sourceUserID );
                prestate.setInt( 4, ori_seq );
                prestate.executeUpdate();
            }

        }
        catch ( Exception e )
        {
            Logging.error( "DataTransition.transitionData(string, string) Exceptionが発生", e );
            throw e;
        }
        finally
        {

            DBConnection.releaseResources( result, prestate, connection );

            rsmd = null;
            querySb = null;
            insertSql = null;
            selectSql = null;
            selectParamList = null;
            colName = null;
            seqMap = null;
            updateSql = null;

        }
    }

    /**
     * データ移行
     * 
     * @param baseTableName
     * @throws Exception
     */
    private void transitionData(String baseTableName) throws Exception
    {

        PreparedStatement prestate = null;

        StringBuilder querySb = null;

        try
        {

            querySb = new StringBuilder();

            querySb.append( "UPDATE " + baseTableName + " a" );
            querySb.append( ", (" );
            querySb.append( " SELECT" );
            querySb.append( "  *" );
            querySb.append( " FROM " + baseTableName + " a" );
            querySb.append( " WHERE user_id = ?" );
            querySb.append( " AND NOT EXISTS (" );
            querySb.append( "  SELECT 'X' FROM " + baseTableName + " b" );
            querySb.append( "  WHERE a.id = b.id" );
            querySb.append( "  AND user_id = ?" );
            querySb.append( " )" );
            querySb.append( ") b" );
            querySb.append( " SET a.user_id = ?" );
            querySb.append( " WHERE a.user_id = b.user_id" );
            querySb.append( " AND a.id = b.id" );

            prestate = this.connection.prepareStatement( querySb.toString() );
            prestate.setString( 1, sourceUserID );
            prestate.setString( 2, destinationUserID );
            prestate.setString( 3, destinationUserID );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "DataTransition.transitionData(string) Exceptionが発生", e );
            throw e;
        }
        finally
        {

            DBConnection.releaseResources( prestate );

            querySb = null;

        }

    }

    /**
     * user_seqの付番処理
     * 
     * @throws Exception
     */
    private void transitionNumberingData() throws Exception
    {

        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        StringBuilder querySb = null;

        Map<String, Map<Integer, Integer>> userDataIndexMap = null;
        Map<Integer, Integer> idSeqMap = null;

        Map<Integer, Integer> sourceUserIdSeqMap = null;
        Map<Integer, Integer> destinationUserIdSeqMap = null;

        String user_id = null;

        try
        {

            querySb = new StringBuilder();
            querySb.append( "SELECT" );
            querySb.append( " user_id" );
            querySb.append( ", id" );
            querySb.append( ", user_seq" );
            querySb.append( " FROM hh_user_data_index" );
            querySb.append( " WHERE user_id IN (?, ?)" );

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( querySb.toString() );
            prestate.setString( 1, sourceUserID );
            prestate.setString( 2, destinationUserID );
            result = prestate.executeQuery();

            userDataIndexMap = new HashMap<String, Map<Integer, Integer>>();

            while( result.next() )
            {
                user_id = result.getString( "user_id" );
                int hotel_id = result.getInt( "id" );
                idSeqMap = userDataIndexMap.get( user_id );
                if ( idSeqMap == null )
                {
                    idSeqMap = new HashMap<Integer, Integer>();
                }
                idSeqMap.put( hotel_id, result.getInt( "user_seq" ) );
                userDataIndexMap.put( user_id, idSeqMap );
            }

            sourceUserIdSeqMap = userDataIndexMap.get( sourceUserID );
            destinationUserIdSeqMap = userDataIndexMap.get( destinationUserID );

            for( Iterator<Integer> keyIt = sourceUserIdSeqMap.keySet().iterator() ; keyIt.hasNext() ; )
            {

                int hotel_id = keyIt.next();

                int source_user_seq, destination_user_seq;

                source_user_seq = sourceUserIdSeqMap.get( hotel_id );

                if ( destinationUserIdSeqMap.containsKey( hotel_id ) )
                { // 移行先登録済み
                    destination_user_seq = destinationUserIdSeqMap.get( hotel_id );
                    int maxVisitSeq = getMaxVisitSeq( destination_user_seq );
                    numberingPointPay( source_user_seq, destination_user_seq, maxVisitSeq, "hh_user_point_pay" );
                    numberingPointPay( source_user_seq, destination_user_seq, maxVisitSeq, "hh_user_point_pay_temp" );
                    numberingCiTable( hotel_id, source_user_seq, destination_user_seq, maxVisitSeq, "hh_hotel_ci" );
                    numberingCiTable( hotel_id, source_user_seq, destination_user_seq, maxVisitSeq, "ap_touch_ci" );
                }
                else
                {
                    if ( updateUserDataIndex( hotel_id ) )
                    {
                        numberingCiTable( hotel_id, source_user_seq, "hh_hotel_ci" );
                        numberingCiTable( hotel_id, source_user_seq, "ap_touch_ci" );
                    }
                }

            }

        }
        catch ( Exception e )
        {
            Logging.error( "DataTransition.transitionNumberingData(string) Exceptionが発生", e );
            throw e;
        }
        finally
        {

            DBConnection.releaseResources( result, prestate, connection );

            querySb = null;

        }

    }

    /**
     * hh_user_data_indexのデータ更新
     * 
     * @param id
     * @return
     * @throws Exception
     */
    private boolean updateUserDataIndex(int id) throws Exception
    {

        boolean ret = false;

        PreparedStatement prestate = null;
        StringBuilder querySb = null;

        try
        {

            querySb = new StringBuilder();
            querySb.append( "UPDATE hh_user_data_index SET" );
            querySb.append( " user_id = ?" );
            querySb.append( " WHERE user_id = ?" );
            querySb.append( " AND id = ?" );

            prestate = this.connection.prepareStatement( querySb.toString() );
            prestate.setString( 1, destinationUserID );
            prestate.setString( 2, sourceUserID );
            prestate.setInt( 3, id );
            int result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "DataTransition.numberingCiTable Exceptionが発生", e );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return ret;

    }
    
    private int getMaxVisitSeq(int destination_user_seq) throws Exception
    {
        
        int returnVal = 0;
        
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        
        StringBuilder querySb = null;
        
        try
        {
            
            querySb = new StringBuilder();
            querySb.append( "SELECT max(visit_seq) visit_seq FROM hh_user_point_pay" );
            querySb.append( " WHERE user_id = ?" );
            querySb.append( " AND user_seq = ?" );
            
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( querySb.toString() );
            prestate.setString( 1, destinationUserID );
            prestate.setInt( 2, destination_user_seq );
            result = prestate.executeQuery();
            
            if ( result.next() )
            {
                returnVal = result.getInt( "visit_seq" );
            }
            
        }
        catch ( Exception e )
        {
            Logging.error( "DataTransition.getMapVisitSeq Exceptionが発生", e );
            throw e;
        }
        finally
        {

            DBConnection.releaseResources( result, prestate, connection );
            
        }
        
        return returnVal;
        
    }
    
    /**
     * hh_user_point_pay(_temp)のuser_seqとvisit_seqを更新
     * @param source_user_seq
     * @param destination_user_seq
     * @param maxVisitSeq
     * @param table_name
     * @throws Exception
     */
    private void numberingPointPay(int source_user_seq, int destination_user_seq, int maxVisitSeq, String table_name) throws Exception
    {

        PreparedStatement prestate = null;

        StringBuilder querySb = null;

        try
        {

            // hh_hotel_ci･･･user_seq（ホテルごとに付番されている）
            querySb = new StringBuilder();
            querySb.append( "UPDATE " + table_name );
            querySb.append( " SET user_seq = ?" );
            querySb.append( " , visit_seq = ? + visit_seq" );
            querySb.append( " WHERE user_id = ?" );
            querySb.append( " AND user_seq = ?" );

            prestate = this.connection.prepareStatement( querySb.toString() );
            prestate.setInt( 1, destination_user_seq );
            prestate.setInt( 2, maxVisitSeq );
            prestate.setString( 3, sourceUserID );
            prestate.setInt( 4, source_user_seq );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "DataTransition.numberingPointPayTemp Exceptionが発生", e );
            throw e;
        }
        finally
        {

            DBConnection.releaseResources( prestate );

            querySb = null;

        }

    }

    /**
     * hh_hotel_ci/ap_touch_ciのuser_seqを更新
     * 
     * @param result
     * @param numberingCiTableTableName
     * @throws Exception
     */
    private void numberingCiTable(int id, int source_user_seq, int destination_user_seq, int maxVisitSeq, String numberingCiTableTableName) throws Exception
    {

        PreparedStatement prestate = null;

        StringBuilder querySb = null;

        try
        {

            // hh_hotel_ci･･･user_seq（ホテルごとに付番されている）
            querySb = new StringBuilder();
            querySb.append( "UPDATE " + numberingCiTableTableName + " SET" );
            querySb.append( " user_id = ?" );
            querySb.append( ", user_seq = ?" );
            querySb.append( ", visit_seq = ? + visit_seq" );
            querySb.append( " WHERE user_id = ?" );
            querySb.append( " AND user_seq = ?" );
            querySb.append( " AND id = ?" );

            prestate = this.connection.prepareStatement( querySb.toString() );
            prestate.setString( 1, destinationUserID );
            prestate.setInt( 2, destination_user_seq );
            prestate.setInt( 3, maxVisitSeq );
            prestate.setString( 4, sourceUserID );
            prestate.setInt( 5, source_user_seq );
            prestate.setInt( 6, id );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "DataTransition.numberingCiTable Exceptionが発生", e );
            throw e;
        }
        finally
        {

            DBConnection.releaseResources( prestate );

            querySb = null;

        }

    }

    /**
     * hh_hotel_ci/ap_touch_ciのuser_seqを更新
     * 
     * @param id
     * @param source_user_seq
     * @param numberingCiTableTableName
     * @throws Exception
     */
    private void numberingCiTable(int id, int source_user_seq, String numberingCiTableTableName) throws Exception
    {

        PreparedStatement prestate = null;

        StringBuilder querySb = null;

        try
        {

            // hh_hotel_ci･･･user_seq（ホテルごとに付番されている）
            querySb = new StringBuilder();
            querySb.append( "UPDATE " + numberingCiTableTableName + " SET" );
            querySb.append( " user_id = ?" );
            querySb.append( " WHERE user_id = ?" );
            querySb.append( " AND user_seq = ?" );
            querySb.append( " AND id = ?" );

            prestate = this.connection.prepareStatement( querySb.toString() );
            prestate.setString( 1, destinationUserID );
            prestate.setString( 2, sourceUserID );
            prestate.setInt( 3, source_user_seq );
            prestate.setInt( 4, id );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "DataTransition.numberingCiTable Exceptionが発生", e );
            throw e;
        }
        finally
        {

            DBConnection.releaseResources( prestate );

            querySb = null;

        }

    }

    /**
     * ap_touch_user_pointデータ移行
     * 
     * @throws Exception
     */
    private void updateApTouchUserPoint() throws Exception
    {

        PreparedStatement prestate = null;

        StringBuilder querySb = null;

        try
        {

            querySb = new StringBuilder();
            querySb.append( "UPDATE ap_touch_user_point a" );
            querySb.append( ", (" );
            querySb.append( " SELECT" );
            querySb.append( "  *" );
            querySb.append( " FROM ap_touch_user_point a" );
            querySb.append( " WHERE a.user_id = ?" );
            querySb.append( " AND NOT EXISTS (" );
            querySb.append( "  SELECT 'X' FROM ap_touch_user_point b" );
            querySb.append( "  WHERE a.id = b.id" );
            querySb.append( "  AND a.ci_seq = b.ci_seq" );
            querySb.append( "  AND a.code = b.code" );
            querySb.append( "  AND user_id = ?" );
            querySb.append( " )" );
            querySb.append( ") b" );
            querySb.append( " SET a.user_id = ?" );
            querySb.append( " WHERE a.user_id = b.user_id" );
            querySb.append( " AND a.id = b.id" );
            querySb.append( " AND a.ci_seq = b.ci_seq" );
            querySb.append( " AND a.code = b.code" );

            prestate = this.connection.prepareStatement( querySb.toString() );
            prestate.setString( 1, sourceUserID );
            prestate.setString( 2, destinationUserID );
            prestate.setString( 3, destinationUserID );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "DataTransition.updateApTouchUserPoint Exceptionが発生", e );
            throw e;
        }
        finally
        {

            DBConnection.releaseResources( prestate );

            querySb = null;

        }
    }

}
