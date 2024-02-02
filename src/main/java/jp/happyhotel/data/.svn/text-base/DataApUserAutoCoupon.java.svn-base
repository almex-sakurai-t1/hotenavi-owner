package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 自動発行クーポン発行履歴(ap_user_auto_coupon)取得クラス
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/10/23
 */
public class DataApUserAutoCoupon implements Serializable
{
    public static final String TABLE = "ap_user_auto_coupon";
    private int                id;                           // ハピホテホテルID
    private int                couponSeq;                    // ap_auto_coupon.coupon_seq
    private int                seq;                          // 連番
    private String             userId;                       // ユーザーNo
    private int                startDate;                    // クーポン利用可能開始日（YYYYMMDD)
    private int                endDate;                      // クーポン利用可能終了日（YYYYMMDD)
    private int                printDate;                    // クーポン獲得日付
    private int                printTime;                    // クーポン獲得時刻
    private int                delFlag;                      // 1:取消
    private int                usedFlag;                     // 0:未使用,1:利用
    private int                usedDate;                     // 利用日付(YYYYMMDD)
    private int                usedTime;                     // 利用時刻(HHMMSS)

    /**
     * データを初期化します。
     */
    public DataApUserAutoCoupon()
    {
        this.id = 0;
        this.couponSeq = 0;
        this.seq = 0;
        this.userId = "";
        this.startDate = 0;
        this.endDate = 0;
        this.printDate = 0;
        this.printTime = 0;
        this.delFlag = 0;
        this.usedFlag = 0;
        this.usedDate = 0;
        this.usedTime = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getCouponSeq()
    {
        return couponSeq;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public int getPrintDate()
    {
        return printDate;
    }

    public int getPrintTime()
    {
        return printTime;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public int getUsedFlag()
    {
        return usedFlag;
    }

    public int getUsedDate()
    {
        return usedDate;
    }

    public int getUsedTime()
    {
        return usedTime;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setCouponSeq(int couponSeq)
    {
        this.couponSeq = couponSeq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setPrintDate(int printDate)
    {
        this.printDate = printDate;
    }

    public void setPrintTime(int printTime)
    {
        this.printTime = printTime;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setUsedFlag(int usedFlag)
    {
        this.usedFlag = usedFlag;
    }

    public void setUsedDate(int usedDate)
    {
        this.usedDate = usedDate;
    }

    public void setUsedTime(int usedTime)
    {
        this.usedTime = usedTime;
    }

    /****
     * 自動発行クーポン発行履歴(ap_user_auto_coupon)取得
     * 
     * @param id ハピホテホテルID
     * @param couponSeq ap_auto_coupon.coupon_seq
     * @param seq 連番
     * @return
     */
    public boolean getData(int id, int couponSeq, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_user_auto_coupon WHERE id = ? AND coupon_seq = ? AND seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, couponSeq );
            prestate.setInt( 3, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.couponSeq = result.getInt( "coupon_seq" );
                    this.seq = result.getInt( "seq" );
                    this.userId = result.getString( "user_id" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.printDate = result.getInt( "print_date" );
                    this.printTime = result.getInt( "print_time" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.usedFlag = result.getInt( "used_flag" );
                    this.usedDate = result.getInt( "used_date" );
                    this.usedTime = result.getInt( "used_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUserAutoCoupon.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 自動発行クーポン発行履歴(ap_user_auto_coupon)設定
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
                this.couponSeq = result.getInt( "coupon_seq" );
                this.seq = result.getInt( "seq" );
                this.userId = result.getString( "user_id" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.printDate = result.getInt( "print_date" );
                this.printTime = result.getInt( "print_time" );
                this.delFlag = result.getInt( "del_flag" );
                this.usedFlag = result.getInt( "used_flag" );
                this.usedDate = result.getInt( "used_date" );
                this.usedTime = result.getInt( "used_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUserAutoCoupon.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 自動発行クーポン発行履歴(ap_user_auto_coupon)挿入
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

        query = "INSERT ap_user_auto_coupon SET ";
        query += " id=?";
        query += ", coupon_seq=?";
        query += ", seq=?";
        query += ", user_id=?";
        query += ", start_date=?";
        query += ", end_date=?";
        query += ", print_date=?";
        query += ", print_time=?";
        query += ", del_flag=?";
        query += ", used_flag=?";
        query += ", used_date=?";
        query += ", used_time=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.couponSeq );
            prestate.setInt( i++, this.seq );
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.startDate );
            prestate.setInt( i++, this.endDate );
            prestate.setInt( i++, this.printDate );
            prestate.setInt( i++, this.printTime );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.usedFlag );
            prestate.setInt( i++, this.usedDate );
            prestate.setInt( i++, this.usedTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUserAutoCoupon.insertData] Exception=" + e.toString() );
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
     * 自動発行クーポン発行履歴(ap_user_auto_coupon)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ハピホテホテルID
     * @param couponSeq ap_auto_coupon.coupon_seq
     * @param seq 連番
     * @return
     */
    public boolean updateData(int id, int couponSeq, int seq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_user_auto_coupon SET ";
        query += " user_id=?";
        query += ", start_date=?";
        query += ", end_date=?";
        query += ", print_date=?";
        query += ", print_time=?";
        query += ", del_flag=?";
        query += ", used_flag=?";
        query += ", used_date=?";
        query += ", used_time=?";
        query += " WHERE id=? AND coupon_seq=? AND seq=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.startDate );
            prestate.setInt( i++, this.endDate );
            prestate.setInt( i++, this.printDate );
            prestate.setInt( i++, this.printTime );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.usedFlag );
            prestate.setInt( i++, this.usedDate );
            prestate.setInt( i++, this.usedTime );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.couponSeq );
            prestate.setInt( i++, this.seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUserAutoCoupon.updateData] Exception=" + e.toString() );
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
