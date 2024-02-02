/*
 * @(#)DataUserTermIdTemp.java 1.00 2007/08/12 Copyright (C) ALMEX Inc. 2007 ���[�U�[����񉼗̈�f�[�^�擾�N���X
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * ���[�U�[����񉼗̈�f�[�^�擾�N���X
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/12
 * @version 1.1 2007/11/27
 */
public class DataUserTermIdTemp implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -5271822083421316016L;

    private String            mobileTermNo;
    private int               termnoStatus;
    private int               lastUpdate;
    private int               lastUptime;

    /**
     * �f�[�^�����������܂��B
     */
    public DataUserTermIdTemp()
    {
        mobileTermNo = "";
        termnoStatus = 0;
        lastUpdate = 0;
        lastUptime = 0;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public String getMobileTermNo()
    {
        return mobileTermNo;
    }

    public int getTermnoStatus()
    {
        return termnoStatus;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setMobileTermNo(String mobileTermNo)
    {
        this.mobileTermNo = mobileTermNo;
    }

    public void setTermnoStatus(int termnoStatus)
    {
        this.termnoStatus = termnoStatus;
    }

    /**
     * ���[�U�[����񉼗̈�f�[�^�擾
     * 
     * @param mobileTermNo ���[�U�[���ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(String mobileTermNo)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_idtemp WHERE mobile_termno = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, mobileTermNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.mobileTermNo = result.getString( "mobile_termno" );
                    this.termnoStatus = result.getInt( "termno_status" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserTermIdTemp.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ���[�U�[����񉼗̈�f�[�^�ݒ�
     * 
     * @param result ���[�U��{�f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.mobileTermNo = result.getString( "mobile_termno" );
                this.termnoStatus = result.getInt( "termno_status" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserTermIdTemp.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ���[�U�[����񉼗̈�f�[�^�ǉ�
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_user_idtemp SET ";
        query = query + " mobile_termno = ?,";
        query = query + " termno_status = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( 1, this.mobileTermNo );
            prestate.setInt( 2, this.termnoStatus );
            prestate.setInt( 3, this.lastUpdate );
            prestate.setInt( 4, this.lastUptime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserTermIdTemp.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U�[����񉼗̈�f�[�^�ύX
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param mobileTermno �[���ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateData(String mobileTermno)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_idtemp SET ";
        query = query + " termno_status = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " WHERE mobile_termno = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, this.termnoStatus );
            prestate.setInt( 2, this.lastUpdate );
            prestate.setInt( 3, this.lastUptime );
            prestate.setString( 4, mobileTermno );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserTermIdTemp.updateData] Exception=" + e.toString() );
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
