/*
 * @(#)MasterQuestionData.java 1.00 2008/05/14 Copyright (C) ALMEX Inc. 2007 アンケート質問取得クラス
 */

package jp.happyhotel.others;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * アンケート質問取得クラス。 アンケート質問の情報を取得する機能を提供する
 * 
 * @author S.Tashiro
 * @version 1.00 2008/05/14
 */
public class MasterQuestionData implements Serializable
{

    private static final long          serialVersionUID = -5556439022682318127L;
    private int                        masterDataCount;
    private int                        masterBranchCount;
    private DataMasterQuestionData     masterData;
    private DataMasterQuestionBranch[] masterBranch;

    /**
     * データを初期化します。
     */
    public MasterQuestionData()
    {
        masterDataCount = 0;
        masterBranchCount = 0;
    }

    /** アンケート質問情報件数取得 **/
    public int getQuestionDataCount()
    {
        return(masterDataCount);
    }

    /** アンケート選択肢情報件数取得 **/
    public int getQuestionBranchCount()
    {
        return(masterBranchCount);
    }

    /** アンケート質問情報取得 **/
    public DataMasterQuestionData getQuestionDataInfo()
    {
        return(masterData);
    }

    /** アンケート選択肢情報取得 **/
    public DataMasterQuestionBranch[] getQuestionBranchInfo()
    {
        return(masterBranch);
    }

    /**
     * アンケート質問を取得する
     * 
     * @param questionId 管理番号
     * @param questionNo 質問番号
     * @param dispFlag 表示フラグ（1：PC用、2:携帯用）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterQuestionData(int questionId, int questionNo, int dispFlag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( questionId < 0 || questionNo < 0 || dispFlag < 0 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_master_question_data";
        query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND question_id = ?";
        query = query + " AND question_no = ?";
        query = query + " AND member_flag < 2";
        query = query + " AND real_flag = 0";
        if ( dispFlag == 1 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 2 )";
        }
        else if ( dispFlag == 2 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 3 )";
        }
        query = query + " ORDER BY question_id DESC, question_no";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            prestate.setInt( 2, questionNo );
            ret = getMasterQuestionDataSub( prestate, questionId, dispFlag );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterQuestionData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * アンケート質問のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @param questionId 管理番号
     * @param dispFlag 表示フラグ（1:PC表示用、2:携帯表示用）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMasterQuestionDataSub(PreparedStatement prestate, int questionId, int dispFlag)
    {
        ResultSet result = null;
        boolean ret;
        int i;
        MasterQuestionBranch mqb;

        i = 0;
        ret = false;
        mqb = new MasterQuestionBranch();
        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                this.masterData = new DataMasterQuestionData();
                if ( result.next() != false )
                {
                    this.masterData.setData( result );
                    ret = mqb.getMasterQuestionBranch( questionId, result.getInt( "question_no" ), dispFlag );

                    if ( ret != false )
                    {
                        if ( mqb.getQuestionBranchCount() > 0 )
                        {
                            masterBranchCount = mqb.getQuestionBranchCount();
                            this.masterBranch = new DataMasterQuestionBranch[masterBranchCount];
                            for( i = 0 ; i < masterBranchCount ; i++ )
                            {
                                this.masterBranch[i] = new DataMasterQuestionBranch();
                                this.masterBranch[i] = mqb.getQuestionBranchInfo()[i];
                            }
                        }
                    }
                    else
                    {
                        masterDataCount = 0;
                        masterBranchCount = 0;
                    }
                }
            }
            else
            {
                masterDataCount = 0;
                masterBranchCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterQuestionDataSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( masterDataCount > 0 || masterBranchCount > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * アンケート質問を取得する
     * 
     * @param questionId 管理番号
     * @param questionNo 質問番号
     * @param dispFlag 表示フラグ（1：PC用、2:携帯用）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRealTimeQuestionData(int questionId, int questionNo, int dispFlag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( questionId < 0 || questionNo < 0 || dispFlag < 0 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_master_question_data";
        query = query + " WHERE question_id = ?";
        query = query + " AND question_no = ?";
        query = query + " AND member_flag < 2";
        query = query + " AND real_flag = 1";
        query = query + " AND backnum_start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND backnum_end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );

        if ( dispFlag == 1 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 2 )";
        }
        else if ( dispFlag == 2 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 3 )";
        }
        query = query + " ORDER BY question_id DESC, question_no";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            prestate.setInt( 2, questionNo );
            ret = getRealTimeQuestionDataSub( prestate, questionId, dispFlag );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestionData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * アンケート質問のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @param questionId 管理番号
     * @param dispFlag 表示フラグ(1:PC表示用、2:携帯表示用)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getRealTimeQuestionDataSub(PreparedStatement prestate, int questionId, int dispFlag)
    {
        ResultSet result = null;
        boolean ret;
        int i;
        MasterQuestionBranch mqb;

        i = 0;
        ret = false;
        mqb = new MasterQuestionBranch();
        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                this.masterData = new DataMasterQuestionData();
                if ( result.next() != false )
                {
                    this.masterData.setData( result );
                    ret = mqb.getMasterQuestionBranch( questionId, result.getInt( "question_no" ), dispFlag );

                    if ( ret != false )
                    {
                        if ( mqb.getQuestionBranchCount() > 0 )
                        {
                            masterBranchCount = mqb.getQuestionBranchCount();
                            this.masterBranch = new DataMasterQuestionBranch[masterBranchCount];
                            for( i = 0 ; i < masterBranchCount ; i++ )
                            {
                                this.masterBranch[i] = new DataMasterQuestionBranch();
                                this.masterBranch[i] = mqb.getQuestionBranchInfo()[i];
                            }
                        }
                    }
                    else
                    {
                        masterDataCount = 0;
                        masterBranchCount = 0;
                    }
                }
            }
            else
            {
                masterDataCount = 0;
                masterBranchCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestionDataSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( masterDataCount > 0 || masterBranchCount > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * アンケート質問を取得する
     * 
     * @param questionId 管理番号
     * @param questionNo 質問番号
     * @param dispFlag 表示フラグ(1:PC表示用、2:携帯表示用)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRealTimeQuestionBackNumData(int questionId, int questionNo, int dispFlag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( questionId < 0 || questionNo < 0 || dispFlag < 0 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_master_question_data";
        query = query + " WHERE question_id = ?";
        query = query + " AND question_no = ?";
        query = query + " AND member_flag < 2";
        query = query + " AND real_flag = 1";
        query = query + " AND backnum_start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND backnum_end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        if ( dispFlag == 1 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 2 )";
        }
        else if ( dispFlag == 2 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 3 )";
        }
        query = query + " ORDER BY question_id DESC, question_no";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            prestate.setInt( 2, questionNo );
            ret = getRealTimeQuestionBackNumDataSub( prestate, questionId, dispFlag );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestionBackNumData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * アンケート質問のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @param questionId 管理番号
     * @param dispFlag 表示フラグ（1:PC表示用、2:携帯表示用）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getRealTimeQuestionBackNumDataSub(PreparedStatement prestate, int questionId, int dispFlag)
    {
        ResultSet result = null;

        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                this.masterData = new DataMasterQuestionData();
                if ( result.next() != false )
                {
                    masterDataCount = 1;
                    this.masterData.setData( result );
                }
                else
                {
                    this.masterDataCount = 0;
                }
            }
            else
            {
                this.masterDataCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestionBackNumDataSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( masterDataCount > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }
}
