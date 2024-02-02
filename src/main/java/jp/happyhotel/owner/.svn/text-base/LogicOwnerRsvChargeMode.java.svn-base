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

/**
 * 料金モード設定ビジネスロジック
 */
public class LogicOwnerRsvChargeMode implements Serializable
{

    private static final long      serialVersionUID = 7842036061138797503L;

    private FormOwnerRsvChargeMode frm;

    /* フォームオブジェクト */
    public FormOwnerRsvChargeMode getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvChargeMode frm)
    {
        this.frm = frm;
    }

    /**
     * 料金モード情報取得
     * 
     * @param なし
     * @return なし
     */
    public void getInit() throws Exception
    {
        int cnt = 0;
        String modeNm = "";
        String remarks = "";
        int editFlg = 0;
        ArrayList<Integer> modeIdList = new ArrayList<Integer>();
        ArrayList<String> modeNmList = new ArrayList<String>();
        ArrayList<String> remarksList = new ArrayList<String>();
        ArrayList<Integer> editFlgList = new ArrayList<Integer>();
        ArrayList<Integer> checkDelList = new ArrayList<Integer>();
        ArrayList<Integer> dispList = new ArrayList<Integer>();

        // 料金モード情報取得
        cnt = getChargeModeData();

        for( int i = 1 ; i <= 20 ; i++ )
        {
            editFlg = 0;
            modeNm = "";

            if ( cnt != 0 )
            {
                // 料金モードデータがある場合
                return;
            }

            // 料金モードデータがない場合
            modeIdList.add( i );
            switch( i )
            {
                case 1:
                    modeNm = "平日";
                    editFlg = 1;
                    break;
                case 2:
                    modeNm = "休前日";
                    editFlg = 1;
                    break;
                case 3:
                    modeNm = "休日";
                    editFlg = 1;
                    break;
            }
            modeNmList.add( modeNm );
            remarksList.add( remarks );
            editFlgList.add( editFlg );
            checkDelList.add( 0 );
            dispList.add( 0 );
        }

        // フォームにセット
        frm.setChargeModeIdList( modeIdList );
        frm.setChargeModeNmList( modeNmList );
        frm.setRemarks( remarksList );
        frm.setEditFlag( editFlgList );
        frm.setCheckDel( checkDelList );
        frm.setDispList( dispList );

    }

    /**
     * 料金モード情報取得
     * 
     * @param なし
     * @return int 登録件数
     */
    private int getChargeModeData() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        int cnt = 0;
        PreparedStatement prestate = null;
        ArrayList<Integer> modeIdList = new ArrayList<Integer>();
        ArrayList<String> modeNmList = new ArrayList<String>();
        ArrayList<String> remarksList = new ArrayList<String>();
        ArrayList<Integer> editFlgList = new ArrayList<Integer>();
        ArrayList<Integer> checkDelList = new ArrayList<Integer>();
        ArrayList<Integer> dispList = new ArrayList<Integer>();

        query = query + "SELECT * FROM hh_rsv_charge_mode WHERE id = ? ";
        query = query + "ORDER BY charge_mode_id ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                modeIdList.add( result.getInt( "charge_mode_id" ) );
                modeNmList.add( ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( result.getString( "charge_mode_name" ) ) ) );
                remarksList.add( CheckString.checkStringForNull( result.getString( "remarks" ) ) );
                editFlgList.add( result.getInt( "edit_flag" ) );
                checkDelList.add( 0 );
                dispList.add( result.getInt( "disp_index" ) );
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                cnt = result.getRow();
            }

            // フォームにセット
            frm.setChargeModeIdList( modeIdList );
            frm.setChargeModeNmList( modeNmList );
            frm.setRemarks( remarksList );
            frm.setEditFlag( editFlgList );
            frm.setCheckDel( checkDelList );
            frm.setDispList( dispList );

            return cnt;

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvChargeMode.getChargeDataCnt] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 料金情報を登録する。
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号
     * @param selEqIdList 画面で選択され手いる設備IDのリスト
     * @return ArrayList<Integer> 設備IDのリスト
     */
    public void registChargeMode() throws Exception
    {
        int modeId = 0;
        String modeNm = "";
        String remarks = "";
        int editFlg = 0;
        int dispIndex = 0;

        try
        {
            // 料金モードデータ削除
            if ( execDelChargeMode() == false )
            {
                throw new Exception( "料金モードデータの削除に失敗しました。HotelID = " + frm.getSelHotelID() );
            }

            // 料金モードデータ追加
            for( int i = 0 ; i < frm.getChargeModeIdList().size() ; i++ )
            {
                modeId = frm.getChargeModeIdList().get( i );
                if ( frm.getCheckDel().get( i ) != 0 )
                {
                    modeNm = "";
                }
                else
                {
                    modeNm = frm.getChargeModeNmList().get( i );
                }
                remarks = frm.getRemarks().get( i );
                editFlg = frm.getEditFlag().get( i );
                dispIndex = frm.getDispList().get( i );
                if ( execInsChargeMode( modeId, modeNm, remarks, editFlg, dispIndex ) == false )
                {
                    throw new Exception( "料金モードデータの追加に失敗しました。HotelID = " + frm.getSelHotelID() + " : ModeID = " + modeId );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvChargeMode.registChargeMode] Exception=" + e.toString() );
            throw new Exception( e );
        }
    }

    /**
     * 料金モードデータ削除処理
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    private boolean execDelChargeMode() throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "DELETE FROM hh_rsv_charge_mode WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, frm.getSelHotelID() );

            result = prestate.executeUpdate();

            if ( result >= 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvChargeMode.execDelChargeMode] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvChargeMode.execDelChargeMode] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 料金モードデータ追加処理
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    private boolean execInsChargeMode(int modeId, String modeNm, String remarks, int editFlg, int dispIndex) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT INTO hh_rsv_charge_mode SET ";
        query = query + " id = ?, ";
        query = query + " charge_mode_id = ?, ";
        query = query + " charge_mode_name = ?, ";
        query = query + " remarks = ?, ";
        query = query + " edit_flag = ?, ";
        query = query + " hotel_id = ?,";
        query = query + " user_id = ?, ";
        query = query + " last_update = ?, ";
        query = query + " last_uptime = ?,";
        query = query + " disp_index = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, modeId );
            prestate.setString( 3, ConvertCharacterSet.convForm2Db( modeNm ) );
            prestate.setString( 4, ConvertCharacterSet.convForm2Db( remarks ) );
            prestate.setInt( 5, editFlg );
            prestate.setString( 6, frm.getOwnerHotelID() );
            prestate.setInt( 7, frm.getUserId() );
            prestate.setInt( 8, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 9, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 10, dispIndex );

            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvChargeMode.execInsChargeMode] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvChargeMode.execInsChargeMode] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * プラン別料金マスタが存在するか
     * 
     * @param hotelId ホテルID
     * @param priceModeId 料金モードID
     * @return true:存在する、False:存在しない
     */
    public boolean isExistsPlanCharge(int hotelId, int priceModeId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        int cnt = 0;
        boolean ret = false;
        PreparedStatement prestate = null;

        query = query + "SELECT COUNT(*) AS CNT FROM hh_rsv_plan_charge ";
        query = query + "WHERE id = ? ";
        query = query + "  AND charge_mode_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, priceModeId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            // レコード件数取得
            if ( cnt > 0 )
            {
                ret = true;
            }

            return ret;

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvChargeMode.isExistsPlanCharge] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

}
