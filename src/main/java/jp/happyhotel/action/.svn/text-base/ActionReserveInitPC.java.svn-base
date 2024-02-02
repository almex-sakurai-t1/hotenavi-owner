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
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataRsvReserveBasic;
import jp.happyhotel.data.DataRsvRoom;
import jp.happyhotel.owner.FormOwnerRsvManageCalendar;
import jp.happyhotel.owner.LogicOwnerRsvManageCalendar;
import jp.happyhotel.reserve.FormReserveOptionSub;
import jp.happyhotel.reserve.FormReserveOptionSubImp;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveInitPC;
import jp.happyhotel.user.UserBasicInfo;

/**
 * 
 * 予約入力画面(PC版) Action Class
 */

public class ActionReserveInitPC extends BaseAction
{

    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int hotelId = 0;
        int planId = 0;
        int seq = 0;
        int reserveDate = 0;
        int ymd = 0;
        int child = 0;
        int adult = 0;
        String reserveDateFormat = "";
        String rsvDate;
        String week;
        String strCarrierUrl = "";
        int currentDate = 0;
        String userId = "";
        String mailAddr = "";
        ArrayList<String> mailAddrList = new ArrayList<String>();
        String errMsg = "";
        boolean isLoginUser = false;
        DataRsvReserveBasic drrb = new DataRsvReserveBasic();

        ReserveCommon rsvCmm = new ReserveCommon();
        FormReservePersonalInfoPC frm = new FormReservePersonalInfoPC();
        ArrayList<FormReserveOptionSubImp> frmOptSubImpList = new ArrayList<FormReserveOptionSubImp>();
        FormReserveOptionSub frmOptSub = new FormReserveOptionSub();
        ArrayList<String> optSubRemarksList = new ArrayList<String>();

        String checkHotelId = "";
        String checkPlanId = "";
        String checkSeq = "";
        String checkRsvDate = "";

        try
        {
            // URLパラメータを不正に変更された場合は、エラーページへ遷移させる
            checkHotelId = request.getParameter( "id" );
            checkPlanId = request.getParameter( "plan_id" );
            checkSeq = request.getParameter( "seq" );
            checkRsvDate = request.getParameter( "rsv_date" );

            // パラメータが消された場合のチェック
            if ( (checkHotelId == null) || (checkPlanId == null) || (checkSeq == null) || (checkRsvDate == null) )
            {
                errMsg = Message.getMessage( "erro.30009", "パラメータなし" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // 数値のパラメータに、数値以外が指定された場合のチェック
            try
            {
                // パラメータ取得
                hotelId = Integer.parseInt( request.getParameter( "id" ) ); // ホテルID
                planId = Integer.parseInt( request.getParameter( "plan_id" ) ); // プランID
                seq = Integer.parseInt( request.getParameter( "seq" ) ); // 部屋番号
                reserveDate = Integer.parseInt( request.getParameter( "rsv_date" ) ); // 予約日
                adult = Integer.parseInt( request.getParameter( "adult" ) );
                if ( rsvCmm.checkLoveHotelFlag( hotelId ) == false )
                {
                    child = Integer.parseInt( request.getParameter( "child" ) );
                }
            }
            catch ( Exception exception )
            {
                errMsg = Message.getMessage( "erro.30009", "パラメータ値不正" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // ログインユーザー情報取得
            DataLoginInfo_M2 dataLoginInfo_M2;
            dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

            if ( dataLoginInfo_M2 == null )
            {
                // ログアウト状態の場合はエラー
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_nomember.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( dataLoginInfo_M2.isMemberFlag() == true )
            {
                // 存在する
                isLoginUser = true;
                userId = dataLoginInfo_M2.getUserId();
            }

            if ( isLoginUser == false )
            {
                // ログインユーザーが存在しない場合はエラー
                errMsg = Message.getMessage( "erro.30004" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            UserBasicInfo userinfoUbi = new UserBasicInfo();
            userinfoUbi.getUserBasic( userId );
            // メールアドレスをセット
            if ( userinfoUbi.getUserInfo().getMailAddr().equals( "" ) == false )
            {
                mailAddr = userinfoUbi.getUserInfo().getMailAddr();
                mailAddrList.add( userinfoUbi.getUserInfo().getMailAddr() );
            }
            if ( userinfoUbi.getUserInfo().getMailAddrMobile().equals( "" ) == false )
            {
                if ( mailAddr.equals( "" ) != false )
                {
                    mailAddr = userinfoUbi.getUserInfo().getMailAddrMobile();
                }
                mailAddrList.add( userinfoUbi.getUserInfo().getMailAddrMobile() );
            }

            // 仮オープンかどうかを取得する
            drrb.getData( hotelId );

            // パラメータの整合性チェック
            errMsg = rsvCmm.checkParam( hotelId, planId, seq, reserveDate, ReserveCommon.USER_KBN_USER, ReserveCommon.MODE_INS );
            if ( errMsg.trim().length() != 0 )
            {
                // 仮オープンだったら何もしない
                if ( drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 0 )
                {
                    // チェックNGの場合、エラーページへ遷移する。
                    request.setAttribute( "errMsg", errMsg );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }
            }

            // 予約人数の範囲チェック
            errMsg = rsvCmm.checkAdultChildNum( hotelId, planId, adult, child );
            if ( errMsg.trim().length() != 0 )
            {
                // チェックNGの場合、エラーページへ遷移する。
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            // データチェック
            FormReserveSheetPC frmSheetPC = new FormReserveSheetPC();
            frmSheetPC.setSelHotelId( hotelId );
            frmSheetPC.setSelPlanId( planId );
            frmSheetPC.setRsvNo( "" );
            frmSheetPC.setSeq( seq );
            frmSheetPC.setMode( ReserveCommon.MODE_INS );
            frmSheetPC.setRsvDate( reserveDate );
            frmSheetPC.setUserId( userId );
            frmSheetPC = rsvCmm.chkDspMaster( frmSheetPC );
            if ( frmSheetPC.getErrMsg().trim().length() != 0 )
            {
                // チェックNGの場合、エラーページへ遷移する。
                // request.setAttribute( "errMsg", frmSheetPC.getErrMsg() );
                // requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                // requestDispatcher.forward( request, response );
                // return;
            }

            // フォームにセット
            frm.setSelHotelID( hotelId );
            frm.setSelPlanID( planId );
            frm.setSelSeq( seq );
            frm.setSelRsvDate( reserveDate );
            frm.setOrgReserveDate( reserveDate );
            frm.setLoginUserId( userId );
            frm.setLoginMailAddr( mailAddr );
            frm.setLoginMailAddrList( mailAddrList );
            frm.setLoginTel( userinfoUbi.getUserInfo().getTel1() );
            frm.setPaymemberFlg( dataLoginInfo_M2.isPaymemberFlag() );

            // 予約可能かチェック
            if ( reserveDate > 0 )
            {
                // 無料会員＆有料会員の区別をするためにプランデータ取得
                ReserveCommon rsvcomm = new ReserveCommon();
                frm = rsvcomm.getPlanData( frm );

                int curDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                int curTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                int checkDateST = DateEdit.addDay( curDate, (frm.getRsvEndDate()) );
                int checkDateED = DateEdit.addDay( curDate, (frm.getRsvStartDate()) );
                int checkDatePremiumED = DateEdit.addDay( curDate, (frm.getRsvStartDatePremium()) );
                int checkDatePremiumST = DateEdit.addDay( curDate, (frm.getRsvEndDatePremium()) );

                if ( (reserveDate > checkDateST && reserveDate < checkDateED &&
                        reserveDate >= frm.getSalesStartDay() && reserveDate <= frm.getSalesEndDay()) ||
                        (reserveDate == checkDateST && curTime <= frm.getRsvEndTime() &&
                                reserveDate >= frm.getSalesStartDay() && reserveDate <= frm.getSalesEndDay()) ||
                        (reserveDate == checkDateED && curTime >= frm.getRsvStartTime() &&
                                reserveDate >= frm.getSalesStartDay() && reserveDate <= frm.getSalesEndDay()) )
                {
                }
                else
                {
                    if ( ((reserveDate > checkDatePremiumST) && (reserveDate < checkDatePremiumED) &&
                            (reserveDate >= frm.getSalesStartDay()) && (reserveDate <= frm.getSalesEndDay())) ||
                            (reserveDate == checkDatePremiumST && curTime <= frm.getRsvEndTime()) ||
                            (reserveDate == checkDatePremiumED && curTime >= frm.getRsvStartTime()) )
                    {
                        // プレミアム会員が有効の場合は表示を変更
                        String err1 = "本プランはハピホテプレミアム会員様のみ予約可能です。";
                        errMsg = err1;
                    }
                    else
                    {
                        // 仮オープンだったら何もしない
                        if ( drrb.getSalesFlag() == 1 || drrb.getPreOpenFlag() == 0 )
                        {
                            errMsg = "予約受付の期間外です。お手数ですが、もう一度最初からやり直してください。";
                        }
                    }
                    // エラー情報が入っている場合のみ
                    if ( errMsg.equals( "" ) == false )
                    {
                        // チェックNGの場合、エラーページへ遷移する。
                        request.setAttribute( "errMsg", errMsg );
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                        requestDispatcher.forward( request, response );
                        return;
                    }
                }
            }

            // 過去に予約データが存在すれば住所などはセットする
            if ( frm.getZipCd3().equals( "" ) && frm.getZipCd4().equals( "" ) &&
                    frm.getSelPrefId() == 0 && frm.getSelJisCd() == 0 &&
                    frm.getAddress3().equals( "" ) && frm.getLastName().equals( "" ) &&
                    frm.getFirstName().equals( "" ) && frm.getLastNameKana().equals( "" ) &&
                    frm.getFirstNameKana().equals( "" ) && frm.getTel().equals( "" ) )
            {
                DataRsvReserve rsv = new DataRsvReserve();
                rsv.getDataByUserId( userId );
                if ( !rsv.getReserveNo().equals( "" ) )
                {
                    frm.setZipCd3( rsv.getZipCd().substring( 0, 3 ) );
                    frm.setZipCd4( rsv.getZipCd().substring( 3, rsv.getZipCd().length() ) );
                    frm.setSelPrefId( rsv.getPrefCode() );
                    frm.setSelJisCd( rsv.getJisCode() );
                    frm.setAddress3( rsv.getAddress3() );
                    frm.setLastName( rsv.getNameLast() );
                    frm.setFirstName( rsv.getNameFirst() );
                    frm.setLastNameKana( rsv.getNameLastKana() );
                    frm.setFirstNameKana( rsv.getNameFirstKana() );
                    frm.setTel( rsv.getTel1() );
                }
            }

            // 日付の表示形式
            ymd = Integer.parseInt( request.getParameter( "rsv_date" ) );
            if ( ymd != 0 )
            {
                week = DateEdit.getWeekName( ymd );
                rsvDate = String.valueOf( ymd );
                reserveDateFormat = rsvDate.substring( 0, 4 ) + "年" + rsvDate.substring( 4, 6 ) + "月" + rsvDate.substring( 6, 8 ) + "日（" + week + "）";
                frm.setReserveDateFormat( reserveDateFormat );
                frm.setReserveDateDay( ymd );
            }
            else
            {
                frm.setReserveDateFormat( "日付未選択（下記のカレンダーより日付を選択して下さい）" );
            }

            // ホテル基本情報取得
            frm = rsvCmm.getHotelData( frm );

            // ホテルの駐車場利用区分、駐車場利用台数を取得
            frm = rsvCmm.getParking( frm );
            frm.setParkingUsedKbnInit( 0 );

            // プランマスタより各種情報を取得
            frm = rsvCmm.getPlanData( frm );
            if ( frm.getNumAdultList().size() > 0 )
            {
                frm.setSelNumAdult( 2 );
            }
            else
            {
                frm.setSelNumAdult( 1 );
            }
            frm.setSelNumChild( 0 );

            // 別料金マスタよりチェックイン開始、終了時刻を取得
            frm = rsvCmm.getCiCoTime( frm, adult, child, reserveDate );
            frm.setSelEstTimeArrival( -1 );

            // 画面情報設定;
            frm.setMode( ReserveCommon.MODE_INS );

            // 都道府県情報取得
            frm = getPref( frm );

            // 設備情報
            frm = getEquip( frm );

            // システム日付・時刻の取得
            currentDate = Integer.valueOf( DateEdit.getDate( 2 ) );
            frm.setCurrentDate( currentDate );

            // 駐車場情報取得
            DataRsvReserve rsvData = new DataRsvReserve();
            if ( !frm.getReserveNo().equals( "" ) )
            {
                rsvData.getData( frm.getSelHotelID(), frm.getReserveNo() );
                frm.setSelParkingCount( rsvData.getParkingCount() );
                frm.setSelHiRoofCount( rsvData.getParkingHiRoofCount() );
            }

            // 必須オプション情報取得
            frmOptSubImpList = rsvCmm.getOptionSubImp( frm.getSelHotelID(), frm.getSelPlanID() );
            frm.setFrmOptSubImpList( frmOptSubImpList );
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
                frm.setSelOptionImpSubIdList( selOptSubImpIdList );
            }

            // 通常オプション情報取得
            frmOptSub = rsvCmm.getOptionSub( frm.getSelHotelID(), frm.getSelPlanID(), ymd );
            frm.setFrmOptSub( frmOptSub );
            for( int i = 0 ; i < frmOptSub.getUnitPriceList().size() ; i++ )
            {
                optSubRemarksList.add( "" );
            }
            frm.setSelOptSubRemarksList( optSubRemarksList );

            // カレンダー情報取得
            setCalenderInfo( frm, hotelId, planId, seq, ymd, adult, child );
            frm.setLastMonth( DateEdit.addMonth( ymd, -1 ) );
            frm.setNextMonth( DateEdit.addMonth( ymd, 1 ) );
            frm.setSelCalYm( ymd );

            frm.setLoginUserKbn( ReserveCommon.USER_KBN_USER );

            // 予約日のチェック
            errMsg = rsvCmm.checkReserveDuplicate( userId, Integer.parseInt( checkRsvDate ) );
            frm.setErrMsg( errMsg );

            request.setAttribute( "dsp", frm );
            request.setAttribute( "err", "" );

            strCarrierUrl = "/reserve/reserve_personal_info.jsp";
            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + strCarrierUrl );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionReserveInitPC.execute() ][hotelId = "
                    + hotelId + " ,planId = " + planId + " ,reserveDate = " + reserveDate + "] Exception", exception );
            try
            {
                errMsg = Message.getMessage( "erro.30005" );
                request.setAttribute( "errMsg", errMsg );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/reserve/reserve_error_PC.jsp" );
                requestDispatcher.forward( request, response );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionReserveInitPC.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
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
        ReserveCommon rsvCmm = new ReserveCommon();

        // 都道府県リスト取得
        prefIdList = rsvCmm.getPrefIdList();
        prefNMList = rsvCmm.getPrefNmList();

        // フォームにセット
        frm.setPrefIdList( prefIdList );
        frm.setPrefNmList( prefNMList );

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

        if ( frm.getOfferKind() == ReserveCommon.OFFER_KIND_ROOM )
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

    /**
     * 
     * カレンダー情報
     * 
     * @param frm
     * @param selHotelId ホテルID
     * @param planId プラン番号
     * @param seq 部屋番号
     * @param adult 大人人数
     * @param child 子供人数
     * @throws Exception
     */
    private void setCalenderInfo(FormReservePersonalInfoPC frm, int selHotelId, int planId, int seq, int rsvDate, int adult, int child) throws Exception
    {
        LogicOwnerRsvManageCalendar logicCalendar;
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        String year = "";
        String month = "";
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
        ReserveCommon rsvCmm = new ReserveCommon();

        logicCalendar = new LogicOwnerRsvManageCalendar();

        try
        {

            // カレンダー情報取得
            // 当日取得
            Calendar calendar = Calendar.getInstance();
            year = Integer.toString( calendar.get( Calendar.YEAR ) );
            month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );

            if ( rsvDate != 0 )
            {
                year = String.valueOf( rsvDate ).substring( 0, 4 );
                month = String.valueOf( rsvDate ).substring( 4, 6 );
            }
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

            frm.setRsvStartDayInt( dispCheckDateST );
            frm.setRsvEndDayInt( dispCheckDateED );

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
                    String reserveCharge = rsvCmm.getReserveCharge( selHotelId, planId, adult, child, frmMC.getCalDate() );
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
            Logging.error( "[ActionReserveInitPC.setRsvManage() ] Exception", exception );
            throw new Exception( "[ActionOwnerRsvMenu.setRsvManage] " + exception );
        }
    }

}
