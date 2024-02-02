package jp.happyhotel.dto;

import java.util.ArrayList;

public class DtoApCouponList
{

    private DtoApCommon                dtoApCommon;
    private ArrayList<DtoApCouponUnit> dtoApCouponUnit;

    public DtoApCouponList()
    {
        this.dtoApCommon = null;
        this.dtoApCouponUnit = null;
    }

    public ArrayList<DtoApCouponUnit> getApCouponUnit()
    {
        return dtoApCouponUnit;
    }

    public void setApCouponUnit(ArrayList<DtoApCouponUnit> dtoApCouponUnit)
    {
        this.dtoApCouponUnit = dtoApCouponUnit;
    }

    public DtoApCommon getApCommon()
    {
        return dtoApCommon;
    }

    public void setApCommon(DtoApCommon dtoApCommon)
    {
        this.dtoApCommon = dtoApCommon;
    }

}
