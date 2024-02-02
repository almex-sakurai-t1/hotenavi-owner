package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataRsvPlan;

/**
 * �J�����_�[�r�W�l�X���W�b�N
 */
public class LogicOwnerRsvManageCalendar implements Serializable
{

    private static final long   serialVersionUID    = -2912059728003741855L;
    private static final String RSV_ON              = "��t��";
    private static final String RSV_OFF             = "��t�I��";
    private static final String RSV_STOP            = "��~��";
    private static final String RSV_ON_MARK         = "��";
    private static final String RSV_OFF_MARK        = "�~";
    private static final String RSV_IMPOSSIBLE_MARK = "�|";

    /**
     * �J�����_�[�E�c�������擾
     * 
     * @param hotelId �z�e��ID
     * @param planId �v����ID
     * @param targetYM �Ώ۔N��
     * @return �w�肳�ꂽ����1�������̃J�����_�[���
     */
    public ArrayList<ArrayList<FormOwnerRsvManageCalendar>> getCalendarData(int hotelId, int planId, int targetYM) throws Exception
    {
        int cnt = 0;
        int weekId = 0;
        String year = "";
        String month = "";
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<FormOwnerRsvManageCalendar> oneWeekList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        FormOwnerRsvManageCalendar frm = new FormOwnerRsvManageCalendar();
        FormOwnerRsvManageCalendar frmEmpty;

        // �J�����_�[���擾
        frmList = getCalendar( frm, hotelId, planId, targetYM );

        // �����c�����擾
        for( int i = 0 ; i < frmList.size() ; i++ )
        {
            frm = frmList.get( i );
            weekId = frm.getWeekId();
            // 1�����O�̓��t���̋�f�[�^�쐬
            if ( (i == 0) && (weekId != 0) )
            {
                for( int j = 0 ; j < weekId ; j++ )
                {
                    frmEmpty = new FormOwnerRsvManageCalendar();
                    oneWeekList.add( frmEmpty );
                }
            }

            // �Ώ۔N���擾
            year = Integer.toString( targetYM ).substring( 0, 4 );
            month = Integer.toString( targetYM ).substring( 4 );
            frm.setCurrentYear( year );
            frm.setCurrentMonth( month );

            if ( planId == -1 )
            {
                // �v����ID���w��̏ꍇ
                // �S�����c���擾
                cnt = getRoomRemainder( hotelId, planId, 1, frm.getCalDate() );
                frm.setAllRoomNum( cnt );

                // �󂫕����c��
                cnt = 0;
                cnt = getRoomRemainder( hotelId, planId, 2, frm.getCalDate() );
                frm.setVacancyRoomNum( cnt );

                if ( (frm.getRsvJotaiFlg() == 1) && (frm.getSalesFlag() == 0) )
                {
                    frm.setRsvJotai( RSV_STOP );
                }
            }
            else
            {
                // �v����ID�w��̏ꍇ
                // �S�����c���擾
                cnt = getRoomRemainderPlan( hotelId, planId, 1, frm.getCalDate() );
                frm.setAllRoomNum( cnt );

                // �󂫕����c��
                cnt = 0;
                cnt = getRoomRemainderPlan( hotelId, planId, 2, frm.getCalDate() );
                frm.setVacancyRoomNum( cnt );
                if ( frm.getRsvJotaiMark().equals( RSV_ON_MARK ) )
                {
                    if ( cnt == 0 )
                    {
                        frm.setRsvJotai( RSV_OFF );
                        frm.setRsvJotaiMark( RSV_OFF_MARK );
                    }
                    else
                    {
                        // �\�������m�F
                        if ( checkMaxRsvRommCnt( hotelId, planId, frm.getCalDate() ) == false )
                        {
                            frm.setRsvJotai( RSV_OFF );
                            frm.setRsvJotaiMark( RSV_OFF_MARK );
                        }
                    }
                }
            }

            oneWeekList.add( frm );

            if ( frm.getWeekId() == 6 )
            {
                // �y�j�̏ꍇ�́A�V�������X�g���쐬
                monthlyList.add( oneWeekList );
                oneWeekList = new ArrayList<FormOwnerRsvManageCalendar>();
            }
        }
        // �ŏI�����ȍ~�̋�f�[�^�쐬
        monthlyList.add( oneWeekList );

        return(monthlyList);

    }

    //
    /**
     * �J�����_�[�E�c�������擾
     * 
     * @param hotelId �z�e��ID
     * @param planId �v����ID
     * @param seq �Ǘ��ԍ�
     * @param targetYM �Ώ۔N��
     * @return �w�肳�ꂽ����1�������̃J�����_�[���
     */
    public ArrayList<ArrayList<FormOwnerRsvManageCalendar>> getCalendarData(int hotelId, int planId, int seq, int targetYM) throws Exception
    {
        int cnt = 0;
        int weekId = 0;
        String year = "";
        String month = "";
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<FormOwnerRsvManageCalendar> oneWeekList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        FormOwnerRsvManageCalendar frm = new FormOwnerRsvManageCalendar();
        FormOwnerRsvManageCalendar frmEmpty;

        // �J�����_�[���擾
        frmList = getCalendar( frm, hotelId, planId, targetYM );

        // �����c�����擾
        for( int i = 0 ; i < frmList.size() ; i++ )
        {
            frm = frmList.get( i );
            weekId = frm.getWeekId();
            // 1�����O�̓��t���̋�f�[�^�쐬
            if ( (i == 0) && (weekId != 0) )
            {
                for( int j = 0 ; j < weekId ; j++ )
                {
                    frmEmpty = new FormOwnerRsvManageCalendar();
                    oneWeekList.add( frmEmpty );
                }
            }

            // �Ώ۔N���擾
            year = Integer.toString( targetYM ).substring( 0, 4 );
            month = Integer.toString( targetYM ).substring( 4 );
            frm.setCurrentYear( year );
            frm.setCurrentMonth( month );

            if ( planId == -1 )
            {
                // �v����ID���w��̏ꍇ
                // �S�����c���擾
                cnt = getRoomRemainder( hotelId, planId, 1, frm.getCalDate() );
                frm.setAllRoomNum( cnt );

                // �\��ςݕ�����
                cnt = 0;
                cnt = getRoomRemainder( hotelId, planId, 2, frm.getCalDate() );
                frm.setVacancyRoomNum( cnt );
            }
            else
            {
                // �v����ID�w��̏ꍇ
                // �S�����c���擾
                cnt = getRoomRemainderPlan( hotelId, planId, 1, seq, frm.getCalDate() );
                frm.setAllRoomNum( cnt );

                // �\��ςݕ�����
                cnt = 0;
                cnt = getRoomRemainderPlan( hotelId, planId, 2, seq, frm.getCalDate() );
                frm.setVacancyRoomNum( cnt );
                if ( frm.getRsvJotaiMark().equals( RSV_ON_MARK ) )
                {
                    if ( cnt == 0 )
                    {
                        frm.setRsvJotai( RSV_OFF );
                        frm.setRsvJotaiMark( RSV_OFF_MARK );
                    }
                    else
                    {
                        // �\�������m�F
                        if ( checkMaxRsvRommCnt( hotelId, planId, frm.getCalDate() ) == false )
                        {
                            frm.setRsvJotai( RSV_OFF );
                            frm.setRsvJotaiMark( RSV_OFF_MARK );
                        }
                    }
                }
            }

            oneWeekList.add( frm );

            if ( frm.getWeekId() == 6 )
            {
                // �y�j�̏ꍇ�́A�V�������X�g���쐬
                monthlyList.add( oneWeekList );
                oneWeekList = new ArrayList<FormOwnerRsvManageCalendar>();
            }
        }
        // �ŏI�����ȍ~�̋�f�[�^�쐬
        monthlyList.add( oneWeekList );

        return(monthlyList);

    }

    /**
     * �J�����_�[���擾
     * 
     * @param frm FormOwnerRsvManageCalendar�I�u�W�F�N�g
     * @param hotelId �z�e��ID
     * @param planId �v����ID(���w��̏ꍇ��-1���w�肷��)
     * @param targetYM �����Ώ۔N��(YYYYMM)
     * @return FormOwnerRsvManageCalendar��ArrayList
     * @throws Exception
     */
    private ArrayList<FormOwnerRsvManageCalendar> getCalendar(FormOwnerRsvManageCalendar frm, int hotelId, int planId, int targetYM) throws Exception
    {
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();

        if ( planId != -1 )
        {
            frmList = getPlanIdCalendar( frm, hotelId, planId, targetYM );
        }
        else
        {
            // �v����ID���w�莞
            frmList = getNoPlanIdCalendar( frm, hotelId, targetYM );
        }

        return(frmList);
    }

    /**
     * �v����ID�w�莞�̃J�����_�[���擾
     * 
     * @param frm FormOwnerRsvManageCalendar�I�u�W�F�N�g
     * @param hotelId �z�e��ID
     * @param planId �v����ID
     * @param targetYM �����Ώ۔N��(YYYYMM)
     * @return FormOwnerRsvManageCalendar��ArrayList
     * @throws Exception
     */
    private ArrayList<FormOwnerRsvManageCalendar> getPlanIdCalendar(FormOwnerRsvManageCalendar frm, int hotelId, int planId, int targetYM) throws Exception
    {
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<FormOwnerRsvManageCalendar> hotelCalendarList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<FormOwnerRsvManageCalendar> dayChargeList = new ArrayList<FormOwnerRsvManageCalendar>();
        FormOwnerRsvManageCalendar hotelCalendarFrm;
        FormOwnerRsvManageCalendar dayChargeFrm;
        FormOwnerRsvManageCalendar newFrm;
        boolean setFlg = false;

        // �z�e���J�����_�[���擾
        hotelCalendarList = getHotelCalendar( frm, hotelId, targetYM );

        // ���ʗ����}�X�^�擾
        dayChargeList = getDayCharge( frm, hotelId, planId, targetYM );

        // �z�e���J�����_�[�Ɠ��ʗ����}�X�^�����}�[�W
        for( int i = 0 ; i < hotelCalendarList.size() ; i++ )
        {
            hotelCalendarFrm = hotelCalendarList.get( i );
            setFlg = false;

            for( int j = 0 ; j < dayChargeList.size() ; j++ )
            {
                dayChargeFrm = dayChargeList.get( j );

                if ( hotelCalendarFrm.getCalDate() == dayChargeFrm.getCalDate() )
                {
                    // ���ʗ������̃f�[�^�ŏ㏑��
                    newFrm = new FormOwnerRsvManageCalendar();
                    newFrm.setHotelId( hotelId );
                    newFrm.setCalDate( hotelCalendarFrm.getCalDate() );
                    newFrm.setDate( hotelCalendarFrm.getDate() );
                    newFrm.setChargeModeId( dayChargeFrm.getChargeModeId() );
                    newFrm.setChargeModeNm( dayChargeFrm.getChargeModeNm() );
                    newFrm.setWeekId( hotelCalendarFrm.getWeekId() );
                    newFrm.setHolidayKind( hotelCalendarFrm.getHolidayKind() );
                    newFrm.setSalesFlag( hotelCalendarFrm.getSalesFlag() );
                    newFrm.setPlanId( planId );
                    newFrm.setAdultTwoCharge( dayChargeFrm.getAdultTwoCharge() );
                    newFrm.setAdultTwoChargeFormat( dayChargeFrm.getAdultTwoChargeFormat() );
                    newFrm.setCurrentFlg( hotelCalendarFrm.getCurrentFlg() );
                    newFrm.setRsvJotai( hotelCalendarFrm.getRsvJotai() );
                    newFrm.setRsvJotaiMark( hotelCalendarFrm.getRsvJotaiMark() );
                    newFrm.setRsvJotaiFlg( hotelCalendarFrm.getRsvJotaiFlg() );
                    newFrm.setErrMsg( hotelCalendarFrm.getErrMsg() );

                    frmList.add( newFrm );
                    setFlg = true;
                    break;
                }
            }

            if ( setFlg == false )
            {
                // �J�����_�[���𐳂Ƃ���
                newFrm = new FormOwnerRsvManageCalendar();
                newFrm.setHotelId( hotelId );
                newFrm.setCalDate( hotelCalendarFrm.getCalDate() );
                newFrm.setDate( hotelCalendarFrm.getDate() );
                newFrm.setChargeModeId( hotelCalendarFrm.getChargeModeId() );
                newFrm.setChargeModeNm( hotelCalendarFrm.getChargeModeNm() );
                newFrm.setWeekId( hotelCalendarFrm.getWeekId() );
                newFrm.setHolidayKind( hotelCalendarFrm.getHolidayKind() );
                newFrm.setSalesFlag( hotelCalendarFrm.getSalesFlag() );
                newFrm.setPlanId( planId );
                newFrm.setAdultTwoCharge( 0 );
                newFrm.setAdultTwoChargeFormat( "0" );
                newFrm.setCurrentFlg( hotelCalendarFrm.getCurrentFlg() );
                newFrm.setRsvJotai( hotelCalendarFrm.getRsvJotai() );
                newFrm.setRsvJotaiMark( hotelCalendarFrm.getRsvJotaiMark() );
                newFrm.setRsvJotaiFlg( hotelCalendarFrm.getRsvJotaiFlg() );
                newFrm.setErrMsg( hotelCalendarFrm.getErrMsg() );

                frmList.add( newFrm );
            }

        }

        return(frmList);
    }

    /**
     * ���ʗ����}�X�^���擾
     * 
     * @param frm FormOwnerRsvManageCalendar�I�u�W�F�N�g
     * @param hotelId �z�e��ID
     * @param planId �v����ID
     * @param targetYM �Ώ۔N��
     * @return ���ʗ����}�X�^��ArrayList
     */
    private ArrayList<FormOwnerRsvManageCalendar> getDayCharge(FormOwnerRsvManageCalendar frm, int hotelId, int planId, int targetYM) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();
        NumberFormat objNum = NumberFormat.getCurrencyInstance();

        query = query + "SELECT dc.id, dc.plan_id, dc.cal_date, dc.charge_mode_id, cm.charge_mode_name, ";
        query = query + "CASE pc.adult_two_charge IS null WHEN 1 THEN 0 ELSE pc.adult_two_charge END adult_two_charge ";
        query = query + "FROM hh_rsv_day_charge dc ";
        query = query + "    LEFT JOIN hh_rsv_charge_mode cm ON dc.id = cm.id AND dc.charge_mode_id = cm.charge_mode_id ";
        query = query + "    LEFT JOIN hh_rsv_plan_charge pc ON dc.id = pc.id AND dc.plan_id = pc.plan_id AND dc.charge_mode_id = pc.charge_mode_id ";
        query = query + "WHERE dc.id = ? ";
        query = query + "  AND dc.cal_date BETWEEN ? AND ? ";
        query = query + "  AND dc.plan_id = ? ";
        query = query + "ORDER BY dc.cal_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, Integer.toString( targetYM ) + "01" );
            prestate.setString( 3, Integer.toString( targetYM ) + "31" );
            prestate.setInt( 4, planId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                frm = new FormOwnerRsvManageCalendar();
                frm.setHotelId( result.getInt( "id" ) );
                frm.setPlanId( result.getInt( "plan_id" ) );
                frm.setCalDate( result.getInt( "cal_date" ) );
                frm.setDate( Integer.parseInt( result.getString( "cal_date" ).substring( 6 ) ) );
                frm.setChargeModeId( result.getInt( "charge_mode_id" ) );
                frm.setChargeModeNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( CheckString.checkStringForNull( result.getString( "charge_mode_name" ) ) ) ) );
                frm.setAdultTwoCharge( result.getInt( "adult_two_charge" ) );
                if ( result.getInt( "adult_two_charge" ) > 0 )
                {
                    frm.setAdultTwoChargeFormat( objNum.format( result.getInt( "adult_two_charge" ) ) );
                }
                else
                {
                    frm.setAdultTwoChargeFormat( "" );
                }
                frmList.add( frm );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.getDayCharge] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(frmList);
    }

    /**
     * �z�e���J�����_�[���擾
     * 
     * @param frm FormOwnerRsvManageCalendar�I�u�W�F�N�g
     * @param hotelId �z�e��ID
     * @param targetYM �Ώ۔N��
     * @return �z�e���J�����_�[����ArrayList
     */
    private ArrayList<FormOwnerRsvManageCalendar> getHotelCalendar(FormOwnerRsvManageCalendar frm, int hotelId, int targetYM) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();
        String year = "";
        String month = "";
        String day = "";
        int today = 0;
        int count = 0;

        query = query + "SELECT ca.id, ca.cal_date, ca.charge_mode_id, ca.week, ca.holiday_kind, ca.sales_flag, cm.charge_mode_name ";
        query = query + "FROM hh_rsv_hotel_calendar ca ";
        query = query + "    LEFT JOIN hh_rsv_charge_mode cm ON ca.id = cm.id AND ca.charge_mode_id = cm.charge_mode_id ";
        query = query + "WHERE ca.id = ? ";
        query = query + "AND ca.cal_date BETWEEN ? AND ? ";
        query = query + "ORDER BY ca.cal_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, Integer.toString( targetYM ) + "01" );
            prestate.setString( 3, Integer.toString( targetYM ) + "31" );
            result = prestate.executeQuery();

            // �����擾
            Calendar calendar = Calendar.getInstance();
            year = Integer.toString( calendar.get( Calendar.YEAR ) );
            month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );
            day = String.format( "%1$02d", calendar.get( Calendar.DATE ) );
            today = Integer.parseInt( year + month + day );

            while( result.next() )
            {
                frm = new FormOwnerRsvManageCalendar();
                frm.setHotelId( result.getInt( "id" ) );
                frm.setCalDate( result.getInt( "cal_date" ) );
                frm.setDate( Integer.parseInt( result.getString( "cal_date" ).substring( 6 ) ) );
                frm.setChargeModeId( result.getInt( "charge_mode_id" ) );
                frm.setChargeModeNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( CheckString.checkStringForNull( result.getString( "charge_mode_name" ) ) ) ) );
                frm.setWeekId( result.getInt( "week" ) );
                frm.setHolidayKind( result.getInt( "holiday_kind" ) );
                frm.setSalesFlag( result.getInt( "sales_flag" ) );
                // �����t���O�̃Z�b�g
                if ( today == (result.getInt( "cal_date" )) )
                {
                    // ����
                    frm.setCurrentFlg( 1 );
                }
                else
                {
                    // �����ȊO
                    frm.setCurrentFlg( 0 );
                }
                // ��t�t���O�̃Z�b�g
                if ( (result.getInt( "cal_date" )) < today )
                {
                    // �������O�͎�t�I��
                    frm.setRsvJotai( RSV_OFF );
                    frm.setRsvJotaiMark( RSV_OFF_MARK ); // �~�}�[�N
                    frm.setRsvJotaiFlg( 0 );
                }
                else
                {
                    // ��t��
                    frm.setRsvJotai( RSV_ON );
                    frm.setRsvJotaiMark( RSV_ON_MARK ); // ���}�[�N
                    frm.setRsvJotaiFlg( 1 );
                }

                frmList.add( frm );
            }

            // ���R�[�h�����擾
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // �Y���f�[�^���Ȃ��ꍇ
            if ( count == 0 )
            {
                frm = new FormOwnerRsvManageCalendar();
                frm.setErrMsg( Message.getMessage( "erro.30001", "�J�����_�[���" ) );
                frmList.add( frm );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.getHotelCalendar] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(frmList);
    }

    /**
     * �v����ID���w�莞�̃J�����_�[���擾
     * 
     * @param frm FormOwnerRsvManageCalendar�I�u�W�F�N�g
     * @param hotelId �z�e��ID
     * @param targetYM �����Ώ۔N��(YYYYMM)
     * @return FormOwnerRsvManageCalendar��ArrayList
     * @throws Exception
     */
    private ArrayList<FormOwnerRsvManageCalendar> getNoPlanIdCalendar(FormOwnerRsvManageCalendar frm, int hotelId, int targetYM) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();
        String year = "";
        String month = "";
        String day = "";
        int today = 0;
        int count = 0;
        int roomCnt = 0;

        query = query + "SELECT ca.id, ca.cal_date, ca.charge_mode_id, cm.charge_mode_name, ca.week, ca.holiday_kind, ca.sales_flag ";
        query = query + "FROM hh_rsv_hotel_calendar ca ";
        query = query + "    LEFT JOIN hh_rsv_charge_mode cm ON ca.id = cm.id AND ca.charge_mode_id = cm.charge_mode_id ";
        query = query + "WHERE ca.id = ? ";
        query = query + "AND cal_date BETWEEN ? AND ? ";
        query = query + "ORDER BY ca.cal_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, Integer.toString( targetYM ) + "01" );
            prestate.setString( 3, Integer.toString( targetYM ) + "31" );
            result = prestate.executeQuery();

            // �����擾
            Calendar calendar = Calendar.getInstance();
            year = Integer.toString( calendar.get( Calendar.YEAR ) );
            month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );
            day = String.format( "%1$02d", calendar.get( Calendar.DATE ) );
            today = Integer.parseInt( year + month + day );

            while( result.next() )
            {
                frm = new FormOwnerRsvManageCalendar();
                frm.setHotelId( result.getInt( "id" ) );
                frm.setCalDate( result.getInt( "cal_date" ) );
                frm.setDate( Integer.parseInt( result.getString( "cal_date" ).substring( 6 ) ) );
                frm.setChargeModeId( result.getInt( "charge_mode_id" ) );
                frm.setChargeModeNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( CheckString.checkStringForNull( result.getString( "charge_mode_name" ) ) ) ) );
                frm.setWeekId( result.getInt( "week" ) );
                frm.setHolidayKind( result.getInt( "holiday_kind" ) );
                frm.setSalesFlag( result.getInt( "sales_flag" ) );
                // �����t���O�̃Z�b�g
                if ( today == (result.getInt( "cal_date" )) )
                {
                    // ����
                    frm.setCurrentFlg( 1 );
                }
                else
                {
                    // �����ȊO
                    frm.setCurrentFlg( 0 );
                }
                // ��t�t���O�̃Z�b�g
                if ( (result.getInt( "cal_date" )) < today )
                {
                    // �������O�͎�t�I��
                    frm.setRsvJotai( RSV_OFF );
                    frm.setRsvJotaiMark( RSV_OFF_MARK ); // �~�}�[�N
                    frm.setRsvJotaiFlg( 0 );
                }
                else
                {
                    // ��t��
                    frm.setRsvJotai( RSV_ON );
                    frm.setRsvJotaiMark( RSV_ON_MARK ); // ���}�[�N
                    frm.setRsvJotaiFlg( 1 );
                }

                frmList.add( frm );
            }

            // ���R�[�h�����擾
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // �Y���f�[�^���Ȃ��ꍇ
            if ( count == 0 )
            {
                frm = new FormOwnerRsvManageCalendar();
                frm.setErrMsg( Message.getMessage( "erro.30001", "�J�����_�[���" ) );
                frmList.add( frm );
                return(frmList);
            }

            // �����c���f�[�^���Ȃ��ꍇ
            roomCnt = getRoomRemainderCnt( hotelId, targetYM );
            if ( roomCnt == 0 )
            {
                frmList = new ArrayList<FormOwnerRsvManageCalendar>();
                frm = new FormOwnerRsvManageCalendar();
                frm.setErrMsg( Message.getMessage( "erro.30001", "�J�����_�[���" ) );
                frmList.add( frm );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.getNoPlanIdCalendar] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(frmList);
    }

    /**
     * �Ώ۔N���̕����c���擾
     * 
     * @param hotelId �z�e��ID
     * @param targetYM �����Ώ۔N��(YYYYMM)
     * @return �擾���������c��
     * @throws Exception
     */
    private int getRoomRemainderCnt(int hotelId, int targetYM) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;

        query = query + "SELECT COUNT(*) AS CNT ";
        query = query + "FROM hh_rsv_room_remainder ";
        query = query + "WHERE id = ? ";
        query = query + "  AND cal_date LIKE concat(?, '%') ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, targetYM );
            result = prestate.executeQuery();

            while( result.next() )
            {
                count = result.getInt( "CNT" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManageCalendar.getRoomRemainderCnt] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(count);
    }

    /**
     * �����c���擾
     * 
     * @param hotelId �z�e��ID
     * @param planId �v����ID(-1�w�莞�́A�v�������i���Č����͍s��Ȃ�)
     * @param selKbn �����敪(1:�S�c�����A2:�󂫕�����)
     * @param targetDate �����Ώ۔N����(YYYYMMDD)
     * @return �Ȃ�
     * @throws Exception
     */
    private int getRoomRemainder(int hotelId, int planId, int selKbn, int targetDate) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;

        query = query + "SELECT DISTINCT count(cal_date) AS CNT ";
        query = query + "FROM hh_rsv_room_remainder ";
        query = query + "WHERE id = ? ";
        query = query + "  AND cal_date = ? ";
        if ( planId != -1 )
        {
            query = query + "  AND plan_id = ? ";
        }
        if ( selKbn == 2 )
        {
            query = query + "  AND status = 2 ";
        }

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, targetDate );
            if ( planId != -1 )
            {
                prestate.setInt( 3, planId );
            }
            result = prestate.executeQuery();

            while( result.next() )
            {
                count = result.getInt( "CNT" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManageCalendar.getRoomRemainder] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(count);
    }

    /**
     * �����c���擾
     * 
     * @param hotelId �z�e��ID
     * @param planId �v����ID(-1�w�莞�́A�v�������i���Č����͍s��Ȃ�)
     * @param selKbn �����敪(1:�S�c�����A2:�󂫕������A3:�\�񕔉���)
     * @param targetDate �����Ώ۔N����(YYYYMMDD)
     * @return �Ȃ�
     * @throws Exception
     */
    private int getRoomRemainderPlan(int hotelId, int planId, int selKbn, int targetDate) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;

        query = "SELECT COUNT(*) AS CNT FROM hh_rsv_room_remainder rrr  " +
                "INNER JOIN hh_rsv_rel_plan_room rrpr ON " +
                "     ( rrpr.id = rrr.id " +
                "   AND rrpr.seq = rrr.seq ) " +
                "INNER JOIN hh_rsv_room rr ON " +
                "     ( rrr.id = rr.id " +
                "   AND rrr.seq = rr.seq ) " +
                " WHERE rrpr.id = ? " +
                "   AND rrr.cal_date = ? " +
                "   AND rrpr.plan_id = ? " +
                "   AND rr.sales_flag = 1 ";
        if ( selKbn == 2 )
        {
            query = query + "  AND rrr.status = 1 ";
        }
        else if ( selKbn == 3 )
        {
            query = query + "  AND rrr.status = 2 ";
        }

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, targetDate );
            prestate.setInt( 3, planId );
            result = prestate.executeQuery();
            while( result.next() )
            {
                count = result.getInt( "CNT" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManageCalendar.getRoomRemainder] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(count);
    }

    /**
     * �\�����l�m�F����
     * �\��f�[�^�̂�����t�ς�ں��ތ����ƃv�����}�X�^�̗\�������Ƃ��r����
     * 
     * @param id �z�e��ID
     * @param planid �v����ID
     * @param rsvDate �\���
     * @return false: ���݂��� true: ���݂��Ȃ�
     * @throws Exception
     */
    private boolean checkMaxRsvRommCnt(int id, int planid, int rsvDate) throws Exception
    {
        boolean blnRet = false;
        DataRsvPlan dataPlan = new DataRsvPlan();
        int intMaxQuantity = 0;
        int rsvCnt = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            // �v�����}�X�^������l���擾
            if ( dataPlan.getData( id, planid ) == true )
            {
                // �\�������̎擾
                intMaxQuantity = dataPlan.getMaxQuantity();
            }

            if ( intMaxQuantity != 0 )
            {
                // ���ID�A����ID�A�\��������Ɏ�t�ς̗\��f�[�^�̌������擾����
                query = "SELECT COUNT(*) AS CNT FROM hh_rsv_reserve " +
                        " WHERE id = ? AND plan_id = ? AND reserve_date = ? AND status = 1 ";

                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, id );
                prestate.setInt( 2, planid );
                prestate.setInt( 3, rsvDate );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        rsvCnt = result.getInt( "CNT" );
                    }
                }

                // �����w��̏ꍇ
                if ( intMaxQuantity > rsvCnt )
                {
                    // �\��f�[�^�̌�����������قȂ�ꍇ�͂n�j
                    blnRet = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManageCalendar.checkMaxRsvRommCnt] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            dataPlan = null;
            DBConnection.releaseResources( result, prestate, connection );
        }
        return blnRet;
    }

    /**
     * �����c���擾
     * 
     * @param hotelId �z�e��ID
     * @param planId �v����ID(-1�w�莞�́A�v�������i���Č����͍s��Ȃ�)
     * @param selKbn �����敪(1:�S�c�����A2:�󂫕�����)
     * @param seq �Ǘ��ԍ�
     * @param targetDate �����Ώ۔N����(YYYYMMDD)
     * @return �Ȃ�
     * @throws Exception
     */
    private int getRoomRemainderPlan(int hotelId, int planId, int selKbn, int seq, int targetDate) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;

        query = "SELECT COUNT(*) AS CNT FROM hh_rsv_room_remainder rrr  " +
                "INNER JOIN hh_rsv_rel_plan_room rrpr ON " +
                "     ( rrpr.id = rrr.id " +
                "   AND rrpr.seq = rrr.seq ) " +
                "INNER JOIN hh_rsv_room rr ON " +
                "     ( rrr.id = rr.id " +
                "   AND rrr.seq = rr.seq ) " +
                " WHERE rrpr.id = ? " +
                "   AND rrr.cal_date = ? " +
                "   AND rrpr.plan_id = ? " +
                "   AND rrr.seq = ? " +
                "   AND rr.sales_flag = 1 ";
        if ( selKbn == 2 )
        {
            query = query + "  AND rrr.status = 1 ";
        }

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, targetDate );
            prestate.setInt( 3, planId );
            prestate.setInt( 4, seq );
            result = prestate.executeQuery();

            while( result.next() )
            {
                count = result.getInt( "CNT" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManageCalendar.getRoomRemainder] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(count);
    }

    /**
     * �J�����_�[�E�c�������擾
     * 
     * @param hotelId �z�e��ID
     * @param planId �v����ID
     * @param targetYM �Ώ۔N��
     * @return �w�肳�ꂽ����1�������̃J�����_�[���
     */
    public ArrayList<ArrayList<FormOwnerRsvManageCalendar>> getPlanCalendarData(int hotelId, int planId, int targetYM) throws Exception
    {
        int cnt = 0;
        int weekId = 0;
        String year = "";
        String month = "";
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<FormOwnerRsvManageCalendar> oneWeekList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        FormOwnerRsvManageCalendar frm = new FormOwnerRsvManageCalendar();
        FormOwnerRsvManageCalendar frmEmpty;

        // �J�����_�[���擾
        frmList = getCalendar( frm, hotelId, planId, targetYM );

        // �����c�����擾
        for( int i = 0 ; i < frmList.size() ; i++ )
        {
            frm = frmList.get( i );
            weekId = frm.getWeekId();
            // 1�����O�̓��t���̋�f�[�^�쐬
            if ( (i == 0) && (weekId != 0) )
            {
                for( int j = 0 ; j < weekId ; j++ )
                {
                    frmEmpty = new FormOwnerRsvManageCalendar();
                    oneWeekList.add( frmEmpty );
                }
            }

            // �Ώ۔N���擾
            year = Integer.toString( targetYM ).substring( 0, 4 );
            month = Integer.toString( targetYM ).substring( 4 );
            frm.setCurrentYear( year );
            frm.setCurrentMonth( month );

            if ( planId == -1 )
            {
                // �v����ID���w��̏ꍇ
                // �S�����c���擾
                cnt = getRoomRemainder( hotelId, planId, 1, frm.getCalDate() );
                frm.setAllRoomNum( cnt );

                // �󂫕����c��
                cnt = 0;
                cnt = getRoomRemainder( hotelId, planId, 2, frm.getCalDate() );
                frm.setVacancyRoomNum( cnt );

                if ( (frm.getRsvJotaiFlg() == 1) && (frm.getSalesFlag() == 0) )
                {
                    frm.setRsvJotai( RSV_STOP );
                }
            }
            else
            {
                // �v����ID�w��̏ꍇ
                // �S�����c���擾
                cnt = getRoomRemainderPlan( hotelId, planId, 1, frm.getCalDate() );

                if ( cnt > ReserveCommon.getMaxQuantityPlan( hotelId, planId ) )
                {
                    cnt = ReserveCommon.getMaxQuantityPlan( hotelId, planId );
                }
                frm.setAllRoomNum( cnt );

                // �\��ς݂̕�����
                cnt = 0;
                // cnt = getRoomRemainderPlan( hotelId, planId, 3, frm.getCalDate() );
                // �\�񂳂ꂽ�J�E���g����\������
                cnt = ReserveCommon.getReserveCount( hotelId, planId, frm.getCalDate() );
                frm.setVacancyRoomNum( cnt );

                // ���[�U�T�C�g�Ɠ����\�������邽�߂̋󂫕��������擾����i2013/07/18�j
                cnt = getRoomRemainderPlan( hotelId, planId, 2, frm.getCalDate() );
                // ��Ŏ擾�����󂫕������Ŕ��f����
                if ( frm.getRsvJotaiMark().equals( RSV_ON_MARK ) )
                {
                    // if ( frm.getAllRoomNum() <= cnt )
                    if ( cnt == 0 )
                    {
                        frm.setRsvJotai( RSV_OFF );
                        frm.setRsvJotaiMark( RSV_OFF_MARK );
                    }
                    else
                    {
                        // �\�������m�F
                        if ( checkMaxRsvRommCnt( hotelId, planId, frm.getCalDate() ) == false )
                        {
                            frm.setRsvJotai( RSV_OFF );
                            frm.setRsvJotaiMark( RSV_OFF_MARK );
                        }
                    }
                }

                // �������[�h��0��������̔����Ȃ�
                if ( frm.getChargeModeId() == 0 )
                {
                    frm.setRsvJotai( RSV_STOP );
                    frm.setRsvJotaiMark( RSV_IMPOSSIBLE_MARK );
                    frm.setChargeModeNm( "�̔���~" );

                }
                if ( frm.getCalDate() < Integer.parseInt( DateEdit.getDate( 2 ) ) )
                {
                    frm.setRsvJotaiMark( RSV_IMPOSSIBLE_MARK );
                }
            }
            oneWeekList.add( frm );

            if ( frm.getWeekId() == 6 )
            {
                // �y�j�̏ꍇ�́A�V�������X�g���쐬
                monthlyList.add( oneWeekList );
                oneWeekList = new ArrayList<FormOwnerRsvManageCalendar>();
            }
        }
        // �ŏI���ȍ~�̋�f�[�^�쐬
        monthlyList.add( oneWeekList );

        return(monthlyList);

    }
}
