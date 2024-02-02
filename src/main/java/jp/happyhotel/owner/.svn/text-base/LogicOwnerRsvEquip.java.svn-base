package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;

/**
 * 設備設定詳細画面ビジネスロジッククラス
 */
public class LogicOwnerRsvEquip implements Serializable
{

    private static final long   serialVersionUID = -7588528130133495440L;
    private static final String ROOM_ALL         = "全室";

    private FormOwnerRsvEquip   frm;

    /* フォームオブジェクト */
    public FormOwnerRsvEquip getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvEquip frm)
    {
        this.frm = frm;
    }

    /**
     * 設備情報取得
     * 
     * @param なし
     * @return なし
     */
    public void getEquipData() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        int equipId = 0;
        PreparedStatement prestate = null;

        int count = 0;

        query = query + "SELECT ";
        query = query + "hotelEq.equip_id, masterEq.name, masterEq.sort_display, hotelEq.equip_type, masterEq.input_flag2, hotelEq.equip_rental, masterEq.branch_name1 ";
        query = query + "FROM hh_hotel_equip hotelEq ";
        query = query + "  LEFT JOIN hh_master_equip masterEq ON hotelEq.equip_id = masterEq.equip_id ";
        query = query + "WHERE hotelEq.id = ? AND hotelEq.equip_id = ? ";
        query = query + "ORDER BY masterEq.input_flag6, masterEq.sort_display ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, frm.getEquipID() );
            result = prestate.executeQuery();
            while( result.next() )
            {
                equipId = result.getInt( "equip_id" );
                frm.setEquipID( equipId );
                frm.setEqName( result.getString( "name" ) );
                frm.setInputFlag2( result.getInt( "input_flag2" ) );
                frm.setBranchName( result.getString( "branch_name1" ) );
                frm.setEqType( result.getInt( "equip_type" ) );
                frm.setDisp( result.getInt( "sort_display" ) );
                frm.setEquipRental( result.getInt( "equip_rental" ) );
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "設備情報" ) );
                return;
            }

            // 設備に紐づく部屋番号取得
            getEquipRoom();

            // ホテルの部屋番号取得
            getHotelRoom();

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvEquip.getEquipData] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 設備が持つ部屋番号の文字列取得
     * 
     * @param なし
     * @return なし
     */
    private void getEquipRoom() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        String seqNm = "";
        PreparedStatement prestate = null;
        ReserveCommon rsvcomm = new ReserveCommon();
        ArrayList<Integer> eqRoomIdList = new ArrayList<Integer>();
        int roomCnt = 0;

        query = query + "SELECT relEq.seq ";
        query = query + "FROM hh_rsv_rel_room_equip relEq ";
        query = query + "WHERE relEq.id = ? ";
        query = query + "  AND relEq.equip_id = ? ";
        query = query + "ORDER BY relEq.equip_id, relEq.seq";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, frm.getEquipID() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( seqNm.trim().length() != 0 )
                {
                    seqNm = seqNm + " ";
                }
                seqNm = seqNm + result.getString( "seq" );
                eqRoomIdList.add( result.getInt( "seq" ) );
            }

            // ホテルに紐づく全部屋数取得
            roomCnt = rsvcomm.getRoomCnt( frm.getSelHotelID() );
            if ( roomCnt == eqRoomIdList.size() )
            {
                seqNm = ROOM_ALL;
            }

            // フォームにセット
            frm.setEqqIdList( eqRoomIdList );
            frm.setRoomNo( seqNm );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvEquip.getEquipData] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * ホテルが持つ部屋番号の取得
     * 
     * @param なし
     * @return なし
     */
    private void getHotelRoom() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> roomIdList = new ArrayList<Integer>();

        query = query + "SELECT hr.seq ";
        query = query + "FROM hh_hotel_roomrank hrr ";
        query = query + "  LEFT JOIN hh_hotel_room_more hr ON hrr.id = hr.id AND hrr.room_rank = hr.room_rank ";
        query = query + "     LEFT JOIN hh_rsv_room rsvroom ON hr.id = rsvroom.id and hr.seq = rsvroom.seq ";
        query = query + "WHERE hrr.id = ? ";
        query = query + "  AND (hr.disp_flag = 0 or hr.disp_flag = 1)";
        query = query + "  AND hrr.room_rank <> 0 ";
        query = query + "  AND (hrr.disp_index BETWEEN 1 AND 98) ";
        query = query + "ORDER BY hr.seq";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                roomIdList.add( result.getInt( "seq" ) );
            }

            // フォームにセット
            frm.setAllEqIdList( roomIdList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvEquip.getEquipData] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 部屋情報登録処理
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    public boolean registRoom() throws Exception
    {
        boolean ret = false;
        int hotelId = 0;
        int equipId = 0;
        ArrayList<Integer> orgSeqList = new ArrayList<Integer>();

        try
        {
            hotelId = frm.getSelHotelID();
            equipId = frm.getEquipID();

            // 現在登録済みの設備情報取得
            orgSeqList = getSeqList( hotelId, equipId );

            // 画面で選択されている部屋とマージ
            for( int i = 0 ; i < frm.getSelSeqList().size() ; i++ )
            {
                orgSeqList.add( frm.getSelSeqList().get( i ) );
            }
            // 設備情報更新
            for( int i = 0 ; i < orgSeqList.size() ; i++ )
            {
                registRoomEq( hotelId, equipId, frm.getSelSeqList() );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerO10702.registRoom] Exception=" + e.toString() );
            ret = false;
            throw new Exception( e );
        }
        ret = true;
        return ret;
    }

    /**
     * 部屋と設備の紐付け登録実処理
     * 
     * @param hotelId ホテルID
     * @param equipId 設備ID
     * @param selSeqList 画面で選択されている部屋のリスト
     * @return ArrayList<Integer> 設備IDのリスト
     */
    private void registRoomEq(int hotelId, int equipId, ArrayList<Integer> selSeqList) throws Exception
    {

        boolean isExists = false;
        ArrayList<Integer> regSeqIdList = new ArrayList<Integer>();
        int regSeqId = 0;
        int seq = 0;

        try
        {
            // ▼選択されている管理番号を元に登録処理
            for( int i = 0 ; i < selSeqList.size() ; i++ )
            {
                seq = selSeqList.get( i );
                // 存在チェック
                if ( isExistsRsvRelRoom( hotelId, seq, equipId ) == false )
                {
                    // 存在しない場合は登録
                    execInsRelRsvRoom( hotelId, seq, equipId );
                }

            }

            // ▼登録済みの管理番号を元に削除処理
            // 登録済みの管理番号を取得
            regSeqIdList.clear();
            regSeqIdList = getSeqList( hotelId, equipId );

            // 登録済み設備情報を削除
            if ( (regSeqIdList == null) || (regSeqIdList.size() == 0) )
            {
                return;
            }

            for( int i = 0 ; i < regSeqIdList.size() ; i++ )
            {
                regSeqId = regSeqIdList.get( i );
                isExists = false;
                for( int j = 0 ; j < selSeqList.size() ; j++ )
                {
                    // 選択済み設備情報にあるか
                    if ( regSeqId == selSeqList.get( j ) )
                    {
                        // 存在する
                        isExists = true;
                        break;
                    }
                }

                // 存在しない場合は、削除
                if ( isExists == false )
                {
                    execDelRoom( hotelId, regSeqId, equipId );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerO10702.execRegEquipID] Exception=" + e.toString() );
            throw new Exception( e );
        }
    }

    /**
     * 指定された設備に、対象の部屋が登録されているか
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号
     * @param equipId 設備ID
     * @return true:存在する、false:存在しない
     */
    private boolean isExistsRsvRelRoom(int hotelId, int seq, int equipId) throws Exception
    {
        boolean ret = false;

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int cnt = 0;

        query = query + "SELECT Count(*) As COUNT ";
        query = query + "FROM hh_rsv_rel_room_equip ";
        query = query + "WHERE id = ? ";
        query = query + "  AND seq = ? ";
        query = query + "  AND equip_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, seq );
            prestate.setInt( 3, equipId );

            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "COUNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerO10702.isExistsRsvRelRoom] Exception=" + e.toString() );
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
     * 部屋・設備情報追加処理
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号
     * @param equipId 設備ID
     * @return true:処理成功、false:更新対象なし
     * @throws Exception
     */
    private boolean execInsRelRsvRoom(int hotelId, int seq, int equipId) throws Exception
    {
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        int result;
        boolean ret = false;

        query = "INSERT hh_rsv_rel_room_equip SET ";
        query = query + " id = ?,";
        query = query + " seq = ?,";
        query = query + " equip_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, seq );
            prestate.setInt( 3, equipId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerO10702.execInsRelRsvRoom] Exception=" + e.toString() );
            throw new Exception( "[LogicOwnerO10702.execInsRelRsvRoom] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 設備に紐づく管理番号のリストを取得
     * 
     * @param hotelId ホテルID
     * @param equipId 設備ID
     * @return ArrayList<Integer> 管理番号のリスト
     */
    public ArrayList<Integer> getSeqList(int hotelId, int equipId) throws Exception
    {

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> seqList = new ArrayList<Integer>();

        query = query + "SELECT seq ";
        query = query + "FROM hh_rsv_rel_room_equip ";
        query = query + "WHERE id = ? ";
        query = query + "  AND equip_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, equipId );

            result = prestate.executeQuery();

            while( result.next() )
            {
                seqList.add( result.getInt( "seq" ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerO10702.isExistsRsvRelRoom] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(seqList);
    }

    /**
     * 部屋・設備情報削除処理
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号
     * @param equipId 設備ID
     * @return true:処理成功、false:処理失敗
     * @throws Exception
     */
    private boolean execDelRoom(int hotelId, int seq, int equipId) throws Exception
    {
        String query;
        int result;
        Connection connection = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = "DELETE FROM hh_rsv_rel_room_equip ";
        query = query + " WHERE  id = ? ";
        query = query + "   AND seq = ? ";
        query = query + "   AND equip_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, seq );
            prestate.setInt( 3, equipId );

            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerO10702.execDelRoom] Exception=" + e.toString() );
            throw new Exception( "[LogicOwnerO10702.execDelRoom] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

}
