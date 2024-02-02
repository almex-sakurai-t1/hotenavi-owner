/*
 * @(#)DataMasterUseragent.java 1.00 2007/08/03 Copyright (C) ALMEX Inc. 2007 携帯ユーザエージェント情報取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 携帯ユーザエージェント情報取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/03
 * @version 1.1 2007/11/29
 */
public class DataMasterUseragent implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID   = 598869684747591613L;

    /**
     * キャリアフラグ(1:DoCoMo)
     */
    public static final int   CARRIER_DOCOMO     = 1;
    /**
     * キャリアフラグ(2:au)
     */
    public static final int   CARRIER_AU         = 2;
    /**
     * キャリアフラグ(3:SoftBank)
     */
    public static final int   CARRIER_SOFTBANK   = 3;
    /**
     * キャリアフラグ(4:その他)
     */
    public static final int   CARRIER_ETC        = 4;
    /**
     * キャリアフラグ(5:スマートフォン)
     */
    public static final int   CARRIER_SMARTPHONE = 5;

    /** 管理番号 **/
    private int               seq;
    /** キャリアフラグ **/
    private int               carrierFlag;
    /** ユーザエージェント名 **/
    private String            userAgent;
    /** 対応端末フラグ **/
    private int               officialFlag;
    /** 端末性能フラグ **/
    private int               performanceFlag;
    /** GPS対応フラグ **/
    private int               gpsFlag;
    /** SSL対応フラグ **/
    private int               sslFlag;

    /**
     * データを初期化します。
     */
    public DataMasterUseragent()
    {
        seq = 0;
        carrierFlag = 0;
        userAgent = "";
        officialFlag = 1;
        performanceFlag = 0;
        gpsFlag = 0;
        sslFlag = 1;
    }

    public int getCarrierFlag()
    {
        return carrierFlag;
    }

    public int getGpsFlag()
    {
        return gpsFlag;
    }

    public int getOfficialFlag()
    {
        return officialFlag;
    }

    public int getPerformanceFlag()
    {
        return performanceFlag;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getSslFlag()
    {
        return sslFlag;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public void setCarrierFlag(int carrierFlag)
    {
        this.carrierFlag = carrierFlag;
    }

    public void setGpsFlag(int gpsFlag)
    {
        this.gpsFlag = gpsFlag;
    }

    public void setOfficialFlag(int officialFlag)
    {
        this.officialFlag = officialFlag;
    }

    public void setPerformanceFlag(int performanceFlag)
    {
        this.performanceFlag = performanceFlag;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setSslFlag(int sslFlag)
    {
        this.sslFlag = sslFlag;
    }

    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }

    /**
     * 携帯ユーザエージェント情報取得
     * 
     * @param request HTTPリクエスト
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(HttpServletRequest request)
    {
        String query;
        String agent;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_useragent WHERE useragent LIKE ?";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );

            prestate.setString( 1, request.getHeader( "user-agent" ) );
            agent = request.getHeader( "user-agent" );
            if ( agent.startsWith( "Semulator" ) != false )
            {
                agent.replaceAll( "Semulator", "SoftBank" );
                prestate.setString( 1, agent );
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.seq = result.getInt( "seq" );
                    this.carrierFlag = result.getInt( "carrier_flag" );
                    this.userAgent = result.getString( "useragent" );
                    this.officialFlag = result.getInt( "official_flag" );
                    this.performanceFlag = result.getInt( "performance_flag" );
                    this.gpsFlag = result.getInt( "gps_flag" );
                    this.sslFlag = result.getInt( "ssl_flag" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterUseragent.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 携帯ユーザエージェントデータ設定
     * 
     * @param result 携帯ユーザエージェントレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.seq = result.getInt( "seq" );
                this.carrierFlag = result.getInt( "carrier_flag" );
                this.userAgent = result.getString( "useragent" );
                this.officialFlag = result.getInt( "official_flag" );
                this.performanceFlag = result.getInt( "performance_flag" );
                this.gpsFlag = result.getInt( "gps_flag" );
                this.sslFlag = result.getInt( "ssl_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterUseragent.setData] Exception=" + e.toString() );
        }

        return(true);
    }
}
