package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 取得クラス
 * 
 * @author sakurai-t1
 * @version 1.00 2016/3/1
 */
public class DataLvjHotelBasic implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = 600752364650037394L;
    public static final String TABLE            = "lvj_hotel_basic";
    private int                hotelId;                               // ホテルID (hh_hotel_basic.id)
    private int                seq;                                   // 管理番号
    private String             name;                                  // ホテル名
    private String             nameKana;                              // ホテル名（日本語カナ）
    private int                rank;                                  // ホテルランク -1:掲載なし ,0:未加盟,1:ライト,2:スタンダード,3:ハピホテマイル
    private String             mapId;                                 // 最寄駅コード カンマ区切りで複数渡す
    private String             zipCode;                               // 郵便番号（ハイフンなし）
    private int                jisCode;                               // 市区町村コード
    private int                prefId;                                // 都道府県コード
    private String             prefName;                              // 都道府県名
    private String             address1;                              // 住所(1)
    private String             address2;                              // 住所(2)
    private String             address3;                              // 住所(3)
    private String             tel;                                   // 電話番号
    private String             fax;                                   // FAX
    private int                roomCount;                             // 部屋数
    private int                halfway;                               // 途中外出　0:未入力,1:可,9:不可
    private String             halfwayMessage;                        // 途中外出詳細 (hh_hotel_remarks disp_no=8)
    private int                possibleOne;                           // 一人利用　0:未入力,1:可,9:不可
    private int                possibleThree;                         // 複数人利用（3人以上）　0:未入力,1:可,9:不可
    private String             hotelLat;                              // ホテル緯度
    private String             hotelLon;                              // ホテル経度
    private int                over18Flag;                            // 18禁フラグ　0:未入力,1:あり,9:なし
    private int                restPriceFrom;                         // 休憩料金（最低）← hh_hotel_price
    private int                restPriceTo;                           // 休憩料金（最高）← hh_hotel_price
    private int                stayPriceFrom;                         // 宿泊料金（最低）← hh_hotel_price
    private int                stayPriceTo;                           // 宿泊料金（最高）← hh_hotel_price
    private int                isWomensUse;                           // 同性利用可（女性）　　0:未入力,1:可,9:不可 (hh_hotel_equip equip_id=73)
    private int                isMensUse;                             // 同性利用可（男性）　　0:未入力,1:可,9:不可　(hh_hotel_equip equip_id=72)
    private int                isFreeWifi;                            // 無料Wi-Fi(無線LAN)　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=54)
    private int                isFreeLan;                             // 有線LAN　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=53)
    private int                isNoSmokingRoom;                       // 禁煙ルーム　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=2)
    private int                isPartyRoom;                           // パーティールーム　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=62)
    private int                isRoomService;                         // ルームサービス　0:未入力,1:あり,9:なし　（hh_hotel_basic.roomservice）
    private int                isCasher;                              // 精算機　0:未入力,1:あり,9:なし (hh_hotel_basic.pay_auto)
    private int                isJetbath;                             // ジェットバス／ブロアバス　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=3)
    private int                isOpenAirBath;                         // 露天風呂　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=11)
    private int                isBedrockBath;                         // 岩盤浴　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=13)
    private int                isSauna;                               // サウナ　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=5)
    private int                isHotSpring;                           // 温泉　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=47)
    private int                isPool;                                // プール　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=48)
    private int                isBathroomTv;                          // 浴室TV　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=9)
    private int                isHugeScreenTv;                        // 大画面テレビ100インチ以上　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=59)
    private int                isSurroundSystem;                      // 5.1chサラウンド　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=23)
    private int                isVod;                                 // ビデオ・オン・デマンド　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=25)
    private int                isOnlineKaraoke;                       // 通信カラオケ　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=35)
    private int                isVideoGame;                           // TVゲーム　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=37)
    private int                isPc;                                  // 客室PC　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=51)
    private int                isTabletPc;                            // 客室タブレット　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=52)
    private int                isInternet;                            // 客室インターネット　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=41)
    private int                isJapaneseRoom;                        // 和室　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=45)
    private int                isLaundry;                             // 洗濯機　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=16)
    private int                isDryningMachine;                      // 乾燥機　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=18)
    private int                isShowerToilet;                        // シャワートイレ　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=34)
    private int                isProjecter;                           // プロジェクター　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=46)
    private int                isBathrobe;                            // バスローブ　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=38)
    private int                isRoomWear;                            // ルームウェア　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=64)
    private int                isWomenCosmetics;                      // 女性化粧品　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=68)
    private int                isVariousShampoo;                      // 各種シャンプー　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=42)
    private int                isCostume;                             // コスチューム　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=44)
    private int                isMobilePhoneCharger;                  // 携帯電話充電器　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=55)
    private int                isAndroidPhoneCharger;                 // Androidスマホ充電器　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=56)
    private int                isIphonePhoneCharger;                  // iPhone充電器　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=57)
    private int                lastUpdate;                            // 最終更新日(YYYYMMDD)
    private int                lastUptime;                            // 最終更新時刻(HHMMSS)
    private int                reflectFlag;                           // 1:反映済み（変更時に古いデータがあれば1にする）
    private int                reflectUpdate;                         // 最終更新日(YYYYMMDD)
    private int                reflectUptime;                         // 最終更新時刻(HHMMSS)

    /**
     * データを初期化します。
     */
    public DataLvjHotelBasic()
    {
        this.hotelId = 0;
        // this.seq = 0;
        this.name = "";
        this.nameKana = "";
        this.rank = 0;
        this.mapId = "";
        this.zipCode = "";
        this.jisCode = 0;
        this.prefId = 0;
        this.prefName = "";
        this.address1 = "";
        this.address2 = "";
        this.address3 = "";
        this.tel = "";
        this.fax = "";
        this.roomCount = 0;
        this.halfway = 0;
        this.halfwayMessage = "";
        this.possibleOne = 0;
        this.possibleThree = 0;
        this.hotelLat = "";
        this.hotelLon = "";
        this.over18Flag = 0;
        this.restPriceFrom = 0;
        this.restPriceTo = 0;
        this.stayPriceFrom = 0;
        this.stayPriceTo = 0;
        this.isWomensUse = 0;
        this.isMensUse = 0;
        this.isFreeWifi = 0;
        this.isFreeLan = 0;
        this.isNoSmokingRoom = 0;
        this.isPartyRoom = 0;
        this.isRoomService = 0;
        this.isCasher = 0;
        this.isJetbath = 0;
        this.isOpenAirBath = 0;
        this.isBedrockBath = 0;
        this.isSauna = 0;
        this.isHotSpring = 0;
        this.isPool = 0;
        this.isBathroomTv = 0;
        this.isHugeScreenTv = 0;
        this.isSurroundSystem = 0;
        this.isVod = 0;
        this.isOnlineKaraoke = 0;
        this.isVideoGame = 0;
        this.isPc = 0;
        this.isTabletPc = 0;
        this.isInternet = 0;
        this.isJapaneseRoom = 0;
        this.isLaundry = 0;
        this.isDryningMachine = 0;
        this.isShowerToilet = 0;
        this.isProjecter = 0;
        this.isBathrobe = 0;
        this.isRoomWear = 0;
        this.isWomenCosmetics = 0;
        this.isVariousShampoo = 0;
        this.isCostume = 0;
        this.isMobilePhoneCharger = 0;
        this.isAndroidPhoneCharger = 0;
        this.isIphonePhoneCharger = 0;
        this.lastUpdate = 0;
        this.lastUptime = 0;
        this.reflectFlag = 0;
    }

    public int getHotelId()
    {
        return hotelId;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public int getRank()
    {
        return rank;
    }

    public String getMapId()
    {
        return mapId;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public int getJisCode()
    {
        return jisCode;
    }

    public int getPrefId()
    {
        return prefId;
    }

    public String getPrefName()
    {
        return prefName;
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

    public String getTel()
    {
        return tel;
    }

    public String getFax()
    {
        return fax;
    }

    public int getRoomCount()
    {
        return roomCount;
    }

    public int getHalfway()
    {
        return halfway;
    }

    public String getHalfwayMessage()
    {
        return halfwayMessage;
    }

    public int getPossibleOne()
    {
        return possibleOne;
    }

    public int getPossibleThree()
    {
        return possibleThree;
    }

    public String getHotelLat()
    {
        return hotelLat;
    }

    public String getHotelLon()
    {
        return hotelLon;
    }

    public int getOver18Flag()
    {
        return over18Flag;
    }

    public int getRestPriceFrom()
    {
        return restPriceFrom;
    }

    public int getRestPriceTo()
    {
        return restPriceTo;
    }

    public int getStayPriceFrom()
    {
        return stayPriceFrom;
    }

    public int getStayPriceTo()
    {
        return stayPriceTo;
    }

    public int getIsWomensUse()
    {
        return isWomensUse;
    }

    public int getIsMensUse()
    {
        return isMensUse;
    }

    public int getIsFreeWifi()
    {
        return isFreeWifi;
    }

    public int getIsFreeLan()
    {
        return isFreeLan;
    }

    public int getIsNoSmokingRoom()
    {
        return isNoSmokingRoom;
    }

    public int getIsPartyRoom()
    {
        return isPartyRoom;
    }

    public int getIsRoomService()
    {
        return isRoomService;
    }

    public int getIsCasher()
    {
        return isCasher;
    }

    public int getIsJetbath()
    {
        return isJetbath;
    }

    public int getIsOpenAirBath()
    {
        return isOpenAirBath;
    }

    public int getIsBedrockBath()
    {
        return isBedrockBath;
    }

    public int getIsSauna()
    {
        return isSauna;
    }

    public int getIsHotSpring()
    {
        return isHotSpring;
    }

    public int getIsPool()
    {
        return isPool;
    }

    public int getIsBathroomTv()
    {
        return isBathroomTv;
    }

    public int getIsHugeScreenTv()
    {
        return isHugeScreenTv;
    }

    public int getIsSurroundSystem()
    {
        return isSurroundSystem;
    }

    public int getIsVod()
    {
        return isVod;
    }

    public int getIsOnlineKaraoke()
    {
        return isOnlineKaraoke;
    }

    public int getIsVideoGame()
    {
        return isVideoGame;
    }

    public int getIsPc()
    {
        return isPc;
    }

    public int getIsTabletPc()
    {
        return isTabletPc;
    }

    public int getIsInternet()
    {
        return isInternet;
    }

    public int getIsJapaneseRoom()
    {
        return isJapaneseRoom;
    }

    public int getIsLaundry()
    {
        return isLaundry;
    }

    public int getIsDryningMachine()
    {
        return isDryningMachine;
    }

    public int getIsShowerToilet()
    {
        return isShowerToilet;
    }

    public int getIsProjecter()
    {
        return isProjecter;
    }

    public int getIsBathrobe()
    {
        return isBathrobe;
    }

    public int getIsRoomWear()
    {
        return isRoomWear;
    }

    public int getIsWomenCosmetics()
    {
        return isWomenCosmetics;
    }

    public int getIsVariousShampoo()
    {
        return isVariousShampoo;
    }

    public int getIsCostume()
    {
        return isCostume;
    }

    public int getIsMobilePhoneCharger()
    {
        return isMobilePhoneCharger;
    }

    public int getIsAndroidPhoneCharger()
    {
        return isAndroidPhoneCharger;
    }

    public int getIsIphonePhoneCharger()
    {
        return isIphonePhoneCharger;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public int getReflectUpdate()
    {
        return reflectUpdate;
    }

    public int getReflectUptime()
    {
        return reflectUptime;
    }

    public int getReflectFlag()
    {
        return reflectFlag;
    }

    public void setHotelId(int hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public void setRank(int rank)
    {
        this.rank = rank;
    }

    public void setMapId(String mapId)
    {
        this.mapId = mapId;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public void setJisCode(int jisCode)
    {
        this.jisCode = jisCode;
    }

    public void setPrefId(int prefId)
    {
        this.prefId = prefId;
    }

    public void setPrefName(String prefName)
    {
        this.prefName = prefName;
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

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public void setRoomCount(int roomCount)
    {
        this.roomCount = roomCount;
    }

    public void setHalfway(int halfway)
    {
        this.halfway = halfway;
    }

    public void setHalfwayMessage(String halfwayMessage)
    {
        this.halfwayMessage = halfwayMessage;
    }

    public void setPossibleOne(int possibleOne)
    {
        this.possibleOne = possibleOne;
    }

    public void setPossibleThree(int possibleThree)
    {
        this.possibleThree = possibleThree;
    }

    public void setHotelLat(String hotelLat)
    {
        this.hotelLat = hotelLat;
    }

    public void setHotelLon(String hotelLon)
    {
        this.hotelLon = hotelLon;
    }

    public void setOver18Flag(int over18Flag)
    {
        this.over18Flag = over18Flag;
    }

    public void setRestPriceFrom(int restPriceFrom)
    {
        this.restPriceFrom = restPriceFrom;
    }

    public void setRestPriceTo(int restPriceTo)
    {
        this.restPriceTo = restPriceTo;
    }

    public void setStayPriceFrom(int stayPriceFrom)
    {
        this.stayPriceFrom = stayPriceFrom;
    }

    public void setStayPriceTo(int stayPriceTo)
    {
        this.stayPriceTo = stayPriceTo;
    }

    public void setIsWomensUse(int isWomensUse)
    {
        this.isWomensUse = isWomensUse;
    }

    public void setIsMensUse(int isMensUse)
    {
        this.isMensUse = isMensUse;
    }

    public void setIsFreeWifi(int isFreeWifi)
    {
        this.isFreeWifi = isFreeWifi;
    }

    public void setIsFreeLan(int isFreeLan)
    {
        this.isFreeLan = isFreeLan;
    }

    public void setIsNoSmokingRoom(int isNoSmokingRoom)
    {
        this.isNoSmokingRoom = isNoSmokingRoom;
    }

    public void setIsPartyRoom(int isPartyRoom)
    {
        this.isPartyRoom = isPartyRoom;
    }

    public void setIsRoomService(int isRoomService)
    {
        this.isRoomService = isRoomService;
    }

    public void setIsCasher(int isCasher)
    {
        this.isCasher = isCasher;
    }

    public void setIsJetbath(int isJetbath)
    {
        this.isJetbath = isJetbath;
    }

    public void setIsOpenAirBath(int isOpenAirBath)
    {
        this.isOpenAirBath = isOpenAirBath;
    }

    public void setIsBedrockBath(int isBedrockBath)
    {
        this.isBedrockBath = isBedrockBath;
    }

    public void setIsSauna(int isSauna)
    {
        this.isSauna = isSauna;
    }

    public void setIsHotSpring(int isHotSpring)
    {
        this.isHotSpring = isHotSpring;
    }

    public void setIsPool(int isPool)
    {
        this.isPool = isPool;
    }

    public void setIsBathroomTv(int isBathroomTv)
    {
        this.isBathroomTv = isBathroomTv;
    }

    public void setIsHugeScreenTv(int isHugeScreenTv)
    {
        this.isHugeScreenTv = isHugeScreenTv;
    }

    public void setIsSurroundSystem(int isSurroundSystem)
    {
        this.isSurroundSystem = isSurroundSystem;
    }

    public void setIsVod(int isVod)
    {
        this.isVod = isVod;
    }

    public void setIsOnlineKaraoke(int isOnlineKaraoke)
    {
        this.isOnlineKaraoke = isOnlineKaraoke;
    }

    public void setIsVideoGame(int isVideoGame)
    {
        this.isVideoGame = isVideoGame;
    }

    public void setIsPc(int isPc)
    {
        this.isPc = isPc;
    }

    public void setIsTabletPc(int isTabletPc)
    {
        this.isTabletPc = isTabletPc;
    }

    public void setIsInternet(int isInternet)
    {
        this.isInternet = isInternet;
    }

    public void setIsJapaneseRoom(int isJapaneseRoom)
    {
        this.isJapaneseRoom = isJapaneseRoom;
    }

    public void setIsLaundry(int isLaundry)
    {
        this.isLaundry = isLaundry;
    }

    public void setIsDryningMachine(int isDryningMachine)
    {
        this.isDryningMachine = isDryningMachine;
    }

    public void setIsShowerToilet(int isShowerToilet)
    {
        this.isShowerToilet = isShowerToilet;
    }

    public void setIsProjecter(int isProjecter)
    {
        this.isProjecter = isProjecter;
    }

    public void setIsBathrobe(int isBathrobe)
    {
        this.isBathrobe = isBathrobe;
    }

    public void setIsRoomWear(int isRoomWear)
    {
        this.isRoomWear = isRoomWear;
    }

    public void setIsWomenCosmetics(int isWomenCosmetics)
    {
        this.isWomenCosmetics = isWomenCosmetics;
    }

    public void setIsVariousShampoo(int isVariousShampoo)
    {
        this.isVariousShampoo = isVariousShampoo;
    }

    public void setIsCostume(int isCostume)
    {
        this.isCostume = isCostume;
    }

    public void setIsMobilePhoneCharger(int isMobilePhoneCharger)
    {
        this.isMobilePhoneCharger = isMobilePhoneCharger;
    }

    public void setIsAndroidPhoneCharger(int isAndroidPhoneCharger)
    {
        this.isAndroidPhoneCharger = isAndroidPhoneCharger;
    }

    public void setIsIphonePhoneCharger(int isIphonePhoneCharger)
    {
        this.isIphonePhoneCharger = isIphonePhoneCharger;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setReflectFlag(int reflectFlag)
    {
        this.reflectFlag = reflectFlag;
    }

    public void setReflectUpdate(int reflectUpdate)
    {
        this.reflectUpdate = reflectUpdate;
    }

    public void setReflectUptime(int reflectUptime)
    {
        this.reflectUptime = reflectUptime;
    }

    /****
     * 取得
     * 
     * @param hotelId ホテルID (hh_hotel_basic.id)
     * @param seq 管理番号
     * @return
     */
    public boolean getData(int hotelId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM lvj_hotel_basic WHERE hotel_id = ? AND seq = ? ";
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
                    ret = setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataLvjHotelBasic.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 設定
     * 
     * @param result マスターソートレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.hotelId = result.getInt( "hotel_id" );
                this.seq = result.getInt( "seq" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
                this.rank = result.getInt( "rank" );
                this.mapId = result.getString( "map_id" );
                this.zipCode = result.getString( "zip_code" );
                this.jisCode = result.getInt( "jis_code" );
                this.prefId = result.getInt( "pref_id" );
                this.prefName = result.getString( "pref_name" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.address3 = result.getString( "address3" );
                this.tel = result.getString( "tel" );
                this.fax = result.getString( "fax" );
                this.roomCount = result.getInt( "room_count" );
                this.halfway = result.getInt( "halfway" );
                this.halfwayMessage = result.getString( "halfway_message" );
                this.possibleOne = result.getInt( "possible_one" );
                this.possibleThree = result.getInt( "possible_three" );
                this.hotelLat = result.getString( "hotel_lat" );
                this.hotelLon = result.getString( "hotel_lon" );
                this.over18Flag = result.getInt( "over18_flag" );
                this.restPriceFrom = result.getInt( "rest_price_from" );
                this.restPriceTo = result.getInt( "rest_price_to" );
                this.stayPriceFrom = result.getInt( "stay_price_from" );
                this.stayPriceTo = result.getInt( "stay_price_to" );
                this.isWomensUse = result.getInt( "is_womens_use" );
                this.isMensUse = result.getInt( "is_mens_use" );
                this.isFreeWifi = result.getInt( "is_free_wifi" );
                this.isFreeLan = result.getInt( "is_free_lan" );
                this.isNoSmokingRoom = result.getInt( "is_no_smoking_room" );
                this.isPartyRoom = result.getInt( "is_party_room" );
                this.isRoomService = result.getInt( "is_room_service" );
                this.isCasher = result.getInt( "is_casher" );
                this.isJetbath = result.getInt( "is_jetbath" );
                this.isOpenAirBath = result.getInt( "is_open_air_bath" );
                this.isBedrockBath = result.getInt( "is_bedrock_bath" );
                this.isSauna = result.getInt( "is_sauna" );
                this.isHotSpring = result.getInt( "is_hot_spring" );
                this.isPool = result.getInt( "is_pool" );
                this.isBathroomTv = result.getInt( "is_bathroom_tv" );
                this.isHugeScreenTv = result.getInt( "is_huge_screen_tv" );
                this.isSurroundSystem = result.getInt( "is_surround_system" );
                this.isVod = result.getInt( "is_vod" );
                this.isOnlineKaraoke = result.getInt( "is_online_karaoke" );
                this.isVideoGame = result.getInt( "is_video_game" );
                this.isPc = result.getInt( "is_pc" );
                this.isTabletPc = result.getInt( "is_tablet_pc" );
                this.isInternet = result.getInt( "is_internet" );
                this.isJapaneseRoom = result.getInt( "is_japanese_room" );
                this.isLaundry = result.getInt( "is_laundry" );
                this.isDryningMachine = result.getInt( "is_drying_machine" );
                this.isShowerToilet = result.getInt( "is_shower_toilet" );
                this.isProjecter = result.getInt( "is_projecter" );
                this.isBathrobe = result.getInt( "is_bathrobe" );
                this.isRoomWear = result.getInt( "is_room_wear" );
                this.isWomenCosmetics = result.getInt( "is_women_cosmetics" );
                this.isVariousShampoo = result.getInt( "is_various_shampoo" );
                this.isCostume = result.getInt( "is_costume" );
                this.isMobilePhoneCharger = result.getInt( "is_mobile_phone_charger" );
                this.isAndroidPhoneCharger = result.getInt( "is_android_phone_charger" );
                this.isIphonePhoneCharger = result.getInt( "is_iphone_phone_charger" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.reflectFlag = result.getInt( "reflect_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataLvjHotelBasic.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT lvj_hotel_basic SET ";
        query += " hotel_id=?";
        query += ", seq=0";
        query += ", name=?";
        query += ", name_kana=?";
        query += ", rank=?";
        query += ", map_id=?";
        query += ", zip_code=?";
        query += ", jis_code=?";
        query += ", pref_id=?";
        query += ", pref_name=?";
        query += ", address1=?";
        query += ", address2=?";
        query += ", address3=?";
        query += ", tel=?";
        query += ", fax=?";
        query += ", room_count=?";
        query += ", halfway=?";
        query += ", halfway_message=?";
        query += ", possible_one=?";
        query += ", possible_three=?";
        query += ", hotel_lat=?";
        query += ", hotel_lon=?";
        query += ", over18_flag=?";
        query += ", rest_price_from=?";
        query += ", rest_price_to=?";
        query += ", stay_price_from=?";
        query += ", stay_price_to=?";
        query += ", is_womens_use=?";
        query += ", is_mens_use=?";
        query += ", is_free_wifi=?";
        query += ", is_free_lan=?";
        query += ", is_no_smoking_room=?";
        query += ", is_party_room=?";
        query += ", is_room_service=?";
        query += ", is_casher=?";
        query += ", is_jetbath=?";
        query += ", is_open_air_bath=?";
        query += ", is_bedrock_bath=?";
        query += ", is_sauna=?";
        query += ", is_hot_spring=?";
        query += ", is_pool=?";
        query += ", is_bathroom_tv=?";
        query += ", is_huge_screen_tv=?";
        query += ", is_surround_system=?";
        query += ", is_vod=?";
        query += ", is_online_karaoke=?";
        query += ", is_video_game=?";
        query += ", is_pc=?";
        query += ", is_tablet_pc=?";
        query += ", is_internet=?";
        query += ", is_japanese_room=?";
        query += ", is_laundry=?";
        query += ", is_drying_machine=?";
        query += ", is_shower_toilet=?";
        query += ", is_projecter=?";
        query += ", is_bathrobe=?";
        query += ", is_room_wear=?";
        query += ", is_women_cosmetics=?";
        query += ", is_various_shampoo=?";
        query += ", is_costume=?";
        query += ", is_mobile_phone_charger=?";
        query += ", is_android_phone_charger=?";
        query += ", is_iphone_phone_charger=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", reflect_flag=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.hotelId );
            // prestate.setInt( i++, this.seq );
            prestate.setString( i++, this.name );
            prestate.setString( i++, this.nameKana );
            prestate.setInt( i++, this.rank );
            prestate.setString( i++, this.mapId );
            prestate.setString( i++, this.zipCode );
            prestate.setInt( i++, this.jisCode );
            prestate.setInt( i++, this.prefId );
            prestate.setString( i++, this.prefName );
            prestate.setString( i++, this.address1 );
            prestate.setString( i++, this.address2 );
            prestate.setString( i++, this.address3 );
            prestate.setString( i++, this.tel );
            prestate.setString( i++, this.fax );
            prestate.setInt( i++, this.roomCount );
            prestate.setInt( i++, this.halfway );
            prestate.setString( i++, this.halfwayMessage );
            prestate.setInt( i++, this.possibleOne );
            prestate.setInt( i++, this.possibleThree );
            prestate.setString( i++, this.hotelLat );
            prestate.setString( i++, this.hotelLon );
            prestate.setInt( i++, this.over18Flag );
            prestate.setInt( i++, this.restPriceFrom );
            prestate.setInt( i++, this.restPriceTo );
            prestate.setInt( i++, this.stayPriceFrom );
            prestate.setInt( i++, this.stayPriceTo );
            prestate.setInt( i++, this.isWomensUse );
            prestate.setInt( i++, this.isMensUse );
            prestate.setInt( i++, this.isFreeWifi );
            prestate.setInt( i++, this.isFreeLan );
            prestate.setInt( i++, this.isNoSmokingRoom );
            prestate.setInt( i++, this.isPartyRoom );
            prestate.setInt( i++, this.isRoomService );
            prestate.setInt( i++, this.isCasher );
            prestate.setInt( i++, this.isJetbath );
            prestate.setInt( i++, this.isOpenAirBath );
            prestate.setInt( i++, this.isBedrockBath );
            prestate.setInt( i++, this.isSauna );
            prestate.setInt( i++, this.isHotSpring );
            prestate.setInt( i++, this.isPool );
            prestate.setInt( i++, this.isBathroomTv );
            prestate.setInt( i++, this.isHugeScreenTv );
            prestate.setInt( i++, this.isSurroundSystem );
            prestate.setInt( i++, this.isVod );
            prestate.setInt( i++, this.isOnlineKaraoke );
            prestate.setInt( i++, this.isVideoGame );
            prestate.setInt( i++, this.isPc );
            prestate.setInt( i++, this.isTabletPc );
            prestate.setInt( i++, this.isInternet );
            prestate.setInt( i++, this.isJapaneseRoom );
            prestate.setInt( i++, this.isLaundry );
            prestate.setInt( i++, this.isDryningMachine );
            prestate.setInt( i++, this.isShowerToilet );
            prestate.setInt( i++, this.isProjecter );
            prestate.setInt( i++, this.isBathrobe );
            prestate.setInt( i++, this.isRoomWear );
            prestate.setInt( i++, this.isWomenCosmetics );
            prestate.setInt( i++, this.isVariousShampoo );
            prestate.setInt( i++, this.isCostume );
            prestate.setInt( i++, this.isMobilePhoneCharger );
            prestate.setInt( i++, this.isAndroidPhoneCharger );
            prestate.setInt( i++, this.isIphonePhoneCharger );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.reflectFlag );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataLvjHotelBasic.insertData] Exception=" + e.toString() );
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
     * 更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param hotelId ホテルID (hh_hotel_basic.id)
     * @param seq 管理番号
     * @return
     */
    public boolean updateData(int hotelId, int seq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE lvj_hotel_basic SET ";
        query += " name=?";
        query += ", name_kana=?";
        query += ", rank=?";
        query += ", map_id=?";
        query += ", zip_code=?";
        query += ", jis_code=?";
        query += ", pref_id=?";
        query += ", pref_name=?";
        query += ", address1=?";
        query += ", address2=?";
        query += ", address3=?";
        query += ", tel=?";
        query += ", fax=?";
        query += ", room_count=?";
        query += ", halfway=?";
        query += ", halfway_message=?";
        query += ", possible_one=?";
        query += ", possible_three=?";
        query += ", hotel_lat=?";
        query += ", hotel_lon=?";
        query += ", over18_flag=?";
        query += ", rest_price_from=?";
        query += ", rest_price_to=?";
        query += ", stay_price_from=?";
        query += ", stay_price_to=?";
        query += ", is_womens_use=?";
        query += ", is_mens_use=?";
        query += ", is_free_wifi=?";
        query += ", is_free_lan=?";
        query += ", is_no_smoking_room=?";
        query += ", is_party_room=?";
        query += ", is_room_service=?";
        query += ", is_casher=?";
        query += ", is_jetbath=?";
        query += ", is_open_air_bath=?";
        query += ", is_bedrock_bath=?";
        query += ", is_sauna=?";
        query += ", is_hot_spring=?";
        query += ", is_pool=?";
        query += ", is_bathroom_tv=?";
        query += ", is_huge_screen_tv=?";
        query += ", is_surround_system=?";
        query += ", is_vod=?";
        query += ", is_online_karaoke=?";
        query += ", is_video_game=?";
        query += ", is_pc=?";
        query += ", is_tablet_pc=?";
        query += ", is_internet=?";
        query += ", is_japanese_room=?";
        query += ", is_laundry=?";
        query += ", is_drying_machine=?";
        query += ", is_shower_toilet=?";
        query += ", is_projecter=?";
        query += ", is_bathrobe=?";
        query += ", is_room_wear=?";
        query += ", is_women_cosmetics=?";
        query += ", is_various_shampoo=?";
        query += ", is_costume=?";
        query += ", is_mobile_phone_charger=?";
        query += ", is_android_phone_charger=?";
        query += ", is_iphone_phone_charger=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", reflect_flag=?";
        query += ", reflect_update=?";
        query += ", reflect_uptime=?";
        query += " WHERE hotel_id=? AND seq=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.name );
            prestate.setString( i++, this.nameKana );
            prestate.setInt( i++, this.rank );
            prestate.setString( i++, this.mapId );
            prestate.setString( i++, this.zipCode );
            prestate.setInt( i++, this.jisCode );
            prestate.setInt( i++, this.prefId );
            prestate.setString( i++, this.prefName );
            prestate.setString( i++, this.address1 );
            prestate.setString( i++, this.address2 );
            prestate.setString( i++, this.address3 );
            prestate.setString( i++, this.tel );
            prestate.setString( i++, this.fax );
            prestate.setInt( i++, this.roomCount );
            prestate.setInt( i++, this.halfway );
            prestate.setString( i++, this.halfwayMessage );
            prestate.setInt( i++, this.possibleOne );
            prestate.setInt( i++, this.possibleThree );
            prestate.setString( i++, this.hotelLat );
            prestate.setString( i++, this.hotelLon );
            prestate.setInt( i++, this.over18Flag );
            prestate.setInt( i++, this.restPriceFrom );
            prestate.setInt( i++, this.restPriceTo );
            prestate.setInt( i++, this.stayPriceFrom );
            prestate.setInt( i++, this.stayPriceTo );
            prestate.setInt( i++, this.isWomensUse );
            prestate.setInt( i++, this.isMensUse );
            prestate.setInt( i++, this.isFreeWifi );
            prestate.setInt( i++, this.isFreeLan );
            prestate.setInt( i++, this.isNoSmokingRoom );
            prestate.setInt( i++, this.isPartyRoom );
            prestate.setInt( i++, this.isRoomService );
            prestate.setInt( i++, this.isCasher );
            prestate.setInt( i++, this.isJetbath );
            prestate.setInt( i++, this.isOpenAirBath );
            prestate.setInt( i++, this.isBedrockBath );
            prestate.setInt( i++, this.isSauna );
            prestate.setInt( i++, this.isHotSpring );
            prestate.setInt( i++, this.isPool );
            prestate.setInt( i++, this.isBathroomTv );
            prestate.setInt( i++, this.isHugeScreenTv );
            prestate.setInt( i++, this.isSurroundSystem );
            prestate.setInt( i++, this.isVod );
            prestate.setInt( i++, this.isOnlineKaraoke );
            prestate.setInt( i++, this.isVideoGame );
            prestate.setInt( i++, this.isPc );
            prestate.setInt( i++, this.isTabletPc );
            prestate.setInt( i++, this.isInternet );
            prestate.setInt( i++, this.isJapaneseRoom );
            prestate.setInt( i++, this.isLaundry );
            prestate.setInt( i++, this.isDryningMachine );
            prestate.setInt( i++, this.isShowerToilet );
            prestate.setInt( i++, this.isProjecter );
            prestate.setInt( i++, this.isBathrobe );
            prestate.setInt( i++, this.isRoomWear );
            prestate.setInt( i++, this.isWomenCosmetics );
            prestate.setInt( i++, this.isVariousShampoo );
            prestate.setInt( i++, this.isCostume );
            prestate.setInt( i++, this.isMobilePhoneCharger );
            prestate.setInt( i++, this.isAndroidPhoneCharger );
            prestate.setInt( i++, this.isIphonePhoneCharger );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.reflectFlag );
            prestate.setInt( i++, this.reflectUpdate );
            prestate.setInt( i++, this.reflectUptime );
            prestate.setInt( i++, this.hotelId );
            prestate.setInt( i++, this.seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataLvjHotelBasic.updateData] Exception=" + e.toString() );
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
