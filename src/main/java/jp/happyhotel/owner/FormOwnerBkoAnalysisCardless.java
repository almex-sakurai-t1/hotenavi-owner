package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * カードレスメンバー利用分析画面Formクラス
 */
public class FormOwnerBkoAnalysisCardless
{
    private int                selHotelID     = 0;
    private String             selHotelName   = "";
    private String             errMsg         = "";
    private int                selYear        = 0;
    private int                selMonth       = 0;
    private String             simeKikan      = "";
    private int                dateFrom       = 0;
    private int                dateTo         = 0;

    private ArrayList<Integer> registDateList = new ArrayList<Integer>(); // 移行日
    private ArrayList<String>  customIdList   = new ArrayList<String>(); // 移行済みカード
    private ArrayList<Integer> userSeqList    = new ArrayList<Integer>(); // ユーザーSeq

    // 合計
    private int                sumNewMember   = 0;
    private int                sumIkouMember  = 0;

    public int getSelHotelID()
    {
        return selHotelID;
    }

    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
    }

    public String getSelHotelName()
    {
        return selHotelName;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public int getSelYear()
    {
        return selYear;
    }

    public void setSelYear(int selYear)
    {
        this.selYear = selYear;
    }

    public int getSelMonth()
    {
        return selMonth;
    }

    public void setSelMonth(int selMonth)
    {
        this.selMonth = selMonth;
    }

    public String getSimeKikan()
    {
        return simeKikan;
    }

    public void setSimeKikan(String simeKikan)
    {
        this.simeKikan = simeKikan;
    }

    public int getDateFrom()
    {
        return dateFrom;
    }

    public void setDateFrom(int dateFrom)
    {
        this.dateFrom = dateFrom;
    }

    public int getDateTo()
    {
        return dateTo;
    }

    public void setDateTo(int dateTo)
    {
        this.dateTo = dateTo;
    }

    public void setRegistDateList(ArrayList<Integer> registDateList)
    {
        this.registDateList = registDateList;
    }

    public ArrayList<Integer> getRegistDateList()
    {
        return registDateList;
    }

    public ArrayList<String> getCustomIdList()
    {
        return customIdList;
    }

    public void setCustomIdList(ArrayList<String> customIdList)
    {
        this.customIdList = customIdList;
    }

    public ArrayList<Integer> getUserSeqList()
    {
        return userSeqList;
    }

    public void setUserSeqList(ArrayList<Integer> userSeqList)
    {
        this.userSeqList = userSeqList;
    }

    public int getSumNewMember()
    {
        return sumNewMember;
    }

    public void setSumNewMember(int sumNewMember)
    {
        this.sumNewMember = sumNewMember;
    }

    public int getSumIkouMember()
    {
        return sumIkouMember;
    }

    public void setSumIkouMember(int sumIkouMember)
    {
        this.sumIkouMember = sumIkouMember;
    }
}
