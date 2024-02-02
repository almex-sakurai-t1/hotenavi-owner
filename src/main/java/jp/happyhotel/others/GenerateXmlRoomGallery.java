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
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.hotel.HotelRoomMore;

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

public class GenerateXmlRoomGallery implements Serializable
{

    static int pageRecords = 6; // 1�y�[�W�̍ő�\����(�e���v���[�g�̏��)

    /**
     * xml�t�@�C���𐶐�����
     * 
     * @param arrHotelIdList �������ʃz�e��ID���X�g
     * @param param �����ɗv����p�����[�^�Ƃ��̒l
     * @param pageNum �y�[�W�ԍ�
     * @param searchCondition ������������
     * @param termNo �[���ԍ�
     * @param userAgentType �g�уL�����A
     * @param category �M�������[�̃J�e�S��
     * @return �������ꂽxml��FilePath
     */
    static public String GenerateXml(int hotelId, String param, int pageNum, int currentNum, String termNo, int userAgentType, String flashActPath, String paramBefore)
    {
        DataHotelBasic dhb;
        HotelRoomMore hrm;
        DataHotelRoomMore dhrm;

        final int ZERO = 0;
        int i;
        int allRoomNum;
        int firstRoomNum = 0;
        int lastPage = 0;
        boolean existReferName = false;
        boolean existImage = false;
        boolean existBigImage = false;
        boolean ret = false;
        String xmlFilePath = "";
        final String templetePath = "/happyhotel/flash/flfast/package2/2/hotenavi_temp2.swf";
        String carrier = "";
        String mvishaReturnUrl = "";
        String roomNameAndRank = "";
        String tempUrl = "";
        String roomText = "";
        final String baseDir = "/usr/local/tomcat/temp";
        final String baseUrl = Url.getUrl() + "/";
        // final String baseUrl = "http://happyhotel.jp/_debug_/";
        // final String baseUrl = "http://121.101.88.177/"; // �Г����p
        final String mvishaUrl = Url.getUrl() + "/flash-cgi/if/kf.cgi";
        // final String mvishaUrl = "http://121.101.88.177/flash/if/kf.cgi";
        NumberFormat nf;

        xmlFilePath = baseDir + "/" + termNo + ".gallery.xml";
        // Logging.info( "[GenerateXmlGallery1.GenerateXml] param:" + param );

        firstRoomNum = pageNum * pageRecords;
        dhb = new DataHotelBasic();
        nf = new DecimalFormat( "0000" );

        hrm = new HotelRoomMore();
        dhrm = new DataHotelRoomMore();
        hrm.getRoomGallery( hotelId, ZERO, ZERO );
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

            // �R�s�[���C�g�\�� //TODO �v�ύX
            Element copyText = document.createElement( "PARAM" );
            copyText.setAttribute( "name", "COPYTEXT" );
            // copyText.setAttribute( "value", "(C)USEN CORPRATION/(C)imedia inc." );
            copyText.setAttribute( "value", "(C)ALMEX" );
            root.appendChild( copyText );

            // �������Ƃ̃m�[�h��ǉ�
            for( i = 0 ; i < pageRecords ; i++ )
            {
                if ( i >= allRoomNum - pageRecords * pageNum )
                {
                    break;
                }

                // �����摜
                Element image = document.createElement( "IMAGE" );
                image.setAttribute( "name", "IMAGE0" + (i + 1) );
                image.setAttribute( "type", "conversion" );

                // �����̉摜��������
                if ( hrm.getHotelRoom()[firstRoomNum + i].getRoomNo() == hrm.getHotelRoom()[firstRoomNum + i].getSeq() )
                {
                    image.setAttribute( "value", getImageRoomUrl( hotelId, dhb.getHotenaviId(), hrm.getHotelRoom()[firstRoomNum + i].getSeq(), hrm.getHotelRoom()[firstRoomNum + i].getReferName() ) );
                }
                else
                {
                    image.setAttribute( "value", getImageGalleryUrl( hotelId, dhb.getHotenaviId(), hrm.getHotelRoom()[firstRoomNum + i].getSeq(), hrm.getHotelRoom()[firstRoomNum + i].getReferName() ) );
                }

                root.appendChild( image );
                // [�����ԍ�]�@�������iseq��room_no��������������"����"�ƕ\������j
                Element text2 = document.createElement( "TEXT" );
                text2.setAttribute( "name", "TEXT" + (i + 1) + "_1" );
                text2.setAttribute( "type", "urlencoded" );

                ret = false;
                roomNameAndRank = "";
                dhrm = null;
                dhrm = new DataHotelRoomMore();
                ret = dhrm.getData( hotelId, hrm.getHotelRoom()[firstRoomNum + i].getRoomNo() );
                if ( ret != false )
                {
                    // �R�t�����̕�������\��
                    if ( dhrm.getRoomName().compareTo( "" ) != 0 )
                    {
                        roomNameAndRank = "[" + ReplaceString.replaceMobile( dhrm.getRoomName() );
                        if ( dhrm.getRoomName().matches( "[0-9]+" ) != false )
                        {
                            roomNameAndRank += "����";
                        }
                        roomNameAndRank += "]";
                    }
                }

                // ��������\������iseq��room_no��������������"����"�ƕ\������j
                if ( hrm.getHotelRoom()[firstRoomNum + i].getRoomName() != null )
                {
                    if ( roomNameAndRank.compareTo( "" ) != 0 )
                    {
                        roomNameAndRank += "  ";
                    }
                    if ( hrm.getHotelRoom()[firstRoomNum + i].getRoomNo() == hrm.getHotelRoom()[firstRoomNum + i].getSeq() )
                    {
                        roomNameAndRank += "����";
                    }
                    else
                    {
                        roomNameAndRank += ReplaceString.replaceMobile( hrm.getHotelRoom()[firstRoomNum + i].getRoomName() );
                    }
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
                // RoomText������ꍇ�̓e�L�X�g��\��
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
                // �Ȃ��ꍇ�A[�����ԍ�]�@�������iseq��room_no��������������"����"�ƕ\������j
                else
                {
                    text3.setAttribute( "value", "" );
                    text4.setAttribute( "type", "urlencoded" );
                    text5.setAttribute( "type", "urlencoded" );

                    if ( ret != false )
                    {
                        if ( dhrm.getRoomName().matches( "[0-9]+" ) != false )
                        {
                            text4.setAttribute( "value", "[" + dhrm.getRoomName() + "����]" );
                        }
                        else
                        {
                            text4.setAttribute( "value", "[" + ReplaceString.replaceMobile( dhrm.getRoomName().replace( "<br>", " " ) ) + "]" );
                        }
                    }

                    if ( hrm.getHotelRoom()[firstRoomNum + i].getRoomName() != null )
                    {
                        // �����ԍ���seq�������������畔���ƕ\������
                        if ( hrm.getHotelRoom()[firstRoomNum + i].getRoomNo() == hrm.getHotelRoom()[firstRoomNum + i].getSeq() )
                        {
                            text5.setAttribute( "value", URLEncoder.encode( "����", "SHIFT-JIS" ) );
                        }
                        else
                        {
                            text5.setAttribute( "value", ReplaceString.replaceMobile( hrm.getHotelRoom()[firstRoomNum + i].getRoomName() ) );
                        }
                    }
                }
                root.appendChild( text3 );
                root.appendChild( text4 );
                root.appendChild( text5 );

                // �������{�^�������N��i�e�L�X�g�Ńy�[�W�A�h���X�j
                Element linkBtnA = document.createElement( "PARAM" );
                linkBtnA.setAttribute( "name", "LINKBTNA" + (i + 1) );

                if ( hrm.getHotelRoom()[firstRoomNum + i].getRoomNo() == hrm.getHotelRoom()[firstRoomNum + i].getSeq() )
                {
                    linkBtnA.setAttribute( "value", URLEncoder.encode( baseUrl + carrier + "search/hotel_roomdetails.jsp?" + param + "&seq=" + hrm.getHotelRoom()[firstRoomNum + i].getSeq() + "&more=true", "SHIFT-JIS" ) );
                }
                else
                {
                    linkBtnA.setAttribute( "value", URLEncoder.encode( baseUrl + carrier + "search/room_gallerydetails.jsp?" + param + "&seq=" + hrm.getHotelRoom()[firstRoomNum + i].getSeq() + "&more=true", "SHIFT-JIS" ) );
                }
                linkBtnA.setAttribute( "type", "urlencoded" );
                root.appendChild( linkBtnA );
                // �����E�{�^�������N��imvisha�����N��j
                Element linkBtnB = document.createElement( "PARAM" );
                linkBtnB.setAttribute( "name", "LINKBTNB" + (i + 1) );
                linkBtnB.setAttribute( "type", "urlencoded" );
                // mvisha����̖߂��URL��Encode
                mvishaReturnUrl = URLEncoder.encode( baseUrl + carrier + "search/roomGalleryFlashList.act?" + param + "&page=" + pageNum + "&num=" + i, "SHIFT-JIS" );

                // mvisha�ŕ\������摜��URL���擾
                tempUrl = mvishaUrl + "?file=";

                // �����̉摜��������
                if ( hrm.getHotelRoom()[firstRoomNum + i].getRoomNo() == hrm.getHotelRoom()[firstRoomNum + i].getSeq() )
                {
                    tempUrl += getImageRoomUrl( hotelId, dhb.getHotenaviId(), hrm.getHotelRoom()[firstRoomNum + i].getSeq(), hrm.getHotelRoom()[firstRoomNum + i].getReferName() );
                }
                else
                {
                    tempUrl += getImageGalleryUrl( hotelId, dhb.getHotenaviId(), hrm.getHotelRoom()[firstRoomNum + i].getSeq(), hrm.getHotelRoom()[firstRoomNum + i].getReferName() );
                }
                // �擾����mvisha�̉摜�C���[�W�Ɩ߂���URL���Z�b�g
                linkBtnB.setAttribute( "value", URLEncoder.encode( tempUrl + "&tp=" + mvishaReturnUrl, "SHIFT-JIS" ) );
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
            Logging.error( "[GenerateXmlGallery1.GenerateXml] Exception=" + e.toString() );
            return(null);
        }
        return(xmlFilePath);
    }

    /* �����摜��URL�擾 */
    static public String getImageRoomUrl(int hotelId, String hotenaviId, int seq, String referName)
    {
        String returnUrl = "";
        boolean existReferName = false;
        boolean existImage = false;
        NumberFormat nf;

        nf = new DecimalFormat( "0000" );
        // hh_hotel_room_more.refer_name�������Ă�����z�e�i�r����
        existReferName = (referName != null) && (referName.compareTo( "" ) != 0);
        if ( existReferName != false )
        {
            returnUrl = "/happyhotel/hotenavi/" + hotenaviId + "/image/r" + referName + ".jpg";
        }
        else
        {
            File file = new File( "/happyhotel/common/images/HRM/" + hotelId + "_" + nf.format( seq ) + "jpg.jpg" );
            existImage = file.exists();
            if ( existImage != false )
            {
                returnUrl = "/happyhotel/common/images/HRM/" + hotelId + "_" + nf.format( seq ) + "jpg.jpg";
            }
            else
            {
                returnUrl = "/happyhotel/common/images/noimage.jpg";
            }
        }

        return(returnUrl);
    }

    /* �M�������[�摜��URL�擾 */
    static public String getImageGalleryUrl(int hotelId, String hotenaviId, int seq, String referName)
    {
        String returnUrl = "";
        boolean existReferName = false;
        boolean existImage = false;
        NumberFormat nf;

        nf = new DecimalFormat( "0000" );
        existReferName = (referName != null) && (referName.compareTo( "" ) != 0);

        if ( existReferName != false )
        {
            returnUrl = "/happyhotel/common/room/" + hotenaviId + "/gallery/g" + referName + ".jpg";
        }
        else
        {
            File file = new File( "/happyhotel/common/room/" + hotelId + "/gallery/g" + nf.format( seq ) + ".jpg" );
            existImage = file.exists();
            if ( existImage != false )
            {
                returnUrl = "/happyhotel/common/room/" + hotelId + "/gallery/g" + nf.format( seq ) + ".jpg";
            }
            else
            {
                returnUrl = "/happyhotel/common/images/noimage.jpg";
            }
        }

        return(returnUrl);
    }
}
