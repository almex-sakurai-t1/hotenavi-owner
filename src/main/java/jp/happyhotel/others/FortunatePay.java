/*
 * @(#)FortunatePay 1.00 2009/08/13
 * Copyright (C) ALMEX Inc. 2009
 * 有料占い情報取得クラス
 */

package jp.happyhotel.others;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataFortunatePay;
import jp.happyhotel.data.DataMasterFortune;

/**
 * 有料占い情報取得クラス
 * 有料の占い情報を扱うクラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/08/13
 */
public class FortunatePay implements Serializable
{
    private static final long serialVersionUID   = -4305973605981647598L;

    private int               masterCount;
    private DataFortunatePay  m_FortunatePay;
    private String            m_Fortune;                                 // 星座名
    private String            m_FortuneFull;                             // 星座名(該当日付込み)
    private String            m_FortuneItem;                             // ラッキー
    private String            m_FortuneHotel;                            // ラッキーホテル
    private String            m_FortuneItemUrl;                          // ラッキーアイテムURL
    private String            m_FortuneHotelUrl;                         // ラッキーホテルURL
    private final int         Fortune            = 0;
    private final int         FortuneItem        = 1;
    private final int         FortuneHotel       = 2;
    /** 端末種別：DoCoMo **/
    public static final int   USERAGENT_DOCOMO   = 1;
    /** 端末種別：au **/
    public static final int   USERAGENT_AU       = 2;
    /** 端末種別：J-PHONE,Vodafone **/
    public static final int   USERAGENT_JPHONE   = 3;
    /** 端末種別：J-PHONE,Vodafone **/
    public static final int   USERAGENT_VODAFONE = 3;
    /** 端末種別：J-PHONE,Vodafone,SoftBank **/
    public static final int   USERAGENT_SOFTBANK = 3;
    /** 端末種別：pc **/
    public static final int   USERAGENT_PC       = 4;

    /**
     * データを初期化します。
     */
    public FortunatePay()
    {
        masterCount = 0;
        m_Fortune = "";
        m_FortuneFull = "";
        m_FortuneItem = "";
        m_FortuneHotel = "";
        m_FortuneItemUrl = "";
        m_FortuneHotelUrl = "";
    }

    /** 占いランク情報件数取得 **/
    public int getCount()
    {
        return(masterCount);
    }

    /** 占いランク情報取得 **/
    public DataFortunatePay getFortunatePayInfo()
    {
        return(m_FortunatePay);
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

    /** 占いマスタ情報取得（ラッキーアイテム） **/
    public String getMasterFortuneItem()
    {
        return(m_FortuneItem);
    }

    /** 占いマスタ情報取得（ラッキーアイテム） **/
    public String getMasterFortuneItemUrl()
    {
        return(m_FortuneItemUrl);
    }

    /** 占いマスタ情報取得（ラッキーホテル） **/
    public String getMasterFortuneHotel()
    {
        return(m_FortuneHotel);
    }

    /** 占いマスタ情報取得（ラッキーホテル） **/
    public String getMasterFortuneHotelUrl()
    {
        return(m_FortuneHotelUrl);
    }

    /**
     * 占いデータを取得する
     * 
     * @param today 今日の日付
     * @param constellation 星座
     * @param userAgentType ユーザーエージェントタイプ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getFortunatePay(int today, int constellation, int userAgentType)
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

        query = "SELECT * FROM hh_fortunate_pay";
        query = query + " WHERE date = ?";
        query = query + " AND constellation = ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, today );
            prestate.setInt( 2, constellation );
            ret = geFortuneSub( prestate );
            if ( ret != false )
            {
                dmf = new DataMasterFortune();
                ret = dmf.getData( Fortune, this.m_FortunatePay.getConstellation() );
                if ( ret != false )
                {
                    // 該当日付込みの星座名と、星座名を取得する
                    this.m_FortuneFull = dmf.getName();
                    this.m_Fortune = m_FortuneFull.substring( 0, m_FortuneFull.indexOf( '(' ) );
                }
                ret = dmf.getData( FortuneItem, this.m_FortunatePay.getLuckyItem() );
                if ( ret != false )
                {
                    this.m_FortuneItem = dmf.getName();
                    if ( userAgentType == USERAGENT_DOCOMO )
                    {
                        this.m_FortuneItemUrl = dmf.getUrlDocomo();
                    }
                    else if ( userAgentType == USERAGENT_AU )
                    {
                        this.m_FortuneItemUrl = dmf.getUrlAu();
                    }
                    else if ( userAgentType == USERAGENT_SOFTBANK )
                    {
                        this.m_FortuneItemUrl = dmf.getUrlSoftBank();
                    }
                    else
                    {
                        this.m_FortuneItemUrl = dmf.getUrl();
                    }
                }
                ret = dmf.getData( FortuneHotel, this.m_FortunatePay.getLuckyHotel() );
                if ( ret != false )
                {
                    this.m_FortuneHotel = dmf.getName();
                    if ( userAgentType == USERAGENT_DOCOMO )
                    {
                        this.m_FortuneHotelUrl = dmf.getUrlDocomo();
                    }
                    else if ( userAgentType == USERAGENT_AU )
                    {
                        this.m_FortuneHotelUrl = dmf.getUrlAu();
                    }
                    else if ( userAgentType == USERAGENT_SOFTBANK )
                    {
                        this.m_FortuneHotelUrl = dmf.getUrlSoftBank();
                    }
                    else
                    {
                        this.m_FortuneHotelUrl = dmf.getUrl();
                    }
                }

            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getFortunatePay] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * プレゼント情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean geFortuneSub(PreparedStatement prestate)
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
                this.m_FortunatePay = new DataFortunatePay();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // 占いランキング情報の設定
                    this.m_FortunatePay.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[geFortuneSub] Exception=" + e.toString() );
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
