package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.reserve.FormReserveOptionSub;
import jp.happyhotel.reserve.FormReserveOptionSubImp;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;

/**
 * 
 * テスト用TOP画面 Action Class
 */

public class ActionReserveInitMobile extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int type = 1;
        String carrierUrl = "";
        String paramUidLink = "";
        String rsvDateFormat = "";
        String rsvDate;
        String week;
        int paramHotelId = 0;
        int paramPlanId = 0;
        int paramSeq = 0;
        int paramRsvDate = 0;
        String errMsg = "";
        String url = "";
        String userId = "";
        String paramAdult;
        String paramChild;

        ReserveCommon rsvCmm = new ReserveCommon();
        FormReservePersonalInfoPC frmInfoPC;
        FormReserveOptionSub frmOptSub = new FormReserveOptionSub();
        ArrayList<FormReserveOptionSubImp> frmOptSubImpList = new ArrayList<FormReserveOptionSubImp>();
        ArrayList<String> optSubRemarksList = new ArrayList<String>();
        DataLoginInfo_M2 dataLoginInfo_M2;

        String checkHotelId = "";
        String checkPlanId = "";
        String checkSeq = "";
        String checkRsvDate = "";

        try
        {
            // キャリアの判別
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            type = UserAgent.getUserAgentType( request );
            if ( type == UserAgent.USERAGENT_AU )
            {
                carrierUrl = "../../au/reserve/";
            }
            else if ( type == UserAgent.USERAGENT_VODAFONE )
            {
                carrierUrl = "../../y/reserve/";
            }
            else if ( type == UserAgent.USERAGENT_DOCOMO )
            {
                carrierUrl = "../../i/reserve/";
            }

            // URLパラメータを不正に変更された場合は、エラーページへ遷移させる
            checkHotelId = request.getParameter( "id" );
            checkPlanId = request.getParameter( "plan_id" );
            checkSeq = request.getParameter( "seq" );
            checkRsvDate = request.getParameter( "rsv_date" );
            paramAdult = request.getParameter( "num_adult" );
            paramChild = request.getParameter( "num_child" );
            if ( (paramAdult == null) || (paramAdult.equals( "" ) != false) || (CheckString.numCheck( paramAdult ) == false) )
            {
                paramAdult = "2";
            }
            if ( (paramChild == null) || (paramChild.equals( "" ) != false) || (CheckString.numCheck( paramChild ) == false) )
            {
                paramChild = "0";
            }

            // パラメータが消された場合のチェック
            if ( (checkHotelId == null) || (checkPlanId == null) || (checkSeq == null) || (checkRsvDate == null) )
            {
                url = carrierUrl + "reserve_error.jsp";
                errMsg = Message.getMessage( "erro.30009", "パラメータなし" );
                request.setAttribute( "err", errMsg );
                request.setAttribute( "premiuminfo", "" );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // ログイン情報取得
            dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
            if ( (dataLoginInfo_M2 != null) && (dataLoginInfo_M2.isMemberFlag() == true) )
            {
                // 存在する
                userId = dataLoginInfo_M2.getUserId();
            }
            else
            {
                url = carrierUrl + "reserve_nomember.jsp";
                response.sendRedirect( url );
                return;
            }

            // 数値のパラメータに、数値以外が指定された場合のチェック
            try
            {
                // パラメータ取得
                paramHotelId = Integer.parseInt( request.getParameter( "id" ) );
                paramPlanId = Integer.parseInt( request.getParameter( "plan_id" ) );
                paramSeq = Integer.parseInt( request.getParameter( "seq" ) );
                paramRsvDate = Integer.parseInt( request.getParameter( "rsv_date" ) );
            }
            catch ( Exception exception )
            {
                url = carrierUrl + "reserve_error.jsp";
                errMsg = Message.getMessage( "erro.30009", "パラメータ値不正" );
                request.setAttribute( "err", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // 4号営業は子供人数を外す
            if ( rsvCmm.checkLoveHotelFlag( paramHotelId ) )
            {
                paramChild = "0";
            }

            // パラメータの整合性チェック
            errMsg = rsvCmm.checkParam( paramHotelId, paramPlanId, paramSeq, paramRsvDate, ReserveCommon.USER_KBN_USER, ReserveCommon.MODE_INS );
            if ( errMsg.trim().length() != 0 )
            {
                // チェックNGの場合、エラーページへ遷移する。
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", errMsg );
                request.setAttribute( "premiuminfo", "" );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url );
                requestDispatcher.forward( request, response );
                return;
            }

            // 日付の表示形式
            week = DateEdit.getWeekName( paramRsvDate );
            rsvDate = String.valueOf( paramRsvDate );
            if ( rsvDate.compareTo( "0" ) != 0 )
            {
                rsvDateFormat = rsvDate.substring( 0, 4 ) + "年" + rsvDate.substring( 4, 6 ) + "月" + rsvDate.substring( 6, 8 ) + "日(" + week + ")";
            }
            else
            {
                rsvDateFormat = "日付未選択";
            }

            frmInfoPC = new FormReservePersonalInfoPC();
            frmInfoPC.setMode( ReserveCommon.MODE_INS );
            frmInfoPC.setSelHotelID( paramHotelId );
            frmInfoPC.setSelPlanID( paramPlanId );
            frmInfoPC.setOrgReserveDate( paramRsvDate );
            frmInfoPC.setSelRsvDate( paramRsvDate );
            frmInfoPC.setSeq( paramSeq );
            frmInfoPC.setSelSeq( paramSeq );
            frmInfoPC.setReserveDateFormat( rsvDateFormat );
            frmInfoPC.setParkingUsedKbnInit( 0 );
            frmInfoPC.setPaymemberFlg( dataLoginInfo_M2.isPaymemberFlag() );

            // 無料会員＆有料会員の区別をするためにプランデータ取得
            ReserveCommon rsvcomm = new ReserveCommon();
            frmInfoPC = rsvcomm.getPlanData( frmInfoPC );

            int curDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            int curTime = Integer.parseInt( DateEdit.getTime( 1 ) );
            int checkDateST = DateEdit.addDay( curDate, (frmInfoPC.getRsvEndDate()) );
            int checkDateED = DateEdit.addDay( curDate, (frmInfoPC.getRsvStartDate()) );
            int checkDatePremiumED = DateEdit.addDay( curDate, (frmInfoPC.getRsvStartDatePremium()) );
            int checkDatePremiumST = DateEdit.addDay( curDate, (frmInfoPC.getRsvEndDatePremium()) );

            // 予約可能かチェック
            if ( (paramRsvDate > checkDateST && paramRsvDate < checkDateED &&
                    paramRsvDate >= frmInfoPC.getSalesStartDay() && paramRsvDate <= frmInfoPC.getSalesEndDay()) ||
                    (paramRsvDate == checkDateST && curTime <= frmInfoPC.getRsvEndTime() &&
                            paramRsvDate >= frmInfoPC.getSalesStartDay() && paramRsvDate <= frmInfoPC.getSalesEndDay()) ||
                    (paramRsvDate == checkDateED && curTime >= frmInfoPC.getRsvStartTime() &&
                            paramRsvDate >= frmInfoPC.getSalesStartDay() && paramRsvDate <= frmInfoPC.getSalesEndDay()) )
            {

            }
            else
            {
                String errMSG = "";
                String premiuminfo = "0";
                if ( ((paramRsvDate > checkDatePremiumST) && (paramRsvDate < checkDatePremiumED) &&
                        (paramRsvDate >= frmInfoPC.getSalesStartDay()) && (paramRsvDate <= frmInfoPC.getSalesEndDay())) ||
                        (paramRsvDate == checkDatePremiumST && curTime <= frmInfoPC.getRsvEndTime()) ||
                        (paramRsvDate == checkDatePremiumED && curTime >= frmInfoPC.getRsvStartTime()) )
                {
                    // プレミアム会員が有効の場合は表示を変更
                    String err1 = "本プランはハピホテプレミアム会員様のみ予約可能です。";
                    errMSG = err1;
                    premiuminfo = "1";
                }
                else
                {
                    errMSG = "予約受付の期間外です。お手数ですが、もう一度最初からやり直してください。";
                }
                // チェックNGの場合、エラーページへ遷移する。
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", errMSG );
                request.setAttribute( "premiuminfo", premiuminfo );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url );
                requestDispatcher.forward( request, response );
                return;
            }

            // データチェック
            FormReserveSheetPC frmSheetPC = new FormReserveSheetPC();
            frmSheetPC.setSelHotelId( paramHotelId );
            frmSheetPC.setSelPlanId( paramPlanId );
            frmSheetPC.setRsvNo( "" );
            frmSheetPC.setSeq( paramSeq );
            frmSheetPC.setMode( ReserveCommon.MODE_INS );
            frmSheetPC.setRsvDate( paramRsvDate );
            frmSheetPC.setUserId( userId );
            frmSheetPC = rsvCmm.chkDspMaster( frmSheetPC );
            if ( frmSheetPC.getErrMsg().trim().length() != 0 )
            {
                // チェックNGの場合、エラーページへ遷移する。
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", frmSheetPC.getErrMsg() );
                request.setAttribute( "premiuminfo", "" );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url );
                requestDispatcher.forward( request, response );
                return;
            }

            // 予約人数の範囲チェック
            errMsg = rsvCmm.checkAdultChildNum( paramHotelId, paramPlanId, Integer.parseInt( paramAdult ), Integer.parseInt( paramChild ) );
            if ( errMsg.trim().length() != 0 )
            {
                // チェックNGの場合、エラーページへ遷移する。
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", errMsg );
                request.setAttribute( "premiuminfo", "" );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url );
                requestDispatcher.forward( request, response );
                return;
            }

            // ホテル名称取得
            frmInfoPC = rsvCmm.getHotelData( frmInfoPC );

            // 予約基本より駐車場利用区分、駐車場利用台数を取得
            frmInfoPC = rsvCmm.getParking( frmInfoPC );
            frmInfoPC.setParkingUsedKbnInit( 0 );

            // プラン別料金マスタよりチェックイン開始、終了時刻を取得
            frmInfoPC = rsvCmm.getCiCoTime( frmInfoPC, Integer.parseInt( paramAdult ), Integer.parseInt( paramChild ), paramRsvDate );
            frmInfoPC.setSelEstTimeArrival( -1 );

            // プランマスタより最大人数大人、最大人数子供を取得
            frmInfoPC = rsvCmm.getPlanData( frmInfoPC );
            // if ( frmInfoPC.getNumAdultList().size() > 0 )
            // {
            // frmInfoPC.setSelNumAdult( 2 );
            // }
            // else
            // {
            // frmInfoPC.setSelNumAdult( 1 );
            // }
            frmInfoPC.setSelNumAdult( Integer.parseInt( paramAdult ) );
            frmInfoPC.setSelNumChild( Integer.parseInt( paramChild ) );

            // 部屋残数取得
            frmInfoPC = rsvCmm.getRoomZanSuu( frmInfoPC );

            // 必須オプション情報取得
            frmOptSubImpList = rsvCmm.getOptionSubImp( frmInfoPC.getSelHotelID(), frmInfoPC.getSelPlanID() );
            frmInfoPC.setFrmOptSubImpList( frmOptSubImpList );
            ArrayList<Integer> selOptSubImpIdList = new ArrayList<Integer>();
            // 1つ目のオプションを選択済みとする。
            for( int i = 0 ; i < frmOptSubImpList.size() ; i++ )
            {
                for( int j = 0 ; j < frmOptSubImpList.get( i ).getOptSubIdList().size() ; j++ )
                {
                    if ( j == 0 )
                    {
                        selOptSubImpIdList.add( frmOptSubImpList.get( i ).getOptSubIdList().get( j ) );
                        break;
                    }
                }
                frmInfoPC.setSelOptionImpSubIdList( selOptSubImpIdList );
            }

            // 通常オプション情報取得
            frmOptSub = rsvCmm.getOptionSub( frmInfoPC.getSelHotelID(), frmInfoPC.getSelPlanID(), paramRsvDate );
            frmInfoPC.setFrmOptSub( frmOptSub );
            for( int i = 0 ; i < frmOptSub.getUnitPriceList().size() ; i++ )
            {
                optSubRemarksList.add( "" );
            }
            frmInfoPC.setSelOptSubRemarksList( optSubRemarksList );

            // 画面情報設定;
            request.setAttribute( "dsp", frmInfoPC );

            // デバッグ環境かどうか
            if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
            {
                carrierUrl = "/_debug_" + carrierUrl;
            }

            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + carrierUrl + "reserve_application.jsp" + "?" + paramUidLink );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReserveInitMobile.execute() ][hotelId = "
                    + paramHotelId + " ,planId = " + paramPlanId + " ,reserveDate = " + paramRsvDate + "] Exception", exception );
            try
            {
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", Message.getMessage( "erro.30005" ) );
                request.setAttribute( "premiuminfo", "" );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReserveInitMobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }
}
