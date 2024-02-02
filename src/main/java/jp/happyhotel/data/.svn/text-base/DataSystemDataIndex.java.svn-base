package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri Apr 15 14:22:56 JST 2011
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * システム採番データ
 * 
 * @author tashiro-s1
 * @version 1.0
 */
public class DataSystemDataIndex implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -1109824884523822660L;
    private int               kind;
    private int               id;
    private int               maxSeq;
    private int               maxSeqSub1;
    private int               maxSeqSub2;
    private int               maxSeqSub3;
    private int               updateDate;
    private int               updateTime;

    public DataSystemDataIndex()
    {
        kind = 0;
        id = 0;
        maxSeq = 0;
        maxSeqSub1 = 0;
        maxSeqSub2 = 0;
        maxSeqSub3 = 0;
        updateDate = 0;
        updateTime = 0;
    }

    public int getKind()
    {
        return kind;
    }

    public int getId()
    {
        return id;
    }

    public int getMaxSeq()
    {
        return maxSeq;
    }

    public int getMaxSeqSub1()
    {
        return maxSeqSub1;
    }

    public int getMaxSeqSub2()
    {
        return maxSeqSub2;
    }

    public int getMaxSeqSub3()
    {
        return maxSeqSub3;
    }

    public int getUpdateDate()
    {
        return updateDate;
    }

    public int getUpdateTime()
    {
        return updateTime;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
    }

    public void setId(int hotelId)
    {
        this.id = hotelId;
    }

    public void setMaxSeq(int maxSeq)
    {
        this.maxSeq = maxSeq;
    }

    public void setMaxSeqSub1(int maxSeqSub1)
    {
        this.maxSeqSub1 = maxSeqSub1;
    }

    public void setMaxSeqSub2(int maxSeqSub2)
    {
        this.maxSeqSub2 = maxSeqSub2;
    }

    public void setMaxSeqSub3(int maxSeqSub3)
    {
        this.maxSeqSub3 = maxSeqSub3;
    }

    public void setUpdateDate(int updateDate)
    {
        this.updateDate = updateDate;
    }

    public void setUpdateTime(int updateTime)
    {
        this.updateTime = updateTime;
    }

    /****
     * システム採番データ取得
     * 
     * @param kind
     * @param id
     * @return
     */
    public boolean getData(int kind, int id)
    {

        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_system_data_index WHERE kind = ? and id = ?";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, kind );
            prestate.setInt( 2, id );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.kind = result.getInt( "kind" );
                    this.id = result.getInt( "id" );
                    this.maxSeq = result.getInt( "max_seq" );
                    this.maxSeqSub1 = result.getInt( "max_seq_sub1" );
                    this.maxSeqSub2 = result.getInt( "max_seq_sub2" );
                    this.maxSeqSub3 = result.getInt( "max_seq_sub3a" );
                    this.updateDate = result.getInt( "update_date" );
                    this.updateTime = result.getInt( "upData_time" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            Logging.error( "[DataSystemDataIndex.getData] count=" + count );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemDataIndex.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( count > 0 )
            return(true);
        else
            return(false);
    }

    /**
     * システム採番データ設定
     * 
     * @param result ユーザポイントデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.kind = result.getInt( "kind" );
                this.id = result.getInt( "id" );
                this.maxSeq = result.getInt( "max_seq" );
                this.maxSeqSub1 = result.getInt( "max_seq_sub1" );
                this.maxSeqSub2 = result.getInt( "max_seq_sub2" );
                this.maxSeqSub3 = result.getInt( "max_seq_sub3a" );
                this.updateDate = result.getInt( "update_date" );
                this.updateTime = result.getInt( "upData_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemDataIndex.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * システム採番データ挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_system_data_index SET ";
        query += " kind = ?,";
        query += " id = ?,";
        query += " max_seq = ?,";
        query += " max_seq_sub1 = ?,";
        query += " max_seq_sub2 = ?,";
        query += " max_seq_sub3 = ?,";
        query += " update_date = ?,";
        query += " uppdate_time = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.kind );
            prestate.setInt( 2, this.id );
            prestate.setInt( 3, this.maxSeq );
            prestate.setInt( 4, this.maxSeqSub1 );
            prestate.setInt( 5, this.maxSeqSub2 );
            prestate.setInt( 6, this.maxSeqSub3 );
            prestate.setInt( 7, this.updateDate );
            prestate.setInt( 8, this.updateTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemDataIndex.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * システム採番データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param kind　区分
     * @param id　ホテルID
     * @return
     */
    public boolean updateData(int kind, int id)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_system_data_index SET ";
        query += " max_seq = ?,";
        query += " max_seq_sub1 = ?,";
        query += " max_seq_sub2 = ?,";
        query += " max_seq_sub3 = ?,";
        query += " update_date = ?,";
        query += " uppdate_time = ?";
        query = query + " WHERE kind = ? AND id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.maxSeq );
            prestate.setInt( 2, this.maxSeqSub1 );
            prestate.setInt( 3, this.maxSeqSub2 );
            prestate.setInt( 4, this.maxSeqSub3 );
            prestate.setInt( 5, this.updateDate );
            prestate.setInt( 6, this.updateTime );
            prestate.setInt( 7, kind );
            prestate.setInt( 8, id );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemDataIndex.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

}
