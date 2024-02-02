/*
 * @(#)UserBasicHistory.java 1.00 2007/08/30 Copyright (C) ALMEX Inc. 2007 メール送信クラス
 */
package jp.happyhotel.user;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;
import jp.happyhotel.data.*;

/**
 * ユーザ基本情報履歴取得クラス。 ユーザの基本情報履歴を取得する機能を提供する
 * 
 * @author S.Tashiro
 * @version 1.00 2007/08/30
 * @version 1.1 2007/11/26
 */
public class UserBasicHistoryInfo implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -7867754625202884884L;

    /**
     * データを初期化します。
     */
    public UserBasicHistoryInfo()
    {

    }

    /**
     * ハンドルネーム重複チェック
     * 
     * @param handleName ハンドルネーム
     * @return 処理結果(false:未登録,true:登録済み)
     */
    public boolean getHandleNameCheck(String handleName)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_basic_history WHERE handle_name = ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setString( 1, handleName );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserBasicHistory.getHandleNameCheck] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ハンドルネーム重複チェック
     * 
     * @param userId ユーザID
     * @param handleName ハンドルネーム
     * @return 処理結果(false:未登録,true:登録済み)
     */
    public boolean getHandleNameCheck(String userId, String handleName)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataUserBasic dub;

        // 現在の自分のものだった場合は未登録とする
        dub = new DataUserBasic();
        ret = dub.getData( userId );
        if ( ret != false )
        {
            if ( handleName.compareTo( dub.getHandleName() ) == 0 )
            {
                return(false);
            }
        }

        while( true )
        {
            // 自分以外のもの
            query = "SELECT user_id FROM hh_user_basic WHERE user_id <> ? AND handle_name = ?";

            ret = false;

            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                if ( prestate != null )
                {
                    prestate.setString( 1, userId );
                    prestate.setString( 2, handleName );
                    result = prestate.executeQuery();
                    if ( result != null )
                    {
                        if ( result.next() != false )
                        {
                            ret = true;
                        }
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserBasicHistory.getHandleNameCheck] Exception=" + e.toString() );
            }
            finally
            {
                result = null;
                prestate = null;
            }

            // もし重複していた場合はここで終了
            if ( ret != false )
            {
                break;
            }

            // 自分以外のものでユーザIDとかぶっているかチェックする
            query = "SELECT user_id FROM hh_user_basic WHERE user_id <> ? AND user_id = ?";

            ret = false;

            try
            {
                prestate = connection.prepareStatement( query );
                if ( prestate != null )
                {
                    prestate.setString( 1, userId );
                    prestate.setString( 2, handleName );
                    result = prestate.executeQuery();
                    if ( result != null )
                    {
                        if ( result.next() != false )
                        {
                            ret = true;
                        }
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserBasicHistory.getHandleNameCheck] Exception=" + e.toString() );
            }
            finally
            {
                result = null;
                prestate = null;
            }

            // もし重複していた場合はここで終了
            if ( ret != false )
            {
                break;
            }

            // 履歴の参照
            query = "SELECT user_id FROM hh_user_basic_history WHERE handle_name = ?";

            ret = false;

            try
            {
                prestate = connection.prepareStatement( query );
                if ( prestate != null )
                {
                    prestate.setString( 1, handleName );
                    result = prestate.executeQuery();
                    if ( result != null )
                    {
                        if ( result.next() != false )
                        {
                            ret = true;
                        }
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserBasicHistory.getHandleNameCheck] Exception=" + e.toString() );
            }
            finally
            {
                result = null;
                prestate = null;
            }

            // while用break
            break;
        }

        DBConnection.releaseResources( result, prestate, connection );

        return(ret);
    }
}
