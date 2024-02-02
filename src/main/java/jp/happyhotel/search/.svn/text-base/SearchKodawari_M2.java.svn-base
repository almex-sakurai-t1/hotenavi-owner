package jp.happyhotel.search;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.data.DataSearchHotel_M2;

public class SearchKodawari_M2

{

    private static final long    serialVersionUID = -5524226612470789020L;
    private int                  m_hotelCount;                            // Number of hotels displayed on any page
    private int                  m_hotelAllCount;                         // Total number of hotels found
    private int[]                m_hotelIdList;                           // List of hotel id's searched
    private DataSearchHotel_M2[] m_hotelInfo;                             // Details of hotels
    private static int           maxRecords       = Constants.maxRecords;
    private String               m_queryString;
    private String[]             sortParams;

    public SearchKodawari_M2()
    {
        m_hotelCount = 0;
        m_hotelAllCount = 0;
    }

    public int getCount()
    {
        return(m_hotelCount);
    }

    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    public int[] getHotelIdList()
    {
        return(m_hotelIdList);
    }

    public DataSearchHotel_M2[] getHotelInfo()
    {
        return(m_hotelInfo);
    }

    public String getQueryString()
    {
        return m_queryString;
    }

    public void setQueryString(String queryString)
    {
        this.m_queryString = queryString;
    }

    public String[] getSortParams()
    {
        return sortParams;
    }

    public void setSortParams(String[] sortParams)
    {
        this.sortParams = sortParams;
    }

    public void searchHotelList(HttpServletRequest request, int pageNum, int pageRecords) throws Exception
    {
        int[] arrHotelIdList; // List of seached hotel Id's
        int count; // calculates the no of hotels to be displayed on curent page
        int loop;
        SearchHotelDao_M2 searchResultDao = null;
        // HotelStatus hs;

        arrHotelIdList = searchHotels( request );
        // this.m_hotelAllCount = hotelBasicList.length;

        if ( m_hotelAllCount > 0 && m_hotelAllCount < maxRecords )
        {
            searchResultDao = new SearchHotelDao_M2(); // Controls Data Interactivity related to Searches

            this.m_hotelInfo = new DataSearchHotel_M2[pageRecords];
            for( loop = 0 ; loop < pageRecords ; loop++ )
            {
                m_hotelInfo[loop] = new DataSearchHotel_M2();
            }

            count = 0;

            for( loop = pageRecords * pageNum ; loop < m_hotelAllCount ; loop++ )
            { // no of hotel to be fetch
                count++;
                if ( count >= pageRecords && pageRecords != 0 )
                    break;

            }

            int hotelIds[] = new int[count];
            int start = pageRecords * pageNum;
            for( int k = 0 ; k < count ; k++ )
            {
                hotelIds[k] = arrHotelIdList[start];

                start++;
            }

            m_hotelInfo = searchResultDao.getDataFromDataBase( hotelIds );

            for( loop = 0 ; loop < count ; loop++ )
            {
                if ( m_hotelInfo[loop].getRank() >= 2 )
                {
                    m_hotelInfo[loop].setStars( PagingDetails.getStarsCount( m_hotelInfo[loop].getPoints(), m_hotelInfo[loop].getBbsAllCount() ) );

                }
            }

            m_hotelCount = count;

            searchResultDao = null;

        }

    }

    public void searchHotelListMobile(HttpServletRequest request, int pageNum, int pageRecords, int prefId) throws Exception
    {
        int[] arrHotelIdList; // List of seached hotel Id's
        int count; // calculates the no of hotels to be displayed on curent page
        int loop;
        SearchHotelDao_M2 searchHotelDao = null;
        // HotelStatus hs;

        arrHotelIdList = searchHotelsMobile( request, prefId );

        if ( m_hotelAllCount > 0 )
        {
            searchHotelDao = new SearchHotelDao_M2(); // Controls Data Interactivity related to Searches

            this.m_hotelInfo = new DataSearchHotel_M2[pageRecords];
            for( loop = 0 ; loop < pageRecords ; loop++ )
            {
                m_hotelInfo[loop] = new DataSearchHotel_M2();
            }

            count = 0;

            for( loop = pageRecords * pageNum ; loop < m_hotelAllCount ; loop++ )
            { // no of hotel to be fetch
                count++;
                if ( count >= pageRecords && pageRecords != 0 )
                    break;
            }

            int hotelIds[] = new int[count];
            int start = pageRecords * pageNum;
            for( int k = 0 ; k < count ; k++ )
            {
                hotelIds[k] = arrHotelIdList[start];

                start++;
            }

            m_hotelInfo = searchHotelDao.getDataFromDataBase( hotelIds );

            for( loop = 0 ; loop < count ; loop++ )
            {
                if ( m_hotelInfo[loop].getRank() >= 2 )
                {
                    m_hotelInfo[loop].setStars( PagingDetails.getStarsCount( m_hotelInfo[loop].getPoints(), m_hotelInfo[loop].getBbsAllCount() ) );
                }
            }

            m_hotelCount = count;

            searchHotelDao = null;

        }

    }

    /**
     * It searches hotel Ids based on kodawari parameters
     * 
     * @param request
     * @return
     * @throws Exception
     */
    public int[] searchHotels(HttpServletRequest request) throws Exception
    {
        int j;
        int k;
        int l;
        int count;
        int findCount;
        int hotelAllCount;
        int serviceCount;
        int[][] workIdList;
        int[] hotelIdList;
        int[] innerSearchIdList;
        int[] equipList;
        int[] priceKind;
        int[] serviceKind;
        int[] prefId;
        int searchType;
        boolean ret;
        boolean boolSort;
        String[] paramPrefId;
        String paramWeekFree;
        String paramHolidayFree;
        String paramH24Rest;
        String paramRestFrom;
        String paramRestTo;
        String paramStayFrom;
        String paramStayTo;
        String paramCredit;
        String paramParking;
        String paramCoupon;
        String paramHalfway;
        String paramReserve;
        String paramOne;
        String paramThree;
        String paramRoomService;
        String paramPayAuto;
        String paramEmpty;
        String[] paramAmenity;
        String paramJisCode;
        String paramFreeword;
        String paramAreaId;
        String paramStationId;
        String paramIcId;
        String paramBuilding;
        String paramKodate;
        String paramRentou;
        String qryString;
        String paramKuchikomi;
        String paramPoint;
        String paramCleanness;
        String paramWidth;
        String paramService;
        String paramEquip;
        String paramCost;
        String paramSort;
        String paramKind;
        String paramRoom;

        SearchHotelCommon searchHotelCommon;
        SearchEngineBasic_M2 searchEngineBasic;
        SearchHotelPrice_M2 searchHotelPrice;
        SearchHotelEquip_M2 searchHotelEquip;
        SearchHotelCity_M2 searchHotelCity;
        SearchHotelFreeword_M2 searchHotelFreeword;
        SearchHotelArea_M2 searchHotelArea;
        SearchHotelStation_M2 searchHotelStation;
        SearchHotelIc_M2 searchHotelIc;
        SearchHotelService_M2 searchHotelService;
        SearchHotelBuilding searchHotelBuilding;
        SearchHotelBbs_M2 searchHotelBbs;
        SearchHotelRoom searchHotelRoom;

        ret = false;
        boolSort = false;
        hotelIdList = new int[10000];
        serviceKind = new int[10];

        // 各種パラメタを取得する
        paramPrefId = request.getParameterValues( "pref_id" );
        paramWeekFree = request.getParameter( "week_free" );
        paramHolidayFree = request.getParameter( "holiday_free" );
        paramH24Rest = request.getParameter( "h24_rest" );
        paramRestFrom = request.getParameter( "rest_from" );
        paramRestTo = request.getParameter( "rest_to" );
        paramStayFrom = request.getParameter( "stay_from" );
        paramStayTo = request.getParameter( "stay_to" );
        paramCredit = request.getParameter( "credit" );
        paramParking = request.getParameter( "parking" );
        paramCoupon = request.getParameter( "coupon" );
        paramHalfway = request.getParameter( "halfway" );
        paramReserve = request.getParameter( "reserve" );
        paramOne = request.getParameter( "one" );
        paramThree = request.getParameter( "three" );
        paramRoomService = request.getParameter( "roomservice" );
        paramPayAuto = request.getParameter( "payauto" );
        paramEmpty = request.getParameter( "empty" );
        paramAmenity = request.getParameterValues( "amenity" );
        paramJisCode = request.getParameter( "jis_code" );
        paramFreeword = request.getParameter( "freeword" );
        paramAreaId = request.getParameter( "area_id" );
        paramStationId = request.getParameter( "station_id" );
        paramIcId = request.getParameter( "ic_id" );
        paramBuilding = request.getParameter( "building" );
        paramKodate = request.getParameter( "kodate" );
        paramRentou = request.getParameter( "rentou" );
        paramKuchikomi = request.getParameter( "kuchikomi" );
        paramPoint = request.getParameter( "point" );
        paramCleanness = request.getParameter( "cleanness" );
        paramWidth = request.getParameter( "width" );
        paramService = request.getParameter( "service" );
        paramEquip = request.getParameter( "equip" );
        paramCost = request.getParameter( "cost" );
        paramSort = request.getParameter( "sort" );
        paramKind = request.getParameter( "kind" );
        paramRoom = request.getParameter( "room" );

        findCount = 0;
        count = 0;
        qryString = "";

        if ( paramKind == null )
            paramKind = "4";
        if ( paramKuchikomi == null )
            paramKuchikomi = "0";

        // qryString = qryString + "kind=" + paramKind;

        // 建物検索 ３つのパラメータのnullチェック

        if ( paramBuilding != null )
        {
            if ( paramBuilding.compareTo( "" ) == 0 || CheckString.numCheck( paramBuilding ) == false )
                paramBuilding = "0";
            else
            {
                qryString = qryString + "&building=1";
            }
        }
        else
            paramBuilding = "0";
        if ( paramKodate != null )
        {
            if ( paramKodate.compareTo( "" ) == 0 || CheckString.numCheck( paramKodate ) == false )
                paramKodate = "0";
            else
            {
                qryString = qryString + "&kodate=1";
            }
        }
        else
            paramKodate = "0";
        if ( paramRentou != null )
        {
            if ( paramRentou.compareTo( "" ) == 0 || CheckString.numCheck( paramRentou ) == false )
                paramRentou = "0";
            else
            {
                qryString = qryString + "&rentou=1";
            }
        }
        else
            paramRentou = "0";
        // 建物検索の場合分け（OR検索のためのパターン分け）
        if ( Integer.parseInt( paramBuilding ) == 1 && Integer.parseInt( paramKodate ) == 1 && Integer.parseInt( paramRentou ) == 1 )
        {
            searchType = 7;
        }
        else if ( Integer.parseInt( paramBuilding ) == 1 && Integer.parseInt( paramKodate ) == 0 && Integer.parseInt( paramRentou ) == 0 )
        {
            searchType = 1;
        }
        else if ( Integer.parseInt( paramBuilding ) == 0 && Integer.parseInt( paramKodate ) == 1 && Integer.parseInt( paramRentou ) == 0 )
        {
            searchType = 2;
        }
        else if ( Integer.parseInt( paramBuilding ) == 0 && Integer.parseInt( paramKodate ) == 0 && Integer.parseInt( paramRentou ) == 1 )
        {
            searchType = 3;
        }
        else if ( Integer.parseInt( paramBuilding ) == 1 && Integer.parseInt( paramKodate ) == 1 && Integer.parseInt( paramRentou ) == 0 )
        {
            searchType = 4;
        }
        else if ( Integer.parseInt( paramBuilding ) == 1 && Integer.parseInt( paramKodate ) == 0 && Integer.parseInt( paramRentou ) == 1 )
        {
            searchType = 5;
        }
        else if ( Integer.parseInt( paramBuilding ) == 0 && Integer.parseInt( paramKodate ) == 1 && Integer.parseInt( paramRentou ) == 1 )
        {
            searchType = 6;
        }
        else
            searchType = 0;

        // 各パラメタに一致するホテルを取得
        // 都道府県検索
        if ( paramPrefId != null )
        {
            if ( paramPrefId.length > 0 )
            {
                prefId = new int[paramPrefId.length];
                int pid;
                for( j = 0 ; j < paramPrefId.length ; j++ )
                {
                    pid = Integer.parseInt( paramPrefId[j] );
                    prefId[j] = pid;
                    // 全国の場合は終了
                    if ( pid == 0 )
                    {
                        qryString = qryString + "&pref_id=" + paramPrefId[j];
                        break;
                    }

                    qryString = qryString + "&pref_id=" + paramPrefId[j];
                }
                searchEngineBasic = new SearchEngineBasic_M2();
                searchEngineBasic.getPrefHotelList( prefId );

                workIdList = searchEngineBasic.getHotelIdList();
                if ( workIdList != null )
                {
                    for( k = 0 ; k < workIdList.length ; k++ )
                    {
                        for( l = 0 ; l < workIdList[k].length ; l++ )
                        {
                            hotelIdList[count++] = workIdList[k][l];
                        }
                    }
                }
                searchEngineBasic = null;
                findCount++;
            }
        }

        // 料金検索（平日フリータイム）
        searchHotelPrice = new SearchHotelPrice_M2();
        if ( paramWeekFree != null )
        {
            priceKind = new int[1];
            priceKind[0] = 3;
            ret = searchHotelPrice.getHotelIdList( null, priceKind, 0, 999999, 0, 0 );
            if ( ret != false )
            {
                if ( findCount != 0 )
                {
                    // マージする
                    searchHotelCommon = new SearchHotelCommon();
                    searchHotelCommon.setResultHotelList( hotelIdList );
                    searchHotelCommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                    hotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                }
                else
                {
                    hotelIdList = searchHotelPrice.getHotelIdList();
                }

                findCount++;
            }
            qryString = qryString + "&week_free=" + paramWeekFree;
        }
        // 料金検索（休日フリータイム）
        if ( paramHolidayFree != null )
        {
            priceKind = new int[1];
            priceKind[0] = 4;
            ret = searchHotelPrice.getHotelIdList( null, priceKind, 0, 999999, 0, 0 );
            if ( ret != false )
            {
                if ( findCount != 0 )
                {
                    // マージする
                    searchHotelCommon = new SearchHotelCommon();
                    searchHotelCommon.setResultHotelList( hotelIdList );
                    searchHotelCommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                    hotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                }
                else
                {
                    hotelIdList = searchHotelPrice.getHotelIdList();
                }

                findCount++;
            }
            qryString = qryString + "&holiday_free=" + paramHolidayFree;
        }
        // 料金検索（２４時間休憩）
        if ( paramH24Rest != null )
        {
            priceKind = new int[2];
            priceKind[0] = 1;
            priceKind[1] = 2;
            ret = searchHotelPrice.getHotelIdListByTime( null, priceKind, 0, 2400, 0, 0 );
            if ( ret != false )
            {
                if ( findCount != 0 )
                {
                    // マージする
                    searchHotelCommon = new SearchHotelCommon();
                    searchHotelCommon.setResultHotelList( hotelIdList );
                    searchHotelCommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                    hotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                }
                else
                {
                    hotelIdList = searchHotelPrice.getHotelIdList();
                }

                findCount++;
            }
            qryString = qryString + "&h24_rest=" + paramH24Rest;
        }

        if ( paramRestFrom != null || paramRestTo != null )
        {
            if ( paramRestFrom == null )
            {
                paramRestFrom = "0";
            }
            if ( paramRestTo == null )
            {
                paramRestTo = "999999";
            }
            if ( CheckString.numCheck( paramRestFrom ) != false && CheckString.numCheck( paramRestTo ) != false )
            {
                if ( Integer.parseInt( paramRestFrom ) != 0 || Integer.parseInt( paramRestTo ) != 999999 )
                {
                    priceKind = new int[4];
                    priceKind[0] = 1;
                    priceKind[1] = 2;
                    priceKind[2] = 3;
                    priceKind[3] = 4;
                    ret = searchHotelPrice.getHotelIdList( null, priceKind, Integer.parseInt( paramRestFrom ), Integer.parseInt( paramRestTo ), 0, 0 );
                    if ( ret != false )
                    {
                        if ( findCount != 0 )
                        {
                            // マージする
                            searchHotelCommon = new SearchHotelCommon();
                            searchHotelCommon.setResultHotelList( hotelIdList );
                            searchHotelCommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                            hotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                        }
                        else
                        {
                            hotelIdList = searchHotelPrice.getHotelIdList();
                        }

                        findCount++;
                    }
                }
                qryString = qryString + "&rest_from=" + paramRestFrom + "&rest_to=" + paramRestTo;

            }
        }
        // 料金検索
        if ( paramStayFrom != null || paramStayTo != null )
        {
            if ( paramStayFrom == null )
            {
                paramStayFrom = "0";
            }
            if ( paramStayTo == null )
            {
                paramStayTo = "999999";
            }

            if ( CheckString.numCheck( paramStayFrom ) != false && CheckString.numCheck( paramStayTo ) != false )
            {
                if ( Integer.parseInt( paramStayFrom ) != 0 || Integer.parseInt( paramStayTo ) != 999999 )
                {
                    priceKind = new int[2];
                    priceKind[0] = 5;
                    priceKind[1] = 6;
                    ret = searchHotelPrice.getHotelIdList( null, priceKind, Integer.parseInt( paramStayFrom ), Integer.parseInt( paramStayTo ), 0, 0 );
                    if ( ret != false )
                    {
                        if ( findCount != 0 )
                        {
                            // マージする
                            searchHotelCommon = new SearchHotelCommon();
                            searchHotelCommon.setResultHotelList( hotelIdList );
                            searchHotelCommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                            hotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                        }
                        else
                        {
                            hotelIdList = searchHotelPrice.getHotelIdList();
                        }

                        findCount++;
                    }
                }
                qryString = qryString + "&stay_from=" + paramStayFrom + "&stay_to=" + paramStayTo;
            }
        }

        searchHotelPrice = null;

        // サービス・空室検索
        serviceCount = 0;

        if ( paramCredit != null )
        {
            serviceKind[0] = 1;
            serviceCount++;

            qryString = qryString + "&credit=" + paramCredit;
        }
        if ( paramParking != null )
        {
            serviceKind[1] = 1;
            serviceCount++;

            qryString = qryString + "&parking=" + paramParking;
        }
        if ( paramCoupon != null )
        {
            serviceKind[2] = 1;
            serviceCount++;

            qryString = qryString + "&coupon=" + paramCoupon;
        }
        if ( paramHalfway != null )
        {
            serviceKind[3] = 1;
            serviceCount++;

            qryString = qryString + "&halfway=" + paramHalfway;
        }
        if ( paramReserve != null )
        {
            serviceKind[4] = 1;
            serviceCount++;

            qryString = qryString + "&reserve=" + paramReserve;
        }
        if ( paramOne != null )
        {
            serviceKind[5] = 1;
            serviceCount++;

            qryString = qryString + "&one=" + paramOne;
        }
        if ( paramThree != null )
        {
            serviceKind[6] = 1;
            serviceCount++;

            qryString = qryString + "&three=" + paramThree;
        }
        if ( paramRoomService != null )
        {
            serviceKind[7] = 1;
            serviceCount++;

            qryString = qryString + "&roomservice=" + paramRoomService;
        }
        if ( paramPayAuto != null )
        {
            serviceKind[8] = 1;
            serviceCount++;

            qryString = qryString + "&payauto=" + paramPayAuto;
        }
        if ( paramEmpty != null )
        {
            serviceKind[9] = 1;
            serviceCount++;

            qryString = qryString + "&empty=" + paramEmpty;
        }
        if ( serviceCount > 0 )
        {
            searchHotelService = new SearchHotelService_M2();
            ret = searchHotelService.getHotelIdList( serviceKind, 0, 0, 0 );
            if ( ret != false )
            {
                if ( findCount != 0 )
                {
                    // マージする
                    searchHotelCommon = new SearchHotelCommon();
                    searchHotelCommon.setResultHotelList( hotelIdList );
                    searchHotelCommon.setEquipHotelList( searchHotelService.getHotelIdList() );
                    hotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                }
                else
                {
                    hotelIdList = searchHotelService.getHotelIdList();
                }

                findCount++;
            }
            searchHotelService = null;
        }
        // 設備
        if ( paramAmenity != null )
        {
            if ( paramAmenity.length > 0 )
            {
                equipList = new int[paramAmenity.length];

                for( j = 0 ; j < paramAmenity.length ; j++ )
                {
                    if ( CheckString.numCheck( paramAmenity[j] ) != false )
                    {
                        equipList[j] = Integer.parseInt( paramAmenity[j] );
                    }
                    qryString = qryString + "&amenity=" + paramAmenity[j];
                }

                searchHotelEquip = new SearchHotelEquip_M2();
                ret = searchHotelEquip.getHotelIdList( equipList, 0, 0 );
                if ( ret != false )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        searchHotelCommon = new SearchHotelCommon();
                        searchHotelCommon.setResultHotelList( hotelIdList );
                        searchHotelCommon.setEquipHotelList( searchHotelEquip.getHotelIdList() );
                        hotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = searchHotelEquip.getHotelIdList();
                    }
                }
                searchHotelEquip = null;
                findCount++;
            }
        }
        // 市区町村
        if ( paramJisCode != null )
        {
            if ( CheckString.numCheck( paramJisCode ) != false )
            {
                searchHotelCity = new SearchHotelCity_M2();
                innerSearchIdList = searchHotelCity.getHotelIdList( Integer.parseInt( paramJisCode ) );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        searchHotelCommon = new SearchHotelCommon();
                        searchHotelCommon.setResultHotelList( hotelIdList );
                        searchHotelCommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&jis_code=" + paramJisCode;
                searchHotelCity = null;

            }
        }
        // フリーワード
        if ( paramFreeword != null )
        {
            if ( paramFreeword.compareTo( "" ) != 0 )
            {
                try
                {
                    paramFreeword = new String( paramFreeword.getBytes( "8859_1" ), "Windows-31J" );
                }
                catch ( UnsupportedEncodingException e )
                {
                    Logging.error( "[SearchKodawari.searchHotels() ] Exception=" + e.toString() );
                }
                searchHotelFreeword = new SearchHotelFreeword_M2();
                innerSearchIdList = searchHotelFreeword.getSearchIdList( paramFreeword );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        searchHotelCommon = new SearchHotelCommon();
                        searchHotelCommon.setResultHotelList( hotelIdList );
                        searchHotelCommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&freeword=" + paramFreeword;
                searchHotelFreeword = null;
            }
        }
        // ホテル街
        if ( paramAreaId != null )
        {
            if ( CheckString.numCheck( paramAreaId ) != false )
            {
                searchHotelArea = new SearchHotelArea_M2();
                innerSearchIdList = searchHotelArea.getSearchIdList( Integer.parseInt( paramAreaId ) );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        searchHotelCommon = new SearchHotelCommon();
                        searchHotelCommon.setResultHotelList( hotelIdList );
                        searchHotelCommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&area_id=" + paramAreaId;
                searchHotelArea = null;
            }
        }
        // 駅
        if ( paramStationId != null )
        {
            if ( paramStationId.compareTo( "" ) != 0 )
            {
                searchHotelStation = new SearchHotelStation_M2();
                innerSearchIdList = searchHotelStation.getHotelIdList( paramStationId );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        searchHotelCommon = new SearchHotelCommon();
                        searchHotelCommon.setResultHotelList( hotelIdList );
                        searchHotelCommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&station_id=" + paramStationId;
                searchHotelStation = null;
            }
        }
        // IC
        if ( paramIcId != null )
        {
            if ( paramIcId.compareTo( "" ) != 0 )
            {
                searchHotelIc = new SearchHotelIc_M2();
                innerSearchIdList = searchHotelIc.getSearchIdList( paramIcId );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        searchHotelCommon = new SearchHotelCommon();
                        searchHotelCommon.setResultHotelList( hotelIdList );
                        searchHotelCommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&ic_id=" + paramIcId;
                searchHotelIc = null;
            }
        }

        // 部屋画像検索
        if ( paramRoom != null )
        {
            searchHotelRoom = new SearchHotelRoom();
            if ( findCount > 0 )
            {
                ret = searchHotelRoom.getHotelList( hotelIdList, 0, 0 );
                if ( ret != false )
                {
                    // マージする
                    searchHotelCommon = new SearchHotelCommon();
                    searchHotelCommon.setResultHotelList( hotelIdList );
                    searchHotelCommon.setEquipHotelList( searchHotelRoom.getHotelIdList() );
                    hotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                }
            }
            else
            {
                ret = searchHotelRoom.getHotelList( null, 0, 0 );
                if ( ret != false )
                {
                    hotelIdList = searchHotelRoom.getHotelIdList();
                }
            }
            findCount++;
            qryString = qryString + "&room=" + paramRoom;
            searchHotelRoom = null;

        }

        // 建物形式別
        if ( searchType > 0 && searchType < 8 )
        {
            searchHotelBuilding = new SearchHotelBuilding();
            if ( findCount > 0 )
            {
                ret = searchHotelBuilding.getHotelList( hotelIdList, searchType, 0, 0 );
                if ( ret != false )
                {
                    // マージする
                    searchHotelCommon = new SearchHotelCommon();
                    searchHotelCommon.setResultHotelList( hotelIdList );
                    searchHotelCommon.setEquipHotelList( searchHotelBuilding.getHotelIdList() );
                    hotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                }
            }
            else
            {
                ret = searchHotelBuilding.getHotelList( null, searchType, 0, 0 );
                if ( ret != false )
                {
                    hotelIdList = searchHotelBuilding.getHotelIdList();
                }
            }
            findCount++;
            searchHotelBuilding = null;
        }

        // クチコミ検索
        if ( paramKuchikomi != null )
        {
            if ( paramPoint == null )
                paramPoint = "0";
            if ( paramCleanness == null )
                paramCleanness = "0";
            if ( paramWidth == null )
                paramWidth = "0";
            if ( paramEquip == null )
                paramEquip = "0";
            if ( paramService == null )
                paramService = "0";
            if ( paramCost == null )
                paramCost = "0";

            if ( Integer.parseInt( paramKuchikomi ) > 0 || Integer.parseInt( paramPoint ) > 0 || Integer.parseInt( paramCleanness ) > 0 || Integer.parseInt( paramWidth ) > 0
                    || Integer.parseInt( paramEquip ) > 0 || Integer.parseInt( paramService ) > 0 || Integer.parseInt( paramCost ) > 0 )
            {
                boolSort = true;

                searchHotelBbs = new SearchHotelBbs_M2();
                // if( paramSort == null ) paramSort = "1";
                if ( findCount > 0 )
                {

                    ret = searchHotelBbs.getHotelList( hotelIdList, Integer.parseInt( paramKuchikomi ), Integer.parseInt( paramPoint ), Integer.parseInt( paramCleanness ),
                            Integer.parseInt( paramWidth ), Integer.parseInt( paramService ), Integer.parseInt( paramEquip ), Integer.parseInt( paramCost ), 0, 0 );

                    if ( ret != false )
                    {
                        hotelIdList = searchHotelBbs.getHotelIdList();
                    }
                }
                else
                {
                    ret = searchHotelBbs.getHotelList( null, Integer.parseInt( paramKuchikomi ), Integer.parseInt( paramPoint ), Integer.parseInt( paramCleanness ),
                            Integer.parseInt( paramWidth ), Integer.parseInt( paramService ), Integer.parseInt( paramEquip ), Integer.parseInt( paramCost ), 0, 0 );

                    if ( ret != false )
                    {
                        hotelIdList = searchHotelBbs.getHotelIdList();
                    }
                }
                qryString = qryString + "&kuchikomi=" + paramKuchikomi
                        + "&point=" + paramPoint
                        + "&cleanness=" + paramCleanness
                        + "&width=" + paramWidth
                        + "&service=" + paramService
                        + "&equip=" + paramEquip
                        + "&cost=" + paramCost;

                searchHotelBbs = null;
            }
        }

        count = 0;
        hotelAllCount = 0;

        if ( hotelIdList.length > 0 )
        {
            // PV順に並び替えるときに使用する
            if ( paramSort == null )
                paramSort = "0";
            if ( paramSort.compareTo( "0" ) == 0 )
            {
                searchHotelCommon = new SearchHotelCommon();
                hotelIdList = searchHotelCommon.sortHotelRank( hotelIdList );
            }

            for( j = 0 ; j < hotelIdList.length ; j++ )
            {
                if ( hotelIdList[j] == 0 )
                {
                    break;
                }
                hotelAllCount++;
            }
        }
        String sortparams[] = { paramSort, "" + boolSort };
        this.setSortParams( sortparams );
        this.m_hotelAllCount = hotelAllCount;
        // qryString = "kind=4&" + qryString;
        this.m_queryString = qryString;

        return hotelIdList;

    }

    public int[] searchHotelsMobile(HttpServletRequest request, int prefId) throws Exception
    {

        int j;
        int k;
        int l;
        int count;
        int findCount;
        int hotelAllCount;
        int serviceCount;
        int[][] workIdList;
        int[] hotelIdList;
        int[] innerSearchIdList;
        int[] equipList;
        int[] priceKind;
        int[] serviceKind;
        int searchType;
        boolean ret;
        boolean boolSort;
        String paramWeekFree;
        String paramHolidayFree;
        String paramH24Rest;
        String paramRestFrom;
        String paramRestTo;
        String paramStayFrom;
        String paramStayTo;
        String paramCredit;
        String paramParking;
        String paramCoupon;
        String paramHalfway;
        String paramReserve;
        String paramOne;
        String paramThree;
        String paramRoomService;
        String paramPayAuto;
        String paramEmpty;
        String[] paramAmenity;
        String paramJisCode;
        String paramFreeword;
        String paramAreaId;
        String paramStationId;
        String paramIcId;
        String paramBuilding;
        String paramKodate;
        String paramRentou;
        String qryString;
        String paramKuchikomi;
        String paramPoint;
        String paramCleanness;
        String paramWidth;
        String paramService;
        String paramEquip;
        String paramCost;
        String paramSort;
        String paramRoom;
        String paramLonPos = "";
        String paramLatPos = "";
        String paramScale;

        SearchHotelCommon shcommon;
        SearchEngineBasic_M2 searchEngineBasic;
        SearchHotelPrice_M2 searchHotelPrice;
        SearchHotelEquip_M2 searchHotelEquip;
        SearchHotelCity_M2 searchHotelCity;
        SearchHotelFreeword_M2 searchHotelFreeword;
        SearchHotelArea_M2 searchHotelArea;
        SearchHotelStation_M2 searchHotelStation;
        SearchHotelIc_M2 searchHotelIc;
        SearchHotelService_M2 searchHotelService;
        SearchHotelBuilding searchHotelBuilding;
        SearchHotelBbs_M2 searchHotelBbs;
        SearchHotelRoom searchHotelRoom;
        SearchHotelGps_M2 searchHotelGps;

        ret = false;
        boolSort = false;
        hotelIdList = new int[10000];
        serviceKind = new int[10];

        // 各種パラメタを取得する

        paramWeekFree = request.getParameter( "week_free" );
        paramHolidayFree = request.getParameter( "holiday_free" );
        paramH24Rest = request.getParameter( "h24_rest" );
        paramRestFrom = request.getParameter( "rest_from" );
        paramRestTo = request.getParameter( "rest_to" );
        paramStayFrom = request.getParameter( "stay_from" );
        paramStayTo = request.getParameter( "stay_to" );
        paramCredit = request.getParameter( "credit" );
        paramParking = request.getParameter( "parking" );
        paramCoupon = request.getParameter( "coupon" );
        paramHalfway = request.getParameter( "halfway" );
        paramReserve = request.getParameter( "reserve" );
        paramOne = request.getParameter( "one" );
        paramThree = request.getParameter( "three" );
        paramRoomService = request.getParameter( "roomservice" );
        paramPayAuto = request.getParameter( "payauto" );
        paramEmpty = request.getParameter( "empty" );
        paramAmenity = request.getParameterValues( "amenity" );
        paramJisCode = request.getParameter( "jis_code" );
        paramFreeword = request.getParameter( "freeword" );
        paramAreaId = request.getParameter( "area_id" );
        paramStationId = request.getParameter( "station_id" );
        paramIcId = request.getParameter( "ic_id" );
        paramBuilding = request.getParameter( "building" );
        paramKodate = request.getParameter( "kodate" );
        paramRentou = request.getParameter( "rentou" );
        paramKuchikomi = request.getParameter( "kuchikomi" );
        paramPoint = request.getParameter( "point" );
        paramCleanness = request.getParameter( "cleanness" );
        paramWidth = request.getParameter( "width" );
        paramService = request.getParameter( "service" );
        paramEquip = request.getParameter( "equip" );
        paramCost = request.getParameter( "cost" );
        paramSort = request.getParameter( "sort" );
        paramRoom = request.getParameter( "room" );
        paramLonPos = request.getParameter( "lon" );
        paramLatPos = request.getParameter( "lat" );
        paramScale = request.getParameter( "scale" );

        findCount = 0;
        count = 0;
        qryString = "";

        if ( paramKuchikomi == null )
            paramKuchikomi = "0";

        // 建物検索 ３つのパラメータのnullチェック

        if ( paramBuilding != null )
        {
            if ( paramBuilding.compareTo( "" ) == 0 || CheckString.numCheck( paramBuilding ) == false )
                paramBuilding = "0";
            else
            {
                qryString = qryString + "&building=1";
            }
        }
        else
            paramBuilding = "0";
        if ( paramKodate != null )
        {
            if ( paramKodate.compareTo( "" ) == 0 || CheckString.numCheck( paramKodate ) == false )
                paramKodate = "0";
            else
            {
                qryString = qryString + "&kodate=1";
            }
        }
        else
            paramKodate = "0";
        if ( paramRentou != null )
        {
            if ( paramRentou.compareTo( "" ) == 0 || CheckString.numCheck( paramRentou ) == false )
                paramRentou = "0";
            else
            {
                qryString = qryString + "&rentou=1";
            }
        }
        else
            paramRentou = "0";
        // 建物検索の場合分け（OR検索のためのパターン分け）
        if ( Integer.parseInt( paramBuilding ) == 1 && Integer.parseInt( paramKodate ) == 1 && Integer.parseInt( paramRentou ) == 1 )
        {
            searchType = 7;
        }
        else if ( Integer.parseInt( paramBuilding ) == 1 && Integer.parseInt( paramKodate ) == 0 && Integer.parseInt( paramRentou ) == 0 )
        {
            searchType = 1;
        }
        else if ( Integer.parseInt( paramBuilding ) == 0 && Integer.parseInt( paramKodate ) == 1 && Integer.parseInt( paramRentou ) == 0 )
        {
            searchType = 2;
        }
        else if ( Integer.parseInt( paramBuilding ) == 0 && Integer.parseInt( paramKodate ) == 0 && Integer.parseInt( paramRentou ) == 1 )
        {
            searchType = 3;
        }
        else if ( Integer.parseInt( paramBuilding ) == 1 && Integer.parseInt( paramKodate ) == 1 && Integer.parseInt( paramRentou ) == 0 )
        {
            searchType = 4;
        }
        else if ( Integer.parseInt( paramBuilding ) == 1 && Integer.parseInt( paramKodate ) == 0 && Integer.parseInt( paramRentou ) == 1 )
        {
            searchType = 5;
        }
        else if ( Integer.parseInt( paramBuilding ) == 0 && Integer.parseInt( paramKodate ) == 1 && Integer.parseInt( paramRentou ) == 1 )
        {
            searchType = 6;
        }
        else
            searchType = 0;

        // 各パラメタに一致するホテルを取得
        // 都道府県検索
        if ( prefId > 0 )
        {

            searchEngineBasic = new SearchEngineBasic_M2();
            searchEngineBasic.getPrefHotelList( new int[]{ prefId } );

            workIdList = searchEngineBasic.getHotelIdList();
            if ( workIdList != null )
            {
                for( k = 0 ; k < workIdList.length ; k++ )
                {
                    for( l = 0 ; l < workIdList[k].length ; l++ )
                    {
                        hotelIdList[count++] = workIdList[k][l];
                    }
                }
            }
            searchEngineBasic = null;
            findCount++;

        }

        // GPS
        if ( paramLatPos != null && paramLonPos != null )
        {

            if ( CheckString.numCheck( paramScale ) == false )
            {
                paramScale = "0";
            }
            if ( (paramLatPos.compareTo( "" ) != 0) && (paramLonPos.compareTo( "" ) != 0) )
            {
                searchHotelGps = new SearchHotelGps_M2();
                innerSearchIdList = searchHotelGps.getHotelIdList( paramLatPos, paramLonPos, Integer.parseInt( paramScale ) );

                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        shcommon = new SearchHotelCommon();
                        shcommon.setResultHotelList( hotelIdList );
                        shcommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                }
                qryString = qryString + "&lat=" + paramLatPos + "&lon=" + paramLonPos + "&scale=" + paramScale;
                findCount++;
                innerSearchIdList = null;
                searchHotelGps = null;
            }
        }

        // 料金検索（平日フリータイム）
        searchHotelPrice = new SearchHotelPrice_M2();
        if ( paramWeekFree != null )
        {
            priceKind = new int[1];
            priceKind[0] = 3;
            ret = searchHotelPrice.getHotelIdList( null, priceKind, 0, 999999, 0, 0 );
            if ( ret != false )
            {
                if ( findCount != 0 )
                {
                    // マージする
                    shcommon = new SearchHotelCommon();
                    shcommon.setResultHotelList( hotelIdList );
                    shcommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                    hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                }
                else
                {
                    hotelIdList = searchHotelPrice.getHotelIdList();
                }

                findCount++;
            }
            qryString = qryString + "&week_free=" + paramWeekFree;
        }
        // 料金検索（休日フリータイム）
        if ( paramHolidayFree != null )
        {
            priceKind = new int[1];
            priceKind[0] = 4;
            ret = searchHotelPrice.getHotelIdList( null, priceKind, 0, 999999, 0, 0 );
            if ( ret != false )
            {
                if ( findCount != 0 )
                {
                    // マージする
                    shcommon = new SearchHotelCommon();
                    shcommon.setResultHotelList( hotelIdList );
                    shcommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                    hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                }
                else
                {
                    hotelIdList = searchHotelPrice.getHotelIdList();
                }

                findCount++;
            }
            qryString = qryString + "&holiday_free=" + paramHolidayFree;
        }
        // 料金検索（２４時間休憩）
        if ( paramH24Rest != null )
        {
            priceKind = new int[2];
            priceKind[0] = 1;
            priceKind[1] = 2;
            ret = searchHotelPrice.getHotelIdListByTime( null, priceKind, 0, 2400, 0, 0 );
            if ( ret != false )
            {
                if ( findCount != 0 )
                {
                    // マージする
                    shcommon = new SearchHotelCommon();
                    shcommon.setResultHotelList( hotelIdList );
                    shcommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                    hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                }
                else
                {
                    hotelIdList = searchHotelPrice.getHotelIdList();
                }

                findCount++;
            }
            qryString = qryString + "&h24_rest=" + paramH24Rest;
        }

        if ( paramRestFrom != null || paramRestTo != null )
        {
            if ( paramRestFrom == null )
            {
                paramRestFrom = "0";
            }
            if ( paramRestTo == null )
            {
                paramRestTo = "999999";
            }

            if ( CheckString.numCheck( paramRestFrom ) != false && CheckString.numCheck( paramRestTo ) != false )
            {
                if ( Integer.parseInt( paramRestFrom ) != 0 || Integer.parseInt( paramRestTo ) != 999999 )
                {
                    priceKind = new int[4];
                    priceKind[0] = 1;
                    priceKind[1] = 2;
                    priceKind[2] = 3;
                    priceKind[3] = 4;
                    ret = searchHotelPrice.getHotelIdList( null, priceKind, Integer.parseInt( paramRestFrom ), Integer.parseInt( paramRestTo ), 0, 0 );
                    if ( ret != false )
                    {
                        if ( findCount != 0 )
                        {
                            // マージする
                            shcommon = new SearchHotelCommon();
                            shcommon.setResultHotelList( hotelIdList );
                            shcommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                            hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                        }
                        else
                        {
                            hotelIdList = searchHotelPrice.getHotelIdList();
                        }

                        findCount++;
                    }
                }
                qryString = qryString + "&rest_from=" + paramRestFrom + "&rest_to=" + paramRestTo;

            }
        }
        // 料金検索
        if ( paramStayFrom != null || paramStayTo != null )
        {
            if ( paramStayFrom == null )
            {
                paramStayFrom = "0";
            }
            if ( paramStayTo == null )
            {
                paramStayTo = "999999";
            }

            if ( CheckString.numCheck( paramStayFrom ) != false && CheckString.numCheck( paramStayTo ) != false )
            {
                if ( Integer.parseInt( paramStayFrom ) != 0 || Integer.parseInt( paramStayTo ) != 999999 )
                {
                    priceKind = new int[2];
                    priceKind[0] = 5;
                    priceKind[1] = 6;
                    ret = searchHotelPrice.getHotelIdList( null, priceKind, Integer.parseInt( paramStayFrom ), Integer.parseInt( paramStayTo ), 0, 0 );
                    if ( ret != false )
                    {
                        if ( findCount != 0 )
                        {
                            // マージする
                            shcommon = new SearchHotelCommon();
                            shcommon.setResultHotelList( hotelIdList );
                            shcommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                            hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                        }
                        else
                        {
                            hotelIdList = searchHotelPrice.getHotelIdList();
                        }

                        findCount++;
                    }
                }
                qryString = qryString + "&stay_from=" + paramStayFrom + "&stay_to=" + paramStayTo;
            }
        }

        searchHotelPrice = null;

        // サービス・空室検索
        serviceCount = 0;

        if ( paramCredit != null )
        {
            serviceKind[0] = 1;
            serviceCount++;

            qryString = qryString + "&credit=" + paramCredit;
        }
        if ( paramParking != null )
        {
            serviceKind[1] = 1;
            serviceCount++;

            qryString = qryString + "&parking=" + paramParking;
        }
        if ( paramCoupon != null )
        {
            serviceKind[2] = 1;
            serviceCount++;

            qryString = qryString + "&coupon=" + paramCoupon;
        }
        if ( paramHalfway != null )
        {
            serviceKind[3] = 1;
            serviceCount++;

            qryString = qryString + "&halfway=" + paramHalfway;
        }
        if ( paramReserve != null )
        {
            serviceKind[4] = 1;
            serviceCount++;

            qryString = qryString + "&reserve=" + paramReserve;
        }
        if ( paramOne != null )
        {
            serviceKind[5] = 1;
            serviceCount++;

            qryString = qryString + "&one=" + paramOne;
        }
        if ( paramThree != null )
        {
            serviceKind[6] = 1;
            serviceCount++;

            qryString = qryString + "&three=" + paramThree;
        }
        if ( paramRoomService != null )
        {
            serviceKind[7] = 1;
            serviceCount++;

            qryString = qryString + "&roomservice=" + paramRoomService;
        }
        if ( paramPayAuto != null )
        {
            serviceKind[8] = 1;
            serviceCount++;

            qryString = qryString + "&payauto=" + paramPayAuto;
        }
        if ( paramEmpty != null )
        {
            serviceKind[9] = 1;
            serviceCount++;

            qryString = qryString + "&empty=" + paramEmpty;
        }
        if ( serviceCount > 0 )
        {
            searchHotelService = new SearchHotelService_M2();
            ret = searchHotelService.getHotelIdList( serviceKind, 0, 0, 1 );
            if ( ret != false )
            {
                if ( findCount != 0 )
                {
                    // マージする
                    shcommon = new SearchHotelCommon();
                    shcommon.setResultHotelList( hotelIdList );
                    shcommon.setEquipHotelList( searchHotelService.getHotelIdList() );
                    hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                }
                else
                {
                    hotelIdList = searchHotelService.getHotelIdList();
                }

                findCount++;
            }
            searchHotelService = null;
        }
        // 設備
        if ( paramAmenity != null )
        {
            if ( paramAmenity.length > 0 )
            {
                equipList = new int[paramAmenity.length];

                for( j = 0 ; j < paramAmenity.length ; j++ )
                {
                    if ( CheckString.numCheck( paramAmenity[j] ) != false )
                    {
                        equipList[j] = Integer.parseInt( paramAmenity[j] );
                    }
                    qryString = qryString + "&amenity=" + paramAmenity[j];
                }

                searchHotelEquip = new SearchHotelEquip_M2();
                ret = searchHotelEquip.getHotelIdList( equipList, 0, 0 );
                if ( ret != false )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        shcommon = new SearchHotelCommon();
                        shcommon.setResultHotelList( hotelIdList );
                        shcommon.setEquipHotelList( searchHotelEquip.getHotelIdList() );
                        hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = searchHotelEquip.getHotelIdList();
                    }
                }
                searchHotelEquip = null;
                findCount++;
            }
        }
        // 市区町村
        if ( paramJisCode != null )
        {
            if ( CheckString.numCheck( paramJisCode ) != false )
            {
                searchHotelCity = new SearchHotelCity_M2();
                innerSearchIdList = searchHotelCity.getHotelIdList( Integer.parseInt( paramJisCode ) );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        shcommon = new SearchHotelCommon();
                        shcommon.setResultHotelList( hotelIdList );
                        shcommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&jis_code=" + paramJisCode;
                searchHotelCity = null;
            }
        }
        // フリーワード
        if ( paramFreeword != null )
        {
            if ( paramFreeword.compareTo( "" ) != 0 )
            {
                try
                {
                    paramFreeword = new String( paramFreeword.getBytes( "8859_1" ), "Windows-31J" );
                }
                catch ( UnsupportedEncodingException e )
                {
                    Logging.error( "[SearchKodawari.searchHotels() ] Exception=" + e.toString() );
                }

                searchHotelFreeword = new SearchHotelFreeword_M2();
                innerSearchIdList = searchHotelFreeword.getSearchIdList( paramFreeword );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        shcommon = new SearchHotelCommon();
                        shcommon.setResultHotelList( hotelIdList );
                        shcommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&freeword=" + paramFreeword;
                searchHotelFreeword = null;
            }
        }
        // ホテル街
        if ( paramAreaId != null )
        {
            if ( CheckString.numCheck( paramAreaId ) != false )
            {
                searchHotelArea = new SearchHotelArea_M2();
                innerSearchIdList = searchHotelArea.getSearchIdList( Integer.parseInt( paramAreaId ) );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        shcommon = new SearchHotelCommon();
                        shcommon.setResultHotelList( hotelIdList );
                        shcommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&area_id=" + paramAreaId;
                searchHotelArea = null;
            }
        }
        // 駅
        if ( paramStationId != null )
        {
            if ( paramStationId.compareTo( "" ) != 0 )
            {
                searchHotelStation = new SearchHotelStation_M2();
                innerSearchIdList = searchHotelStation.getHotelIdList( paramStationId );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        shcommon = new SearchHotelCommon();
                        shcommon.setResultHotelList( hotelIdList );
                        shcommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&station_id=" + paramStationId;
                searchHotelStation = null;
            }
        }
        // IC
        if ( paramIcId != null )
        {
            if ( paramIcId.compareTo( "" ) != 0 )
            {
                searchHotelIc = new SearchHotelIc_M2();
                innerSearchIdList = searchHotelIc.getSearchIdList( paramAreaId );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        shcommon = new SearchHotelCommon();
                        shcommon.setResultHotelList( hotelIdList );
                        shcommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&ic_id=" + paramIcId;
                searchHotelIc = null;
            }
        }

        // 部屋画像検索
        if ( paramRoom != null )
        {
            searchHotelRoom = new SearchHotelRoom();
            if ( findCount > 0 )
            {
                ret = searchHotelRoom.getHotelList( hotelIdList, 0, 0 );
                if ( ret != false )
                {
                    // マージする
                    shcommon = new SearchHotelCommon();
                    shcommon.setResultHotelList( hotelIdList );
                    shcommon.setEquipHotelList( searchHotelRoom.getHotelIdList() );
                    hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                }
            }
            else
            {
                ret = searchHotelRoom.getHotelList( null, 0, 0 );
                if ( ret != false )
                {
                    hotelIdList = searchHotelRoom.getHotelIdList();
                }
            }
            findCount++;
            qryString = qryString + "&room=" + paramRoom;

            searchHotelRoom = null;

        }

        // 建物形式別
        if ( searchType > 0 && searchType < 8 )
        {
            searchHotelBuilding = new SearchHotelBuilding();
            if ( findCount > 0 )
            {
                ret = searchHotelBuilding.getHotelList( hotelIdList, searchType, 0, 0 );
                if ( ret != false )
                {
                    // マージする
                    shcommon = new SearchHotelCommon();
                    shcommon.setResultHotelList( hotelIdList );
                    shcommon.setEquipHotelList( searchHotelBuilding.getHotelIdList() );
                    hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                }
            }
            else
            {
                ret = searchHotelBuilding.getHotelList( null, searchType, 0, 0 );
                if ( ret != false )
                {
                    hotelIdList = searchHotelBuilding.getHotelIdList();
                }
            }
            findCount++;
            searchHotelBuilding = null;
        }

        // クチコミ検索
        if ( paramKuchikomi != null )
        {
            if ( paramPoint == null )
                paramPoint = "0";
            if ( paramCleanness == null )
                paramCleanness = "0";
            if ( paramWidth == null )
                paramWidth = "0";
            if ( paramEquip == null )
                paramEquip = "0";
            if ( paramService == null )
                paramService = "0";
            if ( paramCost == null )
                paramCost = "0";

            if ( Integer.parseInt( paramKuchikomi ) > 0 || Integer.parseInt( paramPoint ) > 0 || Integer.parseInt( paramCleanness ) > 0 || Integer.parseInt( paramWidth ) > 0
                    || Integer.parseInt( paramEquip ) > 0 || Integer.parseInt( paramService ) > 0 || Integer.parseInt( paramCost ) > 0 )
            {
                boolSort = true;
                // if( paramSort == null ) paramSort = "1";

                searchHotelBbs = new SearchHotelBbs_M2();
                if ( findCount > 0 )
                {

                    ret = searchHotelBbs.getHotelList( hotelIdList, Integer.parseInt( paramKuchikomi ), Integer.parseInt( paramPoint ), Integer.parseInt( paramCleanness ),
                            Integer.parseInt( paramWidth ), Integer.parseInt( paramService ), Integer.parseInt( paramEquip ), Integer.parseInt( paramCost ), 0, 0 );

                    if ( ret != false )
                    {
                        hotelIdList = searchHotelBbs.getHotelIdList();
                    }
                }
                else
                {
                    ret = searchHotelBbs.getHotelList( null, Integer.parseInt( paramKuchikomi ), Integer.parseInt( paramPoint ), Integer.parseInt( paramCleanness ),
                            Integer.parseInt( paramWidth ), Integer.parseInt( paramService ), Integer.parseInt( paramEquip ), Integer.parseInt( paramCost ), 0, 0 );
                    if ( ret != false )
                    {
                        hotelIdList = searchHotelBbs.getHotelIdList();
                    }
                }
                qryString = qryString + "&kuchikomi=" + paramKuchikomi;
                qryString = qryString + "&point=" + paramPoint;
                qryString = qryString + "&cleanness=" + paramCleanness;
                qryString = qryString + "&width=" + paramWidth;
                qryString = qryString + "&service=" + paramService;
                qryString = qryString + "&equip=" + paramEquip;
                qryString = qryString + "&cost=" + paramCost;

                searchHotelBbs = null;
            }
        }

        count = 0;
        hotelAllCount = 0;

        if ( hotelIdList.length > 0 )
        {
            // PV順に並び替えるときに使用する
            if ( paramSort == null )
                paramSort = "0";
            if ( paramSort.compareTo( "0" ) == 0 )
            {
                shcommon = new SearchHotelCommon();
                hotelIdList = shcommon.sortHotelRank( hotelIdList );
            }

            for( j = 0 ; j < hotelIdList.length ; j++ )
            {
                if ( hotelIdList[j] == 0 )
                {
                    break;
                }
                hotelAllCount++;
            }
        }
        String sortparams[] = { paramSort, "" + boolSort };
        this.setSortParams( sortparams );
        this.m_hotelAllCount = hotelAllCount;
        // qryString = "kind=4&" + qryString;
        this.m_queryString = qryString;

        return hotelIdList;

    }

    public static String makeQueryString(HttpServletRequest request) throws Exception
    {
        String queryString = "";
        String paramWeekFree;
        String paramHolidayFree;
        String paramH24Rest;
        String paramRestFrom;
        String paramRestTo;
        String paramStayFrom;
        String paramStayTo;
        String paramCredit;
        String paramParking;
        String paramCoupon;
        String paramHalfway;
        String paramReserve;
        String paramOne;
        String paramThree;
        String paramRoomService;
        String paramPayAuto;
        String paramEmpty;
        String[] paramAmenity;
        String paramJisCode;
        String paramFreeword;
        String paramAreaId;
        String paramStationId;
        String paramIcId;
        String paramBuilding;
        String paramKodate;
        String paramRentou;
        String paramKuchikomi;
        String paramPoint;
        String paramCleanness;
        String paramWidth;
        String paramService;
        String paramEquip;
        String paramCost;
        String paramRoom;

        paramWeekFree = request.getParameter( "week_free" );
        paramHolidayFree = request.getParameter( "holiday_free" );
        paramH24Rest = request.getParameter( "h24_rest" );
        paramRestFrom = request.getParameter( "rest_from" );
        paramRestTo = request.getParameter( "rest_to" );
        paramStayFrom = request.getParameter( "stay_from" );
        paramStayTo = request.getParameter( "stay_to" );
        paramCredit = request.getParameter( "credit" );
        paramParking = request.getParameter( "parking" );
        paramCoupon = request.getParameter( "coupon" );
        paramHalfway = request.getParameter( "halfway" );
        paramReserve = request.getParameter( "reserve" );
        paramOne = request.getParameter( "one" );
        paramThree = request.getParameter( "three" );
        paramRoomService = request.getParameter( "roomservice" );
        paramPayAuto = request.getParameter( "payauto" );
        paramEmpty = request.getParameter( "empty" );
        paramAmenity = request.getParameterValues( "amenity" );
        paramJisCode = request.getParameter( "jis_code" );
        paramFreeword = request.getParameter( "freeword" );
        paramAreaId = request.getParameter( "area_id" );
        paramStationId = request.getParameter( "station_id" );
        paramIcId = request.getParameter( "ic_id" );
        paramBuilding = request.getParameter( "building" );
        paramKodate = request.getParameter( "kodate" );
        paramRentou = request.getParameter( "rentou" );
        paramKuchikomi = request.getParameter( "kuchikomi" );
        paramPoint = request.getParameter( "point" );
        paramCleanness = request.getParameter( "cleanness" );
        paramWidth = request.getParameter( "width" );
        paramService = request.getParameter( "service" );
        paramEquip = request.getParameter( "equip" );
        paramCost = request.getParameter( "cost" );
        paramRoom = request.getParameter( "room" );

        // 料金検索（平日フリータイム）
        if ( paramWeekFree != null )
        {
            queryString = queryString + "&week_free=" + paramWeekFree;
        }
        // 料金検索（休日フリータイム）
        if ( paramHolidayFree != null )
        {
            queryString = queryString + "&holiday_free=" + paramHolidayFree;
        }
        // 料金検索（２４時間休憩）
        if ( paramH24Rest != null )
        {
            queryString = queryString + "&h24_rest=" + paramH24Rest;
        }

        if ( paramRestFrom != null && paramRestTo != null )
        {
            if ( CheckString.numCheck( paramRestFrom ) != false && CheckString.numCheck( paramRestTo ) != false )
            {
                queryString = queryString + "&rest_from=" + paramRestFrom + "&rest_to=" + paramRestTo;
            }
        }
        // 料金検索
        if ( paramStayFrom != null && paramStayTo != null )
        {
            if ( CheckString.numCheck( paramStayFrom ) != false && CheckString.numCheck( paramStayTo ) != false )
            {
                queryString = queryString + "&stay_from=" + paramStayFrom + "&stay_to=" + paramStayTo;
            }
        }

        // サービス・空室検索

        if ( paramCredit != null )
        {
            queryString = queryString + "&credit=" + paramCredit;
        }
        if ( paramParking != null )
        {
            queryString = queryString + "&parking=" + paramParking;
        }
        if ( paramCoupon != null )
        {
            queryString = queryString + "&coupon=" + paramCoupon;
        }
        if ( paramHalfway != null )
        {
            queryString = queryString + "&halfway=" + paramHalfway;
        }
        if ( paramReserve != null )
        {
            queryString = queryString + "&reserve=" + paramReserve;
        }
        if ( paramOne != null )
        {
            queryString = queryString + "&one=" + paramOne;
        }
        if ( paramThree != null )
        {

            queryString = queryString + "&three=" + paramThree;
        }
        if ( paramRoomService != null )
        {
            queryString = queryString + "&roomservice=" + paramRoomService;
        }
        if ( paramPayAuto != null )
        {
            queryString = queryString + "&payauto=" + paramPayAuto;
        }
        if ( paramEmpty != null )
        {
            queryString = queryString + "&empty=" + paramEmpty;
        }

        // 設備
        if ( paramAmenity != null )
        {
            if ( paramAmenity.length > 0 )
            {
                for( int j = 0 ; j < paramAmenity.length ; j++ )
                {
                    queryString = queryString + "&amenity=" + paramAmenity[j];
                }
            }
        }

        // 市区町村
        if ( paramJisCode != null )
        {
            if ( CheckString.numCheck( paramJisCode ) != false )
            {
                queryString = queryString + "&jis_code=" + paramJisCode;
            }
        }
        // フリーワード
        if ( paramFreeword != null )
        {
            if ( paramFreeword.compareTo( "" ) != 0 )
            {
                paramFreeword = new String( paramFreeword.getBytes( "8859_1" ), "Windows-31J" );
                queryString = queryString + "&freeword=" + paramFreeword;
            }
        }
        // ホテル街
        if ( paramAreaId != null )
        {
            if ( CheckString.numCheck( paramAreaId ) != false )
            {
                queryString = queryString + "&area_id=" + paramAreaId;
            }
        }
        // 駅
        if ( paramStationId != null )
        {
            if ( paramStationId.compareTo( "" ) != 0 )
            {
                queryString = queryString + "&station_id=" + paramStationId;
            }
        }
        // IC
        if ( paramIcId != null )
        {
            if ( paramIcId.compareTo( "" ) != 0 )
            {
                queryString = queryString + "&ic_id=" + paramIcId;
            }
        }
        // ビル形式
        if ( paramBuilding != null )
        {
            if ( paramBuilding.compareTo( "" ) != 0 )
            {
                queryString = queryString + "&building=1";
            }
        }
        // 戸建形式
        if ( paramKodate != null )
        {
            if ( paramKodate.compareTo( "" ) != 0 )
            {
                queryString = queryString + "&kodate=1";
            }
        }
        // 連棟形式
        if ( paramRentou != null )
        {
            if ( paramRentou.compareTo( "" ) != 0 )
            {
                queryString = queryString + "&rentou=1";
            }
        }

        // 部屋画像検索
        if ( paramRoom != null )
        {
            queryString = queryString + "&room=" + paramRoom;

        }

        // クチコミ検索
        if ( paramKuchikomi != null )
        {
            if ( paramPoint == null )
                paramPoint = "0";
            if ( paramCleanness == null )
                paramCleanness = "0";
            if ( paramWidth == null )
                paramWidth = "0";
            if ( paramEquip == null )
                paramEquip = "0";
            if ( paramService == null )
                paramService = "0";
            if ( paramCost == null )
                paramCost = "0";

            if ( Integer.parseInt( paramKuchikomi ) > 0 || Integer.parseInt( paramPoint ) > 0 || Integer.parseInt( paramCleanness ) > 0 || Integer.parseInt( paramWidth ) > 0
                    || Integer.parseInt( paramEquip ) > 0 || Integer.parseInt( paramService ) > 0 || Integer.parseInt( paramCost ) > 0 )
            {
                queryString = queryString + "&kuchikomi=" + paramKuchikomi;
                queryString = queryString + "&point=" + paramPoint;
                queryString = queryString + "&cleanness=" + paramCleanness;
                queryString = queryString + "&width=" + paramWidth;
                queryString = queryString + "&service=" + paramService;
                queryString = queryString + "&equip=" + paramEquip;
                queryString = queryString + "&cost=" + paramCost;
            }
        }

        return queryString;
    }

    public void searchHotelCountMobile(HttpServletRequest request, int prefId) throws Exception
    {

        int j;
        int k;
        int l;
        int count;
        int findCount;
        int hotelAllCount;
        int serviceCount;
        int[][] workIdList;
        int[] hotelIdList;
        int[] innerSearchIdList;
        int[] equipList;
        int[] priceKind;
        int[] serviceKind;
        int searchType;
        boolean ret;
        boolean boolSort;
        String paramWeekFree;
        String paramHolidayFree;
        String paramH24Rest;
        String paramRestFrom;
        String paramRestTo;
        String paramStayFrom;
        String paramStayTo;
        String paramCredit;
        String paramParking;
        String paramCoupon;
        String paramHalfway;
        String paramReserve;
        String paramOne;
        String paramThree;
        String paramRoomService;
        String paramPayAuto;
        String paramEmpty;
        String[] paramAmenity;
        String paramJisCode;
        String paramFreeword;
        String paramAreaId;
        String paramStationId;
        String paramIcId;
        String paramBuilding;
        String paramKodate;
        String paramRentou;
        String qryString;
        String paramKuchikomi;
        String paramPoint;
        String paramCleanness;
        String paramWidth;
        String paramService;
        String paramEquip;
        String paramCost;
        String paramSort;
        String paramRoom;
        String paramLonPos = "";
        String paramLatPos = "";
        String paramScale;

        SearchHotelCommon shcommon;
        SearchEngineBasic_M2 searchEngineBasic;
        SearchHotelPrice_M2 searchHotelPrice;
        SearchHotelEquip_M2 searchHotelEquip;
        SearchHotelCity_M2 searchHotelCity;
        SearchHotelFreeword_M2 searchHotelFreeword;
        SearchHotelArea_M2 searchHotelArea;
        SearchHotelStation_M2 searchHotelStation;
        SearchHotelIc_M2 searchHotelIc;
        SearchHotelService_M2 searchHotelService;
        SearchHotelBuilding searchHotelBuilding;
        SearchHotelBbs_M2 searchHotelBbs;
        SearchHotelRoom searchHotelRoom;
        SearchHotelGps_M2 searchHotelGps;

        ret = false;
        boolSort = false;
        hotelIdList = new int[10000];
        serviceKind = new int[10];

        // 各種パラメタを取得する

        paramWeekFree = request.getParameter( "week_free" );
        paramHolidayFree = request.getParameter( "holiday_free" );
        paramH24Rest = request.getParameter( "h24_rest" );
        paramRestFrom = request.getParameter( "rest_from" );
        paramRestTo = request.getParameter( "rest_to" );
        paramStayFrom = request.getParameter( "stay_from" );
        paramStayTo = request.getParameter( "stay_to" );
        paramCredit = request.getParameter( "credit" );
        paramParking = request.getParameter( "parking" );
        paramCoupon = request.getParameter( "coupon" );
        paramHalfway = request.getParameter( "halfway" );
        paramReserve = request.getParameter( "reserve" );
        paramOne = request.getParameter( "one" );
        paramThree = request.getParameter( "three" );
        paramRoomService = request.getParameter( "roomservice" );
        paramPayAuto = request.getParameter( "payauto" );
        paramEmpty = request.getParameter( "empty" );
        paramAmenity = request.getParameterValues( "amenity" );
        paramJisCode = request.getParameter( "jis_code" );
        paramFreeword = request.getParameter( "freeword" );
        paramAreaId = request.getParameter( "area_id" );
        paramStationId = request.getParameter( "station_id" );
        paramIcId = request.getParameter( "ic_id" );
        paramBuilding = request.getParameter( "building" );
        paramKodate = request.getParameter( "kodate" );
        paramRentou = request.getParameter( "rentou" );
        paramKuchikomi = request.getParameter( "kuchikomi" );
        paramPoint = request.getParameter( "point" );
        paramCleanness = request.getParameter( "cleanness" );
        paramWidth = request.getParameter( "width" );
        paramService = request.getParameter( "service" );
        paramEquip = request.getParameter( "equip" );
        paramCost = request.getParameter( "cost" );
        paramSort = request.getParameter( "sort" );
        paramRoom = request.getParameter( "room" );
        paramLonPos = request.getParameter( "lon" );
        paramLatPos = request.getParameter( "lat" );
        paramScale = request.getParameter( "scale" );

        findCount = 0;
        count = 0;
        qryString = "";

        if ( paramKuchikomi == null )
            paramKuchikomi = "0";

        // 建物検索 ３つのパラメータのnullチェック

        if ( paramBuilding != null )
        {
            if ( paramBuilding.compareTo( "" ) == 0 || CheckString.numCheck( paramBuilding ) == false )
                paramBuilding = "0";
            else
            {
                qryString = qryString + "&building=1";
            }
        }
        else
            paramBuilding = "0";
        if ( paramKodate != null )
        {
            if ( paramKodate.compareTo( "" ) == 0 || CheckString.numCheck( paramKodate ) == false )
                paramKodate = "0";
            else
            {
                qryString = qryString + "&kodate=1";
            }
        }
        else
            paramKodate = "0";
        if ( paramRentou != null )
        {
            if ( paramRentou.compareTo( "" ) == 0 || CheckString.numCheck( paramRentou ) == false )
                paramRentou = "0";
            else
            {
                qryString = qryString + "&rentou=1";
            }
        }
        else
            paramRentou = "0";
        // 建物検索の場合分け（OR検索のためのパターン分け）
        if ( Integer.parseInt( paramBuilding ) == 1 && Integer.parseInt( paramKodate ) == 1 && Integer.parseInt( paramRentou ) == 1 )
        {
            searchType = 7;
        }
        else if ( Integer.parseInt( paramBuilding ) == 1 && Integer.parseInt( paramKodate ) == 0 && Integer.parseInt( paramRentou ) == 0 )
        {
            searchType = 1;
        }
        else if ( Integer.parseInt( paramBuilding ) == 0 && Integer.parseInt( paramKodate ) == 1 && Integer.parseInt( paramRentou ) == 0 )
        {
            searchType = 2;
        }
        else if ( Integer.parseInt( paramBuilding ) == 0 && Integer.parseInt( paramKodate ) == 0 && Integer.parseInt( paramRentou ) == 1 )
        {
            searchType = 3;
        }
        else if ( Integer.parseInt( paramBuilding ) == 1 && Integer.parseInt( paramKodate ) == 1 && Integer.parseInt( paramRentou ) == 0 )
        {
            searchType = 4;
        }
        else if ( Integer.parseInt( paramBuilding ) == 1 && Integer.parseInt( paramKodate ) == 0 && Integer.parseInt( paramRentou ) == 1 )
        {
            searchType = 5;
        }
        else if ( Integer.parseInt( paramBuilding ) == 0 && Integer.parseInt( paramKodate ) == 1 && Integer.parseInt( paramRentou ) == 1 )
        {
            searchType = 6;
        }
        else
            searchType = 0;

        // 各パラメタに一致するホテルを取得
        // 都道府県検索
        if ( prefId > 0 )
        {

            searchEngineBasic = new SearchEngineBasic_M2();
            searchEngineBasic.getPrefHotelList( new int[]{ prefId } );

            workIdList = searchEngineBasic.getHotelIdList();
            if ( workIdList != null )
            {
                for( k = 0 ; k < workIdList.length ; k++ )
                {
                    for( l = 0 ; l < workIdList[k].length ; l++ )
                    {
                        hotelIdList[count++] = workIdList[k][l];
                    }
                }
            }
            searchEngineBasic = null;
            findCount++;

        }

        // GPS
        if ( paramLatPos != null && paramLonPos != null )
        {

            if ( CheckString.numCheck( paramScale ) == false )
            {
                paramScale = "0";
            }
            if ( (paramLatPos.compareTo( "" ) != 0) && (paramLonPos.compareTo( "" ) != 0) )
            {
                searchHotelGps = new SearchHotelGps_M2();
                innerSearchIdList = searchHotelGps.getHotelIdList( paramLatPos, paramLonPos, Integer.parseInt( paramScale ) );

                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        shcommon = new SearchHotelCommon();
                        shcommon.setResultHotelList( hotelIdList );
                        shcommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                }
                qryString = qryString + "&lat=" + paramLatPos + "&lon=" + paramLonPos + "&scale=" + paramScale;
                findCount++;
                innerSearchIdList = null;
                searchHotelGps = null;
            }
        }

        // 料金検索（平日フリータイム）
        searchHotelPrice = new SearchHotelPrice_M2();
        if ( paramWeekFree != null )
        {
            priceKind = new int[1];
            priceKind[0] = 3;
            ret = searchHotelPrice.getHotelIdList( null, priceKind, 0, 999999, 0, 0 );
            if ( ret != false )
            {
                if ( findCount != 0 )
                {
                    // マージする
                    shcommon = new SearchHotelCommon();
                    shcommon.setResultHotelList( hotelIdList );
                    shcommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                    hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                }
                else
                {
                    hotelIdList = searchHotelPrice.getHotelIdList();
                }

                findCount++;
            }
            qryString = qryString + "&week_free=" + paramWeekFree;
        }
        // 料金検索（休日フリータイム）
        if ( paramHolidayFree != null )
        {
            priceKind = new int[1];
            priceKind[0] = 4;
            ret = searchHotelPrice.getHotelIdList( null, priceKind, 0, 999999, 0, 0 );
            if ( ret != false )
            {
                if ( findCount != 0 )
                {
                    // マージする
                    shcommon = new SearchHotelCommon();
                    shcommon.setResultHotelList( hotelIdList );
                    shcommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                    hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                }
                else
                {
                    hotelIdList = searchHotelPrice.getHotelIdList();
                }

                findCount++;
            }
            qryString = qryString + "&holiday_free=" + paramHolidayFree;
        }
        // 料金検索（２４時間休憩）
        if ( paramH24Rest != null )
        {
            priceKind = new int[2];
            priceKind[0] = 1;
            priceKind[1] = 2;
            ret = searchHotelPrice.getHotelIdListByTime( null, priceKind, 0, 2400, 0, 0 );
            if ( ret != false )
            {
                if ( findCount != 0 )
                {
                    // マージする
                    shcommon = new SearchHotelCommon();
                    shcommon.setResultHotelList( hotelIdList );
                    shcommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                    hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                }
                else
                {
                    hotelIdList = searchHotelPrice.getHotelIdList();
                }

                findCount++;
            }
            qryString = qryString + "&h24_rest=" + paramH24Rest;
        }

        if ( paramRestFrom != null && paramRestTo != null )
        {
            if ( CheckString.numCheck( paramRestFrom ) != false && CheckString.numCheck( paramRestTo ) != false )
            {
                if ( Integer.parseInt( paramRestFrom ) != 0 || Integer.parseInt( paramRestTo ) != 999999 )
                {
                    priceKind = new int[4];
                    priceKind[0] = 1;
                    priceKind[1] = 2;
                    priceKind[2] = 3;
                    priceKind[3] = 4;
                    ret = searchHotelPrice.getHotelIdList( null, priceKind, Integer.parseInt( paramRestFrom ), Integer.parseInt( paramRestTo ), 0, 0 );
                    if ( ret != false )
                    {
                        if ( findCount != 0 )
                        {
                            // マージする
                            shcommon = new SearchHotelCommon();
                            shcommon.setResultHotelList( hotelIdList );
                            shcommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                            hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                        }
                        else
                        {
                            hotelIdList = searchHotelPrice.getHotelIdList();
                        }

                        findCount++;
                    }
                }
                qryString = qryString + "&rest_from=" + paramRestFrom + "&rest_to=" + paramRestTo;

            }
        }
        // 料金検索
        if ( paramStayFrom != null && paramStayTo != null )
        {
            if ( CheckString.numCheck( paramStayFrom ) != false && CheckString.numCheck( paramStayTo ) != false )
            {
                if ( Integer.parseInt( paramStayFrom ) != 0 || Integer.parseInt( paramStayTo ) != 999999 )
                {
                    priceKind = new int[2];
                    priceKind[0] = 5;
                    priceKind[1] = 6;
                    ret = searchHotelPrice.getHotelIdList( null, priceKind, Integer.parseInt( paramStayFrom ), Integer.parseInt( paramStayTo ), 0, 0 );
                    if ( ret != false )
                    {
                        if ( findCount != 0 )
                        {
                            // マージする
                            shcommon = new SearchHotelCommon();
                            shcommon.setResultHotelList( hotelIdList );
                            shcommon.setPriceHotelList( searchHotelPrice.getHotelIdList() );
                            hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                        }
                        else
                        {
                            hotelIdList = searchHotelPrice.getHotelIdList();
                        }

                        findCount++;
                    }
                }
                qryString = qryString + "&stay_from=" + paramStayFrom + "&stay_to=" + paramStayTo;
            }
        }

        searchHotelPrice = null;

        // サービス・空室検索
        serviceCount = 0;

        if ( paramCredit != null )
        {
            serviceKind[0] = 1;
            serviceCount++;

            qryString = qryString + "&credit=" + paramCredit;
        }
        if ( paramParking != null )
        {
            serviceKind[1] = 1;
            serviceCount++;

            qryString = qryString + "&parking=" + paramParking;
        }
        if ( paramCoupon != null )
        {
            serviceKind[2] = 1;
            serviceCount++;

            qryString = qryString + "&coupon=" + paramCoupon;
        }
        if ( paramHalfway != null )
        {
            serviceKind[3] = 1;
            serviceCount++;

            qryString = qryString + "&halfway=" + paramHalfway;
        }
        if ( paramReserve != null )
        {
            serviceKind[4] = 1;
            serviceCount++;

            qryString = qryString + "&reserve=" + paramReserve;
        }
        if ( paramOne != null )
        {
            serviceKind[5] = 1;
            serviceCount++;

            qryString = qryString + "&one=" + paramOne;
        }
        if ( paramThree != null )
        {
            serviceKind[6] = 1;
            serviceCount++;

            qryString = qryString + "&three=" + paramThree;
        }
        if ( paramRoomService != null )
        {
            serviceKind[7] = 1;
            serviceCount++;

            qryString = qryString + "&roomservice=" + paramRoomService;
        }
        if ( paramPayAuto != null )
        {
            serviceKind[8] = 1;
            serviceCount++;

            qryString = qryString + "&payauto=" + paramPayAuto;
        }
        if ( paramEmpty != null )
        {
            serviceKind[9] = 1;
            serviceCount++;

            qryString = qryString + "&empty=" + paramEmpty;
        }
        if ( serviceCount > 0 )
        {
            searchHotelService = new SearchHotelService_M2();
            ret = searchHotelService.getHotelIdList( serviceKind, 0, 0, 1 );
            if ( ret != false )
            {
                if ( findCount != 0 )
                {
                    // マージする
                    shcommon = new SearchHotelCommon();
                    shcommon.setResultHotelList( hotelIdList );
                    shcommon.setEquipHotelList( searchHotelService.getHotelIdList() );
                    hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                }
                else
                {
                    hotelIdList = searchHotelService.getHotelIdList();
                }

                findCount++;
            }
            searchHotelService = null;
        }
        // 設備
        if ( paramAmenity != null )
        {
            if ( paramAmenity.length > 0 )
            {
                equipList = new int[paramAmenity.length];

                for( j = 0 ; j < paramAmenity.length ; j++ )
                {
                    if ( CheckString.numCheck( paramAmenity[j] ) != false )
                    {
                        equipList[j] = Integer.parseInt( paramAmenity[j] );
                    }
                    qryString = qryString + "&amenity=" + paramAmenity[j];
                }

                searchHotelEquip = new SearchHotelEquip_M2();
                ret = searchHotelEquip.getHotelIdList( equipList, 0, 0 );
                if ( ret != false )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        shcommon = new SearchHotelCommon();
                        shcommon.setResultHotelList( hotelIdList );
                        shcommon.setEquipHotelList( searchHotelEquip.getHotelIdList() );
                        hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = searchHotelEquip.getHotelIdList();
                    }
                }
                searchHotelEquip = null;
                findCount++;
            }
        }
        // 市区町村
        if ( paramJisCode != null )
        {
            if ( CheckString.numCheck( paramJisCode ) != false )
            {
                searchHotelCity = new SearchHotelCity_M2();
                innerSearchIdList = searchHotelCity.getHotelIdList( Integer.parseInt( paramJisCode ) );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        shcommon = new SearchHotelCommon();
                        shcommon.setResultHotelList( hotelIdList );
                        shcommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&jis_code=" + paramJisCode;
                searchHotelCity = null;
            }
        }
        // フリーワード
        if ( paramFreeword != null )
        {
            if ( paramFreeword.compareTo( "" ) != 0 )
            {
                try
                {
                    paramFreeword = new String( paramFreeword.getBytes( "8859_1" ), "Windows-31J" );
                }
                catch ( UnsupportedEncodingException e )
                {
                    Logging.error( "[SearchKodawari.searchHotels() ] Exception=" + e.toString() );
                }

                searchHotelFreeword = new SearchHotelFreeword_M2();
                innerSearchIdList = searchHotelFreeword.getSearchIdList( paramFreeword );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        shcommon = new SearchHotelCommon();
                        shcommon.setResultHotelList( hotelIdList );
                        shcommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&freeword=" + paramFreeword;
                searchHotelFreeword = null;
            }
        }
        // ホテル街
        if ( paramAreaId != null )
        {
            if ( CheckString.numCheck( paramAreaId ) != false )
            {
                searchHotelArea = new SearchHotelArea_M2();
                innerSearchIdList = searchHotelArea.getSearchIdList( Integer.parseInt( paramAreaId ) );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        shcommon = new SearchHotelCommon();
                        shcommon.setResultHotelList( hotelIdList );
                        shcommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&area_id=" + paramAreaId;
                searchHotelArea = null;
            }
        }
        // 駅
        if ( paramStationId != null )
        {
            if ( paramStationId.compareTo( "" ) != 0 )
            {
                searchHotelStation = new SearchHotelStation_M2();
                innerSearchIdList = searchHotelStation.getHotelIdList( paramStationId );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        shcommon = new SearchHotelCommon();
                        shcommon.setResultHotelList( hotelIdList );
                        shcommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&station_id=" + paramStationId;
                searchHotelStation = null;
            }
        }
        // IC
        if ( paramIcId != null )
        {
            if ( paramIcId.compareTo( "" ) != 0 )
            {
                searchHotelIc = new SearchHotelIc_M2();
                innerSearchIdList = searchHotelIc.getSearchIdList( paramAreaId );
                if ( innerSearchIdList != null )
                {
                    if ( findCount != 0 )
                    {
                        // マージする
                        shcommon = new SearchHotelCommon();
                        shcommon.setResultHotelList( hotelIdList );
                        shcommon.setEquipHotelList( innerSearchIdList );
                        hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                    }
                    else
                    {
                        hotelIdList = innerSearchIdList;
                    }
                    innerSearchIdList = null;
                    findCount++;
                }
                qryString = qryString + "&ic_id=" + paramIcId;
                searchHotelIc = null;
            }
        }

        // 部屋画像検索
        if ( paramRoom != null )
        {
            searchHotelRoom = new SearchHotelRoom();
            if ( findCount > 0 )
            {
                ret = searchHotelRoom.getHotelList( hotelIdList, 0, 0 );
                if ( ret != false )
                {
                    // マージする
                    shcommon = new SearchHotelCommon();
                    shcommon.setResultHotelList( hotelIdList );
                    shcommon.setEquipHotelList( searchHotelRoom.getHotelIdList() );
                    hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                }
            }
            else
            {
                ret = searchHotelRoom.getHotelList( null, 0, 0 );
                if ( ret != false )
                {
                    hotelIdList = searchHotelRoom.getHotelIdList();
                }
            }
            findCount++;
            qryString = qryString + "&room=" + paramRoom;

            searchHotelRoom = null;

        }

        // 建物形式別
        if ( searchType > 0 && searchType < 8 )
        {
            searchHotelBuilding = new SearchHotelBuilding();
            if ( findCount > 0 )
            {
                ret = searchHotelBuilding.getHotelList( hotelIdList, searchType, 0, 0 );
                if ( ret != false )
                {
                    // マージする
                    shcommon = new SearchHotelCommon();
                    shcommon.setResultHotelList( hotelIdList );
                    shcommon.setEquipHotelList( searchHotelBuilding.getHotelIdList() );
                    hotelIdList = shcommon.getMargeHotel( 0, 0, false );
                }
            }
            else
            {
                ret = searchHotelBuilding.getHotelList( null, searchType, 0, 0 );
                if ( ret != false )
                {
                    hotelIdList = searchHotelBuilding.getHotelIdList();
                }
            }
            findCount++;
            searchHotelBuilding = null;
        }

        // クチコミ検索
        if ( paramKuchikomi != null )
        {
            if ( paramPoint == null )
                paramPoint = "0";
            if ( paramCleanness == null )
                paramCleanness = "0";
            if ( paramWidth == null )
                paramWidth = "0";
            if ( paramEquip == null )
                paramEquip = "0";
            if ( paramService == null )
                paramService = "0";
            if ( paramCost == null )
                paramCost = "0";

            if ( Integer.parseInt( paramKuchikomi ) > 0 || Integer.parseInt( paramPoint ) > 0 || Integer.parseInt( paramCleanness ) > 0 || Integer.parseInt( paramWidth ) > 0
                    || Integer.parseInt( paramEquip ) > 0 || Integer.parseInt( paramService ) > 0 || Integer.parseInt( paramCost ) > 0 )
            {
                boolSort = true;
                // if( paramSort == null ) paramSort = "1";

                searchHotelBbs = new SearchHotelBbs_M2();
                if ( findCount > 0 )
                {
                    // hotelIdListのみを取得する
                    ret = searchHotelBbs.getHotelIdListOnly( hotelIdList, Integer.parseInt( paramKuchikomi ), Integer.parseInt( paramPoint ), Integer.parseInt( paramCleanness ),
                            Integer.parseInt( paramWidth ), Integer.parseInt( paramService ), Integer.parseInt( paramEquip ), Integer.parseInt( paramCost ), prefId );

                    if ( ret != false )
                    {
                        // hotelIdList = null;
                        hotelIdList = searchHotelBbs.getHotelIdList();
                    }
                }
                else
                {
                    // hotelIdListのみを取得する
                    ret = searchHotelBbs.getHotelIdListOnly( null, Integer.parseInt( paramKuchikomi ), Integer.parseInt( paramPoint ), Integer.parseInt( paramCleanness ),
                            Integer.parseInt( paramWidth ), Integer.parseInt( paramService ), Integer.parseInt( paramEquip ), Integer.parseInt( paramCost ), prefId );
                    if ( ret != false )
                    {
                        // hotelIdList = null;
                        hotelIdList = searchHotelBbs.getHotelIdList();
                    }
                }

                qryString = qryString + "&kuchikomi=" + paramKuchikomi;
                qryString = qryString + "&point=" + paramPoint;
                qryString = qryString + "&cleanness=" + paramCleanness;
                qryString = qryString + "&width=" + paramWidth;
                qryString = qryString + "&service=" + paramService;
                qryString = qryString + "&equip=" + paramEquip;
                qryString = qryString + "&cost=" + paramCost;

                searchHotelBbs = null;
            }
        }

        count = 0;
        hotelAllCount = 0;

        // マージされていない場合は0以外が入っていないかを確認する
        if ( hotelIdList.length == 10000 )
        {
            // PV順に並び替えるときに使用する
            if ( paramSort == null )
            {
                paramSort = "0";
            }
            if ( paramSort.compareTo( "0" ) == 0 )
            {
                shcommon = new SearchHotelCommon();
                hotelIdList = shcommon.sortHotelRank( hotelIdList );
            }

            for( j = 0 ; j < hotelIdList.length ; j++ )
            {
                if ( hotelIdList[j] == 0 )
                {
                    break;
                }
                hotelAllCount++;
            }
        }
        else
        {
            hotelAllCount = hotelIdList.length;
        }

        String sortparams[] = { paramSort, "" + boolSort };
        this.setSortParams( sortparams );
        this.m_hotelAllCount = hotelAllCount;
        // qryString = "kind=4&" + qryString;
        this.m_queryString = qryString;

    }
}
