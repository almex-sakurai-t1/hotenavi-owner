package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Thu Nov 19 16:50:39 JST 2009
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 相性占い情報取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/11/20
 */
public class DataFortunateAffinityPay implements Serializable
{

    private static final long serialVersionUID = 3358626252305568525L;
    private int               date;
    private int               constellation1;
    private int               constellation2;
    private int               point;
    private String            content;

    public DataFortunateAffinityPay()
    {
        date = 0;
        constellation1 = 0;
        constellation2 = 0;
        point = 0;
        content = "";
    }

    public int getDate()
    {
        return this.date;
    }

    public int getConstellation1()
    {
        return this.constellation1;
    }

    public int getConstellation2()
    {
        return this.constellation2;
    }

    public int getPoint()
    {
        return this.point;
    }

    public String getContent()
    {
        return this.content;
    }

    public void setDate(int date)
    {
        this.date = date;
    }

    public void setConstellation1(int constellation1)
    {
        this.constellation1 = constellation1;
    }

    public void setConstellation2(int constellation2)
    {
        this.constellation2 = constellation2;
    }

    public void setPoint(int point)
    {
        this.point = point;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    /**
     * 有料占い情報取得
     * 
     * @param date 日付
     * @param constellation 星座
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int date, int constellation1, int constellation2)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_fortunate_affinity_pay WHERE date = ? AND constellation1 = ?";
        query += " AND constellation2 =?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, date );
            prestate.setInt( 2, constellation1 );
            prestate.setInt( 3, constellation2 );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.date = result.getInt( "date" );
                    this.constellation1 = result.getInt( "constellation1" );
                    this.constellation2 = result.getInt( "constellation2" );
                    this.point = result.getInt( "point" );
                    this.content = result.getString( "content" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunateAffinityPay.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 有料占い情報取得
     * 
     * @param result 有料占いデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.date = result.getInt( "date" );
                this.constellation1 = result.getInt( "constellation1" );
                this.constellation2 = result.getInt( "constellation2" );
                this.point = result.getInt( "point" );
                this.content = result.getString( "content" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunateAffinityPay.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * 有料占いデータ設定
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

        query = "INSERT hh_fortunate_affinity_pay SET";
        query = query + " date = ?,";
        query = query + " constellation1 = ?,";
        query = query + " constellation2 = ?,";
        query = query + " point = ?,";
        query = query + " content = ?,";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.date );
            prestate.setInt( 2, this.constellation1 );
            prestate.setInt( 3, this.constellation2 );
            prestate.setInt( 4, this.point );
            prestate.setString( 5, this.content );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunateAffinityPay.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 有料占いデータ設定
     * 
     * @param date 日付
     * @param constellation 星座
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int date, int constellation1, int constellation2)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "INSERT hh_fortunate_event_pay SET";
        query = query + " point = ?,";
        query = query + " content = ?,";
        query = query + " Where date = ?";
        query = query + " AND constellation1 = ?";
        query = query + " AND constellation2 = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.point );
            prestate.setString( 2, this.content );
            prestate.setInt( 3, date );
            prestate.setInt( 4, constellation1 );
            prestate.setInt( 5, constellation2 );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunateAffinityPay.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

}
