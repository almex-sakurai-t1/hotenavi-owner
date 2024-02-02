package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Sun Dec 14 15:49:20 JST 2014
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class DataApUuid implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 2979098065154150712L;
    private String            uuid;
    private String            receipt;
    private int               receiptCheck;
    private int               restore;
    private int               registStatusPay;
    private int               registDate;
    private int               registTime;
    private int               updateDate;
    private int               updateTime;

    public DataApUuid()
    {
        this.uuid = "";
        this.receipt = "";
        this.receiptCheck = 1;
        this.restore = 0;
        this.registStatusPay = 0;
        this.registDate = 0;
        this.registTime = 0;
        this.updateDate = 0;
        this.updateTime = 0;
    }

    public String getUuid()
    {
        return this.uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getReceipt()
    {
        return this.receipt;
    }

    public void setReceipt(String receipt)
    {
        this.receipt = receipt;
    }

    public int getReceiptCheck()
    {
        return this.receiptCheck;
    }

    public void setReceiptCheck(int receiptCheck)
    {
        this.receiptCheck = receiptCheck;
    }

    public int getRestore()
    {
        return this.restore;
    }

    public void setRestore(int restore)
    {
        this.restore = restore;
    }

    public int getRegistStatusPay()
    {
        return this.registStatusPay;
    }

    public void setRegistStatusPay(int registStatusPay)
    {
        this.registStatusPay = registStatusPay;
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

    public int getUpdateDate()
    {
        return this.updateDate;
    }

    public void setUpdateDate(int updateDate)
    {
        this.updateDate = updateDate;
    }

    public int getUpdateTime()
    {
        return this.updateTime;
    }

    public void setUpdateTime(int updateTime)
    {
        this.updateTime = updateTime;
    }

    /**
     * UUID取得
     * 
     * @param uuid
     * @return
     */
    public boolean getData(String uuid)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_uuid WHERE uuid = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, uuid );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.uuid = result.getString( "uuid" );
                    this.receipt = result.getString( "receipt" );
                    this.receiptCheck = result.getInt( "receipt_check" );
                    this.restore = result.getInt( "restore" );
                    this.registStatusPay = result.getInt( "regist_status_pay" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.updateDate = result.getInt( "update_date" );
                    this.updateTime = result.getInt( "update_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuid.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * UUID取得
     * 
     * @param result マスターソートレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @see 値をセットすること
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.uuid = result.getString( "uuid" );
                this.receipt = result.getString( "receipt" );
                this.receiptCheck = result.getInt( "receipt_check" );
                this.restore = result.getInt( "restore" );
                this.registStatusPay = result.getInt( "regist_status_pay" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.updateDate = result.getInt( "update_date" );
                this.updateTime = result.getInt( "update_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuid.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * UUID挿入
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @see 値をセットすること
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

        query = "INSERT ap_uuid SET ";
        query += " uuid=?";
        query += ", receipt=?";
        query += ", receipt_check=?";
        query += ", restore=?";
        query += ", regist_status_pay=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", update_date=?";
        query += ", update_time=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.uuid );
            prestate.setString( i++, this.receipt );
            prestate.setInt( i++, this.receiptCheck );
            prestate.setInt( i++, this.restore );
            prestate.setInt( i++, this.registStatusPay );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuid.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /***
     * UUID更新
     * 
     * @param uuid
     * @return
     */
    public boolean updateData(String uuid)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_uuid SET ";
        query += " receipt=?";
        query += ", receipt_check=?";
        query += ", restore=?";
        query += ", regist_status_pay=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", update_date=?";
        query += ", update_time=?";
        query += " WHERE uuid=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.receipt );
            prestate.setInt( i++, this.receiptCheck );
            prestate.setInt( i++, this.restore );
            prestate.setInt( i++, this.registStatusPay );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            prestate.setString( i++, uuid );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuid.updateData] Exception=" + e.toString() );
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
