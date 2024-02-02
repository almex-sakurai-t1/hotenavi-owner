package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * メニュー画面ビジネスロジッククラス
 */
public class LogicOwnerBkoMenu implements Serializable
{
    private static final long serialVersionUID = -3251943307533618501L;
    private FormOwnerBkoMenu  frm;

    /* フォームオブジェクト */
    public FormOwnerBkoMenu getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoMenu frm)
    {
        this.frm = frm;
    }

    /**
     * ログインユーザ権限取得
     * 
     * @param hotenaviId ホテナビID
     * @param userId ユーザID
     * @return int 権限区分
     */
    public int getUserAuth(String hotenaviId, int userId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int ret = 0;

        query = query + "SELECT sec_level19 FROM owner_user_security ";
        query = query + "WHERE hotelid = ? AND userid = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotenaviId );
            prestate.setInt( 2, userId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                ret = result.getInt( "sec_level19" );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoMenu.getUserAuth] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 管理者フラグ取得
     * 
     * @param String hotelId ホテルID
     * @param int userId ユーザーID
     * @return int (1:管理者、0:管理者以外)
     */
    public int getAdminFlg(String hotelId, int userId) throws Exception
    {
        ArrayList<String> ary = new ArrayList<String>();
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int adminFlg = 0;

        query = query + "SELECT admin_flag FROM owner_user_security ";
        query = query + "WHERE hotelid = ?";
        query = query + " AND userid = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            prestate.setInt( 2, userId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                adminFlg = result.getInt( "admin_flag" );
            }

            frm.setHotelNmList( ary );

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoMenu.getAdminFlg] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(adminFlg);
    }

    /**
     * 管理者用ホテルID取得
     * 
     * @param なし
     * @return ArrayList<Integer> ホテルIDのリスト
     */
    public ArrayList<Integer> getAdminHotelIDList() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> idList = new ArrayList<Integer>();

        query = query + "SELECT hh_hotel_basic.id FROM hh_hotel_basic ";
        query = query + "INNER JOIN hh_hotel_newhappie ON hh_hotel_basic.id = hh_hotel_newhappie.id ";
        query = query + "WHERE hh_hotel_basic.hotenavi_id != '' ";
        // query = query + "  AND hh_hotel_basic.rank >= 2 ";
        query = query + "ORDER BY hh_hotel_basic.hotenavi_id ,hh_hotel_basic.id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            while( result.next() )
            {
                idList.add( result.getInt( "hh_hotel_basic.id" ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoMenu.getAdminHotelIDList] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(idList);
    }

    /**
     * マイル加盟店ホテルID取得
     * 
     * @param なし
     * @return ArrayList<Integer> ホテルIDのリスト
     */
    public ArrayList<Integer> getMileHotelIDList() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> idList = new ArrayList<Integer>();

        query = query + "SELECT hh_hotel_basic.id FROM hh_hotel_basic ";
        query = query + "INNER JOIN hh_hotel_newhappie ON hh_hotel_basic.id = hh_hotel_newhappie.id ";
        query = query + "WHERE hh_hotel_basic.hotenavi_id != '' ";
        query = query + "  AND hh_hotel_basic.rank = 3 ";
        query = query + "ORDER BY hh_hotel_basic.hotenavi_id ,hh_hotel_basic.id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            while( result.next() )
            {
                idList.add( result.getInt( "hh_hotel_basic.id" ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoMenu.getMileHotelIDList] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(idList);
    }

    /**
     * 管理者用ホテルID取得
     * 
     * @param なし
     * @return ArrayList<String> ホテナビID + ホテル名のリスト
     */
    public ArrayList<String> getAdminHotelNmList() throws Exception
    {

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<String> nmList = new ArrayList<String>();

        query = query + "SELECT concat(concat(hh_hotel_basic.hotenavi_id,','),hh_hotel_basic.name) AS hotelNm FROM hh_hotel_basic ";
        query = query + "INNER JOIN hh_hotel_newhappie ON hh_hotel_basic.id = hh_hotel_newhappie.id ";
        query = query + "WHERE hh_hotel_basic.hotenavi_id != '' ";
        // query = query + "  AND hh_hotel_basic.rank >= 2 ";
        query = query + "ORDER BY hh_hotel_basic.hotenavi_id ,hh_hotel_basic.id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            while( result.next() )
            {
                nmList.add( result.getString( "hotelNm" ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoMenu.getHotelID] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(nmList);
    }

    /**
     * マイル加盟店ホテル名称取得
     * 
     * @param なし
     * @return ArrayList<String> ホテナビID + ホテル名のリスト
     */
    public ArrayList<String> getMileHotelNmList() throws Exception
    {

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<String> nmList = new ArrayList<String>();

        query = query + "SELECT concat(concat(hh_hotel_basic.hotenavi_id,','),hh_hotel_basic.name) AS hotelNm FROM hh_hotel_basic ";
        query = query + "INNER JOIN hh_hotel_newhappie ON hh_hotel_basic.id = hh_hotel_newhappie.id ";
        query = query + "WHERE hh_hotel_basic.hotenavi_id != '' ";
        query = query + "  AND hh_hotel_basic.rank = 3 ";
        query = query + "ORDER BY hh_hotel_basic.hotenavi_id ,hh_hotel_basic.id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            while( result.next() )
            {
                nmList.add( result.getString( "hotelNm" ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoMenu.getHotelID] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(nmList);
    }

    /**
     * 管理者以外のホテルID取得
     * 
     * @param hotelId ホテルID
     * @param userId ユーザID
     * @return ArrayList<Integer> ホテルIDのリスト
     */
    public ArrayList<Integer> getHotelIDList(String hotelId, int userId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> idList = new ArrayList<Integer>();

        query = query + "SELECT hb.id ";
        query = query + "FROM hh_hotel_basic hb INNER JOIN owner_user_hotel uh ON hb.hotenavi_id = uh.accept_hotelid ";
        query = query + "INNER JOIN hh_hotel_newhappie ON hb.id = hh_hotel_newhappie.id ";
        query = query + "WHERE uh.hotelid = ? ";
        query = query + "  AND uh.userid = ? ";
        // query = query + "  AND hb.rank >= 2 ";
        query = query + "ORDER BY hb.hotenavi_id ,hb.id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            prestate.setInt( 2, userId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                idList.add( result.getInt( "hb.id" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoMenu.getHotelIDList] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(idList);
    }

    /**
     * 管理者以外のホテルID取得
     * 
     * @param hotelId ホテルID
     * @param userId ユーザID
     * @return ArrayList<Integer> ホテルIDのリスト
     */
    public ArrayList<String> getHotelNmList(String hotelId, int userId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<String> nmList = new ArrayList<String>();

        // 2011-06-13 名前だけを表示する
        // query = query + "SELECT concat(concat(hb.hotenavi_id,','),hb.name) AS hotelNm ";
        query = query + "SELECT hb.name AS hotelNm ";
        query = query + "FROM hh_hotel_basic hb INNER JOIN owner_user_hotel uh ON hb.hotenavi_id = uh.accept_hotelid ";
        query = query + "INNER JOIN hh_hotel_newhappie ON hb.id = hh_hotel_newhappie.id ";
        query = query + "WHERE uh.hotelid = ? ";
        query = query + "  AND uh.userid = ? ";
        // query = query + "  AND hb.rank >= 2 ";
        query = query + "ORDER BY hb.hotenavi_id ,hb.id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            prestate.setInt( 2, userId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                nmList.add( result.getString( "hotelNm" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvMenu.getHotelNmList] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(nmList);
    }
}
