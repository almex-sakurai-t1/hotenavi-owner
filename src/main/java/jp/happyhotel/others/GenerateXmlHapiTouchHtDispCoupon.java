package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチクーポン表示情報
 */
public class GenerateXmlHapiTouchHtDispCoupon extends WebApiResultBase
{
    // タグ名
    private static final String                             TAG_DISP_COUPON = "HtDispCoupon";
    private static final String                             TAG_RESULT      = "Result";
    private static final String                             TAG_ERROR_CODE  = "ErrorCode";
    private static final String                             TAG_COUPON_NO   = "CouponNo";
    private static final String                             TAG_COUPON_SEQ  = "CouponSeq";
    private static final String                             TAG_COUPON_Data = "CouponData";

    private XmlTag                                          result;                                                                 // 結果用タグ
    private XmlTag                                          errorCode;                                                              // エラーコード用タグ
    private XmlTag                                          couponNo;                                                               //
    private XmlTag                                          couponSeq;                                                              //
    private XmlTag                                          couponData;                                                             //
    private ArrayList<GenerateXmlHapiTouchHtDispCouponData> couponList      = new ArrayList<GenerateXmlHapiTouchHtDispCouponData>();

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_DISP_COUPON );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, couponNo );
        XmlTag.setParent( root, couponSeq );
        if ( couponList != null )
        {
            for( int i = 0 ; i < couponList.size() ; i++ )
            {
                this.couponList.get( i ).setRootNode( root );
                this.couponList.get( i ).initXmlNodeInfo();
            }

        }

        return;
    }

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

    public void setErrorCode(int errCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errCode );
    }

    public void setCouponNo(String couponno)
    {
        this.couponNo = XmlTag.createXmlTag( TAG_COUPON_NO, couponno );
    }

    public void setCouponSeq(int seq)
    {
        this.couponSeq = XmlTag.createXmlTag( TAG_COUPON_SEQ, seq );
    }

    public void addCouponData(GenerateXmlHapiTouchHtDispCouponData coupondata)
    {
        couponList.add( coupondata );
    }

}
