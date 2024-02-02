package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * オプション管理画面Formクラス
 */
public class FormOwnerRsvOptionManage
{
    private int                selHotelId          = 0;
    private int                userId              = 0;
    private String             mustErrMsg          = "";
    private String             commErrMsg          = "";
    private String             errMsg              = "";

    // 必須オプション
    private ArrayList<Integer> optionFlagList      = new ArrayList<Integer>();
    private ArrayList<Integer> optionIdList        = new ArrayList<Integer>();
    private ArrayList<Integer> optionSubIdList     = new ArrayList<Integer>();
    private ArrayList<String>  optionNmList        = new ArrayList<String>();
    private ArrayList<String>  optionSubNmList     = new ArrayList<String>();
    private ArrayList<Integer> optionChargeList    = new ArrayList<Integer>();
    private ArrayList<String>  dispIndexList       = new ArrayList<String>();

    // 通常オプション
    private ArrayList<Integer> comOptionFlagList   = new ArrayList<Integer>();
    private ArrayList<Integer> comOptionIdList     = new ArrayList<Integer>();
    private ArrayList<Integer> comOptionSubIdList  = new ArrayList<Integer>();
    private ArrayList<String>  comOptionNmList     = new ArrayList<String>();
    private ArrayList<String>  comOptionSubNmList  = new ArrayList<String>();
    private ArrayList<String>  comOptionChargeList = new ArrayList<String>();
    private ArrayList<String>  comDispIndexList    = new ArrayList<String>();

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

    public ArrayList<Integer> getOptionIdList()
    {
        return optionIdList;
    }

    public void setOptionIdList(ArrayList<Integer> optionIdList)
    {
        this.optionIdList = optionIdList;
    }

    public ArrayList<Integer> getOptionSubIdList()
    {
        return optionSubIdList;
    }

    public void setOptionSubIdList(ArrayList<Integer> optionSubIdList)
    {
        this.optionSubIdList = optionSubIdList;
    }

    public ArrayList<String> getOptionNmList()
    {
        return optionNmList;
    }

    public void setOptionNmList(ArrayList<String> optionNmList)
    {
        this.optionNmList = optionNmList;
    }

    public ArrayList<Integer> getOptionChargeList()
    {
        return optionChargeList;
    }

    public void setOptionChargeList(ArrayList<Integer> optionChargeList)
    {
        this.optionChargeList = optionChargeList;
    }

    public ArrayList<String> getDispIndexList()
    {
        return dispIndexList;
    }

    public void setDispIndexList(ArrayList<String> dispIndexList)
    {
        this.dispIndexList = dispIndexList;
    }

    public ArrayList<String> getOptionSubNmList()
    {
        return optionSubNmList;
    }

    public void setOptionSubNmList(ArrayList<String> optionSubNmList)
    {
        this.optionSubNmList = optionSubNmList;
    }

    public ArrayList<Integer> getOptionFlagList()
    {
        return optionFlagList;
    }

    public void setOptionFlagList(ArrayList<Integer> optionFlagList)
    {
        this.optionFlagList = optionFlagList;
    }

    public String getMustErrMsg()
    {
        return mustErrMsg;
    }

    public void setMustErrMsg(String mustErrMsg)
    {
        this.mustErrMsg = mustErrMsg;
    }

    public String getCommErrMsg()
    {
        return commErrMsg;
    }

    public void setCommErrMsg(String commErrMsg)
    {
        this.commErrMsg = commErrMsg;
    }

    public ArrayList<Integer> getComOptionFlagList()
    {
        return comOptionFlagList;
    }

    public void setComOptionFlagList(ArrayList<Integer> comOptionFlagList)
    {
        this.comOptionFlagList = comOptionFlagList;
    }

    public ArrayList<Integer> getComOptionIdList()
    {
        return comOptionIdList;
    }

    public void setComOptionIdList(ArrayList<Integer> comOptionIdList)
    {
        this.comOptionIdList = comOptionIdList;
    }

    public ArrayList<Integer> getComOptionSubIdList()
    {
        return comOptionSubIdList;
    }

    public void setComOptionSubIdList(ArrayList<Integer> comOptionSubIdList)
    {
        this.comOptionSubIdList = comOptionSubIdList;
    }

    public ArrayList<String> getComOptionNmList()
    {
        return comOptionNmList;
    }

    public void setComOptionNmList(ArrayList<String> comOptionNmList)
    {
        this.comOptionNmList = comOptionNmList;
    }

    public ArrayList<String> getComOptionSubNmList()
    {
        return comOptionSubNmList;
    }

    public void setComOptionSubNmList(ArrayList<String> comOptionSubNmList)
    {
        this.comOptionSubNmList = comOptionSubNmList;
    }

    public ArrayList<String> getComOptionChargeList()
    {
        return comOptionChargeList;
    }

    public void setComOptionChargeList(ArrayList<String> comOptionChargeList)
    {
        this.comOptionChargeList = comOptionChargeList;
    }

    public ArrayList<String> getComDispIndexList()
    {
        return comDispIndexList;
    }

    public void setComDispIndexList(ArrayList<String> comDispIndexList)
    {
        this.comDispIndexList = comDispIndexList;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }
}
