package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 有料イベント占いデータ
 * 
 * @author S.Tashiro
 * @version 1.00 2009/11/17
 */
public class DataFortunateEventPay implements Serializable
{

    private static final long serialVersionUID = -4748037008945714370L;
    private int               date;
    private int               constellation;
    private String            title;
    private String            item1;
    private String            item1Text;
    private String            item2;
    private String            item2Text;
    private String            item3;
    private String            item3Text;
    private String            item4;
    private String            item4Text;

    public DataFortunateEventPay()
    {
        date = 0;
        constellation = 0;
        item1 = "";
        item1Text = "";
        item2 = "";
        item2Text = "";
        item3 = "";
        item3Text = "";
        item4 = "";
        item4Text = "";
    }

    public int getDate()
    {
        return date;
    }

    public int getConstellation()
    {
        return constellation;
    }

    public String getTitle()
    {
        return title;
    }

    public String getItem1()
    {
        return item1;
    }

    public String getItem1Text()
    {
        return item1Text;
    }

    public String getItem2()
    {
        return item2;
    }

    public String getItem2Text()
    {
        return item2Text;
    }

    public String getItem3()
    {
        return item3;
    }

    public String getItem3Text()
    {
        return item3Text;
    }

    public String getItem4()
    {
        return item4;
    }

    public String getItem4Text()
    {
        return item4Text;
    }

    public void setDate(int date)
    {
        this.date = date;
    }

    public void setConstellation(int constellation)
    {
        this.constellation = constellation;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setItem1(String item1)
    {
        this.item1 = item1;
    }

    public void setItem1Text(String item1Text)
    {
        this.item1Text = item1Text;
    }

    public void setItem2(String item2)
    {
        this.item2 = item2;
    }

    public void setItem2Text(String item2Text)
    {
        this.item2Text = item2Text;
    }

    public void setItem3(String item3)
    {
        this.item3 = item3;
    }

    public void setItem3Text(String item3Text)
    {
        this.item3Text = item3Text;
    }

    public void setItem4(String item4)
    {
        this.item4 = item4;
    }

    public void setItem4Text(String item4Text)
    {
        this.item4Text = item4Text;
    }

    /**
     * 有料占い情報取得
     * 
     * @param date 日付
     * @param constellation 星座
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int date, int constellation)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_fortunate_event_pay WHERE date = ? AND constellation = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, date );
            prestate.setInt( 2, constellation );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.date = result.getInt( "date" );
                    this.constellation = result.getInt( "constellation" );
                    this.title = result.getString( "title" );
                    this.item1 = result.getString( "item1" );
                    this.item1Text = result.getString( "item1_text" );
                    this.item2 = result.getString( "item2" );
                    this.item2Text = result.getString( "item2_text" );
                    this.item3 = result.getString( "item3" );
                    this.item3Text = result.getString( "item3_text" );
                    this.item4 = result.getString( "item4" );
                    this.item4Text = result.getString( "item4_text" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunateEventPay.getData] Exception=" + e.toString() );
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
                this.constellation = result.getInt( "constellation" );
                this.title = result.getString( "title" );
                this.item1 = result.getString( "item1" );
                this.item1Text = result.getString( "item1_text" );
                this.item2 = result.getString( "item2" );
                this.item2Text = result.getString( "item2_text" );
                this.item3 = result.getString( "item3" );
                this.item3Text = result.getString( "item3_text" );
                this.item4 = result.getString( "item4" );
                this.item4Text = result.getString( "item4_text" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunateEventPay.setData] Exception=" + e.toString() );
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

        query = "INSERT hh_fortunate_event_pay SET";
        query = query + " date = ?,";
        query = query + " constellation = ?,";
        query = query + " title = ?,";
        query = query + " item1 = ?,";
        query = query + " item1_text = ?,";
        query = query + " item2 = ?,";
        query = query + " item2_text = ?,";
        query = query + " item3 = ?,";
        query = query + " item3_text = ?,";
        query = query + " item4 = ?,";
        query = query + " item4_text = ?,";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.date );
            prestate.setInt( 2, this.constellation );
            prestate.setString( 3, this.title );
            prestate.setString( 4, this.item1 );
            prestate.setString( 5, this.item1Text );
            prestate.setString( 6, this.item2 );
            prestate.setString( 7, this.item2Text );
            prestate.setString( 8, this.item3 );
            prestate.setString( 9, this.item3Text );
            prestate.setString( 10, this.item4 );
            prestate.setString( 11, this.item4Text );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunateEventPay.insertData] Exception=" + e.toString() );
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
    public boolean updateData(int date, int constellation)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "INSERT hh_fortunate_event_pay SET";
        query = query + " title = ?,";
        query = query + " item1 = ?,";
        query = query + " item1_text = ?,";
        query = query + " item2 = ?,";
        query = query + " item2_text = ?,";
        query = query + " item3 = ?,";
        query = query + " item3_text = ?,";
        query = query + " item4 = ?,";
        query = query + " item4_text = ?,";
        query = query + " Where date = ?";
        query = query + " AND constellation = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.title );
            prestate.setString( 2, this.item1 );
            prestate.setString( 3, this.item1Text );
            prestate.setString( 4, this.item2 );
            prestate.setString( 5, this.item2Text );
            prestate.setString( 6, this.item3 );
            prestate.setString( 7, this.item3Text );
            prestate.setString( 8, this.item4 );
            prestate.setString( 9, this.item4Text );
            prestate.setInt( 10, date );
            prestate.setInt( 11, constellation );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunateEventPay.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
