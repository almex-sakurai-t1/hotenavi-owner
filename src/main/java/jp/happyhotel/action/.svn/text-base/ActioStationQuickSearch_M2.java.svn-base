package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataSearchStation_M2;
import jp.happyhotel.search.SearchHotelStation_M2;

import org.apache.commons.codec.net.URLCodec;

/**
 * 駅名検索クラス
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/18
 */
public class ActioStationQuickSearch_M2 extends BaseAction
{

    private RequestDispatcher requestDispatcher;

    /**
     * 駅名検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "駅名→search_station_04_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String paramStationName = null;
        SearchHotelStation_M2 searchHotelStation = null;
        DataSearchStation_M2 stationSearchDTO = null;

        try
        {
            stationSearchDTO = new DataSearchStation_M2();
            searchHotelStation = new SearchHotelStation_M2();
            paramStationName = request.getParameter( "station_name" );

            if ( paramStationName == null )
            {
                paramStationName = "";
            }

            if ( "GET".equals( request.getMethod() ) ) // GETでアクセスされた場合はパラメータをデコードする必要あり
                paramStationName = new String( paramStationName.getBytes( "8859_1" ), "Windows-31J" );

            // スマホからのアクセスならスマホ用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType == UserAgent.USERAGENT_SMARTPHONE )
            {
                String path = "/phone/search/st/searchStationMobile.act";
                URLCodec codec = new URLCodec( "Shift-JIS" );
                String queryStr = "name=" + codec.encode( paramStationName );
                response.sendRedirect( request.getContextPath() + path + "?" + queryStr );
                return;
            }

            stationSearchDTO.setParamStationName( paramStationName );
            stationSearchDTO.setReturn( searchHotelStation
                    .getRailwayStationListByName( paramStationName ) );
            stationSearchDTO.setSearchHotelStation( searchHotelStation );

            request.setAttribute( "SEARCH-RESULT", stationSearchDTO );
            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_station_04_M2.jsp" );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActioStationQuickSearch_M2.execute] Exception="
                    + exception.toString(), exception );
        }
        finally
        {
            stationSearchDTO = null;
            paramStationName = null;
            searchHotelStation = null;
        }

    }

}
