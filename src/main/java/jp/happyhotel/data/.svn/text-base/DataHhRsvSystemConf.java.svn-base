package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * SystemCong取得クラス
 * 
 * @author K.Mitsuhashi
 * @version 1.00 2017/06/6
 */
public class DataHhRsvSystemConf implements Serializable
{
    private static final long serialVersionUID = -6185468558334380773L;

    /** ID1 **/
    private int               ctrlId1;
    /** ID2 **/
    private int               ctrlId2;
    /** Val1 **/
    private String            val1;
    /** Val2 **/
    private String            val2;
    /** Val3 **/
    private String            val3;
    /** remarks **/
    private String            remarks;

    /**
     * データを初期化します。
     */
    public DataHhRsvSystemConf()
    {
        ctrlId1 = 0;
        ctrlId2 = 0;
        val1 = "";
        val2 = "";
        val3 = "";
        remarks = "";
    }

    public int getCtrlId1()
    {
        return ctrlId1;
    }

    public int getCtrlId2()
    {
        return ctrlId2;
    }

    public String getVal1()
    {
        return val1;
    }

    public String getVal2()
    {
        return val2;
    }

    public String getVal3()
    {
        return val3;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setCtrlId1(int ctrlId1)
    {
        this.ctrlId1 = ctrlId1;
    }

    public void setCtrlId2(int ctrlId2)
    {
        this.ctrlId2 = ctrlId2;
    }

    public void setVal1(String val1)
    {
        this.val1 = val1;
    }

    public void setVal2(String val2)
    {
        this.val2 = val2;
    }

    public void setVal3(String val3)
    {
        this.val3 = val3;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    /**
     * データ取得
     * 
     * @param ctrl_id1 ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int ctrl_id1, int ctrl_id2)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_rsv_system_conf WHERE ctrl_id1 = ? AND ctrl_id2 = ? ";
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, ctrl_id1 );
            prestate.setInt( 2, ctrl_id2 );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvSystemConf.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * データ設定
     * 
     * @param result データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.ctrlId1 = result.getInt( "ctrl_id1" );
                this.ctrlId2 = result.getInt( "ctrl_id2" );
                this.val1 = result.getString( "val1" );
                this.val2 = result.getString( "val2" );
                this.val3 = result.getString( "val3" );
                this.remarks = result.getString( "remarks" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvSystemConf.setData] Exception=" + e.toString() );
        }

        return(true);
    }
}
