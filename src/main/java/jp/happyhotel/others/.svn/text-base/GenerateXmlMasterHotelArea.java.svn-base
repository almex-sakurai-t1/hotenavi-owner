package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;
import jp.happyhotel.data.DataMasterArea;

/***
 * ホテルエリアマスタ情報XML作成クラス
 */
public class GenerateXmlMasterHotelArea extends WebApiResultBase
{
    // タグ名
    private static final String                         TAG_HOTELAREA        = "hotelArea";
    private static final String                         TAG_HOTELAREA_PREFID = "prefId";
    private static final String                         TAG_HOTELAREA_NAME   = "detail";

    private XmlTag                                      hotelArea;                                                               // ホテルエリアタグ
    private XmlTag                                      prefId;                                                                  // 都道府県ID
    private ArrayList<GenerateXmlMasterHotelAreaDetail> detail               = new ArrayList<GenerateXmlMasterHotelAreaDetail>(); // ホテルエリアマスタ

    @Override
    protected void initXmlNodeInfo()
    {
        hotelArea = createRootChild( TAG_HOTELAREA );
        XmlTag.setParent( hotelArea, prefId );
        if ( detail != null )
        {
            for( int i = 0 ; i < detail.size() ; i++ )
            {
                detail.get( i ).setRootNode( hotelArea );
                detail.get( i ).initXmlNodeInfo();
            }
        }
    }

    public void setPrefId(int id)
    {
        prefId = XmlTag.createXmlTag( TAG_HOTELAREA_PREFID, id );
        return;
    }

    public void addDetail(GenerateXmlMasterHotelAreaDetail add)
    {
        detail.add( add );
        return;
    }

    public void addHotelAreaInfo(ArrayList<DataMasterArea> dma)
    {
        if ( dma.size() > 0 )
        {
            this.setPrefId( dma.get( 0 ).getPrefId() );
            // 詳細データ追加
            for( int i = 0 ; i < dma.size() ; i++ )
            {
                GenerateXmlMasterHotelAreaDetail addAreaDetail = new GenerateXmlMasterHotelAreaDetail();
                addAreaDetail.setId( dma.get( i ).getAreaId() );
                addAreaDetail.setName( dma.get( i ).getName() );
                this.addDetail( addAreaDetail );
            }
        }

    }
}
