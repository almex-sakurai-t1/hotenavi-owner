package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * topメニューXML作成クラス
 */
public class GenerateXmlMenuTop extends WebApiResultBase
{
    // タグ名
    private static final String           TAG_TOP        = "topMenu";
    private static final String           TAG_COUNT      = "count";
    private static final String           TAG_TOUCH_DISP = "touchDisp";

    private XmlTag                        topMenu;                                             // トップメニュータブ
    private XmlTag                        count;                                               // 都道府県
    private XmlTag                        touchDisp;                                           // タッチメニュー表示フラグ
    private ArrayList<GenerateXmlMenuSub> subMenu        = new ArrayList<GenerateXmlMenuSub>(); // 駅路線詳細マスタ

    @Override
    protected void initXmlNodeInfo()
    {
        topMenu = createRootChild( TAG_TOP );
        XmlTag.setParent( topMenu, count );
        XmlTag.setParent( topMenu, touchDisp );
        if ( subMenu != null )
        {
            for( int i = 0 ; i < subMenu.size() ; i++ )
            {
                subMenu.get( i ).setRootNode( topMenu );
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

    public void setTouchDisp(int touchDispValue)
    {
        touchDisp = XmlTag.createXmlTag( TAG_TOUCH_DISP, touchDispValue );
        return;
    }

    public void setMenuSub(GenerateXmlMenuSub sub)
    {
        subMenu.add( sub );
        return;
    }

}
