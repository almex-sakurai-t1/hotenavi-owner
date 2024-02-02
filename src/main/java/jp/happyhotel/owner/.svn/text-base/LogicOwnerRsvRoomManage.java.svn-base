package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;

/**
 * 部屋管理ビジネスロジック
 */
public class LogicOwnerRsvRoomManage implements Serializable
{
    private static final long      serialVersionUID = -4497340002631090524L;

    private static final String    SALESFLG_NM_OK   = "予約受付中";
    private static final String    SALESFLG_NM_NG   = "予約停止中";
    private static final int       SALESFLG_START   = 1;

    private FormOwnerRsvRoomManage frm;

    /* フォームオブジェクト */
    public FormOwnerRsvRoomManage getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvRoomManage frm)
    {
        this.frm = frm;
    }

    /**
     * 部屋情報取得
     * 
     * @param なし
     * @return なし
     */
    public void getRoomData() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        ArrayList<Integer> idList = new ArrayList<Integer>();
        ArrayList<String> hotenaviList = new ArrayList<String>();
        ArrayList<Integer> rankIdList = new ArrayList<Integer>();
        ArrayList<String> rankList = new ArrayList<String>();
        ArrayList<Integer> seqList = new ArrayList<Integer>();
        ArrayList<String> nmList = new ArrayList<String>();
        ArrayList<Integer> salesFlgList = new ArrayList<Integer>();
        ArrayList<String> salesFlgNmList = new ArrayList<String>();
        ArrayList<Integer> imgList = new ArrayList<Integer>();
        ArrayList<String> referNameList = new ArrayList<String>();

        query = query + "SELECT ";
        query = query + "hrr.id, hb.hotenavi_id, hrr.room_rank, hrr.rank_name, hr.seq, hr.room_name, hr.room_picture_pc, rsvroom.sales_flag, hr.refer_name ";
        query = query + "FROM hh_hotel_roomrank hrr ";
        query = query + "  LEFT JOIN hh_hotel_room_more hr ON hrr.id = hr.id AND hrr.room_rank = hr.room_rank ";
        query = query + "     LEFT JOIN hh_rsv_room rsvroom ON hr.id = rsvroom.id and hr.seq = rsvroom.seq ";
        query = query + "       LEFT JOIN hh_hotel_basic hb ON hrr.id = hb.id ";
        query = query + "WHERE hrr.id = ? ";
        query = query + "  AND (hr.disp_flag = 0 or hr.disp_flag = 1) ";
        query = query + "  AND hrr.room_rank <> 0 ";
        query = query + "  AND (hrr.disp_index BETWEEN 1 AND 98) ";
        query = query + "ORDER BY hrr.disp_index, hr.seq";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                idList.add( result.getInt( "id" ) );
                hotenaviList.add( result.getString( "hotenavi_id" ) );
                rankIdList.add( result.getInt( "room_rank" ) );
                rankList.add( ConvertCharacterSet.convDb2Form( result.getString( "rank_name" ) ) );
                seqList.add( result.getInt( "seq" ) );
                nmList.add( ConvertCharacterSet.convDb2Form( result.getString( "room_name" ) ) );
                imgList.add( result.getInt( "hr.room_picture_pc" ) );
                salesFlgList.add( result.getInt( "sales_flag" ) );
                referNameList.add( result.getString( "refer_name" ) );
                if ( result.getInt( "sales_flag" ) == 1 )
                {
                    salesFlgNmList.add( SALESFLG_NM_OK );
                }
                else
                {
                    salesFlgNmList.add( SALESFLG_NM_NG );
                }
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "部屋情報" ) );
                return;
            }

            // Formに値をセット
            frm.setId( idList );
            frm.setHotenaviIdList( hotenaviList );
            frm.setRankIdList( rankIdList );
            frm.setRankNmList( rankList );
            frm.setSeq( seqList );
            frm.setRoomNmList( nmList );
            frm.setSalesFlgList( salesFlgList );
            frm.setSalesFlgNm( salesFlgNmList );
            frm.setImage( imgList );
            frm.setReferNameList( referNameList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerO10401.getRoomData] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 予約部屋マスタ追加
     * 
     * @param hotelId ホテルID
     * @param userId ユーザーID
     * @return 部屋データ挿入数（-1:データなし、0:データなし[挿入済],それ以外の整数:部屋データ挿入数）
     * @throws Exception
     */
    public int insertRsvRoom(int hotelId, String hotenaviId, int userId)
    {
        int count = -1;
        FormOwnerRsvRoomManage frmRoom;
        boolean ret = false;
        LogicOwnerRsvManage logic;
        logic = new LogicOwnerRsvManage();

        frmRoom = new FormOwnerRsvRoomManage();
        frmRoom.setSelHotelID( hotelId );

        try
        {
            // フォームをセット
            this.setFrm( frmRoom );
            // フォームから部屋データを取得
            this.getRoomData();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvRoomManage.insertRsvRoom] Exception :" + e.toString() );

        }

        // インサート対象のデータがあった場合
        if ( frm.getSeq() != null && frm.getSeq().size() > 0 )
        {
            count = 0;
            for( int i = 0 ; i < frm.getSeq().size() ; i++ )
            {
                try
                {
                    if ( frm.getSalesFlgList().get( i ) != 1 )
                    {
                        ret = logic.execInsRsvRoom( frm.getId().get( i ), frm.getSeq().get( i ), SALESFLG_START, userId, hotenaviId );
                        if ( ret != false )
                        {
                            count++;
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.error( "[LogicOwnerRsvRoomManage.insertRsvRoom] Insert Exception :" + e.toString() );
                }
            }
        }
        return(count);
    }
}
