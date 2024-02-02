package jp.happyhotel.data;

public class DataMasterLocal_M2
{

    /**
     * @param args
     */
    /** ínï˚ID **/
    private int    localId;
    /** ínï˚ñºèÃ **/
    private String name;

    private int    hotelCount;

    public DataMasterLocal_M2()
    {
        localId = 0;
        name = "";
        hotelCount = 0;
    }

    public int getLocalId()
    {
        return localId;
    }

    public void setLocalId(int localId)
    {
        this.localId = localId;
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

}
