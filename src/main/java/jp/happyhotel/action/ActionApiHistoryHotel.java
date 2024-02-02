package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.others.GenerateXmlAd;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.others.GenerateXmlSearchResultHotel;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.user.UserLoginInfo;

/**
 * �ŋߌ����z�e�������N���X�iAPI�j
 * 
 * @author S.Tashirro
 * @version 1.0 2011/04/26
 * 
 */

public class ActionApiHistoryHotel extends BaseAction
{

    static int                pageRecords       = Constants.pageLimitRecordMobile; // �\������
    static int                maxRecords        = Constants.maxRecordsMobile;     // �ő匏��
    static String             recordNotFound1   = Constants.errorRecordsNotFound1; // �����Ȃ��̏ꍇ�̃G���[���b�Z�[�W
    static String             recordsNotFound2  = Constants.errorRecordsNotFound2; // �����Ȃ��̏ꍇ�̃G���[���b�Z�[�W
    static String             recordsNotFound3  = Constants.errorRecordsNotFound3; // �ő匏���𒴂����ꍇ�̃G���[���b�Z�[�W
    static String             recordsNotFound4  = Constants.ERROR_MSG_API5;       // �K�{�p�����[�^�̕s��
    public static final int   DISP_MAX          = 200;

    private RequestDispatcher requestDispatcher = null;

    /**
     * �t���[���[�h�����iAPI�j
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int hotelCount = 0;
        int hotelAllCount = 0;
        int[] arrHotelIdList = null;
        String[] paramIdList = null;
        String errorMsg = "";
        String paramMethod = null;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        SearchHotelDao_M2 searchHotelDao = null;
        UserLoginInfo uli;

        // XML�o��
        boolean ret = false;
        GenerateXmlAd ad = new GenerateXmlAd();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );
            paramIdList = request.getParameterValues( "hotel_id" );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( paramIdList != null )
            {
                arrHotelIdList = new int[paramIdList.length];
                for( int i = 0 ; i < paramIdList.length ; i++ )
                {
                    arrHotelIdList[i] = Integer.parseInt( paramIdList[i] );
                }

                if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                {

                    // �z�e���ڍ׏��̎擾
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( arrHotelIdList, 0, 0 );

                    // ���ʂɂ��邽�߃f�[�^���Z�b�g����
                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();
                }
                else
                {
                    hotelAllCount = 0;
                    hotelCount = 0;
                    errorMsg = recordsNotFound2;
                }
            }
            else
            {
                // �K�{�p�����[�^�̃G���[
                hotelAllCount = 0;
                hotelCount = 0;
                errorMsg = recordsNotFound4;
            }

            // �������ʍ쐬
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( errorMsg );
            searchResult.setResultCount( hotelAllCount );
            // �L�����Z�b�g
            searchResult.setAd( ad );

            // �z�e���̏����Z�b�g
            for( int i = 0 ; i < hotelCount ; i++ )
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
            searchHeader.setName( "�ŋߌ����z�e��" );
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
            Logging.error( "[ActionApiHystoryHotel.execute() ] Exception:", exception );

            // �G���[���o��
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // �������ʃw�b�_�쐬
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "�ŋߌ����z�e��" );
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
                Logging.error( "[ActionApiHystoryHotel response]Exception:" + e.toString() );
            }
        }
        finally
        {
            arrDataSearchHotel = null;
        }
    }
}
