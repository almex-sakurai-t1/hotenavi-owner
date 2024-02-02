package jp.happyhotel.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataMasterLocal_M2;
import jp.happyhotel.data.DataMasterPref_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelFreeword_M2;

/**
 * �t���[���[�h�����N���X�i�g�сj
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/18
 */

public class ActionFreewordSearchMobile_M2 extends BaseAction
{

    static int                pageRecords         = Constants.pageLimitRecordMobile; // 1�y�[�W�ɕ\�����錏��
    static int                maxRecords          = Constants.maxRecordsMobile;     // �ő匏��
    static String             recordsNotFoundMsg1 = Constants.errorRecordsNotFound1; // �����Ȃ��̏ꍇ�̃G���[���b�Z�[�W
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2; // �����Ȃ��̏ꍇ�̃G���[���b�Z�[�W
    static boolean            zeroResultDisp      = Constants.zeroResultDisplay;    // 0���������ꍇ�A�\�����邩�ǂ���
    static boolean            shiborikomiByPref   = Constants.shiborikomiByPref;    // �s���{���ōi�肱�ނ��ǂ���
    private RequestDispatcher requestDispatcher   = null;

    /**
     * �t���[���[�h�����i�g�сj
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     * @see "�f�B�X�p�b�`���search_result_freeword_M2.jsp<br>
     *      50���𒴂��A�n��ID���Ȃ��A�s���{��ID���Ȃ��ꍇ��type2��<br>
     *      �n��ID������ꍇ��type=3<br>
     *      �s���{��ID������ꍇ��type4��<br>
     *      type1��type4���������ʂ�\��"
     * 
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int hotelCount;
        int hotelAllCount;
        int pageNum = 0;
        int[] hotelIdList;
        int dispFormat = 1;
        String paramFreeword = null;
        String paramPrefId = null;
        String paramLocalId = null;
        String paramAndWord = null;
        String paramUidLink = null;
        String paramPage = null;
        String pageHeader = null;
        String queryString = null;
        String currentPageRecords;
        String pageLinks = "";
        SearchHotelFreeword_M2 searchHotelFreeWord = null;
        DataSearchHotel_M2[] dataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataMasterLocal_M2[] dataMasterLocal = null;
        DataMasterPref_M2[] dataMasterPref = null;
        SearchHotelCommon shcom;
        SearchHotelDao_M2 searchHotelDao = null;
        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 && UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_SMARTPHONE )
            {
                response.sendRedirect( requestURL.replace( "http://", "https://" ) + (request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" )) );
                return;
            }

            paramFreeword = request.getParameter( "freeword" );
            paramPrefId = request.getParameter( "pref_id" );
            paramLocalId = request.getParameter( "local_id" );
            paramPage = request.getParameter( "page" );
            paramAndWord = request.getParameter( "andword" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );

            if ( !CheckString.numCheck( paramPage ) )
            {
                paramPage = "0";
            }

            if ( paramFreeword != null )
            {
                try
                {
                    paramFreeword = new String( paramFreeword.getBytes( "8859_1" ), "Windows-31J" );
                    // au�̔��p��߰��΍�
                    paramFreeword = ReplaceString.replace( paramFreeword, " ", "�@" );
                }
                catch ( UnsupportedEncodingException e )
                {
                    Logging.error( "[ActionFreeWordSearchMobile.execute() ] Exception=" + e.toString() );
                }

                if ( paramFreeword != null && CheckString.numCheck( paramPage ) != false && paramPrefId == null && paramLocalId == null )
                {

                    searchHotelFreeWord = new SearchHotelFreeword_M2();
                    pageHeader = "�u" + paramFreeword + "�v���������܂���";

                    pageNum = Integer.parseInt( paramPage );

                    // �t���[���[�h�Ńz�e��ID���X�g���擾
                    hotelIdList = searchHotelFreeWord.getSearchIdList( paramFreeword );

                    shcom = new SearchHotelCommon();
                    shcom.setEquipHotelList( hotelIdList );

                    // �i�荞�݂̃t���[���[�h���������ꍇ
                    if ( paramAndWord != null && (paramAndWord.trim().length() > 0) )
                    {
                        try
                        {
                            paramAndWord = new String( paramAndWord.getBytes( "8859_1" ), "Windows-31J" );

                            hotelIdList = searchHotelFreeWord.getSearchIdList( paramAndWord );

                            shcom.setResultHotelList( hotelIdList );

                            hotelIdList = shcom.getMargeHotel( pageRecords, pageNum );

                            // �\������
                            pageHeader = "�u" + paramAndWord + "�v�ōi���݌������܂���";

                        }
                        catch ( UnsupportedEncodingException e )
                        {
                            Logging.error( "[ActionFreeWordSearchMobile.execute() ] Exception=" + e.toString() );
                        }
                    }

                    // �������ʂ�50���𒴂��Ă��Ȃ��ꍇ
                    if ( hotelIdList.length < maxRecords )
                    {
                        // �������ʂ�0���̏ꍇ
                        if ( hotelIdList.length == 0 )
                        {
                            // �i�荞�݂̃t���[���[�h�ōi�荞��
                            if ( paramAndWord != null && (paramAndWord.trim().length() > 0) )
                            {
                                dataSearchResult = new DataSearchResult_M2();

                                dataSearchResult.setRecordsOnPage( recordsNotFoundMsg2 );
                                dataSearchResult.setHotelCount( 0 );
                                dataSearchResult.setHotelAllCount( 0 );
                                dataSearchResult.setPageHeader( pageHeader );
                                dataSearchResult.setDataSearchHotel( dataSearchHotel );
                                dataSearchResult.setParamParameter1( paramFreeword );
                                dataSearchResult.setParamParameter2( "type1" );
                                dataSearchResult.setParamParameter3( paramPrefId );
                            }
                            else
                            {
                                dataSearchResult = new DataSearchResult_M2();
                                dataSearchResult.setRecordsOnPage( recordsNotFoundMsg2 );
                                dataSearchResult.setHotelCount( 0 );
                                dataSearchResult.setHotelAllCount( 0 );
                                dataSearchResult.setPageHeader( pageHeader );
                                dataSearchResult.setDataSearchHotel( dataSearchHotel );
                                dataSearchResult.setParamParameter1( paramFreeword );
                                dataSearchResult.setParamParameter2( "type1" );
                                dataSearchResult.setParamParameter3( paramPrefId );
                            }
                        }
                        else
                        {
                            searchHotelDao = new SearchHotelDao_M2();
                            searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );

                            dataSearchHotel = searchHotelDao.getHotelInfo();
                            hotelCount = searchHotelDao.getCount();
                            hotelAllCount = searchHotelDao.getAllCount();
                            queryString = "searchFreewordMobile.act?freeword=" + URLEncoder.encode( paramFreeword, "Shift_JIS" );
                            if ( paramAndWord != null )
                            {
                                queryString = queryString + "&andword=" + URLEncoder.encode( paramAndWord, "Shift_JIS" );
                            }

                            currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords, hotelAllCount, hotelCount, dispFormat );
                            dataSearchResult = new DataSearchResult_M2();

                            if ( hotelAllCount > pageRecords )
                            {
                                pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, pageRecords, hotelAllCount, queryString, paramUidLink );
                                dataSearchResult.setPageLink( pageLinks );
                            }
                            dataSearchResult.setRecordsOnPage( currentPageRecords );
                            dataSearchResult.setHotelCount( hotelCount );
                            dataSearchResult.setHotelAllCount( hotelAllCount );
                            dataSearchResult.setPageHeader( pageHeader );
                            dataSearchResult.setDataSearchHotel( dataSearchHotel );
                            dataSearchResult.setParamParameter1( paramFreeword );
                            dataSearchResult.setParamParameter2( "type1" );
                            dataSearchResult.setParamParameter3( paramPrefId );
                        }
                    }
                    else
                    {
                        dataMasterLocal = searchHotelFreeWord.getHotelListByLocal( hotelIdList, zeroResultDisp );

                        int localcount = searchHotelFreeWord.getLocalCount();

                        dataSearchResult = new DataSearchResult_M2();
                        dataSearchResult.setDataMasterLocal( dataMasterLocal );
                        dataSearchResult.setLocalCount( localcount );
                        dataSearchResult.setParamParameter1( paramFreeword );
                        dataSearchResult.setParamParameter2( "type2" );
                        dataSearchResult.setParamParameter3( paramPrefId );
                    }
                }
                /* �t���[���[�h����A�n��ID��null�ȊO */
                else if ( paramFreeword != null && CheckString.numCheck( paramPage ) != false && paramLocalId != null )
                {

                    int localId = Integer.parseInt( paramLocalId );
                    searchHotelFreeWord = new SearchHotelFreeword_M2();

                    hotelIdList = searchHotelFreeWord.getSearchIdList( paramFreeword );

                    dataMasterPref = searchHotelFreeWord.getHotelListByPref( hotelIdList, localId, zeroResultDisp );

                    int prefcount = searchHotelFreeWord.getPrefCount();

                    // �n�����̌������ʂ�0���������ꍇ�A�\�����Ȃ�
                    int check = 0;
                    for( int k = 0 ; k < prefcount ; k++ )
                    {
                        if ( dataMasterPref[k].getHotelCount() == 0 )
                            check++;
                        else
                        {
                            break;
                        }
                    }
                    if ( check == prefcount )
                    {
                        dataSearchResult = new DataSearchResult_M2();

                        dataSearchResult.setErrorOccurring( true );
                        dataSearchResult.setErrorMessage( " " );
                        dataSearchResult.setParamParameter1( paramFreeword );
                        dataSearchResult.setParamParameter2( "type0" );

                    }
                    else
                    {
                        dataSearchResult = new DataSearchResult_M2();

                        dataSearchResult.setDataMasterPrefMobile( dataMasterPref );
                        dataSearchResult.setPrefCount( prefcount );
                        dataSearchResult.setParamParameter1( paramFreeword );
                        dataSearchResult.setParamParameter2( "type3" );
                        dataSearchResult.setParamParameter3( paramPrefId );
                    }

                }
                /* �t���[���[�h������A���s���{��ID��null�ȊO */
                else if ( paramFreeword != null && CheckString.numCheck( paramPage ) != false && paramPrefId != null )
                {

                    searchHotelFreeWord = new SearchHotelFreeword_M2();

                    pageHeader = "�u" + paramFreeword + "�v���������܂���";

                    int PrefId = Integer.parseInt( paramPrefId );
                    pageNum = Integer.parseInt( paramPage );

                    // �t���[���[�h�Ɠs���{��ID����z�e��ID���X�g���擾
                    hotelIdList = searchHotelFreeWord.getHotelBasicIdListByPref( paramFreeword, PrefId );

                    // ���݂̃z�e��ID���X�g���Z�b�g
                    shcom = new SearchHotelCommon();
                    shcom.setEquipHotelList( hotelIdList );

                    if ( paramAndWord != null && (paramAndWord.trim().length() > 0) )
                    {
                        try
                        {
                            paramAndWord = new String( paramAndWord.getBytes( "8859_1" ), "Windows-31J" );

                            // �i�荞�݂̃t���[���[�h�Ɠs���{��ID�ōi�荞�񂾌��ʂ�0���������ꍇ
                            if ( hotelIdList.length == 0 )
                            {
                                hotelIdList = searchHotelFreeWord.getHotelBasicIdListByPref( paramAndWord, PrefId );
                                pageHeader = "�u" + paramAndWord + "�v���������܂���";
                            }
                            else
                            {
                                if ( shiborikomiByPref != true )
                                {
                                    // �s���{��ID�ōi�荞�񂾌���
                                    hotelIdList = searchHotelFreeWord.getSearchIdList( paramFreeword );
                                    shcom.setEquipHotelList( hotelIdList );
                                }

                                // �i�荞�݂̃t���[���[�h�ōi�荞��
                                hotelIdList = searchHotelFreeWord.getSearchIdList( paramAndWord );

                                shcom.setResultHotelList( hotelIdList );

                                hotelIdList = shcom.getMargeHotel( pageRecords, pageNum );

                                // �\������
                                pageHeader = "�u" + paramAndWord + "�v�ōi���݌������܂���";
                            }
                        }
                        catch ( UnsupportedEncodingException e )
                        {
                            Logging.error( "[ActionFreeWordSearchMobile.execute() ] Exception=" + e.toString() );
                        }
                    }
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );

                    dataSearchHotel = searchHotelDao.getHotelInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();
                    queryString = "searchFreewordMobile.act?freeword=" + URLEncoder.encode( paramFreeword, "Shift_JIS" ) + "&pref_id=" + paramPrefId;
                    if ( paramAndWord != null )
                    {
                        queryString = queryString + "&andword=" + URLEncoder.encode( paramAndWord, "Shift_JIS" );
                    }

                    currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords, hotelAllCount, hotelCount, dispFormat );

                    dataSearchResult = new DataSearchResult_M2();

                    if ( hotelAllCount > pageRecords )
                    {
                        pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, pageRecords, hotelAllCount, queryString, paramUidLink );
                        dataSearchResult.setPageLink( pageLinks );
                    }

                    dataSearchResult.setRecordsOnPage( currentPageRecords );

                    if ( paramAndWord != null && (paramAndWord.trim().length() > 0) && hotelCount == 0 )
                    {
                        dataSearchResult.setRecordsOnPage( recordsNotFoundMsg2 );
                    }

                    dataSearchResult.setHotelCount( hotelCount );
                    dataSearchResult.setHotelAllCount( hotelAllCount );

                    dataSearchResult.setPageHeader( pageHeader );
                    dataSearchResult.setDataSearchHotel( dataSearchHotel );
                    dataSearchResult.setParamParameter1( paramFreeword );
                    dataSearchResult.setParamParameter2( "type4" );
                    dataSearchResult.setParamParameter3( paramPrefId );
                }
                else
                {
                    dataSearchResult = new DataSearchResult_M2();
                    dataSearchResult.setErrorOccurring( true );
                    dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                    dataSearchResult.setParamParameter1( paramFreeword );
                    dataSearchResult.setParamParameter2( "type0" );
                    dataSearchResult.setParamParameter3( paramPrefId );
                }
            }
            else
            {
                response.sendRedirect( "../../index.jsp?" + paramUidLink );
                return;
            }

            request.setAttribute( "SEARCH-RESULT", dataSearchResult );
            requestDispatcher = request.getRequestDispatcher( "search_result_freeword_M2.jsp" );

            requestDispatcher.forward( request, response );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Logging.error( "[ActionFreeWordSearchMobile.execute() ] Exception=" + e.toString() );
            try
            {
                dataSearchResult = new DataSearchResult_M2();
                dataSearchResult.setErrorOccurring( true );
                dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                dataSearchResult.setParamParameter1( paramFreeword );
                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "search_result_freeword_M2.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception exp )
            {
                Logging.error( "unable to dispatch.....=" + e.toString() );
            }
        }
        finally
        {
            searchHotelFreeWord = null;
            dataSearchHotel = null;
            dataSearchResult = null;
            searchHotelDao = null;
        }
    }
}
