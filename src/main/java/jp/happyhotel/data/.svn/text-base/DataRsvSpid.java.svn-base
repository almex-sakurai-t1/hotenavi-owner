/*
 * 予約ノーショークレジット用SPIDデータクラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvSpid implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1301982339239965148L;

    private String            spid;
    private int               id;
    private String            sales_name;
    private int               card_company;
    private int               last_update;
    private int               last_uptime;
    private int               del_flag;

    /**
     * データの初期化
     */
    public DataRsvSpid()
    {
        setSpid( "" );

        setSales_name( "" );
        setCard_company( 0 );
        setLast_update( 0 );
        setLast_uptime( 0 );
        setDel_flag( 0 );
    }

    // getter

    /**
     * SPID情報取得
     * 
     * @param spid SPID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(String spid)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT spid, id, sales_name, card_company, last_update, last_uptime, " +
                " del_flag " +
                " FROM hh_rsv_spid WHERE spid = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, spid );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.spid = result.getString( "spid" );
                    this.id = result.getInt( "id" );
                    this.sales_name = result.getString( "sales_name" );
                    this.card_company = result.getInt( "card_company" );
                    this.last_update = result.getInt( "last_update" );
                    this.last_uptime = result.getInt( "last_uptime" );
                    this.del_flag = result.getInt( "del_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvSpid.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * SPID情報取得(削除フラグが立っていないレコード)
     * 
     * @param hotelid ホテルID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getDataByHotelid(int hotelid)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "SELECT spid, id, sales_name, card_company, last_update, last_uptime, " +
                " del_flag " +
                " FROM hh_rsv_spid WHERE id = ? AND del_flag = 0";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelid );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.spid = result.getString( "spid" );
                    this.id = result.getInt( "id" );
                    this.sales_name = result.getString( "sales_name" );
                    this.card_company = result.getInt( "card_company" );
                    this.last_update = result.getInt( "last_update" );
                    this.last_uptime = result.getInt( "last_uptime" );
                    this.del_flag = result.getInt( "del_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvSpid.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);

    }

    /**
     * SPID情報取得(削除フラグが立っていないレコード)
     * 
     * @param conn DB接続クラス
     * @param hotelid ホテルID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getDataByHotelid(Connection conn, int hotelid)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "SELECT spid, id, sales_name, card_company, last_update, last_uptime, " +
                " del_flag " +
                " FROM hh_rsv_spid WHERE id = ? AND del_flag = 0";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, hotelid );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.spid = result.getString( "spid" );
                    this.id = result.getInt( "id" );
                    this.sales_name = result.getString( "sales_name" );
                    this.card_company = result.getInt( "card_company" );
                    this.last_update = result.getInt( "last_update" );
                    this.last_uptime = result.getInt( "last_uptime" );
                    this.del_flag = result.getInt( "del_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvSpid.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);

    }

    /**
     * 予約基本データ登録
     * 
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

        query = "INSERT INTO hh_rsv_spid SET " +
                " spid = ?" +
                ", id = ? " +
                ", sales_name = ? " +
                ", card_company = ? " +
                ", last_update = ? " +
                ", last_uptime = ? " +
                ", del_flag = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.spid );
            prestate.setInt( 2, this.id );
            prestate.setString( 3, this.sales_name );
            prestate.setInt( 4, this.card_company );
            prestate.setInt( 5, this.last_update );
            prestate.setInt( 6, this.last_uptime );
            prestate.setInt( 9, this.del_flag );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvSpid.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    public String getSpid()
    {
        return spid;
    }

    public int getId()
    {
        return id;
    }

    public String getSales_name()
    {
        return sales_name;
    }

    public int getCard_company()
    {
        return card_company;
    }

    public int getLast_update()
    {
        return last_update;
    }

    public int getLast_uptime()
    {
        return last_uptime;
    }

    public int getDel_flag()
    {
        return del_flag;
    }

    public void setSpid(String spid)
    {
        this.spid = spid;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setSales_name(String sales_name)
    {
        this.sales_name = sales_name;
    }

    public void setCard_company(int card_company)
    {
        this.card_company = card_company;
    }

    public void setLast_update(int last_update)
    {
        this.last_update = last_update;
    }

    public void setLast_uptime(int last_uptime)
    {
        this.last_uptime = last_uptime;
    }

    public void setDel_flag(int del_flag)
    {
        this.del_flag = del_flag;
    }

}
