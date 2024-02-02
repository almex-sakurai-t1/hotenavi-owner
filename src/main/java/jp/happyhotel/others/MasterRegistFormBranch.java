/*
 * @(#)MasterRegistFormBranch.java 1.00 2009/01/13 Copyright (C) ALMEX Inc. 2007 汎用フォーム選択肢取得クラス
 */

package jp.happyhotel.others;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * 汎用フォーム選択肢取得クラス。 汎用フォーム選択肢の情報を取得する機能を提供する
 * 
 * @author S.Tashiro
 * @version 1.00 2009/01/13
 */
public class MasterRegistFormBranch implements Serializable
{

    private static final long            serialVersionUID = 6830840862241264353L;

    private int                          masterBranchCount;
    private DataMasterRegistFormBranch[] masterBranch;

    /**
     * データを初期化します。
     */
    public MasterRegistFormBranch()
    {
        masterBranchCount = 0;
    }

    /** 汎用フォーム選択肢情報件数取得 **/
    public int getRegistFormBranchCount()
    {
        return(masterBranchCount);
    }

    /** 汎用フォーム選択肢情報取得 **/
    public DataMasterRegistFormBranch[] getRegistFormBranchInfo()
    {
        return(masterBranch);
    }

    /**
     * 汎用フォーム選択肢を取得する
     * 
     * @param formId 管理番号
     * @param questionNo 質問番号
     * @param dispFlag 表示フラグ(1:PC表示、2:携帯表示)
     * @param memberFlag メンバーフラグ(true:会員のみ)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterRegistFormBranch(int formId, int questionNo, int dispFlag, boolean memberFlag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( formId < 0 || questionNo < 0 || dispFlag <= -1 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_master_regist_form_branch";
        query = query + " WHERE form_id = ?";
        query = query + " AND question_no = ?";
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
            query = query + " AND member_flag = 0 ";
        }
        else
        {
            query = query + " AND ( member_flag = 0 ";
            query = query + " OR member_flag = 1 ) ";
        }
        query = query + " ORDER BY form_id DESC, question_no, branch";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, formId );
            prestate.setInt( 2, questionNo );
            ret = getMasterRegistFormBranchSub( prestate );
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
    private boolean getMasterRegistFormBranchSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;
        int i;

        i = 0;
        count = 0;
        try
        {
            masterBranchCount = 0;
            result = prestate.executeQuery();
            if ( result != null )
            {
                // 取得データ数
                if ( result.last() != false )
                {
                    this.masterBranchCount = result.getRow();
                }
                this.masterBranch = new DataMasterRegistFormBranch[this.masterBranchCount];

                for( i = 0 ; i < masterBranchCount ; i++ )
                {
                    masterBranch[i] = new DataMasterRegistFormBranch();
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
