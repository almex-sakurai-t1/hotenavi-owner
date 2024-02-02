package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ユーザスマホ課金履歴情報取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2012/09/27
 */
public class DataUserSpHistory implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -1726817903530959206L;
    private String            userId;
    private int               seq;
    private String            openId;
    private int               carrierKind;
    private int               registDate;
    private int               registTime;
    private int               delDate;
    private int               delTime;
    private int               registDatePay;
    private int               registTimePay;
    private int               delDatePay;
    private int               delTimePay;
    private int               freeMymenu;
    private int               chargeFlag;
    private int               delFlag;
    private String            token;
    private String            orderNo;
    private int               addDate;
    private int               addTime;

    public DataUserSpHistory()
    {
        userId = "";
        seq = 0;
        openId = "";
        carrierKind = 0;
        registDate = 0;
        registTime = 0;
        delDate = 0;
        delTime = 0;
        registDatePay = 0;
        registTimePay = 0;
        delDatePay = 0;
        delTimePay = 0;
        freeMymenu = 0;
        chargeFlag = 0;
        delFlag = 0;
        token = "";
        orderNo = "";
        addDate = 0;
        addTime = 0;

    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getSeq()
    {
        return seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public String getOpenId()
    {
        return openId;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    public int getCarrierKind()
    {
        return carrierKind;
    }

    public void setCarrierKind(int carrierKind)
    {
        this.carrierKind = carrierKind;
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

    public int getDelDate()
    {
        return delDate;
    }

    public void setDelDate(int delDate)
    {
        this.delDate = delDate;
    }

    public int getDelTime()
    {
        return delTime;
    }

    public void setDelTime(int delTime)
    {
        this.delTime = delTime;
    }

    public int getRegistDatePay()
    {
        return registDatePay;
    }

    public void setRegistDatePay(int registDatePay)
    {
        this.registDatePay = registDatePay;
    }

    public int getRegistTimePay()
    {
        return registTimePay;
    }

    public void setRegistTimePay(int registTimePay)
    {
        this.registTimePay = registTimePay;
    }

    public int getDelDatePay()
    {
        return delDatePay;
    }

    public void setDelDatePay(int delDatePay)
    {
        this.delDatePay = delDatePay;
    }

    public int getDelTimePay()
    {
        return delTimePay;
    }

    public void setDelTimePay(int delTimePay)
    {
        this.delTimePay = delTimePay;
    }

    public int getFreeMymenu()
    {
        return freeMymenu;
    }

    public void setFreeMymenu(int freeMymenu)
    {
        this.freeMymenu = freeMymenu;
    }

    public int getChargeFlag()
    {
        return chargeFlag;
    }

    public void setChargeFlag(int chargeFlag)
    {
        this.chargeFlag = chargeFlag;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public int getAddDate()
    {
        return addDate;
    }

    public void setAddDate(int addDate)
    {
        this.addDate = addDate;
    }

    public int getAddTime()
    {
        return addTime;
    }

    public void setAddTime(int addTime)
    {
        this.addTime = addTime;
    }

    /**
     * ユーザスマホ課金情報取得
     * 
     * @param userId ユーザユーザID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_sp_history WHERE user_id = ? AND seq = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.seq = result.getInt( "seq" );
                    this.openId = result.getString( "open_id" );
                    this.carrierKind = result.getInt( "carrier_kind" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.delDate = result.getInt( "del_date" );
                    this.delTime = result.getInt( "del_time" );
                    this.registDatePay = result.getInt( "regist_date_pay" );
                    this.registTimePay = result.getInt( "regist_time_pay" );
                    this.delDatePay = result.getInt( "del_date_pay" );
                    this.delTimePay = result.getInt( "del_time_pay" );
                    this.freeMymenu = result.getInt( "free_mymenu" );
                    this.chargeFlag = result.getInt( "charge_flag" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.token = result.getString( "token" );
                    this.orderNo = result.getString( "order_no" );
                    this.addDate = result.getInt( "add_date" );
                    this.addTime = result.getInt( "add_time" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSpHistory.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザスマホ課金履歴データ設定
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.userId = result.getString( "user_id" );
                this.seq = result.getInt( "seq" );
                this.openId = result.getString( "open_id" );
                this.carrierKind = result.getInt( "carrier_kind" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.delDate = result.getInt( "del_date" );
                this.delTime = result.getInt( "del_time" );
                this.registDatePay = result.getInt( "regist_date_pay" );
                this.registTimePay = result.getInt( "regist_time_pay" );
                this.delDatePay = result.getInt( "del_date_pay" );
                this.delTimePay = result.getInt( "del_time_pay" );
                this.freeMymenu = result.getInt( "free_mymenu" );
                this.chargeFlag = result.getInt( "charge_flag" );
                this.delFlag = result.getInt( "del_flag" );
                this.token = result.getString( "token" );
                this.orderNo = result.getString( "order_no" );
                this.addDate = result.getInt( "add_date" );
                this.addTime = result.getInt( "add_time" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSpHistory.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザスマホ課金履歴データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと。seqはオートインクリメント"
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

        query = "INSERT hh_user_sp_history SET ";
        query = query + " user_id = ?,";
        query = query + " seq = 0,";
        query = query + " open_id = ?,";
        query = query + " carrier_kind = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " del_date = ?,";
        query = query + " del_time = ?,";
        query = query + " regist_date_pay = ?,";
        query = query + " regist_time_pay = ?,";
        query = query + " del_date_pay = ?,";
        query = query + " del_time_pay = ?,";
        query = query + " free_mymenu = ?,";
        query = query + " charge_flag = ?,";
        query = query + " del_flag = ?,";
        query = query + " token = ?,";
        query = query + " order_no = ?,";
        query = query + " add_date = ?,";
        query = query + " add_time = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setString( 2, this.openId );
            prestate.setInt( 3, this.carrierKind );
            prestate.setInt( 4, this.registDate );
            prestate.setInt( 5, this.registTime );
            prestate.setInt( 6, this.delDate );
            prestate.setInt( 7, this.delTime );
            prestate.setInt( 8, this.registDatePay );
            prestate.setInt( 9, this.registTimePay );
            prestate.setInt( 10, this.delDatePay );
            prestate.setInt( 11, this.delTimePay );
            prestate.setInt( 12, this.freeMymenu );
            prestate.setInt( 13, this.chargeFlag );
            prestate.setInt( 14, this.delFlag );
            prestate.setString( 15, this.token );
            prestate.setString( 16, this.orderNo );
            prestate.setInt( 17, this.addDate );
            prestate.setInt( 18, this.addTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSpHistory.insertData] Exception=" + e.toString() );
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
     * ユーザスマホ課金履歴データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_sp_history SET ";
        query = query + " open_id = ?,";
        query = query + " carrier_kind = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " del_date = ?,";
        query = query + " del_time = ?,";
        query = query + " regist_date_pay = ?,";
        query = query + " regist_time_pay = ?,";
        query = query + " del_date_pay = ?,";
        query = query + " del_time_pay = ?,";
        query = query + " free_mymenu = ?,";
        query = query + " charge_flag = ?,";
        query = query + " del_flag = ?,";
        query = query + " token = ?,";
        query = query + " order_no = ?,";
        query = query + " add_date = ?,";
        query = query + " add_time = ?";
        query = query + " WHERE user_id = ?";
        query = query + " AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.openId );
            prestate.setInt( 2, this.carrierKind );
            prestate.setInt( 3, this.registDate );
            prestate.setInt( 4, this.registTime );
            prestate.setInt( 5, this.delDate );
            prestate.setInt( 6, this.delTime );
            prestate.setInt( 7, this.registDatePay );
            prestate.setInt( 8, this.registTimePay );
            prestate.setInt( 9, this.delDatePay );
            prestate.setInt( 10, this.delTimePay );
            prestate.setInt( 11, this.freeMymenu );
            prestate.setInt( 12, this.chargeFlag );
            prestate.setInt( 13, this.delFlag );
            prestate.setString( 14, this.token );
            prestate.setString( 15, this.orderNo );
            prestate.setInt( 16, this.addDate );
            prestate.setInt( 17, this.addTime );
            prestate.setString( 18, userId );
            prestate.setInt( 19, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSpHistory.updateData] Exception=" + e.toString() );
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
