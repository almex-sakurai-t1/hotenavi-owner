/*
 * @(#)DataHotelBasicHistory.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ホテル基本履歴情報クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * ホテルの基本履歴情報の取得
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/17
 * @version 1.1 2007/11/29
 */
public class DataHotelBasicHistory implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 6579814994784149425L;

    private int               id;
    private int               seq;
    private int               rank;
    private int               kind;
    private String            hotenaviId;
    private String            name;
    private String            nameKana;
    private String            nameMobile;
    private String            zipCode;
    private int               jisCode;
    private int               prefId;
    private String            prefName;
    private String            prefKana;
    private String            address1;
    private String            address2;
    private String            address3;
    private String            addressAll;
    private String            tel1;
    private String            tel2;
    private String            fax;
    private String            chargeLast;
    private String            chargeFirst;
    private String            chargeKanaLast;
    private String            chargeKanaFirst;
    private String            chargeTel;
    private String            chargeMail;
    private int               openDate;
    private int               renewalDate;
    private String            url;
    private String            urlOfficial1;
    private String            urlOfficial2;
    private String            urlOfficialMobile;
    private String            pr;
    private String            prDetail;
    private String            prEvent;
    private String            prMember;
    private String            access;
    private String            accessStation;
    private String            accessIc;
    private int               roomCount;
    private int               parking;
    private int               parkingCount;
    private int               typeBuilding;
    private int               typeKodate;
    private int               typeRentou;
    private String            typeEtc;
    private int               locationStation;
    private int               locationIc;
    private int               locationKougai;
    private int               benefit;
    private int               roomService;
    private int               payFront;
    private int               payAuto;
    private int               credit;
    private int               creditVisa;
    private int               creditMaster;
    private int               creditJcb;
    private int               creditDc;
    private int               creditNicos;
    private int               creditAmex;
    private String            creditEtc;
    private int               halfway;
    private int               coupon;
    private int               possibleOne;
    private int               possibleThree;
    private int               reserve;
    private int               reserveTel;
    private int               reserveMail;
    private int               reserveWeb;
    private int               emptyMethod;
    private String            hotelLat;
    private String            hotelLon;
    private int               hotelLatNum;
    private int               hotelLonNum;
    private String            dispLat;
    private String            dispLon;
    private int               zoom;
    private int               over18Flag;
    private int               companyType;
    private int               emptyKind;
    private int               emptyStatus;
    private int               groupCode;
    private byte[]            hotelPicturePc;
    private byte[]            hotelPictureGif;
    private byte[]            hotelPicturePng;
    private int               lastUpdate;
    private int               lastUptime;
    private int               registDate;
    private int               registTime;

    /**
     * データを初期化します。
     */
    public DataHotelBasicHistory()
    {
        id = 0;
        seq = 0;
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
        lastUpdate = 0;
        lastUptime = 0;
        registDate = 0;
        registTime = 0;
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

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
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

    public int getSeq()
    {
        return seq;
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

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
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

    public void setSeq(int seq)
    {
        this.seq = seq;
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
     * ホテル情報履歴取得
     * 
     * @param hotelId ホテルコード
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_basic_history WHERE id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
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
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBasicHistory.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテル情報履歴データ設定
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
                this.seq = result.getInt( "seq" );
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
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBasicHistory.setData] Exception=" + e.toString() );
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

        query = "INSERT hh_hotel_basic_history SET ";
        query = query + " id = ?,";
        query = query + " seq = 0,";
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
        query = query + " empty_method = ?,";
        query = query + " empty_kind = ?,";
        query = query + " group_code = ?,";
        query = query + " hotel_picture_pc = ?,";
        query = query + " hotel_picture_gif = ?,";
        query = query + " hotel_picture_png = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?";

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
            prestate.setInt( 79, this.emptyKind );
            prestate.setInt( 80, this.emptyStatus );
            prestate.setInt( 81, this.groupCode );
            prestate.setBytes( 82, this.hotelPicturePc );
            prestate.setBytes( 83, this.hotelPictureGif );
            prestate.setBytes( 84, this.hotelPicturePng );
            prestate.setInt( 85, this.lastUpdate );
            prestate.setInt( 86, this.lastUptime );
            prestate.setInt( 87, this.registDate );
            prestate.setInt( 88, this.registTime );

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
     * ホテル情報履歴データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int id, int seq)
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
        query = query + " empty_method = ?,";
        query = query + " empty_kind = ?,";
        query = query + " group_code = ?,";
        query = query + " hotel_picture_pc = ?,";
        query = query + " hotel_picture_gif = ?,";
        query = query + " hotel_picture_png = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?";
        query = query + " WHERE id = ? AND seq = ?";

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
            prestate.setInt( 78, this.emptyKind );
            prestate.setInt( 79, this.emptyStatus );
            prestate.setInt( 80, this.groupCode );
            prestate.setBytes( 81, this.hotelPicturePc );
            prestate.setBytes( 82, this.hotelPictureGif );
            prestate.setBytes( 83, this.hotelPicturePng );
            prestate.setInt( 84, this.lastUpdate );
            prestate.setInt( 85, this.lastUptime );
            prestate.setInt( 86, this.registDate );
            prestate.setInt( 87, this.registTime );
            prestate.setInt( 88, id );
            prestate.setInt( 89, seq );

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
}
