package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ���[�UUuid�擾�N���X�B
 * 
 * @author T.Sakurai
 * @version 1.00 2016/05/17
 * 
 */
public class UserUuidInfo implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 6514172473676853729L;

    /**
     * �L����������菈���iuser_id����j
     * 
     * @param user_id ���[�UID
     * @return ���茋��(True���L������AFalse���������)
     */
    public static boolean isPayMember(String user_id)
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
                query = "SELECT AU.uuid FROM ap_uuid_user AUU";
                query += " INNER JOIN ap_uuid AU ON AUU.uuid=AU.uuid AND AU.regist_status_pay = 2";
                query += " WHERE AUU.user_id = ? ORDER BY AUU.regist_date DESC,AUU.regist_time DESC";
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
            Logging.info( "[UserUuidInfo.isPayMember] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

}
