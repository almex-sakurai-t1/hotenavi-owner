package jp.happyhotel.reserve;

import java.util.ArrayList;

public class FormReserveListMobile
{
    private String             userId          = "";                      // ƒ†[ƒUID
    private int                pageMax         = 0;                       // —\–ñŒ”
    private int                pageAct         = 0;                       //
    private int                pageSt          = 0;                       // Œ»İ•\¦—\–ñŒ”(æ“ª)
    private int                pageEd          = 0;                       // Œ»İ•\¦—\–ñŒ”(ÅŒã)
    private ArrayList<Integer> hotelIdList     = new ArrayList<Integer>();
    private ArrayList<String>  hotelNmList     = new ArrayList<String>();
    private ArrayList<String>  reserveNoList   = new ArrayList<String>();
    private ArrayList<Integer> reserveDateList = new ArrayList<Integer>();
    private ArrayList<String>  reserveDtList   = new ArrayList<String>();
    private String             pageLink        = "";

    /**
     *
     * getter
     *
     */
    public String getUserId()
    {
        return userId;
    }

    public ArrayList<Integer> getHotelIdList()
    {
        return this.hotelIdList;
    }

    public ArrayList<String> getHotelNmList()
    {
        return this.hotelNmList;
    }

    public ArrayList<String> getReserveNoList()
    {
        return this.reserveNoList;
    }

    public ArrayList<Integer> getReserveDateList()
    {
        return this.reserveDateList;
    }

    public ArrayList<String> getReserveDtList()
    {
        return this.reserveDtList;
    }

    public int getPageMax()
    {
        return pageMax;
    }

    public int getPageAct()
    {
        return pageAct;
    }

    public int getPageSt()
    {
        return this.pageSt;
    }

    public int getPageEd()
    {
        return this.pageEd;
    }

    public String getPageLink()
    {
        return pageLink;
    }

    /**
     *
     * setter
     *
     */
    public void setUserId(String userid)
    {
        this.userId = userid;
    }

    public void setPageMax(int pagemax)
    {
        this.pageMax = pagemax;
    }

    public void setPageAct(int pageact)
    {
        this.pageAct = pageact;
    }

    public void setHotelIdList(int hotelid)
    {
        this.hotelIdList.add( hotelid );
    }

    public void setHotelNmList(String hotelnm)
    {
        this.hotelNmList.add( hotelnm );
    }

    public void setReserveNoList(String reserveno)
    {
        this.reserveNoList.add( reserveno );
    }

    public void setReserveDateList(int reservedate)
    {
        this.reserveDateList.add( reservedate );
    }

    public void setReserveDtList(String reservedt)
    {
        this.reserveDtList.add( reservedt );
    }

    public void setPageSt(int pagest)
    {
        this.pageSt = pagest;
    }

    public void setPageEd(int pageed)
    {
        this.pageEd = pageed;
    }

    public void setPageLink(String pageLink)
    {
        this.pageLink = pageLink;
    }

}
