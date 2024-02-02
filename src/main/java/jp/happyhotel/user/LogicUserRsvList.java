package jp.happyhotel.user;

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
import jp.happyhotel.data.DataRsvReserve;

/**
 * 
 * 予約一覧画面 business Logic
 */
public class LogicUserRsvList implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID   = -105823339524269343L;
    private static final String sts1               = "来店待ち";
    private static final String sts2               = "来店";
    private static final String sts3               = "キャンセル";
    private static final String sts4               = "<br>(仮来店)";
    private int                 listmax            = 20;                      // 一画面最大明細表示件数
    private String              errMsg             = "";
    private String              userId             = "";
    private int                 maxCnt             = 0;
    private int                 dateFROM           = 0;
    private int                 dateTO             = 0;
    private String              rsvNo              = "";
    private ArrayList<Integer>  idList             = new ArrayList<Integer>(); // ホテルID
    private ArrayList<String>   nmList             = new ArrayList<String>(); // ホテル名
    private ArrayList<String>   addressList        = new ArrayList<String>(); // 住所
    private ArrayList<String>   optionList         = new ArrayList<String>(); // オプション
    private ArrayList<String>   planNmList         = new ArrayList<String>(); // プラン名
    private ArrayList<String>   reserveNoList      = new ArrayList<String>(); // 予約番号
    private ArrayList<String>   rsvDateList        = new ArrayList<String>(); // 予約日
    private ArrayList<Integer>  rsvDateValList     = new ArrayList<Integer>(); // 予約日
    private ArrayList<Integer>  seqList            = new ArrayList<Integer>(); // 部屋番号
    private ArrayList<String>   userIdList         = new ArrayList<String>(); // ユーザID
    private ArrayList<String>   userNmList         = new ArrayList<String>(); // 利用者名
    private ArrayList<String>   tel1List           = new ArrayList<String>(); // 連絡先
    private ArrayList<String>   estTimeArrivalList = new ArrayList<String>(); // 到着予定時刻
    private ArrayList<String>   statusList         = new ArrayList<String>(); // ステータス
    private ArrayList<Integer>  statusValList      = new ArrayList<Integer>(); // ステータス
    private ArrayList<Integer>  dspList            = new ArrayList<Integer>(); // 提供区分
    private ArrayList<Integer>  noshowFlagList     = new ArrayList<Integer>(); // ノーショー区分
    private ArrayList<Integer>  paymentList        = new ArrayList<Integer>(); // 決済方法
    private ArrayList<Integer>  paymentStatusList  = new ArrayList<Integer>(); // 決済ステータス

    /**
     * 画面入力項目チェック
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

        // 戻り値の初期化
        isResult = true;

        try
        {

            // 開始日
            if ( (request.getParameter( "date_f" ) == null) || (request.getParameter( "date_f" ).toString().length() == 0) )
            {
                errMsg = "";
            }
            else
            {
                dateF = request.getParameter( "date_f" );
                if ( DateEdit.checkDate( dateF ) )
                {
                    // 日付として正しい場合
                }
                else
                {
                    errMsg = errMsg + Message.getMessage( "warn.00009", "開始日" ) + "<br />";
                    isResult = false;
                }
            }
            // 終了日
            if ( (request.getParameter( "date_t" ) == null) || (request.getParameter( "date_t" ).toString().length() == 0) )
            {
                errMsg = errMsg + "";
            }
            else
            {
                dateT = request.getParameter( "date_t" );
                if ( DateEdit.checkDate( dateT ) )
                {
                    // 日付として正しい場合
                }
                else
                {
                    errMsg = errMsg + Message.getMessage( "warn.00009", "終了日" ) + "<br />";
                    isResult = false;
                }
            }

            if ( isResult )
            {
                // 範囲チェック
                if ( (request.getParameter( "date_f" ) != null)
                        && (request.getParameter( "date_f" ).toString().length() != 0) &&
                        (request.getParameter( "date_t" ) != null)
                        && (request.getParameter( "date_t" ).toString().length() != 0) )
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

            // 予約番号
            if ( (request.getParameter( "rsv_no" ) == null) || (request.getParameter( "rsv_no" ).toString().length() == 0) )
            {
            }
            else
            {
                rsvNo = request.getParameter( "rsv_no" );
                if ( CheckString.numAlphaCheck( rsvNo ) == false )
                {
                    // 半角英数字でない場合はエラー
                    errMsg = errMsg + Message.getMessage( "warn.30007", "予約番号", "半角英数字" ) + "<br />";
                    isResult = false;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveListPC.chkDsp] Exception=" + e.toString() );
            throw e;
        }

        // 戻り値
        return isResult;
    }

    /**
     * 予約情報取得
     * 
     * @param frm // 編集先ﾌｫｰﾑid
     * @param objKbn // 抽出対象
     * @param pageCnt // ページ番号(0から)
     * @param dispCnt // 表示件数
     * @param mode // ページング機能(ALL:ページング無し全件, PARTS:ページング有)
     * @param viewMode 表示モード
     * @return
     * @throws Exception
     */
    public boolean getData(FormUserRsvList frm, int objKbn, int pageCnt, int dispCnt, String mode, int viewMode)
            throws Exception
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

        // 戻り値の初期化
        isResult = false;

        rsvDateF = frm.getDateFrom();
        rsvDateT = frm.getDateTo();
        this.listmax = dispCnt;

        try
        {
            query = "SELECT hh_rsv_reserve.id, hh_rsv_reserve.reserve_no AS reserveNo, hh_rsv_reserve.reserve_date AS reserveDate, "
                    + " hh_rsv_reserve.name_last, hh_rsv_reserve.name_first, hh_rsv_reserve.option_charge_total, "
                    + " hh_rsv_reserve.seq, hh_rsv_plan.plan_name, hh_rsv_reserve.est_time_arrival AS estTimeArrival, "
                    + " hh_rsv_reserve.user_id, hh_rsv_reserve.status, hh_rsv_plan.room_select_kind as offer_kind, "
                    + " hh_rsv_reserve.temp_coming_flag, hh_rsv_reserve.tel1, hh_hotel_basic.name, hh_hotel_basic.pref_name, hh_hotel_basic.address1,"
                    + " hh_rsv_reserve.noshow_flag, hh_rsv_reserve.payment, hh_rsv_reserve.payment_status"
                    + " FROM newRsvDB.hh_rsv_reserve INNER JOIN newRsvDB.hh_rsv_plan "
                    + " ON ( hh_rsv_reserve.id = hh_rsv_plan.id AND hh_rsv_reserve.plan_id = hh_rsv_plan.plan_id AND hh_rsv_reserve.plan_sub_id = hh_rsv_plan.plan_sub_id) "
                    + " INNER JOIN hh_hotel_basic ON ( hh_rsv_reserve.id = hh_hotel_basic.id)"
                    + " WHERE hh_rsv_reserve.user_id = ? "
                    + " AND hh_rsv_reserve.no_disp_flag = 0 "
                    + " AND hh_rsv_reserve.plan_sub_id = hh_rsv_plan.plan_sub_id "
                    + " AND hh_rsv_reserve.ext_flag = 0";

            if ( objKbn == 1 )
            {
                // 予約情報のみ
                query += " AND hh_rsv_reserve.status = 1 ";
            }
            else if ( objKbn == 2 )
            {
                // 履歴情報のみ
                query += " AND hh_rsv_reserve.status = 2 ";
            }
            else if ( objKbn == 3 )
            {
                // キャンセル情報のみ
                query += " AND hh_rsv_reserve.status = 3 ";
            }
            else if ( objKbn == 4 )
            {
                // 仮来店情報のみ
                query += " AND hh_rsv_reserve.temp_coming_flag = 1 ";
            }
            else if ( objKbn == 5 )
            {
                // キャンセル以外
                query += " AND hh_rsv_reserve.status <> 3 ";
            }
            else
            {
                // 全ての場合は条件無し

            }
            // 日付開始
            if ( rsvDateF != 0 )
            {
                query += " AND hh_rsv_reserve.reserve_date >= ? ";
            }
            // 日付終了
            if ( rsvDateT != 0 )
            {
                query += " AND hh_rsv_reserve.reserve_date <= ? ";
            }

            if ( !this.rsvNo.equals( "" ) )
            {
                query += " AND hh_rsv_reserve.reserve_no LIKE ? ";
            }

            // 旧予約システム
            query += " UNION SELECT hh_rsv_reserve.id, hh_rsv_reserve.reserve_no AS reserveNo, hh_rsv_reserve.reserve_date AS reserveDate, "
                    + " hh_rsv_reserve.name_last, hh_rsv_reserve.name_first, hh_rsv_reserve.option_charge_total, "
                    + " hh_rsv_reserve.seq, hh_rsv_plan.plan_name, hh_rsv_reserve.est_time_arrival AS estTimeArrival, "
                    + " hh_rsv_reserve.user_id, hh_rsv_reserve.status, 0 as offer_kind, "
                    + " hh_rsv_reserve.temp_coming_flag, hh_rsv_reserve.tel1, hh_hotel_basic.name, hh_hotel_basic.pref_name, hh_hotel_basic.address1,"
                    + " hh_rsv_reserve.noshow_flag, 0 as payment, 0 as payment_status"
                    + " FROM hh_rsv_reserve INNER JOIN hh_rsv_plan "
                    + " ON ( hh_rsv_reserve.id = hh_rsv_plan.id AND hh_rsv_reserve.plan_id = hh_rsv_plan.plan_id ) "
                    + " INNER JOIN hh_hotel_basic ON ( hh_rsv_reserve.id = hh_hotel_basic.id)"
                    + " WHERE hh_rsv_reserve.user_id = ? "
                    + " AND hh_rsv_reserve.no_disp_flag = 0 ";

            if ( objKbn == 1 )
            {
                // 予約情報のみ
                query += " AND hh_rsv_reserve.status = 1 ";
            }
            else if ( objKbn == 2 )
            {
                // 履歴情報のみ
                query += " AND hh_rsv_reserve.status = 2 ";
            }
            else if ( objKbn == 3 )
            {
                // キャンセル情報のみ
                query += " AND hh_rsv_reserve.status = 3 ";
            }
            else if ( objKbn == 4 )
            {
                // 仮来店情報のみ
                query += " AND hh_rsv_reserve.temp_coming_flag = 1 ";
            }
            else if ( objKbn == 5 )
            {
                // キャンセル以外
                query += " AND hh_rsv_reserve.status <> 3 ";
            }
            else
            {
                // 全ての場合は条件無し

            }
            // 日付開始
            if ( rsvDateF != 0 )
            {
                query += " AND hh_rsv_reserve.reserve_date >= ? ";
            }
            // 日付終了
            if ( rsvDateT != 0 )
            {
                query += " AND hh_rsv_reserve.reserve_date <= ? ";
            }

            if ( !this.rsvNo.equals( "" ) )
            {
                query += " AND hh_rsv_reserve.reserve_no LIKE ? ";
            }

            // 出力順は予約日、到着予定時刻順
            if ( viewMode == 2 )
            {
                query += " ORDER BY reserveDate desc,estTimeArrival desc, reserveNo desc";
            }
            else
            {
                query += " ORDER BY reserveDate asc,estTimeArrival asc, reserveNo asc";
            }
            // Logging.info( query + queryWhere + ",userId=" + frm.getUserId() + ",rsvDateF=" + rsvDateF + ",rsvDateT=" + rsvDateT, "[LogicUserRsvList.getData()]" );
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query + queryWhere );
            seq = 1;
            prestate.setString( seq++, frm.getUserId() );
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
            prestate.setString( seq++, frm.getUserId() );
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

                // 全件抽出
                while( result.next() )
                {
                    this.setIdList( result.getInt( "id" ) ); // ホテルID
                    this.setNmList( result.getString( "name" ) );
                    this.setAddressList( result.getString( "pref_name" ) + "・" + result.getString( "address1" ) );
                    convDate = String.valueOf( result.getInt( "reserveDate" ) );
                    weekName = DateEdit.getWeekName( result.getInt( "reserveDate" ) );
                    this.setRsvDateList( convDate.substring( 0, 4 ) + "/" + convDate.substring( 4, 6 ) + "/"
                            + convDate.substring( 6, 8 ) +
                            "(" + weekName + ")" );
                    this.setRsvDateValList( result.getInt( "reserveDate" ) );
                    this.setSeqList( result.getInt( "seq" ) );
                    if ( result.getInt( "option_charge_total" ) == 0 )
                    {
                        this.setOptionList( "無" );
                    }
                    else
                    {
                        this.setOptionList( "有" );
                    }
                    this.setUserNmList( ConvertCharacterSet.convDb2Form( result.getString( "name_last" ).toString() ) +
                            ConvertCharacterSet.convDb2Form( result.getString( "name_first" ).toString() ) );
                    this.setTel1List( ConvertCharacterSet.convDb2Form( result.getString( "tel1" ).toString() ) );
                    this.setPlanNmList( ConvertCharacterSet.convDb2Form( result.getString( "plan_name" ).toString() ) );
                    this.setReserveNoList( result.getString( "reserveNo" ) );

                    estTime = ConvertTime.convTimeStr( result.getInt( "estTimeArrival" ), 3 );
                    this.setEstTimeArrivalList( estTime );

                    this.setUserIdList( result.getString( "user_id" ).toString() );

                    if ( result.getInt( "status" ) == 1 )
                    {
                        this.setStatusList( result.getInt( "temp_coming_flag" ) == 1 ? sts1 + sts4 : sts1 );
                    }
                    else if ( result.getInt( "status" ) == 2 )
                    {
                        this.setStatusList( result.getInt( "temp_coming_flag" ) == 1 ? sts2 + sts4 : sts2 + "済み" );
                    }
                    else if ( result.getInt( "status" ) == 3 )
                    {
                        this.setStatusList( result.getInt( "temp_coming_flag" ) == 1 ? sts3 + sts4 : sts3 + "済み" );
                    }
                    this.setStatusValList( result.getInt( "status" ) );

                    this.setDspList( result.getInt( "offer_kind" ) );

                    this.setNoshowFlagList( result.getInt( "noshow_flag" ) );
                    this.setPaymentList( result.getInt( "payment" ) );
                    this.setPaymentStatusList( result.getInt( "payment_status" ) );

                    // 件数ｶｳﾝﾄ
                    intMaxCnt++;
                }

                if ( mode.equals( "ALL" ) )
                {
                    // 全件画面表示の場合はそのままフォームへ編集する
                    for( i = 0 ; i < intMaxCnt - 1 ; i++ )
                    {
                        frm.setIdList( this.idList.get( i ) );
                        frm.setNmList( this.nmList.get( i ) );
                        frm.setAddressList( this.addressList.get( i ) );
                        frm.setRsvDateList( this.rsvDateList.get( i ) );
                        frm.setRsvDateValList( this.rsvDateValList.get( i ) );
                        frm.setSeqList( this.seqList.get( i ) );
                        frm.setOptionList( this.optionList.get( i ) );
                        frm.setUserNmList( this.userNmList.get( i ) );
                        frm.setTel1List( this.tel1List.get( i ) );
                        frm.setPlanNmList( this.planNmList.get( i ) );
                        frm.setReserveNoList( this.reserveNoList.get( i ) );
                        frm.setEstTimeArrivalList( this.estTimeArrivalList.get( i ) );
                        frm.setUserIdList( this.userIdList.get( i ) );
                        frm.setStatusList( this.statusList.get( i ) );
                        frm.setDspList( this.dspList.get( i ) );
                        frm.setStatusValList( this.statusValList.get( i ) );
                        frm.setNoshowFlagList( this.noshowFlagList.get( i ) );
                        frm.setPaymentList( this.paymentList.get( i ) );
                        frm.setPaymentStatusList( this.paymentStatusList.get( i ) );

                    }

                    isResult = true;
                    frm.setPageMax( intMaxCnt - 1 );
                    frm.setPageSt( 0 );
                    frm.setPageEd( intMaxCnt - 1 );
                }
                else if ( mode.equals( "PARTS" ) )
                {

                    // 抽出した件数のうち一部分のみフォームへ編集する
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

                        // 表示対象ページ分のみ
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
                            frm.setIdList( this.idList.get( i ) );
                            frm.setNmList( this.nmList.get( i ) );
                            frm.setAddressList( this.addressList.get( i ) );
                            frm.setRsvDateList( this.rsvDateList.get( i ) );
                            frm.setRsvDateValList( this.rsvDateValList.get( i ) );
                            frm.setSeqList( this.seqList.get( i ) );
                            frm.setOptionList( this.optionList.get( i ) );
                            frm.setUserNmList( this.userNmList.get( i ) );
                            frm.setTel1List( this.tel1List.get( i ) );
                            frm.setPlanNmList( this.planNmList.get( i ) );
                            frm.setReserveNoList( this.reserveNoList.get( i ) );
                            frm.setEstTimeArrivalList( this.estTimeArrivalList.get( i ) );
                            frm.setUserIdList( this.userIdList.get( i ) );
                            frm.setStatusList( this.statusList.get( i ) );
                            frm.setDspList( this.dspList.get( i ) );
                            frm.setStatusValList( this.statusValList.get( i ) );
                            frm.setNoshowFlagList( this.noshowFlagList.get( i ) );
                            frm.setPaymentList( this.paymentList.get( i ) );
                            frm.setPaymentStatusList( this.paymentStatusList.get( i ) );
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
                // 抽出件数
                frm.setPageMax( intMaxCnt );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicUserRsvList.getData()] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return isResult;
    }

    /***
     * 予約の非表示処理
     * 
     * @param userId ユーザID
     * @param rsvNo 予約番号
     * @return
     */
    public boolean deleteData(int id, String rsvNo) throws Exception
    {
        final int NO_DISP = 1;
        boolean ret = false;
        DataRsvReserve drr;

        try
        {
            // 初期化
            drr = new DataRsvReserve();
            // 予約データ取得
            ret = drr.getData( id, rsvNo );

            // データがあった場合
            if ( ret != false )
            {
                // 非表示フラグをセット
                drr.setNoDispFlag( NO_DISP );
                // データの更新
                ret = drr.updateData( id, rsvNo );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicUserRsvList.deleteData()] Exception=" + e.toString() );
            throw e;
        }

        return(ret);
    }

    /***
     * 予約の非表示処理
     * 
     * @param userId ユーザID
     * @param rsvNo 予約番号
     * @return
     */
    public boolean deleteNewRsvData(int id, String rsvNo) throws Exception
    {
        final int NO_DISP = 1;
        boolean ret = false;
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        try
        {
            query = "UPDATE newRsvDB.hh_rsv_reserve SET no_disp_flag = ? WHERE id= ? AND reserve_no = ?";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, NO_DISP );
            prestate.setInt( 2, id );
            prestate.setString( 3, rsvNo );
            if ( prestate.executeUpdate() > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicUserRsvList.deleteNewRsvData()] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 
     * getter
     * 
     */
    public ArrayList<String> getAddressList()
    {
        return this.addressList;
    }

    public int getDateFrom()
    {
        return this.dateFROM;
    }

    public int getDateTo()
    {
        return this.dateTO;
    }

    public ArrayList<Integer> getDspList()
    {
        return this.dspList;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public ArrayList<Integer> getIdList()
    {
        return idList;
    }

    public ArrayList<String> getNmList()
    {
        return nmList;
    }

    public ArrayList<String> getOptionList()
    {
        return optionList;
    }

    public ArrayList<String> getRsvDateList()
    {
        return rsvDateList;
    }

    public ArrayList<Integer> getRsvDateValList()
    {
        return rsvDateValList;
    }

    public ArrayList<String> getStatusList()
    {
        return this.statusList;
    }

    public ArrayList<Integer> getStatusValList()
    {
        return this.statusValList;
    }

    public ArrayList<Integer> getSeqList()
    {
        return seqList;
    }

    public ArrayList<String> getUserNmList()
    {
        return userNmList;
    }

    public ArrayList<String> getTel1List()
    {
        return tel1List;
    }

    public ArrayList<String> getPlanNmList()
    {
        return planNmList;
    }

    public ArrayList<String> getReserveNoList()
    {
        return reserveNoList;
    }

    public ArrayList<String> getUserIdList()
    {
        return userIdList;
    }

    public ArrayList<String> getEstTimeArrivalList()
    {
        return this.estTimeArrivalList;
    }

    public String getUserId()
    {
        return this.userId;
    }

    public int getMaxCnt()
    {
        return this.maxCnt;
    }

    public ArrayList<Integer> getNoshowFlagList()
    {
        return this.noshowFlagList;
    }

    /**
     * 
     * setter
     * 
     */
    public void setAddressList(String address)
    {
        this.addressList.add( address );
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public void setDateFrom(int datefrom)
    {
        this.dateFROM = datefrom;
    }

    public void setDateTo(int dateto)
    {
        this.dateTO = dateto;
    }

    public void setDspList(int dspval)
    {
        this.dspList.add( dspval );
    }

    public void setRsvNo(String rsvno)
    {
        this.rsvNo = rsvno;
    }

    public void setIdList(int id)
    {
        this.idList.add( id );
    }

    public void setNmList(String nm)
    {
        this.nmList.add( nm );
    }

    public void setOptionList(String option)
    {
        this.optionList.add( option );
    }

    public void setRsvDateList(String rsvdate)
    {
        this.rsvDateList.add( rsvdate );
    }

    public void setRsvDateValList(int rsvdateVal)
    {
        this.rsvDateValList.add( rsvdateVal );
    }

    public void setStatusList(String status)
    {
        this.statusList.add( status );
    }

    public void setStatusValList(int status)
    {
        this.statusValList.add( status );
    }

    public void setSeqList(int seq)
    {
        this.seqList.add( seq );
    }

    public void setUserNmList(String usernm)
    {
        this.userNmList.add( usernm );
    }

    public void setTel1List(String tel1)
    {
        this.tel1List.add( tel1 );
    }

    public void setUserIdList(String userid)
    {
        this.userIdList.add( userid );
    }

    public void setPlanNmList(String plannm)
    {
        this.planNmList.add( plannm );
    }

    public void setReserveNoList(String reserveno)
    {
        this.reserveNoList.add( reserveno );
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setEstTimeArrivalList(String esttime)
    {
        this.estTimeArrivalList.add( esttime );
    }

    public void setNoshowFlagList(int noshowFlag)
    {
        this.noshowFlagList.add( noshowFlag );
    }

    public void setPaymentList(int payment)
    {
        this.paymentList.add( payment );
    }

    public void setPaymentStatusList(int paymentstatus)
    {
        this.paymentStatusList.add( paymentstatus );
    }

}
