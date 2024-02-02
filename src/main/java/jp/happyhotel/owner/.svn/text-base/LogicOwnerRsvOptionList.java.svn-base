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
 * 予約一覧画面 business Logic
 */
public class LogicOwnerRsvOptionList implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID      = -115937955416856550L;
    private static final int    listmax               = 5;                       // 一画面最大明細表示件数
    private static final String sts1                  = "来店待ち";
    private static final String sts2                  = "来店";
    private static final String sts3                  = "キャンセル";
    private static final String sts4                  = "<br>(仮来店)";
    private String              errMsg                = "";
    private int                 hotelId               = 0;
    private int                 maxCnt                = 0;
    private String              dateFROM              = "";
    private String              dateTO                = "";
    private String              rsvNo                 = "";
    private ArrayList<Integer>  idList                = new ArrayList<Integer>(); // ホテルID
    private ArrayList<String>   optionList            = new ArrayList<String>(); // オプション
    private ArrayList<Integer>  optionCountList       = new ArrayList<Integer>(); // オプション数量
    private ArrayList<String>   planNmList            = new ArrayList<String>(); // プラン名
    private ArrayList<String>   reserveNoList         = new ArrayList<String>(); // 予約番号
    private ArrayList<String>   rsvDateList           = new ArrayList<String>(); // 予約日
    private ArrayList<Integer>  rsvDateValList        = new ArrayList<Integer>(); // 予約日
    private ArrayList<Integer>  seqList               = new ArrayList<Integer>(); // 部屋番号
    private ArrayList<String>   userIdList            = new ArrayList<String>(); // ユーザID
    private ArrayList<String>   userNmList            = new ArrayList<String>(); // 利用者名
    private ArrayList<String>   tel1List              = new ArrayList<String>(); // 連絡先
    private ArrayList<String>   estTimeArrivalList    = new ArrayList<String>(); // 到着予定時刻
    private ArrayList<Integer>  estTimeArrivalValList = new ArrayList<Integer>(); // 到着予定時刻
    private ArrayList<Integer>  dspList               = new ArrayList<Integer>(); // 提供区分
    private ArrayList<String>   optionDataList        = new ArrayList<String>(); // オプション(一次的なデータ)
    private ArrayList<Integer>  optionCountDataList   = new ArrayList<Integer>(); // オプション数量(一次的なデータ)

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
        int hourF = 0;
        int minF = 0;
        int hourTo = 0;
        int minTo = 0;

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
            // 開始時刻
            if ( (request.getParameter( "time_f_h" ) == null) || (request.getParameter( "time_f_h" ).toString().length() == 0) ||
                    (request.getParameter( "time_f_m" ) == null) || (request.getParameter( "time_f_m" ).toString().length() == 0) )
            {
                errMsg = "";
            }
            else
            {
                // 時刻数値が正しいかチェック
                if ( checkTime( request.getParameter( "time_f_h" ), request.getParameter( "time_f_m" ) ) )
                {
                    // 時刻として正しい場合
                }
                else
                {
                    errMsg = errMsg + Message.getMessage( "warn.00009", "開始時刻" ) + "<br />";
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
                if ( dateT.equals( "0" ) || dateT.equals( "1" ) )
                {
                    // 選択日付判定
                }
                else
                {
                    errMsg = errMsg + Message.getMessage( "warn.00009", "終了日" ) + "<br />";
                    isResult = false;
                }
            }

            // 終了時刻
            if ( (request.getParameter( "time_t_h" ) == null) || (request.getParameter( "time_t_h" ).toString().length() == 0) ||
                    (request.getParameter( "time_t_m" ) == null) || (request.getParameter( "time_t_m" ).toString().length() == 0) )
            {
                errMsg = "";
            }
            else
            {
                // 時刻数値が正しいかチェック
                if ( checkTime( request.getParameter( "time_t_h" ), request.getParameter( "time_t_m" ) ) )
                {
                    // 時刻として正しい場合
                }
                else
                {
                    errMsg = errMsg + Message.getMessage( "warn.00009", "終了時刻" ) + "<br />";
                    isResult = false;
                }
            }

            if ( isResult )
            {
                // 範囲チェック
                // if ( (request.getParameter( "date_f" ) != null) && (request.getParameter( "date_f" ).toString().length() != 0) &&
                // (request.getParameter( "date_t" ) != null) && (request.getParameter( "date_t" ).toString().length() != 0) )
                // {
                // fromVal = Integer.parseInt( dateF.replace( "/", "" ) );
                // toVal = Integer.parseInt( dateT.replace( "/", "" ) );
                // if ( fromVal > toVal )
                // {
                // errMsg = errMsg + Message.getMessage( "warn.00012" ) + "<br />";
                // isResult = false;
                // }
                // }
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
     * 時・分チェック処理
     * 
     * @param hourStr 時間
     * @param minStr 分
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
     * 選択ホテルの情報を得る
     * 
     * @param frm
     * @return
     * @throws Exception
     */
    public boolean getHotelInfo(FormOwnerRsvOptionList frm) throws Exception
    {
        boolean isResult;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // 戻り値の初期化
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
                // 全件抽出
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
     * 予約情報取得
     * 
     * @param frm // 編集先ﾌｫｰﾑid
     * @param pageCnt // ページ番号(0から)
     * @param mode // ページング機能(ALL:ページング無し全件, PARTS:ページング有)
     * @return
     * @throws Exception
     */
    public boolean getData(FormOwnerRsvOptionList frm, int pageCnt, String mode) throws Exception
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
        int rsvTimeF = 0;
        int rsvDateT = 0;
        int rsvTimeT = 0;
        int intMaxCnt = 0;
        String estTime = "";
        int seq = 0;

        // 戻り値の初期化
        isResult = false;

        // 条件の編集
        if ( (this.dateFROM == null) || (this.dateFROM.toString().length() == 0) )
        {
            rsvDateF = 0;
        }
        else
        {
            convDate = this.dateFROM.replace( "/", "" );
            rsvDateF = Integer.parseInt( convDate );
        }
        rsvTimeF = Integer.parseInt( frm.getTimeFromHour() ) * 10000 + Integer.parseInt( frm.getTimeFromMin() ) * 100;
        if ( (this.dateTO == null) || (this.dateTO.toString().length() == 0) )
        {
            rsvDateT = 0;
        }
        else
        {
            convDate = this.dateTO.replace( "/", "" );
            rsvDateT = Integer.parseInt( convDate );
        }
        rsvTimeT = Integer.parseInt( frm.getTimeToHour() ) * 10000 + Integer.parseInt( frm.getTimeToMin() ) * 100;

        try
        {
            query = "SELECT hh_rsv_reserve.id, hh_rsv_reserve.reserve_no, hh_rsv_reserve.reserve_date, " +
                    " hh_rsv_reserve.name_last, hh_rsv_reserve.name_first, hh_rsv_reserve.option_charge_total, " +
                    " hh_rsv_reserve.seq, hh_rsv_plan.plan_name, hh_rsv_reserve.est_time_arrival , " +
                    " hh_rsv_reserve.user_id, hh_rsv_reserve.status, hh_rsv_plan.offer_kind, " +
                    " hh_rsv_reserve.temp_coming_flag, hh_rsv_reserve.tel1, hh_rsv_reserve.parking, hh_rsv_reserve.parking_count," +
                    " hh_rsv_reserve.demands, hh_rsv_reserve.remarks" +
                    " FROM hh_rsv_reserve INNER JOIN hh_rsv_plan " +
                    " ON ( hh_rsv_reserve.id = hh_rsv_plan.id AND hh_rsv_reserve.plan_id = hh_rsv_plan.plan_id ) " +
                    " WHERE hh_rsv_reserve.id = ? ";
            // 予約情報のみ
            queryWhere = " AND hh_rsv_reserve.status = 1 ";
            // 開始&終了
            if ( rsvDateF != 0 && rsvDateT != 0 )
            {
                queryWhere = queryWhere + " AND ( hh_rsv_reserve.reserve_date = ? AND hh_rsv_reserve.est_time_arrival >= ?) OR";
                queryWhere = queryWhere + " ( hh_rsv_reserve.reserve_date = ? AND hh_rsv_reserve.est_time_arrival < ?)";
            }

            if ( !this.rsvNo.equals( "" ) )
            {
                queryWhere = queryWhere + " AND hh_rsv_reserve.reserve_no LIKE ? ";
            }

            // 出力順は予約日、到着予定時刻順
            queryWhere = queryWhere + " ORDER BY hh_rsv_reserve.reserve_date, " +
                    "hh_rsv_reserve.est_time_arrival, hh_rsv_reserve.reserve_no";

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
                // 全件抽出
                while( result.next() )
                {
                    // 対象オプションデータ取得
                    setOptionData( connection, result.getInt( "id" ), result.getString( "reserve_no" ) );
                    // オプション数分ループ作成
                    for( i = 0 ; i < optionDataList.size() ; i++ )
                    {
                        this.setIdList( result.getInt( "id" ) ); // ホテルID
                        convDate = String.valueOf( result.getInt( "reserve_date" ) );
                        weekName = DateEdit.getWeekName( result.getInt( "reserve_date" ) );
                        this.setRsvDateList( convDate.substring( 0, 4 ) + "/" + convDate.substring( 4, 6 ) + "/" + convDate.substring( 6, 8 ) +
                                "(" + weekName + ")" );
                        this.setRsvDateValList( result.getInt( "reserve_date" ) );
                        this.setSeqList( result.getInt( "seq" ) );
                        this.setOptionList( this.optionDataList.get( i ) );
                        this.setOptionCountList( this.optionCountDataList.get( i ) );
                        this.setUserNmList( ConvertCharacterSet.convDb2Form( result.getString( "name_last" ).toString() ) +
                                ConvertCharacterSet.convDb2Form( result.getString( "name_first" ).toString() ) );
                        this.setTel1List( ConvertCharacterSet.convDb2Form( result.getString( "tel1" ).toString() ) );
                        this.setPlanNmList( ConvertCharacterSet.convDb2Form( result.getString( "plan_name" ).toString() ) );
                        this.setReserveNoList( result.getString( "reserve_no" ) );

                        estTime = ConvertTime.convTimeStr( result.getInt( "est_time_arrival" ), 3 );
                        this.setEstTimeArrivalList( estTime );
                        this.setEstTimeArrivalValList( result.getInt( "est_time_arrival" ) );

                        this.setUserIdList( result.getString( "user_id" ).toString() );

                        this.setDspList( result.getInt( "offer_kind" ) );
                        // 件数ｶｳﾝﾄ
                        intMaxCnt++;
                    }
                }

                if ( mode.equals( "ALL" ) )
                {
                    // 全件画面表示の場合はそのままフォームへ編集する
                    for( i = 0 ; i < intMaxCnt - 1 ; i++ )
                    {
                        frm.setIdList( this.idList.get( i ) );
                        frm.setRsvDateList( this.rsvDateList.get( i ) );
                        frm.setRsvDateValList( this.rsvDateValList.get( i ) );
                        frm.setSeqList( this.seqList.get( i ) );
                        frm.setOptionList( this.optionList.get( i ) );
                        frm.setUserNmList( this.userNmList.get( i ) );
                        frm.setTel1List( this.tel1List.get( i ) );
                        frm.setPlanNmList( this.planNmList.get( i ) );
                        frm.setReserveNoList( this.reserveNoList.get( i ) );
                        frm.setEstTimeArrivalList( this.estTimeArrivalList.get( i ) );
                        frm.setEstTimeArrivalValList( this.estTimeArrivalValList.get( i ) );
                        frm.setUserIdList( this.userIdList.get( i ) );
                        frm.setDspList( this.dspList.get( i ) );
                        frm.setOptionList( this.optionList.get( i ) );
                        frm.setOptionCountList( this.optionCountList.get( i ) );
                    }

                    isResult = true;
                    frm.setPageMax( intMaxCnt - 1 );
                    frm.setPageSt( 0 );
                    frm.setPageEd( intMaxCnt - 1 );
                }
                else if ( mode.equals( "PARTS" ) )
                {

                    // 抽出した件数のうち一部分のみフォームへ編集する
                    Logging.error( "☆" );
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
                            frm.setRsvDateList( this.rsvDateList.get( i ) );
                            frm.setRsvDateValList( this.rsvDateValList.get( i ) );
                            frm.setSeqList( this.seqList.get( i ) );
                            frm.setUserNmList( this.userNmList.get( i ) );
                            frm.setTel1List( this.tel1List.get( i ) );
                            frm.setPlanNmList( this.planNmList.get( i ) );
                            frm.setReserveNoList( this.reserveNoList.get( i ) );
                            frm.setEstTimeArrivalList( this.estTimeArrivalList.get( i ) );
                            frm.setEstTimeArrivalValList( this.estTimeArrivalValList.get( i ) );
                            frm.setUserIdList( this.userIdList.get( i ) );
                            frm.setDspList( this.dspList.get( i ) );
                            frm.setOptionList( this.optionList.get( i ) );
                            frm.setOptionCountList( this.optionCountList.get( i ) );
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
            Logging.error( "[LogicReserveListPC.getData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return isResult;
    }

    /**
     * 予約のオプションを取り出して編集結果を返す
     * 
     * @param connection DBコネクション
     * @param id ホテルID
     * @param reserve_no 予約番号
     * @return オプション編集結果
     */
    private void setOptionData(Connection connection, int id, String reserve_no)
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
                // 全件抽出
                while( result.next() )
                {
                    // オプションマスタが見つかれば、オプション名を取り出す
                    if ( result.getInt( "not_find" ) == 0 )
                    {
                        if ( result.getInt( "option_flag" ) == 1 ) // 必須
                        {
                            this.optionDataList.add( ConvertCharacterSet.convDb2Form( result.getString( "option_name" ) ) + "(" +
                                    ConvertCharacterSet.convDb2Form( result.getString( "option_sub_name" ) ) + ")" );
                        }
                        else
                        {
                            this.optionDataList.add( ConvertCharacterSet.convDb2Form( result.getString( "option_name" ) ) );
                        }
                    }
                    // 数量のセット
                    if ( result.getInt( "option_flag" ) != 1 )
                    {
                        this.optionCountDataList.add( result.getInt( "quantity" ) );
                    }
                    else
                    {
                        // 必須選択は1固定
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

    public ArrayList<Integer> getEstTimeArrivalValList()
    {
        return this.estTimeArrivalValList;
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

    public void setHotelId(int hotelid)
    {
        this.hotelId = hotelid;
    }

    public void setEstTimeArrivalList(String esttime)
    {
        this.estTimeArrivalList.add( esttime );
    }

    public void setEstTimeArrivalValList(int esttime)
    {
        this.estTimeArrivalValList.add( esttime );
    }

    public void setOptionCountList(Integer optinCountList)
    {
        this.optionCountList.add( optinCountList );
    }

    public ArrayList<Integer> getOptinCountList()
    {
        return optionCountList;
    }

}
