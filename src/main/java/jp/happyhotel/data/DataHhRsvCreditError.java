package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 予約クレジットエラー取得クラス
 * 
 * @author takeshi-sakurai
 * @version 1.00 2015/5/18
 */
public class DataHhRsvCreditError implements Serializable
{
    /**
	 * 
	 */
    private static final long  serialVersionUID = -6272749999727739199L;
    public static final String TABLE            = "hh_rsv_credit_error";
    private String             orderId;                                 // オーダーID
    private int                jobSeq;                                  // 処理連番
    private int                errorSeq;                                // エラー連番
    private String             errorCode;                               // エラーコード
    private String             errorInfo;                               // エラー詳細コード

    /**
     * データを初期化します。
     */
    public DataHhRsvCreditError()
    {
        this.orderId = "";
        this.jobSeq = 0;
        this.errorSeq = 0;
        this.errorCode = "";
        this.errorInfo = "";
    }

    public String getOrderId()
    {
        return orderId;
    }

    public int getJobSeq()
    {
        return jobSeq;
    }

    public int getErrorSeq()
    {
        return errorSeq;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public String getErrorInfo()
    {
        return errorInfo;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public void setJobSeq(int jobSeq)
    {
        this.jobSeq = jobSeq;
    }

    public void setErrorSeq(int errorSeq)
    {
        this.errorSeq = errorSeq;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    public void setErrorInfo(String errorInfo)
    {
        this.errorInfo = errorInfo;
    }

    /****
     * 予約クレジットエラー取得
     * 
     * @param orderId 予約番号
     * @param jobSeq 処理連番
     * @param errorSeq 連番
     * @return
     */
    public boolean getData(String orderId, int jobSeq, int errorSeq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM newRsvDB.hh_rsv_credit_error WHERE order_id = ? AND job_seq = ? AND error_seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, orderId );
            prestate.setInt( 2, jobSeq );
            prestate.setInt( 3, errorSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.orderId = result.getString( "order_id" );
                    this.jobSeq = result.getInt( "job_seq" );
                    this.errorSeq = result.getInt( "error_seq" );
                    this.errorCode = result.getString( "error_code" );
                    this.errorInfo = result.getString( "error_info" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCreditError.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 予約クレジットエラー設定
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
                this.orderId = result.getString( "order_id" );
                this.jobSeq = result.getInt( "job_seq" );
                this.errorSeq = result.getInt( "error_seq" );
                this.errorCode = result.getString( "error_code" );
                this.errorInfo = result.getString( "error_info" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCreditError.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 予約クレジットエラー挿入
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

        query = "INSERT newRsvDB.hh_rsv_credit_error SET ";
        query += " order_id=?";
        query += ", job_seq=?";
        query += ", error_seq=?";
        query += ", error_code=?";
        query += ", error_info=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.orderId );
            prestate.setInt( i++, this.jobSeq );
            prestate.setInt( i++, this.errorSeq );
            prestate.setString( i++, this.errorCode );
            prestate.setString( i++, this.errorInfo );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCreditError.insertData] Exception=" + e.toString() );
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
     * 予約クレジットエラー挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData(Connection connection)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT newRsvDB.hh_rsv_credit_error SET ";
        query += " order_id=?";
        query += ", job_seq=?";
        query += ", error_seq=?";
        query += ", error_code=?";
        query += ", error_info=?";
        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.orderId );
            prestate.setInt( i++, this.jobSeq );
            prestate.setInt( i++, this.errorSeq );
            prestate.setString( i++, this.errorCode );
            prestate.setString( i++, this.errorInfo );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCreditError.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * 予約クレジットエラー更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param orderId 予約番号
     * @param jobSeq 処理連番
     * @param errorSeq 連番
     * @return
     */
    public boolean updateData(String orderId, int jobSeq, int errorSeq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE newRsvDB.hh_rsv_credit_error SET ";
        query += " error_code=?";
        query += ", error_info=?";
        query += " WHERE order_id=? AND job_seq=? AND error_seq=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.errorCode );
            prestate.setString( i++, this.errorInfo );
            prestate.setString( i++, this.orderId );
            prestate.setInt( i++, this.jobSeq );
            prestate.setInt( i++, this.errorSeq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCreditError.updateData] Exception=" + e.toString() );
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
