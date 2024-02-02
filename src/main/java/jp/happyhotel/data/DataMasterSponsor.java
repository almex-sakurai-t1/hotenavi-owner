/*
 * @(#)DataMasterSponsor.java 1.00 2007/09/01 Copyright (C) ALMEX Inc. 2007 スポンサー管理マスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DBSync;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Url;

/**
 * スポンサー管理マスタ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/09/01
 * @version 1.1 2007/11/29
 */
public class DataMasterSponsor implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -6534240496304070378L;

    private int               sponsorCode;
    private int               areaCode;
    private String            dispPos;
    private int               hotelId;
    private String            title;
    private String            titleMobile;
    private String            pr;
    private String            prMobile;
    private int               startDate;
    private int               endDate;
    private String            url;
    private String            urlDocomo;
    private String            urlAu;
    private String            urlSoftbank;
    private int               dispText;
    private int               dispImage;
    private byte[]            image;
    private byte[]            imageGif;
    private byte[]            imagePng;
    private int               appendDate;
    private int               appendTime;
    private String            exUrl;
    private String            exUrlDocomo;
    private String            exUrlAu;
    private String            exUrlSoftbank;
    private String            adTitle;
    private String            adTitleMobile;
    private String            adUrl;
    private String            adUrlDocomo;
    private String            adUrlAu;
    private String            adUrlSoftbank;
    private String            url2;
    private String            exUrl2;
    private int               randomDispFlag;
    private int               ownerStartDate;
    private int               groupEndDate;
    private int               localId;
    private int               prefId;
    private int               memberFlag;
    private String            urlSmart;
    private String            urlSmart2;
    private String            adUrlSmart;
    private int               dispIndex;

    /**
     * データを初期化します。
     */
    public DataMasterSponsor()
    {
        sponsorCode = 0;
        areaCode = 0;
        dispPos = "";
        hotelId = 0;
        title = "";
        titleMobile = "";
        pr = "";
        prMobile = "";
        startDate = 0;
        endDate = 0;
        url = "";
        urlDocomo = "";
        urlAu = "";
        urlSoftbank = "";
        dispText = 0;
        dispImage = 0;
        image = null;
        imageGif = null;
        imagePng = null;
        appendDate = 0;
        appendTime = 0;
        exUrl = "";
        exUrlDocomo = "";
        exUrlAu = "";
        exUrlSoftbank = "";
        adTitle = "";
        adTitleMobile = "";
        adUrl = "";
        adUrlDocomo = "";
        adUrlAu = "";
        adUrlSoftbank = "";
        url2 = "";
        exUrl2 = "";
        randomDispFlag = 0;
        ownerStartDate = 0;
        groupEndDate = 0;
        localId = 0;
        prefId = 0;
        memberFlag = 0;
        urlSmart = "";
        urlSmart2 = "";
        adUrlSmart = "";
        dispIndex = 0;
    }

    public String getAdTitle()
    {
        return adTitle;
    }

    public String getAdTitleMobile()
    {
        return adTitleMobile;
    }

    public String getAdUrl()
    {
        return Url.convertUrl( adUrl );
    }

    public String getAdUrlAu()
    {
        return Url.convertUrl( adUrlAu );
    }

    public String getAdUrlDocomo()
    {
        return Url.convertUrl( adUrlDocomo );
    }

    public String getAdUrlSmart()
    {
        return Url.convertUrl( adUrlSmart );
    }

    public String getAdUrlSoftbank()
    {
        return Url.convertUrl( adUrlSoftbank );
    }

    public int getAppendDate()
    {
        return appendDate;
    }

    public int getAppendTime()
    {
        return appendTime;
    }

    public int getAreaCode()
    {
        return areaCode;
    }

    public int getDispImage()
    {
        return dispImage;
    }

    public String getDispPos()
    {
        return dispPos;
    }

    public int getDispText()
    {
        return dispText;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public String getExUrl()
    {
        return Url.convertUrl( exUrl );
    }

    public String getExUrl2()
    {
        return Url.convertUrl( exUrl2 );
    }

    public String getExUrlDocomo()
    {
        return Url.convertUrl( exUrlDocomo );
    }

    public String getExUrlAu()
    {
        return Url.convertUrl( exUrlAu );
    }

    public String getExUrlSoftbank()
    {
        return Url.convertUrl( exUrlSoftbank );
    }

    public int getGroupEndDate()
    {
        return groupEndDate;
    }

    public int getHotelId()
    {
        return hotelId;
    }

    public byte[] getImage()
    {
        return image;
    }

    public byte[] getImageGif()
    {
        return imageGif;
    }

    public byte[] getImagePng()
    {
        return imagePng;
    }

    public int getLocalId()
    {
        return localId;
    }

    public int getMemberFlag()
    {
        return memberFlag;
    }

    public int getOwnerStartDate()
    {
        return ownerStartDate;
    }

    public String getPr()
    {
        return pr;
    }

    public int getPrefId()
    {
        return prefId;
    }

    public String getPrMobile()
    {
        return prMobile;
    }

    public int getRandomDispFlag()
    {
        return randomDispFlag;
    }

    public int getSponsorCode()
    {
        return sponsorCode;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public String getTitle()
    {
        return title;
    }

    public String getTitleMobile()
    {
        return titleMobile;
    }

    public String getUrl()
    {
        return Url.convertUrl( url );
    }

    public String getUrl2()
    {
        return Url.convertUrl( url2 );
    }

    public String getUrlAu()
    {
        return Url.convertUrl( urlAu );
    }

    public String getUrlDocomo()
    {
        return Url.convertUrl( urlDocomo );
    }

    public String getUrlSmart()
    {
        return Url.convertUrl( urlSmart );
    }

    public String getUrlSmart2()
    {
        return Url.convertUrl( urlSmart2 );
    }

    public String getUrlSoftbank()
    {
        return Url.convertUrl( urlSoftbank );
    }

    public int getDispIndex()
    {
        return dispIndex;
    }

    // setter
    public void setAdTitle(String adTitle)
    {
        this.adTitle = adTitle;
    }

    public void setAdTitleMobile(String adTitleMobile)
    {
        this.adTitleMobile = adTitleMobile;
    }

    public void setAdUrl(String adUrl)
    {
        this.adUrl = adUrl;
    }

    public void setAdUrlAu(String adUrlAu)
    {
        this.adUrlAu = adUrlAu;
    }

    public void setAdUrlDocomo(String adUrlDocomo)
    {
        this.adUrlDocomo = adUrlDocomo;
    }

    public void setAdUrlSmart(String adUrlSmart)
    {
        this.adUrlSmart = adUrlSmart;
    }

    public void setAdUrlSoftbank(String adUrlSoftbank)
    {
        this.adUrlSoftbank = adUrlSoftbank;
    }

    public void setAppendDate(int appendDate)
    {
        this.appendDate = appendDate;
    }

    public void setAppendTime(int appendTime)
    {
        this.appendTime = appendTime;
    }

    public void setAreaCode(int areaCode)
    {
        this.areaCode = areaCode;
    }

    public void setDispImage(int dispImage)
    {
        this.dispImage = dispImage;
    }

    public void setDispPos(String dispPos)
    {
        this.dispPos = dispPos;
    }

    public void setDispText(int dispText)
    {
        this.dispText = dispText;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setExUrl(String exUrl)
    {
        this.exUrl = exUrl;
    }

    public void setExUrl2(String exUrl2)
    {
        this.exUrl2 = exUrl2;
    }

    public void setExUrlAu(String exUrlAu)
    {
        this.exUrlAu = exUrlAu;
    }

    public void setExUrlDocomo(String exUrlDocomo)
    {
        this.exUrlDocomo = exUrlDocomo;
    }

    public void setExUrlSoftbank(String exUrlSoftbank)
    {
        this.exUrlSoftbank = exUrlSoftbank;
    }

    public void setGroupEndDate(int groupEndDate)
    {
        this.groupEndDate = groupEndDate;
    }

    public void setHotelId(int hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }

    public void setImageGif(byte[] imageGif)
    {
        this.imageGif = imageGif;
    }

    public void setImagePng(byte[] imagePng)
    {
        this.imagePng = imagePng;
    }

    public void setLocalId(int localId)
    {
        this.localId = localId;
    }

    public void setMemberFlag(int memberFlag)
    {
        this.memberFlag = memberFlag;
    }

    public void setOwnerStartDate(int ownerStartDate)
    {
        this.ownerStartDate = ownerStartDate;
    }

    public void setPr(String pr)
    {
        this.pr = pr;
    }

    public void setPrefId(int prefId)
    {
        this.prefId = prefId;
    }

    public void setPrMobile(String prMobile)
    {
        this.prMobile = prMobile;
    }

    public void setRandomDispFlag(int randomDispFlag)
    {
        this.randomDispFlag = randomDispFlag;
    }

    public void setSponsorCode(int sponsorCode)
    {
        this.sponsorCode = sponsorCode;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setTitleMobile(String titleMobile)
    {
        this.titleMobile = titleMobile;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setUrl2(String url2)
    {
        this.url2 = url2;
    }

    public void setUrlAu(String urlAu)
    {
        this.urlAu = urlAu;
    }

    public void setUrlDocomo(String urlDocomo)
    {
        this.urlDocomo = urlDocomo;
    }

    public void setUrlSmart(String urlSmart)
    {
        this.urlSmart = urlSmart;
    }

    public void setUrlSmart2(String urlSmart2)
    {
        this.urlSmart2 = urlSmart2;
    }

    public void setUrlSoftbank(String urlSoftbank)
    {
        this.urlSoftbank = urlSoftbank;
    }

    public void setDispIndex(int dispIndex)
    {
        this.dispIndex = dispIndex;
    }

    /**
     * スポンサー管理マスタ取得
     * 
     * @param sponsorCode スポンサーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int sponsorCode)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_master_sponsor WHERE sponsor_code = ?";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, sponsorCode );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.sponsorCode = result.getInt( "sponsor_code" );
                    this.areaCode = result.getInt( "area_code" );
                    this.dispPos = result.getString( "disp_pos" );
                    this.hotelId = result.getInt( "hotel_id" );
                    this.title = result.getString( "title" );
                    this.titleMobile = result.getString( "title_mobile" );
                    this.pr = result.getString( "pr" );
                    this.prMobile = result.getString( "pr_mobile" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.url = result.getString( "url" );
                    this.urlDocomo = result.getString( "url_docomo" );
                    this.urlAu = result.getString( "url_au" );
                    this.urlSoftbank = result.getString( "url_softbank" );
                    this.dispText = result.getInt( "disp_text" );
                    this.dispImage = result.getInt( "disp_image" );
                    this.image = result.getBytes( "image" );
                    this.imageGif = result.getBytes( "image_gif" );
                    this.imagePng = result.getBytes( "image_png" );
                    this.appendDate = result.getInt( "append_date" );
                    this.appendTime = result.getInt( "append_time" );
                    this.exUrl = result.getString( "ex_url" );
                    this.exUrlDocomo = result.getString( "ex_url_docomo" );
                    this.exUrlAu = result.getString( "ex_url_au" );
                    this.exUrlSoftbank = result.getString( "ex_url_softbank" );
                    this.adTitle = result.getString( "ad_title" );
                    this.adTitleMobile = result.getString( "ad_title_mobile" );
                    this.adUrl = result.getString( "ad_url" );
                    this.adUrlDocomo = result.getString( "ad_url_docomo" );
                    this.adUrlAu = result.getString( "ad_url_au" );
                    this.adUrlSoftbank = result.getString( "ad_url_softbank" );
                    this.url2 = result.getString( "url2" );
                    this.exUrl2 = result.getString( "ex_url2" );
                    this.randomDispFlag = result.getInt( "random_disp_flag" );
                    this.ownerStartDate = result.getInt( "owner_start_date" );
                    this.groupEndDate = result.getInt( "group_end_date" );
                    this.localId = result.getInt( "local_id" );
                    this.prefId = result.getInt( "pref_id" );
                    this.memberFlag = result.getInt( "member_flag" );
                    this.urlSmart = result.getString( "url_smart" );
                    this.urlSmart2 = result.getString( "url_smart2" );
                    this.adUrlSmart = result.getString( "ad_url_smart" );
                    this.dispIndex = result.getInt( "disp_index" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSponsor.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * スポンサー管理マスタ設定
     * 
     * @param result スポンサー管理マスタレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.sponsorCode = result.getInt( "sponsor_code" );
                this.areaCode = result.getInt( "area_code" );
                this.dispPos = result.getString( "disp_pos" );
                this.hotelId = result.getInt( "hotel_id" );
                this.title = result.getString( "title" );
                this.titleMobile = result.getString( "title_mobile" );
                this.pr = result.getString( "pr" );
                this.prMobile = result.getString( "pr_mobile" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.url = result.getString( "url" );
                this.urlDocomo = result.getString( "url_docomo" );
                this.urlAu = result.getString( "url_au" );
                this.urlSoftbank = result.getString( "url_softbank" );
                this.dispText = result.getInt( "disp_text" );
                this.dispImage = result.getInt( "disp_image" );
                this.image = result.getBytes( "image" );
                this.imageGif = result.getBytes( "image_gif" );
                this.imagePng = result.getBytes( "image_png" );
                this.appendDate = result.getInt( "append_date" );
                this.appendTime = result.getInt( "append_time" );
                this.exUrl = result.getString( "ex_url" );
                this.exUrlDocomo = result.getString( "ex_url_docomo" );
                this.exUrlAu = result.getString( "ex_url_au" );
                this.exUrlSoftbank = result.getString( "ex_url_softbank" );
                this.adTitle = result.getString( "ad_title" );
                this.adTitleMobile = result.getString( "ad_title_mobile" );
                this.adUrl = result.getString( "ad_url" );
                this.adUrlDocomo = result.getString( "ad_url_docomo" );
                this.adUrlAu = result.getString( "ad_url_au" );
                this.adUrlSoftbank = result.getString( "ad_url_softbank" );
                this.url2 = result.getString( "url2" );
                this.exUrl2 = result.getString( "ex_url2" );
                this.randomDispFlag = result.getInt( "random_disp_flag" );
                this.ownerStartDate = result.getInt( "owner_start_date" );
                this.groupEndDate = result.getInt( "group_end_date" );
                this.localId = result.getInt( "local_id" );
                this.prefId = result.getInt( "pref_id" );
                this.memberFlag = result.getInt( "member_flag" );
                this.urlSmart = result.getString( "url_smart" );
                this.urlSmart2 = result.getString( "url_smart2" );
                this.adUrlSmart = result.getString( "ad_url_smart" );
                this.dispIndex = result.getInt( "disp_index" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSponsor.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ホテル部屋情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        return insertData( false );
    }

    /**
     * ホテル部屋情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(boolean isStg)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_master_sponsor SET ";
        query = query + " sponsor_code = ?,";
        query = query + " area_code = ?,";
        query = query + " disp_pos = ?,";
        query = query + " hotel_id = ?,";
        query = query + " title = ?,";
        query = query + " title_mobile = ?,";
        query = query + " pr = ?,";
        query = query + " pr_mobile = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " url = ?,";
        query = query + " url_docomo = ?,";
        query = query + " url_au = ?,";
        query = query + " url_softbank = ?,";
        query = query + " disp_text = ?,";
        query = query + " disp_image = ?,";
        query = query + " image = ?,";
        query = query + " image_gif = ?,";
        query = query + " image_png = ?,";
        query = query + " append_date = ?,";
        query = query + " append_time = ?,";
        query = query + " ex_url = ?,";
        query = query + " ex_url_docomo = ?,";
        query = query + " ex_url_au = ?,";
        query = query + " ex_url_softbank = ?,";
        query = query + " ad_title = ?,";
        query = query + " ad_title_mobile = ?,";
        query = query + " ad_url = ?,";
        query = query + " ad_url_docomo = ?,";
        query = query + " ad_url_au = ?,";
        query = query + " ad_url_softbank = ?,";
        query = query + " url2 = ?,";
        query = query + " ex_url2 = ?,";
        query = query + " random_disp_flag = ?,";
        query = query + " owner_start_date = ?,";
        query = query + " group_end_date = ?,";
        query = query + " local_id = ?,";
        query = query + " pref_id = ?,";
        query = query + " member_flag = ?,";
        query = query + " url_smart = ?,";
        query = query + " url_smart2 = ?,";
        query = query + " ad_url_smart = ?,";
        query = query + " disp_index = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.sponsorCode );
            prestate.setInt( 2, this.areaCode );
            prestate.setString( 3, this.dispPos );
            prestate.setInt( 4, this.hotelId );
            prestate.setString( 5, this.title );
            prestate.setString( 6, this.titleMobile );
            prestate.setString( 7, this.pr );
            prestate.setString( 8, this.prMobile );
            prestate.setInt( 9, this.startDate );
            prestate.setInt( 10, this.endDate );
            prestate.setString( 11, this.url );
            prestate.setString( 12, this.urlDocomo );
            prestate.setString( 13, this.urlAu );
            prestate.setString( 14, this.urlSoftbank );
            prestate.setInt( 15, this.dispText );
            prestate.setInt( 16, this.dispImage );
            prestate.setBytes( 17, this.image );
            prestate.setBytes( 18, this.imageGif );
            prestate.setBytes( 19, this.imagePng );
            prestate.setInt( 20, this.appendDate );
            prestate.setInt( 21, this.appendTime );
            prestate.setString( 22, this.exUrl );
            prestate.setString( 23, this.exUrlDocomo );
            prestate.setString( 24, this.exUrlAu );
            prestate.setString( 25, this.exUrlSoftbank );
            prestate.setString( 26, this.adTitle );
            prestate.setString( 27, this.adTitleMobile );
            prestate.setString( 28, this.adUrl );
            prestate.setString( 29, this.adUrlDocomo );
            prestate.setString( 30, this.adUrlAu );
            prestate.setString( 31, this.adUrlSoftbank );
            prestate.setString( 32, this.url2 );
            prestate.setString( 33, this.exUrl2 );
            prestate.setInt( 34, this.randomDispFlag );
            prestate.setInt( 35, this.ownerStartDate );
            prestate.setInt( 36, this.groupEndDate );
            prestate.setInt( 37, this.localId );
            prestate.setInt( 38, this.prefId );
            prestate.setInt( 39, this.memberFlag );
            prestate.setString( 40, this.urlSmart );
            prestate.setString( 41, this.urlSmart2 );
            prestate.setString( 42, this.adUrlSmart );
            prestate.setInt( 43, this.dispIndex );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                // PubSubデータ同期
                DBSync.publish( prestate, true, isStg );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSponsor.insertData] Exception=" + e.toString() );
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
     * ホテル部屋情報データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param sponsorCode スポンサーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int sponsorCode)
    {
        return updateData( sponsorCode, false );
    }

    /**
     * ホテル部屋情報データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param sponsorCode スポンサーコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int sponsorCode, boolean isStg)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_master_sponsor SET ";
        query = query + " area_code = ?,";
        query = query + " disp_pos = ?,";
        query = query + " hotel_id = ?,";
        query = query + " title = ?,";
        query = query + " title_mobile = ?,";
        query = query + " pr = ?,";
        query = query + " pr_mobile = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " url = ?,";
        query = query + " url_docomo = ?,";
        query = query + " url_au = ?,";
        query = query + " url_softbank = ?,";
        query = query + " disp_text = ?,";
        query = query + " disp_image = ?,";
        query = query + " image = ?,";
        query = query + " image_gif = ?,";
        query = query + " image_png = ?,";
        query = query + " append_date = ?,";
        query = query + " append_time = ?,";
        query = query + " ex_url = ?,";
        query = query + " ex_url_docomo = ?,";
        query = query + " ex_url_au = ?,";
        query = query + " ex_url_softbank = ?,";
        query = query + " ad_title = ?,";
        query = query + " ad_title_mobile = ?,";
        query = query + " ad_url = ?,";
        query = query + " ad_url_Docomo = ?,";
        query = query + " ad_url_Au = ?,";
        query = query + " ad_url_Softbank = ?,";
        query = query + " url2 = ?,";
        query = query + " ex_url2 = ?,";
        query = query + " random_disp_flag = ?,";
        query = query + " owner_start_date = ?,";
        query = query + " group_end_date = ?,";
        query = query + " local_id = ?,";
        query = query + " pref_id = ?,";
        query = query + " member_flag = ?,";
        query = query + " url_smart = ?,";
        query = query + " url_smart2 = ?,";
        query = query + " ad_url_smart = ?,";
        query = query + " disp_index = ? ";
        query = query + " WHERE sponsor_code = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.areaCode );
            prestate.setString( 2, this.dispPos );
            prestate.setInt( 3, this.hotelId );
            prestate.setString( 4, this.title );
            prestate.setString( 5, this.titleMobile );
            prestate.setString( 6, this.pr );
            prestate.setString( 7, this.prMobile );
            prestate.setInt( 8, this.startDate );
            prestate.setInt( 9, this.endDate );
            prestate.setString( 10, this.url );
            prestate.setString( 11, this.urlDocomo );
            prestate.setString( 12, this.urlAu );
            prestate.setString( 13, this.urlSoftbank );
            prestate.setInt( 14, this.dispText );
            prestate.setInt( 15, this.dispImage );
            prestate.setBytes( 16, this.image );
            prestate.setBytes( 17, this.imageGif );
            prestate.setBytes( 18, this.imagePng );
            prestate.setInt( 19, this.appendDate );
            prestate.setInt( 20, this.appendTime );
            prestate.setString( 21, this.exUrl );
            prestate.setString( 22, this.exUrlDocomo );
            prestate.setString( 23, this.exUrlAu );
            prestate.setString( 24, this.exUrlSoftbank );
            prestate.setString( 25, this.adTitle );
            prestate.setString( 26, this.adTitleMobile );
            prestate.setString( 27, this.adUrl );
            prestate.setString( 28, this.adUrlDocomo );
            prestate.setString( 29, this.adUrlAu );
            prestate.setString( 30, this.adUrlSoftbank );
            prestate.setString( 31, this.url2 );
            prestate.setString( 32, this.exUrl2 );
            prestate.setInt( 33, this.randomDispFlag );
            prestate.setInt( 34, this.ownerStartDate );
            prestate.setInt( 35, this.groupEndDate );
            prestate.setInt( 36, this.localId );
            prestate.setInt( 37, this.prefId );
            prestate.setInt( 38, this.memberFlag );
            prestate.setString( 39, this.urlSmart );
            prestate.setString( 40, this.urlSmart2 );
            prestate.setString( 41, this.adUrlSmart );
            prestate.setInt( 42, this.dispIndex );
            prestate.setInt( 43, sponsorCode );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                // sql文取得
                String sql = prestate.toString().split( ":", 2 )[1];

                // schema追加
                sql = sql.replace( "UPDATE ", "UPDATE hotenavi." );

                // updateをreplace文に変える
                // sql = DBSync.updateToReplaceSQL( sql );

                if ( isStg )
                {
                    sql = sql.replace( "https://happyhotel.jp", "https://full.gcpstg.happyhotel.jp" );
                }

                // PubSubデータ同期
                DBSync.publish( sql, isStg );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSponsor.updateData] Exception=" + e.toString() );
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
