package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ情報
 */
public class GenerateXmlHapiTouchGetConfigPoint extends WebApiResultBase
{
    // タグ名
    private static final String                               TAG_POINT            = "Point";
    private static final String                               TAG_NOW_ID           = "NowId";
    private static final String                               TAG_DEFAULT          = "Default";
    private static final String                               TAG_VISIT_RATE       = "VisitRate";
    private static final String                               TAG_AMOUNT_RATE      = "AmountRate";
    private static final String                               TAG_RSV_RATE         = "RsvRate";
    private static final String                               TAG_VISIT_RATE_FREE  = "VisitRateFree";
    private static final String                               TAG_AMOUNT_RATE_FREE = "AmountRateFree";
    private static final String                               TAG_RSV_RATE_FREE    = "RsvRateFree";

    private XmlTag                                            point;
    private XmlTag                                            nowId;
    private XmlTag                                            xmlDefault;
    private XmlTag                                            visitRate;
    private XmlTag                                            amountRate;
    private XmlTag                                            rsvRate;
    private XmlTag                                            visitRateFree;
    private XmlTag                                            amountRateFree;
    private XmlTag                                            rsvRateFree;
    private ArrayList<GenerateXmlHapiTouchGetConfigPointData> dataList             = new ArrayList<GenerateXmlHapiTouchGetConfigPointData>();

    @Override
    protected void initXmlNodeInfo()
    {
        point = createRootChild( TAG_POINT );
        XmlTag.setParent( point, nowId );
        XmlTag.setParent( point, xmlDefault );

        XmlTag.setParent( xmlDefault, visitRate );
        XmlTag.setParent( xmlDefault, amountRate );
        XmlTag.setParent( xmlDefault, rsvRate );
        XmlTag.setParent( xmlDefault, visitRateFree );
        XmlTag.setParent( xmlDefault, amountRateFree );
        XmlTag.setParent( xmlDefault, rsvRateFree );

        if ( dataList != null )
        {
            for( int i = 0 ; i < dataList.size() ; i++ )
            {
                this.dataList.get( i ).setRootNode( point );
                this.dataList.get( i ).initXmlNodeInfo();
            }

        }
        return;
    }

    public void setNowId(int seq)
    {
        this.nowId = XmlTag.createXmlTag( TAG_NOW_ID, seq );
    }

    public void setDefault(String strDefault)
    {
        this.xmlDefault = XmlTag.createXmlTag( TAG_DEFAULT, strDefault );
    }

    public void setData(GenerateXmlHapiTouchGetConfigPointData data)
    {
        this.dataList.add( data );
    }

    public void setVisitRate(double visitrate)
    {
        if ( visitrate == 0 )
        {
            visitrate = 1;
        }
        this.visitRate = XmlTag.createXmlTag( TAG_VISIT_RATE, visitrate );
    }

    public void setAmountRate(double amountrate)
    {
        if ( amountrate == 0 )
        {
            amountrate = 1;
        }
        this.amountRate = XmlTag.createXmlTag( TAG_AMOUNT_RATE, amountrate );
    }

    public void setRsvRate(double rsvrate)
    {
        if ( rsvrate == 0 )
        {
            rsvrate = 1;
        }
        this.rsvRate = XmlTag.createXmlTag( TAG_RSV_RATE, rsvrate );
    }

    public void setVisitRateFree(double visitrateFree)
    {
        if ( visitrateFree == 0 )
        {
            visitrateFree = 1;
        }
        this.visitRateFree = XmlTag.createXmlTag( TAG_VISIT_RATE_FREE, visitrateFree );
    }

    public void setAmountRateFree(double amountrateFree)
    {
        if ( amountrateFree == 0 )
        {
            amountrateFree = 1;
        }
        this.amountRateFree = XmlTag.createXmlTag( TAG_AMOUNT_RATE_FREE, amountrateFree );
    }

    public void setRsvRateFree(double rsvratefree)
    {
        if ( rsvratefree == 0 )
        {
            rsvratefree = 1;
        }
        this.rsvRateFree = XmlTag.createXmlTag( TAG_RSV_RATE_FREE, rsvratefree );
    }

}
