package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ予約データデータ
 */
public class GenerateXmlHapiTouchRsvDataDetailImperativeOption extends WebApiResultBase
{
    // タグ名
    private static final String TAG_IMPERATIVE_OPTION          = "ImperativeOption";
    private static final String TAG_IMPERATIVE_OPTION_NAME     = "ImperativeOptionName";
    private static final String TAG_IMPERATIVE_OPTION_SUB_NAME = "ImperativeOptionSubName";

    private XmlTag              imperativeOption;
    private XmlTag              imperativeOptionName;
    private XmlTag              imperativeOptionSubName;

    @Override
    protected void initXmlNodeInfo()
    {
        imperativeOption = createRootChild( TAG_IMPERATIVE_OPTION );
        XmlTag.setParent( imperativeOption, imperativeOptionName );
        XmlTag.setParent( imperativeOption, imperativeOptionSubName );

        return;
    }

    public void setImperativeOptionName(String optionName)
    {
        this.imperativeOptionName = XmlTag.createXmlTag( TAG_IMPERATIVE_OPTION_NAME, optionName );
    }

    public void setImperativeOptionSubName(String optionSubName)
    {
        this.imperativeOptionSubName = XmlTag.createXmlTag( TAG_IMPERATIVE_OPTION_SUB_NAME, optionSubName );
    }

}
