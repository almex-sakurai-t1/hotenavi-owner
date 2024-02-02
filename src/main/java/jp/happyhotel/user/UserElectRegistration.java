package jp.happyhotel.user;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckNgWord;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.SendMail;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataMasterChain;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.data.DataMasterPresent;
import jp.happyhotel.data.DataMasterZip;
import jp.happyhotel.data.DataUserElect;

/**
 * 
 * This class is called by the controller in case of FreeWordSearch for mobile It delegates to the related business logic for FreeWordSearch and dispatches the request to the required JSP.
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/18
 */

public class UserElectRegistration extends BaseAction
{

    /**
     *
     */
    private static final long serialVersionUID  = -9182084211676295161L;

    private RequestDispatcher requestDispatcher = null;

    /**
     * �|�C���gde�����@���又��
     * 
     * @param seq �Ǘ��ԍ�
     * @return (true:false)
     */
    public synchronized void execute(HttpServletRequest request, HttpServletResponse response)
    {
        Logging.info( "[ue.registUserElect] �r�����b�N�J�n" );

        int nTotal;
        int nMinusPoint;
        int i;
        int type = 1;
        int carrierFlag;
        int loop;
        final int MAX_REGIST = 3;
        final int ONCE = 1;
        boolean ret;
        boolean memberFlag;
        boolean boolUserId;
        boolean boolSeq;
        String strTitle;
        String paramSeq;
        String paramOpinion;
        String strUserId;
        String strAddress1;
        String strAddress2;
        String strZipCode1;
        String strZipCode2;
        String strZipCode;
        String strName;
        String strTel;
        String strPref;
        String strMemo;
        String strErr;
        String strCheck;
        String strHandleName;
        String mailAddr;
        String strHotelName;
        String strCarrierUrl;
        String paramUidLink;
        String uidParam;
        String uidLink;
        UserBasicInfo ubi;
        DataMasterPoint dmp;
        DataMasterPresent dmPresent;
        DataHotelBasic dhb;
        DataMasterChain dmChain;
        DataUserElect due;
        UserPoint up;
        UserElect ue;
        String strString;
        Cookie[] cookies;
        Cookie hhCookie;

        ret = false;
        dmp = new DataMasterPoint();
        due = new DataUserElect();
        up = new UserPoint();
        ue = new UserElect();
        dhb = new DataHotelBasic();
        ubi = new UserBasicInfo();
        dmChain = new DataMasterChain();
        dmPresent = new DataMasterPresent();
        strTitle = "";
        strErr = "";
        nTotal = 0;
        nMinusPoint = 0;
        carrierFlag = 1;
        strZipCode = "";
        strPref = "";
        strUserId = "";
        strHandleName = "";
        mailAddr = "";
        strHotelName = "";
        strString = "�`";
        boolUserId = false;
        boolSeq = false;
        memberFlag = false;
        strCarrierUrl = "";
        uidLink = "";
        hhCookie = null;

        paramUidLink = (String)request.getAttribute( "UID-LINK" );

        // cookie�̊m�F�iPC�p�j
        cookies = request.getCookies();
        if ( cookies != null )
        {
            for( loop = 0 ; loop < cookies.length ; loop++ )
            {
                if ( cookies[loop].getName().compareTo( "hhuid" ) == 0 )
                {
                    hhCookie = cookies[loop];
                    break;
                }
            }
        }

        if ( hhCookie != null )
        {
            ret = ubi.getUserBasicByCookie( hhCookie.getValue() );
            if ( ret != false )
            {
                memberFlag = true;
            }
        }

        // �g�їp
        if ( type == UserAgent.USERAGENT_DOCOMO || type == UserAgent.USERAGENT_VODAFONE || type == UserAgent.USERAGENT_AU )
        {

            if ( type == UserAgent.USERAGENT_AU )
            {
                uidParam = request.getHeader( "x-up-subno" );
                uidLink = "";
                strCarrierUrl = "/au/others/present_offer_hotel_write.jsp";
            }
            else if ( type == UserAgent.USERAGENT_VODAFONE )
            {
                uidParam = request.getHeader( "x-jphone-uid" );
                // UID�ʒm���Ă��Ȃ��ꍇ�AuidParam��null�ɂȂ�
                if ( uidParam != null )
                {
                    uidParam = uidParam.substring( 1 );
                }
                uidLink = "uid=1&sid=BN14&pid=P423";
                strCarrierUrl = "/y/others/present_offer_hotel_write.jsp";
            }
            else if ( type == UserAgent.USERAGENT_DOCOMO )
            {
                uidParam = request.getParameter( "uid" );
                uidLink = "uid=NULLGWDOCOMO";
                strCarrierUrl = "/i/others/present_offer_hotel_write.jsp";
            }
            else
            {
                uidLink = "";
                uidParam = "";
                strCarrierUrl = "/others/present_offer_hotel_write.jsp";
            }

            if ( uidParam != null )
            {
                if ( request.getServerPort() != 80 && type == UserAgent.USERAGENT_DOCOMO )
                {
                    ret = ubi.getUserBasicByMd5( uidParam );
                }
                else
                {
                    ret = ubi.getUserBasicByTermno( uidParam );
                }
                if ( ret != false )
                {
                    memberFlag = true;
                }
                else
                {
                    memberFlag = false;
                }

                if ( request.getServerPort() != 80 && type == UserAgent.USERAGENT_DOCOMO )
                {
                    uidLink = "uid=" + "uid=" + ubi.getUserInfo().getMailAddrMobileMd5();
                }
            }
        }

        strName = request.getParameter( "name" );
        strPref = request.getParameter( "pref" );
        paramSeq = request.getParameter( "seq" );
        paramOpinion = request.getParameter( "opinion" );
        strZipCode1 = request.getParameter( "zip_code1" );
        strZipCode2 = request.getParameter( "zip_code2" );
        strAddress1 = request.getParameter( "address1" );
        strAddress2 = request.getParameter( "address2" );
        strTel = request.getParameter( "tel" );
        strMemo = request.getParameter( "memo" );
        strCheck = request.getParameter( "check" );
        if ( strZipCode1 == null )
            strZipCode1 = "";
        if ( strZipCode2 == null )
            strZipCode2 = "";
        strZipCode = strZipCode1 + strZipCode2;

        // �I�������ް����擾
        if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || (CheckString.numCheck( paramSeq ) == false) )
        {
            paramSeq = "0";
            strErr = strErr + "�ܕi��I�����Ă��������B<br>";
        }
        if ( paramOpinion == null )
            paramOpinion = "";
        if ( CheckNgWord.ngWordCheck( paramOpinion ) != false )
            strErr = strErr + "NGܰ�ނ������Ă��邽�߁A�o�^�ł��܂���<br>";

        if ( (strName == null) || (strName.compareTo( "" ) == 0) )
        {
            strName = "";
            strErr = strErr + "���t�悲���������͂���Ă��܂���B<br>";
        }
        if ( (strPref == null) || (strPref.compareTo( "" ) == 0) )
        {
            strPref = "";
            strErr = strErr + "�s���{�����I������Ă��܂���B<br>";
        }
        if ( (strZipCode.length() >= 1 && strZipCode.length() <= 6) || strZipCode.compareTo( "" ) == 0 )
        {
            strZipCode = "0";
            strErr = strErr + "�X�֔ԍ������������͂���Ă��܂���B<br>";
        }
        else
        {
            if ( CheckString.numCheck( strZipCode ) == false )
            {
                strErr = strErr + "�X�֔ԍ������������͂���Ă��܂���B<br>";
                strZipCode = "0";
            }
        }
        if ( (strAddress1 == null) || (strAddress1.compareTo( "" ) == 0) )
        {
            strAddress1 = "";
            strErr = strErr + "�s�撬�������͂���Ă��܂���B<br>";
        }
        if ( (strAddress2 == null) || (strAddress2.compareTo( "" ) == 0) )
        {
            strAddress2 = "";
        }
        if ( (strTel == null) || (strTel.compareTo( "" ) == 0) )
        {
            strTel = "";
            strErr = strErr + "�d�b�ԍ������͂���Ă��܂���B<br>";
        }
        if ( (strMemo == null) || (strMemo.compareTo( "" ) == 0) )
        {
            strMemo = "";
        }
        else
        {
            if ( CheckString.numCheck( strTel.replaceAll( "-", "" ) ) == false )
            {
                strErr = strErr + "�d�b�ԍ��ɕs��������܂��B<br>";
            }
        }

        // �I�������s���{�����ƁA�X�֔ԍ�����v���邩�H
        if ( strPref.compareTo( "0" ) != 0 && strZipCode.compareTo( "0" ) != 0 )
        {
            DataMasterZip dmZip;
            dmZip = new DataMasterZip();
            dmZip.getData( strZipCode );

            if ( dmZip.getPrefName().compareTo( strPref ) != 0 )
            {
                strErr = strErr + "�X�֔ԍ��������͑I�������s���{��������������܂���B<br>";
            }
        }

        if ( (strCheck == null) || (strCheck.compareTo( "" ) == 0) )
        {
            strErr = strErr + "�l��񗘗p�ړI�ɓ��ӂ���Ă��܂���B";
        }

        // ��M�����Ǘ��ԍ�������ھ���Ͻ����擾�A���Z�߲�Đ������߂�
        ret = dmPresent.getData( Integer.parseInt( paramSeq ) );
        if ( ret != false )
        {
            if ( dmPresent.getLimitFrom() <= Integer.parseInt( DateEdit.getDate( 2 ) ) && dmPresent.getLimitTo() >= Integer.parseInt( DateEdit.getDate( 2 ) ) )
            {
                // �z�e�����擾
                if ( dmPresent.getOfferHotel() < 100000 )
                {
                    ret = dmChain.getData( dmPresent.getOfferHotel() );
                    if ( ret != false )
                        strHotelName = dmChain.getName();
                }
                else
                {
                    ret = dhb.getData( dmPresent.getOfferHotel() );
                    if ( ret != false )
                        strHotelName = dhb.getName();
                }
                ret = dmp.getData( dmPresent.getPointCode() );
                if ( ret != false )
                {
                    nMinusPoint = dmp.getAddPoint();
                }
                else
                    strErr = strErr + "�߲�Ă��擾�ł��܂���ł����B<br>";

                if ( dmPresent.getTitle().compareTo( "" ) != 0 )
                    strTitle = dmPresent.getTitle();
                else
                    strErr = strErr + "�I�������ܕi�����擾�ł��܂���ł����B<br>";
            }
            else
                strErr = strErr + "�L���������؂�Ă��܂��B<br>";

            // �c���̊m�F( �c����0�ȉ��܂��́A�񋟐��������吔���������ꍇ�G���[�Ƃ��� )
            if ( dmPresent.getRemainsNumber() <= 0 || dmPresent.getElectNumber() <= ue.getUserElectBySeq( Integer.parseInt( paramSeq ) ) )
            {
                strErr = strErr + "�I�����ꂽ���i�͎c�����Ȃ��Ȃ�܂����̂ŁA����ȏ㉞��ł��܂���B<br>";
            }
        }
        else
            strErr = strErr + "�I�������ܕi�͑��݂��܂���B<br>";

        if ( (memberFlag != false) && (ubi.getUserInfo().getRegistStatus() == 9) )
        {

            strUserId = ubi.getUserInfo().getUserId();
            strHandleName = ubi.getUserInfo().getHandleName();
            if ( ubi.getUserInfo().getMailAddrMobile().compareTo( "" ) != 0 )
            {
                mailAddr = ubi.getUserInfo().getMailAddrMobile();
            }
            else if ( ubi.getUserInfo().getMailAddr().compareTo( "" ) != 0 )
            {
                mailAddr = ubi.getUserInfo().getMailAddr();
            }

            // �߲�Čv�Z
            nTotal = up.getNowPoint( ubi.getUserInfo().getUserId(), false );
            if ( nTotal < nMinusPoint )
            {
                strErr = strErr + "�߲�Ă�����܂���B<br>";
                ret = false;
            }
            else if ( nTotal == 0 )
            {
                strErr = strErr + "�߲�Ă�����܂���B<br>";
                ret = false;
            }

            // ���łɉ���ς݂̃��[�U�[�͉��傳���Ȃ�
            if ( ue.getUserElectByFirstCome( ubi.getUserInfo().getUserId(), dmPresent.getLimitFrom(), dmPresent.getLimitTo() ) != false )
            {
                if ( ue.getCount() >= MAX_REGIST )
                {
                    strErr = strErr + "���łɊ��Ԓ�" + MAX_REGIST + "�񂲉��傢�������Ă��邽�߁A����ȏ�̂�����͂ł��܂���B<br>";
                }
                // �z�e�����Ƃ̉���f�[�^���擾
                if ( ue.getUserElectByOfferHotel( ubi.getUserInfo().getUserId(), dmPresent.getOfferHotel(), dmPresent.getPrefId(), dmPresent.getLimitFrom(), dmPresent.getLimitTo() ) != false )
                {
                    // 1��ł����債�Ă���
                    if ( ue.getCount() > 0 )
                        ;
                    {
                        // �S�Ẵf�[�^���`�F�b�N����B
                        for( i = 0 ; i < ue.getCount() ; i++ )
                        {
                            boolUserId = false;
                            boolSeq = false;

                            // UserId�Ɖ���f�[�^�̔�r�����O�ɏo�́B
                            Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp �擾�f�[�^��r DB userId=" + ue.getUserElect()[i].getUserId() + "," +
                                    " �p�����[�^ userId=" + ubi.getUserInfo().getUserId() +
                                    ", DB seq=" + ue.getUserElect()[i].getSeq() + "," + " �p�����[�^ seq=" + paramSeq +
                                    ", DB application_count=" + ue.getUserElect()[i].getApplicationCount() );

                            // UserId������f�[�^�Ɣ�r
                            if ( ue.getUserElect()[i].getUserId().compareTo( ubi.getUserInfo().getUserId() ) == 0 )
                            {
                                boolUserId = true;
                            }
                            // �ܕi�̊Ǘ��ԍ����p�����[�^�Ɣ�r����B
                            if ( ue.getUserElect()[i].getSeq() == Integer.parseInt( paramSeq ) )
                            {
                                boolSeq = true;
                            }

                            // UserId�Ɖ���f�[�^�̔�r�����O�ɏo�́B
                            if ( boolUserId != false && boolSeq != false )
                            {
                                Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp boolUserId=" + boolUserId + ", boolSeq=" + boolSeq );
                                strErr = strErr + "������قւ͂��łɂ�����ς݂ł��B����ȏ�̂�����͂ł��܂���B<br>";
                                break;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            ret = false;
            strErr = strErr + "��ھ��ĉ��巬��݂͉߰������ł��B<br>";
        }

        if ( strErr.compareTo( "" ) == 0 )
        {
            due.setUserId( strUserId );
            due.setUserName( strName );
            due.setApplicationCount( ONCE );
            due.setSeq( Integer.parseInt( paramSeq ) );
            due.setZipCode( strZipCode );
            due.setPrefName( strPref );
            due.setAddress1( strAddress1 );
            due.setAddress2( strAddress2 );
            due.setTel1( strTel );
            due.setMemo( strMemo );
            due.setInputDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            due.setInputTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            due.setStatusFlag( 3 );
            ret = due.insertDataOnce();
            Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp �C���T�[�g���ʁF" + ret );
            if ( ret != false )
            {
                ret = up.setPointOfferHotelPresent( strUserId, dmPresent.getPointCode(), dmPresent.getSeq() );
                Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp �|�C���g�폜���ʁF" + ret );
            }

            if ( ret != false )
            {
                // �c���̊m�F
                if ( dmPresent.getRemainsNumber() > 0 && dmPresent.getElectNumber() >= ue.getUserElectBySeq( Integer.parseInt( paramSeq ) ) )
                {
                    Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp �c��=" + dmPresent.getRemainsNumber() );
                    // �c�������炷����
                    dmPresent.setRemainsNumber( dmPresent.getRemainsNumber() - 1 );
                    ret = dmPresent.updateData( dmPresent.getSeq() );
                    // UserId�Ɖ���f�[�^�̔�r�����O�ɏo�́B
                    Logging.info( "[ue.getUserElectByOfferHotel] present_offer_hotel_write.jsp �c�������炷�����F" + ret );

                }

                // ���[���̑��M
                String title_mail = "�y�n�s�z�e�z�v���[���g�����t";
                String encdata = "";
                String text = "";

                try
                {
                    encdata = URLEncoder.encode( "���₢���킹", "Shift_JIS" );
                }
                catch ( UnsupportedEncodingException e )
                {

                }

                if ( strHandleName.compareTo( "" ) != 0 )
                {
                    text = text + strHandleName + " ����" + "\r\n";
                }
                else
                {
                    text = text + strUserId + " ����" + "\r\n";
                }
                text = text + "\r\n";
                text = text + "�n�b�s�[�E�z�e���������p���������܂��Ă��肪�Ƃ��������܂��B" + "\r\n";
                text = text + "�{���[���̓n�b�s�[�E�z�e���̃|�C���g�������ɂ����傢��������\r\n���q�l�ɑ��M���Ă��܂��B" + "\r\n";
                text = text + "\r\n";
                text = text + "���o�^�����������Z���ɏܕi�𔭑��������܂��̂ŁA���΂炭���҂����������܂��B" + "\r\n";
                text = text + "\r\n";
                text = text + "�y�z�e�����z\r\n";
                text = text + strHotelName + "\r\n";
                text = text + "\r\n";
                if ( dmPresent.getMemo().compareTo( "" ) != 0 )
                {
                    text = text + "�y���l�z\r\n";
                    text = text + dmPresent.getMemo() + "\r\n";
                    text = text + "\r\n";
                }

                text = text + "�y����ܕi�z\r\n";
                text = text + strTitle + "�i" + dmp.getAddPoint() + "pt�j" + "\r\n";
                text = text + "\r\n";
                text = text + "�y�ܕi������z" + "\r\n";
                text = text + strName + "�l" + "\r\n";
                text = text + "�� " + strZipCode1 + "-" + strZipCode2 + "\r\n";
                text = text + strPref + strAddress1 + strAddress2 + "\r\n";
                text = text + "TEL " + strTel + "\r\n";
                text = text + "\r\n";
                /* �ǉ��_�� */
                text = text + "�������`�P�b�g�̑��t�ɂ��܂��Ă͉��傩�甭���܂�2" + strString + "4�T�Ԓ��x�̂����Ԃ�\r\n";
                text = text + "�����Ă���܂��B\r\n";
                text = text + "�������`�P�b�g�͕��Ёu������ЃA�����b�N�X�v�̎Ж���������ꂽ���n�^�C�v\r\n";
                text = text + "�̕����ɂĔ��������Ă��������܂��B\r\n";
                text = text + "\r\n";
                /* �ǉ��_�� */
                text = text + "������\r\n";
                text = text + "���{���[���ɂ��S������̂Ȃ��ꍇ�́A�{���[���̔j�������肢�������܂��B" + "\r\n";
                text = text + "\r\n";
                text = text + "���₢���킹" + "\r\n";
                text = text + "mailto:info@happyhotel.jp?subject=" + encdata + "\r\n";
                text = text + "\r\n";
                text = text + "�n�b�s�[�E�z�e����USEN-NEXT�O���[�v�̊�����ЃA�����b�N�X���^�c����" + "\r\n";
                text = text + "���W���[�z�e�������T�C�g�ł��B" + "\r\n";
                text = text + "\r\n";
                text = text + "�n�b�s�[�E�z�e��URL" + "\r\n";
                text = text + Url.getUrl() + "\r\n";

                // ���[�����M���s��
                SendMail.send( "info@happyhotel.jp", mailAddr, title_mail, text );
            }
        }
        Logging.info( "�G���[���b�Z�[�W�F" + strErr );
        request.setAttribute( "err", strErr );

        // �f�o�b�O�����ǂ���
        if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
        {
            strCarrierUrl = "/_debug_" + strCarrierUrl;
        }
        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + strCarrierUrl + "?" + paramUidLink );
        Logging.info( "�擾����URL�F" + request.getContextPath() + strCarrierUrl + "?" + paramUidLink );
        Logging.info( "[ue.registUserElect] �r�����b�N����" );
        try
        {
            requestDispatcher.forward( request, response );
        }
        catch ( Exception e )
        {
            Logging.error( "[UserElectRegistration() ] Exception=" + e.toString() );
        }

    }
} // End Of Class

