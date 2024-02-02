package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.net.URLCodec;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
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
 * 住所検索クラス
 * 
 * @author HCL Technologies Ltd.
 * @version 1.0 2008/08/18
 */

public class ActionAreaSearch_M2 extends BaseAction
{

    private RequestDispatcher requestDispatcher;
    private final boolean     DISP_PC             = false;
    private int               DISP_COUNT          = 2;

    static int                pageRecords         = Constants.pageLimitRecords;
    static int                maxRecords          = Constants.maxRecords;
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2;

    /**
     * 住所検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→search_area_01_M2.jsp<br>
     *      都道府県ID→search_area_02_M2.jsp<br>
     *      市区町村ID→search_result_area_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {

        int hotelCount = 0;
        int hotelAllCount = 0;
        int pageNum = 0;
        int jisCode = 0;
        int prefId = 0;
        int masterEquipCount = 0;
        int[] arrHotelIdList = null;
        String paramPrefId = null;
        String paramJisCode = null;
        String paramPage = null;
        String prefName = null;
        String cityName = null;
        String queryString = null;
        String currentPageRecords = null;
        String pageLinks = "";
        DataSearchArea_M2 dataSearchAreaDTO = null;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchMasterEquip_M2[] arrDataSearchMasterEquip = null;
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

            if ( !CheckString.numCheck( paramPage ) )
            {
                paramPage = "0";
            }
            // 都道府県コードのデータのみが正しいとき
            if ( CheckString.numCheck( paramPrefId ) && paramJisCode == null )
            {
                dataSearchAreaDTO = new DataSearchArea_M2();
                int prefID = Integer.parseInt( paramPrefId );
                // 都道府県IDから、市区町村データを取得
                getSearchCity( prefID, dataSearchAreaDTO );
                // 都道府県IDから、ホテルエリアデータを取得
                getSearchHotelArea( prefID, dataSearchAreaDTO );
                // 都道府県IDから、広告データを取得
                getSearchSponserDisp( prefID, dataSearchAreaDTO );
                getSearchRandomSponserDisp( prefID, dataSearchAreaDTO );

                request.setAttribute( "SEARCH-RESULT", dataSearchAreaDTO );
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_area_02_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_area_02_M2.jsp" );
                }
                requestDispatcher.forward( request, response );

            }
            else if ( CheckString.numCheck( paramJisCode ) )
            {
                try
                {
                    searchEngineBasic = new SearchEngineBasic_M2();
                    boolean isEquipListFound = false;
                    // ページ左のデータ (こだわり検索の設備)
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

                    // 件の情報を取得するため、市の情報をセットする
                    searchArea.getCityDetail( jisCode );
                    prefName = searchArea.getPrefName();
                    prefId = searchArea.getPrefId();
                    cityName = searchArea.getCityName();

                    // スポンサー情報の取得
                    try
                    {
                        getSearchResultSponser( jisCode, prefId, dataSearchResult );
                        Logging.info( "[ActionAreaSearch_M2.getSearchResultSponser()] getSearchResultSponser=" + dataSearchResult.getSponsorData().getSponsorCount() );
                    }
                    catch ( Exception exception )
                    {
                        Logging.error( "[ActionAreaSearch_M2.getSearchResultSponser()]" + exception.toString() +
                                " 下記のスポンサー広告を追加してください。 pref_id = " + prefId );
                    }
                    // ローテーションバナー広告の取得
                    try
                    {
                        getSearchResultRandomSponser( prefId, dataSearchResult );
                    }
                    catch ( Exception exception )
                    {
                        Logging.error( "[ActionAreaSearch_M2.getSearchResultRandomSponser()]" + exception.toString() +
                                " 下記のローーションバナー広告を追加してください。 pref_id = " + prefId );
                    }

                    // JisCodeからホテルリストを取得
                    arrHotelIdList = searchArea.getSearchIdList( jisCode );

                    if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                    {
                        // ホテルの詳細を取得

                        searchHotelDao = new SearchHotelDao_M2();
                        searchHotelDao.getHotelList( arrHotelIdList, pageRecords,
                                pageNum );

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
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_area_M2.jsp" );

                    }
                    else
                    {
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_area_M2.jsp" );
                    }
                    requestDispatcher.forward( request, response );

                }
                catch ( Exception exception )
                {
                    Logging.error(
                            "[ActionAreaSearch.execute() ][paramJisCode = "
                                    + paramJisCode + "] Exception="
                                    + exception.toString(), exception );
                    try
                    {
                        dataSearchResult = new DataSearchResult_M2();
                        dataSearchResult.setErrorOccurring( true );
                        dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );

                        request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                        if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                        {
                            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_area_M2.jsp" );
                        }
                        else
                        {
                            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_area_M2.jsp" );
                        }
                        requestDispatcher.forward( request, response );
                    }
                    catch ( Exception exception1 )
                    {
                        Logging
                                .error( "unable to dispatch.....="
                                        + exception1.toString() );
                    }
                }
                finally
                {
                    searchArea = null;
                    arrDataSearchHotel = null;
                    dataSearchResult = null;
                    searchEngineBasic = null;
                    searchHotelDao = null;
                }
            }
            else
            {
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_area_01_M2.jsp" );

                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_area_01_M2.jsp" );
                }
                requestDispatcher.forward( request, response );
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionAreaSearch_M2.execute()] Exception", exception );
        }
        finally
        {
            dataSearchAreaDTO = null;
        }
    }

    /**
     * 市区町村データ取得（都道府県IDで取得）
     * 
     * @param prefId 都道府県ID
     * @param dataSearchAreaDTO DataSearchArea_M2クラス
     * @throws Exception
     */
    public void getSearchCity(int prefId, DataSearchArea_M2 dataSearchAreaDTO)
            throws Exception
    {
        SearchArea_M2 searchArea = null;
        DataHotelCity_M2[] arrDataHotelCity = null;
        try
        {
            searchArea = new SearchArea_M2();
            arrDataHotelCity = searchArea.getCityListByPref( prefId );

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
            Logging.error( "[ActionAreaSearch_M2.getSearchCity(" + prefId + ","
                    + dataSearchAreaDTO + ")] " + exception.toString() );
            throw exception;
        }
        finally
        {
            searchArea = null;
            arrDataHotelCity = null;

        }
    }

    /**
     * ホテルエリア情報取得（都道府県IDで取得）
     * 
     * @param prefId 都道府県ID
     * @param dataSearchAreaDTO DataSearchArea_M2クラス
     * @throws Exception
     */
    public void getSearchHotelArea(int prefId,
            DataSearchArea_M2 dataSearchAreaDTO) throws Exception
    {

        SearchHotelArea_M2 searchHotelArea = null;
        DataHotelArea_M2[] arrDataHotelArea = null;
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
            Logging.error( "[ActionAreaSearch_M2.getSearchHotelArea(" + prefId
                    + "," + dataSearchAreaDTO + ")] " + exception.toString() );
            throw exception;
        }
        finally
        {
            searchHotelArea = null;
            arrDataHotelArea = null;
        }
    }

    /**
     * スポンサー広告取得
     * 
     * @param prefId 都道府県ID
     * @param dataSearchAreaDTO DataSearchArea_M2クラス
     * @throws Exception
     */
    public void getSearchSponserDisp(int prefId,
            DataSearchArea_M2 dataSearchAreaDTO) throws Exception
    {
        SponsorData_M2 sponsorData = null;
        boolean isSponsorPrefFound = false;
        try
        {

            sponsorData = new SponsorData_M2();

            // スポンサー広告のデータを取得
            isSponsorPrefFound = sponsorData.getSponsorByPref( prefId );
            dataSearchAreaDTO.setSponserDisplayResult( isSponsorPrefFound );
            if ( isSponsorPrefFound )
            {
                dataSearchAreaDTO.setSponsorData( sponsorData );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionAreaSearch_M2.getSearchSponserDisp(" + prefId
                    + "," + dataSearchAreaDTO + ")] " + exception.toString() );
            throw exception;
        }
        finally
        {
            sponsorData = null;
        }
    }

    /**
     * ローテーションバナー取得
     * 
     * @param prefId 都道府県ID
     * @param dataSearchAreaDTO DataSearchArea_M2クラス
     * @throws Exception
     */
    public void getSearchRandomSponserDisp(int prefId,
            DataSearchArea_M2 dataSearchAreaDTO) throws Exception
    {
        SponsorData_M2 sponsorData = null;
        boolean isSponsorPrefFound = false;
        try
        {

            sponsorData = new SponsorData_M2();

            // ローテーションバナーのデータを取得(上限の2件を取得する)
            isSponsorPrefFound = sponsorData.getRandomSponsorByPref( prefId, DISP_COUNT, DISP_PC );
            dataSearchAreaDTO.setRandomSponsorDisplayResult( isSponsorPrefFound );
            if ( isSponsorPrefFound )
            {
                dataSearchAreaDTO.setRandomSponsorData( sponsorData );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionAreaSearch_M2.getSearchSponserDisp(" + prefId
                    + "," + dataSearchAreaDTO + ")] " + exception.toString() );
            throw exception;
        }
        finally
        {
            sponsorData = null;
        }
    }

    /**
     * スポンサー広告取得（市区町村コードまたは、都道府県コードから取得）
     * 
     * @param jisCode 市区町村ID
     * @param prefId 都道府県
     * @throws Exception
     */
    public void getSearchResultSponser(int jisCode, int prefId,
            DataSearchResult_M2 dataSearchResult) throws Exception
    {
        SponsorData_M2 sponsorData = null;
        boolean isSponsorPrefFound = false;
        try
        {
            sponsorData = new SponsorData_M2();
            // 市区町村からスポンサー広告を取得
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
            Logging.info( "[ActionAreaSearch_M2.getSearchResultSponser()] count=" + sponsorData.getSponsorCount() );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionAreaSearch_M2.getSearchResultSponser("
                    + jisCode + "," + prefId + "," + dataSearchResult + ")] "
                    + exception.toString() );
            throw exception;
        }
        finally
        {
            sponsorData = null;
        }
    }

    /**
     * ローテーションバナー取得（市区町村コードまたは、都道府県コードから取得）
     * 
     * @param prefId 都道府県ID
     * @param dataSearchResult DataSearchResult_M2クラス
     * @throws Exception
     */
    public void getSearchResultRandomSponser(int prefId,
            DataSearchResult_M2 dataSearchResult) throws Exception
    {
        SponsorData_M2 sponsorData = null;
        boolean isSponsorPrefFound = false;
        try
        {
            sponsorData = new SponsorData_M2();
            // ローテーションバナーのデータを取得(上限の2件を取得する)
            isSponsorPrefFound = sponsorData.getRandomSponsorByPref( prefId, DISP_COUNT, DISP_PC );
            dataSearchResult.setRandomSponsorDataStatus( isSponsorPrefFound );
            if ( isSponsorPrefFound )
            {
                dataSearchResult.setRandomSponsorData( sponsorData );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionAreaSearch_M2.getSearchResultSponser("
                    + prefId + "," + dataSearchResult + ")] " + exception.toString() );
            throw exception;
        }
        finally
        {
            sponsorData = null;
        }
    }
}
