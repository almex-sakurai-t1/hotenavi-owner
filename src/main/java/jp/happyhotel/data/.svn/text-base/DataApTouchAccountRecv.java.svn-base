package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ユーザータッチデータ(ap_touch_account_recv)取得クラス
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/8/13
 */
public class DataApTouchAccountRecv implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -7294044327103880848L;
    public static final String TABLE            = "ap_touch_account_recv";
    private String             userId;                                    // ユーザーID
    private int                ciSeq;                                     // チェックインコード(ap_touch_ci.seq)
    private int                accrecvSlipNo;                             // hh_bko_account_recv.accrecv_slip_no
    private int                registDate;                                // 登録日付(YYYYMMDD)
    private int                registTime;                                // 登録時刻(HHMMSS)

    /**
     * データを初期化します。
     */
    public DataApTouchAccountRecv()
    {
        this.userId = "";
        this.ciSeq = 0;
        this.accrecvSlipNo = 0;
        this.registDate = 0;
        this.registTime = 0;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getCiSeq()
    {
        return ciSeq;
    }

    public int getAccrecvSlipNo()
    {
        return accrecvSlipNo;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setCiSeq(int ciSeq)
    {
        this.ciSeq = ciSeq;
    }

    public void setAccrecvSlipNo(int accrecvSlipNo)
    {
        this.accrecvSlipNo = accrecvSlipNo;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    /****
     * ユーザータッチデータ(ap_touch_account_recv)取得
     * 
     * @param userId ユーザーID
     * @param ciSeq チェックインコード(ap_touch_ci.seq)
     * @return
     */
    public boolean getData(String userId, int ciSeq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_touch_account_recv WHERE user_id = ? AND ci_seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, ciSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.ciSeq = result.getInt( "ci_seq" );
                    this.accrecvSlipNo = result.getInt( "accrecv_slip_no" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchAccountRecv.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザータッチデータ(ap_touch_account_recv)設定
     * 
     * @param result マスターソートレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.userId = result.getString( "user_id" );
                this.ciSeq = result.getInt( "ci_seq" );
                this.accrecvSlipNo = result.getInt( "accrecv_slip_no" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchAccountRecv.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ユーザータッチデータ(ap_touch_account_recv)挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
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

        query = "INSERT ap_touch_account_recv SET ";
        query += " user_id=?";
        query += ", ci_seq=?";
        query += ", accrecv_slip_no=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.ciSeq );
            prestate.setInt( i++, this.accrecvSlipNo );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchAccountRecv.insertData] Exception=" + e.toString() );
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
     * ユーザータッチデータ(ap_touch_account_recv)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザーID
     * @param ciSeq チェックインコード(ap_touch_ci.seq)
     * @return
     */
    public boolean updateData(String userId, int ciSeq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_touch_account_recv SET ";
        query += " accrecv_slip_no=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += " WHERE user_id=? AND ci_seq=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.accrecvSlipNo );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.ciSeq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchAccountRecv.updateData] Exception=" + e.toString() );
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
