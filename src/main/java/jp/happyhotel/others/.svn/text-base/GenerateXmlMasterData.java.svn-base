package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * マスタ情報XML作成クラス
 * 
 * @author tashiro-s1
 * @version 1.0 2011/04/11
 * 
 */
public class GenerateXmlMasterData extends WebApiResultBase
{
    // タグ名
    private static final String                            TAG_MASTER_VERSION         = "version";
    private static final String                            TAG_MASTER_ALLCOUNT        = "allCount";
    private static final String                            TAG_MASTER_LOCAL_COUNT     = "localCount";
    private static final String                            TAG_MASTER_LOCAL           = "local";
    private static final String                            TAG_MASTER_PREF_COUNT      = "prefCount";
    private static final String                            TAG_MASTER_PREF            = "pref";
    private static final String                            TAG_MASTER_CITY_COUNT      = "cityCount";
    private static final String                            TAG_MASTER_CITY            = "city";
    private static final String                            TAG_MASTER_ROUTEST_COUNT   = "routeStCount";
    private static final String                            TAG_MASTER_ROUTEST         = "routeSt";
    private static final String                            TAG_MASTER_ST_COUNT        = "stCount";
    private static final String                            TAG_MASTER_ST              = "st";
    private static final String                            TAG_MASTER_ROUTEIC_COUNT   = "routeIcCount";
    private static final String                            TAG_MASTER_ROUTEIC         = "routeIc";
    private static final String                            TAG_MASTER_IC_COUNT        = "icCount";
    private static final String                            TAG_MASTER_IC              = "ic";
    private static final String                            TAG_MASTER_HOTELAREA_COUNT = "hotelAreaCount";
    private static final String                            TAG_MASTER_HOTELAREA       = "hotelArea";
    // テスト用
    private static final String                            TAG_MASTER_ST_Route        = "stRoute";
    private static final String                            TAG_MASTER_ST_DETAIL       = "stDetail";

    private static final String                            TAG_MASTER_IC2_COUNT       = "ic2Count";
    private static final String                            TAG_MASTER_IC2             = "ic2";

    private XmlTag                                         version;
    private XmlTag                                         allCount;                                                                         // マスター情報全件数
    private XmlTag                                         localCount;                                                                       // 地方件数
    private XmlTag                                         prefCount;                                                                        // 都道府県件数
    private XmlTag                                         cityCount;                                                                        // 市区町村件数
    private XmlTag                                         routeStCount;                                                                     // ルート駅件数
    private XmlTag                                         stCount;                                                                          // 駅件数
    private XmlTag                                         routeIcCount;                                                                     // ルートIC件数
    private XmlTag                                         icCount;                                                                          // IC件数
    private XmlTag                                         hotelAreaCount;                                                                   // ホテルエリア件数
    private ArrayList<GenerateXmlMasterLocal>              localList                  = new ArrayList<GenerateXmlMasterLocal>();             // 地方マスタ
    private ArrayList<GenerateXmlMasterPref>               prefList                   = new ArrayList<GenerateXmlMasterPref>();              // 都道府県マスタ
    private ArrayList<GenerateXmlMasterCity>               cityList                   = new ArrayList<GenerateXmlMasterCity>();              // 市区町村マスタ
    private ArrayList<GenerateXmlMasterRouteStation>       routeStList                = new ArrayList<GenerateXmlMasterRouteStation>();      // 駅路線マスタ
    private ArrayList<GenerateXmlMasterStation>            stList                     = new ArrayList<GenerateXmlMasterStation>();           // 駅マスタ
    private ArrayList<GenerateXmlMasterRouteIc>            routeIcList                = new ArrayList<GenerateXmlMasterRouteIc>();           // IC路線マスタ
    private ArrayList<GenerateXmlMasterIc>                 icList                     = new ArrayList<GenerateXmlMasterIc>();                // ICマスタ
    private ArrayList<GenerateXmlMasterHotelArea>          hotelAreaList              = new ArrayList<GenerateXmlMasterHotelArea>();         // ホテルエリアマスタ

    // テスト用
    private ArrayList<GenerateXmlMasterStationRoute>       stRouteList                = new ArrayList<GenerateXmlMasterStationRoute>();
    private ArrayList<GenerateXmlMasterStationRouteDetail> stDetailList               = new ArrayList<GenerateXmlMasterStationRouteDetail>();

    private XmlTag                                         ic2Count;                                                                         // IC件数
    private ArrayList<GenerateXmlMasterIc2>                ic2List                    = new ArrayList<GenerateXmlMasterIc2>();               // ICマスタ

    @Override
    protected void initXmlNodeInfo()
    {
        int i = 0;
        XmlTag.setParent( root, version );
        XmlTag.setParent( root, allCount );
        XmlTag.setParent( root, localCount );
        // 地方マスタがnull以外であれば追加
        if ( localList != null )
        {
            for( i = 0 ; i < localList.size() ; i++ )
            {
                localList.get( i ).setRootNode( root );
                localList.get( i ).initXmlNodeInfo();
            }
        }

        XmlTag.setParent( root, prefCount );
        // 都道府県マスタがnull以外であれば追加
        if ( prefList != null )
        {
            for( i = 0 ; i < prefList.size() ; i++ )
            {
                prefList.get( i ).setRootNode( root );
                prefList.get( i ).initXmlNodeInfo();
            }
        }

        XmlTag.setParent( root, cityCount );
        // 市区町村マスタがnull以外であれば追加
        if ( cityList != null )
        {
            for( i = 0 ; i < cityList.size() ; i++ )
            {
                cityList.get( i ).setRootNode( root );
                cityList.get( i ).initXmlNodeInfo();
            }
        }

        XmlTag.setParent( root, routeStCount );
        // 駅路線マスタがnull以外であれば追加
        if ( routeStList != null )
        {
            for( i = 0 ; i < routeStList.size() ; i++ )
            {
                routeStList.get( i ).setRootNode( root );
                routeStList.get( i ).initXmlNodeInfo();
            }
        }

        XmlTag.setParent( root, stCount );
        // 駅マスタがnull以外であれば追加
        if ( stList != null )
        {
            for( i = 0 ; i < stList.size() ; i++ )
            {
                stList.get( i ).setRootNode( root );
                stList.get( i ).initXmlNodeInfo();
            }
        }

        XmlTag.setParent( root, routeIcCount );
        // IC路線マスタがnull以外であれば追加
        if ( routeIcList != null )
        {
            for( i = 0 ; i < routeIcList.size() ; i++ )
            {
                routeIcList.get( i ).setRootNode( root );
                routeIcList.get( i ).initXmlNodeInfo();
            }
        }

        XmlTag.setParent( root, icCount );
        // ICマスタがnull以外であれば追加
        if ( icList != null )
        {
            for( i = 0 ; i < icList.size() ; i++ )
            {
                icList.get( i ).setRootNode( root );
                icList.get( i ).initXmlNodeInfo();
            }
        }

        XmlTag.setParent( root, hotelAreaCount );
        // ホテルエリアマスタがnull以外であれば追加
        if ( hotelAreaList != null )
        {
            for( i = 0 ; i < hotelAreaList.size() ; i++ )
            {
                hotelAreaList.get( i ).setRootNode( root );
                hotelAreaList.get( i ).initXmlNodeInfo();
            }
        }

        XmlTag.setParent( root, ic2Count );
        // ICマスタがnull以外であれば追加
        if ( ic2List != null )
        {
            for( i = 0 ; i < ic2List.size() ; i++ )
            {
                ic2List.get( i ).setRootNode( root );
                ic2List.get( i ).initXmlNodeInfo();
            }
        }

        return;
    }

    //
    public void setVersion(String ver)
    {
        version = XmlTag.createXmlTag( TAG_MASTER_ALLCOUNT, ver );
        return;
    }

    public void setAllCount(int count)
    {
        allCount = XmlTag.createXmlTag( TAG_MASTER_ALLCOUNT, count );
        return;
    }

    public void setLocalCount(int count)
    {
        localCount = XmlTag.createXmlTag( TAG_MASTER_LOCAL_COUNT, count );
        return;
    }

    public void setLocal(GenerateXmlMasterLocal local)
    {
        this.localList.add( local );
        return;
    }

    public void setPrefCount(int count)
    {
        prefCount = XmlTag.createXmlTag( TAG_MASTER_PREF_COUNT, count );
        return;
    }

    public void setPref(GenerateXmlMasterPref pref)
    {
        this.prefList.add( pref );
        return;
    }

    public void setCityCount(int count)
    {
        cityCount = XmlTag.createXmlTag( TAG_MASTER_CITY_COUNT, count );
        return;
    }

    public void setCity(GenerateXmlMasterCity city)
    {
        this.cityList.add( city );
        return;
    }

    public void setRouteStCount(int count)
    {
        routeStCount = XmlTag.createXmlTag( TAG_MASTER_ROUTEST_COUNT, count );
        return;
    }

    public void setRouteSt(GenerateXmlMasterRouteStation routeSt)
    {
        this.routeStList.add( routeSt );
        return;
    }

    public void setStCount(int count)
    {
        stCount = XmlTag.createXmlTag( TAG_MASTER_ST_COUNT, count );
        return;
    }

    public void setSt(GenerateXmlMasterStation st)
    {
        this.stList.add( st );
        return;
    }

    // test
    public void setStDetail(GenerateXmlMasterStationRouteDetail stDetail)
    {
        this.stDetailList.add( stDetail );
    }

    // test
    public void setStRoute(GenerateXmlMasterStationRoute stRoute)
    {
        this.stRouteList.add( stRoute );
    }

    public void setRouteIcCount(int count)
    {
        routeIcCount = XmlTag.createXmlTag( TAG_MASTER_ROUTEIC_COUNT, count );
        return;
    }

    public void setRouteIc(GenerateXmlMasterRouteIc routeIc)
    {
        this.routeIcList.add( routeIc );
        return;
    }

    public void setIcCount(int count)
    {
        icCount = XmlTag.createXmlTag( TAG_MASTER_IC_COUNT, count );
        return;
    }

    public void setIc(GenerateXmlMasterIc ic)
    {
        this.icList.add( ic );
        return;
    }

    public void setHotelAreaCount(int count)
    {
        hotelAreaCount = XmlTag.createXmlTag( TAG_MASTER_HOTELAREA_COUNT, count );
        return;
    }

    public void setHotelArea(GenerateXmlMasterHotelArea hotelArea)
    {
        this.hotelAreaList.add( hotelArea );
        return;
    }

    public void setIc2Count(int count)
    {
        ic2Count = XmlTag.createXmlTag( TAG_MASTER_IC2_COUNT, count );
        return;
    }

    public void setIc2(GenerateXmlMasterIc2 ic)
    {
        this.ic2List.add( ic );
        return;
    }

}
