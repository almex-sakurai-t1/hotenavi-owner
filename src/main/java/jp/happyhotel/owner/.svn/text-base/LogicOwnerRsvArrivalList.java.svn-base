package jp.happyhotel.owner;

import java.io.*;
import java.sql.*;

import javax.servlet.http.HttpServletRequest;
import jp.happyhotel.common.*;
import jp.happyhotel.owner.FormOwnerRsvArrivalList;

/**
 *
 * 到着予定一覧画面 business Logic
 */
public class LogicOwnerRsvArrivalList implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 3723491086006455536L;
    private static final String sts1 = "受付";
    private static final String sts2 = "利用済み";
    private static final String sts3 = "キャンセル";
    private static final String sts4 = "<br>(仮来店)";
    private String             errMsg             = "";
    private int                hotelId            = 0;
    private int                maxCnt             = 0;
    private String             dateFROM           = "";
    private String             dateTO             = "";
    private String             rsvNo              = "";

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
                    errMsg = errMsg + Message.getMessage( "warn.00009", "終了日" ) + "<br>";
                    isResult = false;
                }
            }
            
            if (isResult)
            {
                // 範囲チェック
                if ( (request.getParameter( "date_f" ) != null) && (request.getParameter( "date_f" ).toString().length() != 0) &&
                        (request.getParameter( "date_t" ) != null) && (request.getParameter( "date_t" ).toString().length() != 0) )
                {
                    fromVal = Integer.parseInt( dateF.replace( "/", "" ) );
                    toVal = Integer.parseInt( dateT.replace( "/", "" ) );
                    if ( fromVal > toVal )
                    {
                        errMsg = errMsg + Message.getMessage( "warn.00012" ) + "<br>";
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
                    // 半角数字でない場合はエラー
                    errMsg = errMsg + Message.getMessage( "warn.30007", "予約番号", "半角英数字" ) + "<br>";
                    isResult = false;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvArrivalList.chkDsp] Exception=" + e.toString() );
            throw e;
        }

        // 戻り値
        return isResult;
    }

    /**
     * 予約情報取得
     *
     * @param frm           // 編集先ﾌｫｰﾑid
     * @param objKbn        // 抽出対象
     * @return
     * @throws Exception
     */
    public boolean getData(FormOwnerRsvArrivalList frm, int objKbn) throws Exception
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
        int rsvDateT = 0;
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
        if ( (this.dateTO == null) || (this.dateTO.toString().length() == 0) )
        {
            rsvDateT = 0;
        }
        else
        {
            convDate = this.dateTO.replace( "/", "" );
            rsvDateT = Integer.parseInt( convDate );
        }

        try
        {
            query = "SELECT R.id, R.reserve_no, R.reserve_date, " +
                    " R.name_last, R.name_first, R.option_charge_total, " +
                    " R.seq, P.plan_name, R.est_time_arrival , " +
                    " R.num_adult, R.num_child, " +
                    " R.user_id, R.status, R.temp_coming_flag, P.offer_kind " +
                    " FROM hh_rsv_reserve R INNER JOIN hh_rsv_plan P" +
                    " ON ( R.id = P.id AND R.plan_id = P.plan_id ) " +
                    " WHERE R.id = ? ";
            if ( objKbn == 1 )
            {
                // 予約情報のみ
                queryWhere = " AND R.status = 1 ";
            }
            else if ( objKbn == 2 )
            {
                // 履歴情報のみ
                queryWhere = " AND R.status = 2 ";
            }
            else if ( objKbn == 3 )
            {
                // キャンセル情報のみ
                queryWhere = " AND R.status = 3 ";
            }
            else if ( objKbn == 4 )
            {
                // 仮受付のみ
                queryWhere = " AND R.temp_coming_flag = 1 ";
            }
            else
            {
                // 全ての場合は条件無し
            }
            // 日付開始
            if ( rsvDateF != 0 )
            {
                queryWhere = queryWhere + " AND R.reserve_date >= ? ";
            }
            // 日付終了
            if ( rsvDateT != 0 )
            {
                queryWhere = queryWhere + " AND R.reserve_date <= ? ";
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
                    //frm.setIdList( result.getInt( "id" ) ); // ホテルID
                    convDate = String.valueOf( result.getInt( "reserve_date" ) );
                    weekName = DateEdit.getWeekName( result.getInt( "reserve_date" ) );
                    frm.setRsvDateList( convDate.substring( 0, 4 ) + "/" + convDate.substring( 4, 6 ) + "/" + convDate.substring( 6, 8 ) +
                            "(" + weekName + ")" );
                    frm.setSeqList( result.getInt( "seq" ) );
                    
                    //オプションの取り出し
                    String strOption = getOption(connection, result.getInt( "id" ), result.getString( "reserve_no" ));
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

                    if (result.getInt( "status" ) == 1)
                    {
                        frm.setStatusList( result.getInt( "temp_coming_flag" ) == 1 ? sts1 + sts4: sts1);
                    }
                    else if (result.getInt( "status" ) == 2)
                    {
                        frm.setStatusList( result.getInt( "temp_coming_flag" ) == 1 ? sts2 + sts4: sts2  );
                    }
                    else if (result.getInt( "status" ) == 3)
                    {
                        frm.setStatusList( result.getInt( "temp_coming_flag" ) == 1 ? sts3 + sts4: sts3 );
                    }

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
            Logging.error( "[LogicOwnerRsvArrivalList.getData] Exception=" + e.toString() );
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
     * @param connection    DBコネクション
     * @param id            ホテルID
     * @param reserve_no    予約番号
     * @return              オプション編集結果
     */
    private String getOption(Connection connection , int id, String reserve_no)
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
                    if (strOption.length() > 0) //2件目以降は、カンマで連結
                    {
                        strOption += "、";
                    }
                    //オプションマスタが見つかれば、オプション名を取り出す
                    if (result.getInt( "not_find" ) == 0)
                    {   
                        if (result.getInt( "option_flag" ) == 1)   //必須
                        {
                            strOption += ConvertCharacterSet.convDb2Form( result.getString( "option_name" )) + "(" +
                                         ConvertCharacterSet.convDb2Form( result.getString( "option_sub_name" )) + ")";
                        }
                        else
                        {
                            strOption += ConvertCharacterSet.convDb2Form( result.getString( "option_name" ));
                        }
                    }
                    if (result.getInt( "option_flag" ) != 1)
                    {
                        strOption += "×" + result.getInt( "quantity" );     //数量をセット
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvArrivalList.getOption] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result);
            DBConnection.releaseResources( prestate );
        }

        if (strOption.length() == 0)
        {
            strOption = "なし";
        }

        return strOption;
    }
    
    /**
     * 選択ホテルの情報を得る
     * @param frm
     * @return
     * @throws Exception
     */
    public boolean getHotelInfo(FormOwnerRsvArrivalList frm) throws Exception
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
            prestate = connection.prepareStatement(query);
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
