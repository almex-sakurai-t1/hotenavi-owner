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
 * ���[�U����G���[��񌟍��E�X�V�N���X
 *
 * @author an-j1
 * @version 1.00 2017/09/07
 */
public class UserOpeError
{

    /**
     * �R���X�g���N�^
     */
    public UserOpeError()
    {
    }

    /**
     * ���[�U����G���[���擾
     *
     * @param strUserId ���[�UID
     * @return ��������(ArrayList<DataUserOpeError>)
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
     * ���[�U����G���[���擾
     *
     * @param strUserId ���[�UID
     * @param iSeqNo �V�[�P���X�m�n
     * @return ��������(DataUserOpeError)
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
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
