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
import jp.happyhotel.common.QueryStrChecker;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataMapPoint_M2;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchMasterEquip_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.data.DataSearchStation_M2;
import jp.happyhotel.search.SearchEngineBasic_M2;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelStation_M2;
import jp.happyhotel.sponsor.SponsorData_M2;

/**
 * �w�����N���X
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/10
 */
public class ActionStationSearch_M2 extends BaseAction
{

    private RequestDispatcher requestDispatcher;
    static int                pageRecords         = Constants.pageLimitRecords;     // 1�y�[�W�ɕ\�����錏��
    static int                maxRecords          = Constants.maxRecords;           // �ő匏��
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2; // �����Ȃ��̏ꍇ�̃G���[���b�Z�[�W
    static String             limitRecordsMsg     = Constants.errorLimitRecords;    // �ő匏���𒴂����ꍇ�̃G���[���b�Z�[�W

    private String            actionURL           = "searchStation.act?";
    private final boolean     DISP_PC             = false;
    private final int         DISP_COUNT          = 2;

    /**
     * �w����
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     * @see "�p�����[�^�Ȃ���search_station_01_M2.jsp<br>
     *      �s���{��ID��search_station_02_M2.jsp<br>
     *      �s���{��ID�A���[�gID��search_station_03_M2.jsp<br>
     *      �wID��search_result_station_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String paramPrefId = null;
        String paramRouteId = null;
        String paramStationId = null;
        String paramJisCode = null;
        String paramPage = null;
        DataSearchStation_M2 stationSearchDTO = null;
        DataSearchResult_M2 dataSearchResultDTO = null;
        DataMapPoint_M2 dataMapPointDTO = null;
        SearchHotelStation_M2 searchHotelStation = null;

        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 )
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
            paramStationId = request.getParameter( "station_id" );
            paramJisCode = request.getParameter( "jis_code" );
            paramPage = request.getParameter( "page" );

            // �X�}�z����̃A�N�Z�X�Ȃ�X�}�z�p�̃y�[�W�ɔ�΂�
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType != UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getSmartUrl( request ) );
                return;
            }

            if ( CheckString.numCheck( paramPrefId ) && paramRouteId == null )
            {
                // �s���{��ID��null��u�����N�ł͂Ȃ��A�����[�gID��null
                int prefId = Integer.parseInt( paramPrefId );
                stationSearchDTO = new DataSearchStation_M2();
                stationSearchDTO.setParamPrefId( prefId );

                // Set Search_Station_Route field into stationSearchDTO
                getSearchStationRoute( prefId, stationSearchDTO );

                // �G���A�L�����擾����
                searchSponserDisp( prefId, stationSearchDTO );
                // �G���A�L���i���[�e�[�V�����j���擾����
                searchRandomSponserDisp( prefId, stationSearchDTO );

                request.setAttribute( "SEARCH-RESULT", stationSearchDTO );
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_station_02_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_station_02_M2.jsp" );
                }
            }
            else if ( CheckString.isvalidString( paramRouteId ) &&
                    CheckString.numCheck( paramPrefId ) )
            {
                int prefId = Integer.parseInt( paramPrefId );
                searchHotelStation = new SearchHotelStation_M2();
                stationSearchDTO = new DataSearchStation_M2();
                stationSearchDTO.setName( searchHotelStation.getDataMapRouteName( paramRouteId ) );

                // ���[�gID�ŉw���X�g���擾
                getSearchStationList( paramRouteId, stationSearchDTO );

                // �G���A�L�����擾����
                searchSponserDisp( prefId, stationSearchDTO );
                // �G���A�L���i���[�e�[�V�����j���擾����
                searchRandomSponserDisp( prefId, stationSearchDTO );

                request.setAttribute( "SEARCH-RESULT", stationSearchDTO );
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_station_03_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_station_03_M2.jsp" );
                }
            }
            // �wID�`�F�b�N
            else if ( CheckString.isvalidString( paramStationId ) )
            {
                boolean isMapPointDataExist = false;
                boolean isCityDataExist = false;
                dataSearchResultDTO = new DataSearchResult_M2();
                searchHotelStation = new SearchHotelStation_M2();
                isMapPointDataExist = searchHotelStation.getDataMapPoint( paramStationId );
                dataMapPointDTO = searchHotelStation.getStationInfo();
                dataSearchResultDTO.setDataMapPointName( dataMapPointDTO.getName() );
                dataSearchResultDTO.setDataMapPointOption6( dataMapPointDTO.getOption6() );
                dataSearchResultDTO.setParamParameter1( paramStationId );

                // �s�撬���R�[�h������Ύs�撬���R�[�h��n��
                if ( CheckString.numCheck( paramJisCode ) )
                {
                    dataSearchResultDTO.setJisCode( paramJisCode );
                    // �s�撬���R�[�h�͍����E�ғ��E���ŁE���ߎw��s�s�ŐV�݂ɂȂ�ȂǕύX�����ꍇ�����邽��1000�Ŋ���A�s���{���R�[�h���Z�b�g
                    dataSearchResultDTO.setDataMasterCityPrefID( Integer.parseInt( paramJisCode ) / 1000 );

                    getSearchResultSponser( paramJisCode, dataSearchResultDTO, searchHotelStation );
                    getSearchResultRandomSponser( paramJisCode, dataSearchResultDTO, searchHotelStation );
                }

                // �w��������
                getSearchResultStation( paramPage, paramStationId, dataSearchResultDTO, searchHotelStation, paramJisCode );
                request.setAttribute( "SEARCH-RESULT", dataSearchResultDTO );
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_station_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_station_M2.jsp" );
                }
            }
            else
            {
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_station_01_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_station_01_M2.jsp" );
                }
            }
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearch_M2 - execute()] Exception",
                    exception );
        }
        finally
        {
            stationSearchDTO = null;
            dataSearchResultDTO = null;
        }
    }

    /**
     * ���[�g���X�g�y�у��[�g�����擾
     * 
     * @param paramPrefId �s���{��ID
     * @param stationSearchDTO DataSearchStation_M2�N���X
     * @throws Exception
     */
    public void getSearchStationRoute(int paramPrefId, DataSearchStation_M2 stationSearchDTO) throws Exception
    {
        boolean isRouteRecordExist = false;
        SearchHotelStation_M2 searchHotelStation = null;
        try
        {
            searchHotelStation = new SearchHotelStation_M2();
            isRouteRecordExist = searchHotelStation.getRailwayRouteList( paramPrefId );
            stationSearchDTO.setReturn( isRouteRecordExist );

            if ( isRouteRecordExist != false )
            {
                stationSearchDTO.setName( searchHotelStation.getPrefName() );
                stationSearchDTO.setMapRouteCount( searchHotelStation.getMapRouteCount() );
                stationSearchDTO.setMapRoute( searchHotelStation.getMapRoute() );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearch_M2 - getSearchStationRoute()] "
                    + exception.toString() );
            throw exception;
        }
        finally
        {
            searchHotelStation = null;
        }
    }

    /**
     * �G���A�L���擾
     * 
     * @param paramPrefId �s���{��ID
     * @param stationSearchDTO DataSearchStation_M2�N���X
     * @throws Exception
     */
    public void searchSponserDisp(int paramPrefId, DataSearchStation_M2 stationSearchDTO) throws Exception
    {
        boolean isSponserResult = false;
        SponsorData_M2 sponsorData = null;
        try
        {
            sponsorData = new SponsorData_M2();
            isSponserResult = sponsorData.getSponsorByPref( paramPrefId );
            stationSearchDTO.setSponserDisplayResult( isSponserResult );
            if ( isSponserResult )
            {
                stationSearchDTO.setSponsorData( sponsorData );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearch_M2 - searchSponserDisp()] "
                    + exception.toString() );
            throw exception;
        }
        finally
        {
            sponsorData = null;
        }
    }

    /**
     * �G���A�L���i���[�e�[�V�����j�擾
     * 
     * @param paramPrefId �s���{��ID
     * @param stationSearchDTO DataSearchStation_M2�N���X
     * @throws Exception
     */
    public void searchRandomSponserDisp(int paramPrefId, DataSearchStation_M2 stationSearchDTO) throws Exception
    {
        boolean isSponserResult = false;
        SponsorData_M2 sponsorData = null;
        try
        {
            sponsorData = new SponsorData_M2();
            isSponserResult = sponsorData.getRandomSponsorByPref( paramPrefId, DISP_COUNT, DISP_PC );
            stationSearchDTO.setRandomSponserDisplayResult( isSponserResult );
            Logging.error( "[ActionStationSearch_M2 - searchSponserDisp() = " + isSponserResult + "]" );
            if ( isSponserResult )
            {
                stationSearchDTO.setRandomSponsorData( sponsorData );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearch_M2 - searchRandomSponserDisp()] "
                    + exception.toString() );
            throw exception;
        }
        finally
        {
            sponsorData = null;
        }
    }

    /**
     * �G���A�L���擾�i�������ʁj
     * 
     * @param paramJisCode �s�撬��ID
     * @param dataSearchResultDTO DataSearchResult_M2�N���X
     * @param searchHotelStation SearchHotelStation_M2�N���X
     * @throws Exception
     */
    public void getSearchResultSponser(String paramJisCode,
            DataSearchResult_M2 dataSearchResultDTO, SearchHotelStation_M2 searchHotelStation)
            throws Exception
    {
        boolean ret = false;
        SponsorData_M2 sponsorData = null;
        try
        {
            sponsorData = new SponsorData_M2();

            // �s�撬���R�[�h�͍����E�ғ��E���ŁE���ߎw��s�s�ŐV�݂ɂȂ�ȂǕύX�����ꍇ�����邽��1000�Ŋ���A�s���{���R�[�h�ŋ��߂�
            ret = sponsorData.getSponsorByPref( Integer.parseInt( paramJisCode ) / 1000 );
            dataSearchResultDTO.setSponsorDataStatus( ret );
            dataSearchResultDTO.setSponsorData( sponsorData );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearch_M2 - getSearchResultSponser()] "
                    + exception.toString() );
            throw exception;
        }
        finally
        {
            sponsorData = null;
        }
    }

    /**
     * �G���A�L���擾�i���[�e�[�V�����j�i�������ʁj
     * 
     * @param paramJisCode �s�撬��ID
     * @param dataSearchResultDTO DataSearchResult_M2�N���X
     * @param searchHotelStation SearchHotelStation_M2�N���X
     * @throws Exception
     */
    public void getSearchResultRandomSponser(String paramJisCode,
            DataSearchResult_M2 dataSearchResultDTO, SearchHotelStation_M2 searchHotelStation)
            throws Exception
    {
        boolean ret = false;
        SponsorData_M2 sponsorData = null;
        try
        {
            sponsorData = new SponsorData_M2();

            // �s�撬���R�[�h�͍����E�ғ��E���ŁE���ߎw��s�s�ŐV�݂ɂȂ�ȂǕύX�����ꍇ�����邽��1000�Ŋ���A�s���{���R�[�h�ŋ��߂�
            ret = sponsorData.getRandomSponsorByPref( Integer.parseInt( paramJisCode ) / 1000, DISP_COUNT, DISP_PC );
            dataSearchResultDTO.setRandomSponsorDataStatus( ret );
            dataSearchResultDTO.setRandomSponsorData( sponsorData );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearch_M2 - getSearchResultRandomSponser()] "
                    + exception.toString() );
            throw exception;
        }
        finally
        {
            sponsorData = null;
        }
    }

    /**
     * �w���X�g�擾
     * 
     * @param paramRouteId ���[�gID
     * @param stationSearchDTO DataSearchStation_M2�N���X
     * @throws Exception
     */
    public void getSearchStationList(String paramRouteId,
            DataSearchStation_M2 stationSearchDTO) throws Exception
    {
        boolean isStationRecordExist = false;
        SearchHotelStation_M2 searchHotelStation = null;
        try
        {
            searchHotelStation = new SearchHotelStation_M2();
            isStationRecordExist = searchHotelStation.getRailwayStationList( paramRouteId );
            stationSearchDTO.setReturn( isStationRecordExist );

            if ( isStationRecordExist != false )
            {
                stationSearchDTO.setMapPointCount( searchHotelStation.getMapPointCount() );
                stationSearchDTO.setMapPointHotelCount( searchHotelStation
                        .getMapPointHotelCount() );
                stationSearchDTO.setMapPoint( searchHotelStation.getMapPoint() );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearch_M2 - getSearchStationList()] "
                    + exception.toString() );
            throw exception;
        }
        finally
        {
            searchHotelStation = null;
        }
    }

    /**
     * �w�������ʎ擾
     * 
     * @param paramPage �y�[�W
     * @param paramStationId �wID
     * @param dataSearchResultDTO DataSearchResult_M2�N���X
     * @param searchHotelStation SearchHotelStation_M2�N���X
     * @param paramJisCode �s�撬��ID
     * @throws Exception
     */
    public void getSearchResultStation(String paramPage, String paramStationId,
            DataSearchResult_M2 dataSearchResultDTO, SearchHotelStation_M2 searchHotelStation,
            String paramJisCode)
            throws Exception
    {
        int hotelCount = 0;
        int hotelAllCount = 0;
        int pageNum = 0;
        int masterEquipCount = 0;
        boolean isEquipList = false;
        String queryString = "";
        String currentPageRecords = "";
        String pageLinks = "";
        DataSearchHotel_M2[] dataSearchHotel = null;
        DataSearchMasterEquip_M2[] dataSearchMasterEquip = null;
        SearchEngineBasic_M2 searchEngineBasic = null;
        SearchHotelDao_M2 searchHotelDao = null;
        try
        {
            if ( !CheckString.numCheck( paramPage ) )
            {
                paramPage = "0";
                pageNum = 0;
            }
            pageNum = Integer.parseInt( paramPage );
            // �z�e��ID���X�g�̎擾
            searchHotelDao = new SearchHotelDao_M2();
            searchHotelDao.getHotelList( searchHotelStation.getHotelIdList( paramStationId ), 20, pageNum );
            dataSearchHotel = searchHotelDao.getHotelInfo();
            hotelCount = searchHotelDao.getCount();
            hotelAllCount = searchHotelDao.getAllCount();

            // �ݔ����̎擾
            searchEngineBasic = new SearchEngineBasic_M2();
            isEquipList = searchEngineBasic.getEquipList( false );

            if ( isEquipList )
            {
                masterEquipCount = searchEngineBasic.getMasterEquipCount();
                dataSearchMasterEquip = searchEngineBasic.getMasterEquip();
            }

            currentPageRecords = PagingDetails.getPageRecords( pageNum,
                    pageRecords, hotelAllCount, hotelCount );
            queryString = actionURL + "station_id=" + paramStationId + "&jis_code=" + paramJisCode;

            if ( hotelAllCount < maxRecords )
            {
                if ( hotelAllCount == 0 )
                {
                    dataSearchResultDTO.setErrorOccurring( true );
                    dataSearchResultDTO.setErrorMessage( recordsNotFoundMsg2 );
                    dataSearchResultDTO.setMasterEquipCount( masterEquipCount );
                    dataSearchResultDTO
                            .setDataSearchMasterEquip( dataSearchMasterEquip );
                }
                else
                {
                    if ( hotelAllCount > pageRecords )
                    {
                        pageLinks = PagingDetails.getPagenationLink(
                                pageNum, pageRecords, hotelAllCount, queryString );
                        dataSearchResultDTO.setPageLink( pageLinks );
                    }
                    dataSearchResultDTO.setRecordsOnPage( currentPageRecords );
                    dataSearchResultDTO.setHotelCount( hotelCount );
                    dataSearchResultDTO.setDataSearchHotel( dataSearchHotel );
                    dataSearchResultDTO.setMasterEquipCount( masterEquipCount );
                    dataSearchResultDTO
                            .setDataSearchMasterEquip( dataSearchMasterEquip );
                }
            }
            else
            {
                dataSearchResultDTO.setErrorOccurring( true );
                dataSearchResultDTO.setErrorMessage( limitRecordsMsg );
                dataSearchResultDTO.setMasterEquipCount( masterEquipCount );
                dataSearchResultDTO
                        .setDataSearchMasterEquip( dataSearchMasterEquip );
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearch_M2 - getSearchResultStation()] "
                    + exception.toString() );
            throw exception;
        }
        finally
        {
            searchEngineBasic = null;
            searchHotelDao = null;
        }
    }

}
