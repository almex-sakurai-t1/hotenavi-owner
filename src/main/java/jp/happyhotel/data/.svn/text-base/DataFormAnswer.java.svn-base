/*
 * @(#)DataFormAnswer.java 1.00 2009/01/13 Copyright (C) ALMEX Inc. 2009 アンケート回答マスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * 汎用フォーム回答マスタ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/01/13
 */
public class DataFormAnswer implements Serializable
{

    private static final long serialVersionUID = -5893225828505876288L;

    private int               formId;
    private int               questionNo;
    private int               seq;
    private int               answerBranch;
    private int               answerBranchSub;
    private String            answerText;
    private int               answerDate;
    private int               answerTime;
    private String            termNo;
    private String            userId;
    private String            userStatus;
    private String            ownerHotelId;
    private int               ownerUserId;
    private String            answerIp;
    private String            answerUserAgent;

    /**
     * データを初期化します。
     */
    public DataFormAnswer()
    {
        formId = 0;
        questionNo = 0;
        seq = 0;
        answerBranch = 0;
        answerBranchSub = 0;
        answerText = "";
        answerDate = 0;
        answerTime = 0;
        termNo = "";
        userId = "";
        userStatus = "";
        ownerHotelId = "";
        ownerUserId = 0;
        answerIp = "";
        answerUserAgent = "";
    }

    // ゲッター
    public int getAnswerBranch()
    {
        return answerBranch;
    }

    public int getAnswerBranchSub()
    {
        return answerBranchSub;
    }

    public int getAnswerDate()
    {
        return answerDate;
    }

    public String getAnswerIp()
    {
        return answerIp;
    }

    public String getAnswerText()
    {
        return answerText;
    }

    public int getAnswerTime()
    {
        return answerTime;
    }

    public String getAnswerUserAgent()
    {
        return answerUserAgent;
    }

    public String getOwnerHotelId()
    {
        return ownerHotelId;
    }

    public int getOwnerUserId()
    {
        return ownerUserId;
    }

    public int getFormId()
    {
        return formId;
    }

    public int getQuetstionNo()
    {
        return questionNo;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getTermNo()
    {
        return termNo;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getUserStatus()
    {
        return userStatus;
    }

    // セッター
    public void setAnswerBranch(int answerBranch)
    {
        this.answerBranch = answerBranch;
    }

    public void setAnswerBranchSub(int answerBranchSub)
    {
        this.answerBranchSub = answerBranchSub;
    }

    public void setAnswerDate(int answerDate)
    {
        this.answerDate = answerDate;
    }

    public void setAnswerIp(String answerIp)
    {
        this.answerIp = answerIp;
    }

    public void setAnswerText(String answerText)
    {
        this.answerText = answerText;
    }

    public void setAnswerTime(int answerTime)
    {
        this.answerTime = answerTime;
    }

    public void setAnswerUserAgent(String answerUserAgent)
    {
        this.answerUserAgent = answerUserAgent;
    }

    public void setOwnerHotelId(String ownerHotelId)
    {
        this.ownerHotelId = ownerHotelId;
    }

    public void setOwnerUserId(int ownerUserId)
    {
        this.ownerUserId = ownerUserId;
    }

    public void setFormId(int formId)
    {
        this.formId = formId;
    }

    public void setQuestionNo(int questionNo)
    {
        this.questionNo = questionNo;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setTermNo(String termNo)
    {
        this.termNo = termNo;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setUserStatus(String userStatus)
    {
        this.userStatus = userStatus;
    }

    /**
     * アンケート回答マスタデータ取得
     * 
     * @param formId 管理番号
     * @param questionNo 質問番号
     * @param seq 回答連番
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int formId, int questionNo, int seq)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_form_answer WHERE form_id= ? AND question_no = ?";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, formId );
            prestate.setInt( 1, questionNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.formId = result.getInt( "form_id" );
                    this.questionNo = result.getInt( "question_no" );
                    this.seq = result.getInt( "seq" );
                    this.answerBranch = result.getInt( "answer_branch" );
                    this.answerBranchSub = result.getInt( "answer_branch_sub" );
                    this.answerText = result.getString( "answer_text" );
                    this.answerDate = result.getInt( "answer_date" );
                    this.answerTime = result.getInt( "answer_time" );
                    this.termNo = result.getString( "termno" );
                    this.userId = result.getString( "user_id" );
                    this.userStatus = result.getString( "user_status" );
                    this.ownerHotelId = result.getString( "owner_hotelid" );
                    this.ownerUserId = result.getInt( "owner_userid" );
                    this.answerIp = result.getString( "answer_ip" );
                    this.answerUserAgent = result.getString( "answer_useragent" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            Logging.error( "[DataQuestionAnswer.getData] count=" + count );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataQuestionAnswer.getData] Exception=" + e.toString() );
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
     * アンケート回答マスタデータ設定
     * 
     * @param result 賞品管理マスタデータレコード
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
                this.seq = result.getInt( "seq" );
                this.answerBranch = result.getInt( "answer_branch" );
                this.answerBranchSub = result.getInt( "answer_branch_sub" );
                this.answerText = result.getString( "answer_text" );
                this.answerDate = result.getInt( "answer_date" );
                this.answerTime = result.getInt( "answer_time" );
                this.termNo = result.getString( "termno" );
                this.userId = result.getString( "user_id" );
                this.userStatus = result.getString( "user_status" );
                this.ownerHotelId = result.getString( "owner_hotelid" );
                this.ownerUserId = result.getInt( "owner_userid" );
                this.answerIp = result.getString( "answer_ip" );
                this.answerUserAgent = result.getString( "answer_useragent" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataQuestionAnswer.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * アンケート回答マスタデータ設定
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

        query = "INSERT hh_form_answer SET";
        query = query + " form_id = ?,";
        query = query + " question_no = ?,";
        query = query + " seq = ?,";
        query = query + " answer_branch = ?,";
        query = query + " answer_branch_sub = ?,";
        query = query + " answer_text = ?,";
        query = query + " answer_date = ?,";
        query = query + " answer_time = ?,";
        query = query + " termno = ?,";
        query = query + " user_id = ?,";
        query = query + " user_status = ?,";
        query = query + " owner_hotelid = ?,";
        query = query + " owner_userid = ?,";
        query = query + " answer_ip = ?,";
        query = query + " answer_useragent = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.formId );
            prestate.setInt( 2, this.questionNo );
            prestate.setInt( 3, this.seq );
            prestate.setInt( 4, this.answerBranch );
            prestate.setInt( 5, this.answerBranchSub );
            prestate.setString( 6, this.answerText );
            prestate.setInt( 7, this.answerDate );
            prestate.setInt( 8, this.answerTime );
            prestate.setString( 9, this.termNo );
            prestate.setString( 10, this.userId );
            prestate.setString( 11, this.userStatus );
            prestate.setString( 12, this.ownerHotelId );
            prestate.setInt( 13, this.ownerUserId );
            prestate.setString( 14, this.answerIp );
            prestate.setString( 15, this.answerUserAgent );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataQuestionAnswer.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * アンケート回答マスタデータ設定
     * 
     * @param formId 管理番号
     * @param questionNo 質問番号
     * @param seq 回答連番
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int formId, int questionNo, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_form_answer SET";
        query = query + " answer_branch = ?,";
        query = query + " answer_branch_sub = ?,";
        query = query + " answer_text = ?,";
        query = query + " answer_date = ?,";
        query = query + " answer_time = ?,";
        query = query + " termno = ?,";
        query = query + " user_id = ?,";
        query = query + " user_status = ?,";
        query = query + " owner_hotelid = ?,";
        query = query + " owner_userid = ?,";
        query = query + " answer_ip = ?,";
        query = query + " answer_useragent = ?";
        query = query + " WHERE form_id = ?";
        query = query + " AND quetion_No = ?";
        query = query + " AND seq = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.answerBranch );
            prestate.setInt( 2, this.answerBranchSub );
            prestate.setString( 3, this.answerText );
            prestate.setInt( 4, this.answerDate );
            prestate.setInt( 5, this.answerTime );
            prestate.setString( 6, this.termNo );
            prestate.setString( 7, this.userId );
            prestate.setString( 8, this.userStatus );
            prestate.setString( 9, this.ownerHotelId );
            prestate.setInt( 10, this.ownerUserId );
            prestate.setString( 11, this.answerIp );
            prestate.setString( 12, this.answerUserAgent );
            prestate.setInt( 13, formId );
            prestate.setInt( 14, questionNo );
            prestate.setInt( 15, seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataQuestionAnswer.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
