package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataRsvReserveBasic;

/**
 * 予約情報登録クラス
 */
public class LogicOwnerRsvReserveBasic implements Serializable
{
    private static final long        serialVersionUID = 4113655459687204977L;
    private FormOwnerRsvReserveBasic frm;

    /* フォームオブジェクト */
    public FormOwnerRsvReserveBasic getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvReserveBasic frm)
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
        int count = 0;

        query = query + "SELECT id, deadline_time, cash_deposit, format(cash_deposit,0) as cashDepositView ,noshow_credit_flag, parking FROM hh_rsv_reserve_basic ";
        query = query + "WHERE id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            result = prestate.executeQuery();
            while( result.next() )
            {
                frm.setHotelId( result.getInt( "id" ) );
                frm.setDeadlineTimeView( ConvertTime.convTimeHHMM( result.getInt( "deadline_time" ), 0 ) );
                frm.setDeadlineTimeHH( ConvertTime.convTimeHH( result.getInt( "deadline_time" ) ) );
                frm.setDeadlineTimeMM( ConvertTime.convTimeMM( result.getInt( "deadline_time" ) ) );
                frm.setCashDeposit( result.getString( "cash_deposit" ) );
                frm.setCashDepositView( result.getString( "cashDepositView" ) );
                frm.setParking( result.getInt( "parking" ) );
                frm.setNoshowCreditFlag( result.getInt( "noshow_credit_flag" ) );
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
            Logging.error( "[LogicOwnerRsvReserveBasic.getHotelRsv] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
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
            // 更新対象の値をセットする
            data.setId( frm.getHotelId() );
            data.setDeadline_time( ConvertTime.convTimeSS( Integer.parseInt( frm.getDeadlineTimeHH() ), Integer.parseInt( frm.getDeadlineTimeMM() ), 2 ) );
            data.setReservePr( rsvcomm.getChildChargeInfo( String.valueOf( frm.getHotelId() ) ) );
            data.setCashDeposit( Integer.parseInt( frm.getCashDeposit() ) );
            if ( frm.getParking() == 1 )
            {
                // 駐車場入力あり台数あり
                if ( frm.getHiroof() == 1 )
                {
                    // ハイルーフ可
                    frm.setParking( ReserveCommon.PARKING_INPUT_COUNT_HIROOF );
                }
                else if ( frm.getHiroof() == 2 )
                {
                    // ハイルーフ不可
                    frm.setParking( ReserveCommon.PARKING_INPUT_COUNT_NOHIROOF );
                }
                else if ( frm.getHiroof() == 3 )
                {
                    // どちらでもよい
                    frm.setParking( ReserveCommon.PARKING_INPUT_COUNT );
                }
            }
            else if ( frm.getParking() == 2 )
            {
                // 駐車場入力あり台数なし
                if ( frm.getHiroof() == 1 )
                {
                    // ハイルーフ可
                    frm.setParking( ReserveCommon.PARKING_INPUT_NOCOUNT_HIROOF );
                }
                else if ( frm.getHiroof() == 2 )
                {
                    // ハイルーフ不可
                    frm.setParking( ReserveCommon.PARKING_INPUT_NOCOUNT_NOHIROOF );
                }
                else if ( frm.getHiroof() == 3 )
                {
                    // どちらでもよい
                    frm.setParking( ReserveCommon.PARKING_INPUT_NOCOUNT );
                }
            }
            else
            {
                // 駐車場入力なし
                frm.setParking( ReserveCommon.PARKING_NO_INPUT );
            }
            data.setParking( frm.getParking() );
            data.setParkingCount( 0 );
            data.setCancelPolicy( rsvcomm.getDefaultCancelPolicy() );
            data.setSalesFlag( 0 );
            data.setHotelId( frm.getOwnerHotelID() );
            data.setUserId( frm.getUserID() );
            data.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            data.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            data.setNoshow_credit_flag( frm.getNoshowCreditFlag() );
            data.setEquipDispFlag( frm.getEquipDispFlag() );

            // 該当の予約基本データがあるか
            existsData = isExistsRsvBasic( frm.getHotelId() );
            if ( existsData == false )
            {
                // データなしの場合は、新規追加
                data.insertData();
            }
            else
            {
                // データあり時は更新
                ExecUpdRsvBasic( data.getCancelPolicy() );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvReserveBasic.RegistHotelBase] Exception=" + e.toString() );
            ret = false;
            throw new Exception( e );
        }
        ret = true;
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
     * @param cancelPolicy キャンセルポリシー文言
     * @return なし
     * @throws Exception
     */
    private boolean ExecUpdRsvBasic(String cancelPolicy) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        int deadLine = 0;

        ret = false;

        query = "UPDATE hh_rsv_reserve_basic SET ";
        query = query + " deadline_time = ?, ";
        query = query + " cash_deposit = ?,";
        query = query + " parking = ?,";
        query = query + " hotel_id = ?,";
        query = query + " user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " noshow_credit_flag = ?,";
        query = query + " cancel_policy = ?,";
        query = query + " equip_disp_flag = ?";
        query = query + " WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            deadLine = ConvertTime.convTimeSS( Integer.parseInt( frm.getDeadlineTimeHH() ), Integer.parseInt( frm.getDeadlineTimeMM() ), 2 );
            prestate.setInt( 1, deadLine );
            prestate.setString( 2, frm.getCashDeposit() );
            prestate.setInt( 3, frm.getParking() );
            prestate.setString( 4, frm.getOwnerHotelID() );
            prestate.setInt( 5, frm.getUserID() );
            prestate.setInt( 6, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 7, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 8, frm.getNoshowCreditFlag() );
            prestate.setString( 9, cancelPolicy );
            prestate.setInt( 10, frm.getEquipDispFlag() );
            prestate.setInt( 11, frm.getHotelId() );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvReserveBasic.ExecUpdRsvBasic] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvReserveBasic.ExecUpdRsvBasic] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }
}
