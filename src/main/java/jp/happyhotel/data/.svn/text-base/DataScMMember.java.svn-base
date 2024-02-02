/*
 * @(#)DataMMember.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 メンバーマスター情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * メンバーマスター情報データ取得クラス
 * 
 * @author Mitsuhashi-k1
 * @version 1.00 2019/06/25
 */
public class DataScMMember implements Serializable
{
    private static final long serialVersionUID = -4585645439526206154L;

    /**
     * メンバー番号。
     */
    private int               memberNo;

    /**
     * 契約先ID。
     */
    private int               accountId;

    /**
     * メンバーID（メールアドレス）。
     */
    private String            memberId;

    /**
     * 契約先ID、メンバーIDをもとに暗号化したkey。
     */
    private String            md5;

    /**
     * パスワード。
     */
    private String            password;

    /**
     * ステータス。
     */
    private int               status;

    /**
     * 仮登録日付（YYYYMMDD)。
     */
    private int               tempDate;

    /**
     * 仮登録時刻(HHMMSS)。
     */
    private int               tempTime;

    /**
     * 登録日付（YYYYMMDD)。
     */
    private int               registDate;

    /**
     * 登録時刻（HHMMSS)。
     */
    private int               registTime;

    /**
     * 変更日付（YYYYMMDD)。
     */
    private int               updateDate;

    /**
     * 変更時刻（HHMMSS)。
     */
    private int               updateTime;

    /**
     * 1:削除。
     */
    private int               delFlag;

    /**
     * ニックネーム（ホスト登録用）。
     */
    private String            nickname;

    /**
     * 誕生日1（ホスト登録）。
     */
    private int               birthday1;

    /**
     * 誕生日2（ホスト登録）。
     */
    private int               birthday2;

    /**
     * 記念日1（ホスト登録）。
     */
    private int               memorial1;

    /**
     * 記念日2（ホスト登録）。
     */
    private int               memorial2;

    /**
     * 1:男性。
     */
    private int               sex;

    /**
     * 漢字氏名。
     */
    private String            name;

    /**
     * カナ氏名。
     */
    private String            nameKana;

    /**
     * 都道府県＋市区町村。
     */
    private String            address1;

    /**
     * その他住所。
     */
    private String            address2;

    /**
     * 電話番号。
     */
    private String            tel1;

    /**
     * パスワード変更日付（YYYYMMDD)。
     */
    private int               chgpassDate;

    /**
     * パスワード変更時刻（HHMMSS)。
     */
    private int               chgpassTime;

    /**
     * ハピホテユーザID
     */
    private String            hhUserId;

    /**
     * ハピホテユーザパスワード
     */
    private String            hhUserPassword;

    /**
     * ワンタイムキー。
     */
    private String            onetimeKey;

    /**
     * データを初期化します。
     */
    public DataScMMember()
    {
        memberNo = 0;
        accountId = 0;
        memberId = "";
        md5 = "";
        password = "";
        status = 0;
        tempDate = 0;
        tempTime = 0;
        registDate = 0;
        registTime = 0;
        updateDate = 0;
        updateTime = 0;
        delFlag = 0;
        nickname = "";
        birthday1 = 0;
        birthday2 = 0;
        memorial1 = 0;
        memorial2 = 0;
        sex = 0;
        name = "";
        nameKana = "";
        address1 = "";
        address2 = "";
        tel1 = "";
        chgpassDate = 0;
        chgpassTime = 0;
        onetimeKey = "";

    }

    public int getMemberNo()
    {
        return memberNo;
    }

    public void setMemberNo(int memberNo)
    {
        this.memberNo = memberNo;
    }

    public int getAccountId()
    {
        return accountId;
    }

    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }

    public String getMemberId()
    {
        return memberId;
    }

    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }

    public String getMd5()
    {
        return md5;
    }

    public void setMd5(String md5)
    {
        this.md5 = md5;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getTempDate()
    {
        return tempDate;
    }

    public void setTempDate(int tempDate)
    {
        this.tempDate = tempDate;
    }

    public int getTempTime()
    {
        return tempTime;
    }

    public void setTempTime(int tempTime)
    {
        this.tempTime = tempTime;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public int getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate(int updateDate)
    {
        this.updateDate = updateDate;
    }

    public int getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(int updateTime)
    {
        this.updateTime = updateTime;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
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

    public int getMemorial1()
    {
        return memorial1;
    }

    public void setMemorial1(int memorial1)
    {
        this.memorial1 = memorial1;
    }

    public int getMemorial2()
    {
        return memorial2;
    }

    public void setMemorial2(int memorial2)
    {
        this.memorial2 = memorial2;
    }

    public int getSex()
    {
        return sex;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public String getAddress1()
    {
        return address1;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public String getTel1()
    {
        return tel1;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public int getChgpassDate()
    {
        return chgpassDate;
    }

    public void setChgpassDate(int chgpassDate)
    {
        this.chgpassDate = chgpassDate;
    }

    public int getChgpassTime()
    {
        return chgpassTime;
    }

    public void setChgpassTime(int chgpassTime)
    {
        this.chgpassTime = chgpassTime;
    }

    public String getHhUserId()
    {
        return hhUserId;
    }

    public void setHhUserId(String hhUserId)
    {
        this.hhUserId = hhUserId;
    }

    public String getHhUserPassword()
    {
        return hhUserPassword;
    }

    public void setHhUserPassword(String hhUserPassword)
    {
        this.hhUserPassword = hhUserPassword;
    }

    public String getOnetimeKey()
    {
        return onetimeKey;
    }

    public void setOnetimeKey(String onetimeKey)
    {
        this.onetimeKey = onetimeKey;
    }

    /**
     * メンバーマスター情報データ取得
     * 
     * @param memberNo メンバー番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int memberNo)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM sc.m_member WHERE member_no = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, memberNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMMember.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * メンバーマスター情報データ取得
     * 
     * @param accountId 契約先ID
     * @param memberId メンバーID（メールアドレス）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int accountId, String memberId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM sc.m_member WHERE account_id = ? AND member_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accountId );
            prestate.setString( 2, memberId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMMember.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * メンバーマスター情報データ取得
     * 
     * @param md5 契約先ID、メンバーIDをもとに暗号化したkey
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String md5)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM sc.m_member WHERE md5 = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, md5 );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMMember.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * メンバーマスター情報データ設定
     * 
     * @param result メンバーマスター情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.memberNo = result.getInt( "member_no" );
                this.accountId = result.getInt( "account_id" );
                this.memberId = result.getString( "member_id" );
                this.md5 = result.getString( "md5" );
                this.password = result.getString( "password" );
                this.status = result.getInt( "status" );
                this.tempDate = result.getInt( "temp_date" );
                this.tempTime = result.getInt( "temp_time" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.updateDate = result.getInt( "update_date" );
                this.updateTime = result.getInt( "update_time" );
                this.delFlag = result.getInt( "del_flag" );
                this.nickname = result.getString( "nickname" );
                this.birthday1 = result.getInt( "birthday1" );
                this.birthday2 = result.getInt( "birthday2" );
                this.memorial1 = result.getInt( "memorial1" );
                this.memorial2 = result.getInt( "memorial2" );
                this.sex = result.getInt( "sex" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.tel1 = result.getString( "tel1" );
                this.chgpassDate = result.getInt( "chgpass_date" );
                this.chgpassTime = result.getInt( "chgpass_time" );
                this.onetimeKey = result.getString( "onetime_key" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMMember.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * メンバーマスター情報データ追加
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
        DataScMMemberHistory dsmh;
        int insertMemberNo = 0;

        ret = false;

        query = "INSERT sc.m_member SET ";
        query = query + "account_id      =   ?,";
        query = query + "member_id       =   ?,";
        query = query + "md5             =   ?,";
        query = query + "password        =   ?,";
        query = query + "status          =   ?,";
        query = query + "temp_date       =   ?,";
        query = query + "temp_time       =   ?,";
        query = query + "regist_date     =   ?,";
        query = query + "regist_time     =   ?,";
        query = query + "update_date     =   ?,";
        query = query + "update_time     =   ?,";
        query = query + "del_flag        =   ?,";
        query = query + "nickname        =   ?,";
        query = query + "birthday1       =   ?,";
        query = query + "birthday2       =   ?,";
        query = query + "memorial1       =   ?,";
        query = query + "memorial2       =   ?,";
        query = query + "sex             =   ?,";
        query = query + "name            =   ?,";
        query = query + "name_kana       =   ?,";
        query = query + "address1        =   ?,";
        query = query + "address2        =   ?,";
        query = query + "tel1            =   ?,";
        query = query + "chgpass_date    =   ?,";
        query = query + "chgpass_time    =   ?,";
        query = query + "onetime_key     =   ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            int i = 1;
            prestate.setInt( i++, this.accountId );
            prestate.setString( i++, this.memberId );
            prestate.setString( i++, this.md5 );
            prestate.setString( i++, this.password );
            prestate.setInt( i++, this.status );
            prestate.setInt( i++, this.tempDate );
            prestate.setInt( i++, this.tempTime );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            prestate.setInt( i++, this.delFlag );
            prestate.setString( i++, this.nickname );
            prestate.setInt( i++, this.birthday1 );
            prestate.setInt( i++, this.birthday2 );
            prestate.setInt( i++, this.memorial1 );
            prestate.setInt( i++, this.memorial2 );
            prestate.setInt( i++, this.sex );
            prestate.setString( i++, this.name );
            prestate.setString( i++, this.nameKana );
            prestate.setString( i++, this.address1 );
            prestate.setString( i++, this.address2 );
            prestate.setString( i++, this.tel1 );
            prestate.setInt( i++, this.chgpassDate );
            prestate.setInt( i++, this.chgpassTime );
            prestate.setString( i++, this.onetimeKey );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
                insertMemberNo = getMemberNo( prestate, connection );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMMember.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        /* DB登録後、履歴にも書き込む */
        if ( ret != false )
        {
            try
            {
                dsmh = new DataScMMemberHistory();
                dsmh.setMemberNo( insertMemberNo );
                dsmh.setAccountId( this.accountId );
                dsmh.setMemberId( this.memberId );
                dsmh.setMd5( this.md5 );
                dsmh.setPassword( this.password );
                dsmh.setStatus( this.status );
                dsmh.setTempDate( this.tempDate );
                dsmh.setTempTime( this.tempTime );
                dsmh.setRegistDate( this.registDate );
                dsmh.setRegistTime( this.registTime );
                dsmh.setUpdateDate( this.updateDate );
                dsmh.setUpdateTime( this.updateTime );
                dsmh.setDelFlag( this.delFlag );
                dsmh.setNickname( this.nickname );
                dsmh.setBirthday1( this.birthday1 );
                dsmh.setBirthday2( this.birthday2 );
                dsmh.setMemorial1( this.memorial1 );
                dsmh.setMemorial2( this.memorial2 );
                dsmh.setSex( this.sex );
                dsmh.setName( this.name );
                dsmh.setNameKana( this.nameKana );
                dsmh.setAddress1( this.address1 );
                dsmh.setAddress2( this.address2 );
                dsmh.setTel1( this.tel1 );
                dsmh.setChgpassDate( this.chgpassDate );
                dsmh.setChgpassTime( this.chgpassTime );
                dsmh.setOnetimeKey( this.onetimeKey );
                dsmh.insertData();
            }
            catch ( Exception e )
            {
                Logging.error( "[DataScMMemberHistory.insertData] Exception=" + e.toString() );
                ret = false;
            }
        }

        return(ret);
    }

    /**
     * メンバーマスター情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param memberNo メンバーNo
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int memberNo)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        DataScMMemberHistory dsmh;

        ret = false;

        query = "UPDATE sc.m_member SET ";
        query = query + "account_id      =   ?,";
        query = query + "member_id       =   ?,";
        query = query + "md5             =   ?,";
        query = query + "password        =   ?,";
        query = query + "status          =   ?,";
        query = query + "temp_date       =   ?,";
        query = query + "temp_time       =   ?,";
        query = query + "regist_date     =   ?,";
        query = query + "regist_time     =   ?,";
        query = query + "update_date     =   ?,";
        query = query + "update_time     =   ?,";
        query = query + "del_flag        =   ?,";
        query = query + "nickname        =   ?,";
        query = query + "birthday1       =   ?,";
        query = query + "birthday2       =   ?,";
        query = query + "memorial1       =   ?,";
        query = query + "memorial2       =   ?,";
        query = query + "sex             =   ?,";
        query = query + "name            =   ?,";
        query = query + "name_kana       =   ?,";
        query = query + "address1        =   ?,";
        query = query + "address2        =   ?,";
        query = query + "tel1            =   ?,";
        query = query + "chgpass_date    =   ?,";
        query = query + "chgpass_time    =   ?,";
        query = query + "onetime_key     =   ?";
        query = query + " WHERE member_no = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            int i = 1;
            prestate.setInt( i++, this.accountId );
            prestate.setString( i++, this.memberId );
            prestate.setString( i++, this.md5 );
            prestate.setString( i++, this.password );
            prestate.setInt( i++, this.status );
            prestate.setInt( i++, this.tempDate );
            prestate.setInt( i++, this.tempTime );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            prestate.setInt( i++, this.delFlag );
            prestate.setString( i++, this.nickname );
            prestate.setInt( i++, this.birthday1 );
            prestate.setInt( i++, this.birthday2 );
            prestate.setInt( i++, this.memorial1 );
            prestate.setInt( i++, this.memorial2 );
            prestate.setInt( i++, this.sex );
            prestate.setString( i++, this.name );
            prestate.setString( i++, this.nameKana );
            prestate.setString( i++, this.address1 );
            prestate.setString( i++, this.address2 );
            prestate.setString( i++, this.tel1 );
            prestate.setInt( i++, this.chgpassDate );
            prestate.setInt( i++, this.chgpassTime );
            prestate.setString( i++, this.onetimeKey );
            prestate.setInt( i++, this.memberNo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMMember.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        /* DB登録後、履歴にも書き込む */
        if ( ret != false )
        {
            try
            {
                dsmh = new DataScMMemberHistory();
                dsmh.setMemberNo( this.memberNo );
                dsmh.setAccountId( this.accountId );
                dsmh.setMemberId( this.memberId );
                dsmh.setMd5( this.md5 );
                dsmh.setPassword( this.password );
                dsmh.setStatus( this.status );
                dsmh.setTempDate( this.tempDate );
                dsmh.setTempTime( this.tempTime );
                dsmh.setRegistDate( this.registDate );
                dsmh.setRegistTime( this.registTime );
                dsmh.setUpdateDate( this.updateDate );
                dsmh.setUpdateTime( this.updateTime );
                dsmh.setDelFlag( this.delFlag );
                dsmh.setNickname( this.nickname );
                dsmh.setBirthday1( this.birthday1 );
                dsmh.setBirthday2( this.birthday2 );
                dsmh.setMemorial1( this.memorial1 );
                dsmh.setMemorial2( this.memorial2 );
                dsmh.setSex( this.sex );
                dsmh.setName( this.name );
                dsmh.setNameKana( this.nameKana );
                dsmh.setAddress1( this.address1 );
                dsmh.setAddress2( this.address2 );
                dsmh.setTel1( this.tel1 );
                dsmh.setChgpassDate( this.chgpassDate );
                dsmh.setChgpassTime( this.chgpassTime );
                dsmh.setOnetimeKey( this.onetimeKey );

                dsmh.insertData();
            }
            catch ( Exception e )
            {
                Logging.error( "[DataScMMemberHistory.insertData] Exception=" + e.toString() );
                ret = false;
            }
        }

        return(ret);
    }

    /****
     * メンバーマスターデータ存在チェック
     * (削除フラグは考慮しない)
     * 
     * @param accountId 契約先ID
     * @param memberId メンバーID（メールアドレス）
     * @return 存在するとき：true 存在しないとき:false
     */
    public boolean existsData(int accountId, String memberId)
    {

        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT count(*) as cnt FROM sc.m_member WHERE account_id = ? AND member_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accountId );
            prestate.setString( 2, memberId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    if ( result.getInt( "cnt" ) > 0 )
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMMember.existsData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * メンバーマスターデータ存在チェック
     * （削除フラグを考慮する）
     * 
     * @param md5 md5
     * @return 存在するとき：true 存在しないとき:false
     */
    public boolean existsData(String md5)
    {

        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT count(*) as cnt FROM sc.m_member WHERE md5 = ? AND del_flag = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, md5 );
            prestate.setInt( 2, 0 );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    if ( result.getInt( "cnt" ) > 0 )
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMMember.existsData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * メンバーマスターデータ仮登録有効性チェック
     * 
     * @param md5 契約先ID、メンバーIDをもとに暗号化したkey
     * 
     *            仮登録日付＋仮登録時刻とパスワード変更日付＋パスワード変更時刻を比較し<br>
     *            大きい方の日付＋時刻で24時間の有効性チェックを行う。
     * 
     * @return int配列 <br>
     *         rtn[0]: 有効性（0:無効 1:有効) <br>
     *         rtn[1]: 対象処理 (0:新規会員登録 1:パスワード再設定) <br>
     */
    public int[] effectiveChk(String md5)
    {
        int[] ret = new int[2];
        ret[0] = 0;

        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM sc.m_member WHERE md5 = ? AND del_flag = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, md5 );
            prestate.setInt( 2, 0 );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.setData( result );
                    int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                    int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

                    int targetDate = 0;
                    int targetTime = 0;
                    NumberFormat nf = new DecimalFormat( "000000" );
                    String tempD = Integer.toString( this.getTempDate() ) + nf.format( this.getTempTime() );
                    String chgpassD = Integer.toString( this.getChgpassDate() ) + nf.format( this.getChgpassTime() );
                    if ( Long.parseLong( tempD ) > Long.parseLong( chgpassD ) )
                    {
                        targetDate = this.getTempDate();
                        targetTime = this.getTempTime();
                        ret[1] = 0;
                    }
                    else
                    {
                        targetDate = this.getChgpassDate();
                        targetTime = this.getChgpassTime();
                        ret[1] = 1;
                    }
                    int effectiveDate = DateEdit.addDay( targetDate, 1 );
                    int effectiveTime = targetTime;
                    if ( effectiveDate > nowDate || (effectiveDate == nowDate && effectiveTime > nowTime) )
                    {
                        ret[0] = 1;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMMember.effectiveChk] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 登録したメンバーNO取得
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @return true:正常、false:失敗
     */
    private int getMemberNo(PreparedStatement prestate, Connection connection) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int memberNo = 0;

        query = query + "SELECT LAST_INSERT_ID() AS IDX ";

        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            while( result.next() )
            {
                memberNo = result.getInt( "IDX" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMMember.getMemberNo] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(memberNo);
    }

    /**
     * SCメンバー情報を取得
     * 
     * @param apiKey
     * @param memberID
     */
    public boolean getData(int accountID, int memberNo)
    {

        boolean ret = false;

        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        try
        {

            StringBuilder query = new StringBuilder();
            query.append( "SELECT" );
            query.append( " sc_mm.member_no AS memberNo" );
            query.append( " , sc_mm.account_id AS accountID" );
            query.append( " , sc_mm.member_id AS memberID" );
            query.append( " , sc_mm.`password`" );
            query.append( " , sc_mm.nickname AS nickName" );
            query.append( " , sc_mm.birthday1" );
            query.append( " , sc_mm.birthday2" );
            query.append( " , sc_mm.memorial1" );
            query.append( " , sc_mm.memorial2" );
            query.append( " , sc_mm.sex" );
            query.append( " , sc_mm.name" );
            query.append( " , sc_mm.name_kana AS nameKana" );
            query.append( " , sc_mm.address1" );
            query.append( " , sc_mm.address2" );
            query.append( " , sc_mm.tel1" );
            query.append( " , hub.user_id AS hhUserID" );
            query.append( " , hub.passwd AS hhUserPassword" );
            query.append( " , CASE WHEN LENGTH(IFNULL(hub.mail_addr, '')) > 0 THEN hub.mail_addr ELSE hub.mail_addr_mobile END AS mailAddr" );
            query.append( " , IFNULL(hub.regist_status_pay, 0) AS registStatusPey" );
            query.append( " FROM sc.m_member sc_mm" );
            query.append( " INNER JOIN sc.m_account sc_ma ON sc_ma.account_id = sc_mm.account_id" );
            query.append( " LEFT JOIN sc.r_user_member sc_rum ON sc_mm.member_no = sc_rum.member_no" );
            query.append( " LEFT JOIN hh_user_basic hub ON sc_rum.user_id = hub.user_id" );
            query.append( " WHERE sc_mm.account_id = ?" );
            query.append( " AND sc_mm.member_no = ?" );
            query.append( " AND sc_mm.`status` = 1" );
            query.append( " AND sc_mm.del_flag = 0" );
            query.append( " AND CASE WHEN sc_rum.user_id IS NOT NULL THEN sc_rum.del_flag = 0 ELSE 1 END" );
            query.append( " AND CASE WHEN hub.user_id IS NOT NULL THEN hub.regist_status = 9 ELSE 1 END" );
            query.append( " AND CASE WHEN hub.user_id IS NOT NULL THEN hub.del_flag = 0 ELSE 1 END" );

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            prestate.setInt( 1, accountID );
            prestate.setInt( 2, memberNo );
            result = prestate.executeQuery();
            if ( result.next() )
            {
                this.accountId = result.getInt( "accountID" );
                this.memberNo = result.getInt( "memberNo" );
                this.memberId = result.getString( "memberID" );
                this.password = result.getString( "password" );
                this.name = result.getString( "nickName" );
                this.birthday1 = result.getInt( "birthday1" );
                this.birthday2 = result.getInt( "birthday2" );
                this.memorial1 = result.getInt( "memorial1" );
                this.memorial2 = result.getInt( "memorial2" );
                this.sex = result.getInt( "sex" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "nameKana" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.tel1 = result.getString( "tel1" );
                this.hhUserId = result.getString( "hhUserID" );
                this.hhUserPassword = result.getString( "hhUserPassword" );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataScMMember.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;

    }

    /**
     * SCメンバーとハピホテユーザの連携情報を保存
     */
    public void insertUserMember()
    {
        Connection connection = null;
        PreparedStatement prestate = null;

        try
        {

            StringBuilder query = new StringBuilder();
            query.append( "INSERT INTO sc.r_user_member (" );
            query.append( "    user_id," );
            query.append( "    member_no," );
            query.append( "    regist_date," );
            query.append( "    regist_time," );
            query.append( "    update_date," );
            query.append( "    update_time," );
            query.append( "    del_flag" );
            query.append( ") VALUES (" );
            query.append( "    ?," );
            query.append( "    ?," );
            query.append( "    ?," );
            query.append( "    ?," );
            query.append( "    ?," );
            query.append( "    ?," );
            query.append( "    ?" );
            query.append( ")" );

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            prestate.setString( 1, hhUserId );
            prestate.setInt( 2, memberNo );
            prestate.setInt( 3, registDate );
            prestate.setInt( 4, registTime );
            prestate.setInt( 5, updateDate );
            prestate.setInt( 6, updateTime );
            prestate.setInt( 7, 0 );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[DataScMMember.insertUserMember] Exception=" + e.toString() );
            if ( connection != null )
            {
                try
                {
                    connection.rollback();
                }
                catch ( SQLException e1 )
                {
                    Logging.error( "[DataScMMember.insertUserMember rollBack] Exception=" + e.toString() );
                }
            }
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

}
