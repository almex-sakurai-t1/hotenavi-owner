package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class BkoDataDelete implements Serializable
{
    private static final long serialVersionUID = 8108503716559584113L;

    public String             slipNo           = "";
    public int                billCd           = 0;
    public String             billSlipNo       = "";
    public String             errMsg           = "";

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
        int resultBkoAccountRecv = 0;

        boolean ret = false;
        String query = "";
        String errMsg = "";
        Connection conn = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            conn = DBConnection.getConnection( false );

            query = "START TRANSACTION ";
            prestate = conn.prepareStatement( query );
            result = prestate.executeQuery();

            // hh_bko_account_recv_detailとhh_bko_account_recvのテーブルを削除
            query = "DELETE hh_bko_account_recv_detail, hh_bko_account_recv";
            query += " FROM hh_bko_account_recv_detail, hh_bko_account_recv ";
            query += " WHERE hh_bko_account_recv.accrecv_slip_no = hh_bko_account_recv_detail.accrecv_slip_no";
            query += " AND  hh_bko_account_recv.id = ?";
            query += " AND  hh_bko_account_recv.closing_kind < 3";
            query += " AND  hh_bko_account_recv_detail.closing_kind < 3";
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, id );

            resultBkoAccountRecv = prestate.executeUpdate();

            if ( resultBkoAccountRecv >= 0 )
            {
                query = "COMMIT ";
                prestate = conn.prepareStatement( query );
                result = prestate.executeQuery();
            }
            else
            {
                query = "ROLLBACK";
                prestate = conn.prepareStatement( query );
                result = prestate.executeQuery();
                errMsg += "売掛、売掛明細のデータがないか、削除に失敗しました。<br>";
            }

            if ( resultBkoAccountRecv >= 0 )
            {
                ret = true;
            }

            // hh_bko_account_recvだけのデータがあった場合に備えて、再度削除処理
            if ( ret != false )
            {
                conn = DBConnection.getConnection( false );

                query = "START TRANSACTION ";
                prestate = conn.prepareStatement( query );
                result = prestate.executeQuery();

                // hh_bko_account_recv_detailとhh_bko_account_recvのテーブルを削除
                query = "DELETE FROM hh_bko_account_recv ";
                query += " WHERE hh_bko_account_recv.id = ?";
                query += " AND  hh_bko_account_recv.closing_kind < 3";

                prestate = conn.prepareStatement( query );
                prestate.setInt( 1, id );

                resultBkoAccountRecv = prestate.executeUpdate();

                if ( resultBkoAccountRecv >= 0 )
                {
                    query = "COMMIT ";
                    prestate = conn.prepareStatement( query );
                    result = prestate.executeQuery();
                }
                else
                {
                    query = "ROLLBACK";
                    prestate = conn.prepareStatement( query );
                    result = prestate.executeQuery();
                    errMsg += "売掛、売掛明細のデータがないか、削除に失敗しました。<br>";
                }

                if ( resultBkoAccountRecv >= 0 )
                {
                    ret = true;
                }

            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[BkoDataDelete.deleteData()] Exception:" + e.toString() );
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
