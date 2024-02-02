/*
 * @(#)MasterPoint.java 1.00 2008/04/22 Copyright (C) ALMEX Inc. 2007 ユーザ基本情報取得クラス
 */

package jp.happyhotel.others;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataMasterChain;
import jp.happyhotel.data.DataMasterName;
import jp.happyhotel.data.DataMasterPresent;
import jp.happyhotel.search.SearchEngineBasic;

/**
 * 賞品マスタ取得クラス。 賞品マスタの情報を取得する機能を提供する
 * 
 * @author S.Tashiro
 * @version 1.00 2008/04/22
 */
public class MasterPresent implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID = -659576481267252802L;
    private int                 masterCount;
    private int                 hotelCount;
    private int                 hotelAllCount;
    private DataMasterPresent[] masterPresent;
    private DataMasterName[]    masterName;
    private DataHotelBasic[]    m_hotelBasic;
    private DataMasterChain[]   masterChain;

    /**
     * データを初期化します。
     */
    public MasterPresent()
    {
        masterCount = 0;
        hotelCount = 0;
        hotelAllCount = 0;
    }

    /** 賞品マスタ情報件数取得 **/
    public int getCount()
    {
        return(masterCount);
    }

    /** 賞品マスタ情報取得 **/
    public DataMasterPresent[] getMasterPresentInfo()
    {
        return(masterPresent);
    }

    /** マスターネーム取得 **/
    public DataMasterName[] getMasterNameInfo()
    {
        return(masterName);
    }

    /** ホテル件数取得(1ページの件数) **/
    public int getOfferHotelCount()
    {
        return(hotelCount);
    }

    /** ホテル件数取得(全件) **/
    public int getOfferHotelAllCount()
    {
        return(hotelAllCount);
    }

    /** ホテル情報取得 **/
    public DataHotelBasic[] getOfferHotel()
    {
        return(m_hotelBasic);
    }

    /** ホテルグループ情報取得 **/
    public DataMasterChain[] getMasterChain()
    {
        return(masterChain);
    }

    /**
     * 賞品マスタを取得する（有効期限内から）
     * 
     * @param today 今日の日付
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterPresent(int today)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( today < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_present";
        query = query + " WHERE limit_from <= ?";
        query = query + " AND limit_to >= ?";
        query = query + " AND kind_flag = 0";
        query = query + " ORDER BY seq DESC, limit_to DESC, limit_from DESC ";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, today );
            prestate.setInt( 2, today );
            ret = getPresentSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterPresent] Exception=" + e.toString() );
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
    private boolean getPresentSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;
        int i;

        i = 0;
        count = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    masterCount = result.getRow();
                }
                this.masterPresent = new DataMasterPresent[this.masterCount];
                this.masterName = new DataMasterName[this.masterCount];

                for( i = 0 ; i < masterCount ; i++ )
                {
                    masterPresent[i] = new DataMasterPresent();
                    masterName[i] = new DataMasterName();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // プレゼントデータ情報の設定
                    this.masterPresent[count].setData( result );
                    // 名前情報の設定
                    this.masterName[count].getData( 3, result.getInt( "category" ) );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getPresentSub] Exception=" + e.toString() );
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
     * ネームマスタを取得する（クラスから）
     * 
     * @param class クラスID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterName(int classCode)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( classCode < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_name";
        query = query + " WHERE class = ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, classCode );

            ret = getMasterNameSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserDecome_getMasterName] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ネームマスタ情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMasterNameSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;
        int i;

        i = 0;
        count = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    masterCount = result.getRow();
                }
                this.masterName = new DataMasterName[this.masterCount];
                for( i = 0 ; i < masterCount ; i++ )
                {
                    masterName[i] = new DataMasterName();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ネームマスタ情報の設定
                    this.masterName[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterNameSub] Exception=" + e.toString() );
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
     * 賞品マスタを取得する（当選発表期間内から）
     * 
     * @param today 今日の日付
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterPresentByDisp(int today)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( today < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_present";
        query = query + " WHERE disp_from <= ?";
        query = query + " AND disp_to >= ?";
        query = query + " AND kind_flag = 0";
        query = query + " ORDER BY seq DESC, limit_to DESC, limit_from DESC ";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, today );
            prestate.setInt( 2, today );
            ret = getPresentSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterPresent] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * プレゼント提供ホテル数取得（都道府県IDから）
     * 
     * @param prefId 地方ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public int getPresentCountByPref(int prefId)
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
        query = "SELECT COUNT( * ) FROM hh_master_present";
        query = query + " WHERE hh_master_present.limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_present.limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_present.pref_id = ?";
        query = query + " AND hh_master_present.kind_flag = 1";
        query = query + " AND hh_master_present.elect_number > 0";
        query = query + " GROUP BY hh_master_present.offer_hotel";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, prefId );
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
            Logging.info( "[getPresentCountByPref] Exception=" + e.toString() );
            return(0);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    /**
     * プレゼント提供ホテルソート
     * 
     * @param prefId 都道府県ID
     * @param orderFlag ソートフラグ(0:地域順、1:提供数の降順、2:残数の降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSortOfferHotel(int prefId, int order, int countNum, int pageNum)
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
        else if ( order == 0 )
        {
            query = "SELECT * FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            if ( prefId > 0 )
            {
                query = query + " AND pref_id = ?";
            }
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND offer_hotel > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY offer_hotel";
            query = query + " ORDER BY local_id, pref_id, disp_pos";
        }
        else if ( order == 1 )
        {
            query = "SELECT SUM( elect_number ) as sort, hh_master_present.*";
            query = query + " FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            if ( prefId > 0 )
            {
                query = query + " AND pref_id = ?";
            }
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND offer_hotel > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY offer_hotel";
            query = query + " ORDER BY sort DESC, disp_pos";
        }
        else if ( order == 2 )
        {
            query = "SELECT SUM( remains_number ) as sort, hh_master_present.*";
            query = query + " FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            if ( prefId > 0 )
            {
                query = query + " AND pref_id = ?";
            }
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND offer_hotel > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY offer_hotel";
            query = query + " ORDER BY sort DESC, disp_pos";
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
                masterPresent = new DataMasterPresent[hotelCount];
                masterName = new DataMasterName[hotelCount];
                masterChain = new DataMasterChain[hotelCount];

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelBasic[count] = new DataHotelBasic();
                    this.masterPresent[count] = new DataMasterPresent();
                    this.masterName[count] = new DataMasterName();
                    this.masterChain[count] = new DataMasterChain();

                    this.masterPresent[count].setData( result );
                    this.masterName[count].getData( 3, result.getInt( "category" ) );
                    if ( result.getInt( "offer_hotel" ) < 100000 )
                    {
                        this.m_hotelBasic[count].setId( result.getInt( "offer_hotel" ) );
                        this.m_hotelBasic[count].setPrefId( result.getInt( "pref_id" ) );
                        this.masterChain[count].getData( result.getInt( "offer_hotel" ) );
                    }
                    else
                    {
                        this.m_hotelBasic[count].getData( result.getInt( "offer_hotel" ) );
                        if ( m_hotelBasic[count].getPrefId() != result.getInt( "pref_id" ) )
                        {
                            m_hotelBasic[count].setPrefId( result.getInt( "pref_id" ) );
                        }
                        this.masterChain[count].setGroupId( result.getInt( "offer_hotel" ) );
                    }

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
            Logging.info( "[getSortOfferHotel] Exception=" + e.toString() );
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
        else if ( order == 0 )
        {
            query = "SELECT * FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            if ( prefId > 0 )
            {
                query = query + " AND pref_id = ?";
            }
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND offer_hotel > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY offer_hotel";
            query = query + " ORDER BY local_id, pref_id, disp_pos";
        }
        else if ( order == 1 )
        {
            query = "SELECT SUM( elect_number ) as sort, hh_master_present.*";
            query = query + " FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            if ( prefId > 0 )
            {
                query = query + " AND pref_id = ?";
            }
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND offer_hotel > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY offer_hotel";
            query = query + " ORDER BY sort DESC, disp_pos";
        }
        else if ( order == 2 )
        {
            query = "SELECT SUM( remains_number ) as sort, hh_master_present.*";
            query = query + " FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            if ( prefId > 0 )
            {
                query = query + " AND pref_id = ?";
            }
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND offer_hotel > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY offer_hotel";
            query = query + " ORDER BY sort DESC, disp_pos";
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
     * プレゼント情報取得（ホテルIDから）
     * 
     * @param offerHotel 提供ホテルID
     * @param prefId 都道府県ID
     * @param orderFlag ソートフラグ(0:名前順、1:提供数の降順、2:残数の降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterPresentByOfferHotel(int offerHotel, int prefId, int order)
    {
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        ret = false;
        query = "";
        if ( order < 0 || prefId < 0 || offerHotel < 0 )
        {
            return(false);
        }
        else if ( order == 0 )
        {
            query = "SELECT hh_master_present.* FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND offer_hotel = ?";
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND pref_id = ?";
            query = query + " ORDER BY seq DESC";
        }
        else if ( order == 1 )
        {
            query = "SELECT * FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND offer_hotel = ?";
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND pref_id = ?";
            query = query + " ORDER BY elect_number DESC";
        }
        else if ( order == 2 )
        {
            query = "SELECT * FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND offer_hotel = ?";
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND pref_id = ?";
            query = query + " ORDER BY remains_number DESC";
        }

        ret = false;
        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, offerHotel );
            prestate.setInt( 2, prefId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.masterCount = result.getRow();
                }
                masterPresent = new DataMasterPresent[masterCount];
                masterName = new DataMasterName[masterCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.masterPresent[count] = new DataMasterPresent();
                    this.masterName[count] = new DataMasterName();

                    this.masterPresent[count].setData( result );
                    this.masterName[count].getData( 3, result.getInt( "category" ) );
                    count++;
                }
            }
            else
            {
                this.masterCount = 0;
            }
        }
        catch ( Exception e )
        {
            this.masterCount = 0;
            Logging.info( "[getMasterPresentByOfferHotel] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( masterCount > 0 )
            ret = true;
        else
            ret = false;

        return(ret);
    }

    /**
     * プレゼント提供ホテルソート
     * 
     * @param prefId 都道府県ID
     * @param orderFlag ソートフラグ(0:名前順、1:提供数の降順、2:残数の降順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSortOfferHotelByLocal(int localId, int order, int countNum, int pageNum)
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
        else if ( order == 0 )
        {
            // hh_master_presentと、hh_master_chainのデータを取得する
            query = "SELECT hh_master_present.* FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            if ( localId > 0 )
            {
                query = query + " AND local_id = ?";
            }
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND offer_hotel > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY offer_hotel, pref_id";
            query = query + " ORDER BY local_id, pref_id";

            query = query + " UNION ";
            // hh_master_presentと、hh_hotel_basicのデータを取得する
            query = "SELECT hh_master_present.* FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            if ( localId > 0 )
            {
                query = query + " AND local_id = ?";
            }
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND offer_hotel > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY offer_hotel, pref_id";
            query = query + " ORDER BY local_id, pref_id";

        }
        else if ( order == 1 )
        {
            query = "SELECT SUM( elect_number ) as sort, hh_master_present.*";
            query = query + " FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            if ( localId > 0 )
            {
                query = query + " AND local_id = ?";
            }
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND offer_hotel > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY offer_hotel, pref_id";
            query = query + " ORDER BY sort DESC";
        }
        else if ( order == 2 )
        {
            query = "SELECT SUM( remains_number ) as sort, hh_master_present.*";
            query = query + " FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            if ( localId > 0 )
            {
                query = query + " AND local_id = ?";
            }
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND offer_hotel > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY offer_hotel, pref_id";
            query = query + " ORDER BY sort DESC";
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
                masterPresent = new DataMasterPresent[hotelCount];
                masterName = new DataMasterName[hotelCount];
                masterChain = new DataMasterChain[hotelCount];

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelBasic[count] = new DataHotelBasic();
                    this.masterPresent[count] = new DataMasterPresent();
                    this.masterName[count] = new DataMasterName();
                    this.masterChain[count] = new DataMasterChain();

                    this.masterPresent[count].setData( result );
                    this.masterName[count].getData( 3, result.getInt( "category" ) );
                    if ( result.getInt( "offer_hotel" ) < 100000 )
                    {
                        this.m_hotelBasic[count].setId( result.getInt( "offer_hotel" ) );
                        this.m_hotelBasic[count].setPrefId( result.getInt( "pref_id" ) );
                        this.masterChain[count].getData( result.getInt( "offer_hotel" ) );
                    }
                    else
                    {
                        this.m_hotelBasic[count].getData( result.getInt( "offer_hotel" ) );
                        if ( m_hotelBasic[count].getPrefId() != result.getInt( "pref_id" ) )
                        {
                            m_hotelBasic[count].setPrefId( result.getInt( "pref_id" ) );
                        }
                        this.masterChain[count].setGroupId( result.getInt( "offer_hotel" ) );
                    }

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
            Logging.info( "[getSortOfferHotelByLocal] Exception=" + e.toString() );
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
        else if ( order == 0 )
        {
            query = "SELECT * FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            if ( localId > 0 )
            {
                query = query + " AND local_id = ?";
            }
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND offer_hotel > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY offer_hotel, pref_id";
            query = query + " ORDER BY local_id, pref_id, seq";
        }
        else if ( order == 1 )
        {
            query = "SELECT SUM( elect_number ) as sort, hh_master_present.*";
            query = query + " FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            if ( localId > 0 )
            {
                query = query + " AND local_id = ?";
            }
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND offer_hotel > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY offer_hotel, pref_id";
            query = query + " ORDER BY sort DESC, seq";
        }
        else if ( order == 2 )
        {
            query = "SELECT SUM( remains_number ) as sort, hh_master_present.*";
            query = query + " FROM hh_master_present";
            query = query + " WHERE limit_from <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            query = query + " AND limit_to >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
            if ( localId > 0 )
            {
                query = query + " AND local_id = ?";
            }
            query = query + " AND kind_flag = 1";
            query = query + " AND elect_number > 0";
            query = query + " AND offer_hotel > 0";
            query = query + " AND pref_id > 0";
            query = query + " GROUP BY offer_hotel, pref_id";
            query = query + " ORDER BY sort DESC";
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
            Logging.info( "[getSortOfferHotelByLocal] Exception=" + e.toString() );
            return(false);
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
     * プレゼント提供ホテル数取得（地方IDから）
     * 
     * @param localId 地方ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public int getPresentCountByLocal(int localId)
    {
        int i;
        int j;
        int localCount;
        SearchEngineBasic seb;
        seb = new SearchEngineBasic();

        if ( localId < 0 )
            return(-1);

        localCount = 0;
        seb.getLocalList( localId, 0 );
        try
        {
            if ( seb.getMasterLocalCount() > 0 )
            {
                for( i = 0 ; i < seb.getMasterLocalCount() ; i++ )
                {
                    seb.getPrefListByLocal( seb.getMasterLocal()[i].getLocalId(), 0 );
                    if ( seb.getMasterPrefCount() > 0 )
                    {
                        for( j = 0 ; j < seb.getMasterPrefCount() ; j++ )
                        {
                            localCount = localCount + getPresentCountByPref( seb.getMasterPref()[j].getPrefId() );
                        }
                    }
                    else
                    {
                        localCount = 0;
                        Logging.error( "[getPresentCountByLocal] pref=0" );
                    }
                }
            }
            else
            {
                localCount = 0;
                Logging.error( "[getPresentCountByLocal] local=0" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getPresentCountByLocal] Exception=" + e.toString() );
        }
        return(localCount);
    }

    /**
     * 賞品マスタを取得する（有効期限内から）
     * 
     * @param today 今日の日付
     * @param kind 区分(0:ポイント応募、1:ポイント交換、2:ポイント試泊)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterPresent(int today, int kind)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( today < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_present";
        query = query + " WHERE limit_from <= ?";
        query = query + " AND limit_to >= ?";
        query = query + " AND kind_flag = ?";
        query = query + " ORDER BY seq DESC, limit_to DESC, limit_from DESC ";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, today );
            prestate.setInt( 2, today );
            prestate.setInt( 3, kind );
            ret = getPresentSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterPresent] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 賞品マスタを取得する（当選発表期間内から）
     * 
     * @param today 今日の日付
     * @param kind 区分(0:ポイント応募、1:ポイント交換、2:ポイント試泊)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterPresentByDisp(int today, int kind)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( today < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_present";
        query = query + " WHERE disp_from <= ?";
        query = query + " AND disp_to >= ?";
        query = query + " AND kind_flag = 0";
        query = query + " ORDER BY seq DESC, limit_to DESC, limit_from DESC ";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, today );
            prestate.setInt( 2, today );
            ret = getPresentSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterPresent] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

}
