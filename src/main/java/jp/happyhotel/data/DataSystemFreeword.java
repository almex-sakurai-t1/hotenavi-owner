/*
 * @(#)DataSystemFreeword.java 1.00 2007/09/01 Copyright (C) ALMEX Inc. 2007 �t���[���[�h�����Ǘ��f�[�^�擾�N���X
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * �t���[���[�h�����Ǘ��f�[�^�擾�N���X
 *
 * @author S.Shiiya
 * @version 1.00 2007/09/01
 * @version 1.1 2007/11/20
 */
public class DataSystemFreeword implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 8402996550368342786L;

    private int               date;
    private String            freeword;
    private int               count;

    /**
     * �f�[�^�����������܂��B
     */
    public DataSystemFreeword()
    {
        date = 0;
        freeword = "";
        count = 0;
    }

    public int getDate()
    {
        return date;
    }

    public int getCount()
    {
        return count;
    }

    public String getFreeword()
    {
        return freeword;
    }

    public void setDate(int date)
    {
        this.date = date;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public void setFreeword(String freeword)
    {
        this.freeword = freeword;
    }

    /**
     * �t���[���[�h�����Ǘ��f�[�^�擾(���ݓ��t)
     *
     * @param freeword �t���[���[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(String freeword)
    {
        int nowDate = 0;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_system_freeword";
        query = query + " WHERE date = ?";
        query = query + " AND freeword = ?";

        try
        {
            nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, nowDate );
            prestate.setString( 2, freeword );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {

                    this.freeword = result.getString( "freeword" );
                    this.count = result.getInt( "count" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemFreeword.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * �t���[���[�h�����Ǘ��f�[�^�擾
     *
     * @param freeword �t���[���[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getDataYesterday(String freeword)
    {
        int nowDate = 0;
        int yesterdayDate = 0;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_system_freeword";
        query = query + " WHERE date = ?";
        query = query + " AND freeword = ?";

        try
        {
            nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            yesterdayDate = DateEdit.addDay( nowDate, -1 );
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, yesterdayDate );
            prestate.setString( 2, freeword );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {

                    this.freeword = result.getString( "freeword" );
                    this.count = result.getInt( "count" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemFreeword.getDataYesterday] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * �t���[���[�h�����Ǘ��f�[�^�ݒ�
     *
     * @param result �t���[���[�h�����Ǘ��f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.date = result.getInt( "date" );
                this.freeword = result.getString( "freeword" );
                this.count = result.getInt( "count" );
            }
        }
        catch ( Exception e )
        {
        }

        return(true);
    }

    /**
     * �t���[���[�h�����Ǘ��f�[�^�ǉ�
     *
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean insertData()
    {
        int result;
        int nowDate = 0;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_system_freeword SET ";
        query = query + " date = ?,";
        query = query + " freeword = ?,";
        query = query + " count = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, nowDate );
            prestate.setString( 2, this.freeword );
            prestate.setInt( 3, this.count );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemFreeword.insertData] Exception=" + e.toString() );
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
     * �t���[���[�h�����Ǘ��f�[�^�ύX
     *
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param freeword �t���[���[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateData(String freeword)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        int nowDate = 0;

        ret = false;

        query = "UPDATE hh_system_freeword SET ";
        query = query + " count = count + 1";
        query = query + " WHERE freeword = ?";
        query = query + " AND date = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( 1, this.freeword );
            prestate.setInt( 2, nowDate );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemFreeword.updateData] Exception=" + e.toString() );
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
