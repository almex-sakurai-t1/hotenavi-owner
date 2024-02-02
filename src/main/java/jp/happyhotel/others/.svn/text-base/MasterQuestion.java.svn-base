/*
 * @(#)MasterQuestion.java 1.00 2008/05/14 Copyright (C) ALMEX Inc. 2007 アンケートマスタ取得クラス
 */

package jp.happyhotel.others;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * アンケートマスタ取得クラス。 アンケートマスタの情報を取得する機能を提供する
 * 
 * @author S.Tashiro
 * @version 1.00 2008/05/14
 */
public class MasterQuestion implements Serializable
{

    /**
     *
     */
    private static final long    serialVersionUID = -659576481267252802L;
    private final int            NOT_REALFLAG     = 0;
    private final int            REALFLAG         = 1;

    private int                  masterQuestionCount;
    private int                  masterQuestionDataCount;
    private DataMasterQuestion   masterQuestion;
    private DataMasterQuestion[] masterQuestionMulti;
    private int[]                masterQuestionNo;

    /**
     * データを初期化します。
     */
    public MasterQuestion()
    {
        masterQuestionCount = 0;
        masterQuestionDataCount = 0;
    }

    /** アンケートマスタ情報件数取得 **/
    public int getQuestionCount()
    {
        return(masterQuestionCount);
    }

    /** アンケート質問マスタ情報件数取得 **/
    public int getQuestionDataCount()
    {
        return(masterQuestionDataCount);
    }

    /** アンケート番号取得 **/
    public int[] getQuestionNo()
    {
        return(masterQuestionNo);
    }

    /** アンケートマスタ情報取得 **/
    public DataMasterQuestion getQuestionInfo()
    {
        return(masterQuestion);
    }

    /** アンケートマスタ情報取得 **/
    public DataMasterQuestion[] getQuestionInfoMulti()
    {
        return(masterQuestionMulti);
    }

    /**
     * アンケートマスタを取得する
     * 
     * @param dispStatus 表示フラグ(1:PC表示、2:携帯表示)
     * @param ownerFlag オーナー表示フラグ
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterQuestion(int dispStatus, int ownerFlag, int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( dispStatus < 0 || ownerFlag < 0 || id < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_question";
        query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND member_flag < 2";
        query = query + " AND owner_flag = ?";
        query = query + " AND real_flag = 0";
        query = query + " AND id = ?";
        if ( dispStatus == 1 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 2 )";
        }
        else if ( dispStatus == 2 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 3 )";
        }
        query = query + " ORDER BY question_id DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, ownerFlag );
            prestate.setInt( 2, id );
            ret = getMasterQuestionSub( prestate, dispStatus, NOT_REALFLAG );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterQuestion] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * アンケートデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @param dispStatus 表示フラグ(1:PC表示、2:携帯表示)
     * @param realFlag 表示フラグ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMasterQuestionSub(PreparedStatement prestate, int dispStatus, int realFlag)
    {
        ResultSet result = null;
        boolean ret;

        ret = false;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                this.masterQuestion = new DataMasterQuestion();
                if ( result.next() != false )
                {
                    this.masterQuestionCount = 1;
                    // アンケートマスタ情報の設定
                    this.masterQuestion.setData( result );
                    // アンケート質問数を取得
                    this.masterQuestionDataCount = getMasterQuestionDataCount( masterQuestion.getQuestionId(),
                            dispStatus, realFlag );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterQuestionSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( masterQuestionCount != 0 || masterQuestionDataCount != 0 )
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
     * アンケートの質問数をセット
     * 
     * @param questionId 管理番号
     * @param dispStatus 表示フラグ(1:PC表示、2:携帯表示)
     * @oaram realFlag リアルタイムアンケートフラグ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public int getMasterQuestionDataCount(int questionId, int dispStatus, int realFlag)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( dispStatus < 0 || questionId < 0 || realFlag < 0 )
        {
            return(-1);
        }

        query = "SELECT question_no FROM hh_master_question_data";
        // query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate(2) );
        // query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate(2) );
        query = query + " WHERE start_date >= 0";
        query = query + " AND question_id = ?";
        query = query + " AND member_flag < 2";
        query = query + " AND real_flag = ?";
        if ( dispStatus == 1 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 2 )";
        }
        else if ( dispStatus == 2 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 3 )";
        }
        query = query + " ORDER BY question_id DESC, question_no";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            prestate.setInt( 2, realFlag );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // 取得データ数
                if ( result.last() != false )
                {
                    masterQuestionDataCount = result.getRow();
                }
                this.masterQuestionNo = new int[masterQuestionDataCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.masterQuestionNo[count] = result.getInt( "question_no" );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterQuestionDataCount] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(count);
    }

    /**
     * リアルタイムアンケートマスタを取得する
     * 
     * @param dispStatus 表示フラグ(1:PC表示、2:携帯表示)
     * @param ownerFlag オーナー表示フラグ
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @see リアルタイムフラグが1のものを取得する
     */
    public boolean getRealTimeQuestion(int dispStatus, int ownerFlag, int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( dispStatus < 0 || ownerFlag < 0 || id < 0 )
        {
            return(false);
        }
        Logging.info( "[getMasterQuestion] 開始" );
        query = "SELECT * FROM hh_master_question";
        query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND member_flag < 2";
        query = query + " AND owner_flag = ?";
        query = query + " AND real_flag = 1";
        query = query + " AND id = ?";
        if ( dispStatus == 1 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 2 )";
        }
        else if ( dispStatus == 2 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 3 )";
        }
        query = query + " ORDER BY question_id DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, ownerFlag );
            prestate.setInt( 2, id );
            ret = getMasterQuestionSub( prestate, dispStatus, REALFLAG );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestion] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * リアルタイムアンケートマスタを取得する
     * 
     * @param dispStatus 表示フラグ(1:PC表示、2:携帯表示)
     * @param ownerFlag オーナー表示フラグ
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @see リアルタイムフラグが1のものを取得する
     */
    public boolean getRealTimeQuestionBackNum(int dispStatus, int ownerFlag, int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( dispStatus < 0 || ownerFlag < 0 || id < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_question";
        query = query + " WHERE backnum_start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND backnum_end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND member_flag < 2";
        query = query + " AND owner_flag = ?";
        query = query + " AND real_flag = 1";
        query = query + " AND id = ?";
        if ( dispStatus == 1 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 2 )";
        }
        else if ( dispStatus == 2 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 3 )";
        }
        query = query + " ORDER BY question_id DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, ownerFlag );
            prestate.setInt( 2, id );
            ret = getRealTimeQuestionBackNumSub( prestate, dispStatus );
            Logging.info( "[getRealTimeQuestionBackNum] RET=" + ret );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestionBackNum] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * アンケートデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @param dispStatus 表示フラグ(1:PC表示、2:携帯表示)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getRealTimeQuestionBackNumSub(PreparedStatement prestate, int dispStatus)
    {
        int count;
        ResultSet result = null;
        boolean ret;

        ret = false;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    masterQuestionCount = result.getRow();
                }
                this.masterQuestionMulti = new DataMasterQuestion[masterQuestionCount];

                result.beforeFirst();
                count = 0;
                while( result.next() != false )
                {
                    this.masterQuestionMulti[count] = new DataMasterQuestion();

                    // アンケートマスタ情報の設定
                    this.masterQuestionMulti[count].setData( result );

                    // アンケート質問数を取得
                    this.masterQuestionDataCount = getMasterQuestionDataCount( masterQuestionMulti[count].getQuestionId(),
                            dispStatus, REALFLAG );
                    count++;
                }
            }
            Logging.info( "[getRealTimeQuestionBackNumSub] masterQuestionCount=" + masterQuestionCount );
            Logging.info( "[getRealTimeQuestionBackNumSub] masterQuestionDataCount=" + masterQuestionDataCount );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestionBackNumSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( masterQuestionCount != 0 || masterQuestionDataCount != 0 )
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
     * リアルタイムアンケートマスタを取得する
     * 
     * @param questionId 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @see リアルタイムフラグが1のものを取得する
     */
    public boolean getRealTimeQuestionByQid(int questionId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( questionId < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_question";
        query = query + " WHERE question_id = ?";
        query = query + " AND real_flag = 1";
        query = query + " ORDER BY question_id DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            ret = getRealTimeQuestionByQidSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestionByQid] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * アンケートデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getRealTimeQuestionByQidSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        boolean ret;

        ret = false;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                this.masterQuestion = new DataMasterQuestion();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    this.masterQuestionCount = 1;
                    // アンケートマスタ情報の設定
                    this.masterQuestion.setData( result );

                    // アンケート質問数を取得
                    this.masterQuestionDataCount = getMasterQuestionDataCount( masterQuestion.getQuestionId(),
                            masterQuestion.getDispFlag(), REALFLAG );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestionByQidSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( masterQuestionCount != 0 || masterQuestionDataCount != 0 )
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
     * アンケートマスタを取得する(管理番号から)
     * 
     * @param dispStatus 表示フラグ(1:PC表示、2:携帯表示)
     * @param ownerFlag オーナー表示フラグ
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterQuestion(int dispStatus, int questionId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( dispStatus < 0 || questionId < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_question";
        query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND member_flag < 2";
        query = query + " AND question_id = ?";
        query = query + " AND real_flag = 0";
        if ( dispStatus == 1 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 2 )";
        }
        else if ( dispStatus == 2 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 3 )";
        }
        query = query + " ORDER BY question_id DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            ret = getMasterQuestionSub( prestate, dispStatus, NOT_REALFLAG );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterQuestion] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

}
