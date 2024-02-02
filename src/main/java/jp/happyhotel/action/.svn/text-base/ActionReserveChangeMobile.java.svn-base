package jp.happyhotel.action;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.reserve.FormReserveOptionSub;
import jp.happyhotel.reserve.FormReserveOptionSubImp;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.LogicReservePersonalInfoPC;

/**
 * 
 * プラン申込画面（変更） Action
 */

public class ActionReserveChangeMobile extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int type = 0;
        String paramUidLink = "";
        String userId = "";
        String loginMail = "";
        String carrierUrl = "";
        String errMsg = "";
        String url = "";
        String paramMode = "";
        String paramRsvNo = "";
        int paramHotelId = 0;
        int optId = 0;
        int unitPrice = 0;
        int quantity = 0;
        String remarks = "";
        String optNm = "";
        String rsvUserId = "";
        boolean setFlg = false;

        FormReservePersonalInfoPC frmPC = new FormReservePersonalInfoPC();
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        FormReserveOptionSub frmOptSub = new FormReserveOptionSub();
        ReserveCommon rsvCmm = new ReserveCommon();
        ArrayList<FormReserveOptionSubImp> frmOptSubImpList = new ArrayList<FormReserveOptionSubImp>();
        ArrayList<Integer> selOptImpSubIdList = new ArrayList<Integer>();
        ArrayList<String> optSubRemarksList = new ArrayList<String>();
        ArrayList<Integer> newOptIdList = new ArrayList<Integer>();
        ArrayList<Integer> newQuantityList = new ArrayList<Integer>();
        ArrayList<Integer> newUnitPriceList = new ArrayList<Integer>();
        ArrayList<String> newRemarksList = new ArrayList<String>();
        ArrayList<String> newOptNmList = new ArrayList<String>();
        DataLoginInfo_M2 dataLoginInfo_M2;

        String checkHotelId = "";
        String checkRsvNo = "";
        String checkMode = "";

        try
        {
            // キャリアの判別
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
            checkHotelId = request.getParameter( "hotelid" );
            checkRsvNo = request.getParameter( "reserveNo" );
            checkMode = request.getParameter( "mode" );

            // パラメータが消された場合のチェック
            if ( (checkHotelId == null) || (checkRsvNo == null) || (checkMode == null) )
            {
                url = carrierUrl + "reserve_error.jsp";
                errMsg = Message.getMessage( "erro.30009", "パラメータなし" );
                request.setAttribute( "err", errMsg );
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
                loginMail = dataLoginInfo_M2.getMailAddr();
            }
            else
            {
                url = carrierUrl + "reserve_nomember.jsp";
                response.sendRedirect( url );
                return;
            }

            // 処理モードが、既定以外の値の場合のチェック
            if ( !(checkMode.equals( ReserveCommon.MODE_UPD )) )
            {
                url = carrierUrl + "reserve_error.jsp";
                errMsg = Message.getMessage( "erro.30009", "処理モード不正" );
                request.setAttribute( "err", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // 数値のパラメータに、数値以外が指定された場合のチェック
            try
            {
                // パラメータ取得
                paramHotelId = Integer.parseInt( request.getParameter( "hotelid" ) );
                paramRsvNo = request.getParameter( "reserveNo" );
                paramMode = request.getParameter( "mode" );
                paramUidLink = (String)request.getAttribute( "UID-LINK" );
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

            // クッキーのユーザーIDと予約の作成者が同じか確認
            rsvUserId = rsvCmm.getRsvUserId( paramRsvNo );

            if ( userId.compareTo( rsvUserId ) != 0 )
            {
                // 違う場合はエラーページ
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", Message.getMessage( "erro.30004" ) );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
                return;
            }

            // フォームにセット
            frmPC.setSelHotelID( paramHotelId );
            frmPC.setReserveNo( paramRsvNo );
            frmPC.setMode( paramMode );
            frmPC.setLoginUserId( userId );
            frmPC.setLoginMailAddr( loginMail );

            // 予約データ抽出
            logic.setFrm( frmPC );
            logic.getReserveData();
            frmPC = logic.getFrm();

            // ホテル名の取得
            frmPC = rsvCmm.getHotelData( frmPC );

            // 人数リストの取得
            frmPC = rsvCmm.getPlanData( frmPC );

            // 到着予定時刻の取得
            frmPC = rsvCmm.getCiCoTime( frmPC );

            // 予約基本データから駐車場情報取得
            frmPC = rsvCmm.getParking( frmPC );

            // 必須オプション情報取得
            frmOptSubImpList = rsvCmm.getOptionSubImp( frmPC.getSelHotelID(), frmPC.getSelPlanID() );
            frmPC.setFrmOptSubImpList( frmOptSubImpList );

            // 選択済み必須オプション取得
            selOptImpSubIdList = logic.getRsvSelOptImpSubIdList( frmPC.getSelHotelID(), frmPC.getReserveNo() );
            frmPC.setSelOptionImpSubIdList( selOptImpSubIdList );

            // 通常オプション情報取得
            frmOptSub = rsvCmm.getOptionSub( frmPC.getSelHotelID(), frmPC.getSelPlanID(), frmPC.getSelRsvDate() );
            for( int i = 0 ; i < frmOptSub.getUnitPriceList().size() ; i++ )
            {
                optSubRemarksList.add( "" );
            }
            frmPC.setSelOptSubRemarksList( optSubRemarksList );

            // 選択済み通常オプション取得
            frmPC = logic.getRsvSelOptSubIdList( frmPC.getSelHotelID(), frmPC.getReserveNo(), frmPC );

            // 通常オプションデータ作成
            ArrayList<Integer> newOptNumList = new ArrayList<Integer>();
            for( int i = 0 ; i < frmOptSub.getOptIdList().size() ; i++ )
            {
                optId = frmOptSub.getOptIdList().get( i );
                optNm = frmOptSub.getOptNmList().get( i );
                unitPrice = frmOptSub.getUnitPriceList().get( i );
                remarks = frmOptSub.getOptRemarksList().get( i );
                quantity = frmOptSub.getMaxQuantityList().get( i ); //
                setFlg = false;

                for( int j = 0 ; j < frmPC.getSelOptSubIdList().size() ; j++ )
                {
                    if ( optId == frmPC.getSelOptSubIdList().get( j ) )
                    {
                        // 該当オプションが存在する
                        setFlg = true;
                        newOptIdList.add( optId );
                        newOptNmList.add( optNm );
                        newUnitPriceList.add( frmPC.getSelOptSubUnitPriceList().get( j ) );
                        newRemarksList.add( frmPC.getSelOptSubRemarksList().get( j ) );
                        newOptNumList.add( frmPC.getSelOptSubNumList().get( j ) );
                        if ( quantity < frmPC.getSelOptSubNumList().get( j ) )
                        {
                            // 在庫数より登録数の方が多い場合
                            newQuantityList.add( frmPC.getSelOptSubNumList().get( j ) );
                        }
                        else
                        {
                            newQuantityList.add( quantity );
                        }
                        continue;
                    }
                }

                if ( setFlg == false )
                {
                    // 該当オプションが存在しない
                    newOptIdList.add( optId );
                    newOptNmList.add( optNm );
                    newQuantityList.add( quantity );
                    newUnitPriceList.add( unitPrice );
                    newRemarksList.add( remarks );
                    newOptNumList.add( 0 );
                }
            }
            frmOptSub.setMaxQuantityList( newQuantityList );
            frmOptSub.setOptNmList( newOptNmList );
            frmOptSub.setUnitPriceList( newUnitPriceList );
            frmPC.setSelOptSubNumList( newOptNumList );
            frmPC.setSelOptSubRemarksList( newRemarksList );
            frmPC.setFrmOptSub( frmOptSub );

            url = carrierUrl + "reserve_application.jsp";
            request.setAttribute( "dsp", frmPC );

            // デバッグ環境かどうか
            if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
            {
                url = "/_debug_" + url;
            }
            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
            requestDispatcher.forward( request, response );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReserveChangeMobile.execute() ][hotelId = "
                    + paramHotelId + ",reserveNo = " + paramRsvNo + "] Exception", exception );
            try
            {
                url = carrierUrl + "reserve_error.jsp";
                request.setAttribute( "err", Message.getMessage( "erro.30005" ) );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + url + "?" + paramUidLink );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReserveChangeMobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

}
