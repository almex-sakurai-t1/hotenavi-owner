package jp.happyhotel.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertGeodesic;
import jp.happyhotel.common.CreateUrl;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.DistanceDetermination;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.QueryStrChecker;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelMap;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMapPoint;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataUserRouteQuery;
import jp.happyhotel.search.SearchHotelMapPoint;

/**
 * 
 * ルート表示クラス
 * 
 * @author S.Tashiro
 * @version 1.0 2009/07/10
 */

public class ActionRouteMap extends BaseAction
{

    private RequestDispatcher   requestDispatcher = null;
    private DataLoginInfo_M2    dataLoginInfo_M2  = null;
    private String              termNo            = "";

    private static final String GPS               = "II5G";
    private static final String STATION           = "II8G";
    private static final String IC                = "II7G";
    private static final String HOTEL             = "II6G";

    /**
     * 任意の地点からホテルまでのルートを調べる
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param response サーバからクライアントへのレスポンス
     * @see "/hotelIdのみあった場合、hotel_route_list.jspへ"
     * @see "/hotelId,lat,lon,kindのパラメータがあった場合、hotel_route.jspへ"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean memberFlag;
        boolean paymemberFlag;
        boolean paymemberTempFlag;
        boolean ret;
        int registStatus;
        int delFlag;
        int scale;
        int dispScale;
        int carrierFlag;
        int distance;
        int gpsFlag = 0;
        String paramId;
        String paramMapId;
        String paramDx;
        String paramDy;
        String paramScale;
        String paramDscale;
        String url;
        String strPosIcon;
        String paramPos;
        String paramLat;
        String paramLon;
        String routePoint;
        String paramKind;
        String paramGps;
        String paramPoint;
        String paramUidLink = null;
        String paramAcRead;
        CreateUrl cu;
        ConvertGeodesic cg;
        DataHotelBasic dhb;
        DataMapPoint dmPoint;
        DistanceDetermination dd;
        AuAuthCheck auCheck;

        // 近くの駅・IC検索用変数
        int icCount;
        int stCount;
        String[] icDistance;
        String[] stDistance;
        DataMapPoint[] dmpIc;
        DataMapPoint[] dmpStation;
        SearchHotelMapPoint shm;

        url = "";
        routePoint = "";
        ret = false;
        scale = 0;
        distance = 0;
        cu = new CreateUrl();
        dhb = new DataHotelBasic();
        dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
        carrierFlag = UserAgent.getUserAgentType( request );

        icCount = 0;
        stCount = 0;
        icDistance = null;
        stDistance = null;
        dmpIc = null;
        dmpStation = null;
        shm = new SearchHotelMapPoint();

        try
        {
            // 昭文社の地図コードをパラメータで受けた場合はゼンリンのコードに変換しリダイレクト（#15040対応）
            QueryStrChecker qsc = new QueryStrChecker();
            int redirection_flag = 0; // 1：リダイレクト、0：スルー、-1：エラー（トップページへリダイレクト）
            String query_str = request.getQueryString();
            if ( query_str == null )
            {
                query_str = "";
            }
            query_str = query_str.replaceAll( "&amp;", "&" ); // "&"が置換されている場合があるので元に戻す（リダイレクトするため）
            redirection_flag = qsc.checkIncludingShobunshaCode( query_str );
            if ( redirection_flag == 1 )
            {
                response.sendRedirect( request.getRequestURI() + "?" + qsc.getConvertedQueryStr() );
                return;
            }
            else if ( redirection_flag == -1 )
            {
                response.sendRedirect( Url.getUrl() );
                return;
            }

            paramUidLink = (String)request.getAttribute( "UID-LINK" );

            // ユーザー情報の取得
            if ( dataLoginInfo_M2 != null )
            {

                memberFlag = dataLoginInfo_M2.isMemberFlag();
                paymemberFlag = dataLoginInfo_M2.isPaymemberFlag();
                paymemberTempFlag = dataLoginInfo_M2.isPaymemberTempFlag();
                registStatus = dataLoginInfo_M2.getRegistStatus();
                delFlag = dataLoginInfo_M2.getDelFlag();
                carrierFlag = dataLoginInfo_M2.getCarrierFlag();
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
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionRouteMap dataLoginInfo] Exception:" + e.toString() );
        }

        paramId = request.getParameter( "hotel_id" );
        paramPos = request.getParameter( "pos" );
        paramLat = request.getParameter( "lat" );
        paramLon = request.getParameter( "lon" );
        paramMapId = request.getParameter( "map_id" );
        paramKind = request.getParameter( "kind" );
        paramGps = request.getParameter( "gps" );
        paramPoint = request.getParameter( "point" );
        strPosIcon = request.getParameter( "icon" );
        paramDx = request.getParameter( "dx" );
        paramDy = request.getParameter( "dy" );
        paramScale = request.getParameter( "scale" );
        paramDscale = request.getParameter( "dscale" );

        Logging.info( paramId );
        if ( (paramId == null) || (paramId.compareTo( "" ) == 0) || (CheckString.numCheck( paramId ) == false) )
        {
            paramId = "0";
        }
        if ( (paramPos == null) || (paramPos.compareTo( "" ) == 0) )
        {
            paramPos = "";
        }
        if ( (paramLat == null) || (paramLat.compareTo( "" ) == 0) )
        {
            paramLat = "";
        }
        if ( (paramLon == null) || (paramLon.compareTo( "" ) == 0) )
        {
            paramLon = "";
        }
        if ( paramMapId == null )
        {
            paramMapId = "";
        }
        if ( (paramKind == null) || (paramKind.compareTo( "" ) == 0) || CheckString.numCheck( paramKind ) == false )
        {
            paramKind = "0";
        }
        if ( (paramGps == null) || (paramGps.compareTo( "true" ) != 0) )
        {
            paramGps = "false";
        }
        if ( paramPoint == null )
        {
            paramPoint = "";
        }
        if ( strPosIcon == null )
        {
            strPosIcon = "";
        }

        // ホテルIDがあるかどうかでアクションを振り分ける
        // (各機種で位置情報取得後)
        if ( Integer.parseInt( paramId ) == 0 )
        {
            // 位置情報を取得するデータがある場合（GPSの結果が返ってきた場合）
            if ( (paramLat.compareTo( "" ) != 0 && paramLon.compareTo( "" ) != 0) || paramPos.compareTo( "" ) != 0 )
            {
                dhb = this.getUserRouteQuery();
                // ソフトバンクはposから位置情報を分割する
                if ( carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK )
                {
                    if ( paramPos.compareTo( "" ) != 0 )
                    {
                        int i = paramPos.indexOf( "E" );
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
                        response.sendRedirect( "/index.jsp?" + paramUidLink );
                    }
                    catch ( Exception e )
                    {
                        Logging.error( "[ActionRouteMap sendRedirect] Exception" + e.toString() );
                    }
                    return;
                }

                // GPSの座標をいろいろな測地系に変換し、地図表示へ
                cg = new ConvertGeodesic();
                cg.convertDms2Degree( paramLat, paramLon );
                paramLat = Double.toString( cg.getLatWGS() );
                paramLon = Double.toString( cg.getLonWGS() );

                if ( dhb != null )
                {
                    // ホテル周辺の駅を探す
                    ret = shm.getSearchHotelNearStation( Integer.toString( dhb.getId() ) );
                    if ( ret != false )
                    {
                        stCount = shm.getStationCount();
                        dmpStation = shm.getStation();
                        stDistance = shm.getStationDistance();
                    }
                    // ホテル周辺のICを探す
                    ret = shm.getSearchHotelNearIc( Integer.toString( dhb.getId() ) );
                    if ( ret != false )
                    {
                        icCount = shm.getIcCount();
                        dmpIc = shm.getIc();
                        icDistance = shm.getIcDistance();
                    }

                    dd = new DistanceDetermination();
                    distance = dd.getDistance( cg.getLatTOKYONum(), cg.getLonTOKYONum(), dhb.getHotelLatNum(),
                            dhb.getHotelLonNum() );

                    routePoint = "WGS84," + paramLat + "," + paramLon + ",WGS84," + dhb.getHotelLat() + ","
                            + dhb.getHotelLon();
                    strPosIcon = GPS + ":PWGS84," + paramLat + "," + paramLon +
                            ":" + HOTEL + ":PWGS84," + dhb.getHotelLat() + "," + dhb.getHotelLon();
                }

                // test
                cu.setPoint( paramLat + "," + paramLon + "," + dhb.getHotelLat() + "," + dhb.getHotelLon() );
                url = cu.getYolpRouteMap();
                request.setAttribute( "url", url );
                // test

                try
                {
                    request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                    request.setAttribute( "kind", "3" );
                    request.setAttribute( "point", routePoint );
                    request.setAttribute( "pos", strPosIcon );

                    if ( dhb != null )
                    {
                        request.setAttribute( "dhb", dhb );
                        request.setAttribute( "hotel_id", Integer.toString( dhb.getId() ) );
                        request.setAttribute( "countIc", icCount );
                        request.setAttribute( "countSt", stCount );
                        request.setAttribute( "ic", dmpIc );
                        request.setAttribute( "st", dmpStation );
                        request.setAttribute( "distanceIc", icDistance );
                        request.setAttribute( "distanceSt", stDistance );
                    }

                    if ( distance > 0 && distance <= 5000 )
                    {
                        requestDispatcher = request.getRequestDispatcher( "hotel_route.jsp" );
                    }
                    else
                    {
                        requestDispatcher = request.getRequestDispatcher( "hotel_route_error.jsp" );
                    }
                    requestDispatcher.forward( request, response );
                }
                catch ( Exception e )
                {
                    Logging.info( "[ActionRouteMap dispMap] Exception:" + e.toString() );
                }

            }
            else
            {
                // エラーページへ飛ばす
                try
                {
                    dhb = this.getUserRouteQuery();
                    if ( dhb != null )
                    {
                        // ホテル周辺の駅を探す
                        ret = shm.getSearchHotelNearStation( Integer.toString( dhb.getId() ) );
                        if ( ret != false )
                        {
                            stCount = shm.getStationCount();
                            dmpStation = shm.getStation();
                            stDistance = shm.getStationDistance();
                        }
                        // ホテル周辺のICを探す
                        ret = shm.getSearchHotelNearIc( Integer.toString( dhb.getId() ) );
                        if ( ret != false )
                        {
                            icCount = shm.getIcCount();
                            dmpIc = shm.getIc();
                            icDistance = shm.getIcDistance();
                        }
                        request.setAttribute( "dhb", dhb );
                        request.setAttribute( "hotel_id", Integer.toString( dhb.getId() ) );
                        request.setAttribute( "countIc", icCount );
                        request.setAttribute( "countSt", stCount );
                        request.setAttribute( "ic", dmpIc );
                        request.setAttribute( "st", dmpStation );
                        request.setAttribute( "distanceIc", icDistance );
                        request.setAttribute( "distanceSt", stDistance );
                    }
                    requestDispatcher = request.getRequestDispatcher( "hotel_route_error.jsp" );
                    requestDispatcher.forward( request, response );
                }
                catch ( Exception e )
                {
                    Logging.error( "[ActionRouteMap sendRedirect] Exception:" + e.toString() );
                }
                return;
            }

        }
        else
        {
            ret = dhb.getData( Integer.parseInt( paramId ) );
            if ( ret != false )
            {
                // ホテル周辺の駅を探す
                ret = shm.getSearchHotelNearStation( paramId );
                if ( ret != false )
                {
                    stCount = shm.getStationCount();
                    dmpStation = shm.getStation();
                    stDistance = shm.getStationDistance();
                }
                // ホテル周辺のICを探す
                ret = shm.getSearchHotelNearIc( paramId );
                if ( ret != false )
                {
                    icCount = shm.getIcCount();
                    dmpIc = shm.getIc();
                    icDistance = shm.getIcDistance();
                }

                // GPSフラグがあったらGPS取得の処理を行う。
                if ( paramGps.compareTo( "true" ) == 0 )
                {
                    // GPS取得処理へリダイレクトさせるロジックへ渡す。
                    ret = this.setUserRouteQuery( Integer.parseInt( paramId ) );
                    if ( ret != false )
                    {
                        try
                        {
                            if ( request.getAttribute( "UID-LINK" ) != null )
                            {
                                response.sendRedirect( "hotel_route_gps.jsp?hotel_id=" + paramId + "&"
                                        + (String)request.getAttribute( "UID-LINK" ) );
                            }
                        }
                        catch ( Exception e )
                        {
                            Logging.info( "[ActionRoute requestDipatcher] Exception:" + e.toString() );
                        }
                        return;
                    }
                    else
                    {
                        try
                        {
                            response.sendRedirect( "hotel_route_error.jsp?" + (String)request.getAttribute( "UID-LINK" ) );
                        }
                        catch ( Exception e )
                        {
                            Logging.info( "[ActionRoute requestDipatcher] Exception:" + e.toString() );
                        }
                        return;
                    }
                }
                // 駅又はICを選択した場合マップIDからホテルまでの地図を表示する
                else if ( paramMapId.compareTo( "" ) != 0 )
                {
                    boolean boolMap = false;
                    DataHotelMap dhm;
                    dhm = new DataHotelMap();
                    dmPoint = new DataMapPoint();
                    dhm.getData( Integer.parseInt( paramId ), paramMapId );

                    // 最寄の地図IDがあればそちらを取得する
                    if ( dhm.getNearestId().equals( "" ) == false )
                    {
                        boolMap = dmPoint.getDataEx( dhm.getNearestId() );
                    }
                    else
                    {
                        boolMap = dmPoint.getData( paramMapId );
                    }
                    if ( boolMap != false && dmPoint.getId().equals( "" ) == false
                            && dhb.getData( Integer.parseInt( paramId ) ) != false )
                    {
                        cg = new ConvertGeodesic();
                        cg.TokyoNum2Tokyo( dmPoint.getLat(), dmPoint.getLon() );
                        cg.Tokyo2Wgs( cg.getLatTOKYO(), cg.getLonTOKYO() );
                        paramLat = Double.toString( cg.getLatWGS() );
                        paramLon = Double.toString( cg.getLonWGS() );

                        routePoint = "WGS84," + paramLat + "," + paramLon + ",WGS84," + dhb.getHotelLat() + ","
                                + dhb.getHotelLon();
                        // 表示するアイコンの指定
                        if ( Integer.parseInt( paramKind ) == 1 )
                        {
                            strPosIcon = STATION + ":PWGS84," + paramLat + "," + paramLon +
                                    ":" + HOTEL + ":PWGS84," + dhb.getHotelLat() + "," + dhb.getHotelLon();
                        }
                        else if ( Integer.parseInt( paramKind ) == 2 )
                        {
                            strPosIcon = IC + ":PWGS84," + paramLat + "," + paramLon +
                                    ":" + HOTEL + ":PWGS84," + dhb.getHotelLat() + "," + dhb.getHotelLon();
                        }
                        else
                        {
                            strPosIcon = GPS + ":PWGS84," + paramLat + "," + paramLon +
                                    ":" + HOTEL + ":PWGS84," + dhb.getHotelLat() + "," + dhb.getHotelLon();
                        }

                        // ///////////test

                        cu.setPoint( paramLat + "," + paramLon + "," + dhb.getHotelLat() + "," + dhb.getHotelLon() );

                        // キャリアによって出す画像タイプを変更
                        if ( (carrierFlag == DataMasterUseragent.CARRIER_AU)
                                || (carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK) )
                        {
                            cu.setImgType( "PNG" );
                            Logging.info( "[ActionRouteMap]:PNGセット" );
                        }
                        else if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
                        {
                            cu.setImgType( "GIF" );
                            Logging.info( "[ActionRouteMap]:GIFセット" );
                        }

                        // 地図表示用URLの作成
                        url = cu.getYolpRouteMap();

                        // /////////test

                        try
                        {
                            request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                            request.setAttribute( "kind", paramKind );
                            request.setAttribute( "point", routePoint );
                            request.setAttribute( "pos", strPosIcon );

                            request.setAttribute( "hotel_id", paramId );
                            request.setAttribute( "dhb", dhb );

                            request.setAttribute( "countIc", icCount );
                            request.setAttribute( "countSt", stCount );
                            request.setAttribute( "ic", dmpIc );
                            request.setAttribute( "st", dmpStation );
                            request.setAttribute( "distanceIc", icDistance );
                            request.setAttribute( "distanceSt", stDistance );

                            request.setAttribute( "url", url );

                            requestDispatcher = request.getRequestDispatcher( "hotel_route.jsp" );
                            requestDispatcher.forward( request, response );
                        }
                        catch ( Exception e )
                        {
                            Logging.info( "[ActionRouteMap dispMap] Exception:" + e.toString() );
                        }

                    }
                }

                // strPos,paramPointが取得できたら地図移動
                if ( strPosIcon.compareTo( "" ) != 0 && paramPoint.compareTo( "" ) != 0 )
                {
                    try
                    {
                        request.setAttribute( "pos", strPosIcon );
                        request.setAttribute( "point", paramPoint );
                        request.setAttribute( "kind", paramKind );
                        request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                        request.setAttribute( "hotel_id", paramId );
                        request.setAttribute( "dhb", dhb );
                        request.setAttribute( "countIc", icCount );
                        request.setAttribute( "countSt", stCount );
                        request.setAttribute( "ic", dmpIc );
                        request.setAttribute( "st", dmpStation );
                        request.setAttribute( "distanceIc", icDistance );
                        request.setAttribute( "distanceSt", stDistance );

                        request.setAttribute( "scale", paramScale );
                        request.setAttribute( "dx", paramDx );
                        request.setAttribute( "dy", paramDy );
                        request.setAttribute( "dscale", paramDscale );

                        url = cu.getYolpRouteMap();
                        request.setAttribute( "url", url );

                        requestDispatcher = request.getRequestDispatcher( "hotel_route.jsp" );
                        requestDispatcher.forward( request, response );
                    }
                    catch ( Exception e )
                    {
                        Logging.error( "[ActionRouteMap requestDispatcher] Exception:" + e.toString() );
                    }
                }
                // 取得したパラメータがhotelIdのみだった場合、スタート地点を選択させるページを表示する
                if ( paramPos.compareTo( "" ) == 0 && paramLat.compareTo( "" ) == 0 && paramLon.compareTo( "" ) == 0 &&
                        strPosIcon.compareTo( "" ) == 0 && paramPoint.compareTo( "" ) == 0 )
                {

                    try
                    {
                        request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                        request.setAttribute( "hotel_id", paramId );
                        request.setAttribute( "dhb", dhb );
                        request.setAttribute( "countIc", icCount );
                        request.setAttribute( "countSt", stCount );
                        request.setAttribute( "ic", dmpIc );
                        request.setAttribute( "st", dmpStation );
                        request.setAttribute( "distanceIc", icDistance );
                        request.setAttribute( "distanceSt", stDistance );
                        requestDispatcher = request.getRequestDispatcher( "hotel_route_list.jsp" );
                        requestDispatcher.forward( request, response );
                    }
                    catch ( Exception e )
                    {
                        Logging.error( "[ActionRouteMap requestDispatcher] Exception:" + e.toString() );
                    }
                }
            }
            else
            {
                try
                {
                    response.sendRedirect( "../index.jsp?" + paramUidLink );
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionRouteMap sendRedirect] Exception:" + e.toString() );
                }
                return;
            }

        }

    }

    /**
     * ホテルIDをDBに保存する
     * 
     * @param hotelId ホテルID
     * 
     */
    private boolean setUserRouteQuery(int hotelId)
    {
        boolean ret;
        DataUserRouteQuery durq;

        ret = false;
        try
        {
            if ( this.dataLoginInfo_M2 != null )
            {
                durq = new DataUserRouteQuery();
                ret = durq.getData( this.dataLoginInfo_M2.getUserId() );
                if ( durq.getUserId().equals( "" ) == false )
                {
                    durq.setUserId( this.dataLoginInfo_M2.getUserId() );
                    durq.setQueryString( Integer.toString( hotelId ) );
                    durq.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    durq.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    ret = durq.updateData( this.dataLoginInfo_M2.getUserId() );
                }
                else
                {
                    durq.setUserId( this.dataLoginInfo_M2.getUserId() );
                    durq.setQueryString( Integer.toString( hotelId ) );
                    durq.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    durq.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    ret = durq.insertData();
                }
                durq = null;
            }
            else if ( this.termNo.compareTo( "" ) != -1 )
            {
                durq = new DataUserRouteQuery();
                ret = durq.getData( this.termNo );
                if ( durq.getUserId().equals( "" ) == false )
                {
                    durq.setUserId( this.termNo );
                    durq.setQueryString( Integer.toString( hotelId ) );
                    durq.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    durq.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    ret = durq.updateData( this.termNo );
                }
                else
                {
                    durq.setUserId( this.termNo );
                    durq.setQueryString( Integer.toString( hotelId ) );
                    durq.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    durq.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    ret = durq.insertData();
                }
                durq = null;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[ActionRouteMap.setUserDetailQuery] Exception:" + e.toString() );
            ret = false;
        }
        return(ret);
    }

    /**
     * ホテルIDをDBに保存する
     * 
     * @return DataHotelBasicをセット
     */
    private DataHotelBasic getUserRouteQuery()
    {
        boolean ret;
        DataUserRouteQuery durq;
        int seq;
        DataHotelBasic dhbBasic;

        ret = false;
        seq = 0;
        dhbBasic = null;
        try
        {
            durq = new DataUserRouteQuery();
            if ( this.dataLoginInfo_M2 != null )
            {
                ret = durq.getData( this.dataLoginInfo_M2.getUserId() );
            }
            else if ( this.termNo.compareTo( "" ) != -1 )
            {
                ret = durq.getData( this.termNo );
            }
            if ( ret != false )
            {
                dhbBasic = new DataHotelBasic();
                dhbBasic.getData( Integer.parseInt( durq.getQueryString() ) );
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[ActionRouteMap.getUserDetailQuery] Exception:" + e.toString() );
            ret = false;
        }
        return(dhbBasic);
    }

}
