/*
 * @(#)DataScRUserMember.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ハピホテユーザーIDとメンバーのリレーション取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ハピホテユーザーIDとメンバーのリレーション管理情報データ取得クラス
 * 
 * @author Mitsuhashi-k1
 * @version 1.00 2019/07/26
 */
public class DataScRUserMember implements Serializable
{
    private static final long serialVersionUID = -4585645439526206154L;

    /**
     * ハピホテユーザーID。
     */
    private String            userId;

    /**
     * メンバーNo。
     */
    private int               memberNo;

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
     * データを初期化します。
     */
    public DataScRUserMember()
    {
        userId = "";
        memberNo = 0;
        registDate = 0;
        registTime = 0;
        updateDate = 0;
        updateTime = 0;
        delFlag = 0;

    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getMemberNo()
    {
        return memberNo;
    }

    public void setMemberNo(int memberNo)
    {
        this.memberNo = memberNo;
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

    /**
     * ハピホテユーザーIDとメンバーのリレーション取得
     * 
     * @param userId ユーザーID
     * @param memberNo メンバー番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int memberNo)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM sc.r_user_member WHERE user_id = ? AND member_no = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, memberNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.memberNo = result.getInt( "member_no" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.updateDate = result.getInt( "update_date" );
                    this.updateTime = result.getInt( "update_time" );
                    this.delFlag = result.getInt( "del_flag" );

                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataScRUserMember.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ハピホテユーザーIDとメンバーのリレーション設定
     * 
     * @param result ハピホテユーザーIDとメンバーのリレーションレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.userId = result.getString( "user_id" );
                this.memberNo = result.getInt( "member_no" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.updateDate = result.getInt( "update_date" );
                this.updateTime = result.getInt( "update_time" );
                this.delFlag = result.getInt( "del_flag" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataScRUserMember.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ハピホテユーザーIDとメンバーのリレーション追加
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

        query = "INSERT sc.r_user_member SET ";
        query = query + "user_id          =   ?,";
        query = query + "member_no        =   ?,";
        query = query + "regist_date     =   ?,";
        query = query + "regist_time     =   ?,";
        query = query + "update_date     =   ?,";
        query = query + "update_time     =   ?,";
        query = query + "del_flag        =   ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            int i = 1;
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.memberNo );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            prestate.setInt( i++, this.delFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataScRUserMember.insertData] Exception=" + e.toString() );
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
     * ハピホテユーザーIDとメンバーのリレーション更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザーID
     * @param memberNo メンバーNo
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId, int memberNo)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;
        query = "UPDATE sc.r_user_member SET ";
        query = query + "regist_date     =   ?,";
        query = query + "regist_time     =   ?,";
        query = query + "update_date     =   ?,";
        query = query + "update_time     =   ?,";
        query = query + "del_flag        =   ?,";
        query = query + " WHERE user_id = ?";
        query = query + "    AND member_no = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            int i = 1;

            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            prestate.setInt( i++, this.delFlag );
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.memberNo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataScRUserMember.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /****
     * ハピホテユーザーIDとメンバーのリレーション存在チェック
     * (削除フラグは考慮する)
     * 
     * @param userId ユーザーID
     * @param memberNo メンバーNo
     * @return 存在するとき：true 存在しないとき:false
     */
    public boolean existsData(String userId, int memberNo)
    {

        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT count(*) as cnt FROM sc.r_user_member WHERE user_id = ? AND member_no= ? AND del_flag = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, memberNo );
            prestate.setInt( 3, 0 );
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
            Logging.error( "[DataScRUserMember.existsData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * 同じaccount_idに紐づくmember_noのユーザーIDデータ存在チェック
     * (削除フラグは考慮する)
     * 
     * @param userId ユーザーID
     * @param accountId アカウントID
     * @return 存在するとき：true 存在しないとき:false
     */
    public boolean existsSameAcountData(String userId, int accountId)
    {

        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT count(*) as cnt ";
        query += " FROM sc.r_user_member sc_rum ";
        query += "INNER JOIN hh_user_basic hub  ";
        query += "   ON sc_rum.user_id = hub.user_id ";
        query += "LEFT JOIN sc.m_member sc_mm  ";
        query += "   ON sc_rum.member_no = sc_mm.member_no ";
        query += "WHERE hub.user_id = ? ";
        query += "AND sc.sc_mm.account_id = ? ";
        query += "AND sc.sc_rum .del_flag = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, accountId );
            prestate.setInt( 3, 0 );
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
            Logging.error( "[DataScRUserMember.existsData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
