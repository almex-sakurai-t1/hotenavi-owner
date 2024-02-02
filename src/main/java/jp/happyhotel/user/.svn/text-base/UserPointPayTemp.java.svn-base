/*
 * @(#)UserPointPayTemp.java 1.00 2007/08/23 Copyright (C) ALMEX Inc. 2007 ユーザポイント取得・更新クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelCi;
import jp.happyhotel.data.DataHotelHappie;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.data.DataUserPointPayTemp;

/**
 * 有料ユーザーポイント
 * 
 * @author S.Tashiro
 * @version 1.00 2009/08/07
 */
public class UserPointPayTemp implements Serializable
{
    /**
     *
     */
    private static final long      serialVersionUID = 4658546477538933384L;
    private static final int       ONE_HUNDREDTH    = 100;                 // 100分率
    private static final int       ADD_FLAG_FINISH  = 1;
    private static final int       TOMORROW         = 1;

    private int                    userPointCount;
    private DataUserPointPayTemp[] userPoint;

    /**
     * データを初期化します。
     */
    public UserPointPayTemp()
    {
        userPointCount = 0;
    }

    public DataUserPointPayTemp[] getUserPoint()
    {
        return userPoint;
    }

    public int getUserPointCount()
    {
        return userPointCount;
    }

    /**
     * 仮ハピーのデータを取得する（未反映のもののみ）
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

        query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic";

        if ( userId.compareTo( "" ) != 0 )
        {
            query += " WHERE hh_user_point_pay_temp.user_id = ?";
            query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
            query += " AND hh_user_basic.regist_status = 9";
            query += " AND hh_user_basic.del_flag = 0";
            // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
            query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";
            query += " AND hh_user_point_pay_temp.add_flag=0";
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
            Logging.info( "[UserPointPayTemp.getPointList] Exception=" + e.toString() );
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
                this.userPoint = new DataUserPointPayTemp[this.userPointCount];
                for( i = 0 ; i < userPointCount ; i++ )
                {
                    userPoint[i] = new DataUserPointPayTemp();
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
            Logging.info( "[UserPointPayTemp.getUserPointSub] Exception=" + e.toString() );
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
     * ユーザポイントを取得する（IDから）
     * 
     * @param userId ユーザID
     * @param nowMonth 今月獲得数フラグ(true:今月獲得数)
     * @return ポイント数
     */
    public int getNowPoint(String userId, boolean nowMonth)
    {
        int point;
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
        // ユーザ情報の取得
        ubi = new UserBasicInfo();
        ubi.getUserBasic( userId );

        if ( nowMonth != false )
        {
            query = "SELECT SUM(hh_user_point_pay_temp.point) FROM hh_user_point_pay_temp, hh_user_basic";
            query += " WHERE hh_user_point_pay_temp.user_id = ?";
            query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id ";
            query += " AND hh_user_basic.regist_status = 9";
            query += " AND hh_user_basic.del_flag = 0";
            query += " AND hh_user_point_pay_temp.add_flag=0";
            // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
            query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";
        }
        else
        {

            // 最終集計日以降のもののみ取得する
            query = "SELECT SUM( hh_user_point_pay_temp.point ) FROM hh_user_point_pay_temp, hh_user_basic";
            query += " WHERE hh_user_point_pay_temp.user_id = ?";
            query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id ";
            query += " AND hh_user_basic.regist_status = 9";
            query += " AND hh_user_basic.del_flag = 0";
            query += " AND hh_user_point_pay_temp.add_flag=0";
            // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
            query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";
        }
        try
        {
            connection = DBConnection.getConnection();
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
                }
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPayTemp.getNowPoint] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(point);
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
            Logging.info( "[UserPointPayTemp.getMasterPoint] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic";
                query += " WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.get_date = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPoint] Exception=" + e.toString() );
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
                query = "SELECT  hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPoint] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic";
                query += " WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay_temp.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay_temp.get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
            Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.get_date = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic";
                query += " WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.regist_status_pay = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay_temp.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay_temp.get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
            Logging.info( "[UserPointPayTemp.getMasterPointExtString] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.regist_status_pay = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.get_date = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                query += " AND hh_user_point_pay_temp.ext_string = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExtString] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.regist_status_pay = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                query += " AND hh_user_point_pay_temp.ext_string = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExtString] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic";
                query += " WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.regist_status_pay = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                query += " AND hh_user_point_pay_temp.ext_string = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay_temp.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay_temp.get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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

        query = "SELECT * FROM hh_master_point WHERE kind = ? AND code = ?";

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
            Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.get_date = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic";
                query += " WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay_temp.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay_temp.get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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

        query = "SELECT * FROM hh_master_point WHERE kind = ? AND code = ?";

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
            Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.get_date = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic";
                query += " WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay_temp.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay_temp.get_date <= " + date;
                // 最初の登録日をhh_user_basic.point_pay_updateに登録しているため、それ以降の日付を登録
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
        DataUserPointPayTemp dupt;

        dmp = new DataMasterPoint();
        dupt = new DataUserPointPayTemp();
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
                dupt = new DataUserPointPayTemp();
                dupt.setUserId( userId );
                dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dupt.setCode( dmp.getCode() );
                // ポイントの加減をチェック
                if ( dmp.getAdjustmentFlag() == 0 )
                {
                    dupt.setPoint( dmp.getAddPoint() );
                }
                else if ( dmp.getAdjustmentFlag() == 1 )
                {
                    dupt.setPoint( -dmp.getAddPoint() );
                }
                dupt.setPointKind( dmp.getKind() );
                dupt.setExtCode( formId );
                dupt.setMemo( memo );
                ret = dupt.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPayTemp.setPointPresent ] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * 来店回数取得
     * 
     * @param userId ユーザID
     * @param id ホテルID
     * @return 来店回数
     */
    public int getMaxVisitSeq(String userId, int id)
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
            query = "SELECT MAX(visit_seq) AS visit FROM hh_user_point_pay_temp";
            query += " WHERE point_kind BETWEEN 21 AND 23 ";
            query += " AND user_id = ?";
            query += " AND ext_code = ?";
            query += " ORDER BY visit DESC";

            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setString( 1, userId );
                prestate.setInt( 2, id );
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
            Logging.error( "[UserPointPayTemp().getMaxVisitSeq()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(nMaxSeq);
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
            query = "SELECT MAX(seq) FROM hh_user_point_pay_temp";
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
            Logging.error( "[UserPointPayTemp().getMaxSeq()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(nMaxSeq);
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
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setUsePoint(String userId, int pointCode, int thenPoint, int point, int employeeCode, DataHotelCi dhc)
    {
        boolean ret = false;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;

        dmp = new DataMasterPoint();
        dmp.getData( pointCode );
        Logging.info( "setUsePoint", "pointCode:" + pointCode + ",thenPoint:" + thenPoint + ",usePoint:" + dhc.getUsePoint() );

        ret = this.getMasterPointExtNum( userId, dmp.getKind(), dmp.getCode(), dhc.getId(), dmp );
        if ( ret != false )
        {
            try
            {
                // hh_user_point_payと同じように現在ポイントから
                // if ( thenPoint >= Math.abs( (dmp.getAddPoint() * point) ) )
                // {
                dupt = new DataUserPointPayTemp();
                dupt.setUserId( userId );
                // 登録されていない場合のみセット
                if ( dupt.getGetDate() == 0 )
                {
                    dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                }
                dupt.setCode( dmp.getCode() );
                // ポイントの加減をチェック
                if ( dmp.getAdjustmentFlag() == 0 )
                {
                    dupt.setPoint( Math.abs( (dmp.getAddPoint() * point) ) );
                }
                else if ( dmp.getAdjustmentFlag() == 1 )
                {
                    dupt.setPoint( Math.abs( (dmp.getAddPoint() * point) ) * -1 );
                }
                dupt.setPointKind( dmp.getKind() );
                dupt.setExtCode( dhc.getId() );
                dupt.setMemo( "" );
                // 登録されていない場合のみセット
                if ( dupt.getReflectDate() == 0 )
                {
                    // hh_user_point_payにすぐ追加するため、追加済みにする
                    dupt.setReflectDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                }
                dupt.setAddFlag( ADD_FLAG_FINISH );
                dupt.setIdm( dhc.getIdm() );
                dupt.setUserSeq( dhc.getUserSeq() );
                dupt.setVisitSeq( dhc.getVisitSeq() );
                dupt.setRoomNo( dhc.getRoomNo() );
                dupt.setThenPoint( thenPoint );
                dupt.setHotenaviId( dhc.getVisitHotenaviId() );
                dupt.setEmployeeCode( employeeCode );
                // ユーザタイプをセット
                dupt.setUserType( dhc.getUserType() );
                dupt.setCiDate( dhc.getCiDate() );
                dupt.setCiTime( dhc.getCiTime() );

                ret = dupt.insertData();
                // }
                // else
                // {
                // ret = false;
                // }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPayTemp.setUsePoint] Exception=" + e.toString() );
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
     * @param code ポイントコード
     * @param extCode 付帯番号
     * @param point 利用ポイント
     * @param idm フェリカID
     * @param userSeq ユーザ管理番号
     * @param visitSeq 来店回数
     * @param thenPoint その当時のポイント
     * @param hotenaviId ホテナビID
     * @param employeeCode 従業員コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setUsePointUpdate(String userId, int pointCode, int thenPoint, int point, int employeeCode, DataHotelCi dhc)
    {
        boolean ret = false;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;

        dmp = new DataMasterPoint();
        dmp.getData( pointCode );
        Logging.info( "setUsePointUpdate1", "pointCode:" + pointCode + ",thenPoint:" + thenPoint + ",usePoint:" + dhc.getUsePoint() );

        ret = this.getMasterPointExtNum( userId, dmp.getKind(), dmp.getCode(), dhc.getId(), dmp );
        if ( ret != false )
        {
            try
            {
                Logging.info( "setUsePointUpdate2", "thenPoint" + thenPoint + "dmp.getAddPoint() :" + dmp.getAddPoint() + ",point:" + point );
                // hh_user_point_payと同じように現在ポイントから
                // if ( thenPoint >= Math.abs( (dmp.getAddPoint() * point) ) )
                // {
                dupt = new DataUserPointPayTemp();
                ret = dupt.getData( userId, dmp.getKind(), dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );
                Logging.info( "setUsePointUpdate3", "ret:" + ret + "userId:" + userId + ",pointKind:" + dmp.getKind() + ",Id:" + dhc.getId() + ",UserSeq:" + dhc.getUserSeq() + ",VisitSeq:" + dhc.getVisitSeq() );

                dupt.setUserId( userId );
                dupt.setCode( dmp.getCode() );
                // ポイントの加減をチェック
                if ( dmp.getAdjustmentFlag() == 0 )
                {
                    dupt.setPoint( Math.abs( (dmp.getAddPoint() * point) ) );
                }
                else if ( dmp.getAdjustmentFlag() == 1 )
                {
                    dupt.setPoint( Math.abs( (dmp.getAddPoint() * point) ) * -1 );
                }
                dupt.setPointKind( dmp.getKind() );
                dupt.setExtCode( dhc.getId() );
                dupt.setMemo( "" );
                // 登録されていない場合のみセット
                if ( dupt.getGetDate() == 0 )
                {
                    dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                }
                // 登録されていない場合のみセット
                if ( dupt.getReflectDate() == 0 )
                {
                    dupt.setReflectDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                }
                dupt.setAddFlag( ADD_FLAG_FINISH );
                dupt.setIdm( dhc.getIdm() );
                dupt.setUserSeq( dhc.getUserSeq() );
                dupt.setVisitSeq( dhc.getVisitSeq() );
                dupt.setRoomNo( dhc.getRoomNo() );
                dupt.setSlipNo( dhc.getSlipNo() );
                dupt.setThenPoint( thenPoint );
                dupt.setHotenaviId( dhc.getVisitHotenaviId() );
                dupt.setEmployeeCode( employeeCode );
                dupt.setUserType( dhc.getUserType() );
                dupt.setCiDate( dhc.getCiDate() );
                dupt.setCiTime( dhc.getCiTime() );

                if ( ret != false )
                {
                    ret = dupt.updateData( userId, dupt.getSeq() );
                }
                else
                {
                    ret = dupt.insertData();
                }
                // }
                // else
                // {
                // ret = false;
                // }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPayTemp.setUsePoint] Exception=" + e.toString() );
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
     * @param kind ポイントマスタ区分
     * @param employeeCode 従業員コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean cancelUsePoint(DataHotelCi dhc, int kind, int employeeCode)
    {
        boolean ret = false;
        DataUserPointPayTemp dupt;

        try
        {
            dupt = new DataUserPointPayTemp();

            // ポイント変更のデータを取得
            ret = dupt.getData( dhc.getUserId(), kind, dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );
            // 最新のデータを取得した場合にポイントが+であれば、取消済みなので処理しない
            if ( ret != false && dupt.getPoint() <= 0 )
            {
                dupt.setSeq( 0 );
                dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dupt.setPoint( Math.abs( dupt.getPoint() ) );
                // hh_user_point_payにすぐ追加するため、追加済みにする
                dupt.setReflectDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dupt.setAddFlag( ADD_FLAG_FINISH );
                dupt.setEmployeeCode( employeeCode );
                dupt.setUserType( dhc.getUserType() );
                dupt.setCiDate( dhc.getCiDate() );
                dupt.setCiTime( dhc.getCiTime() );

                ret = dupt.insertData();
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPayTemp.cancelUsePoint] Exception=" + e.toString() );
        }
        finally
        {
        }
        return(ret);
    }

    /*****
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
        int reflectDate;
        boolean ret;
        boolean retReflect;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;
        DataHotelHappie dhh;
        DataHotelBasic dhb;
        UserPointPay upp;

        addPoint = 0;
        result = 0;
        reflectDate = 0;
        dmp = new DataMasterPoint();
        dupt = new DataUserPointPayTemp();
        dhh = new DataHotelHappie();
        upp = new UserPointPay();
        retReflect = false;

        ret = dmp.getData( pointCode );
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
            // hh_user_point_payに来店ハピーが追加されたら、一時添付テーブルにも追加
            if ( upp.setVisitPoint( userId, pointCode, thenPoint, 0, dhc ) > 0 )
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
                    dupt = new DataUserPointPayTemp();
                    boolean ret_dupt = dupt.getData( userId, dmp.getKind(), dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );
                    dupt.setUserId( userId );
                    // 登録されていない場合のみセット
                    if ( dupt.getGetDate() == 0 )
                    {
                        dupt.setGetDate( dhc.getCiDate() );
                        dupt.setGetTime( dhc.getCiTime() );
                    }
                    dupt.setCode( dmp.getCode() );
                    dupt.setPoint( addPoint );
                    dupt.setPointKind( dmp.getKind() );
                    dupt.setExtCode( dhc.getId() );
                    dupt.setMemo( "" );
                    dupt.setAddFlag( ADD_FLAG_FINISH );
                    // 登録されていない場合のみセット
                    if ( dupt.getReflectDate() == 0 )
                    {
                        dupt.setReflectDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    }
                    dupt.setIdm( dhc.getIdm() );
                    dupt.setUserSeq( dhc.getUserSeq() );
                    dupt.setVisitSeq( dhc.getVisitSeq() );
                    dupt.setThenPoint( thenPoint );
                    dupt.setHotenaviId( dhb.getHotenaviId() );
                    // dupt.setEmployeeCode( employeeCode );
                    dupt.setUserType( dhc.getUserType() );
                    dupt.setCiDate( dhc.getCiDate() );
                    dupt.setCiTime( dhc.getCiTime() );
                    if ( ret_dupt != false )
                    {
                        ret = dupt.updateData( userId, dupt.getSeq() );
                        result = -1;
                    }
                    else
                    {
                        ret = dupt.insertData();
                        if ( ret != false )
                        {
                            result = addPoint;
                        }
                        else
                        {
                            result = -1;
                        }
                    }
                }
                catch ( Exception e )
                {
                    result = -1;
                    Logging.info( "[UserPointPayTemp.setPointPresent ] Exception=" + e.toString() );
                }
                finally
                {
                }
            }
        }
        else
        {
            result = 0;
        }
        return(result);
    }

    /***
     * ホテル利用ハピーの追加
     * 
     * @param userId
     * @param pointCode
     * @param price
     * @param employeeCode
     * @param dhc
     * @return
     */
    public int setAmountPoint(String userId, int pointCode, int thenPoint, int price, int employeeCode, DataHotelCi dhc)
    {
        int addPoint;
        int result;
        int reflectDate;
        boolean ret;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;
        DataHotelBasic dhb;

        addPoint = 0;
        result = 0;
        reflectDate = 0;
        dmp = new DataMasterPoint();
        dupt = new DataUserPointPayTemp();
        ret = dmp.getData( pointCode );
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

        ret = getMasterPointExtNum( userId, dmp.getKind(), pointCode, dhc.getId(), dmp );
        if ( ret != false )
        {
            addPoint = dmp.getAddPoint();
            // 料金に対するハピーの付与分の計算
            if ( price / ONE_HUNDREDTH > 1 )
            {
                if ( dhc.getAmountRate() > 0 )
                {
                    // 倍率を加算
                    addPoint = (int)(addPoint * dhc.getAmountRate() * price / ONE_HUNDREDTH);
                }
                else
                {
                    addPoint = addPoint * price / ONE_HUNDREDTH;
                }
            }
            else
            {
                addPoint = 0;
            }

            reflectDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            // 移行日が1日以上だったら移行日を加算
            if ( dmp.getShiftDay() > 0 )
            {
                reflectDate = DateEdit.addDay( reflectDate, dmp.getShiftDay() );
            }

            try
            {
                dupt = new DataUserPointPayTemp();
                boolean ret_dupt = dupt.getData( userId, dmp.getKind(), dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );
                dupt.setUserId( userId );
                // 登録されていない場合のみセット
                if ( dupt.getGetDate() == 0 )
                {
                    dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                }
                dupt.setCode( dmp.getCode() );
                dupt.setPoint( addPoint );
                dupt.setPointKind( dmp.getKind() );
                dupt.setExtCode( dhc.getId() );
                dupt.setMemo( "" );
                // 登録されていない場合のみセット
                if ( dupt.getReflectDate() == 0 )
                {
                    dupt.setReflectDate( reflectDate );
                }
                dupt.setIdm( dhc.getIdm() );
                dupt.setUserSeq( dhc.getUserSeq() );
                dupt.setVisitSeq( dhc.getVisitSeq() );
                dupt.setSlipNo( dhc.getSlipNo() );
                dupt.setRoomNo( dhc.getRoomNo() );
                dupt.setAmount( price );
                dupt.setThenPoint( thenPoint );
                dupt.setHotenaviId( dhc.getVisitHotenaviId() );
                dupt.setEmployeeCode( employeeCode );
                dupt.setUserType( dhc.getUserType() );
                dupt.setCiDate( dhc.getCiDate() );
                dupt.setCiTime( dhc.getCiTime() );
                if ( ret_dupt != false )
                {
                    ret = dupt.updateData( userId, dupt.getSeq() );
                    result = -1;
                }
                else
                {
                    ret = dupt.insertData();
                    if ( ret != false )
                    {
                        result = addPoint;
                    }
                    else
                    {
                        result = -1;
                    }
                }

            }
            catch ( Exception e )
            {
                result = -1;
                Logging.info( "[UserPointPayTemp.setAmountPoint(dhc) ] Exception=" + e.toString() );
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

    /***
     * ホテル利用ハピーの更新
     * 
     * @param userId
     * @param pointCode
     * @param price
     * @param employeeCode
     * @param dhc
     * @return
     */
    public int setAmountPointUpdate(String userId, int pointCode, int thenPoint, int price, int employeeCode, DataHotelCi dhc)
    {
        int addPoint;
        int result;
        int reflectDate;
        boolean ret;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;
        DataHotelBasic dhb;

        addPoint = 0;
        result = 0;
        reflectDate = 0;
        dmp = new DataMasterPoint();
        dupt = new DataUserPointPayTemp();
        ret = dmp.getData( pointCode );
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

        ret = getMasterPointExtNum( userId, dmp.getKind(), pointCode, dhc.getId(), dmp );
        if ( ret != false )
        {
            addPoint = dmp.getAddPoint();
            // 料金に対するハピーの付与分の計算
            if ( price / ONE_HUNDREDTH > 1 )
            {
                if ( dhc.getAmountRate() > 0 )
                {
                    // 倍率を加算
                    addPoint = (int)(addPoint * dhc.getAmountRate() * price / ONE_HUNDREDTH);
                }
                else
                { // 倍率を加算
                    addPoint = addPoint * price / ONE_HUNDREDTH;
                }
            }
            else
            {
                addPoint = 0;
            }

            reflectDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            // 移行日が1日以上だったら移行日を加算
            if ( dmp.getShiftDay() > 0 )
            {
                reflectDate = DateEdit.addDay( reflectDate, dmp.getShiftDay() );
            }

            try
            {
                dupt = new DataUserPointPayTemp();
                // 修正対象のレコードを取得
                ret = dupt.getData( userId, dmp.getKind(), dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );

                dupt.setUserId( userId );
                // 登録されていない場合のみセット
                if ( dupt.getGetDate() == 0 )
                {
                    dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                }
                dupt.setCode( dmp.getCode() );
                dupt.setPoint( addPoint );
                dupt.setPointKind( dmp.getKind() );
                dupt.setExtCode( dhc.getId() );
                dupt.setMemo( "" );
                // 登録されていない場合のみセット
                if ( dupt.getReflectDate() == 0 )
                {
                    dupt.setReflectDate( reflectDate );
                }
                dupt.setIdm( dhc.getIdm() );
                dupt.setUserSeq( dhc.getUserSeq() );
                dupt.setVisitSeq( dhc.getVisitSeq() );
                dupt.setSlipNo( dhc.getSlipNo() );
                dupt.setRoomNo( dhc.getRoomNo() );
                dupt.setAmount( price );
                dupt.setThenPoint( thenPoint );
                dupt.setHotenaviId( dhc.getVisitHotenaviId() );
                dupt.setEmployeeCode( employeeCode );
                dupt.setUserType( dhc.getUserType() );
                dupt.setCiDate( dhc.getCiDate() );
                dupt.setCiTime( dhc.getCiTime() );

                // データが内場合はインサート
                if ( ret == false )
                {
                    ret = dupt.insertData();
                    if ( ret != false )
                    {
                        result = addPoint;
                    }
                    else
                    {
                        result = -1;
                    }
                }
                // ある場合はアップデート
                else
                {
                    ret = dupt.updateData( userId, dupt.getSeq() );
                    result = addPoint;
                }

            }
            catch ( Exception e )
            {
                result = -1;
                Logging.info( "[UserPointPayTemp.setAmountPointUpdate ] Exception=" + e.toString() );
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

    /***
     * ホテルの来店回数を求める
     * 
     * @param userId ユーザID
     * @param id ホテルID
     * @param userSeq ユーザ管理番号
     * @param visitSeq 来店回数
     * @return
     */
    public boolean isCheckOut(String userId, int id, int getDate, int userSeq, int visitSeq)
    {
        String query = "";
        boolean ret = false;
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        int nVisitSeq = 0;

        // すでにホテルID、ユーザ関番号
        query = "SELECT visit_seq FROM hh_user_point_pay_temp";
        query += " WHERE user_id = ?";
        query += " AND get_date >= ?";
        query += " AND get_date <= ?";
        query += " AND ext_code = ?";
        query += " AND user_seq = ?";
        query += " AND visit_seq = ?";
        query += " AND point_kind = 22";

        try
        {
            // トランザクションの開始
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );

            prestate.setString( 1, userId );
            prestate.setInt( 2, getDate );
            prestate.setInt( 3, DateEdit.addDay( getDate, TOMORROW ) );
            prestate.setInt( 4, id );
            prestate.setInt( 5, userSeq );
            prestate.setInt( 6, visitSeq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    nVisitSeq = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserDataIndex.isCheckOut()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }

        if ( nVisitSeq > 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        return(ret);
    }

    /**
     * ポイント履歴取得
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @param pointKind ポイント区分
     * @param userSeq ユーザ管理番号
     * @param visitSeq 来店管理番号
     * @return
     */
    public boolean getUserPointHistory(String userId, int hotelId, int pointKind, int userSeq, int visitSeq)
    {
        boolean ret;
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        int i;
        int count;
        ret = false;

        query = " SELECT * FROM hh_user_point_pay_temp WHERE user_id = ?";
        query += " AND ext_code = ?";
        query += " AND point_kind = ?";
        query += " AND user_seq= ?";
        query += " AND visit_seq = ?";
        query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
        query += " LIMIT 0,1 ";

        try
        {
            // トランザクションの開始
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );

            prestate.setString( 1, userId );
            prestate.setInt( 2, hotelId );
            prestate.setInt( 3, pointKind );
            prestate.setInt( 4, userSeq );
            prestate.setInt( 5, visitSeq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        userPointCount = result.getRow();
                    }

                    // クラスの配列を用意し、初期化する。
                    this.userPoint = new DataUserPointPayTemp[this.userPointCount];
                    for( i = 0 ; i < userPointCount ; i++ )
                    {
                        userPoint[i] = new DataUserPointPayTemp();
                    }

                    count = 0;
                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // ユーザポイント情報の取得
                        this.userPoint[count++].setData( result );
                    }
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[UserDataIndex.getUserPointHistory()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }

        return ret;
    }

    /**
     * ポイント履歴取得
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @param pointKind ポイント区分
     * @param userSeq ユーザ管理番号
     * @param visitSeq 来店管理番号
     * @return
     */
    public boolean getUserPointHistory(String userId, int hotelId, int userSeq, int visitSeq)
    {
        boolean ret;
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        int i;
        int count;
        ret = false;

        query = " SELECT * FROM hh_user_point_pay_temp WHERE user_id = ?";
        query += " AND ext_code = ?";
        query += " AND user_seq= ?";
        query += " AND visit_seq = ?";
        query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
        query += " LIMIT 0,1 ";

        try
        {
            // トランザクションの開始
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );

            prestate.setString( 1, userId );
            prestate.setInt( 2, hotelId );
            prestate.setInt( 3, userSeq );
            prestate.setInt( 4, visitSeq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        userPointCount = result.getRow();
                    }

                    // クラスの配列を用意し、初期化する。
                    this.userPoint = new DataUserPointPayTemp[this.userPointCount];
                    for( i = 0 ; i < userPointCount ; i++ )
                    {
                        userPoint[i] = new DataUserPointPayTemp();
                    }

                    count = 0;
                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // ユーザポイント情報の取得
                        this.userPoint[count++].setData( result );
                    }
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[UserDataIndex.getUserPointHistory()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }

        return ret;
    }

    /**
     * ポイント履歴取得
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @param pointKind ポイント区分
     * @param userSeq ユーザ管理番号
     * @param visitSeq 来店管理番号
     * @return
     */
    public boolean getUserPointHistoryByRsvNo(String userId, int hotelId, int pointKind, String rsvNo)
    {
        boolean ret;
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        int i;
        int count;
        ret = false;

        query = " SELECT * FROM hh_user_point_pay_temp WHERE user_id = ?";
        query += " AND ext_code = ?";
        query += " AND point_kind = ?";
        if ( rsvNo.equals( "" ) == false )
        {
            query += " AND ext_string = ?";
        }
        query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
        query += " LIMIT 0,1 ";

        try
        {
            // トランザクションの開始
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );

            prestate.setString( 1, userId );
            prestate.setInt( 2, hotelId );
            prestate.setInt( 3, pointKind );
            if ( rsvNo.equals( "" ) == false )
            {
                prestate.setString( 4, rsvNo );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        userPointCount = result.getRow();
                    }

                    // クラスの配列を用意し、初期化する。
                    this.userPoint = new DataUserPointPayTemp[this.userPointCount];
                    for( i = 0 ; i < userPointCount ; i++ )
                    {
                        userPoint[i] = new DataUserPointPayTemp();
                    }

                    count = 0;
                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // ユーザポイント情報の取得
                        this.userPoint[count++].setData( result );
                    }
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[UserDataIndex.getUserPointHistoryByRsvNo()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }

        return ret;
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
        DataUserPointPayTemp dupt;

        try
        {
            dupt = new DataUserPointPayTemp();

            // ポイント変更のデータを取得
            ret = dupt.getData( dhc.getUserId(), kind, dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );
            // 最新のデータを取得した場合にポイントが-であれば、取消済みなので処理しない
            if ( ret != false && dupt.getPoint() >= 0 )
            {
                dupt.setSeq( 0 );
                dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dupt.setPoint( Math.abs( dupt.getPoint() ) * -1 );
                // hh_user_point_payにすぐ追加するため、追加済みにする
                dupt.setReflectDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dupt.setAddFlag( ADD_FLAG_FINISH );
                dupt.setEmployeeCode( employeeCode );
                dupt.setUserType( dhc.getUserType() );
                dupt.setCiDate( dhc.getCiDate() );
                dupt.setCiTime( dhc.getCiTime() );

                ret = dupt.insertData();
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPayTemp.cancelRsvPoint] Exception=" + e.toString() );
        }
        finally
        {
        }
        return(ret);
    }

    /**
     * マイル挿入
     * 
     * @param datc
     * @param pointCode
     * @param thenPoint
     * @return
     */
    public int insertMile(DataHotelCi dhc, int pointCode, int thenPoint)
    {
        int nMaxSeq = 0;
        int reflectDate = 0;
        int addFlag = 0;
        boolean ret = false;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;

        dmp = new DataMasterPoint();
        dmp.getData( pointCode );

        ret = this.getMasterPointExtNum( dhc.getUserId(), dmp.getKind(), dmp.getCode(), dhc.getId(), dmp );
        if ( ret != false )
        {
            try
            {
                reflectDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                // 移行日が1日以上だったら移行日を加算
                if ( dmp.getShiftDay() > 0 )
                {
                    reflectDate = DateEdit.addDay( reflectDate, dmp.getShiftDay() );
                }
                else
                {
                    addFlag = ADD_FLAG_FINISH;
                }

                nMaxSeq = getMaxSeq( dhc.getUserId() );
                // インサート対象となるため、+1する
                nMaxSeq++;
                // hh_user_point_payと同じように現在ポイントから
                if ( thenPoint >= Math.abs( dhc.getUsePoint() ) )
                {
                    dupt = new DataUserPointPayTemp();
                    dupt.setUserId( dhc.getUserId() );
                    dupt.setSeq( nMaxSeq );
                    dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    dupt.setCode( dmp.getCode() );
                    // ポイントの加減をチェック
                    if ( dmp.getAdjustmentFlag() == 0 )
                    {
                        dupt.setPoint( Math.abs( dhc.getUsePoint() ) );
                    }
                    else if ( dmp.getAdjustmentFlag() == 1 )
                    {
                        dupt.setPoint( Math.abs( dhc.getUsePoint() ) * -1 );
                    }
                    dupt.setPointKind( dmp.getKind() );
                    dupt.setExtCode( dhc.getId() );
                    dupt.setMemo( "" );
                    // hh_user_point_payにすぐ追加するため、追加済みにする
                    dupt.setReflectDate( reflectDate );
                    dupt.setAddFlag( addFlag );
                    dupt.setIdm( dhc.getIdm() );
                    dupt.setUserSeq( dhc.getUserSeq() );
                    dupt.setVisitSeq( dhc.getVisitSeq() );
                    dupt.setRoomNo( dhc.getRoomNo() );
                    dupt.setThenPoint( thenPoint );
                    dupt.setHotenaviId( dhc.getVisitHotenaviId() );
                    // ユーザタイプをセット
                    dupt.setUserType( dhc.getUserType() );
                    dupt.setCiDate( dhc.getCiDate() );
                    dupt.setCiTime( dhc.getCiTime() );
                    ret = dupt.insertDataBySeq();

                }
                else
                {
                    ret = false;
                    nMaxSeq = 0;
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPayTemp.insertMile()] Exception=" + e.toString() );
                nMaxSeq = 0;
            }
            finally
            {
            }
        }
        return(nMaxSeq);
    }

    /**
     * マイル更新
     * 
     * @param datc
     * @param pointCode
     * @param thenPoint
     * @return
     */
    public int updateMile(DataHotelCi dhc, int pointCode, int thenPoint, int seq)
    {
        int reflectDate = 0;
        int addFlag = 0;
        boolean ret = false;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;

        dmp = new DataMasterPoint();
        dmp.getData( pointCode );

        ret = this.getMasterPointExtNum( dhc.getUserId(), dmp.getKind(), dmp.getCode(), dhc.getId(), dmp );
        if ( ret != false )
        {
            try
            {
                reflectDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                // 移行日が1日以上だったら移行日を加算
                if ( dmp.getShiftDay() > 0 )
                {
                    reflectDate = DateEdit.addDay( reflectDate, dmp.getShiftDay() );
                }
                else
                {
                    addFlag = ADD_FLAG_FINISH;
                }

                // hh_user_point_payと同じように現在ポイントから
                if ( thenPoint >= Math.abs( dhc.getUsePoint() ) )
                {
                    dupt = new DataUserPointPayTemp();
                    dupt.getData( dhc.getUserId(), seq );
                    dupt.setUserId( dhc.getUserId() );
                    dupt.setSeq( seq );

                    if ( dupt.getGetDate() == 0 )
                    {
                        dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    }
                    dupt.setCode( dmp.getCode() );
                    // ポイントの加減をチェック
                    if ( dmp.getAdjustmentFlag() == 0 )
                    {
                        dupt.setPoint( Math.abs( dhc.getUsePoint() ) );
                    }
                    else if ( dmp.getAdjustmentFlag() == 1 )
                    {
                        dupt.setPoint( Math.abs( dhc.getUsePoint() ) * -1 );
                    }
                    dupt.setPointKind( dmp.getKind() );
                    dupt.setExtCode( dhc.getId() );
                    dupt.setMemo( "" );
                    // hh_user_point_payにすぐ追加するため、追加済みにする
                    if ( dupt.getReflectDate() == 0 )
                    {
                        dupt.setReflectDate( reflectDate );
                    }
                    dupt.setAddFlag( addFlag );
                    dupt.setIdm( dhc.getIdm() );
                    dupt.setUserSeq( dhc.getUserSeq() );
                    dupt.setVisitSeq( dhc.getVisitSeq() );
                    dupt.setRoomNo( dhc.getRoomNo() );
                    dupt.setThenPoint( thenPoint );
                    dupt.setHotenaviId( dhc.getVisitHotenaviId() );
                    // ユーザタイプをセット
                    dupt.setUserType( dhc.getUserType() );
                    dupt.setCiDate( dhc.getCiDate() );
                    dupt.setCiTime( dhc.getCiTime() );
                    ret = dupt.updateData( dhc.getUserId(), seq );

                }
                else
                {
                    ret = false;
                    seq = 0;
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPayTemp.insertMile()] Exception=" + e.toString() );
                seq = 0;
            }
            finally
            {
            }
        }
        return(seq);
    }

    /**
     * 予約ボーナスマイルの更新
     * 
     * @param userId ユーザID
     * @param dhc チェックインデータ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * */
    public boolean setAddBonusMile(String userId, int thenPoint, int employeeCode, DataHotelCi dhc)
    {
        boolean ret = false;
        DataUserPointPayTemp duppt;
        UserPointPay upp;

        try
        {
            duppt = new DataUserPointPayTemp();
            duppt.setUserId( userId );
            if ( duppt.getData( userId, dhc.getRsvNo() ) )
            {
                duppt.setUserSeq( dhc.getUserSeq() );
                duppt.setVisitSeq( dhc.getVisitSeq() );
                duppt.setThenPoint( thenPoint );
                duppt.setEmployeeCode( employeeCode );
                duppt.setAddFlag( ADD_FLAG_FINISH );
                ret = duppt.updateData( userId, duppt.getSeq() ); // ユーザSeqとVisitSeqを更新
                if ( ret )
                {
                    upp = new UserPointPay();
                    upp.setAddBonusMile( userId, thenPoint, duppt.getPoint(), employeeCode, dhc );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPayTemp.setUsePoint] Exception=" + e.toString() );
        }
        finally
        {
        }
        return(ret);
    }

}
