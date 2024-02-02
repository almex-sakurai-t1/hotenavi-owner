package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMapPoint;
import jp.happyhotel.data.DataMapRoute;
import jp.happyhotel.data.DataMasterArea;
import jp.happyhotel.data.DataMasterCity;
import jp.happyhotel.data.DataMasterLocal;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.others.GenerateXmlMasterCity;
import jp.happyhotel.others.GenerateXmlMasterData;
import jp.happyhotel.others.GenerateXmlMasterHotelArea;
import jp.happyhotel.others.GenerateXmlMasterIc;
import jp.happyhotel.others.GenerateXmlMasterIc2;
import jp.happyhotel.others.GenerateXmlMasterIc2Route;
import jp.happyhotel.others.GenerateXmlMasterIcRoute;
import jp.happyhotel.others.GenerateXmlMasterIcRouteDetail;
import jp.happyhotel.others.GenerateXmlMasterLocal;
import jp.happyhotel.others.GenerateXmlMasterPref;
import jp.happyhotel.others.GenerateXmlMasterRouteIc;
import jp.happyhotel.others.GenerateXmlMasterRouteStation;
import jp.happyhotel.others.GenerateXmlMasterStation;
import jp.happyhotel.others.GenerateXmlMasterStationRoute;
import jp.happyhotel.others.GenerateXmlMasterStationRouteDetail;
import jp.happyhotel.search.SearchMasterData;

/**
 * マスターデータ送付クラス
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/05
 */

public class ActionApiMasterDataDeliver extends BaseAction
{

    final private static int DISP_MAX_NUMBER = 20;

    /**
     * 
     * 
     * @param request クライアントからサーバへのリクエスト
     * @param response サーバからクライアントへのレスポンス
     * @see "/キャリアのフォルダ/search/hotelmap_M2.jsp うまくいった場合に遷移する"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int count = 0;
        int nStCount = 0;
        int nIcCount = 0;
        String localId;
        String prefId;
        ArrayList<DataMasterLocal> dmLocal = null;
        ArrayList<DataMasterPref> dmPref = null;
        ArrayList<DataMasterCity> dmCity = null;
        ArrayList<ArrayList<DataMapRoute>> dmStRoute = null;
        ArrayList<DataMapPoint> dmStation = null;
        ArrayList<ArrayList<DataMapRoute>> dmIcRoute = null;
        ArrayList<DataMapPoint> dmIc = null;
        ArrayList<ArrayList<DataMasterArea>> dmArea = null;
        ArrayList<Integer> stPref = null;
        ArrayList<Integer> icPref = null;
        ArrayList<Integer> icLocal = null;
        SearchMasterData smd;

        smd = new SearchMasterData();

        // 地方IDのデータチェック
        localId = request.getParameter( "local_id" );
        if ( (localId == null) || (localId.equals( "" ) != false) || (CheckString.numCheck( localId ) == false) )
        {
            localId = "0";
        }
        // 都道府県IDのデータチェック
        prefId = request.getParameter( "pref_id" );
        if ( (prefId == null) || (prefId.equals( "" ) != false) || (CheckString.numCheck( prefId ) == false) )
        {
            prefId = "0";
        }

        try
        {
            // // 地方マスタを取得
            smd.getMasterLocal( Integer.parseInt( localId ) );
            if ( smd.getMasterLocalCount() > 0 )
            {
                dmLocal = smd.getMasterLocal();
                count += smd.getMasterLocalCount();
            }

            // 都道府県マスタを取得
            smd.getMasterPref( Integer.parseInt( prefId ) );
            if ( smd.getMasterPrefCount() > 0 )
            {
                dmPref = smd.getMasterPref();
                count += smd.getMasterPrefCount();
            }
            // 市区町村マスタを取得
            smd.getMasterCity( Integer.parseInt( prefId ) );
            if ( smd.getMasterCityCount() > 0 )
            {
                dmCity = smd.getMasterCity();
                count += smd.getMasterCityCount();
            }

            // 路線の取得
            smd.getMasterStationRoute( Integer.parseInt( prefId ) );
            if ( smd.getStRouteCount() > 0 )
            {
                dmStRoute = smd.getStRoute();
                count += smd.getStRouteCount();
            }

            // 駅の件数取得(駅のデータはdmStationRouteの路線IDから取得する)
            nStCount = smd.getMasterStationCount( Integer.parseInt( prefId ) );
            count += nStCount;

            // 自動車道の取得
            smd.getMasterIcRoute( Integer.parseInt( prefId ) );
            if ( smd.getIcRouteCount() > 0 )
            {
                dmIcRoute = smd.getIcRoute();
                count += smd.getIcRouteCount();
            }

            // ICの取得(駅のデータはdmIcRouteの路線IDから取得する)
            nIcCount = smd.getMasterIcCount( Integer.parseInt( prefId ) );
            count += nIcCount;

            // ホテルエリアの取得(ArrayListを使用)
            smd.getMasterHotelArea( Integer.parseInt( prefId ) );
            if ( smd.getMasterAreaCount() > 0 )
            {
                dmArea = smd.getMasterArea();
                count += smd.getMasterAreaCount();
            }

            // XML出力クラスを呼びだし値をセットしていく
            // 検索結果作成
            GenerateXmlMasterData master = new GenerateXmlMasterData();
            master.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            master.setAllCount( count );
            master.setLocalCount( smd.getMasterLocalCount() );

            // 地方の情報をセット
            for( int i = 0 ; i < smd.getMasterLocalCount() ; i++ )
            {
                GenerateXmlMasterLocal local = new GenerateXmlMasterLocal();
                local.addLocalInfo( dmLocal.get( i ) );
                master.setLocal( local );
            }

            // // 都道府県の件数
            master.setPrefCount( smd.getMasterPrefCount() );
            // 都道府県の情報をセット
            for( int i = 0 ; i < smd.getMasterPrefCount() ; i++ )
            {
                GenerateXmlMasterPref pref = new GenerateXmlMasterPref();
                pref.addPref( dmPref.get( i ) );
                master.setPref( pref );
            }

            // 市区町村の件数
            master.setCityCount( smd.getMasterCityCount() );
            // 市区町村の情報をセット
            for( int i = 0 ; i < smd.getMasterCityCount() ; i++ )
            {
                GenerateXmlMasterCity city = new GenerateXmlMasterCity();
                city.addCity( dmCity.get( i ), 0 );
                master.setCity( city );
            }

            // // 路線の件数をセット
            master.setRouteStCount( smd.getStRouteCount() );
            dmStRoute = smd.getStRoute();
            stPref = smd.getStPrefList();
            // 路線の情報をセット
            for( int i = 0 ; i < dmStRoute.size() ; i++ )
            {
                GenerateXmlMasterRouteStation routeSt = new GenerateXmlMasterRouteStation();
                routeSt.addRouteStationInfo( dmStRoute.get( i ), stPref.get( i ) );
                master.setRouteSt( routeSt );
            }

            // 駅の件数をセット
            master.setStCount( nStCount );
            if ( dmStRoute != null )
            {
                // 駅の情報をセット
                for( int i = 0 ; i < dmStRoute.size() ; i++ )
                {
                    GenerateXmlMasterStation st = new GenerateXmlMasterStation();
                    st.setPrefId( stPref.get( i ) );
                    for( int j = 0 ; j < dmStRoute.get( i ).size() ; j++ )
                    {
                        GenerateXmlMasterStationRoute stRoute = new GenerateXmlMasterStationRoute();
                        stRoute.setRouteId( dmStRoute.get( i ).get( j ).getRouteId() );

                        // 路線IDに紐づく
                        boolean ret = false;
                        ret = smd.getMasterStation( dmStRoute.get( i ).get( j ).getRouteId(), stPref.get( i ) );
                        if ( ret != false )
                        {
                            for( int k = 0 ; k < smd.getStCount() ; k++ )
                            {
                                GenerateXmlMasterStationRouteDetail stRouteDetail = new GenerateXmlMasterStationRouteDetail();
                                stRouteDetail.setId( smd.getSt().get( k ).getOption4() );
                                stRouteDetail.setName( smd.getSt().get( k ).getName() );
                                stRoute.setDetail( stRouteDetail );
                            }
                        }
                        st.setRoute( stRoute );
                    }
                    master.setSt( st );
                }
            }

            // 高速道路の件数をセット
            master.setRouteIcCount( smd.getIcRouteCount() );
            icPref = smd.getIcPrefList();

            // 高速道路の情報をセット
            for( int i = 0 ; i < smd.getIcRoute().size() ; i++ )
            {
                GenerateXmlMasterRouteIc routeIc = new GenerateXmlMasterRouteIc();
                routeIc.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                routeIc.addRouteIcInfo( dmIcRoute.get( i ), icPref.get( i ) );
                master.setRouteIc( routeIc );
            }

            // ICの件数をセット
            master.setIcCount( nIcCount );
            if ( dmIcRoute != null )
            {
                for( int i = 0 ; i < dmIcRoute.size() ; i++ )
                {
                    GenerateXmlMasterIc ic = new GenerateXmlMasterIc();
                    ic.setPrefId( icPref.get( i ) );
                    for( int j = 0 ; j < dmIcRoute.get( i ).size() ; j++ )
                    {
                        GenerateXmlMasterIcRoute icRoute = new GenerateXmlMasterIcRoute();
                        icRoute.setRouteId( dmIcRoute.get( i ).get( j ).getRouteId() );

                        // 路線IDに紐づく
                        boolean ret = false;
                        ret = smd.getMasterIc( dmIcRoute.get( i ).get( j ).getRouteId(), icPref.get( i ) );
                        if ( ret != false )
                        {
                            for( int k = 0 ; k < smd.getIcCount() ; k++ )
                            {
                                GenerateXmlMasterIcRouteDetail icRouteDetail = new GenerateXmlMasterIcRouteDetail();
                                icRouteDetail.setId( smd.getIc().get( k ).getOption4() );
                                icRouteDetail.setName( smd.getIc().get( k ).getName() );
                                icRoute.setDetail( icRouteDetail );
                            }
                        }
                        ic.setRoute( icRoute );
                    }
                    master.setIc( ic );
                }
            }

            // ホテルエリアの件数をセットを取得
            master.setHotelAreaCount( smd.getMasterAreaCount() );
            // ホテルエリアの情報をセット
            for( int i = 0 ; i < smd.getMasterArea().size() ; i++ )
            {
                GenerateXmlMasterHotelArea hotelArea = new GenerateXmlMasterHotelArea();
                hotelArea.addHotelAreaInfo( dmArea.get( i ) );
                master.setHotelArea( hotelArea );
            }

            if ( Integer.parseInt( prefId ) > 0 && Integer.parseInt( prefId ) <= 47 && Integer.parseInt( localId ) == 0 )
            {
                smd.getMasterPref();
                if ( smd.getMasterPrefCount() > 0 )
                {
                    localId = Integer.toString( smd.getMasterPref().get( 0 ).getLocalId() );
                }
            }

            // ICの件数をセット
            smd.getMasterIc2Route( Integer.parseInt( localId ) );
            icLocal = smd.getIc2Local();

            nIcCount = 0;
            for( int i = 0 ; i < icLocal.size() ; i++ )
            {
                GenerateXmlMasterIc2 ic2 = new GenerateXmlMasterIc2();
                ic2.setLocalId( icLocal.get( i ) );
                dmIcRoute = smd.getIc2Route();
                for( int j = 0 ; j < dmIcRoute.get( i ).size() ; j++ )
                {
                    boolean ret = false;

                    // 自動車道IDから、対応するICを取得
                    ret = smd.getMasterIc2( dmIcRoute.get( i ).get( j ).getRouteId() );
                    if ( ret != false )
                    {
                        GenerateXmlMasterIc2Route ic2Route = new GenerateXmlMasterIc2Route();
                        ic2Route.setRouteId( dmIcRoute.get( i ).get( j ).getRouteId() );
                        ic2Route.setRouteName( dmIcRoute.get( i ).get( j ).getName() );
                        ic2Route.addRouteIcInfo( smd.getIc2(), smd.getIc2HotelCount() );
                        ic2.setRoute( ic2Route );
                        nIcCount += smd.getIc2Count();
                    }
                }
                master.setIc2( ic2 );
            }
            master.setIc2Count( nIcCount );
            master.setAllCount( count + nIcCount );

            try
            {
                String xmlOut = master.createXml();
                // Logging.info( xmlOut );
                ServletOutputStream out = null;

                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.info( e.toString() );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionApiMasterDataDeliver.execute()] Ecxeption:" + e.toString() );
        }
    }
}
