package jp.happyhotel.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.owner.FormOwnerRsvArrivalAssignList;
import jp.happyhotel.owner.FormOwnerRsvList;
import jp.happyhotel.owner.LogicOwnerRsvArrivalAssignList;

/**
 * 
 * �����\��ꗗ��� Action Class
 */

public class ActionOwnerRsvArrivalAssignList extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;

    /*
     * (�� Javadoc)
     * @see jp.happyhotel.common.BaseAction#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean blnCheck;
        boolean blnRet;

        String strErr = "";
        String strCarrierUrl = "";
        String hotelId = ""; // �z�e��ID
        String errMsg = "";
        int nextday = 0;

        FormOwnerRsvArrivalAssignList dsp0; // form
        LogicOwnerRsvArrivalAssignList lgLPC; // logic
        dsp0 = new FormOwnerRsvArrivalAssignList();
        lgLPC = new LogicOwnerRsvArrivalAssignList();

        blnRet = false;

        try
        {
            // �z�e��ID�擾
            dsp0.setSelHotelID( Integer.parseInt( request.getParameter( "selHotelIDValue" ).toString() ) );
            hotelId = String.valueOf( dsp0.getSelHotelID() );

            // �I���z�e�����𓾂�
            blnRet = lgLPC.getHotelInfo( dsp0 );

            // �ȍ~���o�����擾
            blnCheck = lgLPC.chkDsp( request );

            if ( blnCheck == false )
            {
                dsp0.setSelHotelID( Integer.parseInt( hotelId ) ); // �z�e��ID

                dsp0.setRecCnt( -99 ); // ں��ތ���
                getDspHeader( request, dsp0 );

                strErr = lgLPC.getErrMsg();
                dsp0.setErrMsg( strErr );
                request.setAttribute( "err", "" );
            }
            else
            {
                dsp0.setSelHotelID( Integer.parseInt( hotelId ) );

                // �G���[�łȂ���΃f�[�^�𒊏o����
                getDspHeader( request, dsp0 );
                // To�̓��t�Z�b�g������
                if ( dsp0.getDateTo().equals( "1" ) )
                {
                    // ����
                    nextday = DateEdit.addDay( Integer.valueOf( dsp0.getDateFrom().replaceAll( "/", "" ) ), 1 );
                    dsp0.setDateTo( String.format( "%1$02d/%2$02d/%3$02d", nextday / 10000, nextday % 10000 / 100, nextday % 10000 % 100 ) );
                }
                else
                {
                    // ����
                    dsp0.setDateTo( dsp0.getDateFrom() );
                }
                lgLPC.setDateFrom( dsp0.getDateFrom() );
                lgLPC.setDateTo( dsp0.getDateTo() );
                lgLPC.setRsvNo( dsp0.getReserveNo() );
                lgLPC.setHotelId( Integer.parseInt( hotelId ) );

                // �f�[�^���o
                blnRet = lgLPC.getData( dsp0 );

                if ( blnRet )
                {
                    strErr = lgLPC.getErrMsg();
                    dsp0.setErrMsg( strErr );
                    request.setAttribute( "err", "" );
                }
                else
                {
                    strErr = Message.getMessage( "erro.30001", "�w�肳�ꂽ�����Ɉ�v�����f�[�^" ) + "<br>";
                    dsp0.setRecCnt( -99 );
                    dsp0.setErrMsg( strErr );
                    request.setAttribute( "err", "" );
                }
            }

            request.setAttribute( "FORM_OwnerRsvArrivalAssignList", dsp0 );
            strCarrierUrl = "owner_rsv_arrival_assign_list.jsp";

            /**
             * �{�ԗp
             */
            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + strCarrierUrl );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionOwnerRsvArrivalList.execute() ][hotelId = "
                    + hotelId + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionOwnerRsvArrivalList.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
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
     * �\��ꗗ�̃w�b�_�����擾
     * 
     * @param request
     * @param frm
     */
    public void getDspHeader(HttpServletRequest request, FormOwnerRsvArrivalAssignList frm)
    {
        // ��ʂ̒l���擾
        if ( request.getParameter( "date_f" ) != null )
        {
            frm.setDateFrom( request.getParameter( "date_f" ).toString() );
        }
        if ( request.getParameter( "date_t" ) != null )
        {
            frm.setDateTo( request.getParameter( "date_t" ).toString() );
        }
        if ( request.getParameter( "time_f_h" ) != null )
        {
            frm.setTimeFromHour( Integer.parseInt( request.getParameter( "time_f_h" ) ) );
        }
        if ( request.getParameter( "time_f_m" ) != null )
        {
            frm.setTimeFromMin( Integer.parseInt( request.getParameter( "time_f_m" ) ) );
        }
        if ( request.getParameter( "time_t_h" ) != null )
        {
            frm.setTimeToHour( Integer.parseInt( request.getParameter( "time_t_h" ) ) );
        }
        if ( request.getParameter( "time_t_m" ) != null )
        {
            frm.setTimeToMin( Integer.parseInt( request.getParameter( "time_t_m" ) ) );
        }
        if ( request.getParameter( "rsv_no" ) != null )
        {
            frm.setReserveNo( request.getParameter( "rsv_no" ).toString() );
        }
    }

}
