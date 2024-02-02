package jp.happyhotel.data;

import jp.happyhotel.search.SearchHotelStation_M2;
import jp.happyhotel.sponsor.SponsorData_M2;

public class DataSearchStation_M2
{

    /** 都道府県名称 * */
    private int                   paramPrefId;

    private int                   paramLocalID;

    private int                   mapRouteCount;

    private int                   mapPointCount;

    private String                name;

    private String                paramStationName;

    private DataMapRoute_M2[]     mapRoute;

    private int[]                 mapPointHotelCount;

    private DataMapPoint_M2[]     mapPoint;

    private boolean               isReturn;

    private boolean               sponserDisplayResult;
    private boolean               randomSponserDisplayResult;

    private SponsorData_M2        sponsorData;
    private SponsorData_M2        randomSponsorData;

    private SearchHotelStation_M2 searchHotelStation;

    private String                paramName;

    public DataSearchStation_M2()
    {
        paramLocalID = 0;
        paramPrefId = 0;
        mapRouteCount = 0;
        mapPointCount = 0;
        name = "";
        paramStationName = "";
        mapRoute = null;
        mapPointHotelCount = null;
        mapPoint = null;
        isReturn = false;
        sponserDisplayResult = false;
        randomSponserDisplayResult = false;
        sponsorData = null;
        randomSponsorData = null;
        searchHotelStation = null;
    }

    /**
     * @return スポンサー表示結果
     */
    public boolean isSponserDisplayResult()
    {
        return sponserDisplayResult;
    }

    /**
     * @return ランダムスポンサー表示結果
     */
    public boolean isRandomSponserDisplayResult()
    {
        return randomSponserDisplayResult;
    }

    /**
     * @param スポンサー表示結果 the sponserDisplayResult to set
     */
    public void setSponserDisplayResult(boolean sponserDisplayResult)
    {
        this.sponserDisplayResult = sponserDisplayResult;
    }

    /**
     * @param ランダムスポンサー表示結果 the sponserDisplayResult to set
     */
    public void setRandomSponserDisplayResult(boolean randomSponserDisplayResult)
    {
        this.randomSponserDisplayResult = randomSponserDisplayResult;
    }

    /**
     * @return 駅名
     */
    public String getParamStationName()
    {
        return paramStationName;
    }

    /**
     * @param 駅名のセット the paramStationName to set
     */
    public void setParamStationName(String paramStationName)
    {
        this.paramStationName = paramStationName;
    }

    /**
     * @return スポンサー広告
     */
    public SponsorData_M2 getSponsorData()
    {
        return sponsorData;
    }

    /**
     * @return ランダムスポンサー広告
     */
    public SponsorData_M2 getRandomSponsorData()
    {
        return randomSponsorData;
    }

    /**
     * @param スポンサー広告 the sponsorData to set
     */
    public void setSponsorData(SponsorData_M2 sponsorData)
    {
        this.sponsorData = sponsorData;
    }

    /**
     * @param ランダムスポンサー広告 the sponsorData to set
     */
    public void setRandomSponsorData(SponsorData_M2 randomSponsorData)
    {
        this.randomSponsorData = randomSponsorData;
    }

    /**
     * @return the isReturn
     */
    public boolean isReturn()
    {
        return isReturn;
    }

    /**
     * @param isReturn the isReturn to set
     */
    public void setReturn(boolean isReturn)
    {
        this.isReturn = isReturn;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the mapRoute
     */
    public DataMapRoute_M2[] getMapRoute()
    {
        return mapRoute;
    }

    /**
     * @param mapRoute the mapRoute to set
     */
    public void setMapRoute(DataMapRoute_M2[] mapRoute)
    {
        this.mapRoute = mapRoute;
    }

    /**
     * @return the mapRouteCount
     */
    public int getMapRouteCount()
    {
        return mapRouteCount;
    }

    /**
     * @param mapRouteCount the mapRouteCount to set
     */
    public void setMapRouteCount(int mapRouteCount)
    {
        this.mapRouteCount = mapRouteCount;
    }

    /**
     * @return the paramPrefId
     */
    public int getParamPrefId()
    {
        return paramPrefId;
    }

    /**
     * @param paramPrefId the paramPrefId to set
     */
    public void setParamPrefId(int paramPrefId)
    {
        this.paramPrefId = paramPrefId;
    }

    /**
     * @return the mapPoint
     */
    public DataMapPoint_M2[] getMapPoint()
    {
        return mapPoint;
    }

    /**
     * @param mapPoint the mapPoint to set
     */
    public void setMapPoint(DataMapPoint_M2[] mapPoint)
    {
        this.mapPoint = mapPoint;
    }

    /**
     * @return the mapPointCount
     */
    public int getMapPointCount()
    {
        return mapPointCount;
    }

    /**
     * @param mapPointCount the mapPointCount to set
     */
    public void setMapPointCount(int mapPointCount)
    {
        this.mapPointCount = mapPointCount;
    }

    /**
     * @return the mapPointHotelCount
     */
    public int[] getMapPointHotelCount()
    {
        return mapPointHotelCount;
    }

    /**
     * @param mapPointHotelCount the mapPointHotelCount to set
     */
    public void setMapPointHotelCount(int[] mapPointHotelCount)
    {
        this.mapPointHotelCount = mapPointHotelCount;
    }

    /**
     * @return the searchHotelStation
     */
    public SearchHotelStation_M2 getSearchHotelStation()
    {
        return searchHotelStation;
    }

    /**
     * @param searchHotelStation the searchHotelStation to set
     */
    public void setSearchHotelStation(SearchHotelStation_M2 searchHotelStation)
    {
        this.searchHotelStation = searchHotelStation;
    }

    /**
     * @return the paramName
     */
    public String getParamName()
    {
        return paramName;
    }

    /**
     * @param paramName the paramName to set
     */
    public void setParamName(String paramName)
    {
        this.paramName = paramName;
    }

    /**
     * @return the paramLocalID
     */
    public int getParamLocalID()
    {
        return paramLocalID;
    }

    /**
     * @param paramLocalID the paramLocalID to set
     */
    public void setParamLocalID(int paramLocalID)
    {
        this.paramLocalID = paramLocalID;
    }

}
