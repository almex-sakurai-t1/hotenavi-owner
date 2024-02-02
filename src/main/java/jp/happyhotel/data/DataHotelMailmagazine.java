package jp.happyhotel.data;

import java.sql.ResultSet;

import jp.happyhotel.common.Logging;

/**
 * @author Sakurai
 */
public class DataHotelMailmagazine
{
    private String hotel_id;
    private String name;

    /**
     * データを初期化します。
     */
    public DataHotelMailmagazine()
    {
        hotel_id = "";
        name = "";
    }

    public String getHotelId()
    {
        return hotel_id;
    }

    public String getName()
    {
        return name;
    }

    public void setHotelId(String hotel_id)
    {
        this.hotel_id = hotel_id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * ホテルギャラリー設定
     * 
     * @param result 地方データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.hotel_id = result.getString( "hotel_id" );
                this.name = result.getString( "name" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMailmagazine.setData] Exception=" + e.toString() );
        }

        return(true);
    }

}
