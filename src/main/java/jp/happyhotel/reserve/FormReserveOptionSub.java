package jp.happyhotel.reserve;

import java.util.ArrayList;

/**
 * �I�v�V�����p�t�H�[���N���X
 */
public class FormReserveOptionSub
{
    //
    private ArrayList<Integer> optIdList          = new ArrayList<Integer>(); // �I�v�V�����̃��X�g
    private ArrayList<String>  optNmList          = new ArrayList<String>();
    private ArrayList<Integer> optStatusList      = new ArrayList<Integer>();
    private ArrayList<Integer> maxQuantityList    = new ArrayList<Integer>(); // 1���̏����
    private ArrayList<Integer> inpMaxQuantityList = new ArrayList<Integer>(); // 1��̏����
    private ArrayList<String>  optRemarksList     = new ArrayList<String>();
    private ArrayList<Integer> unitPriceList      = new ArrayList<Integer>();

    public ArrayList<Integer> getOptIdList()
    {
        return optIdList;
    }

    public void setOptIdList(ArrayList<Integer> optIdList)
    {
        this.optIdList = optIdList;
    }

    public ArrayList<String> getOptNmList()
    {
        return optNmList;
    }

    public void setOptNmList(ArrayList<String> optNmList)
    {
        this.optNmList = optNmList;
    }

    public ArrayList<Integer> getMaxQuantityList()
    {
        return maxQuantityList;
    }

    public void setMaxQuantityList(ArrayList<Integer> maxQuantityList)
    {
        this.maxQuantityList = maxQuantityList;
    }

    public ArrayList<String> getOptRemarksList()
    {
        return optRemarksList;
    }

    public void setOptRemarksList(ArrayList<String> optRemarksList)
    {
        this.optRemarksList = optRemarksList;
    }

    public ArrayList<Integer> getUnitPriceList()
    {
        return unitPriceList;
    }

    public void setUnitPriceList(ArrayList<Integer> unitPriceList)
    {
        this.unitPriceList = unitPriceList;
    }

    public ArrayList<Integer> getInpMaxQuantityList()
    {
        return inpMaxQuantityList;
    }

    public void setInpMaxQuantityList(ArrayList<Integer> inpMaxQuantityList)
    {
        this.inpMaxQuantityList = inpMaxQuantityList;
    }

    public void setOptStatusList(ArrayList<Integer> optStatusList)
    {
        this.optStatusList = optStatusList;
    }

    public ArrayList<Integer> getOptStatusList()
    {
        return optStatusList;
    }

}
