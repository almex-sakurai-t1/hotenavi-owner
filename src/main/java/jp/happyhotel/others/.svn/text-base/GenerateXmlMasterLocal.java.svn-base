package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;
import jp.happyhotel.data.DataMasterLocal;

/***
 * 地方マスタ情報XML作成クラス
 */
public class GenerateXmlMasterLocal extends WebApiResultBase
{
    // タグ名
    private static final String TAG_LOCAL      = "local";
    private static final String TAG_LOCAL_ID   = "id";
    private static final String TAG_LOCAL_NAME = "name";

    private XmlTag              local;                   // 地方タグ
    private XmlTag              localId;                 // 地方ID
    private XmlTag              localName;               // 地方名

    @Override
    protected void initXmlNodeInfo()
    {
        local = createRootChild( TAG_LOCAL );
        XmlTag.setParent( local, localId );
        XmlTag.setParent( local, localName );
        return;
    }

    public void setId(int id)
    {
        localId = XmlTag.createXmlTag( TAG_LOCAL_ID, id );
        return;
    }

    public void setName(String name)
    {
        localName = XmlTag.createXmlTag( TAG_LOCAL_NAME, name );
        return;
    }

    public void addLocalInfo(DataMasterLocal dml)
    {
        setId( dml.getLocalId() );
        setName( dml.getName() );
    }
}
