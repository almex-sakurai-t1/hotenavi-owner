package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.net.URLCodec;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelArea_M2;
import jp.happyhotel.data.DataHotelCity_M2;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataSearchArea_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchMasterEquip_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchArea_M2;
import jp.happyhotel.search.SearchEngineBasic_M2;
import jp.happyhotel.search.SearchHotelArea_M2;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.sponsor.SponsorData_M2;

/**
 * ハピホテマイル加盟店検索クラス
 * 
 * @author tashiro-s1
 */
public class ActionHappieSearch extends BaseAction
{
    private RequestDispatcher requestDispatcher;
    private final int         DISP_PC             = 0;
    private int               DISP_COUNT          = 2;
    private static final int  HAPPIE_MEMBER_HOTEL = 0;
    static int                pageRecords         = 20;
    static int                maxRecords          = 200;
    static String             recordsNotFoundMsg2 = "現在、表示するハピホテマイル加盟店情報がありません。";

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int hotelCount = 0;
        int hotelAllCount = 0;
        int pageNum = 0;
        int jisCode = 0;
        int prefId = 0;
        int masterEquipCount = 0;
        int[] arrHotelIdList = (int[])null;
        String paramPrefId = null;
        String paramJisCode = null;
        String paramPage = null;
        String prefName = null;
        String cityName = null;
        String queryString = null;
        String currentPageRecords = null;
        String pageLinks = "";
        DataSearchArea_M2 dataSearchAreaDTO = null;
        DataSearchHotel_M2[] arrDataSearchHotel = (DataSearchHotel_M2[])null;
        DataSearchMasterEquip_M2[] arrDataSearchMasterEquip = (DataSearchMasterEquip_M2[])null;
        SearchEngineBasic_M2 searchEngineBasic = null;
        DataSearchResult_M2 dataSearchResult = null;
        SearchArea_M2 searchArea = null;
        SearchHotelDao_M2 searchHotelDao = null;
        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 )
            {
                response.sendRedirect( requestURL.replace( "http://", "https://" ) + (request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" )) );
                return;
            }
            paramPrefId = request.getParameter( "pref_id" );
            paramJisCode = request.getParameter( "jis_code" );
            paramPage = request.getParameter( "page" );

            // スマホからのアクセスならスマホ用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType != UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getSmartUrl( request ) );
                return;
            }

            if ( !(CheckString.numCheck( paramPage )) )
            {
                paramPage = "0";
            }

            if ( (CheckString.numCheck( paramPrefId )) && (paramJisCode == null) )
            {
                dataSearchAreaDTO = new DataSearchArea_M2();
                int prefID = Integer.parseInt( paramPrefId );

                getSearchCity( prefID, dataSearchAreaDTO );

                // getSearchHotelArea( prefID, dataSearchAreaDTO );
                // getSearchSponserDisp( prefID, dataSearchAreaDTO );
                // getSearchRandomSponserDisp( prefID, dataSearchAreaDTO );

                request.setAttribute( "SEARCH-RESULT", dataSearchAreaDTO );
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    this.requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_happie_02_M2.jsp" );
                }
                else
                {
                    this.requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_happie_02_M2.jsp" );
                }
            }
            else if ( CheckString.numCheck( paramJisCode ) )
            {
                try
                {
                    searchEngineBasic = new SearchEngineBasic_M2();
                    boolean isEquipListFound = false;

                    isEquipListFound = searchEngineBasic.getEquipList( false );

                    if ( isEquipListFound )
                    {
                        masterEquipCount = searchEngineBasic.getMasterEquipCount();
                        arrDataSearchMasterEquip = searchEngineBasic.getMasterEquip();
                    }

                    searchArea = new SearchArea_M2();
                    dataSearchResult = new DataSearchResult_M2();
                    pageNum = Integer.parseInt( paramPage );
                    jisCode = Integer.parseInt( paramJisCode );

                    searchArea.getCityDetail( jisCode );
                    prefName = searchArea.getPrefName();
                    prefId = searchArea.getPrefId();
                    cityName = searchArea.getCityName();
                    try
                    {
                        getSearchResultSponser( jisCode, prefId, dataSearchResult );
                        Logging.info( "[ActionHappieSearch.getSearchResultSponser()] getSearchResultSponser=" + dataSearchResult.getSponsorData().getSponsorCount() );
                    }
                    catch ( Exception exception )
                    {
                        Logging.error( "[ActionHappieSearch.getSearchResultSponser()]" + exception.toString() +
                                " 下記のスポンサー広告を追加してください。 pref_id = " + prefId );
                    }

                    try
                    {
                        getSearchResultRandomSponser( prefId, dataSearchResult );
                    }
                    catch ( Exception exception )
                    {
                        Logging.error( "[ActionHappieSearch.getSearchResultRandomSponser()]" + exception.toString() +
                                " 下記のローーションバナー広告を追加してください。 pref_id = " + prefId );
                    }

                    arrHotelIdList = searchArea.getSearchHappieIdList( jisCode, 0 );

                    if ( (arrHotelIdList != null) && (arrHotelIdList.length > 0) )
                    {
                        searchHotelDao = new SearchHotelDao_M2();
                        searchHotelDao.getHotelList( arrHotelIdList, pageRecords, pageNum );

                        arrDataSearchHotel = searchHotelDao.getHotelInfo();
                        hotelCount = searchHotelDao.getCount();
                        hotelAllCount = searchHotelDao.getAllCount();

                        queryString = "searchArea.act?jis_code=" + paramJisCode;
                        currentPageRecords = PagingDetails.getPageRecords( pageNum, pageRecords, hotelAllCount, hotelCount );

                        if ( hotelAllCount > pageRecords )
                        {
                            pageLinks = PagingDetails.getPagenationLink( pageNum, pageRecords, hotelAllCount, queryString );

                            dataSearchResult.setPageLink( pageLinks );
                        }
                        dataSearchResult.setRecordsOnPage( currentPageRecords );
                        dataSearchResult.setHotelCount( hotelCount );
                        dataSearchResult.setHotelAllCount( hotelAllCount );
                        dataSearchResult.setDataSearchHotel( arrDataSearchHotel );
                    }
                    else
                    {
                        dataSearchResult.setErrorOccurring( true );
                        dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                    }
                    dataSearchResult.setMasterEquipCount( masterEquipCount );
                    dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
                    dataSearchResult.setParamParameter1( Integer.toString( prefId ) );
                    dataSearchResult.setParamParameter2( prefName );
                    dataSearchResult.setParamParameter3( cityName );
                    dataSearchResult.setJisCode( paramJisCode );

                    request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                    if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                    {
                        this.requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_happie_M2.jsp" );
                    }
                    else
                    {
                        this.requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_happie_M2.jsp" );
                    }
                }
                catch ( Exception exception )
                {
                    Logging.error(
                            "[ActionAreaSearch.execute() ][paramJisCode = " +
                                    paramJisCode + "] Exception=" +
                                    exception.toString(), exception );
                    try
                    {
                        dataSearchResult = new DataSearchResult_M2();
                        dataSearchResult.setErrorOccurring( true );
                        dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );

                        request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                        if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                        {
                            this.requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_happie_M2.jsp" );
                        }
                        else
                        {
                            this.requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_happie_M2.jsp" );
                        }
                        this.requestDispatcher.forward( request, response );
                    }
                    catch ( Exception exception1 )
                    {
                        Logging.error( "unable to dispatch.....=" +
                                exception1.toString() );
                    }
                }
                finally
                {
                    searchArea = null;
                    arrDataSearchHotel = (DataSearchHotel_M2[])null;
                    dataSearchResult = null;
                    searchEngineBasic = null;
                    searchHotelDao = null;
                }
            }
            else
            {
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    this.requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_happie_01_M2.jsp" );
                }
                else
                {
                    this.requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_happie_01_M2.jsp" );
                }
            }
            this.requestDispatcher.forward( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionHappieSearch.execute()] Exception", exception );
        }
        finally
        {
            dataSearchAreaDTO = null;
        }
    }

    public void getSearchCity(int prefId, DataSearchArea_M2 dataSearchAreaDTO)
            throws Exception
    {
        SearchArea_M2 searchArea = null;
        DataHotelCity_M2[] arrDataHotelCity = (DataHotelCity_M2[])null;
        try
        {
            searchArea = new SearchArea_M2();

            arrDataHotelCity = searchArea.getCityListHappieByPref( prefId );

            if ( arrDataHotelCity != null )
            {
                dataSearchAreaDTO.setDataHotelCity( arrDataHotelCity );
                dataSearchAreaDTO.setPrefName( arrDataHotelCity[0].getPrefName() );
                dataSearchAreaDTO.setCityCount( searchArea.getCityCount() );
            }
            else
            {
                dataSearchAreaDTO.setPrefName( searchArea.getPrefInfo( prefId ) );
                dataSearchAreaDTO.setCityCount( 0 );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionHappieSearch.getSearchCity(" + prefId + "," +
                    dataSearchAreaDTO + ")] " + exception.toString() );
            throw exception;
        }
        finally
        {
            searchArea = null;
            arrDataHotelCity = (DataHotelCity_M2[])null;
        }
    }

    public void getSearchHotelArea(int prefId, DataSearchArea_M2 dataSearchAreaDTO)
            throws Exception
    {
        SearchHotelArea_M2 searchHotelArea = null;
        DataHotelArea_M2[] arrDataHotelArea = (DataHotelArea_M2[])null;
        try
        {
            searchHotelArea = new SearchHotelArea_M2();
            arrDataHotelArea = searchHotelArea.getAreaListByPref( prefId );

            if ( arrDataHotelArea != null )
            {
                dataSearchAreaDTO.setDataHotelArea( arrDataHotelArea );
                dataSearchAreaDTO.setHotelAreaCount( searchHotelArea.getAreaCount() );
            }
            else
            {

                dataSearchAreaDTO.setPrefName( searchHotelArea.getPrefInfo( prefId ) );
                dataSearchAreaDTO.setHotelAreaCount( 0 );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionHappieSearch.getSearchHotelArea(" + prefId +
                    "," + dataSearchAreaDTO + ")] " + exception.toString() );
            throw exception;
        }
        finally
        {
            searchHotelArea = null;
            arrDataHotelArea = (DataHotelArea_M2[])null;
        }
    }

    public void getSearchSponserDisp(int prefId, DataSearchArea_M2 dataSearchAreaDTO)
            throws Exception
    {
        SponsorData_M2 sponsorData = null;
        boolean isSponsorPrefFound = false;
        try
        {
            sponsorData = new SponsorData_M2();

            isSponsorPrefFound = sponsorData.getSponsorByPref( prefId );
            dataSearchAreaDTO.setSponserDisplayResult( isSponsorPrefFound );
            if ( !(isSponsorPrefFound) )
            {
                dataSearchAreaDTO.setSponsorData( sponsorData );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionHappieSearch.getSearchSponserDisp(" + prefId +
                    "," + dataSearchAreaDTO + ")] " + exception.toString() );
            throw exception;
        }
        finally
        {
            sponsorData = null;
        }
    }

    public void getSearchRandomSponserDisp(int prefId, DataSearchArea_M2 dataSearchAreaDTO)
            throws Exception
    {
        SponsorData_M2 sponsorData = null;
        boolean isSponsorPrefFound = false;
        try
        {
            sponsorData = new SponsorData_M2();

            isSponsorPrefFound = sponsorData.getRandomSponsorByPref( prefId, this.DISP_COUNT, false );
            dataSearchAreaDTO.setRandomSponsorDisplayResult( isSponsorPrefFound );
            if ( !(isSponsorPrefFound) )
                dataSearchAreaDTO.setRandomSponsorData( sponsorData );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionHappieSearch.getSearchSponserDisp(" + prefId +
                    "," + dataSearchAreaDTO + ")] " + exception.toString() );
            throw exception;
        }
        finally
        {
            sponsorData = null;
        }
    }

    public void getSearchResultSponser(int jisCode, int prefId, DataSearchResult_M2 dataSearchResult)
            throws Exception
    {
        SponsorData_M2 sponsorData = null;
        boolean isSponsorPrefFound = false;
        try
        {
            sponsorData = new SponsorData_M2();

            sponsorData.getSponsorByCity( jisCode );
            if ( sponsorData.getSponsorCount() == 0 )
            {
                isSponsorPrefFound = sponsorData.getSponsorByPref( prefId );
            }
            dataSearchResult.setSponsorDataStatus( isSponsorPrefFound );
            if ( isSponsorPrefFound )
            {
                dataSearchResult.setSponsorData( sponsorData );
            }
            Logging.info( "[ActionHappieSearch.getSearchResultSponser()] count=" + sponsorData.getSponsorCount() );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionHappieSearch.getSearchResultSponser(" +
                    jisCode + "," + prefId + "," + dataSearchResult + ")] " +
                    exception.toString() );
            throw exception;
        }
        finally
        {
            sponsorData = null;
        }
    }

    public void getSearchResultRandomSponser(int prefId, DataSearchResult_M2 dataSearchResult)
            throws Exception
    {
        SponsorData_M2 sponsorData = null;
        boolean isSponsorPrefFound = false;
        try
        {
            sponsorData = new SponsorData_M2();

            isSponsorPrefFound = sponsorData.getRandomSponsorByPref( prefId, this.DISP_COUNT, false );
            dataSearchResult.setRandomSponsorDataStatus( isSponsorPrefFound );
            if ( isSponsorPrefFound )
            {
                dataSearchResult.setRandomSponsorData( sponsorData );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionHappieSearch.getSearchResultSponser(" +
                    prefId + "," + dataSearchResult + ")] " + exception.toString() );
            throw exception;
        }
        finally
        {
            sponsorData = null;
        }
    }
}
