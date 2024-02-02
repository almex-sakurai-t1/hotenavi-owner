package jp.happyhotel.reserve;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.ReserveCommon;

/**
 * 予約入力初期表示（ＰＣ）ビジネスロジック
 */
public class LogicReserveInitPC implements Serializable
{

    private static final long         serialVersionUID = -8518144710738342742L;
    private FormReservePersonalInfoPC frm;

    public FormReservePersonalInfoPC getFrm()
    {
        return frm;
    }

    public void setFrm(FormReservePersonalInfoPC frm)
    {
        this.frm = frm;
    }

    /**
     * 部屋設備情報取得
     * 
     * @param id ホテルID
     * @param roomno 部屋番号
     * @throws Exception
     */
    public void getEquip(int id, int roomno) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> equipIdList = new ArrayList<Integer>();
        ArrayList<String> equipNmList = new ArrayList<String>();
        ArrayList<Integer> sortDispList = new ArrayList<Integer>();
        ArrayList<String> equipPassList = new ArrayList<String>();
        ArrayList<Integer> equipTypeList = new ArrayList<Integer>();
        ArrayList<String> equipBranchNmList = new ArrayList<String>();

        query = query + "SELECT ";
        query = query + "hotelEq.equip_id, masterEq.name, masterEq.input_flag6, masterEq.sort_display, hotelEq.equip_type, masterEq.branch_name1 ";
        query = query + "FROM hh_hotel_equip hotelEq ";
        query = query + "   LEFT JOIN hh_master_equip masterEq ON hotelEq.equip_id = masterEq.equip_id ";
        query = query + "   LEFT JOIN hh_rsv_rel_room_equip roomEq ON roomEq.id = hotelEq.id AND roomEq.equip_id = hotelEq.equip_id ";
        query = query + "WHERE hotelEq.id = ? ";
        query = query + " AND ( roomEq.seq = ? AND hotelEq.equip_type IN (1, 2 ) OR hotelEq.equip_type = 3) GROUP BY equip_id  ";
        query = query + "ORDER BY  masterEq.input_flag6, masterEq.sort_display";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, roomno );
            result = prestate.executeQuery();

            if ( result != null )
            {
                while( result.next() )
                {
                    equipIdList.add( result.getInt( "equip_id" ) );
                    equipNmList.add( ConvertCharacterSet.convDb2Form( result.getString( "name" ) ) );
                    sortDispList.add( result.getInt( "sort_display" ) );
                    equipPassList.add( String.valueOf( result.getInt( "equip_id" ) ) );
                    equipTypeList.add( result.getInt( "equip_type" ) );
                    equipBranchNmList.add( ConvertCharacterSet.convDb2Form( result.getString( "branch_name1" ) ) );
                }
            }

            frm.setEquipIdList( equipIdList );
            frm.setEquipNmList( equipNmList );
            frm.setEquipSortList( sortDispList );
            frm.setEquipPassList( equipPassList );
            frm.setEquipTypeList( equipTypeList );
            frm.setEquipBranchNameList( equipBranchNmList );
        }
        catch ( Exception e )
        {
            Logging.info( "[LogicReserveInitPC.getEquip() ] " + e.getMessage() );
            throw new Exception( "[LogicReserveInitPC.getEquip() ] " + e.getMessage() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * プラン設備情報取得
     * 
     * @param id ホテルID
     * @param planSeqList プラン部屋番号リスト
     * @throws Exception
     */
    public void getEquipPlan(int id, ArrayList<Integer> planSeqList) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> equipIdList = new ArrayList<Integer>();
        ArrayList<String> equipNmList = new ArrayList<String>();
        ArrayList<String> equipPassList = new ArrayList<String>();
        ArrayList<Integer> sortDisplayList = new ArrayList<Integer>();
        ArrayList<Integer> equipTypeList = new ArrayList<Integer>();
        ArrayList<String> equipBranchNmList = new ArrayList<String>();
        int equiptype = 0;

        query = query + "SELECT ";
        query = query + "hotelEq.id, hotelEq.equip_id, masterEq.name , hotelEq.equip_type, masterEq.input_flag6, masterEq.sort_display, masterEq.branch_name1 ";
        query = query + "FROM hh_hotel_equip hotelEq ";
        query = query + "  LEFT JOIN hh_master_equip masterEq ON hotelEq.equip_id = masterEq.equip_id ";
        query = query + "WHERE hotelEq.id = ? ";
        query = query + "  AND hotelEq.equip_type IN (1,2,3 ) ";
        query = query + " ORDER BY masterEq.input_flag6, masterEq.sort_display";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();

            if ( result != null )
            {
                while( result.next() )
                {
                    equipIdList.add( result.getInt( "equip_id" ) );
                    equipNmList.add( ReplaceString.HTMLEscape( ConvertCharacterSet.convDb2Form( result.getString( "name" ) ) ) );
                    sortDisplayList.add( result.getInt( "sort_display" ) );
                    equipPassList.add( String.valueOf( result.getInt( "equip_id" ) ) );
                    equiptype = result.getInt( "equip_type" );
                    if ( result.getInt( "equip_type" ) == 1 || result.getInt( "equip_type" ) == 2 || result.getInt( "equip_type" ) == 3 )
                    {
                        int checkEquipType = result.getInt( "equip_type" );
                        if ( checkEquipType == 1 && (ReplaceString.HTMLEscape( ConvertCharacterSet.convDb2Form( result.getString( "branch_name1" ) ) ).equals( "販売" ) == true) )
                        {
                            checkEquipType = 4;
                        }
                        // 紐付いている部屋が全部屋対応か一部か判定を行なってセットする(レンタル以外)
                        if ( result.getInt( "equip_type" ) != 3 )
                        {
                            equiptype = ReserveCommon.checkRoomEquipList( id, result.getInt( "equip_id" ), planSeqList, checkEquipType );
                        }
                    }
                    equipTypeList.add( equiptype );
                    equipBranchNmList.add( ReplaceString.HTMLEscape( ConvertCharacterSet.convDb2Form( result.getString( "branch_name1" ) ) ) );
                }
            }

            frm.setEquipIdList( equipIdList );
            frm.setEquipNmList( equipNmList );
            frm.setEquipPassList( equipPassList );
            frm.setEquipSortList( sortDisplayList );
            frm.setEquipTypeList( equipTypeList );
            frm.setEquipBranchNameList( equipBranchNmList );
        }
        catch ( Exception e )
        {
            Logging.info( "[LogicReserveInitPC.getEquipPlan() ] " + e.getMessage() );
            throw new Exception( "[LogicReserveInitPC.getEquipPlan() ] " + e.getMessage() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }
}
