/*
 * @(#)UserBasicInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ユーザ基本情報取得クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataRsvUserBasic;

/**
 * 予約ユーザ基本情報取得クラス。 ユーザの予約基本情報を取得する機能を提供する
 * 
 * @author Y.Tanabe
 * @version 1.00 2012/09/20
 */
public class UserRsvBasicInfo implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -3924291538252750667L;

    private DataRsvUserBasic  userRsvBasic;

    /**
     * データを初期化します。
     */
    public UserRsvBasicInfo()
    {
    }

    /** 予約ユーザ基本情報取得 **/
    public DataRsvUserBasic getRsvUserInfo()
    {
        return(userRsvBasic);
    }

    /**
     * 予約停止ユーザチェック処理
     * 
     * @return 処理結果(TRUE:予約停止ユーザ)
     */
    public boolean checkRsvStopUser()
    {
        boolean ret = false;
        int nowyyyymmdd = 0;

        try
        {
            nowyyyymmdd = Integer.parseInt( DateEdit.getDate( 2 ) );
            if ( userRsvBasic != null )
            {
                // 予約機能停止期間内であれば予約停止ユーザとする
                if ( nowyyyymmdd >= userRsvBasic.getLimitationStartDate() && nowyyyymmdd <= userRsvBasic.getLimitationEndDate() )
                {
                    ret = true;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[checkRsvStopUser] Exception=" + e.toString() );
        }
        finally
        {
        }

        return(ret);
    }

    /**
     * 予約ユーザ基本情報を取得する（IDから）
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRsvUserBasic(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(false);
        }
        query = "SELECT * FROM hh_rsv_user_basic";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ?";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }
            ret = getRsvUserBasicSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRsvUserBasic] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 予約ユーザ基本情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getRsvUserBasicSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        boolean ret = false;

        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                userRsvBasic = new DataRsvUserBasic();

                if ( result.next() != false )
                {
                    // ユーザ基本データ情報の設定
                    this.userRsvBasic.setData( result );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getRsvUserBasicSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(ret);
    }

}
