package jp.happyhotel.action;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataRsvRoom;
import jp.happyhotel.owner.FormOwnerRsvManageCalendar;
import jp.happyhotel.owner.LogicOwnerRsvManageCalendar;
import jp.happyhotel.reserve.FormReserveOptionSub;
import jp.happyhotel.reserve.FormReserveOptionSubImp;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveInitPC;
import jp.happyhotel.reserve.LogicReservePersonalInfoPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;
import jp.happyhotel.user.UserBasicInfo;

/**
 * 
 * 個人情報入力更新画面(PC版) Action Class
 */

public class ActionReservePersonalInfoUpdPC extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int paramHotelId = 0;
        String paramRsvNo = "";
        String paramMode = "";
        String paramUserKbn = "";
        int currentDate = 0;
        int planId = 0;
        String reserveDate = "";
        String strCarrierUrl = "";
        int ownerUserId = 0;
        String userId = "";
        int optId = 0;
        int unitPrice = 0;
        int quantity = 0;
        String remarks = "";
        String optNm = "";
        String rsvUserId = "";
        boolean setFlg = false;
        boolean isLoginUser = false;
        String errMsg = "";

        FormReservePersonalInfoPC frm = new FormReservePersonalInfoPC();
        FormReserveSheetPC frmSheet;
        FormReserveOptionSub frmOptSub = new FormReserveOptionSub();
        LogicReservePersonalInfoPC logic = new LogicReservePersonalInfoPC();
        LogicReserveSheetPC logicSheet;
        ReserveCommon rsvCmm = new ReserveCommon();
        DataLoginInfo_M2 dataLoginInfo_M2;
        ArrayList<FormReserveOptionSubImp> frmOptSubImpList = new ArrayList<FormReserveOptionSubImp>();
        ArrayList<Integer> selOptImpSubIdList = new ArrayList<Integer>();
        ArrayList<String> optSubRemarksList = new ArrayList<String>();
        ArrayList<Integer> newOptIdList = new ArrayList<Integer>();
        ArrayList<Integer> newQuantityList = new ArrayList<Integer>();
        ArrayList<Integer> newUnitPriceList = new ArrayList<Integer>();
        ArrayList<String> newRemarksList = new ArrayList<String>();
        ArrayList<String> newOptNmList = new ArrayList<String>();
        boolean payMemberFlg = false;

        String checkHotelId = "";
        String checkRsvNo = "";
        String checkMode = "";
        String checkUserKbn = "";
        int numchild = 0;

        try
        {
            // URLパラメータを不正に変更された場合は、エラーページへ遷移させる
            checkHotelId = request.getParameter( "id" );
            checkRsvNo = request.getParameter( "rsvno" );
            checkMode = request.getParameter( "mode" );
            checkUserKbn = request.getParameter( "usr" );

            // パラメータが消された場合のチェック
            if ( (checkHotelId == null) || (checkRsvNo == null) || (checkMode == null) || (checkUserKbn == null) )
            {
                errMsg = Message.getMessage( "erro.30009", "パラメータなし" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // 処理モードが、既定以外の値の場合のチェック
            if ( !(checkMode.equals( ReserveCommon.MODE_UPD )) )
            {
                errMsg = Message.getMessage( "erro.30009", "処理モード不正" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // ユーザ区分が、既定以外の値の場合のチェック
            if ( !(checkUserKbn.equals( ReserveCommon.USER_KBN_USER )) && !(checkUserKbn.equals( ReserveCommon.USER_KBN_OWNER )) )
            {
                errMsg = Message.getMessage( "erro.30009", "ユーザ区分不正" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // 数値のパラメータに、数値以外が指定された場合のチェック
            try
            {
                // 画面のパラメータ取得
                paramHotelId = Integer.parseInt( request.getParameter( "id" ) );
                paramRsvNo = request.getParameter( "rsvno" );
                paramMode = request.getParameter( "mode" );
                paramUserKbn = request.getParameter( "usr" );
            }
            catch ( Exception exception )
            {
                errMsg = Message.getMessage( "erro.30009", "パラメータ値不正" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            frm.setSelHotelID( paramHotelId );
            frm.setReserveNo( paramRsvNo );
            frm.setMode( paramMode );
            frm.setLoginUserKbn( paramUserKbn );

            // ログインユーザー情報取得
            if ( paramUserKbn.equals( ReserveCommon.USER_KBN_USER ) )
            {
                // ユーザーの場合
                dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

                if ( dataLoginInfo_M2 == null )
                {
                    // ログアウト状態の場合はエラー
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_nomember.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }

                payMemberFlg = dataLoginInfo_M2.isMemberFlag();
                if ( dataLoginInfo_M2.isMemberFlag() == true )
                {
                    // 存在する
                    isLoginUser = true;
                    userId = dataLoginInfo_M2.getUserId();
                }
            }
            else if ( paramUserKbn.equals( ReserveCommon.USER_KBN_OWNER ) )
            {
                // オーナーの場合
                ownerUserId = OwnerRsvCommon.getCookieLoginUserId( request.getCookies() );
                if ( ownerUserId != 0 )
                {
                    // 存在する
                    isLoginUser = true;
                }
                userId = Integer.toString( ownerUserId );
                payMemberFlg = true;
            }

            if ( (isLoginUser == false) || ((!paramUserKbn.equals( ReserveCommon.USER_KBN_USER )) && (!paramUserKbn.equals( ReserveCommon.USER_KBN_OWNER ))) )
            {
                // ログインユーザーが存在しない場合はエラー
                errMsg = Message.getMessage( "erro.30004" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // クッキーのユーザーIDと予約の作成者が同じか確認
            if ( paramUserKbn.equals( ReserveCommon.USER_KBN_USER ) )
            {
                rsvUserId = rsvCmm.getRsvUserId( paramRsvNo );
                if ( userId.compareTo( rsvUserId ) != 0 )
                {
                    // 違う場合はエラーページ
                    errMsg = Message.getMessage( "erro.30004" );
                    request.setAttribute( "errMsg", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }
            }

            // ステータスが受付以外は予約票を表示
            if ( rsvCmm.checkStatus( paramHotelId, paramRsvNo, 0 ) == false )
            {
                // データの抽出
                frmSheet = new FormReserveSheetPC();
                frmSheet.setSelHotelId( paramHotelId );
                frmSheet.setRsvNo( paramRsvNo );

                logicSheet = new LogicReserveSheetPC();
                logicSheet.setFrm( frmSheet );
                logicSheet.getData( 2 );

                strCarrierUrl = "/reserve/reserve_sheet_PC.jsp";
                request.setAttribute( "FORM_ReserveSheetPC", frmSheet );

                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + strCarrierUrl );
                requestDispatcher.forward( request, response );
                return;
            }

            // 予約データの抽出
            frm.setPaymemberFlg( payMemberFlg );
            logic.setFrm( frm );
            logic.getReserveData();
            frm = logic.getFrm();

            // 4号営業ホテルは子供人数を強制的に0扱い
            if ( rsvCmm.checkLoveHotelFlag( paramHotelId ) == false )
            {
                numchild = frm.getSelNumChild();
            }

            // 予約登録したユーザーのメールアドレス取得
            UserBasicInfo ubi = new UserBasicInfo();
            ubi.getUserBasic( frm.getLoginUserId() );
            frm.setLoginTel( ubi.getUserInfo().getTel1() );

            String mailAddr = "";
            ArrayList<String> mailAddrList = new ArrayList<String>();
            // メールアドレスをセット
            if ( ubi.getUserInfo().getMailAddr().equals( "" ) == false )
            {
                mailAddr = ubi.getUserInfo().getMailAddr();
                mailAddrList.add( ubi.getUserInfo().getMailAddr() );
            }
            if ( ubi.getUserInfo().getMailAddrMobile().equals( "" ) == false )
            {
                if ( mailAddr.equals( "" ) != false )
                {
                    mailAddr = ubi.getUserInfo().getMailAddrMobile();
                }
                mailAddrList.add( ubi.getUserInfo().getMailAddrMobile() );
            }
            frm.setLoginMailAddr( mailAddr );
            frm.setLoginMailAddrList( mailAddrList );

            // ホテル基本情報取得
            frm = rsvCmm.getHotelData( frm );

            // 予約基本データから駐車場情報取得
            frm = rsvCmm.getParking( frm );

            // プランマスタより各種情報を取得
            frm = rsvCmm.getPlanData( frm );

            // 別料金マスタよりチェックイン開始、終了時刻を取得
            // frm = rsvCmm.getCiCoTime( frm, frm.getSelNumAdult(), numchild, frm.getSelRsvDate() );
            frm = rsvCmm.getCiCoTime( frm );

            // 都道府県情報取得
            frm = getPref( frm );

            // 設備情報
            frm = getEquip( frm );

            // システム日付・時刻の取得
            currentDate = Integer.valueOf( DateEdit.getDate( 2 ) );
            frm.setCurrentDate( currentDate );

            frm.setLastMonth( DateEdit.addMonth( frm.getOrgReserveDate(), -1 ) );
            frm.setNextMonth( DateEdit.addMonth( frm.getOrgReserveDate(), 1 ) );
            frm.setSelCalYm( frm.getOrgReserveDate() );

            // カレンダー情報取得
            setCalenderInfo( frm, paramHotelId, frm.getSelPlanID(), frm.getSeq(), frm.getOrgReserveDate(), frm.getSelNumAdult(), numchild );

            // 必須オプション情報取得
            frmOptSubImpList = rsvCmm.getOptionSubImp( frm.getSelHotelID(), frm.getSelPlanID() );
            frm.setFrmOptSubImpList( frmOptSubImpList );

            // 選択済み必須オプション取得
            selOptImpSubIdList = logic.getRsvSelOptImpSubIdList( frm.getSelHotelID(), frm.getReserveNo() );
            frm.setSelOptionImpSubIdList( selOptImpSubIdList );

            // 通常オプション情報取得
            frmOptSub = rsvCmm.getOptionSub( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelRsvDate() );
            for( int i = 0 ; i < frmOptSub.getUnitPriceList().size() ; i++ )
            {
                optSubRemarksList.add( "" );
            }
            frm.setSelOptSubRemarksList( optSubRemarksList );

            // 選択済み通常オプション取得
            frm = logic.getRsvSelOptSubIdList( frm.getSelHotelID(), frm.getReserveNo(), frm );

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

                for( int j = 0 ; j < frm.getSelOptSubIdList().size() ; j++ )
                {
                    if ( optId == frm.getSelOptSubIdList().get( j ) )
                    {
                        // 該当オプションが存在する
                        setFlg = true;
                        newOptIdList.add( optId );
                        newOptNmList.add( optNm );
                        newUnitPriceList.add( frm.getSelOptSubUnitPriceList().get( j ) );
                        newRemarksList.add( frm.getSelOptSubRemarksList().get( j ) );
                        newOptNumList.add( frm.getSelOptSubNumList().get( j ) );
                        if ( quantity < frm.getSelOptSubNumList().get( j ) )
                        {
                            // 在庫数より登録数の方が多い場合
                            newQuantityList.add( frm.getSelOptSubNumList().get( j ) );
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
            frm.setSelOptSubNumList( newOptNumList );
            frm.setSelOptSubRemarksList( newRemarksList );
            frm.setFrmOptSub( frmOptSub );

            // 処理モード
            frm.setMode( ReserveCommon.MODE_UPD );

            request.setAttribute( "ViewMode", 1 );
            request.setAttribute( "dsp", frm );
            strCarrierUrl = "/reserve/reserve_personal_info.jsp";
            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + strCarrierUrl );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReservePersonalInfoUpdPC.execute() ][hotelId = "
                    + paramHotelId + " ,planId = " + planId + " ,reserveDate = " + reserveDate + "] Exception", exception );
            try
            {
                if ( paramUserKbn.equals( ReserveCommon.USER_KBN_USER ) )
                {
                    errMsg = Message.getMessage( "erro.30005" );
                    request.setAttribute( "errMsg", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                }
                else
                {
                    errMsg = Message.getMessage( "erro.30005" );
                    request.setAttribute( "errMsg", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                }
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReservePersonalInfoUpdPC.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
    }

    /**
     * 
     * カレンダー情報
     * 
     * @param frm
     * @param selHotelId
     * @param planId
     * @param seq
     * @param adult
     * @param child
     * @throws Exception
     */
    private void setCalenderInfo(FormReservePersonalInfoPC frm, int selHotelId, int planId, int seq, int ymd, int adult, int child) throws Exception
    {
        LogicOwnerRsvManageCalendar logicCalendar;
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        String year = "";
        String month = "";
        String rsvDate = "";
        int checkDateST = 0;
        int checkDateED = 0;
        int dispCheckDateST = 0;
        int dispCheckDateED = 0;
        int checkDatePremiumST = 0;
        int checkDatePremiumED = 0;
        int checkDateFreeST = 0;
        int checkDateFreeED = 0;
        int checkDateFreeAddED = 0;
        int curDate = 0;
        int curTime = 0;
        int minusDay = 0;
        int plusDay = 0;
        int numchild = 0;
        ReserveCommon rsvCmm = new ReserveCommon();

        logicCalendar = new LogicOwnerRsvManageCalendar();

        try
        {
            // 4号営業ホテルは子供人数は0で検索
            if ( rsvCmm.checkLoveHotelFlag( selHotelId ) == false )
            {
                numchild = child;
            }

            // カレンダー情報取得
            // 当日取得
            Calendar calendar = Calendar.getInstance();
            year = Integer.toString( calendar.get( Calendar.YEAR ) );
            month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );
            rsvDate = String.valueOf( ymd );
            year = rsvDate.substring( 0, 4 );
            month = rsvDate.substring( 4, 6 );
            if ( seq != 0 )
            {
                monthlyList = logicCalendar.getCalendarData( selHotelId, planId, seq, Integer.parseInt( year + month ) );
            }
            else
            {
                monthlyList = logicCalendar.getCalendarData( selHotelId, planId, Integer.parseInt( year + month ) );
            }

            curDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            curTime = Integer.parseInt( DateEdit.getTime( 1 ) );

            if ( curTime > frm.getRsvEndTime() )
            {
                plusDay++;
            }
            if ( curTime < frm.getRsvStartTime() )
            {
                minusDay++;
            }

            checkDateST = DateEdit.addDay( curDate, frm.getRsvEndDate() + plusDay );
            checkDateED = DateEdit.addDay( curDate, frm.getRsvStartDate() - minusDay );

            // 販売開始期間が超えている場合は開始日を変える
            if ( checkDateST < frm.getSalesStartDay() )
            {
                dispCheckDateST = frm.getSalesStartDay();
            }
            else
            {
                dispCheckDateST = checkDateST;
            }
            // 販売終了期間を超えている場合は終了日をセット
            if ( checkDateED > frm.getSalesEndDay() )
            {
                dispCheckDateED = frm.getSalesEndDay();
            }
            else
            {
                dispCheckDateED = checkDateED;
            }

            frm.setRsvStartDayStr( String.format( "%1$04d年%2$02d月%3$02d日", dispCheckDateST / 10000, dispCheckDateST % 10000 / 100, dispCheckDateST % 10000 % 100 ) );
            frm.setRsvEndDayStr( String.format( "%1$04d年%2$02d月%3$02d日", dispCheckDateED / 10000, dispCheckDateED % 10000 / 100, dispCheckDateED % 10000 % 100 ) );

            checkDateFreeST = DateEdit.addDay( curDate, frm.getRsvEndDateFree() + plusDay );
            checkDateFreeED = DateEdit.addDay( curDate, frm.getRsvStartDateFree() - minusDay );
            checkDateFreeAddED = DateEdit.addDay( curDate, frm.getRsvStartDateFree() - minusDay + 1 );

            checkDatePremiumST = DateEdit.addDay( curDate, frm.getRsvEndDatePremium() + plusDay );
            checkDatePremiumED = DateEdit.addDay( curDate, frm.getRsvStartDatePremium() - minusDay );

            frm.setRsvPremiumEndDayStr( String.format( "%1$04d年%2$02d月%3$02d日", checkDatePremiumED / 10000, checkDatePremiumED % 10000 / 100, checkDatePremiumED % 10000 % 100 ) );

            if ( checkDateFreeED == checkDatePremiumED )
            {
                // 無料会員と有料会員の終了日が同じ場合は、開始表示をなしにする
                frm.setRsvPremiumStartDayStr( "" );
            }
            else
            {
                frm.setRsvPremiumStartDayStr( String.format( "%1$04d年%2$02d月%3$02d日", checkDateFreeAddED / 10000, checkDateFreeAddED % 10000 / 100, checkDateFreeAddED % 10000 % 100 ) );
            }

            for( int i = 0 ; i <= monthlyList.size() - 1 ; i++ )
            {
                ArrayList<FormOwnerRsvManageCalendar> oneList = monthlyList.get( i );
                for( int j = 0 ; j <= oneList.size() - 1 ; j++ )
                {
                    FormOwnerRsvManageCalendar frmMC = oneList.get( j );
                    String reserveCharge = rsvCmm.getReserveCharge( selHotelId, planId, adult, numchild, frmMC.getCalDate() );
                    frmMC.setReserveChargeFormat( reserveCharge );
                    if ( frmMC.getVacancyRoomNum() != 0 )
                    {
                        if ( (frmMC.getCalDate() >= checkDateST) && (frmMC.getCalDate() <= checkDateED) &&
                                (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay()) )
                        {
                            // frmMC.setRsvJotaiMark( ReserveCommon.RSV_ON_MARK );
                            // 無料会員が閲覧できる条件か確認して見れない場合はプレミアム有効日フラグをたてる
                            if ( frmMC.getCalDate() < checkDateFreeST || frmMC.getCalDate() > checkDateFreeED )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                        }
                        else
                        {
                            frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            // 有料会員が閲覧できる条件か確認して閲覧できる場合はプレミアム有効日フラグをたてる
                            if ( ((frmMC.getCalDate() >= checkDatePremiumST) && (frmMC.getCalDate() <= checkDatePremiumED) &&
                                    (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay())) )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                        }
                    }
                    else
                    {
                        if ( frm.getMode().equals( ReserveCommon.MODE_UPD ) && (frmMC.getCalDate() == frm.getOrgReserveDate()) )
                        {
                            // 変更の場合、元の予約日は「○」にする。
                            frmMC.setRsvJotaiMark( ReserveCommon.RSV_ON_MARK );
                        }
                        else
                        {
                            frmMC.setRsvJotaiMark( ReserveCommon.RSV_OFF_MARK );
                        }
                        if ( (frmMC.getCalDate() >= checkDateST) && (frmMC.getCalDate() <= checkDateED) &&
                                (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay()) )
                        {
                            // 無料会員が閲覧できる条件か確認して見れない場合はプレミアム有効日フラグをたてる
                            if ( frmMC.getCalDate() < checkDateFreeST || frmMC.getCalDate() > checkDateFreeED )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                            if ( frmMC.getRsvJotaiFlg() == 1 && frmMC.getSalesFlag() == 0 )
                            {
                                frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            }
                        }
                        else
                        {
                            // 有料会員が閲覧できる条件か確認して閲覧できる場合はプレミアム有効日フラグをたてる
                            if ( ((frmMC.getCalDate() >= checkDatePremiumST) && (frmMC.getCalDate() <= checkDatePremiumED) &&
                                    (frmMC.getCalDate() >= frm.getSalesStartDay()) && (frmMC.getCalDate() <= frm.getSalesEndDay())) )
                            {
                                frmMC.setRsvPremiumOnlyFlg( true );
                                if ( frmMC.getRsvJotaiMark().equals( ReserveCommon.RSV_ON_MARK ) == true )
                                {
                                    frmMC.setRsvJotaiMark( ReserveCommon.RSV_PREMIUM_MARK );
                                }
                            }
                            else
                            {
                                // 期間外表示
                                frmMC.setRsvJotaiMark( ReserveCommon.RSV_IMPOSSIBLE_MARK );
                            }
                        }
                    }

                }
            }

            // フォームにセット
            frm.setMonthlyList( monthlyList );
            // 有料会員限定の開始日、終了日を追加
            frm.setRsvPremiumStartDay( checkDatePremiumST );
            frm.setRsvPremiumEndDay( checkDatePremiumED );
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReserve_TEST_PC.setRsvManage() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setRsvManage] " + exception );
        }
    }

    /**
     * 
     * 都道府県情報取得
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC getPref(FormReservePersonalInfoPC frm) throws Exception
    {
        ArrayList<Integer> prefIdList = new ArrayList<Integer>();
        ArrayList<String> prefNMList = new ArrayList<String>();
        ArrayList<Integer> jisCdList = new ArrayList<Integer>();
        ArrayList<String> jisNMList = new ArrayList<String>();
        ReserveCommon rsvCmm = new ReserveCommon();

        // 都道府県リスト取得
        prefIdList = rsvCmm.getPrefIdList();
        prefNMList = rsvCmm.getPrefNmList();

        // 市区町村リスト取得
        jisCdList = rsvCmm.getJisCdList( frm.getSelPrefId() );
        jisNMList = rsvCmm.getJisNmList( frm.getSelPrefId() );

        // フォームにセット
        frm.setPrefIdList( prefIdList );
        frm.setPrefNmList( prefNMList );
        frm.setJisCdList( jisCdList );
        frm.setJisNmList( jisNMList );

        return(frm);
    }

    /**
     * 
     * 設備情報取得
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    private FormReservePersonalInfoPC getEquip(FormReservePersonalInfoPC frm) throws Exception
    {

        LogicReserveInitPC logic = new LogicReserveInitPC();
        DataRsvRoom drr = new DataRsvRoom();
        String roomRemarks = "";
        String roomPr = "";

        logic.setFrm( frm );

        if ( frm.getOfferKind() == 2 )
        {
            // 部屋提供
            drr.getData( frm.getSelHotelID(), frm.getSelSeq() );
            roomRemarks = drr.getRemarks();
            roomPr = drr.getRoomPr();
            logic.getEquip( frm.getSelHotelID(), frm.getSelSeq() );
            frm = logic.getFrm();
            frm.setRoomRemarks( roomRemarks );
            frm.setRoomPr( roomPr );

            return(frm);
        }

        // プラン提供
        roomRemarks = "";
        roomPr = "";
        logic.getEquipPlan( frm.getSelHotelID(), ReserveCommon.getRoomSeqList( frm.getSelHotelID(), frm.getSelPlanID() ) );

        frm.setRoomRemarks( roomRemarks );
        frm.setRoomPr( roomPr );

        return(frm);
    }

}
