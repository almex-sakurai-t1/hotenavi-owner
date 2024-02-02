/*
 * @(#)DataHotelJobOffer.java 1.00 2009/03/13 Copyright (C) ALMEX Inc. 2009 ホテル求人情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * ホテル求人情報データ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/03/13
 */
public class DataHotelJobOffer implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -624324030367675873L;

    private int               sponsorCode;
    private int               seq;
    private int               hotelId;
    private int               delFlag;
    private String            title;
    private String            topText;
    private String            topTextMobile;
    private String            job;
    private String            workContents;
    private String            salary;
    private String            workHours;
    private String            jobQualifications;
    private String            holiday;
    private String            treatment;
    private String            workStartDate;
    private String            memo;
    private String            workLocation;
    private String            contactAddress;
    private String            url;
    private String            urlDocomo;
    private String            urlAu;
    private String            urlSoftbank;
    private String            bottomText;
    private String            bottomTextMobile;
    private int               registFlag;
    private int               startDate;
    private int               endDate;
    private String            mailAddr;

    /**
     * データを初期化します。
     */
    public DataHotelJobOffer()
    {
        sponsorCode = 0;
        seq = 0;
        hotelId = 0;
        delFlag = 0;
        title = "";
        topText = "";
        topTextMobile = "";
        job = "";
        workContents = "";
        salary = "";
        workHours = "";
        jobQualifications = "";
        holiday = "";
        treatment = "";
        workStartDate = "";
        memo = "";
        workLocation = "";
        contactAddress = "";
        url = "";
        urlDocomo = "";
        urlAu = "";
        urlSoftbank = "";
        bottomText = "";
        bottomTextMobile = "";
        registFlag = 0;
        startDate = 0;
        endDate = 0;
        mailAddr = "";
    }

    public String getBottomText()
    {
        return bottomText;
    }

    public String getBottomTextMobile()
    {
        return bottomTextMobile;
    }

    public String getContactAddress()
    {
        return contactAddress;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public String getHoliday()
    {
        return holiday;
    }

    public int getHotelId()
    {
        return hotelId;
    }

    public String getJob()
    {
        return job;
    }

    public String getJobQualifications()
    {
        return jobQualifications;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public String getMemo()
    {
        return memo;
    }

    public int getRegistFlag()
    {
        return registFlag;
    }

    public String getSalary()
    {
        return salary;
    }

    public int getSeq()
    {
        return seq;
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

    public String getTopText()
    {
        return topText;
    }

    public String getTopTextMobile()
    {
        return topTextMobile;
    }

    public String getTreatment()
    {
        return treatment;
    }

    public String getUrl()
    {
        return url;
    }

    public String getUrlAu()
    {
        return urlAu;
    }

    public String getUrlDocomo()
    {
        return urlDocomo;
    }

    public String getUrlSoftbank()
    {
        return urlSoftbank;
    }

    public String getWorkContents()
    {
        return workContents;
    }

    public String getWorkHours()
    {
        return workHours;
    }

    public String getWorkLocation()
    {
        return workLocation;
    }

    public String getWorkStartDate()
    {
        return workStartDate;
    }

    public void setBottomText(String bottomText)
    {
        this.bottomText = bottomText;
    }

    public void setBottomTextMobile(String bottomTextMobile)
    {
        this.bottomTextMobile = bottomTextMobile;
    }

    public void setContactAddress(String contactAddress)
    {
        this.contactAddress = contactAddress;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setHoliday(String holiday)
    {
        this.holiday = holiday;
    }

    public void setHotelId(int hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setJob(String job)
    {
        this.job = job;
    }

    public void setJobQualifications(String jobQualifications)
    {
        this.jobQualifications = jobQualifications;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public void setRegistFlag(int registFlag)
    {
        this.registFlag = registFlag;
    }

    public void setSalary(String salary)
    {
        this.salary = salary;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
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

    public void setTopText(String topText)
    {
        this.topText = topText;
    }

    public void setTopTextMobile(String topTextMobile)
    {
        this.topTextMobile = topTextMobile;
    }

    public void setTreatment(String treatment)
    {
        this.treatment = treatment;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setUrlAu(String urlAu)
    {
        this.urlAu = urlAu;
    }

    public void setUrlDocomo(String urlDocomo)
    {
        this.urlDocomo = urlDocomo;
    }

    public void setUrlSoftbank(String urlSoftbank)
    {
        this.urlSoftbank = urlSoftbank;
    }

    public void setWorkContents(String workContents)
    {
        this.workContents = workContents;
    }

    public void setWorkHours(String workHours)
    {
        this.workHours = workHours;
    }

    public void setWorkLocation(String workLocation)
    {
        this.workLocation = workLocation;
    }

    public void getWorkStartDate(String workStartDate)
    {
        this.workStartDate = workStartDate;
    }

    /**
     * ホテル求人情報データ取得
     * 
     * @param hotelId ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int sponsorCode, int seq)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_joboffer WHERE sponsor_code = ? AND seq = ?";

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
                    this.hotelId = result.getInt( "hotel_id" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.title = result.getString( "title" );
                    this.topText = result.getString( "top_text" );
                    this.topTextMobile = result.getString( "top_text_mobile" );
                    this.job = result.getString( "job" );
                    this.workContents = result.getString( "work_contents" );
                    this.salary = result.getString( "salary" );
                    this.workHours = result.getString( "work_hours" );
                    this.jobQualifications = result.getString( "job_qualifications" );
                    this.holiday = result.getString( "holiday" );
                    this.treatment = result.getString( "treatment" );
                    this.workStartDate = result.getString( "work_start_date" );
                    this.memo = result.getString( "memo" );
                    this.workLocation = result.getString( "work_location" );
                    this.contactAddress = result.getString( "contact_address" );
                    this.url = result.getString( "url" );
                    this.urlDocomo = result.getString( "url_docomo" );
                    this.urlAu = result.getString( "url_au" );
                    this.urlSoftbank = result.getString( "url_softbank" );
                    this.bottomText = result.getString( "bottom_text" );
                    this.bottomTextMobile = result.getString( "bottom_text_mobile" );
                    this.registFlag = result.getInt( "regist_flag" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.mailAddr = result.getString( "mail_addr" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelJobOffer.getData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテル求人情報データ設定
     * 
     * @param result ホテル設備情報データレコード
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
                this.hotelId = result.getInt( "hotel_id" );
                this.delFlag = result.getInt( "del_flag" );
                this.title = result.getString( "title" );
                this.topText = result.getString( "top_text" );
                this.topTextMobile = result.getString( "top_text_mobile" );
                this.job = result.getString( "job" );
                this.workContents = result.getString( "work_contents" );
                this.salary = result.getString( "salary" );
                this.workHours = result.getString( "work_hours" );
                this.jobQualifications = result.getString( "job_qualifications" );
                this.holiday = result.getString( "holiday" );
                this.treatment = result.getString( "treatment" );
                this.workStartDate = result.getString( "work_start_date" );
                this.memo = result.getString( "memo" );
                this.workLocation = result.getString( "work_location" );
                this.contactAddress = result.getString( "contact_address" );
                this.url = result.getString( "url" );
                this.urlDocomo = result.getString( "url_docomo" );
                this.urlAu = result.getString( "url_au" );
                this.urlSoftbank = result.getString( "url_softbank" );
                this.bottomText = result.getString( "bottom_text" );
                this.bottomTextMobile = result.getString( "bottom_text_mobile" );
                this.registFlag = result.getInt( "regist_flag" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.mailAddr = result.getString( "mail_addr" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelJobOffer.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ホテル求人情報データ追加
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

        query = "INSERT hh_hotel_joboffer SET ";
        query = query + " sponsor_code = ?,";
        query = query + " seq = 0,";
        query = query + " hotel_id = ?,";
        query = query + " del_flag = ?,";
        query = query + " title = ?,";
        query = query + " top_text = ?,";
        query = query + " top_text_mobile = ?,";
        query = query + " job = ?,";
        query = query + " work_contents = ?,";
        query = query + " salary = ?,";
        query = query + " work_hours = ?,";
        query = query + " job_qualifications = ?,";
        query = query + " holiday = ?,";
        query = query + " treatment = ?,";
        query = query + " work_start_date = ?,";
        query = query + " memo = ?,";
        query = query + " work_location = ?,";
        query = query + " contact_address = ?,";
        query = query + " url = ?,";
        query = query + " url_docomo = ?,";
        query = query + " url_au = ?,";
        query = query + " url_softbank = ?,";
        query = query + " bottom_text = ?,";
        query = query + " bottom_text_mobile = ?,";
        query = query + " regist_flag = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " mail_addr = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.sponsorCode );
            prestate.setInt( 2, this.hotelId );
            prestate.setInt( 3, this.delFlag );
            prestate.setString( 4, this.title );
            prestate.setString( 5, this.topText );
            prestate.setString( 6, this.topTextMobile );
            prestate.setString( 7, this.job );
            prestate.setString( 8, this.workContents );
            prestate.setString( 9, this.salary );
            prestate.setString( 10, this.workHours );
            prestate.setString( 11, this.jobQualifications );
            prestate.setString( 12, this.holiday );
            prestate.setString( 13, this.treatment );
            prestate.setString( 14, this.workStartDate );
            prestate.setString( 15, this.memo );
            prestate.setString( 16, this.workLocation );
            prestate.setString( 17, this.contactAddress );
            prestate.setString( 18, this.url );
            prestate.setString( 19, this.urlDocomo );
            prestate.setString( 20, this.urlAu );
            prestate.setString( 21, this.urlSoftbank );
            prestate.setString( 22, this.bottomText );
            prestate.setString( 23, this.bottomTextMobile );
            prestate.setInt( 24, this.registFlag );
            prestate.setInt( 25, this.startDate );
            prestate.setInt( 26, this.endDate );
            prestate.setString( 27, this.mailAddr );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelJobOffer.insertData] Exception=" + e.toString() );
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
     * ホテル求人情報データ更新
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

        query = "UPDATE hh_hotel_oboffer SET ";
        query = query + " hotel_id = ?,";
        query = query + " del_flag = ?,";
        query = query + " title = ?,";
        query = query + " top_text = ?,";
        query = query + " top_text_mobile = ?,";
        query = query + " job = ?,";
        query = query + " work_contents = ?,";
        query = query + " salary = ?,";
        query = query + " work_hours = ?,";
        query = query + " job_qualifications = ?,";
        query = query + " holiday = ?,";
        query = query + " treatment = ?,";
        query = query + " work_start_date = ?,";
        query = query + " memo = ?,";
        query = query + " work_location = ?,";
        query = query + " contact_address = ?,";
        query = query + " url = ?,";
        query = query + " url_docomo = ?,";
        query = query + " url_au = ?,";
        query = query + " url_softbank = ?,";
        query = query + " bottom_text = ?,";
        query = query + " bottom_text_mobile = ?,";
        query = query + " regist_flag = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " mail_addr = ?";
        query = query + " WHERE sponsor_code = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.hotelId );
            prestate.setInt( 2, this.delFlag );
            prestate.setString( 3, this.title );
            prestate.setString( 4, this.topText );
            prestate.setString( 5, this.topTextMobile );
            prestate.setString( 6, this.job );
            prestate.setString( 7, this.workContents );
            prestate.setString( 8, this.salary );
            prestate.setString( 9, this.workHours );
            prestate.setString( 10, this.jobQualifications );
            prestate.setString( 11, this.holiday );
            prestate.setString( 12, this.treatment );
            prestate.setString( 13, this.workStartDate );
            prestate.setString( 14, this.memo );
            prestate.setString( 15, this.workLocation );
            prestate.setString( 16, this.contactAddress );
            prestate.setString( 17, this.url );
            prestate.setString( 18, this.urlDocomo );
            prestate.setString( 19, this.urlAu );
            prestate.setString( 20, this.urlSoftbank );
            prestate.setString( 21, this.bottomText );
            prestate.setString( 22, this.bottomTextMobile );
            prestate.setInt( 23, this.registFlag );
            prestate.setInt( 24, this.startDate );
            prestate.setInt( 25, this.endDate );
            prestate.setString( 26, this.mailAddr );
            prestate.setInt( 27, this.sponsorCode );
            prestate.setInt( 28, this.seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelJobOffer.updateData] Exception=" + e.toString() );
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
