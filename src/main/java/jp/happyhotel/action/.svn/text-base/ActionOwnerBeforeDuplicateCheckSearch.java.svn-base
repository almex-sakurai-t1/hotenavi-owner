package jp.happyhotel.action;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMileBalanceData;
import jp.happyhotel.data.DataSameUUIDData;
import jp.happyhotel.search.SearchMileBalanceData;
import jp.happyhotel.search.SearchSameUUIDData;
import jp.happyhotel.util.DuplicateIDCheckUtil;

public class ActionOwnerBeforeDuplicateCheckSearch
{

    /**
     * 検索ユーザと同じuuidのユーザ一覧のHTMLを取得
     * 
     * @param request リクエスト
     * @return
     */
    public String sameUUIDSearch(HttpServletRequest request)
    {

        StringBuilder resultScript = new StringBuilder();

        String user_id = request.getParameter( "user_id" );
        String init = request.getParameter( "init" );

        StringBuilder uuidHistoryResultHtml = new StringBuilder();

        SearchSameUUIDData searchUUIDData = null;
        DataSameUUIDData[] uuidDatas = null;
        DataSameUUIDData uuidData = null;

        String userID = null;

        if ( user_id != null )
        {

            // 渡されてきたuser_idの前後のスペース（全角も含む）を取り除く
            user_id = user_id.trim().replaceAll( "^[\\s　]*", "" ).replaceAll( "[\\s　]*$", "" );

            searchUUIDData = new SearchSameUUIDData();
            searchUUIDData.setUser_id( user_id );

            try
            {
                if ( searchUUIDData.setSameUUIDDatas() )
                {
                    uuidDatas = searchUUIDData.getSameUUIDDatas();
                    int recordSize = uuidDatas.length;
                    int pageNum = 1;
                    if ( request.getParameter( "page_num" ) != null )
                    {
                        pageNum = Integer.parseInt( request.getParameter( "page_num" ) );
                    }

                    final int recordsNumPrePage = 10;
                    final int showpage_numMaxCount = 11;

                    if ( recordSize > 0 )
                    {
                        
                        int baseNum = (pageNum - 1) * recordsNumPrePage;

                        for( int i = 0 ; i < recordSize ; i++ )
                        {

                            int currentRow = i + 1;
                            if ( baseNum < currentRow && currentRow <= baseNum + recordsNumPrePage )
                            {
                                uuidData = uuidDatas[i];

                                userID = uuidData.getUser_id();
                                uuidHistoryResultHtml.append( "<tr class='honbun'" );
                                uuidHistoryResultHtml.append( " onmouseover=\"this.style.backgroundColor='#FFDDDD';\"" );
                                if ( userID.equals( user_id ) )
                                {
                                    uuidHistoryResultHtml.append( " onmouseout=\"this.style.backgroundColor='#FFEEFF';\"" );
                                    uuidHistoryResultHtml.append( " style='cursor: pointer; background-color: #FFEEFF;'" );
                                }
                                else
                                {
                                    uuidHistoryResultHtml.append( " onmouseout=\"this.style.backgroundColor='#FFFFFF';\"" );
                                    uuidHistoryResultHtml.append( " style='cursor: pointer;'" );
                                }
                                uuidHistoryResultHtml.append( " onclick=\"showMileHistory(this)\">" );
                                uuidHistoryResultHtml.append( "<td align='right'>" ).append( currentRow ).append( "</td>" );
                                uuidHistoryResultHtml.append( "<td align='left'>" ).append( userID ).append( "</td>" );
                                uuidHistoryResultHtml.append( "<td align='right'>" ).append( String.format( "%,3d", uuidData.getPoint_sum() ) ).append( "</td>" );
                                uuidHistoryResultHtml.append( "</tr>" );
                            }
                        }

                        resultScript.append( "u1 = '" + user_id + "';" );
                        
                    }
                    else
                    {
                        uuidHistoryResultHtml.append( "<tr class='honbun'>" );
                        uuidHistoryResultHtml.append( "<td align='center' colspan='7'>データがありません</td>" );
                        uuidHistoryResultHtml.append( "</tr>" );

                        resultScript.append( "initTransitionStatus();" );
                        resultScript.append( "u1 = '';" );
                    }
                    
                    if ( init != null && "1".equals( init ) )
                    {
                        resultScript.append( "u2 = '';" );
                    }
                    
                    resultScript.append( "$('#uuidHistoryResultBody').html(\"" );
                    resultScript.append( uuidHistoryResultHtml.toString().replace( "\"", "\\\"" ) );
                    resultScript.append( "\");" );

                    resultScript.append( DuplicateIDCheckUtil.getPagingHtmlScript(
                            "same_uuid_result.jsp",
                            user_id,
                            recordsNumPrePage,
                            showpage_numMaxCount,
                            recordSize,
                            pageNum,
                            "uuidHistoryResultPagingDiv" ) );

                }

            }
            catch ( Exception e )
            {
                Logging.error( "error : " + e.getMessage() + "\n " );
                resultScript.append( "alert('エラーが発生しました。\\n" + e.toString() + "');" );
            }
            finally
            {
                user_id = null;

                uuidHistoryResultHtml = null;

                searchUUIDData = null;
                uuidDatas = null;
                uuidData = null;

                userID = null;
            }

        }
        else
        {
            resultScript.append( "alert('ユーザIDが正常に取得できませんでした。');" );
        }

        return resultScript.toString();
    }

    /**
     * 指定ユーザのポイント履歴一覧のHTMLを取得
     * 
     * @param request
     * @return
     */
    public String mileBalanceSearch(HttpServletRequest request)
    {

        StringBuilder resultScript = new StringBuilder();

        String user_id = request.getParameter( "user_id" );
        String init = request.getParameter( "init" );

        StringBuilder uuidHistoryResultHtml = new StringBuilder();

        SearchMileBalanceData searchMileBalanceData = null;
        DataMileBalanceData[] mileBalanceDatas = null;
        DataMileBalanceData mileBalanceData = null;

        if ( user_id != null )
        {

            // 渡されてきたuser_idの前後のスペース（全角も含む）を取り除く
            user_id = user_id.trim().replaceAll( "^[\\s　]*", "" ).replaceAll( "[\\s　]*$", "" );

            searchMileBalanceData = new SearchMileBalanceData();
            searchMileBalanceData.setUser_id( user_id );

            try
            {

                if ( searchMileBalanceData.ｓetMileBalanceDatas() )
                {

                    mileBalanceDatas = searchMileBalanceData.getMileBalanceDatas();
                    int recordSize = mileBalanceDatas.length;

                    int pageNum = 1;
                    if ( request.getParameter( "page_num" ) != null )
                    {
                        pageNum = Integer.parseInt( request.getParameter( "page_num" ) );
                    }

                    final int recordsNumPrePage = 10;
                    final int showpage_numMaxCount = 11;

                    if ( recordSize > 0 )
                    {

                        int baseNum = (pageNum - 1) * recordsNumPrePage;

                        for( int i = 0 ; i < recordSize ; i++ )
                        {
                            int currentRow = i + 1;
                            if ( baseNum < currentRow && currentRow <= baseNum + recordsNumPrePage )
                            {
                                mileBalanceData = mileBalanceDatas[i];

                                uuidHistoryResultHtml.append( "<tr class='honbun' style='cursor: pointer;' onmouseover=\"this.style.backgroundColor='#FFDDDD';\" onmouseout=\"this.style.backgroundColor='#FFFFFF';\" >" );
                                uuidHistoryResultHtml.append( "<td align='right'>" ).append( currentRow ).append( "</td>" );
                                uuidHistoryResultHtml.append( "<td align='left'>" ).append( mileBalanceData.getUser_id() ).append( "</td>" );
                                uuidHistoryResultHtml.append( "<td align='right'>" ).append( mileBalanceData.getSeq() ).append( "</td>" );
                                uuidHistoryResultHtml.append( "<td align='right'>" ).append( mileBalanceData.getCode() ).append( "</td>" );
                                uuidHistoryResultHtml.append( "<td align='center'>" ).append( mileBalanceData.getGet_date() ).append( "</td>" );
                                uuidHistoryResultHtml.append( "<td align='center'>" ).append( mileBalanceData.getGet_time() ).append( "</td>" );
                                uuidHistoryResultHtml.append( "<td align='right'>" ).append( String.format( "%,3d", mileBalanceData.getPoint() ) ).append( "</td>" );
                                uuidHistoryResultHtml.append( "<td align='right'>" ).append( mileBalanceData.getPoint_kind() ).append( "</td>" );
                                uuidHistoryResultHtml.append( "<td align='right'>" ).append( mileBalanceData.getExt_code() ).append( "</td>" );
                                uuidHistoryResultHtml.append( "<td align='left' style='overflow: hidden; text-overflow: ellipsis; white-space: nowrap;' title='" + mileBalanceData.getExt_string() + "'>" ).append( mileBalanceData.getExt_string() )
                                        .append( "</td>" );
                                uuidHistoryResultHtml.append( "<td align='right'>" ).append( String.format( "%,3d", mileBalanceData.getUsed_point() ) ).append( "</td>" );
                                uuidHistoryResultHtml.append( "<td align='right'>" ).append( String.format( "%,3d", mileBalanceData.getExpired_point() ) ).append( "</td>" );
                                uuidHistoryResultHtml.append( "</tr>" );
                            }
                        }

                    }
                    else
                    {
                        uuidHistoryResultHtml.append( "<tr class='honbun'>" );
                        uuidHistoryResultHtml.append( "<td align='center' colspan='12'>データがありません</td>" );
                        uuidHistoryResultHtml.append( "</tr>" );
                    }

                    if ( uuidHistoryResultHtml.length() > 0 )
                    {
                        resultScript.append( "$('#userMileBalanceBody').html(\"" );
                        resultScript.append( uuidHistoryResultHtml.toString().replace( "\"", "\\\"" ) );
                        resultScript.append( "\");" );
                    }

                    resultScript.append( DuplicateIDCheckUtil.getPagingHtmlScript(
                            "mile_balance_result.jsp",
                            user_id,
                            recordsNumPrePage,
                            showpage_numMaxCount,
                            recordSize,
                            pageNum,
                            "userMileBalancePagingDiv" ) );

                    if ( init != null && "1".equals( init ) )
                    {
                        resultScript.append( "showTransitionDiv(" + recordSize + ");" );
                    }
                }

            }
            catch ( Exception e )
            {
                Logging.error( "error : " + e.getMessage() + "\n " );
            }
            finally
            {
                user_id = null;
                init = null;

                uuidHistoryResultHtml = null;

                searchMileBalanceData = null;
                mileBalanceDatas = null;
                mileBalanceData = null;
            }

        }
        else
        {
            resultScript.append( "alert('ユーザIDが正常に取得できませんでした。')" );
        }

        return resultScript.toString();

    }

}
