package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataRsvReserveBasic;

/**
 * 施設情報登録クラス
 */
public class LogicOwnerRsvHotelBasic implements Serializable
{
    private static final long      serialVersionUID  = -5193250619798676025L;
    // 18禁フラグの値
    private static final String    MSG_OVER18_OK     = "通常";
    private static final String    MSG_OVER18_NG     = "18禁";
    private static final String    MSG_OVER18_RYOKAN = "旅館業法";
    private FormOwnerRsvHotelBasic frm;

    /* フォームオブジェクト */
    public FormOwnerRsvHotelBasic getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvHotelBasic frm)
    {
        this.frm = frm;
    }

    /**
     * ホテル予約情報取得
     * 
     * @param なし
     * @return
     */
    public void getHotelRsv() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ReserveCommon rsvcomm = null;
        int count = 0;
        String zipCd = "";
        String zipCdView = "";

        query = query + "SELECT hb.id, hb.name, hb.zip_code, concat(concat(hb.address1, hb.address2), hb.address3) as address, hb.tel1, ";
        query = query + "hb.room_count, hb.over18_flag, rrb.reserve_pr, rrb.child_charge_info, rrb.ci_info,rrb.co_info ";
        query = query + "FROM hh_hotel_basic hb LEFT JOIN hh_rsv_reserve_basic rrb ON hb.id = rrb.id ";
        query = query + "WHERE hb.id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            result = prestate.executeQuery();
            while( result.next() )
            {
                frm.setHotelId( result.getInt( "id" ) );
                frm.setHotelName( ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( result.getString( "name" ) ) ) );
                zipCd = CheckString.checkStringForNull( result.getString( "zip_code" ) );
                if ( zipCd.trim().length() != 0 )
                {
                    zipCdView = zipCd.substring( 0, 3 ) + "-" + zipCd.substring( 3 );
                }
                frm.setZipCode( zipCdView );
                frm.setAddress( ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( result.getString( "address" ) ) ) );
                frm.setTel1( CheckString.checkStringForNull( result.getString( "tel1" ) ) );
                frm.setRoomCnt( result.getInt( "room_count" ) );
                frm.setReservePr( ConvertCharacterSet.convDb2Form( (CheckString.checkStringForNull( result.getString( "reserve_pr" ) )).trim() ) );
                if ( result.getString( "child_charge_info" ) == null || result.getString( "child_charge_info" ).equals( "" ) )
                {
                    rsvcomm = new ReserveCommon();
                    String childChargeInfo = rsvcomm.getChildChargeInfo( result.getString( "id" ) );
                    frm.setChildCharge( childChargeInfo );
                    frm.setChildChargeView( childChargeInfo );
                }
                else
                {
                    frm.setChildCharge( ConvertCharacterSet.convDb2Form( (CheckString.checkStringForNull( result.getString( "child_charge_info" ) )).trim() ) );
                    frm.setChildChargeView( ConvertCharacterSet.convDb2Form( (CheckString.checkStringForNull( result.getString( "child_charge_info" ) )).trim() ) );
                }
                frm.setReservePrView( ConvertCharacterSet.convDb2Form( (ReplaceString.HTMLEscape( (CheckString.checkStringForNull( result.getString( "rrb.reserve_pr" ) )).trim() )) ) );
                frm.setOver18Flag( result.getInt( "hb.over18_flag" ) );
                if ( result.getInt( "hb.over18_flag" ) == 0 )
                {
                    frm.setOver18Value( MSG_OVER18_OK );
                }
                else if ( result.getInt( "hb.over18_flag" ) == 1 )
                {
                    frm.setOver18Value( MSG_OVER18_NG );
                }
                else
                {
                    frm.setOver18Value( MSG_OVER18_RYOKAN );
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
                frm.setErrMsg( Message.getMessage( "erro.30001", "ホテル予約基本情報" ) );
                return;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvHotelBasic.getHotelRsv] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * ホテル基本情報登録
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean registHotelBase() throws Exception
    {
        boolean existsData;
        boolean ret = false;
        DataRsvReserveBasic data = new DataRsvReserveBasic();
        ReserveCommon rsvcomm = new ReserveCommon();

        try
        {
            // データクラスに値をセット
            data.setId( frm.getHotelId() );
            data.setReservePr( ConvertCharacterSet.convForm2Db( frm.getReservePr() ) );
            data.setCashDeposit( 0 );
            data.setParking( 1 );
            data.setParkingCount( 0 );
            data.setCancelPolicy( rsvcomm.getDefaultCancelPolicy() );
            data.setSalesFlag( 0 );
            data.setHotelId( frm.getOwnerHotelID() );
            data.setUserId( frm.getUserID() );
            data.setChild_charge( frm.getChildCharge() );
            data.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            data.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

            // 該当の予約基本データがあるか
            existsData = isExistsRsvBasic( frm.getHotelId() );
            if ( existsData == false )
            {
                // データなしの場合は、新規追加
                ret = data.insertData();
            }
            else
            {
                // データあり時は更新
                ExecUpdRsvBasic( data.getCancelPolicy() );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvHotelBasic.RegistHotelBase] Exception=" + e.toString() );
            ret = false;
            throw new Exception( e );
        }
        return ret;
    }

    /**
     * 予約基本情報存在チェック
     * 
     * @param hotelId ホテルID
     * @return True:存在する、False:存在しない
     */
    private boolean isExistsRsvBasic(int hotelID) throws Exception
    {
        boolean isExists = false;
        DataRsvReserveBasic rsvBasic = new DataRsvReserveBasic();

        // 予約基本データの取得
        isExists = rsvBasic.getData( hotelID );
        if ( isExists == false )
        {
            // データなし
            return isExists;
        }

        // データあり
        if ( rsvBasic.getID() != 0 )
        {
            isExists = true;
        }

        return isExists;
    }

    /**
     * 予約基本情報更新処理
     * 
     * @param cancelpolicy キャンセルポリシー文言
     * @return なし
     * @throws Exception
     */
    private boolean ExecUpdRsvBasic(String cancelpolicy) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_reserve_basic SET ";
        query = query + " reserve_pr = ?,";
        query = query + " ci_info = ?,";
        query = query + " co_info = ?,";
        query = query + " hotel_id = ?,";
        query = query + " user_id = ?,";
        query = query + " child_charge_info = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " cancel_policy = ?";
        query = query + " WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, ConvertCharacterSet.convForm2Db( frm.getReservePr() ) ); // 予約用PR
            prestate.setString( 2, "" ); // チェックイン情報
            prestate.setString( 3, "" ); // チェックアウト情報
            prestate.setString( 4, frm.getOwnerHotelID() ); // ホテルID
            prestate.setInt( 5, frm.getUserID() ); // ユーザID
            prestate.setString( 6, ConvertCharacterSet.convForm2Db( frm.getChildCharge() ) ); // 子供料金定義
            prestate.setInt( 7, Integer.parseInt( DateEdit.getDate( 2 ) ) ); // 最終更新日
            prestate.setInt( 8, Integer.parseInt( DateEdit.getTime( 1 ) ) ); // 最終更新時刻
            prestate.setString( 9, cancelpolicy ); // キャンセルポリシー
            prestate.setInt( 10, frm.getHotelId() ); // ホテルID

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvHotelBasic.ExecUpdRsvBasic] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvHotelBasic.ExecUpdRsvBasic] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }
}
