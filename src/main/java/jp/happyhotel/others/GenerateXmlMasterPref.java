package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;
import jp.happyhotel.data.DataMasterPref;

/***
 * s¹{§}X^îñXMLì¬NX
 */
public class GenerateXmlMasterPref extends WebApiResultBase
{
    // ^O¼
    private static final String TAG_PREF          = "pref";
    private static final String TAG_PREF_LOCAL_ID = "localId";
    private static final String TAG_PREF_ID       = "id";
    private static final String TAG_PREF_NAME     = "name";

    private XmlTag              pref;                         // s¹{§^O
    private XmlTag              localId;                      // nûID
    private XmlTag              prefid;                       // s¹{§ID
    private XmlTag              prefName;                     // s¹{§¼

    @Override
    protected void initXmlNodeInfo()
    {
        pref = createRootChild( TAG_PREF );
        XmlTag.setParent( pref, localId );
        XmlTag.setParent( pref, prefid );
        XmlTag.setParent( pref, prefName );
        return;
    }

    public void setLocalId(int id)
    {
        localId = XmlTag.createXmlTag( TAG_PREF_LOCAL_ID, id );
        return;
    }

    public void setId(int id)
    {
        prefid = XmlTag.createXmlTag( TAG_PREF_ID, id );
        return;
    }

    public void setName(String name)
    {
        prefName = XmlTag.createXmlTag( TAG_PREF_NAME, name );
        return;
    }

    public void addPref(DataMasterPref dmp)
    {
        setId( dmp.getPrefId() );
        setName( dmp.getName() );
        setLocalId( dmp.getLocalId() );
    }
}
