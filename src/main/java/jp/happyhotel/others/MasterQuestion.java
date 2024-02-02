/*
 * @(#)MasterQuestion.java 1.00 2008/05/14 Copyright (C) ALMEX Inc. 2007 �A���P�[�g�}�X�^�擾�N���X
 */

package jp.happyhotel.others;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * �A���P�[�g�}�X�^�擾�N���X�B �A���P�[�g�}�X�^�̏����擾����@�\��񋟂���
 * 
 * @author S.Tashiro
 * @version 1.00 2008/05/14
 */
public class MasterQuestion implements Serializable
{

    /**
     *
     */
    private static final long    serialVersionUID = -659576481267252802L;
    private final int            NOT_REALFLAG     = 0;
    private final int            REALFLAG         = 1;

    private int                  masterQuestionCount;
    private int                  masterQuestionDataCount;
    private DataMasterQuestion   masterQuestion;
    private DataMasterQuestion[] masterQuestionMulti;
    private int[]                masterQuestionNo;

    /**
     * �f�[�^�����������܂��B
     */
    public MasterQuestion()
    {
        masterQuestionCount = 0;
        masterQuestionDataCount = 0;
    }

    /** �A���P�[�g�}�X�^��񌏐��擾 **/
    public int getQuestionCount()
    {
        return(masterQuestionCount);
    }

    /** �A���P�[�g����}�X�^��񌏐��擾 **/
    public int getQuestionDataCount()
    {
        return(masterQuestionDataCount);
    }

    /** �A���P�[�g�ԍ��擾 **/
    public int[] getQuestionNo()
    {
        return(masterQuestionNo);
    }

    /** �A���P�[�g�}�X�^���擾 **/
    public DataMasterQuestion getQuestionInfo()
    {
        return(masterQuestion);
    }

    /** �A���P�[�g�}�X�^���擾 **/
    public DataMasterQuestion[] getQuestionInfoMulti()
    {
        return(masterQuestionMulti);
    }

    /**
     * �A���P�[�g�}�X�^���擾����
     * 
     * @param dispStatus �\���t���O(1:PC�\���A2:�g�ѕ\��)
     * @param ownerFlag �I�[�i�[�\���t���O
     * @param id �z�e��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getMasterQuestion(int dispStatus, int ownerFlag, int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( dispStatus < 0 || ownerFlag < 0 || id < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_question";
        query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND member_flag < 2";
        query = query + " AND owner_flag = ?";
        query = query + " AND real_flag = 0";
        query = query + " AND id = ?";
        if ( dispStatus == 1 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 2 )";
        }
        else if ( dispStatus == 2 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 3 )";
        }
        query = query + " ORDER BY question_id DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, ownerFlag );
            prestate.setInt( 2, id );
            ret = getMasterQuestionSub( prestate, dispStatus, NOT_REALFLAG );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterQuestion] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �A���P�[�g�f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @param dispStatus �\���t���O(1:PC�\���A2:�g�ѕ\��)
     * @param realFlag �\���t���O
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getMasterQuestionSub(PreparedStatement prestate, int dispStatus, int realFlag)
    {
        ResultSet result = null;
        boolean ret;

        ret = false;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                this.masterQuestion = new DataMasterQuestion();
                if ( result.next() != false )
                {
                    this.masterQuestionCount = 1;
                    // �A���P�[�g�}�X�^���̐ݒ�
                    this.masterQuestion.setData( result );
                    // �A���P�[�g���␔���擾
                    this.masterQuestionDataCount = getMasterQuestionDataCount( masterQuestion.getQuestionId(),
                            dispStatus, realFlag );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterQuestionSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( masterQuestionCount != 0 || masterQuestionDataCount != 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        return(ret);
    }

    /**
     * �A���P�[�g�̎��␔���Z�b�g
     * 
     * @param questionId �Ǘ��ԍ�
     * @param dispStatus �\���t���O(1:PC�\���A2:�g�ѕ\��)
     * @oaram realFlag ���A���^�C���A���P�[�g�t���O
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public int getMasterQuestionDataCount(int questionId, int dispStatus, int realFlag)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( dispStatus < 0 || questionId < 0 || realFlag < 0 )
        {
            return(-1);
        }

        query = "SELECT question_no FROM hh_master_question_data";
        // query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate(2) );
        // query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate(2) );
        query = query + " WHERE start_date >= 0";
        query = query + " AND question_id = ?";
        query = query + " AND member_flag < 2";
        query = query + " AND real_flag = ?";
        if ( dispStatus == 1 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 2 )";
        }
        else if ( dispStatus == 2 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 3 )";
        }
        query = query + " ORDER BY question_id DESC, question_no";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            prestate.setInt( 2, realFlag );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // �擾�f�[�^��
                if ( result.last() != false )
                {
                    masterQuestionDataCount = result.getRow();
                }
                this.masterQuestionNo = new int[masterQuestionDataCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.masterQuestionNo[count] = result.getInt( "question_no" );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterQuestionDataCount] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(count);
    }

    /**
     * ���A���^�C���A���P�[�g�}�X�^���擾����
     * 
     * @param dispStatus �\���t���O(1:PC�\���A2:�g�ѕ\��)
     * @param ownerFlag �I�[�i�[�\���t���O
     * @param id �z�e��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @see ���A���^�C���t���O��1�̂��̂��擾����
     */
    public boolean getRealTimeQuestion(int dispStatus, int ownerFlag, int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( dispStatus < 0 || ownerFlag < 0 || id < 0 )
        {
            return(false);
        }
        Logging.info( "[getMasterQuestion] �J�n" );
        query = "SELECT * FROM hh_master_question";
        query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND member_flag < 2";
        query = query + " AND owner_flag = ?";
        query = query + " AND real_flag = 1";
        query = query + " AND id = ?";
        if ( dispStatus == 1 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 2 )";
        }
        else if ( dispStatus == 2 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 3 )";
        }
        query = query + " ORDER BY question_id DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, ownerFlag );
            prestate.setInt( 2, id );
            ret = getMasterQuestionSub( prestate, dispStatus, REALFLAG );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestion] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ���A���^�C���A���P�[�g�}�X�^���擾����
     * 
     * @param dispStatus �\���t���O(1:PC�\���A2:�g�ѕ\��)
     * @param ownerFlag �I�[�i�[�\���t���O
     * @param id �z�e��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @see ���A���^�C���t���O��1�̂��̂��擾����
     */
    public boolean getRealTimeQuestionBackNum(int dispStatus, int ownerFlag, int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( dispStatus < 0 || ownerFlag < 0 || id < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_question";
        query = query + " WHERE backnum_start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND backnum_end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND member_flag < 2";
        query = query + " AND owner_flag = ?";
        query = query + " AND real_flag = 1";
        query = query + " AND id = ?";
        if ( dispStatus == 1 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 2 )";
        }
        else if ( dispStatus == 2 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 3 )";
        }
        query = query + " ORDER BY question_id DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, ownerFlag );
            prestate.setInt( 2, id );
            ret = getRealTimeQuestionBackNumSub( prestate, dispStatus );
            Logging.info( "[getRealTimeQuestionBackNum] RET=" + ret );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestionBackNum] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �A���P�[�g�f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @param dispStatus �\���t���O(1:PC�\���A2:�g�ѕ\��)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getRealTimeQuestionBackNumSub(PreparedStatement prestate, int dispStatus)
    {
        int count;
        ResultSet result = null;
        boolean ret;

        ret = false;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    masterQuestionCount = result.getRow();
                }
                this.masterQuestionMulti = new DataMasterQuestion[masterQuestionCount];

                result.beforeFirst();
                count = 0;
                while( result.next() != false )
                {
                    this.masterQuestionMulti[count] = new DataMasterQuestion();

                    // �A���P�[�g�}�X�^���̐ݒ�
                    this.masterQuestionMulti[count].setData( result );

                    // �A���P�[�g���␔���擾
                    this.masterQuestionDataCount = getMasterQuestionDataCount( masterQuestionMulti[count].getQuestionId(),
                            dispStatus, REALFLAG );
                    count++;
                }
            }
            Logging.info( "[getRealTimeQuestionBackNumSub] masterQuestionCount=" + masterQuestionCount );
            Logging.info( "[getRealTimeQuestionBackNumSub] masterQuestionDataCount=" + masterQuestionDataCount );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestionBackNumSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( masterQuestionCount != 0 || masterQuestionDataCount != 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        return(ret);
    }

    /**
     * ���A���^�C���A���P�[�g�}�X�^���擾����
     * 
     * @param questionId �Ǘ��ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @see ���A���^�C���t���O��1�̂��̂��擾����
     */
    public boolean getRealTimeQuestionByQid(int questionId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( questionId < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_question";
        query = query + " WHERE question_id = ?";
        query = query + " AND real_flag = 1";
        query = query + " ORDER BY question_id DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            ret = getRealTimeQuestionByQidSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestionByQid] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �A���P�[�g�f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getRealTimeQuestionByQidSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        boolean ret;

        ret = false;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                this.masterQuestion = new DataMasterQuestion();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    this.masterQuestionCount = 1;
                    // �A���P�[�g�}�X�^���̐ݒ�
                    this.masterQuestion.setData( result );

                    // �A���P�[�g���␔���擾
                    this.masterQuestionDataCount = getMasterQuestionDataCount( masterQuestion.getQuestionId(),
                            masterQuestion.getDispFlag(), REALFLAG );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getRealTimeQuestionByQidSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( masterQuestionCount != 0 || masterQuestionDataCount != 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        return(ret);
    }

    /**
     * �A���P�[�g�}�X�^���擾����(�Ǘ��ԍ�����)
     * 
     * @param dispStatus �\���t���O(1:PC�\���A2:�g�ѕ\��)
     * @param ownerFlag �I�[�i�[�\���t���O
     * @param id �z�e��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getMasterQuestion(int dispStatus, int questionId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( dispStatus < 0 || questionId < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_question";
        query = query + " WHERE start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND member_flag < 2";
        query = query + " AND question_id = ?";
        query = query + " AND real_flag = 0";
        if ( dispStatus == 1 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 2 )";
        }
        else if ( dispStatus == 2 )
        {
            query = query + " AND ( disp_flag = 1";
            query = query + " OR disp_flag = 3 )";
        }
        query = query + " ORDER BY question_id DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, questionId );
            ret = getMasterQuestionSub( prestate, dispStatus, NOT_REALFLAG );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMasterQuestion] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

}
