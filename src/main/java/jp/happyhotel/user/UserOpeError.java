/**
 *
 */
package jp.happyhotel.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserOpeError;

/**
 * ユーザ操作エラー情報検索・更新クラス
 *
 * @author an-j1
 * @version 1.00 2017/09/07
 */
public class UserOpeError
{

    /**
     * コンストラクタ
     */
    public UserOpeError()
    {
    }

    /**
     * ユーザ操作エラー情報取得
     *
     * @param strUserId ユーザID
     * @return 処理結果(ArrayList<DataUserOpeError>)
     */
    public ArrayList<DataUserOpeError> getData(String strUserId)
    {
        ArrayList<DataUserOpeError> dataUserOpeErrorList = new ArrayList<DataUserOpeError>();
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_ope_error WHERE user_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, strUserId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() )
                {
                    DataUserOpeError data = new DataUserOpeError();
                    data.setData( result );
                    dataUserOpeErrorList.add( data );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserOpeError.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(dataUserOpeErrorList);
    }

    /**
     * ユーザ操作エラー情報取得
     *
     * @param strUserId ユーザID
     * @param iSeqNo シーケンスＮＯ
     * @return 処理結果(DataUserOpeError)
     */
    public DataUserOpeError getData(String strUserId, int iSeqNo)
    {
        DataUserOpeError dataUserOpeError = new DataUserOpeError();

        if ( !dataUserOpeError.getData( strUserId, iSeqNo ) )
        {
            dataUserOpeError = null;
        }

        return(dataUserOpeError);
    }

    /**
     *
     * @param userId
     * @param strMsg
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(String userId, String strMsg)
    {
        boolean ret = false;

        DataUserOpeError data = new DataUserOpeError();

        data.setUserId( userId );
        data.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        data.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        data.setOpeError( strMsg );

        ret = data.insertData();

        return ret;

    }
}
