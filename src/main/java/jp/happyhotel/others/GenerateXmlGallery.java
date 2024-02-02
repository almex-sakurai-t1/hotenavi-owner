/*
 * @(#)OutputSwf.java 1.00 2009/06/15
 * Copyright (C) ALMEX Inc. 2009
 * flfast用xmlファイル生成クラス
 */

package jp.happyhotel.others;

import java.io.File;
import java.io.Serializable;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.hotel.HotelGallery;
import jp.happyhotel.hotel.HotelRoomMore;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 部屋画像一覧flash表示(flfast)用xmlファイル生成クラス
 * flfast用のxmlファイル生成し、そのFilePathを返す
 * 
 * 
 * @author N.Ide
 * @version 1.00 2009/06/15
 */

public class GenerateXmlGallery implements Serializable
{

    private static final long serialVersionUID = 3295285417257609581L;
    static int                pageRecords      = 6;                   // 1ページの最大表示数(テンプレートの上限)

    /**
     * xmlファイルを生成する
     * 
     * @param arrHotelIdList 検索結果ホテルIDリスト
     * @param param 検索に要するパラメータとその値
     * @param pageNum ページ番号
     * @param searchCondition 検索条件文言
     * @param termNo 端末番号
     * @param userAgentType 携帯キャリア
     * @param category ギャラリーのカテゴリ
     * @return 生成されたxmlのFilePath
     */
    static public String GenerateXml(int hotelId, String param, int pageNum, int currentNum, String termNo, int userAgentType, String flashActPath, String paramBefore, int category)
    {
        DataHotelBasic dhb;
        HotelRoomMore hrm;
        HotelGallery hg;

        int i;
        int allRoomNum;
        int firstRoomNum = 0;
        int lastPage = 0;
        boolean existReferName = false;
        boolean existImage = false;
        boolean existBigImage = false;
        String xmlFilePath = "";
        final String templetePath = "/happyhotel/flash/flfast/package2/2/hotenavi_temp2.swf";
        String carrier = "";
        String mvishaReturnUrl = "";
        String roomNameAndRank = "";
        String roomText = "";
        final String baseDir = "/usr/local/tomcat/temp";
        final String baseUrl = Url.getUrl();
        // final String baseUrl = "http://happyhotel.jp/_debug_/";
        // final String baseUrl = "http://121.101.88.177/"; // 社内環境用
        final String mvishaUrl = Url.getUrl() + "/flash-cgi/if/kf.cgi";
        // final String mvishaUrl = "http://121.101.88.177/flash/if/kf.cgi";
        NumberFormat nf;

        xmlFilePath = baseDir + "/" + termNo + ".gallery.xml";
        // Logging.info( "[GenerateXmlGallery1.GenerateXml] param:" + param );

        firstRoomNum = pageNum * pageRecords;
        dhb = new DataHotelBasic();
        hg = new HotelGallery();
        nf = new DecimalFormat( "0000" );

        hg.getGalleryData( hotelId, category );
        dhb.getData( hotelId );

        allRoomNum = hg.getHotelGalleryCount();
        lastPage = (hg.getHotelGalleryCount() - 1) / pageRecords;
        if ( pageNum > lastPage )
        {
            pageNum = lastPage;
        }

        if ( userAgentType == UserAgent.USERAGENT_AU )
        {
            carrier = "au/";
        }
        else if ( userAgentType == UserAgent.USERAGENT_DOCOMO )
        {
            carrier = "i/";
        }
        else if ( userAgentType == UserAgent.USERAGENT_SOFTBANK )
        {
            carrier = "y/";
        }

        try
        {
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docbuilder = dbfactory.newDocumentBuilder(); // DocumentBuilderインスタンス
            Document document = docbuilder.newDocument(); // Documentの生成

            // ルートノード作成
            Element root = document.createElement( "SLIDE" );
            root.setAttribute( "template", templetePath );
            // ルートノードをDocumentに追加
            document.appendChild( root );

            // ホテル名
            Element text1 = document.createElement( "TEXT" );
            text1.setAttribute( "name", "TEXT1_2" );
            text1.setAttribute( "type", "urlencoded" );
            text1.setAttribute( "value", URLEncoder.encode( ReplaceString.replaceMobile( dhb.getName() ), "SHIFT-JIS" ) );
            root.appendChild( text1 );
            // ホテル詳細アドレス
            Element linkBack = document.createElement( "PARAM" );
            linkBack.setAttribute( "name", "LINKBACK" );
            linkBack.setAttribute( "type", "urlencoded" );
            linkBack.setAttribute( "value",
                    URLEncoder.encode( baseUrl + carrier + "search/hotel_details.jsp?" + param, "SHIFT-JIS" ) );
            root.appendChild( linkBack );
            // TOPアドレス
            Element linkTop = document.createElement( "PARAM" );
            linkTop.setAttribute( "name", "LINKTOP" );
            linkTop.setAttribute( "type", "urlencoded" );
            linkTop.setAttribute( "value",
                    URLEncoder.encode( baseUrl + carrier + "index.jsp?" + param, "SHIFT-JIS" ) );
            root.appendChild( linkTop );
            // 前ページXMLリンクアドレス
            Element before = document.createElement( "PARAM" );
            before.setAttribute( "name", "BEFORE" );
            if ( pageNum > 0 )
            {
                before.setAttribute( "type", "urlencoded" );
                before.setAttribute( "value",
                        URLEncoder.encode( baseUrl + carrier + flashActPath + "?" + param + "&page=" + (pageNum - 1) + "&bef=true", "SHIFT-JIS" ) );
            }
            else
            {
                before.setAttribute( "value", "" );
            }
            root.appendChild( before );
            // 後ページXMLリンクアドレス
            Element next = document.createElement( "PARAM" );
            next.setAttribute( "name", "NEXT" );
            next.setAttribute( "type", "urlencoded" );
            if ( pageNum < lastPage )
            {
                next.setAttribute( "value",
                        URLEncoder.encode( baseUrl + carrier + flashActPath + "?" + param + "&page=" + (pageNum + 1), "SHIFT-JIS" ) );
            }
            else
            {
                next.setAttribute( "value", "" );
            }
            root.appendChild( next );
            // 1ページの件数
            Element itemCount = document.createElement( "PARAM" );
            itemCount.setAttribute( "name", "ITEM_COUNT" );
            if ( pageRecords > allRoomNum - pageRecords * pageNum )
            {
                itemCount.setAttribute( "value", String.valueOf( allRoomNum - pageRecords * pageNum ) );
            }
            else
            {
                itemCount.setAttribute( "value", String.valueOf( pageRecords ) );
            }
            root.appendChild( itemCount );
            // 初期表示位置
            Element itemCurrent = document.createElement( "PARAM" );
            itemCurrent.setAttribute( "name", "ITEM_CURRENT" );
            if ( paramBefore.compareTo( "true" ) != 0 )
            {
                if ( currentNum != 0 )
                {
                    itemCurrent.setAttribute( "value", Integer.toString( currentNum ) );
                }
                else
                {
                    itemCurrent.setAttribute( "value", "0" );
                }
            }
            // 次ﾍﾟｰｼﾞから戻った場合は最後の項目に合わせる
            else
            {
                itemCurrent.setAttribute( "value", Integer.toString( pageRecords - 1 ) );
            }
            root.appendChild( itemCurrent );

            // コピーライト表示 //TODO 要変更
            Element copyText = document.createElement( "PARAM" );
            copyText.setAttribute( "name", "COPYTEXT" );
            // copyText.setAttribute( "value", "(C)USEN CORPRATION/(C)imedia inc." );
            copyText.setAttribute( "value", "(C)ALMEX" );
            root.appendChild( copyText );

            // 部屋ごとのノードを追加
            for( i = 0 ; i < pageRecords ; i++ )
            {
                if ( i >= allRoomNum - pageRecords * pageNum )
                {
                    break;
                }

                // 部屋画像
                Element image = document.createElement( "IMAGE" );
                image.setAttribute( "name", "IMAGE0" + (i + 1) );
                image.setAttribute( "type", "conversion" );
                // hh_hotel_room_more.refer_nameが入っていたらホテナビから
                existReferName = (hg.getHotelGallery()[firstRoomNum + i].getReferName() != null) && (hg.getHotelGallery()[firstRoomNum + i].getReferName().compareTo( "" ) != 0);
                if ( existReferName != false )
                {
                    image.setAttribute( "value", "/happyhotel/common/room/" + dhb.getHotenaviId() + "/gallery/g" + hg.getHotelGallery()[firstRoomNum + i].getReferName() + ".jpg" );
                }
                else
                {
                    File file = new File( "/happyhotel/common/room/" + hotelId + "/gallery/g" + nf.format( hg.getHotelGallery()[firstRoomNum + i].getSeq() ) + ".jpg" );
                    existImage = file.exists();
                    if ( existImage != false )
                    {
                        image.setAttribute( "value", "/happyhotel/common/room/" + hotelId + "/gallery/g" + nf.format( hg.getHotelGallery()[firstRoomNum + i].getSeq() ) + ".jpg" );
                    }
                    else
                    {
                        image.setAttribute( "value", "/happyhotel/common/images/noimage.jpg" );
                    }
                }
                root.appendChild( image );
                // 部屋名 - ランク名
                Element text2 = document.createElement( "TEXT" );
                text2.setAttribute( "name", "TEXT" + (i + 1) + "_1" );
                text2.setAttribute( "type", "urlencoded" );
                if ( hg.getHotelGallery()[firstRoomNum + i].getRoomName() != null )
                {
                    roomNameAndRank = ReplaceString.replaceMobile( hg.getHotelGallery()[firstRoomNum + i].getRoomName() );
                }
                if ( hg.getHotelGallery()[firstRoomNum + i].getRoomName().matches( "[0-9]+" ) )
                {
                    roomNameAndRank += "号室";
                }

                if ( hg.getHotelCategory()[firstRoomNum + i].getCategoryName() != null && hg.getHotelCategory()[firstRoomNum + i].getDispIndex() != 99 )
                {
                    roomNameAndRank += " - " + ReplaceString.replaceMobile( hg.getHotelCategory()[firstRoomNum + i].getCategoryName() );
                }
                roomNameAndRank = roomNameAndRank.replace( "<br>", " " );
                roomNameAndRank = roomNameAndRank.replace( "<BR>", " " );
                text2.setAttribute( "value", URLEncoder.encode( ReplaceString.replaceMobile( roomNameAndRank ), "SHIFT-JIS" ) );

                root.appendChild( text2 );
                // PR文（PR文がある場合）、部屋名称（PR文がない場合）、ランク名称（PR文がない場合）
                Element text3 = document.createElement( "TEXT" );
                text3.setAttribute( "name", "TEXT" + (i + 1) + "_3" );
                Element text4 = document.createElement( "TEXT" );
                text4.setAttribute( "name", "TEXT" + (i + 1) + "_4" );
                Element text5 = document.createElement( "TEXT" );
                text5.setAttribute( "name", "TEXT" + (i + 1) + "_5" );
                if ( (hg.getHotelGallery()[firstRoomNum + i].getRoomText() != null) && (hg.getHotelGallery()[firstRoomNum + i].getRoomText().compareTo( "" ) != 0) )
                {
                    roomText = ReplaceString.replaceMobile( hg.getHotelGallery()[firstRoomNum + i].getRoomText() );
                    roomText = ReplaceString.replace( roomText, "<br>", "" );
                    roomText = ReplaceString.replace( roomText, "<BR>", "" );
                    text3.setAttribute( "type", "urlencoded" );
                    text3.setAttribute( "value", URLEncoder.encode( roomText, "SHIFT-JIS" ) );
                    text4.setAttribute( "value", "" );
                    text5.setAttribute( "value", "" );
                }
                else
                {
                    text3.setAttribute( "value", "" );
                    text4.setAttribute( "type", "urlencoded" );
                    text5.setAttribute( "type", "urlencoded" );
                    if ( hg.getHotelGallery()[firstRoomNum + i].getRoomName() != null )
                    {
                        if ( hg.getHotelGallery()[firstRoomNum + i].getRoomName().matches( "[0-9]+" ) )
                        {
                            text4.setAttribute( "value", URLEncoder.encode( ReplaceString.replaceMobile( hg.getHotelGallery()[firstRoomNum + i].getRoomName() + "号室" ), "SHIFT-JIS" ) );
                        }
                        else
                        {
                            text4.setAttribute( "value", ReplaceString.replaceMobile( hg.getHotelGallery()[firstRoomNum + i].getRoomName() ) );
                        }
                    }
                    if ( hg.getHotelCategory()[firstRoomNum + i].getCategoryName() != null && hg.getHotelCategory()[firstRoomNum + i].getDispIndex() != 99 )
                    {
                        text5.setAttribute( "value", ReplaceString.replaceMobile( hg.getHotelCategory()[firstRoomNum + i].getCategoryName().replace( "<br>", " " ) ) );
                    }
                }
                root.appendChild( text3 );
                root.appendChild( text4 );
                root.appendChild( text5 );

                // 中央左ボタンリンク先（テキスト版ページアドレス）
                Element linkBtnA = document.createElement( "PARAM" );
                linkBtnA.setAttribute( "name", "LINKBTNA" + (i + 1) );
                linkBtnA.setAttribute( "value", URLEncoder.encode( baseUrl + carrier + "search/hotel_gallerydetails.jsp?" + param + "&seq=" + hg.getHotelGallery()[firstRoomNum + i].getSeq() + "&more=true", "SHIFT-JIS" ) );
                linkBtnA.setAttribute( "type", "urlencoded" );
                root.appendChild( linkBtnA );
                // 中央右ボタンリンク先（mvishaリンク先）
                Element linkBtnB = document.createElement( "PARAM" );
                linkBtnB.setAttribute( "name", "LINKBTNB" + (i + 1) );
                linkBtnB.setAttribute( "type", "urlencoded" );
                // mvishaからの戻り先URLをEncode
                mvishaReturnUrl = URLEncoder.encode( baseUrl + carrier + "search/galleryFlashList.act?" + param + "&page=" + pageNum + "&num=" + i, "SHIFT-JIS" );
                if ( existReferName != false )
                {
                    // 大画像の有無を確認
                    File fileBig = new File( "/happyhotel/common/room/" + dhb.getHotenaviId() + "/gallery/g" + hg.getHotelGallery()[firstRoomNum + i].getReferName() + "m.jpg" );
                    existBigImage = fileBig.exists();
                    if ( existBigImage != false )
                    {
                        linkBtnB.setAttribute( "value",
                                URLEncoder.encode( mvishaUrl + "?file=/happyhotel/common/room/" + dhb.getHotenaviId() + "/gallery/g" + hg.getHotelGallery()[firstRoomNum + i].getReferName() + "m.jpg" +
                                        "&tp=" + mvishaReturnUrl, "SHIFT-JIS" ) );
                    }
                    else
                    {
                        linkBtnB.setAttribute( "value",
                                URLEncoder.encode( mvishaUrl + "?file=/happyhotel/common/room/" + dhb.getHotenaviId() + "/gallery/g" + hg.getHotelGallery()[firstRoomNum + i].getReferName() + ".jpg" +
                                        "&tp=" + mvishaReturnUrl, "SHIFT-JIS" ) );
                    }
                }
                else
                {
                    if ( existImage != false )
                    {
                        linkBtnB.setAttribute( "value",
                                URLEncoder.encode( mvishaUrl + "?file=/happyhotel/common/room/" + hotelId + "/gallery/g" + nf.format( hg.getHotelGallery()[firstRoomNum + i].getSeq() ) + ".jpg" +
                                        "&tp=" + mvishaReturnUrl, "SHIFT-JIS" ) );
                    }
                    else
                    {
                        linkBtnB.setAttribute( "value",
                                URLEncoder.encode( mvishaUrl + "?file=/happyhotel/common/images/noimage.jpg&tp=" + mvishaReturnUrl, "SHIFT-JIS" ) );
                    }
                }
                root.appendChild( linkBtnB );

            }
            // xmlファイルを一時保存
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();
            tf.setOutputProperty( "encoding", "SHIFT_JIS" );
            tf.transform( new DOMSource( document ), new StreamResult( new File( xmlFilePath ) ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[GenerateXmlGallery1.GenerateXml] Exception=" + e.toString() );
            return(null);
        }
        return(xmlFilePath);
    }
}
