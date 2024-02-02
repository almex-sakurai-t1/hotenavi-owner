/*
 * 実績データ
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

public class DataRsvResult implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 7317045875665481704L;
    private int               id;
    private String            reserve_no;
    private int               seq;
    private int               ci_date;
    private int               use_kind;
    private int               total_flag;
    private int               total_date;
    private int               total_time;
    private int               regist_date;
    private int               regist_time;

    /**
     * データの初期化
     */
    public DataRsvResult()
    {
        id = 0;
        reserve_no = "";
        seq = 0;
        ci_date = 0;
        use_kind = 0;
        total_flag = 0;
        total_date = 0;
        total_time = 0;
        regist_date = 0;
        regist_time = 0;
    }

    /**
     * 
     * getter
     * 
     */
    public int getCiDate()
    {
        return this.ci_date;
    }

    public int getId()
    {
        return this.id;
    }

    public int getRegistDate()
    {
        return this.regist_date;
    }

    public int getRegistTime()
    {
        return this.regist_time;
    }

    public String getRsvNo()
    {
        return this.reserve_no;
    }

    public int getSeq()
    {
        return this.seq;
    }

    public int getTotalDate()
    {
        return this.total_date;
    }

    public int getTotalFlag()
    {
        return this.total_flag;
    }

    public int getTotalTime()
    {
        return this.total_time;
    }

    public int getUseKind()
    {
        return this.use_kind;
    }

    /**
     * 
     * setter
     * 
     */
    public void setCiDate(int cidate)
    {
        this.ci_date = cidate;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setRegistDate(int registdate)
    {
        this.regist_date = registdate;
    }

    public void setRegistTime(int registtime)
    {
        this.regist_time = registtime;
    }

    public void setRsvNo(String rsvno)
    {
        this.reserve_no = rsvno;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setTotalDate(int totaldate)
    {
        this.total_date = totaldate;
    }

    public void setTotalFlag(int totalflag)
    {
        this.total_flag = totalflag;
    }

    public void setTotalTime(int totaltime)
    {
        this.total_time = totaltime;
    }

    public void setUseKind(int usekind)
    {
        this.use_kind = usekind;
    }

    //

    /**
     * 実績データ情報取得
     * 
     * @param id
     * @param rsvno
     * 
     *            return
     */
    public boolean getData(int id, String rsvno)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, reserve_no, seq, ci_date, use_kind, " +
                " total_flag, total_date, total_time, regist_date, regist_time " +
                " FROM hh_rsv_result WHERE id = ? AND reserve_no = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, rsvno );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.reserve_no = result.getString( "reserve_no" );
                    this.seq = result.getInt( "seq" );
                    this.ci_date = result.getInt( "ci_date" );
                    this.use_kind = result.getInt( "use_kind" );
                    this.total_flag = result.getInt( "total_flag" );
                    this.total_date = result.getInt( "total_date" );
                    this.total_time = result.getInt( "total_time" );
                    this.regist_date = result.getInt( "regist_date" );
                    this.regist_time = result.getInt( "regist_time" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvResult.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 実績データ登録
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

        query = "INSERT INTO hh_rsv_result SET " +
                "  id = ?" +
                ", reserve_no = ?" +
                ", seq = ?" +
                ", ci_date = ? " +
                ", use_kind = ? " +
                ", total_flag = ? " +
                ", total_date = ? " +
                ", total_time = ? " +
                ", regist_date = ? " +
                ", regist_time = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setString( 2, this.reserve_no );
            prestate.setInt( 3, this.seq );
            prestate.setInt( 4, this.ci_date );
            prestate.setInt( 5, this.use_kind );
            prestate.setInt( 6, this.total_flag );
            prestate.setInt( 7, this.total_date );
            prestate.setInt( 8, this.total_time );
            prestate.setInt( 9, this.regist_date );
            prestate.setInt( 10, this.regist_time );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvResult.insertData] Exception=" + e.toString() );
            ret = false;
        }
        return(ret);
    }

    /**
     * 実績データ削除
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean deleteData(Connection conn)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "DELETE FROM hh_rsv_result  " +
                " WHERE id = ?" +
                " AND reserve_no = ?";

        try
        {
            prestate = conn.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setString( 2, this.reserve_no );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvResult.deleteData] Exception=" + e.toString() );
            ret = false;
        }
        return(ret);
    }

}
