package jp.happyhotel.owner;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Kanma;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelNewhappie;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 月別クレジット明細ロジック
 */
public class LogicOwnerBkoCreditDetail implements Serializable
{

    private static final long        serialVersionUID = -6778531648260740904L;
    String                           strTitle         = "ハピホテ予約　月別クレジット明細";

    private FormOwnerBkoCreditDetail frm;

    /* フォームオブジェクト */
    public FormOwnerBkoCreditDetail getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoCreditDetail frm)
    {
        this.frm = frm;
    }

    /**
     * クレジット明細取得
     * 
     * @param なし
     * @return なし
     */
    public void getCreditDetail() throws Exception
    {
        // 対象の年月取得
        getCreditDateList();
        // クレジット明細情報取得
        getCreditDetailHeader();

    }

    /**
     * クレジット情報取得
     * 
     * @param なし
     * @return なし
     */
    private void getCreditDetailHeader() throws Exception
    {
        String query = "";
        String queryL = "";
        String queryO = "";
        String queryOta = "";
        String queryOtaVisit = "";
        String queryOtaCancel = "";
        String reserveNo = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        int amountTotal = 0;
        int creditFeeTotal = 0;
        int amountSum = 0;

        int selFrom = 0;
        int selTo = 0;
        selFrom = frm.getSelYearFrom() * 10000 + frm.getSelMonthFrom() * 100 + 1;
        selTo = frm.getSelYearTo() * 10000 + frm.getSelMonthTo() * 100 + 31;

        // newRsvDB.hh_rsv_creditよりセット
        ArrayList<String> reserveNoList = new ArrayList<String>(); // 予約番号
        ArrayList<String> tranidList = new ArrayList<String>(); // トランザクションID
        ArrayList<String> approvedList = new ArrayList<String>(); // 承認番号
        ArrayList<String> forwardedList = new ArrayList<String>(); // 仕向先
        ArrayList<Integer> amountList = new ArrayList<Integer>(); // 請求金額
        ArrayList<Integer> creditFeeList = new ArrayList<Integer>(); // クレジット手数料金額
        ArrayList<Integer> salesDateList = new ArrayList<Integer>(); // 売上日付
        ArrayList<Integer> salesTimeList = new ArrayList<Integer>(); // 売上時刻
        ArrayList<Integer> salesFlagList = new ArrayList<Integer>(); // 売上フラグ
        ArrayList<Integer> delDateList = new ArrayList<Integer>(); // 削除日付
        ArrayList<Integer> delTimeList = new ArrayList<Integer>(); // 削除時刻
        ArrayList<Integer> delFlagList = new ArrayList<Integer>(); // 削除フラグ

        // newRsvDB.hh_rsv_reserveよりセット
        ArrayList<Integer> reserveSubNoList = new ArrayList<Integer>(); // 予約サブNo
        ArrayList<Integer> reserveDateList = new ArrayList<Integer>(); // 予約日
        ArrayList<String> nameList = new ArrayList<String>(); // 予約者名（name_last&" "&name_first)
        ArrayList<Integer> statusList = new ArrayList<Integer>(); // ステータス
        ArrayList<Integer> noshowList = new ArrayList<Integer>(); // ノーショー
        ArrayList<Integer> extFlagList = new ArrayList<Integer>(); // 予約の区別(0:ハピホテからの予約1:lvjからの予約2:OTAからの予約)

        // hotenavi.hh_user_data_indexより
        ArrayList<Integer> userSeqList = new ArrayList<Integer>(); // ホテルごとのユーザーID

        query = "SELECT";
        query += " hrc.reserve_no";
        query += ",hrc.seq";
        query += ",hrc.tranid";
        query += ",hrc.approved";
        query += ",hrc.forwarded";
        query += ",hrc.amount";
        query += ",hrc.credit_fee";
        query += ",'' AS main_reserve";
        query += ",hrc.sales_date";
        query += ",hrc.sales_time";
        query += ",hrc.sales_flag";
        query += ",hrc.del_date";
        query += ",hrc.del_time";
        query += ",hrc.del_flag";
        query += ",hrr.reserve_sub_no";
        query += ",hrr.reserve_date";
        query += ",hrr.name_last";
        query += ",hrr.name_first";
        query += ",hrr.status";
        query += ",hrr.noshow_flag";
        query += ",hrr.ext_flag";
        query += ",0 AS reserve_date_to";
        query += ",0 AS co_time";
        query += ",0 AS est_time_arrival";
        query += ",CASE WHEN hudi.user_seq IS NULL THEN 0 ELSE hudi.user_seq END AS user_seq";
        query += " FROM newRsvDB.hh_rsv_reserve hrr";
        query += " INNER JOIN newRsvDB.hh_rsv_credit hrc ON hrr.reserve_no = hrc.reserve_no";
        query += " LEFT JOIN hh_user_data_index hudi ON hrr.user_id = hudi.user_id AND hrr.id = hudi.id";
        query += " WHERE hrr.id = ?";
        query += " AND hrc.sales_date BETWEEN ? AND ?"; // クレジットの決済日付をもとに検索する
        query += " AND hrr.ext_flag = ?";// 予約の区別(0:ハピホテからの予約)

        // ラブインジャパン来店データ
        queryL += "SELECT";
        queryL += " hrc.reserve_no";
        queryL += ",hrc.seq";
        queryL += ",hrc.tranid";
        queryL += ",hrc.approved";
        queryL += ",hrc.forwarded";
        queryL += ",hrc.amount";
        queryL += ",hrc.credit_fee";
        queryL += ",'' AS main_reserve";
        queryL += ",hrc.sales_date";
        queryL += ",hrc.sales_time";
        queryL += ",hrc.sales_flag";
        queryL += ",hrc.del_date";
        queryL += ",hrc.del_time";
        queryL += ",hrc.del_flag";
        queryL += ",hrr.reserve_sub_no";
        queryL += ",hrr.reserve_date";
        queryL += ",hrr.name_last";
        queryL += ",hrr.name_first";
        queryL += ",hrr.status";
        queryL += ",hrr.noshow_flag";
        queryL += ",hrr.ext_flag";
        queryL += ",0 AS reserve_date_to";
        queryL += ",0 AS co_time";
        queryL += ",0 AS est_time_arrival";
        queryL += ",CASE WHEN hudi.user_seq IS NULL THEN 0 ELSE hudi.user_seq END AS user_seq";
        queryL += " FROM newRsvDB.hh_rsv_reserve hrr";
        queryL += " INNER JOIN newRsvDB.hh_rsv_credit hrc ON hrr.reserve_no = hrc.reserve_no";
        queryL += " LEFT JOIN hh_user_data_index hudi ON hrr.user_id = hudi.user_id AND hrr.id = hudi.id";
        queryL += " WHERE hrr.id = ?";
        queryL += " AND hrr.accept_date BETWEEN ? AND ?"; // 予約データの受入日付をもとに検索する
        queryL += " AND hrr.ext_flag = ?";// 予約の区別(1:lvjからの予約)
        queryL += " AND hrc.sales_flag = ?"; // 実売上フラグ（1:売上）
        queryL += " AND hrr.status <> ?";// ステータス(キャンセル以外)(3：キャンセル)

        queryL += " UNION ";
        // ラブインジャパンキャンセルデータ
        queryL += "SELECT";
        queryL += " hrc.reserve_no";
        queryL += ",hrc.seq";
        queryL += ",hrc.tranid";
        queryL += ",hrc.approved";
        queryL += ",hrc.forwarded";
        queryL += ",hrc.amount";
        queryL += ",hrc.credit_fee";
        queryL += ",'' AS main_reserve";
        queryL += ",hrc.sales_date";
        queryL += ",hrc.sales_time";
        queryL += ",hrc.sales_flag";
        queryL += ",hrc.del_date";
        queryL += ",hrc.del_time";
        queryL += ",hrc.del_flag";
        queryL += ",hrr.reserve_sub_no";
        queryL += ",hrr.reserve_date";
        queryL += ",hrr.name_last";
        queryL += ",hrr.name_first";
        queryL += ",hrr.status";
        queryL += ",hrr.noshow_flag";
        queryL += ",hrr.ext_flag";
        queryL += ",0 AS reserve_date_to";
        queryL += ",0 AS co_time";
        queryL += ",0 AS est_time_arrival";
        queryL += ",CASE WHEN hudi.user_seq IS NULL THEN 0 ELSE hudi.user_seq END AS user_seq";
        queryL += " FROM newRsvDB.hh_rsv_reserve hrr";
        queryL += " INNER JOIN newRsvDB.hh_rsv_credit hrc ON hrr.reserve_no = hrc.reserve_no";
        queryL += " LEFT JOIN hh_user_data_index hudi ON hrr.user_id = hudi.user_id AND hrr.id = hudi.id";
        queryL += " WHERE hrr.id = ?";
        queryL += " AND hrr.reserve_date BETWEEN ? AND ?"; // 予約データの予約日付をもとに検索する
        queryL += " AND hrr.ext_flag = ?";// 予約の区別(1:lvjからの予約)
        queryL += " AND hrr.status = ?";// ステータス(3：キャンセル)

        queryO += " ORDER BY reserve_date, seq,sales_date, sales_time";

        queryOta = "SELECT";
        queryOta += " hrr.reserve_no";
        queryOta += ",0 AS seq";
        queryOta += ",'' AS tranid";
        queryOta += ",'' AS approved";
        queryOta += ",'' AS forwarded";
        queryOta += ",CASE WHEN hrr.reserve_no_main = '' THEN hrr.charge_total ELSE hrr.charge_total_all END AS amount";
        queryOta += ",hrrb.hotel_fee_ota AS credit_fee";
        queryOta += ",hrr.reserve_no_main AS main_reserve";
        queryOta += ",0 AS sales_date";
        queryOta += ",0 AS sales_time";
        queryOta += ",CASE WHEN hrr.payment_status = 1 THEN 1 ELSE 0 END AS sales_flag";
        queryOta += ",hrr.cancel_date AS del_date";
        queryOta += ",0 AS del_time";
        queryOta += ",CASE WHEN hrr.status = 3 AND hrr.cancel_date != 0 THEN 1 ELSE 0 END AS del_flag";
        queryOta += ",hrr.reserve_sub_no";
        queryOta += ",hrr.reserve_date";
        queryOta += ",hrr.name_last";
        queryOta += ",hrr.name_first";
        queryOta += ",hrr.status";
        queryOta += ",hrr.noshow_flag";
        queryOta += ",hrr.ext_flag";
        queryOta += ",hrr.reserve_date_to";
        queryOta += ",hrr.co_time";
        queryOta += ",hrr.est_time_arrival";
        queryOta += ",CASE WHEN hudi.user_seq IS NULL THEN 0 ELSE hudi.user_seq END AS user_seq";
        queryOta += " FROM newRsvDB.hh_rsv_reserve hrr";
        queryOta += " INNER JOIN newRsvDB.hh_rsv_reserve_basic hrrb ON hrrb.id = hrr.id";
        queryOta += " LEFT JOIN hotenavi.hh_user_data_index hudi ON hrr.user_id = hudi.user_id AND hrr.id = hudi.id";
        queryOta += " WHERE hrr.id = ? ";
        queryOta += " AND hrr.ext_flag = ? ";

        // OTA来店データ
        queryOtaVisit = " AND hrr.accept_date BETWEEN ? AND ? ";
        queryOtaVisit += " AND hrr.payment_status = ? ";
        queryOtaVisit += " AND hrr.status <> ? ";

        // OTAキャンセルデータ
        queryOtaCancel = " AND hrr.reserve_date BETWEEN ? AND ?";
        queryOtaCancel += " AND hrr.status = ? ";

        if ( frm.getRsvKind() == 1 )
        {
            // ハピホテからの予約
            query = query + queryO;
        }
        else if ( frm.getRsvKind() == 2 )
        {
            // LIJからの予約
            query = queryL + queryO;
        }
        else if ( frm.getRsvKind() == 3 )
        {
            // OTAからの予約
            query = queryOta + queryOtaVisit + " UNION " + queryOta + queryOtaCancel + queryO;
        }
        else
        {
            // ハピホテと LIJ、OTAからの予約
            query = query + " UNION " + queryL + " UNION " + queryOta + queryOtaVisit + " UNION " + queryOta + queryOtaCancel + queryO;
        }
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            int i = 1;
            if ( frm.getRsvKind() == 1 )
            {
                prestate.setInt( i++, frm.getSelHotelID() );
                prestate.setInt( i++, selFrom );
                prestate.setInt( i++, selTo );
                prestate.setInt( i++, 0 );// ext_flag=0
            }
            else if ( frm.getRsvKind() == 2 )
            {
                prestate.setInt( i++, frm.getSelHotelID() );
                prestate.setInt( i++, selFrom );
                prestate.setInt( i++, selTo );
                prestate.setInt( i++, 1 );// ext_flag=1
                prestate.setInt( i++, 1 );// sales_flag=1
                prestate.setInt( i++, 3 );// status != 3

                prestate.setInt( i++, frm.getSelHotelID() );
                prestate.setInt( i++, selFrom );
                prestate.setInt( i++, selTo );
                prestate.setInt( i++, 1 );// ext_flag=1
                prestate.setInt( i++, 3 );// status != 3
            }
            else if ( frm.getRsvKind() == 3 )
            {
                prestate.setInt( i++, frm.getSelHotelID() );
                prestate.setInt( i++, 2 );// ext_flag=2
                prestate.setInt( i++, selFrom );
                prestate.setInt( i++, selTo );
                prestate.setInt( i++, 1 );// payment_status = 1
                prestate.setInt( i++, 3 );// status != 3

                prestate.setInt( i++, frm.getSelHotelID() );
                prestate.setInt( i++, 2 );// ext_flag=2
                prestate.setInt( i++, selFrom );
                prestate.setInt( i++, selTo );
                prestate.setInt( i++, 3 );// status = 3
            }
            else
            {
                prestate.setInt( i++, frm.getSelHotelID() );
                prestate.setInt( i++, selFrom );
                prestate.setInt( i++, selTo );
                prestate.setInt( i++, 0 );// ext_flag=0

                // LIJ
                prestate.setInt( i++, frm.getSelHotelID() );
                prestate.setInt( i++, selFrom );
                prestate.setInt( i++, selTo );
                prestate.setInt( i++, 1 );// ext_flag=1
                prestate.setInt( i++, 1 );// sales_flag=1
                prestate.setInt( i++, 3 );// status != 3

                prestate.setInt( i++, frm.getSelHotelID() );
                prestate.setInt( i++, selFrom );
                prestate.setInt( i++, selTo );
                prestate.setInt( i++, 1 );// ext_flag=1
                prestate.setInt( i++, 3 );// status != 3

                // OTA
                prestate.setInt( i++, frm.getSelHotelID() );
                prestate.setInt( i++, 2 );// ext_flag=2
                prestate.setInt( i++, selFrom );
                prestate.setInt( i++, selTo );
                prestate.setInt( i++, 1 );// payment_status = 1
                prestate.setInt( i++, 3 );// status != 3

                prestate.setInt( i++, frm.getSelHotelID() );
                prestate.setInt( i++, 2 );// ext_flag=2
                prestate.setInt( i++, selFrom );
                prestate.setInt( i++, selTo );
                prestate.setInt( i++, 3 );// status = 3
            }

            result = prestate.executeQuery();

            while( result.next() )
            {
                int amount = 0;
                int fee = 0;
                int otaFee = 0;

                // OTA
                if ( result.getInt( "ext_flag" ) == 2 )
                {
                    String mainReserve = result.getString( "main_reserve" );
                    reserveNo = result.getString( "reserve_no" );
                    // 連泊プラン
                    if ( !mainReserve.equals( "" ) )
                    {
                        reserveNo = reserveNo.substring( reserveNo.indexOf( "-" ) + 1 );
                        // 連泊の場合、予約親番号以外表示しない
                        if ( !mainReserve.equals( reserveNo ) )
                        {
                            continue;
                        }
                    }
                    amount = result.getInt( "amount" );
                    // チェックアウト日
                    int reserveDatTo = result.getInt( "reserve_date_to" );
                    if ( reserveDatTo == 0 )
                    {
                        reserveDatTo = result.getInt( "reserve_date" );
                    }
                    // 到着時刻 < チェックアウト時刻のとき当日。到着時刻 > チェックアウト時刻のとき翌日。
                    int checkOutDate = 0;
                    int coTimeBuf = result.getInt( "co_time" );
                    if ( coTimeBuf >= 240000 )
                    {
                        coTimeBuf -= 240000;
                    }
                    if ( result.getInt( "est_time_arrival" ) < coTimeBuf )
                    {
                        checkOutDate = reserveDatTo;
                    }
                    else
                    {
                        checkOutDate = DateEdit.addDay( reserveDatTo, 1 );// checkoutはreserve_date_toの翌日
                    }
                    salesDateList.add( checkOutDate ); // OTAの決済日付はチェックアウト日

                    otaFee = result.getInt( "credit_fee" );
                    fee = (int)(amount * otaFee / 100);
                }
                // その他
                else
                {
                    amount = result.getInt( "amount" );
                    fee = result.getInt( "credit_fee" );
                    salesDateList.add( result.getInt( "sales_date" ) );
                }
                reserveNoList.add( result.getString( "reserve_no" ) );
                tranidList.add( result.getString( "tranid" ) );
                approvedList.add( result.getString( "approved" ) );
                forwardedList.add( result.getString( "forwarded" ) );
                salesTimeList.add( result.getInt( "sales_time" ) );
                salesFlagList.add( result.getInt( "sales_flag" ) );
                delDateList.add( result.getInt( "del_date" ) );
                delTimeList.add( result.getInt( "del_time" ) );
                delFlagList.add( result.getInt( "del_flag" ) );
                reserveSubNoList.add( result.getInt( "reserve_sub_no" ) );
                reserveDateList.add( result.getInt( "reserve_date" ) );
                nameList.add( result.getString( "name_last" ) + " " + result.getString( "name_first" ) );
                userSeqList.add( result.getInt( "user_seq" ) );
                statusList.add( result.getInt( "status" ) );
                noshowList.add( result.getInt( "noshow_flag" ) );
                extFlagList.add( result.getInt( "ext_flag" ) );
                amountList.add( amount );
                creditFeeList.add( fee );
                amountTotal += amount;

                if ( result.getInt( "sales_flag" ) == 1 && result.getInt( "del_flag" ) != 1 )
                {
                    creditFeeTotal += fee;
                    amountSum += amount - fee;
                }

            }

            frm.setReserveNoList( reserveNoList );
            frm.setTranidList( tranidList );
            frm.setApprovedList( approvedList );
            frm.setForwardedList( forwardedList );
            frm.setAmountList( amountList );
            frm.setCreditFeeList( creditFeeList );
            frm.setSalesDateList( salesDateList );
            frm.setSalesTimeList( salesTimeList );
            frm.setSalesFlagList( salesFlagList );
            frm.setDelDateList( delDateList );
            frm.setDelTimeList( delTimeList );
            frm.setDelFlagList( delFlagList );
            frm.setReserveSubNoList( reserveSubNoList );
            frm.setReserveDateList( reserveDateList );
            frm.setNameList( nameList );
            frm.setUserSeqList( userSeqList );
            frm.setStatusList( statusList );
            frm.setNoshowList( noshowList );
            frm.setExtFlagList( extFlagList );

            frm.setAmountTotal( amountTotal );
            frm.setCreditFeeTotal( creditFeeTotal );
            frm.setAmountSum( amountSum );

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "クレジット明細" ) );
                return;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoCreditDetail.getCreditDetail] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 対象年月取得(0の月も表示するように変更)
     * 
     * @param なし
     * @return なし
     */
    private void getCreditDateList() throws Exception
    {
        final int NEXT_MONTH = 1;
        int count = 0;
        ArrayList<String> creditDateStrList = new ArrayList<String>();
        ArrayList<Integer> creditDateList = new ArrayList<Integer>();
        int selFrom = 0;
        int selTo = 0;
        DataHotelNewhappie dhn = new DataHotelNewhappie();

        selFrom = frm.getSelYearFrom() * 100 + frm.getSelMonthFrom();
        selTo = frm.getSelYearTo() * 100 + frm.getSelMonthTo();
        dhn.getData( frm.getSelHotelID() );
        // 指定された開始日よりもバックオフィスの表示開始日の方が大きい場合は、バックオフィスの表示開始日をセット
        if ( selFrom < dhn.getBkoDateStart() / 100 )
        {
            selFrom = dhn.getBkoDateStart() / 100;
        }

        int i = 0;
        count = 0;
        i = selFrom;
        while( i <= selTo )
        {
            creditDateList.add( i );
            creditDateStrList.add( Integer.toString( i ).substring( 0, 4 ) + "/" + Integer.toString( i ).substring( 4, 6 ) );
            i = DateEdit.addMonth( i * 100 + 01, NEXT_MONTH ) / 100;
            count++;
        }

        try
        {
            // 該当データがない場合
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "クレジット明細" ) );
                return;
            }

            frm.setCreditDateIntList( creditDateList );
            frm.setCreditDateStrList( creditDateStrList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoCreditDetail.getCreditDateList] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        if ( request.getParameter( "send" ) != null )
        {
            if ( request.getParameter( "send" ).equals( "PDF" ) )
            {
                createPDF( request, response );
            }
            else
            {
                createCSV( request, response );
            }
        }
    }

    public void createPDF(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {

        int count = 0;
        // 画面からの送信されてきたパラメータを取得
        request.setCharacterEncoding( "Shift-JIS" );
        // 出力用のStreamをインスタンス化
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();

        // 文書オブジェクトを生成
        // ページサイズを設定
        Document doc = new Document( PageSize.A4, 50, 50, 20, 50 );

        try
        {
            // アウトプットストリームをPDFWriterに設定
            PdfWriter.getInstance( doc, byteout );

            // TODO
            doc.addAuthor( "株式会社アルメックス" );
            doc.addSubject( strTitle );

            // フッター出力
            HeaderFooter footer = new HeaderFooter( new Phrase( "-" ), new Phrase( "-" ) );
            footer.setAlignment( Element.ALIGN_CENTER );
            footer.setBorder( Rectangle.NO_BORDER );
            doc.setFooter( footer );

            // 文書のオープン
            doc.open();

            // タイトル行
            doc.add( outputTitle1() );
            doc.add( outputNothing( 2 ) );
            doc.add( outputTitle2() );
            doc.add( outputNothing( 5 ) );

            // 明細へダー
            doc.add( outputHeader() );
            // 明細出力
            for( int i = 0 ; i < frm.getReserveNoList().size() ; i++ )
            {
                count++;
                if ( count > 25 ) // 改ページ
                {
                    doc.add( outputNothing( 10 ) );
                    // タイトル行
                    doc.add( outputTitle1() );
                    doc.add( outputNothing( 2 ) );
                    doc.add( outputTitle2() );
                    doc.add( outputNothing( 5 ) );
                    // 明細へダー
                    doc.add( outputHeader() );
                    count = 1;
                }
                // 明細
                doc.add( outputDetail( i ) );
            }
            // 合計出力
            doc.add( outputTotal() );

        }
        catch ( DocumentException e )
        {
            e.printStackTrace();
            Logging.error( "error : " + e.getMessage() );
        }

        // 出力終了
        doc.close();

        // ブラウザへのデータを送信
        response.setContentType( "application/pdf" );
        response.setContentLength( byteout.size() );
        OutputStream out = response.getOutputStream();
        out.write( byteout.toByteArray() );
        out.close();
    }

    /*
     * 【PDF】タイトル行の出力(1行目)
     */
    private PdfPTable outputTitle1() throws DocumentException, IOException
    {
        PdfPTable table = new PdfPTable( 3 );

        Font fontM15B = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 15, Font.BOLD );
        Font fontM10B = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10, Font.BOLD );
        Font fontM09 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 9 );

        PdfPCell cell = new PdfPCell( new Phrase( strTitle, fontM15B ) );
        table.setWidthPercentage( 100f );
        int table_width[] = { 60, 20, 20 };
        table.setWidths( table_width );
        cell.setBorderWidth( 0f );
        cell.setBorderWidthLeft( 0f );
        cell.setBorderWidthBottom( 0f );
        cell.setBorderWidthRight( 0f );
        cell.setBorderWidthTop( 0f );
        table.addCell( cell );
        cell.setPhrase( new Phrase( frm.getSelHotelName(), fontM10B ) );
        table.addCell( cell );
        cell.setPhrase( new Phrase( DateEdit.getDate( 1 ) + " " + DateEdit.getTime( 0 ), fontM09 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        table.addCell( cell );
        return table;
    }

    /*
     * 【PDF】タイトル行の出力(2行目)
     */
    private PdfPTable outputTitle2() throws DocumentException, IOException
    {
        String rsvKind = "";
        if ( frm.getRsvKind() == 1 )
        {
            rsvKind = "(ハピホテ)";
        }
        else if ( frm.getRsvKind() == 2 )
        {
            rsvKind = "(Loveinn Japan)";
        }

        PdfPTable table = new PdfPTable( 2 );
        DataHotelBasic dhb = new DataHotelBasic();
        String hotelName = "";
        Font fontM10B = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10, Font.BOLD );
        Font fontM10 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10 );
        if ( dhb.getData( frm.getSelHotelID() ) )
        {
            hotelName = dhb.getName();
        }

        PdfPCell cell = new PdfPCell( new Phrase( hotelName + rsvKind, fontM10B ) );
        table.setWidthPercentage( 100f );
        int table_width[] = { 60, 40 };
        table.setWidths( table_width );
        cell.setBorderWidth( 0f );
        cell.setBorderWidthLeft( 0f );
        cell.setBorderWidthBottom( 0f );
        cell.setBorderWidthRight( 0f );
        cell.setBorderWidthTop( 0f );
        table.addCell( cell );
        cell.setPhrase( new Phrase( "決済日付:" + frm.getSelYearFrom() + "年" + frm.getSelMonthFrom() + "月～" + frm.getSelYearTo() + "年" + frm.getSelMonthTo() + "月", fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        table.addCell( cell );
        return table;
    }

    /*
     * 【PDF】明細へダー
     */
    private Table outputHeader() throws DocumentException, IOException
    {
        Font fontM10 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10 );

        // 明細の出力
        Table table = new Table( 8 );
        table.setWidth( 100 ); // 全体の幅
        table.setWidths( new int[]{ 18, 17, 8, 12, 12, 12, 9, 12 } ); // 列幅(%)
        table.setDefaultHorizontalAlignment( Element.ALIGN_CENTER ); // 表示位置（横）
        table.setDefaultVerticalAlignment( Element.ALIGN_MIDDLE ); // 表示位置（縦）
        table.setPadding( 1 ); // 余白
        table.setSpacing( 0 ); // セル間の間隔
        table.setBorderColor( new Color( 0, 0, 0 ) ); // 線の色

        // 1行目
        Cell cell = new Cell( new Phrase( "予約No", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 2 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "氏名", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 2 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "ユーザ\r\nID", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 2 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "チェック\r\nイン", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 2 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "クレジット", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setColspan( 4 );
        table.addCell( cell );

        // 2行目

        cell = new Cell( new Phrase( "決済日付", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "料金", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "手数料", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "入金額", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        table.addCell( cell );

        return table;
    }

    /*
     * 【PDF】明細
     */
    private PdfPTable outputDetail(int i) throws DocumentException, IOException
    {
        Font fontM10 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10 );
        Font fontM10ST = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10, Font.STRIKETHRU );
        // 明細の出力
        PdfPTable table = new PdfPTable( 8 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 18, 17, 8, 12, 12, 12, 9, 12 } ); // 列幅(%)

        // 予約No
        PdfPCell cell = new PdfPCell( new Phrase( frm.getReserveNoList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_CENTER );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthLeft( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setMinimumHeight( 25f );
        table.addCell( cell );

        // 氏名
        cell = new PdfPCell( new Phrase( frm.getNameList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_LEFT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // ユーザーID
        cell = new PdfPCell( new Phrase( Integer.toString( frm.getUserSeqList().get( i ) ), fontM10 ) );
        cell.setPaddingRight( 5 );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // チェックイン
        String str = DateEdit.formatDate( 5, frm.getReserveDateList().get( i ) );
        if ( frm.getStatusList().get( i ) == 3 )
        {
            str += "\nキャンセル";
        }
        cell = new PdfPCell( new Phrase( str, fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_CENTER );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // 決済日付
        if ( frm.getSalesFlagList().get( i ) == 1 )
        {
            str = DateEdit.formatDate( 5, frm.getSalesDateList().get( i ) );
        }
        else if ( frm.getStatusList().get( i ) != 3 )
        {
            str = "来店未処理";
        }
        if ( frm.getStatusList().get( i ) == 2 && frm.getDelFlagList().get( i ) != 0 )
        {
            str += "\nキャンセル";
        }
        cell = new PdfPCell( new Phrase( str, fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_CENTER );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // 料金
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getAmountList().get( i ) ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setPaddingRight( 5 );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // 手数料
        if ( frm.getDelFlagList().get( i ) == 1 )
        {
            cell = new PdfPCell( new Phrase( Kanma.get( frm.getCreditFeeList().get( i ) ), fontM10ST ) );
        }
        else
        {
            cell = new PdfPCell( new Phrase( Kanma.get( frm.getCreditFeeList().get( i ) ), fontM10 ) );
        }
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setPaddingRight( 5 );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // 入金額
        if ( frm.getDelFlagList().get( i ) == 1 )
        {
            cell = new PdfPCell( new Phrase( Kanma.get( frm.getAmountList().get( i ) - frm.getCreditFeeList().get( i ) ), fontM10ST ) );
        }
        else
        {
            cell = new PdfPCell( new Phrase( Kanma.get( frm.getAmountList().get( i ) - frm.getCreditFeeList().get( i ) ), fontM10 ) );
        }
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setPaddingRight( 5 );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        return table;
    }

    /*
     * 【PDF】合計
     */
    private PdfPTable outputTotal() throws DocumentException, IOException
    {
        Font fontM10 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10 );

        // 合計の出力
        PdfPTable table = new PdfPTable( 5 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 55, 12, 12, 9, 12 } ); // 列幅(%)

        // 空白
        PdfPCell cell = new PdfPCell( new Phrase( "", fontM10 ) );
        cell.setBorderWidth( 0f );
        cell.setBorderWidthLeft( 0f );
        cell.setBorderWidthBottom( 0f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthTop( 1f );
        cell.setMinimumHeight( 25f );
        table.addCell( cell );

        // 合計
        cell = new PdfPCell( new Phrase( "合　計", fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_CENTER );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // 料金
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getAmountTotal() ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setPaddingRight( 5 );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // 手数料
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getCreditFeeTotal() ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setPaddingRight( 5 );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // 入金額
        // cell = new PdfPCell( new Phrase( Kanma.get( frm.getAmountTotal() - frm.getCreditFeeTotal() ), fontM10 ) );
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getAmountSum() ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setPaddingRight( 5 );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        return table;
    }

    /*
     * 【PDF】空白行の出力(1行目)
     */
    private PdfPTable outputNothing(int height)
    {
        PdfPTable table = new PdfPTable( 1 );

        PdfPCell cell = new PdfPCell( new Phrase( "" ) );
        table.setWidthPercentage( 100f );
        cell.setBorderWidth( 0f );
        cell.setBorderWidthLeft( 0f );
        cell.setBorderWidthBottom( 0f );
        cell.setBorderWidthRight( 0f );
        cell.setBorderWidthTop( 0f );
        cell.setPaddingTop( height );
        table.addCell( cell );
        return table;
    }

    /*
     * CSV出力
     */
    public void createCSV(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        try
        {
            String filename = "credit.csv";
            filename = new String( filename.getBytes( "Shift_JIS" ), "ISO8859_1" );
            filename = ReplaceString.SQLEscape( filename );
            response.setHeader( "Content-Disposition", "attachment;filename=\"" + filename + "\"" );
            response.setContentType( "text/html; charset=Windows-31J" );
            PrintWriter pw_base = response.getWriter();

            // 追記モード
            BufferedWriter bw = new BufferedWriter( pw_base, 10 );

            // 項目をセット
            bw.write( "予約No,氏名,ユーザID,チェックイン,決済日付,料金,手数料,入金額" );
            bw.newLine();
            // 明細出力
            for( int i = 0 ; i < frm.getReserveNoList().size() ; i++ )
            {
                outputCsvRow( bw, i );

            }
            bw.close();
            pw_base.close();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoCreditDetail.createCSV()] Exception:" + e.toString() );
            e.printStackTrace( System.err );
        }
    }

    private void outputCsvRow(BufferedWriter bw, int seq)
    {

        try
        {
            // 予約No
            bw.write( frm.getReserveNoList().get( seq ) + "," );

            // 氏名
            bw.write( frm.getNameList().get( seq ) + "," );

            // ユーザーID
            bw.write( frm.getUserSeqList().get( seq ) + "," );

            // チェックイン
            String str = DateEdit.formatDate( 5, frm.getReserveDateList().get( seq ) );
            if ( frm.getStatusList().get( seq ) == 3 )
            {
                str += "(ｷｬﾝｾﾙ)";
            }
            bw.write( str + "," );

            // 決済日付
            if ( frm.getSalesFlagList().get( seq ) == 1 )
            {
                str = DateEdit.formatDate( 5, frm.getSalesDateList().get( seq ) );
            }
            else if ( frm.getStatusList().get( seq ) != 3 )
            {
                str = "来店未処理";
            }
            if ( frm.getStatusList().get( seq ) == 2 && frm.getDelFlagList().get( seq ) != 0 )
            {
                str += "(ｷｬﾝｾﾙ)";
            }
            bw.write( str + "," );

            // 料金
            bw.write( frm.getAmountList().get( seq ) + "," );

            // 手数料
            if ( frm.getDelFlagList().get( seq ) == 1 )
            {
                bw.write( "0" + "," );
            }
            else
            {
                bw.write( frm.getCreditFeeList().get( seq ) + "," );
            }

            // 入金額
            if ( frm.getDelFlagList().get( seq ) == 1 )
            {
                bw.write( "0" + "," );
            }
            else
            {
                bw.write( frm.getAmountList().get( seq ) - frm.getCreditFeeList().get( seq ) + "," );
            }

            bw.newLine();

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoCreditDetail.outputCsvRow()] Exception:" + e.toString() );
        }
    }

}
