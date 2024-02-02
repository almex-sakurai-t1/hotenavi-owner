package jp.happyhotel.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelCity_M2;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataSearchArea_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchArea_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelFreeword_M2;

/**
 * ハピホテマイル加盟店検索クラス（携帯）
 * 
 * @author tashiro-s1
 */
public class ActionHappieSearchMobile extends BaseAction
{
    private static final int  HAPPIE_MEMBER_HOTEL = 0;
    static int                pageRecords         = 10;
    static int                maxRecords          = 50;
    static String             recordNotFound2     = "現在、表示するハピホテマイル加盟店情報がありません。";
    public static final int   dispFormat          = 1;
    private RequestDispatcher requestDispatcher   = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int pageNum = 0;

        int[] arrHotelIdList = (int[])null;

        String paramJisCode = null;
        String paramPrefId = null;
        String paramLocalId = null;

        String pageLinks = "";
        String paramUidLink = null;
        String pageHeader = null;

        DataSearchHotel_M2[] arrDataSearchHotel = (DataSearchHotel_M2[])null;
        DataSearchResult_M2 dataSearchResult = null;
        DataSearchArea_M2 dataSearchAreaDTO = null;
        SearchHotelCommon shcom = null;
        SearchHotelFreeword_M2 searchHotelFreeWord = null;
        SearchHotelDao_M2 searchHotelDao = null;
        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 && UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_SMARTPHONE )
            {
                response.sendRedirect( requestURL.replace( "http://", "https://" ) + (request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" )) );
                return;
            }
            paramPrefId = request.getParameter( "pref_id" );
            paramLocalId = request.getParameter( "local_id" );
            paramJisCode = request.getParameter( "jis_code" );
            String paramPage = request.getParameter( "page" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            String paramAndWord = request.getParameter( "andword" );

            SearchArea_M2 searchArea = new SearchArea_M2();

            if ( !(CheckString.numCheck( paramPage )) )
            {
                paramPage = "0";
            }

            // PCからのアクセスならPC用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType == UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getPCUrl( request ) );
                return;
            }

            if ( (CheckString.numCheck( paramJisCode )) && (CheckString.numCheck( paramPage )) )
            {
                pageNum = Integer.parseInt( paramPage );
                int jisCode = Integer.parseInt( paramJisCode );

                searchArea.getCityDetail( jisCode );
                String cityName = searchArea.getCityName();
                int prefId = searchArea.getPrefId();

                arrHotelIdList = searchArea.getSearchHappieIdList( jisCode, 0 );

                shcom = new SearchHotelCommon();
                shcom.setPrefHotelList( arrHotelIdList );
                String queryString = "searchHappieMobile.act?jis_code=" + paramJisCode;

                if ( CheckString.isvalidString( paramAndWord ) )
                {
                    try
                    {
                        paramAndWord = new String( paramAndWord.getBytes( "8859_1" ), "Windows-31J" );

                        searchHotelFreeWord = new SearchHotelFreeword_M2();

                        arrHotelIdList = searchHotelFreeWord.getSearchIdList( paramAndWord );

                        shcom.setResultHotelList( arrHotelIdList );

                        arrHotelIdList = shcom.getMargeHotel( pageRecords, pageNum );

                        pageHeader = "「" + paramAndWord + "」を検索しました";

                        queryString = "searchHappieMobile.act?jis_code=" + paramJisCode + "&andword=" + paramAndWord;
                    }
                    catch ( UnsupportedEncodingException e )
                    {
                        Logging.error( "[ActionFreeWordSearchMobile.execute(): [AndWord= " + paramAndWord + " ] Exception=" + e.toString() );
                        throw e;
                    }
                }
                dataSearchResult = new DataSearchResult_M2();
                if ( (arrHotelIdList != null) && (arrHotelIdList.length > 0) )
                {
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( arrHotelIdList, pageRecords, pageNum );

                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    int hotelCount = searchHotelDao.getCount();
                    int hotelAllCount = searchHotelDao.getAllCount();

                    String currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords, hotelAllCount, hotelCount, 1 );

                    if ( hotelAllCount > pageRecords )
                    {
                        pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, pageRecords, hotelAllCount, queryString, paramUidLink );
                        dataSearchResult.setPageLink( pageLinks );
                    }
                    dataSearchResult.setRecordsOnPage( currentPageRecords );
                    dataSearchResult.setHotelCount( hotelCount );
                    dataSearchResult.setHotelAllCount( hotelAllCount );
                    dataSearchResult.setPageHeader( pageHeader );
                    dataSearchResult.setDataSearchHotel( arrDataSearchHotel );
                }
                else
                {
                    dataSearchResult.setHotelCount( 0 );
                    dataSearchResult.setHotelAllCount( 0 );
                    dataSearchResult.setErrorOccurring( true );
                    dataSearchResult.setErrorMessage( recordNotFound2 );
                }

                dataSearchResult.setParamParameter1( Integer.toString( prefId ) );
                dataSearchResult.setParamParameter2( paramJisCode );
                dataSearchResult.setParamParameter3( cityName );

                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                this.requestDispatcher = request.getRequestDispatcher( "search_result_happie.jsp" );
            }
            else if ( (CheckString.numCheck( paramPrefId )) && (paramJisCode == null) && (paramLocalId != null) )
            {
                dataSearchAreaDTO = new DataSearchArea_M2();
                int prefID = Integer.parseInt( paramPrefId );

                getSearchCity( prefID, dataSearchAreaDTO );

                dataSearchAreaDTO.setParameter1( paramLocalId );

                request.setAttribute( "SEARCH-RESULT", dataSearchAreaDTO );
                this.requestDispatcher = request.getRequestDispatcher( "happie_01.jsp" );
            }
            else
            {
                this.requestDispatcher = request.getRequestDispatcher( "index.jsp" );
            }
            this.requestDispatcher.forward( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionAreaSearchMobile.execute() ][paramJisCode = " +
                    paramJisCode + ",paramPrefId = " + paramPrefId + ",paramLocalId = " + paramLocalId + "] Exception", exception );
            try
            {
                response.sendRedirect( "../../index.jsp" );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionAreaSearchMobile.execute() ] - Unable to dispatch....." +
                        subException.toString() );
            }
        }
        finally
        {
            SearchArea_M2 searchArea = null;
            arrDataSearchHotel = (DataSearchHotel_M2[])null;
            dataSearchResult = null;
        }
    }

    public void getSearchCity(int PrefId, DataSearchArea_M2 dataSearchAreaDTO)
            throws Exception
    {
        SearchArea_M2 searchArea = null;
        DataHotelCity_M2[] arrDataHotelCity = (DataHotelCity_M2[])null;
        DataMasterPref dataMasterPref = null;
        boolean isMasterPrefFound = false;
        try
        {
            searchArea = new SearchArea_M2();
            arrDataHotelCity = searchArea.getCityListHappieByPref( PrefId );

            if ( arrDataHotelCity != null )
            {
                dataSearchAreaDTO.setDataHotelCity( arrDataHotelCity );
                dataSearchAreaDTO.setPrefName( arrDataHotelCity[0].getPrefName() );
                dataSearchAreaDTO.setCityCount( searchArea.getCityCount() );
            }

            dataMasterPref = new DataMasterPref();

            isMasterPrefFound = dataMasterPref.getData( PrefId );
            if ( isMasterPrefFound )
            {
                dataSearchAreaDTO.setPrefName( dataMasterPref.getName() );
            }
            else
            {
                dataSearchAreaDTO.setCityCount( 0 );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionAreaSearchMobile_M2.getSearchCity(): PrefId = " + PrefId + " ] " + exception.toString() );
            throw exception;
        }
        finally
        {
            searchArea = null;
            arrDataHotelCity = (DataHotelCity_M2[])null;
            dataMasterPref = null;
        }
    }
}
