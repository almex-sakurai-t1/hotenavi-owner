/*
 * 予約クレジットデータクラス
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

public class DataRsvCredit implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1301982339239965148L;

    private String            reserve_no;
    private String            card_no;
    private int               limit_date;
    private int               id;
    private int               last_update;
    private int               last_uptime;
    private int               del_date;
    private int               del_time;
    private int               del_flag;

    /**
     * データの初期化
     */
    public DataRsvCredit()
    {
        setReserve_no( "" );
        setCard_no( "" );
        setLimit_date( 0 );
        setId( 0 );
        setLast_update( 0 );
        setLast_uptime( 0 );
        setDel_date( 0 );
        setDel_time( 0 );
    }

    // getter

    /**
     * 予約クレジットデータ情報取得
     * 
     * @param reserveNo 予約番号
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(String reserveNo)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT reserve_no, card_no, limit_date, id, last_update, last_uptime, " +
                " del_date, del_time, del_flag " +
                " FROM hh_rsv_credit WHERE reserve_no = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, reserveNo );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.reserve_no = result.getString( "reserve_no" );
                    // カード番号は複合化
                    this.card_no = result.getString( "card_no" );
                    this.limit_date = result.getInt( "limit_date" );
                    this.id = result.getInt( "id" );
                    this.last_update = result.getInt( "last_update" );
                    this.last_uptime = result.getInt( "last_uptime" );
                    this.del_date = result.getInt( "del_date" );
                    this.del_time = result.getInt( "del_time" );
                    this.del_flag = result.getInt( "del_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvCredit.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 予約クレジットデータ更新
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_credit SET " +
                " card_no = ? " +
                ", limit_date = ? " +
                ", id = ? " +
                ", last_update = ? " +
                ", last_uptime = ? " +
                ", del_date = ? " +
                ", del_time = ? " +
                ", del_flag = ? " +
                " where reserve_no = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, "" );
            prestate.setInt( 2, this.limit_date );
            prestate.setInt( 3, this.id );
            prestate.setInt( 4, this.last_update );
            prestate.setInt( 5, this.last_uptime );
            prestate.setInt( 6, this.del_date );
            prestate.setInt( 7, this.del_date );
            prestate.setInt( 8, this.del_flag );
            prestate.setString( 9, this.reserve_no );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvCredit.updateData] Exception=" + e.toString() );
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
     * 予約クレジットデータ登録
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

        query = "INSERT INTO hh_rsv_credit SET " +
                "reserve_no = ?" +
                ", card_no = ? " +
                ", limit_date = ? " +
                ", id = ? " +
                ", last_update = ? " +
                ", last_uptime = ? " +
                ", del_date = ? " +
                ", del_time = ? " +
                ", del_flag = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.reserve_no );
            prestate.setString( 2, "" );
            prestate.setInt( 3, this.limit_date );
            prestate.setInt( 4, this.id );
            prestate.setInt( 5, this.last_update );
            prestate.setInt( 6, this.last_uptime );
            prestate.setInt( 7, this.del_date );
            prestate.setInt( 8, this.del_date );
            prestate.setInt( 9, this.del_flag );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvCredit.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    public String getReserve_no()
    {
        return reserve_no;
    }

    public String getCard_no()
    {
        return card_no;
    }

    public int getLimit_date()
    {
        return limit_date;
    }

    public int getLast_update()
    {
        return last_update;
    }

    public int getLast_uptime()
    {
        return last_uptime;
    }

    public void setCard_no(String card_no)
    {
        this.card_no = card_no;
    }

    public void setLimit_date(int limit_date)
    {
        this.limit_date = limit_date;
    }

    public int getDel_date()
    {
        return del_date;
    }

    public int getId()
    {
        return id;
    }

    public int getDel_time()
    {
        return del_time;
    }

    public int getDel_flag()
    {
        return del_flag;
    }

    public void setReserve_no(String reserve_no)
    {
        this.reserve_no = reserve_no;
    }

    public void setLast_update(int last_update)
    {
        this.last_update = last_update;
    }

    public void setLast_uptime(int last_uptime)
    {
        this.last_uptime = last_uptime;
    }

    public void setDel_date(int del_date)
    {
        this.del_date = del_date;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setDel_time(int del_time)
    {
        this.del_time = del_time;
    }

    public void setDel_flag(int del_flag)
    {
        this.del_flag = del_flag;
    }

}
