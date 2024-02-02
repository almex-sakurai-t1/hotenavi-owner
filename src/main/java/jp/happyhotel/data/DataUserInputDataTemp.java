package jp.happyhotel.data;

/*
 * @(#)DataUserInputDataTemp 1.00 2009/11/05
 * Copyright (C) ALMEX Inc. 2009
 * 有料ユーザー入力仮領域データ
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ユーザ入力仮領域データクラス
 * 
 * 
 * @author S.Tashiro
 * @version 1.00 2009/11/06
 */
public class DataUserInputDataTemp implements Serializable
{

    private static final long serialVersionUID = -1230147739350770000L;

    private String            userId;
    private String            userName;
    private String            handleName;
    private String            termNo;
    private String            mailAddr;
    private int               inquiryNo;
    private int               inquiryNoSub;
    private String            inquiryKind;
    private String            hotelName;
    private String            hotelAddress;
    private String            inquiry;
    private String            zipCode;
    private String            prefName;
    private String            address1;
    private String            address2;
    private String            tel1;
    private String            tel2;
    private int               sex;
    private int               birthday1;
    private int               birthday2;
    private int               memorialDay1;
    private int               memorialDay2;
    private int               availableDate;
    private int               useKind;
    private int               restStartTime;
    private String            memo;
    private int               registDate;
    private int               registTime;

    /**
     * データを初期化します。
     */
    public DataUserInputDataTemp()
    {
        userId = "";
        userName = "";
        handleName = "";
        termNo = "";
        mailAddr = "";
        inquiryNo = 0;
        inquiryNoSub = 0;
        inquiryKind = "";
        hotelName = "";
        hotelAddress = "";
        inquiry = "";
        zipCode = "";
        prefName = "";
        address1 = "";
        address2 = "";
        tel1 = "";
        tel2 = "";
        sex = 0;
        birthday1 = 0;
        birthday2 = 0;
        memorialDay1 = 0;
        memorialDay2 = 0;
        availableDate = 0;
        useKind = 0;
        restStartTime = 0;
        memo = "";
        registDate = 0;
        registTime = 0;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getHandleName()
    {
        return handleName;
    }

    public String getTermNo()
    {
        return termNo;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public int getInquiryNo()
    {
        return inquiryNo;
    }

    public int getInquiryNoSub()
    {
        return inquiryNoSub;
    }

    public String getInquiryKind()
    {
        return inquiryKind;
    }

    public String getHotelName()
    {
        return hotelName;
    }

    public String getHotelAddress()
    {
        return hotelAddress;
    }

    public String getInquiry()
    {
        return inquiry;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public String getPrefName()
    {
        return prefName;
    }

    public String getAddress1()
    {
        return address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public String getTel1()
    {
        return tel1;
    }

    public String getTel2()
    {
        return tel2;
    }

    public int getSex()
    {
        return sex;
    }

    public int getBirthday1()
    {
        return birthday1;
    }

    public int getBirthday2()
    {
        return birthday2;
    }

    public int getMemorialDay1()
    {
        return memorialDay1;
    }

    public int getMemorialDay2()
    {
        return memorialDay2;
    }

    /**
     * @return availableDate
     */
    public int getAvailableDate()
    {
        return availableDate;
    }

    public int getUseKind()
    {
        return useKind;
    }

    public int getRestStartTime()
    {
        return restStartTime;
    }

    public String getMemo()
    {
        return memo;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setHandleName(String handleName)
    {
        this.handleName = handleName;
    }

    public void setTermNo(String termNo)
    {
        this.termNo = termNo;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public void setInquiryNo(int inquiryNo)
    {
        this.inquiryNo = inquiryNo;
    }

    public void setInquiryNoSub(int inquiryNoSub)
    {
        this.inquiryNoSub = inquiryNoSub;
    }

    public void setInquiryKind(String inquiryKind)
    {
        this.inquiryKind = inquiryKind;
    }

    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    public void setHotelAddress(String hotelAddres)
    {
        this.hotelAddress = hotelAddres;
    }

    public void setInquiry(String inquiry)
    {
        this.inquiry = inquiry;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public void setPrefName(String prefName)
    {
        this.prefName = prefName;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public void setTel2(String tel2)
    {
        this.tel2 = tel2;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public void setBirthday1(int birthday1)
    {
        this.birthday1 = birthday1;
    }

    public void setBirthday2(int birthday2)
    {
        this.birthday2 = birthday2;
    }

    public void setMemorialDay1(int memorialDay1)
    {
        this.memorialDay1 = memorialDay1;
    }

    public void setMemorialDay2(int memorialDay2)
    {
        this.memorialDay2 = memorialDay2;
    }

    public void setAvailableDate(int availableDate)
    {
        this.availableDate = availableDate;
    }

    public void setUseKind(int useKind)
    {
        this.useKind = useKind;
    }

    public void setRestStartTime(int restStartTime)
    {
        this.restStartTime = restStartTime;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    /**
     * ユーザ入力仮領域データクラス取得
     * 
     * @param inquiryNo 問い合わせ番号
     * @param inquiryNoSub 問い合わせ番号取得
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "SELECT * FROM hh_user_input_data_temp WHERE user_id = ?";

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
                    this.userId = result.getString( "user_id" );
                    this.userName = result.getString( "user_name" );
                    this.handleName = result.getString( "handle_name" );
                    this.termNo = result.getString( "termno" );
                    this.mailAddr = result.getString( "mailaddr" );
                    this.inquiryNo = result.getInt( "inquiry_no" );
                    this.inquiryNoSub = result.getInt( "inquiry_no_sub" );
                    this.inquiryKind = result.getString( "inquiry_kind" );
                    this.hotelName = result.getString( "hotel_name" );
                    this.hotelAddress = result.getString( "hotel_address" );
                    this.inquiry = result.getString( "inquiry" );
                    this.zipCode = result.getString( "zip_code" );
                    this.prefName = result.getString( "pref_name" );
                    this.address1 = result.getString( "address1" );
                    this.address2 = result.getString( "address2" );
                    this.tel1 = result.getString( "tel1" );
                    this.tel2 = result.getString( "tel2" );
                    this.sex = result.getInt( "sex" );
                    this.birthday1 = result.getInt( "birthday1" );
                    this.birthday2 = result.getInt( "birthday2" );
                    this.memorialDay1 = result.getInt( "memorial_day1" );
                    this.memorialDay2 = result.getInt( "memorial_day2" );
                    this.availableDate = result.getInt( "available_date" );
                    this.useKind = result.getInt( "use_kind" );
                    this.restStartTime = result.getInt( "rest_start_time" );
                    this.memo = result.getString( "memo" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                }
            }
            if ( this.userId.compareTo( "" ) != 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInputDataTemp.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザ入力仮領域データクラス取得
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
                this.userId = result.getString( "user_id" );
                this.userName = result.getString( "user_name" );
                this.handleName = result.getString( "handle_name" );
                this.termNo = result.getString( "termno" );
                this.mailAddr = result.getString( "mailaddr" );
                this.inquiryNo = result.getInt( "inquiry_no" );
                this.inquiryNoSub = result.getInt( "inquiry_no_sub" );
                this.inquiryKind = result.getString( "inquiry_kind" );
                this.hotelName = result.getString( "hotel_name" );
                this.hotelAddress = result.getString( "hotel_address" );
                this.inquiry = result.getString( "inquiry" );
                this.zipCode = result.getString( "zip_code" );
                this.prefName = result.getString( "pref_name" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.tel1 = result.getString( "tel1" );
                this.tel2 = result.getString( "tel2" );
                this.sex = result.getInt( "sex" );
                this.birthday1 = result.getInt( "birthday1" );
                this.birthday2 = result.getInt( "birthday2" );
                this.memorialDay1 = result.getInt( "memorial_day1" );
                this.memorialDay2 = result.getInt( "memorial_day2" );
                this.availableDate = result.getInt( "available_date" );
                this.useKind = result.getInt( "use_kind" );
                this.restStartTime = result.getInt( "rest_start_time" );
                this.memo = result.getString( "memo" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInputDataTemp.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザ入力仮領域データクラス追加
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
        query = "INSERT hh_user_input_data_temp SET ";
        query = query + " user_id = ?,";
        query = query + " user_name = ?,";
        query = query + " handle_name = ?,";
        query = query + " termno = ?,";
        query = query + " mailaddr = ?,";
        query = query + " inquiry_no = ?,";
        query = query + " inquiry_no_sub = ?,";
        query = query + " inquiry_kind = ?,";
        query = query + " hotel_name = ?,";
        query = query + " hotel_address = ?,";
        query = query + " inquiry = ?,";
        query = query + " zip_code = ?,";
        query = query + " pref_name = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " sex = ?,";
        query = query + " birthday1 = ?,";
        query = query + " birthday2 = ?,";
        query = query + " memorial_day1 = ?,";
        query = query + " memorial_day2 = ?,";
        query = query + " use_kind = ?,";
        query = query + " rest_start_time = ?,";
        query = query + " memo = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?";
        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする

            prestate.setString( 1, this.userId );
            prestate.setString( 2, this.userName );
            prestate.setString( 3, this.handleName );
            prestate.setString( 4, this.termNo );
            prestate.setString( 5, this.mailAddr );
            prestate.setInt( 6, this.inquiryNo );
            prestate.setInt( 7, this.inquiryNoSub );
            prestate.setString( 8, this.inquiryKind );
            prestate.setString( 9, this.hotelName );
            prestate.setString( 10, this.hotelAddress );
            prestate.setString( 11, this.inquiry );
            prestate.setString( 12, this.zipCode );
            prestate.setString( 13, this.prefName );
            prestate.setString( 14, this.address1 );
            prestate.setString( 15, this.address2 );
            prestate.setString( 16, this.tel1 );
            prestate.setString( 17, this.tel2 );
            prestate.setInt( 18, this.sex );
            prestate.setInt( 19, this.birthday1 );
            prestate.setInt( 20, this.birthday2 );
            prestate.setInt( 21, this.memorialDay1 );
            prestate.setInt( 22, this.memorialDay2 );
            prestate.setInt( 23, this.useKind );
            prestate.setInt( 24, this.restStartTime );
            prestate.setString( 25, this.memo );
            prestate.setInt( 26, this.registDate );
            prestate.setInt( 27, this.registTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInputDataTemp.insertData] Exception=" + e.toString() );
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
     * ユーザ入力仮領域データクラス更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_input_data_temp SET ";
        query = query + " user_name = ?,";
        query = query + " handle_name = ?,";
        query = query + " termno = ?,";
        query = query + " mailaddr = ?,";
        query = query + " inquiry_no = ?,";
        query = query + " inquiry_no_sub = ?,";
        query = query + " inquiry_kind = ?,";
        query = query + " hotel_name = ?,";
        query = query + " hotel_address = ?,";
        query = query + " inquiry = ?,";
        query = query + " zip_code = ?,";
        query = query + " pref_name = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " sex = ?,";
        query = query + " birthday1 = ?,";
        query = query + " birthday2 = ?,";
        query = query + " memorial_day1 = ?,";
        query = query + " memorial_day2 = ?,";
        query = query + " use_kind = ?,";
        query = query + " rest_start_time = ?,";
        query = query + " memo = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?";
        query = query + " WHERE user_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.userName );
            prestate.setString( 2, this.handleName );
            prestate.setString( 3, this.termNo );
            prestate.setString( 4, this.mailAddr );
            prestate.setInt( 5, this.inquiryNo );
            prestate.setInt( 6, this.inquiryNoSub );
            prestate.setString( 7, this.inquiryKind );
            prestate.setString( 8, this.hotelName );
            prestate.setString( 9, this.hotelAddress );
            prestate.setString( 10, this.inquiry );
            prestate.setString( 11, this.zipCode );
            prestate.setString( 12, this.prefName );
            prestate.setString( 13, this.address1 );
            prestate.setString( 14, this.address2 );
            prestate.setString( 15, this.tel1 );
            prestate.setString( 16, this.tel2 );
            prestate.setInt( 17, this.sex );
            prestate.setInt( 18, this.birthday1 );
            prestate.setInt( 19, this.birthday2 );
            prestate.setInt( 20, this.memorialDay1 );
            prestate.setInt( 21, this.memorialDay2 );
            prestate.setInt( 22, this.useKind );
            prestate.setInt( 23, this.restStartTime );
            prestate.setString( 24, this.memo );
            prestate.setInt( 25, this.registDate );
            prestate.setInt( 26, this.registTime );
            prestate.setString( 27, userId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInputDataTemp.updateData] Exception=" + e.toString() );
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
