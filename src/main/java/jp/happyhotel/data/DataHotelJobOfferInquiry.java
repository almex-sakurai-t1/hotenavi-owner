/*
 * @(#)DataHotelJobOffer.java 1.00 2009/03/13 Copyright (C) ALMEX Inc. 2009 ホテル求人問い合わせ情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * ホテル求人問い合わせ情報データ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/03/13
 */
public class DataHotelJobOfferInquiry implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -624324030367675873L;

    private int               sponsorCode;
    private int               seq;
    private int               inquiryNum;
    private String            name;
    private String            mailAddr;
    private String            tell;
    private String            inquiry;
    private int               registDate;
    private int               registTime;

    /**
     * データを初期化します。
     */
    public DataHotelJobOfferInquiry()
    {
        sponsorCode = 0;
        seq = 0;
        inquiryNum = 0;
        mailAddr = "";
        tell = "";
        inquiry = "";
        registDate = 0;
        registTime = 0;
    }

    public String getInquiry()
    {
        return inquiry;
    }

    public int getInquiryNum()
    {
        return inquiryNum;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public String getName()
    {
        return name;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getSponsorCode()
    {
        return sponsorCode;
    }

    public String getTell()
    {
        return tell;
    }

    public void setInquiry(String inquiry)
    {
        this.inquiry = inquiry;
    }

    public void setInquiryNum(int inquiryNum)
    {
        this.inquiryNum = inquiryNum;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setSponsorCode(int sponsorCode)
    {
        this.sponsorCode = sponsorCode;
    }

    public void setTell(String tell)
    {
        this.tell = tell;
    }

    /**
     * ホテル求人情報データ取得
     * 
     * @param hotelId ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int sponsorCode, int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_joboffer_inquiry WHERE sponsor_code = ?, AND seq = ?" +
                " inquiry_num = ?";

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
                    this.inquiryNum = result.getInt( "inquiry_num" );
                    this.name = result.getString( "name" );
                    this.mailAddr = result.getString( "mail_addr" );
                    this.tell = result.getString( "tell" );
                    this.inquiry = result.getString( "inquiry_num" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelJobOfferInquiry.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
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
                this.inquiryNum = result.getInt( "inquiry_num" );
                this.name = result.getString( "name" );
                this.mailAddr = result.getString( "mail_addr" );
                this.tell = result.getString( "tell" );
                this.inquiry = result.getString( "inquiry_num" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelJobOfferInquiry.setData] Exception=" + e.toString() );
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

        query = "INSERT hh_hotel_joboffer_inquiry SET ";
        query = query + " sponsor_code = ?,";
        query = query + " seq = ?,";
        query = query + " inquiry_num = 0,";
        query = query + " name = ?,";
        query = query + " mail_addr = ?,";
        query = query + " tell = ?,";
        query = query + " inquiry = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.sponsorCode );
            prestate.setInt( 2, this.seq );
            prestate.setString( 3, this.name );
            prestate.setString( 4, this.mailAddr );
            prestate.setString( 5, this.tell );
            prestate.setString( 6, this.inquiry );
            prestate.setInt( 7, this.registDate );
            prestate.setInt( 8, this.registTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelJobOfferInquiry.insertData] Exception=" + e.toString() );
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

        query = "UPDATE hh_hotel_oboffer_inquiry SET ";
        query = query + " name = ?,";
        query = query + " mail_addr = ?,";
        query = query + " tell = ?,";
        query = query + " inquiry = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?";
        query = query + " WHERE sponsor_code = ? AND seq = ? AND inquiry_num = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.name );
            prestate.setString( 2, this.mailAddr );
            prestate.setString( 3, this.tell );
            prestate.setString( 4, this.inquiry );
            prestate.setInt( 5, this.registDate );
            prestate.setInt( 6, this.registTime );
            prestate.setInt( 7, this.sponsorCode );
            prestate.setInt( 8, this.seq );
            prestate.setInt( 9, this.inquiryNum );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelJobOfferInquiry.updateData] Exception=" + e.toString() );
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
