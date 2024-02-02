package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;

/**
 * 
 * �����\��ꗗ��� business Logic
 */
public class LogicOwnerRsvArrivalOptionList implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID    = 3723491086006455536L;
    private static final String sts1                = "��t";
    private static final String sts2                = "���p�ς�";
    private static final String sts3                = "�L�����Z��";
    private static final String sts4                = "<br>(�����X)";
    private String              errMsg              = "";
    private int                 hotelId             = 0;
    private int                 maxCnt              = 0;
    private String              dateFROM            = "";
    private String              dateTO              = "";
    private String              rsvNo               = "";
    private ArrayList<String>   optionDataList      = new ArrayList<String>(); // �I�v�V����(�ꎟ�I�ȃf�[�^)
    private ArrayList<Integer>  optionCountDataList = new ArrayList<Integer>(); // �I�v�V��������(�ꎟ�I�ȃf�[�^)

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
                    errMsg = errMsg + Message.getMessage( "warn.00009", "�J�n��" ) + "<br>";
                    isResult = false;
                }
            }
            // �J�n����
            if ( (request.getParameter( "time_f_h" ) == null) || (request.getParameter( "time_f_h" ).toString().length() == 0) ||
                    (request.getParameter( "time_f_m" ) == null) || (request.getParameter( "time_f_m" ).toString().length() == 0) )
            {
                errMsg = "";
            }
            else
            {
                // �������l�����������`�F�b�N
                if ( checkTime( request.getParameter( "time_f_h" ), request.getParameter( "time_f_m" ) ) )
                {
                    // �����Ƃ��Đ������ꍇ
                }
                else
                {
                    errMsg = errMsg + Message.getMessage( "warn.00009", "�J�n����" ) + "<br />";
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
                if ( dateT.equals( "0" ) || dateT.equals( "1" ) )
                {
                    // ���t�Ƃ��Đ������ꍇ
                }
                else
                {
                    errMsg = errMsg + Message.getMessage( "warn.00009", "�I����" ) + "<br>";
                    isResult = false;
                }
            }

            // �I������
            if ( (request.getParameter( "time_t_h" ) == null) || (request.getParameter( "time_t_h" ).toString().length() == 0) ||
                    (request.getParameter( "time_t_m" ) == null) || (request.getParameter( "time_t_m" ).toString().length() == 0) )
            {
                errMsg = "";
            }
            else
            {
                // �������l�����������`�F�b�N
                if ( checkTime( request.getParameter( "time_t_h" ), request.getParameter( "time_t_m" ) ) )
                {
                    // �����Ƃ��Đ������ꍇ
                }
                else
                {
                    errMsg = errMsg + Message.getMessage( "warn.00009", "�I������" ) + "<br />";
                    isResult = false;
                }
            }

            if ( isResult )
            {
                // �͈̓`�F�b�N
                // if ( (request.getParameter( "date_f" ) != null) && (request.getParameter( "date_f" ).toString().length() != 0) &&
                // (request.getParameter( "date_t" ) != null) && (request.getParameter( "date_t" ).toString().length() != 0) )
                // {
                // fromVal = Integer.parseInt( dateF.replace( "/", "" ) );
                // toVal = Integer.parseInt( dateT.replace( "/", "" ) );
                // if ( fromVal > toVal )
                // {
                // errMsg = errMsg + Message.getMessage( "warn.00012" ) + "<br>";
                // isResult = false;
                // }
                // }
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
                    // ���p�����łȂ��ꍇ�̓G���[
                    errMsg = errMsg + Message.getMessage( "warn.30007", "�\��ԍ�", "���p�p����" ) + "<br>";
                    isResult = false;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvArrivalAssignList.chkDsp] Exception=" + e.toString() );
            throw e;
        }

        // �߂�l
        return isResult;
    }

    /**
     * ���E���`�F�b�N����
     * 
     * @param hourStr ����
     * @param minStr ��
     * @return
     * @throws Exception
     */
    private static boolean checkTime(String hourStr, String minStr)
    {
        boolean isResult = false;
        int hour = 0;
        int min = 0;

        try
        {
            if ( hourStr != null && minStr != null && !hourStr.equals( "" ) && !minStr.equals( "" ) )
            {
                hour = Integer.parseInt( hourStr );
                min = Integer.parseInt( minStr );
                if ( hour >= 0 && hour < 24 && min >= 0 && min < 60 )
                {
                    isResult = true;
                }
            }
        }
        catch ( Exception e )
        {
        }
        finally
        {
        }
        return(isResult);
    }

    /**
     * �\����擾
     * 
     * @param frm // �ҏW��̫��id
     * @return
     * @throws Exception
     */
    public boolean getData(FormOwnerRsvArrivalOptionList frm) throws Exception
    {

        boolean isResult;
        String query = "";
        String queryWhere = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String convDate = "";
        String weekName = "";
        int rsvDateF = 0;
        int rsvTimeF = 0;
        int rsvDateT = 0;
        int rsvTimeT = 0;
        int intMaxCnt = 0;
        String estTime = "";
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
        rsvTimeF = frm.getTimeFromHour() * 10000 + frm.getTimeFromMin() * 100;
        if ( (this.dateTO == null) || (this.dateTO.toString().length() == 0) )
        {
            rsvDateT = 0;
        }
        else
        {
            convDate = this.dateTO.replace( "/", "" );
            rsvDateT = Integer.parseInt( convDate );
        }
        rsvTimeT = frm.getTimeToHour() * 10000 + frm.getTimeToMin() * 100;

        try
        {
            query = "SELECT R.id, R.reserve_no, R.reserve_date, " +
                    " R.name_last, R.name_first, R.option_charge_total, " +
                    " R.seq, P.plan_name, R.est_time_arrival , " +
                    " R.user_id, R.tel1, R.temp_coming_flag, P.offer_kind, R.parking, R.parking_count, R.demands, R.remarks " +
                    " FROM hh_rsv_reserve R INNER JOIN hh_rsv_plan P" +
                    " ON ( R.id = P.id AND R.plan_id = P.plan_id ) " +
                    " WHERE R.id = ? ";
            // �\����̂�
            queryWhere = " AND R.status = 1 ";

            // �J�n�I���Z�b�g
            if ( rsvDateF != 0 && rsvDateT != 0 )
            {
                queryWhere = queryWhere + " AND ( R.reserve_date = ? AND R.est_time_arrival >= ?) OR";
                queryWhere = queryWhere + " ( R.reserve_date = ? AND R.est_time_arrival < ?)";
            }

            if ( !this.rsvNo.equals( "" ) )
            {
                queryWhere = queryWhere + " AND R.reserve_no LIKE ? ";
            }

            // �o�͏��͗\����A�����\�莞����
            queryWhere = queryWhere + " ORDER BY R.id, R.reserve_date, R.est_time_arrival, R.reserve_no";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query + queryWhere );
            prestate.setInt( 1, this.hotelId );

            seq = 2;
            if ( rsvDateF != 0 )
            {
                prestate.setInt( seq++, rsvDateF );
                prestate.setInt( seq++, rsvTimeF );
            }
            if ( rsvDateT != 0 )
            {
                prestate.setInt( seq++, rsvDateT );
                prestate.setInt( seq++, rsvTimeT );
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
                    setOption( connection, result.getInt( "id" ), result.getString( "reserve_no" ) );
                    // �I�v�V�������쐬
                    for( int i = 0 ; i < optionDataList.size() ; i++ )
                    {
                        convDate = String.valueOf( result.getInt( "reserve_date" ) );
                        weekName = DateEdit.getWeekName( result.getInt( "reserve_date" ) );
                        frm.setRsvDateList( convDate.substring( 0, 4 ) + "/" + convDate.substring( 4, 6 ) + "/" + convDate.substring( 6, 8 ) +
                                "(" + weekName + ")" );
                        frm.setSeqList( result.getInt( "seq" ) );
                        frm.setTel1List( ConvertCharacterSet.convDb2Form( result.getString( "tel1" ).toString() ) );

                        frm.setOptionList( this.optionDataList.get( i ) );
                        frm.setOptionCountList( this.optionCountDataList.get( i ) );
                        frm.setUserNmList( ConvertCharacterSet.convDb2Form( result.getString( "name_last" ).toString() ) +
                                ConvertCharacterSet.convDb2Form( result.getString( "name_first" ).toString() ) );
                        frm.setPlanNmList( ConvertCharacterSet.convDb2Form( result.getString( "plan_name" ).toString() ) );
                        frm.setReserveNoList( result.getString( "reserve_no" ) );

                        estTime = ConvertTime.convTimeStr( result.getInt( "est_time_arrival" ), 3 );
                        frm.setEstTimeArrivalList( estTime );

                        frm.setUserIdList( result.getString( "user_id" ).toString() );

                        frm.setDspList( result.getInt( "offer_kind" ) );
                        // ��������
                        intMaxCnt++;
                    }
                }

                isResult = true;
            }
            else
            {
                // not Found
                isResult = false;

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvArrivalAssignList.getData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return isResult;
    }

    /**
     * �\��̃I�v�V���������o���ĕҏW���ʂ�Ԃ�
     * 
     * @param connection DB�R�l�N�V����
     * @param id �z�e��ID
     * @param reserve_no �\��ԍ�
     * @return �I�v�V�����ҏW����
     */
    private void setOption(Connection connection, int id, String reserve_no)
    {
        ResultSet result = null;
        PreparedStatement prestate = null;
        String sql;

        sql = "SELECT R.quantity, O.option_name, O.option_sub_name, O.option_flag, ISNULL(O.option_flag) AS not_find" +
                " FROM hh_rsv_rel_reserve_option R LEFT JOIN hh_rsv_option O" +
                " ON R.id = O.id AND O.id AND R.option_id = O.option_id AND R.option_sub_id = O.option_sub_id" +
                " WHERE R.id = ? AND R.reserve_no = ?" +
                " ORDER BY O.disp_index, O.option_flag desc, R.quantity desc";

        try
        {
            this.optionDataList = new ArrayList<String>();
            this.optionCountDataList = new ArrayList<Integer>();
            this.optionDataList.clear();
            this.optionCountDataList.clear();
            prestate = connection.prepareStatement( sql );
            prestate.setInt( 1, id );
            prestate.setString( 2, reserve_no );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // �S�����o
                while( result.next() )
                {
                    // �I�v�V�����}�X�^��������΁A�I�v�V�����������o��
                    if ( result.getInt( "not_find" ) == 0 )
                    {
                        if ( result.getInt( "option_flag" ) == 1 ) // �K�{
                        {
                            this.optionDataList.add( ConvertCharacterSet.convDb2Form( result.getString( "option_name" ) ) + "(" +
                                    ConvertCharacterSet.convDb2Form( result.getString( "option_sub_name" ) ) + ")" );
                        }
                        else
                        {
                            this.optionDataList.add( ConvertCharacterSet.convDb2Form( result.getString( "option_name" ) ) );
                        }
                    }
                    // ���ʂ̃Z�b�g
                    if ( result.getInt( "option_flag" ) != 1 )
                    {
                        this.optionCountDataList.add( result.getInt( "quantity" ) );
                    }
                    else
                    {
                        // �K�{�I����1�Œ�
                        this.optionCountDataList.add( 1 );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvOptionList.getOption] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return;
    }

    /**
     * �I���z�e���̏��𓾂�
     * 
     * @param frm
     * @return
     * @throws Exception
     */
    public boolean getHotelInfo(FormOwnerRsvArrivalOptionList frm) throws Exception
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
            Logging.error( "[LogicOwnerRsvArrivalAssignList.getHotelInfo] Exception=" + e.toString() );
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

    public String getErrMsg()
    {
        return errMsg;
    }

    public int getHotelId()
    {
        return this.hotelId;
    }

    public int getMaxCnt()
    {
        return this.maxCnt;
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

    public void setRsvNo(String rsvno)
    {
        this.rsvNo = rsvno;
    }

    public void setHotelId(int hotelid)
    {
        this.hotelId = hotelid;
    }

}
