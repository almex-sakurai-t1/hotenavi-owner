/*
 * @(#)DataSponsorData.java 1.00 2007/09/01 Copyright (C) ALMEX Inc. 2007 スポンサー管理データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * スポンサー管理データ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/09/01
 * @version 1.1 2007/11/29
 */
public class DataSponsorData implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 4310050395659786881L;

    private int               sponsorCode;
    private int               addupDate;
    private int               impression;
    private int               impressionMobile;
    private int               clickCount;
    private int               clickCountMobile;
    private int               exClickCount;
    private int               exClickCountMobile;
    private int               exImpression;
    private int               exImpressionMobile;
    private int               exClickCount2;
    private int               exClickCountMobile2;
    private int               impressionSmart;
    private int               clickCountSmart;
    private int               exClickCountSmart;
    private int               exImpressionSmart;
    private int               exClickCountSmart2;

    /**
     * データを初期化します。
     */
    public DataSponsorData()
    {
        sponsorCode = 0;
        addupDate = 0;
        impression = 0;
        impressionMobile = 0;
        clickCount = 0;
        clickCountMobile = 0;
        exClickCount = 0;
        exClickCountMobile = 0;
        exImpression = 0;
        exImpressionMobile = 0;
        exClickCount2 = 0;
        exClickCountMobile2 = 0;
        impressionSmart = 0;
        clickCountSmart = 0;
        exClickCountSmart = 0;
        exImpressionSmart = 0;
        exClickCountSmart2 = 0;
    }

    public int getSponsorCode()
    {
        return sponsorCode;
    }

    public int getAddupDate()
    {
        return addupDate;
    }

    public int getImpression()
    {
        return impression;
    }

    public int getImpressionMobile()
    {
        return impressionMobile;
    }

    public int getImpressionSmart()
    {
        return impressionSmart;
    }

    public int getClickCount()
    {
        return clickCount;
    }

    public int getClickCountMobile()
    {
        return clickCountMobile;
    }

    public int getClickCountSmart()
    {
        return clickCountSmart;
    }

    public int getExClickCount()
    {
        return exClickCount;
    }

    public int getExClickCountMobile()
    {
        return exClickCountMobile;
    }

    public int getExClickCountSmart()
    {
        return exClickCountSmart;
    }

    public int getExImpression()
    {
        return exClickCountMobile;
    }

    public int getExImpressionMobile()
    {
        return exImpressionMobile;
    }

    public int getExImpressionSmart()
    {
        return exImpressionSmart;
    }

    public int getExClickCount2()
    {
        return exClickCount2;
    }

    public int getExClickCountMobile2()
    {
        return exClickCountMobile2;
    }

    public int getExClickCountSmart2()
    {
        return exClickCountSmart2;
    }

    /* setter */
    public void setSponsorCode(int sponsorCode)
    {
        this.sponsorCode = sponsorCode;
    }

    public void setAddupDate(int addupDate)
    {
        this.addupDate = addupDate;
    }

    public void setImpression(int impression)
    {
        this.impression = impression;
    }

    public void setImpressionMobile(int impressionMobile)
    {
        this.impressionMobile = impressionMobile;
    }

    public void setImpressionSmart(int impressionSmart)
    {
        this.impressionSmart = impressionSmart;
    }

    public void setClickCount(int clickCount)
    {
        this.clickCount = clickCount;
    }

    public void setClickCountMobile(int clickCountMobile)
    {
        this.clickCountMobile = clickCountMobile;
    }

    public void setClickCountSmart(int clickCountSmart)
    {
        this.clickCountSmart = clickCountSmart;
    }

    public void setExClickCount(int exClickCount)
    {
        this.exClickCount = exClickCount;
    }

    public void setExClickCountMobile(int exClickCountMobile)
    {
        this.exClickCountMobile = exClickCountMobile;
    }

    public void setExClickCountSmart(int exClickCountSmart)
    {
        this.exClickCountSmart = exClickCountSmart;
    }

    public void setExImpression(int exImpression)
    {
        this.exImpression = exImpression;
    }

    public void setExImpressionMobile(int exImpressionMobile)
    {
        this.exImpressionMobile = exImpressionMobile;
    }

    public void setExImpressionSmart(int exImpressionSmart)
    {
        this.exImpressionSmart = exImpressionSmart;
    }

    public void setExClickCount2(int exClickCount2)
    {
        this.exClickCount2 = exClickCount2;
    }

    public void setExClickCountMobile2(int exClickCountMobile2)
    {
        this.exClickCountMobile2 = exClickCountMobile2;
    }

    public void setExClickCountSmart2(int exClickCountSmart2)
    {
        this.exClickCountSmart2 = exClickCountSmart2;
    }

    /**
     * スポンサー管理データ取得
     * 
     * @param sponsorCode スポンサーコード
     * @param addupDate 対象日付
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int sponsorCode, int addupDate)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_sponsor_data WHERE sponsor_code = ? AND addup_date = ?";

        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, sponsorCode );
            prestate.setInt( 2, addupDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.sponsorCode = result.getInt( "sponsor_code" );
                    this.addupDate = result.getInt( "addup_date" );
                    this.impression = result.getInt( "impression" );
                    this.impressionMobile = result.getInt( "impression_mobile" );
                    this.clickCount = result.getInt( "click_count" );
                    this.clickCountMobile = result.getInt( "click_count_mobile" );
                    this.exClickCount = result.getInt( "ex_click_count" );
                    this.exClickCountMobile = result.getInt( "ex_click_count_mobile" );
                    this.exImpression = result.getInt( "ex_impression" );
                    this.exImpressionMobile = result.getInt( "ex_impression_mobile" );
                    this.exClickCount2 = result.getInt( "ex_click_count2" );
                    this.exClickCountMobile2 = result.getInt( "ex_click_count_mobile2" );

                    this.impressionSmart = result.getInt( "impression_smart" );
                    this.clickCountSmart = result.getInt( "click_count_smart" );
                    this.exClickCountSmart = result.getInt( "ex_click_count_smart" );
                    this.exImpressionSmart = result.getInt( "ex_impression_smart" );
                    this.exClickCountSmart2 = result.getInt( "ex_click_count_smart2" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSponsorData.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * スポンサー管理データ設定
     * 
     * @param result スポンサー管理データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.sponsorCode = result.getInt( "sponsor_code" );
                this.addupDate = result.getInt( "addup_date" );
                this.impression = result.getInt( "impression" );
                this.impressionMobile = result.getInt( "impression_mobile" );
                this.clickCount = result.getInt( "click_count" );
                this.clickCountMobile = result.getInt( "click_count_mobile" );
                this.exClickCount = result.getInt( "ex_click_count" );
                this.exClickCountMobile = result.getInt( "ex_click_count_mobile" );
                this.exImpression = result.getInt( "ex_impression" );
                this.exImpressionMobile = result.getInt( "ex_impression_mobile" );
                this.exClickCount2 = result.getInt( "ex_click_count2" );
                this.exClickCountMobile2 = result.getInt( "ex_click_count_mobile2" );
                this.impressionSmart = result.getInt( "impression_smart" );
                this.clickCountSmart = result.getInt( "click_count_smart" );
                this.exClickCountSmart = result.getInt( "ex_click_count_smart" );
                this.exImpressionSmart = result.getInt( "ex_impression_smart" );
                this.exClickCountSmart2 = result.getInt( "ex_click_count_smart2" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSponsorData.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * スポンサー管理データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_sponsor_data SET ";
        query = query + " sponsor_code = ?,";
        query = query + " addup_date = ?,";
        query = query + " impression = ?,";
        query = query + " impression_mobile = ?,";
        query = query + " click_count = ?,";
        query = query + " click_count_mobile = ?,";
        query = query + " ex_click_count = ?,";
        query = query + " ex_click_count_mobile = ?,";
        query = query + " ex_impression = ?,";
        query = query + " ex_impression_mobile = ?,";
        query = query + " ex_click_count2 = ?,";
        query = query + " ex_click_count_mobile2 = ?,";
        query = query + " impression_smart = ?,";
        query = query + " click_count_smart = ?,";
        query = query + " ex_click_count_smart = ?,";
        query = query + " ex_impression_smart = ?,";
        query = query + " ex_click_count_smart2 = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.sponsorCode );
            prestate.setInt( 2, this.addupDate );
            prestate.setInt( 3, this.impression );
            prestate.setInt( 4, this.impressionMobile );
            prestate.setInt( 5, this.clickCount );
            prestate.setInt( 6, this.clickCountMobile );
            prestate.setInt( 7, this.exClickCount );
            prestate.setInt( 8, this.exClickCountMobile );
            prestate.setInt( 9, this.exImpression );
            prestate.setInt( 10, this.exImpressionMobile );
            prestate.setInt( 11, this.exClickCount2 );
            prestate.setInt( 12, this.exClickCountMobile2 );
            prestate.setInt( 13, this.impressionSmart );
            prestate.setInt( 14, this.clickCountSmart );
            prestate.setInt( 15, this.exClickCountSmart );
            prestate.setInt( 16, this.exImpressionSmart );
            prestate.setInt( 17, this.exClickCountSmart2 );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSponsorData.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * スポンサー管理データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param sponsorCode スポンサーコード
     * @param addupDate 対象日付
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int sponsorCode, int addupDate)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_sponsor_data SET ";
        query = query + " impression = ?,";
        query = query + " impression_mobile = ?,";
        query = query + " click_count = ?,";
        query = query + " click_count_mobile = ?,";
        query = query + " ex_click_count = ?,";
        query = query + " ex_click_count_mobile = ?,";
        query = query + " ex_impression = ?,";
        query = query + " ex_impression_mobile = ?,";
        query = query + " ex_click_count2 = ?,";
        query = query + " ex_click_count_mobile2 = ?,";
        query = query + " impression_smart = ?,";
        query = query + " click_count_smart = ?,";
        query = query + " ex_click_count_smart = ?,";
        query = query + " ex_impression_smart = ?,";
        query = query + " ex_click_count_smart2 = ?";
        query = query + " WHERE sponsor_code = ? AND addup_date = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.impression );
            prestate.setInt( 2, this.impressionMobile );
            prestate.setInt( 3, this.clickCount );
            prestate.setInt( 4, this.clickCountMobile );
            prestate.setInt( 5, this.exClickCount );
            prestate.setInt( 6, this.exClickCountMobile );
            prestate.setInt( 7, this.exImpression );
            prestate.setInt( 8, this.exImpressionMobile );
            prestate.setInt( 9, this.exClickCount2 );
            prestate.setInt( 10, this.exClickCountMobile2 );
            prestate.setInt( 11, this.impressionSmart );
            prestate.setInt( 12, this.clickCountSmart );
            prestate.setInt( 13, this.exClickCountSmart );
            prestate.setInt( 14, this.exImpressionSmart );
            prestate.setInt( 15, this.exClickCountSmart2 );

            prestate.setInt( 16, sponsorCode );
            prestate.setInt( 17, addupDate );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSponsorData.updateData] Exception=" + e.toString() );
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
