package jp.happyhotel.touch;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApAutoCoupon;
import jp.happyhotel.data.DataApAutoCouponDetail;
import jp.happyhotel.data.DataApUserAutoCoupon;
import jp.happyhotel.data.DataHotelCoupon;
import jp.happyhotel.data.DataMasterCoupon;
import jp.happyhotel.data.DataUserCoupon;

public class TouchUserCoupon implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 2131931557947189863L;
    int                       userCouponCount;
    int                       userAutoCouponCount;
    boolean                   isAutoUserCoupon;
    boolean                   isUserCoupon;
    DataApAutoCoupon[]        coupon;
    DataApAutoCouponDetail[]  couponDetail;
    DataApUserAutoCoupon[]    userAutoCoupon;
    DataUserCoupon            userCoupon;
    DataHotelCoupon           hotelCoupon;
    DataMasterCoupon          masterCoupon;
    int[]                     planNoList;
    int[]                     chargeModeList;
    int[]                     discountList;

    public TouchUserCoupon()
    {
        userCouponCount = 0;
        userAutoCouponCount = 0;
        isAutoUserCoupon = false;
        isUserCoupon = false;
    }

    public int getUserCouponCount()
    {
        return userCouponCount;
    }

    public int getUserAutoCouponCount()
    {
        return userAutoCouponCount;
    }

    public boolean isAutoUserCoupon()
    {
        return isAutoUserCoupon;
    }

    public boolean isUserCoupon()
    {
        return isUserCoupon;
    }

    public DataApAutoCoupon[] getCoupon()
    {
        return coupon;
    }

    public DataApAutoCouponDetail[] getCouponDetail()
    {
        return couponDetail;
    }

    public DataApUserAutoCoupon[] getUserAutoCoupon()
    {
        return userAutoCoupon;
    }

    public DataUserCoupon getUserCoupon()
    {
        return userCoupon;
    }

    public DataHotelCoupon getHotelCoupon()
    {
        return hotelCoupon;
    }

    public int[] getPlanNoList()
    {
        return planNoList;
    }

    public int[] getChargeModeList()
    {
        return chargeModeList;
    }

    public int[] getDiscountList()
    {
        return discountList;
    }

    public void setUserCouponCount(int userCouponCount)
    {
        this.userCouponCount = userCouponCount;
    }

    public void setUserAutoCouponCount(int userAutoCouponCount)
    {
        this.userAutoCouponCount = userAutoCouponCount;
    }

    public void setAutoUserCoupon(boolean isAutoUserCoupon)
    {
        this.isAutoUserCoupon = isAutoUserCoupon;
    }

    public void setUserCoupon(boolean isUserCoupon)
    {
        this.isUserCoupon = isUserCoupon;
    }

    public void setCoupon(DataApAutoCoupon[] coupon)
    {
        this.coupon = coupon;
    }

    public void setCouponDetail(DataApAutoCouponDetail[] couponDetail)
    {
        this.couponDetail = couponDetail;
    }

    public void setUserAutoCoupon(DataApUserAutoCoupon[] userAutoCoupon)
    {
        this.userAutoCoupon = userAutoCoupon;
    }

    public void setUserCoupon(DataUserCoupon userCoupon)
    {
        this.userCoupon = userCoupon;
    }

    public void setHotelCoupon(DataHotelCoupon hotelCoupon)
    {
        this.hotelCoupon = hotelCoupon;
    }

    public DataMasterCoupon getMasterCoupon()
    {
        return masterCoupon;
    }

    public void setMasterCoupon(DataMasterCoupon masterCoupon)
    {
        this.masterCoupon = masterCoupon;
    }

    public void setPlanNoList(int[] planNoList)
    {
        this.planNoList = planNoList;
    }

    public void setChargeModeList(int[] chargeModeList)
    {
        this.chargeModeList = chargeModeList;
    }

    public void setDiscountList(int[] discountList)
    {
        this.discountList = discountList;
    }

    /**
     * ユーザクーポンを取得する
     * 
     * @param userId
     * @param id
     * @return
     */
    public boolean getCouponData(String userId, int id)
    {
        boolean ret = false;

        this.userCoupon = null;
        this.hotelCoupon = null;
        this.masterCoupon = null;
        this.isAutoUserCoupon = getUserAutoCoupon( userId, id );

        // 2015.03.18 事業部要望によりタッチ登録時に旧クーポンは表示しないように変更 tashiro
        // this.isUserCoupon = getUserCoupon( id, userId );

        if ( isAutoUserCoupon != false || isUserCoupon != false )
        {
            ret = true;
        }
        return ret;
    }

    /**
     * ユーザクーポンを取得する
     * 
     * @param userId
     * @param id
     * @return
     */
    public boolean getDeclareCouponData(String userId, int id, int couponNo)
    {

        boolean ret = false;

        this.userCoupon = null;
        this.hotelCoupon = null;
        this.masterCoupon = null;
        this.isUserCoupon = getUserCoupon( id, couponNo, userId );
        if ( isUserCoupon != false )
        {
            ret = true;
        }

        return ret;
    }

    /**
     * ユーザクーポンを取得する
     * 
     * @param userId
     * @param id
     * @return
     */
    public boolean getDeclareCouponData(String userId, int id)
    {
        boolean ret = false;

        this.userCoupon = null;
        this.hotelCoupon = null;
        this.masterCoupon = null;
        this.isUserCoupon = getUserCoupon( id, userId );
        if ( isUserCoupon != false )
        {
            ret = true;
        }

        return ret;
    }

    /**
     * ユーザの自動適用クーポンを取得する
     * 
     * @param userId
     * @param id
     * @return
     */
    public boolean getUserAutoCoupon(String userId, int id)
    {
        boolean ret = false;

        int i = 0;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM ap_user_auto_coupon A";
        query += " INNER JOIN ap_auto_coupon B ON A.id = B.id AND A.coupon_seq = B.coupon_seq AND B.del_flag=0";
        query += " INNER JOIN ap_hotel_setting C ON A.id = C.id AND C.auto_coupon_flag=1";
        query += " WHERE A.user_id = ?";
        query += " AND A.id = ?";
        query += " AND A.start_date <= ?";
        query += " AND A.end_date >= ?";
        query += " AND A.used_flag = 0";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, id );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    count = result.getRow();
                }
                this.userAutoCouponCount = count;

                result.beforeFirst();
                coupon = new DataApAutoCoupon[count];
                userAutoCoupon = new DataApUserAutoCoupon[count];

                while( result.next() != false )
                {
                    coupon[i] = new DataApAutoCoupon();
                    userAutoCoupon[i] = new DataApUserAutoCoupon();
                    coupon[i].setData( result );
                    userAutoCoupon[i].setData( result );
                    i++;
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TouchUserCoupon.getCouponData()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /***
     * ユーザの自動適用クーポンを取得（クーポン番号から取得）
     * 
     * @param userId
     * @param id
     * @param seq
     * @return
     */
    public boolean getCouponData(String userId, int id, int seq)
    {
        boolean ret = false;

        int i = 0;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM ap_user_auto_coupon A";
        query += " INNER JOIN ap_auto_coupon B ON A.id = B.id AND A.coupon_seq = B.coupon_seq AND B.del_flag=0";
        query += " WHERE A.user_id = ?";
        query += " AND A.id = ?";
        query += " AND A.coupon_seq = ?";
        query += " AND A.start_date <= ?";
        query += " AND A.end_date >= ?";
        query += " AND A.used_flag = 0";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, id );
            prestate.setInt( 3, seq );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 5, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    count = result.getRow();
                }
                this.userAutoCouponCount = count;

                result.beforeFirst();
                coupon = new DataApAutoCoupon[count];
                userAutoCoupon = new DataApUserAutoCoupon[count];

                while( result.next() != false )
                {
                    coupon[i] = new DataApAutoCoupon();
                    userAutoCoupon[i] = new DataApUserAutoCoupon();
                    coupon[i].setData( result );
                    userAutoCoupon[i].setData( result );
                    i++;
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TouchUserCoupon.getCouponData()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /***
     * ユーザの自動適用クーポンを取得（クーポン番号から取得）
     * 
     * @param userId
     * @param id
     * @param seq
     * @return
     */
    public boolean getCouponData(String userId, int id, int couponSeq, int seq)
    {
        boolean ret = false;

        int i = 0;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM ap_user_auto_coupon A";
        query += " INNER JOIN ap_auto_coupon B ON A.id = B.id AND A.coupon_seq = B.coupon_seq AND B.del_flag=0";
        query += " WHERE A.user_id = ?";
        query += " AND A.id = ?";
        query += " AND A.coupon_seq = ?";
        query += " AND A.seq = ?";
        query += " AND A.start_date <= ?";
        query += " AND A.end_date >= ?";
        query += " AND A.used_flag = 0";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, id );
            prestate.setInt( 3, couponSeq );
            prestate.setInt( 4, seq );
            prestate.setInt( 5, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 6, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    count = result.getRow();
                }
                this.userAutoCouponCount = count;
                result.beforeFirst();
                coupon = new DataApAutoCoupon[count];
                userAutoCoupon = new DataApUserAutoCoupon[count];

                while( result.next() != false )
                {
                    coupon[i] = new DataApAutoCoupon();
                    userAutoCoupon[i] = new DataApUserAutoCoupon();
                    coupon[i].setData( result );
                    userAutoCoupon[i].setData( result );
                    i++;
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TouchUserCoupon.getCouponData()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /***
     * ユーザの自動適用クーポンを取得（クーポン番号から取得）
     * 
     * @param userId
     * @param id
     * @param seq
     * @return
     */
    public boolean getCouponDetailData(int id, int couponSeq)
    {
        boolean ret = false;

        int i = 0;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM ap_auto_coupon_detail ";
        query += " WHERE id = ?";
        query += " AND coupon_seq = ?";
        query += " ORDER BY plan_no, charge_mode ";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, couponSeq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    count = result.getRow();
                }
                result.beforeFirst();
                couponDetail = new DataApAutoCouponDetail[count];
                planNoList = new int[count];
                chargeModeList = new int[count];
                discountList = new int[count];

                while( result.next() != false )
                {
                    couponDetail[i] = new DataApAutoCouponDetail();
                    couponDetail[i].setData( result );
                    planNoList[i] = couponDetail[i].getPlanNo();
                    chargeModeList[i] = couponDetail[i].getChargeMode();
                    discountList[i] = couponDetail[i].getDiscount();
                    i++;
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TouchUserCoupon.getCouponData()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /**
     * 申告制クーポンを取得する(ID、couponNo、ユーザIDから)
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号(hh_master_coupon.seq)
     * @param userId ユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUserCoupon(int id, int couponNo, String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( (id < 0) || (userId == null) || (userId.equals( "" ) != false) )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_coupon A";
        query += " INNER JOIN hh_master_coupon B ON A.id = B.id AND  A.seq = B.coupon_no";
        query += " INNER JOIN hh_hotel_coupon C ON C.id = B.id AND C.seq = B.coupon_no AND del_flag = 0";
        query = query + " WHERE A.id = ?";
        query = query + " AND A.coupon_no = ?";
        query = query + " AND A.user_id = ?";
        query = query + " AND A.start_date <= ?";
        query = query + " AND A.end_date >= ?";
        // query = query + " AND A.used_flag = 0";
        query = query + " ORDER BY A.print_date DESC, A.print_time DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, couponNo );
            prestate.setString( 3, userId );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 5, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.userCouponCount = result.getRow();
                }
                this.userCoupon = new DataUserCoupon();
                this.hotelCoupon = new DataHotelCoupon();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // クーポン情報の設定
                    this.userCoupon.setData( result );
                    this.hotelCoupon.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserCoupon] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        if ( userCouponCount != 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        return(ret);
    }

    /**
     * 申告制クーポンを取得する(ID、seq、ユーザIDから)
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号(hh_master_coupon.seq)
     * @param userId ユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUserCoupon(int id, String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( (id < 0) || (userId == null) || (userId.equals( "" ) != false) )
        {
            return(false);
        }
        // まずホテルクーポンとマスタクーポンを取得
        query = " SELECT * FROM hh_hotel_coupon A ";
        query += " INNER JOIN hh_master_coupon B ON A.id = B.id AND A.seq = B.coupon_no ";
        query += " AND B.service_flag < 2";
        query += " AND B.available = 0";
        query += " WHERE A.id = ?";
        query += " AND A.del_flag <> 1 ";
        query += " AND A.start_date <= ?";
        query += " AND A.end_date >= ?";
        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.hotelCoupon = new DataHotelCoupon();
                    this.masterCoupon = new DataMasterCoupon();

                    // クーポン情報の設定
                    this.hotelCoupon.setData( result );
                    this.masterCoupon.setData( result );
                    ret = this.getUserCoupon( connection, id, this.masterCoupon.getSeq(), userId );
                    if ( ret != false )
                    {
                        this.userCouponCount = 1;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserCoupon] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * クーポンシリアル番号を取得する(ID、seq、発行日から)
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号(hh_master_coupon.seq)
     * @param userId ユーザーID
     * @param printDate 発行日
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUserCoupon(Connection connection, int id, int seq, String userId)
    {
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( (id < 0) || (userId == null) || (userId.equals( "" ) != false) )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_coupon";
        query += " WHERE id = ?";
        query += " AND seq = ?";
        query += " AND user_id = ?";
        query += " AND start_date <= ? ";
        query += " AND end_date >= ? ";
        query += " AND used_flag = 0";
        query = query + " ORDER BY print_date DESC, print_time DESC";

        ret = false;

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );
            prestate.setString( 3, userId );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 5, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.userCouponCount = result.getRow();
                }
                this.userCoupon = new DataUserCoupon();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // クーポン情報の設定
                    this.userCoupon.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserCoupon] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        if ( userCouponCount != 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        return(ret);
    }

    /**
     * 申告制クーポンを取得する(ID、couponNo、ユーザIDから)
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号(hh_master_coupon.seq)
     * @param userId ユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateDeclareCoupon(int id, int couponNo, int seq, int used)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        int result = 0;

        if ( id < 0 || couponNo < 0 || seq < 0 || used < 0 )
        {
            return(false);
        }
        query = "UPDATE * FROM hh_user_coupon";
        query += " SET used_flag = ?";
        query += " WHERE id = ?";
        query += " AND coupon_no = ?";
        query += " AND seq = ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, used );
            prestate.setInt( 2, id );
            prestate.setInt( 3, couponNo );
            prestate.setInt( 4, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserCoupon] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 自動適用クーポンの発行処理
     * 
     * @param daac
     * @param userId
     * @return
     */
    public boolean registUserAutoCoupon(DataApAutoCoupon daac, String userId)
    {
        DataApUserAutoCoupon dauac = new DataApUserAutoCoupon();
        boolean ret = false;

        // 取得したホテルの自動適用クーポンデータから、ユーザの自動適用クーポンデータへ書き込み。
        dauac.setId( daac.getId() );
        dauac.setCouponSeq( daac.getCouponSeq() );
        dauac.setSeq( 0 );
        dauac.setUserId( userId );
        dauac.setStartDate( daac.getStartDate() );
        dauac.setEndDate( daac.getEndDate() );
        dauac.setPrintDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        dauac.setPrintTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        ret = dauac.insertData();

        return ret;
    }

}
