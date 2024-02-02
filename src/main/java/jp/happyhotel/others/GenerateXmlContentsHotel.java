package jp.happyhotel.others;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * 検索以外のコンテンツの共通ホテル情報xml生成クラス
 * 
 * @author N.Ide
 * @version 1.0 2011/04/25
 */

// コンテンツ共通ホテル情報
public class GenerateXmlContentsHotel extends WebApiResultBase
{
    // タグ名
    private static final String TAG_HOTEL         = "hotel";
    private static final String TAG_HOTEL_NEW     = "new";
    private static final String TAG_HOTEL_DATE    = "date";
    private static final String TAG_HOTEL_NAME    = "name";
    private static final String TAG_HOTEL_ADDRESS = "address";
    private static final String TAG_HOTEL_ID      = "id";
    private static final String TAG_HOTEL_HOTELID = "hotelId"; // PvRanking用
    private static final String TAG_HOTEL_MESSAGE = "message";
    private static final String TAG_HOTEL_RANKING = "ranking";

    // フラグ値
    public static final int     HOTEL_NEW         = 1;        // 新規掲載(新着ホテル)、新築(新規オープンホテル)
    public static final int     HOTEL_UPDATE      = 2;        // 更新(新着ホテル)、リニューアル(新規オープンホテル)

    private XmlTag              hotel;                        // ホテル情報格納タグ
    private XmlTag              hotelNew;                     // 新規掲載フラグ(新着ホテル)、新築フラグ(新規オープンホテル)
    private XmlTag              hotelDate;                    // 日付
    private XmlTag              hotelName;                    // ホテル名
    private XmlTag              hotelAddress;                 // 住所
    private XmlTag              hotelId;                      // ホテルID
    private XmlTag              hotelMessage;                 // 表示文言
    private XmlTag              hotelRanking;                 // PVランキング順位

    @Override
    protected void initXmlNodeInfo()
    {
        hotel = createRootChild( TAG_HOTEL );

        XmlTag.setParent( hotel, hotelNew );
        XmlTag.setParent( hotel, hotelDate );
        XmlTag.setParent( hotel, hotelName );
        XmlTag.setParent( hotel, hotelAddress );
        XmlTag.setParent( hotel, hotelId );
        XmlTag.setParent( hotel, hotelMessage );
        XmlTag.setParent( hotel, hotelRanking );

        return;
    }

    public void setNew(int newFlag)
    {
        hotelNew = XmlTag.createXmlTag( TAG_HOTEL_NEW, newFlag );
        return;
    }

    public void setDate(int date)
    {
        hotelDate = XmlTag.createXmlTag( TAG_HOTEL_DATE, date );
        return;
    }

    public void setDate(String date)
    {
        hotelDate = XmlTag.createXmlTag( TAG_HOTEL_DATE, date );
        return;
    }

    public void setName(String name)
    {
        if ( name != null )
        {
            name = ReplaceString.replaceApiSpecial( name );
        }
        hotelName = XmlTag.createXmlTag( TAG_HOTEL_NAME, name );
        return;
    }

    public void setAddress(String address)
    {
        hotelAddress = XmlTag.createXmlTag( TAG_HOTEL_ADDRESS, address );
        return;
    }

    public void setId(int id)
    {
        hotelId = XmlTag.createXmlTag( TAG_HOTEL_ID, id );
        return;
    }

    public void setHotelId(int id)
    {
        hotelId = XmlTag.createXmlTag( TAG_HOTEL_HOTELID, id );
        return;
    }

    public void setMessage(String message)
    {
        if ( message != null )
        {
            message = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( message ) );
        }
        hotelMessage = XmlTag.createXmlTag( TAG_HOTEL_MESSAGE, message );
        return;
    }

    public void setRanking(String ranking)
    {
        hotelRanking = XmlTag.createXmlTag( TAG_HOTEL_RANKING, ranking );
        return;
    }

}
