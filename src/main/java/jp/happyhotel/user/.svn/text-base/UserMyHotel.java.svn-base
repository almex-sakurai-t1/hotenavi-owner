/*
 * @(#)UserMyHotel.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ユーザマイホテル取得・更新クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserMyHotel;

/**
 * ユーザマイホテル取得・更新クラス。 ユーザのマイホテル情報を取得・更新する機能を提供する
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/27
 */
public class UserMyHotel implements Serializable
{
    /**
     *
     */
    private static final long    serialVersionUID = 9160510941585039404L;

    private int                  userMyHotelCount;
    private DataUserMyHotel[]    userMyHotel;
    private int                  customMyHotelCount;
    private Map<Integer, String> customMyHotel;

    /**
     * データを初期化します。
     */
    public UserMyHotel()
    {
        userMyHotelCount = 0;
    }

    /** ユーザマイホテル情報件数取得 **/
    public int getCount()
    {
        return(userMyHotelCount);
    }

    /** ユーザマイホテル情報取得 **/
    public DataUserMyHotel[] getMyHotel()
    {
        return(userMyHotel);
    }

    /** 顧客登録済マイホテル情報取得 **/
    public Map<Integer, String> getCustomMyHotel()
    {
        return(customMyHotel);
    }

    /**
     * ユーザマイホテルを取得する（IDから。非加盟は取得しない）
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMyHotelList(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_user_myhotel.* FROM hh_user_myhotel, hh_hotel_basic";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ? AND del_flag = 0";
            query = query + " AND hh_user_myhotel.id = hh_hotel_basic.id";
            query = query + " AND hh_hotel_basic.rank > 0";
            query = query + " ORDER BY append_date DESC, append_time DESC";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getMyHotelListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getMyHotelList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ユーザマイホテルを取得する（IDから。非加盟は取得しない。ホテルメンバーと紐付ける）
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMyHotelListWithMembers(String userId)
    {
        boolean ret;
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId.compareTo( "" ) != 0 )
        {
            query = "SELECT hh_user_myhotel.* FROM hh_user_myhotel";
            query += " INNER JOIN hh_hotel_basic  ON hh_user_myhotel.id = hh_hotel_basic.id";
            query += "                           AND hh_hotel_basic.rank > 0";
            query += " INNER JOIN ap_hotel_custom ON ap_hotel_custom.id = hh_user_myhotel.id";
            query += "                           AND ap_hotel_custom.user_id = hh_user_myhotel.user_id";
            query += "                           AND ap_hotel_custom.del_flag = 0";
            query += "                           AND ap_hotel_custom.regist_status = 1";
            query += " WHERE hh_user_myhotel.user_id = ?";
            query += "   AND hh_user_myhotel.del_flag = 0";
            query += " ORDER BY hh_user_myhotel.append_date DESC, hh_user_myhotel.append_time DESC";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getMyHotelListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.error( "[getMyHotelListWithMembers] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ユーザマイホテルを取得する（端末番号から）
     * 
     * @param mobileTermno 端末番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMyHotelListByTermno(String mobileTermno)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_user_myhotel.* FROM hh_user_myhotel, hh_user_basic, hh_hotel_basic";

        if ( mobileTermno.compareTo( "" ) != 0 )
        {
            query = query + " WHERE hh_user_basic.mobile_termno = ?";
            query = query + " AND hh_user_basic.regist_status = 9";
            query = query + " AND hh_user_basic.del_flag = 0";
            query = query + " AND hh_user_myhotel.del_flag = 0";
            query = query + " AND hh_user_basic.user_id = hh_user_myhotel.user_id";
            query = query + " AND hh_user_myhotel.id = hh_hotel_basic.id";
            query = query + " AND hh_hotel_basic.rank > 0";
            query = query + " ORDER BY append_date DESC, append_time DESC";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( mobileTermno.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, mobileTermno );
            }

            ret = getMyHotelListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getMyHotelListByTermno] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ユーザマイホテルを取得する（IDから）（削除済みも含む）
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMyHotelListAll(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_myhotel";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ?";
            query = query + " ORDER BY append_date DESC, append_time DESC";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getMyHotelListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getMyHotelListAll] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ユーザマイホテルのデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMyHotelListSub(PreparedStatement prestate)
    {
        int i;
        int count;
        ResultSet result = null;

        count = 0;

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    userMyHotelCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.userMyHotel = new DataUserMyHotel[this.userMyHotelCount];
                for( i = 0 ; i < userMyHotelCount ; i++ )
                {
                    userMyHotel[i] = new DataUserMyHotel();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ユーザ情報の取得
                    this.userMyHotel[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserMyHotelSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userMyHotelCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ユーザマイホテル登録確認処理
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @return 処理結果(true:すでに登録済み,false:未登録)
     */
    public boolean existMyHotel(String userId, int hotelId)
    {
        boolean ret;
        DataUserMyHotel dum;

        dum = new DataUserMyHotel();

        // 既存データの確認
        ret = dum.getData( userId, hotelId );
        if ( ret != false )
        {
            if ( dum.getDelFlag() == 1 )
            {
                return(false);
            }
        }

        return(ret);
    }

    /**
     * ユーザマイホテル登録処理
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @return 処理結果(0:正常,1:パラメタ異常,2:DB異常,3:すでに登録済み,4:その他)
     */
    public int setMyHotelData(String userId, int hotelId)
    {
        boolean ret;
        DataUserMyHotel dum;

        // 入力値チェック
        if ( hotelId <= 0 || userId.compareTo( "" ) == 0 )
        {
            return(1);
        }

        dum = new DataUserMyHotel();

        // 既存データの確認
        ret = dum.getData( userId, hotelId );
        if ( ret != false )
        {
            if ( dum.getDelFlag() == 1 )
            {
                // 以前削除されていたので、更新する。
                dum.setUserId( userId );
                dum.setHotelId( hotelId );
                dum.setDelFlag( 0 );
                dum.setAppendDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dum.setAppendTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dum.setDispDate( 0 );
                dum.setDispTime( 0 );
                ret = dum.updateData( userId, hotelId );
                if ( ret != false )
                {
                    return(0);
                }
                else
                {
                    return(2);
                }
            }

            return(3);
        }

        // データをセットする
        dum.setUserId( userId );
        dum.setHotelId( hotelId );
        dum.setAppendDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        dum.setAppendTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        dum.setDispDate( 0 );
        dum.setDispTime( 0 );

        ret = dum.insertData();
        if ( ret != false )
        {
            return(0);
        }
        else
        {
            return(2);
        }
    }

    /**
     * ユーザーマイホテル更新処理
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @param pushFlag push通知フラグ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updataMyHotelData(String userId, int hotelId, int pushFlag)
    {
        boolean ret;
        DataUserMyHotel dum;

        // 入力値チェック
        if ( hotelId <= 0 || userId.compareTo( "" ) == 0 || (pushFlag < 0 || pushFlag > 1) )
        {
            return(false);
        }

        dum = new DataUserMyHotel();

        // 既存データの確認
        ret = dum.getData( userId, hotelId );
        if ( ret != false )
        {
            // データをセットする
            dum.setUserId( userId );
            dum.setHotelId( hotelId );
            dum.setPushFlag( pushFlag );

            ret = dum.updateData( userId, hotelId );
        }
        return(ret);
    }

    /**
     * ユーザマイホテル削除処理
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setDeleteMyHotel(String userId, int hotelId)
    {
        boolean ret;
        DataUserMyHotel dum;

        // 入力値チェック
        if ( hotelId <= 0 || userId.compareTo( "" ) == 0 )
        {
            return(false);
        }

        dum = new DataUserMyHotel();

        // 既存データの確認
        ret = dum.getData( userId, hotelId );
        if ( ret != false )
        {
            // データをセットする
            dum.setUserId( userId );
            dum.setHotelId( hotelId );
            dum.setDelFlag( 1 );
            dum.setAppendDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dum.setAppendTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dum.setDispDate( 0 );
            dum.setDispTime( 0 );

            ret = dum.updateData( userId, hotelId );
        }
        return(ret);
    }

    /**
     * 顧客登録済マイホテル取得処理
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getCustomMyHotelList(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT A.id, C.custom_id FROM hh_user_myhotel A"
                + " INNER JOIN hh_hotel_basic B ON A.id = B.id"
                + " AND B.rank > 0"
                + " LEFT JOIN ap_hotel_custom C ON A.id = C.id"
                + " AND A.user_id = C.user_id"
                + " AND C.regist_status = 1"
                + " AND C.del_flag=0";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE A.user_id = ?";
            query = query + " ORDER BY A.append_date DESC, A.append_time DESC";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getCustomMyHotelListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMyHotelList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 顧客登録済マイホテルのデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getCustomMyHotelListSub(PreparedStatement prestate)
    {
        int i;
        ResultSet result = null;

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    customMyHotelCount = result.getRow();
                }

                this.customMyHotel = new LinkedHashMap<Integer, String>();

                result.beforeFirst();
                while( result.next() != false )
                {
                    if ( result != null )
                    {
                        customMyHotel.put( result.getInt( "id" ), result.getString( "custom_id" ) );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getCustomMyHotelListSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( customMyHotelCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }
}
