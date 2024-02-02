/*
 * @(#)DataUserBasic.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ユーザ基本情報取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 外国人用ユーザ基本情報取得クラス
 * 
 * @author S.Mitsuhashia
 * @version 1.00 2017/03/23
 * @version
 */
public class DataUserBasicForeign implements Serializable
{
    private static final long serialVersionUID = -5004946880747817567L;

    private String            userId;
    private String            nameLast;
    private String            nameFirst;
    private String            country;
    private String            address;
    private int               sex;
    private int               birthdayYear;
    private int               birthdayMonth;
    private int               birthdayDay;
    private String            tel;
    private int               registDate;
    private int               registTime;
    private int               lastUpdate;
    private int               lastUptime;

    /**
     * データを初期化します。
     */
    public DataUserBasicForeign()
    {
        userId = "";
        nameLast = "";
        nameFirst = "";
        country = "";
        address = "";
        sex = 2;
        birthdayYear = 0;
        birthdayMonth = 0;
        birthdayDay = 0;
        tel = "";
        registDate = 0;
        registTime = 0;
        lastUpdate = 0;
        lastUptime = 0;

    }

    public String getUserId()
    {
        return userId;
    }

    public String getNameLast()
    {
        return nameLast;
    }

    public String getNameFirst()
    {
        return nameFirst;
    }

    public String getCountry()
    {
        return country;
    }

    public String getAddress()
    {
        return address;
    }

    public int getSex()
    {
        return sex;
    }

    public int getBirthdayDay()
    {
        return birthdayDay;
    }

    public int getBirthdayMonth()
    {
        return birthdayMonth;
    }

    public int getBirthdayYear()
    {
        return birthdayYear;
    }

    public String gettel()
    {
        return tel;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setNameLast(String nameLast)
    {
        this.nameLast = nameLast;
    }

    public void setNameFirst(String nameFirst)
    {
        this.nameFirst = nameFirst;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public void setBirthdayDay(int birthdayDay)
    {
        this.birthdayDay = birthdayDay;
    }

    public void setBirthdayMonth(int birthdayMonth)
    {
        this.birthdayMonth = birthdayMonth;
    }

    public void setBirthdayYear(int birthdayYear)
    {
        this.birthdayYear = birthdayYear;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
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
     * 外国人用ユーザ基本データ取得
     * 
     * @param userId ユーザ基本データ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_basic_foreign WHERE user_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.nameLast = result.getString( "name_last" );
                    this.nameFirst = result.getString( "name_first" );
                    this.country = result.getString( "country" );
                    this.address = result.getString( "address" );
                    this.sex = result.getInt( "sex" );
                    this.birthdayYear = result.getInt( "birthday_year" );
                    this.birthdayMonth = result.getInt( "birthday_month" );
                    this.birthdayDay = result.getInt( "birthday_day" );
                    this.tel = result.getString( "tel" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );

                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserBasicForeign.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 外国人用ユーザ基本データ設定
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.userId = result.getString( "user_id" );
                this.nameLast = result.getString( "name_last" );
                this.nameFirst = result.getString( "name_first" );
                this.country = result.getString( "country" );
                this.address = result.getString( "address" );
                this.sex = result.getInt( "sex" );
                this.birthdayYear = result.getInt( "birthday_year" );
                this.birthdayMonth = result.getInt( "birthday_month" );
                this.birthdayDay = result.getInt( "birthday_day" );
                this.tel = result.getString( "tel" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserBasicForeign.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * 外国人用ユーザ基本情報データ追加
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

        query = "INSERT hh_user_basic_foreign SET ";
        query = query + " user_id = ?,";
        query = query + " name_last = ?,";
        query = query + " name_first = ?,";
        query = query + " country = ?,";
        query = query + " address = ?,";
        query = query + " sex = ?,";
        query = query + " birthday_year = ?,";
        query = query + " birthday_month = ?,";
        query = query + " birthday_day = ?,";
        query = query + " tel = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            int i = 1;
            // 更新対象の値をセットする
            prestate.setString( i++, this.userId );
            prestate.setString( i++, this.nameLast );
            prestate.setString( i++, this.nameFirst );
            prestate.setString( i++, this.country );
            prestate.setString( i++, this.address );
            prestate.setInt( i++, this.sex );
            prestate.setInt( i++, this.birthdayYear );
            prestate.setInt( i++, this.birthdayMonth );
            prestate.setInt( i++, this.birthdayDay );
            prestate.setString( i++, this.tel );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
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
            Logging.error( "[DataUserBasicForeign.insertData] Exception=" + e.toString() );
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
     * 外国人用ユーザ基本情報データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_basic_foreign SET ";
        query = query + " name_last = ?,";
        query = query + " name_first = ?,";
        query = query + " country = ?,";
        query = query + " address = ?,";
        query = query + " sex = ?,";
        query = query + " birthday_year = ?,";
        query = query + " birthday_month = ?,";
        query = query + " birthday_day = ?,";
        query = query + " tel = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ? ";
        query = query + " WHERE user_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            int i = 1;
            // 更新対象の値をセットする
            prestate.setString( i++, this.nameLast );
            prestate.setString( i++, this.nameFirst );
            prestate.setString( i++, this.country );
            prestate.setString( i++, this.address );
            prestate.setInt( i++, this.sex );
            prestate.setInt( i++, this.birthdayYear );
            prestate.setInt( i++, this.birthdayMonth );
            prestate.setInt( i++, this.birthdayDay );
            prestate.setString( i++, this.tel );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setString( i++, userId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserBasicForeign.updateData] Exception=" + e.toString() );
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
