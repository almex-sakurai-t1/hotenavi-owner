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
public class DataApiHogeLvjHotel implements Serializable
{
    /**
     *
     */
    private static final long     serialVersionUID = 600752364650037394L;
    public static final String    TABLE            = "lvj_hotel_basic";
    private int                   hotelId;                               // ホテルID (hh_hotel_basic.id)
    private int                   seq;                                   // 管理番号
    private String                name;                                  // ホテル名
    private String                nameKana;                              // ホテル名（日本語カナ）
    private int                   rank;                                  // ホテルランク -1:掲載なし ,0:未加盟,1:ライト,2:スタンダード,3:ハピホテマイル
    private String                mapId;                                 // 最寄駅コード カンマ区切りで複数渡す
    private String                zipCode;                               // 郵便番号（ハイフンなし）
    private int                   jisCode;                               // 市区町村コード
    private int                   prefId;                                // 都道府県コード
    private String                prefName;                              // 都道府県名
    private String                address1;                              // 住所(1)
    private String                address2;                              // 住所(2)
    private String                address3;                              // 住所(3)
    private String                tel;                                   // 電話番号
    private String                fax;                                   // FAX
    private int                   roomCount;                             // 部屋数
    private int                   halfway;                               // 途中外出　0:未入力,1:可,9:不可
    private String                halfwayMessage;                        // 途中外出詳細 (hh_hotel_remarks disp_no=8)
    private int                   possibleOne;                           // 一人利用　0:未入力,1:可,9:不可
    private int                   possibleThree;                         // 複数人利用（3人以上）　0:未入力,1:可,9:不可
    private String                hotelLat;                              // ホテル緯度
    private String                hotelLon;                              // ホテル経度
    private int                   over18Flag;                            // 18禁フラグ　0:未入力,1:あり,9:なし
    private int                   restPriceFrom;                         // 休憩料金（最低）← hh_hotel_price
    private int                   restPriceTo;                           // 休憩料金（最高）← hh_hotel_price
    private int                   stayPriceFrom;                         // 宿泊料金（最低）← hh_hotel_price
    private int                   stayPriceTo;                           // 宿泊料金（最高）← hh_hotel_price
    private int                   isWomensUse;                           // 同性利用可（女性）　　0:未入力,1:可,9:不可 (hh_hotel_equip equip_id=73)
    private int                   isMensUse;                             // 同性利用可（男性）　　0:未入力,1:可,9:不可　(hh_hotel_equip equip_id=72)
    private int                   isFreeWifi;                            // 無料Wi-Fi(無線LAN)　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=54)
    private int                   isFreeLan;                             // 有線LAN　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=53)
    private int                   isNoSmokingRoom;                       // 禁煙ルーム　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=2)
    private int                   isPartyRoom;                           // パーティールーム　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=62)
    private int                   isRoomService;                         // ルームサービス　0:未入力,1:あり,9:なし　（hh_hotel_basic.roomservice）
    private int                   isCasher;                              // 精算機　0:未入力,1:あり,9:なし (hh_hotel_basic.pay_auto)
    private int                   isJetbath;                             // ジェットバス／ブロアバス　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=3)
    private int                   isOpenAirBath;                         // 露天風呂　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=11)
    private int                   isBedrockBath;                         // 岩盤浴　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=13)
    private int                   isSauna;                               // サウナ　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=5)
    private int                   isHotSpring;                           // 温泉　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=47)
    private int                   isPool;                                // プール　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=48)
    private int                   isBathroomTv;                          // 浴室TV　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=9)
    private int                   isHugeScreenTv;                        // 大画面テレビ100インチ以上　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=59)
    private int                   isSurroundSystem;                      // 5.1chサラウンド　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=23)
    private int                   isVod;                                 // ビデオ・オン・デマンド　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=25)
    private int                   isOnlineKaraoke;                       // 通信カラオケ　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=35)
    private int                   isVideoGame;                           // TVゲーム　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=37)
    private int                   isPc;                                  // 客室PC　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=51)
    private int                   isTabletPc;                            // 客室タブレット　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=52)
    private int                   isInternet;                            // 客室インターネット　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=41)
    private int                   isJapaneseRoom;                        // 和室　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=45)
    private int                   isLaundry;                             // 洗濯機　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=16)
    private int                   isDryningMachine;                      // 乾燥機　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=18)
    private int                   isShowerToilet;                        // シャワートイレ　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=34)
    private int                   isProjecter;                           // プロジェクター　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=46)
    private int                   isBathrobe;                            // バスローブ　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=38)
    private int                   isRoomWear;                            // ルームウェア　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=64)
    private int                   isWomenCosmetics;                      // 女性化粧品　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=68)
    private int                   isVariousShampoo;                      // 各種シャンプー　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=42)
    private int                   isCostume;                             // コスチューム　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=44)
    private int                   isMobilePhoneCharger;                  // 携帯電話充電器　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=55)
    private int                   isAndroidPhoneCharger;                 // Androidスマホ充電器　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=56)
    private int                   isIphonePhoneCharger;                  // iPhone充電器　0:未入力,1:あり,3:ﾚﾝﾀﾙ,9:なし (hh_hotel_equip equip_id=57)
    private int                   lastUpdate;                            // 最終更新日(YYYYMMDD)
    private int                   lastUptime;                            // 最終更新時刻(HHMMSS)
    /** 予約対応言語 */
    private Language              language         = new Language();
    private int                   m_Count;                               // レコード件数
    private DataApiHogeLvjHotel[] m_Basic;

    /**
     * データを初期化します。
     */
    public DataApiHogeLvjHotel()
    {
        this.hotelId = 0;
        this.seq = 0;
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
    }

    /**
     * 予約対応言語
     */
    public class Language
    {
        /** 英語 */
        private boolean en;
        /** 韓国語 */
        private boolean kr;
        /** 繁体字 */
        private boolean tw;
        /** 簡体字 */
        private boolean ch;

        public boolean getEn()
        {
            return en;
        }

        public void setEn(boolean en)
        {
            this.en = en;
        }

        public boolean getKr()
        {
            return kr;
        }

        public void setKr(boolean kr)
        {
            this.kr = kr;
        }

        public boolean getTw()
        {
            return tw;
        }

        public void setTw(boolean tw)
        {
            this.tw = tw;
        }

        public boolean getCh()
        {
            return ch;
        }

        public void setCh(boolean ch)
        {
            this.ch = ch;
        }
    }

    public int getHotel_id()
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

    public String getName_kana()
    {
        return nameKana;
    }

    public int getRank()
    {
        return rank;
    }

    public String getMap_id()
    {
        return mapId;
    }

    public String getZip_code()
    {
        return zipCode;
    }

    public int getJis_code()
    {
        return jisCode;
    }

    public int getPref_id()
    {
        return prefId;
    }

    public String getPref_name()
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

    public int getRoom_count()
    {
        return roomCount;
    }

    public int getHalfway()
    {
        return halfway;
    }

    public String getHalfway_message()
    {
        return halfwayMessage;
    }

    public int getPossible_one()
    {
        return possibleOne;
    }

    public int getPossible_three()
    {
        return possibleThree;
    }

    public String getHotel_lat()
    {
        return hotelLat;
    }

    public String getHotel_lon()
    {
        return hotelLon;
    }

    public int getOver18_flag()
    {
        return over18Flag;
    }

    public int getRest_price_from()
    {
        return restPriceFrom;
    }

    public int getRest_price_to()
    {
        return restPriceTo;
    }

    public int getStay_price_from()
    {
        return stayPriceFrom;
    }

    public int getStay_price_to()
    {
        return stayPriceTo;
    }

    public int getIs_womens_use()
    {
        return isWomensUse;
    }

    public int getIs_mens_use()
    {
        return isMensUse;
    }

    public int getIs_free_wifi()
    {
        return isFreeWifi;
    }

    public int getIs_free_lan()
    {
        return isFreeLan;
    }

    public int getIs_no_smoking_room()
    {
        return isNoSmokingRoom;
    }

    public int getIs_party_room()
    {
        return isPartyRoom;
    }

    public int getIs_room_service()
    {
        return isRoomService;
    }

    public int getIs_casher()
    {
        return isCasher;
    }

    public int getIs_jetbath()
    {
        return isJetbath;
    }

    public int getIs_open_air_bath()
    {
        return isOpenAirBath;
    }

    public int getIs_bedrock_bath()
    {
        return isBedrockBath;
    }

    public int getIs_sauna()
    {
        return isSauna;
    }

    public int getIs_hot_spring()
    {
        return isHotSpring;
    }

    public int getIs_pool()
    {
        return isPool;
    }

    public int getIs_bathroom_tv()
    {
        return isBathroomTv;
    }

    public int getIs_huge_screen_tv()
    {
        return isHugeScreenTv;
    }

    public int getIs_surround_system()
    {
        return isSurroundSystem;
    }

    public int getIs_vod()
    {
        return isVod;
    }

    public int getIs_online_karaoke()
    {
        return isOnlineKaraoke;
    }

    public int getIs_video_game()
    {
        return isVideoGame;
    }

    public int getIs_pc()
    {
        return isPc;
    }

    public int getIs_tablet_pc()
    {
        return isTabletPc;
    }

    public int getIs_internet()
    {
        return isInternet;
    }

    public int getis_japanese_room()
    {
        return isJapaneseRoom;
    }

    public int getIs_laundry()
    {
        return isLaundry;
    }

    public int getis_drying_machine()
    {
        return isDryningMachine;
    }

    public int getIs_shower_toilet()
    {
        return isShowerToilet;
    }

    public int getIs_projecter()
    {
        return isProjecter;
    }

    public int getIs_bathrobe()
    {
        return isBathrobe;
    }

    public int getIs_room_wear()
    {
        return isRoomWear;
    }

    public int getIs_women_cosmetics()
    {
        return isWomenCosmetics;
    }

    public int getIs_various_shampoo()
    {
        return isVariousShampoo;
    }

    public int getIs_costume()
    {
        return isCostume;
    }

    public int getis_mobile_phone_charger()
    {
        return isMobilePhoneCharger;
    }

    public int getIs_android_phone_charger()
    {
        return isAndroidPhoneCharger;
    }

    public int getIs_iphone_phone_charger()
    {
        return isIphonePhoneCharger;
    }

    public int getLast_update()
    {
        return lastUpdate;
    }

    public int getLast_uptime()
    {
        return lastUptime;
    }

    public Language getLanguage()
    {
        return language;
    }

    public void setLanguage(Language language)
    {
        this.language = language;
    }

    public int getCount()
    {
        return m_Count;
    }

    public DataApiHogeLvjHotel[] getApiData()
    {
        return m_Basic;
    }

    /****
     * 取得
     * 
     * @param hotelId ホテルID (hh_hotel_basic.id)
     * @return
     */
    public boolean getData(int hotelId)
    {
        boolean ret;
        String query;
        int count = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM lvj_hotel_basic";
        query += " LEFT JOIN newRsvDB.hh_rsv_reserve_basic ON hh_rsv_reserve_basic.id = lvj_hotel_basic.hotel_id ";
        if ( hotelId != 0 )
        {
            query += " WHERE lvj_hotel_basic.hotel_id = ? ";
        }
        else
        {
            query += " WHERE lvj_hotel_basic.reflect_flag = ? "; // 全件取得の場合はreflect_flag=0のみ
        }
        query += " ORDER BY lvj_hotel_basic.seq DESC";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( hotelId != 0 )
            {
                prestate.setInt( 1, hotelId );
            }
            else
            {
                prestate.setInt( 1, 0 );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_Count = result.getRow();
                }
                // クラスの配列を用意し、初期化する。
                this.m_Basic = new DataApiHogeLvjHotel[this.m_Count];
                for( int i = 0 ; i < m_Count ; i++ )
                {
                    m_Basic[i] = new DataApiHogeLvjHotel();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // 情報の取得
                    this.m_Basic[count++].setData( result );
                }
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApiHogeLvjHotel.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

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
                this.language.en = false;
                if ( result.getInt( "foreign_localize_english" ) == 1 )
                {
                    this.language.en = true;
                }
                this.language.ch = false;
                if ( result.getInt( "foreign_localize_chinese" ) == 1 )
                {
                    this.language.ch = true;
                }
                this.language.kr = false;
                if ( result.getInt( "foreign_localize_korean" ) == 1 )
                {
                    this.language.kr = true;
                }
                this.language.tw = false;
                if ( result.getInt( "foreign_localize_taiwan" ) == 1 )
                {
                    this.language.tw = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApiHogeLvjHotel.setData] Exception=" + e.toString() );
        }
        return(true);
    }
}
