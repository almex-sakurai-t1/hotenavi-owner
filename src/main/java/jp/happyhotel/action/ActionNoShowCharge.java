package jp.happyhotel.action;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.DecodeData;
import jp.happyhotel.common.GMOCcsCredit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataRsvCreditSales;
import jp.happyhotel.data.DataRsvMailRequest;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataRsvReserveBasic;
import jp.happyhotel.data.DataRsvSpid;

/**
 * noshowクレジット処理
 * 
 * @author S.Tashiro
 * @version 2.00 2013/02/22
 */
public class ActionNoShowCharge extends BaseAction
{

    private static final int  TYPE_AUTO_CANCEL   = 1;   // 自動キャンセル&NoShowクレジット対象
    private static final int  TYPE_NOSHOW_CREDIT = 2;   // NoShowクレジット対象
    private static final int  NO_SHOW_ON         = 1;

    private static Properties prop               = null;
    private static String     driver             = "";
    private static String     connurl            = "";
    private static String     user               = "";
    private static String     password           = "";
    private static String     jdbcds             = null;
    private static String     adminAddress       = null;
    private static String     reserveAddress     = null;

    static
    {
        try
        {

            prop = new Properties();
            // ﾌﾟﾛﾊﾟﾃｨﾌｧｲﾙからｷｰと値のﾘｽﾄを読み込みます

            // 事務局のメールアドレスと送り元メールアドレスの取得
            prop.load( new FileInputStream( "/etc/happyhotel/reserveMail.conf" ) );

            adminAddress = prop.getProperty( "admin.address" );
            reserveAddress = prop.getProperty( "reserve.address" );

        }
        catch ( Exception e )
        {
            System.out.println( e.toString() );
        }
    }

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String rsvDate = "";
        String hotelMailText = "";
        String hotelMailTitle = "";
        String imediaMailText = "";
        String imediaMailTitle = "";
        int nowHotelId = 0;
        String hotelId = "";
        ArrayList<String> reserveNoList = new ArrayList<String>();
        ArrayList<Integer> hotelidList = new ArrayList<Integer>();
        ArrayList<String> hotelcancelList = new ArrayList<String>();
        HashMap<String, String> reserveDatetimeHash = new HashMap<String, String>();
        HashMap<String, Integer> typeHash = new HashMap<String, Integer>();
        String rsvNo = "";
        boolean creditflag = true;
        int i;
        int count = 0;
        ServletOutputStream stream;

        try
        {
            i = 1;
            stream = response.getOutputStream();
            rsvDate = request.getParameter( "date" );
            if ( rsvDate == null || rsvDate.equals( "" ) != false || CheckString.numAlphaCheck( rsvDate ) == false )
            {
                rsvDate = "0";
            }

            hotelId = request.getParameter( "id" );
            if ( hotelId == null || hotelId.equals( "" ) != false || CheckString.numAlphaCheck( hotelId ) == false )
            {
                // 指定ホテルID
                hotelId = "0";
            }

            rsvNo = request.getParameter( "rsvNo" );
            if ( rsvNo == null || rsvNo.equals( "" ) != false )
            {
                rsvNo = "";
            }

            // 初期文言セット
            hotelMailText = "";
            imediaMailTitle = "NoShowクレジット結果";

            // MySQLへ接続する
            connection = DBConnection.getConnection();

            // 対象の予約データ取得
            query = "SELECT id, reserve_no,reserve_date,est_time_arrival,status FROM hh_rsv_reserve " +
                    " WHERE status = 3 AND noshow_flag = 1 AND tranid = '' ";

            if ( rsvDate.equals( "0" ) == false )
            {
                query += " AND reserve_date =  ?";

            }
            if ( hotelId.equals( "0" ) == false )
            {
                query += " AND id = ? ";
            }
            if ( rsvNo.equals( "" ) == false )
            {
                query += " AND reserve_no = ? ";
            }
            query += " order by id,reserve_no, reserve_date, est_time_arrival ";

            prestate = connection.prepareStatement( query );
            if ( rsvDate.equals( "0" ) == false )
            {
                prestate.setInt( i++, Integer.parseInt( rsvDate ) );
            }
            if ( hotelId.equals( "0" ) == false )
            {
                prestate.setString( i++, hotelId );
            }
            if ( rsvNo.equals( "" ) == false )
            {
                prestate.setString( i++, rsvNo );
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    reserveNoList.add( result.getString( "reserve_no" ) );
                    hotelidList.add( result.getInt( "id" ) );
                    reserveDatetimeHash.put( result.getString( "reserve_no" ), String.format( "%1$04d年%2$02d月%3$02d日 %4$02d時%5$02d分", result.getInt( "reserve_date" ) / 10000, result.getInt( "reserve_date" ) % 10000 / 100,
                            result.getInt( "reserve_date" ) % 10000 % 100, result.getInt( "est_time_arrival" ) / 10000, result.getInt( "est_time_arrival" ) % 10000 / 100 ) );
                }
            }

            if ( reserveNoList.size() > 0 )
            {
                // 予約番号リストから対象のノーショーチャージを行う
                for( int n = 0 ; n < reserveNoList.size() ; n++ )
                {
                    ret = false;
                    ret = executeNoshowSales( reserveNoList.get( n ) );
                    if ( ret != false )
                    {
                        count++;
                    }
                }
            }

            // 登録完了
            response.setContentType( "text/html" );
            response.setCharacterEncoding( "Shift_JIS" );
            response.setStatus( 200 );

            if ( count > 0 )
            {
                stream.print( "成功：" + reserveNoList.size() + "件中" + count + "件" );
            }
            else
            {
                stream.print( "失敗：" + reserveNoList.size() + "件中" + count + "件" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionNoShowCharge.main] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return;
    }

    /**
     * ノーショー請求処理
     * 
     * @param reserveNo 予約番号
     * 
     * @return 成否(true→成功,false→失敗)
     */
    private static boolean executeNoshowSales(String reserveNo)
    {
        String query = "";
        GMOCcsCredit gmoccs = new GMOCcsCredit();
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String cardno = "";
        int limitdate = 0;
        int hotelid = 0;
        int checkhotelid = 0;
        int amount = 0;
        String frontIp = "";
        boolean ret = false;
        String mailText = "";
        String mailTitle = "";
        String adminAddress = "";
        String reserveAddress = "";
        String senddata;
        String header = "";
        // デフォルト15秒
        int timeout = 15000;
        int retrycount = 0;
        int portno = 0;
        String recvdata = "";
        char cdata[];
        String data = "";

        DataRsvCreditSales dataSales = null;
        DataRsvSpid dataSpid = null;
        DataRsvReserve dataReserve = null;

        try
        {

            // ホスト送信タイムアウト時間
            prop.load( new FileInputStream( "/etc/happyhotel/gmoccscredit.conf" ) );
            if ( CheckString.numCheck( prop.getProperty( "creditdata.hosttimeout" ) ) )
            {
                timeout = Integer.parseInt( prop.getProperty( "creditdata.hosttimeout" ) );
            }
            if ( CheckString.numCheck( prop.getProperty( "creditdata.retrycount" ) ) )
            {
                retrycount = Integer.parseInt( prop.getProperty( "creditdata.retrycount" ) );
            }
            if ( CheckString.numCheck( prop.getProperty( "creditdata.hostportno" ) ) )
            {
                portno = Integer.parseInt( prop.getProperty( "creditdata.hostportno" ) );
            }

            // MySQLへ接続する
            connection = DBConnection.getConnection();

            while( true )
            {
                // 予約番号から対象のクレジットデータ取得
                query = "select card_no, limit_date, id from hh_rsv_credit where reserve_no = ? AND del_flag = 0";
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, reserveNo );
                result = prestate.executeQuery();
                if ( result != null && result.next() != false )
                {

                    cardno = DecodeData.decodeString( "axpol ptmhyeeahl".getBytes( "8859_1" ), "s h t t i s n h ".getBytes( "8859_1" ), new String( result.getString( "card_no" ) ) );
                    // 有効期限
                    limitdate = result.getInt( "limit_date" );
                    // ホテルID
                    hotelid = result.getInt( "id" );
                }

                // クレジットデータのホテルIDから対象のSPID取得
                if ( hotelid <= 0 )
                {
                    break;
                }

                // ホテルIDからSPID情報取得
                dataSpid = new DataRsvSpid();
                if ( dataSpid.getDataByHotelid( connection, hotelid ) == false )
                {
                    Logging.error( "[ActionNoShowCharge.executeNoshowSales] hh_rsv_spid noData hotelid=" + hotelid );
                    break;
                }

                // ホテルのIPアドレス取得
                result.close();
                query = "SELECT hh_hotel_basic.id,hotel.front_ip,hh_hotel_basic.noshow_hotelid FROM hh_hotel_basic,hotel WHERE hh_hotel_basic.id = ? AND hh_hotel_basic.hotenavi_id = hotel.hotel_id";
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, hotelid );
                result = prestate.executeQuery();
                if ( result != null && result.next() != false )
                {
                    if ( result.getInt( "noshow_hotelid" ) > 0 )
                    {
                        int noshowid = result.getInt( "noshow_hotelid" );
                        result.close();
                        // 対象のidでfront_ip再取得
                        query = "SELECT hh_hotel_basic.id,hotel.front_ip FROM hh_hotel_basic,hotel WHERE hh_hotel_basic.id = ? AND hh_hotel_basic.hotenavi_id = hotel.hotel_id";
                        prestate = connection.prepareStatement( query );
                        prestate.setInt( 1, noshowid );
                        result = prestate.executeQuery();
                        if ( result != null && result.next() != false )
                        {
                            frontIp = result.getString( "front_ip" );
                        }
                        checkhotelid = noshowid;
                    }
                    else
                    {
                        frontIp = result.getString( "front_ip" );
                        checkhotelid = hotelid;
                    }
                }
                if ( frontIp.equals( "" ) )
                {
                    break;
                }

                // SPIDの現場データとの一致の確認
                if ( gmoccs.checkSPID( checkhotelid, dataSpid.getSpid(), frontIp ) == false )
                {
                    // 事務局へメール
                    mailTitle = "NoShowクレジットSPID間違い";
                    mailText = "SPIDが間違っています。\r\n";
                    mailText = "------DBデータ------\r\n";
                    mailText += "ホテルID：" + checkhotelid + "\r\n";
                    mailText += "SPID：" + dataSpid.getSpid() + "\r\n";
                    mailText += "frontIp：" + frontIp + "\r\n";
                    // SendMail.send( reserveAddress, adminAddress, mailTitle, mailText );
                    break;
                }

                // ノーショー課金
                // 請求額取得
                amount = getAmount( reserveNo );
                if ( amount <= 0 )
                {
                    Logging.error( "[ActionNoShowCharge.executeNoshowSales] noAmountValue reserveNo=" + reserveNo + "amount=" + amount );
                    break;
                }
                gmoccs.setCardNo( cardno );
                gmoccs.setCardExpire( String.format( "%1$04d", limitdate ) );
                gmoccs.setSpid( dataSpid.getSpid() );
                gmoccs.setAmount( amount );
                if ( gmoccs.execSales() == false )
                {
                    break;
                }
                // 売上データの作成
                dataSales = new DataRsvCreditSales();
                dataSales.setReserve_no( reserveNo );
                dataSales.setSpid( dataSpid.getSpid() );
                dataSales.setSales_date( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dataSales.setGenerate_date( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dataSales.setGenerate_time( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dataSales.setSales_name( dataSpid.getSales_name() );
                dataSales.setReserve_amount( amount );
                dataSales.setAmount( amount );
                dataSales.setPercent( 100 );
                dataSales.setApprove( Integer.valueOf( gmoccs.getApproveNo() ) );
                dataSales.setForward( gmoccs.getForwardCode() );
                dataSales.setTranid( gmoccs.getTranId() );
                dataSales.setCancel_tranid( "" );
                dataSales.setCancel_flag( 0 );
                dataSales.setId( hotelid );
                dataSales.insertData( connection );
                dataSales.setSelectSeqNo();
                // ホストから送信するためのログ吐き出し
                // 予約データにトランザクションIDを更新
                dataReserve = new DataRsvReserve();
                if ( dataReserve.getData( connection, hotelid, reserveNo ) == false )
                {
                    Logging.error( "[ActionNoShowCharge.executeNoshowSales] noDataRsvReserve hotelid=" + hotelid + "reserveNo=" + reserveNo );
                    break;
                }
                dataReserve.setTranid( gmoccs.getTranId() );
                dataReserve.setCancelTranid( "" );
                if ( dataReserve.updateData( connection, hotelid, reserveNo ) == false )
                {
                    Logging.error( "[ActionNoShowCharge.executeNoshowSales] DataRsvReserve UPDATA NG hotelid=" + hotelid + "reserveNo=" + reserveNo + "tranid=" + gmoccs.getTranId() );
                    break;
                }
                ret = true;
                break;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionNoShowCharge.executeNoshowSales] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 
     * 予約請求額取得
     * 
     * @param yyyyymmdd 取得対象予約日
     * 
     * @return 予約番号リスト
     */
    private static int getAmount(String reserveNo)
    {
        int ret = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String driver = "";
        String connurl = "";
        String user = "";
        String password = "";

        try
        {
            // MySQLへ接続する
            connection = DBConnection.getConnection();

            query = "SELECT charge_total FROM hh_rsv_reserve WHERE reserve_no = ?";
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, reserveNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    ret = result.getInt( "charge_total" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionNoShowCharge.getAmount] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * メール送信依頼データ作成
     * 
     * @param connection Connection
     * @param drr 予約データ
     * @return なし
     */
    private static void sendMail(Connection connection, DataRsvReserve drr)
    {
        DataRsvMailRequest data = new DataRsvMailRequest();

        data.setId( drr.getID() );
        data.setReserveNo( drr.getReserveNo() );
        data.setReserveSubNo( drr.getReserveNoSub() );
        if ( drr.getNowShowFlag() == ReserveCommon.NO_SHOW_ON )
        {
            data.setRequestMailKind( ReserveCommon.MAIL_REQ_NOSHOW );
        }
        else
        {
            data.setRequestMailKind( ReserveCommon.MAIL_REQ_CANCELRSV );
        }
        data.setRequestFlag( 0 );
        data.setRegistTermKind( ReserveCommon.TERM_KIND_PC );
        data.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        data.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

        data.insertData( connection );

        // ホテル向けに作成
        data.setRequestMailKind( ReserveCommon.MAIL_REQ_HOTEL_NOSHOW ); // 依頼メール区分
        data.insertData( connection );

        // リマインダが"ON"の場合、さらに１件追加する
        if ( drr.getReminderFlag() == 1 )
        {
            data.setRequestMailKind( ReserveCommon.MAIL_REQ_REMINDAR ); // 依頼メール区分
            data.insertData( connection );
        }
    }

    /**
     * 
     * 予約データの取得
     * 
     * @param hotelid ホテルID
     * @param reserveNo 予約番号
     * @throws Exception
     */
    private static DataRsvReserve getReserveData(int hotelid, String reserveNo)
    {
        DataRsvReserve ret = new DataRsvReserve();
        Connection connection = null;

        try
        {
            connection = DBConnection.getConnection(); // データの取得
            ret.getData( connection, hotelid, reserveNo );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionNoShowCharge.getReserveData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /***
     * ノーショークレジット有効ホテル判定処理
     * 
     * @param hotelid ホテルID
     * @return 判定結果(true→有効,false→無効)
     **/
    public static boolean checkNoShowCreditHotel(int hotelid)
    {
        boolean ret = false;

        DataRsvReserveBasic drrb = new DataRsvReserveBasic();
        ret = drrb.getData( hotelid );
        if ( ret != false )
        {
            if ( drrb.getNoshow_credit_flag() == 1 )
            {
                ret = true;
            }
            else
            {
                ret = false;
            }
        }

        return(ret);
    }
}
