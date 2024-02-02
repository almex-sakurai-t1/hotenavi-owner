package jp.happyhotel.data;

import java.io.Serializable;

public class DataChainResult_M2 implements Serializable
{
    private static final long    serialVersionUID = -7608876492189543327L;

    private int                  pageRecords;
    private DataMasterChain_M2[] chainData;
    private String               errorMessage;

    public DataChainResult_M2()
    {
        pageRecords = 0;
        chainData = null;
        errorMessage = "";
    }

    public DataMasterChain_M2[] getChainData()
    {
        return chainData;
    }

    public void setChainData(DataMasterChain_M2[] chainData)
    {
        this.chainData = chainData;
    }

    public int getPageRecords()
    {
        return pageRecords;
    }

    public void setPageRecords(int pageRecords)
    {
        this.pageRecords = pageRecords;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

}
