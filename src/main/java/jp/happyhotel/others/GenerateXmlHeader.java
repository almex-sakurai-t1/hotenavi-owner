package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

// 検索結果ヘッダ
public class GenerateXmlHeader extends WebApiResultBase
{
    // タグ名
    private static final String     TAG_HEADER         = "header";
    private static final String     TAG_HEADER_METHOD  = "method";
    private static final String     TAG_HEADER_NAME    = "name";
    private static final String     TAG_HEADER_ANDWORD = "andword";
    private static final String     TAG_HEADER_COUNT   = "count";

    private XmlTag                  header;                        // ヘッダ情報格納タグ
    private XmlTag                  headerMethod;                  // メソッド
    private XmlTag                  headerName;                    // 名称
    private XmlTag                  headerAndword;                 // 絞込み検索ワード
    private XmlTag                  headerCount;                   // 件数

    private GenerateXmlSearchResult searchResult;                  // 検索結果
    private GenerateXmlDeleteResult deleteResult;                  // 削除結果
    private GenerateXmlChainList    chain;
    private GenerateXmlDetail       detail;
    private GenerateXmlKuchikomi    kuchikomi;
    private GenerateXmlContents     contents;
    private GenerateXmlMenu         menu;
    private GenerateXmlPushInfo     pushInfo;
    private GenerateXmlRsvData      rsvData;
    private GenerateXmlTouchState   touchState;
    private GenerateXmlReviewUrl    reviewUrl;                     // AppleStore・GooglePlayレビューURL

    @Override
    protected void initXmlNodeInfo()
    {
        header = createRootChild( TAG_HEADER );

        XmlTag.setParent( header, headerMethod );
        XmlTag.setParent( header, headerName );
        XmlTag.setParent( header, headerAndword );
        XmlTag.setParent( header, headerCount );

        if ( searchResult != null )
        {
            searchResult.setRootNode( root );
            searchResult.initXmlNodeInfo();
        }
        if ( deleteResult != null )
        {
            deleteResult.setRootNode( root );
            deleteResult.initXmlNodeInfo();
        }
        if ( chain != null )
        {
            chain.setRootNode( root );
            chain.initXmlNodeInfo();
        }
        if ( detail != null )
        {
            detail.setRootNode( root );
            detail.initXmlNodeInfo();
        }
        if ( kuchikomi != null )
        {
            kuchikomi.setRootNode( root );
            kuchikomi.initXmlNodeInfo();
        }
        if ( contents != null )
        {
            contents.setRootNode( root );
            contents.initXmlNodeInfo();
        }
        if ( menu != null )
        {
            menu.setRootNode( root );
            menu.initXmlNodeInfo();
        }
        if ( pushInfo != null )
        {
            pushInfo.setRootNode( root );
            pushInfo.initXmlNodeInfo();
        }
        if ( rsvData != null )
        {
            rsvData.setRootNode( root );
            rsvData.initXmlNodeInfo();
        }
        if ( touchState != null )
        {
            touchState.setRootNode( root );
            touchState.initXmlNodeInfo();
        }
        if ( reviewUrl != null )
        {
            reviewUrl.setRootNode( root );
            reviewUrl.initXmlNodeInfo();
        }
        return;
    }

    public void setMethod(String method)
    {
        headerMethod = XmlTag.createXmlTag( TAG_HEADER_METHOD, method );
        return;
    }

    public void setName(String name)
    {
        headerName = XmlTag.createXmlTag( TAG_HEADER_NAME, name );
        return;
    }

    public void setAndword(String andword)
    {
        headerAndword = XmlTag.createXmlTag( TAG_HEADER_ANDWORD, andword );
        return;
    }

    public void setCount(int count)
    {
        headerCount = XmlTag.createXmlTag( TAG_HEADER_COUNT, count );
        return;
    }

    public void setSearchResult(GenerateXmlSearchResult searchResult)
    {
        this.searchResult = searchResult;
        return;
    }

    public void setDeleteResult(GenerateXmlDeleteResult deleteResult)
    {
        this.deleteResult = deleteResult;
        return;
    }

    public void setChain(GenerateXmlChainList chainList)
    {
        this.chain = chainList;
        return;
    }

    public void setDetail(GenerateXmlDetail detailList)
    {
        this.detail = detailList;
        return;
    }

    public void setKuchikomi(GenerateXmlKuchikomi kuchikomiList)
    {
        this.kuchikomi = kuchikomiList;
        return;
    }

    public void setContents(GenerateXmlContents contentsList)
    {
        this.contents = contentsList;
        return;
    }

    public void setMenu(GenerateXmlMenu menuList)
    {
        this.menu = menuList;
        return;
    }

    public void setPushInfo(GenerateXmlPushInfo pushInfo)
    {
        this.pushInfo = pushInfo;
    }

    public void setRsvData(GenerateXmlRsvData rsvdata)
    {
        this.rsvData = rsvdata;
    }

    public void setTouchState(GenerateXmlTouchState touchstate)
    {
        this.touchState = touchstate;
    }

    public void setReviewUrl(GenerateXmlReviewUrl reviewUrl)
    {
        this.reviewUrl = reviewUrl;
    }
}
