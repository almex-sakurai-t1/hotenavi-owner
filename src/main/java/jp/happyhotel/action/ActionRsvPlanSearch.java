package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataSearchPlan;
import jp.happyhotel.data.DataSearchRsvPlanResult;
import jp.happyhotel.search.SearchRsvPlan;
import jp.happyhotel.search.SearchRsvPlanDao;

/**
 * ハピー加盟店検索クラス
 * 
 * @author S.Tashiro
 * @version 1.0 2011/02/09
 */

public class ActionRsvPlanSearch extends BaseAction
{

    private RequestDispatcher requestDispatcher;

    static int                pageRecords         = 10;
    static int                maxRecords          = Constants.maxRecords;
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2;

    /**
     * 住所検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {

        int planCount = 0;
        int planAllCount = 0;
        int pageNum = 0;
        int[] arrPlanIdList = null;
        String paramHotelId = "";
        String paramDate = "";
        String paramAdult = "";
        String paramChild = "";
        String paramChargeFrom = "";
        String paramChargeTo = "";
        String paramPage = "";
        String queryString = null;
        String currentPageRecords = null;
        String pageLinks = "";
        String ownerFlag = "";
        String ownerQuery = "";
        SearchRsvPlanDao searchPlanDao = null;
        SearchRsvPlan searchPlanId = null;
        DataSearchRsvPlanResult dataSearchRsvPlanResult = null;
        DataSearchPlan[] arrDataSearchPlan = null;
        ReserveCommon rsvcomm = null;

        try
        {
            int startDate;
            int endDate;
            paramHotelId = request.getParameter( "id" );
            paramDate = request.getParameter( "date" );
            paramAdult = request.getParameter( "adult" );
            paramChild = request.getParameter( "child" );
            paramChargeFrom = request.getParameter( "charge_from" );
            paramChargeTo = request.getParameter( "charge_to" );
            paramPage = request.getParameter( "page" );
            ownerFlag = request.getParameter( "owner" );

            dataSearchRsvPlanResult = new DataSearchRsvPlanResult();
            searchPlanId = new SearchRsvPlan();
            rsvcomm = new ReserveCommon();

            // 数値チェック
            if ( CheckString.numCheck( paramHotelId ) == false )
            {
                paramHotelId = "0";
            }
            if ( CheckString.numCheck( paramDate ) == false )
            {
                paramDate = "0";
            }
            if ( CheckString.numCheck( paramAdult ) == false || Integer.parseInt( paramAdult ) > searchPlanId.SEARCH_ADULT_MAX_NUM || Integer.parseInt( paramAdult ) < 0 )
            {
                paramAdult = "0";
            }
            if ( rsvcomm.checkLoveHotelFlag( Integer.parseInt( paramHotelId ) ) == true || CheckString.numCheck( paramChild ) == false || Integer.parseInt( paramChild ) > searchPlanId.SEARCH_CHILD_MAX_NUM || Integer.parseInt( paramChild ) < 0 )
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

            if ( CheckString.numCheck( paramPage ) == false )
            {
                paramPage = "0";
            }
            pageNum = Integer.parseInt( paramPage );

            // 予約のプレオープンのフラグチェック
            if ( (ownerFlag == null) || (ownerFlag.equals( "1" ) == false) )
            {
                ownerFlag = "0";
            }
            else
            {
                ownerQuery = "&owner=1";
            }

            if ( Integer.parseInt( paramDate ) > 0 && Integer.parseInt( DateEdit.getDate( 2 ) ) >= Integer.parseInt( paramDate ) )
            {
                dataSearchRsvPlanResult.setErrorOccurring( true );
                dataSearchRsvPlanResult.setErrorMessage( "過去の日付のため表示できません。<br>" );
            }

            // プランIDリストを検索条件から取得
            arrPlanIdList = searchPlanId.getSearchPlanIdList( Integer.parseInt( paramHotelId ), Integer.parseInt( paramDate ),
                    Integer.parseInt( paramAdult ), Integer.parseInt( paramChild ), Integer.parseInt( paramChargeFrom ), Integer.parseInt( paramChargeTo ) );

            // 開始日付と終了日付を取得
            startDate = searchPlanId.getStartDate();
            endDate = searchPlanId.getEndDate();
            dataSearchRsvPlanResult.setStartDate( startDate );
            dataSearchRsvPlanResult.setEndDate( endDate );
            dataSearchRsvPlanResult.setAdult( Integer.parseInt( paramAdult ) );
            dataSearchRsvPlanResult.setChild( Integer.parseInt( paramChild ) );
            dataSearchRsvPlanResult.setChargeFrom( Integer.parseInt( paramChargeFrom ) );
            dataSearchRsvPlanResult.setChargeTo( Integer.parseInt( paramChargeTo ) );

            if ( arrPlanIdList != null && arrPlanIdList.length > 0 )
            {
                searchPlanDao = new SearchRsvPlanDao();

                // 取得したプランIDリストから予約一覧ページに必要なデータを取得する
                searchPlanDao.getPlanList( Integer.parseInt( paramHotelId ), arrPlanIdList, Integer.parseInt( paramAdult ),
                        Integer.parseInt( paramChild ), startDate, endDate, pageRecords, pageNum );

                // searchPlanDaoからプランID、開始日、終了日を取得
                arrDataSearchPlan = searchPlanDao.getPlanInfo();
                planCount = searchPlanDao.getCount();
                planAllCount = searchPlanDao.getAllCount();

                // クエリーストリングの作成
                queryString = "searchRsvPlan.act?id=" + paramHotelId + "&date=" + paramDate + "&adult=" + paramAdult +
                        "&child=" + paramChild + "&charge_from=" + paramChargeFrom + "&charge_to=" + paramChargeTo + ownerQuery;
                currentPageRecords = PagingDetails.getPageRecords( pageNum, pageRecords, planAllCount, planCount );

                if ( planAllCount > pageRecords )
                {
                    pageLinks = PagingDetails.getPagenationLinkHotenavi( pageNum, pageRecords, planAllCount, queryString, "" );
                    // pageLinks = PagingDetails.getPagenationLink( pageNum, pageRecords, planAllCount, queryString );
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

            request.setAttribute( "SEARCH-RESULT", dataSearchRsvPlanResult );
            requestDispatcher = request.getRequestDispatcher( "detail_reserve.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionRsvPlanSearch.execute()] Exception", exception );
            try
            {
                dataSearchRsvPlanResult = new DataSearchRsvPlanResult();
                dataSearchRsvPlanResult.setErrorOccurring( true );
                dataSearchRsvPlanResult.setErrorMessage( "予約可能なプランが見つかりませんでした。<br>" );

                request.setAttribute( "SEARCH-RESULT", dataSearchRsvPlanResult );
                requestDispatcher = request.getRequestDispatcher( "detail_reserve.jsp" );
                requestDispatcher.forward( request, response );
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
}
