package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.ConvertGeodesic;
import jp.happyhotel.common.ConvertScale;
import jp.happyhotel.common.CreateUrl;
import jp.happyhotel.common.HttpConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.ReadXml;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelDistance;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.others.MapSpot;
import jp.happyhotel.others.MasterSpot;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelEmpty;
import jp.happyhotel.search.SearchHotelFreeword_M2;
import jp.happyhotel.user.UserMap;

/**
 * �����h�}�[�N��������N���X
 * 
 * 
 * @author S.Tashiro
 * @version 1.00 2010/04/30
 */

public class ActionLandmarkSearchMobile extends BaseAction
{

    static int                 pageRecords       = Constants.pageLimitRecordMobile;
    static int                 maxRecords        = Constants.maxRecordsMobile;
    public static final int    dispFormat        = 1;
    public static final int    RECOMMEND         = 1;
    public static final int    NOT_RECOMMEND     = 0;
    public static final int    SPOT_COUNT        = 20;                             // TOP�؁[�W �X�|�b�g�ꗗ�̕\������
    public static final int    WIDTH             = 240;
    public static final int    HEIGHT            = 240;
    public static final String PNG               = "PNG";
    public static final String GIF               = "GIF";

    private RequestDispatcher  requestDispatcher = null;
    private DataLoginInfo_M2   dataLoginInfo_M2  = null;
    private String             lat               = "";
    private String             lon               = "";
    private int                hotelAllCount     = 0;
    private int                hotelCount        = 0;
    private UserMap            um                = null;
    private MapSpot            mapSpot           = null;

    /**
     * �C�ӂ̃����h�}�[�N�t�߂̃z�e��������
     * 
     * @param request �N���C�A���g����T�[�o�ւ̃��N�G�X�g
     * @param response �T�[�o����N���C�A���g�ւ̃��X�|���X
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        // ��`
        boolean ret;
        boolean spotRet;
        boolean boolAdd = false;
        int i;
        int carrierFlag;
        int pageNum;
        // int nDefaultScale;
        // ���S�̍��W
        int coordinateLat;
        int coordinateLon;
        // �E��̍��W
        int coordinateUrLat;
        int coordinateUrLon;
        // �����̍��W
        int coordinateDlLat;
        int coordinateDlLon;
        int[] hotelIdList;
        int[] distance;
        int[] arrHotelIdList = null;
        String paramAcRead;
        String paramUidLink = null;
        String paramLocalId;
        String paramPrefId;
        String paramSpotId;
        String paramSeq;
        String paramScale;
        String sendUrl;
        String paramId;
        String paramEmpty; // 1:�󎺂������A2:�ڍׂ̃z�e��������
        String pageHeader;
        String paramCenter;
        String paramPage; // �y�[�W���Ǘ�����p�����[�^

        String pageLinks;
        String queryString;
        String strLat;
        String strLon;
        String strDispMapUrl;
        String strPosIcon;
        String currentPageRecords = null;
        String strImgType = "";
        String paramCircle;
        String url = null;
        String pin = "";

        AuAuthCheck auCheck;
        CreateUrl cu;
        ConvertGeodesic cg;
        DataHotelBasic dhb;
        DataHotelDistance[] dhd;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        HttpConnection con;
        SearchHotelEmpty she;
        SearchHotelFreeword_M2 searchHotelFreeWord;
        SearchHotelCommon searchHotelCommon;
        SearchHotelDao_M2 searchHotelDao = null;
        ReadXml readXml;
        MasterSpot masterSpot = null;

        ret = false;
        spotRet = false;
        // nDefaultScale = 0;
        sendUrl = "";
        queryString = "";
        strLat = "";
        strLon = "";
        strDispMapUrl = "";
        pageLinks = "";
        paramCenter = "";
        pageHeader = null;
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
        hotelIdList = null;
        distance = null;

        carrierFlag = UserAgent.getUserAgentType( request );
        dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
        paramUidLink = (String)request.getAttribute( "UID-LINK" );
        paramAcRead = request.getParameter( "acread" );

        paramLocalId = request.getParameter( "local_id" );
        paramPrefId = request.getParameter( "pref_id" );
        paramSpotId = request.getParameter( "spot_id" );
        paramSeq = request.getParameter( "seq" );
        paramScale = request.getParameter( "scale" );
        paramId = request.getParameter( "hotel_id" );
        paramEmpty = request.getParameter( "empty" );
        paramPage = request.getParameter( "page" );
        paramCircle = request.getParameter( "circle" );

        if ( (paramLocalId == null) || (paramLocalId.compareTo( "" ) == 0)
                || (CheckString.numCheck( paramLocalId ) == false) )
        {
            paramLocalId = "0";
        }
        if ( (paramPrefId == null) || (paramPrefId.compareTo( "" ) == 0) || (CheckString.numCheck( paramPrefId ) == false) )
        {
            paramPrefId = "0";
        }
        if ( (paramSpotId == null) || (paramSpotId.compareTo( "" ) == 0) || (CheckString.numCheck( paramSpotId ) == false) )
        {
            paramSpotId = "0";
        }
        if ( (paramSeq == null) || (paramSeq.compareTo( "" ) == 0) || (CheckString.numCheck( paramSeq ) == false) )
        {
            paramSeq = "0";
        }
        if ( (paramScale == null) || (paramScale.compareTo( "" ) == 0) || (CheckString.numCheck( paramScale ) == false) )
        {
            paramScale = "0";
        }
        if ( (paramId == null) || (paramId.compareTo( "" ) == 0) || (CheckString.numCheck( paramId ) == false) )
        {
            paramId = "0";
        }
        if ( (paramPage == null) || (paramPage.compareTo( "" ) == 0) || (CheckString.numCheck( paramPage ) == false) )
        {
            paramPage = "0";
        }
        if ( (paramEmpty == null) || (paramEmpty.compareTo( "" ) == 0) || (CheckString.numCheck( paramEmpty ) == false) )
        {
            paramEmpty = "0";
        }
        if ( (paramCircle == null) || (paramCircle.compareTo( "" ) == 0) )
        {
            paramCircle = "";
        }

        // �L�����A�ŕ\������摜��ς���
        if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
        {
            strImgType = GIF;
        }
        else
        {
            strImgType = PNG;
        }

        // �������ʃy�[�W�����L������`�F�b�N���s���B
        if ( Integer.parseInt( paramSeq ) > 0 )
        {
            // au��������A�N�Z�X�`�P�b�g���`�F�b�N����
            if ( (paramAcRead == null) && (carrierFlag == DataMasterUseragent.CARRIER_AU) )
            {
                try
                {
                    auCheck = new AuAuthCheck();
                    ret = auCheck.authCheckForClass( request, "tokushu/transfer_landmark.jsp?" + paramUidLink );
                    // �A�N�Z�X�`�P�b�g�m�F�̌��� false�������烊�_�C���N�g
                    if ( ret == false )
                    {
                        response.sendRedirect( auCheck.getResultData() );
                        return;
                    }
                    // �A�N�Z�X�`�P�b�g�m�F�̌��� true������������擾
                    else
                    {
                        // DataLoginInfo_M2���擾����
                        if ( auCheck.getDataLoginInfo() != null )
                        {
                            dataLoginInfo_M2 = auCheck.getDataLoginInfo();
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[ActionLandMarkSearchMobile AuAuthCheck] Exception:" + e.toString() );
                }
            }

            try
            {
                // ���[�U�[���̎擾
                if ( dataLoginInfo_M2 != null )
                {
                    // �L������o�^�r���̃��[�U�[�͗L������o�^�y�[�W��
                    if ( dataLoginInfo_M2.isMemberFlag() == false && dataLoginInfo_M2.isPaymemberTempFlag() != false )
                    {
                        response.sendRedirect( "../../free/mymenu/paymemberRegist.act?" + paramUidLink );
                        return;
                    }
                    // �L������o�^���s���Ă��Ȃ����[�U�[�͋󖞌����Љ�y�[�W��
                    else if ( dataLoginInfo_M2.isPaymemberFlag() == false
                            && dataLoginInfo_M2.isPaymemberTempFlag() == false )
                    {
                        response.sendRedirect( "../../tokushu/transfer_landmark.jsp?" + paramUidLink );
                        return;
                    }

                }
                else
                {
                    response.sendRedirect( "../../tokushu/transfer_landmark.jsp?" + paramUidLink );
                    return;
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionLandMarkSearchMobile dataLoginInfo] Exception:" + e.toString() );
            }
        }

        pageNum = Integer.parseInt( paramPage );
        // spot_id���Ȃ��ꍇ��TOP�y�[�W��
        if ( Integer.parseInt( paramSpotId ) == 0 )
        {
            try
            {
                masterSpot = new MasterSpot();
                mapSpot = new MapSpot();
                queryString = "searchLandmarkMobile.act?spot_id=0";

                // hh_master_spot�f�[�^�ꗗ���擾���Z�b�g
                spotRet = masterSpot.getMasterSpot( SPOT_COUNT, pageNum );
                if ( spotRet != false )
                {
                    request.setAttribute( "MASTER_SPOT", masterSpot );
                    currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, SPOT_COUNT,
                            masterSpot.getAllCount(), masterSpot.getCount(), dispFormat );
                    request.setAttribute( "PAGE_RECORDS", currentPageRecords );
                    if ( masterSpot.getAllCount() > SPOT_COUNT )
                    {
                        pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, SPOT_COUNT,
                                masterSpot.getAllCount(), queryString, paramUidLink );
                        request.setAttribute( "PAGE_LINK", pageLinks );
                    }
                }

                // �g�b�v�y�[�W�ŕ\�������郉���h�}�[�N�f�[�^���擾���Z�b�g
                spotRet = mapSpot.getSpotDataByTopDisp();
                if ( spotRet != false )
                {
                    request.setAttribute( "SPOT_DATA", mapSpot );
                }
                requestDispatcher = request.getRequestDispatcher( "index.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionLandMarkSearchMobile sendRedirect:error.jsp] Exception:" + e.toString() );
            }
        }
        // �����y�[�W
        if ( Integer.parseInt( paramSeq ) == 0 )
        {
            mapSpot = new MapSpot();
            queryString = "searchLandmarkMobile.act?spot_id=" + paramSpotId + "&local_id=" + paramLocalId + "&pref_id="
                    + paramPrefId;
            // �����h�}�[�N�I���y�[�W��
            if ( Integer.parseInt( paramPrefId ) > 0 )
            {
                spotRet = mapSpot.getMapSpot( Integer.parseInt( paramSpotId ), Integer.parseInt( paramPrefId ),
                        NOT_RECOMMEND, pageRecords, pageNum );
                sendUrl = "landmark_02.jsp";
            }
            // �s���{���I���y�[�W��
            else if ( Integer.parseInt( paramLocalId ) > 0 )
            {
                boolAdd = mapSpot.getSpotIdListPref( Integer.parseInt( paramSpotId ), Integer.parseInt( paramLocalId ) );
                spotRet = mapSpot.getMapSpot( Integer.parseInt( paramSpotId ), 0, RECOMMEND, pageRecords, pageNum );
                sendUrl = "landmark_01.jsp";
                if ( boolAdd != false )
                {
                    request.setAttribute( "PREF_LIST", mapSpot.getPrefIdList() );
                    request.setAttribute( "SPOT_LIST", mapSpot.getSpotIdList() );
                }
            }
            // �n���I���y�[�W��
            else
            {
                boolAdd = mapSpot.getSpotIdListLocal( Integer.parseInt( paramSpotId ), 0 );
                spotRet = mapSpot.getMapSpot( Integer.parseInt( paramSpotId ), 0, RECOMMEND, pageRecords, pageNum );
                sendUrl = "landmark_index.jsp";
                if ( boolAdd != false )
                {
                    request.setAttribute( "LOCAL_LIST", mapSpot.getLocalIdList() );
                    request.setAttribute( "SPOT_LIST", mapSpot.getSpotIdList() );
                }
            }

            try
            {
                request.setAttribute( "PREF_ID", paramPrefId );
                request.setAttribute( "LOCAL_ID", paramLocalId );
                if ( dataLoginInfo_M2 != null )
                {
                    request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                }
                request.setAttribute( "SPOT", paramSpotId );
                if ( spotRet != false )
                {
                    request.setAttribute( "SPOT_DATA", mapSpot );
                    // �s���{��ID������A�P�y�[�W�̕\�������𒴂����ꍇ�Ƀy�[�W�����N���Z�b�g
                    if ( Integer.parseInt( paramPrefId ) > 0 )
                    {
                        currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords,
                                mapSpot.getAllCount(), mapSpot.getCount(), dispFormat );
                        request.setAttribute( "PAGE_RECORDS", currentPageRecords );
                        if ( mapSpot.getAllCount() > pageRecords )
                        {

                            pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, pageRecords,
                                    mapSpot.getAllCount(), queryString, paramUidLink );
                            request.setAttribute( "PAGE_LINK", pageLinks );
                        }
                    }
                }
                requestDispatcher = request.getRequestDispatcher( sendUrl );
                requestDispatcher.forward( request, response );
                return;
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionLandMarkSearchMobile sendRedirect:" + sendUrl + "] Exception:" + e.toString() );
            }
        }
        // ��������
        else if ( Integer.parseInt( paramSeq ) > 0 )
        {
            mapSpot = new MapSpot();
            spotRet = mapSpot.getMapSpotBySeq( Integer.parseInt( paramSpotId ), Integer.parseInt( paramSeq ) );
            sendUrl = "landmark_result.jsp";
            queryString = "searchLandmarkMobile.act?spot_id=" + paramSpotId + "&seq=" + paramSeq + "&scale="
                    + paramScale + "&empty=" + paramEmpty;

            // �n�}�f�[�^�̎擾�Ǝ��ӂ̃z�e�����擾����
            if ( spotRet != false && mapSpot.getAllCount() > 0 )
            {
                cu = new CreateUrl();
                con = new HttpConnection();

                this.um = new UserMap();
                this.um.getData( dataLoginInfo_M2.getUserId() );

                strLat = mapSpot.getMapSpotInfo()[0].getLat();
                strLon = mapSpot.getMapSpotInfo()[0].getLon();

                // �z�e��ID��0�̏ꍇ�̂�URL�����N�G�X�g����
                if ( Integer.parseInt( paramId ) == 0 )
                {
                    /*
                     * // �n�}��\�����邽�߂�URL�ɃZ�b�g���Ă���
                     * cu.setC("WGS84," + strLat + "," + strLon);
                     * cu.setOutdatum("WGS84");
                     * cu.setPos("II5G:PWGS84," + strLat + "," + strLon);
                     * // �g�ь����ɉ摜�̎���������L���ɂ���
                     * // cu.setWm( true );
                     * // cu.setHm( true );
                     * cu.setHeight(HEIGHT);
                     * cu.setWidth(WIDTH);
                     * cu.setImgType(strImgType);
                     */

                    cu.setLat( strLat );
                    cu.setLon( strLon );
                    cu.setScale( ConvertScale.getScaleFromLevel( Integer.parseInt( paramScale ) ) );
                    // if ( paramCircle.compareTo( "true" ) == 0 )
                    // {
                    cu.setCircle( "R" + (ConvertScale.getScaleFromLevel( Integer.parseInt( paramScale ) ) * 120) / 10000
                            + ":BFFFFFF:W1:PWGS84," + strLat + "," + strLon );
                    // }
                    // strDispMapUrl = cu.getDrawMap();
                    cu.setPindefault( strLat + "," + strLon );
                    strDispMapUrl = cu.getMapURL();
                    if ( spotRet != false )
                    {
                        // HTTP�ʐM�Œn�}�����擾
                        ret = con.urlConnection( request, response, strDispMapUrl );
                        // UserMap�ɓo�^���s��
                        ret = this.um.registUserMap( dataLoginInfo_M2.getUserId(), con );
                    }
                }
                else
                {
                    if ( this.um != null )
                    {
                        ret = true;
                    }
                    else
                    {
                        ret = false;
                    }
                }

                if ( ret != false )
                {
                    try
                    {
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

                        // �w��͈͓�����z�e����T��
                        // �z�e��ID���w�肳��Ă�����hh_user_map����scale���擾����
                        if ( Integer.parseInt( paramId ) > 0 )
                        {
                            // �����DB�Ŏ擾����Lat�ALon���g�p���Ă����iyahooAPI�ŕԂ��Ă���Lat�ALon�������ɂ���Ă��邽�߁j
                            ret = she.getSearchHotel( coordinateDlLat, coordinateDlLon, coordinateUrLat,
                                    coordinateUrLon, Double.parseDouble( strLat ), Double.parseDouble( strLon ),
                                    Integer.parseInt( paramEmpty ), Integer.parseInt( um.getUserMapInfo().getScale() ) );

                            /*
                             * yahooAPI����擾����LatLon���g�p����ꍇ�͂�����
                             * ret = she.getSearchHotel( coordinateDlLat, coordinateDlLon, coordinateUrLat, coordinateUrLon, cg.getLatWGS(), cg.getLonWGS(),
                             * Integer.parseInt( paramEmpty ), Integer.parseInt( um.getUserMapInfo().getScale() ) );
                             */

                        }
                        else
                        {
                            // �����DB�Ŏ擾����Lat�ALon���g�p���Ă����iyahooAPI�ŕԂ��Ă���Lat�ALon�������ɂ���Ă��邽�߁j
                            ret = she.getSearchHotel( coordinateDlLat, coordinateDlLon, coordinateUrLat,
                                    coordinateUrLon, Double.parseDouble( strLat ), Double.parseDouble( strLon ),
                                    Integer.parseInt( paramEmpty ), ConvertScale.getScaleFromLevel( Integer
                                            .parseInt( paramScale ) ) );

                            // yahooAPI����擾����LatLon���g�p����ꍇ�͂�����
                            // ret = she.getSearchHotel( coordinateDlLat, coordinateDlLon, coordinateUrLat, coordinateUrLon, cg.getLatWGS(), cg.getLonWGS(), Integer.parseInt( paramEmpty ), ConvertScale.getScaleFromLevel( Integer.parseInt( paramScale ) ) );
                        }

                        if ( ret != false )
                        {
                            // �z�e�����A�z�e��ID���X�g�A�ܓx�o�x���擾
                            hotelAllCount = she.getHotelAllCount();
                            hotelIdList = she.getHotelId();
                            distance = she.getDistance();
                            strPosIcon = "WGS84,";

                            // �\������y�[�W��lat.lon���擾
                            dhd = this.getHotelList( she.getHotelDistance(), pageRecords, pageNum );

                            // �������ʂŕ\��������z�e���̃f�[�^�擾����
                            searchHotelDao = new SearchHotelDao_M2();
                            searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );
                            arrDataSearchHotel = searchHotelDao.getHotelInfo();
                            hotelCount = searchHotelDao.getCount();
                            hotelAllCount = searchHotelDao.getAllCount();
                            currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords,
                                    hotelAllCount, hotelCount, dispFormat );
                            dataSearchResult = new DataSearchResult_M2();
                            String icon = "";

                            if ( dhd != null )
                            {
                                String lastIcon = "";
                                // �i�荞��DataHotelDistance����\��������pin�����
                                for( i = dhd.length - 1 ; i >= 0 ; i-- )
                                {
                                    pin += "&pin" + (i + 1) + "=" + dhd[i].getHotelLat() + "," + dhd[i].getHotelLon();

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

                                strDispMapUrl = "";
                                cu = null;
                                cu = new CreateUrl();

                                cu.setOutdatum( "WGS84" );
                                // �g�ь����ɉ摜�̎���������L���ɂ���
                                // cu.setWm( true );
                                // cu.setHm( true );
                                cu.setHeight( HEIGHT );
                                cu.setWidth( WIDTH );
                                cu.setImgType( strImgType );
                                cu.setAddress( "" );
                                // �z�e���̃s�����Z�b�g
                                cu.setPos( icon );

                                cu.setPin( pin );

                                // �z�e��ID������΁A�z�e���̈ʒu�����Z�b�g����
                                if ( Integer.parseInt( paramId ) == 0 )
                                {
                                    cu.setScale( ConvertScale.getScaleFromLevel( Integer.parseInt( paramScale ) ) );
                                    // if ( paramCircle.compareTo( "true" ) == 0 )
                                    // {
                                    cu.setCircle( "R"
                                            + (ConvertScale.getScaleFromLevel( Integer.parseInt( paramScale ) ) * 120)
                                            / 10000 + ":BFFFFFF:W1:PWGS84," + strLat + "," + strLon );
                                    // }
                                    // yahooAPI�Ŏ擾����Lat�ALon���g���ꍇ�͂�����
                                    // cu.setC( "WGS84," + cg.getLatWGS() + "," + cg.getLonWGS() );

                                    // DB�Ŏ擾����Lat�ALon���g���ꍇ�͂�����
                                    // cu.setC("WGS84," + strLat + "," + strLon);
                                    cu.setLat( strLat );
                                    cu.setLon( strLon );
                                    cu.setPindefault( strLat + "," + strLon );
                                    strDispMapUrl = cu.getMapURL();
                                    ret = con.urlConnection( request, response, strDispMapUrl );
                                    if ( ret != false )
                                    {
                                        ret = this.um.registImage( dataLoginInfo_M2.getUserId(), con );
                                    }
                                }
                                else
                                {
                                    cu.setScale( ConvertScale.getScaleFromLevel( Integer.parseInt( paramScale ) ) );
                                    cu.setC( paramCenter );
                                    // if ( paramCircle.compareTo( "true" ) == 0 )
                                    // {
                                    // �g��O�̏k�ڂŔ��a��\������
                                    cu.setCircle( "R" + (Integer.parseInt( this.um.getUserMapInfo().getScale() ) * 120)
                                            / 10000 + ":BFFFFFF:W1:PWGS84," + strLat + "," + strLon );
                                    // }

                                    cu.setPindefault( paramCenter );
                                    strDispMapUrl = cu.getMapURL();
                                    ret = con.urlConnection( request, response, strDispMapUrl );

                                    queryString = "searchLandmarkMobile.act?spot_id="
                                            + paramSpotId
                                            + "&seq="
                                            + paramSeq
                                            +
                                            "&scale="
                                            + Integer.toString( ConvertScale.getLevelFromScale( Integer.parseInt( this.um
                                                    .getUserMapInfo().getScale() ) ) ) + "&empty=" + paramEmpty;
                                    if ( ret != false )
                                    {
                                        ret = this.um.registImage( dataLoginInfo_M2.getUserId(), con );
                                    }
                                }
                            }
                        }
                    }
                    catch ( Exception e )
                    {
                        Logging.error( "[ActionLandMarkSearchMobile result] Exception:" + e.toString() );
                    }
                }

                try
                {
                    if ( this.um != null )
                    {
                        request.setAttribute( "USERMAP", this.um );
                    }
                    request.setAttribute( "HOTELID", paramId );
                    request.setAttribute( "SCALE", paramScale );
                    request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                    request.setAttribute( "DISPMAP", "true" );
                    request.setAttribute( "EMPTY", paramEmpty );
                    request.setAttribute( "PAGE", paramPage );
                    request.setAttribute( "url", strDispMapUrl );

                    if ( dhd != null )
                    {
                        request.setAttribute( "DATAHOTELDISTANCE", dhd );
                    }

                    if ( dataSearchResult == null )
                    {
                        dataSearchResult = new DataSearchResult_M2();
                    }
                    if ( hotelAllCount > pageRecords )
                    {
                        pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, pageRecords, hotelAllCount,
                                queryString, paramUidLink );
                        dataSearchResult.setPageLink( pageLinks );
                    }
                    if ( currentPageRecords != null )
                    {
                        dataSearchResult.setRecordsOnPage( currentPageRecords );
                    }
                    if ( arrDataSearchHotel != null )
                    {
                        dataSearchResult.setDataSearchHotel( arrDataSearchHotel );
                    }
                    dataSearchResult.setParamParameter1( strLat );
                    dataSearchResult.setParamParameter2( strLon );
                    dataSearchResult.setParamParameter3( paramScale );
                    // dataSearchResult.setPageHeader( pageHeader );
                    dataSearchResult.setHotelCount( hotelCount );
                    dataSearchResult.setHotelAllCount( hotelAllCount );

                    request.setAttribute( "SPOT", paramSpotId );
                    request.setAttribute( "SEQ", paramSeq );
                    if ( spotRet != false )
                    {
                        request.setAttribute( "SPOT_DATA", mapSpot );
                    }

                    request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                    requestDispatcher = request.getRequestDispatcher( "landmark_result.jsp" );
                    requestDispatcher.forward( request, response );

                }
                catch ( Exception e )
                {
                    Logging.error( "[ActionLandMarkSearchMobile requestDispatcher:" + sendUrl + "] Exception:"
                            + e.toString() );
                }
            }
            else
            {
                try
                {
                    request.setAttribute( "LOGIN_INFO", dataLoginInfo_M2 );
                    request.setAttribute( "HOTELID", paramId );
                    request.setAttribute( "SCALE", paramScale );
                    request.setAttribute( "DISPMAP", "true" );
                    request.setAttribute( "EMPTY", paramEmpty );
                    request.setAttribute( "PAGE", paramPage );

                    requestDispatcher = request.getRequestDispatcher( "landmark_result.jsp" );
                    requestDispatcher.forward( request, response );
                }
                catch ( Exception e )
                {

                }
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
