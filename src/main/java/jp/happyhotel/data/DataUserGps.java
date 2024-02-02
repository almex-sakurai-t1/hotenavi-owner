package jp.happyhotel.data;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ���[�U�[���݈ʒu���擾�N���X
 * 
 * @author Keion.Park
 */

public class DataUserGps
{

    public static final String TABLE     = "hh_user_gps";
    private String             userId;                   // ���[�U�[ID
    private int                seq;
    private int                registDate;               // �o�^���t(YYYYMMDD)
    private int                registTime;               // �o�^����(HHMMSS)
    private String             lat;                      // �ܓx
    private String             lon;                      // �o�x
    private String             url;                      // url
    private String             userAgent;                // ���[�U�[�G�[�W�F���g
    final int                  START_IDX = 0;
    final int                  END_IDX   = 255;

    /**
     * �f�[�^�����������܂��B
     */
    public DataUserGps()
    {
        this.userId = "";
        this.seq = 0;
        this.registDate = 0;
        this.registTime = 0;
        this.lat = "";
        this.lon = "";
        this.url = "";
        this.userAgent = "";
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getSeq()
    {
        return seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public String getLat()
    {
        return lat;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public String getLon()
    {
        return lon;
    }

    public void setLon(String lon)
    {
        this.lon = lon;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }

    /****
     * ���[�U�[�ʒu�f�[�^(hh_user_gps)�擾
     * 
     * @param errorSeq �G���[�A��
     * @return
     */
    public boolean getData(String userId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM hh_user_gps WHERE user_id=? AND seq = ?  ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    setData( result );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserGps.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * �G���[����(ap_error_history)�ݒ�
     * 
     * @param result �}�X�^�[�\�[�g���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.seq = result.getInt( "seq" );
                this.userId = result.getString( "user_id" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.lat = result.getBigDecimal( "lat" ).toString();
                this.lon = result.getBigDecimal( "lon" ).toString();
                this.url = result.getString( "url" );
                this.userAgent = result.getString( "user_agent" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserGps.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ���[�U�[���݈ʒu���(hh_user_gps)�}��
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */

    public boolean insertData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        BigDecimal Lat = null;
        BigDecimal Lon = null;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT hh_user_gps SET ";
        query += " user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", lat=?";
        query += ", lon=?";
        query += ", url=?";
        query += ", user_agent=?";
        Lat = new BigDecimal( this.lat );
        Lon = new BigDecimal( this.lon );

        if ( this.userAgent != null )
        {
            if ( this.userAgent.length() > END_IDX )
            {
                this.userAgent = this.userAgent.substring( START_IDX, END_IDX );
            }
        }

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setBigDecimal( i++, Lat );
            prestate.setBigDecimal( i++, Lon );
            prestate.setString( i++, this.url );
            prestate.setString( i++, this.userAgent );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserGps.insertData] Exception=" + e.toString() );
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
