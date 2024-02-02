/*
 * @(#)DataMasterRegistFormData.java 1.00 2009/01/13 Copyright (C) ALMEX Inc. 2009 汎用フォーム質問内容取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 汎用フォーム質問マスタ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/05/12
 */
public class DataMasterRegistFormData implements Serializable
{

    private static final long serialVersionUID = 8295268368722209749L;

    private int               formId;
    private int               questionNo;
    private int               kind;
    private String            questionTitle;
    private int               dispFlag;
    private int               memberFlag;
    private int               necessaryFlag;

    /**
     * データを初期化します。
     */
    public DataMasterRegistFormData()
    {
        formId = 0;
        questionNo = 0;
        kind = 0;
        questionTitle = "";
        dispFlag = 0;
        memberFlag = 0;
        necessaryFlag = 0;
    }

    // ゲッター
    public int getDispFlag()
    {
        return dispFlag;
    }

    public int getFormId()
    {
        return formId;
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

    public int getQuestionNo()
    {
        return questionNo;
    }

    public String getQuestionTitle()
    {
        return questionTitle;
    }

    // セッター
    public void setDispFlag(int dispFlag)
    {
        this.dispFlag = dispFlag;
    }

    public void setFormId(int questionId)
    {
        this.formId = questionId;
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

    public void setQuestionNo(int questionNo)
    {
        this.questionNo = questionNo;
    }

    public void setQuestionTitle(String questionTitle)
    {
        this.questionTitle = questionTitle;
    }

    /**
     * 汎用フォーム質問内容データ取得
     * 
     * @param formId 管理番号
     * @param questionNo 質問番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int formId, int questionNo)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_regist_form_data WHERE form_id= ? AND question_no = ?";

        count = 0;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, formId );
            prestate.setInt( 2, questionNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.formId = result.getInt( "form_id" );
                    this.questionNo = result.getInt( "question_no" );
                    this.kind = result.getInt( "kind" );
                    this.questionTitle = result.getString( "question_title" );
                    this.dispFlag = result.getInt( "disp_flag" );
                    this.memberFlag = result.getInt( "member_flag" );
                    this.necessaryFlag = result.getInt( "necessary_flag" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistFormData.getData] Exception=" + e.toString() );
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
     * 汎用フォーム質問内容データ設定
     * 
     * @param result 汎用フォーム質問データレコード
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
                this.kind = result.getInt( "kind" );
                this.questionTitle = result.getString( "question_title" );
                this.dispFlag = result.getInt( "disp_flag" );
                this.memberFlag = result.getInt( "member_flag" );
                this.necessaryFlag = result.getInt( "necessary_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistFormData.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 汎用フォーム質問マスタデータ設定
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

        query = "INSERT hh_master_regist_form_data SET";
        query = query + " form_id = ?,";
        query = query + " question_no = ?,";
        query = query + " kind = ?,";
        query = query + " question_title = ?,";
        query = query + " disp_flag = ?,";
        query = query + " member_flag = ?,";
        query = query + " necessary_flag = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.formId );
            prestate.setInt( 2, this.questionNo );
            prestate.setInt( 3, this.kind );
            prestate.setString( 4, this.questionTitle );
            prestate.setInt( 5, this.dispFlag );
            prestate.setInt( 6, this.memberFlag );
            prestate.setInt( 7, this.necessaryFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistFormData.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 汎用フォーム質問マスタデータ設定
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

        query = "UPDATE hh_master_regist_form_data SET";
        query = query + " kind = ?,";
        query = query + " question_title = ?,";
        query = query + " disp_flag = ?,";
        query = query + " member_flag = ?,";
        query = query + " necessary_flag = ?";
        query = query + " WHERE form_id = ?";
        query = query + " AND question_no = ?";

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
            prestate.setInt( 6, formId );
            prestate.setInt( 7, questionNo );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistFormData.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 汎用フォーム質問マスタデータ削除(一致するFormId全てのデータ)
     * 
     * @param formId フォームID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean deleteData(int formId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "DELETE FROM hh_master_regist_form_data ";
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
            Logging.error( "[DataMasterRegistFormData.deleteData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 汎用フォーム質問マスタデータ削除
     * 
     * @param formId フォームID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean deleteData(int formId, int questionNo)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "DELETE FROM hh_master_regist_form_data ";
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
            Logging.error( "[DataMasterRegistFormData.deleteData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
