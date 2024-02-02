/*
 * @(#)OutputSwf.java 1.00 2009/06/15
 * Copyright (C) ALMEX Inc. 2009
 * flfast用xmlファイル生成クラス
 */

package jp.happyhotel.others;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelMaster;
import jp.happyhotel.data.DataHotelStatus;
import jp.happyhotel.hotel.HotelBbs;
import jp.happyhotel.sponsor.SponsorData_M2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 検索結果flash表示(flfast)用xmlファイル生成クラス
 * flfast用のxmlファイル生成し、そのFilePathを返す
 * 
 * @author N.Ide
 * @version 1.00 2009/06/15
 */

public class GenerateXml1 implements Serializable
{

    /**
     *
     */

    static int                pageRecords      = Constants.pageLimitRecordMobile; // 1ページの最大表示数

    private static final long serialVersionUID = -4131852613466879649L;

    /**
     * xmlファイルを生成する
     * 
     * @param arrHotelIdList 検索結果ホテルIDリスト
     * @param param 検索に要するパラメータとその値
     * @param pageNum ページ番号
     * @param searchCondition 検索条件文言
     * @param termNo 端末番号
     * @param userAgentType 携帯キャリア
     * @param actPath 検索actのパス
     * @param actPathFlash 検索act(Flash)のパス
     * @param prefId 都道府県ID(広告表示用)
     * @return swfバイナリデータ
     */
    static public String GenerateXml(int[] arrHotelIdList, String param, int pageNum, String searchCondition, String termNo,
            int userAgentType, String actPath, String actPathFlash, int prefId, String uid)
    {
        final boolean FLG_MOBILE = true;
        final int DISP_COUNT = 5; // 広告表示数
        final int DISP_COUNT_ROTE = 1; // ローテーション広告表示数

        DataHotelBasic dhb;
        DataHotelStatus dhs;
        DataHotelMaster dhm;
        HotelBbs bbs;
        SponsorData_M2 sd;
        boolean ret;
        int i, j, k;
        int hotelId = 0;
        int allHotelNum;
        int firstHotelNum = 0;
        int lastPage = 0;
        int rank = 0;
        int kuchikomiCount = 0;
        int sponsorCount = 0;
        int sponsorCountRote = 0;
        int integerAverage = 0;
        int decimalAverage = 0;
        BigDecimal pointAverage;
        // String buildingType = "";
        String emptyStatus = "";
        String xmlFilePath = "";
        String carrier = "";
        String pr = "";
        final String baseDir = "/usr/local/tomcat/temp";
        final String templaetPath = "/happyhotel/flash/flfast/package2/1/FLK0001A.swf"; // 本番環境
        final String baseUrl = Url.getUrl() + "/";
        // final String baseUrl = "http://happyhotel.jp/_debug_/";
        // final String baseUrl = "http://121.101.88.177/"; // 社内環境用

        xmlFilePath = baseDir + "/" + termNo + ".xml";
        allHotelNum = arrHotelIdList.length;
        lastPage = (allHotelNum - 1) / pageRecords;
        if ( pageNum > lastPage )
        {
            pageNum = lastPage;
        }
        firstHotelNum = pageNum * pageRecords;
        dhb = new DataHotelBasic();
        dhs = new DataHotelStatus();
        dhm = new DataHotelMaster();
        bbs = new HotelBbs();
        sd = new SponsorData_M2();

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
            root.setAttribute( "template", templaetPath );
            // ルートノードをDocumentに追加
            document.appendChild( root );

            // 共通項目ノードを追加
            // タイトル（検索条件）
            Element title = document.createElement( "PARAM" );
            title.setAttribute( "name", "TITLE" );
            title.setAttribute( "value", searchCondition );
            root.appendChild( title );
            // 検索件数（全件数）
            Element numMax = document.createElement( "PARAM" );
            numMax.setAttribute( "name", "NUM_MAX" );
            numMax.setAttribute( "value", String.valueOf( allHotelNum ) );
            root.appendChild( numMax );
            // 現在表示数
            Element numMaxp = document.createElement( "PARAM" );
            numMaxp.setAttribute( "name", "NUM_MAXP" );
            if ( pageRecords > allHotelNum - pageRecords * pageNum )
            {
                numMaxp.setAttribute( "value", firstHotelNum + 1 + "-" + (firstHotelNum + (allHotelNum - pageRecords * pageNum)) );
            }
            else
            {
                numMaxp.setAttribute( "value", firstHotelNum + 1 + "-" + (firstHotelNum + pageRecords) );
            }
            root.appendChild( numMaxp );
            // 前ページアドレス
            Element before = document.createElement( "PARAM" );
            before.setAttribute( "name", "BEFORE" );
            if ( pageNum > 0 )
            {
                before.setAttribute( "type", "urlencoded" );
                before.setAttribute( "value",
                        URLEncoder.encode( baseUrl + carrier + actPathFlash + "?" + param + "&page=" + (pageNum - 1) + "&" + uid, "SHIFT_JIS" ) );

            }
            else
            {
                before.setAttribute( "value", "" );
            }
            root.appendChild( before );
            // 後ページアドレス
            Element next = document.createElement( "PARAM" );
            next.setAttribute( "name", "NEXT" );
            next.setAttribute( "type", "urlencoded" );
            if ( pageNum < lastPage )
            {
                next.setAttribute( "value",
                        URLEncoder.encode( baseUrl + carrier + actPathFlash + "?" + param + "&page=" + (pageNum + 1) + "&" + uid, "SHIFT_JIS" ) );
            }
            else
            {
                next.setAttribute( "value", "" );
            }
            root.appendChild( next );
            // 戻り先リンクアドレス
            Element modoru = document.createElement( "PARAM" );
            modoru.setAttribute( "name", "MODORU" );
            modoru.setAttribute( "type", "urlencoded" );
            modoru.setAttribute( "value",
                    URLEncoder.encode( baseUrl + carrier + actPath + "?" + uid, "SHIFT_JIS" ) ); // TODO 戻り先アドレス変更
            root.appendChild( modoru );
            // テキスト版リンクアドレス
            Element textmode = document.createElement( "PARAM" );
            textmode.setAttribute( "name", "TEXTMODE" );
            textmode.setAttribute( "type", "urlencoded" );
            textmode.setAttribute( "value",
                    URLEncoder.encode( baseUrl + carrier + actPath + "?" + param + "&page=" + (pageNum) + "&" + uid, "SHIFT_JIS" ) );
            root.appendChild( textmode );
            // 1ページの件数
            Element itemCount = document.createElement( "PARAM" );
            itemCount.setAttribute( "name", "ITEM_COUNT" );
            if ( pageRecords > allHotelNum - pageRecords * pageNum )
            {
                itemCount.setAttribute( "value", String.valueOf( allHotelNum - pageRecords * pageNum ) );
            }
            else
            {
                itemCount.setAttribute( "value", String.valueOf( pageRecords ) );
            }
            root.appendChild( itemCount );
            // 初期表示位置
            Element itemCurrent = document.createElement( "PARAM" );
            itemCurrent.setAttribute( "name", "ITEM_CURRENT" );
            itemCurrent.setAttribute( "value", "0" );
            root.appendChild( itemCurrent );
            // コピーライト表示 //TODO 要変更
            Element copyText = document.createElement( "PARAM" );
            copyText.setAttribute( "name", "COPYTEXT" );
            copyText.setAttribute( "value", "(C)ALMEX inc." );
            root.appendChild( copyText );

            // 広告
            if ( prefId > 0 )
            {
                // ノーマル広告
                ret = sd.getSponsorRandomByPrefAdOnly( prefId );
                if ( ret )
                {
                    sponsorCount = sd.getSponsorCount();
                    if ( sponsorCount > DISP_COUNT )
                    {
                        sponsorCount = DISP_COUNT;
                    }
                    if ( sponsorCount > 0 )
                    {
                        for( i = 0 ; i < sponsorCount ; i++ )
                        {
                            // 広告の表示
                            Element adname = document.createElement( "TEXT" );
                            adname.setAttribute( "name", "ADNAME" + (i + 1) );
                            adname.setAttribute( "type", "urlencoded" );
                            adname.setAttribute( "value", URLEncoder.encode( ReplaceString.replaceMobile( sd.getSponsor()[i].getTitleMobile() ), "SHIFT_JIS" ) );
                            root.appendChild( adname );
                            // 広告のリンク先
                            Element adurl = document.createElement( "PARAM" );
                            adurl.setAttribute( "name", "ADLINK" + (i + 1) );
                            adurl.setAttribute( "type", "urlencoded" );
                            adurl.setAttribute( "value", URLEncoder.encode( baseUrl + carrier + "send_sponsor.jsp?sponsor_code=" + sd.getSponsor()[i].getSponsorCode() + "&" + uid, "SHIFT_JIS" ) );
                            root.appendChild( adurl );
                            // ｲﾝﾌﾟﾚｯｼｮﾝｶｳﾝﾄの加算
                            sd.setImpressionCount( sd.getSponsor()[i].getSponsorCode(), FLG_MOBILE );
                        }
                    }
                }
                // ローテーション広告
                ret = sd.getRandomSponsorByPref( prefId, DISP_COUNT_ROTE, FLG_MOBILE );
                if ( ret )
                {
                    sponsorCountRote = sd.getSponsorCount();
                    if ( sponsorCountRote > 0 )
                    {
                        for( i = 0 ; i < sponsorCountRote ; i++ )
                        {
                            // 広告の表示
                            Element adname = document.createElement( "TEXT" );
                            adname.setAttribute( "name", "ADNAME" + (sponsorCount + i + 1) );
                            adname.setAttribute( "type", "urlencoded" );
                            adname.setAttribute( "value", URLEncoder.encode( ReplaceString.replaceMobile( sd.getSponsor()[i].getTitleMobile() ), "SHIFT_JIS" ) );
                            root.appendChild( adname );
                            // 広告のリンク先
                            Element adurl = document.createElement( "PARAM" );
                            adurl.setAttribute( "name", "ADLINK" + (sponsorCount + i + 1) );
                            adurl.setAttribute( "type", "urlencoded" );
                            adurl.setAttribute( "value", URLEncoder.encode( baseUrl + carrier + "send_sponsor.jsp?sponsor_code=" + sd.getSponsor()[i].getSponsorCode() + "&" + uid, "SHIFT_JIS" ) );
                            root.appendChild( adurl );
                            // ｲﾝﾌﾟﾚｯｼｮﾝｶｳﾝﾄの加算
                            sd.setImpressionCount( sd.getSponsor()[i].getSponsorCode(), FLG_MOBILE );
                        }
                    }
                }
                // 広告の合計数
                Element adCount = document.createElement( "PARAM" );
                adCount.setAttribute( "name", "AD_COUNT" );
                adCount.setAttribute( "value", String.valueOf( sponsorCount + sponsorCountRote ) );
                root.appendChild( adCount );
            }
            else
            {
                // 広告の合計数
                Element adCount = document.createElement( "PARAM" );
                adCount.setAttribute( "name", "AD_COUNT" );
                adCount.setAttribute( "value", "0" );
                root.appendChild( adCount );
            }

            // 物件固有ノードを追加
            for( j = 0 ; j < pageRecords ; j++ )
            {
                if ( j >= allHotelNum - pageRecords * pageNum )
                {
                    break;
                }
                // ホテル情報取得
                hotelId = arrHotelIdList[firstHotelNum + j];
                dhb.getData( hotelId );
                dhs.getData( hotelId );
                dhm.getData( hotelId );
                rank = dhb.getRank();

                // ﾃｽﾄｺｰﾄﾞ rank3以上をrank2に変更する。 TODO
                /*
                 * if ( rank >= 3 )
                 * {
                 * rank = 2;
                 * }
                 */

                // 項目画像
                if ( rank >= 2 )
                {
                    Element image = document.createElement( "IMAGE" );
                    image.setAttribute( "name", "IMAGE" + (j + 1) );
                    image.setAttribute( "type", "conversion" );
                    image.setAttribute( "value", "/happyhotel/common/images/HB/" + (hotelId) + "f.jpg" );
                    // image.setAttribute( "value", "http://happyhotel.jp/common/images/HB/" + (hotelId) + "f.jpg" );
                    root.appendChild( image );
                }
                // 項目番号
                Element nump = document.createElement( "TEXT" );
                nump.setAttribute( "name", "NUMP" + (j + 1) );
                nump.setAttribute( "value", String.valueOf( firstHotelNum + j + 1 ) );
                root.appendChild( nump );
                // ホテル名
                Element titles = document.createElement( "TEXT" );
                titles.setAttribute( "name", "TITLES" + (j + 1) );
                titles.setAttribute( "type", "urlencoded" );
                titles.setAttribute( "value", URLEncoder.encode( ReplaceString.replaceMobile( dhb.getNameMobile() ), "SHIFT_JIS" ) );
                root.appendChild( titles );
                // 住所
                Element texts1 = document.createElement( "TEXT" );
                texts1.setAttribute( "name", "TEXT1_" + (j + 1) );
                texts1.setAttribute( "value",
                        ReplaceString.replaceMobile( dhb.getAddress1() ) +
                                ReplaceString.replaceMobile( dhb.getAddress2() ) );
                root.appendChild( texts1 );
                // クチコミ
                Element texts2 = document.createElement( "TEXT" );
                Element texts3 = document.createElement( "TEXT" );
                texts2.setAttribute( "name", "TEXT2_" + (j + 1) );
                texts3.setAttribute( "name", "TEXT3_" + (j + 1) );
                if ( rank >= 2 )
                {
                    texts2.setAttribute( "value", "ｸﾁｺﾐ" );
                    String stars = "";
                    if ( bbs.getBbsList( hotelId, 1, 1, 1 ) )
                    {
                        kuchikomiCount = bbs.getBbsAllCount();
                        pointAverage = bbs.getPointAverageByDecimal( hotelId );
                        if ( pointAverage != null )
                        {
                            pointAverage = pointAverage.movePointRight( 2 );
                            integerAverage = pointAverage.intValue() / 100;
                            decimalAverage = pointAverage.intValue() % 100;
                            pointAverage = pointAverage.movePointLeft( 2 );
                        }
                        if ( kuchikomiCount > 0 )
                        {
                            for( k = 0 ; k < 5 ; k++ )
                            {
                                if ( k < integerAverage )
                                {
                                    stars = stars + "★";
                                }
                                else if ( decimalAverage >= 50 )
                                {
                                    decimalAverage = 0;
                                    stars = stars + "★";
                                }
                                else
                                {
                                    stars = stars + "☆";
                                }
                            }
                        }
                        else
                        {
                            stars = "未評価";
                        }
                    }
                    else
                    {
                        stars = "未評価";
                    }
                    texts3.setAttribute( "value", stars );
                }
                else
                {
                    texts2.setAttribute( "value", "" );
                    texts3.setAttribute( "value", "" );
                }
                root.appendChild( texts2 );
                root.appendChild( texts3 );
                // PR文
                Element texts4 = document.createElement( "TEXT" );
                texts4.setAttribute( "name", "TEXT4_" + (j + 1) );
                if ( rank >= 1 )
                {
                    pr = ReplaceString.replaceMobile( dhb.getPr() );
                    pr = ReplaceString.replace( pr, "<br>", "" );
                    pr = ReplaceString.replace( pr, "<BR>", "" );
                    texts4.setAttribute( "type", "urlencoded" );
                    texts4.setAttribute( "value", URLEncoder.encode( pr, "SHIFT_JIS" ) );
                }
                else
                {
                    texts4.setAttribute( "value", "" );
                }
                root.appendChild( texts4 );
                // 【空】表示
                Element texts5 = document.createElement( "TEXT" );
                texts5.setAttribute( "name", "TEXT5_" + (j + 1) );
                emptyStatus = "";

                // hh_hotel_masterのempty_disp_kind=1が表示対象
                // hh_hotel_statusのempty_status=1のみ表示する
                if ( dhm.getEmptyDispKind() == 1 )
                {
                    if ( dhb.getEmptyStatus() == 1 )
                    {
                        emptyStatus = "空 ";
                    }
                }
                texts5.setAttribute( "value", emptyStatus );
                root.appendChild( texts5 );

                // TEL
                Element texts6 = document.createElement( "TEXT" );
                texts6.setAttribute( "name", "TEXT6_" + (j + 1) );
                texts6.setAttribute( "value", ReplaceString.replaceMobile( dhb.getTel1() ) );
                root.appendChild( texts6 );

                // 詳細ページリンクアドレス
                Element link = document.createElement( "PARAM" );
                link.setAttribute( "name", "LINK" + (j + 1) );
                if ( rank >= 1 )
                {
                    link.setAttribute( "type", "urlencoded" );
                    link.setAttribute( "value", URLEncoder.encode( baseUrl + carrier + "search/hotel_details.jsp?hotel_id=" + hotelId + "&" + uid, "SHIFT_JIS" ) );
                }
                else
                {
                    link.setAttribute( "value", "" );
                }
                root.appendChild( link );
                // 契約ﾗﾝｸ
                Element Rank = document.createElement( "PARAM" );
                Rank.setAttribute( "name", "RANK" + (j + 1) );
                Rank.setAttribute( "value", String.valueOf( rank ) );
                root.appendChild( Rank );
            }
            // xmlファイルを一時保存
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();
            tf.setOutputProperty( "encoding", "SHIFT_JIS" );
            tf.transform( new DOMSource( document ), new StreamResult( new File( xmlFilePath ) ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[GenerateXml1.GenerateXml] Exception=" + e.toString() );
            return(null);
        }
        return(xmlFilePath);
    }
}
