/* @(#)FormAnswer.java  1.00 2008/04/22
 *
 * Copyright (C) ALMEX Inc. 2007
 *
 * �ėp�t�H�[���񓚏��擾�N���X
 */

package jp.happyhotel.others;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * �ėp�t�H�[���񓚃f�[�^�擾�N���X�B
 *   �ėp�t�H�[���񓚃f�[�^�̏����擾����@�\��񋟂���
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
     * �f�[�^�����������܂��B
     */
    public FormAnswer( ) {
        masterCount = 0;
    }

    /** �ėp�t�H�[���񓚃f�[�^��񌏐��擾 **/
    public int getCount( ) {
        return( masterCount );
    }

    /** �ėp�t�H�[���񓚃f�[�^���擾 **/
    public DataQuestionAnswer[] getQuestionAnswerInfo( ) {
        return( questionAnswer );
    }

    /** �ėp�t�H�[���f�[�^�擾
     * @see getRealTimeQuestionAnswer�Ŏ擾
     **/
    public DataMasterQuestion[] getMasterQuestion() {
        return( masterQuestion );
    }
    /** �ėp�t�H�[���I�����f�[�^�擾
     * @see getRealTimeQuestionAnswer�Ŏ擾
     **/
    public DataMasterQuestionBranch[] getMasterQuestionBranch() {
        return( masterQuestionBranch );
    }
    /** �ėp�t�H�[������f�[�^�擾
     * @see getRealTimeQuestionAnswer�Ŏ擾
     **/
    public DataMasterQuestionData[] getMasterQuestionData() {
        return( masterQuestionData );
    }
    public int[] getCollectCount() {
        return( collectCount );
    }

    /**
     * �ėp�t�H�[���񓚃f�[�^���擾����
     *
     * @param formId �Ǘ��ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * �ėp�t�H�[���񓚏��̃f�[�^���Z�b�g
     *
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
                    // �ėp�t�H�[���񓚏��̐ݒ�
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
     * �ėp�t�H�[���񓚃f�[�^���擾(���[�U�[ID����擾)
     *
     * @param formId �Ǘ��ԍ�
     * @param userId ���[�U�[ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * �ėp�t�H�[���񓚃f�[�^���擾(�[���ԍ�����擾)
     *
     * @param formId �Ǘ��ԍ�
     * @param termNo �[���ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * �ėp�t�H�[���񓚃f�[�^���擾(�I�[�i�[�z�e��ID�E�I�[�i�[���[�U�[ID����擾)
     *
     * @param formId �Ǘ��ԍ�
     * @param ownerHotelId �I�[�i�[�z�e��ID
     * @param ownerUserId �I�[�i�[���[�U�[ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * �ėp�t�H�[���񓚃f�[�^���擾(����ID�A����ԍ����炻�ꂼ��̑I�����̉񓚐������)
     *
     * @param formId �Ǘ��ԍ�
     * @param questionNo ����ԍ�
     * @param branch �I����
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * �ėp�t�H�[���񓚏��̃f�[�^���Z�b�g
     *
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private int getQuestionAnswerByBranchSub( PreparedStatement prestate ) {
        ResultSet    result = null;
        int          count;

        count = 0;
        try {
            result = prestate.executeQuery();
            if ( result != null ) {
                while ( result.next() != false ) {
                    // �ėp�t�H�[���񓚏��̐ݒ�
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
