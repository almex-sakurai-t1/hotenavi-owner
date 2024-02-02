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

import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
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
public class LogicOwnerBkoBillToday implements Serializable
{
    private static final long     serialVersionUID = -7916744090550829506L;
    String                        strTitle         = "�n�s�z�e�}�C���@���x����";

    private FormOwnerBkoBillToday frm;

    /* �t�H�[���I�u�W�F�N�g */
    public FormOwnerBkoBillToday getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoBillToday frm)
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
        int seisanAmount = 0;
        int seisanFee = 0;
        int rsvAmount = 0;
        int rsvAmountLvj = 0;
        int rsvFee = 0;
        int rsvFeeLvj = 0;
        int rsvBonus = 0;
        int slipNo = 0;
        int bill = 0;
        int pay = 0;
        String paystr = "";
        int sumBill = 0;
        int sumPay = 0;
        int sumSyusi = 0;
        String sumSyusiView = "";
        ArrayList<String> seisanAmountList = new ArrayList<String>();
        ArrayList<String> seisanFeeList = new ArrayList<String>();
        ArrayList<String> rsvAmountList = new ArrayList<String>();
        ArrayList<String> rsvFeeList = new ArrayList<String>();
        ArrayList<String> rsvBonusList = new ArrayList<String>();
        ArrayList<String> billList = new ArrayList<String>(); // ����
        ArrayList<String> payList = new ArrayList<String>(); // �x��
        NumberFormat formatCur = NumberFormat.getNumberInstance();

        // ���|�w�b�_�[���擾(���p�����A�S���A�ڋqID�A�`�[No�A����)
        getAccountRecvHeader();

        // ���e�擾
        getAccountNm();

        // ���|���׏��擾(���z�A�����A�x����)
        for( int i = 0 ; i < frm.getSlipNoList().size() ; i++ )
        {
            slipNo = frm.getSlipNoList().get( i );
            bill = 0;
            pay = 0;
            paystr = "";
            seisanAmount = 0;
            seisanFee = 0;
            rsvAmount = 0;
            rsvAmountLvj = 0;
            rsvFee = 0;
            rsvFeeLvj = 0;
            rsvBonus = 0;
            bill = 0;
            pay = 0;

            // �����Z���z�̎擾
            seisanAmount = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 1 );
            seisanAmountList.add( formatCur.format( seisanAmount ) );

            // ���^�b�`�萔���̎擾
            seisanFee = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 3 );
            seisanFeeList.add( formatCur.format( seisanFee ) );

            // ���\����z�̎擾
            rsvAmount = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 2 );
            rsvAmountLvj = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 2 );

            // ���\��萔���̎擾
            rsvFee = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 3 );
            rsvFeeLvj = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 3 );

            if ( frm.getRsvKind() == 0 )
            {
                // �S��
                rsvAmountList.add( formatCur.format( rsvAmount + rsvAmountLvj ) );
                rsvFeeList.add( formatCur.format( rsvFee + rsvFeeLvj ) );
            }
            else if ( frm.getRsvKind() == 1 )
            {
                // �n�s�z�e�^�b�`
                rsvAmountList.add( formatCur.format( rsvAmount ) );
                rsvFeeList.add( formatCur.format( rsvFee ) );
            }
            else
            {
                // ���u�C���\��
                rsvAmountList.add( formatCur.format( rsvAmountLvj ) );
                rsvFeeList.add( formatCur.format( rsvFeeLvj ) );
            }

            // ���\��{�[�i�X�̎擾
            rsvBonus = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_150, 3 );
            rsvBonusList.add( formatCur.format( rsvBonus ) );

            // ���x���̎擾�i�A�����b�N�X���Ō����Ƃ��͎��������A�z�e�������猩���Ƃ��͎x���j
            bill = getBillAmount( frm.getSlipNoList().get( i ) );
            paystr = formatCur.format( bill );

            billList.add( paystr );

            // �������̎擾�i�A�����b�N�X���Ō����Ƃ��͎x�������A�z�e�������猩���Ƃ��͎����j
            pay = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_120, 3 );
            pay = pay * -1;

            payList.add( formatCur.format( pay ) );

            sumBill = sumBill + bill;
            sumPay = sumPay + pay;
        }

        frm.setSeisanAmountList( seisanAmountList );
        frm.setSeisanFeeList( seisanFeeList );
        frm.setRsvAmountList( rsvAmountList );
        frm.setRsvFeeList( rsvFeeList );
        frm.setRsvBonusList( rsvBonusList );
        frm.setBillList( billList );
        frm.setPayList( payList );
        frm.setSumBill( formatCur.format( sumBill ) );
        frm.setSumBill( formatCur.format( sumBill ) );
        frm.setSumPay( formatCur.format( sumPay ) );
        sumSyusi = sumPay - sumBill;
        sumSyusiView = formatCur.format( sumSyusi );
        frm.setSumSyusi( sumSyusiView );
    }

    /**
     * ���|�w�b�_�[���擾
     * 
     * @param �Ȃ�
     * @return �Ȃ�
     */
    private void getAccountRecvHeader() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        String usageDate = "";
        ArrayList<Integer> accSlipNoList = new ArrayList<Integer>();
        ArrayList<String> htSlipNoList = new ArrayList<String>();
        ArrayList<String> usageDateList = new ArrayList<String>();
        ArrayList<Integer> usageDateIntList = new ArrayList<Integer>();
        ArrayList<String> usageTimeList = new ArrayList<String>();
        ArrayList<Integer> usageTimeIntList = new ArrayList<Integer>();
        ArrayList<String> personNmList = new ArrayList<String>();
        ArrayList<Integer> usMngNoList = new ArrayList<Integer>();
        ArrayList<String> roomNoList = new ArrayList<String>();
        ArrayList<Integer> billDateList = new ArrayList<Integer>();
        ArrayList<String> userIdList = new ArrayList<String>();

        query = query + "SELECT DISTINCT ";
        query = query + "rcv.accrecv_slip_no, rcv.ht_slip_no, rcv.usage_date, rcv.usage_time, rcv.person_name, ";
        query = query + "rcv.user_management_no, rcv.ht_room_no, bill.bill_date, detail.user_id ";
        query = query + "FROM hh_bko_bill bill ";
        query = query + " INNER JOIN hh_bko_rel_bill_account_recv bdt ON bill.bill_slip_no = bdt.bill_slip_no ";
        query = query + "   LEFT JOIN hh_bko_account_recv rcv ON bdt.accrecv_slip_no = rcv.accrecv_slip_no ";
        query = query + "     LEFT JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no ";
        query = query + " INNER JOIN hh_hotel_newhappie happie ON bill.id = happie.id ";
        query = query + "WHERE bill.bill_date = ? ";
        query = query + "  AND rcv.id = ? ";
        query = query + "  AND rcv.invalid_flag = ? ";
        if ( frm.getUsageDate() != 0 )
        {
            query = query + "AND rcv.usage_date = ? ";
        }
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
            query = query + " AND (detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_141 + " ) ";
        }

        query = query + "ORDER BY rcv.usage_date, rcv.usage_time, rcv.user_management_no, rcv.accrecv_slip_no";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getIntBillDate() );
            prestate.setInt( 2, frm.getSelHotelID() );
            prestate.setInt( 3, 0 );
            if ( frm.getUsageDate() != 0 )
            {
                prestate.setInt( 4, frm.getUsageDate() );
            }
            result = prestate.executeQuery();

            while( result.next() )
            {

                accSlipNoList.add( result.getInt( "accrecv_slip_no" ) );
                if ( result.getString( "ht_slip_no" ) == null )
                {
                    htSlipNoList.add( "" );
                }
                else
                {
                    if ( (result.getString( "ht_room_no" ) == null) || (result.getString( "ht_room_no" ).equals( "0" )) )
                    {
                        htSlipNoList.add( "" );
                    }
                    else
                    {
                        htSlipNoList.add( result.getString( "ht_slip_no" ) );
                    }
                }
                usageDate = result.getString( "usage_date" );
                usageDateIntList.add( result.getInt( "usage_date" ) );
                usageDateList.add( usageDate.substring( 0, 4 ) + "/" + usageDate.substring( 4, 6 ) + "/" + usageDate.substring( 6 ) );
                usageTimeList.add( ConvertTime.convTimeHHMM( result.getInt( "usage_time" ), 0 ) );
                usageTimeIntList.add( result.getInt( "usage_time" ) );
                personNmList.add( result.getString( "person_name" ) );
                usMngNoList.add( result.getInt( "user_management_no" ) );
                if ( (result.getString( "ht_room_no" ) == null) || (result.getString( "ht_room_no" ).trim().length() == 0) )
                {
                    roomNoList.add( "��" );
                }
                else
                {
                    if ( result.getString( "ht_room_no" ).equals( "0" ) )
                    {
                        roomNoList.add( "��" );
                    }
                    else
                    {
                        roomNoList.add( result.getString( "ht_room_no" ) );
                    }
                }
                billDateList.add( result.getInt( "bill_date" ) );
                userIdList.add( result.getString( "user_id" ) );
            }

            // ���R�[�h�����擾
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // �Y���f�[�^���Ȃ��ꍇ
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "���x����" ) );
                return;
            }

            frm.setSlipNoList( accSlipNoList );
            frm.setHtSlipNoList( htSlipNoList );
            frm.setUsageDateList( usageDateList );
            frm.setUsageDateIntList( usageDateIntList );
            frm.setUsageTimeList( usageTimeList );
            frm.setUsageTimeIntList( usageTimeIntList );
            frm.setPersonNmList( personNmList );
            frm.setCustomerIdList( usMngNoList );
            frm.setRoomList( roomNoList );
            frm.setBillDate( billDateList );
            frm.setUserIdList( userIdList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillToday.getAccountRecvHeader] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * ���e�擾
     * 
     * @param �Ȃ�
     * @return �Ȃ�
     */
    private void getAccountNm() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> accSlipNoList = new ArrayList<Integer>();
        ArrayList<Integer> accTitleCdList = new ArrayList<Integer>();
        ArrayList<String> accTitleNmist = new ArrayList<String>();
        ArrayList<Integer> newAccTitleCdList = new ArrayList<Integer>();
        int orgSlipNo = 0;
        int newSlipNo = 0;
        int orgAccTitleCd = 0;
        ArrayList<String> huyoList = new ArrayList<String>();
        ArrayList<String> siyouList = new ArrayList<String>();
        ArrayList<String> soukyakuList = new ArrayList<String>();
        ArrayList<String> acc200List = new ArrayList<String>();
        int idx = 0;

        query = query + "SELECT DISTINCT ";
        query = query + "rcv.accrecv_slip_no, detail.account_title_cd, detail.account_title_name ";
        query = query + "FROM hh_bko_bill bill ";
        query = query + " INNER JOIN hh_bko_rel_bill_account_recv bdt ON bill.bill_slip_no = bdt.bill_slip_no ";
        query = query + "   LEFT JOIN hh_bko_account_recv rcv ON bdt.accrecv_slip_no = rcv.accrecv_slip_no ";
        query = query + "     LEFT JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no ";
        query = query + " INNER JOIN hh_hotel_newhappie happie ON bill.id = happie.id ";
        query = query + "WHERE bill.bill_date = ? ";
        query = query + "  AND rcv.id = ? ";
        query = query + "  AND rcv.invalid_flag = ? ";
        if ( frm.getUsageDate() != 0 )
        {
            query = query + " AND rcv.usage_date = ? ";
        }
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
            query = query + "  AND (detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_141 + " )";
        }

        query = query + "ORDER BY rcv.usage_date, rcv.usage_time, rcv.user_management_no, rcv.accrecv_slip_no, detail.account_title_cd";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getIntBillDate() );
            prestate.setInt( 2, frm.getSelHotelID() );
            prestate.setInt( 3, 0 );
            if ( frm.getUsageDate() != 0 )
            {
                prestate.setInt( 4, frm.getUsageDate() );
            }
            result = prestate.executeQuery();

            while( result.next() )
            {
                accSlipNoList.add( result.getInt( "accrecv_slip_no" ) );
                accTitleCdList.add( result.getInt( "account_title_cd" ) );
                accTitleNmist.add( result.getString( "account_title_name" ) );
            }

            // �Ȗږ��̐��`
            for( int i = 0 ; i < accSlipNoList.size() ; i++ )
            {
                orgSlipNo = accSlipNoList.get( i );
                if ( i == 0 )
                {
                    newSlipNo = accSlipNoList.get( i );
                    if ( accTitleCdList.get( i ) >= OwnerBkoCommon.ACCOUNT_TITLE_CD_200 )
                    {
                        orgAccTitleCd = 1;
                    }
                    switch( accTitleCdList.get( i ) )
                    {
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_110:
                            huyoList.add( "��" );
                            siyouList.add( "" );
                            soukyakuList.add( "" );
                            acc200List.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_120:
                            huyoList.add( "" );
                            siyouList.add( "��" );
                            soukyakuList.add( "" );
                            acc200List.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_140:
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_141:
                            huyoList.add( "" );
                            siyouList.add( "" );
                            soukyakuList.add( "��" );
                            acc200List.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_150:
                            huyoList.add( "" );
                            siyouList.add( "" );
                            soukyakuList.add( "��" );
                            acc200List.add( "" );
                            break;
                        default:
                            huyoList.add( "" );
                            siyouList.add( "" );
                            soukyakuList.add( "" );
                            acc200List.add( accTitleNmist.get( i ) );
                            break;
                    }
                }

                if ( orgSlipNo == newSlipNo )
                {
                    if ( i != 0 )
                    {
                        switch( accTitleCdList.get( i ) )
                        {
                            case OwnerBkoCommon.ACCOUNT_TITLE_CD_110:
                                huyoList.set( idx, "��" );
                                break;
                            case OwnerBkoCommon.ACCOUNT_TITLE_CD_120:
                                siyouList.set( idx, "��" );
                                break;
                            case OwnerBkoCommon.ACCOUNT_TITLE_CD_140:
                                soukyakuList.set( idx, "��" );
                                break;
                            case OwnerBkoCommon.ACCOUNT_TITLE_CD_150:
                                soukyakuList.set( idx, "��" );
                                break;
                            default:
                                acc200List.add( accTitleNmist.get( i ) );
                                break;
                        }
                    }
                }
                else
                {
                    // �l���i�[
                    switch( accTitleCdList.get( i ) )
                    {
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_110:
                            huyoList.add( "��" );
                            siyouList.add( "" );
                            soukyakuList.add( "" );
                            acc200List.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_120:
                            huyoList.add( "" );
                            siyouList.add( "��" );
                            soukyakuList.add( "" );
                            acc200List.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_140:
                            huyoList.add( "" );
                            siyouList.add( "" );
                            soukyakuList.add( "��" );
                            acc200List.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_150:
                            huyoList.add( "" );
                            siyouList.add( "" );
                            soukyakuList.add( "��" );
                            acc200List.add( "" );
                            break;
                        default:
                            huyoList.add( "" );
                            siyouList.add( "" );
                            soukyakuList.add( "" );
                            acc200List.add( accTitleNmist.get( i ) );
                            break;
                    }
                    newAccTitleCdList.add( orgAccTitleCd );
                    orgAccTitleCd = 0;
                    if ( accTitleCdList.get( i ) >= OwnerBkoCommon.ACCOUNT_TITLE_CD_200 )
                    {
                        orgAccTitleCd = 1;
                    }

                    idx++;
                }
                newSlipNo = orgSlipNo;
                if ( accTitleCdList.get( i ) >= OwnerBkoCommon.ACCOUNT_TITLE_CD_200 )
                {
                    orgAccTitleCd = 1;
                }
            }
            newAccTitleCdList.add( orgAccTitleCd );

            frm.setAccountTitleCdList( newAccTitleCdList );
            frm.setHuyoList( huyoList );
            frm.setSiyouList( siyouList );
            frm.setReserveList( soukyakuList );
            frm.setAcc200List( acc200List );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillToday.getAccountNm] Exception=" + e.toString() );
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
     * @param int slipNo ���|�`�[�ԍ�
     * @param int accTitleCd �ȖڃR�[�h
     * @param int selKbn (1:���p���z�擾�A2�F�\����z�擾�A3:���ׂ̋��z)
     * @return int ���z
     */
    private int getAccountRecvDetail(int slipNo, int accTitleCd, int selKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int amount = 0;

        // �`�[�敪�̏������擾

        query = query + "SELECT detail.amount, rcv.usage_charge, rcv.receive_charge ";
        query = query + "FROM hh_bko_account_recv rcv ";
        query = query + "   LEFT JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no ";
        query = query + "WHERE rcv.id = ? ";
        query = query + "  AND rcv.accrecv_slip_no = ? ";
        query = query + "  AND detail.account_title_cd = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, slipNo );
            prestate.setInt( 3, accTitleCd );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( selKbn == 1 )
                {
                    amount += result.getInt( "usage_charge" );

                }
                else if ( selKbn == 2 )
                {
                    amount += result.getInt( "receive_charge" );

                }
                else
                {
                    amount += result.getInt( "amount" );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillToday.getAccountRecvDetail] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(amount);
    }

    /**
     * �������z�̎擾
     * 
     * @param int slipNo ���|�`�[�ԍ�
     * @return int ���z
     */
    private int getBillAmount(int slipNo) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int amount = 0;

        query = query + "SELECT SUM(detail.amount) AS amount ";
        query = query + "FROM hh_bko_account_recv rcv ";
        query = query + "   LEFT JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no ";
        query = query + "WHERE rcv.id = ? ";
        query = query + "  AND rcv.invalid_flag = ? ";
        query = query + "  AND rcv.accrecv_slip_no = ? ";
        query = query + "  AND (detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_110 + " OR ";
        if ( frm.getRsvKind() == 0 )
        {
            query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_140 + " OR ";
            query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_141 + " OR ";
        }
        else if ( frm.getRsvKind() == 1 )
        {
            query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_140 + " OR ";
        }
        else
        {
            query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_141 + " OR ";
        }
        query = query + "       detail.account_title_cd =  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_150 + " OR ";
        query = query + "       detail.account_title_cd >=  " + OwnerBkoCommon.ACCOUNT_TITLE_CD_200 + " ) ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, 0 );
            prestate.setInt( 3, slipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                amount = result.getInt( "amount" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillToday.getBillAmount] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(amount);
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
            for( int i = 0 ; i < frm.getSlipNoList().size() ; i++ )
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
            doc.add( outputTotal1() );
            doc.add( outputTotal2() );

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
        table.setWidths( new int[]{ 11, 7, 7, 9, 6, 9, 7, 9, 7, 8, 10, 10 } ); // ��(%)
        table.setDefaultHorizontalAlignment( Element.ALIGN_CENTER ); // �\���ʒu�i���j
        table.setDefaultVerticalAlignment( Element.ALIGN_MIDDLE ); // �\���ʒu�i�c�j
        table.setPadding( 1 ); // �]��
        table.setSpacing( 0 ); // �Z���Ԃ̊Ԋu
        table.setBorderColor( new Color( 0, 0, 0 ) ); // ���̐F

        // 1�s��
        Cell cell = new Cell( new Phrase( "���t", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 3 );
        cell.setColspan( 2 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "���[�U\r\n�[ID", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 3 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "���V�[�g\r\n�ԍ�", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 3 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "����\r\n�ԍ�", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 3 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "�x�o", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 6 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "����", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        // 2�s��

        cell = new Cell( new Phrase( "�n�s�z�e�^�b�`", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 2 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "�\��", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 3 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "�萔��\r\n���v", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 2 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "�}�C��\r\n�g�p", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 2 );
        cell.setColspan( 1 );
        table.addCell( cell );

        // 3�s��

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
        PdfPTable table = new PdfPTable( 12 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 11, 7, 7, 9, 6, 9, 7, 9, 7, 8, 10, 10 } ); // ��(%)

        // ���t
        PdfPCell cell = new PdfPCell( new Phrase( frm.getUsageDateList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_CENTER );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthLeft( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setMinimumHeight( 25f );
        table.addCell( cell );

        // ����
        cell = new PdfPCell( new Phrase( frm.getUsageTimeList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_CENTER );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        if ( frm.getAccountTitleCdList().get( i ) == 1 )
        {
            cell = new PdfPCell( new Phrase( frm.getAcc200List().get( i ), fontM10 ) );
            cell.setHorizontalAlignment( Element.ALIGN_CENTER );
            cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
            cell.setBorderWidthRight( 1f );
            cell.setColspan( 8 );
            table.addCell( cell );
        }
        else
        {
            // ���[�U�[ID
            if ( frm.getCustomerIdList().get( i ) == 0 )
            {
                // &nbsp;
                cell = new PdfPCell( new Phrase( "", fontM10 ) );
                cell.setHorizontalAlignment( Element.ALIGN_CENTER );
                cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
                cell.setBorderWidthRight( 1f );
                table.addCell( cell );
            }
            else
            {
                cell = new PdfPCell( new Phrase( String.valueOf( frm.getCustomerIdList().get( i ) ), fontM10 ) );
                cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
                cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
                cell.setBorderWidthRight( 1f );
                table.addCell( cell );
            }

            // ���V�[�g�ԍ�
            if ( (frm.getHtSlipNoList().get( i ) == null) || (frm.getHtSlipNoList().get( i ).trim().length() == 0) )
            {
                // &nbsp;
                cell = new PdfPCell( new Phrase( "", fontM10 ) );
                cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
                cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
                cell.setBorderWidthRight( 1f );
                table.addCell( cell );
            }
            else if ( (Integer.parseInt( frm.getHtSlipNoList().get( i ) ) == 0) )
            {
                // &nbsp;
                cell = new PdfPCell( new Phrase( "", fontM10 ) );
                cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
                cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
                cell.setBorderWidthRight( 1f );
                table.addCell( cell );

            }
            else
            {
                cell = new PdfPCell( new Phrase( frm.getHtSlipNoList().get( i ), fontM10 ) );
                cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
                cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
                cell.setBorderWidthRight( 1f );
                table.addCell( cell );

            }

            // �����ԍ�
            cell = new PdfPCell( new Phrase( frm.getRoomList().get( i ), fontM10 ) );
            cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
            cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
            cell.setBorderWidthRight( 1f );
            table.addCell( cell );

            // �n�s�z�e�^�b�`���z
            cell = new PdfPCell( new Phrase( frm.getSeisanAmountList().get( i ), fontM10 ) );
            cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
            cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
            cell.setBorderWidthRight( 1f );
            table.addCell( cell );

            // �n�s�z�e�^�b�`�萔��
            cell = new PdfPCell( new Phrase( frm.getSeisanFeeList().get( i ), fontM10 ) );
            cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
            cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
            cell.setBorderWidthRight( 1f );
            table.addCell( cell );

            // �n�s�z�e�\����z
            cell = new PdfPCell( new Phrase( frm.getRsvAmountList().get( i ), fontM10 ) );
            cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
            cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
            cell.setBorderWidthRight( 1f );
            table.addCell( cell );

            // �n�s�z�e�\��萔��
            cell = new PdfPCell( new Phrase( frm.getRsvFeeList().get( i ), fontM10 ) );
            cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
            cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
            cell.setBorderWidthRight( 1f );
            table.addCell( cell );

            // �n�s�z�e�\��{�[�i�X
            cell = new PdfPCell( new Phrase( frm.getRsvBonusList().get( i ), fontM10 ) );
            cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
            cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
            cell.setBorderWidthRight( 1f );
            table.addCell( cell );

        }

        // �萔�����v
        cell = new PdfPCell( new Phrase( frm.getBillList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // �}�C���g�p
        cell = new PdfPCell( new Phrase( frm.getPayList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        return table;
    }

    /*
     * �yPDF�z���v
     */
    private PdfPTable outputTotal1() throws DocumentException, IOException
    {
        Font fontM10 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10 );

        // ���v�̏o��
        PdfPTable table = new PdfPTable( 3 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 80, 10, 10 } ); // ��(%)

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

        // �萔��
        cell = new PdfPCell( new Phrase( frm.getSumBill(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // �}�C���g�p
        cell = new PdfPCell( new Phrase( frm.getSumPay(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        return table;
    }

    /*
     * �yPDF�z���v
     */
    private PdfPTable outputTotal2() throws DocumentException, IOException
    {
        Font fontM10 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10 );

        // ���v�̏o��
        PdfPTable table = new PdfPTable( 3 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 80, 10, 10 } ); // ��(%)

        // �u�����N
        PdfPCell cell = new PdfPCell( new Phrase( "", fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_CENTER );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthLeft( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setMinimumHeight( 25f );
        table.addCell( cell );

        // ���x�v
        cell = new PdfPCell( new Phrase( "���x�v", fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // �}�C���g�p
        cell = new PdfPCell( new Phrase( frm.getSumSyusi(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
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
            String filename = "billtoday.csv";
            filename = new String( filename.getBytes( "Shift_JIS" ), "ISO8859_1" );
            filename = ReplaceString.SQLEscape( filename );
            response.setHeader( "Content-Disposition", "attachment;filename=\"" + filename + "\"" );
            response.setContentType( "text/html; charset=Windows-31J" );
            PrintWriter pw_base = response.getWriter();

            // �ǋL���[�h
            BufferedWriter bw = new BufferedWriter( pw_base, 10 );

            // ���ڂ��Z�b�g
            bw.write( "���t,����,���[�U�[ID,���V�[�g�ԍ�,�����ԍ�,�x�o�E�^�b�`�E���z,�x�o�E�^�b�`�E�萔��,�x�o�E�\��E���z,�x�o�E�\��E�萔��,�x�o�E�\��E�\��{�[�i�X,�x�o�E�萔�����v,�����E�}�C���g�p" );

            bw.newLine();
            // ���׏o��
            for( int i = 0 ; i < frm.getSlipNoList().size() ; i++ )
            {
                outputCsvRow( bw, i );

            }
            bw.close();
            pw_base.close();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillToDay.createCSV()] Exception:" + e.toString() );
            e.printStackTrace( System.err );
        }
    }

    private void outputCsvRow(BufferedWriter bw, int seq)
    {

        try
        {
            String result = "";

            // ���t
            bw.write( frm.getUsageDateList().get( seq ) + "," );

            // ����
            bw.write( frm.getUsageTimeList().get( seq ) + "," );

            // ���[�U�[ID
            if ( frm.getCustomerIdList().get( seq ) == 0 )
            {
                bw.write( "," );
            }
            else
            {
                bw.write( frm.getCustomerIdList().get( seq ) + "," );
            }
            // ���V�[�g�ԍ�
            if ( frm.getHtSlipNoList().get( seq ) == null || frm.getHtSlipNoList().get( seq ).trim().length() == 0 )
            {
                bw.write( "," );
            }
            else if ( Integer.parseInt( frm.getHtSlipNoList().get( seq ) ) == 0 )
            {
                bw.write( "," );
            }
            else
            {
                bw.write( frm.getHtSlipNoList().get( seq ) + "," );
            }

            // �����ԍ�
            bw.write( frm.getRoomList().get( seq ) + "," );

            // �x�o�E�^�b�`�E���z
            result = frm.getSeisanAmountList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // �x�o�E�^�b�`�E�萔��
            result = frm.getSeisanFeeList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // �x�o�E�\��E���z
            result = frm.getRsvAmountList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // �x�o�E�\��E�萔��
            result = frm.getRsvFeeList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // �x�o�E�\��E�\��{�[�i�X
            result = frm.getRsvBonusList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // �x�o�E�萔�����v
            result = frm.getBillList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // �����E�}�C���g�p
            result = frm.getPayList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            bw.newLine();

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoBillToDay.outputCsvRow()] Exception:" + e.toString() );
        }
    }

}
