package jp.happyhotel.others;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApTouchHistory;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataUserFelica;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.touch.TouchCi;

/**
 * �^�b�`�����o�̓��C�u�����N���X
 * 
 */
public class TouchHistory
{
    // �n�s�z�e�A�v������̃^�b�`
    private static final String METHOD_HT_PRINT_COUPON         = "HtPrintCoupon";       // �^�b�`�[���N�[�|����
    private static final String METHOD_HT_DISCOUNT_COUPON      = "HtDiscountCoupon";    // �^�b�`�[�������N�[�|��
    private static final String METHOD_HT_COUPON               = "HtCoupon";            // �^�b�`�[���N�[�|���擾
    private static final String METHOD_REGIST                  = "Regist";

    private static final String METHOD_FIND                    = "Find";

    private static final String METHOD_RSV_DATA_DETAIL         = "RsvDataDetail";
    private static final String METHOD_GET_RSV_ROOM            = "GetRsvRoom";
    private static final String METHOD_RSV_FIX                 = "RsvFix";
    private static final String METHOD_RSV_CANCEL              = "RsvCancel";
    private static final String METHOD_RSV_UNDO_CANCEL         = "RsvUndoCancel";
    private static final String METHOD_RSV_ROOM_CHANGE         = "RsvRoomChange";
    private static final String METHOD_RSV_UNDO_FIX            = "RsvUndoFix";
    private static final String METHOD_VISIT                   = "Visit";
    private static final String METHOD_RSV_MODIFY_ARRIVAL_TIME = "RsvModifyArrivalTime";
    private static final String METHOD_GET_MEMBER_LIST         = "GetMemberList";

    String                      paramMethod                    = "";
    String                      paramIdm                       = "";
    String                      paramSeq                       = "";
    String                      paramUserId                    = "";
    String                      paramRsvNo                     = "";

    /**
     * ap_touch_history ���O�o�̓��\�b�h
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g���
     */
    public boolean touchHistory(int hotelId, HttpServletRequest request)
    {

        boolean ret = false;
        TouchCi tc = null;
        HotelCi hc = null;
        try
        {
            // ���N�G�X�g�p�����[�^�[�̎擾
            this.getRequestParameter( request );

            // ���\�b�h���Ƀ`�F�b�N�C��ID�E���[�U�[ID�擾
            if ( paramMethod.compareTo( METHOD_RSV_DATA_DETAIL ) == 0 || paramMethod.compareTo( METHOD_RSV_UNDO_FIX ) == 0 )
            {
                // �z�e��ID�Ɨ\��NO����擾
                if ( paramSeq.equals( "0" ) && !paramRsvNo.equals( "" ) )
                {
                    paramSeq = String.valueOf( this.getCheckInCode( hotelId, paramRsvNo ) );
                }
            }

            if ( paramMethod.compareTo( METHOD_HT_PRINT_COUPON ) == 0 ||
                    paramMethod.compareTo( METHOD_HT_DISCOUNT_COUPON ) == 0 ||
                    paramMethod.compareTo( METHOD_HT_COUPON ) == 0 )
            {
                // �z�e���^�b�`�f�[�^(ap_touch_ci)�擾
                tc = new TouchCi();
                ret = tc.getData( hotelId, Integer.parseInt( paramSeq ) );
                if ( tc.getTouchCi().getId() > 0 && tc.getTouchCi().getSeq() > 0 )
                {
                    paramUserId = tc.getTouchCi().getUserId();
                }
            }
            else if ( paramMethod.compareTo( METHOD_RSV_DATA_DETAIL ) == 0 ||
                    paramMethod.compareTo( METHOD_GET_RSV_ROOM ) == 0 ||
                    paramMethod.compareTo( METHOD_RSV_FIX ) == 0 ||
                    paramMethod.compareTo( METHOD_RSV_CANCEL ) == 0 ||
                    paramMethod.compareTo( METHOD_RSV_UNDO_CANCEL ) == 0 ||
                    paramMethod.compareTo( METHOD_RSV_ROOM_CHANGE ) == 0 ||
                    paramMethod.compareTo( METHOD_RSV_UNDO_FIX ) == 0 )
            {
                paramUserId = this.getUserId( hotelId, paramRsvNo );
                for( int retry = 0 ; retry < 3 ; retry++ )
                {
                    if ( !paramUserId.equals( "" ) )
                        break;
                    Thread.sleep( 1000 );
                    paramUserId = this.getUserId( hotelId, paramRsvNo );
                }
            }
            else if ( paramMethod.compareTo( METHOD_REGIST ) == 0 )
            {
                DataLoginInfo_M2 dataLoginInfo_M2 = null;
                dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
                if ( dataLoginInfo_M2 != null )
                {
                    paramUserId = dataLoginInfo_M2.getUserId();
                }
            }
            else if ( paramMethod.compareTo( METHOD_VISIT ) == 0 )
            {
                DataUserFelica duf;
                ret = true;
                duf = new DataUserFelica();
                if ( paramIdm == null || paramIdm.equals( "" ) )
                {
                    paramUserId = request.getParameter( "userId" );
                }
                else
                {

                    ret = duf.getUserData( paramIdm );
                    if ( ret )
                    {
                        paramUserId = duf.getUserId();
                    }
                }
                if ( paramUserId == null || paramUserId.equals( "" ) )
                {
                    paramUserId = "";
                    ret = false;
                }

                if ( ret )
                {
                    hc = new HotelCi();
                    ret = hc.getCheckInBeforeData( hotelId, paramUserId );
                    if ( ret )
                    {
                        paramSeq = String.valueOf( hc.getHotelCi().getSeq() );
                    }
                }

            }
            else if ( paramMethod.compareTo( METHOD_FIND ) == 0 )
            {
                boolean retCI;
                DataUserFelica duf;
                duf = new DataUserFelica();
                String seq = request.getParameter( "seq" );
                hc = new HotelCi();
                if ( paramIdm.equals( "" ) == false )
                {
                    ret = duf.getUserData( paramIdm );

                    retCI = hc.getCheckInBeforeData( hotelId, duf.getUserId() );
                    if ( retCI != false )
                    {
                        paramSeq = String.valueOf( hc.getHotelCi().getSeq() );
                        paramUserId = hc.getHotelCi().getIdm();
                    }
                    else
                    {
                        paramSeq = "0";
                        paramUserId = "";
                    }

                }
                else if ( seq.equals( "" ) == false )
                {
                    ret = hc.getData( hotelId, Integer.parseInt( seq ) );
                    if ( ret )
                    {

                        // CiStatus�����X����A�����m�肵�Ă�����0��Ԃ�
                        if ( hc.getHotelCi().getCiStatus() >= 0 && hc.getHotelCi().getCiStatus() < 2 &&
                                hc.getHotelCi().getFixFlag() == 0 )
                        {
                            paramSeq = String.valueOf( hc.getHotelCi().getSeq() );
                            paramUserId = hc.getHotelCi().getIdm();
                        }
                        else
                        {
                            paramSeq = "0";
                            paramUserId = "";
                        }
                    }
                }
                else
                {
                    paramSeq = "0";
                    paramUserId = "";
                }
            }
            else if ( paramMethod.compareTo( METHOD_RSV_MODIFY_ARRIVAL_TIME ) == 0 )
            {
                DataRsvReserve drr = new DataRsvReserve();
                if ( paramRsvNo.compareTo( "" ) != 0 )
                {
                    ret = drr.getData( hotelId, paramRsvNo );
                    if ( ret != false )
                    {
                        paramUserId = drr.getUserId();
                    }
                }
            }
            else if ( paramMethod.compareTo( METHOD_GET_MEMBER_LIST ) == 0 )
            {
                paramUserId = "";
            }
            else
            {
                // �z�e���`�F�b�N�C���f�[�^(hh_hotel_ci)�擾
                hc = new HotelCi();
                ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );
                if ( ret != false )
                {
                    paramUserId = hc.getHotelCi().getUserId();
                }

            }
            // �^�b�`�����t�@�C���o�^
            ret = touchHistory( hotelId, paramMethod, paramSeq, paramUserId, request );

        }
        catch ( Exception e )
        {
            Logging.error( "[TouchHistory.touchHistory() ] " + e.getMessage() );
        }
        return(ret);
    }

    /**
     * ap_touch_history ���O�o�̓��\�b�h
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g���
     */
    public boolean touchHistory(int hotelId, String paramMethod, String ciSeq, String userId, HttpServletRequest request)
    {

        boolean ret = false;
        try
        {

            // �^�b�`�����t�@�C���o�^
            DataApTouchHistory datath = new DataApTouchHistory();
            datath.setId( hotelId );
            if ( !ciSeq.equals( "0" ) )
            {
                datath.setCiSeq( Integer.parseInt( ciSeq ) );
            }
            if ( !userId.equals( "" ) )
            {
                datath.setUserId( userId );
            }
            String queryString = request.getQueryString();

            if ( paramMethod.equals( "HtUseMile" ) )// post�ł��邽��
            {
                queryString += "&point=" + request.getParameter( "point" ) + "&allUse=" + request.getParameter( "allUse" );
            }
            datath.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            datath.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            datath.setDetail( request.getRequestURL() + "?" + queryString );
            ret = datath.insertData();

        }
        catch ( Exception e )
        {
            Logging.error( "[TouchHistory.touchHistory() ] " + e.getMessage() );
        }
        return(ret);

    }

    /**
     * ���N�G�X�g����p�����[�^�[���擾����
     * 
     * @param request
     * @return String �G���[���b�Z�[�W
     */
    private void getRequestParameter(HttpServletRequest request)
    {

        // ���\�b�h
        this.paramMethod = request.getParameter( "method" );
        if ( this.paramMethod == null )
        {
            this.paramMethod = "";
        }

        // idm
        this.paramIdm = request.getParameter( "idm" );
        if ( (this.paramIdm == null) || (this.paramIdm.compareTo( "" ) == 0) )
        {
            this.paramIdm = "";
        }

        // �\��NO
        this.paramRsvNo = request.getParameter( "rsvNo" );
        if ( (this.paramRsvNo == null) || (this.paramRsvNo.equals( "" ) != false) )
        {
            this.paramRsvNo = "";
        }

        // �`�F�b�N�C���R�[�h
        this.paramSeq = request.getParameter( "seq" );
        if ( (this.paramSeq == null) || (this.paramSeq.compareTo( "" ) == 0) || CheckString.numCheck( this.paramSeq ) == false )
        {
            this.paramSeq = request.getParameter( "h_seq" );// HtOverwriteCustom�̂Ƃ�
            if ( (this.paramSeq == null) || (this.paramSeq.compareTo( "" ) == 0) || CheckString.numCheck( this.paramSeq ) == false )
            {
                this.paramSeq = "0";
            }
        }

        // ���[�U�[ID
        paramUserId = request.getParameter( "userId" );// HtInquiryForm�̂Ƃ�
        if ( (paramUserId == null) || (paramUserId.compareTo( "" ) == 0) )
        {
            paramUserId = request.getParameter( "user_id" );// HtNonAuto�̂Ƃ�
            if ( (paramUserId == null) || (paramUserId.compareTo( "" ) == 0) )
            {
                paramUserId = "";
            }
        }
    }

    /**
     * �z�e���`�F�b�N�C���f�[�^�擾
     * 
     * @param �\��No
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private int getCheckInCode(int id, String rsv_no)
    {
        int ciCode = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT seq FROM hh_hotel_ci WHERE id=? AND rsv_no=? AND ci_status<=1 ORDER BY seq DESC,sub_seq DESC LIMIT 0,1";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, rsv_no );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ciCode = result.getInt( "seq" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsv.getCheckInCode] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ciCode);
    }

    private String getUserId(int id, String rsv_no)
    {
        String user_id = "";
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT user_id FROM newRsvDB.hh_rsv_reserve WHERE id=? AND reserve_no=? LIMIT 0,1";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, rsv_no );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    user_id = result.getString( "user_id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsv.getUserId] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(user_id);
    }
}
