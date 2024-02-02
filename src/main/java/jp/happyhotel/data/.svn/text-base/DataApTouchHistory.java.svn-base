package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri Jul 20 11:53:01 JST 2012
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ap_touch_history.
 * 
 * @author mitsuhashi
 * @version 1.0
 *          history
 *          Symbol Date Person Note
 *          [1] 2016/11/15 mitsuhashi Generated.
 */
public class DataApTouchHistory implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -7975602684113024812L;
    private int               id;
    private int               seq;
    private int               ciSeq;
    private String            userId;
    private int               registDate;
    private int               registTime;
    private String            detail;

    public DataApTouchHistory()
    {
        id = 0;
        seq = 0;
        ciSeq = 0;
        userId = "";
        registDate = 0;
        registTime = 0;
        detail = "";
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getSeq()
    {
        return this.seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public int getCiSeq()
    {
        return this.ciSeq;
    }

    public void setCiSeq(int ciSeq)
    {
        this.ciSeq = ciSeq;
    }

    public int getciSeq()
    {
        return this.ciSeq;
    }

    public String getUserId()
    {
        return this.userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getRegistDate()
    {
        return this.registDate;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public int getRegistTime()
    {
        return this.registTime;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public String getDetail()
    {
        return this.detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    /****
     * メニューデータ取得
     * 
     * @param kind
     * @param seq
     * @return
     */
    public boolean getData(int seq)
    {

        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM ap_touch_history WHERE seq = ?";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.ciSeq = result.getInt( "ci_seq" );
                    this.userId = result.getString( "user_id" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.detail = result.getString( "detail" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataTouchHistory.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( count > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ホテルタッチ履歴データ設定
     * 
     * @param result ホテルタッチ履歴データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.seq = result.getInt( "seq" );
                this.ciSeq = result.getInt( "ci_seq" );
                this.userId = result.getString( "user_id" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.detail = result.getString( "detail" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchHistory.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ホテルタッチ履歴データ挿入
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

        query = "INSERT ap_touch_history SET ";
        query += " id = ?,";
        query += " ci_seq = ?,";
        query += " user_id = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?,";
        query += " detail = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.ciSeq );
            prestate.setString( 3, this.userId );
            prestate.setInt( 4, this.registDate );
            prestate.setInt( 5, this.registTime );
            prestate.setString( 6, this.detail );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchHistory.insertData] Exception=" + e.toString() );
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
     * ホテルタッチ履歴データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param key1　アクセスキー1
     * @param key2　アクセスキー2
     * @return
     */
    public boolean updateData(int kind, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE ap_touch_history SET ";
        query += " id = ?,";
        query += " ci_seq = ?,";
        query += " user_id = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?,";
        query += " detail = ? ";
        query += " WHERE seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.ciSeq );
            prestate.setString( 3, this.userId );
            prestate.setInt( 4, this.registDate );
            prestate.setInt( 5, this.registTime );
            prestate.setString( 6, this.detail );
            prestate.setInt( 7, this.seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchHistory.updateData] Exception=" + e.toString() );
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
