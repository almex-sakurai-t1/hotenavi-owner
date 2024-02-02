/*
 * @(#)UserPointHistory.java 1.00
 * 2010/11/22 Copyright (C) ALMEX Inc. 2010
 * ユーザポイント履歴
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.data.DataUserPointHistory;

/**
 * ユーザーに表示する地図を取得する。
 * 
 * @author S.Tashiro
 * @version 1.00 2009/07/30
 */
public class UserPointHistory implements Serializable
{
    /**
     *
     */
    private static final long    serialVersionUID = -1475978782029948447L;
    private static final int     NEW_YEARS_EVE    = 1231;

    private boolean              PLUS_FLAG        = true;
    private boolean              MINUS_FLAG       = false;
    private DataUserPointHistory duph;
    private int                  nLostPointLatest;

    /**
     * データを初期化します。
     */
    public UserPointHistory()
    {
        this.nLostPointLatest = 0;
    }

    public DataUserPointHistory getUserPointHistory()
    {
        return(duph);
    }

    public int getLostPointLatest()
    {
        return(this.nLostPointLatest);
    }

    /***
     * ユーザポイント履歴データ取得
     * 
     * @param userId ユーザーID
     * @return 処理結果(TRUE:成功,FALSE:失敗)
     */
    public boolean getData(String userId, int manageYear)
    {
        boolean ret;
        ret = false;

        // クラスが割り当てられていたら一旦nullをセット
        if ( this.duph != null )
        {
            this.duph = null;
            this.duph = new DataUserPointHistory();
        }
        else
        {
            this.duph = new DataUserPointHistory();
        }
        ret = this.duph.getData( userId, manageYear );
        // 返ってきたデータが正しいかチェックを行う。
        if ( ret != false )
        {
            if ( this.duph.getUserId().compareTo( userId ) == 0 )
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

    /***
     * ユーザポイント履歴データ取得
     * 
     * @param userId ユーザーID
     * @return 処理結果(TRUE:成功,FALSE:失敗)
     */
    public boolean getLatestData(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        if ( this.duph != null )
        {
            this.duph = null;
            this.duph = new DataUserPointHistory();
        }

        // 管理年は現在日付以前のものを取得
        query = "SELECT * FROM hh_user_point_history WHERE user_id = ?" +
                " AND manage_year <= " + Integer.parseInt( DateEdit.getDate( 2 ) ) / 10000 +
                " ORDER BY manage_year DESC";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.duph.setData( result );
                }
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserPointHistory.getLatestData] Exception:" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /***
     * 年末失効ポイント集計処理
     * 
     * @param userId
     * @return 処理結果(TRUE:成功,FALSE:失敗)
     */
    public boolean yearCollectUserPoint(String userId)
    {
        boolean ret;
        int collectDate;
        int startDate;
        int endDate;
        DataUserBasic dub;

        ret = false;

        if ( this.duph == null )
        {
            this.duph = new DataUserPointHistory();
        }
        dub = new DataUserBasic();

        ret = dub.getData( userId );
        if ( ret != false )
        {
            // 正会員以外はfalse
            if ( dub.getRegistStatus() != 9 && dub.getDelFlag() != 0 )
            {
                return(false);
            }
        }
        else
        {
            return(false);
        }

        // 昨日の日付を取得
        collectDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 );

        // データがないユーザは本年のポイントを集計する
        startDate = (collectDate / 10000) * 10000;
        endDate = collectDate;

        // ポイント履歴の最新の年データを取得
        ret = this.getLatestData( userId );
        if ( this.duph.getManageYear() > 0 )
        {
            // 最新履歴のデータと年が違っていたら年末失効ポイントを計算する
            if ( collectDate / 10000 > this.duph.getManageYear() )
            {
                int collectYear = this.duph.getManageYear();
                while( collectYear < collectDate / 10000 )
                {
                    // 開始日と終了日を管理年に変更
                    startDate = collectYear * 10000 + 101;
                    endDate = collectYear * 10000 + 9999;

                    // その年の年末失効ポイントを集計し、年末失効ポイントを追加する
                    ret = setLostPoint( userId, startDate, endDate, true );
                    collectYear++;
                }
            }
            else if ( collectDate / 10000 < this.duph.getManageYear() )
            {
                Logging.error( "[UserPointHistory.yearCollectUserPoint()] 集計日より未来のデータがあります。" );
                return(false);
            }
        }

        // 年末失効ポイントを計算する
        startDate = (collectDate / 10000) * 10000 + 101;
        endDate = collectDate;

        // 年末失効ポイントを計算すべきかどうかを確認する（集計対象日より更新日が小さかったら集計する）
        if ( collectDate > this.duph.getLastUpdate() )
        {
            if ( collectDate % 10000 == NEW_YEARS_EVE )
            {
                ret = setLostPoint( userId, startDate, endDate, true );
            }
            else
            {
                ret = setLostPoint( userId, startDate, endDate, false );
            }
        }
        else
        {
            ret = true;
            this.nLostPointLatest = this.duph.getLostPoint();
        }

        return(ret);
    }

    /***
     * 年末失効ポイント集計処理
     * 
     * @param userId
     * @param startDate
     * @param endDate
     * @param lostFlag
     * @return 処理結果(TRUE:成功,FALSE:失敗)
     */
    public boolean setLostPoint(String userId, int startDate, int endDate, boolean lostFlag)
    {
        boolean ret;
        boolean boolThisYear;
        int lastYear;
        int lastYearPoint;
        int thisYear;
        int thisYearPointPlus = 0;
        int thisYearPointMinus = 0;
        int collectDate;
        UserPoint up;

        // 昨日の日付を取得
        collectDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 );

        thisYear = startDate / 10000;
        lastYear = DateEdit.addYear( startDate, -1 ) / 10000;

        if ( this.duph != null )
        {
            this.duph = null;
            this.duph = new DataUserPointHistory();
        }
        up = new UserPoint();

        // 昨年のポイントを取得
        ret = this.getData( userId, lastYear );
        lastYearPoint = this.duph.getPoint();

        // 加算ポイントの集計
        // thisYearPointPlus = up.getUserTotalPointOneSide( userId, startDate, endDate, PLUS_FLAG );
        // 減算ポイントの集計
        // thisYearPointMinus = up.getUserTotalPointOneSide( userId, startDate, endDate, MINUS_FLAG );

        if ( lastYearPoint - Math.abs( thisYearPointMinus ) > 0 )
        {
            this.nLostPointLatest = lastYearPoint - Math.abs( thisYearPointMinus );
        }
        else
        {
            this.nLostPointLatest = 0;
        }

        if ( this.duph != null )
        {
            this.duph = null;
            this.duph = new DataUserPointHistory();
        }

        // 最後に更新・挿入を判断するためのフラグ
        boolThisYear = this.getData( userId, thisYear );

        // hh_uesr_point_historyの更新
        this.duph.setUserId( userId );
        this.duph.setManageYear( thisYear );
        // 年末時点でのポイント（去年のポイント + 今年のポイント）
        this.duph.setPoint( lastYearPoint + thisYearPointPlus - Math.abs( thisYearPointMinus ) - this.nLostPointLatest );
        this.duph.setPlusPoint( thisYearPointPlus );
        this.duph.setMinusPoint( thisYearPointMinus );
        this.duph.setLostPoint( Math.abs( this.nLostPointLatest ) * -1 );

        if ( (collectDate / 10000 > thisYear) || (collectDate % 10000 == NEW_YEARS_EVE) )
        {
            // 集計日以前の年なので、年末の日付で更新
            collectDate = thisYear * 10000 + NEW_YEARS_EVE;
            this.duph.setLastUpdate( collectDate );
            this.duph.setLastUptime( 235959 );
        }
        else
        {
            this.duph.setLastUpdate( collectDate );
            this.duph.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        }

        if ( boolThisYear != false )
        {
            ret = this.duph.updateData( userId, thisYear );
        }
        else
        {
            ret = this.duph.insertData();
        }
        if ( lostFlag != false )
        {
            // 年末失効ポイント履歴の追加
            up.setLostPoint( userId, collectDate, this.nLostPointLatest );
        }
        return(ret);
    }

    /**
     * 年末失効ポイントを取得
     * 
     * 
     */
    public boolean getLostPoint(String userId)
    {
        int nMinusPoint = 0;
        boolean ret;
        ret = this.getData( userId, Integer.parseInt( DateEdit.getDate( 2 ) ) / 10000 );

        if ( ret != false )
        {
            this.nLostPointLatest = this.duph.getLostPoint();
            // ロストポイントがあれば集計日以降のマイナスポイントを集計
            if ( this.nLostPointLatest < 0 )
            {
                // 集計日以降のマイナスポイント取得
                // nMinusPoint = up.getUserTotalPointOneSide( userId, DateEdit.addDay( this.duph.getLastUpdate(), 1 ), Integer.parseInt( DateEdit.getDate( 2 ) ), false );
                if ( Math.abs( this.nLostPointLatest ) - Math.abs( nMinusPoint ) > 0 )
                {
                    this.nLostPointLatest = Math.abs( this.nLostPointLatest ) - Math.abs( nMinusPoint );
                }
                else
                {
                    this.nLostPointLatest = 0;
                }

            }
            else
            {
                this.nLostPointLatest = 0;
            }

        }
        else
        {
            this.nLostPointLatest = 0;
        }
        return(ret);
    }
}
