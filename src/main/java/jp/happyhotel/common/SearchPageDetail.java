package jp.happyhotel.common;

import java.math.BigDecimal;

public class SearchPageDetail
{

    /**
     * �\�������̌v�Z
     * 
     * @param pageNumber �y�[�W�ԍ�
     * @param pageRecords �y�[�W�\������
     * @param hotelAllCount �S�z�e����
     * @param hotelCount 1�y�[�W�ɕ\������z�e������
     * @return String
     */
    public static String getPageRecords(int pageNumber, int pageRecords, int hotelAllCount, int hotelCount)
    {
        String currentPageRecords = null;
        int startPageRecords;
        int endPageRecords;

        startPageRecords = (pageNumber * 20) + 1;

        if ( ((pageNumber + 1) * pageRecords) < hotelAllCount )
        {
            endPageRecords = ((pageNumber + 1) * pageRecords);
        }
        else
        {
            endPageRecords = (pageNumber * pageRecords) + hotelCount;
        }

        currentPageRecords = "<span class=\"current\">" + startPageRecords + "</span>�`<span class=\"current\">" + endPageRecords + "</span>��/ �S<span class=\"current\">" + hotelAllCount + "</span>��";

        return currentPageRecords;
    }

    /**
     * �y�[�W�����N�쐬
     * 
     * @param pageNumber �y�[�W�ԍ�
     * @param pageRecords �y�[�W�\������
     * @param pageMax �y�[�W�����N��\������ő匏���i��F5��5�y�[�W���̃y�[�W�����N��\���j
     * @param hotelAllCount �S�z�e����
     * @param queryString �N�G���[�X�g�����O
     * @return String
     */

    public static String getPagenationLink(int pageNumber, int pageRecords, int pageMax, int hotelAllCount, String queryString)
    {
        String pagenationLink = "<span class=\"title\">PAGE</span> ";
        int startPage;
        int i;
        if ( pageMax != 0 && pageNumber != 0 )
        {
            pagenationLink = pagenationLink + "<a href=\"searchResultFreeword.act?" + queryString + "&page=" + (pageNumber - 1) + "\" class=\"page-number\">&lt;&lt;</a>";
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
                pagenationLink = pagenationLink + "<span class=\"current\"><a href=\"searchResultFreeword.act?" + queryString + "&page=" + startPage + "\"  class=\"page-number\">" + (startPage + 1) + "</a></span>";
            }
            else
            {
                pagenationLink = pagenationLink + "<a href=\"searchResultFreeword.act?" + queryString + "&page=" + startPage + "\" class=\"page-number\">" + (startPage + 1) + "</a>";
            }
            startPage++;
        }

        if ( pageNumber < pageMax )
        {
            pagenationLink = pagenationLink + "<a href=\"searchResultFreeword.act?" + queryString + "&page=" + (pageNumber + 1) + "\" class=\"page-number\">&gt;&gt;</a>";
        }

        return pagenationLink;

    }

    /**
     * �N�`�R�~���ϓ_�v�Z
     * 
     * @param points �|�C���g
     * @param bbsAllCount �N�`�R�~���[��
     * @return starCount �N�`�R�~���ϓ_
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

}
