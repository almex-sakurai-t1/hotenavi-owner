package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataEquipListResult_M2;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterName;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchMasterEquip_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.data.DataUserDetailQuery;
import jp.happyhotel.search.SearchEngineBasic_M2;
import jp.happyhotel.search.SearchKodawari_M2;
import jp.happyhotel.user.UserBasicInfo;

/**
 * こだわり検索クラス（携帯）
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/26
 */

public class ActionKodawariSearchMobile_M2 extends BaseAction
{

    private RequestDispatcher requestDispatcher;

    private String            actionURL           = "searchKodawariMobile.act?";
    static int                pageRecords         = Constants.pageLimitRecordMobile;
    static int                maxRecords          = Constants.maxRecordsMobile;
    static final int          dispFormat          = 2;
    static String             recordsNotFoundMsg1 = Constants.errorRecordsNotFound1;
    static int[][]            pref_ID             = { { 1, 2, 3, 4, 5, 6, 7 },
                                                  { 8, 9, 10, 11, 12, 13, 14 },
                                                  { 15, 19, 20 },
                                                  { 21, 22, 23, 24 },
                                                  { 16, 17, 18 },
                                                  { 25, 26, 27, 28, 29, 30 },
                                                  { 31, 32, 33, 34, 35 },
                                                  { 36, 37, 38, 39 },
                                                  { 40, 41, 42, 43, 44, 45, 46, 47 }
                                                  };

    /**
     * こだわり検索クラス（携帯）
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "非会員→index_M2.jsp<br>
     *      有料途中会員→../../free/mymenu/paymemberRegist.act<br>
     *      GPSパラメータあり→carrier_distinction_M2.jsp<br>
     *      以下こだわり検索の条件があることを前提とする<br>
     *      地方IDなし、都道府県IDなし、GPSパラメータなし→search_detail_local_M2.jsp<br>
     *      地方ID→search_detail_pref_M2.jsp<br>
     *      都道府県ID→search_result_kodawari_M2.jsp
     *      GPSパラメータ→search_result_kodawari_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean memberFlag;
        boolean paymemberFlag;
        boolean paymemberTempFlag;
        boolean ret;
        boolean yMemberFlag = false;
        int i;
        int registStatus = 0;
        int delFlag;
        int carrierFlag;
        String userId;
        int[] amenityId;
        String[] paramAmenity;
        String paramAcRead;
        String paramUidLink = null;
        SearchEngineBasic_M2 searchEngineBasic;
        DataEquipListResult_M2 dataEquipListResult;
        DataSearchMasterEquip_M2[] dataSearchMasterEquip;
        DataMasterName[] dataMasterName;
        DataLoginInfo_M2 dataLoginInfo_M2;
        AuAuthCheck auCheck;
        String termNo = "";
        UserBasicInfo ubi;
        Cookie[] cookies;
        Cookie hhYhappy = null;
        String canSearch = "";
        boolean canSearchFlag = false;
        int loop = 0;
        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 && UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_SMARTPHONE )
            {
                response.sendRedirect( requestURL.replace( "http://", "https://" ) + (request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" )) );
                return;
            }

            // PCからのアクセスならPC用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType == UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getPCUrl( request ) );
                return;
            }

            canSearch = request.getParameter( "canSearchFlag" );
            if ( canSearch != null && canSearch.equals( "true" ) )
            {
                canSearchFlag = true;
            }

            Logging.info( "画面遷移app=" + canSearchFlag, "[ActionKodawari_mobile dataLoginInfo_M2] :" );

            // cookieの確認
            cookies = request.getCookies();
            if ( cookies != null )
            {
                for( loop = 0 ; loop < cookies.length ; loop++ )
                {
                    if ( cookies[loop].getName().compareTo( "hhyhappy" ) == 0 )
                    {
                        hhYhappy = cookies[loop];
                        break;
                    }
                }
            }
            if ( hhYhappy != null )
            {
                yMemberFlag = true;
            }

            if ( request.getParameter( "search" ) != null )
            {
                dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

                paramUidLink = (String)request.getAttribute( "UID-LINK" );

                // auだったらアクセスチケットをチェックする
                paramAcRead = request.getParameter( "acread" );
                carrierFlag = UserAgent.getUserAgentType( request );
                if ( (paramAcRead == null) && (carrierFlag == DataMasterUseragent.CARRIER_AU) )
                {
                    try
                    {
                        auCheck = new AuAuthCheck();
                        ret = auCheck.authCheckForClass( request, false );
                        // アクセスチケット確認の結果 falseだったらリダイレクト
                        if ( ret == false )
                        {
                            response.sendRedirect( auCheck.getResultData() );
                            return;
                        }
                        // アクセスチケット確認の結果 trueだったら情報を取得
                        else
                        {
                            // DataLoginInfo_M2を取得する
                            if ( auCheck.getDataLoginInfo() != null )
                            {
                                dataLoginInfo_M2 = auCheck.getDataLoginInfo();
                            }
                            Logging.info( "mobileTermNo:" + auCheck.getUbi().getUserInfo().getMobileTermNo() );
                            Logging.info( "RS_PAY:" + auCheck.getUbi().getUserInfo().getRegistStatusPay() );
                        }
                    }
                    catch ( Exception e )
                    {
                        Logging.info( "[ActionEmptySearch AuAuthCheck] Exception:" + e.toString() );
                    }
                }
                if ( dataLoginInfo_M2 != null )
                {
                    memberFlag = dataLoginInfo_M2.isMemberFlag();
                    paymemberFlag = dataLoginInfo_M2.isPaymemberFlag();
                    paymemberTempFlag = dataLoginInfo_M2.isPaymemberTempFlag();
                    registStatus = dataLoginInfo_M2.getRegistStatus();
                    delFlag = dataLoginInfo_M2.getDelFlag();
                    userId = dataLoginInfo_M2.getUserId();

                    // 有料会員途中のユーザーの場合、リダイレクト
                    if ( paymemberFlag == false && paymemberTempFlag != false )
                    {
                        // response.sendRedirect( redirectUrl );
                        // return;
                    }
                    if ( !memberFlag || registStatus != 9 || delFlag == 1 )
                    {
                        if ( canSearchFlag )
                        {
                            // 何もしない
                        }
                        else
                        {
                            response.sendRedirect( "index_M2.jsp" );
                            return;
                        }

                    }
                    if ( request.getParameter( "gps" ) != null )
                    {
                        searchGps( request, response, userId );
                        return;
                    }
                }
                else if ( yMemberFlag == false )
                {
                    if ( canSearchFlag )
                    {
                        // 何もしない
                    }
                    else
                    {
                        response.sendRedirect( "index_M2.jsp" );
                        return;
                    }

                }

                Logging.info( "検索画面app=" + canSearchFlag, "[ActionKodawari_mobile dataLoginInfo_M2] :" );
                searchHotels( request, response );
                return;
            }
            // 非会員→有料会員(ID/Pass未取得)対応 2009/10/09 ide
            dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            Logging.info( "[ActionKodawari_mobile dataLoginInfo_M2] :" + dataLoginInfo_M2 );

            if ( dataLoginInfo_M2 != null )
            {
                memberFlag = dataLoginInfo_M2.isMemberFlag();
                paymemberFlag = dataLoginInfo_M2.isPaymemberFlag();
                paymemberTempFlag = dataLoginInfo_M2.isPaymemberTempFlag();

                // 有料会員途中のユーザーの場合、リダイレクト
                if ( paymemberFlag == false && paymemberTempFlag != false )
                {
                    // response.sendRedirect( redirectUrl );
                    // return;
                }
            }
            else if ( yMemberFlag == false )
            {
                ubi = new UserBasicInfo();
                // termNoの取得
                carrierFlag = UserAgent.getUserAgentType( request );
                if ( carrierFlag == UserAgent.USERAGENT_AU )
                {
                    termNo = request.getHeader( "x-up-subno" );
                }
                else if ( carrierFlag == UserAgent.USERAGENT_VODAFONE )
                {
                    termNo = request.getHeader( "x-jphone-uid" );
                    if ( termNo != null )
                    {
                        termNo = termNo.substring( 1 );
                    }
                }
                else if ( carrierFlag == UserAgent.USERAGENT_DOCOMO )
                {
                    termNo = request.getParameter( "uid" );
                }
                if ( termNo != null )
                {
                    if ( ubi.getUserBasicByTermnoNoCheck( termNo ) != false && ubi != null )
                    {
                        Logging.info( "ActionKodawari_mobile ubi.getUserBasicByTermnoNoCheck( termNo )=" + ubi.getUserBasicByTermnoNoCheck( termNo ) );

                        if ( ubi.getUserInfo().getRegistStatusPay() == 1 )
                        {
                            // response.sendRedirect( redirectUrl );
                            // return;
                        }
                    }
                }
            }
            // 有料半端会員対応ここまで

            paramAmenity = request.getParameterValues( "amenity" );
            if ( paramAmenity != null )
            {
                amenityId = new int[paramAmenity.length];
                for( i = 0 ; i < paramAmenity.length ; i++ )
                {
                    amenityId[i] = Integer.parseInt( paramAmenity[i] );
                }
            }
            else
            {
                amenityId = new int[0];
            }
            searchEngineBasic = new SearchEngineBasic_M2();

            // 設備一覧を取得
            searchEngineBasic.getEquipClassGroup( true );
            if ( searchEngineBasic.getMasterEquipCount() > 0 )
            {
                dataEquipListResult = new DataEquipListResult_M2();
                dataEquipListResult.setDataEquipListDto( searchEngineBasic.getMasterEquip() );
                dataEquipListResult.setAmenityId( amenityId );
                dataSearchMasterEquip = dataEquipListResult.getDataEquipListDto();
                dataMasterName = searchEngineBasic.getMasterEquipClassName();

                for( int j = 0 ; j < dataSearchMasterEquip.length ; j++ )
                {
                    dataSearchMasterEquip[j].setEquipClassNameKana( dataMasterName[j].getNameKana() );
                }
                dataEquipListResult.setDataEquipListDto( searchEngineBasic.getMasterEquip() );
                dataEquipListResult.setEquipClassNameKana( dataMasterName[0].getNameKana() );
                request.setAttribute( "EQUIP-LIST", dataEquipListResult );
            }
            Logging.info( "app=" + canSearchFlag, "[ActionKodawariSearchMobile_M2.execute(REQ,RESP) ]" );
            if ( canSearchFlag )
            {
                requestDispatcher = request.getRequestDispatcher( "index_M2_renewal.jsp" );
                Logging.info( "forward(index_M2_renewal.jsp)", "[ActionKodawariSearchMobile_M2.execute(REQ,RESP) ]" );
            }
            else
            {
                requestDispatcher = request.getRequestDispatcher( "index_M2.jsp" );
                Logging.info( "forward(index_M2.jsp)", "[ActionKodawariSearchMobile_M2.execute(REQ,RESP) ]" );
            }
            requestDispatcher.forward( request, response );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionKodawariSearchMobile_M2.execute(REQ,RESP) ] Exception ", e );
        }
        finally
        {
            searchEngineBasic = null;
            dataEquipListResult = null;
        }
    }

    private void searchGps(HttpServletRequest request, HttpServletResponse response, String userId) throws Exception
    {

        String paramKind = request.getParameter( "kind" );
        String queryString = "";
        DataUserDetailQuery dataUserDetailQuery;

        try
        {
            if ( paramKind != null )
            {
                if ( CheckString.numCheck( paramKind ) != false )
                {
                    if ( Integer.parseInt( paramKind ) < 0 && Integer.parseInt( paramKind ) >= 6 )
                        paramKind = "0";
                }
                else
                    paramKind = "0";
            }
            else
            {
                paramKind = "0";
            }
            queryString = queryString + "&kind=" + paramKind + SearchKodawari_M2.makeQueryString( request );
            dataUserDetailQuery = new DataUserDetailQuery();
            dataUserDetailQuery.setUserId( userId );
            dataUserDetailQuery.setQueryString( queryString );
            dataUserDetailQuery.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dataUserDetailQuery.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dataUserDetailQuery.insertData();
            request.setAttribute( "QUERY-STRING", queryString );
            requestDispatcher = request.getRequestDispatcher( "carrier_distinction_M2.jsp" );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionKodawariSearchMobile_M2.searchGps(REQ,RESP," + userId + ") ] Exception ", e );
        }
        finally
        {
            dataUserDetailQuery = null;
        }
    }

    /**
     * ホテル検索ロジック
     * 
     * @param request
     * @param response
     */
    private void searchHotels(HttpServletRequest request, HttpServletResponse response)
    {

        int hotelCount;
        int hotelAllCount;
        int pageNum = 0;
        int prefId;
        int[] prefIds;
        int[] prefHotelCounts = null;
        String paramPage;
        String paramGps;
        String paramLocalId;
        String paramPrefId;
        String paramKind;
        String paramUidLink = null;
        String strKodawari = "";
        String currentPageRecords;
        String pageLinks = "";
        String queryString = "";
        String canSearch = "";
        boolean canSearchFlag = false;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        SearchKodawari_M2 searchKodawari;

        canSearch = request.getParameter( "canSearchFlag" );
        if ( canSearch != null && canSearch.equals( "true" ) )
        {
            canSearchFlag = true;
        }

        try
        {
            Logging.info( "こだわり検索開始app=" + canSearchFlag, "[ActionKodawariSearch_M2_Mobile.class]serachHotels:" );

            paramPage = request.getParameter( "page" );
            paramLocalId = request.getParameter( "local_id" );
            paramPrefId = request.getParameter( "pref_id" );
            paramKind = request.getParameter( "kind" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            paramGps = request.getParameter( "byGps" );

            if ( paramPrefId != null && paramPrefId.trim().length() > 0 && CheckString.numCheck( paramPrefId ) )
            {
                prefId = Integer.parseInt( paramPrefId );
            }
            else
            {
                prefId = 0;
            }
            if ( paramLocalId == null && (paramPrefId == null || prefId == 0) && paramGps == null )
            {
                Logging.info( "地方選択app=" + canSearchFlag, "[ActionKodawariSearch_M2_Mobile.class]serachHotels:" );
                if ( paramKind != null )
                {
                    if ( CheckString.numCheck( paramKind ) != false )
                    {
                        if ( Integer.parseInt( paramKind ) == 1 )
                            strKodawari = "<font color=\"red\">「予約」</font>のできるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 2 )
                            strKodawari = "<font color=\"red\">「岩盤浴」</font>のあるﾎﾃﾙ";
                        else if ( Integer.parseInt( paramKind ) == 3 )
                            strKodawari = "<font color=\"red\">「露天風呂」</font>のあるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 4 )
                            strKodawari = "<font color=\"red\">「温泉」</font>のあるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 5 )
                            strKodawari = "<font color=\"red\">「1人」</font>で泊まれるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 6 )
                            strKodawari = "<font color=\"red\">「3人以上」</font>で泊まれるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 7 )
                            strKodawari = "<font color=\"red\">「ｺｽﾌﾟﾚ」</font>できるﾎﾃﾙ";
                        else
                            paramKind = "0";

                        queryString = queryString + "&kind=" + paramKind;
                    }
                    else
                        paramKind = "0";
                }
                else
                {
                    paramKind = "0";
                    strKodawari = "";
                }

                if ( canSearchFlag )
                {
                    queryString = queryString + "&canSearchFlag=true" + SearchKodawari_M2.makeQueryString( request );
                }
                else
                {
                    queryString = queryString + SearchKodawari_M2.makeQueryString( request );
                }

                Logging.info( "地方queryString=" + queryString, "[ActionKodawariSearch_M2_Mobile.class]serachHotels:" );

                request.setAttribute( "QUERY-STRING", queryString );
                request.setAttribute( "KODAWARI-STRING", strKodawari );
                requestDispatcher = request.getRequestDispatcher( "search_detail_local_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            // 地方が選択されている場合
            else if ( paramLocalId != null && (paramPrefId == null || prefId == 0) && paramGps == null )
            {
                Logging.info( "都道府県検索app=" + canSearchFlag, "[ActionKodawariSearch_M2_Mobile.class]serachHotels:" );
                if ( paramKind != null )
                {
                    if ( CheckString.numCheck( paramKind ) != false )
                    {
                        if ( Integer.parseInt( paramKind ) == 1 )
                            strKodawari = "<font color=\"red\">「予約」</font>のできるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 2 )
                            strKodawari = "<font color=\"red\">「岩盤浴」</font>のあるﾎﾃﾙ";
                        else if ( Integer.parseInt( paramKind ) == 3 )
                            strKodawari = "<font color=\"red\">「露天風呂」</font>のあるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 4 )
                            strKodawari = "<font color=\"red\">「温泉」</font>のあるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 5 )
                            strKodawari = "<font color=\"red\">「1人」</font>で泊まれるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 6 )
                            strKodawari = "<font color=\"red\">「3人以上」</font>で泊まれるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 7 )
                            strKodawari = "<font color=\"red\">「ｺｽﾌﾟﾚ」</font>できるﾎﾃﾙ";
                        else
                            paramKind = "0";

                        queryString = queryString + "&kind=" + paramKind;

                    }
                    else
                        paramKind = "0";
                }
                else
                {
                    paramKind = "0";
                    strKodawari = "";
                }
                prefIds = pref_ID[(Integer.parseInt( paramLocalId ) - 1)];
                prefHotelCounts = new int[prefIds.length];
                searchKodawari = new SearchKodawari_M2();
                for( int i = 0 ; i < prefIds.length ; i++ )
                {
                    // ホテルの件数とクエリーストリングを取得する
                    searchKodawari.searchHotelCountMobile( request, prefIds[i] );
                    prefHotelCounts[i] = searchKodawari.getAllCount();
                }

                if ( canSearchFlag )
                {
                    queryString = queryString + "&search=true&canSearchFlag=true" + searchKodawari.getQueryString();
                }
                else
                {
                    queryString = queryString + "&search=true" + searchKodawari.getQueryString();
                }

                Logging.info( "都道府県queryString=" + queryString, "[ActionKodawariSearch_M2_Mobile.class]serachHotels:" );

                request.setAttribute( "HOTEL-COUNTS", prefHotelCounts );
                request.setAttribute( "QUERY-STRING", queryString );
                request.setAttribute( "KODAWARI-STRING", strKodawari );
                requestDispatcher = request.getRequestDispatcher( "search_detail_pref_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;

            }
            else if ( paramPrefId != null || paramGps != null )
            {
                Logging.info( "GpsとはGps=" + paramGps, "[ActionKodawariSearch_M2_Mobile.class]serachHotels:" );
                if ( paramKind != null )
                {
                    if ( CheckString.numCheck( paramKind ) != false )
                    {
                        if ( Integer.parseInt( paramKind ) == 1 )
                            strKodawari = "<font color=\"red\">「予約」</font>のできるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 2 )
                            strKodawari = "<font color=\"red\">「岩盤浴」</font>のあるﾎﾃﾙ";
                        else if ( Integer.parseInt( paramKind ) == 3 )
                            strKodawari = "<font color=\"red\">「露天風呂」</font>のあるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 4 )
                            strKodawari = "<font color=\"red\">「温泉」</font>のあるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 5 )
                            strKodawari = "<font color=\"red\">「1人」</font>で泊まれるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 6 )
                            strKodawari = "<font color=\"red\">「3人以上」</font>で泊まれるﾗﾌﾞﾎ";
                        else if ( Integer.parseInt( paramKind ) == 7 )
                            strKodawari = "<font color=\"red\">「ｺｽﾌﾟﾚ」</font>できるﾎﾃﾙ";
                        else
                            paramKind = "0";

                        if ( paramGps != null )
                        {
                            queryString = queryString + "&byGps=true&kind=" + paramKind;
                        }
                        else
                        {
                            queryString = queryString + "&kind=" + paramKind;
                        }
                    }
                    else
                        paramKind = "0";
                }
                else
                {
                    paramKind = "0";
                    strKodawari = "";
                }
                if ( !CheckString.numCheck( paramPage ) )
                {
                    paramPage = "0";
                }
                pageNum = Integer.parseInt( paramPage );
                searchKodawari = new SearchKodawari_M2();
                if ( prefId == 0 )
                {
                    searchKodawari.searchHotelListMobile( request, pageNum, pageRecords, prefId );
                }
                else
                {
                    searchKodawari.searchHotelListMobile( request, pageNum, pageRecords, prefId );
                }
                if ( canSearchFlag )
                {
                    queryString = actionURL + "search=true&canSearchFlag=true&pref_id=" + paramPrefId + "&" + queryString + "&" + searchKodawari.getQueryString();
                }
                else
                {
                    queryString = actionURL + "search=true&pref_id=" + paramPrefId + "&" + queryString + "&" + searchKodawari.getQueryString();
                }
                Logging.info( "GpsqueryString=" + queryString, "[ActionKodawariSearch_M2_Mobile.class]serachHotels:" );

                // 共通にするためデータをセットする
                arrDataSearchHotel = searchKodawari.getHotelInfo();
                hotelCount = searchKodawari.getCount();
                hotelAllCount = searchKodawari.getAllCount();

                currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords, hotelAllCount, hotelCount, dispFormat );

                dataSearchResult = new DataSearchResult_M2();
                if ( hotelAllCount > pageRecords )
                {
                    pageLinks = PagingDetails.getPagenationLinkMobileSorted( pageNum, pageRecords, hotelAllCount, queryString, paramUidLink, searchKodawari.getSortParams()[0] );

                    dataSearchResult.setPageLink( pageLinks );
                }
                dataSearchResult.setRecordsOnPage( currentPageRecords );
                dataSearchResult.setHotelCount( hotelCount );
                dataSearchResult.setHotelAllCount( hotelAllCount );
                dataSearchResult.setDataSearchHotel( arrDataSearchHotel );
                if ( paramPrefId != null && (paramPrefId.trim().length() > 0) )
                {
                    dataSearchResult.setParamParameter1( paramPrefId );
                }

                dataSearchResult.setParamParameter2( searchKodawari.getSortParams()[0] );
                dataSearchResult.setParamParameter3( searchKodawari.getSortParams()[1] );
                request.setAttribute( "KODAWARI-STRING", strKodawari );
                request.setAttribute( "QUERY-STRING", queryString );
                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                requestDispatcher = request.getRequestDispatcher( "search_result_kodawari_M2.jsp" );
                requestDispatcher.forward( request, response );

            }
            else
            {
                Logging.info( "選択無app=" + canSearchFlag, "[ActionKodawariSearch_M2_Mobile.class]serachHotels:" );
                if ( canSearchFlag )
                {
                    response.sendRedirect( "index_M2_renewal.jsp" );
                }
                else
                {
                    response.sendRedirect( "index_M2.jsp" );
                }
                return;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionKodawariSearchMobile_M2.searchHotels(REQ,RESP) ] Exception ", e );
            try
            {
                dataSearchResult = new DataSearchResult_M2();
                dataSearchResult.setErrorOccurring( true );
                // dataSearchResult.setErrorMessage(recordsNotFoundMsg2);
                request.setAttribute( "SEARCH-RESULT", dataSearchResult );

                requestDispatcher = request.getRequestDispatcher( "search_result_kodawari_M2.jsp" );

                requestDispatcher.forward( request, response );
            }
            catch ( Exception exp )
            {
                Logging.error( "unable to dispatch.....=" + exp.toString(), exp );
            }
        }
        finally
        {
            searchKodawari = null;
            arrDataSearchHotel = null;
            dataSearchResult = null;
        }
    }

}
