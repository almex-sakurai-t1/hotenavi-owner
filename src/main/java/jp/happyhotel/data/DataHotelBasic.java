/*
 * @(#)DataHotelBasic.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ホテル基本情報クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Url;

/**
 * ホテルの基本情報の取得
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/17
 * @version 1.1 2007/11/15
 */
public class DataHotelBasic implements Serializable
{
    public static final int    COMPANY_TYPE_UNKNOWN                      = 0;                                                        // 未調査
    public static final int    COMPANY_TYPE_LOVEHOTEL_FIX                = 1;                                                        // 風俗営業法第4号(確定)
    public static final int    COMPANY_TYPE_LOVEHOTEL_NOTIFICATION_UNFIX = 2;                                                        // 風俗営業法第4号(届出書未確認)
    public static final int    COMPANY_TYPE_LOVEHOTEL_REPORT_UNFIX       = 3;                                                        // 風俗営業法第4号(調査票未着)
    public static final int    COMPANY_TYPE_LOVEHOTEL_UNFIX              = 4;                                                        // 風俗営業法第4号（両方未着）
    public static final int    COMPANY_TYPE_BUSINESSHOTEL_FIX            = 9;                                                        // 旅館業法
    public static final int    COMPANY_TYPE_BUSINESSHOTEL_REPORT_UNFIX   = 10;                                                       // 旅館業法（調査票未着）

    private static final long  serialVersionUID                          = 6183046423556510306L;

    public static final String COLUMNS                                   = "id,rank,kind,hotenavi_id,name,name_kana,name_mobile," +
                                                                                 "zip_code,jis_code,pref_id,pref_name,pref_kana,address1,address2,address3,address_all," +
                                                                                 "tel1,tel2,fax,charge_last,charge_first,charge_kana_last,charge_kana_first,charge_tel," +
                                                                                 "charge_mail,open_date,renewal_date,url,url_official1,url_official2,url_official_mobile," +
                                                                                 "pr,pr_detail,pr_event,pr_member,access,access_station,access_ic,room_count,parking,parking_count," +
                                                                                 "type_building,type_kodate,type_rentou,type_etc,location_station,location_ic,location_kougai,benefit," +
                                                                                 "roomservice,pay_front,pay_auto,credit,credit_visa,credit_master,credit_jcb,credit_dc,credit_nicos,credit_amex,credit_etc," +
                                                                                 "halfway,coupon,possible_one,possible_three,reserve,reserve_tel,reserve_mail,reserve_web,empty_method," +
                                                                                 "hotel_lat,hotel_lon,hotel_lat_num,hotel_lon_num,disp_lat,disp_lon,zoom,over18_flag,company_type," +
                                                                                 "empty_kind,empty_status,group_code,last_update,last_uptime,pr_room,pr_etc,renewal_flag,url_special," +
                                                                                 "hotel_lat_jp,hotel_lon_jp,map_code,high_roof,high_roof_count,empty_hotenavi_id,attention_flag," +
                                                                                 "ad_pref_id,renewal_date_text,new_open_search_flag";

    private int                id;
    private int                rank;
    private int                kind;
    private String             hotenaviId;
    private String             name;
    private String             nameKana;
    private String             nameMobile;
    private String             zipCode;
    private int                jisCode;
    private int                prefId;
    private String             prefName;
    private String             prefKana;
    private String             address1;
    private String             address2;
    private String             address3;
    private String             addressAll;
    private String             tel1;
    private String             tel2;
    private String             fax;
    private String             chargeLast;
    private String             chargeFirst;
    private String             chargeKanaLast;
    private String             chargeKanaFirst;
    private String             chargeTel;
    private String             chargeMail;
    private int                openDate;
    private int                renewalDate;
    private String             url;
    private String             urlOfficial1;
    private String             urlOfficial2;
    private String             urlOfficialMobile;
    private String             pr;
    private String             prDetail;
    private String             prEvent;
    private String             prMember;
    private String             access;
    private String             accessStation;
    private String             accessIc;
    private int                roomCount;
    private int                parking;
    private int                parkingCount;
    private int                typeBuilding;
    private int                typeKodate;
    private int                typeRentou;
    private String             typeEtc;
    private int                locationStation;
    private int                locationIc;
    private int                locationKougai;
    private int                benefit;
    private int                roomService;
    private int                payFront;
    private int                payAuto;
    private int                credit;
    private int                creditVisa;
    private int                creditMaster;
    private int                creditJcb;
    private int                creditDc;
    private int                creditNicos;
    private int                creditAmex;
    private String             creditEtc;
    private int                halfway;
    private int                coupon;
    private int                possibleOne;
    private int                possibleThree;
    private int                reserve;
    private int                reserveTel;
    private int                reserveMail;
    private int                reserveWeb;
    private int                emptyMethod;
    private String             hotelLat;
    private String             hotelLon;
    private int                hotelLatNum;
    private int                hotelLonNum;
    private String             dispLat;
    private String             dispLon;
    private int                zoom;
    private int                over18Flag;
    private int                companyType;
    private int                emptyKind;
    private int                emptyStatus;
    private int                groupCode;
    private byte[]             hotelPicturePc;
    private byte[]             hotelPictureGif;
    private byte[]             hotelPicturePng;
    private int                lastUpdate;
    private int                lastUptime;
    private String             prRoom;
    private String             prEtc;
    private int                renewalFlag;
    private String             urlSpecial;
    private String             hotelLatJp;
    private String             hotelLonJp;
    private String             mapCode;
    private int                highRoof;
    private int                highRoofCount;
    private String             emptyHotenaviId;
    private int                attentionFlag;
    private int                adPrefId;
    private String             renewalDateText;
    private int                newOpenSearchFlag;

    /**
     * データを初期化します。
     */
    public DataHotelBasic()
    {
        id = 0;
        rank = 0;
        kind = 0;
        hotenaviId = "";
        name = "";
        nameKana = "";
        nameMobile = "";
        zipCode = "";
        jisCode = 0;
        prefId = 0;
        prefName = "";
        prefKana = "";
        address1 = "";
        address2 = "";
        address3 = "";
        addressAll = "";
        tel1 = "";
        tel2 = "";
        fax = "";
        chargeLast = "";
        chargeFirst = "";
        chargeKanaLast = "";
        chargeKanaFirst = "";
        chargeTel = "";
        chargeMail = "";
        openDate = 0;
        renewalDate = 0;
        url = "";
        urlOfficial1 = "";
        urlOfficial2 = "";
        urlOfficialMobile = "";
        pr = "";
        prDetail = "";
        prEvent = "";
        prMember = "";
        access = "";
        accessStation = "";
        accessIc = "";
        roomCount = 0;
        parking = 0;
        parkingCount = 0;
        typeBuilding = 0;
        typeKodate = 0;
        typeRentou = 0;
        typeEtc = "";
        locationStation = 0;
        locationIc = 0;
        locationKougai = 0;
        benefit = 0;
        roomService = 0;
        payFront = 0;
        payAuto = 0;
        credit = 0;
        creditVisa = 0;
        creditMaster = 0;
        creditJcb = 0;
        creditDc = 0;
        creditNicos = 0;
        creditAmex = 0;
        creditEtc = "";
        halfway = 0;
        coupon = 0;
        possibleOne = 0;
        possibleThree = 0;
        reserve = 0;
        reserveTel = 0;
        reserveMail = 0;
        reserveWeb = 0;
        emptyMethod = 0;
        hotelLat = "";
        hotelLon = "";
        hotelLatNum = 0;
        hotelLonNum = 0;
        dispLat = "";
        dispLon = "";
        zoom = 0;
        over18Flag = 0;
        companyType = 0;
        emptyKind = 0;
        emptyStatus = 0;
        groupCode = 0;
        hotelPicturePc = null;
        hotelPictureGif = null;
        hotelPicturePng = null;
        lastUpdate = 0;
        lastUptime = 0;
        prRoom = "";
        prEtc = "";
        renewalFlag = 0;
        urlSpecial = "";
        hotelLatJp = "";
        hotelLonJp = "";
        mapCode = "";
        highRoof = 0;
        highRoofCount = 0;
        emptyHotenaviId = "";
        attentionFlag = 0;
        adPrefId = 0;
        renewalDateText = "";
        newOpenSearchFlag = 0;

    }

    public String getRenewalDateText()
    {
        return renewalDateText;
    }

    public int getNewOpenSearchFlag()
    {
        return newOpenSearchFlag;
    }

    public void setRenewalDateText(String renewalDateText)
    {
        renewalDateText = this.renewalDateText;
    }

    public void setNewOpenSearchFlag(int newOpenSearchFlag)
    {
        newOpenSearchFlag = this.newOpenSearchFlag;
    }

    public int getAttentionFlag()
    {
        return attentionFlag;
    }

    public int getAdPrefId()
    {
        return adPrefId;
    }

    public void setAttentionFlag(int attentionFlag)
    {
        this.attentionFlag = attentionFlag;
    }

    public void setAdPrefId(int adPrefId)
    {
        this.adPrefId = adPrefId;
    }

    public String getEmptyHotenaviId()
    {
        return emptyHotenaviId;
    }

    public void setEmptyHotenaviId(String emptyHotenaviId)
    {
        this.emptyHotenaviId = emptyHotenaviId;
    }

    public int getHighRoof()
    {
        return highRoof;
    }

    public void setHighRoof(int highRoof)
    {
        this.highRoof = highRoof;
    }

    public int getHighRoofCount()
    {
        return highRoofCount;
    }

    public void setHighRoofCount(int highRoofCount)
    {
        this.highRoofCount = highRoofCount;
    }

    public String getUrlSpecial()
    {
        return Url.convertUrl( urlSpecial );
    }

    public void setUrlSpecial(String urlSpecial)
    {
        this.urlSpecial = urlSpecial;
    }

    public String getHotelLatJp()
    {
        return hotelLatJp;
    }

    public void setHotelLatJp(String hotelLatJp)
    {
        this.hotelLatJp = hotelLatJp;
    }

    public String getHotelLonJp()
    {
        return hotelLonJp;
    }

    public void setHotelLonJp(String hotelLonJp)
    {
        this.hotelLonJp = hotelLonJp;
    }

    public String getMapCode()
    {
        return mapCode;
    }

    public void setMapCode(String mapCode)
    {
        this.mapCode = mapCode;
    }

    public int getRenewalFlag()
    {
        return renewalFlag;
    }

    public void setRenewalFlag(int renewalFlag)
    {
        this.renewalFlag = renewalFlag;
    }

    public String getPrEtc()
    {
        return prEtc;
    }

    public String getPrRoom()
    {
        return prRoom;
    }

    public void setPrEtc(String prEtc)
    {
        this.prEtc = prEtc;
    }

    public void setPrRoom(String prRoom)
    {
        this.prRoom = prRoom;
    }

    public String getAccess()
    {
        return access;
    }

    public String getAccessIc()
    {
        return accessIc;
    }

    public String getAccessStation()
    {
        return accessStation;
    }

    public String getAddress1()
    {
        return address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public String getAddress3()
    {
        return address3;
    }

    public String getAddressAll()
    {
        return addressAll;
    }

    public int getBenefit()
    {
        return benefit;
    }

    public String getChargeFirst()
    {
        return chargeFirst;
    }

    public String getChargeKanaFirst()
    {
        return chargeKanaFirst;
    }

    public String getChargeKanaLast()
    {
        return chargeKanaLast;
    }

    public String getChargeLast()
    {
        return chargeLast;
    }

    public String getChargeMail()
    {
        return chargeMail;
    }

    public String getChargeTel()
    {
        return chargeTel;
    }

    public int getCompanyType()
    {
        return companyType;
    }

    public int getCoupon()
    {
        return coupon;
    }

    public int getCredit()
    {
        return credit;
    }

    public int getCreditAmex()
    {
        return creditAmex;
    }

    public int getCreditDc()
    {
        return creditDc;
    }

    public String getCreditEtc()
    {
        return creditEtc;
    }

    public int getCreditJcb()
    {
        return creditJcb;
    }

    public int getCreditMaster()
    {
        return creditMaster;
    }

    public int getCreditNicos()
    {
        return creditNicos;
    }

    public int getCreditVisa()
    {
        return creditVisa;
    }

    public String getDispLat()
    {
        return dispLat;
    }

    public String getDispLon()
    {
        return dispLon;
    }

    public int getEmptyKind()
    {
        return emptyKind;
    }

    public int getEmptyMethod()
    {
        return emptyMethod;
    }

    public int getEmptyStatus()
    {
        return emptyStatus;
    }

    public String getFax()
    {
        return fax;
    }

    public int getGroupCode()
    {
        return groupCode;
    }

    public int getHalfway()
    {
        return halfway;
    }

    public String getHotelLat()
    {
        return hotelLat;
    }

    public int getHotelLatNum()
    {
        return hotelLatNum;
    }

    public String getHotelLon()
    {
        return hotelLon;
    }

    public int getHotelLonNum()
    {
        return hotelLonNum;
    }

    public byte[] getHotelPictureGif()
    {
        return hotelPictureGif;
    }

    public byte[] getHotelPicturePc()
    {
        return hotelPicturePc;
    }

    public byte[] getHotelPicturePng()
    {
        return hotelPicturePng;
    }

    public String getHotenaviId()
    {
        return hotenaviId;
    }

    public int getId()
    {
        return id;
    }

    public int getJisCode()
    {
        return jisCode;
    }

    public int getKind()
    {
        return kind;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public int getLocationIc()
    {
        return locationIc;
    }

    public int getLocationKougai()
    {
        return locationKougai;
    }

    public int getLocationStation()
    {
        return locationStation;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public String getNameMobile()
    {
        return nameMobile;
    }

    public int getOpenDate()
    {
        return openDate;
    }

    public int getOver18Flag()
    {
        return over18Flag;
    }

    public int getParking()
    {
        return parking;
    }

    public int getParkingCount()
    {
        return parkingCount;
    }

    public int getPayAuto()
    {
        return payAuto;
    }

    public int getPayFront()
    {
        return payFront;
    }

    public int getPossibleOne()
    {
        return possibleOne;
    }

    public int getPossibleThree()
    {
        return possibleThree;
    }

    public String getPr()
    {
        return pr;
    }

    public String getPrDetail()
    {
        return prDetail;
    }

    public int getPrefId()
    {
        return prefId;
    }

    public String getPrefKana()
    {
        return prefKana;
    }

    public String getPrefName()
    {
        return prefName;
    }

    public String getPrEvent()
    {
        return prEvent;
    }

    public String getPrMember()
    {
        return prMember;
    }

    public int getRank()
    {
        return rank;
    }

    public int getRenewalDate()
    {
        return renewalDate;
    }

    public int getReserve()
    {
        return reserve;
    }

    public int getReserveMail()
    {
        return reserveMail;
    }

    public int getReserveTel()
    {
        return reserveTel;
    }

    public int getReserveWeb()
    {
        return reserveWeb;
    }

    public int getRoomCount()
    {
        return roomCount;
    }

    public int getRoomService()
    {
        return roomService;
    }

    public String getTel1()
    {
        return tel1;
    }

    public String getTel2()
    {
        return tel2;
    }

    public int getTypeBuilding()
    {
        return typeBuilding;
    }

    public String getTypeEtc()
    {
        return typeEtc;
    }

    public int getTypeKodate()
    {
        return typeKodate;
    }

    public int getTypeRentou()
    {
        return typeRentou;
    }

    public String getUrl()
    {
        return url;
    }

    public String getUrlOfficial1()
    {
        return urlOfficial1;
    }

    public String getUrlOfficial2()
    {
        return urlOfficial2;
    }

    public String getUrlOfficialMobile()
    {
        return urlOfficialMobile;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public int getZoom()
    {
        return zoom;
    }

    public void setAccess(String access)
    {
        this.access = access;
    }

    public void setAccessIc(String accessIc)
    {
        this.accessIc = accessIc;
    }

    public void setAccessStation(String accessStation)
    {
        this.accessStation = accessStation;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public void setAddress3(String address3)
    {
        this.address3 = address3;
    }

    public void setAddressAll(String addressAll)
    {
        this.addressAll = addressAll;
    }

    public void setBenefit(int benefit)
    {
        this.benefit = benefit;
    }

    public void setChargeFirst(String chargeFirst)
    {
        this.chargeFirst = chargeFirst;
    }

    public void setChargeKanaFirst(String chargeKanaFirst)
    {
        this.chargeKanaFirst = chargeKanaFirst;
    }

    public void setChargeKanaLast(String chargeKanaLast)
    {
        this.chargeKanaLast = chargeKanaLast;
    }

    public void setChargeLast(String chargeLast)
    {
        this.chargeLast = chargeLast;
    }

    public void setChargeMail(String chargeMail)
    {
        this.chargeMail = chargeMail;
    }

    public void setChargeTel(String chargeTel)
    {
        this.chargeTel = chargeTel;
    }

    public void setCompanyType(int companyType)
    {
        this.companyType = companyType;
    }

    public void setCoupon(int coupon)
    {
        this.coupon = coupon;
    }

    public void setCredit(int credit)
    {
        this.credit = credit;
    }

    public void setCreditAmex(int creditAmex)
    {
        this.creditAmex = creditAmex;
    }

    public void setCreditDc(int creditDc)
    {
        this.creditDc = creditDc;
    }

    public void setCreditEtc(String creditEtc)
    {
        this.creditEtc = creditEtc;
    }

    public void setCreditJcb(int creditJcb)
    {
        this.creditJcb = creditJcb;
    }

    public void setCreditMaster(int creditMaster)
    {
        this.creditMaster = creditMaster;
    }

    public void setCreditNicos(int creditNicos)
    {
        this.creditNicos = creditNicos;
    }

    public void setCreditVisa(int creditVisa)
    {
        this.creditVisa = creditVisa;
    }

    public void setDispLat(String dispLat)
    {
        this.dispLat = dispLat;
    }

    public void setDispLon(String dispLon)
    {
        this.dispLon = dispLon;
    }

    public void setEmptyKind(int emptyKind)
    {
        this.emptyKind = emptyKind;
    }

    public void setEmptyMethod(int emptyMethod)
    {
        this.emptyMethod = emptyMethod;
    }

    public void setEmptyStatus(int emptyStatus)
    {
        this.emptyStatus = emptyStatus;
    }

    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public void setGroupCode(int groupCode)
    {
        this.groupCode = groupCode;
    }

    public void setHalfway(int halfway)
    {
        this.halfway = halfway;
    }

    public void setHotelLat(String hotelLat)
    {
        this.hotelLat = hotelLat;
    }

    public void setHotelLatNum(int hotelLatNum)
    {
        this.hotelLatNum = hotelLatNum;
    }

    public void setHotelLon(String hotelLon)
    {
        this.hotelLon = hotelLon;
    }

    public void setHotelLonNum(int hotelLonNum)
    {
        this.hotelLonNum = hotelLonNum;
    }

    public void setHotelPictureGif(byte[] hotelPictureGif)
    {
        this.hotelPictureGif = hotelPictureGif;
    }

    public void setHotelPicturePc(byte[] hotelPicturePc)
    {
        this.hotelPicturePc = hotelPicturePc;
    }

    public void setHotelPicturePng(byte[] hotelPicturePng)
    {
        this.hotelPicturePng = hotelPicturePng;
    }

    public void setHotenaviId(String hotenaviId)
    {
        this.hotenaviId = hotenaviId;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setJisCode(int jisCode)
    {
        this.jisCode = jisCode;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setLocationIc(int locationIc)
    {
        this.locationIc = locationIc;
    }

    public void setLocationKougai(int locationKougai)
    {
        this.locationKougai = locationKougai;
    }

    public void setLocationStation(int locationStation)
    {
        this.locationStation = locationStation;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public void setNameMobile(String nameMobile)
    {
        this.nameMobile = nameMobile;
    }

    public void setOpenDate(int openDate)
    {
        this.openDate = openDate;
    }

    public void setOver18Flag(int over18Flag)
    {
        this.over18Flag = over18Flag;
    }

    public void setParking(int parking)
    {
        this.parking = parking;
    }

    public void setParkingCount(int parkingCount)
    {
        this.parkingCount = parkingCount;
    }

    public void setPayAuto(int payAuto)
    {
        this.payAuto = payAuto;
    }

    public void setPayFront(int payFront)
    {
        this.payFront = payFront;
    }

    public void setPossibleOne(int possibleOne)
    {
        this.possibleOne = possibleOne;
    }

    public void setPossibleThree(int possibleThree)
    {
        this.possibleThree = possibleThree;
    }

    public void setPr(String pr)
    {
        this.pr = pr;
    }

    public void setPrDetail(String prDetail)
    {
        this.prDetail = prDetail;
    }

    public void setPrefId(int prefId)
    {
        this.prefId = prefId;
    }

    public void setPrefKana(String prefKana)
    {
        this.prefKana = prefKana;
    }

    public void setPrefName(String prefName)
    {
        this.prefName = prefName;
    }

    public void setPrEvent(String prEvent)
    {
        this.prEvent = prEvent;
    }

    public void setPrMember(String prMember)
    {
        this.prMember = prMember;
    }

    public void setRank(int rank)
    {
        this.rank = rank;
    }

    public void setRenewalDate(int renewalDate)
    {
        this.renewalDate = renewalDate;
    }

    public void setReserve(int reserve)
    {
        this.reserve = reserve;
    }

    public void setReserveMail(int reserveMail)
    {
        this.reserveMail = reserveMail;
    }

    public void setReserveTel(int reserveTel)
    {
        this.reserveTel = reserveTel;
    }

    public void setReserveWeb(int reserveWeb)
    {
        this.reserveWeb = reserveWeb;
    }

    public void setRoomCount(int roomCount)
    {
        this.roomCount = roomCount;
    }

    public void setRoomService(int roomService)
    {
        this.roomService = roomService;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public void setTel2(String tel2)
    {
        this.tel2 = tel2;
    }

    public void setTypeBuilding(int typeBuilding)
    {
        this.typeBuilding = typeBuilding;
    }

    public void setTypeEtc(String typeEtc)
    {
        this.typeEtc = typeEtc;
    }

    public void setTypeKodate(int typeKodate)
    {
        this.typeKodate = typeKodate;
    }

    public void setTypeRentou(int typeRentou)
    {
        this.typeRentou = typeRentou;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setUrlOfficial1(String urlOfficial1)
    {
        this.urlOfficial1 = urlOfficial1;
    }

    public void setUrlOfficial2(String urlOfficial2)
    {
        this.urlOfficial2 = urlOfficial2;
    }

    public void setUrlOfficialMobile(String urlOfficialMobile)
    {
        this.urlOfficialMobile = urlOfficialMobile;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public void setZoom(int zoom)
    {
        this.zoom = zoom;
    }

    /**
     * ホテル情報取得
     * 
     * @param hotelId ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId)
    {
        boolean ret;
        Connection connection = null;
        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            ret = getData( connection, hotelId );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBasic.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ホテル情報取得
     * 
     * @param hotelId ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(Connection connection, int hotelId)
    {
        boolean ret;
        String query;
        String column;
        String[] colArr = COLUMNS.split( "," );
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT ";
        for( int i = 0 ; i < colArr.length ; i++ )
        {
            query += " ";
            if ( i > 0 )
            {
                query += ", ";
            }
            column = colArr[i];
            if ( "empty_kind".equals( column.toLowerCase() ) )
            {
                query += "IFNULL(hh_hotel_status.`mode`, 0) ";
            }
            else if ( "empty_status".equals( column.toLowerCase() ) )
            {
                query += "IFNULL(hh_hotel_status.empty_status, 0) ";
            }
            else
            {
                query += "hh_hotel_basic.";
            }
            query += column;
        }
        query += " FROM hh_hotel_basic";
        query += " LEFT JOIN hh_hotel_status";
        query += " ON hh_hotel_basic.id = hh_hotel_status.id";
        query += " WHERE hh_hotel_basic.id = ?";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    setData( result );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBasic.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * ホテルデータ設定
     * 
     * @param result ホテルデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.rank = result.getInt( "rank" );
                this.kind = result.getInt( "kind" );
                this.hotenaviId = result.getString( "hotenavi_id" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
                this.nameMobile = result.getString( "name_mobile" );
                this.zipCode = result.getString( "zip_code" );
                this.jisCode = result.getInt( "jis_code" );
                this.prefId = result.getInt( "pref_id" );
                this.prefName = result.getString( "pref_name" );
                this.prefKana = result.getString( "pref_kana" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.address3 = result.getString( "address3" );
                this.addressAll = result.getString( "address_all" );
                this.tel1 = result.getString( "tel1" );
                this.tel2 = result.getString( "tel2" );
                this.fax = result.getString( "fax" );
                this.chargeLast = result.getString( "charge_last" );
                this.chargeFirst = result.getString( "charge_first" );
                this.chargeKanaLast = result.getString( "charge_kana_last" );
                this.chargeKanaFirst = result.getString( "charge_kana_first" );
                this.chargeTel = result.getString( "charge_tel" );
                this.chargeMail = result.getString( "charge_mail" );
                this.openDate = result.getInt( "open_date" );
                this.renewalDate = result.getInt( "renewal_date" );
                this.url = result.getString( "url" );
                this.urlOfficial1 = result.getString( "url_official1" );
                this.urlOfficial2 = result.getString( "url_official2" );
                this.urlOfficialMobile = result.getString( "url_official_mobile" );
                this.pr = result.getString( "pr" );
                this.prDetail = result.getString( "pr_detail" );
                this.prEvent = result.getString( "pr_event" );
                this.prMember = result.getString( "pr_member" );
                this.access = result.getString( "access" );
                this.accessStation = result.getString( "access_station" );
                this.accessIc = result.getString( "access_ic" );
                this.roomCount = result.getInt( "room_count" );
                this.parking = result.getInt( "parking" );
                this.parkingCount = result.getInt( "parking_count" );
                this.typeBuilding = result.getInt( "type_building" );
                this.typeKodate = result.getInt( "type_kodate" );
                this.typeRentou = result.getInt( "type_rentou" );
                this.typeEtc = result.getString( "type_etc" );
                this.locationStation = result.getInt( "location_station" );
                this.locationIc = result.getInt( "location_ic" );
                this.locationKougai = result.getInt( "location_kougai" );
                this.benefit = result.getInt( "benefit" );
                this.roomService = result.getInt( "roomservice" );
                this.payFront = result.getInt( "pay_front" );
                this.payAuto = result.getInt( "pay_auto" );
                this.credit = result.getInt( "credit" );
                this.creditVisa = result.getInt( "credit_visa" );
                this.creditMaster = result.getInt( "credit_master" );
                this.creditJcb = result.getInt( "credit_jcb" );
                this.creditDc = result.getInt( "credit_dc" );
                this.creditNicos = result.getInt( "credit_nicos" );
                this.creditAmex = result.getInt( "credit_amex" );
                this.creditEtc = result.getString( "credit_etc" );
                this.halfway = result.getInt( "halfway" );
                this.coupon = result.getInt( "coupon" );
                this.possibleOne = result.getInt( "possible_one" );
                this.possibleThree = result.getInt( "possible_three" );
                this.reserve = result.getInt( "reserve" );
                this.reserveTel = result.getInt( "reserve_tel" );
                this.reserveMail = result.getInt( "reserve_mail" );
                this.reserveWeb = result.getInt( "reserve_web" );
                this.emptyMethod = result.getInt( "empty_method" );
                this.hotelLat = result.getString( "hotel_lat" );
                this.hotelLon = result.getString( "hotel_lon" );
                this.hotelLatNum = result.getInt( "hotel_lat_num" );
                this.hotelLonNum = result.getInt( "hotel_lon_num" );
                this.dispLat = result.getString( "disp_lat" );
                this.dispLon = result.getString( "disp_lon" );
                this.zoom = result.getInt( "zoom" );
                this.over18Flag = result.getInt( "over18_flag" );
                this.companyType = result.getInt( "company_type" );
                this.emptyKind = result.getInt( "empty_kind" );
                this.emptyStatus = result.getInt( "empty_status" );
                this.groupCode = result.getInt( "group_code" );
                // this.hotelPicturePc = result.getBytes("hotel_picture_pc");
                // this.hotelPictureGif = result.getBytes("hotel_picture_gif");
                // this.hotelPicturePng = result.getBytes("hotel_picture_png");
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.prRoom = result.getString( "pr_room" );
                this.prEtc = result.getString( "pr_etc" );
                this.renewalFlag = result.getInt( "renewal_flag" );
                this.urlSpecial = result.getString( "url_special" );
                this.hotelLatJp = result.getString( "hotel_lat_jp" );
                this.hotelLonJp = result.getString( "hotel_lon_jp" );
                this.mapCode = result.getString( "map_code" );
                this.highRoof = result.getInt( "high_roof" );
                this.highRoofCount = result.getInt( "high_roof_count" );
                this.emptyHotenaviId = result.getString( "empty_hotenavi_id" );
                this.attentionFlag = result.getInt( "attention_flag" );
                this.adPrefId = result.getInt( "ad_pref_id" );
                this.renewalDateText = result.getString( "renewal_date_text" );
                this.newOpenSearchFlag = result.getInt( "new_open_search_flag" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBasic.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ホテル情報取得（画像あり）
     * 
     * @param hotelId ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataEx(int hotelId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;
        query = "SELECT";
        query += "  hh_hotel_basic.id";
        query += ", hh_hotel_basic.rank";
        query += ", hh_hotel_basic.kind";
        query += ", hh_hotel_basic.hotenavi_id";
        query += ", hh_hotel_basic.name";
        query += ", hh_hotel_basic.name_kana";
        query += ", hh_hotel_basic.name_mobile";
        query += ", hh_hotel_basic.zip_code";
        query += ", hh_hotel_basic.jis_code";
        query += ", hh_hotel_basic.pref_id";
        query += ", hh_hotel_basic.pref_name";
        query += ", hh_hotel_basic.pref_kana";
        query += ", hh_hotel_basic.address1";
        query += ", hh_hotel_basic.address2";
        query += ", hh_hotel_basic.address3";
        query += ", hh_hotel_basic.address_all";
        query += ", hh_hotel_basic.tel1";
        query += ", hh_hotel_basic.tel2";
        query += ", hh_hotel_basic.fax";
        query += ", hh_hotel_basic.charge_last";
        query += ", hh_hotel_basic.charge_first";
        query += ", hh_hotel_basic.charge_kana_last";
        query += ", hh_hotel_basic.charge_kana_first";
        query += ", hh_hotel_basic.charge_tel";
        query += ", hh_hotel_basic.charge_mail";
        query += ", hh_hotel_basic.open_date";
        query += ", hh_hotel_basic.renewal_date";
        query += ", hh_hotel_basic.url";
        query += ", hh_hotel_basic.url_official1";
        query += ", hh_hotel_basic.url_official2";
        query += ", hh_hotel_basic.url_official_mobile";
        query += ", hh_hotel_basic.pr";
        query += ", hh_hotel_basic.pr_detail";
        query += ", hh_hotel_basic.pr_event";
        query += ", hh_hotel_basic.pr_member";
        query += ", hh_hotel_basic.access";
        query += ", hh_hotel_basic.access_station";
        query += ", hh_hotel_basic.access_ic";
        query += ", hh_hotel_basic.room_count";
        query += ", hh_hotel_basic.parking";
        query += ", hh_hotel_basic.parking_count";
        query += ", hh_hotel_basic.type_building";
        query += ", hh_hotel_basic.type_kodate";
        query += ", hh_hotel_basic.type_rentou";
        query += ", hh_hotel_basic.type_etc";
        query += ", hh_hotel_basic.location_station";
        query += ", hh_hotel_basic.location_ic";
        query += ", hh_hotel_basic.location_kougai";
        query += ", hh_hotel_basic.benefit";
        query += ", hh_hotel_basic.roomservice";
        query += ", hh_hotel_basic.pay_front";
        query += ", hh_hotel_basic.pay_auto";
        query += ", hh_hotel_basic.credit";
        query += ", hh_hotel_basic.credit_visa";
        query += ", hh_hotel_basic.credit_master";
        query += ", hh_hotel_basic.credit_jcb";
        query += ", hh_hotel_basic.credit_dc";
        query += ", hh_hotel_basic.credit_nicos";
        query += ", hh_hotel_basic.credit_amex";
        query += ", hh_hotel_basic.credit_etc";
        query += ", hh_hotel_basic.halfway";
        query += ", hh_hotel_basic.coupon";
        query += ", hh_hotel_basic.possible_one";
        query += ", hh_hotel_basic.possible_three";
        query += ", hh_hotel_basic.reserve";
        query += ", hh_hotel_basic.reserve_tel";
        query += ", hh_hotel_basic.reserve_mail";
        query += ", hh_hotel_basic.reserve_web";
        query += ", hh_hotel_basic.empty_method";
        query += ", hh_hotel_basic.hotel_lat";
        query += ", hh_hotel_basic.hotel_lon";
        query += ", hh_hotel_basic.hotel_lat_num";
        query += ", hh_hotel_basic.hotel_lon_num";
        query += ", hh_hotel_basic.disp_lat";
        query += ", hh_hotel_basic.disp_lon";
        query += ", hh_hotel_basic.zoom";
        query += ", hh_hotel_basic.over18_flag";
        query += ", hh_hotel_basic.company_type";
        query += ", hh_hotel_basic.last_update";
        query += ", hh_hotel_basic.last_uptime";
        query += ", IFNULL(hh_hotel_status.`mode`, 0) AS empty_kind";
        query += ", IFNULL(hh_hotel_status.empty_status, 0) AS empty_status";
        query += ", hh_hotel_basic.group_code";
        query += ", hh_hotel_basic.hotel_picture_pc";
        query += ", hh_hotel_basic.hotel_picture_gif";
        query += ", hh_hotel_basic.hotel_picture_png";
        query += ", hh_hotel_basic.pr_room";
        query += ", hh_hotel_basic.pr_etc";
        query += ", hh_hotel_basic.renewal_flag";
        query += ", hh_hotel_basic.url_special";
        query += ", hh_hotel_basic.hotel_lat_jp";
        query += ", hh_hotel_basic.hotel_lon_jp";
        query += ", hh_hotel_basic.map_code";
        query += ", hh_hotel_basic.high_roof";
        query += ", hh_hotel_basic.high_roof_count";
        query += ", hh_hotel_basic.empty_hotenavi_id";
        query += ", hh_hotel_basic.attention_flag";
        query += ", hh_hotel_basic.ad_pref_id";
        query += ", hh_hotel_basic.renewal_date_text";
        query += ", hh_hotel_basic.new_open_search_flag";
        query += ", hh_hotel_basic.url_yahoo";
        query += ", hh_hotel_basic.touch_equip_flag";
        query += ", hh_hotel_basic.noshow_hotelid";
        query += " FROM hh_hotel_basic";
        query += " LEFT JOIN hh_hotel_status";
        query += " ON hh_hotel_basic.id = hh_hotel_status.id";
        query += " WHERE hh_hotel_basic.id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.rank = result.getInt( "rank" );
                    this.kind = result.getInt( "kind" );
                    this.hotenaviId = result.getString( "hotenavi_id" );
                    this.name = result.getString( "name" );
                    this.nameKana = result.getString( "name_kana" );
                    this.nameMobile = result.getString( "name_mobile" );
                    this.zipCode = result.getString( "zip_code" );
                    this.jisCode = result.getInt( "jis_code" );
                    this.prefId = result.getInt( "pref_id" );
                    this.prefName = result.getString( "pref_name" );
                    this.prefKana = result.getString( "pref_kana" );
                    this.address1 = result.getString( "address1" );
                    this.address2 = result.getString( "address2" );
                    this.address3 = result.getString( "address3" );
                    this.addressAll = result.getString( "address_all" );
                    this.tel1 = result.getString( "tel1" );
                    this.tel2 = result.getString( "tel2" );
                    this.fax = result.getString( "fax" );
                    this.chargeLast = result.getString( "charge_last" );
                    this.chargeFirst = result.getString( "charge_first" );
                    this.chargeKanaLast = result.getString( "charge_kana_last" );
                    this.chargeKanaFirst = result.getString( "charge_kana_first" );
                    this.chargeTel = result.getString( "charge_tel" );
                    this.chargeMail = result.getString( "charge_mail" );
                    this.openDate = result.getInt( "open_date" );
                    this.renewalDate = result.getInt( "renewal_date" );
                    this.url = result.getString( "url" );
                    this.urlOfficial1 = result.getString( "url_official1" );
                    this.urlOfficial2 = result.getString( "url_official2" );
                    this.urlOfficialMobile = result.getString( "url_official_mobile" );
                    this.pr = result.getString( "pr" );
                    this.prDetail = result.getString( "pr_detail" );
                    this.prEvent = result.getString( "pr_event" );
                    this.prMember = result.getString( "pr_member" );
                    this.access = result.getString( "access" );
                    this.accessStation = result.getString( "access_station" );
                    this.accessIc = result.getString( "access_ic" );
                    this.roomCount = result.getInt( "room_count" );
                    this.parking = result.getInt( "parking" );
                    this.parkingCount = result.getInt( "parking_count" );
                    this.typeBuilding = result.getInt( "type_building" );
                    this.typeKodate = result.getInt( "type_kodate" );
                    this.typeRentou = result.getInt( "type_rentou" );
                    this.typeEtc = result.getString( "type_etc" );
                    this.locationStation = result.getInt( "location_station" );
                    this.locationIc = result.getInt( "location_ic" );
                    this.locationKougai = result.getInt( "location_kougai" );
                    this.benefit = result.getInt( "benefit" );
                    this.roomService = result.getInt( "roomservice" );
                    this.payFront = result.getInt( "pay_front" );
                    this.payAuto = result.getInt( "pay_auto" );
                    this.credit = result.getInt( "credit" );
                    this.creditVisa = result.getInt( "credit_visa" );
                    this.creditMaster = result.getInt( "credit_master" );
                    this.creditJcb = result.getInt( "credit_jcb" );
                    this.creditDc = result.getInt( "credit_dc" );
                    this.creditNicos = result.getInt( "credit_nicos" );
                    this.creditAmex = result.getInt( "credit_amex" );
                    this.creditEtc = result.getString( "credit_etc" );
                    this.halfway = result.getInt( "halfway" );
                    this.coupon = result.getInt( "coupon" );
                    this.possibleOne = result.getInt( "possible_one" );
                    this.possibleThree = result.getInt( "possible_three" );
                    this.reserve = result.getInt( "reserve" );
                    this.reserveTel = result.getInt( "reserve_tel" );
                    this.reserveMail = result.getInt( "reserve_mail" );
                    this.reserveWeb = result.getInt( "reserve_web" );
                    this.emptyMethod = result.getInt( "empty_method" );
                    this.hotelLat = result.getString( "hotel_lat" );
                    this.hotelLon = result.getString( "hotel_lon" );
                    this.hotelLatNum = result.getInt( "hotel_lat_num" );
                    this.hotelLonNum = result.getInt( "hotel_lon_num" );
                    this.dispLat = result.getString( "disp_lat" );
                    this.dispLon = result.getString( "disp_lon" );
                    this.zoom = result.getInt( "zoom" );
                    this.over18Flag = result.getInt( "over18_flag" );
                    this.companyType = result.getInt( "company_type" );
                    this.emptyKind = result.getInt( "empty_kind" );
                    this.emptyStatus = result.getInt( "empty_status" );
                    this.groupCode = result.getInt( "group_code" );
                    this.hotelPicturePc = result.getBytes( "hotel_picture_pc" );
                    this.hotelPictureGif = result.getBytes( "hotel_picture_gif" );
                    this.hotelPicturePng = result.getBytes( "hotel_picture_png" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.prRoom = result.getString( "pr_room" );
                    this.prEtc = result.getString( "pr_etc" );
                    this.renewalFlag = result.getInt( "renewal_flag" );
                    this.urlSpecial = result.getString( "url_special" );
                    this.hotelLatJp = result.getString( "hotel_lat_jp" );
                    this.hotelLonJp = result.getString( "hotel_lon_jp" );
                    this.mapCode = result.getString( "map_code" );
                    this.highRoof = result.getInt( "high_roof" );
                    this.highRoofCount = result.getInt( "high_roof_count" );
                    this.emptyHotenaviId = result.getString( "empty_hotenavi_id" );
                    this.attentionFlag = result.getInt( "attention_flag" );
                    this.adPrefId = result.getInt( "ad_pref_id" );
                    this.renewalDateText = result.getString( "renewal_date_text" );
                    this.newOpenSearchFlag = result.getInt( "new_open_search_flag" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBasic.getDataEx] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテルデータ設定（画像あり）
     * 
     * @param result ホテルデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setDataEx(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.rank = result.getInt( "rank" );
                this.kind = result.getInt( "kind" );
                this.hotenaviId = result.getString( "hotenavi_id" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
                this.nameMobile = result.getString( "name_mobile" );
                this.zipCode = result.getString( "zip_code" );
                this.jisCode = result.getInt( "jis_code" );
                this.prefId = result.getInt( "pref_id" );
                this.prefName = result.getString( "pref_name" );
                this.prefKana = result.getString( "pref_kana" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.address3 = result.getString( "address3" );
                this.addressAll = result.getString( "address_all" );
                this.tel1 = result.getString( "tel1" );
                this.tel2 = result.getString( "tel2" );
                this.fax = result.getString( "fax" );
                this.chargeLast = result.getString( "charge_last" );
                this.chargeFirst = result.getString( "charge_first" );
                this.chargeKanaLast = result.getString( "charge_kana_last" );
                this.chargeKanaFirst = result.getString( "charge_kana_first" );
                this.chargeTel = result.getString( "charge_tel" );
                this.chargeMail = result.getString( "charge_mail" );
                this.openDate = result.getInt( "open_date" );
                this.renewalDate = result.getInt( "renewal_date" );
                this.url = result.getString( "url" );
                this.urlOfficial1 = result.getString( "url_official1" );
                this.urlOfficial2 = result.getString( "url_official2" );
                this.urlOfficialMobile = result.getString( "url_official_mobile" );
                this.pr = result.getString( "pr" );
                this.prDetail = result.getString( "pr_detail" );
                this.prEvent = result.getString( "pr_event" );
                this.prMember = result.getString( "pr_member" );
                this.access = result.getString( "access" );
                this.accessStation = result.getString( "access_station" );
                this.accessIc = result.getString( "access_ic" );
                this.roomCount = result.getInt( "room_count" );
                this.parking = result.getInt( "parking" );
                this.parkingCount = result.getInt( "parking_count" );
                this.typeBuilding = result.getInt( "type_building" );
                this.typeKodate = result.getInt( "type_kodate" );
                this.typeRentou = result.getInt( "type_rentou" );
                this.typeEtc = result.getString( "type_etc" );
                this.locationStation = result.getInt( "location_station" );
                this.locationIc = result.getInt( "location_ic" );
                this.locationKougai = result.getInt( "location_kougai" );
                this.benefit = result.getInt( "benefit" );
                this.roomService = result.getInt( "roomservice" );
                this.payFront = result.getInt( "pay_front" );
                this.payAuto = result.getInt( "pay_auto" );
                this.credit = result.getInt( "credit" );
                this.creditVisa = result.getInt( "credit_visa" );
                this.creditMaster = result.getInt( "credit_master" );
                this.creditJcb = result.getInt( "credit_jcb" );
                this.creditDc = result.getInt( "credit_dc" );
                this.creditNicos = result.getInt( "credit_nicos" );
                this.creditAmex = result.getInt( "credit_amex" );
                this.creditEtc = result.getString( "credit_etc" );
                this.halfway = result.getInt( "halfway" );
                this.coupon = result.getInt( "coupon" );
                this.possibleOne = result.getInt( "possible_one" );
                this.possibleThree = result.getInt( "possible_three" );
                this.reserve = result.getInt( "reserve" );
                this.reserveTel = result.getInt( "reserve_tel" );
                this.reserveMail = result.getInt( "reserve_mail" );
                this.reserveWeb = result.getInt( "reserve_web" );
                this.emptyMethod = result.getInt( "empty_method" );
                this.hotelLat = result.getString( "hotel_lat" );
                this.hotelLon = result.getString( "hotel_lon" );
                this.hotelLatNum = result.getInt( "hotel_lat_num" );
                this.hotelLonNum = result.getInt( "hotel_lon_num" );
                this.dispLat = result.getString( "disp_lat" );
                this.dispLon = result.getString( "disp_lon" );
                this.zoom = result.getInt( "zoom" );
                this.over18Flag = result.getInt( "over18_flag" );
                this.companyType = result.getInt( "company_type" );
                this.emptyKind = result.getInt( "empty_kind" );
                this.emptyStatus = result.getInt( "empty_status" );
                this.groupCode = result.getInt( "group_code" );
                this.hotelPicturePc = result.getBytes( "hotel_picture_pc" );
                this.hotelPictureGif = result.getBytes( "hotel_picture_gif" );
                this.hotelPicturePng = result.getBytes( "hotel_picture_png" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.prRoom = result.getString( "pr_room" );
                this.prEtc = result.getString( "pr_etc" );
                this.renewalFlag = result.getInt( "renewal_flag" );
                this.urlSpecial = result.getString( "url_special" );
                this.hotelLatJp = result.getString( "hotel_lat_jp" );
                this.hotelLonJp = result.getString( "hotel_lon_jp" );
                this.mapCode = result.getString( "map_code" );
                this.highRoof = result.getInt( "high_roof" );
                this.highRoofCount = result.getInt( "high_roof_count" );
                this.emptyHotenaviId = result.getString( "empty_hotenavi_id" );
                this.attentionFlag = result.getInt( "attention_flag" );
                this.adPrefId = result.getInt( "ad_pref_id" );
                this.renewalDateText = result.getString( "renewal_date_text" );
                this.newOpenSearchFlag = result.getInt( "new_open_search_flag" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBasic.setDataEx] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ホテルデータ設定
     * 
     * @param result ホテルデータレコード
     * @param headStr 別名の頭文字
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result, String headStr)
    {
        try
        {
            this.id = result.getInt( headStr + "id" );
            this.rank = result.getInt( headStr + "rank" );
            this.kind = result.getInt( headStr + "kind" );
            this.hotenaviId = result.getString( headStr + "hotenavi_id" );
            this.name = result.getString( headStr + "name" );
            this.nameKana = result.getString( headStr + "name_kana" );
            this.nameMobile = result.getString( headStr + "name_mobile" );
            this.zipCode = result.getString( headStr + "zip_code" );
            this.jisCode = result.getInt( headStr + "jis_code" );
            this.prefId = result.getInt( headStr + "pref_id" );
            this.prefName = result.getString( headStr + "pref_name" );
            this.prefKana = result.getString( headStr + "pref_kana" );
            this.address1 = result.getString( headStr + "address1" );
            this.address2 = result.getString( headStr + "address2" );
            this.address3 = result.getString( headStr + "address3" );
            this.addressAll = result.getString( headStr + "address_all" );
            this.tel1 = result.getString( headStr + "tel1" );
            this.tel2 = result.getString( headStr + "tel2" );
            this.fax = result.getString( headStr + "fax" );
            this.chargeLast = result.getString( headStr + "charge_last" );
            this.chargeFirst = result.getString( headStr + "charge_first" );
            this.chargeKanaLast = result.getString( headStr + "charge_kana_last" );
            this.chargeKanaFirst = result.getString( headStr + "charge_kana_first" );
            this.chargeTel = result.getString( headStr + "charge_tel" );
            this.chargeMail = result.getString( headStr + "charge_mail" );
            this.openDate = result.getInt( headStr + "open_date" );
            this.renewalDate = result.getInt( headStr + "renewal_date" );
            this.url = result.getString( headStr + "url" );
            this.urlOfficial1 = result.getString( headStr + "url_official1" );
            this.urlOfficial2 = result.getString( headStr + "url_official2" );
            this.urlOfficialMobile = result.getString( headStr + "url_official_mobile" );
            this.pr = result.getString( headStr + "pr" );
            this.prDetail = result.getString( headStr + "pr_detail" );
            this.prEvent = result.getString( headStr + "pr_event" );
            this.prMember = result.getString( headStr + "pr_member" );
            this.access = result.getString( headStr + "access" );
            this.accessStation = result.getString( headStr + "access_station" );
            this.accessIc = result.getString( headStr + "access_ic" );
            this.roomCount = result.getInt( headStr + "room_count" );
            this.parking = result.getInt( headStr + "parking" );
            this.parkingCount = result.getInt( headStr + "parking_count" );
            this.typeBuilding = result.getInt( headStr + "type_building" );
            this.typeKodate = result.getInt( headStr + "type_kodate" );
            this.typeRentou = result.getInt( headStr + "type_rentou" );
            this.typeEtc = result.getString( headStr + "type_etc" );
            this.locationStation = result.getInt( headStr + "location_station" );
            this.locationIc = result.getInt( headStr + "location_ic" );
            this.locationKougai = result.getInt( headStr + "location_kougai" );
            this.benefit = result.getInt( headStr + "benefit" );
            this.roomService = result.getInt( headStr + "roomservice" );
            this.payFront = result.getInt( headStr + "pay_front" );
            this.payAuto = result.getInt( headStr + "pay_auto" );
            this.credit = result.getInt( headStr + "credit" );
            this.creditVisa = result.getInt( headStr + "credit_visa" );
            this.creditMaster = result.getInt( headStr + "credit_master" );
            this.creditJcb = result.getInt( headStr + "credit_jcb" );
            this.creditDc = result.getInt( headStr + "credit_dc" );
            this.creditNicos = result.getInt( headStr + "credit_nicos" );
            this.creditAmex = result.getInt( headStr + "credit_amex" );
            this.creditEtc = result.getString( headStr + "credit_etc" );
            this.halfway = result.getInt( headStr + "halfway" );
            this.coupon = result.getInt( headStr + "coupon" );
            this.possibleOne = result.getInt( headStr + "possible_one" );
            this.possibleThree = result.getInt( headStr + "possible_three" );
            this.reserve = result.getInt( headStr + "reserve" );
            this.reserveTel = result.getInt( headStr + "reserve_tel" );
            this.reserveMail = result.getInt( headStr + "reserve_mail" );
            this.reserveWeb = result.getInt( headStr + "reserve_web" );
            this.emptyMethod = result.getInt( headStr + "empty_method" );
            this.hotelLat = result.getString( headStr + "hotel_lat" );
            this.hotelLon = result.getString( headStr + "hotel_lon" );
            this.hotelLatNum = result.getInt( headStr + "hotel_lat_num" );
            this.hotelLonNum = result.getInt( headStr + "hotel_lon_num" );
            this.dispLat = result.getString( headStr + "disp_lat" );
            this.dispLon = result.getString( headStr + "disp_lon" );
            this.zoom = result.getInt( headStr + "zoom" );
            this.over18Flag = result.getInt( headStr + "over18_flag" );
            this.companyType = result.getInt( headStr + "company_type" );
            this.emptyKind = result.getInt( headStr + "empty_kind" );
            this.emptyStatus = result.getInt( headStr + "empty_status" );
            this.groupCode = result.getInt( headStr + "group_code" );
            // this.hotelPicturePc = result.getBytes(appendString + "hotel_picture_pc");
            // this.hotelPictureGif = result.getBytes(appendString + "hotel_picture_gif");
            // this.hotelPicturePng = result.getBytes(appendString + "hotel_picture_png");
            this.lastUpdate = result.getInt( headStr + "last_update" );
            this.lastUptime = result.getInt( headStr + "last_uptime" );
            this.prRoom = result.getString( headStr + "pr_room" );
            this.prEtc = result.getString( headStr + "pr_etc" );
            this.renewalFlag = result.getInt( headStr + "renewal_flag" );
            this.urlSpecial = result.getString( headStr + "url_special" );
            this.hotelLatJp = result.getString( headStr + "hotel_lat_jp" );
            this.hotelLonJp = result.getString( headStr + "hotel_lon_jp" );
            this.mapCode = result.getString( headStr + "map_code" );
            this.highRoof = result.getInt( headStr + "high_roof" );
            this.highRoofCount = result.getInt( headStr + "high_roof_count" );
            this.emptyHotenaviId = result.getString( headStr + "empty_hotenavi_id" );
            this.attentionFlag = result.getInt( headStr + "attention_flag" );
            this.adPrefId = result.getInt( headStr + "ad_pref_id" );
            this.renewalDateText = result.getString( headStr + "renewal_date_text" );
            this.newOpenSearchFlag = result.getInt( headStr + "new_open_search_flag" );
        }
        catch ( Exception e )
        {
            Logging.error( headStr + "[DataHotelBasic.setDataEx] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ホテル基本情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;

        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT hh_hotel_basic SET ";
        query = query + " id = ?,";
        query = query + " rank = ?,";
        query = query + " kind = ?,";
        query = query + " hotenavi_id = ?,";
        query = query + " name = ?,";
        query = query + " name_kana = ?,";
        query = query + " name_mobile = ?,";
        query = query + " zip_code = ?,";
        query = query + " jis_code = ?,";
        query = query + " pref_id = ?,";
        query = query + " pref_name = ?,";
        query = query + " pref_kana = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " address3 = ?,";
        query = query + " address_all = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " fax = ?,";
        query = query + " charge_last = ?,";
        query = query + " charge_first = ?,";
        query = query + " charge_kana_last = ?,";
        query = query + " charge_kana_first = ?,";
        query = query + " charge_tel = ?,";
        query = query + " charge_mail = ?,";
        query = query + " open_date = ?,";
        query = query + " renewal_date = ?,";
        query = query + " url = ?,";
        query = query + " url_official1 = ?,";
        query = query + " url_official2 = ?,";
        query = query + " url_official_mobile = ?,";
        query = query + " pr = ?,";
        query = query + " pr_detail = ?,";
        query = query + " pr_event = ?,";
        query = query + " pr_member = ?,";
        query = query + " access = ?,";
        query = query + " access_station = ?,";
        query = query + " access_ic = ?,";
        query = query + " room_count = ?,";
        query = query + " parking = ?,";
        query = query + " parking_count = ?,";
        query = query + " type_building = ?,";
        query = query + " type_kodate = ?,";
        query = query + " type_rentou = ?,";
        query = query + " type_etc = ?,";
        query = query + " location_station = ?,";
        query = query + " location_ic = ?,";
        query = query + " location_kougai = ?,";
        query = query + " benefit = ?,";
        query = query + " roomservice = ?,";
        query = query + " pay_front = ?,";
        query = query + " pay_auto = ?,";
        query = query + " credit = ?,";
        query = query + " credit_visa = ?,";
        query = query + " credit_master = ?,";
        query = query + " credit_jcb = ?,";
        query = query + " credit_dc = ?,";
        query = query + " credit_nicos = ?,";
        query = query + " credit_amex = ?,";
        query = query + " credit_etc = ?,";
        query = query + " halfway = ?,";
        query = query + " coupon = ?,";
        query = query + " possible_one = ?,";
        query = query + " possible_three = ?,";
        query = query + " reserve = ?,";
        query = query + " reserve_tel = ?,";
        query = query + " reserve_mail = ?,";
        query = query + " reserve_web = ?,";
        query = query + " empty_method = ?,";
        query = query + " hotel_lat = ?,";
        query = query + " hotel_lon = ?,";
        query = query + " hotel_lat_num = ?,";
        query = query + " hotel_lon_num = ?,";
        query = query + " disp_lat = ?,";
        query = query + " disp_lon = ?,";
        query = query + " zoom = ?,";
        query = query + " over18_flag = ?,";
        query = query + " company_type = ?,";
        // query = query + " empty_kind = ?,";
        // query = query + " empty_status = ?,";
        query = query + " group_code = ?,";
        query = query + " hotel_picture_pc = ?,";
        query = query + " hotel_picture_gif = ?,";
        query = query + " hotel_picture_png = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " pr_room = ?,";
        query = query + " pr_etc = ?,";
        query = query + " renewal_flag = ?,";
        query = query + " url_special = ?,";
        query = query + " hotel_lat_jp = ?,";
        query = query + " hotel_lon_jp = ?,";
        query = query + " map_code = ?,";
        query = query + " high_roof = ?,";
        query = query + " high_roof_count = ?,";
        query = query + " empty_hotenavi_id = ?,";
        query = query + " attention_flag = ?,";
        query = query + " ad_pref_id = ?,";
        query = query + " renewal_date_text = ?, ";
        query = query + " new_open_search_flag = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.rank );
            prestate.setInt( 3, this.kind );
            prestate.setString( 4, this.hotenaviId );
            prestate.setString( 5, this.name );
            prestate.setString( 6, this.nameKana );
            prestate.setString( 7, this.nameMobile );
            prestate.setString( 8, this.zipCode );
            prestate.setInt( 9, this.jisCode );
            prestate.setInt( 10, this.prefId );
            prestate.setString( 11, this.prefName );
            prestate.setString( 12, this.prefKana );
            prestate.setString( 13, this.address1 );
            prestate.setString( 14, this.address2 );
            prestate.setString( 15, this.address3 );
            prestate.setString( 16, this.addressAll );
            prestate.setString( 17, this.tel1 );
            prestate.setString( 18, this.tel2 );
            prestate.setString( 19, this.fax );
            prestate.setString( 20, this.chargeLast );
            prestate.setString( 21, this.chargeFirst );
            prestate.setString( 22, this.chargeKanaLast );
            prestate.setString( 23, this.chargeKanaFirst );
            prestate.setString( 24, this.chargeTel );
            prestate.setString( 25, this.chargeMail );
            prestate.setInt( 26, this.openDate );
            prestate.setInt( 27, this.renewalDate );
            prestate.setString( 28, this.url );
            prestate.setString( 29, this.urlOfficial1 );
            prestate.setString( 30, this.urlOfficial2 );
            prestate.setString( 31, this.urlOfficialMobile );
            prestate.setString( 32, this.pr );
            prestate.setString( 33, this.prDetail );
            prestate.setString( 34, this.prEvent );
            prestate.setString( 35, this.prMember );
            prestate.setString( 36, this.access );
            prestate.setString( 37, this.accessStation );
            prestate.setString( 38, this.accessIc );
            prestate.setInt( 39, this.roomCount );
            prestate.setInt( 40, this.parking );
            prestate.setInt( 41, this.parkingCount );
            prestate.setInt( 42, this.typeBuilding );
            prestate.setInt( 43, this.typeKodate );
            prestate.setInt( 44, this.typeRentou );
            prestate.setString( 45, this.typeEtc );
            prestate.setInt( 46, this.locationStation );
            prestate.setInt( 47, this.locationIc );
            prestate.setInt( 48, this.locationKougai );
            prestate.setInt( 49, this.benefit );
            prestate.setInt( 50, this.roomService );
            prestate.setInt( 51, this.payFront );
            prestate.setInt( 52, this.payAuto );
            prestate.setInt( 53, this.credit );
            prestate.setInt( 54, this.creditVisa );
            prestate.setInt( 55, this.creditMaster );
            prestate.setInt( 56, this.creditJcb );
            prestate.setInt( 57, this.creditDc );
            prestate.setInt( 58, this.creditNicos );
            prestate.setInt( 59, this.creditAmex );
            prestate.setString( 60, this.creditEtc );
            prestate.setInt( 61, this.halfway );
            prestate.setInt( 62, this.coupon );
            prestate.setInt( 63, this.possibleOne );
            prestate.setInt( 64, this.possibleThree );
            prestate.setInt( 65, this.reserve );
            prestate.setInt( 66, this.reserveTel );
            prestate.setInt( 67, this.reserveMail );
            prestate.setInt( 68, this.reserveWeb );
            prestate.setInt( 69, this.emptyMethod );
            prestate.setString( 70, this.hotelLat );
            prestate.setString( 71, this.hotelLon );
            prestate.setInt( 72, this.hotelLatNum );
            prestate.setInt( 73, this.hotelLonNum );
            prestate.setString( 74, this.dispLat );
            prestate.setString( 75, this.dispLon );
            prestate.setInt( 76, this.zoom );
            prestate.setInt( 77, this.over18Flag );
            prestate.setInt( 78, this.companyType );
            // prestate.setInt( 79, this.emptyKind );
            // prestate.setInt( 80, this.emptyStatus );
            prestate.setInt( 79, this.groupCode );
            prestate.setBytes( 80, this.hotelPicturePc );
            prestate.setBytes( 81, this.hotelPictureGif );
            prestate.setBytes( 82, this.hotelPicturePng );
            prestate.setInt( 83, this.lastUpdate );
            prestate.setInt( 84, this.lastUptime );
            prestate.setString( 85, this.prRoom );
            prestate.setString( 86, this.prEtc );
            prestate.setInt( 87, this.renewalFlag );
            prestate.setString( 88, this.urlSpecial );
            prestate.setString( 89, this.hotelLatJp );
            prestate.setString( 90, this.hotelLonJp );
            prestate.setString( 91, this.mapCode );
            prestate.setInt( 92, this.highRoof );
            prestate.setInt( 93, this.highRoofCount );
            prestate.setString( 94, this.emptyHotenaviId );
            prestate.setInt( 95, this.attentionFlag );
            prestate.setInt( 96, this.adPrefId );
            prestate.setString( 97, this.renewalDateText );
            prestate.setInt( 98, this.newOpenSearchFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBasic.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ホテル情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int id)
    {
        int result;
        boolean ret;
        String query;

        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;
        query = "UPDATE hh_hotel_basic SET ";
        query = query + " rank = ?,";
        query = query + " kind = ?,";
        query = query + " hotenavi_id = ?,";
        query = query + " name = ?,";
        query = query + " name_kana = ?,";
        query = query + " name_mobile = ?,";
        query = query + " zip_code = ?,";
        query = query + " jis_code = ?,";
        query = query + " pref_id = ?,";
        query = query + " pref_name = ?,";
        query = query + " pref_kana = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " address3 = ?,";
        query = query + " address_all = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " fax = ?,";
        query = query + " charge_last = ?,";
        query = query + " charge_first = ?,";
        query = query + " charge_kana_last = ?,";
        query = query + " charge_kana_first = ?,";
        query = query + " charge_tel = ?,";
        query = query + " charge_mail = ?,";
        query = query + " open_date = ?,";
        query = query + " renewal_date = ?,";
        query = query + " url = ?,";
        query = query + " url_official1 = ?,";
        query = query + " url_official2 = ?,";
        query = query + " url_official_mobile = ?,";
        query = query + " pr = ?,";
        query = query + " pr_detail = ?,";
        query = query + " pr_event = ?,";
        query = query + " pr_member = ?,";
        query = query + " access = ?,";
        query = query + " access_station = ?,";
        query = query + " access_ic = ?,";
        query = query + " room_count = ?,";
        query = query + " parking = ?,";
        query = query + " parking_count = ?,";
        query = query + " type_building = ?,";
        query = query + " type_kodate = ?,";
        query = query + " type_rentou = ?,";
        query = query + " type_etc = ?,";
        query = query + " location_station = ?,";
        query = query + " location_ic = ?,";
        query = query + " location_kougai = ?,";
        query = query + " benefit = ?,";
        query = query + " roomservice = ?,";
        query = query + " pay_front = ?,";
        query = query + " pay_auto = ?,";
        query = query + " credit = ?,";
        query = query + " credit_visa = ?,";
        query = query + " credit_master = ?,";
        query = query + " credit_jcb = ?,";
        query = query + " credit_dc = ?,";
        query = query + " credit_nicos = ?,";
        query = query + " credit_amex = ?,";
        query = query + " credit_etc = ?,";
        query = query + " halfway = ?,";
        query = query + " coupon = ?,";
        query = query + " possible_one = ?,";
        query = query + " possible_three = ?,";
        query = query + " reserve = ?,";
        query = query + " reserve_tel = ?,";
        query = query + " reserve_mail = ?,";
        query = query + " reserve_web = ?,";
        query = query + " empty_method = ?,";
        query = query + " hotel_lat = ?,";
        query = query + " hotel_lon = ?,";
        query = query + " hotel_lat_num = ?,";
        query = query + " hotel_lon_num = ?,";
        query = query + " disp_lat = ?,";
        query = query + " disp_lon = ?,";
        query = query + " zoom = ?,";
        query = query + " over18_flag = ?,";
        query = query + " company_type = ?,";
        // query = query + " empty_kind = ?,";
        // query = query + " empty_status = ?,";
        query = query + " group_code = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " pr_room = ?,";
        query = query + " pr_etc = ?,";
        query = query + " renewal_flag = ?,";
        query = query + " url_special = ?,";
        query = query + " hotel_lat_jp = ?,";
        query = query + " hotel_lon_jp = ?,";
        query = query + " map_code = ?,";
        query = query + " high_roof = ?,";
        query = query + " high_roof_count = ?,";
        query = query + " empty_hotenavi_id = ?,";
        query = query + " attention_flag = ?,";
        query = query + " ad_pref_id = ?, ";
        query = query + " renewal_date_text = ?, ";
        query = query + " new_open_search_flag = ? ";
        query = query + " WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.rank );
            prestate.setInt( 2, this.kind );
            prestate.setString( 3, this.hotenaviId );
            prestate.setString( 4, this.name );
            prestate.setString( 5, this.nameKana );
            prestate.setString( 6, this.nameMobile );
            prestate.setString( 7, this.zipCode );
            prestate.setInt( 8, this.jisCode );
            prestate.setInt( 9, this.prefId );
            prestate.setString( 10, this.prefName );
            prestate.setString( 11, this.prefKana );
            prestate.setString( 12, this.address1 );
            prestate.setString( 13, this.address2 );
            prestate.setString( 14, this.address3 );
            prestate.setString( 15, this.addressAll );
            prestate.setString( 16, this.tel1 );
            prestate.setString( 17, this.tel2 );
            prestate.setString( 18, this.fax );
            prestate.setString( 19, this.chargeLast );
            prestate.setString( 20, this.chargeFirst );
            prestate.setString( 21, this.chargeKanaLast );
            prestate.setString( 22, this.chargeKanaFirst );
            prestate.setString( 23, this.chargeTel );
            prestate.setString( 24, this.chargeMail );
            prestate.setInt( 25, this.openDate );
            prestate.setInt( 26, this.renewalDate );
            prestate.setString( 27, this.url );
            prestate.setString( 28, this.urlOfficial1 );
            prestate.setString( 29, this.urlOfficial2 );
            prestate.setString( 30, this.urlOfficialMobile );
            prestate.setString( 31, this.pr );
            prestate.setString( 32, this.prDetail );
            prestate.setString( 33, this.prEvent );
            prestate.setString( 34, this.prMember );
            prestate.setString( 35, this.access );
            prestate.setString( 36, this.accessStation );
            prestate.setString( 37, this.accessIc );
            prestate.setInt( 38, this.roomCount );
            prestate.setInt( 39, this.parking );
            prestate.setInt( 40, this.parkingCount );
            prestate.setInt( 41, this.typeBuilding );
            prestate.setInt( 42, this.typeKodate );
            prestate.setInt( 43, this.typeRentou );
            prestate.setString( 44, this.typeEtc );
            prestate.setInt( 45, this.locationStation );
            prestate.setInt( 46, this.locationIc );
            prestate.setInt( 47, this.locationKougai );
            prestate.setInt( 48, this.benefit );
            prestate.setInt( 49, this.roomService );
            prestate.setInt( 50, this.payFront );
            prestate.setInt( 51, this.payAuto );
            prestate.setInt( 52, this.credit );
            prestate.setInt( 53, this.creditVisa );
            prestate.setInt( 54, this.creditMaster );
            prestate.setInt( 55, this.creditJcb );
            prestate.setInt( 56, this.creditDc );
            prestate.setInt( 57, this.creditNicos );
            prestate.setInt( 58, this.creditAmex );
            prestate.setString( 59, this.creditEtc );
            prestate.setInt( 60, this.halfway );
            prestate.setInt( 61, this.coupon );
            prestate.setInt( 62, this.possibleOne );
            prestate.setInt( 63, this.possibleThree );
            prestate.setInt( 64, this.reserve );
            prestate.setInt( 65, this.reserveTel );
            prestate.setInt( 66, this.reserveMail );
            prestate.setInt( 67, this.reserveWeb );
            prestate.setInt( 68, this.emptyMethod );
            prestate.setString( 69, this.hotelLat );
            prestate.setString( 70, this.hotelLon );
            prestate.setInt( 71, this.hotelLatNum );
            prestate.setInt( 72, this.hotelLonNum );
            prestate.setString( 73, this.dispLat );
            prestate.setString( 74, this.dispLon );
            prestate.setInt( 75, this.zoom );
            prestate.setInt( 76, this.over18Flag );
            prestate.setInt( 77, this.companyType );
            // prestate.setInt( 78, this.emptyKind );
            // prestate.setInt( 79, this.emptyStatus );
            prestate.setInt( 78, this.groupCode );
            prestate.setInt( 79, this.lastUpdate );
            prestate.setInt( 80, this.lastUptime );
            prestate.setString( 81, this.prRoom );
            prestate.setString( 82, this.prEtc );
            prestate.setInt( 83, this.renewalFlag );
            prestate.setString( 84, this.urlSpecial );
            prestate.setString( 85, this.hotelLatJp );
            prestate.setString( 86, this.hotelLonJp );
            prestate.setString( 87, this.mapCode );
            prestate.setInt( 88, this.highRoof );
            prestate.setInt( 89, this.highRoofCount );
            prestate.setString( 90, this.emptyHotenaviId );
            prestate.setInt( 91, this.attentionFlag );
            prestate.setInt( 92, this.adPrefId );
            prestate.setString( 93, this.renewalDateText );
            prestate.setInt( 94, this.newOpenSearchFlag );
            prestate.setInt( 95, id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBasic.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ホテル情報データ更新（画像更新あり）
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateDataEx(int id)
    {
        int result;
        boolean ret;
        String query;

        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;
        query = "UPDATE hh_hotel_basic SET ";
        query = query + " rank = ?,";
        query = query + " kind = ?,";
        query = query + " hotenavi_id = ?,";
        query = query + " name = ?,";
        query = query + " name_kana = ?,";
        query = query + " name_mobile = ?,";
        query = query + " zip_code = ?,";
        query = query + " jis_code = ?,";
        query = query + " pref_id = ?,";
        query = query + " pref_name = ?,";
        query = query + " pref_kana = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " address3 = ?,";
        query = query + " address_all = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " fax = ?,";
        query = query + " charge_last = ?,";
        query = query + " charge_first = ?,";
        query = query + " charge_kana_last = ?,";
        query = query + " charge_kana_first = ?,";
        query = query + " charge_tel = ?,";
        query = query + " charge_mail = ?,";
        query = query + " open_date = ?,";
        query = query + " renewal_date = ?,";
        query = query + " url = ?,";
        query = query + " url_official1 = ?,";
        query = query + " url_official2 = ?,";
        query = query + " url_official_mobile = ?,";
        query = query + " pr = ?,";
        query = query + " pr_detail = ?,";
        query = query + " pr_event = ?,";
        query = query + " pr_member = ?,";
        query = query + " access = ?,";
        query = query + " access_station = ?,";
        query = query + " access_ic = ?,";
        query = query + " room_count = ?,";
        query = query + " parking = ?,";
        query = query + " parking_count = ?,";
        query = query + " type_building = ?,";
        query = query + " type_kodate = ?,";
        query = query + " type_rentou = ?,";
        query = query + " type_etc = ?,";
        query = query + " location_station = ?,";
        query = query + " location_ic = ?,";
        query = query + " location_kougai = ?,";
        query = query + " benefit = ?,";
        query = query + " roomservice = ?,";
        query = query + " pay_front = ?,";
        query = query + " pay_auto = ?,";
        query = query + " credit = ?,";
        query = query + " credit_visa = ?,";
        query = query + " credit_master = ?,";
        query = query + " credit_jcb = ?,";
        query = query + " credit_dc = ?,";
        query = query + " credit_nicos = ?,";
        query = query + " credit_amex = ?,";
        query = query + " credit_etc = ?,";
        query = query + " halfway = ?,";
        query = query + " coupon = ?,";
        query = query + " possible_one = ?,";
        query = query + " possible_three = ?,";
        query = query + " reserve = ?,";
        query = query + " reserve_tel = ?,";
        query = query + " reserve_mail = ?,";
        query = query + " reserve_web = ?,";
        query = query + " empty_method = ?,";
        query = query + " hotel_lat = ?,";
        query = query + " hotel_lon = ?,";
        query = query + " hotel_lat_num = ?,";
        query = query + " hotel_lon_num = ?,";
        query = query + " disp_lat = ?,";
        query = query + " disp_lon = ?,";
        query = query + " zoom = ?,";
        query = query + " over18_flag = ?,";
        query = query + " company_type = ?,";
        // query = query + " empty_kind = ?,";
        // query = query + " empty_status = ?,";
        query = query + " group_code = ?,";
        query = query + " hotel_picture_pc = ?,";
        query = query + " hotel_picture_gif = ?,";
        query = query + " hotel_picture_png = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " pr_room = ?,";
        query = query + " pr_etc = ?,";
        query = query + " renewal_flag = ?,";
        query = query + " url_special = ?,";
        query = query + " hotel_lat_jp = ?,";
        query = query + " hotel_lon_jp = ?,";
        query = query + " map_code = ?,";
        query = query + " high_roof = ?,";
        query = query + " high_roof_count = ?,";
        query = query + " empty_hotenavi_id = ?,";
        query = query + " attention_flag = ?,";
        query = query + " ad_pref_id = ?, ";
        query = query + " renewal_date_text = ?, ";
        query = query + " new_open_search_flag = ? ";
        query = query + " WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.rank );
            prestate.setInt( 2, this.kind );
            prestate.setString( 3, this.hotenaviId );
            prestate.setString( 4, this.name );
            prestate.setString( 5, this.nameKana );
            prestate.setString( 6, this.nameMobile );
            prestate.setString( 7, this.zipCode );
            prestate.setInt( 8, this.jisCode );
            prestate.setInt( 9, this.prefId );
            prestate.setString( 10, this.prefName );
            prestate.setString( 11, this.prefKana );
            prestate.setString( 12, this.address1 );
            prestate.setString( 13, this.address2 );
            prestate.setString( 14, this.address3 );
            prestate.setString( 15, this.addressAll );
            prestate.setString( 16, this.tel1 );
            prestate.setString( 17, this.tel2 );
            prestate.setString( 18, this.fax );
            prestate.setString( 19, this.chargeLast );
            prestate.setString( 20, this.chargeFirst );
            prestate.setString( 21, this.chargeKanaLast );
            prestate.setString( 22, this.chargeKanaFirst );
            prestate.setString( 23, this.chargeTel );
            prestate.setString( 24, this.chargeMail );
            prestate.setInt( 25, this.openDate );
            prestate.setInt( 26, this.renewalDate );
            prestate.setString( 27, this.url );
            prestate.setString( 28, this.urlOfficial1 );
            prestate.setString( 29, this.urlOfficial2 );
            prestate.setString( 30, this.urlOfficialMobile );
            prestate.setString( 31, this.pr );
            prestate.setString( 32, this.prDetail );
            prestate.setString( 33, this.prEvent );
            prestate.setString( 34, this.prMember );
            prestate.setString( 35, this.access );
            prestate.setString( 36, this.accessStation );
            prestate.setString( 37, this.accessIc );
            prestate.setInt( 38, this.roomCount );
            prestate.setInt( 39, this.parking );
            prestate.setInt( 40, this.parkingCount );
            prestate.setInt( 41, this.typeBuilding );
            prestate.setInt( 42, this.typeKodate );
            prestate.setInt( 43, this.typeRentou );
            prestate.setString( 44, this.typeEtc );
            prestate.setInt( 45, this.locationStation );
            prestate.setInt( 46, this.locationIc );
            prestate.setInt( 47, this.locationKougai );
            prestate.setInt( 48, this.benefit );
            prestate.setInt( 49, this.roomService );
            prestate.setInt( 50, this.payFront );
            prestate.setInt( 51, this.payAuto );
            prestate.setInt( 52, this.credit );
            prestate.setInt( 53, this.creditVisa );
            prestate.setInt( 54, this.creditMaster );
            prestate.setInt( 55, this.creditJcb );
            prestate.setInt( 56, this.creditDc );
            prestate.setInt( 57, this.creditNicos );
            prestate.setInt( 58, this.creditAmex );
            prestate.setString( 59, this.creditEtc );
            prestate.setInt( 60, this.halfway );
            prestate.setInt( 61, this.coupon );
            prestate.setInt( 62, this.possibleOne );
            prestate.setInt( 63, this.possibleThree );
            prestate.setInt( 64, this.reserve );
            prestate.setInt( 65, this.reserveTel );
            prestate.setInt( 66, this.reserveMail );
            prestate.setInt( 67, this.reserveWeb );
            prestate.setInt( 68, this.emptyMethod );
            prestate.setString( 69, this.hotelLat );
            prestate.setString( 70, this.hotelLon );
            prestate.setInt( 71, this.hotelLatNum );
            prestate.setInt( 72, this.hotelLonNum );
            prestate.setString( 73, this.dispLat );
            prestate.setString( 74, this.dispLon );
            prestate.setInt( 75, this.zoom );
            prestate.setInt( 76, this.over18Flag );
            prestate.setInt( 77, this.companyType );
            // prestate.setInt( 78, this.emptyKind );
            // prestate.setInt( 79, this.emptyStatus );
            prestate.setInt( 78, this.groupCode );
            prestate.setBytes( 79, this.hotelPicturePc );
            prestate.setBytes( 80, this.hotelPictureGif );
            prestate.setBytes( 81, this.hotelPicturePng );
            prestate.setInt( 82, this.lastUpdate );
            prestate.setInt( 83, this.lastUptime );
            prestate.setString( 84, this.prRoom );
            prestate.setString( 85, this.prEtc );
            prestate.setInt( 86, this.renewalFlag );
            prestate.setString( 87, this.urlSpecial );
            prestate.setString( 88, this.hotelLatJp );
            prestate.setString( 89, this.hotelLonJp );
            prestate.setString( 90, this.mapCode );
            prestate.setInt( 91, this.highRoof );
            prestate.setInt( 92, this.highRoofCount );
            prestate.setString( 93, this.emptyHotenaviId );
            prestate.setInt( 94, this.attentionFlag );
            prestate.setInt( 95, this.adPrefId );
            prestate.setString( 96, this.renewalDateText );
            prestate.setInt( 97, this.newOpenSearchFlag );
            prestate.setInt( 98, id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBasic.updateDataEx] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ホテル情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateHotelStatus(int id)
    {
        int result;
        boolean ret;
        String query;

        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;
        query = "UPDATE hh_hotel_basic SET ";
        query = query + " empty_kind = ?,";
        query = query + " empty_status = ?";
        query = query + " WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.emptyKind );
            prestate.setInt( 2, this.emptyStatus );
            prestate.setInt( 3, id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBasic.updateHotelStatus] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ホテル情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @staff_id 社員ID（調査用フォームログイン）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateHotel(int id, String staff_id)
    {
        boolean ret;
        String query;

        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        ret = false;
        query = "UPDATE hh_hotel_basic SET ";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " WHERE id = ?";

        if ( query.startsWith( "" ) )
        {

        }
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 3, id );
            int prestateUpdate = prestate.executeUpdate();
            if ( prestateUpdate > 0 )
            {
                ret = true;
            }
            DBConnection.releaseResources( prestate );

            if ( ret )
            {
                int user_id = 0;
                query = "SELECT userid FROM owner_user WHERE hotelid='happyhotel'";
                query = query + " AND  loginid= ?";
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, staff_id );
                result = prestate.executeQuery();
                if ( result.next() )
                {
                    user_id = result.getInt( "userid" );
                }
                DBConnection.releaseResources( result );
                DBConnection.releaseResources( prestate );
                if ( user_id == 0 )
                {
                    if ( CheckString.numCheck( staff_id ) )
                    {
                        user_id = Integer.parseInt( staff_id );
                    }
                }
                query = "INSERT INTO hh_hotel_adjustment_history SET ";
                query = query + "id=?, ";
                query = query + "hotel_id='happyhotel', ";
                query = query + "user_id=?, ";
                query = query + "edit_id=1000, ";
                query = query + "input_date=?, ";
                query = query + "input_time=?, ";
                query = query + "memo='→GCPに情報更新'";
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, id );
                prestate.setInt( 2, user_id );
                prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                prestate.setInt( 4, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                prestate.executeUpdate();
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBasic.updateHotel] Exception=" + e.toString(), "DataHotelBasic" );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ホテル情報取得
     * 
     * @param hotenaviId ホテルナビID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String hotenaviId)
    {
        boolean ret;
        Connection connection = null;
        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            ret = getData( connection, hotenaviId );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBasic.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ホテル情報取得
     * 
     * @param hotenaviId ホテルナビID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(Connection connection, String hotenaviId)
    {
        boolean ret;
        String query;
        String column;
        String[] colArr = COLUMNS.split( "," );
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT ";
        for( int i = 0 ; i < colArr.length ; i++ )
        {
            query += " ";
            if ( i > 0 )
            {
                query += ", ";
            }
            column = colArr[i];
            if ( "empty_kind".equals( column.toLowerCase() ) )
            {
                query += "IFNULL(hh_hotel_status.`mode`, 0) ";
            }
            else if ( "empty_status".equals( column.toLowerCase() ) )
            {
                query += "IFNULL(hh_hotel_status.empty_status, 0) ";
            }
            else
            {
                query += "hh_hotel_basic.";
            }
            query += column;
        }
        query += " FROM hh_hotel_basic";
        query += " LEFT JOIN hh_hotel_status";
        query += " ON hh_hotel_basic.id = hh_hotel_status.id";
        query += " WHERE hh_hotel_basic.hotenavi_id = ?";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotenaviId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    setData( result );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBasic.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }
}
