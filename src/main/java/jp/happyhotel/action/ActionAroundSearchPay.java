package jp.happyhotel.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.ConvertGeodesic;
import jp.happyhotel.common.CreateUrl;
import jp.happyhotel.common.HttpConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.QueryStrChecker;
import jp.happyhotel.common.ReadXml;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelDistance;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMapPoint;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelEmpty;
import jp.happyhotel.search.SearchHotelFreeword_M2;
import jp.happyhotel.user.UserMap;

/**
 * 
 * ���ӌ����N���X�i�L����������j
 * 
 * @author S.Tashiro
 * @version 1.0 2010/02/22
 */

public class ActionAroundSearchPay extends BaseAction
{
    static int                pageRecords       = Constants.pageLimitRecordMobile;
    static int                maxRecords        = Constants.maxRecordsMobile;
    public static final int   dispFormat        = 1;

    private RequestDispatcher requestDispatcher = null;
    private DataLoginInfo_M2  dataLoginInfo_M2  = null;
    private String            lat               = "";
    private String            lon               = "";
    private int               hotelAllCount     = 0;
    private int               hotelCount        = 0;
    private UserMap           um                = null;

    /**
     * �C�ӂ̏ꏊ�t�߂Ńz�e��������
     * 
     * @param request �N���C�A���g����T�[�o�ւ̃��N�G�X�g
     * @param response �T�[�o����N���C�A���g�ւ̃��X�|���X
     * 
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean memberFlag;
        boolean paymemberFlag;
        boolean paymemberTempFlag;
        boolean ret;
        int i;
        int registStatus;
        int delFlag;
        int carrierFlag;
        int pageNum;
        // ���S�̍��W
        int coordinateLat;
        int coordinateLon;
        // �E��̍��W
        int coordinateUrLat;
        int coordinateUrLon;
        // �����̍��W
        int coordinateDlLat;
        int coordinateDlLon;
        int hotelLatNum;
        int hotelLonNum;
        int[] hotelIdList;
        int[] distance;
        int[] arrHotelIdList = null;
        String paramId; // �z�e���𒆐S�ɒn�}�摜�\�����s���ۂɕK�v�ȃp�����[�^
        String paramScale; // �k�ڂ̊Ǘ����s���p�����[�^
        String url;
        String strPosIcon;
        String paramPos; // �\�t�g�o���N��GPS�擾�����Ƃ��ɕԂ��ʒu���̃p�����[�^
        String paramLat; // �ʒu�������������p�����[�^
        String paramLon; // �ʒu�������������p�����[�^
        String paramAddr; // �Z���Ō�������ꍇ�ɕK�v�ȃp�����[�^
        String paramPage; // �y�[�W���Ǘ�����p�����[�^
        String pageLinks;
        String pageHeader;
        String paramCenter;
        String queryString;
        String currentPageRecords;
        String paramUidLink = null;
        String paramGqs; // ���̃p�����[�^���Ȃ�������Z����yahooAPI����擾����
        String paramEmpty; // 1:�󎺂������A2:�ڍׂ̃z�e��������
        String paramAcRead;
        String paramAndWord;
        String paramAndWordEnc;
        String paramGo; // GPS�擾�̃G���[���f�Ɏg�p����p�����[�^
        String paramKind; // ��ނ̔��ʂ��s��(1:�w���ӁA2:IC���ӁA3:�z�e������)
        String paramRouteId; // route_id���擾����i�w�EIC�̎��ӌ������s���ꍇ�Ɏg�p����j
        String paramHotel; // hotel_id���擾����i�z�e���̎��ӌ������s���ꍇ�Ɏg�p����j
        String termNo; // �����̒[���ԍ����擾�iuser_id����Ɏg�p����j
        String aroundUrl = "";

        CreateUrl cu;
        ConvertGeodesic cg;
        DataMapPoint dmPoint;
        DataHotelDistance[] dhd;
        HttpConnection con;
        SearchHotelEmpty she;
        SearchHotelFreeword_M2 searchHotelFreeWord;
        SearchHotelCommon searchHotelCommon;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataLoginInfo_M2 dataLoginInfo_M2 = null;
        DataHotelBasic dhb;
        SearchHotelDao_M2 searchHotelDao = null;
        ReadXml readXml;
        AuAuthCheck auCheck;

        memberFlag = false;
        paymemberFlag = false;
        paymemberTempFlag = false;
        url = "";
        pageLinks = "";
        pageHeader = null;
        queryString = "";
        ret = false;
        carrierFlag = 0;
        hotelCount = 0;
        hotelAllCount = 0;
        paramCenter = "";
        cu = new CreateUrl();
        con = new HttpConnection();
        she = new SearchHotelEmpty();
        dhd = null;
        coordinateLat = 0;
        coordinateLon = 0;
        coordinateUrLat = 0;
        coordinateUrLon = 0;
        coordinateDlLat = 0;
        coordinateDlLon = 0;
        hotelLatNum = 0;
        hotelLonNum = 0;
        hotelIdList = null;
        distance = null;
        termNo = "";

        carrierFlag = UserAgent.getUserAgentType( request );
        dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
        paramUidLink = (String)request.getAttribute( "UID-LINK" );
        paramAcRead = request.getParameter( "acread" );

        try
        {
            // �����Ђ̒n�}�R�[�h���p�����[�^�Ŏ󂯂��ꍇ�̓[�������̃R�[�h�ɕϊ������_�C���N�g�i#15040�Ή��j
            QueryStrChecker qsc = new QueryStrChecker();
            int redirection_flag = 0; // 1�F���_�C���N�g�A0�F�X���[�A-1�F�G���[�i�g�b�v�y�[�W�փ��_�C���N�g�j
            String query_str = request.getQueryString();
            if ( query_str == null )
            {
                query_str = "";
            }
            query_str = query_str.replaceAll( "&amp;", "&" ); // "&"���u������Ă���ꍇ������̂Ō��ɖ߂��i���_�C���N�g���邽�߁j
            redirection_flag = qsc.checkIncludingShobunshaCode( query_str );
            if ( redirection_flag == 1 )
            {
                response.sendRedirect( request.getRequestURI() + "?" + qsc.getConvertedQueryStr() );
                return;
            }
            else if ( redirection_flag == -1 )
            {
                response.sendRedirect( Url.getUrl() );
                return;
            }

            // ���[�U�[���̎擾
            if ( dataLoginInfo_M2 != null )
            {
                memberFlag = dataLoginInfo_M2.isMemberFlag();
                paymemberFlag = dataLoginInfo_M2.isPaymemberFlag();
                paymemberTempFlag = dataLoginInfo_M2.isPaymemberTempFlag();
                registStatus = dataLoginInfo_M2.getRegistStatus();
                delFlag = dataLoginInfo_M2.getDelFlag();
                carrierFlag = dataLoginInfo_M2.getCarrierFlag();
                this.um = new UserMap();
                this.um.getData( dataLoginInfo_M2.getUserId() );
            }
            else
            {
                // �L�����A�t���O�ƒ[���ԍ����擾����
                if ( carrierFlag == UserAgent.USERAGENT_AU )
                {
                    termNo = request.getHeader( "x-up-subno" );
                }
                else if ( carrierFlag == UserAgent.USERAGENT_VODAFONE )
                {
                    termNo = request.getHeader( "x-jphone-uid" );
                    termNo = termNo.substring( 1 );
                }
                else if ( carrierFlag == UserAgent.USERAGENT_DOCOMO )
                {
                    termNo = request.getParameter( "uid" );
                }

                memberFlag = false;
                paymemberFlag = false;
                paymemberTempFlag = false;
                registStatus = 0;
                delFlag = 1;
                this.um = new UserMap();
                if ( termNo.equals( "" ) == false )
                {
                    this.um.getData( termNo );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionAroundPay dataLoginInfo] Exception:" + e.toString() );
        }

        paramAndWord = request.getParameter( "andword" );
        paramAndWordEnc = "";
        paramLat = request.getParameter( "lat" );
        paramLon = request.getParameter( "lon" );
        paramPos = request.getParameter( "pos" );
        paramAddr = request.getParameter( "addr" );
        paramPage = request.getParameter( "page" );
        paramScale = request.getParameter( "scale" );
        paramEmpty = request.getParameter( "empty" );
        paramGqs = request.getParameter( "gps" );
        paramId = request.getParameter( "hotel_id" );
        paramGo = request.getParameter( "go" );
        paramKind = request.getParameter( "kind" );
        paramRouteId = request.getParameter( "route_id" );
        paramHotel = request.getParameter( "hotel" );

        if ( paramLat == null )
        {
            paramLat = "";
        }
        if ( paramLon == null )
        {
            paramLon = "";
        }
        if ( paramPos == null )
        {
            paramPos = "";
        }
        if ( paramAddr == null )
        {
            paramAddr = "";
        }
        if ( (paramPage == null) || (paramPage.compareTo( "" ) == 0) || (CheckString.numCheck( paramPage ) == false) )
        {
            paramPage = "0";
        }
        if ( (paramScale == null) || (paramScale.compareTo( "" ) == 0) || (CheckString.numCheck( paramScale ) == false) )
        {
            paramScale = "0";
        }
        if ( (paramEmpty == null) || (paramEmpty.compareTo( "" ) == 0) || (CheckString.numCheck( paramEmpty ) == false) )
        {
            paramEmpty = "0";
        }
        if ( paramGqs == null )
        {
            paramGqs = "";
        }
        if ( (paramId == null) || (paramId.compareTo( "" ) == 0) || (CheckString.numCheck( paramId ) == false) )
        {
            paramId = "0";
        }
        if ( (paramGo == null) || (paramGo.compareTo( "" ) == 0) || (CheckString.numCheck( paramGo ) == false) )
        {
            paramGo = "0";
        }
        if ( paramAndWord == null )
        {
            paramAndWord = "";
        }
        else
        {
            try
            {
                paramAndWord = new String( paramAndWord.getBytes( "8859_1" ), "Windows-31J" );
                // au�ł̕��������΍�
                paramAndWordEnc = URLEncoder.encode( paramAndWord, "Shift_JIS" );
            }
            catch ( Exception e )
            {
                paramAndWord = "";
            }
        }

        if ( (paramKind == null) || (paramKind.compareTo( "" ) == 0) || (CheckString.numCheck( paramKind ) == false) )
        {
            paramKind = "0";
        }
        if ( paramRouteId == null )
        {
            paramRouteId = "";
        }
        if ( (paramHotel == null) || (paramHotel.compareTo( "" ) == 0) || (CheckString.numCheck( paramHotel ) == false) )
        {
            paramHotel = "0";
        }

        pageNum = Integer.parseInt( paramPage );

        // �L�����A�ɂ���ďo���摜�^�C�v��ύX
        if ( (carrierFlag == DataMasterUseragent.CARRIER_AU) || (carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK) )
        {
            cu.setImgType( "PNG" );
        }
        else if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
        {
            cu.setImgType( "GIF" );
        }

        // �z�e��ID���������炻���𒆐S�ɕ\������
        if ( Integer.parseInt( paramId ) > 0 )
        {
            queryString = "searchAroundPay.act?gps=1&scale=" + paramScale + "&lat=" + paramLat + "&lon=" + paramLon
                    + "&empty=" + paramEmpty;
            if ( paramAndWord.compareTo( "" ) != 0 )
            {
                queryString += "&andword=" + paramAndWordEnc;
            }
            if ( this.um != null )
            {
                ret = true;
            }

        }
        // �ʒu�����擾����f�[�^������ꍇ�iGPS�̌��ʂ��Ԃ��Ă����ꍇ�j
        else if ( (paramLat.compareTo( "" ) != 0 && paramLon.compareTo( "" ) != 0) || paramPos.compareTo( "" ) != 0 )
        {
            // ����擾�������̂������NURL�ɐݒ�
            queryString = "searchAroundPay.act?gps=1&scale=" + paramScale + "&lat=" + paramLat + "&lon=" + paramLon
                    + "&empty=" + paramEmpty;
            if ( paramAndWord.compareTo( "" ) != 0 )
            {
                queryString += "&andword=" + paramAndWordEnc;
            }
            try
            {
                if ( paramGqs.compareTo( "" ) == 0 )
                {
                    // �\�t�g�o���N��pos����ʒu���𕪊�����
                    if ( carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK )
                    {
                        if ( paramPos.compareTo( "" ) != 0 )
                        {
                            i = paramPos.indexOf( "E" );
                            if ( i != -1 )
                            {
                                paramLat = paramPos.substring( 1, i );
                                paramLon = paramPos.substring( i + 1, paramPos.length() );
                            }
                            else
                            {
                                i = paramPos.indexOf( "W" );
                                if ( i != -1 )
                                {
                                    paramLat = paramPos.substring( 1, i );
                                    paramLon = paramPos.substring( i + 1, paramPos.length() );
                                }
                                else
                                {
                                    paramLat = "";
                                    paramLon = "";
                                }
                            }
                        }

                    }
                    else if ( carrierFlag == DataMasterUseragent.CARRIER_ETC )
                    {
                        try
                        {
                            response.sendRedirect( "/index.jsp" );
                        }
                        catch ( Exception e )
                        {
                            Logging.error( "[ActionAroundPay sendRedirect] Exception" + e.toString() );
                        }
                        return;
                    }
                    // GPS�̍��W�����낢��ȑ��n�n�ɕϊ����A�n�}�\����
                    cg = new ConvertGeodesic();
                    cg.convertDms2Degree( paramLat, paramLon );
                    paramLat = Double.toString( cg.getLatWGS() );
                    paramLon = Double.toString( cg.getLonWGS() );
                    queryString = "searchAroundPay.act?gps=1&scale=" + paramScale + "&lat=" + paramLat + "&lon="
                            + paramLon + "&empty=" + paramEmpty;
                    if ( paramAndWord.compareTo( "" ) != 0 )
                    {
                        queryString += "&andword=" + paramAndWordEnc;
                    }

                    // �ܓx�o�x����Z����o�^����
                    cu = new CreateUrl();
                    cu.setLat( paramLat );
                    cu.setLon( paramLon );
                    readXml = new ReadXml( cu.geoDecode() );
                    // �Z�����擾
                    readXml.getElementAddr();
                    // �Z����o�^����
                    if ( dataLoginInfo_M2 != null )
                    {
                        this.um.registAddress( dataLoginInfo_M2.getUserId(), readXml.getAddress() );
                    }
                    else
                    {
                        this.um.registAddress( termNo, readXml.getAddress() );
                    }
                    cu = null;
                    cu = new CreateUrl();

                }

                // �g�ь����ɉ摜�̎���������L���ɂ���
                cu.setWm( true );
                cu.setHm( true );

                cu.setScale( this.getScale( Integer.parseInt( paramScale ) ) );
                cu.setLat( paramLat );
                cu.setLon( paramLon );
                cu.setPindefault( paramLat + "," + paramLon );
                aroundUrl = cu.getMapURL();
                cu = null;
                cu = new CreateUrl();
                ret = con.urlConnection( request, response, aroundUrl );
            }
            catch ( Exception e )
            {
                Logging.info( "[ActionAroundPay  test] Exception:" + e.toString() );
            }
        }
        // �Z�������͂���Ă����ꍇ
        else if ( paramAddr.compareTo( "" ) != 0 )
        {
            queryString = "searchAroundPay.act?gps=1&empty=" + paramEmpty;
            if ( paramAndWord.compareTo( "" ) != 0 )
            {
                queryString += "&andword=" + paramAndWordEnc;
            }
            try
            {
                cu.setOutdatum( "WGS84" );
                // �g�ь����ɉ摜�̎���������L���ɂ���
                cu.setWm( true );
                cu.setHm( true );
                cu.setScale( this.getScale( Integer.parseInt( paramScale ) ) );
                cu.setPos( "Ipin" );

                try
                {
                    paramAddr = new String( paramAddr.getBytes( "8859_1" ), "Shift_JIS" );

                }
                catch ( UnsupportedEncodingException e )
                {
                    Logging.info( "[ActionAroundPay] Exception:" + e.toString() );
                }
                // �Z�����Z�b�g����
                cu = new CreateUrl();
                cu.setAddress( paramAddr );
                readXml = new ReadXml( cu.geoDecode() );
                readXml.getElementAddr();
                this.replaceGeodesic( readXml.getCoordinate() );
                paramLat = this.lat;
                paramLon = this.lon;

                // �n�}�\��
                cu = null;
                cu = new CreateUrl();
                cu.setPindefault( paramLat + "," + paramLon );
                cu.setLat( paramLat );
                cu.setLon( paramLon );
                cu.setScale( this.getScale( Integer.parseInt( paramScale ) ) );
                aroundUrl = cu.getMapURL();
                ret = false;

                ret = con.urlConnection( request, response, aroundUrl );
            }
            catch ( Exception e )
            {
                Logging.info( "[ActionAroundPay] Exception:" + e.toString() );
            }
        }
        // �w�EIC���ӂ���������ꍇ
        else if ( paramRouteId.compareTo( "" ) != 0 )
        {
            dmPoint = new DataMapPoint();
            ret = dmPoint.getData( paramRouteId );

            queryString = "searchAroundPay.act?gps=1&empty=" + paramEmpty;
            if ( ret != false )
            {
                // ��f�[�^����Ȃ����Ƃ��m�F����
                if ( dmPoint.getId().compareTo( "" ) != 0 )
                {
                    cg = new ConvertGeodesic();
                    cg.TokyoNum2Tokyo( dmPoint.getLat(), dmPoint.getLon() );
                    cg.Tokyo2Wgs( cg.getLatTOKYO(), cg.getLonTOKYO() );
                    paramLat = Double.toString( cg.getLatWGS() );
                    paramLon = Double.toString( cg.getLonWGS() );
                    Logging.info( "[ActionAroundSearchPay  test] " + paramLat + "," + paramLon );
                }
                else
                {
                    ret = false;
                }
            }

            // �g�ь����ɉ摜�̎���������L���ɂ���
            cu.setWm( true );
            cu.setHm( true );
            cu.setScale( this.getScale( Integer.parseInt( paramScale ) ) );
            // cu.setPindefault( paramLat + "," + paramLon );
            cu.setLat( paramLat );
            cu.setLon( paramLon );
            aroundUrl = cu.getMapURL();
            cu = new CreateUrl();

            if ( ret != false )
            {
                ret = con.urlConnection( request, response, aroundUrl );
            }

        }
        // �z�e�����ӂ���������ꍇ
        else if ( Integer.parseInt( paramHotel ) > 0 )
        {
            dhb = new DataHotelBasic();
            ret = dhb.getData( Integer.parseInt( paramHotel ) );

            queryString = "searchAroundPay.act?gps=1&empty=" + paramEmpty;
            if ( ret != false )
            {
                if ( dhb.getHotelLat().compareTo( "" ) != 0 && dhb.getHotelLon().compareTo( "" ) != 0 )
                {
                    paramLat = dhb.getHotelLat();
                    paramLon = dhb.getHotelLon();
                    hotelLatNum = dhb.getHotelLatNum();
                    hotelLonNum = dhb.getHotelLonNum();
                }
                else
                {
                    ret = false;
                }
            }

            // �n�}��\�����邽�߂�URL�ɃZ�b�g���Ă���
            cu.setWm( true );
            cu.setHm( true );
            cu.setScale( this.getScale( Integer.parseInt( paramScale ) ) );
            cu.setPindefault( paramLat + "," + paramLon );
            cu.setLat( paramLat );
            cu.setLon( paramLon );
            aroundUrl = cu.getMapURL();
            if ( ret != false )
            {
                ret = con.urlConnection( request, response, aroundUrl );
            }
        }
        else
        {
            try
            {
                if ( dataLoginInfo_M2 != null )
                {
                    request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                }
                requestDispatcher = request.getRequestDispatcher( "search_around_pay_error.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionAroundPay] Exception:" + e.toString() );
            }
        }

        // HTTP�ʐM�ɐ��������狤�ʍ��ڂ�
        if ( ret != false )
        {
            try
            {
                // �z�e��ID���Ȃ��ꍇ�̂ݓo�^����i�z�e��ID������ꍇ�́A�n�}�\���ς݂̂��߁j
                if ( Integer.parseInt( paramId ) == 0 )
                {
                    if ( dataLoginInfo_M2 != null )
                    {
                        ret = this.um.registUserMap( dataLoginInfo_M2.getUserId(), con );
                    }
                    else
                    {
                        ret = this.um.registUserMap( termNo, con );
                    }
                }

                cg = new ConvertGeodesic();

                // �����̍��W��lat,lon�ɕ�����
                this.replaceGeodesic( this.um.getUserMapInfo().getCoordinateDL() );
                cg.convertDegree( this.lat, this.lon );
                coordinateDlLat = cg.getLatTOKYONum();
                coordinateDlLon = cg.getLonTOKYONum();

                // �E��̍��W��lat,lon�ɕ�����
                this.replaceGeodesic( this.um.getUserMapInfo().getCoordinateUR() );
                cg.convertDegree( this.lat, this.lon );
                coordinateUrLat = cg.getLatTOKYONum();
                coordinateUrLon = cg.getLonTOKYONum();

                // ���S�̍��W��lat,lon�ɕ�����
                this.replaceGeodesic( this.um.getUserMapInfo().getCoordinate() );
                cg.convertDegree( this.lat, this.lon );
                // ���{���n�n�֕ϊ�
                coordinateLat = cg.getLatTOKYONum();
                coordinateLon = cg.getLonTOKYONum();

                Logging.info( "lat:" + hotelLatNum + "lon:" + hotelLonNum );
                if ( hotelLatNum > 0 && hotelLonNum > 0 )
                {
                    coordinateLat = hotelLatNum;
                    coordinateLon = hotelLonNum;
                }

                // �z�e�����Ӓn�}�̏ꍇ��Lat�ALon��ǉ����Ȃ�
                if ( paramAddr.compareTo( "" ) != 0 && Integer.parseInt( paramHotel ) == 0 )
                {
                    queryString += "&lat=" + paramLat + "&lon=" + paramLon;
                    if ( paramAndWord.compareTo( "" ) != 0 )
                    {
                        queryString += "&andword=" + paramAndWordEnc;

                    }
                }

                if ( paramGqs.compareTo( "" ) == 0 )
                {
                    // �ܓx�o�x����Z����o�^����
                    cu = new CreateUrl();
                    cu.setLat( paramLat );
                    cu.setLon( paramLon );
                    readXml = new ReadXml( cu.geoDecode() );
                    // �Z�����擾
                    readXml.getElementAddr();
                    // �Z����o�^����
                    if ( dataLoginInfo_M2 != null )
                    {
                        this.um.registAddress( dataLoginInfo_M2.getUserId(), readXml.getAddress() );
                    }
                    else
                    {
                        this.um.registAddress( termNo, readXml.getAddress() );
                    }
                }

                // �w��͈͓�����z�e����T��
                ret = she.getSearchHotel( coordinateDlLat, coordinateDlLon, coordinateUrLat, coordinateUrLon,
                        coordinateLat, coordinateLon, Integer.parseInt( paramEmpty ) );
                if ( ret != false )
                {
                    // �z�e�����A�z�e��ID���X�g�A�ܓx�o�x���擾
                    hotelAllCount = she.getHotelAllCount();
                    hotelIdList = she.getHotelId();
                    distance = she.getDistance();
                    strPosIcon = "WGS84,";

                    // �i�����������ꍇ�̏���
                    if ( paramAndWord != null && (paramAndWord.trim().length() > 0) )
                    {
                        // This will set the current ids
                        searchHotelCommon = new SearchHotelCommon();
                        searchHotelCommon.setEquipHotelList( hotelIdList );
                        Logging.info( "GPS����ID���X�g" + hotelIdList.length );

                        try
                        {
                            searchHotelFreeWord = new SearchHotelFreeword_M2();
                            // �t���[���[�h�����p�̃z�e��ID���X�g
                            arrHotelIdList = searchHotelFreeWord.getSearchIdList( paramAndWord );
                            Logging.info( "�t���[���[�h����ID���X�g" + arrHotelIdList.length );

                            // �t���[���[�h�����p�̃z�e��ID���X�g���Z�b�g
                            searchHotelCommon.setResultHotelList( arrHotelIdList );

                            // GPS�̃z�e��ID���X�g�ƃt���[���[�h�����p�̃z�e��ID���X�g���}�[�W����
                            hotelIdList = searchHotelCommon.getMargeHotel( pageRecords, pageNum );
                            Logging.info( "�}�[�W����ID���X�g" + hotelIdList.length );

                            she = null;
                            she = new SearchHotelEmpty();
                            ret = she.getSearchHotel( hotelIdList, coordinateLat, coordinateLon,
                                    Integer.parseInt( paramEmpty ) );
                            if ( ret != false )
                            {
                                // �z�e�����A�z�e��ID���X�g�A�ܓx�o�x���擾
                                hotelAllCount = she.getHotelAllCount();
                                hotelIdList = she.getHotelId();
                                distance = she.getDistance();
                            }

                            pageHeader = "�u" + paramAndWord + "�v�ōi���݌������܂���";
                        }
                        catch ( UnsupportedEncodingException e )
                        {
                            Logging.error( "[ActionAroundSearcPay.execute() : AndWord = " + paramAndWord
                                    + " ] Exception=" + e.toString() );
                            throw e;
                        }
                    }

                    // �\������y�[�W��lat.lon���擾
                    dhd = this.getHotelList( she.getHotelDistance(), pageRecords, pageNum );

                    // �������ʂŕ\��������z�e���̃f�[�^�擾����
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );
                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();
                    currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords, hotelAllCount,
                            hotelCount, dispFormat );
                    dataSearchResult = new DataSearchResult_M2();
                    String icon = "";

                    if ( dhd != null )
                    {
                        String lastIcon = "";
                        String pin = "";
                        // �i�荞��DataHotelDistance����\��������pin�����
                        for( i = dhd.length - 1 ; i >= 0 ; i-- )
                        {
                            pin += "&pin" + (i + 1) + "=" + dhd[i].getHotelLat() + "," + dhd[i].getHotelLon();
                            // �L������ȊO�͕s���Ƃ���
                            if ( paymemberFlag == false )
                            {
                                icon += "II" + (3 * i + 14) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                        + dhd[i].getHotelLon();
                                strPosIcon = "II" + (3 * i + 14) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                        + dhd[i].getHotelLon();
                            }
                            else
                            {
                                // �s��
                                if ( arrDataSearchHotel[i].getEmptyStatus() == 0 )
                                {
                                    icon += "II" + (3 * i + 14) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                            + dhd[i].getHotelLon();
                                    strPosIcon = "II" + (3 * i + 14) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                            + dhd[i].getHotelLon();
                                    pin += ",,green";
                                }
                                // ��
                                else if ( arrDataSearchHotel[i].getEmptyStatus() == 1 )
                                {
                                    icon += "II" + (3 * i + 12) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                            + dhd[i].getHotelLon();
                                    strPosIcon = "II" + (3 * i + 12) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                            + dhd[i].getHotelLon();
                                    pin += ",,blue";
                                }
                                // ����
                                else if ( arrDataSearchHotel[i].getEmptyStatus() == 2 )
                                {
                                    /*
                                     * icon += "II" + (3 * i + 13) + "G:PWGS84," + dhd[i].getHotelLat() + "," + dhd[i].getHotelLon();
                                     * strPosIcon = "II" + (3 * i + 13) + "G:PWGS84," + dhd[i].getHotelLat() + "," + dhd[i].getHotelLon();
                                     */
                                    // �����ł��s�������Ƃ���
                                    icon += "II" + (3 * i + 14) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                            + dhd[i].getHotelLon();
                                    strPosIcon = "II" + (3 * i + 14) + "G:PWGS84," + dhd[i].getHotelLat() + ","
                                            + dhd[i].getHotelLon();
                                    pin += ",,green";
                                }
                            }

                            // �擾�����z�e��ID�Ɠ����ꍇ�A�z�e���̈ʒu�����擾����
                            if ( dhd[i].getId() == Integer.parseInt( paramId ) )
                            {
                                paramCenter = dhd[i].getHotelLat() + "," + dhd[i].getHotelLon();
                                lastIcon = strPosIcon;
                            }
                            icon += ":";
                        }
                        icon += "II5G:PWGS84," + cg.getLatWGS() + "," + cg.getLonWGS();
                        if ( lastIcon.compareTo( "" ) != 0 )
                        {
                            icon += ":" + lastIcon;
                        }

                        cu.setOutdatum( "WGS84" );
                        // �g�ь����ɉ摜�̎���������L���ɂ���
                        cu.setWm( true );
                        cu.setHm( true );
                        cu.setAddress( "" );
                        // �z�e���̃s�����Z�b�g����
                        cu.setPos( icon );
                        cu.setPin( pin );

                        // �z�e��ID������΁A�z�e���̈ʒu�����Z�b�g����
                        if ( Integer.parseInt( paramId ) == 0 )
                        {
                            cu.setScale( this.getScale( Integer.parseInt( paramScale ) ) );
                            cu.setPindefault( paramLat + "," + paramLon );
                            cu.setLat( paramLat );
                            cu.setLon( paramLon );
                            aroundUrl = cu.getMapURL();
                            ret = con.urlConnection( request, response, aroundUrl );
                            if ( ret != false )
                            {
                                if ( dataLoginInfo_M2 != null )
                                {
                                    ret = this.um.registUserMap( dataLoginInfo_M2.getUserId(), con );
                                }
                                else
                                {
                                    ret = this.um.registUserMap( termNo, con );
                                }

                            }
                        }
                        else
                        {
                            cu.setScale( this.getScale( Integer.parseInt( paramScale ) ) );
                            cu.setC( paramCenter );
                            aroundUrl = cu.getMapURL();
                            ret = con.urlConnection( request, response, aroundUrl );
                            queryString = "searchAroundPay.act?gps=1&scale="
                                    + Integer.toString( this.getScaleTo( Integer.parseInt( this.um.getUserMapInfo()
                                            .getScale() ) ) ) +
                                    "&lat=" + paramLat + "&lon=" + paramLon + "&empty=" + paramEmpty;
                            if ( paramAndWord.compareTo( "" ) != 0 )
                            {
                                queryString += "&andword" + paramAndWordEnc;

                            }
                            if ( ret != false )
                            {
                                if ( dataLoginInfo_M2 != null )
                                {
                                    ret = this.um.registImage( dataLoginInfo_M2.getUserId(), con );
                                }
                                else
                                {
                                    ret = this.um.registImage( termNo, con );
                                }
                            }
                        }

                    }
                    // �߂�l�̃p�����[�^��ǉ�����
                    if ( Integer.parseInt( paramKind ) > 0 )
                    {
                        queryString += "&kind=" + paramKind;
                    }
                    if ( paramRouteId.compareTo( "" ) != 0 )
                    {
                        queryString += "&route_id=" + paramRouteId;
                    }
                    if ( Integer.parseInt( paramHotel ) > 0 )
                    {
                        queryString += "&hotel=" + paramHotel;

                    }

                    if ( hotelAllCount > pageRecords )
                    {
                        pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, pageRecords, hotelAllCount,
                                queryString, paramUidLink );
                        dataSearchResult.setPageLink( pageLinks );
                    }
                    dataSearchResult.setRecordsOnPage( currentPageRecords );
                    dataSearchResult.setHotelCount( hotelCount );
                    dataSearchResult.setHotelAllCount( hotelAllCount );
                    if ( pageHeader != null )
                    {
                        dataSearchResult.setPageHeader( pageHeader );
                    }
                    dataSearchResult.setDataSearchHotel( arrDataSearchHotel );

                    dataSearchResult.setParamParameter1( paramLat );
                    dataSearchResult.setParamParameter2( paramLon );
                    dataSearchResult.setParamParameter3( paramScale );
                    request.setAttribute( "DATAHOTELDISTANCE", dhd );

                }
                if ( this.um != null )
                {
                    request.setAttribute( "USERMAP", this.um );
                }
                request.setAttribute( "HOTELID", paramId );
                request.setAttribute( "SCALE", paramScale );
                if ( dataLoginInfo_M2 != null )
                {
                    request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                }
                request.setAttribute( "DISPMAP", "true" );
                request.setAttribute( "EMPTY", paramEmpty );
                request.setAttribute( "PAGE", paramPage );
                request.setAttribute( "ANDWORD", paramAndWord );
                request.setAttribute( "ROUTEID", paramRouteId );
                request.setAttribute( "HOTEL", paramHotel );
                request.setAttribute( "KIND", paramKind );
                request.setAttribute( "url", aroundUrl );

                if ( dataSearchResult == null )
                {
                    dataSearchResult = new DataSearchResult_M2();
                    dataSearchResult.setParamParameter1( paramLat );
                    dataSearchResult.setParamParameter2( paramLon );
                    dataSearchResult.setParamParameter3( paramScale );
                    dataSearchResult.setPageHeader( pageHeader );
                    dataSearchResult.setHotelCount( hotelCount );
                    dataSearchResult.setHotelAllCount( hotelAllCount );
                }
                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                requestDispatcher = request.getRequestDispatcher( "search_result_around_pay.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionAroundPay HTTPConnection=true]Exception" + e.toString() );
            }
        }
        else if ( paramAddr.compareTo( "" ) != 0 )
        {
            try
            {
                request.setAttribute( "HOTELID", paramId );
                requestDispatcher = request.getRequestDispatcher( "search_result_around_pay.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionAroundPay HTTPConnection=false] Exception:" + e.toString() );
            }
        }
        else
        {
            try
            {
                if ( dataLoginInfo_M2 != null )
                {
                    request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                }
                requestDispatcher = request.getRequestDispatcher( "search_around_pay_error.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionAroundPay] Exception:" + e.toString() );
            }
        }

    }

    /**
     * yahooAPI��dms�\�L����u������ixx/xx/xx.xx �� xx.xx.xx.xx�j
     * 
     * @param coordinate ���W
     */
    private void replaceGeodesic(String coordinate)
    {
        int nIndex;
        nIndex = 0;

        coordinate = coordinate.replaceAll( "WGS84,", "" );
        nIndex = coordinate.indexOf( "," );
        if ( nIndex != -1 )
        {
            this.lat = coordinate.substring( nIndex + 1 );
            this.lat = this.lat.replaceAll( "/", "." );
            this.lon = coordinate.substring( 0, nIndex );
            this.lon = this.lon.replaceAll( "/", "." );

        }
    }

    /**
     * �k�ڂ��擾����
     * 
     * @param scale �X�P�[���B�k��
     * @return ��������(5000�`1000000�܂ł̏k��)
     * 
     */
    private int getScale(int scale)
    {
        int dispScale;

        switch( scale )
        {
            case -4:
                dispScale = 5000;
                break;
            case -3:
                dispScale = 5000;
                break;
            case -2:
                dispScale = 10000;
                break;
            case -1:
                dispScale = 25000;
                break;
            case 0:
                dispScale = 70000;
                break;
            case 1:
                dispScale = 250000;
                break;
            case 2:
                dispScale = 500000;
                break;
            case 3:
                dispScale = 1000000;
                break;
            case 4:
                dispScale = 3000000;
                break;
            default:
                dispScale = 70000;
        }
        return(dispScale);
    }

    /**
     * �k�ڂ��獡�̒i�K���擾����
     * 
     * @param scale �X�P�[���B�k��
     * @return ��������(5000�`1000000�܂ł̏k��)
     * 
     */
    private int getScaleTo(int scale)
    {
        int dispScale;

        switch( scale )
        {
            case 5000:
                dispScale = -3;
                break;
            case 10000:
                dispScale = -2;
                break;
            case 25000:
                dispScale = -1;
                break;
            case 70000:
                dispScale = 0;
                break;
            case 250000:
                dispScale = 1;
                break;
            case 500000:
                dispScale = 2;
                break;
            case 1000000:
                dispScale = 3;
                break;
            case 3000000:
                dispScale = 4;
                break;

            default:
                dispScale = 0;
        }
        return(dispScale);
    }

    /**
     * �z�e�����X�g���擾����
     * 
     * @param DataHotelDistance �z�e�������A�ܓx�o�x�擾�N���X
     * @param countNum �擾���錏��
     * @param pageNum �y�[�W��
     * @return DataHotelDistance �z�e�������A�ܓx�o�x�擾�N���X
     * 
     */
    private DataHotelDistance[] getHotelList(DataHotelDistance[] dhd, int countNum, int pageNum)
    {
        DataHotelDistance[] dhDistance;
        int allCount;
        int count;
        int loop;
        int k;

        dhDistance = null;
        if ( dhd != null )
        {
            allCount = dhd.length;
            if ( allCount > 0 )
            {

                count = 0;
                for( loop = countNum * pageNum ; loop < allCount ; loop++ )
                {
                    count++;
                    if ( count >= countNum && countNum != 0 )
                        break;
                }

                dhDistance = new DataHotelDistance[count];
                int start = countNum * pageNum;
                for( k = 0 ; k < count ; k++ )
                {
                    dhDistance[k] = dhd[start];
                    start++;
                }
            }
        }
        return(dhDistance);
    }

}
