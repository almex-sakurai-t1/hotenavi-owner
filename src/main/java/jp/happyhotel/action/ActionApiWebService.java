package jp.happyhotel.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.ApiConstants;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.BaseApiAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.others.GenerateXmlContents;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.user.UserLoginInfo;

/**
 * 
 * �Z�������N���X�i�g�сj
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/06
 */

public class ActionApiWebService extends BaseAction
{
    final String              MASTER_VERSION    = "MasterVersion";
    final String              MASTER_ALL        = "MasterAll";
    final String              AD                = "Ad";
    final String              AD_RANDOM         = "AdRandom";
    final String              GPS               = "Gps";
    final String              AREA              = "Area";
    final String              STATION           = "St";
    final String              IC                = "Ic";
    final String              HOTEL_AREA        = "HotelArea";
    final String              KODAWARI          = "Kodawari";
    final String              HOTENAVI          = "Hotenavi";
    final String              COUPON            = "Coupon";
    final String              CHAIN_LIST        = "ChainList";
    final String              CHAIN             = "Chain";
    final String              FREEWORD          = "Freeword";
    final String              FREEWORD2         = "Freeword2";
    final String              HAPPIE            = "Happie";
    final String              RESERVE           = "Reserve";
    final String              PICKUP            = "Pickup";
    final String              NEW_OPEN          = "Newopen";
    final String              NEW_BUZZ          = "Newbuzz";
    final String              NEW_MESSAGE       = "Newmessage";
    final String              PV_RANKING        = "PvRanking";
    final String              FAVORITE          = "Favorite";
    final String              HISTORY           = "History";
    final String              DETAIL            = "Detail";
    final String              KUCHIKOMI         = "Kuchikomi";
    final String              CHECK_USER        = "CheckUser";
    final String              TOP_MENU          = "TopMenu";
    final String              SPECIAL_MENU      = "SpecialMenu";
    final String              MYHOTEL           = "MyHotel";
    final String              TOPICS            = "Topics";
    final String              MILE              = "Mile";
    final String              TOP_MESSAGE       = "TopMessage";
    final String              ISSUE_ID          = "IssueId";
    final String              DEL_MYHOTEL       = "DelMyHotel";
    final String              REG_DEVICEID      = "RegDeviceId";
    final String              PUSH_INFO         = "PushInfo";
    final String              PUSH_UNREAD       = "PushUnread";
    final String              UPD_PUSH_DATA     = "UpdPushData";
    final String              UUID_PUSH_CONFIG  = "UuidPushConfig";
    final String              RSV_DATA          = "RsvData";
    final String              TOUCH_STATE       = "TouchState";
    final String              ISSUE_UUID        = "IssueUuid";
    final String              CHECK_RECEIPT     = "CheckReceipt";
    final String              LVJ_HOTEL         = "lvjHotel";
    final String              userAgent         = "HappyHotel";
    final String              ipAddress         = "^125\\.63\\.42\\.(1(9[2-9])|2([0-1][0-9]|2[0-3]))$"; // 125.63.42.192/27�i125.63.42.192 �` 125.63.42.223�j�̐��K�\��
    private static ApiInfos   apiInfos;
    private RequestDispatcher requestDispatcher = null;

    /**
     * �n�b�s�[�z�e��WEBAPI
     * 
     * @see
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String paramUserId;
        String paramPasswd;
        String paramMethod;
        UserLoginInfo uli;
        ActionApiAdSearch aaAdSearch;
        ActionApiAdRandomSearch aaAdRandomSearch;
        ActionApiAreaSearch aaAreaSearch;
        ActionApiChainSearch aaChainSearch;
        ActionApiCouponSearch aaCouponSearch;
        ActionApiFavoriteHotel aaFavoriteHotel;
        ActionApiFreewordSearch aaFreewordSearch;
        ActionApiGpsSearch aaGpsSearch;
        ActionApiHistoryHotel aaHistoryHotel;
        ActionApiHotelAreaSearch aaHotelAreaSearch;
        ActionApiHotelDetail aaHotelDetail;
        ActionApiHotelKuchikomi aaHotelKuchikomi;
        ActionApiHotelNewArrival aaHotelNewArrival;
        ActionApiHotelNewBuzz aaHotelNewBuzz;
        ActionApiHotelNewMessage aaHotelNewMessage;
        ActionApiHotelNewOpen aaHotelNewOpen;
        ActionApiHotenaviSearch aaHotenaviSearch;
        ActionApiHotelPvRank aaHotelPvRank;
        ActionApiInterChangeSearch aaICSearch;
        ActionApiKodawariSearch aaKodawariSearch;
        ActionApiMasterVersionCheck aaMasterVersionCheck;
        ActionApiMasterDataDeliver aaMasterDataDeliver;
        ActionApiStationSearch aaStationSearch;
        ActionApiReserveSearch aaReserveSearch;
        ActionApiHappieSearch aaHappieSearch;
        ActionApiLoginCheck aaLoginCheck;
        ActionApiMenu aaMenu;
        ActionApiMyHotel aaMyHotel;
        ActionApiTopics aaTopics;
        ActionApiMile aaMile;
        ActionApiTopMessage aaTopMessage;
        ActionApiIssueId aaIssueId;
        ActionApiMyHotelDelete aaMyhotelDelete;
        ActionApiRegDeviceId aaRegDevice;
        ActionApiPushInfo aaPushInfo;
        ActionApiPushUnread aaPushUnread;
        ActionApiUpdPushData aaUpdPushData;
        ActionApiUuidPushConfig aaUuidPushConfig;
        ActionApiRsvData aaRsvData;
        ActionApiTouchState aaTouchState;
        ActionApiIssueUuid aaIssueUuid;
        ActionApiCheckReceipt aaCheckReceipt;
        GenerateXmlHeader header = new GenerateXmlHeader();

        // Logging.info( "log:" + request.getRequestURI() + "?" + request.getQueryString() );

        paramUserId = request.getParameter( "user_id" );
        paramPasswd = request.getParameter( "password" );
        paramMethod = request.getParameter( "method" );
        uli = new UserLoginInfo();

        Logging.info( "method=" + paramMethod + ", user_id=" + paramUserId + ", password=" + paramPasswd );

        if ( paramUserId == null )
        {
            paramUserId = "";
        }
        if ( paramPasswd == null )
        {
            paramPasswd = "";
        }
        if ( paramMethod == null )
        {
            paramMethod = "";
        }

        // �s���A�N�Z�X�h�~
        try
        {
            if ( !paramMethod.equals( LVJ_HOTEL ) && request.getHeader( "user-agent" ).indexOf( userAgent ) == -1 )
            {
                // �s���A�N�Z�X�̈׉�ʑJ��
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/index.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            else if ( paramMethod.equals( LVJ_HOTEL ) && !(request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr()).equals( "172.25.21.61" ) &&
                    !(request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr()).matches( ipAddress ) )
            {
                // �s���A�N�Z�X�̈׉�ʑJ��
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/index.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionApiWebService]Exception:" + e.toString() );
        }

        if ( paramMethod.equals( LVJ_HOTEL ) ) // ���u�C���W���p���pJSON
        {
            apiInfos = new ApiInfos();
            apiInfos.add( LVJ_HOTEL, ActionApiLvjHotel.class.getName() );

            try
            {

                @SuppressWarnings("rawtypes") Class myClass = Class.forName( apiInfos.getClassName( paramMethod ) );
                BaseApiAction action = (jp.happyhotel.common.BaseApiAction)myClass.newInstance();
                action.initialize( request );
                action.execute( request, response );
                return;
            }
            catch ( Throwable t )
            {
                Logging.error( "[ActionApiWebService method=" + paramMethod + "]Exception:" + t.toString(), t );
                BaseApiAction action = new BaseApiAction();
                action.initialize( request );
                action.outputErrorJon( response, ApiConstants.ERROR_CODE_API9, ApiConstants.ERROR_MSG_API9 );
            }

            return;

        }
        else
        // ����XML
        {
            if ( paramUserId.equals( "" ) == false && paramPasswd.equals( "" ) == false )
            {
                // ���[�U�F�؂��s��
                uli.getUserLoginInfo( paramUserId, paramPasswd );
            }
            // ���ʂ��Z�b�g
            request.setAttribute( "USER_INFO", uli );

            // ������`�F�b�N
            if ( paramMethod.equals( CHECK_USER ) != false )
            {
                aaLoginCheck = new ActionApiLoginCheck();
                aaLoginCheck.execute( request, response );
                return;
            }

            // ���Âꂩ�̉�������擾������paramMethod�Ŕ��ʂ���
            if ( uli.isMemberFlag() != false || uli.isNonmemberFlag() != false || uli.isPaymemberFlag() != false || uli.isPaymemberTempFlag() != false )
            {

                // �e���\�b�h�ɉ����ĐU�蕪����
                // �}�X�^�[�o�[�W�����̊m�F
                if ( paramMethod.equals( MASTER_VERSION ) != false )
                {
                    aaMasterVersionCheck = new ActionApiMasterVersionCheck();
                    aaMasterVersionCheck.execute( request, response );
                    return;
                }
                // �}�X�^�[���擾
                else if ( paramMethod.equals( MASTER_ALL ) != false )
                {
                    aaMasterDataDeliver = new ActionApiMasterDataDeliver();
                    aaMasterDataDeliver.execute( request, response );
                    return;
                }
                // �L�����擾
                else if ( paramMethod.equals( AD ) != false )
                {
                    aaAdSearch = new ActionApiAdSearch();
                    aaAdSearch.execute( request, response );
                    return;
                }
                // �L�����[�e�[�V����
                else if ( paramMethod.equals( AD_RANDOM ) != false )
                {
                    aaAdRandomSearch = new ActionApiAdRandomSearch();
                    aaAdRandomSearch.execute( request, response );
                    return;
                }

                // GPS���擾
                else if ( paramMethod.equals( GPS ) != false )
                {
                    aaGpsSearch = new ActionApiGpsSearch();
                    aaGpsSearch.execute( request, response );
                    return;
                }
                // �Z������
                else if ( paramMethod.equals( AREA ) != false )
                {
                    aaAreaSearch = new ActionApiAreaSearch();
                    aaAreaSearch.execute( request, response );
                    return;
                }
                // �w����
                else if ( paramMethod.equals( STATION ) != false )
                {
                    aaStationSearch = new ActionApiStationSearch();
                    aaStationSearch.execute( request, response );
                    return;
                }
                // �C���^�[�`�F���W����
                else if ( paramMethod.equals( IC ) != false )
                {
                    aaICSearch = new ActionApiInterChangeSearch();
                    aaICSearch.execute( request, response );
                    return;
                }
                // �z�e���G���A����
                else if ( paramMethod.equals( HOTEL_AREA ) != false )
                {
                    aaHotelAreaSearch = new ActionApiHotelAreaSearch();
                    aaHotelAreaSearch.execute( request, response );
                    return;
                }
                // ������茟��
                else if ( paramMethod.equals( KODAWARI ) != false )
                {
                    aaKodawariSearch = new ActionApiKodawariSearch();
                    aaKodawariSearch.execute( request, response );
                    return;
                }
                // �z�e�i�r����
                else if ( paramMethod.equals( HOTENAVI ) != false )
                {
                    aaHotenaviSearch = new ActionApiHotenaviSearch();
                    aaHotenaviSearch.execute( request, response );
                    return;
                }
                // �N�[�|������
                else if ( paramMethod.equals( COUPON ) != false )
                {
                    aaCouponSearch = new ActionApiCouponSearch();
                    aaCouponSearch.execute( request, response );
                    return;
                }
                // �`�F�[���X����
                else if ( paramMethod.equals( CHAIN_LIST ) != false || paramMethod.equals( CHAIN ) != false )
                {
                    aaChainSearch = new ActionApiChainSearch();
                    aaChainSearch.execute( request, response );
                    return;
                }
                // �t���[���[�h����
                else if ( paramMethod.equals( FREEWORD ) != false || paramMethod.equals( FREEWORD2 ) != false )
                {
                    aaFreewordSearch = new ActionApiFreewordSearch();
                    aaFreewordSearch.execute( request, response );
                    return;
                }
                // �n�s�[�����X����
                else if ( paramMethod.equals( HAPPIE ) != false )
                {
                    aaHappieSearch = new ActionApiHappieSearch();
                    aaHappieSearch.execute( request, response );
                    return;
                }
                // �\��n�s�[�����X����
                else if ( paramMethod.equals( RESERVE ) != false )
                {
                    aaReserveSearch = new ActionApiReserveSearch();
                    aaReserveSearch.execute( request, response );
                    return;
                }
                // �V���z�e������
                else if ( paramMethod.equals( PICKUP ) != false )
                {
                    aaHotelNewArrival = new ActionApiHotelNewArrival();
                    aaHotelNewArrival.execute( request, response );
                    return;
                }
                // �j���[�I�[�v���z�e������
                else if ( paramMethod.equals( NEW_OPEN ) != false )
                {
                    aaHotelNewOpen = new ActionApiHotelNewOpen();
                    aaHotelNewOpen.execute( request, response );
                    return;
                }
                // �V���N�`�R�~
                else if ( paramMethod.equals( NEW_BUZZ ) != false )
                {
                    aaHotelNewBuzz = new ActionApiHotelNewBuzz();
                    aaHotelNewBuzz.execute( request, response );
                    return;
                }
                // �V�����b�Z�[�W
                else if ( paramMethod.equals( NEW_MESSAGE ) != false )
                {
                    aaHotelNewMessage = new ActionApiHotelNewMessage();
                    aaHotelNewMessage.execute( request, response );
                    return;
                }
                // �z�e�������L���O
                else if ( paramMethod.equals( PV_RANKING ) != false )
                {
                    aaHotelPvRank = new ActionApiHotelPvRank();
                    aaHotelPvRank.execute( request, response );
                    return;
                }
                // ���C�ɓ���z�e��
                else if ( paramMethod.equals( FAVORITE ) != false )
                {
                    aaFavoriteHotel = new ActionApiFavoriteHotel();
                    aaFavoriteHotel.execute( request, response );
                    return;
                }
                // �ŋ߃`�F�b�N�����z�e��
                else if ( paramMethod.equals( HISTORY ) != false )
                {
                    aaHistoryHotel = new ActionApiHistoryHotel();
                    aaHistoryHotel.execute( request, response );
                    return;
                }
                // �z�e���ڍ�
                else if ( paramMethod.equals( DETAIL ) != false )
                {
                    aaHotelDetail = new ActionApiHotelDetail();
                    aaHotelDetail.execute( request, response );
                    return;
                }
                // �N�`�R�~
                else if ( paramMethod.equals( KUCHIKOMI ) != false )
                {
                    aaHotelKuchikomi = new ActionApiHotelKuchikomi();
                    aaHotelKuchikomi.execute( request, response );
                    return;
                }
                // XML���j���[�\��
                else if ( (paramMethod.equals( TOP_MENU ) != false) || (paramMethod.equals( SPECIAL_MENU ) != false) )
                {
                    aaMenu = new ActionApiMenu();
                    aaMenu.execute( request, response );
                    return;
                }
                // �}�C�z�e��
                else if ( paramMethod.equals( MYHOTEL ) != false )
                {
                    aaMyHotel = new ActionApiMyHotel();
                    aaMyHotel.execute( request, response );
                    return;
                }
                // �g�s�b�N�X
                else if ( paramMethod.equals( TOPICS ) != false )
                {
                    aaTopics = new ActionApiTopics();
                    aaTopics.execute( request, response );
                    return;
                }
                // ���[�U�}�C���\��
                else if ( paramMethod.equals( MILE ) != false )
                {
                    aaMile = new ActionApiMile();
                    aaMile.execute( request, response );
                    return;
                }
                // �g�b�v���b�Z�[�W
                else if ( paramMethod.equals( TOP_MESSAGE ) != false )
                {
                    aaTopMessage = new ActionApiTopMessage();
                    aaTopMessage.execute( request, response );
                    return;
                }
                // �A�v��������o�^
                else if ( paramMethod.equals( ISSUE_ID ) != false )
                {
                    aaIssueId = new ActionApiIssueId();
                    aaIssueId.execute( request, response );
                    return;
                }
                // �}�C�z�e���폜
                else if ( paramMethod.equals( DEL_MYHOTEL ) != false )
                {
                    aaMyhotelDelete = new ActionApiMyHotelDelete();
                    aaMyhotelDelete.execute( request, response );
                    return;

                }
                // PUSH�ʒm�pID�o�^�ʒm
                else if ( paramMethod.equals( REG_DEVICEID ) != false )
                {
                    aaRegDevice = new ActionApiRegDeviceId();
                    aaRegDevice.execute( request, response );
                    return;
                }
                // PUSH�ꗗ�擾
                else if ( paramMethod.equals( PUSH_INFO ) != false )
                {
                    aaPushInfo = new ActionApiPushInfo();
                    aaPushInfo.execute( request, response );
                    return;
                }
                // PUSH���ǌ����ʒm
                else if ( paramMethod.equals( PUSH_UNREAD ) != false )
                {
                    aaPushUnread = new ActionApiPushUnread();
                    aaPushUnread.execute( request, response );
                    return;
                }
                // PUSH���ǌ����ʒm
                else if ( paramMethod.equals( UPD_PUSH_DATA ) != false )
                {
                    aaUpdPushData = new ActionApiUpdPushData();
                    aaUpdPushData.execute( request, response );
                    return;
                }
                // �n�s�z�ePUSH�ʒm�ݒ�
                else if ( paramMethod.equals( UUID_PUSH_CONFIG ) != false )
                {
                    aaUuidPushConfig = new ActionApiUuidPushConfig();
                    aaUuidPushConfig.execute( request, response );
                    return;
                }
                // �\��ꗗ���
                else if ( paramMethod.equals( RSV_DATA ) != false )
                {
                    aaRsvData = new ActionApiRsvData();
                    aaRsvData.execute( request, response );
                    return;
                }
                // �^�b�`��t��
                else if ( paramMethod.equals( TOUCH_STATE ) != false )
                {
                    aaTouchState = new ActionApiTouchState();
                    aaTouchState.execute( request, response );
                    return;
                }
                // UUID���s
                else if ( paramMethod.equals( ISSUE_UUID ) != false )
                {
                    aaIssueUuid = new ActionApiIssueUuid();
                    aaIssueUuid.execute( request, response );
                    return;
                }
                // ���V�[�g���ؗv��
                else if ( paramMethod.equals( CHECK_RECEIPT ) != false )
                {
                    aaCheckReceipt = new ActionApiCheckReceipt();
                    aaCheckReceipt.execute( request, response );
                    return;
                }

                else
                {
                    GenerateXmlContents contents = new GenerateXmlContents();
                    contents.setError( Constants.ERROR_MSG_API4 );
                    contents.setErrorCode( Constants.ERROR_CODE_API4 );

                    // ���[�g�m�[�h���Z�b�g
                    header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                    header.setMethod( paramMethod );
                    header.setCount( 0 );
                    header.setContents( contents );

                }
            }
            else
            {
                GenerateXmlContents contents = new GenerateXmlContents();
                contents.setError( Constants.ERROR_MSG_API3 );
                contents.setErrorCode( Constants.ERROR_CODE_API3 );

                // ���[�g�m�[�h���Z�b�g
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setCount( 0 );
                header.setContents( contents );
            }

            try
            {
                // �o�͂��w�b�_�[����
                String xmlOut = header.createXml();
                Logging.info( xmlOut );
                ServletOutputStream out = null;

                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiWebService]Exception:" + e.toString() );
            }
        }
    }

    /**
     * API���Ǘ��N���X
     * 
     */
    private class ApiInfos
    {
        Map<String, ApiInfo> map = new HashMap<String, ApiInfo>();

        /**
         * API����ǉ�
         * 
         * @param method
         * @param className
         */
        public void add(String method, String className)
        {
            ApiInfo apiInfo = new ApiInfo( method, className, false );
            map.put( method, apiInfo );
        }

        /**
         * API����ǉ�
         * 
         * @param method
         * @param className
         * @param needAuth
         */
        public void add(String method, String className, boolean needAuth)
        {
            ApiInfo apiInfo = new ApiInfo( method, className, needAuth );
            map.put( method, apiInfo );
        }

        /**
         * �L����API���ǂ���
         * 
         * @param method
         * @return
         */
        public boolean exists(String method)
        {
            return getClassName( method ) != null;
        }

        /**
         * API�̃N���X����Ԃ�
         * 
         * @param method
         * @return
         */
        public String getClassName(String method)
        {
            ApiInfo apiInfo = map.get( method );
            if ( apiInfo == null )
            {
                return null;
            }
            return apiInfo.getClassName();
        }

        /**
         * �F�؂̗v�ۂ�Ԃ�
         * 
         * @param method
         * @return
         */
        public boolean isNeedAuth(String method)
        {
            ApiInfo apiInfo = map.get( method );
            if ( apiInfo == null )
            {
                return true;
            }
            return apiInfo.isNeedAuth();
        }
    }

    /**
     * API���
     * 
     */
    private class ApiInfo
    {
        String  method;
        String  className;
        boolean needAuth;

        public ApiInfo(String method, String className, boolean needAuth)
        {
            this.method = method;
            this.className = className;
            this.needAuth = needAuth;
        }

        @SuppressWarnings("unused")
        public String getMethod()
        {
            return method;
        }

        public String getClassName()
        {
            return className;
        }

        public boolean isNeedAuth()
        {
            return needAuth;
        }
    }
}
