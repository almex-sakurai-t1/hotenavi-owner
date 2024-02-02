package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �f�[�^�|�C���g�����N���X
 * 
 * @author S.Shiiya
 * @version 1.00 2009/11/04
 */
public class DataUserPointHistory implements Serializable
{

    private static final long serialVersionUID = 5643283370067117137L;
    private String            userId;
    private int               manageYear;
    private int               point;
    private int               plusPoint;
    private int               minusPoint;
    private int               lostPoint;
    private int               lastUpdate;
    private int               lastUptime;

    /**
     * �f�[�^�����������܂��B
     */
    public DataUserPointHistory()
    {
        userId = "";
        manageYear = 0;
        point = 0;
        plusPoint = 0;
        minusPoint = 0;
        lostPoint = 0;
        lastUpdate = 0;
        lastUptime = 0;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getManageYear()
    {
        return manageYear;
    }

    public int getPoint()
    {
        return point;
    }

    public int getPlusPoint()
    {
        return plusPoint;
    }

    public int getMinusPoint()
    {
        return minusPoint;
    }

    public int getLostPoint()
    {
        return lostPoint;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setManageYear(int manageYear)
    {
        this.manageYear = manageYear;
    }

    public void setPoint(int point)
    {
        this.point = point;
    }

    public void setPlusPoint(int plusPoint)
    {
        this.plusPoint = plusPoint;
    }

    public void setMinusPoint(int minusPoint)
    {
        this.minusPoint = minusPoint;
    }

    public void setLostPoint(int lostPoint)
    {
        this.lostPoint = lostPoint;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    /**
     * ���[�U�|�C���g�����f�[�^�擾
     * 
     * @param userId ���[�UID
     * @param manageYear �Ǘ��N�x
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(String userId, int manageYear)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_point_history WHERE user_id = ? AND manage_year = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, manageYear );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.manageYear = result.getInt( "manage_year" );
                    this.point = result.getInt( "point" );
                    this.plusPoint = result.getInt( "plus_point" );
                    this.minusPoint = result.getInt( "minus_point" );
                    this.lostPoint = result.getInt( "lost_point" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointHistory.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ���[�U�|�C���g�����f�[�^�ݒ�
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
                this.userId = result.getString( "user_id" );
                this.manageYear = result.getInt( "manage_year" );
                this.point = result.getInt( "point" );
                this.plusPoint = result.getInt( "plus_point" );
                this.minusPoint = result.getInt( "minus_point" );
                this.lostPoint = result.getInt( "lost_point" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointHistory.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ���[�U�|�C���g�����f�[�^�}��
     * 
     * @see set���Ă���X�V���邱��
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean insertData()
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        int result;

        ret = false;

        query = "INSERT  hh_user_point_history" +
                " SET user_id = ?, " +
                " manage_year = ?," +
                " point = ?, " +
                " plus_point = ?, " +
                " minus_point = ?, " +
                " lost_point = ?, " +
                " last_update = ?, " +
                " last_uptime = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, this.manageYear );
            prestate.setInt( 3, this.point );
            prestate.setInt( 4, this.plusPoint );
            prestate.setInt( 5, this.minusPoint );
            prestate.setInt( 6, this.lostPoint );
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
            Logging.error( "[DataUserPointHistory.updateData] Exception=" + e.toString() );
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
     * ���[�U�|�C���g�����f�[�^�X�V
     * 
     * @param userId ���[�UID
     * @param manageYear �Ǘ��N�x
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateData(String userId, int manageYear)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        int result;

        ret = false;

        query = "UPDATE  hh_user_point_history" +
                " SET point = ?, " +
                " plus_point = ?, " +
                " minus_point = ?, " +
                " lost_point = ?, " +
                " last_update = ?, " +
                " last_uptime = ? " +
                " WHERE user_id = ? AND manage_year = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.point );
            prestate.setInt( 2, this.plusPoint );
            prestate.setInt( 3, this.minusPoint );
            prestate.setInt( 4, this.lostPoint );
            prestate.setInt( 5, this.lastUpdate );
            prestate.setInt( 6, this.lastUptime );
            prestate.setString( 7, userId );
            prestate.setInt( 8, manageYear );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointHistory.insertData] Exception=" + e.toString() );
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
