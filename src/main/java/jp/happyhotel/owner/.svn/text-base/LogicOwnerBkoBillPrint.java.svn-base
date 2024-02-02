package jp.happyhotel.owner;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataBkoBill;
import jp.happyhotel.data.DataBkoCompany;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 請求書発行ビジネスロジック
 */
public class LogicOwnerBkoBillPrint extends HttpServlet
{
    /**
     *
     */
    private static final long     serialVersionUID = 5825603280645593157L;

    private FormOwnerBkoBillPrint frm;
    private DataBkoBill           dataBill         = new DataBkoBill();
    private static final String   JPEG_FILE        = "/happyhotel/secure/owner/images/bko/seal_impression.jpeg";

    /* フォームオブジェクト */
    public FormOwnerBkoBillPrint getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoBillPrint frm)
    {
        this.frm = frm;
    }

    public boolean getData()
    {
        // ここでデータあるかないか判断

        int paraBillDate = frm.getBillYear() * 100 + frm.getBillMonth();

        // 請求データ取得
        if ( !dataBill.getData( frm.getSelHotelID(), paraBillDate ) )
        {
            return false;
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {

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
            // PdfWriter pdfwriter = PdfWriter.getInstance( doc, byteout );
            PdfWriter.getInstance( doc, byteout );

            // ヘッダー
            Font fontHeader = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED ), 15, Font.BOLD );
            // 明細
            Font fontM07 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 7 );
            Font fontM10 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10 );
            Font fontM10U = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10, Font.UNDERLINE );
            Font fontM10B = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10, Font.BOLD );
            Font fontM15UB = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 15, Font.BOLD | Font.UNDERLINE );

            // サブタイトル１
            // Font fontSubTitle1 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED ), 11, Font.UNDERLINE );
            // サブタイトル２
            Font fontSubTitle2 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED ), 11 );
            fontSubTitle2.setColor( new Color( 255, 0, 0 ) );

            // 空白セル用フォント(非表示)
            Font font_empty = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED ), 9 );
            font_empty.setColor( new Color( 255, 255, 255 ) );

            // 明細行　空文字
            PdfPCell cellTblDetailBrank = new PdfPCell( new Phrase( "　", fontM07 ) );
            cellTblDetailBrank.setBorderWidth( 1f );

            // １ページ目明細行数
            int PAGE1_ROW_COUNT = 11;
            // ２ページ目以降明細行数
            int PAGE2_ROW_COUNT = 40;
            // 次ページに続く
            PdfPCell cellNextPage = new PdfPCell( new Phrase( "次ページに続く", fontM10 ) );
            cellNextPage.setHorizontalAlignment( Element.ALIGN_LEFT );
            cellNextPage.setBorderWidth( 1f );

            // TODO
            doc.addAuthor( "株式会社アルメックス" );
            doc.addSubject( "御請求書" );

            // フッター出力
            HeaderFooter footer = new HeaderFooter( new Phrase( "-" ), new Phrase( "-" ) );
            footer.setAlignment( Element.ALIGN_CENTER );
            footer.setBorder( Rectangle.NO_BORDER );
            doc.setFooter( footer );

            doc.open();

            // No.
            Paragraph paraNo = new Paragraph( "No. " + dataBill.getBillNo(), fontM10U );
            paraNo.setAlignment( Element.ALIGN_RIGHT );

            doc.add( paraNo );

            // 発行
            // Locale.setDefault( new Locale( "ja", "JP", "JP" ) );
            Calendar cal = Calendar.getInstance();
            cal.set( Calendar.YEAR, (frm.getIssueYear()) );
            cal.set( Calendar.MONTH, (frm.getIssueMonth() - 1) );
            cal.set( Calendar.DATE, (frm.getIssueDay()) );
            SimpleDateFormat format = new SimpleDateFormat( "yyyy年M月d日" );
            Paragraph paraIssueDate = new Paragraph( format.format( cal.getTime() ), fontM10 );
            paraIssueDate.setAlignment( Element.ALIGN_RIGHT );
            doc.add( paraIssueDate );

            doc.add( new Paragraph( "　" ) );
            doc.add( new Paragraph( "　" ) );

            // ホテル名テーブル
            PdfPTable custTbl = new PdfPTable( 5 );
            custTbl.setWidthPercentage( 100f );

            int table_width[] = { 2, 38, 17, 33, 15 };
            custTbl.setWidths( table_width );

            PdfPCell cellNothing = new PdfPCell( new Phrase( "", fontM10B ) );
            cellNothing.setBorderWidthRight( 0 );
            cellNothing.setBorderWidthRight( 0 );
            cellNothing.setBorderWidth( 0f );
            cellNothing.setBorderWidthLeft( 0f );
            cellNothing.setBorderWidthBottom( 0f );
            cellNothing.setBorderWidthRight( 0f );
            cellNothing.setBorderWidthTop( 0f );

            // 1列目　空
            custTbl.addCell( cellNothing );

            // 請求先住所
            String zipCode = "";
            if ( dataBill.getBillZipCode().length() >= 3 )
            {
                zipCode = dataBill.getBillZipCode().substring( 0, 3 ) + "-" + dataBill.getBillZipCode().substring( 3, dataBill.getBillZipCode().length() );
            }
            PdfPCell cellCustAddr = new PdfPCell( new Phrase( "〒" + zipCode + "\n" + dataBill.getBillAddress1() + dataBill.getBillAddress2() + dataBill.getBillAddress3() + "\n" + dataBill.getBillName() + " 御中", fontM10B ) );
            cellCustAddr.setHorizontalAlignment( Element.ALIGN_LEFT );
            cellCustAddr.setBorderWidthRight( 0 );
            cellCustAddr.setBorderWidth( 0f );
            cellCustAddr.setBorderWidthLeft( 0f );
            cellCustAddr.setBorderWidthBottom( 0f );
            cellCustAddr.setBorderWidthRight( 0f );
            cellCustAddr.setBorderWidthTop( 0f );
            custTbl.addCell( cellCustAddr );

            // 3列目　空
            custTbl.addCell( cellNothing );

            // 自社マスタ取得
            DataBkoCompany dataCompany = new DataBkoCompany();
            dataCompany.getData( 1 );

            String strCompany = "\n\n\n";

            // 請求元名称１
            strCompany = strCompany + getStr( dataCompany.getCompanyName() ) + "\n";

            // 請求元名称２
            // strCompany = strCompany + getStr( dataCompany.getDepartmentName() ) + "\n";

            // 請求元郵便番号
            String companyZipCode = "";
            if ( dataCompany.getZipCode().length() >= 3 )
            {
                companyZipCode = dataCompany.getZipCode().substring( 0, 3 ) + "-" + dataCompany.getZipCode().substring( 3, dataCompany.getZipCode().length() );
            }
            strCompany = strCompany + getStr( "〒" + companyZipCode ) + "\n";

            // 請求元住所
            strCompany = strCompany + dataCompany.getAddress1() + dataCompany.getAddress2() + dataCompany.getAddress3() + "\n";

            // 請求元電話番号
            strCompany = strCompany + getStr( "TEL:" + dataCompany.getTel() ) + "\n";

            // 請求元FAX番号
            strCompany = strCompany + getStr( "FAX:" + dataCompany.getFax() ) + "\n";

            PdfPCell cellCustHotel = new PdfPCell( new Phrase( strCompany, fontM10B ) );
            cellCustHotel.setHorizontalAlignment( Element.ALIGN_LEFT );
            cellCustHotel.setBorderWidth( 0f );
            cellCustHotel.setBorderWidthLeft( 0f );
            cellCustHotel.setBorderWidthBottom( 0f );
            cellCustHotel.setBorderWidthRight( 0f );
            cellCustHotel.setBorderWidthTop( 0f );
            cellCustHotel.setBorderColor( new Color( 255, 255, 255 ) );
            cellCustHotel.setPaddingTop( 10f );

            custTbl.addCell( cellCustHotel );

            // イメージ出力(JPEGファイル読み込み）
            Image jpg = Image.getInstance( JPEG_FILE );
            PdfPCell cellJpeg = new PdfPCell();
            cellJpeg.setBorderWidth( 0f );
            cellJpeg.setBorderWidthLeft( 0f );
            cellJpeg.setBorderWidthBottom( 0f );
            cellJpeg.setBorderWidthRight( 0f );
            cellJpeg.setBorderWidthTop( 0f );
            cellJpeg.setImage( jpg );
            cellJpeg.setVerticalAlignment( Element.ALIGN_MIDDLE );

            custTbl.addCell( cellJpeg );

            doc.add( custTbl );

            Paragraph pBillCd = new Paragraph( "　　(" + String.valueOf( dataBill.getBillCd() ) + ")", fontM10 );
            pBillCd.setAlignment( Element.ALIGN_LEFT );
            doc.add( pBillCd );

            // 押印欄テーブル
            Table stampTbl = new Table( 4 );
            stampTbl.setWidth( 40 ); // 全体の幅
            stampTbl.setWidths( new int[]{ 25, 25, 25, 25 } ); // 列幅(%)
            stampTbl.setDefaultHorizontalAlignment( Element.ALIGN_CENTER ); // 表示位置（横）
            stampTbl.setDefaultVerticalAlignment( Element.ALIGN_TOP ); // 表示位置（縦）
            stampTbl.setPadding( 1 ); // 余白
            stampTbl.setSpacing( 0 ); // セル間の間隔
            stampTbl.setBorderColor( new Color( 0, 0, 0 ) ); // 線の色
            stampTbl.setAlignment( Element.ALIGN_RIGHT );

            stampTbl.addCell( new Cell( new Phrase( "\n\n\n", fontM10 ) ) );
            stampTbl.addCell( new Cell( new Phrase( "", fontM10 ) ) );
            stampTbl.addCell( new Cell( new Phrase( "", fontM10 ) ) );

            doc.add( stampTbl );

            // タイトル
            String strTitle = "御　請　求　書";
            // Logging.error( "▲" + frm.getChkReissue() );
            if ( "on".equals( frm.getChkReissue() ) )
            {
                strTitle = strTitle + "（再）";
            }
            Paragraph pTitle = new Paragraph( strTitle, fontHeader );
            pTitle.setAlignment( Element.ALIGN_CENTER );
            doc.add( pTitle );

            // 下記の通り～
            Paragraph pKaki = new Paragraph( "下記の通り、御請求致します。", fontM10 );
            pKaki.setAlignment( Element.ALIGN_LEFT );
            doc.add( pKaki );

            DataBkoBill dataBill = getBill();

            // 合計金額
            Paragraph pSum = new Paragraph( "合計金額　　　　　" + addComma( dataBill.getChargeIncTax() ) + ".-", fontM15UB );
            pSum.setAlignment( Element.ALIGN_LEFT );
            doc.add( pSum );
            doc.add( new Paragraph( "　" ) );

            // 明細テーブル
            /**
             * Table detailTbl = new Table( 5 );
             * detailTbl.setWidth( 100 ); // 全体の幅
             * detailTbl.setWidths( new int[]{ 5,60,5,15,15 } ); // 列幅(%)
             * detailTbl.setDefaultHorizontalAlignment( Element.ALIGN_CENTER ); // 表示位置（横）
             * detailTbl.setDefaultVerticalAlignment( Element.ALIGN_MIDDLE ); // 表示位置（縦）
             * detailTbl.setPadding( 1 ); // 余白
             * detailTbl.setSpacing( 0 ); // セル間の間隔
             * detailTbl.setBorderColor( new Color( 0, 0, 0 ) ); // 線の色
             * 
             * //Color backColor = new Color( 192, 255, 192 );
             */
            PdfPTable detailTbl1 = new PdfPTable( 5 );
            detailTbl1.setWidthPercentage( 100f );
            int detailTable_width[] = { 5, 60, 5, 15, 15 };
            detailTbl1.setWidths( detailTable_width );

            PdfPTable detailTbl1_2 = new PdfPTable( 5 );
            detailTbl1_2.setWidthPercentage( 100f );
            detailTbl1_2.setWidths( table_width );

            PdfPCell cellTblTitle1 = new PdfPCell( new Phrase( "項目", fontM10 ) );
            PdfPCell cellTblTitle2 = new PdfPCell( new Phrase( "商品名及び仕様", fontM10 ) );
            PdfPCell cellTblTitle3 = new PdfPCell( new Phrase( "数量", fontM10 ) );
            PdfPCell cellTblTitle4 = new PdfPCell( new Phrase( "単価", fontM10 ) );
            PdfPCell cellTblTitle5 = new PdfPCell( new Phrase( "金額", fontM10 ) );

            cellTblTitle1.setBorderWidth( 1f );
            cellTblTitle2.setBorderWidth( 1f );
            cellTblTitle3.setBorderWidth( 1f );
            cellTblTitle4.setBorderWidth( 1f );
            cellTblTitle5.setBorderWidth( 1f );

            cellTblTitle1.setHorizontalAlignment( Element.ALIGN_CENTER );
            cellTblTitle2.setHorizontalAlignment( Element.ALIGN_CENTER );
            cellTblTitle3.setHorizontalAlignment( Element.ALIGN_CENTER );
            cellTblTitle4.setHorizontalAlignment( Element.ALIGN_CENTER );
            cellTblTitle5.setHorizontalAlignment( Element.ALIGN_CENTER );

            cellTblTitle1.setVerticalAlignment( Element.ALIGN_MIDDLE );
            cellTblTitle2.setVerticalAlignment( Element.ALIGN_MIDDLE );
            cellTblTitle3.setVerticalAlignment( Element.ALIGN_MIDDLE );
            cellTblTitle4.setVerticalAlignment( Element.ALIGN_MIDDLE );
            cellTblTitle5.setVerticalAlignment( Element.ALIGN_MIDDLE );

            detailTbl1.addCell( cellTblTitle1 );
            detailTbl1.addCell( cellTblTitle2 );
            detailTbl1.addCell( cellTblTitle3 );
            detailTbl1.addCell( cellTblTitle4 );
            detailTbl1.addCell( cellTblTitle5 );

            // 明細データ取得
            String query = "";
            Connection connection = null;
            ResultSet result = null;
            PreparedStatement prestate = null;

            query = query + "SELECT d.brand_name ,d.amount ,d.unit_price ,d.quantity, h.bill_date ";
            query = query + "FROM hh_bko_bill h ";
            query = query + "INNER JOIN hh_bko_bill_detail d ";
            query = query + "ON d.bill_slip_no = h.bill_slip_no ";
            query = query + "WHERE h.id = ? ";
            query = query + "AND h.bill_issue_date > ? AND h.bill_issue_date < ? ";
            query = query + "ORDER BY d.line_no ";

            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, frm.getSelHotelID() );
                prestate.setInt( 2, (frm.getBillYear() * 100 + frm.getBillMonth()) * 100 );
                prestate.setInt( 3, (frm.getBillYear() * 100 + frm.getBillMonth() + 1) * 100 );
                result = prestate.executeQuery();

                int rowNo = 0;

                while( result.next() )
                {

                    rowNo++;

                    // 項目
                    PdfPCell cellTblDetail1 = new PdfPCell( new Phrase( String.valueOf( rowNo ), fontM10 ) );
                    cellTblDetail1.setHorizontalAlignment( Element.ALIGN_RIGHT );

                    // 商品名及び仕様
                    PdfPCell cellTblDetail2 = new PdfPCell( new Phrase( String.format( "【%1$04d年%2$02d月】", result.getInt( "h.bill_date" ) / 100, result.getInt( "h.bill_date" ) % 100 ) + result.getString( "d.brand_name" ), fontM10 ) );
                    cellTblDetail2.setHorizontalAlignment( Element.ALIGN_LEFT );

                    // 数量
                    PdfPCell cellTblDetail3 = new PdfPCell( new Phrase( String.valueOf( result.getInt( "d.quantity" ) ), fontM10 ) );
                    cellTblDetail3.setHorizontalAlignment( Element.ALIGN_CENTER );

                    // 単価
                    PdfPCell cellTblDetail4 = new PdfPCell( new Phrase( addComma( result.getInt( "d.unit_price" ) ), fontM10 ) );
                    cellTblDetail4.setHorizontalAlignment( Element.ALIGN_RIGHT );

                    // 金額
                    PdfPCell cellTblDetail5 = new PdfPCell( new Phrase( addComma( result.getInt( "d.amount" ) ), fontM10 ) );
                    cellTblDetail5.setHorizontalAlignment( Element.ALIGN_RIGHT );

                    cellTblDetail1.setBorderWidth( 1f );
                    cellTblDetail2.setBorderWidth( 1f );
                    cellTblDetail3.setBorderWidth( 1f );
                    cellTblDetail4.setBorderWidth( 1f );
                    cellTblDetail5.setBorderWidth( 1f );

                    if ( rowNo < PAGE1_ROW_COUNT )
                    {
                        detailTbl1.addCell( cellTblDetail1 );
                        detailTbl1.addCell( cellTblDetail2 );
                        detailTbl1.addCell( cellTblDetail3 );
                        detailTbl1.addCell( cellTblDetail4 );
                        detailTbl1.addCell( cellTblDetail5 );
                    }
                    else
                    {
                        detailTbl1_2.addCell( cellTblDetail1 );
                        detailTbl1_2.addCell( cellTblDetail2 );
                        detailTbl1_2.addCell( cellTblDetail3 );
                        detailTbl1_2.addCell( cellTblDetail4 );
                        detailTbl1_2.addCell( cellTblDetail5 );
                    }

                    if ( rowNo == PAGE1_ROW_COUNT )
                    {
                        detailTbl1.addCell( cellTblDetailBrank );
                        detailTbl1.addCell( cellNextPage );
                        detailTbl1.addCell( cellTblDetailBrank );
                        detailTbl1.addCell( cellTblDetailBrank );
                        detailTbl1.addCell( cellTblDetailBrank );
                    }
                }

                int count = 0;
                // レコード件数取得
                if ( result.last() != false )
                {
                    count = result.getRow();
                }
                /**
                 * Logging.error( "■■" + count );
                 * // 該当データがない場合
                 * if ( count == 0 )
                 * {
                 * frm.setErrMsg( Message.getMessage( "erro.30001", "請求書" ) );
                 * return;
                 * }
                 */

                // 空行を追加（１ページ）
                if ( count < PAGE1_ROW_COUNT )
                {
                    for( int i = count ; i < PAGE1_ROW_COUNT ; i++ )
                    {
                        for( int j = 0 ; j < 5 ; j++ )
                        {
                            if ( j == 0 )
                            {
                                PdfPCell cellTblDetail = new PdfPCell( new Phrase( String.valueOf( i + 1 ), fontM10 ) );
                                cellTblDetail.setHorizontalAlignment( Element.ALIGN_RIGHT );
                                cellTblDetail.setBorderWidth( 1f );
                                detailTbl1.addCell( cellTblDetail );

                            }
                            else
                            {

                                detailTbl1.addCell( cellTblDetailBrank );

                            }
                        }
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[LogicOwnerBkoComingToday.getAccountRecvDetail] Exception=" + e.toString() );
                // throw new Exception( e );
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }

            doc.add( detailTbl1 );

            // 納品先・合計テーブル
            Table detailTbl2 = new Table( 3 );
            detailTbl2.setWidth( 100 ); // 全体の幅
            detailTbl2.setWidths( new int[]{ 70, 15, 15 } ); // 列幅(%)
            detailTbl2.setDefaultHorizontalAlignment( Element.ALIGN_LEFT ); // 表示位置（横）
            detailTbl2.setDefaultVerticalAlignment( Element.ALIGN_MIDDLE ); // 表示位置（縦）
            detailTbl2.setPadding( 1 ); // 余白
            detailTbl2.setSpacing( 0 ); // セル間の間隔
            detailTbl2.setBorderColor( new Color( 0, 0, 0 ) ); // 線の色
            // detailTbl2.setAlignment( Element.ALIGN_LEFT );

            // 1行目
            // 納品先
            Cell cellPageTitle = new Cell( new Phrase( "納品先　" + dataBill.getHotelName(), fontM10 ) );
            cellPageTitle.setHorizontalAlignment( Element.ALIGN_LEFT );
            cellPageTitle.setVerticalAlignment( Element.ALIGN_TOP );
            cellPageTitle.setBorderWidthRight( 1f );
            cellPageTitle.setRowspan( 3 );
            detailTbl2.addCell( cellPageTitle );

            // 小計
            Cell cellPaymentTitle = new Cell( new Phrase( "小計", fontM10 ) );
            cellPaymentTitle.setHorizontalAlignment( Element.ALIGN_CENTER );
            cellPaymentTitle.setBorderWidthRight( 1f );
            cellPaymentTitle.setBorderWidthBottom( 0.3f );
            detailTbl2.addCell( cellPaymentTitle );

            // 小計
            Cell cellClassTitle = new Cell( new Phrase( addComma( dataBill.getChargeNotIncTax() ), fontM10 ) );
            cellClassTitle.setHorizontalAlignment( Element.ALIGN_RIGHT );
            detailTbl2.addCell( cellClassTitle );

            // 消費税
            Cell cellCustKbnTitle = new Cell( new Phrase( "消費税", fontM10 ) );
            cellCustKbnTitle.setHorizontalAlignment( Element.ALIGN_CENTER );
            cellCustKbnTitle.setBorderWidthRight( 1f );
            cellCustKbnTitle.setBorderWidthBottom( 0.3f );
            detailTbl2.addCell( cellCustKbnTitle );

            // 消費税
            Cell cellAddupKbnTitle = new Cell( new Phrase( addComma( dataBill.getTax() ), fontM10 ) );
            cellAddupKbnTitle.setHorizontalAlignment( Element.ALIGN_RIGHT );
            detailTbl2.addCell( cellAddupKbnTitle );

            // 合計
            Cell cellSumTitle = new Cell( new Phrase( "合計", fontM10 ) );
            cellSumTitle.setHorizontalAlignment( Element.ALIGN_CENTER );
            cellSumTitle.setBorderWidthRight( 1f );
            cellSumTitle.setBorderWidthBottom( 0.3f );
            detailTbl2.addCell( cellSumTitle );

            // 合計
            Cell cellSum = new Cell( new Phrase( addComma( dataBill.getChargeIncTax() ), fontM10 ) );
            cellSum.setHorizontalAlignment( Element.ALIGN_RIGHT );
            detailTbl2.addCell( cellSum );

            doc.add( detailTbl2 );

            // 口座情報テーブル
            Table detailTbl3 = new Table( 1 );
            detailTbl3.setAlignment( Element.ALIGN_LEFT );
            detailTbl3.setWidth( 100 ); // 全体の幅
            detailTbl3.setWidths( new int[]{ 100 } ); // 列幅(%)
            detailTbl3.setDefaultHorizontalAlignment( Element.ALIGN_LEFT ); // 表示位置（横）
            detailTbl3.setDefaultVerticalAlignment( Element.ALIGN_MIDDLE ); // 表示位置（縦）
            detailTbl3.setPadding( 1 ); // 余白
            detailTbl3.setSpacing( 0 ); // セル間の間隔
            detailTbl3.setBorderColor( new Color( 0, 0, 0 ) ); // 線の色

            //
            Cell cellBikouTitle = new Cell( new Phrase( "下記口座へお振込をお願いいたします。" + "\n", fontM10 ) );
            cellBikouTitle.add( new Phrase( dataCompany.getBankForTransfer() + "\n", fontM10B ) );
            cellBikouTitle.add( new Phrase( dataCompany.getAccountName() + "\n", fontM10B ) );
            cellBikouTitle.add( new Phrase( "お振込期限：" + getDateStr( dataBill.getPaymentDate() ) + "迄\n", fontM10B ) );
            cellBikouTitle.add( new Phrase( "※振込手数料は御社ご負担にてお願いいたします。\n", fontM10U ) );
            cellBikouTitle.add( new Phrase( "　", fontM07 ) );
            cellBikouTitle.setHorizontalAlignment( Element.ALIGN_LEFT );
            cellBikouTitle.setVerticalAlignment( Element.ALIGN_MIDDLE );
            detailTbl3.addCell( cellBikouTitle );

            doc.add( detailTbl3 );

            // 2ページ目
            if ( detailTbl1_2.getRows().size() > 0 )
            {

                int rowNum = 0;
                PdfPTable detail = null;
                // detail.setWidthPercentage( 100f );
                // detail.setWidths(table_width);

                for( int k = 0 ; k < detailTbl1_2.getRows().size() ; k++ )
                {
                    if ( rowNum == 0 )
                    {

                        doc.newPage();
                        detail = new PdfPTable( 5 );
                        detail.setWidthPercentage( 100f );
                        detail.setWidths( table_width );

                        detail.addCell( cellTblTitle1 );
                        detail.addCell( cellTblTitle2 );
                        detail.addCell( cellTblTitle3 );
                        detail.addCell( cellTblTitle4 );
                        detail.addCell( cellTblTitle5 );

                    }

                    detail.getRows().add( detailTbl1_2.getRow( k ) );

                    rowNum++;

                    if ( rowNum == PAGE2_ROW_COUNT )
                    {
                        if ( k != detailTbl1_2.getRows().size() - 1 )
                        {
                            doc.add( detail );
                            rowNum = 0;
                        }
                    }

                }

                // 空行を追加
                for( int i = rowNum + 1 ; i < PAGE2_ROW_COUNT - 1 ; i++ )
                {
                    for( int j = 0 ; j < 5 ; j++ )
                    {
                        detail.addCell( cellTblDetailBrank );
                    }
                }

                doc.add( detail );
            }

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

    private String getDateStr(int date)
    {
        String str = String.valueOf( date );
        if ( str.length() == 8 )
        {
            return str.substring( 0, 4 ) + "年" + str.substring( 4, 6 ) + "月" + str.substring( 6, 8 ) + "日";
        }
        return "";
    }

    private String getStr(String str)
    {
        str = str + "　　　　　　　　　　　　　　　";
        return getSubStringByte( str, 30 );
    }

    private String getSubStringByte(String src, int len)
    {
        int dstlen = 0;
        for( int i = 0 ; i < src.length() ; i++ )
        {
            dstlen += (src.charAt( i ) <= 0xff ? 1 : 2);
            if ( dstlen > len )
                return src.substring( 0, i );
        }
        return src;
    }

    private String addComma(int str)
    {
        DecimalFormat format = new DecimalFormat( "###,###,###" );
        return "￥" + format.format( str );
    }

    private DataBkoBill getBill()
    {
        int paraBillDate = frm.getBillYear() * 100 + frm.getBillMonth();

        // 請求データ取得
        if ( !dataBill.getData( frm.getSelHotelID(), paraBillDate ) )
        {
            return null;
        }

        return dataBill;
    }
}
