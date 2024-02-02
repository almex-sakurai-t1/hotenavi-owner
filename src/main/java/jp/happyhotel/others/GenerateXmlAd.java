package jp.happyhotel.others;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;
import jp.happyhotel.data.DataMasterSponsor;

// 広告情報
public class GenerateXmlAd extends WebApiResultBase
{
    // タグ名
    private static final String TAG_AD         = "ad";
    private static final String TAG_AD_KIND    = "kind";
    private static final String TAG_AD_HOTELID = "hotelId";
    private static final String TAG_AD_CHAINID = "chainId";
    private static final String TAG_AD_URL     = "url";
    private static final String TAG_AD_TEXT    = "text";
    private static final String TAG_AD_IMAGE   = "image";
    private static final String TAG_AD_SPONSOR = "sponsor";

    // 広告種別値
    public static final int     AD_KIND_HOTEL  = 1;        // ホテル
    public static final int     AD_KIND_CHAIN  = 2;        // チェーン
    public static final int     AD_KIND_PLAN   = 3;        // 企画

    private XmlTag              ad;                        // 広告情報格納タグ
    private XmlTag              adKind;                    // 広告種別
    private XmlTag              adHotelId;                 // ホテルID
    private XmlTag              adChainId;                 // チェーンID
    private XmlTag              adUrl;                     // 企画広告URL
    private XmlTag              adText;                    // 広告リンク文言
    private XmlTag              adImage;                   // 広告画像URL
    private XmlTag              adSponsorCd;               // スポンサーコード

    @Override
    protected void initXmlNodeInfo()
    {
        ad = createRootChild( TAG_AD );

        XmlTag.setParent( ad, adKind );
        XmlTag.setParent( ad, adHotelId );
        XmlTag.setParent( ad, adChainId );
        XmlTag.setParent( ad, adUrl );
        XmlTag.setParent( ad, adText );
        XmlTag.setParent( ad, adImage );
        XmlTag.setParent( ad, adSponsorCd );

        return;
    }

    public void setKind(int kind)
    {
        adKind = XmlTag.createXmlTag( TAG_AD_KIND, kind );
        return;
    }

    public void setHotelId(int hotelId)
    {
        adHotelId = XmlTag.createXmlTag( TAG_AD_HOTELID, hotelId );
        return;
    }

    public void setChainId(int chainId)
    {
        adChainId = XmlTag.createXmlTag( TAG_AD_CHAINID, chainId );
        return;
    }

    public void setUrl(String url)
    {
        if ( url != null )
        {
            url = ReplaceString.replaceApiSpecial( url );
        }
        adUrl = XmlTag.createXmlTag( TAG_AD_URL, url );
        return;
    }

    public void setText(String text)
    {
        if ( text != null )
        {
            text = ReplaceString.replaceApiSpecial( text );
        }
        adText = XmlTag.createXmlTag( TAG_AD_TEXT, text );
        return;
    }

    public void setImage(String imageUrl)
    {
        if ( imageUrl != null && imageUrl.equals( "" ) == false && imageUrl.indexOf( "http://" ) == -1 && imageUrl.indexOf( "https://" ) == -1 )
        {
            imageUrl = Url.getUrl() + imageUrl;
        }
        adImage = XmlTag.createXmlTag( TAG_AD_IMAGE, imageUrl );
        return;
    }

    public void setSponsorCd(int sponsorCd)
    {
        adSponsorCd = XmlTag.createXmlTag( TAG_AD_SPONSOR, sponsorCd );
        return;
    }

    public void setAdInfo(DataMasterSponsor masterSponsor, HttpServletRequest request)
    {
        int hotelId = 0;
        int chainId = 0;
        int kind = 0;
        String url = "";
        String strText = "";

        // タイトル
        if ( masterSponsor.getTitle().equals( "" ) == false )
        {
            strText = masterSponsor.getTitle();
        }
        else
        {
            strText = masterSponsor.getTitleMobile();
        }

        if ( masterSponsor.getAreaCode() == 0 && masterSponsor.getHotelId() == 0 )
        {
            kind = 3;
            url = masterSponsor.getUrl();
        }
        else if ( masterSponsor.getHotelId() < 100000 )
        {
            kind = 2;
            chainId = masterSponsor.getHotelId();
            url = Url.getUrl() + "/searchChain.act?group_id=" + chainId;
        }
        else
        {
            kind = 1;
            hotelId = masterSponsor.getHotelId();
            url = Url.getUrl() + "/detail/detail_top.jsp?id=" + hotelId;
        }

        this.setKind( kind );
        this.setHotelId( hotelId );
        this.setChainId( chainId );

        String Urltmp = ReplaceString.replace( url, "&", "!" );
        url = Url.getUrl() + "/phone/send_sponsor.jsp?sponsor_code=" + masterSponsor.getSponsorCode() + "&ad_flag=false&coupon_flag=false&r=" + Urltmp;
        this.setUrl( url );
        this.setText( ReplaceString.replaceKanaFull( strText ) );
        if ( masterSponsor.getExUrl() != null && masterSponsor.getExUrl().equals( "" ) == false && masterSponsor.getExUrl().equals( " " ) == false )
        {
            this.setImage( masterSponsor.getExUrl() );
        }
        this.setSponsorCd( masterSponsor.getSponsorCode() );
    }

    public void setAdInfo2(DataMasterSponsor masterSponsor, HttpServletRequest request)
    {
        int hotelId = 0;
        int chainId = 0;
        int kind = 0;
        String url = "";
        String strText = "";
        String strImg = "";

        // タイトル
        if ( masterSponsor.getTitle().equals( "" ) == false )
        {
            strText = masterSponsor.getTitle();
        }
        else
        {
            strText = masterSponsor.getTitleMobile();
        }

        if ( masterSponsor.getAreaCode() == 0 && masterSponsor.getHotelId() == 0 )
        {
            kind = 3;
            url = masterSponsor.getUrl();
        }
        else if ( masterSponsor.getHotelId() < 100000 )
        {
            kind = 2;
            chainId = masterSponsor.getHotelId();
            url = Url.getUrl() + "/searchChain.act?group_id=" + chainId;
        }
        else
        {
            kind = 1;
            hotelId = masterSponsor.getHotelId();
            url = Url.getUrl() + "/detail/detail_top.jsp?id=" + hotelId;
        }

        this.setKind( kind );
        this.setHotelId( hotelId );
        this.setChainId( chainId );

        String Urltmp = ReplaceString.replace( url, "&", "!" );
        url = Url.getUrl() + "/phone/send_sponsor.jsp?sponsor_code=" + masterSponsor.getSponsorCode() + "&ad_flag=false&coupon_flag=false&r=" + Urltmp;
        this.setUrl( url );
        this.setText( ReplaceString.replaceApiBr2Space( ReplaceString.replaceKanaFull( strText ) ) );

        // if ( kind != 3 )
        // {
        strImg = Url.getUrl() + "/servlet/SponsorPicture?sponsor_code=" + masterSponsor.getSponsorCode() + "&type=jpg";
        this.setImage( ReplaceString.replaceApiSpecial( strImg ) );
        // }

        this.setSponsorCd( masterSponsor.getSponsorCode() );
    }
}
