/*
 * @(#)MasterQuestionBranch.java 1.00 2008/05/14 Copyright (C) ALMEX Inc. 2007 アンケート選択肢取得クラス
 */

package jp.happyhotel.others;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * アンケート選択肢取得クラス。 アンケート選択肢の情報を取得する機能を提供する
 * 
 * @author S.Tashiro
 * @version 1.00 2008/05/14
 */
public class MasterQuestionBranch implements Serializable
{

    private static final long          serialVersionUID = -5556439022682318127L;

    private int                        masterBranchCount;
    private DataMasterQuestionBranch[] masterBranch;

    /**
     * データを初期化します。
     */
    public MasterQuestionBranch()
    {
        masterBranchCount = 0;
    }

    /** アンケート選択肢情報件数取得 **/
    public int getQuestionBranchCount()
    {
        return(masterBranchCount);
    }

    /** アンケート選択肢情報取得 **/
    public DataMasterQuestionBranch[] getQuestionBranchInfo()
    {
        return(masterBranch);
    }

    /**
     * アンケート選択肢を取得する
     * 
     * @param questionId 管理番号
     * @param questionNo 質問番号
     * @param dispFlag 表示フラグ(1:PC表示、2:携帯表示)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterQuestionBranch(int questionId, int questionNo, int dispFlag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( questionId < 0 || questionNo < 0 || dispFlag < 0 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_master_question_branch";
        query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND question_id = ?";
        query = query + " AND question_no = ?";
        query = query + " AND member_flag < 2";
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
        query = query + " ORDER BY question_id DESC, question_no, branch";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            prestate.setInt( 2, questionNo );
            ret = getMasterQuestionBranchSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterQuestionBranch] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * アンケート選択肢のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMasterQuestionBranchSub(PreparedStatement prestate)
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
                // 取得データ数
                if ( result.last() != false )
                {
                    masterBranchCount = result.getRow();
                }
                this.masterBranch = new DataMasterQuestionBranch[this.masterBranchCount];

                for( i = 0 ; i < masterBranchCount ; i++ )
                {
                    masterBranch[i] = new DataMasterQuestionBranch();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // アンケート選択肢情報の設定
                    this.masterBranch[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterQuestionBranchSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        Logging.info( "[getMasterQuestionBranchSub] masterBranchCount=" + masterBranchCount );

        if ( masterBranchCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * アンケート選択肢を取得する
     * 
     * @param questionId 管理番号
     * @param questionNo 質問番号
     * @param dispFlag 表示フラグ(1:PC表示、2:携帯表示)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getRealTimeQuestionBranch(int questionId, int questionNo, int dispFlag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( questionId < 0 || questionNo < 0 || dispFlag < 0 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_master_question_branch";
        query = query + " WHERE question_id = ?";
        query = query + " AND question_no = ?";
        query = query + " AND member_flag < 2";
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
        query = query + " ORDER BY question_id DESC, question_no, branch";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            prestate.setInt( 2, questionNo );
            ret = getRealTimeQuestionBranchSub( prestate );
            Logging.info( "[getMasterQuestionBranchSub] ret=" + ret );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterQuestionBranch] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * アンケート選択肢のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getRealTimeQuestionBranchSub(PreparedStatement prestate)
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
                // 取得データ数
                if ( result.last() != false )
                {
                    masterBranchCount = result.getRow();
                }
                this.masterBranch = new DataMasterQuestionBranch[this.masterBranchCount];

                for( i = 0 ; i < masterBranchCount ; i++ )
                {
                    masterBranch[i] = new DataMasterQuestionBranch();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // アンケート選択肢情報の設定
                    this.masterBranch[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterQuestionBranchSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        Logging.info( "[getMasterQuestionBranchSub] masterBranchCount=" + masterBranchCount );

        if ( masterBranchCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }
}
