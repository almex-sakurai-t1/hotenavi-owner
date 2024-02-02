/*
 * @(#)UserPoint.java 1.00 2007/08/23 Copyright (C) ALMEX Inc. 2007 ユーザポイント取得・更新クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.data.DataUserInvitation;
import jp.happyhotel.data.DataUserPoint;

/**
 * ユーザポイント取得・更新クラス。 ユーザのポイント情報を取得・更新する機能を提供する
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/27
 */
public class UserPoint implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 4658546477538933384L;

    private static final int  MYAREA_MAX       = 20;
    private static final int  MYHOTEL_MAX      = 20;

    private int               userPointCount;
    private DataUserPoint[]   userPoint;

    /**
     * データを初期化します。
     */
    public UserPoint()
    {
        userPointCount = 0;
    }

    public DataUserPoint[] getUserPoint()
    {
        return userPoint;
    }

    public int getUserPointCount()
    {
        return userPointCount;
    }

    /**
     * ユーザポイントを取得する（IDから）
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPointList(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_point";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ?";
        }
        else
        {
            return(false);
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
            Logging.info( "[UserPoint.getPointList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ユーザポイントを取得する（IDから･日付最新順･範囲/件数を指定）
     * 
     * @param userId ユーザID
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPointList(String userId, int countNum, int pageNum)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT  * FROM hh_user_point";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ?";
            // 2009年1月1日以降のデータを取得する
            query = query + " AND get_date >= 20090101 ";
        }
        else
        {
            return(false);
        }
        query = query + " ORDER BY get_date DESC, get_time DESC";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
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
            Logging.info( "[UserPoint.getPointList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
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
        // int pointCollect;
        // String query;
        // Connection connection = null;
        // ResultSet result = null;
        // PreparedStatement prestate = null;
        // UserBasicInfo ubi;
        // UserPointHistory uph;

        if ( userId.compareTo( "" ) == 0 )
        {
            return(0);
        }

        point = 0;
        // pointCollect = 0;

        // 年末失効ポイント履歴の計算
        // uph = new UserPointHistory();
        // uph.yearCollectUserPoint( userId );

        // 現在のポイント集計
        // this.collectUserPoint( userId );

        // if ( nowMonth != false )
        // {
        // query = "SELECT SUM(point) FROM hh_user_point";
        // query = query + " WHERE user_id = ?";
        // query = query + " AND get_date >= " + ((Integer.parseInt( DateEdit.getDate( 2 ) ) / 100) * 100);
        // query = query + " AND get_date <= " + ((Integer.parseInt( DateEdit.getDate( 2 ) ) / 100) * 100 + 99);
        // }
        // else
        // {
        // ユーザ情報の取得
        // ubi = new UserBasicInfo();
        // ubi.getUserBasic( userId );

        // pointCollect = ubi.getUserInfo().getPoint();

        // 最終集計日以降のもののみ取得する
        // query = "SELECT SUM(point) FROM hh_user_point";
        // query = query + " WHERE user_id = ?";
        // query = query + " AND get_date > " + ubi.getUserInfo().getPointUpdate();
        // }

        // try
        // {
        // connection = DBConnection.getConnection();
        // prestate = connection.prepareStatement( query );

        // if ( userId.compareTo( "" ) != 0 )
        // {
        // prestate.setString( 1, userId );
        // }
        // result = prestate.executeQuery();
        // if ( result != null )
        // {
        // if ( result.next() != false )
        // {
        // point = result.getInt( 1 );

        // if ( nowMonth == false )
        // {
        // point = point + pointCollect;
        // }
        // }
        // }

        // }
        // catch ( Exception e )
        // {
        // Logging.info( "[UserPoint.getNowPoint] Exception=" + e.toString() );
        // }
        // finally
        // {
        // DBConnection.releaseResources( result, prestate, connection );
        // }

        return(point);
    }

    /**
     * ユーザポイントを取得する（端末番号から）
     * 
     * @param mobileTermno 端末番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPointListByTermno(String mobileTermno)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_user_point.* FROM hh_user_point,hh_user_basic";

        if ( mobileTermno.compareTo( "" ) != 0 )
        {
            query = query + " WHERE hh_user_basic.mobile_termno = ?";
            query = query + " AND hh_user_basic.regist_status = 9";
            query = query + " AND hh_user_basic.del_flag = 0";
            query = query + " AND hh_user_basic.user_id = hh_user_point.user_id";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( mobileTermno.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, mobileTermno );
            }

            ret = getPointListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[UserPoint.getMyHotelListByTermno] Exception=" + e.toString() );
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
                this.userPoint = new DataUserPoint[this.userPointCount];
                for( i = 0 ; i < userPointCount ; i++ )
                {
                    userPoint[i] = new DataUserPoint();
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
            Logging.info( "[UserPoint.getUserPointSub] Exception=" + e.toString() );
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
     * 入会ポイントを加算する
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointJoin(String userId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // 入会ポイントの加算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPoint( userId, 1, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( 0 );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * マイエリアポイントを加算する
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointMyArea(String userId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;
        UserMyArea uma;

        ret = false;

        // マイエリア登録数が２０件を超えた場合はポイント加算しない
        uma = new UserMyArea();
        uma.getMyAreaListAll( userId );
        if ( uma.getCount() > MYAREA_MAX )
        {
            return(true);
        }

        // マイエリアポイントの加算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPoint( userId, 2, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( 0 );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * マイエリアポイントを加算する
     * 
     * @param userId ユーザID
     * @param jisCode 市区町村コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointMyArea(String userId, int jisCode)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;
        UserMyArea uma;

        ret = false;

        // マイエリア登録数が２０件を超えた場合はポイント加算しない
        uma = new UserMyArea();
        uma.getMyAreaListAll( userId );
        if ( uma.getCount() > MYAREA_MAX )
        {
            return(true);
        }

        // マイエリアポイントの加算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 2, jisCode, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( jisCode );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * ホテル詳細ポイントを加算する
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @param tabNo タブ番号(1:基本情報,2:部屋,3:地図,4:クーポン,5:クチコミ,6:ホテルHP)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointHotelDetail(String userId, int hotelId, int tabNo)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // ホテル詳細ポイントの加算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 3, hotelId, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( hotelId );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * スポンサーポイントを加算する
     * 
     * @param userId ユーザID
     * @param sponsorId スポンサーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointSponsor(String userId, int sponsorId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // スポンサーポイントの加算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 4, sponsorId, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( sponsorId );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * メルマガポイントを加算する
     * 
     * @param userId ユーザID
     * @param magId メルマガID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointMailmag(String userId, int magId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // メルマガポイントの加算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 5, magId, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( magId );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * マイホテルポイントを加算する
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointMyHotel(String userId, int hotelId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;
        UserMyHotel umh;

        ret = false;

        // マイホテル登録数が２０件を超えた場合はポイント加算しない
        umh = new UserMyHotel();
        umh.getMyHotelListAll( userId );
        if ( umh.getCount() > MYHOTEL_MAX )
        {
            return(true);
        }

        // マイホテルポイントの加算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 6, hotelId, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( hotelId );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * クチコミポイントを加算する
     * 
     * @param userId ユーザID
     * @param bbsId クチコミID
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointBbs(String userId, int bbsId, int hotelId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // メルマガポイントの加算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPointExtString( userId, 7, hotelId, Integer.toString( bbsId ), dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( hotelId );
            dup.setExtString( Integer.toString( bbsId ) );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * メルマガ登録ポイントを加算する
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointMailmagEntry(String userId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // マイエリアポイントの加算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPoint( userId, 8, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( 0 );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * おすすめホテルポイントを加算する
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointHotelRecommend(String userId, int hotelId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // おすすめホテルポイントの加算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 10, hotelId, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( hotelId );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * 問い合わせポイントを加算する
     * 
     * @param userId ユーザID
     * @param addPoint 加算ポイント数
     * @param extCode 付帯番号
     * @param extString 付帯文字列
     * @param personCode 登録担当者
     * @param appendReason 登録理由
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointInquiry(String userId, int addPoint, int extCode, String extString, String personCode, String appendReason)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // おすすめホテルポイントの加算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 11, extCode, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( addPoint );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( extCode );
            dup.setExtString( extString );
            dup.setPersonCode( personCode );
            dup.setAppendReason( appendReason );
            ret = dup.insertData();
        }

        return(ret);
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
            Logging.info( "[UserPoint.getMasterPoint] Exception=" + e.toString() );
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
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND code = ?";
                query = query + " AND point_kind = ?";
                query = query + " AND get_date = ?";

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
                    Logging.info( "[UserPoint.getMasterPoint] Exception=" + e.toString() );
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
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND code = ?";
                query = query + " AND point_kind = ?";

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
                    Logging.info( "[UserPoint.getMasterPoint] Exception=" + e.toString() );
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
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND code = ?";
                query = query + " AND point_kind = ?";
                if ( startDate > 0 )
                {
                    query += " AND get_date >= " + startDate;
                }
                query += " AND get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );

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
                    Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
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
            Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
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
            else if ( dmp.getLimitFlag() == 1 )// 1日1回
            {
                // 必ずfalseを返す。20151030.sakurai　スロークエリになっているので発行させない。
                ret = false;
                DBConnection.releaseResources( connection );

                // 本日のポイント取得履歴を取得
                // query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                // query = query + " AND point_kind = ?";
                // query = query + " AND get_date = ?";
                // query = query + " AND ext_code = ?";

                // ret = true;
                // try
                // {
                // prestate = connection.prepareStatement( query );
                // if ( prestate != null )
                // {
                // prestate.setString( 1, userId );
                // prestate.setInt( 2, pointKind );
                // prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                // prestate.setInt( 4, extCode );
                // result = prestate.executeQuery();
                // if ( result != null )
                // {
                // if ( result.next() != false )
                // {
                // もしデータがあった場合は加算対象としない
                // ret = false;
                // }
                // }
                // }
                // }
                // catch ( Exception e )
                // {
                // Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
                // ret = false;
                // }
                // finally
                // {
                // DBConnection.releaseResources( result, prestate, connection );
                // }
            }
            else if ( dmp.getLimitFlag() == 2 )
            {
                // 過去のポイントを取得
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND point_kind = ?";
                query = query + " AND ext_code = ?";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, pointKind );
                        prestate.setInt( 3, extCode );
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
                    Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point* FROM hh_user_point";
                query += " WHERE user_id = ?";
                query += " AND point_kind = ?";
                query += " AND ext_code = ?";
                if ( startDate > 0 )
                {
                    query += " AND get_date >= " + startDate;
                }
                query += " AND hh_get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );
                query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
                query += " limit 0,1";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, pointKind );
                        prestate.setInt( 3, extCode );
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
                    Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
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
            Logging.info( "[UserPoint.getMasterPointExtString] Exception=" + e.toString() );
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
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND code = ?";
                query = query + " AND point_kind = ?";
                query = query + " AND get_date = ?";
                query = query + " AND ext_code = ?";
                query = query + " AND ext_string = ?";

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
                    Logging.info( "[UserPoint.getMasterPointExtString] Exception=" + e.toString() );
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
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND code = ?";
                query = query + " AND point_kind = ?";
                query = query + " AND ext_code = ?";
                query = query + " AND ext_string = ?";

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
                    Logging.info( "[UserPoint.getMasterPointExtString] Exception=" + e.toString() );
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
                query = "SELECT * FROM hh_user_point";
                query += " WHERE user_id = ?";
                query += " AND code = ?";
                query += " AND point_kind = ?";
                query += " AND ext_code = ?";
                query += " AND ext_string = ?";
                if ( startDate > 0 )
                {
                    query += " AND get_date >= " + startDate;
                }
                query += " AND get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );

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
                    Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
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
     * 取得デコメを確認する
     * 
     * @param userId ユーザーID
     * @param code ポイントコード
     * @param extCode デコメコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDecomeConfirm(String userId, int code, int extCode)
    {
        boolean ret;
        boolean boolGot;
        DataMasterPoint dmp;
        ret = false;

        // デコメのポイント減算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPointDecome( userId, 12, code, extCode, dmp );
        if ( ret != false )
        {
            boolGot = false;
        }
        else
        {
            boolGot = true;
        }
        return(boolGot);
    }

    /**
     * デコメ取得ポイントを減算する
     * 
     * @param userId ユーザーID
     * @param code ポイントコード
     * @param extCode デコメ管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointDecome(String userId, int code, int extCode)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // デコメのポイント減算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPointDecome( userId, 12, code, extCode, dmp );
        Logging.info( "[getMasterPoint]" + ret );
        if ( ret != false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setCode( dmp.getCode() );
                dup.setPoint( -dmp.getAddPoint() );
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( extCode );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointDecome] Exception=" + e.toString() );
            }
            finally
            {

            }
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
    private boolean getMasterPointDecome(String userId, int pointKind, int code, int extCode, DataMasterPoint dmp)
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
            Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND code = ?";
                query = query + " AND point_kind = ?";
                query = query + " AND get_date = ?";
                query = query + " AND ext_code = ?";

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
                    Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND code = ?";
                query = query + " AND point_kind = ?";
                query = query + " AND ext_code = ?";

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
                    Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
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
     * 紹介ポイントを加算する
     * 
     * @param invitationId 紹介ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointInvitation(String invitationId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserInvitation dui;
        DataUserPoint dup;

        ret = false;

        dui = new DataUserInvitation();
        dmp = new DataMasterPoint();
        ret = dui.getData( invitationId );
        if ( ret != false )
        {
            if ( dui.getAddFlag() == 0 )
            {

                // 紹介者のポイントを追加
                ret = getMasterPoint( dui.getUserId(), 14, dmp );
                if ( ret != false )
                {
                    try
                    {
                        dup = new DataUserPoint();
                        dup.setUserId( dui.getUserId() );
                        dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        dup.setCode( dmp.getCode() );
                        dup.setPoint( dmp.getAddPoint() );
                        dup.setPointKind( dmp.getKind() );
                        ret = dup.insertData();
                    }
                    catch ( Exception e )
                    {
                        Logging.info( "[UserPoint.setPointInvitation ] Exception=" + e.toString() );
                    }
                    finally
                    {
                    }
                }
                // 入会者のポイントを追加
                ret = getMasterPoint( dui.getEntryId(), 14, dmp );
                if ( ret != false )
                {
                    try
                    {
                        dup = new DataUserPoint();
                        dup.setUserId( dui.getEntryId() );
                        dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        dup.setCode( dmp.getCode() );
                        dup.setPoint( dmp.getAddPoint() );
                        dup.setPointKind( dmp.getKind() );
                        ret = dup.insertData();
                    }
                    catch ( Exception e )
                    {
                        Logging.info( "[UserPoint.setPointInvitation ] Exception=" + e.toString() );
                    }
                    finally
                    {
                    }
                }
                return(ret);
            }
            else
            {
                return(false);
            }
        }
        else
        {
            return(false);
        }
    }

    /**
     * 応募ポイントを減算する
     * 
     * @param userId ユーザーID
     * @param pointCode ポイントコード
     * @param extCode コード番号
     * @param applicataionCount 応募口数
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointPresent(String userId, int pointCode, int extCode, int applicationCount)
    {
        int i;
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        dmp = new DataMasterPoint();
        dup = new DataUserPoint();

        i = 0;
        ret = getMasterPointDecome( userId, 13, pointCode, extCode, dmp );
        if ( ret != false )
        {
            for( i = 0 ; i < applicationCount ; i++ )
            {
                try
                {
                    dup = new DataUserPoint();
                    dup.setUserId( userId );
                    dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    dup.setCode( dmp.getCode() );
                    dup.setPoint( -dmp.getAddPoint() );
                    dup.setPointKind( dmp.getKind() );
                    dup.setExtCode( extCode );
                    ret = dup.insertData();
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPoint.setPointPresent ] Exception=" + e.toString() );
                }
                finally
                {
                }
            }
        }
        return(ret);
    }

    /**
     * クチコミ投票ポイントを加算する
     * 
     * @param userId ユーザID
     * @param bbsId クチコミID
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointBbsVote(String userId, int bbsId, int hotelId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // クチコミ投票ポイントの加算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPointExtString( userId, 15, hotelId, Integer.toString( bbsId ), dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( hotelId );
            dup.setExtString( Integer.toString( bbsId ) );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * アンケート投稿ポイントを加算する
     * 
     * @param userId ユーザID
     * @param questionId アンケートID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointEnquete(String userId, int questionId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // アンケートの加算対象チェック（）
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 16, questionId, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( questionId );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * ホテル提供応募ポイントを減算する
     * 
     * @param userId ユーザーID
     * @param pointCode ポイントコード
     * @param extCode コード番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointOfferHotelPresent(String userId, int pointCode, int extCode)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        dmp = new DataMasterPoint();
        dup = new DataUserPoint();

        ret = getMasterPointDecome( userId, 17, pointCode, extCode, dmp );
        if ( ret != false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setCode( dmp.getCode() );
                dup.setPoint( -dmp.getAddPoint() );
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( extCode );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointPresent ] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * 応募ポイントを減算する
     * 
     * @param userId ユーザーID
     * @param pointCode ポイントコード
     * @param formId フォームID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointUserApply(String userId, int pointCode, int formId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        dmp = new DataMasterPoint();
        dup = new DataUserPoint();
        ret = dmp.getData( pointCode );
        if ( ret == false )
        {
            return false;
        }

        ret = getMasterPointDecome( userId, dmp.getKind(), pointCode, formId, dmp );
        if ( ret != false )
        {
            try
            {
                dup = new DataUserPoint();
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
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointPresent ] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * 相性占いのポイント履歴を確認する
     * 
     * @param userId ユーザーID
     * @param code ポイントコード
     * @param loveConstellation 相手の星座の管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getAffinityConfirm(String userId, int loveConstellation)
    {
        boolean ret;
        boolean boolGot;
        DataMasterPoint dmp;
        ret = false;

        dmp = new DataMasterPoint();
        // ポイント減算済みかを確認する
        ret = getMasterPointExt( userId, 19, loveConstellation, dmp );
        if ( ret != false )
        {
            boolGot = false;
        }
        else
        {
            boolGot = true;
        }
        return(boolGot);

    }

    /**
     * 相性占いの使用ポイントを減算する
     * 
     * @param userId ユーザーID
     * @param pointCode ポイントコード
     * @param loveConstellation 相手の星座の管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointAffinity(String userId, int loveConstellation)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        dmp = new DataMasterPoint();
        dup = new DataUserPoint();

        ret = getMasterPointExt( userId, 19, loveConstellation, dmp );
        if ( ret != false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setCode( dmp.getCode() );
                dup.setPoint( -dmp.getAddPoint() );
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( loveConstellation );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointAffinity() ] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * デコメ取得ポイントを減算する
     * 
     * @param userId ユーザーID
     * @param kind ポイント区分
     * @param code ポイントコード
     * @param extCode デコメ管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointDownload(String userId, int kind, int code, int extCode)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // デコメのポイント減算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPointDecome( userId, kind, code, extCode, dmp );
        Logging.info( "[getMasterPoint]" + ret );
        if ( ret != false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setCode( dmp.getCode() );
                dup.setPoint( -dmp.getAddPoint() );
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( extCode );
                dup.setAppendReason( "" );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointDownload] Exception=" + e.toString() );
            }
            finally
            {

            }
        }
        return(ret);
    }

    /**
     * デコメ取得ポイントを減算する
     * 
     * @param userId ユーザーID
     * @param kind ポイント区分
     * @param code ポイントコード
     * @param extCode デコメ管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointDownloadNoPoint(String userId, int kind, int code, int extCode)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // デコメのポイント減算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPointDecome( userId, kind, code, extCode, dmp );
        Logging.info( "[getMasterPoint]" + ret );
        if ( ret != false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setCode( dmp.getCode() );
                dup.setPoint( 0 );
                dup.setAppendReason( "有料会員のためポイント消費無し" );
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( extCode );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointDownloadPay] Exception=" + e.toString() );
            }
            finally
            {

            }
        }
        return(ret);
    }

    /**
     * 加算・減算ポイント取得クラス
     * 
     * @param userId ユーザーID
     * @param startDate 集計開始日
     * @param endDate 集計終了日
     * @param 加算フラグ（true:プラス分のみ、false:マイナス分のみ）
     * @return 処理結果(集計ポイント)
     **/
    public int getUserTotalPointOneSide(String userId, int startDate, int endDate, boolean plusFlag)
    {
        int point;
        // String query;
        // Connection connection = null;
        // ResultSet result = null;
        // PreparedStatement prestate = null;

        point = 0;
        // 指定期間のポイントを取得（加算ポイントまたは減算ポイントのみ取得）
        // query = "SELECT SUM(point) as point_total FROM hh_user_point WHERE user_id= ?";
        // query = query + " AND get_date BETWEEN ? AND ?";
        // if ( plusFlag == false )
        // {
        // query = query + " AND point < 0";
        // }
        // else
        // {
        // query = query + " AND point >= 0";
        // }
        // try
        // {
        // connection = DBConnection.getConnection();
        // prestate = connection.prepareStatement( query );
        // if ( prestate != null )
        // {
        // prestate.setString( 1, userId );

        // prestate.setInt( 2, startDate );
        // prestate.setInt( 3, endDate );
        // result = prestate.executeQuery();
        // if ( result != null )
        // {
        // if ( result.next() != false )
        // {
        // point = result.getInt( 1 );
        // }
        // }
        // }
        // }
        // catch ( Exception e )
        // {
        // Logging.info( "[UserPoint.getUserTotalPointOneSide] Exception=" + e.toString() );
        // }
        // finally
        // {
        // DBConnection.releaseResources( result, prestate, connection );
        // }

        return(point);
    }

    /**
     * ポイント取得クラス（期間）
     * 
     * @param userId ユーザーID
     * @param startDate 集計開始日
     * @param endDate 集計終了日
     * @return 処理結果(集計ポイント)
     **/
    public int getUserPoint(String userId, int startDate, int endDate)
    {
        int point;
        // String query;
        // Connection connection = null;
        // ResultSet result = null;
        // PreparedStatement prestate = null;

        point = 0;
        // 指定期間のポイントを取得（加算ポイントまたは減算ポイントのみ取得）
        // query = "SELECT SUM(point) as point_total FROM hh_user_point WHERE user_id= ?";
        // query = query + " AND get_date BETWEEN ? AND ?";
        // try
        // {
        // connection = DBConnection.getConnection();
        // prestate = connection.prepareStatement( query );
        // if ( prestate != null )
        // {
        // prestate.setString( 1, userId );
        // prestate.setInt( 2, startDate );
        // prestate.setInt( 3, endDate );
        // result = prestate.executeQuery();
        // if ( result != null )
        // {
        // if ( result.next() != false )
        // {
        // point = result.getInt( 1 );
        // }
        // }
        // }
        // }
        // catch ( Exception e )
        // {
        // Logging.info( "[UserPoint.getUserPoint] Exception=" + e.toString() );
        // }
        // finally
        // {
        // DBConnection.releaseResources( result, prestate, connection );
        // }

        return(point);
    }

    /**
     * 年末失効ポイントデータ取得
     * 
     * @param userId ユーザID
     * @param date 日付
     * @return 処理結果（true:データあり、false:データなし）
     */
    public boolean getLostPoint(String userId, int date)
    {
        boolean ret;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query;
        ret = false;

        query = "SELECT * FROM hh_user_point where user_id = ?" +
                " AND get_date = ?" +
                " AND point_kind = 18";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setString( 1, userId );
                prestate.setInt( 2, date );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPoint.getLostPoint] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 年末失効ポイントデータセット
     * 
     * @param userId
     * @param date
     * @param point
     * @return 処理結果（true:追加成功、false:追加失敗）
     * 
     */
    public boolean setLostPoint(String userId, int date, int point)
    {
        final int TIME = 235959;
        final int POINT_CODE = 18;
        final int POINT_KIND = 18;
        boolean ret;
        DataUserPoint dup;

        dup = new DataUserPoint();

        ret = getLostPoint( userId, date );
        if ( ret == false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( date );
                dup.setGetTime( TIME );
                dup.setCode( POINT_CODE );
                dup.setPoint( Math.abs( point ) * -1 );
                dup.setPointKind( POINT_KIND );
                dup.setExtCode( date / 10000 );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointAffinity() ] Exception=" + e.toString() );
                ret = false;
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * ポイント履歴一覧取得
     * 
     * @param userId
     * @param date
     * @param point
     * @return 処理結果（true:追加成功、false:追加失敗）
     * 
     */
    public boolean setPointHistory(String userId, int date, int point)
    {
        final int TIME = 235959;
        final int POINT_CODE = 18;
        final int POINT_KIND = 18;
        boolean ret;
        DataUserPoint dup;

        dup = new DataUserPoint();

        ret = getLostPoint( userId, date );
        if ( ret == false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( date );
                dup.setGetTime( TIME );
                dup.setCode( POINT_CODE );
                dup.setPoint( Math.abs( point ) * -1 );
                dup.setPointKind( POINT_KIND );
                dup.setExtCode( date / 10000 );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointAffinity() ] Exception=" + e.toString() );
                ret = false;
            }
            finally
            {
            }
        }
        return(ret);
    }

    /***
     * ユーザポイントデータ集計処理
     * 
     * @param userId
     */
    public void collectUserPoint(String userId)
    {
        boolean ret = false;
        int collectDate;
        int latestPoint = 0;
        DataUserBasic dub;
        dub = new DataUserBasic();

        ret = dub.getData( userId );
        if ( ret != false )
        {
            // 正会員のみポイント集計対象とする
            if ( dub.getRegistStatus() == 9 && dub.getDelFlag() == 0 )
            {
                // 昨日の日付を取得
                collectDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 );
                if ( collectDate > dub.getPointUpdate() )
                {
                    // ポイント更新日以降～昨日までのポイントを取得
                    latestPoint = this.getUserPoint( userId, DateEdit.addDay( dub.getPointUpdate(), 1 ), collectDate );
                    if ( latestPoint != 0 )
                    {
                        // 合計値がマイナスになるようだったら0をセット
                        if ( dub.getPoint() + latestPoint > 0 )
                        {
                            dub.setPoint( dub.getPoint() + latestPoint );
                        }
                        else
                        {
                            dub.setPoint( 0 );
                        }
                        dub.setPointUpdate( collectDate );
                        ret = dub.updateData( userId, "クラスでポイント集計" );
                        Logging.info( "[UserPoint.collectUserPoint:userID -> " + userId + ", amaountPoint ->" + dub.getPoint() + "]" );
                    }
                }
            }
        }
    }

    /**
     * ユーザポイントを取得する（IDから）
     * 
     * @param userId ユーザID
     * @param nowMonth 今月獲得数フラグ(true:今月獲得数)
     * @return ポイント数
     */
    public int getNowPointDebug(String userId, boolean nowMonth)
    {
        int point;
        int pointCollect;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        UserBasicInfo ubi;
        UserPointHistory uph;

        if ( userId.compareTo( "" ) == 0 )
        {
            return(0);
        }

        point = 0;
        pointCollect = 0;

        // 年末失効ポイント履歴の計算
        uph = new UserPointHistory();
        uph.yearCollectUserPoint( userId );

        // 現在のポイント集計
        this.collectUserPoint( userId );

        if ( nowMonth != false )
        {
            query = "SELECT SUM(point) FROM hh_user_point";
            query = query + " WHERE user_id = ?";
            query = query + " AND get_date >= " + ((Integer.parseInt( DateEdit.getDate( 2 ) ) / 100) * 100);
            query = query + " AND get_date <= " + ((Integer.parseInt( DateEdit.getDate( 2 ) ) / 100) * 100 + 99);
        }
        else
        {
            // ユーザ情報の取得
            ubi = new UserBasicInfo();
            ubi.getUserBasic( userId );

            pointCollect = ubi.getUserInfo().getPoint();

            // 最終集計日以降のもののみ取得する
            query = "SELECT SUM(point) FROM hh_user_point";
            query = query + " WHERE user_id = ?";
            query = query + " AND get_date > " + ubi.getUserInfo().getPointUpdate();
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

                    if ( nowMonth == false )
                    {
                        point = point + pointCollect;
                    }
                }
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[UserPoint.getNowPointDebug] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(point);
    }

    /**
     * ホテル詳細ポイントを加算する(API向け)
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @param tabNo タブ番号(1:基本情報,2:部屋,3:地図,4:クーポン,5:クチコミ,6:ホテルHP,20:アプリ)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setPointHotelDetail(String userId, int hotelId)
    {
        final int POINT_CODE_IPHONE = 20;
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;
        int pointCode = 0;

        ret = false;

        // ホテル詳細ポイントの加算対象チェック
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 3, hotelId, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( pointCode ); // ポイントマスタのcodeをセット
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( hotelId );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }
}
