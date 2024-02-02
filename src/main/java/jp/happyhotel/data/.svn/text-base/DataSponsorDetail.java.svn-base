/*
 * @(#)DataSponsorData.java 1.00 2007/09/01 Copyright (C) ALMEX Inc. 2007 スポンサー管理データ取得クラス
 */

package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * スポンサー詳細データ取得クラス
 * 
 * @author N.Ide
 * @version 1.00 2009/03/04
 */

public class DataSponsorDetail implements Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 6568261261636665995L;

    private int               sponsorCode;
    private int               seq;
    private String            title;
    private String            subtitle;
    private String            titleMobile;
    private String            subtitleMobile;
    private String            pr;
    private String            prMobile;
    private String            imageUrl;
    private String            imageUrlDocomo;
    private String            imageUrlAu;
    private String            imageUrlSoftbank;
    private String            imageUrl2;

    /**
     * データを初期化します。
     */
    public DataSponsorDetail()
    {
        sponsorCode = 0;
        seq = 0;
        title = "";
        subtitle = "";
        titleMobile = "";
        subtitleMobile = "";
        pr = "";
        prMobile = "";
        imageUrl = "";
        imageUrlDocomo = "";
        imageUrlAu = "";
        imageUrlSoftbank = "";
        imageUrl2 = "";
    }

    public int getSponsorCode()
    {
        return sponsorCode;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public String getTitleMobile()
    {
        return titleMobile;
    }

    public String getSubtitleMobile()
    {
        return subtitleMobile;
    }

    public String getPr()
    {
        return pr;
    }

    public String getPrMobile()
    {
        return prMobile;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public String getImageUrlDocomo()
    {
        return imageUrlDocomo;
    }

    public String getImageUrlAu()
    {
        return imageUrlAu;
    }

    public String getImageUrlSoftbank()
    {
        return imageUrlSoftbank;
    }

    public String getImageUrl2()
    {
        return imageUrl2;
    }

    public void setSponsorCode(int sponsorCode)
    {
        this.sponsorCode = sponsorCode;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setSubtitle(String subtitle)
    {
        this.subtitle = subtitle;
    }

    public void setTitleMobile(String titleMobile)
    {
        this.titleMobile = titleMobile;
    }

    public void setSubtitleMobile(String subtitleMobile)
    {
        this.subtitleMobile = subtitleMobile;
    }

    public void setPr(String pr)
    {
        this.pr = pr;
    }

    public void setPrMobile(String prMobile)
    {
        this.prMobile = prMobile;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public void setImageUrlDocomo(String imageUrlDocomo)
    {
        this.imageUrlDocomo = imageUrlDocomo;
    }

    public void setImageUrlAu(String imageUrlAu)
    {
        this.imageUrlAu = imageUrlAu;
    }

    public void setImageUrlSoftbank(String imageUrlSoftbank)
    {
        this.imageUrlSoftbank = imageUrlSoftbank;
    }

    public void setImageUrl2(String imageUrl2)
    {
        this.imageUrl2 = imageUrl2;
    }

    /**
     * スポンサー詳細データ取得
     * 
     * @param sponsorCode スポンサーコード
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean getData(int sponsorCode, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_sponsor_detail WHERE sponsor_code = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, sponsorCode );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.sponsorCode = result.getInt( "sponsor_code" );
                    this.seq = result.getInt( "seq" );
                    this.title = result.getString( "title" );
                    this.subtitle = result.getString( "subtitle" );
                    this.titleMobile = result.getString( "title_mobile" );
                    this.subtitleMobile = result.getString( "subtitle_mobile" );
                    this.pr = result.getString( "pr" );
                    this.imageUrl = result.getString( "image_url" );
                    this.imageUrlDocomo = result.getString( "image_url_docomo" );
                    this.imageUrlAu = result.getString( "image_url_au" );
                    this.imageUrlSoftbank = result.getString( "image_url_softbank" );
                    this.imageUrl2 = result.getString( "image_url2" );
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
     * スポンサー詳細データ設定
     * 
     * @param result スポンサー詳細データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.sponsorCode = result.getInt( "sponsor_code" );
                this.seq = result.getInt( "seq" );
                this.title = result.getString( "title" );
                this.subtitle = result.getString( "subtitle" );
                this.titleMobile = result.getString( "title_mobile" );
                this.subtitleMobile = result.getString( "subtitle_mobile" );
                this.pr = result.getString( "pr" );
                this.imageUrl = result.getString( "image_url" );
                this.imageUrlDocomo = result.getString( "image_url_docomo" );
                this.imageUrlAu = result.getString( "image_url_au" );
                this.imageUrlSoftbank = result.getString( "image_url_softbank" );
                this.imageUrl2 = result.getString( "image_url2" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSponsorDetail.setData] Exception=" + e.toString() );
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

        query = "INSERT hh_sponsor_detail SET ";
        query = query + " sponsor_code = ?,";
        query = query + " seq = ?,";
        query = query + " title = ?,";
        query = query + " subtitle = ?,";
        query = query + " title_mobile = ?,";
        query = query + " subtitle_mobile = ?,";
        query = query + " pr = ?,";
        query = query + " image_url = ?,";
        query = query + " image_url_docomo = ?,";
        query = query + " image_url_au = ?,";
        query = query + " image_url_softbank = ?,";
        query = query + " image_url2 = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.sponsorCode );
            prestate.setInt( 2, this.seq );
            prestate.setString( 3, this.title );
            prestate.setString( 4, this.subtitle );
            prestate.setString( 5, this.titleMobile );
            prestate.setString( 6, this.subtitleMobile );
            prestate.setString( 7, this.pr );
            prestate.setString( 8, this.imageUrl );
            prestate.setString( 9, this.imageUrlDocomo );
            prestate.setString( 10, this.imageUrlAu );
            prestate.setString( 11, this.imageUrlSoftbank );
            prestate.setString( 12, this.imageUrl2 );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSponsorDetail.insertData] Exception=" + e.toString() );
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
     * スポンサー詳細データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param sponsorCode スポンサーコード
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int sponsorCode, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_sponsor_detail SET ";
        query = query + " title = ?,";
        query = query + " subtitle = ?,";
        query = query + " title_mobile = ?,";
        query = query + " subtitle_mobile = ?,";
        query = query + " pr = ?,";
        query = query + " image_url = ?,";
        query = query + " image_url_docomo = ?,";
        query = query + " image_url_au = ?,";
        query = query + " image_url_softbank = ?,";
        query = query + " image_url2 = ?";
        query = query + " WHERE sponsor_code = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.title );
            prestate.setString( 2, this.subtitle );
            prestate.setString( 3, this.titleMobile );
            prestate.setString( 4, this.subtitleMobile );
            prestate.setString( 5, this.pr );
            prestate.setString( 6, this.imageUrl );
            prestate.setString( 7, this.imageUrlDocomo );
            prestate.setString( 8, this.imageUrlAu );
            prestate.setString( 9, this.imageUrlSoftbank );
            prestate.setString( 10, this.imageUrl2 );
            prestate.setInt( 11, sponsorCode );
            prestate.setInt( 12, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSponsorDetail.updateData] Exception=" + e.toString() );
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
