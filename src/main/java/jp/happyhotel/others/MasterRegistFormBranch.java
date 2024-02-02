/*
 * @(#)MasterRegistFormBranch.java 1.00 2009/01/13 Copyright (C) ALMEX Inc. 2007 �ėp�t�H�[���I�����擾�N���X
 */

package jp.happyhotel.others;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * �ėp�t�H�[���I�����擾�N���X�B �ėp�t�H�[���I�����̏����擾����@�\��񋟂���
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
     * �f�[�^�����������܂��B
     */
    public MasterRegistFormBranch()
    {
        masterBranchCount = 0;
    }

    /** �ėp�t�H�[���I������񌏐��擾 **/
    public int getRegistFormBranchCount()
    {
        return(masterBranchCount);
    }

    /** �ėp�t�H�[���I�������擾 **/
    public DataMasterRegistFormBranch[] getRegistFormBranchInfo()
    {
        return(masterBranch);
    }

    /**
     * �ėp�t�H�[���I�������擾����
     * 
     * @param formId �Ǘ��ԍ�
     * @param questionNo ����ԍ�
     * @param dispFlag �\���t���O(1:PC�\���A2:�g�ѕ\��)
     * @param memberFlag �����o�[�t���O(true:����̂�)
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * �A���P�[�g�I�����̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
                // �擾�f�[�^��
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
                    // �A���P�[�g�I�������̐ݒ�
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
