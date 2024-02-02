package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class UserPointDelete implements Serializable
{
    // �|�C���g�敪
    private static final int POINT_KIND_RAITEN = 21; // ���X
    private static final int POINT_KIND_FUYO   = 22; // �t�^
    private static final int POINT_KIND_RIYOU  = 23; // �g�p
    private static final int POINT_KIND_YOYAKU = 24; // �\��
    public String            errMsg            = "";

    public String getErrorMessage()
    {
        return errMsg;
    }

    /***
     * ����|���f�[�^�擾
     * 
     * @param duppt
     * @return
     */
    public boolean deleteData(int id)
    {
        int resultUserPointPay = 0;
        int resultUserPointPayTemp = 0;

        boolean ret = false;
        boolean existSlipNo = false;
        boolean existBillSlipNo = false;
        String query = "";
        String errMsg = "";
        Connection conn = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            conn = DBConnection.getConnection();

            // hh_user_point_pay�̃f�[�^�폜
            query = "DELETE FROM hh_user_point_pay WHERE ext_code = ? ";
            query += " AND point_kind BETWEEN " + POINT_KIND_RAITEN + " AND " + POINT_KIND_YOYAKU;
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, id );

            resultUserPointPay = prestate.executeUpdate();

            // hh_user_point_pay_temp�̃f�[�^�폜
            query = "DELETE FROM hh_user_point_pay_temp WHERE ext_code = ?";
            query += " AND point_kind BETWEEN " + POINT_KIND_RAITEN + " AND " + POINT_KIND_YOYAKU;
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, id );

            resultUserPointPayTemp = prestate.executeUpdate();

            if ( (resultUserPointPay >= 0) && (resultUserPointPayTemp >= 0) )
            {
                ret = true;
            }
            else
            {
                errMsg += "�n�s�z�e�}�C���A�n�s�z�e���}�C���̃f�[�^���Ȃ����폜�Ɏ��s���܂���<br>";
                ret = false;
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[UserPointDelete.deleteData()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }

        return ret;
    }
}
