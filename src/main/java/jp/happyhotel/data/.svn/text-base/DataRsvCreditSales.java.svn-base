/*
 * 予約ノーショークレジット売上データクラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvCreditSales implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1301982339239965148L;

    private String            reserve_no;
    private int               seq_no;
    private String            spid;
    private int               sales_date;
    private int               generate_date;
    private int               generate_time;
    private String            sales_name;
    private int               reserve_amount;
    private int               amount;
    private int               percent;
    private int               approve;
    private String            forward;
    private String            tranid;
    private String            cancel_tranid;
    private int               cancel_flag;
    private int               id;
    private int               informFlag;

    /**
     * データの初期化
     */
    public DataRsvCreditSales()
    {
        setReserve_no( "" );
        setSeq_no( 0 );
        setSpid( "" );
        setSales_date( 0 );
        setGenerate_date( 0 );
        setGenerate_time( 0 );
        setSales_name( "" );
        setReserve_amount( 0 );
        setAmount( 0 );
        setPercent( 0 );
        setApprove( 0 );
        setForward( "" );
        setTranid( "" );
        setCancel_tranid( "" );
        setCancel_flag( 0 );
        setId( 0 );
        setInformFlag( 0 );
    }

    /**
     * 
     * シーケンス番号取得処理
     * 
     */
    public void setSelectSeqNo()
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            if ( this.reserve_no.equals( "" ) != true && this.tranid.equals( "" ) == false )
            {
                query = "select seq_no from hh_rsv_credit_sales where reserve_no = ? and tranid = ?";
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, this.reserve_no );
                prestate.setString( 2, this.tranid );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        this.setSeq_no( result.getInt( "seq_no" ) );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvCreditSales.setSelectSeqNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return;
    }

    /**
     * 
     * シーケンス番号取得処理
     * 
     */
    public void setSelectSeqNo(Connection connection)
    {
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            if ( this.reserve_no.equals( "" ) != true && this.tranid.equals( "" ) == false )
            {
                query = "select seq_no from hh_rsv_credit_sales where reserve_no = ? and tranid = ?";
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, this.reserve_no );
                prestate.setString( 2, this.tranid );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        this.setSeq_no( result.getInt( "seq_no" ) );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvCreditSales.setSelectSeqNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return;
    }

    // getter

    /**
     * 予約クレジットデータ情報取得
     * 
     * @param reserveNo 予約番号
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(String reserveNo, int seq_no)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT reserve_no, seq_no, spid, sales_date, generate_date, " +
                " generate_time, sales_name, reserve_amount, amount, percent, approve, forward, tranid, cancel_tranid, cancel_flag, id " +
                " FROM hh_rsv_credit_sales WHERE reserve_no = ? and seq_no = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, reserveNo );
            prestate.setInt( 2, seq_no );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.setReserve_no( result.getString( "reserve_no" ) );
                    this.setSeq_no( result.getInt( "seq_no" ) );
                    this.setSpid( result.getString( "spid" ) );
                    this.setSales_date( result.getInt( "sales_date" ) );
                    this.setGenerate_date( result.getInt( "generate_date" ) );
                    this.setGenerate_time( result.getInt( "generate_time" ) );
                    this.setSales_name( result.getString( "sales_name" ) );
                    this.setReserve_amount( result.getInt( "reserve_amount" ) );
                    this.setAmount( result.getInt( "amount" ) );
                    this.setPercent( result.getInt( "percent" ) );
                    this.setApprove( result.getInt( "approve" ) );
                    this.setForward( result.getString( "forward" ) );
                    this.setTranid( result.getString( "tranid" ) );
                    this.setCancel_tranid( result.getString( "cancel_tranid" ) );
                    this.setCancel_flag( result.getInt( "cancel_flag" ) );
                    this.setId( result.getInt( "id" ) );
                    this.setInformFlag( result.getInt( "inform_flag" ) );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvCreditSales.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * クレジット売上データ登録
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT INTO hh_rsv_credit_sales SET " +
                " reserve_no = ?" +
                ", spid = ? " +
                ", sales_date = ? " +
                ", generate_date = ? " +
                ", generate_time = ? " +
                ", sales_name = ? " +
                ", reserve_amount = ? " +
                ", amount = ? " +
                ", percent = ? " +
                ", approve = ? " +
                ", forward = ? " +
                ", tranid = ? " +
                ", cancel_tranid = ? " +
                ", cancel_flag = ? " +
                ", id = ? " +
                ", inform_flag = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.reserve_no );
            prestate.setString( 2, this.spid );
            prestate.setInt( 3, this.sales_date );
            prestate.setInt( 4, this.generate_date );
            prestate.setInt( 5, this.generate_time );
            prestate.setString( 6, this.sales_name );
            prestate.setInt( 7, this.reserve_amount );
            prestate.setInt( 8, this.amount );
            prestate.setInt( 9, this.percent );
            prestate.setInt( 10, this.approve );
            prestate.setString( 11, this.forward );
            prestate.setString( 12, this.tranid );
            prestate.setString( 13, this.cancel_tranid );
            prestate.setInt( 14, this.cancel_flag );
            prestate.setInt( 15, this.id );
            prestate.setInt( 16, this.informFlag );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvCreditSales.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * クレジット売上データ登録
     * 
     * @param conn DB接続クラス
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(Connection conn)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT INTO hh_rsv_credit_sales SET " +
                " reserve_no = ?" +
                ", spid = ? " +
                ", sales_date = ? " +
                ", generate_date = ? " +
                ", generate_time = ? " +
                ", sales_name = ? " +
                ", reserve_amount = ? " +
                ", amount = ? " +
                ", percent = ? " +
                ", approve = ? " +
                ", forward = ? " +
                ", tranid = ? " +
                ", cancel_tranid = ? " +
                ", cancel_flag = ? " +
                ", id = ? " +
                ", inform_flag = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.reserve_no );
            prestate.setString( 2, this.spid );
            prestate.setInt( 3, this.sales_date );
            prestate.setInt( 4, this.generate_date );
            prestate.setInt( 5, this.generate_time );
            prestate.setString( 6, this.sales_name );
            prestate.setInt( 7, this.reserve_amount );
            prestate.setInt( 8, this.amount );
            prestate.setInt( 9, this.percent );
            prestate.setInt( 10, this.approve );
            prestate.setString( 11, this.forward );
            prestate.setString( 12, this.tranid );
            prestate.setString( 13, this.cancel_tranid );
            prestate.setInt( 14, this.cancel_flag );
            prestate.setInt( 15, this.id );
            prestate.setInt( 16, this.informFlag );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvCreditSales.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    public String getReserve_no()
    {
        return reserve_no;
    }

    public int getSeq_no()
    {
        return seq_no;
    }

    public String getSpid()
    {
        return spid;
    }

    public int getSales_date()
    {
        return sales_date;
    }

    public int getGenerate_date()
    {
        return generate_date;
    }

    public int getGenerate_time()
    {
        return generate_time;
    }

    public String getSales_name()
    {
        return sales_name;
    }

    public int getReserve_amount()
    {
        return reserve_amount;
    }

    public int getAmount()
    {
        return amount;
    }

    public int getPercent()
    {
        return percent;
    }

    public int getApprove()
    {
        return approve;
    }

    public String getForward()
    {
        return forward;
    }

    public String getTranid()
    {
        return tranid;
    }

    public String getCancel_tranid()
    {
        return cancel_tranid;
    }

    public int getCancel_flag()
    {
        return cancel_flag;
    }

    public int getId()
    {
        return id;
    }

    public int getInformFlag()
    {
        return informFlag;
    }

    public void setReserve_no(String reserve_no)
    {
        this.reserve_no = reserve_no;
    }

    public void setSeq_no(int seq_no)
    {
        this.seq_no = seq_no;
    }

    public void setSpid(String spid)
    {
        this.spid = spid;
    }

    public void setSales_date(int sales_date)
    {
        this.sales_date = sales_date;
    }

    public void setGenerate_date(int generate_date)
    {
        this.generate_date = generate_date;
    }

    public void setGenerate_time(int generate_time)
    {
        this.generate_time = generate_time;
    }

    public void setSales_name(String sales_name)
    {
        this.sales_name = sales_name;
    }

    public void setReserve_amount(int reserve_amount)
    {
        this.reserve_amount = reserve_amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public void setPercent(int percent)
    {
        this.percent = percent;
    }

    public void setApprove(int approve)
    {
        this.approve = approve;
    }

    public void setForward(String forward)
    {
        this.forward = forward;
    }

    public void setTranid(String tranid)
    {
        this.tranid = tranid;
    }

    public void setCancel_tranid(String cancel_tranid)
    {
        this.cancel_tranid = cancel_tranid;
    }

    public void setCancel_flag(int cancel_flag)
    {
        this.cancel_flag = cancel_flag;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setInformFlag(int informflag)
    {
        this.informFlag = informflag;
    }

}
