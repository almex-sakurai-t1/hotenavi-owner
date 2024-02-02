/*
 * @(#)MasterRegistFormData.java 1.00 2009/01/13 Copyright (C) ALMEX Inc. 2007 汎用フォーム質問取得クラス
 */

package jp.happyhotel.others;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * 汎用フォーム質問取得クラス。 汎用フォーム質問の情報を取得する機能を提供する
 * 
 * @author S.Tashiro
 * @version 1.00 2008/05/14
 */
public class MasterRegistFormData implements Serializable
{

    private static final long          serialVersionUID = -5556439022682318127L;
    private int                        masterDataCount;
    private DataMasterRegistFormData[] masterData;

    /**
     * データを初期化します。
     */
    public MasterRegistFormData()
    {
        masterDataCount = 0;
    }

    /** 汎用フォーム質問情報件数取得 **/
    public int getRegistFormDataCount()
    {
        return(masterDataCount);
    }

    /** 汎用フォーム質問情報取得 **/
    public DataMasterRegistFormData[] getRegistFormDataInfo()
    {
        return(masterData);
    }

    /**
     * 汎用フォーム質問を取得する
     * 
     * @param formId 管理番号
     * @param questionNo 質問番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterRegistFormData(int formId, int questionNo)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( formId < 0 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_master_regist_form_data";
        query = query + " WHERE form_id = ?";
        query = query + " AND question_no = ? ";
        query = query + " ORDER BY form_id DESC, question_no";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, formId );
            prestate.setInt( 2, questionNo );
            ret = getMasterRegistFormDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterRegistFormData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 汎用フォーム質問を取得する
     * 
     * @param formId 管理番号
     * @param questionNo 質問番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterRegistFormData(int formId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( formId < 0 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_master_regist_form_data";
        query = query + " WHERE form_id = ?";
        query = query + " ORDER BY form_id DESC, question_no";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, formId );
            ret = getMasterRegistFormDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterRegistFormData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 汎用フォーム質問を取得する
     * 
     * @param formId 管理番号
     * @param dispFlag 表示フラグ（1：PC用、2:携帯用）
     * @param memberFlag メンバーフラグ(true:会員のみ)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterRegistFormData(int formId, int dispFlag, boolean memberFlag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( formId < 0 || dispFlag <= -1 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_master_regist_form_data";
        query = query + " WHERE form_id = ?";
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
        if ( memberFlag == false )
        {
            query = query + " AND ( member_flag = 0 ";
            query = query + " OR member_flag = 2 )";
        }
        else
        {
            query = query + " AND ( member_flag = 0 ";
            query = query + " OR member_flag = 1 )";
        }
        query = query + " ORDER BY form_id DESC, question_no";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, formId );
            ret = getMasterRegistFormDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterRegistFormData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 汎用フォーム質問のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @param questionId 管理番号
     * @param dispFlag 表示フラグ（1:PC表示用、2:携帯表示用）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMasterRegistFormDataSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;

        count = 0;
        masterDataCount = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.masterDataCount = result.getRow();
                }

                result.beforeFirst();
                this.masterData = new DataMasterRegistFormData[masterDataCount];
                while( result.next() != false )
                {
                    this.masterData[count] = new DataMasterRegistFormData();
                    this.masterData[count].setData( result );
                    count++;
                }

            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterRegistFormDataSub] Exception=" + e.toString() );
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

    /**
     * 汎用フォーム質問を取得する
     * 
     * @param questionId 管理番号
     * @param dispFlag 表示フラグ（1：PC用、2:携帯用）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterRegistFormData(int formId, int questionNo, int dispFlag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( formId < 0 || dispFlag <= -1 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_master_regist_form_data";
        query = query + " WHERE form_id = ?";
        query = query + " AND question_no = ? ";
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
        query = query + " ORDER BY form_id DESC, question_no";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, formId );
            prestate.setInt( 2, questionNo );
            ret = getMasterRegistFormDataSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterRegistFormData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

}
