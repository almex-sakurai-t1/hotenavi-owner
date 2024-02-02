package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

// 検索結果ヘッダ
public class GenerateXmlChainList extends WebApiResultBase
{
    // タグ名
    private static final String         TAG_ERROR_CODE    = "errorCode";
    private static final String         TAG_ERROR_MESSAGE = "errorMessage";
    private static final String         TAG_CHAIN_COUNT   = "chainCount";
    private static final String         TAG_CHAIN         = "chain";

    private XmlTag                      errorCode;                                            // ヘッダ情報格納タグ
    private XmlTag                      errorMessage;                                         // メソッド
    private XmlTag                      chainCount;                                           // 名称

    private ArrayList<GenerateXmlChain> chain             = new ArrayList<GenerateXmlChain>(); //

    @Override
    protected void initXmlNodeInfo()
    {

        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, errorMessage );
        XmlTag.setParent( root, chainCount );

        if ( chain != null )
        {
            for( int i = 0 ; i < chain.size() ; i++ )
            {
                chain.get( i ).setRootNode( root );
                chain.get( i ).initXmlNodeInfo();
            }
        }

        return;
    }

    public void setErrorCode(String errCode)
    {
        errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errCode );
        return;
    }

    public void setErrorMessage(String errMsg)
    {
        errorMessage = XmlTag.createXmlTag( TAG_ERROR_MESSAGE, errMsg );
        return;
    }

    public void setChainCount(int chainGroupCount)
    {
        chainCount = XmlTag.createXmlTag( TAG_CHAIN_COUNT, chainGroupCount );
        return;
    }

    public void setChain(GenerateXmlChain chainList)
    {
        chain.add( chainList );
        return;
    }

}
