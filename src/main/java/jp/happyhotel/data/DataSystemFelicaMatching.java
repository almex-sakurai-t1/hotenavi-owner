package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Thu May 31 14:03:48 JST 2012
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �t�F���J�R�t���f�[�^
 * 
 * @author tashiro-s1
 * @version 1.0
 */
public class DataSystemFelicaMatching implements Serializable
{
    private String key1;
    private String key2;
    private String idm;
    private int    registStatus;
    private int    registDate;
    private int    registTime;
    private int    lastUpdate;
    private int    lastUptime;

    /**
     * Constractor
     */
    public DataSystemFelicaMatching()
    {
        key1 = "";
        key2 = "";
        idm = "";
        registDate = 0;
        registTime = 0;
        registStatus = 0;
        lastUpdate = 0;
        lastUptime = 0;
    }

    public String getKey1()
    {
        return this.key1;
    }

    public void setKey1(String key1)
    {
        this.key1 = key1;
    }

    public String getKey2()
    {
        return this.key2;
    }

    public void setKey2(String key2)
    {
        this.key2 = key2;
    }

    public String getIdm()
    {
        return this.idm;
    }

    public void setIdm(String idm)
    {
        this.idm = idm;
    }

    public int getRegistStatus()
    {
        return this.registStatus;
    }

    public void setRegistStatus(int registStatus)
    {
        this.registStatus = registStatus;
    }

    public int getRegistDate()
    {
        return this.registDate;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public int getRegistTime()
    {
        return this.registTime;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public int getLastUpdate()
    {
        return this.lastUpdate;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public int getLastUptime()
    {
        return this.lastUptime;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    /****
     * �t�F���J�R�t���f�[�^�擾
     * 
     * @param key1
     * @param key2
     * @return
     */
    public boolean getData(String key1, String key2)
    {

        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_system_felica_matching WHERE key1 = ? AND key2 = ?";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, key1 );
            prestate.setString( 2, key2 );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.key1 = result.getString( "key1" );
                    this.key2 = result.getString( "key2" );
                    this.idm = result.getString( "idm" );
                    this.registStatus = result.getInt( "regist_status" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemFelicaMatching.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( count > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * �t�F���J�R�t���f�[�^�ݒ�
     * 
     * @param result ���[�U�|�C���g�f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.key1 = result.getString( "key1" );
                this.key2 = result.getString( "key2" );
                this.idm = result.getString( "idm" );
                this.registStatus = result.getInt( "regist_status" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemFelicaMatching.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * �t�F���J�R�t���f�[�^�}��
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

        query = "INSERT hh_system_felica_matching SET ";
        query += " key1 = ?,";
        query += " key2 = ?,";
        query += " idm = ?,";
        query += " regist_status = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?,";
        query += " last_update = ?,";
        query += " last_uptime = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( 1, this.key1 );
            prestate.setString( 2, this.key2 );
            prestate.setString( 3, this.idm );
            prestate.setInt( 4, this.registStatus );
            prestate.setInt( 5, this.registDate );
            prestate.setInt( 6, this.registTime );
            prestate.setInt( 7, this.lastUpdate );
            prestate.setInt( 8, this.lastUptime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserDataIndex.insertData] Exception=" + e.toString() );
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
     * �t�F���J�R�t���f�[�^�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param key1�@�A�N�Z�X�L�[1
     * @param key2�@�A�N�Z�X�L�[2
     * @return
     */
    public boolean updateData(String key1, String key2)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_system_felica_matching SET ";
        query += " idm = ?,";
        query += " regist_status = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?,";
        query += " last_update = ?,";
        query += " last_uptime = ?";
        query = query + " WHERE key1 = ? AND key2 = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( 1, this.idm );
            prestate.setInt( 2, this.registStatus );
            prestate.setInt( 3, this.registDate );
            prestate.setInt( 4, this.registTime );
            prestate.setInt( 5, this.lastUpdate );
            prestate.setInt( 6, this.lastUptime );
            prestate.setString( 7, key1 );
            prestate.setString( 8, key2 );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemFelicaMatching.updateData] Exception=" + e.toString() );
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
