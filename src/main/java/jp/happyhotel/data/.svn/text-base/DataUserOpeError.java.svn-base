/**
 *
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ユーザ操作エラー情報保持クラス
 *
 * @author an-j1
 * @version 1.00 2017/09/07
 */
public class DataUserOpeError implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String            userId;
    private int               seqNo;
    private int               registDate;
    private int               registTime;
    private String            opeError;

    /**
     * コンストラクタ
     */
    public DataUserOpeError()
    {
        userId = "";
        seqNo = 0;
        registDate = 0;
        registTime = 0;
        opeError = "";
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getSeqNo()
    {
        return seqNo;
    }

    public void setSeqNo(int seqNo)
    {
        this.seqNo = seqNo;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public String getOpeError()
    {
        return opeError;
    }

    public void setOpeError(String opeError)
    {
        this.opeError = opeError;
    }

    /**
     * ユーザ操作エラー情報取得
     *
     * @param userId ユーザID
     * @param seqNo SeqNo
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int seqNo)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_ope_error WHERE user_id = ? AND seq_no = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, seqNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    setUserId( result.getString( "user_id" ) );
                    setSeqNo( result.getInt( "seq_no" ) );
                    setRegistDate( result.getInt( "regist_date" ) );
                    setRegistTime( result.getInt( "regist_time" ) );
                    setOpeError( result.getString( "ope_error" ) );
                    ret = true;
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
        return(ret);
    }

    /**
     * ユーザ操作エラーデータ設定
     *
     * @param result ユーザ操作エラーデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                setUserId( result.getString( "user_id" ) );
                setSeqNo( result.getInt( "seq_no" ) );
                setRegistDate( result.getInt( "regist_date" ) );
                setRegistTime( result.getInt( "regist_time" ) );
                setOpeError( result.getString( "ope_error" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserOpeError.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザ操作エラーデータ追加
     *
     * @see "値のセット後(setXXX)に行うこと"
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

        query = "INSERT hh_user_ope_error SET ";
        query = query + " user_id = ?,";
        query = query + " seq_no = 0,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " ope_error = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, getUserId() );
            prestate.setInt( 2, getRegistDate() );
            prestate.setInt( 3, getRegistTime() );
            prestate.setString( 4, getOpeError() );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserOpeError.insertData] Exception=" + e.toString() );
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
     * ユーザ操作エラーデータ変更
     *
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @param seqNo SeqNo
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId, int seqNo)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_ope_error SET ";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " ope_error = ?";
        query = query + " WHERE user_id = ? AND seq_no = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, getRegistDate() );
            prestate.setInt( 2, getRegistTime() );
            prestate.setString( 3, getOpeError() );
            prestate.setString( 4, userId );
            prestate.setInt( 5, seqNo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserOpeError.updateData] Exception=" + e.toString() );
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
