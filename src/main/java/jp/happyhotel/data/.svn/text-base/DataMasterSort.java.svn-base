package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri Mar 29 18:56:48 JST 2013
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * DataMasterSort
 * 
 */
public class DataMasterSort implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 3869171773316742673L;
    private int               kind;
    private int               limitValue;
    private int               coefficient;

    public DataMasterSort()
    {
        kind = 0;
        limitValue = 0;
        coefficient = 0;
    }

    public int getKind()
    {
        return kind;
    }

    public int getLimitValue()
    {
        return limitValue;
    }

    public int getCoefficient()
    {
        return coefficient;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
    }

    public void setLimitValue(int limitValue)
    {
        this.limitValue = limitValue;
    }

    public void setCoefficient(int coefficient)
    {
        this.coefficient = coefficient;
    }

    /****
     * マスターデータ取得
     * 
     * @param kind
     * @return
     */
    public boolean getData(int kind)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_master_sort WHERE kind = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, kind );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.kind = result.getInt( "kind" );
                    this.limitValue = result.getInt( "limit_value" );
                    this.coefficient = result.getInt( "coefficient" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSort.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * マスターデータ設定
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
                this.kind = result.getInt( "kind" );
                this.limitValue = result.getInt( "limit_value" );
                this.coefficient = result.getInt( "coefficient" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSort.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * マスターデータ挿入
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

        query = "INSERT hh_master_sort SET ";
        query += " kind = ?,";
        query += " limit_value = ?,";
        query += " coefficient = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.kind );
            prestate.setInt( 2, this.limitValue );
            prestate.setInt( 3, this.coefficient );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSort.insertData] Exception=" + e.toString() );
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
     * マスターデータ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param kind 区分
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int kind)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_master_sort SET ";
        query += " limit_value = ?,";
        query += " coefficient = ?";
        query += " WHERE kind = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.limitValue );
            prestate.setInt( 2, this.coefficient );
            prestate.setInt( 3, kind );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSort.updateData] Exception=" + e.toString() );
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
