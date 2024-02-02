package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

// 検索結果共通レスポンス
public class GenerateXmlSearchResult extends WebApiResultBase
{
    // タグ名
    private static final String                     TAG_ERROR        = "error";
    private static final String                     TAG_RESULT_COUNT = "ResultCount";

    private XmlTag                                  error;
    private XmlTag                                  resultCount;
    private GenerateXmlAd                           xmlAd;
    private ArrayList<GenerateXmlAd>                adRandom         = new ArrayList<GenerateXmlAd>();
    private ArrayList<GenerateXmlSearchResultHotel> hotel            = new ArrayList<GenerateXmlSearchResultHotel>();

    @Override
    protected void initXmlNodeInfo()
    {
        XmlTag.setParent( root, error );
        XmlTag.setParent( root, resultCount );

        if ( xmlAd != null )
        {
            xmlAd.setRootNode( root );
            xmlAd.initXmlNodeInfo();
        }

        if ( adRandom != null )
        {
            for( int i = 0 ; i < adRandom.size() ; i++ )
            {
                adRandom.get( i ).setRootNode( root );
                adRandom.get( i ).initXmlNodeInfo();
            }
        }

        if ( hotel != null )
        {
            for( int i = 0 ; i < hotel.size() ; i++ )
            {
                hotel.get( i ).setRootNode( root );
                hotel.get( i ).initXmlNodeInfo();
            }
        }

        return;
    }

    public void setError(String message)
    {
        error = XmlTag.createXmlTag( TAG_ERROR, message );
        return;
    }

    public void setResultCount(int count)
    {
        resultCount = XmlTag.createXmlTag( TAG_RESULT_COUNT, count );
        return;
    }

    public void setAd(GenerateXmlAd ad)
    {
        xmlAd = ad;
        return;
    }

    public void addAd(GenerateXmlAd ad)
    {
        adRandom.add( ad );
        return;
    }

    public void addHotel(GenerateXmlSearchResultHotel addHotel)
    {
        hotel.add( addHotel );
        return;
    }
}
