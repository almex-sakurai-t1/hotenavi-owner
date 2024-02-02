package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.others.GenerateXmlAd;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.others.GenerateXmlSearchResultHotel;
import jp.happyhotel.search.SearchHotelArea_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.sponsor.SponsorData_M2;
import jp.happyhotel.user.UserLoginInfo;

/**
 * �z�e���G���A�����N���X�iAPI�j
 *
 * @author S.Tashiro
 * @version 1.0 2011/04/07
 */

public class ActionApiHotelAreaSearch extends BaseAction
{

    static int              pageRecords     = Constants.pageLimitRecords;
    static int              maxRecords      = Constants.maxRecordsMobile;
    static String           recordNotFound2 = Constants.errorRecordsNotFound2;
    public static final int dispFormat      = 1;

    /**
     * �z�e���G���A�����iAPI�j
     *
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int DISP_COUNT = 5;
        final int DISP_FLAG = 2;
        int i = 0;
        int hotelCount;
        int hotelAllCount;
        int pageNum = 0;
        int areaId;
        int[] arrHotelIdList = null;
        String areaName = "";
        String paramAreaId = null;
        String paramPrefId = null;
        String paramLocalId = null;
        String paramPage;
        String paramAndWord;
        String errorMsg = "";
        String paramMethod = null;
        SearchHotelArea_M2 searchHotelArea;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        SearchHotelCommon searchHotelCommon = null;
        SearchHotelDao_M2 searchHotelDao = null;

        UserLoginInfo uli;

        // XML�o��
        boolean ret = false;
        SponsorData_M2 sd;
        sd = new SponsorData_M2();

        try
        {
            searchHotelArea = new SearchHotelArea_M2();
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramPrefId = request.getParameter( "pref_id" );
            paramAreaId = request.getParameter( "area_id" );
            paramLocalId = request.getParameter( "local_id" );
            paramPage = request.getParameter( "page" );
            paramAndWord = request.getParameter( "andword" );
            paramMethod = request.getParameter( "method" );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( paramPage == null || paramPage.equals( "" ) != false || CheckString.numCheck( paramPage ) == false )
            {
                paramPage = "0";
            }
            if ( paramLocalId == null || paramLocalId.equals( "" ) != false || CheckString.numCheck( paramLocalId ) == false )
            {
                paramLocalId = "0";
            }
            if ( paramPrefId == null || paramPrefId.equals( "" ) != false || CheckString.numCheck( paramPrefId ) == false )
            {
                paramPrefId = "0";
            }

            if ( CheckString.numCheck( paramAreaId ) && CheckString.numCheck( paramPage ) != false )
            {
                pageNum = Integer.parseInt( paramPage );
                areaId = Integer.parseInt( paramAreaId );

                searchHotelArea.getHotelAreaDetail( areaId );
                areaName = searchHotelArea.getAreaName();

                // �z�e���G���AID����f�[�^�擾
                arrHotelIdList = searchHotelArea.getSearchIdList( areaId );

                searchHotelCommon = new SearchHotelCommon();
                searchHotelCommon.setEquipHotelList( arrHotelIdList );

                if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                {
                    // �z�e���ڍ׏����擾
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( arrHotelIdList, pageRecords, pageNum );

                    // ���ʂɂ��邽�߃f�[�^���Z�b�g����
                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();

                }
                else
                {
                    hotelCount = 0;
                    hotelAllCount = 0;
                    errorMsg = recordNotFound2;
                }

            }
            else
            {
                hotelCount = 0;
                hotelAllCount = 0;
                errorMsg = recordNotFound2;
            }

            // �������ʍ쐬
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( errorMsg );
            searchResult.setResultCount( hotelAllCount );

            // ���[�e�[�V�����o�i�[�擾
            ret = sd.getAdRandomData( Integer.parseInt( paramPrefId ), DISP_COUNT, DISP_FLAG );

            if ( ret != false )
            {

                for( i = 0 ; i < sd.getSponsor().length ; i++ )
                {
                    GenerateXmlAd ad = new GenerateXmlAd();
                    // �X�}�[�g�t�H���̕\������ǉ�
                    sd.setImpressionCountForSmart( sd.getSponsor()[i].getSponsorCode() );

                    // �L���p��XML��ǉ�
                    ad.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                    ad.setAdInfo2( sd.getSponsor()[i] ,request);
                    searchResult.addAd( ad );

                }
            }

            // �z�e���̏����Z�b�g
            for( i = 0 ; i < hotelCount ; i++ )
            {
                GenerateXmlSearchResultHotel addHotel = new GenerateXmlSearchResultHotel();
                // �z�e�������Z�b�g
                addHotel.addHotelInfo( arrDataSearchHotel[i], uli.isPaymemberFlag(), 0 );
                // �������ʃm�[�h�Ƀz�e���m�[�h��ǉ�
                searchResult.addHotel( addHotel );
            }

            // �������ʃw�b�_�쐬
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( areaName );
            searchHeader.setAndword( paramAndWord );
            searchHeader.setCount( hotelCount );
            // �������ʃm�[�h���������ʃw�b�_�[�m�[�h�ɒǉ�
            searchHeader.setSearchResult( searchResult );

            String xmlOut = searchHeader.createXml();

            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiHotelAreaSearch.execute() ] Exception:", exception );

            // �G���[���o��
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // �������ʃw�b�_�쐬
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "�z�e���G���A����" );
            searchHeader.setCount( 0 );
            // �z�e���ڍׂ�ǉ�
            searchHeader.setSearchResult( searchResult );

            String xmlOut = searchHeader.createXml();
            ServletOutputStream out = null;

            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiHotelAreaSearch response]Exception:" + e.toString() );
            }
        }
        finally
        {
            arrDataSearchHotel = null;
            searchHotelDao = null;
        }
    }

}
