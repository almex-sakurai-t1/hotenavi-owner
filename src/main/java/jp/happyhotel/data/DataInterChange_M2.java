package jp.happyhotel.data;

public class DataInterChange_M2
{
    private String            icName       = "";
    private String            errorMessage = "";
    private int               m_mapPointCount;
    private int               pageRecords  = 0;
    private DataMapPoint_M2[] m_mapPoint;
    private int[]             m_mapPointHotelCount;

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public String getIcName()
    {
        return icName;
    }

    public void setIcName(String icName)
    {
        this.icName = icName;
    }

    public void setMapPointCount(int mapPointCount)
    {
        m_mapPointCount = mapPointCount;
    }

    public int getMapPointCount()
    {
        return m_mapPointCount;
    }

    public void setMapPointHotelCount(int[] mapPointHotelCount)
    {
        m_mapPointHotelCount = mapPointHotelCount;
    }

    public int[] getMapPointHotelCount()
    {
        return(m_mapPointHotelCount);
    }

    public void setDataMapPoint(DataMapPoint_M2[] mapPoint)
    {
        m_mapPoint = mapPoint;
    }

    public DataMapPoint_M2[] getMapPoint()
    {
        return(m_mapPoint);
    }

    public int getPageRecords()
    {
        return pageRecords;
    }

    public void setPageRecords(int pageRecords)
    {
        this.pageRecords = pageRecords;
    }
}
