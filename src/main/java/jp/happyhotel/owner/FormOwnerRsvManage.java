package jp.happyhotel.owner;

import java.util.ArrayList;

/**
 *
 * 予約管理画面 Formクラス
 */
public class FormOwnerRsvManage
{
    private int                                              selHotelID     = 0;
    private int                                              userId         = 0;
    private String                                           selHotelErrMsg = "";
    private int                                              hotelSalesFlg  = 0;
    private ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList    = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>(); // 1つ目のカレンダー
    private ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList2   = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>(); // 2つ目のカレンダー
    private FormOwnerRsvRoomManage                           frmRoom        = new FormOwnerRsvRoomManage();
    private FormOwnerRsvPlanManage                           frmPlan        = new FormOwnerRsvPlanManage();
    private int                                              nowTopMonth    = 0;

    public int getSelHotelID()
    {
        return selHotelID;
    }

    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public FormOwnerRsvRoomManage getFrmRoom()
    {
        return frmRoom;
    }

    public void setFrmRoom(FormOwnerRsvRoomManage frmRoom)
    {
        this.frmRoom = frmRoom;
    }

    public String getSelHotelErrMsg()
    {
        return selHotelErrMsg;
    }

    public void setSelHotelErrMsg(String selHotelErrMsg)
    {
        this.selHotelErrMsg = selHotelErrMsg;
    }

    public FormOwnerRsvPlanManage getFrmPlan()
    {
        return frmPlan;
    }

    public void setFrmPlan(FormOwnerRsvPlanManage frmPlan)
    {
        this.frmPlan = frmPlan;
    }

    public int getHotelSalesFlg()
    {
        return hotelSalesFlg;
    }

    public void setHotelSalesFlg(int hotelSalesFlg)
    {
        this.hotelSalesFlg = hotelSalesFlg;
    }

    public ArrayList<ArrayList<FormOwnerRsvManageCalendar>> getMonthlyList()
    {
        return monthlyList;
    }

    public void setMonthlyList(ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList)
    {
        this.monthlyList = monthlyList;
    }

    public ArrayList<ArrayList<FormOwnerRsvManageCalendar>> getMonthlyList2()
    {
        return monthlyList2;
    }

    public void setMonthlyList2(ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList2)
    {
        this.monthlyList2 = monthlyList2;
    }

    public int getNowTopMonth()
    {
        return nowTopMonth;
    }

    public void setNowTopMonth(int nowTopMonth)
    {
        this.nowTopMonth = nowTopMonth;
    }

}
