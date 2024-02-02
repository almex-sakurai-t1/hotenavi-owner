package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * DataFortunatePay
 * 
 * @author S.Tashiro
 * @version 1.00 2009/08/12
 */
public class DataFortunatePay implements Serializable
{

    private static final long serialVersionUID = -8483501457613063547L;
    private int               date;
    private int               constellation;
    private String            loveText;
    private int               lovePoint5;
    private String            moneyText;
    private int               moneyPoint5;
    private String            workText;
    private int               workPoint5;
    private String            totalText;
    private int               totalPoint;
    private String            luckyDirection;
    private int               luckyNumber;
    private String            luckyColor;
    private String            luckyFood;
    private String            luckyFashion;
    private String            luckyPerson;
    private int               luckyItem;
    private int               luckyHotel;

    public DataFortunatePay()
    {
        date = 0;
        constellation = 0;
        loveText = "";
        lovePoint5 = 0;
        moneyText = "";
        moneyPoint5 = 0;
        workText = "";
        workPoint5 = 0;
        totalText = "";
        totalPoint = 0;
        luckyDirection = "";
        luckyNumber = 0;
        luckyColor = "";
        luckyFood = "";
        luckyFashion = "";
        luckyPerson = "";
        luckyItem = 0;
        luckyHotel = 0;
    }

    public int getDate()
    {
        return date;
    }

    public void setDate(int date)
    {
        this.date = date;
    }

    public int getConstellation()
    {
        return constellation;
    }

    public void setConstellation(int constellation)
    {
        this.constellation = constellation;
    }

    public String getLoveText()
    {
        return loveText;
    }

    public void setLoveText(String loveText)
    {
        this.loveText = loveText;
    }

    public int getLovePoint5()
    {
        return lovePoint5;
    }

    public void setLovePoint5(int lovePoint5)
    {
        this.lovePoint5 = lovePoint5;
    }

    public String getMoneyText()
    {
        return moneyText;
    }

    public void setMoneyText(String moneyText)
    {
        this.moneyText = moneyText;
    }

    public int getMoneyPoint5()
    {
        return moneyPoint5;
    }

    public void setMoneyPoint5(int moneyPoint5)
    {
        this.moneyPoint5 = moneyPoint5;
    }

    public String getWorkText()
    {
        return workText;
    }

    public void setWorkText(String workText)
    {
        this.workText = workText;
    }

    public int getWorkPoint5()
    {
        return workPoint5;
    }

    public void setWorkPoint5(int workPoint5)
    {
        this.workPoint5 = workPoint5;
    }

    public String getTotalText()
    {
        return totalText;
    }

    public void setTotalText(String totalText)
    {
        this.totalText = totalText;
    }

    public int getTotalPoint()
    {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint)
    {
        this.totalPoint = totalPoint;
    }

    public String getLuckyDirection()
    {
        return luckyDirection;
    }

    public void setLuckyDirection(String luckyDirection)
    {
        this.luckyDirection = luckyDirection;
    }

    public int getLuckyNumber()
    {
        return luckyNumber;
    }

    public void setLuckyNumber(int luckyNumber)
    {
        this.luckyNumber = luckyNumber;
    }

    public String getLuckyColor()
    {
        return luckyColor;
    }

    public void setLuckyColor(String luckyColor)
    {
        this.luckyColor = luckyColor;
    }

    public String getLuckyFood()
    {
        return luckyFood;
    }

    public void setLuckyFood(String luckyFood)
    {
        this.luckyFood = luckyFood;
    }

    public String getLuckyFashion()
    {
        return luckyFashion;
    }

    public void setLuckyFashion(String luckyFashion)
    {
        this.luckyFashion = luckyFashion;
    }

    public String getLuckyPerson()
    {
        return luckyPerson;
    }

    public void setLuckyPerson(String luckyPerson)
    {
        this.luckyPerson = luckyPerson;
    }

    public int getLuckyItem()
    {
        return luckyItem;
    }

    public void setLuckyItem(int luckyItem)
    {
        this.luckyItem = luckyItem;
    }

    public int getLuckyHotel()
    {
        return luckyHotel;
    }

    public void setLuckyHotel(int luckyHotel)
    {
        this.luckyHotel = luckyHotel;
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

        query = "SELECT * FROM hh_fortunate_pay WHERE date = ? AND constellation = ?";
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
                    this.loveText = result.getString( "love_text" );
                    this.lovePoint5 = result.getInt( "love_point5" );
                    this.moneyText = result.getString( "money_text" );
                    this.moneyPoint5 = result.getInt( "money_point5" );
                    this.workText = result.getString( "work_text" );
                    this.workPoint5 = result.getInt( "work_point5" );
                    this.totalText = result.getString( "total_text" );
                    this.totalPoint = result.getInt( "total_point" );
                    this.luckyDirection = result.getString( "lucky_direction" );
                    this.luckyNumber = result.getInt( "lucky_number" );
                    this.luckyColor = result.getString( "lucky_color" );
                    this.luckyFood = result.getString( "lucky_food" );
                    this.luckyFashion = result.getString( "lucky_fashion" );
                    this.luckyPerson = result.getString( "lucky_person" );
                    this.luckyItem = result.getInt( "lucky_item" );
                    this.luckyHotel = result.getInt( "lucky_hotel" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunatePay.getData] Exception=" + e.toString() );
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
                this.loveText = result.getString( "love_text" );
                this.lovePoint5 = result.getInt( "love_point5" );
                this.moneyText = result.getString( "money_text" );
                this.moneyPoint5 = result.getInt( "money_point5" );
                this.workText = result.getString( "work_text" );
                this.workPoint5 = result.getInt( "work_point5" );
                this.totalText = result.getString( "total_text" );
                this.totalPoint = result.getInt( "total_point" );
                this.luckyDirection = result.getString( "lucky_direction" );
                this.luckyNumber = result.getInt( "lucky_number" );
                this.luckyColor = result.getString( "lucky_color" );
                this.luckyFood = result.getString( "lucky_food" );
                this.luckyFashion = result.getString( "lucky_fashion" );
                this.luckyPerson = result.getString( "lucky_person" );
                this.luckyItem = result.getInt( "lucky_item" );
                this.luckyHotel = result.getInt( "lucky_hotel" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunatePay.setData] Exception=" + e.toString() );
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

        query = "INSERT hh_fortunate_pay SET";
        query = query + " date = ?,";
        query = query + " constellation = ?,";
        query = query + " love_text = ?,";
        query = query + " love_point5 = ?,";
        query = query + " money_text = ?,";
        query = query + " money_point5 = ?,";
        query = query + " work_text = ?,";
        query = query + " work_point5 = ?,";
        query = query + " total_text = ?,";
        query = query + " total_point = ?,";
        query = query + " lucky_direction = ?,";
        query = query + " lucky_number = ?,";
        query = query + " lucky_color = ?,";
        query = query + " lucky_food = ?,";
        query = query + " lucky_fashion = ?,";
        query = query + " lucky_person = ?,";
        query = query + " lucky_item = ?,";
        query = query + " lucky_hotel = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.date );
            prestate.setInt( 2, this.constellation );
            prestate.setString( 3, this.loveText );
            prestate.setInt( 4, this.lovePoint5 );
            prestate.setString( 5, this.moneyText );
            prestate.setInt( 6, this.moneyPoint5 );
            prestate.setString( 7, this.workText );
            prestate.setInt( 8, this.workPoint5 );
            prestate.setString( 9, this.totalText );
            prestate.setInt( 10, this.totalPoint );
            prestate.setString( 11, this.luckyDirection );
            prestate.setInt( 12, this.luckyNumber );
            prestate.setString( 13, this.luckyColor );
            prestate.setString( 14, this.luckyFood );
            prestate.setString( 15, this.luckyFashion );
            prestate.setString( 16, this.luckyPerson );
            prestate.setInt( 17, this.luckyItem );
            prestate.setInt( 18, this.luckyHotel );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunatePay.insertData] Exception=" + e.toString() );
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

        query = "INSERT hh_fortunate_pay SET";
        query = query + " love_text = ?,";
        query = query + " love_point5 = ?,";
        query = query + " money_text = ?,";
        query = query + " money_point5 = ?,";
        query = query + " work_text = ?,";
        query = query + " work_point5 = ?,";
        query = query + " total_text = ?,";
        query = query + " total_point = ?,";
        query = query + " lucky_direction = ?,";
        query = query + " lucky_number = ?,";
        query = query + " lucky_color = ?,";
        query = query + " lucky_food = ?,";
        query = query + " lucky_fashion = ?,";
        query = query + " lucky_person = ?,";
        query = query + " lucky_item = ?,";
        query = query + " lucky_hotel = ?";
        query = query + " Where date = ?";
        query = query + " AND constellation = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.loveText );
            prestate.setInt( 2, this.lovePoint5 );
            prestate.setString( 3, this.moneyText );
            prestate.setInt( 4, this.moneyPoint5 );
            prestate.setString( 5, this.workText );
            prestate.setInt( 6, this.workPoint5 );
            prestate.setString( 7, this.totalText );
            prestate.setInt( 8, this.totalPoint );
            prestate.setString( 9, this.luckyDirection );
            prestate.setInt( 10, this.luckyNumber );
            prestate.setString( 11, this.luckyColor );
            prestate.setString( 12, this.luckyFood );
            prestate.setString( 13, this.luckyFashion );
            prestate.setString( 14, this.luckyPerson );
            prestate.setInt( 15, this.luckyItem );
            prestate.setInt( 16, this.luckyHotel );
            prestate.setInt( 17, date );
            prestate.setInt( 18, constellation );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunatePay.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
