package jp.happyhotel.owner;

import java.util.ArrayList;

public class FormOwnerRsvOption
{
    private int                selHotelId              = 0;
    private int                selOptId                = 0;
    private int                userId                  = 0;
    private String             ownerHotelID            = "";
    private String             errMsg                  = "";

    // 必須オプション
    private String             optionNm_Must           = "";
    private String             optionNmMustView        = "";
    private String             optionNm_MustSearch     = "";
    private String             optionNm_MustSearchView = "";
    private String             dispIdx                 = "";
    private ArrayList<Integer> delOptSubIdList         = new ArrayList<Integer>();
    private ArrayList<Integer> optSubIdMustList        = new ArrayList<Integer>();
    private ArrayList<String>  optSubNmMustList        = new ArrayList<String>();
    private int                maxRow                  = 0;
    private int                maxSubOptId             = 0;

    // 通常オプション
    private String             optionNm_Comm           = "";
    private String             optionNmCommView        = "";
    private String             optionNm_CommSearch     = "";
    private String             optionNm_CommSearchView = "";
    private String             optCharge               = "";
    private String             optChargeView           = "";
    private String             maxQuantity             = "";
    private String             maxQuantityView         = "";
    private String             inpMaxQuantity          = "";
    private String             inpMaxQuantityView      = "";
    private String             cancelLimitDate         = "";
    private String             cancelLimitDateView     = "";
    private String             cancelLimitTimeHH       = "";
    private String             cancelLimitTimeHHView   = "";
    private String             cancelLimitTimeMM       = "";
    private String             cancelLimitTimeMMView   = "";
    private String             dispIndexComm           = "";

    public int getSelHotelId()
    {
        return selHotelId;
    }

    public void setSelHotelId(int selHotelId)
    {
        this.selHotelId = selHotelId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public String getOptionNm_Must()
    {
        return optionNm_Must;
    }

    public void setOptionNm_Must(String optionNm_Must)
    {
        this.optionNm_Must = optionNm_Must;
    }

    public int getSelOptId()
    {
        return selOptId;
    }

    public void setSelOptId(int selOptId)
    {
        this.selOptId = selOptId;
    }

    public ArrayList<Integer> getOptSubIdMustList()
    {
        return optSubIdMustList;
    }

    public void setOptSubIdMustList(ArrayList<Integer> optSubIdMustList)
    {
        this.optSubIdMustList = optSubIdMustList;
    }

    public ArrayList<String> getOptSubNmMustList()
    {
        return optSubNmMustList;
    }

    public void setOptSubNmMustList(ArrayList<String> optSubNmMustList)
    {
        this.optSubNmMustList = optSubNmMustList;
    }

    public int getMaxRow()
    {
        return maxRow;
    }

    public void setMaxRow(int maxRow)
    {
        this.maxRow = maxRow;
    }

    public int getMaxSubOptId()
    {
        return maxSubOptId;
    }

    public void setMaxSubOptId(int maxSubOptId)
    {
        this.maxSubOptId = maxSubOptId;
    }

    public ArrayList<Integer> getDelOptSubIdList()
    {
        return delOptSubIdList;
    }

    public void setDelOptSubIdList(ArrayList<Integer> delOptSubIdList)
    {
        this.delOptSubIdList = delOptSubIdList;
    }

    public String getDispIdx()
    {
        return dispIdx;
    }

    public void setDispIdx(String dispIdx)
    {
        this.dispIdx = dispIdx;
    }

    public String getOptionNmMustView()
    {
        return optionNmMustView;
    }

    public void setOptionNmMustView(String optionNmMustView)
    {
        this.optionNmMustView = optionNmMustView;
    }

    public String getOptionNm_Comm()
    {
        return optionNm_Comm;
    }

    public void setOptionNm_Comm(String optionNm_Comm)
    {
        this.optionNm_Comm = optionNm_Comm;
    }

    public String getOptionNmCommView()
    {
        return optionNmCommView;
    }

    public void setOptionNmCommView(String optionNmCommView)
    {
        this.optionNmCommView = optionNmCommView;
    }

    public String getOptCharge()
    {
        return optCharge;
    }

    public void setOptCharge(String optCharge)
    {
        this.optCharge = optCharge;
    }

    public String getOptChargeView()
    {
        return optChargeView;
    }

    public void setOptChargeView(String optChargeView)
    {
        this.optChargeView = optChargeView;
    }

    public String getMaxQuantity()
    {
        return maxQuantity;
    }

    public void setMaxQuantity(String maxQuantity)
    {
        this.maxQuantity = maxQuantity;
    }

    public String getMaxQuantityView()
    {
        return maxQuantityView;
    }

    public void setMaxQuantityView(String maxQuantityView)
    {
        this.maxQuantityView = maxQuantityView;
    }

    public String getInpMaxQuantity()
    {
        return inpMaxQuantity;
    }

    public void setInpMaxQuantity(String inpMaxQuantity)
    {
        this.inpMaxQuantity = inpMaxQuantity;
    }

    public String getInpMaxQuantityView()
    {
        return inpMaxQuantityView;
    }

    public void setInpMaxQuantityView(String inpMaxQuantityView)
    {
        this.inpMaxQuantityView = inpMaxQuantityView;
    }

    public String getCancelLimitDate()
    {
        return cancelLimitDate;
    }

    public void setCancelLimitDate(String cancelLimitDate)
    {
        this.cancelLimitDate = cancelLimitDate;
    }

    public String getCancelLimitDateView()
    {
        return cancelLimitDateView;
    }

    public void setCancelLimitDateView(String cancelLimitDateView)
    {
        this.cancelLimitDateView = cancelLimitDateView;
    }

    public String getCancelLimitTimeHH()
    {
        return cancelLimitTimeHH;
    }

    public void setCancelLimitTimeHH(String cancelLimitTimeHH)
    {
        this.cancelLimitTimeHH = cancelLimitTimeHH;
    }

    public String getCancelLimitTimeHHView()
    {
        return cancelLimitTimeHHView;
    }

    public void setCancelLimitTimeHHView(String cancelLimitTimeHHView)
    {
        this.cancelLimitTimeHHView = cancelLimitTimeHHView;
    }

    public String getCancelLimitTimeMM()
    {
        return cancelLimitTimeMM;
    }

    public void setCancelLimitTimeMM(String cancelLimitTimeMM)
    {
        this.cancelLimitTimeMM = cancelLimitTimeMM;
    }

    public String getCancelLimitTimeMMView()
    {
        return cancelLimitTimeMMView;
    }

    public void setCancelLimitTimeMMView(String cancelLimitTimeMMView)
    {
        this.cancelLimitTimeMMView = cancelLimitTimeMMView;
    }

    public String getDispIndexComm()
    {
        return dispIndexComm;
    }

    public void setDispIndexComm(String dispIndexComm)
    {
        this.dispIndexComm = dispIndexComm;
    }

    public String getOwnerHotelID()
    {
        return ownerHotelID;
    }

    public void setOwnerHotelID(String ownerHotelID)
    {
        this.ownerHotelID = ownerHotelID;
    }

    public void setOptionNm_MustSearch(String optionNm_MustSearch)
    {
        this.optionNm_MustSearch = optionNm_MustSearch;
    }

    public String getOptionNm_MustSearch()
    {
        return optionNm_MustSearch;
    }

    public void setOptionNm_MustSearchView(String optionNm_MustSearchView)
    {
        this.optionNm_MustSearchView = optionNm_MustSearchView;
    }

    public String getOptionNm_MustSearchView()
    {
        return optionNm_MustSearchView;
    }

    public void setOptionNm_CommSearch(String optionNm_CommSearch)
    {
        this.optionNm_CommSearch = optionNm_CommSearch;
    }

    public String getOptionNm_CommSearch()
    {
        return optionNm_CommSearch;
    }

    public void setOptionNm_CommSearchView(String optionNm_CommSearchView)
    {
        this.optionNm_CommSearchView = optionNm_CommSearchView;
    }

    public String getOptionNm_CommSearchView()
    {
        return optionNm_CommSearchView;
    }
}
