package jp.happyhotel.dto;

public class DtoRsvList
{
    private int                sendCount;
    private int                identifyNo;

    protected DtoRsvListData[] dtoRsvListData;

    public DtoRsvList()
    {
        this.dtoRsvListData = null;
        this.sendCount = 0;
        this.identifyNo = 0;
    }

    public DtoRsvListData[] getRsvListData()
    {
        return dtoRsvListData;
    }

    public void setRsvListData(DtoRsvListData[] rsvList)
    {
        this.dtoRsvListData = rsvList;
    }

    public int getSendCount()
    {
        return sendCount;
    }

    public void setSendCount(int sendCount)
    {
        this.sendCount = sendCount;
    }

    public int getIdentifyNo()
    {
        return identifyNo;
    }

    public void setIdentifyNo(int identifyNo)
    {
        this.identifyNo = identifyNo;
    }

}
