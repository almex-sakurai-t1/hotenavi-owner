package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Mon Oct 15 16:39:48 JST 2012
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * メッセージデータ
 * 
 * @author tashiro-s1
 * @version
 */
public class DataSystemMessage implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -8453989593652373987L;
    private int               kind;
    private int               seq;
    private int               startDate;
    private int               endDate;
    private String            text;
    private int               delFlag;

    /**
     * Constractor
     */
    public DataSystemMessage()
    {
        kind = 0;
        seq = 0;
        startDate = 0;
        endDate = 0;
        text = "";
        delFlag = 0;
    }

    public int getKind()
    {
        return kind;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public String getText()
    {
        return text;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    /****
     * メニューデータ取得
     * 
     * @param kind
     * @param seq
     * @return
     */
    public boolean getData(int kind, int seq)
    {

        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_system_menu WHERE kind = ? AND seq = ?";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, kind );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.kind = result.getInt( "kind" );
                    this.seq = result.getInt( "seq" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.text = result.getString( "text" );
                    this.delFlag = result.getInt( "del_flag" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemMenu.getData] Exception=" + e.toString() );
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
     * フェリカ紐付けデータ設定
     * 
     * @param result ユーザポイントデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.kind = result.getInt( "kind" );
                this.seq = result.getInt( "seq" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.text = result.getString( "text" );
                this.delFlag = result.getInt( "del_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemMenu.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * フェリカ紐付けデータ挿入
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

        query = "INSERT hh_system_felica_matching SET ";
        query += " kind = ?,";
        query += " seq = ?,";
        query += " start_date = ?,";
        query += " end_date = ?,";
        query += " text = ?,";
        query += " del_flag = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.kind );
            prestate.setInt( 2, this.seq );
            prestate.setInt( 3, this.startDate );
            prestate.setInt( 4, this.endDate );
            prestate.setString( 5, this.text );
            prestate.setInt( 6, this.delFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemMenu.insertData] Exception=" + e.toString() );
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
     * フェリカ紐付けデータ更新
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

        query = "UPDATE hh_system_felica_matching SET ";
        query += " start_date = ?,";
        query += " end_date = ?,";
        query += " text = ?,";
        query += " del_flag = ?";
        query = query + " WHERE kind = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.startDate );
            prestate.setInt( 2, this.endDate );
            prestate.setString( 3, this.text );
            prestate.setInt( 4, this.delFlag );
            prestate.setInt( 5, kind );
            prestate.setInt( 6, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemMenu.updateData] Exception=" + e.toString() );
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
