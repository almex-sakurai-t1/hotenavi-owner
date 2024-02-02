/*
 * @(#)HotelBbsMaillist.java 1.00 2008/01/10 Copyright (C) ALMEX Inc. 2007 クチコミメール取得クラス
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelBbs;
import jp.happyhotel.data.DataHotelBbsMaillist;
import jp.happyhotel.data.DataOwnerUser;
import jp.happyhotel.data.DataOwnerUserSecurity;
import jp.happyhotel.data.DataUserBasic;

/**
 * ホテルクチコミ情報取得・更新クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/01/10
 */
public class HotelBbsMaillist implements Serializable
{

    /**
     *
     */
    private static final long      serialVersionUID = -1584452731291572040L;

    private int                    m_bbsMailCount;
    private int                    m_bbsMailAllCount;
    private DataHotelBbsMaillist[] m_hotelBbsMail;

    /**
     * データを初期化します。
     */
    public HotelBbsMaillist()
    {
        m_bbsMailCount = 0;
        m_bbsMailAllCount = 0;
    }

    /** ホテルクチコミ情報件数取得 **/
    public int getBbsMailCount()
    {
        return(m_bbsMailCount);
    }

    /** ホテルクチコミ情報総件数取得 **/
    public int getBbsMailAllCount()
    {
        return(m_bbsMailAllCount);
    }

    /** ホテルクチコミ情報取得 **/
    public DataHotelBbsMaillist[] getHotelBbsMail()
    {
        return(m_hotelBbsMail);
    }

    /**
     * ホテルクチコミ情報取得
     * 
     * @param hotelId ホテルID(0: 全件)
     * @param status ステータス(0:未送信,1:送信済み,2:その他,3全て表示)
     * @param countNum 表示件数
     * @param pagenum　ページ数
     * @param flag 表示フラグ(0:管理者用メール,1:ホテル向けメール,2:ユーザー向けメール,3:全て表示)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getBbsMailList(int hotelId, int status, int countNum, int pageNum, int flag)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_bbs_maillist";
        if ( status >= 0 && status < 3 )
        {
            query = query + " WHERE send_status = ?";
            if ( flag >= 0 && flag < 3 )
            {
                query = query + " AND send_flag = ?";
            }
            if ( hotelId > 0 )
            {
                query = query + " AND id = ?";
            }
        }
        else
        {
            if ( flag >= 0 && flag < 3 )
            {
                query = query + " WHERE send_flag = ?";
                if ( hotelId > 0 )
                {
                    query = query + " AND id = ?";
                }
            }
            else
            {
                if ( hotelId > 0 )
                {
                    query = query + " WHERE id = ?";
                }
            }
        }

        query = query + " ORDER BY regist_date DESC, regist_time DESC";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( status >= 0 && status < 3 )
            {
                prestate.setInt( 1, status );
                if ( flag >= 0 && flag < 3 )
                {
                    prestate.setInt( 2, flag );
                    if ( hotelId > 0 )
                    {
                        prestate.setInt( 3, hotelId );
                    }
                }
                else
                {
                    if ( hotelId > 0 )
                    {
                        prestate.setInt( 2, hotelId );
                    }
                }
            }
            else
            {
                if ( flag >= 0 && flag < 3 )
                {
                    prestate.setInt( 1, flag );
                    if ( hotelId > 0 )
                    {
                        prestate.setInt( 2, hotelId );
                    }
                }
                else
                {
                    if ( hotelId > 0 )
                    {
                        prestate.setInt( 1, hotelId );
                    }
                }
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_bbsMailCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelBbsMail = new DataHotelBbsMaillist[this.m_bbsMailCount];
                for( i = 0 ; i < m_bbsMailCount ; i++ )
                {
                    m_hotelBbsMail[i] = new DataHotelBbsMaillist();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.m_hotelBbsMail[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getBbsMailList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // クチコミメール総件数の取得
        query = "SELECT COUNT(*) FROM hh_hotel_bbs_maillist";
        if ( status >= 0 && status < 3 )
        {
            query = query + " WHERE send_status = ?";
            if ( flag >= 0 && flag < 3 )
            {
                query = query + " AND send_flag = ?";
            }
            if ( hotelId > 0 )
            {
                query = query + " AND id = ?";
            }
        }
        else
        {
            if ( flag >= 0 && flag < 3 )
            {
                query = query + " WHERE send_flag = ?";
                if ( hotelId > 0 )
                {
                    query = query + " AND id = ?";
                }
            }
            else
            {
                if ( hotelId > 0 )
                {
                    query = query + " WHERE id = ?";
                }
            }
        }

        try
        {
            prestate = connection.prepareStatement( query );
            if ( status >= 0 && status < 3 )
            {
                prestate.setInt( 1, status );
                if ( flag >= 0 && flag < 3 )
                {
                    prestate.setInt( 2, flag );
                    if ( hotelId > 0 )
                    {
                        prestate.setInt( 3, hotelId );
                    }
                }
                else
                {
                    if ( hotelId > 0 )
                    {
                        prestate.setInt( 2, hotelId );
                    }
                }
            }
            else
            {
                if ( flag >= 0 && flag < 3 )
                {
                    prestate.setInt( 1, flag );
                    if ( hotelId > 0 )
                    {
                        prestate.setInt( 2, hotelId );
                    }
                }
                else
                {
                    if ( hotelId > 0 )
                    {
                        prestate.setInt( 1, hotelId );
                    }
                }
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // 総件数の取得
                    this.m_bbsMailAllCount = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelBbsMailList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテルクチコミ投稿処理
     * 
     * @param hotelId ホテルID
     * @param bbsSeq クチコミ管理番号
     * @param title タイトル
     * @param body 本文
     * @param sendflag 送信メール区分(0:管理者,1:ホテル,2:ユーザー)
     * @param memo
     * @return 処理結果(0:正常,1:パラメタ異常,2:DB異常,3:その他)
     */
    public int setBbsMailData(int hotelId, int bbsSeq, String title, String body, int sendFlag, String memo)
    {
        final int ANYTIME_FLAG = 9999;
        final int SEND_STATUS_OK = 0;
        final int SEND_STATUS_NG = 2;
        final int SEND_START_TIME = 930;
        final int SEND_END_TIME = 2200;
        int startTime;
        int endTime;
        int sendStatus;
        boolean ret;
        String sendAddr;
        String fromAddr;
        DataHotelBbsMaillist dhbm;
        DataHotelBbs dhb;
        DataUserBasic dub;

        sendAddr = "";
        fromAddr = "kuchikomi@happyhotel.jp";
        sendStatus = 0;
        startTime = ANYTIME_FLAG;
        endTime = ANYTIME_FLAG;
        dhbm = new DataHotelBbsMaillist();
        // 入力値チェック
        if ( hotelId <= 0 || bbsSeq <= 0 || title.compareTo( "" ) == 0 || body.compareTo( "" ) == 0 || (sendFlag <= 0 && sendFlag > 3) || memo == null )
        {
            Logging.info( "[HotelBbsMaillist.setBbsMailData] パラメータが足りない" );
            return(1);
        }

        if ( sendFlag == 0 || sendFlag == 2 )
        {
            if ( sendFlag == 0 )
            {
                sendAddr = "kuchikomi@happyhotel.jp";
                sendStatus = SEND_STATUS_OK;
            }
            else
            {
                dhb = new DataHotelBbs();
                dub = new DataUserBasic();
                ret = dhb.getData( hotelId, bbsSeq );
                if ( ret != false )
                {
                    ret = dub.getData( dhb.getUserId() );
                    if ( ret != false )
                    {
                        if ( dub.getRegistStatus() == 9 && dub.getDelFlag() == 0 )
                        {
                            if ( dub.getMailAddr().compareTo( "" ) != 0 )
                            {
                                sendAddr = dub.getMailAddr();
                            }
                            else
                            {
                                sendAddr = dub.getMailAddrMobile();
                            }
                            if ( dub.getMailStartTime() == 0 )
                                startTime = SEND_START_TIME;
                            else
                                startTime = dub.getMailStartTime();
                            if ( dub.getMailEndTime() == 0 )
                                endTime = SEND_END_TIME;
                            else
                                endTime = dub.getMailEndTime();
                            sendStatus = SEND_STATUS_OK;
                        }
                        else
                        {
                            sendAddr = "";
                            sendStatus = SEND_STATUS_NG;
                            memo = "削除済みか会員登録が不十分";
                            startTime = 0;
                            endTime = 0;
                        }
                    }
                    else
                    {
                        sendAddr = "";
                        sendStatus = SEND_STATUS_NG;
                        memo = "ユーザーIDが存在しない";
                        startTime = 0;
                        endTime = 0;
                    }
                }
                else
                {
                    sendAddr = "";
                    sendStatus = SEND_STATUS_NG;
                    memo = "ホテルIDが存在しない";
                    startTime = 0;
                    endTime = 0;
                }
            }
            // データをセットする
            dhbm.setId( hotelId );
            dhbm.setBbsSeq( bbsSeq );
            dhbm.setToAddr( sendAddr );
            dhbm.setFromAddr( fromAddr );
            dhbm.setTitle( title );
            dhbm.setBody( body );
            dhbm.setSendStatus( sendStatus );
            dhbm.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dhbm.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dhbm.setSendMailStartTime( startTime );
            dhbm.setSendMailEndTime( endTime );
            dhbm.setSendFlag( sendFlag );
            dhbm.setMemo( memo );
            ret = dhbm.insertData();
            if ( ret != false )
            {
                return(0);
            }
            else
            {
                Logging.info( "[HotelBbsMaillist.setBbsMailData] Exception=send_flag=" + sendFlag + ": insert Error" );
                return(2);
            }
        }
        else if ( sendFlag == 1 )
        {
            int count;
            int errCount;
            String query;
            String hotenaviId;
            Connection connection = null;
            PreparedStatement prestate = null;
            ResultSet result = null;
            DataOwnerUser dou;
            DataOwnerUserSecurity dous;
            DataHotelBasic dhbi;

            count = 0;
            dhbi = new DataHotelBasic();
            Logging.info( "[ﾎﾃﾙ送信ﾛｼﾞｯｸ:" + hotelId + "]" );
            ret = dhbi.getData( hotelId );
            if ( ret != false )
            {
                hotenaviId = dhbi.getHotenaviId();

                query = "SELECT DISTINCT owner_user.* FROM owner_user,owner_user_security,owner_user_hotel";
                query += " WHERE  owner_user_hotel.accept_hotelid = '" + hotenaviId + "'";
                query += " AND owner_user.mailaddr_pc <>''";
                query += " AND owner_user_hotel.hotelid = owner_user.hotelid";
                query += " AND owner_user_hotel.userid = owner_user.userid";
                query += " AND owner_user_security.hotelid = owner_user.hotelid";
                query += " AND owner_user_security.userid = owner_user.userid";
                query += " AND owner_user_security.sec_level17 = 1";
                query += " GROUP BY owner_user.mailaddr_pc";
                // テーブルをUNIONで結合させる
                query += " UNION SELECT DISTINCT owner_user.* FROM owner_user,owner_user_security,owner_user_hotel";
                query += " WHERE  owner_user_hotel.accept_hotelid = '" + hotenaviId + "'";
                query += " AND owner_user.mailaddr_mobile <> ''";
                query += " AND owner_user_hotel.hotelid = owner_user.hotelid";
                query += " AND owner_user_hotel.userid = owner_user.userid";
                query += " AND owner_user_security.hotelid = owner_user.hotelid";
                query += " AND owner_user_security.userid = owner_user.userid";
                query += " AND owner_user_security.sec_level18 = 1";
                query += " GROUP BY owner_user.mailaddr_mobile";

                ret = false;
                count = 0;
                errCount = 0;
                Logging.info( "[sql発行]：" + query );
                Logging.info( "[グループID=" + hotenaviId + "] [ホテルID=" + dhbi.getHotenaviId() + "]" );
                try
                {
                    connection = DBConnection.getConnection();
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();
                    if ( result != null )
                    {
                        dou = new DataOwnerUser();
                        dous = new DataOwnerUserSecurity();

                        result.beforeFirst();
                        while( result.next() != false )
                        {
                            // オーナーユーザ情報取得
                            dou.setData( result );
                            // セキュリティ情報の取得
                            dous.getData( result.getString( "hotelid" ), result.getInt( "userid" ) );

                            if ( dou.getMailStartTime() == 0 )
                            {
                                startTime = SEND_START_TIME;
                            }
                            else
                            {
                                startTime = dou.getMailStartTime();
                            }
                            if ( dou.getMailEndTime() == 0 )
                            {
                                endTime = SEND_END_TIME;
                            }
                            else
                            {
                                endTime = dou.getMailEndTime();
                            }
                            if ( dous.getSecLevel17() == 1 && dou.getMailAddrPc().compareTo( "" ) != 0 )
                            {
                                // データをセットする
                                dhbm.setId( hotelId );
                                dhbm.setBbsSeq( bbsSeq );
                                dhbm.setToAddr( dou.getMailAddrPc() );
                                dhbm.setFromAddr( fromAddr );
                                dhbm.setTitle( title );
                                dhbm.setBody( body );
                                dhbm.setSendStatus( sendStatus );
                                dhbm.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                dhbm.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                dhbm.setSendMailStartTime( startTime );
                                dhbm.setSendMailEndTime( endTime );
                                dhbm.setSendFlag( sendFlag );
                                dhbm.setMemo( "ホテナビId：" + hotenaviId + "\t" + memo );
                                ret = dhbm.insertData();
                                if ( ret == false )
                                {
                                    errCount++;
                                }
                                count++;
                            }
                            if ( dous.getSecLevel18() == 1 && dou.getMailAddrMobile().compareTo( "" ) != 0 )
                            {
                                // データをセットする
                                dhbm.setId( hotelId );
                                dhbm.setBbsSeq( bbsSeq );
                                dhbm.setToAddr( dou.getMailAddrMobile() );
                                dhbm.setFromAddr( fromAddr );
                                dhbm.setTitle( title );
                                dhbm.setBody( body );
                                dhbm.setSendStatus( sendStatus );
                                dhbm.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                dhbm.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                dhbm.setSendMailStartTime( startTime );
                                dhbm.setSendMailEndTime( endTime );
                                dhbm.setSendFlag( sendFlag );
                                dhbm.setMemo( "ホテナビId：" + hotenaviId + "\t" + memo );
                                ret = dhbm.insertData();
                                if ( ret == false )
                                {
                                    errCount++;
                                }
                                count++;
                            }
                        }

                        if ( count == 0 )
                        {
                            // データをセットする
                            dhbm.setId( hotelId );
                            dhbm.setBbsSeq( bbsSeq );
                            dhbm.setToAddr( "" );
                            dhbm.setFromAddr( fromAddr );
                            dhbm.setTitle( title );
                            dhbm.setBody( body );
                            dhbm.setSendStatus( SEND_STATUS_NG );
                            dhbm.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            dhbm.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            dhbm.setSendMailStartTime( startTime );
                            dhbm.setSendMailEndTime( endTime );
                            dhbm.setSendFlag( sendFlag );
                            dhbm.setMemo( "ホテナビId：" + hotenaviId + "\t" + memo + " 該当ユーザーがいない" );
                            ret = dhbm.insertData();
                            if ( ret == false )
                            {
                                errCount++;
                            }
                        }

                        if ( errCount == 0 )
                        {
                            return(0);
                        }
                        else
                        {
                            Logging.info( "[HotelBbsMaillist.setBbsMailData] Exception=Send_Flag=2: insert Error" );
                            return(3);
                        }
                    }
                    else
                    {
                        Logging.info( "[HotelBbsMaillist.setBbsMailData] Exception=Send_Flag=2: result=null" );
                        return(2);
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[HotelBbsMaillist.setBbsMailData] Exception=" + e.toString() );
                    return(2);
                }
                finally
                {
                    result = null;
                    prestate = null;
                }
            }
            else
            {
                Logging.info( "[HotelBbsMaillist.setBbsMailData] Exception=hotelId Error" );
                return(3);
            }
        }
        else
        {
            Logging.info( "[HotelBbsMaillist.setBbsMailData] Exception=Send_Flag Error" );
            return(1);
        }
    }
}
