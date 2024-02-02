package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class UserPointDelete implements Serializable
{
    // ポイント区分
    private static final int POINT_KIND_RAITEN = 21; // 来店
    private static final int POINT_KIND_FUYO   = 22; // 付与
    private static final int POINT_KIND_RIYOU  = 23; // 使用
    private static final int POINT_KIND_YOYAKU = 24; // 予約
    public String            errMsg            = "";

    public String getErrorMessage()
    {
        return errMsg;
    }

    /***
     * 売り掛けデータ取得
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

            // hh_user_point_payのデータ削除
            query = "DELETE FROM hh_user_point_pay WHERE ext_code = ? ";
            query += " AND point_kind BETWEEN " + POINT_KIND_RAITEN + " AND " + POINT_KIND_YOYAKU;
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, id );

            resultUserPointPay = prestate.executeUpdate();

            // hh_user_point_pay_tempのデータ削除
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
                errMsg += "ハピホテマイル、ハピホテ仮マイルのデータがないか削除に失敗しました<br>";
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
