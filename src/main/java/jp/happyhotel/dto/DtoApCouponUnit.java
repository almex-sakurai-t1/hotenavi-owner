package jp.happyhotel.dto;

public class DtoApCouponUnit
{

    int    couponNo;
    int    couponKind;
    int    usedFlag;
    String couponName;

    public DtoApCouponUnit()
    {
        this.couponNo = 0;
        this.couponKind = 0;
        this.usedFlag = 0;
        this.couponName = "";

    }

    public int getCouponNo()
    {
        return couponNo;
    }

    public int getCouponKind()
    {
        return couponKind;
    }

    public int getUsedFlag()
    {
        return usedFlag;
    }

    public void setCouponNo(int couponNo)
    {
        this.couponNo = couponNo;
    }

    public void setCouponKind(int couponKind)
    {
        this.couponKind = couponKind;
    }

    public void setUsedFlag(int usedFlag)
    {
        this.usedFlag = usedFlag;
    }

    public String getCouponName()
    {
        return couponName;
    }

    public void setCouponName(String couponName)
    {
        this.couponName = couponName;
    }

}
