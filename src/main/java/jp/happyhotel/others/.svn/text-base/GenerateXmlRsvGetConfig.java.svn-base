package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML PMSó\ñÒòAåg ó\ñÒê›íËéÊìæ
 */
public class GenerateXmlRsvGetConfig extends WebApiResultBase
{
    // É^ÉOñº
    private static final String TAG_GET_RSV_CONFIG = "GetRsvConfig";
    private static final String TAG_MANAGE_DATE    = "ManageDate";
    private static final String TAG_DEADLINE_TIME  = "DeadlineTime";
    private static final String TAG_ERROR_CODE     = "ErrorCode";

    private XmlTag              manageDate;
    private XmlTag              deadlineTime;
    private XmlTag              errorCode;

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_GET_RSV_CONFIG );
        XmlTag.setParent( root, manageDate );
        XmlTag.setParent( root, deadlineTime );
        XmlTag.setParent( root, errorCode );
        return;
    }

    public void setManageDate(String manageDate)
    {
        this.manageDate = XmlTag.createXmlTag( TAG_MANAGE_DATE, manageDate );
    }

    public void setDeadlineTime(String deadlineTime)
    {
        this.deadlineTime = XmlTag.createXmlTag( TAG_DEADLINE_TIME, deadlineTime );
    }

    public void setErrorCode(int errCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errCode );
    }
}
