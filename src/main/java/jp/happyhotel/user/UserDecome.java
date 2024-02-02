/*
 * @(#)UserBasicInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ���[�U��{���擾�N���X
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterDecome;
import jp.happyhotel.data.DataMasterName;
import jp.happyhotel.data.DataMasterPoint;

/**
 * ���[�U��{���擾�N���X�B ���[�U�̊�{�����擾����@�\��񋟂���
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/26
 */
public class UserDecome implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -3924291538252750667L;

    private int                userDecomeCount;
    private int                userDecomeAllCount;
    private DataMasterDecome[] userDecome;
    private DataMasterName[]   masterName;
    private DataMasterPoint[]  masterPoint;

    /**
     * �f�[�^�����������܂��B
     */
    public UserDecome()
    {
        userDecomeCount = 0;
        userDecomeAllCount = 0;
    }

    public int getCount()
    {
        return(userDecomeCount);
    }

    public int getAllCount()
    {
        return(userDecomeAllCount);
    }

    public DataMasterDecome[] getDecomeInfo()
    {
        return(userDecome);
    }

    /** �}�X�^�[�l�[���擾 **/
    public DataMasterName[] getMasterNameInfo()
    {
        return(masterName);
    }

    public DataMasterPoint[] getMasterPoint()
    {
        return(masterPoint);
    }

    /**
     * �f�R�������擾����iID����j
     * 
     * @param category �J�e�S��
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getDecome(int category)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( category < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_decome";
        query = query + " WHERE category = ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, category );

            ret = getDecomeSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.error( "[getUserDecome] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �f�R�����̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getDecomeSub(PreparedStatement prestate)
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
                    userDecomeCount = result.getRow();
                }
                this.userDecome = new DataMasterDecome[this.userDecomeCount];
                for( i = 0 ; i < userDecomeCount ; i++ )
                {
                    userDecome[i] = new DataMasterDecome();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �f�R���f�[�^���̐ݒ�
                    this.userDecome[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getDecomeSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userDecomeCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * �l�[���}�X�^���擾����i�N���X����j
     * 
     * @param class �N���XID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getMasterName(int classCode)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( classCode < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_master_name";
        query = query + " WHERE class = ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, classCode );

            ret = getMasterNameSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.error( "[getUserDecome_getMasterName] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �l�[���}�X�^���̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getMasterNameSub(PreparedStatement prestate)
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
                    userDecomeCount = result.getRow();
                }
                this.masterName = new DataMasterName[this.userDecomeCount];
                for( i = 0 ; i < userDecomeCount ; i++ )
                {
                    masterName[i] = new DataMasterName();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �l�[���}�X�^���̐ݒ�
                    this.masterName[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getMasterNameSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userDecomeCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * �f�R�������擾����iID����,�y�[�W,�\�������w��j
     * 
     * @param classCode �N���X�R�[�h
     * @param category �J�e�S��
     * @param countNum �\������
     * @param pageNum �y�[�W��
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getDecome(int classCode, int category, int countNum, int pageNum)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( category < 0 )
        {
            return(false);
        }

        query = "SELECT hh_master_decome.*, hh_master_point.*";
        query = query + " FROM hh_master_decome, hh_master_point";
        query = query + " WHERE hh_master_decome.point_code = hh_master_point.code";
        query = query + " AND hh_master_decome.class = ?";
        query = query + " AND hh_master_decome.category = ?";
        query = query + " AND hh_master_decome.start_date <= ?";
        query = query + " AND hh_master_decome.end_date >= ?";
        query = query + "  ORDER BY hh_master_decome.start_date DESC, hh_master_decome.disp_pos DESC";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, classCode );
            prestate.setInt( 2, category );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            ret = getDecomeInfoSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[UserDecome.getDecome()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        // �f�R���S����
        query = "SELECT COUNT(*) FROM hh_master_decome, hh_master_point";
        query = query + " WHERE hh_master_decome.point_code = hh_master_point.code";
        query = query + " AND hh_master_decome.class = ?";
        query = query + " AND hh_master_decome.category = ?";
        query = query + " AND hh_master_decome.start_date <= ?";
        query = query + " AND hh_master_decome.end_date >= ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, classCode );
            prestate.setInt( 2, category );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // �������̎擾
                    this.userDecomeAllCount = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserDecome.getDecome()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �f�R�����̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @see �f�R���f�[�^�ƃ|�C���g�f�[�^���擾����
     */
    private boolean getDecomeInfoSub(PreparedStatement prestate)
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
                    userDecomeCount = result.getRow();
                }
                this.userDecome = new DataMasterDecome[this.userDecomeCount];
                this.masterPoint = new DataMasterPoint[this.userDecomeCount];
                for( i = 0 ; i < userDecomeCount ; i++ )
                {
                    this.userDecome[i] = new DataMasterDecome();
                    this.masterPoint[i] = new DataMasterPoint();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �f�R���f�[�^���̐ݒ�
                    this.userDecome[count].setData( result );
                    this.masterPoint[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getDecomeSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userDecomeCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

}
