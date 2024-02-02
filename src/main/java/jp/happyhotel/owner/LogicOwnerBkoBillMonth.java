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
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Kanma;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerBkoCommon;
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
 * 月別請求明細ビジネスロジック
 */
public class LogicOwnerBkoBillMonth implements Serializable
{

    private static final long     serialVersionUID = -6778531648260740904L;
    String                        strTitle         = "ハピホテマイル　月別収支明細";

    private FormOwnerBkoBillMonth frm;

    /* フォームオブジェクト */
    public FormOwnerBkoBillMonth getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoBillMonth frm)
    {
        this.frm = frm;
    }

    /**
     * 請求情報取得
     * 
     * @param なし
     * @return なし
     */
    public void getAccountRecv() throws Exception
    {
        int billDate = 0;
        int seisanCnt = 0;
        int seisanAmount = 0;
        int seisanSeikyu = 0;
        int seisanPay = 0;
        int rsvCnt = 0;
        int rsvAmount = 0;
        int rsvSeikyu = 0;
        int rsvCntLvj = 0;
        int rsvAmountLvj = 0;
        int rsvSeikyuLvj = 0;
        int rsvBonus = 0;
        int otherCnt = 0;
        int otherSeikyu = 0;
        int seikyu = 0;
        int seikyuIncome = 0;
        int seikyuPay = 0;
        int sumSeisanCnt = 0;
        int sumSeisanAmount = 0;
        int sumSeisanSeikyu = 0;
        int sumSeisanPay = 0;
        int sumRsvCnt = 0;
        int sumRsvAmount = 0;
        int sumRsvSeikyu = 0;
        int sumRsvBonus = 0;
        int sumOtherCnt = 0;
        int sumOtherSeikyu = 0;
        int sumTotal = 0;
        int sumTotalIncome = 0;
        int inputCharge = 0;
        int inputDate = 0;
        int seikyuMonth = 0;
        int tax;
        ArrayList<Integer> seisanCntList = new ArrayList<Integer>();
        ArrayList<String> seisanAmountList = new ArrayList<String>();
        ArrayList<String> seisanSeikyuList = new ArrayList<String>();
        ArrayList<String> seisanPayList = new ArrayList<String>();
        ArrayList<Integer> rsvCntList = new ArrayList<Integer>();
        ArrayList<String> rsvAmountList = new ArrayList<String>();
        ArrayList<String> rsvSeikyuList = new ArrayList<String>();
        ArrayList<String> rsvBonusList = new ArrayList<String>();
        ArrayList<Integer> otherCntList = new ArrayList<Integer>();
        ArrayList<String> otherSeikyuList = new ArrayList<String>();
        ArrayList<String> seikyuList = new ArrayList<String>();
        ArrayList<String> seikyuMonthList = new ArrayList<String>();
        ArrayList<String> inputChargeList = new ArrayList<String>();
        ArrayList<String> inputDateList = new ArrayList<String>();

        ArrayList<String> seikyuIncomeList = new ArrayList<String>();

        ArrayList<String> seikyuPayList = new ArrayList<String>();

        NumberFormat formatCur = NumberFormat.getNumberInstance();

        // 対象の年月取得
        getBillDateList();

        // 売掛明細情報取得
        for( int i = 0 ; i < frm.getBillDateIntList().size() ; i++ )
        {
            billDate = frm.getBillDateIntList().get( i );
            seisanCnt = 0;
            seisanAmount = 0;
            seisanSeikyu = 0;
            seisanPay = 0;
            rsvCnt = 0;
            rsvAmount = 0;
            rsvSeikyu = 0;
            rsvCntLvj = 0;
            rsvAmountLvj = 0;
            rsvSeikyuLvj = 0;
            seikyuMonth = 0;
            otherCnt = 0;
            otherSeikyu = 0;
            seikyu = 0;
            tax = 0;
            rsvBonus = 0;
            seikyuIncome = 0;
            seikyuPay = 0;
            inputCharge = 0;
            inputDate = 0;

            // hh_bko_closing_controlから対象月の消費税率を取得する
            tax = OwnerBkoCommon.getTax( billDate );

            // ■精算
            // 組数取得
            if ( frm.getRsvKind() == 0 || frm.getRsvKind() == 1 )
            {
                seisanCnt = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 4 );
                seisanAmount = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 1 );
                seisanSeikyu = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 3 );
                seisanPay = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_120, 3 );
            }
            seisanCntList.add( seisanCnt );

            // 金額
            seisanAmountList.add( formatCur.format( seisanAmount ) );

            // 画面では、ホテル側から見たときは、支払、収入が逆となるため、＋、－の符号を逆にして表示する
            // 支払（アルメックス側で見たときは収入だが、ホテル側から見たときは支払）
            seisanSeikyu = OwnerBkoCommon.reCalctTax( seisanSeikyu, tax );
            // seisanSeikyu = seisanSeikyu * -1;
            seisanSeikyuList.add( formatCur.format( seisanSeikyu ) );

            // 収入（アルメックス側で見たときは支払だが、ホテル側から見たときは収入）
            seisanPay = seisanPay * -1;
            seisanPayList.add( formatCur.format( seisanPay ) );

            // ■予約
            // 【 ハピホテタッチ 】
            // 組数
            rsvCnt = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 4 );

            // 金額
            rsvAmount = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 2 );

            // 支払（アルメックス側で見たときは収入だが、ホテル側から見たときは支払）
            rsvSeikyu = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 3 );
            rsvSeikyu = OwnerBkoCommon.reCalctTax( rsvSeikyu, tax );

            // 【 ラブイン予約 】
            // 組数
            rsvCntLvj = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 4 );

            // 金額
            rsvAmountLvj = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 2 );

            // 支払（アルメックス側で見たときは収入だが、ホテル側から見たときは支払）
            rsvSeikyuLvj = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 3 );
            rsvSeikyuLvj = OwnerBkoCommon.reCalctTax( rsvSeikyuLvj, tax );

            if ( frm.getRsvKind() == 0 )
            {
                // 全て
                rsvCntList.add( rsvCnt + rsvCntLvj );
                rsvAmountList.add( formatCur.format( rsvAmount + rsvAmountLvj ) );
                rsvSeikyuList.add( formatCur.format( rsvSeikyu + rsvSeikyuLvj ) );
            }
            else if ( frm.getRsvKind() == 1 )
            {
                // ハピホテタッチ
                rsvCntList.add( rsvCnt );
                rsvAmountList.add( formatCur.format( rsvAmount ) );
                rsvSeikyuList.add( formatCur.format( rsvSeikyu ) );
            }
            else
            {
                // ラブイン予約
                rsvCntList.add( rsvCntLvj );
                rsvAmountList.add( formatCur.format( rsvAmountLvj ) );
                rsvSeikyuList.add( formatCur.format( rsvSeikyuLvj ) );
            }

            if ( frm.getRsvKind() == 0 || frm.getRsvKind() == 1 )
            {
                rsvBonus = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_150, 3 );
            }
            rsvBonus = OwnerBkoCommon.reCalctTax( rsvBonus, tax );
            // rsvSeikyu = rsvSeikyu * -1;
            rsvBonusList.add( formatCur.format( rsvBonus ) );

            // 月ごとの請求金額（アルメックス側で見たときは収入だが、ホテル側から見たときは支払）
            seikyuMonth = getAmountSeikyu( billDate );
            // seikyuMonth = seikyuMonth * -1;
            if ( seikyuMonth != 0 )
            {
                seikyuMonthList.add( formatCur.format( seikyuMonth ) );
            }
            else
            {
                seikyuMonthList.add( "繰越" );
            }

            // ■その他
            // 組数
            if ( frm.getRsvKind() == 0 || frm.getRsvKind() == 1 )
            {
                otherCnt = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, -1, 4 );
                otherSeikyu = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, -1, 3 );
            }
            otherCntList.add( otherCnt );

            // 支払（アルメックス側で見たときは収入だが、ホテル側から見たときは支払）
            otherSeikyu = OwnerBkoCommon.reCalctTax( otherSeikyu, tax );
            // otherSeikyu = otherSeikyu * -1;
            otherSeikyuList.add( formatCur.format( otherSeikyu ) );

            // ■ご収支
            if ( frm.getRsvKind() == 0 )
            {
                // 全て
                seikyu = (seisanSeikyu + rsvSeikyu + rsvSeikyuLvj + otherSeikyu + rsvBonus) + (seisanPay);
            }
            else if ( frm.getRsvKind() == 1 )
            {
                // ハピホテタッチ
                seikyu = (seisanSeikyu + rsvSeikyu + otherSeikyu + rsvBonus) + (seisanPay);
            }
            else
            {
                // ラブイン予約
                seikyu = (seisanSeikyu + rsvSeikyuLvj + otherSeikyu + rsvBonus) + (seisanPay);
            }

            seikyuList.add( formatCur.format( seikyu ) );

            // ■ご収支（収入 ホテルからみたら支出）
            if ( frm.getRsvKind() == 0 )
            {
                // 全て
                seikyuIncome = (seisanSeikyu + rsvSeikyu + rsvSeikyuLvj + rsvBonus + otherSeikyu);
            }
            else if ( frm.getRsvKind() == 1 )
            {
                // ハピホテタッチ
                seikyuIncome = (seisanSeikyu + rsvSeikyu + rsvBonus + otherSeikyu);
            }
            else
            {
                // ラブイン予約
                seikyuIncome = (seisanSeikyu + rsvSeikyuLvj + rsvBonus + otherSeikyu);
            }

            seikyuIncomeList.add( formatCur.format( seikyuIncome ) );

            // ■ご収支（支出 ホテルからみた収入）
            seikyuPay = (seisanPay);
            seikyuPayList.add( formatCur.format( seikyuPay ) );

            // ■入金金額取得
            inputCharge = getInputChargeByMonth( billDate, 0 );
            inputCharge = inputCharge * -1;
            inputChargeList.add( formatCur.format( inputCharge ) );

            // ■入金日
            inputDate = getInputChargeDateByMonth( billDate, 0 );
            if ( inputDate > 0 )
            {
                inputDateList.add( String.format( "%1$04d/%2$02d/%3$02d", inputDate / 10000, inputDate % 10000 / 100, inputDate % 10000 % 100 ) );
            }
            else
            {
                inputDateList.add( "-" );
            }

            // ■合計
            sumSeisanCnt = sumSeisanCnt + seisanCnt;
            sumSeisanAmount = sumSeisanAmount + seisanAmount;
            sumSeisanSeikyu = sumSeisanSeikyu + seisanSeikyu;
            sumSeisanPay = sumSeisanPay + seisanPay;

            if ( frm.getRsvKind() == 0 )
            {
                // 全て
                sumRsvCnt = sumRsvCnt + rsvCnt + rsvCntLvj;
                sumRsvAmount = sumRsvAmount + rsvAmount + rsvAmountLvj;
                sumRsvSeikyu = sumRsvSeikyu + rsvSeikyu + rsvSeikyuLvj;
            }
            else if ( frm.getRsvKind() == 1 )
            {
                // ハピホテタッチ
                sumRsvCnt = sumRsvCnt + rsvCnt;
                sumRsvAmount = sumRsvAmount + rsvAmount;
                sumRsvSeikyu = sumRsvSeikyu + rsvSeikyu;
            }
            else
            {
                // ラブイン予約
                sumRsvCnt = sumRsvCnt + rsvCntLvj;
                sumRsvAmount = sumRsvAmount + rsvAmountLvj;
                sumRsvSeikyu = sumRsvSeikyu + rsvSeikyuLvj;
            }

            sumRsvBonus = sumRsvBonus + rsvBonus;

            sumOtherCnt = sumOtherCnt + otherCnt;
            sumOtherSeikyu = sumOtherSeikyu + otherSeikyu;

            sumTotal = sumTotal + seikyu;
            sumTotalIncome = sumTotalIncome + seikyuIncome;

        }

        frm.setSeisanCntList( seisanCntList );
        frm.setSeisanAmountList( seisanAmountList );
        frm.setSeisanSeikyuList( seisanSeikyuList );
        frm.setSeisanPayList( seisanPayList );

        frm.setRsvCntList( rsvCntList );
        frm.setRsvAmountList( rsvAmountList );
        frm.setRsvSeikyuList( rsvSeikyuList );
        frm.setRsvBonusList( rsvBonusList );

        frm.setOtherCntList( otherCntList );
        frm.setOtherSeikyuList( otherSeikyuList );

        frm.setTotalSeikyuList( seikyuList );
        frm.setSeikyuList( seikyuMonthList );
        frm.setInputChargeList( inputChargeList );
        frm.setInputDateList( inputDateList );

        frm.setTotalSeikyuIncomeList( seikyuIncomeList );

        // 画面では、ホテル側から見たときは、支払、収入が逆となるため、＋、－の符号を逆にして表示する
        // ■精算の各合計
        frm.setSumSeisanCnt( sumSeisanCnt );
        frm.setSumSeisanAmount( formatCur.format( sumSeisanAmount ) );

        // 支払合計（アルメックス側で見たときは収入だが、ホテル側から見たときは支払）
        frm.setSumSeisanSeikyu( formatCur.format( sumSeisanSeikyu ) );

        // 収入合計（アルメックス側で見たときは支払だが、ホテル側から見たときは収入）
        frm.setSumSeisanPay( formatCur.format( sumSeisanPay ) );

        // ■予約の各合計
        frm.setSumRsvCnt( sumRsvCnt );
        frm.setSumRsvAmount( formatCur.format( sumRsvAmount ) );
        frm.setSumRsvSeikyu( formatCur.format( sumRsvSeikyu ) );
        frm.setSumRsvBonus( formatCur.format( sumRsvBonus ) );

        // ■その他の各合計
        frm.setSumOtherCnt( sumOtherCnt );

        // 支払合計（アルメックス側で見たときは収入だが、ホテル側から見たときは支払）
        frm.setSumOtherSeikyu( formatCur.format( sumOtherSeikyu ) );

        // ■ご収支の合計
        frm.setSumTotalSeikyu( formatCur.format( sumTotal ) );
        frm.setSumTotalSeikyuIncome( formatCur.format( sumTotalIncome ) );

    }

    /**
     * 指定月入力金額取得処理
     * 
     * @param todate 指定年月
     * @return 入力金額合計額
     */
    private int getInputChargeByMonth(int toMonth, int kind)
    {
        int ret = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = "SELECT deposit_amount FROM hh_bko_deposit bko" +
                    " INNER JOIN hh_hotel_newhappie happie ON bko.id = happie.id AND bko.bill_issue_date >= happie.bko_date_start " +
                    " WHERE bko.id = ? " +
                    " AND bko.bill_issue_date > ? " +
                    " AND bko.bill_issue_date < ?";
            if ( kind != 0 )
                query += " AND bko.kind =" + kind;

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, toMonth * 100 );
            prestate.setInt( 3, (toMonth + 1) * 100 );
            result = prestate.executeQuery();
            while( result.next() != false )
            {
                ret += result.getInt( "deposit_amount" );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillMonth.getInputChargeByMonth] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 入金日取得処理
     * 
     * @param todate 指定年月日
     * @param kind 1: 入金，2:支払い
     * @return 入金日
     */
    private int getInputChargeDateByMonth(int toMonth, int kind)
    {
        int ret = 0;

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = "SELECT deposit_date FROM hh_bko_deposit bko" +
                    " INNER JOIN hh_hotel_newhappie happie ON bko.id = happie.id AND bko.bill_issue_date >= happie.bko_date_start " +
                    " WHERE bko.id = ? " +
                    " AND bko.bill_issue_date > ? " +
                    " AND bko.bill_issue_date < ?";
            if ( kind != 0 )
            {
                query += " AND bko.kind =" + kind;
            }
            query += " ORDER BY deposit_date DESC";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, toMonth * 100 );
            prestate.setInt( 3, (toMonth + 1) * 100 );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                ret = result.getInt( "deposit_date" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillMonth.getInputChargeDateByMonth] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 対象年月取得(0の月も表示するように変更)
     * 
     * @param なし
     * @return なし
     */
    private void getBillDateList() throws Exception
    {
        final int NEXT_MONTH = 1;
        int count = 0;
        ArrayList<String> billDateStrList = new ArrayList<String>();
        ArrayList<Integer> billDateList = new ArrayList<Integer>();
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
            billDateList.add( i );
            billDateStrList.add( Integer.toString( i ).substring( 0, 4 ) + "/" + Integer.toString( i ).substring( 4, 6 ) );
            i = DateEdit.addMonth( i * 100 + 01, NEXT_MONTH ) / 100;
            count++;
        }

        try
        {
            // 該当データがない場合
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "月別収支明細" ) );
                return;
            }

            frm.setBillDateIntList( billDateList );
            frm.setBillDateStrList( billDateStrList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillMonth.getBillDateList] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
        }
    }

    /**
     * 指定請求月請求金額取得
     * 
     * @param int billDate 請求年月
     * @return int 金額
     */
    private int getAmountSeikyu(int billDate) throws Exception
    {
        int ret = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT ";
        query = query + "COUNT(*) as CNT, SUM(detail.amount) AS amount, SUM(rcv.usage_charge) AS usgCharge, SUM(rcv.receive_charge) AS rcvCharge ";
        query = query + "FROM hh_bko_bill bill ";
        query = query + " INNER JOIN hh_bko_rel_bill_account_recv bdt ON bill.bill_slip_no = bdt.bill_slip_no ";
        query = query + "   LEFT JOIN hh_bko_account_recv rcv ON bdt.accrecv_slip_no = rcv.accrecv_slip_no ";
        query = query + "     LEFT JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no ";
        query = query + " INNER JOIN hh_hotel_newhappie happie ON bill.id = happie.id AND bill.bill_issue_date >= happie.bko_date_start ";
        query = query + "WHERE bill.id = ? ";
        query = query + "  AND bill.bill_issue_date > ? AND bill.bill_issue_date < ?";
        query = query + "  AND rcv.invalid_flag = ? ";
        query = query + " GROUP BY bill.bill_date ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, billDate * 100 );
            prestate.setInt( 3, (billDate + 1) * 100 );
            prestate.setInt( 4, 0 );
            result = prestate.executeQuery();
            // 請求発行年月日が指定の範囲内の金額を合計する
            while( result.next() )
            {
                ret += result.getInt( "amount" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillMonth.getAmountSeikyu] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        Logging.info( "LogicOwnerBkoBillMonth doPost()", "LogicOwnerBkoBillMonth" );
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
            for( int i = 0 ; i < frm.getBillDateIntList().size() ; i++ )
            {
                count++;
                if ( count > 23 ) // 改ページ
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
        cell.setPhrase( new Phrase( "日付:" + frm.getSelYearFrom() + "年" + frm.getSelMonthFrom() + "月～" + frm.getSelYearTo() + "年" + frm.getSelMonthTo() + "月", fontM10 ) );
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
        Table table = new Table( 11 );
        table.setWidth( 100 ); // 全体の幅
        table.setWidths( new int[]{ 10, 6, 12, 9, 6, 12, 9, 9, 9, 9, 9 } ); // 列幅(%)
        table.setDefaultHorizontalAlignment( Element.ALIGN_CENTER ); // 表示位置（横）
        table.setDefaultVerticalAlignment( Element.ALIGN_MIDDLE ); // 表示位置（縦）
        table.setPadding( 1 ); // 余白
        table.setSpacing( 0 ); // セル間の間隔
        table.setBorderColor( new Color( 0, 0, 0 ) ); // 線の色

        // 1行目
        Cell cell = new Cell( new Phrase( "年月", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 3 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "支出", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 9 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "収入", fontM10 ) );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        table.addCell( cell );

        // 2行目

        cell = new Cell( new Phrase( "ハピホテタッチ", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 3 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "ハピホテ予約", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 4 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "その他", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 2 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "合計", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 2 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "マイル\r\n使用", fontM10 ) );
        cell.setRowspan( 2 );
        cell.setColspan( 1 );
        table.addCell( cell );

        // 3行目

        cell = new Cell( new Phrase( "組数", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "金額", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "手数料", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "組数", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "金額", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "手数料", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "予約ボ\r\nーナス", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        return table;
    }

    /*
     * 【PDF】明細
     */
    private PdfPTable outputDetail(int i) throws DocumentException, IOException
    {

        Font fontM10 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10 );
        // 明細の出力
        PdfPTable table = new PdfPTable( 11 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 10, 6, 12, 9, 6, 12, 9, 9, 9, 9, 9 } ); // 列幅(%)

        // 年月
        PdfPCell cell = new PdfPCell( new Phrase( frm.getBillDateStrList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_CENTER );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthLeft( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setMinimumHeight( 25f );
        table.addCell( cell );

        // 組数
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getSeisanCntList().get( i ) ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // 金額
        cell = new PdfPCell( new Phrase( frm.getSeisanAmountList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setPaddingRight( 5 );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // 手数料
        cell = new PdfPCell( new Phrase( frm.getSeisanSeikyuList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // 組数
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getRsvCntList().get( i ) ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // 金額
        cell = new PdfPCell( new Phrase( frm.getRsvAmountList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // 手数料
        cell = new PdfPCell( new Phrase( frm.getRsvSeikyuList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // 予約ボーナス
        cell = new PdfPCell( new Phrase( frm.getRsvBonusList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // その他
        cell = new PdfPCell( new Phrase( frm.getOtherSeikyuList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // 合計
        cell = new PdfPCell( new Phrase( frm.getTotalSeikyuIncomeList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // マイル使用
        cell = new PdfPCell( new Phrase( frm.getSeisanPayList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
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
        PdfPTable table = new PdfPTable( 11 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 10, 6, 12, 9, 6, 12, 9, 9, 9, 9, 9 } ); // 列幅(%)

        // 合計
        PdfPCell cell = new PdfPCell( new Phrase( "", fontM10 ) );
        cell = new PdfPCell( new Phrase( "合　計", fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_CENTER );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthLeft( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setMinimumHeight( 25f );
        table.addCell( cell );

        // 組数
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getSumSeisanCnt() ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // 金額
        cell = new PdfPCell( new Phrase( frm.getSumSeisanAmount(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // 手数料
        cell = new PdfPCell( new Phrase( frm.getSumSeisanSeikyu(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // 組数
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getSumRsvCnt() ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // 金額
        cell = new PdfPCell( new Phrase( frm.getSumRsvAmount(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // 手数料
        cell = new PdfPCell( new Phrase( frm.getSumRsvSeikyu(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // 予約ボーナス
        cell = new PdfPCell( new Phrase( frm.getSumRsvBonus(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // その他
        cell = new PdfPCell( new Phrase( frm.getSumOtherSeikyu(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // 合計
        cell = new PdfPCell( new Phrase( frm.getSumTotalSeikyuIncome(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // マイル使用
        cell = new PdfPCell( new Phrase( frm.getSumSeisanPay(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
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
            String filename = "billmonth.csv";
            filename = new String( filename.getBytes( "Shift_JIS" ), "ISO8859_1" );
            filename = ReplaceString.SQLEscape( filename );
            response.setHeader( "Content-Disposition", "attachment;filename=\"" + filename + "\"" );
            response.setContentType( "text/html; charset=Windows-31J" );
            PrintWriter pw_base = response.getWriter();

            // 追記モード
            BufferedWriter bw = new BufferedWriter( pw_base, 10 );

            // 項目をセット
            bw.write( "年月,支出・タッチ・組数,支出・タッチ・金額,支出・タッチ・手数料,支出・予約・組数,支出・予約・金額,支出・予約・手数料,支出・予約・予約ボーナス,支出・その他,支出・合計,収入・マイル使用" );

            bw.newLine();
            // 明細出力
            for( int i = 0 ; i < frm.getBillDateIntList().size() ; i++ )
            {
                outputCsvRow( bw, i );

            }
            bw.close();
            pw_base.close();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillMonth.createCSV()] Exception:" + e.toString() );
            e.printStackTrace( System.err );
        }
    }

    private void outputCsvRow(BufferedWriter bw, int seq)
    {

        try
        {
            String result = "";

            // 年月
            bw.write( frm.getBillDateStrList().get( seq ) + "," );

            // 支出・タッチ・組数
            bw.write( frm.getSeisanCntList().get( seq ) + "," );

            // 支出・タッチ・金額
            result = frm.getSeisanAmountList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // 支出・タッチ・手数料
            result = frm.getSeisanSeikyuList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // 支出・予約・組数
            bw.write( frm.getRsvCntList().get( seq ) + "," );

            // 支出・予約・金額
            result = frm.getRsvAmountList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // 支出・予約・手数料
            result = frm.getRsvSeikyuList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // 支出・予約・予約ボーナス
            result = frm.getRsvBonusList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // 支出・その他
            result = frm.getOtherSeikyuList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // 支出・合計
            result = frm.getTotalSeikyuIncomeList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // 収入・マイル使用
            result = frm.getSeisanPayList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            bw.newLine();

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillMonth.outputCsvRow()] Exception:" + e.toString() );
        }
    }
}
