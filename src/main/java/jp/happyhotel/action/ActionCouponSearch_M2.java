package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchMasterEquip_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchEngineBasic_M2;
import jp.happyhotel.search.SearchHotelCoupon_M2;
import jp.happyhotel.search.SearchHotelDao_M2;

/**
 * 
 * �N�[�|�������N���X
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/15
 * 
 */

public class ActionCouponSearch_M2 extends BaseAction
{

    static int                pageRecords         = Constants.pageLimitRecords;     // �\������
    static int                maxRecords          = Constants.maxRecords;           // �ő匏��
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2; // �����Ȃ��̏ꍇ�̃G���[���b�Z�[�W
    static String             LimitRecords        = Constants.errorLimitRecords;    // �ő匏���𒴂����ꍇ�̃G���[���b�Z�[�W
    private String            actionURL           = "searchCoupon.act";
    private RequestDispatcher requestDispatcher   = null;

    /**
     * �N�[�|������
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     * @see "�n��ID��search_result_coupon_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {

        boolean memberFlag = false;
        int hotelCount;
        int hotelAllCount;
        int pageNum;
        int masterEquipCount = 0;
        int nType;
        int registStatus = 0;
        int[] arrHotelIdList = null;
        String paramPage;
        String paramLocalId;
        String queryString;
        String currentPageRecords;
        String pageLinks = "";
        SearchHotelCoupon_M2 searchHotelCoupon;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataSearchMasterEquip_M2[] arrDataSearchMasterEquip = null;
        DataLoginInfo_M2 dataLoginInfo = null;
        SearchEngineBasic_M2 searchEngineBasic;
        SearchHotelDao_M2 searchHotelDao = null;

        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 )
            {
                response.sendRedirect( requestURL.replace( "http://", "https://" ) + (request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" )) );
                return;
            }

            paramPage = request.getParameter( "page" );
            paramLocalId = request.getParameter( "local_id" );

            dataLoginInfo = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

            if ( dataLoginInfo != null )
            {
                memberFlag = dataLoginInfo.isMemberFlag();
                registStatus = dataLoginInfo.getRegistStatus();
            }

            if ( memberFlag != false && registStatus == 9 )
            {
                nType = 1;
            }
            else
            {
                nType = 2;
            }
            searchHotelCoupon = new SearchHotelCoupon_M2();
            if ( !CheckString.numCheck( paramPage ) )
            {
                paramPage = "0";
            }
            pageNum = Integer.parseInt( paramPage );

            // �ݔ����̎擾
            searchEngineBasic = new SearchEngineBasic_M2();
            boolean isEquipListFound = false;
            isEquipListFound = searchEngineBasic.getEquipList( false );
            if ( isEquipListFound != false )
            {
                masterEquipCount = searchEngineBasic.getMasterEquipCount();
                arrDataSearchMasterEquip = searchEngineBasic.getMasterEquip();
            }

            // �z�e��ID���X�g���擾
            arrHotelIdList = searchHotelCoupon.getHotelIdList( paramLocalId, nType, false );

            // �z�e���ڍ׏����擾
            searchHotelDao = new SearchHotelDao_M2();
            searchHotelDao.getHotelList( arrHotelIdList, pageRecords, pageNum );
            // ���ʂɂ��邽�߃f�[�^���Z�b�g����
            arrDataSearchHotel = searchHotelDao.getHotelInfo();
            hotelCount = searchHotelDao.getCount();
            hotelAllCount = searchHotelDao.getAllCount();

            currentPageRecords = PagingDetails.getPageRecords( pageNum, pageRecords, hotelAllCount, hotelCount );
            queryString = "local_id=" + paramLocalId;
            actionURL = actionURL + "?" + queryString;

            // ���X��200���ȏ�͕\�����Ȃ��d�l���������A�֓���200���𒴂��Ă��邽�߂ɐ������͂���
            if ( hotelAllCount >= 0 )
            {
                if ( hotelAllCount == 0 )
                {
                    dataSearchResult = new DataSearchResult_M2();

                    dataSearchResult.setErrorOccurring( true );
                    dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );

                }
                else
                {
                    dataSearchResult = new DataSearchResult_M2();
                    if ( hotelAllCount > pageRecords )
                    {
                        pageLinks = PagingDetails.getPagenationLink( pageNum, pageRecords, hotelAllCount, actionURL );
                        dataSearchResult.setPageLink( pageLinks );
                    }
                    dataSearchResult.setRecordsOnPage( currentPageRecords );
                    dataSearchResult.setHotelCount( hotelCount );
                    dataSearchResult.setDataSearchHotel( arrDataSearchHotel );

                }
            }
            else
            {
                dataSearchResult = new DataSearchResult_M2();
                dataSearchResult.setErrorOccurring( true );
                dataSearchResult.setErrorMessage( LimitRecords );

            }
            dataSearchResult.setParamParameter1( paramLocalId );
            dataSearchResult.setMasterEquipCount( masterEquipCount );
            dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );

            request.setAttribute( "SEARCH-RESULT", dataSearchResult );
            if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
            {
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_coupon_M2.jsp" );
            }
            else
            {
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_coupon_M2.jsp" );
            }
            requestDispatcher.forward( request, response );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionCouponSearch_M2.execute() ] Exception", e );
            try
            {
                dataSearchResult = new DataSearchResult_M2();
                dataSearchResult.setErrorOccurring( true );
                dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );

                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_coupon_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_coupon_M2.jsp" );
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
            searchHotelCoupon = null;
            arrDataSearchHotel = null;
            dataSearchResult = null;
            searchHotelDao = null;
        }
    }
}
