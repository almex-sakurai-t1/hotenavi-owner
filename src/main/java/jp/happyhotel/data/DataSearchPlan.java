package jp.happyhotel.data;

import java.util.ArrayList;

import jp.happyhotel.reserve.FormReservePersonalInfoPC;

/**
 * データプランクラス
 *
 * @author S.Tashiro
 * @version 1.0 2011/02/16
 */
public class DataSearchPlan
{
    private int                       id                  = 0;
    private int                       planId              = 0;
    private String                    planName            = "";
    private int                       lowestCharge        = 0;
    private int                       maxCharge           = 0;
    private int                       charge              = 0;   // 指定日の料金
    private int                       chargeWeekday       = 0;   // 指定日なし　平日料金
    private int                       chargeWeekend       = 0;   // 指定日なし　休日料金
    private int                       chargeWeekendBefore = 0;   // 指定日なし　休前日料金
    private ArrayList<String>         chargeModeNameList  = null; // 指定日なし 料金ﾓｰﾄﾞ名称リスト
    private String                    ciTime              = "";  // 指定日のチェックイン
    private String                    ciTimeTo            = "";  // 指定日のチェックインTO
    private ArrayList<String>         ciTimeList          = null; // 指定日なし ﾁｪｯｸｲﾝFROMリスト
    private ArrayList<String>         ciTimeToList        = null; // 指定日なし ﾁｪｯｸｲﾝTOリスト
    private String                    coTime              = "";  // 指定日のチェックアウト
    private ArrayList<String>         coTimeList          = null;
    private String                    option              = "";
    private String                    subOption           = "";
    private String                    planPr              = "";
    private int                       searchAdult         = 0;
    private int                       searchChild         = 0;
    private String                    remarks             = "";
    private int                       offerkind           = 0;
    private String                    planImagePc         = "";  // プラン画像PC
    private String                    planImageGif        = "";  // プラン画像GIF
    private String                    planImagePng        = "";  // プラン画像PNG
    private int                       roomAllCount        = 0;   // 全部屋数
    private int                       roomCount           = 0;   // 部屋数
    private String[]                  roomName            = null; // 部屋名
    private int[]                     roomSeq             = null; // 部屋管理番号
    private String[]                  roomImagePc         = null; // 部屋画像PC
    private String[]                  roomImageGif        = null; // 部屋画像GIF
    private String[]                  roomImagePng        = null; // 部屋画像PNG
    private String[]                  roomText            = null; // 部屋説明
    private String[]                  roomEquip           = null; // 部屋設備名称
    private String[]                  roomPr              = null; // 部屋PR
    private String[]                  roomRemarks         = null; // 部屋設備備考
    private FormReservePersonalInfoPC reserveForm         = null;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getPlanId()
    {
        return planId;
    }

    public void setPlanId(int planId)
    {
        this.planId = planId;
    }

    public String getPlanName()
    {
        return planName;
    }

    public void setPlanName(String planName)
    {
        this.planName = planName;
    }

    public int getLowestCharge()
    {
        return lowestCharge;
    }

    public void setLowestCharge(int lowestCharge)
    {
        this.lowestCharge = lowestCharge;
    }

    public int getCharge()
    {
        return charge;
    }

    public void setCharge(int charge)
    {
        this.charge = charge;
    }

    public int getChargeWeekday()
    {
        return chargeWeekday;
    }

    public void setChargeWeekday(int chargeWeekday)
    {
        this.chargeWeekday = chargeWeekday;
    }

    public int getChargeWeekend()
    {
        return chargeWeekend;
    }

    public void setChargeWeekend(int chargeWeekend)
    {
        this.chargeWeekend = chargeWeekend;
    }

    public int getChargeWeekendBefore()
    {
        return chargeWeekendBefore;
    }

    public void setChargeWeekendBefore(int chargeWeekendBefore)
    {
        this.chargeWeekendBefore = chargeWeekendBefore;
    }

    public String getCiTime()
    {
        return ciTime;
    }

    public void setCiTime(String ciTime)
    {
        this.ciTime = ciTime;
    }

    public String getCiTimeTo()
    {
        return ciTimeTo;
    }

    public void setCiTimeTo(String ciTimeTo)
    {
        this.ciTimeTo = ciTimeTo;
    }

    public String getCoTime()
    {
        return coTime;
    }

    public void setCoTime(String coTime)
    {
        this.coTime = coTime;
    }

    public String getOption()
    {
        return option;
    }

    public void setOption(String option)
    {
        this.option = option;
    }

    public String getSubOption()
    {
        return subOption;
    }

    public void setSubOption(String subOption)
    {
        this.subOption = subOption;
    }

    public String getPlanPr()
    {
        return planPr;
    }

    public void setPlanPr(String planPr)
    {
        this.planPr = planPr;
    }

    public int getSearchAdult()
    {
        return searchAdult;
    }

    public void setSearchAdult(int searchAdult)
    {
        this.searchAdult = searchAdult;
    }

    public int getSearchChild()
    {
        return searchChild;
    }

    public void setSearchChild(int searchChild)
    {
        this.searchChild = searchChild;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public int getOfferkind()
    {
        return offerkind;
    }

    public void setOfferkind(int offerkind)
    {
        this.offerkind = offerkind;
    }

    public String getPlanImagePc()
    {
        return planImagePc;
    }

    public void setPlanImagePc(String planImagePc)
    {
        this.planImagePc = planImagePc;
    }

    public String getPlanImageGif()
    {
        return planImageGif;
    }

    public void setPlanImageGif(String planImageGif)
    {
        this.planImageGif = planImageGif;
    }

    public String getPlanImagePng()
    {
        return planImagePng;
    }

    public void setPlanImagePng(String planImagePng)
    {
        this.planImagePng = planImagePng;
    }

    public int getRoomAllCount()
    {
        return roomAllCount;
    }

    public void setRoomAllCount(int roomAllCount)
    {
        this.roomAllCount = roomAllCount;
    }

    public int getRoomCount()
    {
        return roomCount;
    }

    public void setRoomCount(int roomCount)
    {
        this.roomCount = roomCount;
    }

    public String[] getRoomName()
    {
        return roomName;
    }

    public void setRoomName(String[] roomName)
    {
        this.roomName = roomName;
    }

    public int[] getRoomSeq()
    {
        return roomSeq;
    }

    public void setRoomSeq(int[] roomSeq)
    {
        this.roomSeq = roomSeq;
    }

    public String[] getRoomImagePc()
    {
        return roomImagePc;
    }

    public void setRoomImagePc(String[] roomImagePc)
    {
        this.roomImagePc = roomImagePc;
    }

    public String[] getRoomImageGif()
    {
        return roomImageGif;
    }

    public void setRoomImageGif(String[] roomImageGif)
    {
        this.roomImageGif = roomImageGif;
    }

    public String[] getRoomImagePng()
    {
        return roomImagePng;
    }

    public void setRoomImagePng(String[] roomImagePng)
    {
        this.roomImagePng = roomImagePng;
    }

    public String[] getRoomText()
    {
        return roomText;
    }

    public void setRoomText(String[] roomText)
    {
        this.roomText = roomText;
    }

    public String[] getRoomEquip()
    {
        return roomEquip;
    }

    public void setRoomEquip(String[] roomEquip)
    {
        this.roomEquip = roomEquip;
    }

    public String[] getRoomPr()
    {
        return roomPr;
    }

    public void setRoomPr(String[] roomPr)
    {
        this.roomPr = roomPr;
    }

    public String[] getRoomRemarks()
    {
        return roomRemarks;
    }

    public void setRoomRemarks(String[] roomRemarks)
    {
        this.roomRemarks = roomRemarks;
    }

    public FormReservePersonalInfoPC getReserveForm()
    {
        return reserveForm;
    }

    public void setReserveForm(FormReservePersonalInfoPC reserveForm)
    {
        this.reserveForm = reserveForm;
    }

    public void setCiTimeList(ArrayList<String> ciTimeList)
    {
        this.ciTimeList = ciTimeList;
    }

    public ArrayList<String> getCiTimeList()
    {
        return ciTimeList;
    }

    public void setCiTimeToList(ArrayList<String> ciTimeToList)
    {
        this.ciTimeToList = ciTimeToList;
    }

    public ArrayList<String> getCiTimeToList()
    {
        return ciTimeToList;
    }

    public void setCoTimeList(ArrayList<String> coTimeList)
    {
        this.coTimeList = coTimeList;
    }

    public ArrayList<String> getCoTimeList()
    {
        return coTimeList;
    }

    public void setChargeModeNameList(ArrayList<String> chargeModeNameList)
    {
        this.chargeModeNameList = chargeModeNameList;
    }

    public ArrayList<String> getChargeModeNameList()
    {
        return chargeModeNameList;
    }

    public int getMaxCharge() {
        return maxCharge;
    }

    public void setMaxCharge(int maxCharge) {
        this.maxCharge = maxCharge;
    }

}
