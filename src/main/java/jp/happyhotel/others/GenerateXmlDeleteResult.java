package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

// 削除結果レスポンス
public class GenerateXmlDeleteResult extends WebApiResultBase
{
    // タグ名
    private static final String                     TAG_ERROR_CODE       = "errorCode";
    private static final String                     TAG_ERROR_MESSAGE    = "errorMessage";

    private XmlTag                                  errorCode;
    private XmlTag                                  errorMessage;

	@Override
	protected void initXmlNodeInfo()
	{
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, errorMessage );

	}

	public void setErrorCode(int code)
	{
		errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, code );
		return;
	}

	public void setErrorMessage(String message)
	{
		errorMessage = XmlTag.createXmlTag( TAG_ERROR_MESSAGE, message );
		return;
	}
}
