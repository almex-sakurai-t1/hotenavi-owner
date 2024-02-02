package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLチェックインエラーデータ金額反映
 */
public class GenerateXmlReflectCi extends WebApiResultBase
{
    // タグ名
    private static final String TAG_REFLECT_CI = "ReflectCi";
    private static final String TAG_RESULT     = "Result";

    private XmlTag              result;                      // 結果用タグ

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_REFLECT_CI );
        XmlTag.setParent( root, result );
        return;
    }

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

}
