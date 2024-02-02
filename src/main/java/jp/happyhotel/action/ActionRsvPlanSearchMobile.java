package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataRsvCalendar;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.data.DataRsvRoomRemainder;
import jp.happyhotel.data.DataSearchPlan;
import jp.happyhotel.data.DataSearchRsvPlanResult;
import jp.happyhotel.others.RsvCalendar;
import jp.happyhotel.owner.FormOwnerRsvManageCalendar;
import jp.happyhotel.owner.LogicOwnerRsvManageCalendar;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.search.SearchRsvPlan;
import jp.happyhotel.search.SearchRsvPlanDao;

/**
 * ハピー加盟店検索クラス
 * 
 * @author S.Tashiro
 * @version 1.0 2011/02/09
 */

public class ActionRsvPlanSearchMobile extends BaseAction
{

    private RequestDispatcher requestDispatcher;
    private final static int  FULL                = 0;
    private final static int  FEW                 = 2;

    static int                pageRecords         = 5;
    static int                maxRecords          = Constants.maxRecords;
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2;
    static final int          PAGERECORD_PLAN     = 5;
    static final int          dispFormat          = 1;

    /**
     * 住所検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int i = 0;
        int planCount = 0;
        int planAllCount = 0;
        int pageNum = 0;
        int startDate = 0;
        int endDate = 0;
        int roomCount = 0;
        int roomAllCount = 0;
        int roomRemainderCount = 0; // プランごとの空き部屋数で使用
        int[] arrPlanIdList = null;
        int[] roomRemainder = null; // 日ごとの空き部屋数で使用
        String paramHotelId = "";
        String paramDate = "";
        String paramAdult = "";
        String paramChild = "";
        String paramChargeFrom = "";
        String paramChargeTo = "";
        String paramYear = "";
        String paramMonth = "";
        String paramDay = "";
        String paramPlanId = "";
        String paramSeq = "";
        String paramPage = "";
        String paramMode = "";
        String queryString = null;
        String currentPageRecords = null;
        String pageLinks = "";
        String dispDate = ""; // 予約検索結果ページで表示
        String[] paramDispDate = null;// 日付選択ページで表示
        String paramUidLink = "";
        SearchRsvPlanDao searchPlanDao = null;
        SearchRsvPlan searchPlan = null;
        DataSearchRsvPlanResult dataSearchRsvPlanResult = null;
        DataSearchPlan[] arrDataSearchPlan = null;
        DataRsvRoomRemainder dataRsvRoomRemainder;
        RsvCalendar rsvCal = null;
        DataRsvPlan drp = null;
        FormReservePersonalInfoPC frmInfoPC = null;
        FormReservePersonalInfoPC[] arryfrmInfoPC = null;
        DataLoginInfo_M2 dataLoginInfo_M2 = null;
        ReserveCommon rsvcomm = null;
        String ownerFlag = "";
        String ownerQuery = "";
        String setCalDate = "";
        String selectMonth = "";

        try
        {
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            paramHotelId = request.getParameter( "hotel_id" );
            paramDate = request.getParameter( "date" );
            paramAdult = request.getParameter( "adult" );
            paramChild = request.getParameter( "child" );
            paramChargeFrom = request.getParameter( "charge_from" );
            paramChargeTo = request.getParameter( "charge_to" );
            paramPage = request.getParameter( "page" );
            paramYear = request.getParameter( "year" );
            paramMonth = request.getParameter( "month" );
            paramDay = request.getParameter( "day" );
            paramPlanId = request.getParameter( "plan_id" );
            paramSeq = request.getParameter( "seq" );
            paramMode = request.getParameter( "mode" );
            ownerFlag = request.getParameter( "owner" );
            selectMonth = request.getParameter( "select_month" );
            dataSearchRsvPlanResult = new DataSearchRsvPlanResult();
            searchPlan = new SearchRsvPlan();
            rsvcomm = new ReserveCommon();

            // 数値チェック
            if ( (paramHotelId == null) || (paramHotelId.equals( "" ) != false) || (CheckString.numCheck( paramHotelId ) == false) )
            {
                paramHotelId = "0";
            }
            if ( (paramDate == null) || (paramDate.equals( "" ) != false) || (CheckString.numCheck( paramDate ) == false) )
            {
                paramDate = "0";
            }
            if ( CheckString.numCheck( paramAdult ) == false || Integer.parseInt( paramAdult ) < 0 )
            {
                paramAdult = "0";
            }
            if ( rsvcomm.checkLoveHotelFlag( Integer.parseInt( paramHotelId ) ) == true || CheckString.numCheck( paramChild ) == false || Integer.parseInt( paramChild ) < 0 )
            {
                paramChild = "0";
            }
            if ( CheckString.numCheck( paramChargeFrom ) == false )
            {
                paramChargeFrom = "0";
            }
            if ( CheckString.numCheck( paramChargeTo ) == false )
            {
                paramChargeTo = "0";
            }

            if ( (paramPage == null) || (paramPage.equals( "" ) != false) || (CheckString.numCheck( paramPage ) == false) )
            {
                paramPage = "0";
            }
            pageNum = Integer.parseInt( paramPage );

            if ( (paramYear == null) || (paramYear.equals( "" ) != false) || (CheckString.numCheck( paramYear ) == false) )
            {
                paramYear = "0";
            }

            if ( (paramMonth == null) || (paramMonth.equals( "" ) != false) || (CheckString.numCheck( paramMonth ) == false) )
            {
                paramMonth = "0";
            }

            if ( (paramDay == null) || (paramDay.equals( "" ) != false) || (CheckString.numCheck( paramDay ) == false) )
            {
                paramDay = "0";
            }

            if ( (paramPlanId == null) || (paramPlanId.equals( "" ) != false) || (CheckString.numCheck( paramPlanId ) == false) )
            {
                paramPlanId = "0";
            }
            if ( (paramSeq == null) || (paramSeq.equals( "" ) != false) || (CheckString.numCheck( paramSeq ) == false) )
            {
                paramSeq = "0";
            }

            if ( (paramMode == null) || (paramMode.equals( "" ) != false) || (CheckString.numCheck( paramMode ) == false) )
            {
                paramMode = "0";
            }
            if ( Integer.parseInt( paramMode ) < 0 || Integer.parseInt( paramMode ) > 3 )
            {
                paramMode = "0";
            }
            if ( paramUidLink == null )
            {
                paramUidLink = "";
            }
            // 予約のプレオープンのフラグチェック
            if ( (ownerFlag == null) || (ownerFlag.equals( "1" ) == false) )
            {
                ownerFlag = "0";
            }
            else
            {
                ownerQuery = "&owner=1";
            }

            if ( (selectMonth == null) || (selectMonth.equals( "" ) != false) || (CheckString.numCheck( selectMonth ) == false) )
            {
                selectMonth = "0";
            }

            // 年、月、日のパラメータが0以上だったらparamDateにセット
            if ( (Integer.parseInt( paramYear ) > 0) && (Integer.parseInt( paramMonth ) > 0) && (Integer.parseInt( paramDay ) > 0) )
            {
                paramDate = String.format( "%04d", Integer.parseInt( paramYear ) ) + String.format( "%02d", Integer.parseInt( paramMonth ) ) + String.format( "%02d", Integer.parseInt( paramDay ) );
            }

            if ( Integer.parseInt( paramDate ) > 0 && Integer.parseInt( paramDate ) <= Integer.parseInt( DateEdit.getDate( 2 ) ) )
            {
                dataSearchRsvPlanResult.setErrorOccurring( true );
                dataSearchRsvPlanResult.setErrorMessage( "過去の日付のため表示できません。<br>" );
            }
            // 日付の妥当性チェック
            if ( Integer.parseInt( paramDate ) > 0 && DateEdit.checkDate( Integer.parseInt( paramDate ) / 10000, Integer.parseInt( paramDate ) / 100 % 100, Integer.parseInt( paramDate ) % 100 ) != true )
            {
                dataSearchRsvPlanResult.setErrorOccurring( true );
                dataSearchRsvPlanResult.setErrorMessage( "日付が正しくないため表示できません。<br>" );
            }
            if ( paramHotelId.equals( "0" ) == false )
            {
                // 予約に最低必要なデータを取得
                dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

                if ( paramPlanId.equals( "0" ) == false )
                {
                    if ( dataLoginInfo_M2 != null && dataLoginInfo_M2.isMemberFlag() != false )
                    {
                        frmInfoPC = new FormReservePersonalInfoPC();
                        frmInfoPC.setMode( ReserveCommon.MODE_INS );
                        frmInfoPC.setSelHotelID( Integer.parseInt( paramHotelId ) );
                        frmInfoPC.setSelPlanID( Integer.parseInt( paramPlanId ) );
                        frmInfoPC.setOrgReserveDate( Integer.parseInt( paramDate ) );
                        frmInfoPC.setSelRsvDate( Integer.parseInt( paramDate ) );
                        frmInfoPC.setSeq( Integer.parseInt( paramSeq ) );
                        frmInfoPC.setReserveDateFormat( "" );
                        frmInfoPC.setParkingUsedKbnInit( 0 );
                        frmInfoPC.setSelSeq( Integer.parseInt( paramSeq ) );
                        frmInfoPC.setPaymemberFlg( dataLoginInfo_M2.isPaymemberFlag() );
                        frmInfoPC = rsvcomm.getPlanData( frmInfoPC );
                    }
                }
            }

            // ホテルIDがなかったらエラー
            if ( paramHotelId.equals( "0" ) != false )
            {
                response.sendRedirect( "/index.jsp" );
            }
            // 日付・料金選択TOPページへ
            else if ( paramMode.equals( "0" ) != false )
            {
                rsvCal = new RsvCalendar();
                // 明日の日付と、10日後の日付を入れてカレンダーを取得する
                rsvCal.getCalendar( DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), 1 ), DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), 10 ) );

                // 残数情報を保持する
                roomRemainder = new int[rsvCal.getCalendarList().get( 0 ).size()];
                paramDispDate = new String[rsvCal.getCalendarList().get( 0 ).size()];
                // カレンダーリスト分だけ残数を取得する
                for( i = 0 ; i < rsvCal.getCalendarList().get( 0 ).size() ; i++ )
                {
                    paramDispDate[i] = new String();

                    // 部屋の空き数を表示（予約上限を見ていないため使用しないようにした。）
                    // roomRemainder[i] = searchPlan.getEmptyRoom( Integer.parseInt( paramHotelId ), rsvCal.getCalendarList().get( 0 ).get( i ).getCalDate() );

                    // 予約可能なプラン数を取得
                    arrPlanIdList = searchPlan.getSearchPlanIdList( Integer.parseInt( paramHotelId ), rsvCal.getCalendarList().get( 0 ).get( i ).getCalDate(),
                            Integer.parseInt( paramAdult ), Integer.parseInt( paramChild ), Integer.parseInt( paramChargeFrom ), Integer.parseInt( paramChargeTo ) );
                    //
                    if ( arrPlanIdList != null && arrPlanIdList.length > 0 )
                    {
                        roomRemainder[i] = arrPlanIdList.length;
                    }
                    else
                    {
                        roomRemainder[i] = 0;
                    }

                    // 表示する文言をセット
                    paramDispDate[i] = String.format( "%04d/%02d/%02d", rsvCal.getCalendarList().get( 0 ).get( i ).getCalDate() / 10000, rsvCal.getCalendarList().get( 0 ).get( i ).getCalDate() % 10000 / 100,
                            rsvCal.getCalendarList().get( 0 ).get( i ).getCalDate() % 100 );

                    // 曜日をセット
                    paramDispDate[i] += this.getDayOfWeek( rsvCal.getCalendarList().get( 0 ).get( i ).getWeek() );
                    // 残数から空き状況を表す記号をセット
                    paramDispDate[i] += getRoomRemainderStatus( roomRemainder[i] );
                }

                // 日付のセット
                request.setAttribute( "DATE", paramDate );
                // カレンダーリストのセット
                request.setAttribute( "CALENDAR-LIST", rsvCal.getCalendarList() );
                // 残数情報のセット
                request.setAttribute( "ROOM-REMAINDER", roomRemainder );
                // 表示文言のセット
                request.setAttribute( "DISP-DATE", paramDispDate );
                requestDispatcher = request.getRequestDispatcher( "hotel_rsv_date.jsp" );
                requestDispatcher.forward( request, response );
            }
            // 日付プルダウン・料金選択ページへ
            else if ( paramMode.equals( "1" ) != false )
            {
                // 日付のセット
                request.setAttribute( "DATE", paramDate );
                requestDispatcher = request.getRequestDispatcher( "hotel_rsv_anotherday.jsp" );
                requestDispatcher.forward( request, response );
            }
            // 日付カレンダー・料金選択ページへ
            else if ( paramMode.equals( "2" ) != false )
            {
                // 日付のセット
                request.setAttribute( "DATE", paramDate );
                if ( paramPlanId.equals( "0" ) == false )
                {
                    if ( Integer.parseInt( paramDate ) == 0 )
                    {
                        // 日付未指定の時は現在年月日セット
                        paramDate = DateEdit.getDate( 2 );
                    }

                    if ( frmInfoPC != null )
                    {
                        // プランIDが入っていればセットする
                        frmInfoPC = setCalenderInfo( frmInfoPC, Integer.parseInt( paramDate ) / 100 );
                        request.setAttribute( "RESERVE-PLAN", frmInfoPC );
                    }
                }
                // 先行予約カレンダー情報セット
                requestDispatcher = request.getRequestDispatcher( "hotel_rsv_calendar.jsp" );
                requestDispatcher.forward( request, response );
            }
            else if ( paramMode.equals( "3" ) )
            {
                // 表示日付のセット
                if ( paramDate.equals( "0" ) == false )
                {
                    dispDate = this.getCalendarDayOfWeek( Integer.parseInt( paramDate ) );
                    ReserveCommon rsvCmm = new ReserveCommon();
                    if ( dataLoginInfo_M2 != null )
                    {
                        // 予約日の重複チェック
                        if ( rsvCmm.checkReserveDuplicate( dataLoginInfo_M2.getUserId(), Integer.parseInt( paramDate ) ).equals( "" ) == false )
                        {
                            dataSearchRsvPlanResult.setErrorOccurring( true );
                            dataSearchRsvPlanResult.setErrorMessage( Message.getMessage( "warn.00018" ) + "<br />" );
                        }
                    }
                }
                else
                {
                    int start = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), 1 );
                    int end = DateEdit.addMonth( Integer.parseInt( DateEdit.getDate( 2 ) ), 2 );
                    dispDate = "日付未選択";
                }

                request.setAttribute( "DISP-DATE", dispDate );

                // プランIDリストを検索条件から取得
                arrPlanIdList = searchPlan.getSearchPlanIdList( Integer.parseInt( paramHotelId ), Integer.parseInt( paramDate ),
                        Integer.parseInt( paramAdult ), Integer.parseInt( paramChild ), Integer.parseInt( paramChargeFrom ), Integer.parseInt( paramChargeTo ) );

                // 開始日付と終了日付を取得(共通)
                startDate = searchPlan.getStartDate();
                endDate = searchPlan.getEndDate();
                dataSearchRsvPlanResult.setStartDate( startDate );
                dataSearchRsvPlanResult.setEndDate( endDate );
                dataSearchRsvPlanResult.setAdult( Integer.parseInt( paramAdult ) );
                dataSearchRsvPlanResult.setChild( Integer.parseInt( paramChild ) );
                dataSearchRsvPlanResult.setChargeFrom( Integer.parseInt( paramChargeFrom ) );
                dataSearchRsvPlanResult.setChargeTo( Integer.parseInt( paramChargeTo ) );
                // 有料会員・無料会員の情報をセット
                if ( dataLoginInfo_M2 != null )
                {
                    dataSearchRsvPlanResult.setPaymemberFlg( dataLoginInfo_M2.isPaymemberFlag() );
                }

                // プランIDと部屋番号があれば予約プラン詳細ページヘ
                if ( Integer.parseInt( paramPlanId ) > 0 && Integer.parseInt( paramSeq ) > 0 )
                {
                    // プランの空き部屋数をセット
                    dataRsvRoomRemainder = new DataRsvRoomRemainder();
                    roomRemainderCount = dataRsvRoomRemainder.getRemainderSumCount( Integer.parseInt( paramHotelId ), Integer.parseInt( paramPlanId ), Integer.parseInt( paramDate ) );

                    // searchPlanDaoに必要な情報をセットする
                    searchPlanDao = new SearchRsvPlanDao();
                    searchPlanDao.getRoomDetails( Integer.parseInt( paramHotelId ), Integer.parseInt( paramPlanId ), Integer.parseInt( paramAdult ),
                            Integer.parseInt( paramChild ), startDate, endDate, Integer.parseInt( paramSeq ) );

                    // searchPlanDaoからプランID、開始日、終了日を取得
                    arrDataSearchPlan = searchPlanDao.getPlanInfo();
                    planCount = searchPlanDao.getCount();
                    planAllCount = searchPlanDao.getAllCount();
                    roomCount = searchPlanDao.getRoomCount();
                    roomAllCount = searchPlanDao.getRoomAllCount();

                    // プラン数、部屋数が0の場合はエラー
                    if ( planCount == 0 || roomCount == 0 )
                    {
                        dataSearchRsvPlanResult.setErrorOccurring( true );
                        dataSearchRsvPlanResult.setErrorMessage( "該当する部屋が見つかりませんでした。<br>" );
                    }

                    dataSearchRsvPlanResult.setRecordsOnPage( "" );
                    dataSearchRsvPlanResult.setPlanCount( planCount );
                    dataSearchRsvPlanResult.setPlanAllCount( planAllCount );
                    // 検索結果をセット
                    dataSearchRsvPlanResult.setDataSearchPlan( arrDataSearchPlan );

                    /* 2014/05/20 スマホのカレンダー表示のため追加 */
                    if ( paramPlanId.equals( "0" ) == false )
                    {
                        if ( Integer.parseInt( selectMonth ) == 0 )
                        {
                            if ( Integer.parseInt( paramDate ) == 0 )
                            {
                                // 日付未指定の時は現在年月日セット
                                selectMonth = DateEdit.getDate( 2 );
                            }
                            else
                            {
                                selectMonth = paramDate;
                            }
                        }

                        if ( frmInfoPC != null )
                        {
                            // プランIDが入っていればセットする
                            frmInfoPC = setCalenderInfo( frmInfoPC, Integer.parseInt( selectMonth ) / 100 );
                            request.setAttribute( "RESERVE-PLAN", frmInfoPC );
                        }
                    }
                    /* 2014/05/20 スマホのカレンダー表示のため追加 */

                    // 日付のセット
                    request.setAttribute( "DATE", paramDate );
                    request.setAttribute( "SEARCH-RESULT", dataSearchRsvPlanResult );
                    request.setAttribute( "REMAINDER-COUNT", Integer.toString( roomRemainderCount ) );

                    request.setAttribute( "RESERVE-PLAN", frmInfoPC );
                    requestDispatcher = request.getRequestDispatcher( "hotel_rsv_plan_room_detail.jsp" );
                    requestDispatcher.forward( request, response );
                }
                // プランIDのみだったら予約プラン部屋一覧ページへ
                else if ( Integer.parseInt( paramPlanId ) > 0 && Integer.parseInt( paramSeq ) == 0 )
                {
                    // ホテルIDとプランIDの整合チェック
                    drp = new DataRsvPlan();
                    drp.getData( Integer.parseInt( paramHotelId ), Integer.parseInt( paramPlanId ) );
                    if ( drp.getID() != Integer.parseInt( paramHotelId ) || drp.getPlanId() != Integer.parseInt( paramPlanId ) )
                    {
                        dataSearchRsvPlanResult.setErrorOccurring( true );
                        dataSearchRsvPlanResult.setErrorMessage( "該当するプランが見つかりませんでした。<br>" );
                    }
                    else
                    {

                        // プランの空き部屋数をセット
                        dataRsvRoomRemainder = new DataRsvRoomRemainder();
                        roomRemainderCount = dataRsvRoomRemainder.getRemainderSumCount( Integer.parseInt( paramHotelId ), Integer.parseInt( paramPlanId ), Integer.parseInt( paramDate ) );
                        Logging.info( "roomRemainderCount:" + roomRemainderCount );

                        searchPlanDao = new SearchRsvPlanDao();
                        searchPlanDao.getRoomList( Integer.parseInt( paramHotelId ), Integer.parseInt( paramPlanId ), Integer.parseInt( paramAdult ),
                                Integer.parseInt( paramChild ), startDate, endDate, PAGERECORD_PLAN, pageNum );

                        // searchPlanDaoからプランID、開始日、終了日を取得
                        arrDataSearchPlan = searchPlanDao.getPlanInfo();
                        planCount = searchPlanDao.getCount();
                        planAllCount = searchPlanDao.getAllCount();
                        roomCount = searchPlanDao.getRoomCount();
                        roomAllCount = searchPlanDao.getRoomAllCount();
                        // 最大最少人数設定変更前にリンクを押された場合の処理
                        if ( planCount > 0 )
                        {
                            // クエリーストリングの作成
                            queryString = "searchRsvPlanMobile.act?hotel_id=" + paramHotelId + "&plan_id=" + paramPlanId + "&seq=" + paramSeq + "&mode=3&date=" + paramDate + "&adult=" + paramAdult +
                                    "&child=" + paramChild + "&charge_from=" + paramChargeFrom + "&charge_to=" + paramChargeTo + ownerQuery;

                            currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, PAGERECORD_PLAN, roomAllCount, roomCount, dispFormat );

                            // 部屋数が設定値より多い場合
                            if ( roomAllCount > PAGERECORD_PLAN )
                            {
                                pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, PAGERECORD_PLAN, roomAllCount, queryString, paramUidLink );
                                dataSearchRsvPlanResult.setPageLink( pageLinks );
                            }
                            dataSearchRsvPlanResult.setRecordsOnPage( currentPageRecords );
                            dataSearchRsvPlanResult.setPlanCount( planCount );
                            dataSearchRsvPlanResult.setPlanAllCount( planAllCount );
                            // 検索結果をセット
                            dataSearchRsvPlanResult.setDataSearchPlan( arrDataSearchPlan );
                        }
                        else
                        {
                            dataSearchRsvPlanResult.setErrorOccurring( true );
                            dataSearchRsvPlanResult.setErrorMessage( "該当するプランが見つかりませんでした。<br>" );

                        }
                    }

                    /* 2014/05/20 スマホのカレンダー表示のため追加 */
                    if ( paramPlanId.equals( "0" ) == false )
                    {
                        if ( Integer.parseInt( selectMonth ) == 0 )
                        {
                            if ( Integer.parseInt( paramDate ) == 0 )
                            {
                                // 日付未指定の時は現在年月日セット
                                selectMonth = DateEdit.getDate( 2 );
                            }
                            else
                            {
                                selectMonth = paramDate;
                            }
                        }

                        if ( frmInfoPC != null )
                        {
                            // プランIDが入っていればセットする
                            frmInfoPC = setCalenderInfo( frmInfoPC, Integer.parseInt( selectMonth ) / 100 );
                            request.setAttribute( "RESERVE-PLAN", frmInfoPC );
                        }
                    }
                    /* 2014/05/20 スマホのカレンダー表示のため追加 */

                    // 日付のセット
                    request.setAttribute( "DATE", paramDate );
                    request.setAttribute( "SEARCH-RESULT", dataSearchRsvPlanResult );
                    request.setAttribute( "REMAINDER-COUNT", Integer.toString( roomRemainderCount ) );

                    requestDispatcher = request.getRequestDispatcher( "hotel_rsv_plan_room.jsp" );
                    requestDispatcher.forward( request, response );
                }
                else
                {
                    if ( arrPlanIdList != null && arrPlanIdList.length > 0 )
                    {
                        searchPlanDao = new SearchRsvPlanDao();

                        // 取得したプランIDリストから予約一覧ページに必要なデータを取得する
                        searchPlanDao.getPlanList( Integer.parseInt( paramHotelId ), arrPlanIdList, Integer.parseInt( paramAdult ),
                                Integer.parseInt( paramChild ), startDate, endDate, pageRecords, pageNum );

                        // searchPlanDaoからプランID、開始日、終了日を取得
                        arrDataSearchPlan = searchPlanDao.getPlanInfo();
                        if ( paramHotelId.equals( "0" ) == false )
                        {
                            // 予約に最低必要なデータを取得
                            dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
                            if ( arrDataSearchPlan.length > 0 )
                            {
                                arryfrmInfoPC = new FormReservePersonalInfoPC[arrDataSearchPlan.length];
                            }
                            for( int j = 0 ; j < arrDataSearchPlan.length ; j++ )
                            {
                                FormReservePersonalInfoPC item = new FormReservePersonalInfoPC();

                                item.setMode( ReserveCommon.MODE_INS );
                                item.setSelHotelID( Integer.parseInt( paramHotelId ) );
                                item.setSelPlanID( arrDataSearchPlan[j].getPlanId() );
                                item.setOrgReserveDate( Integer.parseInt( paramDate ) );
                                item.setSelRsvDate( Integer.parseInt( paramDate ) );
                                item.setSeq( Integer.parseInt( paramSeq ) );
                                item.setReserveDateFormat( "" );
                                item.setParkingUsedKbnInit( 0 );
                                if ( dataLoginInfo_M2 != null )
                                {
                                    item.setPaymemberFlg( dataLoginInfo_M2.isPaymemberFlag() );
                                }
                                else
                                {
                                    item.setPaymemberFlg( false );
                                }
                                rsvcomm = new ReserveCommon();
                                item = rsvcomm.getPlanData( item );

                                arryfrmInfoPC[j] = item;
                            }
                        }

                        planCount = searchPlanDao.getCount();
                        planAllCount = searchPlanDao.getAllCount();

                        // クエリーストリングの作成
                        queryString = "searchRsvPlanMobile.act?hotel_id=" + paramHotelId + "&mode=3&date=" + paramDate + "&adult=" + paramAdult +
                                "&child=" + paramChild + "&charge_from=" + paramChargeFrom + "&charge_to=" + paramChargeTo + ownerQuery;
                        currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords, planAllCount, planCount, dispFormat );

                        if ( planAllCount > pageRecords )
                        {
                            pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, pageRecords, planAllCount, queryString, paramUidLink );
                            dataSearchRsvPlanResult.setPageLink( pageLinks );
                        }
                        dataSearchRsvPlanResult.setRecordsOnPage( currentPageRecords );
                        dataSearchRsvPlanResult.setPlanCount( planCount );
                        dataSearchRsvPlanResult.setPlanAllCount( planAllCount );
                        // 検索結果をセット
                        dataSearchRsvPlanResult.setDataSearchPlan( arrDataSearchPlan );
                    }
                    else
                    {
                        dataSearchRsvPlanResult.setErrorOccurring( true );
                        dataSearchRsvPlanResult.setErrorMessage( "予約可能なプランが見つかりませんでした。<br>" );
                        dataSearchRsvPlanResult.setPlanCount( 0 );
                        dataSearchRsvPlanResult.setPlanAllCount( 0 );
                    }
                    // 日付のセット
                    request.setAttribute( "DATE", paramDate );
                    request.setAttribute( "RESERVE-PLANS", arryfrmInfoPC );
                    request.setAttribute( "SEARCH-RESULT", dataSearchRsvPlanResult );
                    requestDispatcher = request.getRequestDispatcher( "hotel_rsv_plan.jsp" );
                    requestDispatcher.forward( request, response );
                }
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionRsvPlanSearchMobile.execute()] Exception", exception );
            try
            {
                response.sendRedirect( "/index.jsp" );
            }
            catch ( Exception exception1 )
            {
                Logging.error( "unable to dispatch.....=" + exception1.toString() );
            }

        }
        finally
        {
        }
    }

    /***
     * 表示日の取得
     * 
     * @param week 0:日曜～6:土曜
     * @return
     */
    private String getCalendarDayOfWeek(int day)
    {
        boolean ret = false;
        String returnWord = "";
        DataRsvCalendar drCal;

        drCal = new DataRsvCalendar();
        returnWord = String.format( "%04d年%02d月%02d日", day / 10000, day % 10000 / 100, day % 100 );

        ret = drCal.getData( day );
        if ( ret != false )
        {
            returnWord += this.getDayOfWeek( drCal.getWeek() );
        }
        return returnWord;
    }

    /***
     * 曜日変換処理
     * 
     * @param week 0:日曜～6:土曜
     * @return
     */
    private String getDayOfWeek(int week)
    {
        String returnString = "";

        if ( week == 0 )
        {
            returnString = "(日)";
        }
        if ( week == 1 )
        {
            returnString = "(月)";
        }
        if ( week == 2 )
        {
            returnString = "(火)";
        }
        if ( week == 3 )
        {
            returnString = "(水)";
        }
        if ( week == 4 )
        {
            returnString = "(木)";
        }
        if ( week == 5 )
        {
            returnString = "(金)";
        }
        if ( week == 6 )
        {
            returnString = "(土)";
        }

        return(returnString);
    }

    /***
     * 空部屋数 判定処理
     * 
     * @param roomRemainder 空部屋数
     * @return
     */
    private String getRoomRemainderStatus(int roomRemainder)
    {
        String returnString = "";
        if ( roomRemainder == FULL )
        {
            returnString = "&nbsp;×";
        }
        // else if ( roomRemainder <= FEW )
        // {
        // returnString = "&nbsp;△";
        // }
        else
        {
            returnString = "&nbsp;○";
        }

        return(returnString);
    }

    /**
     * 
     * カレンダー情報
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @param targetDate 対象年月
     * @return FormReservePersonalInfoPCオブジェクト
     * @throws Exception
     */
    private FormReservePersonalInfoPC setCalenderInfo(FormReservePersonalInfoPC frm, int targetDate) throws Exception
    {
        LogicOwnerRsvManageCalendar logicCalendar;
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        int checkDateST = 0;
        int checkDateED = 0;
        int checkDatePremiumST = 0;
        int checkDatePremiumED = 0;
        int dispCheckDateST = 0;
        int dispCheckDateED = 0;
        int checkDateFreeST = 0;
        int checkDateFreeED = 0;
        int checkDateFreeAddED = 0;
        int curDate = 0;
        int curTime = 0;
        int minusDay = 0;
        int plusDay = 0;
        ReserveCommon rsvCmm = new ReserveCommon();
        logicCalendar = new LogicOwnerRsvManageCalendar();
        int numchild = 0;

        try
        {
            // 子供人数制御(4号営業ホテルは強制的に0)
            if ( rsvCmm.checkLoveHotelFlag( frm.getSelHotelID() ) == false )
            {
                numchild = frm.getSelNumChild();
            }
            // カレンダー情報取得
            if ( frm.getSelSeq() != 0 )
            {
                monthlyList = logicCalendar.getCalendarData( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelSeq(), targetDate );
            }
            else
            {
                monthlyList = logicCalendar.getCalendarData( frm.getSelHotelID(), frm.getSelPlanID(), targetDate );
            }

            curDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            curTime = Integer.parseInt( DateEdit.getTime( 1 ) );

            if ( curTime > frm.getRsvEndTime() )
            {
                plusDay++;
            }
            if ( curTime < frm.getRsvStartTime() )
            {
                minusDay++;
            }

            checkDateST = DateEdit.addDay( curDate, frm.getRsvEndDate() + plusDay );
            checkDateED = DateEdit.addDay( curDate, frm.getRsvStartDate() - minusDay );

            // 販売開始期間が超えている場合は開始日を変える
            if ( checkDateST < frm.getSalesStartDay() )
            {
                dispCheckDateST = frm.getSalesStartDay();
            }
            else
            {
                dispCheckDateST = checkDateST;
            }
            // 販売終了期間を超えている場合は終了日をセット
            if ( checkDateED > frm.getSalesEndDay() )
            {
                dispCheckDateED = frm.getSalesEndDay();
            }
            else
            {
                dispCheckDateED = checkDateED;
            }

            frm.setRsvStartDayStr( String.format( "%1$04d年%2$02d月%3$02d日", dispCheckDateST / 10000, dispCheckDateST % 10000 / 100, dispCheckDateST % 10000 % 100 ) );
            frm.setRsvEndDayStr( String.format( "%1$04d年%2$02d月%3$02d日", dispCheckDateED / 10000, dispCheckDateED % 10000 / 100, dispCheckDateED % 10000 % 100 ) );

            checkDateFreeST = DateEdit.addDay( curDate, frm.getRsvEndDateFree() + plusDay );
            checkDateFreeED = DateEdit.addDay( curDate, frm.getRsvStartDateFree() - minusDay );
            checkDateFreeAddED = DateEdit.addDay( curDate, frm.getRsvStartDateFree() - minusDay + 1 );

            checkDatePremiumST = DateEdit.addDay( curDate, frm.getRsvEndDatePremium() + plusDay );
            checkDatePremiumED = DateEdit.addDay( curDate, frm.getRsvStartDatePremium() - minusDay );

            frm.setRsvPremiumEndDayStr( String.format( "%1$04d年%2$02d月%3$02d日", checkDatePremiumED / 10000, checkDatePremiumED % 10000 / 100, checkDatePremiumED % 10000 % 100 ) );

            if ( checkDateFreeED == checkDatePremiumED )
            {
                // 無料会員と有料会員の終了日が同じ場合は、開始表示をなしにする
                frm.setRsvPremiumStartDayStr( "" );
            }
            else
            {
                frm.setRsvPremiumStartDayStr( String.format( "%1$04d年%2$02d月%3$02d日", checkDateFreeAddED / 10000, checkDateFreeAddED % 10000 / 100, checkDateFreeAddED % 10000 % 100 ) );
            }

            for( int i = 0 ; i <= monthlyList.size() - 1 ; i++ )
            {
                ArrayList<FormOwnerRsvManageCalendar> oneList = monthlyList.get( i );
                for( int j = 0 ; j <= oneList.size() - 1 ; j++ )
                {
                    FormOwnerRsvManageCalendar frmMC = oneList.get( j );
                    String reserveCharge = rsvCmm.getReserveCharge( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelNumAdult(), numchild, frmMC.getCalDate() );
                    frmMC.setReserveChargeFormat( reserveCharge );
                    if ( frmMC.getVacancyRoomNum() != 0 )
                    {
                        if ( (frmMC.getCalDate() >= checkDateST) && (frmMC.getCalDate() <= checkDateED) &&
                                (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay()) )
                        {
                            // frmMC.setRsvJotaiMark( ReserveCommon.RSV_ON_MARK );
                            // 無料会員が閲覧できる条件か確認して見れない場合はプレミアム有効日フラグをたてる
                            if ( frmMC.getCalDate() < checkDateFreeST || frmMC.getCalDate() > checkDateFreeED )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                        }
                        else
                        {
                            // 有料会員が閲覧できる条件か確認して閲覧できる場合はプレミアム有効日フラグをたてる
                            frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            if ( ((frmMC.getCalDate() >= checkDatePremiumST) && (frmMC.getCalDate() <= checkDatePremiumED) &&
                                    (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay())) )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                        }
                    }
                    else
                    {
                        if ( frm.getMode().equals( ReserveCommon.MODE_UPD ) && (frmMC.getCalDate() == frm.getOrgReserveDate()) )
                        {
                            // 変更の場合、元の予約日は「○」にする。
                            frmMC.setRsvJotaiMark( ReserveCommon.RSV_ON_MARK );
                        }
                        else
                        {
                            frmMC.setRsvJotaiMark( ReserveCommon.RSV_OFF_MARK );
                            if ( frmMC.getSalesFlag() == 0 )
                            {
                                frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            }
                        }
                        if ( (frmMC.getCalDate() >= checkDateST) && (frmMC.getCalDate() <= checkDateED) &&
                                (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay()) )
                        {
                            // 無料会員が閲覧できる条件か確認して見れない場合はプレミアム有効日フラグをたてる
                            if ( frmMC.getCalDate() < checkDateFreeST || frmMC.getCalDate() > checkDateFreeED )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                            if ( frmMC.getRsvJotaiFlg() == 1 && frmMC.getSalesFlag() == 0 )
                            {
                                frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            }
                        }
                        else
                        {
                            // 有料会員が閲覧できる条件か確認して閲覧できる場合はプレミアム有効日フラグをたてる
                            if ( ((frmMC.getCalDate() >= checkDatePremiumST) && (frmMC.getCalDate() <= checkDatePremiumED) &&
                                    (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay())) )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                            else
                            {
                                // 期間外表示
                                frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            }
                        }
                    }

                }
            }

            // フォームにセット
            frm.setMonthlyList( monthlyList );
            // 有料会員限定の開始日、終了日を追加
            frm.setRsvPremiumStartDay( checkDatePremiumST );
            frm.setRsvPremiumEndDay( checkDatePremiumED );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionRsvPlanSearchMobile.setCalenderInfo() ] Exception", exception );
            throw new Exception( "[ActionRsvPlanSearchMobile.setCalenderInfo] " + exception );
        }

        return(frm);
    }
}
