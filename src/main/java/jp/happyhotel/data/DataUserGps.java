package jp.happyhotel.data;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ユーザー現在位置情報取得クラス
 * 
 * @author Keion.Park
 */

public class DataUserGps
{

    public static final String TABLE     = "hh_user_gps";
    private String             userId;                   // ユーザーID
    private int                seq;
    private int                registDate;               // 登録日付(YYYYMMDD)
    private int                registTime;               // 登録時刻(HHMMSS)
    private String             lat;                      // 緯度
    private String             lon;                      // 経度
    private String             url;                      // url
    private String             userAgent;                // ユーザーエージェント
    final int                  START_IDX = 0;
    final int                  END_IDX   = 255;

    /**
     * データを初期化します。
     */
    public DataUserGps()
    {
        this.userId = "";
        this.seq = 0;
        this.registDate = 0;
        this.registTime = 0;
        this.lat = "";
        this.lon = "";
        this.url = "";
        this.userAgent = "";
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getSeq()
    {
        return seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public String getLat()
    {
        return lat;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public String getLon()
    {
        return lon;
    }

    public void setLon(String lon)
    {
        this.lon = lon;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }

    /****
     * ユーザー位置データ(hh_user_gps)取得
     * 
     * @param errorSeq エラー連番
     * @return
     */
    public boolean getData(String userId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM hh_user_gps WHERE user_id=? AND seq = ?  ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    setData( result );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserGps.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * エラー履歴(ap_error_history)設定
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
                this.seq = result.getInt( "seq" );
                this.userId = result.getString( "user_id" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.lat = result.getBigDecimal( "lat" ).toString();
                this.lon = result.getBigDecimal( "lon" ).toString();
                this.url = result.getString( "url" );
                this.userAgent = result.getString( "user_agent" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserGps.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ユーザー現在位置情報(hh_user_gps)挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        BigDecimal Lat = null;
        BigDecimal Lon = null;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT hh_user_gps SET ";
        query += " user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", lat=?";
        query += ", lon=?";
        query += ", url=?";
        query += ", user_agent=?";
        Lat = new BigDecimal( this.lat );
        Lon = new BigDecimal( this.lon );

        if ( this.userAgent != null )
        {
            if ( this.userAgent.length() > END_IDX )
            {
                this.userAgent = this.userAgent.substring( START_IDX, END_IDX );
            }
        }

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setBigDecimal( i++, Lat );
            prestate.setBigDecimal( i++, Lon );
            prestate.setString( i++, this.url );
            prestate.setString( i++, this.userAgent );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserGps.insertData] Exception=" + e.toString() );
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
