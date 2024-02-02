package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * ê›îıê›íËFormÉNÉâÉX
 */
public class FormOwnerRsvEquipManage
{
    private int                selHotelID     = 0;
    private ArrayList<Integer> eqIdList       = new ArrayList<Integer>();
    private ArrayList<String>  eqNmList       = new ArrayList<String>();
    private ArrayList<Integer> inpFlg6List    = new ArrayList<Integer>();
    private ArrayList<Integer> inpFlg2List    = new ArrayList<Integer>();
    private ArrayList<Integer> dspList        = new ArrayList<Integer>();
    private ArrayList<String>  seqList        = new ArrayList<String>();
    private ArrayList<Integer> eqTypeList     = new ArrayList<Integer>();
    private ArrayList<String>  eqTypeNmList   = new ArrayList<String>();
    private ArrayList<Integer> eqTypeIdList   = new ArrayList<Integer>();
    private ArrayList<String>  eqBranchNmList = new ArrayList<String>();
    private String             errMsg         = "";
    private int                userId         = 0;

    public int getSelHotelID()
    {
        return selHotelID;
    }

    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public ArrayList<Integer> getEqIdList()
    {
        return eqIdList;
    }

    public void setEqIdList(ArrayList<Integer> eqIdList)
    {
        this.eqIdList = eqIdList;
    }

    public ArrayList<String> getEqNmList()
    {
        return eqNmList;
    }

    public void setEqNmList(ArrayList<String> eqNmList)
    {
        this.eqNmList = eqNmList;
    }

    public ArrayList<Integer> getInpFlg6List()
    {
        return inpFlg6List;
    }

    public void setInpFlg6List(ArrayList<Integer> inpFlg6List)
    {
        this.inpFlg6List = inpFlg6List;
    }

    public ArrayList<Integer> getDspList()
    {
        return dspList;
    }

    public void setDspList(ArrayList<Integer> dspList)
    {
        this.dspList = dspList;
    }

    public ArrayList<String> getSeqList()
    {
        return seqList;
    }

    public void setSeqList(ArrayList<String> seqList)
    {
        this.seqList = seqList;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public ArrayList<Integer> getEqTypeList()
    {
        return eqTypeList;
    }

    public void setEqTypeList(ArrayList<Integer> eqTypeList)
    {
        this.eqTypeList = eqTypeList;
    }

    public ArrayList<String> getEqTypeNmList()
    {
        return eqTypeNmList;
    }

    public void setEqTypeNmList(ArrayList<String> eqTypeNmList)
    {
        this.eqTypeNmList = eqTypeNmList;
    }

    public void setEqBranchNmList(ArrayList<String> eqBranchNmList)
    {
        this.eqBranchNmList = eqBranchNmList;
    }

    public ArrayList<String> getEqBranchNmList()
    {
        return eqBranchNmList;
    }

    public void setInpFlg2List(ArrayList<Integer> inpFlg2List)
    {
        this.inpFlg2List = inpFlg2List;
    }

    public ArrayList<Integer> getInpFlg2List()
    {
        return inpFlg2List;
    }

    public void setEqTypeIdList(ArrayList<Integer> eqTypeIdList)
    {
        this.eqTypeIdList = eqTypeIdList;
    }

    public ArrayList<Integer> getEqTypeIdList()
    {
        return eqTypeIdList;
    }

}
