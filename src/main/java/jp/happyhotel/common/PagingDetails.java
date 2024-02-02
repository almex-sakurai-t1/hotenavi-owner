package jp.happyhotel.common;

import java.math.BigDecimal;

/**
 * ページ関連作成クラス
 * 
 * @author HCL.ltd
 * @version 1.00 2008/11/10
 */
public class PagingDetails
{

    /**
     * 表示件数の計算(PC)
     * 
     * @param pageNumber 現在表示しているページ数
     * @param pageRecords 表示件数
     * @param hotelAllCount 全件数
     * @param hotelCount ページ内の件数
     * @return 計算結果（○件～○件 / 全○件）
     */
    public static String getPageRecords(int pageNumber, int pageRecords, int hotelAllCount, int hotelCount)
    {
        String currentPageRecords = null;
        int startPageRecords;
        int endPageRecords;

        startPageRecords = (pageNumber * pageRecords) + 1;

        if ( ((pageNumber + 1) * pageRecords) < hotelAllCount )
        {
            endPageRecords = ((pageNumber + 1) * pageRecords);
        }
        else
        {
            endPageRecords = (pageNumber * pageRecords) + hotelCount;
        }

        currentPageRecords = "<span class=\"current\">" + startPageRecords + "</span>～ <span class=\"current\">" + endPageRecords + "</span>件 / 全<span class=\"current\">" + hotelAllCount + "</span>件";

        return currentPageRecords;
    }

    /**
     * ページリンクの作成(PC)
     * 
     * @param pageNumber 現在表示しているページ
     * @param pageRecords 表示件数
     * @param hotelAllCount 全件数
     * @param queryString リンク先のアドレス（相対または絶対で指定）
     * @return ページリンク
     * @see "queryString パラメータを最低1つは指定する必要がある。"
     */

    public static String getPagenationLink(int pageNumber, int pageRecords, int hotelAllCount, String queryString)
    {
        String pagenationLink = "<span class=\"title\">PAGE</span> ";
        int startPage;
        int i;
        int pageMax;
        pageMax = (hotelAllCount / pageRecords) - 1;
        if ( (hotelAllCount % pageRecords) != 0 )
            pageMax = pageMax + 1;

        if ( pageMax != 0 && pageNumber != 0 )
        {
            pagenationLink = pagenationLink + "<a href=\"" + queryString + "&page=" + (pageNumber - 1) + "\" class=\"page-number\">&lt;&lt;</a>";
        }

        // StartPage Calculation
        if ( (pageNumber - 1) > 0 )
        {
            startPage = pageNumber - 1;
            if ( pageNumber == pageMax )
            {
                startPage = pageNumber - 2;
            }
        }
        else
        {
            startPage = 0;
        }

        for( i = 0 ; i < 3 ; i++ )
        {
            if ( startPage > pageMax )
            {
                break;
            }
            if ( startPage == pageNumber )
            {
                pagenationLink = pagenationLink + "<span class=\"current\"><a href=\"" + queryString + "&page=" + startPage + "\"  class=\"page-number\">" + (startPage + 1) + "</a></span>";
            }
            else
            {
                pagenationLink = pagenationLink + "<a href=\"" + queryString + "&page=" + startPage + "\" class=\"page-number\">" + (startPage + 1) + "</a>";
            }
            startPage++;
        }

        if ( pageNumber < pageMax )
        {
            pagenationLink = pagenationLink + "<a href=\"" + queryString + "&page=" + (pageNumber + 1) + "\" class=\"page-number\">&gt;&gt;</a>";
        }

        return pagenationLink;

    }

    /**
     * クチコミ平均点計算
     * 
     * @param points クチコミの総得点
     * @param bbsAllCount クチコミの件数
     * @return starCount 星の数（0～5まで5段階評価）
     */
    public static int getStarsCount(int points, int bbsAllCount)
    {
        int star;
        int avg;
        int integerAverage = 0;
        int decimalAverage = 0;
        int paramStar = 0;
        BigDecimal pointAverage = null;
        BigDecimal decimalAvg;
        avg = 0;
        decimalAvg = new BigDecimal( 1.2 );

        if ( bbsAllCount != 0 )
        {
            avg = points / bbsAllCount;
            decimalAvg = new BigDecimal( Integer.toString( avg ) );
            pointAverage = decimalAvg.movePointLeft( 2 );
        }
        if ( pointAverage != null )
        {
            pointAverage = pointAverage.movePointRight( 2 );
            integerAverage = pointAverage.intValue() / 100;
            decimalAverage = pointAverage.intValue() % 100;
            pointAverage = pointAverage.movePointLeft( 2 );
        }

        for( star = 0 ; star < 5 ; star++ )
        {
            if ( star < integerAverage )
            {
                paramStar = paramStar + 1;
            }
            else if ( decimalAverage >= 50 )
            {
                decimalAverage = 0;
                paramStar = paramStar + 1;
            }
            else
            {
                paramStar = paramStar + 0;
            }
        }
        return paramStar;
    }

    /**
     * 表示件数の計算(携帯)
     * 
     * @param pageNumber 現在のページ
     * @param pageRecords 表示件数
     * @param hotelAllCount 全件数
     * @param hotelCount ページ内の件数
     * @param type 表示件数の指定<br>
     *            （1:○件中 ○件～○件を表示,2:○件～○件 / 全○件,それ以外:○件～○件を表示 ）
     * @return 表示件数
     */
    public static String getPageRecordsMobile(int pageNumber, int pageRecords, int hotelAllCount, int hotelCount, int type)
    {
        String currentPageRecords = null;
        int startPageRecords;
        int endPageRecords;

        startPageRecords = (pageNumber * pageRecords) + 1;

        if ( ((pageNumber + 1) * pageRecords) < hotelAllCount )
        {
            endPageRecords = ((pageNumber + 1) * pageRecords);
        }
        else
        {
            endPageRecords = (pageNumber * pageRecords) + hotelCount;
        }

        if ( type == 1 )
        {
            currentPageRecords = hotelAllCount + "件中&nbsp;" + startPageRecords + "～" + endPageRecords + "件を表示";
        }
        else if ( type == 2 )
        {
            currentPageRecords = startPageRecords + "～" + endPageRecords + "件 / 全" + hotelAllCount + "件";
        }
        else
        {
            currentPageRecords = startPageRecords + "～" + endPageRecords + "件を表示";
        }
        return currentPageRecords;
    }

    /**
     * ページリンクの作成(携帯)
     * 
     * @param numPage 現在のページ
     * @param pageRecords 表示件数
     * @param hotelAllCount 全件数
     * @param queryString リンク先のアドレス（相対または絶対で指定）
     * @param paramUidLink UIDリンク（uidを取得するためのリンク）
     * @return ページリンク
     * @see "queryString パラメータを最低1つは指定する必要がある。"
     */

    public static String getPagenationLinkMobile(int numPage, int pageRecords, int hotelAllCount, String queryString, String paramUidLink)
    {
        String pagenationLink = "";
        int tempMax;
        int tempMin;
        int maxPage;
        int minPage;

        if ( hotelAllCount % pageRecords == 0 )
        {
            tempMax = hotelAllCount / pageRecords - 1;
        }
        else
        {
            tempMax = hotelAllCount / pageRecords;
        }
        tempMin = 0;
        maxPage = tempMax;
        minPage = tempMin;
        if ( (numPage == 0) && (tempMax >= 4) )
        {
            maxPage = 4;
        }
        else if ( (numPage == 1) && (tempMax >= 4) )
        {
            maxPage = 4;
        }
        else if ( (numPage + 1) < tempMax )
        {
            maxPage = numPage + 2;
        }

        if ( (numPage >= 4) && (numPage == tempMax) )
        {
            minPage = numPage - 4;
        }
        else if ( (numPage >= 3) && (numPage == tempMax - 1) )
        {
            minPage = numPage - 3;
        }
        else if ( (tempMin < numPage - 2) )
        {
            minPage = numPage - 2;
        }
        for( int i = minPage ; i <= maxPage ; i++ )
        {
            if ( i == numPage )
            {
                if ( tempMax != 0 )
                {

                    pagenationLink = pagenationLink + (i + 1) + "/ ";

                }
            }
            else
            {

                pagenationLink = pagenationLink + "<a href=\"" + queryString + "&page=" + i + "&" + paramUidLink + "\"> " + (i + 1) + "</a>/ ";

            }
        }

        return pagenationLink;

    }

    /**
     * ページリンク作成(PCこだわり検索用)
     * 
     * @param pageNumber 現在表示しているページ
     * @param pageRecords 表示件数
     * @param pageMax ページリンクの最大数
     * @param hotelAllCount 全件数
     * @param queryString リンク先のアドレス（相対または絶対で指定）
     * @return ページリンク
     * @see "queryString パラメータを最低1つは指定する必要がある。"
     */

    public static String getPagenationLinkSorted(int pageNumber, int pageRecords, int pageMax, int hotelAllCount, String queryString, String paramSort)
    {
        String pagenationLink = "<span class=\"title\">PAGE</span> ";
        int startPage;
        int i;
        if ( pageMax != 0 && pageNumber != 0 )
        {
            pagenationLink = pagenationLink + "<a href=\"" + queryString + "&page=" + (pageNumber - 1) + "&sort=" + paramSort + "\" class=\"page-number\">&lt;&lt;</a>";
        }

        // StartPage Calculation
        if ( (pageNumber - 1) > 0 )
        {
            startPage = pageNumber - 1;
            if ( pageNumber == pageMax )
            {
                startPage = pageNumber - 2;
            }
        }
        else
        {
            startPage = 0;
        }

        for( i = 0 ; i < 3 ; i++ )
        {
            if ( startPage > pageMax )
            {
                break;
            }
            if ( startPage == pageNumber )
            {
                pagenationLink = pagenationLink + "<span class=\"current\"><a href=\"" + queryString + "&page=" + startPage + "&sort=" + paramSort + "\"  class=\"page-number\">" + (startPage + 1) + "</a></span>";
            }
            else
            {
                pagenationLink = pagenationLink + "<a href=\"" + queryString + "&page=" + startPage + "&sort=" + paramSort + "\" class=\"page-number\">" + (startPage + 1) + "</a>";
            }
            startPage++;
        }

        if ( pageNumber < pageMax )
        {
            pagenationLink = pagenationLink + "<a href=\"" + queryString + "&page=" + (pageNumber + 1) + "&sort=" + paramSort + "\" class=\"page-number\">&gt;&gt;</a>";
        }

        return pagenationLink;

    }

    /**
     * ページリンク作成(ホテナビ検索用)
     * 
     * @param pageNumber 現在表示しているページ
     * @param pageRecords 表示件数
     * @param hotelAllCount 全件数
     * @param queryString リンク先のアドレス（相対または絶対で指定）
     * @param paramUidLink UIDリンク
     * @return ページリンク
     * @see "queryString パラメータを最低1つは指定する必要がある。"
     */

    public static String getPagenationLinkHotenavi(int pageNumber, int pageRecords, int hotelAllCount, String queryString, String paramUidLink)
    {
        String pagenationLink = "";
        int startPage;
        int i;
        int pageMax;
        pageMax = (hotelAllCount / pageRecords) - 1;
        if ( (hotelAllCount % pageRecords) != 0 )
            pageMax = pageMax + 1;

        if ( pageMax != 0 && pageNumber != 0 )
        {
            pagenationLink = pagenationLink + "<a href=\"" + queryString + "&page=" + (pageNumber - 1) + "&uid=" + paramUidLink + "\"> &lt;&lt;</a> ";
        }

        // StartPage Calculation
        if ( (pageNumber - 1) > 0 )
        {
            startPage = pageNumber - 1;
            if ( pageNumber == pageMax )
            {
                startPage = pageNumber - 2;
            }
        }
        else
        {
            startPage = 0;
        }

        for( i = 0 ; i < 3 ; i++ )
        {
            if ( startPage > pageMax )
            {
                break;
            }
            if ( startPage == pageNumber )
            {
                pagenationLink = pagenationLink + "<a href=\"" + queryString + "&page=" + startPage + "&" + paramUidLink + "\"> " + (startPage + 1) + "</a> ";
            }
            else
            {
                pagenationLink = pagenationLink + "<a href=\"" + queryString + "&page=" + startPage + "&" + paramUidLink + "\"> " + (startPage + 1) + "</a> ";
            }
            startPage++;
        }

        if ( pageNumber < pageMax )
        {
            pagenationLink = pagenationLink + "<a href=\"" + queryString + "&page=" + (pageNumber + 1) + "&" + paramUidLink + "\">&gt;&gt;</a> ";
        }

        return pagenationLink;

    }

    /**
     * ページリンク作成(こだわり検索用)
     * 
     * @param numPage 現在のページ
     * @param pageRecords 表示件数
     * @param hotelAllCount 全件数
     * @param queryString リンク先のアドレス（相対または絶対で指定）
     * @param paramUidLink UIDリンク（uidを取得するためのリンク）
     * @param paramSort こだわり検索用のパラメータ（0:標準の並び順、1:クチコミ平均の高い順）
     * @return ページリンク
     * @see "queryString パラメータを最低1つは指定する必要がある。"
     */

    public static String getPagenationLinkMobileSorted(int numPage, int pageRecords, int hotelAllCount, String queryString, String paramUidLink, String paramSort)
    {
        String pagenationLink = "";
        int tempMax;
        int tempMin;
        int maxPage;
        int minPage;

        if ( hotelAllCount % pageRecords == 0 )
        {
            tempMax = hotelAllCount / pageRecords - 1;
        }
        else
        {
            tempMax = hotelAllCount / pageRecords;
        }
        tempMin = 0;
        maxPage = tempMax;
        minPage = tempMin;
        if ( (numPage == 0) && (tempMax >= 4) )
        {
            maxPage = 4;
        }
        else if ( (numPage == 1) && (tempMax >= 4) )
        {
            maxPage = 4;
        }
        else if ( (numPage + 1) < tempMax )
        {
            maxPage = numPage + 2;
        }

        if ( (numPage >= 4) && (numPage == tempMax) )
        {
            minPage = numPage - 4;
        }
        else if ( (numPage >= 3) && (numPage == tempMax - 1) )
        {
            minPage = numPage - 3;
        }
        else if ( (tempMin < numPage - 2) )
        {
            minPage = numPage - 2;
        }
        for( int i = minPage ; i <= maxPage ; i++ )
        {
            if ( i == numPage )
            {
                if ( tempMax != 0 )
                {
                    pagenationLink = pagenationLink + (i + 1) + "/ ";
                }
            }
            else
            {
                pagenationLink = pagenationLink + "<a href=\"" + queryString + "&page=" + i + "&sort=" + paramSort + "&" + paramUidLink + "\"> " + (i + 1) + "</a>/ ";
            }
        }
        return pagenationLink;
    }

    /**
     * ページリンクの作成(スマホ)
     * 
     * @param pageNumber 現在表示しているページ
     * @param pageRecords 表示件数
     * @param hotelAllCount 全件数
     * @param queryString リンク先のアドレス（相対または絶対で指定）
     * @return ページリンク
     * @see "queryString パラメータを最低1つは指定する必要がある。"
     */

    public static String getPagenationLinkSp(int numPage, int pageRecords, int hotelAllCount, String queryString)
    {
        String pagenationLink = "";
        int tempMax;
        int tempMin;
        int maxPage;
        int minPage;
        int i = 0;

        if ( hotelAllCount % pageRecords == 0 )
        {
            tempMax = hotelAllCount / pageRecords - 1;
        }
        else
        {
            tempMax = hotelAllCount / pageRecords;
        }
        tempMin = 0;
        maxPage = tempMax;
        minPage = tempMin;
        if ( (numPage == 0) && (tempMax >= 4) )
        {
            maxPage = 4;
        }
        else if ( (numPage == 1) && (tempMax >= 4) )
        {
            maxPage = 4;
        }
        else if ( (numPage + 1) < tempMax )
        {
            maxPage = numPage + 2;
        }

        if ( (numPage >= 4) && (numPage == tempMax) )
        {
            minPage = numPage - 4;
        }
        else if ( (numPage >= 3) && (numPage == tempMax - 1) )
        {
            minPage = numPage - 3;
        }
        else if ( (tempMin < numPage - 2) )
        {
            minPage = numPage - 2;
        }
        for( i = minPage ; i <= maxPage ; i++ )
        {
            if ( i == numPage )
            {
                pagenationLink = pagenationLink + "<li><span class=\"active\">" + (i + 1) + "</span></li>";
            }
            else
            {
                pagenationLink = pagenationLink + "<li><a href=\"" + queryString + "&page=" + i + "\">" + (i + 1) + "</a></li>";
            }
        }

        if ( i > 0 )
        {
            pagenationLink = "<section class=\"pagination clear\"><ul>" + pagenationLink + "</ul></section>";
        }

        return pagenationLink;
    }

    /**
     * ページリンクの作成(スマホ)
     * 
     * @param pageNumber 現在表示しているページ
     * @param pageRecords 表示件数
     * @param hotelAllCount 全件数
     * @param queryString リンク先のアドレス（相対または絶対で指定）
     * @return ページリンク
     * @see "queryString パラメータを最低1つは指定する必要がある。"
     */

    public static String getPagenationLinkSp2(int numPage, int pageRecords, int hotelAllCount, String queryString)
    {
        String pagenationLink = "";
        int tempMax;
        int tempMin;
        int maxPage;
        int minPage;
        int i = 0;

        if ( hotelAllCount % pageRecords == 0 )
        {
            tempMax = hotelAllCount / pageRecords - 1;
        }
        else
        {
            tempMax = hotelAllCount / pageRecords;
        }
        tempMin = 0;
        maxPage = tempMax;
        minPage = tempMin;
        if ( (numPage == 0) && (tempMax >= 4) )
        {
            maxPage = 4;
        }
        else if ( (numPage == 1) && (tempMax >= 4) )
        {
            maxPage = 4;
        }
        else if ( (numPage + 1) < tempMax )
        {
            maxPage = numPage + 2;
        }

        if ( (numPage >= 4) && (numPage == tempMax) )
        {
            minPage = numPage - 4;
        }
        else if ( (numPage >= 3) && (numPage == tempMax - 1) )
        {
            minPage = numPage - 3;
        }
        else if ( (tempMin < numPage - 2) )
        {
            minPage = numPage - 2;
        }
        for( i = minPage ; i <= maxPage ; i++ )
        {
            if ( i == numPage )
            {
                pagenationLink = pagenationLink + "<li><span class=\"active\">" + (i + 1) + "</span></li>";
            }
            else
            {
                pagenationLink = pagenationLink + "<li><a href=\"" + queryString + "&page=" + i + "\">" + (i + 1) + "</a></li>";
            }
        }

        if ( i > 0 )
        {
            pagenationLink = "<ul>" + pagenationLink + "</ul>";
        }

        return pagenationLink;
    }

}
