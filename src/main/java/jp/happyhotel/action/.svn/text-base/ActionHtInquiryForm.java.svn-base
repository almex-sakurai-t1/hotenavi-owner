package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.SendMail;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataApHotelTerminal;
import jp.happyhotel.data.DataApTouchCi;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.dto.DtoApCommon;
import jp.happyhotel.dto.DtoApInquiryForm;

/**
 * ハピホテアプリチェックインクラス
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtInquiryForm extends BaseAction
{
    private RequestDispatcher requestDispatcher;
    private DtoApCommon       apCommon;
    private DtoApInquiryForm  apInquiry;
    private final int         TIME = 1;
    private final int         DATE = 2;

    /**
     * ハピホテタッチ
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final String MAIL_ADDR = "info@happyhotel.jp";

        // XML出力
        boolean ret = false;
        boolean retHotel = false;
        boolean retTouch = false;
        String paramId;
        String paramSeq;
        String paramErrorCode = "";
        String paramSubmit = "";

        String paramRoomNo = "";
        String paramTerminalId = "";
        String paramCiDateYear = "";
        String paramCiDateMonth = "";
        String paramCiDateDay = "";
        String paramCiTimeHours = "";
        String paramCiTimeMinutes = "";
        String paramUserId = "";
        String paramProcessId = "";
        String paramHotelName = "";
        String rsvNo = "";
        DataHotelBasic dhb = new DataHotelBasic();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();
        DataApHotelTerminal daht = new DataApHotelTerminal();
        DataApTouchCi datc = new DataApTouchCi();

        paramId = request.getParameter( "id" );
        paramSeq = request.getParameter( "seq" );
        paramErrorCode = request.getParameter( "code" );
        paramSubmit = request.getParameter( "submit" );
        paramRoomNo = request.getParameter( "roomNo" );
        paramTerminalId = request.getParameter( "terminalId" );
        paramCiDateYear = request.getParameter( "ciDate_year" );
        paramCiDateMonth = request.getParameter( "ciDate_month" );
        paramCiDateDay = request.getParameter( "ciDate_day" );
        paramCiTimeHours = request.getParameter( "ciTime_hours" );
        paramCiTimeMinutes = request.getParameter( "ciTime_minutes" );
        paramUserId = request.getParameter( "userId" );
        paramProcessId = request.getParameter( "processdId" );
        paramHotelName = request.getParameter( "hotelName" );
        rsvNo = request.getParameter( "rsv" );

        try
        {
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( paramSeq == null || paramSeq.equals( "" ) != false || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( paramErrorCode == null || paramErrorCode.equals( "" ) != false || CheckString.numCheck( paramErrorCode ) == false )
            {
                paramErrorCode = "0";
            }
            if ( paramSubmit == null || paramSubmit.equals( "" ) != false )
            {
                paramSubmit = "";
            }
            if ( paramRoomNo == null || paramRoomNo.equals( "" ) != false )
            {
                paramRoomNo = "";
            }
            if ( paramTerminalId == null || paramTerminalId.equals( "" ) != false || CheckString.numCheck( paramTerminalId ) == false )
            {
                paramTerminalId = "0";
            }
            if ( paramCiDateYear == null || paramCiDateYear.equals( "" ) != false || CheckString.numCheck( paramCiDateYear ) == false )
            {
                paramCiDateYear = "0";
            }
            if ( paramCiDateMonth == null || paramCiDateMonth.equals( "" ) != false || CheckString.numCheck( paramCiDateMonth ) == false )
            {
                paramCiDateMonth = "0";
            }
            if ( paramCiDateDay == null || paramCiDateDay.equals( "" ) != false || CheckString.numCheck( paramCiDateDay ) == false )
            {
                paramCiDateDay = "0";
            }
            if ( paramCiTimeHours == null || paramCiTimeHours.equals( "" ) != false || CheckString.numCheck( paramCiTimeHours ) == false )
            {
                paramCiTimeHours = "0";
            }
            if ( paramCiTimeMinutes == null || paramCiTimeMinutes.equals( "" ) != false || CheckString.numCheck( paramCiTimeMinutes ) == false )
            {
                paramCiTimeMinutes = "0";
            }
            if ( paramUserId == null || paramUserId.equals( "" ) != false )
            {
                paramUserId = "";
            }
            if ( rsvNo == null )
            {
                rsvNo = "";
            }

            retHotel = dhb.getData( Integer.parseInt( paramId ) );
            retTouch = datc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );

            // 部屋情報取得
            dhrm.getData( Integer.parseInt( paramId ), datc.getRoomNo() );
            daht.getDataByRoomNo( Integer.parseInt( paramId ), dhrm.getRoomNo() );
            Logging.info( "[ActionHtInquiryForm]paramId:" + paramId );
            Logging.info( "[ActionHtInquiryForm]paramSeq:" + paramSeq );
            Logging.info( "[ActionHtInquiryForm]paramUserId:" + paramUserId );
            Logging.info( "[ActionHtInquiryForm]paramSubmit:" + paramSubmit );

            if ( paramSubmit.equals( "" ) != false )
            {

                apCommon = new DtoApCommon();
                apInquiry = new DtoApInquiryForm();

                // 共通設定
                apCommon.setHtCheckIn( true );
                apCommon.setConnected( true );
                apCommon.setHotelName( dhb.getName() );
                apCommon.setRoomNo( dhrm.getRoomNameHost() );
                apCommon.setId( Integer.parseInt( paramId ) );
                apCommon.setSeq( Integer.parseInt( paramSeq ) );

                apInquiry.setCiDate( Integer.parseInt( DateEdit.getDate( DATE ) ) );
                apInquiry.setCiTime( Integer.parseInt( DateEdit.getTime( TIME ) ) );
                apInquiry.setProcessedId( 1 );
                apInquiry.setErrorCode( Integer.parseInt( paramErrorCode ) );
                apInquiry.setTerminalId( Integer.toString( daht.getTerminalId() ) );
                apInquiry.setUserId( paramUserId );
                apInquiry.setReserveNo( rsvNo );
                apInquiry.setApCommon( apCommon );
                request.setAttribute( "DtoApInquiryForm", apInquiry );

                requestDispatcher = request.getRequestDispatcher( "inquiryForm.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            else
            {
                DataApErrorHistory dae = new DataApErrorHistory();
                dae.setErrorCode( Integer.parseInt( paramErrorCode ) );
                dae.setErrorSub( 0 );
                dae.setId( Integer.parseInt( paramId ) );
                dae.setTerminalId( Integer.parseInt( paramTerminalId ) );
                dae.setHotenaviId( dhb.getHotenaviId() );
                dae.setRoomNo( dhrm.getRoomNo() );
                dae.setTerminalNo( daht.getTerminalNo() );
                dae.setTouchSeq( Integer.parseInt( paramSeq ) );
                dae.setUserId( paramUserId );
                dae.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dae.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dae.setHotelName( paramHotelName );
                dae.setRoomName( paramRoomNo );
                dae.setEntryDate( Integer.parseInt( paramCiDateYear ) * 10000 + Integer.parseInt( paramCiDateMonth ) * 100 + Integer.parseInt( paramCiDateDay ) );
                dae.setEntryTime( Integer.parseInt( paramCiTimeHours ) * 100 + Integer.parseInt( paramCiTimeMinutes ) );
                dae.setSendFlag( 1 );
                dae.setReserveNo( rsvNo );
                dae.insertData();

                String title = "【ハピホテタッチ問い合わせ." + paramId + "(" + paramHotelName + ")】";
                String text = "";
                text += "エラーコード:" + paramErrorCode + "\r\n";
                text += "入力日付:" + DateEdit.getDate( 1 ) + "\r\n";
                text += "入力時刻:" + DateEdit.getTime( 0 ) + "\r\n";
                text += "====================\r\n";
                text += "ハピホテID:" + paramId + "\r\n";
                text += "ホテナビID:" + dhb.getHotenaviId() + "\r\n";
                text += "ホテル名:" + dhb.getName() + "\r\n";
                text += "ターミナルNo." + paramTerminalId + "\r\n";
                text += "部屋No:" + dhrm.getRoomNo() + "\r\n";
                text += "部屋名:" + dhrm.getRoomNameHost() + "\r\n";
                text += "チェックインコード:" + paramSeq + "\r\n";
                text += "予約No:" + rsvNo + "\r\n";
                text += "====================\r\n";
                text += "ユーザーID:" + paramUserId + "\r\n";
                text += "ホテル名（修正後）:" + paramHotelName + "\r\n";
                text += "部屋番号（修正後）:" + paramRoomNo + "\r\n";
                text += "入室日時（修正後）:" + paramCiDateYear + "年" + paramCiDateMonth + "月" + paramCiDateDay + "日";
                text += "　" + paramCiTimeHours + "時" + paramCiTimeMinutes + "分頃\r\n";
                Logging.info( "[ActionHtInquiryForm]host:" + request.getHeader( "host" ) );
                Logging.info( "[ActionHtInquiryForm]sendMail:" + text );
                if ( request.getHeader( "host" ).equals( "happyhotel.jp" ) )
                {
                    SendMail.send( MAIL_ADDR, MAIL_ADDR, title, text );
                }
                requestDispatcher = request.getRequestDispatcher( "inquiryAfterTransmission.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionHtInquiryForm]Exception:" + exception );
        }
        finally
        {

        }
    }
}
