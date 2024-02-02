package jp.happyhotel.touch;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApHotelCustom;
import jp.happyhotel.data.DataScRMemberCustom;

/**
 * ハピホテユーザーIDに紐づくデータ(ap_hotel_custom)と、ステイコンシェルジュ側メンバー別顧客登録情報(r_member_custom)を同期させる
 * 
 * @author K.Mitsuhashi
 * @version 1.00 2019/11/22
 */
public class MemberSync implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -6199069863331936331L;

    /**
     * ハピホテユーザーIDに紐づくデータ(ap_hotel_custom)と、ステイコンシェルジュ側メンバー別顧客登録情報(r_member_custom)を同期させる
     * 
     * @param accountId
     * @param id
     * @param userId
     * @return
     */
    public void syncData(int acccountId, String userId)
    {
        ArrayList<Integer> arrId = new ArrayList<Integer>();
        // accountIdから関連するidを取得する
        arrId = getIds( acccountId );
        for( int i = 0 ; i < arrId.size() ; i++ )
        {
            syncData( acccountId, arrId.get( i ), userId );
        }
    }

    /**
     * ハピホテユーザーIDに紐づくデータ(ap_hotel_custom)と、ステイコンシェルジュ側メンバー別顧客登録情報(r_member_custom)を同期させる
     * 
     * @param accountId
     * @param id
     * @param userId
     * @return
     */
    public void syncData(int acccountId, int id, String userId)
    {
        // ハピホテユーザーIDに紐づくデータ(ap_hotel_custom)が存在するとき、ステイコンシェルジュ側メンバー別顧客登録情報(r_member_custom)を作成する
        syncDataSc( acccountId, id, userId );
        // ステイコンシェルジュ側メンバー別顧客登録情報(r_member_custom)が存在するとき、ハピホテユーザーIDに紐づくデータ(ap_hotel_custom)を作成する
        syncDataAp( acccountId, id, userId );
    }

    /**
     * ハピホテユーザーIDに紐づくデータ(ap_hotel_custom)と、ステイコンシェルジュ側メンバー別顧客登録情報(r_member_custom)を同期させる
     * 
     * @param accountId
     * @param id
     * @param userId
     * @return
     */
    public boolean syncDataSc(int acccountId, int id, String userId)
    {
        boolean ret = false;
        ArrayList<Integer> arrAccountId = new ArrayList<Integer>();
        ArrayList<Integer> arrMemberNo = new ArrayList<Integer>();

        // accountId=0のとき、関連するaccountIdを取得する(r_acount_hotelを使用する）
        if ( acccountId == 0 )
        {
            arrAccountId = getAccountId( id );
        }
        else
        {
            arrAccountId.add( acccountId );
        }
        if ( arrAccountId.size() == 0 )
        {
            // account_idが取得できなければ終了
            return false;
        }

        // userIdから連携されているmember_noを取得する(ハピホテユーザーIDとメンバーのリレーション(r_user_member)データを使用する)
        arrMemberNo = getMemberNo( arrAccountId, id, userId );
        if ( arrMemberNo.size() == 0 )
        {
            // member_noが取得できなければ終了
            return false;
        }

        // ap_hotel_customからデータ取得する(パラメータ：id, user_id)
        DataApHotelCustom apCustom = new DataApHotelCustom();
        if ( apCustom.getData( id, userId ) )
        {
            // ap_hotel_customデータが存在するとき、sc.r_member_customにデータを作成する
            for( int i = 0 ; i < arrMemberNo.size() ; i++ )
            {
                DataScRMemberCustom scCustom = new DataScRMemberCustom();
                if ( !scCustom.getData( arrMemberNo.get( i ), id ) )
                {
                    // 存在しなければ、sc.r_member_customデータを作成する
                    scCustom.setMemberNo( arrMemberNo.get( i ) );
                    scCustom.setId( id );
                    scCustom.setSecurityCode( apCustom.getSecurityCode() );
                    scCustom.setCustomId( apCustom.getCustomId() );
                    scCustom.setHotelUserId( apCustom.getHotelUserId() );
                    scCustom.setHotelPassword( apCustom.getHotelPassword() );
                    scCustom.setBirthday1( apCustom.getBirthday1() );
                    scCustom.setBirthday2( apCustom.getBirthday2() );
                    scCustom.setMemorial1( apCustom.getMemorial1() );
                    scCustom.setMemorial2( apCustom.getMemorial2() );
                    scCustom.setNickname( apCustom.getNickname() );
                    scCustom.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    scCustom.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    scCustom.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    scCustom.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    scCustom.setRegistStatus( apCustom.getRegistStatus() );
                    scCustom.setDelFlag( apCustom.getDelFlag() );
                    scCustom.setAutoFlag( apCustom.getAutoFlag() );
                    scCustom.insertData();
                    ret = true;
                }
            }
        }
        return ret;
    }

    /**
     * ステイコンシェルジュ側メンバー別顧客登録情報(r_member_custom)と、ハピホテユーザーIDに紐づくデータ(ap_hotel_custom)を同期させる
     * 
     * @param accountId
     * @param id
     * @param memberNo
     * @return
     */
    public boolean syncDataAp(int acccountId, int id, int memberNo)
    {
        boolean ret = false;
        ArrayList<String> arrUserId = new ArrayList<String>();

        // memberNoから連携されているuser_idを取得する(ハピホテユーザーIDとメンバーのリレーション(r_user_member)データを使用する)
        arrUserId = getUserId( acccountId, id, memberNo );

        if ( arrUserId.size() == 0 )
        {
            // ユーザーIDが取得できなければ(ステイアプリでハピホテIDログインしていなければ)、処理を終了する。
            return ret;
        }

        // r_member_customからデータ取得する(パラメータ：id, user_id)
        DataScRMemberCustom scCustom = new DataScRMemberCustom();
        if ( scCustom.getData( memberNo, id ) )
        {
            // r_member_customデータが存在するとき、ap_hotel_customにデータを作成する
            for( int i = 0 ; i < arrUserId.size() ; i++ )
            {
                DataApHotelCustom apCustom = new DataApHotelCustom();
                if ( !apCustom.getData( id, arrUserId.get( i ) ) )
                {
                    // 存在しなければ、ap_hotel_customデータを作成する
                    apCustom.setId( id );
                    apCustom.setUserId( arrUserId.get( i ) );
                    apCustom.setSecurityCode( scCustom.getSecurityCode() );
                    apCustom.setCustomId( scCustom.getCustomId() );
                    apCustom.setHotelUserId( scCustom.getHotelUserId() );
                    apCustom.setHotelPassword( scCustom.getHotelPassword() );
                    apCustom.setBirthday1( scCustom.getBirthday1() );
                    apCustom.setBirthday2( scCustom.getBirthday2() );
                    apCustom.setMemorial1( scCustom.getMemorial1() );
                    apCustom.setMemorial2( scCustom.getMemorial2() );
                    apCustom.setNickname( scCustom.getNickname() );
                    apCustom.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    apCustom.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    apCustom.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    apCustom.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    apCustom.setRegistStatus( scCustom.getRegistStatus() );
                    apCustom.setDelFlag( scCustom.getDelFlag() );
                    apCustom.setAutoFlag( scCustom.getAutoFlag() );
                    apCustom.insertData();
                    ret = true;
                }
            }
        }
        return ret;
    }

    /**
     * ステイコンシェルジュ側メンバー別顧客登録情報(r_member_custom)と、ハピホテユーザーIDに紐づくデータ(ap_hotel_custom)を同期させる
     * 
     * @param accountId
     * @param id
     * @param memberNo
     * @return
     */
    public boolean syncDataAp(int acccountId, int id, String userId)
    {
        boolean ret = false;
        ArrayList<Integer> accountIdArr = new ArrayList<Integer>();
        ArrayList<Integer> arrMemberNo = new ArrayList<Integer>();
        accountIdArr.add( acccountId );

        // UserIdから連携されているmember_noを取得する(ハピホテユーザーIDとメンバーのリレーション(r_user_member)データを使用する)
        arrMemberNo = getMemberNo( accountIdArr, id, userId );
        if ( arrMemberNo.size() == 0 )
        {
            // メンバーNOが取得できなければ、処理を終了する。
            return ret;
        }

        for( int i = 0 ; i < arrMemberNo.size() ; i++ )
        {
            // r_member_customからデータ取得する(パラメータ：id, user_id)
            DataScRMemberCustom scCustom = new DataScRMemberCustom();
            if ( scCustom.getData( arrMemberNo.get( i ), id ) )
            {
                // r_member_customデータが存在するとき、ap_hotel_customにデータを作成する
                DataApHotelCustom apCustom = new DataApHotelCustom();
                if ( !apCustom.getData( id, userId ) )
                {
                    // 存在しなければ、ap_hotel_customデータを作成する
                    apCustom.setId( id );
                    apCustom.setUserId( userId );
                    apCustom.setSecurityCode( scCustom.getSecurityCode() );
                    apCustom.setCustomId( scCustom.getCustomId() );
                    apCustom.setHotelUserId( scCustom.getHotelUserId() );
                    apCustom.setHotelPassword( scCustom.getHotelPassword() );
                    apCustom.setBirthday1( scCustom.getBirthday1() );
                    apCustom.setBirthday2( scCustom.getBirthday2() );
                    apCustom.setMemorial1( scCustom.getMemorial1() );
                    apCustom.setMemorial2( scCustom.getMemorial2() );
                    apCustom.setNickname( scCustom.getNickname() );
                    apCustom.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    apCustom.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    apCustom.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    apCustom.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    apCustom.setRegistStatus( scCustom.getRegistStatus() );
                    apCustom.setDelFlag( scCustom.getDelFlag() );
                    apCustom.setAutoFlag( scCustom.getAutoFlag() );
                    apCustom.insertData();
                    ret = true;
                }
            }
        }
        return ret;
    }

    /**
     * ホテルIDから、ステイコンシェルジュで連携されているアカウントIDを取得する
     * 
     * @param id ホテルID
     * @return arrAccountId アカウントID配列
     */
    private ArrayList<Integer> getAccountId(int id)
    {

        // ホテルIDから、ステイコンシェルジュで連携されているアカウントIDを取得する(アカウントごとのホテルを管理(r_account_hotel)データを使用する)
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> arrAccountId = new ArrayList<Integer>();

        query = "SELECT sc_rah.account_id ";
        query = query + " FROM sc.r_account_hotel sc_rah";
        query = query + " WHERE sc_rah.id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();
            if ( result != null )
            {
                result.beforeFirst();
                while( result.next() != false )
                {
                    arrAccountId.add( result.getInt( "account_id" ) );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[MemberSync.getAccountId] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(arrAccountId);
    }

    /**
     * アカウントIDから、ステイコンシェルジュで連携されているホテルIDを取得する
     * 
     * @param accountId アカウントID
     * @return arrId ホテルID配列
     */
    private ArrayList<Integer> getIds(int accountId)
    {

        // アカウントIDから、ステイコンシェルジュで連携されているホテルIDを取得する(アカウントごとのホテルを管理(r_account_hotel)データを使用する)
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> arrId = new ArrayList<Integer>();

        query = "SELECT sc_rah.id ";
        query = query + " FROM sc.r_account_hotel sc_rah";
        query = query + " WHERE sc_rah.account_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accountId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                result.beforeFirst();
                while( result.next() != false )
                {
                    arrId.add( result.getInt( "id" ) );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[MemberSync.getIds] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(arrId);
    }

    /**
     * ホテルIDとユーザーIDから、ステイコンシェルジュ側に連携されているメンバーNOを取得する
     * 
     * @param accountId アカウントID
     * @param id ホテルID
     * @param userId ユーザーID
     * @return arrMemberNo メンバーNO配列
     */
    private ArrayList<Integer> getMemberNo(ArrayList<Integer> accountId, int id, String userId)
    {

        // user_idから連携されているmember_noを取得する(ハピホテユーザーIDとメンバーのリレーション(r_user_member)データを使用する)
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> arrMemberNo = new ArrayList<Integer>();

        query = "SELECT sc_rum.user_id, sc_rum.member_no,  sc_mm.account_id,  sc_mm.member_id ";
        query = query + " FROM sc.r_user_member sc_rum";
        query = query + " INNER JOIN sc.m_member sc_mm";
        query = query + "    ON sc_rum.member_no = sc_mm.member_no";
        query = query + " INNER JOIN sc.r_account_hotel sc_rah";
        query = query + "    ON sc_rah.account_id = sc_mm.account_id";
        query = query + " WHERE sc_mm.account_id IN( ";
        for( int k = 0 ; k < accountId.size() ; k++ )
        {
            if ( k > 0 )
            {
                query = query + ",";
            }
            query = query + "?";
        }
        query = query + ")";
        query = query + "    AND sc_mm.status =?";
        query = query + "    AND sc_rah.id =?";
        query = query + "    AND sc_rum.user_id =?";
        query = query + "    AND sc_rum.del_flag =?";

        try
        {
            int i;
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            for( i = 0 ; i < accountId.size() ; i++ )
            {
                prestate.setInt( i + 1, accountId.get( i ) );
            }
            i = accountId.size() + 1;
            prestate.setInt( i++, 1 );
            prestate.setInt( i++, id );
            prestate.setString( i++, userId );
            prestate.setInt( i++, 0 );
            result = prestate.executeQuery();
            if ( result != null )
            {
                result.beforeFirst();
                while( result.next() != false )
                {
                    arrMemberNo.add( result.getInt( "member_no" ) );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[MemberSync.getMemberNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(arrMemberNo);
    }

    /**
     * ホテルIDとユーザーIDから、ステイコンシェルジュ側に連携されているメンバーNOを取得する
     * 
     * @param accountId アカウントID
     * @param id ホテルID
     * @param memberNo メンバーNO
     * @return arrMemberNo ユーザーID配列
     */
    private ArrayList<String> getUserId(int accountId, int id, int memberNo)
    {
        // memberNoから連携されているユーザーIDを取得する(ハピホテユーザーIDとメンバーのリレーション(r_user_member)データを使用する)
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<String> arrUserId = new ArrayList<String>();

        query = "SELECT sc_rum.user_id, sc_rum.member_no,  sc_mm.account_id,  sc_mm.member_id ";
        query = query + " FROM sc.r_user_member sc_rum";
        query = query + " INNER JOIN sc.m_member sc_mm";
        query = query + "    ON sc_rum.member_no = sc_mm.member_no";
        query = query + " INNER JOIN sc.r_account_hotel sc_rah";
        query = query + "    ON sc_rah.account_id = sc_mm.account_id";
        query = query + " WHERE sc_mm.account_id = ?";
        query = query + "    AND sc_mm.status =?";
        query = query + "    AND sc_rah.id =?";
        query = query + "    AND sc_rum.member_no =?";
        query = query + "    AND sc_rum.del_flag =?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accountId );
            prestate.setInt( 2, 1 );
            prestate.setInt( 3, id );
            prestate.setInt( 4, memberNo );
            prestate.setInt( 5, 0 );
            result = prestate.executeQuery();
            if ( result != null )
            {
                result.beforeFirst();
                while( result.next() != false )
                {
                    arrUserId.add( result.getString( "user_id" ) );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[MemberSync.getUserId] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(arrUserId);
    }
}
