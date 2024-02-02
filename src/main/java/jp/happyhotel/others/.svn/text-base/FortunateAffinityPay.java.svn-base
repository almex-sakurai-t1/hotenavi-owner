/*
 * @(#)FortunatePay 1.00 2009/11/18
 * Copyright (C) ALMEX Inc. 2009
 * 有料イベント占い情報取得クラス
 */

package jp.happyhotel.others;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataFortunateAffinityPay;
import jp.happyhotel.data.DataMasterFortune;

/**
 * 有料相性占い情報取得クラス
 * 有料の相性占い情報を扱うクラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/11/18
 */
public class FortunateAffinityPay implements Serializable
{
    private static final long        serialVersionUID = -4305973605981647598L;

    private int                      masterCount;
    private DataFortunateAffinityPay m_FortunateAffinityPay;
    private String                   m_Fortune1;                              // 星座名
    private String                   m_Fortune1Full;                          // 星座名(該当日付込み)
    private String                   m_Fortune1Color;
    private String                   m_Fortune2;                              // 星座名
    private String                   m_Fortune2Full;                          // 星座名(該当日付込み)
    private String                   m_Fortune2Color;
    private final int                Fortune          = 0;

    /**
     * データを初期化します。
     */
    public FortunateAffinityPay()
    {
        masterCount = 0;
        m_Fortune1 = "";
        m_Fortune1Full = "";
        m_Fortune1Color = "";
        m_Fortune2 = "";
        m_Fortune2Full = "";
        m_Fortune2Color = "";
    }

    /** 相性占い情報件数取得 **/
    public int getCount()
    {
        return(masterCount);
    }

    /** 相性占い情報取得 **/
    public DataFortunateAffinityPay getFortunateAffinityPayInfo()
    {
        return(m_FortunateAffinityPay);
    }

    /** 占いマスタ情報取得（ユーザ星座） **/
    public String getMasterFortune1()
    {
        return(m_Fortune1);
    }

    /** 占いマスタ情報取得（ユーザ星座　該当日付込み） **/
    public String getMasterFortune1Full()
    {
        return(m_Fortune1Full);
    }

    /** 占いマスタ情報取得（ユーザ星座　色） **/
    public String getMasterFortune1Color()
    {
        return(m_Fortune1Color);
    }

    /** 占いマスタ情報取得（パートナー星座） **/
    public String getMasterFortune2()
    {
        return(m_Fortune2);
    }

    /** 占いマスタ情報取得（パートナー星座　該当日付込み） **/
    public String getMasterFortune2Full()
    {
        return(m_Fortune2Full);
    }

    /** 占いマスタ情報取得（パートナー星座　色） **/
    public String getMasterFortune2Color()
    {
        return(m_Fortune2Color);
    }

    /**
     * 相性占いデータを取得する
     * 
     * @param today 今日の日付(YYYYMMDDまたは、YYYYMMで指定する)
     * @param constellation1 星座(ユーザー)
     * @param constellation2 星座(パートナー)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getFortunateAffinityPay(int today, int constellation1, int constellation2)
    {
        boolean ret;
        int nConste1; // 比較して小さい方の星座を代入
        int nConste2; // 比較して大きい方の星座を代入
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        DataMasterFortune dmf;

        nConste1 = constellation1;
        nConste2 = constellation2;
        if ( today <= 0 || constellation1 <= 0 || constellation2 <= 0 )
        {
            return(false);
        }

        // 星座の大小を比較し、小さい方をnConste1に代入
        if ( constellation1 > constellation2 )
        {
            nConste1 = constellation2;
            nConste2 = constellation1;
        }

        query = "SELECT * FROM hh_fortunate_affinity_pay";
        query = query + " WHERE date = ?";
        query = query + " AND constellation1 = ?";
        query = query + " AND constellation2 = ?";
        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, today );
            prestate.setInt( 2, nConste1 );
            prestate.setInt( 3, nConste2 );
            ret = getFortunateAffinityPaySub( prestate );
            if ( ret != false )
            {
                dmf = new DataMasterFortune();
                // ユーザの星座を取得
                ret = dmf.getData( Fortune, constellation1 );
                if ( ret != false )
                {
                    // 該当日付込みの星座名と、星座名を取得する
                    this.m_Fortune1Full = dmf.getName();
                    this.m_Fortune1 = m_Fortune1Full.substring( 0, m_Fortune1Full.indexOf( '(' ) );
                    this.m_Fortune1Color = dmf.getColor();
                }
                // パートナーの星座を取得
                ret = dmf.getData( Fortune, constellation2 );
                if ( ret != false )
                {
                    // 該当日付込みの星座名と、星座名を取得する
                    this.m_Fortune2Full = dmf.getName();
                    this.m_Fortune2 = m_Fortune2Full.substring( 0, m_Fortune2Full.indexOf( '(' ) );
                    this.m_Fortune2Color = dmf.getColor();
                }

            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getFortunateAffinityPay] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 相性占い情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getFortunateAffinityPaySub(PreparedStatement prestate)
    {
        ResultSet result = null;

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    masterCount = result.getRow();
                }
                this.m_FortunateAffinityPay = new DataFortunateAffinityPay();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // 相性占い情報の設定
                    this.m_FortunateAffinityPay.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getFortunateAffinityPaySub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( masterCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * 星座情報を取得する
     * 
     * @param constellation1 星座(ユーザー)
     * @param constellation2 星座(パートナー)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterFortune(int constellation1, int constellation2)
    {
        boolean ret;
        DataMasterFortune dmf;

        ret = false;
        if ( constellation1 < 0 || constellation2 < 0 )
        {
            return(false);
        }

        try
        {
            dmf = new DataMasterFortune();
            // ユーザの星座を取得
            ret = dmf.getData( Fortune, constellation1 );
            if ( ret != false )
            {
                // 該当日付込みの星座名と、星座名を取得する
                this.m_Fortune1Full = dmf.getName();
                if ( this.m_Fortune1Full.compareTo( "" ) != 0 )
                {
                    this.m_Fortune1 = m_Fortune1Full.substring( 0, m_Fortune1Full.indexOf( '(' ) );
                    this.m_Fortune1Color = dmf.getColor();
                }
            }
            else
            {
                this.m_Fortune1Full = "";
                this.m_Fortune1 = "";
                this.m_Fortune1Color = "";
            }

            // DataMasterFortuneをクリアする
            dmf = null;
            dmf = new DataMasterFortune();

            // パートナーの星座を取得
            ret = dmf.getData( Fortune, constellation2 );
            if ( ret != false )
            {
                // 該当日付込みの星座名と、星座名を取得する
                this.m_Fortune2Full = dmf.getName();
                if ( this.m_Fortune2Full.compareTo( "" ) != 0 )
                {
                    this.m_Fortune2 = m_Fortune2Full.substring( 0, m_Fortune2Full.indexOf( '(' ) );
                    this.m_Fortune2Color = dmf.getColor();
                }
            }
            else
            {
                this.m_Fortune2Full = "";
                this.m_Fortune2 = "";
                this.m_Fortune2Color = "";
            }
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.info( "[getFortunateMaster] Exception=" + e.toString() );
        }
        finally
        {
        }

        return(ret);
    }
}
