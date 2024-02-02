package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;
import jp.happyhotel.data.DataMasterPref;

/***
 * ìsìπï{åßÉ}ÉXÉ^èÓïÒXMLçÏê¨ÉNÉâÉX
 */
public class GenerateXmlMasterPref extends WebApiResultBase
{
    // É^ÉOñº
    private static final String TAG_PREF          = "pref";
    private static final String TAG_PREF_LOCAL_ID = "localId";
    private static final String TAG_PREF_ID       = "id";
    private static final String TAG_PREF_NAME     = "name";

    private XmlTag              pref;                         // ìsìπï{åßÉ^ÉO
    private XmlTag              localId;                      // ínï˚ID
    private XmlTag              prefid;                       // ìsìπï{åßID
    private XmlTag              prefName;                     // ìsìπï{åßñº

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
