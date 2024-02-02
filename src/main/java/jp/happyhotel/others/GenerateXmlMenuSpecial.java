package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * 特集メニュー情報XML作成クラス
 */
public class GenerateXmlMenuSpecial extends WebApiResultBase
{
    // タグ名
    private static final String           TAG_SPECIAL = "specialMenu";
    private static final String           TAG_COUNT   = "count";

    private XmlTag                        specialMenu;                                      // 特集メニュータブ
    private XmlTag                        count;                                            // 都道府県
    private ArrayList<GenerateXmlMenuSub> subMenu     = new ArrayList<GenerateXmlMenuSub>(); // 駅路線詳細マスタ

    @Override
    protected void initXmlNodeInfo()
    {
        specialMenu = createRootChild( TAG_SPECIAL );
        XmlTag.setParent( specialMenu, count );
        if ( subMenu != null )
        {
            for( int i = 0 ; i < subMenu.size() ; i++ )
            {
                subMenu.get( i ).setRootNode( specialMenu );
                subMenu.get( i ).initXmlNodeInfo();
            }
        }
        return;
    }

    public void setCount(int cnt)
    {
        count = XmlTag.createXmlTag( TAG_COUNT, cnt );
        return;
    }

    public void setMenuSub(GenerateXmlMenuSub sub)
    {
        subMenu.add( sub );
        return;
    }

}
