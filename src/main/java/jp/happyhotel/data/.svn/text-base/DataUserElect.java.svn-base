/*
 * @(#)DataUserElect.java 1.00 2008/02/20 Copyright (C) ALMEX Inc. 2008 ユーザ当選情報取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ユーザ当選情報取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/02/20
 */
public class DataUserElect implements Serializable
{
    private static final long serialVersionUID = -2858905569386702755L;

    private String            userId;
    private int               seq;
    private int               applicationCount;
    private String            userName;
    private String            zipCode;
    private String            prefName;
    private String            address1;
    private String            address2;
    private String            tel1;
    private String            tel2;
    private String            memo;
    private int               emitDate;
    private int               emitTime;
    private int               refDate;
    private int               refTime;
    private int               inputDate;
    private int               inputTime;
    private int               statusFlag;
    private String            handleName;
    private int               sex;
    private String            mailAddr;
    private int               birthday1;
    private int               birthday2;
    private int               memorialDay1;
    private int               memorialDay2;
    private int               availableDate;
    private int               useKind;
    private int               restStartTime;
    private String            favoritePref;
    private String            favoriteCity;
    private String            favoriteHotel;
    private String            roomEquip;
    private String            age;
    private String            occupation;

    /**
     * データを初期化します。
     */
    public DataUserElect()
    {
        userId = "";
        seq = 0;
        applicationCount = 0;
        userName = "";
        zipCode = "";
        prefName = "";
        address1 = "";
        address2 = "";
        tel1 = "";
        tel2 = "";
        memo = "";
        emitDate = 0;
        emitTime = 0;
        refDate = 0;
        refTime = 0;
        inputDate = 0;
        inputTime = 0;
        statusFlag = 0;
        handleName = "";
        sex = 0;
        mailAddr = "";
        birthday1 = 0;
        birthday2 = 0;
        memorialDay1 = 0;
        memorialDay2 = 0;
        availableDate = 0;
        useKind = 0;
        restStartTime = 0;
        favoritePref = "";
        favoriteCity = "";
        favoriteHotel = "";
        roomEquip = "";
        age = "";
        occupation = "";

    }

    /* ▼2012/07/03追加▼ */
    public String getRoomEquip()
    {
        return roomEquip;
    }

    public void setRoomEquip(String roomEquip)
    {
        this.roomEquip = roomEquip;
    }

    public String getAge()
    {
        return age;
    }

    public void setAge(String age)
    {
        this.age = age;
    }

    public String getOccupation()
    {
        return occupation;
    }

    public void setOccupation(String occupation)
    {
        this.occupation = occupation;
    }

    /* ▲2012/07/03追加▲ */

    /* ▼ポイントde試泊対応▼ */
    public String getHandleName()
    {
        return handleName;
    }

    public void setHandleName(String handleName)
    {
        this.handleName = handleName;
    }

    public int getSex()
    {
        return sex;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public int getBirthday1()
    {
        return birthday1;
    }

    public void setBirthday1(int birthday1)
    {
        this.birthday1 = birthday1;
    }

    public int getBirthday2()
    {
        return birthday2;
    }

    public void setBirthday2(int birthday2)
    {
        this.birthday2 = birthday2;
    }

    public int getMemorialDay1()
    {
        return memorialDay1;
    }

    public void setMemorialDay1(int memorialDay1)
    {
        this.memorialDay1 = memorialDay1;
    }

    public int getMemorialDay2()
    {
        return memorialDay2;
    }

    public void setMemorialDay2(int memorialday2)
    {
        this.memorialDay2 = memorialday2;
    }

    public int getAvailableDate()
    {
        return availableDate;
    }

    public void setAvailableDate(int availableDate)
    {
        this.availableDate = availableDate;
    }

    public int getUseKind()
    {
        return useKind;
    }

    public void setUseKind(int useKind)
    {
        this.useKind = useKind;
    }

    public int getRestStartTime()
    {
        return restStartTime;
    }

    public void setRestStartTime(int restStartTime)
    {
        this.restStartTime = restStartTime;
    }

    public String getFavoritePref()
    {
        return favoritePref;
    }

    public void setFavoritePref(String favoritePref)
    {
        this.favoritePref = favoritePref;
    }

    public String getFavoriteCity()
    {
        return favoriteCity;
    }

    public void setFavoriteCity(String favoriteCity)
    {
        this.favoriteCity = favoriteCity;
    }

    public String getFavoriteHotel()
    {
        return favoriteHotel;
    }

    public void setFavoriteHotel(String favoriteHotel)
    {
        this.favoriteHotel = favoriteHotel;
    }

    /* ▲ポイントde試泊対応▲ */

    public String getAddress1()
    {
        return address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public int getApplicationCount()
    {
        return applicationCount;
    }

    public int getEmitDate()
    {
        return emitDate;
    }

    public int getEmitTime()
    {
        return emitTime;
    }

    public int getInputDate()
    {
        return inputDate;
    }

    public int getInputTime()
    {
        return inputTime;
    }

    public String getMemo()
    {
        return memo;
    }

    public String getPrefName()
    {
        return prefName;
    }

    public int getRefDate()
    {
        return refDate;
    }

    public int getRefTime()
    {
        return refTime;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getStatusFlag()
    {
        return statusFlag;
    }

    public String getTel1()
    {
        return tel1;
    }

    public String getTel2()
    {
        return tel2;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public void setApplicationCount(int applicationCount)
    {
        this.applicationCount = applicationCount;
    }

    public void setEmitDate(int emitDate)
    {
        this.emitDate = emitDate;
    }

    public void setEmitTime(int emitTime)
    {
        this.emitTime = emitTime;
    }

    public void setInputDate(int inputDate)
    {
        this.inputDate = inputDate;
    }

    public void setInputTime(int inputTime)
    {
        this.inputTime = inputTime;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public void setPrefName(String prefName)
    {
        this.prefName = prefName;
    }

    public void setRefDate(int refDate)
    {
        this.refDate = refDate;
    }

    public void setRefTime(int refTime)
    {
        this.refTime = refTime;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setStatusFlag(int statusFlag)
    {
        this.statusFlag = statusFlag;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public void setTel2(String tel2)
    {
        this.tel2 = tel2;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    /**
     * ユーザ当選データ取得
     * 
     * @param userId ユーザID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int seq, int applicationCount)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = " SELECT * FROM hh_user_elect WHERE user_id = ? AND seq = ? ";
        query = query + " AND application_count = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, seq );
            prestate.setInt( 3, applicationCount );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.seq = result.getInt( "seq" );
                    this.applicationCount = result.getInt( "application_count" );
                    this.userName = result.getString( "user_name" );
                    this.prefName = result.getString( "pref_name" );
                    this.zipCode = result.getString( "zip_code" );
                    this.address1 = result.getString( "address1" );
                    this.address2 = result.getString( "address2" );
                    this.tel1 = result.getString( "tel1" );
                    this.tel2 = result.getString( "tel2" );
                    this.memo = result.getString( "memo" );
                    this.emitDate = result.getInt( "emit_date" );
                    this.emitTime = result.getInt( "emit_time" );
                    this.refDate = result.getInt( "ref_date" );
                    this.refTime = result.getInt( "ref_time" );
                    this.inputDate = result.getInt( "input_date" );
                    this.inputTime = result.getInt( "input_time" );
                    this.statusFlag = result.getInt( "status_flag" );
                    this.handleName = result.getString( "handle_name" );
                    this.sex = result.getInt( "sex" );
                    this.mailAddr = result.getString( "mail_addr" );
                    this.birthday1 = result.getInt( "birthday1" );
                    this.birthday2 = result.getInt( "birthday2" );
                    this.memorialDay1 = result.getInt( "memorial_day1" );
                    this.memorialDay2 = result.getInt( "memorial_day2" );
                    this.availableDate = result.getInt( "available_date" );
                    this.useKind = result.getInt( "use_kind" );
                    this.restStartTime = result.getInt( "rest_start_time" );
                    this.favoritePref = result.getString( "favorite_pref" );
                    this.favoriteCity = result.getString( "favorite_city" );
                    this.favoriteHotel = result.getString( "favorite_hotel" );
                    this.roomEquip = result.getString( "room_equip" );
                    this.age = result.getString( "age" );
                    this.occupation = result.getString( "occupation" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserElect.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * ユーザ当選データ設定
     * 
     * @param result ユーザ当選データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.userId = result.getString( "user_id" );
                this.seq = result.getInt( "seq" );
                this.applicationCount = result.getInt( "application_count" );
                this.userName = result.getString( "user_name" );
                this.prefName = result.getString( "pref_name" );
                this.zipCode = result.getString( "zip_code" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.tel1 = result.getString( "tel1" );
                this.tel2 = result.getString( "tel2" );
                this.memo = result.getString( "memo" );
                this.emitDate = result.getInt( "emit_date" );
                this.emitTime = result.getInt( "emit_time" );
                this.refDate = result.getInt( "ref_date" );
                this.refTime = result.getInt( "ref_time" );
                this.inputDate = result.getInt( "input_date" );
                this.inputTime = result.getInt( "input_time" );
                this.statusFlag = result.getInt( "status_flag" );
                this.handleName = result.getString( "handle_name" );
                this.sex = result.getInt( "sex" );
                this.mailAddr = result.getString( "mail_addr" );
                this.birthday1 = result.getInt( "birthday1" );
                this.birthday2 = result.getInt( "birthday2" );
                this.memorialDay1 = result.getInt( "memorial_day1" );
                this.memorialDay2 = result.getInt( "memorial_day2" );
                this.availableDate = result.getInt( "available_date" );
                this.useKind = result.getInt( "use_kind" );
                this.restStartTime = result.getInt( "rest_start_time" );
                this.favoritePref = result.getString( "favorite_pref" );
                this.favoriteCity = result.getString( "favorite_city" );
                this.favoriteHotel = result.getString( "favorite_hotel" );
                this.roomEquip = result.getString( "room_equip" );
                this.age = result.getString( "age" );
                this.occupation = result.getString( "occupation" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserElect.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザ当選情報データ追加
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

        query = "INSERT hh_user_elect SET ";
        query = query + " user_id = ?,";
        query = query + " seq = ?,";
        query = query + " application_count = 0,";
        query = query + " user_name = ?,";
        query = query + " pref_name = ?,";
        query = query + " zip_code = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " memo = ?,";
        query = query + " emit_date = ?,";
        query = query + " emit_time = ?,";
        query = query + " ref_date = ?,";
        query = query + " ref_time = ?,";
        query = query + " input_date = ?,";
        query = query + " input_time = ?,";
        query = query + " status_flag = ?,";
        query = query + " handle_name = ?,";
        query = query + " sex = ?,";
        query = query + " mail_addr = ?,";
        query = query + " birthday1 = ?,";
        query = query + " birthday2 = ?,";
        query = query + " memorial_day1 = ?,";
        query = query + " memorial_day2 = ?,";
        query = query + " available_date = ?,";
        query = query + " use_kind = ?,";
        query = query + " rest_start_time = ?,";
        query = query + " favorite_pref = ?,";
        query = query + " favorite_city = ?,";
        query = query + " favorite_hotel = ?,";
        query = query + " room_equip = ?,";
        query = query + " age = ?,";
        query = query + " occupation = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, this.seq );
            prestate.setString( 3, this.userName );
            prestate.setString( 4, this.prefName );
            prestate.setString( 5, this.zipCode );
            prestate.setString( 6, this.address1 );
            prestate.setString( 7, this.address2 );
            prestate.setString( 8, this.tel1 );
            prestate.setString( 9, this.tel2 );
            prestate.setString( 10, this.memo );
            prestate.setInt( 11, this.emitDate );
            prestate.setInt( 12, this.emitTime );
            prestate.setInt( 13, this.refDate );
            prestate.setInt( 14, this.refTime );
            prestate.setInt( 15, this.inputDate );
            prestate.setInt( 16, this.inputTime );
            prestate.setInt( 17, this.statusFlag );
            prestate.setString( 18, this.handleName );
            prestate.setInt( 19, this.sex );
            prestate.setString( 20, this.mailAddr );
            prestate.setInt( 21, this.birthday1 );
            prestate.setInt( 22, this.birthday2 );
            prestate.setInt( 23, this.memorialDay1 );
            prestate.setInt( 24, this.memorialDay2 );
            prestate.setInt( 25, this.availableDate );
            prestate.setInt( 26, this.useKind );
            prestate.setInt( 27, this.restStartTime );
            prestate.setString( 28, this.favoritePref );
            prestate.setString( 29, this.favoriteCity );
            prestate.setString( 30, this.favoriteHotel );
            prestate.setString( 31, this.roomEquip );
            prestate.setString( 32, this.age );
            prestate.setString( 33, this.occupation );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserElect.insertData] Exception=" + e.toString() );
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
     * ユーザ当選情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertDataOnce()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_user_elect SET ";
        query = query + " user_id = ?,";
        query = query + " seq = ?,";
        query = query + " application_count = ?,";
        query = query + " user_name = ?,";
        query = query + " pref_name = ?,";
        query = query + " zip_code = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " memo = ?,";
        query = query + " emit_date = ?,";
        query = query + " emit_time = ?,";
        query = query + " ref_date = ?,";
        query = query + " ref_time = ?,";
        query = query + " input_date = ?,";
        query = query + " input_time = ?,";
        query = query + " status_flag = ?,";
        query = query + " handle_name = ?,";
        query = query + " sex = ?,";
        query = query + " mail_addr = ?,";
        query = query + " birthday1 = ?,";
        query = query + " birthday2 = ?,";
        query = query + " memorial_day1 = ?,";
        query = query + " memorial_day2 = ?,";
        query = query + " available_date = ?,";
        query = query + " use_kind = ?,";
        query = query + " rest_start_time = ?,";
        query = query + " favorite_pref = ?,";
        query = query + " favorite_city = ?,";
        query = query + " favorite_hotel = ?,";
        query = query + " room_equip = ?,";
        query = query + " age = ?,";
        query = query + " occupation = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, this.seq );
            prestate.setInt( 3, this.applicationCount );
            prestate.setString( 4, this.userName );
            prestate.setString( 5, this.prefName );
            prestate.setString( 6, this.zipCode );
            prestate.setString( 7, this.address1 );
            prestate.setString( 8, this.address2 );
            prestate.setString( 9, this.tel1 );
            prestate.setString( 10, this.tel2 );
            prestate.setString( 11, this.memo );
            prestate.setInt( 12, this.emitDate );
            prestate.setInt( 13, this.emitTime );
            prestate.setInt( 14, this.refDate );
            prestate.setInt( 15, this.refTime );
            prestate.setInt( 16, this.inputDate );
            prestate.setInt( 17, this.inputTime );
            prestate.setInt( 18, this.statusFlag );
            prestate.setString( 19, this.handleName );
            prestate.setInt( 20, this.sex );
            prestate.setString( 21, this.mailAddr );
            prestate.setInt( 22, this.birthday1 );
            prestate.setInt( 23, this.birthday2 );
            prestate.setInt( 24, this.memorialDay1 );
            prestate.setInt( 25, this.memorialDay2 );
            prestate.setInt( 26, this.availableDate );
            prestate.setInt( 27, this.useKind );
            prestate.setInt( 28, this.restStartTime );
            prestate.setString( 29, this.favoritePref );
            prestate.setString( 30, this.favoriteCity );
            prestate.setString( 31, this.favoriteHotel );
            prestate.setString( 32, this.roomEquip );
            prestate.setString( 33, this.age );
            prestate.setString( 34, this.occupation );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserElect.insertDataOnce] Exception=" + e.toString() );
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
     * ユーザ当選情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(int applicationCount)
    {
        int i;
        int count;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        i = 0;
        ret = false;

        if ( applicationCount < 1 )
        {
            return(false);
        }
        query = "INSERT hh_user_elect SET ";
        query = query + " user_id = ?,";
        query = query + " seq = ?,";
        query = query + " application_count = 0,";
        query = query + " user_name = ?,";
        query = query + " pref_name = ?,";
        query = query + " zip_code = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " memo = ?,";
        query = query + " emit_date = ?,";
        query = query + " emit_time = ?,";
        query = query + " ref_date = ?,";
        query = query + " ref_time = ?,";
        query = query + " input_date = ?,";
        query = query + " input_time = ?,";
        query = query + " status_flag = ?,";
        query = query + " handle_name = ?,";
        query = query + " sex = ?,";
        query = query + " mail_addr = ?,";
        query = query + " birthday1 = ?,";
        query = query + " birthday2 = ?,";
        query = query + " memorial_day1 = ?,";
        query = query + " memorial_day2 = ?,";
        query = query + " available_date = ?,";
        query = query + " use_kind = ?,";
        query = query + " rest_start_time = ?,";
        query = query + " favorite_pref = ?,";
        query = query + " favorite_city = ?,";
        query = query + " favorite_hotel = ?,";
        query = query + " room_equip = ?,";
        query = query + " age = ?,";
        query = query + " occupation = ?";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            for( i = 0 ; i < applicationCount ; i++ )
            {
                // 更新対象の値をセットする
                prestate.setString( 1, this.userId );
                prestate.setInt( 2, this.seq );
                prestate.setString( 3, this.userName );
                prestate.setString( 4, this.prefName );
                prestate.setString( 5, this.zipCode );
                prestate.setString( 6, this.address1 );
                prestate.setString( 7, this.address2 );
                prestate.setString( 8, this.tel1 );
                prestate.setString( 9, this.tel2 );
                prestate.setString( 10, this.memo );
                prestate.setInt( 11, this.emitDate );
                prestate.setInt( 12, this.emitTime );
                prestate.setInt( 13, this.refDate );
                prestate.setInt( 14, this.refTime );
                prestate.setInt( 15, this.inputDate );
                prestate.setInt( 16, this.inputTime );
                prestate.setInt( 17, this.statusFlag );
                prestate.setString( 18, this.handleName );
                prestate.setInt( 19, this.sex );
                prestate.setString( 20, this.mailAddr );
                prestate.setInt( 21, this.birthday1 );
                prestate.setInt( 22, this.birthday2 );
                prestate.setInt( 23, this.memorialDay1 );
                prestate.setInt( 24, this.memorialDay2 );
                prestate.setInt( 25, this.availableDate );
                prestate.setInt( 26, this.useKind );
                prestate.setInt( 27, this.restStartTime );
                prestate.setString( 28, this.favoritePref );
                prestate.setString( 29, this.favoriteCity );
                prestate.setString( 30, this.favoriteHotel );
                prestate.setString( 31, this.roomEquip );
                prestate.setString( 32, this.age );
                prestate.setString( 33, this.occupation );

                result = prestate.executeUpdate();
                if ( result <= 0 )
                {
                    count++;
                }
            }
            if ( count == 0 )
                ret = true;
            else
                ret = false;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserElect.insertData] Exception=" + e.toString() );
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
     * ユーザ当選データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId, int seq, int applicataionCount)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_elect SET ";
        query = query + " user_name = ?,";
        query = query + " zip_code = ?,";
        query = query + " pref_name = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " memo = ?,";
        query = query + " emit_date = ?,";
        query = query + " emit_time = ?,";
        query = query + " ref_date = ?,";
        query = query + " ref_time= ?,";
        query = query + " input_date = ?,";
        query = query + " input_time = ?,";
        query = query + " status_flag = ?,";
        query = query + " handle_name = ?,";
        query = query + " sex = ?,";
        query = query + " mail_addr = ?,";
        query = query + " birthday1 = ?,";
        query = query + " birthday2 = ?,";
        query = query + " memorial_day1 = ?,";
        query = query + " memorial_day2 = ?,";
        query = query + " available_date = ?,";
        query = query + " use_kind = ?,";
        query = query + " rest_start_time = ?,";
        query = query + " favorite_pref = ?,";
        query = query + " favorite_city = ?,";
        query = query + " favorite_hotel = ?,";
        query = query + " room_equip = ?,";
        query = query + " age = ?,";
        query = query + " occupation = ?";
        query = query + " WHERE user_id = ?";
        query = query + " AND seq = ?";
        query = query + " AND application_count = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.userName );
            prestate.setString( 2, this.zipCode );
            prestate.setString( 3, this.prefName );
            prestate.setString( 4, this.address1 );
            prestate.setString( 5, this.address2 );
            prestate.setString( 6, this.tel1 );
            prestate.setString( 7, this.tel2 );
            prestate.setString( 8, this.memo );
            prestate.setInt( 9, this.emitDate );
            prestate.setInt( 10, this.emitTime );
            prestate.setInt( 11, this.refDate );
            prestate.setInt( 12, this.refTime );
            prestate.setInt( 13, this.inputDate );
            prestate.setInt( 14, this.inputTime );
            prestate.setInt( 15, this.statusFlag );
            prestate.setString( 16, this.handleName );
            prestate.setInt( 17, this.sex );
            prestate.setString( 18, this.mailAddr );
            prestate.setInt( 19, this.birthday1 );
            prestate.setInt( 20, this.birthday2 );
            prestate.setInt( 21, this.memorialDay1 );
            prestate.setInt( 22, this.memorialDay2 );
            prestate.setInt( 23, this.availableDate );
            prestate.setInt( 24, this.useKind );
            prestate.setInt( 25, this.restStartTime );
            prestate.setString( 26, this.favoritePref );
            prestate.setString( 27, this.favoriteCity );
            prestate.setString( 28, this.favoriteHotel );
            prestate.setString( 29, this.roomEquip );
            prestate.setString( 30, this.age );
            prestate.setString( 31, this.occupation );

            prestate.setString( 32, userId );
            prestate.setInt( 33, seq );
            prestate.setInt( 34, applicataionCount );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserBasic.updateData] Exception=" + e.toString() );
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
     * ユーザ当選データ存在確認
     * 
     * @param userId ユーザID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean isData(String userId, int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = " SELECT * FROM hh_user_elect WHERE user_id = ? AND seq = ? LIMIT 0,1 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserElect.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }
}
