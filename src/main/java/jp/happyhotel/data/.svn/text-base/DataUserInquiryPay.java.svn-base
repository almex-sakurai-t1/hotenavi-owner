/*
 * @(#)DataUserInquiryPay 1.00 2009/11/05
 * Copyright (C) ALMEX Inc. 2009
 * 有料ユーザー問い合わせ情報クラス
 */
package jp.happyhotel.data;

/**
 * 有料ユーザー問い合わせ情報クラス
 * 
 * 
 * @author S.Tashiro
 * @version 1.00 2009/11/05
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class DataUserInquiryPay implements Serializable
{
    private static final long serialVersionUID = -6209451495209580781L;

    private int               inquiryNo;
    private int               inquiryNoSub;
    private int               inquiryStatus;
    private String            userId;
    private String            userName;
    private String            termNo;
    private String            mailAddr;
    private String            hotelName;
    private String            hotelAddress;
    private String            inquiryKind;
    private String            inquiry;
    private int               inquiryDate;
    private int               inquiryTime;
    private String            inquiryIp;
    private String            inquiryUseragent;
    private String            returnInquiry;
    private int               returnDate;
    private int               returnTime;
    private String            returnIp;
    private String            returnUseragent;
    private String            returnName;

    /**
     * データを初期化します。
     */
    public DataUserInquiryPay()
    {
        inquiryNo = 0;
        inquiryNoSub = 0;
        inquiryStatus = 0;
        userId = "";
        userName = "";
        termNo = "";
        mailAddr = "";
        hotelName = "";
        hotelAddress = "";
        inquiryKind = "";
        inquiry = "";
        inquiryDate = 0;
        inquiryTime = 0;
        inquiryIp = "";
        inquiryUseragent = "";
        returnInquiry = "";
        returnDate = 0;
        returnTime = 0;
        returnIp = "";
        returnUseragent = "";
        returnName = "";
    }

    public int getInquiryNo()
    {
        return inquiryNo;
    }

    public int getInquiryNoSub()
    {
        return inquiryNoSub;
    }

    public int getInquiryStatus()
    {
        return inquiryStatus;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getTermNo()
    {
        return termNo;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public String getHotelName()
    {
        return hotelName;
    }

    public String getHotelAddress()
    {
        return hotelAddress;
    }

    public String getInquiryKind()
    {
        return inquiryKind;
    }

    public String getInquiry()
    {
        return inquiry;
    }

    public int getInquiryDate()
    {
        return inquiryDate;
    }

    public int getInquiryTime()
    {
        return inquiryTime;
    }

    public String getInquiryIp()
    {
        return inquiryIp;
    }

    public String getInquiryUseragent()
    {
        return inquiryUseragent;
    }

    public String getReturnInquiry()
    {
        return returnInquiry;
    }

    public int getReturnDate()
    {
        return returnDate;
    }

    public int getReturnTime()
    {
        return returnTime;
    }

    public String getReturnIp()
    {
        return returnIp;
    }

    public String getReturnUseragent()
    {
        return returnUseragent;
    }

    public String getReturnName()
    {
        return returnName;
    }

    public void setInquiryNo(int inquiryNo)
    {
        this.inquiryNo = inquiryNo;
    }

    public void setInquiryNoSub(int inquiryNoSub)
    {
        this.inquiryNoSub = inquiryNoSub;
    }

    public void setInquiryStatus(int inquiryStatus)
    {
        this.inquiryStatus = inquiryStatus;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setTermNo(String termNo)
    {
        this.termNo = termNo;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    public void setHotelAddress(String hotelAddress)
    {
        this.hotelAddress = hotelAddress;
    }

    public void setInquiryKind(String inquiryKind)
    {
        this.inquiryKind = inquiryKind;
    }

    public void setInquiry(String inquiry)
    {
        this.inquiry = inquiry;
    }

    public void setInquiryDate(int inquiryDate)
    {
        this.inquiryDate = inquiryDate;
    }

    public void setInquiryTime(int inquiryTime)
    {
        this.inquiryTime = inquiryTime;
    }

    public void setInquiryIp(String inquiryIp)
    {
        this.inquiryIp = inquiryIp;
    }

    public void setInquiryUseragent(String inquiryUseragent)
    {
        this.inquiryUseragent = inquiryUseragent;
    }

    public void setReturnInquiry(String returnInquiry)
    {
        this.returnInquiry = returnInquiry;
    }

    public void setReturnDate(int returnDate)
    {
        this.returnDate = returnDate;
    }

    public void setReturnTime(int returnTime)
    {
        this.returnTime = returnTime;
    }

    public void setReturnIp(String returnIp)
    {
        this.returnIp = returnIp;
    }

    public void setReturnUseragent(String returnUseragent)
    {
        this.returnUseragent = returnUseragent;
    }

    public void setReturnName(String returnName)
    {
        this.returnName = returnName;
    }

    /**
     * 問い合わせデータ取得
     * 
     * @param inquiryNo 問い合わせ番号
     * @param inquiryNoSub 問い合わせ番号取得
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int inquiryNo, int inquiryNoSub)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_inquiry_pay WHERE inquiry_no = ? AND inquiry_no_sub = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, inquiryNo );
            prestate.setInt( 2, inquiryNoSub );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.inquiryNo = result.getInt( "inquiry_no" );
                    this.inquiryNoSub = result.getInt( "inquiry_no_sub" );
                    this.userId = result.getString( "user_id" );
                    this.userName = result.getString( "user_name" );
                    this.termNo = result.getString( "termno" );
                    this.mailAddr = result.getString( "mailaddr" );
                    this.hotelName = result.getString( "hotel_name" );
                    this.hotelAddress = result.getString( "hotel_address" );
                    this.inquiryKind = result.getString( "inquiry_kind" );
                    this.inquiry = result.getString( "inquiry" );
                    this.inquiryDate = result.getInt( "inquiry_date" );
                    this.inquiryTime = result.getInt( "inquiry_time" );
                    this.inquiryIp = result.getString( "inquiry_ip" );
                    this.inquiryUseragent = result.getString( "inquiry_useragent" );
                    this.returnInquiry = result.getString( "return_inquiry" );
                    this.returnDate = result.getInt( "return_date" );
                    this.returnTime = result.getInt( "return_time" );
                    this.returnIp = result.getString( "return_ip" );
                    this.returnUseragent = result.getString( "return_useragent" );
                    this.returnName = result.getString( "return_name" );

                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInquiryPay.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 問い合わせデータ取得
     * 
     * @param result ホテル設定情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.inquiryNo = result.getInt( "inquiry_no" );
                this.inquiryNoSub = result.getInt( "inquiry_no_sub" );
                this.userId = result.getString( "user_id" );
                this.userName = result.getString( "user_name" );
                this.termNo = result.getString( "termno" );
                this.mailAddr = result.getString( "mailaddr" );
                this.hotelName = result.getString( "hotel_name" );
                this.hotelAddress = result.getString( "hotel_address" );
                this.inquiryKind = result.getString( "inquiry_kind" );
                this.inquiry = result.getString( "inquiry" );
                this.inquiryDate = result.getInt( "inquiry_date" );
                this.inquiryTime = result.getInt( "inquiry_time" );
                this.inquiryIp = result.getString( "inquiry_ip" );
                this.inquiryUseragent = result.getString( "inquiry_useragent" );
                this.returnInquiry = result.getString( "return_inquiry" );
                this.returnDate = result.getInt( "return_date" );
                this.returnTime = result.getInt( "return_time" );
                this.returnIp = result.getString( "return_ip" );
                this.returnUseragent = result.getString( "return_useragent" );
                this.returnName = result.getString( "return_name" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInquiryPay.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * 問い合わせデータ追加
     * 
     * @see "値のセット後(setXXX)に行うこと<br>inquiry_noをオートインクリメントで挿入"
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
        query = "INSERT hh_user_inquiry_pay SET ";
        query = query + " inquiry_no = 0,";
        query = query + " inquiry_no_sub = ?,";
        query = query + " inquiry_status = ?,";
        query = query + " user_id = ?,";
        query = query + " user_name = ?,";
        query = query + " termno = ?,";
        query = query + " mailaddr = ?,";
        query = query + " hotel_name = ?,";
        query = query + " hotel_address = ?,";
        query = query + " inquiry_kind = ?,";
        query = query + " inquiry = ?,";
        query = query + " inquiry_date = ?,";
        query = query + " inquiry_time = ?,";
        query = query + " inquiry_ip = ?,";
        query = query + " inquiry_useragent = ?,";
        query = query + " return_inquiry = ?,";
        query = query + " return_date = ?,";
        query = query + " return_time = ?,";
        query = query + " return_ip = ?,";
        query = query + " return_useragent = ?,";
        query = query + " return_name = ?";

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.inquiryNoSub );
            prestate.setInt( 2, this.inquiryStatus );
            prestate.setString( 3, this.userId );
            prestate.setString( 4, this.userName );
            prestate.setString( 5, this.termNo );
            prestate.setString( 6, this.mailAddr );
            prestate.setString( 7, this.hotelName );
            prestate.setString( 8, this.hotelAddress );
            prestate.setString( 9, this.inquiryKind );
            prestate.setString( 10, this.inquiry );
            prestate.setInt( 11, this.inquiryDate );
            prestate.setInt( 12, this.inquiryTime );
            prestate.setString( 13, this.inquiryIp );
            prestate.setString( 14, this.inquiryUseragent );
            prestate.setString( 15, this.returnInquiry );
            prestate.setInt( 16, this.returnDate );
            prestate.setInt( 17, this.returnTime );
            prestate.setString( 18, this.returnIp );
            prestate.setString( 19, this.returnUseragent );
            prestate.setString( 20, this.returnName );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInquiryPay.insertData] Exception=" + e.toString() );
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
     * 問い合わせデータ追加（問い合わせ番号指定）
     * 
     * @see "値のセット後(setXXX)に行うこと<br>inquiry_noを指定して挿入"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertDataAsInquiryNo()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;
        query = "INSERT hh_user_inquiry_pay SET ";
        query = query + " inquiry_no = ?,";
        query = query + " inquiry_no_sub = ?,";
        query = query + " inquiry_status = ?,";
        query = query + " user_id = ?,";
        query = query + " user_name = ?,";
        query = query + " termno = ?,";
        query = query + " mailaddr = ?,";
        query = query + " hotel_name = ?,";
        query = query + " hotel_address = ?,";
        query = query + " inquiry_kind = ?,";
        query = query + " inquiry = ?,";
        query = query + " inquiry_date = ?,";
        query = query + " inquiry_time = ?,";
        query = query + " inquiry_ip = ?,";
        query = query + " inquiry_useragent = ?,";
        query = query + " return_inquiry = ?,";
        query = query + " return_date = ?,";
        query = query + " return_time = ?,";
        query = query + " return_ip = ?,";
        query = query + " return_useragent = ?,";
        query = query + " return_name = ?";

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.inquiryNo );
            prestate.setInt( 2, this.inquiryNoSub );
            prestate.setInt( 3, this.inquiryStatus );
            prestate.setString( 4, this.userId );
            prestate.setString( 5, this.userName );
            prestate.setString( 6, this.termNo );
            prestate.setString( 7, this.mailAddr );
            prestate.setString( 8, this.hotelName );
            prestate.setString( 9, this.hotelAddress );
            prestate.setString( 10, this.inquiryKind );
            prestate.setString( 11, this.inquiry );
            prestate.setInt( 12, this.inquiryDate );
            prestate.setInt( 13, this.inquiryTime );
            prestate.setString( 14, this.inquiryIp );
            prestate.setString( 15, this.inquiryUseragent );
            prestate.setString( 16, this.returnInquiry );
            prestate.setInt( 17, this.returnDate );
            prestate.setInt( 18, this.returnTime );
            prestate.setString( 19, this.returnIp );
            prestate.setString( 20, this.returnUseragent );
            prestate.setString( 21, this.returnName );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInquiryPay.insertDataAsInquiryNo] Exception=" + e.toString() );
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
     * 問い合わせデータ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param inquiryNo 問い合わせ番号
     * @param inquiryNoSub 問い合わせ番号取得
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int inquiryNo, int inquiryNoSub)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_user_inquiry_pay SET ";
        query = query + " inquiry_status = ?,";
        query = query + " user_id = ?,";
        query = query + " user_name = ?,";
        query = query + " termno = ?,";
        query = query + " mailaddr = ?,";
        query = query + " hotel_name = ?,";
        query = query + " hotel_address = ?,";
        query = query + " inquiry_kind = ?,";
        query = query + " inquiry = ?,";
        query = query + " inquiry_date = ?,";
        query = query + " inquiry_time = ?,";
        query = query + " inquiry_ip = ?,";
        query = query + " inquiry_useragent = ?,";
        query = query + " return_inquiry = ?,";
        query = query + " return_date = ?,";
        query = query + " return_time = ?,";
        query = query + " return_ip = ?,";
        query = query + " return_useragent = ?,";
        query = query + " return_name = ?";
        query = query + " WHERE inquiry_no = ?";
        query = query + " AND inquiry_no_sub = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.inquiryStatus );
            prestate.setString( 2, this.userId );
            prestate.setString( 3, this.userName );
            prestate.setString( 4, this.termNo );
            prestate.setString( 5, this.mailAddr );
            prestate.setString( 6, this.hotelName );
            prestate.setString( 7, this.hotelAddress );
            prestate.setString( 8, this.inquiryKind );
            prestate.setString( 9, this.inquiry );
            prestate.setInt( 10, this.inquiryDate );
            prestate.setInt( 11, this.inquiryTime );
            prestate.setString( 12, this.inquiryIp );
            prestate.setString( 13, this.inquiryUseragent );
            prestate.setString( 14, this.returnInquiry );
            prestate.setInt( 15, this.returnDate );
            prestate.setInt( 16, this.returnTime );
            prestate.setString( 17, this.returnIp );
            prestate.setString( 18, this.returnUseragent );
            prestate.setString( 19, this.returnName );
            prestate.setInt( 20, inquiryNo );
            prestate.setInt( 21, inquiryNoSub );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInquiryPay.updateData] Exception=" + e.toString() );
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
     * 問い合わせ番号枝番取得(指定した問い合わせ番号で問い合わせ番号枝番の最大のデータを取得)
     * 
     * @param inquiryNo 問い合わせ番号
     * @return 処理結果(0以上:正常,-1:異常)
     */
    public int getMaxInquiryNoSub(int inquiryNo)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = -1;

        query = "SELECT MAX( inquiry_no_sub ) FROM hh_user_inquiry_pay WHERE inquiry_no = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, inquiryNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    count = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInquiryPay.getMaxInquiryNoSub] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    /**
     * ユーザの登録状況を確認
     * 
     * @param userId ユーザID
     * @return 処理結果(0以上:正常,-1:異常)
     */
    public int getMaxInquiryNoByUserId(String userId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = -1;

        query = "SELECT MAX( inquiry_no ) FROM hh_user_inquiry_pay WHERE user_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    count = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInquiryPay.getMaxInquiryNoSub] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    /**
     * 名前とメールアドレスから問い合わせ番号検索
     * 
     * @param userName ユーザ名前
     * @param mailaddr ユーザーメールアドレス
     * @return 処理結果(0以上:正常,-1:異常)
     */
    public int getMaxInquiryNoByNameAndMail(String userName, String mailaddr)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = -1;

        query = "SELECT MAX( inquiry_no ) FROM hh_user_inquiry_pay WHERE user_name = ? AND mailaddr = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userName );
            prestate.setString( 2, mailaddr );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    count = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInquiryPay.getMaxInquiryNoByName] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }
}
