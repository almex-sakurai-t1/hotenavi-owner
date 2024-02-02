/*
 * @(#)HotelJobOffer.java 1.00 2009/03/16 Copyright (C) ALMEX Inc. 2009 ホテル求人データ取得クラス
 */
package jp.happyhotel.hotel;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;
import jp.happyhotel.data.DataMasterSponsor;
import jp.happyhotel.data.DataHotelJobOffer;

/**
 * ホテル求人広告データ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/03/16
 */
public class HotelJobOffer implements Serializable
{
    /**
     *
     */
    private static final long   serialVersionUID   = 8748004432656209320L;

    private int                 sponsorCount;
    private DataMasterSponsor   sponsor;
    private int                 hotelJobOfferCount;
    private DataHotelJobOffer[] hotelJobOffer;

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

    public HotelJobOffer()
    {
        sponsorCount = 0;
        hotelJobOfferCount = 0;
    }

    public DataMasterSponsor getSponsor()
    {
        return sponsor;
    }

    public int getSponsorCount()
    {
        return sponsorCount;
    }

    public DataHotelJobOffer[] getHotelJobOffer()
    {
        return hotelJobOffer;
    }

    public int getHotelJobOfferCount()
    {
        return hotelJobOfferCount;
    }

    /**
     * ホテル求人情報取得
     * 
     * @param sponsorCode スポンサーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getHotelJobOffer(int sponsorCode) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        this.sponsor = new DataMasterSponsor();
        ret = this.sponsor.getData( sponsorCode );
        if ( ret != false )
        {
            this.sponsorCount = 1;
        }
        else
        {
            this.sponsorCount = 0;
        }

        // 表示対象のもの
        query = "SELECT * from hh_hotel_joboffer WHERE sponsor_code = ?"
                + " AND start_date <= ? AND end_date >= ? "
                + " AND del_flag = 0"
                + " ORDER BY seq";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, sponsorCode );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // Gets Sponsor Data
            ret = getHotekJobOfferSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelJobOffer.getHotekJobOffer( int sponsorCode = " + sponsorCode + " )] Exception=" + e.toString() );
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
     * ホテル求人情報取得
     * 
     * @param sponsorCode スポンサーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getHotelJobOffer(int sponsorCode, int date) throws Exception
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        this.sponsor = new DataMasterSponsor();
        ret = this.sponsor.getData( sponsorCode );
        if ( ret != false )
        {
            this.sponsorCount = 1;
        }
        else
        {
            this.sponsorCount = 0;
        }

        // 表示対象のもの
        query = "SELECT * from hh_hotel_joboffer WHERE sponsor_code = ?"
                + " AND start_date <= ? AND end_date >= ? "
                + " AND del_flag = 0"
                + " ORDER BY seq";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, sponsorCode );
            prestate.setInt( 2, date );
            prestate.setInt( 3, date );
            // Gets Sponsor Data
            ret = getHotekJobOfferSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelJobOffer.getHotekJobOffer( int sponsorCode = " + sponsorCode + " )] Exception=" + e.toString() );
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
    private boolean getHotekJobOfferSub(PreparedStatement prestate) throws Exception
    {
        int i;
        int count = 0;
        boolean ret = false;
        ResultSet result = null;

        this.hotelJobOfferCount = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.hotelJobOfferCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.hotelJobOffer = new DataHotelJobOffer[this.hotelJobOfferCount];
                for( i = 0 ; i < hotelJobOfferCount ; i++ )
                {
                    hotelJobOffer[i] = new DataHotelJobOffer();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // スポンサー情報の設定
                    this.hotelJobOffer[count].setData( result );

                    count++;
                }
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[HotelJobOffer.getHotelJobOfferSub(prestate)] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        if ( hotelJobOfferCount > 0 )
            ret = true;
        else
            ret = false;

        return(ret);
    }
}
