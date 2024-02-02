package jp.happyhotel.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.ConvertGeodesic;
import jp.happyhotel.common.CreateUrl;
import jp.happyhotel.common.DistanceDetermination;
import jp.happyhotel.common.HttpConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.ReadXml;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelDistance;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMapPoint;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelEmpty;
import jp.happyhotel.search.SearchHotelFreeword_M2;
import jp.happyhotel.user.UserMap;

/**
 * 
 * GPS検索クラス（有料会員向け）
 * 
 * @author S.Tashiro
 * @version 1.0 2009/10/21
 */

public class ActionGpsSearchPay extends BaseAction
{
    static int                pageRecords       = Constants.pageLimitRecordMobile;
    static int                maxRecords        = Constants.maxRecordsMobile;
    public static final int   dispFormat        = 1;

    private RequestDispatcher requestDispatcher = null;
    private DataLoginInfo_M2  dataLoginInfo_M2  = null;
    private String            lat               = "";
    private String            lon               = "";
    private int               hotelAllCount     = 0;
    private int               hotelCount        = 0;
    private UserMap           um                = null;

    /**
     * 任意の場所付近でホテルを検索
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param response サーバからクライアントへのレスポンス
     * 
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean memberFlag;
        boolean paymemberFlag;
        boolean paymemberTempFlag;
        boolean ret;
        int i;
        int registStatus;
        int delFlag;
        int carrierFlag;
        int pageNum;
        // 中心の座標
        int coordinateLat;
        int coordinateLon;
        // 右上の座標
        int coordinateUrLat;
        int coordinateUrLon;
        // 左下の座標
        int coordinateDlLat;
        int coordinateDlLon;
        int[] hotelIdList;
        int[] distance;
        int[] arrHotelIdList = null;
        String paramId; // ホテルを中心に地図画像表示を行う際に必要なパラメータ
        String paramScale; // 縮尺の管理を行うパラメータ
        // String url;
        String strPosIcon;
        String paramPos; // ソフトバンクでGPS取得したときに返す位置情報のパラメータ
        String paramLat; // 位置情報を扱う扱うパラメータ
        String paramLon; // 位置情報を扱う扱うパラメータ
        String paramAddr; // 住所で検索する場合に必要なパラメータ
        String paramPage; // ページを管理するパラメータ
        String pageLinks;
        String pageHeader;
        String paramCenter;
        String queryString;
        String currentPageRecords;
        String paramUidLink = null;
        String paramGqs; // このパラメータがなかったら住所をyahooAPIから取得する
        String paramEmpty; // 1:空室を検索、2:詳細のホテルを検索
        String paramAcRead;
        String paramAndWord;
        String paramAndWordEnc;
        String paramGo; // GPS取得のエラー判断に使用するパラメータ
        String termNo; // 非会員の端末番号を取得（user_id代わりに使用する）
        CreateUrl cu;
        ConvertGeodesic cg;
        DataMapPoint dmPoint;
        DistanceDetermination dd;
        DataHotelDistance[] dhd;
        HttpConnection con;
        SearchHotelEmpty she;
        SearchHotelFreeword_M2 searchHotelFreeWord;
        SearchHotelCommon searchHotelCommon;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataLoginInfo_M2 dataLoginInfo_M2 = null;
        SearchHotelDao_M2 searchHotelDao = null;
        ReadXml readXml;
        AuAuthCheck auCheck;

        memberFlag = false;
        paymemberFlag = false;
        paymemberTempFlag = false;
        // url = "";
        pageLinks = "";
        pageHeader = null;
        queryString = "";
        ret = false;
        carrierFlag = 0;
        hotelCount = 0;
        hotelAllCount = 0;
        paramCenter = "";
        cu = new CreateUrl();
        con = new HttpConnection();
        she = new SearchHotelEmpty();
        dhd = null;
        coordinateLat = 0;
        coordinateLon = 0;
        coordinateUrLat = 0;
        coordinateUrLon = 0;
        coordinateDlLat = 0;
        coordinateDlLon = 0;
        hotelIdList = null;
        distance = null;
        termNo = "";

        carrierFlag = UserAgent.getUserAgentType( request );
        dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
        paramUidLink = (String)request.getAttribute( "UID-LINK" );
        paramAcRead = request.getParameter( "acread" );

        String gpsUrl = null;
        try
        {

            // ユーザー情報の取得
            if ( dataLoginInfo_M2 != null )
            {
                memberFlag = dataLoginInfo_M2.isMemberFlag();
                paymemberFlag = dataLoginInfo_M2.isPaymemberFlag();
                paymemberTempFlag = dataLoginInfo_M2.isPaymemberTempFlag();
                registStatus = dataLoginInfo_M2.getRegistStatus();
                delFlag = dataLoginInfo_M2.getDelFlag();
                carrierFlag = dataLoginInfo_M2.getCarrierFlag();
                this.um = new UserMap();
                this.um.getData( dataLoginInfo_M2.getUserId() );
            }
            else
            {
                // キャリアフラグと端末番号を取得する
                if ( carrierFlag == UserAgent.USERAGENT_AU )
                {
                    termNo = request.getHeader( "x-up-subno" );
                }
                else if ( carrierFlag == UserAgent.USERAGENT_VODAFONE )
                {
                    termNo = request.getHeader( "x-jphone-uid" );
                    termNo = termNo.substring( 1 );
                }
                else if ( carrierFlag == UserAgent.USERAGENT_DOCOMO )
                {
                    termNo = request.getParameter( "uid" );
                }

                memberFlag = false;
                paymemberFlag = false;
                paymemberTempFlag = false;
                registStatus = 0;
                delFlag = 1;
                this.um = new UserMap();
                if ( termNo.equals( "" ) == false )
                {
                    this.um.getData( termNo );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionGpsSearchPay dataLoginInfo] Exception:" + e.toString() );
        }

        paramAndWord = request.getParameter( "andword" );
        paramAndWordEnc = "";
        paramLat = request.getParameter( "lat" );
        paramLon = request.getParameter( "lon" );
        paramPos = request.getParameter( "pos" );
        paramAddr = request.getParameter( "addr" );
        paramPage = request.getParameter( "page" );
        paramScale = request.getParameter( "scale" );
        paramEmpty = request.getParameter( "empty" );
        paramGqs = request.getParameter( "gps" );
        paramId = request.getParameter( "hotel_id" );
        paramGo = request.getParameter( "go" );

        if ( paramLat == null )
        {
            paramLat = "";
        }
        if ( paramLon == null )
        {
            paramLon = "";
        }
        if ( paramPos == null )
        {
            paramPos = "";
        }
        if ( paramAddr == null )
        {
            paramAddr = "";
        }
        if ( (paramPage == null) || (paramPage.compareTo( "" ) == 0) || (CheckString.numCheck( paramPage ) == false) )
        {
            paramPage = "0";
        }
        if ( (paramScale == null) || (paramScale.compareTo( "" ) == 0) || (CheckString.numCheck( paramScale ) == false) )
        {
            paramScale = "0";
        }
        if ( (paramEmpty == null) || (paramEmpty.compareTo( "" ) == 0) || (CheckString.numCheck( paramEmpty ) == false) )
        {
            paramEmpty = "0";
        }
        if ( paramGqs == null )
        {
            paramGqs = "";
        }
        if ( (paramId == null) || (paramId.compareTo( "" ) == 0) || (CheckString.numCheck( paramId ) == false) )
        {
            paramId = "0";
        }
        if ( (paramGo == null) || (paramGo.compareTo( "" ) == 0) || (CheckString.numCheck( paramGo ) == false) )
        {
            paramGo = "0";
        }
        if ( paramAndWord == null )
        {
            paramAndWord = "";
        }
        else
        {
            try
            {
                paramAndWord = new String( paramAndWord.getBytes( "8859_1" ), "Windows-31J" );
                // auでの文字化け対策
                paramAndWordEnc = URLEncoder.encode( paramAndWord, "Shift_JIS" );
            }
            catch ( Exception e )
            {
                paramAndWord = "";
            }
        }
        pageNum = Integer.parseInt( paramPage );

        // キャリアによって出す画像タイプを変更
        if ( (carrierFlag == DataMasterUseragent.CARRIER_AU) || (carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK) )
        {
            cu.setImgType( "PNG" );
            Logging.info( "[ActionGpsSearchPay]:PNGセット" );
        }
        else if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
        {
            cu.setImgType( "GIF" );
            Logging.info( "[ActionGpsSearchPay]:GIFセット" );
        }

        // ホテルIDがあったらそこを中心に表示する
        if ( Integer.parseInt( paramId ) > 0 )
        {
            queryString = "searchGpsPay.act?gps=1&scale=" + paramScale + "&lat=" + paramLat + "&lon=" + paramLon
                    + "&empty=" + paramEmpty;
            if ( paramAndWord.compareTo( "" ) != 0 )
            {
                queryString += "&andword=" + paramAndWordEnc;
            }
            if ( this.um != null )
            {
                ret = true;
            }

            Logging.info( "==================[ActionGpsSearchPay] paramId >0 : " + paramLat + "/" + paramLon );

        }
        // 位置情報を取得するデータがある場合（GPSの結果が返ってきた場合：世界測地系）
        else if ( (paramLat.compareTo( "" ) != 0 && paramLon.compareTo( "" ) != 0) || paramPos.compareTo( "" ) != 0 )
        {
            Logging.info( "ActionGpsSearchPay 位置情報取得:キャリアフラグ" + carrierFlag );

            // 現状取得したものをリンクURLに設定
            queryString = "searchGpsPay.act?gps=1&scale=" + paramScale + "&lat=" + paramLat + "&lon=" + paramLon
                    + "&empty=" + paramEmpty;
            if ( paramAndWord.compareTo( "" ) != 0 )
            {
                queryString += "&andword=" + paramAndWordEnc;
            }
            try
            {
                if ( paramGqs.compareTo( "" ) == 0 )
                {
                    // ソフトバンクはposから位置情報を分割する
                    if ( carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK )
                    {
                        if ( paramPos.compareTo( "" ) != 0 )
                        {
                            i = paramPos.indexOf( "E" );
                            if ( i != -1 )
                            {
                                paramLat = paramPos.substring( 1, i );
                                paramLon = paramPos.substring( i + 1, paramPos.length() );
                            }
                            else
                            {
                                i = paramPos.indexOf( "W" );
                                if ( i != -1 )
                                {
                                    paramLat = paramPos.substring( 1, i );
                                    paramLon = paramPos.substring( i + 1, paramPos.length() );
                                }
                                else
                                {
                                    paramLat = "";
                                    paramLon = "";
                                }
                            }
                        }

                    }
                    else if ( carrierFlag == DataMasterUseragent.CARRIER_ETC )
                    {
                        try
                        {
                            response.sendRedirect( "/index.jsp" );
                        }
                        catch ( Exception e )
                        {
                            Logging.error( "[ActionGpsSearchPay sendRedirect] Exception" + e.toString() );
                        }
                        return;
                    }

                    Logging.info( "==================[ActionGpsSearchPay convertGeodesic]" );
                    // GPSの座標をいろいろな測地系に変換し、地図表示へ
                    cg = new ConvertGeodesic();
                    cg.convertDms2Degree( paramLat, paramLon );
                    paramLat = Double.toString( cg.getLatWGS() );
                    paramLon = Double.toString( cg.getLonWGS() );
                    queryString = "searchGpsPay.act?gps=1&scale=" + paramScale + "&lat=" + paramLat + "&lon="
                            + paramLon + "&empty=" + paramEmpty;
                    if ( paramAndWord.compareTo( "" ) != 0 )
                    {
                        queryString += "&andword=" + paramAndWordEnc;
                    }

                    Logging.info( "==================[ActionGpsSearchPay registAddress]" );
                    // 緯度経度から住所を登録する
                    cu.setPoint( "WGS84," + paramLat + "," + paramLon );
                    cu.setMlv( 4 );
                    cu.setLat( paramLat );
                    cu.setLon( paramLon );
                    readXml = new ReadXml( cu.geoDecode() );
                    // 住所を取得
                    readXml.getElementAddr();
                    // 住所を登録する
                    if ( dataLoginInfo_M2 != null )
                    {
                        this.um.registAddress( dataLoginInfo_M2.getUserId(), readXml.getAddress() );
                    }
                    else
                    {
                        // 非会員は端末番号で登録
                        this.um.registAddress( termNo, readXml.getAddress() );
                    }
                    cu = null;
                    cu = new CreateUrl();

                }
                cu.setOutdatum( "WGS84" );
                cu.setPos( "II5G:PWGS84," + paramLat + "," + paramLon );

                // 携帯向けに画像の自動調整を有効にする
                cu.setWm( true );
                cu.setHm( true );

                Logging.info( "==================[ActionGpsSearchPay] 地図表示" );
                cu.setScale( this.getScale( Integer.parseInt( paramScale ) ) );
                cu.setPindefault( paramLat + "," + paramLon );
                cu.setLat( paramLat );
                cu.setLon( paramLon );
                gpsUrl = cu.getMapURL();
                cu = null;
                cu = new CreateUrl();
                ret = con.urlConnection( request, response, gpsUrl );
            }
            catch ( Exception e )
            {
                Logging.info( "[ActionGpsSearchPay  test] Exception:" + e.toString() );
            }
        }
        // 住所が入力されていた場合
        else if ( paramAddr.compareTo( "" ) != 0 )
        {
            Logging.info( "==================[ActionGpsSearchPay] 住所で検索" );

            queryString = "searchGpsPay.act?gps=1&empty=" + paramEmpty;
            if ( paramAndWord.compareTo( "" ) != 0 )
            {
                queryString += "&andword=" + paramAndWordEnc;

            }
            try
            {
                paramAddr = new String( paramAddr.getBytes( "8859_1" ), "Shift_JIS" );
                cu = new CreateUrl();

                // 住所でlat,lon取得
                cu.setAddress( paramAddr );
                readXml = new ReadXml( cu.geoDecode() );
                readXml.getElementAddr();
                this.replaceGeodesic( readXml.getCoordinate() );
                paramLat = this.lat;
                paramLon = this.lon;

                // 地図表示
                cu = null;
                cu = new CreateUrl();
                cu.setPindefault( paramLat + "," + paramLon );
                cu.setLat( paramLat );
                cu.setLon( paramLon );
                cu.setScale( this.getScale( Integer.parseInt( paramScale ) ) );
                gpsUrl = cu.getMapURL();

                ret = false;
                ret = con.urlConnection( request, response, gpsUrl );
            }
            catch ( UnsupportedEncodingException e )
            {
                Logging.info( "[ActionGpsSearchPay] Exception:" + e.toString() );
            }

            catch ( Exception e )
            {
                Logging.info( "[ActionGpsSearchPay] Exception:" + e.toString() );
            }
        }
        else
        {

            try
            {
                if ( dataLoginInfo_M2 != null )
                {
                    request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                }
                if ( Integer.parseInt( paramGo ) == 0 )
                {
                    requestDispatcher = request.getRequestDispatcher( "search_gps_pay_error.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( "search_gps_pay.jsp" );
                }
                requestDispatcher.forward( request, response );
                return;
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionGpsSearchPay] Exception:" + e.toString() );
            }
        }

        // HTTP通信に成功したら共通項目へ
        if ( ret != false || paramAddr.compareTo( "" ) != 0 )
        {
            Logging.info( "==================[ActionGpsSearchPay] HTTP通信に成功したら共通項目へ" );
            try
            {
                // ホテルIDがない場合のみ登録する（ホテルIDがある場合は、地図表示済みのため）
                if ( Integer.parseInt( paramId ) == 0 )
                {
                    Logging.info( "==================[ActionGpsSearchPay] ホテルIDがない場合、登録する" );
                    if ( dataLoginInfo_M2 != null )
                    {
                        ret = this.um.registUserMap( dataLoginInfo_M2.getUserId(), con );
                    }
                    else
                    {
                        ret = this.um.registUserMap( termNo, con );
                    }
                }

                cg = new ConvertGeodesic();

                Logging.info( "==================[ActionGpsSearchPay] 座標設定" );

                try
                {
                    // 左下の座標をlat,lonに分ける
                    this.replaceGeodesic( this.um.getUserMapInfo().getCoordinateDL() );
                    cg.convertDegree( this.lat, this.lon );
                    coordinateDlLat = cg.getLatTOKYONum();
                    coordinateDlLon = cg.getLonTOKYONum();

                    // 右上の座標をlat,lonに分ける
                    this.replaceGeodesic( this.um.getUserMapInfo().getCoordinateUR() );
                    cg.convertDegree( this.lat, this.lon );
                    coordinateUrLat = cg.getLatTOKYONum();
                    coordinateUrLon = cg.getLonTOKYONum();

                    // 中心の座標をlat,lonに分ける
                    this.replaceGeodesic( this.um.getUserMapInfo().getCoordinate() );
                    cg.convertDegree( this.lat, this.lon );
                    // 日本測地系へ変換
                    coordinateLat = cg.getLatTOKYONum();
                    coordinateLon = cg.getLonTOKYONum();

                    Logging.info( "==================[ActionGpsSearchPay] 座標 ur:" + coordinateUrLat + "/" + coordinateUrLon );
                    Logging.info( "==================[ActionGpsSearchPay] 座標 dl:" + coordinateDlLat + "/" + coordinateDlLon );
                }
                catch ( NumberFormatException e )
                {
                    Logging.error( "[ActionGpsSearchPay] replaceGeodesic NumberFormatException" + e );
                }

                if ( paramAddr.compareTo( "" ) != 0 )
                {
                    queryString += "&lat=" + paramLat + "&lon=" + paramLon;
                    if ( paramAndWord.compareTo( "" ) != 0 )
                    {
                        queryString += "&andword=" + paramAndWordEnc;

                    }
                }

                if ( paramGqs.compareTo( "" ) == 0 )
                {
                    // 緯度経度から住所を登録する
                    cu.setPoint( "WGS84," + paramLat + "," + paramLon );
                    cu.setMlv( 4 );
                    cu.setLat( paramLat );
                    cu.setLon( paramLon );
                    readXml = new ReadXml( cu.geoDecode() );
                    // 住所を取得
                    readXml.getElementAddr();
                    // 住所を登録する
                    if ( dataLoginInfo_M2 != null )
                    {
                        this.um.registAddress( dataLoginInfo_M2.getUserId(), readXml.getAddress() );
                    }
                    else
                    {
                        this.um.registAddress( termNo, readXml.getAddress() );
                    }
                }

                Logging.info( "==================[ActionGpsSearchPay] 指定範囲内からホテル探す" );
                // 指定範囲内からホテルを探す
                ret = she.getSearchHotel( coordinateDlLat, coordinateDlLon, coordinateUrLat, coordinateUrLon,
                        coordinateLat, coordinateLon, Integer.parseInt( paramEmpty ) );
                if ( ret != false )
                {
                    Logging.info( "==================[ActionGpsSearchPay] ホテル有" );
                    // ホテル数、ホテルIDリスト、緯度経度を取得
                    hotelAllCount = she.getHotelAllCount();
                    hotelIdList = she.getHotelId();
                    distance = she.getDistance();
                    strPosIcon = "WGS84,";

                    // 絞込があった場合の処理
                    if ( paramAndWord != null && (paramAndWord.trim().length() > 0) )
                    {
                        // This will set the current ids
                        searchHotelCommon = new SearchHotelCommon();
                        searchHotelCommon.setEquipHotelList( hotelIdList );
                        Logging.info( "GPS検索IDリスト" + hotelIdList.length );

                        try
                        {
                            Logging.info( paramAndWord );

                            searchHotelFreeWord = new SearchHotelFreeword_M2();
                            // フリーワード検索用のホテルIDリスト
                            arrHotelIdList = searchHotelFreeWord.getSearchIdList( paramAndWord );
                            Logging.info( "フリーワード検索IDリスト" + arrHotelIdList.length );

                            // フリーワード検索用のホテルIDリストをセット
                            searchHotelCommon.setResultHotelList( arrHotelIdList );

                            // GPSのホテルIDリストとフリーワード検索用のホテルIDリストをマージする
                            hotelIdList = searchHotelCommon.getMargeHotel( pageRecords, pageNum );
                            Logging.info( "マージ結果IDリスト" + hotelIdList.length );

                            she = null;
                            she = new SearchHotelEmpty();
                            ret = she.getSearchHotel( hotelIdList, coordinateLat, coordinateLon,
                                    Integer.parseInt( paramEmpty ) );
                            if ( ret != false )
                            {
                                // ホテル数、ホテルIDリスト、緯度経度を取得
                                hotelAllCount = she.getHotelAllCount();
                                hotelIdList = she.getHotelId();
                                distance = she.getDistance();
                            }

                            pageHeader = "「" + paramAndWord + "」で絞込み検索しました";
                        }
                        catch ( UnsupportedEncodingException e )
                        {
                            Logging.error( "[ActionGpsSearchMobile.execute() : AndWord = " + paramAndWord
                                    + " ] Exception=" + e.toString() );
                            throw e;
                        }
                    }

                    Logging.info( "==================[ActionGpsSearchPay] ホテルリスト取得" );
                    // 表示するページのlat.lonを取得
                    dhd = this.getHotelList( she.getHotelDistance(), pageRecords, pageNum );

                    // 検索結果で表示させるホテルのデータ取得する
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );
                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();
                    currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords, hotelAllCount,
                            hotelCount, dispFormat );
                    dataSearchResult = new DataSearchResult_M2();
                    String icon = "";

                    Logging.info( "==================[ActionGpsSearchPay] ホテルリスト有" );
                    if ( dhd != null )
                    {
                        String lastIcon = "";
                        String pin = "";
                        // 絞り込んだDataHotelDistanceから表示させるpinを作る
                        for( i = dhd.length - 1 ; i >= 0 ; i-- )
                        {
                            pin += "&pin" + (i + 1) + "=" + dhd[i].getHotelLat() + "," + dhd[i].getHotelLon();

                            if ( paymemberFlag == false )
                            {
                                icon += "II" + (3 * i + 14) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                        + dhd[i].getHotelLon();
                                strPosIcon = "II" + (3 * i + 14) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                        + dhd[i].getHotelLon();
                            }
                            else
                            {
                                // 不明
                                if ( arrDataSearchHotel[i].getEmptyStatus() == 0 )
                                {
                                    icon += "II" + (3 * i + 14) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                            + dhd[i].getHotelLon();
                                    strPosIcon = "II" + (3 * i + 14) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                            + dhd[i].getHotelLon();
                                    pin += ",,green";
                                }
                                // 空室
                                else if ( arrDataSearchHotel[i].getEmptyStatus() == 1 )
                                {
                                    icon += "II" + (3 * i + 12) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                            + dhd[i].getHotelLon();
                                    strPosIcon = "II" + (3 * i + 12) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                            + dhd[i].getHotelLon();
                                    pin += ",,blue";
                                }
                                // 満室
                                else if ( arrDataSearchHotel[i].getEmptyStatus() == 2 )
                                {
                                    /*
                                     * icon += "II" + (3 * i + 13) + "G:PWGS84," + dhd[i].getHotelLat() + "," + dhd[i].getHotelLon();
                                     * strPosIcon = "II" + (3 * i + 13) + "G:PWGS84," + dhd[i].getHotelLat() + "," + dhd[i].getHotelLon();
                                     */
                                    // 満室でも不明扱いとする
                                    icon += "II" + (3 * i + 14) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                            + dhd[i].getHotelLon();
                                    strPosIcon = "II" + (3 * i + 14) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                            + dhd[i].getHotelLon();
                                }
                            }

                            // 取得したホテルIDと同じ場合、ホテルの位置情報を取得する
                            if ( dhd[i].getId() == Integer.parseInt( paramId ) )
                            {
                                paramCenter = dhd[i].getHotelLat() + "," + dhd[i].getHotelLon();
                                Logging.info( "==================[ActionGpsSearchPay] ホテル位置情報 : " + paramLat + "//" + paramLon );
                                lastIcon = strPosIcon;
                            }
                            icon += ":";
                        }
                        icon += "II5G:PWGS84," + cg.getLatWGS() + "," + cg.getLonWGS();
                        if ( lastIcon.compareTo( "" ) != 0 )
                        {
                            icon += ":" + lastIcon;
                        }

                        cu.setOutdatum( "WGS84" );
                        // 携帯向けに画像の自動調整を有効にする
                        cu.setWm( true );
                        cu.setHm( true );
                        cu.setAddress( "" );
                        // ホテルのピンをセットして
                        cu.setPos( icon );

                        cu.setPin( pin );

                        // ホテルIDがあれば、ホテルの位置情報をセットする
                        if ( Integer.parseInt( paramId ) == 0 )
                        {
                            cu.setScale( this.getScale( Integer.parseInt( paramScale ) ) );
                            cu.setPindefault( paramLat + "," + paramLon );
                            gpsUrl = cu.getMapURL();
                            ret = con.urlConnection( request, response, gpsUrl );
                            if ( ret != false )
                            {
                                if ( dataLoginInfo_M2 != null )
                                {
                                    ret = this.um.registUserMap( dataLoginInfo_M2.getUserId(), con );
                                }
                                else
                                {
                                    ret = this.um.registUserMap( termNo, con );
                                }

                            }
                        }
                        else
                        {
                            cu.setScale( this.getScale( Integer.parseInt( paramScale ) ) );
                            // cu.setPindefault( paramCenter );
                            cu.setC( paramCenter );
                            // cu.setLat( paramLat );
                            // cu.setLon( paramLon );
                            gpsUrl = cu.getMapURL();
                            ret = con.urlConnection( request, response, gpsUrl );
                            queryString = "searchGpsPay.act?gps=1&scale="
                                    + Integer.toString( this.getScaleTo( Integer.parseInt( this.um.getUserMapInfo()
                                            .getScale() ) ) ) +
                                    "&lat=" + paramLat + "&lon=" + paramLon + "&empty=" + paramEmpty;
                            if ( paramAndWord.compareTo( "" ) != 0 )
                            {
                                queryString += "&andword" + paramAndWordEnc;

                            }
                            if ( ret != false )
                            {
                                if ( dataLoginInfo_M2 != null )
                                {
                                    ret = this.um.registImage( dataLoginInfo_M2.getUserId(), con );
                                }
                                else
                                {
                                    ret = this.um.registImage( termNo, con );
                                }
                            }
                        }

                        Logging.info( "==================[ActionGpsSearchPay] 1 : " + paramLat + "//" + paramLon );

                    }

                    if ( hotelAllCount > pageRecords )
                    {
                        pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, pageRecords, hotelAllCount,
                                queryString, paramUidLink );
                        dataSearchResult.setPageLink( pageLinks );
                    }
                    dataSearchResult.setRecordsOnPage( currentPageRecords );
                    dataSearchResult.setHotelCount( hotelCount );
                    dataSearchResult.setHotelAllCount( hotelAllCount );
                    if ( pageHeader != null )
                    {
                        dataSearchResult.setPageHeader( pageHeader );
                    }
                    dataSearchResult.setDataSearchHotel( arrDataSearchHotel );

                    dataSearchResult.setParamParameter1( paramLat );
                    dataSearchResult.setParamParameter2( paramLon );
                    dataSearchResult.setParamParameter3( paramScale );
                    request.setAttribute( "DATAHOTELDISTANCE", dhd );
                    Logging.info( "==================[ActionGpsSearchPay] 2 : " + paramLat + "//" + paramLon );
                }

                if ( this.um != null )
                {
                    request.setAttribute( "USERMAP", this.um );
                }
                request.setAttribute( "HOTELID", paramId );
                request.setAttribute( "SCALE", paramScale );
                if ( dataLoginInfo_M2 != null )
                {
                    request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                }
                request.setAttribute( "DISPMAP", "true" );
                request.setAttribute( "EMPTY", paramEmpty );
                request.setAttribute( "PAGE", paramPage );
                request.setAttribute( "ANDWORD", paramAndWord );
                request.setAttribute( "url", gpsUrl );
                if ( dataSearchResult == null )
                {
                    dataSearchResult = new DataSearchResult_M2();
                    dataSearchResult.setParamParameter1( paramLat );
                    dataSearchResult.setParamParameter2( paramLon );
                    dataSearchResult.setParamParameter3( paramScale );
                    dataSearchResult.setPageHeader( pageHeader );
                    dataSearchResult.setHotelCount( hotelCount );
                    dataSearchResult.setHotelAllCount( hotelAllCount );
                }
                Logging.info( "==================[ActionGpsSearchPay] 3 : " + paramLat + "//" + paramLon );
                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                requestDispatcher = request.getRequestDispatcher( "search_result_gps_pay.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionGpsSearchPay HTTPConnection=true]Exception" + e.toString() );
            }
        }
    }

    /**
     * yahooAPIのdms表記から置換する（xx/xx/xx.xx → xx.xx.xx.xx）
     * 
     * @param coordinate 座標
     */
    private void replaceGeodesic(String coordinate)
    {
        int nIndex;
        nIndex = 0;

        coordinate = coordinate.replaceAll( "WGS84,", "" );
        nIndex = coordinate.indexOf( "," );
        if ( nIndex != -1 )
        {
            this.lat = coordinate.substring( nIndex + 1 );
            this.lat = this.lat.replaceAll( "/", "." );
            this.lon = coordinate.substring( 0, nIndex );
            this.lon = this.lon.replaceAll( "/", "." );

        }
    }

    /**
     * 縮尺を取得する
     * 
     * @param scale スケール。縮尺
     * @return 処理結果(5000〜1000000までの縮尺)
     * 
     */
    private int getScale(int scale)
    {
        int dispScale;

        switch( scale )
        {
            case -4:
                dispScale = 5000;
                break;
            case -3:
                dispScale = 5000;
                break;
            case -2:
                dispScale = 10000;
                break;
            case -1:
                dispScale = 25000;
                break;
            case 0:
                dispScale = 70000;
                break;
            case 1:
                dispScale = 250000;
                break;
            case 2:
                dispScale = 500000;
                break;
            case 3:
                dispScale = 1000000;
                break;
            case 4:
                dispScale = 3000000;
                break;
            default:
                dispScale = 70000;
        }
        return(dispScale);
    }

    /**
     * 縮尺から今の段階を取得する
     * 
     * @param scale スケール。縮尺
     * @return 処理結果(5000〜1000000までの縮尺)
     * 
     */
    private int getScaleTo(int scale)
    {
        int dispScale;

        switch( scale )
        {
            case 5000:
                dispScale = -3;
                break;
            case 10000:
                dispScale = -2;
                break;
            case 25000:
                dispScale = -1;
                break;
            case 70000:
                dispScale = 0;
                break;
            case 250000:
                dispScale = 1;
                break;
            case 500000:
                dispScale = 2;
                break;
            case 1000000:
                dispScale = 3;
                break;
            case 3000000:
                dispScale = 4;
                break;

            default:
                dispScale = 0;
        }
        return(dispScale);
    }

    /**
     * ホテルリストを取得する
     * 
     * @param DataHotelDistance ホテル距離、緯度経度取得クラス
     * @param countNum 取得する件数
     * @param pageNum ページ数
     * @return DataHotelDistance ホテル距離、緯度経度取得クラス
     * 
     */
    private DataHotelDistance[] getHotelList(DataHotelDistance[] dhd, int countNum, int pageNum)
    {
        DataHotelDistance[] dhDistance;
        int allCount;
        int count;
        int loop;
        int k;

        dhDistance = null;
        if ( dhd != null )
        {
            allCount = dhd.length;
            if ( allCount > 0 )
            {

                count = 0;
                for( loop = countNum * pageNum ; loop < allCount ; loop++ )
                {
                    count++;
                    if ( count >= countNum && countNum != 0 )
                        break;
                }

                dhDistance = new DataHotelDistance[count];
                int start = countNum * pageNum;
                for( k = 0 ; k < count ; k++ )
                {
                    dhDistance[k] = dhd[start];
                    start++;
                }
            }
        }
        return(dhDistance);
    }

}
