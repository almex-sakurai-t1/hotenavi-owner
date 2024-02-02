package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 予約クレジットデータ取得クラス
 * 
 * @author Techno
 * @version 1.00 2015/4/27
 */
public class DataHhRsvCredit implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -1191773740511683295L;
    public static final String TABLE            = "hh_rsv_credit";
    /** 予約番号 */
    private String             reserveNo;
    /** カード番号 */
    private String             cardNo;
    /** 有効期限 */
    private int                limitDate;
    /** ホテルＩＤ */
    private int                id;
    /** トランザクションID */
    private String             tranid;
    /** 承認番号 */
    private String             approved;
    /** 仕向先コード */
    private String             forwarded;
    /** エラーコード */
    private String             errCode;
    /** エラー詳細コード */
    private String             errInfo;
    /** エラーレベル */
    private int                errLevel;
    /** 最終更新日付 */
    private int                lastUpdate;
    /** 最終更新時刻 */
    private int                lastUptime;
    /** 削除日付 */
    private int                delDate;
    /** 削除時刻 */
    private int                delTime;
    /** 削除フラグ */
    private int                delFlag;
    /** 取引ID */
    private String             accessId;
    /** 取引パスワード */
    private String             accessPass;
    /** オーダーID */
    private String             orderId;
    /** 売上金額 */
    private int                amount;
    /** 実売上フラグ */
    private int                salesFlag;                               // 1:実売上
    /** 実売上日付 */
    private int                salesDate;                               // YYYYMMDD
    /** 実売上時刻 */
    private int                salesTime;                               // HHMMSS
    /** クレジット手数料 */
    private int                creditFee;

    /**
     * データを初期化します。
     */
    public DataHhRsvCredit()
    {
        this.reserveNo = "";
        this.cardNo = "";
        this.limitDate = 0;
        this.id = 0;
        this.tranid = "";
        this.approved = "";
        this.forwarded = "";
        this.errCode = "";
        this.errInfo = "";
        this.errLevel = 0;
        this.lastUpdate = 0;
        this.lastUptime = 0;
        this.delDate = 0;
        this.delTime = 0;
        this.delFlag = 0;
        this.accessId = "";
        this.accessPass = "";
        this.orderId = "";
        this.amount = 0;
        this.salesFlag = 0;
        this.salesDate = 0;
        this.salesTime = 0;
        this.creditFee = 0;
    }

    public String getReserveNo()
    {
        return reserveNo;
    }

    public String getCardNo()
    {
        return cardNo;
    }

    public int getLimitDate()
    {
        return limitDate;
    }

    public int getId()
    {
        return id;
    }

    public String getTranid()
    {
        return tranid;
    }

    public String getApproved()
    {
        return approved;
    }

    public String getForwarded()
    {
        return forwarded;
    }

    public String getErrCode()
    {
        return errCode;
    }

    public String getErrInfo()
    {
        return errInfo;
    }

    public int getErrLevel()
    {
        return errLevel;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public int getDelDate()
    {
        return delDate;
    }

    public int getDelTime()
    {
        return delTime;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public String getAccessId()
    {
        return accessId;
    }

    public String getAccessPass()
    {
        return accessPass;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public int getAmount()
    {
        return amount;
    }

    public int getSalesFlag()
    {
        return salesFlag;
    }

    public int getSalesDate()
    {
        return salesDate;
    }

    public int getSalesTime()
    {
        return salesTime;
    }

    public int getCreditFee()
    {
        return creditFee;
    }

    public void setReserveNo(String reserveNo)
    {
        this.reserveNo = reserveNo;
    }

    public void setCardNo(String cardNo)
    {
        this.cardNo = cardNo;
    }

    public void setLimitDate(int limitDate)
    {
        this.limitDate = limitDate;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setTranid(String tranid)
    {
        this.tranid = tranid;
    }

    public void setApproved(String approved)
    {
        this.approved = approved;
    }

    public void setForwarded(String forwarded)
    {
        this.forwarded = forwarded;
    }

    public void setErrCode(String errCode)
    {
        this.errCode = errCode;
    }

    public void setErrInfo(String errInfo)
    {
        this.errInfo = errInfo;
    }

    public void setErrLevel(int errLevel)
    {
        this.errLevel = errLevel;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setDelDate(int delDate)
    {
        this.delDate = delDate;
    }

    public void setDelTime(int delTime)
    {
        this.delTime = delTime;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setAccessId(String accessId)
    {
        this.accessId = accessId;
    }

    public void setAccessPass(String accessPass)
    {
        this.accessPass = accessPass;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public void setSalesFlag(int salesFlag)
    {
        this.salesFlag = salesFlag;
    }

    public void setSalesDate(int salesDate)
    {
        this.salesDate = salesDate;
    }

    public void setSalesTime(int salesTime)
    {
        this.salesTime = salesTime;
    }

    public void setCreditFee(int creditFee)
    {
        this.creditFee = creditFee;
    }

    /**
     * 予約クレジットデータ取得
     * 
     * @param reserveNo 予約番号
     * @return
     */
    public boolean getData(String reserveNo)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM newRsvDB.hh_rsv_credit WHERE reserve_no = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, reserveNo );
            result = prestate.executeQuery();
            if ( result.next() == false )
            {
                return false;
            }
            return setData( result );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCredit.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 予約クレジットデータ取得
     * 
     * @param reserveNo 予約番号
     * @return
     */
    public boolean getData(Connection connection, String reserveNo)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM newRsvDB.hh_rsv_credit WHERE reserve_no = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, reserveNo );
            result = prestate.executeQuery();
            if ( result.next() )
            {
                this.reserveNo = result.getString( "reserve_no" );
                this.cardNo = result.getString( "card_no" );
                this.limitDate = result.getInt( "limit_date" );
                this.id = result.getInt( "id" );
                this.tranid = result.getString( "tranid" );
                this.approved = result.getString( "approved" );
                this.forwarded = result.getString( "forwarded" );
                this.errCode = result.getString( "err_code" );
                this.errInfo = result.getString( "err_info" );
                this.errLevel = result.getInt( "err_level" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.delDate = result.getInt( "del_date" );
                this.delTime = result.getInt( "del_time" );
                this.delFlag = result.getInt( "del_flag" );
                this.accessId = result.getString( "access_id" );
                this.accessPass = result.getString( "access_pass" );
                this.orderId = result.getString( "order_id" );
                this.amount = result.getInt( "amount" );
                this.salesFlag = result.getInt( "sales_flag" );
                this.salesDate = result.getInt( "sales_date" );
                this.salesTime = result.getInt( "sales_time" );
                this.creditFee = result.getInt( "credit_fee" );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCredit.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * 予約クレジットデータ設定
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
                this.reserveNo = result.getString( "reserve_no" );
                this.cardNo = result.getString( "card_no" );
                this.limitDate = result.getInt( "limit_date" );
                this.id = result.getInt( "id" );
                this.tranid = result.getString( "tranid" );
                this.approved = result.getString( "approved" );
                this.forwarded = result.getString( "forwarded" );
                this.errCode = result.getString( "err_code" );
                this.errInfo = result.getString( "err_info" );
                this.errLevel = result.getInt( "err_level" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.delDate = result.getInt( "del_date" );
                this.delTime = result.getInt( "del_time" );
                this.delFlag = result.getInt( "del_flag" );
                this.accessId = result.getString( "access_id" );
                this.accessPass = result.getString( "access_pass" );
                this.orderId = result.getString( "order_id" );
                this.amount = result.getInt( "amount" );
                this.salesFlag = result.getInt( "sales_flag" );
                this.salesDate = result.getInt( "sales_date" );
                this.salesTime = result.getInt( "sales_time" );
                this.creditFee = result.getInt( "credit_fee" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCredit.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 予約クレジットデータ挿入
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

        query = "INSERT newRsvDB.hh_rsv_credit SET ";
        query += " reserve_no=?";
        query += ", card_no=?";
        query += ", limit_date=?";
        query += ", id=?";
        query += ", tranid=?";
        query += ", approved=?";
        query += ", forwarded=?";
        query += ", err_code=?";
        query += ", err_info=?";
        query += ", err_level=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", del_date=?";
        query += ", del_time=?";
        query += ", del_flag=?";
        query += ", access_id=?";
        query += ", access_pass=?";
        query += ", order_id=?";
        query += ", amount=?";
        query += ", sales_flag=?";
        query += ", sales_date=?";
        query += ", sales_time=?";
        query += ", credit_fee=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.reserveNo );
            prestate.setString( i++, "" );
            prestate.setInt( i++, this.limitDate );
            prestate.setInt( i++, this.id );
            prestate.setString( i++, this.tranid );
            prestate.setString( i++, this.approved );
            prestate.setString( i++, this.forwarded );
            prestate.setString( i++, this.errCode );
            prestate.setString( i++, this.errInfo );
            prestate.setInt( i++, this.errLevel );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.delDate );
            prestate.setInt( i++, this.delTime );
            prestate.setInt( i++, this.delFlag );
            prestate.setString( i++, this.accessId );
            prestate.setString( i++, this.accessPass );
            prestate.setString( i++, this.orderId );
            prestate.setInt( i++, this.amount );
            prestate.setInt( i++, this.salesFlag );
            prestate.setInt( i++, this.salesDate );
            prestate.setInt( i++, this.salesTime );
            prestate.setInt( i++, this.creditFee );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCredit.insertData] Exception=" + e.toString() );
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
     * 予約クレジットデータ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param reserveNo 予約番号
     * @return
     */
    public boolean updateData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE newRsvDB.hh_rsv_credit SET ";
        query += " card_no=?";
        query += ", limit_date=?";
        query += ", id=?";
        query += ", tranid=?";
        query += ", approved=?";
        query += ", forwarded=?";
        query += ", err_code=?";
        query += ", err_info=?";
        query += ", err_level=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", del_date=?";
        query += ", del_time=?";
        query += ", del_flag=?";
        query += ", access_id=?";
        query += ", access_pass=?";
        query += ", order_id=?";
        query += ", amount=?";
        query += ", sales_flag=?";
        query += ", sales_date=?";
        query += ", sales_time=?";
        query += ", credit_fee=?";
        query += " WHERE reserve_no=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, "" );
            prestate.setInt( i++, this.limitDate );
            prestate.setInt( i++, this.id );
            prestate.setString( i++, this.tranid );
            prestate.setString( i++, this.approved );
            prestate.setString( i++, this.forwarded );
            prestate.setString( i++, this.errCode );
            prestate.setString( i++, this.errInfo );
            prestate.setInt( i++, this.errLevel );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.delDate );
            prestate.setInt( i++, this.delTime );
            prestate.setInt( i++, this.delFlag );
            prestate.setString( i++, this.accessId );
            prestate.setString( i++, this.accessPass );
            prestate.setString( i++, this.orderId );
            prestate.setInt( i++, this.amount );
            prestate.setInt( i++, this.salesFlag );
            prestate.setInt( i++, this.salesDate );
            prestate.setInt( i++, this.salesTime );
            prestate.setInt( i++, this.creditFee );
            prestate.setString( i++, this.reserveNo );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCredit.updateData] Exception=" + e.toString() );
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
     * 予約クレジットデータ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param reserveNo 予約番号
     * @return
     */
    public boolean updateData(Connection connection)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE newRsvDB.hh_rsv_credit SET ";
        query += " card_no=?";
        query += ", limit_date=?";
        query += ", id=?";
        query += ", tranid=?";
        query += ", approved=?";
        query += ", forwarded=?";
        query += ", err_code=?";
        query += ", err_info=?";
        query += ", err_level=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", del_date=?";
        query += ", del_time=?";
        query += ", del_flag=?";
        query += ", access_id=?";
        query += ", access_pass=?";
        query += ", order_id=?";
        query += ", amount=?";
        query += ", sales_flag=?";
        query += ", sales_date=?";
        query += ", sales_time=?";
        query += ", credit_fee=?";
        query += " WHERE reserve_no=?";

        try
        {
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, "" );
            prestate.setInt( i++, this.limitDate );
            prestate.setInt( i++, this.id );
            prestate.setString( i++, this.tranid );
            prestate.setString( i++, this.approved );
            prestate.setString( i++, this.forwarded );
            prestate.setString( i++, this.errCode );
            prestate.setString( i++, this.errInfo );
            prestate.setInt( i++, this.errLevel );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.delDate );
            prestate.setInt( i++, this.delTime );
            prestate.setInt( i++, this.delFlag );
            prestate.setString( i++, this.accessId );
            prestate.setString( i++, this.accessPass );
            prestate.setString( i++, this.orderId );
            prestate.setInt( i++, this.amount );
            prestate.setInt( i++, this.salesFlag );
            prestate.setInt( i++, this.salesDate );
            prestate.setInt( i++, this.salesTime );
            prestate.setInt( i++, this.creditFee );
            prestate.setString( i++, this.reserveNo );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCredit.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }
}
