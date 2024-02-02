/*
 * @(#)UserPointPay.java 1.00 2007/08/23 Copyright (C) ALMEX Inc. 2007 ユーザポイント取得・更新クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelCi;
import jp.happyhotel.data.DataHotelHappie;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.data.DataUserPointPay;

/**
 * 有料ユーザーポイント
 * 
 * @author S.Tashiro
 * @version 1.00 2009/08/07
 */
public class UserPointPay implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = 4658546477538933384L;

    private int                userPointCount;
    private DataUserPointPay[] userPoint;
    private int                expiredPoint;
    private int                expiredPointNextMonth;
    private String             expiredMonth;
    private String             expiredNextMonth;

    /**
     * データを初期化します。
     */
    public UserPointPay()
    {
        userPointCount = 0;
        expiredPoint = 0;
        expiredPointNextMonth = 0;
        expiredMonth = "";
        expiredNextMonth = "";
    }

    public DataUserPointPay[] getUserPoint()
    {
        return userPoint;
    }

    public int getUserPointCount()
    {
        return userPointCount;
    }

    public int getExpiredPoint()
    {
        return expiredPoint;
    }

    public String getExpiredPointFormat()
    {
        String strExpiredPoint = "";
        NumberFormat nfComma;
        nfComma = NumberFormat.getInstance();
        strExpiredPoint = nfComma.format( expiredPoint );
        return strExpiredPoint;
    }

    public int getExpiredPointNext()
    {
        return expiredPointNextMonth;
    }

    public String getExpiredPointNextFormat()
    {
        String strExpiredPointNext = "";
        NumberFormat nfComma;
        nfComma = NumberFormat.getInstance();
        strExpiredPointNext = nfComma.format( expiredPointNextMonth );
        return strExpiredPointNext;
    }

    public String getExpiredMonth()
    {
        return expiredMonth;
    }

    public String getExpiredMonthNext()
    {
        return expiredNextMonth;
    }

    /**
     * ユーザポイントを取得する（IDから･日付最新順･範囲/件数を指定）
     * 
     * @param userId ユーザID
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @see 開始日は会員登録日以降を表示
     */
    public boolean getPointList(String userId, int countNum, int pageNum)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic";

        if ( userId.compareTo( "" ) != 0 )
        {
            query += " WHERE hh_user_point_pay.user_id = ?";
            query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
            query += " AND hh_user_basic.regist_status = 9";
            query += " AND hh_user_basic.del_flag = 0";
            // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
            query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";
        }
        else
        {
            return(false);
        }
        query += " ORDER BY get_date DESC, get_time DESC";
        if ( countNum > 0 )
        {
            query += " LIMIT " + (pageNum * countNum) + "," + countNum;
        }
        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getPointListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPay.getPointList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ユーザポイントのデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getPointListSub(PreparedStatement prestate)
    {
        int i;
        int count;
        ResultSet result = null;

        count = 0;

        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    userPointCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.userPoint = new DataUserPointPay[this.userPointCount];
                for( i = 0 ; i < userPointCount ; i++ )
                {
                    userPoint[i] = new DataUserPointPay();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ユーザポイント情報の取得
                    this.userPoint[count++].setData( result );
                }

            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPay.getUserPointSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userPointCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ポイントマスタ取得処理
     * 
     * @param pointKind ポイント種別
     * @param userId ユーザID
     * @param dmp ポイント管理マスタクラス
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMasterPoint(String userId, int pointKind, DataMasterPoint dmp)
    {
        int checkKind;
        int limitDate;
        int startDate;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_point WHERE kind = ?";

        ret = false;
        checkKind = 0;
        limitDate = 0;
        startDate = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setInt( 1, pointKind );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        dmp.setData( result );
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPay.getMasterPoint] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        // そのポイントが加算対象かチェックする
        if ( ret != false )
        {
            ret = false;

            if ( dmp.getLimitFlag() == 0 )
            {
                ret = true;
                // DBのクローズ処理
                DBConnection.releaseResources( connection );
            }
            else if ( dmp.getLimitFlag() == 1 )
            {
                // 本日のポイント取得履歴を取得
                query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic";
                query += " WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                query += " AND hh_user_point_pay.get_date = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";

                ret = true;
                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // もしデータがあった場合は加算対象としない
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPoint] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            else if ( dmp.getLimitFlag() == 2 )
            {
                // 過去のポイントを取得
                query = "SELECT  hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";

                ret = true;
                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // もしデータがあった場合は加算対象としない
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPoint] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            // 時間
            else if ( dmp.getLimitFlag() > 2 && dmp.getLimitFlag() < 6 )
            {
                if ( dmp.getLimitFlag() == 3 )
                {
                    checkKind = 3;
                    // 時間を操作するメソッドがないため日にちに換算
                    limitDate = dmp.getRange() / 24;
                    if ( dmp.getRange() % 24 > 0 )
                    {
                        limitDate++;
                    }
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addDay( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 4 )
                {
                    checkKind = 2;
                    limitDate = dmp.getRange();
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addDay( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 5 )
                {
                    checkKind = 1;
                    limitDate = dmp.getRange();
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addMonth( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }

                // 過去のポイントを獲得
                query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic";
                query += " WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay.get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";

                query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
                query += " limit 0,1";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                if ( result.getInt( "get_date" ) > 0 )
                                {
                                    // 来店ハピー追加から○時間以内ならtrue、それ以外ならfalse
                                    ret = DateEdit.isValidDate( result.getInt( "get_date" ), result.getInt( "get_time" ), checkKind, Math.abs( dmp.getRange() ) );
                                    // ○時間以内は来店ハピーを追加させないので、retの値を逆にしてリターン
                                    return(!ret);
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
        }
        else
        {
            // DBのクローズ処理
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ポイントマスタ取得処理(付帯番号対応）
     * 
     * @param userId ユーザID
     * @param pointKind ポイント種別
     * @param extCode 付帯番号
     * @param dmp ポイント管理マスタクラス
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMasterPointExt(String userId, int pointKind, int extCode, DataMasterPoint dmp)
    {
        int checkKind;
        int limitDate;
        int startDate;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_point WHERE kind = ?";

        ret = false;
        checkKind = 0;
        limitDate = 0;
        startDate = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setInt( 1, pointKind );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        dmp.setData( result );
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        // そのポイントが加算対象かチェックする
        if ( ret != false )
        {
            ret = false;

            if ( dmp.getLimitFlag() == 0 )
            {
                ret = true;
                // DBのクローズ処理
                DBConnection.releaseResources( connection );
            }
            else if ( dmp.getLimitFlag() == 1 )
            {
                // 本日のポイント取得履歴を取得
                query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                query += " AND hh_user_point_pay.get_date = ?";
                query += " AND hh_user_point_pay.ext_code = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";

                ret = true;
                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        prestate.setInt( 5, extCode );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // もしデータがあった場合は加算対象としない
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            else if ( dmp.getLimitFlag() == 2 )
            {
                // 過去のポイントを取得
                query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                query += " AND hh_user_point_pay.ext_code = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, extCode );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // もしデータがあった場合は加算対象としない
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            // 時間
            else if ( dmp.getLimitFlag() > 2 && dmp.getLimitFlag() < 6 )
            {
                if ( dmp.getLimitFlag() == 3 )
                {
                    checkKind = 3;
                    // 時間を操作するメソッドがないため日にちに換算
                    limitDate = dmp.getRange() / 24;
                    if ( dmp.getRange() % 24 > 0 )
                    {
                        limitDate++;
                    }
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addDay( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 4 )
                {
                    checkKind = 2;
                    limitDate = dmp.getRange();
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addDay( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 5 )
                {
                    checkKind = 1;
                    limitDate = dmp.getRange();
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addMonth( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }

                // 過去のポイントを獲得
                query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic";
                query += " WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                query += " AND hh_user_point_pay.ext_code = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay.get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";
                query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
                query += " limit 0,1";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, extCode );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                if ( result.getInt( "get_date" ) > 0 )
                                {
                                    // 来店ハピー追加から○時間以内ならtrue、それ以外ならfalse
                                    ret = DateEdit.isValidDate( result.getInt( "get_date" ), result.getInt( "get_time" ), checkKind, Math.abs( dmp.getRange() ) );
                                    // ○時間以内は来店ハピーを追加させないので、retの値を逆にしてリターン
                                    return(!ret);
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
        }
        else
        {
            // DBのクローズ処理
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ポイントマスタ取得処理(付帯番号・付帯文字対応）
     * 
     * @param pointKind ポイント種別
     * @param userId ユーザID
     * @param extCode 付帯番号
     * @param dmp ポイント管理マスタクラス
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMasterPointExtString(String userId, int pointKind, int extCode, String extString, DataMasterPoint dmp)
    {
        int checkKind;
        int limitDate;
        int startDate;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_point WHERE kind = ?";

        ret = false;
        checkKind = 0;
        limitDate = 0;
        startDate = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setInt( 1, pointKind );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        dmp.setData( result );
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPay.getMasterPointExtString] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        // そのポイントが加算対象かチェックする
        if ( ret != false )
        {
            ret = false;

            if ( dmp.getLimitFlag() == 0 )
            {
                ret = true;
                DBConnection.releaseResources( connection );
            }
            else if ( dmp.getLimitFlag() == 1 )
            {
                // 本日のポイント取得履歴を取得
                query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                query += " AND hh_user_point_pay.get_date = ?";
                query += " AND hh_user_point_pay.ext_code = ?";
                query += " AND hh_user_point_pay.ext_string = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";

                ret = true;
                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        prestate.setInt( 5, extCode );
                        prestate.setString( 6, extString );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // もしデータがあった場合は加算対象としない
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPointExtString] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            else if ( dmp.getLimitFlag() == 2 )
            {

                // 過去のポイントを取得
                query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                query += " AND hh_user_point_pay.ext_code = ?";
                query += " AND hh_user_point_pay.ext_string = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, extCode );
                        prestate.setString( 5, extString );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // もしデータがあった場合は加算対象としない
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPointExtString] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            // 時間
            else if ( dmp.getLimitFlag() > 2 && dmp.getLimitFlag() < 6 )
            {
                if ( dmp.getLimitFlag() == 3 )
                {
                    checkKind = 3;
                    // 時間を操作するメソッドがないため日にちに換算
                    limitDate = dmp.getRange() / 24;
                    if ( dmp.getRange() % 24 > 0 )
                    {
                        limitDate++;
                    }
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addDay( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 4 )
                {
                    checkKind = 2;
                    limitDate = dmp.getRange();
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addDay( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 5 )
                {
                    checkKind = 1;
                    limitDate = dmp.getRange();
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addMonth( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }

                // 過去のポイントを獲得
                query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic";
                query += " WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                query += " AND hh_user_point_pay.ext_code = ?";
                query += " AND hh_user_point_pay.ext_string = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay.get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";

                query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
                query += " limit 0,1";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, extCode );
                        prestate.setString( 5, extString );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                if ( result.getInt( "get_date" ) > 0 )
                                {
                                    // 来店ハピー追加から○時間以内ならtrue、それ以外ならfalse
                                    ret = DateEdit.isValidDate( result.getInt( "get_date" ), result.getInt( "get_time" ), checkKind, Math.abs( dmp.getRange() ) );
                                    // ○時間以内は来店ハピーを追加させないので、retの値を逆にしてリターン
                                    return(!ret);
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
        }
        else
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ポイントマスタ取得処理(付帯番号対応）
     * 
     * @param userId ユーザID
     * @param pointKind ポイント種別
     * @param code ポイントコード
     * @param extCode 付帯番号
     * @param dmp ポイント管理マスタクラス
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMasterPointExtNum(String userId, int pointKind, int code, int extCode, DataMasterPoint dmp)
    {
        int checkKind;
        int limitDate;
        int startDate;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_point WHERE kind = ? AND code=?";

        ret = false;
        checkKind = 0;
        limitDate = 0;
        startDate = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setInt( 1, pointKind );
                prestate.setInt( 2, code );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        dmp.setData( result );
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        // そのポイントが加算対象かチェックする
        if ( ret != false )
        {
            ret = false;

            if ( dmp.getLimitFlag() == 0 )
            {
                ret = true;
                // DBコネクションのクローズ処理
                DBConnection.releaseResources( connection );
            }
            else if ( dmp.getLimitFlag() == 1 )
            {
                // 本日のポイント取得履歴を取得
                query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                query += " AND hh_user_point_pay.get_date = ?";
                query += " AND hh_user_point_pay.ext_code = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";

                ret = true;
                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        prestate.setInt( 5, extCode );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // もしデータがあった場合は加算対象としない
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            else if ( dmp.getLimitFlag() == 2 )
            {
                // 過去のポイントを取得
                query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                query += " AND hh_user_point_pay.ext_code = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, extCode );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // もしデータがあった場合は加算対象としない
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            // 時間
            else if ( dmp.getLimitFlag() > 2 && dmp.getLimitFlag() < 6 )
            {
                if ( dmp.getLimitFlag() == 3 )
                {
                    checkKind = 3;
                    // 時間を操作するメソッドがないため日にちに換算
                    limitDate = dmp.getRange() / 24;
                    if ( dmp.getRange() % 24 > 0 )
                    {
                        limitDate++;
                    }
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addDay( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 4 )
                {
                    checkKind = 2;
                    limitDate = dmp.getRange();
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addDay( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 5 )
                {
                    checkKind = 1;
                    limitDate = dmp.getRange();
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addMonth( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }

                // 過去のポイントを獲得
                query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic";
                query += " WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                query += " AND hh_user_point_pay.ext_code = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay.get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";
                query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
                query += " limit 0,1";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, extCode );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                if ( result.getInt( "get_date" ) > 0 )
                                {
                                    // 来店ハピー追加から○時間以内ならtrue、それ以外ならfalse
                                    ret = DateEdit.isValidDate( result.getInt( "get_date" ), result.getInt( "get_time" ), checkKind, Math.abs( dmp.getRange() ) );
                                    // ○時間以内は来店ハピーを追加させないので、retの値を逆にしてリターン
                                    return(!ret);
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
        }
        else
        {
            // DBのクローズ処理
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ポイントマスタ取得処理(付帯番号対応）
     * 
     * @param userId ユーザID
     * @param pointKind ポイント種別
     * @param code ポイントコード
     * @param extCode 付帯番号
     * @param dmp ポイント管理マスタクラス
     * @param date 日付
     * @param time 時刻
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterPointExtNum(String userId, int pointKind, int code, int extCode, DataMasterPoint dmp, int date, int time)
    {
        int checkKind;
        int limitDate;
        int startDate;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_point WHERE kind = ? AND code=?";

        ret = false;
        checkKind = 0;
        limitDate = 0;
        startDate = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setInt( 1, pointKind );
                prestate.setInt( 2, code );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        dmp.setData( result );
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        // そのポイントが加算対象かチェックする
        if ( ret != false )
        {
            ret = false;

            if ( dmp.getLimitFlag() == 0 )
            {
                ret = true;
                // DBコネクションのクローズ処理
                DBConnection.releaseResources( connection );
            }
            else if ( dmp.getLimitFlag() == 1 )
            {
                // 本日のポイント取得履歴を取得
                query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                query += " AND hh_user_point_pay.get_date = ?";
                query += " AND hh_user_point_pay.ext_code = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";

                ret = true;
                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, date );
                        prestate.setInt( 5, extCode );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // もしデータがあった場合は加算対象としない
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            else if ( dmp.getLimitFlag() == 2 )
            {
                // 過去のポイントを取得
                query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                query += " AND hh_user_point_pay.ext_code = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, extCode );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // もしデータがあった場合は加算対象としない
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            // 時間
            else if ( dmp.getLimitFlag() > 2 && dmp.getLimitFlag() < 6 )
            {
                if ( dmp.getLimitFlag() == 3 )
                {
                    checkKind = 3;
                    // 時間を操作するメソッドがないため日にちに換算
                    limitDate = dmp.getRange() / 24;
                    if ( dmp.getRange() % 24 > 0 )
                    {
                        limitDate++;
                    }
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addDay( date, -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 4 )
                {
                    checkKind = 2;
                    limitDate = dmp.getRange();
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addDay( date, -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 5 )
                {
                    checkKind = 1;
                    limitDate = dmp.getRange();
                    // データの取得開始日を算出
                    startDate = Integer.valueOf( DateEdit.addMonth( date, -1 * Math.abs( limitDate ) ) );
                }

                // 過去のポイントを獲得
                query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic";
                query += " WHERE hh_user_point_pay.user_id = ?";
                query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay.code = ?";
                query += " AND hh_user_point_pay.point_kind = ?";
                query += " AND hh_user_point_pay.ext_code = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay.get_date <= " + date;
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";
                query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
                query += " limit 0,1";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, extCode );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                if ( result.getInt( "get_date" ) > 0 )
                                {
                                    // 来店ハピー追加から○時間以内ならtrue、それ以外ならfalse
                                    ret = DateEdit.isValidDate( result.getInt( "get_date" ), result.getInt( "get_time" ), date, time, checkKind, Math.abs( dmp.getRange() ) );
                                    // ○時間以内は来店ハピーを追加させないので、retの値を逆にしてリターン
                                    return(!ret);
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
        }
        else
        {
            // DBのクローズ処理
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ユーザポイントを取得する（IDから）
     * 
     * @param userId ユーザID
     * @param nowMonth 今月獲得数フラグ(true:今月獲得数)
     * @return ポイント数
     */
    public int getNowPoint(String userId, boolean nowMonth)
    {
        int point;
        int pointCollect;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        UserBasicInfo ubi;

        if ( userId.compareTo( "" ) == 0 )
        {
            return(0);
        }

        point = 0;
        pointCollect = 0;
        // ユーザ情報の取得
        ubi = new UserBasicInfo();
        ubi.getUserBasic( userId );

        // マイル表示日の確認を行う。
        this.modifyPointPayUpdate( ubi.getUserInfo() );
        if ( nowMonth != false )
        {
            query = "SELECT SUM(hh_user_point_pay.point) FROM hh_user_point_pay, hh_user_basic";
            query += " WHERE hh_user_point_pay.user_id = ?";
            query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id ";
            query += " AND hh_user_basic.regist_status = 9";
            query += " AND hh_user_basic.del_flag = 0";
            // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
            query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";
        }
        else
        {
            try
            {
                pointCollect = ubi.getUserInfo().getPointPay();
            }
            catch ( Exception e )
            {
                pointCollect = 0;
            }

            // 最終集計日以降のもののみ取得する
            query = "SELECT SUM( hh_user_point_pay.point ) FROM hh_user_point_pay, hh_user_basic";
            query += " WHERE hh_user_point_pay.user_id = ?";
            query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id ";
            query += " AND hh_user_basic.regist_status = 9";
            query += " AND hh_user_basic.del_flag = 0";
            // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
            query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";
        }
        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );

            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    point = result.getInt( 1 );

                    if ( nowMonth == false )
                    {
                        point = point + pointCollect;
                    }
                }
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPay.getNowPointPayMember] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(point);
    }

    /**
     * ユーザポイントを取得する（チェックインデータから）
     * 
     * @param DataHotelCi hh_hotel_ciのデータクラス
     * @return ポイント数
     */
    public int getNowPoint(DataHotelCi dhc)
    {
        int point = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( dhc.getUserId().compareTo( "" ) == 0 )
        {
            return(0);
        }
        if ( dhc.getCiStatus() != 1 )
        {
            return(getNowPoint( dhc.getUserId(), false ));
        }

        query = "SELECT then_point FROM hh_user_point_pay_temp ";
        query += " WHERE ext_code = ? ";
        query += " AND user_id = ? ";
        query += " AND user_seq = ? ";
        query += " AND visit_seq = ? ";
        query += " ORDER BY seq ";
        query += " LIMIT 0,1";
        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, dhc.getId() );
            prestate.setString( 2, dhc.getUserId() );
            prestate.setInt( 3, dhc.getUserSeq() );
            prestate.setInt( 4, dhc.getVisitSeq() );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    point = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPay.getNowPoint(DataHotelCi dhc)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(point);
    }

    /**
     * ポイントを加減する
     * 
     * @param userId ユーザーID
     * @param pointCode ポイントコード
     * @param formId フォームID
     * @param memo メモ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPoint(String userId, int pointCode, int formId, String memo)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPointPay dup;

        dmp = new DataMasterPoint();
        dup = new DataUserPointPay();
        ret = dmp.getData( pointCode );
        if ( ret == false )
        {
            return false;
        }

        ret = getMasterPointExtNum( userId, dmp.getKind(), pointCode, formId, dmp );
        if ( ret != false )
        {
            try
            {
                dup = new DataUserPointPay();
                dup.setUserId( userId );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setCode( dmp.getCode() );
                // ポイントの加減をチェック
                if ( dmp.getAdjustmentFlag() == 0 )
                {
                    dup.setPoint( dmp.getAddPoint() );
                }
                else if ( dmp.getAdjustmentFlag() == 1 )
                {
                    dup.setPoint( -dmp.getAddPoint() );
                }
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( formId );
                dup.setMemo( memo );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPay.setPointPresent ] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * ポイントを加減する
     * 
     * @param userId ユーザーID
     * @param pointCode ポイントコード
     * @param formId フォームID
     * @param memo メモ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setRegistPoint(String userId, int pointCode, int formId, String memo)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPointPay dup;

        dmp = new DataMasterPoint();
        dup = new DataUserPointPay();
        ret = dmp.getData( pointCode );
        if ( ret == false )
        {
            return false;
        }

        ret = getRegistPointPay( userId, dmp.getKind(), pointCode, formId, dmp );
        if ( ret != false )
        {
            try
            {
                dup = new DataUserPointPay();
                dup.setUserId( userId );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setCode( dmp.getCode() );
                // ポイントの加減をチェック
                if ( dmp.getAdjustmentFlag() == 0 )
                {
                    dup.setPoint( dmp.getAddPoint() );
                }
                else if ( dmp.getAdjustmentFlag() == 1 )
                {
                    dup.setPoint( -dmp.getAddPoint() );
                }
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( formId );
                dup.setMemo( memo );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPay.setPointPresent ] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * 有料入会ポイント取得処理
     * 
     * @param userId ユーザID
     * @param pointKind ポイント種別
     * @param code ポイントコード
     * @param extCode 付帯番号
     * @param dmp ポイント管理マスタクラス
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getRegistPointPay(String userId, int pointKind, int code, int extCode, DataMasterPoint dmp)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_point WHERE kind = ? AND code=?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setInt( 1, pointKind );
                prestate.setInt( 2, code );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        dmp.setData( result );
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        // そのポイントが加算対象かチェックする
        if ( ret != false )
        {
            ret = false;

            // 退会日以降にデータがあった場合は追加しない
            query = "SELECT hh_user_point_pay.* FROM hh_user_point_pay, hh_user_basic WHERE hh_user_point_pay.user_id = ?";
            query += " AND hh_user_point_pay.user_id = hh_user_basic.user_id";
            query += " AND hh_user_basic.regist_status = 9";
            query += " AND hh_user_basic.del_flag = 0";
            query += " AND hh_user_point_pay.code = ?";
            query += " AND hh_user_point_pay.point_kind = ?";
            // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
            query += " AND hh_user_point_pay.get_date >= hh_user_basic.point_pay_update ";
            query += " AND hh_user_point_pay.ext_code = ?";

            ret = true;
            try
            {
                prestate = connection.prepareStatement( query );
                if ( prestate != null )
                {
                    prestate.setString( 1, userId );
                    prestate.setInt( 2, dmp.getCode() );
                    prestate.setInt( 3, pointKind );
                    prestate.setInt( 4, extCode );
                    result = prestate.executeQuery();
                    if ( result != null )
                    {
                        if ( result.next() != false )
                        {
                            // もしデータがあった場合は加算対象としない
                            ret = false;
                        }
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPay.getMasterPointExt] Exception=" + e.toString() );
                ret = false;
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }
        }
        else
        {
            // DBのコネクションを閉じる
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 最大値取得
     * 
     * @param userId ユーザID
     * @return seq
     */
    public int getMaxSeq(String userId)
    {
        int nMaxSeq = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            // 来店ハピーまたは、ハピー付与履歴の中でMAXのものを取得
            connection = DBConnection.getConnection();
            query = "SELECT MAX(seq) FROM hh_user_point_pay";
            query += " WHERE user_id = ?";

            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setString( 1, userId );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        nMaxSeq = result.getInt( 1 );
                    }
                }

            }

        }
        catch ( Exception e )
        {
            Logging.error( "[UserPointPay().getMaxSeq()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(nMaxSeq);
    }

    /****
     * 来店ハピーの追加（付与ハピー付与時に追加）
     * 
     * @param userId ユーザーID
     * @param pointCode ポイントコード
     * @param thenPoint 当時ポイント
     * @param employeeCode 従業員コード
     * @param dhc チェックインデータ
     * @return
     */
    public int setVisitPoint(String userId, int pointCode, int thenPoint, int employeeCode, DataHotelCi dhc)
    {
        int addPoint;
        int result;
        boolean ret;
        DataMasterPoint dmp;
        DataUserPointPay dup;
        DataHotelHappie dhh;
        DataHotelBasic dhb;

        addPoint = 0;
        result = 0;
        dmp = new DataMasterPoint();
        dup = new DataUserPointPay();
        dhh = new DataHotelHappie();
        ret = dmp.getData( pointCode );
        Logging.info( "[UserPointPayTemp.setVisitPoint()]:" + ret );
        if ( ret == false )
        {
            return -1;
        }

        dhb = new DataHotelBasic();
        ret = dhb.getData( dhc.getId() );
        if ( ret == false )
        {
            result = -1;
            return result;
        }
        else
        {
            // TODO
            if ( dhb.getRank() < 3 )
            {
                // result = -1;
                // return result;
            }
        }

        ret = getMasterPointExtNum( userId, dmp.getKind(), pointCode, dhc.getId(), dmp, dhc.getCiDate(), dhc.getCiTime() );
        if ( ret != false )
        {
            addPoint = dmp.getAddPoint();
            // 加減フラグが1だったら減算なのでマイナスを行う
            if ( dmp.getAdjustmentFlag() == 1 )
            {
                addPoint = Math.abs( addPoint ) * -1;
            }

            // 無料会員だったら無料の倍率をかける
            if ( dhc.getUserType() == 1 )
            {
                addPoint = (int)(addPoint * dmp.getFreeMultiple());
            }

            // 加算するハピーの倍率をhh_hotel_happieから取得
            dhh.getData( dhc.getId() );

            // 来店ハピーの倍率をチェック
            if ( dhh.getComePointMultiple() > 1 )
            {
                addPoint = addPoint * dhh.getComePointMultiple();
            }

            try
            {
                dup = new DataUserPointPay();
                dup.setUserId( userId );
                dup.setGetDate( dhc.getCiDate() );
                dup.setGetTime( dhc.getCiTime() );
                dup.setCode( dmp.getCode() );
                dup.setPoint( addPoint );
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( dhc.getId() );
                dup.setMemo( "" );
                dup.setIdm( dhc.getIdm() );
                dup.setUserSeq( dhc.getUserSeq() );
                dup.setVisitSeq( dhc.getVisitSeq() );
                dup.setThenPoint( thenPoint );
                dup.setHotenaviId( dhb.getHotenaviId() );
                // dupt.setEmployeeCode( 0 );
                dup.setUserType( dhc.getUserType() );

                // 予約関連で追加
                dup.setSlipNo( dhc.getSlipNo() );
                dup.setRoomNo( dhc.getRoomNo() );
                dup.setAmount( dhc.getAmount() );
                dup.setExtString( dhc.getRsvNo() );

                ret = dup.insertData();
                Logging.info( "[UserPointPay dup.insertData()]:" + ret );
                if ( ret != false )
                {
                    result = addPoint;
                }
                else
                {
                    result = -1;
                }

            }
            catch ( Exception e )
            {
                result = -1;
                Logging.info( "[UserPointPay.setPointPresent ] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        else
        {
            result = 0;
        }
        return(result);
    }

    /**
     * ハピー利用履歴追加
     * 
     * @param userId ユーザID
     * @param code ポイントコード
     * @param extCode 付帯番号
     * @param point 利用ポイント
     * @param idm フェリカID
     * @param userSeq ユーザ管理番号
     * @param visitSeq 来店回数
     * @param thenPoint その当時のポイント
     * @param hotenaviId ホテナビID
     * @param employeeCode 従業員コード
     * @return 処理結果(true:成功、false:失敗)
     */
    public boolean setUsePoint(String userId, int pointCode, int thenPoint, int point, int employeeCode, DataHotelCi dhc)
    {
        int nowPoint;
        boolean ret = false;
        DataMasterPoint dmp;
        DataUserPointPay dup;

        nowPoint = 0;
        dmp = new DataMasterPoint();
        dmp.getData( pointCode );

        ret = this.getMasterPointExtNum( userId, dmp.getKind(), dmp.getCode(), dhc.getId(), dmp );
        if ( ret != false )
        {
            try
            {
                nowPoint = this.getNowPoint( userId, false );
                // if ( nowPoint >= Math.abs( (dmp.getAddPoint() * point) ) )
                // {
                dup = new DataUserPointPay();
                dup.setUserId( userId );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setCode( dmp.getCode() );
                // ポイントの加減をチェック
                if ( dmp.getAdjustmentFlag() == 0 )
                {
                    dup.setPoint( Math.abs( (dmp.getAddPoint() * point) ) );
                }
                else if ( dmp.getAdjustmentFlag() == 1 )
                {
                    dup.setPoint( Math.abs( (dmp.getAddPoint() * point) ) * -1 );
                }
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( dhc.getId() );
                dup.setMemo( "" );
                dup.setIdm( dhc.getIdm() );
                dup.setUserSeq( dhc.getUserSeq() );
                dup.setVisitSeq( dhc.getVisitSeq() );
                dup.setRoomNo( dhc.getRoomNo() );
                dup.setThenPoint( thenPoint );
                dup.setHotenaviId( dhc.getVisitHotenaviId() );
                dup.setEmployeeCode( employeeCode );
                // ユーザタイプをセット
                dup.setUserType( dhc.getUserType() );

                ret = dup.insertData();
                // }
                // else
                // {
                // ret = false;
                // }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPay.setUsePoint] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * ハピー利用履歴追加
     * 
     * @param userId ユーザID
     * @param pointCode ポイントコード
     * @param extCode 付帯番号
     * @param point 利用ポイント
     * @param idm フェリカID
     * @param userSeq ユーザ管理番号
     * @param visitSeq 来店回数
     * @param thenPoint その当時のポイント
     * @param hotenaviId ホテナビID
     * @param employeeCode 従業員コード
     * @return 処理結果(true:成功、false:失敗)
     */
    public boolean setUsePointUpdate(String userId, int pointCode, int thenPoint, int point, int employeeCode, DataHotelCi dhc)
    {
        boolean ret = false;
        DataMasterPoint dmp;
        DataUserPointPay dup;

        dmp = new DataMasterPoint();
        dmp.getData( pointCode );

        ret = this.getMasterPointExtNum( userId, dmp.getKind(), dmp.getCode(), dhc.getId(), dmp );
        if ( ret != false )
        {
            try
            {

                // if ( thenPoint >= Math.abs( (dmp.getAddPoint() * point) ) )
                // {
                dup = new DataUserPointPay();
                // ポイント履歴からデータを取得

                // もし、予約Noが入っていた場合は予約番号より取得する。
                if ( !dhc.getRsvNo().equals( "" ) )
                {
                    ret = dup.getData( userId, dmp.getKind(), dhc.getId(), dhc.getRsvNo() );
                }
                else
                {
                    ret = dup.getData( userId, dmp.getKind(), dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );
                }
                dup.setUserId( userId );
                // 登録されていない場合,及び予約番号が入っていた場合にセット
                // if ( dup.getGetDate() == 0 || !dhc.getRsvNo().equals( "" ) )
                // {
                // dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                // dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setGetDate( dhc.getCiDate() );
                dup.setGetTime( dhc.getCiTime() );
                // }
                dup.setCode( dmp.getCode() );
                // ポイントの加減をチェック
                if ( dmp.getAdjustmentFlag() == 0 )
                {
                    dup.setPoint( Math.abs( (dmp.getAddPoint() * point) ) );
                }
                else if ( dmp.getAdjustmentFlag() == 1 )
                {
                    dup.setPoint( Math.abs( (dmp.getAddPoint() * point) ) * -1 );
                }
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( dhc.getId() );
                dup.setMemo( "" );
                dup.setIdm( dhc.getIdm() );
                dup.setUserSeq( dhc.getUserSeq() );
                dup.setVisitSeq( dhc.getVisitSeq() );
                dup.setExtString( dhc.getRsvNo() );
                dup.setRoomNo( dhc.getRoomNo() );
                dup.setThenPoint( thenPoint );
                dup.setHotenaviId( dhc.getVisitHotenaviId() );
                dup.setEmployeeCode( employeeCode );
                dup.setUserType( dhc.getUserType() );

                // データを取得できれば更新、取得できなかったら挿入
                if ( ret != false )
                {
                    ret = dup.updateData( userId, dup.getSeq() );
                }
                else
                {
                    ret = dup.insertData();
                }
                // }
                // else
                // {
                // ret = false;
                // }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPay.setUsePointUpdate] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * ハピー利用履歴追加
     * 
     * @param dhc チェックインデータ
     * @param code ポイントマスタのコード
     * @param employeeCode 従業員コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean cancelUsePoint(DataHotelCi dhc, int kind, int employeeCode)
    {
        boolean ret = false;
        DataUserPointPay dup;

        try
        {
            dup = new DataUserPointPay();

            // ポイント変更のデータを取得
            ret = dup.getData( dhc.getUserId(), kind, dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );
            // 最新のデータを取得した場合にポイントが+であれば、取消済みなので処理しない
            if ( ret != false && dup.getPoint() <= 0 )
            {
                dup.setSeq( 0 );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setPoint( Math.abs( dup.getPoint() ) );
                dup.setEmployeeCode( employeeCode );

                ret = dup.insertData();
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPay.cancelUsePoint] Exception=" + e.toString() );
        }
        finally
        {
        }
        return(ret);
    }

    /****
     * マイル更新日変更
     * 
     * @param dub ユーザ情報クラス
     */
    public void modifyPointPayUpdate(DataUserBasic dub)
    {
        String userId = "";
        boolean ret = false;
        int registDate = 0;
        int updateDate = 0;

        if ( dub != null )
        {
            userId = dub.getUserId();
            registDate = dub.getRegistDatePay();
            updateDate = dub.getPointPayUpdate();
            // 更新日が登録されていなかったら入会日をセットする
            if ( updateDate == 0 && registDate == 0 )
            {
                dub.setPointPayUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                ret = dub.updateData( userId );
            }
            // 入会日より更新日の方が先の場合、入会日を開始日とする
            else if ( registDate != 0 && registDate < updateDate )
            {
                dub.setPointPayUpdate( registDate );
                ret = dub.updateData( userId );
            }
            // 更新日が0の場合、入会日を開始日とする
            else if ( updateDate == 0 )
            {
                dub.setPointPayUpdate( registDate );
                ret = dub.updateData( userId );
            }
        }
    }

    /**
     * 有料ポイントの失効ポイントを取得する
     * 
     * @param userId ユーザID
     * @param calDate 集計日（YYYYMMDD）
     * @return
     */
    public boolean getPointExpired(String userId)
    {
        final int TWO = -2;
        final int ONE = 1;
        int calDate = 0;
        int agoDate = 0;
        int ago2Year = 0;
        int ago2YearNext = 0;
        int pointExpired = 0;
        int pointExpiredNext = 0;
        int pointUsed = 0;
        int pointTotal = 0;
        int point = 0;
        int pointNext = 0;

        try
        {
            calDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            // 翌月の月初を当日にする
            calDate = DateEdit.addMonth( calDate / 100 * 100 + 1, ONE );
            if ( calDate < 20130101 )
            {
                calDate = 20130101;
            }

            // 先月の月末
            agoDate = DateEdit.addDay( calDate, ONE * -1 );

            // 2年前の月初の日付を取得
            ago2Year = DateEdit.addYear( calDate / 100 * 100 + 1, TWO );
            // 2年前の月初の翌月を取得
            ago2YearNext = DateEdit.addMonth( ago2Year, ONE );

            // 2年前の月初の月を取得
            this.expiredMonth = Integer.toString( agoDate / 100 % 100 );
            // 2年前の月初の翌月の月を取得
            this.expiredNextMonth = Integer.toString( calDate / 100 % 100 );

            // 現在のポイント
            pointTotal = this.getNowPoint( userId, false );
            // 有効期限切れポイント
            pointExpired = this.getPointExpiredByDate( userId, ago2Year );
            // 翌月の有効期限切れポイント
            pointExpiredNext = this.getPointExpiredByDate( userId, ago2YearNext );
            // 使用済みポイント
            pointUsed = this.getPointUsed( userId, calDate );

            if ( pointExpired + pointUsed > 0 )
            {
                point = pointTotal - (pointExpired + pointUsed);
            }
            else
            {
                point = pointTotal;
            }

            if ( pointExpiredNext + pointUsed > 0 )
            {
                pointNext = pointTotal - (pointExpiredNext + pointUsed);
            }
            else
            {
                pointNext = pointTotal;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserPointPay.getLostPoint()] userID=" + userId + ", Exception :" + e.toString() );
        }
        this.expiredPoint = pointTotal - point;
        this.expiredPointNextMonth = point - pointNext;

        return true;
    }

    /**
     * 失効マイル
     * 
     * @param userId ユーザID
     * @param calDate 対象日付（YYYYMMDD）
     * @return
     */
    private int getPointExpiredByDate(String userId, int calDate)
    {
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query = "";
        int pointExpired = 0;

        // 2年の翌月経過失効マイル
        query = "SELECT SUM(point - expired_point) AS point_sum FROM hh_user_point_pay";
        query += " WHERE hh_user_point_pay.user_id = ?";
        query += " AND get_date < ?";
        // 有効なポイントのみ取得条件にする
        query += " AND point <> expired_point";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, calDate );
            result = prestate.executeQuery();
            if ( result.next() )
            {
                pointExpired = result.getInt( "point_sum" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserPointPay.getPointExpired()] userID=" + userId
                    + ", calDate=" + calDate + ", Exception :" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return pointExpired;
    }

    /*****
     * 使用マイル
     * 
     * @param userId ユーザID
     * @param calDate 集計日
     * @return
     */
    private int getPointUsed(String userId, int calDate)
    {
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query = "";
        int pointUsed = 0;

        // まだ計算されていない使用マイル
        query = "SELECT SUM(point - expired_point) AS point_sum FROM hh_user_point_pay";
        query += " WHERE hh_user_point_pay.user_id = ? ";
        query += " AND get_date < ?";
        query += " AND point    < 0";
        // 有効なポイントのみ取得条件にする
        query += " AND point <> expired_point";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, calDate );
            result = prestate.executeQuery();
            if ( result.next() )
            {
                pointUsed = result.getInt( "point_sum" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserPointPay.getPointUsed()] userID=" + userId
                    + ", calDate=" + calDate + ", Exception :" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return pointUsed;
    }

    /**
     * ハピー利用履歴追加
     * 
     * @param dhc チェックインデータ
     * @param kind ポイントマスタ区分
     * @param employeeCode 従業員コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean cancelRsvPoint(DataHotelCi dhc, int kind, int employeeCode)
    {
        boolean ret = false;
        DataUserPointPay dup;

        try
        {
            dup = new DataUserPointPay();

            // ポイント変更のデータを取得
            ret = dup.getData( dhc.getUserId(), kind, dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );
            // 最新のデータを取得した場合にポイントが-であれば、取消済みなので処理しない
            if ( ret != false && dup.getPoint() >= 0 )
            {
                dup.setSeq( 0 );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setPoint( Math.abs( dup.getPoint() ) * -1 );
                dup.setEmployeeCode( employeeCode );
                dup.setUserType( dhc.getUserType() );

                ret = dup.insertData();
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPay.cancelRsvPoint] Exception=" + e.toString() );
        }
        finally
        {
        }
        return(ret);
    }

    /**
     * マイル挿入
     * 
     * @param dhc
     * @param pointCode
     * @param thenPoint
     * @return
     */
    public int insertMile(DataHotelCi dhc, int pointCode, int thenPoint)
    {
        int nMaxSeq = 0;
        boolean ret = false;
        DataMasterPoint dmp;
        DataUserPointPay dup;

        dmp = new DataMasterPoint();
        dmp.getData( pointCode );

        ret = this.getMasterPointExtNum( dhc.getUserId(), dmp.getKind(), dmp.getCode(), dhc.getId(), dmp );
        if ( ret != false )
        {
            try
            {
                nMaxSeq = getMaxSeq( dhc.getUserId() );
                // インサート対象となるため、+1する
                nMaxSeq++;
                // hh_user_point_payと同じように現在ポイントから
                if ( thenPoint >= Math.abs( dhc.getUsePoint() ) )
                {
                    dup = new DataUserPointPay();
                    dup.setUserId( dhc.getUserId() );
                    dup.setSeq( nMaxSeq );
                    dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    dup.setCode( dmp.getCode() );
                    // ポイントの加減をチェック
                    if ( dmp.getAdjustmentFlag() == 0 )
                    {
                        dup.setPoint( Math.abs( dhc.getUsePoint() ) );
                    }
                    else if ( dmp.getAdjustmentFlag() == 1 )
                    {
                        dup.setPoint( Math.abs( dhc.getUsePoint() ) * -1 );
                    }
                    dup.setPointKind( dmp.getKind() );
                    dup.setExtCode( dhc.getId() );
                    dup.setMemo( "" );
                    // hh_user_point_payにすぐ追加するため、追加済みにする
                    dup.setIdm( dhc.getIdm() );
                    dup.setUserSeq( dhc.getUserSeq() );
                    dup.setVisitSeq( dhc.getVisitSeq() );
                    dup.setRoomNo( dhc.getRoomNo() );
                    dup.setThenPoint( thenPoint );
                    dup.setHotenaviId( dhc.getVisitHotenaviId() );
                    // ユーザタイプをセット
                    dup.setUserType( dhc.getUserType() );
                    ret = dup.insertDataBySeq();

                }
                else
                {
                    ret = false;
                    nMaxSeq = 0;
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPay.insertMile()] Exception=" + e.toString() );
                nMaxSeq = 0;
            }
            finally
            {
            }
        }
        return(nMaxSeq);
    }

    /**
     * マイル挿入
     * 
     * @param dhc
     * @param pointCode
     * @param thenPoint
     * @return
     */
    public int updateMile(DataHotelCi dhc, int pointCode, int thenPoint, int seq)
    {
        boolean ret = false;
        DataMasterPoint dmp;
        DataUserPointPay dup;

        dmp = new DataMasterPoint();
        dmp.getData( pointCode );

        ret = this.getMasterPointExtNum( dhc.getUserId(), dmp.getKind(), dmp.getCode(), dhc.getId(), dmp );
        if ( ret != false )
        {
            try
            {
                // hh_user_point_payと同じように現在ポイントから
                if ( thenPoint >= Math.abs( dhc.getUsePoint() ) )
                {
                    dup = new DataUserPointPay();
                    dup.getData( dhc.getUserId(), seq );
                    dup.setUserId( dhc.getUserId() );
                    dup.setSeq( seq );
                    // 登録されていない場合のみセット
                    if ( dup.getGetDate() == 0 )
                    {
                        dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    }
                    dup.setCode( dmp.getCode() );
                    // ポイントの加減をチェック
                    if ( dmp.getAdjustmentFlag() == 0 )
                    {
                        dup.setPoint( Math.abs( dhc.getUsePoint() ) );
                    }
                    else if ( dmp.getAdjustmentFlag() == 1 )
                    {
                        dup.setPoint( Math.abs( dhc.getUsePoint() ) * -1 );
                    }
                    dup.setPointKind( dmp.getKind() );
                    dup.setExtCode( dhc.getId() );
                    dup.setMemo( "" );
                    // hh_user_point_payにすぐ追加するため、追加済みにする
                    dup.setIdm( dhc.getIdm() );
                    dup.setUserSeq( dhc.getUserSeq() );
                    dup.setVisitSeq( dhc.getVisitSeq() );
                    dup.setRoomNo( dhc.getRoomNo() );
                    dup.setThenPoint( thenPoint );
                    dup.setHotenaviId( dhc.getVisitHotenaviId() );
                    // ユーザタイプをセット
                    dup.setUserType( dhc.getUserType() );
                    ret = dup.updateData( dhc.getUserId(), seq );

                }
                else
                {
                    ret = false;
                    seq = 0;
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPay.updateMile()] Exception=" + e.toString() );
                seq = 0;
            }
            finally
            {
            }
        }
        return(seq);
    }

    /****
     * 予約ボーナスマイルの追加（付与ハピー付与時に追加）
     * 
     * @param userId ユーザーID
     * @param pointCode ポイントコード
     * @param thenPoint 当時ポイント
     * @param employeeCode 従業員コード
     * @param dhc チェックインデータ
     * @return
     */
    public boolean setAddBonusMile(String userId, int thenPoint, int addBonusMile, int employeeCode, DataHotelCi dhc)
    {
        boolean ret = false;
        DataUserPointPay dup;
        DataHotelBasic dhb;

        dup = new DataUserPointPay();
        dhb = new DataHotelBasic();
        dhb.getData( dhc.getId() );

        try
        {
            dup = new DataUserPointPay();
            if ( !dup.getData( userId, OwnerRsvCommon.HAPYPOINT_29, dhc.getId(), dhc.getRsvNo() ) )
            {
                dup.setUserId( userId );
                dup.setGetDate( dhc.getCiDate() );
                dup.setGetTime( dhc.getCiTime() );
                dup.setCode( OwnerRsvCommon.RSV_BONUS_CODE );
                dup.setPoint( addBonusMile );
                dup.setPointKind( OwnerRsvCommon.HAPYPOINT_29 );
                dup.setExtCode( dhc.getId() );
                dup.setMemo( "" );
                dup.setIdm( dhc.getIdm() );
                dup.setUserSeq( dhc.getUserSeq() );
                dup.setVisitSeq( dhc.getVisitSeq() );
                dup.setThenPoint( thenPoint );
                dup.setHotenaviId( dhb.getHotenaviId() );
                // dupt.setEmployeeCode( 0 );
                dup.setUserType( dhc.getUserType() );

                // 予約関連で追加
                dup.setSlipNo( dhc.getSlipNo() );
                dup.setRoomNo( dhc.getRoomNo() );
                dup.setAmount( dhc.getAmount() );
                dup.setExtString( dhc.getRsvNo() );

                ret = dup.insertData();
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPay.setPointPresent ] Exception=" + e.toString() );
        }
        finally
        {
        }
        return(ret);
    }
}
