package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML PMS予約連携 予約プランデータ取得
 */
public class GenerateXmlRsvPlanData extends WebApiResultBase
{
    // タグ名
    private static final String                  TAG_RSV_PLAN_DATA = "RsvPlanData";
    private static final String                  TAG_ERROR_CODE    = "ErrorCode";
    private static final String                  TAG_PLAN_COUNT    = "PlanCount";

    private XmlTag                               planCount;
    private XmlTag                               errorCode;

    private ArrayList<GenerateXmlRsvPlanDataSub> dataList          = new ArrayList<GenerateXmlRsvPlanDataSub>();

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_RSV_PLAN_DATA );
        XmlTag.setParent( root, planCount );
        for( int i = 0 ; i < dataList.size() ; i++ )
        {
            this.dataList.get( i ).setRootNode( root );
            this.dataList.get( i ).initXmlNodeInfo();
        }
        XmlTag.setParent( root, errorCode );

        return;
    }

    public void setPlanCount(String planCount)
    {
        this.planCount = XmlTag.createXmlTag( TAG_PLAN_COUNT, planCount );
    }

    public void addData(GenerateXmlRsvPlanDataSub data)
    {
        this.dataList.add( data );
    }

    public void setErrorCode(int errCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errCode );
    }
}
