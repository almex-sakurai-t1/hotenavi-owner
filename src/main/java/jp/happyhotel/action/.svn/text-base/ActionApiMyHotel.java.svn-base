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

                // TODO マイホテルにメンバー顧客情報を取得する部分を追加する。
                myhotel = new UserMyHotel();

                DataApUuidUser dauu = new DataApUuidUser();
                dauu.getData( paramUuid, paramUserId );
                appStatus = dauu.getAppStatus();

                Logging.info( "ActionApiMyHotel.execute appStatus = " + appStatus );

                // ログアウト中
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
                // ログイン中
                // else
                {
                    // ホテルメンバーとの紐付なし
                    if ( paramType == 0 )
                    {
                        myhotel.getMyHotelList( uli.getUserInfo().getUserId() );
                    }
                    // ホテルメンバーとの紐付あり
                    else
                    {
                        myhotel.getMyHotelListWithMembers( uli.getUserInfo().getUserId() );
                    }

                    hotelList = myhotel.getMyHotel();

                    if ( hotelList != null && hotelList.length > 0 )
                    {
                        hotelidList = new int[hotelList.length];

                        // ﾏｲﾎﾃﾙのﾎﾃﾙIDを全取得
                        for( int i = 0 ; i < hotelList.length ; i++ )
                        {
                            hotelidList[i] = hotelList[i].getHotelId();
                        }
                        // ﾎﾃﾙﾃﾞｰﾀ取得
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
                // 広告用のXMLを追加
                ad.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                ad.setAdInfo( sd.getSponsor()[0] ,request);
            }

            // 検索結果作成
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( errorMsg );
            searchResult.setResultCount( hotelAllCount );
            // 広告をセット
            searchResult.setAd( ad );

            // 顧客登録済のマイホテルリスト取得
            myhotel.getCustomMyHotelList( uli.getUserInfo().getUserId() );
            customHotelList = myhotel.getCustomMyHotel();

            // ホテルの情報をセット
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

                // 検索結果にホテル情報を追加
                searchResult.addHotel( addHotel );
            }

            // 検索結果ヘッダ作成
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "マイホテル" );
            searchHeader.setAndword( "" );
            searchHeader.setCount( hotelCount );
            // 検索結果ノードを検索結果ヘッダーノードに追加
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

            // エラーを出力
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // 検索結果ヘッダ作成
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "マイホテル" );
            searchHeader.setCount( 0 );
            // ホテル詳細を追加
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
