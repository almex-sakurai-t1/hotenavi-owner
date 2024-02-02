package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML PMS予約連携 予約プランデータ
 */
public class GenerateXmlRsvPlanDataSub extends WebApiResultBase
{
    // タグ名
    private static final String                           TAG_DATA                   = "Data";
    private static final String                           TAG_PLAN_ID                = "PlanId";
    private static final String                           TAG_PLAN_NAME              = "PlanName";
    private static final String                           TAG_PLAN_TYPE              = "PlanType";
    private static final String                           TAG_STOCK_TEXT             = "StockText";
    private static final String                           TAG_STOCK_VALUE            = "StockValue";
    private static final String                           TAG_BONUS_MILE_TEXT        = "BonusMileText";
    private static final String                           TAG_BONUS_MILE_VALUE       = "BonusMileValue";
    private static final String                           TAG_PREMIUM_MILE           = "PremiumMile";
    private static final String                           TAG_NORMAL_MILE            = "NormalMile";
    private static final String                           TAG_NOTE                   = "Note";
    private static final String                           TAG_PLAN_PR                = "PlanPr";
    private static final String                           TAG_CHECK_IN_TEXT          = "CheckInText";
    private static final String                           TAG_FROM_TIME              = "FromTime";
    private static final String                           TAG_TO_TIME                = "ToTime";
    private static final String                           TAG_CHECK_OUT_TEXT         = "CheckOutText";
    private static final String                           TAG_COMING_SOON            = "ComingSoon";
    private static final String                           TAG_PEOPLE_TEXT            = "PeopleText";
    private static final String                           TAG_ADDITIONAL_CHARGE_TEXT = "AdditionalChargeText";
    private static final String                           TAG_PEOPLE_OPTION          = "PeopleOption";
    private static final String                           TAG_PRE_PAY                = "PrePay";
    private static final String                           TAG_PAY_AT_HOTEL           = "PayAtHotel";
    private static final String                           TAG_ROOM_RANKS             = "RoomRanks";
    private static final String                           TAG_ROOMS                  = "Rooms";
    private static final String                           TAG_CHARGE_TEXT            = "ChargeText";
    private static final String                           TAG_CHARGE                 = "Charge";
    private static final String                           TAG_SALES_STATUS           = "SalesStatus";

    private XmlTag                                        data;
    private XmlTag                                        planId;
    private XmlTag                                        planName;
    private XmlTag                                        planType;
    private XmlTag                                        stockText;
    private XmlTag                                        stockValue;
    private XmlTag                                        bonusMileText;
    private XmlTag                                        bonusMileValue;
    private XmlTag                                        premiumMile;
    private XmlTag                                        normalMile;
    private XmlTag                                        note;
    private XmlTag                                        planPr;
    private XmlTag                                        checkInText;
    private XmlTag                                        fromTime;
    private XmlTag                                        toTime;
    private XmlTag                                        checkOutText;
    private XmlTag                                        comingSoon;
    private XmlTag                                        peopleText;
    private XmlTag                                        additionalChargeText;
    private XmlTag                                        prePay;
    private XmlTag                                        payAtHotel;
    private XmlTag                                        chargeText;
    private XmlTag                                        charge;
    private XmlTag                                        salesStatus;

    private ArrayList<GenerateXmlRsvPlanDataPeopleOption> peopleOptionList           = new ArrayList<GenerateXmlRsvPlanDataPeopleOption>();
    private ArrayList<GenerateXmlRsvPlanDataRoomRanks>    roomRanksList              = new ArrayList<GenerateXmlRsvPlanDataRoomRanks>();
    private ArrayList<GenerateXmlRsvPlanDataRooms>        roomsList                  = new ArrayList<GenerateXmlRsvPlanDataRooms>();

    @Override
    protected void initXmlNodeInfo()
    {
        data = createRootChild( TAG_DATA );
        XmlTag.setParent( data, planId );
        XmlTag.setParent( data, planName );
        XmlTag.setParent( data, planType );
        XmlTag.setParent( data, stockText );
        XmlTag.setParent( data, stockValue );
        XmlTag.setParent( data, bonusMileText );
        XmlTag.setParent( data, bonusMileValue );
        XmlTag.setParent( data, premiumMile );
        XmlTag.setParent( data, normalMile );
        XmlTag.setParent( data, note );
        XmlTag.setParent( data, planPr );
        XmlTag.setParent( data, checkInText );
        XmlTag.setParent( data, fromTime );
        XmlTag.setParent( data, toTime );
        XmlTag.setParent( data, checkOutText );
        XmlTag.setParent( data, comingSoon );
        XmlTag.setParent( data, peopleText );
        XmlTag.setParent( data, additionalChargeText );
        for( int i = 0 ; i < peopleOptionList.size() ; i++ )
        {
            this.peopleOptionList.get( i ).setRootNode( data );
            this.peopleOptionList.get( i ).initXmlNodeInfo();
        }
        XmlTag.setParent( data, prePay );
        XmlTag.setParent( data, payAtHotel );
        XmlTag.setParent( data, chargeText );
        XmlTag.setParent( data, charge );
        for( int i = 0 ; i < roomRanksList.size() ; i++ )
        {
            this.roomRanksList.get( i ).setRootNode( data );
            this.roomRanksList.get( i ).initXmlNodeInfo();
        }
        for( int i = 0 ; i < roomsList.size() ; i++ )
        {
            this.roomsList.get( i ).setRootNode( data );
            this.roomsList.get( i ).initXmlNodeInfo();
        }
        XmlTag.setParent( data, salesStatus );
        return;
    }

    public void setData(String data)
    {
        this.data = XmlTag.createXmlTag( TAG_DATA, data );
    }

    public void setPlanId(String planId)
    {
        this.planId = XmlTag.createXmlTag( TAG_PLAN_ID, planId );
    }

    public void setPlanName(String planName)
    {
        this.planName = XmlTag.createXmlTag( TAG_PLAN_NAME, planName );
    }

    public void setPlanType(String planType)
    {
        this.planType = XmlTag.createXmlTag( TAG_PLAN_TYPE, planType );
    }

    public void setStockText(String stockText)
    {
        this.stockText = XmlTag.createXmlTag( TAG_STOCK_TEXT, stockText );
    }

    public void setStockValue(String stockValue)
    {
        this.stockValue = XmlTag.createXmlTag( TAG_STOCK_VALUE, stockValue );
    }

    public void setBonusMileText(String bonusMileText)
    {
        this.bonusMileText = XmlTag.createXmlTag( TAG_BONUS_MILE_TEXT, bonusMileText );
    }

    public void setBonusMileValue(String bonusMileValue)
    {
        this.bonusMileValue = XmlTag.createXmlTag( TAG_BONUS_MILE_VALUE, bonusMileValue );
    }

    public void setPremiumMile(String premiumMile)
    {
        this.premiumMile = XmlTag.createXmlTag( TAG_PREMIUM_MILE, premiumMile );
    }

    public void setNormalMile(String normalMile)
    {
        this.normalMile = XmlTag.createXmlTag( TAG_NORMAL_MILE, normalMile );
    }

    public void setNote(String note)
    {
        this.note = XmlTag.createXmlTag( TAG_NOTE, note );
    }

    public void setPlanPr(String planPr)
    {
        this.planPr = XmlTag.createXmlTag( TAG_PLAN_PR, planPr );
    }

    public void setCheckInText(String checkInText)
    {
        this.checkInText = XmlTag.createXmlTag( TAG_CHECK_IN_TEXT, checkInText );
    }

    public void setFromTime(String fromTime)
    {
        this.fromTime = XmlTag.createXmlTag( TAG_FROM_TIME, fromTime );
    }

    public void setToTime(String toTime)
    {
        this.toTime = XmlTag.createXmlTag( TAG_TO_TIME, toTime );
    }

    public void setCheckOutText(String checkOutText)
    {
        this.checkOutText = XmlTag.createXmlTag( TAG_CHECK_OUT_TEXT, checkOutText );
    }

    public void setComingSoon(String comingSoon)
    {
        this.comingSoon = XmlTag.createXmlTag( TAG_COMING_SOON, comingSoon );
    }

    public void setPeopleText(String peopleText)
    {
        this.peopleText = XmlTag.createXmlTag( TAG_PEOPLE_TEXT, peopleText );
    }

    public void setAdditionalChargeText(String additionalChargeText)
    {
        this.additionalChargeText = XmlTag.createXmlTag( TAG_ADDITIONAL_CHARGE_TEXT, additionalChargeText );
    }

    public void addPeopleOption(GenerateXmlRsvPlanDataPeopleOption peopleOption)
    {
        this.peopleOptionList.add( peopleOption );
    }

    public void setPrePay(String prePay)
    {
        this.prePay = XmlTag.createXmlTag( TAG_PRE_PAY, prePay );
    }

    public void setPayAtHotel(String payAtHotel)
    {
        this.payAtHotel = XmlTag.createXmlTag( TAG_PAY_AT_HOTEL, payAtHotel );
    }

    public void addRoomRanks(GenerateXmlRsvPlanDataRoomRanks roomRanks)
    {
        this.roomRanksList.add( roomRanks );
    }

    public void addRooms(GenerateXmlRsvPlanDataRooms rooms)
    {
        this.roomsList.add( rooms );
    }

    public void setChargeText(String chargeText)
    {
        this.chargeText = XmlTag.createXmlTag( TAG_CHARGE_TEXT, chargeText );
    }

    public void setCharge(String charge)
    {
        this.charge = XmlTag.createXmlTag( TAG_CHARGE, charge );
    }

    public void setSalesStatus(String salesStatus)
    {
        this.salesStatus = XmlTag.createXmlTag( TAG_SALES_STATUS, salesStatus );
    }

}
