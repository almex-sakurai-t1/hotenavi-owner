/*
 * @(#)SponsorDetail.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 スポンサー詳細データ取得クラス
 */
package jp.happyhotel.sponsor;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataMasterSponsor;
import jp.happyhotel.data.DataSponsorData;
import jp.happyhotel.data.DataMasterLocal;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataSponsorDetail;
import jp.happyhotel.data.DataSystemInfo;
import jp.happyhotel.search.SearchEngineBasic;

/**
 * スポンサーデータ取得クラス
 * 
 * @author N.Ide
 * @version 1.00 2009/03/04
 */
public class SponsorDetail implements Serializable
{

    /**
	 * 
	 */
    private static final long   serialVersionUID = 1239364866480101200L;

    private int                 sponsorCount;
    private DataSponsorDetail[] sponsor;

    /**
     * データを初期化します。
     */
    public SponsorDetail()
    {
        sponsorCount = 0;
        sponsor = new DataSponsorDetail[0];
    }

    public int getCount()
    {
        return sponsorCount;
    }

    public DataSponsorDetail[] getSponsorDetail()
    {
        return sponsor;
    }

    /**
     * スポンサー詳細データリスト取得
     * 
     * @param sponsorCode スポンサーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSponsorDetailList(int sponsorCode) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i;
        int count;

        //
        query = "SELECT * FROM hh_sponsor_detail";
        query = query + " WHERE sponsor_code = ?";
        ;

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, sponsorCode );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.sponsorCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.sponsor = new DataSponsorDetail[this.sponsorCount];
                for( i = 0 ; i < sponsorCount ; i++ )
                {
                    sponsor[i] = new DataSponsorDetail();
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
            Logging.error( "[SponsorDetail.getSponsorDetailList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
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

}
