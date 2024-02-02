package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataBkoAccountRecv;
import jp.happyhotel.data.DataBkoAccountRecvDetail;
import jp.happyhotel.data.DataUserPointPayTemp;

public class BkoAccountRecv implements Serializable
{
    // ポイント区分
    private static final int        POINT_KIND_RAITEN   = 21; // 来店
    private static final int        POINT_KIND_RIYOU    = 22; // 利用
    private static final int        POINT_KIND_WARIBIKI = 23; // 割引
    private static final int        POINT_KIND_YOYAKU   = 24; // 予約

    public int                      bkoAccountRecvCount;
    public int                      bkoSlipDetailNo;
    public DataBkoAccountRecv       bkoAccountRecv;
    public DataBkoAccountRecvDetail bkoAccountRecvDetail;

    public int getBkoAccountRecvCount()
    {
        return bkoAccountRecvCount;
    }

    public int getSlipDetailNo()
    {
        return bkoSlipDetailNo;
    }

    public DataBkoAccountRecv getBkoAccountRecv()
    {
        return bkoAccountRecv;
    }

    public DataBkoAccountRecvDetail getBkoAccountRecvDetail()
    {
        return bkoAccountRecvDetail;
    }

    /***
     * 売り掛けデータ取得
     * 
     * @param duppt
     * @return
     */
    public boolean getData(DataUserPointPayTemp duppt)
    {
        boolean ret = false;

        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        int i;
        int count;
        ret = false;

        query = " SELECT Recv.*, RecvDetail.slip_detail_no FROM hh_user_point_pay_temp Point";
        query += " INNER JOIN hh_bko_account_recv_detail RecvDetail";
        query += "    ON  Point.user_id = RecvDetail.user_id AND Point.seq = RecvDetail.seq";
        query += " INNER JOIN hh_bko_account_recv Recv";
        query += "    ON RecvDetail.accrecv_slip_no = Recv.accrecv_slip_no";
        query += " WHERE Point.user_id = ? ";
        query += " AND Point.point_kind BETWEEN " + POINT_KIND_RAITEN + " AND " + POINT_KIND_YOYAKU;
        query += " AND Point.ext_code = ?";
        query += " AND Point.user_seq= ?";
        query += " AND Point.visit_seq = ?";
        query += " ORDER BY Point.seq DESC, Point.get_date DESC, Point.get_time DESC";
        query += " LIMIT 0,1 ";

        try
        {
            conn = DBConnection.getConnectionRO();
            prestate = conn.prepareStatement( query );

            prestate.setString( 1, duppt.getUserId() );
            prestate.setInt( 2, duppt.getExtCode() );
            prestate.setInt( 3, duppt.getUserSeq() );
            prestate.setInt( 4, duppt.getVisitSeq() );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        bkoAccountRecvCount = result.getRow();
                    }

                    // レコードセットを1件目に移動
                    result.first();
                    // クラスの配列を用意し、初期化する。
                    this.bkoAccountRecv = new DataBkoAccountRecv();
                    bkoAccountRecv.setData( result );
                    this.bkoSlipDetailNo = this.getMaxSlipDetailNo( this.bkoAccountRecv.getAccrecvSlipNo() );
                }
            }
            if ( bkoAccountRecvCount > 0 )
            {
                ret = true;
            }
            else
            {
                ret = false;
            }
            Logging.info( "BkoAccountRecv", "" + ret );

        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[BkoAccountRecv.getData()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }

        return ret;
    }

    /***
     * 売り掛け詳細データ取得
     * 
     * @param duppt
     * @return
     */
    public boolean getDetailData(DataUserPointPayTemp duppt)
    {
        boolean ret = false;

        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        int i;
        int count;
        ret = false;

        query = " SELECT Recv.*, RecvDetail.* FROM hh_user_point_pay_temp Point";
        query += " INNER JOIN hh_bko_account_recv_detail RecvDetail";
        query += "    ON  Point.user_id = RecvDetail.user_id AND Point.seq = RecvDetail.seq";
        query += " INNER JOIN hh_bko_account_recv Recv";
        query += "    ON RecvDetail.accrecv_slip_no = Recv.accrecv_slip_no";
        query += " WHERE Point.user_id = ? ";
        query += " AND Point.seq = ? ";
        query += " ORDER BY Point.seq DESC, Point.get_date DESC, Point.get_time DESC ";
        query += " LIMIT 0,1 ";

        try
        {
            // トランザクションの開始
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );

            prestate.setString( 1, duppt.getUserId() );
            prestate.setInt( 2, duppt.getSeq() );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        bkoAccountRecvCount = result.getRow();
                    }

                    // レコードセットを1件目に移動
                    result.first();
                    // クラスの配列を用意し、初期化する。
                    this.bkoAccountRecv = new DataBkoAccountRecv();
                    this.bkoAccountRecvDetail = new DataBkoAccountRecvDetail();
                    this.bkoAccountRecv.setData( result );
                    this.bkoAccountRecvDetail.setData( result );
                    this.bkoSlipDetailNo = this.getMaxSlipDetailNo( this.bkoAccountRecv.getAccrecvSlipNo() );
                }
            }
            if ( bkoAccountRecvCount > 0 )
            {
                ret = true;
            }
            else
            {
                ret = false;
            }
            // Logging.info( query, "getDetailData:" + ret );
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[BkoAccountRecv.getDetailData()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }

        return ret;
    }

    private int getMaxSlipDetailNo(int accrecvSlipNo)
    {
        boolean ret = false;
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        int count = 0;
        ret = false;

        query = " SELECT MAX( slip_detail_no ) FROM hh_bko_account_recv_detail";
        query += " WHERE accrecv_slip_no = ? ";

        try
        {
            // トランザクションの開始
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );

            prestate.setInt( 1, accrecvSlipNo );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        count = result.getInt( 1 );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[BkoAccountRecv.getMaxSlipDetailNo()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }

        return count;
    }
}
