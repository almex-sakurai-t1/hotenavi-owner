/*
 * @(#)DataMasterRegistFormBranch.java 1.00 2009/01/13 Copyright (C) ALMEX Inc. 2009 汎用フォーム選択マスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 汎用フォーム選択肢マスタ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/05/12
 */
public class DataMasterRegistFormBranch implements Serializable
{

    private static final long serialVersionUID = -4729096443061508354L;

    private int               formId;
    private int               questionNo;
    private int               branch;
    private String            branchTitle;
    private int               dispFlag;
    private int               memberFlag;
    private int               textFlag;
    private String            textTitle;
    private int               textLimit;
    private int               textWidth;
    private int               numericFlag;

    /**
     * データを初期化します。
     */
    public DataMasterRegistFormBranch()
    {
        formId = 0;
        questionNo = 0;
        branch = 0;
        branchTitle = "";
        dispFlag = 0;
        memberFlag = 0;
        textFlag = 0;
        textTitle = "";
        textLimit = 0;
        textWidth = 0;
        numericFlag = 0;
    }

    // ゲッター
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

    public int getMemberFlag()
    {
        return memberFlag;
    }

    public int getNumericFlag()
    {
        return numericFlag;
    }

    public int getFormId()
    {
        return formId;
    }

    public int getQuetstionNo()
    {
        return questionNo;
    }

    public int getTextFlag()
    {
        return textFlag;
    }

    public int getTextLimit()
    {
        return textLimit;
    }

    public String getTextTitle()
    {
        return textTitle;
    }

    public int getTextWidth()
    {
        return textWidth;
    }

    // セッター
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

    public void setMemberFlag(int memberFlag)
    {
        this.memberFlag = memberFlag;
    }

    public void setNumericFlag(int numericFlag)
    {
        this.numericFlag = numericFlag;
    }

    public void setFormId(int questionId)
    {
        this.formId = questionId;
    }

    public void setQuestionNo(int questionNo)
    {
        this.questionNo = questionNo;
    }

    public void setTextFlag(int textFlag)
    {
        this.textFlag = textFlag;
    }

    public void setTextLimit(int textLimit)
    {
        this.textLimit = textLimit;
    }

    public void setTextTitle(String textTitle)
    {
        this.textTitle = textTitle;
    }

    public void setTextWidth(int textWidth)
    {
        this.textWidth = textWidth;
    }

    /**
     * 汎用フォーム質問選択肢マスタデータ取得
     * 
     * @param formId 管理番号
     * @param questionNo 質問番号
     * @param branch 回答連番
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int formId, int questionNo, int branch)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_regist_form_branch WHERE form_id= ? AND question_no = ?";
        query = query + " AND branch = ?";

        count = 0;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, formId );
            prestate.setInt( 2, questionNo );
            prestate.setInt( 3, branch );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.formId = result.getInt( "form_id" );
                    this.questionNo = result.getInt( "question_no" );
                    this.branch = result.getInt( "branch" );
                    this.branchTitle = result.getString( "branch_title" );
                    this.dispFlag = result.getInt( "disp_flag" );
                    this.memberFlag = result.getInt( "member_flag" );
                    this.textFlag = result.getInt( "text_flag" );
                    this.textTitle = result.getString( "text_title" );
                    this.textLimit = result.getInt( "text_limit" );
                    this.textWidth = result.getInt( "text_width" );
                    this.numericFlag = result.getInt( "numeric_flag" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistFormBranch.getData] Exception=" + e.toString() );
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
     * 汎用フォーム選択肢マスタデータ設定
     * 
     * @param result 汎用フォーム選択肢マスタデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.formId = result.getInt( "form_id" );
                this.questionNo = result.getInt( "question_no" );
                this.branch = result.getInt( "branch" );
                this.branchTitle = result.getString( "branch_title" );
                this.dispFlag = result.getInt( "disp_flag" );
                this.memberFlag = result.getInt( "member_flag" );
                this.textFlag = result.getInt( "text_flag" );
                this.textTitle = result.getString( "text_title" );
                this.textLimit = result.getInt( "text_limit" );
                this.textWidth = result.getInt( "text_width" );
                this.numericFlag = result.getInt( "numeric_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistFormBranch.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 汎用フォーム選択肢マスタデータ設定
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

        query = "INSERT hh_master_regist_form_branch SET";
        query = query + " form_id = ?,";
        query = query + " question_no = ?,";
        query = query + " branch = ?,";
        query = query + " branch_title = ?,";
        query = query + " disp_flag = ?,";
        query = query + " member_flag = ?,";
        query = query + " text_flag = ?,";
        query = query + " text_title = ?,";
        query = query + " text_limit = ?,";
        query = query + " text_width = ?,";
        query = query + " numeric_flag = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.formId );
            prestate.setInt( 2, this.questionNo );
            prestate.setInt( 3, this.branch );
            prestate.setString( 4, this.branchTitle );
            prestate.setInt( 5, this.dispFlag );
            prestate.setInt( 6, this.memberFlag );
            prestate.setInt( 7, this.textFlag );
            prestate.setString( 8, this.textTitle );
            prestate.setInt( 9, this.textLimit );
            prestate.setInt( 10, this.textWidth );
            prestate.setInt( 11, this.numericFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistFormBranch.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 汎用フォーム選択肢マスタデータ設定
     * 
     * @param formId 管理番号
     * @param questionNo 質問番号
     * @param branch 回答番号
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int formId, int questionNo, int branch)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_master_regist_form_branch SET";
        query = query + " branch_title = ?,";
        query = query + " disp_flag = ?,";
        query = query + " member_flag = ?,";
        query = query + " text_flag = ?,";
        query = query + " text_title = ?,";
        query = query + " text_limit = ?,";
        query = query + " text_width = ?,";
        query = query + " numeric_flag = ?";

        query = query + " WHERE form_id = ?";
        query = query + " AND question_no = ?";
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
            prestate.setInt( 6, this.textLimit );
            prestate.setInt( 7, this.textWidth );
            prestate.setInt( 8, this.numericFlag );
            prestate.setInt( 9, formId );
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
            Logging.error( "[DataMasterRegistFormBranch.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 汎用フォーム選択肢マスタデータ削除
     * 
     * @param formId 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean deleteData(int formId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "DELETE FROM hh_master_regist_form_branch ";
        query = query + " WHERE form_id = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, formId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistFormBranch.deleteData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 汎用フォーム選択肢マスタデータ削除
     * 
     * @param formId 管理番号
     * @param questionoNo 質問番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean deleteData(int formId, int questionNo)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "DELETE FROM hh_master_regist_form_branch ";
        query = query + " WHERE form_id = ?";
        query = query + " AND question_no = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, formId );
            prestate.setInt( 2, questionNo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistFormBranch.deleteData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 汎用フォーム選択肢マスタデータ削除
     * 
     * @param formId 管理番号
     * @param questionoNo 質問番号
     * @param branch 選択肢
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean deleteData(int formId, int questionNo, int branch)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "DELETE FROM hh_master_regist_form_branch ";
        query = query + " WHERE form_id = ?";
        query = query + " AND question_no = ?";
        query = query + " AND branch = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, formId );
            prestate.setInt( 2, questionNo );
            prestate.setInt( 3, branch );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistFormBranch.deleteData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
