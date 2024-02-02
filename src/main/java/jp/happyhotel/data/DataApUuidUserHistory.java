package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

public class DataApUuidUserHistory implements Serializable
{
    public static final String TABLE = "ap_uuid_user_history";
    private String             uuid;                          //
    private String             userId;                        //

    /**
     * �f�[�^�����������܂��B
     */
    public DataApUuidUserHistory()
    {
        this.uuid = "";
        this.userId = "";
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * �A�v�����[�U�[(ap_uuid_user_history)�}��
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean insertData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT ap_uuid_user_history SET ";
        query += " uuid=?";
        query += ", user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( i++, this.uuid );
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuidUserHistory.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }
}
