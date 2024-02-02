package jp.happyhotel.others;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * クチコミ詳細情報xml生成クラス
 * 
 * @author N.Ide
 * @version 1.0 2011/04/27
 */

// クチコミ詳細情報
public class GenerateXmlKuchikomiKuchikomiDetail extends WebApiResultBase
{
    // タグ名
    private static final String TAG_DETAIL                = "detail";
    private static final String TAG_DETAIL_POSTDATE       = "postDate";
    private static final String TAG_DETAIL_BROWSER        = "browser";
    private static final String TAG_DETAIL_NAME           = "name";
    private static final String TAG_DETAIL_SEX            = "sex";
    private static final String TAG_DETAIL_CLEANNESS      = "cleanness";
    private static final String TAG_DETAIL_WIDTH          = "width";
    private static final String TAG_DETAIL_EQUIP          = "equip";
    private static final String TAG_DETAIL_SERVICE        = "service";
    private static final String TAG_DETAIL_COST           = "cost";
    private static final String TAG_DETAIL_POINT          = "point";
    private static final String TAG_DETAIL_MESSAGE        = "message";
    private static final String TAG_DETAIL_VOTECOUNT      = "voteCount";
    private static final String TAG_DETAIL_VOTEYES        = "voteYes";
    private static final String TAG_DETAIL_REPLY          = "reply";
    private static final String TAG_DETAIL_REPLY_NAME     = "name";
    private static final String TAG_DETAIL_REPLY_POSTDATE = "postDate";
    private static final String TAG_DETAIL_REPLY_MESSAGE  = "message";

    // 投稿区分値
    public static final int     DETAIL_BROWSER_PC         = 1;          // PC
    public static final int     DETAIL_BROWSER_MOBILE     = 2;          // 携帯
    public static final int     DETAIL_BROWSER_SMARTPHONE = 3;          // スマートフォン

    // 性別値
    public static final int     DETAIL_SEX_MAN            = 0;          // 男性
    public static final int     DETAIL_SEX_WOMAN          = 1;          // 女性
    public static final int     DETAIL_SEX_NON            = 2;          // 未登録

    private XmlTag              detail;                                 // クチコミ詳細情報格納タグ
    private XmlTag              detailPostDate;                         // 投稿日付
    private XmlTag              detailBrowser;                          // 投稿区分
    private XmlTag              detailName;                             // 投稿者名
    private XmlTag              detailSex;                              // 性別
    private XmlTag              detailCleanness;                        // きれいさ
    private XmlTag              detailWidth;                            // 広さ
    private XmlTag              detailEquip;                            // 設備
    private XmlTag              detailService;                          // サービス
    private XmlTag              detailCost;                             // 価格満足度
    private XmlTag              detailPoint;                            // 総合評価
    private XmlTag              detailMessage;                          // クチコミ内容
    private XmlTag              detailVoteCount;                        // 総投票数
    private XmlTag              detailVoteYes;                          // 参考数
    private XmlTag              detailReply;                            // ホテルからの返信情報開始タグ
    private XmlTag              replyName;                              // 返信者名
    private XmlTag              replyPostDate;                          // 投稿日付
    private XmlTag              replyMessage;                           // 返信内容

    @Override
    protected void initXmlNodeInfo()
    {
        detail = createRootChild( TAG_DETAIL );
        XmlTag.setParent( detail, detailPostDate );
        XmlTag.setParent( detail, detailBrowser );
        XmlTag.setParent( detail, detailName );
        XmlTag.setParent( detail, detailSex );
        XmlTag.setParent( detail, detailCleanness );
        XmlTag.setParent( detail, detailWidth );
        XmlTag.setParent( detail, detailEquip );
        XmlTag.setParent( detail, detailService );
        XmlTag.setParent( detail, detailCost );
        XmlTag.setParent( detail, detailPoint );
        XmlTag.setParent( detail, detailMessage );
        XmlTag.setParent( detail, detailVoteCount );
        XmlTag.setParent( detail, detailVoteYes );
        // 空満情報がある場合のみ追加する
        if ( detailReply != null )
        {
            XmlTag.setParent( detail, detailReply );
            XmlTag.setParent( detailReply, replyName );
            XmlTag.setParent( detailReply, replyPostDate );
            XmlTag.setParent( detailReply, replyMessage );
        }

        return;
    }

    public void setPostDate(String postDate)
    {
        detailPostDate = XmlTag.createXmlTag( TAG_DETAIL_POSTDATE, postDate );
        return;
    }

    public void setBrowser(String browser)
    {
        detailBrowser = XmlTag.createXmlTag( TAG_DETAIL_BROWSER, browser );
        return;
    }

    public void setName(String name)
    {
        if ( name != null )
        {
            name = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( name ) );
        }
        detailName = XmlTag.createXmlTag( TAG_DETAIL_NAME, name );
        return;
    }

    public void setSex(int sex)
    {
        detailSex = XmlTag.createXmlTag( TAG_DETAIL_SEX, sex );
        return;
    }

    public void setCleanness(int cleanness)
    {
        detailCleanness = XmlTag.createXmlTag( TAG_DETAIL_CLEANNESS, cleanness );
        return;
    }

    public void setWidth(int width)
    {
        detailWidth = XmlTag.createXmlTag( TAG_DETAIL_WIDTH, width );
        return;
    }

    public void setEquip(int equip)
    {
        detailEquip = XmlTag.createXmlTag( TAG_DETAIL_EQUIP, equip );
        return;
    }

    public void setService(int service)
    {
        detailService = XmlTag.createXmlTag( TAG_DETAIL_SERVICE, service );
        return;
    }

    public void setCost(int cost)
    {
        detailCost = XmlTag.createXmlTag( TAG_DETAIL_COST, cost );
        return;
    }

    public void setPoint(int point)
    {
        detailPoint = XmlTag.createXmlTag( TAG_DETAIL_POINT, point );
        return;
    }

    public void setMessage(String message)
    {
        if ( message != null )
        {
            message = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( message ) );
        }
        detailMessage = XmlTag.createXmlTag( TAG_DETAIL_MESSAGE, message );
        return;
    }

    public void setVoteCount(int voteCount)
    {
        detailVoteCount = XmlTag.createXmlTag( TAG_DETAIL_VOTECOUNT, voteCount );
        return;
    }

    public void setVoteYes(int voteYes)
    {
        detailVoteYes = XmlTag.createXmlTag( TAG_DETAIL_VOTEYES, voteYes );
        return;
    }

    public void setReply(String reply)
    {
        if ( reply != null )
        {
            reply = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( reply ) );
        }
        detailReply = XmlTag.createXmlTag( TAG_DETAIL_REPLY, reply );
        return;
    }

    public void setReplyName(String name)
    {
        if ( name != null )
        {
            name = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( name ) );
        }
        replyName = XmlTag.createXmlTag( TAG_DETAIL_REPLY_NAME, name );
        return;
    }

    public void setReplyPostDate(String postDate)
    {
        replyPostDate = XmlTag.createXmlTag( TAG_DETAIL_REPLY_POSTDATE, postDate );
        return;
    }

    public void setReplyMessage(String message)
    {
        if ( message != null )
        {
            message = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( message ) );
        }
        replyMessage = XmlTag.createXmlTag( TAG_DETAIL_REPLY_MESSAGE, message );
        return;
    }

}
