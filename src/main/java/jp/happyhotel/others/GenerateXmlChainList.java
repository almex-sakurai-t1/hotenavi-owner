package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

// �������ʃw�b�_
public class GenerateXmlChainList extends WebApiResultBase
{
    // �^�O��
    private static final String         TAG_ERROR_CODE    = "errorCode";
    private static final String         TAG_ERROR_MESSAGE = "errorMessage";
    private static final String         TAG_CHAIN_COUNT   = "chainCount";
    private static final String         TAG_CHAIN         = "chain";

    private XmlTag                      errorCode;                                            // �w�b�_���i�[�^�O
    private XmlTag                      errorMessage;                                         // ���\�b�h
    private XmlTag                      chainCount;                                           // ����

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
