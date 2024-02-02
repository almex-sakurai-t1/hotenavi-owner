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
 * 日別請求明細ビジネスロジック
 */
public class LogicOwnerBkoBillDay implements Serializable
{
    private static final long   serialVersionUID = 7755247690249534136L;
    String                      strTitle         = "ハピホテマイル　日別収支明細";

    private FormOwnerBkoBillDay frm;

    /* フォームオブジェクト */
    public FormOwnerBkoBillDay getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoBillDay frm)
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
        int usageDate = 0;
        int seisanCnt = 0;
        int seisanAmount = 0;
        int seisanSeikyu = 0;
        int seisanPayCnt = 0;
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
        int sumSeisanCnt = 0;
        int sumSeisanPayCnt = 0;
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
        ArrayList<Integer> seisanCntList = new ArrayList<Integer>();
        ArrayList<String> seisanAmountList = new ArrayList<String>();
        ArrayList<String> seisanSeikyuList = new ArrayList<String>();
        ArrayList<Integer> seisanPayCntList = new ArrayList<Integer>();
        ArrayList<String> seisanPayList = new ArrayList<String>();
        ArrayList<Integer> rsvCntList = new ArrayList<Integer>();
        ArrayList<String> rsvAmountList = new ArrayList<String>();
        ArrayList<String> rsvSeikyuList = new ArrayList<String>();
        ArrayList<String> rsvBonusList = new ArrayList<String>();
        ArrayList<Integer> otherCntList = new ArrayList<Integer>();
        ArrayList<String> otherSeikyuList = new ArrayList<String>();
        ArrayList<String> seikyuList = new ArrayList<String>();
        NumberFormat formatCur = NumberFormat.getNumberInstance();

        // 対象の利用日取得
        getUsageDateList();

        // 売掛明細情報取得
        for( int i = 0 ; i < frm.getUsageDateIntList().size() ; i++ )
        {
            usageDate = frm.getUsageDateIntList().get( i );
            seisanCnt = 0;
            seisanAmount = 0;
            seisanSeikyu = 0;
            seisanPayCnt = 0;
            seisanPay = 0;
            rsvCnt = 0;
            rsvAmount = 0;
            rsvSeikyu = 0;
            rsvCntLvj = 0;
            rsvAmountLvj = 0;
            rsvSeikyuLvj = 0;
            otherCnt = 0;
            otherSeikyu = 0;
            seikyu = 0;
            rsvBonus = 0;

            // ■精算
            // 組数取得
            if ( frm.getRsvKind() == 0 || frm.getRsvKind() == 1 )
            {
                seisanCnt = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 4 );
                seisanAmount = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 1 );
                seisanSeikyu = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 3 );
                seisanPayCnt = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_120, 4 );
                seisanPay = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_120, 3 );
            }
            seisanCntList.add( seisanCnt );

            // 金額
            seisanAmountList.add( formatCur.format( seisanAmount ) );

            // 支払（アルメックス側で見たときは収入だが、ホテル側から見たときは支払）
            // seisanSeikyu = seisanSeikyu * -1;
            seisanSeikyuList.add( formatCur.format( seisanSeikyu ) );

            // 収入（アルメックス側で見たときは支払だが、ホテル側から見たときは収入）
            seisanPayCntList.add( seisanPayCnt );
            seisanPay = seisanPay * -1;
            seisanPayList.add( formatCur.format( seisanPay ) );

            // ■予約
            // 【 ハピホテタッチ 】
            // 組数
            rsvCnt = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 4 );

            // 金額
            rsvAmount = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 2 );
            rsvSeikyu = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 3 );

            // 【 ラブイン予約 】
            // 組数
            rsvCntLvj = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 4 );

            // 金額
            rsvAmountLvj = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 2 );
            rsvSeikyuLvj = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 3 );

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
            else if ( frm.getRsvKind() == 2 )
            {
                // ラブイン予約
                rsvCntList.add( rsvCntLvj );
                rsvAmountList.add( formatCur.format( rsvAmountLvj ) );
                rsvSeikyuList.add( formatCur.format( rsvSeikyuLvj ) );
            }

            if ( frm.getRsvKind() == 0 || frm.getRsvKind() == 1 )
            {
                rsvBonus = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_150, 3 );
            }
            rsvBonusList.add( formatCur.format( rsvBonus ) );

            // ■その他
            // 組数
            if ( frm.getRsvKind() == 0 || frm.getRsvKind() == 1 )
            {
                otherCnt = getAccountRecvDetail( usageDate, -1, 4 );
                otherSeikyu = getAccountRecvDetail( usageDate, -1, 3 );
            }
            otherCntList.add( otherCnt );

            // 支払（アルメックス側で見たときは収入だが、ホテル側から見たときは支払）
            otherSeikyu = otherSeikyu * -1;
            otherSeikyuList.add( formatCur.format( otherSeikyu ) );

            // ■収入
            if ( frm.getRsvKind() == 0 )
            {
                seikyu = (seisanSeikyu + rsvSeikyu + rsvSeikyuLvj + otherSeikyu + rsvBonus);
            }
            else if ( frm.getRsvKind() == 1 )
            {
                seikyu = (seisanSeikyu + rsvSeikyu + otherSeikyu + rsvBonus);
            }
            else
            {
                seikyu = (seisanSeikyu + rsvSeikyuLvj + otherSeikyu + rsvBonus);
            }
            seikyuList.add( formatCur.format( seikyu ) );

            // ■合計
            sumSeisanCnt = sumSeisanCnt + seisanCnt;
            sumSeisanAmount = sumSeisanAmount + seisanAmount;
            sumSeisanSeikyu = sumSeisanSeikyu + seisanSeikyu;
            sumSeisanPayCnt = sumSeisanPayCnt + seisanPayCnt;
            sumSeisanPay = sumSeisanPay + seisanPay;

            sumRsvCnt = sumRsvCnt + rsvCnt;
            sumRsvAmount = sumRsvAmount + rsvAmount;
            sumRsvSeikyu = sumRsvSeikyu + rsvSeikyu;
            sumRsvBonus = sumRsvBonus + rsvBonus;

            sumOtherCnt = sumOtherCnt + otherCnt;
            sumOtherSeikyu = sumOtherSeikyu + otherSeikyu;

            sumTotal = sumTotal + seikyu;
        }

        frm.setSeisanCntList( seisanCntList );
        frm.setSeisanAmountList( seisanAmountList );
        frm.setSeisanSeikyuList( seisanSeikyuList );
        frm.setSeisanPayCntList( seisanPayCntList );
        frm.setSeisanPayList( seisanPayList );

        frm.setRsvCntList( rsvCntList );
        frm.setRsvAmountList( rsvAmountList );
        frm.setRsvSeikyuList( rsvSeikyuList );
        frm.setRsvBonusList( rsvBonusList );

        frm.setOtherCntList( otherCntList );
        frm.setOtherSeikyuList( otherSeikyuList );

        frm.setTotalSeikyuList( seikyuList );

        // 画面では、ホテル側から見たときは、支払、収入が逆となるため、＋、－の符号を逆にして表示する
        // ■精算の各合計
        frm.setSumSeisanCnt( sumSeisanCnt );
        frm.setSumSeisanAmount( formatCur.format( sumSeisanAmount ) );

        // 支払合計（アルメックス側で見たときは収入だが、ホテル側から見たときは支払）
        frm.setSumSeisanSeikyu( formatCur.format( sumSeisanSeikyu ) );

        // 収入合計（アルメックス側で見たときは支払だが、ホテル側から見たときは収入）
        frm.setSumSeisanPayCnt( sumSeisanPayCnt );
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
    }

    /**
     * 売掛ヘッダー情報取得
     * 
     * @param なし
     * @return なし
     */
    private void getUsageDateList() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        String usageDate = "";
        ArrayList<String> usageDateList = new ArrayList<String>();
        ArrayList<Integer> usageDateIntList = new ArrayList<Integer>();
        ArrayList<Integer> billDateList = new ArrayList<Integer>();
        int selDate = 0;

        query = query + "SELECT DISTINCT rcv.usage_date, bill.bill_date ";
        query = query + "FROM hh_bko_bill bill ";
        query = query + " INNER JOIN hh_bko_rel_bill_account_recv bdt ON bill.bill_slip_no = bdt.bill_slip_no ";
        query = query + "   LEFT JOIN hh_bko_account_recv rcv ON bdt.accrecv_slip_no = rcv.accrecv_slip_no ";
        query = query + "     LEFT JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no ";
        query = query + " INNER JOIN hh_hotel_newhappie happie ON bill.id = happie.id ";
        query = query + "WHERE bill.bill_date = ? ";
        query = query + "  AND bill.id = ? ";
        query = query + "  AND rcv.usage_date >= happie.bko_date_start ";
        if ( frm.getRsvKind() == 0 || frm.getRsvKind() == 1 )
        {
            query = query + "  AND (detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_110 + " OR ";
            query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_120 + " OR ";
            query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_140 + " OR ";
            if ( frm.getRsvKind() == 0 )
            {
                query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_141 + " OR ";
            }
            query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_150 + " OR ";
            query = query + "       detail.account_title_cd >=  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_200 + " ) ";
        }
        if ( frm.getRsvKind() == 2 )
        {
            query = query + " AND (detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_141 + " )";
        }

        query = query + "  AND rcv.invalid_flag = ? ";
        query = query + "ORDER BY rcv.usage_date";

        selDate = Integer.parseInt( Integer.toString( frm.getSelYear() ) + String.format( "%1$02d", frm.getSelMonth() ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, selDate );
            prestate.setInt( 2, frm.getSelHotelID() );
            prestate.setInt( 3, 0 );
            result = prestate.executeQuery();

            while( result.next() )
            {
                usageDate = result.getString( "usage_date" );
                usageDateIntList.add( result.getInt( "usage_date" ) );
                usageDateList.add( usageDate.substring( 0, 4 ) + "/" + usageDate.substring( 4, 6 ) + "/" + usageDate.substring( 6 ) );
                billDateList.add( result.getInt( "bill_date" ) );
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "日別収支明細" ) );
                return;
            }

            frm.setUsageDateIntList( usageDateIntList );
            frm.setUsageDateStrList( usageDateList );
            frm.setBillDateList( billDateList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillDay.getUsageDateList] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 売掛明細情報取得
     * 
     * @param int usageDate 利用日
     * @param int accTitleCd 科目コード
     *        (その他の場合、-1を設定する)
     * @param int selKbn (1:利用金額取得、2：予約金額取得、3:明細の金額, 4:件数)
     * @return int 金額、または件数
     */
    private int getAccountRecvDetail(int usageDate, int accTitleCd, int selKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int ret = 0;
        int selDate = 0;

        query = query + "SELECT ";
        query = query + "COUNT(*) as CNT, SUM(detail.amount) AS amount, SUM(rcv.usage_charge) AS usgCharge, SUM(rcv.receive_charge) AS rcvCharge ";
        query = query + "FROM hh_bko_bill bill ";
        query = query + " INNER JOIN hh_bko_rel_bill_account_recv bdt ON bill.bill_slip_no = bdt.bill_slip_no ";
        query = query + "   LEFT JOIN hh_bko_account_recv rcv ON bdt.accrecv_slip_no = rcv.accrecv_slip_no ";
        query = query + "     LEFT JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no ";
        query = query + " INNER JOIN hh_hotel_newhappie happie ON bill.id = happie.id ";
        query = query + "WHERE bill.id = ? ";
        query = query + "  AND rcv.usage_date = ? ";
        query = query + "  AND bill.bill_date = ? ";
        query = query + "  AND rcv.invalid_flag = ? ";
        query = query + "  AND rcv.usage_date >= happie.bko_date_start ";

        if ( accTitleCd != -1 )
        {
            query = query + "  AND detail.account_title_cd = ? ";
        }
        else
        {
            query = query + "  AND (detail.account_title_cd >= " + OwnerBkoCommon.ACCOUNT_TITLE_CD_200 + ")";
        }

        selDate = Integer.parseInt( Integer.toString( frm.getSelYear() ) + String.format( "%1$02d", frm.getSelMonth() ) );
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, usageDate );
            prestate.setInt( 3, selDate );
            prestate.setInt( 4, 0 );
            if ( accTitleCd != -1 )
            {
                prestate.setInt( 5, accTitleCd );
            }
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( selKbn == 1 )
                {
                    ret = result.getInt( "usgCharge" );

                }
                else if ( selKbn == 2 )
                {
                    ret = result.getInt( "rcvCharge" );

                }
                else if ( selKbn == 3 )
                {
                    ret = result.getInt( "amount" );
                }
                else if ( selKbn == 4 )
                {
                    ret = result.getInt( "CNT" );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillDay.getAccountRecvDetail] Exception=" + e.toString() );
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
            for( int i = 0 ; i < frm.getUsageDateIntList().size() ; i++ )
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
            // doc.add( outputTotal() );

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
        cell.setPhrase( new Phrase( "日付:" + frm.getSelYear() + "年" + frm.getSelMonth() + "月", fontM10 ) );
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
        Table table = new Table( 12 );
        table.setWidth( 100 ); // 全体の幅
        table.setWidths( new int[]{ 12, 6, 10, 9, 6, 10, 9, 8, 8, 9, 5, 8 } ); // 列幅(%)
        table.setDefaultHorizontalAlignment( Element.ALIGN_CENTER ); // 表示位置（横）
        table.setDefaultVerticalAlignment( Element.ALIGN_MIDDLE ); // 表示位置（縦）
        table.setPadding( 1 ); // 余白
        table.setSpacing( 0 ); // セル間の間隔
        table.setBorderColor( new Color( 0, 0, 0 ) ); // 線の色

        // 1行目
        Cell cell = new Cell( new Phrase( "日付", fontM10 ) );
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
        cell.setColspan( 2 );
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

        cell = new Cell( new Phrase( "マイル使用", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 2 );
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

        cell = new Cell( new Phrase( "件数", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "マイル", fontM10 ) );
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
        Font fontM10ST = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10, Font.STRIKETHRU );
        // 明細の出力
        PdfPTable table = new PdfPTable( 12 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 12, 6, 10, 9, 6, 10, 9, 8, 8, 9, 5, 8 } ); // 列幅(%)

        // 日付
        PdfPCell cell = new PdfPCell( new Phrase( frm.getUsageDateStrList().get( i ), fontM10 ) );
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
        cell = new PdfPCell( new Phrase( frm.getTotalSeikyuList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // 件数
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getSeisanPayCntList().get( i ) ), fontM10 ) );
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
        PdfPTable table = new PdfPTable( 12 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 12, 6, 10, 9, 6, 10, 9, 8, 8, 9, 5, 8 } ); // 列幅(%)

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
        cell = new PdfPCell( new Phrase( frm.getSumTotalSeikyu(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // 件数
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getSumSeisanPayCnt() ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // マイル
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
            String filename = "billday.csv";
            filename = new String( filename.getBytes( "Shift_JIS" ), "ISO8859_1" );
            filename = ReplaceString.SQLEscape( filename );
            response.setHeader( "Content-Disposition", "attachment;filename=\"" + filename + "\"" );
            response.setContentType( "text/html; charset=Windows-31J" );
            PrintWriter pw_base = response.getWriter();

            // 追記モード
            BufferedWriter bw = new BufferedWriter( pw_base, 10 );

            // 項目をセット
            bw.write( "日付,支出・タッチ・組数,支出・タッチ・金額,支出・タッチ・手数料,支出・予約・組数,支出・予約・金額,支出・予約・手数料,支出・予約・予約ボーナス,支出・その他,支出・合計,収入・マイル使用・件数,収入・マイル使用・マイル" );

            bw.newLine();
            // 明細出力
            for( int i = 0 ; i < frm.getUsageDateIntList().size() ; i++ )
            {
                outputCsvRow( bw, i );

            }
            bw.close();
            pw_base.close();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillDay.createCSV()] Exception:" + e.toString() );
            e.printStackTrace( System.err );
        }
    }

    private void outputCsvRow(BufferedWriter bw, int seq)
    {

        try
        {
            String result = "";

            // 日付
            bw.write( frm.getUsageDateStrList().get( seq ) + "," );

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
            result = frm.getTotalSeikyuList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // 収入・マイル使用・件数
            bw.write( frm.getSeisanPayCntList().get( seq ) + "," );

            // 収入・マイル使用・マイル
            result = frm.getSeisanPayList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            bw.newLine();

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillDay.outputCsvRow()] Exception:" + e.toString() );
        }
    }
}
