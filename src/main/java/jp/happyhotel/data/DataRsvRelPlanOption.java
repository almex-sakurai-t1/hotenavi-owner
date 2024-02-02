/*
 * �v�����E�I�v�V�����ݒ�f�[�^
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/*
 * ������import����N���X��ǉ�
 */

public class DataRsvRelPlanOption implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -2645758846832808971L;

    private int               iD;
    private int               planId;
    private int               optionId;
    private int               optionSubId;

    /**
     * �f�[�^�̏�����
     */
    public DataRsvRelPlanOption()
    {
        iD = 0;
        planId = 0;
        optionId = 0;
        optionSubId = 0;
    }

    // getter
    public int getID()
    {
        return this.iD;
    }

    public int getPlanId()
    {
        return this.planId;
    }

    public int getOptionId()
    {
        return this.optionId;
    }

    public int getOptionSubId()
    {
        return this.optionSubId;
    }

    /**
     *
     * setter
     *
     */
    public void setId(int iD)
    {
        this.iD = iD;
    }

    public void setPlanId(int planId)
    {
        this.planId = planId;
    }

    public void setOptionId(int optionId)
    {
        this.optionId = optionId;
    }

    public void setOptionSubId(int optionSubId)
    {
        this.optionSubId = optionSubId;
    }

    /**
     * �v�����E�I�v�V�����ݒ�f�[�^���擾
     *
     * @param iD �z�e��ID
     * @param planId �v����ID
     * @param optionId �I�v�V����ID
     * @param optionSubId �I�v�V�����T�uID
     * @return ��������(TRUE:����,False:�ُ�)
     */
    public boolean getData(int Id, int planId, int optionId, int optionSubId)
    {
        // �ϐ���`
        boolean ret; // �߂�l
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, plan_id, option_id, option_sub_id " +
                " FROM hh_rsv_rel_plan_option WHERE id = ? AND plan_id = ? " +
                " AND option_id = ? AND option_sub_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, optionId );
            prestate.setInt( 4, optionSubId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.planId = result.getInt( "plan_id" );
                    this.optionId = result.getInt( "option_id" );
                    this.optionSubId = result.getInt( "option_sub_id" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRelPlanOption.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
    /**
     * �v�����E�I�v�V�����ݒ�f�[�^�ǉ�����
     *
     * @return ��������(TRUE:����,False:�ُ�)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT INTO hh_rsv_rel_plan_option SET " +
                "  id = ?" +
                ", plan_id = ?" +
                ", option_id = ?" +
                ", option_sub_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, this.iD );
            prestate.setInt( 2, this.planId );
            prestate.setInt( 3, this.optionId );
            prestate.setInt( 4, this.optionSubId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRelPlanOption.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

}
