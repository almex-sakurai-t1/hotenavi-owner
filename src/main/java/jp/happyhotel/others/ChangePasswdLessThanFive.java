package jp.happyhotel.others;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DBConnectionBatch;
import jp.happyhotel.user.UserLogin;

public class ChangePasswdLessThanFive
{
    public static void ChangePasswd(Connection connection)
    {
        String query = "";
        String queryU = "";
        int i = 0;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // SELECT
        query = "SELECT user_id,passwd";
        query += " FROM hotenavi.hh_user_basic ";
        query += " WHERE LENGTH(passwd) <= 5 LIMIT 10";
        // UPDATE
        queryU = "UPDATE hh_user_basic ";
        queryU += "SET passwd = ? ";
        queryU += "WHERE user_id = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            while( result.next() )
            {
                String userId = result.getString( "user_id" );
                System.out.println( "user_id:" + userId );
                String passwd = UserLogin.encrypt( result.getString( "passwd" ) );
                prestate = connection.prepareStatement( queryU );
                prestate.setString( 1, passwd );
                prestate.setString( 2, userId );
                prestate.executeUpdate();
                i++;
            }
            System.out.println( i + "ŒXV‚µ‚Ü‚µ‚½" );
        }
        catch ( Exception e )
        {
            System.out.println( "[ChangePasswdLessThanFive.ChangePasswdLessThanFive] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    public static void main(String[] args) throws Exception
    {
        ChangePasswd( DBConnectionBatch.makeConnection() );
    }
}
