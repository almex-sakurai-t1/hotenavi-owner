/*
 * @(#)DataMMemberHistory.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 メンバーマスター情報履歴データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * メンバーマスター情報履歴データ取得クラス
 * 
 * @author Mitsuhashi-k1
 * @version 1.00 2019/07/31
 */
public class DataScMMemberHistory implements Serializable
{
    private static final long serialVersionUID = -4585645439526206154L;

    /**
     * メンバー番号。
     */
    private int               memberNo;

    /**
     * メンバー番号連番。
     */
    private int               seq;

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
     * ワンタイムキー。
     */
    private String            onetimeKey;

    /**
     * データを初期化します。
     */
    public DataScMMemberHistory()
    {
        memberNo = 0;
        seq = 0;
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

    public int getSeq()
    {
        return seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
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

    public String getOnetimeKey()
    {
        return onetimeKey;
    }

    public void setOnetimeKey(String onetimeKey)
    {
        this.onetimeKey = onetimeKey;
    }

    /**
     * メンバーマスター情報履歴データ取得
     * 
     * @param memberNo メンバー番号
     * @param seq メンバー番号連番
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int memberNo, int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM sc.m_member_history WHERE member_no = ? AND seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, memberNo );
            prestate.setInt( 2, seq );
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
            Logging.error( "[DataMMemberHistory.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * メンバーマスター情報履歴データ設定
     * 
     * @param result メンバーマスター情報履歴データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.memberNo = result.getInt( "member_no" );
                this.seq = result.getInt( "seq" );
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
            Logging.error( "[DataMMemberHistory.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * メンバーマスター情報履歴データ追加
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

        query = "INSERT sc.m_member_history SET ";
        query = query + "member_no      =   ?,";
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
            prestate.setInt( i++, this.memberNo );
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
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMMemberHistory.insertData] Exception=" + e.toString() );
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
     * メンバーマスター情報履歴データ更新
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

        ret = false;

        query = "UPDATE sc.m_member_history SET ";
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
        query = query + " WHERE member_no = ? AND seq= ? ";
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
            prestate.setInt( i++, this.seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMMemberHistory.updateData] Exception=" + e.toString() );
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
