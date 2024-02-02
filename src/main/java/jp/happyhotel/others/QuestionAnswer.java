/*
 * @(#)QuestionAnswer.java 1.00 2008/04/22 Copyright (C) ALMEX Inc. 2007 �A���P�[�g�񓚏��擾�N���X
 */

package jp.happyhotel.others;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * �A���P�[�g�񓚃f�[�^�擾�N���X�B �A���P�[�g�񓚃f�[�^�̏����擾����@�\��񋟂���
 * 
 * @author S.Tashiro
 * @version 1.00 2008/04/22
 */
public class QuestionAnswer implements Serializable
{

    /**
	 *
	 */
    private static final long          serialVersionUID = -659576481267252802L;
    private int                        masterCount;
    private int                        realTimeAnswerCount;
    private DataQuestionAnswer[]       questionAnswer;
    private DataMasterQuestion[]       masterQuestion;
    private DataMasterQuestionBranch[] masterQuestionBranch;
    private DataMasterQuestionData[]   masterQuestionData;
    private int[]                      collectCount;

    /**
     * �f�[�^�����������܂��B
     */
    public QuestionAnswer()
    {
        masterCount = 0;
        realTimeAnswerCount = 0;
    }

    /** �A���P�[�g�񓚃f�[�^��񌏐��擾 **/
    public int getCount()
    {
        return(masterCount);
    }

    /** �A���P�[�g�񓚃f�[�^��񌏐��擾 **/
    public int getRealTimeAnswerCount()
    {
        return(realTimeAnswerCount);
    }

    /** �A���P�[�g�񓚃f�[�^���擾 **/
    public DataQuestionAnswer[] getQuestionAnswerInfo()
    {
        return(questionAnswer);
    }

    /**
     * �A���P�[�g�f�[�^�擾
     * 
     * @see getRealTimeQuestionAnswer�Ŏ擾
     **/
    public DataMasterQuestion[] getMasterQuestion()
    {
        return(masterQuestion);
    }

    /**
     * �A���P�[�g�I�����f�[�^�擾
     * 
     * @see getRealTimeQuestionAnswer�Ŏ擾
     **/
    public DataMasterQuestionBranch[] getMasterQuestionBranch()
    {
        return(masterQuestionBranch);
    }

    /**
     * �A���P�[�g����f�[�^�擾
     * 
     * @see getRealTimeQuestionAnswer�Ŏ擾
     **/
    public DataMasterQuestionData[] getMasterQuestionData()
    {
        return(masterQuestionData);
    }

    public int[] getCollectCount()
    {
        return(collectCount);
    }

    /**
     * �A���P�[�g�񓚃f�[�^���擾����
     * 
     * @param questionId �Ǘ��ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getQuestionAnswer(int questionId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( questionId < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_question_answer";
        query = query + " WHERE question_id = ?";
        query = query + " ORDER BY question_id , question_no, seq, answer_date DESC, answer_time DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            ret = getQuestionAnswerSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getQuestionAnswer] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �A���P�[�g�񓚏��̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getQuestionAnswerSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;
        int i;

        i = 0;
        count = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    masterCount = result.getRow();
                }
                this.questionAnswer = new DataQuestionAnswer[this.masterCount];

                for( i = 0 ; i < masterCount ; i++ )
                {
                    questionAnswer[i] = new DataQuestionAnswer();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �A���P�[�g�񓚏��̐ݒ�
                    this.questionAnswer[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getQuestionAnswerSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( masterCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * �A���P�[�g�񓚃f�[�^���擾(���[�U�[ID����擾)
     * 
     * @param questionId �Ǘ��ԍ�
     * @param userId ���[�U�[ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getQuestionAnswerByUserId(int questionId, String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( questionId < 0 )
        {
            return(false);
        }
        if ( (userId == null) || (userId.compareTo( "" ) == 0) )
        {
            return(false);
        }

        query = "SELECT * FROM hh_question_answer";
        query = query + " WHERE question_id = ?";
        query = query + " AND user_id = ?";
        query = query + " ORDER BY question_id , question_no, seq, answer_date DESC, answer_time DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            prestate.setString( 2, userId );
            ret = getQuestionAnswerSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getQuestionAnswerByUserId] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �A���P�[�g�񓚃f�[�^���擾(�[���ԍ�����擾)
     * 
     * @param questionId �Ǘ��ԍ�
     * @param termNo �[���ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getQuestionAnswerByTermNo(int questionId, String termNo)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( questionId < 0 )
        {
            return(false);
        }
        if ( termNo == null )
        {
            return(false);
        }

        query = "SELECT * FROM hh_question_answer";
        query = query + " WHERE question_id = ?";
        query = query + " AND termno = ?";
        query = query + " ORDER BY question_id , question_no, seq, answer_date DESC, answer_time DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            prestate.setString( 2, termNo );
            ret = getQuestionAnswerSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getQuestionAnswerByTermNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �A���P�[�g�񓚃f�[�^���擾(�I�[�i�[�z�e��ID�E�I�[�i�[���[�U�[ID����擾)
     * 
     * @param questionId �Ǘ��ԍ�
     * @param ownerHotelId �I�[�i�[�z�e��ID
     * @param ownerUserId �I�[�i�[���[�U�[ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getQuestionAnswerByOwner(int questionId, String ownerHotelId, int ownerUserId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( questionId < 0 || ownerUserId < 0 )
        {
            return(false);
        }
        if ( (ownerHotelId == null) || (ownerHotelId.compareTo( "" ) == 0) )
        {
            return(false);
        }

        query = "SELECT * FROM hh_question_answer";
        query = query + " WHERE question_id = ?";
        query = query + " AND owner_hotelid = ?";
        query = query + " AND owner_userid = ?";
        query = query + " ORDER BY question_id , question_no, seq, answer_date DESC, answer_time DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            prestate.setString( 2, ownerHotelId );
            prestate.setInt( 3, ownerUserId );
            ret = getQuestionAnswerSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getQuestionAnswerByOwner] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �A���P�[�g�񓚃f�[�^���擾(����ID�A����ԍ����炻�ꂼ��̑I�����̉񓚐������)
     * 
     * @param questionId �Ǘ��ԍ�
     * @param questionNo ����ԍ�
     * @param branch �I����
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public int getQuestionAnswerByBranch(int questionId, int questionNo, int branch)
    {
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        int answerCount;

        if ( questionId < 0 || questionNo < 0 )
        {
            return(-1);
        }

        answerCount = 0;
        query = "SELECT count(answer_branch), hh_question_answer.* FROM hh_question_answer";
        query = query + " WHERE question_id = ?";
        query = query + " AND question_no = ?";
        query = query + " AND answer_branch = ?";
        query = query + " GROUP BY ( answer_branch ) ";
        query = query + " ORDER BY question_id , question_no, seq, answer_date DESC, answer_time DESC";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            prestate.setInt( 2, questionNo );
            prestate.setInt( 3, branch );
            answerCount = getQuestionAnswerByBranchSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getQuestionAnswerByBranch] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(answerCount);
    }

    /**
     * �A���P�[�g�񓚏��̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private int getQuestionAnswerByBranchSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;

        count = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    // �A���P�[�g�񓚏��̐ݒ�
                    count = result.getInt( 1 );
                }
            }
            else
            {
                count = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getQuestionAnswerByBranchSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        return(count);
    }

    /**
     * �A���P�[�g�񓚃f�[�^���擾(����ID�A����ԍ����炻�ꂼ��̑I�����̉񓚐������)
     * 
     * @param questionId �Ǘ��ԍ�
     * @param branch �I����
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getRealTimeQuestionAnswer(int questionId)
    {
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        boolean ret;

        if ( questionId < 0 )
        {
            return(false);
        }

        ret = false;
        query = "SELECT COUNT( hh_question_answer.answer_branch ) as total_count,";
        query = query + " hh_master_question_branch.*, hh_master_question_data.*,";
        query = query + " hh_master_question.*, hh_question_answer.*";
        query = query + " FROM hh_master_question, hh_master_question_data,";
        query = query + " hh_master_question_branch, hh_question_answer";
        query = query + " WHERE hh_master_question.question_id = ?";
        query = query + " AND hh_master_question.question_id = hh_master_question_data.question_id";
        query = query + " AND hh_master_question.question_id = hh_master_question_branch.question_id";
        query = query + " AND hh_master_question_data.question_no = hh_master_question_branch.question_no";
        query = query + " AND  hh_master_question.question_id = hh_question_answer.question_id";
        query = query + " AND hh_master_question_data.question_no = hh_question_answer.question_no";
        query = query + " AND hh_master_question_branch.branch = hh_question_answer.answer_branch";
        query = query + " GROUP BY ( hh_question_answer.answer_branch )";
        query = query + " ORDER BY hh_master_question.question_id,";
        query = query + " hh_master_question_data.question_no, hh_master_question_branch.branch";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            ret = getRealTimeQuestionAnswerSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestionAnswer] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �A���P�[�g�񓚏��̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getRealTimeQuestionAnswerSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count = 0;
        boolean ret;

        ret = false;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    masterCount = result.getRow();
                }
                this.masterQuestion = new DataMasterQuestion[masterCount];
                this.masterQuestionData = new DataMasterQuestionData[masterCount];
                this.masterQuestionBranch = new DataMasterQuestionBranch[masterCount];
                this.questionAnswer = new DataQuestionAnswer[masterCount];
                this.collectCount = new int[masterCount];

                realTimeAnswerCount = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.masterQuestion[count] = new DataMasterQuestion();
                    this.masterQuestionData[count] = new DataMasterQuestionData();
                    this.masterQuestionBranch[count] = new DataMasterQuestionBranch();
                    this.questionAnswer[count] = new DataQuestionAnswer();

                    // ���ꂼ����Z�b�g�B
                    this.masterQuestion[count].setData( result );
                    this.masterQuestionData[count].setData( result );
                    this.masterQuestionBranch[count].setData( result );
                    this.questionAnswer[count].setData( result );
                    this.collectCount[count] = result.getInt( "total_count" );
                    realTimeAnswerCount = realTimeAnswerCount + result.getInt( "total_count" );
                    count++;
                }
            }
            else
            {
                ret = false;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestionAnswerSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( count > 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        return(ret);
    }
}
