package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataRsvReserve;

/**
 * 
 * �\��ꗗ��� business Logic
 */
public class LogicOwnerRsvList implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID      = -115937955416856550L;
    private static final int    listmax               = 20;                      // ���ʍő喾�ו\������
    private static final String sts1                  = "���X�҂�";
    private static final String sts2                  = "���X";
    private static final String sts3                  = "�L�����Z��";
    private static final String sts4                  = "<br>(�����X)";
    private String              errMsg                = "";
    private int                 hotelId               = 0;
    private int                 maxCnt                = 0;
    private String              dateFROM              = "";
    private String              dateTO                = "";
    private String              rsvNo                 = "";
    private ArrayList<Integer>  idList                = new ArrayList<Integer>(); // �z�e��ID
    private ArrayList<String>   optionList            = new ArrayList<String>(); // �I�v�V����
    private ArrayList<String>   planNmList            = new ArrayList<String>(); // �v������
    private ArrayList<String>   reserveNoList         = new ArrayList<String>(); // �\��ԍ�
    private ArrayList<String>   rsvDateList           = new ArrayList<String>(); // �\���
    private ArrayList<Integer>  rsvDateValList        = new ArrayList<Integer>(); // �\���
    private ArrayList<Integer>  seqList               = new ArrayList<Integer>(); // �����ԍ�
    private ArrayList<String>   userIdList            = new ArrayList<String>(); // ���[�UID
    private ArrayList<String>   userNmList            = new ArrayList<String>(); // ���p�Җ�
    private ArrayList<String>   tel1List              = new ArrayList<String>(); // �A����
    private ArrayList<String>   estTimeArrivalList    = new ArrayList<String>(); // �����\�莞��
    private ArrayList<Integer>  estTimeArrivalValList = new ArrayList<Integer>(); // �����\�莞��
    private ArrayList<String>   statusList            = new ArrayList<String>(); // �X�e�[�^�X
    private ArrayList<Boolean>  historyFlgList        = new ArrayList<Boolean>(); // ����L�����X�g
    private ArrayList<Integer>  statusValList         = new ArrayList<Integer>(); // �X�e�[�^�X
    private ArrayList<Integer>  dspList               = new ArrayList<Integer>(); // �񋟋敪
    private ArrayList<Integer>  noShowList            = new ArrayList<Integer>(); // �m�[�V���E���X�g�i�����Ώۃ��X�g�j
    private ArrayList<Integer>  reserveSubNoList      = new ArrayList<Integer>(); // �\��}�ԃ��X�g
    private ArrayList<Integer>  acceptDateList        = new ArrayList<Integer>(); // ��t�����X�g
    private ArrayList<Integer>  acceptTimeList        = new ArrayList<Integer>(); // ��t�������X�g
    private ArrayList<Integer>  rsvCountList          = new ArrayList<Integer>(); // �\�񌏐����X�g
    private ArrayList<Integer>  paymentList           = new ArrayList<Integer>(); // ���ϕ��@���X�g
    private ArrayList<Integer>  paymentStatusList     = new ArrayList<Integer>(); // ���σX�e�[�^�X���X�g
    private ArrayList<String>   chargeTotalList       = new ArrayList<String>(); // �\����z���X�g

    /**
     * ��ʓ��͍��ڃ`�F�b�N
     * 
     * @param request
     * @param frm
     * @return
     * @throws Exception
     */
    public boolean chkDsp(HttpServletRequest request) throws Exception
    {
        boolean isResult;
        String dateF = "";
        String dateT = "";
        String rsvNo = "";
        int fromVal = 0;
        int toVal = 0;

        // �߂�l�̏�����
        isResult = true;

        try
        {

            // �J�n��
            if ( (request.getParameter( "date_f" ) == null) || (request.getParameter( "date_f" ).toString().length() == 0) )
            {
                errMsg = "";
            }
            else
            {
                dateF = request.getParameter( "date_f" );
                if ( DateEdit.checkDate( dateF ) )
                {
                    // ���t�Ƃ��Đ������ꍇ
                }
                else
                {
                    errMsg = errMsg + Message.getMessage( "warn.00009", "�J�n��" ) + "<br />";
                    isResult = false;
                }
            }
            // �I����
            if ( (request.getParameter( "date_t" ) == null) || (request.getParameter( "date_t" ).toString().length() == 0) )
            {
                errMsg = errMsg + "";
            }
            else
            {
                dateT = request.getParameter( "date_t" );
                if ( DateEdit.checkDate( dateT ) )
                {
                    // ���t�Ƃ��Đ������ꍇ
                }
                else
                {
                    errMsg = errMsg + Message.getMessage( "warn.00009", "�I����" ) + "<br />";
                    isResult = false;
                }
            }

            if ( isResult )
            {
                // �͈̓`�F�b�N
                if ( (request.getParameter( "date_f" ) != null) && (request.getParameter( "date_f" ).toString().length() != 0) &&
                        (request.getParameter( "date_t" ) != null) && (request.getParameter( "date_t" ).toString().length() != 0) )
                {
                    fromVal = Integer.parseInt( dateF.replace( "/", "" ) );
                    toVal = Integer.parseInt( dateT.replace( "/", "" ) );
                    if ( fromVal > toVal )
                    {
                        errMsg = errMsg + Message.getMessage( "warn.00012" ) + "<br />";
                        isResult = false;
                    }
                }
            }

            // �\��ԍ�
            if ( (request.getParameter( "rsv_no" ) == null) || (request.getParameter( "rsv_no" ).toString().length() == 0) )
            {
            }
            else
            {
                rsvNo = request.getParameter( "rsv_no" );
                if ( CheckString.numAlphaCheck( rsvNo ) == false )
                {
                    // ���p�p�����łȂ��ꍇ�̓G���[
                    errMsg = errMsg + Message.getMessage( "warn.30007", "�\��ԍ�", "���p�p����" ) + "<br />";
                    isResult = false;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveListPC.chkDsp] Exception=" + e.toString() );
            throw e;
        }

        // �߂�l
        return isResult;
    }

    /**
     * �I���z�e���̏��𓾂�
     * 
     * @param frm
     * @return
     * @throws Exception
     */
    public boolean getHotelInfo(FormOwnerRsvList frm) throws Exception
    {
        boolean isResult;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // �߂�l�̏�����
        isResult = false;
        try
        {
            query = "SELECT hotenavi_id, name from hh_hotel_basic where id = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // �S�����o
                if ( result.next() )
                {
                    frm.setSelHotenaviID( result.getString( "hotenavi_id" ) );
                    frm.setSelHotelName( result.getString( "name" ) );
                    isResult = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveListPC.getHotelInfo] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return isResult;
    }

    /**
     * �\��ύX�������L������
     * 
     * @param id �z�e��ID
     * @param reserveNo �\��ԍ�
     * @return true���ύX��������, false���ύX�����Ȃ�
     * @throws Exception
     */
    private boolean checkRserveHistory(int id, String reserveNo) throws Exception
    {
        boolean ret = false;

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = "SELECT COUNT(t.reserve_no) as historyCount FROM ";
            query += "(SELECT id,reserve_no from hh_rsv_reserve_history where id = ? and reserve_no = ?";
            query += " UNION ALL SELECT id,reserve_no from newRsvDB.hh_rsv_reserve_history where id = ? AND reserve_no = ?)t ";
            query += " GROUP BY id,reserve_no";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, reserveNo );
            prestate.setInt( 3, id );
            prestate.setString( 4, reserveNo );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // ���𐔂�1��葽���ꍇ�͗�������ŕԂ�
                if ( result.next() && result.getInt( "historyCount" ) > 1 )
                {
                    ret = true;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveListPC.checkRserveHistory] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �\�񗚗����擾
     * 
     * @param frm // �ҏW��̫��id
     * @param id �z�e��ID
     * @param reserveNo �\��ԍ�
     * @return
     * @throws Exception
     */
    public boolean getHistoryData(FormOwnerRsvList frm, int id, String reserveNo) throws Exception
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String convDate = "";
        String weekName = "";
        String estTime = "";

        try
        {
            query = "SELECT hh_rsv_reserve_history.id, hh_rsv_reserve_history.reserve_no, hh_rsv_reserve_history.reserve_sub_no, hh_rsv_reserve_history.reserve_date, " +
                    " hh_rsv_reserve_history.name_last, hh_rsv_reserve_history.name_first, hh_rsv_reserve_history.option_charge_total, " +
                    " hh_rsv_reserve_history.seq, hh_rsv_plan.plan_name, hh_rsv_reserve_history.est_time_arrival , hh_rsv_reserve_history.accept_date, hh_rsv_reserve_history.accept_time, " +
                    " hh_rsv_reserve_history.user_id, hh_rsv_reserve_history.status, hh_rsv_plan.offer_kind, " +
                    " hh_rsv_reserve_history.temp_coming_flag, hh_rsv_reserve_history.tel1, hh_rsv_reserve_history.noshow_flag" +
                    " FROM hh_rsv_reserve_history INNER JOIN hh_rsv_plan " +
                    " ON ( hh_rsv_reserve_history.id = hh_rsv_plan.id AND hh_rsv_reserve_history.plan_id = hh_rsv_plan.plan_id ) " +
                    " WHERE hh_rsv_reserve_history.id = ? AND hh_rsv_reserve_history.reserve_no = ? order by hh_rsv_reserve_history.accept_date, hh_rsv_reserve_history.accept_time";
            query += " UNION SELECT hh_rsv_reserve_history.id, hh_rsv_reserve_history.reserve_no, hh_rsv_reserve_history.reserve_sub_no, hh_rsv_reserve_history.reserve_date, " +
                    " hh_rsv_reserve_history.name_last, hh_rsv_reserve_history.name_first, hh_rsv_reserve_history.option_charge_total, " +
                    " hh_rsv_reserve_history.seq, hh_rsv_plan.plan_name, hh_rsv_reserve_history.est_time_arrival , hh_rsv_reserve_history.accept_date, hh_rsv_reserve_history.accept_time, " +
                    " hh_rsv_reserve_history.user_id, hh_rsv_reserve_history.status, hh_rsv_plan.offer_kind, " +
                    " hh_rsv_reserve_history.temp_coming_flag, hh_rsv_reserve_history.tel1, hh_rsv_reserve_history.noshow_flag" +
                    " FROM newRsvDB.hh_rsv_reserve_history INNER JOIN newRsvDB.hh_rsv_plan " +
                    " ON ( hh_rsv_reserve_history.id = hh_rsv_plan.id AND hh_rsv_reserve_history.plan_id = hh_rsv_plan.plan_id AND hh_rsv_reserve_history.plan_sub_id = hh_rsv_plan.plan_sub_id ) " +
                    " WHERE hh_rsv_reserve_history.id = ? AND hh_rsv_reserve_history.reserve_no = ? order by hh_rsv_reserve_history.accept_date, hh_rsv_reserve_history.accept_time";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, reserveNo );
            result = prestate.executeQuery();

            while( result.next() != false )
            {
                frm.setIdList( result.getInt( "id" ) ); // �z�e��ID
                convDate = String.valueOf( result.getInt( "reserve_date" ) );
                weekName = DateEdit.getWeekName( result.getInt( "reserve_date" ) );
                frm.setRsvDateList( convDate.substring( 0, 4 ) + "/" + convDate.substring( 4, 6 ) + "/" + convDate.substring( 6, 8 ) +
                        "(" + weekName + ")" );
                frm.setRsvDateValList( result.getInt( "reserve_date" ) );
                frm.setSeqList( result.getInt( "seq" ) );
                if ( result.getInt( "option_charge_total" ) == 0 )
                {
                    frm.setOptionList( "��" );
                }
                else
                {
                    frm.setOptionList( "�L" );
                }
                frm.setUserNmList( ConvertCharacterSet.convDb2Form( result.getString( "name_last" ).toString() ) +
                        ConvertCharacterSet.convDb2Form( result.getString( "name_first" ).toString() ) );
                frm.setTel1List( ConvertCharacterSet.convDb2Form( result.getString( "tel1" ).toString() ) );
                frm.setPlanNmList( ConvertCharacterSet.convDb2Form( result.getString( "plan_name" ).toString() ) );
                frm.setReserveNoList( result.getString( "reserve_no" ) );

                estTime = ConvertTime.convTimeStr( result.getInt( "est_time_arrival" ), 3 );
                frm.setEstTimeArrivalList( estTime );
                frm.setEstTimeArrivalValList( result.getInt( "est_time_arrival" ) );

                frm.setUserIdList( result.getString( "user_id" ).toString() );

                if ( result.getInt( "status" ) == 1 )
                {
                    frm.setStatusList( result.getInt( "temp_coming_flag" ) == 1 ? sts1 + sts4 : sts1 );
                }
                else if ( result.getInt( "status" ) == 2 )
                {
                    frm.setStatusList( result.getInt( "temp_coming_flag" ) == 1 ? sts2 + sts4 : sts2 );
                }
                else if ( result.getInt( "status" ) == 3 )
                {
                    frm.setStatusList( result.getInt( "temp_coming_flag" ) == 1 ? sts3 + sts4 : sts3 );
                }
                frm.setStatusValList( result.getInt( "status" ) );

                frm.setDspList( result.getInt( "offer_kind" ) );
                frm.setNoShowList( result.getInt( "noshow_flag" ) );

                frm.setReserveSubNoList( result.getInt( "reserve_sub_no" ) );
                convDate = String.valueOf( result.getInt( "accept_date" ) );
                weekName = DateEdit.getWeekName( result.getInt( "accept_date" ) );
                estTime = ConvertTime.convTimeStr( result.getInt( "accept_time" ), 3 );
                frm.setAcceptDateList( convDate.substring( 0, 4 ) + "/" + convDate.substring( 4, 6 ) + "/" + convDate.substring( 6, 8 ) + "(" + weekName + ")" );
                frm.setAcceptTimeList( estTime );
            }
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveListPC.getHistoryData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �\����擾
     * 
     * @param frm // �ҏW��̫��id
     * @param objKbn // ���o�Ώ�
     * @param pageCnt // �y�[�W�ԍ�(0����)
     * @param mode // �y�[�W���O�@�\(ALL:�y�[�W���O�����S��, PARTS:�y�[�W���O�L)
     * @return
     * @throws Exception
     */
    public boolean getData(FormOwnerRsvList frm, int objKbn, int pageCnt, String mode) throws Exception
    {

        boolean isResult;
        String query = "";
        String queryNewRsv = "";
        String queryWhere = "";
        String queryOrder = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String convDate = "";
        String weekName = "";
        int stLine = 0;
        int enLine = 0;
        int i = 0;
        int intSt = 0;
        int intEn = 0;
        int rsvDateF = 0;
        int rsvDateT = 0;
        int intMaxCnt = 0;
        String estTime = "";
        int seq = 0;

        // �߂�l�̏�����
        isResult = false;

        // �A�����A�e�f�[�^�擾�̂���
        DataRsvReserve drr = new DataRsvReserve();

        // �����̕ҏW
        if ( (this.dateFROM == null) || (this.dateFROM.toString().length() == 0) )
        {
            rsvDateF = 0;
        }
        else
        {
            convDate = this.dateFROM.replace( "/", "" );
            rsvDateF = Integer.parseInt( convDate );
        }
        if ( (this.dateTO == null) || (this.dateTO.toString().length() == 0) )
        {
            rsvDateT = 0;
        }
        else
        {
            convDate = this.dateTO.replace( "/", "" );
            rsvDateT = Integer.parseInt( convDate );
        }

        try
        {
            queryNewRsv = "SELECT hh_rsv_reserve.id, hh_rsv_reserve.reserve_no, hh_rsv_reserve.reserve_date, " +
                    " hh_rsv_reserve.name_last, hh_rsv_reserve.name_first, hh_rsv_reserve.charge_total, hh_rsv_reserve.option_charge_total, " +
                    " CASE WHEN hh_rsv_reserve.room_hold=0 THEN hh_rsv_reserve.seq ELSE hh_rsv_reserve.room_hold END AS seq, hh_rsv_plan.plan_type, hh_rsv_plan.plan_name, hh_rsv_reserve.est_time_arrival, hh_rsv_reserve.reserve_date_to, " +
                    " hh_rsv_reserve.user_id, hh_rsv_reserve.status, 0 AS offer_kind, " +
                    " hh_rsv_reserve.temp_coming_flag, hh_rsv_reserve.tel1, hh_rsv_reserve.noshow_flag," +
                    " hh_rsv_reserve.payment, hh_rsv_reserve.payment_status,hh_rsv_reserve.used_mile," +
                    " hh_rsv_reserve.reserve_no_main, hh_rsv_reserve.accept_date," +
                    " hh_rsv_plan.foreign_flag,hh_rsv_plan.consecutive_flag,hh_rsv_plan.plan_sub_name,hh_rsv_plan.max_stay_num,hh_rsv_plan.min_stay_num,hh_rsv_plan.local_payment_kind" +
                    " FROM newRsvDB.hh_rsv_reserve INNER JOIN newRsvDB.hh_rsv_plan " +
                    " ON ( hh_rsv_reserve.id = hh_rsv_plan.id AND hh_rsv_reserve.plan_id = hh_rsv_plan.plan_id AND hh_rsv_reserve.plan_sub_id = hh_rsv_plan.plan_sub_id ) " +
                    " WHERE hh_rsv_reserve.id = ? ";
            if ( objKbn == 1 )
            {
                // �\����̂�
                queryWhere = " AND hh_rsv_reserve.status = 1 ";
            }
            else if ( objKbn == 2 )
            {
                // �������̂�
                queryWhere = " AND hh_rsv_reserve.status = 2 ";
            }
            else if ( objKbn == 3 )
            {
                // �L�����Z�����̂�
                queryWhere = " AND hh_rsv_reserve.status = 3 ";
            }
            else if ( objKbn == 4 )
            {
                // �����X���̂�
                queryWhere = " AND hh_rsv_reserve.temp_coming_flag = 1 ";
            }
            else
            {
                // �S�Ă̏ꍇ�͏�������
            }
            // ���t�J�n
            if ( rsvDateF != 0 )
            {
                queryWhere = queryWhere + " AND hh_rsv_reserve.reserve_date >= ? ";
            }
            // ���t�I��
            if ( rsvDateT != 0 )
            {
                queryWhere = queryWhere + " AND hh_rsv_reserve.reserve_date <= ? ";
            }

            if ( !this.rsvNo.equals( "" ) )
            {
                queryWhere = queryWhere + " AND hh_rsv_reserve.reserve_no LIKE ? ";
            }

            // �o�͏��͗\����A�����\�莞����
            queryOrder = " ORDER BY id, reserve_date, est_time_arrival, reserve_no";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( queryNewRsv + queryWhere + queryOrder );

            prestate.setInt( 1, this.hotelId );
            seq = 2;

            if ( rsvDateF != 0 )
            {
                prestate.setInt( seq++, rsvDateF );
            }
            if ( rsvDateT != 0 )
            {
                prestate.setInt( seq++, rsvDateT );
            }

            if ( !this.rsvNo.equals( "" ) )
            {
                prestate.setString( seq++, "%" + this.rsvNo + "%" );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {

                // �S�����o
                while( result.next() )
                {
                    this.setIdList( result.getInt( "id" ) ); // �z�e��ID
                    convDate = String.valueOf( result.getInt( "reserve_date" ) );
                    weekName = DateEdit.getWeekName( result.getInt( "reserve_date" ) );
                    this.setRsvDateList( convDate.substring( 0, 4 ) + "/" + convDate.substring( 4, 6 ) + "/" + convDate.substring( 6, 8 ) +
                            "(" + weekName + ")" );
                    this.setSeqList( result.getInt( "seq" ) );
                    if ( result.getInt( "option_charge_total" ) == 0 )
                    {
                        this.setOptionList( "��" );
                    }
                    else
                    {
                        this.setOptionList( "�L" );
                    }
                    this.setUserNmList( ConvertCharacterSet.convDb2Form( result.getString( "name_last" ).toString() ) +
                            ConvertCharacterSet.convDb2Form( result.getString( "name_first" ).toString() ) );

                    String smsPhoneNo = "";
                    if ( result.getInt( "payment" ) == 2 // ���n����
                            && result.getInt( "local_payment_kind" ) == 2 ) // SMS�F�؂�����
                    {
                        smsPhoneNo = ReserveCommon.getSmsPhoneNo( connection, result.getString( "user_id" ), result.getInt( "accept_date" ) );
                    }
                    this.setTel1List( smsPhoneNo.equals( "" ) ? result.getString( "tel1" ) : smsPhoneNo + "(S)" );

                    if ( result.getInt( "foreign_flag" ) == 1 )
                    {
                        this.setPlanNmList( "LIJ " + OwnerRsvCommon.getFroeignPlanName( result.getString( "plan_name" ).toString(), result.getInt( "consecutive_flag" ), result.getInt( "min_stay_num" ), result.getInt( "max_stay_num" ),
                                result.getString( "plan_sub_name" ) ) );
                    }
                    else if ( result.getInt( "foreign_flag" ) == 2 )
                    {
                        this.setPlanNmList( "[E]" + ConvertCharacterSet.convDb2Form( result.getString( "plan_name" ).toString() ) );
                    }
                    else if ( result.getInt( "plan_type" ) == 2 || result.getInt( "plan_type" ) == 4 )
                    {
                        this.setPlanNmList( "[�x]" + ConvertCharacterSet.convDb2Form( result.getString( "plan_name" ).toString() ) );
                    }
                    else
                    {
                        this.setPlanNmList( ConvertCharacterSet.convDb2Form( result.getString( "plan_name" ).toString() ) );
                    }
                    // Logging.info( "LogicReserveListPC.getData foreign_flg=" + result.getInt( "foreign_flag" ) + ",planName=" + result.getString( "plan_name" ) );
                    // Logging.info( "LogicReserveListPC.getData reserve_no_main=" + result.getString( "reserve_no_main" ) );

                    estTime = ConvertTime.convTimeStr( result.getInt( "est_time_arrival" ), 3 );

                    if ( !result.getString( "reserve_no_main" ).equals( "" ) && !result.getString( "reserve_no" ).equals( "A" + hotelId + "-" + result.getString( "reserve_no_main" ) ) )
                    {
                        if ( drr.getData( connection, hotelId, "A" + hotelId + "-" + result.getString( "reserve_no_main" ) ) != false )
                        {
                            this.setReserveNoList( drr.getReserveNo() );// �\��No�͐e�̗\��No
                            estTime = drr.getReserveDate() / 100 % 100 + "/" + drr.getReserveDate() % 100 + " " + estTime;
                            this.setRsvDateValList( drr.getReserveDate() );
                        }
                        else
                        {
                            this.setReserveNoList( result.getString( "reserve_no" ) );
                            this.setRsvDateValList( result.getInt( "reserve_date" ) );
                        }
                    }
                    else
                    {
                        this.setReserveNoList( result.getString( "reserve_no" ) );
                        this.setRsvDateValList( result.getInt( "reserve_date" ) );
                        if ( result.getInt( "reserve_date" ) != result.getInt( "reserve_date_to" ) && result.getInt( "reserve_date_to" ) != 0 )
                        {
                            estTime = "�A�� " + estTime;
                        }
                    }

                    this.setEstTimeArrivalList( estTime );
                    this.setEstTimeArrivalValList( result.getInt( "est_time_arrival" ) );

                    this.setUserIdList( result.getString( "user_id" ).toString() );

                    if ( result.getInt( "status" ) == 1 )
                    {
                        this.setStatusList( result.getInt( "temp_coming_flag" ) == 1 ? sts1 + sts4 : sts1 );
                    }
                    else if ( result.getInt( "status" ) == 2 )
                    {
                        this.setStatusList( result.getInt( "temp_coming_flag" ) == 1 ? sts2 + sts4 : sts2 );
                    }
                    else if ( result.getInt( "status" ) == 3 )
                    {
                        this.setStatusList( result.getInt( "temp_coming_flag" ) == 1 ? sts3 + sts4 : sts3 );
                    }
                    this.setStatusValList( result.getInt( "status" ) );

                    this.setDspList( result.getInt( "offer_kind" ) );
                    this.setNoShowList( result.getInt( "noshow_flag" ) );
                    this.setPaymentList( result.getInt( "payment" ) );
                    this.setPaymentStatusList( result.getInt( "payment_status" ) );
                    NumberFormat objNum = NumberFormat.getCurrencyInstance();
                    String cnvMoney = objNum.format( result.getInt( "charge_total" ) - result.getInt( "used_mile" ) );
                    this.setChargeTotalList( cnvMoney );
                    this.setHistoryFlgList( checkRserveHistory( result.getInt( "id" ), result.getString( "reserve_no" ) ) );

                    // ��������
                    intMaxCnt++;
                }

                if ( mode.equals( "ALL" ) )
                {
                    // �S����ʕ\���̏ꍇ�͂��̂܂܃t�H�[���֕ҏW����
                    for( i = 0 ; i < intMaxCnt - 1 ; i++ )
                    {
                        frm.setIdList( this.idList.get( i ) );
                        frm.setRsvDateList( this.rsvDateList.get( i ) );
                        frm.setRsvDateValList( this.rsvDateValList.get( i ) );
                        frm.setSeqList( this.seqList.get( i ) );
                        frm.setOptionList( this.optionList.get( i ) );
                        frm.setUserNmList( this.userNmList.get( i ) );
                        frm.setTel1List( this.tel1List.get( i ) );
                        frm.setPlanNmList( this.planNmList.get( i ) );
                        frm.setReserveNoList( this.reserveNoList.get( i ) );
                        frm.setEstTimeArrivalList( this.estTimeArrivalList.get( i ) );
                        frm.setEstTimeArrivalValList( this.estTimeArrivalValList.get( i ) );
                        frm.setUserIdList( this.userIdList.get( i ) );
                        frm.setStatusList( this.statusList.get( i ) );
                        frm.setDspList( this.dspList.get( i ) );
                        frm.setStatusValList( this.statusValList.get( i ) );
                        frm.setNoShowList( this.noShowList.get( i ) );
                        frm.setPaymentList( this.paymentList.get( i ) );
                        frm.setPaymentStatusList( this.paymentStatusList.get( i ) );
                        frm.setChargeTotalList( this.chargeTotalList.get( i ) );
                        frm.setHistoryFlg( this.historyFlgList.get( i ) );
                    }

                    isResult = true;
                    frm.setPageMax( intMaxCnt - 1 );
                    frm.setPageSt( 0 );
                    frm.setPageEd( intMaxCnt - 1 );
                }
                else if ( mode.equals( "PARTS" ) )
                {

                    // ���o���������̂����ꕔ���̂݃t�H�[���֕ҏW����
                    Logging.error( "��" );
                    if ( intMaxCnt != 0 )
                    {
                        frm.setRecCnt( intMaxCnt );

                        if ( pageCnt == 0 )
                        {
                            stLine = 0;
                            if ( listmax > intMaxCnt )
                            {
                                enLine = intMaxCnt - 1;
                            }
                            else
                            {
                                enLine = listmax - 1;
                            }
                            // frm.setPageAct( 1 );
                        }
                        else
                        {
                            stLine = (listmax * pageCnt) + 1;
                            enLine = (listmax * (pageCnt + 1));
                            // frm.setPageAct( pageCnt - 1 );
                        }

                        if ( enLine > intMaxCnt )
                        {
                            enLine = intMaxCnt;
                        }

                        // �\���Ώۃy�[�W���̂�
                        if ( stLine == 0 )
                        {
                            intSt = 0;
                            if ( listmax > intMaxCnt )
                            {
                                intEn = intMaxCnt - 1;
                            }
                            else
                            {
                                intEn = listmax - 1;
                            }
                            frm.setPageSt( stLine + 1 );
                            frm.setPageEd( enLine + 1 );
                        }
                        else
                        {
                            intSt = stLine - 1;
                            intEn = enLine - 1;
                            frm.setPageSt( stLine );
                            frm.setPageEd( enLine );
                        }
                        for( i = intSt ; i <= intEn ; i++ )
                        {
                            frm.setIdList( this.idList.get( i ) );
                            frm.setRsvDateList( this.rsvDateList.get( i ) );
                            frm.setRsvDateValList( this.rsvDateValList.get( i ) );
                            frm.setSeqList( this.seqList.get( i ) );
                            frm.setOptionList( this.optionList.get( i ) );
                            frm.setUserNmList( this.userNmList.get( i ) );
                            frm.setTel1List( this.tel1List.get( i ) );
                            frm.setPlanNmList( this.planNmList.get( i ) );
                            frm.setReserveNoList( this.reserveNoList.get( i ) );
                            frm.setEstTimeArrivalList( this.estTimeArrivalList.get( i ) );
                            frm.setEstTimeArrivalValList( this.estTimeArrivalValList.get( i ) );
                            frm.setUserIdList( this.userIdList.get( i ) );
                            frm.setStatusList( this.statusList.get( i ) );
                            frm.setDspList( this.dspList.get( i ) );
                            frm.setStatusValList( this.statusValList.get( i ) );
                            frm.setNoShowList( this.noShowList.get( i ) );
                            frm.setHistoryFlg( this.historyFlgList.get( i ) );
                        }

                        isResult = true;
                        frm.setPageMax( intMaxCnt );
                        frm.setRecCnt( intMaxCnt );
                    }
                    else
                    {
                        frm.setRecCnt( 0 );
                        frm.setPageMax( 0 );
                        isResult = false;
                    }
                }
            }
            else
            {
                // not Found
                isResult = false;
                // ���o����
                frm.setPageMax( intMaxCnt );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveListPC.getData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return isResult;
    }

    /**
     * �\����擾
     * 
     * @param frm // �ҏW��̫��id
     * @param objKbn // ���o�Ώ�
     * @return
     * @throws Exception
     */
    public boolean getDailyCount(FormOwnerRsvList frm, int objKbn) throws Exception
    {

        boolean isResult;
        String query = "";
        String queryNewRsv = "";
        String queryWhere = "";
        String queryOrder = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String convDate = "";
        int i = 0;
        int rsvDateF = 0;
        int rsvDateT = 0;
        int intMaxCnt = 0;
        int seq = 0;

        // �߂�l�̏�����
        isResult = false;

        // �����̕ҏW
        if ( (this.dateFROM == null) || (this.dateFROM.toString().length() == 0) )
        {
            rsvDateF = 0;
        }
        else
        {
            convDate = this.dateFROM.replace( "/", "" );
            rsvDateF = Integer.parseInt( convDate );
        }
        if ( (this.dateTO == null) || (this.dateTO.toString().length() == 0) )
        {
            rsvDateT = 0;
        }
        else
        {
            convDate = this.dateTO.replace( "/", "" );
            rsvDateT = Integer.parseInt( convDate );
        }

        try
        {
            query = "SELECT hh_rsv_reserve.id, hh_rsv_reserve.reserve_date, COUNT(hh_rsv_reserve.id) AS rsv_count " +
                    " FROM hh_rsv_reserve INNER JOIN hh_rsv_plan " +
                    " ON ( hh_rsv_reserve.id = hh_rsv_plan.id AND hh_rsv_reserve.plan_id = hh_rsv_plan.plan_id ) " +
                    " WHERE hh_rsv_reserve.id = ? ";
            queryNewRsv = " UNION SELECT hh_rsv_reserve.id, hh_rsv_reserve.reserve_date, COUNT(hh_rsv_reserve.id) AS rsv_count " +
                    " FROM newRsvDB.hh_rsv_reserve INNER JOIN newRsvDB.hh_rsv_plan " +
                    " ON ( hh_rsv_reserve.id = hh_rsv_plan.id AND hh_rsv_reserve.plan_id = hh_rsv_plan.plan_id AND hh_rsv_reserve.plan_sub_id = hh_rsv_plan.plan_sub_id ) " +
                    " WHERE hh_rsv_reserve.id = ? ";
            if ( objKbn == 1 )
            {
                // �\����̂�
                queryWhere = " AND hh_rsv_reserve.status = 1 ";
            }
            else if ( objKbn == 2 )
            {
                // �������̂�
                queryWhere = " AND hh_rsv_reserve.status = 2 ";
            }
            else if ( objKbn == 3 )
            {
                // �L�����Z�����̂�
                queryWhere = " AND hh_rsv_reserve.status = 3 ";
            }
            else if ( objKbn == 4 )
            {
                // �����X���̂�
                queryWhere = " AND hh_rsv_reserve.temp_coming_flag = 1 ";
            }
            else if ( objKbn == 5 )
            {
                // ���X�҂��A���X�̂�
                queryWhere = " AND hh_rsv_reserve.status BETWEEN 1 AND 2 ";
            }
            else
            {
                // �S�Ă̏ꍇ�͏�������
            }
            // ���t�J�n
            if ( rsvDateF != 0 && rsvDateT != 0 )
            {
                queryWhere = queryWhere + " AND hh_rsv_reserve.reserve_date BETWEEN ? AND ? ";
            }
            queryWhere += " GROUP BY hh_rsv_reserve.id, hh_rsv_reserve.reserve_date ";
            queryOrder = " ORDER BY id, reserve_date ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query + queryWhere + queryNewRsv + queryWhere + queryOrder );
            // Logging.info( "[LogicReserveListPC.DaylyCount] query=" + query + queryWhere + queryNewRsv + queryWhere + queryOrder );

            prestate.setInt( 1, this.hotelId );

            seq = 2;
            if ( rsvDateF != 0 && rsvDateT != 0 )
            {
                prestate.setInt( seq++, rsvDateF );
                prestate.setInt( seq++, rsvDateT );
            }
            prestate.setInt( seq++, this.hotelId );
            if ( rsvDateF != 0 && rsvDateT != 0 )
            {
                prestate.setInt( seq++, rsvDateF );
                prestate.setInt( seq++, rsvDateT );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {

                // �S�����o
                while( result.next() )
                {
                    this.setIdList( result.getInt( "id" ) ); // �z�e��ID
                    this.setRsvDateValList( result.getInt( "reserve_date" ) );
                    this.setRsvCount( result.getInt( "rsv_count" ) );

                    // ��������
                    intMaxCnt++;
                }

                // �S����ʕ\���̏ꍇ�͂��̂܂܃t�H�[���֕ҏW����
                for( i = 0 ; i < intMaxCnt - 1 ; i++ )
                {
                    frm.setIdList( this.idList.get( i ) );
                    frm.setRsvDateValList( this.rsvDateValList.get( i ) );
                    frm.setRsvCount( this.rsvCountList.get( i ) );

                }

                isResult = true;
                frm.setPageMax( intMaxCnt - 1 );
                frm.setPageSt( 0 );
                frm.setPageEd( intMaxCnt - 1 );
            }
            else
            {
                // not Found
                isResult = false;
                // ���o����
                frm.setPageMax( intMaxCnt );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveListPC.getDailyCount()] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return isResult;
    }

    /**
     * 
     * getter
     * 
     */
    public String getDateFrom()
    {
        return this.dateFROM;
    }

    public String getDateTo()
    {
        return this.dateTO;
    }

    public ArrayList<Integer> getDspList()
    {
        return this.dspList;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public ArrayList<Integer> getIdList()
    {
        return idList;
    }

    public ArrayList<String> getOptionList()
    {
        return optionList;
    }

    public ArrayList<String> getRsvDateList()
    {
        return rsvDateList;
    }

    public ArrayList<Integer> getRsvDateValList()
    {
        return rsvDateValList;
    }

    public ArrayList<String> getStatusList()
    {
        return this.statusList;
    }

    public ArrayList<Integer> getStatusValList()
    {
        return this.statusValList;
    }

    public ArrayList<Integer> getSeqList()
    {
        return seqList;
    }

    public ArrayList<String> getUserNmList()
    {
        return userNmList;
    }

    public ArrayList<String> getTel1List()
    {
        return tel1List;
    }

    public ArrayList<String> getPlanNmList()
    {
        return planNmList;
    }

    public ArrayList<String> getReserveNoList()
    {
        return reserveNoList;
    }

    public ArrayList<String> getUserIdList()
    {
        return userIdList;
    }

    public ArrayList<String> getEstTimeArrivalList()
    {
        return this.estTimeArrivalList;
    }

    public ArrayList<Integer> getEstTimeArrivalValList()
    {
        return this.estTimeArrivalValList;
    }

    public ArrayList<Integer> getNoShowList()
    {
        return this.noShowList;
    }

    public int getHotelId()
    {
        return this.hotelId;
    }

    public int getMaxCnt()
    {
        return this.maxCnt;
    }

    public ArrayList<Boolean> getHistoryFlgList()
    {
        return historyFlgList;
    }

    public ArrayList<Integer> getReserveSubNoList()
    {
        return reserveSubNoList;
    }

    public ArrayList<Integer> getAcceptDateList()
    {
        return acceptDateList;
    }

    public ArrayList<Integer> getAcceptTimeList()
    {
        return acceptTimeList;
    }

    public ArrayList<Integer> getRsvCountList()
    {
        return rsvCountList;
    }

    public ArrayList<Integer> getPaymentList()
    {
        return paymentList;
    }

    public ArrayList<Integer> getPaymentStatusList()
    {
        return paymentStatusList;
    }

    public ArrayList<String> getChargeTotalList()
    {
        return chargeTotalList;
    }

    /**
     * 
     * setter
     * 
     */
    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public void setDateFrom(String datefrom)
    {
        this.dateFROM = datefrom;
    }

    public void setDateTo(String dateto)
    {
        this.dateTO = dateto;
    }

    public void setDspList(int dspval)
    {
        this.dspList.add( dspval );
    }

    public void setRsvNo(String rsvno)
    {
        this.rsvNo = rsvno;
    }

    public void setIdList(int id)
    {
        this.idList.add( id );
    }

    public void setOptionList(String option)
    {
        this.optionList.add( option );
    }

    public void setRsvDateList(String rsvdate)
    {
        this.rsvDateList.add( rsvdate );
    }

    public void setRsvDateValList(int rsvdateVal)
    {
        this.rsvDateValList.add( rsvdateVal );
    }

    public void setStatusList(String status)
    {
        this.statusList.add( status );
    }

    public void setStatusValList(int status)
    {
        this.statusValList.add( status );
    }

    public void setSeqList(int seq)
    {
        this.seqList.add( seq );
    }

    public void setUserNmList(String usernm)
    {
        this.userNmList.add( usernm );
    }

    public void setTel1List(String tel1)
    {
        this.tel1List.add( tel1 );
    }

    public void setUserIdList(String userid)
    {
        this.userIdList.add( userid );
    }

    public void setPlanNmList(String plannm)
    {
        this.planNmList.add( plannm );
    }

    public void setReserveNoList(String reserveno)
    {
        this.reserveNoList.add( reserveno );
    }

    public void setHotelId(int hotelid)
    {
        this.hotelId = hotelid;
    }

    public void setEstTimeArrivalList(String esttime)
    {
        this.estTimeArrivalList.add( esttime );
    }

    public void setEstTimeArrivalValList(int esttime)
    {
        this.estTimeArrivalValList.add( esttime );
    }

    public void setNoShowList(int noshow)
    {
        this.noShowList.add( noshow );
    }

    public void setHistoryFlgList(Boolean historyFlgList)
    {
        this.historyFlgList.add( historyFlgList );
    }

    public void setReserveSubNoList(Integer reserveSubNoList)
    {
        this.reserveSubNoList.add( reserveSubNoList );
    }

    public void setAcceptDateList(Integer acceptDateList)
    {
        this.acceptDateList.add( acceptDateList );
    }

    public void setAcceptTimeList(Integer acceptTimeList)
    {
        this.acceptTimeList.add( acceptTimeList );
    }

    public void setRsvCount(Integer rsvCount)
    {
        this.rsvCountList.add( rsvCount );
    }

    public void setPaymentList(Integer payment)
    {
        this.paymentList.add( payment );
    }

    public void setPaymentStatusList(Integer paymentStatus)
    {
        this.paymentStatusList.add( paymentStatus );
    }

    public void setChargeTotalList(String chargeTotal)
    {
        this.chargeTotalList.add( chargeTotal );
    }

}
