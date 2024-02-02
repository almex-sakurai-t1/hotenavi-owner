package jp.happyhotel.data;

public class DataHogeHotelMap
{
    private int             id;
    private String           mapId;

    public DataHogeHotelMap()
    {
        id      = 0;
        mapId   ="";
    }
    public int getId()
    {
        return id;
    }

    public String getMapId()
    {
        return mapId;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setMapId( String mapId )
    {
        this.mapId = mapId;
    }

}
