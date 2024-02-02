package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 自動発行クーポン管理(ap_auto_coupon)取得クラス
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/10/23
 */
public class DataApAutoCoupon implements Serializable
{
    public static final String TABLE = "ap_auto_coupon";
    private int                id;                      // ハピホテホテルID
    private int                couponSeq;               // クーポン連番（自動採番）
    private int                couponKind;              // 1:割引, 2:商品
    private int                discountCode;            // 割引コード（ホストより）
    private int                startDate;               // 開始日付
    private int                startTime;               // 開始時刻
    private int                endDate;                 // 終了日付
    private int                endTime;                 // 終了時刻
    private String             goodsDetail;             // 商品明細（商品名と個数は.区切り。明細行は改行区切り）
    private String             dispText;                // 表示内容
    private String             printText;               // 印字内容（印字行ごとに改行区切りを入れる）
    private int                period;                  // 有効期限(YYYYMMDD)
    private int                useCount;                // 0:利用回数制限なし
    private int                useStartDay;             // 0:当日から ,n日後から使用可能（未使用）
    private int                useEndDay;               // 0:有効期限まで,n日後まで（未使用）
    private int                delFlag;                 // 削除フラグ…1:表示なし
    private int                method;                  // 割引方法･･･0:％割引,1:金額割引
    private int                mode_flag;               // 料金モード制御･･･0:料金モード制御なし,1:料金モード制御あり
    private int                customFlag;              // 顧客割引併用･･･0:併用可,1:併用不可,2:室料のみ不可,3:商品のみ不可
    private int                discountPoint;           // 割引箇所･･･0:基本,1:室料(税抜),2:室料(延長込み),3:小計,4:合計

    /**
     * データを初期化します。
     */
    public DataApAutoCoupon()
    {
        this.id = 0;
        this.couponSeq = 0;
        this.couponKind = 0;
        this.discountCode = 0;
        this.startDate = 0;
        this.endDate = 0;
        this.goodsDetail = "";
        this.dispText = "";
        this.printText = "";
        this.period = 0;
        this.useCount = 0;
        this.useStartDay = 0;
        this.useEndDay = 0;
        this.delFlag = 0;
        this.method = 0;
        this.mode_flag = 0;
        this.customFlag = 0;
        this.discountPoint = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getCouponSeq()
    {
        return couponSeq;
    }

    public int getCouponKind()
    {
        return couponKind;
    }

    public int getDiscountCode()
    {
        return discountCode;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getStartTime()
    {
        return startTime;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public int getEndTime()
    {
        return endTime;
    }

    public String getGoodsDetail()
    {
        return goodsDetail;
    }

    public String getDispText()
    {
        return dispText;
    }

    public String getPrintText()
    {
        return printText;
    }

    public int getPeriod()
    {
        return period;
    }

    public int getUseCount()
    {
        return useCount;
    }

    public int getUseStartDay()
    {
        return useStartDay;
    }

    public int getUseEndDay()
    {
        return useEndDay;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public int getMethod()
    {
        return method;
    }

    public int getMode_flag()
    {
        return mode_flag;
    }

    public int getCustomFlag()
    {
        return customFlag;
    }

    public int getDiscountPoint()
    {
        return discountPoint;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setCouponSeq(int couponSeq)
    {
        this.couponSeq = couponSeq;
    }

    public void setCouponKind(int couponKind)
    {
        this.couponKind = couponKind;
    }

    public void setDiscountCode(int discountCode)
    {
        this.discountCode = discountCode;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setStartTime(int startTime)
    {
        this.startTime = startTime;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setEndTime(int endTime)
    {
        this.endTime = endTime;
    }

    public void setGoodsDetail(String goodsDetail)
    {
        this.goodsDetail = goodsDetail;
    }

    public void setDispText(String dispText)
    {
        this.dispText = dispText;
    }

    public void setPrintText(String printText)
    {
        this.printText = printText;
    }

    public void setPeriod(int period)
    {
        this.period = period;
    }

    public void setUseCount(int useCount)
    {
        this.useCount = useCount;
    }

    public void setUseStartDay(int useStartDay)
    {
        this.useStartDay = useStartDay;
    }

    public void setUseEndDay(int useEndDay)
    {
        this.useEndDay = useEndDay;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setMethod(int method)
    {
        this.method = method;
    }

    public void setMode_flag(int mode_flag)
    {
        this.mode_flag = mode_flag;
    }

    public void setCustomFlag(int customFlag)
    {
        this.customFlag = customFlag;
    }

    public void setDiscountPoint(int discountPoint)
    {
        this.discountPoint = discountPoint;
    }

    /****
     * 自動発行クーポン管理(ap_auto_coupon)取得
     * 
     * @param id ハピホテホテルID
     * @param couponSeq クーポン連番（自動採番）
     * @return
     */
    public boolean getData(int id, int couponSeq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_auto_coupon WHERE id = ? AND coupon_seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, couponSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.couponSeq = result.getInt( "coupon_seq" );
                    this.couponKind = result.getInt( "coupon_kind" );
                    this.discountCode = result.getInt( "discount_code" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.goodsDetail = result.getString( "goods_detail" );
                    this.dispText = result.getString( "disp_text" );
                    this.printText = result.getString( "print_text" );
                    this.period = result.getInt( "period" );
                    this.useCount = result.getInt( "use_count" );
                    this.useStartDay = result.getInt( "use_start_date" );
                    this.useEndDay = result.getInt( "use_end_date" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.method = result.getInt( "method" );
                    this.mode_flag = result.getInt( "mode_flag" );
                    this.customFlag = result.getInt( "custom_flag" );
                    this.discountPoint = result.getInt( "discount_point" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApAutoCoupon.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 自動発行クーポン管理(ap_auto_coupon)設定
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
                this.couponKind = result.getInt( "coupon_kind" );
                this.discountCode = result.getInt( "discount_code" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.goodsDetail = result.getString( "goods_detail" );
                this.dispText = result.getString( "disp_text" );
                this.printText = result.getString( "print_text" );
                this.period = result.getInt( "period" );
                this.useCount = result.getInt( "use_count" );
                this.useStartDay = result.getInt( "use_start_date" );
                this.useEndDay = result.getInt( "use_end_date" );
                this.delFlag = result.getInt( "del_flag" );
                this.method = result.getInt( "method" );
                this.mode_flag = result.getInt( "mode_flag" );
                this.customFlag = result.getInt( "custom_flag" );
                this.discountPoint = result.getInt( "discount_point" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApAutoCoupon.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 自動発行クーポン管理(ap_auto_coupon)挿入
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

        query = "INSERT ap_auto_coupon SET ";
        query += " id=?";
        query += ", coupon_seq=?";
        query += ", coupon_kind=?";
        query += ", discount_code=?";
        query += ", start_date=?";
        query += ", end_date=?";
        query += ", goods_detail=?";
        query += ", disp_text=?";
        query += ", print_text=?";
        query += ", period=?";
        query += ", use_count=?";
        query += ", use_start_date=?";
        query += ", use_end_date=?";
        query += ", del_flag=?";
        query += ", method=?";
        query += ", mode_flag=?";
        query += ", custom_flag=?";
        query += ", discount_point=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.couponSeq );
            prestate.setInt( i++, this.couponKind );
            prestate.setInt( i++, this.discountCode );
            prestate.setInt( i++, this.startDate );
            prestate.setInt( i++, this.endDate );
            prestate.setString( i++, this.goodsDetail );
            prestate.setString( i++, this.dispText );
            prestate.setString( i++, this.printText );
            prestate.setInt( i++, this.period );
            prestate.setInt( i++, this.useCount );
            prestate.setInt( i++, this.useStartDay );
            prestate.setInt( i++, this.useEndDay );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.method );
            prestate.setInt( i++, this.mode_flag );
            prestate.setInt( i++, this.customFlag );
            prestate.setInt( i++, this.discountPoint );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApAutoCoupon.insertData] Exception=" + e.toString() );
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
     * 自動発行クーポン管理(ap_auto_coupon)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ハピホテホテルID
     * @param couponSeq クーポン連番（自動採番）
     * @return
     */
    public boolean updateData(int id, int couponSeq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_auto_coupon SET ";
        query += " coupon_kind=?";
        query += ", discount_code=?";
        query += ", start_date=?";
        query += ", end_date=?";
        query += ", goods_detail=?";
        query += ", disp_text=?";
        query += ", print_text=?";
        query += ", period=?";
        query += ", use_count=?";
        query += ", use_start_date=?";
        query += ", use_end_date=?";
        query += ", del_flag=?";
        query += ", method=?";
        query += ", mode_flag=?";
        query += ", custom_flag=?";
        query += ", discount_point=?";
        query += " WHERE id=? AND coupon_seq=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.couponKind );
            prestate.setInt( i++, this.discountCode );
            prestate.setInt( i++, this.startDate );
            prestate.setInt( i++, this.endDate );
            prestate.setString( i++, this.goodsDetail );
            prestate.setString( i++, this.dispText );
            prestate.setString( i++, this.printText );
            prestate.setInt( i++, this.period );
            prestate.setInt( i++, this.useCount );
            prestate.setInt( i++, this.useStartDay );
            prestate.setInt( i++, this.useEndDay );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.method );
            prestate.setInt( i++, this.mode_flag );
            prestate.setInt( i++, this.customFlag );
            prestate.setInt( i++, this.discountPoint );
            prestate.setInt( i++, id );
            prestate.setInt( i++, couponSeq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApAutoCoupon.updateData] Exception=" + e.toString() );
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
