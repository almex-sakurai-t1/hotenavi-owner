package jp.happyhotel.touch;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApHotelCustom;

/**
 * �z�e���ڋq�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2011/05/19
 */
public class GroupHotelCustom implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -6199069863331936331L;
    // �[�����f�[�^
    private DataApHotelCustom dahc             = new DataApHotelCustom();

    //
    public DataApHotelCustom getCustom()
    {
        return dahc;
    }

    /**
     * �O���[�v�z�e���̌ڋq�����擾
     * 
     * @param id
     * @param userId
     * @return
     */
    public boolean getMutltiCustomData(int id, String userId)
    {
        boolean ret = false;

        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // �z�e��ID����O���\�v�̑��X��ID�����߂āA�O���[�v�̌ڋq�f�[�^��I�����āA���[�UID�ōi�荞��
        query = "SELECT B.* FROM ap_group_hotel A";
        // �z�e���ڋq�̃f�[�^�Ń��[�UID�ň�v����L���ȃf�[�^���擾
        query += " INNER JOIN ap_hotel_custom B ON A.id = B.id AND B.user_id = ? AND B.regist_status = 1 AND B.del_flag = 0";
        // �O���[�v�R�[�h���z�e��ID�Ɠ����z�e���������ɂ���
        query += " WHERE multi_id = ( SELECT A.multi_id FROM ap_group_hotel A where id = ? AND A.del_flag=0 )";
        query += " LIMIT 0,1";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, id );
            result = prestate.executeQuery();

            if ( result != null )
            {

                if ( result.next() != false )
                {
                    this.dahc.setData( result );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelCustom.getCustomData()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }
}
