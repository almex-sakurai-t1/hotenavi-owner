package jp.happyhotel.data;

public class DataMasterPref_M2
{

    /**
     * @param args
     */
    /** 地方ID **/
    private int    prefId;
    /** 地方名称 **/
    private String name;

    private int    hotelCount;

    public DataMasterPref_M2()
    {
        prefId = 0;
        name = "";
        hotelCount = 0;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getHotelCount()
    {
        return hotelCount;
    }

    public void setHotelCount(int hotelCount)
    {
        this.hotelCount = hotelCount;
    }

    public int getPrefId()
    {
        return prefId;
    }

    public void setPrefId(int prefId)
    {
        this.prefId = prefId;
    }

}
