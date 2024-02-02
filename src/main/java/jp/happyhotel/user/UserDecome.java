/*
 * @(#)UserBasicInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ユーザ基本情報取得クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterDecome;
import jp.happyhotel.data.DataMasterName;
import jp.happyhotel.data.DataMasterPoint;

/**
 * ユーザ基本情報取得クラス。 ユーザの基本情報を取得する機能を提供する
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/26
 */
public class UserDecome implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -3924291538252750667L;

    private int                userDecomeCount;
    private int                userDecomeAllCount;
    private DataMasterDecome[] userDecome;
    private DataMasterName[]   masterName;
    private DataMasterPoint[]  masterPoint;

    /**
     * データを初期化します。
     */
    public UserDecome()
    {
        userDecomeCount = 0;
        userDecomeAllCount = 0;
    }

    public int getCount()
    {
        return(userDecomeCount);
    }

    public int getAllCount()
    {
        return(userDecomeAllCount);
    }

    public DataMasterDecome[] getDecomeInfo()
    {
        return(userDecome);
    }

    /** マスターネーム取得 **/
    public DataMasterName[] getMasterNameInfo()
    {
        return(masterName);
    }

    public DataMasterPoint[] getMasterPoint()
    {
        return(masterPoint);
    }

    /**
     * デコメ情報を取得する（IDから）
     * 
     * @param category カテゴリ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDecome(int category)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( category < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_decome";
        query = query + " WHERE category = ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, category );

            ret = getDecomeSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.error( "[getUserDecome] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * デコメ情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getDecomeSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;
        int i;

        i = 0;
        count = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    userDecomeCount = result.getRow();
                }
                this.userDecome = new DataMasterDecome[this.userDecomeCount];
                for( i = 0 ; i < userDecomeCount ; i++ )
                {
                    userDecome[i] = new DataMasterDecome();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // デコメデータ情報の設定
                    this.userDecome[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getDecomeSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userDecomeCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ネームマスタを取得する（クラスから）
     * 
     * @param class クラスID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterName(int classCode)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( classCode < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_name";
        query = query + " WHERE class = ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, classCode );

            ret = getMasterNameSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.error( "[getUserDecome_getMasterName] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ネームマスタ情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMasterNameSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;
        int i;

        i = 0;
        count = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    userDecomeCount = result.getRow();
                }
                this.masterName = new DataMasterName[this.userDecomeCount];
                for( i = 0 ; i < userDecomeCount ; i++ )
                {
                    masterName[i] = new DataMasterName();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ネームマスタ情報の設定
                    this.masterName[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getMasterNameSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userDecomeCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * デコメ情報を取得する（IDから,ページ,表示件数指定）
     * 
     * @param classCode クラスコード
     * @param category カテゴリ
     * @param countNum 表示件数
     * @param pageNum ページ数
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDecome(int classCode, int category, int countNum, int pageNum)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( category < 0 )
        {
            return(false);
        }

        query = "SELECT hh_master_decome.*, hh_master_point.*";
        query = query + " FROM hh_master_decome, hh_master_point";
        query = query + " WHERE hh_master_decome.point_code = hh_master_point.code";
        query = query + " AND hh_master_decome.class = ?";
        query = query + " AND hh_master_decome.category = ?";
        query = query + " AND hh_master_decome.start_date <= ?";
        query = query + " AND hh_master_decome.end_date >= ?";
        query = query + "  ORDER BY hh_master_decome.start_date DESC, hh_master_decome.disp_pos DESC";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, classCode );
            prestate.setInt( 2, category );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getDecomeInfoSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[UserDecome.getDecome()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        // デコメ全件数
        query = "SELECT COUNT(*) FROM hh_master_decome, hh_master_point";
        query = query + " WHERE hh_master_decome.point_code = hh_master_point.code";
        query = query + " AND hh_master_decome.class = ?";
        query = query + " AND hh_master_decome.category = ?";
        query = query + " AND hh_master_decome.start_date <= ?";
        query = query + " AND hh_master_decome.end_date >= ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, classCode );
            prestate.setInt( 2, category );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // 総件数の取得
                    this.userDecomeAllCount = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserDecome.getDecome()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * デコメ情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @see デコメデータとポイントデータを取得する
     */
    private boolean getDecomeInfoSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;
        int i;

        i = 0;
        count = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    userDecomeCount = result.getRow();
                }
                this.userDecome = new DataMasterDecome[this.userDecomeCount];
                this.masterPoint = new DataMasterPoint[this.userDecomeCount];
                for( i = 0 ; i < userDecomeCount ; i++ )
                {
                    this.userDecome[i] = new DataMasterDecome();
                    this.masterPoint[i] = new DataMasterPoint();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // デコメデータ情報の設定
                    this.userDecome[count].setData( result );
                    this.masterPoint[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getDecomeSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userDecomeCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

}
