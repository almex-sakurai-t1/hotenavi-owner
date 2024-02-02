/*
 * @(#)HotelDetail.java 1.00
 * 2011/04/25 Copyright (C) ALMEX Inc. 2011
 * ホテル詳細クラス
 */
package jp.happyhotel.hotel;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.IDN;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import jp.happyhotel.common.ConstantsHotel;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Url;
import jp.happyhotel.data.DataApHotelCustom;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelGallery;
import jp.happyhotel.data.DataHotelMaster;
import jp.happyhotel.data.DataMasterCoupon;
import jp.happyhotel.data.DataMasterName;
import jp.happyhotel.data.DataRsvReserveBasic;
import jp.happyhotel.touch.AutoCoupon;
import jp.happyhotel.touch.TouchUserCoupon;
import jp.happyhotel.user.UserCoupon;
import jp.happyhotel.user.UserLoginInfo;

/**
 * ホテル詳細クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2011/04/25
 */
public class HotelDetail implements Serializable
{
    final int                            IPHONE                = 0;
    final int                            ANDROID               = 1;

    public final int                     NOT_REAL_TIME         = 0;
    public final int                     REAL_TIME             = 1;
    public final String                  IMAGE_PATH            = "/common/images/HB/";
    public final String                  NO_IMAGE_PATH         = "/common/images/noimage.jpg";
    public final String                  HAPPIE_MEMBER_IMG     = Url.getUrl() + "/common/image/touch_detail_bnr01.gif"; // リニューアル前の画像
    // public final String HAPPIE_MEMBER_IMG = "http://happyhotel.jp/phone/images/btn_touch.png";//リニューアル後の画像
    public final String                  HAPPIE_MEMBER_URL     = Url.getUrl() + "/phone/others/touch_index.jsp";
    public final String                  HAPPIE_MEMBER_TEXT    = "[ハピホテマイル加盟店]\n当ホテルはハピホテタッチに対応しています。";
    public final String                  RSV_HAPPIE_MEMBER_IMG = Url.getUrl() + "/common/image/yoyaku_detail_bnr01.gif"; // リニューアル前の画像
    // public final String RSV_HAPPIE_MEMBER_IMG = "http://happyhotel.jp/phone/images/btn_reserve.png"; // リニューアル後の画像
    public final String                  RSV_HAPPIE_MEMBER_URL = Url.getUrl() + "/phone/bn_newreserve.jsp";
    public final String                  RSV_MEMBER_TEXT       = "[ハピホテマイル加盟店]\n当ホテルはハピホテタッチ・ハピホテ予約に対応しています。";
    // public final String NOMEMBER_TEXT = "空室情報を表示（プレミアム会員専用）\nハピホテプレミアムコースのご案内";
    // public final String NOMEMBER_TEXT = "ハピホテプレミアムコースのご案内";
    public final String                  NOMEMBER_TEXT         = "ハピホテプレミアムコースのご案内";
    public final String                  NOMEMBER_URL          = Url.getUrl() + "/phone/others/info_premium_app.jsp";
    // public final String HAPPYHOTEL_URL = "http://121.101.88.177/phone/search/";
    public final String                  HAPPYHOTEL_URL        = Url.getUrl() + "/phone/search/";

    /**
     *
     */
    private static final long            serialVersionUID      = 130043483767139695L;
    private String                       name;
    private int                          id;
    private int                          rank;
    private int                          rsvFlag;
    private String                       memberImg;
    private String                       memberUrl;
    private String                       memberText;
    private int                          over18;
    private String                       image;
    private String                       message;
    private String                       pr;
    private String                       happiePr;
    private String                       kuchikomiAvg;
    private String                       address;
    private String                       tel;
    private String                       mapcode;
    private boolean                      boolEmpty;
    private int                          emptyStatus;
    private int                          emptyRoomCount;
    private int                          emptyUpdate;
    private int                          emptyUptime;
    private String                       nomemberText;
    private String                       nomemberUrl;
    private String                       roomAllCount;                                                                  // 全部屋数
    private int                          roomCount;                                                                     // 部屋数
    private String[]                     roomName;                                                                      // 部屋名
    private String[]                     roomImage;                                                                     // 画像URL
    private String[]                     roomText;                                                                      // 部屋説明
    private String[]                     roomTextUrl;                                                                   // 部屋説明URL
    private ArrayList<DataHotelGallery>  hotelGallery          = null;                                                  // ホテルギャラリー（カテゴリ名取得）
    private ArrayList<Integer>           galleryCount          = null;                                                  // ギャラリー数
    private ArrayList<ArrayList<String>> galleryName;                                                                   // ギャラリー名
    private ArrayList<ArrayList<String>> galleryImage;                                                                  // ギャラリー画像URK
    private ArrayList<ArrayList<String>> galleryText;                                                                   // ギャラリー説明文
    private ArrayList<ArrayList<String>> galleryTextUrl;                                                                // ギャラリーURL
    private String                       parking;
    private String                       buildingType;
    private String                       access;
    private String                       lat;
    private String                       lon;
    private ArrayList<String>            url                   = null;
    private ArrayList<String>            urlText               = null;
    private ArrayList<String>            hotelPriceName        = null;
    private ArrayList<ArrayList<String>> hotelPriceMessage     = null;
    private ArrayList<String>            hotelPriceRemarks     = null;
    private String                       privilege;
    private String                       roomService;
    private String                       otherService;
    private String                       credit;
    private String                       halfway;
    private String                       headCount;
    private String                       reserve;
    private ArrayList<String>            equipName             = null;
    private ArrayList<ArrayList<String>> equipMessage          = null;
    private ArrayList<String>            equipRemarks          = null;
    private String                       couponImage;
    private String                       couponNo;
    private ArrayList<String>            couponText            = null;
    private ArrayList<String>            couponCondition       = null;
    private String                       couponCommonCondition;
    private String                       couponPeriod;
    private String                       couponIssuance;
    private String                       couponMobileCondition;
    private String                       customId;
    private boolean                      hotenaviRoomPriceFlag;

    /**
     * データを初期化します。
     */
    public HotelDetail()
    {
        name = "";
        id = 0;
        rank = 0;
        rsvFlag = 0;
        memberImg = "";
        memberUrl = "";
        memberText = "";
        over18 = 0;
        image = "";
        message = "";
        pr = "";
        happiePr = "";
        kuchikomiAvg = "";
        address = "";
        tel = "";
        mapcode = "";
        boolEmpty = false;
        emptyStatus = 0;
        emptyRoomCount = 0;
        emptyUpdate = 0;
        emptyUptime = 0;
        roomAllCount = "";
        roomCount = 0;
        parking = "";
        buildingType = "";
        access = "";
        lat = "";
        lon = "";
        privilege = "";
        roomService = "";
        otherService = "";
        credit = "";
        halfway = "";
        headCount = "";
        reserve = "";
        couponImage = "";
        couponNo = "";
        couponCommonCondition = "";
        couponPeriod = "";
        couponIssuance = "";
        couponMobileCondition = "";
        hotenaviRoomPriceFlag = false;
        customId = "";
    }

    public String[] getRoomTextUrl()
    {
        return roomTextUrl;
    }

    public ArrayList<ArrayList<String>> getGalleryTextUrl()
    {
        return galleryTextUrl;
    }

    public void setRoomTextUrl(String[] roomTextUrl)
    {
        this.roomTextUrl = roomTextUrl;
    }

    public void setGalleryTextUrl(ArrayList<ArrayList<String>> galleryTextUrl)
    {
        this.galleryTextUrl = galleryTextUrl;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }

    public int getRank()
    {
        return rank;
    }

    public int getRsvFlag()
    {
        return rsvFlag;
    }

    public String getMemberImg()
    {
        return memberImg;
    }

    public String getMemberUrl()
    {
        return memberUrl;
    }

    public String getMemberText()
    {
        return memberText;
    }

    public int getOver18()
    {
        return over18;
    }

    public String getImage()
    {
        return image;
    }

    public String getMessage()
    {
        return message;
    }

    public String getPr()
    {
        return pr;
    }

    public String getHappiePr()
    {
        return happiePr;
    }

    public String getKuchikomiAvg()
    {
        return kuchikomiAvg;
    }

    public String getAddress()
    {
        return address;
    }

    public String getTel()
    {
        return tel;
    }

    public String getMapcode()
    {
        return mapcode;
    }

    public boolean isBoolEmpty()
    {
        return boolEmpty;
    }

    public int getEmptyStatus()
    {
        return emptyStatus;
    }

    public int getEmptyRoomCount()
    {
        return emptyRoomCount;
    }

    public int getEmptyUpdate()
    {
        return emptyUpdate;
    }

    public int getEmptyUptime()
    {
        return emptyUptime;
    }

    public String getNomemberText()
    {
        return nomemberText;
    }

    public String getNomemberUrl()
    {
        return nomemberUrl;
    }

    public String getRoomAllCount()
    {
        return roomAllCount;
    }

    public int getRoomCount()
    {
        return roomCount;
    }

    public String[] getRoomName()
    {
        return roomName;
    }

    public String[] getRoomImage()
    {
        return roomImage;
    }

    public String[] getRoomText()
    {
        return roomText;
    }

    public ArrayList<DataHotelGallery> getHotelGallery()
    {
        return hotelGallery;
    }

    public ArrayList<Integer> getGalleryCount()
    {
        return galleryCount;
    }

    public ArrayList<ArrayList<String>> getGalleryName()
    {
        return galleryName;
    }

    public ArrayList<ArrayList<String>> getGalleryImage()
    {
        return galleryImage;
    }

    public ArrayList<ArrayList<String>> getGalleryText()
    {
        return galleryText;
    }

    public String getParking()
    {
        return parking;
    }

    public String getBuildingType()
    {
        return buildingType;
    }

    public String getAccess()
    {
        return access;
    }

    public String getLat()
    {
        return lat;
    }

    public String getLon()
    {
        return lon;
    }

    public ArrayList<String> getUrl()
    {
        return url;
    }

    public ArrayList<String> getUrlText()
    {
        return urlText;
    }

    public ArrayList<String> getHotelPriceName()
    {
        return hotelPriceName;
    }

    public ArrayList<ArrayList<String>> getHotelPriceMessage()
    {
        return hotelPriceMessage;
    }

    public ArrayList<String> getHotelPriceRemarks()
    {
        return hotelPriceRemarks;
    }

    public String getPrivilege()
    {
        return privilege;
    }

    public String getRoomService()
    {
        return roomService;
    }

    public String getOtherService()
    {
        return otherService;
    }

    public String getCredit()
    {
        return credit;
    }

    public String getHalfway()
    {
        return halfway;
    }

    public String getHeadCount()
    {
        return headCount;
    }

    public String getReserve()
    {
        return reserve;
    }

    public ArrayList<String> getEquipName()
    {
        return equipName;
    }

    public ArrayList<ArrayList<String>> getEquipMessage()
    {
        return equipMessage;
    }

    public ArrayList<String> getEquipRemarks()
    {
        return equipRemarks;
    }

    public String getCouponImage()
    {
        return couponImage;
    }

    public String getCouponNo()
    {
        return couponNo;
    }

    public ArrayList<String> getCouponText()
    {
        return couponText;
    }

    public ArrayList<String> getCouponCondition()
    {
        return couponCondition;
    }

    public String getCouponCommonCondition()
    {
        return couponCommonCondition;
    }

    public String getCouponPeriod()
    {
        return couponPeriod;
    }

    public String getCouponIssuance()
    {
        return couponIssuance;
    }

    public String getCouponMobileCondition()
    {
        return couponMobileCondition;
    }

    public boolean isHotenaviRoomPriceFlag()
    {
        return hotenaviRoomPriceFlag;
    }

    public void setHotenaviRoomPriceFlag(boolean hotenaviRoomPriceFlag)
    {
        this.hotenaviRoomPriceFlag = hotenaviRoomPriceFlag;
    }

    public String getCustomId()
    {
        return customId;
    }

    public void setCustomId(String customId)
    {
        this.customId = customId;
    }

    /**
     * ホテル詳細データ取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId, UserLoginInfo uli, int appKind)
    {
        boolean ret;
        DataHotelMaster dataHotelMaster;
        DataRsvReserveBasic drrb;
        HotelBasicInfo hotelBasic;
        HotelRemarks hotelRemarks;
        HotenaviRoomPrice hotenaviRoomPrice;
        DataApHotelCustom dahc;
        if ( hotelId <= 0 )
        {
            ret = false;
            return ret;
        }

        dataHotelMaster = new DataHotelMaster();
        hotelBasic = new HotelBasicInfo();
        drrb = new DataRsvReserveBasic();
        hotelRemarks = new HotelRemarks();
        hotenaviRoomPrice = new HotenaviRoomPrice();
        dahc = new DataApHotelCustom();

        drrb.getData( hotelId );
        ret = hotelBasic.getHotelBasicInfo( hotelId );
        hotelBasic.getHotelMessage( hotelId );
        hotelBasic.getHotelPrice( hotelId );
        hotelBasic.getHotelEquipment( hotelId );

        if ( ret != false && hotelBasic.getHotelInfo().getId() > 0 )
        {
            try
            {
                this.hotenaviRoomPriceFlag = hotenaviRoomPrice.checkRoomPrice( hotelBasic.getHotelInfo().getHotenaviId() );
                if ( this.hotenaviRoomPriceFlag != false )
                {
                    // URLが入っていればホテナビ対応。そうでなければ、非対応にする
                    if ( hotelBasic.getHotelInfo().getUrl().equals( "" ) == false )
                    {
                        this.hotenaviRoomPriceFlag = true;
                    }
                    else
                    {
                        this.hotenaviRoomPriceFlag = false;
                    }
                }

                // ホテルのマスタ情報取得
                ret = dataHotelMaster.getData( hotelId );
                if ( dataHotelMaster.getEmptyDispKind() == 1 )
                {
                    // 空室情報のリアルタイム取得の判断
                    if ( dataHotelMaster.getHotenaviEmptyFlag() == 1 )
                    {
                        this.boolEmpty = this.setHotelStatusMessage( hotelId, REAL_TIME, dataHotelMaster.getHotenaviEmptyFlag() );
                    }
                    else
                    {
                        this.boolEmpty = this.setHotelStatusMessage( hotelId, NOT_REAL_TIME, dataHotelMaster.getHotenaviEmptyFlag() );
                    }
                }

                // ホテルの備考情報を取得
                hotelRemarks.getRemarksDataByDispKind( hotelId, ConstantsHotel.DISPKIND_ORDINARY );

                this.name = hotelBasic.getHotelInfo().getName();
                this.id = hotelBasic.getHotelInfo().getId();
                this.rank = hotelBasic.getHotelInfo().getRank();
                this.over18 = hotelBasic.getHotelInfo().getOver18Flag();
                this.rsvFlag = drrb.getSalesFlag();

                if ( this.rank >= 2 )
                {
                    File fileImg = new File( "/happyhotel" + IMAGE_PATH + this.id + "n.jpg" );

                    // リニューアル用の～n.jpgがあるかどうか
                    if ( fileImg.exists() == false )
                    {
                        // 通常の外観画像があるかどうか
                        fileImg = new File( "/happyhotel" + IMAGE_PATH + this.id + "jpg.jpg" );
                        if ( fileImg.exists() == false )
                        {
                            this.image = NO_IMAGE_PATH;
                        }
                        else
                        {
                            this.image = IMAGE_PATH + this.id + "jpg.jpg";
                        }
                    }
                    else
                    {
                        // iPhoneフラグ
                        if ( appKind == IPHONE )
                        {
                            this.image = IMAGE_PATH + this.id + "n.jpg";
                        }
                        // android
                        else
                        {
                            // 通常の外観画像があるかどうか
                            fileImg = new File( "/happyhotel" + IMAGE_PATH + this.id + "jpg.jpg" );
                            if ( fileImg.exists() == false )
                            {
                                this.image = NO_IMAGE_PATH;
                            }
                            else
                            {
                                this.image = IMAGE_PATH + this.id + "jpg.jpg";
                            }
                        }
                    }
                    this.image = Url.getUrl() + image;
                    Logging.info( "[HotelDetail ]uli.isMemberFlag():" + uli.isMemberFlag() );

                    // 顧客コード
                    if ( uli.isMemberFlag() != false )
                    {
                        // ホテナビの顧客ページがあるかを調べる
                        if ( isHotenaviCustom( id ) != false )
                        {
                            Logging.info( "[HotelDetail ]isHotenaviCustom( id ):" + isHotenaviCustom( id ) );

                            this.setCustomId( id, uli.getUserInfo().getUserId() );
                        }
                        else
                        {
                            this.setCustomId( "" );
                        }
                    }

                }
                if ( this.rank >= 3 )
                {
                    // ハピーPR
                    this.setHappiePrMessage( hotelBasic.getHotelInfo() );
                    this.rsvFlag = drrb.getSalesFlag();
                    // 予約販売開始であれば予約用のURLと画像表示
                    if ( this.rsvFlag == 1 )
                    {
                        this.memberImg = RSV_HAPPIE_MEMBER_IMG;
                        this.memberUrl = RSV_HAPPIE_MEMBER_URL;
                        this.memberText = RSV_MEMBER_TEXT;
                    }
                    else
                    {
                        this.memberImg = HAPPIE_MEMBER_IMG;
                        this.memberUrl = HAPPIE_MEMBER_URL;
                        this.memberText = HAPPIE_MEMBER_TEXT;
                    }
                }
                this.message = hotelBasic.getHotelMessage().getDispMessage();
                if ( (hotelBasic.getHotelInfo().getPrDetail().equals( "" ) != false) || (hotelBasic.getHotelInfo().getRank() <= 1) )
                {
                    this.pr = hotelBasic.getHotelInfo().getPr();
                }
                else
                {
                    this.pr = hotelBasic.getHotelInfo().getPrDetail();
                }

                // クチコミ平均のセット
                if ( dataHotelMaster.getBbsConfig() == 1 )
                {
                    this.setKuchikomiAvg( hotelId );
                }
                this.address = hotelBasic.getHotelInfo().getAddressAll();
                this.tel = hotelBasic.getHotelInfo().getTel1();
                this.mapcode = hotelBasic.getHotelInfo().getMapCode();

                // 空満情報は上位で取得済み

                // 部屋総数;
                this.setRoomMessage( hotelBasic.getHotelInfo(), hotelRemarks );

                Logging.info( "imageGallery" );
                // 有料会員ならばルームギャラリーとホテルギャラリーを表示
                if ( uli.isPaymemberFlag() != false )
                {
                    // ルームギャラリー
                    this.setRoomImageMessage( hotelBasic.getHotelInfo() );

                    // ギャラリー
                    this.setGalleryMessage( hotelBasic.getHotelInfo() );
                }
                else
                {
                    this.setRoomImageMessageNoMember( hotelBasic.getHotelInfo() );
                }

                // 駐車場
                this.setParkingMessage( hotelBasic.getHotelInfo(), hotelRemarks );

                // 建物形式
                this.setBuildingTypeMessage( hotelBasic.getHotelInfo(), hotelRemarks );

                // アクセス
                this.setAccessMessage( hotelBasic.getHotelInfo(), hotelRemarks );

                // map情報
                this.lat = hotelBasic.getHotelInfo().getHotelLat();
                this.lon = hotelBasic.getHotelInfo().getHotelLon();

                // URL
                this.setUrl( hotelBasic.getHotelInfo() );

                // 料金
                this.setPriceMessage( hotelBasic );

                // メンバー特典
                this.setPrivilegeMessage( hotelBasic.getHotelInfo(), hotelRemarks );

                // ルームサービス
                this.setRoomServiceMessage( hotelBasic.getHotelInfo(), hotelRemarks );

                // その他サービス
                this.setOtherServiceMessage( hotelBasic.getHotelInfo() );

                // 支払い方法
                this.setCreditMessage( hotelBasic.getHotelInfo(), hotelRemarks );

                // 外出
                this.setHalfwayMessage( hotelBasic.getHotelInfo(), hotelRemarks );

                // 利用人数
                this.setHeadcountMessage( hotelBasic.getHotelInfo(), hotelRemarks );

                // 予約
                this.setReserveMessage( hotelBasic.getHotelInfo(), hotelRemarks );

                // TODO グループリンク 仕様書にないため、今回は非対応

                // 設備
                if ( hotelBasic.getEquipCount() > 0 )
                {
                    this.setEquipMessage( hotelBasic );
                }

                // クーポン
                this.setCouponMessage( hotelBasic.getHotelInfo(), uli );
                // 自動適用クーポン
                this.getAutoCoupon( hotelBasic.getHotelInfo(), uli );
            }
            catch ( Exception e )
            {
                Logging.info( e.toString() );
            }
            ret = true;
        }
        else
        {
            ret = false;
        }
        return ret;
    }

    /**
     * 空室情報のメッセージ
     * 
     * @param hotelId ホテルID
     * @param realTime リアルタイム(0:DBの値を取得、1:リアルタイムで取得)
     * @param hotenaviFlag ホテナビ準拠フラグ
     * @return
     */
    public boolean setHotelStatusMessage(int hotelId, int realTime, int hotenaviFlag)
    {
        boolean ret;
        HotelStatus hs;

        hs = new HotelStatus();
        ret = hs.getData( hotelId, realTime );
        if ( ret != false )
        {
            // 満室でホテナビ非準拠ホテルの場合は満室表示を行わない
            if ( hs.getHotelStatus().getEmptyStatus() == 2 && hotenaviFlag == 0 )
            {
                this.emptyStatus = 0;
            }
            else
            {
                this.emptyStatus = hs.getHotelStatus().getEmptyStatus();
            }
            this.emptyRoomCount = hs.getHotelStatus().getEmpty();
            this.emptyUpdate = hs.getHotelStatus().getLastUpDate();
            this.emptyUptime = hs.getHotelStatus().getLastUpTime();
            this.nomemberText = NOMEMBER_TEXT;
            this.nomemberUrl = NOMEMBER_URL;
        }
        return ret;
    }

    /**
     * 部屋数のメッセージ
     * 
     * @param DataHotelBasic ホテル情報
     * @param HotelRemarks ホテル備考情報
     */
    public void setRoomMessage(DataHotelBasic dhb, HotelRemarks hr)
    {
        String roomMsg = "";
        if ( dhb.getRoomCount() > 0 )
        {
            roomMsg = dhb.getRoomCount() + "室";
            if ( hr.getRemarksCount() > 0 )
            {
                for( int k = 0 ; k < hr.getRemarksCount() ; k++ )
                {
                    if ( hr.getHotelRemarks()[k].getDispNo() == ConstantsHotel.DISPNO_ROOM )
                    {
                        try
                        {
                            roomMsg += "\n" + hr.getHotelRemarks()[k].getDispMessage();
                        }
                        catch ( Exception e )
                        {
                            Logging.error( "[HotelDetail.setRoomMessage()]Exception:" + e.toString() );
                        }
                    }
                }
            }
            this.roomAllCount = roomMsg;
        }
    }

    /***
     * 駐車場メッセージ
     * 
     * @param DataHotelBasic ホテル情報
     * @param HotelRemarks ホテル備考情報
     */
    public void setParkingMessage(DataHotelBasic dhb, HotelRemarks hr)
    {
        String parkingMsg = "";
        if ( dhb.getParking() != 0 )
        {
            if ( dhb.getParking() == 1 )
            {
                parkingMsg = "有り";
                // 駐車場の台数
                if ( dhb.getParkingCount() > 0 )
                {
                    parkingMsg += "：" + dhb.getParkingCount() + "台";
                }
                // ハイルーフ
                if ( dhb.getHighRoof() == 1 )
                {
                    parkingMsg += "\nハイルーフ可";
                    if ( dhb.getHighRoofCount() > 0 )
                    {
                        parkingMsg += "：" + dhb.getHighRoofCount() + "台";
                    }
                }
                else if ( dhb.getHighRoof() == 9 )
                {
                    parkingMsg += "\nハイルーフ不可";
                }

            }
            else if ( dhb.getParking() == 9 )
            {
                parkingMsg = "なし";
            }
            // 備考の取得
            if ( hr.getRemarksCount() > 0 )
            {
                for( int k = 0 ; k < hr.getRemarksCount() ; k++ )
                {
                    if ( hr.getHotelRemarks()[k].getDispNo() == ConstantsHotel.DISPNO_PARKING )
                    {
                        try
                        {
                            parkingMsg += "\n" + hr.getHotelRemarks()[k].getDispMessage();
                        }
                        catch ( Exception e )
                        {
                            Logging.error( "[HotelDetail.setParkingMessage()]Exception:" + e.toString() );
                        }
                    }
                }
            }
            this.parking = parkingMsg;
        }
    }

    /**
     * 建物形式のメッセージ
     * 
     * @param DataHotelBasic ホテル情報
     * @param HotelRemarks ホテル備考情報
     */
    public void setBuildingTypeMessage(DataHotelBasic dhb, HotelRemarks hr)
    {
        String buildingTypeMsg = "";
        try
        {
            if ( dhb.getTypeBuilding() == 1 )
            {
                buildingTypeMsg += "ビル形式 ";
            }
            if ( dhb.getTypeKodate() == 1 )
            {
                buildingTypeMsg += "戸建形式 ";
            }
            if ( dhb.getTypeRentou() == 1 )
            {
                buildingTypeMsg += "連棟形式 ";
            }
            if ( dhb.getTypeBuilding() != 1 && dhb.getTypeKodate() != 1 && dhb.getTypeRentou() != 1 && dhb.getTypeEtc().equals( "" ) == false )
            {
                buildingTypeMsg += dhb.getTypeEtc();
            }
            // 建物形式が登録されており、備考があれば確認する
            if ( buildingTypeMsg.equals( "" ) == false && hr.getRemarksCount() > 0 )
            {
                for( int k = 0 ; k < hr.getRemarksCount() ; k++ )
                {
                    if ( hr.getHotelRemarks()[k].getDispNo() == ConstantsHotel.DISPNO_BUILDING )
                    {
                        buildingTypeMsg += "\n" + hr.getHotelRemarks()[k].getDispMessage();
                    }
                }
            }
            this.buildingType = buildingTypeMsg;
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.setBuildingTypeMessage()]Exception:" + e.toString() );
        }
    }

    /**
     * アクセス形式のメッセージ
     * 
     * @param DataHotelBasic ホテル情報
     * @param HotelRemarks ホテル備考情報
     */
    public void setAccessMessage(DataHotelBasic dhb, HotelRemarks hr)
    {
        String accessMsg = "";
        try
        {
            if ( dhb.getAccess().equals( "" ) == false )
            {
                accessMsg += dhb.getAccess();
            }
            if ( accessMsg.equals( "" ) == false && hr.getRemarksCount() > 0 )
            {
                for( int k = 0 ; k < hr.getRemarksCount() ; k++ )
                {
                    if ( hr.getHotelRemarks()[k].getDispNo() == ConstantsHotel.DISPNO_ACCESS )
                    {
                        accessMsg += "\n" + hr.getHotelRemarks()[k].getDispMessage();
                    }
                }
            }
            this.access = accessMsg;
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.setAccessMessage()]Exception:" + e.toString() );
        }
    }

    /**
     * メンバー特典のメッセージ
     * 
     * @param DataHotelBasic ホテル情報
     * @param HotelRemarks ホテル備考情報
     */
    public void setPrivilegeMessage(DataHotelBasic dhb, HotelRemarks hr)
    {
        String privilegeMsg = "";
        try
        {
            if ( dhb.getBenefit() == 1 )
            {
                privilegeMsg += "あり";
                if ( dhb.getRank() >= 2 )
                {
                    privilegeMsg += "：" + dhb.getPrMember();
                }
            }
            else if ( dhb.getBenefit() == 9 )
            {
                privilegeMsg += "なし";
            }
            if ( privilegeMsg.equals( "" ) == false && hr.getRemarksCount() > 0 )
            {
                for( int k = 0 ; k < hr.getRemarksCount() ; k++ )
                {
                    if ( hr.getHotelRemarks()[k].getDispNo() == ConstantsHotel.DISPNO_PRMEMBER )
                    {
                        privilegeMsg += "\n" + hr.getHotelRemarks()[k].getDispMessage();
                    }
                }
            }
            this.privilege = privilegeMsg;
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.setPlivilegeMessage()]Exception:" + e.toString() );
        }
    }

    /**
     * ルームサービスのメッセージ
     * 
     * @param DataHotelBasic ホテル情報
     * @param HotelRemarks ホテル備考情報
     */
    public void setRoomServiceMessage(DataHotelBasic dhb, HotelRemarks hr)
    {
        String roomServiceMsg = "";
        try
        {
            if ( dhb.getRoomService() == 1 )
            {
                roomServiceMsg += "あり";
                if ( dhb.getRank() >= 2 )
                {
                    roomServiceMsg += "：" + dhb.getPrRoom();
                }
            }
            else if ( dhb.getRoomService() == 9 )
            {
                roomServiceMsg += "なし";
            }
            if ( roomServiceMsg.equals( "" ) == false && hr.getRemarksCount() > 0 )
            {
                for( int k = 0 ; k < hr.getRemarksCount() ; k++ )
                {
                    if ( hr.getHotelRemarks()[k].getDispNo() == ConstantsHotel.DISPNO_ROOMSERVICE )
                    {
                        roomServiceMsg += "\n" + hr.getHotelRemarks()[k].getDispMessage();
                    }
                }
            }
            this.roomService = roomServiceMsg;
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.setRoomServiceMessage()]Exception:" + e.toString() );
        }
    }

    /**
     * その他サービスのメッセージ
     * 
     * @param DataHotelBasic ホテル情報
     * @param HotelRemarks ホテル備考情報
     */
    public void setOtherServiceMessage(DataHotelBasic dhb)
    {
        String otherServiceMsg = "";
        try
        {
            otherServiceMsg += "あり";
            if ( dhb.getRank() >= 2 )
            {
                otherServiceMsg += dhb.getPrEtc();
            }
            this.otherService = otherServiceMsg;
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.setOtherServiceMessage()]Exception:" + e.toString() );
        }
    }

    /**
     * 支払い/クレジットカードのメッセージ
     * 
     * @param DataHotelBasic ホテル情報
     * @param HotelRemarks ホテル備考情報
     */
    public void setCreditMessage(DataHotelBasic dhb, HotelRemarks hr)
    {
        String creditMsg = "";
        try
        {
            if ( dhb.getCompanyType() == 1 || dhb.getCompanyType() == 2 )
            {
                if ( dhb.getPayAuto() == 1 )
                {
                    creditMsg = "自動精算機：有り\n";
                }
                else if ( dhb.getPayAuto() == 9 )
                {
                    creditMsg = "自動精算機：なし\n";
                }
            }
            if ( dhb.getCredit() == 1 )
            {
                creditMsg += "カード：可 ";
            }
            else if ( dhb.getCredit() == 9 )
            {
                creditMsg += "カード：不可 ";
            }
            if ( dhb.getCreditVisa() == 1 )
            {
                creditMsg += "VISA ";
            }
            if ( dhb.getCreditMaster() == 1 )
            {
                creditMsg += "MASTER ";
            }
            if ( dhb.getCreditJcb() == 1 )
            {
                creditMsg += "JCB ";
            }
            if ( dhb.getCreditDc() == 1 )
            {
                creditMsg += "DC ";
            }
            if ( dhb.getCreditNicos() == 1 )
            {
                creditMsg += "NICOS ";
            }
            if ( dhb.getCreditAmex() == 1 )
            {
                creditMsg += "AMEX ";
            }
            if ( dhb.getCreditEtc().compareTo( "" ) != 0 )
            {
                creditMsg += dhb.getCreditEtc();
            }
            if ( creditMsg.equals( "" ) == false && hr.getRemarksCount() > 0 )
            {
                for( int k = 0 ; k < hr.getRemarksCount() ; k++ )
                {
                    if ( hr.getHotelRemarks()[k].getDispNo() == ConstantsHotel.DISPNO_CREDIT )
                    {
                        creditMsg += "\n" + hr.getHotelRemarks()[k].getDispMessage();
                    }
                }
            }
            this.credit = creditMsg;

        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.setCreditMessage()]Exception:" + e.toString() );
        }
    }

    /**
     * 途中外出のメッセージ
     * 
     * @param DataHotelBasic ホテル情報
     * @param HotelRemarks ホテル備考情報
     */
    public void setHalfwayMessage(DataHotelBasic dhb, HotelRemarks hr)
    {
        String halfwayMsg = "";
        try
        {
            if ( dhb.getHalfway() == 1 )
            {
                halfwayMsg += "可";
            }
            else if ( dhb.getRoomService() == 9 )
            {
                halfwayMsg += "不可";
            }
            if ( halfwayMsg.equals( "" ) == false && hr.getRemarksCount() > 0 )
            {
                for( int k = 0 ; k < hr.getRemarksCount() ; k++ )
                {
                    if ( hr.getHotelRemarks()[k].getDispNo() == ConstantsHotel.DISPNO_HALFWAY )
                    {
                        halfwayMsg += "\n" + hr.getHotelRemarks()[k].getDispMessage();
                    }
                }
            }
            this.halfway = halfwayMsg;
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.setHalfwayMessage()]Exception:" + e.toString() );
        }
    }

    /**
     * 利用人数のメッセージ
     * 
     * @param DataHotelBasic ホテル情報
     * @param HotelRemarks ホテル備考情報
     */
    public void setHeadcountMessage(DataHotelBasic dhb, HotelRemarks hr)
    {
        String headcountMsg = "";
        try
        {
            if ( dhb.getPossibleOne() == 9 &&
                    dhb.getPossibleThree() == 9 )
            {
                headcountMsg = "２人のみ";
            }
            if ( dhb.getPossibleOne() == 1 )
            {
                headcountMsg = "１人利用可 ";
            }
            if ( dhb.getPossibleThree() == 1 )
            {
                headcountMsg += "３人以上利用可 ";
            }
            if ( headcountMsg.equals( "" ) == false && hr.getRemarksCount() > 0 )
            {
                for( int k = 0 ; k < hr.getRemarksCount() ; k++ )
                {
                    if ( hr.getHotelRemarks()[k].getDispNo() == ConstantsHotel.DISPNO_USER )
                    {
                        headcountMsg += "\n" + hr.getHotelRemarks()[k].getDispMessage();
                    }
                }
            }
            this.headCount = headcountMsg;
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.setHeadcountMessage()]Exception:" + e.toString() );
        }
    }

    /**
     * 予約のメッセージ
     * 
     * @param DataHotelBasic ホテル情報
     * @param HotelRemarks ホテル備考情報
     */
    public void setReserveMessage(DataHotelBasic dhb, HotelRemarks hr)
    {
        String reserveMsg = "";
        String addMsg = "";
        try
        {
            if ( dhb.getReserve() == 1 || dhb.getReserveTel() == 1 ||
                    dhb.getReserveMail() == 1 || dhb.getReserveWeb() == 1 )
            {
                reserveMsg = "可 ";
                reserveMsg = reserveMsg + "（ ";
                if ( dhb.getReserveTel() == 1 )
                    reserveMsg = reserveMsg + "電話予約 ";
                if ( dhb.getReserveMail() == 1 )
                    reserveMsg = reserveMsg + "メール予約 ";
                if ( dhb.getReserveWeb() == 1 )
                    reserveMsg = reserveMsg + "WEB予約 ";
                reserveMsg = reserveMsg + "） ";
            }
            else if ( dhb.getReserve() == 9 )
            {
                reserveMsg = "不可";
            }
            // 予約可出来る場合の注意事項
            if ( reserveMsg.compareTo( "不可" ) != 0 && reserveMsg.compareTo( "" ) != 0 && reserveMsg.compareTo( "" ) == 0 )
            {
                addMsg = "\n条件付きの場合がありますので、詳しくはホテルにご確認ください。";
            }
            // 備考の取得
            if ( reserveMsg.equals( "" ) == false && hr.getRemarksCount() > 0 )
            {
                for( int k = 0 ; k < hr.getRemarksCount() ; k++ )
                {
                    if ( hr.getHotelRemarks()[k].getDispNo() == ConstantsHotel.DISPNO_RESERVE )
                    {
                        reserveMsg += "\n" + hr.getHotelRemarks()[k].getDispMessage();
                    }
                }
            }
            this.reserve = reserveMsg + addMsg;
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.setReserveMessage()]Exception:" + e.toString() );
        }
    }

    /**
     * ハピーPRのメッセージ
     * 
     * @param hotelId
     */
    public void setHappiePrMessage(DataHotelBasic dhb)
    {
        boolean ret;
        HotelHappie hh;
        hh = new HotelHappie();

        try
        {
            if ( dhb.getRank() >= 3 )
            {
                ret = hh.getData( dhb.getId() );
                if ( ret != false )
                {
                    this.happiePr = hh.getHotelHappie().getName();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.setHappiePr()]Exception:" + e.toString() );
        }
    }

    /**
     * クチコミ平均のメッセージをセット
     * 
     * @param hotelId
     */
    public void setKuchikomiAvg(int hotelId)
    {
        final int BBS_FLAG = 1;
        boolean ret;
        HotelBbs bbs;
        BigDecimal pointAverage;
        int integerAverage;
        int decimalAverage;

        bbs = new HotelBbs();

        ret = bbs.getBbsList( hotelId, 1, 0, BBS_FLAG );
        if ( ret != false )
        {
            pointAverage = bbs.getPointAverageByDecimal( hotelId );
            if ( pointAverage != null && bbs.getBbsAllCount() > 0 )
            {
                pointAverage = pointAverage.movePointRight( 2 );
                integerAverage = pointAverage.intValue() / 100;
                decimalAverage = pointAverage.intValue() % 100;
                pointAverage = pointAverage.movePointLeft( 2 );
                if ( decimalAverage >= 50 )
                {
                    integerAverage++;
                }
                if ( integerAverage > 0 )
                {
                    this.kuchikomiAvg = String.format( "%1$01d.0", integerAverage );
                }
                else
                {
                    this.kuchikomiAvg = "";
                }
            }
            else
            {
                this.kuchikomiAvg = "0.0";
            }
        }
        else
        {
            this.kuchikomiAvg = "";
        }
    }

    /**
     * URLのメッセージ
     * 
     * @param DataHotelBasic ホテル情報
     */
    public void setUrl(DataHotelBasic dhb)
    {
        String urlMsg = "";
        String urlTextMsg = "";
        try
        {
            this.url = new ArrayList<String>();
            this.urlText = new ArrayList<String>();

            if ( dhb.getUrl().equals( "" ) == false && (dhb.getUrl().indexOf( "http://" ) > -1 || dhb.getUrl().indexOf( "https://" ) > -1) )
            {
                urlMsg = dhb.getUrl();
                urlTextMsg = dhb.getUrl();
                this.url.add( urlMsg );
                this.urlText.add( urlTextMsg );
            }
            if ( dhb.getUrlOfficial1().equals( "" ) == false && (dhb.getUrlOfficial1().indexOf( "http://" ) > -1 || dhb.getUrlOfficial1().indexOf( "https://" ) > -1) )
            {
                urlMsg = dhb.getUrlOfficial1();
                if ( urlMsg.indexOf( "http://" ) != -1 )
                {
                    urlMsg = urlMsg.replace( "http://", "" );
                    urlMsg = IDN.toASCII( urlMsg );
                    urlMsg = "http://" + urlMsg;
                }
                if ( urlMsg.indexOf( "https://" ) != -1 )
                {
                    urlMsg = urlMsg.replace( "https://", "" );
                    urlMsg = IDN.toASCII( urlMsg );
                    urlMsg = "https://" + urlMsg;
                }
                urlTextMsg = dhb.getUrlOfficial1();
                this.url.add( urlMsg );
                this.urlText.add( urlTextMsg );
            }
            if ( dhb.getUrlOfficial2().equals( "" ) == false && (dhb.getUrlOfficial2().indexOf( "http://" ) > -1 || dhb.getUrlOfficial2().indexOf( "https://" ) > -1) )
            {
                urlMsg = dhb.getUrlOfficial2();
                urlTextMsg = dhb.getUrlOfficial2();
                this.url.add( urlMsg );
                this.urlText.add( urlTextMsg );
            }
            if ( dhb.getUrlOfficialMobile().equals( "" ) == false && (dhb.getUrlOfficialMobile().indexOf( "http://" ) > -1 || dhb.getUrlOfficialMobile().indexOf( "https://" ) > -1) )
            {
                urlMsg = dhb.getUrlOfficialMobile();
                urlTextMsg = dhb.getUrlOfficialMobile();
                this.url.add( urlMsg );
                this.urlText.add( urlTextMsg );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.setUrl()]Exception:" + e.toString() );
        }
    }

    /**
     * 料金のメッセージ
     * 
     * @param DataHotelBasic ホテル情報
     */
    public void setPriceMessage(HotelBasicInfo hbi)
    {
        final int TAX_CAHGE_DATE = 20140401;
        int i;
        int datatype = 0;
        int count = 0;
        boolean ret = false;
        HotelRemarks hr;
        String titleName = "";
        String weekName = "";
        String valueName = "";
        String valueNameTo = "";
        String remarkValue = "";
        NumberFormat nf;
        ArrayList<String> valueList = null;

        nf = new DecimalFormat( "00" );
        hr = new HotelRemarks();

        // 料金の備考は別なのでメソッド内で取得する
        ret = hr.getRemarksDataByDispKind( hbi.getHotelInfo().getId(), ConstantsHotel.DISPKIND_PRICE );

        try
        {
            // それぞれの項目を初期化
            this.hotelPriceName = new ArrayList<String>(); // 休憩、宿泊などのタイトル
            this.hotelPriceMessage = new ArrayList<ArrayList<String>>();// 曜日や料金などを追加
            this.hotelPriceRemarks = new ArrayList<String>();// 備考
            valueList = new ArrayList<String>();

            for( i = 0 ; i < hbi.getPriceCount() ; i++ )
            {
                // タイトルが違っていたらタイトルを追加
                if ( titleName.equals( "" ) != false || titleName.equals( hbi.getHotelPrice()[i].getTitle() ) == false )
                {
                    weekName = "";
                    titleName = hbi.getHotelPrice()[i].getTitle();
                    this.hotelPriceName.add( titleName );
                    if ( valueList.size() > 0 )
                    {
                        this.hotelPriceMessage.add( valueList );
                    }
                    valueList = null;
                    valueList = new ArrayList<String>();

                    datatype = hbi.getHotelPrice()[i].getDataType();
                    count++;

                    if ( hbi.getHotelPrice()[i].getStartDate() >= TAX_CAHGE_DATE )
                    {
                        remarkValue += "※表示金額は税込です\n";
                    }
                    else
                    {
                        remarkValue += "※表示金額は税込です\n";
                    }
                    // 備考を取得する
                    if ( ret != false )
                    {
                        if ( hr.getRemarksCount() > 0 )
                        {
                            for( int k = 0 ; k < hr.getRemarksCount() ; k++ )
                            {
                                // 休憩の備考
                                if ( (datatype == 1 || datatype == 2) && hr.getHotelRemarks()[k].getDispNo() == ConstantsHotel.DISPNO_BREAK_FEE )
                                {
                                    remarkValue += hr.getHotelRemarks()[k].getDispMessage() + "\n";
                                }
                                // フリータイムの備考
                                if ( (datatype == 3 || datatype == 4) && hr.getHotelRemarks()[k].getDispNo() == ConstantsHotel.DISPNO_FREETIME_FEE )
                                {
                                    remarkValue += hr.getHotelRemarks()[k].getDispMessage() + "\n";
                                }
                                // 宿泊の備考
                                if ( (datatype == 5 || datatype == 6) && hr.getHotelRemarks()[k].getDispNo() == ConstantsHotel.DISPNO_LODGMENT_FEE )
                                {
                                    remarkValue += hr.getHotelRemarks()[k].getDispMessage() + "\n";
                                }
                            }
                        }
                    }
                    // 必ず追加する（数を合わせる）
                    this.hotelPriceRemarks.add( remarkValue );
                    remarkValue = "";
                }

                // 曜日名判定
                if ( weekName.equals( "" ) != false || weekName.equals( hbi.getHotelPrice()[i].getWeek() ) == false )
                {
                    weekName = hbi.getHotelPrice()[i].getWeek();
                    valueName += "【" + weekName + "】\n";
                }

                valueName += "◇";
                if ( hbi.getHotelPrice()[i].getTimeFrom() == 0 && hbi.getHotelPrice()[i].getTimeTo() == 2400 )
                {
                    valueName += "24時間制";
                }
                else
                {
                    // 開始時刻判定
                    if ( hbi.getHotelPrice()[i].getTimeFrom() < 400 && hbi.getHotelPrice()[i].getTimeFrom() != 0 )
                    {
                        valueName += "翌 ";
                    }
                    else
                    {
                        valueName += "";
                    }

                    // 100で割り切れるかの判定
                    // if( (hbi.getHotelPrice()[i].getTimeFrom() % 100) == 0 )
                    if ( hbi.getHotelPrice()[i].getTimeFrom() == 0 )
                    {
                        valueName += hbi.getHotelPrice()[i].getTimeFrom() / 100 + "時～";
                    }
                    else
                    {
                        valueName += hbi.getHotelPrice()[i].getTimeFrom() / 100 + ":" + nf.format( hbi.getHotelPrice()[i].getTimeFrom() % 100 ) + "～";
                    }

                    // 終了時刻判定
                    if ( hbi.getHotelPrice()[i].getTimeFrom() > hbi.getHotelPrice()[i].getTimeTo() )
                    {
                        valueName += "翌 ";
                    }

                    // 100で割り切れるかの判定
                    // if( ( hbi.getHotelPrice()[i].getTimeTo() % 100) == 0 )
                    if ( hbi.getHotelPrice()[i].getTimeTo() == 0 )
                    {
                        valueName += hbi.getHotelPrice()[i].getTimeTo() / 100 + "時";
                    }
                    else
                    {
                        valueName += hbi.getHotelPrice()[i].getTimeTo() / 100 + ":" + nf.format( hbi.getHotelPrice()[i].getTimeTo() % 100 );
                    }
                }

                // 文言
                if ( hbi.getHotelPrice()[i].getTimeFlag() == 1 )
                {
                    valueName += "ﾁｪｯｸｲﾝより ";
                }
                else if ( hbi.getHotelPrice()[i].getTimeFlag() == 2 )
                {
                    valueName += "の間で最大 ";
                }
                else
                {
                    valueName += "";
                }

                if ( (hbi.getHotelPrice()[i].getTimeSpan() % 60) == 0 )
                {
                    valueName += (hbi.getHotelPrice()[i].getTimeSpan() / 60) + "時間";
                }
                else
                {
                    if ( hbi.getHotelPrice()[i].getTimeSpan() / 60 == 0 )
                    {
                        valueName += (hbi.getHotelPrice()[i].getTimeSpan() % 60) + "分間";
                    }
                    else
                    {
                        valueName += (hbi.getHotelPrice()[i].getTimeSpan() / 60) + "時間";
                        valueName += (hbi.getHotelPrice()[i].getTimeSpan() % 60) + "分";
                    }
                }

                if ( hbi.getHotelPrice()[i].getTimeFlag() == 1 || hbi.getHotelPrice()[i].getTimeFlag() == 2 )
                {
                    valueName += "ご利用";
                }

                valueName += "￥" + hbi.getHotelPrice()[i].getPriceFrom();
                valueNameTo = "～￥" + hbi.getHotelPrice()[i].getPriceTo();

                // 最大料金判定
                if ( hbi.getHotelPrice()[i].getMaxPriceDisp() == 1 )
                {
                    valueNameTo = "～";
                }
                else
                {
                    if ( hbi.getHotelPrice()[i].getPriceFrom() == hbi.getHotelPrice()[i].getPriceTo() )
                    {
                        valueNameTo = "均一";
                    }
                }
                valueName += valueNameTo;
                valueName += "\n";

                // 料金情報が入力されていたら追加する
                if ( valueName.equals( "" ) == false )
                {
                    valueList.add( valueName );
                }
                valueName = "";
            }
            // 最後に途中まで追加した料金リストを追加する
            if ( valueList.size() > 0 )
            {
                this.hotelPriceMessage.add( valueList );
            }

            // 料金の備考を最後に追加する
            if ( hr.getRemarksCount() > 0 )
            {
                remarkValue = "";
                for( int k = 0 ; k < hr.getRemarksCount() ; k++ )
                {
                    if ( hr.getHotelRemarks()[k].getDispNo() == ConstantsHotel.DISPNO_FEE )
                    {
                        remarkValue += hr.getHotelRemarks()[k].getDispMessage() + "\n";
                    }
                }
                if ( remarkValue.equals( "" ) == false )
                {
                    // 変換する
                    remarkValue = this.hotelPriceRemarks.get( this.hotelPriceRemarks.size() - 1 ) + remarkValue;
                    // 最後のリストを削除
                    this.hotelPriceRemarks.remove( this.hotelPriceRemarks.size() - 1 );
                    this.hotelPriceRemarks.add( remarkValue );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.setPriceMessage()]Exception:" + e.toString() );
        }
    }

    /**
     * 設備のメッセージ
     * 
     * @param DataHotelBasic ホテル情報
     */
    public void setEquipMessage(HotelBasicInfo hbi)
    {
        int[] DISPNO_EQUIP = new int[6];
        DISPNO_EQUIP[1] = 21; // 設備バス
        DISPNO_EQUIP[2] = 22; // 設備テレビ
        DISPNO_EQUIP[3] = 23; // 設備アミューズメント
        DISPNO_EQUIP[4] = 24; // 設備お部屋
        DISPNO_EQUIP[5] = 19; // 設備

        int i;
        int getInputFlag6 = 0;
        boolean ret;
        String strEquip;
        String strEquipMessage;
        String strEquipRemark;
        ArrayList<String> strEquipMessageList;
        DataMasterName dmn;
        HotelRemarks hr;

        strEquip = "";
        strEquipMessage = "";
        strEquipRemark = "";
        dmn = new DataMasterName();
        hr = new HotelRemarks();

        // 設備の備考は別なのでメソッド内で取得する
        ret = hr.getRemarksDataByDispKind( hbi.getHotelInfo().getId(), ConstantsHotel.DISPKIND_EQUIP );

        getInputFlag6 = hbi.getEquipName()[0].getInputFlag6();
        dmn.getData( 1, getInputFlag6 );
        getInputFlag6 = 0;

        try
        {
            this.equipName = new ArrayList<String>();
            this.equipMessage = new ArrayList<ArrayList<String>>();
            this.equipRemarks = new ArrayList<String>();
            strEquipMessageList = new ArrayList<String>();

            for( i = 0 ; i < hbi.getEquipCount() ; i++ )
            {
                // strEuipMessageに値が入っていたら追加する
                if ( strEquipMessage.equals( "" ) == false )
                {
                    strEquipMessageList.add( strEquipMessage );
                    strEquipMessage = "";
                }

                strEquipMessage = hbi.getEquipName()[i].getName();
                // その設備を表示するかどうかの判断、販売・一部・レンタルの判断(空白扱いは非表示)
                if ( (hbi.getHotelInfo().getCompanyType() < 1 || hbi.getHotelInfo().getCompanyType() > 4) && hbi.getEquipName()[i].getInputFlag7() == 1 )
                {
                    strEquipMessage = "";
                }
                else if ( hbi.getHotelEquip()[i].getEquipType() == 1 )
                {
                    if ( hbi.getEquipName()[i].getBranchName1().equals( "販売" ) )
                    {
                        strEquipMessage += "(販売)";
                    }
                }
                else if ( hbi.getHotelEquip()[i].getEquipType() == 2 )
                {
                    strEquipMessage += "(一部)";
                }
                else if ( hbi.getHotelEquip()[i].getEquipType() == 3 )
                {
                    if ( hbi.getHotelEquip()[i].getEquipRental() == 1 )
                    {
                        strEquipMessage += "(レンタル)";
                    }
                    else if ( hbi.getHotelEquip()[i].getEquipRental() == 2 )
                    {
                        // strEquipMessage = strEquipMessage;
                    }
                }
                else
                {
                    strEquipMessage = "";
                }

                // 設備タイプの判断
                if ( (hbi.getHotelEquip()[i].getEquipType() > 0) && (hbi.getHotelEquip()[i].getEquipType() < 9) )
                {
                    // 詳細ページでの表示判断
                    if ( (hbi.getEquipName()[i].getInputFlag5() == 1) || (hbi.getEquipName()[i].getInputFlag5() == 2) )
                    {
                        // 設備グループ番号が違っていたら設備名を追加する
                        if ( getInputFlag6 != hbi.getEquipName()[i].getInputFlag6() )
                        {

                            // 設備のグループ番号を取得
                            getInputFlag6 = hbi.getEquipName()[i].getInputFlag6();
                            dmn.getData( 1, getInputFlag6 );
                            // 設備の大項目
                            strEquip = "◆" + dmn.getNameShort();

                            // 備考の取得
                            if ( ret != false )
                            {
                                if ( hr.getRemarksCount() > 0 && getInputFlag6 > 0 )
                                {
                                    for( int k = 0 ; k < hr.getRemarksCount() ; k++ )
                                    {
                                        // 備考が一致したら追加する
                                        if ( hr.getHotelRemarks()[k].getDispNo() == DISPNO_EQUIP[getInputFlag6] )
                                        {
                                            if ( hr.getHotelRemarks()[k].getDispMessage().compareTo( "" ) != 0 )
                                            {
                                                strEquipRemark += "\n*" + hr.getHotelRemarks()[k].getDispMessage();
                                            }
                                        }
                                    }
                                    this.equipRemarks.add( strEquipRemark );
                                    strEquipRemark = "";
                                }
                                else
                                {
                                    // 数合わせのために空白でも追加する
                                    this.equipRemarks.add( "" );
                                }
                            }

                            this.equipName.add( strEquip );
                            if ( strEquipMessageList.size() > 0 )
                            {
                                this.equipMessage.add( strEquipMessageList );
                            }
                            strEquipMessageList = null;
                            strEquipMessageList = new ArrayList<String>();
                        }
                    }
                }
            }

            // 最後に追加もれのstrEuipMessageに値が入っていたら追加する
            if ( strEquipMessage.equals( "" ) == false )
            {
                strEquipMessageList.add( strEquipMessage );
                strEquipMessage = "";
            }

            // 最後に追加もれのリストを変更する
            if ( strEquipMessageList != null )
            {
                if ( strEquipMessageList.size() > 0 )
                {
                    this.equipMessage.add( strEquipMessageList );
                }
            }

            // 備考の取得（一番最後の備考に追加する）
            // if ( ret != false )
            // {
            // if ( hr.getRemarksCount() > 0 )
            // {
            // for( int k = 0 ; k < hr.getRemarksCount() ; k++ )
            // {
            // if ( hr.getHotelRemarks()[k].getDispNo() == DISPNO_EQUIP[5] )
            // {
            // if ( hr.getHotelRemarks()[k].getDispMessage().compareTo( "" ) != 0 )
            // {
            // strEquipRemark += "\n*" + hr.getHotelRemarks()[k].getDispMessage();
            // }
            // }
            // }
            // if ( strEquipRemark.equals( "" ) == false )
            // {
            // strEquipRemark = this.equipRemarks.get( this.equipRemarks.size() - 1 ) + strEquipRemark;
            //
            // // 最後のリストを削除HotelBbs
            // this.equipRemarks.remove( this.equipRemarks.size() - 1 );
            // this.equipRemarks.add( strEquipRemark );
            //
            // }
            // }
            // }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.setEquipMessage()]Exception:" + e.toString() );
        }
    }

    /**
     * ホテルの部屋画像をセットする
     * 
     * @param DataHotelBasic
     */
    public void setRoomImageMessage(DataHotelBasic dhb)
    {
        HotelRoom hr;
        HotelRoomMore hrm;

        if ( dhb.getRank() >= 2 )
        {
            hr = new HotelRoom();
            hrm = new HotelRoomMore();

            hrm.getRoomImageData( dhb.getId(), 0 );
            // データがあれば、hotelRoomMoreから取得、なければ3部屋データから取得
            if ( hrm.getHotelRoomCount() > 0 )
            {
                this.getRoomMoreData( dhb, hrm );
                // 部屋の画像がまったくない場合
                if ( this.roomCount == 0 )
                {
                    hr.getRoomData( dhb.getId(), 0 );
                    this.getRoomData( dhb, hr );
                }
            }
            else
            {
                hr.getRoomData( dhb.getId(), 0 );
                if ( hr.getHotelRoomCount() > 0 )
                {
                    this.getRoomData( dhb, hr );
                }
                else
                {
                    return;
                }
            }
        }
    }

    /**
     * ホテルの部屋画像をセットする
     * 
     * @param DataHotelBasic
     */
    public void setRoomImageMessageNoMember(DataHotelBasic dhb)
    {
        HotelRoom hr;

        if ( dhb.getRank() >= 2 )
        {
            hr = new HotelRoom();

            hr.getRoomData( dhb.getId(), 0 );
            if ( hr.getHotelRoomCount() > 0 )
            {
                this.getRoomData( dhb, hr );
            }
            else
            {
                return;
            }
        }
    }

    /***
     * 部屋のデータを取得する（hh_hotel_roomから）
     * 
     * @param DataHotelBasic ホテル情報
     * @param HotelRemarks ホテル備考情報
     */
    public void getRoomData(DataHotelBasic dhb, HotelRoom hr)
    {
        int i;
        String roomSeq;
        String[] roomName;
        String[] roomUrl;
        String[] roomText;
        String[] roomTextUrl;

        if ( hr.getHotelRoomCount() > 0 )
        {
            roomName = new String[hr.getHotelRoomCount()];
            roomUrl = new String[hr.getHotelRoomCount()];
            roomText = new String[hr.getHotelRoomCount()];
            roomTextUrl = new String[hr.getHotelRoomCount()];

            try
            {
                for( i = 0 ; i < hr.getHotelRoomCount() ; i++ )
                {
                    // 初期化
                    roomName[i] = new String();
                    roomUrl[i] = new String();
                    roomText[i] = new String();
                    roomTextUrl[i] = new String();

                    roomName[i] = hr.getHotelRoom()[i].getRoomName();
                    // 部屋名をセット
                    if ( roomName[i].matches( "[0-9]+" ) )
                    {
                        roomName[i] += "号室";
                    }

                    // 画像URLの参照値を判別
                    if ( hr.getHotelRoom()[i].getReferName() != null && hr.getHotelRoom()[i].getReferName().compareTo( "" ) != 0 )
                    {
                        roomSeq = hr.getHotelRoom()[i].getReferName();
                    }
                    else
                    {
                        roomSeq = hr.getHotelRoom()[i].getRoomName();
                    }

                    // 画像URLをセット
                    File file = new File( "/happyhotel/common/room/" + dhb.getHotenaviId() + "/image/r" + roomSeq + ".jpg" );
                    if ( file.exists() != false )
                    {
                        roomUrl[i] = "/common/room/" + dhb.getHotenaviId() + "/image/r" + roomSeq + ".jpg";
                    }
                    else if ( dhb.getUrl().compareTo( "" ) != 0 && (hr.getHotelRoom()[i].getReferName().compareTo( "none" ) != 0) )
                    {
                        roomUrl[i] = "http://www.hotenavi.com/" + dhb.getHotenaviId() + "/image/r" + roomSeq + ".jpg";
                    }
                    else if ( (dhb.getUrl().compareTo( "" ) == 0) && (hr.getHotelRoom()[i].getReferName().compareTo( "none" ) != 0) )
                    {
                        roomUrl[i] = "/common/room/" + dhb.getHotenaviId() + "/image/r" + roomSeq + ".jpg";
                    }
                    else
                    {
                        // TODO サーブレットでしか画像表示していないところは表示できない。
                        roomUrl[i] = Url.getUrl() + "/servlet/HotelRoomPicture?id=" + dhb.getId() + "&amp;amp;amp;seq=" + hr.getHotelRoom()[i].getSeq();
                        // roomUrl[i] = "/common/images/noimage.jpg";
                    }

                    // 部屋説明を取得
                    // / roomText[i] = hr.getHotelRoom()[i].getRoomText();
                    if ( this.hotenaviRoomPriceFlag != false )
                    {
                        roomText[i] = "WEB版を表示[部屋料金表示有り]";
                    }
                    else
                    {
                        roomText[i] = "WEB版を表示";
                    }
                    roomTextUrl[i] = HAPPYHOTEL_URL + "hotel_roomdetails.jsp?hotel_id=" + dhb.getId() + "&seq=" + hr.getHotelRoom()[i].getSeq();

                }
                this.roomCount = hr.getHotelRoomCount();
                this.roomName = roomName;
                this.roomImage = roomUrl;
                this.roomText = roomText;
                this.roomTextUrl = roomTextUrl;
            }
            catch ( Exception e )
            {
                Logging.info( "[HotelDetail.getRoomData()]Exception():" + e.toString() );
            }
        }
    }

    /***
     * 部屋のデータを取得する（hh_hotel_room_moreから）
     * 
     * @param DataHotelBasic ホテル情報
     * @param HotelRemarks ホテル備考情報
     */
    public void getRoomMoreData(DataHotelBasic dhb, HotelRoomMore hrm)
    {
        int count = 0;
        int i;
        String picPath;
        String[] roomName;
        String[] roomUrl;
        String[] roomText;
        String[] roomTextUrl;

        if ( hrm.getHotelRoomCount() > 0 )
        {
            roomName = new String[hrm.getHotelRoomCount()];
            roomUrl = new String[hrm.getHotelRoomCount()];
            roomText = new String[hrm.getHotelRoomCount()];
            roomTextUrl = new String[hrm.getHotelRoomCount()];

            try
            {
                for( i = 0 ; i < hrm.getHotelRoomCount() ; i++ )
                {
                    if ( hrm.getHotelRoom()[i].getReferName() != null && hrm.getHotelRoom()[i].getReferName().compareTo( "" ) != 0 )
                    {
                        // 初期化
                        roomName[i] = new String();
                        roomUrl[i] = new String();
                        roomText[i] = new String();
                        roomTextUrl[i] = new String();

                        roomName[i] = hrm.getHotelRoom()[i].getRoomName();
                        // 部屋名をセット
                        if ( roomName[i].matches( "[0-9]+" ) )
                        {
                            roomName[i] += "号室";
                        }

                        picPath = "/hotenavi/" + dhb.getHotenaviId() + "/image/r" + hrm.getHotelRoom()[i].getReferName() + ".jpg";

                        // 画像URLをセット
                        File file = new File( "/happyhotel" + picPath );
                        if ( file.exists() != false )
                        {
                            roomUrl[i] = picPath;
                        }
                        else if ( dhb.getUrl().compareTo( "" ) != 0 && (hrm.getHotelRoom()[i].getReferName().compareTo( "none" ) != 0) )
                        {
                            roomUrl[i] = "http://www.hotenavi.com/" + dhb.getHotenaviId() + "/image/r" + roomName[i] + ".jpg";
                        }
                        else
                        {
                            roomUrl[i] = "/common/images/noimage.jpg";
                        }

                        // 部屋説明を取得
                        roomText[i] = hrm.getHotelRoom()[i].getRoomText();
                        if ( this.hotenaviRoomPriceFlag != false )
                        {
                            roomText[i] = "WEB版を表示[部屋料金表示有り]";
                        }
                        else
                        {
                            roomText[i] = "WEB版を表示";
                        }
                        roomTextUrl[i] = HAPPYHOTEL_URL + "hotel_roomdetails.jsp?hotel_id=" + dhb.getId() + "&seq=" + hrm.getHotelRoom()[i].getSeq() + "&more=true";

                        count++;
                    }
                }
                if ( count > 0 )
                {
                    this.roomCount = hrm.getHotelRoomCount();
                    this.roomName = roomName;
                    this.roomImage = roomUrl;
                    this.roomText = roomText;
                    this.roomTextUrl = roomTextUrl;
                }
                else
                {
                    this.roomCount = count;
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[HotelDetail.getRoomData()]Exception():" + e.toString() );
            }
        }
    }

    /***
     * ギャラリーのデータを取得する（hh_hotel_room_moreから）
     * 
     * @param DataHotelBasic ホテル情報
     */
    public void setGalleryMessage(DataHotelBasic dhb)
    {
        int i;
        int j;
        HotelGallery hg;
        HotelGallery hgGallery;
        String picName;
        String picPath;
        ArrayList<String> name;
        ArrayList<String> url;
        ArrayList<String> text;
        ArrayList<String> textUrl;

        hg = new HotelGallery();
        hgGallery = new HotelGallery();

        // カテゴリ別にデータを取得
        hg.getCategoryData( dhb.getId(), 0 );
        if ( hg.getHotelCategoryCount() > 0 )
        {
            try
            {
                this.hotelGallery = new ArrayList<DataHotelGallery>();
                this.galleryCount = new ArrayList<Integer>();
                this.galleryName = new ArrayList<ArrayList<String>>();
                this.galleryImage = new ArrayList<ArrayList<String>>();
                this.galleryText = new ArrayList<ArrayList<String>>();
                this.galleryTextUrl = new ArrayList<ArrayList<String>>();

                name = new ArrayList<String>();
                url = new ArrayList<String>();
                text = new ArrayList<String>();
                textUrl = new ArrayList<String>();

                // カテゴリごとにデータを取得する
                for( i = 0 ; i < hg.getHotelCategoryCount() ; i++ )
                {
                    this.hotelGallery.add( hg.getHotelCategory()[i] );
                    // カテゴリーごとの画像数
                    this.galleryCount.add( hg.getHotelCategoryCountSub()[i] );

                    hgGallery.getGalleryData( dhb.getId(), hg.getHotelCategory()[i].getCategory() );
                    // カテゴリーごとに登録されているホテルギャラリーの画像データを取得
                    for( j = 0 ; j < hgGallery.getHotelGalleryCount() ; j++ )
                    {
                        if ( hgGallery.getHotelGallery()[j].getReferName() != null && hgGallery.getHotelGallery()[j].getReferName().compareTo( "" ) != 0 )
                        {
                            picName = hgGallery.getHotelGallery()[j].getRoomName();
                            // ギャラリーが数字のみだったら号室を追加
                            if ( picName.matches( "[0-9]+" ) )
                            {
                                picName = picName + "号室";
                            }
                            // URLを取得
                            picPath = "/common/room/" + dhb.getHotenaviId() + "/gallery/g" + hgGallery.getHotelGallery()[j].getReferName() + ".jpg";

                            File file = new File( "/happyhotel" + picPath );
                            if ( file.exists() != false )
                            {
                                name.add( picName );
                                url.add( picPath );
                                if ( hgGallery.getHotelGallery()[j].getRoomText().compareTo( "" ) != 0 )
                                {
                                    // text.add( hgGallery.getHotelGallery()[j].getRoomText() );
                                    text.add( "WEB版を表示" );
                                }
                                else
                                {
                                    text.add( "WEB版を表示" );
                                }
                                textUrl.add( HAPPYHOTEL_URL + "hotel_gallerydetails.jsp?hotel_id=" + dhb.getId() + "&seq=" + hgGallery.getHotelGallery()[j].getSeq() );
                            }
                        }
                    }
                    if ( name != null && url != null && text != null )
                    {
                        this.galleryName.add( name );
                        this.galleryImage.add( url );
                        this.galleryText.add( text );
                        this.galleryTextUrl.add( textUrl );

                        name = null;
                        url = null;
                        text = null;
                        textUrl = null;
                    }

                    name = new ArrayList<String>();
                    url = new ArrayList<String>();
                    text = new ArrayList<String>();
                    textUrl = new ArrayList<String>();
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[HotelDetail.setGalleryMessage()]Exception:" + e.toString() );
            }
        }
    }

    /***
     * クーポンのデータを取得する
     * 
     * @param DataHotelBasic ホテル情報
     */
    public void setCouponMessage(DataHotelBasic dhb, UserLoginInfo uli)
    {
        final int PRINT_DATE = 0;
        final String COUPON_IMAGE = "/common/image/coupon.gif";
        boolean ret = false;
        boolean memberFlag;
        boolean boolAvailable = false;
        boolean boolUsed = false;
        boolean boolReUse = true;
        boolean boolPrint = false;
        String userStatus = "UA";
        int nType = 1;

        DataMasterCoupon dmc;
        HotelCoupon coupon;
        UserCoupon uc;

        boolUsed = false;
        boolReUse = true;
        memberFlag = uli.isMemberFlag();
        coupon = new HotelCoupon();
        dmc = new DataMasterCoupon();
        uc = new UserCoupon();

        try
        {
            if ( memberFlag != false && uli.getUserInfo().getRegistStatus() == 9 )
            {
                ret = coupon.getMobileCouponData( dhb.getId(), 1 );
                // スタンダードコース以上
                if ( dhb.getRank() >= 2 )
                {
                    // 携帯表示可能
                    if ( coupon.getHotelCoupon().getDispMobile() == 1 )
                    {
                        // ﾃﾞｰﾀﾏｽﾀｰｸｰﾎﾟﾝを取得
                        dmc.getData( coupon.getHotelCoupon().getId(), coupon.getHotelCoupon().getSeq(), nType );
                        // ｸｰﾎﾟﾝのｼﾘｱﾙ番号を取得(ﾕｰｻﾞｸｰﾎﾟﾝ)
                        ret = uc.getUserCoupon( coupon.getHotelCoupon().getId(), dmc.getSeq(), uli.getUserInfo().getUserId(), PRINT_DATE );
                        if ( ret == false )
                        {
                            if ( uli.getUserInfo().getUserId().compareTo( "" ) != 0 )
                            {
                                boolPrint = true;
                            }
                        }
                        else
                        {
                            // ｸｰﾎﾟﾝが表示可能か(発行したｸｰﾎﾟﾝが有効期限内であり、未使用のものを表示可能とする)
                            if ( Integer.parseInt( DateEdit.getDate( 2 ) ) <= uc.getCouponInfo().getEndDate() && uc.getCouponInfo().getUsedFlag() == 0 )
                            {
                                // 管理番号があっているか？ 違っていたら新しくｼﾘｱﾙ番号を発行する
                                if ( uc.getCouponInfo().getSeq() == dmc.getSeq() && dmc.getCouponNo() == coupon.getHotelCoupon().getSeq() )
                                {
                                    boolAvailable = true;
                                }
                                else
                                {
                                    boolPrint = true;
                                }
                            }
                            else
                            {
                                // ｸｰﾎﾟﾝ再発行が可能か
                                if ( dmc.getUseCount() >= 1 )
                                {
                                    // 有効期限切れの確認
                                    if ( Integer.parseInt( DateEdit.getDate( 2 ) ) > uc.getCouponInfo().getEndDate() && Integer.parseInt( DateEdit.getDate( 2 ) ) > coupon.getHotelCoupon().getEndDate() )
                                        boolReUse = false;
                                    else
                                    // 使用済みの確認
                                    if ( uc.getUsedCouponCount( coupon.getHotelCoupon().getId(), dmc.getSeq(), uli.getUserInfo().getUserId() ) >= dmc.getUseCount() )
                                        boolUsed = true;
                                    else
                                        boolPrint = true;
                                }
                                else
                                {
                                    boolPrint = true;
                                }
                            }
                        }

                        // 発行可能かどうか
                        if ( boolPrint != false )
                        {
                            // ｸｰﾎﾟﾝのｼﾘｱﾙ番号を登録
                            boolAvailable = uc.setUserCoupon( coupon.getHotelCoupon().getId(), nType, uli.getUserInfo().getUserId(), userStatus, "" );
                        }

                        if ( boolAvailable != false )
                        {
                            // クーポンの制限判別
                            if ( memberFlag != false && uli.getUserInfo().getRegistStatus() == 9 && dmc.getAvailable() == 1 )
                            {
                                boolAvailable = true;
                            }
                            else if ( memberFlag == false && dmc.getAvailable() == 2 )
                            {
                                boolAvailable = true;
                            }
                            else if ( dmc.getAvailable() == 0 )
                            {
                                boolAvailable = true;
                            }
                            else
                            {
                                boolAvailable = false;
                            }
                        }

                        // クーポンの表示
                        if ( boolAvailable != false )
                        {
                            // クーポンのイメージをセット
                            this.couponImage = COUPON_IMAGE;

                            // シリアル番号をセット
                            this.couponNo = (String.format( "%1$03d", coupon.getHotelCoupon().getAllSeq() % 1000 )) + "-" + (String.format( "%1$04d", uc.getCouponInfo().getCouponNo() % 10000 ));

                            // 発行日を指定する
                            if ( uc.getCouponInfo().getEndDate() != 0 && uc.getCouponInfo().getEndDate() != 99999999 || Integer.parseInt( DateEdit.getDate( 2 ) ) < uc.getCouponInfo().getStartDate() && dmc.getStartDay() > 0 || dmc.getUseCount() >= 1 )
                            {
                                this.couponIssuance = Integer.toString( uc.getCouponInfo().getPrintDate() / 10000 ) + "年" + String.format( "%1$02d", uc.getCouponInfo().getPrintDate() / 100 % 100 ) + "月"
                                        + String.format( "%1$02d", uc.getCouponInfo().getPrintDate() % 100 ) + "日";
                            }

                            // 有効期間
                            if ( uc.getCouponInfo().getEndDate() != 0 && uc.getCouponInfo().getEndDate() != 99999999 )
                            {
                                // 終了日付よりクーポンの期限の方が早い場合はクーポンの起源を終了日とする
                                if ( uc.getCouponInfo().getEndDate() > coupon.getHotelCoupon().getPeriod() )
                                {

                                    this.couponPeriod = Integer.toString( uc.getCouponInfo().getStartDate() / 10000 ) + "年" + String.format( "%1$02d", uc.getCouponInfo().getStartDate() / 100 % 100 ) + "月"
                                            + String.format( "%1$02d", uc.getCouponInfo().getStartDate() % 100 ) + "日～" + (Integer.toString( coupon.getHotelCoupon().getPeriod() / 10000 )) + "年"
                                            + String.format( "%1$02d", coupon.getHotelCoupon().getPeriod() / 100 % 100 ) + "月" + String.format( "%1$02d", coupon.getHotelCoupon().getPeriod() % 100 % 100 )
                                            + "日まで";
                                }
                                else
                                {
                                    this.couponPeriod = Integer.toString( uc.getCouponInfo().getStartDate() / 10000 ) + "年" + String.format( "%1$02d", uc.getCouponInfo().getStartDate() / 100 % 100 ) + "月"
                                            + String.format( "%1$02d", uc.getCouponInfo().getStartDate() % 100 ) + "日～" + Integer.toString( uc.getCouponInfo().getEndDate() / 10000 ) + "年"
                                            + String.format( "%1$02d", uc.getCouponInfo().getEndDate() / 100 % 100 )
                                            + "月" + String.format( "%1$02d", uc.getCouponInfo().getEndDate() % 100 ) + "日まで";

                                }
                            }

                            // 共通使用条件
                            if ( Integer.parseInt( DateEdit.getDate( 2 ) ) < uc.getCouponInfo().getStartDate() && dmc.getStartDay() > 0 )
                            {
                                this.couponCommonCondition = " ※当日の利用は出来ません。";
                            }
                            this.couponCommonCondition += coupon.getHotelCoupon().getCommonCondition();
                            // 回数指定がある場合
                            if ( dmc.getUseCount() >= 1 )
                            {
                                this.couponCommonCondition += dmc.getUseCount() + "回のみ利用可能";
                                if ( dmc.getUseCount() >= 2 )
                                {
                                    this.couponCommonCondition += "（" + uc.getUsedCouponCount( coupon.getHotelCoupon().getId(), dmc.getSeq(), uli.getUserInfo().getUserId() ) + 1 + "回目）";
                                }
                            }

                            // 特典内容の追加
                            this.couponText = new ArrayList<String>();
                            this.couponCondition = new ArrayList<String>();
                            if ( coupon.getHotelCoupon().getBenefitText1().compareTo( "" ) != 0 )
                            {
                                this.couponText.add( coupon.getHotelCoupon().getBenefitText1() );
                                this.couponCondition.add( coupon.getHotelCoupon().getBenefitCondition1() );
                            }
                            if ( coupon.getHotelCoupon().getBenefitText2().compareTo( "" ) != 0 )
                            {
                                this.couponText.add( coupon.getHotelCoupon().getBenefitText2() );
                                this.couponCondition.add( coupon.getHotelCoupon().getBenefitCondition2() );
                            }
                            if ( coupon.getHotelCoupon().getBenefitText3().compareTo( "" ) != 0 )
                            {
                                this.couponText.add( coupon.getHotelCoupon().getBenefitText3() );
                                this.couponCondition.add( coupon.getHotelCoupon().getBenefitCondition3() );
                            }

                            this.couponMobileCondition = coupon.getHotelCoupon().getDispMobileMessage();
                        }
                    }
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.setCouponMessage]Exception:" + e.toString() );
        }
    }

    /***
     * 顧客のデータを取得する
     * 
     * @param DataApHotelCustom 顧客情報
     */
    public String setCustomId(int id, String userId)
    {

        DataApHotelCustom dahc = new DataApHotelCustom();

        if ( dahc.getValidData( id, userId ) )
        {
            this.customId = dahc.getCustomId();
        }
        return this.customId;
    }

    /***
     * クーポンのデータを取得する
     * 
     * @param DataHotelBasic ホテル情報
     */
    public void getAutoCoupon(DataHotelBasic dhb, UserLoginInfo uli)
    {
        boolean ret = false;
        AutoCoupon ac = new AutoCoupon();
        DataApHotelSetting dahs = new DataApHotelSetting();
        TouchUserCoupon tuc = new TouchUserCoupon();

        try
        {
            dahs.getData( dhb.getId() );
            // 自動適用クーポン発行ホテルで、ユーザが正会員であれば自動適用クーポン取得処理へすすむ
            if ( dahs.getAutoCouponFlag() == 1 && uli.isMemberFlag() != false && uli.getUserInfo().getRegistStatus() == 9 )
            {
                // ユーザの自動適用クーポンを取得する。
                ret = tuc.getUserAutoCoupon( uli.getUserInfo().getUserId(), dhb.getId() );
                // 自動発行クーポンがなければデータ作成処理へ
                if ( ret == false )
                {
                    // ホテルの自動発行クーポン情報を取得
                    ret = ac.getAutoCoupon( dhb.getId() );
                    if ( ret != false )
                    {
                        tuc.registUserAutoCoupon( ac.getAutoCoupon(), uli.getUserInfo().getUserId() );
                    }

                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.getAutoCoupon]Exception:" + e.toString() );
        }
    }

    public static boolean isHotenaviCustom(int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT menu.title FROM hh_hotel_basic";
        query += " INNER JOIN menu ON menu.hotelid=hh_hotel_basic.hotenavi_id";
        query += " WHERE hh_hotel_basic.id = ?";
        query += " AND menu.disp_flg = ? ";
        query += " AND menu.contents = ? ";
        // query += " AND menu.data_type = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, 1 );
            prestate.setString( 3, "search.jsp" );
            // prestate.setInt( 4, 1 );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.isHotenaviCustom] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
