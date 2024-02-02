/*
 * @(#)OutputSwf.java 1.00 2009/06/15
 * Copyright (C) ALMEX Inc. 2009
 * flfast�pxml�t�@�C�������N���X
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
import jp.happyhotel.hotel.HotelRoomMore;
import jp.happyhotel.hotel.HotelRoomrank;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * �����摜�ꗗflash�\��(flfast)�pxml�t�@�C�������N���X
 * flfast�p��xml�t�@�C���������A����FilePath��Ԃ�
 * 
 * 
 * @author N.Ide
 * @version 1.00 2009/06/15
 */

public class GenerateXmlRoom1 implements Serializable
{

    /**
     *
     */

    static int                pageRecords      = 6;                    // 1�y�[�W�̍ő�\����(�e���v���[�g�̏��)

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
     * @return �������ꂽxml��FilePath
     */
    static public String GenerateXml(int hotelId, String param, int pageNum, int currentNum, String termNo, int userAgentType, String flashActPath, String paramBefore)
    {
        DataHotelBasic dhb;
        HotelRoomMore hrm;
        HotelRoomrank hrr;

        int i;
        int allRoomNum;
        int firstRoomNum = 0;
        int lastPage = 0;
        boolean existReferName = false;
        boolean existImage = false;
        boolean existBigImage = false;
        String xmlFilePath = "";
        final String templetePath = "/happyhotel/flash/flfast/package2/2/hotenavi_temp.swf";
        // final String templetePath = "/hotenavi/flash/flfast/package2/2/hotenavi_temp.swf"; // �Г����p
        String carrier = "";
        String mvishaReturnUrl = "";
        String roomNameAndRank = "";
        String roomText = "";
        final String baseDir = "/usr/local/tomcat/temp";
        final String baseUrl = Url.getUrl() + "/";
        // final String baseUrl = "http://happyhotel.jp/_debug_/";
        // final String baseUrl = "http://121.101.88.177/"; // �Г����p
        final String mvishaUrl = Url.getUrl() + "/flash-cgi/if/kf.cgi";
        // final String mvishaUrl = "http://121.101.88.177/flash/if/kf.cgi";
        NumberFormat nf;

        xmlFilePath = baseDir + "/" + termNo + ".room.xml";
        // Logging.info( "[GenerateXmlRoom1.GenerateXml] param:" + param );

        firstRoomNum = pageNum * pageRecords;
        dhb = new DataHotelBasic();
        hrm = new HotelRoomMore();
        hrr = new HotelRoomrank();
        nf = new DecimalFormat( "0000" );

        hrm.getRoomData( hotelId, "" );
        dhb.getData( hotelId );

        allRoomNum = hrm.getHotelRoomCount();
        lastPage = (hrm.getHotelRoomCount() - 1) / pageRecords;
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
            DocumentBuilder docbuilder = dbfactory.newDocumentBuilder(); // DocumentBuilder�C���X�^���X
            Document document = docbuilder.newDocument(); // Document�̐���

            // ���[�g�m�[�h�쐬
            Element root = document.createElement( "SLIDE" );
            root.setAttribute( "template", templetePath );
            // ���[�g�m�[�h��Document�ɒǉ�
            document.appendChild( root );

            // ���ʍ��ڃm�[�h��ǉ�
            // ���ʃ��S�摜 -> �s�v
            /*
             * Element image0 = document.createElement( "IMAGE" );
             * image0.setAttribute( "name", "IMAGE00" );
             * image0.setAttribute( "type", "conversion" );
             * // image0.setAttribute( "value", baseUrl + "common/images/l_logo_areamap.jpg" );
             * image0.setAttribute( "value", baseUrl + "common/image/m_cat_heart.gif" );
             * root.appendChild( image0 );
             */
            // �z�e����
            Element text1 = document.createElement( "TEXT" );
            text1.setAttribute( "name", "TEXT1_2" );
            text1.setAttribute( "type", "urlencoded" );
            text1.setAttribute( "value", URLEncoder.encode( ReplaceString.replaceMobile( dhb.getName() ), "SHIFT-JIS" ) );
            root.appendChild( text1 );
            // �z�e���ڍ׃A�h���X
            Element linkBack = document.createElement( "PARAM" );
            linkBack.setAttribute( "name", "LINKBACK" );
            linkBack.setAttribute( "type", "urlencoded" );
            linkBack.setAttribute( "value",
                    URLEncoder.encode( baseUrl + carrier + "search/hotel_details.jsp?" + param, "SHIFT-JIS" ) );
            root.appendChild( linkBack );
            // TOP�A�h���X
            Element linkTop = document.createElement( "PARAM" );
            linkTop.setAttribute( "name", "LINKTOP" );
            linkTop.setAttribute( "type", "urlencoded" );
            linkTop.setAttribute( "value",
                    URLEncoder.encode( baseUrl + carrier + "index.jsp?" + param, "SHIFT-JIS" ) );
            root.appendChild( linkTop );
            // �O�y�[�WXML�����N�A�h���X
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
            // ��y�[�WXML�����N�A�h���X
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
            /*
             * // �������̃{�^���̕����� -> flash�ɒ��ڋL�q
             * Element textBtnA = document.createElement( "TEXT" );
             * textBtnA.setAttribute( "name", "TEXTBTNA" );
             * textBtnA.setAttribute( "value", "�ڍ׏��" );
             * root.appendChild( textBtnA );
             * // �����E�̃{�^���̕����� -> flash�ɒ��ڋL�q
             * Element textBtnB = document.createElement( "TEXT" );
             * textBtnB.setAttribute( "name", "TEXTBTNB" );
             * textBtnB.setAttribute( "value", "�g��/�k��" );
             * root.appendChild( textBtnB );
             */
            /*
             * // �d�Ȃ�\������ -> ���݂͎g�p����
             * Element textA = document.createElement( "TEXT" );
             * textA.setAttribute( "name", "TEXTA" );
             * textA.setAttribute( "type", "urlencoded" );
             * textA.setAttribute( "value", "��������" );
             * root.appendChild( textA );
             * // �d�Ȃ�\������ -> ���݂͎g�p����
             * Element textB = document.createElement( "TEXT" );
             * textB.setAttribute( "name", "TEXTB" );
             * textB.setAttribute( "value", "�ݸ����" );
             * root.appendChild( textB );
             */
            // 1�y�[�W�̌���
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
            // �����\���ʒu
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
            // ���߰�ނ���߂����ꍇ�͍Ō�̍��ڂɍ��킹��
            else
            {
                itemCurrent.setAttribute( "value", Integer.toString( pageRecords - 1 ) );
            }
            root.appendChild( itemCurrent );
            /*
             * // �R�s�[���C�g�\�� -> flash�ɒ��ڋL�q
             * Element copyText = document.createElement( "PARAM" );
             * copyText.setAttribute( "name", "COPYTEXT" );
             * copyText.setAttribute( "value", "(C)USEN CORPRATION/(C)Imedia inc." );
             * root.appendChild( copyText );
             * // �w�i�F -> Flash�Œ��ڐݒ�
             * Element backgroundColor = document.createElement( "PARAM" );
             * backgroundColor.setAttribute( "name", "BGCOLOR" );
             * backgroundColor.setAttribute( "value", "11" );
             * root.appendChild( backgroundColor );
             */
            // �������Ƃ̃m�[�h��ǉ�
            for( i = 0 ; i < pageRecords ; i++ )
            {
                if ( i >= allRoomNum - pageRecords * pageNum )
                {
                    break;
                }

                hrr.getRoomrankDataByRank( hotelId, hrm.getHotelRoom()[firstRoomNum + i].getRoomRank() );

                // �����摜
                Element image = document.createElement( "IMAGE" );
                image.setAttribute( "name", "IMAGE0" + (i + 1) );
                image.setAttribute( "type", "conversion" );
                // hh_hotel_room_more.refer_name�������Ă�����z�e�i�r����
                existReferName = (hrm.getHotelRoom()[firstRoomNum + i].getReferName() != null) && (hrm.getHotelRoom()[firstRoomNum + i].getReferName().compareTo( "" ) != 0);
                if ( existReferName != false )
                {
                    image.setAttribute( "value", "/happyhotel/hotenavi/" + dhb.getHotenaviId() + "/image/r" + hrm.getHotelRoom()[firstRoomNum + i].getReferName() + ".jpg" );
                }
                else
                {
                    File file = new File( "/happyhotel/common/images/HRM/" + hotelId + "_" + nf.format( hrm.getHotelRoom()[firstRoomNum + i].getSeq() ) + "jpg.jpg" );
                    existImage = file.exists();
                    if ( existImage != false )
                    {
                        image.setAttribute( "value", "/happyhotel/common/images/HRM/" + hotelId + "_" + nf.format( hrm.getHotelRoom()[firstRoomNum + i].getSeq() ) + "jpg.jpg" );
                        // Logging.info( "image = " + baseUrl + "common/images/HRM/" + hotelId + "_" + nf.format( hrm.getHotelRoom()[firstRoomNum + i].getSeq() ) + "jpg.jpg" );
                    }
                    else
                    {
                        image.setAttribute( "value", "/happyhotel/common/images/noimage.jpg" );
                        // Logging.info( "image = " + baseUrl + "common/images/noimage.jpg" );
                    }
                }
                root.appendChild( image );
                // ������ - �����N��
                Element text2 = document.createElement( "TEXT" );
                text2.setAttribute( "name", "TEXT" + (i + 1) + "_1" );
                text2.setAttribute( "type", "urlencoded" );
                if ( hrm.getHotelRoom()[firstRoomNum + i].getRoomName() != null )
                {
                    roomNameAndRank = ReplaceString.replaceMobile( hrm.getHotelRoom()[firstRoomNum + i].getRoomName() );
                }
                if ( hrm.getHotelRoom()[firstRoomNum + i].getRoomName().matches( "[0-9]+" ) )
                {
                    roomNameAndRank += "����";
                }
                if ( hrr.getHotelRoomrank()[0].getRankName() != null && hrr.getHotelRoomrank()[0].getDispIndex() != 99 )
                {
                    roomNameAndRank += " - " + ReplaceString.replaceMobile( hrr.getHotelRoomrank()[0].getRankName() );
                }
                roomNameAndRank = roomNameAndRank.replace( "<br>", " " );
                roomNameAndRank = roomNameAndRank.replace( "<BR>", " " );
                text2.setAttribute( "value", URLEncoder.encode( ReplaceString.replaceMobile( roomNameAndRank ), "SHIFT-JIS" ) );

                root.appendChild( text2 );
                // PR���iPR��������ꍇ�j�A�������́iPR�����Ȃ��ꍇ�j�A�����N���́iPR�����Ȃ��ꍇ�j
                Element text3 = document.createElement( "TEXT" );
                text3.setAttribute( "name", "TEXT" + (i + 1) + "_3" );
                Element text4 = document.createElement( "TEXT" );
                text4.setAttribute( "name", "TEXT" + (i + 1) + "_4" );
                Element text5 = document.createElement( "TEXT" );
                text5.setAttribute( "name", "TEXT" + (i + 1) + "_5" );
                if ( (hrm.getHotelRoom()[firstRoomNum + i].getRoomText() != null) && (hrm.getHotelRoom()[firstRoomNum + i].getRoomText().compareTo( "" ) != 0) )
                {
                    roomText = ReplaceString.replaceMobile( hrm.getHotelRoom()[firstRoomNum + i].getRoomText() );
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
                    if ( hrm.getHotelRoom()[firstRoomNum + i].getRoomName() != null )
                    {
                        if ( hrm.getHotelRoom()[firstRoomNum + i].getRoomName().matches( "[0-9]+" ) )
                        {
                            text4.setAttribute( "value", URLEncoder.encode( ReplaceString.replaceMobile( hrm.getHotelRoom()[firstRoomNum + i].getRoomName() + "����" ), "SHIFT-JIS" ) );
                        }
                        else
                        {
                            text4.setAttribute( "value", ReplaceString.replaceMobile( hrm.getHotelRoom()[firstRoomNum + i].getRoomName() ) );
                        }
                    }
                    if ( hrr.getHotelRoomrank()[0].getRankName() != null && hrr.getHotelRoomrank()[0].getDispIndex() != 99 )
                    {
                        text5.setAttribute( "value", ReplaceString.replaceMobile( hrr.getHotelRoomrank()[0].getRankName().replace( "<br>", " " ) ) );
                    }
                }
                root.appendChild( text3 );
                root.appendChild( text4 );
                root.appendChild( text5 );
                /*
                 * // �d�Ȃ�\���E�� -> ���݂͎g�p����
                 * Element textA1 = document.createElement( "TEXT" );
                 * textA1.setAttribute( "name", "TEXTA1_" + (j + 1) );
                 * textA1.setAttribute( "value", ReplaceString.replaceMobile( hrm.getHotelRoom()[firstRoomNum + j].getRoomText() ) );
                 * root.appendChild( textA1 );
                 * // �d�Ȃ�\���E�� -> ���݂͎g�p����
                 * Element textB1 = document.createElement( "TEXT" );
                 * textB1.setAttribute( "name", "TEXTB1_" + (j + 1) );
                 * textB1.setAttribute( "value", ReplaceString.replaceMobile( hrr.getHotelRoomrank()[0].getRoomText() ) );
                 * root.appendChild( textB1 );
                 */
                // �������{�^�������N��i�e�L�X�g�Ńy�[�W�A�h���X�j
                Element linkBtnA = document.createElement( "PARAM" );
                linkBtnA.setAttribute( "name", "LINKBTNA" + (i + 1) );
                linkBtnA.setAttribute( "value", URLEncoder.encode( baseUrl + carrier + "search/hotel_roomdetails.jsp?" + param + "&seq=" + hrm.getHotelRoom()[firstRoomNum + i].getSeq() + "&more=true", "SHIFT-JIS" ) );
                linkBtnA.setAttribute( "type", "urlencoded" );
                root.appendChild( linkBtnA );
                // �����E�{�^�������N��imvisha�����N��j
                Element linkBtnB = document.createElement( "PARAM" );
                linkBtnB.setAttribute( "name", "LINKBTNB" + (i + 1) );
                linkBtnB.setAttribute( "type", "urlencoded" );
                // mvisha����̖߂��URL��Encode
                mvishaReturnUrl = URLEncoder.encode( baseUrl + carrier + "search/roomFlashList.act?" + param + "&page=" + pageNum + "&num=" + i, "SHIFT-JIS" );
                if ( existReferName != false )
                {
                    // ��摜�̗L�����m�F
                    File fileBig = new File( "/happyhotel/hotenavi/" + dhb.getHotenaviId() + "/image/r" + hrm.getHotelRoom()[firstRoomNum + i].getReferName() + "m.jpg" );
                    existBigImage = fileBig.exists();
                    if ( existBigImage != false )
                    {
                        linkBtnB.setAttribute( "value",
                                URLEncoder.encode( mvishaUrl + "?file=/happyhotel/hotenavi/" + dhb.getHotenaviId() + "/image/r" + hrm.getHotelRoom()[firstRoomNum + i].getReferName() + "m.jpg" +
                                        "&tp=" + mvishaReturnUrl, "SHIFT-JIS" ) );
                        /*
                         * �Г����p
                         * linkBtnB.setAttribute( "value",
                         * URLEncoder.encode( mvishaUrl + "?file=../../happyhotel/hotenavi/" + dhb.getHotenaviId() + "/image/r" + hrm.getHotelRoom()[firstRoomNum + i].getReferName() + "m.jpg" +
                         * "&tp=" + mvishaReturnUrl, "SHIFT-JIS" ) );
                         */
                    }
                    else
                    {
                        linkBtnB.setAttribute( "value",
                                URLEncoder.encode( mvishaUrl + "?file=/happyhotel/hotenavi/" + dhb.getHotenaviId() + "/image/r" + hrm.getHotelRoom()[firstRoomNum + i].getReferName() + ".jpg" +
                                        "&tp=" + mvishaReturnUrl, "SHIFT-JIS" ) );
                        /*
                         * �Г����p
                         * linkBtnB.setAttribute( "value",
                         * URLEncoder.encode( mvishaUrl + "?file=../../happyhotel/hotenavi/" + dhb.getHotenaviId() + "/image/r" + hrm.getHotelRoom()[firstRoomNum + i].getReferName() + ".jpg" +
                         * "&tp=" + mvishaReturnUrl, "SHIFT-JIS" ) );
                         */
                    }
                }
                else
                {
                    if ( existImage != false )
                    {
                        linkBtnB.setAttribute( "value",
                                URLEncoder.encode( mvishaUrl + "?file=/happyhotel/common/images/HRM/" + hotelId + "_" + nf.format( hrm.getHotelRoom()[firstRoomNum + i].getSeq() ) + "jpg.jpg" +
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
            // xml�t�@�C�����ꎞ�ۑ�
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();
            tf.setOutputProperty( "encoding", "SHIFT_JIS" );
            tf.transform( new DOMSource( document ), new StreamResult( new File( xmlFilePath ) ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[GenerateXmlRoom1.GenerateXml] Exception=" + e.toString() );
            return(null);
        }
        return(xmlFilePath);
    }
}
