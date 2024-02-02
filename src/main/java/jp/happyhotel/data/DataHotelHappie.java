package jp.happyhotel.data;

/*
 * ホテルハピーデータ
 * generator Version 1.0.0 release 2011/01/12
 * generated Date Wed Jan 12 16:45:55 JST 2011
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * ホテルハピーデータ
 * 
 * @author tashiro-s1
 * @version 1.0 2011/01/12
 */
public class DataHotelHappie implements Serializable
{
    private static final long serialVersionUID = 3488353457323024469L;

    private int               id;
    private int               seq;
    private String            name;
    private int               startDate;
    private int               endDate;
    private int               comePointMultiple;
    private int               usePointMultiple;

    public DataHotelHappie()
    {
        id = 0;
        seq = 0;
        name = "";
        startDate = 0;
        endDate = 0;
        comePointMultiple = 0;
        usePointMultiple = 0;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getSeq()
    {
        return this.seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getStartDate()
    {
        return this.startDate;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public int getEndDate()
    {
        return this.endDate;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public int getComePointMultiple()
    {
        return this.comePointMultiple;
    }

    public void setComePointMultiple(int comePointMultiple)
    {
        this.comePointMultiple = comePointMultiple;
    }

    public int getUsePointMultiple()
    {
        return this.usePointMultiple;
    }

    public void setUsePointMultiple(int usePointMultiple)
    {
        this.usePointMultiple = usePointMultiple;
    }

    /**
     * ホテルハピーデータ取得
     * 
     * @param id ホテルID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_happie WHERE id = ? AND seq = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.name = result.getString( "name" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.comePointMultiple = result.getInt( "come_point_multiple" );
                    this.usePointMultiple = result.getInt( "use_point_multiple" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelHappie.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテルハピーデータ取得
     * 
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_happie WHERE id = ?";
        query += " AND start_date <= " + DateEdit.getDate( 2 );
        query += " AND end_date >= " + DateEdit.getDate( 2 );
        query += " ORDER BY seq DESC";
        query += " LIMIT 0,1";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.name = result.getString( "name" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.comePointMultiple = result.getInt( "come_point_multiple" );
                    this.usePointMultiple = result.getInt( "use_point_multiple" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelHappie.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテルハピーデータ取得
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        boolean ret;
        ret = false;
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.seq = result.getInt( "seq" );
                this.name = result.getString( "name" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.comePointMultiple = result.getInt( "come_point_multiple" );
                this.usePointMultiple = result.getInt( "use_point_multiple" );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelHappie.setData] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * ホテルハピーデータ追加
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

        query = "INSERT hh_hotel_happie SET ";
        query += " id = ?,";
        query += " seq = ?,";
        query += " name = ?";
        query += " start_date = ?";
        query += " end_date = ?";
        query += " come_point_multiple = ?";
        query += " use_point_multiple = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.seq );
            prestate.setString( 3, this.name );
            prestate.setInt( 4, this.startDate );
            prestate.setInt( 5, this.endDate );
            prestate.setInt( 6, this.comePointMultiple );
            prestate.setInt( 7, this.usePointMultiple );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelHappie.insertData] Exception=" + e.toString() );
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
     * ホテルハピーデータ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int id, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_happie SET ";
        query += " name = ?,";
        query += " start_date = ?,";
        query += " end_date = ?,";
        query += " come_point_multiple = ?,";
        query += " use_point_multiple = ?,";
        query += " WHERE id = ?";
        query += " AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.name );
            prestate.setInt( 2, this.startDate );
            prestate.setInt( 3, this.endDate );
            prestate.setInt( 4, this.comePointMultiple );
            prestate.setInt( 5, this.usePointMultiple );
            prestate.setInt( 6, id );
            prestate.setInt( 7, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelHappie.updateData] Exception=" + e.toString() );
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
