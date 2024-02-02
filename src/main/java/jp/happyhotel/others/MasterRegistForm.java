/*
 * @(#)MasterQuestionData.java 1.00 2008/05/14 Copyright (C) ALMEX Inc. 2007 アンケート質問取得クラス
 */

package jp.happyhotel.others;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * 汎用フォーム取得クラス。 汎用フォームの情報を取得する機能を提供する
 * 
 * @author S.Tashiro
 * @version 1.00 2008/05/14
 */
public class MasterRegistForm implements Serializable
{
    private static final long      serialVersionUID = -5238501884359949837L;
    private int                    masterDataCount;
    private int                    m_masterDataCount;
    private DataMasterRegistForm   masterRegistForm;
    private DataMasterRegistForm[] m_masterRegistForm;

    /**
     * データを初期化します。
     */
    public MasterRegistForm()
    {
        masterDataCount = 0;
        m_masterDataCount = 0;
    }

    /** 汎用フォーム情報件数取得 **/
    public int getCount()
    {
        return(masterDataCount);
    }

    /** 汎用フォーム情報件数取得 **/
    public int getAllCount()
    {
        return(m_masterDataCount);
    }

    /** 汎用フォーム情報取得 **/
    public DataMasterRegistForm getRegistFormInfo()
    {
        return(masterRegistForm);
    }

    /** 汎用フォーム情報取得 **/
    public DataMasterRegistForm[] getRegistFormInfoMulti()
    {
        return(m_masterRegistForm);
    }

    /**
     * 汎用フォームを取得する
     * 
     * @param formId フォームID
     * @param dispFlag 表示フラグ（1:PC、2:携帯）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterRegistForm(int formId, int dispFlag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( formId < 0 || dispFlag <= -1 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_master_regist_form";
        query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND form_id = ?";
        query = query + " AND member_flag <= 2";
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
        query = query + " ORDER BY form_id DESC";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, formId );
            ret = getMasterRegistFormSub( prestate );
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
     * 汎用フォームのデータをセット
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMasterRegistFormSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.masterDataCount = result.getRow();
                }
                this.masterRegistForm = new DataMasterRegistForm();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    this.masterRegistForm.setData( result );

                }

            }
            else
            {
                this.masterDataCount = 0;
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

        if ( masterDataCount > 0 )
        {
            this.m_masterDataCount = this.masterDataCount;
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * 汎用フォームを取得する
     * 
     * @param formId フォームID
     * @param dispFlag 表示フラグ（1:PC、2:携帯）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterRegistFormList(int countNum, int pageNum)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        int count;

        query = "SELECT * FROM hh_master_regist_form";
        query = query + " ORDER BY form_id";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.masterDataCount = result.getRow();
                }
                this.m_masterRegistForm = new DataMasterRegistForm[this.masterDataCount];

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_masterRegistForm[count] = new DataMasterRegistForm();
                    this.m_masterRegistForm[count].setData( result );
                    count++;
                }
            }
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterRegistFormList] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        query = "SELECT * FROM hh_master_regist_form";
        count = 0;
        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.m_masterDataCount = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getMasterRegistFormList] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
