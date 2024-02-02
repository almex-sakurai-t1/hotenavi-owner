package jp.happyhotel.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.QueryStrChecker;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataMapPoint_M2;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.data.DataSearchStation_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelFreeword_M2;
import jp.happyhotel.search.SearchHotelStation_M2;

/**
 * �w�����N���X�i�g�сj
 * 
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/23
 */
public class ActionStationSearchMobile_M2 extends BaseAction
{

    private RequestDispatcher requestDispatcher   = null;

    static int                pageRecords         = Constants.pageLimitRecordMobile; // 1�y�[�W�ɕ\�����錏��
    static int                maxRecords          = Constants.maxRecords;           // �ő匏��
    static String             recordsNotFoundMsg1 = Constants.errorRecordsNotFound1; // �����Ȃ��̏ꍇ�̃G���[���b�Z�[�W
    static String             limitFreewordMsg    = Constants.errorLimitFreeword;   // �ő匏���𒴂����ꍇ�̃G���[���b�Z�[�W

    /**
     * �w�����i�g�сj
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     * @see "�p�����[�^�Ȃ���index_M2.jsp<br>
     *      �n��ID�A�s���{��ID��st_01_M2.jsp<br>
     *      �w����st_01_2_M2.jsp<br>
     *      �s���{��ID�A���[�gID��st_01_2_M2.jsp<br>
     *      ���[�gID�Asearch�p�����[�^��st_result_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String paramPrefId = null;
        String paramRouteId = null;
        String paramSearch = null;
        String paramPage = null;
        String paramUidLink = null;
        String paramName = null;
        String queryString = null;
        String paramAndWord = null;
        String paramLocalId = null;
        String currentPageRecords = null;
        String pageLinks = null;
        String pageHeader = null;
        String paramPos; // �\�t�g�o���N��GPS�擾�����Ƃ��ɕԂ��ʒu���̃p�����[�^
        String paramLat; // �ʒu�������������p�����[�^
        String paramLon; // �ʒu�������������p�����[�^
        String paramKind;
        int i;
        int dispFormat = 1;
        int pageNum = 0;
        int hotelCount;
        int hotelAllCount;
        int carrierFlag;
        int[] hotelIdList = null;
        DataSearchStation_M2 stationSearchDTO = null;
        DataSearchResult_M2 dataSearchResultDTO = null;
        DataMapPoint_M2 dataMapPointDTO = null;
        SearchHotelStation_M2 searchHotelStation = null;
        SearchHotelCommon searchHotelCommon = null;
        SearchHotelFreeword_M2 searchHotelFreeWord = null;
        DataSearchHotel_M2[] dataSearchHotel = null;
        SearchHotelDao_M2 searchHotelDao = null;

        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 && UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_SMARTPHONE )
            {
                response.sendRedirect( requestURL.replace( "http://", "https://" ) + (request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" )) );
                return;
            }
            // �����Ђ̒n�}�R�[�h���p�����[�^�Ŏ󂯂��ꍇ�̓[�������̃R�[�h�ɕϊ������_�C���N�g�i#15040�Ή��j
            QueryStrChecker qsc = new QueryStrChecker();
            int redirection_flag = 0; // 1�F���_�C���N�g�A0�F�X���[�A-1�F�G���[�i�g�b�v�y�[�W�փ��_�C���N�g�j
            String query_str = request.getQueryString();
            if ( query_str == null )
            {
                query_str = "";
            }
            query_str = query_str.replaceAll( "&amp;", "&" ); // "&"���u������Ă���ꍇ������̂Ō��ɖ߂��i���_�C���N�g���邽�߁j
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

            paramPrefId = request.getParameter( "pref_id" );
            paramRouteId = request.getParameter( "route_id" );
            paramName = request.getParameter( "name" );
            paramSearch = request.getParameter( "search" );
            paramPage = request.getParameter( "page" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            paramAndWord = request.getParameter( "andword" );
            paramLocalId = request.getParameter( "local_id" );
            paramLat = request.getParameter( "lat" );
            paramLon = request.getParameter( "lon" );
            paramPos = request.getParameter( "pos" );
            paramKind = request.getParameter( "kind" );
            carrierFlag = UserAgent.getUserAgentType( request );

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
            if ( (paramKind == null) || (paramKind.equals( "" ) != false) || (CheckString.numCheck( paramKind ) == false) )
            {
                paramKind = "0";
            }

            // PC����̃A�N�Z�X�Ȃ�PC�p�̃y�[�W�ɔ�΂�
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType == UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getPCUrl( request ) );
                return;
            }

            // �ʒu�����擾����f�[�^������ꍇ�iGPS�̌��ʂ��Ԃ��Ă����ꍇ�F���E���n�n�j
            if ( (paramLat.compareTo( "" ) != 0 && paramLon.compareTo( "" ) != 0) || paramPos.compareTo( "" ) != 0 )
            {
                // �\�t�g�o���N��pos����ʒu���𕪊�����
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
                stationSearchDTO = new DataSearchStation_M2();

                // �w�����Z�b�g����
                this.getSearchStationListByGps( paramLat, paramLon, Integer.parseInt( paramKind ), stationSearchDTO );

                request.setAttribute( "SEARCH-RESULT", stationSearchDTO );
                request.setAttribute( "GPS", "1" );
                request.setAttribute( "KIND", paramKind );
                // �L�����A�ɉ����ăZ�b�g����p�����[�^��ς���
                if ( carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK )
                {
                    request.setAttribute( "POS", paramPos );
                }
                else
                {
                    request.setAttribute( "LON", paramLon );
                    request.setAttribute( "LAT", paramLat );
                }
                requestDispatcher = request.getRequestDispatcher( "st_01_2_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( CheckString.numCheck( paramPrefId ) && CheckString.numCheck( paramLocalId )
                    && paramRouteId == null )
            {
                int ParamPrefId = Integer.parseInt( paramPrefId );
                stationSearchDTO = new DataSearchStation_M2();
                stationSearchDTO.setParamPrefId( ParamPrefId );
                stationSearchDTO.setParamLocalID( Integer.parseInt( paramLocalId ) );

                // stationSearchDTO�Ƀf�[�^���Z�b�g
                getSearchStationRoute( ParamPrefId, stationSearchDTO );
                request.setAttribute( "SEARCH-RESULT", stationSearchDTO );
                requestDispatcher = request.getRequestDispatcher( "st_01_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            else if ( paramName != null && paramName.compareTo( "null" ) != 0 )
            {
                stationSearchDTO = new DataSearchStation_M2();
                searchHotelStation = new SearchHotelStation_M2();
                boolean isStationFound = false;
                paramName = new String( paramName.getBytes( "8859_1" ), "Windows-31J" );
                stationSearchDTO.setParamName( paramName );

                if ( paramName.compareTo( "" ) != 0 )
                {
                    isStationFound = searchHotelStation
                            .getRailwayStationListByName( paramName );
                    // stationSearchDTO.setReturn(isStationFound);

                    if ( isStationFound )
                    {
                        stationSearchDTO.setMapPointCount( searchHotelStation.getMapPointCount() );
                        stationSearchDTO.setMapPointHotelCount( searchHotelStation.getMapPointHotelCount() );
                        stationSearchDTO.setMapPoint( searchHotelStation.getMapPoint() );
                    }
                }
                request.setAttribute( "SEARCH-RESULT", stationSearchDTO );
                requestDispatcher = request.getRequestDispatcher( "st_01_2_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            else if ( CheckString.isvalidString( paramRouteId )
                    && CheckString.numCheck( paramPrefId )
                    && paramSearch == null )
            {
                int ParamPrefId = Integer.parseInt( paramPrefId );
                stationSearchDTO = new DataSearchStation_M2();
                stationSearchDTO.setParamPrefId( ParamPrefId );

                getSearchStationList( paramRouteId, paramPrefId, stationSearchDTO );
                request.setAttribute( "SEARCH-RESULT", stationSearchDTO );
                requestDispatcher = request.getRequestDispatcher( "st_01_2_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            else if ( CheckString.isvalidString( paramSearch )
                    && CheckString.isvalidString( paramRouteId ) )
            {
                searchHotelStation = new SearchHotelStation_M2();
                searchHotelCommon = new SearchHotelCommon();
                dataSearchResultDTO = new DataSearchResult_M2();

                if ( paramPage != null )
                {
                    pageNum = Integer.parseInt( paramPage );
                }

                // �z�e��ID���X�g���Z�b�g
                hotelIdList = searchHotelStation.getHotelIdList( paramRouteId );
                searchHotelCommon.setEquipHotelList( hotelIdList );

                queryString = "searchStationMobile.act?search=2&route_id=" + paramRouteId;

                if ( CheckString.isvalidString( paramAndWord ) )
                {
                    try
                    {
                        paramAndWord = new String( paramAndWord
                                .getBytes( "8859_1" ), "Windows-31J" );
                        searchHotelFreeWord = new SearchHotelFreeword_M2();
                        // �i�荞�݃t���[���[�h�Ńz�e��ID���X�g���擾
                        hotelIdList = searchHotelFreeWord.getSearchIdList( paramAndWord );
                        // �z�e��ID���X�g���Z�b�g
                        searchHotelCommon.setResultHotelList( hotelIdList );

                        hotelIdList = searchHotelCommon.getMargeHotel( pageRecords, pageNum );

                        // �\������
                        pageHeader = "�u" + paramAndWord + "�v�ōi���݌������܂���";

                        // �y�[�W�����N
                        queryString = "searchStationMobile.act?search=2&route_id=" + paramRouteId + "&andword=" + paramAndWord;

                    }
                    catch ( UnsupportedEncodingException e )
                    {
                        Logging.error( "[ActionStationSearchMobile_M2.execute() ] Exception=" + e.toString() );
                    }
                }
                else
                {
                    if ( searchHotelStation.getDataMapPoint( paramRouteId ) )
                    {
                        dataMapPointDTO = searchHotelStation.getStationInfo();
                        pageHeader = dataMapPointDTO.getName();
                    }
                }

                // �z�e��ID���X�g����z�e���ڍ׏����擾
                searchHotelDao = new SearchHotelDao_M2();
                searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );

                // ���ʂɂ��邽�߃f�[�^���Z�b�g����
                dataSearchHotel = searchHotelDao.getHotelInfo();
                hotelCount = searchHotelDao.getCount();
                hotelAllCount = searchHotelDao.getAllCount();
                currentPageRecords = PagingDetails.getPageRecordsMobile(
                        pageNum, pageRecords, hotelAllCount, hotelCount,
                        dispFormat );

                if ( hotelAllCount > pageRecords )
                {
                    pageLinks = PagingDetails.getPagenationLinkMobile(
                            pageNum, pageRecords, hotelAllCount, queryString,
                            paramUidLink );
                    dataSearchResultDTO.setPageLink( pageLinks );
                }

                dataSearchResultDTO.setRecordsOnPage( currentPageRecords );
                dataSearchResultDTO.setHotelCount( hotelCount );
                dataSearchResultDTO.setHotelAllCount( hotelAllCount );
                dataSearchResultDTO.setPageHeader( pageHeader );
                dataSearchResultDTO.setDataSearchHotel( dataSearchHotel );
                dataSearchResultDTO.setParamParameter1( paramRouteId );
                // dataSearchResultDTO.setParamParameter2();
                // dataSearchResultDTO.setParamParameter3(cityName);

                request.setAttribute( "SEARCH-RESULT", dataSearchResultDTO );
                requestDispatcher = request.getRequestDispatcher( "st_result_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            else
            {
                if ( paramUidLink != null )
                {
                    requestDispatcher = request.getRequestDispatcher( "index_M2.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( "index_M2.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearchMobile_M2.execute()] Exception",
                    exception );
            try
            {
                response.sendRedirect( "../../index.jsp" );
            }
            catch ( Exception subException )
            {
                Logging.error( "[[ActionStationSearchMobile_M2.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            stationSearchDTO = null;
            dataSearchResultDTO = null;
            dataMapPointDTO = null;
            searchHotelCommon = null;
            searchHotelStation = null;
            searchHotelDao = null;
        }
    }

    /**
     * ���[�g���X�g�擾
     * 
     * @param paramPrefId �s���{��ID
     * @param stationSearchDTO DataSearchStation_M2�N���X
     * @throws Exception
     */
    public void getSearchStationRoute(int paramPrefId, DataSearchStation_M2 stationSearchDTO) throws Exception
    {
        boolean isRailwayRouteFound = false;
        SearchHotelStation_M2 searchHotelStation = null;
        try
        {
            searchHotelStation = new SearchHotelStation_M2();
            isRailwayRouteFound = searchHotelStation.getRailwayRouteList( paramPrefId );
            stationSearchDTO.setReturn( isRailwayRouteFound );

            if ( isRailwayRouteFound )
            {
                stationSearchDTO.setName( searchHotelStation.getPrefName() );
                stationSearchDTO.setMapRouteCount( searchHotelStation.getMapRouteCount() );
                stationSearchDTO.setMapRoute( searchHotelStation.getMapRoute() );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearchMobile_M2.getSearchStationRoute()] " + exception.toString() );
            throw exception;
        }
        finally
        {
            searchHotelStation = null;
        }
    }

    /**
     * �w���X�g�擾
     * 
     * @param paramRouteId ���[�gID
     * @param paramPrefId �s���{��ID
     * @param stationSearchDTO DataSearchStation_M2�N���X
     * @throws Exception
     */
    public void getSearchStationList(String paramRouteId, String paramPrefId,
            DataSearchStation_M2 stationSearchDTO) throws Exception
    {
        boolean isStationListFound = false;
        SearchHotelStation_M2 searchHotelStation = null;
        try
        {
            searchHotelStation = new SearchHotelStation_M2();
            isStationListFound = searchHotelStation.getRailwayStationList( paramRouteId, Integer
                    .parseInt( paramPrefId ) );
            stationSearchDTO.setReturn( isStationListFound );

            if ( isStationListFound )
            {
                stationSearchDTO.setMapPointCount( searchHotelStation.getMapPointCount() );
                stationSearchDTO.setMapPointHotelCount( searchHotelStation
                        .getMapPointHotelCount() );
                stationSearchDTO.setMapPoint( searchHotelStation.getMapPoint() );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearchMobile_M2.getSearchStationList()] "
                    + exception.toString() );
            throw exception;
        }
        finally
        {
            searchHotelStation = null;
        }
    }

    /**
     * �w���X�g�擾(GPS)
     * 
     * @param paramRouteId ���[�gID
     * @param paramPrefId �s���{��ID
     * @param stationSearchDTO DataSearchStation_M2�N���X
     * @throws Exception
     */
    public void getSearchStationListByGps(String lat, String lon, int kind, DataSearchStation_M2 stationSearchDTO)
            throws Exception
    {
        boolean isStationListFound = false;
        SearchHotelStation_M2 searchHotelStation = null;
        try
        {
            searchHotelStation = new SearchHotelStation_M2();
            isStationListFound = searchHotelStation.getRailwayStationListByGps( lat, lon, kind );
            stationSearchDTO.setReturn( isStationListFound );

            if ( isStationListFound )
            {
                stationSearchDTO.setMapPointCount( searchHotelStation.getMapPointCount() );
                stationSearchDTO.setMapPointHotelCount( searchHotelStation.getMapPointHotelCount() );
                stationSearchDTO.setMapPoint( searchHotelStation.getMapPoint() );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearchMobile_M2.getSearchStationListByGps()] " + exception.toString() );
            throw exception;
        }
        finally
        {
            searchHotelStation = null;
        }
    }

}
