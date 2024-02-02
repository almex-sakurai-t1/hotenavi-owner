package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * グループホテル情報(ap_group_hotel)取得クラス
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/8/15
 */
public class DataApGroupHotel implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -7097208354953074701L;
    public static final String TABLE            = "ap_group_hotel";
    private int                multiId;                                 // 多店舗ID
    private int                id;                                      // ハピホテホテルID
    private String             propertyCode;                            // 物件番号
    private int                registDate;                              // 登録日付(YYYYMMDD)
    private int                registTime;                              // 登録時刻(HHMMSS)
    private int                delFlag;                                 // 1:削除
    private int                delDate;                                 // 削除日付(YYYYMMDD)
    private int                delTime;                                 // 削除時刻(HHMMSS)

    /**
     * データを初期化します。
     */
    public DataApGroupHotel()
    {
        this.multiId = 0;
        this.id = 0;
        this.propertyCode = "";
        this.registDate = 0;
        this.registTime = 0;
        this.delFlag = 0;
        this.delDate = 0;
        this.delTime = 0;
    }

    public int getMultiId()
    {
        return multiId;
    }

    public int getId()
    {
        return id;
    }

    public String getPropertyCode()
    {
        return propertyCode;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public int getDelDate()
    {
        return delDate;
    }

    public int getDelTime()
    {
        return delTime;
    }

    public void setMultiId(int multiId)
    {
        this.multiId = multiId;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setPropertyCode(String propertyCode)
    {
        this.propertyCode = propertyCode;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setDelDate(int delDate)
    {
        this.delDate = delDate;
    }

    public void setDelTime(int delTime)
    {
        this.delTime = delTime;
    }

    /****
     * グループホテル情報(ap_group_hotel)取得
     * 
     * @param multiId 多店舗ID
     * @param id ハピホテホテルID
     * @return
     */
    public boolean getData(int multiId, int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_group_hotel WHERE multi_id = ? AND id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, multiId );
            prestate.setInt( 2, id );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.multiId = result.getInt( "multi_id" );
                    this.id = result.getInt( "id" );
                    this.propertyCode = result.getString( "property_code" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.delDate = result.getInt( "del_date" );
                    this.delTime = result.getInt( "del_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApGroupHotel.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * グループホテル情報(ap_group_hotel)設定
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
                this.multiId = result.getInt( "multi_id" );
                this.id = result.getInt( "id" );
                this.propertyCode = result.getString( "property_code" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.delFlag = result.getInt( "del_flag" );
                this.delDate = result.getInt( "del_date" );
                this.delTime = result.getInt( "del_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApGroupHotel.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * グループホテル情報(ap_group_hotel)挿入
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

        query = "INSERT ap_group_hotel SET ";
        query += " multi_id=?";
        query += ", id=?";
        query += ", property_code=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", del_flag=?";
        query += ", del_date=?";
        query += ", del_time=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.multiId );
            prestate.setInt( i++, this.id );
            prestate.setString( i++, this.propertyCode );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.delDate );
            prestate.setInt( i++, this.delTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApGroupHotel.insertData] Exception=" + e.toString() );
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
     * グループホテル情報(ap_group_hotel)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param multiId 多店舗ID
     * @param id ハピホテホテルID
     * @return
     */
    public boolean updateData(int multiId, int id)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_group_hotel SET ";
        query += " property_code=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", del_flag=?";
        query += ", del_date=?";
        query += ", del_time=?";
        query += " WHERE multi_id=? AND id=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.propertyCode );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.delDate );
            prestate.setInt( i++, this.delTime );
            prestate.setInt( i++, this.multiId );
            prestate.setInt( i++, this.id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApGroupHotel.updateData] Exception=" + e.toString() );
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
