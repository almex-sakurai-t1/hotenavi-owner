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
 * 日別請求明細ビジネスロジック
 */
public class LogicOwnerBkoBillToday implements Serializable
{
    private static final long     serialVersionUID = -7916744090550829506L;
    String                        strTitle         = "ハピホテマイル　収支明細";

    private FormOwnerBkoBillToday frm;

    /* フォームオブジェクト */
    public FormOwnerBkoBillToday getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoBillToday frm)
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
        ArrayList<String> billList = new ArrayList<String>(); // 請求
        ArrayList<String> payList = new ArrayList<String>(); // 支払
        NumberFormat formatCur = NumberFormat.getNumberInstance();

        // 売掛ヘッダー情報取得(利用日時、担当、顧客ID、伝票No、部屋)
        getAccountRecvHeader();

        // 内容取得
        getAccountNm();

        // 売掛明細情報取得(金額、請求、支払い)
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

            // ■精算金額の取得
            seisanAmount = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 1 );
            seisanAmountList.add( formatCur.format( seisanAmount ) );

            // ■タッチ手数料の取得
            seisanFee = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 3 );
            seisanFeeList.add( formatCur.format( seisanFee ) );

            // ■予約金額の取得
            rsvAmount = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 2 );
            rsvAmountLvj = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 2 );

            // ■予約手数料の取得
            rsvFee = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 3 );
            rsvFeeLvj = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_141, 3 );

            if ( frm.getRsvKind() == 0 )
            {
                // 全て
                rsvAmountList.add( formatCur.format( rsvAmount + rsvAmountLvj ) );
                rsvFeeList.add( formatCur.format( rsvFee + rsvFeeLvj ) );
            }
            else if ( frm.getRsvKind() == 1 )
            {
                // ハピホテタッチ
                rsvAmountList.add( formatCur.format( rsvAmount ) );
                rsvFeeList.add( formatCur.format( rsvFee ) );
            }
            else
            {
                // ラブイン予約
                rsvAmountList.add( formatCur.format( rsvAmountLvj ) );
                rsvFeeList.add( formatCur.format( rsvFeeLvj ) );
            }

            // ■予約ボーナスの取得
            rsvBonus = getAccountRecvDetail( slipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_150, 3 );
            rsvBonusList.add( formatCur.format( rsvBonus ) );

            // ■支払の取得（アルメックス側で見たときは収入だが、ホテル側から見たときは支払）
            bill = getBillAmount( frm.getSlipNoList().get( i ) );
            paystr = formatCur.format( bill );

            billList.add( paystr );

            // ■収入の取得（アルメックス側で見たときは支払だが、ホテル側から見たときは収入）
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
     * 売掛ヘッダー情報取得
     * 
     * @param なし
     * @return なし
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
                    roomNoList.add( "未" );
                }
                else
                {
                    if ( result.getString( "ht_room_no" ).equals( "0" ) )
                    {
                        roomNoList.add( "未" );
                    }
                    else
                    {
                        roomNoList.add( result.getString( "ht_room_no" ) );
                    }
                }
                billDateList.add( result.getInt( "bill_date" ) );
                userIdList.add( result.getString( "user_id" ) );
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "収支明細" ) );
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
     * 内容取得
     * 
     * @param なし
     * @return なし
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

            // 科目名の整形
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
                            huyoList.add( "○" );
                            siyouList.add( "" );
                            soukyakuList.add( "" );
                            acc200List.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_120:
                            huyoList.add( "" );
                            siyouList.add( "○" );
                            soukyakuList.add( "" );
                            acc200List.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_140:
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_141:
                            huyoList.add( "" );
                            siyouList.add( "" );
                            soukyakuList.add( "○" );
                            acc200List.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_150:
                            huyoList.add( "" );
                            siyouList.add( "" );
                            soukyakuList.add( "○" );
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
                                huyoList.set( idx, "○" );
                                break;
                            case OwnerBkoCommon.ACCOUNT_TITLE_CD_120:
                                siyouList.set( idx, "○" );
                                break;
                            case OwnerBkoCommon.ACCOUNT_TITLE_CD_140:
                                soukyakuList.set( idx, "○" );
                                break;
                            case OwnerBkoCommon.ACCOUNT_TITLE_CD_150:
                                soukyakuList.set( idx, "○" );
                                break;
                            default:
                                acc200List.add( accTitleNmist.get( i ) );
                                break;
                        }
                    }
                }
                else
                {
                    // 値を格納
                    switch( accTitleCdList.get( i ) )
                    {
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_110:
                            huyoList.add( "○" );
                            siyouList.add( "" );
                            soukyakuList.add( "" );
                            acc200List.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_120:
                            huyoList.add( "" );
                            siyouList.add( "○" );
                            soukyakuList.add( "" );
                            acc200List.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_140:
                            huyoList.add( "" );
                            siyouList.add( "" );
                            soukyakuList.add( "○" );
                            acc200List.add( "" );
                            break;
                        case OwnerBkoCommon.ACCOUNT_TITLE_CD_150:
                            huyoList.add( "" );
                            siyouList.add( "" );
                            soukyakuList.add( "○" );
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
     * 売掛明細情報取得
     * 
     * @param int slipNo 売掛伝票番号
     * @param int accTitleCd 科目コード
     * @param int selKbn (1:利用金額取得、2：予約金額取得、3:明細の金額)
     * @return int 金額
     */
    private int getAccountRecvDetail(int slipNo, int accTitleCd, int selKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int amount = 0;

        // 伝票区分の条件文取得

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
     * 請求金額の取得
     * 
     * @param int slipNo 売掛伝票番号
     * @return int 金額
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
            for( int i = 0 ; i < frm.getSlipNoList().size() ; i++ )
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
            doc.add( outputTotal1() );
            doc.add( outputTotal2() );

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
        table.setWidths( new int[]{ 11, 7, 7, 9, 6, 9, 7, 9, 7, 8, 10, 10 } ); // 列幅(%)
        table.setDefaultHorizontalAlignment( Element.ALIGN_CENTER ); // 表示位置（横）
        table.setDefaultVerticalAlignment( Element.ALIGN_MIDDLE ); // 表示位置（縦）
        table.setPadding( 1 ); // 余白
        table.setSpacing( 0 ); // セル間の間隔
        table.setBorderColor( new Color( 0, 0, 0 ) ); // 線の色

        // 1行目
        Cell cell = new Cell( new Phrase( "日付", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 3 );
        cell.setColspan( 2 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "ユーザ\r\nーID", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 3 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "レシート\r\n番号", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 3 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "部屋\r\n番号", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 3 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "支出", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 6 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "収入", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 1 );
        table.addCell( cell );

        // 2行目

        cell = new Cell( new Phrase( "ハピホテタッチ", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 2 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "予約", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setRowspan( 1 );
        cell.setColspan( 3 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "手数料\r\n合計", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 2 );
        cell.setColspan( 1 );
        table.addCell( cell );

        cell = new Cell( new Phrase( "マイル\r\n使用", fontM10 ) );
        cell.setBorderWidthRight( 1f );
        cell.setRowspan( 2 );
        cell.setColspan( 1 );
        table.addCell( cell );

        // 3行目

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
        PdfPTable table = new PdfPTable( 12 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 11, 7, 7, 9, 6, 9, 7, 9, 7, 8, 10, 10 } ); // 列幅(%)

        // 日付
        PdfPCell cell = new PdfPCell( new Phrase( frm.getUsageDateList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_CENTER );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthLeft( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setMinimumHeight( 25f );
        table.addCell( cell );

        // 時刻
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
            // ユーザーID
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

            // レシート番号
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

            // 部屋番号
            cell = new PdfPCell( new Phrase( frm.getRoomList().get( i ), fontM10 ) );
            cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
            cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
            cell.setBorderWidthRight( 1f );
            table.addCell( cell );

            // ハピホテタッチ金額
            cell = new PdfPCell( new Phrase( frm.getSeisanAmountList().get( i ), fontM10 ) );
            cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
            cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
            cell.setBorderWidthRight( 1f );
            table.addCell( cell );

            // ハピホテタッチ手数料
            cell = new PdfPCell( new Phrase( frm.getSeisanFeeList().get( i ), fontM10 ) );
            cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
            cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
            cell.setBorderWidthRight( 1f );
            table.addCell( cell );

            // ハピホテ予約金額
            cell = new PdfPCell( new Phrase( frm.getRsvAmountList().get( i ), fontM10 ) );
            cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
            cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
            cell.setBorderWidthRight( 1f );
            table.addCell( cell );

            // ハピホテ予約手数料
            cell = new PdfPCell( new Phrase( frm.getRsvFeeList().get( i ), fontM10 ) );
            cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
            cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
            cell.setBorderWidthRight( 1f );
            table.addCell( cell );

            // ハピホテ予約ボーナス
            cell = new PdfPCell( new Phrase( frm.getRsvBonusList().get( i ), fontM10 ) );
            cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
            cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
            cell.setBorderWidthRight( 1f );
            table.addCell( cell );

        }

        // 手数料合計
        cell = new PdfPCell( new Phrase( frm.getBillList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        // マイル使用
        cell = new PdfPCell( new Phrase( frm.getPayList().get( i ), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        table.addCell( cell );

        return table;
    }

    /*
     * 【PDF】合計
     */
    private PdfPTable outputTotal1() throws DocumentException, IOException
    {
        Font fontM10 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10 );

        // 合計の出力
        PdfPTable table = new PdfPTable( 3 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 80, 10, 10 } ); // 列幅(%)

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

        // 手数料
        cell = new PdfPCell( new Phrase( frm.getSumBill(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthTop( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // マイル使用
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
     * 【PDF】合計
     */
    private PdfPTable outputTotal2() throws DocumentException, IOException
    {
        Font fontM10 = new Font( BaseFont.createFont( "HeiseiKakuGo-W5", "UniJIS-UCS2-HW-H", BaseFont.NOT_EMBEDDED ), 10 );

        // 合計の出力
        PdfPTable table = new PdfPTable( 3 );
        table.setWidthPercentage( 100f );
        table.setWidths( new int[]{ 80, 10, 10 } ); // 列幅(%)

        // ブランク
        PdfPCell cell = new PdfPCell( new Phrase( "", fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_CENTER );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthLeft( 1f );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        cell.setMinimumHeight( 25f );
        table.addCell( cell );

        // 収支計
        cell = new PdfPCell( new Phrase( "収支計", fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
        cell.setBorderWidthRight( 1f );
        cell.setBorderWidthBottom( 1f );
        table.addCell( cell );

        // マイル使用
        cell = new PdfPCell( new Phrase( frm.getSumSyusi(), fontM10 ) );
        cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
        cell.setVerticalAlignment( Element.ALIGN_BOTTOM );
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
            String filename = "billtoday.csv";
            filename = new String( filename.getBytes( "Shift_JIS" ), "ISO8859_1" );
            filename = ReplaceString.SQLEscape( filename );
            response.setHeader( "Content-Disposition", "attachment;filename=\"" + filename + "\"" );
            response.setContentType( "text/html; charset=Windows-31J" );
            PrintWriter pw_base = response.getWriter();

            // 追記モード
            BufferedWriter bw = new BufferedWriter( pw_base, 10 );

            // 項目をセット
            bw.write( "日付,時刻,ユーザーID,レシート番号,部屋番号,支出・タッチ・金額,支出・タッチ・手数料,支出・予約・金額,支出・予約・手数料,支出・予約・予約ボーナス,支出・手数料合計,収入・マイル使用" );

            bw.newLine();
            // 明細出力
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

            // 日付
            bw.write( frm.getUsageDateList().get( seq ) + "," );

            // 時刻
            bw.write( frm.getUsageTimeList().get( seq ) + "," );

            // ユーザーID
            if ( frm.getCustomerIdList().get( seq ) == 0 )
            {
                bw.write( "," );
            }
            else
            {
                bw.write( frm.getCustomerIdList().get( seq ) + "," );
            }
            // レシート番号
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

            // 部屋番号
            bw.write( frm.getRoomList().get( seq ) + "," );

            // 支出・タッチ・金額
            result = frm.getSeisanAmountList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // 支出・タッチ・手数料
            result = frm.getSeisanFeeList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // 支出・予約・金額
            result = frm.getRsvAmountList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // 支出・予約・手数料
            result = frm.getRsvFeeList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // 支出・予約・予約ボーナス
            result = frm.getRsvBonusList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // 支出・手数料合計
            result = frm.getBillList().get( seq ).replaceAll( ",", "" );
            bw.write( result + "," );

            // 収入・マイル使用
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
