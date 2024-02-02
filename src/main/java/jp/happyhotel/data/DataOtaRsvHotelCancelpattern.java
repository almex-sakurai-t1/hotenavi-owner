package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 取得クラス
 * 
 * @author Mitsuhashi
 * @version 1.00 2018/10/23
 */
public class DataOtaRsvHotelCancelpattern implements Serializable
{
    public static final String TABLE = "ota_rsv_hotel_cancelpattern";
    private int                id;                                   // ホテルID
    private int                otaCd;                                // OTAコード
    private int                seq;                                  // 枝番
    private int                hours;                                // チェックインＨ時間前まではキャンセル料無料
    private Float              percent;                              // 取消料（％）
    private Integer            amount;                               // 取消料（定額）
    private int                lastUpdate;                           // 最終更新日: YYYYMMDD
    private int                lastUptime;                           // 最終更新時刻: HHMMSS

    /**
     * データを初期化します。
     */
    public DataOtaRsvHotelCancelpattern()
    {
        this.id = 0;
        this.otaCd = 0;
        this.seq = 0;
        this.hours = 0;
        this.percent = null;
        this.amount = null;
        this.lastUpdate = 0;
        this.lastUptime = 0;

    }

    public int getId()
    {
        return id;
    }

    public int getOotaCd()
    {
        return otaCd;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getHours()
    {
        return hours;
    }

    public Float getPercent()
    {
        return percent;
    }

    public Integer getAmount()
    {
        return amount;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setOtaCd(int otaCd)
    {
        this.otaCd = otaCd;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setHours(int hours)
    {
        this.hours = hours;
    }

    public void setPercent(Float percent)
    {
        this.percent = percent;
    }

    public void setAmount(Integer amount)
    {
        this.amount = amount;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    /**
     * 取得
     * 
     * @param id ホテルID
     * @param otaCd OTAコード
     * @param seq キャンセルID明細
     * @return
     */
    public boolean getData(int id, int otaCd, int seq)
    {
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            return this.getData( connection, id, otaCd, seq );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOtaRsvHotelCancelpattern.getData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 取得
     * 
     * @param id ホテルID
     * @param cancelId キャンセルID
     * @param seq キャンセルID明細
     * @return
     */
    public boolean getData(Connection connection, int id, int otaCd, int seq)
    {
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "SELECT * FROM newRsvDB.ota_rsv_cancelpattern WHERE id= ? AND ota_cd = ? AND seq = ?";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, otaCd );
            prestate.setInt( 3, seq );
            result = prestate.executeQuery();
            if ( result.next() == false )
            {
                return false;
            }
            return this.setData( result );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOtaRsvHotelCancelpattern.getData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, null );
        }
    }

    /**
     * 設定
     * 
     * @param result マスターソートレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.otaCd = result.getInt( "ota_cd" );
                this.seq = result.getInt( "seq" );
                this.hours = result.getInt( "hours" );
                this.percent = result.getFloat( "percent" );
                this.amount = result.getInt( "amount" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOtaRsvHotelCancelpattern.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData()
    {
        boolean ret = false;
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            ret = insertData( connection );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOtaRsvHotelCancelpattern.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return
     */
    public boolean updateData()
    {
        boolean ret = false;
        Connection connection = null;

        try
        {
            connection = DBConnection.getConnection();
            ret = updateData( connection );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOtaRsvHotelCancelpattern.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 更新(複数テーブル用)
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return
     */

    public boolean updateData(Connection connection)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE newRsvDB.ota_rsv_cancelpattern SET ";
        query += ", hours=?";
        query += ", percent=?";
        query += ", amount=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += " WHERE id=?";
        query += " AND ota_cd=?";
        query += " AND seq=?";

        try
        {
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.hours );
            prestate.setFloat( i++, this.percent );
            prestate.setInt( i++, this.amount );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.otaCd );
            prestate.setInt( i++, this.seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOtaRsvHotelCancelpattern.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            // DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param Connection connection
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData(Connection connection)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "INSERT newRsvDB.hh_rsv_cancelpattern SET ";
        query += "  id=?";
        query += "  ota_cd=?";
        query += "  seq=?";
        query += ", hours=?";
        query += ", percent=?";
        query += ", amount=?";
        query += ", last_update=?";
        query += ", last_uptime=?";

        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.otaCd );
            prestate.setInt( i++, this.seq );
            prestate.setInt( i++, this.hours );
            prestate.setFloat( i++, this.percent );
            prestate.setInt( i++, this.amount );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOtaRsvHotelCancelpattern.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            // DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * キャンセルパターン一覧を取得する
     * 
     * @param ホテルID
     * @return seqをセットした配列
     * @throws Exception
     */
    public static ArrayList<Integer> getCancelPatterns(int id) throws Exception
    {
        int i = 1;
        ArrayList<Integer> seqs = new ArrayList<Integer>();

        StringBuilder query = new StringBuilder();
        query.append( " SELECT seq " );
        query.append( " FROM newRsvDB.ota_rsv_hotel_cancelpattern " );
        query.append( " WHERE id=?  " );

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            prestate.setInt( i++, id );
            result = prestate.executeQuery();

            while( result.next() )
            {
                seqs.add( result.getInt( 1 ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getCancelPatterns] Exception=" + e.toString(), e );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return seqs;

    }

    /**
     * キャンセルパターン一覧を取得する
     * 
     * @param ホテルID
     * @return seqをセットした配列
     * @throws Exception
     */
    public static ArrayList<DataOtaRsvHotelCancelpattern> getCancelPatternList(int id) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataOtaRsvHotelCancelpattern data;
        ArrayList<DataOtaRsvHotelCancelpattern> array = null;

        query = " SELECT * ";
        query += " FROM newRsvDB.ota_rsv_hotel_cancelpattern ";
        query += " WHERE id=?  ";

        try
        {
            array = new ArrayList<DataOtaRsvHotelCancelpattern>();
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();

            if ( result != null )
            {
                result.beforeFirst();
                while( result.next() )
                {
                    data = new DataOtaRsvHotelCancelpattern();
                    data.id = result.getInt( "id" );
                    data.otaCd = result.getInt( "ota_cd" );
                    data.seq = result.getInt( "seq" );
                    data.hours = result.getInt( "hours" );
                    data.percent = result.getFloat( "percent" );
                    data.amount = result.getInt( "amount" );
                    data.lastUpdate = result.getInt( "last_update" );
                    data.lastUptime = result.getInt( "last_uptime" );
                    array.add( data );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getCancelPatternList(int id)] Exception:" + e.toString() );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(array);
    }
}
