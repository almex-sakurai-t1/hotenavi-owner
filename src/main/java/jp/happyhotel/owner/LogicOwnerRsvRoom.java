package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.data.DataRsvRelRoomEquip;
import jp.happyhotel.data.DataRsvRoom;

/**
 * 部屋設定ビジネスロジック
 */
public class LogicOwnerRsvRoom implements Serializable
{

    private static final long   serialVersionUID = 6311473229172286617L;
    private static final String MSG_O10402_1     = "部屋情報がありません。";
    private static final String EQTYPE_BATH      = "バス関連";
    private static final String EQTYPE_TV        = "テレビ関連";
    private static final String EQTYPE_AMUSEMENT = "アミューズメント関連";
    private static final String EQTYPE_ROOM      = "部屋関連";
    private static final String EQTYPE_EQUIP     = "設備関連";
    private static final String EQTYPE_SALES     = "販売関連";
    private static final String EQTYPE_RENTAL    = "レンタル関連";

    private static final int    ID_ALL           = 1;
    private static final int    ID_BATH          = 2;
    private static final int    ID_TV            = 3;
    private static final int    ID_AMUSEMENT     = 4;
    private static final int    ID_ROOM          = 5;
    private static final int    ID_EQUIP         = 6;
    private static final int    ID_SALES         = 7;
    private static final int    ID_RENTAL        = 8;

    private FormOwnerRsvRoom    frm;

    /* フォームオブジェクト */
    public FormOwnerRsvRoom getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvRoom frm)
    {
        this.frm = frm;
    }

    /**
     * ホテナビID取得
     * 
     * @param int hotelId ホテルID
     * @return String ホテナビID
     */
    public String getHotenaviID(int hotelId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String hotenaviID = "";

        query = query + "SELECT hotenavi_id FROM hh_hotel_basic WHERE id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                hotenaviID = result.getString( "hotenavi_id" );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvRoom.getHotenaviID] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(hotenaviID);
    }

    /**
     * 部屋詳細取得
     * 
     * @param なし
     * @return なし
     */
    public void getRoomDetailData() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;

        query = query + "SELECT ";
        query = query + "rank.room_rank, rank.rank_name, rsvRoom.room_name, rsvRoom.room_pr, rsvRoom.remarks ";
        query = query + "FROM hh_hotel_roomrank rank ";
        query = query + "  LEFT JOIN hh_hotel_room_more room ON rank.id = room.id AND rank.room_rank = room.room_rank ";
        query = query + "     LEFT JOIN hh_rsv_room rsvRoom ON room.id = rsvRoom.id AND room.seq = room.seq ";
        query = query + "WHERE rank.id = ? ";
        query = query + "  AND rsvRoom.seq = ? ";
        query = query + "  AND rank.room_rank = ? ";
        query = query + "  AND (room.disp_flag = 0 or room.disp_flag = 1) ";
        query = query + "  AND rank.room_rank <> 0 ";
        query = query + "  AND (rank.disp_index BETWEEN 1 AND 98) ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, frm.getSeq() );
            prestate.setInt( 3, frm.getRoomRank() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                frm.setRoomRank( result.getInt( "room_rank" ) );
                frm.setRankName( ConvertCharacterSet.convDb2Form( result.getString( "rank_name" ) ) );
                frm.setRoomName( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( CheckString.checkStringForNull( result.getString( "room_name" ) ) ) ) );
                frm.setRoomNameInput( ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( result.getString( "room_name" ) ) ) );
                frm.setRoomPR( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( CheckString.checkStringForNull( result.getString( "room_pr" ) ) ) ) );
                frm.setRoomPRInput( ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( result.getString( "room_pr" ) ) ) );
                frm.setRoomText( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( CheckString.checkStringForNull( result.getString( "remarks" ) ) ) ) );
                frm.setRoomTextInput( ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( result.getString( "remarks" ) ) ) );
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                // ランク名取得
                if ( getRankName() == false )
                {
                    frm.setErrMsg( MSG_O10402_1 );
                    return;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvRoom.getRoomDetailData] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 設備情報取得
     * 
     * @param 検索区分：1:ホテルに登録されている設備全て、2:対象のホテル
     * @return なし
     */
    public void getEquip(int searchKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int hotelID = 0;
        int seq = 0;
        int inpTypeFlg6 = 0;
        int inpTypeFlg2 = 0;
        int equipTypeId = 0;
        String eqTypeNm = "";
        ArrayList<Integer> allEquipIdList = new ArrayList<Integer>();
        ArrayList<String> allEquipNmList = new ArrayList<String>();
        ArrayList<Integer> allEqTypeIdList = new ArrayList<Integer>();
        ArrayList<Integer> allEqTypeList = new ArrayList<Integer>();
        ArrayList<String> allEqTypeNmList = new ArrayList<String>();
        ArrayList<Integer> allDispList = new ArrayList<Integer>();
        ArrayList<Integer> equipIdList = new ArrayList<Integer>();
        ArrayList<String> equipNmList = new ArrayList<String>();
        // ArrayList<Integer> iconList = new ArrayList<Integer>();
        ArrayList<Integer> eqTypeList = new ArrayList<Integer>();
        ArrayList<Integer> eqTypeIdList = new ArrayList<Integer>();
        ArrayList<Integer> inputFlg6List = new ArrayList<Integer>();
        ArrayList<Integer> inputFlg2List = new ArrayList<Integer>();
        ArrayList<Integer> dispList = new ArrayList<Integer>();
        ArrayList<String> allEquipBranchNmList = new ArrayList<String>();
        ArrayList<String> equipBranchNmList = new ArrayList<String>();

        // フォームからキー情報取得
        hotelID = frm.getSelHotelID();
        seq = frm.getSeq();

        if ( searchKbn == 1 )
        {
            // ホテルに紐づく設備情報取得
            query = createSqlAllEq();
        }
        else
        {
            // 部屋に紐づく設備情報取得
            query = createSqlRoomEq();
        }

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelID );
            if ( searchKbn == 2 )
            {
                prestate.setInt( 2, seq );
            }
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( searchKbn == 1 )
                {
                    allEquipIdList.add( result.getInt( "equip_id" ) );
                    allEquipNmList.add( ConvertCharacterSet.convDb2Form( result.getString( "name" ) ) );
                    allEqTypeList.add( result.getInt( "equip_type" ) );
                    inpTypeFlg6 = result.getInt( "input_flag6" );
                    inputFlg6List.add( inpTypeFlg6 );
                    inpTypeFlg2 = result.getInt( "input_flag2" );
                    inputFlg2List.add( inpTypeFlg2 );
                    switch( inpTypeFlg6 )
                    {
                        case 1:
                            eqTypeNm = EQTYPE_BATH;
                            equipTypeId = ID_BATH;
                            break;
                        case 2:
                            eqTypeNm = EQTYPE_TV;
                            equipTypeId = ID_TV;
                            break;
                        case 3:
                            eqTypeNm = EQTYPE_AMUSEMENT;
                            equipTypeId = ID_AMUSEMENT;
                            break;
                        case 4:
                            eqTypeNm = EQTYPE_ROOM;
                            equipTypeId = ID_ROOM;
                            break;
                        case 5:
                            eqTypeNm = EQTYPE_EQUIP;
                            equipTypeId = ID_EQUIP;
                            break;
                    }
                    if ( inpTypeFlg2 == 1 )
                    {
                        eqTypeNm = EQTYPE_RENTAL;
                        equipTypeId = ID_RENTAL;
                    }
                    if ( ReplaceString.HTMLEscape( CheckString.checkStringForNull( result.getString( "branch_name1" ) ) ).equals( "販売" ) )
                    {
                        eqTypeNm = EQTYPE_SALES;
                        equipTypeId = ID_SALES;
                    }
                    allEqTypeIdList.add( equipTypeId );
                    allEqTypeNmList.add( eqTypeNm );
                    allDispList.add( result.getInt( "sort_display" ) );
                    allEquipBranchNmList.add( ConvertCharacterSet.convDb2Form( result.getString( "branch_name1" ) ) );
                }
                else
                {
                    equipIdList.add( result.getInt( "equip_id" ) );
                    equipNmList.add( ConvertCharacterSet.convDb2Form( result.getString( "name" ) ) );
                    // iconList.add( result.getInt( "icon_no" ) );
                    eqTypeList.add( result.getInt( "equip_type" ) );
                    dispList.add( result.getInt( "sort_display" ) );
                    equipBranchNmList.add( ConvertCharacterSet.convDb2Form( result.getString( "branch_name1" ) ) );
                }
            }

            // Formに格納
            if ( searchKbn == 1 )
            {
                frm.setAllEquipIdList( allEquipIdList );
                frm.setAllEquipNmList( allEquipNmList );
                frm.setAllEqTypeIdList( allEqTypeIdList );
                frm.setAllEqTypeList( allEqTypeList );
                frm.setAllEqTypeNmList( allEqTypeNmList );
                frm.setAllInputFlg6List( inputFlg6List );
                frm.setAllInputFlg2List( inputFlg2List );
                frm.setAllDispList( allDispList );
                frm.setAllEquipBranchNmList( allEquipBranchNmList );
            }
            else
            {
                frm.setEquipIdList( equipIdList );
                frm.setEquipNmList( equipNmList );
                // frm.setIconList( iconList );
                frm.setDispList( dispList );
                frm.setEquipTypeList( eqTypeList );
                frm.setEquipBranchNmList( equipBranchNmList );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvRoom.getEquip] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 対象ホテルの全設備情報を取得するSQLの作成
     * 
     * @param ホテルID
     * @return なし
     */
    private String createSqlAllEq()
    {
        String query = "";

        query = query + "SELECT ";
        query = query + "hotelEq.equip_id, masterEq.name, masterEq.input_flag6, masterEq.sort_display, reHotelEq.equip_type, masterEq.branch_name1, masterEq.input_flag2 ";
        query = query + "FROM hh_hotel_equip hotelEq ";
        query = query + "  LEFT JOIN hh_master_equip masterEq ON hotelEq.equip_id = masterEq.equip_id ";
        query = query + "  LEFT JOIN research_hotel_equip reHotelEq ON hotelEq.id = reHotelEq.id AND hotelEq.equip_id = reHotelEq.equip_id ";
        query = query + "WHERE hotelEq.id = ? ";
        query = query + "ORDER BY masterEq.branch_name1, masterEq.input_flag2, masterEq.input_flag6, masterEq.sort_display ";

        return query;
    }

    /**
     * 対象部屋の設備情報を取得するSQLの作成
     * 
     * @param ホテルID、管理番号
     * @return なし
     */
    private String createSqlRoomEq()
    {
        String query = "";

        query = query + "SELECT ";
        query = query + "hotelEq.equip_id, masterEq.name, masterEq.input_flag6, masterEq.sort_display, hotelEq.equip_type, masterEq.branch_name1, masterEq.input_flag2  ";
        query = query + "FROM hh_hotel_equip hotelEq ";
        query = query + "   LEFT JOIN hh_master_equip masterEq ON hotelEq.equip_id = masterEq.equip_id ";
        query = query + "   LEFT JOIN hh_rsv_rel_room_equip roomEq ON roomEq.id = hotelEq.id AND roomEq.equip_id = hotelEq.equip_id ";
        query = query + "WHERE hotelEq.id = ? ";
        query = query + " AND ( roomEq.seq = ? AND hotelEq.equip_type IN (1, 2 ) OR hotelEq.equip_type = 3) GROUP BY equip_id  ";
        query = query + "ORDER BY  masterEq.input_flag6, masterEq.sort_display";

        return query;
    }

    /**
     * 設備情報登録処理
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    public boolean registEquip() throws Exception
    {
        boolean ret = false;
        int hotelId = 0;
        int seq = 0;

        try
        {
            hotelId = frm.getSelHotelID();
            seq = frm.getSeq();

            // 予約部屋マスタ更新
            if ( isExistsRsvRoom( hotelId, seq ) == false )
            {
                // 存在しない場合は新規追加
                ret = execInsRsvRoom();
            }
            else
            {
                // 存在する場合は更新
                ret = execUpdRsvRoom();
            }

            // 設備情報更新
            execRegEquipID( hotelId, seq, frm.getEquipIdList() );

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvRoom.registEquip] Exception=" + e.toString() );
            ret = false;
            throw new Exception( e );
        }
        ret = true;
        return ret;
    }

    /**
     * 設備情報を登録する。
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号
     * @param selEqIdList 画面で選択され手いる設備IDのリスト
     * @return ArrayList<Integer> 設備IDのリスト
     */
    private void execRegEquipID(int hotelId, int seq, ArrayList<Integer> selEqIdList) throws Exception
    {

        boolean isExists = false;
        ArrayList<Integer> regEqIdList = new ArrayList<Integer>();
        DataRsvRelRoomEquip rsvData = new DataRsvRelRoomEquip();
        int regEqId = 0;

        try
        {
            // 現在登録済みの設備情報取得
            isExists = rsvData.getData( hotelId, seq );

            if ( isExists == false )
            {
                // データが存在しない場合は、全て新規登録
                for( int i = 0 ; i < selEqIdList.size() ; i++ )
                {
                    execInsEquip( selEqIdList.get( i ) );
                }
            }

            // ▼データが存在する場合
            // 選択済み設備情報を登録
            for( int i = 0 ; i < selEqIdList.size() ; i++ )
            {
                // 該当データが存在するか
                isExists = rsvData.getData( hotelId, seq, selEqIdList.get( i ) );
                if ( isExists == false )
                {
                    // 存在しない場合のみ、追加
                    execInsEquip( selEqIdList.get( i ) );
                }

            }

            // 登録済み設備情報を削除
            isExists = rsvData.getData( hotelId, seq );
            if ( isExists == false )
            {
                return;
            }

            regEqIdList = rsvData.getEquipIdList();

            for( int i = 0 ; i < regEqIdList.size() ; i++ )
            {
                regEqId = regEqIdList.get( i );
                isExists = false;

                for( int j = 0 ; j < selEqIdList.size() ; j++ )
                {
                    // 選択済み設備情報にあるか
                    if ( regEqId == selEqIdList.get( j ) )
                    {
                        // 存在する
                        isExists = true;
                    }
                }

                // 存在しない場合は、削除
                if ( isExists == false )
                {
                    execDelEquip( regEqId );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvRoom.execRegEquipID] Exception=" + e.toString() );
            throw new Exception( e );
        }
    }

    /**
     * ホテルに登録されている設備情報取得
     * 
     * @param hotelId ホテルID
     * @return ArrayList<Integer> 設備IDのリスト
     */
    public ArrayList<Integer> getAllEquipIdList(int hotelId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> allEqIdList = new ArrayList<Integer>();

        // ホテルに紐づく設備情報取得
        query = createSqlAllEq();

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                allEqIdList.add( result.getInt( "equip_id" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvRoom.getEquip] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return allEqIdList;
    }

    /**
     * 予約部屋マスタ存在チェック
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号
     * @return True:存在する、False:存在しない
     */
    private boolean isExistsRsvRoom(int hotelID, int seq) throws Exception
    {
        boolean isExists = false;
        DataRsvRoom rsvData = new DataRsvRoom();

        // 設備情報の取得
        isExists = rsvData.getData( hotelID, seq );
        if ( isExists == false )
        {
            // データなし
            return isExists;
        }

        // データあり
        if ( rsvData.getId() != 0 )
        {
            isExists = true;
        }

        return isExists;
    }

    /**
     * 部屋・設備情報追加処理
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    private boolean execInsEquip(int equipId) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_rsv_rel_room_equip SET ";
        query = query + " id = ?,";
        query = query + " seq = ?,";
        query = query + " equip_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, frm.getSeq() );
            prestate.setInt( 3, equipId );

            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvRoom.execInsEquip] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvRoom.execInsEquip] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 部屋・設備情報削除処理
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    private boolean execDelEquip(int equipId) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "DELETE FROM hh_rsv_rel_room_equip ";
        query = query + " WHERE  id = ? ";
        query = query + "   AND seq = ? ";
        query = query + "   AND equip_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, frm.getSeq() );
            prestate.setInt( 3, equipId );

            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvRoom.execDelEquip] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvRoom.execDelEquip] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 予約部屋マスタ追加
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    private boolean execInsRsvRoom() throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_rsv_room SET ";
        query = query + " id = ?,";
        query = query + " seq = ?,";
        query = query + " room_name = ?,";
        query = query + " room_pr = ?,";
        query = query + " remarks = ?,";
        query = query + " sales_flag = ?,";
        query = query + " hotel_id = ?,";
        query = query + " user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, frm.getSeq() );
            prestate.setString( 3, ConvertCharacterSet.convForm2Db( frm.getRoomNameInput() ) );
            prestate.setString( 4, ConvertCharacterSet.convForm2Db( frm.getRoomPRInput() ) );
            prestate.setString( 5, ConvertCharacterSet.convForm2Db( frm.getRoomTextInput() ) );
            prestate.setInt( 6, 1 );
            prestate.setString( 7, frm.getOwnerHotelID() );
            prestate.setInt( 8, frm.getUserID() );
            prestate.setInt( 9, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 10, Integer.parseInt( DateEdit.getTime( 1 ) ) );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvRoom.execInsRsvRoom] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvRoom.execInsRsvRoom] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 予約部屋マスタ更新
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    private boolean execUpdRsvRoom() throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_room SET ";
        query = query + " room_name = ?,";
        query = query + " room_pr = ?,";
        query = query + " remarks = ?,";
        query = query + " hotel_id = ?,";
        query = query + " user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " WHERE id = ?";
        query = query + "   AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, ConvertCharacterSet.convForm2Db( frm.getRoomNameInput() ) );
            prestate.setString( 2, ConvertCharacterSet.convForm2Db( frm.getRoomPRInput() ) );
            prestate.setString( 3, ConvertCharacterSet.convForm2Db( frm.getRoomTextInput() ) );
            prestate.setString( 4, frm.getOwnerHotelID() );
            prestate.setInt( 5, frm.getUserID() );
            prestate.setInt( 6, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 7, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 8, frm.getSelHotelID() );
            prestate.setInt( 9, frm.getSeq() );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvRoom.execUpdRsvRoom] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvRoom.execUpdRsvRoom] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ランク取得
     * 
     * @param なし
     * @return なし
     */
    public boolean getRankName() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;
        int count = 0;

        query = query + "SELECT rank.rank_name ";
        query = query + "FROM hh_hotel_roomrank rank ";
        query = query + "WHERE rank.id = ? ";
        query = query + "  AND rank.room_rank = ? ";
        query = query + "  AND rank.room_rank <> 0 ";
        query = query + "  AND rank.disp_index <> 0 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, frm.getRoomRank() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                frm.setRankName( result.getString( "rank_name" ) );
                ret = true;
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                ret = false;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvRoom.getRoomDetailData] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }
}
