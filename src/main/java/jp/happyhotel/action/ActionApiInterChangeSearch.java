package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMapConvert;
import jp.happyhotel.data.DataMapPoint;
import jp.happyhotel.data.DataMapPoint_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.others.GenerateXmlAd;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.others.GenerateXmlSearchResultHotel;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelIc_M2;
import jp.happyhotel.sponsor.SponsorData_M2;
import jp.happyhotel.user.UserLoginInfo;

/**
 * IC�����iAPI�j
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/07
 */

public class ActionApiInterChangeSearch extends BaseAction
{
    static int                pageRecords         = Constants.pageLimitRecords;     // 1�y�[�W�ŕ\�����錏��
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2; // �����Ȃ��̏ꍇ�̃G���[���b�Z�[�W
    private RequestDispatcher requestDispatcher   = null;

    /**
     * IC�����i�g�сj
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
        int hotelIdList[] = null;
        String paramLocalId = null;
        String paramPrefId = null;
        String paramIcId = null;
        String paramAndWord = null;
        String paramPage = null;
        String errorMsg = "";
        String paramMethod = null;
        String icName = "";
        DataMapPoint_M2 dMapPoint = null;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        SearchHotelIc_M2 searchHotelIC = null;
        SearchHotelCommon searchHotelCommon = null;
        SearchHotelDao_M2 searchHotelDao = null;
        UserLoginInfo uli;

        // XML�o��
        boolean ret = false;
        SponsorData_M2 sd;
        sd = new SponsorData_M2();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramLocalId = request.getParameter( "local_id" );
            paramPrefId = request.getParameter( "pref_id" );
            paramIcId = request.getParameter( "ic_id" );
            paramAndWord = request.getParameter( "andword" );
            paramPage = request.getParameter( "page" );
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

            // CASE 1 :�C���^�[�`�F���WID�ŗv��
            if ( CheckString.isvalidString( paramIcId ) )
            {
                // �����Ђ̒n�}�R�[�h�������ꍇ�̓[�������̃R�[�h�ɕϊ��i#15057�Ή��j
                if ( paramIcId.matches( "(\\d+@\\d+,?)+" ) )
                {
                    DataMapConvert converter = new DataMapConvert();
                    if ( converter.getData( paramIcId ) )
                    {
                        paramIcId = converter.getConvertId();
                    }
                }

                pageNum = Integer.parseInt( paramPage );
                searchHotelIC = new SearchHotelIc_M2();

                // ���[�gID�Ńz�e�������擾
                hotelIdList = searchHotelIC.getSearchIdList( paramIcId );

                if ( paramAndWord != null && (paramAndWord.trim().length() > 0) )
                {
                    // ���݂̃z�e��ID���X�g���Z�b�g
                    searchHotelCommon = new SearchHotelCommon();
                    searchHotelCommon.setEquipHotelList( hotelIdList );
                    try
                    {
                        // paramAndWord = new String( paramAndWord.getBytes( "8859_1" ), "Windows-31J" );
                        // searchHotelFreeWord = new SearchHotelFreeword_M2();
                        // // �i�荞�݃t���[���[�h�Ō���
                        // hotelIdList = searchHotelFreeWord.getSearchIdList( paramAndWord, paramPrefId );
                        // searchHotelCommon.setResultHotelList( hotelIdList );
                        // // �}�[�W���s��
                        // hotelIdList = searchHotelCommon.getMargeHotel( pageRecords, pageNum );
                        //
                        // // �\������
                        // pageHeader = "�u" + paramAndWord + "�v�ōi���݌������܂���";

                    }
                    catch ( Exception e )
                    {
                        Logging.error( "[ActionAPIInterChangeSearch.execute() ] UnsupportedEncodingException =" + e.toString() );
                        throw e;
                    }
                }

                // �z�e���ڍ׏��̎擾
                searchHotelDao = new SearchHotelDao_M2();
                searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );
                searchHotelIC.getMapPointInfo( paramIcId );

                // �f�[�^�̃Z�b�g
                arrDataSearchHotel = searchHotelDao.getHotelInfo();
                dMapPoint = searchHotelIC.getIcInfo();
                hotelCount = searchHotelDao.getCount();
                hotelAllCount = searchHotelDao.getAllCount();
                icName = dMapPoint.getName();
            }
            else
            {
                hotelAllCount = 0;
                hotelCount = 0;
                errorMsg = Constants.errorRecordsNotFound2;
            }

            // �������ʍ쐬
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( errorMsg );
            searchResult.setResultCount( hotelAllCount );

            // �s���{���R�[�h�������Ă��Ȃ��ꍇ���l������ICID����s���{��ID���擾����
            if ( Integer.parseInt( paramPrefId ) == 0 && paramIcId.equals( "" ) == false )
            {
                DataMapPoint dmap = new DataMapPoint();
                dmap.getData( paramIcId );
                paramPrefId = Integer.toString( dmap.getJisCode() / 1000 );
            }

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
                    ad.setAdInfo2( sd.getSponsor()[i], request );
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
            searchHeader.setName( icName );
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
            Logging.error( "[ActionApiInterChangeSearch.execute() ] Exception:", exception );

            // �G���[���o��
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // �������ʃw�b�_�쐬
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "IC����" );
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
                Logging.error( "[ActionApiInterChangeSearch response]Exception:" + e.toString() );
            }
        }
        finally
        {
            dMapPoint = null;
            arrDataSearchHotel = null;
            searchHotelIC = null;
            searchHotelCommon = null;
            searchHotelDao = null;
        }
    }
}
