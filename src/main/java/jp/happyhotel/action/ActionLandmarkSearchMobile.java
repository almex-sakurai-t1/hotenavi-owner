package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.ConvertGeodesic;
import jp.happyhotel.common.ConvertScale;
import jp.happyhotel.common.CreateUrl;
import jp.happyhotel.common.HttpConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.ReadXml;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelDistance;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.others.MapSpot;
import jp.happyhotel.others.MasterSpot;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelEmpty;
import jp.happyhotel.search.SearchHotelFreeword_M2;
import jp.happyhotel.user.UserMap;

/**
 * ランドマーク検索制御クラス
 * 
 * 
 * @author S.Tashiro
 * @version 1.00 2010/04/30
 */

public class ActionLandmarkSearchMobile extends BaseAction
{

    static int                 pageRecords       = Constants.pageLimitRecordMobile;
    static int                 maxRecords        = Constants.maxRecordsMobile;
    public static final int    dispFormat        = 1;
    public static final int    RECOMMEND         = 1;
    public static final int    NOT_RECOMMEND     = 0;
    public static final int    SPOT_COUNT        = 20;                             // TOPぺージ スポット一覧の表示件数
    public static final int    WIDTH             = 240;
    public static final int    HEIGHT            = 240;
    public static final String PNG               = "PNG";
    public static final String GIF               = "GIF";

    private RequestDispatcher  requestDispatcher = null;
    private DataLoginInfo_M2   dataLoginInfo_M2  = null;
    private String             lat               = "";
    private String             lon               = "";
    private int                hotelAllCount     = 0;
    private int                hotelCount        = 0;
    private UserMap            um                = null;
    private MapSpot            mapSpot           = null;

    /**
     * 任意のランドマーク付近のホテルを検索
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param response サーバからクライアントへのレスポンス
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        // 定義
        boolean ret;
        boolean spotRet;
        boolean boolAdd = false;
        int i;
        int carrierFlag;
        int pageNum;
        // int nDefaultScale;
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
        String paramAcRead;
        String paramUidLink = null;
        String paramLocalId;
        String paramPrefId;
        String paramSpotId;
        String paramSeq;
        String paramScale;
        String sendUrl;
        String paramId;
        String paramEmpty; // 1:空室を検索、2:詳細のホテルを検索
        String pageHeader;
        String paramCenter;
        String paramPage; // ページを管理するパラメータ

        String pageLinks;
        String queryString;
        String strLat;
        String strLon;
        String strDispMapUrl;
        String strPosIcon;
        String currentPageRecords = null;
        String strImgType = "";
        String paramCircle;
        String url = null;
        String pin = "";

        AuAuthCheck auCheck;
        CreateUrl cu;
        ConvertGeodesic cg;
        DataHotelBasic dhb;
        DataHotelDistance[] dhd;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        HttpConnection con;
        SearchHotelEmpty she;
        SearchHotelFreeword_M2 searchHotelFreeWord;
        SearchHotelCommon searchHotelCommon;
        SearchHotelDao_M2 searchHotelDao = null;
        ReadXml readXml;
        MasterSpot masterSpot = null;

        ret = false;
        spotRet = false;
        // nDefaultScale = 0;
        sendUrl = "";
        queryString = "";
        strLat = "";
        strLon = "";
        strDispMapUrl = "";
        pageLinks = "";
        paramCenter = "";
        pageHeader = null;
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

        carrierFlag = UserAgent.getUserAgentType( request );
        dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
        paramUidLink = (String)request.getAttribute( "UID-LINK" );
        paramAcRead = request.getParameter( "acread" );

        paramLocalId = request.getParameter( "local_id" );
        paramPrefId = request.getParameter( "pref_id" );
        paramSpotId = request.getParameter( "spot_id" );
        paramSeq = request.getParameter( "seq" );
        paramScale = request.getParameter( "scale" );
        paramId = request.getParameter( "hotel_id" );
        paramEmpty = request.getParameter( "empty" );
        paramPage = request.getParameter( "page" );
        paramCircle = request.getParameter( "circle" );

        if ( (paramLocalId == null) || (paramLocalId.compareTo( "" ) == 0)
                || (CheckString.numCheck( paramLocalId ) == false) )
        {
            paramLocalId = "0";
        }
        if ( (paramPrefId == null) || (paramPrefId.compareTo( "" ) == 0) || (CheckString.numCheck( paramPrefId ) == false) )
        {
            paramPrefId = "0";
        }
        if ( (paramSpotId == null) || (paramSpotId.compareTo( "" ) == 0) || (CheckString.numCheck( paramSpotId ) == false) )
        {
            paramSpotId = "0";
        }
        if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || (CheckString.numCheck( paramSeq ) == false) )
        {
            paramSeq = "0";
        }
        if ( (paramScale == null) || (paramScale.compareTo( "" ) == 0) || (CheckString.numCheck( paramScale ) == false) )
        {
            paramScale = "0";
        }
        if ( (paramId == null) || (paramId.compareTo( "" ) == 0) || (CheckString.numCheck( paramId ) == false) )
        {
            paramId = "0";
        }
        if ( (paramPage == null) || (paramPage.compareTo( "" ) == 0) || (CheckString.numCheck( paramPage ) == false) )
        {
            paramPage = "0";
        }
        if ( (paramEmpty == null) || (paramEmpty.compareTo( "" ) == 0) || (CheckString.numCheck( paramEmpty ) == false) )
        {
            paramEmpty = "0";
        }
        if ( (paramCircle == null) || (paramCircle.compareTo( "" ) == 0) )
        {
            paramCircle = "";
        }

        // キャリアで表示する画像を変える
        if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
        {
            strImgType = GIF;
        }
        else
        {
            strImgType = PNG;
        }

        // 検索結果ページだけ有料会員チェックを行う。
        if ( Integer.parseInt( paramSeq ) > 0 )
        {
            // auだったらアクセスチケットをチェックする
            if ( (paramAcRead == null) && (carrierFlag == DataMasterUseragent.CARRIER_AU) )
            {
                try
                {
                    auCheck = new AuAuthCheck();
                    ret = auCheck.authCheckForClass( request, "tokushu/transfer_landmark.jsp?" + paramUidLink );
                    // アクセスチケット確認の結果 falseだったらリダイレクト
                    if ( ret == false )
                    {
                        response.sendRedirect( auCheck.getResultData() );
                        return;
                    }
                    // アクセスチケット確認の結果 trueだったら情報を取得
                    else
                    {
                        // DataLoginInfo_M2を取得する
                        if ( auCheck.getDataLoginInfo() != null )
                        {
                            dataLoginInfo_M2 = auCheck.getDataLoginInfo();
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[ActionLandMarkSearchMobile AuAuthCheck] Exception:" + e.toString() );
                }
            }

            try
            {
                // ユーザー情報の取得
                if ( dataLoginInfo_M2 != null )
                {
                    // 有料会員登録途中のユーザーは有料会員登録ページへ
                    if ( dataLoginInfo_M2.isMemberFlag() == false && dataLoginInfo_M2.isPaymemberTempFlag() != false )
                    {
                        response.sendRedirect( "../../free/mymenu/paymemberRegist.act?" + paramUidLink );
                        return;
                    }
                    // 有料会員登録を行っていないユーザーは空満検索紹介ページへ
                    else if ( dataLoginInfo_M2.isPaymemberFlag() == false
                            && dataLoginInfo_M2.isPaymemberTempFlag() == false )
                    {
                        response.sendRedirect( "../../tokushu/transfer_landmark.jsp?" + paramUidLink );
                        return;
                    }

                }
                else
                {
                    response.sendRedirect( "../../tokushu/transfer_landmark.jsp?" + paramUidLink );
                    return;
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionLandMarkSearchMobile dataLoginInfo] Exception:" + e.toString() );
            }
        }

        pageNum = Integer.parseInt( paramPage );
        // spot_idがない場合はTOPページへ
        if ( Integer.parseInt( paramSpotId ) == 0 )
        {
            try
            {
                masterSpot = new MasterSpot();
                mapSpot = new MapSpot();
                queryString = "searchLandmarkMobile.act?spot_id=0";

                // hh_master_spotデータ一覧を取得しセット
                spotRet = masterSpot.getMasterSpot( SPOT_COUNT, pageNum );
                if ( spotRet != false )
                {
                    request.setAttribute( "MASTER_SPOT", masterSpot );
                    currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, SPOT_COUNT,
                            masterSpot.getAllCount(), masterSpot.getCount(), dispFormat );
                    request.setAttribute( "PAGE_RECORDS", currentPageRecords );
                    if ( masterSpot.getAllCount() > SPOT_COUNT )
                    {
                        pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, SPOT_COUNT,
                                masterSpot.getAllCount(), queryString, paramUidLink );
                        request.setAttribute( "PAGE_LINK", pageLinks );
                    }
                }

                // トップページで表示させるランドマークデータを取得しセット
                spotRet = mapSpot.getSpotDataByTopDisp();
                if ( spotRet != false )
                {
                    request.setAttribute( "SPOT_DATA", mapSpot );
                }
                requestDispatcher = request.getRequestDispatcher( "index.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionLandMarkSearchMobile sendRedirect:error.jsp] Exception:" + e.toString() );
            }
        }
        // 検索ページ
        if ( Integer.parseInt( paramSeq ) == 0 )
        {
            mapSpot = new MapSpot();
            queryString = "searchLandmarkMobile.act?spot_id=" + paramSpotId + "&local_id=" + paramLocalId + "&pref_id="
                    + paramPrefId;
            // ランドマーク選択ページへ
            if ( Integer.parseInt( paramPrefId ) > 0 )
            {
                spotRet = mapSpot.getMapSpot( Integer.parseInt( paramSpotId ), Integer.parseInt( paramPrefId ),
                        NOT_RECOMMEND, pageRecords, pageNum );
                sendUrl = "landmark_02.jsp";
            }
            // 都道府県選択ページへ
            else if ( Integer.parseInt( paramLocalId ) > 0 )
            {
                boolAdd = mapSpot.getSpotIdListPref( Integer.parseInt( paramSpotId ), Integer.parseInt( paramLocalId ) );
                spotRet = mapSpot.getMapSpot( Integer.parseInt( paramSpotId ), 0, RECOMMEND, pageRecords, pageNum );
                sendUrl = "landmark_01.jsp";
                if ( boolAdd != false )
                {
                    request.setAttribute( "PREF_LIST", mapSpot.getPrefIdList() );
                    request.setAttribute( "SPOT_LIST", mapSpot.getSpotIdList() );
                }
            }
            // 地方選択ページへ
            else
            {
                boolAdd = mapSpot.getSpotIdListLocal( Integer.parseInt( paramSpotId ), 0 );
                spotRet = mapSpot.getMapSpot( Integer.parseInt( paramSpotId ), 0, RECOMMEND, pageRecords, pageNum );
                sendUrl = "landmark_index.jsp";
                if ( boolAdd != false )
                {
                    request.setAttribute( "LOCAL_LIST", mapSpot.getLocalIdList() );
                    request.setAttribute( "SPOT_LIST", mapSpot.getSpotIdList() );
                }
            }

            try
            {
                request.setAttribute( "PREF_ID", paramPrefId );
                request.setAttribute( "LOCAL_ID", paramLocalId );
                if ( dataLoginInfo_M2 != null )
                {
                    request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                }
                request.setAttribute( "SPOT", paramSpotId );
                if ( spotRet != false )
                {
                    request.setAttribute( "SPOT_DATA", mapSpot );
                    // 都道府県IDがあり、１ページの表示件数を超えた場合にページリンクをセット
                    if ( Integer.parseInt( paramPrefId ) > 0 )
                    {
                        currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords,
                                mapSpot.getAllCount(), mapSpot.getCount(), dispFormat );
                        request.setAttribute( "PAGE_RECORDS", currentPageRecords );
                        if ( mapSpot.getAllCount() > pageRecords )
                        {

                            pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, pageRecords,
                                    mapSpot.getAllCount(), queryString, paramUidLink );
                            request.setAttribute( "PAGE_LINK", pageLinks );
                        }
                    }
                }
                requestDispatcher = request.getRequestDispatcher( sendUrl );
                requestDispatcher.forward( request, response );
                return;
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionLandMarkSearchMobile sendRedirect:" + sendUrl + "] Exception:" + e.toString() );
            }
        }
        // 検索結果
        else if ( Integer.parseInt( paramSeq ) > 0 )
        {
            mapSpot = new MapSpot();
            spotRet = mapSpot.getMapSpotBySeq( Integer.parseInt( paramSpotId ), Integer.parseInt( paramSeq ) );
            sendUrl = "landmark_result.jsp";
            queryString = "searchLandmarkMobile.act?spot_id=" + paramSpotId + "&seq=" + paramSeq + "&scale="
                    + paramScale + "&empty=" + paramEmpty;

            // 地図データの取得と周辺のホテルを取得する
            if ( spotRet != false && mapSpot.getAllCount() > 0 )
            {
                cu = new CreateUrl();
                con = new HttpConnection();

                this.um = new UserMap();
                this.um.getData( dataLoginInfo_M2.getUserId() );

                strLat = mapSpot.getMapSpotInfo()[0].getLat();
                strLon = mapSpot.getMapSpotInfo()[0].getLon();

                // ホテルIDが0の場合のみURLをリクエストする
                if ( Integer.parseInt( paramId ) == 0 )
                {
                    /*
                     * // 地図を表示するためのURLにセットしていく
                     * cu.setC("WGS84," + strLat + "," + strLon);
                     * cu.setOutdatum("WGS84");
                     * cu.setPos("II5G:PWGS84," + strLat + "," + strLon);
                     * // 携帯向けに画像の自動調整を有効にする
                     * // cu.setWm( true );
                     * // cu.setHm( true );
                     * cu.setHeight(HEIGHT);
                     * cu.setWidth(WIDTH);
                     * cu.setImgType(strImgType);
                     */

                    cu.setLat( strLat );
                    cu.setLon( strLon );
                    cu.setScale( ConvertScale.getScaleFromLevel( Integer.parseInt( paramScale ) ) );
                    // if ( paramCircle.compareTo( "true" ) == 0 )
                    // {
                    cu.setCircle( "R" + (ConvertScale.getScaleFromLevel( Integer.parseInt( paramScale ) ) * 120) / 10000
                            + ":BFFFFFF:W1:PWGS84," + strLat + "," + strLon );
                    // }
                    // strDispMapUrl = cu.getDrawMap();
                    cu.setPindefault( strLat + "," + strLon );
                    strDispMapUrl = cu.getMapURL();
                    if ( spotRet != false )
                    {
                        // HTTP通信で地図情報を取得
                        ret = con.urlConnection( request, response, strDispMapUrl );
                        // UserMapに登録を行う
                        ret = this.um.registUserMap( dataLoginInfo_M2.getUserId(), con );
                    }
                }
                else
                {
                    if ( this.um != null )
                    {
                        ret = true;
                    }
                    else
                    {
                        ret = false;
                    }
                }

                if ( ret != false )
                {
                    try
                    {
                        cg = new ConvertGeodesic();

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

                        // 指定範囲内からホテルを探す
                        // ホテルIDが指定されていたらhh_user_mapからscaleを取得する
                        if ( Integer.parseInt( paramId ) > 0 )
                        {
                            // 今回はDBで取得したLat、Lonを使用していく（yahooAPIで返ってくるLat、Lonが微妙にずれているため）
                            ret = she.getSearchHotel( coordinateDlLat, coordinateDlLon, coordinateUrLat,
                                    coordinateUrLon, Double.parseDouble( strLat ), Double.parseDouble( strLon ),
                                    Integer.parseInt( paramEmpty ), Integer.parseInt( um.getUserMapInfo().getScale() ) );

                            /*
                             * yahooAPIから取得したLatLonを使用する場合はこちら
                             * ret = she.getSearchHotel( coordinateDlLat, coordinateDlLon, coordinateUrLat, coordinateUrLon, cg.getLatWGS(), cg.getLonWGS(),
                             * Integer.parseInt( paramEmpty ), Integer.parseInt( um.getUserMapInfo().getScale() ) );
                             */

                        }
                        else
                        {
                            // 今回はDBで取得したLat、Lonを使用していく（yahooAPIで返ってくるLat、Lonが微妙にずれているため）
                            ret = she.getSearchHotel( coordinateDlLat, coordinateDlLon, coordinateUrLat,
                                    coordinateUrLon, Double.parseDouble( strLat ), Double.parseDouble( strLon ),
                                    Integer.parseInt( paramEmpty ), ConvertScale.getScaleFromLevel( Integer
                                            .parseInt( paramScale ) ) );

                            // yahooAPIから取得したLatLonを使用する場合はこちら
                            // ret = she.getSearchHotel( coordinateDlLat, coordinateDlLon, coordinateUrLat, coordinateUrLon, cg.getLatWGS(), cg.getLonWGS(), Integer.parseInt( paramEmpty ), ConvertScale.getScaleFromLevel( Integer.parseInt( paramScale ) ) );
                        }

                        if ( ret != false )
                        {
                            // ホテル数、ホテルIDリスト、緯度経度を取得
                            hotelAllCount = she.getHotelAllCount();
                            hotelIdList = she.getHotelId();
                            distance = she.getDistance();
                            strPosIcon = "WGS84,";

                            // 表示するページのlat.lonを取得
                            dhd = this.getHotelList( she.getHotelDistance(), pageRecords, pageNum );

                            // 検索結果で表示させるホテルのデータ取得する
                            searchHotelDao = new SearchHotelDao_M2();
                            searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );
                            arrDataSearchHotel = searchHotelDao.getHotelInfo();
                            hotelCount = searchHotelDao.getCount();
                            hotelAllCount = searchHotelDao.getAllCount();
                            currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords,
                                    hotelAllCount, hotelCount, dispFormat );
                            dataSearchResult = new DataSearchResult_M2();
                            String icon = "";

                            if ( dhd != null )
                            {
                                String lastIcon = "";
                                // 絞り込んだDataHotelDistanceから表示させるpinを作る
                                for( i = dhd.length - 1 ; i >= 0 ; i-- )
                                {
                                    pin += "&pin" + (i + 1) + "=" + dhd[i].getHotelLat() + "," + dhd[i].getHotelLon();

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

                                    // 取得したホテルIDと同じ場合、ホテルの位置情報を取得する
                                    if ( dhd[i].getId() == Integer.parseInt( paramId ) )
                                    {
                                        paramCenter = dhd[i].getHotelLat() + "," + dhd[i].getHotelLon();
                                        lastIcon = strPosIcon;
                                    }
                                    icon += ":";
                                }
                                icon += "II5G:PWGS84," + cg.getLatWGS() + "," + cg.getLonWGS();
                                if ( lastIcon.compareTo( "" ) != 0 )
                                {
                                    icon += ":" + lastIcon;
                                }

                                strDispMapUrl = "";
                                cu = null;
                                cu = new CreateUrl();

                                cu.setOutdatum( "WGS84" );
                                // 携帯向けに画像の自動調整を有効にする
                                // cu.setWm( true );
                                // cu.setHm( true );
                                cu.setHeight( HEIGHT );
                                cu.setWidth( WIDTH );
                                cu.setImgType( strImgType );
                                cu.setAddress( "" );
                                // ホテルのピンをセット
                                cu.setPos( icon );

                                cu.setPin( pin );

                                // ホテルIDがあれば、ホテルの位置情報をセットする
                                if ( Integer.parseInt( paramId ) == 0 )
                                {
                                    cu.setScale( ConvertScale.getScaleFromLevel( Integer.parseInt( paramScale ) ) );
                                    // if ( paramCircle.compareTo( "true" ) == 0 )
                                    // {
                                    cu.setCircle( "R"
                                            + (ConvertScale.getScaleFromLevel( Integer.parseInt( paramScale ) ) * 120)
                                            / 10000 + ":BFFFFFF:W1:PWGS84," + strLat + "," + strLon );
                                    // }
                                    // yahooAPIで取得したLat、Lonを使う場合はこちら
                                    // cu.setC( "WGS84," + cg.getLatWGS() + "," + cg.getLonWGS() );

                                    // DBで取得したLat、Lonを使う場合はこちら
                                    // cu.setC("WGS84," + strLat + "," + strLon);
                                    cu.setLat( strLat );
                                    cu.setLon( strLon );
                                    cu.setPindefault( strLat + "," + strLon );
                                    strDispMapUrl = cu.getMapURL();
                                    ret = con.urlConnection( request, response, strDispMapUrl );
                                    if ( ret != false )
                                    {
                                        ret = this.um.registImage( dataLoginInfo_M2.getUserId(), con );
                                    }
                                }
                                else
                                {
                                    cu.setScale( ConvertScale.getScaleFromLevel( Integer.parseInt( paramScale ) ) );
                                    cu.setC( paramCenter );
                                    // if ( paramCircle.compareTo( "true" ) == 0 )
                                    // {
                                    // 拡大前の縮尺で半径を表示する
                                    cu.setCircle( "R" + (Integer.parseInt( this.um.getUserMapInfo().getScale() ) * 120)
                                            / 10000 + ":BFFFFFF:W1:PWGS84," + strLat + "," + strLon );
                                    // }

                                    cu.setPindefault( paramCenter );
                                    strDispMapUrl = cu.getMapURL();
                                    ret = con.urlConnection( request, response, strDispMapUrl );

                                    queryString = "searchLandmarkMobile.act?spot_id="
                                            + paramSpotId
                                            + "&seq="
                                            + paramSeq
                                            +
                                            "&scale="
                                            + Integer.toString( ConvertScale.getLevelFromScale( Integer.parseInt( this.um
                                                    .getUserMapInfo().getScale() ) ) ) + "&empty=" + paramEmpty;
                                    if ( ret != false )
                                    {
                                        ret = this.um.registImage( dataLoginInfo_M2.getUserId(), con );
                                    }
                                }
                            }
                        }
                    }
                    catch ( Exception e )
                    {
                        Logging.error( "[ActionLandMarkSearchMobile result] Exception:" + e.toString() );
                    }
                }

                try
                {
                    if ( this.um != null )
                    {
                        request.setAttribute( "USERMAP", this.um );
                    }
                    request.setAttribute( "HOTELID", paramId );
                    request.setAttribute( "SCALE", paramScale );
                    request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                    request.setAttribute( "DISPMAP", "true" );
                    request.setAttribute( "EMPTY", paramEmpty );
                    request.setAttribute( "PAGE", paramPage );
                    request.setAttribute( "url", strDispMapUrl );

                    if ( dhd != null )
                    {
                        request.setAttribute( "DATAHOTELDISTANCE", dhd );
                    }

                    if ( dataSearchResult == null )
                    {
                        dataSearchResult = new DataSearchResult_M2();
                    }
                    if ( hotelAllCount > pageRecords )
                    {
                        pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, pageRecords, hotelAllCount,
                                queryString, paramUidLink );
                        dataSearchResult.setPageLink( pageLinks );
                    }
                    if ( currentPageRecords != null )
                    {
                        dataSearchResult.setRecordsOnPage( currentPageRecords );
                    }
                    if ( arrDataSearchHotel != null )
                    {
                        dataSearchResult.setDataSearchHotel( arrDataSearchHotel );
                    }
                    dataSearchResult.setParamParameter1( strLat );
                    dataSearchResult.setParamParameter2( strLon );
                    dataSearchResult.setParamParameter3( paramScale );
                    // dataSearchResult.setPageHeader( pageHeader );
                    dataSearchResult.setHotelCount( hotelCount );
                    dataSearchResult.setHotelAllCount( hotelAllCount );

                    request.setAttribute( "SPOT", paramSpotId );
                    request.setAttribute( "SEQ", paramSeq );
                    if ( spotRet != false )
                    {
                        request.setAttribute( "SPOT_DATA", mapSpot );
                    }

                    request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                    requestDispatcher = request.getRequestDispatcher( "landmark_result.jsp" );
                    requestDispatcher.forward( request, response );

                }
                catch ( Exception e )
                {
                    Logging.error( "[ActionLandMarkSearchMobile requestDispatcher:" + sendUrl + "] Exception:"
                            + e.toString() );
                }
            }
            else
            {
                try
                {
                    request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                    request.setAttribute( "HOTELID", paramId );
                    request.setAttribute( "SCALE", paramScale );
                    request.setAttribute( "DISPMAP", "true" );
                    request.setAttribute( "EMPTY", paramEmpty );
                    request.setAttribute( "PAGE", paramPage );

                    requestDispatcher = request.getRequestDispatcher( "landmark_result.jsp" );
                    requestDispatcher.forward( request, response );
                }
                catch ( Exception e )
                {

                }
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
