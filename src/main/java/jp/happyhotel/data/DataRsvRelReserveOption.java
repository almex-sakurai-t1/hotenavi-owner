/*
 * 予約・オプション設定クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvRelReserveOption implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 4096210060406179728L;

    private int               iD;
    private int               reserveNo;
    private int               optionId;
    private int               optionSubId;
    private int               quantity;
    private int               unitPrice;
    private int               chargeTotal;
    private String            remarks;

    /**
     * データの初期化
     */
    public DataRsvRelReserveOption()
    {
        iD = 0;
        reserveNo = 0;
        optionId = 0;
        optionSubId = 0;
        quantity = 0;
        unitPrice = 0;
        chargeTotal = 0;
        remarks = "";
    }

    // getter
    public int getId()
    {
        return this.iD;
    }

    public int getReserveNo()
    {
        return this.reserveNo;
    }

    public int getOptionId()
    {
        return this.optionId;
    }

    public int getOptionSubId()
    {
        return this.optionSubId;
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    public int getUnitPricee()
    {
        return this.unitPrice;
    }

    public int getChargeTotal()
    {
        return this.chargeTotal;
    }

    public String getRemarks()
    {
        return this.remarks;
    }

    /**
     *
     * setter
     */
    public void setId(int iD)
    {
        this.iD = iD;
    }

    public void setReserveNo(int reserveNo)
    {
        this.reserveNo = reserveNo;
    }

    public void setOptionId(int optionId)
    {
        this.optionId = optionId;
    }

    public void setOptionSubId(int optionSubId)
    {
        this.optionSubId = optionSubId;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public void setUnitPrice(int unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public void setChargeTotal(int chargeTotal)
    {
        this.chargeTotal = chargeTotal;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    /**
     * プラン別料金情報取得
     *
     * @param iD ホテルID
     * @param reserveNo 予約番号
     * @param optionId オプションID
     * @param optionSubId オプションサブID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, int reserveNo, int optionId, int optionSubId)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, resreve_no, option_id, option_sub_id," +
                " quantity, unti_price, charge_total, remarks" +
                " FROM hh_rsv_rel_reserve_option WHERE id = ? AND resreve_no = ? AND option_id = ? AND option_sub_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, reserveNo );
            prestate.setInt( 3, optionId );
            prestate.setInt( 4, optionSubId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.reserveNo = result.getInt( "resreve_no" );
                    this.optionId = result.getInt( "option_id" );
                    this.optionSubId = result.getInt( "option_sub_id" );
                    this.quantity = result.getInt( "quantity" );
                    this.unitPrice = result.getInt( "unti_price" );
                    this.chargeTotal = result.getInt( "charge_total" );
                    this.remarks = result.getString( "remarks" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRelReserveOption.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

}
