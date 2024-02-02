/*
 * @(#)FortunateEventPay 1.00 2009/11/18
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
import jp.happyhotel.data.DataFortunateEventPay;
import jp.happyhotel.data.DataMasterFortune;

/**
 * 有料イベント占い情報取得クラス
 * 有料のイベント占い情報を扱うクラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/11/18
 */
public class FortunateEventPay implements Serializable
{
    private static final long     serialVersionUID = -4305973605981647598L;

    private int                   masterCount;
    private DataFortunateEventPay m_FortunateEventPay;
    private String                m_Fortune;                               // 星座名
    private String                m_FortuneFull;                           // 星座名(該当日付込み)
    private final int             Fortune          = 0;

    /**
     * データを初期化します。
     */
    public FortunateEventPay()
    {
        masterCount = 0;
        m_Fortune = "";
        m_FortuneFull = "";
    }

    /** イベント占い情報件数取得 **/
    public int getCount()
    {
        return(masterCount);
    }

    /** イベント占い情報取得 **/
    public DataFortunateEventPay getFortunateEventPayInfo()
    {
        return(m_FortunateEventPay);
    }

    /** 占いマスタ情報取得（星座） **/
    public String getMasterFortune()
    {
        return(m_Fortune);
    }

    /** 占いマスタ情報取得（星座　該当日付込み） **/
    public String getMasterFortuneFull()
    {
        return(m_FortuneFull);
    }

    /**
     * イベント占いデータを取得する
     * 
     * @param today 今日の日付(YYYYMMDDまたは、YYYYMMで指定する)
     * @param constellation 星座
     * @param userAgentType ユーザーエージェントタイプ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getFortunateEventPay(int today, int constellation)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        DataMasterFortune dmf;

        if ( today <= 0 || constellation <= 0 )
        {
            return(false);
        }
        // 今日の日付(YYYYYMMDD)を(YYYYMM)に変換させる
        if ( Integer.toString( today ).length() == 8 )
        {
            today /= 100;
        }

        query = "SELECT * FROM hh_fortunate_event_pay";
        query = query + " WHERE date = ?";
        query = query + " AND constellation = ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, today );
            prestate.setInt( 2, constellation );
            ret = getFortunateEventPaySub( prestate );
            if ( ret != false )
            {
                dmf = new DataMasterFortune();
                ret = dmf.getData( Fortune, this.m_FortunateEventPay.getConstellation() );
                if ( ret != false )
                {
                    // 該当日付込みの星座名と、星座名を取得する
                    this.m_FortuneFull = dmf.getName();
                    this.m_Fortune = m_FortuneFull.substring( 0, m_FortuneFull.indexOf( '(' ) );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getFortunateEventPay] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * イベント占い情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getFortunateEventPaySub(PreparedStatement prestate)
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
                this.m_FortunateEventPay = new DataFortunateEventPay();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // イベント占い情報の設定
                    this.m_FortunateEventPay.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getFortunateEventPaySub] Exception=" + e.toString() );
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

}
