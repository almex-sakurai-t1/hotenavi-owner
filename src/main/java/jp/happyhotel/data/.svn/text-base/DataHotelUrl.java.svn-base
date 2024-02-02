package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

import com.hotenavi2.common.DateEdit;

/**
 * hh_hotel_url （リンクURL）取得クラス
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2015/1/9
 */
public class DataHotelUrl implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -9030841807677744776L;
    public static final String TABLE            = "hh_hotel_url";
    protected int              id;                                      // ホテルID
    protected int              seq;                                     // 連番
    protected String           url;                                     // URL
    protected int              dataType;                                // 0:hotenavi,1:オフィシャル,2:携帯用,3:Twitter,4:yahoo地域情報,5:facebook,6:googleインドアビュー
    protected int              delFlag;                                 // 1:削除
    protected int              startDate;                               // 開始日付（yyyymmdd）
    protected int              endDate;                                 // 終了日付（yyyymmdd）

    /**
     * データを初期化します。
     */
    public DataHotelUrl()
    {
        this.id = 0;
        this.seq = 0;
        this.url = "";
        this.dataType = 0;
        this.delFlag = 0;
        this.startDate = 0;
        this.endDate = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getUrl()
    {
        return url;
    }

    public int getDataType()
    {
        return dataType;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setDataType(int dataType)
    {
        this.dataType = dataType;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    /****
     * hh_hotel_url （リンクURL）取得
     * 
     * @param id ホテルID
     * @param data_type データタイプ
     * @param del_flag 削除済みフラグ
     * @return
     */
    public boolean getData(int id, int data_type, boolean del_flag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        DateEdit dt = new DateEdit();
        String now_date = dt.getDate( 2 );
        int del_flag_int = del_flag ? 1 : 0;
        ret = false;
        query = "SELECT * FROM hh_hotel_url WHERE id = ? AND data_type = ? ";
        query += " AND start_date<=" + now_date;
        query += " AND end_date>=" + now_date;
        query += " AND del_flag=" + del_flag_int;
        query += " ORDER BY seq DESC";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, data_type );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.url = result.getString( "url" );
                    this.dataType = result.getInt( "data_type" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelUrl.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * hh_hotel_url （リンクURL）取得（del_flag = 0）
     * 
     * @param id ホテルID
     * @param data_type データタイプ
     * @return
     */
    public boolean getData(int id, int data_type)
    {
        return getData( id, data_type, false );
    }

    /**
     * hh_hotel_url （リンクURL）設定
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
                this.id = result.getInt( "id" );
                this.seq = result.getInt( "seq" );
                this.url = result.getString( "url" );
                this.dataType = result.getInt( "data_type" );
                this.delFlag = result.getInt( "del_flag" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelUrl.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * hh_hotel_url （リンクURL）挿入
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

        query = "INSERT hh_hotel_url SET ";
        query += " id=?";
        query += ", url=?";
        query += ", data_type=?";
        query += ", del_flag=?";
        query += ", start_date=?";
        query += ", end_date=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.id );
            prestate.setString( i++, this.url );
            prestate.setInt( i++, this.dataType );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.startDate );
            prestate.setInt( i++, this.endDate );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelUrl.insertData] Exception=" + e.toString() );
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
     * hh_hotel_url （リンクURL）更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param seq 連番
     * @return
     */
    public boolean updateData(int id, int seq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE hh_hotel_url SET ";
        query += " url=?";
        query += ", data_type=?";
        query += ", del_flag=?";
        query += ", start_date=?";
        query += ", end_date=?";
        query += " WHERE id=? AND seq=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.url );
            prestate.setInt( i++, this.dataType );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.startDate );
            prestate.setInt( i++, this.endDate );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelUrl.updateData] Exception=" + e.toString() );
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
