package jp.happyhotel.data;

public class DataHotelArea_M2
{

    /**
     * @param args
     */
    /** 地方ID **/
    private int    areaId;
    /** 地方名称 **/
    private String areaName;

    private int    hotelCount;

    public DataHotelArea_M2()
    {
        areaId = 0;
        areaName = "";
        hotelCount = 0;
    }

    public int getAreaId()
    {
        return areaId;
    }

    public void setAreaId(int areaId)
    {
        this.areaId = areaId;
    }

    public String getAreaName()
    {
        return areaName;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    public int getHotelCount()
    {
        return hotelCount;
    }

    public void setHotelCount(int hotelCount)
    {
        this.hotelCount = hotelCount;
    }

}
