/*
 * @(#)DataMasterQuestion.java 1.00 2008/05/12 Copyright (C) ALMEX Inc. 2007 アンケート質問選択マスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * アンケート質問選択肢マスタ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/05/12
 */
public class DataMasterQuestionBranch implements Serializable
{

    private static final long serialVersionUID = -4729096443061508354L;

    private int               questionId;
    private int               questionNo;
    private int               branch;
    private String            branchTitle;
    private int               dispFlag;
    private int               memberFlag;
    private int               textFlag;
    private String            textTitle;
    private int               startDate;
    private int               endDate;
    private String            barColor;

    /**
     * データを初期化します。
     */
    public DataMasterQuestionBranch()
    {
        questionId = 0;
        questionNo = 0;
        branch = 0;
        branchTitle = "";
        dispFlag = 0;
        memberFlag = 0;
        textFlag = 0;
        textTitle = "";
        startDate = 0;
        endDate = 0;
        barColor = "";
    }

    // ゲッター
    public String getBarColor()
    {
        return barColor;
    }

    public int getBranch()
    {
        return branch;
    }

    public String getBranchTitle()
    {
        return branchTitle;
    }

    public int getDispFlag()
    {
        return dispFlag;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public int getMemberFlag()
    {
        return memberFlag;
    }

    public int getQuestionId()
    {
        return questionId;
    }

    public int getQuetstionNo()
    {
        return questionNo;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getTextFlag()
    {
        return textFlag;
    }

    public String getTextTitle()
    {
        return textTitle;
    }

    // セッター
    public void setBarColor(String barColor)
    {
        this.barColor = barColor;
    }

    public void setBranch(int branch)
    {
        this.branch = branch;
    }

    public void setBranchTitle(String branchTitle)
    {
        this.branchTitle = branchTitle;
    }

    public void setDispFlag(int dispFlag)
    {
        this.dispFlag = dispFlag;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setMemberFlag(int memberFlag)
    {
        this.memberFlag = memberFlag;
    }

    public void setQuestionId(int questionId)
    {
        this.questionId = questionId;
    }

    public void setQuetstionNo(int questionNo)
    {
        this.questionNo = questionNo;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setTextFlag(int textFlag)
    {
        this.textFlag = textFlag;
    }

    public void setTextTitle(String textTitle)
    {
        this.textTitle = textTitle;
    }

    /**
     * アンケート質問選択肢マスタデータ取得
     * 
     * @param questionId 管理番号
     * @param questionNo 質問番号
     * @param branch 回答連番
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int questionId, int questionNo, int branch)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_question_branch WHERE question_id= ? AND question_no = ?";
        query = query + " AND branch = ?";

        count = 0;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            prestate.setInt( 2, questionNo );
            prestate.setInt( 3, branch );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.questionId = result.getInt( "question_id" );
                    this.questionNo = result.getInt( "question_no" );
                    this.branch = result.getInt( "branch" );
                    this.branchTitle = result.getString( "branch_title" );
                    this.dispFlag = result.getInt( "disp_flag" );
                    this.memberFlag = result.getInt( "member_flag" );
                    this.textFlag = result.getInt( "text_flag" );
                    this.textTitle = result.getString( "text_title" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.barColor = result.getString( "bar_color" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            Logging.error( "[DataMasterPresent.getData] count=" + count );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterPresent.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( count > 0 )
            return(true);
        else
            return(false);
    }

    /**
     * アンケート質問選択肢マスタデータ設定
     * 
     * @param result アンケート質問選択肢マスタデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.questionId = result.getInt( "question_id" );
                this.questionNo = result.getInt( "question_no" );
                this.branch = result.getInt( "branch" );
                this.branchTitle = result.getString( "branch_title" );
                this.dispFlag = result.getInt( "disp_flag" );
                this.memberFlag = result.getInt( "member_flag" );
                this.textFlag = result.getInt( "text_flag" );
                this.textTitle = result.getString( "text_title" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.barColor = result.getString( "bar_color" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterPresent.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * アンケート質問選択肢マスタデータ設定
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "INSERT hh_master_question_branch SET";
        query = query + " question_id = ?,";
        query = query + " question_no = ?,";
        query = query + " branch = ?,";
        query = query + " branch_title = ?,";
        query = query + " disp_flag = ?,";
        query = query + " member_flag = ?,";
        query = query + " text_flag = ?,";
        query = query + " text_title = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " bar_color = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.questionId );
            prestate.setInt( 2, this.questionNo );
            prestate.setInt( 3, this.branch );
            prestate.setString( 4, this.branchTitle );
            prestate.setInt( 5, this.dispFlag );
            prestate.setInt( 6, this.textFlag );
            prestate.setString( 7, this.textTitle );
            prestate.setInt( 8, this.startDate );
            prestate.setInt( 9, this.startDate );
            prestate.setInt( 10, this.endDate );
            prestate.setString( 11, this.barColor );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterDecome.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * アンケート質問選択肢マスタデータ設定
     * 
     * @param questionId 管理番号
     * @param questionNo 質問番号
     * @param branch 回答番号
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int questionId, int questionNo, int branch)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_master_question_branch SET";
        query = query + " branch_title = ?,";
        query = query + " disp_flag = ?,";
        query = query + " member_flag = ?,";
        query = query + " text_flag = ?,";
        query = query + " text_title = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " bar_color = ?";
        query = query + " WHERE quetion_id = ?";
        query = query + " AND quetion_No = ?";
        query = query + " AND branch = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.branchTitle );
            prestate.setInt( 2, this.dispFlag );
            prestate.setInt( 3, this.memberFlag );
            prestate.setInt( 4, this.textFlag );
            prestate.setString( 5, this.textTitle );
            prestate.setInt( 6, this.startDate );
            prestate.setInt( 7, this.endDate );
            prestate.setString( 8, this.barColor );
            prestate.setInt( 9, questionId );
            prestate.setInt( 10, questionNo );
            prestate.setInt( 11, branch );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterPresent.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
