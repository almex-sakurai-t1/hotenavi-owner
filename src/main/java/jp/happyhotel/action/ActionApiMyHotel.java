package jp.happyhotel.action;

import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApUuidUser;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataUserMyHotel;
import jp.happyhotel.hotel.HotelDetail;
import jp.happyhotel.others.GenerateXmlAd;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.others.GenerateXmlSearchResultHotel;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.sponsor.SponsorData_M2;
import jp.happyhotel.user.UserLoginInfo;
import jp.happyhotel.user.UserMyHotel;

public class ActionApiMyHotel extends BaseAction
{
    static int pageRecords = Constants.pageLimitRecords;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int DISP_COUNT = 1;
        final int DISP_FLAG = 2;

        String paramUserId = null;
        String paramMethod = null;
        String paramUuid = null;
        int paramType = 0;
        String paramPage = null;
        int appStatus = 0;
        UserLoginInfo uli;
        UserMyHotel myhotel = null;
        DataUserMyHotel[] hotelList = null;
        int hotelidList[] = null;
        int hotelCount = 0;
        int hotelAllCount = 0;
        String customId = "";
        Map<Integer, String> customHotelList = null;
        SearchHotelDao_M2 searchHotelDao = null;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        String errorMsg = "";
        boolean ret = false;
        SponsorData_M2 sd = new SponsorData_M2();
        GenerateXmlAd ad = new GenerateXmlAd();

        try
        {
            Logging.info( "ActionApiMyHotel.execute start" );

            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );
            paramUuid = request.getParameter( "uuid" );
            paramPage = request.getParameter( "page" );

            if ( CheckString.numCheck( request.getParameter( "type" ) ) == true )
            {
                paramType = Integer.parseInt( request.getParameter( "type" ) );
                Logging.info( "ActionApiMyHotel.execute type = " + paramType );
            }

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }
            if ( paramUserId == null )
            {
                paramUserId = "";
            }

            Logging.info( "ActionApiMyHotel.execute uuid = " + paramUuid );

            if ( paramPage == null || paramPage.equals( "" ) != false || CheckString.numCheck( paramPage ) == false )
            {
                paramPage = "0";
            }

            if ( uli.getUserInfo() != null )
            {
                paramUserId = uli.getUserInfo().getUserId();

                // TODO �}�C�z�e���Ƀ����o�[�ڋq�����擾���镔����ǉ�����B
                myhotel = new UserMyHotel();

                DataApUuidUser dauu = new DataApUuidUser();
                dauu.getData( paramUuid, paramUserId );
                appStatus = dauu.getAppStatus();

                Logging.info( "ActionApiMyHotel.execute appStatus = " + appStatus );

                // ���O�A�E�g��
                // if ( appStatus == 0 )
                // {
                // hotelCount = 0;
                // hotelAllCount = 0;
                // if ( paramType == 0 )
                // {
                // errorMsg = Constants.errorRecordsMyHotel;

                // }
                // else
                // {
                // errorMsg = Constants.errorRecordsHotelMembers;
                // }
                // }
                // ���O�C����
                // else
                {
                    // �z�e�������o�[�Ƃ̕R�t�Ȃ�
                    if ( paramType == 0 )
                    {
                        myhotel.getMyHotelList( uli.getUserInfo().getUserId() );
                    }
                    // �z�e�������o�[�Ƃ̕R�t����
                    else
                    {
                        myhotel.getMyHotelListWithMembers( uli.getUserInfo().getUserId() );
                    }

                    hotelList = myhotel.getMyHotel();

                    if ( hotelList != null && hotelList.length > 0 )
                    {
                        hotelidList = new int[hotelList.length];

                        // ϲ��ق����ID��S�擾
                        for( int i = 0 ; i < hotelList.length ; i++ )
                        {
                            hotelidList[i] = hotelList[i].getHotelId();
                        }
                        // ����ް��擾
                        searchHotelDao = new SearchHotelDao_M2();
                        searchHotelDao.getHotelList( hotelidList, pageRecords, Integer.parseInt( paramPage ) );
                        arrDataSearchHotel = searchHotelDao.getHotelInfo();
                        hotelCount = searchHotelDao.getCount();
                        hotelAllCount = searchHotelDao.getAllCount();
                    }
                    if ( hotelCount == 0 )
                    {
                        if ( paramType == 0 )
                        {
                            errorMsg = Constants.errorRecordsMyHotel;

                        }
                        else
                        {
                            errorMsg = Constants.errorRecordsHotelMembers;
                        }
                    }
                }
            }
            else
            {
                hotelCount = 0;
                hotelAllCount = 0;
                errorMsg = Constants.ERROR_MSG_API16;
            }

            ret = sd.getAdData( 0, DISP_COUNT, DISP_FLAG );
            if ( ret != false )
            {
                sd.setImpressionCountForSmart( sd.getSponsor()[0].getSponsorCode() );
                // �L���p��XML��ǉ�
                ad.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                ad.setAdInfo( sd.getSponsor()[0] ,request);
            }

            // �������ʍ쐬
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( errorMsg );
            searchResult.setResultCount( hotelAllCount );
            // �L�����Z�b�g
            searchResult.setAd( ad );

            // �ڋq�o�^�ς̃}�C�z�e�����X�g�擾
            myhotel.getCustomMyHotelList( uli.getUserInfo().getUserId() );
            customHotelList = myhotel.getCustomMyHotel();

            // �z�e���̏����Z�b�g
            for( int i = 0 ; i < hotelCount ; i++ )
            {
                GenerateXmlSearchResultHotel addHotel = new GenerateXmlSearchResultHotel();
                // addHotel.addHotelInfo( arrDataSearchHotel[i], uli.isPaymemberFlag(), 0 );
                customId = customHotelList.get( arrDataSearchHotel[i].getId() );
                if ( customId == null )
                {
                    customId = "";
                }
                if ( !customId.equals( "" ) )
                {
                    if ( HotelDetail.isHotenaviCustom( arrDataSearchHotel[i].getId() ) == false )
                    {
                        // customId = "";
                    }
                }
                addHotel.addMyHotelInfo( arrDataSearchHotel[i], uli.isPaymemberFlag(), 0, customId );

                // �������ʂɃz�e������ǉ�
                searchResult.addHotel( addHotel );
            }

            // �������ʃw�b�_�쐬
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "�}�C�z�e��" );
            searchHeader.setAndword( "" );
            searchHeader.setCount( hotelCount );
            // �������ʃm�[�h���������ʃw�b�_�[�m�[�h�ɒǉ�
            searchHeader.setSearchResult( searchResult );

            String xmlOut = searchHeader.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiMyHotel.execute() ] Exception:", exception );

            // �G���[���o��
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // �������ʃw�b�_�쐬
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "�}�C�z�e��" );
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
                Logging.error( "[ActionApiMyHotel response]Exception:" + e.toString() );
            }
        }
        finally
        {
            Logging.info( "ActionApiMyHotel.execute end" );
        }
    }
}
