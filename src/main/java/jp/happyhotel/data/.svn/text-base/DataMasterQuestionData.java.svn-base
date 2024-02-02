/*
 * @(#)DataMasterQuestion.java 1.00 2008/05/12 Copyright (C) ALMEX Inc. 2007 アンケート質問マスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * アンケート質問マスタ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/05/12
 */
public class DataMasterQuestionData implements Serializable
{

    private static final long serialVersionUID = -4729096443061508354L;

    private int               questionId;
    private int               questionNo;
    private int               kind;
    private String            questionTitle;
    private int               dispFlag;
    private int               memberFlag;
    private int               necessaryFlag;
    private int               startDate;
    private int               endDate;
    private int               realFlag;
    private int               backnumStartDate;
    private int               backnumEndDate;
    private String            cssText;
    private int               graphStyle;
    private int               graphDispKind;

    /**
     * データを初期化します。
     */
    public DataMasterQuestionData()
    {
        questionId = 0;
        questionNo = 0;
        kind = 0;
        questionTitle = "";
        dispFlag = 0;
        memberFlag = 0;
        necessaryFlag = 0;
        startDate = 0;
        endDate = 0;
        realFlag = 0;
        backnumStartDate = 0;
        backnumEndDate = 0;
        cssText = "";
        graphStyle = 0;
        graphDispKind = 0;
    }

    // ゲッター
    public int getBackNumEndDate()
    {
        return backnumEndDate;
    }

    public int getBackNumStartDate()
    {
        return backnumStartDate;
    }

    public String getCssText()
    {
        return cssText;
    }

    public int getDispFlag()
    {
        return dispFlag;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public int getGraphDispKind()
    {
        return graphDispKind;
    }

    public int getGraphStyle()
    {
        return graphStyle;
    }

    public int getKind()
    {
        return kind;
    }

    public int getMemberFlag()
    {
        return memberFlag;
    }

    public int getNecessaryFlag()
    {
        return necessaryFlag;
    }

    public int getQuestionId()
    {
        return questionId;
    }

    public int getQuetstionNo()
    {
        return questionNo;
    }

    public String getQuestionTitle()
    {
        return questionTitle;
    }

    public int getRealFlag()
    {
        return realFlag;
    }

    public int getStartDate()
    {
        return startDate;
    }

    // セッター
    public void setBackNumEndDate(int backnumEndDate)
    {
        this.backnumEndDate = backnumEndDate;
    }

    public void setBackNumStartDate(int backnumStartDate)
    {
        this.backnumStartDate = backnumStartDate;
    }

    public void setCssText(String cssText)
    {
        this.cssText = cssText;
    }

    public void setDispFlag(int dispFlag)
    {
        this.dispFlag = dispFlag;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setGraphDispKind(int graphDispKind)
    {
        this.graphDispKind = graphDispKind;
    }

    public void setGraphStyle(int graphStyle)
    {
        this.graphStyle = graphStyle;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
    }

    public void setMemberFlag(int memberFlag)
    {
        this.memberFlag = memberFlag;
    }

    public void setNecessaryFlag(int necessaryFlag)
    {
        this.necessaryFlag = necessaryFlag;
    }

    public void setQuestionId(int questionId)
    {
        this.questionId = questionId;
    }

    public void setQuetstionNo(int questionNo)
    {
        this.questionNo = questionNo;
    }

    public void setQuestionTitle(String questionTitle)
    {
        this.questionTitle = questionTitle;
    }

    public void setRealFlag(int realFlag)
    {
        this.realFlag = realFlag;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    /**
     * アンケート質問マスタデータ取得
     * 
     * @param questionId 管理番号
     * @param questionNo 質問番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int questionId, int questionNo)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_question_data WHERE question_id= ? AND question_no = ?";

        count = 0;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            prestate.setInt( 2, questionNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.questionId = result.getInt( "question_id" );
                    this.questionNo = result.getInt( "question_no" );
                    this.kind = result.getInt( "kind" );
                    this.questionTitle = result.getString( "question_title" );
                    this.dispFlag = result.getInt( "disp_flag" );
                    this.memberFlag = result.getInt( "member_flag" );
                    this.necessaryFlag = result.getInt( "necessary_flag" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.realFlag = result.getInt( "real_flag" );
                    this.backnumStartDate = result.getInt( "backnum_start_date" );
                    this.backnumEndDate = result.getInt( "backnum_end_date" );
                    this.cssText = result.getString( "css_text" );
                    this.graphStyle = result.getInt( "graph_style" );
                    this.graphDispKind = result.getInt( "graph_disp_kind" );
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
     * アンケート質問マスタデータ設定
     * 
     * @param result アンケート質問マスタデータレコード
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
                this.kind = result.getInt( "kind" );
                this.questionTitle = result.getString( "question_title" );
                this.dispFlag = result.getInt( "disp_flag" );
                this.memberFlag = result.getInt( "member_flag" );
                this.necessaryFlag = result.getInt( "necessary_flag" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.realFlag = result.getInt( "real_flag" );
                this.backnumStartDate = result.getInt( "backnum_start_date" );
                this.backnumEndDate = result.getInt( "backnum_end_date" );
                this.cssText = result.getString( "css_text" );
                this.graphStyle = result.getInt( "graph_style" );
                this.graphDispKind = result.getInt( "graph_disp_kind" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterPresent.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * アンケート質問マスタデータ設定
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

        query = "INSERT hh_master_question_data SET";
        query = query + " question_id = ?,";
        query = query + " question_no = ?,";
        query = query + " kind = ?,";
        query = query + " question_title = ?,";
        query = query + " disp_flag = ?,";
        query = query + " member_flag = ?,";
        query = query + " necessary_flag = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " real_flag = ?,";
        query = query + " backnum_start_date = ?,";
        query = query + " backnum_end_date = ?,";
        query = query + " css_text = ?,";
        query = query + " graph_style = ?,";
        query = query + " graph_disp_kind = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.questionId );
            prestate.setInt( 2, this.questionNo );
            prestate.setInt( 3, this.kind );
            prestate.setString( 4, this.questionTitle );
            prestate.setInt( 5, this.dispFlag );
            prestate.setInt( 6, this.memberFlag );
            prestate.setInt( 7, this.necessaryFlag );
            prestate.setInt( 8, this.startDate );
            prestate.setInt( 9, this.endDate );
            prestate.setInt( 10, this.realFlag );
            prestate.setInt( 11, this.backnumStartDate );
            prestate.setInt( 12, this.backnumEndDate );
            prestate.setString( 13, this.cssText );
            prestate.setInt( 14, this.graphStyle );
            prestate.setInt( 15, this.graphDispKind );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterPresent.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * アンケート質問マスタデータ設定
     * 
     * @param questionId 管理番号
     * @param questionNo 質問番号
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int questionId, int questionNo)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_master_question_data SET";
        query = query + " kind = ?,";
        query = query + " question_title = ?,";
        query = query + " disp_flag = ?,";
        query = query + " member_flag = ?,";
        query = query + " necessary_flag = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " real_flag = ?,";
        query = query + " backnum_start_date = ?,";
        query = query + " backnum_end_date = ?,";
        query = query + " css_text = ?,";
        query = query + " graph_style = ?,";
        query = query + " graph_disp_kind = ?";
        query = query + " WHERE quetion_id = ?";
        query = query + " AND quetion_no = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.kind );
            prestate.setString( 2, this.questionTitle );
            prestate.setInt( 3, this.dispFlag );
            prestate.setInt( 4, this.memberFlag );
            prestate.setInt( 5, this.necessaryFlag );
            prestate.setInt( 6, this.startDate );
            prestate.setInt( 7, this.endDate );
            prestate.setInt( 8, this.realFlag );
            prestate.setInt( 9, this.backnumStartDate );
            prestate.setInt( 10, this.backnumEndDate );
            prestate.setString( 11, this.cssText );
            prestate.setInt( 12, this.graphStyle );
            prestate.setInt( 13, this.graphDispKind );
            prestate.setInt( 14, questionId );
            prestate.setInt( 15, questionNo );
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
