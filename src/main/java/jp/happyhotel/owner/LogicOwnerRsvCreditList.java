package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.DecodeData;
import jp.happyhotel.common.GMOCcsCredit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.data.DataRsvCreditSales;
import jp.happyhotel.data.DataRsvReserve;

/**
 * 
 * �N���W�b�g������� business Logic
 */
public class LogicOwnerRsvCreditList implements Serializable
{

    /**
     *
     */
    public static final int    ROWSTATUS_SALES        = 1;                       // ����f�[�^
    public static final int    ROWSTATUS_CANCEL       = 2;                       // �L�����Z���f�[�^
    public static final int    ROWSTATUS_CANCEL_SALES = 3;                       // �L�����Z���ϔ���f�[�^

    private static final long  serialVersionUID       = -115937955416856550L;
    private static final int   listmax                = 5;                       // ���ʍő喾�ו\������
    private String             errMsg                 = "";
    private int                hotelId                = 0;
    private int                maxCnt                 = 0;
    private String             dateFROM               = "";
    private String             dateTO                 = "";
    private String             rsvNo                  = "";
    private ArrayList<String>  reserveNoList          = new ArrayList<String>(); // �\��ԍ�
    private ArrayList<Integer> reserveSeqList         = new ArrayList<Integer>(); // �V�[�P���X�ԍ�
    private ArrayList<Integer> reserveDateList        = new ArrayList<Integer>(); // �\���
    private ArrayList<Integer> reserveEsttimeList     = new ArrayList<Integer>(); // �\�񎞍�
    private ArrayList<String>  spidList               = new ArrayList<String>(); // �Ώ�SPID
    private ArrayList<Integer> salesDateList          = new ArrayList<Integer>(); // ������t
    private ArrayList<Integer> generateDateList       = new ArrayList<Integer>(); // �������t
    private ArrayList<Integer> generateTimeList       = new ArrayList<Integer>(); // ��������
    private ArrayList<String>  salesnameList          = new ArrayList<String>(); // ����(��������)
    private ArrayList<Integer> amountList             = new ArrayList<Integer>(); // �������z
    private ArrayList<Integer> approvenoList          = new ArrayList<Integer>(); // ���F�ԍ�
    private ArrayList<String>  forwardList            = new ArrayList<String>(); // �d����
    private ArrayList<String>  tranidList             = new ArrayList<String>(); // �g�����U�N�V����ID
    private ArrayList<String>  cancelTranidList       = new ArrayList<String>(); // ������g�����U�N�V����ID
    private ArrayList<Integer> cancelflagList         = new ArrayList<Integer>(); // ����t���O

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
            Logging.error( "[LogicOwnerRsvCreditList.chkDsp] Exception=" + e.toString() );
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
    public boolean getHotelInfo(FormOwnerRsvCreditList frm) throws Exception
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
            Logging.error( "[LogicOwnerRsvCreditList.getHotelInfo] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return isResult;
    }

    /**
     * ����������
     * 
     * @param reserveno �\��ԍ�
     * @param seqno �V�[�P���X�ԍ�
     * @param hotelid �z�e��ID
     * @return ����(True������ false�����s)
     */
    public boolean deleteSales(String reserveno, String seqno, int hotelid)
    {
        boolean isResult = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        GMOCcsCredit gmoccs = null;
        DataRsvCreditSales dataSales = null;
        DataRsvReserve dataReserve = null;

        try
        {
            // DB����w�肵������f�[�^���擾
            query = "select hh_rsv_credit_sales.reserve_no, hh_rsv_credit_sales.sales_name, hh_rsv_credit_sales.seq_no, hh_rsv_credit_sales.spid, hh_rsv_credit.card_no, hh_rsv_credit.limit_date, hh_rsv_credit_sales.amount, hh_rsv_credit_sales.tranid ";
            query += " from hh_rsv_credit_sales inner join hh_rsv_credit on hh_rsv_credit_sales.reserve_no = hh_rsv_credit.reserve_no where hh_rsv_credit_sales.reserve_no = ? AND hh_rsv_credit_sales.seq_no = ?";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, reserveno );
            prestate.setString( 2, seqno );

            result = prestate.executeQuery();
            if ( result != null && result.next() != false )
            {
                // �Ώۂ̃g�����U�N�V����ID�̔������������s��(gmoccs)
                gmoccs = new GMOCcsCredit();
                gmoccs.setSpid( result.getString( "spid" ) );
                gmoccs.setAmount( result.getInt( "amount" ) );
                gmoccs.setCancelTranId( result.getString( "tranid" ) );
                gmoccs.setCardNo( DecodeData.decodeString( "axpol ptmhyeeahl".getBytes( "8859_1" ), "s h t t i s n h ".getBytes( "8859_1" ), new String( result.getString( "card_no" ) ) ) );
                gmoccs.setCardExpire( String.format( "%1$04d", result.getInt( "limit_date" ) ) );
                if ( gmoccs.execCancelSales() != false )
                {
                    // �N���W�b�g���㗚����ǉ�
                    dataSales = new DataRsvCreditSales();
                    dataSales.setReserve_no( reserveno );
                    dataSales.setSpid( result.getString( "spid" ) );
                    dataSales.setSales_date( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dataSales.setGenerate_date( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dataSales.setGenerate_time( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    dataSales.setSales_name( result.getString( "sales_name" ) );
                    dataSales.setReserve_amount( result.getInt( "amount" ) );
                    dataSales.setAmount( result.getInt( "amount" ) );
                    dataSales.setPercent( 100 );
                    dataSales.setApprove( Integer.valueOf( gmoccs.getApproveNo() ) );
                    dataSales.setForward( gmoccs.getForwardCode() );
                    dataSales.setTranid( gmoccs.getTranId() );
                    dataSales.setCancel_tranid( result.getString( "tranid" ) );
                    dataSales.setCancel_flag( 1 );
                    dataSales.setId( hotelid );
                    dataSales.insertData();
                    // hh_rsv_reserve�̎���g�����U�N�V����ID���X�V
                    dataReserve = new DataRsvReserve();
                    if ( dataReserve.getData( hotelid, reserveno ) != false )
                    {
                        dataReserve.setTranid( gmoccs.getTranId() );
                        dataReserve.setCancelTranid( result.getString( "tranid" ) );
                        if ( dataReserve.updateData( hotelid, reserveno ) != false )
                        {
                            isResult = true;
                        }
                        else
                        {
                            errMsg = Message.getMessage( "erro.30002", "�\��f�[�^�̃g�����U�N�V����ID�X�V" );
                        }
                    }
                    else
                    {
                        errMsg = Message.getMessage( "erro.30002", "�\��f�[�^�̎擾" );
                    }
                }
                else
                {
                    errMsg = Message.getMessage( "erro.30002", "����f�[�^�̎������" );
                }
            }
            else
            {
                errMsg = Message.getMessage( "erro.30002", "�Ώۂ̔��㗚���f�[�^�̎擾" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCreditList.deleteSales] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(isResult);
    }

    /**
     * �N���W�b�g�������擾
     * 
     * @param frm // �ҏW��̫��id
     * @param objKbn // ���o�Ώ�
     * @param pageCnt // �y�[�W�ԍ�(0����)
     * @param mode // �y�[�W���O�@�\(ALL:�y�[�W���O�����S��, PARTS:�y�[�W���O�L)
     * @return
     * @throws Exception
     */
    public boolean getData(FormOwnerRsvCreditList frm, int objKbn, int pageCnt, String mode) throws Exception
    {

        boolean isResult;
        String query = "";
        String queryWhere = "";
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
            query = "SELECT hh_rsv_credit_sales.reserve_no, hh_rsv_credit_sales.seq_no, hh_rsv_reserve.reserve_date, hh_rsv_reserve.est_time_arrival, hh_rsv_credit_sales.seq_no, hh_rsv_credit_sales.spid, " +
                    " hh_rsv_credit_sales.sales_date, hh_rsv_credit_sales.generate_date, hh_rsv_credit_sales.generate_time, " +
                    " hh_rsv_credit_sales.sales_name, hh_rsv_credit_sales.reserve_amount, hh_rsv_credit_sales.amount , " +
                    " hh_rsv_credit_sales.percent, hh_rsv_credit_sales.approve, hh_rsv_credit_sales.forward, " +
                    " hh_rsv_credit_sales.tranid, hh_rsv_credit_sales.cancel_tranid,cancel_flag" +
                    " FROM hh_rsv_credit_sales inner join hh_rsv_reserve on hh_rsv_credit_sales.reserve_no = hh_rsv_reserve.reserve_no where hh_rsv_credit_sales.id = ?";
            if ( objKbn == 2 )
            {
                // ����f�[�^�̂�
                queryWhere = " AND hh_rsv_credit_sales.cancel_flag = 0 ";
            }
            else if ( objKbn == 3 )
            {
                // ����f�[�^�̂�
                queryWhere = " AND hh_rsv_credit_sales.cancel_flag = 1 ";
            }
            else
            {
                // �S�Ă̏ꍇ�͏�������
            }
            // ���t�J�n
            if ( rsvDateF != 0 )
            {
                queryWhere = queryWhere + " AND hh_rsv_credit_sales.sales_date >= ? ";
            }
            // ���t�I��
            if ( rsvDateT != 0 )
            {
                queryWhere = queryWhere + " AND hh_rsv_credit_sales.sales_date <= ? ";
            }

            if ( !this.rsvNo.equals( "" ) )
            {
                queryWhere = queryWhere + " AND hh_rsv_credit_sales.reserve_no LIKE ? ";
            }

            // �o�͏��͔������A����������
            queryWhere = queryWhere + " ORDER BY hh_rsv_reserve.reserve_date, hh_rsv_reserve.est_time_arrival, hh_rsv_credit_sales.reserve_no";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query + queryWhere );
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
                    this.setReserveNoList( result.getString( "reserve_no" ) );
                    this.setReserveSeqList( result.getInt( "seq_no" ) );
                    this.setReserveDateList( result.getInt( "reserve_date" ) );
                    this.setReserveEsttimeList( result.getInt( "est_time_arrival" ) );
                    this.setSpidList( result.getString( "spid" ) );
                    this.setSalesDateList( result.getInt( "sales_date" ) );
                    this.setGenerateDateList( result.getInt( "generate_date" ) );
                    this.setGenerateTimeList( result.getInt( "generate_time" ) );
                    this.setSalesnameList( result.getString( "sales_name" ) );
                    this.setAmountList( result.getInt( "amount" ) );
                    this.setApprovenoList( result.getInt( "approve" ) );
                    this.setForwardList( result.getString( "forward" ) );
                    this.setTranidList( result.getString( "tranid" ) );
                    this.setCancelTranidList( result.getString( "cancel_tranid" ) );
                    this.setCancelflagList( result.getInt( "cancel_flag" ) );

                    // ��������
                    intMaxCnt++;
                }

                if ( mode.equals( "ALL" ) )
                {
                    // �S����ʕ\���̏ꍇ�͂��̂܂܃t�H�[���֕ҏW����
                    for( i = 0 ; i < intMaxCnt - 1 ; i++ )
                    {
                        frm.setReserveNoList( this.reserveNoList.get( i ) );
                        frm.setReserveSeqList( this.reserveSeqList.get( i ) );
                        frm.setSpidList( this.spidList.get( i ) );
                        weekName = DateEdit.getWeekName( this.salesDateList.get( i ) );
                        frm.setSalesDateList( String.format( "%1$02d%2$02d(%s)", this.salesDateList.get( i ) % 10000 / 100, this.salesDateList.get( i ) % 10000 % 100, weekName ) );
                        weekName = DateEdit.getWeekName( this.reserveDateList.get( i ) );
                        frm.setReserveDateList( String.format( "%1$02d%2$02d(%s)", this.reserveDateList.get( i ) % 10000 / 100, this.reserveDateList.get( i ) % 10000 % 100, weekName ) );
                        estTime = ConvertTime.convTimeStr( this.getReserveEsttimeList().get( i ), 3 );
                        frm.setReserveEsttimeList( estTime );
                        frm.setGenerateDateList( this.generateDateList.get( i ) );
                        frm.setGenerateTimeList( this.generateTimeList.get( i ) );
                        frm.setSalesnameList( this.salesnameList.get( i ) );
                        frm.setAmountList( this.amountList.get( i ) );
                        frm.setApprovenoList( this.approvenoList.get( i ) );
                        frm.setForwardList( this.getForwardList().get( i ) );
                        frm.setTranidList( this.tranidList.get( i ) );
                        frm.setCancelTranidList( this.cancelTranidList.get( i ) );
                        frm.setCancelflagList( this.cancelflagList.get( i ) );
                        if ( this.cancelflagList.get( i ) == 0 )
                        {
                            // ����f�[�^
                            frm.setRowstatusList( ROWSTATUS_CANCEL );
                        }
                        else
                        {

                            // ����f�[�^
                            if ( checkCancelSalesData( this.reserveNoList.get( i ), this.tranidList.get( i ) ) == true )
                            {
                                // ����ϔ���f�[�^
                                frm.setRowstatusList( ROWSTATUS_CANCEL_SALES );
                            }
                            else
                            {
                                // �����Ȕ���f�[�^
                                frm.setRowstatusList( ROWSTATUS_SALES );
                            }
                        }
                    }

                    isResult = true;
                    frm.setPageMax( intMaxCnt - 1 );
                    frm.setPageSt( 0 );
                    frm.setPageEd( intMaxCnt - 1 );
                }
                else if ( mode.equals( "PARTS" ) )
                {

                    // ���o���������̂����ꕔ���̂݃t�H�[���֕ҏW����
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
                            frm.setReserveNoList( this.reserveNoList.get( i ) );
                            frm.setReserveSeqList( this.reserveSeqList.get( i ) );
                            frm.setSpidList( this.spidList.get( i ) );
                            weekName = DateEdit.getWeekName( this.salesDateList.get( i ) );
                            frm.setSalesDateList( String.format( "%1$02d/%2$02d", this.salesDateList.get( i ) % 10000 / 100, this.salesDateList.get( i ) % 10000 % 100 ) + "(" + weekName + ")" );
                            weekName = DateEdit.getWeekName( this.reserveDateList.get( i ) );
                            frm.setReserveDateList( String.format( "%1$02d/%2$02d", this.reserveDateList.get( i ) % 10000 / 100, this.reserveDateList.get( i ) % 10000 % 100 ) + "(" + weekName + ")" );
                            estTime = ConvertTime.convTimeStr( this.getReserveEsttimeList().get( i ), 3 );
                            frm.setReserveEsttimeList( estTime );
                            frm.setGenerateDateList( this.generateDateList.get( i ) );
                            frm.setGenerateTimeList( this.generateTimeList.get( i ) );
                            frm.setSalesnameList( this.salesnameList.get( i ) );
                            frm.setAmountList( this.amountList.get( i ) );
                            frm.setApprovenoList( this.approvenoList.get( i ) );
                            frm.setForwardList( this.getForwardList().get( i ) );
                            frm.setTranidList( this.tranidList.get( i ) );
                            frm.setCancelTranidList( this.cancelTranidList.get( i ) );
                            frm.setCancelflagList( this.cancelflagList.get( i ) );
                            if ( this.cancelflagList.get( i ) == 1 )
                            {
                                // ����f�[�^
                                frm.setRowstatusList( ROWSTATUS_CANCEL );
                            }
                            else
                            {

                                // ����f�[�^
                                if ( checkCancelSalesData( this.reserveNoList.get( i ), this.tranidList.get( i ) ) == true )
                                {
                                    // ����ϔ���f�[�^
                                    frm.setRowstatusList( ROWSTATUS_CANCEL_SALES );
                                }
                                else
                                {
                                    // �����Ȕ���f�[�^
                                    frm.setRowstatusList( ROWSTATUS_SALES );
                                }
                            }
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
            Logging.error( "[LogicOwnerRsvCreditList.getData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return isResult;
    }

    /**
     * ����f�[�^�L�����Z�������݊m�F����
     * 
     * @param reserveno �\��ԍ�
     * @param tranid �L�����Z�����g����ID
     * 
     * @return true���L�����Z���f�[�^���� false���L�����Z���f�[�^�Ȃ�
     * 
     */
    private boolean checkCancelSalesData(String reserveno, String tranid)
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
            query = "SELECT reserve_no from hh_rsv_credit_sales where reserve_no = ? AND cancel_tranid = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, reserveno );
            prestate.setString( 2, tranid );

            result = prestate.executeQuery();

            if ( result != null && result.next() != false )
            {
                isResult = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCreditList.checkCancelSalesData] Exception=" + e.toString() );
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

    public ArrayList<String> getReserveNoList()
    {
        return reserveNoList;
    }

    public int getHotelId()
    {
        return this.hotelId;
    }

    public int getMaxCnt()
    {
        return this.maxCnt;
    }

    public ArrayList<String> getSpidList()
    {
        return spidList;
    }

    public ArrayList<Integer> getSalesDateList()
    {
        return salesDateList;
    }

    public ArrayList<Integer> getGenerateDateList()
    {
        return generateDateList;
    }

    public ArrayList<Integer> getGenerateTimeList()
    {
        return generateTimeList;
    }

    public ArrayList<String> getSalesnameList()
    {
        return salesnameList;
    }

    public ArrayList<Integer> getAmountList()
    {
        return amountList;
    }

    public ArrayList<Integer> getApprovenoList()
    {
        return approvenoList;
    }

    public ArrayList<String> getForwardList()
    {
        return forwardList;
    }

    public ArrayList<String> getTranidList()
    {
        return tranidList;
    }

    public ArrayList<String> getCancelTranidList()
    {
        return cancelTranidList;
    }

    public ArrayList<Integer> getCancelflagList()
    {
        return cancelflagList;
    }

    public ArrayList<Integer> getReserveDateList()
    {
        return reserveDateList;
    }

    public ArrayList<Integer> getReserveEsttimeList()
    {
        return reserveEsttimeList;
    }

    public ArrayList<Integer> getReserveSeqList()
    {
        return reserveSeqList;
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

    public void setReserveNoList(String reserveno)
    {
        this.reserveNoList.add( reserveno );
    }

    public void setHotelId(int hotelid)
    {
        this.hotelId = hotelid;
    }

    public void setSpidList(String spidList)
    {
        this.spidList.add( spidList );
    }

    public void setSalesDateList(Integer salesDateList)
    {
        this.salesDateList.add( salesDateList );
    }

    public void setGenerateDateList(Integer generateDateList)
    {
        this.generateDateList.add( generateDateList );
    }

    public void setGenerateTimeList(Integer generateTimeList)
    {
        this.generateTimeList.add( generateTimeList );
    }

    public void setSalesnameList(String salesnameList)
    {
        this.salesnameList.add( salesnameList );
    }

    public void setAmountList(Integer amountList)
    {
        this.amountList.add( amountList );
    }

    public void setApprovenoList(Integer approvenoList)
    {
        this.approvenoList.add( approvenoList );
    }

    public void setForwardList(String forwardList)
    {
        this.forwardList.add( forwardList );
    }

    public void setTranidList(String tranidList)
    {
        this.tranidList.add( tranidList );
    }

    public void setCancelTranidList(String cancelTranidList)
    {
        this.cancelTranidList.add( cancelTranidList );
    }

    public void setCancelflagList(Integer cancelflagList)
    {
        this.cancelflagList.add( cancelflagList );
    }

    public void setReserveDateList(Integer reserveDateList)
    {
        this.reserveDateList.add( reserveDateList );
    }

    public void setReserveEsttimeList(Integer reserveEstTimeList)
    {
        this.reserveEsttimeList.add( reserveEstTimeList );
    }

    public void setReserveSeqList(Integer reserveSeqList)
    {
        this.reserveSeqList.add( reserveSeqList );
    }
}
