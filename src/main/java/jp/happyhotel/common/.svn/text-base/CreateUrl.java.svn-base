/*
 * @(#)CreateUrl.java 1.00 2009/06/17 Copyright (C) ALMEX Inc. 2009 yahoo map 用URL作成クラス
 */
package jp.happyhotel.common;

import java.io.Serializable;
import java.net.URLEncoder;

/**
 * yahoo map用URL作成クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/06/17
 * @see "yahooMapAPIについては、地図検索系資料または、APICourseDevelopersGuideを参照してください。"
 */
public class CreateUrl implements Serializable
{

    private static final long serialVersionUID = -3280429617771026903L;
    private final String      YOLP_APP_ID      = "?appid=dj0zaiZpPWEybUpEVXg2NFNiNiZzPWNvbnN1bWVyc2VjcmV0Jng9M2U-";            // アプリケーションID
    private final String      YOLP_URL         = "http://map.olp.yahooapis.jp/OpenLocalPlatform/V1/static";                    // API呼び出しURL
    private final String      YOLP_REVERSE_URL = "http://reverse.search.olp.yahooapis.jp/OpenLocalPlatform/V1/reverseGeoCoder"; // API呼び出しURL
    private final String      YOLP_ROUTE_ID    = "?appid=dj0zaiZpPXF2cWVaNzVTdlRjbiZzPWNvbnN1bWVyc2VjcmV0Jng9NjU-";
    private final String      YOLP_ROUTE_URL   = "http://routemap.olp.yahooapis.jp/OpenLocalPlatform/V1/routeMap";             // RouteAPI呼び出し URL
    private final String      APP_ID           = "appid=o9_096x27";                                                            // アプリケーションID
    private final String      URL              = "http://api.pmx.proatlas.net/";                                               // API呼び出しURL
    private final String      PES_API          = "PESWebService/v1/";                                                          // PES API
    private final String      SOKODOKO_API     = "SokodokoWebService/v1/";                                                     // SOKODOKO API
    private final String      SMARTROUTING_API = "SmartRoutingWebService/v1/routing?";                                         // SmartRouting API（routingメソッド込み）
    private final String      DRAWMAP          = "drawMap?";                                                                   // drawMapメソッド
    private final String      GETMAPINFO       = "getMapInfo?";                                                                // getMapInfoメソッド
    private final String      GEODECODE        = "geoDecode?";                                                                 // getDecodeメソッド
    private final String      GEOCODER_URL     = "http://geo.search.olp.yahooapis.jp/OpenLocalPlatform/V1/geoCoder";

    // 既定値
    private final int         WALKINGSPEED     = 5;
    private final int         HIGHWAYSPEED     = 100;
    private final int         NATIONROUTESPEED = 60;
    private final int         PREFROADSPEED    = 50;
    private final int         OTHERSROADSPEED  = 20;
    private final int         TOLLROADSPEED    = 80;
    private final int         ONE              = 1;
    private final int         TWO              = 2;
    private final int         THREE            = 3;

    // 共通で使用する変数
    private String            point;
    private String            output;
    // PES APIとSmartRouting APIで共通
    private String            outdatum;
    // PES APIで使用する変数
    private int               width;
    private int               height;
    private int               scale;
    private int               dscaleX;
    private int               dscaleY;
    private int               dscale;
    private boolean           widthMobile;
    private boolean           heightMobile;
    private String            upperLeft;
    private String            upperRight;
    private String            downLeft;
    private String            downRight;
    private String            center;
    private String            pin;
    private String            pindefault;
    private String            select;
    private String            color;
    private String            scalebar;
    private String            info;
    private String            map;
    private String            pos;
    private String            blink;
    private String            circle;
    private String            line;
    private String            area;
    private String            strWord;
    private String            pointlist;
    private String            radius;
    private String            imgtype;
    private String            dataname;
    private String            address;
    private String            zipcode;
    private String            lat;
    private String            lon;
    // SmartRouting APIで使用する変数
    private int               tollPriority;
    private int               costPriority;
    private int               transport;
    private int               travelingFlag;
    private int               useAroundTollRoad;
    private int               speedWalk;
    private int               speedHighway;
    private int               speedCityHighway;
    private int               speedNationRoute;
    private int               speedMainRoad;
    private int               speedPrefRoad;
    private int               speedOtherRoad;
    private int               speedTollRoad;
    private int               detailInfo;
    // SokodokoAPIで使用する変数
    private int               mlv;

    /**
     * データを初期化します。
     */
    public CreateUrl()
    {
        // 共通で使用する変数
        point = "";
        output = "";
        // PES APIとSmartRouting APIで使用する変数
        outdatum = "";
        // PES APIで使用する変数
        width = 0;
        height = 0;
        scale = 0;
        dscaleX = 0;
        dscaleY = 0;
        dscale = 0;
        widthMobile = false;
        heightMobile = false;
        select = "";
        upperLeft = "";
        upperRight = "";
        downLeft = "";
        downRight = "";
        center = "";
        pin = "";
        pindefault = "";
        color = "";
        scalebar = "";
        info = "";
        map = "";
        pos = "";
        blink = "";
        circle = "";
        line = "";
        area = "";
        strWord = "";
        pointlist = "";
        radius = "";
        imgtype = "";
        dataname = "";
        address = "";
        zipcode = "";
        lat = "";
        lon = "";
        // SmartRouting APIで使用する変数
        tollPriority = 0;
        costPriority = 0;
        transport = 0;
        travelingFlag = 0;
        useAroundTollRoad = 0;
        speedWalk = 0;
        speedHighway = 0;
        speedCityHighway = 0;
        speedNationRoute = 0;
        speedMainRoad = 0;
        speedPrefRoad = 0;
        speedOtherRoad = 0;
        speedTollRoad = 0;
        detailInfo = 0;
        // Sokodoko APIで使用する変数
        mlv = 0;
    }

    /*---------------▼getter▼---------------*/

    // 共通で使用するメソッド
    /**
     * 描画するコンテンツの緯度、経度を取得
     */
    public String getPoint()
    {
        return(point);
    }

    /**
     * 出力パラメータの測地系を取得
     */
    public String getOutdatum()
    {
        return(outdatum);
    }

    /**
     * 出力パラメータの測地系を取得
     */
    public String getOutput()
    {
        return(output);
    }

    // PES APIで使用するメソッド
    /**
     * 地図の幅（画像サイズ）を取得
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * 地図の高さ（画像サイズ）を取得
     */
    public int getHeight()
    {
        return(height);
    }

    /**
     * 地図の縮尺を取得
     */
    public int getScale()
    {
        return scale;
    }

    /**
     * 地図の横方向の移動量を取得
     */
    public int getDx()
    {
        return(dscaleX);
    }

    /**
     * 地図の縦方向の移動量を取得
     */
    public int getDy()
    {
        return(dscaleY);
    }

    /**
     * 移動量の基準となる縮尺を取得
     */
    public int getDscale()
    {
        return(dscale);
    }

    /**
     * 携帯向けの地図幅・画像フォーマット自動調整機能の有無の取得
     */
    public boolean getWm()
    {
        return widthMobile;
    }

    /**
     * 携帯向けの地図高さ・画像フォーマット自動調整機能の有無の取得
     */
    public boolean getHm()
    {
        return heightMobile;
    }

    /**
     * 地図の左上の緯度経度の取得
     */
    public String getUl()
    {
        return(upperLeft);
    }

    /**
     * 地図の右上の緯度経度の取得
     */
    public String getUr()
    {
        return(upperRight);
    }

    /**
     * 地図の左下の緯度経度の取得
     */
    public String getDl()
    {
        return(downLeft);
    }

    /**
     * 地図の右下の緯度経度の取得
     */
    public String getDr()
    {
        return(downRight);
    }

    /**
     * 地図の中心の緯度経度の取得
     */
    public String getC()
    {
        return(center);
    }

    /**
     * ホテルのPINのの緯度経度の取得
     */
    public String getPin()
    {
        return(pin);
    }

    /**
     * ホテルのPINのの緯度経度の取得
     */
    public String getPindefault()
    {
        return(pindefault);
    }

    /**
     * 縮尺階層の選択方法を取得
     */
    public String getSelect()
    {
        return select;
    }

    /**
     * 拡張パレットを取得
     */
    public String getColor()
    {
        return(color);
    }

    /**
     * スケールバーの有無を取得
     */
    public String getScaleBar()
    {
        return(scalebar);
    }

    /**
     * 出力パラメータの有無を取得
     */
    public String getInfo()
    {
        return(info);
    }

    /**
     * 地図画像出力の有無を取得
     */
    public String getMap()
    {
        return(map);
    }

    /**
     * 描画するコンテンツを取得
     */
    public String getPos()
    {
        return(pos);
    }

    /**
     * 強調するアイコンのposパラメータにおけるindexを取得
     */
    public String getBlink()
    {
        return(blink);
    }

    /**
     * 描画する円を取得
     */
    public String getCircle()
    {
        return(circle);
    }

    /**
     * 描画する線の取得
     */
    public String getLine()
    {
        return(line);
    }

    /**
     * 描画する面の取得
     */
    public String getArea()
    {
        return(area);
    }

    /**
     * 描画する文字の取得
     */
    public String getStr()
    {
        return(strWord);
    }

    /**
     * 描画する線の緯度経度の取得
     */
    public String getPointList()
    {
        return(pointlist);
    }

    /**
     * 描画する円の半径を取得
     */
    public String getRadius()
    {
        return(radius);
    }

    /**
     * 画像の出力形式を取得
     */
    public String getImgType()
    {
        return(imgtype);
    }

    /**
     * 地図の種類を取得
     */
    public String getDataName()
    {
        return(dataname);
    }

    /**
     * 検索する住所文字列を取得
     */
    public String getAddress()
    {
        return(address);
    }

    /**
     * 検索する郵便番号を取得
     */
    public String getZipCode()
    {
        return(zipcode);
    }

    // SmartRouting APIで使用するメソッド
    /**
     * 優先する道路を取得
     */
    public int getTollPriority()
    {
        return(tollPriority);
    }

    /**
     * 時間・距離の優先の取得
     */
    public int getCostPriority()
    {
        return(costPriority);
    }

    /**
     * 移動手段の取得
     */
    public int getTransport()
    {
        return(transport);
    }

    /**
     * 巡回経路検索を行うフラグの取得
     */
    public int getTravelingFlag()
    {
        return(travelingFlag);
    }

    /**
     * 有料道路上の始終点の対象フラグの取得
     */
    public int getUseAroundTollRoad()
    {
        return(useAroundTollRoad);
    }

    /**
     * 徒歩の速度の取得
     */
    public int getSpeedWalk()
    {
        return(speedWalk);
    }

    /**
     * 高速道路の速度の取得
     */
    public int getSpeedHighway()
    {
        return(speedHighway);
    }

    /**
     * 都市高速の速度の取得
     */
    public int getSpeedCityHighway()
    {
        return(speedCityHighway);
    }

    /**
     * 国道の速度の取得
     */
    public int getSpeedNationRoute()
    {
        return(speedNationRoute);
    }

    /**
     * 主要道の速度の取得
     */
    public int getSpeedMainRoad()
    {
        return(speedMainRoad);
    }

    /**
     * 県道の速度の取得
     */
    public int getSpeedPrefRoad()
    {
        return(speedPrefRoad);
    }

    /**
     * その他の道の速度の取得
     */
    public int getSpeedOtherRoad()
    {
        return(speedOtherRoad);
    }

    /**
     * 有料道の速度の取得
     */
    public int getSpeedTollRoad()
    {
        return(speedTollRoad);
    }

    /**
     * NordList,PartListの出力フラグを取得
     */
    public int getDetailInfo()
    {
        return(detailInfo);
    }

    /**
     * @return 解析深度を取得
     */
    public int getMlv()
    {
        return mlv;
    }

    /**
     * @return 緯度を取得
     */
    public String getLat()
    {
        return lat;
    }

    /**
     * @return 経度を取得
     */
    public String getLon()
    {
        return lon;
    }

    /*---------------▲getter▲---------------*/

    /*---------------▼setter▼---------------*/
    // 共通で使用するメソッド
    /**
     * 描画するコンテンツの緯度、経度をセット
     * 
     * @see "例:Tokyo97,35,135をセットする。"
     */
    public void setPoint(String point)
    {
        this.point = point;
    }

    /**
     * 出力パラメータの測地系をセット
     * 
     * @see "Tokyo97(既定値),JGD2000,WGS84をセットする"
     */
    public void setOutdatum(String outdatum)
    {
        this.outdatum = outdatum;
    }

    /**
     * 出力パラメータの測地系をセット
     * 
     * @see "xml(既定値),jsonをセットする"
     */
    public void setOutput(String output)
    {
        this.output = output;
    }

    /**
     * 地図の幅（画像サイズ）をセット
     * 
     * @see "既定値: 320 (単位=ピクセル),最大値: 1280"
     */
    public void setWidth(int width)
    {
        this.width = width;
    }

    /**
     * 地図の高さ（画像サイズ）をセット
     * 
     * @see "既定値: 240 (単位=ピクセル),最大値: 1280"
     */
    public void setHeight(int height)
    {
        this.height = height;
    }

    /**
     * 地図の縮尺をセット
     * 
     * @see "指定された緯度経度で表示できる最大縮尺が既定値となる。"
     */
    public void setScale(int scale)
    {
        this.scale = scale;
    }

    /**
     * 地図の横方向の移動量をセット
     * 
     * @see "単位はドット"
     */
    public void setDx(int dx)
    {
        this.dscaleX = dx;
    }

    /**
     * 地図の縦方向の移動量をセット
     * 
     * @see "単位はドット"
     */
    public void setDy(int dy)
    {
        this.dscaleY = dy;
    }

    /**
     * 移動量の基準となる縮尺をセット
     */
    public void setDscale(int dscale)
    {
        this.dscale = dscale;
    }

    /**
     * 携帯向けの地図幅・画像フォーマット自動調整機能の有無のセット
     * 
     * @see "true:使用,false:使用しない(既定値)"
     */
    public void setWm(boolean wm)
    {
        this.widthMobile = wm;
    }

    /**
     * 携帯向けの地図高さ・画像フォーマット自動調整機能の有無のセット
     * 
     * @see "true:使用,false:使用しない(既定値)"
     */
    public void setHm(boolean hm)
    {
        this.heightMobile = hm;
    }

    /**
     * 地図の左上の緯度経度のセット
     */
    public void setUl(String ul)
    {
        this.upperLeft = ul;
    }

    /**
     * 地図の右上の緯度経度のをセット
     */
    public void setUr(String ur)
    {
        this.upperRight = ur;
    }

    /**
     * 地図の左下の緯度経度のセット
     */
    public void setDl(String dl)
    {
        this.downLeft = dl;
    }

    /**
     * 地図の右下の緯度経度のセット
     */
    public void setDr(String dr)
    {
        this.downRight = dr;
    }

    /**
     * 地図の中心の緯度経度のセット
     */
    public void setC(String c)
    {
        this.center = c;
    }

    /**
     * ホテルの緯度経度のセット
     */
    public void setPin(String pin)
    {
        this.pin = pin;
    }

    /**
	 *
	 */
    public void setPindefault(String pindefault)
    {
        this.pindefault = pindefault;
    }

    /**
     * 縮尺階層の選択方法をセット
     * 
     * @see "point,area,comp,scrollのいづれかをセット"
     */
    public void setSelect(String select)
    {
        this.select = select;
    }

    /**
     * 拡張パレットをセット
     * 
     * @see "point,area,comp,scrollのいづれかをセット"
     */
    public void setColor(String color)
    {
        this.color = color;
    }

    /**
     * スケールバーの有無をセット
     * 
     * @see "yes:あり(既定値),no:なし"
     */
    public void setScaleBar(String scalebar)
    {
        this.scalebar = scalebar;
    }

    /**
     * 出力パラメータの有無をセット
     * 
     * @see "yes:(既定値),no;なし"
     */
    public void setInfo(String info)
    {
        this.info = info;
    }

    /**
     * 地図画像出力の有無をセット
     * 
     * @see "yes:あり(既定値),no:なし"
     */
    public void setMap(String map)
    {
        this.map = map;
    }

    /**
     * 描画するコンテンツをセット
     * 
     * @see "I:属性指定(no,null,star,pinなど表示するアイコンを指定する),p:中心緯度経度"
     */
    public void setPos(String pos)
    {
        this.pos = pos;
    }

    /**
     * 強調するアイコンのposパラメータにおけるindexをセット
     * 
     * @see "index値:(0～49)"
     */
    public void setBlink(String blink)
    {
        this.blink = blink;
    }

    /**
     * 描画する円をセット(API Course Developers Guideを参照)
     * 
     * @see "A:円内の色,B:円周線の色,R:半径(メートル),S:円の塗りつぶし方法, W:円周線の幅を指定する(既定値:2),P:円の中心緯度経度"
     */
    public void setCircle(String circle)
    {
        this.circle = circle;
    }

    /**
     * 描画する線のセット(API Course Developers Guideを参照)
     * 
     * @see "c:色,W:幅(既定値2),P:線を書くための始終点"
     */
    public void setLine(String line)
    {
        this.line = line;
    }

    /**
     * 描画する面のセット
     * 
     * @see "A:面の色,B:境界線の色,S:面の塗りつぶし方法,W:境界線の幅,P:面を描くための緯度経度"
     */
    public void setArea(String area)
    {
        this.area = area;
    }

    /**
     * 描画する文字のセット
     * 
     * @see "X:横方向の配置座標(ドット),Y:縦方向の配置座標,B:背景色,F:文字色,T:フォントタイプ,P:緯度経度, W:横方向の座標修正,H:縦方向の座標修正,S:表示する文字列"
     */
    public void setStr(String str)
    {
        this.strWord = str;
    }

    /**
     * 描画する線の緯度経度のセット
     */
    public void setPointList(String pointlist)
    {
        this.pointlist = pointlist;
    }

    /**
     * 描画する円の半径をセット
     * 
     * @see "メートル単位（既定値:1000）"
     */
    public void setRadius(String radius)
    {
        this.radius = radius;
    }

    /**
     * 画像の出力形式をセット
     * 
     * @see "PNG(既定値),JPG,GIFをセット"
     */
    public void setImgType(String imgtype)
    {
        this.imgtype = imgtype;
    }

    /**
     * 地図の種類をセット
     * 
     * @see "ProAlas:プロアトラス地図,ProAtlasWeb:Web地図(既定値),ProAtlasWebCity:市街地図, ProAtlasCrear:クリア地図,ProAtlasCrearCity:クリア市街地図"
     */
    public void setDataName(String dataname)
    {
        this.dataname = dataname;
    }

    /**
     * 検索する住所文字列をセット
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * 検索する郵便番号をセット
     */
    public void setZipCode(String zipcode)
    {
        this.zipcode = zipcode;
    }

    // SmartRouting APIで使用するメソッド
    /**
     * 優先する道路をセット
     * 
     * @see "1:有料道路優先(既定値),2:一般道優先,3:一般道のみ"
     */
    public void setTollPriority(int tolPriority)
    {
        this.tollPriority = tolPriority;
    }

    /**
     * 時間・距離の優先の取得
     * 
     * @see "1:時間優先(既定値),2:距離優先"
     */
    public void setCostPriority(int costPriority)
    {
        this.costPriority = costPriority;
    }

    /**
     * 移動手段のセット
     * 
     * @see "1:車(既定値),2:徒歩"
     */
    public void setTransport(int transport)
    {
        this.transport = transport;
    }

    /**
     * 巡回経路検索を行うフラグのセット
     * 
     * @see "0:巡回経路検索を行わない(既定値),1:巡回経路検索を行う"
     */
    public void setTravelingFlag(int travelingFlag)
    {
        this.travelingFlag = travelingFlag;
    }

    /**
     * 有料道路上の始終点の対象フラグのセット
     * 
     * @see "0:有料道路上の始終点の対象としない(既定値),1:有料道路上の始終点の対象とする"
     */
    public void setUseAroundTollRoad(int useAroundTollRoad)
    {
        this.useAroundTollRoad = useAroundTollRoad;
    }

    /**
     * 徒歩の速度のセット
     * 
     * @see "既定値:5"
     */
    public void setSpeedWalk(int speedWalk)
    {
        this.speedWalk = speedWalk;
    }

    /**
     * 高速道路の速度のセット
     * 
     * @see "既定値:100"
     */
    public void setSpeedHighWay(int speedHighway)
    {
        this.speedHighway = speedHighway;
    }

    /**
     * 都市高速の速度のセット
     * 
     * @see "既定値:100"
     */
    public void setSpeedCityHighWay(int speedCityHighway)
    {
        this.speedCityHighway = speedHighway;
    }

    /**
     * 国道の速度のセット
     * 
     * @see "既定値:60"
     */
    public void setSpeedNationRoute(int speedNationRoute)
    {
        this.speedNationRoute = speedNationRoute;
    }

    /**
     * 主要道の速度のセット
     * 
     * @see "既定値:60"
     */
    public void setSpeedMainRoad(int speedMainRoad)
    {
        this.speedMainRoad = speedMainRoad;
    }

    /**
     * 県道の速度のセット
     * 
     * @see "既定値:50"
     */
    public void setSpeedPrefRoad(int speedPrefRoad)
    {
        this.speedPrefRoad = speedPrefRoad;
    }

    /**
     * その他の道の速度のセット
     * 
     * @see "既定値:20"
     */
    public void setSpeedOtherRoad(int speedOtherRoad)
    {
        this.speedOtherRoad = speedOtherRoad;
    }

    /**
     * 有料道の速度のセット
     * 
     * @see "既定値:80"
     */
    public void setSpeedTollRoad(int speedTollRoad)
    {
        this.speedTollRoad = speedTollRoad;
    }

    /**
     * NordList,PartListの出力フラグをセット
     * 
     * @see "0:出力しない(既定値),1:出力する"
     */
    public void setDetailInfo(int detailInfo)
    {
        this.detailInfo = detailInfo;
    }

    /**
     * @param mlv セットする解析深度
     * @see "1:都道府県,2:市区町村,3:町大字,4:丁目までマッチング"
     */
    public void setMlv(int mlv)
    {
        this.mlv = mlv;
    }

    /**
     * /**
     * 緯度をセット
     * */
    public void setLat(String lat)
    {
        this.lat = lat;
    }

    /**
     * 経度をセット
     * */
    public void setLon(String lon)
    {
        this.lon = lon;
    }

    /*---------------▲setter▲---------------*/

    /**
     * PES APIの地図画像表示メソッドを呼び出すURLの作成
     * 
     * @return 作成したURL
     * @see "必要なパラメータをセットすること"
     */
    public String getDrawMap()
    {
        String returnUrl;
        returnUrl = URL + PES_API + DRAWMAP + APP_ID;

        // JGD2000かWGS84のときのみパラメータを追加(共通項目)
        if ( outdatum.compareTo( "JGD2000" ) == 0 || outdatum.compareTo( "WGS84" ) == 0 )
        {
            returnUrl += "&outdatum=" + encodeUtf8( outdatum );
        }

        /*---------------▼PES API共通項目▼---------------*/
        returnUrl += stringCheck( "ul", upperLeft );
        returnUrl += stringCheck( "ur", upperRight );
        returnUrl += stringCheck( "dl", downLeft );
        returnUrl += stringCheck( "dr", downRight );
        returnUrl += stringCheck( "c", center );
        returnUrl += intCheck( "scale", scale );

        // 任意の文字列が入っていなかったら追加しない
        if ( select.indexOf( "point" ) != -1 || (select.indexOf( "area" ) != -1) ||
                select.indexOf( "comp" ) != -1 || select.indexOf( "scroll" ) != -1 )
        {
            returnUrl += "&select=" + encodeUtf8( select );
        }

        // dx,dy,dscaleは3つそろってないと無効となるため、dscaleの値が0以外だったらセットする
        if ( dscale != 0 )
        {
            returnUrl += intCheck( "dscale", dscale );
            returnUrl += "&dx=" + encodeUtf8( Integer.toString( dscaleX ) );
            returnUrl += "&dy=" + encodeUtf8( Integer.toString( dscaleY ) );
        }
        returnUrl += intCheck( "width", width );
        returnUrl += intCheck( "height", height );
        /*---------------▲PES API共通項目▲---------------*/

        /*---------------▼drawMapメソッド項目▼---------------*/
        returnUrl += boolCheck( "wm", widthMobile );
        returnUrl += boolCheck( "hm", heightMobile );

        // grayかhalftoneのときだけパラメータをつける
        if ( color.compareTo( "gray" ) == 0 || color.compareTo( "halftone" ) == 0 )
        {
            returnUrl += "&color=" + encodeUtf8( color );
        }

        // noと一致するときだけパラメータを追加する
        returnUrl += stringCheckEqual( "scalebar", scalebar, "no" );
        returnUrl += stringCheckEqual( "info", info, "no" );
        returnUrl += stringCheckEqual( "map", map, "no" );

        // 任意のコマンド文字が入っている場合のみパラメータを追加する
        if ( pos.indexOf( "I" ) != -1 || pos.indexOf( "P" ) != -1 )
        {
            returnUrl += "&pos=" + encodeUtf8( pos );
            // ブリンクはposパラメータのindexを指定するので、posのパラメータを追加するときのみ
            returnUrl += stringCheck( "blink", blink );
        }
        if ( circle.indexOf( "A" ) != -1 || circle.indexOf( "B" ) != -1 ||
                circle.indexOf( "R" ) != -1 || circle.indexOf( "S" ) != -1 ||
                circle.indexOf( "W" ) != -1 )
        {
            returnUrl += "&circle=" + encodeUtf8( circle );
        }
        if ( line.indexOf( "C" ) != -1 || line.indexOf( "W" ) != -1 || line.indexOf( "P" ) != -1 )
        {
            returnUrl += "&line=" + encodeUtf8( line );
        }
        if ( area.indexOf( "A" ) != -1 || area.indexOf( "B" ) != -1 || area.indexOf( "S" ) != -1 ||
                area.indexOf( "W" ) != -1 || area.indexOf( "P" ) != -1 )
        {
            returnUrl += "&area=" + encodeUtf8( area );
        }
        if ( strWord.indexOf( "X" ) != -1 || strWord.indexOf( "Y" ) != -1 || strWord.indexOf( "B" ) != -1 ||
                strWord.indexOf( "F" ) != -1 || strWord.indexOf( "T" ) != -1 || strWord.indexOf( "P" ) != -1 ||
                strWord.indexOf( "W" ) != -1 || strWord.indexOf( "H" ) != -1 )
        {
            returnUrl += "&str=" + encodeUtf8( strWord );
        }

        // データがあれば、パラメータとして追加
        returnUrl += stringCheck( "point", point );
        returnUrl += stringCheck( "pointlist", pointlist );

        // JPGかGIFと一致したときだけ、パラメータを追加する
        if ( imgtype.compareTo( "JPG" ) == 0 || imgtype.compareTo( "GIF" ) == 0 )
        {
            returnUrl += "&imgtype=" + encodeUtf8( imgtype );
        }

        // 既定値以外の種類が入っていたときのみパラメータを追加する
        if ( dataname.compareTo( "ProAtlas" ) == 0 || dataname.compareTo( "ProAtlasWebCity" ) == 0 ||
                dataname.compareTo( "ProAtlasClear" ) == 0 || dataname.compareTo( "ProAtlasClearCity" ) == 0 )
        {
            returnUrl += "&dataname=" + encodeUtf8( dataname );
        }
        else
        {
            returnUrl += "&dataname=ProAtlasClearCity";
        }

        // データがあれば、パラメータとして追加
        returnUrl += stringCheck( "zipcode", zipcode );
        returnUrl += stringCheck( "address", address );
        // returnUrl += "&address=" + address;
        /*---------------▲drawMapメソッド項目▲---------------*/

        return(returnUrl);
    }

    /**
     * PES APIの地図画像表示メソッドを呼び出すURLの作成
     * 
     * @return 作成したURL
     * @see "必要なパラメータをセットすること"
     */
    public String getYolpDrawMap()
    {
        String returnUrl;
        returnUrl = YOLP_URL + YOLP_APP_ID;

        // JGD2000かWGS84のときのみパラメータを追加(共通項目)
        if ( outdatum.compareTo( "JGD2000" ) == 0 || outdatum.compareTo( "WGS84" ) == 0 )
        {
            returnUrl += "&outdatum=" + encodeUtf8( outdatum );
        }

        /*---------------▼PES API共通項目▼---------------*/
        returnUrl += stringCheck( "ul", upperLeft );
        returnUrl += stringCheck( "ur", upperRight );
        returnUrl += stringCheck( "dl", downLeft );
        returnUrl += stringCheck( "dr", downRight );
        returnUrl += stringCheck( "c", center );
        returnUrl += stringCheck( "pindefault", pin );
        returnUrl += intCheck( "scale", scale );

        // 任意の文字列が入っていなかったら追加しない
        if ( select.indexOf( "point" ) != -1 || (select.indexOf( "area" ) != -1) ||
                select.indexOf( "comp" ) != -1 || select.indexOf( "scroll" ) != -1 )
        {
            returnUrl += "&select=" + encodeUtf8( select );
        }

        // dx,dy,dscaleは3つそろってないと無効となるため、dscaleの値が0以外だったらセットする
        if ( dscale != 0 )
        {
            returnUrl += intCheck( "dscale", dscale );
            returnUrl += "&dx=" + encodeUtf8( Integer.toString( dscaleX ) );
            returnUrl += "&dy=" + encodeUtf8( Integer.toString( dscaleY ) );
        }

        /*---------------▲PES API共通項目▲---------------*/

        /*---------------▼drawMapメソッド項目▼---------------*/
        /*---------------▲drawMapメソッド項目▲---------------*/

        return(returnUrl);
    }

    /**
     * PES APIの地図画像の情報を取得するURLの作成(XML形式で返す)
     * 
     * @return 作成したURL
     * @see "必要なパラメータをセットすること"
     */
    public String getMapInfo()
    {
        String returnUrl;
        returnUrl = URL + PES_API + GETMAPINFO + APP_ID;

        // JGD2000かWGS84のときのみパラメータを追加(共通項目)
        if ( outdatum.compareTo( "JGD2000" ) == 0 || outdatum.compareTo( "WGS84" ) == 0 )
        {
            returnUrl += "&outdatum=" + encodeUtf8( outdatum );
        }

        /*---------------▼PES API共通項目▼---------------*/
        returnUrl += stringCheck( "ul", upperLeft );
        returnUrl += stringCheck( "ur", upperRight );
        returnUrl += stringCheck( "dl", downLeft );
        returnUrl += stringCheck( "dr", downRight );
        returnUrl += stringCheck( "c", center );
        returnUrl += intCheck( "scale", scale );

        // 任意の文字列が入っていなかったら追加しない
        if ( select.indexOf( "point" ) != -1 || (select.indexOf( "area" ) != -1) ||
                select.indexOf( "comp" ) != -1 || select.indexOf( "scroll" ) != -1 )
        {
            returnUrl += "&select=" + encodeUtf8( select );
        }
        // dx,dy,dscaleは3つそろってないと無効となるため、dscaleの値が0以外だったらセットする
        if ( dscale != 0 )
        {
            returnUrl += intCheck( "dscale", dscale );
            returnUrl += "&dx=" + encodeUtf8( Integer.toString( dscaleX ) );
            returnUrl += "&dy=" + encodeUtf8( Integer.toString( dscaleY ) );
        }
        returnUrl += intCheck( "width", width );
        returnUrl += intCheck( "height", height );
        /*---------------▲PES API共通項目▲---------------*/

        // jsonと一致する場合のみパラメータを増やす
        returnUrl += stringCheckEqual( "output", output, "json" );

        return(returnUrl);
    }

    /**
     * SmartRouting APIのルート検索を行うURLの作成
     * 
     * @return 作成したURL
     * @see "必要なパラメータをセットすること"
     */
    public String getRouting()
    {
        String returnUrl;
        returnUrl = URL + SMARTROUTING_API + APP_ID;

        // JGD2000かWGS84のときのみパラメータを追加(共通項目)
        if ( outdatum.compareTo( "JGD2000" ) == 0 || outdatum.compareTo( "WGS84" ) == 0 )
        {
            returnUrl += "&outdatum=" + encodeUtf8( outdatum );
        }

        returnUrl += stringCheck( "point", point );
        if ( tollPriority == TWO || tollPriority == THREE )
        {
            returnUrl += "&tollPriority=" + encodeUtf8( Integer.toString( tollPriority ) );
        }

        /*---------------▼SmartRouting API▼---------------*/
        // 値が2だったらパラメータを追加する
        returnUrl += intCheckEquals( "costPriority", costPriority, TWO );
        returnUrl += intCheckEquals( "transport", transport, TWO );
        // 値が1だったらパラメータを追加する
        returnUrl += intCheckEquals( "travelingFlag", travelingFlag, ONE );
        returnUrl += intCheckEquals( "useAroundTollRoad", useAroundTollRoad, ONE );

        // 既定値以外の数字が入っていたらパラメータを追加する
        returnUrl += intCheck( "speedWalk", speedWalk, WALKINGSPEED );
        returnUrl += intCheck( "speedHighway", speedHighway, HIGHWAYSPEED );
        returnUrl += intCheck( "speedCityHighway", speedCityHighway, HIGHWAYSPEED );
        returnUrl += intCheck( "speedNationRoute", speedNationRoute, NATIONROUTESPEED );
        returnUrl += intCheck( "speedMainRoad", speedMainRoad, NATIONROUTESPEED );
        returnUrl += intCheck( "speedPrefectualRoad", speedPrefRoad, PREFROADSPEED );
        returnUrl += intCheck( "speedOtherRoad", speedOtherRoad, OTHERSROADSPEED );
        returnUrl += intCheck( "speedTollRoad", speedTollRoad, TOLLROADSPEED );

        // 値が1だったらパラメータを追加する
        returnUrl += intCheckEquals( "detailInfo", detailInfo, 1 );
        // jsonと一致する場合のみパラメータを増やす
        returnUrl += stringCheckEqual( "output", output, "json" );
        /*---------------▲SmartRouting API▲---------------*/

        return(returnUrl);
    }

    /**
     * Sokodoko APIを使用して緯度経度から住所を検索する
     * 
     * @return 作成したURL
     * @see "必要なパラメータをセットすること"
     */
    public String geoDecode()
    {
        String returnUrl;
        // returnUrl = URL + SOKODOKO_API + GEODECODE + APP_ID;
        // returnUrl = YOLP_REVERSE_URL + YOLP_ROUTE_ID;
        returnUrl = GEOCODER_URL + YOLP_ROUTE_ID;

        // 緯度の指定
        returnUrl += stringCheck( "lat", lat );
        // 緯度の指定
        returnUrl += stringCheck( "lon", lon );
        // 住所が入力されたら住所検索
        returnUrl += stringCheck( "query", address );

        Logging.info( "CreateUrl geoDecode returnUrl :" + returnUrl );

        return(returnUrl);
    }

    /**
     * Yahoo routeMap API
     * 
     * @return 作成したURL
     * @see "必要なパラメータをセットすること"
     */
    public String getYolpRouteMap()
    {
        String returnUrl;
        returnUrl = YOLP_ROUTE_URL + YOLP_ROUTE_ID;

        // 経路の指定
        returnUrl += "&route=" + encodeUtf8( point );

        // 画像サイズの幅
        returnUrl += intCheck( "width", width );

        // 画像サイズの高さ
        returnUrl += intCheck( "height", height );

        // 出力形式
        returnUrl += stringCheck( "output", getImgType() );

        return returnUrl;
    }

    /**
     * Yahoo API
     * 
     * @return 作成したURL
     * @see "必要なパラメータをセットすること"
     */
    public String getMapURL()
    {
        String returnUrl;
        returnUrl = YOLP_URL + YOLP_APP_ID;

        if ( center.compareTo( "" ) != 0 )
        {
            int index = 0;
            index = center.indexOf( "," );
            lat = center.substring( 0, index );
            lon = center.substring( index + 1 );
        }

        returnUrl += stringCheck( "lat", lat );
        returnUrl += stringCheck( "lon", lon );

        returnUrl += stringCheck( "pindefault", pindefault );

        if ( pin.compareTo( "" ) != 0 )
        {
            returnUrl += pin;
        }

        returnUrl += intCheck( "scale", scale );

        // 画像サイズの幅
        returnUrl += intCheck( "width", width );
        // 画像サイズの高さ
        returnUrl += intCheck( "height", height );

        Logging.info( "CreateUrl getMapURL returnUrl :" + returnUrl );

        return returnUrl;
    }

    /**
     * 文字列データ空白チェック
     * 
     * @param addParam 追加するパラメータの変数
     * @param checkParam チェックする文字列
     * @return 空白じゃなければ、URLに追加するパラメータを返す
     * @see "必要なパラメータをセットすること"
     */
    private String stringCheck(String addParam, String checkParam)
    {
        String returnParam;
        returnParam = "";

        // 確認する文字列にデータがあるかどうかを確認し、データがある場合はパラメータを増やす
        if ( checkParam.compareTo( "" ) != 0 )
        {
            returnParam = "&" + addParam + "=" + encodeUtf8( checkParam );
        }
        return(returnParam);
    }

    /**
     * 文字列データ比較
     * 
     * @param addParam 追加するパラメータの変数
     * @param checkParam チェックする文字列
     * @param compareParam 比較対象の文字列
     * @return 一致すれば、URLに追加するパラメータを返す
     * @see "必要なパラメータをセットすること"
     */
    private String stringCheckEqual(String addParam, String checkParam, String compareParam)
    {
        String returnParam;
        returnParam = "";

        // 確認する文字列にデータがあるかどうかを確認し、データがある場合はパラメータを増やす
        if ( checkParam.compareTo( compareParam ) == 0 )
        {
            returnParam = "&" + addParam + "=" + encodeUtf8( checkParam );
        }
        return(returnParam);
    }

    /**
     * ブールデータチェック
     * 
     * @param addParam 追加するパラメータの変数
     * @param checkParam チェックする文字列
     * @return TRUEだったら、URLに追加するパラメータを返す
     * @see "必要なパラメータをセットすること"
     */
    private String boolCheck(String addParam, boolean checkParam)
    {
        String returnParam;
        returnParam = "";

        // 確認する文字列にデータがあるかどうかを確認し、データがある場合はパラメータを増やす
        if ( checkParam != false )
        {
            returnParam = "&" + addParam + "=" + encodeUtf8( Boolean.toString( checkParam ) );
        }
        return(returnParam);
    }

    /**
     * 整数データ自然数チェック
     * 
     * @param addParam 追加するパラメータの変数
     * @param checkParam チェックする整数データ
     * @return 0以外だったら、URLに追加するパラメータを返す
     * @see "必要なパラメータをセットすること"
     */
    private String intCheck(String addParam, int checkParam)
    {
        String returnParam;
        returnParam = "";

        // 確認する整数データが0以外かどうかを確認し、0以上のある場合はパラメータを増やす
        if ( checkParam != 0 )
        {
            returnParam = "&" + addParam + "=" + encodeUtf8( Integer.toString( checkParam ) );
        }
        return(returnParam);
    }

    /**
     * 整数データ既定値チェック
     * 
     * @param addParam 追加するパラメータの変数
     * @param checkParam チェックする整数データ
     * @param compareParam 比較対象の整数データ
     * @return 既定値だったら、URLに追加するパラメータを返す
     * @see "必要なパラメータをセットすること"
     */
    private String intCheckEquals(String addParam, int checkParam, int compareParam)
    {
        String returnParam;
        returnParam = "";

        // 確認する整数データが等しいかどうかを判断
        if ( checkParam == compareParam )
        {
            returnParam = "&" + addParam + "=" + encodeUtf8( Integer.toString( checkParam ) );
        }
        return(returnParam);
    }

    /**
     * 整数データ既定値外チェック
     * 
     * @param addParam 追加するパラメータの変数
     * @param checkParam チェックする整数データ
     * @param defaultParam 既定値
     * @return 0以上かつ既定値以外だったら、URLに追加するパラメータ
     * @see "必要なパラメータをセットすること"
     */
    private String intCheck(String addParam, int checkParam, int defaultParam)
    {
        String returnParam;
        returnParam = "";

        // 確認する整数データが0以上かつ既定値以外だったらパラメータを取得
        if ( checkParam > 0 && checkParam != defaultParam )
        {
            returnParam = "&" + addParam + "=" + encodeUtf8( Integer.toString( checkParam ) );
        }
        return(returnParam);
    }

    /**
     * UTF-8エンコーディング
     */
    private String encodeUtf8(String param)
    {
        try
        {
            param = URLEncoder.encode( param, "UTF-8" );
        }
        catch ( Exception e )
        {
            param = "";
            Logging.info( "[ CreateUrl.URLEncode() ] Exception:" + e.toString() );
        }
        return(param);
    }

}
