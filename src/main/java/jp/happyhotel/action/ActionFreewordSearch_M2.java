package jp.happyhotel.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelCity_M2;
import jp.happyhotel.data.DataMasterCity;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataSearchArea_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchMasterEquip_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchArea_M2;
import jp.happyhotel.search.SearchEngineBasic;
import jp.happyhotel.search.SearchEngineBasic_M2;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelFreeword_M2;
import jp.happyhotel.sponsor.SponsorData_M2;

import org.apache.commons.codec.net.URLCodec;

/**
 * �t���[���[�h�����N���X
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/18
 */

public class ActionFreewordSearch_M2 extends BaseAction
{

    static int                pageRecords         = Constants.pageLimitRecords;     // 1�y�[�W�ɕ\�����錏��
    static int                maxRecords          = Constants.maxRecords;           // �ő匏��
    static String             recordsNotFoundMsg1 = Constants.errorRecordsNotFound1; // �����Ȃ��̏ꍇ�̃G���[���b�Z�[�W
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2; // �����Ȃ��̏ꍇ�̃G���[���b�Z�[�W
    static String             limitFreewordMsg    = Constants.errorLimitFreeword;   // �ő匏���𒴂����ꍇ�̃G���[���b�Z�[�W
    private RequestDispatcher requestDispatcher   = null;
    private final boolean     DISP_PC             = false;
    private final int         DISP_COUNT          = 2;

    /**
     * �t���[���[�h����
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     * @see "�t���[���[�h�A200���ȓ���search_result_freeword_M2.jsp<br>
     *      �t���[���[�h�A200�ȏとsearch_freeword_01.jsp<br>
     *      �t���[���[�h�A200�ȏ�Acity�p�����[�^��search_freeword_02.jsp"
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int hotelCount;
        int hotelAllCount;
        int pageNum;
        int masterEquipCount = 0;
        int[] arrhotelIdList = null;
        ArrayList<Integer> arylstJiscode = new ArrayList<Integer>();
        ArrayList<Integer> arylstHotelCount = new ArrayList<Integer>();
        ArrayList<String> arylstCityName = new ArrayList<String>();
        Integer[] arrJiscodeList;
        Integer[] arrHotelCountList;
        String[] arrCityNameList;
        boolean isEquipListFound;
        String paramFreeword = null;
        String paramPage;
        String paramPref = "false";
        String paramPrefId = "0";
        String paramCity = "false";
        String paramJiscode = "0";
        boolean forward01Flag = false; // search_freeword_01.jsp��forwad���邩�̃t���O
        boolean forward02Flag = false; // search_freeword_02.jsp��forwad���邩�̃t���O
        String queryString;
        String currentPageRecords;
        String pageLinks = "";
        String prefName = "null";
        String cityName = "null";
        SearchHotelFreeword_M2 searchHotelFreeWord;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataSearchMasterEquip_M2[] arrDataSearchMasterEquip = null;
        DataSearchArea_M2 dataSearchArea;
        DataMasterPref dmp;
        DataMasterCity dmc;
        SearchEngineBasic_M2 searchEngineBasic;
        SearchEngineBasic seb;
        ActionAreaSearch_M2 actionAreaSearch;
        SponsorData_M2 sponsorData = null;
        SponsorData_M2 randomSponsorData = null;
        boolean isSponsorPrefFound = false;
        boolean isRandomSponsorPrefFound = false;

        searchEngineBasic = new SearchEngineBasic_M2();
        SearchHotelDao_M2 searchHotelDao = null;
        actionAreaSearch = new ActionAreaSearch_M2();
        dataSearchArea = new DataSearchArea_M2();

        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 )
            {
                response.sendRedirect( requestURL.replace( "http://", "https://" ) + (request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" )) );
                return;
            }
            // left module data (equip data)
            isEquipListFound = searchEngineBasic.getEquipList( false );
            if ( isEquipListFound != false )
            {
                masterEquipCount = searchEngineBasic.getMasterEquipCount();
                arrDataSearchMasterEquip = searchEngineBasic.getMasterEquip();
            }
            paramPref = request.getParameter( "pref" );
            if ( paramPref == null )
            {
                paramPref = "false";
            }
            paramCity = request.getParameter( "city" );
            if ( paramCity == null )
            {
                paramCity = "false";
            }
            paramFreeword = request.getParameter( "freeword" );
            if ( paramFreeword != null && (paramFreeword.trim().length() > 0) )
            {
                try
                {
                    paramFreeword = new String( paramFreeword.getBytes( "8859_1" ), "Windows-31J" );
                }
                catch ( UnsupportedEncodingException e )
                {
                    Logging.error( "[ActionFreeWordSearch_M2.execute() ] Exception=" + e.toString() );
                }

                // �X�}�z����̃A�N�Z�X�Ȃ�X�}�z�p�̃y�[�W�ɔ�΂�
                int userAgentType = UserAgent.getUserAgentType( request );
                if ( userAgentType == UserAgent.USERAGENT_SMARTPHONE )
                {
                    String path = "/phone/search/freeword/searchFreewordMobile.act";
                    URLCodec codec = new URLCodec( "Shift-JIS" );
                    String queryStr = "x=0&y=0&freeword=" + codec.encode( paramFreeword );
                    response.sendRedirect( request.getContextPath() + path + "?" + queryStr );
                    return;
                }

                paramPage = request.getParameter( "page" );

                searchHotelFreeWord = new SearchHotelFreeword_M2();
                if ( !CheckString.numCheck( paramPage ) )
                {
                    paramPage = "0";
                }
                // pref_id�p�����[�^�擾
                paramPrefId = request.getParameter( "pref_id" );
                if ( paramPrefId == null || CheckString.numCheck( paramPrefId ) != true )
                {
                    paramPrefId = "0";
                }
                // jis_code�p�����[�^�擾
                paramJiscode = request.getParameter( "jis_code" );
                if ( paramJiscode == null || CheckString.numCheck( paramJiscode ) != true )
                {
                    paramJiscode = "0";
                }

                if ( paramFreeword.compareTo( "null" ) != 0 && CheckString.numCheck( paramPage ) != false )
                {
                    pageNum = Integer.parseInt( paramPage );
                    // �z�e���ꗗ�擾
                    if ( paramPrefId.compareTo( "0" ) != 0 )
                    {
                        arrhotelIdList = searchHotelFreeWord.getSearchIdList( paramFreeword, paramPrefId );
                        // prefName���擾
                        dmp = new DataMasterPref();
                        dmp.getData( Integer.parseInt( paramPrefId ) );
                        prefName = dmp.getName();
                    }
                    else if ( paramJiscode.compareTo( "0" ) != 0 )
                    {
                        arrhotelIdList = searchHotelFreeWord.getSearchIdListByJiscode( paramFreeword, paramJiscode );
                        // cityName���擾
                        dmc = new DataMasterCity();
                        dmc.getData( Integer.parseInt( paramJiscode ) );
                        cityName = dmc.getName();
                    }
                    else
                    {
                        if ( (paramPref.compareTo( "true" ) == 0) || (paramCity.compareTo( "true" ) == 0) )
                        {
                            arrhotelIdList = searchHotelFreeWord.getSearchIdListNoCount( paramFreeword );
                        }
                        else
                        {
                            arrhotelIdList = searchHotelFreeWord.getSearchIdList( paramFreeword );
                        }
                    }
                    if ( arrhotelIdList.length < maxRecords )
                    {
                        if ( arrhotelIdList.length == 0 )
                        {
                            dataSearchResult = new DataSearchResult_M2();

                            dataSearchResult.setErrorOccurring( true );
                            dataSearchResult.setErrorMessage( recordsNotFoundMsg1 );
                            dataSearchResult.setParamParameter1( paramFreeword );
                            dataSearchResult.setMasterEquipCount( masterEquipCount );
                            dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
                        }
                        else
                        {
                            searchHotelDao = new SearchHotelDao_M2();
                            searchHotelDao.getHotelList( arrhotelIdList, pageRecords, pageNum );

                            // ���ʂɂ��邽�߃f�[�^���Z�b�g����
                            arrDataSearchHotel = searchHotelDao.getHotelInfo();
                            hotelCount = searchHotelDao.getCount();
                            hotelAllCount = searchHotelDao.getAllCount();

                            // �y�[�W�����N�̃A�h���X
                            currentPageRecords = PagingDetails.getPageRecords( pageNum, pageRecords, hotelAllCount, hotelCount );
                            queryString = "searchFreeword.act?freeword=" + paramFreeword;
                            if ( paramPrefId.compareTo( "0" ) != 0 )
                            {
                                queryString = queryString + "&pref_id=" + paramPrefId;
                            }
                            if ( paramJiscode.compareTo( "0" ) != 0 )
                            {
                                queryString = queryString + "&jis_code=" + paramJiscode;
                            }

                            dataSearchResult = new DataSearchResult_M2();
                            if ( hotelAllCount > pageRecords )
                            {
                                pageLinks = PagingDetails.getPagenationLink( pageNum, pageRecords, hotelAllCount, queryString );
                                dataSearchResult.setPageLink( pageLinks );
                            }
                            dataSearchResult.setRecordsOnPage( currentPageRecords );
                            dataSearchResult.setHotelCount( hotelCount );
                            dataSearchResult.setDataSearchHotel( arrDataSearchHotel );
                            dataSearchResult.setParamParameter1( paramFreeword );
                            dataSearchResult.setMasterEquipCount( masterEquipCount );
                            dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
                        }
                    }
                    else
                    {
                        // �擾����������𒴂����ۂ̏���
                        if ( paramPref.compareTo( "true" ) != 0 && paramCity.compareTo( "true" ) != 0 )
                        {
                            dataSearchResult = new DataSearchResult_M2();
                            dataSearchResult.setErrorOccurring( true );
                            dataSearchResult.setErrorMessage( limitFreewordMsg );
                            dataSearchResult.setParamParameter1( paramFreeword );
                            dataSearchResult.setMasterEquipCount( masterEquipCount );
                            dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
                        }
                        else if ( paramCity.compareTo( "true" ) != 0 )
                        {
                            dataSearchResult = new DataSearchResult_M2();
                            queryString = "searchFreeword.act?freeword=" + paramFreeword;
                            dataSearchResult.setParamParameter1( paramFreeword );
                            forward01Flag = true;
                        }
                        else
                        {
                            // �s�撬���f�[�^�擾
                            try
                            {
                                // �t���[���[�h�ōi�荞�݂����s�撬���f�[�^�擾
                                seb = new SearchEngineBasic();
                                if ( seb.getCityListByPref( Integer.parseInt( paramPrefId ), 0 ) )
                                {

                                    for( int i = 0 ; i < seb.getMasterCityCount() - 1 ; i++ )
                                    {
                                        arrhotelIdList = searchHotelFreeWord.getSearchIdListByJiscode( paramFreeword, Integer.toString( seb.getMasterCity()[i].getJisCode() ) );
                                        if ( arrhotelIdList.length > 0 )
                                        {
                                            // �s�撬���̌������ʂ��P���ȏ゠��ꍇ��ArrayList��add
                                            arylstJiscode.add( seb.getMasterCity()[i].getJisCode() );
                                            arylstHotelCount.add( arrhotelIdList.length );
                                            arylstCityName.add( seb.getMasterCity()[i].getName() );
                                        }
                                    }
                                    arrJiscodeList = (Integer[])arylstJiscode.toArray( new Integer[arylstJiscode.size()] );
                                    arrHotelCountList = (Integer[])arylstHotelCount.toArray( new Integer[arylstHotelCount.size()] );
                                    arrCityNameList = (String[])arylstCityName.toArray( new String[arylstCityName.size()] );

                                    request.setAttribute( "JISCODE-LIST", arrJiscodeList );
                                    request.setAttribute( "HOTELCOUNT-LIST", arrHotelCountList );
                                    request.setAttribute( "CITYNAME-LIST", arrCityNameList );
                                }

                                // �t���[���[�h�ōi�荞�݂��Ȃ��s�撬���f�[�^�擾
                                // actionAreaSearch.getSearchCity(Integer.parseInt(paramPrefId), dataSearchArea );

                                // �X�|���T�[�f�[�^�̎擾
                                actionAreaSearch.getSearchSponserDisp( Integer.parseInt( paramPrefId ), dataSearchArea );
                                sponsorData = new SponsorData_M2();
                                randomSponsorData = new SponsorData_M2();
                                isSponsorPrefFound = sponsorData.getSponsorByPref( Integer.parseInt( paramPrefId ) );
                                if ( isSponsorPrefFound )
                                {
                                    dataSearchArea.setSponsorData( sponsorData );
                                }
                                Logging.info( "[ActionFreeWordSearch_M2.getSearchCity] isSponsorPrefFound=" + isSponsorPrefFound );
                                isRandomSponsorPrefFound = randomSponsorData.getRandomSponsorByPref( Integer.parseInt( paramPrefId ), DISP_COUNT, DISP_PC );
                                if ( isRandomSponsorPrefFound )
                                {
                                    dataSearchArea.setRandomSponsorData( randomSponsorData );
                                    dataSearchArea.setRandomSponsorDisplayResult( isRandomSponsorPrefFound );
                                }
                                Logging.info( "[ActionFreeWordSearch_M2.getSearchCity] isRandomSponsorPrefFound=" + isRandomSponsorPrefFound );
                                Logging.info( "[ActionFreeWordSearch_M2.getSearchCity] randomSponsorData=" + randomSponsorData.getSponsorCount() );

                                request.setAttribute( "SEARCHAREA-RESULT", dataSearchArea );

                            }
                            catch ( Exception e )
                            {
                                Logging.error( "[ActionFreeWordSearch_M2.getSearchCity] Exception=" + e.toString() );
                            }
                            dataSearchResult = new DataSearchResult_M2();
                            queryString = "searchFreeword.act?freeword=" + paramFreeword;
                            dataSearchResult.setParamParameter1( paramFreeword );
                            forward02Flag = true;
                        }
                    }
                }
                else
                {
                    dataSearchResult = new DataSearchResult_M2();
                    dataSearchResult.setErrorOccurring( true );
                    dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                    dataSearchResult.setParamParameter1( paramFreeword );
                    dataSearchResult.setMasterEquipCount( masterEquipCount );
                    dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
                }
            }
            else
            {
                dataSearchResult = new DataSearchResult_M2();
                dataSearchResult.setErrorOccurring( true );
                dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                dataSearchResult.setParamParameter1( paramFreeword );
                dataSearchResult.setMasterEquipCount( masterEquipCount );
                dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
            }

            // prefName��set�i�擾���Ă��Ȃ��ꍇ��"null"�j
            dataSearchResult.setParamParameter2( prefName );
            // prefName��set�i�擾���Ă��Ȃ��ꍇ��"null"�j
            dataSearchResult.setParamParameter3( cityName );

            /**
             * Request Dispatching to JSP
             * 
             */

            request.setAttribute( "SEARCH-RESULT", dataSearchResult );
            if ( forward01Flag == true )
            {
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_freeword_01.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_freeword_01.jsp" );
                }
            }
            else if ( forward02Flag == true )
            {
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_freeword_02.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_freeword_02.jsp" );
                }
            }
            else
            {
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_freeword_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_freeword_M2.jsp" );
                }
            }
            requestDispatcher.forward( request, response );

        }
        catch ( Exception e )
        {
            try
            {
                dataSearchResult = new DataSearchResult_M2();
                dataSearchResult.setErrorOccurring( true );
                dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                dataSearchResult.setParamParameter1( paramFreeword );
                request.setAttribute( "SEARCH-RESULT", dataSearchResult );

                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_freeword_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_freeword_M2.jsp" );
                }

                requestDispatcher.forward( request, response );
            }
            catch ( Exception exp )
            {
                Logging.error( "unable to dispatch.....=" + exp.toString(), exp );
            }
        }
        finally
        {
            searchHotelFreeWord = null;
            arrDataSearchHotel = null;
            dataSearchResult = null;
            searchHotelDao = null;
        }
    }

    /**
     * �s���{�������擾
     * 
     * @param prefId �s���{��ID
     * @param dataSearchArea DataSearchArea_M2�N���X
     * @throws Exception
     */
    public void getSearchCity(int prefId, DataSearchArea_M2 dataSearchArea)
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
                dataSearchArea.setDataHotelCity( arrDataHotelCity );
                dataSearchArea.setPrefName( arrDataHotelCity[0].getPrefName() );
                dataSearchArea.setCityCount( searchArea.getCityCount() );
            }
            else
            {
                dataSearchArea.setPrefName( searchArea.getPrefInfo( prefId ) );
                dataSearchArea.setCityCount( 0 );
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionAreaSearch_M2.getSearchCity(" + prefId + ","
                    + dataSearchArea + ")] " + exception.toString() );
            throw exception;
        }
        finally
        {
            searchArea = null;
            arrDataHotelCity = null;

        }
    }
}
