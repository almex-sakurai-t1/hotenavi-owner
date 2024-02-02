package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReplaceString;

/**
 * 設備設定ビジネスロジック
 */
public class LogicOwnerRsvEquipManage implements Serializable
{
    /**
     *
     */
    private static final long       serialVersionUID = 5891708921740621298L;

    private static final String     ROOM_ALL         = "全室";
    private static final String     EQTYPE_BATH      = "バス関連";
    private static final String     EQTYPE_TV        = "テレビ関連";
    private static final String     EQTYPE_AMUSEMENT = "アミューズメント関連";
    private static final String     EQTYPE_ROOM      = "部屋関連";
    private static final String     EQTYPE_EQUIP     = "設備関連";
    private static final String     EQTYPE_SALES     = "販売関連";
    private static final String     EQTYPE_RENTAL    = "レンタル関連";

    private static final int        ID_ALL           = 1;
    private static final int        ID_BATH          = 2;
    private static final int        ID_TV            = 3;
    private static final int        ID_AMUSEMENT     = 4;
    private static final int        ID_ROOM          = 5;
    private static final int        ID_EQUIP         = 6;
    private static final int        ID_SALES         = 7;
    private static final int        ID_RENTAL        = 8;

    private FormOwnerRsvEquipManage frm;

    /* フォームオブジェクト */
    public FormOwnerRsvEquipManage getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvEquipManage frm)
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
        int inpTypeFlg6 = 0;
        int inpTypeFlg2 = 0;
        String eqTypeNm = "";
        int roomCnt = 0;
        int equipTypeId = 0;
        String strSeq = "";
        PreparedStatement prestate = null;
        ArrayList<Integer> eqIdList = new ArrayList<Integer>();
        ArrayList<String> eqNmList = new ArrayList<String>();
        ArrayList<Integer> inpFlg6List = new ArrayList<Integer>();
        ArrayList<Integer> inpFlg2List = new ArrayList<Integer>();
        ArrayList<Integer> dispList = new ArrayList<Integer>();
        ArrayList<Integer> eqTypeList = new ArrayList<Integer>();
        ArrayList<Integer> eqTypeIdList = new ArrayList<Integer>();
        ArrayList<String> eqTypeNmList = new ArrayList<String>();
        ArrayList<String> eqBranchNmList = new ArrayList<String>();
        ArrayList<String> roomNmList = new ArrayList<String>();

        int count = 0;

        query = query + "SELECT ";
        query = query + "hotelEq.equip_id, masterEq.name, masterEq.input_flag6, masterEq.sort_display, reHotelEq.equip_type, masterEq.branch_name1, masterEq.input_flag2 ";
        query = query + "FROM hh_hotel_equip hotelEq ";
        query = query + "  LEFT JOIN hh_master_equip masterEq ON hotelEq.equip_id = masterEq.equip_id ";
        query = query + "  LEFT JOIN research_hotel_equip reHotelEq ON hotelEq.id = reHotelEq.id AND hotelEq.equip_id = reHotelEq.equip_id ";
        query = query + "WHERE hotelEq.id = ? ";
        query = query + "ORDER BY masterEq.branch_name1, masterEq.input_flag2, masterEq.input_flag6, masterEq.sort_display ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                eqIdList.add( result.getInt( "equip_id" ) );
                eqNmList.add( ReplaceString.HTMLEscape( CheckString.checkStringForNull( result.getString( "name" ) ) ) );
                inpTypeFlg6 = result.getInt( "input_flag6" );
                inpFlg6List.add( inpTypeFlg6 );
                inpTypeFlg2 = result.getInt( "input_flag2" );
                inpFlg2List.add( inpTypeFlg2 );
                dispList.add( result.getInt( "sort_display" ) );
                eqTypeList.add( result.getInt( "equip_type" ) );
                eqBranchNmList.add( ReplaceString.HTMLEscape( CheckString.checkStringForNull( result.getString( "branch_name1" ) ) ) );
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
                eqTypeIdList.add( equipTypeId );
                eqTypeNmList.add( eqTypeNm );
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

            // ホテルに紐づく全部屋数取得
            roomCnt = getRoomCnt( frm.getSelHotelID() );
            // 設備に紐づく部屋番号取得
            for( int i = 0 ; i < eqIdList.size() ; i++ )
            {
                strSeq = getEquipRoom( eqIdList.get( i ), roomCnt );
                roomNmList.add( strSeq );
            }

            // フォームにセット
            frm.setEqIdList( eqIdList );
            frm.setEqNmList( eqNmList );
            frm.setInpFlg6List( inpFlg6List );
            frm.setInpFlg2List( inpFlg2List );
            frm.setDspList( dispList );
            frm.setEqTypeList( eqTypeList );
            frm.setEqTypeNmList( eqTypeNmList );
            frm.setEqBranchNmList( eqBranchNmList );
            frm.setEqTypeIdList( eqTypeIdList );
            frm.setSeqList( roomNmList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvEquipManage.getEquipData] Exception=" + e.toString() );
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
     * @param equipId 管理番号
     * @param allRoomCnt ホテルの持つ部屋総数
     * @return 設備を備えている部屋番号の文字列
     */
    public String getEquipRoom(int equipId, int allRoomCnt) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        String eqTypeNm = "";
        PreparedStatement prestate = null;
        int count = 0;

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
            prestate.setInt( 2, equipId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( eqTypeNm.trim().length() != 0 )
                {
                    eqTypeNm = eqTypeNm + " ";
                }
                eqTypeNm = eqTypeNm + result.getString( "seq" );
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            if ( allRoomCnt == count )
            {
                eqTypeNm = ROOM_ALL;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvEquipManage.getEquipData] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(eqTypeNm);
    }

    /**
     * ホテルに紐づく部屋数取得
     * 
     * @param iD ホテルID
     * @return 件数
     */
    private int getRoomCnt(int Id) throws Exception
    {
        // 変数定義
        int ret = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT COUNT(*) AS COUNT ";
        query = query + "FROM hh_hotel_roomrank hrr ";
        query = query + "  LEFT JOIN hh_hotel_room_more hr ON hrr.id = hr.id AND hrr.room_rank = hr.room_rank ";
        query = query + "     LEFT JOIN hh_rsv_room rsvroom ON hr.id = rsvroom.id and hr.seq = rsvroom.seq ";
        query = query + "WHERE hrr.id = ? ";
        query = query + "  AND (hr.disp_flag = 0 or hr.disp_flag = 1) ";
        query = query + "  AND hrr.room_rank <> 0 ";
        query = query + "  AND (hrr.disp_index BETWEEN 1 AND 98) ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() == true )
                {
                    ret = result.getInt( "Count" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvEquipManage.getRoomCnt] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
