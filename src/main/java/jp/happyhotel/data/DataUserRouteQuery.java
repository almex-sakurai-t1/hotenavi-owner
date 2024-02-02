/*
 * @(#)DataUserRouteQuery.java
 * 1.00 2011/03/31 Copyright (C) ALMEX Inc. 2011
 * ���[�U���[�g�N�G���[�f�[�^
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ���[�U�[���[�g�N�G���[�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2011/03/31
 */
public class DataUserRouteQuery implements Serializable
{

    private String userId;
    private String queryString;
    private int    registDate;
    private int    registTime;

    /**
     * �f�[�^�����������܂��B
     */
    public DataUserRouteQuery()
    {
        userId = "";
        queryString = "";
        registDate = 0;
        registTime = 0;
    }

    public String getQueryString()
    {
        return queryString;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setQueryString(String queryString)
    {
        this.queryString = queryString;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * ���[�U�[���[�g�N�G���[�f�[�^�擾
     * 
     * @param code �|�C���g�R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(String userId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_route_query WHERE user_id= ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.queryString = result.getString( "query_string" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserRouteQuery.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ���[�U�[���[�g�N�G���[�f�[�^�ݒ�
     * 
     * @param result �f�R���Ǘ��}�X�^�f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.userId = result.getString( "user_id" );
                this.queryString = result.getString( "query_string" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserRouteQuery.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ���[�U�[���[�g�N�G���[�f�[�^�ݒ�
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

        query = "INSERT hh_user_route_query SET";
        query = query + " user_id = ?,";
        query = query + " query_string = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.userId );
            prestate.setString( 2, this.queryString );
            prestate.setInt( 3, this.registDate );
            prestate.setInt( 4, this.registTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserRouteQuery.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ���[�U�[���[�g�N�G���[�f�[�^�ݒ�
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateData(String userId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_user_route_query SET";
        query = query + " query_string = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?";
        query = query + " WHERE user_id = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.queryString );
            prestate.setInt( 2, this.registDate );
            prestate.setInt( 3, this.registTime );
            prestate.setString( 4, this.userId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserRouteQuery.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

}
