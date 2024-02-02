package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ情報
 */
public class GenerateXmlHapiTouchGetConfigPointData extends WebApiResultBase
{
    // タグ名
    private static final String TAG_DATA             = "Data";
    private static final String TAG_ID               = "Id";
    private static final String TAG_START_DATE       = "StartDate";
    private static final String TAG_END_DATE         = "EndDate";
    private static final String TAG_VISIT_RATE       = "VisitRate";
    private static final String TAG_AMOUNT_RATE      = "AmountRate";
    private static final String TAG_RSV_RATE         = "RsvRate";
    private static final String TAG_VISIT_RATE_FREE  = "VisitRateFree";
    private static final String TAG_AMOUNT_RATE_FREE = "AmountRateFree";
    private static final String TAG_RSV_RATE_FREE    = "RsvRateFree";

    private XmlTag              data;
    private XmlTag              id;
    private XmlTag              startDate;
    private XmlTag              endDate;
    private XmlTag              visitRate;
    private XmlTag              amountRate;
    private XmlTag              rsvRate;
    private XmlTag              visitRateFree;
    private XmlTag              amountRateFree;
    private XmlTag              rsvRateFree;

    @Override
    protected void initXmlNodeInfo()
    {
        data = createRootChild( TAG_DATA );
        XmlTag.setParent( data, id );
        XmlTag.setParent( data, startDate );
        XmlTag.setParent( data, endDate );
        XmlTag.setParent( data, visitRate );
        XmlTag.setParent( data, amountRate );
        XmlTag.setParent( data, rsvRate );
        XmlTag.setParent( data, visitRateFree );
        XmlTag.setParent( data, amountRateFree );
        XmlTag.setParent( data, rsvRateFree );

        return;
    }

    public void setId(int id)
    {
        this.id = XmlTag.createXmlTag( TAG_ID, id );
    }

    public void setStartDate(int startdate)
    {
        this.startDate = XmlTag.createXmlTag( TAG_START_DATE, startdate );
    }

    public void setEndDate(int enddate)
    {
        this.endDate = XmlTag.createXmlTag( TAG_END_DATE, enddate );
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
