package jp.happyhotel.dto;

public class DtoMemberList
{
    String                        header;

    private int                   memberCount;
    private int                   identifyNo;
    private String                reserve;
    private int                   result;

    private int                   errorCode;
    protected DtoMemberListData[] dtoMemberListData;

    public DtoMemberList()
    {
        this.header = "";
        this.dtoMemberListData = null;
        this.memberCount = 0;
        this.identifyNo = 0;
        this.reserve = "";
        this.errorCode = 0;
        this.result = 0;
    }

    public DtoMemberListData[] getMemberListData()
    {
        return dtoMemberListData;
    }

    public void setMemberListData(DtoMemberListData[] memberList)
    {
        this.dtoMemberListData = memberList;
    }

    public int getMemberCount()
    {
        return memberCount;
    }

    public void setMemberCount(int memberCount)
    {
        this.memberCount = memberCount;
    }

    public int getIdentifyNo()
    {
        return identifyNo;
    }

    public void setIdentifyNo(int identifyNo)
    {
        this.identifyNo = identifyNo;
    }

    public String getReserve()
    {
        return reserve;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setResult(int result)
    {
        this.result = result;
    }
    public int getResult()
    {
        return result;
    }
}
