/* @(#)FormAnswer.java  1.00 2008/04/22
 *
 * Copyright (C) ALMEX Inc. 2007
 *
 * 汎用フォーム回答情報取得クラス
 */

package jp.happyhotel.others;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * 汎用フォーム回答データ取得クラス。
 *   汎用フォーム回答データの情報を取得する機能を提供する
 * @author  S.Tashiro
 * @version 1.00 2008/04/22
 */
public class FormAnswer implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -659576481267252802L;
    private int                         masterCount;
    private DataQuestionAnswer[]        questionAnswer;
    private DataMasterQuestion[]        masterQuestion;
    private DataMasterQuestionBranch[]  masterQuestionBranch;
    private DataMasterQuestionData[]    masterQuestionData;
    private int[]                       collectCount;

    /**
     * データを初期化します。
     */
    public FormAnswer( ) {
        masterCount = 0;
    }

    /** 汎用フォーム回答データ情報件数取得 **/
    public int getCount( ) {
        return( masterCount );
    }

    /** 汎用フォーム回答データ情報取得 **/
    public DataQuestionAnswer[] getQuestionAnswerInfo( ) {
        return( questionAnswer );
    }

    /** 汎用フォームデータ取得
     * @see getRealTimeQuestionAnswerで取得
     **/
    public DataMasterQuestion[] getMasterQuestion() {
        return( masterQuestion );
    }
    /** 汎用フォーム選択肢データ取得
     * @see getRealTimeQuestionAnswerで取得
     **/
    public DataMasterQuestionBranch[] getMasterQuestionBranch() {
        return( masterQuestionBranch );
    }
    /** 汎用フォーム質問データ取得
     * @see getRealTimeQuestionAnswerで取得
     **/
    public DataMasterQuestionData[] getMasterQuestionData() {
        return( masterQuestionData );
    }
    public int[] getCollectCount() {
        return( collectCount );
    }

    /**
     * 汎用フォーム回答データを取得する
     *
     * @param formId 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getQuestionAnswer( int formId ) {
        boolean             ret;
        String               query;
        Connection           connection = null;
        PreparedStatement    prestate = null;

        if ( formId < 0 ) {
            return( false );
        }
        query = "SELECT * FROM hh_form_answer";
        query = query + " WHERE form_id = ?";
        query = query + " ORDER BY form_id , question_no, seq, answer_date DESC, answer_time DESC";

        ret = false;

        try {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setInt( 1, formId );
            ret = getQuestionAnswerSub(prestate );
        } catch ( Exception e ) {
            Logging.info( "[getQuestionAnswer] Exception=" + e.toString() );
        } finally {
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(connection);
        }

        return( ret );
    }


    /**
     * 汎用フォーム回答情報のデータをセット
     *
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getQuestionAnswerSub( PreparedStatement prestate ) {
        ResultSet    result = null;
        int          count;
        int          i;

        i     = 0;
        count = 0;
        try {
            result = prestate.executeQuery();
            if ( result != null ) {
                if ( result.last() != false ) {
                    masterCount = result.getRow();
                }
                this.questionAnswer = new DataQuestionAnswer[ this.masterCount ];

                for ( i = 0; i < masterCount; i++ ) {
                    questionAnswer[ i ] = new DataQuestionAnswer();
                }

                result.beforeFirst();
                while ( result.next() != false ) {
                    // 汎用フォーム回答情報の設定
                    this.questionAnswer[ count ].setData( result );
                    count++;
                }
            }
        } catch ( Exception e ) {
            Logging.info( "[getQuestionAnswerSub] Exception=" + e.toString() );
        } finally {
            DBConnection.releaseResources(result);
        }

        if ( masterCount != 0 ) {
            return( true );
        } else {
            return( false );
        }
    }

    /**
     * 汎用フォーム回答データを取得(ユーザーIDから取得)
     *
     * @param formId 管理番号
     * @param userId ユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getQuestionAnswerByUserId( int formId, String userId ) {
        boolean             ret;
        String               query;
        Connection           connection = null;
        PreparedStatement    prestate = null;

        if ( formId < 0 ) {
            return( false );
        }
        if ( ( userId == null ) || ( userId.compareTo("") == 0 ) ) {
            return( false );
        }

        query = "SELECT * FROM hh_question_answer";
        query = query + " WHERE form_id = ?";
        query = query + " AND user_id = ?";
        query = query + " ORDER BY form_id , question_no, seq, answer_date DESC, answer_time DESC";

        ret = false;

        try {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setInt( 1, formId );
            prestate.setString( 2, userId );
            ret = getQuestionAnswerSub(prestate );
        } catch ( Exception e ) {
            Logging.info( "[getQuestionAnswerByUserId] Exception=" + e.toString() );
        } finally {
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(connection);
        }

        return( ret );
    }


    /**
     * 汎用フォーム回答データを取得(端末番号から取得)
     *
     * @param formId 管理番号
     * @param termNo 端末番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getQuestionAnswerByTermNo( int formId, String termNo ) {
        boolean             ret;
        String               query;
        Connection           connection = null;
        PreparedStatement    prestate = null;

        if ( formId < 0 ) {
            return( false );
        }
        if ( termNo == null ) {
            return( false );
        }

        query = "SELECT * FROM hh_question_answer";
        query = query + " WHERE form_id = ?";
        query = query + " AND termno = ?";
        query = query + " ORDER BY form_id , question_no, seq, answer_date DESC, answer_time DESC";

        ret = false;

        try {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setInt( 1, formId );
            prestate.setString( 2, termNo );
            ret = getQuestionAnswerSub(prestate );
        } catch ( Exception e ) {
            Logging.info( "[getQuestionAnswerByTermNo] Exception=" + e.toString() );
        } finally {
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(connection);
        }

        return( ret );
    }

    /**
     * 汎用フォーム回答データを取得(オーナーホテルID・オーナーユーザーIDから取得)
     *
     * @param formId 管理番号
     * @param ownerHotelId オーナーホテルID
     * @param ownerUserId オーナーユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getQuestionAnswerByOwner( int formId, String ownerHotelId, int ownerUserId ) {
        boolean             ret;
        String               query;
        Connection           connection = null;
        PreparedStatement    prestate = null;

        if ( formId < 0 || ownerUserId < 0 ) {
            return( false );
        }
        if ( ( ownerHotelId == null ) || ( ownerHotelId.compareTo("") == 0 ) ) {
            return( false );
        }

        query = "SELECT * FROM hh_question_answer";
        query = query + " WHERE form_id = ?";
        query = query + " AND owner_hotelid = ?";
        query = query + " AND owner_userid = ?";
        query = query + " ORDER BY formn_id , question_no, seq, answer_date DESC, answer_time DESC";

        ret = false;

        try {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setInt( 1, formId );
            prestate.setString( 2, ownerHotelId );
            prestate.setInt( 3, ownerUserId );
            ret = getQuestionAnswerSub(prestate );
        } catch ( Exception e ) {
            Logging.info( "[getQuestionAnswerByOwner] Exception=" + e.toString() );
        } finally {
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(connection);
        }

        return( ret );
    }


    /**
     * 汎用フォーム回答データを取得(質問ID、質問番号からそれぞれの選択肢の回答数を取る)
     *
     * @param formId 管理番号
     * @param questionNo 質問番号
     * @param branch 選択肢
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public int getQuestionAnswerByBranch( int formId, int questionNo, int branch  ) {
        String               query;
        Connection           connection = null;
        PreparedStatement    prestate = null;
        int                  answerCount;

        if ( formId < 0 || questionNo < 0 ) {
            return( -1 );
        }

        answerCount = 0;
        query = "SELECT count(answer_branch), hh_question_answer.* FROM hh_question_answer";
        query = query + " WHERE form_id = ?";
        query = query + " AND question_no = ?";
        query = query + " AND answer_branch = ?";
        query = query + " GROUP BY ( answer_branch ) ";
        query = query + " ORDER BY question_id , question_no, seq, answer_date DESC, answer_time DESC";

        try {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setInt( 1, formId );
            prestate.setInt( 2, questionNo );
            prestate.setInt( 3, branch );
            answerCount = getQuestionAnswerByBranchSub(prestate );
        } catch ( Exception e ) {
            Logging.info( "[getQuestionAnswerByBranch] Exception=" + e.toString() );
        } finally {
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(connection);
        }

        return( answerCount );
    }

    /**
     * 汎用フォーム回答情報のデータをセット
     *
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private int getQuestionAnswerByBranchSub( PreparedStatement prestate ) {
        ResultSet    result = null;
        int          count;

        count = 0;
        try {
            result = prestate.executeQuery();
            if ( result != null ) {
                while ( result.next() != false ) {
                    // 汎用フォーム回答情報の設定
                    count = result.getInt( 1 );
                }
            } else {
                count = 0;
            }
        } catch ( Exception e ) {
            Logging.info( "[getQuestionAnswerByBranchSub] Exception=" + e.toString() );
        } finally {
            DBConnection.releaseResources(result);
        }
        return( count );
    }
}
