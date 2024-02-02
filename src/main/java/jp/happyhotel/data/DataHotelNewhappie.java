package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 最新ハピー加盟店データ
 * 
 * @author N.Ide
 * @version 1.0 2011/05/26
 */
public class DataHotelNewhappie implements Serializable
{

    private static final long serialVersionUID = -988964843844887170L;

    private int               id;
    private int               dateDue;
    private String            dateDueText;
    private int               dateStart;
    private int               rsvDateDue;
    private String            rsvDateDueText;
    private int               rsvDateStart;
    private int               bkoDateStart;

    public DataHotelNewhappie()
    {
        id = 0;
        dateDue = 0;
        dateDueText = "";
        dateStart = 0;
        rsvDateDue = 0;
        rsvDateDueText = "";
        rsvDateStart = 0;
        bkoDateStart = 0;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getDateDue()
    {
        return dateDue;
    }

    public void setDateDue(int dateDue)
    {
        this.dateDue = dateDue;
    }

    public String getDateDueText()
    {
        return dateDueText;
    }

    public void setDateDueText(String dateDueText)
    {
        this.dateDueText = dateDueText;
    }

    public int getDateStart()
    {
        return dateStart;
    }

    public void setDateStart(int dateStart)
    {
        this.dateStart = dateStart;
    }

    public int getRsvDateDue()
    {
        return rsvDateDue;
    }

    public void setRsvDateDue(int rsvDateDue)
    {
        this.rsvDateDue = rsvDateDue;
    }

    public String getRsvDateDueText()
    {
        return rsvDateDueText;
    }

    public void setRsvDateDueText(String rsvDateDueText)
    {
        this.rsvDateDueText = rsvDateDueText;
    }

    public int getRsvDateStart()
    {
        return rsvDateStart;
    }

    public void setRsvDateStart(int rsvDateStart)
    {
        this.rsvDateStart = rsvDateStart;
    }

    public int getBkoDateStart()
    {
        return bkoDateStart;
    }

    public void setBkoDateStart(int bkoDateStart)
    {
        this.bkoDateStart = bkoDateStart;
    }

    /**
     * 最新ハピー加盟店データ取得
     * 
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(Connection connection, int id)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_newhappie WHERE id = ?";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.dateDue = result.getInt( "date_due" );
                    this.dateDueText = result.getString( "date_due_text" );
                    this.dateStart = result.getInt( "date_start" );
                    this.rsvDateDue = result.getInt( "rsv_date_due" );
                    this.rsvDateDueText = result.getString( "rsv_date_due_text" );
                    this.rsvDateStart = result.getInt( "rsv_date_start" );
                    this.bkoDateStart = result.getInt( "bko_date_start" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelNewhappie.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * 最新ハピー加盟店データ取得
     * 
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id)
    {
        boolean ret;
        Connection connection = null;
        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            ret = getData( connection, id );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelNewhappie.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 最新ハピー加盟店データ設定
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
                this.dateDue = result.getInt( "date_due" );
                this.dateDueText = result.getString( "date_due_text" );
                this.dateStart = result.getInt( "date_start" );
                this.rsvDateDue = result.getInt( "rsv_date_due" );
                this.rsvDateDueText = result.getString( "rsv_date_due_text" );
                this.rsvDateStart = result.getInt( "rsv_date_start" );
                this.bkoDateStart = result.getInt( "bko_date_start" );

                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelNewhappie.setData] Exception=" + e.toString() );
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

        query = "INSERT hh_hotel_newhappie SET ";
        query += " id = ?,";
        query += " date_due = ?,";
        query += " date_due_text = ?";
        query += " date_start = ?,";
        query += " rsv_date_due = ?,";
        query += " rsv_date_due_text = ?,";
        query += " rsv_date_start = ?,";
        query += " bko_date_start = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.dateDue );
            prestate.setString( 3, this.dateDueText );
            prestate.setInt( 4, this.dateStart );
            prestate.setInt( 5, this.rsvDateDue );
            prestate.setString( 6, this.rsvDateDueText );
            prestate.setInt( 7, this.rsvDateStart );
            prestate.setInt( 8, this.bkoDateStart );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelNewhappi.insertData] Exception=" + e.toString() );
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

        query = "UPDATE hh_hotel_newhappie SET ";
        query += " date_due = ?,";
        query += " date_due_text = ?,";
        query += " date_start = ?,";
        query += " rsv_date_due = ?,";
        query += " rsv_date_due_text = ?,";
        query += " rsv_date_start = ?,";
        query += " bko_date_start = ?";
        query += " WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.dateDue );
            prestate.setString( 2, this.dateDueText );
            prestate.setInt( 3, this.dateStart );
            prestate.setInt( 4, this.rsvDateDue );
            prestate.setString( 5, this.rsvDateDueText );
            prestate.setInt( 6, this.rsvDateStart );
            prestate.setInt( 7, this.bkoDateStart );
            prestate.setInt( 8, id );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelNewhappi.updateData] Exception=" + e.toString() );
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
