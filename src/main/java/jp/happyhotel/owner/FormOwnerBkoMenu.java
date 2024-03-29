package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * メニューFormクラス
 */
public class FormOwnerBkoMenu
{
    private ArrayList<Integer> hotelIDList       = new ArrayList<Integer>();
    private ArrayList<String>  hotelNmList       = new ArrayList<String>();
    private int                modeFlg           = 0;
    private int                selHotelID        = 0;
    private int                userId            = 0;
    private int                userAuth          = 0;
    private String             accHotenaviID     = "";
    private ArrayList<String>  accHotenaviIDList = new ArrayList<String>();
    private int                imediaFlg         = 0;
    private int                billFlg           = 0;

    public ArrayList<Integer> getHotelIDList()
    {
        return hotelIDList;
    }

    public void setHotelIDList(ArrayList<Integer> hotelIDList)
    {
        this.hotelIDList = hotelIDList;
    }

    public ArrayList<String> getHotelNmList()
    {
        return hotelNmList;
    }

    public void setHotelNmList(ArrayList<String> hotelNmList)
    {
        this.hotelNmList = hotelNmList;
    }

    public int getModeFlg()
    {
        return modeFlg;
    }

    public void setModeFlg(int modeFlg)
    {
        this.modeFlg = modeFlg;
    }

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

    public int getUserAuth()
    {
        return userAuth;
    }

    public void setUserAuth(int userAuth)
    {
        this.userAuth = userAuth;
    }

    public String getAccHotenaviID()
    {
        return accHotenaviID;
    }

    public void setAccHotenaviID(String accHotenaviID)
    {
        this.accHotenaviID = accHotenaviID;
    }

    public ArrayList<String> getAccHotenaviIDList()
    {
        return accHotenaviIDList;
    }

    public void setAccHotenaviIDList(ArrayList<String> accHotenaviIDList)
    {
        this.accHotenaviIDList = accHotenaviIDList;
    }

    public int getImediaFlg()
    {
        return imediaFlg;
    }

    public void setImediaFlg(int imediaFlg)
    {
        this.imediaFlg = imediaFlg;
    }

    public int getBillFlg()
    {
        return billFlg;
    }

    public void setBillFlg(int billFlg)
    {
        this.billFlg = billFlg;
    }

}
