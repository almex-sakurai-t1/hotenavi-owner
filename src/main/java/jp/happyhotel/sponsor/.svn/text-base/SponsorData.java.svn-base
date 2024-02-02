/*
 * @(#)SponsorData.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 スポンサーデータ取得クラス
 */
package jp.happyhotel.sponsor;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterSponsor;
import jp.happyhotel.data.DataSponsorData;

/**
 * スポンサーデータ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.00 2007/12/03
 */
public class SponsorData implements Serializable
{
    /**
     *
     */
    private static final long   serialVersionUID   = 49553862735802305L;

    private int                 sponsorCount;
    private DataMasterSponsor[] sponsor;

    /** 端末種別：DoCoMo **/
    public static final int     USERAGENT_DOCOMO   = 1;
    /** 端末種別：au **/
    public static final int     USERAGENT_AU       = 2;
    /** 端末種別：J-PHONE,Vodafone **/
    public static final int     USERAGENT_JPHONE   = 3;
    /** 端末種別：J-PHONE,Vodafone **/
    public static final int     USERAGENT_VODAFONE = 3;
    /** 端末種別：J-PHONE,Vodafone,SoftBank **/
    public static final int     USERAGENT_SOFTBANK = 3;
    /** 端末種別：pc **/
    public static final int     USERAGENT_PC       = 4;

    public SponsorData()
    {
        sponsorCount = 0;
    }

    public DataMasterSponsor[] getSponsor()
    {
        return sponsor;
    }

    public int getSponsorCount()
    {
        return sponsorCount;
    }

    /**
     * スポンサー情報取得（都道府県ID）
     * 
     * @param prefId 都道府県ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSponsorByPref(int prefId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_pref";
        query = query + " WHERE hh_master_sponsor.sponsor_code <> 0";
        query = query + " AND hh_master_sponsor.area_code <> 0";
        query = query + " AND hh_master_pref.pref_id = ?";
        query = query + " AND hh_master_pref.sponsor_area_code = hh_master_sponsor.area_code";
        query = query + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)";
        query = query + " ORDER BY hh_master_sponsor.disp_pos";

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
            Logging.error( "[SponsorData.getSponsorByPref] Exception=" + e.toString() );
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
     */
    public boolean getSponsorByCity(int jisCode)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_city";
        query = query + " WHERE hh_master_sponsor.sponsor_code <> 0";
        query = query + " AND hh_master_sponsor.area_code <> 0";
        query = query + " AND hh_master_city.jis_code = ?";
        query = query + " AND hh_master_city.sponsor_area_code = hh_master_sponsor.area_code";
        query = query + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)";
        query = query + " ORDER BY hh_master_sponsor.disp_pos";

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
            Logging.error( "[SponsorData.getSponsorByCity] Exception=" + e.toString() );
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
     */
    public boolean getSponsorByArea(int areaId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor,hh_master_area";
        query = query + " WHERE hh_master_sponsor.sponsor_code <> 0";
        query = query + " AND hh_master_sponsor.area_code <> 0";
        query = query + " AND hh_master_area.area_id = ?";
        query = query + " AND hh_master_area.sponsor_area_code = hh_master_sponsor.area_code";
        query = query + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)";
        query = query + " ORDER BY hh_master_sponsor.disp_pos";

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
    public boolean getSponsorByDispPos(String dispPos)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        // 表示対象のもの
        query = "SELECT hh_master_sponsor.* FROM hh_master_sponsor";
        query = query + " WHERE hh_master_sponsor.sponsor_code <> 0";
        query = query + " AND hh_master_sponsor.area_code <> 0";
        query = query + " AND hh_master_sponsor.disp_pos = ?";
        query = query + " AND (hh_master_sponsor.start_date <= ? AND hh_master_sponsor.end_date >= ?)";
        query = query + " ORDER BY hh_master_sponsor.disp_pos";

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
     */
    private boolean getSponsorDataSub(PreparedStatement prestate)
    {
        int i;
        int count;
        boolean ret;
        ResultSet result = null;

        count = 0;
        ret = false;

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
            Logging.info( "[SponsorData.getSponsorDataSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

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
                dsd.setExClickCountMobile2( dsd.getExClickCountMobile2() + 1 );
            }
            else
            {
                dsd.setExClickCount2( dsd.getExClickCount2() + 1 );
            }

            ret = dsd.updateData( sponsorCode, Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }
        else
        {
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
     * アフィリエイト情報取得（ランダムで１件）
     * 
     * @param userAgentType ユーザーエージェントタイプ
     * @param dispCount 表示数
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getAffiliateRandom(int userAgentType, int dispCount)
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
            Logging.error( "[SponsorData.getAffiliaterandom] Exception=" + e.toString() );
            return(false);
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
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getAdHotelList(int areaCode)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i;
        int count;

        //
        query = "SELECT * FROM hh_master_sponsor";
        query = query + " WHERE area_code = ?";
        query = query + " AND start_date <= ?";
        query = query + " AND end_date >= ?";
        query = query + " ORDER BY disp_pos";

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
            Logging.error( "[SponsorData.getAdHotelList] Exception=" + e.toString() );
            return(false);
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
     * TOPバナー情報取得（ランダムで１件）
     * 
     * @param userAgentType ユーザーエージェントタイプ
     * @param dispCount 表示数
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getBannerRandom(int userAgentType, int dispCount)
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
            Logging.error( "[SponsorData.getAffiliaterandom] Exception=" + e.toString() );
            return(false);
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
     * スポンサー情報ランダム取得（都道府県ID）
     * 
     * @param prefId 都道府県ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSponsorRandomByPref(int prefId)
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
            Logging.error( "[SponsorData.getSponsorByPref] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * スポンサー情報ランダム取得（市区町村コード）
     * 
     * @param jisCode 市区町村コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSponsorRandomByCity(int jisCode)
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
            Logging.error( "[SponsorData.getSponsorByCity] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * スポンサー情報ランダム取得（ホテル街コード）
     * 
     * @param areaId エリアID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSponsorRandomByArea(int areaId)
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
            Logging.error( "[SponsorData.getSponsorByArea] Exception=" + e.toString() );
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
    public boolean getSponsorRandomByDispPos(String dispPos)
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
            Logging.error( "[SponsorData.getSponsorByDispPos] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

}
