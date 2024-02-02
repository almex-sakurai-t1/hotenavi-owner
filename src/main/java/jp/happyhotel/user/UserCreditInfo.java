package jp.happyhotel.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserCredit;

/**
 * ユーザクレジットデータ取得クラス。 ユーザクレジットデータを取得する機能を提供する
 *
 * @author Y.Tanabe
 * @version 1.00 2011/11/08
 *
 */
public class UserCreditInfo
{
    /**
     * ユーザクレジットデータの件数を取得する（IDから）
     *
     * @param user_id ユーザID
     * @return 取得件数
     */
    public int getUserCreditCount(String user_id)
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
                query = "SELECT count(*) FROM hh_user_credit";
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
            Logging.info( "[UserCreditInfo.getUserCreditCount] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate,connection );
        }

        return(count);
    }

    /**
     * 有料会員フラグの立っているユーザクレジットデータを取得する（IDから）
     *
     * @param user_id ユーザID
     * @return データクラス
     */
    public DataUserCredit getPayMemberUserCredit(String user_id)
    {
        DataUserCredit ret = null;

        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        try
        {
            if ( user_id != null && !user_id.equals( "" ) )
            {
                query = "SELECT * FROM hh_user_credit";
                query = query + " WHERE user_id = ? AND (charge_flag = ? OR charge_flag = ? OR charge_flag = ?) AND del_flag = 0";
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, user_id );
                prestate.setInt( 2, DataUserCredit.CHARGEFLAG_PAY );
                prestate.setInt( 3, DataUserCredit.CHARGEFLAG_FIRSTPAY );
                prestate.setInt( 4, DataUserCredit.CHARGEFLAG_ALMEX );
                result = prestate.executeQuery();
                if ( result.next() != false )
                {
                    ret = new DataUserCredit();
                    ret.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserCreditInfo.getPayMemberUserCredit] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate,connection );
        }

        return(ret);
    }

    /**
     * NG会員フラグか判定処理（IDから）
     *
     * @param user_id ユーザID
     * @return 判定結果(True→NG会員、False→その他)
     */
    public boolean getNgMemberFlag(String user_id)
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
                query = "SELECT * FROM hh_user_credit";
                query = query + " WHERE user_id = ? AND charge_flag = ?";
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, user_id );
                prestate.setInt( 2, DataUserCredit.CHARGEFLAG_NGMEMBER );

                result = prestate.executeQuery();
                if ( result.next() != false )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserCreditInfo.getPayMemberUserCredit] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate,connection );
        }

        return(ret);
    }

    /**
     * 有料会員か判定処理（IDから）
     *
     * @param user_id ユーザID
     * @return 判定結果(True→有料会員、False→無料会員)
     */
    public boolean getPayMemberFlag(String user_id)
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
                query = "SELECT * FROM hh_user_credit";
                query = query + " WHERE user_id = ? AND (charge_flag = ? OR charge_flag = ? OR charge_flag = ?) AND del_flag = 0";
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, user_id );
                prestate.setInt( 2, DataUserCredit.CHARGEFLAG_PAY );
                prestate.setInt( 3, DataUserCredit.CHARGEFLAG_FIRSTPAY );
                prestate.setInt( 4, DataUserCredit.CHARGEFLAG_ALMEX );
                result = prestate.executeQuery();
                if ( result.next() != false )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserCreditInfo.getPayMemberUserCredit] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate,connection );
        }
        return(ret);
    }

    /**
     *
     * 退会予約会員取得（IDから）
     *
     * @param user_id ユーザID
     * @return データクラス
     */
    public DataUserCredit getUnRegistReserveMember(String user_id)
    {
        DataUserCredit ret = null;

        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        try
        {
            if ( user_id != null && !user_id.equals( "" ) )
            {
                query = "SELECT * FROM hh_user_credit";
                query = query + " WHERE user_id = ? AND charge_flag = ? AND del_flag = 1";
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, user_id );
                prestate.setInt( 2, DataUserCredit.CHARGEFLAG_PAY );

                result = prestate.executeQuery();
                if ( result.next() != false )
                {
                    ret = new DataUserCredit();
                    ret.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserCreditInfo.getUnRegistReserveMember] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate,connection );
        }

        return(ret);
    }

    /**
     * ユーザクレジットデータの件数を取得する（IDから）
     *
     * @param user_id ユーザID
     * @return 取得件数
     */
    public int getMaxSeq(String user_id)
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
                query = "SELECT MAX(card_seq_no) AS max FROM hh_user_credit";
                query = query + " WHERE user_id = ?";
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, user_id );

                result = prestate.executeQuery();
                if ( result.next() != false )
                {
                    count = result.getInt( "max" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserCreditInfo.getMaxSeq] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate,connection );
        }

        return(count);
    }

}

