package jp.happyhotel.data;

import jp.happyhotel.sponsor.SponsorData_M2;

public class DataSearchArea_M2
{

    private DataHotelCity_M2[] dataHotelCity;
    private DataHotelArea_M2[] dataHotelArea;
    private int                cityCount;
    private int                hotelAreaCount;
    // スポンサー広告
    private SponsorData_M2     sponsorData;
    // ローテーションバナー用
    private SponsorData_M2     randomSponsorData;
    private boolean            sponserDisplayResult;
    private boolean            randomSponserDisplayResult;
    private String             prefName;
    private String             parameter1;

    public String getParameter1()
    {
        return parameter1;
    }

    public void setParameter1(String parameter1)
    {
        this.parameter1 = parameter1;
    }

    /**
     * @return スポンサー広告を表示するかどうかを返す
     */
    public boolean isSponserDisplayResult()
    {
        return sponserDisplayResult;
    }

    /**
     * @param スポンサー広告のデータのありなしを判断する
     */
    public void setSponserDisplayResult(boolean sponserDisplayResult)
    {
        this.sponserDisplayResult = sponserDisplayResult;
    }

    /**
     * @return スポンサー広告データの取得
     */
    public SponsorData_M2 getSponsorData()
    {
        return sponsorData;
    }

    /**
     * @param スポンサー広告データのセット
     */
    public void setSponsorData(SponsorData_M2 sponsorData)
    {
        this.sponsorData = sponsorData;
    }

    /**
     * @return ローテーションバナーを表示するかどうかを返す
     */
    public boolean isRandomSponserDisplayResult()
    {
        return randomSponserDisplayResult;
    }

    /**
     * @param ローテーションバナーのデータのありなしを判断する
     */
    public void setRandomSponsorDisplayResult(boolean randomSponsorDisplayResult)
    {
        this.randomSponserDisplayResult = randomSponsorDisplayResult;
    }

    /**
     * @return ローテーションバナーデータの取得
     */
    public SponsorData_M2 getRandomSponsorData()
    {
        return randomSponsorData;
    }

    /**
     * @param ローテーションバナーデータのセット
     */
    public void setRandomSponsorData(SponsorData_M2 randomSponsorData)
    {
        this.randomSponsorData = randomSponsorData;
    }

    public DataHotelArea_M2[] getDataHotelArea()
    {
        return dataHotelArea;
    }

    public void setDataHotelArea(DataHotelArea_M2[] dataHotelArea)
    {
        this.dataHotelArea = dataHotelArea;
    }

    public DataHotelCity_M2[] getDataHotelCity()
    {
        return dataHotelCity;
    }

    public void setDataHotelCity(DataHotelCity_M2[] dataHotelCity)
    {
        this.dataHotelCity = dataHotelCity;
    }

    public int getCityCount()
    {
        return cityCount;
    }

    public void setCityCount(int cityCount)
    {
        this.cityCount = cityCount;
    }

    public int getHotelAreaCount()
    {
        return hotelAreaCount;
    }

    public void setHotelAreaCount(int hotelAreaCount)
    {
        this.hotelAreaCount = hotelAreaCount;
    }

    public String getPrefName()
    {
        return prefName;
    }

    public void setPrefName(String prefName)
    {
        this.prefName = prefName;
    }

}
