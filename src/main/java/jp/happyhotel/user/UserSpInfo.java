package jp.happyhotel.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserSp;

/**
 * ユーザSP取得クラス。 ユーザキャリア課金データを取得する機能を提供する
 * 
 * @author S.Tashiro
 * @version 1.00 2012/11/23
 * 
 */
public class UserSpInfo
{
    /**
     * ユーザSpデータの件数を取得する（IDから）
     * 
     * @param user_id ユーザID
     * @return 取得件数
     */
    public int getUserSpCount(String user_id)
    {
        int count = 0;

        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        try
        {
            if ( user_id != null && !user_id.equals( "" ) )
            {
                query = "SELECT count(*) FROM hh_user_sp";
                query = query + " WHERE user_id = ?";
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, user_id );

                result = prestate.executeQuery();
                if ( result.next() != false )
                {
                    count = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserSpInfo.getUserSpCount] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(count);
    }

    /**
     * 有料会員フラグの立っているユーザSpデータを取得する（IDから）
     * 
     * @param user_id ユーザID
     * @return データクラス
     */
    public DataUserSp getUserSp(String user_id)
    {
        DataUserSp ret = null;

        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        try
        {
            if ( user_id != null && !user_id.equals( "" ) )
            {
                query = "SELECT * FROM hh_user_sp";
                query += " WHERE user_id = ? AND del_flag = 0";
                query += " AND charge_flag > 0";

                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, user_id );
                result = prestate.executeQuery();
                if ( result.next() != false )
                {
                    ret = new DataUserSp();
                    ret.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserSpInfo.getPayMemberSp] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 無料会員フラグ判定処理（IDから）
     * 
     * @param user_id ユーザID
     * @return 判定結果(True→NG会員、False→その他)
     */
    public boolean getFreeMemberSp(String user_id)
    {
        boolean ret = false;

        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        try
        {
            if ( user_id != null && !user_id.equals( "" ) )
            {
                query = "SELECT * FROM hh_user_sp";
                query += " WHERE user_id = ? AND charge_flag = 0 AND carrier_kind = 1";
                query += " AND free_mymenu = 1 AND del_flag=0";
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, user_id );

                result = prestate.executeQuery();
                if ( result.next() != false )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserSpInfo.getFreeMemberFlag] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 有料会員か判定処理（IDから）
     * 
     * @param user_id ユーザID
     * @return 判定結果(True→有料会員、False→無料会員)
     */
    public boolean getPayMemberSp(String user_id)
    {
        boolean ret = false;

        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        try
        {
            if ( user_id != null && !user_id.equals( "" ) )
            {
                query = "SELECT * FROM hh_user_sp";
                query = query + " WHERE user_id = ? AND charge_flag > 0 AND del_flag = 0";
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, user_id );
                result = prestate.executeQuery();
                if ( result.next() != false )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserSpInfo.getPayMemberFlag] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

}
