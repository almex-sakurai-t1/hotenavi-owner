package jp.happyhotel.util;

public class DuplicateIDCheckUtil
{

    /**
     * 重複ID確認ツール、一覧ページングHTMLスクリプトを生成
     * 
     * @param jsp_name
     * @param user_id
     * @param recordsNumPrePage
     * @param showpage_numMaxCount
     * @param recordSize
     * @param page_num
     * @param paging_div_id
     * @return
     */
    public static String getPagingHtmlScript(
            String jsp_name,
            String user_id,
            int recordsNumPrePage,
            int showpage_numMaxCount,
            int recordSize,
            int page_num,
            String paging_div_id)
    {

        StringBuilder jsScript = new StringBuilder();
        StringBuilder pagingHtml = new StringBuilder();
        StringBuilder uuidHistoryGoFirstHtml, uuidHistoryGoLastHtml = null;

        if ( recordSize > recordsNumPrePage )
        {

            int halfNum = showpage_numMaxCount / 2;

            uuidHistoryGoFirstHtml = new StringBuilder();
            uuidHistoryGoLastHtml = new StringBuilder();

            int pagesCount = recordSize / recordsNumPrePage + (recordSize % 10 > 0 ? 1 : 0);

            int startPage = 0;
            int endPage = 0;

            if ( pagesCount <= showpage_numMaxCount )
            {
                startPage = 1;
                endPage = pagesCount;
            }
            else
            {
                startPage = page_num - halfNum;
                endPage = page_num + halfNum;
                if ( startPage < 1 )
                {
                    startPage = 1;
                    endPage = showpage_numMaxCount;
                }
                else if ( endPage > pagesCount )
                {
                    startPage = pagesCount - showpage_numMaxCount + 1;
                    endPage = pagesCount;
                }

                if ( startPage > 1 )
                {
                    uuidHistoryGoFirstHtml.append( "<a class='link1' onclick=\"" + getClickEvent( jsp_name, user_id, 1 ) + "\">先頭へ</a>" );
                }
                if ( endPage < pagesCount )
                {
                    uuidHistoryGoLastHtml.append( "<a class='link1' onclick=\"" + getClickEvent( jsp_name, user_id, pagesCount ) + "\">最後へ</a>" );
                }
            }

            pagingHtml.append( "<p style='margin-top: 0;'>" );
            pagingHtml.append( uuidHistoryGoFirstHtml.toString() );
            for( int pNum = startPage ; pNum <= endPage ; pNum++ )
            {
                if ( pNum == page_num )
                {
                    pagingHtml.append( "<span style='margin-left: 10px;'>" ).append( pNum ).append( "</span>" );
                }
                else
                {
                    pagingHtml.append( "<a class='link1' onclick=\"" + getClickEvent( jsp_name, user_id, pNum ) + "\">" ).append( pNum ).append( "</a>" );
                }
            }
            pagingHtml.append( uuidHistoryGoLastHtml.toString() );
            pagingHtml.append( "</p>" );
        }

        jsScript.append( "$('#" + paging_div_id + "').html(\"" );
        jsScript.append( pagingHtml.toString().replace( "\"", "\\\"" ) );
        jsScript.append( "\");" );

        return jsScript.toString();
    }

    private static String getClickEvent(String jsp_name, String user_id, int pageNum)
    {
        String paramStr = "";
        paramStr += "'" + jsp_name + "'";
        paramStr += ", 'user_id=" + user_id;
        paramStr += "&page_num=" + pageNum + "'";
        return "doAjax(" + paramStr + ")";
    }

}
