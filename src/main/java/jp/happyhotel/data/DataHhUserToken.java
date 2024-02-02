package jp.happyhotel.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * APIのアクセストークン発行用のリフレッシュトークン取得クラス
 * 
 * @author koshiba-y1
 */
public class DataHhUserToken
{
    public static final String TABLE = "hh_user_token";
    private String             userId;                 // ユーザーID
    private String             token;                  // リフレッシュトークン

    /**
     * データを初期化します。
     */
    public DataHhUserToken()
    {
        this.userId = "";
        this.token = "";
    }

    public String getUserId()
    {
        return userId;
    }

    public String getToken()
    {
        return token;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    /**
     * 取得
     * 
     * @param id ホテルID
     * @return 処理の成功・失敗を表すブール値
     */
    public boolean getData(String userId)
    {
        boolean ret = false;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        final String query = "SELECT * FROM hh_user_token WHERE user_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.token = result.getString( "token" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhUserToken.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /**
     * 削除
     * 
     * @param userId ユーザID
     * @return 処理の成功・失敗を表すブール値
     */
    public boolean deleteData(String userId)
    {
        boolean ret = false;

        Connection connection = null;
        int result = 0;
        PreparedStatement prestate = null;

        final String query = "DELETE FROM hh_user_token WHERE user_id = ?";

        if ( userId != null && userId.compareTo( "" ) != 0 )
        {
            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, userId );
                result = prestate.executeUpdate();
                if ( result > 0 )
                {
                    ret = true;
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[DataHhUserToken.deleteData] Exception=" + e.toString() );
                ret = false;
            }
            finally
            {
                DBConnection.releaseResources( prestate );
                DBConnection.releaseResources( connection );
            }
        }

        return ret;
    }

    // 挿入や更新は必要になったタイミングで実装してください。
}
