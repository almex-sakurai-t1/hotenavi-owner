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
 * ���ʐ������׃r�W�l�X���W�b�N
 */
public class LogicOwnerBkoBillDay implements Serializable
{
    private static final long   serialVersionUID = 7755247690249534136L;
    String                      strTitle         = "�n�s�z�e�}�C���@���ʎ��x����";

    private FormOwnerBkoBillDay frm;

    /* �t�H�[���I�u�W�F�N�g */
    public FormOwnerBkoBillDay getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoBillDay frm)
    {
        this.frm = frm;
    }

    /**
     * �������擾
     * 
     * @param �Ȃ�
     * @return �Ȃ�
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

        // �Ώۂ̗��p���擾
        getUsageDateList();

        // ���|���׏��擾
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

            // �����Z
            // �g���擾
            if ( frm.getRsvKind() == 0 || frm.getRsvKind() == 1 )
            {
                seisanCnt = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 4 );
                seisanAmount = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 1 );
                seisanSeikyu = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 3 );
                seisanPayCnt = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_120, 4 );
                seisanPay = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_120, 3 );
            }
            seisanCntList.add( seisanCnt );

            // ���z
            seisanAmountList.add( formatCur.format( seisanAmount ) );

            // �x���i�A�����b�N�X���Ō����Ƃ��͎��������A�z�e�������猩���Ƃ��͎x���j
            // seisanSeikyu = seisanSeikyu * -1;
            seisanSeikyuList.add( formatCur.format( seisanSeikyu ) );

            // �����i�A�����b�N�X���Ō����Ƃ��͎x�������A�z�e�������猩���Ƃ��͎����j
            seisanPayCntList.add( seisanPayCnt );
            seisanPay = seisanPay * -1;
            seisanPayList.add( formatCur.format( seisanPay ) );

            // ���\��
            // �y �n�s�z�e�^�b�` �z
            // �g��
            rsvCnt = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 4 );

            // ���z
            rsvAmount = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 2 );
            rsvSeikyu = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 3 );

            // �y ���u�C���\�� �z
            // �g��
            rsvCntLvj = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 4 );

            // ���z
            rsvAmountLvj = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 2 );
            rsvSeikyuLvj = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 3 );

            if ( frm.getRsvKind() == 0 )
            {
                // �S��
                rsvCntList.add( rsvCnt + rsvCntLvj );
                rsvAmountList.add( formatCur.format( rsvAmount + rsvAmountLvj ) );
                rsvSeikyuList.add( formatCur.format( rsvSeikyu + rsvSeikyuLvj ) );
            }
            else if ( frm.getRsvKind() == 1 )
            {
                // �n�s�z�e�^�b�`
                rsvCntList.add( rsvCnt );
                rsvAmountList.add( formatCur.format( rsvAmount ) );
                rsvSeikyuList.add( formatCur.format( rsvSeikyu ) );
            }
            else if ( frm.getRsvKind() == 2 )
            {
                // ���u�C���\��
                rsvCntList.add( rsvCntLvj );
                rsvAmountList.add( formatCur.format( rsvAmountLvj ) );
                rsvSeikyuList.add( formatCur.format( rsvSeikyuLvj ) );
            }

            if ( frm.getRsvKind() == 0 || frm.getRsvKind() == 1 )
            {
                rsvBonus = getAccountRecvDetail( usageDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_150, 3 );
            }
            rsvBonusList.add( formatCur.format( rsvBonus ) );

            // �����̑�
            // �g��
            if ( frm.getRsvKind() == 0 || frm.getRsvKind() == 1 )
            {
                otherCnt = getAccountRecvDetail( usageDate, -1, 4 );
                otherSeikyu = getAccountRecvDetail( usageDate, -1, 3 );
            }
            otherCntList.add( otherCnt );

            // �x���i�A�����b�N�X���Ō����Ƃ��͎��������A�z�e�������猩���Ƃ��͎x���j
            otherSeikyu = otherSeikyu * -1;
            otherSeikyuList.add( formatCur.format( otherSeikyu ) );

            // ������
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

            // �����v
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

        // ��ʂł́A�z�e�������猩���Ƃ��́A�x���A�������t�ƂȂ邽�߁A�{�A�|�̕������t�ɂ��ĕ\������
        // �����Z�̊e���v
        frm.setSumSeisanCnt( sumSeisanCnt );
        frm.setSumSeisanAmount( formatCur.format( sumSeisanAmount ) );

        // �x�����v�i�A�����b�N�X���Ō����Ƃ��͎��������A�z�e�������猩���Ƃ��͎x���j
        frm.setSumSeisanSeikyu( formatCur.format( sumSeisanSeikyu ) );

        // �������v�i�A�����b�N�X���Ō����Ƃ��͎x�������A�z�e�������猩���Ƃ��͎����j
        frm.setSumSeisanPayCnt( sumSeisanPayCnt );
        frm.setSumSeisanPay( formatCur.format( sumSeisanPay ) );

        // ���\��̊e���v
        frm.setSumRsvCnt( sumRsvCnt );
        frm.setSumRsvAmount( formatCur.format( sumRsvAmount ) );
        frm.setSumRsvSeikyu( formatCur.format( sumRsvSeikyu ) );
        frm.setSumRsvBonus( formatCur.format( sumRsvBonus ) );

        // �����̑��̊e���v
        frm.setSumOtherCnt( sumOtherCnt );

        // �x�����v�i�A�����b�N�X���Ō����Ƃ��͎��������A�z�e�������猩���Ƃ��͎x���j
        frm.setSumOtherSeikyu( formatCur.format( sumOtherSeikyu ) );

        // �������x�̍��v
        frm.setSumTotalSeikyu( formatCur.format( sumTotal ) );
    }

    /**
     * ���|�w�b�_�[���擾
     * 
     * @param �Ȃ�
     * @return �Ȃ�
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

            // ���R�[�h�����擾
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // �Y���f�[�^���Ȃ��ꍇ
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "���ʎ��x����" ) );
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
     * ���|���׏��擾
     * 
     * @param int usageDate ���p��
     * @param int accTitleCd �ȖڃR�[�h
     *        (���̑��̏ꍇ�A-1��ݒ肷��)
     * @param int selKbn (1:���p���z�擾�A2�F�\����z�擾�A3:���ׂ̋��z, 4:����)
     * @return int ���z�A�܂��͌���
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
        // ��ʂ���̑��M����Ă����p�����[�^���擾
        request.setCharacterEncoding( "Shift-JIS" );
        // �o�͗p��Stream���C���X�^���X��
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();

        // �����I�u�W�F�N�g�𐶐�
        // �y�[�W�T�C�Y��ݒ�
        Document doc = new Document( PageSize.A4, 50, 50, 20, 50 );

        try
        {
            // �A�E�g�v�b�g�X�g���[����PDFWriter�ɐݒ�
            PdfWriter.getInstance( doc, byteout );

            // TODO
            doc.addAuthor( "������ЃA�����b�N�X" );
            doc.addSubject( strTitle );

            // �t�b�^�[�o��
            HeaderFooter footer = new HeaderFooter( new Phrase( "-" ), new Phrase( "-" ) );
            footer.setAlignment( Element.ALIGN_CENTER );
            footer.setBorder( Rectangle.NO_BORDER );
            doc.setFooter( footer );

            // �����̃I�[�v��
            doc.open();

            // �^�C�g���s
            doc.add( outputTitle1() );
            doc.add( outputNothing( 2 ) );
            doc.add( outputTitle2() );
            doc.add( outputNothing( 5 ) );

            // ���ׂփ_�[

            doc.add( outputHeader() );
            // ���׏o��
            for( int i = 0 ; i < frm.getUsageDateIntList().size() ; i++ )
            {
                count++;
                if ( count > 23 ) // ���y�[�W
                {
                    doc.add( outputNothing( 10 ) );
                    // �^�C�g���s
                    doc.add( outputTitle1() );
                    doc.add( outputNothing( 2 ) );
                    doc.add( outputTitle2() );
                    doc.add( outputNothing( 5 ) );
                    // ���ׂփ_�[
                    doc.add( outputHeader() );
                    count = 1;
                }
                // ����
                doc.add( outputDetail( i ) );
            }
            // ���v�o��
            // doc.add( outputTotal() );

        }
        catch ( DocumentException e )
        {
            e.printStackTrace();
            Logging.error( "error : " + e.getMessage() );
        }

        // �o�͏I��
        doc.close();

        // �u���E�U�ւ̃f�[�^�𑗐M
        response.setContentType( "application/pdf" );
        response.setContentLength( byteout.size() );
        OutputStream out = response.getOutputStream();
        out.write( byteout.toByteArray() );
        out.close();
    }

    /*
     * �yPDF�z�^�C�g���s�̏o��(1�s��)
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
     * �yPDF�z�^�C�g���s�̏o��(2�s��)
     */
    private PdfPTable outputTitle2() throws DocumentException, IOException
    {

        String rsvKind = "";
        if ( frm.getRsvKind() == 1 )
        {
            rsvKind = "(�n�s�z�e)";
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
        cell.setPhrase( new Phrase( "���t:" + frm.getSelYear() + "�N" + frm.getSelMonth() + "��", fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        table.addCell( cell );
        return table;
    }

    /*
     * �yPDF�z���ׂփ_�[
     */
    private Table outputHeader() throws DocumentException, IOException
    {

        Font fontM10 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10 );

        // ���ׂ̏o��
        Table table = new Table( 12 );
        table.setWidth( 100 ); // �S�̂̕�
        table.setWidths( new int[]{ 12, 6, 10, 9, 6, 10, 9, 8, 8, 9, 5, 8 } ); // ��(%)
        table.setDefaultHorizontalAlignment( Element.ALIGN_CENTER ); // �\���ʒu�i���j
        table.setDefaultVerticalAlignment( Element.ALIGN_MIDDLE ); // �\���ʒu�i�c�j
        table.setPadding( 1 ); // �]��
        table.setSpacing( 0 ); // �Z���Ԃ̊Ԋu
        table.setBorderColor( new Color( 0, 0, 0 ) ); // ���̐F

        // 1�s��
        Cell cell = new Cell( new Phrase( "���t", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 3 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "�x�o", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 9 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "����", fontM10 ) );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 2 );
        table.addCell( cell );

        // 2�s��

        cell = new Cell( new Phrase( "�n�s�z�e�^�b�`", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 3 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "�n�s�z�e�\��", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 4 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "���̑�", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 2 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "���v", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 2 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "�}�C���g�p", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 2 );
        table.addCell( cell );

        // 3�s��

        cell = new Cell( new Phrase( "�g��", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "���z", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "�萔��", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "�g��", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "���z", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "�萔��", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "�\��{\r\n�[�i�X", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "����", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "�}�C��", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        return table;
    }

    /*
     * �yPDF�z����
     */
    private PdfPTable outputDetail(int i) throws DocumentException, IOException
    {

        Font fontM10 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10 );
        Font fontM10ST = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10, Font.STRIKETHRU );
        // ���ׂ̏o��
        PdfPTable table = new PdfPTable( 12 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 12, 6, 10, 9, 6, 10, 9, 8, 8, 9, 5, 8 } ); // ��(%)

        // ���t
        PdfPCell cell = new PdfPCell( new Phrase( frm.getUsageDateStrList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_CENTER );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthLeft( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setMinimumHeight( 25f );
        table.addCell( cell );

        // �g��
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getSeisanCntList().get( i ) ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // ���z
        cell = new PdfPCell( new Phrase( frm.getSeisanAmountList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // �萔��
        cell = new PdfPCell( new Phrase( frm.getSeisanSeikyuList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // �g��
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getRsvCntList().get( i ) ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // ���z
        cell = new PdfPCell( new Phrase( frm.getRsvAmountList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // �萔��
        cell = new PdfPCell( new Phrase( frm.getRsvSeikyuList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // �\��{�[�i�X
        cell = new PdfPCell( new Phrase( frm.getRsvBonusList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // ���̑�
        cell = new PdfPCell( new Phrase( frm.getOtherSeikyuList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // ���v
        cell = new PdfPCell( new Phrase( frm.getTotalSeikyuList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // ����
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getSeisanPayCntList().get( i ) ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // �}�C���g�p
        cell = new PdfPCell( new Phrase( frm.getSeisanPayList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        return table;
    }

    /*
     * �yPDF�z���v
     */
    private PdfPTable outputTotal() throws DocumentException, IOException
    {
        Font fontM10 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10 );

        // ���v�̏o��
        PdfPTable table = new PdfPTable( 12 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 12, 6, 10, 9, 6, 10, 9, 8, 8, 9, 5, 8 } ); // ��(%)

        // ���v
        PdfPCell cell = new PdfPCell( new Phrase( "", fontM10 ) );
        cell = new PdfPCell( new Phrase( "���@�v", fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_CENTER );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthLeft( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setMinimumHeight( 25f );
        table.addCell( cell );

        // �g��
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getSumSeisanCnt() ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // ���z
        cell = new PdfPCell( new Phrase( frm.getSumSeisanAmount(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // �萔��
        cell = new PdfPCell( new Phrase( frm.getSumSeisanSeikyu(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // �g��
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getSumRsvCnt() ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // ���z
        cell = new PdfPCell( new Phrase( frm.getSumRsvAmount(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // �萔��
        cell = new PdfPCell( new Phrase( frm.getSumRsvSeikyu(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // �\��{�[�i�X
        cell = new PdfPCell( new Phrase( frm.getSumRsvBonus(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // ���̑�
        cell = new PdfPCell( new Phrase( frm.getSumOtherSeikyu(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // ���v
        cell = new PdfPCell( new Phrase( frm.getSumTotalSeikyu(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // ����
        cell = new PdfPCell( new Phrase( Kanma.get( frm.getSumSeisanPayCnt() ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // �}�C��
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
     * �yPDF�z�󔒍s�̏o��(1�s��)
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
     * CSV�o��
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

            // �ǋL���[�h
            BufferedWriter bw = new BufferedWriter( pw_base, 10 );

            // ���ڂ��Z�b�g
            bw.write( "���t,�x�o�E�^�b�`�E�g��,�x�o�E�^�b�`�E���z,�x�o�E�^�b�`�E�萔��,�x�o�E�\��E�g��,�x�o�E�\��E���z,�x�o�E�\��E�萔��,�x�o�E�\��E�\��{�[�i�X,�x�o�E���̑�,�x�o�E���v,�����E�}�C���g�p�E����,�����E�}�C���g�p�E�}�C��" );

            bw.newLine();
            // ���׏o��
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

            // ���t
            bw.write( frm.getUsageDateStrList().get( seq ) + "," );

            // �x�o�E�^�b�`�E�g��
            bw.write( frm.getSeisanCntList().get( seq ) + "," );

            // �x�o�E�^�b�`�E���z
            result = frm.getSeisanAmountList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // �x�o�E�^�b�`�E�萔��
            result = frm.getSeisanSeikyuList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // �x�o�E�\��E�g��
            bw.write( frm.getRsvCntList().get( seq ) + "," );

            // �x�o�E�\��E���z
            result = frm.getRsvAmountList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // �x�o�E�\��E�萔��
            result = frm.getRsvSeikyuList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // �x�o�E�\��E�\��{�[�i�X
            result = frm.getRsvBonusList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // �x�o�E���̑�
            result = frm.getOtherSeikyuList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // �x�o�E���v
            result = frm.getTotalSeikyuList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // �����E�}�C���g�p�E����
            bw.write( frm.getSeisanPayCntList().get( seq ) + "," );

            // �����E�}�C���g�p�E�}�C��
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
