package jp.happyhotel.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.ws.rs.core.MediaType;

import jp.happyhotel.data.DataHhRsvSystemConfList;
import jp.happyhotel.data.DataHotelAdjustmentHistory;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.OwnerLoginInfo;
import jp.happyhotel.util.ReserveGuestMailToken;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

/**
 * �I�[�i�[�T�C�g���j���[���ʃN���X
 * 
 * @author H.Takanami
 * @version 1.00 2010/12/02
 * @see
 */
public class OwnerRsvCommon implements Serializable
{
    private static final long  serialVersionUID                  = 5406052699924130708L;

    // ���n�s�[�t�^�敪
    public static final int    POINT_KIND_ROOM                   = 1;                   // �����ł̕t�^
    public static final int    POINT_KIND_FIX                    = 2;                   // �Œ�|�C���g�ł̕t�^

    // ���I�[�i�[����
    public static final int    USER_AUTH_OWNER                   = 1;                   // �I�[�i�[
    public static final int    USER_AUTH_FRONT                   = 2;                   // �t�����g
    public static final int    USER_AUTH_DEMO                    = 3;                   // �x�X�f��
    public static final int    USER_AUTH_CALLCENTER              = 4;                   // �R�[���Z���^�[

    // ��imediaFlg
    public static final int    IMEDIAFLG_IMEDIA                  = 1;                   // �A�����b�N�X�Ј�
    public static final int    IMEDIAFLG_OWNER                   = 0;                   // �I�[�i�[

    // ���|�C���g�����t���O
    public static final int    LIMIT_FLG_TIME                    = 3;                   // ����
    public static final int    LIMIT_FLG_DAY                     = 4;                   // ��
    public static final int    LIMIT_FLG_MONTH                   = 5;                   // ��

    // ���J�����_�[�j���X�e�[�^�X
    public static final int    CALENDAR_MONDAY                   = 0x01;
    public static final int    CALENDAR_TUESDAY                  = 0x02;
    public static final int    CALENDAR_WEDNESDAY                = 0x04;
    public static final int    CALENDAR_THIRTHDAY                = 0x08;
    public static final int    CALENDAR_FRIDAY                   = 0x10;
    public static final int    CALENDAR_SATURDAY                 = 0x20;
    public static final int    CALENDAR_SUNDAY                   = 0x40;
    public static final int    CALENDAR_HOLIDAY                  = 0x80;
    public static final int    CALENDAR_BEFOREHOLIDAY            = 0x100;

    // ���n�s�[�|�C���g�擾�敪
    public static final int    HAPYPOINT_24                      = 24;                  // �\��}�C��
    public static final int    HAPYPOINT_29                      = 29;                  // �\��{�[�i�X�}�C������敪
    public static final int    RSV_BONUS_CODE                    = 1000017;             // �\��{�[�i�X�}�C���R�[�h

    // ���v�����ʃJ�����_�[�Ǘ��Ŏg�p
    public static final int    PLAN_CAL_HOTELID                  = 0;
    public static final String PLAN_CAL_HOTELNM                  = "�z�e���S��";

    // ���v�����o�^�Ŏg�p����{�^���̋敪�l
    public static final int    BTN_SALES                         = 1;                   // �ꎞ�I�ɒ�~����{�^��
    public static final int    BTN_REGIST                        = 2;                   // �v�����ݒ�X�V
    public static final int    BTN_DRAFT                         = 3;                   // �������ۑ��{�^��
    public static final int    BTN_VIEW                          = 4;                   // �\���{�^��
    public static final int    BTN_DEL                           = 5;                   // �������폜�{�^��
    public static final int    BTN_DRAFTUPD                      = 6;                   // �������@�v�����ݒ�X�V�{�^��
    public static final int    BTN_PREVIEW                       = 7;                   // �v���r���[
    public static final int    BTN_BACK                          = 8;                   // �߂�{�^��
    public static final int    BTN_PREVIEW_DETAIL                = 9;                   // �v���r���[�ڍ�
    public static final int    BTN_COPYREGIST                    = 10;                  // �R�s�[���ĕۑ��{�^��
    public static final int    BTN_COPY                          = 11;                  // �R�s�[�{�^��

    // �v�����f�ڃt���O
    public static final int    PLAN_VIEW_ALL                     = 0;                   // ���f�ڃv�������\������
    public static final int    PLAN_VIEW_PART                    = 1;                   // ���f�ڃv�����͕\�����Ȃ�

    // �z�e���C������
    public static final int    ADJUST_EDIT_ID_RSV                = 200;                 // �\��J�n�E��~
    public static final int    ADJUST_EDIT_ID_RSV_DAY            = 201;                 // �\��J�n�E��~�i���t�P�ʁj
    public static final int    ADJUST_EDIT_ID_RSV_PLAN           = 202;                 // �\��J�n�E��~�i�v�����P�ʁj
    public static final int    ADJUST_EDIT_ID_HOTEL_BASIC        = 210;                 // �{�݊�{���
    public static final int    ADJUST_EDIT_ID_RSV_BASIC          = 220;                 // �\���{���
    public static final int    ADJUST_EDIT_ID_PLAN_ADD           = 230;                 // �v�����ݒ�ǉ�
    public static final int    ADJUST_EDIT_ID_PLAN_UPDWN         = 231;                 // �f�ځE��f��
    public static final int    ADJUST_EDIT_ID_PLAN_UPD           = 232;                 // �ύX
    public static final int    ADJUST_EDIT_ID_CHARGE_ADD         = 240;                 // �������[�h�ǉ�
    public static final int    ADJUST_EDIT_ID_CHARGE_DEL         = 241;                 // �폜
    public static final int    ADJUST_EDIT_ID_CHARGE_UPD         = 242;                 // �X�V
    public static final int    ADJUST_EDIT_ID_PLAN_CHARGE_ADDUPD = 250;                 // �v�����ʗ����ǉ��E�X�V
    public static final int    ADJUST_EDIT_ID_PLAN_CHARGE_DEL    = 251;                 // �폜
    public static final int    ADJUST_EDIT_ID_ROOM               = 270;                 // �������ݒ�
    public static final int    ADJUST_EDIT_ID_OPTION_ADD         = 280;                 // �I�v�V�����ݒ�ǉ�
    public static final int    ADJUST_EDIT_ID_OPTION_DEL         = 281;                 // �폜
    public static final int    ADJUST_EDIT_ID_OPTION_UPD         = 282;                 // �X�V
    public static final int    ADJUST_EDIT_ID_CALENDAR           = 300;                 // �J�����_�[�ݒ�
    public static final int    ADJUST_EDIT_ID_BILL_ADD           = 500;                 // �������גǉ�
    public static final int    ADJUST_EDIT_ID_BILL_DEL           = 501;                 // �������׍폜

    public static final String ADJUST_MEMO_RSV_START             = "�\��J�n";
    public static final String ADJUST_MEMO_RSV_STOP              = "�\���~";
    public static final String ADJUST_MEMO_RSV_START_DAY         = "�\��J�n�i���t�P�ʁj";
    public static final String ADJUST_MEMO_RSV_STOP_DAY          = "�\���~�i���t�P�ʁj";
    public static final String ADJUST_MEMO_RSV_START_PLAN        = "�\��J�n�i�v�����P�ʁj";
    public static final String ADJUST_MEMO_RSV_STOP_PLAN         = "�\���~�i�v�����P�ʁj";
    public static final String ADJUST_MEMO_HOTEL_BASIC           = "�{�݊�{���C��";
    public static final String ADJUST_MEMO_RSV_BASIC             = "�\���{���C��";
    public static final String ADJUST_MEMO_PLAN_ADD              = "�v�����ݒ�ǉ�";
    public static final String ADJUST_MEMO_PLAN_UP               = "�v�����ݒ�f��";
    public static final String ADJUST_MEMO_PLAN_DWN              = "�v�����ݒ��f��";
    public static final String ADJUST_MEMO_PLAN_UPD              = "�v�����ݒ�ύX";
    public static final String ADJUST_MEMO_CHAGE_ADD             = "�������[�h�ǉ�";
    public static final String ADJUST_MEMO_CHAGE_DEL             = "�������[�h�폜";
    public static final String ADJUST_MEMO_CHAGE_UPD             = "�������[�h�ύX";
    public static final String ADJUST_MEMO_PLAN_CHARGE_ADDUPD    = "�v�����ʗ����ǉ��E�X�V";
    public static final String ADJUST_MEMO_PLAN_CHARGE_DEL       = "�v�����ʗ����폜";
    public static final String ADJUST_MEMO_ROOM                  = "�������ݒ�";
    public static final String ADJUST_MEMO_OPTION_ADD            = "�I�v�V�����ݒ�ǉ�";
    public static final String ADJUST_MEMO_OPTION_DEL            = "�I�v�V�����ݒ�폜";
    public static final String ADJUST_MEMO_OPTION_UPD            = "�I�v�V�����ݒ�ύX";
    public static final String ADJUST_MEMO_CALENDAR              = "�J�����_�[�ݒ�";
    public static final String ADJUST_MEMO_BILL_ADD              = "�������גǉ�";
    public static final String ADJUST_MEMO_BILL_DEL              = "�������׍폜";

    // ���\��V�X�e���ݒ�
    public static final int    CTRL_ID1_PREMIUM_GOAHEAD_DAYS     = 1;
    public static final int    CTRL_ID2_PREMIUM_GOAHEAD_DAYS     = 1;

    // �\��敪
    public static final int    EXT_HAPIHOTE                      = 0;                   // �n�s�z�e����̗\��
    public static final int    EXT_LVJ                           = 1;                   // ���u�C���W���p������̗\��
    public static final int    EXT_OTA                           = 2;                   // OTA����̗\��

    // �v�����̔���
    public static final int    PLAN_SALES_STATUS_SALE            = 1;

    // �f�ڃt���O - �f��
    public static final int    PUBLISHING_FLAG_PUBLISH           = 1;

    /***
     * ���j���[�ݒ�
     * 
     * @param frm FormOwnerRsvMenu�I�u�W�F�N�g
     * @param hotelID �z�e��ID
     * @param modeFlg
     * @param userID ���O�C�����[�UID
     * @return �Ȃ�
     **/
    public static void setMenu(FormOwnerBkoMenu frm, int hotelID, int modeFlg, Cookie[] cookies) throws Exception
    {
        int userAuth = 0;
        String hotenaviId = "";
        String loginHotelId = "";
        int loginUserId = 0;
        int hapihoteFlag = 0;
        int imediaFlg = 0;
        int adminFlg = 0;
        int billFlg = 0;
        Cookie hhCookie = null;
        LogicOwnerBkoMenu logic = new LogicOwnerBkoMenu();
        ArrayList<Integer> hotelIdList = new ArrayList<Integer>();
        ArrayList<String> hotelNmList = new ArrayList<String>();
        OwnerLoginInfo loginUser = new OwnerLoginInfo();
        String cookie_value = "";

        frm.setModeFlg( modeFlg );
        frm.setSelHotelID( hotelID );

        try
        {
            logic.setFrm( frm );

            // Cookie���擾
            if ( cookies != null )
            {
                for( int i = 0 ; i < cookies.length ; i++ )
                {
                    if ( cookies[i].getName().compareTo( "hhownuid" ) == 0 )
                    {
                        hhCookie = cookies[i];
                        break;
                    }
                }
            }
            cookie_value = hhCookie.getValue();

            // ���O�C�����擾
            if ( cookie_value.compareTo( "" ) != 0 )
            {
                loginUser.getLoginByCookie( cookie_value );
            }
            loginHotelId = loginUser.getUserInfo().getHotelId();
            loginUserId = loginUser.getUserInfo().getUserId();
            if ( loginHotelId.compareTo( "happyhotel" ) == 0 )
            {
                hapihoteFlag = 1;
            }

            // �z�e�i�rID�擾
            hotenaviId = getCookieLoginHotenavi( cookies );

            // ���[�U�[�̌����擾
            userAuth = logic.getUserAuth( hotenaviId, loginUserId );

            // imediaFlg�擾(1:�A�����b�N�X�Ј��A0:�A�����b�N�X�Ј��ȊO)
            imediaFlg = getImediaFlag( loginHotelId, loginUserId );

            // �Ǘ��҃t���O�擾
            adminFlg = logic.getAdminFlg( loginHotelId, loginUserId );

            // �����{���\�t���O�擾
            billFlg = OwnerBkoCommon.getBillOwnFlg( hotenaviId, loginUserId );

            // �z�e�����擾
            if ( hapihoteFlag == 1 && imediaFlg == 1 && adminFlg == 1 )
            {
                // �Ǘ���
                hotelIdList = logic.getAdminHotelIDList();
                hotelNmList = logic.getAdminHotelNmList();
            }
            else
            {
                if ( userAuth == OwnerRsvCommon.USER_AUTH_CALLCENTER )
                {
                    // �R�[���Z���^�[�̂݃}�C�������X�S�z�e��
                    hotelIdList = logic.getMileHotelIDList();
                    hotelNmList = logic.getMileHotelNmList();
                }
                else
                {
                    // �Ǘ��҈ȊO
                    hotelIdList = logic.getHotelIDList( loginHotelId, loginUserId );
                    hotelNmList = logic.getHotelNmList( loginHotelId, loginUserId );
                }
            }

            frm.setHotelIDList( hotelIdList );
            frm.setHotelNmList( hotelNmList );
            frm.setUserId( loginUserId );
            frm.setUserAuth( userAuth );
            frm.setImediaFlg( imediaFlg );
            frm.setBillFlg( billFlg );
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.setMenu() ] " + e.getMessage() );
            throw new Exception( "[OwnerRsvCommon.setMenu() ] " + e.getMessage() );
        }
    }

    /***
     * �����񒆂̕������𔼊p������Ń`�F�b�N����
     * 
     * @param input �`�F�b�N�Ώۂ̕�����
     * @param length ���͉\����
     * @return �`�F�b�N���ʁB0:�����́A1:�`�F�b�NOK�A99�F�`�F�b�NNG
     **/
    public static int LengthCheck(String input, int length) throws Exception
    {
        int ret = -1;
        int valueLeng;
        try
        {
            valueLeng = input.getBytes( "Shift_JIS" ).length;

            if ( valueLeng == 0 )
            {
                // �����͂̏ꍇ
                ret = 0;
            }
            else if ( valueLeng > length )
            {
                // ����Over
                ret = 1;
            }

            return ret;
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new Exception( "[ActionOwnerU10301.LengthCheck() ]" + e.toString() );
        }
    }

    /**
     * �̔���~�j���ݒ�\���p������擾
     * 
     * @param salesStopWeekStatus �̔���~�j���X�e�[�^�X
     * @return �̔���~�j���ݒ�\���p������
     */
    public static String createSalesStopWeek(int salesStopWeekStatus)
    {
        String ret = "";

        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_MONDAY) == OwnerRsvCommon.CALENDAR_MONDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "��";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_TUESDAY) == OwnerRsvCommon.CALENDAR_TUESDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "��";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_WEDNESDAY) == OwnerRsvCommon.CALENDAR_WEDNESDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "��";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_THIRTHDAY) == OwnerRsvCommon.CALENDAR_THIRTHDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "��";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_FRIDAY) == OwnerRsvCommon.CALENDAR_FRIDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "��";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_SATURDAY) == OwnerRsvCommon.CALENDAR_SATURDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "�y";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_SUNDAY) == OwnerRsvCommon.CALENDAR_SUNDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "��";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_HOLIDAY) == OwnerRsvCommon.CALENDAR_HOLIDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "�j��";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY) == OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "�j�O��";
        }
        if ( ret.equals( "" ) == true )
        {
            ret = "�Ȃ�";
        }

        return(ret);
    }

    /**
     * �I���ςݐݔ�ID�ƑS�Ă̐ݔ�ID�̃}�b�`���O
     * �S�Ă̐ݔ�ID�̂����A�I������Ă���ݔ�ID�ƈ�v������1���Z�b�g����B
     * 
     * @param allEquipIdList �z�e���ɕR�Â��ݔ�ID�̃��X�g
     * @param eqIdList �I������Ă���ݔ�ID�̃��X�g
     * @return �}�b�`���O�������s��ꂽ���ArrayList
     */
    public static ArrayList<Integer> setSelEqList(ArrayList<Integer> allEquipIdList, ArrayList<Integer> eqIdList)
    {
        int selEqID = 0;
        ArrayList<Integer> selEquipIdList = new ArrayList<Integer>();

        // �I���ςݐݔ�ID�̐ݒ�
        for( int i = 0 ; i <= allEquipIdList.size() ; i++ )
        {
            selEquipIdList.add( 0 );
        }

        for( int i = 0 ; i <= eqIdList.size() - 1 ; i++ )
        {
            selEqID = eqIdList.get( i );

            for( int j = 0 ; j <= allEquipIdList.size() - 1 ; j++ )
            {
                if ( selEqID == allEquipIdList.get( j ) )
                {
                    selEquipIdList.set( j, 1 );
                    break;
                }
            }
        }

        return selEquipIdList;
    }

    /**
     * Cookie����z�e�i�rID���擾����
     * 
     * @param cookies �N�b�L�[
     * @return �w�肳�ꂽ�敪�ɊY������l
     */
    public static String getCookieLoginHotenavi(Cookie[] cookies)
    {
        String cookValue = "";
        String value = "";
        int sepalateIdx = 0;

        for( int i = 0 ; cookies != null && i < cookies.length ; i++ )
        {
            // ���O�C�����[�UID�擾
            if ( cookies[i].getName().equals( "hhownuid" ) )
            {
                cookValue = cookies[i].getValue();
                // ��؂蕶����Index���擾
                sepalateIdx = cookValue.indexOf( ":" );
                value = cookValue.substring( 0, sepalateIdx );
                break;
            }
        }
        return value;
    }

    /**
     * ���p�����`�F�b�N����
     * �}�C�i�X�͋��e���Ȃ��B
     * 
     * @param orgNum �`�F�b�N�Ώە�����
     * @return ��������(true:����,false:�ُ�)
     */
    public static boolean numCheck(String orgNum)
    {
        char cutData;
        if ( orgNum != null && orgNum.trim().length() > 0 )
        {
            for( int i = 0 ; i < orgNum.trim().length() ; i++ )
            {
                cutData = orgNum.charAt( i );
                if ( (cutData < '0' || cutData > '9') )
                {
                    return(false);
                }
            }
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * �Ώۃv�������\��ς݂�
     * 
     * @param hotelID �z�e��ID
     * @param planID �v����ID
     * @return true:�\��ς݁AFalse:�\�񂳂�Ă��Ȃ�
     */
    public static boolean isReservePlan(int hotelID, int planID) throws Exception
    {
        int cnt = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = query + "SELECT COUNT(*) CNT ";
        query = query + "FROM hh_rsv_reserve ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";
        query = query + "  AND status = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelID );
            prestate.setInt( 2, planID );
            prestate.setInt( 3, 1 );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.isReservePlan] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �w����ԓ��ɗ\��ς݃f�[�^�����邩
     * 
     * @param hotelID �z�e��ID
     * @param planID �v����ID
     * @param fromDate �Ώۊ��ԊJ�n��
     * @param toDate �Ώۊ��ԏI����
     * @return true:�\��ς݁AFalse:�\�񂳂�Ă��Ȃ�
     */
    public static boolean isReservePlanTargetDate(int hotelID, int planID, int fromDate, int toDate) throws Exception
    {
        int cnt = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = query + "SELECT COUNT(*) CNT ";
        query = query + "FROM hh_rsv_reserve ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";
        query = query + "  AND reserve_date BETWEEN ? AND ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelID );
            prestate.setInt( 2, planID );
            prestate.setInt( 3, fromDate );
            prestate.setInt( 4, toDate );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.isReservePlanTargetDate] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �A�����b�N�X�Ј����ǂ����̏���Ԃ�
     * 
     * @param hotelID �z�e��ID
     * @param userID ���[�UID
     * @return 1:�A�����b�N�X�Ј��A0:
     * @throws Exception
     */
    public static int getImediaFlag(Connection connection, String hotelID, int userID)
    {
        int imediaFlag = 0;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            String sql = "SELECT imedia_user FROM owner_user WHERE hotelid = ? AND userid = ?";
            prestate = connection.prepareStatement( sql );
            prestate.setString( 1, hotelID );
            prestate.setInt( 2, userID );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                imediaFlag = result.getInt( "imedia_user" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getImediaFlag] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return(imediaFlag);
    }

    /**
     * �v���~�A����s�\������̎擾
     * 
     * @return �v���~�A����s�\�����
     * @throws Exception
     */
    public static String getPremiumGoAheadDays()
    {
        String premiumDays = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        Connection connection = null;

        try
        {
            connection = DBConnection.getConnection();
            String sql = "SELECT val1 FROM hh_rsv_system_conf WHERE ctrl_id1 = ? AND ctrl_id2 = ?";
            prestate = connection.prepareStatement( sql );
            prestate.setInt( 1, CTRL_ID1_PREMIUM_GOAHEAD_DAYS );
            prestate.setInt( 2, CTRL_ID2_PREMIUM_GOAHEAD_DAYS );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                premiumDays = result.getString( "val1" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getPremiumGoAheadDays] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(premiumDays);
    }

    /**
     * �A�����b�N�X�Ј����ǂ����̏���Ԃ�
     * Connection�̓��\�b�h�̒���Close
     * 
     * @param hotelID �z�e��ID
     * @param userID ���[�UID
     * @return 1:�A�����b�N�X�Ј��A0:
     * @throws Exception
     */
    public static int getImediaFlag(String hotelID, int userID)
    {
        int imediaFlag = 0;
        ResultSet result = null;
        PreparedStatement prestate = null;
        Connection connection = null;

        try
        {
            String sql = "SELECT imedia_user FROM owner_user WHERE hotelid = ? AND userid = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( sql );
            prestate.setString( 1, hotelID );
            prestate.setInt( 2, userID );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                imediaFlag = result.getInt( "imedia_user" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getImediaFlag] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(imediaFlag);
    }

    /**
     * �ŐV�\��}�Ԏ擾����
     * 
     * @param id �z�e��ID
     * @param reserveNo �\��ԍ�
     * @return �\��}��(�ō��l)
     */
    public static int getMaxRsvSubNo(int id, String reserveNo) throws Exception
    {
        int ret = 0;

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = "select reserve_sub_no from hh_rsv_reserve_history where id = ? and reserve_no = ? order by reserve_no desc";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, reserveNo );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                ret = result.getInt( "reserve_sub_no" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getMaxRsvSubNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �w����w��v�����ɗ\�񂪂��邩
     * 
     * @param hotelID �z�e��ID
     * @param planID �v����ID
     * @param caldate �`�F�b�N�Ώۓ�
     * @return true:�\�񂠂�AFalse:�\��Ȃ�
     */
    public static boolean isExistsRsvPlanByDay(int hotelID, int planID, int caldate) throws Exception
    {
        int cnt = 0;
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT COUNT(*) CNT ";
        query = query + "FROM hh_rsv_reserve ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";
        query = query + "  AND status = 1 ";
        query = query + "  AND reserve_date = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelID );
            prestate.setInt( 2, planID );
            prestate.setInt( 3, caldate );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.isExistsRsvPlanByDay] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �w��v�����ɗ\�񂪂��邩
     * 
     * @param hotelID �z�e��ID
     * @param planID �v����ID
     * @return true:�\�񂠂�AFalse:�\��Ȃ�
     */
    public static boolean isExistsRsvPlan(int hotelID, int planID) throws Exception
    {
        int cnt = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = query + "SELECT COUNT(*) CNT ";
        query = query + "FROM hh_rsv_reserve ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";
        query = query + "  AND status = 1 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelID );
            prestate.setInt( 2, planID );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.iisExistsRsvPlan] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �I�[�i�[���[�U�[�����擾
     * 
     * @param int userId ���[�U�[ID
     * @param cookie[] cookies
     * @return �I�[�i�[���[�U�[��񂪊i�[����Ă���HashTable
     * @throws Exception
     */
    public static Hashtable<String, String> getOwnerUserData(int userId, Cookie[] cookies)
    {
        String hotenaviId = "";
        String mailAddr = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        Hashtable<String, String> retHash = new Hashtable<String, String>();

        try
        {
            // �z�e�i�rID�擾
            hotenaviId = getCookieLoginHotenavi( cookies );

            String sql = "SELECT * FROM owner_user WHERE hotelid = ? AND userid = ?";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( sql );
            prestate.setString( 1, hotenaviId );
            prestate.setInt( 2, userId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( result.getString( "mailaddr_pc" ) != null )
                {
                    mailAddr = result.getString( "mailaddr_pc" );
                }

                retHash.put( "mailAddr", mailAddr );
                retHash.put( "tel1", "" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getOwnerUserData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(retHash);
    }

    /**
     * �|�C���g�����l�擾
     * 
     * @param int �����敪(1:�|�C���g�擾�A2:�|�C���g�R�[�h�擾�A3:�|�C���g�����t���O�A4:�͈�)
     * @return �Ȃ�
     * @throws Exception
     */
    public static int getInitHapyPoint(int selKbn) throws Exception
    {
        int ret = 0;
        Connection connection = null;

        try
        {
            connection = DBConnection.getConnection();
            ret = getInitHapyPoint( connection, selKbn );
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getInitHapyPoint] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    public static int getInitHapyPoint(Connection connection, int selKbn) throws Exception
    {
        String query = "";
        int ret = 0;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT add_point, code, limit_flag, available_range FROM hh_master_point WHERE kind = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, HAPYPOINT_24 );
            result = prestate.executeQuery();

            while( result.next() )
            {
                switch( selKbn )
                {
                    case 1:
                        // �|�C���g�擾
                        ret = result.getInt( "add_point" );
                        break;

                    case 2:
                        // �|�C���g�R�[�h�擾
                        ret = result.getInt( "code" );
                        break;

                    case 3:
                        // �|�C���g�����t���O�擾
                        ret = result.getInt( "limit_flag" );
                        break;

                    case 4:
                        // �͈͎擾
                        ret = result.getInt( "available_range" );
                        break;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getInitHapyPoint] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * �z�e���C�������f�[�^��ǉ�����
     * 
     * @param id �F�z�e��ID
     * @param hotelId �F�I�[�i�[�z�e��ID
     * @param userId �F���[�UID
     * @param editId �F�C������
     * @param editSub �F�C�����ځi�T�u�j
     * @param memo �F����
     * @return
     * @throws Exception
     */
    public static boolean addAdjustmentHistory(int id, String hotelId, int userId, int editId, int editSub, String memo) throws Exception
    {
        boolean ret = false;
        DataHotelAdjustmentHistory dataAdjust;

        dataAdjust = new DataHotelAdjustmentHistory();
        dataAdjust.setId( id );
        dataAdjust.setHotelId( hotelId );
        dataAdjust.setUserId( userId );
        dataAdjust.setEditId( editId );
        dataAdjust.setEditSub( editSub );
        dataAdjust.setMemo( memo );
        dataAdjust.setInputDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        dataAdjust.setInputTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

        // �����ɒǉ�����
        if ( dataAdjust.insertData() )
        {
            ret = true;
        }
        else
        {
            Logging.error( "[addAdjustmentHistory] Error dataAdjust" );
        }
        return(ret);
    }

    /**
     * Cookie���烍�O�C�����[�UID���擾����
     * 
     * @param cookies �N�b�L�[
     * @return ���O�C�����[�UID
     */
    public static int getCookieLoginUserId(Cookie[] cookies)
    {
        int ret = 0;
        String cookValue = "";
        String value = "";
        int sepalateIdx = 0;

        for( int i = 0 ; cookies != null && i < cookies.length ; i++ )
        {
            // ���O�C�����[�UID�擾
            if ( cookies[i].getName().equals( "hhownuid" ) )
            {
                cookValue = cookies[i].getValue();
                // ��؂蕶����Index���擾
                sepalateIdx = cookValue.indexOf( ":" );
                // ���[�UID�擾
                value = cookValue.substring( sepalateIdx + 1, cookValue.trim().length() );
                ret = Integer.parseInt( value );
                break;
            }
        }
        return ret;
    }

    /**
     * �Ώۗ������[�h�ɗ\���t���̃f�[�^�����݂��邩
     * 
     * @param hotelID �z�e��ID
     * @param planID �v����ID
     * @param chargeModeId �������[�hID
     * @return true:�f�[�^����AFalse:�f�[�^�Ȃ�
     */
    public static boolean existsRsvChargeMode(int hotelID, int planID, int chargeModeId) throws Exception
    {
        int cnt = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = query + "SELECT COUNT(*) CNT ";
        query = query + "FROM hh_rsv_reserve rsv ";
        query = query + "  INNER JOIN  hh_rsv_plan_charge pc ON rsv.id = pc.id AND rsv.plan_id = pc.plan_id ";
        query = query + "WHERE rsv.id = ? ";
        query = query + "  AND rsv.plan_id = ? ";
        query = query + "  AND pc.charge_mode_id = ? ";
        query = query + "  AND rsv.status = 1 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelID );
            prestate.setInt( 2, planID );
            prestate.setInt( 3, chargeModeId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.isExistsRsvChargeMode] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �`�F�b�N�C���A�`�F�b�N�A�E�g���Ԃ̐������`�F�b�N
     * �`�F�b�N�C�����Ԃ�24���𒴂����ꍇ�̂݃`�F�b�N
     * 
     * @param int ciTime �`�F�b�N�C������
     * @param int coTime �`�F�b�N�A�E�g����
     * @return true:�`�F�b�NOK�Afalse:�`�F�b�NNG
     */
    public static boolean checkCiCoTime(int ciTime, int coTime)
    {
        boolean ret = true;
        int ciTimeh = 0;

        if ( ciTime >= 2400 )
        {

            ciTimeh = ciTime - 2400;
            // �`�F�b�N�C�����ԁA�`�F�b�N�A�E�g���Ԃ̔�r
            if ( ciTimeh > coTime )
            {
                ret = false;
            }
        }

        return(ret);
    }

    /**
     * �������擾
     * 
     * @param hotelId �z�e��ID
     * @return ������
     */
    public static int getDeadLineTime(int hotelId)
    {
        int ret = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "";

        try
        {
            query = "select deadline_time from hh_rsv_reserve_basic where id = ?";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            while( result.next() != false )
            {
                ret = result.getInt( "deadline_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getDeadLineTime] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �Ǘ��z�e���̃`�F�b�N
     * 
     * @param hotelId �z�e��ID(�z�e�i�rID)
     * @param userId ���[�UID
     * @param id �z�e��ID(�n�s�z�e)
     * @return true:�Ǘ��z�e���Afalse:�Ǘ��O�̃z�e��
     */
    public static boolean checkHotelID(String hotelId, int userId, int id) throws Exception
    {
        boolean ret = false;

        // �����ǂ��ǂ���
        if ( OwnerRsvCommon.getImediaFlag( hotelId, userId ) == OwnerRsvCommon.IMEDIAFLG_IMEDIA )
        {
            // �����ǂ̏ꍇ
            ret = true;
            return(ret);
        }

        // �����ǈȊO�̏ꍇ�A�S���z�e���̃`�F�b�N
        if ( getHotelIDList( hotelId, userId, id ) == true )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * �Ǘ��҈ȊO�̏ꍇ�A�I�������z�e��ID���Ǘ��z�e�����ǂ���
     * 
     * @param hotelId �z�e��ID(�z�e�i�rID)
     * @param userId ���[�UID
     * @param id �z�e��ID(�n�s�z�e)
     * @return true:�Ǘ��z�e���Afalse:�Ǘ��O�̃z�e��
     */
    private static boolean getHotelIDList(String hotelId, int userId, int id) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int cnt = 0;
        boolean ret = false;

        query = query + "SELECT COUNT(*) AS CNT ";
        query = query + "FROM hh_hotel_basic hb ";
        query = query + "   INNER JOIN owner_user_hotel uh ON hb.hotenavi_id = uh.accept_hotelid ";
        query = query + "WHERE uh.hotelid = ? ";
        query = query + "  AND uh.userid = ? ";
        query = query + "  AND hb.id = ? ";
        query = query + "  AND hb.rank >= 2 ";
        query = query + "ORDER BY hb.hotenavi_id ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            prestate.setInt( 2, userId );
            prestate.setInt( 3, id );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvMenu.getHotelIDList] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �O���l�����v�����p�ꖼ�擾
     * 
     * @param planName �v������
     * @param consecutiveFlag �A���t���O
     * @param minStayNum �ŏ��l��
     * @param maxStayNum �ő�l��
     * @return
     * @throws Exception
     */
    public static String getFroeignPlanName(String planName, int consecutiveFlag, int minStayNum, int maxStayNum) throws Exception
    {
        return getFroeignPlanName( planName, consecutiveFlag, minStayNum, maxStayNum, "" );
    }

    /**
     * �O���l�����v�����p�ꖼ�擾
     * 
     * @param planName �v������
     * @param consecutiveFlag �A���t���O
     * @param minStayNum �ŏ��l��
     * @param maxStayNum �ő�l��
     * @return
     * @throws Exception
     */
    public static String getFroeignPlanName(String planName, int consecutiveFlag, int minStayNum, int maxStayNum, String planSubName) throws Exception
    {
        String planNameEn = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query = "";

        // �v�����T�u�^�C�g���ǉ�
        if ( !planSubName.equals( "" ) )
        {
            planNameEn = planSubName + "/";
        }

        // �^�C�g���擾
        if ( StringUtils.isNotBlank( planName ) )
        {
            String[] id = planName.split( "," );
            connection = DBConnection.getConnection();

            try
            {
                for( int i = 0 ; i < id.length ; i++ )
                {
                    query = "SELECT  val2 nameEn";
                    query += " FROM hh_rsv_system_conf ";
                    query += " WHERE ctrl_id1 = 7 AND  ctrl_id2 = " + id[i];

                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();
                    if ( result.next() != false )
                    {

                        if ( result.getString( "nameEn" ) != null )
                        {
                            planNameEn = planNameEn + result.getString( "nameEn" ) + ",";
                        }
                    }
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );
                }
            }
            catch ( Exception e )
            {
                Logging.info( "foward Exception e=" + e.toString() );
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }
        }

        // �Ō�̃J���}���폜
        if ( planNameEn.length() > 0 )
        {
            if ( planNameEn.substring( planNameEn.length() - 1 ).equals( "," ) )
            {
                planNameEn = planNameEn.substring( 0, planNameEn.length() - 1 );
            }
        }
        return planNameEn;
    }

    /**
     * �A���\��̓��t���Ƃ̗���
     * 
     * @param id �z�e��ID
     * @param reserveNo �\��ԍ�
     * @return
     * @throws Exception
     */
    public static String[][] getRsvEachAmountArr(int id, String reserve_no) throws Exception
    {

        ArrayList<String> ArrDate = new ArrayList<String>();
        ArrayList<String> ArrChargeTotal = new ArrayList<String>();

        String reserveNoMain = "";
        int chargeTotal = 0;
        int chargeTotalPre = 0;

        int rsvDateFrom = 0;
        int rsvDateTo = 0;
        int cnt = 0;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        reserveNoMain = reserve_no.substring( reserve_no.indexOf( "-" ) + 1 );

        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query = "";

        query += "SELECT * FROM newRsvDB.hh_rsv_reserve  ";
        query += " WHERE id = ? ";
        query += " AND reserve_no_main = ? ";
        query += " ORDER BY reserve_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, reserveNoMain );
            result = prestate.executeQuery();

            while( result.next() )
            {
                chargeTotal = result.getInt( "charge_total" );

                if ( chargeTotalPre == 0 || (chargeTotal == chargeTotalPre) )
                {
                    if ( cnt == 0 )
                    {
                        rsvDateFrom = result.getInt( "reserve_date" );
                    }
                    else
                    {
                        rsvDateTo = result.getInt( "reserve_date" );
                    }
                    cnt++;
                }
                else
                {
                    // �o��
                    if ( cnt == 1 )
                    {
                        ArrDate.add( DateEdit.formatDate( 7, rsvDateFrom ) );
                        ArrChargeTotal.add( currencyFormat.format( chargeTotalPre ) );
                    }
                    else
                    {
                        ArrDate.add( DateEdit.formatDate( 7, rsvDateFrom ) + "�`" + DateEdit.formatDate( 7, rsvDateTo ) );
                        ArrChargeTotal.add( currencyFormat.format( chargeTotalPre ) + "�~" + cnt );
                    }
                    // �Z�b�g
                    rsvDateFrom = result.getInt( "reserve_date" );
                    rsvDateTo = 0;
                    cnt = 1;
                }

                chargeTotalPre = chargeTotal;
            }

            // �o��
            if ( rsvDateFrom != 0 )
            {

                if ( cnt == 1 )
                {
                    ArrDate.add( DateEdit.formatDate( 7, rsvDateFrom ) );
                    ArrChargeTotal.add( currencyFormat.format( chargeTotalPre ) );
                }
                else
                {
                    ArrDate.add( DateEdit.formatDate( 7, rsvDateFrom ) + "�`" + DateEdit.formatDate( 7, rsvDateTo ) );
                    ArrChargeTotal.add( currencyFormat.format( chargeTotalPre ) + "�~" + cnt );
                }
            }
            if ( ArrDate.isEmpty() )
            {
                return new String[0][0];
            }
            int len = ArrDate.size();
            String[][] eachAmount = new String[len][2];
            for( int index = 0 ; index < len ; index++ )
            {
                eachAmount[index][0] = ArrDate.get( index );
                eachAmount[index][1] = ArrChargeTotal.get( index );
            }
            return eachAmount;

        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getRsvEachAmountArr] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

    }

    /**
     * �A���\��̓��t���Ƃ̗���
     * 
     * @param id �z�e��ID
     * @param reserveNo �\��ԍ�
     * @return
     * @throws Exception
     */
    public static String getRsvEachAmount(int id, String reserve_no) throws Exception
    {
        String msg = "";
        String[][] eachAmount = getRsvEachAmountArr( id, reserve_no );
        int len = eachAmount.length;
        for( int index = 0 ; index < len ; index++ )
        {
            if ( index != 0 )
            {
                msg += "\n";
            }
            msg += eachAmount[index][0] + " " + eachAmount[index][1];
        }
        return msg;
    }

    /**
     * OTA�pAPI�ďo��
     * 
     * @param api
     * @param param
     * @throws Exception
     */
    public static void callOtaApiThread(final String api, final String param) throws Exception
    {

        ExecutorService service = Executors.newCachedThreadPool();

        try
        {
            service.submit( new Runnable(){

                @Override
                public void run()
                {
                    try
                    {
                        callOtaApi( api, param );
                    }
                    catch ( Exception e )
                    {
                        // TODO �����������ꂽ catch �u���b�N
                        e.printStackTrace();
                    }
                }
            } );
        }
        catch ( Exception e )
        {
            Logging.error( "message : " + e.getMessage(), e );
            throw e;
        }
        finally
        {
            service.shutdown();
        }

    }

    public static JSONObject callOtaApi(final String api, final String param) throws Exception
    {
        Logging.info( "[callOtaApi] api=" + api + " param=" + param, "OwnerRsvCommon" );

        String url = Url.getSiteconApilUrl() + api;
        ClientResponse response =
                getClient()
                        .resource( url )
                        .header( "User-Agent", "PMS-HappyHotel" )
                        .type( MediaType.APPLICATION_FORM_URLENCODED )
                        .post( ClientResponse.class, param );

        Logging.info( "[callOtaApi] Reason Phrase=" + response.getStatusInfo().getReasonPhrase() );
        Logging.info( "[callOtaApi] status=" + response.getStatus() );

        String s = response.getEntity( String.class );
        Logging.info( "[callOtaApi] entity=" + s );

        JSONObject data = JSONObject.fromObject( s );
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader( new InputStreamReader( response.getEntityInputStream() ) );
            Logging.info( "[callOtaApi] reader=" + reader );
            String detailedMessage = data.getString( "detailedMessage" );
            Logging.info( "callOtaApi sucsess:" + data.getBoolean( "success" ) + " message:" + data.getString( "message" ) );
            if ( !data.getBoolean( "success" ) )
            {
                System.out.println( "message : " + data.getString( "message" ) );
            }
        }
        catch ( Exception e )
        {
            data = JSONObject.fromString( "{success : false, msg : \"�p�����[�^������Ɏ擾�ł��܂���ł����B\"}" );
        }
        finally
        {
            if ( reader != null )
                reader.close();
        }

        return data;
    }

    /**
     * SSL�ʐM�pClient���擾
     * 
     * @return
     */
    private static Client getClient()
    {
        DefaultClientConfig config = new DefaultClientConfig();
        config.getProperties().put(
                HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                new HTTPSProperties(
                        new HostnameVerifier()
                        {

                            @Override
                            public boolean verify(String hostname, SSLSession session)
                            {
                                // TODO �����������ꂽ���\�b�h�E�X�^�u
                                return true;
                            }
                        },
                        SSLUtil.context ) );
        return Client.create( config );
    }

    private static class SSLUtil
    {

        static SSLContext context;

        static
        {
            context = getSSLContext();
        }

        private static SSLContext getSSLContext()
        {

            SSLContext context = null;

            try
            {
                // �ؖ������@�S�ċ��Ԃ�
                TrustManager[] tm = { new X509TrustManager()
                {

                    @Override
                    public X509Certificate[] getAcceptedIssuers()
                    {
                        // TODO �����������ꂽ���\�b�h�E�X�^�u
                        return null;
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
                    {
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
                    {
                    }
                } };

                context = SSLContext.getInstance( "SSL" );
                context.init( null, tm, null );

                HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier(){

                    @Override
                    public boolean verify(String arg0, SSLSession arg1)
                    {
                        return true;
                    }
                } );
            }
            catch ( Exception e )
            {
                Logging.error( "" );
            }

            return context;
        }
    }

    /**
     * �����ԍ���OTA�p�v�����Ɏg�p����Ă��邩�`�F�b�N���s��
     * 
     * @param hotelId
     * @param roomRank
     * @param roomSeq
     * @return boolean
     * @throws Exception
     */
    public static boolean chkOtaPlanSeq(int hotelId, int roomRank, int roomSeq) throws Exception
    {
        int count = 0;
        StringBuilder query = new StringBuilder();
        query.append( " SELECT count(*)  as count " );
        // �v�����f�[�^
        query.append( " FROM newRsvDB.hh_rsv_plan plan  " );
        // �v��������
        query.append( " INNER JOIN newRsvDB.hh_rsv_rel_plan_room plan_room " );
        query.append( "   ON plan.id=plan_room.id  " );
        query.append( "   AND plan.plan_id=plan_room.plan_id " );
        query.append( "   AND plan.plan_sub_id=plan_room.plan_sub_id " );

        query.append( " WHERE plan.id = ? " );
        query.append( "  AND plan.foreign_flag= ? " );
        query.append( "  AND plan.latest_flag = ? " );
        query.append( "  AND plan.plan_sales_status = ? " );
        query.append( "  AND plan.publishing_flag = ? " );
        query.append( "  AND plan.sales_end_date >= ? " );
        query.append( "  AND plan_room.seq = ? " );

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, hotelId );
            prestate.setInt( i++, EXT_OTA );
            prestate.setInt( i++, Constants.LATEST_FLAG_LATEST );
            prestate.setInt( i++, PLAN_SALES_STATUS_SALE );
            prestate.setInt( i++, PUBLISHING_FLAG_PUBLISH );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, roomSeq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    count = result.getInt( "count" );
                }
            }
            return count > 0;

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.chkOtaPlanSeq] Exception = " + e.toString(), e );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * OTA�\������Ă���z�e����
     * 
     * @param hotelId �z�e���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public static boolean checkOtaHotel(int hotelId)
    {
        boolean ret = false;
        String query = "SELECT * FROM newRsvDB.hh_rsv_reserve_basic WHERE id = ? ";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    if ( (result.getInt( "rsvota_date_due" ) != 0 || result.getInt( "rsvota_date_start" ) != 0) )
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.checkOtaHotel] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * �Q�X�g�\�� ���[�����M
     * 
     * @param id �z�e��ID
     * @param reserveTempNo ���\��ID
     * @param mailAddr ���[���A�h���X
     * @param userId ���[�U�[ID
     * @param carAf Line�Z�b�V�������
     * @param mode 1:���[������reserve_guest_payment_go.jsp�֑J�� 2:���[������reserve_mobile_guest_payment_go.jsp�֑J��
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public static boolean sendRsvGuestMail(int id, long reserveTempNo, String mailAddr, String userId, String carAf)
    {
        return sendRsvGuestMail( id, reserveTempNo, mailAddr, userId, carAf, 1 );
    }

    public static boolean sendRsvGuestMail(int id, long reserveTempNo, String mailAddr, String userId, String carAf, int mode)
    {
        boolean rtn = true;

        DataHhRsvSystemConfList sysConfList = new DataHhRsvSystemConfList();
        HashMap<String, String> map = sysConfList.getSystemConfMap( 13 ); // ���[������hh_rsv_system_conf����擾����B

        String MAIL_FROM = "mail.from";
        String MAIL_SUBJECT = "mail.subject";

        try
        {
            String encdata = URLEncoder.encode( "���₢���킹", "Shift_JIS" );

            String url = "";
            if ( mode == 1 )
            {
                url = Url.getRsvUrl() + "/others/reserve_guest_payment_go.jsp?";
            }
            else
            {
                url = Url.getRsvUrl() + "/others/reserve_mobile_guest_payment_go.jsp?";
            }
            url += "token=" + ReserveGuestMailToken.issue( id, reserveTempNo );// �p�����[�^���g�[�N����
            if ( CheckString.isvalidString( carAf ) )
            {
                url += "&_car-af=" + carAf;
            }
            Logging.info( "url=" + url, "OwnerRsvCommon.sendRsvGuestMail" );

            String body = "";
            body = "----------------------------------------------------------------------------\r\n";
            body += "���̃��[���́A���̓��[���A�h���X���Ɏ����I�ɂ����肵�Ă��܂��B\r\n";
            body += "---------------------------------------------------------------------------\r\n";
            body += "�܂��\��͊m�肵�Ă��܂���B ���L��URL���N���b�N���Ďx����ʂɂ��i�݂��������B\r\n";
            body += url;
            body += "\r\n\r\n������\r\n";
            body += "���{���[����URL�̗L��������10���ȓ��ƂȂ�܂��B" + "\r\n";
            body += "���{���[���ɂ��S������̂Ȃ��ꍇ�́A�{���[���̔j�������肢�������܂��B" + "\r\n";
            body += "\r\n";
            body += "���₢���킹" + "\r\n";
            body += "mailto:" + map.get( MAIL_FROM ) + "?subject=" + encdata + "\r\n";
            body += "\r\n";
            body += "�n�b�s�[�E�z�e����USEN-NEXT�O���[�v�̊�����ЃA�����b�N�X���^�c����" + "\r\n";
            body += "���W���[�z�e�������T�C�g�ł��B" + "\r\n";
            body += "\r\n";
            body += "�n�b�s�[�E�z�e��URL" + "\r\n";
            body += Url.getUrl() + "" + "\r\n";

            // body = new String( body.getBytes( "Shift_JIS" ) );
            // ������������̂ōŌ�ɋ󔒒ǉ�
            body += " ";

            // ���[�����M���s��
            SendMail.send( map.get( MAIL_FROM ), mailAddr, map.get( MAIL_SUBJECT ), body );
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.sendRsvGuestMail] Exception=" + e.toString(), e );
            rtn = false;
        }

        return rtn;

    }
}