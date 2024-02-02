/*
 * @(#)SponsorData.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 スポンサーデータ取得クラス
 */
package jp.happyhotel.sponsor;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataMasterLocal;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataMasterSponsor;
import jp.happyhotel.data.DataSponsorData;
import jp.happyhotel.search.SearchEngineBasic;

/**
 * スポンサーデータ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.00 2007/12/03
 */
public class SponsorData_M2 implements Serializable
{
    /**
     *
     */
    private static final long   serialVersionUID     = 49553862735802305L;

    private int                 sponsorCount;
    private boolean             sponsorDataStatus;
    private DataMasterSponsor[] sponsor;
    /* 特典用変数 */
    private int                 hotelCount;
    private int                 hotelAllCount;
    private int                 localCount;
    private int[]               prefCount;
    private DataMasterLocal     dmLocal;
    private DataMasterPref[]    dmPref;
    private DataHotelBasic[]    m_hotelBasic;

    /** 端末種別：DoCoMo **/
    public static final int     USERAGENT_DOCOMO     = 1;
    /** 端末種別：au **/
    public static final int     USERAGENT_AU         = 2;
    /** 端末種別：J-PHONE,Vodafone **/
    public static final int     USERAGENT_JPHONE     = 3;
    /** 端末種別：J-PHONE,Vodafone **/
    public static final int     USERAGENT_VODAFONE   = 3;
    /** 端末種別：J-PHONE,Vodafone,SoftBank **/
    public static final int     USERAGENT_SOFTBANK   = 3;
    /** 端末種別：pc **/
    public static final int     USERAGENT_PC         = 4;
    /** 端末種別：pc **/
    public static final int     USERAGENT_SMARTPHONE = 5;

    public SponsorData_M2()
    {
        sponsorCount = 0;
        hotelCount = 0;
        hotelAllCount = 0;
        localCount = 0;
    }

    public DataMasterSponsor[] getSponsor()
    {
        return sponsor;
    }

    public int getSponsorCount()
    {
        return sponsorCount;
    }

    public boolean isSponsorDataStatus()
    {
        return sponsorDataStatus;
    }

    public void setSponsorDataStatus(boolean sponsorDataStatus)
    {
        this.sponsorDataStatus = sponsorDataStatus;
    }

    /* ここから下は特典用のメソッド */
    /** ホテル件数取得（1ページの件数） **/
    public int getPrivilegeHotelCount()
    {
        return hotelCount;
    }

    /** ホテル件数取得 **/
    public int getPrivilegeHotelAllCount()
    {
        return hotelAllCount;
    }

    /** ホテル情報取得 **/
    public DataHotelBasic[] getPrivilegeHotelInfo()
    {
        return(m_hotelBasic);
    }

    /** 地方件数取得 **/
    public int getPrivilegeLocalCount()
    {
        return localCount;
    }

    /** 都道府県件数取得 **/
    public int[] getPrivilegePrefCount()
    {
        return prefCount;
    }

    /** 地方マスタ取得 **/
    public DataMasterLocal getPrivilegeLocal()
    {
        return dmLocal;
    }

    /** 都道府県マスタ取得 **/
    public DataMasterPref[] getPrivilegePref()
    {
        return dmPref;
    }

    /**
     * スポンサー情報取得（都道府県ID）
     * 
     * @param prefId 都道府県ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getSponsorByPref(int prefId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_pref"
                + " WHERE hh_master_sponsor.sponsor_code <> 0"
                + " AND hh_master_sponsor.area_code <> 0"
                + " AND hh_master_pref.pref_id = ?"
                + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code"
                + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
                + " AND hh_master_sponsor.random_disp_flag = 0"
                + " ORDER BY hh_master_sponsor.disp_pos";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // Gets Sponsor Data
            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData.getSponsorByPref( int prefId = " + prefId + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ローテーションバナー用スポンサー情報取得（都道府県ID）
     * 
     * @param prefId 都道府県ID
     * @param dispCount 取得件数
     * @param mobileFlag 携帯フラグ(true:携帯,false：PC)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getRandomSponsorByPref(int prefId, int dispCount, boolean mobileFlag) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT sponsor.* FROM hh_master_sponsor sponsor"
                + " INNER JOIN hh_master_pref pref ON pref.sponsor_area_code = sponsor.area_code"
                + " AND pref.pref_id = ?"
                + " WHERE sponsor.sponsor_code <> 0"
                + " AND sponsor.area_code <> 0"
                + " AND sponsor.start_date <= ? AND sponsor.end_date >= ?"
                + " AND sponsor.random_disp_flag = 2"
                + " ORDER BY RAND() LIMIT 0,?";

        /*
         * // 表示対象のもの
         * query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_pref, hh_sponsor_data"
         * + " WHERE hh_master_sponsor.sponsor_code <> 0"
         * + " AND hh_master_sponsor.area_code <> 0"
         * + " AND hh_master_sponsor.sponsor_code = hh_sponsor_data.sponsor_code"
         * + " AND hh_master_pref.pref_id = ?"
         * + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code"
         * + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
         * + " AND hh_sponsor_data.addup_date = ?"
         * + " AND hh_master_sponsor.random_disp_flag = 2";
         * // 表示区分により
         * if ( mobileFlag != false )
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression_mobile LIMIT 0,?";
         * }
         * else
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression LIMIT 0,?";
         * }
         */
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, dispCount );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData.getSponsorByPref( int prefId = " + prefId + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ローテーションバナー用スポンサー情報取得（都道府県ID）
     * 
     * @param prefId 都道府県ID
     * @param dispCount 取得件数
     * @param dispFlag 携帯フラグ(0:PC,1:携帯,2：スマートフォン)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getRandomSponsorByPref(int prefId, int dispCount, int dispFlag) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT sponsor.* FROM hh_master_sponsor sponsor"
                + " INNER JOIN hh_master_pref pref ON pref.sponsor_area_code = sponsor.area_code"
                + " AND pref.pref_id = ?"
                + " WHERE sponsor.sponsor_code <> 0"
                + " AND sponsor.area_code <> 0"
                + " AND sponsor.start_date <= ? AND sponsor.end_date >= ?"
                + " AND sponsor.random_disp_flag = 2"
                + " ORDER BY RAND() LIMIT 0,?";
        /*
         * query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_pref, hh_sponsor_data"
         * + " WHERE hh_master_sponsor.sponsor_code <> 0"
         * + " AND hh_master_sponsor.area_code <> 0"
         * + " AND hh_master_sponsor.sponsor_code = hh_sponsor_data.sponsor_code"
         * + " AND hh_master_pref.pref_id = ?"
         * + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code"
         * + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
         * + " AND hh_sponsor_data.addup_date = ?"
         * + " AND hh_master_sponsor.random_disp_flag = 2";
         * // 表示区分により
         * if ( dispFlag == 0 )
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression_mobile LIMIT 0,?";
         * }
         * else if ( dispFlag == 1 )
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression LIMIT 0,?";
         * }
         * else if ( dispFlag == 2 )
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression_smart LIMIT 0,?";
         * }
         */
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, dispCount );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getSponsorByPref( int prefId = " + prefId + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * スポンサー情報取得（市区町村コード）
     * 
     * @param jisCode 市区町村コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getSponsorByCity(int jisCode) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_city"
                + " WHERE hh_master_sponsor.sponsor_code <> 0"
                + " AND hh_master_sponsor.area_code <> 0"
                + " AND hh_master_city.jis_code = ?"
                + " AND hh_master_city.sponsor_area_code = hh_master_sponsor.area_code"
                + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
                + " AND hh_master_sponsor.random_disp_flag = 0"
                + " ORDER BY hh_master_sponsor.disp_pos";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, jisCode );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // Fetches Sponsor Data
            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getSponsorByCity( int jisCode = " + jisCode + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * スポンサー情報取得（ホテル街コード）
     * 
     * @param areaId エリアID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getSponsorByArea(int areaId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_area"
                + " WHERE hh_master_sponsor.sponsor_code <> 0"
                + " AND hh_master_sponsor.area_code <> 0"
                + " AND hh_master_area.area_id = ?"
                + " AND hh_master_area.sponsor_area_code = hh_master_sponsor.area_code"
                + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
                + " AND hh_master_sponsor.random_disp_flag = 0"
                + " ORDER BY hh_master_sponsor.disp_pos";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, areaId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData.getSponsorByArea] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * スポンサー情報取得（表示位置指定）
     * 
     * @param dispPos 表示位置区分
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getSponsorByDispPos(String dispPos) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor"
                + " WHERE hh_master_sponsor.sponsor_code <> 0"
                + " AND hh_master_sponsor.area_code <> 0"
                + " AND hh_master_sponsor.disp_pos = ?"
                + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
                + " AND hh_master_sponsor.random_disp_flag = 0"
                + " ORDER BY hh_master_sponsor.disp_pos";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, dispPos );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData.getSponsorByDispPos] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * スポンサー情報をセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    private boolean getSponsorDataSub(PreparedStatement prestate) throws Exception
    {
        int i;
        int count = 0;
        boolean ret = false;
        ResultSet result = null;

        this.sponsorCount = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.sponsorCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.sponsor = new DataMasterSponsor[this.sponsorCount];
                for( i = 0 ; i < sponsorCount ; i++ )
                {
                    sponsor[i] = new DataMasterSponsor();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // スポンサー情報の設定
                    this.sponsor[count].setData( result );

                    count++;
                }
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[SponsorData_M2.getSponsorDataSub(prestate)] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        if ( sponsorCount > 0 )
            ret = true;
        else
            ret = false;

        return(ret);
    }

    /**
     * インプレッションカウント
     * 
     * @param sponsorCode スポンサーコード
     * @param mobileFlag 携帯フラグ（true:携帯）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setImpressionCount(int sponsorCode, boolean mobileFlag)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // 対象広告のデータを取得する
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            if ( mobileFlag != false )
            {
                dsd.setImpressionMobile( dsd.getImpressionMobile() + 1 );
            }
            else
            {
                dsd.setImpression( dsd.getImpression() + 1 );
            }

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // 失敗したらログに残す
            if ( ret == false )
            {
                Logging.error( "[SponsorData_M2.setImpressionCount updateData] Exception sponsorCode:" + sponsorCode
                        + ", Impression:" + Integer.toString( dsd.getImpression() + 1 ) );
            }
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setSponsorCode( sponsorCode );
            if ( mobileFlag != false )
            {
                dsd.setImpressionMobile( 1 );
            }
            else
            {
                dsd.setImpression( 1 );
            }

            ret = dsd.insertData();
            // 失敗したらログに残す
            if ( ret == false )
            {
                Logging.error( "[SponsorData_M2.setImpressionCount insertData] Exception sponsorCode:" + sponsorCode );

            }
        }

        return(ret);
    }

    /**
     * クリックカウント
     * 
     * @param sponsorCode スポンサーコード
     * @param mobileFlag 携帯フラグ（true:携帯）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setClickCount(int sponsorCode, boolean mobileFlag)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // 対象広告のデータを取得する
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            if ( mobileFlag != false )
            {
                dsd.setClickCountMobile( dsd.getClickCountMobile() + 1 );
            }
            else
            {
                dsd.setClickCount( dsd.getClickCount() + 1 );
            }

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setSponsorCode( sponsorCode );
            if ( mobileFlag != false )
            {
                dsd.setClickCountMobile( 1 );
            }
            else
            {
                dsd.setClickCount( 1 );
            }

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * 拡張クリックカウント
     * 
     * @param sponsorCode スポンサーコード
     * @param mobileFlag 携帯フラグ（true:携帯）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setExClickCount(int sponsorCode, boolean mobileFlag)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // 対象広告のデータを取得する
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            if ( mobileFlag != false )
            {
                dsd.setExClickCountMobile( dsd.getExClickCountMobile() + 1 );
            }
            else
            {
                dsd.setExClickCount( dsd.getExClickCount() + 1 );
            }

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setSponsorCode( sponsorCode );
            if ( mobileFlag != false )
            {
                dsd.setExClickCountMobile( 1 );
            }
            else
            {
                dsd.setExClickCount( 1 );
            }

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * 拡張クリックカウント２
     * 
     * @param sponsorCode スポンサーコード
     * @param mobileFlag 携帯フラグ（true:携帯）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setExClickCount2(int sponsorCode, boolean mobileFlag)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // 対象広告のデータを取得する
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            if ( mobileFlag != false )
            {
                dsd.setExClickCountMobile2( dsd.getExClickCountMobile() + 1 );
            }
            else
            {
                dsd.setExClickCount2( dsd.getExClickCount() + 1 );
            }

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setSponsorCode( sponsorCode );
            if ( mobileFlag != false )
            {
                dsd.setExClickCountMobile2( 1 );
            }
            else
            {
                dsd.setExClickCount2( 1 );
            }

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * 拡張インプレッションカウント
     * 
     * @param sponsorCode スポンサーコード
     * @param mobileFlag 携帯フラグ（true:携帯）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setExImpressionCount(int sponsorCode, boolean mobileFlag)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // 対象広告のデータを取得する
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            if ( mobileFlag != false )
            {
                dsd.setExImpressionMobile( dsd.getExImpressionMobile() + 1 );
            }
            else
            {
                dsd.setExImpression( dsd.getExImpression() + 1 );
            }

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setSponsorCode( sponsorCode );
            if ( mobileFlag != false )
            {
                dsd.setExImpressionMobile( 1 );
            }
            else
            {
                dsd.setExImpression( 1 );
            }

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * アフィリエイト情報取得（ランダムで１件）
     * 
     * @param userAgentType ユーザーエージェントタイプ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getAffiliateRandom(int userAgentType)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i;
        int datacount;
        int count;

        // 表示しているキャリアに該当するurlが登録されているレコードの中からランダムで取得する。
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT sponsor_code FROM hh_master_sponsor";
        query = query + " WHERE area_code = '0'";
        query = query + " AND disp_pos = '1'";
        query = query + " AND start_date <= ?";
        query = query + " AND end_date >= ?";

        if ( userAgentType == USERAGENT_DOCOMO )
        {
            query = query + " AND url_docomo != ''";
        }
        else if ( userAgentType == USERAGENT_AU )
        {
            query = query + " AND url_au != ''";
        }
        else if ( userAgentType == USERAGENT_SOFTBANK )
        {
            query = query + " AND url_softbank != ''";
        }
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";
        query = query + " LIMIT 0,1";

        count = 0;
        datacount = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    datacount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.sponsor = new DataMasterSponsor[datacount];
                for( i = 0 ; i < datacount ; i++ )
                {
                    sponsor[i] = new DataMasterSponsor();
                }
                result.beforeFirst();
                if ( result.next() != false )
                {
                    // アフィリエイト情報の取得
                    this.sponsor[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData.getAffiliaterandom] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( datacount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * アフィリエイト情報取得（ランダム）
     * 
     * @param userAgentType ユーザーエージェントタイプ
     * @param dispCount 表示数
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getAffiliateRandom(int userAgentType, int dispCount) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i;
        int count;

        // 表示しているキャリアに該当するurlが登録されているレコードの中からランダムで取得する。
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT sponsor_code FROM hh_master_sponsor";
        query = query + " WHERE area_code = '0'";
        query = query + " AND disp_pos = '1'";
        query = query + " AND start_date <= ?";
        query = query + " AND end_date >= ?";

        if ( userAgentType == USERAGENT_DOCOMO )
        {
            query = query + " AND url_docomo != ''";
        }
        else if ( userAgentType == USERAGENT_AU )
        {
            query = query + " AND url_au != ''";
        }
        else if ( userAgentType == USERAGENT_SOFTBANK )
        {
            query = query + " AND url_softbank != ''";
        }
        else
        {
            query = query + " AND url != '' ";
        }
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";
        query = query + " LIMIT 0,?";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, dispCount );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    sponsorCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.sponsor = new DataMasterSponsor[sponsorCount];
                for( i = 0 ; i < sponsorCount ; i++ )
                {
                    sponsor[i] = new DataMasterSponsor();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // アフィリエイト情報の取得
                    this.sponsor[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            sponsorCount = 0;
            Logging.error( "[SponsorData.getAffiliaterandom] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( sponsorCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * 企画広告ホテル情報取得
     * 
     * @param areaCode エリアコード
     * @param randomFlag ランダムフラグ(0:disp_pos順、1:ランダム)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getAdHotelList(int areaCode, int randomFlag) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i;
        int count;

        if ( randomFlag == 0 )
        {
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE area_code = ?";
            query = query + " AND start_date <= ?";
            query = query + " AND end_date >= ?";
            query = query + " ORDER BY disp_pos";
        }
        else if ( randomFlag == 1 )
        {
            query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
            query = query + " INNER JOIN (";
            query = query + " SELECT sponsor_code FROM hh_master_sponsor";
            query = query + " WHERE area_code = ?";
            query = query + " AND start_date <= ?";
            query = query + " AND end_date >= ?";
            query = query + " ORDER BY RAND()";
            query = query + " ) AS random";
            query = query + " ON hhms.sponsor_code = random.sponsor_code";
        }
        // 不正なパラメータを受け取った場合の処理
        else
        {
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE area_code = ?";
            query = query + " AND start_date <= ?";
            query = query + " AND end_date >= ?";
        }

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, areaCode );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.sponsorCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.sponsor = new DataMasterSponsor[this.sponsorCount];
                for( i = 0 ; i < sponsorCount ; i++ )
                {
                    sponsor[i] = new DataMasterSponsor();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // 企画広告ホテル情報の取得
                    this.sponsor[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            sponsorCount = 0;
            Logging.error( "[SponsorData.getAdHotelList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( sponsorCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * 企画広告ホテル情報取得（スポンサーコード指定）
     * 
     * @param sponsorCode 表示位置区分
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getAdHotelBySponsorCode(int sponsorCode) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i;
        int count;

        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor"
                + " WHERE hh_master_sponsor.area_code <> 0"
                + " AND hh_master_sponsor.sponsor_code = ?"
                + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
                + " AND hh_master_sponsor.random_disp_flag = 0"
                + " ORDER BY hh_master_sponsor.disp_pos";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, sponsorCode );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.sponsorCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.sponsor = new DataMasterSponsor[this.sponsorCount];
                for( i = 0 ; i < sponsorCount ; i++ )
                {
                    sponsor[i] = new DataMasterSponsor();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // 企画広告ホテル情報の取得
                    this.sponsor[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            sponsorCount = 0;
            Logging.error( "[SponsorData.getAdHotelList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( sponsorCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * TOPバナー情報取得（ランダム）
     * 
     * @param userAgentType ユーザーエージェントタイプ
     * @param dispCount 表示数
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getBannerRandom(int userAgentType, int dispCount) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i;
        int count;

        // 表示しているキャリアに該当するurlが登録されているレコードの中からランダムで取得する。
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT sponsor_code FROM hh_master_sponsor";
        query = query + " WHERE sponsor_code > 2000000";
        query = query + " AND sponsor_code < 3000000";
        query = query + " AND area_code = 0";
        query = query + " AND random_disp_flag = 1";
        query = query + " AND start_date <= ?";
        query = query + " AND end_date >= ?";

        if ( userAgentType == USERAGENT_DOCOMO )
        {
            query = query + " AND url_docomo != ''";
        }
        else if ( userAgentType == USERAGENT_AU )
        {
            query = query + " AND url_au != ''";
        }
        else if ( userAgentType == USERAGENT_SOFTBANK )
        {
            query = query + " AND url_softbank != ''";
        }
        else
        {
            query = query + " AND url != '' ";
        }
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";
        query = query + " LIMIT 0,?";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, dispCount );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    sponsorCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.sponsor = new DataMasterSponsor[sponsorCount];
                for( i = 0 ; i < sponsorCount ; i++ )
                {
                    sponsor[i] = new DataMasterSponsor();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // アフィリエイト情報の取得
                    this.sponsor[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            sponsorCount = 0;
            Logging.error( "[SponsorData.getBannerRandom] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( sponsorCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * 携帯用スポンサー情報ランダム取得（都道府県ID）
     * 
     * @param prefId 都道府県ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSponsorRandomByPref(int prefId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT hh_master_sponsor.sponsor_code FROM hh_master_sponsor,hh_master_pref";
        query = query + " WHERE hh_master_sponsor.sponsor_code <> 0";
        query = query + " AND hh_master_sponsor.area_code <> 0";
        query = query + " AND hh_master_pref.pref_id = ?";
        query = query + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code";
        query = query + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)";
        query = query + " AND hh_master_sponsor.random_disp_flag = 0";
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getSponsorByPref] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 携帯用スポンサー情報（募集中を除く）ランダム取得（都道府県ID）
     * 
     * @param prefId 都道府県ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSponsorRandomByPrefAdOnly(int prefId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT hh_master_sponsor.sponsor_code FROM hh_master_sponsor,hh_master_pref";
        query = query + " WHERE hh_master_sponsor.sponsor_code <> 0";
        query = query + " AND hh_master_sponsor.area_code <> 0";
        query = query + " AND hh_master_pref.pref_id = ?";
        query = query + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code";
        query = query + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)";
        query = query + " AND hh_master_sponsor.random_disp_flag = 0";
        query = query + " AND hh_master_sponsor.hotel_id <> 99999999";
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getSponsorByPref] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 携帯用スポンサー情報ランダム取得（市区町村コード）
     * 
     * @param jisCode 市区町村コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSponsorRandomByCity(int jisCode) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT hh_master_sponsor.sponsor_code FROM hh_master_sponsor,hh_master_city";
        query = query + " WHERE hh_master_sponsor.sponsor_code <> 0";
        query = query + " AND hh_master_sponsor.area_code <> 0";
        query = query + " AND hh_master_city.jis_code = ?";
        query = query + " AND hh_master_city.sponsor_area_code = hh_master_sponsor.area_code";
        query = query + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)";
        query = query + " AND hh_master_sponsor.random_disp_flag = 0";
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, jisCode );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getSponsorByCity] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 携帯用スポンサー情報ランダム取得（ホテル街コード）
     * 
     * @param areaId エリアID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSponsorRandomByArea(int areaId) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT hh_master_sponsor.sponsor_code FROM hh_master_sponsor,hh_master_area";
        query = query + " WHERE hh_master_sponsor.sponsor_code <> 0";
        query = query + " AND hh_master_sponsor.area_code <> 0";
        query = query + " AND hh_master_area.area_id = ?";
        query = query + " AND hh_master_area.sponsor_area_code = hh_master_sponsor.area_code";
        query = query + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)";
        query = query + " AND hh_master_sponsor.random_disp_flag = 0";
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, areaId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getSponsorByArea] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * スポンサー情報取得（表示位置指定）
     * 
     * @param dispPos 表示位置区分
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSponsorRandomByDispPos(String dispPos) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT sponsor_code FROM hh_master_sponsor";
        query = query + " WHERE hh_master_sponsor.sponsor_code <> 0";
        query = query + " AND hh_master_sponsor.area_code <> 0";
        query = query + " AND hh_master_sponsor.disp_pos = ?";
        query = query + " AND hh_master_sponsor.title_mobile <> ''";
        query = query + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)";
        query = query + " AND hh_master_sponsor.random_disp_flag = 0";
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, dispPos );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getSponsorByDispPos] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 特典件数取得（都道府県IDから）
     * 
     * @param prefId 都道府県ID
     * @param memberFlag 会員フラグ(0:非会員,1:会員)
     * @return 特典件数
     */
    public int getPrivilegeCountByPref(int prefId, int memberFlag)
    {
        int count;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( prefId <= 0 )
        {
            return(-1);
        }
        query = "SELECT COUNT( * ) FROM hh_master_sponsor";
        query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND area_code = 100000";
        query = query + " AND pref_id = ?";
        if ( memberFlag == 0 )
        {
            query = query + " AND member_flag = 0 ";
        }
        else if ( memberFlag == 1 )
        {
            query = query + " AND( member_flag = 0 OR member_flag = 1 ) ";
        }
        query = query + " GROUP BY hotel_id";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            else
            {
                count = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getsponsorCountByPref] Exception=" + e.toString() );
            return(0);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    /**
     * 特典件数取得（地方IDから）
     * 
     * @param localId 地方ID
     * @param memberFlag 会員フラグ(0:非会員,1:会員)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPrivilegeCountByLocal(int localId, int memberFlag)
    {
        int i;
        int j;
        SearchEngineBasic seb;
        seb = new SearchEngineBasic();

        if ( localId < 0 )
            return(false);

        this.localCount = 0;
        seb.getLocalList( localId, 0 );
        try
        {
            if ( seb.getMasterLocalCount() > 0 )
            {
                // 地方IDの数だけループさせる
                for( i = 0 ; i < seb.getMasterLocalCount() ; i++ )
                {
                    // 地方IDに含まれる都道府県を取得する
                    seb.getPrefListByLocal( seb.getMasterLocal()[i].getLocalId(), 0 );
                    if ( seb.getMasterPrefCount() > 0 )
                    {
                        // 全国の件数取得の場合はカウントだけ取得する
                        if ( localId == 0 )
                        {
                            for( j = 0 ; j < seb.getMasterPrefCount() ; j++ )
                            {
                                this.localCount = this.localCount + getPrivilegeCountByPref( seb.getMasterPref()[j].getPrefId(), memberFlag );
                            }
                        }
                        // 地方ごとに件数取得する場合は、地方、都道府県の件数とデータを取得
                        else
                        {
                            // 地方のデータをセット
                            this.dmLocal = new DataMasterLocal();
                            this.dmLocal = seb.getMasterLocal()[i];
                            // 都道府県の配列を用意
                            this.dmPref = new DataMasterPref[seb.getMasterPrefCount()];
                            this.prefCount = new int[seb.getMasterPrefCount()];
                            for( j = 0 ; j < seb.getMasterPrefCount() ; j++ )
                            {
                                this.localCount = localCount + getPrivilegeCountByPref( seb.getMasterPref()[j].getPrefId(), memberFlag );

                                this.dmPref[j] = new DataMasterPref();
                                this.dmPref[j] = seb.getMasterPref()[j];
                                this.prefCount[j] = getPrivilegeCountByPref( seb.getMasterPref()[j].getPrefId(), memberFlag );
                            }
                        }
                    }
                    else
                    {
                        localCount = 0;
                        Logging.error( "[getsponsorCountByLocal] pref=0" );
                    }
                }
            }
            else
            {
                localCount = 0;
                Logging.error( "[getsponsorCountByLocal] local=0" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getsponsorCountByLocal] Exception=" + e.toString() );
            return false;
        }
        return true;
    }

    /**
     * 特典ホテル情報取得（都道府県ごとに）
     * 
     * @param prefId 都道府県ID
     * @param memberFlag 会員フラグ(0:非会員、1:正会員)
     * @param order ソートフラグ(0:disp_pos順、1:地域順、2:有効期限の昇順、3:ホテル名の昇順)
     * @param countNum 取得件数（0：全件 ※pageNum無視
     * @param pageNum ページ番号（0～)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPrivilegeHotel(int prefId, int memberFlag, int order, int countNum, int pageNum)
    {
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "";
        if ( order < 0 || prefId < 0 )
        {
            return(false);
        }
        else if ( order >= 0 && order <= 2 )
        {
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( member_flag = 0 OR member_flag = 1 ) ";
            }

            if ( prefId > 0 )
            {
                query = query + " AND pref_id = ?";
            }
            query = query + " AND hotel_id > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY hotel_id";
            // disp_pos順
            if ( order == 0 )
            {
                query = query + " ORDER BY start_date DESC";
            }
            // 地域順
            if ( order == 1 )
            {
                query = query + " ORDER BY pref_id, start_date DESC";
            }
            // 有効期限の終了日付の昇順
            else if ( order == 2 )
            {
                query = query + " ORDER BY end_date, pref_id, start_date DESC";
            }
        }
        // ホテル名の昇順
        else if ( order == 3 )
        {
            query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor, hh_hotel_basic, hh_hotel_pv";
            query = query + " WHERE hh_master_sponsor.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_basic.id ";
            query = query + " AND hh_hotel_pv.id = hh_hotel_basic.id ";
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_pv.id ";
            query = query + " AND hh_master_sponsor.area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND hh_master_sponsor.member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( hh_master_sponsor.member_flag = 0 OR hh_master_sponsor.member_flag = 1 ) ";
            }

            if ( prefId > 0 )
            {
                query = query + " AND hh_master_sponsor.pref_id = ?";
            }
            query = query + " AND hh_master_sponsor.hotel_id > 0";
            query = query + " GROUP BY hh_master_sponsor.hotel_id";
            query = query + " ORDER BY hh_hotel_basic.name_kana, hh_hotel_basic.rank DESC,";
            query = query + " hh_master_sponsor.start_date DESC";
        }

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.hotelCount = result.getRow();
                    Logging.error( "[getSortOfferHotel] count=" + result.getRow() );

                }
                m_hotelBasic = new DataHotelBasic[hotelCount];
                sponsor = new DataMasterSponsor[hotelCount];
                sponsorCount = hotelCount;

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelBasic[count] = new DataHotelBasic();
                    this.sponsor[count] = new DataMasterSponsor();

                    this.sponsor[count].setData( result );
                    this.m_hotelBasic[count].getData( result.getInt( "hotel_id" ) );

                    count++;
                }
            }
            else
            {
                this.hotelCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrivilegeHotel] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        if ( order < 0 || prefId < 0 )
        {
            return(false);
        }
        else if ( order >= 0 && order <= 2 )
        {
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( member_flag = 0 OR member_flag = 1 ) ";
            }

            if ( prefId > 0 )
            {
                query = query + " AND pref_id = ?";
            }
            query = query + " AND hotel_id > 0";
            query = query + " GROUP BY hotel_id";
        }
        // ホテル名の昇順
        else if ( order == 3 )
        {
            query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor, hh_hotel_basic, hh_hotel_pv";
            query = query + " WHERE hh_master_sponsor.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_basic.id ";
            query = query + " AND hh_hotel_pv.id = hh_hotel_basic.id ";
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_pv.id ";
            query = query + " AND hh_master_sponsor.area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND hh_master_sponsor.member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( hh_master_sponsor.member_flag = 0 OR hh_master_sponsor.member_flag = 1 ) ";
            }

            if ( prefId > 0 )
            {
                query = query + " AND hh_master_sponsor.pref_id = ?";
            }
            query = query + " AND hh_master_sponsor.hotel_id > 0";
            query = query + " GROUP BY hh_master_sponsor.hotel_id";
        }
        try
        {
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.hotelAllCount = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrivilegeHotel] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( (hotelCount > 0) && (hotelAllCount > 0) )
            ret = true;
        else
            ret = false;

        return(ret);
    }

    /**
     * 特典ホテル情報取得（地方ごとに）
     * 
     * @param localId 地方ID
     * @param memberFlag 会員フラグ(0:非会員、1:正会員)
     * @param order ソートフラグ(0:disp_pos順、1:地域順、2:有効期限の昇順、3:ホテル名の昇順)
     * @param countNum 取得件数（0：全件 ※pageNum無視
     * @param pageNum ページ番号（0～)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPrivilegeHotelByLocal(int localId, int memberFlag, int order, int countNum, int pageNum)
    {
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "";
        if ( order < 0 || localId < 0 )
        {
            return(false);
        }
        else if ( order >= 0 && order <= 2 )
        {
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( member_flag = 0 OR member_flag = 1 ) ";
            }

            if ( localId > 0 )
            {
                query = query + " AND local_id = ?";
            }
            query = query + " AND hotel_id > 0";
            query = query + " GROUP BY hotel_id";
            // disp_pos順
            if ( order == 0 )
            {
                query = query + " ORDER BY start_date DESC";
            }
            // 地域順
            if ( order == 1 )
            {
                query = query + " ORDER BY pref_id, start_date DESC";
            }
            // 有効期限の終了日付の昇順
            else if ( order == 2 )
            {
                query = query + " ORDER BY end_date, pref_id, start_date DESC";
            }
        }
        // ホテル名の昇順
        else if ( order == 3 )
        {
            query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor, hh_hotel_basic, hh_hotel_pv";
            query = query + " WHERE hh_master_sponsor.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_basic.id ";
            query = query + " AND hh_hotel_pv.id = hh_hotel_basic.id ";
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_pv.id ";
            query = query + " AND hh_master_sponsor.area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND hh_master_sponsor.member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( hh_master_sponsor.member_flag = 0 OR hh_master_sponsor.member_flag = 1 ) ";
            }

            if ( localId > 0 )
            {
                query = query + " AND hh_master_sponsor.local_id = ?";
            }
            query = query + " AND hh_master_sponsor.hotel_id > 0";
            query = query + " GROUP BY hh_master_sponsor.hotel_id";
            query = query + " ORDER BY hh_hotel_basic.name_kana, hh_hotel_basic.rank DESC,";
            query = query + " hh_master_sponsor.start_date DESC";
        }

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( localId > 0 )
            {
                prestate.setInt( 1, localId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.hotelCount = result.getRow();
                    Logging.error( "[getSortOfferHotel] count=" + result.getRow() );

                }
                m_hotelBasic = new DataHotelBasic[hotelCount];
                sponsor = new DataMasterSponsor[hotelCount];
                sponsorCount = hotelCount;

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelBasic[count] = new DataHotelBasic();
                    this.sponsor[count] = new DataMasterSponsor();

                    this.sponsor[count].setData( result );
                    this.m_hotelBasic[count].getData( result.getInt( "hotel_id" ) );

                    count++;
                }
            }
            else
            {
                this.hotelCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrivilegeHotel] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        if ( order < 0 || localId < 0 )
        {
            return(false);
        }
        else if ( order >= 0 && order <= 2 )
        {
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( member_flag = 0 OR member_flag = 1 ) ";
            }

            if ( localId > 0 )
            {
                query = query + " AND local_id = ?";
            }
            query = query + " AND hotel_id > 0";
            query = query + " GROUP BY hotel_id";
        }
        // ホテル名の昇順
        else if ( order == 3 )
        {
            query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor, hh_hotel_basic, hh_hotel_pv";
            query = query + " WHERE hh_master_sponsor.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_basic.id ";
            query = query + " AND hh_hotel_pv.id = hh_hotel_basic.id ";
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_pv.id ";
            query = query + " AND hh_master_sponsor.area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND hh_master_sponsor.member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( hh_master_sponsor.member_flag = 0 OR hh_master_sponsor.member_flag = 1 ) ";
            }

            if ( localId > 0 )
            {
                query = query + " AND hh_master_sponsor.local_id = ?";
            }
            query = query + " AND hh_master_sponsor.hotel_id > 0";
            query = query + " GROUP BY hh_master_sponsor.hotel_id";
        }
        try
        {
            prestate = connection.prepareStatement( query );
            if ( localId > 0 )
            {
                prestate.setInt( 1, localId );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得to
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.hotelAllCount = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrivilegeHotel] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( (hotelCount > 0) && (hotelAllCount > 0) )
            ret = true;
        else
            ret = false;

        return(ret);
    }

    /**
     * 特典ホテル情報取得（最新のもの）
     * 
     * @param prefId 都道府県ID
     * @param memberFlag 会員フラグ(0:非会員、1:正会員)
     * @param order ソートフラグ(0:disp_pos順、1:地域順、2:有効期限の昇順、3:ホテル名の昇順)
     * @param countNum 取得件数（0：全件 ※pageNum無視
     * @param pageNum ページ番号（0～)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPrivilegeHotelLatest(int memberFlag, int newSpan, int countNum, int pageNum)
    {
        int count;
        int startDate;
        int endDate;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        startDate = 0;
        endDate = 0;
        query = "";
        if ( newSpan < 0 )
        {
            return(false);
        }
        else
        {
            startDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -newSpan );
            endDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE start_date >= " + startDate;
            query = query + " AND start_date <= " + endDate;
            query = query + " AND area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( member_flag = 0 OR member_flag = 1 ) ";
            }

            query = query + " AND hotel_id > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY hotel_id";
            query = query + " ORDER BY start_date DESC";
        }

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.hotelCount = result.getRow();
                    Logging.error( "[getSortOfferHotel] count=" + result.getRow() );

                }
                m_hotelBasic = new DataHotelBasic[hotelCount];
                sponsor = new DataMasterSponsor[hotelCount];
                sponsorCount = hotelCount;

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelBasic[count] = new DataHotelBasic();
                    this.sponsor[count] = new DataMasterSponsor();

                    this.sponsor[count].setData( result );
                    this.m_hotelBasic[count].getData( result.getInt( "hotel_id" ) );

                    count++;
                }
            }
            else
            {
                this.hotelCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrivilegeHotel] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        if ( newSpan < 0 )
        {
            return(false);
        }
        else
        {
            startDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -newSpan );
            endDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            query = "SELECT * FROM hh_master_sponsor";
            query = query + " WHERE start_date >= " + startDate;
            query = query + " AND start_date <= " + endDate;
            query = query + " AND area_code = 100000";
            if ( memberFlag == 0 )
            {
                query = query + " AND member_flag = 0 ";
            }
            else if ( memberFlag == 1 )
            {
                query = query + " AND( member_flag = 0 OR member_flag = 1 ) ";
            }

            query = query + " AND hotel_id > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY hotel_id";
            query = query + " ORDER BY start_date DESC";
        }
        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.hotelAllCount = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getPrivilegeHotel] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( (hotelCount > 0) && (hotelAllCount > 0) )
            ret = true;
        else
            ret = false;

        return(ret);
    }

    /**
     * ホテル求人広告取得
     * 
     * @see "全件数→getPrivilegeHotelAllCountを使用する。1ページあたりの件数は、getSponsorCountまたは、getPrivilegeHotelCountを使用する。"
     * @param prefId 都道府県ID(0:全件検索)
     * @param kind　区分(0:PC, 1:携帯)
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelJobOffer(int prefId, int kind, int countNum, int pageNum)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_master_sponsor.* from hh_master_sponsor, hh_hotel_basic";
        query = query + " WHERE hh_master_sponsor.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_sponsor.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_sponsor.sponsor_code >= 5000000";
        query = query + " AND hh_master_sponsor.sponsor_code < 6000000";
        query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_basic.id";
        if ( prefId > 0 )
        {
            query = query + " AND hh_master_sponsor.pref_id = ? ";
        }
        query = query + " ORDER BY hh_master_sponsor.start_date DESC, hh_master_sponsor.append_date DESC, hh_master_sponsor.append_time DESC, hh_hotel_basic.name_kana";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            ret = getSponsorDataSub( prestate );
            if ( ret != false )
            {
                // ホテル件数をセットする
                this.hotelCount = this.sponsorCount;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[HotelJobOffer.getHotelJobOffer1] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            result = null;
            prestate = null;
        }

        try
        {
            query = "SELECT hh_master_sponsor.* from hh_master_sponsor, hh_hotel_basic";
            query = query + " WHERE hh_master_sponsor.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND hh_master_sponsor.sponsor_code >= 5000000";
            query = query + " AND hh_master_sponsor.sponsor_code < 6000000";
            query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_basic.id";
            if ( prefId > 0 )
            {
                query = query + " AND hh_master_sponsor.pref_id = ? ";
            }

            prestate = connection.prepareStatement( query );
            if ( prefId > 0 )
            {
                prestate.setInt( 1, prefId );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.hotelAllCount = result.getRow();
                }
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[HotelJobOffer.getHotelJobOffer2] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * ホテル求人広告取得
     * 
     * @see　"件数取得→getPrivilegeHotelAllCount、getSponsorCountまたは、getPrivilegeHotelCountを使用する。"
     * @param hotelIdList ホテルIDリスト(0:全件検索)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelJobOfferLatest(int hotelId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( hotelId <= 0 )
        {
            return(false);
        }
        // クエリーを登録
        query = "SELECT hh_master_sponsor.* from hh_master_sponsor, hh_hotel_basic";
        query = query + " WHERE hh_master_sponsor.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_sponsor.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_sponsor.sponsor_code >= 5000000";
        query = query + " AND hh_master_sponsor.sponsor_code < 6000000";
        query = query + " AND hh_master_sponsor.hotel_id = hh_hotel_basic.id";
        if ( hotelId > 0 )
        {
            query = query + " AND hh_master_sponsor.hotel_id = ? ";
        }
        query = query + " ORDER BY hh_master_sponsor.start_date DESC, hh_master_sponsor.append_date DESC," +
                " hh_master_sponsor.append_time DESC, hh_hotel_basic.name_kana";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( hotelId > 0 )
            {
                prestate.setInt( 1, hotelId );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.hotelAllCount = result.getRow();
                }
                result.beforeFirst();
                if ( result.next() != false )
                {
                    // 配列の初期化
                    this.sponsor = new DataMasterSponsor[1];
                    // 要素の初期化
                    this.sponsor[0] = new DataMasterSponsor();
                    // 要素にデータをセット
                    this.sponsor[0].setData( result );
                    this.hotelCount = this.hotelAllCount;
                    this.sponsorCount = this.hotelAllCount;
                    Logging.info( "[HotelJobOffer.getHotelJobOffer] test" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[HotelJobOffer.getHotelJobOfferLatest2] Exception=" + e.toString() );
            this.hotelCount = 0;
            this.sponsorCount = 0;
            this.hotelAllCount = 0;
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        Logging.info( "[HotelJobOffer.getHotelJobOfferLatest2] hotelAllCount=" + hotelAllCount );

        if ( hotelAllCount > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ローテーションバナー用データ作成処理
     * 
     * @param prefId 都道府県ID
     * @param dispCount 取得件数
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean insertRandomSponsor() throws Exception
    {
        boolean ret;
        int nextDate;
        int i;
        int count;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        DataSponsorData dsd;

        ret = false;
        count = 0;
        i = 0;

        nextDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), 1 );
        // 表示対象のもの
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_pref"
                + " WHERE hh_master_sponsor.sponsor_code <> 0"
                + " AND hh_master_sponsor.area_code <> 0"
                + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code"
                + " AND ( hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ? )"
                + " AND hh_master_sponsor.random_disp_flag = 2";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, nextDate );
            prestate.setInt( 2, nextDate );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[SponsorData_M2.insertRandomSponsor( )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        try
        {
            if ( ret != false )
            {
                // 取得したデータをインサートする
                if ( sponsorCount > 0 )
                {
                    dsd = new DataSponsorData();
                    // 取得したデータ数だけ繰り返す
                    for( i = 0 ; i < sponsorCount ; i++ )
                    {
                        Logging.error( "[SponsorData_M2.insertRandomSponsor( )] sponsor=" + sponsor[i].getSponsorCode() );
                        dsd.setSponsorCode( sponsor[i].getSponsorCode() );
                        dsd.setAddupDate( nextDate );
                        ret = dsd.insertData();
                        if ( ret == false )
                        {
                            count++;
                        }
                    }
                    // 1個でもfalseがあったらエラーとして返す
                    if ( count == 0 )
                    {
                        ret = true;
                    }
                    else
                    {
                        ret = false;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[SponsorData_M2.insertRandomSponsor( )] Exception=" + e.toString() );
            throw e;
        }

        return(ret);
    }

    /**
     * スーパーバナー情報取得（ランダム）
     * 
     * @param userAgentType ユーザーエージェントタイプ
     * @param kind 区分(0:TOPページ、1:それ以外)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSuperBanner(int userAgentType, int kind) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i;
        int count;

        // 表示しているキャリアに該当するurlが登録されているレコードの中からランダムで取得する。
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT sponsor_code FROM hh_master_sponsor";
        query = query + " Where area_code = 0";
        query = query + " AND random_disp_flag = 1";

        if ( kind == 1 )
        {
            query = query + " AND sponsor_code > 7000000";
            query = query + " AND sponsor_code < 8000000";
            query = query + " AND disp_pos = '7'";
        }
        else
        {
            query = query + " AND sponsor_code > 6000000";
            query = query + " AND sponsor_code < 7000000";
            query = query + " AND disp_pos = '6'";

        }
        query = query + " AND start_date <= ?";
        query = query + " AND end_date >= ?";

        if ( userAgentType == USERAGENT_DOCOMO )
        {
            query = query + " AND url_docomo != ''";
        }
        else if ( userAgentType == USERAGENT_AU )
        {
            query = query + " AND url_au != ''";
        }
        else if ( userAgentType == USERAGENT_SOFTBANK )
        {
            query = query + " AND url_softbank != ''";
        }
        else
        {
            query = query + " AND url != '' ";
        }
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";
        query = query + " LIMIT 0,1";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    sponsorCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.sponsor = new DataMasterSponsor[sponsorCount];
                for( i = 0 ; i < sponsorCount ; i++ )
                {
                    sponsor[i] = new DataMasterSponsor();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // スポンサー情報の取得
                    this.sponsor[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            sponsorCount = 0;
            Logging.error( "[SponsorData_M2.get] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( sponsorCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * スマートフォン向けインプレッションカウント
     * 
     * @param sponsorCode スポンサーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setImpressionCountForSmart(int sponsorCode)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // 対象広告のデータを取得する
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            dsd.setImpressionSmart( dsd.getImpressionSmart() + 1 );

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // 失敗したらログに残す
            if ( ret == false )
            {
                Logging.error( "[SponsorData_M2.setImpressionCountForSmart updateData] Exception sponsorCode:" + sponsorCode
                        + ", Impression:" + Integer.toString( dsd.getImpression() + 1 ) );
            }
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setImpressionSmart( 1 );

            ret = dsd.insertData();
            // 失敗したらログに残す
            if ( ret == false )
            {
                Logging.error( "[SponsorData_M2.setImpressionCountForSmart insertData] Exception sponsorCode:" + sponsorCode );

            }
        }

        return(ret);
    }

    /**
     * スマートフォン向けクリックカウント
     * 
     * @param sponsorCode スポンサーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setClickCountForSmart(int sponsorCode)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // 対象広告のデータを取得する
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            dsd.setClickCountSmart( dsd.getClickCountSmart() + 1 );

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setClickCountSmart( 1 );

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * スマートフォン向け拡張クリックカウント
     * 
     * @param sponsorCode スポンサーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setExClickCountForSmart(int sponsorCode)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // 対象広告のデータを取得する
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            dsd.setExClickCountSmart( dsd.getExClickCountSmart() + 1 );

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setExClickCount( 1 );

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * スマートフォン向け拡張クリックカウント２
     * 
     * @param sponsorCode スポンサーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setExClickCount2ForSmart(int sponsorCode)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // 対象広告のデータを取得する
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            dsd.setExClickCountSmart2( dsd.getExClickCountSmart2() + 1 );

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setSponsorCode( sponsorCode );
            dsd.setExClickCountSmart2( 1 );

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * スマートフォン向け拡張インプレッションカウント
     * 
     * @param sponsorCode スポンサーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setExImpressionCountForSmart(int sponsorCode)
    {
        boolean ret;
        DataSponsorData dsd;

        ret = false;

        // 対象広告のデータを取得する
        dsd = new DataSponsorData();
        ret = dsd.getData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        if ( ret != false )
        {
            dsd.setExImpressionSmart( dsd.getExImpressionSmart() + 1 );

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
            dsd.setSponsorCode( sponsorCode );
            dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dsd.setExImpressionSmart( 1 );

            ret = dsd.insertData();
        }

        return(ret);
    }

    /**
     * スポンサー広告取得
     * 
     * @param prefId 都道府県コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getAdData(int prefId, int dispCount, int dispFlag) throws Exception
    {
        final int DISP_SMART_PHONE = 2;
        boolean ret;
        ret = false;

        if ( prefId == 0 )
        {
            try
            {
                ret = this.getBannerRandom( 1, dispCount );
            }
            catch ( Exception e )
            {
                Logging.error( "[SponsorData_M2.getAdData( int prefId = " + prefId + " )] Exception=" + e.toString() );
                throw e;
            }
        }
        else
        {
            try
            {
                ret = this.getRandomAdByPref( prefId, dispCount, DISP_SMART_PHONE );
            }
            catch ( Exception e )
            {
                Logging.error( "[SponsorData_M2.getAdData( int prefId = " + prefId + " )] Exception=" + e.toString() );
                throw e;
            }
        }

        return(ret);
    }

    /**
     * スポンサー広告取得
     * 
     * @param prefId 都道府県コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getAdRandomData(int prefId, int dispCount, int dispFlag) throws Exception
    {
        ArrayList<Integer> spCodeList;
        boolean ret;
        ret = false;

        spCodeList = new ArrayList<Integer>();
        if ( prefId == 0 )
        {
            try
            {
                ret = this.getBannerRandom( dispFlag, dispCount );
            }
            catch ( Exception e )
            {
                Logging.error( "[SponsorData_M2.getAdData( int prefId = " + prefId + " )] Exception=" + e.toString() );
                throw e;
            }
        }
        else
        {
            try
            {
                // 普通のスポンサーコードを取得
                ret = this.getRandomAdByPref( prefId, dispCount, dispFlag );
                if ( ret != false )
                {
                    for( int i = 0 ; i < this.sponsorCount ; i++ )
                    {
                        spCodeList.add( sponsor[i].getSponsorCode() );
                    }
                }
                // ローテーションのスポンサーコードを取得
                ret = this.getRandomSponsorByPref( prefId, 1, dispFlag );
                if ( ret != false )
                {
                    for( int i = 0 ; i < this.sponsorCount ; i++ )
                    {
                        spCodeList.add( sponsor[i].getSponsorCode() );
                    }
                }

                if ( spCodeList.size() > 0 )
                {
                    ret = true;
                    this.getAdData( spCodeList );
                }

            }
            catch ( Exception e )
            {
                Logging.error( "[SponsorData_M2.getAdData( int prefId = " + prefId + " )] Exception=" + e.toString() );
                throw e;
            }
        }

        return(ret);
    }

    /**
     * スポンサー広告取得
     * 
     * @param prefId 都道府県コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getAdData(ArrayList<Integer> spCodeList) throws Exception
    {
        boolean ret;
        String query = "";
        String subQuery = "";
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( spCodeList == null )
        {
            return false;
        }
        else
        {
            for( int i = 0 ; i < spCodeList.size() ; i++ )
            {
                if ( spCodeList.get( i ) >= 0 )
                {
                    if ( i > 0 )
                    {
                        subQuery += ",";
                    }
                    subQuery += spCodeList.get( i );
                }
            }
        }

        // 表示対象のもの
        query = "SELECT hhms.* FROM hh_master_sponsor AS hhms";
        query = query + " INNER JOIN (";
        query = query + " SELECT sponsor_code FROM hh_master_sponsor";
        query = query + " WHERE sponsor_code <> 0";
        query = query + " AND sponsor_code IN ( " + subQuery + " )";
        query = query + " ORDER BY RAND()";
        query = query + " ) AS random";
        query = query + " ON hhms.sponsor_code = random.sponsor_code";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.get=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return true;

    }

    /**
     * エリア広告用スポンサー情報取得（都道府県ID）
     * 
     * @param prefId 都道府県ID
     * @param dispCount 取得件数
     * @param dispFlag 携帯フラグ(0:PC,1:携帯,2：スマートフォン)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getRandomAdByPref(int prefId, int dispCount, int dispFlag) throws Exception
    {
        boolean ret;
        int[] sponsorCode = null;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;
        sponsorCode = this.getAreaAdByPref( prefId );
        if ( sponsorCode != null )
        {
            insertSponsorData( sponsorCode );
        }

        // 表示対象のもの
        query = "SELECT sponsor.* FROM hh_master_sponsor sponsor"
                + " INNER JOIN hh_master_pref pref ON pref.sponsor_area_code = sponsor.area_code"
                + " AND pref.pref_id = ?"
                + " WHERE sponsor.sponsor_code <> 0"
                + " AND sponsor.area_code <> 0"
                + " AND sponsor.start_date <= ? AND sponsor.end_date >= ?"
                + " AND sponsor.random_disp_flag = 0"
                + " AND sponsor.hotel_id != 99999999"
                + " ORDER BY RAND() LIMIT 0,?";
        /*
         * query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_pref, hh_sponsor_data"
         * + " WHERE hh_master_sponsor.sponsor_code <> 0"
         * + " AND hh_master_sponsor.area_code <> 0"
         * + " AND hh_master_sponsor.sponsor_code = hh_sponsor_data.sponsor_code"
         * + " AND hh_master_pref.pref_id = ?"
         * + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code"
         * + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)"
         * + " AND hh_sponsor_data.addup_date = ?"
         * + " AND hh_master_sponsor.random_disp_flag = 0"
         * + " AND hh_master_sponsor.hotel_id != 99999999";
         * // 表示区分により
         * if ( dispFlag == 0 )
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression_mobile LIMIT 0,?";
         * }
         * else if ( dispFlag == 1 )
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression LIMIT 0,?";
         * }
         * else if ( dispFlag == 2 )
         * {
         * query = query + " ORDER BY hh_sponsor_data.impression_smart LIMIT 0,?";
         * }
         */
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, dispCount );

            ret = getSponsorDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.getRandomAdByPref( int prefId = " + prefId + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * エリア広告用スポンサー情報取得（都道府県ID）
     * 
     * @param prefId 都道府県ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public int[] getAreaAdByPref(int prefId) throws Exception
    {
        boolean ret;
        int dataCount = 0;
        int count = 0;
        int[] sponsorCode = null;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        ret = false;
        query = "SELECT sponsor_code FROM hh_master_sponsor"
                + " WHERE sponsor_code <> 0"
                + " AND area_code = ?"
                + " AND (start_date <= ? AND end_date >= ?)"
                + " AND random_disp_flag = 0";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, prefId );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        dataCount = result.getRow();
                        sponsorCode = new int[dataCount];
                    }

                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        sponsorCode[count] = result.getInt( "sponsor_code" );
                        count++;
                    }
                }
            }
        }
        catch ( Exception e )
        {
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(sponsorCode);
    }

    /**
     * エリア広告用スポンサー情報挿入処理
     * 
     * @param prefId 都道府県ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public void insertSponsorData(int[] sponsorCode) throws Exception
    {
        boolean ret;
        DataSponsorData dsd;

        dsd = new DataSponsorData();
        try
        {
            for( int i = 0 ; i < sponsorCode.length ; i++ )
            {
                ret = dsd.getData( sponsorCode[i], Integer.parseInt( DateEdit.getDate( 2 ) ) );
                if ( ret == false )
                {
                    dsd.setSponsorCode( sponsorCode[i] );
                    dsd.setAddupDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dsd.insertData();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SponsorData_M2.insertSponsorData()] Exception=" + e.toString() );
        }
    }
}
