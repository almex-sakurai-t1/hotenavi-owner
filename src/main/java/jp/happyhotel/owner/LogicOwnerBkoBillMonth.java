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
 * ���ʐ������׃r�W�l�X���W�b�N
 */
public class LogicOwnerBkoBillMonth implements Serializable
{

    private static final long     serialVersionUID = -6778531648260740904L;
    String                        strTitle         = "�n�s�z�e�}�C���@���ʎ��x����";

    private FormOwnerBkoBillMonth frm;

    /* �t�H�[���I�u�W�F�N�g */
    public FormOwnerBkoBillMonth getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoBillMonth frm)
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

        // �Ώۂ̔N���擾
        getBillDateList();

        // ���|���׏��擾
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

            // hh_bko_closing_control����Ώی��̏���ŗ����擾����
            tax = OwnerBkoCommon.getTax( billDate );

            // �����Z
            // �g���擾
            if ( frm.getRsvKind() == 0 || frm.getRsvKind() == 1 )
            {
                seisanCnt = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 4 );
                seisanAmount = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 1 );
                seisanSeikyu = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 3 );
                seisanPay = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_120, 3 );
            }
            seisanCntList.add( seisanCnt );

            // ���z
            seisanAmountList.add( formatCur.format( seisanAmount ) );

            // ��ʂł́A�z�e�������猩���Ƃ��́A�x���A�������t�ƂȂ邽�߁A�{�A�|�̕������t�ɂ��ĕ\������
            // �x���i�A�����b�N�X���Ō����Ƃ��͎��������A�z�e�������猩���Ƃ��͎x���j
            seisanSeikyu = OwnerBkoCommon.reCalctTax( seisanSeikyu, tax );
            // seisanSeikyu = seisanSeikyu * -1;
            seisanSeikyuList.add( formatCur.format( seisanSeikyu ) );

            // �����i�A�����b�N�X���Ō����Ƃ��͎x�������A�z�e�������猩���Ƃ��͎����j
            seisanPay = seisanPay * -1;
            seisanPayList.add( formatCur.format( seisanPay ) );

            // ���\��
            // �y �n�s�z�e�^�b�` �z
            // �g��
            rsvCnt = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 4 );

            // ���z
            rsvAmount = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 2 );

            // �x���i�A�����b�N�X���Ō����Ƃ��͎��������A�z�e�������猩���Ƃ��͎x���j
            rsvSeikyu = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 3 );
            rsvSeikyu = OwnerBkoCommon.reCalctTax( rsvSeikyu, tax );

            // �y ���u�C���\�� �z
            // �g��
            rsvCntLvj = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 4 );

            // ���z
            rsvAmountLvj = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 2 );

            // �x���i�A�����b�N�X���Ō����Ƃ��͎��������A�z�e�������猩���Ƃ��͎x���j
            rsvSeikyuLvj = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 3 );
            rsvSeikyuLvj = OwnerBkoCommon.reCalctTax( rsvSeikyuLvj, tax );

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
            else
            {
                // ���u�C���\��
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

            // �����Ƃ̐������z�i�A�����b�N�X���Ō����Ƃ��͎��������A�z�e�������猩���Ƃ��͎x���j
            seikyuMonth = getAmountSeikyu( billDate );
            // seikyuMonth = seikyuMonth * -1;
            if ( seikyuMonth != 0 )
            {
                seikyuMonthList.add( formatCur.format( seikyuMonth ) );
            }
            else
            {
                seikyuMonthList.add( "�J�z" );
            }

            // �����̑�
            // �g��
            if ( frm.getRsvKind() == 0 || frm.getRsvKind() == 1 )
            {
                otherCnt = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, -1, 4 );
                otherSeikyu = OwnerBkoCommon.getAccountRecvDetail( frm.getSelHotelID(), billDate, -1, 3 );
            }
            otherCntList.add( otherCnt );

            // �x���i�A�����b�N�X���Ō����Ƃ��͎��������A�z�e�������猩���Ƃ��͎x���j
            otherSeikyu = OwnerBkoCommon.reCalctTax( otherSeikyu, tax );
            // otherSeikyu = otherSeikyu * -1;
            otherSeikyuList.add( formatCur.format( otherSeikyu ) );

            // �������x
            if ( frm.getRsvKind() == 0 )
            {
                // �S��
                seikyu = (seisanSeikyu + rsvSeikyu + rsvSeikyuLvj + otherSeikyu + rsvBonus) + (seisanPay);
            }
            else if ( frm.getRsvKind() == 1 )
            {
                // �n�s�z�e�^�b�`
                seikyu = (seisanSeikyu + rsvSeikyu + otherSeikyu + rsvBonus) + (seisanPay);
            }
            else
            {
                // ���u�C���\��
                seikyu = (seisanSeikyu + rsvSeikyuLvj + otherSeikyu + rsvBonus) + (seisanPay);
            }

            seikyuList.add( formatCur.format( seikyu ) );

            // �������x�i���� �z�e������݂���x�o�j
            if ( frm.getRsvKind() == 0 )
            {
                // �S��
                seikyuIncome = (seisanSeikyu + rsvSeikyu + rsvSeikyuLvj + rsvBonus + otherSeikyu);
            }
            else if ( frm.getRsvKind() == 1 )
            {
                // �n�s�z�e�^�b�`
                seikyuIncome = (seisanSeikyu + rsvSeikyu + rsvBonus + otherSeikyu);
            }
            else
            {
                // ���u�C���\��
                seikyuIncome = (seisanSeikyu + rsvSeikyuLvj + rsvBonus + otherSeikyu);
            }

            seikyuIncomeList.add( formatCur.format( seikyuIncome ) );

            // �������x�i�x�o �z�e������݂������j
            seikyuPay = (seisanPay);
            seikyuPayList.add( formatCur.format( seikyuPay ) );

            // ���������z�擾
            inputCharge = getInputChargeByMonth( billDate, 0 );
            inputCharge = inputCharge * -1;
            inputChargeList.add( formatCur.format( inputCharge ) );

            // ��������
            inputDate = getInputChargeDateByMonth( billDate, 0 );
            if ( inputDate > 0 )
            {
                inputDateList.add( String.format( "%1$04d/%2$02d/%3$02d", inputDate / 10000, inputDate % 10000 / 100, inputDate % 10000 % 100 ) );
            }
            else
            {
                inputDateList.add( "-" );
            }

            // �����v
            sumSeisanCnt = sumSeisanCnt + seisanCnt;
            sumSeisanAmount = sumSeisanAmount + seisanAmount;
            sumSeisanSeikyu = sumSeisanSeikyu + seisanSeikyu;
            sumSeisanPay = sumSeisanPay + seisanPay;

            if ( frm.getRsvKind() == 0 )
            {
                // �S��
                sumRsvCnt = sumRsvCnt + rsvCnt + rsvCntLvj;
                sumRsvAmount = sumRsvAmount + rsvAmount + rsvAmountLvj;
                sumRsvSeikyu = sumRsvSeikyu + rsvSeikyu + rsvSeikyuLvj;
            }
            else if ( frm.getRsvKind() == 1 )
            {
                // �n�s�z�e�^�b�`
                sumRsvCnt = sumRsvCnt + rsvCnt;
                sumRsvAmount = sumRsvAmount + rsvAmount;
                sumRsvSeikyu = sumRsvSeikyu + rsvSeikyu;
            }
            else
            {
                // ���u�C���\��
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

        // ��ʂł́A�z�e�������猩���Ƃ��́A�x���A�������t�ƂȂ邽�߁A�{�A�|�̕������t�ɂ��ĕ\������
        // �����Z�̊e���v
        frm.setSumSeisanCnt( sumSeisanCnt );
        frm.setSumSeisanAmount( formatCur.format( sumSeisanAmount ) );

        // �x�����v�i�A�����b�N�X���Ō����Ƃ��͎��������A�z�e�������猩���Ƃ��͎x���j
        frm.setSumSeisanSeikyu( formatCur.format( sumSeisanSeikyu ) );

        // �������v�i�A�����b�N�X���Ō����Ƃ��͎x�������A�z�e�������猩���Ƃ��͎����j
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
        frm.setSumTotalSeikyuIncome( formatCur.format( sumTotalIncome ) );

    }

    /**
     * �w�茎���͋��z�擾����
     * 
     * @param todate �w��N��
     * @return ���͋��z���v�z
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
     * �������擾����
     * 
     * @param todate �w��N����
     * @param kind 1: �����C2:�x����
     * @return ������
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
     * �Ώ۔N���擾(0�̌����\������悤�ɕύX)
     * 
     * @param �Ȃ�
     * @return �Ȃ�
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
        // �w�肳�ꂽ�J�n�������o�b�N�I�t�B�X�̕\���J�n���̕����傫���ꍇ�́A�o�b�N�I�t�B�X�̕\���J�n�����Z�b�g
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
            // �Y���f�[�^���Ȃ��ꍇ
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "���ʎ��x����" ) );
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
     * �w�萿�����������z�擾
     * 
     * @param int billDate �����N��
     * @return int ���z
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
            // �������s�N�������w��͈͓̔��̋��z�����v����
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
            for( int i = 0 ; i < frm.getBillDateIntList().size() ; i++ )
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
            doc.add( outputTotal() );

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
        cell.setPhrase( new Phrase( "���t:" + frm.getSelYearFrom() + "�N" + frm.getSelMonthFrom() + "���`" + frm.getSelYearTo() + "�N" + frm.getSelMonthTo() + "��", fontM10 ) );
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
        Table table = new Table( 11 );
        table.setWidth( 100 ); // �S�̂̕�
        table.setWidths( new int[]{ 10, 6, 12, 9, 6, 12, 9, 9, 9, 9, 9 } ); // ��(%)
        table.setDefaultHorizontalAlignment( Element.ALIGN_CENTER ); // �\���ʒu�i���j
        table.setDefaultVerticalAlignment( Element.ALIGN_MIDDLE ); // �\���ʒu�i�c�j
        table.setPadding( 1 ); // �]��
        table.setSpacing( 0 ); // �Z���Ԃ̊Ԋu
        table.setBorderColor( new Color( 0, 0, 0 ) ); // ���̐F

        // 1�s��
        Cell cell = new Cell( new Phrase( "�N��", fontM10 ) );
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

        cell = new Cell( new Phrase( "�}�C��\r\n�g�p", fontM10 ) );
        cell.setRowspan( 2 );
        cell.setColspan( 1 );
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

        return table;
    }

    /*
     * �yPDF�z����
     */
    private PdfPTable outputDetail(int i) throws DocumentException, IOException
    {

        Font fontM10 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10 );
        // ���ׂ̏o��
        PdfPTable table = new PdfPTable( 11 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 10, 6, 12, 9, 6, 12, 9, 9, 9, 9, 9 } ); // ��(%)

        // �N��
        PdfPCell cell = new PdfPCell( new Phrase( frm.getBillDateStrList().get( i ), fontM10 ) );
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
        cell.setPaddingRight( 5 );
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
        cell = new PdfPCell( new Phrase( frm.getTotalSeikyuIncomeList().get( i ), fontM10 ) );
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
        PdfPTable table = new PdfPTable( 11 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 10, 6, 12, 9, 6, 12, 9, 9, 9, 9, 9 } ); // ��(%)

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
        cell = new PdfPCell( new Phrase( frm.getSumTotalSeikyuIncome(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // �}�C���g�p
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
            String filename = "billmonth.csv";
            filename = new String( filename.getBytes( "Shift_JIS" ), "ISO8859_1" );
            filename = ReplaceString.SQLEscape( filename );
            response.setHeader( "Content-Disposition", "attachment;filename=\"" + filename + "\"" );
            response.setContentType( "text/html; charset=Windows-31J" );
            PrintWriter pw_base = response.getWriter();

            // �ǋL���[�h
            BufferedWriter bw = new BufferedWriter( pw_base, 10 );

            // ���ڂ��Z�b�g
            bw.write( "�N��,�x�o�E�^�b�`�E�g��,�x�o�E�^�b�`�E���z,�x�o�E�^�b�`�E�萔��,�x�o�E�\��E�g��,�x�o�E�\��E���z,�x�o�E�\��E�萔��,�x�o�E�\��E�\��{�[�i�X,�x�o�E���̑�,�x�o�E���v,�����E�}�C���g�p" );

            bw.newLine();
            // ���׏o��
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

            // �N��
            bw.write( frm.getBillDateStrList().get( seq ) + "," );

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
            result = frm.getTotalSeikyuIncomeList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // �����E�}�C���g�p
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
