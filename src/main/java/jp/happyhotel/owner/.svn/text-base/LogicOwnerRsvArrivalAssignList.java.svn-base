package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataRsvReserveBasic;

/**
 * 
 * 到着予定一覧画面 business Logic
 */
public class LogicOwnerRsvArrivalAssignList implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID = 3723491086006455536L;
    private static final String sts1             = "受付";
    private static final String sts2             = "利用済み";
    private static final String sts3             = "キャンセル";
    private static final String sts4             = "<br>(仮来店)";
    private String              errMsg           = "";
    private int                 hotelId          = 0;
    private int                 maxCnt           = 0;
    private String              dateFROM         = "";
    private String              dateTO           = "";
    private String              rsvNo            = "";

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
                    errMsg = errMsg + Message.getMessage( "warn.00009", "開始日" ) + "<br>";
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
                    // 日付として正しい場合
                }
                else
                {
                    errMsg = errMsg + Message.getMessage( "warn.00009", "終了日" ) + "<br>";
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
                // errMsg = errMsg + Message.getMessage( "warn.00012" ) + "<br>";
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
                    // 半角数字でない場合はエラー
                    errMsg = errMsg + Message.getMessage( "warn.30007", "予約番号", "半角英数字" ) + "<br>";
                    isResult = false;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvArrivalAssignList.chkDsp] Exception=" + e.toString() );
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
     * 予約情報取得
     * 
     * @param frm // 編集先ﾌｫｰﾑid
     * @return
     * @throws Exception
     */
    public boolean getData(FormOwnerRsvArrivalAssignList frm) throws Exception
    {

        boolean isResult;
        String query = "";
        String queryWhere = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String convDate = "";
        String weekName = "";
        int rsvDateF = 0;
        int rsvTimeF = 0;
        int rsvDateT = 0;
        int rsvTimeT = 0;
        int intMaxCnt = 0;
        String estTime = "";
        int seq = 0;
        int parkingType = 0;
        DataRsvReserveBasic data;

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
        rsvTimeF = frm.getTimeFromHour() * 10000 + frm.getTimeFromMin() * 100;
        if ( (this.dateTO == null) || (this.dateTO.toString().length() == 0) )
        {
            rsvDateT = 0;
        }
        else
        {
            convDate = this.dateTO.replace( "/", "" );
            rsvDateT = Integer.parseInt( convDate );
        }
        rsvTimeT = frm.getTimeToHour() * 10000 + frm.getTimeToMin() * 100;

        try
        {
            data = new DataRsvReserveBasic();
            data.getData( frm.getSelHotelID() );
            parkingType = data.getParking();
            query = "SELECT R.id, R.reserve_no, R.reserve_date, " +
                    " R.name_last, R.name_first, R.option_charge_total, " +
                    " R.seq, P.plan_name, R.est_time_arrival , " +
                    " R.num_adult, R.num_child, R.tel1," +
                    " R.user_id, R.status, R.temp_coming_flag, P.offer_kind, R.parking, R.parking_count, R.demands, R.remarks " +
                    " FROM hh_rsv_reserve R INNER JOIN hh_rsv_plan P" +
                    " ON ( R.id = P.id AND R.plan_id = P.plan_id ) " +
                    " WHERE R.id = ? ";
            // 予約情報のみ
            queryWhere = " AND R.status = 1 ";

            // 開始終了セット
            if ( rsvDateF != 0 && rsvDateT != 0 )
            {
                queryWhere = queryWhere + " AND ( R.reserve_date = ? AND R.est_time_arrival >= ?) OR";
                queryWhere = queryWhere + " ( R.reserve_date = ? AND R.est_time_arrival < ?)";
            }

            if ( !this.rsvNo.equals( "" ) )
            {
                queryWhere = queryWhere + " AND R.reserve_no LIKE ? ";
            }

            // 出力順は予約日、到着予定時刻順
            queryWhere = queryWhere + " ORDER BY R.id, R.reserve_date, R.est_time_arrival, R.reserve_no";

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
                    // frm.setIdList( result.getInt( "id" ) ); // ホテルID
                    convDate = String.valueOf( result.getInt( "reserve_date" ) );
                    weekName = DateEdit.getWeekName( result.getInt( "reserve_date" ) );
                    frm.setRsvDateList( convDate.substring( 0, 4 ) + "/" + convDate.substring( 4, 6 ) + "/" + convDate.substring( 6, 8 ) +
                            "(" + weekName + ")" );
                    frm.setSeqList( result.getInt( "seq" ) );

                    // オプションの取り出し
                    String strOption = getOption( connection, result.getInt( "id" ), result.getString( "reserve_no" ) );
                    frm.setOptionList( strOption );

                    frm.setUserNmList( ConvertCharacterSet.convDb2Form( result.getString( "name_last" ).toString() ) +
                            ConvertCharacterSet.convDb2Form( result.getString( "name_first" ).toString() ) );
                    frm.setPlanNmList( ConvertCharacterSet.convDb2Form( result.getString( "plan_name" ).toString() ) );
                    frm.setReserveNoList( result.getString( "reserve_no" ) );

                    estTime = ConvertTime.convTimeStr( result.getInt( "est_time_arrival" ), 3 );
                    frm.setEstTimeArrivalList( estTime );

                    frm.setUserIdList( result.getString( "user_id" ).toString() );

                    frm.setNumAdultList( result.getInt( "num_adult" ) );
                    frm.setNumChildList( result.getInt( "num_child" ) );
                    frm.setTel1List( result.getString( "tel1" ) );

                    if ( result.getInt( "parking" ) == ReserveCommon.PRKING_USED_USE )
                    {
                        // 車あり
                        if ( parkingType == ReserveCommon.PARKING_INPUT_COUNT )
                        {
                            // 台数指定
                            frm.setParkingList( String.format( "%s台", result.getInt( "parking_count" ) ) );
                        }
                        else
                        {
                            // 台数指定なし
                            frm.setParkingList( "有" );
                        }
                    }
                    else
                    {
                        // 車なし
                        frm.setParkingList( "無" );
                    }
                    frm.setDemandsList( result.getString( "demands" ) );
                    frm.setRemarksList( result.getString( "remarks" ) );

                    frm.setDspList( result.getInt( "offer_kind" ) );
                    // 件数ｶｳﾝﾄ
                    intMaxCnt++;
                }

                isResult = true;
            }
            else
            {
                // not Found
                isResult = false;

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvArrivalAssignList.getData] Exception=" + e.toString() );
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
    private String getOption(Connection connection, int id, String reserve_no)
    {
        String strOption = "";
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
            prestate = connection.prepareStatement( sql );
            prestate.setInt( 1, id );
            prestate.setString( 2, reserve_no );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // 全件抽出
                while( result.next() )
                {
                    if ( strOption.length() > 0 ) // 2件目以降は、カンマで連結
                    {
                        strOption += "、";
                    }
                    // オプションマスタが見つかれば、オプション名を取り出す
                    if ( result.getInt( "not_find" ) == 0 )
                    {
                        if ( result.getInt( "option_flag" ) == 1 ) // 必須
                        {
                            strOption += ConvertCharacterSet.convDb2Form( result.getString( "option_name" ) ) + "(" +
                                         ConvertCharacterSet.convDb2Form( result.getString( "option_sub_name" ) ) + ")";
                        }
                        else
                        {
                            strOption += ConvertCharacterSet.convDb2Form( result.getString( "option_name" ) );
                        }
                    }
                    if ( result.getInt( "option_flag" ) != 1 )
                    {
                        strOption += "×" + result.getInt( "quantity" ); // 数量をセット
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvArrivalAssignList.getOption] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        if ( strOption.length() == 0 )
        {
            strOption = "なし";
        }

        return strOption;
    }

    /**
     * 選択ホテルの情報を得る
     * 
     * @param frm
     * @return
     * @throws Exception
     */
    public boolean getHotelInfo(FormOwnerRsvArrivalAssignList frm) throws Exception
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
            Logging.error( "[LogicOwnerRsvArrivalAssignList.getHotelInfo] Exception=" + e.toString() );
            throw e;
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

    public void setRsvNo(String rsvno)
    {
        this.rsvNo = rsvno;
    }

    public void setHotelId(int hotelid)
    {
        this.hotelId = hotelid;
    }

}
