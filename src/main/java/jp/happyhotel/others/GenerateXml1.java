/*
 * @(#)OutputSwf.java 1.00 2009/06/15
 * Copyright (C) ALMEX Inc. 2009
 * flfast�pxml�t�@�C�������N���X
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
 * ��������flash�\��(flfast)�pxml�t�@�C�������N���X
 * flfast�p��xml�t�@�C���������A����FilePath��Ԃ�
 * 
 * @author N.Ide
 * @version 1.00 2009/06/15
 */

public class GenerateXml1 implements Serializable
{

    /**
     *
     */

    static int                pageRecords      = Constants.pageLimitRecordMobile; // 1�y�[�W�̍ő�\����

    private static final long serialVersionUID = -4131852613466879649L;

    /**
     * xml�t�@�C���𐶐�����
     * 
     * @param arrHotelIdList �������ʃz�e��ID���X�g
     * @param param �����ɗv����p�����[�^�Ƃ��̒l
     * @param pageNum �y�[�W�ԍ�
     * @param searchCondition ������������
     * @param termNo �[���ԍ�
     * @param userAgentType �g�уL�����A
     * @param actPath ����act�̃p�X
     * @param actPathFlash ����act(Flash)�̃p�X
     * @param prefId �s���{��ID(�L���\���p)
     * @return swf�o�C�i���f�[�^
     */
    static public String GenerateXml(int[] arrHotelIdList, String param, int pageNum, String searchCondition, String termNo,
            int userAgentType, String actPath, String actPathFlash, int prefId, String uid)
    {
        final boolean FLG_MOBILE = true;
        final int DISP_COUNT = 5; // �L���\����
        final int DISP_COUNT_ROTE = 1; // ���[�e�[�V�����L���\����

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
        final String templaetPath = "/happyhotel/flash/flfast/package2/1/FLK0001A.swf"; // �{�Ԋ�
        final String baseUrl = Url.getUrl() + "/";
        // final String baseUrl = "http://happyhotel.jp/_debug_/";
        // final String baseUrl = "http://121.101.88.177/"; // �Г����p

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
            DocumentBuilder docbuilder = dbfactory.newDocumentBuilder(); // DocumentBuilder�C���X�^���X
            Document document = docbuilder.newDocument(); // Document�̐���

            // ���[�g�m�[�h�쐬
            Element root = document.createElement( "SLIDE" );
            root.setAttribute( "template", templaetPath );
            // ���[�g�m�[�h��Document�ɒǉ�
            document.appendChild( root );

            // ���ʍ��ڃm�[�h��ǉ�
            // �^�C�g���i���������j
            Element title = document.createElement( "PARAM" );
            title.setAttribute( "name", "TITLE" );
            title.setAttribute( "value", searchCondition );
            root.appendChild( title );
            // ���������i�S�����j
            Element numMax = document.createElement( "PARAM" );
            numMax.setAttribute( "name", "NUM_MAX" );
            numMax.setAttribute( "value", String.valueOf( allHotelNum ) );
            root.appendChild( numMax );
            // ���ݕ\����
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
            // �O�y�[�W�A�h���X
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
            // ��y�[�W�A�h���X
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
            // �߂�惊���N�A�h���X
            Element modoru = document.createElement( "PARAM" );
            modoru.setAttribute( "name", "MODORU" );
            modoru.setAttribute( "type", "urlencoded" );
            modoru.setAttribute( "value",
                    URLEncoder.encode( baseUrl + carrier + actPath + "?" + uid, "SHIFT_JIS" ) ); // TODO �߂��A�h���X�ύX
            root.appendChild( modoru );
            // �e�L�X�g�Ń����N�A�h���X
            Element textmode = document.createElement( "PARAM" );
            textmode.setAttribute( "name", "TEXTMODE" );
            textmode.setAttribute( "type", "urlencoded" );
            textmode.setAttribute( "value",
                    URLEncoder.encode( baseUrl + carrier + actPath + "?" + param + "&page=" + (pageNum) + "&" + uid, "SHIFT_JIS" ) );
            root.appendChild( textmode );
            // 1�y�[�W�̌���
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
            // �����\���ʒu
            Element itemCurrent = document.createElement( "PARAM" );
            itemCurrent.setAttribute( "name", "ITEM_CURRENT" );
            itemCurrent.setAttribute( "value", "0" );
            root.appendChild( itemCurrent );
            // �R�s�[���C�g�\�� //TODO �v�ύX
            Element copyText = document.createElement( "PARAM" );
            copyText.setAttribute( "name", "COPYTEXT" );
            copyText.setAttribute( "value", "(C)ALMEX inc." );
            root.appendChild( copyText );

            // �L��
            if ( prefId > 0 )
            {
                // �m�[�}���L��
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
                            // �L���̕\��
                            Element adname = document.createElement( "TEXT" );
                            adname.setAttribute( "name", "ADNAME" + (i + 1) );
                            adname.setAttribute( "type", "urlencoded" );
                            adname.setAttribute( "value", URLEncoder.encode( ReplaceString.replaceMobile( sd.getSponsor()[i].getTitleMobile() ), "SHIFT_JIS" ) );
                            root.appendChild( adname );
                            // �L���̃����N��
                            Element adurl = document.createElement( "PARAM" );
                            adurl.setAttribute( "name", "ADLINK" + (i + 1) );
                            adurl.setAttribute( "type", "urlencoded" );
                            adurl.setAttribute( "value", URLEncoder.encode( baseUrl + carrier + "send_sponsor.jsp?sponsor_code=" + sd.getSponsor()[i].getSponsorCode() + "&" + uid, "SHIFT_JIS" ) );
                            root.appendChild( adurl );
                            // ����گ��ݶ��Ẳ��Z
                            sd.setImpressionCount( sd.getSponsor()[i].getSponsorCode(), FLG_MOBILE );
                        }
                    }
                }
                // ���[�e�[�V�����L��
                ret = sd.getRandomSponsorByPref( prefId, DISP_COUNT_ROTE, FLG_MOBILE );
                if ( ret )
                {
                    sponsorCountRote = sd.getSponsorCount();
                    if ( sponsorCountRote > 0 )
                    {
                        for( i = 0 ; i < sponsorCountRote ; i++ )
                        {
                            // �L���̕\��
                            Element adname = document.createElement( "TEXT" );
                            adname.setAttribute( "name", "ADNAME" + (sponsorCount + i + 1) );
                            adname.setAttribute( "type", "urlencoded" );
                            adname.setAttribute( "value", URLEncoder.encode( ReplaceString.replaceMobile( sd.getSponsor()[i].getTitleMobile() ), "SHIFT_JIS" ) );
                            root.appendChild( adname );
                            // �L���̃����N��
                            Element adurl = document.createElement( "PARAM" );
                            adurl.setAttribute( "name", "ADLINK" + (sponsorCount + i + 1) );
                            adurl.setAttribute( "type", "urlencoded" );
                            adurl.setAttribute( "value", URLEncoder.encode( baseUrl + carrier + "send_sponsor.jsp?sponsor_code=" + sd.getSponsor()[i].getSponsorCode() + "&" + uid, "SHIFT_JIS" ) );
                            root.appendChild( adurl );
                            // ����گ��ݶ��Ẳ��Z
                            sd.setImpressionCount( sd.getSponsor()[i].getSponsorCode(), FLG_MOBILE );
                        }
                    }
                }
                // �L���̍��v��
                Element adCount = document.createElement( "PARAM" );
                adCount.setAttribute( "name", "AD_COUNT" );
                adCount.setAttribute( "value", String.valueOf( sponsorCount + sponsorCountRote ) );
                root.appendChild( adCount );
            }
            else
            {
                // �L���̍��v��
                Element adCount = document.createElement( "PARAM" );
                adCount.setAttribute( "name", "AD_COUNT" );
                adCount.setAttribute( "value", "0" );
                root.appendChild( adCount );
            }

            // �����ŗL�m�[�h��ǉ�
            for( j = 0 ; j < pageRecords ; j++ )
            {
                if ( j >= allHotelNum - pageRecords * pageNum )
                {
                    break;
                }
                // �z�e�����擾
                hotelId = arrHotelIdList[firstHotelNum + j];
                dhb.getData( hotelId );
                dhs.getData( hotelId );
                dhm.getData( hotelId );
                rank = dhb.getRank();

                // ýĺ��� rank3�ȏ��rank2�ɕύX����B TODO
                /*
                 * if ( rank >= 3 )
                 * {
                 * rank = 2;
                 * }
                 */

                // ���ډ摜
                if ( rank >= 2 )
                {
                    Element image = document.createElement( "IMAGE" );
                    image.setAttribute( "name", "IMAGE" + (j + 1) );
                    image.setAttribute( "type", "conversion" );
                    image.setAttribute( "value", "/happyhotel/common/images/HB/" + (hotelId) + "f.jpg" );
                    // image.setAttribute( "value", "http://happyhotel.jp/common/images/HB/" + (hotelId) + "f.jpg" );
                    root.appendChild( image );
                }
                // ���ڔԍ�
                Element nump = document.createElement( "TEXT" );
                nump.setAttribute( "name", "NUMP" + (j + 1) );
                nump.setAttribute( "value", String.valueOf( firstHotelNum + j + 1 ) );
                root.appendChild( nump );
                // �z�e����
                Element titles = document.createElement( "TEXT" );
                titles.setAttribute( "name", "TITLES" + (j + 1) );
                titles.setAttribute( "type", "urlencoded" );
                titles.setAttribute( "value", URLEncoder.encode( ReplaceString.replaceMobile( dhb.getNameMobile() ), "SHIFT_JIS" ) );
                root.appendChild( titles );
                // �Z��
                Element texts1 = document.createElement( "TEXT" );
                texts1.setAttribute( "name", "TEXT1_" + (j + 1) );
                texts1.setAttribute( "value",
                        ReplaceString.replaceMobile( dhb.getAddress1() ) +
                                ReplaceString.replaceMobile( dhb.getAddress2() ) );
                root.appendChild( texts1 );
                // �N�`�R�~
                Element texts2 = document.createElement( "TEXT" );
                Element texts3 = document.createElement( "TEXT" );
                texts2.setAttribute( "name", "TEXT2_" + (j + 1) );
                texts3.setAttribute( "name", "TEXT3_" + (j + 1) );
                if ( rank >= 2 )
                {
                    texts2.setAttribute( "value", "����" );
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
                                    stars = stars + "��";
                                }
                                else if ( decimalAverage >= 50 )
                                {
                                    decimalAverage = 0;
                                    stars = stars + "��";
                                }
                                else
                                {
                                    stars = stars + "��";
                                }
                            }
                        }
                        else
                        {
                            stars = "���]��";
                        }
                    }
                    else
                    {
                        stars = "���]��";
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
                // PR��
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
                // �y��z�\��
                Element texts5 = document.createElement( "TEXT" );
                texts5.setAttribute( "name", "TEXT5_" + (j + 1) );
                emptyStatus = "";

                // hh_hotel_master��empty_disp_kind=1���\���Ώ�
                // hh_hotel_status��empty_status=1�̂ݕ\������
                if ( dhm.getEmptyDispKind() == 1 )
                {
                    if ( dhb.getEmptyStatus() == 1 )
                    {
                        emptyStatus = "�� ";
                    }
                }
                texts5.setAttribute( "value", emptyStatus );
                root.appendChild( texts5 );

                // TEL
                Element texts6 = document.createElement( "TEXT" );
                texts6.setAttribute( "name", "TEXT6_" + (j + 1) );
                texts6.setAttribute( "value", ReplaceString.replaceMobile( dhb.getTel1() ) );
                root.appendChild( texts6 );

                // �ڍ׃y�[�W�����N�A�h���X
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
                // �_���ݸ
                Element Rank = document.createElement( "PARAM" );
                Rank.setAttribute( "name", "RANK" + (j + 1) );
                Rank.setAttribute( "value", String.valueOf( rank ) );
                root.appendChild( Rank );
            }
            // xml�t�@�C�����ꎞ�ۑ�
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
