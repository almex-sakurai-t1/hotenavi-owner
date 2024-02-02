package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;
import jp.happyhotel.data.DataMasterCity;

/***
 * Žs‹æ’¬‘ºƒ}ƒXƒ^î•ñ
 */
public class GenerateXmlMasterCity extends WebApiResultBase
{
    // ƒ^ƒO–¼
    private static final String TAG_CITY         = "city";
    private static final String TAG_CITY_PREF_ID = "prefId";
    private static final String TAG_CITY_ID      = "id";
    private static final String TAG_CITY_NAME    = "name";
    private static final String TAG_CITY_COUNT   = "count";

    private XmlTag              city;                       // Žs‹æ’¬‘ºƒ^ƒO
    private XmlTag              prefId;                     // “s“¹•{Œ§ID
    private XmlTag              cityId;                     // Žs‹æ’¬‘ºID
    private XmlTag              cityName;                   // Žs‹æ’¬‘º–¼
    private XmlTag              hotelCount;                 // ƒzƒeƒ‹Œ”

    @Override
    protected void initXmlNodeInfo()
    {
        city = createRootChild( TAG_CITY );
        XmlTag.setParent( city, prefId );
        XmlTag.setParent( city, cityId );
        XmlTag.setParent( city, cityName );
        XmlTag.setParent( city, hotelCount );
        return;
    }

    public void setPrefId(int id)
    {
        prefId = XmlTag.createXmlTag( TAG_CITY_PREF_ID, id );
        return;
    }

    public void setId(int id)
    {
        cityId = XmlTag.createXmlTag( TAG_CITY_ID, id );
        return;
    }

    public void setName(String name)
    {
        cityName = XmlTag.createXmlTag( TAG_CITY_NAME, name );
        return;
    }

    public void setCount(int count)
    {
        hotelCount = XmlTag.createXmlTag( TAG_CITY_COUNT, count );
    }

    public void addCity(DataMasterCity dmc, int hotelCount)
    {
        setId( dmc.getJisCode() );
        setName( dmc.getName() );
        setPrefId( dmc.getPrefId() );
        if ( hotelCount > 0 )
        {
            setCount( hotelCount );
        }
    }
}
