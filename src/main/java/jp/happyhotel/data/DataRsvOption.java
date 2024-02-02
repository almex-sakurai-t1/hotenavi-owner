/*
 * オプションクラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvOption implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 303403244578377398L;

    private int               iD;
    private int               optionId;
    private int               optionSubId;
    private String            optionName;
    private String            searchOptionName;
    private String            optionSubName;
    private int               optionCharge;
    private int               maxQuantity;
    private int               inputMaxQuantity;
    private int               cancelLimitDate;
    private int               cancelLimitTime;
    private int               optionFlag;
    private String            imagePc;
    private String            imageGif;
    private String            imagePng;
    private int               dispIndex;
    private String            hotelId;
    private int               userId;
    private int               lastUpdate;
    private int               lastUptime;

    /**
     * データの初期化
     */
    public DataRsvOption()
    {
        iD = 0;
        optionId = 0;
        optionSubId = 0;
        optionName = "";
        searchOptionName = "";
        optionSubName = "";
        optionCharge = 0;
        maxQuantity = 0;
        inputMaxQuantity = 0;
        cancelLimitDate = 0;
        cancelLimitTime = 0;
        optionFlag = 0;
        imagePc = "";
        imageGif = "";
        imagePng = "";
        dispIndex = 0;
        hotelId = "";
        userId = 0;
        lastUpdate = 0;
        lastUptime = 0;
    }

    // getter
    public int getID()
    {
        return this.iD;
    }

    public int getOptionId()
    {
        return this.optionId;
    }

    public int getOptionSubId()
    {
        return this.optionSubId;
    }

    public String getOptionName()
    {
        return this.optionName;
    }

    public String getSearchOptionName()
    {
        return searchOptionName;
    }

    public String getOptionSubName()
    {
        return this.optionSubName;
    }

    public int optionCharge()
    {
        return this.optionCharge;
    }

    public int getMaxQuantity()
    {
        return this.maxQuantity;
    }

    public int getInputMaxQuantity()
    {
        return this.inputMaxQuantity;
    }

    public int getCancelLimitDate()
    {
        return this.cancelLimitDate;
    }

    public int getCancelLimitTime()
    {
        return this.cancelLimitTime;
    }

    public int getOptionFlag()
    {
        return this.optionFlag;
    }

    public String getImagePc()
    {
        return this.imagePc;
    }

    public String getImageGif()
    {
        return this.imageGif;
    }

    public String getImagePng()
    {
        return this.imagePng;
    }

    public int getDispIndex()
    {
        return this.dispIndex;
    }

    public String getHotelId()
    {
        return this.hotelId;
    }

    public int getUserId()
    {
        return this.userId;
    }

    public int getLastUpdate()
    {
        return this.lastUpdate;
    }

    public int getLastUptime()
    {
        return this.lastUptime;
    }

    /**
     * 
     * setter
     * 
     */
    public void setId(int iD)
    {
        this.iD = iD;
    }

    public void setOptionId(int optionId)
    {
        this.optionId = optionId;
    }

    public void setOptionSubId(int optionSubId)
    {
        this.optionSubId = optionSubId;
    }

    public void setOptionName(String optionName)
    {
        this.optionName = optionName;
    }

    public void setSearchOptionName(String searchOptionName)
    {
        this.searchOptionName = searchOptionName;
    }

    public void setOptionSubName(String optionSubName)
    {
        this.optionSubName = optionSubName;
    }

    public void setOptionCharge(int optionCharge)
    {
        this.optionCharge = optionCharge;
    }

    public void setMaxQuantity(int maxQuantity)
    {
        this.maxQuantity = maxQuantity;
    }

    public void setInputMaxQuantity(int inputMaxQuantity)
    {
        this.inputMaxQuantity = inputMaxQuantity;
    }

    public void setCancelLimitDate(int cancelLimitDate)
    {
        this.cancelLimitDate = cancelLimitDate;
    }

    public void setCancelLimitTime(int cancelLimitTime)
    {
        this.cancelLimitTime = cancelLimitTime;
    }

    public void setOptionFlag(int optionFlag)
    {
        this.optionFlag = optionFlag;
    }

    public void setImagePc(String imagePc)
    {
        this.imagePc = imagePc;
    }

    public void setImageGif(String imageGif)
    {
        this.imageGif = imageGif;
    }

    public void setImagePng(String imagePng)
    {
        this.imagePng = imagePng;
    }

    public void setDispIndex(int dispIndex)
    {
        this.dispIndex = dispIndex;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    /**
     * オプション情報取得
     * 
     * @param iD ホテルID
     * @param optionId オプションID
     * @param optionSubId オプションサブID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, int optionId, int optionSubId)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, option_id, option_sub_id, option_name, search_option_name, option_sub_name, " +
                " option_charge, max_quantity, input_max_quantity, " +
                " cancel_limit_date, cancel_limit_time, option_flag, image_pc," +
                " image_gif, image_png, disp_index, hotel_id, user_id, last_update, last_uptime " +
                " FROM hh_rsv_option WHERE id = ? AND option_id = ? AND option_sub_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, optionId );
            prestate.setInt( 3, optionSubId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.optionId = result.getInt( "option_id" );
                    this.optionSubId = result.getInt( "option_sub_id" );
                    this.optionName = result.getString( "option_name" );
                    this.searchOptionName = result.getString( "search_option_name" );
                    this.optionSubName = result.getString( "option_sub_name" );
                    this.optionCharge = result.getInt( "option_charge" );
                    this.maxQuantity = result.getInt( "max_quantity" );
                    this.inputMaxQuantity = result.getInt( "input_max_quantity" );
                    this.cancelLimitDate = result.getInt( "cancel_limit_date" );
                    this.cancelLimitTime = result.getInt( "cancel_limit_time" );
                    this.optionFlag = result.getInt( "option_flag" );
                    this.imagePc = result.getString( "image_pc" );
                    this.imageGif = result.getString( "image_gif" );
                    this.imagePng = result.getString( "image_png" );
                    this.dispIndex = result.getInt( "disp_index" );
                    this.hotelId = result.getString( "hotel_id" );
                    this.userId = result.getInt( "user_id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvOption.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * オプション情報取得
     * 
     * @param iD ホテルID
     * @param optionId オプションID
     * @param optionSubId オプションサブID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, int optionId)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, option_id, option_sub_id, option_name, search_option_name, option_sub_name, " +
                " option_charge, max_quantity, input_max_quantity, " +
                " cancel_limit_date, cancel_limit_time, option_flag, image_pc," +
                " image_gif, image_png, disp_index, hotel_id, user_id, last_update, last_uptime " +
                " FROM hh_rsv_option WHERE id = ? AND option_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, optionId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                while( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.optionId = result.getInt( "option_id" );
                    this.optionSubId = result.getInt( "option_sub_id" );
                    this.optionName = result.getString( "option_name" );
                    this.searchOptionName = result.getString( "search_option_name" );

                    if ( this.optionSubName.equals( "" ) == false )
                    {
                        this.optionSubName += ",";
                    }
                    this.optionSubName += result.getString( "option_sub_name" );
                    this.optionCharge = result.getInt( "option_charge" );
                    this.maxQuantity = result.getInt( "max_quantity" );
                    this.inputMaxQuantity = result.getInt( "input_max_quantity" );
                    this.cancelLimitDate = result.getInt( "cancel_limit_date" );
                    this.cancelLimitTime = result.getInt( "cancel_limit_time" );
                    this.optionFlag = result.getInt( "option_flag" );
                    this.imagePc = result.getString( "image_pc" );
                    this.imageGif = result.getString( "image_gif" );
                    this.imagePng = result.getString( "image_png" );
                    this.dispIndex = result.getInt( "disp_index" );
                    this.hotelId = result.getString( "hotel_id" );
                    this.userId = result.getInt( "user_id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvOption.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

}
