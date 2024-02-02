package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML PMS予約連携プランデータルームランク別料金
 */
public class GenerateXmlRsvPlanDataRoomRanks extends WebApiResultBase
{
    // タグ名
    private static final String TAG_ROOM_RANKS  = "RoomRanks";
    private static final String TAG_RANK_ID     = "RankId";
    private static final String TAG_RANK_NAME   = "RankName";
    private static final String TAG_CHARGE_TEXT = "ChargeText";
    private static final String TAG_CHARGE      = "Charge";

    private XmlTag              roomRanks;
    private XmlTag              rankId;
    private XmlTag              rankName;
    private XmlTag              chargeText;
    private XmlTag              charge;

    @Override
    protected void initXmlNodeInfo()
    {
        roomRanks = createRootChild( TAG_ROOM_RANKS );
        XmlTag.setParent( roomRanks, rankId );
        XmlTag.setParent( roomRanks, rankName );
        XmlTag.setParent( roomRanks, chargeText );
        XmlTag.setParent( roomRanks, charge );

        return;
    }

    public void setRankId(String rankId)
    {
        this.rankId = XmlTag.createXmlTag( TAG_RANK_ID, rankId );
    }

    public void setRankName(String rankName)
    {
        this.rankName = XmlTag.createXmlTag( TAG_RANK_NAME, rankName );
    }

    public void setChargeText(String chargeText)
    {
        this.chargeText = XmlTag.createXmlTag( TAG_CHARGE_TEXT, chargeText );
    }

    public void setCharge(String charge)
    {
        this.charge = XmlTag.createXmlTag( TAG_CHARGE, charge );
    }

}
